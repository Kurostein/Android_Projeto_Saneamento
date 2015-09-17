package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.security.InvalidParameterException;

public class PhotoDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnCamera;
    private Button btnGaleria;
    private Bundle bundle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_data_screen);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);

        //verificar se os parametros foram passados

        btnCamera.setOnClickListener(this);
        btnGaleria.setOnClickListener(this);

        intent = getIntent();

    }

    public void onClick(View v){
        if(v == btnCamera){
            
        }else if(v == btnGaleria){

        }

        try{
            if(!intent.hasExtra("BUNDLE")){
                throw new InvalidParameterException("Parâmetro não encontrado!");
            }

            bundle = intent.getBundleExtra("BUNDLE");

            //coloca a imagem como um Array de Bytes no bundle

            intent.putExtra("BUNDLE", bundle);
            intent.setClass(this, ProblemDataScreen.class);

            startActivity(intent);
            finish();

        }catch(InvalidParameterException e){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(e.getMessage());
            alert.setNeutralButton("Ok", null);
            alert.show();
            return;
        }

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
