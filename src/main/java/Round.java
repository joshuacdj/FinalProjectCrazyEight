package main.java;

import main.java.Card;
import main.java.DiscardPile;
import main.java.DrawPile;
import main.java.Player;

import java.util.ArrayList;

public class Round {
    private ArrayList<Player> listOfPlayers;
    private DrawPile drawPile;
    private DiscardPile discardPile;
    private int cardsDrawnInTurn = 0;

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

    public int getCardsDrawnInTurn() {
        return cardsDrawnInTurn;
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public void setListOfPlayers(ArrayList<Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    public void setDrawPile(DrawPile drawPile) {
        this.drawPile = drawPile;
    }

    public void setDiscardPile(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    public void setCardsDrawnInTurn(int cardsDrawnInTurn) {
        this.cardsDrawnInTurn = cardsDrawnInTurn;
    }

    // set the first card of the round
    public void setFirstCard(DrawPile drawPile, DiscardPile discardPile) {

        // obtain starting card
        Card startingCard = drawPile.getTopCard();

        // if starting card drawn is 8, put it back into the pile and draw a new starting card
        while (startingCard.getValue() == 8) {
            drawPile.getListOfCards().addFirst(startingCard);
            startingCard = drawPile.getTopCard();
        }

        // add starting card to discard pile
        discardPile.addCard(startingCard);
    }

    public void clearAllPlayerHands() {
        for (Player p : listOfPlayers) {
            p.clearHand();
        }
    }

    public void roundStart() {

        // shuffle a new deck
        drawPile.shuffleDeck();

        // set the first playing card of the game
        setFirstCard(drawPile, discardPile);

        for (int i = 0; i < 5; i++) {
            for (Player p : listOfPlayers) {
                p.drawCard(drawPile.getTopCard());
            }
        }
    }

    public boolean roundEnd() {

        // Initialise round end to be false
        boolean shouldRoundEnd = false;

        // Loop through every player's hand and check if it is empty
        // If it is empty, set round end to true and break out of loop
        for (Player p : listOfPlayers) {
            if (p.getHand().isEmpty()) {
                shouldRoundEnd = true;
                break;
            }
        }

        // If the round ends, clear every player's hand and reset the draw and discard pile
        if (shouldRoundEnd) {
            for (Player p : listOfPlayers) {
                p.clearHand();
            }

            this.drawPile = new DrawPile();
            this.discardPile = new DiscardPile();
        }

        return shouldRoundEnd;
    }
}
