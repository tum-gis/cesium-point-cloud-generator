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
