/*
 * CondicionReservasAction.java
 * 
 * Creado el 28/05/2007
 *
 */
package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action;

import java.util.ArrayList;
import java.util.Collection;
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

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioMotivoMovimientoDTO;
import ec.com.smx.sic.sispe.dto.CalendarioTipoMovimientoDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form.KardexForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite anular las reservas y modificarlo con los nuevos detalles del kardex
 * </p>
 * 
 * <b>Utliza los m\u00E9todos</b>
 * <ul>
 * 
 * 	<li><b>transRegistrarCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO,TipoMovimiento,Motivo)</b> del servicio ServicioClienteServicio</li>
 * </ul>
 * 
 * Utliza forward para determinar la siguiente acci\u00F3n o pantalla.
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class CondicionReservasAction extends BaseAction {
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
		      HttpServletResponse response) throws Exception 
		  {

		 HttpSession session = request.getSession();
		 ActionMessages messages=new ActionMessages();
		 ActionMessages errors = new ActionMessages();
		 ActionMessages info = new ActionMessages();
		 ActionErrors error = new ActionErrors();
		 KardexForm formulario=(KardexForm)form;
		 String forward="condiciones";
		 
		 //graba las condiciones para agregar los detalles al kardex
	 	if(request.getParameter("grabarCondiciones")!=null){
	 		LogSISPE.getLog().info("entro a grabar condiciones");
	 		if(formulario.validarCampos(error,request)==0){
	 			LogSISPE.getLog().info("entro al if");
	 			//Coleccion completa
	 			ArrayList calendarioDetalleDiaLocalDTOColPadre=(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
	 			LogSISPE.getLog().info("colect: {}", calendarioDetalleDiaLocalDTOColPadre);
	 			//Coleccion hija
	 			Collection calendarioDetalleDiaLocalDTOCol=(Collection)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1");
			 	//Cargo el detalle del calendario del dia seleccionado
			 	CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
			 	calendarioDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
			 	//Kardex a anular
			 	CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO = (CalendarioDetalleDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO");
			 	calendarioDetalleDiaLocalDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
			 	//Indice padre a anular
			 	int indicePadre=(new Integer(session.getAttribute("ec.com.smx.calendarizacion.indicePadre").toString())).intValue();
			 	calendarioDetalleDiaLocalDTO.setMovimientosPorAnulacion(new ArrayList());
			 	LogSISPE.getLog().info("indice2: {}" , indicePadre);
				try{
					for(Iterator i = calendarioDetalleDiaLocalDTOCol.iterator(); i.hasNext();){
						CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO1=(CalendarioDetalleDiaLocalDTO)i.next();
						calendarioDetalleDiaLocalDTO1.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//Campos de Auditoria
						if(calendarioDetalleDiaLocalDTO.getNumeroPedido()!=null)
							calendarioDetalleDiaLocalDTO1.setNumeroPedido(calendarioDetalleDiaLocalDTO.getNumeroPedido());
						//Metodo para agregar un detalle al kardex
						SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO1);
						LogSISPE.getLog().info("colect: {}" , calendarioDetalleDiaLocalDTOColPadre);
						LogSISPE.getLog().info("calendarioDetalle: {}" , calendarioDetalleDiaLocalDTO1.getSecuencialDetalleReferencia());
						calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion().add(calendarioDetalleDiaLocalDTO1);
						LogSISPE.getLog().info("grabo el detalle");
					}
					//Metodo para anular el detalle del kardex
					SessionManagerSISPE.getServicioClienteServicio().transAnularCalendarioDetalleDiaLocal(calendarioDiaLocalDTO,calendarioDetalleDiaLocalDTO);
					LogSISPE.getLog().info("anulo el detalle del kardex");
					session.setAttribute("ec.com.smx.calendarizacion.kardexIngresado","ok");
					session.removeAttribute("ec.com.smx.calendarizacion.indicePadre");
					messages.add("grabarkardex",new ActionMessage("messages.kardex.crear"));
					session.setAttribute("ec.com.smx.calendarizacion.cerrarVentana","ok");
				}catch(SISPEException e){
					errors.add("grabarkardex",new ActionMessage("errors.kardex.crear",e.getStackTrace()));
				}
	 		}
	 	}
	 	
		
	 	//si elije Si
		 else if(request.getParameter("condicionSi")!=null){
		 	try{
		 		LogSISPE.getLog().info("entro a condicionSi");
			 	CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO");
			 	Double cantidadNueva=new Double(session.getAttribute("ec.com.smx.calendarizacion.cantidadReservada").toString());
			 	Double cantidadAnterior=calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento();
			 	//Metodo para obtener las transacciones a partir de una reserva
				Collection calendarioDetalleDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transIngresarReservacionCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO,cantidadNueva.doubleValue(),true);
				session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1",calendarioDetalleDiaLocalDTOCol);
				LogSISPE.getLog().info("coleccion reserva: {}", calendarioDetalleDiaLocalDTOCol.size());
				LogSISPE.getLog().info("cantidadA: {}", calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento());
				LogSISPE.getLog().info("cantidadN: {}" , cantidadNueva);
				if(cantidadNueva.doubleValue()<cantidadAnterior.doubleValue())
					info.add("reservarAdicionalSi",new ActionMessage("info.reservarAdicionalSi",cantidadNueva,new Double(cantidadAnterior.doubleValue()-cantidadNueva.doubleValue())));
				else
					info.add("aceptarAdicionalSi",new ActionMessage("info.aceptarAdicionalSi",cantidadAnterior,new Double(cantidadNueva.doubleValue()-cantidadAnterior.doubleValue())));
				
				
		 	}catch (SISPEException e) {
				errors.add("cargaCondiciones",new ActionMessage("errors.transaccionReserva",e.getStackTrace()));
			}	
		 }
		 //si elije No
		 else if(request.getParameter("condicionNo")!=null){
		 	try{
		 		LogSISPE.getLog().info("entro a condicionNo");
			 	CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO");
			 	Double cantidadNueva=new Double(session.getAttribute("ec.com.smx.calendarizacion.cantidadReservada").toString());
			 	Double cantidadAnterior=calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento();
			 	//Metodo para obtener las transacciones a partir de una reserva
				Collection calendarioDetalleDiaLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transIngresarReservacionCalendarioDetalleDiaLocal(calendarioDetalleDiaLocalDTO,cantidadNueva.doubleValue(),false);
				LogSISPE.getLog().info("coleccion reserva: {}", calendarioDetalleDiaLocalDTOCol.size());
				LogSISPE.getLog().info("cantidadA: {}", calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento());
				LogSISPE.getLog().info("cantidadN: {}", cantidadNueva);
				session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1",calendarioDetalleDiaLocalDTOCol);
				if(cantidadNueva.doubleValue()<cantidadAnterior.doubleValue())
					info.add("reservarAdicionalNo",new ActionMessage("info.reservarAdicionalNo",cantidadNueva,new Double(cantidadAnterior.doubleValue()-cantidadNueva.doubleValue())));
				else
					info.add("aceptarAdicionalNo",new ActionMessage("info.aceptarAdicionalNo",cantidadAnterior,new Double(cantidadNueva.doubleValue()-cantidadAnterior.doubleValue())));
				LogSISPE.getLog().info("infos: {}", info.size());
				
		 	}catch (SISPEException e) {
				errors.add("cargaCondiciones",new ActionMessage("errors.transaccionReserva",e.getStackTrace()));
			}	
		 }
		 //si va ver la reserva de referencia
		 else if(request.getParameter("verReserva")!=null){
		 	try
			{
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
				if(calendarioDetalleDiaLocalDTOCol==null || calendarioDetalleDiaLocalDTOCol.size()==0)
			 		info.add("kardex",new ActionMessage("info.cargar.kardexReferencia"));
			 	session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1",calendarioDetalleDiaLocalDTOCol);
			}catch(SISPEException e){
				LogSISPE.getLog().info("error al cargar kardexReferencia");
				errors.add("kardex",new ActionMessage("errors.cargar.kardexRefencia",e.getStackTrace()));
			}
		 	session.setAttribute("ec.com.smx.calendarizacion.kardexIngresado","ok");
		 }
		 	
	 	else{
	 		LogSISPE.getLog().info("entro a condiciones");
	 		session.removeAttribute("ec.com.smx.calendarizacion.kardexIngresado");
	 	}
	 	 saveMessages(request,messages);
		 saveInfos(request,info);
		 saveErrors(request,errors);
		 saveErrors(request,(ActionMessages)error);
		 LogSISPE.getLog().info("salida: {}" , forward);
		 return mapping.findForward(forward);
		 }
}
