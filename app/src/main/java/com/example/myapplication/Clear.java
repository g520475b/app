/* Clear.java */

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Clear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);


        Button toTitle_button = (Button) findViewById(R.id.toTitle_button);
        toTitle_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {//タイトル画面に戻ります
                Intent intent = new Intent(Clear.this, Start.class);
                startActivity(intent);
            }
        });
    }
}
