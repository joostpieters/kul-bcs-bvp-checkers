package domain.piece;
import common.Configs;
import common.Player;


public class Piece {
	private Player player;

	public Piece(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public char getPieceCode()
	{
		return getPlayer() == Player.Black ?
				'z' :
				'w';
	}
	
	public boolean canMoveBackward()
	{
		return false;
	}
	
	public boolean canCatchBackward()
	{
		return Configs.BackwardCatchingAllowed;
	}
	
	public boolean canFly()
	{
		return false;
	}
	
	public boolean canPromote()
	{
		return true;
	}
	
	public Piece getDeepClone()
	{
		return new Piece(getPlayer());
	}
	
	@Override
	public String toString() {
		return "Piece";
	}
}
