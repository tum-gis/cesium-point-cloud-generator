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
