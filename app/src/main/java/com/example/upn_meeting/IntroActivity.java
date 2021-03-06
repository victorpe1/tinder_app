package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(() -> {
            Intent siguiente = new Intent( this, MenuInicioActivity.class);
            startActivity(siguiente);
            finish();
        }, 2000);

    }
}