#version 120

varying vec3 fColor;
uniform float fade;

void main(void) {
	gl_FragColor = vec4(fColor.x, fColor.y, fColor.z, fade);
}