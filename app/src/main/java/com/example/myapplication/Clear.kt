/* Clear.java */

package com.example.ballgame;

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

/* activity_clear.xml */

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
tools:context=".Start">

<TextView
android:id="@+id/clear"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_alignStart="@+id/restart_button"
android:layout_alignLeft="@+id/restart_button"
android:layout_centerHorizontal="true"
android:layout_marginStart="-62dp"
android:layout_marginLeft="-62dp"
android:layout_marginTop="107dp"
android:text="Congratulation!"
android:textSize="40sp" />


<Button
android:id="@+id/toTitle_button"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_below="@+id/clear"
android:layout_centerHorizontal="true"
android:layout_marginTop="134dp"
android:text="タイトルへ"
android:textSize="24sp" />
</RelativeLayout>
