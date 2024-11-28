package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class Controller {

    @FXML
    private TextField txtTargetVolume;

    @FXML
    private TextField txtPopulationSize;

    @FXML
    private TextField txtMutationRate;

    @FXML
    private TextField txtCrossoverRate;

    @FXML
    private Button btnRunAlgorithm;

    @FXML
    private TextArea txtResults;

    @FXML
    private Pane visualizationPane;

    @FXML
    public void initialize() {
        // Инициализация контроллера, если это необходимо
    }

    @FXML
    private void runAlgorithm() {
        try {
            // Получение параметров
            int targetVolume = Integer.parseInt(txtTargetVolume.getText());
            int populationSize = Integer.parseInt(txtPopulationSize.getText());
            double mutationRate = Double.parseDouble(txtMutationRate.getText());
            double crossoverRate = Double.parseDouble(txtCrossoverRate.getText());

            // Вызов алгоритма (здесь подключаем Jenetics)
            String result = runJeneticsAlgorithm(targetVolume, populationSize, mutationRate, crossoverRate);

            // Отображение результата
            txtResults.setText(result);

            // Обновление визуализации
            updateVisualization();
        } catch (NumberFormatException e) {
            txtResults.setText("Wprowadzono nieprawidłowe dane. Sprawdź parametry wejściowe.");
        }
    }

    private String runJeneticsAlgorithm(int targetVolume, int populationSize, double mutationRate, double crossoverRate) {
        // TODO: Подключите реализацию Jenetics
        return "Algorytm uruchomiony!\nDocelowy Objętość: " + targetVolume +
                "\nRozmiar Populacji: " + populationSize +
                "\nMutacja: " + mutationRate +
                "\nKrzyżowanie: " + crossoverRate;
    }

    private void updateVisualization() {
        // Очистка панели визуализации
        visualizationPane.getChildren().clear();

        // Добавьте сюда визуализацию фигур (сфера, цилиндр, прямоугольник)
        // Пример:
        javafx.scene.shape.Sphere sphere = new javafx.scene.shape.Sphere(50); // Радиус сферы
        sphere.setTranslateX(100);
        sphere.setTranslateY(100);
        visualizationPane.getChildren().add(sphere);
    }
}
