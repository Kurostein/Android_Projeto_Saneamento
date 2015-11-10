package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import java.net.MalformedURLException;
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
    private Spinner spnBairro;
    private ArrayAdapter<String> adpBairro;

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
        spnBairro = (Spinner) findViewById(R.id.spnBairro);

        //init spinner
        initSpinners();

        btnAvancar3 = (Button) findViewById(R.id.btnAvancar3);
        btnAvancar3.setOnClickListener(this);

        intent = getIntent();
    }

    public void onClick(View v){
        try{
            if(!intent.hasExtra("BUNDLE")){
                throw new InvalidParameterException("Parâmetro não encontrado!");
            }

            //Verifica se os parametros importantes foram preenchidos
            if(!checkForEmptyFields()){
                throw new IllegalArgumentException("Todos os campos com * devem ser preenchidos.");
            }

            bundle = intent.getBundleExtra("BUNDLE");
            bundle.putString("VALUE_LOGRADOURO", fieldLogradouro.getText().toString());
            bundle.putString("VALUE_NUMERO", fieldNumero.getText().toString());
            bundle.putString("VALUE_BAIRRO", spnBairro.getSelectedItem().toString());
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
        }catch (IllegalArgumentException e){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage(e.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
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

    private void initSpinners(){
        JSONArray auxJsonArray;

        adpTipoProblema = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoProblema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpBairro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpBairro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String response = null;

        try{
            //Buscando dados de problemas do banco
            response = connectServerToGetProblemas();
            auxJsonArray = new JSONArray(response);

            adpTipoProblema.add("Selecione um problema");
            for(int i=0; i<auxJsonArray.length(); i++){
                adpTipoProblema.add(auxJsonArray.getString(i));
            }
            spnTipoProblema.setAdapter(adpTipoProblema);

            //Buscando dados de bairros do banco
            response = connectServerToGetBairros();
            auxJsonArray = new JSONArray(response);

            adpBairro.add("Selecione um bairro");
            for(int i=0; i<auxJsonArray.length(); i++){
                adpBairro.add(auxJsonArray.getString(i));
            }

            spnBairro.setAdapter(adpBairro);

        }catch (ExecutionException e){
            showAlertDialogMsg("Problema na conexão de dados.");
            return;
        }catch (InterruptedException e){
            showAlertDialogMsg("Problema na conexão de dados.");
            return;
        }catch (JSONException e){
            showAlertDialogMsg(response);
            return;
        }catch (MalformedURLException e){
            showAlertDialogMsg("Erro no endereço de conexão com o servidor.");
            return;
        }
    }

    private String connectServerToGetProblemas() throws JSONException, InterruptedException, ExecutionException, MalformedURLException{
        String request;
        String response;

        request = "action=consultarProblemasJson";

        ServerConnection serverConn = new ServerConnection();
        serverConn.setServerAddress(serverConn.getProblemasServletAddress());
        response = serverConn.sendTextDataToServer(request);

        return response;
    }

    private String connectServerToGetBairros() throws JSONException, InterruptedException, ExecutionException, MalformedURLException{
        String request;
        String response;

        request = "action=consultarBairrosJson";

        ServerConnection serverConn = new ServerConnection();
        serverConn.setServerAddress(serverConn.getBairrosServletAddress());
        response = serverConn.sendTextDataToServer(request);

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

    private boolean checkForEmptyFields(){

        if(spnTipoProblema.getSelectedItem().toString() == null
                || spnTipoProblema.getSelectedItem().toString().equals("")
                || spnTipoProblema.getSelectedItem().toString().equals("Selecione um problema")){
            return false;
        } else
        if (fieldLogradouro.getText().toString() == null || fieldLogradouro.getText().toString().equals("")){
            return false;
        } else
        if(fieldNumero.getText().toString() == null || fieldNumero.getText().toString().equals("")){
            return false;
        } else
        if(spnBairro.getSelectedItem().toString() == null
                || spnBairro.getSelectedItem().toString().equals("")
                || spnBairro.getSelectedItem().toString().equals("Selecione um bairro")){
            return false;
        } else
        if(fieldCep.getText().toString() == null || fieldCep.getText().toString().equals("")){
            return false;
        } else {
            return true;
        }
    }

}
