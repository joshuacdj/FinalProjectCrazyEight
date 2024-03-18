import java.util.*;

public abstract class Player {

    // Each player will have a hand, a name and their total points for a game
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> playableCards = new ArrayList<>();
    private String name;
    private int points;

    public Player(ArrayList<Card> hand, String name) {
        this.hand = hand;
        this.name = name;
        // All players should have 0 points initially
        this.points = 0;
    }

    // [card, desiredSuit]
    // Returns null if player skips turn (drew 5 cards)
    public abstract ArrayList<Object> action(Card card, DrawPile deck);

    // Add points to the player depending on the value of the cards left in their hand at the end of a round
    public void addPoints(int points) {
        this.points += points;
    }

    // Calculate the points in player's hand
    public int calculatePoints() {
        int points = 0;
        for (Card c : hand) {
            points += c.calculatePoints();
        }
        return points;
    }
    public void clearHand() {
        hand.clear();
    }

    // Draw a card from the deck
    public void drawCard(Card card) {
        hand.add(card);
    }

    // Return the current hand of the player
    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getPlayableCards() {
        return playableCards;
    }

    // Return the points accumulated by the player in total for that game
    public int getPoints() {
        return points;
    }

    // Return name of player
    public String getName() {
        return name;
    }

    // How card is deemed playable
    public boolean isPlayable (Card currCard, Card lastPlayedCard) {
        if (currCard.getValue() == lastPlayedCard.getValue()) {
            return true;
        } else if (currCard.getSuit() == lastPlayedCard.getSuit()) {
            return true;
        } else return currCard.getValue() == 8;
    }

    // Remove the card from current hand
    public void removeCard(Card card) {
        hand.remove(card);
    }

    //    Create playable cards list
    public void setPlayableCards (Card lastPlayedCard) {
        for (Card card : getHand()) {
            if (isPlayable(card, lastPlayedCard)) {
                playableCards.add(card);
            }
        }
    }


    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Card c : hand) {
                sb.append(c);
            }

            return sb.toString();
        } catch (NullPointerException e) {
            System.out.println("No cards in hand!");
        }

        return "";
    }
}