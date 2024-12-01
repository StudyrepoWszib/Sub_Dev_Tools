package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private TextField txtRectangleCount, txtRectangleMinWidth, txtRectangleMaxWidth, txtRectangleMinHeight,
            txtRectangleMaxHeight, txtRectangleMinLength, txtRectangleMaxLength;
    @FXML
    private TextField txtCylinderCount, txtCylinderMinRadius, txtCylinderMaxRadius, txtCylinderMinHeight, txtCylinderMaxHeight;
    @FXML
    private TextField txtSphereCount, txtSphereMinRadius, txtSphereMaxRadius;
    @FXML
    private TextField txtTargetVolume, txtMaxGenerations, txtMutationRate, txtCrossoverRate;
    @FXML
    private TextArea txtResults;
    @FXML
    private Button btnRun;

    @FXML
    private void initialize() {
        btnRun.setOnAction(event -> runAlgorithm());
    }

    @FXML
    private void runAlgorithm() {
        try {
            // Pobieranie danych wejściowych
            int rectangleCount = Integer.parseInt(txtRectangleCount.getText());
            int rectangleMinWidth = Integer.parseInt(txtRectangleMinWidth.getText());
            int rectangleMaxWidth = Integer.parseInt(txtRectangleMaxWidth.getText());
            int rectangleMinHeight = Integer.parseInt(txtRectangleMinHeight.getText());
            int rectangleMaxHeight = Integer.parseInt(txtRectangleMaxHeight.getText());
            int rectangleMinLength = Integer.parseInt(txtRectangleMinLength.getText());
            int rectangleMaxLength = Integer.parseInt(txtRectangleMaxLength.getText());

            int cylinderCount = Integer.parseInt(txtCylinderCount.getText());
            int cylinderMinRadius = Integer.parseInt(txtCylinderMinRadius.getText());
            int cylinderMaxRadius = Integer.parseInt(txtCylinderMaxRadius.getText());
            int cylinderMinHeight = Integer.parseInt(txtCylinderMinHeight.getText());
            int cylinderMaxHeight = Integer.parseInt(txtCylinderMaxHeight.getText());

            int sphereCount = Integer.parseInt(txtSphereCount.getText());
            int sphereMinRadius = Integer.parseInt(txtSphereMinRadius.getText());
            int sphereMaxRadius = Integer.parseInt(txtSphereMaxRadius.getText());

            int targetVolume = Integer.parseInt(txtTargetVolume.getText());
            int maxGenerations = Integer.parseInt(txtMaxGenerations.getText());

            // Przeliczanie procentów na zakres [0, 1]
            double mutationRate = Double.parseDouble(txtMutationRate.getText()) / 100.0;
            double crossoverRate = Double.parseDouble(txtCrossoverRate.getText()) / 100.0;

            // Sprawdzenie zakresu prawdopodobieństw
            if (mutationRate < 0 || mutationRate > 1 || crossoverRate < 0 || crossoverRate > 1) {
                throw new IllegalArgumentException("Prawdopodobieństwo mutacji i krzyżowania musi być w zakresie [0, 100].");
            }

            // Ustawianie parametrów
            JeneticsExample.setParameters(
                    rectangleCount, cylinderCount, sphereCount,
                    targetVolume, maxGenerations, mutationRate, crossoverRate,
                    rectangleMinWidth, rectangleMaxWidth, rectangleMinHeight,
                    rectangleMaxHeight, rectangleMinLength, rectangleMaxLength,
                    cylinderMinRadius, cylinderMaxRadius, cylinderMinHeight,
                    cylinderMaxHeight, sphereMinRadius, sphereMaxRadius
            );

            // Uruchamianie algorytmu i wyświetlanie wyników
            String results = JeneticsExample.run();
            txtResults.setText(results);

        } catch (NumberFormatException e) {
            txtResults.setText("Błąd: Nieprawidłowy format danych. Upewnij się, że pola zawierają poprawne liczby.");
        } catch (IllegalArgumentException e) {
            txtResults.setText("Błąd: " + e.getMessage());
        } catch (Exception e) {
            txtResults.setText("Błąd: " + e.getMessage());
        }
    }
}