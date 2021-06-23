package com.example.upn_meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContraActivity extends AppCompatActivity {

    private EditText mEmail;
    private FirebaseAuth mAuth;
    private int flag;
    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);

        mAuth = FirebaseAuth.getInstance();
        flag = 0;
        mEmail = (EditText) findViewById(R.id.recuperar_pass);



    }

    public void bRecuperarContra(View view){

        email = mEmail.getText().toString();

        if (email.equals("")) {
            Toast.makeText(RecuperarContraActivity.this, "Digite un correo electronico", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!email.matches(emailpattern)) {
            Toast.makeText(RecuperarContraActivity.this, "Incorrecto la dirección de correo electrónico, ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;

        }

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            flag = 1;
            mAuth.sendPasswordResetEmail(mEmail.getText().toString()).addOnCompleteListener(task1 -> {
            if (task.isSuccessful()) {
                Toast.makeText(RecuperarContraActivity.this, "El correo de recuperacion de contraseña ha sido enviado a tu correo", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(RecuperarContraActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
                });
        });

        if(flag == 0)Toast.makeText(RecuperarContraActivity.this, "Dirección de correo electrónico no encontrado", Toast.LENGTH_SHORT).show();

        }

    @Override
    public  void onBackPressed(){
        Intent BtnClick = new Intent(RecuperarContraActivity.this,LoginActivity.class);
        startActivity(BtnClick);
        super.onBackPressed();
        finish();
        return;

    }

    }

