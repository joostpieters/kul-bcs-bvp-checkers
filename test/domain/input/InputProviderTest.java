package domain.input;

import static org.junit.Assert.*;

import org.junit.Test;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import common.Player;
import domain.action.LegalActionChecker;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.game.Game;
import domain.game.contracts.IGame;
import domain.input.contracts.IInput;

public class InputProviderTest
{
	@Test
	public void testResign()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		String resign = LocalizationManager.getString("resignInput");
		InputProvider provider = new InputProvider(getUI(resign), legalActionChecker, game);
		
		IInput input = provider.askInput();
		assertTrue(input instanceof ResignInput);
		
		provider.close();
	}
	
	@Test
	public void testRemise()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		String remise = LocalizationManager.getString("remiseInput");
		InputProvider provider = new InputProvider(getUI(remise), legalActionChecker, game);
		
		IInput input = provider.askInput();
		assertTrue(input instanceof RemiseInput);
		
		provider.close();
	}
	
	@Test
	public void testAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		InputProvider provider = new InputProvider(getUI("notRemiseOrResign"), legalActionChecker, game);
		
		IInput input = provider.askInput();
		assertTrue(input instanceof ActionInput);
		
		provider.close();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testClosedProvider()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		InputProvider provider = new InputProvider(getUI(""), legalActionChecker, game);

		provider.close();
		provider.askInput();
	}

	private IUserInterface getUI(String input)
	{
		return new IUserInterface()
		{
			@Override
			public void close()
			{
				
			}
			
			@Override
			public boolean askYesNo(String message)
			{
				throw new NotImplementedException();
			}
			
			@Override
			public String askInput(String message)
			{
				throw new NotImplementedException();
			}
			
			@Override
			public String askActionInput(Player player)
			{
				return input;
			}
		};
	}
}
