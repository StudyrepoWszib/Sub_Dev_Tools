package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

public class JeneticsExample {

    private static final int MAX_TOTAL_VOLUME = 500000; // Maksymalna objętość
    private static final double VOLUME_TOLERANCE = MAX_TOTAL_VOLUME * 0.05; // Dopuszczalna tolerancja
    private static final int POPULATION_SIZE = 50; // Rozmiar populacji

    // Metoda obliczająca objętość dla jednego osobnika
    private static int calculateVolume(Genotype<IntegerGene> gt) {
        int height = gt.get(0).get(0).intValue();
        int length = gt.get(1).get(0).intValue();
        int width = gt.get(2).get(0).intValue();
        return height * length * width;
    }

    // Funkcja dopasowania dla osobnika, uwzględniająca całkowitą objętość populacji
    private static double individualFitnessForGroup(Genotype<IntegerGene> gt, int totalVolume) {
        int individualVolume = calculateVolume(gt);

        // Kara za przekroczenie maksymalnej objętości populacji
        if (totalVolume > MAX_TOTAL_VOLUME) {
            return Math.max(0, MAX_TOTAL_VOLUME - individualVolume); // Obniżenie dopasowania
        }

        // Kara za zbyt małą objętość populacji
        if (totalVolume < MAX_TOTAL_VOLUME - VOLUME_TOLERANCE) {
            return individualVolume * 0.9; // Płynne zwiększanie dopasowania
        }

        // Normalne dopasowanie dla objętości w dopuszczalnych granicach
        return individualVolume;
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
                .builder(gt -> 0.0, genotypeFactory) // Tymczasowa funkcja
                .populationSize(POPULATION_SIZE)
                .selector(new RouletteWheelSelector<>()) // Łagodniejsza selekcja
                .alterers(new Mutator<>(0.03), new SinglePointCrossover<>(0.6)) // Zmniejszenie szans na mutacje
                .build();

        // Wykonujemy ewolucję
        EvolutionResult<IntegerGene, Double> result = engine.stream()
                .limit(Limits.byFixedGeneration(200)) // Ograniczenie do 200 pokoleń
                .limit(er -> {
                    // Obliczanie aktualnej objętości populacji
                    int totalVolume = er.population().asList().stream()
                            .mapToInt(pt -> calculateVolume(pt.genotype()))
                            .sum();

                    // Ponowne obliczanie dopasowania dla całej populacji
                    er.population().forEach(pt -> {
                        double fitness = individualFitnessForGroup(pt.genotype(), totalVolume);
                        pt = pt.withFitness(fitness);
                    });

                    // Wyświetlanie aktualnych informacji o populacji
                    System.out.println("Całkowita objętość populacji: " + totalVolume + " (cel: " + MAX_TOTAL_VOLUME + ")");
                    System.out.println("------------------------");

                    // Kończymy ewolucję, jeśli objętość mieści się w dopuszczalnych granicach
                    return Math.abs(MAX_TOTAL_VOLUME - totalVolume) > VOLUME_TOLERANCE;
                })
                .collect(EvolutionResult.toBestEvolutionResult());

        // Obsługa przypadku, gdy wynik jest null
        if (result == null || result.bestPhenotype() == null) {
            System.out.println("Nie udało się osiągnąć rozwiązania w dopuszczalnym zakresie.");
        } else {
            // Wyświetlenie najlepszego rozwiązania
            System.out.println("Najlepszy wynik: " + result.bestPhenotype());
        }
    }
}
