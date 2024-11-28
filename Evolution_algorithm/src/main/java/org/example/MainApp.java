package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Konfigurator Algorytmu Genetycznego");

        // Создаем элементы интерфейса
        Label lblTargetVolume = new Label("Docelowy Objętość:");
        TextField txtTargetVolume = new TextField("500000");

        Label lblPopulationSize = new Label("Rozmiar Populacji:");
        TextField txtPopulationSize = new TextField("43");

        Label lblMutationRate = new Label("Prawdopodobieństwo Mutacji:");
        TextField txtMutationRate = new TextField("0.1");

        Label lblCrossoverRate = new Label("Prawdopodobieństwo Krzyżowania:");
        TextField txtCrossoverRate = new TextField("0.6");

        Button btnRun = new Button("Uruchom Algorytm");

        // Контейнер для формы
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(lblTargetVolume, 0, 0);
        grid.add(txtTargetVolume, 1, 0);
        grid.add(lblPopulationSize, 0, 1);
        grid.add(txtPopulationSize, 1, 1);
        grid.add(lblMutationRate, 0, 2);
        grid.add(txtMutationRate, 1, 2);
        grid.add(lblCrossoverRate, 0, 3);
        grid.add(txtCrossoverRate, 1, 3);
        grid.add(btnRun, 0, 4, 2, 1);

        // Вывод результатов
        TextArea txtResults = new TextArea();
        txtResults.setPrefHeight(300);

        // Контейнер для всего окна
        VBox root = new VBox(10, grid, txtResults);
        root.setPadding(new Insets(10));

        // Обработчик кнопки запуска
        btnRun.setOnAction(e -> {
            // Получаем параметры
            int targetVolume = Integer.parseInt(txtTargetVolume.getText());
            int populationSize = Integer.parseInt(txtPopulationSize.getText());
            double mutationRate = Double.parseDouble(txtMutationRate.getText());
            double crossoverRate = Double.parseDouble(txtCrossoverRate.getText());

            // Запускаем алгоритм (обрабатываем результаты)
            String result = runAlgorithm(targetVolume, populationSize, mutationRate, crossoverRate);
            txtResults.setText(result);
        });

        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    // Метод для запуска алгоритма
    private String runAlgorithm(int targetVolume, int populationSize, double mutationRate, double crossoverRate) {
        // TODO: Добавить вызов алгоритма Jenetics и вернуть результат как строку
        return "Algorytm uruchomiony!\nDocelowy Objętość: " + targetVolume +
                "\nRozmiar Populacji: " + populationSize +
                "\nMutacja: " + mutationRate +
                "\nKrzyżowanie: " + crossoverRate;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
