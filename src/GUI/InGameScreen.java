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
        playerPanel.setOpaque(true);
        playerPanel.setBackground(Color.YELLOW);
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
        computer1Panel.setOpaque(true);
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
//                    // Method to show the "Play card?" button within the layeredPane
//                    JButton play1Button = new JButton("♦");
//                    play1Button.setFont(new Font("♦", Font.BOLD, 30));
//                    play1Button.setBounds(layeredPane.getWidth() - 670, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
//
//
//                    JButton play2Button = new JButton("♣");
//                    play2Button.setFont(new Font("♣", Font.BOLD, 30));
//                    play2Button.setBounds(layeredPane.getWidth() - 530, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
//
//
//                    JButton play3Button = new JButton("♥");
//                    play3Button.setFont(new Font("♥", Font.BOLD, 30));
//                    play3Button.setBounds(layeredPane.getWidth() - 390, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
//
//
//                    JButton play4Button = new JButton("♠");
//                    play4Button.setFont(new Font("♠", Font.BOLD, 30));
//                    play4Button.setBounds(layeredPane.getWidth() - 250, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
//

                    int buttonWidth = 140;
                    int buttonHeight = 70;
                    int totalButtonWidth = 4 * buttonWidth; // For 4 buttons
                    int spacing = (layeredPane.getWidth() - totalButtonWidth) / 5; // Dividing the remaining space into 5 parts

                    JButton play1Button = new JButton("♦");
                    play1Button.setFont(new Font("♦", Font.BOLD, 30));
                    int play1ButtonX = spacing;
                    int buttonY = layeredPane.getHeight() - buttonHeight - 10; // Adjusting Y to position buttons at the bottom
                    play1Button.setBounds(play1ButtonX, buttonY, buttonWidth, buttonHeight);

                    JButton play2Button = new JButton("♣");
                    play2Button.setFont(new Font("♣", Font.BOLD, 30));
                    int play2ButtonX = play1ButtonX + buttonWidth + spacing;
                    play2Button.setBounds(play2ButtonX, buttonY, buttonWidth, buttonHeight);

                    JButton play3Button = new JButton("♥");
                    play3Button.setFont(new Font("♥", Font.BOLD, 30));
                    int play3ButtonX = play2ButtonX + buttonWidth + spacing;
                    play3Button.setBounds(play3ButtonX, buttonY, buttonWidth, buttonHeight);

                    JButton play4Button = new JButton("♠");
                    play4Button.setFont(new Font("♠", Font.BOLD, 30));
                    int play4ButtonX = play3ButtonX + buttonWidth + spacing;
                    play4Button.setBounds(play4ButtonX, buttonY, buttonWidth, buttonHeight);

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

        // drawpilebutton
        // Example of adding a component to the centerPanel
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed
        ImageIcon drawPileIcon = new ImageIcon(new ImageIcon("src/main/resources/images/back_card.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        drawPileButton = new JButton(drawPileIcon);
        // Make buttons transparent
        drawPileButton.setBorder(BorderFactory.createEmptyBorder());
        drawPileButton.setContentAreaFilled(false);
        centerPanel.add(drawPileButton);

        // Prepare discard pile icon and label, and place it within a panel for centering
        String filePath = discardPile.getCards().getLast().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);

        JPanel discardPilePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for auto-centering within the panel
        discardPilePanel.setOpaque(false);
        discardPilePanel.add(discardPileLabel); // This will center the label within the discardPilePanel
        centerPanel.add(discardPilePanel);

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

//    private JPanel createCenterPanel() {
//        JPanel centerPanel = new JPanel(new GridBagLayout());
//        centerPanel.setOpaque(false);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        // Get the filepath of the first discarded card at the start of the round
//        String filePath = discardPile.getCards().getLast().getFilepath();
//        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
//        discardPileLabel.setIcon(discardPileIcon);
//
//        // ImageIcon for draw pile
//        ImageIcon drawPileIcon = new ImageIcon(new ImageIcon("src/main/resources/images/back_card.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
//        drawPileButton = new JButton(drawPileIcon);
//        drawPileButton.setBorder(BorderFactory.createEmptyBorder());
//        drawPileButton.setContentAreaFilled(false);
//
//        // GridBagConstraints settings for draw pile button
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 0.5; // Give extra horizontal space
//        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
//        centerPanel.add(drawPileButton, gbc);
//
//        // GridBagConstraints settings for discard pile label
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0; // Make this occupy most of the space
//        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
//        centerPanel.add(discardPileLabel, gbc);
//
//        // If you have more components to add to the center panel, adjust the gridx and gridy accordingly
//
//        // Ensuring centerPanel and its components resize within the JLayeredPane
//        centerPanel.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                centerPanel.revalidate();
//                centerPanel.repaint();
//            }
//        });
//
//        return centerPanel;
//    }


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

//    public void displayWinPanel() {
//        // Step 1: Create the win panel with improved aesthetics
//        JPanel winPanel = new JPanel();
//        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
//        winPanel.setSize(layeredPane.getWidth(), layeredPane.getHeight());
//        winPanel.setOpaque(true);
//        winPanel.setBackground(new Color(32, 32, 32, 200)); // A darker semi-transparent background
//        winPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        // Step 2: Create a title label with larger font and style
//        JLabel titleLabel = new JLabel("Game Over - Leaderboard");
//        titleLabel.setForeground(new Color(255, 215, 0)); // Gold color for the title
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Some padding for the title
//
//        winPanel.add(titleLabel);
//
//        // Step 3: Populate the panel with sorted scores and rankings
//        List<Player> sortedPlayers = getSortedPlayersByHandValue();
//        for (int i = 0; i < sortedPlayers.size(); i++) {
//            Player player = sortedPlayers.get(i);
//            String rankText = (i + 1) + getPositionSuffix(i + 1) + " Place - " + player.getName() + ": " + player.calculatePoints();
//            JLabel playerScoreLabel = new JLabel(rankText);
//            playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
//            playerScoreLabel.setForeground(Color.WHITE);
//            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            winPanel.add(playerScoreLabel);
//        }
//
//        // Step 4: Add some styling and padding to the win panel
//        winPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Remove listeners from all player panels to prevent interaction
//        panelMap.values().forEach(this::removePanelListeners);
//
//        // Add the win panel to the layeredPane and make it visible
//        layeredPane.add(winPanel, Integer.valueOf(3)); // Ensuring it's at the top layer
//        winPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
//
//        layeredPane.revalidate();
//        layeredPane.repaint();
//    }

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
                playerScoreLabel.setIcon(scaleIcon("src/main/resources/images/gold_medal.png", iconWidth, iconHeight));
            } else if (i == 1) {
                playerScoreLabel.setIcon(scaleIcon("src/main/resources/images/silver_medal.png", iconWidth, iconHeight));
            } else if (i == 2) {
                playerScoreLabel.setIcon(scaleIcon("src/main/resources/images/bronze_medal.png", iconWidth, iconHeight));
            }
            winPanel.add(playerScoreLabel);
        }
        // "Play Again?" button
        JButton playAgainButton = createCustomButton("Play Again?", 300, 60);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            controller.startNewGame();
        });

        // "Close Game" button
        JButton closeGameButton = createCustomButton("Play Again?", 300, 60);
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

    public void highlightPlayerTurn(String orientation) {
        SwingUtilities.invokeLater(() -> {
            // Reset all panels to the default background first
            panelMap.values().forEach(panel -> panel.setBackground(null));

            // Now highlight the active player's panel
            if (orientation != null) {
                JPanel activePanel = panelMap.get(orientation);
                if (activePanel != null) {
                    // Set to a yellow background to indicate active player
                    activePanel.setBackground(Color.YELLOW);
                    activePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Optional: Add a black border for emphasis
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

        // Customize font, e.g., button.setFont(new Font("Arial", Font.BOLD, 12));

//        // Action listener for button's functionality
//        button.addActionListener(e -> {
//            // Your action logic here
//            System.out.println(text + " button clicked.");
//        });

        return button;
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
