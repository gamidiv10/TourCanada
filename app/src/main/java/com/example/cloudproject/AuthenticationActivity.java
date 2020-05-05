package com.example.cloudproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;

public class AuthenticationActivity extends AppCompatActivity {

    Button btn_submitCode;
    EditText input_code;
    String email, authToken, dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Intent intent = getIntent();
        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email");
        }
        if (intent.getStringExtra("dest") != null) {
            dest = intent.getStringExtra("dest");
        }
        btn_submitCode = findViewById(R.id.btn_submitCode);
        input_code = findViewById(R.id.input_code);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        btn_submitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject object = new JSONObject();
                try {
                    object.put("otp", input_code.getText().toString().trim());
                    object.put("password", "");
                    object.put("email", email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    URL url = new URL("http://100.26.195.40:82/auth/loginSecond");

                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url.toString(),
                            object,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        Log.e("Response", response.toString());
                                        String resp  = response.get("status").toString();
                                        authToken = response.get("Authorization").toString();

                                        if (resp.equals("success"))
                                        {
                                            Intent intent = new Intent(AuthenticationActivity.this, TicketBookActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("dest", dest);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Log.e("Response", "Invalid Code");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("Response", response.toString());

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
        });
    }
}
