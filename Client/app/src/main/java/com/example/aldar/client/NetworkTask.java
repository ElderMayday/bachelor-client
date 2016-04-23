package com.example.aldar.client;

import android.os.AsyncTask;
import android.util.Log;

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
     * @param _socket Целевой сокет
     * @param _interval Интервал отправки
     * @param _modeAm
     */
    public NetworkTask(SensorListener _sensorListener, Socket _socket, int _interval, boolean _modeAm) {
        sensorListener = _sensorListener;

        socket = _socket;
        interval = _interval;
        modeAm = _modeAm;

        isOperating = false;
        exception = null;
    }

    public NetworkTask(SensorListener _sensorListener, String _ipAddress, int _port, int _interval, boolean _modeAm) {
        if (_sensorListener != null)
            sensorListener = _sensorListener;

        ipAddress = _ipAddress;
        port = _port;
        modeAm = _modeAm;
        socket = null;

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

            if (socket == null)
                socket = new Socket(ipAddress, port);

            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            sensorListener.GetPitchAM();

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
     * Целевой сокет
     */
    protected Socket socket;

    /**
     * Целевой сокет
     */
    protected String ipAddress;

    /**
     * Целевой сокет
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
