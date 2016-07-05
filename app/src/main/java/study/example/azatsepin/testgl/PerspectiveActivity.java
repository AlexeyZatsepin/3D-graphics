package study.example.azatsepin.testgl;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static study.example.azatsepin.testgl.utils.SupportUtils.supportES2;

public class PerspectiveActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2(this)){
            Toast.makeText(this,"not supported OpenGl",Toast.LENGTH_LONG).show();
            return;
        }
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new PerspectiveRenderer(this));
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
        if (id == R.id.main) {
            startActivity(new Intent(this,MainActivity.class));
        }else if (id == R.id.models){
            startActivity(new Intent(this,ModelsActivity.class));
        }else if (id == R.id.textures){
            startActivity(new Intent(this,TextureActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
