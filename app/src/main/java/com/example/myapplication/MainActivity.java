/* MainActivity.java */

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import android.view.Window;
import android.widget.RelativeLayout;

import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity implements Runnable, SensorEventListener
{
    //for 文用
    int i;
    int N = 20;
    // クラス変数の宣言
    Ball ball;
    Obstacle[] obstacle = new Obstacle[N];
    Hole hole;
    Warp warp;
    Handler handler;

    int width, height;

    //BGMに関する宣言
    private MediaPlayer mMediaPlayer;

    // センサ用の変数を宣言
    float[] mgValues = new float[3];
    float[] acValues = new float[3];
    float[] attitude = new float[3];
    SensorManager mSensorManager;
    Sensor mAccelerometer, mMagField,mOrientation;

    // ボールの最大速度を定義
    int MAX_V = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // フルスクリーンで画面を利用することを宣言
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 画面の幅、高さを width，height 変数にそれぞれ保存
        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;

        // 画面の背景色をRGBでランダムに設定
        RelativeLayout relativeLayout = new RelativeLayout(this);
        int R=(int)((Math.random()+1)*256/2);
        int G=(int)(Math.random()*256);
        int B=(int)(Math.random()*256);
        relativeLayout.setBackgroundColor(Color.rgb(R, G, B));
        setContentView(relativeLayout);

        /* 画面内のオブジェクトの定義 */
        // ボールの定義
        ball = new Ball(this);
        ball.x = 100;
        ball.y = 100;
        // ゴールの定義
        hole = new Hole(this);
        hole.x = width - 100;
        hole.y = height - 100;

        //ワープの定義
        warp = new Warp(this);
        warp.x = width / 2;
        warp.y = (int)(height * Math.random());
        warp.move = -5; /* ワープの初速度*/

        // 障害物の定義
        obstacle[0] = new Obstacle(this);
        obstacle[0].x = width/2;
        obstacle[0].y = 0;
        obstacle[0].width = 50;
        obstacle[0].height = 500;

        obstacle[1] = new Obstacle(this);
        obstacle[1].x = 0;
        obstacle[1].y = height/2;
        obstacle[1].width = 500;
        obstacle[1].height = 50;

        for ( i=2; i < N; i++ ) {
            obstacle[i] = new Obstacle(this);
            do {
                do {
                    obstacle[i].x = (int) (width * random());
                    obstacle[i].y = (int) (height * random());
                } while (obstacle[i].x < 300 && obstacle[i].y < 300);
            }while (obstacle[i].x > (width - 300) && obstacle[i].y > (height - 300));

            obstacle[i].width = 100;
            obstacle[i].height = 100;
        }

        // ボール，障害物，ゴールを画面に反映
        relativeLayout.addView(ball);
        for (i = 0; i < N; i++ ){
            relativeLayout.addView(obstacle[i]);
        }
        relativeLayout.addView(hole);
        relativeLayout.addView(warp);

        // ボールの移動や衝突処理を実施するスレッドを起動
        // 画面に関する処理とは別に，run()内の処理を実行
        handler = new Handler();
        handler.postDelayed(this, 1000);

        // センサマネージャーの初期化と加速度／磁気センサの初期化
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mMagField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this,mMagField,SensorManager.SENSOR_DELAY_NORMAL);

        mOrientation =  mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this,mOrientation,SensorManager.SENSOR_DELAY_NORMAL);

        //BGMに関する処理
        if(mMediaPlayer == null || !mMediaPlayer.isPlaying()){
            music_start();
        } else {
            //音楽を停止する処理を実行
            music_stop();
        }
    }



    // ボールの移動や当たり判定に関する関数
    @Override
    public void run() {

        // 速度に応じてボールの位置を変更
        ball.x += ball.vx;
        ball.y += ball.vy;

        // 画面外処理
        if (ball.x <= ball.radius) {
            ball.x = ball.radius;
        } else if (ball.x >= width - ball.radius) {
            ball.x = width - ball.radius;
        }
        if (ball.y <= ball.radius) {
            ball.y = ball.radius;
        } else if (ball.y >= height - ball.radius) {
            ball.y = height - ball.radius;
        }

        // 障害物とのあたり判定
        for (i=0;i<N;i++) {
            if (isHit(ball, obstacle[i]) == 1) {
                /*ゲームオーバー画面への遷移 */
                Intent intent = new Intent(MainActivity.this, Over.class);
                startActivity(intent);
                // ボールを初期位置へ移動させる
                ball.x = 100;
                ball.y = 100;
                ball.vx = ball.vy = 0;
                acValues = null;
                mgValues = null;
                ball.invalidate();
                // ゲームオーバーに関する処理
            }
        }
        /*2つの障害物とワープの移動*/
        obstacle[0].y += 5;
        if ( obstacle[0].y >= height ){
            obstacle[0].y = -500;
        }
        obstacle[1].x += 5;
        if ( obstacle[1].x >= width ){
            obstacle[1].x = -500;
        }

        warp.x += warp.move;
        if ( warp.x >= width+40 || warp.x <= -40){
            warp.move *= -1;//進行方向を変えて、少しずつ加速します
            warp.y = (int)(height * Math.random());
        }

        // hole 内にボールが含まれたか判定（ゴールしたか否か判定）
        if ((hole.x - hole.r < ball.x &&
                ball.x < hole.x + hole.r) &&
                (hole.y - hole.r < ball.y &&
                        ball.y < hole.y + hole.r)) {
            ball.x = hole.x;
            ball.y = hole.y;
            ball.vx = ball.vy = 0;
            ball.invalidate();
            Intent intent = new Intent(MainActivity.this, Clear.class);
            startActivity(intent);
            // クリアに関する処理
        }
        // warp 内にボールが含まれたか判定（初期位置へ移動）
        else if ((warp.x - warp.r < ball.x &&
                ball.x < warp.x + warp.r) &&
                (warp.y - warp.r < ball.y &&
                        ball.y < warp.y + warp.r)) {
            /*ボールを初期の位置へ*/
            ball.x = 100;
            ball.y = 100;
            ball.vx = ball.vy = 0;

            // ワープに関する処理
            ball.invalidate();
            handler.post(this);
            obstacle[0].invalidate();
            obstacle[1].invalidate();
            warp.invalidate();
        }
        else {
            // 継続して関数を実行
            ball.invalidate();
            handler.post(this);
            obstacle[0].invalidate();
            obstacle[1].invalidate();
            warp.invalidate();
        }
    }

    // 障害物との当たり判定用メソッド
    public int isHit(Ball ball, Obstacle obstacle){
        if (ball.x > (obstacle.x - ball.radius) && ball.x < obstacle.x+obstacle.width && ball.y > obstacle.y && ball.y < (obstacle.y+obstacle.height)) {
            return 1;
        }else{
            return 0;
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        // センサの解除
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagField);

    }

    public void onDestroy() {
        super.onDestroy();
        // handler の解除
        handler.removeCallbacks(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){

        // 傾きセンサ用の変数
        float[] inR = new float[16];
        float[] outR = new float[16];

        // ボール速度に関する数値
        int threshold = 5;
        float delta_v = 0.2f;
        float delta_inverse_v = 0.4f;

        int MATRIX_SIZE = 16;
        float[] I = new float[MATRIX_SIZE];


        // 加速度センサ値と磁気センサ値を取得 (clone 関数を用いて acValues と mgValues に値を格納)
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                acValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mgValues = event.values.clone();
                break;
        }
        if ( acValues != null && mgValues != null ) {

            // 傾きセンサ値の取得（回転行列の計算，端末の画面設定に合わせる，方位角/傾きを取得）
            // 回転行列を計算
            SensorManager.getRotationMatrix(inR, I, acValues, mgValues);
            // 端末の画面設定に合わせる(以下は, 縦表示で画面を上にした場合)
            SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
            // 方位角/傾きを取得
            SensorManager.getOrientation(outR, attitude);


            // 傾きセンサ値（傾斜角と回転角）をラジアンから度に変更
            attitude[1] = (float)Math.toDegrees(attitude[1]);
            attitude[2] = (float)Math.toDegrees(attitude[2]);
            /* 傾きに応じてボールの速度を設定 */
            // 傾斜角（縦方向）に応じて縦方向のボール速度を変更
            if (attitude[1] < -threshold){ // 下方向にある程度傾いているなら，下方向にボールが加速
                ball.vy += delta_v;
                if (ball.vy < - threshold ) ball.vy += delta_inverse_v;
            }else if(attitude[1] > threshold){ // 上方向
                ball.vy -= delta_v;
                if (ball.vy >  threshold ) ball.vy -= delta_inverse_v;
            }
            // 回転角（横方向）に応じて横方向のボール速度を変更
            if (attitude[2] < -threshold){ // 左方向にある程度傾いているなら，左方向にボールが加速
                ball.vx -= delta_v;
                if (ball.vx > 0) ball.vx -= delta_inverse_v;
            }else if(attitude[2] > threshold) { // 右方向
                ball.vx += delta_v;
                if (ball.vx < 0) ball.vx += delta_inverse_v;
            }

            // ボールの最大速度を定義
            if (ball.vx < -MAX_V) ball.vx = -MAX_V;
            if (ball.vx > MAX_V) ball.vx = MAX_V;
            if (ball.vy < -MAX_V) ball.vy = -MAX_V;
            if (ball.vy > MAX_V) ball.vy = MAX_V;
        }

    }
    //アプリがユーザーから見えなくなった時
    @Override
    protected void onStop() {
        super.onStop();
        //音楽を停止する処理を実行
        music_stop();
    }

    private void music_start() {
        //メディアプレイヤーを生成
        mMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.play_bgm);
        //曲の先頭から再生するように設定
        mMediaPlayer.seekTo(0);
        //再生開始
        mMediaPlayer.start();
    }

    //音楽を停止する処理
    private void music_stop() {
        //メディアプレイヤーが生成されていない場合は何もせずメソッドを抜ける
        if(mMediaPlayer == null) return;
        //音楽の再生をストップ
        mMediaPlayer.stop();
        //メディアプレイヤーを解放
        mMediaPlayer.release();
        //メディアプレイヤーにNULLをセット
        mMediaPlayer = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}