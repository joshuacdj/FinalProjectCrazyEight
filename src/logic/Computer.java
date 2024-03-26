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
        int cardsDrawn = 0;
        ArrayList<Object> output = new ArrayList<>();
//        set list of playable cards
        setPlayableCards(lastPlayedCard);
//        check if there are no playable cards in hand
        while (getPlayableCards().isEmpty() && cardsDrawn < 5) {
            drawCard(deck.getTopCard());
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
//        check if player drew 5 cards
        if (!getPlayableCards().isEmpty()) {
//            choose to discard the card worth the most points
//            if 8 present, return 8 and choose suit which is most common in hand
            for (Card card : getPlayableCards()) {
                if (card.getValue() == 8) {
                    removeCard(card);
                    dealCardEightSound();

//                    find which suit is most common in computer's hand
                    HashMap<Suit, Integer> suitCount = new HashMap<>();
                    suitCount.put(Suit.DIAMONDS, 0);
                    suitCount.put(Suit.CLUBS, 0);
                    suitCount.put(Suit.HEARTS, 0);
                    suitCount.put(Suit.SPADES, 0);

                    for (Card c: getHand()) {
                        if (c.equals(card)) { break; } // exclude current 8 card from suit count
                        suitCount.put(c.getSuit(), suitCount.get(c.getSuit()) + 1);
                    }
                    Suit desiredSuit = Collections.max(suitCount.entrySet(), HashMap.Entry.comparingByValue()).getKey();
                    output.add(card);
                    output.add(desiredSuit);
                    return output;
                }
            }
//            else choose the highest face value card
            Card cardPlayed = null;
            if(getPlayableCards().size() < 2){
                cardPlayed = getPlayableCards().getFirst();
            }
            else {
                cardPlayed = Collections.max(getPlayableCards(), new CardCompare());
            }
            removeCard(cardPlayed);
            output.add(cardPlayed);
            output.add(cardPlayed.getSuit());
            return output;
        }
        return null;
    }
}

