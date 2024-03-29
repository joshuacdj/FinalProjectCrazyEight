package logic;

import java.util.Comparator;

public class CardCompare implements Comparator<Card> {
    public int compare(Card a, Card b) {
        // Cards ordered in ascending order of value
        return a.getValue() - b.getValue();
    }
}
