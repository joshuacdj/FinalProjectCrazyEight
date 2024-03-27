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
    private final JPanel centerPanel; // Instance variable for the center panel
    private final JLayeredPane layeredPane;
    private final Round round;
    private final DiscardPile discardPile;
    private final Map<String, JPanel> panelMap = new HashMap<>();
    private final JLabel discardPileLabel = new JLabel();
    private final DrawPile drawPile;
    private JButton drawPileButton;
    private final Controller controller;
    private boolean cardAlreadyPlayedByHuman = false;

//    CONSTANTS
    private final Color darkGreen= new Color(0x00512C); // Light green
    private final Color lightGreen = new Color(0, 153, 76); // Dark green for contrast
    private static final Dimension LAYEREDPANE_DIMENSION = new Dimension(830, 300);
    private static final Dimension HELPBUTTON_DIMENSION = new Dimension(120, 30);
    private static final Dimension CARD_DIMENSION = new Dimension(110, 160);
    private static final Dimension SUITBUTTON_DIMENSION = new Dimension(140, 70);
    private static final Font PLAYERNAME_FONT = new Font("Arial", Font.BOLD, 22);
    private static final int CARD_XOFFSET = 20;
    public InGameScreen(Round round, Controller controller) {

        this.round = round;
        this.controller = controller;
        discardPile = round.getDiscardPile();
        drawPile = round.getDrawPile();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        // Initialize the layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(LAYEREDPANE_DIMENSION);

        // Add a component listener to adjust bounds dynamically
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ensure the centerPanel fills the entire layeredPane
                centerPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                // Adjust the "Play card?" button position dynamically
                adjustSuitsButtonPosition();
            }
        });

        // Create and add centerPanel to layeredPane
        centerPanel = createCenterPanel();
        layeredPane.add(centerPanel, JLayeredPane.DEFAULT_LAYER);

        // add the helpButton
        JButton helpButton = new JButton("How To Play");
        // creating the size of the helpButton
        helpButton.setBounds(0,0,HELPBUTTON_DIMENSION.width,HELPBUTTON_DIMENSION.height);
        helpButton.addActionListener(e -> {
                welcomeClickSound();
                HelpFrame helpWindow = HelpFrame.getInstance();
                helpWindow.setVisible(true);
        });

        layeredPane.add(helpButton,Integer.valueOf(1)); // TODO: yo what this do cuh
        layeredPane.moveToFront(helpButton);

        // Setup the GridBagConstraints for layeredPane
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 2; // Adjust based on your requirement
        gbc.weighty = 1; // Adjust based on your requirement
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(layeredPane, gbc);

        // North player panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel northPanel = createComputerPanel("North");
        add(northPanel, gbc);
        panelMap.put("North", northPanel);

        // South player panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        JPanel southPanel = createHumanPanel();
        add(southPanel, gbc);
        panelMap.put("South", southPanel);

        // East player panel
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel eastPanel = createComputerPanel("East");
        add(eastPanel, gbc);
        panelMap.put("East", eastPanel);

        // West player panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel westPanel = createComputerPanel("West");
        add(westPanel, gbc);
        panelMap.put("West", westPanel);

        // Reset the weights back to 1 for any future components that may be added
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }

    // Create gradient background for InGameScreen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0, 0, darkGreen, 0, getHeight(), lightGreen);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void adjustSuitsButtonPosition() {
        // Find the suits button and adjust its position
        for (Component comp : layeredPane.getComponents()) {
            if (comp instanceof JButton playButton) {
                switch (((JButton) comp).getText()) {
                    case "♠", "♥", "♣", "♦" -> playButton.setBounds(layeredPane.getWidth() - 110,
                                            layeredPane.getHeight() - 40,100, 30);
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
                g2d.setFont(PLAYERNAME_FONT);
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
                g.setFont(PLAYERNAME_FONT);
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
        List<Card> humanHand = round.getListOfPlayers().getFirst().getHand();

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

        Player currentPlayer = round.getListOfPlayers().getFirst();
        boolean cardIsPlayable = currentPlayer.isPlayable(selectedCard, discardPile.getTopCard());

        // Only "play" the card if the card is playable
        if (cardIsPlayable) {
            cardAlreadyPlayedByHuman = true;
            currentPlayer.getHand().remove(selectedCard);
            discardPile.addCard(selectedCard);
            updateDiscardPileImage();
            dealCardSound();
            // Refresh the card buttons to reflect the current hand after a card is played
            setupCardButtons(panel);
            if(currentPlayer.getHand().isEmpty()){
                // Player has won; end the game
                controller.endGame();
                return;
            }
            if (selectedCard.getValue() == 8) {
                // Display all the suit buttons for the player to choose from
                showSuitsButton();
            } else {
                // Let the computers play their turn
                controller.compPlay();
            }
        }
    }

    private void showSuitsButton() {
        final Suit[] suits = {Suit.DIAMONDS, Suit.CLUBS, Suit.HEARTS, Suit.SPADES};

        // Calculate spacing based on the total button width
        int totalButtonWidth = suits.length * SUITBUTTON_DIMENSION.width;
        int spacing = (layeredPane.getWidth() - totalButtonWidth) / (suits.length + 1);

        // Y position for all buttons
        int buttonY = layeredPane.getHeight() - SUITBUTTON_DIMENSION.height - 10;

        // Clear previous suit buttons if they exist
        clearSuitButtons();

        // Create and add buttons for each suit
        for (int i = 0; i < suits.length; i++) {
            int buttonX = spacing + (i * (SUITBUTTON_DIMENSION.width + spacing));
            JButton suitButton = createSuitButton(suits[i], buttonX, buttonY, SUITBUTTON_DIMENSION.width, SUITBUTTON_DIMENSION.height);
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
        discardPile.setTopCard(new Card(0, suit));
        updateDiscardPileImage();
        dealCardEightSound();
        controller.compPlay();
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void setupCardLabels(JPanel panel, String orientation) {
        // Obtain the computer's hand based on their panel orientation
        List<Card> computerHand = switch (orientation) {
            case "West" -> round.getListOfPlayers().get(1).getHand();
            case "North" -> round.getListOfPlayers().get(2).getHand();
            case "East" -> round.getListOfPlayers().get(3).getHand();
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

    private JPanel createCenterPanel() {

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 0, 0)); // 1 row, 2 columns with a gap of 10px
        GridBagConstraints gbc = new GridBagConstraints();
        centerPanel.setOpaque(false);

        // drawpilebutton
        // Example of adding a component to the centerPanel
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed

        drawPileButton = ButtonUtility.createDrawPileButton(CARD_DIMENSION.width, CARD_DIMENSION.height);
        gbc.gridx = 0;
        centerPanel.add(drawPileButton, gbc);

        // Prepare discard pile icon and label, and place it within a panel for centering
        String filePath = discardPile.getCards().getLast().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);

        JPanel discardPilePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for auto-centering within the panel
        discardPilePanel.setOpaque(false);
        discardPilePanel.add(discardPileLabel); // This will center the label within the discardPilePanel
        gbc.gridx = 2;
        centerPanel.add(discardPilePanel, gbc);

        // Ensuring centerPanel and its components resize within the JLayeredPane
        centerPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });
        return centerPanel;
    }

    public void updateDiscardPileImage() {
        // This method updates the image of the discardPile
        String filePath = discardPile.getTopCard().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);
        discardPileLabel.revalidate();
        discardPileLabel.repaint();
    }


    public void restockDrawPile() {
        // This method will check if the drawPile has less than or equal to 5 cards, if so, add the discardPile to the drawPile and then shuffle the drawPile
        final int MINUMUMDRAWPILESIZE = 5;
        if (drawPile.getListOfCards().size() <= MINUMUMDRAWPILESIZE) {
            discardPile.transferTo(drawPile);
            drawPile.shuffleDeck();
        }
    }

    public String determineOrientation(Player player) {
        int playerIndex = round.getListOfPlayers().indexOf(player);
        return switch (playerIndex) {
            case 1 -> "West";
            case 2 -> "North";
            case 3 -> "East";
            default -> "South"; // Default or error case
        };
    }

    public void refreshPlayerPanel(String orientation) {
        JPanel playerPanel = panelMap.get(orientation);
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
                humanPlayer.drawCard(drawPile);
                // Update the player's hand display
                refreshPlayerPanel("South");
                // Re-evaluate conditions after drawing a card
                updateDrawPileButton();
                drawCardSound();
            }
        };
    }

    public void updateDrawPileButton() {
        Human humanPlayer = (Human) round.getListOfPlayers().getFirst();
        // Set the human player's playable cards based on the top card of the discard pile
        humanPlayer.setPlayableCards(discardPile.getTopCard());
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
                controller.compPlay();
            }
        }

