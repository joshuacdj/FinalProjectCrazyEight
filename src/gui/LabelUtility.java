package gui;

import logic.Card;

import javax.swing.*;

public class LabelUtility {

    public static JLabel createCardLabel(Card card, int cardWidth, int cardHeight, boolean isVertical) {
        JLabel cardLabel = new JLabel();
        ImageIcon icon = ImageUtility.loadAndScaleCardImage(card.getFilepath(), cardWidth, cardHeight, isVertical);
        cardLabel.setIcon(icon);
        return cardLabel;
    }
}
