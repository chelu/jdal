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
package info.joseluismartin.gui.report;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportStringParameterEditor implements ReportParameterEditor {

	private JComponent editor;
	
	/* (non-Javadoc)
	 * @see info.joseluismartin.reporting.ReportParameterEditor#getEditor()
	 */
	public JComponent getEditor() {
		if (editor == null) editor = new JTextField(15);
		return editor;
	}

	/* (non-Javadoc)
	 * @see info.joseluismartin.reporting.ReportParameterEditor#getValue()
	 */
	public Object getValue() {
		return ((JTextField) editor).getText();
	}

}
