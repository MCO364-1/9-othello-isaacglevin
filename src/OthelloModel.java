import java.util.*;

public class OthelloModel {
    public static final int SIZE = 8;
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] board;
    private int currentPlayer;

    public OthelloModel() {
        board = new int[SIZE][SIZE];
        resetBoard();
    }

    public void resetBoard() {
        for (int[] row : board)
            Arrays.fill(row, EMPTY);
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        currentPlayer = BLACK;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isValidMove(int row, int col, int player) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return false;
        if (board[row][col] != EMPTY) return false;
        return !getFlipped(row, col, player).isEmpty();
    }

    public boolean makeMove(int row, int col, int player) {
        List<int[]> toFlip = getFlipped(row, col, player);
        if (toFlip.isEmpty()) return false;
        board[row][col] = player;
        for (int[] flip : toFlip)
            board[flip[0]][flip[1]] = player;
        currentPlayer = 3 - player;
        return true;
    }

    private List<int[]> getFlipped(int row, int col, int player) {
        List<int[]> flipped = new ArrayList<>();
        int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
        int opponent = 3 - player;
        for (int d = 0; d < 8; d++) {
            List<int[]> temp = new ArrayList<>();
            int x = row + dx[d], y = col + dy[d];
            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == opponent) {
                temp.add(new int[]{x, y});
                x += dx[d];
                y += dy[d];
            }
            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == player)
                flipped.addAll(temp);
        }
        return flipped;
    }

    public boolean hasValidMove(int player) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (isValidMove(i, j, player)) return true;
        return false;
    }

    public int[] getGreedyMove(int player) {
        int maxFlips = -1;
        int[] bestMove = null;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                List<int[]> flips = getFlipped(i, j, player);
                if (!flips.isEmpty() && flips.size() > maxFlips) {
                    maxFlips = flips.size();
                    bestMove = new int[]{i, j};
                }
            }
        }
        return bestMove;
    }

    public int countPieces(int player) {
        int count = 0;
        for (int[] row : board)
            for (int cell : row)
                if (cell == player) count++;
        return count;
    }

    public List<int[]> getValidMoves(int player) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, player)) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }
}