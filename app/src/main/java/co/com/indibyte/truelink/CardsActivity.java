package co.com.indibyte.truelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.adapter.CardsAdapter;
import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by CharlieMolina on 21/10/15.
 */

public class CardsActivity extends Activity {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private List<Tarjetas> tarjetasUser = null;
    private static List<String> misTarjetas;


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
        ReplaceFont.replaceDefaultFont(this, "DEFAULT", "Ubun.ttf");
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
        ImageView btnBuscar = (ImageView) findViewById(R.id.btn_buscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CardsActivityOnline.class));
            }
        });

    ListView listaTarjetas = (ListView) findViewById(R.id.activity_googlecards_listview);
        listaTarjetas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                inputSearch.clearFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private class RemoteDataTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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


            misTarjetas = ParseUser.getCurrentUser().getList("tarjetas");
            if(misTarjetas!= null){
                for (String a:misTarjetas ) {
                    Log.d("TARJETAS",a);
                }
            }

            if (misTarjetas!=null) {
                ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);
                if(!isNetworkAvailable(CardsActivity.this)){woodwinds.fromLocalDatastore();}
                woodwinds.whereContainedIn("objectId", misTarjetas);
                woodwinds.orderByAscending("Empresa");
                woodwinds.findInBackground(new FindCallback<Tarjetas>() {
                    public void done(List<Tarjetas> tarjetas, ParseException exception) {
                        if (tarjetas == null) {
                            Log.d("query", "request failed.");
                        } else {
                            Log.d("query", "Succes!.");
                            ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);
                            tarjetasUser = tarjetas;
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

            }else{
                // ***********************************if null no diponible INVENTAR ALGO ***********************************
                //Close Dialog
                mProgressDialog.dismiss();
                Log.d("DEBUG","EL USUARIO NO TIENE TARJETAS");
            }

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

            if (isNetworkAvailable(CardsActivity.this)) {
                if (data.getStringExtra("SCAN_RESULT").substring(0, 8).equalsIgnoreCase("truelinc")) {
                    String[] scan_results = data.getStringExtra("SCAN_RESULT").split(":");
                    final String idTarjeta = scan_results[1];

                    //Log.d("query", idTarjeta);
                    final ParseQuery<Tarjetas> query = ParseQuery.getQuery(Tarjetas.class);
                    query.whereEqualTo("objectId", idTarjeta.trim());



                    mProgressDialog = new ProgressDialog(CardsActivity.this);
                    // Set progressdialog title
                    mProgressDialog.setTitle("Cargando tus tarjetas");
                    // Set progressdialog message
                    mProgressDialog.setMessage("Buscando Tarjetas..");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // Show progressdialog
                    mProgressDialog.show();




                    query.findInBackground(new FindCallback<Tarjetas>() {
                        @Override
                        public void done(List<Tarjetas> objects, ParseException e) {


                            if (e == null) {


                                ParseUser user = ParseUser.getCurrentUser();

                                List<String> misTarjetas = user.getList("tarjetas");
                                if(misTarjetas==null){misTarjetas= new ArrayList<String>();}
                                misTarjetas.add(idTarjeta.trim());
                                try {
                                    user.put("tarjetas",misTarjetas);
                                    user.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                Log.d("query", "DONE.");
                                if (!objects.isEmpty()) {

                                    for (Tarjetas t:objects) {
                                        Log.d("queryDOS", t.getObjectID());
                                        if(tarjetasUser==null){tarjetasUser = new ArrayList<Tarjetas>();}
                                        tarjetasUser.add(t);
                                        try {
                                            t.pin();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }

                                    ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);
                                    mGoogleCardsAdapter = new CardsAdapter(CardsActivity.this,tarjetasUser);
                                    SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
                                    swingBottomInAnimationAdapter.setAbsListView(listView);

                                    assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                                    swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

                                    listView.setAdapter(swingBottomInAnimationAdapter);
                                    mProgressDialog.dismiss();



                                } else

                                {
                                    AlertDialog alertDialog = new AlertDialog.Builder(CardsActivity.this).create();
                                    alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                                    alertDialog.setMessage(getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError2));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CardsActivity.this).create();
                                alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                                alertDialog.setMessage(getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError1));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    });



                } else

                {
                    AlertDialog alertDialog = new AlertDialog.Builder(CardsActivity.this).create();
                    alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                    alertDialog.setMessage(getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError1));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
                    else{
                AlertDialog alertDialog = new AlertDialog.Builder(CardsActivity.this).create();
                alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                alertDialog.setMessage(getApplicationContext().getText(R.string.msg_alert_cardActivityBody));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }




        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public boolean isNetworkAvailable( final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}


