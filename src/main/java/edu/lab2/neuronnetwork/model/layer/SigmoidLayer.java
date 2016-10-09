package edu.lab2.neuronnetwork.model.layer;

import edu.lab2.neuronnetwork.model.util.Checkers;
import java.util.Random;

/**
 *
 * @author Евгений
 */
public class SigmoidLayer implements BackpropLayer {

    /**
     * Вес
     */
    private final int WEIGHT = 0;

    /**
     * Дельта
     */
    private final int DELTA = 1;

    /**
     * Размер входного вектора
     */
    private final int inputSize;

    /**
     * Флаг биполярного слоя
     */
    private final boolean bipolar;

    /**
     * Матрица слоя
     */
    private double[][][] matrix;

    /**
     * Констрирует сигмоидальный слой
     *
     * @param inputSize Размер входного вектора
     * @param size Размер слоя
     * @param bipolar Флаг биполярного слоя
     */
    public SigmoidLayer(int inputSize, int size, boolean bipolar) {
        // проверки
        if (inputSize < 1) {
            throw new IllegalArgumentException("Input size is not correct");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Input size is not correct");
        }

        // создаем слой
        matrix = new double[size][inputSize + 1][2];

        // запомним параметры
        this.inputSize = inputSize;
        this.bipolar = bipolar;
    }

    /**
     * Конструирует биполярный слой
     *
     * @param inputSize Размер входного вектора
     * @param size Размер слоя
     */
    public SigmoidLayer(int inputSize, int size) {
        this(inputSize, size, true);
    }

    @Override
    public int getInputSize() {
        return inputSize;
    }

    @Override
    public int getSize() {
        return matrix.length;
    }

    @Override
    public double[] computeOutput(double[] input) {
        // проверки
        Checkers.checkArray(input, "input", inputSize);
        // вычислим выход
        final int size = matrix.length;
        double[] output = new double[size];
        for (int i = 0; i < size; i++) {
            output[i] = matrix[i][0][WEIGHT];
            for (int j = 0; j < inputSize; j++) {
                output[i] += input[j] * matrix[i][j + 1][WEIGHT];
            }
            if (bipolar) {
                output[i] = Math.tanh(output[i]);
            } else {
                output[i] = 1 / (1 + Math.exp(-output[i]));
            }
        }

        // вернем оклик
        return output;
    }

    @Override
    public void randomize(double min, double max) {
        final int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < inputSize + 1; j++) {
                matrix[i][j][WEIGHT] = min + (max - min) * Math.random();
                matrix[i][j][DELTA] = 0;
            }
        }
    }

    @Override
    public double[] computeBackwardError(double[] input, double[] error) {
        // проверки
        Checkers.checkArray(input, "input", inputSize);
        Checkers.checkArray(error, "error", matrix.length);

        // вычислим входящую ошибку
        double[] output = computeOutput(input);
        final int size = matrix.length;
        double[] backwardError = new double[inputSize];

        for (int i = 0; i < inputSize; i++) {
            backwardError[i] = 0;
            for (int j = 0; j < size; j++) {
                backwardError[i]
                        += error[j] * matrix[j][i + 1][WEIGHT] * countWithBipolar(output[j]);
            }
        }

        // вернем ошибку
        return backwardError;
    }

    @Override
    public void adjust(double[] input, double[] error, double rate) {
        // проверки
        Checkers.checkArray(input, "input", inputSize);
        Checkers.checkArray(error, "error", matrix.length);

        // обновляем веса
        double[] output = computeOutput(input);
        final int size = matrix.length;

        for (int i = 0; i < size; i++) {
            final double grad = error[i] * countWithBipolar(output[i]);
            // обновляем нулевой вес
            matrix[i][0][DELTA] = rate * grad + matrix[i][0][DELTA];
            matrix[i][0][WEIGHT] += matrix[i][0][DELTA];
            // обновим остальные веса
            for (int j = 0; j < inputSize; j++) {
                matrix[i][j + 1][DELTA] = rate * input[j] * grad + matrix[i][j + 1][DELTA];
                matrix[i][j + 1][WEIGHT] += matrix[i][j + 1][DELTA];
            }
        }
    }

    private double countWithBipolar(double value) {
        return bipolar
                ? 1 - value * value
                : value * (1 - value);
    }

}
