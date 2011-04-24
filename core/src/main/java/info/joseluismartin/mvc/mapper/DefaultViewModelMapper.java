package info.joseluismartin.mvc.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Default ViewModelMapper implementation. 
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class DefaultViewModelMapper implements ViewModelMapper {

	/**
	 * View to model
	 * @param obj View object
	 * @return Model object
	 */
	public Object fromViewModel(Object obj) {
		return obj;
	}
	
	/**
	 * Model to view
	 * @param obj Model object
	 * @return View Object
	 */
	public Object toViewModel(Object obj) {
		return obj;
	}
	
	/**
	 * @param collection Collection to convert
	 * @return Collection converted
	 */
	@SuppressWarnings("unchecked")
	public Collection fromViewModelCollection(Collection collection) {
		ArrayList list = new ArrayList();
		if (collection != null) {
			Iterator<Object> iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(fromViewModel(iter.next()));
			}
 		}
		
		return list;
	}
	
	/**
	 * @param collection Collection to convert
	 * @return Collection converted
	 */
	@SuppressWarnings("unchecked")
	public Collection toViewModelCollection(Collection collection) {
		ArrayList list = new ArrayList();
		if (collection != null) {
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(toViewModel(iter.next()));
			}
 		}
		
		return list;
	}

}
