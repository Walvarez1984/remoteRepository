package ec.com.smx.sic.sispe.web.reportes.struts.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleEstadoPedidoAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

@SuppressWarnings("unchecked")
public class ReporteEntregasAction extends BaseAction{
	/**
	 * Clase ofrece: 
	 * oandino
	 * 16:02:17
	 * version 0.1
	 *  	
	 */
	// Variable que almacena el valor del registro padre/hijo elegido
	public static final String SESSION_VAR_INDICES_VRG = "ec.com.smx.sic.sispe.indiceVRG";
	
	private static final String ACEPTAR_REPORTE = "aceptarReporte";
	
	private final String REPORTE_1="ec.com.smx.sic.sispe.entrega.tipoReporte.artPed";
	private final String REPORTE_2="ec.com.smx.sic.sispe.entrega.tipoReporte.pedArt"; 
	private final String REPORTE_3="ec.com.smx.sic.sispe.entrega.tipoReporte.fechas";
	
	private static final String VAR_SESSION_PLANTILLA_BUSQUEDA = "ec.com.smx.sic.sispe.plantilla.busqueda";
	
	private static final String VAR_SESSION_DATOS_ORIGINALES = "ec.com.smx.sic.sispe.datos.originales";

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("Se ingresa a ReporteEntregasAction.java...");
		
		// Variable para direccionamiento
		String salida = "desplegar";
		String tipoEntidad = "";
		
		// Instancia de formulario
		ListadoPedidosForm formulario = (ListadoPedidosForm) form;
		
