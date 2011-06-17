package org.jdal.web.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Support class for Model Mappers
 * 
 * @author Jose Luis Martin
 */
public abstract class ModelMapperSupport implements ModelMapper {

	/**
	 * {@inheritDoc}
	 */
	public abstract Object fromModel(Object obj);
	public abstract Object toModel(Object obj);
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection fromModelCollection(Collection collection) {
		ArrayList list = new ArrayList();
		if (collection != null) {
			Iterator<Object> iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(fromModel(iter.next()));
			}
 		}
		
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection toodelCollection(Collection collection) {
		ArrayList list = new ArrayList();
		if (collection != null) {
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(toModel(iter.next()));
			}
 		}
		
		return list;
	}
}
