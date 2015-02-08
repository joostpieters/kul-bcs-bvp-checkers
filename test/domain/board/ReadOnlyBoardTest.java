package domain.board;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.location.LocationPair;

public class ReadOnlyBoardTest
{
	private static IBoard original;
	private static IReadOnlyBoard readonly;

	static
	{
		try
		{
			original = BoardFactory.create(Paths.get("data", "input", "default.txt"));
			readonly = new ReadOnlyBoard(original);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (LocationOutOfRangeException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetBoardSize()
	{
		assertEquals(original.getSize(), readonly.getSize());
	}

	@Test
	public void testGetDeepClone()
	{
		assertEquals(original, readonly.getDeepClone());
	}
	
	@Test
	public void testGetPlayerPieces()
	{
		assertEquals(	original.getPlayerPieces(Player.White), 
						readonly.getPlayerPieces(Player.White));
		assertEquals(	original.getPlayerPieces(Player.Black), 
						readonly.getPlayerPieces(Player.Black));
	}
	
	@Test
	public void testGetSquare() throws LocationOutOfRangeException
	{
		Location location = new Location(0, 0, original.getSize());
		assertEquals(original.getSquare(location).getReadOnlySquare(), readonly.getSquare(location));
	}
	
	@Test
	public void testIsLocationFree() throws LocationOutOfRangeException
	{
		Location target = new Location(5, 5, original.getSize());
		assertEquals(original.isLocationFree(target), readonly.isLocationFree(target));
	}
	
	@Test
	public void testIsLocationOccupiedBy() throws LocationOutOfRangeException
	{
		Location target = new Location(5, 5, original.getSize());
		assertEquals(	original.isLocationOccupiedBy(Player.White, target), 
						readonly.isLocationOccupiedBy(Player.White, target));
		assertEquals(	original.isLocationOccupiedBy(Player.Black, target), 
						readonly.isLocationOccupiedBy(Player.Black, target));
	}
	
	@Test
	public void testIsValidMove() throws LocationOutOfRangeException
	{
		Location x = new Location(5, 5, original.getSize());
		Location y = new Location(7, 7, original.getSize());
		LocationPair pair = new LocationPair(x, y);
		assertEquals(original.isValidMove(pair), readonly.isValidMove(pair));
	}
	
	@Test
	public void testToString()
	{
		assertEquals(original.toString(), readonly.toString());
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(readonly.equals(readonly));
		
		assertFalse(readonly.equals(null));
		assertFalse(readonly.equals(new Object()));
		assertFalse(readonly.equals(new Board(new BoardSize(8, 10)).getReadOnlyBoard()));
		assertFalse(readonly.equals(new Board(new BoardSize(10, 10)).getReadOnlyBoard()));
	}
}
