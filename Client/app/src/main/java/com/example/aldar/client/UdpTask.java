package com.example.aldar.client;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Поток принятия UDP рассылки
 */
public final class UdpTask extends AsyncTask<Void, Void, Void> {

    /**
     * Создаёт поток принятия UDP рассылки
     */
    public UdpTask(DatagramSocket _socket) {
        mustWork = true;
        ipProposed = "none";
        socket = _socket;
    }

    public String GetIpProposed() { return ipProposed; }




    /**
     * Метод-обработчик потока
     * @return
     */
    protected Void doInBackground(Void... params) {
        byte[] messageChar = new byte[50];
        DatagramPacket packet = new DatagramPacket(messageChar, messageChar.length);

        try {
            socket.receive(packet);

            String text = new String(messageChar, 0, packet.getLength());

            if (serverCriteria(text)) {
                ipProposed = packet.getAddress().toString();
                ipProposed = ipProposed.replaceAll("[^0-9.]", "");
                mustWork = false;
            }

            socket.close();
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * Критерий получения сообщения от сервера
     * @param text Полученный текст
     * @return Флаг - результат проверки на критерий
     */
    protected boolean serverCriteria(String text) {
        return text.equals("<server-request>");
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
     * Результат - автоопределенный IP
     */
    private String ipProposed;

    /**
     * Флаг необходимости работы потока
     */
    private boolean mustWork;


    private DatagramSocket socket;
}
