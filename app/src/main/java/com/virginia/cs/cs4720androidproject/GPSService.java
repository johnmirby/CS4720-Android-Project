package com.virginia.cs.cs4720androidproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class GPSService extends Service {

    private final IBinder mBinder = new MyBinder();
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
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
        Toast.makeText(this, "Current Location: Longitude - " + location.getLongitude()
                + " Latitude - " + location.getLatitude(), Toast.LENGTH_SHORT).show();
    }

    public LatLng getCurrentLocation() {
        if (currentLocation != null){
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        else {
            return null;
        }
    }

    private void _shutdownService() {
        locationManager.removeUpdates(locationListener);
    }

    public class MyBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }
}
