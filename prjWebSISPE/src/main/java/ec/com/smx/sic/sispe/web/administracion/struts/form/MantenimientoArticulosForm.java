/*
 * MantenimientoArticulosForm.java
 * Creado el 21/11/2007 11:23:27
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;


/**
 * <p>
 * Crea los campos de las pantallas utilizadas en el proceso de actualizaci\u00F3n de los datos de un art\u00EDculo
 * y adem\u00E1s determina la validez de dichos datos.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5
 */

@SuppressWarnings("serial")
public class MantenimientoArticulosForm extends ActionForm
{
	//campos del formulario principal
	private String codigoArticulo;
	private String opTipoBusqueda;
	private String textoBusqueda;
	
	//campos del formulario de actualizaci\u00F3n
	private String descripcionArticulo; 
	private String precioArticulo;
	private String precioCajaArticulo;
	private String pesoAproximado;
	private String unidadManejo;
	private String unidadManejoCaja;
	private String estadoArticulo;
	private String tipoArticuloCalculoPrecio;
	
  /**
   * Realiza la validaci\u00F3n en la p\u00E1gina <code>actualizarArticulo.jsp</code>
   * 
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		El request que estamos procesando
   * @return errors		Los errores recogidos durante la ejecuci\u00F3n
   */
  public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
  {
  	ActionErrors errors = new ActionErrors();
    PropertyValidator validar = new PropertyValidatorImpl();
    
  	//se verifica el valor del par\u00E1metro
  	String peticion = request.getParameter("ayuda");
  	if(peticion == null){
  		peticion = "";
  	}
  	
    try{
	  	//------------ si se desea guardar la configuraci\u00F3n de la clasificaci\u00F3n -------------
	  	if(peticion.equals("actualizarArticulo")){
	  		//se validan los datos del formulario
	      validar.validateMandatory(errors,"descripcionArticulo",this.descripcionArticulo,"errors.requerido","Descripcion");
	      validar.validateDouble(errors, "precioArticulo", this.precioArticulo, true, 0.01, 999999.99, "errors.formato.double", "Precio");
	      validar.validateDouble(errors, "precioCajaArticulo", this.precioCajaArticulo, true, 0.00, 999999.99, "errors.formato.double", "Precio Caja");
	      validar.validateDouble(errors, "pesoAproximado", this.pesoAproximado, true, 0.00, 999999.99, "errors.formato.double", "Peso Aproximado");
	      validar.validateLong(errors, "unidadManejo", this.unidadManejo, true, 0, 99999, "errors.formato.long", "Unidad de Manejo");
	      validar.validateLong(errors, "unidadManejoCaja", this.unidadManejoCaja, true, 0, 99999, "errors.formato.long", "Unidad de Manejo Caja");
	      validar.validateMandatory(errors, "tipoArticuloCalculoPrecio", this.tipoArticuloCalculoPrecio, "errors.requerido", "Tipo de Art\u00EDculo (para calculos)");
	      validar.validateMandatory(errors, "estadoArticulo", this.estadoArticulo, "errors.requerido", "Estado del Art\u00EDculo");
	  	}
    }catch(Exception ex){
    	LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    }
    //se retornan los errores generados
  	return errors;
  }
  
  /**
   * Inicializa los campos del formulario de actualizaci\u00F3n de los datos de un art\u00EDculo.
   * 
   * @param mapping 	El mapeo utilizado para seleccionar esta instancia
   * @param request 	La petici&oacute;n que estamos procesando
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
  	this.codigoArticulo = null;
  	this.opTipoBusqueda = "nucl";
  	this.textoBusqueda = null;
  	
  	this.descripcionArticulo = null;
  	this.precioArticulo = null;
  	this.precioCajaArticulo = null;
  	this.pesoAproximado = null;
  	this.unidadManejo = null;
  	this.unidadManejoCaja = null;
  	this.estadoArticulo = null;
  }

	/**
	 * @return el descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * @param descripcionArticulo el descripcionArticulo a establecer
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * @return el estadoArticulo
	 */
	public String getEstadoArticulo() {
		return estadoArticulo;
	}

	/**
	 * @param estadoArticulo el estadoArticulo a establecer
	 */
	public void setEstadoArticulo(String estadoArticulo) {
		this.estadoArticulo = estadoArticulo;
	}

	/**
	 * @return el pesoAproximado
	 */
	public String getPesoAproximado() {
		return pesoAproximado;
	}

	/**
	 * @param pesoAproximado el pesoAproximado a establecer
	 */
	public void setPesoAproximado(String pesoAproximado) {
		this.pesoAproximado = pesoAproximado;
	}

	/**
	 * @return el precioArticulo
	 */
	public String getPrecioArticulo() {
		return precioArticulo;
	}

	/**
	 * @param precioArticulo el precioArticulo a establecer
	 */
	public void setPrecioArticulo(String precioArticulo) {
		this.precioArticulo = precioArticulo;
	}

	/**
	 * @return el precioCajaArticulo
	 */
	public String getPrecioCajaArticulo() {
		return precioCajaArticulo;
	}

	/**
	 * @param precioCajaArticulo el precioCajaArticulo a establecer
	 */
	public void setPrecioCajaArticulo(String precioCajaArticulo) {
		this.precioCajaArticulo = precioCajaArticulo;
	}

	/**
	 * @return el unidadManejo
	 */
	public String getUnidadManejo() {
		return unidadManejo;
	}

	/**
	 * @param unidadManejo el unidadManejo a establecer
	 */
	public void setUnidadManejo(String unidadManejo) {
		this.unidadManejo = unidadManejo;
	}

	/**
	 * @return el unidadManejoCaja
	 */
	public String getUnidadManejoCaja() {
		return unidadManejoCaja;
	}

	/**
	 * @param unidadManejoCaja el unidadManejoCaja a establecer
	 */
	public void setUnidadManejoCaja(String unidadManejoCaja) {
		this.unidadManejoCaja = unidadManejoCaja;
	}

	/**
	 * @return el codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo el codigoArticulo a establecer
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	/**
	 * @return el opTipoBusqueda
	 */
	public String getOpTipoBusqueda() {
		return opTipoBusqueda;
	}

	/**
	 * @param opTipoBusqueda el opTipoBusqueda a establecer
	 */
	public void setOpTipoBusqueda(String opTipoBusqueda) {
		this.opTipoBusqueda = opTipoBusqueda;
	}

	/**
	 * @return el textoBusqueda
	 */
	public String getTextoBusqueda() {
		return textoBusqueda;
	}

	/**
	 * @param textoBusqueda el textoBusqueda a establecer
	 */
	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}

	/**
	 * @return el tipoArticuloCalculoPrecio
	 */
	public String getTipoArticuloCalculoPrecio() {
		return tipoArticuloCalculoPrecio;
	}

	/**
	 * @param tipoArticuloCalculoPrecio el tipoArticuloCalculoPrecio a establecer
	 */
	public void setTipoArticuloCalculoPrecio(String tipoArticuloCalculoPrecio) {
		this.tipoArticuloCalculoPrecio = tipoArticuloCalculoPrecio;
	}

  
}