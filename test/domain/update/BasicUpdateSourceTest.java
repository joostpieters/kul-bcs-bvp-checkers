package domain.update;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ui.TextualVisualizer;
import common.Player;
import domain.action.AtomicActionStep;
import domain.action.contracts.IAction;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.input.RemiseInput;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.update.BasicUpdateSource;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class BasicUpdateSourceTest
{
	@Mock
	private static IObserver observer;
	
	@TestSubject
	private final static BasicUpdateSource source = new RemiseInput(null, null);
	
	@Before
	public void setup()
	{
		source.subscribeBasic(observer);
	}
	
	@After
	public void teardown()
	{
		source.unsubscribeBasic(observer);
		reset(observer);
	}
	
	@Test
	public void testSubscribeUnsubscribeBasic()
	{
		assertTrue(source.isSubscribedBasic(observer));
		source.unsubscribeBasic(observer);
		assertFalse(source.isSubscribedBasic(observer));
		source.subscribeBasic(observer);
		assertTrue(source.isSubscribedBasic(observer));
	}
	
	@Test //(expected=IllegalStateException.class)
	public void testSubscribeAlreadySubscribed()
	{
		source.subscribeBasic(observer);
	}
	
	@Test //(expected=IllegalStateException.class)
	public void testUnsubscribeNotSubscribed()
	{
		source.unsubscribeBasic(new TextualVisualizer());
	}
	
	@Test
	public void testDisableEnable()
	{
		assertFalse(source.isDisabled());
		source.disableUpdateObservers();
		assertTrue(source.isDisabled());
		source.enableUpdateObservers();
		assertFalse(source.isDisabled());
	}
	
	@Test
	public void testUpdateBoardEnabled()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player performer = Player.White;
		observer.fireUpdateBoard(board, performer);
		replay(observer);
		source.emitUpdateBoard(board, performer);
		verify(observer);
	}

	@Test
	public void testUpdateBoardDisabled()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player performer = Player.White;
		replay(observer); //nothing recorded
		source.disableUpdateObservers();
		source.emitUpdateBoard(board, performer);
		source.enableUpdateObservers();
		verify(observer);
	}
	
	@Test
	public void testSwitchPlayer()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player switchedIn = Player.White;
		observer.fireSwitchPlayer(board, switchedIn);
		replay(observer);
		source.emitSwitchPlayer(board, switchedIn);
		verify(observer);
	}
	
	@Test
	public void testExecuteAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Location from = new Location(1, size);
		Location to = new Location(6, size);
		DiagonalLocationPair pair = new DiagonalLocationPair(from, to); 
		IAction action = new AtomicActionStep(pair);
		observer.fireExecuteAction(action);
		replay(observer);
		source.emitExecuteAction(action);
		verify(observer);
	}
}
