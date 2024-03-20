package GUI;

import main.java.*;


import java.util.Iterator;
import java.util.List;
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
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class InGameScreen extends JPanel {
    private JPanel centerPanel; // Instance variable for the center panel
    private JLayeredPane layeredPane;


    public InGameScreen() {

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

        // South player panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel southPanel = createPlayerPanel("South");
        add(southPanel, gbc);

        // East player panel
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel eastPanel = createComputer1Panel("East");
        add(eastPanel, gbc);

        // West player panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JPanel westPanel = createComputer1Panel("West");
        add(westPanel, gbc);

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

//    private JButton findPlayCardButton(JLayeredPane layeredPane) {
//        for (Component comp : layeredPane.getComponents()) {
//            if (comp instanceof JButton && "\uFE0E".equals(((JButton) comp).getText())) {
//                return (JButton) comp;
//            }
//        }
//        return null;
//    }


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

    private void setupCardButtons(JPanel panel) {
        int numCards = 5; // The number of cards to display

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

                private void showSuitsButton() {
                    // Method to show the "Play card?" button within the layeredPane
                    JButton playButton = new JButton("♦");
                    playButton.setFont(new Font("♦", Font.BOLD, 30));
                    playButton.setBounds(layeredPane.getWidth() - 670, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
                    playButton.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(playButton);
                        layeredPane.repaint();
                    });

                    JButton play2Button = new JButton("♣");
                    play2Button.setFont(new Font("♣", Font.BOLD, 30));
                    play2Button.setBounds(layeredPane.getWidth() - 530, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
                    play2Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play2Button);
                        layeredPane.repaint();
                    });

                    JButton play3Button = new JButton("♥");
                    play3Button.setFont(new Font("♥", Font.BOLD, 30));
                    play3Button.setBounds(layeredPane.getWidth() - 390, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
                    play3Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play3Button);
                        layeredPane.repaint();
                    });

                    JButton play4Button = new JButton("♠");
                    play4Button.setFont(new Font("♠", Font.BOLD, 30));
                    play4Button.setBounds(layeredPane.getWidth() - 250, layeredPane.getHeight() - 80, 140, 70); // Position at bottom-right
                    play4Button.addActionListener(e -> {
                        // Your action logic
                        layeredPane.remove(play4Button);
                        layeredPane.repaint();
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

                    layeredPane.add(playButton, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play2Button, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play3Button, Integer.valueOf(2)); // Add playButton above centerPanel
                    layeredPane.add(play4Button, Integer.valueOf(2)); // Add playButton above centerPanel


                    layeredPane.moveToFront(playButton);
                    layeredPane.moveToFront(play2Button);
                    layeredPane.moveToFront(play3Button);
                    layeredPane.moveToFront(play4Button);

                    layeredPane.revalidate();
                    layeredPane.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                    List<Card> temp = new ArrayList<>();
                    temp.add(new Card(8, Suit.DIAMONDS));
                    temp.add(new Card(11, Suit.HEARTS));
                    temp.add(new Card(1, Suit.HEARTS));
                    temp.add(new Card(13, Suit.CLUBS));
                    temp.add(new Card(8, Suit.SPADES));

                    DiscardPile test = new DiscardPile();
                    String [] s = cardButton.getName().split("_");
                    int value = Integer.parseInt(s[0]);
                    Suit suit = Suit.valueOf(s[1]);
                    Card tempcard = new Card(value, suit);

//                    for ( int i = 0; i< temp.size(); i ++) {
//                        if(temp.get(i).equals())
//                    }
                            // if card thrown is 8, execute showSuitsButton()
                            if (tempcard.getValue() == 8) {
                                showSuitsButton();
                            }

                            // remove card from hand
                            for (Card c : temp) {
                                if (tempcard.equals(c)) {
                                    temp.remove(c);
                                    test.addCard(c);
                                    System.out.println("top card is " + test.getTopCard());
                                    System.out.println("after adding card");
                                    System.out.println(temp);
                                    break;
                                }
                            }

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

        List<Card> temp = new ArrayList<>();
        temp.add(new Card(8, Suit.DIAMONDS));
        temp.add(new Card(11, Suit.HEARTS));
        temp.add(new Card(1, Suit.HEARTS));
        temp.add(new Card(13, Suit.CLUBS));
        temp.add(new Card(8, Suit.SPADES));


        int numCards = temp.size();
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
            for (int i = 0; i < temp.size(); i++) {
                //TODO! Figure out why there are 2x print
                JButton cardButton = (JButton) panel.getComponent(i);
                cardButton.setName("" + temp.get(i).getValue() + "_" + temp.get(i).getSuit());
                ImageIcon icon = loadAndScaleCardImage(temp.get(i).getFilepath(), cardWidth, cardHeight, isVertical);

                cardButton.setIcon(icon);
                cardButton.setBorderPainted(false);
                cardButton.setContentAreaFilled(false);
                cardButton.setFocusPainted(false);
                cardButton.setOpaque(false);

            // Position the cards with proper offset
                xOffset = (cardWidth - (cardWidth / temp.size()) - 55) * i;


            // Set the bounds for the button based on the orientation
            cardButton.setBounds(xOffset, yOffset, cardWidth, cardHeight);
            }
        }

        panel.revalidate();
        panel.repaint();
        }

    private void positionCardLabel(JPanel panel, String orientation) {

        int numCards = panel.getComponentCount();
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
        ImageIcon icon1 = new ImageIcon(new ImageIcon("src/main/resources/images/8_of_diamonds.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));
        ImageIcon icon2 = new ImageIcon(new ImageIcon("src/main/resources/images/2_of_diamonds.png").getImage().getScaledInstance(-1, 160, Image.SCALE_SMOOTH));

        JButton button1 = new JButton(icon1);
        JButton button2 = new JButton(icon2);
        centerPanel.add(button1);
        centerPanel.add(button2);

        // Make buttons transparent
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.setContentAreaFilled(false);

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
}
