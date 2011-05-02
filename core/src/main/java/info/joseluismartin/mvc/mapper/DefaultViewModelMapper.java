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
package info.joseluismartin.mvc.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Default ViewModelMapper implementation. 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
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
