package org.jdal.vaadin.ui;

import org.jdal.beans.MessageSourceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Base class for main views using a {@link TabSheet}
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public abstract class AbstractMainView extends TabSheet implements View {
	
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();

	public AbstractMainView() {
		setStyleName(Reindeer.TABSHEET_MINIMAL);
	}
	
	/**
	 * Create a new {@link VerticalLayout}
	 * @return a new VerticalLayout.
	 */
	protected VerticalLayout createVerticalLayout() {
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSizeFull();
		vl.setSpacing(true);
		
		return vl;
	}
	
	/**
	 * Add a component as Tab wrapped in a {@link VerticalLayout}
	 * @param component component to add
	 * @param caption message code for caption.
	 */
	public void addTabComponent(Component component, String caption) {
		VerticalLayout vl = createVerticalLayout();
		component.setSizeFull();
		vl.addComponent(component);
		addTab(vl, messageSource.getMessage(caption));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// don nothing by default
	}
	
	public String getMessage(String code) {
		return messageSource.getMessage(code);
	}
	
	public MessageSource getMessageSource() {
		return this.messageSource.getMessageSource();
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}

}
