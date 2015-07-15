package com.company.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 7/14/2015.
 */
public class Chromosome {
    private List<Gene> composition;
    private double fitness;

    public Chromosome() {
        composition = new ArrayList<Gene>();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<Gene> getComposition() {
        return composition;
    }

    public void setComposition(List<Gene> composition) {
        this.composition = composition;
    }

    public void addGeneToComposition(Gene gene) {
        composition.add(gene);
    }

    @Override
    public String toString() {
        String out = "";

        for (Gene gene : composition) {
            out += gene.getMeaning();
        }

        return "Chromosome: " + out + " Fit: " + getFitness();
    }
}
