package samp;

import java.io.IOException;

/**
 */
public class Piece{

    public String color;
    public boolean ePos;
    public String name;
    public boolean moved;
    public int ID; 

    public String isValid(int row1, int col1, int row2, int col2, Board br) throws IOException{return "";}
    public String getName(){
        return name;
    }
    public String getColor(){return color;}
    
    public void setName(String name){
    	this.name = name; 
    }
    
    public int getID(){
    	return ID;
    }
}