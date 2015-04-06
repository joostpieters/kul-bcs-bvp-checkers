package domain.action;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.Dame;
import domain.piece.Piece;

public class ActionPromotionTest
{
	@Test
	public void testPromoteEmptyLocationOnPromotionRow_Invalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location promotionRowSquare = new Location(1, size);
		
		ActionPromotion promotion = new ActionPromotion(promotionRowSquare);
		
		assertFalse(promotion.isValidOn(board, Player.White));
	}
	
	@Test
	public void testPromoteLocationWithDameOnPromotionRow_Invalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location promotionRowSquare = new Location(1, size);
		board.addPiece(promotionRowSquare, new Dame(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(promotionRowSquare);
		
		assertFalse(promotion.isValidOn(board, Player.White));
	}
	
	@Test
	public void testPromoteLocationWithOpponentPieceOnPromotionRow_Invalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location promotionRowSquare = new Location(1, size);
		board.addPiece(promotionRowSquare, new Piece(Player.Black));
		
		ActionPromotion promotion = new ActionPromotion(promotionRowSquare);
		
		assertFalse(promotion.isValidOn(board, Player.White));
	}
	
	@Test
	public void testPromoteLocationWithPieceOnWrongRow_Invalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location notPromotionRowSquare = new Location(6, size);
		board.addPiece(notPromotionRowSquare, new Piece(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(notPromotionRowSquare);
		
		assertFalse(promotion.isValidOn(board, Player.White));
	}
	
	@Test
	public void testPromoteLocationWithPieceOnOpponentPromotionRow_Invalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location notPromotionRowSquare = new Location(50, size);
		board.addPiece(notPromotionRowSquare, new Piece(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(notPromotionRowSquare);
		
		assertFalse(promotion.isValidOn(board, Player.White));
	}
	
	@Test
	public void testPromoteLocationWithPieceOnPromotionRow_Valid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location promotionRowSquare = new Location(1, size);
		board.addPiece(promotionRowSquare, new Piece(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(promotionRowSquare);
		
		assertTrue(promotion.isValidOn(board, Player.White));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testExecuteActionInvalid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location notPromotionRowSquare = new Location(50, size);
		board.addPiece(notPromotionRowSquare, new Piece(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(notPromotionRowSquare);
		
		promotion.executeOn(board, Player.White);
	}
	
	@Test
	public void testExecuteActionValid() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location promotionRowSquare = new Location(1, size);
		board.addPiece(promotionRowSquare, new Piece(Player.White));
		
		ActionPromotion promotion = new ActionPromotion(promotionRowSquare);
		
		assertTrue(board.getSquare(promotionRowSquare).getPiece().canPromote());
		promotion.executeOn(board, Player.White);
		assertFalse(board.getSquare(promotionRowSquare).getPiece().canPromote());
	}
	
	@Test
	public void testGetFrom() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Location location = new Location(3, size);
		ActionPromotion promotion = new ActionPromotion(location);
		
		assertEquals(location, promotion.getFrom());
	}
	
	@Test
	public void testIsCatch() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Location location = new Location(3, size);
		ActionPromotion promotion = new ActionPromotion(location);
		
		assertFalse(promotion.isCatch());
	}
	
	@Test
	public void testEquals() throws LocationOutOfRangeException
	{
		IBoardSize size = new BoardSize(10, 10);
		Location location1 = new Location(1, size);
		Location location2 = new Location(2, size);
		ActionPromotion promotionA = new ActionPromotion(location1);
		ActionPromotion promotionB = new ActionPromotion(location1);
		ActionPromotion promotionC = new ActionPromotion(location2);
		
		assertFalse(promotionA.equals(null));
		assertFalse(promotionA.equals(new Object()));
		assertTrue(promotionA.equals(promotionA));
		assertTrue(promotionA.equals(promotionB));
		assertFalse(promotionA.equals(promotionC));
	}
}
