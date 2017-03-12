package app.utils.ds;

import app.utils.ds.HuffmanNode;

import org.junit.*;
import static org.junit.Assert.*;

public class HuffmanNodeTest
{	
	@Test public void nodeCreationTest()
	{
		HuffmanNode node = new HuffmanNode('a', 5);
		assertEquals("node should have a letter of 'a'", node.getLetter(), 'a');
		assertEquals("node should have a frequency of 5", node.getFrequency(), 5);
		assertNull("node should not have a left child", node.getLeftChild());
		assertNull("node should not have a right child", node.getRightChild());
		assertNull("node should not have a parent", node.getParent());
	}
	
	@Test public void rootCreationTest()
	{
		HuffmanNode leftChild = new HuffmanNode('a', 5);
		HuffmanNode rightChild = new HuffmanNode('b', 5);
		HuffmanNode root = new HuffmanNode('\0', 0);
		
		root.setLeftChild(leftChild);
		assertEquals("root should have a frequency of 5", root.getFrequency(), 5);
		assertNotNull("root should have a left child", root.getLeftChild());
		assertNotNull("leftChild should have a parent", leftChild.getParent());
		assertSame("leftChild should be the same to the root's left child", root.getLeftChild(), leftChild);
		
		root.setRightChild(rightChild);
		assertEquals("root should not have a frequency of 10", root.getFrequency(), 10);
		assertNotNull("root should have a right child", root.getRightChild());
		assertNotNull("rightChild should have a parent", rightChild.getParent());
		assertSame("rightChild should be the same to the root's right child", root.getRightChild(), rightChild);
		
		leftChild.setParent(null);
		rightChild.setParent(null);
		assertNull("leftChild should have no parent node", leftChild.getParent());
		assertNull("rightChild should have no parent node", rightChild.getParent());
	}
}