package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.com.lorencity.connection.ServerConnection;

public class ProblemDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnAvancar3;

    private EditText fieldLogradouro;
    private EditText fieldNumero;
    private EditText fieldBairro;
    private EditText fieldComplemento;
    private EditText fieldCep;

    private Spinner spnTipoProblema;
    private ArrayAdapter<String> adpTipoProblema;

    private Bundle bundle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_data_screen);

        fieldLogradouro = (EditText) findViewById(R.id.fieldLogradouro);
        fieldNumero = (EditText) findViewById(R.id.fieldNumero);
        fieldBairro = (EditText) findViewById(R.id.fieldBairro);
        fieldComplemento = (EditText) findViewById(R.id.fieldComplemento);
        fieldCep = (EditText) findViewById(R.id.fieldCep);
        fieldCep.addTextChangedListener(Mask.insert(Mask.CEP_MASK, fieldCep));
        spnTipoProblema = (Spinner) findViewById(R.id.spnTipoProblema);

        //init spinner
        getListaTipoProblemas();

        btnAvancar3 = (Button) findViewById(R.id.btnAvancar3);
        btnAvancar3.setOnClickListener(this);


        intent = getIntent();
    }

    public void onClick(View v){
        try{
            if(!intent.hasExtra("BUNDLE")){
                throw new InvalidParameterException("Parâmetro não encontrado!");
            }

            //Verificar se os parametros importantes foram preenchidos
            bundle = intent.getBundleExtra("BUNDLE");
            bundle.putString("VALUE_LOGRADOURO", fieldLogradouro.getText().toString());
            bundle.putString("VALUE_NUMERO", fieldNumero.getText().toString());
            bundle.putString("VALUE_BAIRRO", fieldBairro.getText().toString());
            bundle.putString("VALUE_COMPLEMENTO", fieldComplemento.getText().toString());
            bundle.putString("VALUE_CEP", fieldCep.getText().toString());
            bundle.putString("VALUE_TIPO_PROBLEMA", spnTipoProblema.getSelectedItem().toString());

            intent.putExtra("BUNDLE", bundle);
            intent.setClass(this, ResumeDataScreen.class);

            startActivity(intent);
            finish();

        }catch(InvalidParameterException e){
            showAlertDialogMsg(e.getMessage());
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_problem_data_screen, menu);
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

    private void getListaTipoProblemas(){
        JSONArray jsonArrayProblemas;

        adpTipoProblema = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoProblema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try{
            String response = connectServerToGetList();
            jsonArrayProblemas = new JSONArray(response);

            for(int i=0; i<jsonArrayProblemas.length(); i++){
                adpTipoProblema.add(jsonArrayProblemas.getString(i));
            }

            spnTipoProblema.setAdapter(adpTipoProblema);

        }catch (ExecutionException e){
            showAlertDialogMsg("Problema na conexão de dados.");
            return;
        }catch (InterruptedException e){
            showAlertDialogMsg("Problema na conexão de dados.");
            return;
        }catch (JSONException e){
            showAlertDialogMsg("Problema na conversão de dados.");
            return;
        }
    }

    private String connectServerToGetList() throws JSONException, InterruptedException, ExecutionException{
        Map<String, String> params;
        String paramBase64;
        String response;
        JSONObject json;

        params = new HashMap<>();
        params.put("action", "consultar");
        json = new JSONObject(params);
        paramBase64 = Base64.encodeToString(json.toString().getBytes(), Base64.DEFAULT);

        ServerConnection serverConn = new ServerConnection();
        response = serverConn.startConnectionForResponse(paramBase64);

        return response;
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

}
