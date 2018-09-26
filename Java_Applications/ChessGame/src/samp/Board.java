package samp;

/**
 */
public class Board {
	Piece[][] board = new Piece[9][9];
	String[] Black = new String[16];
	String[] White = new String[16];

	public Board() {
		String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8" };
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h" };
		for (int i = 0; i < 8; i++) {
			board[6][i] = new Pawn("White", "wp", i);
			White[i] = String.valueOf(6) + String.valueOf(i);
		}
		board[7][0] = new Rook("White", "wR", 8);
		White[8] = String.valueOf(7) + String.valueOf(0);
		board[7][7] = new Rook("White", "wR", 9);
		White[9] = String.valueOf(7) + String.valueOf(7);
		board[7][1] = new Knight("White", "wN", 10);
		White[10] = String.valueOf(7) + String.valueOf(1);
		board[7][6] = new Knight("White", "wN", 11);
		White[11] = String.valueOf(7) + String.valueOf(6);
		board[7][2] = new Bishop("White", "wB", 12);
		White[12] = String.valueOf(7) + String.valueOf(2);
		board[7][5] = new Bishop("White", "wB", 13);
		White[13] = String.valueOf(7) + String.valueOf(5);
		board[7][3] = new Queen("White", "wQ", 14);
		White[14] = String.valueOf(7) + String.valueOf(6);
		board[7][4] = new King("White", "wK", 15);
		White[15] = String.valueOf(7) + String.valueOf(4);

		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn("Black", "bp", i);
			Black[i] = String.valueOf(1) + String.valueOf(i);
		}
		board[0][0] = new Rook("Black", "bR", 8);
		Black[8] = String.valueOf(0) + String.valueOf(0);
		board[0][7] = new Rook("Black", "bR", 9);
		Black[9] = String.valueOf(0) + String.valueOf(7);
		board[0][1] = new Knight("Black", "bN", 10);
		Black[10] = String.valueOf(0) + String.valueOf(1);
		board[0][6] = new Knight("Black", "bN", 11);
		Black[11] = String.valueOf(0) + String.valueOf(6);
		board[0][2] = new Bishop("Black", "bB", 12);
		Black[12] = String.valueOf(0) + String.valueOf(2);
		board[0][5] = new Bishop("Black", "bB", 13);
		Black[13] = String.valueOf(0) + String.valueOf(5);
		board[0][3] = new Queen("Black", "bQ", 14);
		Black[14] = String.valueOf(0) + String.valueOf(3);
		board[0][4] = new King("Black", "bK", 15);
		Black[15] = String.valueOf(0) + String.valueOf(4);

		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				if (isHash(i, j))
					board[i][j] = new Empty("##");
				else if (isHash(i, j) == false) {
					board[i][j] = new Empty("");
				}
			}
		}
		board[8][8] = new Empty("");
		int x = 7;
		for (int i = 0; i < 8; i++) {
			board[i][8] = new Empty(numbers[x]);
			x--;
		}
		x = 0;
		for (int i = 0; i < 8; i++) {
			board[8][i] = new Empty(letters[x]);
			x++;
		}
	}

	public String toString() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j].getClass().isInstance(new Empty())) {
					if (isHash(i, j))
						board[i][j].setName("##");
					else if (isHash(i, j) == false && isNumOrLetter(board[i][j].getName()) == false) {
						board[i][j].setName("");
					}
					System.out.printf("%-3s", board[i][j].getName());
				} else if (i == 8) {
					System.out.printf("%-3.9s", board[i][j].getName());
				} else
					System.out.print(board[i][j].getName() + " ");
			}
			System.out.println();
		}
		return "";
	}

	public boolean isHash(int row, int col) {
		if (row == 8 || col == 8) {
			return false;
		}
		if (row % 2 == 0) {
			if (col % 2 != 0) {
				return true;
			}
		} else if (row % 2 != 0) {
			if (col % 2 == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isNumOrLetter(String str) {
		if (str.isEmpty()) {
			return false;
		}
		char c = str.charAt(0);
		if (Character.isLetter(c) || Character.isDigit(c)) {
			return true;
		}
		return false;
	}
}
