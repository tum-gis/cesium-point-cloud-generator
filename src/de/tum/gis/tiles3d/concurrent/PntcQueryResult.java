package de.tum.gis.tiles3d.concurrent;

import java.awt.Color;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class PntcQueryResult {
	
	private List<Coordinate> coordinateList;
	private List<Color> colorList;
	
	public List<Coordinate> getCoordinateList() {
		return coordinateList;
	}
	
	public void setCoordinateList(List<Coordinate> coordinateList) {
		this.coordinateList = coordinateList;
	}
	
	public List<Color> getColorList() {
		return colorList;
	}
	
	public void setColorList(List<Color> colorList) {
		this.colorList = colorList;
	}
}
