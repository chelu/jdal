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
package info.joseluismartin.gui;

import info.joseluismartin.gui.form.BoxFormBuilder;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;

import org.springframework.binding.value.support.ListListModel;

/**
 * Component that show two list for selecting objects.
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Selector<T> extends JPanel {

	private static final long serialVersionUID = 1L;
	private String name;
	private ListListModel available = new ListListModel();
	private ListListModel selected = new ListListModel();
	private List<T> all = new ArrayList<T>();
	
	private JList availableList;
	private JList selectedList;
	
	private JButton addSelectedButton;
	private JButton removeSelectedButton;
	
	private Icon rightArrow;
	private Icon leftArrow;
	
	  /** A list of event listeners for this component. */
    protected EventListenerList listenerList = new EventListenerList();
    protected boolean firingActionEvent = false;

	
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
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		availableList = new JList(available);
		selectedList = new JList(selected);
		
		if (rightArrow == null) {
			rightArrow = new ImageIcon(getClass().getResource("/images/button_right.png"));
		}
		if (leftArrow == null) {
			leftArrow = new ImageIcon(getClass().getResource("/images/button_left.png"));
		}
		addSelectedButton = new JButton(new AddSelectedAction());
		removeSelectedButton = new JButton(new RemoveSelectedAction());
		
		JLabel availableLabel = new JLabel("Disponibles");
		JLabel selectedLabel = new JLabel("Asignadas");
		JScrollPane availableScroll = new JScrollPane(availableList);
		JScrollPane selectedScroll = new JScrollPane(selectedList);
		availableList.setVisibleRowCount(3);
		selectedList.setVisibleRowCount(3);
		
		// Three Columns
		BoxFormBuilder b = new BoxFormBuilder();
		
		b.add(availableLabel);
		b.next();
		b.add(selectedLabel);
		b.row();
		b.add(availableScroll);
		Box arrowBox = Box.createHorizontalBox();
		removeSelectedButton.setAlignmentX(Container.CENTER_ALIGNMENT);
		addSelectedButton.setAlignmentX(Container.CENTER_ALIGNMENT);
		arrowBox.add(removeSelectedButton);
		arrowBox.add(Box.createHorizontalStrut(5));
		arrowBox.add(addSelectedButton);
	
		b.add(arrowBox);
		arrowBox.setAlignmentY(Container.CENTER_ALIGNMENT);
		arrowBox.setAlignmentX(Container.CENTER_ALIGNMENT);
		arrowBox.setMaximumSize(new Dimension(70, 30));
		b.add(selectedScroll);
		
		Box box = (Box) b.getForm();
		box.getComponent(0).setMaximumSize(new Dimension(300, 200));
		box.getComponent(2).setMaximumSize(new Dimension(70, 30));
		box.getComponent(4).setMaximumSize(new Dimension(300, 200));
		box.setMaximumSize(new Dimension(400, 200));
		
		add(Box.createHorizontalGlue());
		add(box);
		add(Box.createHorizontalGlue());
		
		availableScroll.setMaximumSize(new Dimension(300, 200));
		availableScroll.setPreferredSize(new Dimension(300,100));
		availableScroll.setAlignmentX(Container.RIGHT_ALIGNMENT);
		availableLabel.setAlignmentX(Container.RIGHT_ALIGNMENT);
		selectedScroll.setMaximumSize(new Dimension(300, 200));
		selectedScroll.setPreferredSize(new Dimension(300,100));
		selectedScroll.setAlignmentX(Container.LEFT_ALIGNMENT);
		selectedLabel.setAlignmentX(Container.LEFT_ALIGNMENT);
	}
	
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
	 * 
	 */
	private void clearSelections() {
		availableList.clearSelection();
		selectedList.clearSelection();
	}

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
	 * 
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

	@SuppressWarnings("unchecked")
	public List<T> getAvailable() {
		return available;
	}

	public void setAvailable(List<T> available) {
		this.available.clear();
		this.available.addAll(available);
		List<T> l = new ArrayList<T>(all);
		l.removeAll(available);
		selected.clear();
		selected.addAll(l);
	}

	@SuppressWarnings("unchecked")
	public List<T> getSelected() {
		return selected;
	}

	public void setSelected(List<T> selected) {
		this.selected.clear();
		this.selected.addAll(selected);
		available.clear();
		available.addAll(all);
		available.removeAll(selected);
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
	
	private class AddSelectedAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public AddSelectedAction() {
			putValue(Action.SMALL_ICON, rightArrow);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			addSelected();
			
		}
		
	}
	
	private class RemoveSelectedAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public RemoveSelectedAction() {
			putValue(Action.SMALL_ICON, leftArrow);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			removeSelected();
		}
		
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
}
