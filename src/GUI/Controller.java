package GUI;

import java.util.*;
import GUI.WelcomeScreen;
import GUI.InGameScreen;
import main.java.*;// Adjust the package path based on your actual structure

import javax.swing.*;

public class Controller {
    private WelcomeScreen welcomeScreen = new WelcomeScreen();
    private Round currentRound = new Round();

    private InGameScreen inGameScreen;

    public Controller() {
        currentRound.roundStart();
        inGameScreen = new InGameScreen(currentRound, this);
        // Initialize the main JFrame to hold different screens (panels)
        welcomeScreen.setVisible(true);
        welcomeScreen.getPlayButton().addActionListener(e -> startGame());
    }

    public void compPlay(){
        //play for comps only
        ArrayList<Player> playerList = currentRound.getListOfPlayers();
        for(Player p : playerList){
            if(p instanceof Computer){
                Computer c = (Computer) p;
                ArrayList<Object> cardNSuit = c.action(currentRound.getDiscardPile().getTopCard(), currentRound.getDrawPile());
                if(cardNSuit != null){
                    Card cardy = (Card) cardNSuit.get(0);
                    Suit s = (Suit) cardNSuit.get(1);
                    currentRound.getDiscardPile().addCard(cardy);
                    currentRound.getDiscardPile().setTopCard(cardy);
//                    System.out.println("The discard pile's top card is " + cardy);
                    inGameScreen.updateDiscardPileImage();
                }
                System.out.println("This is computer " + c.getName() + ":" + c.getHand());
                inGameScreen = new InGameScreen(currentRound, this);
                showScreen(inGameScreen);
//                try{
//                    Thread.sleep(5000);
////                    wait(5000);
//
//                }catch(InterruptedException e){
//                    throw new RuntimeException();
//                }
            }
        }
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

    public static void main(String[] args) {
        Controller control = new Controller();
    }
}

