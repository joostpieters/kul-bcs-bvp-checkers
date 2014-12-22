package domain.piece;

import common.Configs;
import common.Player;
import domain.piece.contracts.IPiece;

/**
 * An IPiece representing a promoted Piece: a Dame.
 */
public class Dame implements IPiece
{
	private final Player player;
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Creates a new {@link Dame} of the given {@link Player}.
	 * 
	 * @param 	player
	 * 			The owner of this {@link IPiece}.
	 */
	public Dame(Player player)
	{
		this.player = player;
	}
	
	/**
	 * Creates a new {@link Dame} equal to but not linked to the given {@link Dame}.
	 *  
	 * @param 	original
	 * 			The original {@link Dame}.
	 */
	public Dame(Dame original)
	{
		this.player = original.getPlayer();
	}
	
	@Override
	public char getPieceCode()
	{
		return getPlayer() == Player.Black ?
				'Z' :
				'W';
	}
	
	@Override
	public boolean canStepBackward()
	{
		return true;
	}
	
	@Override
	public boolean canCatchBackward()
	{
		return true;
	}
	
	@Override
	public boolean canFly()
	{
		return Configs.FlyingDame;
	}
	
	@Override
	public boolean canPromote()
	{
		return false;
	}
	
	@Override
	public IPiece getDeepClone()
	{
		return new Dame(this);
	}
	
	@Override
	public String toString()
	{
		return "Dame";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		if(obj instanceof Dame)
		{
			Dame casted = (Dame)obj;
			return this.getPlayer() == casted.getPlayer();
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(canPromote()) ^ getPlayer().hashCode();
	}
}
