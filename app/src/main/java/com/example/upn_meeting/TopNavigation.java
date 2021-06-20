package com.example.upn_meeting;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class TopNavigation {

    private static final String TAG = "TopNavigation";

    public static void setTopNav(BottomNavigationViewEx tv){
        Log.d(TAG, "setTopNav: Configure la barra de navegacion");
    }

    public static void enaNav(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.ic_perfil:
                    Intent i = new Intent(context, AjustesActivity.class);
                    context.startActivity(i);
                    break;
                case R.id.ic_matched:
                    Intent i1 = new Intent(context, MatchActivity.class);
                    context.startActivity(i1);
                    break;
            }
            return false;
        });
    }


}
