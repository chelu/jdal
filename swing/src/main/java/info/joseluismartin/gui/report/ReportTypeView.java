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
package info.joseluismartin.gui.report;

import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.form.BoxFormBuilder;
import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.reporting.ReportType;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportTypeView extends AbstractView<ReportType>{

	private JComboBox type = FormUtils.newCombo(25);
	
	
	public ReportTypeView(){
		this(new ReportType());
	}
	
	public ReportTypeView(ReportType model){
		setModel(model);
	}
	
	public void init(){
		bind(type, "name");
		doRefresh();
	}
	
	@Override
	protected JComponent buildPanel() {
		// Build Form
		BoxFormBuilder b = new BoxFormBuilder();
		
		b.add("Tipo: ", type);
		
		return b.getForm();
	}
	
	public JComboBox getType(){
		return type;
	}
}
