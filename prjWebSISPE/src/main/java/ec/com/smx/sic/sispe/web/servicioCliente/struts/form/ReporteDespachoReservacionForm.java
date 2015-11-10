/*
 * Creado el 12/04/2007
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Clase que maneja las b&uacute;squedas para sacar los reportes de despacho de reservaci&oacute;n. 
 * </p>
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("serial")
public class ReporteDespachoReservacionForm extends ActionForm 
{
/**
   * Campos para realizar la b\u00FAsqueda en el formulario de reporte despacho reservaci&oacute;n.
   * <ul>
   * <li>String opcion: Campo que registra la opci\u00F3n por la que se realizar\u00E1 la b\u00FAsqueda</li>
   * <li>String numeroPedido: Campo donde se ingresar\u00E1 el n\u00FAmero de pedido para la b\u00FAsqueda</li>
   * <li>String codigoArticulo: Campo donde se ingresar\u00E1 el c\u00F3digo de art\u00EDculo para la b\u00FAsqueda</li>
   * <li>String codigoLocal: Campo donde se ingresar\u00E1 el c\u00F3digo del local para la b\u00FAsqueda</li>
   * <li>String fecha: Campo donde se ingresar\u00E1 el tipo de fecha para la b\u00FAsqueda</li>
   * <li>String fechaInicial: Campo dende se ingresar\u00E1 la fecha incial del rango de b\u00FAsqueda</li>
   * <li>String fechaFinal: Campo dende se ingresar\u00E1 la fecha final del rango de b\u00FAsqueda</li>
   * <ul>
   */
	
	  private String opcionesFecha;
	  private String numeroPedido;
	  private String codigoArticulo;
	  private String codigoLocal;
	  private String fecha;	  
	  private String fechaInicial;
	  private String fechaFinal;
	  private String opcionNumeroPedido;
	  private String opcionCodigoArticulo;
	  private String opcionCodigoLocal;
	  private String opcionFecha;
	  private String botonExportarPDF;
	  private String botonExportarExcel;
	  
	  /**
	   * Par\u00E1metros de paginaci\u00F3n
	   * <ul>
	   * <li>Collection datos: Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
	   * <li>String start: Indice para el inicio del paginador</li>
	   * <li>String range: Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada paginaci\u00F3n</li>
	   * <li>String size: Valor que indica el n\u00FAmero total de registros.</li>
	   * </ul>
	   */
	  private Collection datos;
	  private String start;
	  private String range;
	  private String size;
	  
	  /**
	   * Resetea los controles del formulario de la p\u00E1gina <code>filtrosDespachoReservacion.jsp</code>, en cada petici\u00F3n.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   */
	  public void reset(ActionMapping mapping, HttpServletRequest request) 
	  {
	    //se estabslece la opci\u00F3n de b\u00FAsqueda por defecto por n\u00FAmero de pedido
	    this.opcionNumeroPedido = null;
	    this.numeroPedido=null;
	    this.codigoArticulo = null;
	    this.codigoLocal = null;
	    this.fecha = null;
	    this.fechaInicial = null;
	    this.fechaFinal = null;
		this.opcionesFecha=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaTemporada");
		this.opcionCodigoArticulo=null;
		this.opcionCodigoLocal=null;
		this.opcionFecha=null;
		this.botonExportarExcel=null;
		this.botonExportarPDF=null;
		/*this.datos=null;
		this.start=null;
		this.range=null;
		this.size=null;*/

	  }
	  /**
	   * Ejecuta la validacion en la p\u00E1gina JSP <code>filtrosDespachoReservacion.jsp</code>
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia	
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	   * <code>null</code>
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    ActionErrors errors = new ActionErrors();
	    PropertyValidator validar = new PropertyValidatorImpl();
	    
	    LogSISPE.getLog().info("boton buscar: {}" , request.getParameter("botonBuscar"));
	        
	    if (request.getParameter("botonBuscar") != null) {
	    	 validar.validateMandatory(errors, "codigoLocal", this.codigoLocal,"errors.codigoLocal.requerido");
	    	if(this.opcionNumeroPedido==null && this.opcionCodigoArticulo==null && this.opcionCodigoLocal==null && this.opcionFecha==null)
	        	errors.add("criterio",new ActionMessage("errors.numeroPedido.requerido"));
	        //cuando se escoge Buscar por numero de pedido
	        if (this.opcionNumeroPedido!=null) 
	          validar.validateMandatory(errors, "numeroPedido", this.numeroPedido,"errors.numeroPedido.requerido");
	        if (this.opcionCodigoArticulo!=null) 
		          validar.validateMandatory(errors, "codigoArticulo", this.codigoArticulo,"errors.codigoArticulo.requerido");
	        if (this.opcionFecha!=null){
		          validar.validateMandatory(errors, "fecha", this.fecha,"errors.fecha.requerido");
	        	if(this.opcionesFecha!=null && !this.opcionesFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaTemporada"))){
			          validar.validateMandatory(errors, "fecha", this.fechaInicial,"errors.fechaInicial.requerido");
			          validar.validateMandatory(errors, "fecha", this.fechaFinal,"errors.fechaFinal.requerido");
	        	}
	        }
	      }
	    LogSISPE.getLog().info("numero de errores: {}", errors.size());
	    
	    return(errors);
	    }
	  
	/**
	 * @return Devuelve codigoArticulo.
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo El codigoArticulo a establecer.
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Devuelve codigoLocal.
	 */
	public String getCodigoLocal() {
		return codigoLocal;
	}
	/**
	 * @param codigoLocal El codigoLocal a establecer.
	 */
	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}
	/**
	 * @return Devuelve fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha El fecha a establecer.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Devuelve fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal El fechaFinal a establecer.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Devuelve fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial El fechaInicial a establecer.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Devuelve numeroPedido.
	 */
	public String getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido El numeroPedido a establecer.
	 */
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	/**
	 * @return Devuelve opcionCodigoArticulo.
	 */
	public String getOpcionCodigoArticulo() {
		return opcionCodigoArticulo;
	}
	/**
	 * @param opcionCodigoArticulo El opcionCodigoArticulo a establecer.
	 */
	public void setOpcionCodigoArticulo(String opcionCodigoArticulo) {
		this.opcionCodigoArticulo = opcionCodigoArticulo;
	}
	/**
	 * @return Devuelve opcionCodigoLocal.
	 */
	public String getOpcionCodigoLocal() {
		return opcionCodigoLocal;
	}
	/**
	 * @param opcionCodigoLocal El opcionCodigoLocal a establecer.
	 */
	public void setOpcionCodigoLocal(String opcionCodigoLocal) {
		this.opcionCodigoLocal = opcionCodigoLocal;
	}
