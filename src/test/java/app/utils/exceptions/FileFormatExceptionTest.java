package app.utils.exceptions;

import app.utils.exceptions.FileFormatExceptionTest;

import org.junit.*;
import static org.junit.Assert.*;

public class FileFormatExceptionTest
{
	@Test public void FileFormatExceptionThrown()
	{
		try {
			throw new FileFormatException("Incorrect file format.");
		} catch (FileFormatException ex) {
			assertEquals(ex.getMessage(), "Incorrenct file format.");
		}
	}
}
