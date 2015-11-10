package ec.com.smx.sic.sispe.web.reportes.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.form.BaseForm;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

@SuppressWarnings("serial")
public class ReporteGeneralForm extends BaseForm {
	public static final String COL_PEDIDOS_PAGINADOS = "ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado";
	public static final String COL_PEDIDOS_COMPLETOS = "ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol";
	public static final String COL_PEDIDOS_COMPLETOS_EXCEL = "ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel";
	
	private String txtValorBusqueda;
	private String txtFechaInicial;
	private String txtFechaFinal;
	private String cmbCodigoLocal;
	private String cmbCodigoEstadoPagado;
	private String cmbOpcionEstadoPedido;
	@SuppressWarnings("unused")
	private String cmbCodigoEstado;
	private String cmbTipoFecha;
	private String botonBuscar;
	private String botonExportarReporte;
	private String opcionValorBusqueda;
	private String opcionFechaBusqueda;
	private String numeroMeses;
	private String opTipoAgrupacion;
	private boolean filtrarPorUsuario; 
	
	//Paginaci\u00F3n
	private Collection datos;
	private String start;
	private String range;
	private String size;
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.txtFechaFinal = null;
		this.txtFechaInicial = null;
		this.txtValorBusqueda = null;
		this.cmbCodigoLocal = null;
		this.cmbTipoFecha = null;
		this.botonBuscar = null;
		this.botonExportarReporte = null;
		this.opcionFechaBusqueda = null;
		this.opcionValorBusqueda = null;
		this.cmbCodigoEstadoPagado = null;
		//this.cmbOpcionEstadoPedido = null; 
		this.cmbCodigoEstado = null;	
		this.filtrarPorUsuario = false;
		this.numeroMeses = "4";
	}
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		PropertyValidator validador = new PropertyValidatorImpl();
		String ayuda = request.getParameter(Globals.AYUDA);
		
		LogSISPE.getLog().info("Metodo validate de ReporteGeneralForm");
		LogSISPE.getLog().info("ayuda de ReporteGeneralForm: {}", ayuda);
		
	    if(ayuda != null && !ayuda.equals("") && !ayuda.equals("aceptarReporte")){
	    	if (ayuda.equals("pdf") || ayuda.equals("xls")){
	    		this.botonExportarReporte = ayuda;
	    	}
	    }
		
		if (this.botonBuscar != null){
			
			if (this.cmbCodigoLocal != null && this.cmbCodigoLocal.equals("ciudad")){
				errores.add("cmbCodigoLocal", new ActionMessage("errors.requerido", "Local Responsable"));	
			}
			
			//se valida el tipo de fecha
			if(this.cmbTipoFecha!= null){
				if(!this.cmbTipoFecha.equals("")){
					//validar que se ingresen la fecha inicial y la fecha final si se selecciona la opci\u00F3n para ingresar un rango de fechas
					if (this.opcionFechaBusqueda != null && this.opcionFechaBusqueda.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
						
						validador.validateFecha(errores, "txtFechaInicial", this.txtFechaInicial, true, MessagesWebSISPE.getString("formatos.fecha"), "errors.formato.fecha", "errors.requerido", "Fecha inicial");
						validador.validateFecha(errores, "txtFechaFinal", this.getTxtFechaFinal(), true, MessagesWebSISPE.getString("formatos.fecha"), "errors.formato.fecha", "errors.requerido", "Fecha final");
						if (errores.size() > 1){
							this.opcionFechaBusqueda = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas");
						}
					}
					
					//validar que ingrese bien el n\u00FAmero de meses
					if(this.opcionFechaBusqueda != null && this.opcionFechaBusqueda.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))
							&& errores.isEmpty()){
						//si no hubieron errores
						validador.validateLong(errores, "numeroMeses", this.numeroMeses, true, 0, 999, "errors.formato.long", "N\u00FAmero de Meses");
					}
				}else{
					errores.add("cmbTipoFecha", new ActionMessage("errors.requerido", "Fecha"));
				}
			}
		}
		request.setAttribute("accionAbonos", "desplegar");
		return errores;
	}
	

	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param datos
	 */
	public static void realizarPaginacion(HttpServletRequest request, ReporteGeneralForm formulario, Collection datos, String tipoPaginacion)
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
	
			session.setAttribute(COL_PEDIDOS_PAGINADOS, Util.obtenerSubCollection(datos, inicio, inicio + rango > datos.size() ? datos.size() : inicio + rango));
		}else{
			formulario.setStart(null);
			formulario.setRange(null);
			formulario.setSize(null);
			session.removeAttribute(COL_PEDIDOS_PAGINADOS);
		}
	}
	
	/**
	 * 
	 * @return botonBuscar
	 */
	public String getBotonBuscar() {
		return botonBuscar;
	}
	
	/**
	 * 
	 * @param botonBuscar
	 */
	public void setBotonBuscar(String botonBuscar) {
		this.botonBuscar = botonBuscar;
	}
	
	/**
	 * 
	 * @return opcionFechaBusqueda
	 */
	public String getOpcionFechaBusqueda() {
		return opcionFechaBusqueda;
	}
	
	/**
	 * 
	 * @param opcionFechaBusqueda
	 */
	public void setOpcionFechaBusqueda(String opcionFechaBusqueda) {
		this.opcionFechaBusqueda = opcionFechaBusqueda;
	}
	
	/**
	 * 
	 * @return opcionValorBusqueda
	 */
	public String getOpcionValorBusqueda() {
		return opcionValorBusqueda;
	}
	
	/**
	 * 
	 * @param opcionValorBusqueda
	 */
	public void setOpcionValorBusqueda(String opcionValorBusqueda) {
		this.opcionValorBusqueda = opcionValorBusqueda;
	}
	
	/**
	 * 
	 * @return txtFechaFinal
	 */
	public String getTxtFechaFinal() {
		return txtFechaFinal;
	}
	
	/**
	 * 
	 * @param txtFechaFinal
	 */
	public void setTxtFechaFinal(String txtFechaFinal) {
		this.txtFechaFinal = txtFechaFinal;
	}
	
	/**
	 * 
	 * @return txtFechaInicial
	 */
	public String getTxtFechaInicial() {
		return txtFechaInicial;
	}
	
	/**
	 * 
	 * @param txtFechaInicial
	 */
	public void setTxtFechaInicial(String txtFechaInicial) {
		this.txtFechaInicial = txtFechaInicial;
	}
	
	/**
	 * 
	 * @return txtValorBusqueda
	 */
	public String getTxtValorBusqueda() {
		return txtValorBusqueda;
	}
	
	/**
	 * 
	 * @param txtValorBusqueda
	 */
	public void setTxtValorBusqueda(String txtValorBusqueda) {
		this.txtValorBusqueda = txtValorBusqueda;
	}

	/**
	 * 
	 * @return cmbCodigoLocal
	 */
	public String getCmbCodigoLocal() {
		return cmbCodigoLocal;
	}

	/**
	 * 
	 * @param cmbCodigoLocal
	 */
	public void setCmbCodigoLocal(String cmbCodigoLocal) {
		this.cmbCodigoLocal = cmbCodigoLocal;
	}

	/**
	 * 
	 * @return cmbCodigoEstadoPagado
	 */
	public String getCmbCodigoEstadoPagado() {
		return cmbCodigoEstadoPagado;
	}

	/**
	 * 
	 * @param cmbCodigoEstadoPagado
	 */
	public void setCmbCodigoEstadoPagado(String cmbCodigoEstadoPagado) {
		this.cmbCodigoEstadoPagado = cmbCodigoEstadoPagado;
	}


	/**
	 * @return el cmbOpcionEstadoPedido
	 */
	public String getCmbOpcionEstadoPedido() {
		return cmbOpcionEstadoPedido;
	}

	/**
	 * @param cmbOpcionEstadoPedido el cmbOpcionEstadoPedido a establecer
	 */
	public void setCmbOpcionEstadoPedido(String cmbOpcionEstadoPedido) {
		this.cmbOpcionEstadoPedido = cmbOpcionEstadoPedido;
	}

	/**
	 * @return el botonExportarReporte
	 */
	public String getBotonExportarReporte() {
		return botonExportarReporte;
	}

	/**
	 * @param botonExportarReporte el botonExportarReporte a establecer
	 */
	public void setBotonExportarReporte(String botonExportarReporte) {
		this.botonExportarReporte = botonExportarReporte;
	}

	/**
	 * @return el datos
	 */
	public Collection getDatos() {
		return datos;
	}

	/**
	 * @param datos el datos a establecer
	 */
	public void setDatos(Collection datos) {
		this.datos = datos;
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
	 * @return el cmbTipoFecha
	 */
	public String getCmbTipoFecha() {
		return cmbTipoFecha;
	}

	/**
	 * @param cmbTipoFecha el cmbTipoFecha a establecer
	 */
	public void setCmbTipoFecha(String cmbTipoFecha) {
		this.cmbTipoFecha = cmbTipoFecha;
	}

	/**
	 * @return el filtrarPorUsuario
	 */
	public boolean isFiltrarPorUsuario() {
		return filtrarPorUsuario;
	}

	/**
	 * @param filtrarPorUsuario el filtrarPorUsuario a establecer
	 */
	public void setFiltrarPorUsuario(boolean filtrarPorUsuario) {
		this.filtrarPorUsuario = filtrarPorUsuario;
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
	 * @return el opTipoAgrupacion
	 */
	public String getOpTipoAgrupacion() {
		return opTipoAgrupacion;
	}

	/**
	 * @param opTipoAgrupacion el opTipoAgrupacion a establecer
	 */
	public void setOpTipoAgrupacion(String opTipoAgrupacion) {
		this.opTipoAgrupacion = opTipoAgrupacion;
	}
	
	
	
}
