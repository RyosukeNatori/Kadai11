package com.example.kadai11;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SensorManager mSensorManager;
    Sensor mGyroMeter;
    TextView tv;
    ExampleSensorListener esl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=new TextView(this);
        tv=(TextView) findViewById(R.id.textview);
        mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroMeter=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        esl=new ExampleSensorListener();
    }
    @Override
    protected void onStart(){
        super.onStart();
        mSensorManager.registerListener(esl,mGyroMeter,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(esl);
    }
    class ExampleSensorListener implements SensorEventListener{
        public void onAccuracyChanged(Sensor sensor,int accuracy){}
        public void onSensorChanged(SensorEvent event){
            if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
                tv.setText("Pitch:"+event.values[0]+"\n"
                +"Roll:"+event.values[1]+"\n"
                +"Azimuth:"+event.values[2]);
            }
        }
    }
}