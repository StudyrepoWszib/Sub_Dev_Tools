package org.example.demo;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

import java.text.DecimalFormat;

public class JeneticsExample {

    // Parametry
    private static int rectangleCount;
    private static int cylinderCount;
    private static int sphereCount;
    private static int targetVolume;
    private static int maxGenerations;
    private static double mutationRate;
    private static double crossoverRate;

    private static int rectangleMinWidth;
    private static int rectangleMaxWidth;
    private static int rectangleMinHeight;
    private static int rectangleMaxHeight;
    private static int rectangleMinLength;
    private static int rectangleMaxLength;

    private static int cylinderMinRadius;
    private static int cylinderMaxRadius;
    private static int cylinderMinHeight;
    private static int cylinderMaxHeight;

    private static int sphereMinRadius;
    private static int sphereMaxRadius;


    public static void setParameters(int rectCount, int cylCount, int sphCount,
                                     int targetVol, int maxGen,
                                     double mutRate, double crossRate,
                                     int rectMinWidth, int rectMaxWidth,
                                     int rectMinHeight, int rectMaxHeight,
                                     int rectMinLength, int rectMaxLength,
                                     int cylMinRadius, int cylMaxRadius,
                                     int cylMinHeight, int cylMaxHeight,
                                     int sphMinRadius, int sphMaxRadius) {
        rectangleCount = rectCount;
        cylinderCount = cylCount;
        sphereCount = sphCount;
        targetVolume = targetVol;
        maxGenerations = maxGen;
        mutationRate = mutRate;
        crossoverRate = crossRate;

        rectangleMinWidth = rectMinWidth;
        rectangleMaxWidth = rectMaxWidth;
        rectangleMinHeight = rectMinHeight;
        rectangleMaxHeight = rectMaxHeight;
        rectangleMinLength = rectMinLength;
        rectangleMaxLength = rectMaxLength;

        cylinderMinRadius = cylMinRadius;
        cylinderMaxRadius = cylMaxRadius;
        cylinderMinHeight = cylMinHeight;
        cylinderMaxHeight = cylMaxHeight;

        sphereMinRadius = sphMinRadius;
        sphereMaxRadius = sphMaxRadius;
    }

    // Tworzenie obiektu prostokąta
    private static Rectangle createRectangle(Genotype<IntegerGene> genotype) {
        double width = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        double length = genotype.get(2).get(0).intValue();
        return new Rectangle(width, height, length);
    }

    // Tworzenie obiektu walca
    private static Cylinder createCylinder(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        return new Cylinder(radius, height);
    }

    // Tworzenie obiektu kuli
    private static Sphere createSphere(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        return new Sphere(radius);
    }

    // Funkcja oceny (fitness) dla prostokątów
    private static double fitnessRectangle(Genotype<IntegerGene> genotype) {
        return createRectangle(genotype).calculateVolume();
    }

    // Funkcja oceny (fitness) dla walców
    private static double fitnessCylinder(Genotype<IntegerGene> genotype) {
        return createCylinder(genotype).calculateVolume();
    }

    // Funkcja oceny (fitness) dla kul
    private static double fitnessSphere(Genotype<IntegerGene> genotype) {
        return createSphere(genotype).calculateVolume();
    }

