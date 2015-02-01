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
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White);
		
		MoveActionRequest expected1 = new MoveActionRequest(28, 22);
		MoveActionRequest expected2 = new MoveActionRequest(28, 23);
		MoveActionRequest expected3 = new MoveActionRequest(28, 32);
		MoveActionRequest expected4 = new MoveActionRequest(28, 33);
		
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
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White);
		
		MoveActionRequest expected1 = new MoveActionRequest(36, 31);
		MoveActionRequest expected2 = new MoveActionRequest(36, 41);
		
		assertEquals(2, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
	}
	
	@Test
	public void testFindNotPieceOfPlayer() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
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
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location start = new Location(28, size);
		board.addPiece(start, new Piece(Player.White));
		board.addPiece(new Location(32, size), new Piece(Player.White));
		PossibleAtomicStepActionAnalyser analyser = new PossibleAtomicStepActionAnalyser(board);
		List<MoveActionRequest> requests = analyser.find(Player.White, start);
		
		MoveActionRequest expected1 = new MoveActionRequest(28, 22);
		MoveActionRequest expected2 = new MoveActionRequest(28, 23);
		MoveActionRequest expected4 = new MoveActionRequest(28, 33);
		
		assertEquals(3, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected4));
	}
}
