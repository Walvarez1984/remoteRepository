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

@SuppressWarnings("serial")
public class EnvioOrdenCompraForm extends ActionForm{

	/**
	   * Valida el ingreso de datos en la p\u00E1gina <code>consolidacion.jsp</code>.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		El request que estamos procesando
	   * @return errors		Los errores recogidos durante la ejecuci\u00F3n
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	  {
		LogSISPE.getLog().info("Entrando a validate EnvioOrdenCompraForm");
	  	ActionErrors errors = new ActionErrors();
	  	PropertyValidator validator = new PropertyValidatorImpl();

	  	//se obtiene la variable global ayuda
	  	String ayuda = request.getParameter(Globals.AYUDA);
	  	LogSISPE.getLog().info("AYUDA: {}",ayuda);

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
}