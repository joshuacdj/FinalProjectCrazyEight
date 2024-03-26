package GUI;

public class WIPInGameScreen {
    private void setupAndPositionCardLabels(JPanel panel, String orientation) {
        List<Card> computerHand = switch (orientation) {
            case "West" -> round.getListOfPlayers().get(1).getHand();
            case "North" -> round.getListOfPlayers().get(2).getHand();
            case "East" -> round.getListOfPlayers().get(3).getHand();
            default -> null;
        };

        int numCards = computerHand.size();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LAST_LINE_END;

        boolean isVertical = "East".equals(orientation) || "West".equals(orientation);

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        // Adjust the card dimensions based on the orientation
        int cardWidth = isVertical ? 160 : 110;
        int cardHeight = isVertical ? 110 : 160;
        int overlap = cardWidth / 2;

        // Set the initial offset for the first card
        int xOffset = 0;
        int yOffset = 0;

        for (int i = 0; i < numCards; i++) {
            // create label method
            JLabel cardLabel = createCardLabel(computerHand.get(i), cardWidth, cardHeight, isVertical);

            ImageIcon icon = loadAndScaleCardImage("images/back_card.png", cardWidth, cardHeight, isVertical);
            cardLabel.setIcon(icon);

            // Adjust the offset for the north panel to position cards at the top right
            if ("North".equals(orientation)) {
                xOffset = panelWidth - cardWidth - (cardWidth - 90) * i;
            }

            // Adjust the xOffset for the east panel to position cards from the bottom right
            if ("East".equals(orientation)) {
                xOffset = panelWidth - cardWidth;
                yOffset = panelHeight - cardHeight - (cardHeight - 90) * i;
            }

            if ("West".equals(orientation)) {
                yOffset = (cardHeight - 90) * i;
            }

            // Set the bounds for the label based on the orientation
            cardLabel.setBounds(xOffset, yOffset, cardWidth, cardHeight);

            panel.add(cardLabel, gbc);
        }

        panel.revalidate();
        panel.repaint();
    }

    private JLabel createCardLabel(Card card, int cardWidth, int cardHeight, boolean isVertical) {
        JLabel cardLabel = new JLabel();
        ImageIcon icon = loadAndScaleCardImage(card.getFilepath(), cardWidth, cardHeight, isVertical);
        cardLabel.setIcon(icon);
        return cardLabel;
    }
}
