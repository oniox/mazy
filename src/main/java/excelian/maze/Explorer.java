package excelian.maze;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import excelian.maze.Maze.Cell;

/**
 * World famous Maze explorer
 * Not ThreadSafe
 */
public class Explorer {
	
	private LinkedList<Cell> history = new LinkedList<>();
	private Logger logger = Logger.getLogger(Explorer.class);	
	private Maze maze;
	private Cell currentCell;

	public Explorer(Maze maze) {
		this.maze = maze;
	}

	public void exploreMaze() {
		history.clear();
		Cell startCell = getStartCell();
		navigate(startCell.getX(), startCell.getY(), true);
		
	}
	
	private String getCoordsAsStr(Cell cell) {
		return String.format("coordinates [y=%s, x=%s]", cell.getY(), cell.getX());  
	}

	/**
	 * Navigate maze - in manual or auto mode 
	 * @param x 
	 * @param y
	 * @param auto if <code>true</code>, navigates to F/FINSIH point - recursive backtracking
	 * @return 
	 */
	private boolean navigate(int x, int y, boolean auto) {
		currentCell = maze.getCellAt(x, y);
		if (currentCell.isFinish()) {
			addToHistory(currentCell);
			logger.info(String.format("I have reached the end of the maze [%s] : %s", currentCell.getType(),getCoordsAsStr(currentCell)));
			logger.info("HISTORY : " +  history);
			return true;
		} else if (currentCell.isVisited()) {
			logger.info(String.format("I just encountered a cell I have already visited : %s", getCoordsAsStr(currentCell)));
			return false;
		} else if (currentCell.isWallType()) {
			logger.info(String.format("I just hit a wall at %s", getCoordsAsStr(currentCell)));
			return false;
		}
		logger.info(String.format("Visiting [%s] cell : %s", currentCell.getType(), getCoordsAsStr(currentCell)));		
		addToHistory(currentCell);
		
		if (!auto) {
			return false;
		}
		
		// attempt navigation to right cell and if not accessible (wall or
		// already visited), attempt navigation to cell below it, if not
		// accessible, attempt left cell,
		// if not accessible, attempt top cell.	- NOT very efficient :'(	
		if ((turnRight(x, y, true)) || (moveDown(x, y, true)) || turnLeft(x, y, true) || moveUp(x, y, true)) {
			return true;
		}
		return false;
	}

	private void addToHistory(Cell currentCell) {
		currentCell.setVisited(true);
		history.add(currentCell);
	}
	
	public boolean moveDown() {
		reset();
		return moveDown(currentCell.getX(), currentCell.getY(), false);		
	}
	
	public boolean moveUp() {
		reset();
		return moveUp(currentCell.getX(), currentCell.getY(), false);		
	}
	
	public boolean turnLeft() {
		reset();
		return turnLeft(currentCell.getX(), currentCell.getY(), false);		
	}
	
	public boolean turnRight() {
		reset();
		return turnRight(currentCell.getX(), currentCell.getY(), false);		
	}
	
	public LinkedList<Cell> getHistory() {
		return new LinkedList<>(history);
	}

	private void reset() {
		if (currentCell == null ) {
			history.clear();
			moveToStartPoint();
		}
	}

	void moveToStartPoint() {		
		currentCell = maze.getStartCell();
		navigate(currentCell.getX(), currentCell.getY(), false);		
	}

	private boolean moveDown(int x, int y, boolean auto) {
		return (y < maze.size() - 1) && navigate(x, y + 1, auto);
	}

	private boolean turnLeft(int x, int y, boolean auto) {
		return x > 0 && navigate(x - 1, y, auto);
	}

	private boolean moveUp(int x, int y, boolean auto) {
		return y > 0 && navigate(x, y - 1, auto);
	}

	private boolean turnRight(int x, int y, boolean auto) {
		return x < maze.size() - 1 && navigate(x + 1, y, auto);
	}
	
	public Cell whereAmI() {
		return currentCell;
	}
	
	private Cell getStartCell() {
		return maze.getStartCell();		
	}		
	
	/** Auto explore maze starting form S to finish a F */
	public static void main(String[] args) throws Exception  {
		Maze makeMaze = (new MazeReader()).makeMaze();
		Explorer exp = new Explorer(makeMaze);	
		//solve the maze
		exp.exploreMaze();
	}

}
