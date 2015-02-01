package domain.action;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

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
import domain.piece.Dame;
import domain.piece.Piece;

public class CompositeActionFlyCatchTest
{
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchDistanceTooShort() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 7, size);
		new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchNoOpponentPieceInBetween() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 18, size);
		new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCompositeActionFlyCatchMultipleOpponentPiecesInBetween() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 18, size);
		new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
	}
	
	@Test
	public void testCompositeActionFlyCatchSubAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(1, 23, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
		
		List<IAction> subActions = flyCatch.getActions();
		assertEquals(new AtomicActionStep(new DiagonalLocationPair(1, 7, size)), subActions.get(0));
		assertEquals(new AtomicActionCatch(new DiagonalLocationPair(7, 18, size)), subActions.get(1));
		assertEquals(new AtomicActionStep(new DiagonalLocationPair(18, 23, size)), subActions.get(2));
	}
	
	@Test
	public void testIsValidOnInvalidCompositeAction() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
		
		assertFalse(flyCatch.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOnSourcePieceCannotFly() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Piece(Player.White));
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
		
		assertFalse(flyCatch.isValidOn(board, Player.White));
	}
	
	@Test
	public void testIsValidOn() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(48, size), new Dame(Player.White));
		board.addPiece(new Location(43, size), new Piece(Player.Black));
		Set<Location> opponentPieceLocations = board.getPlayerPieces(Player.Black).keySet();
		DiagonalLocationPair pair = new DiagonalLocationPair(48, 34, size);
		CompositeActionFlyCatch flyCatch = new CompositeActionFlyCatch(opponentPieceLocations, Player.White, pair);
		
		assertTrue(flyCatch.isValidOn(board, Player.White));
	}
}
