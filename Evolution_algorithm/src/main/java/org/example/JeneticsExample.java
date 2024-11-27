package org.example;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;

public class JeneticsExample {

    private static final int MAX_TOTAL_VOLUME = 500000; // Максимальная целевая объем
    private static final int RECTANGLE_COUNT = 43;      // Количество прямоугольников
    private static final int CYLINDER_COUNT = 43;       // Количество цилиндров
    private static final double TARGET_VOLUME = MAX_TOTAL_VOLUME * 0.95; // Целевой объем

    // Создание объекта прямоугольника
    private static Rectangle createRectangle(Genotype<IntegerGene> genotype) {
        double width = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        double length = genotype.get(2).get(0).intValue();
        return new Rectangle(width, height, length);
    }

    // Создание объекта цилиндра
    private static Cylinder createCylinder(Genotype<IntegerGene> genotype) {
        double radius = genotype.get(0).get(0).intValue();
        double height = genotype.get(1).get(0).intValue();
        return new Cylinder(radius, height);
    }

    // Функция фитнеса для прямоугольников
    private static double fitnessRectangle(Genotype<IntegerGene> genotype) {
        return createRectangle(genotype).calculateVolume();
    }

    // Функция фитнеса для цилиндров
    private static double fitnessCylinder(Genotype<IntegerGene> genotype) {
        return createCylinder(genotype).calculateVolume();
    }

    public static void main(String[] args) {
        // Генотип для прямоугольников
        Factory<Genotype<IntegerGene>> rectangleFactory = Genotype.of(
                IntegerChromosome.of(5, 10),   // Ширина
                IntegerChromosome.of(5, 25),  // Высота
                IntegerChromosome.of(5, 25)   // Длина
        );

        // Генотип для цилиндров
        Factory<Genotype<IntegerGene>> cylinderFactory = Genotype.of(
                IntegerChromosome.of(5, 10),  // Радиус
                IntegerChromosome.of(5, 25)   // Высота
        );

        // Эволюционный движок для прямоугольников
        Engine<IntegerGene, Double> rectangleEngine = Engine
                .builder(JeneticsExample::fitnessRectangle, rectangleFactory)
                .populationSize(RECTANGLE_COUNT)
                .selector(new TournamentSelector<>(3)) // Турнирный отбор
                .alterers(
                        new Mutator<>(0.1),             // Мутация с вероятностью 10%
                        new SinglePointCrossover<>(0.7) // Скрещивание с вероятностью 70%
                )
                .build();

        // Эволюционный движок для цилиндров
        Engine<IntegerGene, Double> cylinderEngine = Engine
                .builder(JeneticsExample::fitnessCylinder, cylinderFactory)
                .populationSize(CYLINDER_COUNT)
                .selector(new TournamentSelector<>(3)) // Турнирный отбор
                .alterers(
                        new Mutator<>(0.1),             // Мутация с вероятностью 10%
                        new SinglePointCrossover<>(0.7) // Скрещивание с вероятностью 70%
                )
                .build();

        Phenotype<IntegerGene, Double> bestRectangle = null;
        Phenotype<IntegerGene, Double> bestCylinder = null;
        double bestRectangleVolume = 0;
        double bestCylinderVolume = 0;

        // Выполняем до 100 поколений или достижения целевого объема
        for (int generation = 1; generation <= 100; generation++) {
            EvolutionResult<IntegerGene, Double> rectangleResult = rectangleEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            EvolutionResult<IntegerGene, Double> cylinderResult = cylinderEngine.stream()
                    .limit(1)
                    .collect(EvolutionResult.toBestEvolutionResult());

            Phenotype<IntegerGene, Double> currentBestRectangle = rectangleResult.bestPhenotype();
            Phenotype<IntegerGene, Double> currentBestCylinder = cylinderResult.bestPhenotype();

            double currentRectangleVolume = currentBestRectangle.fitness();
            double currentCylinderVolume = currentBestCylinder.fitness();

            // Обновляем лучшие значения
            if (currentRectangleVolume > bestRectangleVolume) {
                bestRectangle = currentBestRectangle;
                bestRectangleVolume = currentRectangleVolume;
            }

            if (currentCylinderVolume > bestCylinderVolume) {
                bestCylinder = currentBestCylinder;
                bestCylinderVolume = currentCylinderVolume;
            }

            // Рассчитываем общий объем
            double totalVolume = RECTANGLE_COUNT * bestRectangleVolume + CYLINDER_COUNT * bestCylinderVolume;

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

        // Итоговый объем
        double finalVolume = RECTANGLE_COUNT * bestRectangleVolume + CYLINDER_COUNT * bestCylinderVolume;

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
        System.out.println("Общий объем популяции: " + finalVolume);
        System.out.println("Целевой объем: " + TARGET_VOLUME);
    }
}
