/**
 * 
 */
package info.joseluismartin.gui.report;

import info.joseluismartin.dao.filter.ReportFilter;
import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.form.BoxFormBuilder;
import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.logic.PersistentManager;
import info.joseluismartin.reporting.ReportType;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.list.ComboBoxListModel;




/**
 * @author Jose A. Corbacho
 *
 */
public class ReportFilterView extends AbstractView<ReportFilter> {

	private final static int COMBO_WIDTH = 20;
	
	private JTextField reportName = new JTextField();
	private JComboBox reportType = FormUtils.newCombo(COMBO_WIDTH);
	
	private PersistentManager<ReportType, Long> reportTypeService;
	
	public ReportFilterView() {
		this(new ReportFilter());
	}
	
	public ReportFilterView(ReportFilter reportFilter) {
		setModel(reportFilter);
	}

	public void init(){
		bind(reportName, "name");
		bind(reportType, "type");
		refresh();
	}

	@Override
	public void doRefresh() {
		List<ReportType> reportTypeList = reportTypeService.getAll();
		reportTypeList.add(0, null);
		reportType.setModel(new ComboBoxListModel(reportTypeList));
	}

	
	@Override
	protected JComponent buildPanel() {
		// Build Form
		BoxFormBuilder b = new BoxFormBuilder();
		
		b.add("Nombre: ", reportName);
		b.add("Tipo: ", reportType);

		JComponent c = b.getForm();
		
		return c;
	}


	/**
	 * GETTERS AND SETTERS
	 */

	public PersistentManager<ReportType, Long> getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(PersistentManager<ReportType, Long> reportService) {
		this.reportTypeService = reportService;
	}
}
