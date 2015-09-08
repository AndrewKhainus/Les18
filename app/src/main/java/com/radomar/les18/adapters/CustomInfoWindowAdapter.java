package com.radomar.les18.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.radomar.les18.R;

/**
 * Created by Radomar on 07.09.2015.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity mActivity;
    private TextView mDetails;

    public CustomInfoWindowAdapter(Activity _activity) {
        this.mActivity = _activity;
    }

    @Override
    public View getInfoWindow(final Marker _marker) {
        final View view = mActivity.getLayoutInflater().inflate(R.layout.custom_marker_info_window, null);

        mDetails = (TextView)view.findViewById(R.id.tvDetails_CIW);
        mDetails.setText(_marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker _marker) {
        return null;
    }
}
