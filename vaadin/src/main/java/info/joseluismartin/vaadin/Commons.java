package info.joseluismartin.vaadin;

import info.joseluismartin.beans.AppCtx;
import info.joseluismartin.vaadin.ui.Box;

import org.springframework.context.ApplicationContext;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Commons extends Application {
	
	ApplicationContext context = AppCtx.getInstance();
	@Override
	public void init() {
		Window mainWindow = new Window("Application");
		Component pageableTable = (Component) context.getBean("userPageableTable");
		Component dataSourceTable = (Component) context.getBean("dataSourceTable");
		
		pageableTable.setWidth("100%");
		dataSourceTable.setWidth("100%");
		Panel panel = new Panel("Table with external paginator");
		panel.addComponent(pageableTable);
		Panel otherPanel = new Panel("Table with paginator in datasource");
		otherPanel.addComponent(dataSourceTable);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(panel);
		Box.addVerticalStruct(layout, 10);
		layout.addComponent(otherPanel);
		mainWindow.setContent(layout);
		setMainWindow(mainWindow);
	}
}
