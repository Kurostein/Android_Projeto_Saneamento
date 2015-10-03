package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class
        ResumeDataScreen extends AppCompatActivity implements View.OnClickListener{

    private TextView txtCPF;
    private TextView txtTipoProblema;
    private TextView txtEndereco;
    private TextView txtBairro;
    private TextView txtComplemento;
    private TextView txtCEP;
    private ConnectClient conexao;
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

        }catch(InvalidParameterException e){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(e.getMessage());
            alert.setNeutralButton("Ok", null);
            alert.show();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        //Envio de informações
        Map parametros = new HashMap();
        parametros.put("cpf", txtCPF.getText().toString());
        parametros.put("tipoProblema",txtTipoProblema.getText().toString());
        parametros.put("endereco",txtEndereco.getText().toString());
        parametros.put("bairro",txtBairro.getText().toString());
        parametros.put("complemento",txtComplemento.getText().toString());
        parametros.put("cep",txtCEP.getText().toString());
        parametros.put("imagem",imgCamera.getDrawingCache());

        conexao = new ConnectClient("http://192.168.1.100:8080/AndroidWeb/Resposta");
        String resultado = conexao.doPost(parametros);
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(resultado);
        msg.setNeutralButton("OK",null);
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
