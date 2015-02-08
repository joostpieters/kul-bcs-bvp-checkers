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
	public void testIsActionLegalNonMaximalCatching() throws LocationOutOfRangeException
	{
		
		Configs.MandatoryMaximalCatching = false;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		ActionRequest moveRequest = new MoveActionRequest(a, b);
		ActionRequest catchRequest = new CatchActionRequest(a, b);
		
		assertFalse(analyser.isActionLegal(moveRequest, Player.White));
		assertTrue(analyser.isActionLegal(catchRequest, Player.White));
	}
	
	@Test
	public void testIsActionLegalMaximalCatchingMoveAllowedIfNoCatches() throws LocationOutOfRangeException
	{
		Configs.MandatoryMaximalCatching = true;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		ActionRequest moveRequest = new MoveActionRequest(a, b);
		
		assertTrue(analyser.isActionLegal(moveRequest, Player.White));
	}
	
	@Test(expected=AssertionError.class)
	public void testIsActionLegalMaximalCatchingNoCatchesProposeCatch() throws LocationOutOfRangeException
	{
		Configs.MandatoryMaximalCatching = true;
		
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		LegalActionAnalyser analyser = new LegalActionAnalyser(board);
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		ActionRequest catchRequest = new CatchActionRequest(a, b);
		
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
		Location a = new Location(1, size);
		Location b = new Location(2, size);
		ActionRequest moveRequest = new MoveActionRequest(a, b);
		
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
		
		Location from = new Location(28, size);
		Location l14 = new Location(14, size);
		Location l03 = new Location(3, size);
		Location l17 = new Location(17, size);
		Location l21 = new Location(21, size);
		Location l26 = new Location(26, size);
		Location l25 = new Location(25, size);
		
		CatchActionRequest expected1 = new CatchActionRequest(from, l14, l25, l03, l17);
		CatchActionRequest expected2 = new CatchActionRequest(from, l14, l25, l03, l21);
		CatchActionRequest expected3 = new CatchActionRequest(from, l14, l25, l03, l26);
		
		CatchActionRequest nonMax1 = new CatchActionRequest(from, l14);
		CatchActionRequest nonMax2 = new CatchActionRequest(from, l14, l25);
		CatchActionRequest nonMax3 = new CatchActionRequest(from, l14, l25, l03);
		
		assertTrue(analyser.isActionLegal(expected1, Player.White));
		assertTrue(analyser.isActionLegal(expected2, Player.White));
		assertTrue(analyser.isActionLegal(expected3, Player.White));
		
		assertFalse(analyser.isActionLegal(nonMax1, Player.White));
		assertFalse(analyser.isActionLegal(nonMax2, Player.White));
		assertFalse(analyser.isActionLegal(nonMax3, Player.White));
	}
}
