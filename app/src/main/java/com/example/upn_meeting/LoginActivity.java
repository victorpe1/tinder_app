package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ProgressBar spinner;
    private EditText mEmail, mPass;

    private TextView mRecuperarContra;
    private boolean loginClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginClicked = false;
        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password);
        mRecuperarContra = (TextView) findViewById(R.id.recuperar_pass);

        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.isEmailVerified() && !loginClicked) {
                spinner.setVisibility(View.VISIBLE);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                spinner.setVisibility(View.GONE);
                return;
            }
        };


    }

    public void bInicioSesion(View view){

        loginClicked = true;
        spinner.setVisibility(View.VISIBLE);

        final String email = mEmail.getText().toString();
        final String contrase = mPass.getText().toString();

        if(isStringNull(email)  ||  isStringNull(contrase)){
            Toast.makeText(this, "Tu debes escribir en los campos disponibles", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email, contrase).addOnCompleteListener(this,
                    task -> {
                        if(!task.isSuccessful()){
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Intent i = new Intent(this, MainActivity.class);
                                startActivity(i);
                                finish();
                                return;
                            }else{
                                Toast.makeText(this, "Por favor verifica tu correo electronico", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        spinner.setVisibility(View.GONE);

    }
    public void bRecuperarContra(View view){
        spinner.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, RecuperarContraActivity.class);
        startActivity(i);
        finish();
        return;
    }



    private boolean isStringNull(String email) {
        return email.equals("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MenuInicioActivity.class);
        startActivity(i);
        finish();
    }
}















