package com.company;

import com.company.models.Chromosome;
import com.company.models.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    private static String WORD = "apple";
    private static double MUTATION = 5;
    private static double CROSSOVER = .7;
    private static int POPULATION = 500;
    private static int GENERATIONS = 35;
    private static Random rand = new Random();

    public static void main(String[] args) {
        //Step one
        //Create random chromosomes to test with
        List<Chromosome> chromosomeList = new ArrayList<Chromosome>();
        List<Chromosome> generation = new ArrayList<Chromosome>();

        List<List<Chromosome>> generationalList = new ArrayList<List<Chromosome>>();

        int generations = 0;

        while (generation.size() < POPULATION) {
            generation.addAll(breed(chromosomeList));
        }
        generationalList.add(generation);


        while (generations < GENERATIONS) {
            List<Chromosome> oldGeneration = generationalList.get(generationalList.size()-1);
            List<Chromosome> newGeneration = new ArrayList<Chromosome>();

            //While the latest generation is not yet at ten members
            while (newGeneration.size() < POPULATION) {
                newGeneration.addAll(breed(oldGeneration));
            }

            newGeneration.addAll(oldGeneration);
            generationalList.add(newGeneration);

            generations++;
            System.out.println("Generation " + generations);
        }


        Collections.sort(generationalList.get(generationalList.size()-1), new CustomComparator());
        for (int i = 0; i < 100; i++) {
            System.out.println(generationalList.get(generationalList.size()-1).get(i));
        }

    }

    private static List<Chromosome> breed(List<Chromosome> chromosomes) {
        if (chromosomes.isEmpty()) {
            fillWithRandomChromosomes(chromosomes);
        }

        //Step three
        Collections.sort(chromosomes, new CustomComparator());

        Chromosome chromosomeOne = new Chromosome(), chromosomeTwo = new Chromosome();

        chromosomeOne = selectChromosome(chromosomes);
        chromosomeTwo = selectChromosome(chromosomes);

        crossover(chromosomeOne, chromosomeTwo);
        chromosomeOne = mutate(chromosomeOne);
        chromosomeTwo = mutate(chromosomeTwo);

        List<Chromosome> returnList = new ArrayList<Chromosome>();
        returnList.add(chromosomeOne);
        returnList.add(chromosomeTwo);

        return returnList;
    }

    private static Chromosome selectChromosome(List<Chromosome> chromosomes) {
        double totalFitness = 0.0, loopTotal = 0.0;
        for (Chromosome chromosome : chromosomes) {
            totalFitness = totalFitness + chromosome.getFitness();
        }

        double random = totalFitness * rand.nextDouble();

        //Loop till we find the correct chromosome
        for (int i = chromosomes.size() - 1; i >= 0; i--) {
            loopTotal += chromosomes.get(i).getFitness();
            if (loopTotal >= random) {
                return chromosomes.get(i);
            }
        }
        return chromosomes.get(chromosomes.size()-1);
    }

    private static void fillWithRandomChromosomes(List<Chromosome> chromosomeList) {
        for (int i = 0; i < POPULATION; i++) {
            Chromosome c = new Chromosome();

            //Each chromosome contains 5 genes, create 5 random genes
            for (int j = 0; j < WORD.length(); j++) {
                //Each gene has a value between 1 and 26 representing a letter
                Gene g = new Gene();
                g.setValue(randInt(1, 26));
                g.setMeaning(getCharForInt(g.getValue()));

                //Add gene to chromosome
                c.addGeneToComposition(g);
            }

            chromosomeList.add(c);
        }

        for (Chromosome chromosome : chromosomeList) {
            chromosome.setFitness(getFitnessScore(chromosome));
        }
    }

    private static void crossover(Chromosome src, Chromosome dest) {
        if (rand.nextDouble() > CROSSOVER) {
            int pos = randInt(1, WORD.length());

            for (int i = pos; i < WORD.length(); i++) {
                src.getComposition().get(i).setValue(dest.getComposition().get(i).getValue());
                src.getComposition().get(i).setMeaning(dest.getComposition().get(i).getMeaning());
            }
        }
    }

    private static Chromosome mutate(Chromosome chromosome) {
        for (Gene gene : chromosome.getComposition()) {
            if (randInt(0, 100) < MUTATION) {
                gene.setValue(randInt(1, 26));
                gene.setMeaning(getCharForInt(gene.getValue()));
            }
        }


        return chromosome;
    }

    public static double getFitnessScore(Chromosome chromosome) {
        int[] results = new int[WORD.length()];

        //For each gene determine how far it is from the correct letter
        for (int i = 0; i < chromosome.getComposition().size(); i++) {
            results[i] = getIntForChar(WORD.charAt(i)) - chromosome.getComposition().get(i).getValue();
        }

        double score = 0;
        for (int result : results) {
            if (result < 0) result = result * -1;
            score = score + result;
        }

        score = score / 100;
        return score;
    }

    private static int getIntForChar(char c) {
        switch (c) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            case 'i':
                return 9;
            case 'j':
                return 10;
            case 'k':
                return 11;
            case 'l':
                return 12;
            case 'm':
                return 13;
            case 'n':
                return 14;
            case 'o':
                return 15;
            case 'p':
                return 16;
            case 'q':
                return 17;
            case 'r':
                return 18;
            case 's':
                return 19;
            case 't':
                return 20;
            case 'u':
                return 21;
            case 'v':
                return 22;
            case 'w':
                return 23;
            case 'x':
                return 24;
            case 'y':
                return 25;
            case 'z':
                return 26;
        }

        return 99;
    }

    private static char getCharForInt(int value) {
        switch (value) {
            case 1:
                return 'a';
            case 2:
                return 'b';
            case 3:
                return 'c';
            case 4:
                return 'd';
            case 5:
                return 'e';
            case 6:
                return 'f';
            case 7:
                return 'g';
            case 8:
                return 'h';
            case 9:
                return 'i';
            case 10:
                return 'j';
            case 11:
                return 'k';
            case 12:
                return 'l';
            case 13:
                return 'm';
            case 14:
                return 'n';
            case 15:
                return 'o';
            case 16:
                return 'p';
            case 17:
                return 'q';
            case 18:
                return 'r';
            case 19:
                return 's';
            case 20:
                return 't';
            case 21:
                return 'u';
            case 22:
                return 'v';
            case 23:
                return 'w';
            case 24:
                return 'x';
            case 25:
                return 'y';
            case 26:
                return 'z';
        }

        return 'z';
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
