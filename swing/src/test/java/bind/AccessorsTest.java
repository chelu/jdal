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


import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.jdal.swing.bind.ComboAccessor;
import org.jdal.swing.bind.ListAccessor;
import org.jdal.swing.bind.TextComponentAccessor;
import org.jdal.swing.bind.ToggleButtonAccessor;
import org.jdal.swing.list.ListListModel;
import org.jdal.ui.bind.ControlChangeListener;
import org.jdal.ui.bind.ControlEvent;
import org.junit.Test;


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
		JComboBox<Object> combo = new JComboBox<Object>(TEST_LIST);
		ComboAccessor accessor = new ComboAccessor(combo);
		accessor.addControlChangeListener(this);
		combo.setSelectedItem(2);
		assertEquals(2, accessor.getControlValue());
		assertEquals(2, combo.getSelectedItem());
		assertTrue(listened);
		listened = false;
	}
	
	@Test
	public void testListAccessor() {
		JList<Object> list = new JList<Object>();
		ListAccessor accessor = new ListAccessor(list);
		accessor.addControlChangeListener(this);
		list.setModel(new ListListModel<Object>(Arrays.asList(TEST_LIST)));
		assertEquals(Arrays.asList(TEST_LIST), accessor.getControlValue());
		assertTrue(listened);
		listened = false;
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
		listened = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void controlChange(ControlEvent e) {
		listened = true;
	}

}
