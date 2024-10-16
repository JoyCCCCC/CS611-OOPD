public class Tile {
    private Piece piece; // Represents the piece on the tile
    private String position; // The number shown when the tile is empty

    public Tile(String position) {
        this.piece = null; // Initially no piece on the tile
        this.position = position;
    }

    // whether the tile is occupied by one piece or not
    public boolean isOccupied() {
        return piece != null;
    }

    //place the piece on the tile
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    //get the piece on the tile or tile number if not occupied by piece
    public String getPieceName() {
        return isOccupied() ? piece.getName() : position; // If no piece, return position number
    }
}