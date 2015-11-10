/*
 * KardexAction.java
 * Creado el 17/05/2007
 *
 */
package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corpv2.dto.CatalogoValorDTO;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.TransporteDTO;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioHoraCamionLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioMotivoMovimientoDTO;
import ec.com.smx.sic.sispe.dto.CalendarioTipoMovimientoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form.KardexForm;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.reportes.dto.HorasDTO;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite manejar el kardex.
 * </p>
 * 
 * <b>Utliza los m\u00E9todos</b>
 * <ul>
 * 
 * 	<li><b>transRegistrarCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO,TipoMovimiento,Motivo)</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transObtenerCalendarioMotivosMovimiento(calendarioMotivoMovimientoDTO)</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transAnularCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO,cantidadNueva.doubleValue(),true)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendarioTiposMovimientos(calendarioTipoMovimientoDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 * </ul>
 * 
 * Utliza forward para determinar la siguiente acci\u00F3n o pantalla.
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */

@SuppressWarnings("unchecked")
public class KardexAction extends BaseAction {
	/**
	 * <p>
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
	 * </p>
	 * 
	 * @param mapping 				El mapeo utilizado para seleccionar esta instancia
	 * @param form 						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacue; que estamos procesando
	 * @param response 				La respuesta HTTP que se genera
	 * @return ActionForward	Los seguimiento de salida de las acciones
	 * @throws Exception
	 */

	// Variables de control para presentar pantalla de detalle de pedido desde kardex	
	private static final String VISTA_PEDIDO = "ec.com.smx.sic.sispe.vistaPedido";
	// Variable que almacena el valor del registro padre/hijo elegido
	public static final String SESSION_VAR_INDICES_VRG = "ec.com.smx.sic.sispe.indiceVRG";
	// variable que indica si esta logueado la bodega o local
	public static final String KARDEXBOD = "ec.com.smx.kardex.bodega";
	public static final String KARDEXLOC = "ec.com.smx.kardex.local";
	public static final String NUEVOKARDEXCAMION ="ec.com.smx.calendarizacion.nuevokardexcamion";
	public static final String MOSTRARPOPUPKARDEXCAMION = "ec.com.smx.calendarizacion.mostrarpopupkardexcamion";
	public static final String TRANSPORTEDTOCOL = "ec.com.smx.calendarizacion.transportedtocol";
	public static final String HORASDIACOL = "ec.com.smx.calendarizacion.horasdiacol";
	
	public static final String HORASCAMIONRESP = "ec.com.smx.sic.sispe.horascamionresp";
	public static final String DETALLEHORASCAMION = "ec.com.smx.sic.sispe.detallehorascamion";
	public static final String SEPARADOR_COMA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();

		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages info = new ActionMessages();
		KardexForm formulario=(KardexForm)form;
		String forward = "kardex";

		PaginaTab paginaTab = ((PaginaTab)session.getAttribute(PaginaTab.PARAMETRO_SESSION)); //se obtiene la estructura de los tabs
		
		Integer codLocal = SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal();
		
		/*************************************************************************************************************
		 * 								KARDEX
		 *************************************************************************************************************/
		if(request.getParameter("abrirPopupNuevoCamion")!=null){
			LogSISPE.getLog().info("--- abrir Popup configuracion ---");
					
			session.setAttribute(MOSTRARPOPUPKARDEXCAMION, "ok");
			
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
			
			session.setAttribute(NUEVOKARDEXCAMION, "ok");
						
		}
		else if(request.getParameter("ocultarPopupCamion") != null){
			
			session.removeAttribute(MOSTRARPOPUPKARDEXCAMION);
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			session.removeAttribute(NUEVOKARDEXCAMION);
			
			//se limpian los datos ingresados
			formulario.setHoraSeleccionada("");
			formulario.setTransporteSeleccionado("");
			formulario.setNumCamiones("");
						
		}
		//Agrega un registro al kardex
		else if(request.getParameter("botonAgregarHorasCamion")!=null){
			try{
				if(formulario.getHoraSeleccionada()!=null && !formulario.getHoraSeleccionada().isEmpty() && formulario.getTransporteSeleccionado()!=null && !formulario.getTransporteSeleccionado().isEmpty() 
						&& formulario.getNumCamiones()!=null && !formulario.getNumCamiones().isEmpty()){
					
					CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
					
					if(calendarioDiaLocalDTO!=null){
						
						/*Collection<HorasDTO> horaCamionLocalDTOCol = (Collection<HorasDTO>)session.getAttribute(HORASCAMIONRESP);
						Collection<HorasDTO> listaHoras = (Collection<HorasDTO>)session.getAttribute(HORASDIACOL);*/
						CalendarioHoraCamionLocalDTO horaCamionLocalDTO = new CalendarioHoraCamionLocalDTO();
						
						TransporteDTO transporteDTO = new TransporteDTO();
						transporteDTO.getId().setCodigoCompania(calendarioDiaLocalDTO.getId().getCodigoCompania());
						transporteDTO.getId().setCodigoTransporte(new Integer(formulario.getTransporteSeleccionado()));
						
						transporteDTO = nombreTransporteSeleccionado(request, transporteDTO);
						
						String[] arraySeleccionHoras = formulario.getHoraSeleccionada().split(",");
						DateFormat sdf = new SimpleDateFormat("HH:mm");
						Date date = sdf.parse(arraySeleccionHoras[1]);
						
						horaCamionLocalDTO.setNpNumerocamiones(new Integer(formulario.getNumCamiones()));
						horaCamionLocalDTO.getId().setCodigoCompania(calendarioDiaLocalDTO.getId().getCodigoCompania());
						horaCamionLocalDTO.getId().setCodigoLocal(calendarioDiaLocalDTO.getId().getCodigoLocal());
						horaCamionLocalDTO.getId().setCodigoTransporte(new Integer(formulario.getTransporteSeleccionado()));
						horaCamionLocalDTO.getId().setFechaCalendarioDia(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
						horaCamionLocalDTO.getId().setHora(new Time(date.getTime()));
						
						horaCamionLocalDTO.setNpNombreTransporte(transporteDTO.getNombreTransporte());
						horaCamionLocalDTO.setNpCantidadBultos(transporteDTO.getCantidadBultos());
						
						
						
						//Metodo para agregar un detalle al calendario hora local y calendario camion hora local	
						calendarioDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
						SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioHorLocCam(calendarioDiaLocalDTO, horaCamionLocalDTO);
						
						/*HorasDTO horasDTO = new HorasDTO();
						horasDTO = buscarHoras(horaCamionLocalDTOCol, horaCamionLocalDTO.getId().getHora(),listaHoras);
						horasDTO.setNpDetalleCalCamHorLoc(new ArrayList<CalendarioHoraCamionLocalDTO>());
						horasDTO.getNpDetalleCalCamHorLoc().add(horaCamionLocalDTO);
						horaCamionLocalDTOCol.add(horasDTO);*/
						session.removeAttribute(HORASCAMIONRESP);
						cargardetalleCalCamHorLoc(session, errors);
						
						session.removeAttribute(MOSTRARPOPUPKARDEXCAMION);
						
						session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
						
						session.removeAttribute(NUEVOKARDEXCAMION);
						
						formulario.setHoraSeleccionada(null);
						formulario.setTransporteSeleccionado(null);
						formulario.setNumCamiones(null);
						
						messages.add("grabarkardex",new ActionMessage("messages.kardex.crear"));
						
					}
				}
				else{
					LogSISPE.getLog().error("campos", new ActionMessage("errors.campos.vacios"));
					errors.add("campos", new ActionMessage("errors.campos.vacios"));
				}
			}catch(Exception e){
				errors.add("grabarkardex",new ActionMessage("errors.kardex.crear",e.getStackTrace()));
			}
			
			
		}
		//Agrega un registro al kardex
		else if(request.getParameter("botonAgregarKardex")!=null){
			try{
				LogSISPE.getLog().info("entro a agregar kardex");
				//Cargo el detalle del calendario del dia seleccionado
				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
				calendarioDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria

				CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=new CalendarioDetalleDiaLocalDTO();
				calendarioDetalleDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria

				// Se crea una nueva instancia de horario
				Calendar horaActualSistema = new GregorianCalendar();

				// Se setea la hora del sistema para hora/minutos/segundos
				horaActualSistema.set(Calendar.HOUR_OF_DAY, horaActualSistema.get(Calendar.HOUR_OF_DAY));
				horaActualSistema.set(Calendar.MINUTE, horaActualSistema.get(Calendar.MINUTE));
				horaActualSistema.set(Calendar.SECOND, horaActualSistema.get(Calendar.SECOND));

				// Se setea en objeto la fecha del sistema al agregar nuevo registro de kardex
				calendarioDetalleDiaLocalDTO.setHoraDesde(new Time(horaActualSistema.getTime().getTime()));

				LogSISPE.getLog().info("Hora actual: {}",calendarioDetalleDiaLocalDTO.getHoraDesde());

				calendarioDetalleDiaLocalDTO.setConceptoMovimiento(formulario.getConcepto());
				calendarioDetalleDiaLocalDTO.setCantidadAlmacenamiento(new Double(formulario.getCantidad()));
				//a\u00F1ado la fecha actual de registro
				//calendarioDetalleDiaLocalDTO.getPedidoDTO().setPrimeraFechaEntrega(new Timestamp(System.currentTimeMillis()));
				//calendarioDetalleDiaLocalDTO.setNpCantidadAlmacenamiento(new Double(formulario.getCantidad()));
				LogSISPE.getLog().info("motivo: {}" , formulario.getMotivo());
				if(formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.reserva")))
					calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.reservado"));
				else if(formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.recibido"))
						|| formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.pendiente"))
						|| formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.recibidoAdicional")) 
						|| formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteIngreso")))
					calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ingresado"));
				else if(formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.despacho")) 
						|| formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgreso")))
					calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.egresado"));
				else
					calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ajusteCapacidad"));
				calendarioDetalleDiaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				calendarioDetalleDiaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				calendarioDetalleDiaLocalDTO.getId().setFechaCalendarioDia(calendarioDetalleDiaLocalDTO.getId().getFechaCalendarioDia());
				calendarioDetalleDiaLocalDTO.setCodigoMotivoMovimiento(formulario.getMotivo());
				calendarioDetalleDiaLocalDTO.setCodigoTipoMovimiento(formulario.getTipoMovimiento());								

				if(formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadAdicional"))&&((new Double(formulario.getCantidad())).doubleValue()>calendarioDiaLocalDTO.getCapacidadAdicional().doubleValue())){
					errors.add("errorCantidad",new ActionMessage("errors.cantidadKardex.capacidadAdicional"));
				}else if(formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadNormal"))&&((new Double(formulario.getCantidad())).doubleValue()>calendarioDiaLocalDTO.getCapacidadNormal().doubleValue())){
					errors.add("errorCantidad",new ActionMessage("errors.cantidadKardex.capacidadNormal"));
				}else if(formulario.getTipoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.egreso"))
						&& !formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadAdicional"))
						&& !formulario.getMotivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadNormal"))
						&&((new Double(formulario.getCantidad())).doubleValue()>calendarioDiaLocalDTO.getNpTotalIngresosReales().doubleValue())){
					errors.add("errorCantidad",new ActionMessage("errors.cantidadKardex"));
				}else{		 	
					//Metodo para agregar un detalle al kardex
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO);
					calendarioDetalleDiaLocalDTO.setConceptoMovimiento(formulario.getConcepto());
					//obtengo el kardex
					//seleccionDia(info,errors,session,request);
					ArrayList calendarioDetalleDiaLocalDTOCol=(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
					calendarioDetalleDiaLocalDTOCol.add(calendarioDetalleDiaLocalDTO);
					messages.add("grabarkardex",new ActionMessage("messages.kardex.crear"));
					//encero variables
					LogSISPE.getLog().info("va a encerar variables");
					formulario.setTipoMovimiento("");
					formulario.setMotivo("");
					request.setAttribute("tMovimiento","ok");
					session.removeAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol");
					//formulario.setConcepto(null);
					formulario.setCantidad(null);
				}
				//elimina sesion de codigo padre
				session.removeAttribute("ec.com.smx.calendarizacion.indicePadre");
			}catch(SISPEException e){
				errors.add("grabarkardex",new ActionMessage("errors.kardex.crear",e.getStackTrace()));
			}
		}
		//cargo los motivos a partir del tipo movimiento seleccionado
		else if(request.getParameter("tMovimiento")!=null){
			LogSISPE.getLog().info("entro a ver los motivos");
			//cargo la lista de motivo movimientos
			try{
				CalendarioMotivoMovimientoDTO calendarioMotivoMovimientoDTO=new CalendarioMotivoMovimientoDTO();
				calendarioMotivoMovimientoDTO.setTipoCalendario(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoCalendario.local"));
				calendarioMotivoMovimientoDTO.setCodigoTipoMovimiento(formulario.getTipoMovimiento());
				calendarioMotivoMovimientoDTO.setEstadoMotivoMovimiento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
				calendarioMotivoMovimientoDTO.setCodigoGrupoMovimiento(Long.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")));
				Collection calendarioMotivoMovimientoDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioMotivosMovimiento(calendarioMotivoMovimientoDTO);
				session.setAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol",calendarioMotivoMovimientoDTOCol);
				if(calendarioMotivoMovimientoDTOCol==null || calendarioMotivoMovimientoDTOCol.size()==0)
					info.add("tipoMovimiento",new ActionMessage("info.cargar.tipoMovimiento"));
			}catch(SISPEException e){
				errors.add("tipoMovimiento",new ActionMessage("errors.cargar.tipoMovimiento",e.getStackTrace()));
			}
		}
		//Editar cantidad
		else if(request.getParameter("editarCantidad")!=null){
			LogSISPE.getLog().info("entro a editar cantidad: {}" , request.getParameter("editarCantidad"));
			CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO");
			calendarioDetalleDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
			if(calendarioDetalleDiaLocalDTO!=null){
				LogSISPE.getLog().info("paso1: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
				session.setAttribute("ec.com.smx.calendarizacion.indicePadre",request.getParameter("editarCantidad"));
				//cargo la cantidad reservada anteriormente
				Double cantidadN=new Double(session.getAttribute("ec.com.smx.calendarizacion.cantidadReservada").toString());
				LogSISPE.getLog().info("cantidadNueva: {}" , cantidadN);
				LogSISPE.getLog().info("cantidadAnterior: {}" , calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento());
				if(cantidadN.doubleValue()==calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento().doubleValue()){
					calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ingresado"));
					//Cargo el detalle del calendario del dia seleccionado
					CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
					calendarioDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
					//Metodo para modificar un detalle al kardex
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO);
				}
				else if(cantidadN.doubleValue()<calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento().doubleValue()){
					request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","La cantidad ingresada es menor a la reservada. Desea Reservar la cantidad faltante?");
					//session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.reservarAdicional"));
				}
				else{
					request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","La cantidad ingresada es mayor a la reservada. Desea Aceptar la cantidad adicional?");
				}
				//elimino la sessio del ingreso del kardex desde la ventana modal(modificacion de cantidad de reserva)
				session.removeAttribute("ec.com.smx.calendarizacion.kardexIngresado");
			}
		}

		//si va ver la reserva de referencia
		else if(request.getParameter("verReserva")!=null){
			try
			{
				LogSISPE.getLog().info("entro a ver reserva");
				/*ArrayList calendarioDetalleDiaLocalDTOCol =(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
				 CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)calendarioDetalleDiaLocalDTOCol.get(new Integer(request.getParameter("editarCantidad")).intValue());
				 session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO",calendarioDetalleDiaLocalDTO);*/

				//se obtiene la reserva a la cual se hace referencia
				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
				CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=new CalendarioDetalleDiaLocalDTO();
				calendarioDetalleDiaLocalDTO.getId().setSecuencialDetalleCalendarioDia(new Integer(request.getParameter("verReserva")));
				calendarioDetalleDiaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				calendarioDetalleDiaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				calendarioDetalleDiaLocalDTO.getId().setFechaCalendarioDia(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
				calendarioDetalleDiaLocalDTO.setCalendarioMotivoMovimientoDTO(new CalendarioMotivoMovimientoDTO());
				calendarioDetalleDiaLocalDTO.setCalendarioTipoMovimientoDTO(new CalendarioTipoMovimientoDTO());
				//Metodo que obtiene el detalle del kardex
				Collection calendarioDetalleDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO);
				if(calendarioDetalleDiaLocalDTOCol==null || calendarioDetalleDiaLocalDTOCol.size()==0){
					info.add("kardex",new ActionMessage("info.cargar.kardexReferencia"));
				}
				else{
					CalendarioDetalleDiaLocalDTO calendarioDetallePadreDTO =(CalendarioDetalleDiaLocalDTO)calendarioDetalleDiaLocalDTOCol.iterator().next();
					LogSISPE.getLog().info("hora:{}", calendarioDetallePadreDTO.getHoraDesde());
					session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTO1",calendarioDetallePadreDTO);
					LogSISPE.getLog().info("calendarioDetalleDiaLocalDTOCol: {}", calendarioDetalleDiaLocalDTOCol.size());
				}

			}catch(SISPEException e){
				LogSISPE.getLog().info("error al cargar kardexReferencia");
				errors.add("kardex",new ActionMessage("errors.cargar.kardexRefencia",e.getStackTrace()));
			}
			session.setAttribute("ec.com.smx.calendarizacion.kardexIngresado","ok");
		}
		//cierra ventana
		else if(request.getParameter("cerrarVentana")!=null){
			LogSISPE.getLog().info("cierra ventana");
		}
		else if(request.getParameter("kardexSel")!=null){
			
			/*****asigancion del valor dependiendo del local logueado*****/
			if(codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))) ){
				session.setAttribute(KARDEXBOD, "ok");
				session.removeAttribute(KARDEXLOC);
			}
			else{
				session.setAttribute(KARDEXLOC, "ok");
				session.removeAttribute(KARDEXBOD);
			}
			
			/****Carga las horas del dia *******/
			session.setAttribute(HORASDIACOL, formulario.opcionesHoras(session));
			
			formulario.setHorasDia((Object[])session.getAttribute(KardexForm.HORASDIAS));
			
			session.removeAttribute(MOSTRARPOPUPKARDEXCAMION);
			
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			
			session.removeAttribute(NUEVOKARDEXCAMION);
			
			session.removeAttribute(HORASCAMIONRESP);
			
			if((Collection<TransporteDTO>)session.getAttribute(TRANSPORTEDTOCOL)==null){
				cargaTransporte(request, errors);
			}
			
			LogSISPE.getLog().info("entro al else de kardex");

			LogSISPE.getLog().info("kardexSel: {}",request.getParameter("kardexSel"));

			//cargo la lista de tipo movimientos
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioTipoMovimientoDTOCol")==null){
				try{
					CalendarioTipoMovimientoDTO calendarioTipoMovimientoDTO=new CalendarioTipoMovimientoDTO();
					calendarioTipoMovimientoDTO.setEstadoTipoMovimiento((MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")));
					Collection calendarioTipoMovimientoDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioTiposMovimientos(calendarioTipoMovimientoDTO);
					if(calendarioTipoMovimientoDTOCol==null || calendarioTipoMovimientoDTOCol.size()==0)
						info.add("motivo",new ActionMessage("info.cargar.motivo"));
					session.setAttribute("ec.com.smx.calendarizacion.calendarioTipoMovimientoDTOCol",calendarioTipoMovimientoDTOCol);
				}catch(SISPEException e){
					errors.add("motivo",new ActionMessage("errors.cargar.motivo",e.getStackTrace()));
				}
			}
			
			LogSISPE.getLog().info("sesion calendarioDiaLocalDTO: {}" , session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO"));
			//obtengo el kardex
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO")==null){
				LogSISPE.getLog().info("entra a obtener el calendarioDiaLocalDTO");
				//obtiene el detalle del dia seleccionado en el calendario
				Object[] calendarioDiaLocalDTOCol=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOCol[new Integer(request.getParameter("kardexSel")).intValue()];
				session.setAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO",calendarioDiaLocalDTO);				
			}
			//cargo el kardex
			seleccionDia(info,errors,session,request);		 	
			//subo a session el estado reservado
			session.setAttribute("ec.com.smx.calendarizacion.estadoMovimiento.reservado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.reservado"));
			//subo a session el tipo movimiento Ingreso
			session.setAttribute("ec.com.smx.calendarizacion.ingreso",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.ingreso"));
			//subo a session el tipo movimiento Egreso
			session.setAttribute("ec.com.smx.calendarizacion.egreso",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.egreso"));
			//subo a session el tipo movimiento Anulado
			session.setAttribute("ec.com.smx.calendarizacion.anulado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.anulado"));
			//elimino la session del ingreso del kardex desde la ventana modal(modificacion de cantidad de reserva)
			session.removeAttribute("ec.com.smx.calendarizacion.kardexIngresado");
			//elimino la session de referencias padres
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTO1");
			//subo a session los tipos de movimienos de los ajustes de CA y CN de ingresos y egresos
			session.setAttribute("ec.com.smx.calendarizacion.ajusteIngresoCapacidadNormal",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteIngresoCapacidadNormal"));
			session.setAttribute("ec.com.smx.calendarizacion.ajusteEgresoCapacidadNormal",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadNormal"));
			session.setAttribute("ec.com.smx.calendarizacion.ajusteIngresoCapacidadAdicional",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteIngresoCapacidadAdicional"));
			session.setAttribute("ec.com.smx.calendarizacion.ajusteEgresoCapacidadAdicional",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteEgresoCapacidadAdicional"));

			//Se procede a cargar tabs por primera vez
			LogSISPE.getLog().info("Se crean tabs al ingresar por primera vez...");

			// Se determinan las pesta\u00F1as
			Tab tabArticulos = new Tab("art\u00EDculos", "kardex", "/reportes/calendarizacion/reportePedidosArticulo.jsp", true);
			Tab tabKardex = new Tab("kardex", "kardex", "/calendarizacion/nuevaCalendarizacion/kardex.jsp", false);
			Tab tabHorasCamion = null;
			if((String)session.getAttribute(KARDEXBOD)!=null){
				tabHorasCamion = new Tab("camiones", "kardex", "/calendarizacion/nuevaCalendarizacion/horasCamion.jsp", false);
			}

			//Objeto para el manejo de los tabs
			PaginaTab tabsAdministracionPlantillas = new PaginaTab("kardex", "desplegar", 0, 440, request);
			tabsAdministracionPlantillas.addTab(tabArticulos);
			tabsAdministracionPlantillas.addTab(tabKardex);
			
			if((String)session.getAttribute(KARDEXBOD)!=null){
				tabsAdministracionPlantillas.addTab(tabHorasCamion);
			}

			// Se carga reporte de articulos/pedidos
			// Se definen los par\u00E1metros que permiten mostrar el resporte de articulos/pedidos al ingresar a una fecha del kardex		
			VistaReporteGeneralDTO vistaReporteGeneralBusqueda = new VistaReporteGeneralDTO();
			Collection vistaReporteGeneralDTOCol = null;

			// Se setean los valores para crear plantilla de b\u00FAsqueda
			vistaReporteGeneralBusqueda.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			vistaReporteGeneralBusqueda.setEstadoActual(true);			
			vistaReporteGeneralBusqueda.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.articuloPedido"));			
			vistaReporteGeneralBusqueda.setNpCantidadDespacho(1L);		
//			vistaReporteGeneralBusqueda.setNpNoCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado")); //Aqui se debe setera ek valor ANULADO del properties
			vistaReporteGeneralBusqueda.setNpNoCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado").concat(SEPARADOR_COMA)
					.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")).concat(SEPARADOR_COMA)
					.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado")));

			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");										

			Timestamp nuevaFechaMinDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), 0, 0, 0, 0, 0, 0));
			vistaReporteGeneralBusqueda.setFechaDespachoDesde(nuevaFechaMinDespacho);

			LogSISPE.getLog().info("Fecha despacho desde: {}",nuevaFechaMinDespacho);

			Timestamp nuevaFechaHastaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), 0, 0, 23, 59, 59, 0));
			vistaReporteGeneralBusqueda.setFechaDespachoHasta(nuevaFechaHastaDespacho);

			LogSISPE.getLog().info("Fecha despacho hasta: {}",nuevaFechaHastaDespacho);

			// Se setea el codigo local 
			LogSISPE.getLog().info("Cod. local: {}",SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());	    		    
			vistaReporteGeneralBusqueda.setCodigoLocalEntrega(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());

			//Se llama a m\u00E9todo de obtenci\u00F3n de datos de reportes gen\u00E9ricos
			vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralBusqueda);

			//Se almacena el resultado en session pra desplegarlo en reporteArticuloPedidos.jsp
			session.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);

			/*LogSISPE.getLog().info("Size: "+vistaReporteGeneralDTOCol.size());

			for (Iterator i = vistaReporteGeneralDTOCol.iterator(); i.hasNext();) {	
				VistaReporteGeneralDTO vr1 = (VistaReporteGeneralDTO) i.next();

				if(vr1.getDetallesReporte()!=null && !vr1.getDetallesReporte().isEmpty()){
					LogSISPE.getLog().info("Art: "+vr1.getId().getCodigoArticulo());
					LogSISPE.getLog().info("Size art: "+vr1.getDetallesReporte().size());
					for (Iterator j = vr1.getDetallesReporte().iterator(); j.hasNext();) {	

						VistaReporteGeneralDTO vr2 = (VistaReporteGeneralDTO) j.next();

						if(vr2.getDetallesReporte()!=null && !vr2.getDetallesReporte().isEmpty()){
							LogSISPE.getLog().info("Ped: "+vr2.getId().getCodigoPedido());            			
	            		}
					}
				}
			}*/
			// ------------------------------------------------------------------------------------------------------------

		}
		//Se accede al detalle de un registro de pedido
		else if (request.getParameter("indiceVRG") != null){
			LogSISPE.getLog().info("Se ingresa al registro desde reporte de articulos/pedidos");
			LogSISPE.getLog().info("N\u00FAmero de registro: {}",request.getParameter("indice"));

			// Se setea el tipo de acci\u00F3n para identificar el proceso
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteArticuloPedido"));

			// Se obtiene el indice actual de registros 
			String parametroIndices = request.getParameter("indiceVRG");

			// Se almacena en session de forma p\u00FAblica el valor del indice total elegido
			session.setAttribute(SESSION_VAR_INDICES_VRG, parametroIndices);

			// Se obtiene el resultado de la separaci\u00F3n del \u00EDndice original
			String [] indiceGlobal = parametroIndices.split("_");

			LogSISPE.getLog().info("Elemento [0]: {}",indiceGlobal[0]);
			LogSISPE.getLog().info("Elemento [1]: {}",indiceGlobal[1]);

			// Se obtiene el registro por nivel
			List<VistaReporteGeneralDTO> vistaReporteGeneralDTOCol = (List<VistaReporteGeneralDTO>)session.getAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS);		
			List<VistaReporteGeneralDTO> vistaReporteGeneralDTOCol1 = (List<VistaReporteGeneralDTO>)vistaReporteGeneralDTOCol.get(Integer.parseInt(indiceGlobal[0])).getDetallesReporte();			
			VistaReporteGeneralDTO dto = vistaReporteGeneralDTOCol1.get(Integer.parseInt(indiceGlobal[1]));

			LogSISPE.getLog().info("C\u00F3digo del pedido elegido del \u00E1rbol jer\u00E1rquico: {}",dto.getId().getCodigoPedido());

			//oandino
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
				//Se almacena el DTO obtenido en sesi\u00F3n.
				session.setAttribute(VISTA_PEDIDO,vistaPedidoDTO);
			}else{
				errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
			}

			// Se direccionada hacia detalleEstadoPedido.jsp
			forward = "desplegar";
			// ----------------------------------------------------------------------------
		}else if(paginaTab != null && paginaTab.comprobarSeleccionTab(request)){
			//Verifico que tab se ha seleccionado y procedo a identificar el tab activo
			if (paginaTab.esTabSeleccionado(0)){
				LogSISPE.getLog().info("Tab de art\u00EDculos");
			}
			else if (paginaTab.esTabSeleccionado(1)){
				LogSISPE.getLog().info("Tab de kardex");
				//se incializan algunas variables
				formulario.setTipoMovimiento("");
				formulario.setMotivo("");
				session.removeAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol");
			}
			else if (paginaTab.esTabSeleccionado(2)){
				LogSISPE.getLog().info("Tab de horas camion");
				cargardetalleCalCamHorLoc(session,errors);
				session.removeAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol");
			}
		}else{
			//elimino la session de cierre de ventana de las reservas
			session.removeAttribute("ec.com.smx.calendarizacion.cerrarVentana");
		}

		saveMessages(request,messages);
		saveInfos(request,info);
		saveErrors(request,errors);
		LogSISPE.getLog().info("salida kardex: {}" , forward);
		return mapping.findForward(forward);
	}

	/**
	 * Obtiene el kardex
	 * @param info
	 * @param errors
	 * @param session
	 * @param request
	 */
	private void seleccionDia(ActionMessages info,ActionMessages errors,HttpSession session,HttpServletRequest request) throws Exception{
		//Obtener kardex
		try{
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
			LogSISPE.getLog().info("calendarioDiaLocalDTO: {}", calendarioDiaLocalDTO.getEstadoCalendarioDia());

			CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=new CalendarioDetalleDiaLocalDTO();
			calendarioDetalleDiaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			calendarioDetalleDiaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			calendarioDetalleDiaLocalDTO.getId().setFechaCalendarioDia(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
			//calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.noAnulado"));
			calendarioDetalleDiaLocalDTO.setCalendarioMotivoMovimientoDTO(new CalendarioMotivoMovimientoDTO());
			calendarioDetalleDiaLocalDTO.setCalendarioTipoMovimientoDTO(new CalendarioTipoMovimientoDTO());
			calendarioDetalleDiaLocalDTO.setMovimientosPorAnulacion(new ArrayList<CalendarioDetalleDiaLocalDTO>()); //para que obtenga los movimientos relacionados
			//relaci\u00F3n con pedidoDTO
			calendarioDetalleDiaLocalDTO.setPedidoDTO(new PedidoDTO());

			//Metodo que obtiene el detalle del kardex
			//Collection calendarioDetalleDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO);
			Collection<CalendarioDetalleDiaLocalDTO> calendarioDetalleDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO);
			//Collection ordenada para el kardex
			//Collection calendarioDetalleDiaLocalDTOColOrdenada = new ArrayList();

			if(calendarioDetalleDiaLocalDTOCol==null || calendarioDetalleDiaLocalDTOCol.size()==0)
				info.add("kardex",new ActionMessage("info.cargar.kardex"));
			session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol",calendarioDetalleDiaLocalDTOCol);

			LogSISPE.getLog().info("llama funcion recursiva");
			LogSISPE.getLog().info("termino funcion recursiva");
		}catch(SISPEException e){
			LogSISPE.getLog().info("error al cargar kardex");
			errors.add("kardex",new ActionMessage("errors.cargar.kardex",e.getStackTrace()));
		}
	}
	
	/**
	 * Funci\u00F3n que carga los transporte
	 * @param request
	 * @param errors
	 */
	private static void cargaTransporte(HttpServletRequest request,ActionMessages errors){
		try{
						
			TransporteDTO transporteDTO = new TransporteDTO();
			transporteDTO.setTipoMercaderiaTransportada(new CatalogoValorDTO());
			transporteDTO.setTipoTransporte(new CatalogoValorDTO());
			
			Collection<TransporteDTO> transporteDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerTransporte(transporteDTO);
			
			request.getSession().setAttribute(TRANSPORTEDTOCOL, transporteDTOCol);
			LogSISPE.getLog().info("transporte: {}", transporteDTOCol.size());
		}catch(Exception e){
			errors.add("transporte",new ActionMessage("errors.cargarTransporte",e.getStackTrace()));
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void cargardetalleCalCamHorLoc(HttpSession session,ActionMessages errors){
		
		
		try {
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
			if(calendarioDiaLocalDTO!=null){
				
				Collection horaCamionLocalDTOs = SessionManagerSISPE.getServicioClienteServicio().transObtenerDetalleCalCamHorLoc(calendarioDiaLocalDTO);
				
				Collection<HorasDTO> horasDTOResultCol = new ArrayList<HorasDTO>();
				
				Collection<HorasDTO> listaHoras = (Collection<HorasDTO>)session.getAttribute(HORASDIACOL);
				
				if(CollectionUtils.isNotEmpty(horaCamionLocalDTOs)){
					for (Iterator iter = horaCamionLocalDTOs.iterator(); iter.hasNext();) {
						
						Object[] valores = (Object[])iter.next();
						
						CalendarioHoraCamionLocalDTO camionLocalDTO = new CalendarioHoraCamionLocalDTO();
						camionLocalDTO.setNpNumerocamiones(Integer.valueOf(String.valueOf(valores[0])));
						camionLocalDTO.getId().setCodigoCompania(calendarioDiaLocalDTO.getId().getCodigoCompania());
						camionLocalDTO.getId().setCodigoLocal(calendarioDiaLocalDTO.getId().getCodigoLocal());
						camionLocalDTO.getId().setCodigoTransporte(((Integer) valores[1]));
						camionLocalDTO.getId().setFechaCalendarioDia(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
						camionLocalDTO.getId().setHora((Time)valores[2]);
						camionLocalDTO.setNpNombreTransporte((String)valores[3]);
						camionLocalDTO.setNpCantidadBultos((Integer)valores[4]);
						
						HorasDTO horasDTO = new HorasDTO();
						horasDTO = buscarHoras(horasDTOResultCol, camionLocalDTO.getId().getHora(),listaHoras);
						
						if(CollectionUtils.isNotEmpty(horasDTO.getNpDetalleCalCamHorLoc())){
							horasDTO.getNpDetalleCalCamHorLoc().add(camionLocalDTO);
						}
						else{
							horasDTO.setNpDetalleCalCamHorLoc(new ArrayList<CalendarioHoraCamionLocalDTO>());
							horasDTO.getNpDetalleCalCamHorLoc().add(camionLocalDTO);
							horasDTOResultCol.add(horasDTO);
						}
						
					}
					
					session.setAttribute(HORASCAMIONRESP, horasDTOResultCol);
					
				}
			}
			else{
				errors.add("transporte",new ActionMessage("errors.cargarCamionesHora"));
			}
			
		} catch (SISPEException e) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("Error de aplicacion. "+e);
		} catch (Exception e) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("Error de aplicacion. "+e);
		}
		
	}
	
	
	private static HorasDTO buscarHoras(Collection<HorasDTO> horas,Time horaSel,Collection<HorasDTO> listaHoras){
		
		DateFormat sdf = new SimpleDateFormat("HH:mm");
		
		Date date;
		
		HorasDTO horasDTO = new HorasDTO();
		HorasDTO horasDTO2 = new HorasDTO();
		
		try {
			date = sdf.parse(String.valueOf(horaSel));
			Calendar calA = Calendar.getInstance();
			int valorB = Integer.valueOf(new SimpleDateFormat("HH").format(date.getTime()));
			calA.set(Calendar.HOUR_OF_DAY,valorB);
			calA.set(Calendar.MINUTE,59);
			
			String horaBuscar =  new SimpleDateFormat("HH:mm").format(date.getTime())+" - "+new SimpleDateFormat("HH:mm").format(calA.getTime());
			
			LogSISPE.getLog().info("valor de las horas a buscar "+horaBuscar);
			
			String [] horaSeleccionada = new String[1];
			
			horaSeleccionada[0]=horaBuscar;
			
			Collection<HorasDTO> val = seleccionHoras(horas,horaSeleccionada);
			Collection<HorasDTO> valLista = seleccionHoras(listaHoras,horaSeleccionada);
			if(val.size()>0){	
				if(valLista.size()>0){
					
					horasDTO = (HorasDTO)val.iterator().next();
					horasDTO2 = (HorasDTO)valLista.iterator().next();
					setearPosicionHora(horasDTO, horasDTO2);
				}
				return horasDTO;				
			}else{	
				if(valLista.size()>0){
					horasDTO2 = (HorasDTO)valLista.iterator().next();					
					horasDTO = horasDTO2;
					horasDTO.setNpDetalleCalCamHorLoc(null);
					setearPosicionHora(horasDTO, horasDTO2);
				}
				
				return horasDTO;
			}
		} catch (ParseException e) {
			// TODO Bloque catch generado autom\u00E1ticamente
//			e.printStackTrace();
			LogSISPE.getLog().error("No pudo recuperar la posicion de la coleccion de horas ",e);
		} catch (Exception e) {
			// TODO Bloque catch generado autom\u00E1ticamente
//			e.printStackTrace();
			LogSISPE.getLog().error("No pudo recuperar la posicion de la coleccion de horas ",e);
		}
		
		return null;
	}

	private static void setearPosicionHora(HorasDTO horasDTO, HorasDTO horasDTO2) {
		String posicion;
		String[] arraySeleccionHoras = horasDTO2.getSeleccion().split(",");
		posicion = arraySeleccionHoras[0];
		horasDTO.setNpPosicion(Integer.parseInt(posicion));
	}
	
	//devuelve una Collection<HorasDTO>
	public static Collection<HorasDTO> seleccionHoras(Collection<HorasDTO> listaOriginal, final String[] horaSel) throws Exception {
		Predicate predicate = new Predicate() {			
		public boolean evaluate(Object arg0) {
			HorasDTO item = (HorasDTO)arg0;		
			return (ArrayUtils.contains(horaSel, item.getHoras()));
		}
	  };
	  Collection<HorasDTO> listaResultante = new ArrayList<HorasDTO>();
	  CollectionUtils.select(listaOriginal, predicate, listaResultante);
	  return listaResultante;
	}
	
	private TransporteDTO nombreTransporteSeleccionado(HttpServletRequest request,TransporteDTO transporteDTOCon){
		
		Collection<TransporteDTO> transporteDTOCol = (Collection<TransporteDTO>)request.getSession().getAttribute(TRANSPORTEDTOCOL);
		for (TransporteDTO transporteDTO2 : transporteDTOCol) {
			if(transporteDTO2.getId().getCodigoTransporte().equals(transporteDTOCon.getId().getCodigoTransporte()) && transporteDTO2.getId().getCodigoCompania().equals(transporteDTOCon.getId().getCodigoCompania())){
				return transporteDTO2;				
			}
		}	
		return null;
	}
	
}