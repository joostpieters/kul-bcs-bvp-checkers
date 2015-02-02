package domain.input.contracts;

import domain.location.LocationOutOfRangeException;

/**
 * This interface represents one specific kind of input the player can enter. 
 */
public interface IInput
{
	/**
	 * Processes this input.
	 * @return	true if successful, false otherwise.
	 * @throws 	LocationOutOfRangeException
	 * 			When the input included invalid locations.
	 */
	public boolean process() throws LocationOutOfRangeException;
}