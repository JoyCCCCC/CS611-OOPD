import java.util.Scanner;

public class QuoridorGame extends TicTacToeGame {
    private QuoridorBoard board;
    private Player[] players;
    private Scanner scanner;
    private String response;

    // Constructor initializes the board and sets up the scanner for input
    public QuoridorGame(Team[] teams) {
        super(teams); // Call the constructor of the TicTacToeGame class
        board = new QuoridorBoard(); // Initialize a new QuoridorBoard
        scanner = new Scanner(System.in);
    }

    // Start the game
    public void start() {
        boolean keepPlaying = true; // Game loop control flag
        while (keepPlaying) {
            // Choose the number of players (either 2 or 4)
            int playerCount = choosePlayerCount();
            players = new Player[playerCount];

            // Set up players and assign symbols
            for (int i = 0; i < playerCount; i++) {
                players[i] = inputPlayer(i);
                players[i].setSymbol(new Piece(String.valueOf((char) ('A' + i)))); // Assign symbols A, B, C, D
                if (playerCount == 4) {
                    players[i].setWallNumber(5); // Each player gets 5 walls in a 4-player game
                } else {
                    players[i].setWallNumber(10); // Each player gets 10 walls in a 2-player game
                }
            }

            // Initialize starting positions for all players
            initializePlayers();

            System.out.println("Quoridor Game starts!");
            for (int i = 0; i < playerCount; i++) {
                System.out.println("\"" + players[i].getSymbol().getName() + "\" represents player " + players[i].getPlayerNumber());
            }
            board.display(); // Display the initial board setup

            // Determine who will go first and set the current player
            Player firstPlayer = startFirst();
            Player currentPlayer = setCurrentPlayer(firstPlayer);

            // Main game loop: players take turns until someone wins
            while (true) {
                playerTurn(currentPlayer); // Handle player actions
                if (checkWin(currentPlayer)) { // Check if the current player has won
                    playerWins(currentPlayer); // Announce the winner
                    break;
                }
                currentPlayer = switchPlayer(currentPlayer); // Switch to the next player
            }
            printSummary(); // Print game summary after the game ends

            // Ask if the players want to continue playing
            System.out.println("Do you want to continue the game? (Y/N)");
            do {
                response = scanner.next();
                keepPlaying = response.equalsIgnoreCase("Y");
            } while (!(response.equalsIgnoreCase("Y")) && !(response.equalsIgnoreCase("N")));

            if (keepPlaying) {
                this.board = new QuoridorBoard(); // Reset the board for the next game
            }
        }
        resetAllTeamsStatistics(); // Reset the statistics of all teams when the game ends
    }

