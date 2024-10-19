public class SuperBoard extends TicTacToeBoard {
    private TicTacToeBoard[][] miniBoards;

    public TicTacToeBoard[][] getMiniBoards(){
        return miniBoards;
    }

    public SuperBoard() {
        super(3,3); //the general board
        miniBoards = new TicTacToeBoard[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                miniBoards[i][j] = new TicTacToeBoard(3,3); // the size of mini boards is 3x3
                int num = 1;
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        miniBoards[i][j].grid[row][col] = new Tile(Integer.toString(num++)); // Assign position to Tile
                    }
                }
            }
        }
    }

    // display the board on Terminal
    @Override
    public void display() {
        // Display 3 rows of 3 mini boards each, making a 9x9 board
        for (int bigRow = 0; bigRow < 3; bigRow++) { // the rows of general board
            for (int innerRow = 0; innerRow < 3; innerRow++) { // the rows of mini board
                for (int bigCol = 0; bigCol < 3; bigCol++) { // the columns of general board
                    TicTacToeBoard currentMiniBoard = miniBoards[bigRow][bigCol];
                    // Display the row of the current mini board
                    for (int innerCol = 0; innerCol < 3; innerCol++) { // the columns of mini board
                        System.out.print(currentMiniBoard.grid[innerRow][innerCol].getPieceName() + "\t");
                    }
                    System.out.print("|"+"\t");
                }
                System.out.println();
            }
            System.out.println("-----------------------------");
        }
    }
}
