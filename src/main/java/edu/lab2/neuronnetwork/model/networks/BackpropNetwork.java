package edu.lab2.neuronnetwork.model.networks;

import edu.lab2.neuronnetwork.model.layer.BackpropLayer;
import edu.lab2.neuronnetwork.model.layer.Layer;
import edu.lab2.neuronnetwork.model.util.Checkers;

/**
 *
 * @author Евгений
 */
public class BackpropNetwork extends Network {

    /**
     * Обучает сеть паттерну
     *
     * @param input Входной вектор
     * @param goal Заданный выходной вектор
     * @param rate Скорость обучения
     * @param momentum Моментум
     * @return Текущую ошибку обучения
     */
    /**
     * Констрирует нейронную сеть с заданными слоями
     *
     * @param layers
     */
    public BackpropNetwork(Layer[] layers) {
        // передадим родакам
        super(layers);
        // рандомизируем веса
        randomize(0, 3);
    }

    /**
     * Придает случайные значения весам нейроннов в сети
     *
     * @param min
     * @param max
     */
    public final void randomize(double min, double max) {
        // придаем случайные значения весам в сети
        final int size = getSize();
        for (int i = 0; i < size; i++) {
            Layer layer = getLayer(i);
            if (layer instanceof BackpropLayer) {
                ((BackpropLayer) layer).randomize(min, max);
            }
        }
    }

    public double learnPattern(double[] input, double[] goal, double rate) {
        // проверки
        Checkers.checkArray(input, "input", 0);
        Checkers.checkArray(goal, "goal", 0);
        // делаем проход вперед
        final int size = getSize();
        double[][] outputs = new double[size][];

        outputs[0] = getLayer(0).computeOutput(input);
        for (int i = 1; i < size; i++) {
            outputs[i] = getLayer(i).computeOutput(outputs[i - 1]);
        }

        // вычислим ошибку выходного слоя
        Layer layer = getLayer(size - 1);
        final int layerSize = layer.getSize();
        double[] error = new double[layerSize];
        double totalError = 0;

        for (int i = 0; i < layerSize; i++) {
            error[i] = goal[i] - outputs[size - 1][i];
            totalError += Math.abs(error[i]);
        }

        // обновим выходной слой
        if (layer instanceof BackpropLayer) {
            ((BackpropLayer) layer).adjust(size == 1 ? input : outputs[size - 2], error, rate);
        }

        // идем по скрытым слоям
        double[] prevError = error;
        Layer prevLayer = layer;

        for (int i = size - 2; i >= 0; i--, prevError = error, prevLayer = layer) {
            // получим очередной слой
            layer = getLayer(i);
            // вычислим для него ошибку
            if (prevLayer instanceof BackpropLayer) {
                error = ((BackpropLayer) prevLayer).computeBackwardError(outputs[i], prevError);
            } else {
                error = prevError;
            }
            // обновим слой
            if (layer instanceof BackpropLayer) {
                ((BackpropLayer) layer).adjust(i == 0 ? input : outputs[i - 1], error, rate);
            }
        }

        // вернем суммарную ошибку
        return totalError;
    }
}
