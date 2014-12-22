package domain.piece;
import common.Configs;
import common.Player;
import domain.piece.contracts.IPiece;

/**
 * An IPiece representing a basic Piece.  
 */
public class Piece implements IPiece
{
	private final Player player;

	/**
	 * Creates a new {@link Piece} of the given {@link Player}.
	 * 
	 * @param 	player
	 * 			The owner of this {@link IPiece}.
	 */
	public Piece(Player player)
	{
		this.player = player;
	}
	
	/**
	 * Creates a new {@link Piece} equal to but not linked to the given {@link Piece}.
	 *  
	 * @param 	original
	 * 			The original {@link Piece}.
	 */
	public Piece(Piece original)
	{
		this.player = original.getPlayer();
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public char getPieceCode()
	{
		return getPlayer() == Player.Black ?
				'z' :
				'w';
	}
	
	@Override
	public boolean canStepBackward()
	{
		return false;
	}
	
	@Override
	public boolean canCatchBackward()
	{
		return Configs.BackwardCatchingAllowed;
	}
	
	@Override
	public boolean canFly()
	{
		return false;
	}
	
	@Override
	public boolean canPromote()
	{
		return true;
	}
	
	@Override
	public Piece getDeepClone()
	{
		return new Piece(this);
	}
	
	@Override
	public String toString()
	{
		return "Piece";
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
		if(obj instanceof Piece)
		{
			Piece casted = (Piece)obj;
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
