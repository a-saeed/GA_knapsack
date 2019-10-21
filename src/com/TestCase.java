package com;

import javafx.util.Pair;

import java.util.ArrayList;

public class TestCase {
    private int size;
    private int maxWeight;
    private ArrayList<Pair<Integer , Integer>> items;

    public TestCase(int s , int max , ArrayList<Pair<Integer , Integer>> itemss)
    {
        size = s;
        maxWeight = max;
        ArrayList<Pair<Integer , Integer>> items = new ArrayList<>(size);
        items = itemss;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public ArrayList<Pair<Integer, Integer>> getItems() {
        return items;
    }

    public void setItems(ArrayList<Pair<Integer, Integer>> items) {
        this.items = items;
    }

    public float getValuePerWeight(int index)
    {
        return (float)items.get(index).getValue() / (float) items.get(index).getKey();
    }
}
