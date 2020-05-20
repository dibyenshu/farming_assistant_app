package com.example.fhelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Frag1 extends Fragment {
    private String urlString;
    private RecyclerView cropList;
    private ArrayList<String> cropListString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag1_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //create recycler view

        cropList = this.getActivity().findViewById(R.id.cropList);
        cropList.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
        cropList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //code for populating recycler view
        SharedPreferences preferences = this.getActivity().getSharedPreferences("farming_assistant", Context.MODE_PRIVATE);
        urlString = preferences.getString("url", "not found");
        int sessionId = preferences.getInt("sessionId", -1);
        urlString = urlString + "getCrops/" + sessionId;
        URL url;
        try {
            url = new URL(urlString);
            new getCrops().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class getCrops extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                result = convertStreamToString(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("onpost", result);
            cropListString = new ArrayList<>(Arrays.asList(result.split(",")));
            cropList.setAdapter(new CropListAdapter(cropListString));
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
