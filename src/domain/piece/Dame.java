package domain.piece;
import common.Configs;
import common.Player;


public class Dame extends Piece {

	public Dame(Player player) {
		super(player);
	}
	
	@Override
	public char getPieceCode()
	{
		return getPlayer() == Player.Black ?
				'Z' :
				'W';
	}
	
	@Override
	public boolean canMoveBackwards() {
		return true;
	}
	
	@Override
	public boolean canFly() {
		return Configs.FlyingDame;
	}
	
	@Override
	public boolean canPromote() {
		return false;
	}
	
	@Override
	public Piece getDeepClone() {
		return new Dame(getPlayer());
	}
}
