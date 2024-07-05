import java.util.Scanner;

class Board {
    private static final int SIDE = 3;
    private char[][] board;

    public Board() {
        board = new char[SIDE][SIDE];
        initialize();
    }

    public void initialize() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                board[i][j] = '*';
            }
        }
    }

    public void show() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                System.out.print("\t" + board[i][j]);
                if (j < SIDE - 1) System.out.print(" |");
            }
            System.out.println();
            if (i < SIDE - 1) System.out.println("\t-----------");
        }
        System.out.println();
    }

    public boolean isCellEmpty(int x, int y) {
        return board[x][y] == '*';
    }

    public void makeMove(int x, int y, char move) {
        board[x][y] = move;
    }

    public boolean isGameOver() {
        return rowCrossed() || columnCrossed() || diagonalCrossed();
    }

    private boolean rowCrossed() {
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '*') {
                return true;
            }
        }
        return false;
    }

    private boolean columnCrossed() {
        for (int i = 0; i < SIDE; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '*') {
                return true;
            }
        }
        return false;
    }

    private boolean diagonalCrossed() {
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '*') {
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '*') {
            return true;
        }
        return false;
    }

    public char[][] getBoard() {
        return board;
    }
}

class Game {
    private static final char COMPUTERMOVE = 'O';
    private static final char HUMANMOVE = 'X';
    private static final int SIDE = 3;
    private Board board;
    private Scanner scanner;

    public Game() {
        board = new Board();
        scanner = new Scanner(System.in);
    }

    public void showInstructions() {
        System.out.println("Choose a cell numbered from 1 to 9 as below and play:");
        System.out.println("\t 1 | 2 | 3 ");
        System.out.println("\t-----------");
        System.out.println("\t 4 | 5 | 6 ");
        System.out.println("\t-----------");
        System.out.println("\t 7 | 8 | 9 ");
        System.out.println();
    }

    public void playTicTacToe(int whoseTurn) {
        int moveIndex = 0, x = 0, y = 0;
        board.initialize();
        showInstructions();
        board.show();

        while (!board.isGameOver() && moveIndex != SIDE * SIDE) {
            int n;
            if (whoseTurn == 1) {
                n = bestMove(board.getBoard(), moveIndex);
                x = n / SIDE;
                y = n % SIDE;
                board.makeMove(x, y, COMPUTERMOVE);
                System.out.println("COMPUTER has put a " + COMPUTERMOVE + " in cell " + (n + 1));
                board.show();
                moveIndex++;
                whoseTurn = 2;
            } else if (whoseTurn == 2) {
                System.out.print("You can insert in the following positions: ");
                for (int i = 0; i < SIDE; i++) {
                    for (int j = 0; j < SIDE; j++) {
                        if (board.isCellEmpty(i, j)) {
                            System.out.print((i * 3 + j) + 1 + " ");
                        }
                    }
                }
                System.out.println();
                System.out.print("Enter the position: ");
                n = scanner.nextInt();
                n--;
                x = n / SIDE;
                y = n % SIDE;
                if (board.isCellEmpty(x, y) && n < 9 && n >= 0) {
                    board.makeMove(x, y, HUMANMOVE);
                    System.out.println("HUMAN has put a " + HUMANMOVE + " in cell " + (n + 1));
                    board.show();
                    moveIndex++;
                    whoseTurn = 1;
                } else if (!board.isCellEmpty(x, y) && n < 9 && n >= 0) {
                    System.out.println("Position is occupied, select any one place from the available places.");
                } else if (n < 0 || n > 8) {
                    System.out.println("Invalid position.");
                }
            }
        }
        if (!board.isGameOver() && moveIndex == SIDE * SIDE) {
            System.out.println("It's a draw.");
        } else {
            declareWinner(whoseTurn);
        }
    }

    private void declareWinner(int whoseTurn) {
        if (whoseTurn == 1) {
            System.out.println("HUMAN has won");
        } else {
            System.out.println("COMPUTER has won");
        }
    }

    private int minimax(char[][] board, int depth, boolean isAI) {
        int score = 0;
        int bestScore = 0;
        if (gameOver(board)) {
            if (isAI) {
                return -10;
            } else {
                return +10;
            }
        } else {
            if (depth < 9) {
                if (isAI) {
                    bestScore = -999;
                    for (int i = 0; i < SIDE; i++) {
                        for (int j = 0; j < SIDE; j++) {
                            if (board[i][j] == '*') {
                                board[i][j] = COMPUTERMOVE;
                                score = minimax(board, depth + 1, false);
                                board[i][j] = '*';
                                if (score > bestScore) {
                                    bestScore = score;
                                }
                            }
                        }
                    }
                    return bestScore;
                } else {
                    bestScore = 999;
                    for (int i = 0; i < SIDE; i++) {
                        for (int j = 0; j < SIDE; j++) {
                            if (board[i][j] == '*') {
                                board[i][j] = HUMANMOVE;
                                score = minimax(board, depth + 1, true);
                                board[i][j] = '*';
                                if (score < bestScore) {
                                    bestScore = score;
                                }
                            }
                        }
                    }
                    return bestScore;
                }
            } else {
                return 0;
            }
        }
    }

    private boolean gameOver(char[][] board) {
        return rowCrossed(board) || columnCrossed(board) || diagonalCrossed(board);
    }

    private boolean rowCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '*') {
                return true;
            }
        }
        return false;
    }

    private boolean columnCrossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '*') {
                return true;
            }
        }
        return false;
    }

    private boolean diagonalCrossed(char[][] board) {
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '*') {
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '*') {
            return true;
        }
        return false;
    }

    private int bestMove(char[][] board, int moveIndex) {
        int x = -1, y = -1;
        int score = 0, bestScore = -999;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == '*') {
                    board[i][j] = COMPUTERMOVE;
                    score = minimax(board
