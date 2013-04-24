/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.swing.list;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.jdal.swing.AbstractView;
import org.jdal.swing.GuiFactory;
import org.jdal.swing.ViewDialog;
import org.jdal.swing.action.BeanAction;
import org.jdal.swing.form.BoxFormBuilder;
import org.jdal.swing.form.FormUtils;
import org.jdal.swing.View;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * View for Lists
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class ListView<T> extends AbstractView<List<T>> 	{

	private static final String DEFAULT_ADD_ICON= "org/freedesktop/tango/16x16/actions/list-add.png";
	private static final String DEFAULT_REMOVE_ICON= "org/freedesktop/tango/16x16/actions/list-remove.png";
	@Autowired
	private GuiFactory guiFactory;
	private Icon addIcon;
	private Icon removeIcon;
	private JList list = new JList(new ListListModel());
	private ListCellRenderer listCellRenderer;
	private String editor;


	public ListView() {
		super(new ArrayList<T>());
	}

	public void init() {
		addIcon = FormUtils.getIcon(addIcon, DEFAULT_ADD_ICON);
		removeIcon = FormUtils.getIcon(removeIcon, DEFAULT_REMOVE_ICON);
		if (listCellRenderer != null) 
			list.setCellRenderer(listCellRenderer);

		refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	protected JComponent buildPanel() {
		BoxFormBuilder fb = new BoxFormBuilder();

		fb.row();
		fb.startBox();
		fb.setFixedHeight(true);
		fb.row();
		fb.add(new JButton(new AddCommandAction()), 25);
		fb.add(new JButton(new RemoveCommandAction()), 25);
		fb.add(Box.createHorizontalGlue());
		fb.endBox();
		fb.row(Short.MAX_VALUE);
		fb.add(new JScrollPane(list));

		return fb.getForm();

	}

	private class AddCommandAction extends BeanAction {

		public AddCommandAction() {
			setIcon(addIcon);
		}

		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			View<T> view = (View<T>) guiFactory.getView(editor);
			ViewDialog<T> dlg = new ViewDialog<T>();
			dlg.setView(view);
			dlg.setLocationRelativeTo(getPanel());
			dlg.setModal(true);
			dlg.init();
			dlg.setSize(600, 400);
			dlg.setVisible(true);

			if (dlg.isAccepted()) {
				ListListModel listModel = (ListListModel) list.getModel();
				listModel.add(view.getModel());
			}
		}
	}

	private class RemoveCommandAction extends BeanAction {

		public RemoveCommandAction() {
			setIcon(removeIcon);
		}

		public void actionPerformed(ActionEvent e) {
			ListListModel listModel = (ListListModel) list.getModel();
			listModel.removeAll(Arrays.asList(list.getSelectedValues()));
		}
	}

	
}

