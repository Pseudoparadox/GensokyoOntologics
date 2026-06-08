#version 150

const int RETURN_DISTANCE = 0;
const int RETURN_DISTANCE2 = 1;
const int RETURN_DISTANCE_SUB = 2;
const int RETURN_DISTANCE_DIV = 3;
const int RETURN_DISTANCE_ID = 4;

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
// 直接返回float的细胞噪声
// mode: 0=F1, 1=F2, 2=F2-F1, 3=F1/F2, 4=ID
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

    if (mode == RETURN_DISTANCE) return m1;              // F1
    if (mode == RETURN_DISTANCE2) return m2;              // F2
    if (mode == RETURN_DISTANCE_SUB) return m2 - m1;         // 边缘
    if (mode == RETURN_DISTANCE_DIV) return m1 / (m2 + 0.001); // 距离比
    if (mode == RETURN_DISTANCE_ID) return id;              // ID

    return m1;  // 默认返回F1
}

float cellular2DFBM(vec2 p, int mode, float scale, int octaves, float lacunarity, float persistence, float seed, float jitter) {
    vec3 result = vec3(0.0);
    float f1 = 0.0;
    float f2 = 0.0;
    float id = 0.0;

    float amplitude = 1.0;
    float frequency = 1.0;
    float maxValue = 0.0;

    for (int i = 0; i < octaves; i++) {
        vec3 noise = cellular2D(p * frequency * scale, seed + float(i), jitter);

        // 使用不同的细胞属性
        float value = 0.0;
        if (i == 0) {
            value = noise.x;  // 第一倍频使用 F1
        } else {
            value = noise.y;  // 后续倍频使用 F2
        }

        result.x += value * amplitude;  // F1
        result.y += noise.y * amplitude; // F2
        result.z += noise.z * amplitude; // ID

        maxValue += amplitude;
        amplitude *= persistence;
        frequency *= lacunarity;
    }

    // 归一化
    result /= maxValue;
    if (mode == RETURN_DISTANCE) return result.x;              // F1
    if (mode == RETURN_DISTANCE2) return result.y;              // F2
    if (mode == RETURN_DISTANCE_SUB) return result.x - result.y;         // 边缘
    if (mode == RETURN_DISTANCE_DIV) return result.x / (result.y + 0.001); // 距离比
    if (mode == RETURN_DISTANCE_ID) return result.z;              // ID

    return result.x;  // 默认返回F1
}