import java.util.LinkedList;
import java.util.Queue;

public class QuoridorBoard {
    private final int size = 9;
    private final Tile[][] board;
    private final Wall[][] walls;

    public QuoridorBoard() {
        board = new Tile[size * 2 - 1][size * 2 - 1];
        walls = new Wall[size * 2 - 1][size * 2 - 1];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size * 2 - 1; i++) {
            for (int j = 0; j < size * 2 - 1; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board[i][j] = new Tile(".");
                } else {
                    board[i][j] = new Tile(" ");
                }
            }
        }
    }

    public void placePlayer(int x, int y, Piece piece) {
        if (piece.getName().equals(".")) {
            board[x * 2][y * 2] = new Tile(".");
        } else {
            board[x * 2][y * 2].setPiece(piece);
        }
    }

    public boolean placeHorizontalWall(Wall wall, Player[] players) {
        int boardX = wall.getWallX() * 2 + 1;
        int boardY = wall.getWallY() * 2;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX, boardY + 2)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX][boardY + 2] != null) {
            return false;
        }
        // Put a temp wall
        walls[boardX][boardY] = wall;
        walls[boardX][boardY + 2] = wall;
        board[boardX][boardY].setPiece(wall);
        board[boardX][boardY + 1].setPiece(wall);
        board[boardX][boardY + 2].setPiece(wall);

        if (hasPathToGoal(players)) {
            // No path, drawback
            walls[boardX][boardY] = null;
            walls[boardX][boardY + 2] = null;
            board[boardX][boardY] = new Tile(" ");
            board[boardX][boardY + 1] = new Tile(" ");
            board[boardX][boardY + 2] = new Tile(" ");
            return false;
        }
        return true;
    }

    public boolean placeVerticalWall(Wall wall, Player[] players) {
        int boardX = wall.getWallX() * 2;
        int boardY = wall.getWallY() * 2 + 1;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX + 2, boardY)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX + 2][boardY] != null) {
            return false;
        }
        // Put a temp wall
        walls[boardX][boardY] = wall;
        walls[boardX + 2][boardY] = wall;
        board[boardX][boardY].setPiece(wall);
        board[boardX + 1][boardY].setPiece(wall);
        board[boardX + 2][boardY].setPiece(wall);

        if (hasPathToGoal(players)) {
            // No path, drawback
            walls[boardX][boardY] = null;
            walls[boardX + 2][boardY] = null;
            board[boardX][boardY] = new Tile(" ");
            board[boardX + 1][boardY] = new Tile(" ");
            board[boardX + 2][boardY] = new Tile(" ");
            return false;
        }
        return true;
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= size * 2 - 1 || y < 0 || y >= size * 2 - 1;
    }

    public void display() {
        // Print top column numbers aligned with board positions
        System.out.print("  "); // Offset for alignment
        for (int i = 1; i < size; i++) {
            System.out.print("  " + i + " ");
        }
        System.out.println();

        for (int i = 0; i < size * 2 - 1; i++) {
            // Print left row numbers aligned with board positions
            if (i % 2 == 0) {
                System.out.print("  "); // Leave space for wall rows
            } else {
                System.out.print((i / 2 + 1) + " "); // Print row numbers between dot lines
            }

            for (int j = 0; j < size * 2 - 1; j++) {
                System.out.print(board[i][j].getPieceName() + " ");
            }
            System.out.println();
        }
    }

    // Determine a move is valid or not
    public boolean isValidMove(int startX, int startY, int endX, int endY, Player[] players) {
        if (endX < 0 || endX >= size || endY < 0 || endY >= size) {
            return false;
        }
        if (Math.abs(startX - endX) + Math.abs(startY - endY) != 1) {
            // Allow jumping over the opponent
            if (Math.abs(startX - endX) + Math.abs(startY - endY) == 2) {
                for (Player opponent : players) {
                    int opponentX = opponent.getX();
                    int opponentY = opponent.getY();
                    if ((startX == opponentX && Math.abs(startY - opponentY) == 1 && Math.abs(endY - startY) == 2) ||
                            (startY == opponentY && Math.abs(startX - opponentX) == 1 && Math.abs(endX - startX) == 2)) {
                        // Check if the target jumping cell is valid
                        if (isBlockedByWall(startX, startY, opponentX, opponentY) &&
                                isBlockedByWall(opponentX, opponentY, endX, endY)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;
        }
        return isBlockedByWall(startX, startY, endX, endY);
    }

    private boolean isBlockedByWall(int startX, int startY, int endX, int endY) {
        // Determine if move between start and end is blocked by a wall
        if (startX == endX) {
            if (startY < endY && walls[startX * 2][startY * 2 + 1] != null) {
                return false;
            }
            if (startY > endY && walls[startX * 2][startY * 2 - 1] != null) {
                return false;
            }
        } else {
            if (startX < endX && walls[startX * 2 + 1][startY * 2] != null) {
                return false;
            }
            if (startX > endX && walls[startX * 2 - 1][startY * 2] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPathToGoal(Player[] players) {
        for (Player player : players) {
            int goalX;
            int goalY;

            // Original logic for players "A" and "B" remains the same
            if (player.getSymbol().getName().equals("A") || player.getSymbol().getName().equals("B")) {
                goalX = player.getSymbol().getName().equals("A") ? 8 : 0;
                if (!bfs(player.getX(), player.getY(), goalX, players, true)) { // goal on the X axis
                    return true;
                }
            } else {
                // New logic for players "C" and "D", goal is on the Y axis
                goalY = player.getSymbol().getName().equals("C") ? 8 : 0;
                if (!bfs(player.getX(), player.getY(), goalY, players, false)) { // goal on the Y axis
                    return true;
                }
            }
        }
        return false;
    }

    // A bfs algo to find a path to the goal
    private boolean bfs(int startX, int startY, int goal, Player[] players, boolean goalOnX) {
        boolean[][] visited = new boolean[size][size];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (goalOnX && x == goal) { // Goal is on the X axis
                return true;
            } else if (!goalOnX && y == goal) { // Goal is on the Y axis
                return true;
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < size && newY >= 0 && newY < size && !visited[newX][newY] && isValidMove(x, y, newX, newY, players)) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                }
            }
        }
        return false;
    }
}