package com.company;

import com.company.models.Chromosome;
import com.company.models.Gene;

import java.util.*;

public class Main {
    private static String WORD = "aaabbbccc";
    private static double MUTATION = 0.002;
    private static double CROSSOVER = 0.55;
    private static int POPULATION = 32;
    private static int GENERATIONS = 100000;
    private static Random rand = new Random();

    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {
        List<Chromosome> generation = new ArrayList<Chromosome>();

        int generations = 0;

        fillWithRandomChromosomes(generation);
        evaluate(generation);

        while (generations < GENERATIONS) {
            List<Chromosome> newGeneration = new ArrayList<Chromosome>();

            //Create population
            for (int i = 0; newGeneration.size() < POPULATION; i++) {
                newGeneration.addAll(breed(generation));
            }

            evaluate(generation);

            Collections.sort(generation, new CustomComparator());

            //Elitism
            newGeneration.add(generation.get(0));
            generation.clear();
            generation.addAll(newGeneration);
            generations++;

            Collections.sort(generation, new CustomComparator());

            double totalFit = 0;
            for (Chromosome chromosome : generation) {
                totalFit = totalFit + chromosome.getFitness();
            }

            System.out.println("Generation " + generations + " Elite " + generation.get(0) + " FitAvg " + (totalFit / generation.size()));

            if (generation.get(0).getFitness() <= 0.03)
                break;
        }

        //Print top ten entries
        Collections.sort(generation, new CustomComparator());
        for (int i = 10; i >= 0; i--) {
            System.out.println(generation.get(i));
        }

    }

    private static void evaluate(List<Chromosome> generation) {
        for (Chromosome chromosome : generation) {
            chromosome.setFitness(getFitnessScore(chromosome));
        }
    }

    private static void cullDuplicates(List<Chromosome> newGeneration) {
        Set<Chromosome> hs = new HashSet<Chromosome>();
        hs.addAll(newGeneration);
        newGeneration.clear();
        newGeneration.addAll(hs);
    }

    /**
     * Returns a list of two newly created chromosomes from a list of inital chromosomes (old generation)
     * @param chromosomes
     * @return
     */
    private static List<Chromosome> breed(List<Chromosome> chromosomes) {
        List<Chromosome> returnList = new ArrayList<Chromosome>();

        //Step three
        Chromosome chromosomeOne = new Chromosome(),
                   chromosomeTwo = new Chromosome(),
                   chromosomeChild = new Chromosome();

        Collections.sort(chromosomes, new CustomComparator());

        //Roulette wheel implementation
        chromosomeOne = selectChromosome(chromosomes);
        chromosomeTwo = selectChromosome(chromosomes);

        chromosomeChild = crossover(chromosomeOne, chromosomeTwo);
        chromosomeChild = mutate(chromosomeChild);
        chromosomeChild.setFitness(getFitnessScore(chromosomeChild));

        returnList.add(chromosomeChild);

        return returnList;
    }

    /**
     * Roulette wheel selection process
     * @param chromosomes
     * @return
     */
    private static Chromosome selectChromosome(List<Chromosome> chromosomes) {

        double totalFitness = 0.0, loopTotal = 0.0;
        for (Chromosome chromosome : chromosomes) {
            totalFitness = totalFitness + chromosome.getFitness();
        }

        double random = totalFitness * rand.nextDouble();

        for (int i = 0; i < chromosomes.size(); i++) {
            loopTotal += chromosomes.get(i).getFitness();
            if (loopTotal >= random) {
                return chromosomes.get(i);
            }
        }
        return chromosomes.get(chromosomes.size()-1);
    }

    /**
     * Randomly generates initial population
     * @param chromosomeList
     */
    private static void fillWithRandomChromosomes(List<Chromosome> chromosomeList) {
        for (int i = 0; i < POPULATION; i++) {
            Chromosome c = new Chromosome();

            //Each chromosome contains word.length genes, create word.length random genes
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
        cullDuplicates(chromosomeList);
    }

    /**
     * Crossover, randomly select a split and swap every value from there on
     * @param src
     * @param dest
     */
    private static Chromosome crossover(Chromosome src, Chromosome dest) {
        Chromosome resultChromo = new Chromosome();

        if (rand.nextDouble() < CROSSOVER) {
            String binaryOne = src.getBinary().trim();
            String binaryTwo = dest.getBinary().trim();
            String resultBinary = "";

            binaryOne = binaryOne.replace(" ", "");
            binaryTwo = binaryTwo.replace(" ", "");

            int pos = randInt(1, binaryOne.length() - 1);

            String holder = binaryTwo.substring(pos, binaryTwo.length());
            String holderB = binaryOne.substring(0, pos);

            resultBinary = holder + holderB;

            resultBinary = formatBinary(resultBinary);

            resultChromo.setBinary(resultBinary);
        } else {
            if (randInt(0, 100) > 50) {
                resultChromo.setBinary(src.getBinary().trim());
            } else {
                resultChromo.setBinary(dest.getBinary().trim());
            }
        }

        return resultChromo;
    }

    private static String formatBinary(String binary) {
        String newBinary = "";
        for (int i = 0; i < binary.length(); i++) {
            if (i >= 8) {
                if (i % 8 == 0) {
                    newBinary += " ";
                }
            }
            newBinary += binary.charAt(i);
        }

        return newBinary;
    }

    /**
     * Every gene in every chromosome has a small minute chance to mutate to any other possible value
     * @param chromosome
     * @return
     */
    private static Chromosome mutate(Chromosome chromosome) {
        String binaryString = chromosome.getBinary().trim().replace(" ", "");
        char[] binaryArray = binaryString.toCharArray();

        for (int i = 0; i < binaryArray.length; i++) {
            if (rand.nextDouble() < MUTATION) {
                if (binaryArray[i] == '0') {
                    binaryArray[i] = '1';
                } else {
                    binaryArray[i] = '0';
                }
            }
        }

        binaryString = new String(binaryArray);
        binaryString = formatBinary(binaryString);
        chromosome.setBinary(binaryString);

        return chromosome;
    }

    /**
     * Fitness function, determines how close to the correct word the chromosome is, ideally an exact match is 0.
     * @param chromosome
     * @return
     */
    public static double getFitnessScore(Chromosome chromosome) {
        int[] results = new int[WORD.length()];
        int addition = WORD.length();

        //For each gene determine how far it is from the correct letter
        for (int i = 0; i < chromosome.getComposition().size(); i++) {
            results[i] = (getIntForChar(WORD.charAt(i)) - chromosome.getComposition().get(i).getValue()) * 2;
            if (results[i] == 0)
                addition--;
        }

        double score = addition;
        for (int result : results) {
            if (result < 0) result = result * -1;
            score = score + result;
        }
        score = score / 100;
        return score;
    }

    public static int getIntForChar(char c) {
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
            default:
                return 999;
        }
    }

    public static char getCharForInt(int value) {
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
            default:
                return 'z';
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
