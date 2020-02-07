package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class addCrop extends AppCompatActivity {

    EditText editText_cropName;
    Button button_addCrop;
    String urlString;
    URL url;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);
        editText_cropName = findViewById(R.id.cropName);
        button_addCrop = findViewById(R.id.addCrop);
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
                if(editText_cropName.getText().toString().toLowerCase().equals("rice")){
                    try {
                        jsonObject.put(editText_cropName.getText().toString().toLowerCase(),"1");
                        jsonObject.put("wheat","0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        jsonObject.put(editText_cropName.getText().toString().toLowerCase(),"1");
                        jsonObject.put("rice","0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
}
