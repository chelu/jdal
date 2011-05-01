/**
 * 
 */
package info.joseluismartin.reporting.datasource;

import info.joseluismartin.dao.Page;
import info.joseluismartin.service.PersistentService;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Jose A. Corbacho
 *
 */
public class ReportSource extends JRAbstractBeanDataSource {
	
	private static final Log log = LogFactory.getLog(ReportSource.class);
	protected int index = 0;
    protected Object bean;
    private static Map fieldNameMap = new HashMap();
    private PersistentService<Object, Serializable> dataSource;
    
    private Object filter;
    private String sortPropertyName;
    private Page.Order sortOrder;

	private List<Object> resultPage;
	private int pageStart = Integer.MAX_VALUE;
	private int pageEnd = Integer.MIN_VALUE;

    
	public ReportSource(boolean isUseFieldDescription) {
		super(isUseFieldDescription);
	}

	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
	 */
	public void moveFirst() throws JRException {
		index = 0;
        bean = getObject(index);
	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() throws JRException {
		bean = getObject(index++);
        return (bean != null);
	}

	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	public Object getFieldValue(JRField jrField) throws JRException {
        String nameField = getFieldName(jrField.getName());
        try {
			return PropertyUtils.getProperty(bean, nameField);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	/**
     * Replace the character "_" by a ".".
     *
     * @param fieldName the name of the field
     * @return the value in the cache or make
     * the replacement and return this value
     */
    private String getFieldName(String fieldName) {
    	return fieldName;
//      String filteredFieldName
//             = (String) fieldNameMap.get(fieldName);
//      if (filteredFieldName == null) {
//            filteredFieldName = fieldName.replace('_','.');
//            fieldNameMap.put(fieldName,filteredFieldName);
//      }
//        return filteredFieldName;
    }

	public void setDataSource(PersistentService<Object, Serializable> dataSource) {
		this.dataSource = dataSource;
	}

	
	public void setFilter(Object filter){
		this.filter = filter;
	}
	


	@SuppressWarnings("unchecked")
	public List getObjects(int firstResult, int maxResults) {

		Page page = new Page(maxResults);
		page.setFilter(filter);
		page.setStartIndex(firstResult);
		page.setOrder(sortOrder);
		page.setSortName(sortPropertyName);

		Page aPage = dataSource.getPage(page);
		
		
		List<Object> queryResults = aPage.getData();
		
		if (resultPage == null) {
			resultPage = new ArrayList<Object>(queryResults.size());
		}
		resultPage.clear();
		
		for (Object t : aPage.getData()) {
			resultPage.add(t);
		}

		pageStart = firstResult;
		pageEnd = firstResult + queryResults.size() - 1;
		return resultPage;
	}

	public final Object getObject(int index) {
		if (log.isDebugEnabled())
			log.debug("ReportSource. getObject() index: " + index + "; pageStart: " + pageStart + " pageEnd: " + pageEnd + " resultPage: " + resultPage);
		
		if ((resultPage == null) || (index < pageStart) || (index > pageEnd)) {
			resultPage = getObjects(index, Integer.MAX_VALUE);
		}
		
		Object result = null;
		int pos = index - pageStart;
		if ((resultPage != null) && (resultPage.size() > pos)) {
			result = resultPage.get(pos);
		}
		return result;
	}

	public String getSortPropertyName() {
		return sortPropertyName;
	}

	public void setSortPropertyName(String sortPropertyName) {
		this.sortPropertyName = sortPropertyName;
	}

	public Page.Order getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Page.Order sortOrder) {
		this.sortOrder = sortOrder;
	}
}
