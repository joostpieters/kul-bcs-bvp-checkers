package domain.action;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Dame;
import domain.piece.Piece;
import domain.update.contracts.IBasicObserver;

@RunWith(EasyMockRunner.class) 
public class AtomicActionStepTest
{
	@Mock
	private IBasicObserver observer;
	
	@After
	public void tearDown()
	{
		reset(observer);
	}
	
	@Test
	public void testIsValidOnNoFromPiece() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(7, 1, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnDistanceTooLong() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(12, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(12, 1, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnFromPieceFromEnemy() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(7, 1, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnBackwardStepPieceNotAllowed() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnBackwardStepDameAllowed() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Dame(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertTrue(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnAllOk() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(7, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(7, 1, size);
		AtomicAction action = new AtomicActionStep(pair);
		assertTrue(action.isValidOn(board, Player.White));
	}

	@Test(expected=IllegalStateException.class)
	public void testExecuteOnInvalidMove() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(7, 1, size);
		AtomicAction action = new AtomicActionStep(pair);
		action.executeOn(board, Player.White);
	}
	
	@Test
	public void testExecuteOnValidMove() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location from = new Location(7, size);
		Location to = new Location(1, size);
		Piece fromPiece = new Piece(Player.White);
		board.addPiece(from, fromPiece);
		DiagonalLocationPair pair = new DiagonalLocationPair(from, to);
		AtomicAction action = new AtomicActionStep(pair);
		action.subscribeBasic(observer);
		
		IBoard expectedBoard = new Board(size);
		expectedBoard.addPiece(to, fromPiece);
		observer.fireExecuteAction(action);
		observer.fireUpdateBoard(expectedBoard.getReadOnlyBoard(), Player.White);
		replay(observer);
		
		action.executeOn(board, Player.White);
		assertFalse(board.getSquare(from).hasPiece());
		assertTrue(board.getSquare(to).hasPiece());
		verify(observer);
	}

	@Test
	public void testIsCatch() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		AtomicAction action = new AtomicActionStep(pair);
		
		assertFalse(action.isCatch());
	}

}
