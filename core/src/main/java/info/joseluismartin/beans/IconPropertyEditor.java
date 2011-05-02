/*
 * Copyright 2008-2010 the original author or authors.
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
package info.joseluismartin.beans;

import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyEditorSupport;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * PropertyEditor for javax.swing.Icon
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class IconPropertyEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(IconPropertyEditor.class);

	/**
	 * Load image from classpath 
	 * @param text the classpath of image resource
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Resource resource = new ClassPathResource(text);
		Icon icon = null;
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(resource.getURL());
			icon = new ImageIcon(image);
		} catch (IOException e) {
			log.error(e);
		}
		setValue(icon);
	}
}
