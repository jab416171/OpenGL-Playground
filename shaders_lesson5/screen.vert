#version 120

attribute vec3 coords;
attribute vec3 color;
uniform vec3 modelPosition;
uniform vec3 modelRotation;
varying vec3 fragmentColor;

void main(void) {
	mat4x4 positionMatrix=mat4x4(1.0);
	positionMatrix[3].x=modelPosition.x;
	positionMatrix[3].y=modelPosition.y;
	positionMatrix[3].z=modelPosition.z;
	
	mat4x4 heading=mat4x4(1.0);
	heading[0][0]=cos(modelRotation.y);
	heading[0][2]=-(sin(modelRotation.y));
	heading[2][0]=sin(modelRotation.y);
	heading[2][2]=cos(modelRotation.y);
	mat4x4 pitch=mat4x4(1.0);
	pitch[1][1]=cos(modelRotation.x);
	pitch[1][2]=sin(modelRotation.x);
	pitch[2][1]=-(sin(modelRotation.x));
	pitch[2][2]=cos(modelRotation.x);
	mat4x4 roll=mat4x4(1.0);
	roll[0][0]=cos(modelRotation.z);
	roll[0][1]=sin(modelRotation.z);
	roll[1][0]=-(sin(modelRotation.z));
	roll[1][1]=cos(modelRotation.z);
	mat4x4 rotationMatrix=mat4x4(1.0);
	rotationMatrix=heading*pitch*roll;
	
	gl_Position=gl_ModelViewProjectionMatrix*positionMatrix*rotationMatrix*vec4(coords, 1.0);
	
	fragmentColor=color;
}