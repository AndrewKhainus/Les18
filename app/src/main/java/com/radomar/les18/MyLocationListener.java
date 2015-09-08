package com.radomar.les18;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Radomar on 06.09.2015.
 */
public class MyLocationListener implements LocationListener {

    public static Location sImHere;

    @Override
    public void onLocationChanged(Location _location) {
        sImHere = _location;
    }

    @Override
    public void onProviderDisabled(String _provider) {}

    @Override
    public void onProviderEnabled(String _provider) {}

    @Override
    public void onStatusChanged(String _provider, int _status, Bundle _extras) {}
}