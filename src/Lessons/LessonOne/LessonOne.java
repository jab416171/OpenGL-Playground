package Lessons.LessonOne;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Joe
 * Date: 8/19/12
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonOne {

    private ShaderTriangle triangle;

    public void start() {
        try {
            DisplayMode displayMode = new DisplayMode(800,800);

            Display.setDisplayMode(displayMode);
            Display.setVSyncEnabled(true);
            Display.setTitle("Modern OpenGL LWJGL Port Lesson One");
            Display.create();
        } catch (LWJGLException exception) {
            exception.printStackTrace();
            System.exit(0);
        }

        this.init();

        // main loop
        while (!Display.isCloseRequested()) {
            this.render();
            Display.update();
        }

        // close down
        Display.destroy();
    }

    private void init() {
        this.initOpenGL();
        this.initWorld();
    }

    private void initWorld() {
        this.triangle=new ShaderTriangle();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        //world rendering code
        this.triangle.draw();
    }

    private void initOpenGL() {
        // setup viewing area
        DisplayMode displayMode = Display.getDisplayMode();
        int width = displayMode.getWidth();
        int height = displayMode.getHeight();
        glViewport(0, 0, width, height);
        // setup the camera params
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45.0f, (float)width / (float)height, 0.0f, 100f);
        // setup the model (drawing) params
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glShadeModel(GL_SMOOTH);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // white
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    public static void main(String[] args) {
        LessonOne lessonOne = new LessonOne();
        lessonOne.start();
    }
}
