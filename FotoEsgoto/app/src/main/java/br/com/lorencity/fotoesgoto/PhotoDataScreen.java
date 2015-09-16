package br.com.lorencity.fotoesgoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PhotoDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnCamera;
    private Button btnGaleria;
    private Button btnAvancar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_data_screen);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);

        //verificar se os parametros foram passados

        btnCamera.setOnClickListener(new EventCamera());
        btnGaleria.setOnClickListener(new EventGallery());

        btnAvancar2.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent intent = new Intent(this, ProblemDataScreen.class);

        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_data_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
