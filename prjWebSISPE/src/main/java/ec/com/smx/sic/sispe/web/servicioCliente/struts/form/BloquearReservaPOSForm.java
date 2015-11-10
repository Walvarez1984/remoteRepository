/*
 * Clase AnulacionesForm.java Creado el 13/06/2006
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 *
 */
@SuppressWarnings("serial")
public class BloquearReservaPOSForm extends ListadoPedidosForm {
	//campos para la pregunta de confirmaci\u00F3n para anular un pedido
	private String botonSIDesbloquear;
	private String botonNODesbloquear;
	
	private String numeroConfirmacion;

	/**
	 * Valida el ingreso de datos en la p\u00E1gina <code>anulacionPedido.jsp</code>.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		El request que estamos procesando
	 * @return errors		Los errores recogidos durante la ejecuci\u00F3n
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
	
		ActionErrors errors = new ActionErrors();
		PropertyValidator validator = new PropertyValidatorImpl();
	
		try{
			errors = super.validate(mapping, request);
			LogSISPE.getLog().info("errores: {}",errors.size());
			
			if(request.getParameter("desbloquearReserva")!=null){
				validator.validateMandatory(errors,"numeroConfirmacion",this.numeroConfirmacion,"errors.requerido","N&uacute;mero de Confirmaci&oacute;n");
			}
			
			//solo no hay errores
			if(errors.isEmpty()){
				//Para registrar una anulaci\u00F3n--------------------------------------------------------------------------------
				if (request.getParameter("aplicarAutorizacion")!= null){
	
					//si se detecto un error
					if(!errors.isEmpty()){
						request.setAttribute("ec.com.smx.sic.sispe.anular","ok");
					}
				}
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>anulacionPedido.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		El request que estamos procesando
	*/
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		//llamada al reset de la clase padre
		super.reset(mapping, request);
		this.botonSIDesbloquear=null;
		this.botonNODesbloquear=null;
		this.numeroConfirmacion=null;
	}
	
	/**
	 * @return el numeroConfirmacion
	 */
	public String getNumeroConfirmacion() {
		return numeroConfirmacion;
	}

	/**
	 * @param numeroConfirmacion el numeroConfirmacion a establecer
	 */
	public void setNumeroConfirmacion(String numeroConfirmacion) {
		this.numeroConfirmacion = numeroConfirmacion;
	}

	/**
	 * @return el botonSIDesbloquear
	 */
	public String getBotonSIDesbloquear() {
		return botonSIDesbloquear;
	}

	/**
	 * @param botonSIDesbloquear el botonSIDesbloquear a establecer
	 */
	public void setBotonSIDesbloquear(String botonSIDesbloquear) {
		this.botonSIDesbloquear = botonSIDesbloquear;
	}

	/**
	 * @return el botonNODesbloquear
	 */
	public String getBotonNODesbloquear() {
		return botonNODesbloquear;
	}

	/**
	 * @param botonNODesbloquear el botonNODesbloquear a establecer
	 */
	public void setBotonNODesbloquear(String botonNODesbloquear) {
		this.botonNODesbloquear = botonNODesbloquear;
	}
	

}
