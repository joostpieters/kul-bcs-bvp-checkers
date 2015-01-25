package domain.piece;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Configs;
import common.Player;

public class DameTest
{
	private static final Dame whiteDame = new Dame(Player.White);
	private static final Dame blackDame = new Dame(Player.Black);

	@Test
	public void testGetPlayer()
	{
		assertEquals(Player.White, whiteDame.getPlayer());
		assertEquals(Player.Black, blackDame.getPlayer());
	}
	
	@Test
	public void testConstructor()
	{
		assertEquals(Player.White, new Dame(whiteDame).getPlayer());
		assertEquals(Player.Black, new Dame(blackDame).getPlayer());
	}
	
	@Test
	public void testGetPieceCode()
	{
		assertEquals('Z', blackDame.getPieceCode());
		assertEquals('W', whiteDame.getPieceCode());
	}
	
	@Test
	public void testProperties()
	{
		assertEquals(Configs.FlyingDame, whiteDame.canFly());
		assertTrue(whiteDame.canStepBackward());
		assertTrue(whiteDame.canCatchBackward());
		assertFalse(whiteDame.canPromote());
	}
	
	@Test
	public void testDeepClone()
	{
		Dame clone = whiteDame.getDeepClone();
		assertFalse(whiteDame == clone);
		assertEquals(whiteDame, clone);
	}
	
	@Test
	public void testEquals()
	{
		assertFalse(whiteDame.equals(null));
		assertFalse(whiteDame.equals(new Object()));
		assertTrue(whiteDame.equals(whiteDame));
		assertFalse(whiteDame.equals(blackDame));
	}
}
