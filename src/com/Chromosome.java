package com;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    private int cells; //num of test cases.. 0 or 1
    private ArrayList<String> individual ;

    /*constructor*/
    public Chromosome(int cells)
    {
        this.cells = cells;
        individual = new ArrayList<>(cells);
    }

    /*given a probability , set the contents of the chromosome.. 1 or 0*/
    public void initialize(float probability)
    {
        for(int i = 0 ; i < cells ; i++)
        {
            float odds =(float) (Math.random());

            //check id its greater than or equal given probability
            if (odds >= probability)
                individual.add("1");
        }

    }

}
