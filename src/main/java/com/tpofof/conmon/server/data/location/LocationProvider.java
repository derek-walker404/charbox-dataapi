package com.tpofof.conmon.server.data.location;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import com.pofof.conmon.model.MyLocation;
import com.tpofof.utils.Config;

public final class LocationProvider {

	private static DatabaseReader reader;
	
	static {
		File databaseFile = new File(Config.get().getString("location.db.filename"));
		try {
			reader = new DatabaseReader.Builder(databaseFile).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private LocationProvider() { }
	
	public static MyLocation getLocation(String ip) {
		MyLocation loc = new MyLocation().setIp(ip);
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			CityResponse cityResponse = reader.city(ipAddress);
			Location location = cityResponse.getLocation();
			loc.setLat(location.getLatitude())
					.setLon(location.getLongitude());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		return loc;
	}
}
