package edu.lab2.neuronnetwork.model.networks;

import edu.lab2.neuronnetwork.model.layer.Layer;
import edu.lab2.neuronnetwork.model.util.Checkers;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Евгений
 */
public class Network implements Serializable{
    /**
     * Слои
     */
    private Layer[] layers;
    /**
     * Конструирует нейронную сеть с заданными слоями
     * @param layers Нейронные слои
     */
    public Network(Layer[] layers) {
        // проверки
        if (layers == null) throw new IllegalArgumentException("Input layers array is null");
        if (layers.length == 0) throw new IllegalArgumentException("No elements in input layers array");
        // проверим детально
        final int size = layers[0] == null ? 0 : layers.length;
        for (int i = 0; i < size; i++)
            if (layers[i] == null || (i > 1 && layers[i].getInputSize() != layers[i - 1].getSize()))
                throw new IllegalArgumentException("Input layers are not correct");
        // запомним слои
        this.layers = layers;
    }
    
    /**
     * Получает размер входного вектора
     * @return Размер входного вектора
     */
    public final int getInputSize() {
        return layers[0].getInputSize();
    }

    /**
     * Получает размер выходного вектора
     * @return Размер выходного вектора
     */
    public final int getOutputSize() {
        return layers[layers.length - 1].getSize();
    }

    /**
     * Получает размер сети
     * @return Размер сети
     */
    public final int getSize() {
        return layers.length;
    }

    /**
     * Получает нейронный слой по индексу
     * @param index Индекс слоя
     * @return Нейронный слой
     */
    public final Layer getLayer(int index) {
        return layers[index];
    }
    
     /**
     * Вычисляет отклик сети
     * @param input Входной вектор
     * @return Выходной вектор
     */
    public double[] computeOutput(double[] input) {
        // проверки
        Checkers.checkArray(input, "input", getInputSize());

        // вычислим выходной отклик сети
        double[] output = input;
        final int size = layers.length;
        for (int i = 0; i < size; i++)
            output = layers[i].computeOutput(output);

        // вернем выход
        return output;
    }
    
    /**
     * Сохраняет нейронну сеть в файл
     * @param fileName Имя файла
     */
    public void saveToFile(String fileName) {
        // проверки
        if (fileName == null) 
            throw new IllegalArgumentException("File name is null");

        // сохраняем
        try {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
                outputStream.writeObject(this);
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Загружает нейронную сеть из файла
     * @param fileName Имя файла
     * @return Нейронную сеть
     */
    public static Network loadFromFile(String fileName) {
        // проверки
        if (fileName == null) 
            throw new IllegalArgumentException("File name is null");

        // загружаем
        Object network = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            network = inputStream.readObject();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        // отдадим сеть
        return (Network)network;
    }
}
