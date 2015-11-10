/*
 * Creado el 14/05/2007
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
import ec.com.smx.sic.cliente.mdl.dto.CompradorDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.CompradorForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author mnaranjo
 *
 * 
 */
public class CompradorAction extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		String salida = "listadoCompradores";
		String ayuda = request.getParameter(Globals.AYUDA);
		String indice = request.getParameter("indice");
		CompradorForm formulario = (CompradorForm) form;
		ActionMessages messages = new ActionMessages();
		//saco del properties el tipoComprador
		session.setAttribute("administracion.tipoComprador.interno", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.interno"));
		session.setAttribute("administracion.tipoComprador.externo", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.externo"));

		if (indice != null) {
			session.setAttribute("ec.com.smx.sic.sispe.indiceComprador",indice);
			ArrayList compradores = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.compradores");
			CompradorDTO compradorDTO = (CompradorDTO) compradores.get(Integer.parseInt(indice));
			
			formulario.setNombreComprador(compradorDTO.getNombreComprador());
			formulario.setEstadoComprador(compradorDTO.getEstadoComprador());
			formulario.setTipoComprador(compradorDTO.getTipoComprador());
			formulario.setAreaReferencia(compradorDTO.getAreaReferencia());
			salida = "actualizarComprador";

		} else if (ayuda != null && ayuda.equals("nuevoComprador")) {
			salida = "nuevoComprador";
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
				Collection compradores = new ArrayList();
				CompradorDTO consultaCompradorDTO = new CompradorDTO(Boolean.TRUE);
				consultaCompradorDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//llamada al m\u00E9todo del servicio para obtener los compradores
				compradores = SessionManagerSISPE.getServicioClienteServicio().transObtenerComprador(consultaCompradorDTO);
				
				session.setAttribute("ec.com.smx.sic.sispe.compradores",	compradores);

				//se obtiene un mensaje de exito de la sesi\u00F3n
				salida = "listadoCompradores";
			} catch (Exception ex) {
				salida = "errorGlobal";
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
		}
		return mapping.findForward(salida); //finaliza
	}
}
