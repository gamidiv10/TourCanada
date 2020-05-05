package com.example.cloudproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationDetailsActivity extends AppCompatActivity {

    String location_name, city, type, keyFeatures, imagePath;
    ArrayList<JSONObject> locations_list = new ArrayList<>();
    JSONObject location_obj;
    Button btn_book;
    ImageView image;
    TextView location, keyFeat, loc_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        image = findViewById(R.id.image);
        location = findViewById(R.id.location_tv);
        keyFeat = findViewById(R.id.keyfeat_content);
        loc_type = findViewById(R.id.type_content);

        Intent intent = getIntent();

        btn_book = findViewById(R.id.btn_book);
        if (intent.getStringExtra("name") != null) {
            location_name = intent.getStringExtra("name");
        }
        if (intent.getStringExtra("dest") != null) {
            city = intent.getStringExtra("dest");
        }
        if (intent.getStringExtra("imagePath") != null) {
            imagePath = intent.getStringExtra("imagePath");
        }
        if (intent.getStringExtra("type") != null) {
            type = intent.getStringExtra("type");
        }
        if (intent.getStringExtra("keyFeatures") != null) {
            keyFeatures = intent.getStringExtra("keyFeatures");
        }

        Picasso.get().load(imagePath).into(image);
        location.setText(location_name + ", " + city);
        loc_type.setText(type);
        keyFeat.setText(keyFeatures);

        for (int i = 0; i < locations_list.size(); i++) {
            JSONObject obj= locations_list.get(i);
            try {
                if(obj.get("name").toString().toLowerCase().contains(location_name.toLowerCase()))
                {
                    location_obj = obj;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketBooking();


            }
        });
    }
    public void ticketBooking ()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("dest", city);
        startActivity(intent);
    }
}
