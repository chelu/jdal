package info.joseluismartin.gis;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;


public class GoogleTileSource implements TileSource {
	
	private String name = "GoogleTileSource";
	private int i = 1;
	private String type = "vt";
	private boolean usingCache = false;
	private String cacheServer = "http://localhost:8080/gtc/";
	
     public String getTileUrl(int zoom, int tilex, int tiley) {
         return getGoogleMapServer() + "x=" + tilex + "&y=" + tiley + "&zoom=" + (17 - zoom);
     }
	 
	private String getGoogleMapServer() {
		if (usingCache)
			return cacheServer;
		
		if (i++ > 2) i = 0;
		return "http://mt" + i + ".google.com/" + type + "/";
	}
	
	public String getName() { 
		return name;
	}
	
	 public TileUpdate getTileUpdate() {
         return TileUpdate.IfNoneMatch;
     }

	public int getMaxZoom() {
		return 18;
	}

	public int getMinZoom() {
		return 0;
	}

	public String getTileType() {
		return "png";
	}

	/**
	 * @return the usingCache
	 */
	public boolean isUsingCache() {
		return usingCache;
	}

	/**
	 * @param usingCache the usingCache to set
	 */
	public void setUsingCache(boolean usingCache) {
		this.usingCache = usingCache;
	}

	/**
	 * @return the cacheServer
	 */
	public String getCacheServer() {
		return cacheServer;
	}

	/**
	 * @param cacheServer the cacheServer to set
	 */
	public void setCacheServer(String cacheServer) {
		this.cacheServer = cacheServer;
	}
}
