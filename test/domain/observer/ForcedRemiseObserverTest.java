package domain.observer;

import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import common.Player;
import domain.action.contracts.IAction;
import domain.update.contracts.IObserver;

@RunWith(EasyMockRunner.class) 
public class ForcedRemiseObserverTest
{
	@Mock
	private IObserver observer;

	@Mock
	private IAction actionMock;
	
	@After
	public void tearDown()
	{
		reset(observer);
		reset(actionMock);
	}
	
	@Test
	public void testForcedRemiseAfterThreeMoves()
	{
		ForcedRemiseObserver frObserver = new ForcedRemiseObserver(3);
		frObserver.subscribe(observer);
		
		expect(actionMock.isCatch()).andReturn(false).times(3);
		observer.fireForcedRemise();
		replay(observer);
		replay(actionMock);
		
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.Black);
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.White);
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.Black);
		verify(observer);
	}

	@Test
	public void testNoForcedRemiseWhenPromotionHappened()
	{
		ForcedRemiseObserver frObserver = new ForcedRemiseObserver(3);
		frObserver.subscribe(observer);
		
		expect(actionMock.isCatch()).andReturn(false).times(3);
		//record nothing
		replay(observer);
		replay(actionMock);
		
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.Black);
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.White);
		frObserver.fireExecuteAction(actionMock);
		frObserver.firePromotion(null, null);
		frObserver.fireSwitchPlayer(null, Player.Black);
		verify(observer);
		verify(actionMock);
	}
	
	@Test
	public void testNoForcedRemiseWhenCatchHappened()
	{
		ForcedRemiseObserver frObserver = new ForcedRemiseObserver(3);
		frObserver.subscribe(observer);
		
		expect(actionMock.isCatch()).andReturn(false).times(2).andReturn(true);
		//record nothing
		replay(observer);
		replay(actionMock);
		
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.Black);
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.White);
		frObserver.fireExecuteAction(actionMock);
		frObserver.fireSwitchPlayer(null, Player.Black);
		verify(observer);
		verify(actionMock);
	}
}
