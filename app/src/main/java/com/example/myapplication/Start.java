/*Start.java*/

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //カウントダウンのタイマー追加
        final TextView countText = (TextView)findViewById(R.id.timer);
        final CountDownTimer cdt = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countText.setText("開始まで"+ (millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                countText.setText("START");
                Intent intent = new Intent(Start.this, MainActivity.class);
                startActivity(intent);
            }
        };

        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                cdt.start();
            }
        });

        Button description_button = (Button) findViewById(R.id.description_button);
        description_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, description.class);
                startActivity(intent);
            }
        });
    }
}