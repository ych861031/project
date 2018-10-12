package com.example.yangchunghsuan.project;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    SensorEventListener listener;
    Sensor sensor;
    int count =1;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(count++ ==20){
                    double value = Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] +event.values[2]*event.values[2]);
                    String str = String.format("X:%8.4f , Y:%8.4f , Z:%8.4f \n总值为：%8.4f",event.values[0],event.values[1],event.values[2],value);
                    count = 1;
                    textView.setText(str);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }
}
