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

package de.tum.gis.tiles3d.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import de.tum.gis.tiles3d.concurrent.PntcQueryResult;
import de.tum.gis.tiles3d.model.BoundingBox2D;
import de.tum.gis.tiles3d.model.PointCloudModel;
import de.tum.gis.tiles3d.model.PointObject;

public interface DBManager {
	public void createConnection() throws SQLException;
	public Connection getConnection();
	public void KillConnection();
	
	public void createDataTable() throws SQLException;
	public void createIndexes() throws SQLException;
	public void importIntoDatabase(List<PointObject> pointList) throws SQLException;
	public BoundingBox2D calculateGlobalBoundingbox() throws SQLException;
	public PntcQueryResult queryPointEntities(PointCloudModel pntModel) throws SQLException;
}
