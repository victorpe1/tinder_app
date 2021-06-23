package com.example.upn_meeting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AjustesActivity extends AppCompatActivity {

    private EditText mNombre, mTelef;
    private ProgressBar spinner;
   // private Button mConfirm;
    private ImageButton mVolver;
    private ImageView mPerfil_image;
    private EditText m_favs;
    private Spinner s_recibir, s_dar;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsuarioDatabase;

    private String user_id, nombre , telef, URLperfil_image, sexo, presupuesto, recibir, dar;
    private int recIndex, darIndex;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        mNombre = (EditText) findViewById(R.id.nombre);
        mTelef = (EditText) findViewById(R.id.Telefono);

        mPerfil_image = (ImageView) findViewById(R.id.perfil_img);
        mVolver = findViewById(R.id.volver_confg);

        m_favs = (EditText) findViewById(R.id.confg_pres);
        s_recibir = (Spinner) findViewById(R.id.spinner);
        s_dar = (Spinner) findViewById(R.id.spinner);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null && mAuth.getCurrentUser() != null)
            user_id = mAuth.getCurrentUser().getUid();
        else{
            finish();
        }

        mUsuarioDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user_id);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.services, android.R.layout.simple_spinner_item);adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s_recibir.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_give = ArrayAdapter.createFromResource( this, R.array.services, android.R.layout.simple_spinner_item);adapter_give.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s_dar.setAdapter(adapter_give);

        getUserInfo();

        mPerfil_image.setOnClickListener(v -> {
            if ( !checkPermission()){
                Toast.makeText(AjustesActivity.this, "Por favor permitir acceso!", Toast.LENGTH_SHORT ).show();
                requestPermission();
            }
            else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mVolver.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AjustesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        });

        Toolbar barra = findViewById(R.id.setting_toolbartag);
        setSupportActionBar(barra);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.id.main, menu);
        return true;
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[] {READ_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1 );
            }
            else {
                Toast.makeText(this, "Pro favor accede para continuar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //logout button pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if ( item.getItemId() == R.id.contactanos){
            new AlertDialog.Builder(AjustesActivity.this)
                    .setTitle("Contactanos")
                    .setMessage("Contactanoss: xd123h@gmail.com")
                    .setNegativeButton("Dismiss", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (item.getItemId() == R.id.logout){
            spinner.setVisibility(View.VISIBLE);
            mAuth.signOut();
            Toast.makeText(this, "Cerrado sesion con exito", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AjustesActivity.this, MenuInicioActivity.class);
            startActivity(intent);
            finish();
            spinner.setVisibility(View.GONE);
        }
        else if (item.getItemId()==R.id.eliminar_cuenta){
            new AlertDialog.Builder(AjustesActivity.this)
                    .setTitle("Estas seguro?")
                    .setMessage("Elimando la cuenta permanenete")
                    .setPositiveButton("Delete", (dialog, which) ->
                      mAuth.getCurrentUser().delete().addOnCompleteListener(task -> {
                        spinner.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()){
                            EliminarUsuario(user_id);
                            Toast.makeText(AjustesActivity.this, "Cuenta eliminada exitosamente!", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(AjustesActivity.this, MenuInicioActivity.class);
                            startActivity(intent);
                            finish();
                            spinner.setVisibility(View.GONE);
                            return;
                        }
                        else {
                            Toast.makeText(AjustesActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                            mAuth.signOut();
                            Intent intent = new Intent(AjustesActivity.this, MenuInicioActivity.class);
                            startActivity(intent);
                            finish();
                            spinner.setVisibility(View.VISIBLE);
                            return;
                        }
                    }))
                    .setNegativeButton("Dismiss", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return super.onOptionsItemSelected(item);

    }

    public void deleteMatch(String id, String chatId){
        DatabaseReference matchId_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(user_id).child("connections").child("matches").child(id);
        DatabaseReference userId_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(user_id).child("connections").child("matches").child(user_id);
        DatabaseReference yeps_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(user_id).child("connections").child("yeps").child(user_id);
        DatabaseReference yeps_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(user_id).child("connections").child("yeps").child(id);

        DatabaseReference matchId_chat_dbReference = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        matchId_chat_dbReference.removeValue();
        matchId_in_UserId_dbReference.removeValue();
        userId_in_matchId_dbReference.removeValue();
        yeps_in_matchId_dbReference.removeValue();
        yeps_in_UserId_dbReference.removeValue();

    }

    private void EliminarUsuario(String user_id) {
        DatabaseReference usuario = FirebaseDatabase.getInstance().getReference().child("Usuarios"). child(user_id);
        DatabaseReference matches_usuario = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(user_id).child("connections").child("matches");

        matches_usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for ( DataSnapshot match : dataSnapshot.getChildren()){
                        deleteMatch(match.getKey(),match.child("ChatId").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        matches_usuario.removeValue();
        usuario.removeValue();
    }

    public void bConfirmar(View view){
        Intent intent = new Intent(AjustesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private void getUserInfo(){
        mUsuarioDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("nombre") != null) {
                        nombre = map.get("nombre").toString();
                        mNombre.setText(nombre);
                    }
                    if (map.get("telef") != null) {
                        telef = map.get("telef").toString();
                        mTelef.setText(telef);
                    }
                    if (map.get("sexo") != null) {
                        sexo = map.get("sexo").toString();
                    }
                    if (map.get("presupuesto") != null) {
                        presupuesto = map.get("presupuesto").toString();
                    } else presupuesto = "0";

                    if (map.get("dar") != null) {
                        dar = map.get("dar").toString();
                    } else dar = "";

                    if (map.get("recibir") != null) {
                        recibir = map.get("recibir").toString();
                    } else recibir = "";

                    String[] services = getResources().getStringArray(R.array.services);
                    recIndex = darIndex = 0;
                    for (int i = 0; i < services.length; i++) {
                        if (recibir.equals(services[i]))
                            recIndex = 1;
                        if (dar.equals(services[i]))
                            darIndex = 1;
                    }

                    s_recibir.setSelection(recIndex);
                    s_dar.setSelection(darIndex);
                    m_favs.setText(presupuesto);

                    Glide.with(AjustesActivity.this).clear(mPerfil_image);
                    if(map.get("URLperfil_image") != null){
                        URLperfil_image = map.get("URLperfil_image").toString();
                        switch (URLperfil_image){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.profile).into(mPerfil_image);
                                break;
                            default:
                                Glide.with(getApplication()).load(URLperfil_image).into(mPerfil_image);
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
       });
    }

    private void guardarInfoUsuario(){
        nombre = mNombre.getText().toString();
        telef = mTelef.getText().toString();
        presupuesto = m_favs.getText().toString();
        dar = s_dar.getSelectedItem().toString();
        recibir = s_recibir.getSelectedItem().toString();

        Map info_usuario = new HashMap();
        info_usuario.put("nombre", nombre);
        info_usuario.put("telef", telef);
        info_usuario.put("dar", dar);
        info_usuario.put("presupuesto", presupuesto);
        info_usuario.put("recibir", recibir);

        mUsuarioDatabase.updateChildren(info_usuario);
        if(uri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Perfil_imagen").child(user_id);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
            UploadTask up = filepath.putBytes(data);
            up.addOnFailureListener(e -> finish());

            up.addOnSuccessListener(taskSnapshot -> {
                Task<Uri> ur = taskSnapshot.getStorage().getDownloadUrl();
                while (!ur.isComplete());
                Uri descargaUri = ur.getResult();
                Map info_usuari = new HashMap( );
                info_usuari.put("URLperfil_image", descargaUri.toString());
                mUsuarioDatabase.updateChildren(info_usuari);
                finish();
                return;
            });
        }
        else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageuri = data.getData();
            uri = imageuri;
            mPerfil_image.setImageURI(uri);
        }
    }
}