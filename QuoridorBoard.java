import java.util.LinkedList;
import java.util.Queue;

public class QuoridorBoard {
    private final int size = 9;
    private char[][] board;
    private boolean[][] walls;

    public QuoridorBoard() {
        board = new char[size * 2 - 1][size * 2 - 1];
        walls = new boolean[size * 2 - 1][size * 2 - 1];
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

    public boolean placeHorizontalWall(int x, int y) {
        int boardX = x * 2 + 1;
        int boardY = y * 2;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX, boardY + 2)) {
            return false;
        }
        if (walls[boardX][boardY] && walls[boardX][boardY + 2]) {
            return false;
        }
        // 临时放置墙体
        walls[boardX][boardY] = true;
        walls[boardX][boardY + 2] = true;
        board[boardX][boardY] = '-';
        board[boardX][boardY + 1] = '-';
        board[boardX][boardY + 2] = '-';

        // 检查路径
        if (!hasPathToGoal()) {
            // 如果没有路径，撤销墙体
            walls[boardX][boardY] = false;
            walls[boardX][boardY + 2] = false;
            board[boardX][boardY] = ' ';
            board[boardX][boardY + 1] = ' ';
            board[boardX][boardY + 2] = ' ';
            return false;
        }
        return true;
    }

    public boolean placeVerticalWall(int x, int y) {
        int boardX = x * 2;
        int boardY = y * 2 + 1;
        if (isOutOfBounds(boardX, boardY) || isOutOfBounds(boardX + 2, boardY)) {
            return false;
        }
        if (walls[boardX][boardY] && walls[boardX + 2][boardY]) {
            return false;
        }
        // 临时放置墙体
        walls[boardX][boardY] = true;
        walls[boardX + 2][boardY] = true;
        board[boardX][boardY] = '|';
        board[boardX + 1][boardY] = '|';
        board[boardX + 2][boardY] = '|';

        // 检查路径
        if (!hasPathToGoal()) {
            // 如果没有路径，撤销墙体
            walls[boardX][boardY] = false;
            walls[boardX + 2][boardY] = false;
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
        for (int i = 0; i < size * 2 - 1; i++) {
            for (int j = 0; j < size * 2 - 1; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

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
            if (startY < endY && walls[startX * 2][startY * 2 + 1]) {
                return false;
            }
            if (startY > endY && walls[startX * 2][startY * 2 - 1]) {
                return false;
            }
        } else {
            if (startX < endX && walls[startX * 2 + 1][startY * 2]) {
                return false;
            }
            if (startX > endX && walls[startX * 2 - 1][startY * 2]) {
                return false;
            }
        }
        return true;
    }

    private boolean isBlockedByWall(int startX, int startY, int endX, int endY) {
        // Determine if move between start and end is blocked by a wall
        if (startX == endX) {
            if (startY < endY && walls[startX * 2][startY * 2 + 1]) {
                return true;
            }
            if (startY > endY && walls[startX * 2][startY * 2 - 1]) {
                return true;
            }
        } else {
            if (startX < endX && walls[startX * 2 + 1][startY * 2]) {
                return true;
            }
            if (startX > endX && walls[startX * 2 - 1][startY * 2]) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPathToGoal() {
        return bfs(0, 4, 8) && bfs(8, 4, 0);
    }

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