package local.hapra.pongxxkotlin.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import local.hapra.pongxxkotlin.Pong;

/**
 * Activity des Spiels
 * aktualisiert von andbra im Feb2020
 * - savedInstanceState verwendet
 * @author Jan Bauerdick
 *
 */
public class PongActivity extends Activity {

    //private static Context CONTEXT;

    private Pong pongObj;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        pongObj.onPause();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pongObj = new Pong(this, getIntent());

        setContentView(pongObj);

        pongObj.onResume();
    }


}
