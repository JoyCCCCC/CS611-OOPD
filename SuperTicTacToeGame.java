import java.util.Scanner;

public class SuperTicTacToeGame extends TicTacToeGame {
    private SuperBoard superBoard;
    private String response;
    private Scanner scanner;

    public SuperTicTacToeGame(Team[] teams) {
        super(teams);
        this.superBoard = new SuperBoard();
        scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        boolean keepPlaying = true;
        char boardLabel;
        Piece drawPiece = new Piece("D");

        while (keepPlaying) {
            System.out.println("Super Tic Tac Toe Game starts! This is a super board consisting of 9 small boards, and each big board is referred to by A-I respectively.");
            superBoard.display();

            Player team1Player = inputPlayer(0);
            Player team2Player = inputPlayer(1);

            choosePiece(team1Player, team2Player);

            Player currentPlayer = setCurrentPlayer(team1Player);

            // The game loops until there is a winner or a draw on the super board
            while (true) {
                // player enters the symbol of the mini boards
                do {
                    System.out.println("Player " + currentPlayer.getPlayerNumber() + " (" + currentPlayer.getTeam().getName() + "), please enter the board label (A-I):");
                    String input = scanner.next();
                    boardLabel = input.charAt(0);
                }while(boardLabel < 'A' || boardLabel > 'I');

                // get the mini board based on the input
                TicTacToeBoard miniBoard = getMiniBoard(boardLabel);
                // Check if the game on mini board is over (win or draw)
                if (miniBoard.checkForWinner() || miniBoard.isFull()) {
                    System.out.println("This board is already decided. Please choose another.");
                    continue;
                }

                //Players move their pieces.
                currentPlayer.makeMove(miniBoard);
                superBoard.display();

                // If a winner has been determined on the mini board, the entire mini board is marked as that player's piece.
                if (miniBoard.checkForWinner()) {
                    System.out.println("Player " + currentPlayer.getPlayerNumber() + " (" + currentPlayer.getTeam().getName() + ") won on board " + boardLabel + "!");
                    superBoard.makeMove(boardLabelToIndex(boardLabel)+1,0,currentPlayer.getSymbol());
                }

                //I chose "D" to indicate a draw on this mini board.
                if(miniBoard.isFull()) {
                    System.out.println("There is a draw on Board " + boardLabel + "!");
                    superBoard.makeMove(boardLabelToIndex(boardLabel)+1,0,drawPiece);
                }

                // Check if the super board has a win.
                if (superBoard.checkForWinner()) {
                    playerWins(currentPlayer);
                    break;
                }

                // Check if the super board has a draw.
                if (superBoard.isFull()) {
                    fullBoard(team1Player, team2Player);
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

            if (keepPlaying) {
                this.superBoard = new SuperBoard(); // reset the super board
            }
        }

        // If players don't want to play again, reset all team statistics.
        resetAllTeamsStatistics();
    }

    // Get the corresponding mini board according to the input characters A-I.
    private TicTacToeBoard getMiniBoard(char boardLabel) {
        int boardIndex = boardLabelToIndex(boardLabel);
        TicTacToeBoard[][] miniBoards = superBoard.getMiniBoards();
        int row = boardIndex / 3; // the row of super board
        int col = boardIndex % 3; // the column of super board
        return miniBoards[row][col];
    }

    // Convert A-I to indices from 0-8.
    private int boardLabelToIndex(char boardLabel) {
        return boardLabel - 'A'; // Convert A to 0, B to 1, and so on.
    }
}
