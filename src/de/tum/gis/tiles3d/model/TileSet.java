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

package de.tum.gis.tiles3d.model;

import java.util.Iterator;
import java.util.List;

public class TileSet extends AbstractTileContent {
	
	protected Asset asset;
	protected double geometricError;
	protected Tile root;	

	public TileSet () {
	}
	
	public Asset getAsset() {
		return asset;
	}
	
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	public double getGeometricError() {
		return geometricError;
	}
	
	public void setGeometricError(double geometricError) {
		this.geometricError = geometricError;
	}
	
	public Tile getRoot() {
		return root;
	}
	
	public void setRoot(Tile root) {
		this.root = root;
	}
	
	public int calculateNumberOfChildrenTiles() {
		int tileNumber = 1;
		tileNumber = tileNumber + calculateNumberOfChildrenTiles(root);
		return tileNumber;
	}
	
	private int calculateNumberOfChildrenTiles(Tile rootTile) {
		int tileNumber = 0;
		List<Tile> childrenTileList = rootTile.getChildren();
		if (childrenTileList != null) {
			if (childrenTileList.size() > 0) {				
				Iterator<Tile> iter = childrenTileList.iterator();
				while(iter.hasNext()) {
					Tile childTile = iter.next();			
					tileNumber = tileNumber + calculateNumberOfChildrenTiles(childTile);
					AbstractTileContent content = childTile.getContent();
					if (content instanceof TileSet) {
						tileNumber = tileNumber + ((TileSet) content).calculateNumberOfChildrenTiles();
					}
					else if (content instanceof PointCloudModel) {
						tileNumber++;
					}
				}
			}
		}
		return tileNumber;
	}
}
