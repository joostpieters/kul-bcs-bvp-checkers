package domain.board;
import java.nio.file.Path; 
import java.io.IOException;
import java.util.Scanner;

import common.Configs;
import domain.board.contracts.IBoard;
import domain.location.Location;
import domain.piece.PieceFactory;
import domain.piece.contracts.IPiece;


public class BoardFactory
{
	private BoardFactory() { }
	
	public static IBoard create(BoardSize size, Path input) throws IOException
	{
		Board board = new Board(size);
		Scanner scanner = new Scanner(input);
		while(scanner.hasNext())
		{
			int index = scanner.nextInt();
			Location location = new Location(index, size);
			char pieceCode = scanner.next().charAt(0);
			IPiece piece = PieceFactory.create(pieceCode);
			board.addPiece(location, piece);
		}
		scanner.close();
		
		return board;
	}
	
	public static IBoard create(Path input) throws IOException
	{
		return create(Configs.Size, input);
	}
}
