	//coordinate data structure
public class coord {

		// override equals attribute so coord, does not get duplicates in set.
		@Override
		public boolean equals (Object other) {
			return this.hashCode() == other.hashCode();
		}

//create hashcode that wont give true for pair  x = 2, y = 3 and x2 = 3, y2 = 2.
//maybe make x as negative value and y as positive?

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 31 * hash -(this.x);
			hash = 31 * hash + this.y;
			//if x is larger than y then positive, if y is larger then negative.
			return  x <= y ? hash : Math.abs(hash);
			//basically it will result in a different hash depending on if x > y.
			//stops the problem of if x = 2, y = 3 and x = 3 , y = 2 being the same hash.
		}
		private int x , y;

		public void coord (int x, int y) {
			this.x = x;
			this.y = y; 
		}

		public int getY() {
			return y;
		}

		public int getX() {
			return x;
		}
		//compare x and y values.
		private boolean compare(coord one, coord two) {
			return (one.x == two.x) && (one.y == two.y);
		}

		public String toString() {
			return "x : " + x + "\ny : " + y;
		}
}
