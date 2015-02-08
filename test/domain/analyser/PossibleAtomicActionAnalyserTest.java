package domain.analyser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Piece;

public class PossibleAtomicActionAnalyserTest
{
	private static final BoardSize size = new BoardSize(10, 10);

	@Test
	public void testGetBoard()
	{
		IBoard board = new Board(size);
		PossibleAtomicActionAnalyser analyser = new PossibleAtomicActionAnalyser(board);
		assertEquals(board, analyser.getBoard());
	}
	
	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		
		PossibleAtomicActionAnalyser analyser = new PossibleAtomicActionAnalyser(board);
		List<IActionRequest> requests = analyser.find(Player.White);
		
		Location from = new Location(28, size);
		Location a = new Location(17, size);
		Location b = new Location(23, size);
		Location c = new Location(37, size);
		Location d = new Location(33, size);
		CatchActionRequest expected1 = new CatchActionRequest(from, a);
		MoveActionRequest expected2 = new MoveActionRequest(from, b);
		CatchActionRequest expected3 = new CatchActionRequest(from, c);
		MoveActionRequest expected4 = new MoveActionRequest(from, d);
		
		assertEquals(4, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected3));
		assertTrue(requests.contains(expected4));
	}
}
