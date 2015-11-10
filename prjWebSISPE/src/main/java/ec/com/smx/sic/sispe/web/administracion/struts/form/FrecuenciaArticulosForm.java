/*
 * Creado el 09/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * Formulario para registrar las frecuencias de los art\u00EDculos
 * 
 * @author cbarahona
 *  
 */
@SuppressWarnings("serial")
public class FrecuenciaArticulosForm extends ActionForm {
	
	private int indiceFrecuencia;
	
	private String estadoFrecuencia;

	private String descripcionFrecuencia;

	private String codigoArticulo;

	public FrecuenciaArticulosForm() {
		indiceFrecuencia = -1;
	}

	/**
	 * @return Devuelve indiceFrecuencia.
	 */
	public int getIndiceFrecuencia() {
		return indiceFrecuencia;
	}

	/**
	 * @param indiceFrecuencia
	 *            El indiceFrecuencia a establecer.
	 */
	public void setIndiceFrecuencia(int indiceFrecuencia) {
		this.indiceFrecuencia = indiceFrecuencia;
	}

	
	
	/**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevaFrecuenciaArticulo.jsp</code> y 
     * <code>actualizarFrecuenciaArticulo.jsp</code>.
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
			if (ayuda!=null && ayuda.equals("guardarFrecuencia")) {
				validar.validateMandatory(errors, "estadoFrecuencia",this.estadoFrecuencia, "errors.requerido",	"Estado");
				validar.validateMandatory(errors, "descripcionFrecuencia",this.descripcionFrecuencia, "errors.requerido",	"Descripci\u00F3n");
			}
			else if(ayuda!=null && ayuda.equals("agregarArticulo")){
				validar.validateMandatory(errors, "codigoArticulo",this.codigoArticulo, "errors.requerido",	"C\u00F3digo de Art\u00EDculo");				
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
		this.estadoFrecuencia = null;
		this.descripcionFrecuencia = null;
		this.codigoArticulo = null;
	}

	/**
	 * @return Devuelve codigoArticulo.
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo
	 *            El codigoArticulo a establecer.
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return Devuelve descripcionFrecuencia.
	 */
	public String getDescripcionFrecuencia() {
		return descripcionFrecuencia;
	}

	/**
	 * @param descripcionFrecuencia
	 *            El descripcionFrecuencia a establecer.
	 */
	public void setDescripcionFrecuencia(String descripcionFrecuencia) {
		this.descripcionFrecuencia = descripcionFrecuencia;
	}

	/**
	 * @return Devuelve estadoFrecuencia.
	 */
	public String getEstadoFrecuencia() {
		return estadoFrecuencia;
	}

	/**
	 * @param estadoFrecuencia
	 *            El estadoFrecuencia a establecer.
	 */
	public void setEstadoFrecuencia(String estadoFrecuencia) {
		this.estadoFrecuencia = estadoFrecuencia;
	}
}
