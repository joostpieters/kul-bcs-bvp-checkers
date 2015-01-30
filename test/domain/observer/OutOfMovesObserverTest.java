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
public class OutOfMovesObserverTest
{
	@Mock
	private static IObserver observer;
	
	@After
	public void tearDown()
	{
		reset(observer);
	}
	
	@Test
	public void testEmptyBoardAtStart()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		observer.fireOutOfMoves(Player.White);
		replay(observer);
		
		oomObserver.fireStart(board, Player.White);
		verify(observer);
	}
	
	@Test
	public void testEmptyBoard()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		observer.fireOutOfMoves(Player.White);
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}
	
	@Test
	public void testNoMoreBlackPieces()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		observer.fireOutOfMoves(Player.Black);
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.Black);
		verify(observer);
	}
	
	@Test
	public void testMovePossibleCatchNotPossible()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(10, size), new Piece(Player.White));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		//record nothing
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}
	
	@Test
	public void testMovePossibleCatchPossible()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(10, size), new Piece(Player.White));
		board.addPiece(new Location(14, size), new Piece(Player.Black));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		//record nothing
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}
	
	@Test
	public void testMoveNotPossibleCatchPossible()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		board.addPiece(new Location(7, size), new Piece(Player.Black));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		//record nothing
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}

	@Test
	public void testMoveNotPossibleCatchNotPossible()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(1, size), new Piece(Player.White));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		observer.fireOutOfMoves(Player.White);
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}
	
	@Test
	public void testDameMoveNotPossibleCatchNotPossible()
	{
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = new Board(size);
		board.addPiece(new Location(46, size), new Dame(Player.White));
		board.addPiece(new Location(41, size), new Dame(Player.Black));
		board.addPiece(new Location(37, size), new Dame(Player.Black));
		OutOfMovesObserver oomObserver = new OutOfMovesObserver();
		oomObserver.subscribe(observer);
		
		observer.fireOutOfMoves(Player.White);
		replay(observer);
		
		oomObserver.fireSwitchPlayer(board, Player.White);
		verify(observer);
	}
}
