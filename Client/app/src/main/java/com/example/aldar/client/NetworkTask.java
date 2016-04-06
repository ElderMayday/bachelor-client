package com.example.aldar.client;

import android.os.AsyncTask;

import com.example.aldar.client.serializer.Serializer;
import com.example.aldar.client.serializer.SerializerCustom;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Отправляет асинхронно данные гироскопа по TCP протоколу с некоторой периодичностью
 */
public final class NetworkTask extends AsyncTask<Void, Void, Void> {

    /**
     * Создает объект отправки для дальнейшей передачи данных с заданными параметрами
     * @param _sensorListener Слушатель датчиков
     * @param _ipAddress Целевой IP-адрес
     * @param _port Целевой порт
     * @param _interval Интервал отправки
     * @param _modeAm
     */
    public NetworkTask(SensorListener _sensorListener, String _ipAddress, int _port, int _interval, boolean _modeAm) {
        if (_sensorListener != null)
            sensorListener = _sensorListener;

        ipAddress = _ipAddress;
        port = _port;
        interval = _interval;
        modeAm = _modeAm;

        isOperating = false;
        exception = null;
    }

    /**
     * Останавливает поток
     */
    public void Stop() {
        isOperating = false;
    }

    /**
     * Возвращает полученное исключение
     * @return
     */
    public Exception GetException() {
        Exception temp = exception;
        exception = null;
        return temp;
    }

    /**
     * Возвращает флаг работы
     * @return
     */
    public boolean IsOperating() {
        return isOperating;
    }



    /**
     * Метод-обработчик потока
     * @param urls
     * @return
     */
    @Override
    protected Void doInBackground(Void... urls) {
        try {
            exception = null;

            Socket socket = new Socket(ipAddress, port);
            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Serializer serializer = new SerializerCustom();

            isOperating = true;
            while (isOperating) {
                String s;

                double pitch, roll, yaw;

                if (modeAm)
                    s = serializer.Do(sensorListener.GetPitchAM(), sensorListener.GetRollAM(), sensorListener.GetYawAM());
                else
                    s = serializer.Do(sensorListener.GetPitchG(), sensorListener.GetRollG(), sensorListener.GetYawG());

                outToServer.print(s);
                outToServer.flush();
                Thread.sleep(interval);
            }

            outToServer.close();
            socket.close();
        }
        catch (Exception e) {
            exception = e;
        }
        return null;
    }

    /**
     * Метод, выполняющийся до выполнения потока
     */
    @Override
    protected void onPreExecute(){
    }

    /**
     * Метод, выполняющийся после выполнения потока
     * @param feed
     */
    @Override
    protected void onPostExecute(Void feed) {
    }



    /**
     * Флаг работы потока
     */
    protected boolean isOperating;

    /**
     * Слушатель датчиков
     */
    protected SensorListener sensorListener;

    /**
     * Целевой IP-адрес
     */
    protected String ipAddress;

    /**
     * Целевой порт
     */
    protected int port;

    /**
     * Интервал отправки
     */
    protected int interval;

    /**
     * Полученное исключение
     */
    protected Exception exception;

    /**
     * Режим работы - акселерометр и гироскоп
     */
    protected boolean modeAm;
}
