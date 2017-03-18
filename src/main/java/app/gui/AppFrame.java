package app.gui;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JRadioButton inputModeRButton;
	private JRadioButton fileModeRButton;
	private ButtonGroup modeButtonGroup;
	private JButton compressButton;
	private JButton decompressButton;
	private JButton showTreeButton;
	private JButton showCodingButton;
	
	public AppFrame()
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
		this.inputModeRButton = new JRadioButton("Input String");
		this.fileModeRButton = new JRadioButton("Statistical Distribution File");
		this.modeButtonGroup = new ButtonGroup();
		this.compressButton = new JButton("Compress");
		this.decompressButton = new JButton("Decompress");
		this.showTreeButton = new JButton("Show Huffman Tree");
		this.showCodingButton = new JButton("Show Huffman Coding");
		
		this.outputTextField.setEditable(false);
		this.compressButton.setEnabled(false);
		this.decompressButton.setEnabled(false);
		this.showTreeButton.setEnabled(false);
		this.showCodingButton.setEnabled(false);
		
		this.setLayout(guiLayout);

		this.targetTextPanel.add(targetTextLabel);
		this.outputTextPanel.add(outputTextLabel);
		this.modeMessagePanel.add(modeMessageLabel);
		
		this.modePanel.add(compressionModeLabel);
		this.modePanel.add(inputModeRButton);
		this.modePanel.add(fileModeRButton);
		
		this.buttonPanel.add(compressButton);
		this.buttonPanel.add(decompressButton);
		this.buttonPanel.add(showTreeButton);
		this.buttonPanel.add(showCodingButton);
		
		this.modeButtonGroup.add(inputModeRButton);
		this.modeButtonGroup.add(fileModeRButton);
		
		this.add(targetTextPanel);
		this.add(targetTextField);
		this.add(modePanel);
		this.add(buttonPanel);
		this.add(modeMessagePanel);
		this.add(outputTextPanel);
		this.add(outputTextField);
		
		this.fileModeRButton.addActionListener(this);
		this.inputModeRButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == this.fileModeRButton) {
			this.modeMessageLabel.setText("Note: This mode requires you to select a statistical distribution file when you click 'Compress'.");
		} else if (event.getSource() == this.inputModeRButton) {
			this.compressButton.setEnabled(false);
			this.modeMessageLabel.setText(" ");
		}
	}
}
