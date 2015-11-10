/*
 * Creado el 17/05/2007
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author mnaranjo
 * 
 */
public class AdministracionAutorizadoresAction extends BaseAction {
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
			HttpServletResponse response)throws Exception {

		/*
		HttpSession session = request.getSession();
		ActionMessages messages = new ActionMessages();
		String salida = "listadoAdminAutorizadores";
		String ayuda = request.getParameter(Globals.AYUDA);
		String indice = request.getParameter("indice");
		AdministracionAutorizadoresForm formulario = (AdministracionAutorizadoresForm) form;
		String combo = "";

		session.setAttribute("ec.com.smx.sic.sispe.vacioAutorizadores",SessionManagerSISPE.getEstadoActivo(request));

		if (indice != null) 
		{
			try {
				//carga el combo donde se muestran todos los usuarios que puedo agregar
				cargarCombosUsuarios(request);
				//sube a session el indice del administrador que va a ser editado
				session.setAttribute("ec.com.smx.sic.sispe.indiceAdminAutorizadores",indice);
				//obtengo toda la lista de adminAutorizadores
				ArrayList adminAutorizadores = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.adminAutorizadores");
				//obtengo el adminAutorizador elegido
				AutorizadorDTO autorizadorDTOEditar = (AutorizadorDTO) adminAutorizadores.get(Integer.parseInt(indice));
				//subo al formulario los campos escogidos
				formulario.setNombreUsuario(autorizadorDTOEditar.getUsuarioAutorizadorDTO().getUserCompleteName());
				formulario.setEstadoAutorizador(autorizadorDTOEditar.getEstado());

				//formulario.setLocal(vistaEntidadResponsableDTO.getCodigoLocal().toString());
				session.setAttribute("ec.com.smx.sic.sispe.autorizadorDTOEditar",autorizadorDTOEditar);
				
				session.setAttribute("ec.com.smx.sic.sispe.localActual",SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo()
						+ " - "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
				
				formulario.setLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString());
				VistaUsuariosAutorizadosEST vistaUsuarios=new VistaUsuariosAutorizadosEST(new VistaUsuariosAutorizadosDTO());
				vistaUsuarios.setValoresUsuariosAutorizados(autorizadorDTOEditar.getCodigoCompania(), autorizadorDTOEditar.getCodigoSistema(), autorizadorDTOEditar.getCodigoLocal().toString(), autorizadorDTOEditar.getIdUsuarioAutorizador());
				VistaUsuariosAutorizadosEST vistaUsuariosAutorizadosEST= SessionManagerSISPE.getCorpAutorizacitonesServicio(request).obtenerUsuariosAutorizadosEST(vistaUsuarios);
				 Collection autorizados=vistaUsuariosAutorizadosEST.getRegistros();
				 session.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",autorizados);
				
			} catch (Exception ex) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
			salida = "actualizarAdminAutorizadores";

		} 
		else if (ayuda != null && ayuda.equals("nuevoAdminAutorizadores")) {
			
			try{
				//llamada a las funciones que cargan los combos de usuarios
				//cargarCombosUsuarioNoAutorizadores(request);
				cargarCombosUsuarios(request);
				
				String tipoEntidadResponsable = SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable();
				String tipoEntidadResponsableLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				if(!tipoEntidadResponsable.equals(tipoEntidadResponsableLocal)){
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
					combo = SessionManagerSISPE.getEstadoActivo(request);
					session.setAttribute("ec.com.smx.sic.sispe.combo", combo);
				}else{
					session.setAttribute("ec.com.smx.sic.sispe.localActual",SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo()
							+ " - "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
					formulario.setLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString());
					combo = SessionManagerSISPE.getEstadoInactivo(request);
					session.setAttribute("ec.com.smx.sic.sispe.combo", combo);
				}
				salida = "nuevoAdminAutorizadores";
			}catch(Exception ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
		} 
		else {
			//*************************************************************
			try{
				//se obtiene un mensaje de exito de la sesi\u00F3n
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
				
				SessionManagerSISPE.removeVarSession(request);
				//colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
				Collection adminAutorizadores = new ArrayList();
				
				//aqui tengo que cambiar el m\u00E9todo porque ahora debo utilizar un metodo que me devuelva todos los autorizadores.
				AutorizadorDTO autorizadorDTO = new AutorizadorDTO();
				autorizadorDTO.setEstado(SessionManagerSISPE.getEstadoActivo(request));
				autorizadorDTO.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				autorizadorDTO.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				autorizadorDTO.setIdUsuarioAutorizador(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				
				LogSISPE.debug("**************************************");
				LogSISPE.debug("**************************************");
				LogSISPE.debug("El codigo del local es: "+autorizadorDTO.getCodigoLocal());
				AutorizadorEST autorizadorEST=new AutorizadorEST(autorizadorDTO);
				autorizadorEST = SessionManagerSISPE.getCorpAutorizacitonesServicio(request).obtenerAutorizadorEST(autorizadorEST);
				 adminAutorizadores=autorizadorEST.getRegistros();
				session.setAttribute("ec.com.smx.sic.sispe.adminAutorizadores",	adminAutorizadores);
				
				LogSISPE.debug("**************************************");
				LogSISPE.debug("**************************************");
				LogSISPE.debug("La longitud es "+adminAutorizadores.size());
				
				if(adminAutorizadores.isEmpty()){
					messages.add("Autorizadores",new ActionMessage("messages.administracionAutorizadores.noHayAutorizadores"));
					saveInfos(request,messages);
				}
				//session.setAttribute("ec.com.smx.sic.sispe.local",SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				//ec.com.smx.sic.sispe.estado.activo
				//tabla = SessionManagerSISPE.getEstadoActivo(request);
				salida = "listadoAdminAutorizadores";
				//*************************************************************
			}catch(Exception ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
		}
        session.removeAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
			return mapping.findForward(salida); //finaliza
		*/
		
		return null;
	}
	
	/*
	 * 
	 * @param request
	 */
	/*public void cargarCombosUsuarioNoAutorizadores(HttpServletRequest request){
		
		ActionMessages messages = new ActionMessages();
	  HttpSession session = request.getSession();
	  
		try{
			//se arma el objeto para la consulta
			VistaEntidadResponsableDTO consultaVistaEntidadResponsableDTO = new VistaEntidadResponsableDTO();
			consultaVistaEntidadResponsableDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaVistaEntidadResponsableDTO.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			consultaVistaEntidadResponsableDTO.setNpEstadoUserIdNoAutorizador("1");
			
			Collection colvistaEntidadResponsableDTO = SessionManagerSISPE.getServicioClienteServicio()
				.transObtenerVistaEntidadResponsable(consultaVistaEntidadResponsableDTO);
			
			session.setAttribute("ec.com.smx.sic.sispe.nombresNoAutorizadores",colvistaEntidadResponsableDTO);
			//se verifica si la consulta devolvi\u00F3 datos
			if(colvistaEntidadResponsableDTO.isEmpty()){
			   LogSISPE.debug("entro al infos");
			   messages.add("vacioAutorizadores",new ActionMessage("messages.administracionAutorizadores.noHayAutorizadoresLibres"));
			   saveInfos(request,messages);
			   session.setAttribute("ec.com.smx.sic.sispe.vacioAutorizadores", SessionManagerSISPE.getEstadoInactivo(request));
			}
			
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
	}*/
	
	
	/**
	 * Carga el combo de usuarios habilitados para el local actual.
	 * 
	 * @param request		- La petici\u00F3n HTTP
	 */
	public void cargarCombosUsuarios(HttpServletRequest request){

		HttpSession session = request.getSession();
		try{
			VistaEntidadResponsableDTO consultaVistaEntidadResponsableDTO = new VistaEntidadResponsableDTO();
			consultaVistaEntidadResponsableDTO.setTipoEntidadResponsable(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable());
			consultaVistaEntidadResponsableDTO.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			consultaVistaEntidadResponsableDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			Collection colVistaEntidadResponsableDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaEntidadResponsable(consultaVistaEntidadResponsableDTO);
			session.setAttribute("ec.com.smx.sic.sispe.nombresUsuarioAutorizados",colVistaEntidadResponsableDTO);
			
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
	}

}
