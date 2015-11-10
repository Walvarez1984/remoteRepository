/*
 * Creado el 17/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;


/**
 * @author mnaranjo
 *
 */
public class ActualizarAdminAutorizadoresAction extends BaseAction 
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
			HttpServletResponse response)throws Exception	{

		/*
		Collection usuariosAutorizados=null;
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		AdministracionAutorizadoresForm formulario = (AdministracionAutorizadoresForm) form;
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		String salida = "actualizarAdminAut";
		String ayuda = "";
		if(request.getParameter(Globals.AYUDA)!=null){
			ayuda = request.getParameter(Globals.AYUDA);
		}
		
		try{
			if(ayuda.equals("guardarAdminAutorizadores")){
				//Obtengo el codigo del AUTORIZADOR que estoy editando.
				AutorizadorDTO autorizadorDTOEditar = (AutorizadorDTO)session.getAttribute("ec.com.smx.sic.sispe.autorizadorDTOEditar");

				//String idAutorizador=request.getParameter("nombreUsuario");
				LogSISPE.debug("El autorizador es: "+autorizadorDTOEditar.getIdUsuarioAutorizador());
				//se obtiene la colecci\u00F3n autorizadores
				ArrayList adminAutorizadores = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.adminAutorizadores");

				//se obtiene el indice de la colecci\u00F3n
				int indice = Integer.parseInt((String)session.getAttribute("ec.com.smx.sic.sispe.indiceAdminAutorizadores"));

				//Obtengo el AUTORIZADOR que voy a editar
				AutorizadorDTO autorizadorDTO = (AutorizadorDTO) adminAutorizadores.get(indice);

				//establecimiento de datos a actualizar
				autorizadorDTO.getUsuarioAutorizadorDTO().setUserName(formulario.getNombreUsuario());

				autorizadorDTO.setEstado(formulario.getEstadoAutorizador());

				//String userId=request.getParameter("idUsuarioAutorizado");
				//autorizadorDTO.getId().setUsuarioAutorizador(idAutorizador);

				//obtengo los usuarios autorizados
				Collection autorizados=(Collection)session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados");
				//Creo una colecci\u00F3n donde voy a guardar todos los autorizadores con sus respectivos autorizados
				Collection autorizadosPorAutorizador=new ArrayList();
				//A cada usuario autorizado le doy el codigo del usuario AUTORIZADOR

				for (Iterator iter = autorizados.iterator(); iter.hasNext();) {
					AutorizadorDTO element = (AutorizadorDTO) iter.next();
					element.getId().setUsuarioAutorizador(autorizadorDTOEditar.getId().getUsuarioAutorizador());
					element.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					element.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());

					autorizadosPorAutorizador.add(element);
				}
				LogSISPE.debug("si esta cambia");
				//llama al m\u00E9todo
				SessionManagerSISPE.getCorpAutorizacitonesServicio(request).transRegistrarAutorizador(autorizadosPorAutorizador);

				//establece la lista de AUTORIZADORES con sus respectivos autorizados en sesion
				session.setAttribute("ec.com.smx.sic.sispe.adminAutorizadores", autorizadosPorAutorizador);
				//se guarda emensaje de exito 
				ControlMensajes controlMensajes = new ControlMensajes();
				controlMensajes.setMessages(session,"message.exito.actualizacion","El Administrador");
				session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");

				//messages.add("exito",new ActionMessage("message.exito.actualizacion","El administrador de autorizadores"));
		    //    saveMessages(request,messages);
				salida = "guardarActualizacionAdminAut";
			}

			else if(ayuda.equals("agregarUsuario")){

				//obtengo el id del autorizado
				String idUsuario = formulario.getIdUsuarioAutorizado();
				//verifico que haya seleccionado un usuario
				if(!idUsuario.equals("sinValor")){
					LogSISPE.debug("***************************************");
					LogSISPE.debug("****************Estoy en el nuevo***********************");
					LogSISPE.debug("El id del autorizado es: " + idUsuario);
					LogSISPE.debug("***************************************");
					LogSISPE.debug("***************************************");
					//creo un objeto autorizado
					AutorizadorDTO autorizado = new AutorizadorDTO();

					//le doy el id al autorizado
					autorizado.getId().setUsuarioAutorizado(idUsuario);
					//obtengo el array de usuarios
					ArrayList usuarios=(ArrayList)session.getAttribute("ec.com.smx.sic.sispe.nombresUsuarioAutorizados");
					//busco cual usuario es el elegido para ser autorizado
					for (Iterator it = usuarios.iterator(); it.hasNext();)
					{
						VistaEntidadResponsableDTO usuarioSeleccionado = (VistaEntidadResponsableDTO) it.next();

						if(usuarioSeleccionado.getId().getIdUsuario().equals(autorizado.getId().getUsuarioAutorizado())){

							UserDto user= new UserDto();
							user.setUserCompleteName(usuarioSeleccionado.getNombreCompletoUsuario());
							autorizado.setUsuarioAutorizadoDTO(user);
							autorizado.setEstado(estadoActivo);//Por q lo agrega recien.  
							//autorizado.getUsuarioAutorizadoDTO().setUserName(usuarioSeleccionado.getNombreUsuario());
						}
					}
					Collection usuariosExistentes=(Collection)session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados");

					if(usuariosExistentes!=null){
						if(usuariosExistentes.size()==0){
							usuariosAutorizados=new ArrayList();
							usuariosAutorizados.add(autorizado);
						}
						else{
							usuariosAutorizados=usuariosExistentes;
							if(!seRepite(usuariosAutorizados,autorizado)){
								usuariosAutorizados.add(autorizado);
							}
							else{
								messages.add("Administrador_Autorizadores",new ActionMessage("errors.usuarioAutorizadoRepetido"));
								saveErrors(request, messages);
							}
						}
					}
					else{
						usuariosAutorizados=new ArrayList();
						usuariosAutorizados.add(autorizado);
					}
					session.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",usuariosAutorizados);
				}
				else{
					messages.add("Administrador_Autorizadores",new ActionMessage("errors.requerido","Usuario"));
					saveErrors(request, messages);
				}
			}
			
			else if(ayuda.startsWith("eliminar")) {

				ArrayList autorizados = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados");
				try{
					int indice = Integer.parseInt(ayuda.substring("eliminar".length()));
					//se obtiene el EspecialClasificacionDTO
					AutorizadorDTO autorizadorDTO = (AutorizadorDTO) autorizados.get(indice);
					autorizadorDTO.setEstado(null);

					session.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",autorizados);
				} catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					saveErrors(request,messages);
				}
				salida = "actualizarAdminAut";

			}
			else if (ayuda != null && (ayuda.startsWith("desactivar") || ayuda.startsWith("activar"))) {
				//obtener el \u00EDndice de desactivaci\u00F3n a partir de una subcadena de la ayuda
				int indice = -1;

				//si el  usuario quiere desactivar
				if(ayuda.startsWith("desactivar")){
					indice = Integer.parseInt(ayuda.substring("desactivar".length()));
				}
				//el usuario quiere activar el art\u00EDculo
				else{
					indice = Integer.parseInt(ayuda.substring("activar".length()));
				}

				ArrayList usAutorizados = new ArrayList((Collection) session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados"));
				//O
				AutorizadorDTO autorizado = (AutorizadorDTO) usAutorizados.get(indice);

				//preguntamos si el estado del art\u00EDculo es activo
				if (autorizado.getEstado().equals(estadoActivo)) {
					autorizado.setEstado(estadoInactivo);
				} else {
					autorizado.setEstado(estadoActivo);
				}

				//cambiamos en la lista de frecuencia articulos y subimos a la
				// sesi\u00F3n
				usAutorizados.set(indice, autorizado);
				session	.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",usAutorizados);
				salida = "actualizarAdminAut";
			}
			else{
				//se obtiene el indice de la colecci\u00F3n

				Collection autorizados = null;

				ArrayList adminAutorizadores = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.adminAutorizadores");

				int indice = Integer.parseInt((String)session.getAttribute("ec.com.smx.sic.sispe.indiceAdminAutorizadores"));

				AutorizadorDTO autorizadorDTO = (AutorizadorDTO) adminAutorizadores.get(indice);

				
				autorizados = SessionManagerSISPE.getServicioClienteServicio(request).transObtenerAutorizados(autorizadorDTO);

				session.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",autorizados);

				salida = "actualizarAdminAut";
			}

		}catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			LogSISPE.debug("*******************************");
			LogSISPE.debug("Entro en la excepcion:");
			messages.add("Administrador_Autorizadores",new ActionMessage("errors.llamadaServicio.registrarDatos","el Administrador de Autorizadores"));

			saveErrors(request, messages);
		}
		return mapping.findForward(salida);
		*/
		
		return null;
	}

	/*
	 * Verifica si el usuario que se desea agregar a la lista de usuarios autorizados ya est\u00E1 en la lista.
	 * 
	 * @param 	usuariosAutorizados
	 * @param 	autorizadorDTO
	 * @return	[true: si el usuario autorizado ya est\u00E1 en la lista, false: si el usuario no est\u00E1]
	 */
	/*public boolean seRepite(Collection usuariosAutorizados, AutorizadorDTO autorizadorDTO){
		//se itera la colecci\u00F3n de usuarios autorizados
		for (Iterator it = usuariosAutorizados.iterator(); it.hasNext();) {
			AutorizadorDTO autDTO = (AutorizadorDTO) it.next();
			//se verifica si el usuario est\u00E1 en la lista
			if(autDTO.getUsuarioAutorizadoDTO().getUserCompleteName().equals(autorizadorDTO.getUsuarioAutorizadoDTO().getUserCompleteName()))
				return true;
		}
		return false;
	}*/
}
