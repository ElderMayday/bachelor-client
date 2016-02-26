package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public class SensorListener implements SensorEventListener {

    protected float xAcc; // Acceleration to x;
    protected float yAcc; // Acceleration to y;
    protected float zAcc; // Acceleration to z;
    protected double pitch, roll, yaw;

    protected SensorManager sensorManager;
    protected Sensor sensor;

    protected float[] gData = new float[3];
    protected float[] mData = new float[3];
    protected float[] rMat = new float[9];
    protected float[] iMat = new float[9];
    protected float[] orientation = new float[3];

    public float GetXAcc() { return xAcc; }
    public float GetYAcc() { return yAcc; }
    public float GetZAcc() { return zAcc; }
    public double GetPitch() { return pitch; }
    public double GetRoll() { return roll; }
    public double GetYaw() { return yaw; }


    public SensorListener(SensorManager _sensorManager) {
        sensorManager = _sensorManager;

        sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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

            gData = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mData = event.values.clone();

            if ( SensorManager.getRotationMatrix(rMat, iMat, gData, mData))
            {
                yaw = (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }
        }
    }
}
