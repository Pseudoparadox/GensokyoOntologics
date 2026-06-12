#version 330

layout(std140) uniform AppData{
  mat4 ModelViewMat;
  mat4 ProjMat;
};

layout(std140) uniform SphereData {
  vec4  Color;       // rgb, a
  vec3  Center;      // world-space center of this shell
  vec2  Tilling;
  vec2  Offset;
  float CellDensity;
};

layout(location = 0) in vec3 Position; // unit-sphere OR obj-vertex in LOCAL space
layout(location = 1) in vec2 UV0;
layout(location = 2) in vec3 Normal;

out vec3 vWorldPos;
out vec3 vNormal;
out vec2 vUV;

void main() {
  // Position 是 local → 变世界坐标
  vec3 worldPos = Center + Position;

  gl_Position = ProjMat * ModelViewMat * vec4(worldPos, 1.0);
  // 对 glow/additive shell：轻微 bias 远离表面防 z-fight
  gl_Position.z -= 0.00006 * gl_Position.w;

  vWorldPos = worldPos;
  vNormal   = Normal;
  // UV from sphere-normal（无 UV 的 OBJ 也适用）
  vec3 n = normalize(Normal);
  float u = atan(n.z, n.x) * 0.15915494309189535 + 0.5;
  float v = acos(clamp(n.y,-1.0,1.0)) * 0.3183098861837907;
  vUV.x = fract((u * Tilling.x + Offset.x) * 1.0) * 1.0;
  vUV.y = fract((v * Tilling.y + Offset.y) * 1.0);
}