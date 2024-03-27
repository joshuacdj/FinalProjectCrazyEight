package logic;

public class Card {
    private final int value;
    private final Suit suit;
    // Filepath refers to the filepath of the image that we are using to display each card
    private final String filepath;

    public Card(int value, Suit suit) {
        // 11 -> J ; 12 -> Q ; 13 -> K
        this.value = value;
        this.suit = suit;
        this.filepath = "images/" + value + "_of_" + suit.toString().toLowerCase() + ".png";
    }

    public String getFilepath() {
        return filepath;
    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public int calculatePoints() {
        return switch (value) {
            // For 8s, the points are equivalent to 50
            case 8 -> 50;
            // For Jack, Queen or King, the points are equivalent to 10
            case 11, 12, 13 -> 10;
            // Every other card uses the face value of the cards as the points
            default -> value;
        };
    }

    public boolean equals(Card c) {
        return c.getValue() == value && c.getSuit().equals(suit);
    }

    @Override
    public String toString() {
        return String.format("value: %d suit: %s%n", value, suit);
    }
}

