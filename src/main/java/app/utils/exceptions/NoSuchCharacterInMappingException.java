package app.utils.exceptions;

public class NoSuchCharacterInMappingException extends Exception
{
	private static final long serialVersionUID = -1177686693942909134L;

	public NoSuchCharacterInMappingException()
	{
		super();
	}
	
	public NoSuchCharacterInMappingException(String message) 
	{
		super(message);
	}
	
	public NoSuchCharacterInMappingException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NoSuchCharacterInMappingException(Throwable cause)
	{
		super(cause);
	}
}
