package edu.lab2.neuronnetwork.model.layer;

import java.io.Serializable;

/**
 *
 * @author Евгений
 */
public interface Layer extends Serializable{
    /**
     * Получает размер входного вектора
     * @return Размер входного вектора
     */
    int getInputSize();

    /**
     * Получает размер слоя
     * @return Размер слоя
     */
    int getSize();

    /**
     * Вычисляет отклик слоя
     * @param input Входной вектор
     * @return Выходной вектор
     */
    double[] computeOutput(double[] input);
}
