package GUI;

import logic.Card;
import logic.Player;
import logic.Suit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import static gui.Sound.dealCardEightSound;
import static gui.Sound.dealCardSound;

public class WIPInGameScreen {
    private void setupAndPositionCardButtons(JPanel panel) {
        // Clear existing card buttons from the panel
        panel.removeAll();
        panel.setLayout(null);

        List<Card> humanHand = round.getListOfPlayers().getFirst().getHand();
        int numCards = humanHand.size();
        if (numCards == 0) return;

        int panelHeight = panel.getHeight();
        //change to const
        int cardWidth =  110; // Adjusted card width for better layout
        int cardHeight =  160; // Adjusted card height for better layout

        // Determine the starting x and y offset for card positioning
//        int xOffsetStart = (panelWidth - (numCards * cardWidth + (numCards - 1) * 10)) / 2;
        int yOffset = panelHeight - cardHeight; // Adjust yOffset for vertical orientation if necessary

        for(int i = 0; i < numCards; i++){
            int xOffset = (cardWidth - 90) * i;
            Card card = humanHand.get(i);
            JButton cardButton = createCardButton(card, cardWidth, cardHeight);
            addCardButtonListeners(cardButton, card, panel);
            cardButton.setBounds(xOffset, yOffset, cardWidth, cardHeight);
            panel.add(cardButton);
            panel.setComponentZOrder(panel.getComponent(i), 0);
        }

        // Refresh the panel to display the new buttons
        panel.revalidate();
        panel.repaint();
    }

    private JButton createCardButton(Card card, int cardWidth, int cardHeight) {
        JButton cardButton = new JButton();
//         Set the icon for the card
        ImageIcon icon = loadAndScaleCardImage(card.getFilepath(), cardWidth, cardHeight, false);
        cardButton.setIcon(icon);

        cardButton.setName(card.getValue() + "_" + card.getSuit().toString());
        cardButton.setBorderPainted(false);
        cardButton.setContentAreaFilled(false);
        cardButton.setFocusPainted(false);
        cardButton.setOpaque(false);

        return cardButton;
    }
    //
    private void addCardButtonListeners(JButton cardButton, Card card, JPanel panel) {
        cardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                // Card is not raised, lower it back down
                cardButton.setLocation(cardButton.getX(), cardButton.getY() + 40);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Card is already raised, raise it up
                cardButton.setLocation(cardButton.getX(), cardButton.getY() - 40);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleCardSelection(card, panel);
            }
        });
    }

    private void handleCardSelection(Card selectedCard, JPanel panel) {
        if (cardPlayedByHuman) {
            return;
        }

        Player currentPlayer = round.getListOfPlayers().get(0);
        boolean cardIsPlayable = currentPlayer.isPlayable(selectedCard, discardPile.getTopCard());

        if (cardIsPlayable) {
            cardPlayedByHuman = true;
            currentPlayer.getHand().remove(selectedCard);
            discardPile.addCard(selectedCard);
            updateDiscardPileImage();
            dealCardSound();
            if (selectedCard.getValue() == 8) {
                showSuitsButton();
            } else {
                controller.compPlay();
            }
            // Refresh the card buttons to reflect the current hand after a card is played
            setupAndPositionCardButtons(panel);
        } else {
            System.out.println("INVALID CARD");
            // Optionally, show an error message or some feedback
        }
    }

    private void showSuitsButton() {
        // Constants for button sizing and layout
        final int buttonWidth = 140;
        final int buttonHeight = 70;
        final Suit[] suits = {Suit.DIAMONDS, Suit.CLUBS, Suit.HEARTS, Suit.SPADES};

        // Calculate spacing based on the total button width
        int totalButtonWidth = suits.length * buttonWidth;
        int spacing = (layeredPane.getWidth() - totalButtonWidth) / (suits.length + 1);

        int buttonY = layeredPane.getHeight() - buttonHeight - 10; // Y position for all buttons

        // Clear previous suit buttons if they exist
        clearSuitButtons();

        // Create and add buttons for each suit
        for (int i = 0; i < suits.length; i++) {
            int buttonX = spacing + (i * (buttonWidth + spacing));
            JButton suitButton = createSuitButton(suits[i], buttonX, buttonY, buttonWidth, buttonHeight);
            layeredPane.add(suitButton, Integer.valueOf(2));
            layeredPane.moveToFront(suitButton);
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void clearSuitButtons() {
        // List of suit symbols as strings to check against existing buttons
        String[] suitSymbols = {"♦", "♣", "♥", "♠"};
        for (Component comp : layeredPane.getComponents()) {
            if (comp instanceof JButton) {
                String buttonText = ((JButton) comp).getText();
                if (Arrays.asList(suitSymbols).contains(buttonText)) {
                    layeredPane.remove(comp);
                }
            }
        }
    }

    private JButton createSuitButton(Suit suit, int x, int y, int width, int height) {
        JButton button = new JButton(suitSymbol(suit));
        button.setFont(new Font("Dialog", Font.BOLD, 30));
        button.setBounds(x, y, width, height);

        button.addActionListener(e -> {
            updateGameAfterSuitSelected(suit);
        });

        return button;
    }

    private String suitSymbol(Suit suit) {
        switch (suit) {
            case DIAMONDS: return "♦";
            case CLUBS: return "♣";
            case HEARTS: return "♥";
            case SPADES: return "♠";
            default: return "?";
        }
    }

    private void updateGameAfterSuitSelected(Suit suit) {
        // Logic to update the game state after a suit is selected
        clearSuitButtons();
        discardPile.setTopCard(new Card(0, suit));
        updateDiscardPileImage();
        dealCardEightSound();
        controller.compPlay();
        layeredPane.revalidate();
        layeredPane.repaint();
    }

}
