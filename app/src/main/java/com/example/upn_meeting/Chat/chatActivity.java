package com.example.upn_meeting.Chat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.upn_meeting.MatchActivity;
import com.example.upn_meeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.load;

public class chatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendEditex;
    private ImageButton mback;

    private ImageButton mSendButton;
    private String notification;
    private String currentUserID, matchId, chatId;
    private String matchName, matchGive, matchNeed, matchBudget, matchProfile;
    private String lastMessage, lastTimeStamp;
    private String message, createByUser, isSeen, messageId, currentUserName;
    private Boolean currentUserBoolean;
    ValueEventListener seenListener;
    DatabaseReference mDatabaseUser, mDatabaseChat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    matchId = getIntent().getExtras().getString("matchId");
    matchName = getIntent().getExtras().getString("nombre");
    matchGive   = getIntent().getExtras().getString("dar");
    matchNeed = getIntent().getExtras().getString("recibo");
    matchBudget = getIntent().getExtras().getString("presupuesto");
    matchProfile = getIntent().getExtras().getString("perfil");
    currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
    mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

    getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);

    mChatLayoutManager = new  LinearLayoutManager(chatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
    mChatAdapter = new chatAdapt(getDataSetChat(), chatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

    mSendEditex = findViewById(R.id.message);
    mback = findViewById(R.id.back);

    mSendButton = findViewById(R.id.enviar);

    mSendButton.setOnClickListener(v -> seendMessage(""));


    mRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if(bottom < oldBottom){
                mRecyclerView.postDelayed(() -> mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount()-1), 100);
            }
        });
        mback.setOnClickListener(v -> {
            Intent i =new Intent(chatActivity.this, MatchActivity.class);
            startActivity(i);
            finish();
            return;
        });


    androidx.appcompat.widget.Toolbar barra2 = findViewById(R.id.chat_barra);
    setSupportActionBar(barra2);

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(currentUserID);
    Map onchat = new HashMap();
        onchat.put("onchat", matchId);
        reference.updateChildren(onchat);

    DatabaseReference current = FirebaseDatabase.getInstance().getReference("Usuarios")
            .child(matchId).child("connection").child("matches").child(currentUserID);
        Map lastSenn = new HashMap();
        lastSenn.put("ultimoVisto", "false");
        current.updateChildren(lastSenn);
}

    private List<chat> getDataSetChat() {
        return null;
    }

    @Override
    protected void onPause() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(currentUserID);
        Map onchat = new HashMap();
        onchat.put("onChat", "none");
        reference.updateChildren(onchat);
        super.onPause();
    }

    @Override
    protected void onStop() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(currentUserID);
        Map onchat = new HashMap();
        onchat.put("onChat", "None");
        reference.updateChildren(onchat);
        super.onStop();
    }

    private void seendMessage(final String text ) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(matchId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    if(dataSnapshot.child("onChat").exists()){
                        if(dataSnapshot.child("notificationKey").exists())notification =dataSnapshot.child("notificationKey").getValue().toString();
                    else
                        notification = " ";

                    if(!dataSnapshot.child("onChat").getValue().toString().equals(currentUserID)){
                            new SendNotification(text, "Nuevo mensaje de: " + currentUserName, notification,
                                    "activityToBeOpened", "MatchesActivity");
                        }
                    else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                                    .child(currentUserID).child("conections").child("matches").child(matchId);
                            Map seenInfo = new HashMap();
                            seenInfo.put("lastSend", "false");
                            reference.updateChildren(seenInfo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        TextView mMatchNameTextView = (TextView) findViewById(R.id.chat_barra);
        mMatchNameTextView.setText(matchName);
        return true;
    }

    public void showProfile(View v ){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.item_perfil, null);

        TextView name = (TextView) popupView.findViewById(R.id.name);
        ImageView image = (ImageView) popupView.findViewById(R.id.image);
        TextView budget = (TextView) popupView.findViewById(R.id.presu);
        ImageView mNeddImage= (ImageView) popupView.findViewById(R.id.necesitoImage);
        ImageView mGiveImage = (ImageView) popupView.findViewById(R.id.giveImage);

        name.setText(matchName);
        budget.setText(matchBudget);

        //need Image
        if (matchNeed.equals("Netflix"))
            mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        else if (matchNeed.equals("Hulu"))
            mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        else if (matchNeed.equals("Vudu"))
        mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
                else if (matchNeed.equals("HBO Now"))
            mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        else if (matchNeed.equals("Youtube Originals"))
            mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        else
            mNeddImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));


        //Give Image
        if (matchNeed.equals("Netflix"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        else if (matchNeed.equals("Amazon Prime"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.amazon));
        else if (matchNeed.equals("Hulu"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        else if (matchNeed.equals("Vudu"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
        else if (matchNeed.equals("HBO Now"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        else if (matchNeed.equals("Youtube Originals"))
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        else
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));

        switch (matchProfile){
            case "default" :
                Glide.with(popupView.getContext()).load(R.drawable.profile).into(image);
                break;
            default:
                Glide.with(this).clear(image);
                Glide.with(popupView.getContext()).load(matchProfile).into(image);
                break;
        }
        int  width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable  = true;
        final PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);

        hideSofKeyBoard();

        popupWindow.showAtLocation(v, Gravity.CENTER, 0,0);
        popupView.setOnTouchListener((v1, event) -> {
            popupWindow.dismiss();
            return false;
        });
    }


    private void hideSofKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId()==R.id.unMatch){
            new AlertDialog.Builder(chatActivity.this)
                    .setTitle("Unmatch" )
                    .setMessage("Estas seguro que quieres quitar el Match?")
                    .setPositiveButton("Unmatch", (dialog, which) -> {
                        deleteMatch(matchId);
                        Intent intent = new Intent(chatActivity.this,MatchActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(chatActivity.this,"Unmatch successful", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Dismiss", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (item.getItemId()==R.id.perfil_img){
            showProfile(findViewById(R.id.content));
        }
        return  super.onOptionsItemSelected(item);
    }

    private void deleteMatch(String matchId) {
        DatabaseReference matchId_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("matches").child(matchId);
        DatabaseReference userId_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId).child("connections").child("matches").child(currentUserID);
        DatabaseReference yeps_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchId).child("connections").child("yeps").child(currentUserID);
        DatabaseReference yeps_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("yeps").child(matchId);

        DatabaseReference matchId_chat_dbReference = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        matchId_chat_dbReference.removeValue();
        matchId_in_UserId_dbReference.removeValue();
        userId_in_matchId_dbReference.removeValue();
        yeps_in_matchId_dbReference.removeValue();
        yeps_in_UserId_dbReference.removeValue();

    }
    private void seenMessage(String sendMessageText){
         String sendMessageText = mSendEditex.getText().toString();
        long now =  System.currentTimeMillis();
        String timeStamp = Long.toString(now);

        if (!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createByUser", currentUserID);
            newMessage.put("Text", sendMessageText);
            newMessage.put("timeStamp", timeStamp);
            newMessage.put("seen", "false ");

            DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Usuarios").child(currentUserID);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if(snapshot.child("nombre").exists())
                            currentUserName = snapshot.child("nombre").getValue().toString();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            lastMessage = sendMessageText;
            lastTimeStamp  = timeStamp;
            updateLastMessage();
            seenMessage(sendMessageText);
            newMessageDb.setValue(newMessage);
        }
        mSendEditex.setText(null);
    }

    private void updateLastMessage() {

        DatabaseReference currUserDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(currentUserID)
                .child("conections").child("matches").child(matchId);
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(matchId)
                .child("conections").child("matches").child(currentUserID);

        Map lastMessageMap = new HashMap();
        lastMessageMap.put("lastMessage", lastMessage);
        Map lastTimestampMap = new HashMap();
        lastMessageMap.put("lastTimeStamp", lastTimeStamp);

        Map lastSeen = new HashMap();
        lastSeen.put("lastSeen", "true");
        currUserDb.updateChildren(lastSeen);
        currUserDb.updateChildren(lastMessageMap);
        currUserDb.updateChildren(lastTimestampMap);

        matchDb.updateChildren(lastMessageMap);
        matchDb.updateChildren(lastTimestampMap);

    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessage();
                }
            }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
            });
        }

