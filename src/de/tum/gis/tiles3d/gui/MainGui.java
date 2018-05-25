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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.citydb.util.gui.GuiUtil;

@SuppressWarnings("serial")
public final class MainGui extends JFrame {

	private JSplitPane mainPanel;
	private ParameterPanel inputPanel;
	private ConsolePanel consolePanel;

	public MainGui() {}

	public void initGui() {				
		this.inputPanel = new ParameterPanel(this);
		this.consolePanel = new ConsolePanel(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		setTitle("Cesium Point Cloud Generator");

		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setContinuousLayout(true);
		mainPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setOpaque(false);
		mainPanel.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					public void setBorder(Border b) {}
				};
			}
		});
		mainPanel.setLeftComponent(inputPanel);
		mainPanel.setRightComponent(consolePanel);		
		add(mainPanel, GuiUtil.setConstraints(0, 0, 1.0, 1.0, GridBagConstraints.BOTH, 0, 0, 0, 0));
		
		showWindow();
	}

	private void showWindow() {
		Toolkit t = Toolkit.getDefaultToolkit();

		// default size for the GUI
		Integer width = 1280;
		Integer height = 760;

		// set the location of the GUI (center of the monitor)
		Integer x = (t.getScreenSize().width - width) / 2;
		Integer y = (t.getScreenSize().height - height) / 2;
		setLocation(x, y);
		setSize(width, height);
		setVisible(true);

		// set the scale of the left and right panels (1:1)
		Integer dividerLocation = (int) (width * .5);
		mainPanel.setDividerLocation(dividerLocation);
	}
}
