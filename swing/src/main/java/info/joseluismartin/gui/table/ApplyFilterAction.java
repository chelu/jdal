package info.joseluismartin.gui.table;

import info.joseluismartin.gui.View;
import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

public class ApplyFilterAction extends TablePanelAction {

	public static final String DEFAULT_ICON = "/images/table/22x22/edit-find.png";

	private static final long serialVersionUID = 1L;

	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}


	public void actionPerformed(ActionEvent arg0) {
		View<Object> filterView = getTablePanel().getFilterView();
		filterView.update();

		if (filterView.validateView()) {
			getTablePanel().getTable().setFilter(filterView.getModel());
			getTablePanel().refresh();
		}
	}
}
