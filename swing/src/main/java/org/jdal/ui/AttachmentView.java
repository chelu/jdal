package org.jdal.ui;


import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.jdal.model.Attachment;
import org.jdal.system.SystemUtils;
import org.jdal.ui.form.BoxFormBuilder;
import org.jdal.ui.form.FormUtils;
import org.jdal.ui.list.ListListModel;

/**
 * Attachment View 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AttachmentView extends AbstractView<List<Attachment>> {

	private static final String DEFAULT_ADD_ICON= "org/freedesktop/tango/16x16/actions/list-add.png";
	private static final String DEFAULT_REMOVE_ICON= "org/freedesktop/tango/16x16/actions/list-remove.png";
	private JList attachments = new JList(new ListListModel());
	private Icon addIcon;
	private Icon removeIcon;
	private ListCellRenderer listCellRenderer;
	
	public AttachmentView() {
		this(new ArrayList<Attachment>());
	}
	
	public AttachmentView(ArrayList<Attachment> attachments) {
		setModel(attachments);
	}

	public void init() {
		addIcon = FormUtils.getIcon(addIcon, DEFAULT_ADD_ICON);
		removeIcon = FormUtils.getIcon(removeIcon, DEFAULT_REMOVE_ICON);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		JScrollPane scroll = new JScrollPane(attachments);
		attachments.setCellRenderer(listCellRenderer);
		attachments.addMouseListener(new AttachmentMouseListener());
		
		BoxFormBuilder fb = new BoxFormBuilder();
		fb.row();
		fb.startBox();
		fb.setFixedHeight(true);
		fb.row();
		fb.add(new JButton(new AddAction()));
		fb.setMaxWidth(25);
		fb.add(new JButton(new RemoveAction()));
		fb.setMaxWidth(25);
		fb.endBox();
		fb.row(Short.MAX_VALUE);
		fb.add(scroll);
		
		return fb.getForm();
	}

	/**
	 * Add Attachments from user selections
	 */
	private void addAttachment() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);

		if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(getPanel())) {
			File[] files = chooser.getSelectedFiles();
			List<Attachment> addList = new ArrayList<Attachment>(files.length);
			for (File file : files) {
				try {
					addList.add(new Attachment(file));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getPanel(), 
							getMessage("AttachmentView.cantReadFile") + " " + file.getName());
				}
			}
			ListListModel listModel = (ListListModel) attachments.getModel();
			listModel.addAll(addList);
		}
	}
	
	/**
	 * Remove Attachments
	 */
	private void removeAttachment() {
		Object[] values = attachments.getSelectedValues();
		ListListModel listModel = (ListListModel) attachments.getModel();
		listModel.removeAll(Arrays.asList(values));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRefresh() {
		ListListModel llm = (ListListModel) attachments.getModel();
		llm.clear();
		llm.addAll(getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doUpdate() {
		getModel().clear();
		ListListModel llm = (ListListModel) attachments.getModel();
		getModel().addAll(llm.getList());
	}

	public Icon getAddIcon() {
		return addIcon;
	}

	public void setAddIcon(Icon addIcon) {
		this.addIcon = addIcon;
	}

	public Icon getRemoveIcon() {
		return removeIcon;
	}

	public void setRemoveIcon(Icon removeIcon) {
		this.removeIcon = removeIcon;
	}
	
	public ListCellRenderer getListCellRenderer() {
		return listCellRenderer;
	}

	public void setListCellRenderer(ListCellRenderer listCellRenderer) {
		this.listCellRenderer = listCellRenderer;
	}

	private class AddAction extends AbstractAction {
		
		public AddAction() {
			putValue(Action.SMALL_ICON, addIcon);
		}

		public void actionPerformed(ActionEvent e) {
			addAttachment();
			
		}
	}
	
	private class RemoveAction extends AbstractAction {
		
		public RemoveAction() {
			putValue(Action.SMALL_ICON, removeIcon);
		}

		public void actionPerformed(ActionEvent e) {
			removeAttachment();
		}
	}
	
	/**
	 * Open attachments on double clicks 
	 */
	private class AttachmentMouseListener extends MouseAdapter {
	
		@Override
		public void mouseClicked(MouseEvent e){
			if (e.getClickCount() == 2) {
				int index = attachments.locationToIndex(e.getPoint());
				ListModel dlm = attachments.getModel();
				Attachment a = (Attachment) dlm.getElementAt(index);
				attachments.ensureIndexIsVisible(index);
				SystemUtils.open(a.getData(), a.getName());
			}
		}
		
	}
	
}
