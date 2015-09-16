package br.com.lorencity.fotoesgoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProblemDataScreen extends AppCompatActivity implements View.OnClickListener{

    private Button btnConcluir;
    private EditText fieldEndereco;
    private EditText fieldBairro;
    private EditText fieldComplemento;
    private EditText fieldCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_data_screen);

        fieldEndereco = (EditText) findViewById(R.id.fieldEndereco);
        fieldBairro = (EditText) findViewById(R.id.fieldBairro);
        fieldComplemento = (EditText) findViewById(R.id.fieldComplemento);
        fieldCep = (EditText) findViewById(R.id.fieldCep);

        //verificar se os parametros existem

        btnConcluir = (Button) findViewById(R.id.btnConcluir);
        btnConcluir.setOnClickListener(this);
    }

    public void onClick(View v){
        //////
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
