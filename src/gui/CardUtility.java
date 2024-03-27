package gui;

import logic.Card;

import javax.swing.*;

public class CardUtility {

    public static JButton createCardButton(Card card, int cardWidth, int cardHeight) {
        JButton cardButton = new JButton();
//         Set the icon for the card
        ImageIcon icon = ImageUtility.loadAndScaleCardImage(card.getFilepath(), cardWidth, cardHeight, false);
        cardButton.setIcon(icon);

        cardButton.setName(card.getValue() + "_" + card.getSuit().toString());
        cardButton.setBorderPainted(false);
        cardButton.setContentAreaFilled(false);
        cardButton.setFocusPainted(false);
        cardButton.setOpaque(false);

        return cardButton;
    }

    public static JLabel createCardLabel(Card card, int cardWidth, int cardHeight, boolean isVertical) {
        JLabel cardLabel = new JLabel();
        ImageIcon icon = ImageUtility.loadAndScaleCardImage(card.getFilepath(), cardWidth, cardHeight, isVertical);
        cardLabel.setIcon(icon);
        return cardLabel;
    }
}
