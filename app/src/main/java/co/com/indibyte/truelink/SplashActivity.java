package co.com.indibyte.truelink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



/**
 * Created by CharlieMolina on 23/11/15.
 */
public class SplashActivity extends Activity {

    private final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SignUpOrLoginActivity.class));
            }
        },DELAY);

    }
}
