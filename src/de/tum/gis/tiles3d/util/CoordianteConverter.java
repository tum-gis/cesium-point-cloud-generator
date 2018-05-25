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

package de.tum.gis.tiles3d.util;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

import de.tum.gis.tiles3d.model.BoundingBox2D;
import de.tum.gis.tiles3d.model.Region;

public class CoordianteConverter {

	final static String WGS84_2D_Geographic_Srid = "4326";
	
	public static Coordinate convertPointToWGS84Cartesian(Coordinate inputCoordinate, String srid) throws CoordinateConversionException{		
	
		Coordinate wgs84Coordinate = convertPointBetweenCrs(inputCoordinate, srid, WGS84_2D_Geographic_Srid);
		wgs84Coordinate.z = inputCoordinate.z;
		
		CoordinateReferenceSystem wgs84_3D_CRS = DefaultGeographicCRS.WGS84_3D;;
		CoordinateReferenceSystem wgs84_3D_Cartesian = DefaultGeocentricCRS.CARTESIAN;
		
		return convertPointBetweenCrs(wgs84Coordinate, wgs84_3D_CRS, wgs84_3D_Cartesian);
	}
	
	public static Region convert2DBoundingboxToWGS84Region(BoundingBox2D boundingbox, String srid) throws CoordinateConversionException {
		double minX = boundingbox.getMinX();
		double minY = boundingbox.getMinY();
		double maxX = boundingbox.getMaxX();
		double maxY = boundingbox.getMaxY();	
		
		Coordinate projectedLowerCorner = new Coordinate(minX, minY, 0);
		Coordinate projectedUpperCorner = new Coordinate(maxX, maxY, 0);
		Coordinate wgs84LowerCorner = CoordianteConverter.convertPointBetweenCrs(projectedLowerCorner, srid, WGS84_2D_Geographic_Srid);
		Coordinate wgs84UpperCorner = CoordianteConverter.convertPointBetweenCrs(projectedUpperCorner, srid, WGS84_2D_Geographic_Srid);
		
		double minLon = wgs84LowerCorner.x;
		double minLat = wgs84LowerCorner.y;
		double maxLon = wgs84UpperCorner.x;
		double maxLat = wgs84UpperCorner.y;
		
		return new Region(minLon, minLat, maxLon, maxLat, 0, 0);
	}
	
	private static Coordinate convertPointBetweenCrs(Coordinate inputCoordinate, String _SourceSrs, String _TargetSrs) throws CoordinateConversionException {		
		CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);		
		CoordinateReferenceSystem srcCRS = null;
		CoordinateReferenceSystem dstCRS = null;
		try {
			srcCRS = factory.createCoordinateReferenceSystem("EPSG:" + _SourceSrs);
			dstCRS = factory.createCoordinateReferenceSystem("EPSG:" + _TargetSrs);
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new CoordinateConversionException("Unsupported SRID number. More details: ", e);			
		}

		return convertPointBetweenCrs(inputCoordinate, srcCRS, dstCRS);
	}
	
	private static Coordinate convertPointBetweenCrs(Coordinate inputCoordinate, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem dstCRS) throws CoordinateConversionException {				
		Coordinate outputCoordinate = null;		
		MathTransform transform;

		try {
			transform = CRS.findMathTransform(srcCRS, dstCRS, false);
			outputCoordinate = JTS.transform(inputCoordinate, outputCoordinate, transform);
		} catch (MismatchedDimensionException | TransformException e) {
			e.printStackTrace();
			throw new CoordinateConversionException("Faild to perform cooridnate transformation. "
					+ "The source and target CRSs may have mismatched dimensions (e.g., one is 2D, and the other one is 3D). More details: ", e);	
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new CoordinateConversionException("Faild to perform cooridnate transformation. "
					+ "This transformation is not supported by GeoTools API. More details: ", e);
		} 
		
		double outputX = outputCoordinate.x;
		double outputY = outputCoordinate.y;
		double outputZ = outputCoordinate.z;		
		outputCoordinate = new Coordinate(outputX, outputY, outputZ);
		
		return outputCoordinate;
	}
}
