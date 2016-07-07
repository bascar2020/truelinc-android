package co.com.indibyte.truelink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import co.com.indibyte.truelink.adapter.CardsAdapter;
import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by CharlieMolina on 21/10/15.
 */

public class CardsActivity extends Fragment {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private List<Tarjetas> tarjetasUser = null;
    private static List<String> misTarjetas;


    List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    // Search EditText



 public CardsAdapter mGoogleCardsAdapter;

    public CardsActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.activity_cards, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        misTarjetas = ParseUser.getCurrentUser().getList("tarjetas");

        //setContentView(R.layout.activity_cards);
        ReplaceFont.replaceDefaultFont(getActivity(), "DEFAULT", "Ubun.ttf");
        new RemoteDataTask().execute();



        final EditText inputSearch = (EditText) getView().findViewById(R.id.inputSearch);
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

        ImageView btnBorrar = (ImageView) getView().findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
            }
        });
       /* ImageView btnBuscar = (ImageView) findViewById(R.id.btn_buscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CardsActivityOnline.class));
            }
        });*/

        ListView listaTarjetas = (ListView) getView().findViewById(R.id.activity_googlecards_listview);
        listaTarjetas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                inputSearch.clearFocus();
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });



        // llamar funcion tutorial para comprobar si debe o no poner la imagen
        tutorial();

    }




    private class RemoteDataTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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




            if (misTarjetas!=null) {
                ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);
                if(!isNetworkAvailable(getActivity())){woodwinds.fromLocalDatastore();}
                woodwinds.whereContainedIn("objectId", misTarjetas);
                woodwinds.orderByAscending("Empresa");
                woodwinds.findInBackground(new FindCallback<Tarjetas>() {
                    public void done(List<Tarjetas> tarjetas, ParseException exception) {
                        if (tarjetas == null) {
                            Log.d("query", "request failed.");
                        } else {
                            Log.d("query", "Succes!.");
                            ListView listView = (ListView) getView().findViewById(R.id.activity_googlecards_listview);
                            tarjetasUser = tarjetas;
                            mGoogleCardsAdapter = new CardsAdapter(getActivity(), tarjetas);
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
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        Log.d("DEBUG_RESULT", intent.getStringExtra("SCAN_RESULT"));
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0 && Activity.RESULT_OK==resultCode){

            if (isNetworkAvailable(getActivity())) {
                if (data.getStringExtra("SCAN_RESULT").substring(0, 8).equalsIgnoreCase("truelinc")) {
                    String[] scan_results = data.getStringExtra("SCAN_RESULT").split(":");
                    final String idTarjeta = scan_results[1];

                    //Log.d("query", idTarjeta);
                    final ParseQuery<Tarjetas> query = ParseQuery.getQuery(Tarjetas.class);
                    query.whereEqualTo("objectId", idTarjeta.trim());


                    mProgressDialog = new ProgressDialog(getActivity());
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

                                //volver invisible la imagen tutorial
                                ImageView im = (ImageView) getView().findViewById(R.id.img_tutorial);
                                im.setVisibility(View.INVISIBLE);



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

                                    ListView listView = (ListView) getView().findViewById(R.id.activity_googlecards_listview);
                                    mGoogleCardsAdapter = new CardsAdapter(getActivity(),tarjetasUser);
                                    SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
                                    swingBottomInAnimationAdapter.setAbsListView(listView);

                                    assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                                    swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

                                    listView.setAdapter(swingBottomInAnimationAdapter);
                                    mProgressDialog.dismiss();



                                } else
                                {
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                                    alertDialog.setMessage(getActivity().getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError2));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                                alertDialog.setMessage(getActivity().getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError1));
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                    alertDialog.setMessage(getActivity().getApplicationContext().getText(R.string.msg_alert_cardActivityBodyError1));
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
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(R.string.msg_alert_cardActivityTitle);
                alertDialog.setMessage(getActivity().getApplicationContext().getText(R.string.msg_alert_cardActivityBody));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }

    }*/
/*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/


    public boolean isNetworkAvailable( final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void tutorial(){
        misTarjetas = ParseUser.getCurrentUser().getList("tarjetas");
        // las tarjetas son vacias mostar imagen del tutorial
        ImageView img_tuto = (ImageView) getView().findViewById(R.id.img_tutorial);
        if(misTarjetas == null || misTarjetas.isEmpty()){
            // set background dependiendo la version

            if(Build.VERSION.SDK_INT >= 17){
                img_tuto.setBackgroundResource(R.drawable.tutorial1);
            }else{
                img_tuto.setBackgroundResource(R.drawable.tutorial1);
            }
            img_tuto.setVisibility(View.VISIBLE);
        }else{
            img_tuto.setVisibility(View.INVISIBLE);
        }
    }


}


