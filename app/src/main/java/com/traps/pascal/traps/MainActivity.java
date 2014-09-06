package com.traps.pascal.traps;

import android.app.Activity;
import android.app.Dialog;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {

    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking if Google Play Services are installed and up-to-date
        if (checkGooglePlayServices()) {
            Log.d("Google Play Services", "Success");

            Location startLocation = getStartLocation();
            Log.d("StartLcoation", startLocation.toString());

            TextView latitude = (TextView)findViewById(R.id.latitude_data);
            TextView longitude = (TextView)findViewById(R.id.longitude_data);
            TextView altitude = (TextView)findViewById(R.id.altitude_data);
            TextView addressTextView = (TextView)findViewById(R.id.address_data);

            latitude.setText(startLocation.getLatitude()+"");
            longitude.setText(startLocation.getLongitude()+"");
            altitude.setText(startLocation.getAltitude()+"");
            addressTextView.setText(address);
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

    private Location getStartLocation() {
        Location location = null;
        double longitude = 0.0;
        double latitude = 0.0;
        double altitude = 0.0;
        String provider;
        Geocoder geocoder;

        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        provider = locationManager.getBestProvider(criteria, false);

        LocationListener myLocationListener = new MyLocationListener();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,myLocationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,myLocationListener);
        } else {
            provider = LocationManager.PASSIVE_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,myLocationListener);
        }

        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();
            Log.d("Longitude", ""+longitude);
            Log.d("Latitude", ""+latitude);
            Log.d("Altitude", ""+altitude);
        } else {
            Log.d("Location Error", "Location == null");
        }

        List<Address> addressList = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latitude,longitude,1);
            Log.d("AddressList", addressList.toString());
            String addres = addressList.get(0).getAddressLine(0);
            String city = addressList.get(0).getAddressLine(1);
            String zip_code = addressList.get(0).getPostalCode();
            String country = addressList.get(0).getCountryName();
            address = Html.fromHtml(addres +", "+ city+", "+zip_code+", "+country).toString();
            Log.d("Address", address);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Address", "Address couldn't be fetched");
        }

        return location;

    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();
            location.getAltitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
