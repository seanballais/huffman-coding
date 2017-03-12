package app.utils.ds;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;

public class HuffmanTree
{
	private HuffmanNode root;
	
	public HuffmanTree()
	{
		// Not planning to initialize the target text here cause that would mean that
		// the text to be compressed is static. However, the text can be changed depending
		// on the input. Input on this will be appreciated.

		this.root = new HuffmanNode('\0', 0);
	}
	
	public String compress(String text)
	{
		HashMap<Character, String> characterMapping = this.buildCoding(text);
		
		String compressedText = "";
		for (char c : text.toCharArray()) {
			compressedText += characterMapping.get(c);
		}
		
		return compressedText;
	}
	
	public String decompress(String encoding)
	{
		String decompressedText = "";
		HuffmanNode currentNode = this.root;
		for (char c: encoding.toCharArray()) {
			if (currentNode.getLetter() == '\0') {
				// Still not in a leaf
				if (c == '0') {
					currentNode = currentNode.getLeftChild();
					if (currentNode.getLetter() != '\0') {
						decompressedText += currentNode.getLetter();
						currentNode = this.root;
					}
				} else if (c == '1') {
					currentNode = currentNode.getRightChild();
					if (currentNode.getLetter() != '\0') {
						decompressedText += currentNode.getLetter();
						currentNode = this.root;
					}
				}
			}
		}

		return decompressedText;
	}

	private HashMap<Character, Integer> getCharacterFrequency(String targetText)
	{
		HashMap<Character, Integer> frequencyStat = new HashMap<Character, Integer>();
		
		for (char c : targetText.toCharArray()) {
			int count = frequencyStat.containsKey(c) ? frequencyStat.get(c) : 0;
			frequencyStat.put(c, count + 1);
		}
		
		return frequencyStat;
	}
	
	private PriorityQueue<HuffmanNode> createQueue(HashMap<Character, Integer> characterFrequency)
	{
		int initCapacity = characterFrequency.size();
		Comparator<HuffmanNode> comparator = new Comparator<HuffmanNode>() {
			@Override public int compare(HuffmanNode h1, HuffmanNode h2) {
				return (int) (h1.getFrequency() - h2.getFrequency());
			}
		};
		PriorityQueue<HuffmanNode> nodes = new PriorityQueue<HuffmanNode>(initCapacity, comparator);
		
		for (HashMap.Entry<Character, Integer> entry : characterFrequency.entrySet()) {
			char letter = entry.getKey();
			int frequency = entry.getValue();
			
			nodes.add(new HuffmanNode(letter, frequency));
		}
		
		return nodes;
	}
	
	private HuffmanNode createSubtree(HuffmanNode parent, HuffmanNode leftChild, HuffmanNode rightChild)
	{
		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);
		
		leftChild.setParent(parent);
		rightChild.setParent(parent);
		
		return parent;
	}
	
	private HashMap<Character, String> getCharacterMapping(HashMap<Character, String> mapping, HuffmanNode node, String code)
	{
		if (node.getLetter() == '\0') {
			getCharacterMapping(mapping, node.getLeftChild(), code + "0");
			getCharacterMapping(mapping, node.getRightChild(), code + "1");
		}
		
		// Found the corresponding code for a character.
		mapping.put(node.getLetter(), code);
		
		return mapping;
	}
	
	private HashMap<Character, String> buildCoding(String targetText)
	{
		HashMap<Character, Integer> characterFrequency = this.getCharacterFrequency(targetText);
		PriorityQueue<HuffmanNode> nodes = this.createQueue(characterFrequency);
		
		while (nodes.size() != 1) {
			// Pop all nodes until there is only one element in the queue
			// left which is the resulting Huffman Tree.
			HuffmanNode parent = new HuffmanNode('\0', 0);
			HuffmanNode leftChild = nodes.poll();
			HuffmanNode rightChild = nodes.poll();
			
			nodes.add(this.createSubtree(parent, leftChild, rightChild));
		}
		
		this.root = nodes.poll(); // Get the last remaining node in the queue which is the resulting Huffman Tree\

		return this.getCharacterMapping(new HashMap<Character, String>(), this.root, "");
	}
}