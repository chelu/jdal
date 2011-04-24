package info.joseluismartin.gis;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

public class MapnikTileSource implements TileSource {
	protected String NAME;
	protected String BASE_URL;

	public MapnikTileSource(String name, String base_url)
	{
		NAME = name;
		BASE_URL = base_url;
	}

	public String getName() {
		return NAME;
	}

	public int getMaxZoom() {
		return 18;
	}

	public int getMinZoom() {
		return 0;
	}

	public String getExtension() {
		return "png";
	}

	public String getTilePath(int zoom, int tilex, int tiley) {
		return "/" + zoom + "/" + tilex + "/" + tiley + "." + getExtension();
	}

	public String getBaseUrl() {
		return this.BASE_URL;
	}

	public String getTileUrl(int zoom, int tilex, int tiley) {
		return this.getBaseUrl() + getTilePath(zoom, tilex, tiley);
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getTileType() {
		return "png";
	}

	public int getTileSize() {
		return 256;
	}

	public TileUpdate getTileUpdate() {
		return TileUpdate.IfNoneMatch;
	}
}

