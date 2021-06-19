package com.example.upn_meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private EditText mEmail, mPass, mNombre, mAux;
    private CheckBox checkBox;

    private RadioGroup mRg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "RegistroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        TextView existe_correo = (TextView) findViewById(R.id.existe_correo);
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                spinner.setVisibility(View.VISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {

                    Intent i = new Intent(RegistroActivity.this, MainActivity2.class);
                    startActivity(i);
                    finish();
                    spinner.setVisibility(View.GONE);
                    return;
                }
                spinner.setVisibility(View.GONE);
            }
        };

        existe_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, MainActivity2.class);
                startActivity(i);
                finish();
            }
        });

        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password);
        mNombre = (EditText) findViewById(R.id.nombre);

        checkBox = (CheckBox) findViewById(R.id.checkbox1);
        TextView textView = (TextView) findViewById(R.id.texView2);

        checkBox.setText("");
        textView.setText(Html.fromHtml("Ya he leido y aceptado los "+ "<a href='https://www.blogger.com/u/1/blog/post/preview/4503034348739834759/4359031811917800234'>terminos y condiciones</a> "));
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void bRegistrar(View view){
        spinner.setVisibility(View.VISIBLE);

        final String email = mEmail.getText().toString();
        final String contrase = mPass.getText().toString();
        final String nombre = mNombre.getText().toString();
        final Boolean tnc = checkBox.isChecked();

        if(checkInputs(email, nombre, contrase, tnc)){
            mAuth.createUserWithEmailAndPassword(email, contrase).addOnCompleteListener(this,
                    task -> {
                        if(!task.isSuccessful()){
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(
                                    task1 -> {
                                        if(task1.isSuccessful()) {
                                            Toast.makeText(this, "Usuario registrado exitosamente " + " Por favor revise su correo para la verificacion", Toast.LENGTH_SHORT).show();
                                            String usuario_id = mAuth.getCurrentUser().getUid();
                                            DatabaseReference usuario_act_id = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuario_id);

                                            Map info_usuario = new HashMap<>();
                                            info_usuario.put("nombre", nombre);
                                            info_usuario.put("perfil_image", "default");

                                            usuario_act_id.updateChildren(info_usuario);

                                            mEmail.setText("");
                                            mNombre.setText("");
                                            mPass.setText("");
                                            Intent i = new Intent(this, MenuInicioActivity.class);
                                            startActivity(i);
                                            finish();
                                            return;
                                        }else{
                                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    });
                        }
                    });
            

        }

    }

    private boolean checkInputs(String email, String nombre, String contrase, Boolean tnc) {
        if(email.equals("") || nombre.equals("") || contrase.equals("")){
            Toast.makeText(this, "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.matches(emailPattern)){
            Toast.makeText(this, "Correo invalido, ingresa un correo valido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!tnc){
            Toast.makeText(this, "Aceptar terminos y condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
















