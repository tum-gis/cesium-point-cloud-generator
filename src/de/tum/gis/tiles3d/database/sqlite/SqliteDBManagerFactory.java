package de.tum.gis.tiles3d.database.sqlite;

import de.tum.gis.tiles3d.database.DBManagerFactory;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.database.DBManager;

public class SqliteDBManagerFactory implements DBManagerFactory {
	
	private PntcConfig config;
	
	public SqliteDBManagerFactory(PntcConfig config) {
		this.config = config;
	}

	@Override
	public DBManager createDBManager() {
		return new SqliteDBManager(config);
	}

}
