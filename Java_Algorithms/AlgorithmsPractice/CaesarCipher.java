import java.util.HashMap;
import java.util.Map;

public class caesarCipher {
	
	 private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

	    // Complete the caesarCipher function below.
	    static String caesarCipherFun(String s, int k) {
	        String rotated = rotateAlphabet(alphabet, k), res = "";
	        String x = "";
	        boolean isCaps = false;
	        Map<Character,Character> hm = hashAlphabet(alphabet, rotated);
	        for (int i = 0; i < s.length(); i++){
	            char c = s.charAt(i);
	            isCaps = false;
	            if (Character.isUpperCase(c)) {
	                isCaps = true;
	                c = Character.toLowerCase(c);
	            }
	            if(Character.isLetter(c)){
	                x = hm.get(Character.valueOf(c)).toString();
	                if(isCaps) {
	                    x = x.toUpperCase();
	                }
	                res += x;
	            }
	            else {
	                res += s.charAt(i);
	            }
	        }
	        return res; 
	    }
	    
	      static String rotateAlphabet(String a, int k){
	        int count = 0;
	        char c;
	        while (count < k){
	            c = a.charAt(0);
	            a = a.substring(1);
	            a+= c; 
	            count++;
	        }  
	        return a; 
	    }
	    
	    static Map<Character,Character> hashAlphabet(String alphabet, String rotated){
	        Map<Character,Character> hm = new HashMap<>();
	        for (int i = 0; i <rotated.length() ; i++){
	            hm.put(Character.valueOf(alphabet.charAt(i)), Character.valueOf(rotated.charAt(i)));
	        }
	        
	        return hm;
	    }
	
	

	public static void main(String[] args) {
		int k = 2;
		String s = "middle-Outz";
		
		System.out.println(caesarCipherFun(s,k));

	}

}
