/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.joseluismartin.gis.google;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class DirectionsService {
	
	private static final Log log = LogFactory.getLog(DirectionsService.class);
	private String serverUrl = "http://maps.google.com/maps/api/directions/json";
	private GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
	
	public DirectionsService() {
	}
	
	public MultiLineString getRoute(RouteRequest request) {
	
		try {
			URL url = new URL(serverUrl + "?" + request.toString());
			log.debug("Sending request: " + url.toString());
			return parseResponse(url.getContent());
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	private MultiLineString parseResponse(Object response) throws IOException {
		
		List<LineString> lines = new ArrayList<LineString>();
		
		String jsonString = responseToString(response);
		JSONObject  json =  JSONObject.fromObject(jsonString);
		
		if ("OK".equals(json.get("status"))) { 
			JSONArray routes = json.getJSONArray("routes");
			JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
			JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");
			
			if (log.isDebugEnabled())
				log.debug("Get route with " + steps.size() + " steps");
			
			Iterator iter = steps.iterator();
			
			while (iter.hasNext()) {
				JSONObject step = (JSONObject) iter.next();
				String polyline = step.getJSONObject("polyline").getString("points");
				LineString line = decodePolyline(polyline);
				lines.add(line);
			}
		}
		
		return new MultiLineString(lines.toArray(new LineString[] {}), factory);
	}
	
	private String responseToString(Object response) throws IOException  {
		Writer writer = new StringWriter();
		InputStreamReader reader = new InputStreamReader((InputStream) response);
		int i;
		while ((i = reader.read()) != -1)
			writer.write(i);
		
		return writer.toString();
		
	}
	
	
	
	private LineString decodePolyline(String encoded) {

		List<Coordinate> coordinates = new ArrayList<Coordinate>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        coordinates.add(new Coordinate(lat/1E5, lng/1E5 ));
	    }
	    
	    return new LineString(new CoordinateArraySequence(coordinates.toArray(new Coordinate[] {})), factory);
	}
	
	public static void main(String[] args) {
		RouteRequest request = new RouteRequest();
		request.setDestination(37.2582d, -6.9542);
		request.setOrigin(37.2546d, -6.9504d);
		
		DirectionsService service = new DirectionsService();
		service.getRoute(request);
	}

}
