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
	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
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
	
	@Test
	public void testFindOnBorder() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(36, size), new Piece(Player.White));
		board.addPiece(new Location(31, size), new Piece(Player.Black));
		board.addPiece(new Location(41, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(36, 27);
		CatchActionRequest expected2 = new CatchActionRequest(36, 47);
		
		assertEquals(2, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFindNotPieceOfPlayer() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		analyser.find(Player.White, location);
	}
	
	@Test
	public void testFindTargetOccupied() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		board.addPiece(new Location(17, size), new Piece(Player.White));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 19);
		CatchActionRequest expected2 = new CatchActionRequest(28, 37);
		CatchActionRequest expected4 = new CatchActionRequest(28, 39);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
	
	@Test
	public void testFindNoOpponent() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		board.addPiece(new Location(33, size), new Piece(Player.Black));
		PossibleAtomicCatchActionAnalyser analyser = new PossibleAtomicCatchActionAnalyser(board);
		List<CatchActionRequest> requests = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 19);
		CatchActionRequest expected2 = new CatchActionRequest(28, 37);
		CatchActionRequest expected4 = new CatchActionRequest(28, 39);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
}
