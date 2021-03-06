package co.com.indibyte.truelink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.List;

import co.com.indibyte.truelink.adapter.CardsAdapterBuscador;
import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by user on 19/11/15.
 */
public class CardsActivityOnline extends Fragment{

    private static final int INITIAL_DELAY_MILLIS = 300;
    private CardsAdapterBuscador mGoogleCardsAdapter;
    String busqueda = "";
    ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.buscador, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        ReplaceFont.replaceDefaultFont(getActivity(), "DEFAULT", "Ubun.ttf");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText inputSearch = (EditText) getView().findViewById(R.id.inputSearch);


        ImageView btnBorrar = (ImageView) getView().findViewById(R.id.btn_borrar);
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
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(inputSearch.getWindowToken(),0);

                    new CargarlistView().execute();

                    return  true;
                }
                return false;
            }
        });

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

                if(isNetworkAvailable(getActivity())){
                    // Create a progressdialog
                    mProgressDialog = new ProgressDialog(getActivity());
                    // Set progressdialog title
                    mProgressDialog.setTitle(R.string.dialog_tittle);
                    // Set progressdialog message
                    mProgressDialog.setMessage(getActivity().getApplicationContext().getText(R.string.dialog_message));
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // Show progressdialog
                    mProgressDialog.show();
                }else{
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


        @Override
        protected ArrayAdapter<Tarjetas> doInBackground(Void... params) {
            ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);
            woodwinds.whereContains("Nombre", busqueda.toLowerCase());
            woodwinds.whereEqualTo("Privada", false);




            ParseQuery<Tarjetas> woodwinds2 = ParseQuery.getQuery(Tarjetas.class);
            woodwinds2.whereContains("Empresa", busqueda.toLowerCase());
            woodwinds2.whereEqualTo("Privada", false);

            ParseQuery<Tarjetas> woodwinds3 = ParseQuery.getQuery(Tarjetas.class);
            String strName = busqueda.toLowerCase();
            String[] strArray = new String[] {strName};
            woodwinds3.whereContainedIn("tags",Arrays.asList(strArray));
            woodwinds3.whereEqualTo("Privada", false);

            if(ParseUser.getCurrentUser().getList("tarjetas") != null) {
                woodwinds.whereNotContainedIn("objectId", ParseUser.getCurrentUser().getList("tarjetas"));
                woodwinds2.whereNotContainedIn("objectId", ParseUser.getCurrentUser().getList("tarjetas"));
                woodwinds3.whereNotContainedIn("objectId", ParseUser.getCurrentUser().getList("tarjetas"));
            }



            //List<String> milista =  ParseUser.getCurrentUser().getList("tarjetas");



            List<ParseQuery<Tarjetas>> queries = new ArrayList<ParseQuery<Tarjetas>>();
            queries.add(woodwinds);
            queries.add(woodwinds2);
            queries.add(woodwinds3);

            ParseQuery<Tarjetas> mainQuery = ParseQuery.or(queries);
            mainQuery.findInBackground(new FindCallback<Tarjetas>() {
                    public void done(List<Tarjetas> tarjetas, ParseException exception) {
                    if (tarjetas == null) {
                        //Log.d("query", "request failed.");
                    } else {
                        if (tarjetas.isEmpty()) {
                            Tarjetas vacia = new Tarjetas();
                            vacia.setNombre("datos no encontrados");
                            vacia.setEmpresa("no datos");
                            tarjetas.add(vacia);
                        }
                        ListView listView = (ListView) getView().findViewById(R.id.activity_googlecards_listview);
                        mGoogleCardsAdapter = new CardsAdapterBuscador(getActivity(), tarjetas);
                        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mGoogleCardsAdapter);
                        swingBottomInAnimationAdapter.setAbsListView(listView);

                        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
                        listView.setAdapter(swingBottomInAnimationAdapter);

                        mProgressDialog.dismiss();
                    }

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<Tarjetas> tarjetasArrayAdapter) {
            super.onPostExecute(tarjetasArrayAdapter);
        }

    }

    public boolean isNetworkAvailable( final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
