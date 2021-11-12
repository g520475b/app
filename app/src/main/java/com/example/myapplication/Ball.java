/*Ball.java*/
package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.Random;

public class Ball extends View {
    // 座標，半径
    int x, y, radius;
    // 速度
    float vx, vy;
    Paint paint;

    public Ball(Context context) {
        super(context);
        radius = 30;
        vx = vy = x = y = 0;
        paint = new Paint();
        Random r = new Random();
        int n = r.nextInt(3) + 1;
        if(n == 1) {
            paint.setColor(Color.RED);
        }
        if(n == 2) {
            paint.setColor(Color.YELLOW);
        }
        if(n == 3) {
            paint.setColor(Color.BLUE);
        }
        paint.setStyle(Paint.Style.FILL);
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

}
