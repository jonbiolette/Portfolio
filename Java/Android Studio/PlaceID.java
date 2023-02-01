package com.example.restaurant_picker;

import static com.example.restaurant_picker.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PlaceID {
    private GoogleMap mMap;
    public void find(String id) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" +
                id +
                "&key=" + MAPS_API_KEY;
        PlaceID.RetrieveMapDataTask task = new PlaceID.RetrieveMapDataTask();
        task.doInBackground(url);

    }

    @SuppressLint("StaticFieldLeak")
    public class RetrieveMapDataTask extends AsyncTask<String, Void, PlaceDetails> {
        PlaceDetails place;
        PlaceDetails[] places = new PlaceDetails[1];
        @Override
        protected PlaceDetails doInBackground(String ... url) {
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
                place = new PlaceDetails(Jobject.getJSONObject("result"));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            TextSearch.place = this.place;
            return place;
        }

        @Override
        protected void onPostExecute(PlaceDetails place) {

            TextSearch.place = this.place;
        }
    }
    public static PlaceDetails[] doubleSize(PlaceDetails[] arr, PlaceDetails place){
        PlaceDetails cloneArray[] = new PlaceDetails[arr.length+1];
        for(int i = 0;i < arr.length; i++){
            cloneArray[i] = arr[i];
        }
        cloneArray[cloneArray.length -1] = place;
        return cloneArray;
    }

    public static class PlaceDetails{
        String businessStatus; String formattedAddress;
        String formattedPhone; String hours; int priceLevel;
        int rating; int numRatings; String website;

       JSONArray JsonArrHours;
       String[] weeklyHours = new String[7];

        public PlaceDetails(JSONObject place) {
            try {
                businessStatus = place.getString("business_status");
                formattedAddress = place.getString("formatted_address");
                formattedPhone = place.getString("formatted_phone_number");
                priceLevel = place.getInt("price_level");
                rating = place.getInt("rating");
                numRatings = place.getInt("user_ratings_total");
                website = place.getString("website");
                JsonArrHours = place.getJSONObject("opening_hours").getJSONArray("weekday_text");
                setHours();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getStatus() {
            return businessStatus;
        }
        public void setStatus(String businessStatus){
            this.businessStatus = businessStatus;
        }
        public String getFormattedAddress(){
            return formattedAddress;
        }
        public String getFormattedPhone(){
            return formattedPhone;
        }
        public int getPriceLevel(){
            return priceLevel;
        }
        public int getRating(){
            return rating;
        }
        public int getNumRatings(){
            return numRatings;
        }
        public void setHours(){
            for (int i = 0; i < JsonArrHours.length(); i++) {
                try {
                    weeklyHours[i] = JsonArrHours.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        public String[] getHours() {
            return weeklyHours;
        }
        @Override
        public String toString(){
            return "Address: "+ formattedAddress +"\n" +
                    "Phone: "+ formattedPhone +"\n" +
                    "Price level: "+ priceLevel +"\n" +
                    "Rating: "+ rating +"/5" +"\n" +
                    "Hours: " +"\n" +
                    getHours().toString();

        }
    }
}