//        System.out.println("Human has " + humanPlayer.getPlayableCards().size() + " cards to play");
//        System.out.println("Human hand is " + humanPlayer.getHand());
//        System.out.println("Top card is " + discardPile.getTopCard());
//        System.out.println(humanPlayer.getPlayableCards());

        drawPileButton.revalidate();
        drawPileButton.repaint();
    }

    public void displayWinPanel() {
        List<Player> sortedPlayers = getSortedPlayersByHandValue();
        JPanel winPanel= WinPanel.winPanel(layeredPane.getWidth(), layeredPane.getHeight(), sortedPlayers);

        // "Play Again?" button
        JButton playAgainButton = ButtonUtility.createCustomButton("Play Again", 300, 60);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            stopSound("youWin");
            stopSound("youLose");
            welcomeClickSound();
            controller.startNewGame();
        });

        // "Close Game" button
        JButton closeGameButton = ButtonUtility.createCustomButton("Quit Game", 300, 60);
        closeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeGameButton.addActionListener(e -> {
            welcomeClickSound();
            // Display a confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                    this, // Assuming 'inGameScreen' is the component you want to anchor the dialog to
                    "Are you sure you want to quit the game?", // The message to display
                    "Quit Game", // The title of the dialog window
                    JOptionPane.YES_NO_OPTION, // Option type (Yes/No)
                    JOptionPane.QUESTION_MESSAGE // Message type
            );

            // Check if the user confirmed
            if (confirm == JOptionPane.YES_OPTION) {
                // User clicked "YES", so exit the game
                System.exit(0);
            }
            // If "NO" or closed dialog, do nothing and return to the game
        });

        // Add buttons to win panel
        winPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        winPanel.add(playAgainButton);
        winPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        winPanel.add(closeGameButton);

        // Add the win panel to the layeredPane and make it visible
        layeredPane.add(winPanel, Integer.valueOf(3)); // Ensuring it's at the top layer
        winPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        // Step 4: Disable all interactions
        disableInteractions();

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void disableInteractions() {
        // Remove listeners from all player panels to prevent interaction
        panelMap.values().forEach(this::removePanelListeners);
        // Disable the draw pile button
        drawPileButton.setEnabled(false);
    }

    private void removePanelListeners(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton button) {
                Arrays.stream(button.getMouseListeners()).forEach(button::removeMouseListener);
            }
        }
    }

    private List<Player> getSortedPlayersByHandValue() {
        // Clone the list of players to avoid modifying the original list
        List<Player> sortedPlayers = new ArrayList<>(round.getListOfPlayers());
        // Sort the cloned list based on hand value in ascending order
        sortedPlayers.sort(Comparator.comparingInt(Player::calculatePoints).thenComparing(Player::getHandSize));
        return sortedPlayers;
    }

    public void highlightPlayerTurn(String orientation) {
        SwingUtilities.invokeLater(() -> {
            // Reset all panels to the default background first
            panelMap.values().forEach(panel -> panel.setBorder(new RoundedBorder(20, Color.white, 3)));

            // Now highlight the active player's panel
            if (orientation != null) {
                JPanel activePanel = panelMap.get(orientation);
                if (activePanel != null) {
                    // Set to a yellow border to indicate active player
                    activePanel.setBorder(new RoundedBorder(20, Color.orange, 3)); // Optional: Add a black border for emphasis
                }
            }

            // Refresh the panels
            panelMap.values().forEach(panel -> {
                panel.revalidate();
                panel.repaint();
            });
        });
    }
}
