package app.utils.ds;

import app.utils.ds.HuffmanTree;

import org.junit.*;
import static org.junit.Assert.*;

public class HuffmanTreeTest
{
	@Test public void compressionDecompressionTest()
	{
		HuffmanTree tree = new HuffmanTree();
		assertEquals(
			"Huffman Tree must return bitstring of '111100110110010111010101010001001001010111011000'",
			"111100110110010111010101010001001001010111011000",
			tree.compressFromString("jeff_is_awesome")
		);
		assertEquals(
			"Huffman Tree must return a decompressed text that is equal to the original text.",
			"jeff_is_awesome",
			tree.decompress(tree.compressFromString("jeff_is_awesome"))
		);
	}
}