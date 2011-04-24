package info.joseluismartin.gui;

import javax.swing.JComponent;

public class ViewPanelHolder extends PanelHolder {

	public ViewPanelHolder() {
		
	}
	
	public ViewPanelHolder(View<?> view) {
		super();
		this.view = view;
	}

	private View<?> view;

	/**
	 * @return the view
	 */
	public View<?> getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(View<?> view) {
		this.view = view;
	}

	@Override
	public JComponent getPanel() {
		return view.getPanel();
	}

	@Override
	public String getName() {
		return view.getName();
	}
}
