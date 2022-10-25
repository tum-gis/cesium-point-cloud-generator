/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import org.citydb.gui.util.GuiUtil;


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
