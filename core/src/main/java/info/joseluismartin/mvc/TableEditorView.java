package info.joseluismartin.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.web.servlet.view.AbstractView;

import info.joseluismartin.table.DataTableModel;

public class TableEditorView extends AbstractView {
	
	private JsonConfig jsonConfig;

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataTableModel dtm = (DataTableModel) model.get("dtm");
		JSON json =  JSONSerializer.toJSON(dtm, jsonConfig);
		json.write(response.getWriter());
	}
	
	public JsonConfig getJsonConfig() {
		return jsonConfig;
	}

	public void setJsonConfig(JsonConfig jsonConfig) {
		this.jsonConfig = jsonConfig;
	}


}
