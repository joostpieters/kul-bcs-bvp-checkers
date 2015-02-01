package domain.input;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import domain.analyser.LegalActionAnalyser;
import domain.board.BoardFactory;
import domain.board.contracts.IBoard;
import domain.game.Game;
import domain.game.contracts.IGame;
import domain.location.LocationOutOfRangeException;

public class ActionInputTest
{
	private ActionInput getInput(String move, String inputFile) throws LocationOutOfRangeException
	{
		try
		{
			IBoard board = BoardFactory.create(Paths.get("data", "input", inputFile));
			IGame game = new Game(board);
			LegalActionAnalyser analyzer = new LegalActionAnalyser(board);
			return new ActionInput(move, game, analyzer);
		}
		catch (IOException ex)
		{
			fail(ex.getMessage());
			return null;
		}
	}
	
	@Test
	public void testInvalidRequest() throws LocationOutOfRangeException
	{
		ActionInput input = getInput("lalala", "testCatchPriority.txt");
		boolean result = input.process();
		assertFalse(result);	
	}
	
	@Test
	public void testIllegalAction() throws LocationOutOfRangeException
	{
		ActionInput input = getInput("36x27", "testCatchPriority.txt"); //non-maximal catch
		boolean result = input.process();
		assertFalse(result);		
	}
	
	
	@Test
	public void testInvalidAction() throws LocationOutOfRangeException
	{
		ActionInput input = getInput("36-31", "default.txt"); //move to occupied square
		boolean result = input.process();
		assertFalse(result);
	}
	
	@Test
	public void testValidAction() throws LocationOutOfRangeException
	{
		ActionInput input = getInput("36x27x38x29x40x49", "testCatchPriority.txt"); //maximal catch
		boolean result = input.process();
		assertTrue(result);
	}
}
