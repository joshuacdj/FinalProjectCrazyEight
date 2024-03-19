package main.java;

import main.java.Card;
import main.java.DrawPile;

import java.util.*;

public class Human extends Player {

    public Human(String name) {
        super(name);
    }

    public ArrayList action(Card topCard, DrawPile deck) {
        // Take in user input
        Scanner sc = new Scanner(System.in);

        int cardsDrawn = 0;
//        set list of playable cards
        setPlayableCards(topCard);
//        Handles cases where I do not have a playable card
        while (getPlayableCards().isEmpty() && cardsDrawn < 5) {

            System.out.print("Input Action: Draw or Play?");

            // Get user input
            String a = sc.nextLine();

            if (a.equals("Draw")) {
                sc.nextLine();
                drawCard(deck.getTopCard());
                setPlayableCards(topCard);
                cardsDrawn++;
            } else {
                System.out.println("No playable cards, please input draw only");
            }

        }

        // If I have a playable card
        if (cardsDrawn <= 5) {

            boolean validAction = true;
            ArrayList<Object> output = new ArrayList<>();

            // Can only play and not draw
            do {
                System.out.println("Input Action: Draw or Play? ");

                String a = sc.nextLine();

                if (!a.equals("Play")) {
                    System.out.println("Please input play");
                    validAction = false;
                } else {
                    validAction = true;
                }

            } while (!validAction);

            // Print the available cards in hand
            System.out.println("Cards in hand:");
            for (Card c : getHand()) {
                System.out.println(c);
            }

            // Print the playable cards
            System.out.println("Playable Cards:");
            for (Card c : getPlayableCards()) {
                System.out.println(c);
            }


            while (true) {
                // Get value
                System.out.print("Choose a value: ");
                int value = Integer.parseInt(sc.nextLine());

                // Get suit
                System.out.print("Choose a suit: ");
                String s = sc.nextLine();
                Suit suit = Suit.valueOf(s);

                Card inputCard = new Card(value, suit);

                for (Card c : getPlayableCards()) {

                    if (c.equals(inputCard)) {
                        removeCard(c);
                        output.add(c);
//                        if card is an 8, additionally ask player for suit
                        if (c.getValue() == 8) {
                            System.out.println("Choose a suit: ");
                            Suit desiredSuit = Suit.valueOf(sc.nextLine());
                            output.add(desiredSuit);
                        }
                        else {
                            output.add(c.getSuit());
                        }
                        return output;
                    }
                }
            }


        }
        return null;
    }

    // Testing Code
//    public static void main(String[] args) {
//
//        ArrayList<Card> hand = new ArrayList<>();
//        Card c1 = new Card(3, Suit.DIAMONDS);
//        Card c2 = new Card(4, Suit.HEARTS);
//        Card c3 = new Card(7, Suit.CLUBS);
//        Card c4 = new Card(8, Suit.HEARTS);
//        Card c5 = new Card(5, Suit.SPADES);
//
//        hand.add(c1);
//        hand.add(c2);
//        hand.add(c3);
//        hand.add(c4);
//        hand.add(c5);
//
//        Human h = new Human(hand, "tommy");
//
//        DrawPile rp =  new DrawPile();
//
//        rp.getListOfCards().remove(c1);
//        rp.getListOfCards().remove(c2);
//        rp.getListOfCards().remove(c3);
//        rp.getListOfCards().remove(c4);
//        rp.getListOfCards().remove(c5);
//
//        h.action(rp.getTopCard(), rp);
//    }
}
