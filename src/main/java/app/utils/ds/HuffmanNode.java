package app.utils.ds;

public class HuffmanNode
{
	private HuffmanNode leftChild;
	private HuffmanNode rightChild;
	private HuffmanNode parent;
	private char letter;
	private int frequency;
	
	public HuffmanNode(char letter, int frequency)
	{
		this.letter = letter;
		this.frequency = frequency;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;
	}
	
	public void setParent(HuffmanNode parent)
	{
		this.parent = parent;
	}
	
	public void setLeftChild(HuffmanNode child)
	{
		this.leftChild = child;
		this.frequency += this.leftChild.getFrequency();
		child.setParent(this);
	}
	
	public void setRightChild(HuffmanNode child)
	{
		this.rightChild = child;
		this.frequency += this.rightChild.getFrequency();
		child.setParent(this);
	}
	
	public HuffmanNode getParent()
	{
		return this.parent;
	}
	
	public HuffmanNode getLeftChild()
	{
		return this.leftChild;
	}
	
	public HuffmanNode getRightChild()
	{
		return this.rightChild;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
	
	public int getFrequency()
	{
		return this.frequency;
	}
}