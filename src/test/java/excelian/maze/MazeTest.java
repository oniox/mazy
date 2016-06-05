package excelian.maze;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.hamcrest.Matchers;
import org.junit.Test;

import excelian.maze.Maze.Cell;

public class MazeTest {

	@Test
	public void testGetStartCell() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nXXXF".getBytes())));
		assertThat(maze.getStartCell(), Matchers.notNullValue());	
		
		assertThat(maze.getStartCell().getX(), Matchers.is(1));
		assertThat(maze.getStartCell().getY(), Matchers.is(1));
	}

	/**
	 * AC: After a maze has been created I should be able to put in a coordinate and know what exists at that point.
	 * @throws Exception.
	 */
	@Test
	public void testGetCellAt() throws Exception{
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nXXXF".getBytes())));
		assertThat(maze.getCellAt(2,3), Matchers.notNullValue());	
		assertThat(maze.getCellAt(2,3).getCoords(), Matchers.is(new Point(2,3)));
		assertThat(maze.getCellAt(2,3).getType(), Matchers.is(Cell.Type.WALL));
		assertThat(maze.getCellAt(1,1).getType(), Matchers.is(Cell.Type.START));
		assertThat(maze.getCellAt(2,2).getType(), Matchers.is(Cell.Type.SPACE));
		assertThat(maze.getCellAt(3,3).getType(), Matchers.is(Cell.Type.FINISH));
	}

	@Test
	public void ensureAccurateSize() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nXXXF".getBytes())));
		assertThat(maze.size(), Matchers.is(4));
	}
	
	/**
	 * AC: After a maze has been created the number of walls and empty spaces should be available to me.  
	 */
	@Test
	public void ensureExpectedNumOfWallsAndSpaces() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nX XX\nX  X\nXXXF".getBytes())));
		assertThat(maze.getNumberOfWalls(), Matchers.is(17));
		assertThat(maze.getNumberOfSpaces(), Matchers.is(5));
	}
	
	/**
	 * AC: one and only one Start point 'S' and one and only one exit 'F' 
	 */
	@Test(expected=IllegalStateException.class)	
	public void exceptionShouldOccurOnMultipleExitPoints() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nX XX\nX  X\nFXXF".getBytes())));		
	}
	
	/**
	 * AC: one and only one Start point 'S' and one and only one exit 'F' 
	 */
	@Test(expected=IllegalStateException.class)	
	public void exceptionShouldOccurOnNoExitPoint() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nX XX\nX  X\nXXXX".getBytes())));		
	}
	
	/**
	 * AC: one and only one Start point 'S' and one and only one exit 'F' 
	 */
	@Test(expected=IllegalStateException.class)	
	public void exceptionShouldOccurOnMultipleStartPoints() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXS X\nXX X\nS XX\nX  X\nXXXF".getBytes())));		
	}
	
	/**
	 * AC: one and only one Start point 'S' and one and only one exit 'F' 
	 */
	@Test(expected=IllegalStateException.class)	
	public void exceptionShouldOccurNoStartPoint() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader( new ByteArrayInputStream("XXXX\nXX X\nXX X\nX XX\nX  X\nXXXF".getBytes())));		
	}

}
