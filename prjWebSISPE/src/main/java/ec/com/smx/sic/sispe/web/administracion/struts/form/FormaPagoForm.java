/*
 * Creado el 09/05/2007
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
 */
@SuppressWarnings("serial")
public class FormaPagoForm extends ActionForm {

	private String descripcionFormaPago;
	private String estadoFormaPago;

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			//  cuando se da click el boton Nuevo
			// Registro----------------------------------------------------------------
			if (ayuda!=null && ayuda.equals("guardarFormaPago")) {
				validar.validateMandatory(errors, "estadoFormaPago",this.estadoFormaPago, "errors.requerido",	"Estado");
				validar.validateMandatory(errors, "descripcionFormaPago",this.descripcionFormaPago, "errors.requerido",	"Descripci\u00F3n");
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
		this.descripcionFormaPago = null;
		this.estadoFormaPago = null;
	}
	
	/**
	 * @return Devuelve descripcionFormaPago.
	 */
	public String getDescripcionFormaPago() {
		return descripcionFormaPago;
	}

	/**
	 * @param descripcionFormaPago
	 *            El descripcionFormaPago a establecer.
	 */
	public void setDescripcionFormaPago(String descripcionFormaPago) {
		this.descripcionFormaPago = descripcionFormaPago;
	}

	/**
	 * @return Devuelve estadoFormaPago.
	 */
	public String getEstadoFormaPago() {
		return estadoFormaPago;
	}

	/**
	 * @param estadoFormaPago
	 *            El estadoFormaPago a establecer.
	 */
	public void setEstadoFormaPago(String estadoFormaPago) {
		this.estadoFormaPago = estadoFormaPago;
	}

}
