package domain.input.contracts;

import domain.location.LocationOutOfRangeException;

public interface IInput
{
	public boolean process() throws LocationOutOfRangeException;
}