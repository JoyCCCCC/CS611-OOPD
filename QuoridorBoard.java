import java.util.LinkedList;
import java.util.Queue;

public class QuoridorBoard {
    private final int size = 9;
    private char[][] board;
    private Wall[][] walls;

    public QuoridorBoard() {
        board = new char[size * 2 - 1][size * 2 - 1];
        walls = new Wall[size * 2 - 1][size * 2 - 1];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size * 2 - 1; i++) {
            for (int j = 0; j < size * 2 - 1; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    board[i][j] = '.';
                } else {
                    board[i][j] = ' ';
                }
            }
        }
    }

    public void placePlayer(int x, int y, char player) {
        board[x * 2][y * 2] = player;
    }

    public boolean placeHorizontalWall(int x, int y, int player1X, int player1Y, int player2X, int player2Y) {
        int boardX = x * 2 + 1;
        int boardY = y * 2;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX, boardY + 2)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX][boardY + 2] != null) {
            return false;
        }
        // Put a temp wall
        walls[boardX][boardY] = new Wall("-");
        walls[boardX][boardY + 2] = new Wall("-");
        board[boardX][boardY] = '-';
        board[boardX][boardY + 1] = '-';
        board[boardX][boardY + 2] = '-';


        if (!hasPathToGoal(player1X, player1Y, player2X, player2Y)) {
            // No path, drawback
            walls[boardX][boardY] = null;
            walls[boardX][boardY + 2] = null;
            board[boardX][boardY] = ' ';
            board[boardX][boardY + 1] = ' ';
            board[boardX][boardY + 2] = ' ';
            return false;
        }
        return true;
    }

    public boolean placeVerticalWall(int x, int y, int player1X, int player1Y, int player2X, int player2Y) {
        int boardX = x * 2;
        int boardY = y * 2 + 1;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX + 2, boardY)) {
            return false;
        }
        if (walls[boardX][boardY] != null && walls[boardX + 2][boardY] != null) {
            return false;
        }
        // Put a temp wall
        walls[boardX][boardY] = new Wall("|");
        walls[boardX + 2][boardY] = new Wall("|");
        board[boardX][boardY] = '|';
        board[boardX + 1][boardY] = '|';
        board[boardX + 2][boardY] = '|';

        // 检查路径
        if (!hasPathToGoal(player1X, player1Y, player2X, player2Y)) {
            // No path, drawback
            walls[boardX][boardY] = null;
            walls[boardX + 2][boardY] = null;
            board[boardX][boardY] = ' ';
            board[boardX + 1][boardY] = ' ';
            board[boardX + 2][boardY] = ' ';
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
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }


    // Determine a move is valid or not
    public boolean isValidMove(int startX, int startY, int endX, int endY, int opponentX, int opponentY) {
        if (endX < 0 || endX >= size || endY < 0 || endY >= size) {
            return false;
        }
        if (Math.abs(startX - endX) + Math.abs(startY - endY) != 1) {
            // Allow jumping over the opponent
            if (Math.abs(startX - endX) + Math.abs(startY - endY) == 2) {
                if ((startX == opponentX && Math.abs(startY - opponentY) == 1 && Math.abs(endY - startY) == 2) ||
                        (startY == opponentY && Math.abs(startX - opponentX) == 1 && Math.abs(endX - startX) == 2)) {
                    // Check if the target jumping cell is valid
                    return !isBlockedByWall(startX, startY, opponentX, opponentY) &&
                            !isBlockedByWall(opponentX, opponentY, endX, endY);
                }
            }
            return false;
        }
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

    private boolean isBlockedByWall(int startX, int startY, int endX, int endY) {
        // Determine if move between start and end is blocked by a wall
        if (startX == endX) {
            if (startY < endY && walls[startX * 2][startY * 2 + 1] != null) {
                return true;
            }
            if (startY > endY && walls[startX * 2][startY * 2 - 1] != null) {
                return true;
            }
        } else {
            if (startX < endX && walls[startX * 2 + 1][startY * 2] != null) {
                return true;
            }
            if (startX > endX && walls[startX * 2 - 1][startY * 2] != null) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPathToGoal(int player1X, int player1Y, int player2X, int player2Y) {
        return bfs(player1X, player1Y, 8) && bfs(player2X, player2Y, 0);
    }

    // A bfs algo to find a path to the goal
    private boolean bfs(int startX, int startY, int goalX) {
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

            if (x == goalX) {
                return true;
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < size && newY >= 0 && newY < size && !visited[newX][newY] && isValidMove(x, y, newX, newY,-1,-1)) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                }
            }
        }
        return false;
    }
}