package de.tum.gis.tiles3d;

import de.tum.gis.tiles3d.gui.MainGui;

public class Main {

	public static void main(String[] args) throws Exception {		
		new Main().startUp(args);
	}
	
	// set look and feel
	static {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startUp(String[] args) {
		MainGui mainView = new MainGui();
		mainView.initGui();
	}
}
