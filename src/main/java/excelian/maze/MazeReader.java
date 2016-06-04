package excelian.maze;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import excelian.maze.Maze.Cell.Type;

public class MazeReader {	
	
	private Logger logger = Logger.getLogger(MazeReader.class);
	
	public Maze makeDefaultMaze() throws Exception {
		List<String> readDefault = readDefault();				
		return parse(readDefault);
		
	}
	
	public List<String> readDefault() throws Exception {
		return read(new FileReader("src/main/resources/maze.csv"));
	}
	
	public List<String> read(InputStreamReader inputStreamReader) throws Exception {		
		List<String> lines = new ArrayList<>();
		String line = null;
		try (BufferedReader bufReader = new BufferedReader(inputStreamReader);) {
			while((line=bufReader.readLine()) != null) {
				System.out.println(line);
				if (!line.trim().equals("")) {
					lines.add(line);					
				} else {					
					logger.warn("Empty line detected, skipping it.");
				}
			}					
		}		
		return lines;
	}
	
	private Maze parse(List<String>  mazeData) {
		Maze.Cell mazeCells [][] = null;
		for (int y = 0; y < mazeData.size(); y++) {
			String row = mazeData.get(y);
			char[] cells = row.toCharArray();
			
			if (mazeCells == null) {
				mazeCells = new Maze.Cell[mazeData.size()][cells.length];
			}
			/**every column must contain at least {@link MazeReader#MIN_COL_LEN} columns - and number of columns in each row must match that of the first.*//*
			validateCols(mazeCells[0].length, y, cols);*/
							
			for (int x = 0; x < cells.length; x++) {
				Maze.Cell cell = new Maze.Cell(typeOf(String.valueOf(cells[x])), new Point(x,y));
				mazeCells[y][x] = cell;
			}			
		}
		return new Maze(mazeCells);
	}

	/*private void validateCols(int expectedColLength, int y, char [] cols) {
		int colLength = cols.length;
		if (colLength > MIN_COL_LEN && colLength != expectedColLength) {
			String expMsg = (colLength == 0 ) ? String.format("Row %s  must contain at least one column", y) : String.format("Row %s is of non uniform column size (%s) - %s columns expected.", y+1, colLength, expectedColLength);
			throw new IllegalStateException(expMsg);
		}
		if (typeOf(String.valueOf(cols[0])) != Cell.Type.WALL || typeOf(String.valueOf(cols[colLength-1])) != Cell.Type.WALL ) {
			String expMsg = String.format("First and or last column of row %s is of wrong type - %s type expected.", y+1, Cell.Type.WALL.getCode());
			throw new IllegalStateException(expMsg);						
		}
	}*/
	
	private static Map<String, Type> lookup = new HashMap<>();
	static {	      
		for(Type t : EnumSet.allOf(Type.class))
	           lookup.put(t.getCode(), t);
	}
	
	private static Type typeOf(String code) {
		Type type = lookup.get(code);
		if (type == null ) {
			throw new IllegalArgumentException(String.format("Type code %s not valid", code));
		}
		return type;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(typeOf("X"));
		//new MazeReader().read("src/main/resources/maze.csv");
		
		new MazeReader().makeDefaultMaze();
	}

}
