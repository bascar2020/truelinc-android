package co.com.indibyte.truelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import co.com.indibyte.truelink.adapter.CardsAdapter;
import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by CharlieMolina on 21/10/15.
 */

public class CardsActivity extends Activity {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private List<Tarjetas> tarjetasUser = null;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    // Search EditText



 private CardsAdapter mGoogleCardsAdapter;

    public CardsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);

        new RemoteDataTask().execute();

        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mGoogleCardsAdapter != null) {
                    mGoogleCardsAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });;

        ImageView btnBorrar = (ImageView) findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputSearch.setText("");
            }
        });



    }

    private class RemoteDataTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(CardsActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Cargando tus tarjetas");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {



            ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);





            woodwinds.findInBackground(new FindCallback<Tarjetas>() {
                public void done(List<Tarjetas> tarjetas, ParseException exception) {
                    if (tarjetas == null) {
                        Log.d("query", "request failed.");
                    } else {
                        Log.d("query", "Succes!.");
                        ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);

                        mGoogleCardsAdapter = new CardsAdapter(CardsActivity.this, tarjetas);
                        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
                        swingBottomInAnimationAdapter.setAbsListView(listView);

                        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

                        listView.setAdapter(swingBottomInAnimationAdapter);

                        //Close Dialog
                        mProgressDialog.dismiss();
                    }
                    //Close Dialog
                    mProgressDialog.dismiss();


                }
            });

            // ***********************************if null no diponible INVENTAR ALGO ***********************************


//                for (ParseObject tarjeta : ob){
//                    Tarjetas tarjetas = new Tarjetas();
//                    tarjetas.setNombre(tarjeta.getString("Nombre"));
//                    tarjetas.setCargo(tarjeta.getString("Cargo"));
//                    tarjetas.setEmpresa(tarjeta.getString("Empresa"));
//                    tarjetas.setDireccion(tarjeta.getString("Direccion"));
//                    tarjetas.setTelefono(tarjeta.getString("Telefono"));
//                    tarjetas.setEmail(tarjeta.getString("Email"));
//                    tarjetas.setCiudad(tarjeta.getString("Ciudad"));
//                    tarjetas.setTwit(tarjeta.getString("Twit"));
//                    tarjetas.setFoto(tarjeta.getParseFile("Foto"));
//                    tarjetas.setLogo(tarjeta.getParseFile("LogoEmpresa"));
//
//                    tarjetasUser.add(tarjetas);
//                }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);




        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        // action with ID action_refresh was selected
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_camara:
//                Toast.makeText(this, "Camara selected", Toast.LENGTH_SHORT) .show();
                Intent it = new Intent(this, com.google.zxing.client.android.CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
//            case R.id.action_searh:
//                Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(CardsActivity.this, SignUpOrLoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0 && RESULT_OK==resultCode){
            Toast.makeText(this, "RESULTADO"+data.getStringExtra("SCAN_RESULT")+" "+data.getStringExtra("SCAN_FORMAT"), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
