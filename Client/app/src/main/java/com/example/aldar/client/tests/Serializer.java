package com.example.aldar.client.tests;

/**
 * Абстрактный сериализатор сетевых пакетов
 */
public abstract class Serializer {

    /**
     * Сериализует данные в пакет
     * @param pitch
     * @param roll
     * @param yaw
     * @return Содержимое пакета
     */
    public abstract String Do(double pitch, double roll, double yaw);
}
