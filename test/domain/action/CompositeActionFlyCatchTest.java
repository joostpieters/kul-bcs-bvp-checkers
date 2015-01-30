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
import domain.piece.Dame;
import domain.piece.Piece;

public class CompositeActionFlyCatchTest
{
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchDistanceTooShort()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		new CompositeActionFlyCatch(board, Player.White, pair);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchNoOpponentPieceInBetween()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 18, size);
		new CompositeActionFlyCatch(board, Player.White, pair);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchMultipleOpponentPiecesInBetween()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 18, size);
		new CompositeActionFlyCatch(board, Player.White, pair);
	}
	
	@Test
	public void testCompositeActionFlyCatchSubAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 23, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(board, Player.White, pair);
		
		List<IAction> subActions = flyCatch.getActions();
		assertEquals(new AtomicActionStep(new DiagonalLocationPair(1, 7, size)), subActions.get(0));
		assertEquals(new AtomicActionCatch(new DiagonalLocationPair(7, 18, size)), subActions.get(1));
		assertEquals(new AtomicActionStep(new DiagonalLocationPair(18, 23, size)), subActions.get(2));
	}
	
	@Test
	public void testIsValidOnInvalidCompositeAction()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(board, Player.White, pair);
		
		assertFalse(flyCatch.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnSourcePieceCannotFly()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Piece(Player.White));
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(board, Player.White, pair);
		
		assertFalse(flyCatch.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOn()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Dame(Player.White));
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(board, Player.White, pair);
		
		assertTrue(flyCatch.isValidOn(board, Player.White));
	}
}
