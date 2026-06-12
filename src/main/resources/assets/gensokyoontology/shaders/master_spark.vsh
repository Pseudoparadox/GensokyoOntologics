#version 330

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

layout(std140) uniform SparkData{
    vec2 Offset;
    vec2 Tilling;
    float CellDensity;
};

layout(location = 0) in vec3 Position; // unit-sphere OR obj-vertex in LOCAL space
layout(location = 1) in vec2 UV0;
layout(location = 2) in vec3 Normal;

out float density;
out vec2 offset;
out vec2 tilling;
out vec4 beamColor;

out vec3 view;
out vec3 normal;
out vec2 texCoord0;

void main() {

    vec3 pos = Position;

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    gl_Position.z = gl_Position.z * 0.999;  // 轻微偏移，避免深度冲突

    normal = Normal;
    offset = Offset;
    density = CellDensity;
    tilling = Tilling;
    texCoord0 = UV0;
}
