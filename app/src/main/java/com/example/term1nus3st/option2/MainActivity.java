package com.example.term1nus3st.option2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.GenericArrayType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    private ImageView mLondon;
    private ImageView mNewyork;
    private ImageView mParis;
    private ImageView mYerevan;

    private TextView mTemperature;
    private TextView mPressure;

    private URL apixuLondonEndPoint;
    private URL apixuNewYorkEndPoint;
    private URL apixuParisEndPoint;
    private URL apixuYerevanEndPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            apixuLondonEndPoint = new URL("http://api.apixu.com/v1/current.json?key=5f6768877ec941038f2111630180602&q=London");
            apixuNewYorkEndPoint = new URL("http://api.apixu.com/v1/current.json?key=5f6768877ec941038f2111630180602&q=New%20York");
            apixuParisEndPoint = new URL("http://api.apixu.com/v1/current.json?key=5f6768877ec941038f2111630180602&q=Paris");
            apixuYerevanEndPoint = new URL("http://api.apixu.com/v1/current.json?key=5f6768877ec941038f2111630180602&q=Yerevan");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        mTemperature = (TextView) findViewById(R.id.textTemperature);
        mPressure = (TextView) findViewById(R.id.textPressure);

        mLondon = (ImageView) findViewById(R.id.imagelondon);
        mNewyork = (ImageView) findViewById(R.id.imagenewyork);
        mParis = (ImageView) findViewById(R.id.imageparis);
        mYerevan = (ImageView) findViewById(R.id.imageyerevan);


        mLondon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherTask().execute(apixuLondonEndPoint);
            }
        });

        mNewyork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherTask().execute(apixuNewYorkEndPoint);
            }
        });

        mParis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherTask().execute(apixuParisEndPoint);
            }
        });

        mYerevan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherTask().execute(apixuYerevanEndPoint);
            }
        });


    }


    private class WeatherTask extends AsyncTask<URL, Void, String> {

        String JSONtemperature;
        String JSONpressure;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            BufferedReader reader = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                connection.setRequestMethod("GET");

                connection.connect();
                int responsecode = connection.getResponseCode();
                if (responsecode != 200) {
                    Log.e("AsyncTask", "doInBackground: vata");
                } else {
                    InputStream responseBody = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(responseBody));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    String finalJson = buffer.toString();

                    JSONObject parentObject = new JSONObject(finalJson);

                    JSONObject currentObject = parentObject.getJSONObject("current");

                    JSONtemperature = currentObject.getString("temp_c");
                    JSONpressure = currentObject.getString("pressure_mb");


                    connection.disconnect();
                    responseBody.close();

                    Log.e("AsyncTask", "doInBackground: lava");

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mTemperature.setText(JSONtemperature + " °");
            mPressure.setText(JSONpressure);
            super.onPostExecute(s);
        }
    }

}
