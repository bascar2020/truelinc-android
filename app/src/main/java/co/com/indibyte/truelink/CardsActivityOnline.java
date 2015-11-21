package co.com.indibyte.truelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.adapter.CardsAdapterBuscador;
import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by user on 19/11/15.
 */
public class CardsActivityOnline extends Activity{

    private static final int INITIAL_DELAY_MILLIS = 300;
    private CardsAdapterBuscador mGoogleCardsAdapter;
    String busqueda = "";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscador);
        ReplaceFont.replaceDefaultFont(this, "DEFAULT", "Ubun.ttf");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);


        ImageView btnBorrar = (ImageView) findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
            }
        });

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH){

                    busqueda = inputSearch.getText().toString();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(inputSearch.getWindowToken(),0);

                    new CargarlistView().execute();

                    return  true;
                }
                return false;
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

    private class CargarlistView extends AsyncTask<Void,Void,ArrayAdapter<Tarjetas>>{

        Context context;

        public CargarlistView()
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            mProgressDialog = new ProgressDialog(CardsActivityOnline.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Cargando tus tarjetas");
            // Set progressdialog message
            mProgressDialog.setMessage("Buscando Tarjetas..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Show progressdialog
            mProgressDialog.show();

        }


        @Override
        protected ArrayAdapter<Tarjetas> doInBackground(Void... params) {
            ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);
            woodwinds.whereContains("Nombre", busqueda.toLowerCase());
            woodwinds.whereEqualTo("Privada", false);
            woodwinds.whereNotContainedIn("ObjectId", ParseUser.getCurrentUser().getList("tarjetas"));


            ParseQuery<Tarjetas> woodwinds2 = ParseQuery.getQuery(Tarjetas.class);
            woodwinds2.whereContains("Empresa", busqueda.toLowerCase());
            woodwinds2.whereEqualTo("Privada", false);
            woodwinds2.whereNotContainedIn("ObjectId", ParseUser.getCurrentUser().getList("tarjetas"));

            List<ParseQuery<Tarjetas>> queries = new ArrayList<ParseQuery<Tarjetas>>();
            queries.add(woodwinds);
            queries.add(woodwinds2);

            ParseQuery<Tarjetas> mainQuery = ParseQuery.or(queries);
            mainQuery.findInBackground(new FindCallback<Tarjetas>() {
                public void done(List<Tarjetas> tarjetas, ParseException exception) {
                    if (tarjetas == null) {
                        Log.d("query", "request failed.");
                    } else {
                        Log.d("query", "Succes Hola care monda!.");
                        if (tarjetas.isEmpty()) {
                            Tarjetas vacia = new Tarjetas();
                            vacia.setNombre("datos no encontrados");
                            vacia.setEmpresa("no datos");
                            tarjetas.add(vacia);
                        }
                        ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);
                        mGoogleCardsAdapter = new CardsAdapterBuscador(CardsActivityOnline.this, tarjetas);
                        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
                        swingBottomInAnimationAdapter.setAbsListView(listView);

                        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
                        listView.setAdapter(swingBottomInAnimationAdapter);


                    }

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<Tarjetas> tarjetasArrayAdapter) {
            super.onPostExecute(tarjetasArrayAdapter);


            mProgressDialog.dismiss();


        }

    }
}
