package logic;

import java.util.*;
import java.util.Collections;

public class DrawPile implements Deck {
    private ArrayList<Card> listOfCards;

    public DrawPile() {
        listOfCards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            for (Suit suit : Suit.values()) {
                Card temp = new Card(i, suit);
                listOfCards.add(temp);
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
