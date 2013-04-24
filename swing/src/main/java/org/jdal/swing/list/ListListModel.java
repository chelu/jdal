package org.jdal.swing.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A ListModel that uses a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List list;
	
	public ListListModel() {
		this(new ArrayList());
	}
	
	public ListListModel(List<?> list) {
		if (list != null)
			this.list = list;
	}
	
	public Object getElementAt(int index) {
		return list.get(index);
	}

	public int getSize() {
		return list.size();
	}

	/**
	 * @return the list
	 */
	public List getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List list) {
		this.list = list;
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * @param c collection with objects to remove
	 */
	public void removeAll(Collection c) {
		list.removeAll(c);
		fireContentsChanged(this, -1, -1);
		
	}

	/**
	 * @param c collection with objets to add
	 */
	public void addAll(Collection c) {
		list.addAll(c);
		fireContentsChanged(this, -1, -1);
	}

	public void clear() {
		list.clear();
		fireContentsChanged(this, -1, -1);
		
	}
	
	public void add(Object item) {
		list.add(item);
		fireContentsChanged(this, getSize() - 1, getSize());
	}
}
