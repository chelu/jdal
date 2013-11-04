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
package org.jdal.swing.table;


import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JButton;

import org.jdal.swing.form.FormUtils;
import org.jdal.swing.View;

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
		setName(messageSource.getMessage("HideShowFilterAction.hideFilter"));
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		View<Object> filterView = getTablePanel().getFilterView();
		if (filterView != null) {
			filterView.getPanel().setVisible(!filterView.getPanel().isVisible());
			
			String value = filterView.getPanel().isVisible() ? 
					messageSource.getMessage("HideShowFilterAction.hideFilter") :
						messageSource.getMessage("HideShowFilterAction.showFilter");
					
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
