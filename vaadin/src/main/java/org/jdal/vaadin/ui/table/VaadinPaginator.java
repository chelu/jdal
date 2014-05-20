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
package org.jdal.vaadin.ui.table;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.jdal.beans.MessageSourceWrapper;
import org.jdal.dao.Page;
import org.jdal.dao.Paginator;
import org.jdal.dao.PaginatorListener;
import org.jdal.vaadin.ui.AbstractView;
import org.jdal.vaadin.ui.form.BoxFormBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Paginator implementation for Vaadin framework
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@Configurable
public class VaadinPaginator<T> extends AbstractView<Page<T>> implements Paginator, Serializable {
	
	public static final String PAGINATOR = "paginator";
	
	private static final long serialVersionUID = 1L;
	/** Label to show in pagination status. */
	private Label status = new Label("- / -");
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
	private ComboBox pgs = new ComboBox();
	/** select with all pages for jump to page number */
	private ComboBox goTo = new ComboBox();
	/** Listen buttons clicks */
	private ButtonClickListener buttonClickListener = new ButtonClickListener();
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();
	
	private String nextIconUrl = "images/table/go-next.png";
	private String previousIconUrl = "images/table/go-previous.png";
	private String lastIconUrl = "images/table/go-last.png";
	private String firstIconUrl = "images/table/go-first.png";
	
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
	
	@PostConstruct
	public void init() {
		if (next == null) {
			setNext(createButton(nextIconUrl));
		}
		
		if (this.last == null) {
			setLast(createButton(lastIconUrl));
		}
		
		if (this.previous == null) {
			setPrevious(createButton(previousIconUrl));
		}
		
		if (this.first == null) {
			setFirst(createButton(firstIconUrl));
		}
		
	}

	private Button createButton(String icon) {
		Button b = new Button();
		b.setIcon(new ThemeResource(icon));

		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component buildPanel() {		
		// goto page select
		Label goToLabel = new Label(messageSource.getMessage("vaadinPaginator.goto"));
		goToLabel.setSizeUndefined();
		goToLabel.setStyleName(PAGINATOR);
		goTo.addValueChangeListener(new GoToValueChangeListener());
		goTo.setImmediate(true);
		// records by page select
		Label showRecords = new Label(messageSource.getMessage("vaadinPaginator.pageSize"));
		showRecords.setSizeUndefined();
		// page size combo
		for (String size : pageSizes) {
			pgs.addItem(size);
		}
		
		pgs.setNullSelectionAllowed(false);
		pgs.setValue(String.valueOf(getModel().getPageSize()));
		pgs.setWidth("6em");
		pgs.setImmediate(true);
		
		pgs.addValueChangeListener(new PgsValueChangeListener());
		

		BoxFormBuilder fb = new BoxFormBuilder();
		fb.setMargin(false);
		fb.setFixedHeight();
		fb.row();
		fb.add(resultCount);
		fb.addHorizontalGlue();
		fb.add(first);
		fb.add(previous);
		fb.add(status);
		fb.add(next);
		fb.add(last);
		fb.add(goToLabel);
		fb.add(goTo, 60);
		fb.addHorizontalGlue();
		fb.add(showRecords);
		fb.add(pgs, 60);
		
		return fb.getForm();
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
	public void doRefresh() {
		// update status
		int currentPage = getTotalPages() == 0 ? 0 : getPage();
		status.setValue(currentPage + " / " + getTotalPages());
		resultCount.setValue(messageSource.getMessage("vaadinPaginator.records") 
				+ getModel().getCount());

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
		next.addClickListener(buttonClickListener);
	}

	public Button getPrevious() {
		return previous;
	}

	public void setPrevious(Button previous) {
		this.previous = previous;
		previous.addClickListener(buttonClickListener);
	}

	public Button getFirst() {
		return first;
	}

	public void setFirst(Button first) {
		this.first = first;
		first.addClickListener(buttonClickListener);
	}

	public Button getLast() {
		return last;
	}

	public void setLast(Button last) {
		this.last = last;
		last.addClickListener(buttonClickListener);
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
	
	/**
	 * Handle clicks on paginator buttons
	 * 
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
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
	public int getCount() {
		return getModel().getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCount(int count) {
		getModel().setCount(count);
	}

	/**
	 * @return the nextIconUrl
	 */
	public String getNextIconUrl() {
		return nextIconUrl;
	}

	/**
	 * @param nextIconUrl the nextIconUrl to set
	 */
	public void setNextIconUrl(String nextIconUrl) {
		this.nextIconUrl = nextIconUrl;
	}

	/**
	 * @return the previousIconUrl
	 */
	public String getPreviousIconUrl() {
		return previousIconUrl;
	}

	/**
	 * @param previousIconUrl the previousIconUrl to set
	 */
	public void setPreviousIconUrl(String previousIconUrl) {
		this.previousIconUrl = previousIconUrl;
	}

	/**
	 * @return the lastIconUrl
	 */
	public String getLastIconUrl() {
		return lastIconUrl;
	}

	/**
	 * @param lastIconUrl the lastIconUrl to set
	 */
	public void setLastIconUrl(String lastIconUrl) {
		this.lastIconUrl = lastIconUrl;
	}

	/**
	 * @return the firstIconUrl
	 */
	public String getFirstIconUrl() {
		return firstIconUrl;
	}

	/**
	 * @param firstIconUrl the firstIconUrl to set
	 */
	public void setFirstIconUrl(String firstIconUrl) {
		this.firstIconUrl = firstIconUrl;
	}
	
	public MessageSource getMessageSource() {
		return messageSource.getMessageSource();
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}
}
