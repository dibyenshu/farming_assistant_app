package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText username,name,dob,phNo,email,pass,passCopy;
    Button register;
    URL url;//change it
    String urlString;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.register);

        InitData();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1,pass2;
                pass1=pass.getText().toString();
                pass2=passCopy.getText().toString();
                if(pass1.equals(pass2)==false){
                    Toast.makeText(RegisterActivity.this,"Passwords don't match",Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e("passequal","here");
                    try {
                        jsonObject.put(getResources().getString(R.string.table_username),username.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_name),name.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_dob),dob.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_location),"22 87");//location
                        jsonObject.put(getResources().getString(R.string.table_phoneNo),phNo.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_email),email.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_password),pass.getText().toString());
                        jsonObject.put(getResources().getString(R.string.table_type),"1");//type
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new RegisterPost().execute(url);
                }
            }
        });
    }

    private void InitData(){
        urlString = "http://192.168.1.25:5000/registration";//change it
        username = findViewById(R.id.editText_username);
        name = findViewById(R.id.editText_name);
        dob = findViewById(R.id.editText_dob);
        phNo = findViewById(R.id.editText_phno);
        email = findViewById(R.id.editText_email);
        pass = findViewById(R.id.editText_pass);
        passCopy = findViewById(R.id.editText_pass_conf);
        register = findViewById(R.id.button_register);
        jsonObject = new JSONObject();

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class RegisterPost extends AsyncTask<URL,Void, Integer>{

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

        @Override
        protected void onPostExecute(Integer result){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            if(result!=null)
                intent.putExtra("registration_result",result.intValue());
            else intent.putExtra("registration_result",404);
            startActivity(intent);
        }

    }

}
