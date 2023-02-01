package com.example.restaurant_picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class customInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public customInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info,null);
    }
    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView Title = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            Title.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView Snippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            Snippet.setText(snippet);
        }
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
