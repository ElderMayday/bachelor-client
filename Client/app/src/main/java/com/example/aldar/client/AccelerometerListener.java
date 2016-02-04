package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public class AccelerometerListener implements SensorEventListener {

    protected float xyAngle;
    protected float xzAngle;
    protected float yzAngle;
    protected SensorManager sensorManager;
    protected Sensor accelerometer;

    public float GetXyAngle() {
        return xyAngle;
    }

    public float GetXzAngle() {
        return xzAngle;
    }

    public float GetYzAngle() {
        return yzAngle;
    }

    public AccelerometerListener(SensorManager _sensorManager) {
        sensorManager = _sensorManager;
        accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xyAngle = event.values[0];
        xzAngle = event.values[1];
        yzAngle = event.values[2];
    }
}
