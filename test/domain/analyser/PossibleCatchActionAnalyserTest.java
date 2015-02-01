package domain.analyser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import common.Player;
import domain.action.request.CatchActionRequest;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Dame;
import domain.piece.Piece;

public class PossibleCatchActionAnalyserTest
{

	@Test
	public void testFindAtomicCatch() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 17);
		CatchActionRequest expected2 = new CatchActionRequest(28, 19);
		CatchActionRequest expected3 = new CatchActionRequest(28, 37);
		CatchActionRequest expected4 = new CatchActionRequest(28, 39);
		
		assertEquals(4, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected3));
		assertTrue(requests.contains(expected4));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFindNoPiece() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(1, size);
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White, location);
		
		assertEquals(0, requests.size());
	}

	@Test
	public void testFindSingleFlyCatch() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 10);
		CatchActionRequest expected2 = new CatchActionRequest(28, 5);
		
		assertEquals(2, results.size());
		assertTrue(results.contains(expected1));
		assertTrue(results.contains(expected2));
	}
	
	@Test
	public void testFindMultiFlyCatch() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(19, size), new Piece(Player.Black));
		board.addPiece(new Location(20, size), new Piece(Player.Black));
		board.addPiece(new Location(9, size), new Piece(Player.Black));
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		
		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 14);
		CatchActionRequest expected2 = new CatchActionRequest(28, 14, 3);
		CatchActionRequest expected3 = new CatchActionRequest(28, 14, 3, 17);
		CatchActionRequest expected4 = new CatchActionRequest(28, 14, 3, 21);
		CatchActionRequest expected5 = new CatchActionRequest(28, 14, 3, 26);
		CatchActionRequest expected6 = new CatchActionRequest(28, 14, 3, 25);
		CatchActionRequest expected7 = new CatchActionRequest(28, 14, 25);
		CatchActionRequest expected8 = new CatchActionRequest(28, 14, 25, 3);
		CatchActionRequest expected9 = new CatchActionRequest(28, 14, 25, 3, 17);
		CatchActionRequest expected10 = new CatchActionRequest(28, 14, 25, 3, 21);
		CatchActionRequest expected11 = new CatchActionRequest(28, 14, 25, 3, 26);
		CatchActionRequest expected12 = new CatchActionRequest(28, 10);
		CatchActionRequest expected13 = new CatchActionRequest(28, 5);
		
		assertEquals(13, results.size());
		assertTrue(results.contains(expected1));
		assertTrue(results.contains(expected2));
		assertTrue(results.contains(expected3));
		assertTrue(results.contains(expected4));
		assertTrue(results.contains(expected5));
		assertTrue(results.contains(expected6));
		assertTrue(results.contains(expected7));
		assertTrue(results.contains(expected8));
		assertTrue(results.contains(expected9));
		assertTrue(results.contains(expected10));
		assertTrue(results.contains(expected11));
		assertTrue(results.contains(expected12));
		assertTrue(results.contains(expected13));
	}

// does any invalid move ever get suggested?
//	@Test
//	public void testFindInvalid() throws LocationOutOfRangeException
//	{
//		BoardSize size = new BoardSize(10, 10);
//		IBoard board = new Board(size);
//		Location location = new Location(28, size);
//		board.addPiece(location, new Dame(Player.White));
//		
//		PossibleCatchActionAnalyser analyser = new PossibleCatchActionAnalyser(board);
//		
//		List<CatchActionRequest> results = analyser.find(Player.White, location);
//		
//		assertEquals(0, results.size());
//	}
}
