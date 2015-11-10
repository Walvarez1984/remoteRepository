/*
 * Creado el 09/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.FormaPagoDTO;
import ec.com.smx.sic.sispe.web.administracion.struts.form.FormaPagoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * @author mnaranjo
 * 
 * TODO Para cambiar la plantilla de este comentario generado, vaya a Ventana -
 * Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
public class ActualizarFormaPagoAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		FormaPagoForm formulario = (FormaPagoForm) form;
		String salida = "actualizacionFormaPago";
		
		String ayuda = request.getParameter(Globals.AYUDA);
		if(ayuda.equals("guardarFormaPago")){
			
			 //se obtiene la colecci\u00F3n de formas de Pago
	        ArrayList formasPago = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.formasPago");
	        try {
	          //se obtiene el indice de la colecci\u00F3n
	          int indice = Integer.parseInt((String)session.getAttribute("ec.com.smx.sic.sispe.indiceFormaPago"));
	          //DTO de motivo descuento
	          FormaPagoDTO formaPagoDTO = (FormaPagoDTO) formasPago.get(indice);
	          //establecimiento de datos a actualizar
	          formaPagoDTO.setEstadoFormaPago(formulario.getEstadoFormaPago());
	          formaPagoDTO.setDescripcionFormaPago(formulario.getDescripcionFormaPago());
	          //campo auditoria
	          formaPagoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
	          
	          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
	          SessionManagerSISPE.getServicioClienteServicio().transRegistrarFormaPago(formaPagoDTO);
	          
	          //formulario.setBotonActualizar(null);
	          //establece la lista de registros en sesi\u00F3n
	          session.setAttribute("ec.com.smx.sic.sispe.formasPago", formasPago);
	          ControlMensajes controlMensajes = new ControlMensajes();
	          controlMensajes.setMessages(session,"message.exito.actualizacion","La Forma de Pago");
	          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
	          salida = "guardarActualizacionFormaPago";
	        }
	        catch (NumberFormatException e) {
	          //si falla el m\u00E9todo de obtenci\u00F3n de datos
	          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
	          messages.add("formaPago", new ActionMessage("errors.llamadaServicio.registrarDatos","la Forma de Pago"));
	          saveErrors(request, messages);
	        }
		}
		return mapping.findForward(salida);
	}
}
