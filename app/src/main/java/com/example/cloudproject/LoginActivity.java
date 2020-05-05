package com.example.cloudproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final Pattern PATTERN_PASSWORD =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,24}" +
                    "$");

    TextInputEditText emailEditText, passwordEditText;
    Button btnLogin;
    TextView createAccountTv;
    public String email, password;
    TextInputLayout emailInputLayout, passwordInputLayout;
    String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent =getIntent();
        if (intent.getStringExtra("dest") != null) {
            destination = intent.getStringExtra("dest");
        }
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        createAccountTv = findViewById(R.id.tv_createAccount);
        btnLogin = findViewById(R.id.btn_login);
        emailInputLayout = findViewById(R.id.inputLayout_email);
        passwordInputLayout = findViewById(R.id.inputLayout_password);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEMAIL() & validatePassword()) {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("otp", "");
                        object.put("password", passwordEditText.getText().toString().trim());
                        object.put("email", emailEditText.getText().toString().trim());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        URL url = new URL("http://100.26.195.40:82/auth/login");

                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url.toString(),
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                           String resp  = response.get("status").toString();
                                           if (resp.equals("success"))
                                            {
                                                Intent intent = new Intent(LoginActivity.this, AuthenticationActivity.class);
                                                intent.putExtra("email", emailEditText.getText().toString().trim());
                                                intent.putExtra("dest", destination);
                                                startActivity(intent);
                                            }
                                           else
                                           {
                                               Log.e("Response", "Invalid Credentials");
                                               Toast toast = Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT);
                                               toast.show();
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
                        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(objectRequest);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignup = new Intent(getApplicationContext(), SignupActivity.class);
                intentToSignup.putExtra("dest", destination);
                startActivity(intentToSignup);
            }
        });
    }



    public Boolean validateEMAIL() {
        email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailInputLayout.setError("Field should not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Please enter a valid email");
            return false;
        } else {
            emailInputLayout.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordInputLayout.setError("Field should not be empty");
            return false;
        } else if (!PATTERN_PASSWORD.matcher(password).matches()) {
            passwordInputLayout.setError("Password should be between 8 to 24 character\n" +
                    "at least 1 special character [@#$%^&+=]\n" +
                    "at least 1 digit\n" +
                    "at least 1 capital letter\n" +
                    "at least 1 small letter\n");
            passwordEditText.setText("");
            return false;
        } else {
            passwordInputLayout.setError(null);
            return true;
        }

    }
}
