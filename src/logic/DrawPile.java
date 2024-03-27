package logic;

import java.util.*;
import java.util.Collections;

public class DrawPile implements Deck {
    private final static int CARDS_PER_SUIT = 13;
    private ArrayList<Card> listOfCards;

    public DrawPile() {
        // Initialise the deck of 52 cards, 13 per suit
        listOfCards = new ArrayList<>();
        for (int i = 1; i <= CARDS_PER_SUIT; i++) {
            for (Suit suit : Suit.values()) {
                listOfCards.add(new Card(i, suit));
            }
        }
    }

    public ArrayList<Card> getListOfCards() {
        return listOfCards;
    }

    public Card getCard(){
        return listOfCards.getFirst();
    }

    public Card getTopCard(){
        // Attain top card (i.e. last of list)
        Card c = listOfCards.getLast();
        // Remove card from the DrawPile
        listOfCards.removeLast();
        return c;
    }

    public void add(Card c){
        // Add card to the top of the drawPile
        listOfCards.add(c);
    }


    @Override
    public void shuffleDeck(){
        // Shuffle the drawPile
        Collections.shuffle(listOfCards);
    }
}
