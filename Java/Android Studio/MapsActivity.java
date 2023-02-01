package com.example.restaurant_picker;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.restaurant_picker.MapsActivity.contextBuilder.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.restaurant_picker.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapsActivity<dataStore> extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private ActivityMapsBinding binding;
    private static Location userLocation;
    private static final int RECORD_REQUEST_CODE = 101;
    protected static LocationManager locationManager;
    private LocationListener ls;
    String foodPref;
    int distPref;
    int pricePref;
    private String log = "LOG";
    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Intent intent = getIntent();
        foodPref = intent.getStringExtra("food_preference");
        distPref = intent.getIntExtra("distance_preference", 1);
        pricePref = intent.getIntExtra("price_preference", 1);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    protected void onResume() {
        super.onResume();
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION);
        if (permission != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage("Location needed to find nearby traffic camera's.")
                        .setTitle("Permission required");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                makeRequest();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }
    }

    protected void makeRequest() {

        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION},
                RECORD_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] !=

                        PERMISSION_GRANTED) {
                    //Default location
                    userLocation = new Location("Seattle");
                    userLocation.setLatitude(47.6);
                    userLocation.setLongitude(-122.335167);
                    try {
                        onLocationReady(mMap, userLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    ls = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            try {
                                onLocationReady(mMap, userLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 10, ls);
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setInfoWindowAdapter(new customInfoWindowAdapter(MapsActivity.this));
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == 0) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                onLocationReady(mMap, userLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setupPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    public void onLocationReady(GoogleMap googleMap, Location location) throws IOException {
        int zoom; int milliseconds;
        if(distPref <=2){
            milliseconds = 3000;
            zoom = 14;
        }else if(distPref > 2 && distPref <= 5){
            milliseconds = 4000;
            zoom = 13;
        }else if(distPref > 5 && distPref <= 8){
            milliseconds = 5000;
            zoom = 12;
        }else{
            milliseconds = 6000;
            zoom = 11;
        }
        getContext();
        startLoadingScreen(milliseconds);
        mMap = googleMap;
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, zoom));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .position(current)
                .title("Current Location"));
        new TextSearch().find(foodPref, (toMeters(distPref)), current, mMap, pricePref, MapsActivity.this);
    }

    public void pinLocations(TextSearch.BusinessDetails[] shops, GoogleMap googleMap,Context context) {
        contextBuilder.setContext(context);
        mMap = googleMap;
        if (shops.length == 0) {
            AlertDialog.Builder build = new AlertDialog.Builder(context);
            build.setTitle("No Restaurants found that fit this criteria");
            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    navigateUpTo(new Intent(((Activity) getContext()),Cuisine.class));
                    startActivity(getIntent());
                    //TODO launch fragment
                }
            });
            build.create();
            build.show();
        }else {
            for (TextSearch.BusinessDetails shop : shops) {
                if(shop.getWebsite() == null){
                    break;
                }else {

                    LatLng business = new LatLng((shop.getLatLng(1).latitude), (shop.getLatLng(1).longitude));
                    String hours = "";
                    String price = "";
                    for (int i = 0; i < shop.getHours().length; i++) {
                        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                        if (shop.getHours()[i] == null) {
                            hours += days[i] + ": N/A" + "\n";
                        } else {
                            hours += shop.getHours()[i] + "\n";
                        }
                    }
                    if (shop.getPriceLevel() == 1) {
                        price = "$";
                    } else if (shop.getPriceLevel() == 2 | shop.getPriceLevel() == 3) {
                        price = "$$";
                    } else {
                        price = "$$$";
                    }
                    String snippet =
                            shop.getFormattedAddress() + "\n" +
                                    "Price: " + price + "\n" +
                                    "Phone: " + shop.getFormattedPhone() + "\n" + hours + "\n" +
                                    "Website: " + shop.getWebsite();
                    googleMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                    .position(business)
                                    .title(shop.getName()))
                            .setSnippet(snippet);

                    mMap.setOnMarkerClickListener(this::onMarkerClick);
                }
            }
        }
    }


    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        showButtons(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
        return true;
    }
    public void showButtons(Marker marker){
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        String details[] = marker.getSnippet().split(" ");
        String webAddress = details[details.length-1];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Button addButton =(Button)((MapsActivity) getContext()).findViewById(R.id.addButton2);
        addButton.setVisibility(View.VISIBLE);
        addButton.setY((height/2)-355);
        addButton.setX((width/2)-340);

        Button cancelButton =(Button)((MapsActivity) getContext()).findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setY((height/2)-355);
        cancelButton.setX((width/2)-5);

        Button website =(Button)((MapsActivity) getContext()).findViewById(R.id.website);
        website.setVisibility(View.VISIBLE);
        website.setY((height/2)-240);
        website.setX((width/2)-340);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                new RandomPicker().randomTitle(marker);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.hideInfoWindow();
                addButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                website.setVisibility(View.GONE);
                mMap.getUiSettings().setScrollGesturesEnabled(true);

            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSite(webAddress);
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public void onClick(View view) {
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                Marker winnerArray = RandomPicker.randomWinner();
                Marker winner = winnerArray;

                LatLng fullScreenPosition = new LatLng(winner.getPosition().latitude+.01,winner.getPosition().longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fullScreenPosition, 14));
                winner.showInfoWindow();
                Log.d(log,winner.getPosition().toString());
                winner.getPosition();
                String details[] = winner.getSnippet().split(" ");
                String webAddress = details[details.length-1];

                Button addButton =(Button)((MapsActivity) getContext()).findViewById(R.id.addButton2);
                addButton.setVisibility(View.GONE);
                Button cancelButton =(Button)((MapsActivity) getContext()).findViewById(R.id.cancelButton);
                cancelButton.setVisibility(View.GONE);

                Button winnersSite = (Button)((Activity) getContext()).findViewById(R.id.website);
                winnersSite.setVisibility(View.VISIBLE);
                winnersSite.setY((height/2)+1105);
                winnersSite.setX(width+200);

                Button winnersDirections = (Button)((Activity) getContext()).findViewById(R.id.directions);
                winnersDirections.setVisibility(View.VISIBLE);
                winnersDirections.setY((height/2)+1215);
                winnersDirections.setX(width+200);

                Button startOver = (Button)((Activity) getContext()).findViewById(R.id.restart);
                startOver.setVisibility(View.VISIBLE);
                startOver.setY(height);
                startOver.setX(width);

                winnersSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchSite(webAddress);
                    }
                });

                winnersDirections.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchDirections(winner.getPosition());
                    }
                });
                winnersSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchSite(webAddress);
                    }
                });
                startOver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigateUpTo(new Intent(((Activity) getContext()),Cuisine.class));
                        startActivity(getIntent());
                    }
                });
                mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
                    @Override
                    public void onInfoWindowClose(@NonNull Marker marker) {
                        winnersSite.setVisibility(View.GONE);
                        winnersDirections.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    //--------------------------------------- HELPER METHODS ---------------------------------------

    public double toMeters(double miles) {
        double multiplier = 1609.34;
        double meters = miles * multiplier;
        Log.i("Meters", Double.toString(meters));
        Log.i("Miles", Double.toString(miles));
        return meters;
    }

    public void startLoadingScreen(int milliSeconds) {
        Intent loading = new Intent(MapsActivity.this, LoadingScreen.class);
        loading.putExtra("milliSeconds", milliSeconds);
        startActivity(loading);
        overridePendingTransition(0, 0);
    }
    public static void launchSite(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    @SuppressLint("MissingPermission")
    public void launchDirections(LatLng destination){
        String thisLatLng = destination.toString();
        thisLatLng = thisLatLng.substring(10,thisLatLng.length()-1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&origin"+userLocation+"&destination="+thisLatLng));
        startActivity(mapIntent);
    }
    public static class contextBuilder extends MapsActivity {
        private  static Context context;
        public  static void setContext(Context parentContext){
            context = parentContext;
        }
        public static Context getContext() {
            return context;
        }
    }
    }
