package Lessons.LessonOne;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBVertexShader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShader;

public class ShaderTriangle {

    private int shader, vertexShader, fragmentShader;
    private boolean useShaders;

    public static boolean verboseValidityCheck=false;
    private static final String SHADERS_ROOT_PATH="shaders_lesson1";
    private static Random random = new Random(0);

    public ShaderTriangle() {
        this.shader=0;
        this.vertexShader=0;
        this.fragmentShader=0;
        this.useShaders=false;
        this.initShaders();
    }

    private void initShaders() {
        //create the main shader program
        this.shader=glCreateProgramObjectARB();
        if(this.shader!=0) { //if passed, create sub shaders
            this.vertexShader=createVertexShader(SHADERS_ROOT_PATH+File.separator+"screen.vert");
            this.fragmentShader=createFragmentShader(SHADERS_ROOT_PATH+File.separator+"screen.frag");
            if(this.vertexShader!=0 && this.fragmentShader!=0) {
                //add in shaders to main program
                glAttachObjectARB(this.shader, this.vertexShader);
                glAttachObjectARB(this.shader, this.fragmentShader);
                glLinkProgramARB(this.shader);
                glValidateProgramARB(this.shader);
                this.useShaders=true;
            }
        }
    }
    int count;
    public void draw() {
        if(useShaders) { //use the shader linked
            glUseProgramObjectARB(this.shader);
        }

        glLoadIdentity();
        //red, which will show if our fragment shader doesn't work
        glColor3f(1.0f, 0.0f, 0.0f);
        drawVertices(count);

        //free up use of shader program
        glUseProgramObjectARB(0);
        count++;
    }

    protected void drawVertices(final int count) {
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        glBegin(GL_POLYGON);
//        for (int i = 0; i < 4; i++) {
//            float f1 = random.nextFloat() * 2 - 1;
//            float f2 = random.nextFloat() * 2 - 1;
//            glVertex2f(f1, f2);
//        }
        float transformVector = count * .01f;
        float left = (float) Math.cos(-transformVector);
        float right = (float) Math.sin(transformVector);
//        if(1 > left || left > -1) {
//            this.count = 0;
//        } else if(right < 0.2f) {
//            this.count = 1000;
//            right = 0.199999999999f;
//        }
        // SEMI WORKING
        /*
        // bottom left
//        glVertex2f(-0.8f, -0.8f);
        glVertex2f(-left, -left);
        // bottom right
//        glVertex2f(0.8f, -0.8f);
        glVertex2f(right, -left);
        // top right
//        glVertex2f(0.8f, 0.8f);
        glVertex2f(right ,right );
        // top left
//        glVertex2f(-0.8f, 0.8f);
        glVertex2f(-left, right);
        */
        // working rotation
//        glVertex2f(left, right);
//        glVertex2f(-right, left);
//        glVertex2f(-left, -right);
//        glVertex2f(right, -left);
        glVertex2f(left, right);
        glVertex2f(-right, left);
        glVertex2f(-left, -right);
        glVertex2f(right, -left);
        glEnd();


    }

    private int createFragmentShader(String pathToShaderFile) {
        int fragmentShader=createShader(pathToShaderFile, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        return fragmentShader;
    }

    private int createVertexShader(String pathToShaderFile) {
        int vertexShader=createShader(pathToShaderFile, ARBVertexShader.GL_VERTEX_SHADER_ARB);
        return vertexShader;
    }

    private int createShader(String pathToShaderFile, int shaderType) {
        int newShader=glCreateShaderObjectARB(shaderType);
        if(newShader!=0) {
            String shaderCode=readInShaderCodeFromShaderFile(pathToShaderFile);
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

    private String readInShaderCodeFromShaderFile(String pathToShaderFile) {
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