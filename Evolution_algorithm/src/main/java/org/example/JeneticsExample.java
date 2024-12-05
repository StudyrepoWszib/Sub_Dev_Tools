package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class JeneticsExample {

    // Stałe dotyczące maksymalnej objętości i liczby figur
    private static final int MAX_TOTAL_VOLUME = 900000; // Maksymalna objętość
    private static final int RECTANGLE_COUNT = 50;      // Liczba prostokątów
    private static final int CYLINDER_COUNT = 50;       // Liczba cylindrów
    private static final int SPHERE_COUNT = 50;         // Liczba sfer

    // Docelowa objętość (95% maksymalnej objętości)
    private static final double TARGET_VOLUME = MAX_TOTAL_VOLUME * 0.95;

    // Parametry mutacji i krzyżowania
    private static final double MUTATION_RATE = 0.2;  // Prawdopodobieństwo mutacji
    private static final double CROSSOVER_RATE = 0.6; // Prawdopodobieństwo krzyżowania

    // Zakresy wymiarów dla prostokątów
    private static final int RECTANGLE_MIN_DIM = 5;
    private static final int RECTANGLE_MAX_WIDTH = 10;
    private static final int RECTANGLE_MAX_HEIGHT = 50;
    private static final int RECTANGLE_MAX_LENGTH = 50;

    // Zakresy wymiarów dla cylindrów
    private static final int CYLINDER_MIN_RADIUS = 5;
    private static final int CYLINDER_MAX_RADIUS = 10;
    private static final int CYLINDER_MIN_HEIGHT = 5;
    private static final int CYLINDER_MAX_HEIGHT = 30;

    // Zakresy wymiarów dla sfer
    private static final int SPHERE_MIN_RADIUS = 5;
    private static final int SPHERE_MAX_RADIUS = 25;

    // Metoda tworząca genotyp z minimalnymi wartościami genów
    private static Genotype<IntegerGene> createMinimalGenotype(Factory<Genotype<IntegerGene>> factory) {
        Genotype<IntegerGene> genotype = factory.newInstance();

        List<Chromosome<IntegerGene>> minimalChromosomes = new ArrayList<>();
        for (Chromosome<IntegerGene> chromosome : genotype) {
            List<IntegerGene> minimalGenes = new ArrayList<>();
            for (IntegerGene gene : chromosome) {
                minimalGenes.add(IntegerGene.of(gene.min(), gene.min(), gene.max()));
            }
            minimalChromosomes.add(IntegerChromosome.of(minimalGenes));
        }
        return Genotype.of(minimalChromosomes);
    }

    // Funkcja tworząca prostokąt
    private static Rectangle createRectangle(Genotype<IntegerGene> genotype) {
        double width = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        double length = genotype.get(2).get(0).intValue();
        return new Rectangle(width, height, length);
    }

    // Funkcja tworząca cylinder
    private static Cylinder createCylinder(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        return new Cylinder(radius, height);
    }

    // Funkcja tworząca sferę
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
        // Fabryki genotypów z minimalnymi wartościami
        Factory<Genotype<IntegerGene>> rectangleFactory = () -> createMinimalGenotype(
                Genotype.of(
                        IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_WIDTH),
                        IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_HEIGHT),
                        IntegerChromosome.of(RECTANGLE_MIN_DIM, RECTANGLE_MAX_LENGTH)
                )
        );

        Factory<Genotype<IntegerGene>> cylinderFactory = () -> createMinimalGenotype(
                Genotype.of(
                        IntegerChromosome.of(CYLINDER_MIN_RADIUS, CYLINDER_MAX_RADIUS),
                        IntegerChromosome.of(CYLINDER_MIN_HEIGHT, CYLINDER_MAX_HEIGHT)
                )
        );

        Factory<Genotype<IntegerGene>> sphereFactory = () -> createMinimalGenotype(
                Genotype.of(
                        IntegerChromosome.of(SPHERE_MIN_RADIUS, SPHERE_MAX_RADIUS)
                )
        );

        // Silniki ewolucyjne
        Engine<IntegerGene, Double> rectangleEngine = Engine
                .builder(JeneticsExample::fitnessRectangle, rectangleFactory)
                .populationSize(RECTANGLE_COUNT)
                .selector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        Engine<IntegerGene, Double> cylinderEngine = Engine
                .builder(JeneticsExample::fitnessCylinder, cylinderFactory)
                .populationSize(CYLINDER_COUNT)
                .selector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        Engine<IntegerGene, Double> sphereEngine = Engine
                .builder(JeneticsExample::fitnessSphere, sphereFactory)
                .populationSize(SPHERE_COUNT)
                .selector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(MUTATION_RATE),
                        new SinglePointCrossover<>(CROSSOVER_RATE)
                )
                .build();

        // Wyniki ewolucji
        Phenotype<IntegerGene, Double> bestRectangle = null;
        Phenotype<IntegerGene, Double> bestCylinder = null;
        Phenotype<IntegerGene, Double> bestSphere = null;

        double bestRectangleVolume = 0;
        double bestCylinderVolume = 0;
        double bestSphereVolume = 0;

        // Proces ewolucji
        for (int generation = 1; generation <= 100; generation++) {
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

            double totalVolume = RECTANGLE_COUNT * bestRectangleVolume +
                    CYLINDER_COUNT * bestCylinderVolume + SPHERE_COUNT * bestSphereVolume;

            if (totalVolume >= TARGET_VOLUME) {
                break;
            }
        }

        // Tworzenie najlepszych obiektów
        Rectangle bestRectangleObject = createRectangle(bestRectangle.genotype());
        Cylinder bestCylinderObject = createCylinder(bestCylinder.genotype());
        Sphere bestSphereObject = createSphere(bestSphere.genotype());

        DecimalFormat df = new DecimalFormat("#.00");
        double finalVolume = RECTANGLE_COUNT * bestRectangleVolume +
                CYLINDER_COUNT * bestCylinderVolume + SPHERE_COUNT * bestSphereVolume;

        // Wyświetlanie wyników
        System.out.println("\nWyniki końcowe:");
        System.out.println("Najlepszy prostokąt: " +
                "Szerokość=" + bestRectangleObject.getWidth() +
                ", Wysokość=" + bestRectangleObject.getHeight() +
                ", Długość=" + bestRectangleObject.getLength() +
                ", Objętość=" + df.format(bestRectangleVolume));
        System.out.println("Najlepszy cylinder: " +
                "Promień=" + bestCylinderObject.getRadius() +
                ", Wysokość=" + bestCylinderObject.getHeight() +
                ", Objętość=" + df.format(bestCylinderVolume));
        System.out.println("Najlepsza sfera: " +
                "Promień=" + bestSphereObject.getRadius() +
                ", Objętość=" + df.format(bestSphereVolume));
        System.out.println("Całkowita objętość populacji: " + df.format(finalVolume));
        System.out.println("Docelowa objętość: " + TARGET_VOLUME);
    }
}
