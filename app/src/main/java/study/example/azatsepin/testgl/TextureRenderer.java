package study.example.azatsepin.testgl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import study.example.azatsepin.testgl.utils.ShaderUtils;
import study.example.azatsepin.testgl.utils.TextureUtils;

import static android.opengl.GLES10.glActiveTexture;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.GL_TRIANGLES;
import static android.opengl.GLES10.glDrawElements;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.GL_DEPTH_TEST;
import static android.opengl.GLES10.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES10.GL_UNSIGNED_BYTE;
import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class TextureRenderer implements GLSurfaceView.Renderer{
    private final static int POSITION_COUNT = 3;
//    private static final int TEXTURE_COUNT = 2;
//    private static final int STRIDE = (POSITION_COUNT
//            + TEXTURE_COUNT) * 4;
    private final long TIME = 10000L;
    private Context context;

    private FloatBuffer vertexData;
    private ByteBuffer indexArray;


    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];

    private int texture;

    public TextureRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);

        init();
        prepareData();
        bindData();
        createViewMatrix();
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void prepareData() {

        float[] vertices = {
                -1,  1,  1,     // верхняя левая ближняя
                1,  1,  1,     // верхняя правая ближняя
                -1, -1,  1,     // нижняя левая ближняя
                1, -1,  1,     // нижняя правая ближняя
                -1,  1, -1,     // верхняя левая дальняя
                1,  1, -1,     // верхняя правая дальняя
                -1, -1, -1,     // нижняя левая дальняя
                1, -1, -1       // нижняя правая дальняя
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        indexArray =  ByteBuffer.allocateDirect(36)
                .put(new byte[] {
                        // грани куба
                        // ближняя
                        1, 3, 0,
                        0, 3, 2,

                        // дальняя
                        4, 6, 5,
                        5, 6, 7,

                        // левая
                        0, 2, 4,
                        4, 2, 6,

                        // правая
                        5, 7, 1,
                        1, 7, 3,

                        // верхняя
                        5, 1, 4,
                        4, 1, 0,

                        // нижняя
                        6, 2, 7,
                        7, 2, 3
                });
        indexArray.position(0);

        texture = TextureUtils.loadTextureCube(context, new int[]{R.drawable.box, R.drawable.box,
                R.drawable.box, R.drawable.box,
                R.drawable.box, R.drawable.box});
    }

    private void init() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.cube_vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.cube_fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);

        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void bindData() {
        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, 0 , vertexData);
        glEnableVertexAttribArray(aPositionLocation);

//        // координаты текстур
//        vertexData.position(POSITION_COUNT);
//        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
//                false, STRIDE, vertexData);
//        glEnableVertexAttribArray(aTextureLocation);

//        // помещаем текстуру в target 2D юнита 0
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture);
        // помещаем текстуру в target CUBE_MAP юнита 0
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 2;
        float eyeZ = 4;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        Matrix.setIdentityM(mModelMatrix, 0);

        setModelMatrix();
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }

    private void setModelMatrix() {
        float angle = (float)(SystemClock.uptimeMillis() % TIME) / TIME * 360;
        Matrix.rotateM(mModelMatrix, 0, angle, 0, 1, 0);
        bindMatrix();
    }
}
