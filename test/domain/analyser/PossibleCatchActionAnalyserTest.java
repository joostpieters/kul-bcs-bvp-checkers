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
		
		Location from = new Location(28, size);
		Location a = new Location(17, size);
		Location b = new Location(19, size);
		Location c = new Location(37, size);
		Location d = new Location(39, size);
		CatchActionRequest expected1 = new CatchActionRequest(Player.White, from, a);
		CatchActionRequest expected2 = new CatchActionRequest(Player.White, from, b);
		CatchActionRequest expected3 = new CatchActionRequest(Player.White, from, c);
		CatchActionRequest expected4 = new CatchActionRequest(Player.White, from, d);
		
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
		
		Location from = new Location(28, size);
		Location a = new Location(5, size);
		Location b = new Location(10, size);
		CatchActionRequest expected1 = new CatchActionRequest(Player.White, from, a);
		CatchActionRequest expected2 = new CatchActionRequest(Player.White, from, b);
		
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
		
		Location from = new Location(28, size);
		Location l14 = new Location(14, size);
		Location l03 = new Location(3, size);
		Location l17 = new Location(17, size);
		Location l21 = new Location(21, size);
		Location l26 = new Location(26, size);
		Location l25 = new Location(25, size);
		Location l10 = new Location(10, size);
		Location l05 = new Location(5, size);
		
		CatchActionRequest expected1  = new CatchActionRequest(Player.White, from, l14);
		CatchActionRequest expected2  = new CatchActionRequest(Player.White, from, l14, l03);
		CatchActionRequest expected3  = new CatchActionRequest(Player.White, from, l14, l03, l17);
		CatchActionRequest expected4  = new CatchActionRequest(Player.White, from, l14, l03, l21);
		CatchActionRequest expected5  = new CatchActionRequest(Player.White, from, l14, l03, l26);
		CatchActionRequest expected6  = new CatchActionRequest(Player.White, from, l14, l03, l25);
		CatchActionRequest expected7  = new CatchActionRequest(Player.White, from, l14, l25);
		CatchActionRequest expected8  = new CatchActionRequest(Player.White, from, l14, l25, l03);
		CatchActionRequest expected9  = new CatchActionRequest(Player.White, from, l14, l25, l03, l17);
		CatchActionRequest expected10 = new CatchActionRequest(Player.White, from, l14, l25, l03, l21);
		CatchActionRequest expected11 = new CatchActionRequest(Player.White, from, l14, l25, l03, l26);
		CatchActionRequest expected12 = new CatchActionRequest(Player.White, from, l10);
		CatchActionRequest expected13 = new CatchActionRequest(Player.White, from, l05);
		
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
