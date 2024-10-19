# Readme

# CS611-Assignment 3
## Quoridor game
---------------------------------------------------------------------------
- Name: Yijia Chen && Zhenyang Qian
- Email: cyj0709@bu.edu / zhenyang@bu.edu
- Student ID: U98033467 / U76414206

## Files
---------------------------------------------------------------------------

```
src/
├── GeneralBoard.java          # An interface that contains abstract methods for displaying the board, moving pieces, and determining whether the piece's position is valid.
├── TicTacToeBoard.java        # Implements the GeneralBoard interface. Initialize the board layout, display the board, record the piece placement, and check for win or draw conditions.
├── SuperBoard.java            # Inherits from TicTacToeBoard, the initialization and display methods have been overridden.
├── QuoridorBoard.java         # Implements the GeneralBoard interface. In addition to implementing the abstract methods in the interface, it also initializes the board, checks whether walls can be placed, and uses BFS to determine if a player is completely trapped.
├── GeneralGame.java           # Abstract general game class. Set up methods to handle draws, methods to print the summary table, and methods to reset team statistics.
├── TicTacToeGame.java         # Inherits from the GeneralGame class. Show Tic-Tac-Toe game logic, including features such as replaying the game, inputting or changing the board size, entering the current player's number, allowing players to choose their piece type, updating and displaying the board, determining and recording the outcome, and switching players.
├── OrderAndChaosGame.java     # Inherits from the TicTacToeGame class. Show Order and Chaos game logic, which is similar to Tic-Tac-Toe, but the `inputPlayer` and `choosePiece` methods have been overridden. This is because the team names are fixed as 'ORDER' and 'CHAOS', and players can choose either 'X' or 'O' in each round.
├── SuperTicTacToeGame.java    # Inherits from the TicTacToeGame class. Show Super Tic-Tac-Toe game logic. I have divided the super board into 9 mini boards, first checking the victory conditions for the mini boards before placing them into the super board for further checks. This part will be explained in detail in the design documentation.
├── QuoridorGame.java          # Inherits from the TicTacToeGame. Show quoridor game logic, including setting walls , making movements, choosing player numbers(2 or 4). When 2 players, their chess are 'A' and' B'. Extending the TicTacToe because they have same board and playing logic.
├── Player.java                # Record the relevant members required for the player.
├── Team.java                  # Record the relevant members required for the team.
├── Piece.java                 # Record the piece type.
├── Tile.java                  # Record the piece status on the board cell.
├── Wall.java                  # Inherits from Piece where the status of the walls are stored.
├── Main.java                  # Initialize and start the game.


```

## Notes
---------------------------------------------------------------------------
**Board Scalability**: For Quoridor, the size of the board is bound to 9*9, and the winning condition is that one player reaches the opposite edge. The game can loop continuously. Each player either places a wall or moves a piece each turn. After placing the wall, the board will show where the wall is placed. To place the wall, input (h x y) or (v x y), where 'h' stands for placing a horizontal wall and 'v' stands for placing a vertical wall, x and y are the grid cells designated for wall placement, which is indicated on the map. A wall cannot extend beyond the boundaries, cannot overlap with a previous wall, cannot have more than the number of walls each player has, and cannot completely trap an opponent.

**Player Scalability**: The number of players can be 2 or 4, and each round, any player from each team can be selected to play the match. At the end of each round, a **Summary Table** is generated to record the pieces of the players and the results (wins). Finally, the total number of wins for each team is calculated. The player can input 'w','a','s','d' to choose the moving directions.(stands for up,down,left,right)

Additionally, We implemented some input validation. Also, there is a BFS algorithm to detect whether a player if fully blocked (It is not allowed in this game!). When there is another player on the direction of the movement, the player can jump through the other players and if there is a wall, the player can choose the jump direction(left or right, up or down). As for the 4-player situation, we have also implemented that when multiple players meet, it's forbidden to jump more than 1 player.


## How to compile and run
---------------------------------------------------------------------------
1. Navigate to the directory after unzipping the files
2. Run the following instructions:
javac *.java
java Main


