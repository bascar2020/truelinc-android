package co.com.indibyte.truelink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by CharlieMolina on 17/09/15.
 */
public class SignUpOrLoginActivity extends Activity {
    @Override
    public void onCreate(Bundle saveInstaceState){
        super.onCreate(saveInstaceState);

        setContentView(R.layout.signup_or_login);

        // Log in button click handler
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                Log.d("DEB","entre login");
                startActivity(new Intent(SignUpOrLoginActivity.this, LoginActivity.class));
            }
        });

        // Sign up button click handler
        ((Button) findViewById(R.id.btn_signUp)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                Log.d("DEB","entre sing");
                startActivity(new Intent(SignUpOrLoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
