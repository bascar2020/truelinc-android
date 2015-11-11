package co.com.indibyte.truelink;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by CharlieMolina on 11/11/15.
 */
public class PopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        ImageView qr = (ImageView) findViewById(R.id.iv_qr1);
        findViewById(R.id.lineaId).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onBackPressed();
                return false;
            }
        });



        int width = dm.widthPixels;
        int height = dm.heightPixels;




        if (bundle.get("qr")!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("qr"), 0, bundle.getByteArray("qr").length);
            qr.setImageBitmap(bitmap);
        }

        getWindow().setLayout((int)(width*.9),(int)(height*.8));
    }
}
