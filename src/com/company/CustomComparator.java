package com.company;

import com.company.models.Chromosome;

import java.util.Comparator;

/**
 * Created by Scott on 7/14/2015.
 */
public class CustomComparator implements Comparator<Chromosome> {
    @Override
    public int compare(Chromosome o1, Chromosome o2) {
        if (o1.getFitness() == o2.getFitness())
            return 0;
        if (o1.getFitness() > o2.getFitness())
            return 1;

        return -1;
    }
}
