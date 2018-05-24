package de.tum.gis.tiles3d.model;

public class BoundingBox2D {	
	private double minY;
	private double minX;
	private double maxY;
	private double maxX;
	
	public BoundingBox2D(double minY, double minX, double maxY, double maxX) {
		this.minY = minY;
		this.minX = minX;
		this.maxY = maxY;
		this.maxX = maxX;
	}

	public BoundingBox2D[][] tileBoundingbox(double tileSize) {
		int colnum = (int) ((maxX - minX) / tileSize) + 1;
		int rownum = (int) ((maxY - minY) / tileSize) + 1;

		return tileBoundingbox(rownum, colnum);
	}
	
	public BoundingBox2D[][] tileBoundingbox(int rownum, int colnum) {

		double tileSizeX = (maxX - minX) / colnum;
		double tileSizeY = (maxY - minY) / rownum;
				
		BoundingBox2D[][] tileBoundingVolumes = new BoundingBox2D[rownum][colnum];
		for (int i = 0; i < colnum; i++) {
			for (int j = 0; j < rownum; j++) {
				BoundingBox2D tileBoundingVolume = new BoundingBox2D(
						minY + tileSizeY * j, 
						minX + tileSizeX * i, 
						minY + tileSizeY * (j + 1), 
						minX + tileSizeX * (i + 1));
				tileBoundingVolumes[j][i] = tileBoundingVolume;
			}
		}
		
		return tileBoundingVolumes;
	}
	
	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}
		
}
