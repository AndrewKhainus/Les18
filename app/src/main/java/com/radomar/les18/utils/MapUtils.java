package com.radomar.les18.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.radomar.les18.R;

/**
 * Created by Radomar on 07.09.2015.
 */
public class MapUtils {

    public static Bitmap createMarkerBitmapWithImage(Context _context, Uri _imageUri) {

        View markerView = ((Activity)_context).getLayoutInflater().inflate(R.layout.custom_marker_layout, null);
        ImageView photo =  (ImageView) markerView.findViewById(R.id.ivPhoto_CML);
        photo.setImageURI(_imageUri);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) _context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        markerView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        markerView.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        markerView.draw(canvas);

        return bitmap;
    }

}
