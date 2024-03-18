package main.java;

import main.java.Card;

import java.util.Comparator;

public class CardCompare implements Comparator<Card> {
    public int compare(Card a, Card b) {
//        cards in ascending order
        return a.getValue() - b.getValue();
    }
}
