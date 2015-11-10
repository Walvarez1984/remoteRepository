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
import ec.com.smx.sic.cliente.mdl.dto.CompradorDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.CompradorForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/*
 * Creado el 15/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */

/**
 * @author mnaranjo
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
public class ActualizarCompradorAction extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		CompradorForm formulario = (CompradorForm) form;
		String salida = "actualizarComprador";
		String ayuda = request.getParameter(Globals.AYUDA);
		//saco del properties el tipoComprador
		session.setAttribute("administracion.tipoComprador.interno", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.interno"));
		session.setAttribute("administracion.tipoComprador.externo", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoComprador.externo"));
		
		if(ayuda.equals("guardarComprador")){
			
			LogSISPE.getLog().info("ingreso a actualizar");
			 //se obtiene la colecci\u00F3n compradores
	        ArrayList compradores = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.compradores");
	        try {
	          //se obtiene el indice de la colecci\u00F3n
	          int indice = Integer.parseInt((String)session.getAttribute("ec.com.smx.sic.sispe.indiceComprador"));
	          //DTO Comprador
	          CompradorDTO compradorDTO = (CompradorDTO) compradores.get(indice);
	          //establecimiento de datos a actualizar
	          compradorDTO.setEstadoComprador(formulario.getEstadoComprador());
	          compradorDTO.setTipoComprador(formulario.getTipoComprador());
	          compradorDTO.setNombreComprador(formulario.getNombreComprador());
//	          compradorDTO.setFechaActualizacion(new Timestamp(System.currentTimeMillis()));
//	          compradorDTO.setUsuarioActualizacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
	          compradorDTO.setAreaReferencia(formulario.getAreaReferencia());
	          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
	          SessionManagerSISPE.getServicioClienteServicio().transRegistrarComprador(compradorDTO);
	          
	          //establece la lista de registros en sesi\u00F3n
	          session.setAttribute("ec.com.smx.sic.sispe.compradores", compradores);
	          //se guarda emensaje de exito
	          ControlMensajes controlMensajes = new ControlMensajes();
	          controlMensajes.setMessages(session,"message.exito.actualizacion","El Comprador");
	          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
	          salida = "guardarActualizacionComprador";
	        }
	        catch (NumberFormatException e) {
	          //si falla el m\u00E9todo de obtenci\u00F3n de datos
	          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
	          messages.add("comprador", new ActionMessage("errors.llamadaServicio.registrarDatos","el Comprador"));
	          saveErrors(request, messages);
	        }
		}
		return mapping.findForward(salida);
	}
}
