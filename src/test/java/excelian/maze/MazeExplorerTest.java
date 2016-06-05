package excelian.maze;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.hamcrest.Matchers;
import org.junit.Test;

import excelian.maze.Maze.Cell;

public class MazeExplorerTest {

	/**
	 * AC: An explorer must be able to automatically explore a maze and find the
	 * exit, and on exit they must be able to state the route they took in an
	 * understandable fashion.
	 * @throws Exception
	 */
	@Test
	public void testExploreMaze() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader(new FileInputStream("src/test/resources/maze.txt")));
		Explorer explorer = new Explorer(maze);
		explorer.exploreMaze();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.FINISH));
		assertThat(explorer.whereAmI().getCoords(), Matchers.is(new Point(1, 14)));
		assertThat(explorer.getHistory(), Matchers.hasSize(76));
		//ensure that first and last cell is appropriately stored in history stack
		assertThat(explorer.getHistory().peekFirst().getType(), Matchers.is(Cell.Type.START));
		assertThat(explorer.getHistory().peekLast().getType(), Matchers.is(Cell.Type.FINISH));
	}

	/**
	 * Understand what is in front of them; Understand all movement options from
	 * their given location; Have a record of where they have been. 
	 * @throws Exception
	 */
	@Test
	public void testMoveDown() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(
				new InputStreamReader(new ByteArrayInputStream("XXXX\nXS X\nXX X\nX XX\nX  X\nXXXF".getBytes())));
		Explorer explorer = new Explorer(maze);
		explorer.moveToStartPoint();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.START));
		// move down to next row from that of S
		explorer.moveDown();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.WALL));
		// move down again - should see a space
		explorer.moveDown();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.SPACE));
	}

	/**
	 * Understand what is in front of them; Understand all movement options from
	 * their given location; Have a record of where they have been. 
	 * @throws Exception
	 */
	@Test
	public void testMoveUp() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(
				new InputStreamReader(new ByteArrayInputStream("XXXX\nXX X\nX XX\nXX X\nX SX\nXXXF".getBytes())));
		Explorer explorer = new Explorer(maze);
		explorer.moveToStartPoint();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.START));
		// move up a row from that of S
		explorer.moveUp();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.SPACE));
		// move up again - should see a space
		explorer.moveUp();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.WALL));
	}

	/**
	 * AC: An explorer in a maze must be able to: Move forward; Turn left and
	 * right; Understand what is in front of them;
	 */
	@Test
	public void testTurnLeft() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader(new FileInputStream("src/test/resources/maze.txt")));
		Explorer explorer = new Explorer(maze);
		explorer.moveToStartPoint();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.START));
		// turn to left of start S
		explorer.turnLeft();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.WALL));
		// turn to left again - should see a space
		explorer.turnLeft();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.SPACE));
	}

	/**
	 * AC: An explorer in a maze must be able to: Move forward; Turn left and
	 * right; Understand what is in front of them;
	 */
	@Test
	public void testTurnRight() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(new InputStreamReader(new FileInputStream("src/test/resources/maze.txt")));
		Explorer explorer = new Explorer(maze);
		explorer.moveToStartPoint();
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.START));
		explorer.turnRight();
		// in maze.txt, a Space ' ' is to the right of S cell
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.SPACE));
		assertThat(explorer.getHistory(), Matchers.hasSize(2));
	}

	/**
	 * Given a maze the explorer should be able to drop in to the Start point
	 */
	@Test
	public void testMoveToStartPoint() throws Exception {
		MazeReader reader = new MazeReader();
		Maze maze = reader.makeMaze(
				new InputStreamReader(new ByteArrayInputStream("XXXX\nXS X\nXX X\nX XX\nX  X\nXXXF".getBytes())));
		Explorer explorer = new Explorer(maze);
		explorer.moveToStartPoint();
		assertThat(explorer.whereAmI(), Matchers.notNullValue());

		assertThat(explorer.whereAmI().getCoords(), Matchers.is(new Point(1, 1)));
		assertThat(explorer.whereAmI().getType(), Matchers.is(Cell.Type.START));
	}

}
