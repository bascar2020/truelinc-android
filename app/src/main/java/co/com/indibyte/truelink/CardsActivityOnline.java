package co.com.indibyte.truelink;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import co.com.indibyte.truelink.R;

/**
 * Created by user on 19/11/15.
 */
public class CardsActivityOnline extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscador);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);


        ImageView btnBorrar = (ImageView) findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://hago un case por si en un futuro agrego mas opciones
                Log.d("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
