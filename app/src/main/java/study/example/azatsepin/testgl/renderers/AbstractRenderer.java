package study.example.azatsepin.testgl.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glViewport;

public abstract class AbstractRenderer implements GLSurfaceView.Renderer{
    protected final static int POSITION_COUNT = 3;
    protected final long TIME = 10000;//for animation

    protected Context context;
    protected int programId;

    protected FloatBuffer vertexData;

    protected float[] mProjectionMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] mModelMatrix = new float[16];
    protected float[] mMatrix = new float[16]; // result


    /**
     * shader variables
     */
    protected int uMatrixLocation;
    protected int aPositionLocation;

    public AbstractRenderer(Context context) {
        this.context = context;
    }

    protected abstract void prepareData();

    protected void createProjectionMatrix(int width, int height) {
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

    protected abstract void init();
    protected abstract void createViewMatrix();
    protected abstract void bindData();
    protected abstract void bindMatrix();

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        init();
        prepareData();
        bindData();
        createViewMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }
}
