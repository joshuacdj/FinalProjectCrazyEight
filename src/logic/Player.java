package logic;

import java.util.*;

public abstract class Player {
    protected static final int MAXDRAWCOUNT = 5;

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
        return hand.size();
    }

    public ArrayList<Card> getPlayableCards() {
        return playableCards;
    }

    public String getName() {
        return name;
    }

    public int calculatePoints() {
        int points = 0;
        for (Card c : hand) {
            points += c.calculatePoints();
        }
        return points;
    }

    public void drawCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public void setPlayableCards (Card lastPlayedCard) {
        // Clear the previous list of playable cards
        playableCards.clear();

        // Loop through every card in hand to and add to playableCards if card is playable
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