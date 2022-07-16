package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.settings.extra.REPLY";
    public static final String CLEAR = "com.example.android.settings.extra.CLEAR";
    private Button modBtn;
    private int mode_type;

    private TextView normalText;
    private  TextView randomText;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        modBtn = (Button) findViewById(R.id.mode);
        normalText = (TextView)findViewById(R.id.bestScoreNormal);
        randomText = (TextView)findViewById(R.id.bestScoreRandom);
        Intent intent = getIntent();
        float nIntent;
        float rIntent;
        nIntent = intent.getFloatExtra("normalScore", 0);
        rIntent = intent.getFloatExtra("randomScore", 0);
        if(nIntent != 0)
        normalText.setText("Best score on normal: " + String.valueOf(nIntent));
        else
            normalText.setText("Best score on normal: none");
        if(rIntent!=0)
            randomText.setText("Best score on random: " +String.valueOf(rIntent));
        else
            randomText.setText("Best score on random: none");

        if(intent.getIntExtra("Mode", 0) == 0){
            modBtn.setText("Mode: Normal");
            mode_type = 0;
        }
        else{
            modBtn.setText("Mode: Random");
            mode_type = 1;
        }
    }

    public void soundSwitch(View view) {

    }

    @SuppressLint("SetTextI18n")
    public void modeSwitch(View view) {
        if (mode_type == 0) {
            modBtn.setText("Mode: Random");
            mode_type = 1;
        } else {
            modBtn.setText("Mode: Normal");
            mode_type = 0;
        }
        Intent replyResult = new Intent();
        replyResult.putExtra(EXTRA_REPLY, mode_type);
        setResult(RESULT_OK, replyResult);
    }

    public void onClickBackToGame(View view) {
        finish();
    }
    @SuppressLint("SetTextI18n")
    public void clearData(View view)
    {
        Intent replyResult = new Intent();
        replyResult.putExtra(CLEAR, true);
        normalText.setText("Best score on normal: none");
        randomText.setText("Best score on random: none");
        setResult(RESULT_OK, replyResult);
    }
}