package SudokuGrid;

/**
 * Created by Nts on 12/16/2015.
 * A Sudoku grid that can be created and accessed
 * Sizes other than 9x9 are not yet supported
 */
public class SudokuGrid {
	private int gridSize;
	private int[][] grid;
	private boolean[][] permanenceGrid;

	public SudokuGrid(int gridSize) {
		this.gridSize = gridSize;
		makeEmptyGrid();
	}

	// make an empty grid of numbers
	public void makeEmptyGrid() {
		grid = new int[gridSize][gridSize];
		permanenceGrid = new boolean[gridSize][gridSize];
		for (int i = 0; i < gridSize * gridSize; i++) {
			setGridNumberPosition(i, 0, false);
		}
	}

	// set the value of a grid position
	public void setGridNumberPosition(int position, int insertion, boolean permanent) {
		grid[position / gridSize][position % gridSize] = insertion;
		permanenceGrid[position / gridSize][position % gridSize] = permanent;
	}


	public int getGridNumberPosition(int position) {
		return grid[position / gridSize][position % gridSize];
	}

	public boolean getGridPositionPermanent(int position) {
		if (permanenceGrid[position / gridSize][position % gridSize]) {
			return true;
		} else
			return false;
	}

	private boolean gridTest(int position, int insertion) {
		return rowTest(insertion, position / gridSize, position % gridSize) && squareTest(insertion, position / gridSize, position % gridSize);
	}

	private boolean rowTest(int insertion, int r, int c) {
		boolean b = true;
		for (int row = 0; row < gridSize; row++) {
			if (insertion == grid[row][c])
				b = false;
		}
		for (int col = 0; col < gridSize; col++) {
			if (insertion == grid[r][col])
				b = false;
		}
		return b;
	}

	private boolean squareTest(int insertion, int r, int c) {
		int left, right;
		if (c % 3 == 0) {
			left = c;
			right = c + 2;
		} else if (c % 3 == 1) {
			left = c - 1;
			right = c + 1;
		} else {
			left = c - 2;
			right = c;
		}
		return vertSquareTest(insertion, left, right, r, c);
	}

	private boolean vertSquareTest(int insertion, int left, int right, int r, int c) {
		if (r % 3 == 0) {
			for (int row = r; row < r + 3; row++) {
				for (int col = left; col < right + 1; col++) {
					if (insertion == grid[row][col]) {
						return false;
					}
				}
			}

		} else if (r % 3 == 1) {
			for (int row = r - 1; row < r + 2; row++) {
				for (int col = left; col < right + 1; col++) {
					if (insertion == grid[row][col]) {
						return false;
					}
				}
			}
		} else if (r % 3 == 2) {
			for (int row = r - 2; row < r + 1; row++) {
				for (int col = left; col < right + 1; col++) {
					if (insertion == grid[row][col]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// clear the grid of numbers that aren't permanent
	private void clearTemporary() {
		for (int i = 0; i < gridSize * gridSize; i++) {
			if (!getGridPositionPermanent(i)) {
				setGridNumberPosition(i, 0, false);
			}
		}
	}

	// solves the remaining grid using a brute force method
	public int solve() {
		clearTemporary();
		int position = 0;
		int reversalCounter = 0;
		boolean reversing = false;
		boolean success = false;
		while (position < gridSize * gridSize) {
			// skip the index if it is permanent
			if (getGridPositionPermanent(position)) {
				if (reversing)
					position -= 1;
				else
					position += 1;
			} // if
			else {
				int insertion;
				if (getGridNumberPosition(position) == 0)
					insertion = 1;
				else
					insertion = getGridNumberPosition(position) + 1;
				for (int timeout = insertion; timeout <= gridSize; timeout++) {
					if (gridTest(position, insertion)) {
						if (getGridPositionPermanent(position)) {
							success = true;
							reversing = false;
							break;
						}
						setGridNumberPosition(position, insertion, false);
						success = true;
						reversing = false;
						break;
					} // if
					else {
						success = false;
						reversing = true;
						insertion += 1;
					}
				} // for timeout
				if (success)
					position += 1;
				else {
					setGridNumberPosition(position, 0, false);
					position -= 1;
					if (position < 0) {
						break;
					}
					reversalCounter += 1;
				} // else
			} // else
		} // while
		return reversalCounter;
	} // solve
} // SudokuGrid