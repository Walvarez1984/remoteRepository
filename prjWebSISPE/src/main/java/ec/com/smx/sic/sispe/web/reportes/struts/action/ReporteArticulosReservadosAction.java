package ec.com.smx.sic.sispe.web.reportes.struts.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

@SuppressWarnings("unchecked")
public class ReporteArticulosReservadosAction extends BaseAction{
	/**
	 * Clase ofrece: 
	 * oandino
	 * 12:54:30
	 * version 0.1
	 *  	
	 */
	private static final String VAR_SESSION_DATOS_ORIGINALES = "ec.com.smx.sic.sispe.datos.originales";
	private static final String VAR_SESSION_PLANTILLA_BUSQUEDA = "ec.com.smx.sic.sispe.plantilla.busqueda";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("Se ingresa a ReporteArticulosReservadosAction.java...");

		// Variable para direccionamiento
		String salida = "desplegar";		

		// Instancia de formulario
		ListadoPedidosForm formulario = (ListadoPedidosForm) form;

		// Variables de manejo de session y mensajes
		HttpSession session = request.getSession();
		ActionMessages errores = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		//se obtienen el estados activo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);

		//Proceso de b\u00FAsqueda
		if(request.getParameter("buscar") != null){
			LogSISPE.getLog().info("Se procede a realizar la b\u00FAsqueda en base a los par\u00E1metros ingresados...");
			//Instancia de objeto VistaReporteGeneralDTO
			VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();

			// Se setean campos espec\u00EDficos para este caso
			vistaReporteGeneralBusqueda.setEstadoActual(true);			
			vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.articulosReservados"));					

			// Se obtienen las variables de estados 
			String estadoPedRes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
			String estadoPedEnp = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
			String estadoPedPro = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
			String estadoPedDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
			String estadoTotal = estadoPedRes+","+estadoPedEnp+","+estadoPedPro+","+estadoPedDes;

			vistaReporteGeneralBusqueda.getId().setCodigoEstado(estadoTotal);

			if(formulario.getCodigoLocal()!=null && !formulario.getCodigoLocal().equals(""))
				vistaReporteGeneralBusqueda.setCodigoLocalEntrega(Integer.valueOf(formulario.getCodigoLocal()));

			// Se llama a m\u00E9todo de construcci\u00F3n de plantilla de consulta
			vistaReporteGeneralBusqueda = WebSISPEUtil.construirConsultaReporteGeneral(request, formulario, vistaReporteGeneralBusqueda);

			// Se guarda en session la plantilla de b\u00FAsqueda
			session.setAttribute(VAR_SESSION_PLANTILLA_BUSQUEDA, vistaReporteGeneralBusqueda);

			// Collection para almacenar resultados de b\u00FAsqueda
			Collection<VistaReporteGeneralDTO> vistaReporteGeneralDTOCol = null;
			Collection<VistaReporteGeneralDTO> vistaReporteGeneralDTOColActual = null;

			// Se llama a m\u00E9todo de obtenci\u00F3n de datos de reportes gen\u00E9ricos
			vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);

			//consultar el parametro que indica de donde se consulta el alcance de articulos
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.estado.consultar.alcance", request);
			//0-SIC, 1-MAX
			int estadoConsultarAlcance = Integer.parseInt(parametroDTO.getValorParametro());
			
			if(CollectionUtils.isNotEmpty(vistaReporteGeneralDTOCol)){
				try {
					//Se llama al m\u00E9todo de obtenci\u00F3n de stock
					if(formulario.getOpStockArticuloReservado() != null && formulario.getOpStockArticuloReservado().equals(estadoActivo)){
						LogSISPE.getLog().info("Estado Activo");
						vistaReporteGeneralDTOColActual = UtilesSISPE.procesarVistaReporteGeneralParaObtenerStock(vistaReporteGeneralDTOCol,true, estadoConsultarAlcance);
					}else{
						LogSISPE.getLog().info("Estado Inactivo");
						vistaReporteGeneralDTOColActual = UtilesSISPE.procesarVistaReporteGeneralParaObtenerStock(vistaReporteGeneralDTOCol,false, estadoConsultarAlcance);
					}	
				}catch (Exception e){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
					errores.add("SISPEException",new ActionMessage("errors.SISPEException",e.getMessage()));
				}
			}else{
				//se muestra el mensaje porque no existen datos
				infos.add("listaVacia", new ActionMessage("message.listaVacia", "art\u00EDculos reservados"));
			}
			
