package logic;

import java.util.*;
import static gui.Sound.*;

public class Computer extends Player {
    private DrawActionListener drawActionListener;

    public Computer(String name) {
        super(name);
    }

    public void setDrawActionListener(DrawActionListener listener) {
        this.drawActionListener = listener;
    }

    public ArrayList<Object> action(Card lastPlayedCard, DrawPile deck) {
        // Initialise the number of cards drawn per turn in a computer action
        int cardsDrawn = 0;

        // Initialise the card and suit chosen by the computer
        ArrayList<Object> output = new ArrayList<>();

        // Set list of playable cards
        setPlayableCards(lastPlayedCard);

        // Check if there are no playable cards in hand and if cards drawn is less than the allowed maximum number of cards drawn
        while (getPlayableCards().isEmpty() && cardsDrawn < MAXDRAWCOUNT) {
            // Draw a card and update playable cards and the number of cards drawn
            drawCard(deck.getTopCard());
            if (drawActionListener != null) {
                drawActionListener.onCardDrawn(this); // Notify about the draw
            }
            setPlayableCards(lastPlayedCard);
            drawCardSound();
            cardsDrawn++;

            // Delay for 1 second
            try {
                // Pause for 1 second before the next draw
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore the interrupted status after catching the interrupted exception
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Check if player has at least one playable card
        if (!getPlayableCards().isEmpty()) {
            // Declare the card to be played
            Card cardPlayed = null;

            // Choose to discard the card worth the most points
            // If 8 present, return 8 and choose suit which has the highest point total in hand
            for (Card card : getPlayableCards()) {
                if (card.getValue() == 8) {
                    removeCard(card);
                    dealCardEightSound();

                    // Set the suit to the one which accumulates to the most points in the computer's hand
                    Suit desiredSuit = findHighestPointSuit();
                    output.add(card);
                    output.add(desiredSuit);
                    return output;
                }
            }

            // Else choose the highest face value card
            // Handle the case if comp only has 1 playable card. He must play it.
            if(getPlayableCards().size() == 1){
                cardPlayed = getPlayableCards().getFirst();
            } else {
                // If more than 1 card, play the highest valued card
                cardPlayed = Collections.max(getPlayableCards(), new CardCompare());
            }

            // Update the computer's hand and return the card's value and suit
            removeCard(cardPlayed);
            output.add(cardPlayed);
            output.add(cardPlayed.getSuit());
            return output;
        }
        return null;
    }

    public Suit findHighestPointSuit() {
        // Find which suit has the highest point total in computer's hand
        // Initialise a hashmap with the suit as the key and the point total for that suit as the value
        HashMap<Suit, Integer> suitPointCount = new HashMap<>();

        // Loop through every card in hand
        for (Card c: getHand()) {
            if (c.getValue() == 8) {
                // Exclude any 8 card from suit count
                continue;
            }

            // Calculate the points to put into map
            int points = suitPointCount.getOrDefault(c.getSuit(), 0) + c.calculatePoints();
            suitPointCount.put(c.getSuit(), points);
        }
        // Return the suit with the highest sum of points in the computer's hand
        return Collections.max(suitPointCount.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }
}

