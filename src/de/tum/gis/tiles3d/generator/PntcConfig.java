package de.tum.gis.tiles3d.generator;

public class PntcConfig {
	private String inputPath;
	private String outputFolderPath;
	private double tileSize;
	private double zOffset;
	private String srid;
	private String separatorCharacter;
	private double zScaleFactor;
	private int colorBitSize;	
	private int maxNumOfPointsPerTile;
	
	public int getColorBitSize() {
		return colorBitSize;
	}

	public void setColorBitSize(int colorBitSize) {
		this.colorBitSize = colorBitSize;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputFolderPath() {
		return outputFolderPath;
	}

	public void setOutputFolderPath(String outputFolderPath) {
		this.outputFolderPath = outputFolderPath;
	}

	public double getTileSize() {
		return tileSize;
	}

	public void setTileSize(double tileSize) {
		this.tileSize = tileSize;
	}

	public double getzOffset() {
		return zOffset;
	}

	public void setzOffset(double zOffset) {
		this.zOffset = zOffset;
	}

	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	public String getSeparatorCharacter() {
		return separatorCharacter;
	}

	public void setSeparatorCharacter(String separatorCharacter) {
		this.separatorCharacter = separatorCharacter;
	}

	public double getZScaleFactor() {
		return zScaleFactor;
	}

	public void setZScaleFactor(double zScaleFactor) {
		this.zScaleFactor = zScaleFactor;
	}

	public int getMaxNumOfPointsPerTile() {
		return maxNumOfPointsPerTile;
	}

	public void setMaxNumOfPointsPerTile(int maxNumOfPointsPerTile) {
		this.maxNumOfPointsPerTile = maxNumOfPointsPerTile;
	}


}
