#version 120

varying vec3 fColor;
uniform float fade;
uniform mat4 transformMatrix;

void main(void) {
	gl_FragColor = vec4(fColor.x, fColor.y, fColor.z, fade);
}