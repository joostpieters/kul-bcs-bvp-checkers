package domain.game;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class ReadOnlyGameTest
{
	private final static IBoard board = new Board(new BoardSize(10, 10));
	
	@Test
	public void testEquals()
	{
		Game game = new Game(board);
		ReadOnlyGame readOnly = new ReadOnlyGame(game);
		ReadOnlyGame readOnlyCopy = new ReadOnlyGame(game);
		
		assertFalse(readOnly.equals(null));
		assertFalse(readOnly.equals(new Object()));
		assertTrue(readOnly.equals(readOnly));
		assertTrue(readOnly.equals(readOnlyCopy));
	}
	
	@Test
	public void testEqualsDifferentCurrentPlayer()
	{
		Game game = new Game(board);
		Game copy = new Game(game);
		copy.switchCurrentPlayer();
		ReadOnlyGame readOnly = new ReadOnlyGame(game);
		ReadOnlyGame readOnlyCopy = new ReadOnlyGame(copy);
		assertFalse(readOnly.equals(readOnlyCopy));
	}
	
	@Test
	public void testEqualsDifferentGameState()
	{
		Game game = new Game(board);
		Game copy = new Game(game);
		copy.remise();
		ReadOnlyGame readOnly = new ReadOnlyGame(game);
		ReadOnlyGame readOnlyCopy = new ReadOnlyGame(copy);
		assertFalse(readOnly.equals(readOnlyCopy));
	}
	
	@Test
	public void testEqualsDifferentWinners()
	{
		Game game = new Game(board);
		
		Game copyWinnerWhite = new Game(game);
		Game copyWinnerBlack = new Game(game);
		copyWinnerWhite.gameOver(Player.White);
		copyWinnerBlack.gameOver(Player.Black);
		ReadOnlyGame readOnlyWhite = new ReadOnlyGame(copyWinnerWhite);
		ReadOnlyGame readOnlyBlack = new ReadOnlyGame(copyWinnerBlack);
		assertFalse(readOnlyWhite.equals(readOnlyBlack));
	}
	
	@Test
	public void testEqualsSameWinners()
	{
		Game game = new Game(board);
		
		Game copy1 = new Game(game);
		Game copy2 = new Game(game);
		copy1.gameOver(Player.White);
		copy2.gameOver(Player.White);
		ReadOnlyGame readOnly1 = new ReadOnlyGame(copy1);
		ReadOnlyGame readOnly2 = new ReadOnlyGame(copy2);
		assertTrue(readOnly1.equals(readOnly2));
	}
	
	@Test
	public void testEqualsDifferentBoard()
	{
		Game game = new Game(board);
		Game game2 = new Game(new Board(new BoardSize(8, 8)));
		ReadOnlyGame readOnly = new ReadOnlyGame(game);
		ReadOnlyGame readOnly2 = new ReadOnlyGame(game2);
		assertFalse(readOnly.equals(readOnly2));
	}
	
	@Test
	public void testGetReadOnlyGame()
	{
		IGame game = new Game(board);
		
		IReadOnlyGame readonly = new ReadOnlyGame(game);
		IReadOnlyGame readonly2 = readonly.getReadOnlyGame();
		assertEquals(readonly, readonly2);
	}
	
	@Test
	public void testIsOver()
	{
		IGame game = new Game(board);
		IReadOnlyGame readonly = game.getReadOnlyGame();
		assertFalse(readonly.isOver());
		game.remise();
		assertTrue(readonly.isOver());
	}
}