private void getChatMessage() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
@Override
public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (dataSnapshot.exists()) {
        messageId = null;
        message = null;
        createByUser = null;
        isSeen = null;
        if (dataSnapshot.child("text").getValue() != null) {
        message = dataSnapshot.child("Text").getValue().toString();
        }
        if (dataSnapshot.child("createByUser").getValue() != null) {
        createByUser = dataSnapshot.child("createByUser").getValue().toString();
        }
        if (dataSnapshot.child("seen").getValue() != null) {
        isSeen = dataSnapshot.child("seen").getValue().toString();
        } else isSeen = "true";

        messageId = dataSnapshot.getKey().toString();
        if (messageId != null && createByUser != null) {
        currentUserBoolean = false;
        if (createByUser.equals(currentUserID)) {
        currentUserBoolean = true;
        }
        chat newMessage = null;
        if (isSeen.equals("false")) {
        if (!currentUserBoolean) {
        isSeen = "true";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chat")
        .child(chatId).child(messageId);
        Map seenInfo = new HashMap();
        seenInfo.put("seen", "true");
        reference.updateChildren(seenInfo);

        newMessage = new chat(message, currentUserBoolean, true);
        } else {
        newMessage = new chat(message, currentUserBoolean, false);
        }
        } else {
        newMessage = new chat(message, currentUserBoolean, true);

        DatabaseReference userInChat = FirebaseDatabase.getInstance().getReference().child("chat").child(matchId);

        resultsChat.add(newMessage);
        mChatAdapter.notifyDataSetChanged();
        if (mRecyclerView.getAdapter() != null && resultsChat.size() > 0)
        mRecyclerView.smoothScrollToPosition(resultsChat.size() - 1);
        else
        Toast.makeText(chatActivity.this, "Chat Empty", Toast.LENGTH_SHORT).show();

        }

        }
        }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

                }
                });

                }
        private ArrayList<chat> resultsChat = ArrayList<>();

        private List<chat> getDataSetChat(){
                return resultsChat;
                }
        @Override
        public void onBackPressed(){
                super.onBackPressed();
                finish();
                return;
                }


        }
