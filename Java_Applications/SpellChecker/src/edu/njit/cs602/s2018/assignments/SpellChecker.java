package edu.njit.cs602.s2018.assignments;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.*;
import java.lang.*;

///Program written and submitted by Safia Hareema///

public class SpellChecker {

    public static final String PUNCTUATION_MARKS = ".;?";
    public static final int IGNORE_RULE_OPTION = 1;
    public static final int VALID_WORD_OPTION = 2;
    public static final int CORRECT_WORD_OPTION = 3;
    public static final int IGNORE_WORD_OPTION = 4;

    private class Dictionary {
        private final String dictionaryFile;
        private final Set<String> wordList = new HashSet<>();

        /**
         * Add word to dictionary
         * @param word
         */
        public void addWord(String word) {
            wordList.add(word);
        }

        /**
         * Is it a valid word ?
         * @param word
         * @return
         */
        public boolean isValid(String word) {
            return wordList.contains(word);
        }

        /**
         * Update the dictionary file
         */
        public void update() throws IOException {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFile));
                for (String word : wordList) {
                    writer.write(word);
                    writer.newLine();
                }
                writer.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        

        /**
         * Construct dictionary from a dictionary file, one word per line
         * @param dictionaryFile
         * @throws IOException
         */
        public Dictionary(String dictionaryFile) throws IOException {
            this.dictionaryFile = dictionaryFile;
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
            String line = null;
            while ((line=reader.readLine()) != null) {
                addWord(line.trim());
            }
            reader.close();
        }
    }
    
    private static SpellChecker.Dictionary dictionary = null; 
    
    private static String wordToAdd = "";


    /**
     * Constructs WordChecker with a dictionary given in dictionary file
     * @param dictionaryFile
     */
    public SpellChecker(String dictionaryFile) throws IOException {
    	Scanner scn = new Scanner(System.in);
    	//create dictionary object with the words from input dictionary file
    	SpellChecker.Dictionary dictionary = new SpellChecker.Dictionary(dictionaryFile); 
    	SpellChecker.dictionary = dictionary; 
    	/*
    	Iterator<String> dictIterator = dictionary.wordList.iterator();
    	while (dictIterator.hasNext()) {
    		System.out.println(dictIterator.next());
    	}
    	*/
    }

    /**
     * Update dictionary file
     */
    public void updateDictionary() {
    	dictionary.addWord(wordToAdd);
    }


    /**
     * Check words in the file targetFile and output the word in outputFile
     * @param targetFile input file
     * @param outputFile output file
     */
    public void checkWords(String targetFile, String outputFile) {
    	Scanner scanner = new Scanner(System.in);
    	String[] splitLine = null;
    	String s = "", testWord = "", punctuation = "" , replacementWord = "" , testWordCopy;
    	int userInput = -1; 
    	boolean hasPunctuation = false;
    	try {
			BufferedReader inputFileReader = new BufferedReader(new FileReader(targetFile));
			//should overwrite and create new output file, not append to existing one
			BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile));
			while ((s = inputFileReader.readLine()) != null){
				splitLine = s.split("\\s+");
				for (int i = 0; i < splitLine.length; i++) {
					hasPunctuation = false;
					punctuation = "";
					System.out.println("split line " + splitLine[i]);
					testWord = splitLine[i]; 
					//method to check if word has punctuation, if yes save punctuation 
					if (hasPunctuation(testWord)) {
						hasPunctuation = true;
						punctuation += testWord.charAt(testWord.length()-1);
						testWord = testWord.substring(0,testWord.length()-1);
					}
					if (! dictionary.isValid(testWord)) {
						System.out.println(testWord + " is not a valid word. Choose an option:");
						System.out.println(IGNORE_RULE_OPTION + ") Ignore Rule");
						System.out.println(VALID_WORD_OPTION + ") Valid Word");
						System.out.println(CORRECT_WORD_OPTION + ") Correct Word");
						System.out.println(IGNORE_WORD_OPTION + ") Ignore Word");
						userInput = scanner.nextInt();
						switch (userInput){
							case IGNORE_RULE_OPTION: 
								// ignore rule- add word to output file
								System.out.println("testWord: " + testWord);
								if (hasPunctuation) {
									testWord += punctuation; 
								}
								outputFileWriter.write(testWord + " ");
								continue;
							case VALID_WORD_OPTION:
								// valid word - include in output and to dictionary
								System.out.println("testWord: " + testWord);
								//wordToAdd = testWord;
								SpellChecker.dictionary.addWord(testWord);
								if (hasPunctuation) {
									testWord += punctuation; 
								}
								outputFileWriter.write(testWord + " ");
								continue;
							case CORRECT_WORD_OPTION:
								System.out.println("Please enter replacement word: ");
								replacementWord = scanner.next(); 
								while (! (replacementWord.matches("[a-zA-Z]+"))) {
									System.out.println("Invalid word (has non-alphabetic characters). Please enter a valid word: ");
									replacementWord = scanner.next();
								}
								if (hasPunctuation) {
									 replacementWord += punctuation; 
								}
								outputFileWriter.write(testWord + " ");
								//wordToAdd = replacementWord;
								SpellChecker.dictionary.addWord(testWord);	
								continue;
							case IGNORE_WORD_OPTION:
								//ignore word option-don't include in output..so basically don't do anything
								continue;
						}
					}
				}
			}
			
		    //s = inputFileReader.readLine();
			
			System.out.print("test string from input: " + s);
			//splitLine = s.split("\\s+");
			System.out.println("split line length" + splitLine.length);
			for (int i = 0; i < splitLine.length; i++) {
				System.out.println("split line " + splitLine[i]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Checks if input word has any of the punctuation marks in PUNCTUATION_MARKS variable
    public boolean hasPunctuation(String word) {
    	boolean result = false;
    	String s = ""; 
    	for (int i = 0; i < PUNCTUATION_MARKS.length(); i++) {
    		s = "";
    		s += PUNCTUATION_MARKS.charAt(i);
    		if (word.contains(s)) {
        		result = true;
        	}
    	}    	
    	return result; 
    }
    
    // checks to see if word is capitalized 
    public boolean isCapitalized(String word) {
    	boolean result = false;
    	String firstLetter = "";   
    	firstLetter = (word.length() > 1) ? word.substring(0,1) : word;
    	if (firstLetter.equals(firstLetter.toUpperCase())) {
    		result = true;
    	}
    	return result; 
    }


    public static void main(String [] args) throws Exception {
        String dictionaryFile = args[0];
        SpellChecker checker = new SpellChecker(dictionaryFile);
        String inputFile = args[1];
        String outputFile = args[2];
        checker.checkWords(inputFile, outputFile);
        checker.updateDictionary();
    }
}
