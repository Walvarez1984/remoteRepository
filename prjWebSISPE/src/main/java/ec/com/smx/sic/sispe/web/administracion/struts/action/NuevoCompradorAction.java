/*
 * Creado el 15/05/2007
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
import ec.com.smx.sic.cliente.mdl.dto.CompradorDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.CompradorForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author mnaranjo
 *
 */
@SuppressWarnings("unchecked")
public class NuevoCompradorAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		CompradorForm formulario = (CompradorForm) form;

		String salida = "nuevoComprador";
		String ayuda = request.getParameter(Globals.AYUDA);
		//saco del properties el tipoComprador
		session.setAttribute("administracion.tipoComprador.interno", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.interno"));
		session.setAttribute("administracion.tipoComprador.externo", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.externo"));

		try {
			if (ayuda.equals("guardarComprador")) {
				
				Collection compradores = null;

				//comprueba si la colecci\u00F3n de compradores no es vac\u00EDa
				if (session.getAttribute("ec.com.smx.sic.sispe.compradores") != null)
					//se obtiene la colecci\u00F3n de compradores
					compradores = (Collection) session.getAttribute("ec.com.smx.sic.sispe.compradores");
				else {
					compradores = new ArrayList();
				}
				CompradorDTO compradorDTO = new CompradorDTO(Boolean.TRUE);
				compradorDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//establece los campos para el nuevo registro
				
				compradorDTO.setEstadoComprador(formulario.getEstadoComprador());
				compradorDTO.setTipoComprador(formulario.getTipoComprador());
				compradorDTO.setNombreComprador(formulario.getNombreComprador());
//				compradorDTO.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
//				compradorDTO.setUsuarioCreacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());				
				//compradorDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				compradorDTO.setAreaReferencia(formulario.getAreaReferencia());
				
				//llamada al m\u00E9todo del servicio para almacenar el nuevo registro
				SessionManagerSISPE.getServicioClienteServicio().transRegistrarComprador(compradorDTO);
				
				//se agrega a la colecci\u00F3n la lista de registrso obtenidos
				compradores.add(compradorDTO);

				//almacena la lista de temporadas en sesi\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.compradores",compradores);
				LogSISPE.getLog().info("tamano coleccion: " + compradores.size());
				 //se guarda el mensaje de exito en la sesi\u00F3n
		        ControlMensajes controlMensajes = new ControlMensajes();
		        controlMensajes.setMessages(session,"message.exito.registro","El Comprador");
		        session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
				salida = "guardarComprador";
			}
		} catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			messages.add("Comprador",new ActionMessage("errors.llamadaServicio.registrarDatos","el Comprador"));
			saveErrors(request, messages);
		}
		return mapping.findForward(salida);
	}
}
