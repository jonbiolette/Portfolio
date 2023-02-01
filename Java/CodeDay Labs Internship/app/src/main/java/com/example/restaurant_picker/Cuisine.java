package com.example.restaurant_picker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapters.CuisineAdapter;
import basic_class.ItemCuisine;
import basic_class.RecyclerItemClickListener;

public class Cuisine extends Fragment {

    private List<ItemCuisine> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CuisineAdapter mAdapter;
    private AppCompatActivity appCompatActivity;
    private String log = "LOG";

    public void onCreate(Bundle a) {
        super.onCreate(a);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cuisine_view, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mAdapter = new CuisineAdapter(setupCuisine(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        appCompatActivity = (AppCompatActivity) getActivity();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(appCompatActivity, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                final String[] selectedCuisine = {itemList.get(position).getCuisine()};
                if (selectedCuisine[0] == "TYPE MY OWN") {
                    Context thisContext = container.getContext();
                    AlertDialog.Builder build = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                    //new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom)
                    build.setTitle("Type your choice");
                    build.setCancelable(true);
                    EditText input = new EditText(thisContext);
                    build.setView(input);
                    input.setMaxLines(1);
                    input.setTextColor(-16777216);
                    build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (input.getText().toString().equals("")){
                                Toast.makeText(thisContext, "Please enter a food preference", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d(log, input.getText().toString());
                                selectedCuisine[0] = input.getText().toString();
                                dialogInterface.cancel();
                                Intent intent = new Intent(getActivity(), UserInput.class);
                                intent.putExtra("selectedCuisine", selectedCuisine[0].toLowerCase() + " restaurant");
                                startActivity(intent);
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    build.create();
                    build.show();
                }
                else {
                    Intent intent = new Intent(getActivity(), UserInput.class);
                    intent.putExtra("selectedCuisine", selectedCuisine[0].toLowerCase() + " restaurant");
                    startActivity(intent);
                }
            }
        }));
        return view;
    }

    private List<ItemCuisine> setupCuisine() {
        itemList = new ArrayList<>();
        String allCuisines[] = {"TYPE MY OWN", "FAST FOOD", "MEXICAN", "JAPANESE", "ITALIAN", "INDIAN", "CHINESE", "MEDITERRANEAN", "VEGAN", "FRENCH", "THAI", "COFFEE"};
        String images[] = {
                "https://images.pexels.com/photos/1113520/pexels-photo-1113520.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/2725744/pexels-photo-2725744.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/2092897/pexels-photo-2092897.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/271715/pexels-photo-271715.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1438672/pexels-photo-1438672.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/958545/pexels-photo-958545.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/7364117/pexels-photo-7364117.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1618898/pexels-photo-1618898.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1143754/pexels-photo-1143754.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/323682/pexels-photo-323682.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/699953/pexels-photo-699953.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        };


        for (int i = 0; i < allCuisines.length; i++) {
            ItemCuisine item = new ItemCuisine();
            item.setCuisine(allCuisines[i]);
            item.setImage(images[i]);
            itemList.add(item);
        }
        return itemList;
    }
}
