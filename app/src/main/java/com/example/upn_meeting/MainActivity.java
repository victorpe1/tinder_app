package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bDislike(View view){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void bLike(View view){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }


}