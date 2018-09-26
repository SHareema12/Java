package samp;

import java.io.IOException;

/**
 */
public class King extends Piece {
	public String color;
	public boolean ePos;
	public String name;
	public boolean moved;
	public int ID; 

	public King(String color, String name, int ID) {
		this.color = color;
		this.name = name;
		this.ID = ID; 
		ePos = false;
		moved = false;
	}

	public King() {
		
	}
	
	public int getID(){
		return ID;
	}
	public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException {
		String cl = br.board[row1][col1].getColor();
		// moving more than one spot down or up
		if (row1 - row2 > 1 || row2 - row1 > 1) {
			return "No";
		}
		// moving more than two spots to the left or to the right
		else if (col1 - col2 > 2 || col2 - col1 > 2) {
			return "No";
		}
		// moving two spots to the left or to the right diagonally
		else if ((col1 - col2 == 2 && row1 != row2) || (col2 - col1 == 2 && row1 != row2)) {
			return "No";
		}
		//// moving two spots to the left or right
		else if (col1 - col2 == 2) {
			// king has already moved
			if (br.board[row1][col1].moved == true) {
				return "No";
			}
			// moving towards the left and pieces in the way?
			else if ((!br.board[row1][col1 - 1].getClass().isInstance(new Empty()))
					|| (!br.board[row1][col1 - 2].getClass().isInstance(new Empty()))
					|| (!br.board[row1][col1 - 3].getClass().isInstance(new Empty()))) {
				return "No";
			}

			// moving to the left and path is clear... is there a rook for
			// castling?
			else if (br.board[row1][col1 - 4].getClass().isInstance(new Rook())) {
				// has the rook already moved??
				if (br.board[row1][col1 - 4].moved == true) {
					return "No";
				}
				// valid left castling
				return "lc";
			}
			return "No";
		}
		// king is moving to the right
		else if (col2 - col1 == 2) {
			// king has already moved
			if (br.board[row1][col1].moved == true) {
				return "No";
			}
			// moving towards the right and pieces in the way?
			else if ((!br.board[row1][col1 + 1].getClass().isInstance(new Empty()))
					|| (!br.board[row1][col1 + 2].getClass().isInstance(new Empty()))) {
				return "No";
			} else if (br.board[row1][col1 + 3].getClass().isInstance(new Rook())) {
				// has the rook already moved??
				if (br.board[row1][col1 + 3].moved == true) {
					return "No";
				}
				// valid right castling
				return "rc";
			}
			return "No";
		}
		// Spot is within 1 and is empty
		else if (br.board[row2][col2].getClass().isInstance(new Empty())) {
			return "FreeMove";
		}
		// Spot is is within 1 and is a friendly piece
		else if (!br.board[row2][col2].getClass().isInstance(new Empty())
				&& br.board[row2][col2].getColor().equals(cl)) {
			return "No";
		}
		// Enemy piece for killing
		return "Kill";
	}

	public boolean properMove(int row1, int col1, int row2, int col2) {

		return false;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
}