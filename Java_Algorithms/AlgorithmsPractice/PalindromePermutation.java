package StringsArrays;

import java.util.HashMap;
import java.util.Map;

/*1.4 Palindrome Permutation: Given a string, write a function to check if it is a permutation of a palindrome.
A palindrome is a word or phrase that is the same forwards and backwards. A permutation
is a rearrangement of letters. The palindrome does not need to be limited to just dictionary words.
EXAMPLE
Input: Tact Coa
Output: True (permutations: "taco cat", "atco eta", etc.)
*
 */

public class PalindromePermutation {


    public PalindromePermutation() {
    }

    public boolean checkPalindromePermutation(String s){
        s = s.toLowerCase();
        s = s.replaceAll("\\s","");
        boolean res;
        Map<Character, Integer> hm = hashString(s);
        res = isPalindromePermutation(hm, s.length());
        return res;
    }

    /*If length of s % 2 = 0, then s is even, that means there should be an even number
    of every letter

    If lenght of s % 2 != 0, then s is odd, that means there can be only one letter that
    is odd
     */
    private boolean isPalindromePermutation(Map<Character, Integer> hm, int length) {
        boolean res = true, isEven = false;
        int oddCount = 0, remainder;
        if (length % 2 == 0){
            isEven = true;
        }
        for (int i: hm.values()){
            remainder = i % 2;
            //odd amount of letters
            if (remainder != 0){
                oddCount++;

                //if s is odd
                if(!isEven){
                    if(oddCount > 1){
                        res = false;
                        break;
                    }
                } else {
                    if(oddCount > 1){
                        res = false;
                        break;
                    }
                }
            }
            //even amount of letters
            else {
                if(!isEven){
                    if(oddCount > 1){
                        res = false;
                        break;
                    }
                }
            }
        }
        return res;
    }

    //Hashes the string into a hashmap
    private Map<Character,Integer> hashString(String s){
        Map<Character, Integer> hm = new HashMap<>();
        char c;
        int x;
        for(int i = 0; i <s.length(); i++){
            c = s.charAt(i);
            if(hm.containsKey(c)){
                x = hm.remove(c);
                x++;
                hm.put(c,x);
            }
            else{
                hm.put(c,1);
            }
        }
        return hm;
    }

    public static void main(String[] args){
        String s = "taco cat";
        boolean res;
        PalindromePermutation pm = new PalindromePermutation();
        res = pm.checkPalindromePermutation(s);
        System.out.println(res);
    }
}
