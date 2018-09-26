
package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire
 * Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {

	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;

	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a
	 * circular linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i = 0; i < cardValues.length; i++) {
			cardValues[i] = i + 1;
		}

		// shuffle the cards
		Random randgen = new Random();
		for (int i = 0; i < cardValues.length; i++) {
			int other = randgen.nextInt(28);
			int temp = cardValues[i];
			cardValues[i] = cardValues[other];
			cardValues[other] = temp;
		}

		// create a circular linked list from this deck and make deckRear point
		// to its last node
		CardNode cn = new CardNode();
		cn.cardValue = cardValues[0];
		cn.next = cn;
		deckRear = cn;
		for (int i = 1; i < cardValues.length; i++) {
			cn = new CardNode();
			cn.cardValue = cardValues[i];
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = cn;
			deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
		System.out.println("initial makedeck rear value:" + deckRear.cardValue);
		System.out.println("deckRear.next value:" + deckRear.next.cardValue);
	}

	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode ptr = deckRear;
		CardNode aft = deckRear.next;
		System.out.println("Initial deckRear:" + deckRear.cardValue + "aft: " + deckRear.next.cardValue);
		int jokerA, temp;
		do {
			if (ptr.cardValue == 27) {
				jokerA = ptr.cardValue;
				temp = aft.cardValue;
				aft.cardValue = jokerA;
				ptr.cardValue = temp;
				break;
				// System.out.print("Joker A method:" );
			}
			ptr = aft;
			aft = aft.next;
		} while (ptr != deckRear);
		printList(deckRear);
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		CardNode prev = deckRear, ptr = deckRear.next, aft = deckRear.next.next;
		System.out.println("prev in jokerB:" + prev.cardValue);
		int jokerB, second, third;
		do {
			if (prev.cardValue == 28) {
				jokerB = prev.cardValue;
				second = ptr.cardValue;
				third = aft.cardValue;
				prev.cardValue = second;
				ptr.cardValue = third;
				aft.cardValue = jokerB;
				// System.out.print("Joker B method:" );
				break;
			}
			prev = ptr;
			ptr = aft;
			aft = aft.next;
		} while (prev != deckRear);
		printList(deckRear);
	}

	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		CardNode j1 = null, j2 = null, startj1 = deckRear.next, endj1 = null, startj2 = null, endj2 = deckRear,
				ptr = deckRear, prev = null;
		// finding correct positions where triple cut should occur
		do {
			// first time rear might be joker
			if (ptr.cardValue == 27 || ptr.cardValue == 28) {
				if (ptr == deckRear) {
					j2 = ptr;
					endj2 = null;
				}
				if (j1 == null && ptr != deckRear) {
					j1 = ptr;
					if (startj1 == ptr)
						startj1 = null;
					if (prev != deckRear)
						endj1 = prev;
				} else if (j1 !=null){
					j2 = ptr;
					startj2 = ptr.next;
				}
			}
			if (j1 != null && j2 != null)
				break;
			prev = ptr;
			ptr = ptr.next;
		} while (ptr != deckRear);

		// moving cards to perform triple cut
		// breaking links
		//joker at front
		if (startj1 == null && endj1 == null) {
			deckRear = j2;
			printList(deckRear);
			return;
		}
		//both jokers on ends 
		else if (j2.next == j1){
			printList(deckRear);
			return;
		}
		//joker at end
		else if (startj2 == null && endj2 == null){
			deckRear = endj1;
			printList(deckRear); 
			return;
		}
		//all other cases
		else{ 
			if (endj1 != null) {
				endj1.next = null;
			}
			if (startj2 != null) {
				j2.next = null;
			}
			// do I need this??
			if (endj2 != null) {
				endj2.next = null;
			} else {
				j2.next = null;
			}
			// making new links
			if (startj1 != null) {
				j2.next = startj1;
			}
			if (endj2 != null) {
				endj2.next = j1;
			}
			if (startj2 == null) {
				endj1.next = j1;
			}
			if (endj1 != null && startj2 != null) {
				endj1.next = startj2;
			} else if (startj2 != null && endj1 == null) {
				j2.next = startj2;
			}
		}
		// updating deckRear
		if (j2.next != j1) {
			if (endj1 != null) {
				deckRear = endj1;
				printList(deckRear);
				return;
			}
			if (j2.next == startj2) {
				deckRear = j2;
				printList(deckRear);
				return;
			}
		}
		// System.out.println("Triple Count Method");
	}

	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {
		// COMPLETE THIS METHOD
		int num = deckRear.cardValue, count = 1;
		CardNode start = deckRear.next, end = deckRear.next, prev = deckRear.next, newFront = null;
		if (num == 27 || num == 28) {
			// printList(deckRear);
			return;
		}
		while (count < num) {
			end = end.next;
			count++;
		}
		newFront = end.next;
		while (prev != deckRear) {
			if (prev.next == deckRear)
				break;
			prev = prev.next;
		}
		deckRear.next = null;
		end.next = null;
		prev.next = null;
		prev.next = start;
		end.next = deckRear;
		deckRear.next = newFront;
		printList(deckRear);
		// System.out.println("Count cut method");
	}

	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count
	 * Cut, then counts down based on the value of the first card and extracts
	 * the next card value as key. But if that value is 27 or 28, repeats the
	 * whole process (Joker A through Count Cut) on the latest (current) deck,
	 * until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		boolean repeat = true;
		int key = -1;
		while (repeat == true) {
			jokerA();
			System.out.println("Called Joker A in Getkey" + deckRear.cardValue);
			jokerB();
			System.out.println("Called Joker B in Getkey" + deckRear.cardValue);
			tripleCut();
			// System.out.println("Called tripleCut in Getkey");
			countCut();
			// System.out.println("Called countCut in Getkey");
			CardNode ptr = deckRear.next;
			int num = deckRear.next.cardValue, count = 1;
			// changing 28 to 27
			if (num == 28) {
				num = 27;
			}
			while (count < num) {
				ptr = ptr.next;
				count++;
			}
			key = ptr.next.cardValue;
			System.out.println(key);
			if (key != 27 && key != 28)
				repeat = false;
		}
		return key;
	}

	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear
	 *            Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) {
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message
	 *            Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String ucMessage = "";
		// generating all caps message to be encrypted
		for (int i = 0; i < message.length(); i++) {
			if (Character.isLetter(message.charAt(i)) == true) {
				ucMessage += Character.toUpperCase(message.charAt(i));
			}
		}
		// encryption
		String result = "";
		for (int i = 0; i < ucMessage.length(); i++) {
			int key = getKey();
			// System.out.println("called GetKey in encryption " + "key:" + i+1
			// + key);
			int c = ucMessage.charAt(i) - 'A' + 1 + key;
			System.out.println("letter value:" + c);
			if (c > 26) {
				c = c - 26;
			}
			result += toChar(c);
		}
		return result;
	}

	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message
	 *            Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String decrypted = "";
		for (int i = 0; i < message.length(); i++) {
			int key = getKey();
			int code = message.charAt(i) - 'A' + 1;
			if (code <= key) {
				code = (code + 26) - key;
			} else {
				code = code - key;
			}
			decrypted += toChar(code);
		}
		return decrypted;
	}

	// my own copy list method
	private CardNode copyDR(CardNode rear) {
		CardNode ogDRCopy = null, ptr = rear;
		do {
			CardNode n = new CardNode();
			if (ogDRCopy == null) {
				ogDRCopy = n;
				ogDRCopy.cardValue = ptr.cardValue;
				ogDRCopy.next = ogDRCopy;
			}
			n.next = ogDRCopy.next;
			ogDRCopy.next = n;
		} while (ptr != rear);

		return ogDRCopy;
	}

	// my own method to convert int to char
	private String toChar(int c) {
		String result = "";
		switch (c) {
		case 1:
			result = "A";
			break;
		case 2:
			result = "B";
			break;
		case 3:
			result = "C";
			break;
		case 4:
			result = "D";
			break;
		case 5:
			result = "E";
			break;
		case 6:
			result = "F";
			break;
		case 7:
			result = "G";
			break;
		case 8:
			result = "H";
			break;
		case 9:
			result = "I";
			break;
		case 10:
			result = "J";
			break;
		case 11:
			result = "K";
			break;
		case 12:
			result = "L";
			break;
		case 13:
			result = "M";
			break;
		case 14:
			result = "N";
			break;
		case 15:
			result = "O";
			break;
		case 16:
			result = "P";
			break;
		case 17:
			result = "Q";
			break;
		case 18:
			result = "R";
			break;
		case 19:
			result = "S";
			break;
		case 20:
			result = "T";
			break;
		case 21:
			result = "U";
			break;
		case 22:
			result = "V";
			break;
		case 23:
			result = "W";
			break;
		case 24:
			result = "X";
			break;
		case 25:
			result = "Y";
			break;
		case 26:
			result = "Z";
			break;
		}

		return result;
	}
}