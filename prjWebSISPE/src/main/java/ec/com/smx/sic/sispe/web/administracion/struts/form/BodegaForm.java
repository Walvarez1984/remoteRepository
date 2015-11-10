/*
 * Creado el 16/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.form.BaseForm;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * @author cbarahona
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
@SuppressWarnings("serial")
public class BodegaForm extends BaseForm {
	
	private String codigoBodega;
	private String codigoBodegaPadre;
	private String descripcionBodega;
	private String estadoBodega;
	private String usuarioCreacion;
	private String fechaCreacion;
	
	/**
	 * @return Devuelve codigoBodegaPadre.
	 */
	public String getCodigoBodegaPadre() {
		return codigoBodegaPadre;
	}
	/**
	 * @param codigoBodegaPadre El codigoBodegaPadre a establecer.
	 */
	public void setCodigoBodegaPadre(String codigoBodegaPadre) {
		this.codigoBodegaPadre = codigoBodegaPadre;
	}
	/**
	 * @return Devuelve descripcionBodega.
	 */
	public String getDescripcionBodega() {
		return descripcionBodega;
	}
	/**
	 * @param descripcionBodega El descripcionBodega a establecer.
	 */
	public void setDescripcionBodega(String descripcionBodega) {
		this.descripcionBodega = descripcionBodega;
	}
	/**
	 * @return Devuelve estadoBodega.
	 */
	public String getEstadoBodega() {
		return estadoBodega;
	}
	/**
	 * @param estadoBodega El estadoBodega a establecer.
	 */
	public void setEstadoBodega(String estadoBodega) {
		this.estadoBodega = estadoBodega;
	}
	
	/**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevaBdega.jsp</code> y 
     * <code>actualizarBodega.jsp</code>.
     * @param mapping		El mapeo utilizado para seleccionar esta instancia
     * @param request		El request que estamos procesando
     * @return errors		Los errores recogidos durante la ejecuci\u00F3n
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        
    	ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			//cuando el usuario quiere guardar los datos de nuevo o actualizar
			if (ayuda!=null && ayuda.equals("guardarBodega")) {				
				validar.validateMandatory(errors, "descripcionBodega",this.descripcionBodega, "errors.requerido",	"Descripci\u00F3n");
				validar.validateMandatory(errors, "estadoBodega",this.estadoBodega, "errors.requerido",	"Estado");
			}			

		} catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
    }

	/**
	 * Resetea los controles del formulario de la p\u00E1gina
	 * <code>estadoPedido.jsp</code>, en cada petici\u00F3n.
	 * 
	 * @param mapping
	 *            El mapeo utilizado para seleccionar esta instancia
	 * @param request
	 *            La petici\u00F3n que se est\u00E1 procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.codigoBodegaPadre = null;
		this.descripcionBodega = null;
		this.estadoBodega = null;
		this.usuarioCreacion = null;
		this.fechaCreacion = null;
		
	}
	/**
	 * @return Devuelve el c\u00F3digo de una bodega.
	 */
	public String getCodigoBodega() {
		return codigoBodega;
	}
	/**
	 * @param el c\u00F3digo de la bodega
	 */
	public void setCodigoBodega(String codBodega) {
		this.codigoBodega = codBodega;
	}
	/**
	 * @return el fechaCreacion
	 */
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	/**
	 * @param fechaCreacion el fechaCreacion a establecer
	 */
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	/**
	 * @return el usuarioCreacion
	 */
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	/**
	 * @param usuarioCreacion el usuarioCreacion a establecer
	 */
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	
}
