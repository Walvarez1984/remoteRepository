/*
 * Creado el 17/04/2007
 *
 * AdministrarPlantillaAction.java
 */
package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.dto.CatalogoValorDTO;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.TransporteDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioColorDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalCamionDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalHoraDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioPlantillaLocalDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form.CalendarizacionForm;
import ec.com.smx.sic.sispe.web.reportes.dto.CalendarioDetPlaLocHorCamDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.HorasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.MesesDelAnioDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.OrdenDiasSemanaDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * <p>
 * Esta clase permite realizar la calendarizacion para los locales.
 * </p>
 * 
 * <b>Utliza los m\u00E9todos</b>
 * <ul>
 * 
 * 	<li><b>transAplicarPlantillaAdias(calendarioPlantillaLocalDTO,calendarioDiaLocalDTOCol)</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transEliminarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transRegistrarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerColoresPorLocal(localID,calendarioPlantillaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendariosPlantillasLocal(calendarioPlantillaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendarizacionParaLocalPorFecha(localID,calendarioPlantillaLocalDTO,Date)</b> del servicio ServicioClienteServicio</li>
 * </ul>
 * 
 * Utliza forward para determinar la siguiente acci\u00F3n o pantalla.
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */

public class CalendarizacionAction extends BaseAction {
	/**
	 * <p>
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
	 * </p>
	 * 
	 * @param mapping 		El mapeo utilizado para seleccionar esta instancia
	 * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          				campos
	 * @param request 		La petici&oacue; que estamos procesando
	 * @param response 		La respuesta HTTP que se genera
	 * @return ActionForward	Los seguimiento de salida de las acciones
	 * @throws Exception
	 */
		
	public static final String CALENDARIODETPLALOCHORCAMDTOCOL = "ec.com.smx.calendarizacion.calendarioDetPlaLocHorCamDTOCol";
	public static final String CALENDARIOBOD = "ec.com.smx.calendarizacion.calendariobod";
	public static final String CALENDARIOLOC = "ec.com.smx.calendarizacion.calendarioloc";
	public static final String MOSTRARPOPUP = "ec.com.smx.calendarizacion.mostrarpopup";
	public static final String CALENDARIOPLANTILLALOCALDTO="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO";
	public static final String NUEVAPLANTILLA ="ec.com.smx.calendarizacion.nuevaplantilla";
	public static final String MODIFICAPLANTILLA ="ec.com.smx.calendarizacion.modificaplantilla";
	public static final String DETALLELOCHORACAMION ="ec.com.smx.calendarizacion.detallelochoracamion";
	
	public static final String CALENDARIOPLANTILLALOCALDTONUEVA="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTONueva";
		
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
			{
		
		/************************Declaracion de variables**********************************/
		HttpSession session = request.getSession();
		ActionMessages messages=new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages info = new ActionMessages();
		
		ActionMessages warnings=new ActionMessages();
		CalendarizacionForm formulario=(CalendarizacionForm)form;
						
		String forward="calendarizacion";
		
		Integer codLocal = SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal();
		
		
		/***********************************************************************************
		 *********************************PARA EL CALENDARIO********************************
		 ***********************************************************************************/
		//Configuracion de la entrega
		if(request.getParameter("abrirConfiguracion")!=null){
			LogSISPE.getLog().info("--- abrir Popup configuracion ---");
					
			session.setAttribute(MOSTRARPOPUP, "ok");
			
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
			formulario.setNombrePlantilla(null);
			formulario.setEstadoPlantilla(false);
			formulario.paleta(session,null,errors);
			if(errors.size()>0){
				session.setAttribute("ec.com.smx.calendarizacion.verDetalles","ok");
			}
			session.removeAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
			session.removeAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
			
			session.setAttribute(NUEVAPLANTILLA, "ok");
			session.removeAttribute(MODIFICAPLANTILLA);
			
			cargaDatosPorDefecto(session,formulario);
			
			
			
			
		}
		else if(request.getParameter("ocultarVentana") != null){
			
			try {
				session.removeAttribute(MOSTRARPOPUP);
				
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
				
				session.removeAttribute(NUEVAPLANTILLA);
				session.removeAttribute(MODIFICAPLANTILLA);
				
				cargaDatosPorDefecto(session,formulario);
				
				session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
				obtenerPlantillas(formulario,session,request,errors,info,new Date());
			} catch (Exception e) {
				// TODO: handle exception
				LogSISPE.getLog().error("problemas al momento de cancelar el popup");
			}
			
			
		}
		
		else if (request.getParameter("agregarDetallesConfiguracion") != null && (codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))))) {			
			LogSISPE.getLog().info("Entro a agregar un detalle de configuracion horas y camiones");
			
			if(formulario.getDiaSeleccionado()!=null && !formulario.getDiaSeleccionado().isEmpty() && formulario.getHoraSeleccionada()!=null && !formulario.getHoraSeleccionada().isEmpty() && 
			   formulario.getTransporteSeleccionado()!=null && !formulario.getTransporteSeleccionado().isEmpty() && formulario.getCn()!=null && !formulario.getCn().isEmpty()){
				Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs = new ArrayList<CalendarioDetPlaLocHorCamDTO>();
				
				if((Collection<CalendarioDetPlaLocHorCamDTO>)session.getAttribute(CALENDARIODETPLALOCHORCAMDTOCOL)!=null){
					localHorasCamionesDTOs = (Collection<CalendarioDetPlaLocHorCamDTO>)session.getAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
				}					
				String[] arraySeleccionHoras = formulario.getHoraSeleccionada().split(",");
				DateFormat sdf = new SimpleDateFormat("HH:mm");
				Date date = sdf.parse(arraySeleccionHoras[1]);
				
				//verificacion de ingreso de informacion
				if(verificarDatosConfiguracionAgregados(request,formulario, warnings,errors,localHorasCamionesDTOs, new Integer(formulario.getDiaSeleccionado()) ,new Time(date.getTime()), new Integer(formulario.getTransporteSeleccionado()))){
					/**valores para el detalle plantilla local**/
					CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=new CalendarioDetallePlantillaLocalDTO();
					LogSISPE.getLog().info("numero de dia: {}" , formulario.getDiaSeleccionado());
					calendarioDetallePlantillaLocalDTO.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
					calendarioDetallePlantillaLocalDTO.setCapacidadNormal(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
					calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
					calendarioDetallePlantillaLocalDTO.setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.activo"));
					calendarioDetallePlantillaLocalDTO.setNpDiaExistente(numDiasIguales(localHorasCamionesDTOs,new Integer(formulario.getDiaSeleccionado())));
					
					/**valores para el detalle plantilla local horas**/
					CalendarioDetallePlantillaLocalHoraDTO calendarioDetallePlantillaLocalHoraDTO=new CalendarioDetallePlantillaLocalHoraDTO();
					
					LogSISPE.getLog().info("hora del dia: {}" , date.getTime());
					calendarioDetallePlantillaLocalHoraDTO.getId().setHora(new Time(date.getTime()));
					calendarioDetallePlantillaLocalHoraDTO.setNpPosicionHoras(arraySeleccionHoras[0]);
					calendarioDetallePlantillaLocalHoraDTO.setNpNumeroDia(new Integer(formulario.getDiaSeleccionado()));
					calendarioDetallePlantillaLocalHoraDTO.setNpHoraExiste(numDiaHoraIguales(localHorasCamionesDTOs,new Integer(formulario.getDiaSeleccionado()),new Time(date.getTime())));
					
					/**valores para el detalle plantilla local camion**/
					CalendarioDetallePlantillaLocalCamionDTO calendarioDetallePlantillaLocalCamionDTO = new CalendarioDetallePlantillaLocalCamionDTO();			
					LogSISPE.getLog().info("transporte seleccionado: {}"+formulario.getTransporteSeleccionado() );
					calendarioDetallePlantillaLocalCamionDTO.setCantidad(new Integer(formulario.getCn()));
					calendarioDetallePlantillaLocalCamionDTO.getId().setCodigoTransporte(new Integer(formulario.getTransporteSeleccionado()));
					calendarioDetallePlantillaLocalCamionDTO.setNpNombreTransporte(nombreTransporteSeleccionado(request, new Integer(formulario.getTransporteSeleccionado())));
					
					CalendarioDetPlaLocHorCamDTO calendarioDetallesPlantillasLocalHorasCamionesDTO = new CalendarioDetPlaLocHorCamDTO();
					calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalDTO(calendarioDetallePlantillaLocalDTO);
					calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalHoraDTO(calendarioDetallePlantillaLocalHoraDTO);
					calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalCamionDTO(calendarioDetallePlantillaLocalCamionDTO);
					
					
					localHorasCamionesDTOs.add(calendarioDetallesPlantillasLocalHorasCamionesDTO);				
														
					session.setAttribute(CALENDARIODETPLALOCHORCAMDTOCOL,localHorasCamionesDTOs);
					formulario.setDiaSeleccionado("");
					formulario.setHoraSeleccionada("");
					formulario.setTransporteSeleccionado("");
					formulario.setCn(null);
				}				
			}
			else{
				LogSISPE.getLog().error("campos", new ActionMessage("errors.campos.vacios"));
				errors.add("campos", new ActionMessage("errors.campos.vacios"));
			}
			
			
		}
		else if(request.getParameter("eliminarDetallesConfiguracion")!=null && (codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))))){
			
			ArrayList calendarioDetallePlantillaLocalDTOCol=(ArrayList)session.getAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
			//ubico la configuracion que deseo eliminar
			CalendarioDetPlaLocHorCamDTO calendarioDetallesPlantillasLocalHorasCamionesDTO=(CalendarioDetPlaLocHorCamDTO)calendarioDetallePlantillaLocalDTOCol.get(new Integer(request.getParameter("eliminarDetallesConfiguracion")).intValue());
			calendarioDetallesPlantillasLocalHorasCamionesDTO.getCalendarioDetallePlantillaLocalDTO().setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"));
			
			if(calendarioDetallesPlantillasLocalHorasCamionesDTO.getCalendarioDetallePlantillaLocalDTO().getId().getSecuencialDetallePlantilla().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.codigoInicializaAtributosID"))){
				calendarioDetallePlantillaLocalDTOCol.remove(new Integer(request.getParameter("eliminarDetallesConfiguracion")).intValue());
				session.setAttribute(CALENDARIODETPLALOCHORCAMDTOCOL, calendarioDetallePlantillaLocalDTOCol);
			}
			
			LogSISPE.getLog().info("lista de detalles: {}" , calendarioDetallePlantillaLocalDTOCol.size());
		}
		//graba la plantilla
		else if(request.getParameter("guardarDetalleConfiguracion")!=null && (codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))))){
			try{
				LogSISPE.getLog().info("entro a guardar detalle configuracion");	
				//color de la plantilla
				CalendarioColorDTO calendarioColorDTO=(CalendarioColorDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTO");
				//datos de la plantilla
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO = new CalendarioPlantillaLocalDTO();
				//datos de los detalles de la configuracion
				Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOsCol = (Collection<CalendarioDetPlaLocHorCamDTO>)session.getAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
				
				if(CollectionUtils.isNotEmpty(localHorasCamionesDTOsCol)){
					
					
					if((String)session.getAttribute(NUEVAPLANTILLA)==null){
						calendarioPlantillaLocalDTO = (CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
					}
					
										
					calendarioPlantillaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
					
					
					for (CalendarioDetPlaLocHorCamDTO calendarioDetPlaLocHorCamDTO : localHorasCamionesDTOsCol) {
						
						calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().add(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO());						
						calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocalHora().add(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalHoraDTO());
						calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocalCamion().add(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalCamionDTO());
					}
					
					//verifica si ha sido agregada una configuracion para todos los dias de la semana
					String resto=opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"),session);
					String todos=opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"),session);
					LogSISPE.getLog().info("resto: {}" , resto);
					LogSISPE.getLog().info("todos: {}" , todos);					
					
					//veo cuantas configuraciones no anuladas existen 
					int registros=0;
					for(CalendarioDetPlaLocHorCamDTO locHorCamDTO : localHorasCamionesDTOsCol){
						
						CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO1=locHorCamDTO.getCalendarioDetallePlantillaLocalDTO();
						//cuenta cuantas configuraciones activas existen en el detalle de la plantilla
						if(!calendarioDetallePlantillaLocalDTO1.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"))){
							registros++;
						}
					}
					if(formulario.getNombrePlantilla() !=null && !formulario.getNombrePlantilla().isEmpty()){
						//pregunta si existe una configuracion para el RESTO o TODOS o si es una plantilla que no puede ser modificado su detalle
						if((resto!=null || todos!=null || session.getAttribute("ec.com.smx.calendarizacion.verDetalles")!=null || registros>=7)){
														
							calendarioPlantillaLocalDTO.setNombrePlantilla(formulario.getNombrePlantilla());
							calendarioPlantillaLocalDTO.setCodigoColorPrincipal(calendarioColorDTO.getId().getCodigoColorPrincipal());
							calendarioPlantillaLocalDTO.setCodigoColorSecundario(calendarioColorDTO.getCodigoColorSecundario());
							//pregunta si es plantilla por defecto
							if(formulario.isEstadoPlantilla()){
								LogSISPE.getLog().info("estadoPlantilla: {}" , formulario.isEstadoPlantilla());
								calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
							}
							//si no es plantilla por defecto entonces solo le pone estado activo
							else{
								calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.activa"));
								formulario.setEstadoPlantilla(Boolean.TRUE);
							}
							calendarioPlantillaLocalDTO.setTipoPlantilla(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.plantilla.tipoPorHora"));
							calendarioPlantillaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							calendarioPlantillaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
																			
							LogSISPE.getLog().info("nombre: {}" , calendarioPlantillaLocalDTO.getNombrePlantilla());
							LogSISPE.getLog().info("color P: {}" , calendarioPlantillaLocalDTO.getCodigoColorPrincipal());
							LogSISPE.getLog().info("color S: {}" , calendarioPlantillaLocalDTO.getCodigoColorSecundario());
							LogSISPE.getLog().info("estado: {}" , calendarioPlantillaLocalDTO.getEstadoPlantillaLocal());
							LogSISPE.getLog().info("tipo: {}" , calendarioPlantillaLocalDTO.getTipoPlantilla());
							LogSISPE.getLog().info("compania: {}" , calendarioPlantillaLocalDTO.getId().getCodigoCompania());
							LogSISPE.getLog().info("local: {}" , calendarioPlantillaLocalDTO.getId().getCodigoLocal());
							LogSISPE.getLog().info("coleccion detalle plantilla local: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
							LogSISPE.getLog().info("coleccion detalle plantilla horas: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocalHora().size());
							LogSISPE.getLog().info("coleccion detalle plantilla camiones: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocalCamion().size());
												
							
							//Metodo para registrar o modificar una plantilla
							SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioPlantillaLocalHorCam(calendarioPlantillaLocalDTO);
							messages.add("grabarPlantilla",new ActionMessage("messages.grabarPlantilla",calendarioPlantillaLocalDTO.getNombrePlantilla()));
							
							//obtengo las plantillas
							Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
							session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
							obtenerPlantillas(formulario,session,request,errors,info,mes);
							
							session.removeAttribute(MOSTRARPOPUP);
							
							session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
							session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
							session.removeAttribute(NUEVAPLANTILLA);
							
							if(!calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto")) && (String)session.getAttribute(MODIFICAPLANTILLA)!=null){
								cargarDetalles(session, calendarioPlantillaLocalDTO);
								session.removeAttribute(MODIFICAPLANTILLA);
							}
							
						}
						//no se puede grabar una plantilla si no tiene una configuracion para todos los dias
						else{
							info.add("grabarPlantilla",new ActionMessage("info.informacionGrabaPlantila"));							
						}
					}
					else{
						errors.add("nombrePlantilla", new ActionMessage("errors.nombrePlantilla.requerido"));						
					}
					/**************Quita la seleccion de los dias del calendario**************/
					String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
					seleccionCalendario(session,dias,formulario);
					request.removeAttribute(CALENDARIOPLANTILLALOCALDTO);
					
				}
				else{
					//en caso de modificar nombre plantilla y se ponga o quite el por defecto pero ya tiene aplicaion en las tablas de horas
					if((String)session.getAttribute(NUEVAPLANTILLA)==null && (String)session.getAttribute(MODIFICAPLANTILLA)!=null){
						calendarioPlantillaLocalDTO = (CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
						calendarioPlantillaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
						if(formulario.getNombrePlantilla() !=null && !formulario.getNombrePlantilla().isEmpty()){
							calendarioPlantillaLocalDTO.setNombrePlantilla(formulario.getNombrePlantilla());
							calendarioPlantillaLocalDTO.setCodigoColorPrincipal(calendarioColorDTO.getId().getCodigoColorPrincipal());
							calendarioPlantillaLocalDTO.setCodigoColorSecundario(calendarioColorDTO.getCodigoColorSecundario());
							//pregunta si es plantilla por defecto
							if(formulario.isEstadoPlantilla()){
								LogSISPE.getLog().info("estadoPlantilla: {}" , formulario.isEstadoPlantilla());
								calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
							}
							//si no es plantilla por defecto entonces solo le pone estado activo
							else{
								calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.activa"));
								formulario.setEstadoPlantilla(Boolean.TRUE);
							}
							calendarioPlantillaLocalDTO.setTipoPlantilla(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.plantilla.tipoPorHora"));
							calendarioPlantillaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							calendarioPlantillaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
							//Metodo para registrar o modificar una plantilla
							SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioPlantillaLocalHorCam(calendarioPlantillaLocalDTO);
							
							messages.add("grabarPlantilla",new ActionMessage("messages.grabarPlantilla",calendarioPlantillaLocalDTO.getNombrePlantilla()));
							
							//obtengo las plantillas
							Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
							session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
							obtenerPlantillas(formulario,session,request,errors,info,mes);
							
							session.removeAttribute(MOSTRARPOPUP);
							
							session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
							
							session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
							
							if(!calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto")) && (String)session.getAttribute(MODIFICAPLANTILLA)!=null){
								cargarDetalles(session, calendarioPlantillaLocalDTO);
								session.removeAttribute(MODIFICAPLANTILLA);
							}
						}
						else{
							errors.add("nombrePlantilla", new ActionMessage("errors.nombrePlantilla.requerido"));						
						}
						/**************Quita la seleccion de los dias del calendario**************/
						String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
						seleccionCalendario(session,dias,formulario);
						request.removeAttribute(CALENDARIOPLANTILLALOCALDTO);
						
					}
					else{
						errors.add("campos", new ActionMessage("errors.campos.vacios"));
					}
					
				}
				if((CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO")==null){
					cargaDatosPorDefecto(session,formulario);
				}
				
				
			}catch(Exception e){
				errors.add("grabarPlantilla",new ActionMessage("errors.grabarPlantila",e.getStackTrace()));
			}
		}
		/****************************SELECCION DE LOS DIAS DEL CALENDARIO*******************/
		//Seleccion por dias de la semana
		else if(request.getParameter("diaSel")!=null && !request.getParameter("diaSel").equals("")){
			LogSISPE.getLog().info("entra a seleccionar dia");
			
			//orden de los dias de la semana
			ArrayList<OrdenDiasSemanaDTO> ordenDiasSemanaDTOCol=(ArrayList<OrdenDiasSemanaDTO>)session.getAttribute("ec.com.smx.calendarizacion.ordenDiasSemanaDTOCol");
			OrdenDiasSemanaDTO ordenDiasSemanaDTO =(OrdenDiasSemanaDTO)ordenDiasSemanaDTOCol.get(new Integer(request.getParameter("diaSel")).intValue());
			//Si selecciono un dia de la semana
			if(ordenDiasSemanaDTO.getSeleccion().equals("")){
				ordenDiasSemanaDTO.setSeleccion("current");
				seleccionDia(formulario,new Integer(request.getParameter("diaSel")).intValue(),session,true,7,5);
			}
			//Se quito la seleccion de un dia de la semana
			else{
				ordenDiasSemanaDTO.setSeleccion("");
				seleccionDia(formulario,new Integer(request.getParameter("diaSel")).intValue(),session,false,7,5);
			}
		}
		
		//Selecciona una semana del mes
		else if(request.getParameter("filaSel")!=null){
			//orden de los dias de la semana
			String[] filaSeleccionada=(String[])session.getAttribute("ec.com.smx.calendarizacion.filaSeleccionada");
			//si fue seleccionada una semana completa
			if(filaSeleccionada[(new Integer(request.getParameter("filaSel")).intValue()/7)].equals("")){
				filaSeleccionada[(new Integer(request.getParameter("filaSel")).intValue()/7)]="current";
				seleccionDia(formulario,new Integer(request.getParameter("filaSel")).intValue(),session,true,1,7);
			}
			//si quito la seleccion de una semana completa
			else{
				filaSeleccionada[(new Integer(request.getParameter("filaSel")).intValue()/7)]="";
				seleccionDia(formulario,new Integer(request.getParameter("filaSel")).intValue(),session,false,1,7);
			}
			formulario.setFilaSeleccionada(filaSeleccionada);
		}
		
		//Selecciona un dia del mes
		else if(request.getParameter("seleccionCal")!=null){
			seleccionDia(formulario,new Integer(request.getParameter("seleccionCal")).intValue(),session,true,1,1);
		}
		
		//Selecciona todo el mes
		else if(request.getParameter("mes")!=null){
			//cargo todos los dias de las semana y todas las semanas
			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
			String[] filaSeleccionada=(String[])session.getAttribute("ec.com.smx.calendarizacion.filaSeleccionada");
			ArrayList<OrdenDiasSemanaDTO> ordenDiasSemanaDTOCol=(ArrayList<OrdenDiasSemanaDTO>)session.getAttribute("ec.com.smx.calendarizacion.ordenDiasSemanaDTOCol");
			Integer numeroSemanas=(Integer)session.getAttribute("ec.com.smx.calendarizacion.numeroSemanas");
			//si selecciono todos
			if(session.getAttribute("ec.com.smx.calendarizacion.seleccionarTodos").equals("")){
				session.setAttribute("ec.com.smx.calendarizacion.seleccionarTodos","current");
				seleccionDia(formulario,new Integer(request.getParameter("mes")).intValue(),session,true,1,calendarioDiaLocalDTOObj.length);
				for(int i=0;i<numeroSemanas.intValue();i++){
					filaSeleccionada[i]="current";
				}
				for(OrdenDiasSemanaDTO ordenDiasSemanaDTO : ordenDiasSemanaDTOCol){
					
					ordenDiasSemanaDTO.setSeleccion("current");
				}
			}
			//si quito la seleccion de todos
			else{
				session.setAttribute("ec.com.smx.calendarizacion.seleccionarTodos","");
				seleccionDia(formulario,new Integer(request.getParameter("mes")).intValue(),session,false,1,calendarioDiaLocalDTOObj.length);
				
				for(int i=0;i<numeroSemanas.intValue();i++){
					filaSeleccionada[i]="";
				}
				for(OrdenDiasSemanaDTO ordenDiasSemanaDTO : ordenDiasSemanaDTOCol){
					
					ordenDiasSemanaDTO.setSeleccion("");
				}
			}
			formulario.setFilaSeleccionada(filaSeleccionada);
		}
		
		/***************************************NAVEGACION ENTRE MESES*************************************/
		else if(request.getParameter("mesAnterior")!=null){
			LogSISPE.getLog().info("mes anterior");
			Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
			//resto un mes al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			fechaCalendario.add(Calendar.MONTH,-1);
			mes=fechaCalendario.getTime();
			
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
			checkPlantillaDefecto(formulario, calendarioPlantillaLocalDTO);
			obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
		}
		else if(request.getParameter("mesSiguiente")!=null){
			LogSISPE.getLog().info("mes siguiente");
			Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
			//sumo un mes al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			fechaCalendario.add(Calendar.MONTH,1);
			mes=fechaCalendario.getTime();
			
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
			checkPlantillaDefecto(formulario, calendarioPlantillaLocalDTO);
			obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
		}
		/****************************************BUSQUEDAS POR FECHA*******************************************/
		//busqueda del calendario por fecha actual
		else if(request.getParameter("botonBuscarHoy")!=null){
			LogSISPE.getLog().info("busca por fecha actual");
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
				Date mes=new Date();
				
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
				obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
			}
			else{
				info.add("plantillaDefecto",new ActionMessage("info.plantillaDefecto.requerida"));
			}
		}
		else if(request.getParameter("botonBuscarCalendar")!=null){
			//busca por una fecha especifica
			if(formulario.getBuscaFecha()!=null && !formulario.getBuscaFecha().equals("")){
				LogSISPE.getLog().info("busca por fecha especifica");
				if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
					Date mes=ConverterUtil.parseStringToDate(formulario.getBuscaFecha());
					
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
				}
				else{
					info.add("plantillaDefecto",new ActionMessage("info.plantillaDefecto.requerida"));
				}
				
			}
			//busca por mes y a\u00F1o
			else if(formulario.getBuscaAnio()!=null && formulario.getBuscaMes()!=null){
				LogSISPE.getLog().info("busca por mes y a\u00F1o");
				if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
					GregorianCalendar fechaCalendario=new GregorianCalendar();
					fechaCalendario.set(Calendar.YEAR,new Integer(formulario.getBuscaAnio()).intValue());
					fechaCalendario.set(Calendar.MONTH,new Integer(formulario.getBuscaMes()).intValue());
					Date mes=fechaCalendario.getTime();
					
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
				}
				else{
					info.add("plantillaDefecto",new ActionMessage("info.plantillaDefecto.requerida"));
				}
				
			}
			
		}
		/***********************************APLICAR PLANTILLAS AL CALENDARIO**********************************/
		else if(request.getParameter("aplicarCalendario")!=null){
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados")!=null && ((Collection)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados")).size()>0){
				try{
					//obtengo los dias seleccionados para aplicar la plantilla
					Collection calendarioDiaLocalDTOCol =(Collection)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados");
					LogSISPE.getLog().info("dias seleccionados: {}" , calendarioDiaLocalDTOCol.size());
					//obtengo la plantilla a aplicar
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
					calendarioPlantillaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
					//Metodo para aplicar las plantillas: devuelve los dias a los que no se les pudo aplicar una plantilla
					Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOColNoAplicados=SessionManagerSISPE.getServicioClienteServicio().transAplicarPlantillaAdias(calendarioPlantillaLocalDTO,calendarioDiaLocalDTOCol);
					//obtengo los dias a los que no pudo ser aplicada la plantilla
					if(calendarioDiaLocalDTOColNoAplicados!=null && calendarioDiaLocalDTOColNoAplicados.size()>0){
						StringBuffer diasNoAplicados=new StringBuffer();
						for(CalendarioDiaLocalDTO calendarioDiaLocalDTO : calendarioDiaLocalDTOColNoAplicados){
							
							//OJO VER COMO CONCATENAR
							diasNoAplicados.append(calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
							diasNoAplicados.append(", ");
						}
						warnings.add("diasNoAplicados",new ActionMessage("warnings.diasNoAplicados",diasNoAplicados));
					}
					else
						messages.add("diasAplicados",new ActionMessage("messages.diasAplicados"));
					
					/********Quito la seleccion a todo el mes para ver los cambios al aplicar la plantilla***********/
					String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
					seleccionCalendario(session,dias,formulario);
					session.removeAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados");
					
					//Cargo nuevamente los datos del calendario
					Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTODefecto=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTODefecto,errors,mes);
					checkPlantillaDefecto(formulario, calendarioPlantillaLocalDTODefecto);
					
				}catch(SISPEException e){
					errors.add("aplicarPlantilla",new ActionMessage("errors.aplicarPlantilla",e.getMessage()!=null?e.getMessage():e));
				}
			}else{
				warnings.add("seleccionDia",new ActionMessage("warnings.seleccionDia"));
			}	
		
		}
		/******************************************************************************************************
		 *****************************************PARA LA PLANTILLA********************************************
		 ******************************************************************************************************/
		//Muestra el Detalle de una plantilla
		else if(request.getParameter("verPlantillaLink")!=null || request.getParameter("eliminarPlantillaLink")!=null){
			int plantilla=0;
			if(request.getParameter("verPlantillaLink")!=null)
				plantilla=new Integer(request.getParameter("verPlantillaLink")).intValue();
			else
				plantilla=new Integer(request.getParameter("eliminarPlantillaLink")).intValue();
			
			LogSISPE.getLog().info("Entro a verPlantillaLink");
			
			ArrayList<CalendarioPlantillaLocalDTO> calendarioPlantillaLocalDTOCol=(ArrayList<CalendarioPlantillaLocalDTO>) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
			//ubica la plantilla que desea ver
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(plantilla);
			
			//Metodo para obtener el detalle de la plantilla									
			if((String)session.getAttribute(CALENDARIOLOC)!=null){
				SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO);
			}
			if((String)session.getAttribute(CALENDARIOBOD)!=null){						
				cargarDetalles(session,calendarioPlantillaLocalDTO);
			}
			
			//Pregunta si la plantilla seleccionada es la plantilla por defecto
			if(calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
				session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
				formulario.setEstadoPlantilla(true);
			}
			//carga color de la plantilla
			formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
			
			session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
			session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO);
			//Si selecciono que desea eliminar la plantilla
			if(request.getParameter("eliminarPlantillaLink")!=null){
				/****************************verifico si la plantilla no tiene ya registro de kardex************************/
				try{
					int resultado=SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
					if((String)session.getAttribute(CALENDARIOBOD)!=null){	
						Collection<CalendarioDetallePlantillaLocalCamionDTO> locHorCamDTOs = new ArrayList<CalendarioDetallePlantillaLocalCamionDTO>();
						locHorCamDTOs = (Collection<CalendarioDetallePlantillaLocalCamionDTO>) session.getAttribute(DETALLELOCHORACAMION);
						resultado = resultado + SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocalHorCam(calendarioPlantillaLocalDTO,locHorCamDTOs);						
					}
					LogSISPE.getLog().info("resultado: {}" , resultado);
					//Si no tiene registros de kardex si se puede eliminar
					if(resultado==0){
						LogSISPE.getLog().info("va a elminar plantilla");
						//guarda en session la posicion de la plantilla que desea eliminar
						session.setAttribute("ec.com.smx.calendarizacion.indexPlantilla",new Integer(request.getParameter("eliminarPlantillaLink")));
						
						request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Desea Eliminar la Plantilla?");
						session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.eliminar"));
					}
					//Si tiene registros en el kardex la plantilla no puede ser eliminada
					else{
						LogSISPE.getLog().info("tiene registros en el kardex");
						errors.add("errorsPlantilla",new ActionMessage("errors.eliminarPlantilla",calendarioPlantillaLocalDTO.getNombrePlantilla()));
						//session.removeAttribute("ec.com.smx.calendarizacion.detallePlantilla");
					}
				}catch(SISPEException e){
					errors.add("validarCalendario",new ActionMessage("error.validarCalendario",e.getStackTrace())); 
				}
			}
			
		}
		//condiciones para las ventanas emergentes de respuesta si en edici\u00F3n de plantillas
		else if(request.getParameter("condicionSi")!=null){
			//eliminar plantilla
			if(session.getAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes").toString().equals(MessagesWebSISPE.getString("etiqueta.eliminar"))){
				LogSISPE.getLog().info("entra a eliminar plantilla");
				ArrayList<CalendarioPlantillaLocalDTO> calendarioPlantillaLocalDTOCol=(ArrayList<CalendarioPlantillaLocalDTO>) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
				
				Integer indexPlantilla=(Integer) session.getAttribute("ec.com.smx.calendarizacion.indexPlantilla");
				try{
					//ubico la plantilla a eliminar
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(indexPlantilla.intValue());
					String nombrePlantilla=calendarioPlantillaLocalDTO.getNombrePlantilla();
					calendarioPlantillaLocalDTO.setUsuarioModificacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					int diasEliminados=SessionManagerSISPE.getServicioClienteServicio().transEliminarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
					LogSISPE.getLog().info("elimino la planilla: {}" , diasEliminados);
					//obtengo las plantillas
					Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
					session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
					//....session.removeAttribute(COL_CALENDARIO_DIA_LOCAL);
					obtenerPlantillas(formulario,session,request,errors,info,mes);

					session.removeAttribute(CALENDARIOPLANTILLALOCALDTO);
					session.removeAttribute("ec.com.smx.calendarizacion.detallePlantilla");
					/*****************OBTIENE LOS DATOS DE LA PLANTILLA POR DEFECTO PARA SIEMPRE VISUALIZARLA POR DEFECTO*/
					if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
						CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO1=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
						session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
						session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO1);
						formulario.setEstadoPlantilla(true);
						session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
						
						//carga color de la plantilla
						formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
					}

					messages.add("eliminarPlantilla",new ActionMessage("messages.eliminarPlantilla",nombrePlantilla));
				}catch(SISPEException e){
					errors.add("eliminarPlantilla",new ActionMessage("errors.eliminarPlantilla",e.getStackTrace()));
				}
			}
			//selecciono todos:va a eliminar la configuracion de los otros dias y solo deja la de todos
			else if(session.getAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes").toString().equals(MessagesWebSISPE.getString("etiqueta.todo"))){
				
				if((String)session.getAttribute(CALENDARIOBOD)!=null){
					Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs = (Collection<CalendarioDetPlaLocHorCamDTO>)session.getAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
					if(CollectionUtils.isNotEmpty(localHorasCamionesDTOs)){
						Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOsElimi = new ArrayList<CalendarioDetPlaLocHorCamDTO>();
						for (CalendarioDetPlaLocHorCamDTO calendarioDetPlaLocHorCamDTO : localHorasCamionesDTOs) {
							calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO().setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"));
							if(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO().getId().getSecuencialDetallePlantilla().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.codigoInicializaAtributosID"))){
								localHorasCamionesDTOsElimi.add(calendarioDetPlaLocHorCamDTO);
							}
						}
						if(CollectionUtils.isNotEmpty(localHorasCamionesDTOsElimi)){
							localHorasCamionesDTOs.removeAll(localHorasCamionesDTOsElimi);
						}
						String[] arraySeleccionHoras = formulario.getHoraSeleccionada().split(",");
						DateFormat sdf = new SimpleDateFormat("HH:mm");
						Date date = sdf.parse(arraySeleccionHoras[1]);
						
						CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=new CalendarioDetallePlantillaLocalDTO();
						LogSISPE.getLog().info("numero de dia: {}" , formulario.getDiaSeleccionado());
						calendarioDetallePlantillaLocalDTO.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
						calendarioDetallePlantillaLocalDTO.setCapacidadNormal(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
						calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
						calendarioDetallePlantillaLocalDTO.setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.activo"));
						calendarioDetallePlantillaLocalDTO.setNpDiaExistente(numDiasIguales(localHorasCamionesDTOs,new Integer(formulario.getDiaSeleccionado())));
						
						/**valores para el detalle plantilla local horas**/
						CalendarioDetallePlantillaLocalHoraDTO calendarioDetallePlantillaLocalHoraDTO=new CalendarioDetallePlantillaLocalHoraDTO();
						
						LogSISPE.getLog().info("hora del dia: {}" , date.getTime());
						calendarioDetallePlantillaLocalHoraDTO.getId().setHora(new Time(date.getTime()));
						calendarioDetallePlantillaLocalHoraDTO.setNpPosicionHoras(arraySeleccionHoras[0]);
						calendarioDetallePlantillaLocalHoraDTO.setNpNumeroDia(new Integer(formulario.getDiaSeleccionado()));
						calendarioDetallePlantillaLocalHoraDTO.setNpHoraExiste(numDiaHoraIguales(localHorasCamionesDTOs,new Integer(formulario.getDiaSeleccionado()),new Time(date.getTime())));
						
						/**valores para el detalle plantilla local camion**/
						CalendarioDetallePlantillaLocalCamionDTO calendarioDetallePlantillaLocalCamionDTO = new CalendarioDetallePlantillaLocalCamionDTO();			
						LogSISPE.getLog().info("transporte seleccionado: {}"+formulario.getTransporteSeleccionado() );
						calendarioDetallePlantillaLocalCamionDTO.setCantidad(new Integer(formulario.getCn()));
						calendarioDetallePlantillaLocalCamionDTO.getId().setCodigoTransporte(new Integer(formulario.getTransporteSeleccionado()));
						calendarioDetallePlantillaLocalCamionDTO.setNpNombreTransporte(nombreTransporteSeleccionado(request, new Integer(formulario.getTransporteSeleccionado())));
						
						CalendarioDetPlaLocHorCamDTO calendarioDetallesPlantillasLocalHorasCamionesDTO = new CalendarioDetPlaLocHorCamDTO();
						calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalDTO(calendarioDetallePlantillaLocalDTO);
						calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalHoraDTO(calendarioDetallePlantillaLocalHoraDTO);
						calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalCamionDTO(calendarioDetallePlantillaLocalCamionDTO);
						
						
						localHorasCamionesDTOs.add(calendarioDetallesPlantillasLocalHorasCamionesDTO);				
																
						session.setAttribute(CALENDARIODETPLALOCHORCAMDTOCOL,localHorasCamionesDTOs);
						formulario.setDiaSeleccionado("");
						formulario.setHoraSeleccionada("");
						formulario.setTransporteSeleccionado("");
						formulario.setCn(null);
					}
				}
				if((String)session.getAttribute(CALENDARIOLOC)!=null){
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
					//remueve todos los detalles
					for(CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO : calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal()){
						
						calendarioPlantillaLocalDTO.removeCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
					}
					//ingresa un detalle con la configuracion de TODOS
					LogSISPE.getLog().info("numeroDia: {}" , formulario.getDiaSeleccionado());
					CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTONuevo=new CalendarioDetallePlantillaLocalDTO();
					calendarioDetallePlantillaLocalDTONuevo.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
					calendarioDetallePlantillaLocalDTONuevo.setCapacidadNormal(new Double(formulario.getCn()));
	
					//IMPORTANTE: Se setea 0 en lugar del valor de formulario
					//calendarioDetallePlantillaLocalDTONuevo.setCapacidadAdicional(new Double(formulario.getCa()));				
					calendarioDetallePlantillaLocalDTONuevo.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
					
					calendarioPlantillaLocalDTO.addCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTONuevo);
					LogSISPE.getLog().info("numero de detalles: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
					//ubica la posicion en el detalle donde esta ubicada la configuracion para TODOS
					opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"),session);
					formulario.setDiaSeleccionado("");
					formulario.setCn(null);
					//formulario.setCa(null);
				}
				
			}
			//reemplazo configuracion:Cuando ya existe la configuracion de ese dia la actualiza con la nueva configuracion 
			else if(session.getAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes").toString().equals(MessagesWebSISPE.getString("etiqueta.reemplazar"))){
				String registroReemplazo=session.getAttribute("ec.com.smx.calendarizacion.diaReemplazar").toString();
				LogSISPE.getLog().info("dia a reemplazar: {}" , registroReemplazo);
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
				LogSISPE.getLog().info("dia: {}" , formulario.getDiaSeleccionado());
				LogSISPE.getLog().info("cn: {}" , formulario.getCn());
				
				//IMPORTANTE: Se setea 0 en lugar del valor de formulario
				//LogSISPE.getLog().info("ca: " + formulario.getCa());
				//calendarioPlantillaLocalDTO.updateCalendarioDetallePlantillaLocal(new Integer(registroReemplazo).intValue(),new Integer(formulario.getDiaSeleccionado()).intValue(),new Double(formulario.getCn()),new Double(formulario.getCa()));
				calendarioPlantillaLocalDTO.updateCalendarioDetallePlantillaLocal(new Integer(registroReemplazo).intValue(),new Integer(formulario.getDiaSeleccionado()).intValue(),new Double(formulario.getCn()),new Double(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
				
				session.removeAttribute("ec.com.smx.calendarizacion.diaReemplazar");
				formulario.setDiaSeleccionado("");
				formulario.setCn(null);
				//formulario.setCa(null);
			}
		}
		//condiciones para las ventanas emergentes de respuesta no
		else if(request.getParameter("condicionNo")!=null){
			LogSISPE.getLog().info("entro a condiciionNo");
			formulario.setDiaSeleccionado("");
			formulario.setCn(null);
			//formulario.setCa(null);
		}
		
		//Seleccion de color para la plantilla
		else if(request.getParameter("indicecolor")!=null){
			Object[] calendarioColorDTOCol=(Object[])session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol");
			CalendarioColorDTO calendarioColorDTO=(CalendarioColorDTO)calendarioColorDTOCol[new Integer(request.getParameter("indicecolor")).intValue()];
			session.setAttribute("ec.com.smx.calendarizacion.calendarioColorDTO",calendarioColorDTO);
		}
		
		//editar plantillas
		else if(request.getParameter("modificarPlantillaLink")!=null){
			LogSISPE.getLog().info("entro a modificarPlantilla");
			session.removeAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
			ArrayList<CalendarioPlantillaLocalDTO> calendarioPlantillaLocalDTOCol=(ArrayList<CalendarioPlantillaLocalDTO>) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
			int indexPlantilla=new Integer(request.getParameter("modificarPlantillaLink")).intValue();
			//ubico la plantilla que deseo editar
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(indexPlantilla);
			/****************************verifico si la plantilla no tiene ya registro de kardex************************/
			try{
				int resultado=SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
				if((String)session.getAttribute(CALENDARIOBOD)!=null){	
					Collection<CalendarioDetallePlantillaLocalCamionDTO> locHorCamDTOs = new ArrayList<CalendarioDetallePlantillaLocalCamionDTO>();
					locHorCamDTOs = (Collection<CalendarioDetallePlantillaLocalCamionDTO>) session.getAttribute(DETALLELOCHORACAMION);
					resultado = resultado + SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocalHorCam(calendarioPlantillaLocalDTO,locHorCamDTOs);						
				}
				LogSISPE.getLog().info("resultado: {}" , resultado);
				formulario.setNombrePlantilla(calendarioPlantillaLocalDTO.getNombrePlantilla());
				if(calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
					formulario.setEstadoPlantilla(true);
					session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
				}
				else{
					formulario.setEstadoPlantilla(false);
					session.removeAttribute("ec.com.smx.calendarizacion.plantillPorDefecto");
				}
				session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO);	
				//session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","nuevo");
				//para mostrar popup
				session.setAttribute(MOSTRARPOPUP, "ok");
				
				session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				session.setAttribute(MODIFICAPLANTILLA, "ok");
				//fin mostrar popup
				formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
				//Si no tiene registros de kardex si se puede modificar
				if(resultado==0){
					if((String)session.getAttribute(CALENDARIOLOC)!=null){
						SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO);
						session.setAttribute(CALENDARIOPLANTILLALOCALDTONUEVA,calendarioPlantillaLocalDTO);
					}
					if((String)session.getAttribute(CALENDARIOBOD)!=null){					
						cargarDetalles(session,calendarioPlantillaLocalDTO);						
					}
					session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
					
				}
				else{
					LogSISPE.getLog().info("tiene registros en el kardex");
					//elimina el detalle de la plantilla
					calendarioPlantillaLocalDTO.inicializeCalendariosPlantillasLocal();
					session.setAttribute("ec.com.smx.calendarizacion.verDetalles","ok");
					session.setAttribute(CALENDARIOPLANTILLALOCALDTONUEVA,calendarioPlantillaLocalDTO);
					warnings.add("modificarPlantilla",new ActionMessage("warning.modificarPlantilla",calendarioPlantillaLocalDTO.getNombrePlantilla()));
				}
			}catch(SISPEException e){
				errors.add("validarCalendario",new ActionMessage("error.validarCalendario",e.getStackTrace())); 
			}
		}
		
		//nueva plantilla
		else if(request.getParameter("botonNuevaPlantilla")!=null){
			LogSISPE.getLog().info("entro al boton nuevo");
			formulario.setNombrePlantilla(null);
			formulario.setEstadoPlantilla(false);
			formulario.paleta(session,null,errors);
			session.removeAttribute(CALENDARIOPLANTILLALOCALDTO);
			session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","nuevo");
			session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
			session.removeAttribute("ec.com.smx.calendarizacion.plantillPorDefecto");
		}
		
		//paleta de colores
		else if(request.getParameter("seleccionarColor")!=null){
			LogSISPE.getLog().info("entro a seleccionar color");
			request.setAttribute("ec.com.smx.calendarizacion.paletaColores","ok");
		}
		
		//a\u00F1adir configuracion a la plantilla
		else if (request.getParameter("agregarConfiguracion") != null) {
			LogSISPE.getLog().info("Entro a agregar una configuracion");
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=new CalendarioPlantillaLocalDTO();
			boolean agregar=false; //variable para saber si la configuracion puede ser agregada
			//if(session.getAttribute(CALENDARIOPLANTILLALOCALDTO)!=null){
			if(session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA)!=null){
				calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
				//averigua si el dia seleccionado ya tiene una configuracion registrada
				String seleccion=opcionesDiasModificado(calendarioPlantillaLocalDTO,formulario.getDiaSeleccionado(),session);
				LogSISPE.getLog().info("colec:{} " , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
				//si no existe configuracion para el dia seleccionado
				if(seleccion==null){
					//si el dia seleccionado es TODOS
					if(formulario.getDiaSeleccionado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
						request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Desea Eliminar la configuracion para el resto de d\u00EDas?");
						warnings.add("eliminarDias",new ActionMessage("warning.eliminarDias"));
						session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.todo"));
					}
					//si el dia seleccionado no es TODOS
					else{
						if(session.getAttribute("ec.com.smx.calendarizacion.filaTodos")!=null){
							LogSISPE.getLog().info("entro a configuracion todos");
							//si el dia seleccionado es RESTO pregunta si desea sobreescribir la configuracion de TODOS
							if(formulario.getDiaSeleccionado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"))){
								LogSISPE.getLog().info("entro a restodias");
								request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Esta configuraci\u00F3n sera sobreescrita en la configuraci\u00F3n de TODOS desea continuar?");
								session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.reemplazar"));
								warnings.add("sobreescribirTodos",new ActionMessage("warning.sobreescribirTodos"));
								//dia en donde voy a reemplazar la configuracion
								session.setAttribute("ec.com.smx.calendarizacion.diaReemplazar",session.getAttribute("ec.com.smx.calendarizacion.filaTodos"));
							}
							//si al elegir un dia existe una configuracion para todos lo cambia por resto
							else{
								LogSISPE.getLog().info("entro a otro dia");
								agregar=true;
								ArrayList<CalendarioDetallePlantillaLocalDTO> calendarioDetallePlantillaLocalDTOCol=(ArrayList<CalendarioDetallePlantillaLocalDTO>)calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal();
								CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=(CalendarioDetallePlantillaLocalDTO)calendarioDetallePlantillaLocalDTOCol.get(((Integer)session.getAttribute("ec.com.smx.calendarizacion.filaTodos")).intValue());
								calendarioPlantillaLocalDTO.updateCalendarioDetallePlantillaLocal((((Integer)session.getAttribute("ec.com.smx.calendarizacion.filaTodos")).intValue()),new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias")).intValue(),calendarioDetallePlantillaLocalDTO.getCapacidadNormal(),calendarioDetallePlantillaLocalDTO.getCapacidadAdicional());
								warnings.add("todosPorResto",new ActionMessage("warning.todosPorResto"));
							}
						}
						else{
							LogSISPE.getLog().info("no existe una configuracion para todos los dias");
							agregar=true;
						}
					}
				}
				else{
					LogSISPE.getLog().info("el dia ha sido ya seleccionado");
					//***********Busca el nombre del dia seleccionado*********************
					ArrayList<OrdenDiasSemanaDTO> opcionesDiasCol =(ArrayList<OrdenDiasSemanaDTO>)session.getAttribute("ec.com.smx.calendarizacion.opcionesDiasCol");
					OrdenDiasSemanaDTO ordenDiasSemanaDTO=(OrdenDiasSemanaDTO)opcionesDiasCol.get(new Integer(formulario.getDiaSeleccionado()).intValue());
					//***********Llama a la ventana emergente para sobreescribir la configuracion
					request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Desea Sobreescribir la configuracion para el "+ ordenDiasSemanaDTO.getNombreDia()+"?");
					session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.reemplazar"));
					//dia en donde voy a reemplazar la configuracion
					session.setAttribute("ec.com.smx.calendarizacion.diaReemplazar",seleccion);
				}
			}
			else{
				agregar=true;
			}
			//Agrega un nuevo detalle de configuracion
			if(agregar){
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=new CalendarioDetallePlantillaLocalDTO();
				LogSISPE.getLog().info("numero de dia: {}" , formulario.getDiaSeleccionado());
				calendarioDetallePlantillaLocalDTO.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
								
				calendarioDetallePlantillaLocalDTO.setCapacidadNormal(new Double(formulario.getCn()));
				
				//IMPORTANTE: Se setea 0 para el valor de capacidad adicional en lugar del valor desde el formulario
				// ---------------------------------------------------------------------------------------------------------------------------------------------
				//calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(new Double(formulario.getCa()));
				
				calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
				// ---------------------------------------------------------------------------------------------------------------------------------------------
								
				calendarioPlantillaLocalDTO.addCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
				//session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO);
				session.setAttribute(CALENDARIOPLANTILLALOCALDTONUEVA,calendarioPlantillaLocalDTO);
				formulario.setDiaSeleccionado("");
				formulario.setCn(null);
				
				//IMPORTANTE: Se comenta a anulacin de campo de cantidad adicional
				//formulario.setCa(null);
				
			}
			//session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO);
		}
		//eliminar configuracion
		else if(request.getParameter("eliminarConfiguracionLink")!=null){
			//CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
			ArrayList<CalendarioDetallePlantillaLocalDTO> calendarioDetallePlantillaLocalDTOCol=(ArrayList<CalendarioDetallePlantillaLocalDTO>)calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal();
			//ubico la configuracion que deseo eliminar
			CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=(CalendarioDetallePlantillaLocalDTO)calendarioDetallePlantillaLocalDTOCol.get(new Integer(request.getParameter("eliminarConfiguracionLink")).intValue());
			calendarioPlantillaLocalDTO.removeCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
			LogSISPE.getLog().info("lista de detalles: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
			/*******************pregunta si solo existe en la lista una configuracion para resto para sobreescribirlo con todo******************************/
			int registros=0;
			String seleccionado=null;
			for(CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO1 : calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal()){
				
				//cuenta cuantas configuraciones activas existen en el detalle de la plantilla
				if(!calendarioDetallePlantillaLocalDTO1.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"))){
					registros++;
				}
				LogSISPE.getLog().info("dia a comparar: {}" , calendarioDetallePlantillaLocalDTO1.getNumeroDia());
				//si existe en el detalle una configuracion activa para RESTO
				if(calendarioDetallePlantillaLocalDTO1.getNumeroDia().toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"))){
					seleccionado=opcionesDiasModificado(calendarioPlantillaLocalDTO,calendarioDetallePlantillaLocalDTO1.getNumeroDia().toString(),session);
				}
			}
			LogSISPE.getLog().info("registros: {}" , registros);
			LogSISPE.getLog().info("seleccionado: {}" , seleccionado);
			//si solo existe una configuracion activa y corresponde al RESTO lo cambia por TODOS
			if(registros==1 && seleccionado!=null){
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTOActualizar=(CalendarioDetallePlantillaLocalDTO)calendarioDetallePlantillaLocalDTOCol.get(new Integer(seleccionado).intValue());
				calendarioPlantillaLocalDTO.updateCalendarioDetallePlantillaLocal(new Integer(seleccionado).intValue(),new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias")).intValue(),calendarioDetallePlantillaLocalDTOActualizar.getCapacidadNormal(),calendarioDetallePlantillaLocalDTOActualizar.getCapacidadAdicional());
				warnings.add("restoPorTodos",new ActionMessage("warning.restoPorTodos"));
			}
				
			session.setAttribute(CALENDARIOPLANTILLALOCALDTONUEVA, calendarioPlantillaLocalDTO);
			
			
			LogSISPE.getLog().info("lista de detalles: {}" , calendarioDetallePlantillaLocalDTOCol.size());
			
		}
		//graba la plantilla
		else if(request.getParameter("guardarPlantilla")!=null){
			try{
				LogSISPE.getLog().info("entro a guardar");												
				//color de la plantilla
				CalendarioColorDTO calendarioColorDTO=(CalendarioColorDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTO");
				//datos de la plantilla
				//CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTONUEVA);
				calendarioPlantillaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
				//verifica si ha sido agregada una configuracion para todos los dias de la semana
				String resto=opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"),session);
				String todos=opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"),session);
				LogSISPE.getLog().info("resto: {}" , resto);
				LogSISPE.getLog().info("todos: {}" , todos);
				//veo cuantas configuraciones no anuladas existen 
				int registros=0;
				for(CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO1 : calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal()){
					
					//cuenta cuantas configuraciones activas existen en el detalle de la plantilla
					if(!calendarioDetallePlantillaLocalDTO1.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"))){
						registros++;
					}
				}
				//pregunta si existe una configuracion para el RESTO o TODOS o si es una plantilla que no puede ser modificado su detalle
				if(resto!=null || todos!=null || session.getAttribute("ec.com.smx.calendarizacion.verDetalles")!=null || registros>=7){
					calendarioPlantillaLocalDTO.setNombrePlantilla(formulario.getNombrePlantilla());
					calendarioPlantillaLocalDTO.setCodigoColorPrincipal(calendarioColorDTO.getId().getCodigoColorPrincipal());
					calendarioPlantillaLocalDTO.setCodigoColorSecundario(calendarioColorDTO.getCodigoColorSecundario());
					//pregunta si es plantilla por defecto
					if(formulario.isEstadoPlantilla()){
						LogSISPE.getLog().info("estadoPlantilla: {}" , formulario.isEstadoPlantilla());
						calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
					}
					//si no es plantilla por defecto entonces solo le pone estado activo
					else
						calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.activa"));
					calendarioPlantillaLocalDTO.setTipoPlantilla(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.plantilla.tipoPorDia"));
					calendarioPlantillaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					calendarioPlantillaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
					
					LogSISPE.getLog().info("nombre: {}" , calendarioPlantillaLocalDTO.getNombrePlantilla());
					LogSISPE.getLog().info("color P: {}" , calendarioPlantillaLocalDTO.getCodigoColorPrincipal());
					LogSISPE.getLog().info("color S: {}" , calendarioPlantillaLocalDTO.getCodigoColorSecundario());
					LogSISPE.getLog().info("estado: {}" , calendarioPlantillaLocalDTO.getEstadoPlantillaLocal());
					LogSISPE.getLog().info("tipo: {}" , calendarioPlantillaLocalDTO.getTipoPlantilla());
					LogSISPE.getLog().info("compania: {}" , calendarioPlantillaLocalDTO.getId().getCodigoCompania());
					LogSISPE.getLog().info("local: {}" , calendarioPlantillaLocalDTO.getId().getCodigoLocal());
					LogSISPE.getLog().info("coleccion: {}" , calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
										
					
					//Metodo para registrar o modificar una plantilla
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
					messages.add("grabarPlantilla",new ActionMessage("messages.grabarPlantilla",calendarioPlantillaLocalDTO.getNombrePlantilla()));
					
					//obtengo las plantillas
					Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
					session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
					obtenerPlantillas(formulario,session,request,errors,info,mes);
					
					session.removeAttribute(MOSTRARPOPUP);					
					
					session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
					session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
					session.removeAttribute(NUEVAPLANTILLA);
					session.removeAttribute(MODIFICAPLANTILLA);
					
				}
				//no se puede grabar una plantilla si no tiene una configuracion para todos los dias
				else{
					info.add("grabarPlantilla",new ActionMessage("info.informacionGrabaPlantila"));
				}
				/**************Quita la seleccion de los dias del calendario**************/
				String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
				seleccionCalendario(session,dias,formulario);

			}catch(Exception e){
				errors.add("grabarPlantilla",new ActionMessage("errors.grabarPlantila",e.getStackTrace()));
			}
		}
		//regresar de kardex a calendario
		else if(request.getParameter("regresar")!=null){
			LogSISPE.getLog().info("Entro a regresar de kardex");
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioTipoMovimientoDTOCol");
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol");
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
			session.removeAttribute("ec.com.smx.calendarizacion.cantidadReservada");
			Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
			LogSISPE.getLog().info("session detalle: {}" , session.getAttribute("ec.com.smx.calendarizacion.detallePlantilla"));
			if((String) session.getAttribute("ec.com.smx.calendarizacion.detallePlantilla")!=null){
				if(session.getAttribute("ec.com.smx.calendarizacion.detallePlantilla").toString().equals("ver")){
					LogSISPE.getLog().info("va a cargar solo calendario");
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
					checkPlantillaDefecto(formulario, calendarioPlantillaLocalDTO);
				}
				else{
					LogSISPE.getLog().info("carga las plantillas y el calendariio");
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO1=(CalendarioPlantillaLocalDTO)session.getAttribute(CALENDARIOPLANTILLALOCALDTO);
					SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO1);
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO,errors,mes);
					checkPlantillaDefecto(formulario, calendarioPlantillaLocalDTO);
				/**********************************Obtengo las plantillas**************************************/
				//obtenerPlantillas(session,request,errors,info,mes);
				session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
				}
			}
			//elimina las sessiones de los dias y semanas seleccionadas
			String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
			seleccionCalendario(session,dias,formulario);

		}
		//cierro la paleta de colores
		else if(request.getParameter("cerrarPaleta")!=null)
			LogSISPE.getLog().info("cierra la paleta de colores");
		
		else{
			LogSISPE.getLog().info("entro al else");
			/***********Elimina variables de session***********************/
			//se eliminan las posibles variables y formularios de sesi\u00F3n
			SessionManagerSISPE.removeVarSession(request);
			/***********************Subo a session los datos del local****************************/
			session.setAttribute("ec.com.smx.sic.sispe.local",SessionManagerSISPE.getCurrentEntidadResponsable(request));
			//**********************Orden de dias de la semana**********************************
			//cargo los dias de la semana
			String[] orden1={"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
			String[] orden2={"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
			String[] dias=null;
			//si el primer dias es el 1 la semana empieza en domingo caso contrario en lunes
			if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.primerDiaSemana").equals("1"))
				dias=orden2;
			else
				dias=orden1;
			session.setAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias",dias);
			//*******************Subo a session el localID*********
			LocalID localID=new LocalID();
			localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			localID.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			session.setAttribute("ec.com.smx.calendarizacion.localID",localID);
			
			/*****asigancion del valor dependiendo del local logueado*****/
			if(codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))) ){
				session.setAttribute(CALENDARIOBOD, "ok");
				session.removeAttribute(CALENDARIOLOC);
			}
			else{
				session.setAttribute(CALENDARIOLOC, "ok");
				session.removeAttribute(CALENDARIOBOD);
			}
			
			/**************Sessiones para seleccionar por dias y por semanas en el calendario**************/
			seleccionCalendario(session,dias,formulario);
			
			/**************Carga lista de dias para el combo de configuracion de las plantillas**************/
			
			session.setAttribute("ec.com.smx.calendarizacion.opcionesDiasCol",formulario.opcionesDias(dias,session));
			
			/****Carga las horas del dia *******/
			session.setAttribute("ec.com.smx.calendarizacion.opcionesHorasDiaCol", formulario.opcionesHoras(session));
			
			/**********************************Cargo meses del a\u00F1o*****************************************/
			obtenerMeses(session,formulario);
			/**********************************Obtengo las plantillas**************************************/
			obtenerPlantillas(formulario,session,request,errors,info,new Date());
			/*****************OBTIENE LOS DATOS DE LA PLANTILLA POR DEFECTO PARA SIEMPRE VISUALIZARLA POR DEFECTO*/
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
				session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
				session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO);
				formulario.setEstadoPlantilla(true);
				session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
				
				//carga color de la plantilla
				formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
			}
			//*******************Subo a session los estados***********************
			session.setAttribute("ec.com.smx.calendarizacion.estadoAnulado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.anulado"));
			session.setAttribute("ec.com.smx.calendarizacion.estadoActivo",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.activo"));
			session.setAttribute("ec.com.smx.calendarizacion.reservado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.reservado"));
			session.setAttribute("ec.com.smx.calendarizacion.porDefecto",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
			session.setAttribute("ec.com.smx.calendarizacion.estadoAnuladoPlantilla",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"));
			//*******************Subo a session la fecha actual para poder recorrer el calendario*********
			session.setAttribute("ec.com.smx.calendarizacion.mesBusqueda",new Date());
			
		}
		/**************************Refresca los valores de las variables de formulario***********************/
		if(session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol")!=null){
			formulario.setColores((Object[])session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol"));
		}
		//dias de la semana
		formulario.setDias((Object[])session.getAttribute("ec.com.smx.calendarizacion.opcionesDiasOBJ"));
		
		//horas dia
		formulario.setHorasDia((Object[])session.getAttribute("ec.com.smx.calendarizacion.opcionesHorasDiaOBJ"));
		
		if((Collection<TransporteDTO>)session.getAttribute("ec.com.smx.calendarizacion.transporteDTOCol")==null){
			cargaTransporte(request, errors);
		}
		
		formulario.setTransporte((Collection)session.getAttribute("ec.com.smx.calendarizacion.transporteDTOCol"));
		
		if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null)
			formulario.setCalendarioDiaLocal((Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL));
		
		if((String)session.getAttribute("ec.com.smx.calendarizacion.plantillPorDefecto")!=null && formulario.isEstadoPlantilla() ){
			formulario.setEstadoPlantilla(true);
		}		
		//semanas seleccionadas
		if(session.getAttribute("ec.com.smx.calendarizacion.filaSeleccionada")!=null){
			LogSISPE.getLog().info("si hay semanas seleccionadas");
			formulario.setFilaSeleccionada((String[])session.getAttribute("ec.com.smx.calendarizacion.filaSeleccionada"));
		}
		
		//Grabo los mensajes para que se desplieguen
		saveMessages(request,messages);
		saveInfos(request,info);
		saveErrors(request,errors);
		saveWarnings(request,warnings);
		
		LogSISPE.getLog().info("salida calendarizacion: {}", forward);
		
		return mapping.findForward(forward);
	}

	private void checkPlantillaDefecto(CalendarizacionForm formulario,CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO) {
		if(calendarioPlantillaLocalDTO !=null && calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
			formulario.setEstadoPlantilla(true);
		}
	}

	private void cargaDatosPorDefecto(HttpSession session,CalendarizacionForm formulario) {
		CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");	
		//Pregunta si la plantilla seleccionada es la plantilla por defecto
		if(calendarioPlantillaLocalDTO !=null && calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
			session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
			formulario.setEstadoPlantilla(true);
		}
		session.setAttribute(CALENDARIOPLANTILLALOCALDTO,calendarioPlantillaLocalDTO);
		
	}
	
	/**
	 * Seleccion de dias del mes
	 * @param formulario
	 * @param dia               dia individual, semana, o dia de la semana seleccionado en el calendario
	 * @param session
	 * @param seleccionado		dice si los dias van a ser seleccionados o no		
	 * @param indice            dice cual va a ser el incremento entre dias ej. si se seleccion una semana deben seleccionarse los dias sumandole 7
	 * @param limite         	dice cuantos dias van a ser seleccionados   
	 */
	@SuppressWarnings("unchecked")
	private void seleccionDia(CalendarizacionForm formulario, int diaParam,HttpSession session,boolean seleccionado,int indice,int limite) throws Exception{
		
		int dia = diaParam;
		LogSISPE.getLog().info("entra a seleccionar los dias: {}" , dia);
		Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
		//coleccion de dias seleccionados
		Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=new ArrayList<CalendarioDiaLocalDTO>();
		//recupera los dias ya seleccionados
		if(session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados")!=null)
			calendarioDiaLocalDTOCol=(Collection<CalendarioDiaLocalDTO>)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados");
		//itera para poder seleccionar o quitar la seleccion de los dias de acuerdo al dia elejido en el calendario 
		for(int i=0;i<limite;i++){
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia];
			LogSISPE.getLog().info("seleccionado: {}" , calendarioDiaLocalDTO.getNpEsSeleccionado());
			//si selecciona dia por dia en el calendario
			if(indice==1 && limite==1)
			{
				if(calendarioDiaLocalDTO.getNpEsSeleccionado().booleanValue()){
					calendarioDiaLocalDTOCol.remove(calendarioDiaLocalDTO);
					calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.FALSE);
				}
				else{
					calendarioDiaLocalDTOCol.add(calendarioDiaLocalDTO);
					calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.TRUE);
				}
			}
			//si se seleccionaron varios dias a la vez por ej: una semana completa
			else{
				if(seleccionado){
					if(!calendarioDiaLocalDTO.getNpEsSeleccionado().booleanValue()){
						calendarioDiaLocalDTOCol.add(calendarioDiaLocalDTO);
						calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.TRUE);
					}
				}
				else{
					calendarioDiaLocalDTOCol.remove(calendarioDiaLocalDTO);
					calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.FALSE);
				}
			}
			LogSISPE.getLog().info("numero seleccionado: {}" , calendarioDiaLocalDTOCol.size());
			//incremetento los dias de acurdo a lo seleccionado en el calendario
			dia=dia+indice;
		}
		session.setAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados",calendarioDiaLocalDTOCol);
		formulario.setCalendarioDiaLocal(calendarioDiaLocalDTOObj);
	}
	
	/**
	 * Sirve para seleccionar: una semana, todos los dias, un dia en especial para todo el mes
	 * por defecto ninguno seleccionado
	 * 
	 * @param dias
	 * @param session
	 * @param formulario
	 */
	
	public static void seleccionCalendario(HttpSession session,String[] dias,CalendarizacionForm formulario)
	{
		//funcion donde encera los dias de la semana para que por defecto no salga seleccionado ningun dia
		Collection ordenDiasSemanaDTOCol=WebSISPEUtil.ordenDiasSemana(dias);
		session.setAttribute("ec.com.smx.calendarizacion.ordenDiasSemanaDTOCol",ordenDiasSemanaDTOCol);
		
		//Sesion para seleccionar toda una semana en el calendario
		session.setAttribute("ec.com.smx.calendarizacion.filaSeleccionada",formulario.getFilaSeleccionada());
		
		//Por defecto todos los dias del mes sin seleccionar 
		session.setAttribute("ec.com.smx.calendarizacion.seleccionarTodos",formulario.getSeleccionarTodos());
		
		//Por defecto ningun dia seleccionado
		session.removeAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTOColSeleccionados");
	}
	
	
	
	/**
	 * Busca si existe el dia seleccionado dentro de la coleccion
	 * @param calendarioPlantillaLocalDTO
	 * @param diaSeleccionado
	 * @param session
	 * @return
	 */
	
	public static String opcionesDiasModificado(CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO,String diaSeleccionado,HttpSession session) 
	{
		/* Esta funci\u00F3n busca si ya existe una configuraci\u00F3n creada para el dia seleccionado:
		 * Si existe devuelve la ubicaci\u00F3n del registro para preguntar si desea modificarlo o no
		 */
		session.removeAttribute("ec.com.smx.calendarizacion.filaTodos");
		LogSISPE.getLog().info("entro a verificar si el dia existe: {}" , diaSeleccionado);
		String seleccionado=null; //ubicacion del registro en caso de que exista la configuracion
		int contador=0; //contador de filas para ubicar el registro
		LogSISPE.getLog().info("dia seleccionado: {}" , diaSeleccionado);
		for(CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO : calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal()){
			
			LogSISPE.getLog().info("dia a comparar: {}" , calendarioDetallePlantillaLocalDTO.getNumeroDia());
			//pregunta si el dia seleccionado existe y tiene configuracion activa
			if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(diaSeleccionado)){
				LogSISPE.getLog().info("entro a if");
				seleccionado=new Integer(contador).toString();
			}
			//***************** Si el dia seleccionados fue TODOS los guarda en session para cambiarlo por RESTO en caso de elegir una configuracion para otro dis****************
			if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
				LogSISPE.getLog().info("dia todos");
				session.setAttribute("ec.com.smx.calendarizacion.filaTodos",new Integer(contador));
			}
			contador++;
		}
		LogSISPE.getLog().info("fila: {}" , seleccionado);
		return(seleccionado);
	}
	
	
	
	/**
	 * Obtiene las plantillas existentes
	 * @param session
	 * @param request
	 * @param errors
	 * @param info
	 * @param mes
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public static void obtenerPlantillas(CalendarizacionForm formulario,HttpSession session,HttpServletRequest request,ActionMessages errors,ActionMessages info,Date mes) throws Exception
	{
		try{
			//obtener plantillas
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=new CalendarioPlantillaLocalDTO();
			calendarioPlantillaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			calendarioPlantillaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantillaBusqueda.activasOporDefecto"));
			//Metodo para obtener las plantillas
			Collection calendarioPlantillaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendariosPlantillasLocal(calendarioPlantillaLocalDTO);
			//pregunta si existe al menos una plantilla		 	
			if(calendarioPlantillaLocalDTOCol!=null && calendarioPlantillaLocalDTOCol.size()>0){
				LogSISPE.getLog().info("numero de plantillas: {}" , calendarioPlantillaLocalDTOCol.size());
				session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol",calendarioPlantillaLocalDTOCol);
				//obtengo la plantilla por defecto
				HashMap estado=new HashMap();
				estado.put("estadoPlantillaLocal",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
				Collection calendarioPlantillaLocalDTOCol1=ConverterUtil.buscarObjetosEnColeccion(calendarioPlantillaLocalDTOCol,estado);
				//pregunta si existe plantilla por defecto
				if(calendarioPlantillaLocalDTOCol1!=null && calendarioPlantillaLocalDTOCol1.size()>0){
					LogSISPE.getLog().info("si tiene plantilla por defecto");
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO1=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol1.iterator().next();
					LogSISPE.getLog().info("calendarioPlantillaDefectoCol: {}" , calendarioPlantillaLocalDTOCol1.size());
					LogSISPE.getLog().info("calendarioPlantillaDefecto: {}" , calendarioPlantillaLocalDTO1.getNombrePlantilla());
					//obtengo el detalle de la plantilla por defecto
					
					if((String)session.getAttribute(CALENDARIOLOC)!=null){
						SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO1);
					}
					if((String)session.getAttribute(CALENDARIOBOD)!=null){						
						cargarDetalles(session,calendarioPlantillaLocalDTO1);
					}
					
					
					session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto",calendarioPlantillaLocalDTO1);
					session.setAttribute(CALENDARIOPLANTILLALOCALDTO, calendarioPlantillaLocalDTO1);
					//*****************************************************
					
					//obtengo datos del calendario
					obtenerCalendario(formulario,session,calendarioPlantillaLocalDTO1,errors,mes);
				}
				//si no existe una plantilla por defecto se pide que registre una para poder seguir el proceso
				else{
					session.removeAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
					info.add("plantillaDefecto",new ActionMessage("info.plantillaDefecto.requerida"));
					session.removeAttribute("ec.com.smx.calendarizacion.detallePlantilla");
					session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
				}
			}
			//si no existen plantillas pide que se ingrese una por defecto
			else{
				info.add("plantillaDefecto",new ActionMessage("info.plantillaDefecto.requerida"));
				session.removeAttribute("ec.com.smx.calendarizacion.detallePlantilla");
				session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
			}
		}catch(SISPEException e){
			
		}
	}
	/**
	 * obtiene datos del calendario
	 * @param session
	 * @param request
	 * @param calendarioPlantillaLocalDTO
	 * @param errors
	 * @param mes
	 * @throws Exception
	 */
	
	public static void obtenerCalendario(CalendarizacionForm formulario,HttpSession session, CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO,ActionMessages errors,Date mes) throws Exception
	{
		LogSISPE.getLog().info("Se ingresa a obtenerCalendario...");
		try{
			LocalID localID=(LocalID)session.getAttribute("ec.com.smx.calendarizacion.localID");
			//Metodo para obtener el detalle del calendario enviando la plantilla por defecto y el mes que deseo consultar
			List calendarioDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,calendarioPlantillaLocalDTO,mes,null,null,null,null);
			LogSISPE.getLog().info("lista de calendario: {}",calendarioDiaLocalDTOCol.size());
			Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
			session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
			//subo a session el mes de busqueda
			session.setAttribute("ec.com.smx.calendarizacion.mesBusqueda",mes);
			//calculo cuantas semanas tiene el mes
			int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
			LogSISPE.getLog().info("numero de semanas: {}" , maximoSemanas);
			//subo a sesion el numero de semanas
			session.setAttribute("ec.com.smx.calendarizacion.numeroSemanas",new Integer(maximoSemanas));
		}catch(SISPEException e){
			errors.add("obtenerCalendario",new ActionMessage("error.obtener.calendario",e.getStackTrace()));
		}
		/**************Quita la seleccion de los dias del calendario**************/
		String[] dias=(String[])session.getAttribute("ec.com.smx.sic.sispe.calendarizacion.ordenDias");
		seleccionCalendario(session,dias,formulario);
	}
	
	/**
	 * carga los meses del a\u00F1o
	 * @param session
	 * @param formulario
	 * @throws Exception
	 */
	
	public static void obtenerMeses(HttpSession session,CalendarizacionForm formulario) throws Exception{
		Collection<MesesDelAnioDTO> mesesDelAnioDTOCol=new ArrayList<MesesDelAnioDTO>();
		MesesDelAnioDTO mesesDelAnioDTO=new MesesDelAnioDTO();//DTO no persistente
		for(int i=0;i<formulario.getMeses().length;i++){
			LogSISPE.getLog().info("mes: {}" , formulario.getMeses()[i]);
			mesesDelAnioDTO=new MesesDelAnioDTO();
			mesesDelAnioDTO.setCodigoMes(new Integer(i).toString());
			mesesDelAnioDTO.setNombreMes(formulario.getMeses()[i]);
			mesesDelAnioDTOCol.add(mesesDelAnioDTO);
		}
		session.setAttribute("ec.com.smx.calendarizacion.mesesDelAnioDTOCol",mesesDelAnioDTOCol);
	}
	
	public static void instanciarVentanaOpcionesConfiguracion(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Crear plantilla");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		
		Integer codLocal = SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal();
		
		if(codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))) || codLocal.equals(new Integer(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))) ){
			popUp.setValorOK("requestAjax('calendarizacion.do', ['pregunta','div_pagina'], {parameters: 'guardarDetalleConfiguracion=ok',popWait:true, evalScripts:true});ocultarModal();");
		}
		else{
			popUp.setValorOK("requestAjax('calendarizacion.do', ['pregunta','div_pagina'], {parameters: 'guardarPlantilla=ok',popWait:true, evalScripts:true});ocultarModal();");
		}
		
		popUp.setValorCANCEL("requestAjax('calendarizacion.do', ['pregunta','div_pagina'], {parameters: 'ocultarVentana=ok', popWait:false, evalScripts:true});ocultarModal();");
		
		popUp.setAccionEnvioCerrar("requestAjax('calendarizacion.do', ['pregunta','div_pagina'], {parameters: 'ocultarVentana=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setContenidoVentana("calendarizacion/nuevaCalendarizacion/nuevaPlantilla.jsp");
		popUp.setAncho(75D);
		popUp.setTope(-50D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		
		popUp = null;
	}
	
	/**
	 * Funci\u00F3n que carga los transporte
	 * @param request
	 * @param errors
	 */
	@SuppressWarnings("unchecked")
	private static void cargaTransporte(HttpServletRequest request,ActionMessages errors){
		try{
						
			TransporteDTO transporteDTO = new TransporteDTO();
			transporteDTO.setTipoMercaderiaTransportada(new CatalogoValorDTO());
			transporteDTO.setTipoTransporte(new CatalogoValorDTO());
			
			Collection<TransporteDTO> transporteDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerTransporte(transporteDTO);
			
			
			request.getSession().setAttribute("ec.com.smx.calendarizacion.transporteDTOCol", transporteDTOCol);
			LogSISPE.getLog().info("transporte: {}", transporteDTOCol.size());
		}catch(Exception e){
			errors.add("transporte",new ActionMessage("errors.cargarTransporte",e.getStackTrace()));
		}
	}
	
	private String nombreTransporteSeleccionado(HttpServletRequest request,Integer idTransporte){
		String nombre = null;
		Collection<TransporteDTO> transporteDTO = (Collection<TransporteDTO>)request.getSession().getAttribute("ec.com.smx.calendarizacion.transporteDTOCol");
		for (TransporteDTO transporteDTO2 : transporteDTO) {
			if(transporteDTO2.getId().getCodigoTransporte().equals(idTransporte)){
				nombre = transporteDTO2.getNombreTransporte();
				break;
			}
		}
		return nombre;
	}
	
	@SuppressWarnings("unchecked")
	private static void cargarDetalles(HttpSession session,CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO){
		
		//Metodo para obtener el detalle de la plantilla					
		try {
			session.removeAttribute(DETALLELOCHORACAMION);
			session.removeAttribute(CALENDARIODETPLALOCHORCAMDTOCOL);
			Collection<CalendarioDetallePlantillaLocalCamionDTO> valor = SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantillaLocHorCam(calendarioPlantillaLocalDTO);
			LogSISPE.getLog().info("tamano detalle configuracion plantilla horas numero camiones "+valor.size());
			
			Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs = new ArrayList<CalendarioDetPlaLocHorCamDTO>();
			
			session.setAttribute(DETALLELOCHORACAMION, valor);
			
			for (CalendarioDetallePlantillaLocalCamionDTO calendarioDetallePlantillaLocalCamionDTO : valor) {
				
				CalendarioDetPlaLocHorCamDTO calendarioDetallesPlantillasLocalHorasCamionesDTO = new CalendarioDetPlaLocHorCamDTO();
				
				//detalle configurcion local
				calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().getCalendarioDetallePlantillaLocalDTO().setNpDiaExistente(new Integer(2));
				calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().getCalendarioDetallePlantillaLocalDTO().setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.activo"));
				calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalDTO(calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().getCalendarioDetallePlantillaLocalDTO());
				
				//detalle configuracion horas
				calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().setNpPosicionHoras(buscarPosicionHoras(session,calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().getId().getHora()));
				calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().setNpNumeroDia(calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().getCalendarioDetallePlantillaLocalDTO().getNumeroDia());
				calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO().setNpHoraExiste(new Integer(2));
				calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalHoraDTO(calendarioDetallePlantillaLocalCamionDTO.getCalendarioDetallePlantillaLocalHoraDTO());
				
				//detalle configuracion camiones
				calendarioDetallePlantillaLocalCamionDTO.setNpNombreTransporte(calendarioDetallePlantillaLocalCamionDTO.getTransporteDTO().getNombreTransporte());
				calendarioDetallesPlantillasLocalHorasCamionesDTO.setCalendarioDetallePlantillaLocalCamionDTO(calendarioDetallePlantillaLocalCamionDTO);
				
				//agregar detalles para mostrar en pantalla
				localHorasCamionesDTOs.add(calendarioDetallesPlantillasLocalHorasCamionesDTO);
			}
			
			session.setAttribute(CALENDARIODETPLALOCHORCAMDTOCOL,localHorasCamionesDTOs);
			
		} catch (SISPEException e) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("Error de aplicacion. "+e);
		} catch (Exception e) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("Error de aplicacion. "+e);
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private static String buscarPosicionHoras(HttpSession session,Time horaSel){
		
		Collection<HorasDTO> horas = (Collection<HorasDTO>)session.getAttribute("ec.com.smx.calendarizacion.opcionesHorasDiaCol");
		
		DateFormat sdf = new SimpleDateFormat("HH:mm");
		String posicion = null;
		Date date;
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
			
			if(val.size()>0){	
				HorasDTO horasDTO = new HorasDTO();
				horasDTO = (HorasDTO)val.iterator().next();
				String[] arraySeleccionHoras = horasDTO.getSeleccion().split(",");
				posicion = arraySeleccionHoras[0];
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
		
		return posicion;
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
	
	
	public static String opcionesDiasModificadoHorCam(Collection<CalendarioDetallePlantillaLocalDTO> detallePlantillaLocalDTO  ,String diaSeleccionado,HttpSession session) 
	{
		/* Esta funci\u00F3n busca si ya existe una configuraci\u00F3n creada para el dia seleccionado:
		 * Si existe devuelve la ubicaci\u00F3n del registro para preguntar si desea modificarlo o no
		 */
		session.removeAttribute("ec.com.smx.calendarizacion.filaTodos");
		LogSISPE.getLog().info("entro a verificar si el dia existe: {}" , diaSeleccionado);
		String seleccionado=null; //ubicacion del registro en caso de que exista la configuracion
		int contador=0; //contador de filas para ubicar el registro
		LogSISPE.getLog().info("dia seleccionado: {}" , diaSeleccionado);
		for(CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO : detallePlantillaLocalDTO){
			
			LogSISPE.getLog().info("dia a comparar: {}" , calendarioDetallePlantillaLocalDTO.getNumeroDia());
			//pregunta si el dia seleccionado existe y tiene configuracion activa
			if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(diaSeleccionado)){
				LogSISPE.getLog().info("entro a if");
				seleccionado=new Integer(contador).toString();
			}
			//***************** Si el dia seleccionados fue TODOS los guarda en session para cambiarlo por RESTO en caso de elegir una configuracion para otro dis****************
			if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
				LogSISPE.getLog().info("dia todos");
				session.setAttribute("ec.com.smx.calendarizacion.filaTodos",new Integer(contador));
			}
			contador++;
		}
		LogSISPE.getLog().info("fila: {}" , seleccionado);
		return(seleccionado);
	}
	
	public boolean verificarConfiguracionCalendario(HttpServletRequest request,CalendarizacionForm formulario,ActionMessages warnings,CalendarioDetallePlantillaLocalDTO plantillaLocalDTO,String seleccion){
		//si no existe configuracion para el dia seleccionado
		boolean agregar = true;
		HttpSession session = request.getSession();
		if(seleccion==null){
			//si el dia seleccionado es TODOS
			if(formulario.getDiaSeleccionado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
				request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Desea Eliminar la configuraci\u00F3n para el resto de d\u00EDas?");
				warnings.add("eliminarDias",new ActionMessage("warning.eliminarDias"));
				session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.todo"));
				agregar = false;				
			}
			//si el dia seleccionado no es TODOS
			else{
				if(session.getAttribute("ec.com.smx.calendarizacion.filaTodos")!=null){
					LogSISPE.getLog().info("entro a configuracion todos");
					//si el dia seleccionado es RESTO pregunta si desea sobreescribir la configuracion de TODOS
					if(formulario.getDiaSeleccionado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"))){
						LogSISPE.getLog().info("entro a restodias");
						request.setAttribute("ec.com.smx.calendarizacion.eliminarPlantilla","Esta configuraci\u00F3n sera sobreescrita en la configuraci\u00F3n de TODOS desea continuar?");
						session.setAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes",MessagesWebSISPE.getString("etiqueta.reemplazar"));
						warnings.add("sobreescribirTodos",new ActionMessage("warning.sobreescribirTodos"));
						//dia en donde voy a reemplazar la configuracion
						session.setAttribute("ec.com.smx.calendarizacion.diaReemplazar",session.getAttribute("ec.com.smx.calendarizacion.filaTodos"));
					}
					//si al elegir un dia existe una configuracion para todos lo cambia por resto
					else{
						LogSISPE.getLog().info("entro a otro dia");									
						plantillaLocalDTO.setNpEstadoDetalle(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.actualizado"));
						plantillaLocalDTO.setNumeroDia(new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias")).intValue());
						agregar=true;
						warnings.add("todosPorResto",new ActionMessage("warning.todosPorResto"));
					}
				}
				else{
					LogSISPE.getLog().info("no existe una configuracion para todos los dias");
					agregar=true;
				}
			}
		}
		return agregar;
	}
	
	private boolean verificarDatosConfiguracionAgregados(HttpServletRequest request,CalendarizacionForm formulario,ActionMessages warnings,ActionMessages errors,Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs,Integer numDia,Time horaSel,Integer transporteSel){
		
		boolean agregar=true;
		
		if(CollectionUtils.isNotEmpty(localHorasCamionesDTOs)){
			
			HttpSession session=request.getSession();
			String seleccion=null; //ubicacion del registro en caso de que exista la configuracion
//			int contador=0; //contador de filas para ubicar el registro
			Collection<CalendarioDetallePlantillaLocalDTO> detallePlantillaLocalDTO = new ArrayList<CalendarioDetallePlantillaLocalDTO>();
			for (CalendarioDetPlaLocHorCamDTO calendarioDetPlaLocHorCamDTO : localHorasCamionesDTOs) {
				if(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalHoraDTO().getId().getHora().equals(horaSel) && 
				   calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalCamionDTO().getId().getCodigoTransporte().equals(transporteSel) &&
				   calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO().getNumeroDia().equals(numDia)){
					agregar=false;
					errors.add("horTrans", new ActionMessage("errors.campos.hora.transporte.repetidos"));
					break;
				}
				else{
					CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO = calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO();
					detallePlantillaLocalDTO.add(calendarioDetallePlantillaLocalDTO);
					seleccion = opcionesDiasModificadoHorCam(detallePlantillaLocalDTO,formulario.getDiaSeleccionado(),session);
					agregar = verificarConfiguracionCalendario(request,formulario,warnings,calendarioDetallePlantillaLocalDTO,seleccion);
					if(formulario.getDiaSeleccionado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
						break;
					}
				}
				seleccion=null;
			}
			
		}	
		return agregar;
	}
	
	private static Integer numDiasIguales (Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs,Integer numDia){
		if(CollectionUtils.isNotEmpty(localHorasCamionesDTOs)){
			for (CalendarioDetPlaLocHorCamDTO calendarioDetPlaLocHorCamDTO : localHorasCamionesDTOs) {
				if(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalDTO().getNumeroDia().equals(numDia)){
					return 1;//si exite el dia
				}
			}
		}
		return 0;//no existe el dia
	}
	
	private static Integer numDiaHoraIguales (Collection<CalendarioDetPlaLocHorCamDTO> localHorasCamionesDTOs,Integer numDia,Time hora){
		if(CollectionUtils.isNotEmpty(localHorasCamionesDTOs)){
			for (CalendarioDetPlaLocHorCamDTO calendarioDetPlaLocHorCamDTO : localHorasCamionesDTOs) {
				if(calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalHoraDTO().getNpNumeroDia().equals(numDia) && 
				   calendarioDetPlaLocHorCamDTO.getCalendarioDetallePlantillaLocalHoraDTO().getId().getHora().equals(hora)){
					return 1;//si exite el dia
				}
			}
		}
		return 0;//no existe el dia
	}
	
}