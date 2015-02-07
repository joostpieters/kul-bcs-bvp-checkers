package domain.action;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Piece;

public class CompositeActionTest
{

	@Test
	public void testGetActions() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		List<IAction> subActions = action.getSubActions();
		assertTrue(subActions.get(0).equals(action0));
		assertTrue(subActions.get(1).equals(action1));
	}

	@Test(expected=IllegalStateException.class)
	public void testIsValidOnNoSubActions()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		CompositeAction action = new CompositeAction();
		action.isValidOn(board, Player.White);
	}

	@Test
	public void testIsValidOnOneSubActionInvalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(33, size), new Piece(Player.White));
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(28, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		assertFalse(action.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOn() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(33, size), new Piece(Player.White));
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		assertTrue(action.isValidOn(board, Player.White));
	}

	@Test(expected=IllegalStateException.class)
	public void testExecuteOnInvalidAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(33, size), new Piece(Player.White));
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(28, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		action.executeOn(board, Player.White);
	}
	
	@Test
	public void testExecuteOnValidAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(33, size), new Piece(Player.White));
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		action.executeOn(board, Player.White);
	}

	@Test
	public void testGetFrom() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		assertEquals(step0.getFrom(), action.getFrom());
	}

	@Test
	public void testIsCatchAllSubActionsAreMoves() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 23, size);
		AtomicActionStep action0 = new AtomicActionStep(step0);
		AtomicActionStep action1 = new AtomicActionStep(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		assertFalse(action.isCatch());
	}
	
	@Test
	public void testIsCatchSomeSubActionsAreCatches() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 18, size);
		AtomicAction action0 = new AtomicActionStep(step0);
		AtomicAction action1 = new AtomicActionCatch(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		
		assertTrue(action.isCatch());
	}
	
	@Test
	public void testEquals() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair step0 = new DiagonalLocationPair(33, 29, size);
		DiagonalLocationPair step1 = new DiagonalLocationPair(29, 18, size);
		AtomicAction action0 = new AtomicActionStep(step0);
		AtomicAction action1 = new AtomicActionCatch(step1);
		CompositeAction action = new CompositeAction(action0, action1);
		CompositeAction clone = new CompositeAction(action0, action1);
		CompositeAction less = new CompositeAction(action0);
		CompositeAction reversed = new CompositeAction(action1, action0);
		
		assertFalse(action.equals(null));
		assertFalse(action.equals(new Object()));
		assertTrue(action.equals(action));
		assertTrue(action.equals(clone));
		assertFalse(action.equals(less));
		assertFalse(action.equals(reversed));
	}
}
