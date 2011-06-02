package info.joseluismartin.vaadin.ui.table;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

/**
 * Add methods to vaadin Table for friendly configuration on spring bean defintion file.
 * Use <code>info.joseluismartin.ui.table.Column</code> as inner bean for configure columns:
 * 
 * <property name="columns">
 * 	<list>
 * 		<bean class="info.joseluismartin.ui.table.Column"/>
 * 			<property name="name" value="a_bean_property_name"/>
 * 			<property name="displayName" value="String_to_show_in_table_header"/>
 * 			<property name="width" value="column_width"/>
 * 			...
 * 		</bean>
 * 		<bean class="info.joseluismartin.vaadin.ui.table.Column">
 * 			...
 * 		</bean>
 * 	</list>
 * <property name="service" value="a_persistent_service"/>
 * </property>
 * 
 * ConfigurableTable use persistenService.getPage() to get List of models.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("serial")
public class ConfigurableTable extends Table {
	
	private boolean usingChecks = false;
	private List<Column> columns;
	private TableSorter sorter;
	
	public List<Column> getColumns() {
		return this.columns;
	}
	
	
	/**
	 * Configure Vaadin table with column definitions. 
	 * Method useful for use from context bean definition file.
	 * @param columns
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContainerDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource);
		configure();
	}
	/**
	 * Vaadin Table throw an exception when set this properties 
	 * with an empty Container datasource. 
	 */
	protected void configure() {
		if (columns == null)
			return;
		
		int size = columns.size();
		String[] visibleColumns = new String[size];
		String[] displayNames = new String[size];
		String[] alignments = new String[size];
		int[] widths = new int[size];
				
		for (int i = 0; i < size; i++) {
			visibleColumns[i] = columns.get(i).getName();
			displayNames[i] = columns.get(i).getDisplayName();
			alignments[i] = columns.get(i).getAlign();
			widths[i] = columns.get(i).getWidth();
		}
		
		this.setVisibleColumns(visibleColumns);
		this.setColumnHeaders(displayNames);
		this.setColumnAlignments(alignments);
		
		for (int i = 0; i < size; i++) {
			if ( widths[i] != -1)
				this.setColumnWidth(visibleColumns[i], widths[i]);
		}
	}
	
	
	
	/**
	 * Gets usingChecks property, if true, table show checkboxes for row selection
	 * @return the usingChecks
	 */
	public boolean isUsingChecks() {
		return usingChecks;
	}


	/**
	 * Sets usingChecks property, if true, table show checkboxes for row selection
	 * @param usingChecks the usingChecks to set
	 */
	public void setUsingChecks(boolean usingChecks) {
		this.usingChecks = usingChecks;
	}


	/**
	 * Sort on container or 
	 */
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (sorter != null)
			sorter.sort(propertyId, ascending);
		else {
			super.sort(propertyId, ascending);
		}
	}


	public TableSorter getSorter() {
		return sorter;
	}


	public void setSorter(TableSorter sorter) {
		this.sorter = sorter;
	}
}
