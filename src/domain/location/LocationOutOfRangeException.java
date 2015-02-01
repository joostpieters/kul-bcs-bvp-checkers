package domain.location;

public class LocationOutOfRangeException extends Exception
{
	private static final long serialVersionUID = 3691225736394551268L;

	public LocationOutOfRangeException()
	{
		super();
	}

	public LocationOutOfRangeException(String message)
	{
		super(message);
	}

	public LocationOutOfRangeException(Throwable cause)
	{
		super(cause);
	}

	public LocationOutOfRangeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public LocationOutOfRangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
