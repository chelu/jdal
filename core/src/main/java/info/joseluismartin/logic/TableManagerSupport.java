package info.joseluismartin.logic;

import info.joseluismartin.dao.UserPreferenceDao;
import info.joseluismartin.model.TableState;
import info.joseluismartin.model.User;
import info.joseluismartin.model.UserPreference;
import info.joseluismartin.service.TableService;

import org.springframework.util.StringUtils;

/**
 * TableService implementation that use UserPreference Dao to load/save 
 * the TableState
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.2.1
 *
 */
public abstract class TableManagerSupport implements TableService {

	private static final String VISIBLE_COLUMNS = "visible_columns";
	private static final String PAGE_SIZE = "page_size";
	private UserPreferenceDao userPreferenceDao;
	private String separator = ".";
	
	/**
	 * {@inheritDoc}
	 */
	public TableState getState(String name) {
		if (getUser() == null)
			return null;
		
		TableState state = null;
		String visibleColumns = userPreferenceDao.findUserPreferenceValue(getUser(),
				getPreferenceName(name, VISIBLE_COLUMNS));
		String pageSize  = userPreferenceDao.findUserPreferenceValue(getUser(),
				getPreferenceName(name, PAGE_SIZE));
		
		if (visibleColumns != null || pageSize != null) {
			state = new TableState();
			state.setName(name);
			state.setVisibleColumns(visibleColumns);
			if (org.apache.commons.lang.StringUtils.isNumeric(pageSize))
				state.setPageSize(Integer.parseInt(pageSize));
		}
		
		return state;
	}

	/** 
	 * @return authenticated user of null if no authenticated.
	 */
	protected abstract User getUser();
	
	/**
	 * @param name
	 * @param string
	 * @return
	 */
	private String getPreferenceName(String name, String preferenceName) {
		return name + separator + preferenceName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveState(TableState state) {
		savePreference(getPreferenceName(state.getName(), VISIBLE_COLUMNS), 
				StringUtils.collectionToCommaDelimitedString(state.getVisibleColumns()));
		
		savePreference(getPreferenceName(state.getName(), PAGE_SIZE), String.valueOf(state.getPageSize()));
	}
	
	private void savePreference(String name, String value) {
		UserPreference p = userPreferenceDao.findUserPreference(getUser(), name);
		
		if (p == null) {
			p = userPreferenceDao.createUserPreference();
			p.setUser(getUser());
			p.setName(name);
		}
		
		p.setValue(value);
		userPreferenceDao.save(p);
	}

	/**
	 * @return the userPreferenceDao
	 */
	public UserPreferenceDao getUserPreferenceDao() {
		return userPreferenceDao;
	}

	/**
	 * @param userPreferenceDao the userPreferenceDao to set
	 */
	public void setUserPreferenceDao(UserPreferenceDao userPreferenceDao) {
		this.userPreferenceDao = userPreferenceDao;
	}

}
