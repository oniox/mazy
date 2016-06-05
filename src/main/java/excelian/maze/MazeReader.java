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

/**
 * Reads Maze data from specified and parses into {@code Maze} instance.  
 * @author FN
 */
public class MazeReader {	
	
	private static final String DEFAULT_FILE_PATH = "src/main/resources/maze.txt";
	private Logger logger = Logger.getLogger(MazeReader.class);
	
	public Maze makeMaze() throws Exception {
		List<String> readDefault = readDefault();				
		return parse(readDefault);
		
	}
	
	private List<String> readDefault() throws Exception {
		return read(new FileReader(DEFAULT_FILE_PATH));
	}
	
	public Maze makeMaze(InputStreamReader mazeStream) throws Exception {
		List<String> readDefault = read(mazeStream);				
		return parse(readDefault);		
	}
	
	private List<String> read(InputStreamReader inputStreamReader) throws Exception {		
		List<String> lines = new ArrayList<>();
		String line = null;
		try (BufferedReader bufReader = new BufferedReader(inputStreamReader);) {
			while((line=bufReader.readLine()) != null) {				
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
							
			for (int x = 0; x < cells.length; x++) {
				Maze.Cell cell = new Maze.Cell(typeOf(String.valueOf(cells[x])), new Point(x,y));
				mazeCells[y][x] = cell;
			}			
		}
		return new Maze(mazeCells);
	}
	
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
}
