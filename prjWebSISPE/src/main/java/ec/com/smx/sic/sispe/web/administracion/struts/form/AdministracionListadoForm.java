/*
 * Clase AdministracionListadoForm.java
 * Creado el 12/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.Globals;

/**
 * <p>
 * Contiene controles del formulario para la Administraci\u00F3n del listado de para las pantallas de mantenimiento.
 * Tambi\u00E9n se resetea el formulario cada vez que se realiza una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class AdministracionListadoForm extends ActionForm
{
    /**
     * <ul>Campos del formulario de Par\u00E1metros Generales
     * <li><code>String botonNuevo</code>	Permite crear un nuevo registro para las tablas de mantenimiento.</li>
     * <li><code>String botonSalir</code> Permite regresar al men\u00FA principal.</li>
     * </ul>
     */
    private String botonNuevo;
    private String botonSalir;
    
    /**
     * Par\u00E1metros de paginaci\u00F3n
     * <ul>
     * <li><code>Collection datos</code> 	Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
     * <li><code>String start</code> 		Indice para el inicio del paginador</li>
     * <li><code>String range</code> 		Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada 
     * 									paginaci\u00F3n</li>
     * <li><code>String size</code> 		Valor que indica el n\u00FAmero total de registros.</li>
     * </ul>
     */
    private Collection datos;
    private String start;
    private String range;
    private String size;
    
    /**
     * Resetea los controles del formulario de la p\u00E1gina <code>administracionListado.jsp</code>, en cada petici\u00F3n.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        this.botonNuevo=null;
        this.botonSalir=null;
        this.datos=null;
        this.start=null;
        this.size=null;
        this.range=null;
    }
    
    /**
     * 
     */
    public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        String ayuda = request.getParameter(Globals.AYUDA);
        if(ayuda != null && !ayuda.equals("")) {
            if(ayuda.equals("registrarNuevo")) {
                this.botonNuevo = ayuda;
            }
        }
        
        return errors;
    }
    
    /**
     * @return Devuelve botonNuevo.
     */
    public String getBotonNuevo()
    {
        return botonNuevo;
    }
    /**
     * @param botonNuevo El botonNuevo a establecer.
     */
    public void setBotonNuevo(String botonNuevo)
    {
        this.botonNuevo = botonNuevo;
    }
    /**
     * @return Devuelve botonSalir.
     */
    public String getBotonSalir()
    {
        return botonSalir;
    }
    /**
     * @param botonSalir El botonSalir a establecer.
     */
    public void setBotonSalir(String botonSalir)
    {
        this.botonSalir = botonSalir;
    }
    
    /**
     * @return Devuelve datos.
     */
    public Collection getDatos()
    {
        return datos;
    }
    /**
     * @param datos El datos a establecer.
     */
    public void setDatos(Collection datos)
    {
        this.datos = datos;
    }
    /**
     * @return Devuelve start.
     */
    public String getStart()
    {
        return start;
    }
    /**
     * @param start El start a establecer.
     */
    public void setStart(String start)
    {
        this.start = start;
    }
    /**
     * @return Devuelve range.
     */
    public String getRange()
    {
        return range;
    }
    /**
     * @param range El range a establecer.
     */
    public void setRange(String range)
    {
        this.range = range;
    }
    /**
     * @return Devuelve size.
     */
    public String getSize()
    {
        return size;
    }
    /**
     * @param size El size a establecer.
     */
    public void setSize(String size)
    {
        this.size = size;
    }
    
}
