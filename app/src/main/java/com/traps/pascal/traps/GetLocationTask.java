package com.traps.pascal.traps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by Pascal on 02.09.2014.
 * Source: http://stackoverflow.com/questions/21504776/using-asynctask-to-get-location-information
 */
public class GetLocationTask extends AsyncTask<Void, Void, String> {

    private Context TaskContext;
    public GetLocationTask(Context context) {
        this.TaskContext = context;
    }

    Dialog progress;
    private String provider;
    private LocationManager locationManagerAsync;
    double latAsync = 0.0;
    double lonAsync = 0.0;
    double altAsync = 0.0;

    String AddressAsync = "";
    Geocoder GeocoderAsync;

    Location location;

    protected void onPreExecute() {
        super.onPreExecute();
        //progress = ProgressDialog.show(TaskContext, "Loading Traps", "Please Wait");
    }

    protected String doInBackground(Void... arg0) {
        locationManagerAsync = (LocationManager) TaskContext.getSystemService(TaskContext.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        provider = locationManagerAsync.getBestProvider(criteria, false);

        if (locationManagerAsync.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationManagerAsync.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (locationManagerAsync.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            provider = LocationManager.PASSIVE_PROVIDER;
        }

        location = locationManagerAsync.getLastKnownLocation(provider);
        if (location != null) {
            latAsync = location.getLatitude();
            lonAsync = location.getLongitude();
            altAsync = location.getAltitude();
            Log.d("Latitude", ""+latAsync);
            Log.d("Longitude", ""+lonAsync);
            Log.d("Altitude", ""+altAsync);
        } else {
            Toast.makeText(TaskContext, "Unable to fetch location", Toast.LENGTH_SHORT).show();
        }

        List<Address> adresses = null;
        GeocoderAsync = new Geocoder(TaskContext, Locale.getDefault());
        try {
            adresses = GeocoderAsync.getFromLocation(latAsync, lonAsync, 1);
            String address = adresses.get(0).getAddressLine(0);
            String city = adresses.get(0).getAddressLine(1);
            String zip_code = adresses.get(0).getPostalCode();
            String country = adresses.get(0).getCountryName();
            AddressAsync = Html.fromHtml(address + ", " +city+ ", "+zip_code+", "+country).toString();
            Log.d("GetLocationTask", AddressAsync);
        } catch (Exception e) {
            e.printStackTrace();
            AddressAsync = "Refresh for the address";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //progress.dismiss();
    }


}
