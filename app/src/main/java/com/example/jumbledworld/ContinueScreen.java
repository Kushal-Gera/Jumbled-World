package com.example.jumbledworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContinueScreen extends AppCompatActivity {
    private static final String TAG = "ContinueScreen";
    public static final String SHARED_PREF = "shared_pref";
    public static final String LEVEL_REACHED = "something";
    public static final String INTENT_LEVEL = "some";

    Button start, cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_screen);

        start = findViewById(R.id.start);
        cont = findViewById(R.id.cont);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContinueScreen.this, MainActivity.class));
                finish();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                retrieveData();
                SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                int level = preferences.getInt(LEVEL_REACHED, 0);

                Intent intent = new Intent(ContinueScreen.this, MainActivity.class);
                intent.putExtra(INTENT_LEVEL, level);

                startActivity(intent);
                finish();
            }
        });

    }




}