		// Variables de manejo de session y mensajes
		HttpSession session = request.getSession();
		ActionMessages errores = new ActionMessages();
		ActionMessages infos = null;
		
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);
			
		try
	    {
	
		//Se realiza el proceso de b\u00FAsqueda
		if(request.getParameter("buscar") != null){
			
			LogSISPE.getLog().info("Se ha seleccionado buscar...");								
					 						
			//Instancia de objeto VistaReporteGeneralDTO
			VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();
			
			// Se setean campos espec\u00EDficos para este caso
			vistaReporteGeneralBusqueda.setEstadoActual(true);			
			vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.entregas"));			
			vistaReporteGeneralBusqueda.setNpCantidadDespacho(1L);								
			
			// Se obtienen las variables de estados 
			String estadoPedRes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
			String estadoPedEnp = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
			String estadoPedPro = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
			String estadoPedDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
			String estadoTotal = estadoPedRes+","+estadoPedEnp+","+estadoPedPro+","+estadoPedDes;
			
			vistaReporteGeneralBusqueda.getId().setCodigoEstado(estadoTotal);
					
			//Validaci\u00F3n de codigoLocalEntrega para tipos de rol---------------------------------------
			LogSISPE.getLog().info("Tipo de entidad: {}",SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable());
			tipoEntidad = SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable();
			if(tipoEntidad != null && tipoEntidad.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				LogSISPE.getLog().info("Se procede a buscar en base a LOC como entidad responsable...");
				vistaReporteGeneralBusqueda.setCodigoLocalEntrega(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			}else{
				if(formulario.getOpEntidadResponsable()!=null){
					vistaReporteGeneralBusqueda.setNpFiltrarPorCodigosCD(Boolean.TRUE);
					vistaReporteGeneralBusqueda.setTipoEntrega(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"));
				}else if(formulario.getCodigoLocal()!=null && !formulario.getCodigoLocal().equals("")){
					LogSISPE.getLog().info("Se establece {} para c\u00F3digo local...",formulario.getCodigoLocal());
					vistaReporteGeneralBusqueda.setCodigoLocalEntrega(Integer.valueOf(formulario.getCodigoLocal()));
				}				
			}
			//----------------------------------------------------------------------------------------
			
			// Se llama a m\u00E9todo de construcci\u00F3n de plantilla de consulta
			vistaReporteGeneralBusqueda = WebSISPEUtil.construirConsultaReporteGeneral(request, formulario, vistaReporteGeneralBusqueda);
			
			// Se guarda en session la plantilla de b\u00FAsqueda
			session.setAttribute(VAR_SESSION_PLANTILLA_BUSQUEDA, vistaReporteGeneralBusqueda);
			
			// Collection para almacenar resultados de b\u00FAsqueda
			Collection vistaReporteGeneralDTOCol = null;
			
			// Se llama a m\u00E9todo de obtenci\u00F3n de datos de reportes gen\u00E9ricos
	    	vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);
	    	
	    	// Proceso de paginaci\u00F3n ---------------------------------------------------------------------------------------------
	    	// Se cargan en session los datos originales
	    	session.setAttribute(VAR_SESSION_DATOS_ORIGINALES, vistaReporteGeneralDTOCol);
	    	
	    	// Se paginan los datos originales y se suben en session
	    	Collection datosPagina = formulario.paginarDatos(vistaReporteGeneralDTOCol, 0, vistaReporteGeneralDTOCol.size(),false);
	    	session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, datosPagina);	    	
	    	// -------------------------------------------------------------------------------------------------------------------
	    	
	    	// Se almacena el resultado en session pra desplegarlo en reporteArticuloPedidos.jsp
	    	//session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
	    	
	    	LogSISPE.getLog().info("Tama\u00F1o de la colecci\u00F3n resultado: "+vistaReporteGeneralDTOCol.size());	    	
	    	
	      }		
		// Paginaci\u00F3n de datos
		else if(request.getParameter("range")!=null || request.getParameter("start")!=null){
			LogSISPE.getLog().info("Se procede a paginar en ReporteEntregasAction.java...");
			
			// Se obtiene la colecci\u00F3n original de datos
			Collection datosPaginadosOriginales = (Collection)session.getAttribute(VAR_SESSION_DATOS_ORIGINALES);			
			
			// Se cargan los nuevos datos paginados en base al nuevo \u00EDndice
			datosPaginadosOriginales = formulario.paginarDatos(datosPaginadosOriginales, Integer.valueOf(request.getParameter("start")), datosPaginadosOriginales.size(), false);
			session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, datosPaginadosOriginales);
		}	
		// Cerrar pop-up
		else if(peticion != null && peticion.equals("cerrarPopUp")){
			// Se procede a cerrar pop-up y entrar al caso por defecto
			salida = "desplegar";
		}
		// Se acepta cargar reporte desde pop-up
		else if(peticion != null && peticion.equals(ACEPTAR_REPORTE)){
			LogSISPE.getLog().info("Se ingresa a armar colecci\u00F3n con nuevos campos de agrupaci\u00F3n...");
			
			// Obtengo datos de plantilla de b\u00FAsqueda
			VistaReporteGeneralDTO plantillaBusquedaOriginal = (VistaReporteGeneralDTO)session.getAttribute(VAR_SESSION_PLANTILLA_BUSQUEDA);
			
			// Seteo el nuevo tipo de agrupaci\u00F3n
			plantillaBusquedaOriginal.setTipoReporte(formulario.getOpTipoAgrupacion());
			
			// Colecci\u00F3n creada para guardar datos de consulta con nueva agrupaci\u00F3n
			Collection vistaReporteGeneralDTOCol = null;
			
			// Se llama al m\u00E9todo de obtenci\u00F3n de datos
			vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(plantillaBusquedaOriginal);
			
			// Se procede a cargar reportes
			if(vistaReporteGeneralDTOCol!=null){
				
				//Elimino variables de control de reporte en excel
				session.removeAttribute("ec.com.sic.sispe.reporte.reporteEntrega1");
				session.removeAttribute("ec.com.sic.sispe.reporte.reporteEntrega2");
				session.removeAttribute("ec.com.sic.sispe.reporte.reporteEntrega3");
				
				session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("entregas", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reporteEntregas")));
				
				//Subo a sesi\u00F3n la variable correspondiente a la opci\u00F3n escogida
				if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_1))){
					//Agrupaci\u00F3n: articulo/pedido
					LogSISPE.getLog().info("escogio el reporte articulo/pedido");
					session.setAttribute("ec.com.sic.sispe.reporte.reporteEntrega1", "ok");
				}
				else if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_2))){
					//Agrupaci\u00F3n: pedido/articulo
					LogSISPE.getLog().info("escogio el reporte pedido/articulo");
					session.setAttribute("ec.com.sic.sispe.reporte.reporteEntrega2", "ok");
				}
				else if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_3))){
					//Agrupaci\u00F3n: fechas/direcci\u00F3n/art\u00EDculo
					LogSISPE.getLog().info("escogio el reporte adicional");
					session.setAttribute("ec.com.sic.sispe.reporte.reporteEntrega3", "ok");
				}
								
				// Se direcciona a reporteEntregasExcel.jsp
				salida = "reporteEntregasXLS";				
			}
		}
		
		else if(peticion!=null && peticion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel"))){
			
			//Se verifica si existen datos que exportar
			Collection resultadoReporteGeneral = (Collection)session.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS);
			
			if( resultadoReporteGeneral != null && resultadoReporteGeneral.isEmpty() == false){
				
				LogSISPE.getLog().info("Entra a crear popUp");
				
				//Se crea la ventana popUp
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Opciones de agrupaci\u00F3n");
				popUp.setMensajeVentana("Escoja una de las siguientes opciones para crear el reporte: ");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setValorOK("realizarEnvioSinProcesando2('"+ACEPTAR_REPORTE+"');");
				popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
				popUp.setEtiquetaBotonOK("Aceptar");
				popUp.setEtiquetaBotonCANCEL("Cancelar");
				popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
				popUp.setContenidoVentana("/reportes/entregas/opcionesReporteEntregas.jsp");
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
				formulario.setOpTipoAgrupacion((String)session.getAttribute(REPORTE_1));
				//Se guarda en session la imagen del pop-up
				request.setAttribute(SessionManagerSISPE.POPUP, popUp);
			}
			else{		
				// Se muestra mensaje de error
				errores = new ActionMessages();
				errores.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.sinDatos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipo.excel.reportesExcel")));					
				salida = "desplegar";
			}
			
		}					
		// Se accede al detalle de un registro de pedido
		else if (request.getParameter("indiceVRG") != null){
			LogSISPE.getLog().info("Se ingresa al registro desde reporte de entregas");
			LogSISPE.getLog().info("N\u00FAmero de registro: "+request.getParameter("indice"));
									
			// Se obtiene el indice actual de registros 
			String parametroIndices = request.getParameter("indiceVRG");
			
			// Se almacena en session de forma p\u00FAblica el valor del indice total elegido
			session.setAttribute(SESSION_VAR_INDICES_VRG, parametroIndices);
			
			// Se obtiene el resultado de la separaci\u00F3n del \u00EDndice original
			String [] indiceGlobal = parametroIndices.split("_");
			
			LogSISPE.getLog().info("Elemento [0]: "+indiceGlobal[0]);
			LogSISPE.getLog().info("Elemento [1]: "+indiceGlobal[1]);
			
			// Se obtiene el registro por nivel
			List<VistaReporteGeneralDTO> vistaReporteGeneralDTOCol = (List<VistaReporteGeneralDTO>)session.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS);		
			List<VistaReporteGeneralDTO> vistaReporteGeneralDTOCol1 = (List<VistaReporteGeneralDTO>)vistaReporteGeneralDTOCol.get(Integer.parseInt(indiceGlobal[0])).getDetallesReporte();			
			VistaReporteGeneralDTO dto = vistaReporteGeneralDTOCol1.get(Integer.parseInt(indiceGlobal[1]));
			
			LogSISPE.getLog().info("C\u00F3digo del pedido elegido del \u00E1rbol jer\u00E1rquico: "+dto.getId().getCodigoPedido());
						
			//Se construye el objeto VistaPedidoDTO para realizar la consulta
			VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
			consultaVistaPedidoDTO.getId().setCodigoCompania(dto.getId().getCodigoCompania());
			consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(dto.getId().getCodigoAreaTrabajo());
			consultaVistaPedidoDTO.getId().setCodigoPedido(dto.getId().getCodigoPedido());
			consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

			//Primero se obtienen los datos del pedido
			Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);

			//Se obtiene el primer elemento
			if(colVistaPedidoDTO!=null && !colVistaPedidoDTO.isEmpty()){
				VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.iterator().next();
				//Se obtienen los detalles del primer elemento
				EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
				DetalleEstadoPedidoAction.obtenerEntregas(session,vistaPedidoDTO.getVistaDetallesPedidosReporte());
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				
				PaginaTab tabDetallePedido= new PaginaTab("detalleEstadoPedido", "deplegar", 0,335, request);
				Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
				Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
				tabDetallePedido.addTab(tabDetallePedidoComun);	
				tabDetallePedido.addTab(tabDetalleEntregas);
				beanSession.setPaginaTab(tabDetallePedido);
				
			}else{
				errores.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
			}
			
			// Se direccionada hacia detalleEstadoPedido.jsp
			salida = "detalles";

		}		
		// Se ingresa por primera vez
		else{
	    	LogSISPE.getLog().info("Ingreso a caso por omisi\u00F3n en ReporteEntregasAction.java...");
	    	
	    	//Se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
	      	SessionManagerSISPE.removeVarSession(request);
	
	      	String estadoActivo = session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO).toString();
	      	LogSISPE.getLog().info("Estado: "+estadoActivo);
	      		     
	      	session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteEntregas"));	      	
	      	
	      	//Se inicializan los atributos del formulario
	      	formulario.setDatos(null);
	      	formulario.setEtiquetaFechas("Fecha de Estado");      	      
	      	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
	      	//formulario.setOpEntidadResponsable(estadoActivo);
	      	formulario.setOpEstadoActivo(estadoActivo);
	      	
	      	//Se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
	      	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
	      			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
	      	
	      	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
	      			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
	      	
	      	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");
	      		      	  		      
	      	// Se crea instancia de objeto tipo VistaReporteGeneralDTO
	      	VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();
	      	
	      	// Se setean campos espec\u00EDficos para este caso
	      	vistaReporteGeneralBusqueda.setEstadoActual(true);			
			vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.entregas"));			
			vistaReporteGeneralBusqueda.setNpCantidadDespacho(1L);					
			
			//Se obtienen las variables de estados 
			String estadoPedRes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
			String estadoPedEnp = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
			String estadoPedPro = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
			String estadoPedDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
			String estadoTotal = estadoPedRes+","+estadoPedEnp+","+estadoPedPro+","+estadoPedDes;
			
			vistaReporteGeneralBusqueda.getId().setCodigoEstado(estadoTotal);

			// Validaci\u00F3n de codigoLocalEntrega para tipos de rol-----------------------------------------------------------------------------
			LogSISPE.getLog().info("Tipo de entidad: "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable());
			tipoEntidad = SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable();
			if(tipoEntidad != null && tipoEntidad.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){				
				vistaReporteGeneralBusqueda.setCodigoLocalEntrega(99);				
				vistaReporteGeneralBusqueda.setTipoEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"));
			}
			else if(tipoEntidad!=null && tipoEntidad.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				vistaReporteGeneralBusqueda.setCodigoLocalEntrega(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			}
			else if(tipoEntidad!=null && tipoEntidad.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.sis"))){
				vistaReporteGeneralBusqueda.setCodigoLocalEntrega(null);
			}
			//--------------------------------------------------------------------------------------------------------------------------------
												      	
			// Se llama a m\u00E9todo de construcci\u00F3n de plantilla de consulta
	      	vistaReporteGeneralBusqueda = WebSISPEUtil.construirConsultaReporteGeneral(request, formulario, vistaReporteGeneralBusqueda);
	      	
	      	// Se guarda en session la plantilla de b\u00FAsqueda
			session.setAttribute(VAR_SESSION_PLANTILLA_BUSQUEDA, vistaReporteGeneralBusqueda);
			
			// Collection para contener resultados
			Collection vistaReporteGeneralDTOCol = null;			
	      	
			// Se llama a m\u00E9todo de obtenci\u00F3n de datos de reportes gen\u00E9ricos
	    	vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);
	    		    		    
	    	// Proceso de paginaci\u00F3n ----------------------------------------------------------------------------------------------
	    	// Se cargan en session los datos originales
	    	session.setAttribute(VAR_SESSION_DATOS_ORIGINALES, vistaReporteGeneralDTOCol);
	    	
	    	// Se paginana los datos originales y se cargan en session
	    	Collection datosPagina = formulario.paginarDatos(vistaReporteGeneralDTOCol, 0, vistaReporteGeneralDTOCol.size(),false);
	    	session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, datosPagina);
	    	// --------------------------------------------------------------------------------------------------------------------
	    		    	
	    	// Se almacena el resultado en session pra desplegarlo en reporteArticuloPedidos.jsp
	    	//session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
	    	
	    	LogSISPE.getLog().info("Tama\u00F1o de la colecci\u00F3n resultado: "+vistaReporteGeneralDTOCol.size());
	    		    		    
	    	// Se verifica si la entidad responsable no es local, para cargar o no el combo de locales
	      	String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
	    	
	      	if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
	      		//Se obtienen los locales por ciudad
	      		SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);	      			  					  			
	       	}
	    	
	    	// Subo a sesi\u00F3n las variables de los tipos de reporte
			session.setAttribute(REPORTE_1,					
					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.entregas"));
			session.setAttribute(REPORTE_2,
					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.entregasPedArt"));			
			session.setAttribute(REPORTE_3,
					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.entregasFechas"));			
	    	
		}
      			
		saveErrors(request, errores);
		saveInfos(request, infos);
		
		
	    } catch(Exception ex){
	      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
	      salida="errorGlobal";
	    }
	
	LogSISPE.getLog().info("Se direcciona en base a : "+salida);
	return mapping.findForward(salida);
	}
	
}
