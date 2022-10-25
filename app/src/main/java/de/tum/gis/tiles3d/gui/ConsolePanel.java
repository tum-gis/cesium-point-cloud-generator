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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.citydb.gui.util.GuiUtil;

import de.tum.gis.tiles3d.util.Logger;

@SuppressWarnings("serial")
public final class ConsolePanel extends JPanel {

	private JTextArea consoleText;
	private JFrame parentFrame;

	private PrintStream out;
	private PrintStream err;
	
	public ConsolePanel(JFrame parentFrame) {
		this.setParentFrame(parentFrame);
		initGui();
	}

	public void initGui() {
		// layout
		setLayout(new GridBagLayout());

		// right panel (console panel)
		consoleText = new JTextArea();
		consoleText.setAutoscrolls(true);
		consoleText.setFont(new Font(Font.MONOSPACED, 0, 11));
		consoleText.setEditable(false);

		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBackground(this.getBackground());
		this.setLayout(new GridBagLayout());
		{
			JScrollPane scroll = new JScrollPane(consoleText);
			scroll.setBorder(BorderFactory.createEtchedBorder());
			scroll.setViewportBorder(BorderFactory.createEmptyBorder());
			this.add(scroll, GuiUtil.setConstraints(0, 1, 1.0, 1.0, GridBagConstraints.BOTH, 0, 0, 0, 0));
		}
		
		// save the print stream for shutting down
		out = System.out;
		err = System.err;
		
		parentFrame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.setOut(out);
				System.setErr(err);
				Logger.info("Application successfully terminated");
			}
		});
		
		Charset encoding;
		try {
			encoding = Charset.forName("UTF-8");
		} catch (Exception e) {
			encoding = Charset.defaultCharset();
		}

		// let system standard out point to console window
		JTextAreaOutputStream consoleWriter = new JTextAreaOutputStream(consoleText, new ByteArrayOutputStream(), encoding);
		PrintStream writer;

		try {
			writer = new PrintStream(consoleWriter, true, encoding.displayName());
		} catch (UnsupportedEncodingException e) {
			writer = new PrintStream(consoleWriter);
		}

		System.setOut(writer);
	    System.setErr(writer);
	}

	public void clearConsole() {
		consoleText.setText("");
	}

	public JFrame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	private class JTextAreaOutputStream extends FilterOutputStream {
		private final int MAX_DOC_LENGTH = 10000;
		private final JTextArea ta;
		private final Charset encoding;

		public JTextAreaOutputStream(JTextArea ta, OutputStream stream, Charset encoding) {
			super(stream);
			this.ta = ta;
			this.encoding = encoding;
		}

		@Override
		public void write(final byte[] b) {
			try {
				ta.append(new String(b, encoding));
			} catch (Error e) {
			}
		}

		@Override
		public void write(final byte b[], final int off, final int len) {
			try {
				ta.append(new String(b, off, len, encoding));
			} catch (Error e) {
			}
		}

		public void flush() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ta.setCaretPosition(ta.getDocument().getLength());	
					if (ta.getLineCount() > MAX_DOC_LENGTH)
						ta.setText("...truncating console output after " + MAX_DOC_LENGTH + " log messages...");
				}
			});
		}
	}
}
