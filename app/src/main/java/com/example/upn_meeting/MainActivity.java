package com.example.upn_meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ru.dimorinny.showcasecard.ShowCaseView;
import ru.dimorinny.showcasecard.position.ShowCasePosition;
import ru.dimorinny.showcasecard.position.ViewPosition;
import ru.dimorinny.showcasecard.radius.Radius;

public class MainActivity extends AppCompatActivity {

    boolean primerMatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTopNav();


    }

    private void setTopNav() {
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigation.setTopNav(tvEx);
        TopNavigation.enableNav(MainActivity.this,tvEx);
        Menu menu = tvEx.getMenu() ;
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        View view_perfil = findViewById(R.id.ic_perfil);
        //View matched_view = findViewById(R.id.ic_matched);

        if(primerMatch){
            showTool_perfil(new ViewPosition(view_perfil));
        }

        SharedPreferences newPref = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = newPref.edit();
        editor.putBoolean("primerMatch", false);
        editor.apply();
    }

    private void showTool_perfil(ShowCasePosition position) {
        new ShowCaseView.Builder(MainActivity.this)
                .withTypedPosition(position)
                .withTypedRadius(new Radius(186f))
                .withContent("La primera vez que cargue su foto de perfil y haga clic en 'Confirmar', de lo contrario, su aplicación no funcionara bien")
                .build()
                .show(MainActivity.this);

    }

    public void bDislike(View view){
        Intent login = new Intent(this, DislikeActivity.class);
        startActivity(login);
    }

    public void bLike(View view){
        Intent login = new Intent(this, LikeActivity.class);
        startActivity(login);
    }


}