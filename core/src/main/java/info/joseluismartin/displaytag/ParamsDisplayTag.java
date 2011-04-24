package info.joseluismartin.displaytag;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Clase que contiene datos sobre la ordenación y paginación de un listado
 * realizado con DisplayTag
 * 
 * @author Charlie Alejo - (calejo@matchmind.es)
 * @version 1.0
 */
public class ParamsDisplayTag implements Serializable {
    
    /** serialVersionUID*/
    final private static  long serialVersionUID = 1L;

    /**
     * Número de página solicitada
     */
    private int page = 0;

    /**
     * Nombre de la columna ordenada
     */
    private String sort = "";

    /**
     * Orden de ordenación
     */
    private String order = "";
    
    /**
     * Número total de resultados de la lista
     */
    private int totalResultados = 0;
    
    /**
     * Indica si se ha realizado una nueva búsqueda
     */
    private boolean nuevaLista = true;
    
    /** export to excel */
    private boolean excel = false;
    
    
    /**
     * Constructor vacío
     */
    public ParamsDisplayTag() {
        super();
    }
    
    
    /**
     * Constructor con petición al servidor para establecer los parámetros
     * @param request Petición
     */
    public ParamsDisplayTag( HttpServletRequest request) {
        super();
        setParams(request);
    }
    
	/**
     * @return the nuevaLista
     */
    public boolean isNuevaLista() {
        return nuevaLista;
    }

    /**
     * @param nuevaLista the nuevaLista to set
     */
    public void setNuevaLista( boolean nuevaLista) {
        this.nuevaLista = nuevaLista;
    }

    /**
     * @return the totalResultados
     */
    public int getTotalResultados() {
        return totalResultados;
    }

    /**
     * @param totalResultados the totalResultados to set
     */
    public void setTotalResultados( int totalResultados) {
        this.totalResultados = totalResultados;
    }

    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder( String order) {
        this.order = order;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage( int page) {
        this.page = page;
    }

    /**
     * @return the sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort( String sort) {
        this.sort = sort;
    }

    /**
     * Establece los parámetros de ordenación y paginación a partir de una
     * petición al servidor. Los parámetros vendrán a través de la URL en la
     * forma "^d-\d+-[pos]$".
     * 
     * @param request Petición al servidor
     */
    
    @SuppressWarnings("unchecked")
	public void setParams( HttpServletRequest request) {
         Map parametros = request.getParameterMap();
         Iterator iter = parametros.entrySet().iterator();
        while (iter.hasNext()) {
             Map.Entry parametro = (Map.Entry) iter.next();
            if (((String) parametro.getKey()).matches("d-\\d+-p")) {
                this.page =
                    Integer.valueOf(((String[]) parametro.getValue())[0]);
                this.nuevaLista = false;
            } else if (((String) parametro.getKey()).matches("d-\\d+-o")) {
                 int orden =
                    Integer.valueOf(((String[]) parametro.getValue())[0]);
                if (orden == 1) {
                    this.order = "desc";
                } else {
                    this.order = "asc";
                }
                this.nuevaLista = false;
            } else if (((String) parametro.getKey()).matches("d-\\d+-s")) {
                this.sort = ((String[]) parametro.getValue())[0];
                this.nuevaLista = false;
            } else if (((String) parametro.getKey()).matches("d-\\d+-e")) {
            	this.excel = true;
            }
        }
    }


	/**
	 * @return the excel
	 */
	public boolean isExcel() {
		return excel;
	}


	/**
	 * @param excel the excel to set
	 */
	public void setExcel(boolean excel) {
		this.excel = excel;
	}
}
