package org.example.demo;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

public class JeneticsExample {

    // Параметры
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

    // Tworzenie obiektu cylindra
    private static Cylinder createCylinder(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        return new Cylinder(radius, height);
    }

    // Tworzenie obiektu sfery
    private static Sphere createSphere(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        return new Sphere(radius);
    }

    // Funkcja fitness dla prostokątów
    private static double fitnessRectangle(Genotype<IntegerGene> genotype) {
        return createRectangle(genotype).calculateVolume();
    }

    // Funkcja fitness dla cylindrów
    private static double fitnessCylinder(Genotype<IntegerGene> genotype) {
        return createCylinder(genotype).calculateVolume();
    }

    // Funkcja fitness dla sfer
    private static double fitnessSphere(Genotype<IntegerGene> genotype) {
        return createSphere(genotype).calculateVolume();
    }


    public static String run() {
        // Генерация фабрик генотипов для каждой фигуры
        Factory<Genotype<IntegerGene>> rectangleFactory = Genotype.of(
                IntegerChromosome.of(rectangleMinWidth, rectangleMaxWidth),  // Ширина
                IntegerChromosome.of(rectangleMinHeight, rectangleMaxHeight), // Высота
                IntegerChromosome.of(rectangleMinLength, rectangleMaxLength)  // Длина
        );

        Factory<Genotype<IntegerGene>> cylinderFactory = Genotype.of(
                IntegerChromosome.of(cylinderMinRadius, cylinderMaxRadius), // Радиус
                IntegerChromosome.of(cylinderMinHeight, cylinderMaxHeight)  // Высота
        );

        Factory<Genotype<IntegerGene>> sphereFactory = Genotype.of(
                IntegerChromosome.of(sphereMinRadius, sphereMaxRadius)  // Радиус
        );

        // Создание эволюционных движков
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

        // Эволюция на протяжении нескольких поколений
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

            // Обновляем лучшие значения
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

            // Суммарный объем популяции
            double totalVolume = rectangleCount * bestRectangleVolume +
                    cylinderCount * bestCylinderVolume +
                    sphereCount * bestSphereVolume;

            if (totalVolume >= targetVolume) {
                break; // Завершаем эволюцию, если достигнут целевой объем
            }
        }

        // Создаем объекты для представления лучших результатов
        Rectangle bestRectangleObject = createRectangle(bestRectangle.genotype());
        Cylinder bestCylinderObject = createCylinder(bestCylinder.genotype());
        Sphere bestSphereObject = createSphere(bestSphere.genotype());

        // Итоговый объем
        double finalVolume = rectangleCount * bestRectangleVolume +
                cylinderCount * bestCylinderVolume +
                sphereCount * bestSphereVolume;

        // Формируем результат в виде строки
        StringBuilder results = new StringBuilder();
        results.append("Итоговые результаты:\n");
        results.append("Лучший прямоугольник: ")
                .append("Ширина=").append(bestRectangleObject.getWidth())
                .append(", Высота=").append(bestRectangleObject.getHeight())
                .append(", Длина=").append(bestRectangleObject.getLength())
                .append(", Объем=").append(bestRectangleVolume).append("\n");
        results.append("Лучший цилиндр: ")
                .append("Радиус=").append(bestCylinderObject.getRadius())
                .append(", Высота=").append(bestCylinderObject.getHeight())
                .append(", Объем=").append(bestCylinderVolume).append("\n");
        results.append("Лучшая сфера: ")
                .append("Радиус=").append(bestSphereObject.getRadius())
                .append(", Объем=").append(bestSphereVolume).append("\n");
        results.append("Общий объем популяции: ").append(finalVolume).append("\n");
        results.append("Целевой объем: ").append(targetVolume);

        return results.toString();
    }

}
