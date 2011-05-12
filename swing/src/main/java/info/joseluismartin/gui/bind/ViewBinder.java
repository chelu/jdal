package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.View;

@SuppressWarnings("unchecked")
public class ViewBinder extends AbstractBinder {


	public void doRefresh() {
		Object value = getValue();
		View<Object> view = (View<Object>) component;
		view.setModel(value);
		view.refresh();
	}
	
	public void doUpdate() {
		View<Object> view = (View<Object>) component;
		view.update();
		setValue(view.getModel());
	}

}
