/*
 *  Clase CotizarReservarForm.java
 *  Creado el 27/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Contiene los campos y los demas controles del formulario de la Cotizacion,Recotizaci\u00F3n y Reservaci\u00F3n aqu\u00ED 
 * se realizan las validaciones de los datos ingresados por el usuario. Tambi\u00E9n se resetea el formulario cada vez que se realiza 
 * una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version 3.0
 * @since 	JSDK 1.5
 */

public class ConsolidarForm extends ActionForm{

	/*
	 * Controles que se muestran en la secci\u00F3n de Autorizaci\u00F3n:
	 * 
	 * String opAutorizacion: Opci\u00F3n que permite escoger el tipo de autorizaci\u00F3n que se va aplicar
	 * String numeroAutorizacion: Campo donde se ingresa el n\u00FAmero de Autorizaci\u00F3n
	 * String loginAutorizacion: Campo donde se ingresa el login del usuario
	 * String passwordAutorizacion: Campo donde se ingresa el password del usuario
	 * String observacionAutorizacion: Campo donde se ingresa la observaci\u00F3n por el uso de la autorizaci\u00F3n
	 */
	private String opAutorizacion;
	private String numeroAutorizacion;
	private String loginAutorizacion;
	private String passwordAutorizacion;
	private String observacionAutorizacion;
	private String tipoAutorizacion;  
	
	/**
	   * Valida el ingreso de datos en la p\u00E1gina <code>consolidacion.jsp</code>.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		El request que estamos procesando
	   * @return errors		Los errores recogidos durante la ejecuci\u00F3n
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	  {
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);  
	  	ActionErrors errors = new ActionErrors();
	  	PropertyValidator validator = new PropertyValidatorImpl();

	  	//se obtiene la variable global ayuda
	  	String ayuda = request.getParameter(Globals.AYUDA);
	  	LogSISPE.getLog().info("AYUDA: {}",ayuda);
	  	/*------------------------ cuando se da clic en el bot\u00F3n de Aplicar Autorizaci\u00F3n ------------------*/
		if(request.getParameter("aplicarAutorizacion")!=null)
		{
			LogSISPE.getLog().info("opAutorizacion: {}",this.opAutorizacion);
			if(this.opAutorizacion.equals(estadoInactivo))
				validator.validateMandatory(errors,"numeroAutorizacion",this.numeroAutorizacion,"errors.requerido","N\u00FAmero de autorizaci\u00F3n");
			else{
				validator.validateMandatory(errors,"loginAutorizacion",this.loginAutorizacion,"errors.requerido","Usuario");
				validator.validateMandatory(errors,"passwordAutorizacion",this.passwordAutorizacion,"errors.requerido","Contrase\u00F1a");
			}
		}

	  	return errors;
	  }

	  /**
	   * Resetea los controles del formulario de la p\u00E1gina <code>anulacionPedido.jsp</code>, en cada petici\u00F3n.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		El request que estamos procesando
	   */
	  public void reset(ActionMapping mapping, HttpServletRequest request) 
	  {
	  	//llamada al reset de la clase padre
	  	super.reset(mapping, request);
	  }

	public String getOpAutorizacion() {
		return opAutorizacion;
	}

	public void setOpAutorizacion(String opAutorizacion) {
		this.opAutorizacion = opAutorizacion;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public String getLoginAutorizacion() {
		return loginAutorizacion;
	}

	public void setLoginAutorizacion(String loginAutorizacion) {
		this.loginAutorizacion = loginAutorizacion;
	}

	public String getPasswordAutorizacion() {
		return passwordAutorizacion;
	}

	public void setPasswordAutorizacion(String passwordAutorizacion) {
		this.passwordAutorizacion = passwordAutorizacion;
	}

	public String getObservacionAutorizacion() {
		return observacionAutorizacion;
	}

	public void setObservacionAutorizacion(String observacionAutorizacion) {
		this.observacionAutorizacion = observacionAutorizacion;
	}

	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
}