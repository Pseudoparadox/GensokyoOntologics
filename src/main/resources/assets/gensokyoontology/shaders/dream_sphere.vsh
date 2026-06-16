#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

in vec3 Position; // unit-sphere OR obj-vertex in LOCAL space
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

out vec3 view;
out vec3 normal;
out vec2 texCoord0;

out float time;
out float density;
out vec2 offset;
out vec2 tilling;
out vec4 sphereColor;

void main() {
  gl_Position = ProjMat * ModelViewMat * vec4(Position + ModelOffset, 1.0f);

  view = Position;
  normal   = Normal;
  // UV from sphere-normal（无 UV 的 OBJ 也适用）

  vec2 tilling = vec2(1F, 1F);
  vec2 offset = vec2(0F, 0F);

  vec3 n = normalize(Normal);
  float u = atan(n.z, n.x) * 0.15915494309189535 + 0.5;
  float v = acos(clamp(n.y,-1.0,1.0)) * 0.3183098861837907;

  texCoord0.x = fract((u * tilling.x + offset.x) * 1.0) * 1.0;
  texCoord0.y = fract((v * tilling.y + offset.y) * 1.0);

  sphereColor = Color;
  time = GameTime;
}