package com.example.joshuadean.movieapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Learnt from https://developer.android.com/guide/topics/ui/dialogs#java
//and also https://www.androidbegin.com/tutorial/android-dialogfragment-tutorial/
public class dialogFragment extends DialogFragment {
    TextView mTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment, container,
                false);
        Bundle bundle = getArguments();
        String Plot = bundle.getString("plot");

        mTextView = rootView.findViewById(R.id.moviePlot);
        mTextView.setText(Plot);
        return rootView;
    }


}
