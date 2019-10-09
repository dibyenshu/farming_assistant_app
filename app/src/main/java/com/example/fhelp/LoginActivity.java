package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button login;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login);
        login = findViewById(R.id.button_login);
        username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put(getResources().getString(R.string.table_username),username.getText().toString());
                    jsonObject.put(getResources().getString(R.string.table_password),password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("farming_assistant",0);
                String urlString = "http://192.168.1.25:5000/login";
                URL url;
                try {
                    url = new URL(urlString);
                    new loginPost().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class loginPost extends AsyncTask<URL,Void,Integer>{

        @Override
        protected Integer doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setConnectTimeout(2000);
                urlConnection.setReadTimeout(3000);
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                out.write(jsonObject.toString());
                out.close();

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String responseData = convertStreamToString(inputStream);

                Integer httpResult = Integer.parseInt(responseData);
                return  httpResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer results){
            int result = -1;
            if(results!=null) result = results;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("farming_assistant",0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("session",result);
            editor.commit();
//            Log.e("session",String.valueOf(result));

        }

        String convertStreamToString(InputStream inputStream){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            try {
                line=reader.readLine();
                return line;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
        }
    }
}
