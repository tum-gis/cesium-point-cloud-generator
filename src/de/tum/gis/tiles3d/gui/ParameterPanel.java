package de.tum.gis.tiles3d.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.citydb.util.gui.GuiUtil;

import de.tum.gis.tiles3d.database.sqlite.SqliteDBManagerFactory;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.generator.PntcGenerationException;
import de.tum.gis.tiles3d.generator.PntcGenerator;
import de.tum.gis.tiles3d.util.Logger;

@SuppressWarnings("serial")
public class ParameterPanel extends JPanel {

	protected static final int BT = 3;
	private Box containerPanel;
	private JPanel inputPanel;
	private JTextField inputBrowseTextField = new JTextField();
	private JButton inputBrowseButton = new JButton();

	private JPanel sridPanel;
	private JTextField sridInputField = new JTextField();
	
	private JPanel SeparatorCharacterPanel;
	private JTextField SeparatorCharacterInputField = new JTextField();
	
	private JPanel colorBitSizePanel;
	private JTextField colorBitSizeInputField = new JTextField();
	
	private JPanel zScaleFactorPanel;
	private JTextField zScaleFactorInputField = new JTextField();
	
	private JPanel zOffsetPanel;
	private JTextField zOffsetInputField = new JTextField();
	
	private JPanel tileSizePanel;
	private JTextField tileSizeInputField = new JTextField();

	private JPanel maxNumOfPointsPerTilePanel;
	private JTextField maxNumOfPointsPerTileInputField = new JTextField();
	
	private JPanel outputPanel;
	private JTextField outputBrowserTextField = new JTextField();
	private JButton outputBrowseButton = new JButton();
	
	private JButton runButton = new JButton();
	
	private JFrame parentFrame;
	
