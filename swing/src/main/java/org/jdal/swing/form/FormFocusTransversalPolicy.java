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
package org.jdal.swing.form;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * FocusTraversalProvider for BoxFormBuilder
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class FormFocusTransversalPolicy extends FocusTraversalPolicy {
	
	private static final Log log = LogFactory.getLog(FormFocusTransversalPolicy.class);
	ArrayList<Component> components = new ArrayList<Component>();
	
	@Override
	public Component getComponentAfter(Container container,
			Component component) {
		
		// Fix awt bug looking for ComboBoxEditor instead ComboBox
		// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6205817
		if (component.getParent() instanceof JComboBox)
				component = component.getParent();
	
		int index = components.indexOf(component);
		
		if (index == -1) { // not owner
			Container childContainer =  getTopmostProvider(container, component);
			if (childContainer == null)
				return getFirstComponent(container);
			
			FocusTraversalPolicy ftp = childContainer.getFocusTraversalPolicy();
			if (ftp != null && ftp != this) {
				Component next =  ftp.getComponentAfter(childContainer, component);
				if (next != ftp.getFirstComponent(container)) 
					return next;
				
				// child cycle
				do {
					index = components.indexOf(childContainer);
					childContainer = childContainer.getParent();
				} while (index == -1 || childContainer == null);
				
				if (index == -1) {
					log.warn("I can't figure what is the next component");
					return getFirstComponent(container);
				}
			}
		}
		
		index++;
		
		if (index < components.size() && index >= 0) {
			Component c = getComponent(index);
			if (c.isEnabled() && c.isFocusable()) 
				return c;
			else 
				return getComponentAfter(container, c);
		}
		
		return getFirstComponent(container);
	}


	private Component getComponent(int index) {
		Component c =  components.get(index);
		Container cc = null;
		if (c instanceof Container) {
			cc  = (Container) c;
			if (cc.isFocusTraversalPolicyProvider() || cc.isFocusCycleRoot())
				c = cc.getFocusTraversalPolicy().getFirstComponent(cc);
			else if (cc instanceof JScrollPane) {
				if (((JScrollPane) cc).getViewport().getComponentCount() > 0)
				c = ((JScrollPane) cc).getViewport().getComponent(0);
			}
		}
		return c != null ? c : cc;
	}


	@Override
	public Component getComponentBefore(Container aContainer,
			Component aComponent) {
		
		int index = components.indexOf(aComponent);
		index--;
		
		if (index < components.size() && index >= 0) {
			Component c = getComponent(index);
			if (c.isEnabled()) 
				return c;
			else 
				return getComponentBefore(aContainer, c);
		}
		
		return getLastComponent(aContainer);
	}

	@Override
	public Component getDefaultComponent(Container aContainer) {
		if (components.size() > 0) {
			return getComponent(0);
		}
		
		return null;
	}

	@Override
	public Component getFirstComponent(Container aContainer) {
		return getDefaultComponent(aContainer);
	}

	@Override
	public Component getLastComponent(Container aContainer) {
		if (!components.isEmpty()) {
			Component c = components.get(components.size() - 1);
			if (c.isEnabled())
				return c;
			else
				return getComponentBefore(aContainer, c);
		}
		
		return null;
	}

	public void add(Component c) {
		components.add(c);
	}
	
	/**
	 * @param component
	 * @return
	 */
	@SuppressWarnings("unused")
	private FocusTraversalPolicy getFocusTraversalPolicyForComponent(Component component) {
		Container c = null;
		while ((c = component.getParent()) != null) {
			if (c.isFocusTraversalPolicyProvider()) 
				return c.getFocusTraversalPolicy();
				component = c;
		}
		
		return null;
	}
	
	 Container getTopmostProvider(Container focusCycleRoot, Component aComponent) {
	        Container aCont = aComponent.getParent();
	        Container ftp = null;
	        while (aCont  != focusCycleRoot && aCont != null) {
	            if (aCont.isFocusTraversalPolicyProvider()) {
	                ftp = aCont;
	            }
	            aCont = aCont.getParent();
	        }
	        if (aCont == null) {
	            return null;
	        }
	        return ftp;
	    }

}
