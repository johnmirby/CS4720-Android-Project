package com.virginia.cs.cs4720androidproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    String WANTEDCARDFILE = "Wanted_Card_File";

    ArrayList<Card> cardList = new ArrayList<>();
    ArrayAdapter<Card> adapter;

    ArrayList<Card> wantedCardList = new ArrayList<>();
    ArrayAdapter<Card> adapter2;

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

        ListView listView2 = (ListView)findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wantedCardList);

        listView2.setOnItemClickListener(mMessageClickedHandler2);
        listView2.setAdapter(adapter2);

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
                if (line.split(",").length == 5) {
                    card.setImageFileName(line.split(",")[4]);
                }
                cardList.add(card);
                adapter.notifyDataSetChanged();
                Log.d("BuildingListView", cardList.toString());
                line = reader.readLine();
            }
            fis.close();
        }catch(Exception e) {
            Log.e("Cannot Read Card File", e.getMessage());
        }

        try {
            FileInputStream fis2 = openFileInput(WANTEDCARDFILE);
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(fis2));
            String line2 = reader2.readLine();
            while(line2 != null){
                Card card = new Card(line2.split(",")[0]);
                card.setExpansion(line2.split(",")[1]);
                card.setLanguage(line2.split(",")[2]);
                card.setConditionIndex(Integer.parseInt(line2.split(",")[3]));
                wantedCardList.add(card);
                adapter2.notifyDataSetChanged();
                Log.d("BuildingListView", wantedCardList.toString());
                line2 = reader2.readLine();
            }
            fis2.close();
        }catch(Exception e) {
            Log.e("Cannot Read W Card File", e.getMessage());
        }

    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Card card = (Card)parent.getItemAtPosition(position);
            showListEntryDialog(card);
        }
    };

    private AdapterView.OnItemClickListener mMessageClickedHandler2 = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Card card = (Card)parent.getItemAtPosition(position);
            showListEntryDialog2(card);
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

        try {
            FileOutputStream fos2 = openFileOutput(WANTEDCARDFILE, Context.MODE_PRIVATE);
            for (int i = 0; i < wantedCardList.size(); i++) {
                String cardCSV2 = wantedCardList.get(i).toCSV();

                fos2.write(cardCSV2.getBytes());
                fos2.write("\r\n".getBytes());
            }
            fos2.close();
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
        listEntryDialog.cardList = cardList;
        listEntryDialog.adapter = adapter;
        listEntryDialog.show(fm, "fragment_list_entry");
    }

    public void clearList(View view) {
        cardList.clear();
        adapter.notifyDataSetChanged();
    }

    public void addItem2(View view) {
        EditText editText = (EditText)findViewById(R.id.editText2);
        Card card = new Card(editText.getText().toString());
        wantedCardList.add(card);
        adapter2.notifyDataSetChanged();
        Log.d("BuildingListView", wantedCardList.toString());
    }

    private void showListEntryDialog2(Card card) {
        FragmentManager fm = getSupportFragmentManager();
        WantedListEntryFragment listEntryDialog = new WantedListEntryFragment();
        listEntryDialog.card = card;
        listEntryDialog.cardList = wantedCardList;
        listEntryDialog.adapter = adapter2;
        listEntryDialog.show(fm, "fragment_wanted_list_entry");
    }

    public void clearList2(View view) {
        wantedCardList.clear();
        adapter2.notifyDataSetChanged();
    }

}
