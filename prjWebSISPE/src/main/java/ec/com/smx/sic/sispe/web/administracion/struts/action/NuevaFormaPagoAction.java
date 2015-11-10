/*
 * Creado el 09/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

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
@SuppressWarnings("unchecked")
public class NuevaFormaPagoAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		FormaPagoForm formulario = (FormaPagoForm) form;

		String salida = "nuevaFormaPago";
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			if (ayuda.equals("guardarFormaPago")) {
				
				Collection formasPago = null;

				//comprueba si la colecci\u00F3n de temporadas no es vac\u00EDa
				if (session.getAttribute("ec.com.smx.sic.sispe.formasPago") != null)
					//se obtiene la colecci\u00F3n de temporadas
					formasPago = (Collection) session
							.getAttribute("ec.com.smx.sic.sispe.formasPago");
				else {
					formasPago = new ArrayList();
				}
				FormaPagoDTO formaPagoDTO = new FormaPagoDTO();
				formaPagoDTO.getId().setCodigoCompania(
						SessionManagerSISPE.getCurrentCompanys(request).getId()
								.getCodigoCompania());
				//establece los campos para el nuevo registro
				formaPagoDTO.setDescripcionFormaPago(formulario
						.getDescripcionFormaPago());
				formaPagoDTO
						.setEstadoFormaPago(formulario.getEstadoFormaPago());				
				
				//campo auditoria
				formaPagoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				
				//llamada al m\u00E9todo del servicio para almacenar el nuevo
				// registro
				SessionManagerSISPE.getServicioClienteServicio()
						.transRegistrarFormaPago(formaPagoDTO);
				//se agrega a la colecci\u00F3n la lista de registrso obtenidos
				formasPago.add(formaPagoDTO);
				//almacena la lista de temporadas en sesi\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.formasPago",
						formasPago);
				LogSISPE.getLog().info("tamano coleccion: " + formasPago.size());
				 //se guarda el mensaje de exito en la sesi\u00F3n
		        ControlMensajes controlMensajes = new ControlMensajes();
		        controlMensajes.setMessages(session,"message.exito.registro","La Forma de Pago");
		        session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
				salida = "guardarFormaPago";
			}
		} catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			messages.add("Forma de Pago",
					new ActionMessage("errors.llamadaServicio.registrarDatos",
							"la Forma de Pago"));
			saveErrors(request, messages);
		}
		return mapping.findForward(salida);
	}
}
