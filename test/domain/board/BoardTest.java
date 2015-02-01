package domain.board;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Test;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.location.LocationPair;
import domain.piece.Piece;
import domain.piece.contracts.IPiece;

public class BoardTest
{
	private static IBoard board;
	private static Location freeBlackTarget;
	private static Location freeWhiteTarget;
	private static Location blackTargetOccupiedByWhite;
	private static Location blackTargetOccupiedByBlack;
	
	static
	{
		try
		{
			board = BoardFactory.create(Paths.get("data", "input", "default.txt"));
			freeBlackTarget = board.createLocation(4, 5);
			freeWhiteTarget = board.createLocation(4, 4);
			blackTargetOccupiedByWhite = board.createLocation(9, 0);
			blackTargetOccupiedByBlack = board.createLocation(0, 1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (LocationOutOfRangeException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConstructor() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Board board = new Board(size);
		
		assertEquals(size, board.getSize());
		for(int row=0; row < size.getRows(); row++)
		{
			for(int col=0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				assertTrue(board.isLocationFree(location));
			}
		}
	}
	
	@Test
	public void testCopyConstructor() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Board copy = new Board(board);
		
		assertEquals(size, copy.getSize());
		for(int row=0; row < size.getRows(); row++)
		{
			for(int col=0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				assertEquals(board.getSquare(location), copy.getSquare(location));
			}
		}
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(board.equals(board));
		
		assertFalse(board.equals(null));
		assertFalse(board.equals(new Object()));
		assertFalse(board.equals(new Board(new BoardSize(8, 10))));
		assertFalse(board.equals(new Board(new BoardSize(10, 10))));
	}

	@Test
	public void testDeepClone()
	{
		assertEquals(board, board.getDeepClone());
	}
	
	@Test
	public void testIsLocationFree()
	{
		assertTrue(board.isLocationFree(freeBlackTarget));
		assertTrue(board.isLocationFree(freeWhiteTarget));
		assertFalse(board.isLocationFree(blackTargetOccupiedByWhite));
		assertFalse(board.isLocationFree(blackTargetOccupiedByBlack));
	}
	
	@Test
	public void testIsLocationOccupiedBy()
	{
		assertTrue(board.isLocationOccupiedBy(Player.White, blackTargetOccupiedByWhite));
		assertTrue(board.isLocationOccupiedBy(Player.Black, blackTargetOccupiedByBlack));
		assertFalse(board.isLocationOccupiedBy(Player.Black, blackTargetOccupiedByWhite));
		assertFalse(board.isLocationOccupiedBy(Player.White, blackTargetOccupiedByBlack));
		assertFalse(board.isLocationOccupiedBy(Player.White, freeBlackTarget));
		assertFalse(board.isLocationOccupiedBy(Player.Black, freeBlackTarget));
		assertFalse(board.isLocationOccupiedBy(Player.White, freeWhiteTarget));
		assertFalse(board.isLocationOccupiedBy(Player.Black, freeWhiteTarget));
	}
	
	@Test
	public void testGetPlayerPiecesWhite()
	{
		HashMap<Location, IPiece> pieces = board.getPlayerPieces(Player.White);
		assertEquals(20, pieces.size());
		for(Location loc : pieces.keySet())
		{
			IPiece piece = pieces.get(loc);
			assertTrue(piece.getPlayer().equals(Player.White));
		}
	}
	
	@Test
	public void testGetPlayerPiecesBlack()
	{
		HashMap<Location, IPiece> pieces = board.getPlayerPieces(Player.Black);
		assertEquals(20, pieces.size());
		for(Location loc : pieces.keySet())
		{
			IPiece piece = pieces.get(loc);
			assertTrue(piece.getPlayer().equals(Player.Black));
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testPromotePieceEmptySquare()
	{
		board.promotePiece(freeBlackTarget);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testPromotePieceWrongRow()
	{
		board.promotePiece(blackTargetOccupiedByWhite);
	}
	
	@Test
	public void testPromotePiece()
	{
		IBoard testBoard = board.getDeepClone();
		testBoard.removePiece(blackTargetOccupiedByWhite);
		testBoard.addPiece(blackTargetOccupiedByWhite, new Piece(Player.Black));
		
		assertTrue(testBoard.getSquare(blackTargetOccupiedByWhite).getPiece().canPromote());
		testBoard.promotePiece(blackTargetOccupiedByWhite);
		assertFalse(testBoard.getSquare(blackTargetOccupiedByWhite).getPiece().canPromote());
	}
	
	@Test
	public void testIsValidMoveFromWhite()
	{
		LocationPair pair = new LocationPair(freeWhiteTarget, freeBlackTarget);
		assertTrue(freeWhiteTarget.isWhite());
		assertFalse(board.isValidMove(pair));
	}
	
	@Test
	public void testIsValidMoveToWhite()
	{
		LocationPair pair = new LocationPair(blackTargetOccupiedByWhite, freeWhiteTarget);
		assertTrue(freeWhiteTarget.isWhite());
		assertFalse(board.isValidMove(pair));
	}
	
	@Test
	public void testIsValidMoveEmptyFrom()
	{
		LocationPair pair = new LocationPair(freeBlackTarget, blackTargetOccupiedByBlack);
		assertTrue(board.isLocationFree(freeBlackTarget));
		assertFalse(board.isValidMove(pair));
	}
	
	@Test
	public void testIsValidMoveOccupiedTo()
	{
		LocationPair pair = new LocationPair(blackTargetOccupiedByWhite, blackTargetOccupiedByBlack);
		assertFalse(board.isLocationFree(blackTargetOccupiedByBlack));
		assertFalse(board.isValidMove(pair));
	}
	
	@Test
	public void testIsValidMove()
	{
		LocationPair pair = new LocationPair(blackTargetOccupiedByWhite, freeBlackTarget);
		assertTrue(board.isValidMove(pair));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMovePieceIllegal()
	{
		LocationPair pair = new LocationPair(blackTargetOccupiedByWhite, blackTargetOccupiedByBlack);
		board.movePiece(pair);
	}
	
	@Test
	public void testMovePieceLegal()
	{
		LocationPair pair = new LocationPair(blackTargetOccupiedByWhite, freeBlackTarget);
		IBoard testBoard = board.getDeepClone();
		testBoard.movePiece(pair);
		assertTrue(testBoard.isLocationFree(blackTargetOccupiedByWhite));
		assertFalse(testBoard.isLocationFree(freeBlackTarget));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testAddPieceIllegal()
	{
		board.addPiece(blackTargetOccupiedByBlack, new Piece(Player.White));
	}
	
	@Test
	public void testAddPieceLegal()
	{
		IBoard testBoard = board.getDeepClone();
		IPiece piece = new Piece(Player.White);
		testBoard.addPiece(freeBlackTarget, piece);
		assertEquals(piece, testBoard.getSquare(freeBlackTarget).getPiece());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testRemovePieceIllegal()
	{
		board.removePiece(freeBlackTarget);
	}
	
	@Test
	public void testRemovePieceLegal()
	{
		IBoard testBoard = board.getDeepClone();
		testBoard.removePiece(blackTargetOccupiedByWhite);
		assertTrue(testBoard.isLocationFree(blackTargetOccupiedByWhite));
	}
	
	@Test
	public void testCreateLocation() throws LocationOutOfRangeException
	{
		Location created = board.createLocation(7, 8);
		assertEquals(board.getSize(), created.getBoardSize());
		assertEquals(7, created.getRow());
		assertEquals(8, created.getCol());
	}
	
	@Test
	public void testGetReadOnlyBoard()
	{
		IReadOnlyBoard readonly = new ReadOnlyBoard(board);
		assertEquals(readonly, board.getReadOnlyBoard());
	}
}
