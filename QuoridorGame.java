import java.util.Scanner;

public class QuoridorGame extends TicTacToeGame {
    private QuoridorBoard board;
    private Player[] players;
    private Scanner scanner;
    private String response;

    public QuoridorGame(Team[] teams) {
        super(teams);
        board = new QuoridorBoard();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean keepPlaying = true;
        while (keepPlaying) {
            int playerCount = choosePlayerCount();
            players = new Player[playerCount];

            for (int i = 0; i < playerCount; i++) {
                players[i] = inputPlayer(i);
                players[i].setSymbol(new Piece(String.valueOf((char) ('A' + i))));
                if (playerCount == 4) {
                    players[i].setWallNumber(5); // five walls for four players
                } else {
                    players[i].setWallNumber(10); // Ten walls for two players
                }
            }

            initializePlayers();

            System.out.println("Quoridor Game starts!");
            for (int i = 0; i < playerCount; i++) {
                System.out.println("\"" + players[i].getSymbol().getName() + "\" represents team " + (i + 1));
            }
            board.display();

            Player firstPlayer = startFirst();
            Player currentPlayer = setCurrentPlayer(firstPlayer);
            while (true) {
                playerTurn(currentPlayer);
                if (checkWin(currentPlayer)) {
                    playerWins(currentPlayer);
                    break;
                }
                currentPlayer = switchPlayer(currentPlayer);
            }
            printSummary();

            System.out.println("Do you want to continue the game？ (Y/N)");
            do {
                response = scanner.next();
                keepPlaying = response.equalsIgnoreCase("Y");
            } while (!(response.equalsIgnoreCase("Y")) && !(response.equalsIgnoreCase("N")));

            if (keepPlaying) {
                this.board = new QuoridorBoard();
            }
        }
        resetAllTeamsStatistics();
    }
    // Choose the number of player(2 or 4)
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
    // Set the start point of each player
    private void initializePlayers() {
        int[][] startingPositions = {{0, 4}, {8, 4}, {4, 0}, {4, 8}};
        for (int i = 0; i < players.length; i++) {
            players[i].move(startingPositions[i][0], startingPositions[i][1]);
            board.placePlayer(players[i].getX(), players[i].getY(), players[i].getSymbol());
        }
    }
    // Choose the start player
    public Player startFirst() {
        String team;
        do {
            System.out.println("Please choose which team starts first (input 1 to " + players.length + "):");
            team = scanner.next();
        } while (!team.matches("[1-" + players.length + "]"));

        return players[Integer.parseInt(team) - 1];
    }
    // Play turn by turn
    public void playerTurn(Player player) {
        boolean validMove = false;
        while (!validMove) {
            System.out.println("Player " + player.getSymbol().getName() + "'s turn. Enter move (w/s/a/d) or wall (h/v x y):");
            String input = scanner.next();

            if (input.equals("h") || input.equals("v")) {
                int x, y;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt() - 1;
                    x = scanner.nextInt() - 1;
                } else {
                    // Error check
                    System.out.println("Invalid wall placement input.");
                    continue;
                }

                if (player.getWallNumber() <= 0) {
                    System.out.println("You have no walls left to place.");
                    continue;
                }
                // place the wall
                Wall wall = new Wall(input.equals("h") ? "-" : "|");
                wall.setHorizontal(input.equals("h"));
                wall.setVertical(input.equals("v"));
                wall.setX(x);
                wall.setY(y);

                boolean wallPlaced = input.equals("h") ? board.placeHorizontalWall(wall, players) : board.placeVerticalWall(wall, players);
                // Error check
                if (wallPlaced) {
                    System.out.println((input.equals("h") ? "Horizontal" : "Vertical") + " wall placed.");
                    player.decrementWallNumber();
                    validMove = true;
                } else {
                    System.out.println("Invalid wall placement.");
                }

            } else if (input.equals("w") || input.equals("a") || input.equals("s") || input.equals("d")) {
                int newX = player.getX();
                int newY = player.getY();

                switch (input) {
                    case "w": newX -= 1; break;
                    case "s": newX += 1; break;
                    case "a": newY -= 1; break;
                    case "d": newY += 1; break;
                    default: System.out.println("Invalid move."); continue;
                }
                // If do not need to jump ,the move is valid, continue
                if (attemptJump(player, newX, newY)) {
                    validMove = true;
                } else if (board.isValidMove(player.getX(), player.getY(), newX, newY, players)) {
                    // If jump logic is applied and valid, move
                    boolean isOccupied = false;
                    for (Player other : players) {
                        if (other != player && other.getX() == newX && other.getY() == newY) {
                            isOccupied = true;
                            break;
                        }
                    }
                    // If jump logic is applied and invalid, no not move
                    if (!isOccupied) {
                        board.placePlayer(player.getX(), player.getY(), new Piece("."));
                        player.move(newX, newY);
                        board.placePlayer(newX, newY, player.getSymbol());
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

            board.display();
            displayRemainingWalls();
        }
    }

    // jump logic when get into opponents
    public boolean attemptJump(Player player, int newX, int newY) {
        for (Player opponent : players) {
            if (opponent.getX() == newX && opponent.getY() == newY) {
                int jumpX = newX + (newX - player.getX());
                int jumpY = newY + (newY - player.getY());

                for (Player otherPlayer : players) {
                    if (otherPlayer != player && otherPlayer != opponent) {
                        if ((otherPlayer.getX() == jumpX && otherPlayer.getY() == jumpY) ||
                                (otherPlayer.getX() == newX && otherPlayer.getY() == newY)) {
                            System.out.println("Invalid jump: another player is in the path.");
                            return false;
                        }
                    }
                }
                // Try to jump
                if (board.isValidMove(opponent.getX(), opponent.getY(), jumpX, jumpY, players)) {
                    board.placePlayer(player.getX(), player.getY(), new Piece("."));
                    player.move(jumpX, jumpY);
                    board.placePlayer(jumpX, jumpY, player.getSymbol());
                    return true;
                } else {
                    int leftMoveX;
                    int leftMoveY;
                    int rightMoveX;
                    int rightMoveY;
                    if (newX == player.getX()) {
                        leftMoveX = opponent.getX() - 1;
                        leftMoveY = opponent.getY();
                        rightMoveX = opponent.getX() + 1;
                        rightMoveY = opponent.getY();
                    } else {
                        leftMoveX = opponent.getX();
                        leftMoveY = opponent.getY() - 1;
                        rightMoveX = opponent.getX();
                        rightMoveY = opponent.getY() + 1;
                    }
                    // Check whether there is a wall in left and right
                    boolean canJumpLeft = board.isValidMove(opponent.getX(), opponent.getY(), leftMoveX, leftMoveY, players);
                    boolean canJumpRight = board.isValidMove(opponent.getX(), opponent.getY(), rightMoveX, rightMoveY, players);

                    if (!canJumpLeft && !canJumpRight) {
                        System.out.println("Invalid move: both left and right jumps are blocked.");
                        return false;
                    }
                    // Allow to choose lef or right
                    while (true) {
                        System.out.println("Choose jump direction (l for left or up, r for right or down):");
                        String direction = scanner.nextLine();
                        if (direction.equals("l") && canJumpLeft) {
                            board.placePlayer(player.getX(), player.getY(), new Piece("."));
                            player.move(leftMoveX, leftMoveY);
                            board.placePlayer(leftMoveX, leftMoveY, player.getSymbol());
                            return true;
                        } else if (direction.equals("r") && canJumpRight) {
                            board.placePlayer(player.getX(), player.getY(), new Piece("."));
                            player.move(rightMoveX, rightMoveY);
                            board.placePlayer(rightMoveX, rightMoveY, player.getSymbol());
                            return true;
                        } else {
                            System.out.println("Invalid jump direction. Please choose again.");
                        }
                    }
                }
            }
        }
        return false;
    }

    // Check if the piece reaches the destination
    public boolean checkWin(Player player) {
        if (player.getSymbol().getName().equals("A") && player.getX() == 8) {
            return true;
        }
        if (player.getSymbol().getName().equals("B") && player.getX() == 0) {
            return true;
        }
        if (players.length == 4) {
            if (player.getSymbol().getName().equals("C") && player.getY() == 8) {
                return true;
            }
            if (player.getSymbol().getName().equals("D") && player.getY() == 0) {
                return true;
            }
        }
        return false;
    }

    private Player switchPlayer(Player currentPlayer) {
        int currentIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(currentPlayer)) {
                currentIndex = i;
                break;
            }
        }
        return players[(currentIndex + 1) % players.length];
    }

    public void printSummary() {
        for (Player player : players) {
            System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '" + player.getSymbol().getName() + "' " + (player.hasWon() ? "win" : "lose"));
        }
        for (Team team : teams) {
            System.out.println("----------------" + team.getName() + "----------------");
            System.out.println("Total win: " + team.getWinCount());
        }
    }

    @Override
    public Player inputPlayer(int playerIndex) {
        System.out.println("Enter the current player number (input integer):");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer:");
            scanner.next();
        }
        int playerNumber = scanner.nextInt();
        Player player = new Player(null, null, 0);
        player.setPlayerNumber(playerNumber);

        // Assign player to the appropriate team
        int teamIndex = playerIndex % teams.length;
        teams[teamIndex].addTeamPlayer(player);
        player.setTeam(teams[teamIndex]);

        return player;
    }

    // Display the walls
    private void displayRemainingWalls() {
        System.out.println("Remaining walls:");
        for (Player player : players) {
            System.out.println("Player " + player.getSymbol().getName() + ": " + player.getWallNumber() + " walls");
        }
    }
}