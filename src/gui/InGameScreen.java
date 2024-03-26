package gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
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
    private boolean cardPlayedByHuman = false;
//    private boolean gameEnd = false;
    private final Color darkGreen= new Color(0x00512C); // Light green
    private final Color lightGreen = new Color(0, 153, 76); // Dark green for contrast

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
        layeredPane.setPreferredSize(new Dimension(830, 300)); // Adjust according to your layout

        // Add a component listener to adjust bounds dynamically
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ensure the centerPanel fills the entire layeredPane
                centerPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                // Adjust the "Play card?" button position dynamically
                adjustPlayCardButtonPosition();
            }
        });

        // Create and add centerPanel to layeredPane
        centerPanel = createCenterPanel();
        centerPanel.setBounds(0, 0, 600, 600); // Initial bounds, will adjust with componentListener
        layeredPane.add(centerPanel, JLayeredPane.DEFAULT_LAYER);

        // add the helpButton
        JButton helpButton = new JButton("How to play");
        // creating the size of the helpButton
        helpButton.setBounds(0,0,120,30);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help help = new Help();
                help.setVisible(true);
            }

        });

        layeredPane.add(helpButton,Integer.valueOf(1));
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
        JPanel northPanel = createComputer1Panel("North");
        add(northPanel, gbc);
        panelMap.put("North", northPanel);

        // South player panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel southPanel = createPlayerPanel("South");
        add(southPanel, gbc);
        panelMap.put("South", southPanel);

        // East player panel
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel eastPanel = createComputer1Panel("East");
        add(eastPanel, gbc);
        panelMap.put("East", eastPanel);

        // West player panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel westPanel = createComputer1Panel("West");
        add(westPanel, gbc);
        panelMap.put("West", westPanel);

        // Reset the weights back to 1 for any future components that may be added
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0, 0, darkGreen, 0, getHeight(), lightGreen);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void adjustPlayCardButtonPosition() {
        // Find the "Play card?" button and adjust its position

        for (Component comp : layeredPane.getComponents()) {
            if (comp instanceof JButton playButton) {
                if ("♦".equals(((JButton) comp).getText())) {
                    playButton.setBounds(layeredPane.getWidth() - 110, layeredPane.getHeight() - 40, 100, 30);
                } else if ("♣".equals(((JButton) comp).getText())) {
                    playButton.setBounds(layeredPane.getWidth() - 110, layeredPane.getHeight() - 40, 100, 30);
                } else if ("♥".equals(((JButton) comp).getText())) {
                    playButton.setBounds(layeredPane.getWidth() - 110, layeredPane.getHeight() - 40, 100, 30);
                } else if ("♠".equals(((JButton) comp).getText())) {
                    playButton.setBounds(layeredPane.getWidth() - 110, layeredPane.getHeight() - 40, 100, 30);
                }
            }
        }
    }

    private JPanel createComputer1Panel(String orientation) {
        // Create an anonymous subclass of JPanel with custom painting and layout
        JPanel computer1Panel = new JPanel(null) {
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
                Font font = new Font("Arial", Font.BOLD, 22);
                g2d.setFont(font);
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
                    // For vertical orientation, rotate the graphics object
                    g2d.translate(getWidth() - fm.getFont().getSize(), getHeight() / 2);
                    g2d.rotate(Math.PI / 2);
                    x = (int) (-textBounds.getWidth() / 2);
//                    y = (int) (textBounds.getHeight() / 2) - fm.getDescent();
                    y = 0;
                } else {
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
                positionCardLabel(this, orientation);
            }
        };
        computer1Panel.setOpaque(false);

        Border roundedBorder = new RoundedBorder(20, Color.white, 3);
        computer1Panel.setBorder(roundedBorder);

        // Setup card labels or any other initial setup
        setupCardLabel(computer1Panel, orientation);

        return computer1Panel;
    }

    private JPanel createPlayerPanel(String orientation) {
        JPanel playerPanel = new JPanel(null) { // Use null layout for absolute positioning
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set the color and font for the text
                g.setColor(Color.WHITE); // Set the text color
                g.setFont(new Font("Arial", Font.BOLD, 22)); // Set the text font and size

                // Calculate the position of the text to center it in the panel
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                String text = "YOU";
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = metrics.getHeight();

                // Draw the text
                g.drawString(text, x, y);
            }
        };
        playerPanel.setOpaque(false);

        Border roundedBorder = new RoundedBorder(20, Color.ORANGE, 3);
        playerPanel.setBorder(roundedBorder);
        // Add a component listener to resize the card buttons when the panel is resized
        playerPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setupAndPositionCardButtons(playerPanel);
            }
        });

        // Initial card buttons setup
        setupAndPositionCardButtons(playerPanel);
        updateDrawPileButton();

        return playerPanel;
    }

    public void setCardPlayedByHumanToFalse() {
        cardPlayedByHuman = false;
    }

