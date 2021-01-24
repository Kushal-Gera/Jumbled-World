package com.application.jumbledworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ContinueScreen extends AppCompatActivity {
    private static final String TAG = "ContinueScreen";
    public static final String SHARED_PREF = "shared_pref";
    public static final String LEVEL_REACHED = "something";
    public static final String INTENT_LEVEL = "some";
    InterstitialAd interstitialAd;

    Button new_game, cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_screen);
        Log.d(TAG, "onClick: Screen continue ");

        //extend activity to status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        new_game = findViewById(R.id.new_game);
        cont = findViewById(R.id.cont);

        interstitialAd = new InterstitialAd(ContinueScreen.this);
        interstitialAd.setAdUnitId("ca-app-pub-5073642246912223/1171603950");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                int level = preferences.getInt(LEVEL_REACHED, 0);

                Intent intent = new Intent(ContinueScreen.this, MainActivity.class);
                intent.putExtra(INTENT_LEVEL, level);
                startActivity(intent);
                finish();
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: in the new_game btn");
                getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit().putBoolean("trial", false).apply();
                startActivity(new Intent(ContinueScreen.this, MainActivity.class));
            }
        });

        new_game.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit().putBoolean("trial", true).apply();
                startActivity(new Intent(ContinueScreen.this, MainActivity.class));
                return true;
            }
        });

        //just added handler for the new thread and hence smoother animations!!!
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: in the cont btn");

                if (interstitialAd.isLoaded())
                    interstitialAd.show();
                else {
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                    int level = preferences.getInt(LEVEL_REACHED, 0);

                    Intent intent = new Intent(ContinueScreen.this, MainActivity.class);
                    intent.putExtra(INTENT_LEVEL, level);
                    startActivity(intent);
                    finish();

                }
            }


        });


    }

}