	public ParameterPanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		initGui();
		setLabels();
		addListeners();
		initDefaultSettings();
	}

	private void initGui() {
		containerPanel = Box.createVerticalBox();
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// input panel
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.add(inputBrowseTextField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		inputBrowseTextField.setColumns(10);
		inputPanel.add(inputBrowseButton, GuiUtil.setConstraints(3, 0, 0.0, 0.0, GridBagConstraints.NONE, BT, BT, BT, BT));
		containerPanel.add(inputPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));

		// srid panel
		sridPanel = new JPanel();
		sridPanel.setLayout(new GridBagLayout());
		sridPanel.add(sridInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		sridInputField.setColumns(10);
		containerPanel.add(sridPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));

		// SeparatorCharacter Panel
		SeparatorCharacterPanel = new JPanel();
		SeparatorCharacterPanel.setLayout(new GridBagLayout());
		SeparatorCharacterPanel.add(SeparatorCharacterInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		SeparatorCharacterInputField.setColumns(10);
		containerPanel.add(SeparatorCharacterPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// 8/16 bit color panel
		colorBitSizePanel = new JPanel();
		colorBitSizePanel.setLayout(new GridBagLayout());
		colorBitSizePanel.add(colorBitSizeInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		colorBitSizeInputField.setColumns(10);
		containerPanel.add(colorBitSizePanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// zOffset Panel
		zOffsetPanel = new JPanel();
		zOffsetPanel.setLayout(new GridBagLayout());
		zOffsetPanel.add(zOffsetInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		zOffsetInputField.setColumns(10);
		containerPanel.add(zOffsetPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));				
				
		// scale factor Panel
		zScaleFactorPanel = new JPanel();
		zScaleFactorPanel.setLayout(new GridBagLayout());
		zScaleFactorPanel.add(zScaleFactorInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		zScaleFactorInputField.setColumns(10);
		containerPanel.add(zScaleFactorPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));				
		
		// tileSize panel
		tileSizePanel = new JPanel();
		tileSizePanel.setLayout(new GridBagLayout());
		tileSizePanel.add(tileSizeInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		tileSizeInputField.setColumns(10);
		containerPanel.add(tileSizePanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// maxNumOfPointsPerTile panel
		maxNumOfPointsPerTilePanel = new JPanel();
		maxNumOfPointsPerTilePanel.setLayout(new GridBagLayout());
		maxNumOfPointsPerTilePanel.add(maxNumOfPointsPerTileInputField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		maxNumOfPointsPerTileInputField.setColumns(10);
		containerPanel.add(maxNumOfPointsPerTilePanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// output Panel
		outputPanel = new JPanel();
		outputPanel.setLayout(new GridBagLayout());
		outputPanel.add(outputBrowserTextField, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.HORIZONTAL, BT, BT * 6, BT, BT));
		outputBrowserTextField.setColumns(10);
		outputPanel.add(outputBrowseButton, GuiUtil.setConstraints(3, 0, 0.0, 0.0, GridBagConstraints.NONE, BT,BT, BT, BT));

		containerPanel.add(outputPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, BT * 6)));
		
		// run button
		JPanel runButtonPanel = new JPanel();
		runButtonPanel.setLayout(new GridBagLayout());
		runButtonPanel.add(runButton, GuiUtil.setConstraints(0, 0, 1.0, 0.0, GridBagConstraints.NONE, 5, 5, 5, 5));
		containerPanel.add(runButtonPanel);
		
		JPanel dummyPanel = new JPanel(new BorderLayout());
		dummyPanel.add(containerPanel, BorderLayout.NORTH);

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(BT, BT, BT, BT));
		this.add(dummyPanel);
	}

	public void setLabels() {		
		inputPanel.setBorder(BorderFactory.createTitledBorder("Path of the input directory which contains the source data"));
		inputBrowseButton.setText("Browse");		
		sridPanel.setBorder(BorderFactory.createTitledBorder("Spatial Reference Identifier (SRID) of the source data"));
		SeparatorCharacterPanel.setBorder(BorderFactory.createTitledBorder("Separator Character in the source data (blank will be interpreted as a 'whitespace')"));
		colorBitSizePanel.setBorder(BorderFactory.createTitledBorder("8- or 16-bit color used in the source data. Enter '8' or '16' accordinglly"));
		zScaleFactorPanel.setBorder(BorderFactory.createTitledBorder("Scale factor (0.0 - 1.0) for scaling the height of generated point cloud (blank will be interpreted as 1)"));
		zOffsetPanel.setBorder(BorderFactory.createTitledBorder("zOffset for adjusting the height of generated point cloud (blank will be interpreted as 0)"));
		tileSizePanel.setBorder(BorderFactory.createTitledBorder("The side length of tile on the highest LoD (default value 100 will be used if input is blank)"));	
		maxNumOfPointsPerTilePanel.setBorder(BorderFactory.createTitledBorder("Maximum number of points of each tile on lower LODs (default value 5000 will be used if input is blank)"));
		outputPanel.setBorder(BorderFactory.createTitledBorder("Path of the output directory which will contain the generated point cloud tilesets"));
		outputBrowseButton.setText("Browse");		
		runButton.setText("Export");
	}

	private void addListeners() {
		inputBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						doProcess();
					}
				};
				thread.start();
			}
		});
		
		inputBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Select source data folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File(inputBrowseTextField.getText()).getParentFile());
				int result = chooser.showOpenDialog(getTopLevelAncestor());
				if (result == JFileChooser.CANCEL_OPTION) 
					return;
				String browseString = chooser.getSelectedFile().toString();
				inputBrowseTextField.setText(browseString);
			}
		});
		
		outputBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Select output folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File(outputBrowserTextField.getText()).getParentFile());
				int result = chooser.showSaveDialog(getTopLevelAncestor());
				if (result == JFileChooser.CANCEL_OPTION) 
					return;
				String browseString = chooser.getSelectedFile().toString();
				outputBrowserTextField.setText(browseString);
			}
		});
	}

	private void doProcess() {
		String inputPath = inputBrowseTextField.getText();
		if (inputPath.trim().equals("")) {
			errorMessage("Incorrect parameter value", "Please enter a valid folder path");
			return;
		}
		
		String srid = sridInputField.getText();
		if (srid.trim().equals("")) {
			errorMessage("Incorrect parameter value", "Please enter a valid SRID value");
			return;
		}
		
		String separatorCharacter = SeparatorCharacterInputField.getText().trim();
		if (separatorCharacter.trim().equals("")) {
			separatorCharacter = " ";
		}
		
		int colorBitSize;
		String colorBitSizeText = colorBitSizeInputField.getText().trim();
		if (colorBitSizeText.equals("8") || colorBitSizeText.equals("16")) {
			colorBitSize = Integer.valueOf(colorBitSizeText);
		}
		else {
			errorMessage("Incorrect parameter value", "Invalid color bit size, it must be 8 or 16");
			return;
		}			
		
		double zScaleFactor = 1;
		if (zScaleFactorInputField.getText().trim() != "") {
			try {
				zScaleFactor = Double.parseDouble(zScaleFactorInputField.getText().trim());
				if (zScaleFactor > 1 || zScaleFactor < 0) {
					errorMessage("Incorrect parameter value", "Invalid Z scale factor value, it must be a value in the range of (0.0 - 1.0)");
					return;
				}
			}
			catch (NumberFormatException nfe) {
				errorMessage("Incorrect parameter value", "Invalid scale factor value");
				return;
			}
		}

		double zOffset = 0;
		if (zOffsetInputField.getText().trim() != "") {
			try {
				zOffset = Double.parseDouble(zOffsetInputField.getText().trim());
			}
			catch (NumberFormatException nfe) {
				errorMessage("Incorrect parameter value", "Invalid zOffset value");
				return;
			}
		}		
		
		double tileSize = 100;
		if (tileSizeInputField.getText().trim() != "") {
			try {
				tileSize = Double.parseDouble(tileSizeInputField.getText().trim());
				if (tileSize <= 0) {
					errorMessage("Incorrect parameter value", "tile size value must bigger than 0");
					return;
				}
			}
			catch (NumberFormatException nfe) {
				errorMessage("Incorrect parameter value", "Invalid tile size value");
				return;
			}
		}	
		
		int maxNumOfPointsPerTile = 5000;
		if (maxNumOfPointsPerTileInputField.getText().trim() != "") {
			try {
				maxNumOfPointsPerTile = Integer.parseInt(maxNumOfPointsPerTileInputField.getText().trim());
				if (maxNumOfPointsPerTile < 0) {
					errorMessage("Incorrect parameter value", "The value must equal to or bigger than 0");
					return;
				}
			}
			catch (NumberFormatException nfe) {
				errorMessage("Incorrect parameter value", "Invalid geometricError value");
				return;
			}
		}	
		
		String outputPath = outputBrowserTextField.getText();
		if (outputPath.trim().equals("")) {
			errorMessage("Incorrect parameter value", "Please enter a valid folder path for export");
			return;
		}
		
		PntcConfig config = new PntcConfig();		
		config.setInputPath(inputPath);
		config.setSrid(srid);
		config.setSeparatorCharacter(separatorCharacter);
		config.setColorBitSize(colorBitSize);
		config.setZScaleFactor(zScaleFactor);
		config.setzOffset(zOffset);
		config.setTileSize(tileSize);
		config.setMaxNumOfPointsPerTile(maxNumOfPointsPerTile);
		config.setOutputFolderPath(outputPath);
		
		final StatusDialog exportDialog = new StatusDialog(parentFrame);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				exportDialog.setLocationRelativeTo(parentFrame);
				exportDialog.setVisible(true);
			}
		});
		
		SqliteDBManagerFactory dbManagerFactory = new SqliteDBManagerFactory(config);
		final PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);
		
		exportDialog.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						generator.setShouldRun(false);
					}
				});
			}
		});
		
		boolean success = false;
		try {
			success = generator.doProcess();
		} catch (PntcGenerationException e) {
			Logger.error(e.getMessage());
			
			Throwable cause = e.getCause();
			while (cause != null) {
				Logger.error("Cause: " + cause.getMessage());
				cause = cause.getCause();
				generator.setShouldRun(false);
			}
		}	
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				exportDialog.dispose();
			}
		});
		
		if (success) {
			Logger.info("Export successfully finished.");
		} else {
			Logger.warn("Export aborted.");
		}
	}
	
	private void initDefaultSettings() {
		tileSizeInputField.setText("100");
		zOffsetInputField.setText("-400");
		sridInputField.setText("2994");
		zScaleFactorInputField.setText("0.3028");
		SeparatorCharacterInputField.setText(" ");
		colorBitSizeInputField.setText("16");
		maxNumOfPointsPerTileInputField.setText("5000");
		inputBrowseTextField.setText(new File("data" + File.separator + "sample_xyzRGB_data" + File.separator + "small").getAbsolutePath());
		outputBrowserTextField.setText(new File("viewer" + File.separator + "output_data").getAbsolutePath());
	}

	private void errorMessage(String title, String text) {
		JOptionPane.showMessageDialog(this, text, title, JOptionPane.ERROR_MESSAGE);
	}
}
