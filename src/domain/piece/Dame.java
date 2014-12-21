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
	
	public Dame(Player player)
	{
		this.player = player;
	}
	
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
}
