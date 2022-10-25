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

public class CoordinateConversionException extends Exception {

	private static final long serialVersionUID = 6764808191827326837L;

	public CoordinateConversionException() {
		super();
	}
	
	public CoordinateConversionException(String message) {
		super(message);
	}
	
	public CoordinateConversionException(Throwable cause) {
		super(cause);
	}
	
	public CoordinateConversionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
