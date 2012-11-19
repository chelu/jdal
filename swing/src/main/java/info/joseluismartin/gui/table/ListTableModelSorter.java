/*
 * Copyright 2009-2012 the original author or authors.
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
package info.joseluismartin.gui.table;

import info.joseluismartin.gui.ListTableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

/**
 * RowSorter that sort the model list of a ListTableModel.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.3
 */
@SuppressWarnings("rawtypes")
public class ListTableModelSorter extends RowSorter<ListTableModel> {
	
	private ListTableModel model;
	private SortKey key = new SortKey(0, javax.swing.SortOrder.ASCENDING);
	private MutableSortDefinition sortDefinition = new MutableSortDefinition();
 	private Comparator comparator = new PropertyComparator(sortDefinition);
 	
 	/**
	 * 
	 */
	public ListTableModelSorter() {
		super();
	}


	/**
	 * @param model
	 */
	public ListTableModelSorter(ListTableModel model) {
		super();
		this.model = model;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListTableModel getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggleSortOrder(int column) {
		if (key.getColumn() != column) {
			key = new SortKey(column, javax.swing.SortOrder.ASCENDING);
		}
		else {
			key = new SortKey(column, key.getSortOrder() == SortOrder.ASCENDING ?
					SortOrder.DESCENDING : SortOrder.ASCENDING);
		}
		sort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int convertRowIndexToModel(int index) {
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int convertRowIndexToView(int index) {
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSortKeys(List<? extends SortKey> keys) {
		if (!keys.isEmpty()) {
			this.key = keys.get(0);
			sort();
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void sort() {
		String propertyName = model.getPropertyName(key.getColumn());
		sortDefinition.setProperty(propertyName);
		sortDefinition.setAscending(SortOrder.ASCENDING.equals(key.getSortOrder()));
		Collections.sort(model.getList(), comparator);
		fireSortOrderChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends SortKey> getSortKeys() {
		List<SortKey> keys = new ArrayList<SortKey>(1);
		keys.add(key);
		
		return keys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getViewRowCount() {
		return model.getRowCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModelRowCount() {
		return model.getRowCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void modelStructureChanged() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allRowsChanged() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow, int column) {
		
	}

}
