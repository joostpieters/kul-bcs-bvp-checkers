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

public class MaximalCatchActionAnalyserTest
{
	@Test
	public void testGetBoard()
	{
		IBoard board = new Board(new BoardSize(10, 10));
		MaximalCatchActionAnalyser analyser = new MaximalCatchActionAnalyser(board);
		assertEquals(board, analyser.getBoard());
	}
	
	@Test
	public void testFindMaxiMultiFlyCatch() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(19, size), new Piece(Player.Black));
		board.addPiece(new Location(20, size), new Piece(Player.Black));
		board.addPiece(new Location(9, size), new Piece(Player.Black));
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		
		MaximalCatchActionAnalyser analyser = new MaximalCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 14, 25, 3, 17);
		CatchActionRequest expected2 = new CatchActionRequest(28, 14, 25, 3, 21);
		CatchActionRequest expected3 = new CatchActionRequest(28, 14, 25, 3, 26);
		
		assertEquals(3, results.size());
		assertTrue(results.contains(expected1));
		assertTrue(results.contains(expected2));
		assertTrue(results.contains(expected3));
	}
	
	@Test
	public void testFindAllMaxiMultiFlyCatch() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(19, size), new Piece(Player.Black));
		board.addPiece(new Location(20, size), new Piece(Player.Black));
		board.addPiece(new Location(9, size), new Piece(Player.Black));
		board.addPiece(new Location(12, size), new Piece(Player.Black));
		
		MaximalCatchActionAnalyser analyser = new MaximalCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 14, 25, 3, 17);
		CatchActionRequest expected2 = new CatchActionRequest(28, 14, 25, 3, 21);
		CatchActionRequest expected3 = new CatchActionRequest(28, 14, 25, 3, 26);
		
		assertEquals(3, results.size());
		assertTrue(results.contains(expected1));
		assertTrue(results.contains(expected2));
		assertTrue(results.contains(expected3));
	}
	
	@Test
	public void testFindNoCatches() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		
		MaximalCatchActionAnalyser analyser = new MaximalCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
}
