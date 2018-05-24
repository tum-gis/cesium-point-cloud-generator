package de.tum.gis.tiles3d.util;

import java.io.File;

public class FileOperator {
	public static void deleteFiles(File targetFolder) {
		File[] files = targetFolder.listFiles();
		if (files != null) {
			for (File f: files) {
				if (f.isDirectory())
					deleteFolder(f);
				else
					f.delete();
			}
		}
	}

	public static void deleteFolder(File folder) {
		if (folder == null) return;
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f: files) {
				if (f.isDirectory())
					deleteFolder(f);
				else
					f.delete();
			}
		}
		folder.delete();
	}
}
