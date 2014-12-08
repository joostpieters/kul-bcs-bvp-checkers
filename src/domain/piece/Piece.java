package domain.piece;
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
}
