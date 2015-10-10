package com.virginia.cs.cs4720androidproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TradesDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TradesDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradesDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<MarkerOptions> trades;
    ArrayList<String> tradeStrings = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;
    GoogleMap map;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradesDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static TradesDialog newInstance(String param1, String param2) {
        TradesDialog fragment = new TradesDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TradesDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trades_dialog, container, false);
        list = (ListView) view.findViewById(R.id.trades_listView);
        generateTradeStrings();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tradeStrings);
        list.setAdapter(adapter);
        //setRetainInstance(true);
        getDialog().setTitle("My Trades");
        map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.Google_Map)).getMap();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long rowId) {


                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle(trades.get(position).getTitle())
                        .setMessage(trades.get(position).getSnippet())
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        trades.remove(position);
                                        tradeStrings.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();

            }

        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.Google_Map)).getMap();
        map.clear();
        for (int i = 0; i < trades.size(); i++) {
            map.addMarker(trades.get(i));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void generateTradeStrings(){
        for (int i = 0; i < trades.size(); i++){
            String tradeString = trades.get(i).getTitle();
            tradeStrings.add(tradeString);
        }
    }

}
