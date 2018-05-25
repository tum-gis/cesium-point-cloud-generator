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

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.citydb.api.concurrent.DefaultWorkerImpl;
import com.vividsolutions.jts.geom.Coordinate;

import de.tum.gis.tiles3d.database.DBManager;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.model.PointCloudModel;
import de.tum.gis.tiles3d.util.CharacterConverter;
import de.tum.gis.tiles3d.util.Logger;

public class PntcTileCreator extends DefaultWorkerImpl<PntcTileWork>{
	private AtomicInteger totalNumberOfTiles;
	private DBManager dbManager;
	
	public PntcTileCreator(AtomicInteger totalNumberOfTiles, DBManager dbManager, PntcConfig config) throws SQLException{
		this.totalNumberOfTiles = totalNumberOfTiles;
		this.dbManager = dbManager;
	}
	
	@Override
	public void doWork(PntcTileWork work) {	
		try {			
			Logger.info("creating " + work.getPntcModel().getPath());
			processTileset(work);			
			Logger.info("Number of Remaining tiles: " + totalNumberOfTiles.decrementAndGet());
		}
		catch (IOException | SQLException e) {
			e.printStackTrace();			
		}	
	}
	
	@Override
	public void shutdown() {
		// do nothing
	}

	public void processTileset(PntcTileWork work) throws IOException, SQLException {
		PointCloudModel pntModel = work.getPntcModel();
		String pntsFilepath = pntModel.getPath();
		
		// query point object information (XZY and RGB) from database
		PntcQueryResult queryResult = dbManager.queryPointEntities(pntModel);
		if (queryResult == null)
			return;
		
		List<Coordinate> coordinateList = queryResult.getCoordinateList();
		List<Color> colorList = queryResult.getColorList();

		// calculate origin and relative coordinates
		int pointNumber = coordinateList.size();		
		double sumX = 0;
		double sumY = 0;
		double sumZ = 0;		
				
		for (int i = 0; i < pointNumber; i++) {
			double x = coordinateList.get(i).x;
			double y = coordinateList.get(i).y;
			double z = coordinateList.get(i).z;
			sumX = sumX + x;
			sumY = sumY + y;
			sumZ = sumZ + z;			
		}
				
		double avgX = sumX / pointNumber;
		double avgY = sumY / pointNumber;
		double avgZ = sumZ / pointNumber;
		Coordinate origin = new Coordinate(avgX, avgY, avgZ);

		float[] coordinateArray = new float[pointNumber * 3];
		byte[] colorArray = new byte[pointNumber * 3];

		for (int i = 0; i < pointNumber; i++) {
			float relativeX = (float) (coordinateList.get(i).x - origin.x);
			float relativeY = (float) (coordinateList.get(i).y - origin.y);
			float relativeZ = (float) (coordinateList.get(i).z - origin.z);
			coordinateArray[3 * i] = relativeX;
			coordinateArray[3 * i + 1] = relativeY;
			coordinateArray[3 * i + 2] = relativeZ;
			
			colorArray[3 * i] = (byte) colorList.get(i).getRed();
			colorArray[3 * i + 1] = (byte) colorList.get(i).getGreen();
			colorArray[3 * i + 2] = (byte) colorList.get(i).getBlue();
		}
		
		byte[] positionByte = CharacterConverter.convertToByteArray(coordinateArray);
				
		String featureTableJSONString = generateFeatureTableJSON(origin, pointNumber, 0);
		int featureTableJSONLength = featureTableJSONString.length();
		byte[] featureTableJSONByte = featureTableJSONString.getBytes();

		// Create Header
		byte[] outputByte = new byte[0];
		String magic = "pnts";
		byte[] magicByte = magic.getBytes();
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, magicByte);

		int version = Integer.valueOf("1");
		byte[] versionByte = CharacterConverter.convertToByteArray(version);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, versionByte);

		int byteLength = 28 + featureTableJSONLength + positionByte.length;
		byte[] byteLengthByte = CharacterConverter.convertToByteArray(byteLength);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, byteLengthByte);

		int featureTableJSONByteLength = featureTableJSONLength;
		byte[] featureTableJSONByteLengthByte = CharacterConverter.convertToByteArray(featureTableJSONByteLength);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, featureTableJSONByteLengthByte);

		int featureTableBinaryByteLength = positionByte.length + colorArray.length;
		byte[] featureTableBinaryByteLengthByte = CharacterConverter.convertToByteArray(featureTableBinaryByteLength);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, featureTableBinaryByteLengthByte);

		int batchTableJSONByteLength = 0;
		byte[] batchTableJSONByteLengthByte = CharacterConverter.convertToByteArray(batchTableJSONByteLength);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, batchTableJSONByteLengthByte);

		int batchTableBinaryByteLength = 0;
		byte[] batchTableBinaryByteLengthByte = CharacterConverter.convertToByteArray(batchTableBinaryByteLength);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, batchTableBinaryByteLengthByte);	
		
		// Create Feature Table
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, featureTableJSONByte);
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, positionByte);		
		outputByte = CharacterConverter.concatTwoByteArrays(outputByte, colorArray);

		// write to binary file
		writeBinaryFile(outputByte, pntsFilepath);
	}
	
	private String generateFeatureTableJSON(Coordinate origin, int pointNumber, int spaceNumber) 
			throws IOException {
		
		StringBuilder sb= new StringBuilder();
		sb.append("{\"POINTS_LENGTH\":").append(pointNumber).append(",");
		sb.append("\"RTC_CENTER\":[").append(origin.x);
		for (int i = 1; i <= spaceNumber; i++) {
			sb.append(0);
		}
		sb.append(",").append(origin.y).append(",").append(origin.z).append("],");
		sb.append("\"POSITION\":").append("{\"byteOffset\":").append(0).append("},");
		sb.append("\"RGB\":").append("{\"byteOffset\":").append(pointNumber*12).append("}}");
		
		int headerByteLength = sb.toString().getBytes().length;
		int paddingSize = headerByteLength%4;
		
		// adjust byte alignment...
		if (paddingSize != 0)
			return generateFeatureTableJSON(origin, pointNumber, 4 - paddingSize).toString();
					
		return sb.toString();
	}
	
	private void writeBinaryFile(byte[] aBytes, String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		Files.write(path, aBytes);
	}
	
}