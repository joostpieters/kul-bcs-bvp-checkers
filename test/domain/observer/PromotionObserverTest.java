package domain.observer;

import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.piece.Dame;
import domain.piece.Piece;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class PromotionObserverTest
{
	@Mock
	private static IObserver observer;
	
	@After
	public void tearDown()
	{
		reset(observer);
	}
	
	@Test
	public void testPromotionDetected()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location expectedLocation = new Location(4, board.getSize());
		board.addPiece(expectedLocation, new Piece(Player.White));
		PromotionObserver promotionObserver = new PromotionObserver();
		promotionObserver.subscribe(observer);
		
		observer.promotion(board, expectedLocation);
		replay(observer);
		
		promotionObserver.switchPlayer(board, Player.Black);
		verify(observer);
	}
	
	@Test
	public void testCannotPromoteTwice()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location expectedLocation = new Location(4, board.getSize());
		board.addPiece(expectedLocation, new Dame(Player.White));
		PromotionObserver promotionObserver = new PromotionObserver();
		promotionObserver.subscribe(observer);
		
		//record nothing
		replay(observer);
		
		promotionObserver.switchPlayer(board, Player.Black);
		verify(observer);
	}
	
	@Test
	public void testCannotPromoteIfNotOnPromotionRow()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		Location expectedLocation = new Location(6, board.getSize());
		board.addPiece(expectedLocation, new Piece(Player.White));
		PromotionObserver promotionObserver = new PromotionObserver();
		promotionObserver.subscribe(observer);
		
		//record nothing
		replay(observer);
		
		promotionObserver.switchPlayer(board, Player.Black);
		verify(observer);
	}
}
