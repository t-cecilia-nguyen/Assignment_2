
// Adam Simcoe - 101442161
// Nhan Tran -
// Nhu Ly -
// Trang Nguyen - 100749684

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;


public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY = '-';
    private final String[] playerNames = new String[2];
    private final char[] playerSymbols = new char[2];
    private static final char[] SYMBOLS = {'R', 'Y'};

    private char[][] board;
    private int currentPlayer;
    private boolean isSinglePlayer;
    private static final char[] PLAYERS = {'R', 'Y'};


    public ConnectFour() {
        // Create new board
        board = new char[ROWS][COLUMNS];

        // Fill each char in board with EMPTY ('-')
        for (char[] row : board) {
            java.util.Arrays.fill(row, EMPTY);
        }

        Scanner scanner = new Scanner(System.in);
        int numPlayers = 0;
        while (numPlayers != 1 && numPlayers != 2) {
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
                while (playerSymbol != SYMBOLS[0] && playerSymbol != SYMBOLS[1] || (i == 1 && playerSymbol == playerSymbols[i - 1])) {
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
                    column = aiMove(board);
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
                    } catch (InputMismatchException e) {
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
                String winningMsg =
                                "\n-----  Yahooo  -----\n" +
                                "-`-`-`  \\O/  `-`-`-`\n" +
                                "`-`-`-   |   -`-`-`-\n" +
                                "-`-`-`  / \\  `-`-`-`";
                System.out.println(winningMsg);
                System.out.println("---- " + playerNames[currentPlayer] + " wins! ----");
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



    //////-----AI--------///////
    private int dropSymbolTemp(char[][] tempBoard, int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (tempBoard[row][col] == EMPTY) {
                tempBoard[row][col] = playerSymbols[currentPlayer];
                return row; // Return the row where the symbol was successfully dropped
            }
        }
        return -1; // Column is full, unable to drop the symbol
    }

    private int aiMove(char[][] board) {
        int bestColumn = -1;
        int bestRow = -1;
        int bestScore = Integer.MIN_VALUE;
        char[][] tempBoard = new char[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            tempBoard[i] = Arrays.copyOf(board[i], COLUMNS);
        }

        for (int col = 0; col < COLUMNS; col++) {

            int newRow = dropSymbolTemp(tempBoard, col);
            if(newRow != -1) {
                int score = minimax(tempBoard, newRow, col, 5, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);

                if (score > bestScore) {
                    bestScore = score;
                    bestColumn = col;
                    bestRow = newRow;
                }
            }

        }

        System.out.println("\nFINAL BEST SCORE: " + bestScore + " at row: " + bestRow + " ,column: " + bestColumn);
        return bestColumn;
    }


    private boolean isRowFull(char[][] board, int row) {
        for (int col = 0; col < COLUMNS; col++) {
            if (board[row][col] == EMPTY) {
                return false; // Row is not full
            }
        }
        return true; // Row is full
    }

    //@ Calculate weight base on each depth
    private int roundWeight(int round) {
        // Calculate weight based on round number
        int weight = 1;
        for (int i = 1; i <= round; i++) {
            weight *= (COLUMNS - i + 1);
        }
        return weight;
    }

    private int minimax(char[][] tempBoard, int row, int column, int depth, boolean maximizingPlayer, int alpha, int beta, int round) {
        int score;
        if (depth == 0 || checkWin(row, column) || checkDraw()) {
            // return accumulated score
            score = calculateScore(tempBoard, row, column, round);
            return score;
        }

        if (maximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int col =0 ; col <COLUMNS; col++) {
                if (tempBoard[row][col] == EMPTY) {
                    int newRow = dropSymbolTemp(tempBoard, col);
                    if (newRow != -1) {
                        score = minimax(tempBoard, newRow, col, depth - 1, false, alpha, beta, round + 1);
                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, score);
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col =0 ; col <COLUMNS; col++) {
                if (tempBoard[row][col] == EMPTY) {
                    int newRow = dropSymbolTemp(tempBoard, col);
                    if (newRow != -1) {
                        score = minimax(tempBoard, newRow, col, depth - 1, true, alpha, beta, round + 1);
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, score);
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }
            return bestScore;
        }
    }




    private int calculateScore(char[][] tempBoard, int row, int column, int round) {
        int score = 0;


        score += countConsecutiveSymbols(tempBoard, row, column, 0, 1, round); // Horizontal
        score += countConsecutiveSymbols(tempBoard, row, column, 1, 0, round); // Vertical
        score += countConsecutiveSymbols(tempBoard, row, column, 1, 1, round); // (\)
        score += countConsecutiveSymbols(tempBoard, row, column, 1, -1, round); // (/)

        return score;
    }

    private int countConsecutiveSymbols(char[][] tempBoard, int row, int column, int dRow, int dColumn, int round) {
        int player1Count = countConsecutiveSymbolsInDirection(tempBoard, row, column, dRow, dColumn, playerSymbols[currentPlayer]);
        int player2Count = countConsecutiveSymbolsInDirection(tempBoard, row, column, dRow, dColumn, getOpponentSymbol());

        return scoreMove(player1Count, player2Count, round);
    }

    private int countConsecutiveSymbolsInDirection(char[][] tempBoard, int row, int column, int dRow, int dColumn, char symbol) {
        int count = 0;
        int r = row;
        int c = column;

        // Check in the positive direction
        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && tempBoard[r][c] == symbol && count < 4) {
            count++;
            r += dRow;
            c += dColumn;
        }

        // Check in the negative direction
        r = row - dRow;
        c = column - dColumn;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && tempBoard[r][c] == symbol && count < 4) {
            count++;
            r -= dRow;
            c -= dColumn;
        }

        return count;
    }

    private char getOpponentSymbol() {
        char playerSymbol = playerSymbols[currentPlayer];
        return (playerSymbol == SYMBOLS[0]) ? SYMBOLS[1] : SYMBOLS[0];
    }

    private int scoreMove(int player1Count, int player2Count, int round) {
        if (player1Count == 4) {
            return 1000 + roundWeight(round);
        } else if (player2Count == 4) {
            return -1000 - roundWeight(round);
        } else if (player1Count == 3 && player2Count == 0) {
            return 100 + roundWeight(round);
        } else if (player1Count == 2 && player2Count == 0) {
            return 10 + roundWeight(round);
        } else if (player1Count == 1 && player2Count == 0) {
            return 1 + roundWeight(round);
        } else if (player2Count == 3 && player1Count == 0) {
            return -100 - roundWeight(round);
        } else if (player2Count == 2 && player1Count == 0) {
            return -10 - roundWeight(round);
        } else if (player2Count == 1 && player1Count == 0) {
            return -1 - roundWeight(round);
        }

        return 0; // Neutral
    }





//    private int countConsecutiveSymbols(char[][] tempBoard, int row, int column, int dRow, int dColumn, int round) {
//        int player1Count = 0;
//        int player2Count = 0;
//
//        char playerSymbol = playerSymbols[currentPlayer];
//        char opponentSymbol = (playerSymbol == SYMBOLS[0]) ? SYMBOLS[1] : SYMBOLS[0];
//
//        int r = row;
//        int c = column;
//
//        // Check in the positive direction
//        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS &&
//                tempBoard[r][c] == playerSymbol && player1Count < 4) {
//            player1Count++;
//            r += dRow;
//            c += dColumn;
//        }
//
//        // Check in the negative direction
//        r = row - dRow;
//        c = column - dColumn;
//        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS &&
//                tempBoard[r][c] == playerSymbol && player1Count < 4) {
//            player1Count++;
//            r -= dRow;
//            c -= dColumn;
//        }
//
//        // Similar logic for opponent's symbols
//        r = row;
//        c = column;
//
//        // Check in the positive direction
//        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS &&
//                tempBoard[r][c] == opponentSymbol && player2Count < 4) {
//            player2Count++;
//            r += dRow;
//            c += dColumn;
//        }
//
//        // Check in the negative direction
//        r = row - dRow;
//        c = column - dColumn;
//        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS &&
//                tempBoard[r][c] == opponentSymbol && player2Count < 4) {
//            player2Count++;
//            r -= dRow;
//            c -= dColumn;
//        }
//
//
//        // Score the line based on chip counts and apply round-based weight
//        if (player1Count == 4) { // Player 1 wins
//            return 1000 + roundWeight(round);
//        } else if (player2Count == 4) { // Player 2 wins
//            return -1000 - roundWeight(round);
//        } else if (player1Count == 3 && player2Count == 0) { // Potential win for Player 1
//            return 100 + roundWeight(round);
//        } else if (player1Count == 2 && player2Count == 0) { // Favorable for Player 1
//            return 10 + roundWeight(round);
//        } else if (player1Count == 1 && player2Count == 0) { // Slightly favorable for Player 1
//            return 1 + roundWeight(round);
//        } else if (player2Count == 3 && player1Count == 0) { // Potential win for Player 2
//            return -100 - roundWeight(round);
//        } else if (player2Count == 2 && player1Count == 0) { // Favorable for Player 2
//            return -10 - roundWeight(round);
//        } else if (player2Count == 1 && player1Count == 0) { // Slightly favorable for Player 2
//            return -1 - roundWeight(round);
//        }
//
//
//        return 0; // Neutral
//    }






//    private int calculateScore(int row, int column) {
//        int score = 0;
//
//        score += countConsecutiveSymbols(row, column, 0, 1); // Horizontal
//        score += countConsecutiveSymbols(row, column, 1, 0); // Vertical
//        score += countConsecutiveSymbols(row, column, 1, 1); // (\)
//        score += countConsecutiveSymbols(row, column, 1, -1); // (/)
//
//        return score;
//    }

//    private int countConsecutiveSymbols ( char[][] tempBoard,int row, int column, int dRow, int dColumn){
//        playerSymbol = playerSymbols[currentPlayer];
//        int count = 0;
//
//        for (int i = -3; i <= 3; i++) {
//            int r = row + i * dRow;
//            int c = column + i * dColumn;
//
//            if (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && tempBoard[r][c] == playerSymbol) {
//                count++;
//            } else {
//                break;
//            }
//        }
//        return count;
//    }



    // Check Win Methods

    private boolean checkHorizontalWin(int row) {
        for (int column = 0; column < COLUMNS - 3; column++) {
            if (board[row][column] == playerSymbols[currentPlayer] &&
                    board[row][column + 1] == playerSymbols[currentPlayer] &&
                    board[row][column + 2] == playerSymbols[currentPlayer] &&
                    board[row][column + 3] == playerSymbols[currentPlayer]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalWin(int column) {
        for (int row = 0; row < ROWS - 3; row++) {
            if (board[row][column] == playerSymbols[currentPlayer] &&
                    board[row + 1][column] == playerSymbols[currentPlayer] &&
                    board[row + 2][column] == playerSymbols[currentPlayer] &&
                    board[row + 3][column] == playerSymbols[currentPlayer]) {
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
        while (row - i >= 0 && column + i < COLUMNS && board[row - i][column + i] == playerSymbol) {
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




