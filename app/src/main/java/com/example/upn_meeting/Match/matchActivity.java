package com.example.upn_meeting.Match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;

import com.example.upn_meeting.MainActivity;
import com.example.upn_meeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class matchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesManager;
    private ImageButton mBack;
    private DatabaseReference actual;
    private ValueEventListener escuchar;
    private HashMap<String, Integer> mlist = new HashMap<>();
    private String actual_UserId, mUltimoEstdo_conectado, mUltimoMensaje, ultimoVisto;
    DatabaseReference mCIdInsideMatchConnections, mCheckUltimoVisto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        mBack = findViewById(R.id.matchesBack);
        actual_UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesManager = new LinearLayoutManager(matchActivity.this);
        mRecyclerView.setLayoutManager(mMatchesManager);
        mMatchesAdapter= new matchAdapt(getDatSetMatches(), matchActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        mBack.setOnClickListener(v -> {
            Intent intent = new Intent(matchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        });
        getUserMatchId();
        mUltimoMensaje = mUltimoEstdo_conectado = ultimoVisto = "";

    }
    @Override
    protected void onPause(){
        super.onPause();

    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    private void getLastMessageInfo(DatabaseReference userDb) {
        mCIdInsideMatchConnections = userDb.child("connections").child("matches").child(actual_UserId);

        mCIdInsideMatchConnections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("ultimoMensaje").getValue()!= null && dataSnapshot.child("ultimoEstado")
                            .getValue() != null && dataSnapshot.child("ultimoVisto").getValue() != null){
                        mUltimoMensaje = dataSnapshot.child("ultimoMensaje").getValue().toString();
                        mUltimoEstdo_conectado= dataSnapshot.child("ultimoEstado").getValue().toString();
                        ultimoVisto = dataSnapshot.child("ultimoVisto").getValue().toString();
                    }
                    else{
                        mUltimoMensaje = "Start Chatting one";
                        mUltimoEstdo_conectado = "";
                        ultimoVisto = "true";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError  databaseError) {

            }
        });
    }

    private void getUserMatchId() {
        Query sortedMatchesByLastTimeStamp = FirebaseDatabase.getInstance().getReference()
                .child("Usuarios").child(actual_UserId).child("connections").child("matches")
                .orderByChild("ultimoEstado");

        sortedMatchesByLastTimeStamp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot  match :dataSnapshot.getChildren()){
                        FetchMatchInformation(match.getKey(), match.child("ChatId").toString());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(final String key, final String chatId) {
        DatabaseReference userDb=FirebaseDatabase.getInstance().getReference().child("user").child(key);
        getLastMessageInfo(userDb);

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String nombre ="";
                    String imagenURL = "";
                    String recibo = "";
                    String dar="";
                    String presupuest = "";
                    String ultimoMensajee = "";
                    String ultimoEstado = "";

                    if(dataSnapshot.child("nombre").getValue()!= null){
                        nombre = dataSnapshot.child("nombre").getValue().toString();
                    }
                    if(dataSnapshot.child("perfil").getValue() != null){
                        imagenURL = dataSnapshot.child("perfil").getValue().toString();

                    }
                    if(dataSnapshot.child("recibo").getValue() !=null) {
                        recibo = dataSnapshot.child("recibo").getValue().toString() ;

                    }
                    if(dataSnapshot.child("dar").getValue() !=null) {
                        dar = dataSnapshot.child("dar").getValue().toString() ;

                    }
                    if (dataSnapshot.child("presupuesto").getValue() !=null) {
                        presupuest = dataSnapshot.child("presupuesto").getValue().toString();
                    }

                    String millisec = mUltimoMensaje;
                    Long now;
                    try {
                        now = Long.parseLong(millisec);
                        ultimoMensajee = convertMilliToRelative(now);
                        String[]arrOfStr = ultimoMensajee.split(",");
                        mUltimoMensaje = arrOfStr[0];
                    }catch (Exception e){}

                    match obj = new match(userId,nombre, imagenURL, recibo, dar, presupuest,
                            mUltimoMensaje,mUltimoEstdo_conectado,chatId,ultimoVisto);
                    if(mlist.containsKey(chatId)){
                        int key = mlist.get(chatId);

                        resultsMatches.set(resultsMatches.size() - key , obj);


                    }
                    else{
                        resultsMatches.add(0,obj);
                        mlist.put(chatId,resultsMatches.size());
                    }
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {

            }
        });

    }

    private String convertMilliToRelative(Long now) {
        String time = DateUtils.getRelativeDateTimeString(this, now,DateUtils.SECOND_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,DateUtils.FORMAT_ABBREV_ALL).toString();
        return time;
    }

    private ArrayList<match> resultsMatches = new ArrayList<>();
    private List<match> getDatSetMatches() {
        return resultsMatches;

    }
    @Override
    public void  onBackPressed(){
        super.onBackPressed();
        finish();
        return;
    }


}
