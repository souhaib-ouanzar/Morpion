import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 650; // 50px for the text on top and 50px for the restart button

    private final JFrame frame = new JFrame("Tic-Tac-Toe");
    private final JLabel textLabel = new JLabel();
    private final JPanel textPanel = new JPanel();
    private final JPanel boardPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();

    private final JButton[][] board = new JButton[3][3];
    private final JButton restartButton = new JButton("Restart");
    private final JLabel scoreLabel = new JLabel();

    private final String PLAYER_X = "X";
    private final String PLAYER_O = "O";
    private String currentPlayer = PLAYER_X;
    private boolean gameover = false;
    private int turns = 0;
    private int scoreX = 0;
    private int scoreO = 0;

    public TicTacToe() {
        frame.setVisible(true);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setupTextPanel();
        setupBoardPanel();
        setupControlPanel();

        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private void setupTextPanel() {
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
    }

    private void setupBoardPanel() {
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameover) return;
                        JButton clickedTile = (JButton) e.getSource();
                        if (clickedTile.getText().isEmpty()) {
                            clickedTile.setText(currentPlayer);
                            turns++;
                            checkWinner();
                            if (!gameover) {
                                currentPlayer = currentPlayer.equals(PLAYER_X) ? PLAYER_O : PLAYER_X;
                                textLabel.setText(currentPlayer);
                            }
                        }
                    }
                });
            }
        }
    }

    private void setupControlPanel() {
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBackground(Color.darkGray);
        controlPanel.add(scoreLabel, BorderLayout.CENTER);
        controlPanel.add(restartButton, BorderLayout.EAST);

        scoreLabel.setBackground(Color.darkGray);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreLabel();

        restartButton.setBackground(Color.lightGray);
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score - X: " + scoreX + " | O: " + scoreO);
    }

    private void resetBoard() {
        gameover = false;
        turns = 0;
        currentPlayer = PLAYER_X;
        textLabel.setText("Tic-Tac-Toe");

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setForeground(Color.white);
                board[r][c].setBackground(Color.darkGray);
            }
        }
    }

    private void checkWinner() {
        // Check horizontal
        for (int r = 0; r < 3; r++) {
            if (!board[r][0].getText().isEmpty() &&
                board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                declareWinner(board[r][0].getText());
                highlightWinningTiles(new JButton[]{board[r][0], board[r][1], board[r][2]});
                return;
            }
        }
        // Check vertical
        for (int c = 0; c < 3; c++) {
            if (!board[0][c].getText().isEmpty() &&
                board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                declareWinner(board[0][c].getText());
                highlightWinningTiles(new JButton[]{board[0][c], board[1][c], board[2][c]});
                return;
            }
        }
        // Check diagonal
        if (!board[0][0].getText().isEmpty() &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            declareWinner(board[0][0].getText());
            highlightWinningTiles(new JButton[]{board[0][0], board[1][1], board[2][2]});
            return;
        }
        if (!board[0][2].getText().isEmpty() &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            declareWinner(board[0][2].getText());
            highlightWinningTiles(new JButton[]{board[0][2], board[1][1], board[2][0]});
            return;
        }
        // Check for tie
        if (turns == 9) {
            declareTie();
        }
    }

    private void declareWinner(String winner) {
        gameover = true;
        if (winner.equals(PLAYER_X)) {
            scoreX++;
        } else {
            scoreO++;
        }
        updateScoreLabel();
        textLabel.setText(winner + " is the winner!");
    }

    private void declareTie() {
        gameover = true;
        textLabel.setText("It's a tie!");
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setForeground(Color.orange);
                board[r][c].setBackground(Color.gray);
            }
        }
    }

    private void highlightWinningTiles(JButton[] winningTiles) {
        for (JButton tile : winningTiles) {
            tile.setForeground(Color.green);
            tile.setBackground(Color.gray);
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
