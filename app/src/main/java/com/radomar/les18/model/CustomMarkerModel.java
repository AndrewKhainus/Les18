package com.radomar.les18.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Radomar on 07.09.2015.
 */
public class CustomMarkerModel {

    public LatLng latLng;
    public String imageUri;
    public String text;

    public CustomMarkerModel(LatLng _latLng, String _imageUri, String _text) {
        this.latLng = _latLng;
        this.imageUri = _imageUri;
        this.text = _text;
    }
}
