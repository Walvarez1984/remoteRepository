/*
 * Clase ListadoPedidosForm.java
 * Creado el 27/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>Formulario de b\u00FAsqueda gen\u00E9rico usado para cualquier tipo de consulta</p>
 * @author 	fmunoz
 * @version	2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class ListadoPedidosForm extends ActionForm
{
	public final static String INDICE_PAGINACION = "ec.com.smx.sic.sispe.indicePaginacion";
	
	/**
	 * Controles donde se ingresan los par\u00E1metros de busqueda:
	 * <ul>
	 * <li>String codigoLocal: El c\u00F3digo del local donde se desea realizar la b\u00FAsqueda, esta opci\u00F3n solo 
	 * 		est\u00E1 habilitada para la bodega</li>
	 * <li>String campoBusqueda: Campo donde se ingresa el valor del campo a buscar</li>
	 * <li>String fechaInicial: Campo donde se ingresa la fecha inicial, desde donde comienza la consulta</li>
	 * <li>String fechaFinal: Campo donde se ingresa la fecha final, hsta donde llega la consulta</li>
	 * <li>String estadoPedido: Combo donde se escoge el estado del pedido</li>
	 * <li>String opcionCampoBusqueda: RadioButton que permite escoger solo uno de los par\u00E1metros de busqueda</li>
	 * <li>String opcionFecha: RadioButton que permite escoger si se busca por rango de fechas o por toda la temporada</li>
	 * <li>String etiquetaFechas: Permite mostrar la descripci\u00F3n del tipo de fechas utilizadas para la b\u00FAsqueda</li>
	 * <ul>
	 */
	private String codigoLocal;
	private String fechaInicial;
	private String fechaFinal;
	private String estadoPedido;
	private String estadoPagoPedido;
	private String alerta;	
	private String opcionFecha;
	private String etiquetaFechas;
	private String opEntidadResponsable;
	private String opDespachoPendiente;
	private String opStockArticuloReservado;
	private String numeroMeses;
	private String opPedidoOrdenCompra;
	private String opEstadoActivo;
	
	private String numeroPedidoTxt;
	private String numeroReservaTxt;
	private String numeroConsolidadoTxt;
	private String codigoClasificacionTxt;
	private String codigoArticuloTxt;
	private String descripcionArticuloTxt;
	private String documentoPersonalTxt;
	private String nombreContactoTxt;
	private String rucEmpresaTxt;
	private String nombreEmpresaTxt;
	
	private String comboTipoPedido;
	private String comboEstadoPagoPedido;
	private String comboEnviadoCD;
	private String comboTipoFecha;
	private String opTipoAgrupacion;
	
	private FormFile archivo;
	
	//variables para movimiento perecibles
	private String comboMotivoMovimiento;
	private String cantidadMotivoMovimiento;
	private String opConfirmarIncluirDetalle;
	
	private String fechaInicialRepDespachos;
	private String fechaFinalRepDespachos;
	
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
	private Boolean esEstadoActual=Boolean.TRUE;
	
	private String textoMail;
	private String ccMail;
	private String asuntoMail;
	private String emailEnviarCotizacion;
	
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>estadoPedido.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		
		try
		{
			LogSISPE.getLog().info("antes de validar busqueda");
			
			LogSISPE.getLog().info("botonBuscar: {}", request.getParameter("buscar"));

			//cuando se presiona el boton Buscar por c\u00E9dula
			if(request.getParameter("buscar")!=null){
				//se verifica si el usuario logeado no es de un local
				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable()
						.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
					
					if(null != this.codigoLocal && this.codigoLocal.equals("ciudad"))
						errors.add("codigoLocal",new ActionMessage("errors.requerido","Local Responsable"));
				}
				/*------------ cuando se escoge Buscar por fechas ---------------*/
				if(this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))
						&& errors.isEmpty()){
					LogSISPE.getLog().info("Se validan los rangos de fechas...");															
										
					//si no hubieron errores
					validar.validateFecha(errors,"fechaInicial",this.getFechaInicial(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha inicial");
					validar.validateFecha(errors,"fechaFinal",this.getFechaFinal(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha final");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					Date fechaInicial=sdf.parse(this.getFechaInicial());
					Date fechaFinal=sdf.parse(this.getFechaFinal());
					
					String accion = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
					LogSISPE.getLog().info("accion actual: "+accion);
					
					//cuando la accion es entregas, despachos, produccion, si se dejan buscar pedidos posteriores al dia actual
					if(StringUtils.isNotEmpty(accion)
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despachoEspecial"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega")) 
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion")) 
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.enviarDespacho"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteEntregas"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.envio.ordencompra.perecibles"))
							&& !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarAbonoPedido"))){
						
						//valida que la fecha inicial sea menor a la fecha actual
						if(fechaInicial.after(Calendar.getInstance().getTime())){
							errors.add("fechas",new ActionMessage("errors.rango.valores","La fecha inicial","la fecha actual"));
						}
						
						//valida que la fecha final sea menor a la fecha actual
						if(fechaFinal.after(Calendar.getInstance().getTime())){
							errors.add("fechas",new ActionMessage("errors.rango.valores","La fecha final","la fecha actual"));
						}
					}
					
					if( fechaInicial.after(fechaFinal)){
                        errors.add("fechas",new ActionMessage("errors.rango.valores","La fecha inicial","la fecha final"));
                    }
					if(fechaInicial.before(sdf.parse(MessagesWebSISPE.getString("valor.fecha.busqueda")))){
						errors.add("fechas",new ActionMessage("errors.fechaIngresada.menorMinima","La fecha inicial",MessagesWebSISPE.getString("valor.fecha.busqueda")));
					}
				}
				
				/*----------- si se busca por la opci\u00F3n todos ----------------*/
				if(this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))
						&& errors.isEmpty()){
					//si no hubieron errores
					validar.validateLong(errors, "numeroMeses", this.numeroMeses, true, 0, 999, "errors.formato.long", "N\u00FAmero de Meses");
				}
				
			}else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("siEnviarEmail")){
				LogSISPE.getLog().info("request redactarMail");
				validar.validateMandatory(errors,"emailContacto",this.emailEnviarCotizacion,"errors.requerido","Para");
				if(errors.isEmpty()){
					validar.validateFormato(errors,"emailContacto",this.emailEnviarCotizacion,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				if(this.ccMail!=null && !this.ccMail.trim().isEmpty()){
					validar.validateFormato(errors,"emailContactoConCopia",this.ccMail,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				validar.validateMandatory(errors,"asuntoMail",this.asuntoMail,"errors.requerido","Asunto");
				validar.validateMandatory(errors,"textoMail",this.textoMail,"errors.requerido","Contenido");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.textoMail", this.textoMail!=null?this.textoMail:"");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.asuntoMail", this.asuntoMail!=null?this.asuntoMail:"");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.paraMail", this.emailEnviarCotizacion!=null?this.emailEnviarCotizacion:"");
				if (errors.size()>0){
					request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				}
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>estadoPedido.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.codigoLocal=null;
		
		this.fechaInicial=null;
		this.fechaFinal=null;
		this.estadoPedido=null;
		this.estadoPagoPedido = null;
		this.alerta = null;
		
		this.opcionFecha="hoy";
		this.etiquetaFechas=null;
		this.opEntidadResponsable=null;
		this.opPedidoOrdenCompra = null;
		this.opDespachoPendiente=null;
		this.opStockArticuloReservado=null;
		this.opEstadoActivo=null;
		this.numeroMeses = "4";
		
		this.numeroPedidoTxt = null;
		this.numeroReservaTxt = null;
		this.numeroConsolidadoTxt=null;
		this.codigoClasificacionTxt = null;
		this.codigoArticuloTxt = null;
		this.descripcionArticuloTxt = null;
		this.documentoPersonalTxt = null;
		this.nombreContactoTxt = null;
		this.rucEmpresaTxt = null;
		this.nombreEmpresaTxt = null;	
		
		this.comboTipoPedido = null;
		this.comboEstadoPagoPedido = null;
		this.comboEnviadoCD = null;
		this.comboTipoFecha = null;
		
		this.ccMail=null;
		this.emailEnviarCotizacion=null;
		this.asuntoMail=null;
		this.textoMail=null;
		this.fechaInicialRepDespachos=null;
		this.fechaFinalRepDespachos=null;
	}
	
	/**
	 * 
	 * @param datos
	 * @param formulario
	 * @param inicio
	 * @return
	 */
	public Collection paginarDatos(Collection datos, int inicio, int size,boolean paginacionBase) throws Exception{
		LogSISPE.getLog().info("PAGINACION");
        int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        int start = inicio;
        
        this.start = String.valueOf(start);
        this.range = String.valueOf(range);
        this.size = String.valueOf(size);
        
        LogSISPE.getLog().info("start: {}" , this.start);
        LogSISPE.getLog().info("range: {}" , this.range);
        LogSISPE.getLog().info("size: {}" , this.size);
        if(paginacionBase)
        	return datos;
        
        return Util.obtenerSubCollection(datos, start, start + range > size ? size : start + range);
	}
	
	/**
	 * @return Devuelve codigoLocal.
	 */
	public String getCodigoLocal()
	{
		return codigoLocal;
	}
	/**
	 * @param codigoLocal El codigoLocal a establecer.
	 */
	public void setCodigoLocal(String codigoLocal)
	{
		this.codigoLocal = codigoLocal;
	}
	
	// IMPORTANTE: Se comentan m\u00E9todos GET y SET para campoBusqueda
	// ------------------------------------------------------------
	/**
	 * @return Devuelve campoBusqueda.
	 */
	/*public String getCampoBusqueda()
	{
		return campoBusqueda;
	}*/
	/**
	 * @param campoBusqueda El campoBusqueda a establecer.
	 */
	/*public void setCampoBusqueda(String campoBusqueda)
	{
		this.campoBusqueda = campoBusqueda;
	}*/
	// ------------------------------------------------------------

	/**
	 * @return Devuelve estadoPedido.
	 */
	public String getEstadoPedido()
	{
		return estadoPedido;
	}
	/**
	 * @param estadoPedido El estadoPedido a establecer.
	 */
	public void setEstadoPedido(String estadoPedido)
	{
		this.estadoPedido = estadoPedido;
	}



	/**
	 * @return el estadoPagoPedido
	 */
	public String getEstadoPagoPedido() {
		return estadoPagoPedido;
	}

	/**
	 * @param estadoPagoPedido el estadoPagoPedido a establecer
	 */
	public void setEstadoPagoPedido(String estadoPagoPedido) {
		this.estadoPagoPedido = estadoPagoPedido;
	}

	/**
	 * @return Devuelve alerta.
	 */
	public String getAlerta()
	{
		return alerta;
	}
	/**
	 * @param alerta El alerta a establecer.
	 */
	public void setAlerta(String alerta)
	{
		this.alerta = alerta;
	}
	/**
	 * @return Devuelve fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}
	/**
	 * @param fechaFinal El fechaFinal a establecer.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Devuelve fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}
	/**
	 * @param fechaInicial El fechaInicial a establecer.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	// IMPORTANTE: Se comentan m\u00E9todos GET y SET para opcionCampoBusqueda
	// ------------------------------------------------------------------
	/**
	 * @return Devuelve opcionCampoBusqueda.
	 */
	/*public String getOpcionCampoBusqueda()
	{
		return opcionCampoBusqueda;
	}*/
	/**
	 * @param opcionCampoBusqueda El opcionCampoBusqueda a establecer.
	 */
	/*public void setOpcionCampoBusqueda(String opcionCampoBusqueda)
	{
		this.opcionCampoBusqueda = opcionCampoBusqueda;
	}*/
	// ------------------------------------------------------------------

	/**
	 * @return Devuelve opcionFecha.
	 */
	public String getOpcionFecha()
	{
		return opcionFecha;
	}
	/**
	 * @param opcionFecha El opcionFecha a establecer.
	 */
	public void setOpcionFecha(String opcionFecha)
	{
		this.opcionFecha = opcionFecha;
	}
	/**
	 * @return Devuelve datos.
	 */
	public Collection getDatos()
	{
		return datos;
	}
	/**
	 * @param datos El datos a establecer.
	 */
	public void setDatos(Collection datos)
	{
		this.datos = datos;
	}
	/**
	 * @return Devuelve range.
	 */
	public String getRange()
	{
		return range;
	}
	/**
	 * @param range El range a establecer.
	 */
	public void setRange(String range)
	{
		this.range = range;
	}
	/**
	 * @return Devuelve size.
	 */
	public String getSize()
	{
		return size;
	}
	/**
	 * @param size El size a establecer.
	 */
	public void setSize(String size)
	{
		this.size = size;
	}
	/**
	 * @return Devuelve start.
	 */
	public String getStart()
	{
		return start;
	}
	/**
	 * @param start El start a establecer.
	 */
	public void setStart(String start)
	{
		this.start = start;
	}

	/**
	 * @return el etiquetaFechas
	 */
	public String getEtiquetaFechas() {
		return etiquetaFechas;
	}

	/**
	 * @param etiquetaFechas el etiquetaFechas a establecer
	 */
	public void setEtiquetaFechas(String etiquetaFechas) {
		this.etiquetaFechas = etiquetaFechas;
	}

	/**
	 * @return el opEntidadResponsable
	 */
	public String getOpEntidadResponsable() {
		return opEntidadResponsable;
	}

	/**
	 * @param opEntidadResponsable el opEntidadResponsable a establecer
	 */
	public void setOpEntidadResponsable(String opEntidadResponsable) {
		this.opEntidadResponsable = opEntidadResponsable;
	}

	/**
	 * @return el numeroMeses
	 */
	public String getNumeroMeses() {
		return numeroMeses;
	}

	/**
	 * @param numeroMeses el numeroMeses a establecer
	 */
	public void setNumeroMeses(String numeroMeses) {
		this.numeroMeses = numeroMeses;
	}

	/**
	 * @return el opDespachoPendiente
	 */
	public String getOpDespachoPendiente() {
		return opDespachoPendiente;
	}

	/**
	 * @param opDespachoPendiente el opDespachoPendiente a establecer
	 */
	public void setOpDespachoPendiente(String opDespachoPendiente) {
		this.opDespachoPendiente = opDespachoPendiente;
	}

	/**
	 * @return el opPedidoOrdenCompra
	 */
	public String getOpPedidoOrdenCompra() {
		return opPedidoOrdenCompra;
	}

	/**
	 * @param opPedidoOrdenCompra el opPedidoOrdenCompra a establecer
	 */
	public void setOpPedidoOrdenCompra(String opPedidoOrdenCompra) {
		this.opPedidoOrdenCompra = opPedidoOrdenCompra;
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

	// IMPORTANTE: M\u00E9todos GET y SET para b\u00FAsqueda m\u00FAltiple
	// ------------------------------------------------------------------------------
	/**
	 * @return el codigoArticuloTxt
	 */
	public String getCodigoArticuloTxt() {
		return codigoArticuloTxt;
	}

	/**
	 * @param codigoArticuloTxt el codigoArticuloTxt a establecer
	 */
	public void setCodigoArticuloTxt(String codigoArticuloTxt) {
		this.codigoArticuloTxt = codigoArticuloTxt;
	}

	/**
	 * @return el codigoClasificacionTxt
	 */
	public String getCodigoClasificacionTxt() {
		return codigoClasificacionTxt;
	}

	/**
	 * @param codigoClasificacionTxt el codigoClasificacionTxt a establecer
	 */
	public void setCodigoClasificacionTxt(String codigoClasificacionTxt) {
		this.codigoClasificacionTxt = codigoClasificacionTxt;
	}

	/**
	 * @return el descripcionArticuloTxt
	 */
	public String getDescripcionArticuloTxt() {
		return descripcionArticuloTxt;
	}

	/**
	 * @param descripcionArticuloTxt el descripcionArticuloTxt a establecer
	 */
	public void setDescripcionArticuloTxt(String descripcionArticuloTxt) {
		this.descripcionArticuloTxt = descripcionArticuloTxt;
	}

	/**
	 * @return el documentoPersonalTxt
	 */
	public String getDocumentoPersonalTxt() {
		return documentoPersonalTxt;
	}

	/**
	 * @param documentoPersonalTxt el documentoPersonalTxt a establecer
	 */
	public void setDocumentoPersonalTxt(String documentoPersonalTxt) {
		this.documentoPersonalTxt = documentoPersonalTxt;
	}

	/**
	 * @return el nombreContactoTxt
	 */
	public String getNombreContactoTxt() {
		return nombreContactoTxt;
	}

	/**
	 * @param nombreContactoTxt el nombreContactoTxt a establecer
	 */
	public void setNombreContactoTxt(String nombreContactoTxt) {
		this.nombreContactoTxt = nombreContactoTxt;
	}

	/**
	 * @return el nombreEmpresaTxt
	 */
	public String getNombreEmpresaTxt() {
		return nombreEmpresaTxt;
	}

	/**
	 * @param nombreEmpresaTxt el nombreEmpresaTxt a establecer
	 */
	public void setNombreEmpresaTxt(String nombreEmpresaTxt) {
		this.nombreEmpresaTxt = nombreEmpresaTxt;
	}

	/**
	 * @return el numeroPedidoTxt
	 */
	public String getNumeroPedidoTxt() {
		return numeroPedidoTxt;
	}

	/**
	 * @param numeroPedidoTxt el numeroPedidoTxt a establecer
	 */
	public void setNumeroPedidoTxt(String numeroPedidoTxt) {
		this.numeroPedidoTxt = numeroPedidoTxt;
	}

	/**
	 * @return el numeroReservaTxt
	 */
	public String getNumeroReservaTxt() {
		return numeroReservaTxt;
	}

	/**
	 * @param numeroReservaTxt el numeroReservaTxt a establecer
	 */
	public void setNumeroReservaTxt(String numeroReservaTxt) {
		this.numeroReservaTxt = numeroReservaTxt;
	}

	/**
	 * @return el rucEmpresaTxt
	 */
	public String getRucEmpresaTxt() {
		return rucEmpresaTxt;
	}

	/**
	 * @param rucEmpresaTxt el rucEmpresaTxt a establecer
	 */
	public void setRucEmpresaTxt(String rucEmpresaTxt) {
		this.rucEmpresaTxt = rucEmpresaTxt;
	}	
	
	// ------------------------------------------------------------------------------
	
	// IMPORTANTE: M\u00E9todos GET y SET para variable que permite escoger el tipo de agrupamiento de reporte de entregas
	// --------------------------------------------------------------------------------------------------------------
	//
	public String getOpTipoAgrupacion() {
		return opTipoAgrupacion;
	}

	public void setOpTipoAgrupacion(String opTipoAgrupacion) {
		this.opTipoAgrupacion = opTipoAgrupacion;
	}
	// --------------------------------------------------------------------------------------------------------------

	// OANDINO: M\u00E9todos GET y SET para combo de estado de pago de pedido
	public String getComboEstadoPagoPedido() {
		return comboEstadoPagoPedido;
	}

	public void setComboEstadoPagoPedido(String comboEstadoPagoPedido) {
		this.comboEstadoPagoPedido = comboEstadoPagoPedido;
	}

	//OANDINO: M\u00E9todos GET y SET para combo de env\u00EDo de reservas a CD
	public String getComboEnviadoCD() {
		return comboEnviadoCD;
	}

	public void setComboEnviadoCD(String comboEnviadoCD) {
		this.comboEnviadoCD = comboEnviadoCD;
	}

	//OANDINO: M\u00E9todos GET y SET para combo de tipo de fecha 
	public String getComboTipoFecha() {
		return comboTipoFecha;
	}

	public void setComboTipoFecha(String comboTipoFecha) {
		this.comboTipoFecha = comboTipoFecha;
	}

	/**
	 * @return el comboTipoPedido
	 */
	public String getComboTipoPedido() {
		return comboTipoPedido;
	}

	/**
	 * @param comboTipoPedido el comboTipoPedido a establecer
	 */
	public void setComboTipoPedido(String comboTipoPedido) {
		this.comboTipoPedido = comboTipoPedido;
	}

	/**
	 * @return el archivo
	 */
	public FormFile getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo el archivo a establecer
	 */
	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return el opStockArticuloReservado
	 */
	public String getOpStockArticuloReservado() {
		return opStockArticuloReservado;
	}

	/**
	 * @param opStockArticuloReservado el opStockArticuloReservado a establecer
	 */
	public void setOpStockArticuloReservado(String opStockArticuloReservado) {
		this.opStockArticuloReservado = opStockArticuloReservado;
	}

	public String getNumeroConsolidadoTxt() {
		return numeroConsolidadoTxt;
	}

	public void setNumeroConsolidadoTxt(String numeroConsolidadoTxt) {
		this.numeroConsolidadoTxt = numeroConsolidadoTxt;
	}

	public String getComboMotivoMovimiento() {
		return comboMotivoMovimiento;
	}

	public void setComboMotivoMovimiento(String comboMotivoMovimiento) {
		this.comboMotivoMovimiento = comboMotivoMovimiento;
	}

	public String getCantidadMotivoMovimiento() {
		return cantidadMotivoMovimiento;
	}

	public void setCantidadMotivoMovimiento(String cantidadMotivoMovimiento) {
		this.cantidadMotivoMovimiento = cantidadMotivoMovimiento;
	}

	public String getOpConfirmarIncluirDetalle() {
		return opConfirmarIncluirDetalle;
	}

	public void setOpConfirmarIncluirDetalle(String opConfirmarIncluirDetalle) {
		this.opConfirmarIncluirDetalle = opConfirmarIncluirDetalle;
	}

	public Boolean getEstadoActual() {
		return esEstadoActual;
	}

	public String getTextoMail() {
		return textoMail;
	}

	public void setTextoMail(String textoMail) {
		this.textoMail = textoMail;
	}

	public String getCcMail() {
		return ccMail;
	}

	public void setCcMail(String ccMail) {
		this.ccMail = ccMail;
	}

	public String getAsuntoMail() {
		return asuntoMail;
	}

	public void setAsuntoMail(String asuntoMail) {
		this.asuntoMail = asuntoMail;
	}

	public String getEmailEnviarCotizacion() {
		return emailEnviarCotizacion;
	}

	public void setEmailEnviarCotizacion(String emailEnviarCotizacion) {
		this.emailEnviarCotizacion = emailEnviarCotizacion;
	}

	public String getFechaInicialRepDespachos() {
		return fechaInicialRepDespachos;
	}

	public void setFechaInicialRepDespachos(String fechaInicialRepDespachos) {
		this.fechaInicialRepDespachos = fechaInicialRepDespachos;
	}

	public String getFechaFinalRepDespachos() {
		return fechaFinalRepDespachos;
	}

	public void setFechaFinalRepDespachos(String fechaFinalRepDespachos) {
		this.fechaFinalRepDespachos = fechaFinalRepDespachos;
	}
}
