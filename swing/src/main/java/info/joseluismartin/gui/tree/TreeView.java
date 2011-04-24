package info.joseluismartin.gui.tree;

import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.form.FormUtils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.springframework.richclient.list.ComboBoxListModel;

/**
 * Tree View that show a List of TreeModelBuilders
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TreeView extends  AbstractView<TreeViewModel> implements ActionListener  {
	
	JComboBox combo = FormUtils.newCombo(25);
	JTree tree = new JTree();
	
	public void init() {
		combo.addActionListener(this);
	}
	@Override
	protected JComponent buildPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		p.add(BorderLayout.NORTH, combo);
		p.add(BorderLayout.CENTER, new JScrollPane(tree));
		
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRefresh() {
		TreeViewModel model = getModel();
		if (model != null) {
			combo.setModel(new ComboBoxListModel(model.getBuilders()));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		TreeModelBuilder b = (TreeModelBuilder) combo.getSelectedItem();
		if (b != null)
			tree.setModel(b.build());
	}

}
