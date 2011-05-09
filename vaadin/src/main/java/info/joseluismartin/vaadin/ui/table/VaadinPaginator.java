package info.joseluismartin.vaadin.ui.table;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageChangedEvent;
import info.joseluismartin.dao.Paginator;
import info.joseluismartin.dao.PaginatorListener;
import info.joseluismartin.vaadin.ui.AbstractView;
import info.joseluismartin.vaadin.ui.Box;

import java.util.ArrayList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Paginator implementation for Vaadin framework
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VaadinPaginator<T> extends AbstractView<Page<T>> implements Paginator {
	
	private static final long serialVersionUID = 1L;
	/** Label to show pagenation status. */
	private Label status = new Label("-/-");
	/** String array with available page sizes */
	private String[] pageSizes;
	/** goto next page button */
	private Button next;
	/** goto previous page button */
	private Button previous;
	/** goto first page button */
	private Button first;
	/** goto last page button */
	private Button last;
	/** select with page sizes */
	private Select pgs = new Select();
	/** select with all pages for jump to page number */
	private Select goTo = new Select();
	/** paginator listener container */
	private ArrayList<PaginatorListener> listeners = new ArrayList<PaginatorListener>();
	/** Listen buttons clicks */
	private ButtonClickListener buttonClickListener = new ButtonClickListener();
	
	/** 
	 * Creates a new paginator with default page size of 20 records
	 */
	public VaadinPaginator() {
		this (new Page<T>(20));
	}
	
	/**
	 * Creates a new paginator with current page
	 * @param page current page
	 */
	public VaadinPaginator(Page<T> page) {
		setModel(page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component buildPanel() {
		
		// wrap status in a horizontal layout
		HorizontalLayout statusLayout = new HorizontalLayout();
		statusLayout.setSpacing(true);
		status.setSizeUndefined();
		statusLayout.setWidth("8em");
		statusLayout.addComponent(status);
		statusLayout.setComponentAlignment(status, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hbox = new HorizontalLayout();
		Box.addHorizontalGlue(hbox);
		
		// buttons and status
		hbox.addComponent(first);
		hbox.addComponent(previous);
		hbox.addComponent(statusLayout);
		hbox.setComponentAlignment(statusLayout, Alignment.MIDDLE_CENTER);
		hbox.addComponent(next);
		hbox.addComponent(last);
		Box.addHorizontalGlue(hbox);
		
		// goto page select
		Label goToLabel = new Label("Ir a: ");
		hbox.addComponent(goToLabel);
		hbox.setComponentAlignment(goToLabel, Alignment.MIDDLE_CENTER);
		goTo.setWidth("5em");
		goTo.setImmediate(true);
		hbox.addComponent(goTo);
		
		// records by page select
		Label showRecords = new Label("Mostrar:");
		hbox.addComponent(showRecords);
		hbox.setComponentAlignment(showRecords, Alignment.MIDDLE_RIGHT);
		
		for (String size : pageSizes) {
			pgs.addItem(size);
		}
		pgs.setNullSelectionAllowed(false);
		pgs.setValue(String.valueOf(getModel().getPageSize()));
		pgs.setWidth("6em");
		pgs.setImmediate(true);
		hbox.addComponent(pgs);
	
		pgs.addListener(new PgsValueChangeListener());
		goTo.addListener(new GoToValueChangeListener());
		
		return new CustomComponent(hbox);
	}

	// Paginator Interface Implementation //
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return getModel().getPage() < getTotalPages();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPrevious() {
		return getModel().getPage() > 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPage(int indexPage) {
		return indexPage <= getTotalPages() &&  indexPage > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPage(int indexPage) {
		if (hasPage(indexPage)) {
			getModel().setPage(indexPage);
			firePageChanged();
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPage() {
		return getModel().getPage();
	}


	/**
	 * {@inheritDoc}
	 */	
	@Override
	public int getTotalPages() {
		return getModel().getTotalPages();
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void nextPage() {
		getModel().nextPage();
		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void previousPage() {
		getModel().previousPage();
		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void lastPage() {
		setPage(getTotalPages());
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void firstPage() {
		setPage(1);
	}
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public int getStartIndex() {
		return getModel().getStartIndex();
	}


	/**
	 * {@inheritDoc}
	 */	
	@Override
	public int getPageSize() {
		return getModel().getPageSize();
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void setPageSize(int pageSize) {
		if (pageSize > getModel().getCount())
			pageSize = getModel().getCount();
		
		getModel().setPageSize(pageSize);
		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void addPaginatorListener(PaginatorListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void removePaginatorListener(PaginatorListener listener) {
		listeners.remove(listener);
		
	}
	
	/**
	 * Notify Listener that current page changed
	 */
	private void firePageChanged() {
		for (PaginatorListener listener : listeners) {
			Page<T> page = getModel();
			listener.pageChanged(new PageChangedEvent(getComponent(), page.getPage(),
					page.getStartIndex(), page.getTotalPages(), getPageSize()));
		}
		
		refresh();
	}

	/**
	 * Parse string with page number
	 * @param item
	 * @return
	 */
	private int parsePageSize(String item) {
		int pageSize = 20;
		if (item != null) {
			if ("Todas".equals(item.trim())) {
			pageSize = Integer.MAX_VALUE;
			}
			else {
				pageSize = Integer.parseInt(item.trim());
			}
		}
		return pageSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {
		// update status
		status.setValue(getPage() + "/" + getTotalPages());

		// fill goto page select
		goTo.removeAllItems();
		for (int i = 1; i <= getModel().getTotalPages(); i++) {
			goTo.addItem(i);
		}
		
		// Buttons
		next.setEnabled(hasNext());
		last.setEnabled(hasNext());
		previous.setEnabled(hasPrevious());
		first.setEnabled(hasPrevious());
		
		// select current page size
		// pgs.setValue(String.valueOf(getModel().getPageSize()));
	}
	
	// Getters and Setters 
	
	public String[] getPageSizes() {
		return pageSizes;
	}

	public void setPageSizes(String[] pageSizes) {
		this.pageSizes = pageSizes;
	}

	public Button getNext() {
		return next;
	}

	public void setNext(Button next) {
		this.next = next;
		next.addListener(buttonClickListener);
	}

	public Button getPrevious() {
		return previous;
	}

	public void setPrevious(Button previous) {
		this.previous = previous;
		previous.addListener(buttonClickListener);
	}

	public Button getFirst() {
		return first;
	}

	public void setFirst(Button first) {
		this.first = first;
		first.addListener(buttonClickListener);
	}

	public Button getLast() {
		return last;
	}

	public void setLast(Button last) {
		this.last = last;
		last.addListener(buttonClickListener);
	}
	
	// Listeners
	class PgsValueChangeListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			int pageSize = parsePageSize((String) pgs.getValue());
			if (pageSize != getModel().getPageSize()) {
				setPageSize(pageSize);
				firePageChanged();
			}
		}
	}
	
	class GoToValueChangeListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (goTo.getValue() instanceof Integer) {
				if (!goTo.getValue().equals(getModel().getPage()))
					setPage((Integer) goTo.getValue());
			}
		}
	}
	
	class ButtonClickListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getComponent() == next) {
				nextPage();
			}
			else if (event.getComponent() == last) {
				lastPage();
			}
			else if (event.getComponent() == previous) {
				previousPage();
			}
			else if (event.getComponent() == first) {
				firstPage();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCount(int count) {
		// TODO Auto-generated method stub
		
	}
}