//    public void setGameEnd(boolean bool) {
//        gameEnd = bool;
//    }


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

        switch(suit) {
            case Suit.HEARTS, Suit.DIAMONDS -> button.setForeground(Color.RED);
        }


        button.setBounds(x, y, width, height);

        button.addActionListener(e -> {
            updateGameAfterSuitSelected(suit);
        });

        return button;
    }

    private String suitSymbol(Suit suit) {
        return switch (suit) {
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case HEARTS -> "♥";
            case SPADES -> "♠";
            default -> "?";
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

    private void setupCardLabel(JPanel panel, String orientation) {
        int numCards;
        if(orientation.equals("West")){
            numCards = round.getListOfPlayers().get(1).getHand().size();
        }else if(orientation.equals("North")){
            numCards = round.getListOfPlayers().get(2).getHand().size();
        }else{
            numCards = round.getListOfPlayers().get(3).getHand().size();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        for (int i = 0; i < numCards; i++) {
            JLabel cardLabel = new JLabel(); // Create button without icon initially
            // Add the card button to the panel
            panel.add(cardLabel, gbc);
        }
    }

    private void positionCardLabel(JPanel panel, String orientation) {

        //numCards is dependent on the panel
        //East is 1, north is 2 and west is 3
        int numCards = switch (orientation) {
            case "East" -> round.getListOfPlayers().get(3).getHand().size();
            case "North" -> round.getListOfPlayers().get(2).getHand().size();
            case "West" -> round.getListOfPlayers().get(1).getHand().size();
            default -> 0;
        };

        if (numCards == 0){
            System.out.println("someone won");
        }

        boolean isVertical = "East".equals(orientation) || "West".equals(orientation);

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        // Adjust the card dimensions based on the orientation
        int cardWidth = isVertical ? 160 : 110;
        int cardHeight = isVertical ? 110 : 160;
//        int overlap = cardWidth / 2;

        // Set the initial offset for the first card
        int xOffset = 0;
        int yOffset = 0;

        for (int i = 0; i < numCards; i++) {
            JLabel back_card = (JLabel) panel.getComponent(i);
            ImageIcon icon = loadAndScaleCardImage("images/back_card.png", cardWidth, cardHeight, isVertical);
            back_card.setIcon(icon);

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

            // Set the bounds for the button based on the orientation
            back_card.setBounds(xOffset, yOffset, cardWidth, cardHeight);
        }
        
        panel.revalidate();
        panel.repaint();
    }

    private ImageIcon loadAndScaleCardImage(String imagePath, int targetWidth, int targetHeight, boolean isVertical) {
        try {
            // Load the original image from the specified path
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // If the panel is vertical, rotate the image first
            if (isVertical) {
                originalImage = rotateImage(originalImage, 90);
            }

            // Scale the original image to the new dimensions
            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            // Return the scaled image as an ImageIcon
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            // Return a placeholder if the image fails to load
            return new ImageIcon(new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB));
        }
    }

    private BufferedImage rotateImage(BufferedImage originalImage, double angle) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        // Calculate the rotation required and the center position of the original image
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);

        // move the image center to the same center position of the original image
        AffineTransform translationTransform;
        translationTransform = findTranslation(transform, width, height);
        transform.preConcatenate(translationTransform);

        g2d.drawImage(originalImage, transform, null);
        g2d.dispose();

        return rotatedImage;
    }

    private AffineTransform findTranslation(AffineTransform at, int width, int height) {
        Point2D p2din, p2dout;

        p2din = new Point2D.Double(0.0, 0.0);
        p2dout = at.transform(p2din, null);
        double ytrans = p2dout.getY();

        p2din = new Point2D.Double(0, height);
        p2dout = at.transform(p2din, null);
        double xtrans = p2dout.getX();

        AffineTransform tat = new AffineTransform();
        tat.translate(-xtrans, -ytrans);
        return tat;
    }


    private JPanel createCenterPanel() {

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 0, 0)); // 1 row, 2 columns with a gap of 10px
        GridBagConstraints gbc = new GridBagConstraints();
        centerPanel.setOpaque(false);

        // drawpilebutton
        // Example of adding a component to the centerPanel
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed
        ImageIcon drawPileIcon = new ImageIcon(new ImageIcon("images/back_card.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        drawPileButton = new JButton(drawPileIcon);
        // Make buttons transparent
        drawPileButton.setBorder(BorderFactory.createEmptyBorder());
        drawPileButton.setContentAreaFilled(false);
        gbc.gridx = 0;
        centerPanel.add(drawPileButton, gbc );

        // Action History Label


        // Prepare discard pile icon and label, and place it within a panel for centering
        String filePath = discardPile.getCards().getLast().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);

        JPanel discardPilePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for auto-centering within the panel
        discardPilePanel.setOpaque(false);
        discardPilePanel.add(discardPileLabel); // This will center the label within the discardPilePanel
        gbc.gridx = 2;
        centerPanel.add(discardPilePanel, gbc);

        // If you have a third component, add it here, or you can adjust the GridLayout and this method accordingly

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

    //This method is to update the image of the discardPile
    public void updateDiscardPileImage() {
        String filePath = discardPile.getTopCard().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);
        discardPileLabel.revalidate();
        discardPileLabel.repaint();
    }

    //This method will check if the drawpile has less than or equal to 5 cards, if so,
    //add the discardpile to the drawpile and then shuffle the drawpile
    public void restockDrawPile() {
        final int MINUMUMDRAWPILESIZE = 5;
        if (drawPile.getListOfCards().size() <= MINUMUMDRAWPILESIZE) {
            discardPile.transferTo(drawPile);
            drawPile.shuffleDeck();
        }
    }

    public String determineOrientation(Player player) {
        // Implement logic to determine the orientation based on your game's rules
        // This is a placeholder logic
        int playerIndex = round.getListOfPlayers().indexOf(player);
        return switch (playerIndex) {
            case 1 -> "West";
            case 2 -> "North";
            case 3 -> "East";
            default -> "South"; // Default or error case
        };
    }

    public void updateDrawCard(Player player) {
        SwingUtilities.invokeLater(() -> {
            String orientation = "North"; // Default, you might need to determine this based on the player
            JPanel panelToUpdate = null;

            // Determine the panel orientation based on the player
            if (player instanceof Computer) {
                // Assuming you have a way to determine the position of the computer player
                orientation = determineOrientation(player);
            }

            panelToUpdate = panelMap.get(orientation);

            // If no specific panel is found, exit the method
            if (panelToUpdate == null) return;

            // For simplicity, let's just re-setup the card labels for the updated panel
            if ("North".equals(orientation) || "East".equals(orientation) || "West".equals(orientation)) {
                setupCardLabel(panelToUpdate, orientation);
                positionCardLabel(panelToUpdate, orientation);
            }
        });
    }

    public void refreshPlayerPanel(String orientation) {
        // Assuming orientation is something like "North", "South", "East", "West"
        JPanel playerPanel = panelMap.get(orientation);
        if (playerPanel != null) {
            // Clear the panel before re-adding updated content
            playerPanel.removeAll();

            // Utilize existing setup methods based on orientation
            if ("South".equals(orientation)) {
                // Assuming South is always the human player in your game setup
                setupAndPositionCardButtons(playerPanel);
            } else {
                // For computer players
                setupCardLabel(playerPanel, orientation); // Reuse your method to set up computer player labels
                positionCardLabel(playerPanel, orientation); // Assuming this positions card JLabels
            }

            // Reposition labels or buttons as needed, potentially reusing existing logic

            playerPanel.revalidate();
            playerPanel.repaint();
        }
    }

    private ActionListener getDrawListener(Human humanPlayer) {
        return e -> {
            if (humanPlayer.canDrawCard() && drawPileButton.isEnabled()) {
                restockDrawPile();
                humanPlayer.drawCard(drawPile); // Execute draw logic
                refreshPlayerPanel("South"); // Update the player's hand display
                updateDrawPileButton(); // Re-evaluate conditions after drawing
                drawCardSound();
            }
        };
    }

    public void updateDrawPileButton() {
        if (drawPile.getListOfCards().isEmpty()) {
            System.out.println("Deck is empty");
        }
        Human humanPlayer = (Human) round.getListOfPlayers().getFirst();
        humanPlayer.setPlayableCards(discardPile.getTopCard());
        ActionListener drawListener = getDrawListener(humanPlayer);
        // Remove all previous action listeners to prevent stacking
        for(ActionListener al : drawPileButton.getActionListeners()) {
            drawPileButton.removeActionListener(al);
        }

        if(!humanPlayer.getPlayableCards().isEmpty()){
            humanPlayer.resetDrawCounter();
            drawPileButton.setEnabled(false);
        }else if (humanPlayer.canDrawCard()){
            drawPileButton.setEnabled(true);
            drawPileButton.addActionListener(drawListener);
        }else if(!humanPlayer.canDrawCard()){
            drawPileButton.setEnabled(false);
            if(humanPlayer.getPlayableCards().isEmpty()){
                humanPlayer.resetDrawCounter();
                controller.compPlay();
            }
        }

        System.out.println("Human has " + humanPlayer.getPlayableCards().size() + " cards to play");
        System.out.println("Human hand is " + humanPlayer.getHand());
        System.out.println("Top card is " + discardPile.getTopCard());
        System.out.println(humanPlayer.getPlayableCards());

        drawPileButton.revalidate();
        drawPileButton.repaint();
    }

    private Player findHumanPlayer() {
        // Example implementation, adjust based on your actual player management
        return round.getListOfPlayers().stream()
                .filter(p -> p instanceof Human)
                .findFirst()
                .orElse(null);
    }

    public void displayWinPanel() {
        // Step 1: Create the win panel with gradient background
        JPanel winPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(36, 11, 54);
                Color color2 = new Color(91, 76, 121);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
        winPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
        winPanel.setOpaque(false);
        winPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Step 2: Create and style the title
        JLabel titleLabel = new JLabel("Game Over - Leaderboard");
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        winPanel.add(titleLabel);

        int iconWidth = 30; // Example width
        int iconHeight = 30; // Example height

        // Step 3: Populate the panel with sorted scores and player rankings
        List<Player> sortedPlayers = getSortedPlayersByHandValue();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            String rankText = (i + 1) + getPositionSuffix(i + 1) + " - " + player.getName() + ": " + player.calculatePoints();
            JLabel playerScoreLabel = new JLabel(rankText);
            playerScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            playerScoreLabel.setForeground(Color.WHITE);
            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerScoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            if (i == 0) {
                playerScoreLabel.setIcon(scaleIcon("images/gold_medal.png", iconWidth, iconHeight));
            } else if (i == 1) {
                playerScoreLabel.setIcon(scaleIcon("images/silver_medal.png", iconWidth, iconHeight));
            } else if (i == 2) {
                playerScoreLabel.setIcon(scaleIcon("images/bronze_medal.png", iconWidth, iconHeight));
            }
            winPanel.add(playerScoreLabel);
        }
        // "Play Again?" button
        JButton playAgainButton = createCustomButton("Play Again", 300, 60);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            controller.startNewGame();
        });

        // "Close Game" button
        JButton closeGameButton = createCustomButton("Quit Game", 300, 60);
        closeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeGameButton.addActionListener(e -> {
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

    private ImageIcon scaleIcon(String path, int width, int height) {
        // Load the original image
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        // Scale it to fit the UI
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Return the new ImageIcon
        return new ImageIcon(scaledImage);
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

    private String getPositionSuffix(int position) {
        return switch (position) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private List<Player> getSortedPlayersByHandValue() {
        // Clone the list of players to avoid modifying the original list
        List<Player> sortedPlayers = new ArrayList<>(round.getListOfPlayers());
        // Sort the cloned list based on hand value in ascending order
        sortedPlayers.sort(Comparator.comparingInt(Player::calculatePoints));
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

    public JButton createCustomButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            // Custom painting is handled here
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set gradients or solid colors here
                if (getModel().isPressed()) {
                    // Gradient paint for pressed button
                    g2.setPaint(new GradientPaint(0, 0, new Color(180, 180, 180, 200),
                            0, getHeight(), new Color(150, 150, 150, 200)));
                } else if (getModel().isRollover()) {
                    // Gradient paint for hover button
                    g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 255),
                            0, getHeight(), new Color(220, 220, 220, 255)));
                } else {
                    // Default gradient paint
                    g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 200),
                            0, getHeight(), new Color(230, 230, 230, 200)));
                }

                // Draw the rounded rectangle button background
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();

                super.paintComponent(g);
            }

            // Method to paint border if needed
            @Override
            protected void paintBorder(Graphics g) {
                // Optional: Implement custom border painting if required
            }
        };

        button.setPreferredSize(new Dimension(width, height));
        button.setForeground(Color.DARK_GRAY); // Text color
        button.setFocusPainted(false); // Remove the focus border
        button.setContentAreaFilled(false); // Tell Swing to not fill the content area
        button.setOpaque(false); // Make the button non-opaque

        return button;
    }
}
