package com.example.jumbledworld;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SHARED_PREF = "shared_pref";
    public static final String LEVEL_REACHED = "something";
    public static final String INTENT_LEVEL = "some";

    ArrayList<View> arrayList;
    ArrayList<Button> buttonList;
    ArrayList<TextView> tvList;
    ArrayList<String> WORDS;

    private Button op1, op2, op3, op4 ,op5 ,op6, op7, op8;
    private TextView ans1, ans2, ans3, ans4;
    TextView back, reset, hint, level;
    ImageView rocket;

    public int click = 1;
    public int pointer = 0;
    public int allowedHint = 3;
    private static final String default_ques = "?";
    public int l = 0;      //  it is level
    public int size;
    public boolean hasEnd = false;
    Dialog dialog;

    int hint1 = 10;         //just an aribitiary
    int hint2 = 10;         //value to initialise it



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        l = getIntent().getIntExtra(INTENT_LEVEL, 0);
        Toast.makeText(this, "Level " + (l+1), Toast.LENGTH_SHORT).show();

        WORDS = new ArrayList<>();
        WORDS.add("time");  WORDS.add("come");  WORDS.add("mute");  WORDS.add("kite");
        WORDS.add("tyre");  WORDS.add("rain");  WORDS.add("when");  WORDS.add("duke");
        size = WORDS.size();

        //these are the initialisations :(
        op1 = findViewById(R.id.op1);   op2 = findViewById(R.id.op2);   op3 = findViewById(R.id.op3);   op4 = findViewById(R.id.op4);
        op5 = findViewById(R.id.op5);   op6 = findViewById(R.id.op6);   op7 = findViewById(R.id.op7);   op8 = findViewById(R.id.op8);

        ans1 = findViewById(R.id.ans1);   ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);   ans4 = findViewById(R.id.ans4);

        dialog = new Dialog(this);
        arrayList = new ArrayList<>();
        buttonList = new ArrayList<>();
        tvList = new ArrayList<>();
        tvList.add(ans1);   tvList.add(ans2);   tvList.add(ans3);   tvList.add(ans4);

        setAllOptions();
        ///////////////////////////////////////////////////////////////

        rocket = findViewById(R.id.rocket);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hint = findViewById(R.id.hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit here
                if (click < allowedHint && hint1 == 10){
                    click++;
                    Random r = new Random();
                    hint1 = r.nextInt(4);
                    tvList.get(hint1).setText(String.valueOf(WORDS.get(l).charAt(hint1)).toUpperCase() );
                    tvList.get(hint1).setTextColor(getResources().getColor(R.color.colorGrey) );
                }
                else if (click < allowedHint){
                    click++;
                    Random rr = new Random();
                    hint2 = rr.nextInt(4);
                    while (hint1 == hint2){
                        Log.d(TAG, "onClick: values earlier " + hint1 + " and " + hint2);
                        hint2 = rr.nextInt(4);
                        Log.d(TAG, "onClick: values " + hint1 + " and " + hint2);
                    }
                    tvList.get(hint2).setText(String.valueOf(WORDS.get(l).charAt(hint2)).toUpperCase() );
                    tvList.get(hint2).setTextColor(getResources().getColor(R.color.colorGrey) );

                    hint1 = 10;
                    hint2 = 10;

                }
                else {
                    hint.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "No More Hints in This Level", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        level = findViewById(R.id.level);
        level.setText("Level " + (l+1) );

        initialise_with(WORDS.get(l), buttonList);



        
    }

    private void initialise_with(final String word, final ArrayList<Button> btns){

        if (word == null){
            for (int i = 0; i < 8; i++){
                btns.get(i).setText("");
                vanish();
                hasEnd = true;
            }
            return;
        }
        char[] chars = generator(word).toCharArray();

        for (int i = 0; i < chars.length; i++)
            btns.get(i).setText( String.valueOf(chars[i]) );

    }

    private void setAllOptions(){

        buttonList.add(op1);        buttonList.add(op2);        buttonList.add(op3);        buttonList.add(op4);
        buttonList.add(op5);        buttonList.add(op6);        buttonList.add(op7);        buttonList.add(op8);

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op1.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op2.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op3.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op4.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op5.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op6.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op7.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

        op8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op8.getText().toString(), pointer);
                hide_n_add(v);
                pointer++;
            }
        });

    }

    private void setOutputChar(final String key, int pointer){

        if (pointer > 3) return;

        tvList.get(pointer).setText(key);
        tvList.get(pointer).setTextColor(getResources().getColor(R.color.colorBlack) );

        if (pointer == 3){
            String userAns = ans1.getText().toString().concat(ans2.getText().toString()).concat(ans3.getText().toString()).concat(key);
            checkAnswer(userAns);
        }

    }

    private void checkAnswer(final String userAns){

        if (hasEnd) return;

        if (userAns.equalsIgnoreCase(WORDS.get(l)) ){
//            PASS
            Handler h1 = new Handler();
            h1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    dialog.setContentView(R.layout.pass_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    ImageView close1 = dialog.findViewById(R.id.close1);
                    close1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    ImageView replay1 = dialog.findViewById(R.id.replay1);
                    replay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //replay starts and jumble up
                            pointer = 0;
                            initialise_with(WORDS.get(l), buttonList);
                            dialog.dismiss();
                        }
                    });

                    ImageView next1 = dialog.findViewById(R.id.next1);
                    next1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                  increase level currently
                            dialog.dismiss();
                            saveData();
                            if (l+1 < size){
                                pointer = 0;
                                l++;
                                click = 1;
                                hint.setVisibility(View.VISIBLE);
                                initialise_with(WORDS.get(l), buttonList);
                            }
                            else {
                                Snackbar.make(ans1,"Game Ends Here..", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Reset Game", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetSavedLevel();
                                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                        .show();
                                initialise_with(null, buttonList);
                                return;
                            }
                            level.setText("Level " + (l+1) );
                        }
                    });

                    clearAns();

                }
            },300 );

        //******************************************************************************************
        }else {
//            FAIL
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {

                    dialog.setContentView(R.layout.fail_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    ImageView close = dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    ImageView replay = dialog.findViewById(R.id.replay);
                    replay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //replay starts
                            pointer = 0;
                            initialise_with(WORDS.get(l), buttonList);
                            dialog.dismiss();
                        }
                    });

                    clearAns();

                }
            }, 300);
        }


    }

    private void clearAns(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 1000);

    }

    private void hide_n_add(View v){

        v.setVisibility(View.INVISIBLE);
        arrayList.add(v);

    }

    private void show_n_remove(){

        for (View v : arrayList)
            v.setVisibility(View.VISIBLE);

        arrayList.clear();
    }

    private String generator(final String words){

        String new_words = words;
        for (int i = 0; i < 4; i++) {

            int random = new Random().nextInt(26) + 65;
            String ns = String.valueOf( (char) random );

            new_words = new_words.concat(ns);

        }
        return jumbleUp(new_words).toUpperCase();

    }

    private String jumbleUp(final String words){
        char[] chArray = words.toCharArray();

        for (int i = 0; i < 20; i++) {
            int random = new Random().nextInt(8);
            char temp = chArray[random];

            int new_random = new Random().nextInt(8);
            char temp2 = chArray[new_random];

            chArray[random] = temp2;
            chArray[new_random] = temp;
        }
        return String.valueOf(chArray).toUpperCase();

    }

    private void vanish(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
            }
        };

        ans1.setOnClickListener(listener);  ans2.setOnClickListener(listener);
        ans3.setOnClickListener(listener);  ans4.setOnClickListener(listener);

        hint.setOnClickListener(listener);  reset.setOnClickListener(listener);
        level.setOnClickListener(listener); back.setOnClickListener(listener);

        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rocket.animate().translationY(-1000).setDuration(800);
            }
        });



    }

    private void reset(){
        pointer = 0;
        for (TextView tv : tvList){
            tv.setText(default_ques);
            tv.setTextColor(getResources().getColor(R.color.colorBlack));
            show_n_remove();
        }

    }

    private void saveData(){

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(LEVEL_REACHED, (l+1) );
        editor.apply();

    }

    private void resetSavedLevel(){

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(LEVEL_REACHED , 0);
        editor.apply();

    }







}
