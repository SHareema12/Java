package samp;

import java.io.IOException;
import java.util.Scanner;

/**
 */
public class Chess {

	public static void main(String[] args) throws IOException, NullPointerException {

		Board br = new Board();
		Game game = new Game();
		Scanner sc = new Scanner(System.in);
		String input = "";
		boolean draw = false;
		boolean whiteTurn = true;
		boolean moveValue;
		br.toString();
		while (true) {
			if (whiteTurn == true) {
				// System.out.print("White's move: ");
				System.out.printf("White's move:");
			} else {
				// System.out.print("Blacks's move: ");
				System.out.printf("Black's move:");
			}
			input = sc.nextLine();
			if (input.toLowerCase().equals("resign")) {
				break;
			} 
			else if (input.length() == 11) {
				if (input.substring(6, 11).equals("draw?")) {
					draw = true;
				}
			} 
			else {
				if (game.friendlyCheck(br, input, whiteTurn).equals("invalid")){
					System.out.println("Invalid move try again");
				}
				else if (game.friendlyCheck(br, input, whiteTurn).equals("isInFriendlyCheck")){
					System.out.println("Invalid move try again");
				}
				moveValue = game.move(br, input, whiteTurn);
				if (moveValue == false) {
					System.out.println("Invalid move try again");
				} 
				else {
					if (whiteTurn == true) {
						for (int i = 0; i < 8; i++) {
							if (br.board[3][i].getClass().isInstance(new Pawn())) {
								br.board[3][i].ePos = false;
							}
						}
					} 
					else if (whiteTurn == false) {
						for (int i = 0; i < 8; i++) {
							if (br.board[4][i].getClass().isInstance(new Pawn())) {
								br.board[4][i].ePos = false;
							}
						}
					}
					whiteTurn = changeTurn(whiteTurn);
					br.toString();
				}
			}
		}
	}

	public static boolean changeTurn(boolean whiteTurn) {
		if (whiteTurn == true) {
			return false;
		}
		return true;
	}
}