package logic;

import java.util.*;
import java.util.Collections;

public class DrawPile implements Deck {
    private final static int CARDSPERSUIT = 13;
    private ArrayList<Card> listOfCards;

    public DrawPile() {
        
        listOfCards = new ArrayList<>();
        for (int i = 1; i <= CARDSPERSUIT; i++) {
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
        //attain top card (i.e. last of list)
        Card c = listOfCards.getLast();
        //remove card from the DrawPile
        listOfCards.removeLast();
        return c;
    }

    public void add(Card c){
        listOfCards.add(c);
    }


    @Override
    public void shuffleDeck(){
        Collections.shuffle(listOfCards);
    }
}
