package logic;

public class Card {
    /**
     * Stores value of a singular card
     */
    // Instance of value, suit and filepath. Filepath refers to the filepath of the image that we are using to display each card
    private final int value;
    private final Suit suit;
    private String filepath;

    // Card constructor
    public Card(int value, Suit suit) {
        // 11 -> J ; 12 -> Q ; 13 -> K
        this.value = value;
        this.suit = suit;
        this.filepath = "images/" + value + "_of_" + suit.toString().toLowerCase() + ".png";
    }

    // Get the filepath of the card
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

    // Calculate the points associated to each card. Points will be tallied at the endgame to determine player rankings
    public int calculatePoints() {
        return switch (value) {
            // If value is 8, the points are equivalent to 50
            case 8 -> 50;
            // If Jack, Queen or King, the points are equivalent to 10
            case 11, 12, 13 -> 10;
            // Else, use the face value of the cards as the points
            default -> value;
        };
    }

    // Check if two cards are the same. Both values and suits must be the same for the cards to be considered equal
    public boolean equals(Card c) {
        return c.getValue() == value && c.getSuit().equals(suit);
    }


    // To print out the value and suit for card for debugging
    @Override
    public String toString() {
        return String.format("value: %d suit: %s%n", value, suit);
    }

}

