package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CropDetailsActivity extends AppCompatActivity {

    TextView humidity1,moisture1,ph1,precipitation1,soil_temp1,temp1,water_ph1;
    TextView humidity2,moisture2,ph2,precipitation2,soil_temp2,temp2,water_ph2;
    String urlString;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_details);
        String cropName = getIntent().getStringExtra("cropName");
        setTitle(cropName);
        humidity1 = findViewById(R.id.humidiy1TextView);
        humidity2 = findViewById(R.id.humidity2TextView);
        moisture1 = findViewById(R.id.moisture1TextView);
        ph1 = findViewById(R.id.ph1TextView);
        precipitation1 = findViewById(R.id.precipitation1TextView);
        soil_temp1 = findViewById(R.id.soilTemp1TextView);
        temp1 = findViewById(R.id.temp1TextView);
        water_ph1 = findViewById(R.id.waterPh1TextView);
        moisture2 = findViewById(R.id.moisture2TextView);
        ph2 = findViewById(R.id.ph2TextView);
        precipitation2 = findViewById(R.id.precipitation2TextView);
        soil_temp2 = findViewById(R.id.soilTemp2TextView);
        temp2 = findViewById(R.id.temp2TextView);
        water_ph2 = findViewById(R.id.waterPh2TextView);

        SharedPreferences preferences = this.getSharedPreferences("farming_assistant", Context.MODE_PRIVATE);
        urlString = preferences.getString("url", "not found");
        int sessionId = preferences.getInt("sessionId", -1);

        urlString = urlString + "getCropDetails/"+String.valueOf(sessionId) + "/" + cropName;

        try{
            url = new URL(urlString);
        }catch (Exception ex){
            Log.e("error",ex.toString());
        }

        new getCropDetails().execute(url);
    }

    private class getCropDetails extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject farmerCropDetails,cropDetailsRange;
                farmerCropDetails = jsonArray.getJSONObject(0);
                cropDetailsRange = jsonArray.getJSONObject(1);

                humidity1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_humidity)));
                moisture1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_moisture)));
                ph1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_ph)));
                precipitation1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_precipitation)));
                soil_temp1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_soilTemp)));
                temp1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_temp)));
                water_ph1.setText(farmerCropDetails.getString(getResources().getString(R.string.details_waterPh)));

                humidity2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_humidity)));
                moisture2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_moisture)));
                ph2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_ph)));
                precipitation2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_precipitation)));
                soil_temp2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_soilTemp)));
                temp2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_temp)));
                water_ph2.setText(cropDetailsRange.getString(getResources().getString(R.string.details_waterPh)));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = "emptyString";
            HttpURLConnection httpURLConnection;
            try{
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                result = convertStreamToString(httpURLConnection.getInputStream());

            }catch (Exception ex){
                Log.e("error",ex.toString());
            }
            return result;
        }

        String convertStreamToString(InputStream inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder readData = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    readData.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("readData", readData.toString());
            return readData.toString();
        }
    }
}
