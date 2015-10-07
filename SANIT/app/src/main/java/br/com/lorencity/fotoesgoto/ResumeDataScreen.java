package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.com.lorencity.connection.ServerConnection;

public class
        ResumeDataScreen extends AppCompatActivity implements View.OnClickListener{

    private TextView txtCPF;
    private TextView txtTipoProblema;
    private TextView txtEndereco;
    private TextView txtBairro;
    private TextView txtComplemento;
    private TextView txtCEP;
    private ImageView imgCamera;

    private Button btnConcluir;

    private Bundle bundle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_data_screen);

        txtCPF = (TextView) findViewById(R.id.txtViewCPF);
        txtTipoProblema = (TextView) findViewById(R.id.txtViewTipoProblema);
        txtEndereco = (TextView) findViewById(R.id.txtViewEndereco);
        txtBairro = (TextView) findViewById(R.id.txtViewBairro);
        txtComplemento = (TextView) findViewById(R.id.txtViewCompemento);
        txtCEP = (TextView) findViewById(R.id.txtViewCEP);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        btnConcluir = (Button) findViewById(R.id.btnConcluir);

        btnConcluir.setOnClickListener(this);

        intent = getIntent();

        try{
            getResumedInfo();
        }catch(InvalidParameterException e){
            showAlertDialogMsg(e.getMessage());
            return;
        }

    }

    @Override
    public void onClick(View v){
        //Params wrapping as String.
        String paramList = wrapParamsAsString();

        //Conexão com o servidor e envio de dados
        try{
            String serverResponse;
            ServerConnection serverConn = new ServerConnection();

            //serverConn.setServerAddress(serverConn.getDefaultServerAddress());
            //serverConn.openPostConnection();

            serverResponse = serverConn.startConnectionForResponse(paramList);

            showAlertDialogMsg(serverResponse);

        //}catch (IOException e){
        //    showAlertDialogMsg("Problema na escrita de dados.");
        //    return;
        }catch (ExecutionException e){
            Log.i("THREAD PROBLEM: ", "Problema na thread");
            return;
        }catch (InterruptedException e){
            Log.i("THREAD PROBLEM: ", "Problema na thread");
            return;
        }

    }

    private void getResumedInfo() throws InvalidParameterException{
        if(!intent.hasExtra("BUNDLE")){
            throw new InvalidParameterException("Parâmetro não encontrado!");
        }
        bundle = intent.getBundleExtra("BUNDLE");

        txtCPF.setText(bundle.getString("VALUE_CPF"));
        txtTipoProblema.setText(bundle.getString("VALUE_TIPO_PROBLEMA"));
        txtEndereco.setText(bundle.getString("VALUE_ENDERECO"));
        txtBairro.setText(bundle.getString("VALUE_BAIRRO"));
        txtComplemento.setText(bundle.getString("VALUE_COMPLEMENTO"));
        txtCEP.setText(bundle.getString("VALUE_CEP"));

        byte[] imgByteArray = bundle.getByteArray("IMG_BYTE_ARRAY");
        Bitmap img = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
        imgCamera.setImageBitmap(img);
    }

    private String wrapParamsAsString(){
        Map<String, String> params = new HashMap<>();

        params.put("cpf", bundle.getString("VALUE_CPF"));
        params.put("tipoProblema", bundle.getString("VALUE_TIPO_PROBLEMA"));
        params.put("endereco", bundle.getString("VALUE_ENDERECO"));
        params.put("bairro", bundle.getString("VALUE_BAIRRO"));
        params.put("complemento", bundle.getString("VALUE_COMPLEMENTO"));
        params.put("cep", bundle.getString("VALUE_CEP"));
        params.put("imagem", bundle.getByteArray("IMG_BYTE_ARRAY").toString());

        String action = "action=insert";
        String jsonParams = "jsonParams="+formatAsJsonString(params);

        String appDataTag = action+"&"+jsonParams;
        return appDataTag;
    }

    private String formatAsJsonString(Map<String, String> params){
        return new JSONObject(params).toString();
    }

    private void showAlertDialogMsg(String message){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(message);
        msg.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        msg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resume_data_screen, menu);
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
