package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameMaster {

    private ArrayList<Player> listOfPlayers;
    private Round round;

    public GameMaster(ArrayList<Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
        this.round = new Round(listOfPlayers);
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public void gameStart() {

        // Loop as long as game has not ended
        while (!gameEnd()) {
            round.roundStart();
        }

        this.listOfPlayers = ranking(this.listOfPlayers);
    }

    // Checks if game has ended by checking if
    // any player has >= maximumPoints allowed
    boolean gameEnd() {
        final int MAXIMUMPOINTS = 100;

        // Loop for all players playing
        for (Player currentPlayer : listOfPlayers) {

            // If the current player's score exceeds the maximum allowed, game will end
            if (currentPlayer.getPoints() >= MAXIMUMPOINTS) {
                return true;
            }
        }

        // If none of the players have the maximum score, the game does not end
        return false;
    }

    // This method returns the list of players according to the rankings
    public ArrayList<Player> ranking(ArrayList<Player> listOfPlayers) {
        Comparator<Player> playerComparator = new PlayerCompare();
        Collections.sort(listOfPlayers, playerComparator);

        return listOfPlayers;
    }

}
