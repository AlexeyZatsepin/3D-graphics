package study.example.azatsepin.testgl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import study.example.azatsepin.testgl.renderers.MainRenderer;
import study.example.azatsepin.testgl.renderers.ModelsRenderer;
import study.example.azatsepin.testgl.renderers.PerspectiveRenderer;
import study.example.azatsepin.testgl.renderers.TextureRenderer;

public class MainActivity extends AppCompatActivity {
    private Views view;
    private final String TAG = "CurrView";
    private enum Views{
        primitives, perspective ,models, cube
    }
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()){
            Toast.makeText(this,"not supported OpenGl",Toast.LENGTH_LONG).show();
            return;
        }
        view = (Views) getIntent().getSerializableExtra(TAG);
        if (view == null) view = Views.primitives;

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        switch (view){
            case primitives:
                glSurfaceView.setRenderer(new MainRenderer(this));
                break;
            case perspective:
                glSurfaceView.setRenderer(new PerspectiveRenderer(this));
                break;
            case models:
                glSurfaceView.setRenderer(new ModelsRenderer(this));
                break;
            case cube:
                glSurfaceView.setRenderer(new TextureRenderer(this));
                break;
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.perspective) {
            view = Views.perspective;
            //startActivity(new Intent(this, PerspectiveActivity.class));
        }else if (id == R.id.models){
            view = Views.models;
            //startActivity(new Intent(this, ModelsActivity.class));
        }else if (id == R.id.textures){
            view = Views.cube;
            //startActivity(new Intent(this, TextureActivity.class));
        }else if (id == R.id.main){
            view = Views.primitives;
        }
        Intent intent = getIntent();
        intent.putExtra(TAG, view);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    public boolean supportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}
