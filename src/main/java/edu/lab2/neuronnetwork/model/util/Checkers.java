package edu.lab2.neuronnetwork.model.util;

/**
 *
 * @author Евгений
 */
public class Checkers {
    
    public static void checkArray(double[] array, String name, int size) {
        if (array == null) {
            throw new NullPointerException(name + " vector is null");
        }
        if (array.length != size) {
            throw new IllegalArgumentException("Size of " + name + " vector doesn't equals the size of level");
        }
    }
}
