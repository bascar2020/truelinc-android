package co.com.indibyte.truelink;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.adapter.SlidingMenuAdapter;
import co.com.indibyte.truelink.model.ItemSlideMenu;


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
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_action_home, "Mis Tarjetas"));
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
                //Set title
                setTitle(listSliding.get(position).getTitle());
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
            // action with ID action_refresh was selected
            case R.id.action_camara:
//                Toast.makeText(this, "Camara selected", Toast.LENGTH_SHORT) .show();
                Intent it = new Intent(this, com.google.zxing.client.android.CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
            case R.id.btn_buscar:
                startActivity(new Intent(getApplicationContext(), CardsActivityOnline.class));
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(this , SignUpOrLoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;

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
                break;
            default:
               // fragment = new Fragment1();
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
}
