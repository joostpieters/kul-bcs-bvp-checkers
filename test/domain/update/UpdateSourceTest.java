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
import domain.update.UpdateSource;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class UpdateSourceTest
{
	@Mock
	private static IObserver observer;
	
	@TestSubject
	private final static UpdateSource source = new RemiseInput(null, null);
	
	@Before
	public void setup()
	{
		source.subscribe(observer);
	}
	
	@After
	public void teardown()
	{
		source.unsubscribe(observer);
		reset(observer);
	}
	
	@Test
	public void testSubsribeUnsubscribe()
	{		
		assertTrue(source.isSubscribed(observer));
		source.unsubscribe(observer);
		assertFalse(source.isSubscribed(observer));
		source.subscribe(observer);
		assertTrue(source.isSubscribed(observer));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSubscribeAlreadySubscribed()
	{
		source.subscribe(observer);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUnsubscribeNotSubscribed()
	{
		source.unsubscribe(new TextualVisualizer());
	}
	
	@Test
	public void testUpdateBoardEnabled()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player performer = Player.White;
		observer.updateBoard(board, performer);
		replay(observer);
		source.emitUpdateBoard(board, performer);
		verify(observer);
	}

	@Test
	public void testUpdateBoardDisabled()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player performer = Player.White;
		replay(observer); //nothing record
		source.disableUpdateObservers();
		source.emitUpdateBoard(board, performer);
		source.enableUpdateObservers();
		verify(observer);
	}
	
	@Test
	public void testAcceptRemise()
	{
		observer.acceptRemise();
		replay(observer);
		source.emitAcceptRemise();
		verify(observer);
	}
	
	@Test
	public void testDeclineRemise()
	{
		observer.declineRemise();
		replay(observer);
		source.emitDeclineRemise();
		verify(observer);
	}
	
	@Test
	public void testError()
	{
		String message = "error";
		Exception ex = new Exception(message);
		observer.error(message, ex);
		replay(observer);
		source.emitError(message, ex);
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
		source.emitExecuteAction(action);
		verify(observer);
	}
	
	@Test
	public void testForcedRemise()
	{
		observer.forcedRemise();
		replay(observer);
		source.emitForcedRemise();
		verify(observer);
	}
	
	@Test
	public void testGameOver()
	{
		Player winner = Player.White;
		observer.gameOver(winner);
		replay(observer);
		source.emitGameOver(winner);
		verify(observer);
	}
	
	@Test
	public void testOutOfMoves()
	{
		Player player = Player.White;
		observer.outOfMoves(player);
		replay(observer);
		source.emitOutOfMoves(player);
		verify(observer);
	}
	
	@Test
	public void testPromotion()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Location location = new Location(1, new BoardSize(10, 10));
		observer.promotion(board, location);
		replay(observer);
		source.emitPromotion(board, location);
		verify(observer);
	}
	
	@Test
	public void testProposeRemise()
	{
		Player proposer = Player.White;
		observer.proposeRemise(proposer);
		replay(observer);
		source.emitProposeRemise(proposer);
		verify(observer);
	}
	
	@Test
	public void testResign()
	{
		Player resignee = Player.White;
		observer.resign(resignee);
		replay(observer);
		source.emitResign(resignee);
		verify(observer);
	}
	
	@Test
	public void testStart()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player starter = Player.White;
		observer.start(board, starter);
		replay(observer);
		source.emitStart(board, starter);
		verify(observer);
	}
	
	@Test
	public void testSwitchPlayer()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		Player switchedIn = Player.White;
		observer.switchPlayer(board, switchedIn);
		replay(observer);
		source.emitSwitchPlayer(board, switchedIn);
		verify(observer);
	}
	
	@Test
	public void testWarning()
	{
		String message = "error";
		observer.warning(message);
		replay(observer);
		source.emitWarning(message);
		verify(observer);
	}
}