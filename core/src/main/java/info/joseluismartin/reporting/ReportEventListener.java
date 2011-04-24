/**
 * 
 */
package info.joseluismartin.reporting;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interface to be implemented by those objects that need to know about the changes in a report
 * @author Jose A. Corbacho
 *
 */
public interface ReportEventListener extends EventListener {

	/**
	 * Notify changes in a report
	 * @param event
	 */
	public void reportChanged(EventObject event);
}
