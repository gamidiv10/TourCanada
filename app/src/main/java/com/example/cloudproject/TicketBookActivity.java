package com.example.cloudproject;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TicketBookActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Spinner destinationSpinner;
    Spinner sourceSpinner;
    String destinationString = "";
    List<String> destinationList = new ArrayList<>();
    List<String> destList = new ArrayList<>();
    List<String> sourceList = new ArrayList<>();
    List<String> srcList = new ArrayList<>();
    private ArrayList<JSONObject> packageList = new ArrayList<>();
    private EditText departDateText;
    Button departDateButton;
    private EditText returnDateText;
    Button returnDateButton;
    boolean flag = true;
    private EditText ticketPrice;
    private EditText cardNumber;
    private EditText cvvNumber;
    Button submitDetails;
    int departDay, departMonth, departYear;
    int returnDay, returnMonth, returnYear;
    TextView priceTv;
    TextView cardTv;
    TextView cvvTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);
        destinationSpinner = findViewById(R.id.destination);
        sourceSpinner = findViewById(R.id.source);
        Intent intent = getIntent();
        if (intent.getStringExtra("dest") != null) {
            destinationString = intent.getStringExtra("dest");
        }
        destList.add(0, destinationString);
        sourceList.add(0, "Select Source:");
        departDateButton = findViewById(R.id.depart_button);
        departDateText = findViewById(R.id.depart_date);
        returnDateButton = findViewById(R.id.return_button);
        returnDateText = findViewById(R.id.return_date);
        ticketPrice =  findViewById(R.id.ticket_price_text);
        cardNumber = findViewById(R.id.card_number);
        cvvNumber = findViewById(R.id.cvv);
        submitDetails = findViewById(R.id.submit);
        priceTv = findViewById(R.id.price_tv);
        cardTv = findViewById(R.id.card_tv);
        cvvTv = findViewById(R.id.cvv);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://52.90.211.209:81/package/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONArray packages = ((JSONArray) response.get("data"));
                            for (int i=0; i<packages.length(); i++) {
                                packageList.add((JSONObject) packages.get(i));
                            }
                            Log.e("package_list", packageList.toString());
                            for (int i = 0; i< packageList.size(); i++)
                            {
                                try {

                                    sourceList.add(packageList.get(i).get("source").toString());
                                    destinationList.add(packageList.get(i).get("destination").toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e("Source List", sourceList.toString());

                            for (int i = 0; i< destinationList.size(); i++)
                            {
                                if(!destList.contains(destinationList.get(i)))
                                {
                                    destList.add(destinationList.get(i));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.toString());

                    }
                }
        );
        requestQueue.add(objectRequest);
        ArrayAdapter<String> sourceAdapter;
        sourceAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sourceList);

        ArrayAdapter<String> destinationAdapter;
        destinationAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, destList);

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        destinationSpinner.setAdapter(destinationAdapter);
        sourceSpinner.setAdapter(sourceAdapter);

        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPrice();

                    String item =  adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(adapterView.getContext(), "Selected: " +item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPrice();
                    String item =  adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(adapterView.getContext(), "Selected: " +item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        departDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDepartDate();

            }
        });

        returnDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReturnDate();

            }
        });

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!sourceSpinner.getSelectedItem().toString().equals(destinationSpinner.getSelectedItem().toString()))
                        && (cardNumber.getText().length() == 16) && (cvvNumber.getText().length() == 3))
                {
                    if ( returnYear > departYear) {
                        if (returnMonth > departMonth || returnMonth < departMonth || returnMonth == departMonth)
                        {
                            if (returnDay > departDay || returnDay < departDay || returnDay == departDay)
                            {
                                Toast.makeText(getApplicationContext(), "Ticket booked", Toast.LENGTH_LONG).show();
                                generateTicket();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Ticket cannot be booked", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    else if (returnYear == departYear)
                    {
                        if (returnMonth > departMonth)
                        {
                            Toast.makeText(getApplicationContext(), "Ticket booked", Toast.LENGTH_LONG).show();
                            generateTicket();
                        }
                        else if (returnMonth == departMonth)
                        {
                            if (returnDay == departDay || returnDay > departDay)
                            {
                                Toast.makeText(getApplicationContext(), "Ticket booked", Toast.LENGTH_LONG).show();
                                generateTicket();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Ticket cannot be booked", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ticket cannot be booked", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void generateTicket ()
    {
        Intent intent = new Intent(this, SampleticketActivity.class);
        intent.putExtra("source", sourceSpinner.getSelectedItem().toString());
        intent.putExtra("destination", destinationSpinner.getSelectedItem().toString());
        intent.putExtra("departDate", departDateText.getText().toString().trim());
        intent.putExtra("returnDate", returnDateText.getText().toString().trim());
        intent.putExtra("ticketPrice", ticketPrice.getText().toString().trim());
        startActivity(intent);
    }
    public void showDepartDate()
    {
        flag = true;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showReturnDate()
    {
        DatePickerDialog datePickerDialog1 = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog1.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i1+1 + "/" + i2 + "/" + i;
        if (flag == true)
        {
            departDateText.setText(date);
            departDay = i2;
            departMonth = i1+1;
            departYear = i;
            flag = false;
        }
        else
        {
            returnDateText.setText(date);
            returnDay = i2;
            returnMonth = i1+1;
            returnYear = i;
        }
    }
    public void getPrice()
    {
        for (int i = 0; i < packageList.size(); i++) {
            try {
                if (sourceSpinner.getSelectedItem().toString().equals(packageList.get(i).get("source").toString())
                        && destinationSpinner.getSelectedItem().toString().equals(packageList.get(i).get("destination").toString())) {
                    ticketPrice.setText(packageList.get(i).get("price").toString());
                    Log.e("Price is: ", ticketPrice.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
