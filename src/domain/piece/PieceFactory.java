package domain.piece;

import common.Player;
import domain.piece.contracts.IPiece;

public class PieceFactory {
	public static IPiece create(char pieceCode)
	{
		switch(pieceCode)
		{
			case 'o': return null;
			case 'O': return null;
			case 'w': return new Piece(Player.White);
			case 'W': return new Dame(Player.White);
			case 'z': return new Piece(Player.Black);
			case 'Z': return new Dame(Player.Black);
			default: throw new IllegalArgumentException("Invalid pieceCode: " + pieceCode);
		}
	}
}
