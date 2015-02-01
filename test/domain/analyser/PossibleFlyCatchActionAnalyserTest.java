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

public class PossibleFlyCatchActionAnalyserTest
{

	@Test
	public void testFind() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 10);
		CatchActionRequest expected2 = new CatchActionRequest(28, 5);
		
		assertEquals(2, results.size());
		assertTrue(results.contains(expected1));
		assertTrue(results.contains(expected2));
	}
	
	@Test
	public void testFindNoDame() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Piece(Player.White));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindNoPiece() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindBlockedBySelf() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(19, size), new Piece(Player.White));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindBlockedByOpponent() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(10, size), new Piece(Player.Black));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindNoOpponent() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindMultipleOpponents() throws LocationOutOfRangeException
	{
		BoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location location = new Location(28, size);
		board.addPiece(location, new Dame(Player.White));
		board.addPiece(new Location(19, size), new Piece(Player.Black));
		board.addPiece(new Location(10, size), new Piece(Player.Black));
		PossibleFlyCatchActionAnalyser analyser = new PossibleFlyCatchActionAnalyser(board);
		
		List<CatchActionRequest> results = analyser.find(Player.White, location);
		CatchActionRequest expected1 = new CatchActionRequest(28, 14);
		
		assertEquals(1, results.size());
		assertTrue(results.contains(expected1));
	}
}