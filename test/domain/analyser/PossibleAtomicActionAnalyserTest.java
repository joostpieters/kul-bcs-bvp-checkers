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
	@Test
	public void testGetBoard()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		PossibleAtomicActionAnalyser analyser = new PossibleAtomicActionAnalyser(board);
		assertEquals(board, analyser.getBoard());
	}
	
	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(22, size), new Piece(Player.Black));
		board.addPiece(new Location(32, size), new Piece(Player.Black));
		
		PossibleAtomicActionAnalyser analyser = new PossibleAtomicActionAnalyser(board);
		List<IActionRequest> requests = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 17);
		MoveActionRequest expected2 = new MoveActionRequest(28, 23);
		CatchActionRequest expected3 = new CatchActionRequest(28, 37);
		MoveActionRequest expected4 = new MoveActionRequest(28, 33);
		
		assertEquals(4, requests.size());
		assertTrue(requests.contains(expected1));
		assertTrue(requests.contains(expected2));
		assertTrue(requests.contains(expected3));
		assertTrue(requests.contains(expected4));
	}
}
