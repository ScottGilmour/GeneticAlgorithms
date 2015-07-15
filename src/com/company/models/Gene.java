package com.company.models;

/**
 * Created by Scott on 7/14/2015.
 */
public class Gene {
    private int value;
    private char meaning;

    public Gene(int value) {
        this.value = value;
    }

    public Gene() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char getMeaning() {
        return meaning;
    }

    public void setMeaning(char meaning) {
        this.meaning = meaning;
    }
}


