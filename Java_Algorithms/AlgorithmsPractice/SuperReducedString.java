/*
    Steve has a string of lowercase characters in range ascii[‘a’..’z’]. He wants to reduce the string to its shortest length by doing a series of operations. In each operation he selects a pair of adjacent lowercase letters that match, and he deletes them. For instance, the string aab could be shortened to b in one operation.

Steve’s task is to delete as many characters as possible using this method and print the resulting string. If the final string is empty, print Empty String
*/


import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the super_reduced_string function below.
    static String super_reduced_string(String s) {
        int index = 1;
        char curr = 'x';
        String first = "", sec = ""; 
        if (s.length() == 1){
            return s; 
        }
        
        while (index< s.length()){
            if (s.isEmpty() || s.length() == 1){
                break;
            }
            if(s.length() == 2){
                if (s.charAt(index) == s.charAt(index - 1)){
                    s = "Empty String";
                    break;
                }
            }
            curr = s.charAt(index); 
            if (curr == s.charAt(index-1)){
                if (index == 1){
                    s = s.substring(2); 
                    index = 1;
                }
                else{
                    first = s.substring(0, index-1); 
                    sec = s.substring(index+1);
                    s = first + sec; 
                    index = 1;
                }
            }
            else {
                index ++;
            }
            
        }
        return s;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = scanner.nextLine();

        String result = super_reduced_string(s);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
