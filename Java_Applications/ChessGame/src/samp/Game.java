package samp;

import java.io.IOException;

/*
 */
public class Game {

	public Game() {
	}

	public boolean move(Board br, String cr, boolean whiteTurn) throws IOException, NullPointerException {
		int[] cor;
		int row1, col1, row2, col2;
		cor = convert(cr);
		Piece[] tmp = new Piece[1];
		row1 = cor[0];
		col1 = cor[1];
		row2 = cor[2];
		col2 = cor[3];
		//System.out.println(row1 + "," + col1 + "-->" + row2 + "," + col2);
		if (row1 == row2 && col1 == col2) {
			return false;
		} else if (br.board[row1][col1].getClass().isInstance(new Empty())) {
			return false;
		}
		String color = br.board[row1][col1].getColor();
		if ((whiteTurn == true && color.equals("Black")) || (whiteTurn == false && color.equals("White"))) {
			return false;
		}
		String moveDetails = br.board[row1][col1].isValid(row1, col1, row2, col2, br);
		if (moveDetails.equals("No")) {
			return false;
		}
		if (br.board[row1][col1].getClass().isInstance(new Pawn())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
			} else if (moveDetails.equals("setEpos")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
				br.board[row2][col2].ePos = true;
			} else if (moveDetails.equals("Epos")) {
				Epos(br, row1, col1, row2, col2, color, tmp);
			} else if (moveDetails.equals("Kill")) {
				br = Kill(br, row1, col1, row2, col2, tmp, color);
			} else if (moveDetails.equals("KP") || moveDetails.equals("Pro")) {
				br = promotion(br, row1, col1, row2, col2, moveDetails, color, cr);
			}
		} else if (br.board[row1][col1].getClass().isInstance(new Rook())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
				br.board[row2][col2].moved = true;
			} else if (moveDetails.equals("Kill")) {
				br = Kill(br, row1, col1, row2, col2, tmp, color);
				br.board[row2][col2].moved = true;
			}
		} else if (br.board[row1][col1].getClass().isInstance(new Bishop())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
			} else if (moveDetails.equals("Kill")) {
				br.board[row2][col2] = br.board[row1][col1];
				br.board[row1][col1] = new Empty("##");
			}
		} else if (br.board[row1][col1].getClass().isInstance(new Knight())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
			} else if (moveDetails.equals("Kill")) {
				br.board[row2][col2] = br.board[row1][col1];
				br.board[row1][col1] = new Empty("##");
			}
		} else if (br.board[row1][col1].getClass().isInstance(new Queen())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
			} else if (moveDetails.equals("Kill")) {
				br.board[row2][col2] = br.board[row1][col1];
				br.board[row1][col1] = new Empty("##");
			}
		} else if (br.board[row1][col1].getClass().isInstance(new King())) {
			if (moveDetails.equals("FreeMove")) {
				br = freeMove(br, row1, col1, row2, col2, tmp, color);
				br.board[row2][col2].moved = true;
			} else if (moveDetails.equals("Kill")) {
				br = Kill(br, row1, col1, row2, col2, tmp, color);
				br.board[row2][col2].moved = true;
			} else if (moveDetails.equals("lc") || moveDetails.equals("rc")) {
				castling(br, row1, col1, row2, col2, moveDetails, color, tmp);
			}
		}
		return true;
	}

	public Board simulate(Board copy, String crd, boolean turn) {
		// copy = move(copy)
		return copy;
	}

	public String friendlyCheck(Board br, String crd, boolean whiteTurn) throws IOException {
		Board copy = copyBoard(br);
		int cord[] = new int[4];
		int row1, col1, row2, col2;
		cord = convert(crd);
		row1 = cord[0];
		col1 = cord[1];
		row2 = cord[2];
		col2 = cord[3];
		String testCdt = ""; 
		String color = copy.board[row1][col1].getColor();
		boolean result = move(copy, crd, whiteTurn);
		if (result == false) {
			return "invalid"; 
		} else {
			if (color.equals("White")) {
				for (int i = 0; i < copy.Black.length; i++){
					testCdt = convertBack(copy.Black[i]) + " " + convertBack(copy.Black[15]);  
					if (testCdt.isEmpty()){
						continue; 
					}
					else if (move(copy, testCdt, false) == true){
						return "inFriendlyCheck"; 
					}
				}

			}else if (color.equals("Black")) {
				for (int i = 0; i < copy.White.length; i++){
					testCdt = convertBack(copy.White[i]) + " " + convertBack(copy.White[15]); 
					if (testCdt.isEmpty()){
						continue;
					}
					else if (move(copy, testCdt, true) == true){
						return "inFriendlyCheck"; 
					}
				}

			}
		}
		return "notInFriendlyCheck"; 
	}

	public boolean enemyCheck(Board br, int row1, int col1, int row2, int col2) throws IOException {

		return true;
	}

	public Board changePosition(Board br, String color, int id, int row2, int col2) {
		if (color.equals("White")) {
			if (row2 == -1 || col2 == -1) {
				br.White[id] = "";
			} else {
				br.White[id] = String.valueOf(row2) + String.valueOf(col2);
			}
		} else {
			if (row2 == -1 || col2 == -1) {
				br.Black[id] = "";
			} else {
				br.Black[id] = String.valueOf(row2) + String.valueOf(col2);
			}
		}
		return br;
	}

	public Board freeMove(Board br, int row1, int col1, int row2, int col2, Piece[] temp, String color) {
		int id = br.board[row1][col1].getID();
		temp[0] = br.board[row2][col2];
		br.board[row2][col2] = br.board[row1][col1];
		br.board[row1][col1] = temp[0];
		br = changePosition(br, color, id, row2, col2);
		return br;
	}

	public Board promotion(Board br, int row1, int col1, int row2, int col2, String moveDetails, String color,
			String userInput) {
		int pawnID = br.board[row1][col1].getID();
		char colorPiece = color.toLowerCase().charAt(0);
		char promo;
		if (moveDetails.equals("KP")) {
			String killedColor = br.board[row2][col2].getColor();
			int killedID = br.board[row2][col2].getID();
			if (userInput.length() < 7) {
				br = changePosition(br, killedColor, killedID, -1, -1);
				br.board[row2][col2] = new Queen(color, colorPiece + "Q", pawnID);
				br.board[row1][col1] = new Empty("##");
				br = changePosition(br, color, pawnID, row2, col2);
			} else {
				promo = userInput.toUpperCase().charAt(6);
				if (promo == 'N') {
					br.board[row2][col2] = new Knight(color, colorPiece + "N", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'B') {
					br.board[row2][col2] = new Bishop(color, colorPiece + "B", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'R') {
					br.board[row2][col2] = new Rook(color, colorPiece + "R", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'Q') {
					br.board[row2][col2] = new Queen(color, colorPiece + "Q", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				}
			}
		} else if (moveDetails.equals("Pro")) {
			if (userInput.length() < 7) {
				br.board[row2][col2] = new Queen(color, colorPiece + "Q", pawnID);
				br.board[row1][col1] = new Empty("##");
				br = changePosition(br, color, pawnID, row2, col2);
			} else {
				promo = userInput.toUpperCase().charAt(6);
				if (promo == 'N') {
					br.board[row2][col2] = new Knight(color, colorPiece + "N", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'B') {
					br.board[row2][col2] = new Bishop(color, colorPiece + "B", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'R') {
					br.board[row2][col2] = new Rook(color, colorPiece + "R", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				} else if (promo == 'Q') {
					br.board[row2][col2] = new Queen(color, colorPiece + "Q", pawnID);
					br.board[row1][col1] = new Empty("##");
					br = changePosition(br, color, pawnID, row2, col2);
				}
			}
		}
		return br;
	}

	public Board Kill(Board br, int row1, int col1, int row2, int col2, Piece[] temp, String colorKiller) {
		int IDKiller = br.board[row1][col1].getID();
		int IDKilled = br.board[row2][col2].getID();
		String colorKilled = br.board[row2][col2].getColor();
		br.board[row2][col2] = br.board[row1][col1];
		br.board[row1][col1] = new Empty("##");
		br = changePosition(br, colorKiller, IDKiller, row2, col2);
		br = changePosition(br, colorKilled, IDKilled, -1, -1);
		return br;
	}

	public Board Epos(Board br, int row1, int col1, int row2, int col2, String killerColor, Piece[] tmp) {
		int IDKiller = br.board[row1][col1].getID();
		int IDKilled;
		String killedColor;
		if (killerColor.equals("White")) {
			IDKilled = br.board[row2 + 1][col2].getID();
			killedColor = br.board[row2 + 1][col2].getColor();
			tmp[0] = br.board[row2][col2];
			br.board[row2][col2] = br.board[row1][col1];
			br = changePosition(br, killerColor, IDKiller, row2, col2);
			br.board[row1][col1] = tmp[0];
			br.board[row2 + 1][col2] = new Empty("##");
			changePosition(br, killedColor, IDKilled, -1, -1);
		} else if (killerColor.equals("Black")) {
			IDKilled = br.board[row2 - 1][col2].getID();
			killedColor = br.board[row2 - 1][col2].getColor();
			tmp[0] = br.board[row2][col2];
			br.board[row2][col2] = br.board[row1][col1];
			br = changePosition(br, killerColor, IDKiller, row2, col2);
			br.board[row1][col1] = tmp[0];
			br.board[row2 - 1][col2] = new Empty("##");
			changePosition(br, killedColor, IDKilled, -1, -1);
		}
		return br;
	}

	public Board castling(Board br, int row1, int col1, int row2, int col2, String moveDetails, String kingColor,
			Piece[] tmp) {
		int kingID = br.board[row1][col1].getID();
		int rookID;
		if (moveDetails.equals("lc")) {
			rookID = br.board[row2][col2 - 2].getID();
			tmp[0] = br.board[row2][col2];
			br.board[row2][col2] = br.board[row1][col1];
			br.board[row1][col1] = tmp[0];
			br = changePosition(br, kingColor, kingID, row2, col2);
			tmp[0] = br.board[row2][col2 + 1];
			br.board[row2][col2 + 1] = br.board[row2][col2 - 2];
			br.board[row2][col2 - 2] = tmp[0];
			br = changePosition(br, kingColor, rookID, row2, col2 + 1);
			br.board[row2][col2 + 1].moved = true;
		} else if (moveDetails.equals("rc")) {
			rookID = br.board[row2][col2 + 1].getID();
			tmp[0] = br.board[row2][col2];
			br.board[row2][col2] = br.board[row1][col1];
			br.board[row1][col1] = tmp[0];
			br = changePosition(br, kingColor, kingID, row2, col2);
			tmp[0] = br.board[row2][col2 - 1];
			br.board[row2][col2 - 1] = br.board[row2][col2 + 1];
			br.board[row2][col2 + 1] = tmp[0];
			br = changePosition(br, kingColor, rookID, row2, col2 - 1);
			br.board[row2][col2 - 1].moved = true;
		}
		br.board[row2][col2].moved = true;
		return br;
	}

	public String convertBack(String cdt) {
		if (cdt.equals("")) {
			return "";
		}
		String finCdt = "";
		int[] rowArray = { 8, 7, 6, 5, 4, 2, 3, 1 };
		int row = Character.getNumericValue(cdt.charAt(0));
		int corNum = rowArray[row];
		int col = Character.getNumericValue(cdt.charAt(1));
		int i = col + 1;
		char colLetter = (char) (i + 'a' - 1);
		finCdt = "" + (char) (i + 'a' - 1) + corNum;
		return finCdt;
	}

	public int[] convert(String cdt) {
		int[] cor = new int[4];
		int[] rows = { 7, 6, 5, 4, 3, 2, 1, 0 };
		int row = Character.getNumericValue(cdt.charAt(1));
		cor[0] = rows[row - 1];
		switch (cdt.charAt(0)) {
		case 'a':
			cor[1] = 0;
			break;
		case 'b':
			cor[1] = 1;
			break;
		case 'c':
			cor[1] = 2;
			break;
		case 'd':
			cor[1] = 3;
			break;
		case 'e':
			cor[1] = 4;
			break;
		case 'f':
			cor[1] = 5;
			break;
		case 'g':
			cor[1] = 6;
			break;
		case 'h':
			cor[1] = 7;
			break;
		}
		int row2 = Character.getNumericValue(cdt.charAt(4));
		cor[2] = rows[row2 - 1];

		switch (cdt.charAt(3)) {
		case 'a':
			cor[3] = 0;
			break;
		case 'b':
			cor[3] = 1;
			break;
		case 'c':
			cor[3] = 2;
			break;
		case 'd':
			cor[3] = 3;
			break;
		case 'e':
			cor[3] = 4;
			break;
		case 'f':
			cor[3] = 5;
			break;
		case 'g':
			cor[3] = 6;
			break;
		case 'h':
			cor[3] = 7;
			break;
		}
		return cor;
	}

	public Board swap(Board br, int row1, int col1, int row2, int col2) throws IOException {
		Piece[] tmp = new Piece[1];
		tmp[0] = br.board[row2][col2];
		br.board[row2][col2] = br.board[row1][col1];
		br.board[row1][col1] = tmp[0];
		br.board.toString();
		return br;
	}
	
	public Board copyBoard(Board br){
		Board copy = new Board(); 
		int i, j;
		for (i = 0; i < 8; i++){
			for (j = 0; j < 8; j++){
				copy.board[i][j] = br.board[i][j]; 
			}
			
		}
		//coordinates 
		for (i = 0; i <16; i++){
			copy.Black[i] = br.Black[i];
			copy.White[i] = br.White[i]; 
		}
		return copy; 
	}
}