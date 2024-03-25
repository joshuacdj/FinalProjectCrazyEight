package main.java;

public class Human extends Player {

    private int cardsDrawnThisTurn = 0;

    public Human(String name) {
        super(name);
    }

    public void drawCard(DrawPile drawPile) {
        if (cardsDrawnThisTurn < 5) {
            Card drawnCard = drawPile.getTopCard(); // Assume DrawPile has a method to draw a card.
            this.getHand().add(drawnCard);
            cardsDrawnThisTurn++;
        }
    }

    public boolean canDrawCard() {
        return cardsDrawnThisTurn < 5;
    }

    public void resetDrawCounter() {
        cardsDrawnThisTurn = 0;
    }

    public int getCardsDrawnThisTurn() {
        return cardsDrawnThisTurn;
    }

}
