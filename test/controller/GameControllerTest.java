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
import domain.analyser.LegalActionAnalyser;
import domain.board.BoardSize;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.input.InputProvider;
import domain.input.contracts.IInput;
import domain.input.contracts.IInputProvider;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
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
	public void testPromotion() throws LocationOutOfRangeException
	{
		LegalActionAnalyser legalActionChecker = new LegalActionAnalyser(board);
		IInputProvider inputProvider = new InputProvider(ui, legalActionChecker, game);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		Location location = new Location(1, new BoardSize(10, 10));
		
		expect(game.getBoard()).andReturn(board);
		expect(board.getReadOnlyBoard()).andReturn(board);
		observer.firePromotion(board, location);
		board.promotePiece(location);
		replay(observer);
		replay(board);
		replay(game);
		
		controller.firePromotion(board, location);
		verify(observer);
		verify(board);
		verify(game);
	}

	@Test
	public void testOutOfMoves()
	{
		LegalActionAnalyser legalActionChecker = new LegalActionAnalyser(board);
		IInputProvider inputProvider = new InputProvider(ui, legalActionChecker, game);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.fireOutOfMoves(Player.White);
		observer.fireGameOver(Player.Black);
		game.gameOver(Player.Black);
		replay(observer);
		replay(board);
		replay(game);
		
		controller.fireOutOfMoves(Player.White);
		verify(observer);
		verify(board);
		verify(game);
	}
	
	@Test
	public void testInvalidInput() throws LocationOutOfRangeException
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		IInput input = createMock(IInput.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.fireStart(board, Player.White);
		expect(game.isOver()).andReturn(false).andReturn(true);
		expect(game.getBoard()).andReturn(board);
		expect(board.getReadOnlyBoard()).andReturn(board);
		expect(game.getCurrentPlayer()).andReturn(Player.White);
		expect(inputProvider.askInput()).andReturn(input);
		expect(input.process()).andReturn(false);
		observer.fireWarning(LocalizationManager.getString("failedInput"));
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
	public void testValidInput() throws LocationOutOfRangeException
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		IInput input = createMock(IInput.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.fireStart(board, Player.White);
		expect(game.isOver()).andReturn(false).andReturn(true);
		expect(game.getBoard()).andReturn(board).times(2);
		expect(board.getReadOnlyBoard()).andReturn(board).times(2);
		expect(game.getCurrentPlayer()).andReturn(Player.White).andReturn(Player.Black);
		expect(input.process()).andReturn(true);
		expect(inputProvider.askInput()).andReturn(input);
		game.switchCurrentPlayer();
		observer.fireSwitchPlayer(board, Player.Black);
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
		
		observer.fireResign(Player.White);
		game.gameOver(Player.Black);
		observer.fireGameOver(Player.Black);
		replay(game);
		replay(observer);
		
		controller.fireResign(Player.White);
		verify(game);
		verify(observer);
	}
	
	@Test
	public void testAcceptRemise()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.fireAcceptRemise();
		game.remise();
		observer.fireGameOver(null);
		replay(game);
		replay(observer);
		
		controller.fireAcceptRemise();
		verify(game);
		verify(observer);
	}
	
	@Test
	public void testForcedRemise()
	{
		IInputProvider inputProvider = createMock(IInputProvider.class);
		GameController controller = new GameController(game, inputProvider);
		controller.subscribe(observer);
		
		observer.fireForcedRemise();
		game.remise();
		observer.fireGameOver(null);
		replay(game);
		replay(observer);
		
		controller.fireForcedRemise();
		verify(game);
		verify(observer);
	}

}
