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
 * @author fmunoz
 * 
 */
public class NuevoAdminAutorizadoresAction extends BaseAction 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
	throws Exception {

		/*
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		AdministracionAutorizadoresForm formulario = (AdministracionAutorizadoresForm) form;
		Collection usuariosAutorizados=null;
		String salida = "nuevoAdminAutorizadores";
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			//---------------------------------------------------------------------------
			if (ayuda.equals("guardarAdminAutorizadores")) {
				//String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
				String userId = formulario.getNombreUsuario();
				
				Collection adminAutorizadores = null;
				//comprueba si la colecci\u00F3n de administradores no es vac\u00EDa
				if (session.getAttribute("ec.com.smx.sic.sispe.adminAutorizadores") != null)
					//se obtiene la colecci\u00F3n de administradores
					adminAutorizadores = (Collection) session.getAttribute("ec.com.smx.sic.sispe.adminAutorizadores");
				else {
					adminAutorizadores = new ArrayList();
				}
				//establece los campos para el nuevo registro
				//obtengo los usuarios autorizados
				Collection autorizados=(Collection)session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados");
				//Creo una colecci\u00F3n donde voy a guardar todos los autorizadores con sus respectivos autorizados
				Collection autorizadosPorAutorizador=new ArrayList();
				//A cada usuario autorizado le doy el codigo del usuario AUTORIZADOR

				if(autorizados!=null){
					for (Iterator iter = autorizados.iterator(); iter.hasNext();) {
						AutorizadorDTO element = (AutorizadorDTO) iter.next();
						element.getId().setUsuarioAutorizador(userId);
						element.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						element.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());

						LogSISPE.debug("El id del autorizador" + element.getId().getUsuarioAutorizador());
						LogSISPE.debug("El id del autorizado" + element.getId().getUsuarioAutorizado());
						LogSISPE.debug("El estado del autorizado" + element.getEstadoAutorizador());

						autorizadosPorAutorizador.add(element);
					}

					//llama al m\u00E9todo
					for (Iterator iter = autorizadosPorAutorizador.iterator(); iter.hasNext();) {
						AutorizadorDTO element = (AutorizadorDTO) iter.next();
						LogSISPE.debug("El id del autorizador" + element.getId().getUsuarioAutorizador());
						LogSISPE.debug("El id del autorizado" + element.getId().getUsuarioAutorizado());
						LogSISPE.debug("El estado del autorizado" + element.getEstadoAutorizador());
					}
					SessionManagerSISPE.getServicioClienteServicio(request).transRegistrarAutorizador(autorizadosPorAutorizador);

					//establece la lista de AUTORIZADORES con sus respectivos autorizados en sesion
					session.setAttribute("ec.com.smx.sic.sispe.adminAutorizadores", autorizadosPorAutorizador);
					//se guarda emensaje de exito 
					LogSISPE.debug("tamano coleccion: " + adminAutorizadores.size());
					//se guarda el mensaje de exito en la sesi\u00F3n
					ControlMensajes controlMensajes = new ControlMensajes();
					controlMensajes.setMessages(session,"message.exito.registro","La configuraci\u00F3n del nuevo Usuario Autorizador");
					session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
					salida = "guardarAdminAutorizadores";
				}else{
					messages.add("Administrador_Autorizadores",new ActionMessage("errors.listaUsuariosAutorizadosVacia"));
					saveErrors(request, messages);
				}
			}
			//---------------------------------------------------------------------------
			else if(ayuda.equals("agregarUsuario")){
				//obtengo el id del autorizado
				String idUsuario=formulario.getIdUsuarioAutorizado();
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
							//esto solo me sirve para mostrar en la tabla como que el autorizado esta activo 
							//pero no es el verdadero estado del autorizador como tal ya que este estado se modificara
							//mas adelante al momento de guardar :)

							autorizado.setEstadoAutorizador(estadoActivo);
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
								messages.add("Administrador_Autorizadores",new ActionMessage("messages.administracionAutorizadores.usuarioYaAgregado"));
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
			//--------------------------------------------------------------------
			else if (ayuda != null && ayuda.startsWith("eliminar")) {
				ArrayList autorizados = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.usuariosAutorizados");
				try{
					int indice = Integer.parseInt(ayuda.substring("eliminar".length()));
					//se obtiene el EspecialClasificacionDTO
					AutorizadorDTO autorizadorDTO = (AutorizadorDTO) autorizados.get(indice);
					autorizadorDTO.setEstadoAutorizador(null);

					session.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",autorizados);
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					saveErrors(request,messages);
				}

				salida="nuevoAdminAutorizadores";
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
				AutorizadorDTO autorizado = (AutorizadorDTO) usAutorizados.get(indice);

				//preguntamos si el estado del art\u00EDculo es activo
				if (autorizado.getEstadoAutorizador().equals(estadoActivo)) {
					autorizado.setEstadoAutorizador(estadoInactivo);
				} else {
					autorizado.setEstadoAutorizador(estadoActivo);
				}
				usAutorizados.set(indice, autorizado);
				session	.setAttribute("ec.com.smx.sic.sispe.usuariosAutorizados",usAutorizados);
				salida="nuevoAdminAutorizadores";
			}

			LogSISPE.debug("***************************************");
			LogSISPE.debug("***************************************");


		}catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
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
	/*
	public boolean seRepite(Collection usuariosAutorizados, AutorizadorDTO autorizadorDTO){
		//se itera la colecci\u00F3n de usuarios autorizados
		for (Iterator it = usuariosAutorizados.iterator(); it.hasNext();) {
			AutorizadorDTO autDTO = (AutorizadorDTO) it.next();
			//se verifica si el usuario est\u00E1 en la lista
			if(autDTO.getUsuarioAutorizadoDTO().getUserCompleteName().equals(autorizadorDTO.getUsuarioAutorizadoDTO().getUserCompleteName()))
				return true;
		}
		return false;
	}
	*/
}
