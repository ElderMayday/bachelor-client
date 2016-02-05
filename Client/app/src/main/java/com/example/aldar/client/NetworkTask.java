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
    protected AccelerometerListener accelerometerListener;
    protected String ipAddress;
    protected int port;
    protected int interval;
    protected Exception exception;

    public Exception GetException() {
        Exception temp = exception;
        exception = null;
        return temp;
    }

    public NetworkTask(AccelerometerListener _accelerometerListener, String _ipAddress, int _port, int _interval) {
        if (_accelerometerListener != null)
            accelerometerListener = _accelerometerListener;

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

                if (accelerometerListener.GetXyValid() == true)
                    s = "<" + String.valueOf(accelerometerListener.GetXyAngle() + ">\n");
                else
                    s = "<not_in_xy>\n";

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
