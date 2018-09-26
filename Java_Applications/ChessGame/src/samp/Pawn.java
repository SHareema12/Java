package samp;

import java.io.IOException;

/**
 */
public class Pawn extends Piece {
	public String color;
	public boolean ePos;
	public String name;
	public int ID;

	public Pawn(String color, String name, int ID) {
		this.color = color;
		this.name = name;
		this.ID = ID; 
		ePos = false;
	}

	public Pawn() {

	}
	
	public int getID(){
		return ID;
	}

	// checks if player move for pawn is valid, if not then returns false,
	// otherwise returns true
	public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException {
		// "," + col2);
		if (br.board[row1][col1].getColor().equals("White")) {
			// going backwards?
			if (row2 >= row1) {
				return "No";
			}
			// moving more than one spot to the left or right?
			else if (col1 - col2 > 1 || col2 - col1 > 1) {
				return "No";
			}
			// made atleast one move and moving two spots forward?
			else if (row1 < 6 && row1 - row2 > 1) {
				return "No";
			}
			// has not moved and moving more than two spots forward in one turn?
			else if (row1 == 6 && row1 - row2 > 2) {
				return "No";
			}
			// moving two spots as first move, and not moving straight
			else if (row1 == 6 && (row1 - row2 >= 2 && col1 != col2)) {
				return "No";
			}
			String cl = br.board[row2][col2].getColor();
			// desired spot not empty
			if (!(br.board[row2][col2].getClass().isInstance(new Empty()))) {
				// if moving forward and friendly piece in the way
				if ((cl.equals("White") && col2 == col1) || (cl.equals("Black") && col2 == col1)) {
					return "No";
				} else if (cl.equals("White") && col2 != col1) {
					return "No";
				} else if (cl.equals("Black") && col2 != col1 && row2 == 0) {
					return "KP";
				}
				// black piece there for killing
				return "Kill";
			}
			// moving onto an empty spot
			else if (br.board[row2][col2].getClass().isInstance(new Empty())) {
				if (col2 == col1) {
					// promotion
					if (row2 == 8) {
						return "Pro";
					}
					// moving two spots but piece in the way
					else if (row1 == 6 && row1 - row2 == 2
							&& !br.board[row2 + 1][col2].getClass().isInstance(new Empty())) {
						return "No";
					}
					else if (row2 == 4 && row1 ==2){
						return "setEpos"; 
					}
					return "freeMove";
				} else if (row1 == 3 && br.board[row2 + 1][col2].ePos == true) {
					return "Epos";
				}
				return "No";
			}
		} else if (br.board[row1][col1].getColor().equals("Black")) {
			// going backwards?
			if (row2 <= row1) {
				return "No";
			}
			// moving more than one spot to the left or right?
			else if (col2 - col1 > 1 || col1 - col2 > 1) {
				return "No";
			}
			// made atleast one move and moving two spots forward?
			else if (row1 > 1 && row2 - row1 > 1) {
				return "No";
			}
			// has not moved and moving more than two spots forward in one turn?
			else if (row1 == 1 && row2 - row1 > 2) {
				return "No";
			}

			// moving two spots as first move, and not moving straight
			else if (row1 == 1 && (row2 - row1 >= 2 && col1 != col2)) {
				return "No";
			}
			String cl = br.board[row2][col2].getColor();
			// desired spot not empty
			if (!(br.board[row2][col2].getClass().isInstance(new Empty()))) {
				if ((cl.equals("White") && col2 == col1) || (cl.equals("Black") && col2 == col1)) {
					return "No";
				} else if (cl.equals("Black") && col2 != col1) {
					return "No";
				} else if (cl.equals("White") && col2 != col1 && row2 == 7) {
					return "KP";
				}

				// white piece there for killing
				return "Kill";
			}
			// moving onto an empty spot
			else if (br.board[row2][col2].getClass().isInstance(new Empty())) {
				if (col2 == col1) {
					// promotion
					if (row2 == 7) {
						return "Pro";
					} else if (row1 == 1 && row2-row1 == 2 && !br.board[row2 - 1][col2].getClass().isInstance(new Empty())) {
						return "No";
					}
					else if (row2 == 3 && row1 ==1){
						return "setEpos"; 
					}
					return "freeMove";
				} else if (row1 == 4 && br.board[row2 - 1][col2].ePos == true) {
					return "Epos";
				}
				return "No";
			}
		}

		return "freeMove";
	}

	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}