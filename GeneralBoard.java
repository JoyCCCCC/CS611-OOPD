public interface GeneralBoard {
    //update the board with players placing pieces
    void makeMove(int x, int y, Piece piece);
    // display the board on Terminal
    void display();
    // check the move is valid or not
    boolean isValidMove(int startX, int startY, int endX, int endY, Player[] players);

}
