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
package org.jdal.swing;


import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.swing.View;
import org.jdal.swing.form.FormUtils;
import org.jdal.swing.list.ListListModel;

/**
 * A Container with List for select visible panel.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ListPane extends JPanel implements ListSelectionListener {

	public static final String DEFAULT_TABLE_ICON = "/images/table/table.png";
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ListPane.class);
	private List<PanelHolder> panels = new ArrayList<PanelHolder>();
	private JList list;
	private Icon tableIcon;
	private JPanel editorPanel = new JPanel(new BorderLayout());
	private JSplitPane split;
	private ListCellRenderer renderer = new ListCellRenderer();
	private List<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();
	private int cellHeight = 0;
	
	public ListPane() {
		
	}
	
	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		tableIcon = FormUtils.getIcon(tableIcon, DEFAULT_TABLE_ICON);
		for (PanelHolder p : panels)
			p.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
			
		list = new JList(new ListListModel(panels));
		list.setBorder(BorderFactory.createEmptyBorder(5, 5	, 5, 5));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(this);
		list.setCellRenderer(renderer);
		list.setSelectedIndex(0);
		
		if (cellHeight != 0)
			list.setFixedCellHeight(cellHeight);
		
		JScrollPane scroll = new JScrollPane(list);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, editorPanel);
		split.setResizeWeight(0);
		split.setDividerLocation(150);
		add(split);
	}
	
	
	public void valueChanged(ListSelectionEvent e) {
		PanelHolder panel = (PanelHolder) list.getSelectedValue();
		editorPanel.removeAll();
		editorPanel.add(panel.getPanel());
		editorPanel.revalidate();
		editorPanel.repaint();
		
		fireValueChanged(e);
	}


	/**
	 * @param e
	 */
	private void fireValueChanged(ListSelectionEvent e) {
		for (ListSelectionListener lsl : listeners)
			lsl.valueChanged(e);
	}
	
	public void addListSelectionListener(ListSelectionListener l) {
		if (!listeners.contains(l)) 
			listeners.add(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l) {
		listeners.remove(l);
	}
	
	/**
	 * @return the tableIcon
	 */
	public Icon getTableIcon() {
		return tableIcon;
	}

	/**
	 * @param tableIcon the tableIcon to set
	 */
	public void setTableIcon(Icon tableIcon) {
		this.tableIcon = tableIcon;
	}
	
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}
	
	class ListCellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			PanelHolder panel = (PanelHolder) value;
			setText(panel.getName());
			Icon icon = panel.getIcon() != null ? panel.getIcon() : tableIcon;
			setIcon(icon);
			
			return this;
		}
	}

	/**
	 * @return the panels
	 */
	public List<PanelHolder> getPanels() {
		return panels;
	}


	/**
	 * @param panels the panels to set
	 */
	public void setPanels(List<PanelHolder> panels) {
		this.panels = panels;
	}
	
	public void addPanel(Object panel, String name, Icon icon) {
		PanelHolder holder = null;
		if (panel instanceof View) {
			holder = new ViewPanelHolder((View<?>) panel);
		}
		else if (panel instanceof JComponent) {
			holder = new JComponentPanelHolder((JComponent) panel);
		}
		else {
			if (log.isWarnEnabled())
				log.warn("Try to add unsupported object type [" + panel.getClass().getName());
			return;
		}
		
		holder.setIcon(icon);
		holder.setName(name); 
		panels.add(holder);
	}
}
