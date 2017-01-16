package co.com.indibyte.truelink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by CharlieMolina on 22/10/15.
 */
public class TarjetaActivity extends Activity {

    private boolean tarjetaesmia;
    private String objectId;
    private byte[] bitmapQr = null;
    private List<Tarjetas> tarjetasUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarjeta);
        ReplaceFont.replaceDefaultFont(this, "DEFAULT", "Ubun.ttf");

        TextView nombre = (TextView)findViewById(R.id.tv_nombre);
        TextView empresa = (TextView)findViewById(R.id.tv_empresa);
        TextView correo = (TextView)findViewById(R.id.tv_correo);
        TextView direccion = (TextView)findViewById(R.id.tv_direccion);
        TextView telefono = (TextView)findViewById(R.id.tv_telefono);
        TextView tweet = (TextView)findViewById(R.id.tv_twit);
        TextView ciudad = (TextView)findViewById(R.id.tv_ciudad);
        TextView cargo = (TextView)findViewById(R.id.tv_cargo);
        final ParseImageView foto = (ParseImageView) findViewById(R.id.iv_foto);
        ImageView logo = (ImageView) findViewById(R.id.iv_logo);
        ImageView qr = (ImageView) findViewById(R.id.iv_qr);
        ImageView btn_facebook = (ImageView) findViewById(R.id.btn_face);
        ImageView btn_twiter = (ImageView) findViewById(R.id.btn_tweet);
        ImageView btn_www = (ImageView) findViewById(R.id.btn_web);

        final Bundle bundle = getIntent().getExtras();
        if (bundle.get("Foto")!=null){
            if (bundle.getByteArray("Foto") != null) {
                byte[] bitmapdata = bundle.getByteArray("Foto");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                foto.setImageBitmap(bitmap);
            }
        }

        if (bundle.get("LogoEmpresa")!=null){
            byte[] bitmapdata = bundle.getByteArray("LogoEmpresa");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            logo.setImageBitmap(bitmap);
        }
        if (bundle.get("Qr")!=null){
            bitmapQr = bundle.getByteArray("Qr");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapQr, 0, bitmapQr.length);
            qr.setImageBitmap(bitmap);
        }

        objectId = bundle.getString("objecId");
        tarjetaesmia = bundle.getBoolean("follow");

        nombre.setText(bundle.getString("Nombre"));
        empresa.setText(bundle.getString("Empresa"));
        correo.setText(bundle.getString("Email"));
        direccion.setText(bundle.getString("Direccion"));
        telefono.setText(bundle.getString("Telefono"));
        ciudad.setText(bundle.getString("Ciudad"));
        cargo.setText(bundle.getString("Cargo"));
        tweet.setText(bundle.getString("Twit"));

        if(bundle.getString("facebook").isEmpty()){
            btn_facebook.setVisibility(View.GONE);
        }else{
            btn_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(bundle.getString("facebook")));
                    startActivity(browserIntent);
                }
            });
        }

        if(bundle.getString("twiter").isEmpty()){
            btn_twiter.setVisibility(View.GONE);
        }else{
            btn_twiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(bundle.getString("twiter")));
                    startActivity(browserIntent);
                }
            });
        }

        if(bundle.getString("www").isEmpty()){
            btn_www.setVisibility(View.GONE);
        }else{
            btn_www.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(bundle.getString("www")));
                    startActivity(browserIntent);
                }
            });
        }



        final Button btn = (Button) findViewById(R.id.btn_mas);


        if(objectId==""|| objectId==null){
            Log.d("pase","null");
            btn.setVisibility(View.GONE);
        }

        //Inisializar el button de agregar o eliminar
        if (tarjetaesmia) {
            btn.setTextColor(Color.RED);
            btn.setText("Eliminar");
        }else{
            if (!tarjetaesmia){
                btn.setTextColor(Color.GREEN);
                btn.setText("Agregar");
            }}


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tarjetaesmia) {
                    // lamar la funcion eliminar: Eliminar el objectId del array, subirlo a la base de datos. recargar la base local y devoverse a la anterior ventana
                    new AlertDialog.Builder(TarjetaActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.title_alert)
                            .setMessage(R.string.confirmation)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    eliminarTarjeta();
                                    Button button = (Button) v;
                                    button.setTextColor(Color.GREEN);
                                    button.setText("Agregar");
                                    tarjetaesmia=false;
                                }
                            })
                            .setNegativeButton(R.string.no,null)
                            .show();



                } else {
                    if (!tarjetaesmia) {
                        // llamar la funcion agregar :Agregar  el objectId al array, subirlo a la base de datos. recargar la base local y devoverse a la anterior ventana
                        agregarTarjeta();
                        Button button = (Button) v;
                        button.setTextColor(Color.RED);
                        button.setText("Eliminar");
                        tarjetaesmia = true;
                }}
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.popup,
//                        (ViewGroup) findViewById(R.id.pop));
//                ImageView qr1= (ImageView) view.findViewById(R.id.iv_qr1);
//                if (bitmapLogo!=null){
//                    qr1.setImageBitmap(bitmapLogo);
//                }
                Bundle bundle = new Bundle();
                if (bitmapQr == null) {
                    bundle.putByteArray("qr", null);
                } else {
                    bundle.putByteArray("qr", bitmapQr);
                }


                Intent intent = new Intent(getApplicationContext(), PopUp.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void agregarTarjeta() {
        ParseUser user = ParseUser.getCurrentUser();

        List<String> misTarjetas = user.getList("tarjetas");
        if(misTarjetas==null){misTarjetas= new ArrayList<String>();}
        misTarjetas.add(objectId.trim());
        try {
            user.put("tarjetas",misTarjetas);
            user.save();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        final ParseQuery<Tarjetas> query = ParseQuery.getQuery(Tarjetas.class);
        query.whereEqualTo("objectId", objectId.trim());
        query.findInBackground(new FindCallback<Tarjetas>() {
            @Override
            public void done(List<Tarjetas> objects, ParseException e) {
                if (!objects.isEmpty()) {

                    for (Tarjetas t : objects) {
                        Log.d("queryDOS", t.getObjectID());
                        if (tarjetasUser == null) {
                            tarjetasUser = new ArrayList<Tarjetas>();
                        }
                        tarjetasUser.add(t);
                        try {
                            t.pin();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                startActivity(new Intent(TarjetaActivity.this, HomeActivity.class));
            }
        });





    }

    private void eliminarTarjeta(){

        ParseUser user = ParseUser.getCurrentUser();
        List<String> misTarjetas = user.getList("tarjetas");
        if (misTarjetas.remove(objectId)){

            user.put("tarjetas", misTarjetas);
            user.saveInBackground();

            Intent intent = new Intent(TarjetaActivity.this, HomeActivity.class);
            startActivity(intent);


        }else{
            Toast.makeText(TarjetaActivity.this, R.string.errorconexion, Toast.LENGTH_SHORT).show();
        }




    }




}