/**
 * @return Devuelve opcionesFecha.
 */
public String getOpcionesFecha() {
	return opcionesFecha;
}
/**
 * @param opcionesFecha El opcionesFecha a establecer.
 */
public void setOpcionesFecha(String opcionesFecha) {
	this.opcionesFecha = opcionesFecha;
}
	/**
	 * @return Devuelve opcionFecha.
	 */
	public String getOpcionFecha() {
		return opcionFecha;
	}
	/**
	 * @param opcionFecha El opcionFecha a establecer.
	 */
	public void setOpcionFecha(String opcionFecha) {
		this.opcionFecha = opcionFecha;
	}
	/**
	 * @return Devuelve opcionNumeroPedido.
	 */
	public String getOpcionNumeroPedido() {
		return opcionNumeroPedido;
	}
	/**
	 * @param opcionNumeroPedido El opcionNumeroPedido a establecer.
	 */
	public void setOpcionNumeroPedido(String opcionNumeroPedido) {
		this.opcionNumeroPedido = opcionNumeroPedido;
	}
	
	/**
	 * @return Devuelve datos.
	 */
	public Collection getDatos() {
		return datos;
	}
	/**
	 * @param datos El datos a establecer.
	 */
	public void setDatos(Collection datos) {
		this.datos = datos;
	}
	/**
	 * @return Devuelve range.
	 */
	public String getRange() {
		return range;
	}
	/**
	 * @param range El range a establecer.
	 */
	public void setRange(String range) {
		this.range = range;
	}
	/**
	 * @return Devuelve size.
	 */
	public String getSize() {
		return size;
	}
	/**
	 * @param size El size a establecer.
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * @return Devuelve start.
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start El start a establecer.
	 */
	public void setStart(String start) {
		this.start = start;
	}
	/**
	 * @return el botonExportarExcel
	 */
	public String getBotonExportarExcel() {
		return this.botonExportarExcel;
	}
	/**
	 * @param botonExportarExcel el botonExportarExcel a establecer
	 */
	public void setBotonExportarExcel(String botonExportarExcel) {
		this.botonExportarExcel = botonExportarExcel;
	}
	/**
	 * @return el botonExportarPDF
	 */
	public String getBotonExportarPDF() {
		return this.botonExportarPDF;
	}
	/**
	 * @param botonExportarPDF el botonExportarPDF a establecer
	 */
	public void setBotonExportarPDF(String botonExportarPDF) {
		this.botonExportarPDF = botonExportarPDF;
	}
	
}
