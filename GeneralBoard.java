public interface GeneralBoard {
    void makeMove(int x, int y, Piece piece);
    void display();
    boolean isValidMove(int startX, int startY, int endX, int endY, Player[] players);

}
