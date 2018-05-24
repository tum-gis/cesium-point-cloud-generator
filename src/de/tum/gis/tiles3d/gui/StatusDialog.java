
package de.tum.gis.tiles3d.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.citydb.util.gui.GuiUtil;


@SuppressWarnings("serial")
public class StatusDialog extends JDialog {
	
	private static String waitInfo = "Please wait...";
	private JButton cancelButton;
	private JProgressBar progressBar;
	public StatusDialog(JFrame frame) {
		super(frame, null, true);			
		initGUI();
	}

	private void initGUI() {			
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JLabel titleLabel = new JLabel(waitInfo);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		cancelButton = new JButton("Cancel");		
		progressBar = new JProgressBar();

		setLayout(new GridBagLayout()); {
			JPanel main = new JPanel();
			add(main, GuiUtil.setConstraints(0,0,1.0,0.0,GridBagConstraints.BOTH,5,5,5,5));
			main.setLayout(new GridBagLayout());
			{
				main.add(titleLabel, GuiUtil.setConstraints(0,0,0.0,0.5,GridBagConstraints.HORIZONTAL,5,5,5,5));
				main.add(progressBar, GuiUtil.setConstraints(0,1,1.0,0.0,GridBagConstraints.HORIZONTAL,0,5,5,5));
			}
			add(cancelButton, GuiUtil.setConstraints(0,1,0.0,0.0,GridBagConstraints.NONE,5,5,5,5));
			pack();
			progressBar.setIndeterminate(true);
		}
	}

	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public void closeDialog() {
		progressBar.setIndeterminate(true);
	}

}
