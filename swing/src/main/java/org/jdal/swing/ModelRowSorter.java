/*
 * Copyright 2009-2016 the original author or authors.
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
package org.jdal.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;

/**
 * A RowSorter for server side sorting.
 * No mapping between view columns and model columns are needed.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.0
 * @see PageableTable
 */
public class ModelRowSorter<M extends TableModel> extends RowSorter<M> {

	private M model;
	private List<SortKey> sortKeys = new ArrayList<SortKey>();
	
	/**
	 * @param tableModel
	 */
	public ModelRowSorter(M tableModel) {
		this.model = tableModel;
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
	public M getModel() {
		return model;
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
	public List<?extends SortKey> getSortKeys() {
		return sortKeys;
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
	public void modelStructureChanged() {
	
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
	public void rowsInserted(int firstRow, int endRow) {
		
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggleSortOrder(int column) {
		
		if (!isSortable(column))
			return;
		
		List<SortKey> newKeys = new ArrayList<SortKey>(1);
		if (this.sortKeys.size()  > 0) {
			SortKey key = sortKeys.get(0);
			if (key.getColumn() == column) { // toggle order
				newKeys.add(0, toggle(key));
			}
			else {
				newKeys.add(0, new SortKey(column, SortOrder.ASCENDING));
			}
		}
		else {
			newKeys.add(0, new SortKey(column, SortOrder.ASCENDING));	
		}
		setSortKeys(newKeys);
	}

	/**
	 * Test if column is sortable
	 * @param column to check.
	 * @return true if sortable, false otherwise.
	 */
	protected boolean isSortable(int column) {
		if (getModel() instanceof ListTableModel) {
			ListTableModel ltm = (ListTableModel) getModel();
			return ltm.getColumns().get(ltm.columnToPropertyIndex(column)).isSortable();
		}
		
		return true;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(M model) {
		this.model = model;
	}

	/**
	 * @param sortKeys the sortKeys to set
	 */
	public void setSortKeys(List<? extends SortKey> sortKeys) {
		
		this.sortKeys.clear();
		this.sortKeys.addAll(sortKeys);
		fireSortOrderChanged();
	}

	/**
	 * Toggle order of a SortKey
	 * @param key the SortKey to toggle
	 * @return new SortKey with order toggled
	 */
	 private SortKey toggle(SortKey key) {
		 if (key.getSortOrder() == SortOrder.ASCENDING) {
			 return new SortKey(key.getColumn(), SortOrder.DESCENDING);
		 }
		 return new SortKey(key.getColumn(), SortOrder.ASCENDING);
	 }

}
