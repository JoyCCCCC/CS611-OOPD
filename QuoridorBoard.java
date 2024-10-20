import java.util.LinkedList;
import java.util.Queue;

public class QuoridorBoard implements GeneralBoard {
    // The board size (9x9)
    private final int size = 9;
    // The board is represented by a 2D array of Tile objects (including spaces for walls)
    private final Tile[][] board;
    // Walls are tracked in a 2D array
    private final Wall[][] walls;

    // Constructor initializes the board and wall arrays
    public QuoridorBoard() {
        board = new Tile[size * 2 - 1][size * 2 - 1];
        walls = new Wall[size * 2 - 1][size * 2 - 1];
        initializeBoard(); // Set initial state of the board
    }

    // Initializes the board with alternating dots (for players) and spaces (for walls)
    private void initializeBoard() {
        for (int i = 0; i < size * 2 - 1; i++) {
            for (int j = 0; j < size * 2 - 1; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board[i][j] = new Tile("."); // Place pieces at even positions
                } else {
                    board[i][j] = new Tile(" "); // Place empty spaces for walls
                }
            }
        }
    }

    // Move a piece to a specific position on the board
    public void makeMove(int x, int y, Piece piece) {
        if (piece.getName().equals(".")) {
            board[x * 2][y * 2] = new Tile(".");
        } else {
            board[x * 2][y * 2].setPiece(piece);
        }
    }

    // Places a horizontal wall and checks for validity
    public boolean placeHorizontalWall(Wall wall, Player[] players) {
        int boardX = wall.getWallX() * 2 + 1;
        int boardY = wall.getWallY() * 2;

        // Check if wall placement is out of bounds or overlaps another wall
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX, boardY + 2)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX][boardY + 2] != null) {
            return false;
        }

        // Temporarily place the wall
        walls[boardX][boardY] = wall;
        walls[boardX][boardY + 2] = wall;
        board[boardX][boardY].setPiece(wall);
        board[boardX][boardY + 1].setPiece(wall);
        board[boardX][boardY + 2].setPiece(wall);

        // Check if the wall blocks any player's path to the goal
        if (hasPathToGoal(players)) {
            // If it does block, remove the wall and return false
            walls[boardX][boardY] = null;
            walls[boardX][boardY + 2] = null;
            board[boardX][boardY] = new Tile(" ");
            board[boardX][boardY + 1] = new Tile(" ");
            board[boardX][boardY + 2] = new Tile(" ");
            return false;
        }
        return true;
    }

    // Places a vertical wall and checks for validity
    public boolean placeVerticalWall(Wall wall, Player[] players) {
        int boardX = wall.getWallX() * 2;
        int boardY = wall.getWallY() * 2 + 1;

        // Check if wall placement is out of bounds or overlaps another wall
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX + 2, boardY)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX + 2][boardY] != null) {
            return false;
        }

        // Temporarily place the wall
        walls[boardX][boardY] = wall;
        walls[boardX + 2][boardY] = wall;
        board[boardX][boardY].setPiece(wall);
        board[boardX + 1][boardY].setPiece(wall);
        board[boardX + 2][boardY].setPiece(wall);

        // Check if the wall blocks any player's path to the goal
        if (hasPathToGoal(players)) {
            // If it does block, remove the wall and return false
            walls[boardX][boardY] = null;
            walls[boardX + 2][boardY] = null;
            board[boardX][boardY] = new Tile(" ");
            board[boardX + 1][boardY] = new Tile(" ");
            board[boardX + 2][boardY] = new Tile(" ");
            return false;
        }
        return true;
    }

    // Helper method to check if a position is out of bounds
    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= size * 2 - 1 || y < 0 || y >= size * 2 - 1;
    }

    // Displays the board on the Terminal
    public void display() {
        // Print column numbers
        System.out.print("  ");
        for (int i = 1; i < size; i++) {
            System.out.print("  " + i + " ");
        }
        System.out.println();

        for (int i = 0; i < size * 2 - 1; i++) {
            // Print row numbers for the board
            if (i % 2 == 0) {
                System.out.print("  ");
            } else {
                System.out.print((i / 2 + 1) + " ");
            }

            // Print the contents of each tile on the board
            for (int j = 0; j < size * 2 - 1; j++) {
                System.out.print(board[i][j].getPieceName() + " ");
            }
            System.out.println();
        }
    }

    // Checks if a move is valid, including wall and boundary checks
    public boolean isValidMove(int startX, int startY, int endX, int endY, Player[] players) {
        // Check if the destination is out of bounds
        if (endX < 0 || endX >= size || endY < 0 || endY >= size) {
            return false;
        }

        // Check jump moves
        if (Math.abs(startX - endX) + Math.abs(startY - endY) != 1) {
            // Allow jump moves over opponents
            if (Math.abs(startX - endX) + Math.abs(startY - endY) == 2) {
                for (Player opponent : players) {
                    int opponentX = opponent.getX();
                    int opponentY = opponent.getY();
                    if ((startX == opponentX && Math.abs(startY - opponentY) == 1 && Math.abs(endY - startY) == 2) ||
                            (startY == opponentY && Math.abs(startX - opponentX) == 1 && Math.abs(endX - startX) == 2)) {
                        // Check if jumping is blocked by a wall
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

    // Check if a move is blocked by a wall
    private boolean isBlockedByWall(int startX, int startY, int endX, int endY) {
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

    // Check if every player has a valid path to their goal using BFS
    private boolean hasPathToGoal(Player[] players) {
        for (Player player : players) {
            int goalX;
            int goalY;

            // Players "A" and "B" use goalX logic
            if (player.getSymbol().getName().equals("A") || player.getSymbol().getName().equals("B")) {
                goalX = player.getSymbol().getName().equals("A") ? 8 : 0;
                if (!bfs(player.getX(), player.getY(), goalX, players, true)) { // Search on X axis
                    return true;
                }
            } else {
                // Players "C" and "D" use goalY logic
                goalY = player.getSymbol().getName().equals("C") ? 8 : 0;
                if (!bfs(player.getX(), player.getY(), goalY, players, false)) { // Search on Y axis
                    return true;
                }
            }
        }
        return false;
    }

    // BFS algorithm to find a path to the goal
    private boolean bfs(int startX, int startY, int goal, Player[] players, boolean goalOnX) {
        // Create a visited array to track which tiles have been checked
        boolean[][] visited = new boolean[size][size];
        Queue<int[]> queue = new LinkedList<>();

        // Add the starting position to the queue and mark it as visited
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        // Directions for moving (up, down, left, right)
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Continue the BFS search until the queue is empty
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            // Check if the player has reached the goal
            if (goalOnX && x == goal) { // Goal is on the X axis
                return true;
            } else if (!goalOnX && y == goal) { // Goal is on the Y axis
                return true;
            }

            // Explore all four possible directions (up, down, left, right)
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // Check if the new position is within bounds, not visited, and a valid move
                if (newX >= 0 && newX < size && newY >= 0 && newY < size && !visited[newX][newY] && isValidMove(x, y, newX, newY, players)) {
                    queue.add(new int[]{newX, newY});  // Add the new position to the queue
                    visited[newX][newY] = true;  // Mark the new position as visited
                }
            }
        }
        return false;  // Return false if no path to the goal is found
    }
}
