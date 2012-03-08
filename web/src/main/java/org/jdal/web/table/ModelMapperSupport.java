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
	public Collection<?> fromModel(Collection<?> collection) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (collection != null) {
			Iterator<?> iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(fromModel(iter.next()));
			}
 		}
		
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> toModel(Collection<?> collection) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (collection != null) {
			Iterator<?> iter = collection.iterator();
			while (iter.hasNext()) {
				list.add(toModel(iter.next()));
			}
 		}
		
		return list;
	}
}
