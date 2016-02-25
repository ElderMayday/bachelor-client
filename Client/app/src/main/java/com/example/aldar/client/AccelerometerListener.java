package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public class AccelerometerListener implements SensorEventListener {

    protected float xAcc; // Acceleration to x;
    protected float yAcc; // Acceleration to y;
    protected float zAcc; // Acceleration to z;
    protected double pitch, roll;

    protected SensorManager sensorManager;
    protected Sensor accelerometer;

    public float GetXAcc() { return xAcc; }
    public float GetYAcc() { return yAcc; }
    public float GetZAcc() { return zAcc; }
    public double GetPitch() { return pitch; }
    public double GetRoll() { return roll; }

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
        xAcc = event.values[0];
        zAcc = event.values[1];
        yAcc = event.values[2];

        double x = xAcc;
        double y = yAcc;
        double z = zAcc;

        double length = Math.sqrt(x * x + z * z + y * y); // total length of acceleration vector

        x = x / length;
        z = z / length;
        y = y / length;

        pitch = Math.asin(y);
        roll = Math.asin(-z / Math.cos(pitch));

        pitch = Math.toDegrees(pitch);
        roll = Math.toDegrees(roll);
    }
}
