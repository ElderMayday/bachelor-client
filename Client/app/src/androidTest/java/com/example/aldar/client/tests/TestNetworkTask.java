package com.example.aldar.client.tests;

import android.hardware.SensorEventListener;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.aldar.client.NetworkTask;
import com.example.aldar.client.SensorListener;
import com.example.aldar.client.UdpTask;

import junit.framework.TestCase;


import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;

import org.junit.Test;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.Socket;
import android.os.AsyncTask;

/**
 * Created by Aldar on 23.04.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestNetworkTask extends TestCase{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testNetworkTask()
    {
        Socket socket = mock(Socket.class);

        SensorListener sensorListener = mock(SensorListener.class);

        NetworkTask networkTask = new NetworkTask(sensorListener, socket, 0, true);

        try {
            networkTask.execute();
            Thread.sleep(200);
            networkTask.Stop();
            Thread.sleep(200);
        }
        catch (Exception e)
        {
        }

        try {
            verify(socket, times(1)).getOutputStream();
        }
        catch (Exception e)
        {
        }
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.setUp();
    }
}
