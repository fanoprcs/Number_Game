package com.example.quiz;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int mode = 0;//0 for normal
    private Boolean Life;
    private Boolean Win;
    private int[] table;
    private double initTime;
    private double totalTime;
    DecimalFormat df = new DecimalFormat("##0.00");
    private int now;
    private Button start;
    private Button[] numBtn;
    private Timer timer;
    private TextView timeMsn;
    private TextView score;
    private Button settings;
    private TextView randomScore;
    private TextView normalScore;

    private final String NONE = "none";
    private final String NORMAL_SCORE_KEY = "bestScoreOfNormal";
    private final String RANDOM_SCORE_KEY = "bestScoreOfRandom";
    private SharedPreferences mPreferences;
    private final String sharedFile = "com.example.android.digitalGame";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        randomScore = (TextView) findViewById(R.id.randomScore);
        normalScore = (TextView) findViewById(R.id.normalScore);
        settings = (Button) findViewById(R.id.settings);
        timeMsn = (TextView) findViewById(R.id.time);
        score = (TextView) findViewById(R.id.Score);
        mPreferences = getSharedPreferences(sharedFile, MODE_PRIVATE);
        float tmp;
        tmp = mPreferences.getFloat(NORMAL_SCORE_KEY, 0);
        if (tmp == 0)
            normalScore.setText(NONE);
        else
            normalScore.setText(String.valueOf(tmp));
        tmp = mPreferences.getFloat(RANDOM_SCORE_KEY, 0);
        if (tmp == 0)
            randomScore.setText(NONE);
        else
            randomScore.setText(String.valueOf(tmp));
        contactBtn();
        reset();
    }
    @Override
    protected void onPause() {
        super.onPause();
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        if(!normalScore.getText().toString().equals(NONE))
            preferencesEditor.putFloat(NORMAL_SCORE_KEY, Float.parseFloat(normalScore.getText().toString()));
        if(!randomScore.getText().toString().equals(NONE))
            preferencesEditor.putFloat(RANDOM_SCORE_KEY, Float.parseFloat(randomScore.getText().toString()));
        preferencesEditor.apply();
    }
    void contactBtn() {
        numBtn = new Button[16];
        start = (Button) findViewById(R.id.start);
        numBtn[0] = (Button) findViewById(R.id.button);
        numBtn[1] = (Button) findViewById(R.id.button1);
        numBtn[2] = (Button) findViewById(R.id.button2);
        numBtn[3] = (Button) findViewById(R.id.button3);
        numBtn[4] = (Button) findViewById(R.id.button4);
        numBtn[5] = (Button) findViewById(R.id.button5);
        numBtn[6] = (Button) findViewById(R.id.button6);
        numBtn[7] = (Button) findViewById(R.id.button7);
        numBtn[8] = (Button) findViewById(R.id.button8);
        numBtn[9] = (Button) findViewById(R.id.button9);
        numBtn[10] = (Button) findViewById(R.id.button10);
        numBtn[11] = (Button) findViewById(R.id.button11);
        numBtn[12] = (Button) findViewById(R.id.button12);
        numBtn[13] = (Button) findViewById(R.id.button13);
        numBtn[14] = (Button) findViewById(R.id.button14);
        numBtn[15] = (Button) findViewById(R.id.button15);
    }

    @SuppressLint("SetTextI18n")
    public void onClickNumber(View view) {
        Button number1 = (Button) view;
        if (number1.getText().toString().equals(String.valueOf(now))) {
            now++;
            number1.setVisibility(View.INVISIBLE);
            if (Life) {
                resetTime();
                timeMsn.setText("Time remaining : " + String.valueOf(initTime) + "s");
            }
            for (int i = 0; i < 16; i++) {
                if (view.getId() == numBtn[i].getId()) {
                    table[i] = 0;
                    break;
                }
            }
            if (mode == 1) {
                randomNum();
            }
            if (now > 16) {
                victory();
            }
        } else {
            int x = now;
            showNum(x);
        }
    }

    public void onClickStart(View view) {
        start.setVisibility(View.INVISIBLE);
        settings.setVisibility(View.INVISIBLE);
        reset();
        randomNum();
        resetTime();
        runTime();
    }

    public void randomNum() {
        int count = 0;
        for (int i = 0; i < 16; i++) {
            if (table[i] == 1) {
                count++;
            }
        }
        int[] ranArray = new int[count];
        for (int i = 0; i < count; i++) {
            ranArray[i] = (int) (Math.random() * count) + 17 - count;
            for (int j = 0; j < i; j++) {
                while (ranArray[j] == ranArray[i]) {
                    j = 0;
                    ranArray[i] = (int) (Math.random() * count) + 17 - count;
                }
            }
        }
        int k = 0;
        for (int i = 0; i < 16; i++) {
            if (table[i] == 1) {
                numBtn[i].setText(String.valueOf(ranArray[k]));
                k++;
            }
        }
    }

    void resetTime() {
        initTime = 5;
    }

    @SuppressLint("SetTextI18n")
    void victory() {
        Win = true;
        timer.cancel();
        resetTime();
        showVictory();
        float tmp = Float.parseFloat(score.getText().toString());
        if (mode == 0 ) {
            if(normalScore.getText().toString().equals(NONE))
                normalScore.setText(String.valueOf(tmp));
            else if (tmp < Float.parseFloat(normalScore.getText().toString())) {
                normalScore.setText(String.valueOf(tmp));
            }

        }
        else{
            if(randomScore.getText().toString().equals(NONE))
                randomScore.setText(String.valueOf(tmp));
            else if (tmp < Float.parseFloat(randomScore.getText().toString()))
                randomScore.setText(String.valueOf(tmp));
        }
        timeMsn.setText("Time remaining : " + String.valueOf(df.format(initTime)) + "s");
        start.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    void lose() {
        timer.cancel();
        timeMsn.setText("Time remaining : " + String.valueOf(0) + "s");
        showLose();
        Life = false;
        score.setText("Fail");
        start.setText("Restart the game!!");
        start.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
    }

    void runTime() {
        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                                  @SuppressLint("SetTextI18n")
                                  public void run() {

                                      if (initTime > 0.01) {
                                          if (!Win) {
                                              initTime -= 0.01;
                                              totalTime += 0.01;
                                              timeMsn.setText("Time remaining : " + String.valueOf(df.format(initTime)) + "s");
                                              score.setText(String.valueOf(df.format(totalTime)));
                                          }
                                      } else {
                                          lose();
                                      }
                                  }
                              }
                );
            }
        };
        timer.schedule(task, 0, 10);
    }

    void showNum(int x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("您應該點擊的是 : " + x);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showVictory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("您的成績是: " + String.valueOf(df.format(totalTime)) + "秒").setTitle("恭喜獲勝");
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.victory, null));
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showLose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("挑戰失敗");
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.lose, null));
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void reset() {
        Life = true;
        Win = false;
        now = 1;
        totalTime = 0;
        table = new int[16];
        for (int i = 0; i < 16; i++) {
            table[i] = 1;
            numBtn[i].setText("0");
            numBtn[i].setVisibility(View.VISIBLE);
        }
    }

    void changeMode() {
        if (mode == 0)
            mode = 1;
        else
            mode = 0;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        int type = data.getIntExtra(Settings.EXTRA_REPLY, 0);
                        if (type != mode)
                            changeMode();
                        if (data.getBooleanExtra(Settings.CLEAR, false)) {
                            SharedPreferences.Editor preferences = mPreferences.edit();
                            preferences.clear();
                            preferences.apply();
                            float tmp;
                            tmp = mPreferences.getFloat(NORMAL_SCORE_KEY, 0);
                            if (tmp == 0)
                                normalScore.setText(NONE);
                            else
                                normalScore.setText(String.valueOf(tmp));
                            tmp = mPreferences.getFloat(RANDOM_SCORE_KEY, 0);
                            if (tmp == 0)
                                randomScore.setText(NONE);
                            else
                                randomScore.setText(String.valueOf(tmp));
                        }
                    }
                }
            });

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        if(normalScore.getText().toString()!=NONE)
        intent.putExtra("normalScore", Float.parseFloat(normalScore.getText().toString()));
        else
            intent.putExtra("normalScore", Float.parseFloat("0"));
        if(randomScore.getText().toString()!=NONE)
        intent.putExtra("randomScore", Float.parseFloat(randomScore.getText().toString()));
        else
            intent.putExtra("randomScore", Float.parseFloat("0"));
        intent.putExtra("Mode", mode);
        someActivityResultLauncher.launch(intent);
    }
}
