#version 150

in float density;
in vec2 offset;
in vec2 tilling;
in vec4 beamColor;

in vec3 view;
in vec3 normal;
in vec2 texCoord0;

out vec4 fragColor;

const int RETURN_DISTANCE = 0;
const int RETURN_DISTANCE2 = 1;
const int RETURN_DISTANCE_SUB = 2;
const int RETURN_DISTANCE_DIV = 3;
const int RETURN_CELL_VALUE = 4;

// 快速 2D 哈希函数
vec2 hash22(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(0.1031, 0.1030, 0.0973));
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx + p3.yz) * p3.zy);
}

// 快速 3D 哈希函数
vec3 hash33(vec3 p) {
    p = fract(p * vec3(0.1031, 0.1030, 0.0973));
    p += dot(p, p.yxz + 33.33);
    return fract((p.xxy + p.yzz) * p.zyx);
}

// 平滑最小值（用于混合距离）
float smin(float a, float b, float k) {
    float h = clamp(0.5 + 0.5 * (b - a) / k, 0.0, 1.0);
    return mix(b, a, h) - k * h * (1.0 - h);
}

// 平滑最大值
float smax(float a, float b, float k) {
    return -smin(-a, -b, k);
}

// ==================== 经典细胞噪声 ====================

// 2D 细胞噪声 - 基本版本
// 返回：F1（最近距离），F2（第二近距离），id（最近特征点ID）
float cellular2D(vec2 p, int mode, float seed, float jitter) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    float m1 = 8.0;  // 最近距离
    float m2 = 8.0;  // 第二近距离
    vec2 id1 = vec2(0.0);

    // 3x3网格搜索
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 neighbor = vec2(float(x), float(y));
            vec2 point = hash22(i + neighbor + seed);
            point = mix(vec2(0.5), point, jitter);

            vec2 diff = neighbor + point - f;
            float dist = dot(diff, diff);

            if (dist < m1) {
                m2 = m1;
                m1 = dist;
                id1 = i + neighbor;
            } else if (dist < m2) {
                m2 = dist;
            }
        }
    }

    // 计算最终值
    m1 = sqrt(m1);
    m2 = sqrt(m2);
    float id = hash22(id1).x;

    if (mode == 0) return m1;              // F1
    if (mode == 1) return m2;              // F2
    if (mode == 2) return m2 - m1;         // 边缘
    if (mode == 3) return m1 / (m2 + 0.001); // 距离比
    if (mode == 4) return id;              // ID

    return m1;  // 默认返回F1
}

float cellular2DFBM(vec2 p, int mode, float scale, int octaves, float lacunarity, float gain) {
    vec3 result = vec3(0.0);
    float f1 = 0.0;
    float f2 = 0.0;
    float id = 0.0;

    float amplitude = 1.0;
    float frequency = 1.0;
    float maxValue = 0.0;

    for (int i = 0; i < octaves; i++) {
        float noise = cellular2D(p * frequency * scale, mode, 155776.0 + i, 1.0F);

        // 使用不同的细胞属性
        float value = 0.0;
        if (i == 0) {
            f1 = noise;  // 第一倍频使用 F1
        } else {
            f2 = noise;  // 后续倍频使用 F2
        }

        result.x += f1 * amplitude;  // F1
        result.y += f2 * amplitude; // F2

        maxValue += amplitude;
        amplitude *= gain;
        frequency *= lacunarity;
    }

    // 归一化
    result /= maxValue;
    if (mode == RETURN_DISTANCE) return result.x;              // F1
    if (mode == RETURN_DISTANCE2) return result.y;              // F2
    if (mode == RETURN_DISTANCE_SUB) return result.x - result.y;         // 边缘
    if (mode == RETURN_DISTANCE_DIV) return result.x / (result.y + 0.001); // 距离比
    if (mode == RETURN_CELL_VALUE) return result.z;              // ID

    return result.x;  // 默认返回F1
}

vec2 tilingAndOffset(vec2 uv, vec2 tiling, vec2 offset){
    return vec2(fract(uv.x * tilling.x) + offset.x, fract(uv.y * tilling.y) + offset.y);
}

vec3 hsv2rgb(float h, float s, float v) {
    vec3 c = vec3(h * 6.0, s, v);
    vec3 rgb = clamp(
    abs(mod(c.x + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0,
    0.0, 1.0
    );
    return c.z * mix(vec3(1.0), rgb, c.y);
}

vec3 getHueFromAngle(float angleDeg){
    float h = mod(angleDeg, 360.0) / 360.0;
    return hsv2rgb(h, 1.0, 1.0);  // 饱和度和亮度拉满 → 纯色环色
}

vec3 generateColorStrip(vec2 uv){
    return getHueFromAngle(mod(uv.x, 360.F));
}

// 光柱
void main() {
    vec2 uv = vec2(texCoord0.x + offset.x, texCoord0.y + offset.y);
    float noise = cellular2DFBM(uv, RETURN_DISTANCE, density, 4, 2.0F, 2.0F);
    vec3 color = generateColorStrip(uv);

    fragColor = vec4(color.xyz * noise, 0.8F);
    if(density == 0.0F) {
        fragColor = vec4(beamColor.xyz, 0.76F);
    }
}
