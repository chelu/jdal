package info.joseluismartin.vaadin.data;

import info.joseluismartin.dao.Page;
import info.joseluismartin.service.PersistentService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;

/**
 * <p>
 * An adapter to use PageableDataSources as Vaadin Container.
 * Use a integer as itemId and load data by page from data source on
 * request.
 * </p>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public class ContainerDataSource<T> implements Container, Sortable, Indexed, 
	ItemSetChangeNotifier {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ContainerDataSource.class);

	private Page<T> page = new Page<T>();
	
	private PersistentService<T, Serializable> service; 
	List<String> sortableProperties;
	private List<BeanItem<T> > items = new LinkedList<BeanItem<T>>();
	private Class<T> entityClass;
	private List<ItemSetChangeListener> listeners = new ArrayList<ItemSetChangeListener>();
	
	public ContainerDataSource() {}
	
	public ContainerDataSource(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public ContainerDataSource(PersistentService<T, Serializable> service) {
		this.service = service;
		loadPage();
	}

	public void init() {
		loadPage();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object nextItemId(Object itemId) {
		Integer index = (Integer) itemId;
		return index < page.getCount() - 1 ? index + 1 : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object prevItemId(Object itemId) {
		Integer index = (Integer) itemId;
		return index > 0 ? index - 1 :  null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object firstItemId() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object lastItemId() {
		return page.getCount() - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFirstId(Object itemId) {
		return ((Integer) itemId) == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLastId(Object itemId) {
		return ((Integer) itemId) == page.getCount() - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object addItemAfter(Object previousItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new Items after to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records after to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOfId(Object itemId) {
		return (Integer) itemId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getIdByIndex(int index) {
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item addItemAt(int index, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		// only use the first property :I
		page.setSortName(propertyId[0].toString());
		page.setOrder(ascending[0] ? Page.Order.ASC : Page.Order.DESC);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		if (sortableProperties != null)
			return sortableProperties;
		
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getItemPropertyIds();
		}
		
		return new LinkedList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(Object itemId) {
		int index = (Integer) itemId;
		
		if (!containsId(itemId))
			return null;
		
		if (!isInPage(index)) {
			page.setPage(index);
			loadPage();
		}
		return items.get(globalToPage(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> getContainerPropertyIds() {
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getItemPropertyIds();
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> getItemIds() {
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < page.getCount(); i++) {
			ids.add(i);
		}
		return Collections.unmodifiableCollection(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		Item item =  getItem(itemId);
		return item.getItemProperty(propertyId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getType(Object propertyId) {
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getBean().getClass();
		}
		
		return Object.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return page.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsId(Object itemId) {
		int index = (Integer) itemId;
		return index >= 0 && index < page.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object addItem() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeItem(Object itemId) {
		if (!containsId(itemId))
			return false;
		
		int index = (Integer) itemId;	
		
		if (isInPage(index)) {
			service.delete(page.getData().get(globalToPage(index)));
			loadPage();
		}
		else {
			Page<T> oneItem = new Page<T>(1, index);
			oneItem.setFilter(page.getFilter());
			service.getPage(oneItem);
			service.delete(oneItem.getData().get(0));
			page.setCount(page.getCount() - 1);
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new properties to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new properties to container");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		Page all = (Page) page.clone();
		all.setPageSize(Integer.MAX_VALUE);
		service.delete(all.getData());
	
		return true;
	}
	
	/**
	 * Convert global index to page index.
	 * @param index global index
	 * @return the index in current page
	 */
	private int globalToPage(int index) {
		return index - page.getStartIndex();
	}

	private void loadPage() {
		service.getPage(page);
		items.clear();
		for (T t : page.getData()) {
			items.add(new BeanItem<T>(t));
		}
		fireItemSetChange();
	}

	private boolean isInPage(int index) {
		return globalToPage(index) > 0 && globalToPage(index) < page.getPageSize();
	}
	
	public PersistentService<T, Serializable> getService() {
		return service;
	}

	public void setService(PersistentService<T, Serializable> service) {
		this.service = service;
	}
	

	public void setPage(Page<T> page) {
		this.page = page;
		loadPage();
	}

	public List<String> getSortableProperties() {
		return sortableProperties;
	}

	public void setSortableProperties(List<String> sortableProperties) {
		this.sortableProperties = sortableProperties;
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {
		if (listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireItemSetChange() {
		ItemSetChangeEvent isce = new ItemSetChangeEvent() {

			@Override
			public Container getContainer() {
				return ContainerDataSource.this;
			}
		};
		
		for (ItemSetChangeListener listener : listeners) {
			listener.containerItemSetChange(isce);
		}
	}

}
