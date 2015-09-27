package br.com.lorencity.fotoesgoto;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.security.InvalidParameterException;

public class ProblemDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnAvancar3;
    private EditText fieldEndereco;
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

        fieldEndereco = (EditText) findViewById(R.id.fieldEndereco);
        fieldBairro = (EditText) findViewById(R.id.fieldBairro);
        fieldComplemento = (EditText) findViewById(R.id.fieldComplemento);
        fieldCep = (EditText) findViewById(R.id.fieldCep);
        spnTipoProblema = (Spinner) findViewById(R.id.spnTipoProblema);

        //verificar se os parametros existem

        btnAvancar3 = (Button) findViewById(R.id.btnAvancar3);
        btnAvancar3.setOnClickListener(this);


        adpTipoProblema = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoProblema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpTipoProblema.addAll("Vazamento de água", "Vazamento de esgoto");
        spnTipoProblema.setAdapter(adpTipoProblema);

        intent = getIntent();
    }

    public void onClick(View v){
        try{
            if(!intent.hasExtra("BUNDLE")){
                throw new InvalidParameterException("Parâmetro não encontrado!");
            }

            bundle = intent.getBundleExtra("BUNDLE");
            bundle.putString("VALUE_ENDERECO", fieldEndereco.getText().toString());
            bundle.putString("VALUE_BAIRRO", fieldBairro.getText().toString());
            bundle.putString("VALUE_COMPLEMENTO", fieldComplemento.getText().toString());
            bundle.putString("VALUE_CEP", fieldCep.getText().toString());
            bundle.putString("VALUE_TIPO_PROBLEMA", spnTipoProblema.getSelectedItem().toString());

            intent.putExtra("BUNDLE", bundle);
            intent.setClass(this, ResumeDataScreen.class);

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
}
