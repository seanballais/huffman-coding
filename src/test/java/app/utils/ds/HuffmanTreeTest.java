package app.utils.ds;

import app.utils.ds.HuffmanTree;
import app.utils.exceptions.FileFormatException;
import app.utils.exceptions.NoSuchCharacterInMappingException;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

public class HuffmanTreeTest
{
	@Test public void compressionDecompressionStringTest()
	{
		HuffmanTree tree = new HuffmanTree();
		String testText = "jeff_is_Awesome";
		
		String compressedText = "";
		try {
			compressedText = tree.compressFromString(testText);
		} catch (NoSuchCharacterInMappingException nex) {
			fail("Huffman tree should be able to compress the string without any problems.");
		}
		
		assertEquals(
			"Huffman Tree must return bitstring of '111100110110010111010101010001001001010111011000'",
			"111100110110010111010101010001001001010111011000",
			compressedText
		);

		assertEquals(
			"Huffman Tree must return a decompressed text that is equal to the original text.",
			"jeff_is_awesome",
			tree.decompress(compressedText)
		);
		
		try {
			testText = "zebra";
			compressedText = tree.compressFromString(testText);
			fail("Huffman tree should throw an exception because 'z', 'b', and, 'r' are not in the current coding.");
		} catch (NoSuchCharacterInMappingException nex) {
			assertEquals(nex.getMessage(), "Character 'z' does not appear in Huffman mapping");
		}
	}
	
	@Test public void compressionDecompressionFileText()
	{
		HuffmanTree tree = new HuffmanTree();
		String testText = "jeff_is_Awesome";
		URL fileURL = this.getClass().getResource("/txt/stat.txt");
		String filePath = fileURL.getPath().replaceAll("%20", "\\ ");
		String compressedText = "";
		
		try {
			compressedText = tree.compressFromFile(testText, filePath);
		} catch (IOException iex) {
			fail("File does not exist or you have no permission to access " + iex.getMessage());
		} catch (FileFormatException ffex) {
			fail(ffex.getMessage());
		} catch (NoSuchCharacterInMappingException nex) {
			fail("Huffman tree should be able to compress the string without any problems.");
		}
		
		assertEquals(
			"Huffman Tree must return bitstring of '1000'",
			"0011010000110010100101001101010011001001001101010011101111010111001110100111011",
			compressedText
		);
		assertEquals(
			"Huffman Tree must return a decompressed text that is equal to the original text.",
			"jeff_is_awesome",
			tree.decompress(compressedText)
		);
		
		try {
			testText = "!";
			compressedText = tree.compressFromString(testText);
			fail("Huffman tree should throw an exception because '!' is not in the current coding.");
		} catch (NoSuchCharacterInMappingException nex) {
			assertEquals(nex.getMessage(), "Character '!' does not appear in Huffman mapping");
		}
	}
	
	@Test public void contentFormatIncorrectTest()
	{
		HuffmanTree tree = new HuffmanTree();
		String testText = "jeff_is_awesome";
		
		URL testFile1Path = this.getClass().getResource("/txt/incorrect-format.txt");
		URL testFile2Path = this.getClass().getResource("/txt/incorrect-format2.txt");
		URL testFile3Path = this.getClass().getResource("/txt/incorrect-format3.txt");
		URL testFile4Path = this.getClass().getResource("/txt/incorrect-format4.txt");
		URL testFile5Path = this.getClass().getResource("/txt/incorrect-format5.txt");
		
		try {
			tree.compressFromFile(testText, ensureSpaces(testFile1Path.getPath()));
			tree.compressFromFile(testText, ensureSpaces(testFile2Path.getPath()));
			tree.compressFromFile(testText, ensureSpaces(testFile3Path.getPath()));
			tree.compressFromFile(testText, ensureSpaces(testFile4Path.getPath()));
			tree.compressFromFile(testText, ensureSpaces(testFile5Path.getPath()));
			
			fail("File format is incorrect. This should not fail.");
		} catch (IOException iex) {
			fail("File does not exist or you have no permission to access " + iex.getMessage());
		} catch (FileFormatException ffex) {
			assertEquals(ffex.getMessage(), "Incorrect format in line 1 in the given file.");
		} catch (NoSuchCharacterInMappingException nex) {
			fail("Huffman tree should be able to compress the string without any problems.");
		}
	}
	
	private String ensureSpaces(String text)
	{
		return text.replaceAll("%20", "\\ ");
	}
}