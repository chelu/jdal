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
package info.joseluismartin.vaadin.ui.table;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.Paginator;
import info.joseluismartin.dao.PaginatorListener;
import info.joseluismartin.vaadin.ui.AbstractView;
import info.joseluismartin.vaadin.ui.Box;

import java.io.Serializable;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
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
public class VaadinPaginator<T> extends AbstractView<Page<T>> implements Paginator, Serializable {
	
	private static final long serialVersionUID = 1L;
	/** Label to show in pagination status. */
	private Label status = new Label("-/-");
	private Label resultCount = new Label("       ");
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
	/** Listen buttons clicks */
	private ButtonClickListener buttonClickListener = new ButtonClickListener();
	
	/** 
	 * Creates a new paginator with default page size of 10 records
	 */
	public VaadinPaginator() {
		this (new Page<T>(10));
	}
	
	/**
	 * Creates a new paginator with current page
	 * @param page current page
	 */
	public VaadinPaginator(Page<T> page) {
		setModel(page);
		page.firstPage();
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
		hbox.setSpacing(true);
		resultCount.setSizeUndefined();
		hbox.addComponent(resultCount);
		
		Box.addHorizontalGlue(hbox);
		
		// buttons and status
		hbox.addComponent(first);
		hbox.addComponent(previous);
		hbox.addComponent(statusLayout);
//		hbox.setComponentAlignment(statusLayout, Alignment.MIDDLE_CENTER);
		hbox.addComponent(next);
		hbox.addComponent(last);
		Box.addHorizontalStruct(hbox, 10);
		
		// goto page select
		Label goToLabel = new Label("GoTo: ");
		goToLabel.setSizeUndefined();
		hbox.addComponent(goToLabel);
//		hbox.setComponentAlignment(goToLabel, Alignment.MIDDLE_CENTER);
		goTo.setWidth("5em");
		goTo.setImmediate(true);
		hbox.addComponent(goTo);
		Box.addHorizontalGlue(hbox);
	
		// records by page select
		Label showRecords = new Label("Page size: ");
		showRecords.setSizeUndefined();
		hbox.addComponent(showRecords);
//		hbox.setComponentAlignment(showRecords, Alignment.MIDDLE_RIGHT);
		
		for (String size : pageSizes) {
			pgs.addItem(size);
		}
		pgs.setNullSelectionAllowed(false);
		pgs.setValue(String.valueOf(getModel().getPageSize()));
		pgs.setWidth("6em");
		pgs.setImmediate(true);
		hbox.addComponent(pgs);
//		hbox.setComponentAlignment(pgs, Alignment.MIDDLE_RIGHT);
	
		pgs.addListener(new PgsValueChangeListener());
		goTo.addListener(new GoToValueChangeListener());
		
		hbox.setWidth("100%");
		
		return hbox;
	}

	// Paginator Interface Implementation //
	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		return getModel().hasNext();
	}

	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasPrevious() {
		return getModel().hasPrevious();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPage(int indexPage) {
		return getModel().hasPage(indexPage);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPage(int indexPage) {
		getModel().setPage(indexPage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPage() {
		return getModel().getPage();
	}


	/**
	 * {@inheritDoc}
	 */	
	public int getTotalPages() {
		return getModel().getTotalPages();
	}

	/**
	 * {@inheritDoc}
	 */	
	public void nextPage() {
		getModel().nextPage();
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public void previousPage() {
		getModel().previousPage();
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public void lastPage() {
		setPage(getTotalPages());
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public void firstPage() {
		setPage(1);
	}
	/**
	 * {@inheritDoc}
	 */	
	public int getStartIndex() {
		return getModel().getStartIndex();
	}


	/**
	 * {@inheritDoc}
	 */	
	public int getPageSize() {
		return getModel().getPageSize();
	}

	/**
	 * {@inheritDoc}
	 */	
	public void setPageSize(int pageSize) {
		if (pageSize > getModel().getCount())
			pageSize = getModel().getCount();
		
		getModel().setPageSize(pageSize);
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public void addPaginatorListener(PaginatorListener listener) {
		getModel().addPaginatorListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */	
	public void removePaginatorListener(PaginatorListener listener) {
		getModel().removePaginatorListener(listener);
	}
	
	/**
	 * Parse string with page number
	 * @param item
	 * @return
	 */
	private int parsePageSize(String item) {
		int pageSize = 20;
		if (item != null) {
			try {
				pageSize = Integer.parseInt(item.trim());
			} catch (NumberFormatException e) {
				pageSize = Integer.MAX_VALUE;
			}
		}
		return pageSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void refresh() {
		// update status
		int currentPage = getTotalPages() == 0 ? 0 : getPage();
		status.setValue(currentPage + "/" + getTotalPages());
		resultCount.setValue("Records: " + getModel().getCount());

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

		public void valueChange(ValueChangeEvent event) {
			int pageSize = parsePageSize((String) pgs.getValue());
			if (pageSize != getModel().getPageSize()) {
				setPageSize(pageSize);
			}
		}
	}
	
	class GoToValueChangeListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		public void valueChange(ValueChangeEvent event) {
			if (goTo.getValue() instanceof Integer) {
				if (!goTo.getValue().equals(getModel().getPage()))
					setPage((Integer) goTo.getValue());
			}
		}
	}
	
	class ButtonClickListener implements ClickListener {

		private static final long serialVersionUID = 1L;

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
	public void update() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return getModel().getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCount(int count) {
		getModel().setCount(count);
	}
}
