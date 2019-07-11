//Bug-killer
//Lakshmi
//Thursday 10am
package Server;

//To support the game system to store a board information
public class StandardBoard implements Board {
	private char[][] board;
	
	public StandardBoard() {
		board = new char[20][20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	public int calculatePoint(int x1, int y1, int x2, int y2) {
		if(x1 == x2) {
			return Math.abs(y2 - y1 + 1);
		} else if(y1 == y2) {
			return Math.abs(x2 - x1 + 1);
		}
		return 0;		
	}

	public void placeCharacter(char c, int x, int y) {
		board[x][y] = c;
	}
	
	public char[][] getBoard() {
		return board;
	}
	public void printBoard() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				System.out.print(board[i][j]);
				if (j == 19) {
					System.out.println();
				}
			}
		}
	}
}
