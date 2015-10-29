package co.com.indibyte.truelink;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

/**
 * Created by CharlieMolina on 22/10/15.
 */
public class TarjetaActivity extends Activity {

    private boolean tarjetaesmia = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarjeta);

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

        Bundle bundle = getIntent().getExtras();
        ParseFile fotoPerfil = new ParseFile(bundle.getByteArray("Foto"));

        if (fotoPerfil != null) {
            foto.setParseFile(fotoPerfil);
            foto.loadInBackground();
        }

        byte[] bitmapdata = bundle.getByteArray("LogoEmpresa");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        logo.setImageBitmap(bitmap);



        nombre.setText(bundle.getString("Nombre"));
        empresa.setText(bundle.getString("Empresa"));
        correo.setText(bundle.getString("Email"));
        direccion.setText(bundle.getString("Direccion"));
        telefono.setText(bundle.getString("Telefono"));
        ciudad.setText(bundle.getString("Cidudad"));
        cargo.setText(bundle.getString("Cargo"));
        tweet.setText(bundle.getString("Twit"));

        final Button btn = (Button) findViewById(R.id.btn_mas);
        btn.setTextColor(Color.RED);
        btn.setText("Eliminar");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tarjetaesmia) {

                    Button button = (Button) v;
                    button.setTextColor(Color.GREEN);
                    button.setText("Agregar");
                    tarjetaesmia=false;
                }else{
                if (tarjetaesmia==false){

                    Button button = (Button) v;
                    button.setTextColor(Color.RED);
                    button.setText("Eliminar");
                    tarjetaesmia=true;
                }}
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.popup,
                        (ViewGroup) findViewById(R.id.pop));

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(view);
                toast.show();
            }
        });

    }




}
