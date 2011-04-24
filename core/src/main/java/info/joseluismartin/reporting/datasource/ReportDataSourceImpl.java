/**
 * 
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