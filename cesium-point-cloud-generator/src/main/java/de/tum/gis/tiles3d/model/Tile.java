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

public class Tile {
	
	protected AbstractBoundingVolume boundingVolume;	
	protected double geometricError;
	protected Refine refine;
	protected AbstractTileContent content;
	protected String contentUrl; 
	protected List<Tile> children;
	

	public AbstractBoundingVolume getBoundingVolume() {
		return boundingVolume;
	}

	public void setBoundingVolume(AbstractBoundingVolume boundingVolume) {
		this.boundingVolume = boundingVolume;
	}

	public double getGeometricError() {
		return geometricError;
	}
	
	public void setGeometricError(double geometricError) {
		this.geometricError = geometricError;
	}
	
	public Refine getRefine() {
		return refine;
	}
	
	public void setRefine(Refine refine) {
		this.refine = refine;
	}
	
	public AbstractTileContent getContent() {
		return content;
	}
	
	public void setContent(AbstractTileContent content) {
		this.content = content;
	}
	
	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	public List<Tile> getChildren() {
        return this.children;
	}
	
	public void setChildren(List<Tile> children) {
		this.children = children;
	}
	
	public void calculateAndUpdateBoundingVolume(){
		double sMinLat = Integer.MAX_VALUE;
		double sMaxLat = Integer.MIN_VALUE;
		double sMinLon = Integer.MAX_VALUE;
		double sMaxLon = Integer.MIN_VALUE;
		double sMinZ = Integer.MAX_VALUE;
		double sMaxZ = Integer.MIN_VALUE;
		
		Iterator<Tile> iter = this.getChildren().iterator();
		while (iter.hasNext()) {
			AbstractBoundingVolume b = iter.next().getBoundingVolume();
			
			if (b instanceof Region) {
				double minLat = ((Region) b).getMinLat();
				double maxLat = ((Region) b).getMaxLat();
				double minLon = ((Region) b).getMinLon();
				double maxLon = ((Region) b).getMaxLon();
				double minZ = ((Region) b).getMinZ();
				double maxZ = ((Region) b).getMaxZ();
				
				if (minLon < sMinLon)
					sMinLon = minLon;
				if (maxLon > sMaxLon)
					sMaxLon = maxLon;				
				if (minLat < sMinLat)
					sMinLat = minLat;
				if (maxLat > sMaxLat)
					sMaxLat = maxLat;				
				if (minZ < sMinZ)
					sMinZ = minZ;
				if (maxZ > sMaxZ)
					sMaxZ = maxZ;				
			}
			else {/* box and sphere are currently not supported...*/}
		}
		this.setBoundingVolume(new Region(sMinLon, sMinLat, sMaxLon, sMaxLat, sMinZ, sMaxZ));	
	}
	
}
