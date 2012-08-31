package Lessons.LessonFive;

import static org.lwjgl.opengl.ARBBufferObject.GL_STATIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.GL_WRITE_ONLY_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBBufferObject.glMapBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glUnmapBufferARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.util.Random;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Cube {

	private int vertexAttributesBuffer, vertexIndicesBuffer;
	private int vertexAttributesBufferSize, vertexIndicesBufferSize;
	private final int numVertices=8, numAxisPerVertex=3, numColoursPerVertex=3;
	private int numIndeces;
	private int colorAttributeIndex, coordsAttributeIndex,
			modelPositionAttributeIndex, modelRotationAttributeIndex;
	private final int bytesPerFloat=Float.SIZE/8, bytesPerInt=Integer.SIZE/8;
	private int stride;
	private Vector3f position, rotation;
	private static int shader;
    private static final Random random = new Random();
    private float[] vertexAttributesData;
    private int count;

    public Cube() {
		position=new Vector3f(0f, 0f, -4f);
		rotation=new Vector3f(0f, 0f, 0f);
		initShaderAttributes();
		initBuffers();
	}

	public void update(long timeElapsed) {

//        rotation.x-=(45.0/6000.0);
        rotation.y-=(45.0/6000.0);
        rotation.z-=(45.0/6000.0);

        setupVertexAttributesBuffer(timeElapsed);
	}

	private void initShaderAttributes() {
		String coordsAttributeName="coords";
		coordsAttributeIndex=glGetAttribLocation(shader, coordsAttributeName);
		String colorAttributeName="color";
		colorAttributeIndex=glGetAttribLocation(shader, colorAttributeName);
		String modelPositionAttributeName="modelPosition";
		modelPositionAttributeIndex=ARBShaderObjects.glGetUniformLocationARB(shader, modelPositionAttributeName);
		String modelRotationAttributeName="modelRotation";
		modelRotationAttributeIndex=ARBShaderObjects.glGetUniformLocationARB(shader, modelRotationAttributeName);
	}

	private void initBuffers() {
		setupVertexAttributesBuffer(0);
		setupVertexIndecesBuffer();
	}

	private void setupVertexIndecesBuffer() {
		vertexIndicesBuffer=glGenBuffersARB();
		// put in vertex index data
		int[] indecesData=new int[] {
				// front
				0, 1, 2, 2, 3, 0,
				// top
				1, 5, 6, 6, 2, 1,
				// back
				7, 6, 5, 5, 4, 7,
				// bottom
				4, 0, 3, 3, 7, 4,
				// left
				4, 5, 1, 1, 0, 4,
				// right
				3, 2, 6, 6, 7, 3,
		};
		// set index count for later rendering
		numIndeces=indecesData.length;
		vertexIndicesBufferSize=bytesPerInt*numIndeces;
		// set size of vertex index buffer
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBuffer);
		glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBufferSize,
				GL_STATIC_DRAW_ARB);
		ByteBuffer vertexIndeces=glMapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB,
				GL_WRITE_ONLY_ARB, vertexIndicesBufferSize, null);
		vertexIndeces.asIntBuffer().put(indecesData);
		// flip buffer, unmap and unbind
		vertexIndeces.flip();
		glUnmapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	}

	private void setupVertexAttributesBuffer(final long timeElapsed) {
		vertexAttributesBuffer=glGenBuffersARB();
		vertexAttributesBufferSize=bytesPerFloat*numVertices
				*(numAxisPerVertex+numColoursPerVertex);
		// set size of vertex attributes buffer
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBuffer);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBufferSize,
				GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes=glMapBufferARB(GL_ARRAY_BUFFER_ARB,
				GL_WRITE_ONLY_ARB, vertexAttributesBufferSize, null);
        int mod_count = 30;
        if(count++ % mod_count == 0) {
            // put int vertex position and color data
            vertexAttributesData = new float[] {
                //x, y, z, r, g, b
                // front
                -1f, -1f, 1f, random.nextFloat()*2-1,random.nextFloat()*2-1,random.nextFloat()*2-1,// bottom left
                1f, -1f, 1f, random.nextFloat()*2-1, random.nextFloat()*2-1, random.nextFloat()*2-1,// bottom right
                1f, 1f, 1f, random.nextFloat()*2-1, random.nextFloat()*2-1, random.nextFloat()*2-1,// top right
                -1f, 1f, 1f, random.nextFloat()*2-1, random.nextFloat()*2-1, random.nextFloat()*2-1,// top left
                // back
                -1f, -1f, -1f, random.nextFloat()*2-1, random.nextFloat()*2-1,random.nextFloat()*2-1,
                1f, -1f, -1f, random.nextFloat()*2-1, random.nextFloat()*2-1, random.nextFloat()*2-1,
                1f, 1f, -1f, random.nextFloat()*2-1,random.nextFloat()*2-1, random.nextFloat()*2-1,
                -1f, 1f, -1f, random.nextFloat()*2-1, random.nextFloat()*2-1, random.nextFloat()*2-1,
            };
        }
        if(count > mod_count * 1000 && count % mod_count == 0)
            count = 0;
		vertexPositionAttributes.asFloatBuffer().put(vertexAttributesData);
		// flip buffer, unmap and unbind
		vertexPositionAttributes.flip();
		glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		// setup the stride and color offset
		stride=(numAxisPerVertex+numColoursPerVertex)*bytesPerFloat;
	}

	public void draw() {
		// bind vertex data array
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexAttributesBuffer);

		//bind the uniform attributes
		ARBShaderObjects.glUniform3fARB(modelPositionAttributeIndex, position.x, position.y, position.z);
		ARBShaderObjects.glUniform3fARB(modelRotationAttributeIndex, rotation.x, rotation.y, rotation.z);

		// pass in info to vertex shader
		int dataOffset=0;
		glEnableVertexAttribArray(coordsAttributeIndex);
		glVertexAttribPointer(coordsAttributeIndex, numAxisPerVertex,
				GL_FLOAT, false, stride, 0);
		dataOffset+=numAxisPerVertex*bytesPerFloat;
		glEnableVertexAttribArray(colorAttributeIndex);
		glVertexAttribPointer(colorAttributeIndex, numColoursPerVertex,
				GL_FLOAT, false, stride, dataOffset);
		dataOffset+=numColoursPerVertex*bytesPerFloat;

		// draw the vertices using the indices
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, vertexIndicesBuffer);
		GL11.glDrawElements(GL_TRIANGLES, numIndeces, GL_UNSIGNED_INT, 0);

		// unbind the buffers
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

		glDisableVertexAttribArray(colorAttributeIndex);
	}

	public static void setShaderProgram(int shaderProgram) {
		shader=shaderProgram;
	}
}
