package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IGameFollower;
import domain.updates.contracts.IGameUpdateSource;

/**
 * A convenient, basic implementation of {@link IGameUpdateSource}.  
 */
public abstract class GameUpdateSource implements IGameUpdateSource
{
	private final List<IGameFollower> followers = new ArrayList<IGameFollower>(); 
	private boolean disabled = false;
	
	protected List<IGameFollower> getFollowers()
	{
		return followers;
	}
	
	protected boolean isDisabled()
	{
		return disabled;
	}
	
	protected void enableUpdateFollowers() {
		this.disabled = false;
	}
	
	protected void disableUpdateFollowers() {
		this.disabled = true;
	}
	
	protected void updateFollowers(IReadOnlyBoard board)
	{
		if(!isDisabled())
		{
			for(IGameFollower follower : getFollowers())
			{
				follower.update(board);
			}
		}
	}
	
	protected void updateFollowersGameOver(Player winner)
	{
		if(!isDisabled())
		{
			for(IGameFollower follower : getFollowers())
			{
				follower.gameOver(winner);
			}
		}
	}
	
	@Override
	public void subscribe(IGameFollower follower)
	{
		followers.add(follower);
	}
	
	@Override
	public void unsubscribe(IGameFollower follower)
	{
		followers.remove(follower);
	}
}
