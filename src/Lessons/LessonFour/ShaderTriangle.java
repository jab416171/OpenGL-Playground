package Lessons.LessonFour;

import static org.lwjgl.opengl.ARBBufferObject.GL_STATIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.GL_WRITE_ONLY_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBBufferObject.glMapBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glUnmapBufferARB;
import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glValidateProgramARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL12.glDrawRangeElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ShaderTriangle {

	private int shaderProgram, vertexShader, fragmentShader;
	private boolean useShaders;
	private int vertexAttributesBuffer, vertexIndicesBuffer;
	private int vertexAttributesBufferSize, vertexIndicesBufferSize;
	private final int numVertices=3, numAxisPerVertex=3, numColoursPerVertex=3;
	private int vColorAttributeIndex, coord3DAttributeIndex,
			fadeAttributeIndex, transformMatrixAttributeIndex;
	private FloatBuffer transformationValues;
	private final int bytesPerFloat=Float.SIZE/8, bytesPerInt=Integer.SIZE/8;
	private int stride;
	private float currentFade;

	public ShaderTriangle() {
		shaderProgram=0;
		vertexShader=0;
		fragmentShader=0;
		useShaders=false;
		initShaders();
		initShaderAttributes();
		initBuffers();
	}

	public void update(long timeElapsed) {
		currentFade=(float)(Math.sin(timeElapsed / 1000.0 * (2*3.14) / 5) / 2 + 0.5);
		float movement=(float)Math.sin(timeElapsed/1000.0 * (2.314)/5);
		float angle=(float)(timeElapsed/100000.0*45)%360;
		Vector3f zAxis=new Vector3f(0f, 0f, 1f);
		Matrix4f translationMatrix=new Matrix4f();
		translationMatrix.setIdentity();
		translationMatrix.translate(new Vector3f(movement, 0f, 0f));
		Matrix4f rotationMatrix=new Matrix4f();
		rotationMatrix.setIdentity();
		rotationMatrix.rotate(angle, zAxis);
		Matrix4f transformationMatrix=Matrix4f.mul(translationMatrix, rotationMatrix, null);
		transformationValues.clear();
		transformationMatrix.store(transformationValues);
		transformationValues.flip();
	}

	private void initShaderAttributes() {
		String coord3DAttributeName="coord3D";
		coord3DAttributeIndex=glGetAttribLocation(shaderProgram,
				coord3DAttributeName);
		String vColorAttributeName="vColor";
		vColorAttributeIndex=glGetAttribLocation(shaderProgram,
				vColorAttributeName);
		String fadeAttributeName="fade";
		fadeAttributeIndex=glGetUniformLocation(shaderProgram,
				fadeAttributeName);
		String transformMatrixAttributeName="transformMatrix";
		transformMatrixAttributeIndex=glGetUniformLocation(shaderProgram,
				transformMatrixAttributeName);
		transformationValues=BufferUtils.createFloatBuffer(16);
	}

	private void initBuffers() {
		setupVertexAttributesBuffer();
		setupVertexIndecesBuffer();
	}

	private void setupVertexIndecesBuffer() {
		vertexIndicesBuffer=glGenBuffersARB();
		vertexIndicesBufferSize=bytesPerInt*numVertices;
		// set size of vertex index buffer
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBuffer);
		glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBufferSize,
				GL_STATIC_DRAW_ARB);
		ByteBuffer vertexIndeces=glMapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB,
				GL_WRITE_ONLY_ARB, vertexIndicesBufferSize, null);
		// put in vertex index data
		int[] indecesData=new int[] {
				0, 1, 2
		};
		vertexIndeces.asIntBuffer().put(indecesData);
		// flip buffer, unmap and unbind
		vertexIndeces.flip();
		glUnmapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	}

	private void setupVertexAttributesBuffer() {
		vertexAttributesBuffer=glGenBuffersARB();
		vertexAttributesBufferSize=bytesPerFloat*numVertices
				*(numAxisPerVertex+numColoursPerVertex);
		// set size of vertex attributes buffer
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBuffer);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBufferSize,
				GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes=glMapBufferARB(GL_ARRAY_BUFFER_ARB,
				GL_WRITE_ONLY_ARB, vertexAttributesBufferSize, null);
		// put int vertex position & color data
		float[] vertexAttributesData=new float[] {
				0.0f, 0.8f, 0.0f, 1.0f, 1.0f, 0.0f,
				0.8f, -0.8f, 0.0f, 0.0f, 0.0f, 1.0f,
				-0.8f, -0.8f, 0.0f, 1.0f, 0.0f, 0.0f
		};
		vertexPositionAttributes.asFloatBuffer().put(vertexAttributesData);
		// flip buffer, unmap and unbind
		vertexPositionAttributes.flip();
		glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		// setup the stride and color offset
		stride=(numAxisPerVertex+numColoursPerVertex)*bytesPerFloat;
	}

	private void initShaders() {
		// create the main shader program
		shaderProgram=glCreateProgramObjectARB();
		if (shaderProgram!=0) { // if passed, create sub shaders
			vertexShader=ShaderUtilities.createVertexShader("screen.vert");
			fragmentShader=ShaderUtilities.createFragmentShader("screen.frag");
			if (vertexShader!=0&&fragmentShader!=0) {
				// add in shaders to main program
				glAttachObjectARB(shaderProgram, vertexShader);
				glAttachObjectARB(shaderProgram, fragmentShader);
				glLinkProgramARB(shaderProgram);
				glValidateProgramARB(shaderProgram);
				useShaders=true;
			}
		}
	}

	public void draw() {
		if (useShaders) { // use the shader linked
			glUseProgramObjectARB(shaderProgram);
		}
		// bind vertex data array
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBuffer);

		//uniform shader attributes
		glUniform1f(fadeAttributeIndex, currentFade);
		glUniformMatrix4(transformMatrixAttributeIndex, false, transformationValues);

		// pass in info to vertex shader
		int dataOffset=0;
		glEnableVertexAttribArray(coord3DAttributeIndex);
		glVertexAttribPointer(coord3DAttributeIndex, numAxisPerVertex,
				GL_FLOAT, false, stride, 0);
		dataOffset+=numAxisPerVertex*bytesPerFloat;
		glEnableVertexAttribArray(vColorAttributeIndex);
		glVertexAttribPointer(vColorAttributeIndex, numColoursPerVertex,
				GL_FLOAT, false, stride, dataOffset);
		dataOffset+=numColoursPerVertex*bytesPerFloat;

		// draw the vertices using the indices
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBuffer);
		glDrawRangeElements(GL_TRIANGLES, 0, numVertices-1, numVertices,
				GL_UNSIGNED_INT, 0);

		// unbind the buffers
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

		glDisableVertexAttribArray(vColorAttributeIndex);

		glUseProgramObjectARB(0);
	}
}
