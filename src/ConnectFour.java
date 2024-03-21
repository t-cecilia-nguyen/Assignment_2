// Nhan Tran -
// Nhu Ly -
// Trang Nguyen - 100749684


import java.util.Scanner;

public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY = '-';
    private static final char[] PLAYERS = {'R', 'Y'};

    private char[][] board;
    private int currentPlayer;
    private boolean isSinglePlayer;

    public ConnectFour(){
        // Create new board
        board = new char[ROWS][COLUMNS];

        // Fill each char in board with EMPTY ('-')
        for (char[] row: board) {
            java.util.Arrays.fill(row, EMPTY);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of players (1 or 2): ");
        isSinglePlayer = scanner.nextInt() == 1;
    }
}
