package samp;

import java.io.IOException;

/**
 */
public class Knight extends Piece {
    public String color;
    public boolean ePos;
    public String name;
    public  boolean moved;
    public int ID; 

    public Knight(String color, String name, int ID){
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
    
    public Knight(){
    	
    }
    
    public int getID(){
    	return ID;
    }
    
    public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException{
       boolean properMove = properMove(row1, col1, row2, col2); 
       String cl = br.board[row1][col1].getColor(); 
       if (properMove == false){
    	   return "No"; 
       }
       else {
    	   if (br.board[row2][col2].getClass().isInstance(new Empty())){
    		   return "freeMove";
    	   }
    	   else if (br.board[row2][col2].getColor().equals(cl)){
    		  return "No";  
    	   }
       }
       return "kill"; 
    }
    
    public boolean properMove(int row1, int col1, int row2, int col2){
    	if (row2 == row1 - 2 && col2 == col1 -1){
    		return true;
    	}
    	else if (row2 == row1 - 1 && col2 == col1 - 2){
    		return true;
    	}
    	else if (row2 == row1 - 2 && col2 == col1 + 1){
    		return true;
        }
    	else if (row2 == row1 - 1 && col2 == col1 + 2){
        	return true;
        }
    	else if (row2 == row1 + 1 && col2 == col1 + 2){
        	return true;
        }
    	else if (row2 == row1 + 2 && col2 == col1 + 1){
        	return true;
        }
    	else if (row2 == row1 + 2 && col2 == col1 - 1){
        	return true;
        }
    	else if (row2 == row1 + 1 && col2 == col1 - 2){
        	return true;
        }
    	return false; 
    }
}