## Input/Output Example
---------------------------------------------------------------------------
Let's play a game! Please choose a game type(input an integer):
1) Tic-Tac-Toe
2) Order and Chaos
3) Super Tic-Tac-Toe
4) QuoridorGame
5) Exit
4
-----------------------------
Choose number of players (2 or 4):
2
Enter the current player number (input integer):
1
Enter the current player number (input integer):
1
Quoridor Game starts!
"A" represents player1
"B" represents player1
    1   2   3   4   5   6   7   8 
  .   .   .   .   A   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4                                   
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   .   .   .   .   . 
6                                   
  .   .   .   .   .   .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   B   .   .   .   . 
Please choose which team starts first (input 1 to 2):
1
-----------------------------
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
s
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   A   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4                                   
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   .   .   .   .   . 
6                                   
  .   .   .   .   .   .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   B   .   .   .   . 
Remaining walls:
Player A: 10 walls
Player B: 10 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   A   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4                                   
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   .   .   .   .   . 
6                                   
  .   .   .   .   .   .   .   .   . 
7                                   
  .   .   .   .   B   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 10 walls
Player B: 10 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
h 3 4
-----------------------------
Horizontal wall placed.
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   A   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   .   .   .   .   . 
6                                   
  .   .   .   .   .   .   .   .   . 
7                                   
  .   .   .   .   B   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 10 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
v 5 6
-----------------------------
Vertical wall placed.
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   A   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   B   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
s
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   A   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   B   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   A   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   B | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
a
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   B | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
d
-----------------------------
Invalid move.
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   B | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                                   
  .   .   .   .   B | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 9 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
h 5 5	
-----------------------------
Horizontal wall placed.
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   B | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
a
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   B   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
s
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   A   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   B   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   A   .   .   .   .   . 
4         - - -                     
  .   .   .   B   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
d	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   A   .   .   .   . 
4         - - -                     
  .   .   .   B   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
d	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   A   .   .   .   . 
4         - - -                     
  .   .   .   .   B   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
s
-----------------------------
Choose jump direction (l for left or up, r for right or down):
l
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   A   B   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   B   .   .   .   . 
4         - - -                     
  .   .   .   A   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
d
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1                                   
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   B   .   .   .   . 
4         - - -                     
  .   .   .   .   A   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 9 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
h 4 1
-----------------------------
Horizontal wall placed.
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   .   B   .   .   .   . 
4         - - -                     
  .   .   .   .   A   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
w
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   A   .   .   .   . 
3                                   
  .   .   .   .   B   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   B   .   .   .   . 
2                                   
  .   .   .   .   A   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
a	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   B   .   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
d	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   .   B   .   .   . 
2                                   
  .   .   .   A   .   .   .   .   . 
3                                   
  .   .   .   .   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player A's turn. Enter move (w/s/a/d) or wall (h/v x y):
s	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   .   .   .   . 
1             - - -                 
  .   .   .   .   .   B   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   A   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Player B's turn. Enter move (w/s/a/d) or wall (h/v x y):
w	
-----------------------------
    1   2   3   4   5   6   7   8 
  .   .   .   .   .   B   .   .   . 
1             - - -                 
  .   .   .   .   .   .   .   .   . 
2                                   
  .   .   .   .   .   .   .   .   . 
3                                   
  .   .   .   A   .   .   .   .   . 
4         - - -                     
  .   .   .   .   .   .   .   .   . 
5                 - - -             
  .   .   .   .   . | .   .   .   . 
6                   |               
  .   .   .   .   . | .   .   .   . 
7                                   
  .   .   .   .   .   .   .   .   . 
8                                   
  .   .   .   .   .   .   .   .   . 
Remaining walls:
Player A: 8 walls
Player B: 8 walls
Team 2(Player 1) wins!
----------------Team 1----------------
Player '1' plays piece: 'A' lose
Total win: 0
----------------Team 2----------------
Player '1' plays piece: 'B' win
Total win: 1
Do you want to continue the game？ (Y/N)
N	
-----------------------------
Let's play a game! Please choose a game type(input an integer):
1) Tic-Tac-Toe
2) Order and Chaos
3) Super Tic-Tac-Toe
4) QuoridorGame
5) Exit 
5
-----------------------------
Player 3 (Team 1), please enter the board label (A-I):
C
Player 3 (Team 1) (X), please enter the index of cell：
5

Good Game!

Process finished with exit code 0
