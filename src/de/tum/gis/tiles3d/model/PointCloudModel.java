package de.tum.gis.tiles3d.model;

public class PointCloudModel extends AbstractTileContent {
	private int maximumNumberOfPoints = Integer.MAX_VALUE;

	public int getMaximumNumberOfPoints() {
		return maximumNumberOfPoints;
	}

	public void setMaximumNumberOfPoints(int maximumNumberOfPoints) {
		this.maximumNumberOfPoints = maximumNumberOfPoints;
	}
}
