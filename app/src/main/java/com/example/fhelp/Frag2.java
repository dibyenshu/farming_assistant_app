package com.example.fhelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Frag2 extends Fragment {

    TextView suggestion;
    EditText temp,humidity,rainfall;
    String urlString;
    Button button;
    URL url;
    JSONObject jsonObject;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        suggestion = this.getActivity().findViewById(R.id.suggestionTextView);
        button = this.getActivity().findViewById(R.id.suggestionButton);
        temp = this.getActivity().findViewById(R.id.tempEditText);
        humidity = this.getActivity().findViewById(R.id.humidityEditText);
        rainfall = this.getActivity().findViewById(R.id.rainfallEditText);
        jsonObject = new JSONObject();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("farming_assistant", Context.MODE_PRIVATE);
        urlString = preferences.getString("url", "not found");
        urlString = urlString + "getSuggestedCrops";
        try {
            url = new URL(urlString);
        }catch(Exception ex){
            Log.e("error","url error");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayload();
                new getSuggestedCrops().execute(url);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag2_layout, container, false);
    }

    private class getSuggestedCrops extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                result = convertStreamToString(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            suggestion.setText("The suggested crops in decreasing order of priority are:\n"+s);
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

    private void createPayload(){
        try {
            jsonObject.put("temp", temp.getText().toString());
            jsonObject.put("humidity",humidity.getText().toString());
            jsonObject.put("rainfall",rainfall.getText().toString());
        }catch(Exception ex){
            Log.e("Error",ex.toString());
        }
    }
}
