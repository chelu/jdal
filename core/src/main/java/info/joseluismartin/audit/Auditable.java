package info.joseluismartin.audit;

import java.util.Date;

/**
 * Auditable Objects Interface
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public interface Auditable {
	
	/**
	 * @return the date
	 */
	Date getCreationDate();
	/**
	 * @param date the date to set
	 */
	void setCreationDate(Date date);
	/**
	 * @return the date
	 */
	Date getModificationDate();
	/**
	 * @param date the date to set
	 */
	void setModificationDate(Date date);
	/**
	 * @return the user
	 */
	String getCreationUser();
	/**
	 * @param user the user to set
	 */
	void setCreationUser(String user);
	/**
	 * @return the user
	 */
	String getModificationUser();
	/**
	 * @param user the user to set
	 */
	void setModificationUser(String user);
	
	/**
	 * clean all auditable data (set null all values)
	 */
	void nullAuditable ();
}
