package info.joseluismartin.gis.google;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class RouteRequest {
	
	public enum Mode { DRIVING, WALKING, BICYCLING }; 
	private Point origin;
	private Point destination;
	private Mode mode = Mode.DRIVING;
	private String language = "es";
	
	public RouteRequest() {}
	
	public RouteRequest(Point origin, Point destination) {
		this.origin = origin;
		this.destination = destination;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("origin=");
		sb.append(origin.getX());
		sb.append(",");
		sb.append(origin.getY());
		sb.append("&");
		sb.append("destination=");
		sb.append(destination.getX());
		sb.append(",");
		sb.append(destination.getY());
		sb.append("&");
		sb.append("language=");
		sb.append(language);
		sb.append("&");
		sb.append("sensor=false");
		
		return sb.toString();
	}

	private Point getPoint(double lat, double lon) {
		Coordinate coordinate = new Coordinate(lat, lon);
		CoordinateSequence coordinates = new CoordinateArraySequence(new Coordinate[] {coordinate});
		return new Point(coordinates, new GeometryFactory(new PrecisionModel(), 4326));	
	}
	
	/**
	 * @return the origin
	 */
	public Point getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public Point getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Point destination) {
		this.destination = destination;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public void setOrigin(double lat, double lon) {
		origin = getPoint(lat, lon);
	}
	
	public void setDestination(double lat, double lon) {
		destination = getPoint(lat, lon);
	}
		
}
