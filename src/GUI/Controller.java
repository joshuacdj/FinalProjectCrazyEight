package GUI;

import java.util.*;
import main.java.*;// Adjust the package path based on your actual structure

import javax.swing.*;

public class Controller implements DrawActionListener{
    private WelcomeScreen welcomeScreen = new WelcomeScreen();
    private Round currentRound = new Round();

    private InGameScreen inGameScreen;

    public Controller() {
        currentRound.roundStart();
        // After creating Computer instances
        for (Player player : currentRound.getListOfPlayers()) {
            if (player instanceof Computer) {
                ((Computer) player).setDrawActionListener(this);
            }
        }
        inGameScreen = new InGameScreen(currentRound, this);
        // Initialize the main JFrame to hold different screens (panels)
        welcomeScreen.setVisible(true);
        welcomeScreen.getPlayButton().addActionListener(e -> startGame());
    }

    public void compPlay() {
        new Thread(() -> {
            ArrayList<Player> playerList = currentRound.getListOfPlayers();
            for (Player p : playerList) {
                if (p instanceof Computer) {
                    Computer c = (Computer) p;
                    try {
                        // Log computer action
                        System.out.println("This is computer " + c.getName() + ":" + c.getHand());
                        // Wait for 3 seconds before proceeding to the next iteration
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    ArrayList<Object> cardNSuit = c.action(currentRound.getDiscardPile().getTopCard(), currentRound.getDrawPile());
                    if (cardNSuit != null) {
                        Card cardy = (Card) cardNSuit.get(0);
                        Suit s = (Suit) cardNSuit.get(1);
                        // Updating GUI components must be done on the EDT
                        SwingUtilities.invokeLater(() -> {
                            currentRound.getDiscardPile().addCard(cardy);
                            if (cardy.getValue() != 8) {
                                currentRound.getDiscardPile().setTopCard(cardy);
                            } else {
                                currentRound.getDiscardPile().setTopCard(new Card(0, s));
                            }
                            // Update GUI here
                            inGameScreen.refreshPlayerPanel(inGameScreen.determineOrientation(p));;
                            inGameScreen.updateDiscardPileImage();
//                            inGameScreen.repaint(); // Ensure the screen is repainted to reflect changes
                            // System.out.println("The discard pile's top card is " + cardy);
                        });
                    }
                    if(c.getHand().size() == 0){
                        endGame();
                    }
                }
            }
        }).start();
    }
    public void endGame(){
        welcomeScreen.setVisible(false);
    }
    private void startGame() {
        showScreen(inGameScreen);
//        compPlay();
    }



    private void showScreen(JPanel panel) {
        // Switching the content pane to display the specified screen
        welcomeScreen.getContentPane().removeAll();
        welcomeScreen.revalidate();
        welcomeScreen.repaint();
        welcomeScreen.setContentPane(panel);
    }

    @Override
    public void onCardDrawn(Player player) {
        SwingUtilities.invokeLater(() -> {
            inGameScreen.updateDrawCard(player);
        });
    }

    public static void main(String[] args) {
        Controller control = new Controller();
    }
}

