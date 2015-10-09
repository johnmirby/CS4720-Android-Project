package com.virginia.cs.cs4720androidproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListEntryFragment extends DialogFragment {

    Spinner cardConditions;
    EditText expansionText;
    EditText languageText;
    Uri imageUri;
    View view;

    public Card card;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static ListEntryFragment newInstance(String param1, String param2) {
        ListEntryFragment fragment = new ListEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_entry, container);
        getDialog().setTitle("Enter Card Information");
        cardConditions = (Spinner) view.findViewById(R.id.fragmentSpinner);
        cardConditions.setSelection(card.getConditionIndex());
        cardConditions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                card.setConditionIndex(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        expansionText = (EditText)view.findViewById(R.id.fragmentEditText2);
        if (expansionText != null) {
            expansionText.setText(card.getExpansion());
        }
        languageText = (EditText)view.findViewById(R.id.fragmentEditText1);
        if (languageText != null) {
            languageText.setText(card.getLanguage());
        }

        Button b = (Button) view.findViewById(R.id.openCameraButton);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1337);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        card.setExpansion(expansionText.getText().toString());
        card.setLanguage(languageText.getText().toString());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1337) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) view.findViewById(R.id.cardImage);
            imageview.setImageBitmap(image);
        }
    }

}
