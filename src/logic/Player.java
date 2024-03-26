package logic;

import java.util.*;

public abstract class Player {

    protected static final int MAXDRAWCOUNT = 5;

    // Each player will have a hand, a name and their total points for a game
    private ArrayList<Card> hand;
    private ArrayList<Card> playableCards;
    private String name;
    private int points;

    public Player(String name) {

        this.hand = new ArrayList<>();

        this.playableCards = new ArrayList<>();

        this.name = name;
        // All players should have 0 points initially
        this.points = 0;
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


    // Calculate the points in player's hand
    public int calculatePoints() {
        int points = 0;
        for (Card c : hand) {
            points += c.calculatePoints();
        }
        return points;
    }

    // Draw a card from the deck
    public void drawCard(Card card) {
        hand.add(card);
    }

    // Remove the card from current hand
    public void removeCard(Card card) {
        hand.remove(card);
    }

    //    Create playable cards list
    public void setPlayableCards (Card lastPlayedCard) {

        // Clear the previous list of playable cards. Basically clear it for each start of the new turn
        playableCards.clear();

        for (Card card : hand) {
            if (isPlayable(card, lastPlayedCard)) {
                playableCards.add(card);
            }
        }
    }

    //This method returns true if a card is playable
    public boolean isPlayable (Card currCard, Card lastPlayedCard) {
        System.out.println("Curr card:" + currCard + " Last played:" + lastPlayedCard);
        if (currCard.getValue() == lastPlayedCard.getValue()) {
            return true;
        } else if (currCard.getSuit().equals(lastPlayedCard.getSuit())) {
            return true;
        } else return currCard.getValue() == 8;
    }

}