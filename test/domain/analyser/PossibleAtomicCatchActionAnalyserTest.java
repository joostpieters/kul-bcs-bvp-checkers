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
import domain.piece.Piece;

public class PossibleAtomicCatchActionAnalyserTest
{
	private static final BoardSize size = new BoardSize(10, 10);
	
	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		Location from = new Location(28, size);
		board.addPiece(from, new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
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
	
	@Test
	public void testFindOnBorder() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		board.addPiece(new Location(36, size), new Piece(Player.White));
		board.addPiece(new Location(31, size), new Piece(Player.Black));
		board.addPiece(new Location(41, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(36, size);
		Location a = new Location(27, size);
		Location b = new Location(47, size);
		CatchActionRequest expected1 = new CatchActionRequest(Player.White, from, a);
		CatchActionRequest expected2 = new CatchActionRequest(Player.White, from, b);
		
		assertEquals(2, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFindNotPieceOfPlayer() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		analyser.find(Player.White, location);
	}
	
	@Test
	public void testFindTargetOccupied() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		board.addPiece(new Location(17, size), new Piece(Player.White));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(28, size);
		Location a = new Location(19, size);
		Location b = new Location(37, size);
		Location c = new Location(39, size);
		CatchActionRequest expected1 = new CatchActionRequest(Player.White, from, a);
		CatchActionRequest expected2 = new CatchActionRequest(Player.White, from, b);
		CatchActionRequest expected4 = new CatchActionRequest(Player.White, from, c);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
	
	@Test
	public void testFindNoOpponent() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(28, size);
		Location a = new Location(19, size);
		Location b = new Location(37, size);
		Location c = new Location(39, size);
		CatchActionRequest expected1 = new CatchActionRequest(Player.White, from, a);
		CatchActionRequest expected2 = new CatchActionRequest(Player.White, from, b);
		CatchActionRequest expected4 = new CatchActionRequest(Player.White, from, c);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
}
