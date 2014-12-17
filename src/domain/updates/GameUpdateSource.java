package domain.updates;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
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
	
	protected void updateFollowers(IReadOnlyBoard board, Player performer)
	{
		if(!isDisabled())
		{
			for(IGameFollower follower : getFollowers())
			{
				follower.update(board, performer);
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
	
	protected void updateFollowersPromotion(Location location)
	{
		if(!isDisabled())
		{
			for(IGameFollower follower : getFollowers())
			{
				follower.promotion(location);
			}
		}
	}
	
	protected void updateFollowersOutOfMoves(Player player)
	{
		if(!isDisabled())
		{
			for(IGameFollower follower : getFollowers())
			{
				follower.outOfMoves(player);
			}
		}
	}
	
	@Override
	public void subscribe(IGameFollower follower)
	{
		getFollowers().add(follower);
	}
	
	@Override
	public void unsubscribe(IGameFollower follower)
	{
		getFollowers().remove(follower);
	}
}
