package com.example.aldar.client.tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.example.aldar.client.serializer.Serializer;
import com.example.aldar.client.serializer.SerializerCustom;

import junit.framework.TestCase;

/**
 * Created by Aldar on 06.04.2016.
 */
public class TestSerializerCustom extends TestCase {
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @SmallTest
    public void testSerializerCustom()
    {
        Serializer serializer = new SerializerCustom();

        String result = serializer.Do(10.4, -2, 8);

        assertEquals(result, "<10.4;-2.0;8.0>\n");
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.setUp();
    }
}
