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
