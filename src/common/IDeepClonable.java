package common;

/**
 * Indicates that the implementer offers deep copy functionality.
 * 
 * @param <T> The type to clone.
 */
public interface IDeepClonable<T>
{
	/**
	 * Creates a clone of this T and its fields such that the clone is no longer linked to the original in any way, yet remains equal.
	 * 
	 * @return The aforementioned clone.
	 */
	public T getDeepClone();
}
