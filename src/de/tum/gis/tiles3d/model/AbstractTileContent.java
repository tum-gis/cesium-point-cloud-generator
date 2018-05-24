package de.tum.gis.tiles3d.model;

public class AbstractTileContent {
	protected String path;
	protected BoundingBox2D ownerTileBoundingBox;
	protected AbstractBoundingVolume ownerTileboundingVolume;	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public BoundingBox2D getOwnerTileBoundingBox() {
		return ownerTileBoundingBox;
	}

	public void setOwnerTileBoundingBox(BoundingBox2D boundingBox) {
		this.ownerTileBoundingBox = boundingBox;
	}
	
	public AbstractBoundingVolume getOwnerTileBoundingVolume() {
		return ownerTileboundingVolume;
	}

	public void setOwnerTileBoundingVolume(AbstractBoundingVolume boundingVolume) {
		this.ownerTileboundingVolume = boundingVolume;
	}
}
