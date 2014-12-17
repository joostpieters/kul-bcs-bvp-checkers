package domain.piece;
import common.Configs;
import common.Player;
import domain.piece.contracts.IPiece;

/**
 * An IPiece representing a basic Piece.  
 */
public class Piece implements IPiece {
	private final Player player;

	public Piece(Player player) {
		this.player = player;
	}
	
	@Override
	public Player getPlayer() {
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
	public Piece getClone()
	{
		return new Piece(getPlayer());
	}
	
	@Override
	public String toString() {
		return "Piece";
	}
}
