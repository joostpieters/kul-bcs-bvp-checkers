package ui.contracts;

import common.Player;

public interface IUserInterface
{

	public boolean askYesNo(String message);

	public String askInput(String message);

	public String askActionInput(Player player);

	public void close();

}