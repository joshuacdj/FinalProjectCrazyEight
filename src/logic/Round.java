package logic;

import java.util.*;

public class Round {
    private static final int STARTINGDRAWCOUNT = 5;
    private ArrayList<Player> listOfPlayers;
    private DrawPile drawPile;
    private DiscardPile discardPile;

    public Round() {
        listOfPlayers = new ArrayList<>();
        listOfPlayers.add(new Human("You"));
        listOfPlayers.add(new Computer("Comp 1"));
        listOfPlayers.add(new Computer("Comp 2"));
        listOfPlayers.add(new Computer("Comp 3"));
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public void setFirstCard(DrawPile drawPile, DiscardPile discardPile) {
        // Obtain starting card
        Card startingCard = drawPile.getTopCard();

        // If starting card drawn is 8, put it back into the pile and draw a new starting card
        while (startingCard.getValue() == 8) {
            drawPile.getListOfCards().addFirst(startingCard);
            startingCard = drawPile.getTopCard();
        }

        // Add starting card to discard pile
        discardPile.addCard(startingCard);
    }

    public void roundStart() {
        // Shuffle a new deck
        drawPile.shuffleDeck();

        // Set the first playing card of the game
        setFirstCard(drawPile, discardPile);

        // Each player will draw 5 cards to start
        for (int i = 0; i < STARTINGDRAWCOUNT; i++) {
            for (Player p : listOfPlayers) {
                p.drawCard(drawPile.getTopCard());
            }
        }
    }
}

