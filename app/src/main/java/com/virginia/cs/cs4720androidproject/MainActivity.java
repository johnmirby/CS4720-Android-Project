package com.virginia.cs.cs4720androidproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.virginia.cs.cs4720androidproject.MESSAGE";

    private GPSService gpsService;
    boolean mBounded;

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyCardsFragment frg = new MyCardsFragment();
        MyTradesFragment frg1 = new MyTradesFragment();
        MapFragment frg2 = new MapFragment();

        FragmentManager manager = getFragmentManager();//create an instance of fragment manager

        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.My_Container_1_ID, frg, "Frag_Top_tag");
        transaction.add(R.id.My_Container_2_ID, frg1, "Frag_Middle_tag");
        transaction.add(R.id.My_Container_3_ID, frg2, "Frag_Bottom_tag");

        transaction.commit();
        map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.Google_Map)).getMap();
        map.setMyLocationEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, GPSService.class);
        startService(mIntent);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewCards(View view){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void addCard(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText3);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void addTradeMarker(View view){
        map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.Google_Map)).getMap();
        String title = ((EditText)this.findViewById(R.id.editText2)).getText().toString();
        String description = ((EditText)this.findViewById(R.id.editText4)).getText().toString();
        LatLng pos = gpsService.getCurrentLocation();
        if (pos != null){
            map.addMarker(new MarkerOptions().position(pos).title(title).snippet(description));
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(pos)
                    .zoom(18)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);
        }
        else {
            Toast.makeText(this, "Please wait for the current location to be retrieved.", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            GPSService.MyBinder b = (GPSService.MyBinder) binder;
            gpsService = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            gpsService = null;
        }
    };

}
