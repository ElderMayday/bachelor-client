package com.example.aldar.client.tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.example.aldar.client.UdpTask;

import junit.framework.TestCase;


import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;

import org.junit.Test;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.DatagramPacket;
import java.net.DatagramSocket;




import static org.mockito.Mockito.*;

/**
 * Created by Aldar on 23.04.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUdpTask extends TestCase {

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testUdpTask()
    {
        DatagramSocket socket = mock(DatagramSocket.class);

        UdpTask udpTask = new UdpTask(socket);

        udpTask.execute();

        try
        {
            Thread.sleep(100);
            verify(socket, times(1)).receive(any(DatagramPacket.class));
            verify(socket, times(1)).close();
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
