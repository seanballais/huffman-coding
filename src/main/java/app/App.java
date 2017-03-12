package app;

import app.utils.ds.HuffmanTree;

public class App
{
	public static void main(String[] args)
	{
		HuffmanTree tree = new HuffmanTree();
		
		System.out.println(tree.decompress(tree.compress("jeff_is_awesome")));
	}
}