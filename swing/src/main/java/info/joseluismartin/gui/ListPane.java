package info.joseluismartin.gui;

import info.joseluismartin.gui.form.FormUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A Container with List for select visible panel.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ListPane extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_TABLE_ICON = "/images/table/table.png";
	private List<PanelHolder> panels;
	private JList list = new JList();
	private Icon tableIcon;
	private JPanel editorPanel = new JPanel(new BorderLayout());
	private JSplitPane split;
	
	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		tableIcon = FormUtils.getIcon(tableIcon, DEFAULT_TABLE_ICON);
		for (PanelHolder p : panels)
			p.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
			
		list = new JList(panels.toArray());
		list.setBorder(BorderFactory.createEmptyBorder(5, 5	, 5, 5));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		JScrollPane scroll = new JScrollPane(list);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, editorPanel);
		add(split);
		list.addListSelectionListener(this);
		list.setCellRenderer(new ListCellRenderer());
		list.setSelectedIndex(0);
	}
	
	
	public void valueChanged(ListSelectionEvent e) {
		PanelHolder panel = (PanelHolder) list.getSelectedValue();
		editorPanel.removeAll();
		editorPanel.add(panel.getPanel());
		editorPanel.revalidate();
		editorPanel.repaint();
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
}
