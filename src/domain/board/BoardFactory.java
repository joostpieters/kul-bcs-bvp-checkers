package domain.board;
import java.nio.file.Path; 
import java.io.IOException;
import java.util.Scanner;

import common.ConfigurationManager;
import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.PieceFactory;
import domain.piece.contracts.IPiece;

/**
 * This Factory creates {@link IBoard}s based on textual input files.
 */
public class BoardFactory
{
	private BoardFactory() { }

	/**
	 * Return a new {@link IBoard} of given size from the contents of the given input file.
	 * @param 	size
	 * 			The {@link IBoardSize} used to convert location indices to actual {@link Location}s.
	 * @param 	input
	 * 			The path to the input file.
	 * @throws 	IOException
	 * 			When the input file cannot be read.
	 * @throws 	LocationOutOfRangeException
	 * 			When a location index cannot be converted to a Location considering the {@link IBoardSize}.
	 */
	public static IBoard create(IBoardSize size, Path input) throws IOException, LocationOutOfRangeException
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
	
	/**
	 * Return a new {@link IBoard} of default size from the contents of the given input file.
	 * @param 	input
	 * 			The path to the input file.
	 * @throws 	IOException
	 * 			When the input file cannot be read.
	 * @throws 	LocationOutOfRangeException
	 * 			When a location index cannot be converted to a Location considering the {@link IBoardSize}.
	 */
	public static IBoard create(Path input) throws IOException, LocationOutOfRangeException
	{
		return create(ConfigurationManager.getInstance().getBoardSize(), input);
	}
}
