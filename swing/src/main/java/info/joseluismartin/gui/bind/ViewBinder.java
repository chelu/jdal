package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.View;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@SuppressWarnings("unchecked")
public class ViewBinder extends AbstractBinder implements BinderHolder {


	public void doRefresh() {
		Object value = getValue();
		View<Object> view = getView();
		view.setModel(value);
		view.refresh();
	}

	private View<Object> getView() {
		View<Object> view = (View<Object>) component;
		return view;
	}
	
	public void doUpdate() {
		View<Object> view = getView();
		BindingResult br = view.getBindingResult();
		
		if (br != null) {
			br.setNestedPath(propertyName);
		}
			
		view.update();
		setValue(view.getModel());
		
		if (br != null && view.getBindingResult().hasErrors()) {
			for (ObjectError oe : view.getBindingResult().getAllErrors()) {
				getBindingResult().addError(oe);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertyBinder getBinder(String propertyName) {
		View<Object> view = getView();
		if (view instanceof BinderHolder)
			return ((BinderHolder) view).getBinder(propertyName);
		
		throw new  BindingException("View class: [" + view.getClass().getName() +"] " +
				"must implements BinderHolder to validate property: [" + propertyName + "]");
	
	}

}
