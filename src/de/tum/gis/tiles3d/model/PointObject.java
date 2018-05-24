package de.tum.gis.tiles3d.model;

public class PointObject {
	private double x;
	private double y;
	private double z;
	int r;
	int g;
	int b;
	String srid;
	
	public PointObject(double x, double y, double z, int r, int g, int b, String srid) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.srid = srid;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public int getR() {
		return r;
	}
	
	public void setR(int r) {
		this.r = r;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public int getB() {
		return b;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	
	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}
}
