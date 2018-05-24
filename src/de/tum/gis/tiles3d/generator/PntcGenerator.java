package de.tum.gis.tiles3d.generator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.citydb.api.concurrent.PoolSizeAdaptationStrategy;
import org.citydb.api.concurrent.WorkerPool;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import de.tum.gis.tiles3d.concurrent.PntcTileCreatorFactory;
import de.tum.gis.tiles3d.concurrent.PntcTileWork;
import de.tum.gis.tiles3d.database.DBManager;
import de.tum.gis.tiles3d.database.DBManagerFactory;
import de.tum.gis.tiles3d.database.sqlite.SqliteDBManager;
import de.tum.gis.tiles3d.model.AbstractBoundingVolume;
import de.tum.gis.tiles3d.model.AbstractTileContent;
import de.tum.gis.tiles3d.model.BoundingBox2D;
import de.tum.gis.tiles3d.model.ObjectFactory;
import de.tum.gis.tiles3d.model.PointCloudModel;
import de.tum.gis.tiles3d.model.PointObject;
import de.tum.gis.tiles3d.model.Refine;
import de.tum.gis.tiles3d.model.Region;
import de.tum.gis.tiles3d.model.Tile;
import de.tum.gis.tiles3d.model.TileSet;
import de.tum.gis.tiles3d.util.CoordinateConversionException;
import de.tum.gis.tiles3d.util.FileOperator;
import de.tum.gis.tiles3d.util.Logger;

public class PntcGenerator {
	final static String ENCODING = "UTF-8";
	final static Charset CHARSET = Charset.forName(ENCODING);
	final static String tilesFolderName = "Tiles";	
	final static double GeometricErrorRatio = 0.02;
	
	private volatile boolean shouldRun = true;
	private AtomicInteger totalNumberOfTiles;			
	private WorkerPool<PntcTileWork> tileCreatorPool; 
	private DBManager dbManager;
	private PntcConfig config;
	
	public PntcGenerator(PntcConfig config, DBManagerFactory dbManagerFactory) {
		this.config = config;
		dbManager = dbManagerFactory.createDBManager();
	}

	public boolean doProcess() throws PntcGenerationException {
		String outputFolderPath = config.getOutputFolderPath();

		Logger.info("Cleaning up all files in the output folder");
		FileOperator.deleteFiles(new File(outputFolderPath));
	
		try {
			Logger.info("Creating a temporary database in the output folder.");
			dbManager.createConnection();
			dbManager.createDataTable();
			
			if (shouldRun) {
				Logger.info("Reading point cloud data from source files and import into the database.");
				readSourcePointData();
			}
							
			if (shouldRun) {
				Logger.info("Creating database index. It may take a while depending on the size of input data");
				dbManager.createIndexes();	
			} 		
		} catch (Exception e) {		
			throw new PntcGenerationException("Faild to read and import source data into the local temporary database.", e);		
		}
		
		BoundingBox2D globalBoundingbox;
		try {
			if (!shouldRun)
				return false;
			Logger.info("Calculating the global boundingbox of the input point data");
			globalBoundingbox = dbManager.calculateGlobalBoundingbox();			
		} catch (SQLException e) {
			throw new PntcGenerationException("Faild to calculate global boundingbox from database.", e);
		}		

		TileSet tileset = new TileSet();
		try {
			if (!shouldRun)
				return false;
			Logger.info("Generating output file structure according to the Cesium's 3D-Tiles standard");
			double tileSize = config.getTileSize();			
			tileset = generateTileset(globalBoundingbox, outputFolderPath, tileSize);
		} catch (CoordinateConversionException e) {
			throw new PntcGenerationException("Faild to construct 3D-Tiles data structure.", e); 
		}

		totalNumberOfTiles = new AtomicInteger(tileset.calculateNumberOfChildrenTiles());
		int minThreads = 2;
		int maxThreads = Math.max(minThreads, Runtime.getRuntime().availableProcessors());		
		
		Logger.info("Writing data contents to the created file folder");	
		tileCreatorPool = new WorkerPool<PntcTileWork>(
				"tile_creator_pool",
				minThreads,
				maxThreads,
				PoolSizeAdaptationStrategy.AGGRESSIVE,
				new PntcTileCreatorFactory(totalNumberOfTiles, dbManager, config), 300, false);
		
		tileCreatorPool.prestartCoreWorkers();
		
		createPointCloudModel(tileset);

		try {
			tileCreatorPool.shutdownAndWait();
		} catch (InterruptedException e) {
			throw new PntcGenerationException("Failed to shutdown worker pool.", e);
		}
		
		try {
			writeTileset(tileset);				
		} catch (IOException | CoordinateConversionException e) {
			throw new PntcGenerationException("Faild to write 3D-Tiles data files", e); 
		}		

		Logger.info("Disconnecting and dropping the temporary database"); 
		dbManager.KillConnection();	

		return shouldRun;
	}	
	
