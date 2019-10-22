package com;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Knapsack {

    /*GLOBAL*/
    private final static int population_size = 4;
    private final static int test_cases = 20;
    //reading from file
    private static String input = "";

    public static void main(String[] args) throws Exception {

        ArrayList<TestCase> allTestCases = fillAllTestCases();

        /*for each iteration , read a test case from the file*/
        for (int i = 0 ; i < test_cases ; i++ )
        {
            /*VARIABLES*/
            int iterations = 50; //max num of iterations

            /*COLLECTIONS*/
            ArrayList<Chromosome> individuals = new ArrayList<>(population_size); //holding all generations.

            /*initialise the population*/
            TestCase testCase = allTestCases.get(i);

            initializePopulation(individuals , testCase.getSize());

            while (iterations != 0)
            {
                crossover(individuals , selection(individuals , testCase) );

                mutation(individuals , (float) 0.1 );

                replacement(individuals , testCase );

                iterations --;
            }
            System.out.println("case"+ "("+(i+1)+"): "  + bestSolution(individuals , testCase));
        }
    }

    private static void initializePopulation(ArrayList<Chromosome> individuals , int cells) {

        for (int i = 0 ; i < population_size ; i++){
            Chromosome member = new Chromosome(cells);
            member.initialize((float) 0.50);
            individuals.add(member);
        }
    }

    //returns indices of best individuals.
    private static ArrayList<Integer> selection(ArrayList<Chromosome> individuals , TestCase testCase) {

        ArrayList<Integer> returned = new ArrayList<>(2);
        ArrayList<Float> cumulativeFitness = new ArrayList<>(testCase.getSize()); //build the roulette wheel

        int index1;
        int index2;

        //evaluate fitness for each chromosome then add it to cumulative
        for (int i = 0 ; i < population_size ; i++)
        {
            Chromosome chromosome = individuals.get(i);
            float cFitness = chromosome.evaluate_fitness(testCase);

            if (i == 0)
                cumulativeFitness.add(cFitness);
            else
                cumulativeFitness.add(cFitness +cumulativeFitness.get(i-1));
        }

        do {
            float one = (float) (Math.random());
            float two = (float) (Math.random());

             index1 = 11111;
             index2 = 11111;

            one *= cumulativeFitness.get(population_size - 1);
            two *= cumulativeFitness.get(population_size - 1);

            for (int i = 0; i < population_size; i++) {

                if ((one <= cumulativeFitness.get(i)) && (index1 == 11111))
                    index1 = i;

                if ((two <= cumulativeFitness.get(i)) && (index2 == 11111))
                    index2 = i;
            }
        }while (index1 == index2);

        returned.add(index1);
        returned.add(index2);

        return returned;
    }

    /*takes indices of parents to reproduce based on selection */
    private static void crossover(ArrayList<Chromosome> individuals , ArrayList<Integer> parentsIndices){

        Chromosome parent1 = individuals.get(parentsIndices.get(0));
        Chromosome parent2 = individuals.get(parentsIndices.get(1));

        Chromosome offspring1 = new Chromosome(parent1.getCells());
        offspring1.setContents(parent1.getContents());

        Chromosome offspring2 = new Chromosome(parent2.getCells());
        offspring2.setContents(parent2.getContents());


        int limit = 0;

        // do a single point crossOver , location changes depending
        // on number of  cells in chromosome.

        //equal to 5 , third bit location (last 2 bits exchanged)
        if (parent1.getCells() == 5)
            limit = 2;

        //(last 5 bits exchanged)
        else if (parent1.getCells() == 10)
            limit = 4;

        //(last 11 bits exchanged)
        else if (parent1.getCells() == 20)
            limit = 8;

        //(last 17 bits exchanged)
        else if (parent1.getCells() == 30)
            limit = 13;

        //(last 11 bits exchanged)
        else if (parent1.getCells() == 50)
            limit = 24;


        for (int i = parent1.getCells() - 1 ; i > limit  ; i--)
        {
                offspring1.getContents().set(i , offspring2.getContents().get(i));
                offspring2.getContents().set(i , offspring1.getContents().get(i));
        }


        individuals.add(offspring1);
        individuals.add(offspring2);

    }

    private static void mutation(ArrayList<Chromosome> individuals , float prob) {

        int cells = individuals.get(0).getCells();

        for (int i = 0 ; i < 2 ; i++)
        {
            // last two chromosomes added by crossOver
            Chromosome chromosome = individuals.get(individuals.size() - 2 + i);

            for (int j = 0 ; j < cells ; j++)
            {
                float rand =(float) (Math.random());
                if (rand < prob)
                {
                    if (chromosome.getContents().get(j).equals("1"))
                        chromosome.getContents().set(j , "0");

                    else
                        chromosome.getContents().set(j , "1");
                }
            }
            individuals.remove(individuals.size() - 2 + i);
            individuals.add(individuals.size() - 1 + i , chromosome);
        }
    }

    private static void replacement(ArrayList<Chromosome> individuals , TestCase testCase) {

            for (int i = 0; i < individuals.size(); i++) {

                Chromosome chromosome = individuals.get(i);
                int chromosomeWeight = chromosome.check_weight(testCase);

                //remove a chromosome exceeding the max weight.
                if (chromosomeWeight > testCase.getMaxWeight())
                    individuals.remove(chromosome);

                //break when original pop size reached
                if (individuals.size() == population_size)
                    break;
            }

            //remove chromosome with least fitness.
            while (individuals.size() != population_size)
            {
                individuals.remove(getLeastFit(individuals , testCase));
            }
    }

    private static int getLeastFit(ArrayList<Chromosome> individuals , TestCase testCase)
    {
        float minFitness = 1000000;
        int index = 0;

        for (int i = 0 ; i < individuals.size() ; i++)
        {
            float currentFitness = individuals.get(i).evaluate_fitness(testCase);

            if (minFitness > currentFitness) {

                minFitness = currentFitness;
                index = i;
            }
        }
        return index;
    }

    private static float bestSolution(ArrayList<Chromosome> individuals , TestCase testCase)
    {
        float maxValue = 0;

        for (int i = 0 ; i < individuals.size() ; i++)
        {
            float currentValue = individuals.get(i).chromosomeValue(testCase);

            if (maxValue < currentValue)
            {
                maxValue = currentValue;
            }
        }
        return maxValue;
    }

    private static String getAllFile() throws Exception
    {
        File file = new File("input_example.txt");
        BufferedReader READ = new BufferedReader(new FileReader(file));
        int q=READ.read();
        String data = "";
        while(q != -1)
        {
            data +=( char)q;
            q=READ.read();
        }
        return data;
    }
    private static ArrayList<TestCase> fillAllTestCases() throws Exception
    {
        input = getAllFile();
        ArrayList<TestCase> testCases = new ArrayList<>();
        String [] allSeparated = input.split("/");

        System.out.println("length " + allSeparated.length);


        for (int i=0;i<allSeparated.length;i++)
        {
            String [] divided = allSeparated[i].split(System.lineSeparator());
            int maxW=0;
            int size;
            ArrayList<Pair<Float , Float>> items = new ArrayList<>();
//            System.out.println("divided length");
//            System.out.println(divided.length);
            for (int j=0;j<divided.length ;j++)
            {
                if(j==0)
                    maxW = Integer.valueOf(divided[0]);
                else
                {
                    String [] row = divided[j].split(" ");
                    float weight = Float.valueOf(row[0]);
                    float value = Float.valueOf(row[1]);
//                    System.out.print("weight ");
//                    System.out.print(weight);
//                    System.out.print("  value ");
//                    System.out.println(value);
                    Pair<Float,Float> p = new Pair<>(weight , value);
                    items.add(p);
                }
            }
            size = items.size();
            TestCase testCase = new TestCase(size,maxW,items);
            testCases.add(testCase);
        }
        return testCases;
    }
}