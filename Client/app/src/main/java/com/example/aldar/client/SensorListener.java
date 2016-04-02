package com.example.aldar.client;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Aldar on 26.01.2016.
 */
public final class SensorListener implements SensorEventListener {

    /**
     * Геттеры данных
     * @return
     */
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
     * Создает SensorListener и активирует все необходимые сенсоры
     * @param _sensorManager - получаемый из внешнего контекста менеджер сенсоров
     */
    public SensorListener(SensorManager _sensorManager) {
        Sensor sensor;

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

    /**
     * Точность датчиков изменилась
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Откалибровать гироскоп
     */
    public void GaugeGyro()
    {
        pitchG = rollG = yawG = 0.0;
    }

    /**
     * Получает данные от какого-либо сенсора
     * @param event
     */
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
            float[] mData = new float[3];
            float[] rMat = new float[9];
            float[] iMat = new float[9];
            float[] orientation = new float[3];

            mData = event.values.clone();

            if (SensorManager.getRotationMatrix(rMat, iMat, gData, mData))
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



    /**
     * Переменная для замера времени
     */
    private long start;

    /**
     * Менеджер датчиков
     */
    private SensorManager sensorManager;

    private float xAcc; // Линейное ускорение вдоль x;
    private float yAcc; // Линейное ускорение вдоль y;
    private float zAcc; // Линейное ускорение вдоль z;

    private double pitchAM, rollAM, yawAM; // Угловое положение согласно акселерометру и магнитометру
    private double pitchG, rollG, yawG; // Угловое положение согласно гироскопу

    private float[] gData = new float[3]; // Сохраненный массив данных акселерометра (для передачи в магнитометр)

}
