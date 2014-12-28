package domain.piece;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

public class PieceTests
{
	private static final Piece whitePiece = new Piece(Player.White);
	private static final Piece blackPiece = new Piece(Player.Black);

	@Test
	public void testGetPlayer()
	{
		assertEquals(Player.White, whitePiece.getPlayer());
		assertEquals(Player.Black, blackPiece.getPlayer());
	}
	
	@Test
	public void testConstructor()
	{
		assertEquals(Player.White, new Piece(whitePiece).getPlayer());
		assertEquals(Player.Black, new Piece(blackPiece).getPlayer());
	}
	
	@Test
	public void testGetPieceCode()
	{
		assertEquals('z', blackPiece.getPieceCode());
		assertEquals('w', whitePiece.getPieceCode());
	}
	
	@Test
	public void testProperties()
	{
		assertFalse(whitePiece.canFly());
		assertFalse(whitePiece.canStepBackward());
		assertTrue(whitePiece.canCatchBackward());
		assertTrue(whitePiece.canPromote());
	}
	
	@Test
	public void testDeepClone()
	{
		Piece clone = whitePiece.getDeepClone();
		assertFalse(whitePiece == clone);
		assertEquals(whitePiece, clone);
	}
	
	@Test
	public void testEquals()
	{
		assertFalse(whitePiece.equals(null));
		assertFalse(whitePiece.equals(new Object()));
		assertTrue(whitePiece.equals(whitePiece));
		assertFalse(whitePiece.equals(blackPiece));
	}
}
