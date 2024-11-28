package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

public class JeneticsExample {

    // Stałe dla maksymalnego objętości i liczby figur
    private static final int MAX_TOTAL_VOLUME = 500000; // Maksymalna docelowa objętość
    private static final int RECTANGLE_COUNT = 43;      // Liczba prostokątów
    private static final int CYLINDER_COUNT = 43;       // Liczba cylindrów
    private static final int SPHERE_COUNT = 43;         // Liczba sfer

    // Docelowa objętość (95% maksymalnej objętości)
    private static final double TARGET_VOLUME = MAX_TOTAL_VOLUME * 0.95;

    // Parametry mutacji i krzyżowania
    private static final double MUTATION_RATE = 0.1;  // Prawdopodobieństwo mutacji
    private static final double CROSSOVER_RATE = 0.6; // Prawdopodobieństwo krzyżowania

    // Zakresy wymiarów dla prostokątów
    private static final int RECTANGLE_MIN_DIM = 5;
    private static final int RECTANGLE_MAX_WIDTH = 10;
    private static final int RECTANGLE_MAX_HEIGHT = 25;
    private static final int RECTANGLE_MAX_LENGTH = 25;

    // Zakresy wymiarów dla cylindrów
    private static final int CYLINDER_MIN_RADIUS = 5;
    private static final int CYLINDER_MAX_RADIUS = 10;
    private static final int CYLINDER_MIN_HEIGHT = 5;
    private static final int CYLINDER_MAX_HEIGHT = 25;

    // Zakresy wymiarów dla sfer
    private static final int SPHERE_MIN_RADIUS = 5;
    private static final int SPHERE_MAX_RADIUS = 10;

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


    public static void main(String[] args) {
        // Fabryka genotypów dla prostokątów
        Factory<Genotype<IntegerGene>> rectangleFactory = Genotype.of(
                IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_WIDTH),  // Szerokość
                IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_HEIGHT), // Wysokość
                IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_LENGTH)  // Długość
        );

        // Fabryka genotypów dla cylindrów
        Factory<Genotype<IntegerGene>> cylinderFactory = Genotype.of(
                IntegerChromosome.of(CYLINDER_MIN_RADIUS, CYLINDER_MAX_RADIUS), // Promień
                IntegerChromosome.of(CYLINDER_MIN_HEIGHT, CYLINDER_MAX_HEIGHT)  // Wysokość
        );

        // Fabryka genotypów dla sfer
        Factory<Genotype<IntegerGene>> sphereFactory = Genotype.of(
                IntegerChromosome.of(SPHERE_MIN_RADIUS, SPHERE_MAX_RADIUS)  // Promień
        );

        // Silnik ewolucyjny dla prostokątów
        Engine<IntegerGene, Double> rectangleEngine = Engine
                .builder(JeneticsExample::fitnessRectangle, rectangleFactory)
                .populationSize(RECTANGLE_COUNT)
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        // Silnik ewolucyjny dla cylindrów
        Engine<IntegerGene, Double> cylinderEngine = Engine
                .builder(JeneticsExample::fitnessCylinder, cylinderFactory)
                .populationSize(CYLINDER_COUNT)
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        // Silnik ewolucyjny dla sfer
        Engine<IntegerGene, Double> sphereEngine = Engine
                .builder(JeneticsExample::fitnessSphere, sphereFactory)
                .populationSize(SPHERE_COUNT)
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        Phenotype<IntegerGene, Double> bestRectangle = null;
        Phenotype<IntegerGene, Double> bestCylinder = null;
        Phenotype<IntegerGene, Double> bestSphere = null;
        double bestRectangleVolume = 0;
        double bestCylinderVolume = 0;
        double bestSphereVolume = 0;

        // Выполняем до 100 поколений или достижения целевого объема
        for (int generation = 1; generation <= 100; generation++) {
            EvolutionResult<IntegerGene, Double> rectangleResult = rectangleEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            EvolutionResult<IntegerGene, Double> cylinderResult = cylinderEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            EvolutionResult<IntegerGene, Double> sphereResult = cylinderEngine.stream()
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

            // Рассчитываем общий объем
            double totalVolume = RECTANGLE_COUNT * bestRectangleVolume +
                    CYLINDER_COUNT * bestCylinderVolume+SPHERE_COUNT*bestSphereVolume;

            // Вывод промежуточных данных
            System.out.println("Поколение: " + generation);
            System.out.println("Текущий объем: " + totalVolume);
            System.out.println("Целевой объем: " + TARGET_VOLUME);

            if (totalVolume >= TARGET_VOLUME) {
                System.out.println("Достигнут целевой объем! Завершаем эволюцию.");
                break;
            }
        }

        // Создаем объекты для вывода параметров
        Rectangle bestRectangleObject = createRectangle(bestRectangle.genotype());
        Cylinder bestCylinderObject = createCylinder(bestCylinder.genotype());
        Sphere bestSphereObject = createSphere(bestSphere.genotype());

        // Итоговый объем
        double finalVolume = RECTANGLE_COUNT * bestRectangleVolume +
                CYLINDER_COUNT * bestCylinderVolume+SPHERE_COUNT*bestSphereVolume;

        // Вывод результатов
        System.out.println("\nИтоговые результаты:");
        System.out.println("Лучший прямоугольник: " +
                "Ширина=" + bestRectangleObject.getWidth() +
                ", Высота=" + bestRectangleObject.getHeight() +
                ", Длина=" + bestRectangleObject.getLength() +
                ", Объем=" + bestRectangleVolume);
        System.out.println("Лучший цилиндр: " +
                "Радиус=" + bestCylinderObject.getRadius() +
                ", Высота=" + bestCylinderObject.getHeight() +
                ", Объем=" + bestCylinderVolume);
        System.out.println("Лучшая сфера: " +
                "Радиус=" + bestSphereObject.getRadius() +
                ", Объем=" + bestSphereVolume);
        System.out.println("Общий объем популяции: " + finalVolume);
        System.out.println("Целевой объем: " + TARGET_VOLUME);
    }
}
