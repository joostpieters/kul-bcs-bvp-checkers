package domain.action;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.action.contracts.IAction;
import domain.action.contracts.IActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.BoardFactory;
import domain.board.contracts.IBoard;
import domain.location.LocationOutOfRangeException;

@RunWith(EasyMockRunner.class) 
public class ActionFactoryTest
{
	@After
	public void tearDown()
	{
	}
	
	@Test
	public void testCreateAtomicActionStep() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new MoveActionRequest(18, 12);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertFalse(action.isCatch());
		assertTrue(action instanceof AtomicActionStep);
	}
	
	@Test
	public void testCreateCompositeActionFly() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new MoveActionRequest(18, 4);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertFalse(action.isCatch());
		assertTrue(action instanceof CompositeActionFly);
	}
	
	@Test
	public void testCreateAtomicActionCatch() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 27);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof AtomicActionCatch);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateAtomicActionCatchTooClose() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 23);
		ActionFactory.create(request, board, Player.White);
	}
	
	@Test
	public void testCreateCompositeActionFlyCatch() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 45);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeActionFlyCatch);
	}
	
	@Test
	public void testCreateCompositeAction() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 40, 49);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeAction);
		CompositeAction composite = (CompositeAction)action;
		List<IAction> actions = composite.getActions();
		assertTrue(actions.get(0) instanceof CompositeActionFlyCatch);
		assertTrue(actions.get(1) instanceof AtomicActionCatch);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCreateInvalidCompositeAction() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 34, 25);
		ActionFactory.create(request, board, Player.White);
	}

	@Test
	public void testCaughtPiecesAreRemovedImmediately() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		IActionRequest request = new CatchActionRequest(18, 40, 49, 38, 15);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeAction);
		CompositeAction composite = (CompositeAction)action;
		List<IAction> actions = composite.getActions();
		assertTrue(actions.get(0) instanceof CompositeActionFlyCatch);
		assertTrue(actions.get(1) instanceof AtomicActionCatch);
	}
}
