package com.example.jumbledworld;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    ArrayList<View> arrayList;
    ArrayList<Button> buttonList;
    ArrayList<String> WORDS;

    Button op1, op2, op3, op4 ,op5 ,op6, op7, op8;
    TextView ans1, ans2, ans3, ans4;
    TextView back, reset, hint, level;

    private static final String default_ques = "?";
    public int l = 0;//  it is level
    public int size;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WORDS = new ArrayList<>();
        WORDS.add("time");  WORDS.add("come");  WORDS.add("when");  WORDS.add("duke");
        WORDS.add("mute");  WORDS.add("kite");  WORDS.add("tyre");  WORDS.add("rain");
        size = WORDS.size();

        //these are the initialisations :(
        op1 = findViewById(R.id.op1);   op2 = findViewById(R.id.op2);   op3 = findViewById(R.id.op3);   op4 = findViewById(R.id.op4);
        op5 = findViewById(R.id.op5);   op6 = findViewById(R.id.op6);   op7 = findViewById(R.id.op7);   op8 = findViewById(R.id.op8);

        ans1 = findViewById(R.id.ans1);   ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);   ans4 = findViewById(R.id.ans4);

        dialog = new Dialog(this);
        arrayList = new ArrayList<>();
        buttonList = new ArrayList<>();

        setAllOptions();
        ///////////////////////////////////////////////////////////////

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
                Toast.makeText(MainActivity.this, "No Hint Available", Toast.LENGTH_SHORT).show();
            }
        });

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setText(default_ques);   ans2.setText(default_ques);   ans3.setText(default_ques);   ans4.setText(default_ques);
                show_n_remove();
            }
        });

        level = findViewById(R.id.level);
        level.setText("Level " + (l+1) );

        initialise_with(WORDS.get(l), buttonList);


        
    }

    private void initialise_with(final String word, final ArrayList<Button> btns){

        if (word == null){
            for (int i = 0; i < 8; i++)
                btns.get(i).setText("");
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
                setOutputChar(op1.getText().toString());
                hide_n_add(v);
            }
        });

        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op2.getText().toString());
                hide_n_add(v);
            }
        });

        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op3.getText().toString());
                hide_n_add(v);
            }
        });

        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op4.getText().toString());
                hide_n_add(v);
            }
        });

        op5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op5.getText().toString());
                hide_n_add(v);
            }
        });

        op6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op6.getText().toString());
                hide_n_add(v);
            }
        });

        op7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op7.getText().toString());
                hide_n_add(v);
            }
        });

        op8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOutputChar(op8.getText().toString());
                hide_n_add(v);
            }
        });

    }

    private void setOutputChar(final String key){

        if (ans1.getText().toString().equals(default_ques) && ans2.getText().toString().equals(default_ques) && ans3.getText().toString().equals(default_ques) && ans4.getText().toString().equals(default_ques)){
            //enter in 1
            ans1.setText(key);

        }else if (ans2.getText().toString().equals(default_ques) && ans3.getText().toString().equals(default_ques) && ans4.getText().toString().equals(default_ques)){
            //enter in 2
            ans2.setText(key);

        }else if (ans3.getText().toString().equals(default_ques) && ans4.getText().toString().equals(default_ques) ){
            //enter in 3
            ans3.setText(key);

        }else{
            //enter in 4 check for write answer
            ans4.setText(key);
            String userAns = ans1.getText().toString().concat(ans2.getText().toString()).concat(ans3.getText().toString()).concat(key);
            checkAnswer(userAns);
        }

    }

    private void checkAnswer(final String userAns){
//        later increase level as well

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
                            //replay starts
                            dialog.dismiss();
                        }
                    });

                    ImageView next1 = dialog.findViewById(R.id.next1);
                    next1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                  increase level currently
                            dialog.dismiss();
                            if (l+1 < size){
                                l++;
                                initialise_with(WORDS.get(l), buttonList);
                            }
                            else {
                                Snackbar.make(ans1,"Game Ends Here.\nNew Levels Will Come In The Next Update", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Reset Game", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                l = 0;
                                                level.setText("Level " + (l+1));
                                                initialise_with(WORDS.get(l), buttonList);
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
                ans1.setText(default_ques);   ans2.setText(default_ques);   ans3.setText(default_ques);   ans4.setText(default_ques);
                show_n_remove();
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






}
