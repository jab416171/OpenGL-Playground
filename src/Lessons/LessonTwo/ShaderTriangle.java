package Lessons.LessonTwo;

import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glDrawRangeElements;

public class ShaderTriangle {

	private int shader, vertexShader, fragmentShader;
	private boolean useShaders;
	private int vboVertexAttributes, vboVertexIndices;
	private int vboVertexAttributesSize, vboVertexIndicesSize;
	private final int numVertices=3, numAxisPerVertex=2;
	private int coord2dAttributeIndex;

	public ShaderTriangle() {
		this.shader = 0;
		this.vertexShader = 0;
		this.fragmentShader = 0;
		this.useShaders = false;
		this.initShaders();
		this.initBuffers();

		String coord2dAttributeName="coord2d";
		this.coord2dAttributeIndex=GL20.glGetAttribLocation(this.shader, coord2dAttributeName);
	}

	private void initBuffers() {
		this.vboVertexAttributes=glGenBuffersARB();
		this.vboVertexIndices=glGenBuffersARB();
		int bytesPerFloat=Float.SIZE/8;
		int bytesPerInt=Integer.SIZE/8;
		vboVertexAttributesSize=bytesPerFloat*numVertices*numAxisPerVertex;
		vboVertexIndicesSize=bytesPerInt*numVertices;

		// set size of vertex attributes buffer
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, this.vboVertexAttributes);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vboVertexAttributesSize, GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes = glMapBufferARB(
				GL_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB,
				vboVertexAttributesSize, null);
		//put int vertex position data
		float[] vertexPositions=new float[] {
				0.0f, 0.8f,  //v1
				0.8f, -0.8f, //v2
				-0.8f, -0.8f //v3
		};
		vertexPositionAttributes.asFloatBuffer().put(vertexPositions);
		//flip buffer, unmap and unbind
		vertexPositionAttributes.flip();
		glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		// set size of vertex index buffer
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, this.vboVertexIndices);
		glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vboVertexIndicesSize, GL_STATIC_DRAW_ARB);
		ByteBuffer vertexIndeces = glMapBufferARB(
				GL_ELEMENT_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB,
				vboVertexIndicesSize, null);
		//put in vertex index data
		int[] indeces=new int[] {0, 1, 2};
		vertexIndeces.asIntBuffer().put(indeces);
		//flip buffer, unmap and unbind
		vertexIndeces.flip();
		glUnmapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	}

	private void initShaders() {
		// create the main shader program
		this.shader = glCreateProgramObjectARB();
		if (this.shader != 0) { // if passed, create sub shaders
			this.vertexShader = ShaderUtilities
					.createVertexShader("screen.vert");
			this.fragmentShader = ShaderUtilities
					.createFragmentShader("screen.frag");
			if (this.vertexShader != 0 && this.fragmentShader != 0) {
				// add in shaders to main program
				glAttachObjectARB(this.shader, this.vertexShader);
				glAttachObjectARB(this.shader, this.fragmentShader);
				glLinkProgramARB(this.shader);
				glValidateProgramARB(this.shader);
				this.useShaders = true;
			}
		}
	}

	public void draw() {
		if(this.useShaders) {
			glUseProgramObjectARB(this.shader);
		}

		//bind vertex array
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, this.vboVertexAttributes);
		//pass in coords info to vertex shader
		GL20.glEnableVertexAttribArray(coord2dAttributeIndex);
		GL20.glVertexAttribPointer(coord2dAttributeIndex, 2, GL_FLOAT, false, 0, 0);

		//draw the vertices using the indices
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, this.vboVertexIndices);
		glDrawRangeElements(GL_TRIANGLES, 0, this.numVertices-1, this.numVertices, GL_UNSIGNED_INT, 0);

		//unbind the buffers
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

		glUseProgramObjectARB(0);
	}
}
