package com.example.yangchunghsuan.project;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static android.util.Half.EPSILON;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    SensorEventListener listener;
    Sensor sensor_m;
    Sensor sensor_a;
    int count =1;
    TextView textView;

    float[] accelerometerValues=new float[3];
    float[] magneticFieldValues=new float[3];
    float[] values=new float[3];
    float[] r = new float[9];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_m = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensor_a = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    accelerometerValues = event.values;
                }
                if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                    magneticFieldValues = event.values;
                }


                //調用getRotaionMatrix獲得變換矩陣R[]
                SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticFieldValues);
                SensorManager.getOrientation(r, values);

                //經過SensorManager.getOrientation(R, values);得到的values值爲弧度
                //轉換爲角度
                values[0]=(float)Math.toDegrees(values[0]);
                values[1]=(float)Math.toDegrees(values[1]);
                values[2]=(float)Math.toDegrees(values[2]);


                if(Math.round(values[0])<0){
                    values[0] = 360+values[0];
                }

                if (Math.round(values[0])<45 || Math.round(values[0])>315){
                    textView.setText("北"+ Math.round(values[0]));
                }else if(Math.round(values[0])>45 && Math.round(values[0])<135){
                    textView.setText("東"+Math.round(values[0]));
                }else if(Math.round(values[0])>135 && Math.round(values[0])<225){
                    textView.setText("南"+ Math.round(values[0]));
                }else if(Math.round(values[0])>225 && Math.round(values[0])<315){
                    textView.setText("西" + Math.round(values[0]));
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
        sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }
}
