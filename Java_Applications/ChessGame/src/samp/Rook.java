package samp;

import java.io.IOException;
import java.util.Objects;

/**
 */
public class Rook extends Piece {
    public String color;
    public boolean ePos;
    public String name;
    public  boolean moved;
    public int ID; 

    public Rook(String color, String name, int ID){
        this.color = color;
        this.name = name;
        this.ID = ID; 
        ePos = false;
        moved = false;
    }
    
    public Rook(){
    	
    }
    
    public int getID(){
    	return ID;
    }

    public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException {
        String dir = direction(row1,col1,row2,col2);
        String cl = br.board[row1][col1].getColor(); 
        if(dir.equals("invalid")){
            return "No";
        }
        else if(dir.equals("north")){
            for(int i = row1-1;i>row2;i--){
                if(!(br.board[i][col2].getClass().isInstance(new Empty()))){
                	System.out.print(br.board[i][col2].getClass());
                    return "No";
                }
            }
        }
        else if(dir.equals("south")){
            for(int i = row1+1;i<row2;i++){
                if(!(br.board[i][col2].getClass().isInstance(new Empty()))){
                    return "No";
                }
            }
        }
        else if(dir.equals("east")){
            for(int i = col1+1;i<col2;i++){
                if(!(br.board[row2][i].getClass().isInstance(new Empty()))){
                    return "No";
                }
            }
        }
        else if(dir.equals("west")){
            for(int i = col1-1;i>col2;i--){
                if(!(br.board[row2][i].getClass().isInstance(new Empty()))){
                    return "No";
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

    public String direction(int row1,int col1, int row2, int col2){
        if(col1-col2 !=0 && row1-row2!=0){
            return "invalid";
        }
        if(col1-col2!=0){
            if(col1-col2 <0){
                return "east";
            }
            else if(col1-col2>0){
                return "west";
            }
        }
        else if(row1-row2 != 0){
            if(row1-row2 >0){
                return "north";
            }
            else if(row1-row2<0){
                return "south";
            }
        }
        return "invalid";
    }
    public String getColor(){
        return color;
    }

    public String getName(){
        return name;
    }
}