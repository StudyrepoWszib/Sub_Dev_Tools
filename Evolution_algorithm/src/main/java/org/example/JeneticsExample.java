package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

public class JeneticsExample {

    private static final int MAX_TOTAL_VOLUME = 500000; // Maksymalna objętość
    private static final double VOLUME_TOLERANCE = MAX_TOTAL_VOLUME * 0.05; // Dopuszczalna tolerancja
    private static final int POPULATION_SIZE = 50; // Rozmiar populacji

    // Obliczanie objętości dla jednego osobnika
    private static int calculateVolume(Genotype<IntegerGene> gt) {
        int height = gt.get(0).get(0).intValue();
        int length = gt.get(1).get(0).intValue();
        int width = gt.get(2).get(0).intValue();
        return height * length * width;
    }

    // Оценка пригодности, основанная на способности заполнить объем популяцией одинаковых прямоугольников
    private static double fitnessForFillingVolume(Genotype<IntegerGene> gt) {
        int individualVolume = calculateVolume(gt);
        int totalVolume = individualVolume * POPULATION_SIZE;

        // Штраф за превышение максимального объема
        if (totalVolume > MAX_TOTAL_VOLUME) {
            return MAX_TOTAL_VOLUME - totalVolume;
        }

        // Награда за объем, близкий к целевому
        if (Math.abs(MAX_TOTAL_VOLUME - totalVolume) <= VOLUME_TOLERANCE) {
            return MAX_TOTAL_VOLUME - Math.abs(MAX_TOTAL_VOLUME - totalVolume);
        }

        // Умеренное значение для объемов вне допустимого диапазона
        return totalVolume * 0.8;
    }

    public static void main(String[] args) {
        // Tworzymy genotyp z trzema genami (wysokość, długość, szerokość)
        Factory<Genotype<IntegerGene>> genotypeFactory = Genotype.of(
                IntegerChromosome.of(5, 10),   // Wysokość (od 5 do 10)
                IntegerChromosome.of(5, 50),  // Długość (od 5 do 50)
                IntegerChromosome.of(5, 50)   // Szerokość (od 5 do 50)
        );

        // Definiujemy funkcję dopasowania
        Engine<IntegerGene, Double> engine = Engine
                .builder(JeneticsExample::fitnessForFillingVolume, genotypeFactory)
                .populationSize(POPULATION_SIZE)
                .selector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.05), new SinglePointCrossover<>(0.6))
                .build();

        // Wykonujemy ewolucję
        EvolutionResult<IntegerGene, Double> result = engine.stream()
                .limit(Limits.bySteadyFitness(100))
                .limit(Limits.byFixedGeneration(500))
                .collect(EvolutionResult.toBestEvolutionResult());

        // Wyświetlenie najlepszego rozwiązania
        if (result != null && result.bestPhenotype() != null) {
            System.out.println("Najlepszy wynik: " + result.bestPhenotype());
            int bestVolume = calculateVolume(result.bestPhenotype().genotype());
            int totalVolume = bestVolume * POPULATION_SIZE;
            System.out.println("Objętość jednego osobnika: " + bestVolume);
            System.out.println("Całkowita objętość populacji: " + totalVolume + " (cel: " + MAX_TOTAL_VOLUME + ")");
        } else {
            System.out.println("Nie udało się osiągnąć rozwiązania w dopuszczalnym zakresie.");
        }
    }
}
