package com.example.kadai11;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SensorManager mSensorManager;
    Sensor mGyroMeter;
    TextView tv;
    BallSurfaceView bsv;
    Thread th;
    SurfaceHolder holder;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=new TextView(this);
        tv=(TextView) findViewById(R.id.textview);
        SurfaceView sv=findViewById(R.id.surfaceView);
        mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroMeter=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        bsv =new BallSurfaceView();
        holder=sv.getHolder();
        holder.addCallback(bsv);
    }
    @Override
    protected void onStart(){
        super.onStart();
        mSensorManager.registerListener(bsv,mGyroMeter,SensorManager.SENSOR_DELAY_NORMAL);
        th=new Thread(bsv);
        th.start();
    }
    @Override
    protected void onStop(){
        super.onStop();
        th=null;
        mSensorManager.unregisterListener(bsv);
    }
    class BallSurfaceView implements SensorEventListener, SurfaceHolder.Callback,Runnable {
        int screen_width;
        int screen_height;
        Ball ba;
        Racket ra;
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
            screen_height=height;
            screen_width=width;
        }
        public void surfaceCreated(SurfaceHolder holder){
            ba=new Ball();
            ra=new Racket();
        }
        class Racket {
            int x = 500;

            void draw(Canvas ca) {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                ca.drawRect(x, screen_height - 20, x + 100, screen_height - 10, paint);
            }
        }
            class Ball{
                int x=0,y=0,r=10,dx=5,dy=5;
                void move(){
                    x=x+dx;
                    y=y+dy;
                    if(y>screen_height){
                        dx=0;
                        dy=0;
                    }
                    if(x<0||x>screen_width){
                        dx=dx*-1;
                    }
                    if(y<0||(y>screen_height-20&&ra.x<x&&ra.x+100>x)){
                        dy=dy*-1;
                    }
                }
                void draw(Canvas ca){
                    Paint paint=new Paint();
                    paint.setColor(Color.WHITE);
                    ca.drawCircle(x,y,r,paint);
                }
            }
        public void run(){
            while (th!=null){
                Canvas canvas=holder.lockCanvas();
                if(canvas!=null){
                    canvas.drawColor(Color.BLACK);
                    ba.move();
                    ba.draw(canvas);
                    ra.draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public void surfaceDestroyed(SurfaceHolder holder){}
        public void onAccuracyChanged(Sensor sensor,int accuracy){}
        public void onSensorChanged(SensorEvent event){
            if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
                if (event.values[1]>0){
                    if(ra.x+100<=screen_width){
                        ra.x=ra.x+100;
                    }
                }
                else if(event.values[1]<0){
                    if(ra.x-100>=0){
                        ra.x=ra.x-100;
                    }
                }
            }
        }
    }
}