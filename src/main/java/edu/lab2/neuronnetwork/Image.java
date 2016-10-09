package edu.lab2.neuronnetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
@Getter
@Setter
public class Image {

    private double[] input;
    private int answer;
    private int result;

    private Image(int n) {
        input = new double[n];
    }
    
    public static List<Image> readImages(String fileName, int numberOfImages, int sizeOfInput) throws Exception{
        Inputer in = new Inputer(fileName);
        List<Image> images = new ArrayList<>(numberOfImages);
        for (int i = 0; i < numberOfImages; i++) {
            Image image = new Image(sizeOfInput);
            for (int j = 0; j < sizeOfInput; j++) {
                image.input[j] = in.nextInt();
                image.answer = in.nextInt() - 1;
            }
            images.add(image);
        }
        return images;
    }

    private static class Inputer {

        BufferedReader in;
        StringTokenizer st = new StringTokenizer("");

        public Inputer(String fileName) throws IOException {
            in = new BufferedReader(new FileReader(fileName));
            
        }

        public String nextToken() throws Exception {
            while (!st.hasMoreTokens()) {
                st = new StringTokenizer(in.readLine());
            }
            return st.nextToken();
        }

        public Integer nextInt() throws Exception {
            return Integer.parseInt(nextToken());
        }

    }

}
