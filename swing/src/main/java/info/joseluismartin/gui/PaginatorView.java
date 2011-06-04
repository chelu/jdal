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
package info.joseluismartin.gui;

import info.joseluismartin.dao.PageChangedEvent;
import info.joseluismartin.dao.Paginator;
import info.joseluismartin.dao.PaginatorListener;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * PaginatorView with control buttons to manage paginator.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see info.joseluismartin.gui.PageableTable
 */
public class PaginatorView extends AbstractView<Paginator> implements PaginatorListener {
	
	private static final long serialVersionUID = 1L;

	/** paginator */
	private Paginator paginator;
	/** combo for change page sizes */
	private JComboBox pageSizeCombo;
	/** go to next page button */
	private JButton nextPageButton;
	/** go to previous page button */
	private JButton previousPageButton;
	/** go to last page button */
	private JButton lastPageButton;
	/** go to first page button */ 
	private JButton firstPageButton;
	/** String array with available page sizes */
	private String[] pageSizes;
	/** Label to show info about selected page */
	private JLabel statusLabel;
	private JLabel countLabel;
	
	// Icons 
	private Image nextIcon;
	private Image previousIcon;
	private Image lastIcon;
	private Image firstIcon;
	
	
	/** 
	 * Create a PaginatorView with default paginator
	 */
	public PaginatorView() {
		this(new DefaultPaginator());
	}
	
	/**
	 * Create a PaginatorView using this Paginator
	 * @param paginator the paginator to use as model
	 */
	public PaginatorView(Paginator paginator) {
		this.paginator = paginator;
		paginator.addPaginatorListener(this);
	}
	
	/** 
	 * Initialize paginator after property set. Normally called from context 
	 * with init-method.
	 */
	public void init() {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		pageSizeCombo = new JComboBox(pageSizes);
		pageSizeCombo.addItemListener(new PageSizeComboListener());
		nextPageButton = new JButton(new NextPageAction());
		previousPageButton = new JButton(new PreviousPageAction());
		lastPageButton = new JButton(new LastPageAction());
		firstPageButton = new JButton(new FirstPageAction());
		statusLabel = new JLabel();
		countLabel = new JLabel();
		JLabel numberPagesLabel =  new JLabel("Page Size: ");
		pageSizeCombo.setMaximumSize(new Dimension(70, 30));
		numberPagesLabel.setAlignmentX(Container.RIGHT_ALIGNMENT);
		
		Box box = Box.createHorizontalBox();
		box.setBackground(Color.LIGHT_GRAY);
		box.setOpaque(true);
		box.add(countLabel);
		box.add(Box.createHorizontalStrut( 100 	/*180 + numberPagesLabel.getSize().width */));
		box.add(Box.createHorizontalGlue());
		box.add(firstPageButton);
		box.add(previousPageButton);
		box.add(Box.createHorizontalStrut(5));
		box.add(statusLabel);
		box.add(Box.createHorizontalStrut(5));
		box.add(nextPageButton);
		box.add(lastPageButton);
		box.add(Box.createHorizontalGlue());
		box.add(numberPagesLabel);
		box.add(pageSizeCombo);
		box.add(Box.createHorizontalStrut(30));
		
		// set page size from combo box
		String pageSize = (String) pageSizeCombo.getSelectedItem();
		paginator.setPageSize(parsePageSize(pageSize));
		
		return box;
	}

	/**
	 * Handler for paginator changes.
	 * refresh the status label of paginator 
	 * @param event the PaginatorChangedEvent
	 */
	public void pageChanged(PageChangedEvent event) {
		refresh();
		
	}

	/**
	 * Refresh view with data of model
	 */
	public void doRefresh() {
		statusLabel.setText("" + paginator.getPage() + " / " + paginator.getTotalPages());
		countLabel.setText("Records: " + paginator.getCount());
        // disable buttons on fist and last page
		boolean hasNext = paginator.hasNext();
		boolean hasPrevious = paginator.hasPrevious();
		nextPageButton.setEnabled(hasNext);
		lastPageButton.setEnabled(hasNext);
		previousPageButton.setEnabled(hasPrevious);
		firstPageButton.setEnabled(hasPrevious);
	}
	
	/**
	 * 
	 */
	@Override
	public void doUpdate() {
		paginator.setPageSize(
				 parsePageSize((String) this.pageSizeCombo.getSelectedItem()));
	}
	
	/**
	 * @return the nextIcon
	 */
	public Image getNextIcon() {
		return nextIcon;
	}

	/**
	 * @param nextIcon the nextIcon to set
	 */
	public void setNextIcon(Image nextIcon) {
		this.nextIcon = nextIcon;
	}

	/**
	 * @return the previousIcon
	 */
	public Image getPreviousIcon() {
		return previousIcon;
	}

	/**
	 * @param previousIcon the previousIcon to set
	 */
	public void setPreviousIcon(Image previousIcon) {
		this.previousIcon = previousIcon;
	}

	/**
	 * @return the lastIcond
	 */
	public Image getLastIcon() {
		return lastIcon;
	}

	/**
	 * @param lastIcond the lastIcond to set
	 */
	public void setLastIcon(Image lastIcon) {
		this.lastIcon = lastIcon;
	}

	/**
	 * @return the firstIcon
	 */
	public Image getFirstIcon() {
		return firstIcon;
	}

	/**
	 * @param firstIcon the firstIcon to set
	 */
	public void setFirstIcon(Image firstIcon) {
		this.firstIcon = firstIcon;
	}

	/**
	 * @return the pageSizes
	 */
	public String[] getPageSizes() {
		return pageSizes;
	}

	/**
	 * @param pageSizes the pageSizes to set
	 */
	public void setPageSizes(String[] pageSizes) {
		this.pageSizes = pageSizes;
	}
	
	/**
	 * @return the paginator
	 */
	public Paginator getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(Paginator paginator) {
		if (paginator != null)
			paginator.removePaginatorListener(this);
		
		this.paginator = paginator;
		paginator.addPaginatorListener(this);
	}
	
	private int parsePageSize(String item) {
		int pageSize;
		try {
			pageSize = Integer.parseInt(item.trim());
		}
		catch(NumberFormatException nfe) {
			pageSize = Short.MAX_VALUE;
		}
		
		return pageSize;
	}

	// Action Classes
	class NextPageAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NextPageAction() {
			putValue(Action.SMALL_ICON, new ImageIcon(nextIcon));
		}
		
		public void actionPerformed(ActionEvent e) {
			paginator.nextPage();
		}
		
	}
	
	class PreviousPageAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public PreviousPageAction() {
			putValue(Action.SMALL_ICON, new ImageIcon(previousIcon));
		}
		
		public void actionPerformed(ActionEvent e) {
			paginator.previousPage();
		}
	}
	
	class LastPageAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public LastPageAction() {
			putValue(Action.SMALL_ICON, new ImageIcon(lastIcon));
		}
		
		public void actionPerformed(ActionEvent e) {
			paginator.lastPage();
		}
		
	}
	
	class FirstPageAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;

		public FirstPageAction() {
			putValue(Action.SMALL_ICON, new ImageIcon(firstIcon));
		}
		
		public void actionPerformed(ActionEvent e) {
			paginator.firstPage();
		}
	}
	
	
	class PageSizeComboListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {

			String item = (String) e.getItem();
			int pageSize = parsePageSize(item);
			paginator.setPageSize(pageSize);
		}
		
	}
}
