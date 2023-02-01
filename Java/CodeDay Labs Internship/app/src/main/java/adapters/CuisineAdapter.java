package adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurant_picker.R;

import java.util.List;

import basic_class.ItemCuisine;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.MyViewHolder> {

    private List<ItemCuisine> items;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView cuisine;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            cuisine = (TextView) view.findViewById(R.id.tv_cuisine_name);
            imageView = (ImageView) view.findViewById(R.id.iv_cuisine);
        }
    }


    public CuisineAdapter(List<ItemCuisine> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuisine, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemCuisine itemCuisine = items.get(position);
        holder.cuisine.setText(itemCuisine.getCuisine());
        Glide.with(context)
                .load(Uri.parse(itemCuisine.getImage()))
                .circleCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
