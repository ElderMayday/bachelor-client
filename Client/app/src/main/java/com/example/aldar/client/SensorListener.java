package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public class SensorListener implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor sensor;

    protected float xAcc; // Acceleration to x;
    protected float yAcc; // Acceleration to y;
    protected float zAcc; // Acceleration to z;

    protected double pitchAM, rollAM, yawAM;
    protected double pitchG, rollG, yawG;

    protected float[] gData = new float[3];
    protected float[] mData = new float[3];
    protected float[] rMat = new float[9];
    protected float[] iMat = new float[9];
    protected float[] orientation = new float[3];

    public float GetXAcc() { return xAcc; }
    public float GetYAcc() { return yAcc; }
    public float GetZAcc() { return zAcc; }
    public double GetPitchAM() { return pitchAM; }
    public double GetRollAM() { return rollAM; }
    public double GetYawAM() { return yawAM; }
    public double GetPitchG() { return pitchG; }
    public double GetRollG() { return rollG; }
    public double GetYawG() { return yawG; }

    /**
     * Переменная
     */
    private long start;

    /**
     * Создает SensorListener и активирует все необходимые сенсоры
     * @param _sensorManager - получаемый из внешнего контекста менеджер сенсоров
     */
    public SensorListener(SensorManager _sensorManager) {
        sensorManager = _sensorManager;

        sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        pitchAM = rollAM = yawAM = 0.0;
        pitchG = rollG = yawG = 0.0;

        start = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void GaugeGyro()
    {
        pitchG = rollG = yawG = 0.0;
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

            pitchAM = Math.asin(y);
            rollAM = Math.asin(-z / Math.cos(pitchAM));

            pitchAM = Math.toDegrees(pitchAM);
            rollAM = Math.toDegrees(rollAM);

            gData = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mData = event.values.clone();

            if ( SensorManager.getRotationMatrix(rMat, iMat, gData, mData))
            {
                yawAM = (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            double diff = 0.001 * (System.currentTimeMillis() - start);

            pitchG -= event.values[0] * 180.0 / Math.PI * diff;
            yawG -= event.values[1] * 180.0 / Math.PI * diff;
            rollG -= event.values[2] * 180.0 / Math.PI * diff;

            start = System.currentTimeMillis();
        }
    }
}
