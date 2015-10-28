package co.com.indibyte.truelink;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;

/**
 * Created by CharlieMolina on 22/10/15.
 */
public class TarjetaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarjeta);

        TextView nombre = (TextView)findViewById(R.id.tv_nombre);
        TextView empresa = (TextView)findViewById(R.id.tv_empresa);
        TextView correo = (TextView)findViewById(R.id.tv_correo);
        TextView direccion = (TextView)findViewById(R.id.tv_direccion);
        TextView telefono = (TextView)findViewById(R.id.tv_telefono);
        //TextView tweet = (TextView)findViewById(R.id.tv_tweet);
        TextView ciudad = (TextView)findViewById(R.id.tv_ciudad);
        TextView cargo = (TextView)findViewById(R.id.tv_cargo);
        ParseImageView foto = (ParseImageView) findViewById(R.id.iv_foto);
        ParseImageView logo = (ParseImageView) findViewById(R.id.iv_logo);

        Bundle bundle = getIntent().getExtras();
        ParseFile fotoPerfil = new ParseFile(bundle.getByteArray("Foto"));
        ParseFile fotoLogo = new ParseFile(bundle.getByteArray("LogoEmpresa"));

        if (fotoPerfil != null) {
            foto.setParseFile(fotoPerfil);
            foto.loadInBackground();
        }
        if (fotoLogo != null) {
            logo.setParseFile(fotoLogo);
            logo.loadInBackground();
        }
        nombre.setText(bundle.getString("Nombre"));
        empresa.setText(bundle.getString("Empresa"));
        correo.setText(bundle.getString("Email"));
        direccion.setText(bundle.getString("Direccion"));
        telefono.setText(bundle.getString("Telefono"));
        ciudad.setText(bundle.getString("Cidudad"));
        cargo.setText(bundle.getString("Cargo"));



    }



}
