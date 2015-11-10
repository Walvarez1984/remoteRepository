package ec.com.smx.sic.sispe.web.calendarioBodega.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
/**
 * @author jmena
 *
 */
@SuppressWarnings("serial")
public class LineaProduccionForm extends ActionForm {

	/**
	 * Par\u00E1metros de paginaci\u00F3n
	 * <ul>
	 * <li>Collection datos: Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
	 * <li>String start: Indice para el inicio del paginador</li>
	 * <li>String range: Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada paginaci\u00F3n</li>
	 * <li>String size: Valor que indica el n\u00FAmero total de registros.</li>
	 * </ul>
	 */
	public static final String COL_CAL_ART_PAGINADOS = "ec.com.smx.sic.sispe.vistaReporteCalArtDTOColPaginado";
	public static final String COL_CAL_ART_COMPLETOS = "ec.com.smx.sic.sispe.vistaReporteCalArtDTOCol";
	
	//private Collection datos;
	private String start;
	private String range;
	private String size;
	private String codigoArticuloTxt;
	private String codigoClasificacionTxt;
	private String descripcionArticuloTxt;
	private String fechaCalendario;
	private String fechaInicial;
	private String fechaFinal;
	private String opcionFecha;
	private String numeroMeses;
	private String articuloCombo;
	private String [] checksSeleccionar; 
	private String checkTodo;
	private String cantidad;
	private String tipoMovCombo;
	private String observacion;
	private String tipoMotMov;
	private String cmbOpcionAgrupacion;
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
				/*------------ cuando se escoge Buscar por fechas ---------------*/
				if(this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))
						&& errors.isEmpty()){
					LogSISPE.getLog().info("Se validan los rangos de fechas...");					
					//si no hubieron errores
					validar.validateFecha(errors,"fechaInicial",this.getFechaInicial(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha inicial");
					validar.validateFecha(errors,"fechaFinal",this.getFechaFinal(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha final");									
				}
				
				/*----------- si se busca por la opci\u00F3n todos ----------------*/
				if(this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))
						&& errors.isEmpty()){
					//si no hubieron errores
					validar.validateLong(errors, "numeroMeses", this.numeroMeses, true, 0, 999, "errors.formato.long", "N\u00FAmero de Meses");
				}	
			} 
			
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.fechaInicial=null;
		this.fechaFinal=null;
		this.fechaCalendario=null;
		this.opcionFecha="hoy";
		this.numeroMeses = "4";
		this.articuloCombo = null;
		this.codigoClasificacionTxt = null;
		this.codigoArticuloTxt = null;
		this.descripcionArticuloTxt = null;	
		this.checksSeleccionar = null;
		this.checkTodo = null;
		this.cantidad = null;
		this.tipoMovCombo = null;
		this.observacion = null;
		this.tipoMotMov = null;
		//this.cmbOpcionAgrupacion = null;
	}
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param datos
	 */
	public static void realizarPaginacion(HttpServletRequest request, LineaProduccionForm formulario, Collection datos, String tipoPaginacion)
	{
		int inicio = 0;
		int rango = 0;
		HttpSession session = request.getSession();
		try{
			if(request.getParameter("start") != null){
				inicio = Integer.parseInt(request.getParameter("start"));
			}
			if(tipoPaginacion!=null){
				rango = Integer.parseInt(MessagesWebSISPE.getString(tipoPaginacion));
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}

		//si la colecci\u00F3n a paginar no es nula
		if(datos!=null && !datos.isEmpty()){
			formulario.setStart(String.valueOf(inicio));
			formulario.setRange(String.valueOf(rango));
			formulario.setSize(String.valueOf(datos.size()));
	
			session.setAttribute(COL_CAL_ART_PAGINADOS, Util.obtenerSubCollection(datos, inicio, inicio + rango > datos.size() ? datos.size() : inicio + rango));
		}else{
			formulario.setStart(null);
			formulario.setRange(null);
			formulario.setSize(null);
			session.removeAttribute(COL_CAL_ART_PAGINADOS);
		}
	}
	

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
	 * @return el range
	 */
	public String getRange() {
		return range;
	}
	/**
	 * @param range el range a establecer
	 */
	public void setRange(String range) {
		this.range = range;
	}
	/**
	 * @return el size
	 */
	public String getSize() {
		return size;
	}
	/**
	 * @param size el size a establecer
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * @return el start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start el start a establecer
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return el fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal el fechaFinal a establecer
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return el fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial el fechaInicial a establecer
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return el opcionFecha
	 */
	public String getOpcionFecha() {
		return opcionFecha;
	}

	/**
	 * @param opcionFecha el opcionFecha a establecer
	 */
	public void setOpcionFecha(String opcionFecha) {
		this.opcionFecha = opcionFecha;
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
	 * @return el articuloCombo
	 */
	public String getArticuloCombo() {
		return articuloCombo;
	}

	/**
	 * @param articuloCombo el articuloCombo a establecer
	 */
	public void setArticuloCombo(String articuloCombo) {
		this.articuloCombo = articuloCombo;
	}

	/**
	 * @return el fechaCalendario
	 */
	public String getFechaCalendario() {
		return fechaCalendario;
	}

	/**
	 * @param fechaCalendario el fechaCalendario a establecer
	 */
	public void setFechaCalendario(String fechaCalendario) {
		this.fechaCalendario = fechaCalendario;
	}

	/**
	 * @return el checksSeleccionar
	 */
	public String[] getChecksSeleccionar() {
		return checksSeleccionar;
	}

	/**
	 * @param checksSeleccionar el checksSeleccionar a establecer
	 */
	public void setChecksSeleccionar(String[] checksSeleccionar) {
		this.checksSeleccionar = checksSeleccionar;
	}

	/**
	 * @return el checkTodo
	 */
	public String getCheckTodo() {
		return checkTodo;
	}

	/**
	 * @param checkTodo el checkTodo a establecer
	 */
	public void setCheckTodo(String checkTodo) {
		this.checkTodo = checkTodo;
	}

	/**
	 * @return el cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad el cantidad a establecer
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return el observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion el observacion a establecer
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return el tipoMovCombo
	 */
	public String getTipoMovCombo() {
		return tipoMovCombo;
	}

	/**
	 * @param tipoMovCombo el tipoMovCombo a establecer
	 */
	public void setTipoMovCombo(String tipoMovCombo) {
		this.tipoMovCombo = tipoMovCombo;
	}

	public String getTipoMotMov() {
		return tipoMotMov;
	}

	public void setTipoMotMov(String tipoMotMov) {
		this.tipoMotMov = tipoMotMov;
	}

	/**
	 * @return el cmbOpcionAgrupacion
	 */
	public String getCmbOpcionAgrupacion() {
		return cmbOpcionAgrupacion;
	}

	/**
	 * @param cmbOpcionAgrupacion el cmbOpcionAgrupacion a establecer
	 */
	public void setCmbOpcionAgrupacion(String cmbOpcionAgrupacion) {
		this.cmbOpcionAgrupacion = cmbOpcionAgrupacion;
	}

}
