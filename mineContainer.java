import java.util.*;
public class mineContainer{
	public int[][] board;
	public boolean[][] mask; // clarifies which parts of the board is shown to the player.
	//ini board

	//hashhelper for coord


	public mineContainer() {
		board = new int[12][12]; // initialize a 12 by 12 board. 
		mask = new boolean[12][12]; //defaults to false;
	}


	

	//method to add bombs of int -1 randomly onto board.
	//expand by adding easy, medium, hard difficulty later, but make easy work first.
	//change back to private, but public rn for testing purposes.
	public void bomb(int bombs) {
		//get random coordinates for n bombs
		Set<coord> set = new HashSet<>();

		//while set size is less than bomb, so if duplicates, loop will continue until correct number of bombs .
		while(set.size() < bombs) {
		//give random set of coordinates
		//Math.random() * (max - min + 1) + 0 to give range min -> max
		 	int x = (int)Math.floor(Math.random()*(11- 0 +1)+ 0);
		 	int y = (int)Math.floor(Math.random()*(11- 0 +1)+ 0);
		 	coord entry = new coord(); // create new class coord
		 	entry.coord(x,y);
		 	set.add(entry);
		}
		//set now has all coordinates.
		//loop through set and put -1 into x,y.
		for (coord c : set) {
			board[c.getX()][c.getY()] = -1;
			//get neighbourhood. if it is within the scope of the board, +1 to that index.
			//+1 because we do not want to set to 1 and then set it again to 1 per iteration.
			for (int i = c.getX() - 1; i <= c.getX() + 1; i++) {
				for (int j = c.getY() - 1; j <= c.getY() + 1; j++) {
					if ((i > -1 && i < board.length ) && (j > -1  && j < board[0].length) && board[i][j] != -1) board[i][j]++;
				}
			}
		}

	}

	//calls a position on boolean[] and 3 different outcomes
	public void  move(coord xy) {

		int x = xy.getX();
		int y = xy.getY();

		if (mask[x][y]) return;
		//if number value reveal neighbourhood.
		if (board[x][y] > 0) {
			//reveal this position.
			mask[x][y] = true; // if mask is true, show this to user.

		}
		//if 0 reveal all other 0s.recursively call move 
		else if (board[x][y]  == 0) {
			mask[x][y] = true;
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if ((i > -1 && i < board.length ) && (j > -1  && j < board[0].length)) {
						coord m1 = new coord(); // create new class coord
		 				m1.coord(i,j);
						move(m1); // recursively call move.
					}
				}
			}

		}
		else {
			//fail
		}
		//if 0, recursively search for all neighbouring 0s and open these values
		//maybe recursively search move until a number value is hit.

		//if -1 end game. So maybe hold off on this until gui made.

	}



	//once all bombs have been placed, give minesweeper coordinates.e.g 1 if only one bomb in 3x3 neighbourhood.
	//private void sweepValues() {

	//}
	//@Override
	public String toString() {
		String res = "";

		for (int i = 0; i < board.length; i++) {
			res += "[";
			for (int j = 0; j < board[0].length; j++) {
				if (mask[i][j]) res += "" +board[i][j];
				else res += "*";

				if (j != board[0].length -1) res += ","; 
			}
			res += "]\n";
		}

		return res;
	}
	//add 
}