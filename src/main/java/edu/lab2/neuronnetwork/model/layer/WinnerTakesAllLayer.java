package edu.lab2.neuronnetwork.model.layer;

import edu.lab2.neuronnetwork.model.util.Checkers;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
@Getter
@Setter
public class WinnerTakesAllLayer implements BackpropLayer {

    private final int size;
    private double minLevel;

    public WinnerTakesAllLayer(int size, double minLevel) {
        this.size = size;
        this.minLevel = minLevel;
    }

    @Override
    public void randomize(double min, double max) {
        
    }
    
    @Override
    public double[] computeBackwardError(double[] input, double[] error) {
        Checkers.checkArray(input, "input", size);
        Checkers.checkArray(error, "error", size);
        double[] output = computeOutput(input);
        
        double[] backwardError = IntStream.range(0, size)
                .mapToDouble(i -> error[i] + output[i] - input[i])
                .toArray();
        return backwardError;
    }

    @Override
    public void adjust(double[] input, double[] error, double rate) {
    }

    @Override
    public int getInputSize() {
        return size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public double[] computeOutput(double[] input) {
        Checkers.checkArray(input, "input", size);
        int winner = IntStream.range(0, input.length)
                .reduce((i, j) -> input[i] > input[j] ? i : j)
                .getAsInt();
        double[] output = new double[size];
        if (minLevel > 0) {
            double level = IntStream.range(0, input.length)
                    .filter(i -> i != winner)
                    .mapToDouble(i -> Math.abs(input[i] - input[winner]))
                    .min()
                    .getAsDouble();
            if (level < minLevel) {
                return output;
            }
        }
        output[winner] = 1;
        return output;
    }

}
