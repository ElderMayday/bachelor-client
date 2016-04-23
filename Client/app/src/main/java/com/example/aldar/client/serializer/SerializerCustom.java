package com.example.aldar.client.serializer;

import java.text.DecimalFormat;

/**
 * Сериализатор своих пакетов
 */
public final class SerializerCustom extends Serializer {
    @Override
    public String Do(double pitch, double roll, double yaw) {
        String result;

        DecimalFormat df = new DecimalFormat("#.##");

        result =  "<" + df.format(pitch)
                + ";" + df.format(roll)
                + ";" + df.format(yaw) + ">\n";

        return result;
    }
}
