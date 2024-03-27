package logic;

public class Human extends Player {
    private int cardsDrawnThisTurn = 0;

    public Human(String name) {
        super(name);
    }

    public void drawCard(DrawPile drawPile) {
        // Ensure that the Player has not drawn more than the maximum number of cards (5) allowed in one turn
        if (cardsDrawnThisTurn < MAX_DRAW_COUNT) {
            Card drawnCard = drawPile.getTopCard(); // Assume DrawPile has a method to draw a card.
            this.getHand().add(drawnCard);
            cardsDrawnThisTurn++;
        }
    }

    public boolean canDrawCard() {
        // Check if the Player can still draw a card this turn
        return cardsDrawnThisTurn < MAX_DRAW_COUNT;
    }

    public void resetDrawCounter() {
        // Reset the Player's per turn draw counter to 0
        cardsDrawnThisTurn = 0;
    }


}
