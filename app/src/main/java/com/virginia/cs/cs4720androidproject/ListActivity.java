package com.virginia.cs.cs4720androidproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends FragmentActivity {

    ArrayList<String> itemList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);

        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setAdapter(adapter);

        /*Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (message != null){
            itemList.add(message);
            adapter.notifyDataSetChanged();
            Log.d("BuildingListView", itemList.toString());
        }*/
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            showListEntryDialog();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (message != null){
            itemList.add(message);
            adapter.notifyDataSetChanged();
            Log.d("BuildingListView", itemList.toString());
        }
        intent.removeExtra(MainActivity.EXTRA_MESSAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity2, menu);
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

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(backIntent);
    }

    public void addItem(View view) {
        EditText editText = (EditText)findViewById(R.id.editText);
        itemList.add(editText.getText().toString());
        adapter.notifyDataSetChanged();
        Log.d("BuildingListView", itemList.toString());
    }

    private void showListEntryDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ListEntryFragment listEntryDialog = new ListEntryFragment();
        listEntryDialog.show(fm, "fragment_list_entry");
    }

}
