package domain.input.contracts;

/**
 * The interface of a class that asks the player for input.
 */
public interface IInputProvider
{
	/**
	 * Ask the player for input, and return it as an {@link IInput} object.
	 */
	public IInput askInput();
}