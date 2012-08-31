#version 120

attribute vec3 coord3D;
attribute vec3 vColor;
varying vec3 fColor;
uniform float fade;
uniform mat4 transformMatrix;

void main(void) {
	gl_Position = transformMatrix * vec4(coord3D, 1.0);
	fColor = vColor;
}