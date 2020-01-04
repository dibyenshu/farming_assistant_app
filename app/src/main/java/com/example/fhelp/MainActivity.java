package com.example.fhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button button_register,button_login;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlString = "http://192.168.43.194:5000/";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("farming_assistant",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("url",urlString);
        editor.commit();

        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.hasExtra("registration_result")){
            int httpResult = intent.getExtras().getInt("registration_result");

            if(httpResult==404) Toast.makeText(this, "Can't Connect to server, check internet", Toast.LENGTH_LONG).show();
            else if (httpResult == HttpURLConnection.HTTP_OK) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error" + httpResult, Toast.LENGTH_LONG).show();
            }
        }

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
