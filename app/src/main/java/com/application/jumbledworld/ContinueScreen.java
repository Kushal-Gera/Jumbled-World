package com.application.jumbledworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class ContinueScreen extends AppCompatActivity {
    private static final String TAG = "ContinueScreen";
    public static final String SHARED_PREF = "shared_pref";
    public static final String LEVEL_REACHED = "something";
    public static final String INTENT_LEVEL = "some";

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

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {

                new_game.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: in the new_game btn");
                        startActivity(new Intent(ContinueScreen.this, MainActivity.class));
                    }
                });

                //just added handler for the new thread and hence smoother animations!!!
                cont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: in the cont btn");

                        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                        int level = preferences.getInt(LEVEL_REACHED, 0);

                        Intent intent = new Intent(ContinueScreen.this, MainActivity.class);
                        intent.putExtra(INTENT_LEVEL, level);
                        startActivity(intent);
                        finish();
                    }

                });

            }
        });

    }

}
