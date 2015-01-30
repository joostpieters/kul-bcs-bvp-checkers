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
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.Dame;
import domain.piece.Piece;

public class CompositeActionFlyTest
{

	@Test
	public void testIsValidOnInvalidCompositeAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		IReadOnlyBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFly fly = new CompositeActionFly(pair);
		
		assertFalse(fly.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnSourcePieceCannotFly()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Piece(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFly fly = new CompositeActionFly(pair);
		
		assertFalse(fly.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOn()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Dame(Player.White));
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFly fly = new CompositeActionFly(pair);
		
		assertTrue(fly.isValidOn(board, Player.White));
	}

	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyDistanceTooShort()
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		new CompositeActionFly(pair);
	}
	
	@Test
	public void testCompositeActionFly()
	{
		IBoardSize size = new BoardSize(10, 10);
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFly fly = new CompositeActionFly(pair);
		
		List<IAction> subActions = fly.getActions();
		assertEquals(3, subActions.size());
		for(IAction subAction : subActions)
		{
			assertTrue(subAction instanceof AtomicActionStep);
		}
	}
}
