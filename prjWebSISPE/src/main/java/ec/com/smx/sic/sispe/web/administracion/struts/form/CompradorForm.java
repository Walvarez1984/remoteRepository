/*
 * Creado el 14/05/2007
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
 * @author mnaranjo
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
@SuppressWarnings("serial")
public class CompradorForm extends ActionForm 
{
	private String nombreComprador;
	private String estadoComprador;
	private String tipoComprador;
	private String areaReferencia;
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			//  cuando se da click el boton Nuevo
			// Registro----------------------------------------------------------------
			if (ayuda!=null && ayuda.equals("guardarComprador")) {
				validar.validateMandatory(errors, "nombreComprador",this.nombreComprador, "errors.requerido",	"Nombre");
				validar.validateMandatory(errors, "tipoComprador",this.tipoComprador, "errors.requerido",	"Tipo Comprador");
				validar.validateMandatory(errors, "estadoComprador",this.estadoComprador, "errors.requerido",	"Estado");
				validar.validateMandatory(errors, "areaReferencia",this.areaReferencia, "errors.requerido",	"\u00E1rea Referencia");
			}

		} catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
		this.nombreComprador = null;
		this.estadoComprador = null;
		this.tipoComprador = null;
		this.areaReferencia = null;
	}
	/**
	 * @return Devuelve estadoComprador.
	 */
	public String getEstadoComprador() {
		return estadoComprador;
	}
	/**
	 * @param estadoComprador El estadoComprador a establecer.
	 */
	public void setEstadoComprador(String estadoComprador) {
		this.estadoComprador = estadoComprador;
	}
	/**
	 * @return Devuelve nombreComprador.
	 */
	public String getNombreComprador() {
		return nombreComprador;
	}
	/**
	 * @param nombreComprador El nombreComprador a establecer.
	 */
	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}

	/**
	 * @return el tipoComprador
	 */
	public String getTipoComprador() {
		return tipoComprador;
	}

	/**
	 * @param tipoComprador el tipoComprador a establecer
	 */
	public void setTipoComprador(String tipoComprador) {
		this.tipoComprador = tipoComprador;
	}
	
	/**
	 * 
	 * @return el areaReferencia
	 */
	public String getAreaReferencia() {
		return areaReferencia;
	}
	
	/**
	 * 
	 * @param areaReferencia el areaReferencia a establecer
	 */
	public void setAreaReferencia(String areaReferencia) {
		this.areaReferencia = areaReferencia;
	}	
}
