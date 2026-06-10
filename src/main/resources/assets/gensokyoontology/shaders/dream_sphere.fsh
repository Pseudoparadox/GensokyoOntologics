#version 150 core

in float density;
in vec2 offset;
in vec2 tilling;
in vec4 sphereColor;

in vec3 view;
in vec3 normal;
in vec2 texCoord0;
in float u_time;  // 时间变量，用于动画

out vec4 fragColor;

const int RETURN_DISTANCE = 0;
const int RETURN_DISTANCE2 = 1;
const int RETURN_DISTANCE_SUB = 2;
const int RETURN_DISTANCE_DIV = 3;
const int RETURN_CELL_VALUE = 4;

// ==================== 菲涅尔计算 ====================
float fresnel(vec3 viewDir, vec3 normal, float power) {
    float cosTheta = abs(dot(viewDir, normal));
    return pow(1.0 - cosTheta, power);
}

// ==================== 噪声函数 ====================
vec2 hash22(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(0.1031, 0.1030, 0.0973));
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx + p3.yz) * p3.zy);
}

vec3 hash33(vec3 p) {
    p = fract(p * vec3(0.1031, 0.1030, 0.0973));
    p += dot(p, p.yxz + 33.33);
    return fract((p.xxy + p.yzz) * p.zyx);
}

// ==================== 湍流函数 ====================
float turbulence(vec2 p, float time) {
    float sum = 0.0;
    float freq = 1.0;
    float amp = 1.0;
    float maxSum = 0.0;

    // 多层噪声叠加创建湍流效果
    for(int i = 0; i < 5; i++) {
        // 添加时间动画，使图案向外扩散
        vec2 q = p * freq + vec2(time * 0.5 * freq, 0.0);
        float noise = cellular2D(q, RETURN_DISTANCE, 12345.0 + i * 100.0, 1.0);

        // 使用不同的混合模式创建更复杂的图案
        sum += amp * sin(noise * 6.28318 + time * 2.0);
        maxSum += amp;
        freq *= 2.0;
        amp *= 0.5;
    }

    return sum / maxSum;
}

// ==================== 圆形扩散图案 ====================
float circularPattern(vec2 uv, float time) {
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(uv, center);

    // 创建多个扩散波
    float waves = 0.0;
    int numWaves = 3;

    for(int i = 0; i < numWaves; i++) {
        float speed = 0.8 + float(i) * 0.3;
        float phase = time * speed + float(i) * 2.094; // 2π/3 相位差
        float wave = sin(dist * 15.0 - phase) * 0.5 + 0.5;

        // 使用指数衰减使波纹逐渐消失
        float attenuation = exp(-dist * 3.0);
        waves += wave * attenuation;
    }

    return waves / float(numWaves);
}

// ==================== 细胞噪声函数 ====================
float cellular2D(vec2 p, int mode, float seed, float jitter) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float m1 = 8.0, m2 = 8.0;
    vec2 id1 = vec2(0.0);

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
    m1 = sqrt(m1);
    m2 = sqrt(m2);
    float id = hash22(id1).x;
    if (mode == 0) return m1;
    if (mode == 1) return m2;
    if (mode == 2) return m2 - m1;
    if (mode == 3) return m1 / (m2 + 0.001);
    if (mode == 4) return id;
    return m1;
}

float cellular2DFBM(vec2 p, int mode, float scale, int octaves, float lacunarity, float gain) {
    float f1 = 0.0, f2 = 0.0;
    float amplitude = 1.0, frequency = 1.0, maxValue = 0.0;
    for (int i = 0; i < octaves; i++) {
        float noise = cellular2D(p * frequency * scale, mode, 155776.0 + i, 1.0F);
        if (i == 0) f1 = noise;
        else f2 = noise;
        amplitude *= gain;
        frequency *= lacunarity;
        maxValue += amplitude;
    }
    if (mode == RETURN_DISTANCE) return f1 / maxValue;
    if (mode == RETURN_DISTANCE2) return f2 / maxValue;
    if (mode == RETURN_DISTANCE_SUB) return (f1 - f2) / maxValue;
    if (mode == RETURN_DISTANCE_DIV) return f1 / (f2 + 0.001);
    return f1 / maxValue;
}

void main() {
    vec2 uv = vec2(texCoord0.x + offset.x, texCoord0.y + offset.y);

    // 计算菲涅尔效应
    vec3 viewDir = normalize(view);
    vec3 norm = normalize(normal);
    float fresnelFactor = fresnel(viewDir, norm, 3.0);

    // 基础噪声
    float baseNoise = cellular2DFBM(uv, RETURN_DISTANCE, density, 4, 2.0F, 2.0F);

    // 圆形扩散图案
    float circlePattern = circularPattern(uv, u_time);

    // 湍流效果
    float turb = turbulence(uv * 3.0, u_time);

    // 组合所有效果
    float combinedPattern = baseNoise * 0.4 + circlePattern * 0.4 + turb * 0.2;
    combinedPattern = clamp(combinedPattern, 0.0, 1.0);

    // 颜色计算
    vec3 baseColor = sphereColor.xyz * combinedPattern;

    // 菲涅尔辉光叠加
    vec3 glowColor = sphereColor.xyz * fresnelFactor * 0.8;
    vec3 finalColor = baseColor + glowColor;

    // 动态透明度：中心较透明，边缘较不透明
    float alpha = combinedPattern * (0.4 + fresnelFactor * 0.6);
    alpha = clamp(alpha, 0.0, 0.9);

    // 添加脉冲效果
    float pulse = sin(u_time * 2.0) * 0.1 + 0.9;
    finalColor *= pulse;

    if (density == 0.0F) {
        fragColor = vec4(sphereColor.xyz, 0.76F);
    } else {
        fragColor = vec4(finalColor, alpha);
    }
}