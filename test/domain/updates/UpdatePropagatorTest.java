package domain.updates;

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
import domain.updates.contracts.IObserver;

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
		observer.updateBoard(board, performer);
		replay(observer);
		source.updateBoard(board, performer);
		verify(observer);
	}

	@Test
	public void testAcceptRemise()
	{
		observer.acceptRemise();
		replay(observer);
		source.acceptRemise();
		verify(observer);
	}
	
	@Test
	public void testDeclineRemise()
	{
		observer.declineRemise();
		replay(observer);
		source.declineRemise();
		verify(observer);
	}
	
	@Test
	public void testError()
	{
		String message = "error";
		Exception ex = new Exception(message);
		observer.error(message, ex);
		replay(observer);
		source.error(message, ex);
		verify(observer);
	}
	
	@Test
	public void testExecuteAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		Location from = new Location(1, size);
		Location to = new Location(6, size);
		DiagonalLocationPair pair = new DiagonalLocationPair(from, to); 
		IAction action = new AtomicActionStep(pair);
		
		observer.executeAction(action);
		replay(observer);
		source.executeAction(action);
		verify(observer);
	}
	
	@Test
	public void testForcedRemise()
	{
		observer.forcedRemise();
		replay(observer);
		source.forcedRemise();
		verify(observer);
	}
	
	@Test
	public void testGameOver()
	{
		Player winner = Player.White;
		observer.gameOver(winner);
		replay(observer);
		source.gameOver(winner);
		verify(observer);
	}
	
	@Test
	public void testOutOfMoves()
	{
		Player player = Player.White;
		observer.outOfMoves(player);
		replay(observer);
		source.outOfMoves(player);
		verify(observer);
	}
	
	@Test
	public void testPromotion()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Location location = new Location(1, new BoardSize(10, 10));
		observer.promotion(board, location);
		replay(observer);
		source.promotion(board, location);
		verify(observer);
	}
	
	@Test
	public void testProposeRemise()
	{
		Player proposer = Player.White;
		observer.proposeRemise(proposer);
		replay(observer);
		source.proposeRemise(proposer);
		verify(observer);
	}
	
	@Test
	public void testResign()
	{
		Player resignee = Player.White;
		observer.resign(resignee);
		replay(observer);
		source.resign(resignee);
		verify(observer);
	}
	
	@Test
	public void testStart()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player starter = Player.White;
		observer.start(board, starter);
		replay(observer);
		source.start(board, starter);
		verify(observer);
	}
	
	@Test
	public void testSwitchPlayer()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player switchedIn = Player.White;
		observer.switchPlayer(board, switchedIn);
		replay(observer);
		source.switchPlayer(board, switchedIn);
		verify(observer);
	}
	
	@Test
	public void testWarning()
	{
		String message = "error";
		observer.warning(message);
		replay(observer);
		source.warning(message);
		verify(observer);
	}
}
