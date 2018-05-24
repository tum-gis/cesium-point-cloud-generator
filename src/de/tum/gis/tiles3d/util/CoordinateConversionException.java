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
