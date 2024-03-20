//package main.java;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//public class GameMaster {
//
//    private ArrayList<Player> listOfPlayers;
//    private Round round;
//
//    public GameMaster() {
//        listOfPlayers = new ArrayList<>();
//        listOfPlayers.add(new Human("You"));
//        listOfPlayers.add(new Computer("Comp 1"));
//        listOfPlayers.add(new Computer("Comp 2"));
//        listOfPlayers.add(new Computer("Comp 3"));
//        this.round = new Round(listOfPlayers);
//    }
//
//    public ArrayList<Player> getListOfPlayers() {
//        return listOfPlayers;
//    }
//
//    public Round getRound() {
//        return round;
//    }
//
//    //This will rotate the order of listOfPlayers when the next round starts
//    public void setPlayerPosition() {
//        // Get the player who went first
//        Player first = listOfPlayers.getFirst();
//
//        // Remove that player from the arraylist
//        listOfPlayers.removeFirst();
//
//        // Move the player to the end
//        listOfPlayers.add(first);
//    }
//
//    public void gameStart() {
//
//        // Loop as long as game has not ended
//        while (!gameEnd()) {
//            //This will start the round
//            round.roundStart();
//
//            //This will update the play order and set the round
//            //attribute to a new round with the updated play order
//            setPlayerPosition();
//            round = new Round(listOfPlayers);
//        }
//
//        //This will set the rankings of the players
//        ranking();
//    }
//
//    // Checks if game has ended by checking if
//    // any player has >= maximumPoints allowed
//    boolean gameEnd() {
//        final int MAXIMUMPOINTS = 100;
//
//        // Loop for all players playing
//        for (Player currentPlayer : listOfPlayers) {
//
//            // If the current player's score exceeds the maximum allowed, game will end
//            if (currentPlayer.getPoints() >= MAXIMUMPOINTS) {
//                return true;
//            }
//        }
//
//        // If none of the players have the maximum score, the game does not end
//        return false;
//    }
//
//    // This method returns the list of players according to the rankings
//    public void ranking() {
//        Collections.sort(listOfPlayers, new PlayerCompare());
//    }
//
//}
