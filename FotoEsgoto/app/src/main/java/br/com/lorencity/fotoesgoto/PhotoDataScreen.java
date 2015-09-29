package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;

public class PhotoDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnCamera;
    private Button btnGaleria;
    private Button btnAvancar2;
    private Bundle bundle;
    private Intent intent;

    private Bitmap bitmapImg;
    private ImageView imgFoto;

    private static final int TIRAR_FOTO = 10203;
    private static final int GALERIA_FOTO = 10204;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_data_screen);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);
        btnAvancar2 = (Button) findViewById(R.id.btnAvancar2);

        //verificar se os parametros foram passados

        btnCamera.setOnClickListener(this);
        btnGaleria.setOnClickListener(this);
        btnAvancar2.setOnClickListener(this);

        imgFoto = (ImageView) findViewById(R.id.imgFoto);

        intent = getIntent();

    }

    public void onClick(View v){
        if(v == btnCamera){
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(it, TIRAR_FOTO);

        }else if(v == btnGaleria){
            Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
            galeria.setType("image/*");
            startActivityForResult(galeria, GALERIA_FOTO);
        }else if(v == btnAvancar2){
            setImgToBundle();
            intent.setClass(this, ProblemDataScreen.class);

            startActivity(intent);
            finish();
        }

    }

    private void setImgToBundle() {
        try {
            if (!intent.hasExtra("BUNDLE")) {
                throw new InvalidParameterException("Parâmetro não encontrado!");
            }

            bundle = intent.getBundleExtra("BUNDLE");

            ByteArrayOutputStream imgOutput = new ByteArrayOutputStream();
            bitmapImg.compress(Bitmap.CompressFormat.JPEG, 50, imgOutput);
            bundle.putByteArray("IMG_BYTE_ARRAY", imgOutput.toByteArray());
            //coloca a imagem como um Array de Bytes no bundle

            intent.putExtra("BUNDLE", bundle);

        }catch(InvalidParameterException e){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(e.getMessage());
            alert.setNeutralButton("Ok", null);
            alert.show();
            return;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Aqui verifica se o resultado é da Requisição TIRAR_FOTO.
        if (requestCode == TIRAR_FOTO) {
            //Ok
            if (resultCode == RESULT_OK) {
                // Aqui pego a imagem
                bitmapImg = (Bitmap) data.getExtras().get("data");
                // Seta ela no ImaView do Layout
                imgFoto.setImageBitmap(bitmapImg);

                //Aqui posso salvar a foto se quizer.
            } else if (resultCode == RESULT_CANCELED) {//Cancelou a foto
                Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT);
            } else { //Saiu da Intent
                Toast.makeText(this, "Saiu", Toast.LENGTH_SHORT);
            }

        }else if(requestCode == GALERIA_FOTO){
             if(resultCode == RESULT_OK){
                 Uri image_galeria = data.getData();
                 String[] colunas =  {MediaStore.Images.Media.DATA};
                 Cursor cursor = getContentResolver().query(image_galeria,colunas,null,null,null);
                 cursor.moveToFirst();
                 int indice = cursor.getColumnIndex(colunas[0]);
                 String path = cursor.getString(indice);
                 cursor.close();
                 Bitmap bitmap = BitmapFactory.decodeFile(path);
                 imgFoto.setImageBitmap(bitmap);
             }
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
