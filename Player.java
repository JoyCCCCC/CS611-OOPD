import java.util.Scanner;

public class Player {
    private Piece symbol;  // piece type (X or O)
    private int playerNumber; //the number of the player
    private Team team;    // The corresponding team.
    private boolean won;  // Record whether the player won a specific game.
    private boolean draw; // Record whether the player had a draw.
    private int x, y;
    private int wallNumber;


    public Player(Piece symbol, Team team, Integer playerNumber) {
        this.symbol = symbol;
        this.team = team;
        this.playerNumber = playerNumber;
        this.won = false;
        this.draw = false;
        this.x = -1;
        this.y = -1;
        this.wallNumber = 0;
    }

    //get the piece type player chooses
    public Piece getSymbol() {
        return symbol;
    }

    //set the piece type player chooses
    public void setSymbol(Piece symbol) {
        this.symbol = symbol;
    }

    //get the team player belongs to
    public Team getTeam() {
        return team;
    }

    //record whether the player has won or not
    public boolean hasWon() {
        return won;
    }

    //set the winning condition
    public void setWin(boolean won) {
        this.won = won;
    }

    //get player number
    public int getPlayerNumber() {
        return playerNumber;
    }

    //set player number
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    //set the player's team
    public void setTeam(Team team) {
        this.team = team;
    }

    //whether the game is draw or not
    public boolean isDraw() {
        return draw;
    }

    //set the game be a draw
    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    //ask the player to make a move
    public void makeMove(GeneralBoard board) {
        Scanner scanner = new Scanner(System.in);
        int pos;
        do {
            System.out.println("Player " + playerNumber + " (" + team.getName() + ")" + " (" + symbol.getName() + "), please enter the index of cell：");
            pos = scanner.nextInt();
        } while (!board.isMoveValid(pos));
        board.makeMove(pos, symbol);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public int getWallNumber() {
        return wallNumber;
    }
    public void incrementWallNumber() {
        if (wallNumber < 10) {
            wallNumber++;
            System.out.println("You have " + (10 - wallNumber) + " walls left to place.");
        } else {
            System.out.println("You have no walls left to place.");
        }
    }
}
