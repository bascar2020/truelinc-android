package co.com.indibyte.truelink;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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
        ParseImageView foto = (ParseImageView) findViewById(R.id.iv_foto);
        ImageView logo = (ImageView) findViewById(R.id.iv_logo);
        ImageView qr = (ImageView) findViewById(R.id.iv_qr);


        final Bundle bundle = getIntent().getExtras();
        if (bundle.get("Foto")!=null){
            ParseFile fotoPerfil = new ParseFile(bundle.getByteArray("Foto"));
            if (fotoPerfil != null) {
                foto.setParseFile(fotoPerfil);
                foto.loadInBackground();
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
            public void onClick(View v) {
                if (tarjetaesmia) {
                    // lamar la funcion eliminar: Eliminar el objectId del array, subirlo a la base de datos. recargar la base local y devoverse a la anterior ventana
                    eliminarTarjeta();
                    Button button = (Button) v;
                    button.setTextColor(Color.GREEN);
                    button.setText("Agregar");
                    tarjetaesmia=false;

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
                startActivity(new Intent(TarjetaActivity.this, CardsActivity.class));
            }
        });





    }

    private void eliminarTarjeta(){
        ParseUser user = ParseUser.getCurrentUser();
        List<String> misTarjetas = user.getList("tarjetas");

        if (misTarjetas.remove(objectId)){
            Log.d("ANTES", "PASE");
            user.put("tarjetas", misTarjetas);
            user.saveInBackground();


//            ParseQuery<Tarjetas> woodwinds = ParseQuery.getQuery(Tarjetas.class);
//            woodwinds.whereContainedIn("objectId", misTarjetas);
//
//                try {
//                    ParseObject.deleteAllInBackground(woodwinds.find());
//                    ParseObject.pinAllInBackground(woodwinds.find());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

            Intent intent = new Intent(TarjetaActivity.this, CardsActivity.class);
            startActivity(intent);


        }else{
            Toast.makeText(this, "No se puede eliminar compruebe conexion..", Toast.LENGTH_SHORT)
                       .show();
        }
    }




}
