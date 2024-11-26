package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

import java.util.ArrayList;
import java.util.List;

public class JeneticsExample {

    private static final int MAX_TOTAL_VOLUME = 500000; // Maksymalna objętość
    private static final double VOLUME_TOLERANCE = MAX_TOTAL_VOLUME * 0.05; // Dopuszczalna tolerancja
    private static final int POPULATION_SIZE = 50; // Rozmiar populacji

    // Создаем прямоугольник с заданными размерами
    private static Rectangle createRectangle() {
        double width = Math.random() * (10 - 5) + 5;   // Ширина от 5 до 10
        double height = Math.random() * (50 - 5) + 5; // Высота от 5 до 50
        double length = Math.random() * (50 - 5) + 5; // Длина от 5 до 50
        return new Rectangle(width, height, length);
    }

    // Генерируем фабрику генотипов из объектов Rectangle
    private static Factory<Genotype<IntegerGene>> createGenotypeFactory() {
        List<Rectangle> rectangles = new ArrayList<>();

        // Создаем объекты Rectangle для начальной популяции
        for (int i = 0; i < POPULATION_SIZE; i++) {
            rectangles.add(createRectangle());
        }

        return () -> {
            // Для каждого Rectangle создаем генотип
            Rectangle rectangle = rectangles.get((int) (Math.random() * rectangles.size()));
            return Genotype.of(
                    IntegerChromosome.of((int) rectangle.getWidth(), (int) rectangle.getWidth() + 1),
                    IntegerChromosome.of((int) rectangle.getHeight(), (int) rectangle.getHeight() + 1),
                    IntegerChromosome.of((int) rectangle.getLength(), (int) rectangle.getLength() + 1)
            );
        };
    }


    // Обчисление пригодности для заполнения объема
    private static double fitnessForFillingVolume(Genotype<IntegerGene> genotype) {
        double width = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        double length = genotype.get(2).get(0).intValue();

        double individualVolume = width * height * length;
        double totalVolume = individualVolume * POPULATION_SIZE;

        // Штраф за превышение объема
        if (totalVolume > MAX_TOTAL_VOLUME) {
            return MAX_TOTAL_VOLUME - totalVolume;
        }

        // Награда за объем близкий к целевому
        if (Math.abs(MAX_TOTAL_VOLUME - totalVolume) <= VOLUME_TOLERANCE) {
            return MAX_TOTAL_VOLUME - Math.abs(MAX_TOTAL_VOLUME - totalVolume);
        }

        // Умеренная оценка для объемов вне допустимого диапазона
        return totalVolume * 0.8;
    }

    public static void main(String[] args) {
        // Создаем фабрику генотипов
        Factory<Genotype<IntegerGene>> genotypeFactory = createGenotypeFactory();

        // Определение эволюционного процесса
        Engine<IntegerGene, Double> engine = Engine
                .builder(JeneticsExample::fitnessForFillingVolume, genotypeFactory)
                .populationSize(POPULATION_SIZE)
                .selector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.05), new SinglePointCrossover<>(0.6))
                .build();

        // Выполнение эволюции
        EvolutionResult<IntegerGene, Double> result = engine.stream()
                .limit(Limits.bySteadyFitness(100))
                .limit(Limits.byFixedGeneration(500))
                .collect(EvolutionResult.toBestEvolutionResult());

        // Вывод результатов
        if (result != null && result.bestPhenotype() != null) {
            Genotype<IntegerGene> bestGenotype = result.bestPhenotype().genotype();

            double width = bestGenotype.get(0).get(0).intValue();
            double height = bestGenotype.get(1).get(0).intValue();
            double length = bestGenotype.get(2).get(0).intValue();

            double volume = width * height * length;
            double totalVolume = volume * POPULATION_SIZE;

            System.out.println("Лучший прямоугольник:");
            System.out.println("Ширина: " + width);
            System.out.println("Высота: " + height);
            System.out.println("Длина: " + length);
            System.out.println("Объем одного: " + volume);
            System.out.println("Общий объем: " + totalVolume + " (Цель: " + MAX_TOTAL_VOLUME + ")");
        } else {
            System.out.println("Не удалось достичь решения в допустимых границах.");
        }
    }
}
