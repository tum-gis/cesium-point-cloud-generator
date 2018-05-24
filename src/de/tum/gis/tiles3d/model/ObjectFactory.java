package de.tum.gis.tiles3d.model;

public class ObjectFactory {
	
	public ObjectFactory() {
    }
	
	public Tile createTile() {
        return new Tile();
    }

    public TileSet createTileset() {
        return new TileSet();
    }
    
    public Asset createAssect() {
    	return new Asset();
    }
    
    public Region createRegion() {
    	return new Region();
    }

    public PointCloudModel createPointCloudModel () {
    	return new PointCloudModel();
    }
    
    
}
