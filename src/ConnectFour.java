
// Adam Simcoe - 101442161
// Nhan Tran -
// Nhu Ly - 101429112
// Trang Nguyen - 100749684

import java.util.*;


public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY = '-';
    private final String[] playerNames = new String[2];
    private final char[] playerSymbols = new char[2];
    private static final char[] SYMBOLS = {'R', 'Y'};

    private final char[][] board;
    private int currentPlayer;
    private boolean isSinglePlayer;

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
                    char playerSymbol = playerSymbols[currentPlayer];
                    column = aiMove(board, playerSymbol);
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

            if (checkWin(board, row, column, playerSymbols[currentPlayer])) {
                String winningMsg =
                                "\n-----  Yahooo  -----\n" +
                                "-`-`-`  \\O/  `-`-`-`\n" +
                                "`-`-`-   |   -`-`-`-\n" +
                                "-`-`-`  / \\  `-`-`-`";
                System.out.println(winningMsg);
                System.out.println("----  " + playerNames[currentPlayer] + " wins!  ----");
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



    //////-----AI.V2--------///////
    private List<Integer> getValidLocations(char[][] tempBoard){
        List<Integer> validLocations = new ArrayList<>();
        for( int col = 0; col < COLUMNS; col++){
            if(isValidLocation(tempBoard, col))
                validLocations.add(col);
        }
        return validLocations;
    }
    private boolean isValidLocation(char[][] tempBoard, int col){
        return tempBoard[0][col] == EMPTY;
    }

    //@Instead of placing a piece anywhere in the column,
    // getting only the next open row is restricted to the next available slot from the bottom of the board
    private int getNextOpenRow(char[][] tempBoard, int col){
        for (int row= ROWS-1; row >=0; row--){
            if(tempBoard[row][col] == EMPTY){
                return row;
            }
        }
        return -1;
    }
    //@Drop a piece in the next available row, in the chosen column
    private void dropSymbolTemp(char[][] tempBoard, int row, int col, char playerSymbol) {
        if(isValidLocation(tempBoard,col)) {
            tempBoard[row][col] = playerSymbol;
        }
    }
    //@Analyze: scan all possible 4-piece sections of the board,
    // and evaluate (score) each state based on its contents
    private int scorePosition(char[][] tempBoard){
        int score =0;
        char piece = playerSymbols[currentPlayer];
        char[] centerArray = new char[ROWS];
        int SCORE_LENGTH =4;
        int centerCount;
        for (int i = 0; i < ROWS; i++) {
            centerArray[i] = tempBoard[i][COLUMNS / 2];
        }
        // Count occurrences of the piece in the center column
        centerCount = (int) new String(centerArray)
                .chars()  // Convert String to IntStream of characters
                .mapToObj(c -> (char) c)  // Convert each int back to a Character object
                .filter(p -> p == piece)  // Filter to count occurrences of 'piece'
                .count();
        score += centerCount * 3;

        // Score Horizontal positions
        for (int r = ROWS-1; r >= 0; r--) {
            char[] rowArray = tempBoard[r];

            // Iterate over each possible horizontal alignment of 4
            // specifies the number of elements to copy, creating a "window" of elements from rowArray
            for (int c = 0; c <= COLUMNS-3; c++) {
                char[] window = Arrays.copyOfRange(rowArray, c, c + SCORE_LENGTH);
                score += evaluateWindow(window, piece);
            }
        }

        //Score Vertical positions
        for (int c = 0; c< COLUMNS; c++){
            char[] colArray = new char[COLUMNS];
            //populate each col
            for(int r= ROWS-1; r>=0; r-- ){
                colArray[r] = tempBoard[r][c];
            }
            //each possible vertical alignments
            for (int r =0; r <= ROWS - SCORE_LENGTH; r++) {
                char[] window = Arrays.copyOfRange(colArray, r, r + SCORE_LENGTH);
                score += evaluateWindow(window, piece);
            }
        }
        //Score Diagonal (/)
        for (int r =0; r <= ROWS - SCORE_LENGTH; r++) {
            for (int c = 0; c <= COLUMNS - SCORE_LENGTH; c++) {
                char[] window = new char[SCORE_LENGTH];
                for (int i = 0; i < SCORE_LENGTH; i++) {
                    window[i] = tempBoard[r + i][c + i];
                }
                score += evaluateWindow(window, piece);
            }
        }

        // Score diagonals (\)
        for (int r =0; r <= ROWS - SCORE_LENGTH; r++) {
            for (int c = 0; c <= COLUMNS - SCORE_LENGTH; c++) {
                char[] window = new char[SCORE_LENGTH];
                for (int i = 0; i < SCORE_LENGTH; i++) {
                    window[i] = board[r + SCORE_LENGTH - 1 - i][c + i];
                }
                score += evaluateWindow(window, piece);
            }
        }

        return score;
    }

    //@evaluate each scenario
    private int evaluateWindow(char[] window, char piece) {
        int score = 0;
        char opponentPiece = (piece == playerSymbols[currentPlayer]) ? playerSymbols[(currentPlayer+1)%2] : playerSymbols[currentPlayer];

        // Check for winning move (4 in a row)
        if (countOccurrences(window, piece) == 4) {
            score += 100;
        }
        // Check for connecting 3 with 1 empty space
        else if (countOccurrences(window, piece) == 3 && countOccurrences(window, 0) == 1) {
            score += 5;
        }
        // Check for connecting 2 with 2 empty spaces
        else if (countOccurrences(window, piece) == 2 && countOccurrences(window, 0) == 2) {
            score += 2;
        }
        // Check for opponent's potential winning move and prioritize blocking it
        else if (countOccurrences(window, opponentPiece) == 3 && countOccurrences(window, 0) == 1) {
            score -= 4;
        }

        return score;
    }

    private int countOccurrences(char[] array, int value) {
        int count = 0;
        for (int element : array) {
            if (element == value) {
                count++;
            }
        }
        return count;
    }

    private boolean isWinningMove(char[][] tempBoard, int row, int col, char piece){
        return checkWin(tempBoard, row, col, piece) ;
    }

    private boolean isTerminalNode(char[][] board, int row, int col) {
        char piece = playerSymbols[currentPlayer];
        return isWinningMove(board, row, col, piece) || getValidLocations(board).isEmpty();
    }

    //@minimax
    private int[] minimax(char[][] tempBoard, int depth, int alpha, int beta, boolean maximizingPlayer) {
        List<Integer> validLocations = getValidLocations(tempBoard);
        int column = 0;
        int row = 0;
        char piece = playerSymbols[currentPlayer];
        char opponentPiece = (piece == playerSymbols[currentPlayer]) ? playerSymbols[(currentPlayer+1)%2] : playerSymbols[currentPlayer];
        boolean isTerminal = isTerminalNode(tempBoard, row, column);

        if (depth == 0 || isTerminal) {
            if (isTerminal) {
                //Weight the bot winning really high
                if (isWinningMove(tempBoard, row, column, piece) ) {
                    return new int[]{column, Integer.MIN_VALUE};  // Bot wins
                //Weight the human winning really low
                } else if (isWinningMove(tempBoard, row, column, opponentPiece)) {
                    return new int[]{column, Integer.MIN_VALUE}; // Human wins
                } else {
                    return new int[]{column, 0};         // No more valid moves
                }
            } else {
                //Return the bot's score
                return new int[]{column, scorePosition(board)};
            }
        }

        if (maximizingPlayer) {
            int value = Integer.MIN_VALUE;
            column = validLocations.get(new Random().nextInt(validLocations.size()));
            for (int col : validLocations) {
                row = getNextOpenRow(tempBoard, col);
                //@Copy current state of the board
                for (int i = 0; i < ROWS; i++) {
                    tempBoard[i] = Arrays.copyOf(tempBoard[i], COLUMNS);
                }
                if(row != -1){
                    dropSymbolTemp(tempBoard, row, col, piece);
                    int newScore = minimax(tempBoard, depth - 1, alpha, beta, false)[1];
                    if (newScore > value) {
                        value = newScore;
                        column = col; //bestColumn
                    }
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            return new int[]{column, value};
        } else {
            int value = Integer.MAX_VALUE;
            column = validLocations.get(new Random().nextInt(validLocations.size()));
            for (int col : validLocations) {
                row = getNextOpenRow(tempBoard, col);

                //@Copy current state of the board
                for (int i = 0; i < ROWS; i++) {
                    tempBoard[i] = Arrays.copyOf(tempBoard[i], COLUMNS);
                }
                if(row != -1) {
                    dropSymbolTemp(tempBoard, row, col, opponentPiece);

                    int newScore = minimax(tempBoard, depth - 1, alpha, beta, true)[1];
                    if (newScore < value) {
                        value = newScore;
                        column = col;
                    }
                    beta = Math.min(beta, value);
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            return new int[]{column, value};
        }
    }



    private int aiMove(char[][] board, char playerSymbol) {


        int bestScore = Integer.MIN_VALUE;
        char[][] tempBoard = new char[ROWS][COLUMNS];

        //@Copy current state of the board
        for (int i = 0; i < ROWS; i++) {
            tempBoard[i] = Arrays.copyOf(board[i], COLUMNS);
        }
        List<Integer> validLocations = getValidLocations(tempBoard);
        int bestColumn = validLocations.get(new Random().nextInt(validLocations.size()));
        int bestRow = 0;

        for (int col : validLocations) {
            int row = getNextOpenRow(tempBoard, col);
            if(row!=-1) {
                dropSymbolTemp(tempBoard, row, col, playerSymbol);
            }
            int[] score = minimax(tempBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            // Update the best move if the current score is better
            if (score[1] > bestScore) {
                bestScore = score[1];
                bestColumn = score[0];
                bestRow = row;
            }


        }

        System.out.println("\nFINAL BEST SCORE: " + bestScore + " at row: " + bestRow + " ,column: " + bestColumn);
        return bestColumn;

    }



    // Check Win Methods

    private boolean checkHorizontalWin(char[][] board, int row, char playerSymbol) {
        for (int column = 0; column < COLUMNS - 3; column++) {
            if (board[row][column] == playerSymbol &&
                    board[row][column + 1] == playerSymbol &&
                    board[row][column + 2] == playerSymbol &&
                    board[row][column + 3] == playerSymbol ){
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalWin(char[][] board, int column, char playerSymbol) {
        for (int row = 0; row < ROWS - 3; row++) {
            if (board[row][column] == playerSymbol&&
                    board[row + 1][column] == playerSymbol &&
                    board[row + 2][column] == playerSymbol &&
                    board[row + 3][column] == playerSymbol) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalWin(char[][] board,int row, int column, char playerSymbol) {
        //char playerSymbol = playerSymbols[currentPlayer];

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

    private boolean checkWin(char[][] board, int row, int column, char playerSymbol) {
        return checkHorizontalWin(board, row, playerSymbol) || checkVerticalWin(board, column, playerSymbol) || checkDiagonalWin(board, row, column, playerSymbol);
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




