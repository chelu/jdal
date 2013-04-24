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
package org.jdal.swing.editor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.jdal.swing.Editor;
import org.jdal.swing.EditorEvent;
import org.jdal.swing.EditorListener;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ControlState implements ActionListener, ChangeListener, KeyListener, 
	ListSelectionListener, EditorListener {
	
	private Editor<?> editor;
	
	public void listen(Object c) {
		if (c instanceof JComboBox) {
			((JComboBox) c).addActionListener(this);
		}
		else if (c instanceof JTextComponent) {
			((JTextComponent) c).addKeyListener(this);
		}
		else if (c instanceof AbstractButton) {
			((AbstractButton) c).addActionListener(this);
		}
		else if (c instanceof JList) { 
			((JList) c).addListSelectionListener(this);
		}
		else if (c instanceof Editor) {
			((Editor<?>) c).addEditorListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void modelChanged(EditorEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the editor
	 */
	public Editor<?> getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(Editor<?> editor) {
		this.editor = editor;
	}

}
