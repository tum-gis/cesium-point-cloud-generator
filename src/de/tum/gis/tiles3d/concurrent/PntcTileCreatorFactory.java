package de.tum.gis.tiles3d.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

import org.citydb.api.concurrent.Worker;
import org.citydb.api.concurrent.WorkerFactory;

import de.tum.gis.tiles3d.database.DBManager;
import de.tum.gis.tiles3d.generator.PntcConfig;

public class PntcTileCreatorFactory implements WorkerFactory<PntcTileWork> {
	private AtomicInteger totalNumberOfTiles;
	private DBManager dbManager;
	private PntcConfig config;
	
	public PntcTileCreatorFactory(AtomicInteger totalNumberOfTiles, DBManager dbManage, PntcConfig config){
		this.totalNumberOfTiles = totalNumberOfTiles;
		this.dbManager = dbManage;
		this.config = config;
	}
	
	@Override
	public Worker<PntcTileWork> createWorker() {
		PntcTileCreator pntcTilesetCreator = null;

		try {
			pntcTilesetCreator = new PntcTileCreator(totalNumberOfTiles, dbManager, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pntcTilesetCreator;
	}

}
