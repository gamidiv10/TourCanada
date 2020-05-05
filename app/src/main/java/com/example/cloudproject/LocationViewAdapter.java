package com.example.cloudproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vamsi Gamidi on 2020-03-24.
 */
public class LocationViewAdapter extends RecyclerView.Adapter<LocationViewAdapter.ViewHolder> implements Serializable {

    private ArrayList<JSONObject> locationsList;
    private Context context;

    public LocationViewAdapter(ArrayList<JSONObject> locationsList, Context context) {
        this.locationsList = locationsList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_view_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final JSONObject obj = locationsList.get(position);
        try {
            Picasso.get().load(obj.get("imagePath").toString()).into(holder.image);
            holder.txt_locationName.setText(obj.get("name").toString());
            holder.txt_cityName.setText(", " + obj.get("location").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LocationDetailsActivity.class);
                intent.putExtra("name", holder.txt_locationName.getText().toString());
                try {
                    intent.putExtra("dest", obj.get("location").toString());
                    intent.putExtra("imagePath", obj.get("imagePath").toString());
                    intent.putExtra("type", obj.get("type").toString());
                    intent.putExtra("keyFeatures", obj.get("keyFeatures").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_locationName, txt_cityName;
        RelativeLayout parent_layout;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_locationName = itemView.findViewById(R.id.txt_locationName);
            txt_cityName = itemView.findViewById(R.id.txt_city);
            image = itemView.findViewById((R.id.image));
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
