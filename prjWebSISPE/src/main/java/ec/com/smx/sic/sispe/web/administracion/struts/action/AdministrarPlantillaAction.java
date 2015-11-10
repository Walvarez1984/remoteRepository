/*
 * Creado el 17/04/2007
 *
 * AdministrarPlantillaAction.java
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioColorDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalID;
import ec.com.smx.sic.sispe.dto.CalendarioPlantillaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalID;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form.CalendarizacionForm;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.reportes.dto.MesesDelAnioDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.OrdenDiasSemanaDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * <p>
 * Esta clase permite que el administrador pueda crear y asignar una plantilla a varios locales.
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
@SuppressWarnings("unchecked")
public class AdministrarPlantillaAction extends BaseAction {
	
	//Variables de sesion
	private static final String CONTROLTAB= "ec.com.kruger.kflowWorkList.controlTabAcivity"; //Variable de session para el manejo de los tabs
	//titulos del tab
	private static String crearPlantilla ="Crear Plantillas";
	private static String ajustes ="Ajustes de Capacidad";
	
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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		/************************Declaracion de variables**********************************/
		HttpSession session = request.getSession();
		ActionMessages messages=new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages info = new ActionMessages();
		ActionMessages warnings=new ActionMessages();
		ActionErrors error = new ActionErrors();
		
		CalendarizacionForm formulario=(CalendarizacionForm)form;
		String forward="desplegar";
		
		LogSISPE.getLog().info("buscarLocales "+request.getParameter("buscarLocales"));
		/******************************************************************************************************
		 *****************************************PARA LA PLANTILLA********************************************
		 ******************************************************************************************************/
		//Muestra el Detalle de una plantilla
		if(request.getParameter("verPlantillaLink")!=null || request.getParameter("eliminarPlantillaLink")!=null){
			int plantilla=0;
			if(request.getParameter("verPlantillaLink")!=null)
				plantilla=new Integer(request.getParameter("verPlantillaLink")).intValue();
			else
				plantilla=new Integer(request.getParameter("eliminarPlantillaLink")).intValue();
			
			LogSISPE.getLog().info("Entro a verPlantillaLink");
			ArrayList calendarioPlantillaLocalDTOCol=(ArrayList) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
			//ubica la plantilla que desea ver
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(plantilla);
			//Metodo para obtener el detalle de la plantilla
			SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO);
			//Pregunta si la plantilla seleccionada es la plantilla por defecto
			if(calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
				session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
				formulario.setEstadoPlantilla(true);
			}
			//carga color de la plantilla
			formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
			
			session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
			session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO);
			//Si selecciono que desea eliminar la plantilla
			if(request.getParameter("eliminarPlantillaLink")!=null){
				/****************************verifico si la plantilla no tiene ya registro de kardex************************/
				try{
					int resultado=SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
					LogSISPE.getLog().info("resultado: " + resultado);
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
		//condiciones para las ventanas emergentes de respuesta si
		else if(request.getParameter("condicionSi")!=null){
			//eliminar plantilla
			if(session.getAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes").toString().equals(MessagesWebSISPE.getString("etiqueta.eliminar"))){
				LogSISPE.getLog().info("entra a eliminar plantilla");
				ArrayList calendarioPlantillaLocalDTOCol=(ArrayList) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
				
				Integer indexPlantilla=(Integer) session.getAttribute("ec.com.smx.calendarizacion.indexPlantilla");
				try{
					//ubico la plantilla a eliminar
					CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(indexPlantilla.intValue());
					String nombrePlantilla=calendarioPlantillaLocalDTO.getNombrePlantilla();
					calendarioPlantillaLocalDTO.setUsuarioModificacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					int diasEliminados=SessionManagerSISPE.getServicioClienteServicio().transEliminarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
					LogSISPE.getLog().info("elimino la planilla: " + diasEliminados);
					//obtengo las plantillas
					Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
					session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
					//....session.removeAttribute(COL_CALENDARIO_DIA_LOCAL);
					obtenerPlantillas(formulario,session,request,errors,info,mes);

					session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
					session.removeAttribute("ec.com.smx.calendarizacion.detallePlantilla");
					/*****************OBTIENE LOS DATOS DE LA PLANTILLA POR DEFECTO PARA SIEMPRE VISUALIZARLA POR DEFECTO*/
					if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto")!=null){
						CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO1=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTODefecto");
						session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
						session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO1);
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
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
				//remueve todos los detalles
				for(Iterator i=calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().iterator();i.hasNext();){
					CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=(CalendarioDetallePlantillaLocalDTO)i.next();
					calendarioPlantillaLocalDTO.removeCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
				}
				//ingresa un detalle con la configuracion de TODOS
				LogSISPE.getLog().info("numeroDia: " + formulario.getDiaSeleccionado());
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTONuevo=new CalendarioDetallePlantillaLocalDTO();
				calendarioDetallePlantillaLocalDTONuevo.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
				calendarioDetallePlantillaLocalDTONuevo.setCapacidadNormal(new Double(formulario.getCn()));
				
				// IMPORTANTE: Se setea 0 en lugar del valor de formulario
				//calendarioDetallePlantillaLocalDTONuevo.setCapacidadAdicional(new Double(formulario.getCa()));
				calendarioDetallePlantillaLocalDTONuevo.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
				
				
				calendarioPlantillaLocalDTO.addCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTONuevo);
				LogSISPE.getLog().info("numero de detalles: " + calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
				//ubica la posicion en el detalle donde esta ubicada la configuracion para TODOS
				formulario.opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"),session);
				formulario.setDiaSeleccionado("");
				formulario.setCn(null);
				//formulario.setCa(null);
				
			}
			//reemplazo configuracion:Cuando ya existe la configuracion de ese dia la actualiza con la nueva configuracion 
			else if(session.getAttribute("ec.com.smx.calendarizacion.seleccionMensajesEmergentes").toString().equals(MessagesWebSISPE.getString("etiqueta.reemplazar"))){
				String registroReemplazo=session.getAttribute("ec.com.smx.calendarizacion.diaReemplazar").toString();
				LogSISPE.getLog().info("dia a reemplazar: " + registroReemplazo);
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
				LogSISPE.getLog().info("dia: " + formulario.getDiaSeleccionado());
				LogSISPE.getLog().info("cn: " + formulario.getCn());
				
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
			ArrayList calendarioPlantillaLocalDTOCol=(ArrayList) session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol");
			int indexPlantilla=new Integer(request.getParameter("modificarPlantillaLink")).intValue();
			//ubico la plantilla que deseo editar
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)calendarioPlantillaLocalDTOCol.get(indexPlantilla);
			/****************************verifico si la plantilla no tiene ya registro de kardex************************/
			try{
				int resultado=SessionManagerSISPE.getServicioClienteServicio().transValidarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
				LogSISPE.getLog().info("resultado: " + resultado);
				formulario.setNombrePlantilla(calendarioPlantillaLocalDTO.getNombrePlantilla());
				if(calendarioPlantillaLocalDTO.getEstadoPlantillaLocal().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"))){
					formulario.setEstadoPlantilla(true);
					session.setAttribute("ec.com.smx.calendarizacion.plantillPorDefecto","ok");
				}
				else{
					formulario.setEstadoPlantilla(false);
					session.removeAttribute("ec.com.smx.calendarizacion.plantillPorDefecto");
				}
				session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO);	
				session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","nuevo");
				formulario.paleta(session,calendarioPlantillaLocalDTO,errors);
				//Si no tiene registros de kardex si se puede modificar
				if(resultado==0){
					SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallePorPlantilla(calendarioPlantillaLocalDTO);
					session.removeAttribute("ec.com.smx.calendarizacion.verDetalles");
				}
				else{
					LogSISPE.getLog().info("tiene registros en el kardex");
					//elimina el detalle de la plantilla
					calendarioPlantillaLocalDTO.inicializeCalendariosPlantillasLocal();
					session.setAttribute("ec.com.smx.calendarizacion.verDetalles","ok");
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
			session.removeAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
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
			if(session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO")!=null){
				calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
				//averigua si el dia seleccionado ya tiene una configuracion registrada
				String seleccion=formulario.opcionesDiasModificado(calendarioPlantillaLocalDTO,formulario.getDiaSeleccionado(),session);
				LogSISPE.getLog().info("colec: " + calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
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
								ArrayList calendarioDetallePlantillaLocalDTOCol=(ArrayList)calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal();
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
					ArrayList opcionesDiasCol =(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.opcionesDiasCol");
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
				LogSISPE.getLog().info("numero de dia: " + formulario.getDiaSeleccionado());
				calendarioDetallePlantillaLocalDTO.setNumeroDia(new Integer(formulario.getDiaSeleccionado()));
				calendarioDetallePlantillaLocalDTO.setCapacidadNormal(new Double(formulario.getCn()));
				
				//IMPORTANTE: Se setea 0 en lugar de valor de formulario para la capacidad adicional
				//calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(new Double(formulario.getCa()));
				calendarioDetallePlantillaLocalDTO.setCapacidadAdicional(Double.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.valor.ca.platillas")));
				
				calendarioPlantillaLocalDTO.addCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
				session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO);
				formulario.setDiaSeleccionado("");
				formulario.setCn(null);
				//formulario.setCa(null);
				
			}
			//session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO",calendarioPlantillaLocalDTO);
		}
		//eliminar configuracion
		else if(request.getParameter("eliminarConfiguracionLink")!=null){
			CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
			ArrayList calendarioDetallePlantillaLocalDTOCol=(ArrayList)calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal();
			//ubico la configuracion que deseo eliminar
			CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO=(CalendarioDetallePlantillaLocalDTO)calendarioDetallePlantillaLocalDTOCol.get(new Integer(request.getParameter("eliminarConfiguracionLink")).intValue());
			calendarioPlantillaLocalDTO.removeCalendarioDetallePlantillaLocal(calendarioDetallePlantillaLocalDTO);
			LogSISPE.getLog().info("lista de detalles: " + calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
			/*******************pregunta si solo existe en la lista una configuracion para resto para sobreescribirlo con todo******************************/
			int registros=0;
			String seleccionado=null;
			for(Iterator numero=calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().iterator();numero.hasNext();){
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO1=(CalendarioDetallePlantillaLocalDTO)numero.next();
				//cuenta cuantas configuraciones activas existen en el detalle de la plantilla
				if(!calendarioDetallePlantillaLocalDTO1.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"))){
					registros++;
				}
				LogSISPE.getLog().info("dia a comparar: " + calendarioDetallePlantillaLocalDTO1.getNumeroDia());
				//si existe en el detalle una configuracion activa para RESTO
				if(calendarioDetallePlantillaLocalDTO1.getNumeroDia().toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"))){
					seleccionado=formulario.opcionesDiasModificado(calendarioPlantillaLocalDTO,calendarioDetallePlantillaLocalDTO1.getNumeroDia().toString(),session);
				}
			}
			LogSISPE.getLog().info("registros: " + registros);
			LogSISPE.getLog().info("seleccionado: " + seleccionado);
			//si solo existe una configuracion activa y corresponde al RESTO lo cambia por TODOS
			if(registros==1 && seleccionado!=null){
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTOActualizar=(CalendarioDetallePlantillaLocalDTO)calendarioDetallePlantillaLocalDTOCol.get(new Integer(seleccionado).intValue());
				calendarioPlantillaLocalDTO.updateCalendarioDetallePlantillaLocal(new Integer(seleccionado).intValue(),new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias")).intValue(),calendarioDetallePlantillaLocalDTOActualizar.getCapacidadNormal(),calendarioDetallePlantillaLocalDTOActualizar.getCapacidadAdicional());
				warnings.add("restoPorTodos",new ActionMessage("warning.restoPorTodos"));
			}
			
		}
		//graba la plantilla
		else if(request.getParameter("guardarPlantilla")!=null){
			try{
				//llamo al metodo para grabar la plantilla
				grabarPlantilla(request, errors, messages, info, formulario, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal().toString(),false);
				if(info.size()==0){
					//obtengo las plantillas
					Date mes=(Date)session.getAttribute("ec.com.smx.calendarizacion.mesBusqueda");
					obtenerPlantillas(formulario,session,request,errors,info,mes);
					messages.add("grabarPlantilla",new ActionMessage("messages.grabarPlantilla"));
					session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
				}
			}catch(Exception e){
				errors.add("grabarPlantilla",new ActionMessage("errors.grabarPlantila"));
			}
		}
		
		//Aplico la plantilla a los locales
		else if(request.getParameter("aplicarPlantilla")!=null){
			LogSISPE.getLog().info("va a aplicar la plantilla");
			if(formulario.getSeleccionados()!=null && formulario.getSeleccionados().length>0){
				try{	
					//busca los locales seleccionados
					for(int indice=0;indice<formulario.getSeleccionados().length;indice++){
						LogSISPE.getLog().info("estadoPlantilla: " + formulario.isEstadoPlantilla());
						grabarPlantilla(request, errors, messages, info, formulario,formulario.getSeleccionados()[indice] ,true);
					}
					messages.add("grabarPlantilla",new ActionMessage("messages.grabarPlantillaLocales"));
					session.setAttribute("ec.com.smx.calendarizacion.detallePlantilla","ver");
					//Consulto nuevamente los locales
					cargarLocales(request, errors, null,true);
					//borro la seleccion
					formulario.setSeleccionados(null);
				}catch(Exception e){
					errors.add("grabarPlantilla",new ActionMessage("errors.grabarPlantila"));
				}	
			}else{
				warnings.add("seleccion",new ActionMessage("warnings.seleccionDia"));
			}	
		}
		
		//cierro la paleta de colores
		else if(request.getParameter("cerrarPaleta")!=null)
			LogSISPE.getLog().info("cierra la paleta de colores");
		
		//Consulto locales por ciudad para aplicar plantilla
		else if(request.getParameter("buscarLocales")!=null){
			LogSISPE.getLog().info("entra a buscar locales por ciudad para plantillas");
			cargarLocales(request,errors,formulario.getCiudades(),true);
		}
		
		//Consulto locales por ciudad para aplicar ajustes
		else if(request.getParameter("buscarLocalesAjustes")!=null){
			LogSISPE.getLog().info("entra a buscar locales por ciudad para ajustes");
			cargarLocales(request,errors,formulario.getCiudadesA(),false);
		}

		/*************************************************************************************************************
		 * 								AJUSTES
		 *************************************************************************************************************/
		//Agrega un registro al kardex
		else if(request.getParameter("botonGrabarAjuste")!=null){
			try{
				if(formulario.validarIngresoAjustes(request, error)==0){
					LogSISPE.getLog().info("entro a agregar los ajustes");
					
					CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=new CalendarioDetalleDiaLocalDTO();
					Collection<CalendarioDetalleDiaLocalDTO> calendarioDetalleDiaLocalCol=new ArrayList<CalendarioDetalleDiaLocalDTO>();
					//******** transformo la hora ingresada a tipo Time **************
					GregorianCalendar hora=new GregorianCalendar();
					//***************************************************************
					//Si ingresaron capacidad normal
					if(formulario.getAjusteCapacidadNormal()!=null && !formulario.getAjusteCapacidadNormal().equals("")){
						calendarioDetalleDiaLocalDTO.setHoraDesde(new Time(hora.getTime().getTime()));
						calendarioDetalleDiaLocalDTO.setConceptoMovimiento(formulario.getConceptoMovimiento());
						calendarioDetalleDiaLocalDTO.setCantidadAlmacenamiento(new Double(formulario.getAjusteCapacidadNormal()));
						calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ajusteCapacidad"));
						calendarioDetalleDiaLocalDTO.setCodigoMotivoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteIngresoCapacidadNormal"));
						calendarioDetalleDiaLocalDTO.setCodigoTipoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.ingreso"));
						calendarioDetalleDiaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						calendarioDetalleDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
						calendarioDetalleDiaLocalCol.add(calendarioDetalleDiaLocalDTO);
					}
					
					//Si ingresaron capacidad adicional
					/*if(formulario.getAjusteCapacidadAdicional()!=null && !formulario.getAjusteCapacidadAdicional().equals("")){
						calendarioDetalleDiaLocalDTO=new CalendarioDetalleDiaLocalDTO();
						calendarioDetalleDiaLocalDTO.setHoraDesde(new Time(hora.getTime().getTime()));
						calendarioDetalleDiaLocalDTO.setConceptoMovimiento(formulario.getConceptoMovimiento());
						calendarioDetalleDiaLocalDTO.setCantidadAlmacenamiento(new Double(formulario.getAjusteCapacidadAdicional()));
						calendarioDetalleDiaLocalDTO.setEstadoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ajusteCapacidad"));
						calendarioDetalleDiaLocalDTO.setCodigoMotivoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.ajusteIngresoCapacidadAdicional"));
						calendarioDetalleDiaLocalDTO.setCodigoTipoMovimiento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.ingreso"));
						calendarioDetalleDiaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						calendarioDetalleDiaLocalCol.add(calendarioDetalleDiaLocalDTO);
					}*/
										
					Collection<LocalID> locales=new ArrayList<LocalID>();
	
					LocalID localID=new LocalID();
					//Cargo la lista de locales seleccionados
					for(int indice=0;indice<formulario.getSeleccionadosA().length;indice++){
						LogSISPE.getLog().info("estadoPlantilla: " + formulario.isEstadoPlantilla());
						localID=new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal(new Integer(formulario.getSeleccionadosA()[indice]));
						locales.add(localID);
					}
					//Metodo para aplicar los ajustes a los locales
					SessionManagerSISPE.getServicioClienteServicio().transAplicarAjustesLocalesPorFecha(locales, ConverterUtil.parseStringToDate(formulario.getFechaInicial()),ConverterUtil.parseStringToDate(formulario.getFechaFinal()) , calendarioDetalleDiaLocalCol);
					messages.add("grabarAjustes",new ActionMessage("messages.ajustes.crear"));
					//encero variables
					LogSISPE.getLog().info("va a encerar variables");
					formulario.setAjusteCapacidadAdicional(null);
					formulario.setAjusteCapacidadNormal(null);
					formulario.setConceptoMovimiento(null);
					formulario.setFechaInicial(null);
					formulario.setFechaFinal(null);
					formulario.setSeleccionadosA(null);
				}
			}catch(SISPEException e){
				errors.add("grabarAjustes",new ActionMessage("errors.ajustes.crear"));
			}
		}
		/*******************************************************************************************************
		 *********************************************CONTROL TABS**********************************************
		 *******************************************************************************************************/
		else if(session.getAttribute(CONTROLTAB)!=null){
			LogSISPE.getLog().info("va a seleccionar un tab");
			PaginaTab tabsAdministracionPlantillas=(PaginaTab) session.getAttribute(CONTROLTAB);
			if (tabsAdministracionPlantillas != null && tabsAdministracionPlantillas.comprobarSeleccionTab(request)) {
				
				//Cargo nuevamente los datos del tab
				if (tabsAdministracionPlantillas.esTabSeleccionado(0)){
					LogSISPE.getLog().info("ajustes");
					if(session.getAttribute("ec.com.smx.sic.sispe.vistaLocalDTOColA")==null)
						//Carga los locales que tienen una plantilla asignada
						cargarLocales(request,errors,null,false);
				}
				else if (tabsAdministracionPlantillas.esTabSeleccionado(1)){
					LogSISPE.getLog().info("crearPlantilla");
					if(session.getAttribute("ec.com.smx.sic.sispe.vistaLocalDTOColP")==null)
						//Carga los locales que no tienen una plantilla asignada
						cargarLocales(request,errors,null,true);
				}
			}
			else{
				LogSISPE.getLog().info("va a iniciar los tabs");
				//Iniciar Tabs
				Tab tabAjustes = new Tab(ajustes, "adminPlantillas", "/administracion/plantillaCalendario/ajustes.jsp", true);
				Tab tabPlantillas = new Tab(crearPlantilla, "adminPlantillas", "/administracion/plantillaCalendario/crearPlantillas.jsp", false);
				//Objeto para el manejo de los tabs
				tabsAdministracionPlantillas = new PaginaTab("adminPlantillas", "desplegar", 0, 440, request);
				tabsAdministracionPlantillas.addTab(tabAjustes);
				tabsAdministracionPlantillas.addTab(tabPlantillas);
				//Subo a session el TAB
				session.setAttribute(CONTROLTAB,tabsAdministracionPlantillas);
				inicializarVariables(formulario, session, request, errors,info);
			}
		}
		else{
			inicializarVariables(formulario, session, request, errors,info);			
		}
		/**************************Refresca los valores de las variables de formulario***********************/
		if(session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol")!=null){
			formulario.setColores((Object[])session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol"));
		}
		
		//dias de la semana
		formulario.setDias((Object[])session.getAttribute("ec.com.smx.calendarizacion.opcionesDiasOBJ"));
		
		
		//Grabo los mensajes para que se desplieguen
		saveMessages(request,messages);
		saveInfos(request,info);
		saveWarnings(request,warnings);
		if(error.size()>0){
			LogSISPE.getLog().info("graba error");
			saveErrors(request,(ActionMessages)error);
		}
		if(errors.size()>0){
			LogSISPE.getLog().info("graba errors");
			saveErrors(request,errors);
		}
		
		LogSISPE.getLog().info("salida administracion plantillas: " + forward);
		
		return mapping.findForward(forward);
			}
	

	public static void inicializarVariables(CalendarizacionForm formulario,HttpSession session,HttpServletRequest request,ActionMessages errors,ActionMessages info) throws Exception
	{
		LogSISPE.getLog().info("entro al else");
		/***********Elimina variables de session***********************/
		//se eliminan las posibles variables y formularios de sesi\u00F3n
		SessionManagerSISPE.removeVarSession(request);
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
		
		/**************Carga lista de dias para el combo de configuracion de las plantillas**************/
		
		session.setAttribute("ec.com.smx.calendarizacion.opcionesDiasCol",formulario.opcionesDias(orden2,session));
		//*******************Subo a session el localID*********
		LocalID localID=new LocalID();
		localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		localID.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
		session.setAttribute("ec.com.smx.calendarizacion.localID",localID);
		/**********************************Obtengo las plantillas**************************************/
		obtenerPlantillas(formulario,session,request,errors,info,new Date());
		//Carga los locales que no tienen una plantilla asignada
		cargarLocales(request,errors,null,false);
		//Carga las ciudades
		cargarCiudades(request, errors);
		//*******************Subo a session los estados***********************
		session.setAttribute("ec.com.smx.calendarizacion.estadoAnulado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.anulado"));
		session.setAttribute("ec.com.smx.calendarizacion.estadoActivo",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.activo"));
		session.setAttribute("ec.com.smx.calendarizacion.reservado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoCalendarioDia.reservado"));
		session.setAttribute("ec.com.smx.calendarizacion.porDefecto",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
		session.setAttribute("ec.com.smx.calendarizacion.estadoAnuladoPlantilla",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"));
		//Iniciar Tabs
		Tab tabAjustes = new Tab(ajustes, "adminPlantillas", "/administracion/plantillaCalendario/ajustes.jsp", true);
		Tab tabPlantillas = new Tab(crearPlantilla, "adminPlantillas", "/administracion/plantillaCalendario/crearPlantillas.jsp", false);
		//Objeto para el manejo de los tabs
		PaginaTab tabsAdministracionPlantillas = new PaginaTab("adminPlantillas", "desplegar", 0, 440, request);
		tabsAdministracionPlantillas.addTab(tabAjustes);
		tabsAdministracionPlantillas.addTab(tabPlantillas);
		//Subo a session el TAB
		session.setAttribute(CONTROLTAB,tabsAdministracionPlantillas);
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
			session.setAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol",calendarioPlantillaLocalDTOCol);
		}catch(SISPEException e){
			errors.add("plantilla",new ActionMessage("errors.obtener","las Plantillas"));
		}
	}
	
	
	/**
	 * carga los meses del a\u00F1o
	 * @param session
	 * @param formulario
	 * @throws Exception
	 */
	
	public static void obtenerMeses(HttpSession session,CalendarizacionForm formulario) throws Exception
	{
		Collection mesesDelAnioDTOCol=new ArrayList();
		MesesDelAnioDTO mesesDelAnioDTO=new MesesDelAnioDTO();//DTO no persistente
		for(int i=0;i<formulario.getMeses().length;i++){
			LogSISPE.getLog().info("mes: " + formulario.getMeses()[i]);
			mesesDelAnioDTO=new MesesDelAnioDTO();
			mesesDelAnioDTO.setCodigoMes(new Integer(i).toString());
			mesesDelAnioDTO.setNombreMes(formulario.getMeses()[i]);
			mesesDelAnioDTOCol.add(mesesDelAnioDTO);
		}
		session.setAttribute("ec.com.smx.calendarizacion.mesesDelAnioDTOCol",mesesDelAnioDTOCol);
	}
	
	/**
	   * Carga todos los locales existentes
	   * @param request
	   * @throws Exception
	   */
	  private static void cargarLocales(HttpServletRequest request,ActionMessages errors,String ciudad,boolean conPlantilla) throws Exception{
		  LogSISPE.getLog().info("carga los locales");
		  VistaLocalID vistaLocalID = new VistaLocalID();
		    VistaLocalDTO vistaLocalDTO = new VistaLocalDTO();
		    Collection vistaLocales = null;
		    try
				{
		    	vistaLocalID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		    	if(conPlantilla)
		    		vistaLocalDTO.setNpSinPlantilla("ok");
		    	vistaLocalDTO.setId(vistaLocalID);
		    	if(ciudad!=null && !ciudad.equals(""))
		    		vistaLocalDTO.setCodigoCiudad(ciudad);
				vistaLocales = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
				if(conPlantilla)
					request.getSession().setAttribute("ec.com.smx.sic.sispe.vistaLocalDTOColP", vistaLocales);
				else
					request.getSession().setAttribute("ec.com.smx.sic.sispe.vistaLocalDTOColA", vistaLocales);
		    	LogSISPE.getLog().info("Numero de Registros: " + vistaLocales.size());

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("locales",new ActionMessage("errors.obtener","los Locales"));
		}
	  }
	  
	  /**
	   * Carga todas las ciudades
	   * @param request
	   * @param errors
	   * @throws Exception
	   */
	  private static void cargarCiudades(HttpServletRequest request,ActionMessages errors) throws Exception{
		  try{
				/************************CIUDADES*************************
				 * Consulta las ciudades
				 */
				VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = new VistaEstablecimientoCiudadLocalDTO();
				vistaEstablecimientoCiudadLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				Collection vistaEstablecimientoCiudadLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaEstablecimientoCiudadLocalSector(vistaEstablecimientoCiudadLocalDTO);
				request.getSession().setAttribute("ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol", vistaEstablecimientoCiudadLocalDTOCol);
				LogSISPE.getLog().info("ciudades: " + vistaEstablecimientoCiudadLocalDTOCol.size());
			}catch(SISPEException e){
				errors.add("ciudades",new ActionMessage("errors.cargarCiudades",e.getStackTrace()));
			}
	  }
	  
	  /**
	   * Graba las plantillas
	   * @param request
	   * @param errors
	   * @throws Exception
	   */
	  private static void grabarPlantilla(HttpServletRequest request,ActionMessages errors,ActionMessages messages, ActionMessages info,CalendarizacionForm formulario,String codigoLocal,boolean plantillaDefecto) throws Exception{
		  
			  	HttpSession session=request.getSession();
				LogSISPE.getLog().info("entro a guardar");
				//color de la plantilla
				CalendarioColorDTO calendarioColorDTO=(CalendarioColorDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTO");
				//datos de la plantilla
				CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO=(CalendarioPlantillaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO");
				calendarioPlantillaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
				//verifica si ha sido agregada una configuracion para todos los dias de la semana
				String resto=formulario.opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"),session);
				String todos=formulario.opcionesDiasModificado(calendarioPlantillaLocalDTO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"),session);
				LogSISPE.getLog().info("resto: " + resto);
				LogSISPE.getLog().info("todos: " + todos);
				//veo cuantas configuraciones no anuladas existen 
				int registros=0;
				for(Iterator numero=calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().iterator();numero.hasNext();){
					CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO1=(CalendarioDetallePlantillaLocalDTO)numero.next();
					//cuenta cuantas configuraciones activas existen en el detalle de la plantilla
					if(!calendarioDetallePlantillaLocalDTO1.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado"))){
						registros++;
					}
				}
				//pregunta si existe una configuracion para el RESTO o TODOS o si es una plantilla que no puede ser modificado su detalle
				if(resto!=null || todos!=null || session.getAttribute("ec.com.smx.calendarizacion.verDetalles")!=null || registros>=7){
					if(calendarioPlantillaLocalDTO.getNombrePlantilla()==null){
						calendarioPlantillaLocalDTO.setNombrePlantilla(formulario.getNombrePlantilla());
						calendarioPlantillaLocalDTO.setCodigoColorPrincipal(calendarioColorDTO.getId().getCodigoColorPrincipal());
						calendarioPlantillaLocalDTO.setCodigoColorSecundario(calendarioColorDTO.getCodigoColorSecundario());
						calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.activa"));
						calendarioPlantillaLocalDTO.setTipoPlantilla(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.plantilla.tipoPorDia"));
						calendarioPlantillaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					}
					calendarioPlantillaLocalDTO.getId().setCodigoLocal(new Integer(codigoLocal));
					//pregunta si es plantilla por defecto
					if(plantillaDefecto){
						LogSISPE.getLog().info("estadoPlantilla: " + formulario.isEstadoPlantilla());
						calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.porDefecto"));
						//Cuando es plantilla por defecto quiere decir que se va a asignar platnillas nuevas a los locales
						//por lo tanto el codigo de plantilla debe ser inicializado
						calendarioPlantillaLocalDTO.getId().setCodigoPlantilla(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID"));
						//Tambien inicializo los detalles
						for (Iterator iter = calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().iterator(); iter.hasNext();) {
							CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO = (CalendarioDetallePlantillaLocalDTO) iter.next();
							CalendarioDetallePlantillaLocalID calendarioDetallePlantillaLocalID=new CalendarioDetallePlantillaLocalID(); 
							calendarioDetallePlantillaLocalDTO.setId(calendarioDetallePlantillaLocalID);
						}
					}
					//si no es plantilla por defecto entonces solo le pone estado activo
					else
						calendarioPlantillaLocalDTO.setEstadoPlantillaLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoPlantilla.activa"));
					LogSISPE.getLog().info("nombre: " + calendarioPlantillaLocalDTO.getNombrePlantilla());
					LogSISPE.getLog().info("color P: " + calendarioPlantillaLocalDTO.getCodigoColorPrincipal());
					LogSISPE.getLog().info("color S: " + calendarioPlantillaLocalDTO.getCodigoColorSecundario());
					LogSISPE.getLog().info("estado: " + calendarioPlantillaLocalDTO.getEstadoPlantillaLocal());
					LogSISPE.getLog().info("tipo: " + calendarioPlantillaLocalDTO.getTipoPlantilla());
					LogSISPE.getLog().info("compania: " + calendarioPlantillaLocalDTO.getId().getCodigoCompania());
					LogSISPE.getLog().info("local: " + calendarioPlantillaLocalDTO.getId().getCodigoLocal());
					LogSISPE.getLog().info("coleccion: " + calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().size());
					
					//Metodo para registrar o modificar una plantilla
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioPlantillaLocal(calendarioPlantillaLocalDTO);
				}
				//no se puede grabar una plantilla si no tiene una configuracion para todos los dias
				else{
					info.add("grabarPlantilla",new ActionMessage("info.informacionGrabaPlantila"));
				}

			
	  }
}