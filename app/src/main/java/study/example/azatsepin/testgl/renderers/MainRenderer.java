package study.example.azatsepin.testgl.renderers;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import study.example.azatsepin.testgl.R;
import study.example.azatsepin.testgl.utils.ShaderUtils;

import static android.opengl.GLES10.GL_TRIANGLES;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_LINES;


public class MainRenderer extends AbstractRenderer {
    private int aColorLocation;
    private int aPositionLocation;


    public MainRenderer(Context context) {
        super(context);

    }
    @Override
    protected void prepareData() {
        float[] vertices = { //def vec4 (0,0,0,1)
                -1f, 0f, 1.0f, 0.0f, 0.0f,
                -1f, 1f, 0.0f, 1.0f, 0.0f,
                0f, 1f, 0.0f, 0.0f, 1.0f,
//
                0f,1f,1.0f, 0.0f, 0.0f,
                1f,1f, 0.0f, 1.0f, 0.0f,
                1f,0f, 0.0f, 0.0f, 1.0f,
//
                1f,0f,1.0f, 0.0f, 0.0f,
                1f,-1f, 0.0f, 1.0f, 0.0f,
                0f,-1f, 0.0f, 0.0f, 1.0f,
//
                0f,-1f,1.0f, 0.0f, 0.0f,
                -1f,-1f, 0.0f, 1.0f, 0.0f,
                -1f, 0f,0.0f, 0.0f, 1.0f,

                //line
                -1f, -1f, 0.0f, 0.0f, 1.0f,
                1f, 1f, 0.0f, 1.0f, 0.0f,

                -1f, 1f, 0.0f, 0.0f, 1.0f,
                1f, -1f, 0.0f, 1.0f, 0.0f,

                0,0.5f, 0.0f, 0.0f, 1.0f,
                0.5f,0,0.0f, 0.0f, 1.0f,
                0,-0.5f,0.0f, 0.0f, 1.0f,
                -0.5f,0,0.0f, 0.0f, 1.0f,

        };
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    @Override
    protected void init() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.simple_vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.simple_fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId,fragmentShaderId);
        glUseProgram(programId);
    }

    @Override
    protected void createViewMatrix() {

    }
    @Override
    protected void bindMatrix() {

    }
    protected void bindData(){ // передаем данные в шейдер
        // координаты
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        vertexData.position(2);
        glVertexAttribPointer(aColorLocation, 3, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 1f);
        init();
        prepareData();
        bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDrawArrays(GL_TRIANGLES, 3, 3);
        glDrawArrays(GL_TRIANGLES, 6, 3);
        glDrawArrays(GL_TRIANGLES, 9, 3);

//        glDrawArrays(GL_TRIANGLES, 0, 12);
        glLineWidth(12);

        glDrawArrays(GL_LINES, 12, 2);
        glDrawArrays(GL_LINES, 14, 2);

        glDrawArrays(GL_POINTS,16,4);
        //glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
    }
}
