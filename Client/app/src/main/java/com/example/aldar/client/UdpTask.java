package com.example.aldar.client;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Aldar on 26.02.2016.
 */
public class UdpTask extends AsyncTask<Void, Void, Void> {

    private String ipProposed;
    private boolean mustWork;

    public UdpTask()
    {
        mustWork = true;
        ipProposed = "none";
    }

    public String GetIpProposed() { return ipProposed; }

    protected Void doInBackground(Void... urls) {
        byte[] messageChar = new byte[50];
        DatagramPacket packet = new DatagramPacket(messageChar, messageChar.length);
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(11000);

            while (mustWork) {
                socket.receive(packet);

                String text = new String (messageChar, 0,packet.getLength());

                if (text.equals("<server-request>")) {
                    ipProposed = packet.getAddress().toString();
                    ipProposed = ipProposed.replaceAll("[^0-9.]", "");
                    mustWork = false;
                }
            }
        }
        catch (Exception e)
        {
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
