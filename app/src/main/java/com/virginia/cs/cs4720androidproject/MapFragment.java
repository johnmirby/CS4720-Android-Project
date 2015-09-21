package com.virginia.cs.cs4720androidproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.maps.MapFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GMapFragment extends MapFragment {

    public GMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
