package controller;

import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import ui.LocalizationManager;
import ui.contracts.IUserInterface;
import domain.action.LegalActionChecker;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.input.ActionInput;
import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.input.contracts.IInputProvider;
import domain.location.Location;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class GameControllerTest
{
	@Mock
	private IObserver observer;

	@Mock
	private IGame game;
	
	@Mock
	private IBoard board; 
	
	@Mock
	private IUserInterface ui;
	
	@After
	public void tearDown()
	{
		reset(observer);
		reset(game);
		reset(board);
		reset(ui);
	}
	
	@Test
	public void testPromotion()
	{
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		IInputProvider inputProvider = new InputProvider(ui, legalActionChecker, game);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		Location location = new Location(1, new BoardSize(10, 10));
		
		expect(game.getBoard()).andReturn(board);
		expect(board.getReadOnlyBoard()).andReturn(board);
		observer.promotion(board, location);
		board.promotePiece(location);
		replay(observer);
		replay(board);
		replay(game);
		
		controller.promotion(board, location);
		verify(observer);
		verify(board);
		verify(game);
	}

	@Test
	public void testOutOfMoves()
	{
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		IInputProvider inputProvider = new InputProvider(ui, legalActionChecker, game);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.outOfMoves(Player.White);
		observer.gameOver(Player.Black);
		game.gameOver(Player.Black);
		replay(observer);
		replay(board);
		replay(game);
		
		controller.outOfMoves(Player.White);
		verify(observer);
		verify(board);
		verify(game);
	}
	
	@Test
	public void testInvalidInput()
	{
		LegalActionChecker legalActionChecker = new LegalActionChecker(game);
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.start(board, Player.White);
		expect(game.isOver()).andReturn(false).andReturn(true);
		expect(game.getBoard()).andReturn(board).times(2);
		expect(board.getReadOnlyBoard()).andReturn(board);
		expect(game.getCurrentPlayer()).andReturn(Player.White).times(2);
		expect(inputProvider.askInput()).andReturn(new ActionInput("lalala", game, legalActionChecker));
		observer.warning(LocalizationManager.getString("failedInput"));
		replay(game);
		replay(board);
		replay(inputProvider);
		replay(observer);
		
		controller.play();
		verify(game);
		verify(board);
		verify(inputProvider);
		verify(observer);
	}
	
	@Test
	public void testValidInput()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		IInput input = createMock(IInput.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.start(board, Player.White);
		expect(game.isOver()).andReturn(false).andReturn(true);
		expect(game.getBoard()).andReturn(board).times(2);
		expect(board.getReadOnlyBoard()).andReturn(board).times(2);
		expect(game.getCurrentPlayer()).andReturn(Player.White).andReturn(Player.Black);
		expect(input.process()).andReturn(true);
		expect(inputProvider.askInput()).andReturn(input);
		game.switchCurrentPlayer();
		observer.switchPlayer(board, Player.Black);
		replay(game);
		replay(board);
		replay(inputProvider);
		replay(input);
		replay(observer);
		
		controller.play();
		verify(game);
		verify(board);
		verify(inputProvider);
		verify(input);
		verify(observer);
	}
	
	
	@Test
	public void testResign()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.resign(Player.White);
		game.gameOver(Player.Black);
		observer.gameOver(Player.Black);
		replay(game);
		replay(observer);
		
		controller.resign(Player.White);
		verify(game);
		verify(observer);
	}
	
	@Test
	public void testAcceptRemise()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.acceptRemise();
		game.remise();
		observer.gameOver(null);
		replay(game);
		replay(observer);
		
		controller.acceptRemise();
		verify(game);
		verify(observer);
	}
	
	@Test
	public void testForcedRemise()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.forcedRemise();
		game.remise();
		observer.gameOver(null);
		replay(game);
		replay(observer);
		
		controller.forcedRemise();
		verify(game);
		verify(observer);
	}

}
