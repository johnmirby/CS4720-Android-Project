package com.virginia.cs.cs4720androidproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSService extends Service {

    LocationManager locationManager;
    LocationListener locationListener;

    public GPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("GPS Service", "Service Binded");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "GPS Service Started", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                displayCurrentLocation(location);
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
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    private void displayCurrentLocation(Location location) {
        Toast.makeText(this, "Current Location: " + location.toString(), Toast.LENGTH_LONG).show();
    }

    private void _shutdownService() {
        locationManager.removeUpdates(locationListener);
    }
}
