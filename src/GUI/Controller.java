package GUI;

import GUI.WelcomeScreen;
import GUI.InGameScreen;
import main.java.Round; // Adjust the package path based on your actual structure

import javax.swing.*;

public class Controller {
    private WelcomeScreen welcomeScreen = new WelcomeScreen();
    private InGameScreen inGameScreen = new InGameScreen();
    private Round currentRound;

    public Controller() {
        // Initialize the main JFrame to hold different screens (panels)
        welcomeScreen.setVisible(true);

        // Initialize the round logic
        currentRound = new Round();

        // Initialize the welcome screen and add action listeners
        initializeWelcomeScreen();
    }

    private void initializeWelcomeScreen() {
//        welcomeScreen = new WelcomeScreen(
        welcomeScreen.getPlayButton().addActionListener(e -> startGame());
//        welcomeScreen.getExitButton().addActionListener(e -> System.exit(0));
        // Assuming you have methods in WelcomeScreen to get buttons. If not, add them.
//        showScreen(welcomeScreen);
    }

    private void startGame() {
        // Prepare the game round
//        currentRound.setupRound(); // Assuming this method exists to initialize the round

        // Initialize the in-game screen with the current round
//        inGameScreen = new InGameScreen(); // Adjust constructor based on your actual implementation
        showScreen(inGameScreen);

        // Continue with additional game setup or directly jump into game logic
    }

    private void showScreen(JPanel panel) {
        // Switching the content pane to display the specified screen
        welcomeScreen.getContentPane().removeAll();
        welcomeScreen.revalidate();
        welcomeScreen.setContentPane(panel);
        welcomeScreen.repaint();
    }

    public static void main(String[] args) {
        Controller control = new Controller();
    }
}

