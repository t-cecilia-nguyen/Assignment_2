// Adam Simcoe -
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
                while (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1] || (i == 1 && playerSymbol == playerSymbols[0])) {
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
                    // ASK HUMAN FOR NAME
                    // PLAYER PROMPTED TO CHOOSE SYMBOL
                    // PLAYER PROMPTED TO GO FIRST OR SECOND
                    //
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

            //
            // CHECK WINNER FUNCTION HERE
            // CHECK WIN OR DRAW
            // DISPLAY WINNER OR DECLARE DRAW
            //

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
}
