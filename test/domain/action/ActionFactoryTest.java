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
import domain.location.Location;
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
		Location from = new Location(18, board.getSize());
		Location to = new Location(12, board.getSize());
		IActionRequest request = new MoveActionRequest(from, to);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertFalse(action.isCatch());
		assertTrue(action instanceof AtomicActionStep);
	}
	
	@Test
	public void testCreateCompositeActionFly() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location from = new Location(18, board.getSize());
		Location to = new Location(4, board.getSize());
		IActionRequest request = new MoveActionRequest(from, to);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertFalse(action.isCatch());
		assertTrue(action instanceof CompositeActionFly);
	}
	
	@Test
	public void testCreateAtomicActionCatch() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location from = new Location(18, board.getSize());
		Location to = new Location(27, board.getSize());
		IActionRequest request = new CatchActionRequest(from, to);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof AtomicActionCatch);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateAtomicActionCatchTooClose() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location from = new Location(18, board.getSize());
		Location to = new Location(23, board.getSize());
		IActionRequest request = new CatchActionRequest(from, to);
		ActionFactory.create(request, board, Player.White);
	}
	
	@Test
	public void testCreateCompositeActionFlyCatch() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location from = new Location(18, board.getSize());
		Location to = new Location(45, board.getSize());
		IActionRequest request = new CatchActionRequest(from, to);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeActionFlyCatch);
	}
	
	@Test
	public void testCreateCompositeAction() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location a = new Location(18, board.getSize());
		Location b = new Location(40, board.getSize());
		Location c = new Location(49, board.getSize());
		IActionRequest request = new CatchActionRequest(a, b, c);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeAction);
		CompositeAction composite = (CompositeAction)action;
		List<IAction> actions = composite.getSubActions();
		assertTrue(actions.get(0) instanceof CompositeActionFlyCatch);
		assertTrue(actions.get(1) instanceof AtomicActionCatch);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCreateInvalidCompositeAction() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location a = new Location(18, board.getSize());
		Location b = new Location(34, board.getSize());
		Location c = new Location(25, board.getSize());
		IActionRequest request = new CatchActionRequest(a, b, c);
		ActionFactory.create(request, board, Player.White);
	}

	@Test
	public void testCaughtPiecesAreRemovedImmediately() throws IOException, LocationOutOfRangeException
	{
		IBoard board = BoardFactory.create(Paths.get("data", "input", "allActions.txt"));
		Location a = new Location(18, board.getSize());
		Location b = new Location(40, board.getSize());
		Location c = new Location(49, board.getSize());
		Location d = new Location(38, board.getSize());
		Location e = new Location(15, board.getSize());
		IActionRequest request = new CatchActionRequest(a, b, c, d, e);
		IAction action = ActionFactory.create(request, board, Player.White);
		assertTrue(action.isCatch());
		assertTrue(action instanceof CompositeAction);
		CompositeAction composite = (CompositeAction)action;
		List<IAction> actions = composite.getSubActions();
		assertTrue(actions.get(0) instanceof CompositeActionFlyCatch);
		assertTrue(actions.get(1) instanceof AtomicActionCatch);
	}
}
