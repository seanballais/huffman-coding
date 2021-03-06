package app.gui;

import app.utils.ds.HuffmanTree;
import app.utils.ds.HuffmanNode;
import app.utils.exceptions.FileFormatException;
import app.utils.exceptions.NoSuchCharacterInMappingException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 4335591738512431457L;
	private BoxLayout guiLayout;
	private JPanel buttonPanel;
	private JPanel modePanel;
	private JPanel targetTextPanel;
	private JPanel outputTextPanel;
	private JPanel modeMessagePanel;
	private JLabel targetTextLabel;
	private JLabel outputTextLabel;
	private JLabel compressionModeLabel;
	private JLabel modeMessageLabel;
	private JTextField targetTextField;
	private JTextField outputTextField;
	private JScrollPane outputTextScrollPane;
	private JRadioButton inputModeRButton;
	private JRadioButton fileModeRButton;
	private ButtonGroup modeButtonGroup;
	private JButton compressButton;
	private JButton decompressButton;
	private JButton showTreeButton;
	private JButton showCodingButton;
	private JButton resetButton;
	
	private HuffmanTree tree;
	private JTable huffmanCodingTable;
	
	public AppFrame()
	{
		this.setUpGUI();
		
		this.tree = new HuffmanTree();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == this.fileModeRButton) {
			this.modeMessageLabel.setText("Note: This mode requires you to select a statistical distribution file when you click 'Compress'.");
			updateCompressButton();
		} else if (event.getSource() == this.inputModeRButton) {
			this.modeMessageLabel.setText(" ");
			updateCompressButton();
		} else if (event.getSource() == this.compressButton) {
			this.compressText(this.targetTextField.getText());
		} else if (event.getSource() == this.decompressButton) {
			this.decompressText(this.targetTextField.getText());
		} else if (event.getSource() == this.showTreeButton) {
			this.showTree();
		} else if (event.getSource() == this.showCodingButton) {
			this.showCoding();
		} else if (event.getSource() == this.resetButton) {
			this.resetTree();
		}
	}
	
	private void compressText(String targetText)
	{
		this.fileModeRButton.setEnabled(false);
		this.inputModeRButton.setEnabled(false);
		
		String compressedText = "";
		if (this.inputModeRButton.isSelected()) {
			try {
				compressedText = tree.compressFromString(targetText);
			} catch (NoSuchCharacterInMappingException nex) {
				JOptionPane.showMessageDialog(null, "<html>Your input string contains characters that do not have a corresponding Huffman code. We suggest regenerating<br>the Huffman tree and coding by clicking 'Reset Tree' and then 'Compress'.</html>", "An error while compressing text", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (this.fileModeRButton.isSelected()) {
			try {
				if (!this.tree.hasCoding()) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileNameExtensionFilter(".TXT files", "txt"));
					
					fileChooser.showOpenDialog(this);
					File statFile = fileChooser.getSelectedFile();
					if (statFile == null) {
						JOptionPane.showMessageDialog(null, "You must select a file that contains a statistical distribution of characters.", "An error occurred while compressing text", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					compressedText = tree.compressFromFile(targetText, statFile.getAbsolutePath());
				} else {
					compressedText = tree.compressFromFile(targetText, "");	
				}
			} catch (IOException iex) {
				JOptionPane.showMessageDialog(null, "<html>Oops! We had a problem retrieving the selected file. Make sure it<br>exists and you have permissions to access the file.", "An error while compressing text", JOptionPane.ERROR_MESSAGE);
				this.resetTree();
				return;
			} catch (FileFormatException ffex) {
				JOptionPane.showMessageDialog(null, "<html>" + ffex.getMessage() + " Make sure each line in the file follows the format:<br>[character (there must only be one here)]-[frequency (must be an integer)].</html>", "An error while compressing text", JOptionPane.ERROR_MESSAGE);
				this.resetTree();
				return;
			} catch (NoSuchCharacterInMappingException nex) {
				JOptionPane.showMessageDialog(null, "<html>Your input string contains characters that do not have a corresponding Huffman code<br>from the selected file. We suggest adding the anomalous character to your statistical<br>distribution file and click 'Reset Tree', or remove the anomalous character<br>from the input string and click 'Compress' again.</html>", "An error while compressing text", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		this.resetButton.setEnabled(true);
		this.showCodingButton.setEnabled(true);
		this.showTreeButton.setEnabled(true);
		this.modeMessageLabel.setText(" ");
		this.outputTextField.setCaretPosition(0);		
		this.outputTextField.setText(compressedText);
	}
	
	private void decompressText(String compressedText)
	{
		this.outputTextField.setText(this.tree.decompress(compressedText));
	}
	
	private void showTree()
	{
		Graph huffmanTree = new SingleGraph("Generated Huffman Tree");
		SpriteManager spriteManager = new SpriteManager(huffmanTree);
		huffmanTree.addAttribute("ui.quality");
		huffmanTree.addAttribute("ui.antialias");
		huffmanTree.addAttribute("ui.title", "Generated Huffman Tree");
		
		Viewer treeViewer = huffmanTree.display();
		HierarchicalLayout hLayout = new HierarchicalLayout();
		treeViewer.enableAutoLayout(hLayout);
		treeViewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		
		huffmanTree = this.addNodesToGraph(this.tree.getTreeRoot(), spriteManager, huffmanTree, 0, "", true);
	}
	
	private Graph addNodesToGraph(HuffmanNode currentNode, SpriteManager spriteManager, Graph tree, int nodeNum, String prevNodeID, boolean isRoot)
	{
		String id = UUID.randomUUID().toString();

		String label = "";
		char letter = currentNode.getLetter();
		int frequency = currentNode.getFrequency();		
		if (letter == '\0') {
			if (isRoot) { // The root will always have '\0' as its 'letter'.
				label = "(root) ";
				isRoot = false;
			}
			label += "Frequency: " + frequency;
		} else {
			label = "Character: '" + letter + "' | Frequency: " + frequency;
		}
		
		Node node = tree.addNode(id);
		node.addAttribute("ui.label", label);
		Sprite sprite = spriteManager.addSprite(id);
		sprite.attachToNode(id);
		
		if (!prevNodeID.equals("")) {
			String edgeID = prevNodeID + UUID.randomUUID().toString();
			Edge edge = tree.addEdge(edgeID, id, prevNodeID);
			
			if (currentNode.getParent().getLeftChild() == currentNode) {
				edge.addAttribute("ui.label", "0");
			} else if (currentNode.getParent().getRightChild() == currentNode) {
				edge.addAttribute("ui.label", "1");
			}
		}
		
		if (currentNode.getLeftChild() != null) {
			this.addNodesToGraph(currentNode.getLeftChild(), spriteManager, tree, nodeNum++, id, false);
		}
		
		if (currentNode.getRightChild() != null) {
			this.addNodesToGraph(currentNode.getRightChild(), spriteManager, tree, nodeNum++, id, false);
		}
		
		return tree;
	}
	
	private void showCoding()
	{
		if (this.huffmanCodingTable == null) {
			String[] columnNames = {
				"Character",
				"Huffman Coding"
			};
			DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
				private static final long serialVersionUID = -8842243359392592203L;

				@Override public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
						
			this.populateTable(tableModel);
			tableModel.removeRow(0);;
			
			this.huffmanCodingTable = new JTable(tableModel);			
		}
		
		this.displayCodingDialog(this.huffmanCodingTable);
	}
	
	private void populateTable(DefaultTableModel tableModel)
	{
		HashMap<Character, String> coding = this.tree.getCoding();
		for (Map.Entry<Character, String> entry : coding.entrySet()) {
			String characterColumnData = "";
			char key = entry.getKey();
			
			/*
			 *  Add these character checks in case the user adds a character
			 *  that many any of the cases below.
			 */
			if (key == ' ') {
				characterColumnData = "(space)";
			} else if (key == '\t') {
				characterColumnData = "(horizontal tabulation)";
			} else if (key == '\u00A0') {
				characterColumnData = "(space separator)";
			} else if (key == '\u2007') {
				characterColumnData = "(line separator)";
			} else if (key == '\u202F') {
				characterColumnData = "(paragraph separator)";
			} else if (key == '\n') {
				characterColumnData = "(new line)";
			} else if (key == '\u000B') {
				characterColumnData = "(vertical tabulation)";
			} else if (key == '\f') {
				characterColumnData = "(form feed)";
			} else if (key == '\r') {
				characterColumnData = "(carriage return)";
			} else if (key == '\u001C') {
				characterColumnData = "(file separator)";
			} else if (key == '\u001D') {
				characterColumnData = "(group separator)";
			} else if (key == '\u001E') {
				characterColumnData = "(record separator)";
			} else if (key == '\u001F') {
				characterColumnData = "(unit separator)";
			} else {
				characterColumnData = String.valueOf(key);
			}
			
			Object[] rowData = {
				characterColumnData,
				entry.getValue()
			};
			tableModel.addRow(rowData);
		}
	}
	
	private void resetTree()
	{
		this.tree.cleanup();
		this.resetButton.setEnabled(false);
		this.showCodingButton.setEnabled(false);
		this.compressButton.setEnabled(false);
		this.decompressButton.setEnabled(false);
		this.showTreeButton.setEnabled(false);
		this.showCodingButton.setEnabled(false);
		this.fileModeRButton.setEnabled(true);
		this.inputModeRButton.setEnabled(true);
		this.modeButtonGroup.clearSelection();
		this.modeMessageLabel.setText(" ");
		this.outputTextField.setText("");
		
		this.huffmanCodingTable = null;
	}
	
	private void displayCodingDialog(JTable huffmanTable)
	{		
		HuffmanCodeDialog huffmanCodeDialog = new HuffmanCodeDialog(huffmanTable);
		huffmanCodeDialog.setVisible(true);
	}
	
	private void setUpGUI()
	{
		this.initComponents();
		this.addComponentsToFrame();
		this.setComponentProperties();
		this.setUpListeners();
	}
	
	private void initComponents()
	{
		this.guiLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.targetTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.outputTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.modeMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.targetTextLabel = new JLabel("Target text");
		this.outputTextLabel = new JLabel("Output");
		this.compressionModeLabel = new JLabel("Compression Mode:");
		this.modeMessageLabel = new JLabel(" ");
		this.targetTextField = new JTextField();
		this.outputTextField = new JTextField();
		this.outputTextScrollPane = new JScrollPane(this.outputTextField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.inputModeRButton = new JRadioButton("Input String");
		this.fileModeRButton = new JRadioButton("Statistical Distribution File");
		this.modeButtonGroup = new ButtonGroup();
		this.compressButton = new JButton("Compress");
		this.decompressButton = new JButton("Decompress");
		this.showTreeButton = new JButton("Show Huffman Tree");
		this.showCodingButton = new JButton("Show Huffman Coding");
		this.resetButton = new JButton("Reset Tree");
	}
	
	private void setComponentProperties()
	{
		this.outputTextField.setEditable(false);
		this.compressButton.setEnabled(false);
		this.decompressButton.setEnabled(false);
		this.showTreeButton.setEnabled(false);
		this.showCodingButton.setEnabled(false);
		this.resetButton.setEnabled(false);
	}
	
	private void addComponentsToFrame()
	{
		this.setLayout(this.guiLayout);

		this.targetTextPanel.add(this.targetTextLabel);
		this.outputTextPanel.add(this.outputTextLabel);
		this.modeMessagePanel.add(this.modeMessageLabel);
		
		this.modePanel.add(this.compressionModeLabel);
		this.modePanel.add(this.inputModeRButton);
		this.modePanel.add(this.fileModeRButton);
		
		this.buttonPanel.add(this.compressButton);
		this.buttonPanel.add(this.decompressButton);
		this.buttonPanel.add(this.showTreeButton);
		this.buttonPanel.add(this.showCodingButton);
		this.buttonPanel.add(this.resetButton);
		
		this.modeButtonGroup.add(this.inputModeRButton);
		this.modeButtonGroup.add(this.fileModeRButton);
		
		this.add(this.targetTextPanel);
		this.add(this.targetTextField);
		this.add(this.modePanel);
		this.add(this.buttonPanel);
		this.add(this.modeMessagePanel);
		this.add(this.outputTextPanel);
		this.add(this.outputTextField);
		this.add(this.outputTextScrollPane);
	}
	
	private void setUpListeners()
	{
		this.fileModeRButton.addActionListener(this);
		this.inputModeRButton.addActionListener(this);
		this.compressButton.addActionListener(this);
		this.decompressButton.addActionListener(this);
		this.resetButton.addActionListener(this);
		this.showTreeButton.addActionListener(this);
		this.showCodingButton.addActionListener(this);
		
		this.targetTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateCompressButton();
				updateDecompressButton();
			}
			
			public void removeUpdate(DocumentEvent e) {
				updateCompressButton();
				updateDecompressButton();
			}
			
			public void insertUpdate(DocumentEvent e) {
				updateCompressButton();
				updateDecompressButton();
			}
			
			private void updateCompressButton()
			{
				if (!targetTextField.getText().equals("") && modeButtonGroup.getSelection() != null) {
					compressButton.setEnabled(true);
				} else {
					compressButton.setEnabled(false);
				}
			}
			
			private void updateDecompressButton()
			{
				if (!targetTextField.getText().equals("") && targetTextField.getText().matches("^([01]+)$") && tree.hasCoding()) {
					decompressButton.setEnabled(true);
				} else {
					decompressButton.setEnabled(false);
				}
			}
		});
	}
	
	private void updateCompressButton()
	{
		if (!targetTextField.getText().equals("") && modeButtonGroup.getSelection() != null) {
			compressButton.setEnabled(true);
		} else {
			compressButton.setEnabled(false);
		}
	}
	
	private class HuffmanCodeDialog extends JDialog
	{
		private static final long serialVersionUID = -2462417535194452642L;

		public HuffmanCodeDialog(JTable huffmanTable)
		{
			this.setLocationRelativeTo(null);
			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(huffmanTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			this.pack();
			this.setTitle("Huffman Coding");
		}
	}
}
