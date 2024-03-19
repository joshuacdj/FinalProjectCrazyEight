package main.java;

import main.java.Card;
import main.java.DiscardPile;
import main.java.DrawPile;
import main.java.Player;

import java.util.ArrayList;

public class Round {
    private ArrayList<Player> playerPosition;
    private DrawPile drawPile;
    private DiscardPile discardPile;

    public Round(ArrayList<Player> playerPosition) {
        this.playerPosition = playerPosition;
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
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

    public void checkPileSize(DrawPile drawPile, DiscardPile discardPile) {
        if (drawPile.getNoOfCardsRemaining() <= 5) {
            discardPile.transferTo(drawPile);
        }
    }

    public void clearAllPlayerHands() {
        for (Player p : playerPosition) {
            p.clearHand();
        }
    }

    public void roundStart() {

        // shuffle a new deck
        drawPile.shuffleDeck();

        // set the first playing card of the game
        setFirstCard(drawPile, discardPile);

        // each of the 4 players draws 5 cards
        for (int i = 0; i < 5; i++) {
            for (Player p : playerPosition) {
                p.drawCard(drawPile.getTopCard());
            }
        }

        // each player goes through their turns until the round ends this is going to take forever!
        while (!roundEnd()){
            for (Player currentPlayer : playerPosition) {
                // current player makes his move
                // TODO: Implement player play himself
//                Card cardPlayed = currentPlayer.play();

                checkPileSize(drawPile, discardPile);

                // Add the played card to the discard pile only if it is not null aka skip turn
//                if (cardPlayed != null) {
//                    discardPile.addCard(cardPlayed);
//                }

                // check if player's hand is 0 to break out of the for loop
                if (roundEnd()) {
                    break;
                }
            }
        }

        // At the end of the round, we sum up the points of each player and clear their hand
        // Loop through each player
        for (Player p : playerPosition) {

            // Sum up the total points for each player
            p.addPoints(p.calculatePoints());
        }

        //This will clear every player's hand at the end of the round
        clearAllPlayerHands();
    }

    public boolean roundEnd() {

        // Initialise round end to be false
        boolean shouldRoundEnd = false;

        // Loop through every player's hand and check if it is empty
        // If it is empty, set round end to true and break out of loop
        for (Player p : playerPosition) {
            if (p.getHand().isEmpty()) {
                shouldRoundEnd = true;
                break;
            }
        }

        // If the round ends, clear every player's hand and reset the draw and discard pile
        if (shouldRoundEnd) {
            for (Player p : playerPosition) {
                p.clearHand();
            }

            this.drawPile = new DrawPile();
            this.discardPile = new DiscardPile();
        }

        return shouldRoundEnd;
    }
}
