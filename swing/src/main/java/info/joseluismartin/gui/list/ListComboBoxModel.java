package info.joseluismartin.gui.list;

import java.util.ArrayList;
import java.util.List;

import javax.swing.MutableComboBoxModel;

import org.apache.commons.lang.ObjectUtils;


/**
 * A ComboBoxModel that use a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class ListComboBoxModel extends ListListModel implements MutableComboBoxModel {

	private static final long serialVersionUID = 1L;
	private Object selectedItem;
	private boolean allowNullSelection = true;
	
	public ListComboBoxModel() {
		this(new ArrayList());
	}
	
	public ListComboBoxModel(List list) {
		this(list, true);
	}

	public ListComboBoxModel(List list, boolean allowNullSelection) {
		super(list);
		if (list != null && list.size() > 0)
			setSelectedItem(list.get(0));
		this.allowNullSelection = allowNullSelection;
		
	}
	
	public void addElement(Object obj) {
		getList().add(obj);
		int index = getList().indexOf(obj);
		fireIntervalAdded(this, index, index);
		
	}

	public void insertElementAt(Object obj, int index) {
		getList().add(index, obj);
		fireIntervalAdded(this, index, index);
		
	}

	public void removeElement(Object obj) {
		int index = getList().indexOf(obj);
		if (getList().remove(obj)) {
			fireIntervalRemoved(this, index, index);
		}
		if (ObjectUtils.equals(selectedItem, obj)) {
			selectedItem = getList().size() > 0 ? getList().get(0) : null;
		}
	}

	public void removeElementAt(int index) {
		getList().remove(index);
		
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object item) {
		if (item == null) {
			selectNullItem();
			fireContentsChanged(this, -1, -1);
		}
		else if (!item.equals(selectedItem)) {
			int index = getList().indexOf(item);
			if (index != -1) 
				selectedItem = item;
			else
				selectNullItem();
			
				fireContentsChanged(this, index, index);
		}
	}

	private void selectNullItem() {
		if (allowNullSelection)
			selectedItem = null;
		else {
			if (getList().size() > 0)
				setSelectedItem(getList().get(0));
		}
	}
}
