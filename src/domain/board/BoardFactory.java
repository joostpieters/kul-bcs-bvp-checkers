package domain.board;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import common.Configs;
import common.Location;
import common.Player;
import domain.piece.Dame;
import domain.piece.Piece;
import domain.square.Square;


public class BoardFactory {
	public static Board create(BoardSize size, File input) throws FileNotFoundException
	{
		Board board = new Board(size);
		Scanner scanner = new Scanner(input);
		while(scanner.hasNext())
		{
			int index = scanner.nextInt();
			Location location = new Location(index, size);
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
	
	public static void save(Board board, File output) throws FileNotFoundException
	{
		PrintWriter writer;
		try {
			writer = new PrintWriter(output, "UTF-8");
			writer.write(boardToInputFormat(board));
			writer.close();
		} catch (UnsupportedEncodingException e) {
			assert(false);
		}
	}
	
	private static String boardToInputFormat(Board board)
	{
		StringBuilder builder = new StringBuilder();
		
		for(int row=0; row < board.getSize().getRows(); row++)
		{
			for(int col=0; col < board.getSize().getRows(); col++)
			{
				Location location = board.createLocation(row, col);
				if(location.isBlack())
				{
					Square square = board.getSquare(location);
					if(square.hasPiece())
					{
						int index = location.getIndex();
						String paddedIndex = String.format("%2d", index);
						builder.append(paddedIndex);
						builder.append(' ');
						Piece piece = square.getPiece(); 
						builder.append(piece.getPieceCode());
					}
					else
					{
						builder.append("    ");
					}
				}
				else //white square
				{
					builder.append("    ");
				}
			}
			builder.append("\r\n");
		}
		
		return builder.toString();
	}
}
