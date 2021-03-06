package com.application.jumbledworld;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private static final String TAG = "MainActivity";
    public static final String SHARED_PREF = "shared_pref";
    public static final String LEVEL_REACHED = "something";
    public static final String INTENT_LEVEL = "some";

    ArrayList<View> arrayList;
    ArrayList<Button> buttonList;
    ArrayList<TextView> tvList;
    ArrayList<String> WORDS;

    private Button op1, op2, op3, op4, op5, op6, op7, op8;
    private TextView ans1, ans2, ans3, ans4;
    Vibrator vib;
    TextView reset, hint, level, hintLeft, easter;
    ImageView rocket;

    public int click = 1;
    public int temp1 = 11;
    public int pointer = 0;
    public int allowedHint = 3;
    public static final int first_level_change = 4;     // reduce hint by one i.e hint =- 1
    public static final int second_level_change = 12;   // reduce hint by one i.e hint =- 1
    private static final String default_ques = "?";
    public int l = 0;      //  it is (level-1)
    public int size;
    public boolean hasEnd = false;
    Dialog dialog;

    private RewardedVideoAd mRewardVideoAd;
    InterstitialAd interstitialAd;
    private AdView adView;
    public static final String BANNER_ID = "ca-app-pub-5073642246912223/5273510819";
    public static final String INTERSTITIAL_ID = "ca-app-pub-5073642246912223/1171603950";
    public static final int ADD_AFTER_LEVEL = 3;

    int hint1 = 10;         //just an random
    int hint2 = 100;         //value to initialise it

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Screen MAin Activity");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        ad stuff starts here.....
        MobileAds.initialize(this, BANNER_ID);
        adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        mRewardVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        l = getIntent().getIntExtra(INTENT_LEVEL, 0);

        WORDS = new ArrayList<>();
        addSomeWords();
        size = WORDS.size();

        //these are the initialisations :(
        op1 = findViewById(R.id.op1);
        op5 = findViewById(R.id.op5);
        op2 = findViewById(R.id.op2);
        op6 = findViewById(R.id.op6);
        op3 = findViewById(R.id.op3);
        op7 = findViewById(R.id.op7);
        op4 = findViewById(R.id.op4);
        op8 = findViewById(R.id.op8);

        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);

        dialog = new Dialog(this);
        arrayList = new ArrayList<>();
        buttonList = new ArrayList<>();
        tvList = new ArrayList<>();
        tvList.add(ans1);
        tvList.add(ans2);
        tvList.add(ans3);
        tvList.add(ans4);

        setAllOptions();
        ///////////////////////////////////////////////////////////////

        easter = findViewById(R.id.easter);
        easter.setVisibility(View.INVISIBLE);

        rocket = findViewById(R.id.rocket);
        hintLeft = findViewById(R.id.hintLeft);
        if (l + 1 > second_level_change) {
            allowedHint = 1;
            hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_red));
        } else if (l + 1 > first_level_change) {
            allowedHint = 2;
            hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_red));
        }
        hintLeft.setText(String.valueOf(allowedHint - click));

        hint = findViewById(R.id.hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint_func();
            }
        });
        hintLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint_func();
            }
        });
        if (allowedHint - click <= 0) {
            hint.setText("more hints ?");
        }

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                reset();
            }
        });

        level = findViewById(R.id.level);
        level.setText("Level " + (l + 1));


        if (l < size)
            if (WORDS.get(l) != null)
                initialise_with(WORDS.get(l), buttonList);
            else {
                resetSavedLevel();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                initialise_with(null, buttonList);
                level.setText(getResources().getString(R.string.all_cleared));
            }
        else {
            Snackbar.make(ans1, "Game Ends Here..", Snackbar.LENGTH_INDEFINITE)
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
            level.setText(getResources().getString(R.string.all_cleared));
        }

    }

    // main game logic is down belowww.....
    private void addSomeWords() {
        WORDS.add("play");
        WORDS.add("rain");
        WORDS.add("time");

        if (!getSharedPreferences(SHARED_PREF, MODE_PRIVATE).getBoolean("trial", false)) {
            WORDS.add("wind");
            WORDS.add("dash");
            WORDS.add("lock");
            WORDS.add("tune");
            WORDS.add("bird");
            WORDS.add("team");
            WORDS.add("wine");
            WORDS.add("step");
            WORDS.add("food");
            WORDS.add("fine");
            WORDS.add("mute");
            WORDS.add("kite");
            WORDS.add("dark");
            WORDS.add("tyre");
            WORDS.add("sick");
            WORDS.add("skip");
            WORDS.add("chin");
            WORDS.add("soup");
            WORDS.add("kind");
            WORDS.add("mail");
            WORDS.add("spin");
            WORDS.add("toss");
            WORDS.add("pick");
            WORDS.add("sure");
            WORDS.add("show");
            WORDS.add("mind");
            WORDS.add("wink");
            WORDS.add("line");
            WORDS.add("mint");
            WORDS.add("tank");
            WORDS.add("wall");
            WORDS.add("hint");
            WORDS.add("rice");
            WORDS.add("rock");
            WORDS.add("dice");
            WORDS.add("bike");
            WORDS.add("kick");
            WORDS.add("swim");
            WORDS.add("hint");
            WORDS.add("dine");
            WORDS.add("show");
            WORDS.add("wall");
            WORDS.add("blow");
            WORDS.add("mock");
            WORDS.add("talk");
            WORDS.add("rock");
            WORDS.add("loss");
            WORDS.add("fire");
            WORDS.add("meme");
            WORDS.add("rich");
            WORDS.add("site");
            WORDS.add("wind");
            WORDS.add("hole");
            WORDS.add("wise");
        }
    }

    private void initialise_with(final String word, final ArrayList<Button> btns) {

        if (word == null) {
            for (int i = 0; i < 8; i++) {
                btns.get(i).setText("");
                vanish();
                hasEnd = true;
            }
            return;
        }
        char[] chars = generator(word).toCharArray();

        for (int i = 0; i < chars.length; i++)
            btns.get(i).setText(String.valueOf(chars[i]));

    }

    private void hint_func() {
//        all hints go here

        if (click < allowedHint && hint1 == 10) {
//             2 hints left
            click++;
            vib.vibrate(10);

            Random r = new Random();
            hint1 = r.nextInt(4);
            tvList.get(hint1).setText(String.valueOf(WORDS.get(l).charAt(hint1)).toUpperCase());
            tvList.get(hint1).setTextColor(getResources().getColor(R.color.colorGrey));

            hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_red));
            hintLeft.setText(String.valueOf(allowedHint - click));
        } else if (click < allowedHint) {
//             1 hint left
            click++;
            vib.vibrate(10);

            Random rr = new Random();
            do {
                hint2 = rr.nextInt(4);
                Log.d(TAG, "onClick: hint : " + hint1 + " and " + hint2);
            } while (hint1 == hint2 || hint2 == temp1);

            tvList.get(hint2).setText(String.valueOf(WORDS.get(l).charAt(hint2)).toUpperCase());
            tvList.get(hint2).setTextColor(getResources().getColor(R.color.colorGrey));
            hintLeft.setText(String.valueOf(allowedHint - click));
            hint.setText("more hints ?");

            temp1 = hint2;
        } else {
//             0 hint left
            if (mRewardVideoAd.isLoaded())
                mRewardVideoAd.show();
            else
                loadRewardedVideoAd();
        }

        if (allowedHint - click == 0) {
            hint.setText("more hints ?");
        }


    }

    private void setAllOptions() {

        buttonList.add(op1);
        buttonList.add(op2);
        buttonList.add(op3);
        buttonList.add(op4);
        buttonList.add(op5);
        buttonList.add(op6);
        buttonList.add(op7);
        buttonList.add(op8);

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

    private void setOutputChar(final String key, int pointer) {

        if (pointer > WORDS.get(l).length() - 1) return;

        tvList.get(pointer).setText(key);
        tvList.get(pointer).setTextColor(getResources().getColor(R.color.colorBlack));

        if (pointer == WORDS.get(l).length() - 1) {
            String userAns = ans1.getText().toString().concat(ans2.getText().toString()).concat(ans3.getText().toString()).concat(key);
            checkAnswer(userAns);
        }

    }

    private void checkAnswer(final String userAns) {

        if (hasEnd) return;

        if (userAns.equalsIgnoreCase(WORDS.get(l))) {
//            PASS
            Handler h1 = new Handler();
            h1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vib.vibrate(50);

                    dialog.setContentView(R.layout.pass_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

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
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
//                  increase level currently
                            dialog.dismiss();
                            saveData();
                            // because l+1 is level
                            if (l + 1 < size) {
                                pointer = 0;
                                l++;
                                click = 1;
                                adView.loadAd(new AdRequest.Builder().build());
                                hint1 = 10;
                                hint2 = 100;
                                temp1 = 11;

                                hint.setVisibility(View.VISIBLE);
                                hintLeft.setVisibility(View.VISIBLE);
                                if (l % ADD_AFTER_LEVEL == 0) {
                                    if (interstitialAd.isLoaded()) {
                                        interstitialAd.show();
                                        interstitialAd.setAdListener(new AdListener() {
                                            @Override
                                            public void onAdClosed() {
                                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                            }
                                        });
                                    }
                                }

                                hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                if (l + 1 > second_level_change) {
                                    allowedHint = 1;
                                    hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                } else if (l + 1 > first_level_change) {
                                    allowedHint = 2;
                                    hintLeft.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                }
                                hint.setText("want a hint ?");
                                if (allowedHint - click <= 0) {
                                    hint.setText("more hints ?");
                                }

                                hintLeft.setText(String.valueOf(allowedHint - click));
                                initialise_with(WORDS.get(l), buttonList);
                            } else {
                                Snackbar.make(ans1, "Game Ends Here..", Snackbar.LENGTH_INDEFINITE)
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
                                level.setText(getResources().getString(R.string.all_cleared));
                                return;
                            }
                            level.setText("Level " + (l + 1));
                        }
                    });
                    clearAns();

                }
            }, 200);

            //******************************************************************************************
        } else {
//            FAIL
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vib.vibrate(80);

                    dialog.setContentView(R.layout.fail_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    adView.loadAd(new AdRequest.Builder().build());

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
            }, 200);
        }
    }

    private void clearAns() {

        Handler handler = new Handler();
        vib.vibrate(50);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 1000);

    }

    private void hide_n_add(View v) {
        v.setVisibility(View.INVISIBLE);
        arrayList.add(v);
    }

    private void show_n_remove() {

        for (View v : arrayList)
            v.setVisibility(View.VISIBLE);

        arrayList.clear();
    }

    private String generator(final String words) {

        String new_words = words;
        for (int i = 0; i < words.length(); i++) {

            int random = new Random().nextInt(26) + 65;
            String ns = String.valueOf((char) random);

            new_words = new_words.concat(ns);
        }
        return jumbleUp(new_words).toUpperCase();

    }

    private String jumbleUp(final String words) {
        int len = words.length();
        char[] chArray = words.toCharArray();

        for (int i = 0; i < 20; i++) {
            int random = new Random().nextInt(len);
            char temp = chArray[random];

            int new_random = new Random().nextInt(len);
            char temp2 = chArray[new_random];

            chArray[random] = temp2;
            chArray[new_random] = temp;
        }
        return String.valueOf(chArray).toUpperCase();

    }

    private void vanish() {

        easter.setVisibility(View.VISIBLE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
            }
        };

        ans1.setOnClickListener(listener);
        ans2.setOnClickListener(listener);
        ans3.setOnClickListener(listener);
        ans4.setOnClickListener(listener);

        hint.setOnClickListener(listener);
        reset.setOnClickListener(listener);
        level.setOnClickListener(listener);
        easter.setOnClickListener(listener);
        hintLeft.setOnClickListener(listener);

        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rocket.animate().translationY(-1000).setDuration(800);
            }
        });


    }

    private void reset() {
        pointer = 0;

        for (TextView tv : tvList) {
            tv.setText(default_ques);
            tv.setTextColor(getResources().getColor(R.color.colorBlack));
            show_n_remove();
        }

    }

    private void saveData() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(LEVEL_REACHED, (l + 1));
        editor.apply();

    }

    private void resetSavedLevel() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(LEVEL_REACHED, 0);
        editor.apply();

    }


    //    all add stufffff is down belowww.....
    private void loadRewardedVideoAd() {
//        to be changed here
        if (!mRewardVideoAd.isLoaded()) {
            mRewardVideoAd.loadAd("ca-app-pub-5073642246912223/5368045435",
                    new AdRequest.Builder().build());
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRewarded(RewardItem rewardItem) {
        click--;
        hintLeft.setText(String.valueOf(allowedHint - click));
        hint.setText("want a hint ?");
        loadRewardedVideoAd();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    @Override
    public void onResume() {
        mRewardVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardVideoAd.destroy(this);
        super.onDestroy();
    }


}
