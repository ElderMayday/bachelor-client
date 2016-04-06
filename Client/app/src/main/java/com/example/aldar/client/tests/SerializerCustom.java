package com.example.aldar.client.tests;

/**
 * Сериализатор своих пакетов
 */
public final class SerializerCustom extends Serializer {
    @Override
    public String Do(double pitch, double roll, double yaw) {
        String result;

        result =  "<" + pitch
                + ";" + roll
                + ";" + yaw + ">\n";

        return result;
    }
}
