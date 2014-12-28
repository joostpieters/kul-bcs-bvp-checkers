package domain.piece;

import common.Player;
import domain.piece.contracts.IPiece;

/**
 * This class contains a Factory Method for creating {@link Piece}s from their respective character codes.
 */
public class PieceFactory
{
	private PieceFactory() { }
	
	/**
	 * Creates a Piece based on the given character code and returns it.
	 * 
	 * @param 	pieceCode
	 * 			The character code that identifies the Piece to be created.
	 */
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
