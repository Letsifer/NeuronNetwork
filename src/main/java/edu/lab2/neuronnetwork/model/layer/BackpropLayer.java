package edu.lab2.neuronnetwork.model.layer;

/**
 *
 * @author Евгений
 */
public interface BackpropLayer extends Layer {

    /**
     * Придает случайные значения весам нейронов
     *
     * @param min Минимальное значение
     * @param max Максимальное значение
     */
    void randomize(double min, double max);

    /**
     * Выичисляет следующий вектор ошибки в обратном направлении
     *
     * @param input Входной вектор
     * @param error Вектор ошибки
     * @return Следующий вектор ошибки в обратном направлении
     */
    double[] computeBackwardError(double[] input, double[] error);

    /**
     * Подгоняет веса нейронов в сторону уменьшения ошибки
     *
     * @param input Входной вектор
     * @param error Вектор ошибки
     * @param rate Скорость обучения
     * @param momentum Моментум
     */
    void adjust(double[] input, double[] error, double rate);
}
