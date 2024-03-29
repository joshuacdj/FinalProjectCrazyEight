# Crazy Eights
_Authors: Chia Jin Jun, Ian Hoe, Jered Wong, Jeremaine Cheong, Joshua Cheung, Teh Jing Wei_

Note: For the best playing experience, please open the game in full screen.

<p>Crazy Eights is a classic card game brought to life through Java Swing. This project aims to recreate the engaging experience of Crazy Eights, emphasising intuitive gameplay and the strategic depth that has made the card game a favourite for generations.<br>

<p></p>

<p>Crazy Eights is a shedding-type card game where players take turns to match the rank or suit of the previously played card, aiming to be the first to empty their hand. The "Crazy Eight" adds twists and strategic depth to the gameplay. Our implementation in Java Swing not only brings these game mechanics to life but also adds a layer of graphical interface to enhance the playing experience.<br>

## Prerequisites

To ensure a smooth experience with this application, please make sure you have the following setup:

- Java JDK 21 or later
- IntelliJ IDEA (recommended for running the project)

## How to Start the Game

### Utilisation of compile.bat & run.bat

For Mac users, open the terminal and run:
```
chmod +x compile.bat
chmod +x run.bat
```

For non-Mac users, and also Mac users after following the first step, run the game from the terminal via: 
```
./compile.bat
./run.bat
```

### Running through IntelliJ IDEA's CodeRunner
Locate Main.java and run the file to start the game.

### Running via the Terminal
Alternatively, the game can also be “run” from IntellijIDE’s Terminal via:
```
javac -d classes -cp src src/app/Main.java
java -cp classes app.Main
```

## How to play

### Objective
```
The goal of the game is to clear your hand! 
```

### Gameplay
```
Each player takes turns playing a card.
You can play any card that matches the SUIT or VALUE of the latest played card.
When a player plays an 8, he can set the suit of the following cards to be played. 
If a player doesn’t have a playable card, he will draw until he gets a playable card, or up to 5 cards, whichever comes first. 
If after drawing 5 cards, the player still doesn’t have a playable card, his turn will end without playing a card. 
```

### How to Win
```
The game ends when a player empties his/her hand. The rankings of the other players are determined by the number of points they have at the end of the game.
Aces are 1 point. Jacks, Queens and Kings are 10 points each. Any 8 cards are worth 50 POINTS each, so clear them fast!
If 2 players have the same points after the game ends, the higher ranked player is determined by the one who has lesser cards in his hand. 
```

## Screenshots of the Game

#### Welcome Screen
![welcomeScreen](screenshots/welcomeScreen.png) <br>
#### In-Game Screen
![inGameScreen](screenshots/inGameScreen.png) <br>
#### Suits button
![suitsScreen](screenshots/suitsScreen.png) <br>
#### End-Game Screen
![endGameScreen](screenshots/endGameScreen.png) <br>

## Citation
This application was programmed with the slight aid of AI Tools such as ChatGPT for the purpose of debugging, and also for the generation of the application's icon.