	private void createPointCloudModel(TileSet tileset) {
		Tile rootTile = tileset.getRoot();		
		List<Tile> childrenTileList = rootTile.getChildren();
		if (childrenTileList != null) {
			if (childrenTileList.size() > 0) {				
				Iterator<Tile> iter = childrenTileList.iterator();
				while(iter.hasNext()) {
					Tile childTile = iter.next();			
					AbstractTileContent content = childTile.getContent();
					if (content instanceof TileSet) {
						createPointCloudModel((TileSet) content);
					}
					else if (content instanceof PointCloudModel) {
						if (!shouldRun) {			
							return;
						}	
						tileCreatorPool.addWork(new PntcTileWork((PointCloudModel) content));
					}
				}
			}
		}
		tileCreatorPool.addWork(new PntcTileWork((PointCloudModel) rootTile.getContent()));
	}
	
	private void readSourcePointData() throws IOException, SQLException {
		File dataFolder = new File(config.getInputPath());
		File[] fileList = dataFolder.listFiles();
		int numberOfFiles = fileList.length;	
		double minZ = Double.MAX_VALUE;
		for (int i = 0; i < numberOfFiles; i++) {
			File fileEntry = fileList[i];
			String fileEntryPath = fileEntry.getAbsolutePath();			
			BufferedReader br = null;
			try {		
				List<PointObject> batchPointList = new ArrayList<PointObject>();
				br = new BufferedReader(new FileReader(fileEntryPath));
				String line;				
				while ((line = br.readLine()) != null) {
					if (!shouldRun)
						return;
					
					String[] valueArray = line.split(config.getSeparatorCharacter());
					double x = Double.valueOf(valueArray[0]);
					double y = Double.valueOf(valueArray[1]);
					double z = (Double.valueOf(valueArray[2]) + config.getzOffset()) * config.getZScaleFactor();
					
					if (Double.valueOf(valueArray[2]) < minZ)
						minZ = Double.valueOf(valueArray[2]);
					
					int colorScaleFactor = 1;
					if (config.getColorBitSize() == 16)
						colorScaleFactor = 256;
					
					int r = Integer.valueOf(valueArray[3]) / colorScaleFactor;
					int g = Integer.valueOf(valueArray[4]) / colorScaleFactor;
					int b = Integer.valueOf(valueArray[5]) / colorScaleFactor;	
					
					batchPointList.add(new PointObject(x, y, z, r, g, b, config.getSrid()));
					if (batchPointList.size() % SqliteDBManager.batchInsertionSize == 0) {
						dbManager.importIntoDatabase(batchPointList);
						batchPointList = new ArrayList<PointObject>();
					}
				}
				dbManager.importIntoDatabase(batchPointList);
			} catch (NumberFormatException e) {
				throw new IOException("Invalid coordinate or color value in source files.", e);
			} catch (IOException e) {
				throw new IOException("Faild to read data from source files.", e);
			} 
		    finally {				
				try {
					if (br != null) {
						br.close();
					}					
				} catch (IOException ex) {
					ex.printStackTrace();					
				}
			}			
			Logger.info("Reading " + fileEntry.getName() + ". Remaining files: " +  (numberOfFiles - (i + 1)));
		}
		Logger.info("minZ: " + minZ);
	}
	
