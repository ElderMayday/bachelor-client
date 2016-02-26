package com.example.aldar.client;

import android.os.AsyncTask;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Aldar on 26.01.2016.
 */
public class NetworkTask extends AsyncTask<Void, Void, Void> {

    protected boolean isOperating;
    protected SensorListener sensorListener;
    protected String ipAddress;
    protected int port;
    protected int interval;
    protected Exception exception;

    public Exception GetException() {
        Exception temp = exception;
        exception = null;
        return temp;
    }

    public NetworkTask(SensorListener _sensorListener, String _ipAddress, int _port, int _interval) {
        if (_sensorListener != null)
            sensorListener = _sensorListener;

        ipAddress = _ipAddress;
        port = _port;
        interval = _interval;

        isOperating = false;
        exception = null;
    }

    public boolean IsOperating() {
        return isOperating;
    }

    public void Stop() {
        isOperating = false;
    }

    protected Void doInBackground(Void... urls) {
        try {
            exception = null;
            Socket socket = new Socket(ipAddress, port);
            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            isOperating = true;
            while (isOperating) {
                String s;

                s = "<" + String.valueOf(sensorListener.GetPitch() + ";" + sensorListener.GetRoll() + ">\n");

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

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected void onPostExecute(Void feed) {
    }
}
