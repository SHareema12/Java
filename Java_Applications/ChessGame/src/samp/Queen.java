package samp;

import java.io.IOException;

/**
 */
public class Queen extends Piece {

    public String color;
    public boolean ePos;
    public String name;
    public  boolean moved;
    public int ID;

    public Queen(String color, String name, int ID){
        this.color = color;
        this.name = name;
        this.ID = ID;
        ePos = false;
        moved = false;
    }

    public String getName(){
        return name;
    }
    
    public String getColor(){
    	return color;
    }
    
    public Queen(){
    	
    }
    
    public int getID(){
    	return ID;
    }
    
    public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException{
    	Rook rook = new Rook(); 
    	Bishop bishop = new Bishop(); 
    	if (rook.isValid(row1, col1, row2, col2, br).equals("No") && bishop.isValid(row1, col1, row2, col2, br).equals("No")){
    		return "No"; 
    	}
    	else if (rook.isValid(row1, col1, row2, col2, br).equals("freeMove") || bishop.isValid(row1, col1, row2, col2, br).equals("freeMove")){
    		return "freeMove"; 
    	}
    	else if (rook.isValid(row1, col1, row2, col2, br).equals("kill") || bishop.isValid(row1, col1, row2, col2, br).equals("kill")){
    		return "kill";
    	}
    	return "No"; 
    }
}
