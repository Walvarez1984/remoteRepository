/*
 * Clase AnulacionesForm.java Creado el 13/06/2006
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Permite el gestionar los datos de Anulaciones dentro de la aplicaci\u00F3n.
 * 
 * @author 	dlopez
 * @author 	fmunoz
 * @version 3.0
 */
@SuppressWarnings("serial")
public class AnulacionesForm extends ListadoPedidosForm {
  //campos para la pregunta de confirmaci\u00F3n para anular un pedido
  private String botonSIAnular;
  private String botonNOAnular;
  
  //campos para el ingreso de autorizaci\u00F3n
  private String opAutorizacion;
  private String numeroAutorizacion;
  private String loginAutorizacion;
  private String passwordAutorizacion;
  private String botonAplicarAutorizacion;
  private String observacionAutorizacion;
  private String opEstadoActivo;


  /**
   * Valida el ingreso de datos en la p\u00E1gina <code>anulacionPedido.jsp</code>.
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		El request que estamos procesando
   * @return errors		Los errores recogidos durante la ejecuci\u00F3n
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
  {

  	ActionErrors errors = new ActionErrors();
  	PropertyValidator validar = new PropertyValidatorImpl();

  	//se obtiene la variable global ayuda
  	String ayuda = request.getParameter(Globals.AYUDA);
  	LogSISPE.getLog().info("AYUDA: {}",ayuda);
  	if(ayuda!=null && !ayuda.equals("")){
  		if(ayuda.equals("siAnular"))
  			this.botonSIAnular=ayuda;
  	}

  	try
  	{
  		errors = super.validate(mapping, request);
  		LogSISPE.getLog().info("errores: {}",errors.size());
  		
  		//solo no hay errores
  		if(errors.isEmpty()){
  			//Para registrar una anulaci\u00F3n--------------------------------------------------------------------------------
  			if (request.getParameter("aplicarAutorizacion")!= null){
  				LogSISPE.getLog().info("VALIDANDO NUMERO DE AUTORIZACION");
  				if(this.opAutorizacion.equals(SessionManagerSISPE.getEstadoInactivo(request)))
  					validar.validateMandatory(errors,"numeroAutorizacion",this.numeroAutorizacion,"errors.requerido","N\u00FAmero de autorizaci\u00F3n");
  				else{
  					validar.validateMandatory(errors,"loginAutorizacion",this.loginAutorizacion,"errors.requerido","Usuario");
  					validar.validateMandatory(errors,"passwordAutorizacion",this.passwordAutorizacion,"errors.requerido","Contrase\u00F1a");
  				}

  				//si se detecto un error
  				if(!errors.isEmpty())
  					request.setAttribute("ec.com.smx.sic.sispe.anular","ok");
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
  public void reset(ActionMapping mapping, HttpServletRequest request) 
  {
  	//llamada al reset de la clase padre
  	super.reset(mapping, request);

    this.numeroAutorizacion=null;
    this.opAutorizacion= "0";
    this.loginAutorizacion = null;
    this.passwordAutorizacion = null;
    this.botonAplicarAutorizacion = null;
    this.observacionAutorizacion = null;
    
    this.botonNOAnular=null;
    this.botonSIAnular=null;
    this.opEstadoActivo= null;
  }

  /**
   * @return Devuelve botonNOAnular.
   */
  public String getBotonNOAnular()
  {
    return botonNOAnular;
  }
  /**
   * @param botonNOAnular El botonNOAnular a establecer.
   */
  public void setBotonNOAnular(String botonNOAnular)
  {
    this.botonNOAnular = botonNOAnular;
  }
  /**
   * @return Devuelve botonSIAnular.
   */
  public String getBotonSIAnular()
  {
    return botonSIAnular;
  }
  /**
   * @param botonSIAnular El botonSIAnular a establecer.
   */
  public void setBotonSIAnular(String botonSIAnular)
  {
    this.botonSIAnular = botonSIAnular;
  }
  /**
   * @return Devuelve opAutorizacion.
   */
  public String getOpAutorizacion()
  {
    return opAutorizacion;
  }
  /**
   * @param opAutorizacion El opAutorizacion a establecer.
   */
  public void setOpAutorizacion(String opAutorizacion)
  {
    this.opAutorizacion = opAutorizacion;
  }
  
  /**
   * @return Devuelve numeroAutorizacion.
   */
  public String getNumeroAutorizacion()
  {
    return numeroAutorizacion;
  }
  /**
   * @param numeroAutorizacion El numeroAutorizacion a establecer.
   */
  public void setNumeroAutorizacion(String numeroAutorizacion)
  {
    this.numeroAutorizacion = numeroAutorizacion;
  }
  /**
   * @return Devuelve loginAutorizacion.
   */
  public String getLoginAutorizacion()
  {
    return loginAutorizacion;
  }
  /**
   * @param loginAutorizacion El loginAutorizacion a establecer.
   */
  public void setLoginAutorizacion(String loginAutorizacion)
  {
    this.loginAutorizacion = loginAutorizacion;
  }
  /**
   * @return Devuelve passwordAutorizacion.
   */
  public String getPasswordAutorizacion()
  {
    return passwordAutorizacion;
  }
  /**
   * @param passwordAutorizacion El passwordAutorizacion a establecer.
   */
  public void setPasswordAutorizacion(String passwordAutorizacion)
  {
    this.passwordAutorizacion = passwordAutorizacion;
  }
  /**
   * @return Devuelve botonAplicarAutorizacion.
   */
  public String getBotonAplicarAutorizacion()
  {
    return botonAplicarAutorizacion;
  }
  
  /**
	 * @return el observacionAutorizacion
	 */
	public String getObservacionAutorizacion() {
		return observacionAutorizacion;
	}

	/**
	 * @param observacionAutorizacion el observacionAutorizacion a establecer
	 */
	public void setObservacionAutorizacion(String observacionAutorizacion) {
		this.observacionAutorizacion = observacionAutorizacion;
	}

	/**
   * @param botonAplicarAutorizacion El botonAplicarAutorizacion a establecer.
   */
  public void setBotonAplicarAutorizacion(String botonAplicarAutorizacion)
  {
    this.botonAplicarAutorizacion = botonAplicarAutorizacion;
  }

	/**
	 * @return el opEstadoActivo
	 */
	public String getOpEstadoActivo() {
		return opEstadoActivo;
	}

	/**
	 * @param opEstadoActivo el opEstadoActivo a establecer
	 */
	public void setOpEstadoActivo(String opEstadoActivo) {
		this.opEstadoActivo = opEstadoActivo;
	}
  
}
