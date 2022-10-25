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

import java.util.concurrent.atomic.AtomicInteger;

import org.citydb.util.concurrent.Worker;
import org.citydb.util.concurrent.WorkerFactory;

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
