package domain.common;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;

public class PlayerTest
{

	@Test
	public void testOpponent()
	{
		assertEquals(Player.Black, Player.White.getOpponent());
		assertEquals(Player.White, Player.Black.getOpponent());
		
		for(Player player : Player.values())
		{
			assertEquals(player, player.getOpponent().getOpponent());
		}
	}
	
	@Test
	public void testToString()
	{
		assertNotEquals(Player.White.toString(), Player.Black.toString());
	}

}
