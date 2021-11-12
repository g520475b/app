/* Obstacle.java */

package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Obstacle extends View {
    // 座標と幅
    int x, y, height, width;
    Paint paint;

    public Obstacle(Context context) {
        super(context);
        x = y = height = width = 0;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRect(x, y, x+width, y+height, paint);
    }

}
