package main.java;

import java.awt.*;

public class Card {
    /**
     * Stores value of a singular card
     */
    private final int value;
    private final Suit suit;
    private String filepath;
    // Instance of value and suit

    // Card constructor
    public Card(int value, Suit suit) {
        // 11 -> J ; 12 -> Q ; 13 -> K
        this.value = value;
        this.suit = suit;
        this.filepath = "src/main/resources/images/" + value + "_of_" + suit.toString().toLowerCase() + ".png";
    }

    public String getFilepath() {
        return filepath;
    }

    // Return the face value of the card
    public int getValue() {
        return value;
    }

    // Return the suit of the card
    public Suit getSuit() {
        return suit;
    }

    // Calculate the points associated to each card
    public int calculatePoints() {
        int points = 0;
        switch(value) {
            case 8:
                points = 50;
                break;
            case 11:
            case 12:
            case 13:
                points = 10;
                break;
            default:
                points = value;
        }
        return points;
    }

    public boolean equals(Card c) {
        return c.getValue() == value && c.getSuit().equals(suit);
    }

    @Override
    public String toString() {
        return String.format("value: %d suit: %s%n", value, suit);
    }

    // overriding equals method to check for same card value and suit
    @Override
    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        else {
            Card card = (Card) object;
            return card.getValue() == getValue() && card.getSuit() == getSuit();
        }
    }

}