    // Choose the number of players (2 or 4)
    private int choosePlayerCount() {
        int playerCount = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Choose number of players (2 or 4):");
            if (scanner.hasNextInt()) {
                playerCount = scanner.nextInt();
                if (playerCount == 2 || playerCount == 4) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter 2 or 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return playerCount;
    }

    // Initialize the players with starting positions on the board
    private void initializePlayers() {
        int[][] startingPositions = {{0, 4}, {8, 4}, {4, 0}, {4, 8}}; // Starting positions for 2 or 4 players
        for (int i = 0; i < players.length; i++) {
            players[i].move(startingPositions[i][0], startingPositions[i][1]); // Move each player to their starting position
            board.makeMove(players[i].getX(), players[i].getY(), players[i].getSymbol()); // Place the player's symbol on the board
        }
    }

    // Choose the first player to start the game
    public Player startFirst() {
        String team;
        do {
            System.out.println("Please choose which team starts first (input 1 to " + players.length + "):");
            team = scanner.next();
        } while (!team.matches("[1-" + players.length + "]")); // Ensure valid input

        return players[Integer.parseInt(team) - 1]; // Return the selected player as the first player
    }

    // Handle the player's turn
    public void playerTurn(Player player) {
        boolean validMove = false;
        while (!validMove) {
            // Ask the player for input (either move or wall placement)
            System.out.println("Player " + player.getSymbol().getName() + "'s turn. Enter move (w/s/a/d) or wall (h/v x y):");
            String input = scanner.next();

            // Wall placement logic
            if (input.equals("h") || input.equals("v")) {
                int x, y;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt() - 1; // Get Y-coordinate for wall
                    x = scanner.nextInt() - 1; // Get X-coordinate for wall
                } else {
                    System.out.println("Invalid wall placement input.");
                    continue;
                }

                // Check if the player has walls left to place
                if (player.getWallNumber() <= 0) {
                    System.out.println("You have no walls left to place.");
                    continue;
                }

                // Place the wall on the board
                Wall wall = new Wall(input.equals("h") ? "-" : "|");
                wall.setHorizontal(input.equals("h"));
                wall.setVertical(input.equals("v"));
                wall.setX(x);
                wall.setY(y);

                // Check if the wall can be placed and adjust the board accordingly
                boolean wallPlaced = input.equals("h") ? board.placeHorizontalWall(wall, players) : board.placeVerticalWall(wall, players);

                if (wallPlaced) {
                    System.out.println((input.equals("h") ? "Horizontal" : "Vertical") + " wall placed.");
                    player.decrementWallNumber(); // Decrease the player's wall count
                    validMove = true;
                } else {
                    System.out.println("Invalid wall placement.");
                }

                // Movement logic (up, down, left, right)
            } else if (input.equals("w") || input.equals("a") || input.equals("s") || input.equals("d")) {
                int newX = player.getX();
                int newY = player.getY();

                // Determine the new position based on the input
                switch (input) {
                    case "w": newX -= 1; break; // Move up
                    case "s": newX += 1; break; // Move down
                    case "a": newY -= 1; break; // Move left
                    case "d": newY += 1; break; // Move right
                    default: System.out.println("Invalid move."); continue;
                }

                // Check if the move is valid (including jump logic)
                if (attemptJump(player, newX, newY)) {
                    validMove = true;
                } else if (board.isValidMove(player.getX(), player.getY(), newX, newY, players)) {
                    boolean isOccupied = false;
                    for (Player other : players) {
                        if (other != player && other.getX() == newX && other.getY() == newY) {
                            isOccupied = true; // Check if the new position is occupied by another player
                            break;
                        }
                    }

                    // If the move is valid and not occupied, execute the move
                    if (!isOccupied) {
                        board.makeMove(player.getX(), player.getY(), new Piece(".")); // Clear the old position
                        player.move(newX, newY); // Move the player to the new position
                        board.makeMove(newX, newY, player.getSymbol()); // Update the board with the new position
                        validMove = true;
                    } else {
                        System.out.println("Invalid move: another player is in the path.");
                    }
                } else {
                    System.out.println("Invalid move.");
                }
            } else {
                System.out.println("Invalid input. Please enter again!");
            }

            board.display(); // Display the updated board
            displayRemainingWalls(); // Display the remaining walls for each player
        }
    }

