package domain.piece.contracts;

import common.Player;

public interface IPiece {

	public abstract Player getPlayer();

	public abstract char getPieceCode();

	public abstract boolean canStepBackward();

	public abstract boolean canCatchBackward();

	public abstract boolean canFly();

	public abstract boolean canPromote();

	public abstract IPiece getClone();
}