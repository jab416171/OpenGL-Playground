package Lessons.LessonThree;

import static org.lwjgl.opengl.ARBShaderObjects.glCompileShaderARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateShaderObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glShaderSourceARB;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBVertexShader;

public class ShaderUtilities {

	private static final String SHADERS_ROOT_PATH="shaders_lesson3";

	public static int createFragmentShader(String pathToShaderFile) {
		int fragmentShader=createShader(pathToShaderFile, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		return fragmentShader;
	}

	public static int createVertexShader(String pathToShaderFile) {
		int vertexShader=createShader(pathToShaderFile, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		return vertexShader;
	}

	public static int createShader(String shaderFileName, int shaderType) {
		String shaderFilePath=SHADERS_ROOT_PATH+File.separator+shaderFileName;
		int newShader=glCreateShaderObjectARB(shaderType);
		if(newShader!=0) {
			String shaderCode=readInShaderCodeFromShaderFile(shaderFilePath);
			if(shaderCode!=null && !shaderCode.isEmpty()) {
				glShaderSourceARB(newShader, shaderCode);
				glCompileShaderARB(newShader);
				if(isShaderValid(newShader)) {
					return newShader;
				}
			}
		}
		//if anything fails, return 0 as error code
		return 0;
	}

	private static String readInShaderCodeFromShaderFile(String pathToShaderFile) {
		try {
			BufferedReader bufferedReader=new BufferedReader(new FileReader(pathToShaderFile));
			StringBuffer shaderCodeBuffer=new StringBuffer();
			String codeLine=bufferedReader.readLine();
			while(codeLine!=null) {
				shaderCodeBuffer.append(codeLine+"\n");
				codeLine=bufferedReader.readLine();
			}
			bufferedReader.close();
			return shaderCodeBuffer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isShaderValid(int shaderToCheck) {
		int status=glGetShader(shaderToCheck, GL_COMPILE_STATUS);
		return status == GL_TRUE;
	}

}
