package com.example.restaurant_picker;

import static com.example.restaurant_picker.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.restaurant_picker.PlaceID.PlaceDetails;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextSearch extends MapsActivity{
    BusinessDetails[] dataStore;
    private GoogleMap mMap;
    static PlaceDetails place;
    int price;
    Context context;

    public void find(String preference, double distance, com.google.android.gms.maps.model.LatLng current,GoogleMap googleMap,
                     int pricePref, Context context) {
        this.context = context;
        mMap = googleMap;
        price=pricePref;
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
        "location=" + slice(current.latitude) + "%2C" + slice(current.longitude) + "&" +
        "radius=" + distance + "&" +
        "type=restaurant&" +
        "keyword=" + preference + "&" +
        "key=" + MAPS_API_KEY;
        RetrieveMapDataTask task = new RetrieveMapDataTask();
        task.execute(url);
        //this.context = context;

    }

    private static String slice(double value) {
        double num = value;
        DecimalFormat df = new DecimalFormat("#.#######");
        return df.format(num);
    }

    @SuppressLint("StaticFieldLeak")
    public class RetrieveMapDataTask extends AsyncTask<String, Void, BusinessDetails[]> {
        int size;
        @Override
        protected BusinessDetails[] doInBackground(String... url) {

            JSONObject Jobject = null;
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(url[0])
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                Jobject = new JSONObject(response.body().string());
                JSONArray Jarray = Jobject.getJSONArray("results");
                size = Jarray.length();
                dataStore = new BusinessDetails[size];
                for (int counter = 0; counter < size; counter++) {
                    JSONObject object = Jarray.getJSONObject(counter);
                    BusinessDetails business = new BusinessDetails(object);
                    new PlaceID().find(business.getPlaceID());
                    if (place.priceLevel <= price) {
                        business.setFormattedAddress(place.formattedAddress);
                        business.setFormattedPhone(place.formattedPhone);
                        business.setRating(place.rating);
                        business.setNumRatings(place.numRatings);
                        business.setHours(place.weeklyHours);
                        business.setWebsite(place.website);
                        business.setPriceLevel(place.priceLevel);
                        dataStore[counter] = business;
                    } else {
                        break;
                    }
                    response.close();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return dataStore;
        }

        @Override
        protected void onPostExecute(BusinessDetails[] array) {
                postResults(array);
            }
        }
        public void postResults(BusinessDetails[] array){
            new MapsActivity().pinLocations(array, mMap,context);
        }

    public class BusinessDetails {
        String status; String name;String userRating;
        String address;String openNow; String placeID;
        String geo; String formattedAddress;
        String formattedPhone; String hours; int priceLevel;
        int rating; int numRatings; String website;

        LatLng[] locationArray;
        String[] weeklyHours = new String[7];
        public BusinessDetails(JSONObject business){
            try {
                name = business.getString("name");
                geo = business.getString("geometry");
                setLatLng(geo);
                placeID = business.getString("place_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        //------------------------------------ GET'ERS/SET'ERS -------------------------------------
        }
        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getStatus(){
            return status;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getFormattedAddress(){
            return formattedAddress;
        }
        public void setFormattedAddress(String formattedAddress){
            this.formattedAddress = formattedAddress;
        }
        public String getFormattedPhone(){
            return formattedPhone;
        }
        public void setFormattedPhone(String formattedPhone){
            this.formattedPhone = formattedPhone;
        }
        public int getPriceLevel(){
            return priceLevel;
        }
        public void setPriceLevel(int priceLevel){
            this.priceLevel = priceLevel;
        }
        public int getRating(){
            return rating;
        }
        public void setRating(int rating){
            this.rating = rating;
        }
        public int getNumRatings(){
            return numRatings;
        }
        public void setNumRatings(int ratings){
            this.numRatings = ratings;
        }
        public String getWebsite(){
            return website;
        }
        public void setWebsite(String website){
            this.website = website;
        }
        public String[] getHours(){
            return weeklyHours;
        }
        public void setHours(String[] weeklyHours){
            this.weeklyHours = weeklyHours;
        }

        public LatLng getLatLng(int type){
            return locationArray[type];
        }
        public void setLatLng(String geoMass){
            double latMain; double lngMain;
            double latNorth; double lngNorth;
            double latSouth; double lngSouth;
            locationArray = new LatLng[3];

            String[] locationInfo = geoMass.split(":");
            //Main viewport
            latMain = Double.parseDouble(locationInfo[2].substring(0, locationInfo[2].length() - 6));
            lngMain = Double.parseDouble(locationInfo[3].substring(0, locationInfo[3].length() - 12));
            LatLng mainLocation = new LatLng(latMain,lngMain);
            locationArray[0] = mainLocation;

            //Northeast viewport
            latNorth = Double.parseDouble(locationInfo[6].substring(0, locationInfo[6].length() - 6));
            lngNorth = Double.parseDouble(locationInfo[7].substring(0, locationInfo[7].length() - 13));
            LatLng northEastLocation = new LatLng(latNorth,lngNorth);
            locationArray[1] = northEastLocation;


            //Southwest viewport
            latSouth = Double.parseDouble(locationInfo[9].substring(0, locationInfo[9].length() - 6));
            lngSouth = Double.parseDouble(locationInfo[10].substring(0, locationInfo[10].length() - 3));
            LatLng southWestLocation = new LatLng(latSouth,lngSouth);
            locationArray[2] = southWestLocation;


        }

        public String getPlaceID(){
            return placeID;
        }
        public void setPlaceID(String placeID){
            this.placeID = placeID;
        }
        @NonNull
        public String toString(){
            return ("Business name: " + name +"\n"+
                    "Place ID: " + placeID + "\n"+
                    "LatLing: " + getLatLng(1));
        }
    }
}