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

public class LikeActivity extends AppCompatActivity {

    private static final String TAG ="LikeActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = LikeActivity.this;
    private ImageView like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        setTopNav();
        like = findViewById(R.id.like);

        Intent intent = getIntent();
        String perfilUrl = intent.getStringExtra("url");

        switch (perfilUrl){
            case "default":
                Glide.with(mContext).load(R.drawable.profile).into(like);
                break;
            default:
                Glide.with(mContext).load(perfilUrl).into(like);
                break;
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent mainIntent = new Intent(LikeActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }).start();
    }

    private void setTopNav() {
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigation.setTopNav(tvEx);
        TopNavigation.enableNav(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void bLike(View view){

    }
}