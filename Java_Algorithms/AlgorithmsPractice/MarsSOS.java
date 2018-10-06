/* Prompt: Complete the marsExploration function in the editor below. It should return an integer representing the number of letters changed during transmission.
*/

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the marsExploration function below.
    static int marsExploration(String s) {
        int index =0, error = 0; 
        String s1 = ""; 
        
        while(index < s.length()){
            s1 = s.substring(index, index + 3);
            if(s1.charAt(0) != 'S') error++;
            if(s1.charAt(1) != 'O') error++;
            if(s1.charAt(2) != 'S') error++;
            index+=3;
        }
        
        return error;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = scanner.nextLine();

        int result = marsExploration(s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
