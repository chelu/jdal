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
import java.awt.Component;
import java.awt.Frame;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.jdal.service.PersistentService;
import org.jdal.service.PersistentServiceAware;
import org.jdal.swing.action.DialogAcceptAction;
import org.jdal.swing.action.ViewAction;
import org.jdal.swing.action.ViewCancelAction;
import org.jdal.swing.View;
import org.jdal.ui.Editor;
import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.ui.bind.ControlChangeListener;
import org.springframework.validation.BindingResult;

/**
 * A JDialog for use as View Wrapper
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewDialog<T> extends JDialog implements View<T>, Editor<T>  {
	
	public static final int OK = 0;
	public static final int CANCEL= 1;
	
	private static final long serialVersionUID = 1L;
	private View<T> view;
	private ViewAction<T> acceptAction = new DialogAcceptAction<T>();
	private ViewAction<T> cancelAction = new ViewCancelAction<T>(); 
	private JButton acceptButton;
	private JButton cancelButton;
	private int windowWidth = 750;
	private int windowHeight = 750;
	private int value = CANCEL;
	private PersistentService<T, ?extends Serializable> persistentService;
	private ArrayList<EditorListener> editorListeners = new ArrayList<EditorListener>();
	
	public ViewDialog() {
		this(null);
	}
	
	/**
	 * @param owner
	 */
	public ViewDialog(Frame owner) {
		super(owner);
		acceptAction.setDialog(this);
		cancelAction.setDialog(this);
	}

	public void init() {
		acceptAction.setView(view);
		cancelAction.setView(view);

		add(view.getPanel(), BorderLayout.CENTER);
		if (view.getModel() != null && StringUtils.isEmpty(getTitle()))
			setTitle(view.getModel().toString());
		add(createButtonBox(), BorderLayout.SOUTH);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
	}

	protected Component createButtonBox() {
		acceptButton = new JButton(acceptAction);
		cancelButton = new JButton(cancelAction);
		JPanel p = new JPanel();
		p.add(acceptButton);
		p.add(cancelButton);
		
		return p;
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
		cancelAction.setView(view);
		cancelAction.setDialog(this);
	}
	
	public boolean isAccepted() {
		return value == OK;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the dialogWidth
	 * @deprecated use getWindowWidth
	 */
	@Deprecated
	public int getDialogWidth() {
		return windowWidth;
	}

	/**
	 * @param dialogWidth the dialogWidth to set
	 * @deprecated use setWindowWidth
	 */
	@Deprecated
	public void setDialogWidth(int dialogWidth) {
		this.windowWidth = dialogWidth;
	}

	/**
	 * @return the dialogHeight
	 * @deprecated use getWindowHeight
	 */
	@Deprecated
	public int getDialogHeight() {
		return windowHeight;
	}

	/**
	 * @param dialogHeight the dialogHeight to set
	 * @deprecated use setWindoHeigth
	 */
	@Deprecated
	public void setDialogHeight(int dialogHeight) {
		this.windowHeight = dialogHeight;
	}

	public void clear() {
		view.clear();
		
	}

	public T getModel() {
		return view.getModel();
	}

	public JComponent getPanel() {
		return view.getPanel();
	}

	public void refresh() {
		if (view.getModel() != null)
			setTitle(view.getModel().toString());
		
		view.refresh();
	}

	public void setModel(T model) {
		view.setModel(model);
	}

	public void update() {
		view.update();
	}

	public boolean validateView() {
		return view.validateView();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDirty() {
		return false;
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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void setPersistentService(PersistentService<T, ? extends Serializable> persistentService) {
		if (this.acceptAction instanceof PersistentServiceAware)
			((PersistentServiceAware<T>) acceptAction).setPersistentService(persistentService);
		
		this.persistentService = persistentService;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addEditorListener(EditorListener listener) {
		if (!editorListeners.contains(listener))
				editorListeners.add(listener);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void save() {
		// persistentService.save(view.getModel());
		fireModelChanged();
		
	}

	/**
	 * Notify editor listeners that model changed
	 */
	private void fireModelChanged() {
		EditorEvent event = new EditorEvent(this, getModel());
		
		for (EditorListener listener : editorListeners) {
			listener.modelChanged(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeEditorListener(EditorListener listener) {
		editorListeners.remove(listener);
		
	}

	/**
	 * @return the windwoWidth
	 */
	public int getWindwoWidth() {
		return windowWidth;
	}

	/**
	 * @param windwoWidth the windwoWidth to set
	 */
	public void setWindwoWidth(int windwoWidth) {
		this.windowWidth = windwoWidth;
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
	 * {@inheritDoc}
	 */
	public void cancel() {
		
	}

}
