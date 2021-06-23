package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class DislikeActivity extends AppCompatActivity {

    private static final String TAG ="DislikeActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = DislikeActivity.this;
    private ImageView dislike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dislike);

        setTopNav();
        dislike = findViewById(R.id.dislike);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("url");

        switch (profileUrl){
            case "default":
                Glide.with(mContext).load(R.drawable.profile).into(dislike);
                break;
            default:
                Glide.with(mContext).load(profileUrl).into(dislike);
                break;
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(DislikeActivity.this, MainActivity.class);
            startActivity(i);
        }).start();
    }

    private void setTopNav() {
        BottomNavigationViewEx tvEx =findViewById(R.id.topNavViewBar);
        TopNavigation.setTopNav(tvEx);
        TopNavigation.enableNav(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void bLike(View view){

    }

}