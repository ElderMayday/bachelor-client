package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public class AccelerometerListener implements SensorEventListener {

    protected float xyAcc; // Acceleration to z;
    protected float xzAcc; // Acceleration to y;
    protected float yzAcc; // Acceleration to x;

    protected double xyAngle;
    protected boolean xyValid; // is in XY plane

    protected SensorManager sensorManager;
    protected Sensor accelerometer;

    public float GetXyAcc() {
        return xyAcc;
    }

    public float GetXzAcc() {
        return xzAcc;
    }

    public float GetYzAcc() {
        return yzAcc;
    }

    public double GetXyAngle() { return xyAngle; }

    public boolean GetXyValid() { return xyValid; }

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
        xyAcc = event.values[0];
        xzAcc = event.values[1];
        yzAcc = event.values[2];

        double a1 = xyAcc;
        double a2 = xzAcc;
        double a3 = yzAcc;

        double length = Math.sqrt(a1 * a1 + a2 * a2 + a3 * a3); // total length of acceleration vector

        a1 = a1 / length;
        a2 = a2 / length;
        a3 = a3 / length;

        if (Math.abs(a3) <= 0.7) {
            xyAngle = Math.toDegrees(Math.acos(a1));
            xyValid = true;

            if (a2 < 0)
                xyAngle *= -1;
        }
        else {
            xyValid = false;
        }
    }
}
