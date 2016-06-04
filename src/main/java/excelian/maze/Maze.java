package excelian.maze;

import java.awt.Point;

public class Maze {
	
	private static final int MIN_CELL_LEN = 3;
	private static final int MIN_ROW_LEN = 3;
	private Cell[][] mazeCells;
	
	public Maze(Cell[][] mazeCells) {
		validate(mazeCells);
		this.mazeCells = mazeCells;
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
	
	/**
	 * C
	 * @param cells
	 */
	private void validateCells(Cell[][] cells) {		
		for (int i = 0; i < cells.length; i++) {
			if (!(cells[i].length >= MIN_CELL_LEN)) {
				throw new IllegalStateException("Each row must contain at least 3 cells");
			} else if (cells[i].length != cells[0].length) {				
				throw new IllegalStateException(String.format("Row %s is of non uniform cell size (%s) - %s cells expected.", i+1, cells[i].length, cells[0].length));
			} 
			if (cells[i][0].isSpaceType() || cells[i][cells[i].length-1].isSpaceType()) {
				throw new IllegalStateException(String.format("First and or last cell of row %s should not be a %s type.", i+1, Cell.Type.SPACE));
			}
		}		
		//cells of top and bottom rows must contain no space  
		ensureNoSpace(cells[0],0);		
		ensureNoSpace(cells[1],1);		
	}
	
	private void ensureNoSpace(Cell[] cells, int rowId) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i].isSpaceType()) {
				throw new IllegalStateException(String.format("Cell %s of row %s contains unexpected space", i+1, rowId));				
			}
		}		
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
		
		public boolean isVisited() {
			return visited;
		}
		
		public boolean isSpaceType() {
			return type == Type.SPACE;
		}
		
		public Point getCoords() {
			return coords;
		}
		
		
	}
}
