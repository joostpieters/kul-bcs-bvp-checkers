package domain.piece;
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
}
