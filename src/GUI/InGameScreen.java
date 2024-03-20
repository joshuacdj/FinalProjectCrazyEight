package GUI;

import main.java.*;


import java.sql.SQLOutput;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class InGameScreen extends JPanel {
    private JPanel centerPanel; // Instance variable for the center panel
    private JLayeredPane layeredPane;
    private Round round = new Round();
    private DiscardPile discardPile = round.getDiscardPile();
    private Map<String, JPanel> panelMap = new HashMap<>();
    private JLabel discardPileLabel = new JLabel();

    public InGameScreen() {

        round.roundStart();
        System.out.println("-----------");
        System.out.println(discardPile.getCards());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        // Set the weights to distribute extra space equally among all components
        gbc.weightx = 1.0; // Equal horizontal weight
        gbc.weighty = 1.0; // Equal vertical weight

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
        setupCardLabel(computer1Panel);

        return computer1Panel;
    }

    private JPanel createComputer2Panel(String orientation) {
        JPanel computer2Panel = new JPanel(null) { // Use null layout for absolute positioning
//            @Override
//            public void doLayout() {
//                positionCardButtons(this, orientation);
//            }
        };
        computer2Panel.setOpaque(false);
        computer2Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Remove later

        // Initial card buttons setup
        setupCardButtons(computer2Panel);

        return computer2Panel;
    }

    private JPanel createComputer3Panel(String orientation) {
        JPanel computer3Panel = new JPanel(null) { // Use null layout for absolute positioning
//            @Override
//            public void doLayout() {
//                positionCardButtons(this, orientation);
//            }
        };
        computer3Panel.setOpaque(false);
        computer3Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Remove later

        // Add a component listener to resize the card buttons when the panel is resized
//        computer3Panel.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                positionCardButtons(computer3Panel, orientation);
//            }
//        });

        // Initial card buttons setup
        setupCardButtons(computer3Panel);

        return computer3Panel;
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

    private void setupCardButtons(JPanel panel) {
        int numCards = round.getListOfPlayers().getFirst().getHand().size(); // The number of cards to display
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

//                    List<Card> temp = new ArrayList<>();
//                    temp.add(new Card(8, Suit.DIAMONDS));
//                    temp.add(new Card(11, Suit.HEARTS));
//                    temp.add(new Card(1, Suit.HEARTS));
//                    temp.add(new Card(13, Suit.CLUBS));
//                    temp.add(new Card(8, Suit.SPADES));

                    String [] s = cardButton.getName().split("_");
                    int value = Integer.parseInt(s[0]);
                    Suit suit = Suit.valueOf(s[1]);
                    Card tempcard = new Card(value, suit);

                            // remove card from hand
                            for (Card c : currentHand) {
                                if (tempcard.equals(c)) {
                                    panelMap.get("South").removeAll();
                                    currentHand.remove(c);
                                    discardPile.addCard(c);
                                    updateDiscardPileImage();
                                    panelMap.get("South").revalidate();
                                    panelMap.get("South").repaint();
                                    setupCardButtons(panelMap.get("South"));
                                    positionCardButtons(panelMap.get("South"), "South");

                                    //DEBUGGING PRINT STATEMENTS
                                    System.out.println("top card is " + discardPile.getTopCard());
                                    System.out.println("after adding card");
                                    System.out.println(currentHand);
                                    break;
                                }
                            }
                    if (tempcard.getValue() == 8) {
                        System.out.println("tempcard before " + discardPile.getTopCard());
                        showSuitsButton();
                    }
                    System.out.println("tempcard after " + discardPile.getTopCard());

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
                        discardPile.setTopCard(new Card(8, Suit.DIAMONDS));
                    });

                    play2Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(8, Suit.CLUBS));
                    });

                    play3Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(8, Suit.HEARTS));
                    });

                    play4Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.remove(play3Button);
                        layeredPane.remove(play2Button);
                        layeredPane.remove(play1Button);
                        layeredPane.repaint();
                        discardPile.setTopCard(new Card(8, Suit.SPADES));
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

    private void setupCardLabel(JPanel panel) {
        int numCards = 5; // The number of cards to display

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
                xOffset = (cardWidth - (cardWidth / humanHand.size()) - 55) * i;


            // Set the bounds for the button based on the orientation
            cardButton.setBounds(xOffset, yOffset, cardWidth, cardHeight);
            }
        }

        panel.revalidate();
        panel.repaint();
        }

    private void positionCardLabel(JPanel panel, String orientation) {

        //numCards is dependent on the panel
        //East is 1, north is 2 and west is 3
        int numCards = switch (orientation) {
            case "East" -> round.getListOfPlayers().get(1).getHand().size();
            case "North" -> round.getListOfPlayers().get(2).getHand().size();
            case "West" -> round.getListOfPlayers().get(3).getHand().size();
            default -> 0;
        };

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

            for ( int i = 0; i < numCards; i++) {
                JLabel back_card = (JLabel) panel.getComponent(i);
                ImageIcon icon = loadAndScaleCardImage("src/main/resources/images/back_card.png", cardWidth, cardHeight, isVertical);
                back_card.setIcon(icon);

                if (!isVertical) {
                    xOffset = (cardWidth - (cardWidth / numCards) - 55) * i;
                } else {
                    yOffset = (cardHeight - (cardHeight / numCards) - 55) * i;
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

        // Example of adding a component to the centerPanel
        // You can add more components similarly, adjusting the gridx, gridy, weightx, weighty as needed
        ImageIcon drawPileIcon = new ImageIcon(new ImageIcon("src/main/resources/images/back_card.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        //Get the filepath of the first discarded card at the start of the round
        String filePath = discardPile.getCards().getFirst().getFilepath();
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        JButton button1 = new JButton(drawPileIcon);
        discardPileLabel.setIcon(discardPileIcon);
        centerPanel.add(button1);
        centerPanel.add(discardPileLabel);

        // Make buttons transparent
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
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

    // Main method to test the InGameScreen layout
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("In-Game Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new InGameScreen());
            frame.setPreferredSize(new Dimension(1000, 1000)); // Preferred initial size
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    //This method is to update the image of the discardPile
    public void updateDiscardPileImage() {
        String filePath = discardPile.getCards().getLast().getFilepath();
//        JButton discardPileButton = panelMap.get("Center");
        ImageIcon discardPileIcon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        discardPileLabel.setIcon(discardPileIcon);

    }
}
