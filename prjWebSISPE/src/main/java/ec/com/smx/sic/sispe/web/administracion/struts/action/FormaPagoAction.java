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
public class FormaPagoAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		String salida = "listadoFormaPago";
		String ayuda = request.getParameter(Globals.AYUDA);
		String indice = request.getParameter("indice");
		FormaPagoForm formulario = (FormaPagoForm) form;
		ActionMessages messages = new ActionMessages();

		if (indice != null) {
			session.setAttribute("ec.com.smx.sic.sispe.indiceFormaPago",indice);
			ArrayList formaP = (ArrayList) session
					.getAttribute("ec.com.smx.sic.sispe.formasPago");
			FormaPagoDTO formaPago = (FormaPagoDTO) formaP.get(Integer
					.parseInt(indice));
			
			formulario.setEstadoFormaPago(formaPago.getEstadoFormaPago());
			formulario.setDescripcionFormaPago(formaPago
					.getDescripcionFormaPago());
			salida = "actualizarFormaPago";

		} else if (ayuda != null && ayuda.equals("nuevaFormaPago")) {
			salida = "nuevaFormaPago";
		} else {

			try {
				
				ControlMensajes controlMensajes = new ControlMensajes();
		        messages = controlMensajes.getMessages(messages,session);
		        if(!messages.isEmpty()){
		          //se controla el tipo de mensaje que se va a mostrar
		          String tipoMensaje = (String)session.getAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
		          if(tipoMensaje!=null && tipoMensaje.equals("error"))
		            saveErrors(request,messages);
		          else if(tipoMensaje!=null && tipoMensaje.equals("info"))
		            saveInfos(request,messages);
		          else
		            saveMessages(request,messages);
		        }
		        session.removeAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
		        
				//se eliminan las variables de sesi\u00F3n que comienzen con
				// "ec.com.smx"
				SessionManagerSISPE.removeVarSession(request);
				//colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
				Collection formasPago = new ArrayList();
				FormaPagoDTO consultaFormaPagoDTO = new FormaPagoDTO();
				consultaFormaPagoDTO.getId().setCodigoCompania(
						SessionManagerSISPE.getCurrentCompanys(request).getId()
								.getCodigoCompania());
				//llamada al m\u00E9todo del servicio para obtener las formas de
				// pago
				formasPago = SessionManagerSISPE.getServicioClienteServicio().transObtenerFormaPago(consultaFormaPagoDTO);
				session.setAttribute("ec.com.smx.sic.sispe.formasPago",	formasPago);
				//se obtiene un mensaje de exito de la sesi\u00F3n
		        
				salida = "listadoFormaPago";
			} catch (Exception ex) {
				salida = "errorGlobal";
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
		}
		return mapping.findForward(salida); //finaliza

	}
}
