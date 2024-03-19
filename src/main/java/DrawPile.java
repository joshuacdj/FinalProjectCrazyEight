package main.java;

import main.java.Card;
import main.java.Deck;

import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.util.*;
import java.util.Collections;

public class DrawPile implements Deck {
    private ArrayList<Card> listOfCards;

    public DrawPile() {
        listOfCards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            for (Suit suit : Suit.values()) {
                Card cardToAdd = new Card(i, suit);
                String filename = "src/main/resources/images/" + cardToAdd.getValue() + "_of_" + cardToAdd.getSuit().toString().toLowerCase() + ".png";
            }
        }
    }

    public ArrayList<Card> getListOfCards() {
        return listOfCards;
    }

    public Card getCard(){
        return listOfCards.getFirst();
    }

    public void add(Card c){
        listOfCards.add(c);
    }

    @Override
    public void shuffleDeck(){
        Collections.shuffle(listOfCards);
    }

    public Card getTopCard(){
        //attain top card (i.e. last of list)
        Card c = listOfCards.getLast();
        //remove card from the DrawPile
        listOfCards.removeLast();
        return c;
    }

    public int getNoOfCardsRemaining(){
        return listOfCards.size();
    }
}
