package Lessons.LessonFive;

import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glValidateProgramARB;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Main {

	private Cube cube;
	private int shaderProgram, vertexShader, fragmentShader;
	private boolean useShaders;
	private Vector3f cameraPosition, cameraDirection;
	private Vector2f pointerPosition;

	public void start() {
		try {
			DisplayMode displayMode=new DisplayMode(800, 600);
			Display.setDisplayMode(displayMode);
			Display.setVSyncEnabled(true);
			Display.setTitle("Modern OpenGL LWJGL Port Lesson Five");
			Display.create();
			pointerPosition=new Vector2f(0,0);
		} catch (LWJGLException exception) {
			exception.printStackTrace();
			System.exit(0);
		}

		init();

		// main loop
		while (!Display.isCloseRequested()) {
			long timeElapsed=Sys.getTime();
			cube.update(timeElapsed);

			readInUserInput();

			render();
			Display.update();
			Display.sync(60);
		}

		// close down
		Display.destroy();
		System.exit(0);
	}

	private void readInUserInput() {
		// TODO
	}

	private void init() {
		initOpenGL();
		initShaders();
		initWorld();
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
		if (useShaders) { // use the shader linked
			glUseProgramObjectARB(shaderProgram);
		}
	}

	private void initWorld() {
		cameraPosition=new Vector3f(0,2,0);
		cameraDirection=new Vector3f(0, 0, -4);

		if(useShaders) {
			Cube.setShaderProgram(shaderProgram);
		}
		cube=new Cube();
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

		//camera adjustment
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		gluLookAt(cameraPosition.x, cameraPosition.y, cameraPosition.z,
				cameraDirection.x, cameraDirection.y, cameraDirection.z,
				0, 1, 0);

		// world rendering code
		cube.draw();
	}

	private void initOpenGL() {
		// setup viewing area
		DisplayMode displayMode=Display.getDisplayMode();
		int width=displayMode.getWidth();
		int height=displayMode.getHeight();
		glViewport(0, 0, width, height);

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // white
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		// setup the lens params [projection matrix]
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f, (float)width/(float)height, 0.1f, 10f);
	}

	public static void main(String[] args) {
		Main lessonOne=new Main();
		lessonOne.start();
	}

}
