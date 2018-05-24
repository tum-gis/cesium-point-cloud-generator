package de.tum.gis.tiles3d.concurrent;

import de.tum.gis.tiles3d.model.PointCloudModel;

public class PntcTileWork {
	
	private PointCloudModel pntcModel;

	public PntcTileWork(PointCloudModel pntcModel){
		this.setPntcModel(pntcModel);
	}

	public PointCloudModel getPntcModel() {
		return pntcModel;
	}

	public void setPntcModel(PointCloudModel pntcModel) {
		this.pntcModel = pntcModel;
	}

	
}