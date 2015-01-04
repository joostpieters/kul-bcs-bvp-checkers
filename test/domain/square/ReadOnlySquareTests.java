package domain.square;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.piece.Piece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

public class ReadOnlySquareTests
{
	private static final Piece whitePiece = new Piece(Player.White);
	private final static ISquare emptyBlack = new SquareBlack();
	private final static ISquare whiteOnBlack = new SquareBlack(whitePiece);

	@Test
	public void testEquals()
	{
		IReadOnlySquare roEmptyBlack = emptyBlack.getReadOnlySquare();
		IReadOnlySquare roWhiteOnBlack = whiteOnBlack.getReadOnlySquare();
		assertFalse(roEmptyBlack.equals(null));
		assertFalse(roEmptyBlack.equals(new Object()));
		assertTrue(roEmptyBlack.equals(roEmptyBlack));
		assertFalse(roEmptyBlack.equals(roWhiteOnBlack));
		assertTrue(roWhiteOnBlack.equals(new ReadOnlySquare(whiteOnBlack)));
	}
	
	@Test
	public void testGetPiece()
	{
		IReadOnlySquare roWhiteOnBlack = whiteOnBlack.getReadOnlySquare();
		assertEquals(whiteOnBlack.getPiece(), roWhiteOnBlack.getPiece());
	}

}
