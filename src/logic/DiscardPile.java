package logic;

import java.util.*;

public class DiscardPile implements Deck {
    private ArrayList<Card> listOfCards = new ArrayList<>();
    private Card topCard;

    public ArrayList<Card> getCards(){
        return listOfCards;
    }

    public void addCard(Card c) {
        listOfCards.add(c);
        topCard = c;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public Card getTopCard(){
        return topCard;
    }

    @Override
    public void shuffleDeck() {
        Collections.shuffle(listOfCards);
    }

    public void transferTo(DrawPile drawPile) {
        for(int i = 0; i < listOfCards.size() - 1; i++){
            drawPile.getListOfCards().addFirst(listOfCards.getFirst());
            listOfCards.removeFirst();
        }
    }
}
