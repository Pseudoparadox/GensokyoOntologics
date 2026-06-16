#version 330

#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform vec2 Offset;
uniform vec2 Tilling;
uniform float CellDensity;

in vec3 Position; // unit-sphere OR obj-vertex in LOCAL space
in vec2 UV0;
in vec3 Normal;

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
