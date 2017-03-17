package app.utils.exceptions;

public class FileFormatException extends Exception
{
	private static final long serialVersionUID = -5021290194238151895L;

	public FileFormatException()
	{
		super();
	}
	
	public FileFormatException(String message) 
	{
		super(message);
	}
	
	public FileFormatException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public FileFormatException(Throwable cause)
	{
		super(cause);
	}
}
