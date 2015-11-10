/*
 * Creado el 09/05/2007
 *
 */
package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioMotivoMovimientoDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form.AdministracionMotivoMovimientoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite realizar las actualizaciones en el estado de los Motivos de Movimiento
 * </p>
 * @author 	mgudino
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
public class AdministracionMotivoMovimientoAction extends BaseAction 
{
	public ActionForward execute(ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	   
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		AdministracionMotivoMovimientoForm form = (AdministracionMotivoMovimientoForm) formulario;
		String salida = "motivoMovimiento";
		try{
			//cuando se desea desactivar un motivo
			if(request.getParameter("motivoActual")!=null){
			  								
				session.setAttribute("ec.com.smx.sic.sispe.indiceMotivo",new Integer(request.getParameter("motivoActual")));
				session.setAttribute("ec.com.smx.sic.sispe.mensaje","Esta seguro que desea desactivar este motivo ?");
				
			}// Cuando presiona el boton aceptar en el cambio de estado
			else if (form.getBtnAceptar()!=null){
			   
			   ArrayList motivos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.calendarioMotivoMovimientoDTOCol");
			   
			   LogSISPE.getLog().info("valor de indice: {}",session.getAttribute("ec.com.smx.sic.sispe.indiceMotivo"));
			   CalendarioMotivoMovimientoDTO calendarioMotivoMovimientoDTO = (CalendarioMotivoMovimientoDTO)motivos.get(((Integer)session.getAttribute("ec.com.smx.sic.sispe.indiceMotivo")).intValue());
			   LogSISPE.getLog().info("collection de motivos: {}",motivos);
			   
			   // Cambio de estado al contrario del q nos envia en el jsp
			   
			   if(calendarioMotivoMovimientoDTO.getEstadoMotivoMovimiento().equals(estadoActivo))
					calendarioMotivoMovimientoDTO.setEstadoMotivoMovimiento(estadoInactivo);
				else
					calendarioMotivoMovimientoDTO.setEstadoMotivoMovimiento(estadoActivo);
				//campos de auditoria
				
				calendarioMotivoMovimientoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				
				SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioMotivoMovimiento(calendarioMotivoMovimientoDTO);
				session.removeAttribute("ec.com.smx.sic.sispe.mensaje");
				session.removeAttribute("ec.com.smx.sic.sispe.indiceMotivo");
				
			}
			else if(form.getBtnCancelar()!=null){
			   
			   session.removeAttribute("ec.com.smx.sic.sispe.mensaje");
				session.removeAttribute("ec.com.smx.sic.sispe.indiceMotivo");
				 LogSISPE.getLog().info("entro a cancelar");
			   }
			else{
			   
				//eliminaci\u00F3n de las variables de sesi\u00F3n
				SessionManagerSISPE.removeVarSession(request);
				Collection motivos = new ArrayList(); 
				CalendarioMotivoMovimientoDTO motivosDTO = new CalendarioMotivoMovimientoDTO();
				
				// Obtencion de la lista de Motivos de movimiento   
				motivos = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioMotivosMovimiento(motivosDTO);
				if( motivos.isEmpty()){
					//se muestra un mensaje indicando que la lista est\u00E1 vacia
					messages.add("listaVacia",new ActionMessage("message.listaVacia","Motivos de Movimiento"));
					saveInfos(request,messages);
				}else{
					//variable de sesion que almacena la lista obtenida
					session.setAttribute("ec.com.smx.sic.sispe.calendarioMotivoMovimientoDTOCol",motivos);
				}
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		 LogSISPE.getLog().info("fin");
		 form.reset(mapping,request);
		return mapping.findForward(salida);
	}
}
