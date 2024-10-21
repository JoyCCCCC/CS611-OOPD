public class Wall extends Piece {
    // Boolean flags to indicate the orientation of the wall
    private boolean horizontal = false;
    private boolean vertical = false;

    // Coordinates of the wall's position on the board
    private int wallX;
    private int wallY;
    public Wall(String name) {
        super(name);
    }

    // assign the X-coordinate of the wall
    public void setX(int x){
        this.wallX = x;
    }
    // assign the Y-coordinate of the wall
    public void setY(int y){
        this.wallY = y;
    }
    // retrieve the X-coordinate of the wall
    public int getWallX() {
        return this.wallX;
    }
    // retrieve the Y-coordinate of the wall
    public int getWallY() {
        return this.wallY;
    }
    // mark the wall as horizontal
    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }
    // mark the wall as vertical
    public void setVertical(boolean vertical){
        this.vertical = vertical;
    }
}