    // Jump logic when encountering an opponent
    public boolean attemptJump(Player player, int newX, int newY) {
        for (Player opponent : players) {
            // Check if the opponent is in the way
            if (opponent.getX() == newX && opponent.getY() == newY) {
                int jumpX = newX + (newX - player.getX());
                int jumpY = newY + (newY - player.getY());

                // Check if the jump is blocked by another player
                for (Player otherPlayer : players) {
                    if (otherPlayer != player && otherPlayer != opponent) {
                        if (otherPlayer.getX() == jumpX && otherPlayer.getY() == jumpY) {
                            System.out.println("Invalid jump: another player is in the path.");
                            return false; // Jump is blocked, return false
                        }
                    }
                }

                // Check if the jump is a valid move
                if (board.isValidMove(opponent.getX(), opponent.getY(), jumpX, jumpY, players)) {
                    // Perform the jump if valid
                    board.makeMove(player.getX(), player.getY(), new Piece(".")); // Clear old position
                    player.move(jumpX, jumpY); // Move player to new position
                    board.makeMove(jumpX, jumpY, player.getSymbol()); // Update board with new position
                    return true; // Jump is successful
                } else {
                    // Handle movement in case of a blocked jump
                    int leftMoveX, leftMoveY, rightMoveX, rightMoveY;

                    // Set coordinates for new moves based on current position
                    if (newX == player.getX()) { // Moving vertically
                        leftMoveX = opponent.getX() - 1;
                        leftMoveY = opponent.getY();
                        rightMoveX = opponent.getX() + 1;
                        rightMoveY = opponent.getY();
                    } else { // Moving horizontally
                        leftMoveX = opponent.getX();
                        leftMoveY = opponent.getY() - 1;
                        rightMoveX = opponent.getX();
                        rightMoveY = opponent.getY() + 1;
                    }

                    // Check if the new moves are blocked by walls
                    boolean canJumpLeft = board.isValidMove(opponent.getX(), opponent.getY(), leftMoveX, leftMoveY, players);
                    boolean canJumpRight = board.isValidMove(opponent.getX(), opponent.getY(), rightMoveX, rightMoveY, players);

                    // If both left and right jumps are blocked, the move is invalid
                    if (!canJumpLeft && !canJumpRight) {
                        System.out.println("Invalid move: both left and right jumps are blocked.");
                        return false;
                    }

                    // Allow the player to choose between left or right, up or down jump
                    while (true) {
                        System.out.println("Choose jump direction (l for left or up, r for right or down):");
                        String direction = scanner.nextLine();

                        // If the player chooses left and it's a valid move
                        if (direction.equals("l") && canJumpLeft) {
                            board.makeMove(player.getX(), player.getY(), new Piece(".")); // Clear old position
                            player.move(leftMoveX, leftMoveY); // Move to the left/up
                            board.makeMove(leftMoveX, leftMoveY, player.getSymbol()); // Update the board
                            return true; // Jump is successful
                            // If the player chooses right and it's a valid move
                        } else if (direction.equals("r") && canJumpRight) {
                            board.makeMove(player.getX(), player.getY(), new Piece(".")); // Clear old position
                            player.move(rightMoveX, rightMoveY); // Move to the right/down
                            board.makeMove(rightMoveX, rightMoveY, player.getSymbol()); // Update the board
                            return true; // Jump is successful
                        } else {
                            // Invalid direction input
                            System.out.println("Invalid jump direction. Please choose again.");
                        }
                    }
                }
            }
        }
        return false; // If no jump is possible, return false
    }

    // Check if the current player has won the game
    public boolean checkWin(Player player) {
        // Check if player "A" reaches the opposite end (row 8)
        if (player.getSymbol().getName().equals("A") && player.getX() == 8) {
            return true;
        }
        // Check if player "B" reaches the opposite end (row 0)
        if (player.getSymbol().getName().equals("B") && player.getX() == 0) {
            return true;
        }
        // If there are 4 players, check if player "C" or "D" reaches their goal
        if (players.length == 4) {
            if (player.getSymbol().getName().equals("C") && player.getY() == 8) {
                return true;
            }
            if (player.getSymbol().getName().equals("D") && player.getY() == 0) {
                return true;
            }
        }
        return false; // No win condition met
    }

    // Switch to the next player in the turn order
    private Player switchPlayer(Player currentPlayer) {
        int currentIndex = -1;
        // Find the index of the current player
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(currentPlayer)) {
                currentIndex = i;
                break;
            }
        }
        // Return the next player in the array (circular turn order)
        return players[(currentIndex + 1) % players.length];
    }

    // Print the game summary, including each team's performance
    public void printSummary() {
        for (int i = 0; i < players.length; ++i) {
            System.out.println("----------------" + teams[i].getName() + "----------------");
            for (Player player : teams[i].getPlayers()) {
                System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '" + player.getSymbol().getName() + "' " + (player.hasWon() ? "win" : "lose"));
            }
            System.out.println("Total win: " + teams[i].getWinCount());
        }
    }

    @Override
    public Player inputPlayer(int playerIndex) {
        // Input the player's number
        System.out.println("Enter the current player number (input integer):");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer:");
            scanner.next();
        }
        int playerNumber = scanner.nextInt(); // Read player number input
        Player player = new Player(null, null, 0); // Create a new player
        player.setPlayerNumber(playerNumber); // Assign the player number

        // Assign the player to the appropriate team
        int teamIndex = playerIndex % teams.length;
        teams[teamIndex].addTeamPlayer(player); // Add the player to the team
        player.setTeam(teams[teamIndex]); // Set the player's team

        return player; // Return the created player
    }

    // Display the number of walls remaining for each player
    private void displayRemainingWalls() {
        System.out.println("Remaining walls:");
        for (Player player : players) {
            System.out.println("Player " + player.getSymbol().getName() + ": " + player.getWallNumber() + " walls");
        }
    }
}
