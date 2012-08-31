#version 120

attribute vec2 coord2D;
attribute vec3 vColor;
varying vec3 fColor;
uniform float fade;

void main(void) {
	gl_Position = vec4(coord2D, 0.0, 1.0);
	fColor = vColor;
}