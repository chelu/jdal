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
package info.joseluismartin.vaadin.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class FormUtils {

	public static void addOKCancelButtons(Form f) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Button ok = newOKButton(f);
		Button cancel = newCancelButton(f);
		hl.addComponent(ok);
		hl.addComponent(cancel);
		f.getFooter().addComponent(hl);
	}

	/**
	 * @param f
	 * @return
	 */
	private static Button newCancelButton(final Form f) {
		Button cancel = new Button("OK");
		cancel.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.discard();
				// what? where is window.close() ?
				f.getApplication().removeWindow(f.getWindow());
			}
		});
		
		return cancel;
	}

	/**
	 * @param f
	 * @return
	 */
	private static Button newOKButton(final Form f) {
		Button ok = new Button("OK");
		ok.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.commit();
				// what? where is window.close() ?
				f.getApplication().removeWindow(f.getWindow());
			}
		});
		
		return ok;
	}
}
