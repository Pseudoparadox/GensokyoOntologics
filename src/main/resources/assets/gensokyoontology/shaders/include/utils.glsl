#version 120

vec2 tilingAndOffset(vec2 uv, vec2 tiling, vec2 offset){
    return uv * tiling + offset;
}

float fresnel(vec3 viewDir, vec3 normal, float power) {
    float cosTheta = abs(dot(viewDir, normal));
    return pow(1.0 - cosTheta, power);
}