	private TileSet generateTileset(BoundingBox2D globalBoundingbox, 
			String outputFolderPath, double tileSize) throws CoordinateConversionException {	
		
		ObjectFactory objectFactory = new ObjectFactory();	
		TileSet tileset = objectFactory.createTileset();
		Tile rootTile = objectFactory.createTile();		
		tileset.setAsset(objectFactory.createAssect());		
		tileset.setPath(outputFolderPath + File.separator + "tileset.json");
		tileset.setRoot(rootTile);			
		
		BoundingBox2D[][] tileBoundingboxes = globalBoundingbox.tileBoundingbox(tileSize);			
		int rowNumber = tileBoundingboxes.length;
		int colNumber = tileBoundingboxes[0].length;
		int numberOfTiles = rowNumber * colNumber;	
		
		int maximumNumberOfChildrenTiles = 2;
		boolean createTileset = false;
		if (numberOfTiles > maximumNumberOfChildrenTiles * maximumNumberOfChildrenTiles) {
			rowNumber = maximumNumberOfChildrenTiles;
			colNumber = maximumNumberOfChildrenTiles;
			tileBoundingboxes = globalBoundingbox.tileBoundingbox(rowNumber, colNumber);
			createTileset = true;
		}
		
		String tilesFolderPath = outputFolderPath + File.separator + tilesFolderName;
		new File(tilesFolderPath).mkdir();	
		List<Tile> tileList = new ArrayList<Tile>();
		
		for (int rowIndex = 0; rowIndex < rowNumber; rowIndex++) {
		    for (int colIndex = 0; colIndex < colNumber; colIndex++) {	
		    	String childTileFolderPath = tilesFolderPath + File.separator + rowIndex + "_" + colIndex;
		    	new File(childTileFolderPath).mkdir();

		    	Tile tile = objectFactory.createTile();	

		    	if (createTileset) {
			        String contentUrl = tilesFolderName + "/" + rowIndex + "_" + colIndex + "/tileset.json";
			        TileSet childTileset = generateTileset(tileBoundingboxes[rowIndex][colIndex], childTileFolderPath, tileSize);
			        tile.setGeometricError(childTileset.getGeometricError());
			        tile.setContentUrl(contentUrl);
			        tile.setContent(childTileset);
		    	}
		    	else {
		    		String pntsFilename = rowIndex + "_" + colIndex + ".pnts";
					String filePath = childTileFolderPath + File.separator + pntsFilename;
			        String contentUrl = tilesFolderName + "/" + rowIndex + "_" + colIndex + "/" + pntsFilename;
			        
					PointCloudModel pointCloudModel = objectFactory.createPointCloudModel();	
					pointCloudModel.setPath(filePath);
					pointCloudModel.setOwnerTileBoundingBox(tileBoundingboxes[rowIndex][colIndex]);
					pointCloudModel.setMaximumNumberOfPoints(Integer.MAX_VALUE);

					tile.setContent(pointCloudModel);
					tile.setContentUrl(contentUrl);
					tile.setGeometricError(config.getTileSize() * GeometricErrorRatio);
		    	}
				tile.setRefine(Refine.REPLACE);				
				
				tileList.add(tile);	   
		    }  	
		}
				
		rootTile.setChildren(tileList);
		rootTile.setGeometricError(tileList.get(0).getGeometricError() * maximumNumberOfChildrenTiles);
		tileset.setGeometricError(tileList.get(0).getGeometricError() * maximumNumberOfChildrenTiles);
		rootTile.setRefine(Refine.REPLACE);
		PointCloudModel rootPointCloudModel = objectFactory.createPointCloudModel();	
		rootPointCloudModel.setPath(outputFolderPath + File.separator + "root.pnts");
		rootPointCloudModel.setOwnerTileBoundingBox(globalBoundingbox);
		rootPointCloudModel.setMaximumNumberOfPoints(config.getMaxNumOfPointsPerTile());
		rootTile.setContent(rootPointCloudModel);
		rootTile.setContentUrl("root.pnts");
		
		return tileset;
	}
	
