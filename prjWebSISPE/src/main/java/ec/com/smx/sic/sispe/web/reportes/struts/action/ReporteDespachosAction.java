/*
 * Creado el 11/04/2007
 *
 * ReporteDespachoReservacionAction.java
 */
package ec.com.smx.sic.sispe.web.reportes.struts.action;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * La clase <code>ReporteDespachosAction</code> permite realizar busquedas por 
 * distintos filtros para obtener el reporte de despacho reservacion.
 * 
 * @author jacalderon
 * @author mbraganza
 * @version 2.0 
 */
public class ReporteDespachosAction extends BaseAction {
	/**
	 * <p>
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
	 * </p>
	 * Este m\u00E9todo permite:
	 * <ul>
	 * 	<li>Registrar la Actualizaci\u00F3n de descuentos</li>
	 * </ul>
	 * 
	 * @param mapping 		El mapeo utilizado para seleccionar esta instancia
	 * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          				campos
	 * @param request 		La petici&oacue; que estamos procesando
	 * @param response 		La respuesta HTTP que se genera
	 * @return ActionForward	Los seguimiento de salida de las acciones
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ReporteGeneralForm formulario=(ReporteGeneralForm)form;

		String forward = "desplegar";
		//variable para guardar en sesion los valores ingresados en el formulario
		String [] variblesFormulario = new String[6];
		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteDespachos"));
		LogSISPE.getLog().info("Combo de fechas en despachos: {}", formulario.getCmbTipoFecha());

		//-------- se realiza la b\u00FAsqueda ----------
		if(formulario.getBotonBuscar() != null){
			try{
				VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();
				Collection vistaReporteGeneralDTOCol = null;
				Timestamp fechaDesde = null;
				Timestamp fechaHasta = null;

				//Codigo de la compa\u00F1ia actual
				vistaReporteGeneralBusqueda.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());

				//C\u00F3digo del local actual
				if (formulario.getCmbCodigoLocal() != null && formulario.getCmbCodigoLocal().equals("") == false) {
					vistaReporteGeneralBusqueda.getId().setCodigoAreaTrabajo(new Integer(formulario.getCmbCodigoLocal()));
					variblesFormulario[2] = formulario.getCmbCodigoLocal();
				}else	if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
					vistaReporteGeneralBusqueda.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
					session.setAttribute("ec.com.smx.sic.sispe.codigoLocalResponsable", vistaReporteGeneralBusqueda.getId().getCodigoAreaTrabajo());
				}

				//Estado del pedido RES, EPR, PRO, DES
				vistaReporteGeneralBusqueda.getId().setCodigoEstado(
						new StringBuilder()
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
						.toString());

				vistaReporteGeneralBusqueda.setEstadoActual(true);

				//Codigo del estado del pago
				/*vistaReporteGeneralBusqueda.setCodigoEstadoPagado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente")
					.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente")));*/

				//Articulos del pedido que se envian a bodega
				vistaReporteGeneralBusqueda.setReservarBodegaSIC(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
				vistaReporteGeneralBusqueda.setNpCantidadDespacho(0L);

				//si el filtro no est\u00E1 vac\u00EDo
				if(formulario.getTxtValorBusqueda() != null && !formulario.getTxtValorBusqueda().trim().equals("")){
					//Buscar por codigo de pedido
					if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"))){
						vistaReporteGeneralBusqueda.getId().setCodigoPedido(formulario.getTxtValorBusqueda().trim());
					}
	
					//Buscar por codigo de reserva
					if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroReserva"))){
						vistaReporteGeneralBusqueda.setLlaveContratoPOS(formulario.getTxtValorBusqueda().trim());
					}
					
