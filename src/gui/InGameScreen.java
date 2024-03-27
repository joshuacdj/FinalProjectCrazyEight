package gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.Border;

import app.Controller;
import logic.*;
import static gui.Sound.*;

public class InGameScreen extends JPanel {
    private final JPanel CENTER_PANEL;
    private final JLayeredPane LAYERED_PANE;
    private final Round ROUND;
    private final DiscardPile DISCARD_PILE;
    private final Map<String, JPanel> PANEL_MAP = new HashMap<>();
    private final JLabel DISCARD_PILE_LABEL = new JLabel();
    private final DrawPile DRAW_PILE;
    private JButton drawPileButton;
    private final Controller CONTROLLER;
    private boolean cardAlreadyPlayedByHuman = false;

//    CONSTANTS
    private final Color DARK_GREEN = new Color(0x00512C); // Light green
    private final Color LIGHT_GREEN = new Color(0, 153, 76); // Dark green for contrast
    private static final Dimension LAYERED_PANE_DIMENSION = new Dimension(830, 300);
    private static final Dimension HELP_BUTTON_DIMENSION = new Dimension(120, 30);
    private static final Dimension CARD_DIMENSION = new Dimension(110, 160);
    private static final Dimension SUIT_BUTTON_DIMENSION = new Dimension(140, 70);
    private static final Font PLAYER_NAME_DIMENSION = new Font("Arial", Font.BOLD, 22);
    private static final int CARD_XOFFSET = 20;
    public InGameScreen(Round ROUND, Controller CONTROLLER) {

        this.ROUND = ROUND;
        this.CONTROLLER = CONTROLLER;
        DISCARD_PILE = ROUND.getDiscardPile();
        DRAW_PILE = ROUND.getDrawPile();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        // Initialize the layered pane
        LAYERED_PANE = new JLayeredPane();
        LAYERED_PANE.setPreferredSize(LAYERED_PANE_DIMENSION);

        // Add a component listener to adjust bounds dynamically
        LAYERED_PANE.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ensure the CENTER_PANEL fills the entire layered Pane
                CENTER_PANEL.setBounds(0, 0, LAYERED_PANE.getWidth(), LAYERED_PANE.getHeight());
                // Adjust the "Play card?" button position dynamically
                adjustSuitsButtonPosition();
            }
        });

        // Create and add CENTER_PANEL to layered pane
        CENTER_PANEL = createCENTER_PANEL();
        LAYERED_PANE.add(CENTER_PANEL, JLayeredPane.DEFAULT_LAYER);

        // add the helpButton
        JButton helpButton = new JButton("How To Play");
        // creating the size of the helpButton
        helpButton.setBounds(0,0,HELP_BUTTON_DIMENSION.width,HELP_BUTTON_DIMENSION.height);
        helpButton.addActionListener(e -> {
                welcomeClickSound();
                HelpFrame helpWindow = HelpFrame.getInstance();
                helpWindow.setVisible(true);
        });

        LAYERED_PANE.add(helpButton,Integer.valueOf(1)); // TODO: yo what this do cuh
        LAYERED_PANE.moveToFront(helpButton);

        // Setup the GridBagConstraints for layered pane
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 2; // Adjust based on your requirement
        gbc.weighty = 1; // Adjust based on your requirement
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(LAYERED_PANE, gbc);

        // North player panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel northPanel = createComputerPanel("North");
        add(northPanel, gbc);
        PANEL_MAP.put("North", northPanel);

        // South player panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        JPanel southPanel = createHumanPanel();
        add(southPanel, gbc);
        PANEL_MAP.put("South", southPanel);

        // East player panel
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel eastPanel = createComputerPanel("East");
        add(eastPanel, gbc);
        PANEL_MAP.put("East", eastPanel);

        // West player panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel westPanel = createComputerPanel("West");
        add(westPanel, gbc);
        PANEL_MAP.put("West", westPanel);

        // Reset the weights back to 1 for any future components that may be added
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }

    // Create gradient background for InGameScreen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0, 0, DARK_GREEN, 0, getHeight(), LIGHT_GREEN);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void adjustSuitsButtonPosition() {
        // Find the suits button and adjust its position
        for (Component comp : LAYERED_PANE.getComponents()) {
            if (comp instanceof JButton playButton) {
                switch (((JButton) comp).getText()) {
                    case "♠", "♥", "♣", "♦" -> playButton.setBounds(LAYERED_PANE.getWidth() - 110,
                                            LAYERED_PANE.getHeight() - 40,100, 30);
                }
            }
        }
    }

    private JPanel createComputerPanel(String orientation) {
        // Create a JPanel with custom painting and layout
        JPanel computerPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Determine the computer's name based on orientation
                String name = switch (orientation) {
                    case "West" -> "COMP 1";
                    case "North" -> "COMP 2";
                    case "East" -> "COMP 3";
                    default -> "";
                };

                // Set the font and color for the name
                g2d.setFont(PLAYER_NAME_DIMENSION);
                g2d.setColor(Color.WHITE);

                // Get the FontMetrics for calculating text width and height
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(name, g2d);

                int x, y;

                if ("East".equals(orientation)) {
                    // For East orientation
                    g2d.translate(fm.getFont().getSize(), getHeight() / 2);
                    g2d.rotate(-Math.PI / 2);
                    x = (int) (-textBounds.getWidth() / 2);
                    y = 0;
                } else if ("West".equals(orientation)) {
                    // For West orientation
                    g2d.translate(getWidth() - fm.getFont().getSize(), getHeight() / 2);
                    g2d.rotate(Math.PI / 2);
                    x = (int) (-textBounds.getWidth() / 2);
                    y = 0;
                } else {
                    // For North orientation
                    g2d.translate((getWidth() + textBounds.getWidth()) / 2 , getHeight() - fm.getFont().getSize());
                    g2d.rotate(Math.PI);
                    x = 0;
                    y = 0;
                }

                // Draw the string such that it is centered on the panel
                g2d.drawString(name, x, y);
                g2d.dispose();
            }

            @Override
            public void doLayout() {
                setupCardLabels(this, orientation);
            }
        };
        computerPanel.setOpaque(false);

        Border roundedBorder = new RoundedBorder(20, Color.white, 3);
        computerPanel.setBorder(roundedBorder);

        // Setup card labels for the computer panel
        setupCardLabels(computerPanel, orientation);

        return computerPanel;
    }

    private JPanel createHumanPanel() {
        // Create a panel with custom painting for the human player
        JPanel humanPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set the color and font for the text
                g.setFont(PLAYER_NAME_DIMENSION);
                g.setColor(Color.WHITE);

                // Calculate the position of the text to center it in the panel
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                String text = "YOU";
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = metrics.getHeight();

                // Draw the text
                g.drawString(text, x, y);
            }
        };
        humanPanel.setOpaque(false);

        Border roundedBorder = new RoundedBorder(20, Color.ORANGE, 3);
        humanPanel.setBorder(roundedBorder);
        // Add a component listener to resize the card buttons when the panel is resized
        humanPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setupCardButtons(humanPanel);
            }
        });

        // Initial card buttons setup
        setupCardButtons(humanPanel);
        updateDrawPileButton();

        return humanPanel;
    }

    public void setCardAlreadyPlayedByHumanToFalse() {
        cardAlreadyPlayedByHuman = false;
    }

    private void setupCardButtons(JPanel panel) {
//        TODO: can remove panel argument since it will always be referring to south panel
        // Clear existing card buttons from the panel
        panel.removeAll();
        panel.setLayout(null);

        // Obtain the human's hand of cards
        List<Card> humanHand = ROUND.getListOfPlayers().getFirst().getHand();

        // Obtain the number of cards in the human's hand
        int numCards = humanHand.size();

        int panelHeight = panel.getHeight();

        // Determine the y offset for card positioning
        int yOffset = panelHeight - CARD_DIMENSION.height;

        for(int i = 0; i < numCards; i++){
            // Determine the x offset for card positioning
            int xOffset = CARD_XOFFSET * i;
            Card card = humanHand.get(i);
            JButton cardButton = ButtonUtility.createCardButton(card, CARD_DIMENSION.width, CARD_DIMENSION.height);
            addCardButtonListeners(cardButton, card, panel);
            cardButton.setBounds(xOffset, yOffset, CARD_DIMENSION.width, CARD_DIMENSION.height);
            panel.add(cardButton);
            panel.setComponentZOrder(panel.getComponent(i), 0);
        }

        // Refresh the panel to display the new buttons
        panel.revalidate();
        panel.repaint();
    }

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
        // If player has already played a card, do not allow another card to be played
        if (cardAlreadyPlayedByHuman) {
            return;
        }

        Player currentPlayer = ROUND.getListOfPlayers().getFirst();
        boolean cardIsPlayable = currentPlayer.isPlayable(selectedCard, DISCARD_PILE.getTopCard());

        // Only "play" the card if the card is playable
        if (cardIsPlayable) {
            cardAlreadyPlayedByHuman = true;
            currentPlayer.getHand().remove(selectedCard);
            DISCARD_PILE.addCard(selectedCard);
            updateDiscardPileImage();
            dealCardSound();
            // Refresh the card buttons to reflect the current hand after a card is played
            setupCardButtons(panel);
            if(currentPlayer.getHand().isEmpty()){
                // Player has won; end the game
                CONTROLLER.endGame();
                return;
            }
            if (selectedCard.getValue() == 8) {
                // Display all the suit buttons for the player to choose from
                showSuitsButton();
            } else {
                // Let the computers play their turn
                CONTROLLER.compPlay();
            }
        }
    }

    private void showSuitsButton() {
        final Suit[] suits = {Suit.DIAMONDS, Suit.CLUBS, Suit.HEARTS, Suit.SPADES};

        // Calculate spacing based on the total button width
        int totalButtonWidth = suits.length * SUIT_BUTTON_DIMENSION.width;
        int spacing = (LAYERED_PANE.getWidth() - totalButtonWidth) / (suits.length + 1);

        // Y position for all buttons
        int buttonY = LAYERED_PANE.getHeight() - SUIT_BUTTON_DIMENSION.height - 10;

        // Clear previous suit buttons if they exist
        clearSuitButtons();

        // Create and add buttons for each suit
        for (int i = 0; i < suits.length; i++) {
            int buttonX = spacing + (i * (SUIT_BUTTON_DIMENSION.width + spacing));
            JButton suitButton = createSuitButton(suits[i], buttonX, buttonY, SUIT_BUTTON_DIMENSION.width, SUIT_BUTTON_DIMENSION.height);
            LAYERED_PANE.add(suitButton, Integer.valueOf(2));
            LAYERED_PANE.moveToFront(suitButton);
        }

        LAYERED_PANE.revalidate();
        LAYERED_PANE.repaint();
    }

    private void clearSuitButtons() {
        // List of suit symbols as strings to check against existing buttons
        String[] suitSymbols = {"♦", "♣", "♥", "♠"};
        for (Component comp : LAYERED_PANE.getComponents()) {
            if (comp instanceof JButton) {
                String buttonText = ((JButton) comp).getText();
                if (Arrays.asList(suitSymbols).contains(buttonText)) {
                    LAYERED_PANE.remove(comp);
                }
            }
        }
    }

    private JButton createSuitButton(Suit suit, int x, int y, int width, int height) {
        JButton button = new JButton(suitSymbol(suit));
        button.setFont(new Font("Dialog", Font.BOLD, 30));

        switch (suit) {
            case Suit.DIAMONDS, Suit.HEARTS -> button.setForeground(Color.RED);
        }
        button.setBounds(x, y, width, height);

        // Add the action listener to handle the suit selection
        button.addActionListener(e -> updateGameAfterSuitSelected(suit));

        return button;
    }

    private String suitSymbol(Suit suit) {
        return switch (suit) {
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case HEARTS -> "♥";
            case SPADES -> "♠";
        };
    }

    private void updateGameAfterSuitSelected(Suit suit) {
        // Logic to update the game state after a suit is selected
        clearSuitButtons();
        DISCARD_PILE.setTopCard(new Card(0, suit));
        updateDiscardPileImage();
        dealCardEightSound();
        CONTROLLER.compPlay();
        LAYERED_PANE.revalidate();
        LAYERED_PANE.repaint();
    }

    private void setupCardLabels(JPanel panel, String orientation) {
        // Obtain the computer's hand based on their panel orientation
        List<Card> computerHand = switch (orientation) {
            case "West" -> ROUND.getListOfPlayers().get(1).getHand();
            case "North" -> ROUND.getListOfPlayers().get(2).getHand();
            case "East" -> ROUND.getListOfPlayers().get(3).getHand();
            default -> null;
        };

        // Ensure computer's hand is not null
        assert computerHand != null;
        int numCards = computerHand.size();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LAST_LINE_END;

        boolean isVertical = "East".equals(orientation) || "West".equals(orientation);

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        // Adjust the card dimensions based on the orientation
        int cardWidth = isVertical ? (int)CARD_DIMENSION.getHeight(): (int)CARD_DIMENSION.getWidth();
        int cardHeight = isVertical ? (int)CARD_DIMENSION.getWidth(): (int)CARD_DIMENSION.getHeight();

        // Set the initial offset for the first card
        int xOffset = 0;
        int yOffset = 0;

        for (int i = 0; i < numCards; i++) {
            // Create label method
            JLabel cardLabel = LabelUtility.createCardLabel(computerHand.get(i), cardWidth, cardHeight, isVertical);

            ImageIcon icon = ImageUtility.loadAndScaleCardImage("images/back_card.png", cardWidth, cardHeight, isVertical);
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

            // Add the card label to the panel
            panel.add(cardLabel, gbc);
        }

        panel.revalidate();
        panel.repaint();
    }

    private JPanel createCENTER_PANEL() {

        JPanel CENTER_PANEL = new JPanel(new GridLayout(1, 3, 0, 0)); // 1 row, 2 columns with a gap of 10px
        GridBagConstraints gbc = new GridBagConstraints();
        CENTER_PANEL.setOpaque(false);

        // Draw pile button
        // Example of adding a component to the CENTER_PANEL
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed

        drawPileButton = ButtonUtility.createDrawPileButton(CARD_DIMENSION.width, CARD_DIMENSION.height);
        gbc.gridx = 0;
        CENTER_PANEL.add(drawPileButton, gbc);

        // Prepare discard pile icon and label, and place it within a panel for centering
        String filePath = DISCARD_PILE.getCards().getLast().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        DISCARD_PILE_LABEL.setIcon(discardPileIcon);

        // Use GridBagLayout for auto-centering within the panel
        JPanel discardPilePanel = new JPanel(new GridBagLayout());
        discardPilePanel.setOpaque(false);

        // This will center the label within the discardPilePanel
        discardPilePanel.add(DISCARD_PILE_LABEL);
        gbc.gridx = 2;
        CENTER_PANEL.add(discardPilePanel, gbc);

        // Ensuring CENTER_PANEL and its components resize within the JLayeredPane
        CENTER_PANEL.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                CENTER_PANEL.revalidate();
                CENTER_PANEL.repaint();
            }
        });
        return CENTER_PANEL;
    }

    public void updateDiscardPileImage() {
        // This method updates the image of the discardPile
        String filePath = DISCARD_PILE.getTopCard().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        DISCARD_PILE_LABEL.setIcon(discardPileIcon);
        DISCARD_PILE_LABEL.revalidate();
        DISCARD_PILE_LABEL.repaint();
    }


    public void restockDrawPile() {
        // This method will check if the drawPile has less than or equal to 5 cards, if so, add the discardPile to the drawPile and then shuffle the drawPile
        final int MINUMUMDRAWPILESIZE = 5;
        if (DRAW_PILE.getListOfCards().size() <= MINUMUMDRAWPILESIZE) {
            DISCARD_PILE.transferTo(DRAW_PILE);
            DRAW_PILE.shuffleDeck();
        }
    }

    public String determineOrientation(Player player) {
        int playerIndex = ROUND.getListOfPlayers().indexOf(player);
        return switch (playerIndex) {
            case 1 -> "West";
            case 2 -> "North";
            case 3 -> "East";
            default -> "South";
        };
    }

    public void refreshPlayerPanel(String orientation) {
        JPanel playerPanel = PANEL_MAP.get(orientation);
        if (playerPanel != null) {
            // Clear the panel before re-adding updated content
            playerPanel.removeAll();
            if ("South".equals(orientation)) {
                // Update player panels for human players
                setupCardButtons(playerPanel);
            } else {
                // Update computer player panels
                setupCardLabels(playerPanel, orientation);
            }
            // Reposition labels or buttons as needed
            playerPanel.revalidate();
            playerPanel.repaint();
        }
    }

    private ActionListener getDrawListener(Human humanPlayer) {
        return e -> {
            if (humanPlayer.canDrawCard() && drawPileButton.isEnabled()) {
                restockDrawPile();
                // Execute draw logic
                humanPlayer.drawCard(DRAW_PILE);
                // Update the player's hand display
                refreshPlayerPanel("South");
                // Re-evaluate conditions after drawing a card
                updateDrawPileButton();
                drawCardSound();
            }
        };
    }

    public void updateDrawPileButton() {
        Human humanPlayer = (Human) ROUND.getListOfPlayers().getFirst();
        // Set the human player's playable cards based on the top card of the discard pile
        humanPlayer.setPlayableCards(DISCARD_PILE.getTopCard());
        ActionListener drawListener = getDrawListener(humanPlayer);
        // Remove all previous action listeners to prevent stacking
        for(ActionListener al : drawPileButton.getActionListeners()) {
            drawPileButton.removeActionListener(al);
        }

        // Check if the human player has playable cards
        if (!humanPlayer.getPlayableCards().isEmpty()) {
            // Human has playable cards, disable the draw pile button
            humanPlayer.resetDrawCounter();
            drawPileButton.setEnabled(false);
        } else if (humanPlayer.canDrawCard()) {
            // Human can draw a card, enable the draw pile button
            drawPileButton.setEnabled(true);
            drawPileButton.addActionListener(drawListener);
        }else {
            drawPileButton.setEnabled(false);
            // Check if the human player has no playable cards and has drawn the maximum number of cards
            if (humanPlayer.getPlayableCards().isEmpty()) {
                humanPlayer.resetDrawCounter();
                CONTROLLER.compPlay();
            }
        }

        drawPileButton.revalidate();
        drawPileButton.repaint();
    }

    public void displayWinPanel() {
        List<Player> sortedPlayers = getSortedPlayersByHandValue();
        JPanel winPanel= WinPanel.winPanel(LAYERED_PANE.getWidth(), LAYERED_PANE.getHeight(), sortedPlayers);

        // "Play Again?" button
        JButton playAgainButton = ButtonUtility.createCustomButton("Play Again", 300, 60);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            stopSound("youWin");
            stopSound("youLose");
            welcomeClickSound();
            CONTROLLER.startNewGame();
        });

        // "Close Game" button
        JButton closeGameButton = ButtonUtility.createCustomButton("Quit Game", 300, 60);
        closeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeGameButton.addActionListener(e -> {
            welcomeClickSound();
            // Display a confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit the game?",
                    "Quit Game",
                    // Option type (Yes/No)
                    JOptionPane.YES_NO_OPTION,
                    // Message type
                    JOptionPane.QUESTION_MESSAGE
            );

            // Check if the user confirmed
            if (confirm == JOptionPane.YES_OPTION) {
                // User clicked "YES", so exit the game
                System.exit(0);
            }
            // If "NO" or closed dialog, do nothing and return to the game
        });

        // Add buttons to win panel
        // Spacer
        winPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        winPanel.add(playAgainButton);
        // Spacer
        winPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        winPanel.add(closeGameButton);

        // Add the win panel to the layered pane and make it visible
        // Ensuring it's at the top layer
        LAYERED_PANE.add(winPanel, Integer.valueOf(3));
        winPanel.setBounds(0, 0, LAYERED_PANE.getWidth(), LAYERED_PANE.getHeight());

        // Disable all interactions
        disableInteractions();

        LAYERED_PANE.revalidate();
        LAYERED_PANE.repaint();
    }

    private void disableInteractions() {
        // Remove listeners from all player panels to prevent interaction
        PANEL_MAP.values().forEach(this::removePanelListeners);
        // Disable the draw pile button
        drawPileButton.setEnabled(false);
    }

    private void removePanelListeners(JPanel panel) {
        // Remove all mouse listeners from the panel's components
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton button) {
                Arrays.stream(button.getMouseListeners()).forEach(button::removeMouseListener);
            }
        }
    }

    private List<Player> getSortedPlayersByHandValue() {
        // Clone the list of players to avoid modifying the original list
        List<Player> sortedPlayers = new ArrayList<>(ROUND.getListOfPlayers());
        // Sort the cloned list based on hand value in ascending order
        sortedPlayers.sort(Comparator.comparingInt(Player::calculatePoints).thenComparing(Player::getHandSize));
        return sortedPlayers;
    }

    public void highlightPlayerTurn(String orientation) {
        SwingUtilities.invokeLater(() -> {
            // Reset all panels to the default background first
            PANEL_MAP.values().forEach(panel -> panel.setBorder(new RoundedBorder(20, Color.white, 3)));

            // Now highlight the active player's panel
            if (orientation != null) {
                JPanel activePanel = PANEL_MAP.get(orientation);
                if (activePanel != null) {
                    // Set to a yellow border to indicate active player
                    activePanel.setBorder(new RoundedBorder(20, Color.orange, 3)); // Optional: Add a black border for emphasis
                }
            }

            // Refresh the panels
            PANEL_MAP.values().forEach(panel -> {
                panel.revalidate();
                panel.repaint();
            });
        });
    }
}
