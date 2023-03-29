package com.example.crop_prediction_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class weather extends AppCompatActivity {

    EditText city;
    TextView txtreult;
    Button btn;
    String username;
    String output;
    String cityname;
    public DatabaseReference databaseReference;
    private  final String url= "https://api.openweathermap.org/data/2.5/weather";
    private final String appid="344e1e820e1290966af44de92ac71502";

    DecimalFormat df = new DecimalFormat("#.##");
    String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtreult = findViewById(R.id.txtesult);
        btn = findViewById(R.id.btncity);

        SharedPreferences sh = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String get = sh.getString("address","default");
        Toast.makeText(getApplicationContext(),get,Toast.LENGTH_LONG).show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(weather.this,get,Toast.LENGTH_LONG).show();
                String tempurl ="https://api.openweathermap.org/data/2.5/weather?q="+get+"&appid=344e1e820e1290966af44de92ac71502";


                if(get.equals("")){
                    txtreult.setText("Pls Enter City Name");

                }else
                {
//                        tempurl = url + "?q" + get +  "&appid" + appid;
                }
                StringRequest stringrequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response!=null){
//                                Log.d("response",response);

                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String Des = jsonObject1.getString("description");
                                JSONObject jsonObject2 = jsonObject.getJSONObject("main");
                                double temp = jsonObject2.getDouble("temp") - 273.15;
                                cityname = jsonObject.getString("name");
                                output="current weather of  "+ cityname + "\n Temp: "+ df.format(temp)+"°C"+ "\n Description: " + Des;

                                txtreult.setText(output);

                                sharedata(output);



                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(weather.this,"response is null",Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(weather.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringrequest);

            }


        });


    }

    private void sharedata(String output) {

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("weather",output);
        myEdit.apply();
        myEdit.commit();
    }
}