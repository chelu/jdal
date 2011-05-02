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
package info.joseluismartin.reporting.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose A. Corbacho
 * 
 */
@SuppressWarnings("unchecked")
public class ReportDataSourceImpl implements ReportDataSource {

	private List resultPage;
	private int pageStart = Integer.MAX_VALUE;
	private int pageEnd = Integer.MIN_VALUE;
	private static final int PAGE_SIZE = 50;

	public List getObjects(int firstResult, int maxResults) {

		List queryResults = null;
		if (resultPage == null) {
			resultPage = new ArrayList();
		}
		resultPage.clear();
		for (int i = 0; i < queryResults.size(); i++) {
			resultPage.add(queryResults.get(i));
		}
		pageStart = firstResult;
		pageEnd = firstResult + queryResults.size() - 1;
		return resultPage;
	}

	public final Object getObject(int index) {
		if ((resultPage == null) || (index < pageStart) || (index > pageEnd)) {
			resultPage = getObjects(index, PAGE_SIZE);
		}
		Object result = null;
		int pos = index - pageStart;
		if ((resultPage != null) && (resultPage.size() > pos)) {
			result = resultPage.get(pos);
		}
		return result;
	}
}