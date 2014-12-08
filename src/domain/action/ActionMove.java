package domain.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Location;
import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.square.Square;

public class ActionMove extends Action {
	private static final String regex = "(\\d+)\\s*-\\s*(\\d+)";
	private final int fromIndex;
	private final int toIndex;
	
	public int getFromIndex() {
		return fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}
	
	public Location getFrom(BoardSize size)
	{
		return Location.fromIndex(getFromIndex(), size);
	}
	
	public Location getTo(BoardSize size)
	{
		return Location.fromIndex(getToIndex(), size);
	}

	public ActionMove(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}
	
	public ActionMove(String move)
	{
		Pattern movePattern = Pattern.compile(regex);
		Matcher matcher = movePattern.matcher(move);
		if(!matcher.matches())
		{
			throw new IllegalArgumentException("Invalid move pattern: " + move);
		}
		this.fromIndex = Integer.parseInt(matcher.group(1));
		this.toIndex = Integer.parseInt(matcher.group(2));
	}
	
	public static boolean isValidPattern(String pattern)
	{
		return pattern.matches(regex);
	}

	@Override
	public boolean isValidOn(Board board, Player currentPlayer) {
		BoardSize size = board.getSize();
		if(	fromIndex == toIndex ||
			!size.isValidIndex(fromIndex) ||
			!size.isValidIndex(toIndex))
		{
			return false;
		}
		
		Location from = getFrom(board.getSize());
		Location to = getTo(board.getSize());
		
		if(	!from.isOnSameDiagonal(to) || 
			from.getDiagonalDistance(to) != 1 ||
			!to.isInFrontOf(from, currentPlayer))
		{
			return false;
		}
		
		Square fromSquare = board.getSquare(from);
		Square toSquare = board.getSquare(to);
		
		if(	!fromSquare.hasPiece() || 
			toSquare.hasPiece() ||
			fromSquare.getPiece().getPlayer() != currentPlayer)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public void executeOn(Board board, Player currentPlayer) {
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		Location from = getFrom(board.getSize());
		Location to = getTo(board.getSize());
		board.movePiece(from, to);
	}
	
	@Override
	public String toString() {
		return String.format("Move from square %d to %d", getFromIndex(), getToIndex());
	}
}
