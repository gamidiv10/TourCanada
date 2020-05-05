package com.example.cloudproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import static com.example.cloudproject.R.id;
import static com.example.cloudproject.R.layout;


public class SignupActivity extends AppCompatActivity {
    public EditText etEnterEmail, etEnterPassword, etReenterPassword, etUserName,etFirstName, etLastName, etPhoneNumber;
    public Button btnSignup;
    public String enteredEmail, enteredPassword, reenteredPassword, firstName, lastName, phoneNumber, countryCode, gender;
    public TextInputLayout inputLayoutEnterEmail, inputLayoutEnterPassword, inputLayoutReenterPassword;
    public TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutPhoneNumber, userName;
    public Spinner countryCodeSpinner, genderSpinner;
    String destination;

    public static final Pattern PATTERN_PASSWORD =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,24}" +               //at least between 8 to 24 characters
                    "$");
    String passwordError = "Password should be between 8 to 24 character\n" +
            "at least 1 special character [@#$%^&+=]\n" +
            "at least 1 digit\n" +
            "at least 1 capital letter\n" +
            "at least 1 small letter\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_signup);
        Intent intent = getIntent();
        if (intent.getStringExtra("dest") != null) {
            destination = intent.getStringExtra("dest");
        }
        etEnterEmail = findViewById(id.et_enterEmail);
        etEnterPassword = findViewById(id.et_enterPassword);
        etReenterPassword = findViewById(id.et_reenterPassword);
        etFirstName = findViewById(id.et_firstName);
        etLastName = findViewById(id.et_lastName);
        genderSpinner = findViewById(id.spinner_gender);
        btnSignup = findViewById(id.btn_signup);
        inputLayoutEnterEmail = findViewById(id.inputLayout_enterEmail);
        inputLayoutEnterPassword = findViewById(id.inputLayout_enterPassword);
        inputLayoutReenterPassword = findViewById(id.inputLayout_reenterPassword);
        inputLayoutFirstName = findViewById(id.inputLayout_firstName);
        inputLayoutPhoneNumber = findViewById(id.inputLayout_phoneNumber);
        countryCodeSpinner = findViewById(id.spinner_countryCode);
        etPhoneNumber = findViewById(id.et_phoneNumber);

        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isvalidEmail() & isvalidPassword() & isvalidConfirmPassword() & isvalidUsername() & isvalidPhoneNumber())
                {
                    JSONObject object = new JSONObject();
                    try {

                        object.put("firstName", etFirstName.getText().toString().trim());
                        object.put("gender", gender.trim());
                        object.put("lastName", etLastName.getText().toString().trim());
                        object.put("password", etEnterPassword.getText().toString().trim());
                        object.put("email", etEnterEmail.getText().toString().trim());
                        object.put("phoneNo", etPhoneNumber.getText().toString().trim());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        URL url = new URL("http://100.26.195.40:82/user/");

                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url.toString(),
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        String resp  = null;
                                        try {
                                            resp = response.get("status").toString();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (resp.equals("success")) {
                                            Log.e("Response from server:", response.toString());
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            intent.putExtra("dest", destination);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Log.e("Response", "User already registered");
                                            Toast toast = Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Response from server:", error.toString());
                                        Toast toast = Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_SHORT);
                                        toast.show();

                                    }
                                });

                        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(objectRequest);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }
        public Boolean isvalidUsername() {
        firstName = etFirstName.getText().toString().trim();
        if (firstName.isEmpty()) {
            inputLayoutFirstName.setError("Field should not be empty");
            return false;
        } else {
            inputLayoutFirstName.setError(null);
            return true;
        }
    }


    public Boolean isvalidEmail() {
        enteredEmail = etEnterEmail.getText().toString().trim();
        if (enteredEmail.isEmpty()) {
            inputLayoutEnterEmail.setError("Field should not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            inputLayoutEnterEmail.setError("Please enter a valid EMAIL address");
            return false;
        } else {
            inputLayoutEnterEmail.setError(null);
            return true;
        }
    }

    private Boolean isvalidPassword() {
        enteredPassword = etEnterPassword.getText().toString().trim();
        if (enteredPassword.isEmpty()) {
            inputLayoutEnterPassword.setError("Field should not be empty");
            return false;
        } else if (!PATTERN_PASSWORD.matcher(enteredPassword).matches()) {
            inputLayoutEnterPassword.setError(passwordError);
            etEnterPassword.setText("");
            return false;
        } else {
            inputLayoutEnterPassword.setError(null);
            return true;
        }
    }

    private Boolean isvalidConfirmPassword() {
        reenteredPassword = etReenterPassword.getText().toString().trim();
        if (reenteredPassword.isEmpty()) {
            inputLayoutReenterPassword.setError("Field should not be empty");
            return false;
        } else if (!enteredPassword.equals(reenteredPassword)) {
            inputLayoutReenterPassword.setError("Doesn't match the given Password");
            etReenterPassword.setText("");
            return false;
        } else {
            inputLayoutReenterPassword.setError(null);
            return true;
        }
    }

    private Boolean isvalidPhoneNumber() {
        phoneNumber = etPhoneNumber.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            inputLayoutPhoneNumber.setError("Field should not be empty");
            return false;
        } else if (phoneNumber.length() != 10) {
            inputLayoutPhoneNumber.setError("Number not less than 10 digits");
            etPhoneNumber.setText("");
            return false;
        } else {
            inputLayoutPhoneNumber.setError(null);
            return true;
        }
    }
}



