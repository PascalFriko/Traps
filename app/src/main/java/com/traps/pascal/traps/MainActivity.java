package com.traps.pascal.traps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking if Google Play Services are installed and up-to-date
        if (checkGooglePlayServices()) {
            Log.d("Google Play Services", "Success");

            GetLocationTask locTask = new GetLocationTask(this);
            locTask.execute();
        } else {
            //What to do??
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checking if Google Play Services are installed and up-to-date
     * @return Boolean if the correct version of Google Play Services is installed
     */
    public Boolean checkGooglePlayServices() {
        final int GPS_ERRORHANDLING_REQUEST = 9001;
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,this,GPS_ERRORHANDLING_REQUEST);
            errorDialog.show();
            Log.d("Google Play Services", "Update needed");
        } else {
            Toast.makeText(this, "There is an error with your Google Play Services", Toast.LENGTH_LONG).show();
            Log.d("Google Play Services", "Error");
        }
        return false;
    }


}