	private AbstractBoundingVolume writeTileset(TileSet tileset) throws CoordinateConversionException, IOException {
		JsonObject tilesetJS = Json.object().add("asset", Json.object().add("version", tileset.getAsset().getVersion()));
		tilesetJS.add("geometricError", tileset.getGeometricError());
		Tile rootTile = tileset.getRoot();
		JsonObject rootTileJS = createTileJsonObject(rootTile);
		if (rootTileJS == null)
			return null;
		
		tilesetJS.add("root", rootTileJS);
		
		Writer writer = null;
		try {
			writer = new FileWriter(tileset.getPath());
			tilesetJS.writeTo(writer, WriterConfig.PRETTY_PRINT);
		} catch (IOException e) {
			throw new IOException("Failed to write tileset to json file.", e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return rootTile.getBoundingVolume();
	}
	
	private JsonArray createTilesJsonArray(List<Tile> tileList)
			throws IOException, CoordinateConversionException {
		JsonArray childObjectArray = (JsonArray) Json.array();
		
		Iterator<Tile> iter = tileList.iterator();
		while (iter.hasNext() && shouldRun) {
			Tile tile = iter.next();			
			JsonObject tileJS = createTileJsonObject(tile);
			if (tileJS != null)
				childObjectArray.add(tileJS);
		}
		
		return childObjectArray;
	}
	
	private JsonObject createTileJsonObject(Tile tile) 
			throws IOException, CoordinateConversionException {
		
		JsonObject tileJS = Json.object();
		
		List<Tile> childrenTiles = tile.getChildren();
		if (childrenTiles != null) {
			JsonArray childrenTilesJsonArray = createTilesJsonArray(childrenTiles);
			if (childrenTilesJsonArray == null)
				return null;
			tileJS.add("children", childrenTilesJsonArray);
			tile.calculateAndUpdateBoundingVolume();
		}
		
		AbstractTileContent tileContent = tile.getContent();
		if (tileContent != null) {
			tileJS.add("content", Json.object().add("url", tile.getContentUrl()));
			
			AbstractBoundingVolume boundingVolume = null;
			if (tileContent instanceof TileSet) {
				boundingVolume = writeTileset((TileSet) tileContent);				
			}
			else if (tileContent instanceof PointCloudModel) {
				boundingVolume = ((PointCloudModel) tileContent).getOwnerTileBoundingVolume();				
			}
			
			if (boundingVolume == null)
				return null;
			
			tile.setBoundingVolume(boundingVolume);
		}
		
		AbstractBoundingVolume tielBoundingVolume = tile.getBoundingVolume();	
		if (tielBoundingVolume instanceof Region) {
			Region tileRegion = (Region) tielBoundingVolume;
			tileJS.add("boundingVolume", Json.object().add("region", Json.array(
					Math.toRadians(tileRegion.getMinLon()), 
					Math.toRadians(tileRegion.getMinLat()),
					Math.toRadians(tileRegion.getMaxLon()),
					Math.toRadians(tileRegion.getMaxLat()),
					tileRegion.getMinZ(),
					tileRegion.getMaxZ())));
		}
		else {/* box and sphere are currently not supported...*/}

		tileJS.add("geometricError", tile.getGeometricError());
		tileJS.add("refine", tile.getRefine().value());

		return tileJS;
	}

	public boolean isShouldRun() {
		return shouldRun;
	}

	public void setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
		if (!shouldRun && tileCreatorPool != null) {
			tileCreatorPool.drainWorkQueue();				
		}		
		else {
			if (!shouldRun)
				dbManager.KillConnection();
		}
		
	}
	
}
