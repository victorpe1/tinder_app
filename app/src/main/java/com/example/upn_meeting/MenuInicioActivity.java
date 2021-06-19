package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuInicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
    }

    public void bLogin(View view){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void bRegistro(View view){
        Intent registro = new Intent(this, RegistroActivity.class);
        startActivity(registro);
    }
}