package domain.board;

import java.util.HashMap;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationPair;
import domain.piece.Dame;
import domain.piece.contracts.IPiece;
import domain.square.SquareBlack;
import domain.square.SquareWhite;
import domain.square.contracts.IReadOnlySquare;
import domain.square.contracts.ISquare;

public class Board implements IBoard
{
	private final ISquare[][] squares;
	private final BoardSize size;
	private final ReadOnlyBoard readOnlyWrapper = new ReadOnlyBoard(this);
	
	public Board(BoardSize size)
	{
		this.size = size;
		this.squares = new ISquare[size.getRows()][size.getCols()];
		
		for(int row=0; row < size.getRows(); row++)
		{
			for(int col=0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				squares[row][col] = createSquare(location);
			}
		}
	}
	
	/**
	 * Copy constructor. Creates a deep clone of the given {@link IReadOnlyBoard}. 
	 * 
	 * @param 	original
	 * 			The original IReadOnlyBoard to base this new Board on.
	 */
	public Board(IReadOnlyBoard original)
	{
		this(original.getSize());
		BoardSize size = getSize();
		for(int row=0; row < size.getRows(); row++)
		{
			for(int col=0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				IReadOnlySquare originalSquare = original.getSquare(location);
				if(originalSquare.hasPiece())
				{
					IPiece originalPiece = originalSquare.getPiece();
					IPiece newPiece = originalPiece.getDeepClone();
					this.addPiece(location, newPiece);
				}
			}
		}
	}
	
	@Override
	public BoardSize getSize()
	{
		return size;
	}
	
	@Override
	public ReadOnlyBoard getReadOnlyBoard()
	{
		return readOnlyWrapper;
	}
	
	private ISquare createSquare(Location location)
	{
		return location.isBlack() ?
			 new SquareBlack() :
			 new SquareWhite();
	}
	
	@Override
	public Location createLocation(int row, int col)
	{
		return new Location(row, col, getSize());
	}
	
	@Override
	public ISquare getSquare(Location location)
	{
		return squares[location.getRow()][location.getCol()];
	}
	
	@Override
	public void addPiece(Location location, IPiece piece)
	{
		ISquare square = getSquare(location);
		if(square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s already contains a piece.", location));
		}
		
		square.setPiece(piece);
	}
	
	@Override
	public void removePiece(Location location)
	{
		ISquare square = getSquare(location);
		if(!square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s does not contains a piece.", location));
		}
		
		square.setPiece(null);
	}
	
	@Override
	public boolean isValidMove(LocationPair pair)
	{
		ISquare fromSquare = getSquare(pair.getFrom());
		ISquare toSquare = getSquare(pair.getTo());
		
		return 	pair.getFrom().isBlack() &&
				pair.getTo().isBlack() &&
				fromSquare.hasPiece() && 
				!toSquare.hasPiece();
	}
	
	@Override
	public void movePiece(LocationPair pair)
	{
		if(!isValidMove(pair))
		{
			throw new IllegalStateException("Invalid move: Square from was empty or Square to was not.");
		}
		
		ISquare fromSquare = getSquare(pair.getFrom());
		IPiece piece = fromSquare.getPiece();
		removePiece(pair.getFrom());
		addPiece(pair.getTo(), piece);
	}
	
	@Override
	public void promotePiece(Location location)
	{
		ISquare square = getSquare(location);
		if(!square.hasPiece())
		{
			throw new IllegalStateException(String.format("Square %s does not contains a piece.", location));
		}
		IPiece piece = square.getPiece();
		Player player = piece.getPlayer();
		if(!location.isOnPromotionRow(player))
		{
			throw new IllegalStateException(String.format("Cannot promote piece of player %s on row %d.", player, location.getRow()));
		}
		Dame dame = new Dame(player);
		removePiece(location);
		addPiece(location, dame);
	}
	
	@Override
	public HashMap<Location, IPiece> getPlayerPieces(Player player)
	{
		HashMap<Location, IPiece> result = new HashMap<Location, IPiece>();
		for(int row=0; row < getSize().getRows(); row++)
		{
			for(int col=0; col < getSize().getRows(); col++)
			{
				Location location = createLocation(row, col);
				ISquare square = getSquare(location);
				if(square.hasPiece())
				{
					IPiece piece = square.getPiece();
					if(piece.getPlayer() == player)
					{
						result.put(location, piece);
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean isLocationFree(Location target)
	{
		ISquare targetSquare = getSquare(target);
		return !targetSquare.hasPiece();
	}
	
	@Override
	public boolean isLocationOccupiedBy(Player occupant, Location target)
	{
		ISquare targetSquare = getSquare(target);
		return targetSquare.hasPiece() && targetSquare.getPiece().getPlayer() == occupant;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(int row=0; row < getSize().getRows(); row++)
		{
			for(int col=0; col < getSize().getRows(); col++)
			{
				Location location = createLocation(row, col);
				if(location.isBlack())
				{
					ISquare square = getSquare(location);
					if(square.hasPiece())
					{
						int index = location.getIndex();
						String paddedIndex = String.format("%2d", index);
						builder.append(paddedIndex);
						builder.append(' ');
						IPiece piece = square.getPiece(); 
						builder.append(piece.getPieceCode());
					}
					else
					{
						builder.append("    "); //don't print o's
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
	
	@Override
	public IBoard getDeepClone()
	{
		return new Board(this);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		if(obj instanceof Board)
		{
			IBoard casted = (IBoard)obj;
			if (!this.getSize().equals(casted.getSize()))
			{
				return false;
			}
			for(int row=0; row < getSize().getRows(); row++)
			{
				for(int col=0; col < getSize().getCols(); col++)
				{
					Location location = new Location(row, col, getSize());
					if(!this.getSquare(location).equals(casted.getSquare(location)))
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	//TODO hash code
}