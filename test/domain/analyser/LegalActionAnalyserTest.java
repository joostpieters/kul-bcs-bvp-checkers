package domain.analyser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.Configs;
import common.Player;
import domain.action.request.ActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Dame;
import domain.piece.Piece;

public class LegalActionAnalyserTest
{
	private boolean previousConfigSetting;
	
	@Before
	public void setup()
	{
		previousConfigSetting = Configs.MandatoryMaximalCatching;
	}
	
	@After
	public void tearDown()
	{
		Configs.MandatoryMaximalCatching = previousConfigSetting;
	}
	
	@Test
	public void testIsActionLegalNonMaximalCatching()
	{
		
		Configs.MandatoryMaximalCatching = false;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		ActionRequest moveRequest = new MoveActionRequest(1, 2);
		ActionRequest catchRequest = new CatchActionRequest(1, 2);
		
		assertFalse(analyser.isActionLegal(moveRequest, Player.White));
		assertTrue(analyser.isActionLegal(catchRequest, Player.White));
	}
	
	@Test
	public void testIsActionLegalMaximalCatchingMoveAllowedIfNoCatches()
	{
		Configs.MandatoryMaximalCatching = true;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		ActionRequest moveRequest = new MoveActionRequest(1, 2);
		
		assertTrue(analyser.isActionLegal(moveRequest, Player.White));
	}
	
	@Test(expected=AssertionError.class)
	public void testIsActionLegalMaximalCatchingNoCatchesProposeCatch() throws LocationOutOfRangeException
	{
		Configs.MandatoryMaximalCatching = true;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		ActionRequest catchRequest = new CatchActionRequest(1, 2);
		
		analyser.isActionLegal(catchRequest, Player.White);
	}
	
	@Test
	public void testIsActionLegalMaximalCatchingMoveNotAllowedIfCatches() throws LocationOutOfRangeException
	{
		Configs.MandatoryMaximalCatching = true;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(28, size), new Piece(Player.White));
		board.addPiece(new Location(23, size), new Piece(Player.Black));
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		ActionRequest moveRequest = new MoveActionRequest(1, 2);
		
		assertFalse(analyser.isActionLegal(moveRequest, Player.White));
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
		
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		
		
		CatchActionRequest expected1 = new CatchActionRequest(28, 14, 25, 3, 17);
		CatchActionRequest expected2 = new CatchActionRequest(28, 14, 25, 3, 21);
		CatchActionRequest expected3 = new CatchActionRequest(28, 14, 25, 3, 26);
		
		CatchActionRequest nonMax1 = new CatchActionRequest(28, 14);
		CatchActionRequest nonMax2 = new CatchActionRequest(28, 14, 25);
		CatchActionRequest nonMax3 = new CatchActionRequest(28, 14, 25, 3);
		
		assertTrue(analyser.isActionLegal(expected1, Player.White));
		assertTrue(analyser.isActionLegal(expected2, Player.White));
		assertTrue(analyser.isActionLegal(expected3, Player.White));
		
		assertFalse(analyser.isActionLegal(nonMax1, Player.White));
		assertFalse(analyser.isActionLegal(nonMax2, Player.White));
		assertFalse(analyser.isActionLegal(nonMax3, Player.White));
	}
}
