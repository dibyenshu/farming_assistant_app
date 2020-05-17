package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class addCrop extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button_addCrop;
    String urlString;
    ArrayList<String> crops;
    URL url;
    JSONObject jsonObject,detailsJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        button_addCrop = findViewById(R.id.addCrop);
        radioGroup = findViewById(R.id.cropRadioGroup);
        crops = new ArrayList<String>();
        populateCrops();


        SharedPreferences preferences = this.getSharedPreferences("farming_assistant", Context.MODE_PRIVATE);
        urlString = preferences.getString("url", "not found");
        int sessionId = preferences.getInt("sessionId", -1);
        urlString = urlString + "addCrop/" + String.valueOf(sessionId);

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        button_addCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonObject = new JSONObject();
                detailsJsonObject = new JSONObject();
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioButtonId);
                //logic to build up the json object
                try{
                    for(int i=0;i<crops.size();i++){
                        if(radioButton.getText().toString().equals(crops.get(i))) jsonObject.put(crops.get(i),"1");
                        else jsonObject.put(crops.get(i),"0");
                    }
                    getDetails();
                    jsonObject.put("details",detailsJsonObject);
                }catch(Exception ex){
                    Log.e("error",ex.toString());
                }

                //jsonObject formed
                new AddCropPost().execute(url);
            }
        });


    }

    private class AddCropPost extends AsyncTask<URL,Void,Integer>{

        @Override
        protected Integer doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

//                Log.e("json",jsonObject.toString());

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                Integer httpResult = new Integer(urlConnection.getResponseCode());
                return  httpResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void populateCrops(){
        crops.add(getResources().getString(R.string.crop_rice));
        crops.add(getResources().getString(R.string.crop_wheat));
        crops.add(getResources().getString(R.string.crop_mungBean));
        crops.add(getResources().getString(R.string.crop_millet));
        crops.add(getResources().getString(R.string.crop_tea));
        crops.add(getResources().getString(R.string.crop_maize));
    }

    private void getDetails(){
        try {
            detailsJsonObject.put(getResources().getString(R.string.details_precipitation),((EditText)findViewById(R.id.precipitationEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_temp),((EditText)findViewById(R.id.tempEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_humidity),((EditText)findViewById(R.id.humidityEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_moisture),((EditText)findViewById(R.id.moistureEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_soilTemp),((EditText)findViewById(R.id.soilTempEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_ph),((EditText)findViewById(R.id.phEditText)).getText().toString());
            detailsJsonObject.put(getResources().getString(R.string.details_waterPh),((EditText)findViewById(R.id.waterPhEditText)).getText().toString());

        }catch(Exception ex){
            Log.e("error",ex.toString());
        }
    }
}
