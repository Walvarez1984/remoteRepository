package ec.com.smx.sic.sispe.web.reportes.struts.action;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.AbonoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * 
 * @author jcalderon
 * @author fmunoz
 */
public class ReporteVentasAction extends BaseAction {
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String salida = "desplegar";
		ReporteGeneralForm formulario = (ReporteGeneralForm) form;
		HttpSession sesion = request.getSession();
		ActionMessages errores = new ActionMessages();
		ActionMessages infos = null;
		
		sesion.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteVentasServicio"));
		//Mostrar el reporte seg\u00FAn los criterios de b\u00FAsqueda
		if (formulario.getBotonBuscar() != null){
			VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();
			VistaReporteGeneralDTO vistaReporteGeneralPlantilla = new VistaReporteGeneralDTO();
			Collection vistaReporteGeneralDTOCol = null;
			LogSISPE.getLog().info("Presiono el boton buscar de ReporteVentasAction->{}",formulario.getCmbOpcionEstadoPedido());
			//limpio la plantilla.
			sesion.removeAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA_GENERAL1);
			try{
				//Codigo de la compa\u00F1ia actual
				vistaReporteGeneralBusqueda.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				vistaReporteGeneralPlantilla.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());

				//C\u00F3digo del local actual
				if (formulario.getCmbCodigoLocal() != null && formulario.getCmbCodigoLocal().equals("") == false) {
					vistaReporteGeneralBusqueda.getId().setCodigoAreaTrabajo(new Integer(formulario.getCmbCodigoLocal()));
					vistaReporteGeneralPlantilla.getId().setCodigoAreaTrabajo(new Integer(formulario.getCmbCodigoLocal()));
				}else {
					if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
						vistaReporteGeneralBusqueda.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
						vistaReporteGeneralPlantilla.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
						sesion.setAttribute("ec.com.smx.sic.sispe.codigoLocalResponsable", vistaReporteGeneralBusqueda.getId().getCodigoAreaTrabajo());
					}
				}

				//Estado actual del pedido
				vistaReporteGeneralBusqueda.setEstadoActual(true);
				vistaReporteGeneralPlantilla.setEstadoActual(true);

				//codigoEstadoPagado
				if (!formulario.getCmbCodigoEstadoPagado().equals("")){
					vistaReporteGeneralBusqueda.setCodigoEstadoPagado(formulario.getCmbCodigoEstadoPagado());
					vistaReporteGeneralPlantilla.setCodigoEstadoPagado(formulario.getCmbCodigoEstadoPagado());
				}

				//se verifica si el texto no est\u00E1 vac\u00EDo
				if(!formulario.getTxtValorBusqueda().equals("")){
					
					//codigo de pedido
					if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"))){
						vistaReporteGeneralBusqueda.getId().setCodigoPedido(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.getId().setCodigoPedido(formulario.getTxtValorBusqueda().trim());
					}

					//n\u00FAmero de reservaci\u00F3n
					if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroReserva"))){
						vistaReporteGeneralBusqueda.setLlaveContratoPOS(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.setLlaveContratoPOS(formulario.getTxtValorBusqueda().trim());
					}
					
					//Cedula del contacto
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroCedula"))){
						vistaReporteGeneralBusqueda.setNumeroDocumentoPersona(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.setNumeroDocumentoPersona(formulario.getTxtValorBusqueda().trim());
					} 
	
					//Nombre del contacto
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreContacto"))){
						vistaReporteGeneralBusqueda.setNombrePersona(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.setNombrePersona(formulario.getTxtValorBusqueda().trim());
					}
					
					//Ruc empresa
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.rucEmpresa"))){
						vistaReporteGeneralBusqueda.setRucEmpresa(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.setRucEmpresa(formulario.getTxtValorBusqueda().trim());
					}
					
					//Nombre empresa
					else if (formulario.getOpcionValorBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreEmpresa"))){
						vistaReporteGeneralBusqueda.setNombreEmpresa(formulario.getTxtValorBusqueda().trim());
						vistaReporteGeneralPlantilla.setNombreEmpresa(formulario.getTxtValorBusqueda().trim());
					}
					
				}
				
				//Buscar las ventas del d\u00EDa actual
				if (formulario.getOpcionFechaBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
					vistaReporteGeneralBusqueda.setFechaInicialEstadoDesde(new Timestamp(this.obtenerMilisegundosPorFecha(null, 0, 0, 1)));
					vistaReporteGeneralBusqueda.setFechaInicialEstadoHasta(new Timestamp(this.obtenerMilisegundosPorFecha(null, 23, 59, 59)));
					//plantilla
					vistaReporteGeneralPlantilla.setFechaInicialEstadoDesde(new Timestamp(this.obtenerMilisegundosPorFecha(null, 0, 0, 1)));
					vistaReporteGeneralPlantilla.setFechaInicialEstadoHasta(new Timestamp(this.obtenerMilisegundosPorFecha(null, 23, 59, 59)));
				}

				//Buscar por rangos de fecha
				else if (formulario.getOpcionFechaBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
					vistaReporteGeneralBusqueda.setFechaInicialEstadoDesde(new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaInicial(), 0, 0, 1)));					
					vistaReporteGeneralBusqueda.setFechaInicialEstadoHasta(new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaFinal(), 23, 59, 59)));
					//Plantilla
					vistaReporteGeneralPlantilla.setFechaInicialEstadoDesde(new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaInicial(), 0, 0, 1)));
					vistaReporteGeneralPlantilla.setFechaInicialEstadoHasta(new Timestamp(this.obtenerMilisegundosPorFecha(formulario.getTxtFechaFinal(), 23, 59, 59)));
				}

				//busca todos desde un mes espec\u00EDfico
				else if(formulario.getOpcionFechaBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
					int meses = Integer.parseInt(formulario.getNumeroMeses());
					vistaReporteGeneralBusqueda.setFechaInicialEstadoDesde(new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0)));
					vistaReporteGeneralPlantilla.setFechaInicialEstadoDesde(new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0)));
				}
				
				//Se guarda en session la plantilla de b\u00FAsqueda
				sesion.setAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA_GENERAL1, vistaReporteGeneralPlantilla);
				
				//Opciones del combo estado del Pedido.
				if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.cotizadosReservados"))){
					LogSISPE.getLog().info("Opcion cotizados -> reservados");
					//filtra solo por estos estados
					vistaReporteGeneralBusqueda.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"));
					//parametro que me indica que opcion del combo es.
					vistaReporteGeneralBusqueda.setNpReporteVentasTodos("1");
				}
				if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.cotizadosSinReserva"))){
					LogSISPE.getLog().info("Opcion cotizados -> sin reservar");
					//filtra solo por estos estados
					vistaReporteGeneralBusqueda.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado").concat(",")
							.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado")).concat(",")
							.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
					//no realiza join con las entregas
					vistaReporteGeneralBusqueda.setNpNoObtenerEntregas("ok");
					vistaReporteGeneralBusqueda.setNpReporteVentasTodos("2");
				}
				if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.Todos"))){
					LogSISPE.getLog().info("Opcion Todos del combo Estado del Pedido");
					//excluye los siguientes estados
					vistaReporteGeneralBusqueda.setNpNoCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado").concat(",")
							.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.devolucion")));
					//parametro que me indica que debe realizar la relacion con entregas y sin entregas.
					vistaReporteGeneralBusqueda.setNpReporteVentasTodos("3");	
				}
				
				//Tipo de reporte
				vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.ventas"));
				
				//variable de ssesion que instancia reporte en jspGeneral.
				sesion.setAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo"), MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas"));
				
				//Consulta en la base de datos
				vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);
				
				//se realiza la paginaci\u00F3n
				ReporteGeneralForm.realizarPaginacion(request, formulario, vistaReporteGeneralDTOCol, "parametro.paginacionReporteVentas.rango");
				sesion.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);

				if (vistaReporteGeneralDTOCol.isEmpty()){
					infos = new ActionMessages();
					infos.add("infoReporteVentas", new ActionMessage("info.busqueda.sinResultados"));
				}
				
				sesion.setAttribute("ec.com.smx.sic.sispe.buscar","ok");
				request.setAttribute("accionAbonos", "desplegar");
				LogSISPE.getLog().info("******Valor de la variable de sesion->{}",sesion.getAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo")));
				//sesion.setAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo"), MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas.estado"));

			}catch(Exception e){
				errores.add("errorReporteVentas", new ActionMessage("errors.busqueda", ""));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}


		//desplegar - plegar abonos
		else if (request.getParameter("accionAbonos") != null){
			try{
				if (request.getParameter("accionAbonos").equals("desplegar")){
					int nPedido = Integer.parseInt(request.getParameter("nPedido"));
					List<VistaReporteGeneralDTO> pedidos = (List<VistaReporteGeneralDTO>) sesion.getAttribute(ReporteGeneralForm.COL_PEDIDOS_PAGINADOS);
					VistaReporteGeneralDTO pedidoActual = pedidos.get(nPedido);

					if (pedidoActual.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente")) == false 
							|| pedidoActual.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))
							|| (pedidoActual.getAbonosPedido() == null || pedidoActual.getAbonosPedido().size() == 0)){
						this.obtenerAbonosPedido(pedidoActual, request);
					}
					LogSISPE.getLog().info("abonos:{} " , pedidoActual.getAbonosPedido().size());
					sesion.setAttribute("ec.com.smx.sic.sispe.VistaReporteGeneralActual", pedidoActual);
					request.setAttribute("accionAbonos", "plegar");
				}else{
					request.setAttribute("accionAbonos", "desplegar");
				}
			}catch(Exception e){
				errores.add("errorReporteVentas", new ActionMessage("errors.busqueda", ""));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}

		//paginacion
		else if (request.getParameter("start") != null && request.getParameter("range")!=null){
			try{
				
				Collection datos = (Collection) sesion.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS);
				LogSISPE.getLog().error("---Tama\u00F1o de la coleccion--",datos.size());
				ReporteGeneralForm.realizarPaginacion(request, formulario, datos, "parametro.paginacionReporteVentas.rango");
			}catch(Exception e){
				errores.add("errorReporteVentas", new ActionMessage("errors.busqueda", ""));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}

		//primer acceso a la p\u00E1gina
		else{
			try{
				LogSISPE.getLog().info("Primer acceso a la p\u00E1gina de reportes1");
				//Borrar las variables de sesion
				SessionManagerSISPE.removeVarSession(request);

				//Verificar si el usuario logeado no es de un local para cargar los locales
				String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
				}

				//Marcar los radios de numero pedido y fecha de temporada
				formulario.setOpcionValorBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
				formulario.setOpcionFechaBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
				LogSISPE.getLog().info("Opcion fechas: {}", formulario.getOpcionFechaBusqueda());

				//Quitar las importaciones de archivos js para desplegar y plegar una secci\u00F3n
				sesion.setAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"), "");

				//Estados de pago
				EstadoSICDTO estadoSICDTO = new EstadoSICDTO();
				estadoSICDTO.getId().setCodigoEstado(new StringBuilder()
				.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))
				.append(",")
				.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))
				.append(",")
				.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente"))
				.append(",")
				.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago"))
				.toString());
				sesion.setAttribute("ec.com.smx.sic.estadosPago", SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(estadoSICDTO));				
				sesion.setAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo"), MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas"));
				
				ReporteGeneralForm.realizarPaginacion(request, formulario, null, "parametro.paginacionReporteVentas.rango");
				
			}catch(Exception e){
				errores.add("errorReporteVentas", new ActionMessage("errors.busqueda", ""));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}
		saveErrors(request, errores);
		saveInfos(request, infos);
		return mapping.findForward(salida);
	}

	/**
	 * 
	 * @param hora
	 * @param minuto
	 * @param segundo
	 * @return
	 */
	private long obtenerMilisegundosPorFecha(String fechaActual, int hora, int minuto, int segundo){
		GregorianCalendar hoy = new GregorianCalendar();

		if (fechaActual != null && !fechaActual.equals("")){
			hoy.setTime(ConverterUtil.parseStringToDate(fechaActual));
		}
		hoy.set(Calendar.HOUR_OF_DAY, hora);
		hoy.set(Calendar.MINUTE, minuto);
		hoy.set(Calendar.SECOND, segundo);

		return hoy.getTimeInMillis();
	}

	/**
	 * 
	 * @param pedidoActual
	 * @param request
	 * @throws Exception
	 */
	private void obtenerAbonosPedido(VistaReporteGeneralDTO pedidoActual, HttpServletRequest request) throws Exception{
		AbonoPedidoDTO abonoPedidoDTO = new AbonoPedidoDTO();

		abonoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		abonoPedidoDTO.getId().setCodigoAreaTrabajo(pedidoActual.getId().getCodigoAreaTrabajo());
		abonoPedidoDTO.getId().setCodigoPedido(pedidoActual.getId().getCodigoPedido());
		abonoPedidoDTO.getId().setSecuencialEstadoPedido(pedidoActual.getId().getSecuencialEstadoPedido());
		abonoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		
		pedidoActual.setAbonosPedido(SessionManagerSISPE.getServicioClienteServicio().transObtenerAbonoPedido(abonoPedidoDTO));
	}

}
