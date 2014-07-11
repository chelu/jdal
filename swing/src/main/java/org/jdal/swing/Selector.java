/*
 * Copyright 2002-2010 the original author or authors.
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.jdal.swing.form.BoxFormBuilder;
import org.jdal.swing.form.FormUtils;
import org.jdal.swing.list.ListListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * A Twing List editor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Selector<T> extends JPanel {

	private String name;
	/** available list items */
	private ListListModel available = new ListListModel();
	/** selected list items */
	private ListListModel selected = new ListListModel();
	/** all items */
	private List<T> all = new ArrayList<T>();

	private JList availableList;
	private JList selectedList;
	
	private JTextField availableSearch = new JTextField();
	private JTextField selectedSearch = new JTextField();

	private Icon rightArrow = FormUtils.getIcon("images/button_right.png");
	private Icon leftArrow = FormUtils.getIcon("images/button_left.png");

	/** A list of event listeners for this component. */
	protected EventListenerList listenerList = new EventListenerList();
	protected boolean firingActionEvent = false;
	@Autowired
	private MessageSource messageSource;
	
	private int buttonWidth = 30;
	private int buttonHeight = 30;
	private int listWidth = 300;
	private int listheight = 100;
	private boolean showSearchFields = false;

	public Selector() {
	}

	public Selector(List<T> all) {
		this(all, new ArrayList<T>());
		this.all = all;

	}

	public Selector(List<T> available, List<T> selected) {
		this.available = new ListListModel(available);
		this.selected = new ListListModel(selected);
		all.addAll(available);
		all.addAll(selected);
	}

	public void init() {
		if (availableList == null) {
			availableList = new JList(available);
		} 
		else {
			availableList.setModel(available);
		}
		if (selectedList == null) {
			selectedList = new JList(selected);
		} 
		else {
			selectedList.setModel(selected);
		}
		
		availableSearch.setVisible(showSearchFields);
		selectedSearch.setVisible(showSearchFields);
		
		JButton addButton = new JButton(new AddSelectedAction());
		JButton removeButton = new JButton(new RemoveSelectedAction());
		addButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
		removeButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
		
		JScrollPane availableScroll = new JScrollPane(availableList);
		JScrollPane selectedScroll = new JScrollPane(selectedList);
		availableScroll.setPreferredSize(new Dimension(listWidth, listheight));
		selectedScroll.setPreferredSize(new Dimension(listWidth, listheight));
		availableScroll.setMinimumSize(new Dimension(listWidth, listheight));
		selectedScroll.setMinimumSize(new Dimension(listWidth, listheight));
		


		// test message source
		if (messageSource == null) {
			messageSource= new ResourceBundleMessageSource();
			((ResourceBundleMessageSource) messageSource).setBasename("i18n.jdal");
		}

		MessageSourceAccessor msa = new MessageSourceAccessor(messageSource);
		
		BoxFormBuilder fb = new BoxFormBuilder();

		fb.row(Short.MAX_VALUE);
		fb.startBox();
		fb.row();
		fb.add(availableSearch);
		fb.row();
		fb.add(FormUtils.newLabelForBox(msa.getMessage("Selector.available")));
		fb.row(Short.MAX_VALUE);
		fb.add(availableScroll);
		fb.endBox();
		fb.startBox();
		fb.row(Short.MAX_VALUE);
		fb.add(Box.createVerticalGlue());
		fb.row(buttonHeight);
		fb.add(removeButton);
		fb.row(Short.MAX_VALUE);
		fb.add(Box.createVerticalGlue());
		fb.endBox();
		fb.setMaxWidth(buttonWidth);
		fb.startBox();
		fb.row(Short.MAX_VALUE);
		fb.add(Box.createVerticalGlue());
		fb.row(buttonHeight);
		fb.add(addButton);
		fb.row(Short.MAX_VALUE);
		fb.add(Box.createVerticalGlue());
		fb.endBox();
		fb.setMaxWidth(buttonWidth);
		fb.startBox();
		fb.row();
		fb.add(selectedSearch);
		fb.row();
		fb.add(FormUtils.newLabelForBox(msa.getMessage("Selector.selected")));
		fb.row(Short.MAX_VALUE);
		fb.add(selectedScroll);
		fb.endBox();
		
		setLayout(new BorderLayout());
		add(fb.getForm(), BorderLayout.CENTER);
	}

	/**
	 * Add selected values to selected list.
	 */
	private void addSelected() {
		Object[] selectedValues = availableList.getSelectedValues();
		if (selectedValues.length > 0) {
			ListListModel availableModel  = (ListListModel) availableList.getModel();
			availableModel.removeAll(Arrays.asList(selectedValues));
			ListListModel  selectedModel = (ListListModel) selectedList.getModel();
			selectedModel.addAll(Arrays.asList(selectedValues));
			clearSelections();
			fireActionEvent();
		}
	}

	/**
	 * Clear selections on both lists
	 */
	private void clearSelections() {
		availableList.clearSelection();
		selectedList.clearSelection();
	}

	/**
	 * Remove selected values from selected list
	 */
	private void removeSelected() {
		Object[] selectedValues = selectedList.getSelectedValues();
		if (selectedValues.length > 0) {
			ListListModel selectedModel  = (ListListModel) selectedList.getModel();
			selectedModel.removeAll(Arrays.asList(selectedValues));
			ListListModel  availableModel = (ListListModel) availableList.getModel();
			availableModel.addAll(Arrays.asList(selectedValues));
			clearSelections();
			fireActionEvent();
		}
	}

	/**
	 * Notify listeners that selected values changes
	 */
	protected void fireActionEvent() {
		if (!firingActionEvent) {
			firingActionEvent = true;
			ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectorChanged");

			for (ActionListener listener : listenerList.getListeners(ActionListener.class))
				listener.actionPerformed(event);

			firingActionEvent = false;
		}
	}

	/**
	 * Get Available list
	 * @return List with available values
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAvailable() {
		return available.getList();
	}

	/**
	 * Sets the available list
	 * @param available list to set
	 */
	public void setAvailable(List<T> available) {
		this.available.clear();
		this.available.addAll(available);
		List<T> l = new ArrayList<T>(all);
		l.removeAll(available);
		selected.clear();
		selected.addAll(l);
	}

	/**
	 * Gets the selected values
	 * @return List with selected values
	 */
	@SuppressWarnings("unchecked")
	public List<T> getSelected() {
		return selected.getList();
	}

	public void setSelected(List<T> selected) {
		this.selected.clear();
		available.clear();
		available.addAll(all);
		
		if (selected != null) {
			this.selected.addAll(selected);
			available.removeAll(selected);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Icon getRightArrow() {
		return rightArrow;
	}

	public void setRightArrow(Icon rightArrow) {
		this.rightArrow = rightArrow;
	}

	public Icon getLeftArrow() {
		return leftArrow;
	}

	public void setLeftArrow(Icon leftArrow) {
		this.leftArrow = leftArrow;
	}


	/**
	 * Add an ActionListener
	 * @param listener ActionListener to add
	 */
	public void addActionListener(ActionListener listener) {
		if (listener != null) 
			listenerList.add(ActionListener.class, listener);
	}

	/**
	 * Remove an ActionListener
	 * @param listener ActionListener to remove
	 */
	public void removeActionListner(ActionListener listener) {
		listenerList.remove(ActionListener.class, listener);
	}

	/**
	 * @return the all
	 */
	public List<T> getAll() {
		return all;
	}

	/**
	 * @param all the all to set
	 */
	public void setAll(List<T> all) {
		this.all = all;
	}
	
	/**
	 * @return the buttonWidth
	 */
	public int getButtonWidth() {
		return buttonWidth;
	}

	/**
	 * @param buttonWidth the buttonWidth to set
	 */
	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}

	/**
	 * @return the buttonHeight
	 */
	public int getButtonHeight() {
		return buttonHeight;
	}

	/**
	 * @param buttonHeight the buttonHeight to set
	 */
	public void setButtonHeight(int buttonHeight) {
		this.buttonHeight = buttonHeight;
	}

	/**
	 * @return the showSearchFields
	 */
	public boolean isShowSearchFields() {
		return showSearchFields;
	}

	/**
	 * @param showSearchFields the showSearchFields to set
	 */
	public void setShowSearchFields(boolean showSearchFields) {
		this.showSearchFields = showSearchFields;
	}
	
	public static void main(String[] args) {
		ApplicationContextGuiFactory.setPlasticLookAndFeel();
		Selector<Object> selector = new Selector<Object>();
		selector.setAvailable((Arrays.asList(new Object[] {"lala"})));
		selector.init();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(selector);
		f.setSize(800, 800);
		f.setVisible(true);
	}
	
	private class AddSelectedAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public AddSelectedAction() {
			putValue(Action.SMALL_ICON, rightArrow);
		}

		public void actionPerformed(ActionEvent e) {
			addSelected();

		}
	}

	private class RemoveSelectedAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public RemoveSelectedAction() {
			putValue(Action.SMALL_ICON, leftArrow);
		}

		public void actionPerformed(ActionEvent e) {
			removeSelected();
		}
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the listWidth
	 */
	public int getListWidth() {
		return listWidth;
	}

	/**
	 * @param listWidth the listWidth to set
	 */
	public void setListWidth(int listWidth) {
		this.listWidth = listWidth;
	}

	/**
	 * @return the listheight
	 */
	public int getListheight() {
		return listheight;
	}

	/**
	 * @param listheight the listheight to set
	 */
	public void setListheight(int listheight) {
		this.listheight = listheight;
	}

	/**
	 * @return the availableList
	 */
	public JList getAvailableList() {
		return availableList;
	}

	/**
	 * @param availableList the availableList to set
	 */
	public void setAvailableList(JList availableList) {
		this.availableList = availableList;
	}
	
	/**
	 * @return the selectedList
	 */
	public JList getSelectedList() {
		return selectedList;
	}

	/**
	 * @param selectedList the selectedList to set
	 */
	public void setSelectedList(JList selectedList) {
		this.selectedList = selectedList;
	}

	
}
