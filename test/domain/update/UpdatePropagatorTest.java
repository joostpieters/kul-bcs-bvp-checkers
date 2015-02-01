package domain.update;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.action.AtomicActionStep;
import domain.action.CompositeAction;
import domain.action.contracts.IAction;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.update.UpdatePropagator;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class UpdatePropagatorTest
{
	@Mock
	private static IObserver observer;
	
	@TestSubject
	private final static UpdatePropagator source = new CompositeAction();
	
	@Before
	public void setup()
	{
		source.subscribe(observer);
	}
	
	@After
	public void teardown()
	{
		source.unsubscribe(observer);
	}
	
	@Test
	public void testUpdateBoard()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player performer = Player.White;
		observer.fireUpdateBoard(board, performer);
		replay(observer);
		source.fireUpdateBoard(board, performer);
		verify(observer);
	}

	@Test
	public void testAcceptRemise()
	{
		observer.fireAcceptRemise();
		replay(observer);
		source.fireAcceptRemise();
		verify(observer);
	}
	
	@Test
	public void testDeclineRemise()
	{
		observer.fireDeclineRemise();
		replay(observer);
		source.fireDeclineRemise();
		verify(observer);
	}
	
	@Test
	public void testError()
	{
		String message = "error";
		Exception ex = new Exception(message);
		observer.fireError(message, ex);
		replay(observer);
		source.fireError(message, ex);
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
		source.fireExecuteAction(action);
		verify(observer);
	}
	
	@Test
	public void testForcedRemise()
	{
		observer.fireForcedRemise();
		replay(observer);
		source.fireForcedRemise();
		verify(observer);
	}
	
	@Test
	public void testGameOver()
	{
		Player winner = Player.White;
		observer.fireGameOver(winner);
		replay(observer);
		source.fireGameOver(winner);
		verify(observer);
	}
	
	@Test
	public void testOutOfMoves()
	{
		Player player = Player.White;
		observer.fireOutOfMoves(player);
		replay(observer);
		source.fireOutOfMoves(player);
		verify(observer);
	}
	
	@Test
	public void testPromotion() throws LocationOutOfRangeException
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Location location = new Location(1, new BoardSize(10, 10));
		observer.firePromotion(board, location);
		replay(observer);
		source.firePromotion(board, location);
		verify(observer);
	}
	
	@Test
	public void testProposeRemise()
	{
		Player proposer = Player.White;
		observer.fireProposeRemise(proposer);
		replay(observer);
		source.fireProposeRemise(proposer);
		verify(observer);
	}
	
	@Test
	public void testResign()
	{
		Player resignee = Player.White;
		observer.fireResign(resignee);
		replay(observer);
		source.fireResign(resignee);
		verify(observer);
	}
	
	@Test
	public void testStart()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player starter = Player.White;
		observer.fireStart(board, starter);
		replay(observer);
		source.fireStart(board, starter);
		verify(observer);
	}
	
	@Test
	public void testSwitchPlayer()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player switchedIn = Player.White;
		observer.fireSwitchPlayer(board, switchedIn);
		replay(observer);
		source.fireSwitchPlayer(board, switchedIn);
		verify(observer);
	}
	
	@Test
	public void testWarning()
	{
		String message = "error";
		observer.fireWarning(message);
		replay(observer);
		source.fireWarning(message);
		verify(observer);
	}
}
