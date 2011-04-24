package info.joseluismartin.mvc.mapper;

import java.util.Collection;

/**
 * View model to application model mapper for use with 
 * TableEditor.
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
@SuppressWarnings("unchecked")
public interface ViewModelMapper {
	/**
	 * Maps model to view model
	 * @param obj to convert to view model
	 * @return view model
	 */
	Object toViewModel(Object obj);
	/**
	 * maps view model to model
	 * @param obj voew model to convert to model
	 * @return model
	 */
	Object fromViewModel(Object obj);
	
	/** 
	 * Same from ViewModel,  but with collections
	 * @param collection the collection to convert
	 * @return converted collection
	 */
	Collection fromViewModelCollection(Collection collection);
	
	/**
	 * Same toViewModel, but with collections
	 * @param collection the collection to convert
	 * @return converted collection
	 */
	Collection toViewModelCollection(Collection collection);

}
