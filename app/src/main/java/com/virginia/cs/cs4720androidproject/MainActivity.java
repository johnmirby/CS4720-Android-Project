package com.virginia.cs.cs4720androidproject;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.virginia.cs.cs4720androidproject.MESSAGE";

    String MARKERFILE = "Marker_File";

    public ArrayList<MarkerOptions> markerList = new ArrayList<>();

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

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("markers")){
                markerList = savedInstanceState.getParcelableArrayList("markers");
                if (markerList != null){
                    for (int i = 0; i < markerList.size(); i++) {
                        map.addMarker(markerList.get(i));
                    }
                }
            }
        }

        if (markerList.isEmpty()) {
            try {
                FileInputStream fis = openFileInput(MARKERFILE);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line = reader.readLine();
                while (line != null) {
                    MarkerOptions marker = new MarkerOptions();
                    String markerTitle = line.split(",")[0];
                    String markerSnippet = line.split(",")[1];
                    Double markerLatitude = Double.parseDouble(line.split(",")[2]);
                    Double markerLongitude = Double.parseDouble(line.split(",")[3]);
                    marker.position(new LatLng(markerLatitude, markerLongitude))
                            .title(markerTitle).snippet(markerSnippet);
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.trade_icon));
                    markerList.add(marker);
                    for (int i = 0; i < markerList.size(); i++) {
                        map.addMarker(markerList.get(i));
                    }
                    line = reader.readLine();
                }
                fis.close();
            } catch (Exception e) {
                Log.e("Cannot Read Marker File", e.getMessage());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, GPSService.class);
        startService(mIntent);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        mBounded = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }

        try {
            FileOutputStream fos = openFileOutput(MARKERFILE, Context.MODE_PRIVATE);
            for (int i = 0; i < markerList.size(); i++) {
                String markerCSV = markerToCSV(markerList.get(i));

                fos.write(markerCSV.getBytes());
                fos.write("\r\n".getBytes());
            }
            fos.close();
        }
        catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Location mapLocation = map.getMyLocation();
        if (mapLocation != null) {
            LatLng pos = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(pos)
                    .zoom(18)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("markers", markerList);
        super.onSaveInstanceState(outState);
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

    /**
     * Override super.onNewIntent() so that calls to getIntent() will return the
     * latest intent that was used to start this Activity rather than the first
     * intent.
     */
    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public String markerToCSV(MarkerOptions marker){
        return marker.getTitle() + "," + marker.getSnippet() + "," + marker.getPosition().latitude
                + "," + marker.getPosition().longitude;
    }

    public void viewCards(View view){
        Intent intent = new Intent(this, ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void viewTrades(View view){
        if (markerList.size() > 0) {
            FragmentManager fm = getFragmentManager();
            TradesDialog tradesDialog = new TradesDialog();
            tradesDialog.trades = markerList;
            tradesDialog.show(fm, "fragment_trades_dialog");
        }
        else {
            Toast.makeText(this, "You don't currently have any trades", Toast.LENGTH_LONG).show();
        }
    }

    public void addCard(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        EditText editText = (EditText) findViewById(R.id.editText3);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void openAbout(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("About Mobile Trade Binder")
                .setMessage(getString(R.string.welcome_message))
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }

    public void addTradeMarker(View view){
        map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.Google_Map)).getMap();
        String title = ((EditText)this.findViewById(R.id.editText2)).getText().toString();
        String description = ((EditText)this.findViewById(R.id.editText4)).getText().toString();
        LatLng pos = gpsService.getCurrentLocation();
        if (pos != null){
            MarkerOptions markerOptions = new MarkerOptions().position(pos).title(title).snippet(description);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.trade_icon));
            map.addMarker(markerOptions);
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(pos)
                    .zoom(18)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);
            markerList.add(markerOptions);
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
