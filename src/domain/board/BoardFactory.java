package domain.board;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.Configs;
import common.Location;
import common.Player;
import domain.piece.Dame;
import domain.piece.Piece;


public class BoardFactory {
	public static Board create(BoardSize size, File input) throws FileNotFoundException
	{
		Board board = new Board(size);
		Scanner scanner = new Scanner(input);
		while(scanner.hasNext())
		{
			int index = scanner.nextInt();
			Location location = Location.fromIndex(index, size);
			char pieceCode = scanner.next().charAt(0);
			Piece piece = createPiece(pieceCode);
			board.addPiece(location, piece);
		}
		scanner.close();
		
		return board;
	}
	
	public static Board create(File input) throws FileNotFoundException
	{
		return create(Configs.Size, input);
	}
	
	private static Piece createPiece(char pieceCode)
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
