package domain.square;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.piece.Piece;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

public class SquareBlackTest
{
	private static final Piece blackPiece = new Piece(Player.Black);
	private static final Piece whitePiece = new Piece(Player.White);
	private final static ISquare emptyBlack = new SquareBlack();
	private final static ISquare whiteOnBlack = new SquareBlack(whitePiece);
	private final static ISquare blackOnBlack = new SquareBlack(blackPiece);
	
	@Test
	public void testConstructor()
	{
		assertFalse(emptyBlack.hasPiece());
		assertTrue(whiteOnBlack.hasPiece());
		assertTrue(blackOnBlack.hasPiece());
		
		assertEquals(whitePiece, whiteOnBlack.getPiece());
		assertEquals(blackPiece, blackOnBlack.getPiece());
	}
	
	@Test
	public void testEquals()
	{
		assertFalse(emptyBlack.equals(null));
		assertFalse(emptyBlack.equals(new Object()));
		assertFalse(emptyBlack.equals(whiteOnBlack));
		assertTrue(emptyBlack.equals(emptyBlack));
		assertTrue(emptyBlack.equals(new SquareBlack()));
		assertTrue(whiteOnBlack.equals(new SquareBlack(new Piece(Player.White))));
	}
	
	@Test
	public void testClearPiece()
	{
		ISquare square = new SquareBlack(whitePiece);
		square.clearPiece();
		assertFalse(square.hasPiece());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetPieceBlackEmpty()
	{
		emptyBlack.getPiece();
	}
	
	@Test
	public void testGetReadOnlySquare()
	{
		ReadOnlySquare roBlack = new ReadOnlySquare(emptyBlack);
		IReadOnlySquare readOnlyBlack = emptyBlack.getReadOnlySquare();
		assertEquals(roBlack, readOnlyBlack);
	}

}