			if(vistaReporteGeneralDTOColActual!=null&&!vistaReporteGeneralDTOColActual.isEmpty()){
				// Proceso de paginaci\u00F3n ---------------------------------------------------------------------------------------------	    		    	
				// Se paginan los datos originales y se suben en session
				Collection datosPagina = formulario.paginarDatos(vistaReporteGeneralDTOColActual, 0, vistaReporteGeneralDTOColActual.size(),false);
				session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, datosPagina);
			}
			
			//Se guarda en session la colecci\u00F3n original para poder paginar resultados
			session.setAttribute(VAR_SESSION_DATOS_ORIGINALES, vistaReporteGeneralDTOColActual);
			
		}
		//	Paginaci\u00F3n de datos
		else if(request.getParameter("range")!=null || request.getParameter("start")!=null){
			LogSISPE.getLog().info("Se procede a paginar en ReporteEntregasAction.java...");

			// Se obtiene la colecci\u00F3n original de datos
			Collection datosPaginadosOriginales = (Collection)session.getAttribute(VAR_SESSION_DATOS_ORIGINALES);			

			// Se cargan los nuevos datos paginados en base al nuevo \u00EDndice
			Collection datosPaginadosOriginales2 = formulario.paginarDatos(datosPaginadosOriginales, Integer.valueOf(request.getParameter("start")), datosPaginadosOriginales.size(), false);

			session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, datosPaginadosOriginales2);
		}	
		// Presentaci\u00F3n de reporte en Excel
		else if(peticion!=null && peticion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel"))){
			LogSISPE.getLog().info("Se procede a presentar el reporte en formato de hoja de c\u00E1lculo...");

			if(session.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS)!=null){
				//Obtengo datos de plantilla de b\u00FAsqueda
				/*VistaReporteGeneralDTO plantillaBusquedaOriginal = (VistaReporteGeneralDTO)session.getAttribute(VAR_SESSION_PLANTILLA_BUSQUEDA);

				// Seteo el nuevo tipo de agrupaci\u00F3n
				plantillaBusquedaOriginal.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.articulosReservados"));

				// Colecci\u00F3n creada para guardar datos de consulta con nueva agrupaci\u00F3n
				Collection vistaReporteGeneralDTOCol = null;

				// Se llama al m\u00E9todo de obtenci\u00F3n de datos
				vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(plantillaBusquedaOriginal);

				try {
					//Se llama al m\u00E9todo de obtenci\u00F3n de stock
					UtilesSISPE.procesarVistaReporteGeneralParaObtenerStock(vistaReporteGeneralDTOCol);
				}catch (Exception e){				
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}*/

				// Se procede a cargar reportes
				//if(vistaReporteGeneralDTOCol!=null){
					//session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
					request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("articulosReservados", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));							
					// Se direcciona a reporteEntregasExcel.jsp
					salida = "reporteArticulosReservadosXLS";
				//}
			}else{
				// Se muestra mensaje de error
				errores = new ActionMessages();
				errores.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.sinDatos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipo.excel.reportesExcel")));

				salida = "desplegar";
			}

		}
		// Ingreso por primera vez
		else{
			LogSISPE.getLog().info("Se accede por primera vea a ReporteArticulosReservadosAction...");

			//Se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
			SessionManagerSISPE.removeVarSession(request);      	

			// Setear variables que identifican al tipo de proceso
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteArticulosReservados"));	  
			session.setAttribute(SessionManagerSISPE.ACCION_SECCION_BUSQUEDA, "ok");
			session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");

			// Se verifica si la entidad responsable no es local, para cargar o no el combo de locales
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");    	
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
				//Se obtienen los locales por ciudad
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);	      			  					  			
			}
		}
		
		//se guardan todos los mensajes generados 
		saveErrors(request, errores);
		saveInfos(request, infos);

		LogSISPE.getLog().info("Se direcciona en base a : {}",salida);
		return mapping.findForward(salida);
	}

}

