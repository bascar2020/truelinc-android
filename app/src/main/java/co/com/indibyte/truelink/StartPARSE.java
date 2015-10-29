package co.com.indibyte.truelink;

        import android.app.Application;

import com.parse.Parse;
        import com.parse.ParseObject;

        import co.com.indibyte.truelink.model.Tarjetas;

/**
 * Created by CharlieMolina on 18/09/15.
 */
public class StartPARSE extends Application {


        @Override
        public void onCreate() {
            super.onCreate();

            // Enable Local Datastore.
            Parse.enableLocalDatastore(getApplicationContext());
            ParseObject.registerSubclass(Tarjetas.class);
            // Add your initialization code here
            Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));

//            ParseUser.enableAutomaticUser();
//            ParseACL defaultACL = new ParseACL();

            // Optionally enable public read access.
//            defaultACL.setPublicReadAccess(true);
//            ParseACL.setDefaultACL(defaultACL, true);
        }

}
