/* Over.java*/

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Over extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        Button restart_button = (Button) findViewById(R.id.restart_button);
        restart_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {//ゲーム画面に戻ります
                Intent intent = new Intent(Over.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button toTitle_button = (Button) findViewById(R.id.toTitle_button);
        toTitle_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {//タイトル画面に戻ります
                Intent intent = new Intent(Over.this, Start.class);
                startActivity(intent);
            }
        });
    }
}