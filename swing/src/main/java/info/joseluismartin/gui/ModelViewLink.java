package info.joseluismartin.gui;

/**
 * Link Model <-> View in MVC or M-VC 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * 
 * @param <M> Model class
 * @param <V> View class
 */
public interface ModelViewLink<M, V> {

	public void updateModel();
	public void updateView();
	public void setModel(M model);
	public void setView(V view);
	public M getModel();
	public V getView();
}
