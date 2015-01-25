package domain.square;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.piece.Piece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

public class SquareWhiteTest
{
	private final static ISquare emptyWhite = new SquareWhite();
	
	@Test
	public void testConstructor()
	{
		assertFalse(emptyWhite.hasPiece());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetPiece()
	{
		emptyWhite.getPiece();
	}

	@Test(expected=IllegalStateException.class)
	public void testSetPiece()
	{
		emptyWhite.setPiece(new Piece(Player.Black));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testClearPiece()
	{
		emptyWhite.clearPiece();
	}
	
	@Test
	public void testGetReadOnlySquare()
	{
		ReadOnlySquare roWhite = new ReadOnlySquare(emptyWhite);
		IReadOnlySquare readOnlyWhite = emptyWhite.getReadOnlySquare();
		assertEquals(roWhite, readOnlyWhite);
	}
	
	@Test
	public void testEquals()
	{
		assertFalse(emptyWhite.equals(null));
		assertFalse(emptyWhite.equals(new Object()));
		assertTrue(emptyWhite.equals(emptyWhite));
		assertTrue(emptyWhite.equals(new SquareWhite()));
	}
}
