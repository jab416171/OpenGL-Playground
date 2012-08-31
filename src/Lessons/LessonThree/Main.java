package Lessons.LessonThree;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public class Main {

	private ShaderTriangle triangle;

	public void start() {
		try {
			DisplayMode displayMode=new DisplayMode(640, 480);
			Display.setDisplayMode(displayMode);
			Display.setVSyncEnabled(true);
			Display.setTitle("Modern OpenGL LWJGL Port Lesson Three");
			Display.create();
		} catch (LWJGLException exception) {
			exception.printStackTrace();
			System.exit(0);
		}

		this.init();

		// main loop
		while (!Display.isCloseRequested()) {
			long timeElapsed=Sys.getTime();
			triangle.update(timeElapsed);
			this.render();
			Display.update();
			Display.sync(60);
		}

		// close down
		Display.destroy();
		System.exit(0);
	}

	private void init() {
		this.initOpenGL();
		this.initWorld();
	}

	private void initWorld() {
		this.triangle=new ShaderTriangle();
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		// world rendering code
		this.triangle.draw();
	}

	private void initOpenGL() {
		// setup viewing area
		DisplayMode displayMode=Display.getDisplayMode();
		int width=displayMode.getWidth();
		int height=displayMode.getHeight();
		glViewport(0, 0, width, height);
		// setup the camera params
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(45.0f, (float)width/(float)height, 1.0f, 100f);
		// setup the model (drawing) params
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glShadeModel(GL_SMOOTH);
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // white
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		// enable blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void main(String[] args) {
		Main lessonOne=new Main();
		lessonOne.start();
	}

}
