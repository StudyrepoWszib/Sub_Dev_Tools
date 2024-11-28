package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo.JeneticsExample;

public class MainController {

    // Поля для ввода параметров
    @FXML private TextField txtRectangleMinWidth;
    @FXML private TextField txtRectangleMaxWidth;
    @FXML private TextField txtRectangleMinHeight;
    @FXML private TextField txtRectangleMaxHeight;
    @FXML private TextField txtRectangleMinLength;
    @FXML private TextField txtRectangleMaxLength;

    @FXML private TextField txtCylinderMinRadius;
    @FXML private TextField txtCylinderMaxRadius;
    @FXML private TextField txtCylinderMinHeight;
    @FXML private TextField txtCylinderMaxHeight;

    @FXML private TextField txtSphereMinRadius;
    @FXML private TextField txtSphereMaxRadius;

    @FXML private TextField txtRectangleCount;
    @FXML private TextField txtCylinderCount;
    @FXML private TextField txtSphereCount;
    @FXML private TextField txtTargetVolume;
    @FXML private TextField txtMaxGenerations;
    @FXML private TextField txtMutationRate;
    @FXML private TextField txtCrossoverRate;

    // Поля для вывода результатов
    @FXML private TextArea txtResults;

    @FXML private Button btnRun;

    @FXML
    public void initialize() {
        btnRun.setOnAction(event -> runAlgorithm());
    }

    @FXML
    private void runAlgorithm() {
        try {
            // Получаем параметры из полей
            int rectangleMinWidth = Integer.parseInt(txtRectangleMinWidth.getText());
            int rectangleMaxWidth = Integer.parseInt(txtRectangleMaxWidth.getText());
            int rectangleMinHeight = Integer.parseInt(txtRectangleMinHeight.getText());
            int rectangleMaxHeight = Integer.parseInt(txtRectangleMaxHeight.getText());
            int rectangleMinLength = Integer.parseInt(txtRectangleMinLength.getText());
            int rectangleMaxLength = Integer.parseInt(txtRectangleMaxLength.getText());

            int cylinderMinRadius = Integer.parseInt(txtCylinderMinRadius.getText());
            int cylinderMaxRadius = Integer.parseInt(txtCylinderMaxRadius.getText());
            int cylinderMinHeight = Integer.parseInt(txtCylinderMinHeight.getText());
            int cylinderMaxHeight = Integer.parseInt(txtCylinderMaxHeight.getText());

            int sphereMinRadius = Integer.parseInt(txtSphereMinRadius.getText());
            int sphereMaxRadius = Integer.parseInt(txtSphereMaxRadius.getText());

            int rectangleCount = Integer.parseInt(txtRectangleCount.getText());
            int cylinderCount = Integer.parseInt(txtCylinderCount.getText());
            int sphereCount = Integer.parseInt(txtSphereCount.getText());
            int targetVolume = Integer.parseInt(txtTargetVolume.getText());
            int maxGenerations = Integer.parseInt(txtMaxGenerations.getText());
            double mutationRate = Double.parseDouble(txtMutationRate.getText());
            double crossoverRate = Double.parseDouble(txtCrossoverRate.getText());

            // Устанавливаем параметры в статические поля JeneticsExample
            JeneticsExample.setParameters(
                    rectangleCount, cylinderCount, sphereCount,
                    targetVolume, maxGenerations, mutationRate, crossoverRate,
                    rectangleMinWidth, rectangleMaxWidth,
                    rectangleMinHeight, rectangleMaxHeight,
                    rectangleMinLength, rectangleMaxLength,
                    cylinderMinRadius, cylinderMaxRadius,
                    cylinderMinHeight, cylinderMaxHeight,
                    sphereMinRadius, sphereMaxRadius
            );

            // Запуск алгоритма
            String results = JeneticsExample.run();

            // Выводим результаты в текстовое поле
            txtResults.setText(results);

        } catch (Exception e) {
            txtResults.setText("Ошибка: " + e.getMessage());
        }
    }
}
