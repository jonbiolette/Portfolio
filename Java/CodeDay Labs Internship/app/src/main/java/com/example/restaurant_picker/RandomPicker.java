package com.example.restaurant_picker;

import com.google.android.gms.maps.model.Marker;

import java.util.Random;

public class RandomPicker {
    private static Marker[] restaurantTitleArray  = new Marker[1];
    static int pointer = 0;
    public static void randomTitle(Marker clicked) {
        try {
            restaurantTitleArray[pointer] = clicked;
        } catch (ArrayIndexOutOfBoundsException e) {
            restaurantTitleArray = doubleSize(restaurantTitleArray, clicked);

        }
        pointer++;
    }
    public static Marker randomWinner(){
        Random random = new Random();
        int upperbound = restaurantTitleArray.length;
        int selected = random.nextInt(upperbound);
        Marker winner = restaurantTitleArray[selected];

        return winner;
    }
    public static Marker[] doubleSize(Marker[] arr, Marker clicked){
        Marker cloneArray[] = new Marker[restaurantTitleArray.length+1];
        for(int i = 0;i < restaurantTitleArray.length; i++){
            cloneArray[i] = restaurantTitleArray[i];
        }
        cloneArray[cloneArray.length -1] = clicked;
        return cloneArray;
    }
}
