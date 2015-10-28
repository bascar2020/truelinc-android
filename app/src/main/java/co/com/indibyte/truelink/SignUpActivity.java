package co.com.indibyte.truelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by CharlieMolina on 17/09/15.
 */
public class SignUpActivity extends Activity{

    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);

        findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(getResources().getString(R.string.error_intro));
                if (isEmpty(usernameView)) {
                    error = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                }
                if (isEmpty(passwordView)) {
                    if (error) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    error = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                }
                if (!isMatching(passwordView, passwordAgainView)) {
                    if (error) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    error = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_mismatched_passwords));
                }
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (error) {
                    Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Signing up.  Please wait.");
                dlg.show();


                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());
                // Call the Parse signup method
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                    if (e != null){
                        Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(SignUpActivity.this, CardsActivity.class) ;
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                });
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
