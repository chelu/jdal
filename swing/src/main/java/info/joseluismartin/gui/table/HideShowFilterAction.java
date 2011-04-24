package info.joseluismartin.gui.table;

import info.joseluismartin.gui.View;
import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Hide and show FilterView panel Action
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class HideShowFilterAction extends TablePanelAction {

	public static final String DEFAULT_SHOW_FILTER_ICON = "/images/table/22x22/filter-show.png";
	public static final String DEFAULT_HIDE_FILTER_ICON = "/images/table/22x22/filter-hide.png";
	
	private static final long serialVersionUID = 1L;
	private Icon showFilterIcon;
	private Icon hideFilterIcon;

	public void init() {
		showFilterIcon = FormUtils.getIcon(showFilterIcon, DEFAULT_SHOW_FILTER_ICON);
		hideFilterIcon = FormUtils.getIcon(hideFilterIcon, DEFAULT_HIDE_FILTER_ICON);
		setIcon(hideFilterIcon);
		setName("Hide Filter");
	}
	
	public void actionPerformed(ActionEvent e) {
		View<Object> filterView = getTablePanel().getFilterView();
		if (filterView != null) {
			filterView.getPanel().setVisible(!filterView.getPanel().isVisible());
			String value = filterView.getPanel().isVisible() ? "Hide Filter" : "Show Filter";
			Icon icon = filterView.getPanel().isVisible() ? hideFilterIcon : showFilterIcon;
			setName(value);
			setIcon(icon);
		}
		else {
			((JButton) e.getSource()).setEnabled(false);
		}
	}
	
	/**
	 * @return the showFilterIcon
	 */
	public Icon getShowFilterIcon() {
		return showFilterIcon;
	}

	/**
	 * @param showFilterIcon the showFilterIcon to set
	 */
	public void setShowFilterIcon(Icon showFilterIcon) {
		this.showFilterIcon = showFilterIcon;
	}

}
