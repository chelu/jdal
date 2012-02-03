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
package bind;

import info.joseluismartin.gui.bind.ComboAccessor;
import info.joseluismartin.gui.bind.ControlChangeListener;
import info.joseluismartin.gui.bind.ControlEvent;
import info.joseluismartin.gui.bind.ListAccessor;
import info.joseluismartin.gui.bind.TextComponentAccessor;
import info.joseluismartin.gui.bind.ToggleButtonAccessor;
import info.joseluismartin.gui.list.ListListModel;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Test ControlAccessors
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AccessorsTest extends TestCase implements ControlChangeListener {
	
	private String TEST_STRING = "test";
	private Object[] TEST_LIST = new Object[] {1, 2, 3};
	private boolean listened = false;
	
	@Test
	public void testTextFieldAccessor() {
		JTextField field = new JTextField();
		TextComponentAccessor accessor = new TextComponentAccessor(field);
		accessor.addControlChangeListener(this);
		field.setText(TEST_STRING);
		assertEquals(TEST_STRING, field.getText());
		assertEquals(TEST_STRING, accessor.getControlValue());
		assertTrue(listened);
		listened = false;
	}
	
	@Test
	public void testComboAccessor() {
		JComboBox combo = new JComboBox(TEST_LIST);
		ComboAccessor accessor = new ComboAccessor(combo);
		accessor.addControlChangeListener(this);
		combo.setSelectedItem(2);
		assertEquals(2, accessor.getControlValue());
		assertEquals(2, combo.getSelectedItem());
		assertTrue(listened);
	}
	
	@Test
	public void testListAccessor() {
		JList list = new JList();
		ListAccessor accessor = new ListAccessor(list);
		accessor.addControlChangeListener(this);
		list.setModel(new ListListModel(Arrays.asList(TEST_LIST)));
		assertEquals(Arrays.asList(TEST_LIST), accessor.getControlValue());
		assertTrue(listened);
	}
	
	@Test
	public void testToggleButtonAccessor() {
		JCheckBox check = new JCheckBox("", false);
		ToggleButtonAccessor accessor = new ToggleButtonAccessor(check);
		accessor.addControlChangeListener(this);
		check.setSelected(true);
		assertEquals(true, check.isSelected());
		assertEquals(Boolean.TRUE, accessor.getControlValue());
		assertTrue(listened);
	}

	/**
	 * {@inheritDoc}
	 */
	public void controlChange(ControlEvent e) {
		listened = true;
	}

}
