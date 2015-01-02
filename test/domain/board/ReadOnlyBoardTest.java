package domain.board;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
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
	}
	
	@Test
	public void testGetBoardSize()
	{
		assertEquals(original.getSize(), readonly.getSize());
	}

	@Test
	public void testCreateLocation()
	{
		assertEquals(	original.createLocation(0, 0), 
						readonly.createLocation(0, 0));
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
	public void testGetSquare()
	{
		Location location = original.createLocation(0, 0);
		assertEquals(original.getSquare(location).getReadOnlySquare(), readonly.getSquare(location));
	}
	
	@Test
	public void testIsLocationFree()
	{
		Location target = original.createLocation(5, 5);
		assertEquals(original.isLocationFree(target), readonly.isLocationFree(target));
	}
	
	@Test
	public void testIsLocationOccupiedBy()
	{
		Location target = original.createLocation(5, 5);
		assertEquals(	original.isLocationOccupiedBy(Player.White, target), 
						readonly.isLocationOccupiedBy(Player.White, target));
		assertEquals(	original.isLocationOccupiedBy(Player.Black, target), 
						readonly.isLocationOccupiedBy(Player.Black, target));
	}
	
	@Test
	public void testIsValidMove()
	{
		Location x = original.createLocation(5, 5);
		Location y = original.createLocation(7, 7);
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
