import java.util.Scanner;

public class OrderAndChaosGame extends TicTacToeGame {
    private String response;
    private Scanner scanner;
    public OrderAndChaosGame(Team[] teams) {
        super(teams);
        this.board = new GeneralBoard(6,5);
        scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        boolean keepPlaying = true;

        while(keepPlaying) {

            Player orderPlayer = inputPlayer(0);
            Player chaosPlayer = inputPlayer(1);

            // start the game and show the initial board
            System.out.println("Order and Chaos Game starts!");
            board.display();

            Player currentPlayer = setCurrentPlayer(orderPlayer);  // Team Order first

            // game main loop
            while (true) {
                System.out.println("Enter index of cell to place piece.");
                choosePiece(currentPlayer);

                currentPlayer.makeMove(board);  // The current player inputs their move, marking that they participated in the game.
                board.display();                // show the updated board

                // check whether the Order wins
                if (board.checkForWinner()) {
                    playerWins(orderPlayer);
                    break;
                }

                // Chaos winning condition
                if (board.isFull()) {
                    playerWins(chaosPlayer);
                    break;
                }

                // Switch to the next player.
                currentPlayer = switchPlayer(currentPlayer,orderPlayer,chaosPlayer);
            }

            // print the summary table
            printSummary();

            System.out.println("Do you want to continue the game？ (Y/N)");
            do {
                response = scanner.next();
                keepPlaying = response.equalsIgnoreCase("Y");
            } while(!(response.equalsIgnoreCase("Y")) && !(response.equalsIgnoreCase("N")));

            // if continue the game, reset the board
            if (keepPlaying) {
                this.board = new GeneralBoard(6,5);  // reset the board
            }
        }
        resetAllTeamsStatistics(); //If the player chooses not to continue the game, return to the main menu and reset the statistics.
    }

    //print the Order and Chaos summary table
    public void printSummary() {
        for (Team team : teams) {
            System.out.println("----------------" + team.getName() + "----------------");
            for (Player player : team.getPlayers()) {
                System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '' " + (player.hasWon() ? "win" : "lose"));
            }
            System.out.println("Total win: " + team.getWinCount());
        }
    }

    public Player inputPlayer(int teamNum) {
        String teamName;
        if(teamNum == 0){
            teamName = "ORDER";
        }else{
            teamName = "CHAOS";
        }
        System.out.println("Team "+ teamName +", enter the current player number(input integer)：");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer:");
            scanner.next();
        }
        int playerNumber = scanner.nextInt();
        teams[teamNum].setName(teamName);
        Player player = new Player(null, null, 0);
        player.setPlayerNumber(playerNumber);
        teams[teamNum].addTeamPlayer(player);
        player.setTeam(teams[teamNum]);
        return player;
    }

    public void choosePiece(Player currentPlayer) {
        System.out.println("Team " +  currentPlayer.getTeam().getName() + "(Player " + currentPlayer.getPlayerNumber() + "), choose the piece type ('X' or 'O')：");
        String chosenPiece = scanner.next();
        while (!chosenPiece.equals("X") && !chosenPiece.equals("O")) {
            System.out.println("It's an invalid choice. Please choose again ('X' or 'O')：");
            chosenPiece = scanner.next();
        }
        Piece piece = new Piece(chosenPiece);
        currentPlayer.setSymbol(piece);
    }

}
