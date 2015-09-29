package br.com.lorencity.fotoesgoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserDataScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText fieldCpf;
    private Button btnAvancar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_screen);
        fieldCpf = (EditText) findViewById(R.id.fieldCpf);
        fieldCpf.addTextChangedListener(Mask.insert(Mask.CPF_MASK,fieldCpf));
        btnAvancar1 = (Button) findViewById(R.id.btnAvancar1);
        btnAvancar1.setOnClickListener(this);

    }

    public void onClick(View v){
        Intent intent = new Intent(this, PhotoDataScreen.class);
        Bundle bundle = new Bundle();

        bundle.putString("VALUE_CPF", fieldCpf.getText().toString());
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_data_screen, menu);
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
