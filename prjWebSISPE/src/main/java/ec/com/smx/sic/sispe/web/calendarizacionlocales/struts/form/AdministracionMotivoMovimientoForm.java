/*
 * Creado el 09/05/2007
 * 
 */

package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.form.BaseForm;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Esta Form pasa los valores del formulario de la jsp  
 * </p>
 * @author 	mgudino
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("serial")
public class AdministracionMotivoMovimientoForm extends BaseForm {
	private String btnAceptar;
	private String btnCancelar;
	
	
	public void reset(ActionMapping arg0, ServletRequest arg1) {
		this.btnAceptar = null;
		this.btnCancelar = null;
		LogSISPE.getLog().info("entro reset");
	}
	
	/**
	 * @return Devuelve btnAceptar.
	 */
	public String getBtnAceptar() {
		return btnAceptar;
	}
	/**
	 * @param btnAceptar El btnAceptar a establecer.
	 */
	public void setBtnAceptar(String btnAceptar) {
		this.btnAceptar = btnAceptar;
	}
	/**
	 * @return Devuelve btnCancelar.
	 */
	public String getBtnCancelar() {
		return btnCancelar;
	}
	/**
	 * @param btnCancelar El btnCancelar a establecer.
	 */
	public void setBtnCancelar(String btnCancelar) {
		this.btnCancelar = btnCancelar;
	}
}
