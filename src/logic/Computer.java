package logic;

import java.util.*;

import static gui.Sound.*;

public class Computer extends Player {
    private DrawActionListener drawActionListener;

    public void setDrawActionListener(DrawActionListener listener) {
        this.drawActionListener = listener;
    }

    public Computer(String name) {
        super(name);
    }

    public ArrayList<Object> action(Card lastPlayedCard, DrawPile deck) {
        // Initialise the number of cards drawn in a computer action
        int cardsDrawn = 0;

        // Initialise the card and suit chosen by the computer
        ArrayList<Object> output = new ArrayList<>();

        // Set list of playable cards
        setPlayableCards(lastPlayedCard);

        // Check if there are no playable cards in hand and if cards drawn is less than the allowed maximum
        while (getPlayableCards().isEmpty() && cardsDrawn < MAXDRAWCOUNT) {
            // Draw a card and update playable cards and cards drawn
            drawCard(deck.getTopCard());
//WILL DRAW ACTIONLISTENER EVER BE NULL?
            if (drawActionListener != null) {
                drawActionListener.onCardDrawn(this); // Notify about the draw
            }
            setPlayableCards(lastPlayedCard);
            drawCardSound();
            cardsDrawn++;

            // Delay for 1 second
            try {
                Thread.sleep(1000); // Pause for 1 second before the next draw
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                // Optionally handle the interruption, e.g., break the loop
                break;
            }
        }
        System.out.println(getHand());
        // Check if player has at least one playable card
        if (!getPlayableCards().isEmpty()) {
            // Choose to discard the card worth the most points
            // If 8 present, return 8 and choose suit which has the highest point total in hand
            for (Card card : getPlayableCards()) {
                if (card.getValue() == 8) {
                    removeCard(card);
                    dealCardEightSound();

                    // Set the suit to the one the computer has the most number of cards
                    Suit desiredSuit = findHighestPointSuit();
                    output.add(card);
                    output.add(desiredSuit);
                    return output;
                }
            }

            // Else choose the highest face value card
            Card cardPlayed = null;
            if(getPlayableCards().size() == 1){ // Handle the case if comp only has 1 playable card. He must play it.
                cardPlayed = getPlayableCards().getFirst();
            } else {
                cardPlayed = Collections.max(getPlayableCards(), new CardCompare());    // If more than 1 card, play the highest valued card
            }
            removeCard(cardPlayed);
            output.add(cardPlayed);
            output.add(cardPlayed.getSuit());
            return output;
        }
        return null;
    }

    public Suit findHighestPointSuit() {
        // Find which suit has the highest point total in computer's hand
        HashMap<Suit, Integer> suitCount = new HashMap<>();
        suitCount.put(Suit.DIAMONDS, 0);
        suitCount.put(Suit.CLUBS, 0);
        suitCount.put(Suit.HEARTS, 0);
        suitCount.put(Suit.SPADES, 0);

        for (Card c: getHand()) {
            if (c.getValue() == 8) { break; } // exclude any 8 card from suit count
            suitCount.put(c.getSuit(), suitCount.get(c.getSuit()) + c.calculatePoints());
        }

        return Collections.max(suitCount.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }
}