    public static String run() {
        // Generowanie fabryk genotypów dla każdej figury
        Factory<Genotype<IntegerGene>> rectangleFactory = Genotype.of(
                IntegerChromosome.of(rectangleMinWidth, rectangleMaxWidth),  // Szerokość
                IntegerChromosome.of(rectangleMinHeight, rectangleMaxHeight), // Wysokość
                IntegerChromosome.of(rectangleMinLength, rectangleMaxLength)  // Długość
        );

        Factory<Genotype<IntegerGene>> cylinderFactory = Genotype.of(
                IntegerChromosome.of(cylinderMinRadius, cylinderMaxRadius), // Promień
                IntegerChromosome.of(cylinderMinHeight, cylinderMaxHeight)  // Wysokość
        );

        Factory<Genotype<IntegerGene>> sphereFactory = Genotype.of(
                IntegerChromosome.of(sphereMinRadius, sphereMaxRadius)  // Promień
        );

        // Tworzenie silników ewolucyjnych
        Engine<IntegerGene, Double> rectangleEngine = Engine
                .builder(JeneticsExample::fitnessRectangle, rectangleFactory)
                .populationSize(rectangleCount)
                .alterers(
                        new Mutator<>(mutationRate),
                        new SinglePointCrossover<>(crossoverRate)
                )
                .build();

        Engine<IntegerGene, Double> cylinderEngine = Engine
                .builder(JeneticsExample::fitnessCylinder, cylinderFactory)
                .populationSize(cylinderCount)
                .alterers(
                        new Mutator<>(mutationRate),
                        new SinglePointCrossover<>(crossoverRate)
                )
                .build();

        Engine<IntegerGene, Double> sphereEngine = Engine
                .builder(JeneticsExample::fitnessSphere, sphereFactory)
                .populationSize(sphereCount)
                .alterers(
                        new Mutator<>(mutationRate),
                        new SinglePointCrossover<>(crossoverRate)
                )
                .build();

        Phenotype<IntegerGene, Double> bestRectangle = null;
        Phenotype<IntegerGene, Double> bestCylinder = null;
        Phenotype<IntegerGene, Double> bestSphere = null;
        double bestRectangleVolume = 0;
        double bestCylinderVolume = 0;
        double bestSphereVolume = 0;

        // Ewolucja przez wiele pokoleń
        for (int generation = 1; generation <= maxGenerations; generation++) {
            EvolutionResult<IntegerGene, Double> rectangleResult = rectangleEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            EvolutionResult<IntegerGene, Double> cylinderResult = cylinderEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            EvolutionResult<IntegerGene, Double> sphereResult = sphereEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            Phenotype<IntegerGene, Double> currentBestRectangle = rectangleResult.bestPhenotype();
            Phenotype<IntegerGene, Double> currentBestCylinder = cylinderResult.bestPhenotype();
            Phenotype<IntegerGene, Double> currentBestSphere = sphereResult.bestPhenotype();

            double currentRectangleVolume = currentBestRectangle.fitness();
            double currentCylinderVolume = currentBestCylinder.fitness();
            double currentSphereVolume = currentBestSphere.fitness();

            // Aktualizacja najlepszych wartości
            if (currentRectangleVolume > bestRectangleVolume) {
                bestRectangle = currentBestRectangle;
                bestRectangleVolume = currentRectangleVolume;
            }

            if (currentCylinderVolume > bestCylinderVolume) {
                bestCylinder = currentBestCylinder;
                bestCylinderVolume = currentCylinderVolume;
            }

            if (currentSphereVolume > bestSphereVolume) {
                bestSphere = currentBestSphere;
                bestSphereVolume = currentSphereVolume;
            }

            // Całkowita objętość populacji
            double totalVolume = rectangleCount * bestRectangleVolume +
                    cylinderCount * bestCylinderVolume +
                    sphereCount * bestSphereVolume;

            if (totalVolume >= targetVolume) {
                break; // Zatrzymanie ewolucji, jeśli osiągnięto docelową objętość
            }
        }

        // Tworzenie obiektów reprezentujących najlepsze wyniki
        Rectangle bestRectangleObject = createRectangle(bestRectangle.genotype());
        Cylinder bestCylinderObject = createCylinder(bestCylinder.genotype());
        Sphere bestSphereObject = createSphere(bestSphere.genotype());
        DecimalFormat df = new DecimalFormat("#.00");
        // Końcowa objętość
        double finalVolume = rectangleCount * bestRectangleVolume +
                cylinderCount * bestCylinderVolume +
                sphereCount * bestSphereVolume;

        // Sformatowanie wyników jako ciąg znaków
        StringBuilder results = new StringBuilder();
        results.append("Końcowe wyniki:\n");
        results.append("Najlepszy prostokąt:\n")
                .append("Szerokość=").append(bestRectangleObject.getWidth())
                .append("\nWysokość=").append(bestRectangleObject.getHeight())
                .append("\nDługość=").append(bestRectangleObject.getLength())
                .append("\nObjętość=").append(df.format(bestRectangleVolume)).append("\n");
        results.append("Najlepszy walec:\n")
                .append("\nPromień=").append(bestCylinderObject.getRadius())
                .append("\nWysokość=").append(bestCylinderObject.getHeight())
                .append("\nObjętość=").append(df.format(bestCylinderVolume)).append("\n");
        results.append("Najlepsza kula:\n")
                .append("\nPromień=").append(bestSphereObject.getRadius())
                .append("\nObjętość=").append(df.format(bestSphereVolume)).append("\n");
        results.append("\nCałkowita objętość populacji: ").append(df.format(finalVolume)).append("\n");
        results.append("Docelowa objętość: ").append(targetVolume);

        return results.toString();
    }

}
