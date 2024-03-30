// Adam Simcoe - 101442161
// Nhan Tran -
// Nhu Ly -
// Trang Nguyen - 100749684

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY = '-';
    private final String[] playerNames = new String[2];
    private final char[] playerSymbols = new char[2];
    private static final char[] SYMBOLS = {'R', 'Y'};

    private final char[][] board;
    private int currentPlayer;
    private final boolean isSinglePlayer;

    public ConnectFour(){
        // Create new board
        board = new char[ROWS][COLUMNS];

        // Fill each char in board with EMPTY ('-')
        for (char[] row: board) {
            java.util.Arrays.fill(row, EMPTY);
        }

        Scanner scanner = new Scanner(System.in);
        int numPlayers = 0;
        while (numPlayers != 1 && numPlayers != 2){
            System.out.print("Enter number of players (1 or 2): ");
            try {
                numPlayers = scanner.nextInt();
                if (numPlayers != 1 && numPlayers != 2) {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.nextLine();
            }
        }

        isSinglePlayer = numPlayers == 1;

        // If 2 players - name and symbol asked for each player
        if (!isSinglePlayer) {
            for (int i = 0; i < 2; i++) {
                System.out.print("Enter player " + (i + 1) + "'s name: ");
                playerNames[i] = scanner.next();
                char playerSymbol = ' ';
                while (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1] || (i == 1 && playerSymbol == playerSymbols[i-1])) {
                    System.out.printf("%s, choose your symbol ('%c' or '%c'): ", playerNames[i], SYMBOLS[0], SYMBOLS[1]);
                    playerSymbol = scanner.next().charAt(0);
                    if (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1]) {
                        System.out.printf("Invalid input. Please enter '%c' or '%c'.%n", SYMBOLS[0], SYMBOLS[1]);
                    } else if (i == 1 && playerSymbol == playerSymbols[0]) {
                        System.out.printf("Symbol already taken by %s. Please choose the remaining symbol.\n", playerNames[0]);
                    }
                }
                playerSymbols[i] = playerSymbol;
            }
        } else { // if Single player - ask for name, symbol and turn
            System.out.print("Enter your name: ");
            playerNames[0] = scanner.next();
            System.out.printf("%s, choose your symbol ('%c' or '%c'): ", playerNames[0], SYMBOLS[0], SYMBOLS[1]);
            char playerSymbol = ' ';
            while (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1]) {
                playerSymbol = scanner.next().charAt(0);
                if (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1]) {
                    System.out.printf("Invalid input. Please enter '%c' or '%c'.%n", SYMBOLS[0], SYMBOLS[1]);
                }
            }
            playerSymbols[0] = playerSymbol;
            //Set bot's symbol and name
            playerSymbols[1] = (playerSymbol == SYMBOLS[0]) ? SYMBOLS[1] : SYMBOLS[0];
            playerNames[1] = "Bot";

            System.out.print("Do you want to go first or second? (Enter 1 for first, 2 for second): ");
            int choice = 0;
            while (choice != 1 && choice != 2) {
                try {
                    choice = scanner.nextInt();
                    if (choice != 1 && choice != 2) {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number (1 or 2).");
                    scanner.nextLine();
                }
            }
            currentPlayer = (choice == 1) ? 0 : 1;
        }
    }

    public void playConnectFour() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int column = -1;
            boolean validInput = false;
            while (!validInput) {
                // 1 Player Game
                if (isSinglePlayer && currentPlayer == 1) {
                    //
                    // MINIMAX AI CODE HERE
                    System.out.print(playerNames[currentPlayer] + "'s turn. Enter column (1-" + COLUMNS + "): ");
                    column = aiMove();
                    validInput = true;

                } else { // 2 Player Game
                    System.out.print(playerNames[currentPlayer] + "'s turn. Enter column (1-" + COLUMNS + "): ");
                    try {
                        column = scanner.nextInt() - 1;
                        if (column < 0 || column >= COLUMNS) {
                            throw new ArrayIndexOutOfBoundsException("Column out of bounds. Please enter a number between 1 and " + COLUMNS);
                        }
                        validInput = true;
                        scanner.nextLine();
                    } catch (InputMismatchException e){
                        System.out.println("Invalid input. Please enter a number between 1 and " + COLUMNS);
                        printBoard();
                        scanner.nextLine();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Column out of bounds. Please enter a number between 1 and " + COLUMNS);
                        printBoard();
                        scanner.nextLine();
                    }
                }
            }
            int row = dropSymbol(column);
            if (row == -1) {
                System.out.println("Column is full. Try again.");
                continue;
            }
            printBoard();

            if (checkWin(row, column)) {
                System.out.println(playerNames[currentPlayer] + " wins!");
                break;
            } else if (checkDraw()) {
                System.out.println("It's a draw!");
                break;
            }

            currentPlayer = 1 - currentPlayer;  // Switch player
        }
    }

    private int dropSymbol(int column) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == EMPTY) {
                board[row][column] = playerSymbols[currentPlayer];
                return row;
            }
        }
        return -1;  // Column is full
    }

    private void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }


    // AI
    private int aiMove() {
        int bestColumn = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int column = 0; column < COLUMNS; column++) {
            if (board[0][column] == EMPTY) {
                int row = dropSymbol(column);
                int score = minimax(4, Integer.MIN_VALUE, Integer.MAX_VALUE, false, row, column);

                board[row][column] = EMPTY;

                // Update best score and column if score is higher
                if (score > bestScore) {
                    bestScore = score;
                    bestColumn = column;
                }
            }
        }

        return bestColumn;
    }

    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer, int lastMoveRow, int lastMoveColumn) {
        if (depth == 0 || checkWin(lastMoveRow, lastMoveColumn)) {
            if (checkWin(lastMoveRow, lastMoveColumn)) {
                if (currentPlayer == 1) {
                    return Integer.MIN_VALUE + depth; // faster wins for bot
                } else {
                    return Integer.MAX_VALUE - depth; // slower losses for bot
                }
            }
            return 0;
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int column = 0; column < COLUMNS; column++) {
                if (board[0][column] == EMPTY) {
                    int row = dropSymbol(column);
                    int eval = minimax(depth - 1, alpha, beta, false, row, column);
                    board[row][column] = EMPTY;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int column = 0; column < COLUMNS; column++) {
                if (board[0][column] == EMPTY) {
                    int row = dropSymbol(column);
                    int eval = minimax(depth - 1, alpha, beta, true, row, column);
                    board[row][column] = EMPTY;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    // Check Win Methods

    private boolean checkHorizontalWin(int row) {
        for (int column = 0; column < COLUMNS - 3; column++) {
            if (board[row][column] == playerSymbols[currentPlayer] &&
                    board[row][column+1] == playerSymbols[currentPlayer] &&
                    board[row][column+2] == playerSymbols[currentPlayer] &&
                    board[row][column+3] == playerSymbols[currentPlayer]) {
                return true;
            }

        }
        return false;
    }

    private boolean checkVerticalWin(int column) {
        for (int row = 0; row < ROWS - 3; row++) {
            if (board[row][column] == playerSymbols[currentPlayer] &&
                    board[row+1][column] == playerSymbols[currentPlayer] &&
                    board[row+2][column] == playerSymbols[currentPlayer] &&
                    board[row+3][column] == playerSymbols[currentPlayer]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalWin(int row, int column) {
        char playerSymbol = playerSymbols[currentPlayer];

        // DESC Diagonal (\)
        int count = 1;
        int i = 1;

        // Upwards check
        while (row - i >= 0 && column - i >= 0 && board[row - i][column - i] == playerSymbol) {
            count++;
            i++;
        }

        // Downwards check
        i = 1;
        while (row + i < ROWS && column + i < COLUMNS && board[row + i][column + i] == playerSymbol) {
            count++;
            i++;
        }
        if (count >= 4) return true;

        // ASC Diagonal (/)
        count = 1;
        i = 1;

        // Downwards check
        while (row + i < ROWS && column - i >= 0 && board[row + i][column - i] == playerSymbol) {
            count++;
            i++;
        }

        // Upwards check
        i = 1;
        while (row - i >= 0 && column + i < COLUMNS && board[row- i][column + i] == playerSymbol) {
            count++;
            i++;
        }
        if (count >= 4) return true;

        return false;
    }

    private boolean checkWin(int row, int column) {
        return checkHorizontalWin(row) || checkVerticalWin(column) || checkDiagonalWin(row, column);
    }

    private boolean checkDraw() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (board[row][column] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}