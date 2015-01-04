package domain.game;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class GameTest
{
	private final static IBoard board = new Board(new BoardSize(10, 10));
	
	@Test
	public void testGetBoard()
	{
		IGame game = new Game(board);
		assertEquals(board, game.getBoard());
	}
	
	@Test
	public void testCurrentPlayer()
	{
		IGame game = new Game(board);
		
		assertEquals(Player.White, game.getCurrentPlayer());
		game.switchCurrentPlayer();
		assertEquals(Player.Black, game.getCurrentPlayer());
	}
	
	@Test
	public void testGameOver()
	{
		IGame game = new Game(board);
		
		assertFalse(game.isOver());
		assertEquals(GameState.Ongoing, game.getGameState());
		game.gameOver(Player.White);
		assertEquals(Player.White, game.getWinner());
		assertTrue(game.isOver());
		assertEquals(GameState.Finished, game.getGameState());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGameOverGameOver()
	{
		IGame game = new Game(board);
		game.remise();
		game.gameOver(Player.White);
	}
	
	@Test
	public void testRemise()
	{
		IGame game = new Game(board);
		
		assertFalse(game.isOver());
		assertEquals(GameState.Ongoing, game.getGameState());
		game.remise();
		assertTrue(game.isOver());
		assertEquals(GameState.Remise, game.getGameState());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testRemiseGameOver()
	{
		IGame game = new Game(board);
		game.remise();
		game.remise();
	}
	
	@Test
	public void testGetReadOnlyGame()
	{
		IGame game = new Game(board);
		
		IReadOnlyGame readonly = new ReadOnlyGame(game);
		assertEquals(readonly, game.getReadOnlyGame());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetWinnerOngoing()
	{
		IGame game = new Game(board);
		game.getWinner();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetWinnerRemise()
	{
		IGame game = new Game(board);
		game.remise();
		game.getWinner();
	}
	
	@Test
	public void testGetWinner()
	{
		IGame game = new Game(board);
		game.gameOver(Player.Black);
		assertEquals(Player.Black, game.getWinner());
	}
	
	@Test
	public void testEquals()
	{
		Game game = new Game(board);
		Game copy = new Game(game);
		
		assertFalse(game.equals(null));
		assertFalse(game.equals(new Object()));
		assertTrue(game.equals(game));
		assertTrue(game.equals(copy));
	}
	
	@Test
	public void testEqualsDifferentCurrentPlayer()
	{
		Game game = new Game(board);
		Game copy = new Game(game);
		copy.switchCurrentPlayer();
		assertFalse(game.equals(copy));
	}
	
	@Test
	public void testEqualsDifferentGameState()
	{
		Game game = new Game(board);
		Game copy = new Game(game);
		copy.remise();
		assertFalse(game.equals(copy));
	}
	
	@Test
	public void testEqualsDifferentWinners()
	{
		Game game = new Game(board);
		
		Game copyWinnerWhite = new Game(game);
		copyWinnerWhite.gameOver(Player.White);
		Game copyWinnerBlack = new Game(game);
		copyWinnerBlack.gameOver(Player.Black);
		assertFalse(copyWinnerBlack.equals(copyWinnerWhite));
	}
	
	@Test
	public void testEqualsDifferentBoard()
	{
		Game game = new Game(board);
		Game game2 = new Game(new Board(new BoardSize(8, 8)));
		assertFalse(game.equals(game2));
	}
}
