package domain.analyser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import common.Player;
import domain.action.request.MoveActionRequest;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Piece;

public class PossibleAtomicStepActionAnalyserTest
{
	private static final BoardSize size = new BoardSize(10, 10);
	
	@Test
	public void testGetBoard()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		assertEquals(board, analyser.getBoard());
	}

	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(28, size);
		Location a = new Location(22, size);
		Location b = new Location(23, size);
		Location c = new Location(32, size);
		Location d = new Location(33, size);
		
		MoveActionRequest expected1 = new MoveActionRequest(from, a);
		MoveActionRequest expected2 = new MoveActionRequest(from, b);
		MoveActionRequest expected3 = new MoveActionRequest(from, c);
		MoveActionRequest expected4 = new MoveActionRequest(from, d);
		
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
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(36, size);
		Location a = new Location(31, size);
		Location b = new Location(41, size);
		
		MoveActionRequest expected1 = new MoveActionRequest(from, a);
		MoveActionRequest expected2 = new MoveActionRequest(from, b);
		
		assertEquals(2, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
	}
	
	@Test
	public void testFindNotPieceOfPlayer() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Piece(Player.Black));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White, location);
		
		assertEquals(0, requests.size());
	}
	
	@Test
	public void testFindTargetOccupied() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		Location start = new Location(28, size);
		board.addPiece(start, new Piece(Player.White));
		board.addPiece(new Location(32, size), new Piece(Player.White));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White, start);
		
		Location from = new Location(28, size);
		Location a = new Location(22, size);
		Location b = new Location(23, size);
		Location c = new Location(33, size);
		
		MoveActionRequest expected1 = new MoveActionRequest(from, a);
		MoveActionRequest expected2 = new MoveActionRequest(from, b);
		MoveActionRequest expected4 = new MoveActionRequest(from, c);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
}
