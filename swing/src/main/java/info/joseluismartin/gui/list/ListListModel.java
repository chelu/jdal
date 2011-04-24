package info.joseluismartin.gui.list;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A ListModel that uses a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class ListListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List list;
	
	public ListListModel() {
		this(new ArrayList());
	}
	
	public ListListModel(List<?> list) {
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
}
