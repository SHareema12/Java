package samp;

import java.io.IOException;

/**
 */
public class Bishop extends Piece {

	public String color;
	public boolean ePos;
	public String name;
	public boolean moved;
	public int ID;

	public Bishop(String color, String name, int ID) {
		this.color = color;
		this.name = name;
		this.ID = ID;
		ePos = false;
		moved = false;
	}

	public String getName() {
		return name;
	}
	
	public int getID(){
		return ID;
	}
	
	public Bishop(){
		
	}
	
	public String getColor(){
		return color;
	}

	public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException{
		String dir = direction(row1, col1, row2, col2);
		String cl = br.board[row1][col1].getColor(); 
		int i, j;
		if (dir.equals("invalid")) {
			return "No";
		} else if (dir.equals("ne")) {
			for (i = row1 - 1; i > row2; i--) {
				for (j = col1 + 1; j < col2; j++) {
					if (!(br.board[i][j].getClass().isInstance(new Empty()))) {
						return "No";
					}
					i--;
				}
			}
		}
		else if (dir.equals("nw")){
			for (i = row1 - 1; i > row2; i--) {
				for (j = col1 - 1; j > col2; j--) {
					if (!(br.board[i][j].getClass().isInstance(new Empty()))) {
						return "No";
					}
					i--;
				}
			}
		}
		else if (dir.equals("se")){
			for (i = row1 + 1; i < row2; i++) {
				for (j = col1 + 1; j < col2; j++) {
					if (!(br.board[i][j].getClass().isInstance(new Empty()))) {
						return "No";
					}
					i++;
				}
			}
		}
		else if (dir.equals("sw")){
			for (i = row1 + 1; i < row2; i++) {
				for (j = col1 - 1; j > col2; j--) {
					if (!(br.board[i][j].getClass().isInstance(new Empty()))) {
						return "No";
					}
					i++;
				}
			}
		}		
		if (br.board[row2][col2].getClass().isInstance(new Empty())){
        	return "freeMove"; 
        }
        else if (br.board[row2][col2].getColor().equals(cl)){
        	return "No"; 
        }
        return "kill";
	}

	public String direction(int row1, int col1, int row2, int col2) {
		double dY = row2 - row1;
		double dX = col2 - col1;
		double slope = dY / dX;
		if (slope != 1.0 && slope != -1.0) {
			return "invalid";
		} else if (dY > 0 && dX < 0) {
			return "sw";
		} else if (dY < 0 && dX < 0) {
			return "nw";
		} else if (dY > 0 && dX > 0) {
			return "se";
		} else if (dY < 0 && dX > 0) {
			return "ne";
		}
		return "invalid";
	}
}
