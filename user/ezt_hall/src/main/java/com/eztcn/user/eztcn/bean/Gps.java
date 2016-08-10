package com.eztcn.user.eztcn.bean;

/**
 * @title 坐标系实体
 * @describe
 * @author ezt
 * @created 2015年4月29日
 */
public class Gps {

	private double lat;//纬度
	private double lon;//经度

	public Gps(double lat, double lon) {
		setLat(lat);
		setLon(lon);
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	
	
}
