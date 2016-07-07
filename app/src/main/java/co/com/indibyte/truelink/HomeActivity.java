package co.com.indibyte.truelink;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.adapter.SlidingMenuAdapter;
import co.com.indibyte.truelink.model.ItemSlideMenu;
import co.com.indibyte.truelink.model.Tarjetas;


/**
 * Created by CharlieMolina on 1/07/16.
 */
public class HomeActivity  extends FragmentActivity{

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init component
        setContentView(R.layout.activity_home);


        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_tarje, "Mis Tarjetas"));
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_lupa3, "Buscador"));
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_cam3, "Camara"));
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_log3, "Log Out"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/ close sliding list
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar action = getActionBar();
        action.setDisplayHomeAsUpEnabled(true);


        //Set title
        setTitle(listSliding.get(0).getTitle());
        //item selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);



        //Display fragment 1 when start
        replaceFragment(0);
        //Hanlde on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2){
                    setTitle("Mis Tarjetas");
                }else {
                    //Set title
                    setTitle(listSliding.get(position).getTitle());
                }
                //item selected
                listViewSliding.setItemChecked(position, true);
                //Replace fragment
                replaceFragment(position);
                //Close menu
                drawerLayout.closeDrawer(listViewSliding);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // action with ID action_refresh was selected
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(listViewSliding)){
                    drawerLayout.closeDrawer(listViewSliding);
                }else{
                    drawerLayout.openDrawer(listViewSliding);
                }
                break;

            default:
                break;
        }
        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && Activity.RESULT_OK == resultCode) {

            if (isNetworkAvailable(HomeActivity.this)) {
                if (data.getStringExtra("SCAN_RESULT").substring(0, 8).equalsIgnoreCase("truelinc")) {
                    String[] scan_results = data.getStringExtra("SCAN_RESULT").split(":");
                    final String idTarjeta = scan_results[1];

                    //Log.d("query", idTarjeta);
                    final ParseQuery<Tarjetas> query = ParseQuery.getQuery(Tarjetas.class);
                    query.whereEqualTo("objectId", idTarjeta.trim());

                    try {
                        List<Tarjetas> respuesta = query.find();
                        if(!respuesta.isEmpty()){
                            ParseUser user = ParseUser.getCurrentUser();
                            List<String> misTarjetas = user.getList("tarjetas");
                            if (misTarjetas == null) {
                                misTarjetas = new ArrayList<String>();
                            }
                            misTarjetas.add(idTarjeta.trim());
                            try {
                                user.put("tarjetas", misTarjetas);
                                user.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            Log.d("query", "DONE.");
                        }else{

                            // NO LO ENCONTRO
                            Toast.makeText(getApplicationContext(), R.string.msg_alert_cardActivityBodyError2, Toast.LENGTH_SHORT).show();

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }else{
                    Log.d("ERROR", "formato no valido");
                    Toast.makeText(getApplicationContext(), R.string.msg_alert_cardActivityBodyError1, Toast.LENGTH_SHORT).show();
                }
            }else{Log.d("ERROR", "no internet");
                    Toast.makeText(getApplicationContext(), R.string.msg_alert_cardActivityBody, Toast.LENGTH_SHORT).show();

            }
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Create method replace fragment

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                //mis tarjetas
                fragment = new CardsActivity();
                break;
            case 1:
                //buscador online
                fragment = new CardsActivityOnline();
                break;
            case 2:
                //active camera
                Intent it = new Intent(this, com.google.zxing.client.android.CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
            case 3:
                ParseUser.getCurrentUser().logOut();
                finish();
                onBackPressed();
                break;
            default:
                //fragment = new CardsActivity();
                break;
        }

        if(null!=fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
