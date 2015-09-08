package com.radomar.les18.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radomar.les18.MainActivity;
import com.radomar.les18.R;


/**
 * Created by Radomar on 06.09.2015.
 */
public class InfoDialog extends DialogFragment {

    private TextView mInfo;

    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {

        getDialog().setTitle("Address!");
        View view = _inflater.inflate(R.layout.location_info_dialog_fragment, null);

        mInfo  = (TextView) view.findViewById(R.id.tvInfo_IDF);

        mInfo.setText(getArguments().getString(MainActivity.BUNDLE_KEY));

        return view;
    }

}