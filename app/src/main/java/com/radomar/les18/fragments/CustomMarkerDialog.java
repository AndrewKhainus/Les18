package com.radomar.les18.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.radomar.les18.R;
import com.radomar.les18.interfaces.OnDataPass;

/**
 * Created by Radomar on 07.09.2015.
 */
public class CustomMarkerDialog extends DialogFragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 1;

    private OnDataPass mDataPasser;
    private Uri mSelectedImageUri;
    private ImageView mImage;
    private EditText mText;
    private Button mSave;


    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        mDataPasser = (OnDataPass) _activity;
        mSelectedImageUri = Uri.parse("android.resource://" + _activity.getPackageName() + "/drawable/custom_marker.png");
    }

    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {

        getDialog().setTitle("Create marker!");
        View view = _inflater.inflate(R.layout.custom_marker_dialog_fragment, null);

        findViews(view);
        setListener();

        return view;
    }


    @Override
    public void onClick(View _v) {
       switch(_v.getId()) {
           case R.id.btSave_CMDF:
               mDataPasser.onDataPass(mSelectedImageUri, mText.getText().toString());
               dismiss();
               break;
           case R.id.ivImage_CMDF:
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent,
                       "Select Picture"), REQUEST_CODE);
               break;
       }
    }

    @Override
    public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        if (_data != null) {
            if (_requestCode == REQUEST_CODE) {
                mSelectedImageUri = _data.getData();
                mImage.setImageURI(mSelectedImageUri);
            }
        }
    }

    private void findViews(View _view) {
        mImage  = (ImageView) _view.findViewById(R.id.ivImage_CMDF);
        mText   = (EditText)  _view.findViewById(R.id.etText_CMDF);
        mSave   = (Button)    _view.findViewById(R.id.btSave_CMDF);
    }

    private void setListener() {
        mSave.setOnClickListener(this);
        mImage.setOnClickListener(this);
    }
}