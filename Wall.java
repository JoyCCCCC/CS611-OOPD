public class Wall extends Piece {
    private boolean horizontal = false;
    private boolean vertical = false;
    private int wallX;
    private int wallY;
    public Wall(String name) {
        super(name);
    }

    public void setX(int x){
        this.wallX = x;
    }
    public void setY(int y){
        this.wallY = y;
    }
    public int getWallX() {
        return this.wallX;
    }
    public int getWallY() {
        return this.wallY;
    }
    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }
    public void setVertical(boolean vertical){
        this.vertical = vertical;
    }
    public boolean getHorizontal(){
        return this.horizontal;
    }
    public boolean getVertical(){
        return this.vertical;
    }
}