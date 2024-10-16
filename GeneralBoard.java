public class GeneralBoard {
    protected Tile[][] grid;
    protected int checkLength;

    // Initialize the general board with numbers
    public GeneralBoard(int size, int checkLength) {
        grid = new Tile[size][size]; // initialize the board with the size
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Tile(Integer.toString(num++)); // Assign the position number to the tile
            }
        }
        this.checkLength = checkLength;
    }

    // display the board on Terminal
    public void display() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print("| " + grid[i][j].getPieceName() + " ");
            }
            System.out.println("|");
        }
    }

    // check the move is valid or not
    public boolean isMoveValid(int pos) {
        int row = (pos - 1) / grid.length;
        int col = (pos - 1) % grid.length;
        if(row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            return !grid[row][col].isOccupied();
        }
        else
            return false;
    }

    //update the board with players placing pieces
    public void makeMove(int pos, Piece piece) {
        int row = (pos - 1) / grid.length;
        int col = (pos - 1) % grid.length;
        grid[row][col].setPiece(piece);
    }

    // check whether there is a winner
    public boolean checkForWinner() {
        for (int i = 0; i < grid.length; i++) {
            if (checkLine(getRow(i)) || checkLine(getColumn(i))) {
                return true;
            }
        }
        return checkLine(getDiagonal1()) || checkLine(getDiagonal2());
    }

    //Check whether a given row or column or diagonal has n consecutive identical pieces.(5 consecutive identical pieces for Order and Chaos especially)
    public boolean checkLine(Tile[] line) {
        for (int i = 0; i <= (line.length - checkLength); i++) {
            String first = line[i].getPieceName();
            boolean allSame = true;
            for (int j = i + 1; j < i + checkLength; j++) {
                if (!line[j].getPieceName().equals(first)) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) {
                return true;  // find 5 consecutive same pieces
            }
        }
        return false;
    }

    // get all cells in a row
    private Tile[] getRow(int row) {
        return grid[row];
    }

    //// get all cells in a column
    private Tile[] getColumn(int col) {
        Tile[] column = new Tile[grid.length];
        for (int i = 0; i < grid.length; i++) {
            column[i] = grid[i][col];
        }
        return column;
    }

    //Get the diagonal from the top-left to the bottom-right.
    private Tile[] getDiagonal1() {
        Tile[] diagonal = new Tile[grid.length];
        for (int i = 0; i < grid.length; i++) {
            diagonal[i] = grid[i][i];
        }
        return diagonal;
    }

    // Get the diagonal from the top-right to the bottom-left.
    private Tile[] getDiagonal2() {
        Tile[] diagonal = new Tile[grid.length];
        for (int i = 0; i < grid.length; i++) {
            diagonal[i] = grid[i][grid.length - i - 1];
        }
        return diagonal;
    }

    // check whether the board is full to determine the final winning condition
    public boolean isFull() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }


    // get the length of the board side
    public int getSize() {
        return grid.length;
    }
}

