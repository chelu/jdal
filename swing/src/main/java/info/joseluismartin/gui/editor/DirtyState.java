/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.gui.editor;

import info.joseluismartin.gui.Editor;

import java.awt.Component;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.springframework.context.MessageSource;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DirtyState implements EditorState {
	
	private MessageSource messageSource;
	private Editor editor;
	public final static String CONFIRM_MESSAGE_KEY = "editor.confirm.message";

	/**
	 * {@inheritDoc}
	 */
	public void cancel() {
		Component parent = null;
		if (editor instanceof Component)
			parent = (Component) editor;
		
		String message = messageSource.getMessage(CONFIRM_MESSAGE_KEY, null, Locale.getDefault());
		if (JOptionPane.showConfirmDialog(parent, message, "", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) 
			editor.cancel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void save() {
		editor.save();
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
	 * @return the editor
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(Editor editor) {
		this.editor = editor;
	}

}
