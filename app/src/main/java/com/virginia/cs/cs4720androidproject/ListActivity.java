package com.virginia.cs.cs4720androidproject;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListActivity extends FragmentActivity {

    String CARDFILE = "Card_File";

    ArrayList<Card> cardList = new ArrayList<>();
    ArrayAdapter<Card> adapter;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardList);

        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setAdapter(adapter);

        //restoreCards
        try {
            FileInputStream fis = openFileInput(CARDFILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();
            while(line != null){
                Card card = new Card(line.split(",")[0]);
                card.setExpansion(line.split(",")[1]);
                card.setLanguage(line.split(",")[2]);
                card.setConditionIndex(Integer.parseInt(line.split(",")[3]));
                cardList.add(card);
                adapter.notifyDataSetChanged();
                Log.d("BuildingListView", cardList.toString());
                line = reader.readLine();
            }
            fis.close();
        }catch(Exception e) {
            Log.e("Cannot Read Card File", e.getMessage());
        }

    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Card card = (Card)parent.getItemAtPosition(position);
            showListEntryDialog(card);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (message != null){
            cardList.add(new Card(message));
            adapter.notifyDataSetChanged();
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

    @Override
    protected void onStop() {
        super.onStop();

        //save cards
        try {
            FileOutputStream fos = openFileOutput(CARDFILE, Context.MODE_PRIVATE);
            for (int i = 0; i < cardList.size(); i++) {
                String cardCSV = cardList.get(i).toCSV();

                fos.write(cardCSV.getBytes());
                fos.write("\r\n".getBytes());
            }
            fos.close();
        }
        catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public void addItem(View view) {
        EditText editText = (EditText)findViewById(R.id.editText);
        Card card = new Card(editText.getText().toString());
        cardList.add(card);
        adapter.notifyDataSetChanged();
        Log.d("BuildingListView", cardList.toString());
    }

    private void showListEntryDialog(Card card) {
        FragmentManager fm = getSupportFragmentManager();
        ListEntryFragment listEntryDialog = new ListEntryFragment();
        listEntryDialog.card = card;
        listEntryDialog.show(fm, "fragment_list_entry");
    }

    public void clearList(View view) {
        cardList.clear();
        adapter.notifyDataSetChanged();
    }

}
