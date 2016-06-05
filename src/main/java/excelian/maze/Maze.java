package excelian.maze;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import excelian.maze.Maze.Cell.Type;

/**
 * A Maze consists of walls 'X', Empty spaces ' ', one and only one Start point 'S' and one and only one exit 'F'.
 * Encapsulates an x by y array of neighbouring Cells as defined in {@code Cell}
 * First cell is of x,y coordinate (0,0)
 * Not ThreadSafe
 * @author FN
 */
public class Maze {

	private static final int MIN_CELL_LEN = 3;
	private static final int MIN_ROW_LEN = 3;
	private Cell[][] mazeCells;
	private Cell startCell;
	private Map<Type, AtomicInteger> contentsMap;

	public Maze(Cell[][] mazeCells) {
		validate(mazeCells);		
		this.mazeCells = mazeCells;		
		computeContents();
	}

	private void validate(Cell[][] cells) {
		ensureRowSize(cells);
		validateCells(cells);
	}

	private void ensureRowSize(Cell[][] cells) {
		if (!(cells.length >= MIN_ROW_LEN)) {
			throw new IllegalArgumentException("Maze must contan 3 or more rows");
		}
	}
	
	private void validateCells(Cell[][] cells) {
		for (int i = 0; i < cells.length; i++) {
			if (!(cells[i].length >= MIN_CELL_LEN)) {
				throw new IllegalStateException("Each row must contain at least 3 cells");
			} else if (cells[i].length != cells[0].length) {
				throw new IllegalStateException(
						String.format("Row %s is of non uniform cell size (%s) - %s cells expected.", i + 1,
								cells[i].length, cells[0].length));
			}
			if (cells[i][0].isSpaceType() || cells[i][cells[i].length - 1].isSpaceType()) {
				throw new IllegalStateException(String
						.format("First and or last cell of row %s should not be a %s type.", i + 1, Cell.Type.SPACE));
			}
		}
		// cells of top and bottom rows must contain no space
		ensureNoSpace(cells[0], 0);
		ensureNoSpace(cells[cells.length - 1], cells.length - 1);
		ensureSingleStartAndFinish(cells);
	}

	private void ensureSingleStartAndFinish(Cell[][] cells) {
		Set<Type> typeSet = new HashSet<>();
		for (int i = 0; i < cells.length; i++) {
			Cell[] cols = cells[i];
			for (int j = 0; j < cols.length; j++) {
				if (cols[j].isStart() || cols[j].isFinish()) {
					if (typeSet.contains(cols[j].type)) {
						throw new IllegalStateException(
								String.format("Maze should contain only one [%s] point/cell", cols[j].type));
					} else {
						typeSet.add(cols[j].type);
					}
				}
			}
		}
		
		if(!typeSet.contains(Type.START) ) {
			throw new IllegalStateException(String.format("Maze should contain a single [%s] point/cell", Type.START));			
		} else if (!typeSet.contains(Type.FINISH)) {
			throw new IllegalStateException(String.format("Maze should contain a single [%s] point/cell", Type.FINISH));
		}

	}

	private void ensureNoSpace(Cell[] cells, int rowId) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i].isSpaceType()) {
				throw new IllegalStateException(
						String.format("Cell %s of row %s contains unexpected space", i + 1, rowId));
			}
		}
	}
	

	
	private void computeContents() {
		if (contentsMap != null) {
			throw new IllegalStateException("Nultiple invocation not allowed.");
			
		}
		contentsMap = new HashMap<>();
		Cell[][] cells = mazeCells;
		for (int i = 0; i < cells.length; i++) {
			Cell[] cols = cells[i];
			for (int j = 0; j < cols.length; j++) {				
				if(contentsMap.get(cols[j].getType()) == null) {
					contentsMap.put(cols[j].getType(), new AtomicInteger(1));
				} else {
					(contentsMap.get(cols[j].getType())).incrementAndGet();
				}
			}
		}
	}

	public Cell getStartCell() {
		if (startCell != null) {
			return startCell;
		}
		Cell[][] cells = mazeCells;
		for (int i = 0; i < cells.length; i++) {
			Cell[] cols = cells[i];
			for (int j = 0; j < cols.length; j++) {
				if (cols[j].isStart()) {
					startCell = cols[j];
					return cols[j];
				}
			}
		}
		throw new IllegalStateException("Start cell not found as expected");
	}

	public Cell getCellAt(int x, int y) {
		return mazeCells[y][x]; 
	}
	
	public Integer getNumberOfWalls() {
		return contentsMap.get(Type.WALL).intValue();
	}
	
	public Integer getNumberOfSpaces() {
		return contentsMap.get(Type.SPACE).intValue();
	}


	public int size() {
		return mazeCells.length;
	}

	public static class Cell {
		enum Type {
			WALL("X"), SPACE(" "), START("S"), FINISH("F");
			private String code;

			Type(String code) {
				this.code = code;
			}

			public String getCode() {
				return code;
			}
		};

		private Type type;
		private Point coords;
		private boolean visited;

		public Cell(Type type, Point coords) {
			this.type = type;
			this.coords = coords;
		}

		public Type getType() {
			return type;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public final boolean isVisited() {
			return visited;
		}

		public final boolean isWallType() {
			return type == Type.WALL;
		}

		public final boolean isSpaceType() {
			return type == Type.SPACE;
		}

		public final boolean isStart() {
			return type == Type.START;
		}

		public boolean isFinish() {
			return type == Type.FINISH;
		}

		public int getX() {
			return (int) coords.getX();
		}

		public int getY() {
			return (int) coords.getY();
		}
		
		
		Point getCoords() {
			return coords;
		}

		@Override
		public String toString() {
			return "Cell [type=" + type + ", coords=[y=" + coords.getY() + ",x=" + coords.getX() + "], visited="
					+ visited + "]";
		}
	}
}
