package app.utils.exceptions;

public class NoSuchLetterInMappingException extends Exception
{
	private static final long serialVersionUID = -1177686693942909134L;

	public NoSuchLetterInMappingException()
	{
		super();
	}
	
	public NoSuchLetterInMappingException(String message) 
	{
		super(message);
	}
	
	public NoSuchLetterInMappingException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NoSuchLetterInMappingException(Throwable cause)
	{
		super(cause);
	}
}
