package study.example.azatsepin.testgl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import study.example.azatsepin.testgl.utils.ShaderUtils;

import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.GL_POINTS;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

public class PerspectiveRenderer implements GLSurfaceView.Renderer{
    private final static int POSITION_COUNT = 4;

    private Context context;

    private int programId;

    private FloatBuffer vertexData;
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private float[] mProjectionMatrix = new float[16];

    public PerspectiveRenderer(Context context) {
        this.context = context;
        prepareData();
    }

    private void prepareData() {
        //float x1 = -0.5f, y1 = -0.8f, x2 = 0.5f, y2 = -0.8f;

        float[] vertices = {
                0f, 0f, 0.0f, 1.0f, 1f , 0f , 0f,
                0f, 0.5f, 0.0f, 1.0f , 1f , 0f , 0f,
                0f, -0.5f, 0.0f, 1.0f, 1f , 0f , 0f,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData(){ // передаем данные в шейдер
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        // цвет
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        vertexData.position(4);
        glVertexAttribPointer(aColorLocation, 3, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 1f);
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
        bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_POINTS, 0, 3);
    }
}