					//Buscar por codigo de articulo
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.codigoArticulo"))){
						//vistaReporteGeneralBusqueda.getId().setCodigoArticulo(formulario.getTxtValorBusqueda());
						vistaReporteGeneralBusqueda.setCodigoBarras(formulario.getTxtValorBusqueda().trim());
					}
	
					//Cedula del contacto
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroCedula"))){
						vistaReporteGeneralBusqueda.setCedulaContacto(formulario.getTxtValorBusqueda().trim());
					} 
	
					//Nombre del contacto
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreContacto"))){
						vistaReporteGeneralBusqueda.setNombreContacto(formulario.getTxtValorBusqueda().trim());
					}
				}
				
				//Fechas
				if (!formulario.getCmbTipoFecha().equals("")){
					//Buscar por rangos de fecha
					if (formulario.getOpcionFechaBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
						fechaDesde = new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaInicial(), 0, 0, 0));
						fechaHasta = new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaFinal(), 23, 59, 59));
						variblesFormulario[4] = formulario.getTxtFechaInicial();
						variblesFormulario[5] = formulario.getTxtFechaFinal();
					}else if(formulario.getOpcionFechaBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
						//se obtienen los meses
						int meses = Integer.parseInt(formulario.getNumeroMeses());
						fechaDesde = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
						variblesFormulario[4] = formulario.getTxtFechaInicial();
						variblesFormulario[5] = null;
					}

					variblesFormulario[3] = formulario.getCmbTipoFecha();

					//Buscar por fechas de despacho
					if (formulario.getCmbTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaDespacho"))){
						vistaReporteGeneralBusqueda.setFechaDespachoDesde(fechaDesde);
						vistaReporteGeneralBusqueda.setFechaDespachoHasta(fechaHasta);
					} 
					//Buscar por fechas de entrega
					else {
						vistaReporteGeneralBusqueda.setFechaEntregaDesde(fechaDesde);
						vistaReporteGeneralBusqueda.setFechaEntregaHasta(fechaHasta);
					}
				}

				//Id del usuario que ingresa al sistema
				vistaReporteGeneralBusqueda.setIdUsuario(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());

				//filtrar por usuario - clasificaci\u00F3n
				if (formulario.isFiltrarPorUsuario()){
					vistaReporteGeneralBusqueda.setFiltrarPorUsuarioClasificacion(Boolean.TRUE);
				}
				session.setAttribute("ec.com.smx.sic.sispe.filtrarPorUsuario", formulario.isFiltrarPorUsuario());

				vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.despachos"));

				//Consulta en la base de datos
				vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);
				if(vistaReporteGeneralDTOCol.isEmpty()){
					infos.add("infoReporteVentas", new ActionMessage("info.busqueda.sinResultados"));
				}
				
				//se realiza la paginaci\u00F3n
				ReporteGeneralForm.realizarPaginacion(request, formulario, vistaReporteGeneralDTOCol, "parametro.paginacionReporteDespacho.rango");
				session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
				
				//session.setAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol2", vistaReporteGeneralDTOCol);
				session.setAttribute("ec.com.smx.sic.sispe.buscar", variblesFormulario);

			}catch(Exception e){
				errors.add("errorBuscar", new ActionMessage("errors.busqueda", ""));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}

		//paginacion
		else if(request.getParameter("range")!=null || request.getParameter("start")!=null)
		{
			//se realiza la paginaci\u00F3n
			ReporteGeneralForm.realizarPaginacion(request, formulario, (Collection)session.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS), "parametro.paginacionReporteDespacho.rango");
			
			/*
			boolean filtrarPorUsuario = false;
			LogSISPE.getLog().info("ENTRO A LA PAGINACION");
			LogSISPE.getLog().info("range: " + request.getParameter("range"));
			LogSISPE.getLog().info("start: " + request.getParameter("start"));
			Collection datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol2");
			if(datos!=null){
				int size= datos.size();
				int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
				int start= Integer.parseInt(request.getParameter("start"));
				formulario.setStart(String.valueOf(start));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(size));

				Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
				session.setAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado",datosSub);
				formulario.setOpcionValorBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
				if (session.getAttribute("ec.com.smx.sic.sispe.filtrarPorUsuario") != null){
					filtrarPorUsuario = ((Boolean) session.getAttribute("ec.com.smx.sic.sispe.filtrarPorUsuario")).booleanValue();
				}
				formulario.setFiltrarPorUsuario(filtrarPorUsuario);
				LogSISPE.getLog().info("vistaReporteGeneralDTOCol2: " + datosSub.size());
			}
			*/
		}
		else if (formulario.getCmbTipoFecha() != null && formulario.getCmbTipoFecha().equals("") == false){
			request.setAttribute("ec.com.smx.sic.sispe.verFiltrosFechas", "");
			formulario.setOpcionFechaBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
		}
		/*
		//filtra por local
		else if(request.getParameter("filtroLocal")!=null){
			LogSISPE.getLog().info("entro a filtrar por local: " + request.getParameter("filtroLocal"));
			HashMap local=new HashMap(); 
			local.put("id.codigoLocal",new Integer(request.getParameter("filtroLocal")));
			Collection vistaReporteGeneralDTOCol=(Collection)session.getAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol");
			Collection vistaReporteGeneralDTOCol1=ConverterUtil.buscarObjetosEnColeccion(vistaReporteGeneralDTOCol,local);
			LogSISPE.getLog().info("numero de registros: " +vistaReporteGeneralDTOCol1);
			session.setAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol2",vistaReporteGeneralDTOCol1);
			//paginacionInicial(session,formulario);
		}
		//filtra por pedido
		else if(request.getParameter("filtroPedido")!=null){
			LogSISPE.getLog().info("entro a filtrar por pedido");
			HashMap pedido=new HashMap();
			pedido.put("id.codigoPedido",request.getParameter("filtroPedido"));
			Collection vistaReporteGeneralDTOCol=(Collection)session.getAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol");
			Collection vistaReporteGeneralDTOCol1=ConverterUtil.buscarObjetosEnColeccion(vistaReporteGeneralDTOCol,pedido);
			LogSISPE.getLog().info("numero de registros: " +vistaReporteGeneralDTOCol1);
			session.setAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol2",vistaReporteGeneralDTOCol1);
			//paginacionInicial(session,formulario);
		}
		//deshacer filtro
		else if(request.getParameter("deshacerFiltro")!=null){
			LogSISPE.getLog().info("entro a deshacer filtro");
			session.setAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol2",session.getAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol"));
			//paginacionInicial(session,formulario);
		}
		*/
		else{
			LogSISPE.getLog().info("entra al segmento por omisi\u00F3n");
			//se eliminan las posibles variables y formularios de sesi\u00F3n
			SessionManagerSISPE.removeVarSession(request);

			//se verifica la entidad responsable
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
				//se obtienen los locales por ciudad
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			}
			formulario.setOpcionValorBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
			formulario.setFiltrarPorUsuario(true);
			session.setAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo"), MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.despachos"));
		}
		
		saveInfos(request, infos);
		saveErrors(request,errors);
		LogSISPE.getLog().info("salida: {}", forward);
		return mapping.findForward(forward);
	}

	/**
	 * 
	 * @param fechaActual
	 * @param hora
	 * @param minuto
	 * @param segundo
	 * @return
	 */
	private long obtenerMilisegundosPorFecha(String fechaActual, int hora, int minuto, int segundo){
		GregorianCalendar hoy = new GregorianCalendar();

		if (fechaActual != null){
			hoy.setTime(ConverterUtil.parseStringToDate(fechaActual));
		}
		hoy.set(Calendar.HOUR_OF_DAY, hora);
		hoy.set(Calendar.MINUTE, minuto);
		hoy.set(Calendar.SECOND, segundo);

		return hoy.getTimeInMillis();
	}
}
