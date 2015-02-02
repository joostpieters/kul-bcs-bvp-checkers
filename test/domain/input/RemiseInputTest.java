package domain.input;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ui.contracts.IUserInterface;
import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.game.Game;
import domain.game.contracts.IGame;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class RemiseInputTest
{
	@Mock
	private static IObserver observer;
	
	@After
	public void tearDown()
	{
		reset(observer);
	}
	
	private IUserInterface getYesNoUI(boolean yesno)
	{
		return new IUserInterface()
		{
			@Override
			public boolean askYesNo(String message)
			{
				return yesno;
			}
			
			@Override
			public String askInput(String message)
			{
				throw new NotImplementedException();
			}
			
			@Override
			public String askActionInput(Player player)
			{
				throw new NotImplementedException();
			}
		};
	}
	
	@Test
	public void testProcessAcceptRemise()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		RemiseInput input = new RemiseInput(game, getYesNoUI(true));
		input.subscribe(observer);
		
		observer.fireProposeRemise(Player.White);
		observer.fireAcceptRemise();
		replay(observer);
		
		boolean result = input.process();
		assertTrue(result);
		verify(observer);
	}
	
	@Test
	public void testProcessDeclineRemise()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		IGame game = new Game(board);
		RemiseInput input = new RemiseInput(game, getYesNoUI(false));
		input.subscribe(observer);
		
		observer.fireProposeRemise(Player.White);
		observer.fireDeclineRemise();
		replay(observer);
		
		boolean result = input.process();
		assertFalse(result);
		verify(observer);
	}
}
