/*
 * Copyright 2009-2014 the original author or authors.
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
package serialization;

import javax.annotation.Resource;

import org.jdal.annotation.SerializableProxy;
import org.jdal.test.model.Author;
import org.jdal.test.model.Category;
import org.jdal.vaadin.ui.table.PageableTable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Jose Luis Martin
 */
@SerializableProxy(useCache=true)
public class Bean {
	
	@Autowired
	private PageableTable<Author> authorPageableTable;
	@Resource
	@SerializableProxy
	private PageableTable<Category> categoryPageableTable;
	
	@Resource(name="categoryPageableTable")
	@SerializableProxy
	private PageableTable<Category> categoryTable;

	public PageableTable<Author> getAuthorPageableTable() {
		return authorPageableTable;
	}

	public void setAuthorPageableTable(PageableTable<Author> authorPageableTable) {
		this.authorPageableTable = authorPageableTable;
	}

	public PageableTable<Category> getCategoryPageableTable() {
		return categoryPageableTable;
	}

	public void setCategoryPageableTable(
			PageableTable<Category> categoryPageableTable) {
		this.categoryPageableTable = categoryPageableTable;
	}

	public PageableTable<Category> getCategoryTable() {
		return categoryTable;
	}

	public void setCategoryTable(PageableTable<Category> categoryTable) {
		this.categoryTable = categoryTable;
	}
}
