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
