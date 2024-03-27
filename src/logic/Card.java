package logic;

public class Card {
    private final int VALUE;
    private final Suit SUIT;
    // Filepath refers to the filepath of the image that we are using to display each card
    private final String FILE_PATH;

    public Card(int value, Suit suit) {
        // 11 -> J ; 12 -> Q ; 13 -> K
        VALUE = value;
        SUIT = suit;
        FILE_PATH = "images/" + value + "_of_" + suit.toString().toLowerCase() + ".png";
    }

    public String getFilepath() {
        return FILE_PATH;
    }

    public int getValue() {
        return VALUE;
    }

    public Suit getSuit() {
        return SUIT;
    }

    public int calculatePoints() {
        return switch (VALUE) {
            // For 8s, the points are equivalent to 50
            case 8 -> 50;
            // For Jack, Queen or King, the points are equivalent to 10
            case 11, 12, 13 -> 10;
            // Every other card uses the face value of the cards as the points
            default -> VALUE;
        };
    }

}

