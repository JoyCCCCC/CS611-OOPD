import java.util.Scanner;

public class QuoridorGame extends TicTacToeGame {
    private QuoridorBoard board;
    private Player player1;
    private Player player2;
    private Scanner scanner;
    private String response;

    public QuoridorGame(Team[] teams) {
        super(teams);
        board = new QuoridorBoard();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean keepPlaying = true;
        while(keepPlaying) {
            player1 = inputPlayer(0);
            player2 = inputPlayer(1);

            player1.setSymbol(new Piece("A"));
            player2.setSymbol(new Piece("B"));

            player1.move(0, 4);
            player2.move(8, 4);

            board.placePlayer(player1.getX(), player1.getY(), player1.getSymbol());
            board.placePlayer(player2.getX(), player2.getY(), player2.getSymbol());
            board.display();

            Player firstPlayer = startFirst();
            Player currentPlayer = setCurrentPlayer(firstPlayer);
            while (true) {
                playerTurn(currentPlayer);
                if (checkWin(currentPlayer)) {
                    playerWins(currentPlayer);
                    break;
                }
                // switch to another player
                currentPlayer = switchPlayer(currentPlayer,player1,player2);
            }
            printSummary();

            System.out.println("Do you want to continue the game？ (Y/N)");
            do {
                response = scanner.next();
                keepPlaying = response.equalsIgnoreCase("Y");
            } while(!(response.equalsIgnoreCase("Y")) && !(response.equalsIgnoreCase("N")));

            // if continue the game, reset the board
            if (keepPlaying) {
                this.board = new QuoridorBoard();
            }
        }
        //Reset all teams statistics when player change the game.
        resetAllTeamsStatistics();
    }

    public Player startFirst() {
        String team;
        do {
            System.out.println("Please choose which team starts first(input 1 or 2):");
            team = scanner.next();
        } while (!team.equals("1") && !team.equals("2"));

        if(team.equals("1")){
            return player1;
        }else {
            return player2;
        }
    }

    // Input w/a/s/d stands for up/left/down/right
    public void playerTurn(Player player) {
        Player opponent;
        if (player.equals(player1)){
            opponent = player2;
        } else {
            opponent = player1;
        }
        boolean validMove = false;
        while (!validMove) {
            System.out.println("Player " + player.getPlayerNumber() + "'s turn. Enter move (w/s/a/d) or wall (h/v x y), note that the coordinates represent the leftmost or topmost endpoint of the wall:");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            // "h" stands for horizontal, "v" stands for vertical
            if (parts[0].equals("h") || parts[0].equals("v")) {
                if (player.getWallNumber() >= 10) {
                    System.out.println("You have no walls left to place.");
                    continue;
                }
                Wall wall;
                if(parts[0].equals("h")){
                    wall = new Wall("-");
                    wall.setHorizontal(true);
                } else {
                    wall = new Wall("|");
                    wall.setVertical(true);
                }

                int x = Integer.parseInt(parts[1]) - 1;
                int y = Integer.parseInt(parts[2]) - 1;
                wall.setX(x);
                wall.setY(y);

                boolean wallPlaced = false;
                if (wall.getHorizontal()) {
                    wallPlaced = board.placeHorizontalWall(wall, player1.getX(), player1.getY(), player2.getX(), player2.getY());
                } else if (wall.getVertical()) {
                    wallPlaced = board.placeVerticalWall(wall, player1.getX(), player1.getY(), player2.getX(), player2.getY());
                }
                if (wallPlaced) {
                    System.out.println((parts[0].equals("h") ? "Horizontal" : "Vertical") + " wall placed.");
                    player.incrementWallNumber();
                    validMove = true;
                } else {
                    System.out.println("Invalid wall placement.");
                }
            } else if(parts[0].equals("w") || parts[0].equals("a") || parts[0].equals("s") || parts[0].equals("d")) {
                int newX = player.getX();
                int newY = player.getY();
                switch (parts[0]) {
                    case "w":
                        newX -= 1;
                        break;
                    case "s":
                        newX += 1;
                        break;
                    case "a":
                        newY -= 1;
                        break;
                    case "d":
                        newY += 1;
                        break;
                    default:
                        System.out.println("Invalid move.");
                        continue;
                }

                // check if the way is blocked
                if (board.isValidMove(player.getX(), player.getY(), newX, newY, opponent.getX(), opponent.getY())) {
                    // if not blocked, then try to jump
                    if (attemptJump(player, opponent, newX, newY)) {
                        validMove = true;
                    } else {
                        board.placePlayer(player.getX(), player.getY(), new Piece("."));
                        player.move(newX, newY);
                        board.placePlayer(newX, newY, player.getSymbol());
                        validMove = true;
                    }
                } else {
                    System.out.println("Invalid move.");
                }
            } else {
                System.out.println("Invalid input. Please enter again!");
                continue;
            }
            board.display();
        }
    }

    public boolean attemptJump(Player player, Player opponent, int newX, int newY) {
        if (opponent.getX() == newX && opponent.getY() == newY) {
            int jumpX = newX + (newX - player.getX());
            int jumpY = newY + (newY - player.getY());
            if (board.isValidMove(opponent.getX(), opponent.getY(), jumpX, jumpY, player.getX(), player.getY())) {
                board.placePlayer(player.getX(), player.getY(), new Piece("."));
                player.move(jumpX, jumpY);
                board.placePlayer(jumpX, jumpY, player.getSymbol());
                return true;
            } else {
                // Try to jump to another board
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

                // choose right direction until find a right place
                while (true) {
                    System.out.println("Choose jump direction (l for left or down, r for right or up):");
                    String direction = scanner.nextLine();
                    // Find left jump or right jump
                    boolean canJumpLeft = board.isValidMove(opponent.getX(), opponent.getY(), leftMoveX, leftMoveY, player.getX(), player.getY());
                    boolean canJumpRight = board.isValidMove(opponent.getX(), opponent.getY(), rightMoveX, rightMoveY, player.getX(), player.getY());
                    // The direction is restricted to left->right board
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
                    } else if (!canJumpLeft && !canJumpRight) {
                        System.out.println("Both left and right jumps are blocked by walls. Please choose another move.");
                        return false;
                    } else {
                        System.out.println("Invalid jump direction. Please choose again.");
                    }
                }
            }
        }
        return false;
    }

    public boolean checkWin(Player player) {

        if (player.getSymbol().getName().equals("A") && player.getX() == 8) {
            return true;
        }

        if (player.getSymbol().getName().equals("B") && player.getX() == 0) {
            return true;
        }
        return false;
    }

    public void printSummary() {
        for (Team team : teams) {
            System.out.println("----------------" + team.getName() + "----------------");
            for (Player player : team.getPlayers()) {
                System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '" + player.getSymbol().getName() + "' " + (player.hasWon() ? "win" : "lose"));
            }
            System.out.println("Total win: " + team.getWinCount());
        }
    }
}