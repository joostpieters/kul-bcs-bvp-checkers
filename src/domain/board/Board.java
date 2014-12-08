package domain.board;
import common.Location;
import common.Player;
import domain.piece.Dame;
import domain.piece.Piece;
import domain.square.Square;
import domain.square.SquareBlack;
import domain.square.SquareWhite;

public class Board {
	private final Square[][] squares;
	private final BoardSize size;
	
	public Board(BoardSize size)
	{
		this.size = size;
		this.squares = new Square[size.getRows()][size.getCols()];
		
		for(int row=0; row < size.getRows(); row++)
		{
			for(int col=0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				squares[row][col] = createSquare(location);
			}
		}
	}
	
	public BoardSize getSize() {
		return size;
	}

	private Square createSquare(Location location)
	{
		return location.isBlack() ?
			 new SquareBlack() :
			 new SquareWhite();
	}
	
	public Location createLocation(int row, int col)
	{
		return new Location(row, col, getSize());
	}
	
	public Square getSquare(Location location)
	{
		return squares[location.getRow()][location.getCol()];
	}
	
	public void addPiece(Location location, Piece piece)
	{
		Square square = getSquare(location);
		if(square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s already contains a piece.", location));
		}
		
		square.setPiece(piece);
	}
	
	public void removePiece(Location location)
	{
		Square square = getSquare(location);
		if(!square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s does not contains a piece.", location));
		}
		
		square.setPiece(null);
	}
	
	public void movePiece(Location from, Location to)
	{
		if(from.equals(to))
		{
			throw new IllegalArgumentException("Cannot move Piece to original location.");
		}
		Square fromSquare = getSquare(from);
		Square toSquare = getSquare(to);
		
		if(!fromSquare.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s does not contains a piece.", from));
		}
		if(toSquare.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s already contains a piece.", to));
		}
		Piece piece = fromSquare.getPiece();
		removePiece(from);
		addPiece(to, piece);
	}
	
	public void promotePiece(Location location)
	{
		Square square = getSquare(location);
		if(!square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s does not contains a piece.", location));
		}
		Piece piece = square.getPiece();
		Player player = piece.getPlayer();
		if(!location.isPromotionRow(player))
		{
			throw new IllegalStateException(String.format("Cannot promote piece of player %s on row %d.", player, location.getRow()));
		}
		Dame dame = new Dame(player);
		removePiece(location);
		addPiece(location, dame);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(int row=0; row < getSize().getRows(); row++)
		{
			for(int col=0; col < getSize().getRows(); col++)
			{
				Location location = createLocation(row, col);
				if(location.isBlack())
				{
					int index = location.getIndex();
					String paddedIndex = String.format("%2d", index);
					builder.append(paddedIndex);
					builder.append(':');
					Square square = getSquare(location);
					if(square.hasPiece())
					{
						Piece piece = square.getPiece(); 
						builder.append(piece.getPieceCode());
					}
					else
					{
						builder.append('o');
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
