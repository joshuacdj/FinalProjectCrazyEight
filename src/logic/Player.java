package logic;

import java.util.*;

public abstract class Player {

    // Each player will have a hand, a name and their total points for a game
    protected static final int MAX_DRAW_COUNT = 5;
    private ArrayList<Card> hand;
    private ArrayList<Card> playableCards;
    private String name;

    public Player(String name) {
        this.hand = new ArrayList<>();
        this.playableCards = new ArrayList<>();
        this.name = name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getHandSize() {
        // Returning the number of cards in the Player's hand
        return hand.size();
    }

    public ArrayList<Card> getPlayableCards() {
        // Returning the list of playable cards in the Player's current turn
        return playableCards;
    }

    // Return name of player
    public String getName() {
        return name;
    }


    public int calculatePoints() {
        // Calculate the points in player's hand
        int points = 0;
        for (Card c : hand) {
            points += c.calculatePoints();
        }
        return points;
    }

    public void drawCard(Card card) {
        // Draw a card from the deck
        hand.add(card);
    }

    public void removeCard(Card card) {
        // Remove the card from current hand
        hand.remove(card);
    }

    public void setPlayableCards (Card lastPlayedCard) {
        // Clear the previous list of playable cards. Basically clear it for each start of the new turn
        playableCards.clear();
        // Create playable cards list according to the lastPlayedCard
        for (Card card : hand) {
            if (isPlayable(card, lastPlayedCard)) {
                playableCards.add(card);
            }
        }
    }

    public boolean isPlayable (Card currCard, Card lastPlayedCard) {
        //This method returns true if a card is playable
        if (currCard.getValue() == lastPlayedCard.getValue()) {
            return true;
        } else if (currCard.getSuit().equals(lastPlayedCard.getSuit())) {
            return true;
        } else return currCard.getValue() == 8;
    }

}