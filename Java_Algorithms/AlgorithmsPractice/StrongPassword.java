
/* Prompt: Louise joined a social networking site to stay in touch with her friends. The signup page required her to input a name and a password.
 However, the password must be strong. The website considers a password to be strong if it satisfies the following criteria:

Its length is at least .
It contains at least one digit.
It contains at least one lowercase English character.
It contains at least one uppercase English character.
It contains at least one special character. The special characters are: !@#$%^&*()-+
She typed a random string of length  in the password field but wasn't sure if it was strong. Given the string she typed, can you find the minimum
number of characters she must add to make her password strong?
*
 */

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class StrongPassword {

        // Complete the minimumNumber function below.
        static int minimumNumber(int n, String password) {
            String numbers = "0123456789",
                    lower_case = "abcdefghijklmnopqrstuvwxyz",
                    upper_case = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    special_characters = "!@#$%^&*()-+";
            int need = 0;
            // Return the minimum number of characters to make the password strong
            boolean hasNumber = checkNumber(password,numbers);
            if (!hasNumber){
                need++;
            }
            boolean hasLowerCase = checkLowerCase(password, lower_case);
            if(!hasLowerCase){
                need++;
            }
            boolean hasUpperCase = checkUpperCase(password, upper_case);
            if(!hasUpperCase){
                need++;
            }
            boolean hasSpecChar = checkSpecChar(password, special_characters);
            if(!hasSpecChar){
                need++;
            }

            if (n < 6){
                if ((n+need) < 6){
                    need += (6 - (n+need));
                }
            }

            return need;


        }

        public static boolean checkNumber(String password, String numbers){
            int index = -1;
            boolean res = false;
            for(int i = 0; i < numbers.length(); i++){
                index = password.indexOf(numbers.charAt(i));
                if(index != -1){
                    res = true;
                    break;
                }
            }
            return res;

        }

        public static boolean checkLowerCase(String password, String lower_case){
            int index = -1;
            boolean res = false;
            for(int i = 0; i < lower_case.length(); i++){
                index = password.indexOf(lower_case.charAt(i));
                if(index != -1){
                    res = true;
                    break;
                }
            }
            return res;

        }

        public static boolean checkUpperCase(String password, String upper_case){
            int index = -1;
            boolean res = false;
            for(int i = 0; i < upper_case.length(); i++){
                index = password.indexOf(upper_case.charAt(i));
                if(index != -1){
                    res = true;
                }
            }
            return res;

        }

        public static boolean checkSpecChar(String password, String special_characters){
            int index = -1;
            boolean res = false;
            for(int i = 0; i < special_characters.length(); i++){
                index = password.indexOf(special_characters.charAt(i));
                if(index != -1){
                    res = true;
                }
            }
            return res;

        }

        private static final Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) throws IOException {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

            int n = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            String password = scanner.nextLine();

            int answer = minimumNumber(n, password);

            System.out.println("Output: " + answer);

            bufferedWriter.write(String.valueOf(answer));
            bufferedWriter.newLine();

            bufferedWriter.close();

            scanner.close();
        }



}
