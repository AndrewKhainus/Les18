package com.radomar.les18;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.radomar.les18.adapters.CustomInfoWindowAdapter;
import com.radomar.les18.fragments.CustomMarkerDialog;
import com.radomar.les18.fragments.InfoDialog;
import com.radomar.les18.interfaces.OnDataPass;
import com.radomar.les18.model.CustomMarkerModel;
import com.radomar.les18.utils.MapUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                               GoogleMap.OnMapLongClickListener,
                                                               OnDataPass {

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5000;
    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10;

    public static final String BUNDLE_KEY = "text";
    private static final String KEY = "key";

    private CustomMarkerDialog mCustomMarkerDialog;
    private ArrayList<CustomMarkerModel> markers;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private LatLng mSelectedLatLng;
    private GoogleMap mGoogleMap;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        readFromSharePreferences();
        initToolbar();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();

        checkGPS();
        initMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_FOR_UPDATES,
                mLocationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.menu_main, _menu);

        return super.onCreateOptionsMenu(_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem _item) {
        switch (_item.getItemId()) {
            case R.id.action_show_info:

                InfoDialog infoDialog = new InfoDialog();

                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_KEY, getAddress(MyLocationListener.sImHere));

                infoDialog.setArguments(bundle);
                infoDialog.show(getFragmentManager(), "tag");
                break;

            case R.id.action_delete_markers:
                markers.clear();
                mGoogleMap.clear();
                break;
        }

        return super.onOptionsItemSelected(_item);
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        mGoogleMap = _googleMap;

        if (_googleMap == null) {
            return;
        }

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48, 22), 14));
        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        mGoogleMap.setOnMapLongClickListener(this);

        for (CustomMarkerModel customMarkerModel : markers) {
            addMarkerToGoogleMap(customMarkerModel);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onMapLongClick(LatLng _latLng) {
        mSelectedLatLng = _latLng;
        mCustomMarkerDialog = new CustomMarkerDialog();
        mCustomMarkerDialog.show(getFragmentManager(), "tag");
    }

    @Override
    public void onDataPass(Uri _imageUri, String _text) {
        if (markers == null) {
            markers = new ArrayList<>();
        }

        mGoogleMap.addMarker(new MarkerOptions()
                .position(mSelectedLatLng)
                .snippet(_text)
                .icon(BitmapDescriptorFactory.fromBitmap(MapUtils.createMarkerBitmapWithImage(this, _imageUri))));

        CustomMarkerModel customMarkerModel = new CustomMarkerModel(mSelectedLatLng, _imageUri.toString(), _text);

        markers.add(customMarkerModel);
        writeToSharePreferences(new Gson().toJson(markers));
    }

    private void readFromSharePreferences() {
        Type listType = new TypeToken<ArrayList<CustomMarkerModel>>(){}.getType();

        String jsonString = PreferenceManager.getDefaultSharedPreferences(this).getString(KEY, "[]");
        markers = new Gson().fromJson(jsonString, listType);
    }

    private void writeToSharePreferences(String _string) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY, _string).commit();
    }

    private void initMap() {
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentGoogleMap);
        mapFragment.getMapAsync(this);
    }

    private void initToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.tbToolbar_AM);
        setSupportActionBar(mToolbar);
    }

    private void addMarkerToGoogleMap(CustomMarkerModel _customMarkerModel) {
        String snippet = _customMarkerModel.latLng.latitude + "/" + _customMarkerModel.latLng.longitude + '\n' +
                         _customMarkerModel.text;

        mGoogleMap.addMarker(new MarkerOptions()
                .position(_customMarkerModel.latLng)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromBitmap(MapUtils.createMarkerBitmapWithImage(this, Uri.parse(_customMarkerModel.imageUri)))));
    }

    private void checkGPS() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private String getAddress(Location _location) {
        StringBuilder addressInfo = new StringBuilder();
        try {
            Geocoder geo = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(_location.getLatitude(), _location.getLongitude(), 1);
            if (addresses.isEmpty()) {
                addressInfo.append("can't get address");
            }
            else {
                if (addresses.size() > 0) {
                    addressInfo.append("Street: " + addresses.get(0).getAddressLine(0) + "\n" +
                            "City: " + addresses.get(0).getAddressLine(1) + "\n" +
                            "Region: " + addresses.get(0).getAddressLine(2) + "\n" +
                            "Country: " + addresses.get(0).getAddressLine(3));
                    if (addresses.get(0).getPostalCode() != null) {
                        addressInfo.append("\n" + "PostalCode: " + addresses.get(0).getPostalCode());
                    } else {
                        addressInfo.append("\n" + "PostalCode: Unknown");
                    }
                }
            }
        }
        catch (Exception _e) {
            _e.printStackTrace();
        }
        return addressInfo.toString();
    }

}
