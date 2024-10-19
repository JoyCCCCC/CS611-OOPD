import java.util.Scanner;

public class TicTacToeGame extends GeneralGame {
    private String team1Piece;
    private String team2Piece;
    private String response;
    private int boardSize;
    private Scanner scanner;

    public TicTacToeGame(Team[] teams) {
        super(teams);
        this.board = new TicTacToeBoard(boardSize,boardSize);
        scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        boolean keepPlaying = true;
        int flag = 0; // Check whether it's a start of the game.

        while (keepPlaying) {
            if (flag == 0) {
                inputSize();
            }
            System.out.println("Do you want to change the board size？(Y/N)");
            String changeBoardSizeResponse = scanner.next();
            if (changeBoardSizeResponse.equalsIgnoreCase("Y")) {
                inputSize();
            }

            Player team1Player = inputPlayer(0);
            Player team2Player = inputPlayer(1);

            choosePiece(team1Player,team2Player);

            // Start the game and display the initial board.
            System.out.println("Tic Tac Toe Game starts!");
            board.display();

            Player currentPlayer = setCurrentPlayer(team1Player);
            while (true) {
                System.out.println("Enter index of cell to place piece.");
                currentPlayer.makeMove(board); // current player place the piece
                board.display();

                // check whether it's a win
                if (board.checkForWinner()) {
                    playerWins(currentPlayer);
                    break;
                }

                // check whether it's a draw
                if (board.isFull()) {
                    fullBoard(team1Player,team2Player);
                    break;
                }

                // switch to another player
                currentPlayer = switchPlayer(currentPlayer,team1Player,team2Player);
            }

            super.printSummary();

            System.out.println("Do you want to continue the game？ (Y/N)");
            do {
                response = scanner.next();
                keepPlaying = response.equalsIgnoreCase("Y");
            } while(!(response.equalsIgnoreCase("Y")) && !(response.equalsIgnoreCase("N")));

            // if continue the game, reset the board
            if (keepPlaying) {
                this.board = new TicTacToeBoard(board.getSize(),board.getSize());
            }
            flag--;
        }
        //Reset all teams statistics when player change the game.
        resetAllTeamsStatistics();
    }

    //input the board size
    public void inputSize() {
        System.out.println("Please enter an integer that represents the side length: (8 >= integer >= 3)：");
        this.boardSize = scanner.nextInt();
        while (boardSize < 3 || boardSize > 8) {
            System.out.println("It's an invalid board. Please enter again！");
            this.boardSize = scanner.nextInt();
        }
        // create the new board
        this.board = new TicTacToeBoard(boardSize,boardSize);
    }

    //input player numbers and assign them to each team
    public Player inputPlayer(int teamNum) {
        System.out.println("Team " + (teamNum+1) + ", enter the current player number(input integer)：");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer:");
            scanner.next();
        }
        int teamPlayerNumber = scanner.nextInt();
        Player teamPlayer = new Player(null, null, 0);
        teamPlayer.setPlayerNumber(teamPlayerNumber);
        teams[teamNum].addTeamPlayer(teamPlayer);
        teamPlayer.setTeam(teams[teamNum]);
        return teamPlayer;
    }

    //choose the piece type(only "X" and "O" are allowed in TTT)
    public void choosePiece(Player team1Player, Player team2Player) {
        Piece piece1;
        Piece piece2;
        do {
            System.out.println("Team 1 (Player " + team1Player.getPlayerNumber() + "), please choose a piece type ('X' or 'O')：");
            team1Piece = scanner.next();
        } while (!team1Piece.equals("X") && !team1Piece.equals("O"));
        piece1 = new Piece(team1Piece);

        do {
            System.out.println("Team 2 (Player " + team2Player.getPlayerNumber() + "), please choose a piece type different from Team 1 ('X' or 'O')：");
            team2Piece = scanner.next();
        } while (team2Piece.equals(team1Piece) || (!team1Piece.equals("X") && !team1Piece.equals("O")));
        piece2 =new Piece(team2Piece);

        // Assign the piece types to the players.
        team1Player.setSymbol(piece1);
        team2Player.setSymbol(piece2);
    }

    // set the current player in each turn
    public Player setCurrentPlayer(Player player) {
        return player;
    }

    //set the wining status for the current player
    public void playerWins(Player player) {
        System.out.println(player.getTeam().getName() + "(Player " + player.getPlayerNumber() + ") wins!");
        player.setWin(true);
        player.getTeam().incrementWinCount();
    }

    //set the draw status for both players
    public void fullBoard(Player team1Player, Player team2Player) {
        team1Player.setDraw(true);
        team2Player.setDraw(true);
        handleDraw();
    }

    //switch the player when take turns
    public Player switchPlayer(Player currentPlayer, Player player1, Player player2) {
        return (currentPlayer == player1) ? player2 : player1;
    }
}
