package GUI;

import main.java.*;


import java.sql.SQLOutput;
import java.util.concurrent.atomic.*;
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
import javax.sound.sampled.*;

import static GUI.Sound.*;

public class InGameScreen extends JPanel {
    private JPanel centerPanel; // Instance variable for the center panel
    private JLayeredPane layeredPane;
    private Round round;
    private DiscardPile discardPile;
    private Map<String, JPanel> panelMap = new HashMap<>();
    private JLabel discardPileLabel = new JLabel();
    private DrawPile drawPile;

    private JButton drawPileButton;

    private Controller controller;
    private int cardsPlayed = 0;
    private boolean gameEnd = false;

    public InGameScreen(Round round, Controller controller) {
        this.round = round;
        this.controller = controller;
        discardPile = round.getDiscardPile();
        drawPile = round.getDrawPile();
//        System.out.println("-----------");
//        System.out.println(discardPile.getCards());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        // Set the weights to distribute extra space equally among all components
        gbc.weightx = 1.0; // Equal horizontal weight
        gbc.weighty = 1.0; // Equal vertical weight

        // Initialize the layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 300)); // Adjust according to your layout

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

        // Setup the GridBagConstraints for layeredPane
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1; // Adjust based on your requirement
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

    private void adjustPlayCardButtonPosition() {
        // Find the "Play card?" button and adjust its position


        for (Component comp : layeredPane.getComponents()) {
            if (comp instanceof JButton) {
                JButton playButton = (JButton) comp;
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

    private JPanel createPlayerPanel(String orientation) {
        JPanel playerPanel = new JPanel(null) { // Use null layout for absolute positioning
//            @Override
//            public void doLayout() {
//                positionCardButtons(this, orientation);
//            }
        };
        playerPanel.setOpaque(false);
        playerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Remove later

        // Add a component listener to resize the card buttons when the panel is resized
        playerPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionCardButtons(playerPanel, orientation);
            }
        });

        // Initial card buttons setup
        setupCardButtons(playerPanel);
        updateDrawPileButton();

        return playerPanel;
    }

    private JPanel createComputer1Panel(String orientation) {
        JPanel computer1Panel = new JPanel(null) { // Use null layout for absolute positioning
            @Override
            public void doLayout() {
                positionCardLabel(this, orientation);
            }
        };
        computer1Panel.setOpaque(false);
        computer1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Remove later

        // Initial card buttons setup
        setupCardLabel(computer1Panel, orientation);

        return computer1Panel;
    }

    private void setupSuitButtons(JLayeredPane centerPanel) {
        int numSuits = 4;

        for (int i = 0; i < numSuits; i ++) {
            JButton suitButton = new JButton();
            suitButton.setBorderPainted(false);
            suitButton.setContentAreaFilled(false);
            suitButton.setFocusPainted(false);
            suitButton.setOpaque(false);

            centerPanel.add(suitButton);

            suitButton.addMouseListener(new MouseAdapter() {

            });

        }
    }

    public void setCardsPlayed(int num) {
        cardsPlayed = num;
    }

    public void setGameEnd(boolean bool) {
        gameEnd = bool;
    }


    private void setupCardButtons(JPanel panel) {
        int numCards = round.getListOfPlayers().getFirst().getHand().size(); // The number of cards to display
        round.getListOfPlayers().get(0).setPlayableCards(discardPile.getTopCard());
//        updateDrawPileButton();
//        MouseListener listener = new MouseAdapter(){
//            public void mousePressed(MouseEvent e) {
//
//                //TODO THE GET FIRST WILL CHANGE IF WE HAVE MULTIPLE ROUNDS
//                //This will draw a card from the drawpile for the player
//                round.getListOfPlayers().getFirst().getHand().add(drawPile.getTopCard());
//
//                //Keep a counter of the amount of cards drawn, skip the players turn if 5 cards are drawn
//                int cardsDrawn = round.getCardsDrawnInTurn();
//                cardsDrawn++;
//                round.setCardsDrawnInTurn(cardsDrawn);
//                System.out.println(cardsDrawn);
//                if (cardsDrawn == 5) {
//                    System.out.println("YOU HAVE DRAWN 5 CARDS. TOO BAD");
//                }
//
//                //Check if the drawpile has sufficient cards for the next player and restock if necessary
//                restockDrawPile();
//
//                //Update the hand graphics
//                JPanel south = panelMap.get("South");
//                setupCardButtons(south);
//                positionCardButtons(south,"South");
//            }
//        };
//        if(round.getListOfPlayers().get(0).getPlayableCards().size() != 0){
//            drawPileButton.setEnabled(false);
//            drawPileButton.removeMouseListener(listener);
//        }else {
//            drawPileButton.setEnabled(true);
//            drawPileButton.addMouseListener(listener);
//        }
        System.out.println(numCards);
        List<Card> currentHand = round.getListOfPlayers().getFirst().getHand();

        for (int i = 0; i < numCards; i++) {
            JButton cardButton = new JButton(); // Create button without icon initially
            cardButton.setBorderPainted(false);
            cardButton.setContentAreaFilled(false);
            cardButton.setFocusPainted(false);
            cardButton.setOpaque(false);

            // Add the card button to the panel
            panel.add(cardButton);
            
            cardButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    cardButton.setLocation(cardButton.getX(), cardButton.getY() + 40);
                    // Card is not raised, raise it up
                }
                @Override
                public void mouseEntered (MouseEvent e) {
                        // Card is already raised, lower it back down
                        cardButton.setLocation(cardButton.getX(), cardButton.getY() - 40);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (cardsPlayed != 0) {
                        return;
                    }
                    //Initialise the cards the human is able to play
                    //First get the player whose turn it is
                    Player currentPlayer = round.getListOfPlayers().get(0);
                    //Get the current topcard
                    Card currentTopCard = discardPile.getTopCard();
                    currentPlayer.getPlayableCards();

                    //Initialise the card being clicked
                    String[] s = cardButton.getName().split("_");
                    int value = Integer.parseInt(s[0]);
                    Suit suit = Suit.valueOf(s[1]);
                    Card chosenCard = new Card(value, suit);
                    //Check whether the card clicked is playable
                    boolean cardIsPlayable = currentPlayer.isPlayable(chosenCard, currentTopCard);

                    // remove card from hand if it is a valid card
                    if (cardIsPlayable) {
                        cardsPlayed++;
                        for (Card c : currentHand) {
                            if (chosenCard.equals(c)) {
                                panelMap.get("South").removeAll();
                                currentHand.remove(c);
                                discardPile.addCard(c);
                                updateDiscardPileImage();
                                dealCardSound();
                                panelMap.get("South").revalidate();
                                panelMap.get("South").repaint();
                                setupCardButtons(panelMap.get("South"));
                                positionCardButtons(panelMap.get("South"), "South");
                                if (currentPlayer.getHand().size() == 0) {
                                    gameEnd = true;
                                    controller.endGame();
                                    cardButton.removeMouseListener(this);
                                    return;
                                }
                                //DEBUGGING PRINT STATEMENTS
                                System.out.println("top card is " + discardPile.getTopCard());
                                System.out.println("after adding card");
                                System.out.println(currentHand);
                                break;
                            }
                        }
                        if (chosenCard.getValue() == 8) {
                            showSuitsButton();
                            return;
                        }

                    } else {
                        System.out.println("INVALID CARD");
                        return;
                    }

//                    if (chosenCard.getValue() == 8) {
//                        System.out.println("tempcard before " + discardPile.getTopCard());
//                        showSuitsButton();
//                    }

                    System.out.println("tempcard after " + discardPile.getTopCard());
                    controller.compPlay();
                }
                private void showSuitsButton() {
                    // Method to show the "Play card?" button within the layeredPane
                    JButton play1Button = new JButton("♦");
                    play1Button.setFont(new Font("♦", Font.BOLD, 30));
                    play1Button.setBounds(layeredPane.getWidth() - 670, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right


                    JButton play2Button = new JButton("♣");
                    play2Button.setFont(new Font("♣", Font.BOLD, 30));
                    play2Button.setBounds(layeredPane.getWidth() - 530, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right


                    JButton play3Button = new JButton("♥");
                    play3Button.setFont(new Font("♥", Font.BOLD, 30));
                    play3Button.setBounds(layeredPane.getWidth() - 390, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right


                    JButton play4Button = new JButton("♠");
                    play4Button.setFont(new Font("♠", Font.BOLD, 30));
                    play4Button.setBounds(layeredPane.getWidth() - 250, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right


                    // removing of all buttons once a suit is clicked
                    play1Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(0, Suit.DIAMONDS));
                        updateDiscardPileImage();
                        dealCardEightSound();
                        controller.compPlay();
                    });

                    play2Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(0, Suit.CLUBS));
                        updateDiscardPileImage();
                        dealCardEightSound();
                        controller.compPlay();
                    });

                    play3Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(0, Suit.HEARTS));
                        updateDiscardPileImage();
                        dealCardEightSound();
                        controller.compPlay();
                    });

                    play4Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(0, Suit.SPADES));
                        updateDiscardPileImage();
                        dealCardEightSound();
                        controller.compPlay();
                    });

                    // Ensure any existing play button is removed before adding a new one
                    for (Component comp : layeredPane.getComponents()) {
                        if (comp instanceof JButton && "♦".equals(((JButton) comp).getText())) {
                            layeredPane.remove(comp);
                        }
                        if (comp instanceof JButton && "♣".equals(((JButton) comp).getText())) {
                            layeredPane.remove(comp);
                        }
                        if (comp instanceof JButton && "♥".equals(((JButton) comp).getText())) {
                            layeredPane.remove(comp);
                        }
                        if (comp instanceof JButton && "♠".equals(((JButton) comp).getText())) {
                            layeredPane.remove(comp);
                        }
                    }

                    layeredPane.add(play1Button, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play2Button, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play3Button, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play4Button, Integer.valueOf(2)); // Add playButton above centerPanel

                    layeredPane.moveToFront(play1Button);
                    layeredPane.moveToFront(play2Button);
                    layeredPane.moveToFront(play3Button);
                    layeredPane.moveToFront(play4Button);


                    layeredPane.revalidate();
                    layeredPane.repaint();
                }

            });
        }
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

        for (int i = 0; i < numCards; i++) {
            JLabel cardLabel = new JLabel(); // Create button without icon initially

            // Add the card button to the panel
            panel.add(cardLabel);
        }
    }

    private void positionCardButtons(JPanel panel, String orientation) {

        //Initialise a listofcards to be a hand
        /*
        * NOTE HUMAN MAY NOT ALWAYS BE FIRST PLAYER IN LIST OF PLAYERS
        * The index will change after every round
        */
        List<Card> humanHand = round.getListOfPlayers().getFirst().getHand();

        int numCards = humanHand.size();
        if (numCards == 0) return;

        boolean isVertical = "East".equals(orientation) || "West".equals(orientation);

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        // Adjust the card dimensions based on the orientation
        int cardWidth = isVertical ? panelHeight/5 : panelWidth / 8;
        int cardHeight = isVertical ? panelWidth - 20 : panelHeight - 20;
        int overlap = cardWidth / 2;

        if ("South".equals(orientation) || "North".equals(orientation)) {
            cardWidth = 110;
            cardHeight = 160;
        }
        if ("East".equals(orientation) || "West".equals(orientation)) {
            cardWidth = 160;
            cardHeight = 110;
        }


        // For vertical panels, adjust the height for stacking with overlap
        if (isVertical) {
            overlap = cardHeight / 2;
//            cardHeight = (panelHeight + (overlap * (5 - 1))) / numCards;
        }

        // Set the initial offset for the first card
        int xOffset = 0;
        int yOffset = panelHeight - cardHeight;

        if ("South".equals(orientation)) {
            for (int i = 0; i < humanHand.size(); i++) {
                //TODO! Figure out why there are 2x print
                JButton cardButton = (JButton) panel.getComponent(i);
                cardButton.setName("" + humanHand.get(i).getValue() + "_" + humanHand.get(i).getSuit());
                ImageIcon icon = loadAndScaleCardImage(humanHand.get(i).getFilepath(), cardWidth, cardHeight, isVertical);

                cardButton.setIcon(icon);
                cardButton.setBorderPainted(false);
                cardButton.setContentAreaFilled(false);
                cardButton.setFocusPainted(false);
                cardButton.setOpaque(false);

            // Position the cards with proper offset
                xOffset = (cardWidth - 90) * i;


            // Set the bounds for the button based on the orientation
            cardButton.setBounds(xOffset, yOffset, cardWidth, cardHeight);

            // Make sure that the cards added on later are set to the front to overlap nicely
                panel.setComponentZOrder(panel.getComponent(i), 0);
            }
        }


        panel.revalidate();
        panel.repaint();
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
            System.out.println("someone won");;
        }

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

            for ( int i = 0; i < numCards; i++) {
                JLabel back_card = (JLabel) panel.getComponent(i);
                ImageIcon icon = loadAndScaleCardImage("src/main/resources/images/back_card.png", cardWidth, cardHeight, isVertical);
                back_card.setIcon(icon);
                if (!isVertical) {
                    xOffset = (cardWidth - 90)*i;
                } else {
                    yOffset = (cardHeight - 90)*i;
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
//        JPanel centerPanel = new JPanel(new GridBagLayout()); // Using GridBagLayout for flexibility
//        centerPanel.setOpaque(false); // Set based on your UI design
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;

        JPanel centerPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 row, 2 columns with a gap of 10px
        centerPanel.setOpaque(false);

        //Get the filepath of the first discarded card at the start of the round
        String filePath = discardPile.getCards().getLast().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        discardPileLabel.setIcon(discardPileIcon);
        discardPileLabel.setBorder(BorderFactory.createEmptyBorder(0, 120, 0, 0));

        // drawpilebutton
        // Example of adding a component to the centerPanel
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed
        ImageIcon drawPileIcon = new ImageIcon(new ImageIcon("src/main/resources/images/back_card.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        drawPileButton = new JButton(drawPileIcon);
        centerPanel.add(drawPileButton);
        centerPanel.add(discardPileLabel);

        // Make buttons transparent
        drawPileButton.setBorder(BorderFactory.createEmptyBorder());
        drawPileButton.setContentAreaFilled(false);
//        discardPileLabel.setBorder(BorderFactory.createEmptyBorder());
//        discardPileLabel.setContentAreaFilled(false);

        // If you have specific components to add, replicate the above block adjusting gbc as needed

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
//        JButton discardPileButton = panelMap.get("Center");
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
        switch (playerIndex) {
            case 1:
                return "West";
            case 2:
                return "North";
            case 3:
                return "East";
            default:
                return "South"; // Default or error case
        }
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
                setupCardButtons(playerPanel); // If this method sets up the human player's cards
                positionCardButtons(playerPanel, "South");
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

        if(humanPlayer.getPlayableCards().size() != 0){
            humanPlayer.resetDrawCounter();
            drawPileButton.setEnabled(false);
        }else if (humanPlayer.canDrawCard()){
            drawPileButton.setEnabled(true);
            drawPileButton.addActionListener(drawListener);
        }else if(!humanPlayer.canDrawCard()){
            drawPileButton.setEnabled(false);
            if(humanPlayer.getPlayableCards().size() == 0){
                humanPlayer.resetDrawCounter();
                controller.compPlay();
            }
        }

        System.out.println("Human has " + humanPlayer.getPlayableCards().size() + " cards to play");
        System.out.println("Human hand is " + humanPlayer.getHand());
        System.out.println("Top card is " + discardPile.getTopCard());
        System.out.println(humanPlayer.getPlayableCards());

        // Enable or disable the button based on the player's ability to draw a card
//        drawPileButton.setEnabled(humanPlayer.getPlayableCards().isEmpty() && humanPlayer.canDrawCard());
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

//    public void displayWinPanel() {
//        // Step 1: Create the win panel
//        JPanel winPanel = new JPanel();
//        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
//        winPanel.setSize(layeredPane.getSize());
//        winPanel.setOpaque(true);
//        winPanel.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent background
//
//        // Title
//        JLabel titleLabel = new JLabel("Game Over - Leaderboard");
//        titleLabel.setForeground(Color.WHITE);
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        winPanel.add(titleLabel);
//
//        // Step 2: Populate the panel with scores
//        List<Player> sortedPlayers = new ArrayList<>(round.getListOfPlayers());
//        sortedPlayers.sort(Comparator.comparingInt(player -> player.calculatePoints()));
//        sortedPlayers.forEach(player -> {
//            player.addPoints(player.calculatePoints());
//            JLabel playerScoreLabel = new JLabel(player.getName() + ": " + player.getPoints());
//            playerScoreLabel.setForeground(Color.WHITE);
//            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            winPanel.add(playerScoreLabel);
//        });
//
//        // Additional styling for the winPanel can be added here, e.g., borders, fonts, etc.
//
//        // Step 3: Add the panel to the layeredPane
//        layeredPane.add(winPanel, Integer.valueOf(2)); // Adding at a high layer to cover other components
//
//        // Making the panel fill the entire layeredPane
//        winPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
//
//        // Step 4: This method should be called when the game ends to display the win panel
//        layeredPane.revalidate();
//        layeredPane.repaint();
//    }

    public void displayWinPanel() {
        // Step 1: Create the win panel with improved aesthetics
        JPanel winPanel = new JPanel();
        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
        winPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
        winPanel.setOpaque(true);
        winPanel.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent background
        winPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment for the box layout

        // Add some padding
        winPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Step 2: Populate the panel with sorted scores and rankings
        List<Player> sortedPlayers = getSortedPlayersByHandValue();
        AtomicInteger rank = new AtomicInteger(1); // For displaying player rankings
        sortedPlayers.forEach(player -> {
            String positionSuffix = getPositionSuffix(rank.get()); // Get the appropriate suffix for the position
            JLabel playerScoreLabel = new JLabel(rank.getAndIncrement() + positionSuffix + " - " + player.getName() + ": " + player.calculatePoints());
            playerScoreLabel.setForeground(Color.WHITE);
            playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Larger and bold font
            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            winPanel.add(playerScoreLabel);
        });

        // Remove listeners from all player panels to prevent interaction
        panelMap.values().forEach(this::removePanelListeners);

        // Add the win panel to the layeredPane and make it visible
        layeredPane.add(winPanel, Integer.valueOf(3)); // Ensuring it's at the top layer
        winPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void removePanelListeners(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                Arrays.stream(button.getMouseListeners()).forEach(button::removeMouseListener);
            }
        }
    }

    private String getPositionSuffix(int position) {
        switch (position) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }



    private List<Player> getSortedPlayersByHandValue() {
        // Clone the list of players to avoid modifying the original list
        List<Player> sortedPlayers = new ArrayList<>(round.getListOfPlayers());
        // Sort the cloned list based on hand value in ascending order
        sortedPlayers.sort(Comparator.comparingInt(player -> player.calculatePoints()));
        return sortedPlayers;
    }


//    public void onCardDrawn(Player player) {
//        SwingUtilities.invokeLater(() -> {
//            if (player instanceof Human) {
//                updateHumanPlayerPanel();
//            } else if (player instanceof Computer) {
//                // Assuming you have a way to get the correct orientation for this computer player
//                String orientation = determineOrientation(player);
//                updateComputerPlayerPanel(orientation);
//            }
//        });
//    }


}
