package domain.action;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.ConfigurationManager;
import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Piece;
import domain.update.contracts.IBasicObserver;

@RunWith(EasyMockRunner.class) 
public class AtomicActionCatchTest
{
	@Mock
	private IBasicObserver observer;
	
	@After
	public void tearDown()
	{
		reset(observer);
	}
	
	@Test
	public void testIsValidOnNoFromPiece() throws Exception
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnDistanceTooShort() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnNoCenterPiece() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnFromPieceFromEnemy() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.Black));
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnCenterPieceFromSelf() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		board.addPiece(new Location(7, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnBackwardCatchingNotAllowed() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		boolean defaultSetting = ConfigurationManager.getInstance().getBackwardCatchingAllowed();
		ConfigurationManager.getInstance().setBackwardCatchingAllowed(false);
		assertFalse(action.isValidOn(board, Player.White));
		ConfigurationManager.getInstance().setBackwardCatchingAllowed(defaultSetting);
	}
	
	@Test
	public void testIsValidOnBackwardCatchingNotAllowedButForwardStillOk() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(12, size), new Piece(Player.White));
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(12, 1, size);
		AtomicAction action = new AtomicActionCatch(pair);
		boolean defaultSetting = ConfigurationManager.getInstance().getBackwardCatchingAllowed();
		ConfigurationManager.getInstance().setBackwardCatchingAllowed(false);
		assertTrue(action.isValidOn(board, Player.White));
		ConfigurationManager.getInstance().setBackwardCatchingAllowed(defaultSetting);
	}
	
	@Test
	public void testIsValidOnAllOk() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		assertTrue(action.isValidOn(board, Player.White));
	}

	@Test(expected=IllegalStateException.class)
	public void testExecuteOnInvalidMove() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		action.executeOn(board, Player.White);
	}
	
	@Test
	public void testExecuteOnValidMove() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location from = new Location(1, size);
		Location center = new Location(7, size);
		Location to = new Location(12, size);
		Piece fromPiece = new Piece(Player.White);
		board.addPiece(from, fromPiece);
		board.addPiece(center, new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(from, to);
		AtomicAction action = new AtomicActionCatch(pair);
		action.subscribeBasic(observer);
		
		IBoard expectedBoard = new Board(size);
		expectedBoard.addPiece(to, fromPiece);
		observer.fireExecuteAction(action);
		observer.fireUpdateBoard(expectedBoard.getReadOnlyBoard(), Player.White);
		replay(observer);
		
		action.executeOn(board, Player.White);
		
		assertFalse(board.getSquare(from).hasPiece());
		assertFalse(board.getSquare(center).hasPiece());
		assertTrue(board.getSquare(to).hasPiece());
		verify(observer);
	}

	@Test
	public void testIsCatch() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 12, size);
		AtomicAction action = new AtomicActionCatch(pair);
		
		assertTrue(action.isCatch());
	}
}
