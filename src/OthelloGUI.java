import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class OthelloGUI extends JFrame {
    private OthelloModel model;
    private JButton[][] buttons;
    private JPanel boardPanel;
    private JPanel startPanel;
    private JButton startButton;
    private JButton restartButton;

    public OthelloGUI() {
        setTitle("Othello Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new CardLayout());

        initStartScreen();
        initBoardScreen();

        setVisible(true);
    }

    private void initStartScreen() {
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        JLabel welcome = new JLabel("Welcome to Othello!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.addActionListener(e -> showGameBoard());

        startPanel.add(welcome, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.SOUTH);
        add(startPanel, "Start");
    }

    private void initBoardScreen() {
        model = new OthelloModel();
        buttons = new JButton[OthelloModel.SIZE][OthelloModel.SIZE];
        boardPanel = new JPanel(new GridLayout(OthelloModel.SIZE, OthelloModel.SIZE));
        boardPanel.setBackground(new Color(0, 100, 0));

        for (int i = 0; i < OthelloModel.SIZE; i++) {
            for (int j = 0; j < OthelloModel.SIZE; j++) {
                JButton button = new JButton();
                button.setOpaque(true);
                button.setBackground(new Color(0, 100, 0));
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                final int row = i, col = j;
                button.addActionListener(e -> handleMove(row, col));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(restartButton);

        JPanel container = new JPanel(new BorderLayout());
        container.add(boardPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        add(container, "Game");
    }

    private void showGameBoard() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Game");
        updateBoard();
    }

    private void handleMove(int row, int col) {
        if (!model.isValidMove(row, col, OthelloModel.BLACK)) return;
        if (model.makeMove(row, col, OthelloModel.BLACK)) {
            updateBoard();
            Timer delayTimer = new Timer(700, e -> makeComputerMove());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void makeComputerMove() {
        if (!model.hasValidMove(OthelloModel.WHITE)) {
            if (!model.hasValidMove(OthelloModel.BLACK)) {
                showGameOver();
                return;
            }
            return;
        }
        int[] move = model.getGreedyMove(OthelloModel.WHITE);
        if (move != null && model.makeMove(move[0], move[1], OthelloModel.WHITE)) {
            updateBoard();
        }

        if (!model.hasValidMove(OthelloModel.BLACK) && !model.hasValidMove(OthelloModel.WHITE)) {
            showGameOver();
        }
    }

    private void updateBoard() {
        int[][] board = model.getBoard();
        List<int[]> validMoves = model.getValidMoves(OthelloModel.BLACK);

        for (int i = 0; i < OthelloModel.SIZE; i++) {
            for (int j = 0; j < OthelloModel.SIZE; j++) {
                JButton btn = buttons[i][j];
                btn.setIcon(null);
                btn.setBackground(new Color(0, 100, 0));

                if (board[i][j] == OthelloModel.BLACK) {
                    btn.setIcon(createCircleIcon(Color.BLACK));
                } else if (board[i][j] == OthelloModel.WHITE) {
                    btn.setIcon(createCircleIcon(Color.WHITE));
                } else if (isInList(validMoves, i, j)) {
                    btn.setBackground(new Color(50, 150, 50)); // highlight
                }
            }
        }
        setTitle("Othello - Black: " + model.countPieces(OthelloModel.BLACK) + " | White: " + model.countPieces(OthelloModel.WHITE));
    }

    private void showGameOver() {
        String message = "Game Over!\nBlack: " + model.countPieces(OthelloModel.BLACK) +
                "\nWhite: " + model.countPieces(OthelloModel.WHITE);
        JOptionPane.showMessageDialog(this, message);
    }

    private void restartGame() {
        model.resetBoard();
        updateBoard();
    }

    private boolean isInList(List<int[]> list, int row, int col) {
        for (int[] move : list) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }

    private Icon createCircleIcon(Color color) {
        int size = 50;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(5, 5, size - 10, size - 10);
        g2.setColor(Color.BLACK);
        g2.drawOval(5, 5, size - 10, size - 10);
        g2.dispose();
        return new ImageIcon(image);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OthelloGUI::new);
    }
}