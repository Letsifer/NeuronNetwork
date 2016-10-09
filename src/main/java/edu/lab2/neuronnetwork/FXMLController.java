package edu.lab2.neuronnetwork;

import edu.lab2.neuronnetwork.model.layer.Layer;
import edu.lab2.neuronnetwork.model.layer.SigmoidLayer;
import edu.lab2.neuronnetwork.model.layer.WinnerTakesAllLayer;
import edu.lab2.neuronnetwork.model.networks.BackpropNetwork;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController implements Initializable {

    private final String imagesFileName = "src//main//resources//images.txt";
    private final double MAX_MISTAKE_BORDER = 0.1;
    private final int sizeX = 6, sizeY = 6;
    private final int outputNumber = 3;
    private final int imagesAtStyde = 10;
    private List<Image> images;

    private int currentEpoch;
    private BackpropNetwork network;
    private int mustBeRight;

    @FXML
    private LineChart<Integer, Double> graphic;

    private XYChart.Series<Integer, Double> errorLevel = new XYChart.Series<>();

    @FXML
    private Button oneEpoch;

    @FXML
    private Button manyEpoches;

    @FXML
    private Label sizeLabel;
    private final String sizeMessage = "Размер: " + sizeY + " " + sizeX;

    @FXML
    private Label currentEpochLabel;
    private String currentEpochMessage = "Текущая эпоха: ";

    @FXML
    private Label resultLabel;
    private final String educated = "Система обучена! :)",
            nonEducated = "Система не обучена. :(";

    @FXML
    private Label errorBorderLabel;
    private final String errorBorderMessage = "Уровень ошибки: " + MAX_MISTAKE_BORDER;

    @FXML
    private TextArea epochesResultArea;

    private void outputConstantInfo() {
        sizeLabel.setText(sizeMessage);
        errorBorderLabel.setText(errorBorderMessage);
        resultLabel.setText(nonEducated);
    }

    private void outputVariableInfo() {
        currentEpochLabel.setText(currentEpochMessage + currentEpoch);
        StringBuilder stB = new StringBuilder();
        for (int i = 0; i < imagesAtStyde; i++) {
            stB
                    .append(i + 1)
                    .append(") program: ")
                    .append(images.get(i).getResult())
                    .append(", answer: ")
                    .append(images.get(i).getAnswer())
                    .append("\n");
        }
        epochesResultArea.setText(stB.toString());
    }

    private int oneEpoch() {
        int currentRight = 0;
        for (int i = 0; i < imagesAtStyde; i++) {
            double[] image = images.get(i).getInput();
            int result = computeAnswer(network.computeOutput(image));
            int answer = images.get(i).getAnswer();
            images.get(i).setResult(result);
            if (result == answer) {
                currentRight++;
            } else {
                double[] goal = new double[outputNumber];
                goal[answer] = 1;
                network.learnPattern(image, goal, 1);
            }
        }
        return currentRight;
    }

    @FXML
    private void oneEpochClick() {
        currentEpoch++;
        int currentRight = oneEpoch();
        checkEndOfStudy(currentRight);
    }

    @FXML
    private void manyEpochesClick() {
        final int steps = 100;
        for (int i = 0; i < steps; i++) {
            currentEpoch++;
            int currentRight = oneEpoch();
            boolean isEnd = checkEndOfStudy(currentRight);
            if (isEnd) {
                break;
            }
        }
    }

    private boolean checkRight(int currentRight) {
        return currentRight >= mustBeRight;
    }

    private boolean checkEndOfStudy(int currentRight) {
        errorLevel.getData().add(
                new XYChart.Data<>(currentEpoch, (double) currentRight / imagesAtStyde)
        );
        outputVariableInfo();
        if (checkRight(currentRight)) {
            oneEpoch.setDisable(true);
            manyEpoches.setDisable(true);
            resultLabel.setText(educated);
            return true;
        }
        return false;
    }

    private static final Double ZERO = 0.0;
    private int computeAnswer(double[] answers) {
        double value = Double.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] > value) {
                value = answers[i];
                index = i;
            }
        }
        if (Double.compare(value, ZERO) == 0) {
            return index;
        }
        return -1;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //открыть файл обучения, считать оттуда данные
        //обучение
        /*
            count_images = ...
            epoch_num = 0
            для каждой эпохи
            count = 0
            1) для каждого изображения
                а) считать изображение
                б) выяснить, к какой группе оно принадлежит
                в) проверка  - получить суммы на выходах, и максимальная и будет тем элементом
                   1. если принадлежность совпала с ответом то count++
                   2. иначе править массив весов - изменит вес 
                    
            2) вывести на график точку (epoch_num, count/count_images)
            3) if (count/count_images < border) -> закочнить обучение
        
         */

 /*
            нормальная работа
            подается на вход символ в изображении - ответить, к какому классу относится символ
         */
        try {
            outputConstantInfo();
            graphic.getData().add(errorLevel);
            Layer[] layers = createLayers();
            network = new BackpropNetwork(layers);
            images = Image.readImages(imagesFileName, imagesAtStyde, sizeX * sizeY);
            mustBeRight = (int) (imagesAtStyde * (1.0 - MAX_MISTAKE_BORDER));
            currentEpoch = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Layer[] createLayers() {
        final int sizeOfMiddleLayer = sizeX * sizeY * 2;
        Layer first = new SigmoidLayer(sizeOfMiddleLayer, outputNumber);
        Layer last = new WinnerTakesAllLayer(outputNumber, 0);

        return new Layer[]{first, last};
    }
}
