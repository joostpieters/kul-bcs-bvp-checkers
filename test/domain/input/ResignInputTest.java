package domain.input;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.game.Game;
import domain.game.contracts.IGame;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class ResignInputTest
{
	@Mock
	private static IObserver observer;
	
	@TestSubject
	private ResignInput input = new ResignInput(new Game(new Board(new BoardSize(10, 10))));
	
	@Test
	public void testProcess()
	{
		input.subscribe(observer);
		
		observer.resign(Player.White);
		replay(observer);
		
		boolean result = input.process();
		assertTrue(result);
		verify(observer);
	}
}
