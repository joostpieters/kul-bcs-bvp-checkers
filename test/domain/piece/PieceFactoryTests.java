package domain.piece;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

public class PieceFactoryTests
{
	@Test
	public void testCreate()
	{
		assertEquals(new Piece(Player.Black), PieceFactory.create('z'));
		assertEquals(new Dame(Player.Black), PieceFactory.create('Z'));
		assertEquals(new Piece(Player.White), PieceFactory.create('w'));
		assertEquals(new Dame(Player.White), PieceFactory.create('W'));
		assertEquals(null, PieceFactory.create('o'));
		assertEquals(null, PieceFactory.create('O'));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateUnknownCode()
	{
		PieceFactory.create('x');
	}
}
