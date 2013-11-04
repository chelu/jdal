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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.commons.lang.StringUtils;
import org.jdal.service.PersistentService;
import org.jdal.service.PersistentServiceAware;
import org.jdal.swing.View;
import org.jdal.swing.action.DialogAcceptAction;
import org.jdal.swing.action.ViewAction;
import org.jdal.swing.action.ViewCancelAction;
import org.jdal.ui.Editor;
import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.ui.bind.ControlChangeListener;
import org.springframework.validation.BindingResult;

/**
 * A JFrame for use as View Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewFrame<T> extends JFrame implements View<T>, Editor<T> {


	private static final long serialVersionUID = 1L;
	private View<T> view;
	private ViewAction<T> acceptAction = new DialogAcceptAction<T>();
	private ViewAction<T> cancelAction = new ViewCancelAction<T>();
	private JButton acceptButton;
	private JButton cancelButton;
	private int windowWidth;
	private int windowHeight;
	private EventListenerList listenerList = new EventListenerList();
	private PersistentService<T, ?extends Serializable> persistentService;

	/**
	 * Default Ctor
	 */
	public ViewFrame() {
		super();
	}
	
	/**
	 * Compatibility ctor with ViewDialog.
	 * @param owner
	 */
	public ViewFrame(Frame owner) {
		super();
	}

	public void init() {
		acceptAction.setView(view);
		cancelAction.setView(view);
		acceptAction.setDialog(this);
		cancelAction.setDialog(this);
		add(view.getPanel(), BorderLayout.CENTER);
		add(createButtonBox(), BorderLayout.SOUTH);
	
		if (view.getModel() != null && StringUtils.isEmpty(getTitle()))
			setTitle(view.getModel().toString());
		
		setLocationRelativeTo(null);
		setSize(new Dimension(windowWidth, windowHeight));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new CloseListener());
	}

	protected Component createButtonBox() {
		acceptButton = new JButton(acceptAction);
		cancelButton = new JButton(cancelAction);
		JPanel p = new JPanel();
		p.add(acceptButton);
		p.add(cancelButton);
		
		return p;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void save() {
		Object[] listeners = listenerList.getListenerList();
		EditorEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == EditorListener.class) {
				if (e == null) {
					e = new EditorEvent(this, getModel());
				}
				((EditorListener)listeners[i+1]).modelChanged(e);
			}	       
		}
		
	}

	public View<T> getView() {
		return view;
	}

	public void setView(View<T> view) {
		this.view = view;
	}

	public ViewAction<T> getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(ViewAction<T> acceptAction) {
		this.acceptAction = acceptAction;
		this.acceptAction.setView(view);
		this.acceptAction.setDialog(this);
	}

	public ViewAction<T> getCancelAction() {
		return cancelAction;
	}

	public void setCancelAction(ViewAction<T> cancelAction) {
		this.cancelAction = cancelAction;
		cancelAction.setDialog(this);
		cancelAction.setView(view);
	}

	public T getModel() {
		return view.getModel();
	}

	public JComponent getPanel() {
		return view.getPanel();
	}

	public void refresh() {
		view.refresh();
		setTitle(view.getModel().toString());
	}

	public void setModel(T model) {
		view.setModel(model);
	}

	public void update() {
		view.update();
	}

	
	public void clear() {
		view.clear();
	}

	public boolean validateView() {
		return view.validateView();
	}

	/**
	 * @return the windowWidth
	 */
	public int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * @param windowWidth the windowWidth to set
	 */
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * @return the windowHeight
	 */
	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * @param windowHeight the windowHeight to set
	 */
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addEditorListener(EditorListener l) {
		listenerList.add(EditorListener.class, l);
		
	}
	 
	public void removeEditorListener(EditorListener l) {
		listenerList.remove(EditorListener.class, l);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDirty() {
		return view.isDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableView(boolean enabled) {
		view.enableView(enabled);
		acceptButton.setEnabled(enabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public BindingResult getBindingResult() {
		return view.getBindingResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getErrorMessage() {
		return view.getErrorMessage();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addControlChangeListener(ControlChangeListener listener) {
		view.addControlChangeListener(listener);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeControlChangeListener(ControlChangeListener listener) {
		view.removeControlChangeListener(listener);
	}

	@SuppressWarnings("unchecked")
	public void setPersistentService(PersistentService<T, ?extends Serializable> persistentService) {
		this.persistentService = persistentService;
		
		if (this.acceptAction instanceof PersistentServiceAware)
			((PersistentServiceAware<T>) acceptAction).setPersistentService(this.persistentService);
		
	}
	
	
	private class CloseListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			cancelAction.actionPerformed(null);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}
