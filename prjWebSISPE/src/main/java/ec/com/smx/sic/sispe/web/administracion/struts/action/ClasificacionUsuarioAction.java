/*
 * ClasificacionUsuarioAction.java
 * Creado el 19/09/2007
 * 
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.frameworkv2.security.dto.UserDto;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionUsuarioDTO;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ClasificacionUsuarioForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Permite controlar la manipulaci\u00F3n de las diferentes configuraciones de clasificaciones 
 * para el acceso de los diferentes usuarios.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5	 	
 */
@SuppressWarnings("unchecked")
public class ClasificacionUsuarioAction extends AdministracionListadoAction
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Mostrar el listado de las configuraciones de Clasificaciones por usuario</li>
   * <li>Acceso a la Creaci\u00F3n de una nueva configuraci\u00F3n de clasificacion</li>
   * <li>Acceso a la Actualizaci\u00F3n de una determinada configuraci\u00F3n de clasificacion</li>
   * </ul>
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request					El request que estamos procesando
   * @param response 				La respuesta HTTP que se crea
   * @throws Exception
   * @return ActionForward	Describe donde y como se redirige el control
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
  	ActionMessages infos = new ActionMessages();
  	ActionMessages success = new ActionMessages();
  	
  	HttpSession session = request.getSession();
  	ClasificacionUsuarioForm formulario = (ClasificacionUsuarioForm) form;
  	String salida = "clasificacionUsuario";
  	
  	//se verifica el valor del par\u00E1metro
  	String peticion = request.getParameter("ayuda");
  	if(peticion!=null && !peticion.equals("")){
  		if(peticion.equals("nuevaClaUsu"))
  			formulario.setNuevaClasificacionUsuario(peticion);
  	}
  	
  	try{
	  	//--------- cuando se escoge crear una nueva configuraci\u00F3n de permisos sobre las clasificaciones
	  	if(formulario.getNuevaClasificacionUsuario()!=null){
	  		//se cargan los usuarios
	  		cargarUsuarios(request);
	  		//se redirecciona a la p\u00E1gina adecuada
	  		salida="nuevaConfiguracionClasificacion";
	  	}
	  	//--------- cuando se escoge actualizar una determinada configuraci\u00F3n de permisos sobre las clasificaciones
	  	else if(request.getParameter("indiceUsuCla")!=null){
	  		int indice = Integer.parseInt(request.getParameter("indiceUsuCla"));
	  		List<ClasificacionUsuarioDTO> resumenUsuariosClasificaciones = (List<ClasificacionUsuarioDTO>)session.getAttribute("ec.com.smx.sic.sispe.colResumenUsuarioClasificacionDTO");
	  		ClasificacionUsuarioDTO clasificacionUsuarioDTO = resumenUsuariosClasificaciones.get(indice);
	  		
	  		//se obtiene el indice desde donde se debe empezar a recorrer la colecci\u00F3n principal
	  		int indiceInicioResumen = clasificacionUsuarioDTO.getNpIndiceClasificacionUsuario().intValue();
	  		String idUsuarioReferencia = clasificacionUsuarioDTO.getId().getUserId();
	  		//se obtiene la colecci\u00F3n principal que contiene todos los registros
	  		List<ClasificacionUsuarioDTO> clasificacionesUsuariosGenerales = (List<ClasificacionUsuarioDTO>)session.getAttribute("ec.com.smx.sic.sispe.colClasificacionUsuarioDTO");
	  		
	  		//se crea la colecci\u00F3n de resultados
	  		Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuario = new ArrayList<ClasificacionUsuarioDTO>();
	  		//se recorre la colecci\u00F3n desde el indice indicado
	  		for(int i=indiceInicioResumen; i<clasificacionesUsuariosGenerales.size();i++){
	  			ClasificacionUsuarioDTO clasificacionUsuarioActualDTO = clasificacionesUsuariosGenerales.get(i);
	  			//se realiza la comparaci\u00F3n de los ids de usuario
	  			if(clasificacionUsuarioActualDTO.getId().getUserId().equals(idUsuarioReferencia))
	  				clasificacionesPorUsuario.add(clasificacionUsuarioActualDTO);
	  			else
	  				break;
	  		}
	  		//se guarda en sesi\u00F3n la colecci\u00F3n
	  		session.setAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario", clasificacionesPorUsuario);
	  		//se asignan los campos al formulario
	  		formulario.setIdUsuario(clasificacionUsuarioDTO.getId().getUserId());
	  		formulario.setNombreUsuario(clasificacionUsuarioDTO.getUser().getUserName());
	  		formulario.setNombreCompletoUsuario(clasificacionUsuarioDTO.getUser().getUserCompleteName());
	  		
	  		//se cargan los usuarios
	  		cargarUsuarios(request);
	  		
	  		//se redirecciona a la p\u00E1gina de actualizaci\u00F3n
	  		salida="actualizarConfiguracionClasificacion";
	  	}

	  	//------- caso por omisi\u00F3n
	  	else{
	  		//condici\u00F3n para que la colecci\u00F3n de usuarios no se pierda cuando
	  		//se regresa a la pantalla principal
	  		Collection <ClasificacionUsuarioDTO> usuarios = null;
	  		if(request.getParameter("accionCancelada")!=null)
	  			usuarios = (Collection <ClasificacionUsuarioDTO>)session.getAttribute("ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO");

	  		//se guardan los mensajes generados desde las pantallas de ingreso si es el caso
	  		ActionMessages mensajes = (ActionMessages)session.getAttribute("ec.com.smx.sic.sispe.mantenimiento.mensajes.exito");
	  		if(mensajes!=null)
	  			success.add(mensajes);
	  		
	  		//se eliminan las variables de sesi\u00F3n
	  		SessionManagerSISPE.removeVarSession(request);
	  		
		  	ClasificacionUsuarioDTO clasificacionUsuarioDTO = new ClasificacionUsuarioDTO(Boolean.TRUE);
		  	clasificacionUsuarioDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  	clasificacionUsuarioDTO.setClasificacion(new ClasificacionDTO());
		  	clasificacionUsuarioDTO.setUser(new UserDto());
		  	Collection <ClasificacionUsuarioDTO> clasificacionesUsuarios = SessionManagerSISPE
		  		.getServicioClienteServicio().transObtenerClasificacionesUsuario(clasificacionUsuarioDTO);
		  	session.setAttribute("ec.com.smx.sic.sispe.colClasificacionUsuarioDTO", clasificacionesUsuarios);
		  	
		  	//si la colecci\u00F3n est\u00E1 vac\u00EDa
		  	if(clasificacionesUsuarios==null || clasificacionesUsuarios.isEmpty()){
		  		infos.add("clasificacionesVacia", new ActionMessage("message.listaVacia","Configuraciones para las Clasificaciones"));
		  	}else{
		  		int contadorUsuarios = 0;
		  		int indiceInicioAgrupacionUsuario = 0;
		  		ClasificacionUsuarioDTO clasificacionUsuarioAnteriorDTO = null;
		  		String estadoClasificacion = SessionManagerSISPE.getEstadoInactivo(request);
		  		Collection <ClasificacionUsuarioDTO> resumenUsuariosClasificaciones = new ArrayList <ClasificacionUsuarioDTO>();
		  		//se crea una nueva colecci\u00F3n para la agrupaci\u00F3n de clasificaciones por usuario
		  		for(ClasificacionUsuarioDTO clasificacionUsuarioActualDTO : clasificacionesUsuarios) {
		  			contadorUsuarios++;
		  			//se realizan las comparaciones
						if(clasificacionUsuarioAnteriorDTO!=null && 
								!clasificacionUsuarioActualDTO.getUser().getUserId().equals(clasificacionUsuarioAnteriorDTO.getUser().getUserId())){
							//se crea el nuevo objeto
							ClasificacionUsuarioDTO nuevaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO();
							nuevaClasificacionUsuarioDTO.getId().setCodigoCompania(clasificacionUsuarioAnteriorDTO.getId().getCodigoCompania());
							nuevaClasificacionUsuarioDTO.getId().setCodigoClasificacion(clasificacionUsuarioAnteriorDTO.getId().getCodigoClasificacion());
							nuevaClasificacionUsuarioDTO.getId().setUserId(clasificacionUsuarioAnteriorDTO.getUser().getUserId());
							nuevaClasificacionUsuarioDTO.setClasificacion(clasificacionUsuarioAnteriorDTO.getClasificacion());
							nuevaClasificacionUsuarioDTO.setUser(clasificacionUsuarioAnteriorDTO.getUser());
							nuevaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(estadoClasificacion);
							nuevaClasificacionUsuarioDTO.setNpIndiceClasificacionUsuario(new Integer(indiceInicioAgrupacionUsuario));
							resumenUsuariosClasificaciones.add(nuevaClasificacionUsuarioDTO);
							//se actualiza el valor del inicio del indice de agrupamiento
							indiceInicioAgrupacionUsuario = contadorUsuarios - 1;
							//se inicializa el estado de la clasificacion
							estadoClasificacion = SessionManagerSISPE.getEstadoInactivo(request);
							if(clasificacionUsuarioActualDTO.getEstadoClasificacionUsuario().equals(SessionManagerSISPE.getEstadoActivo(request)))
								estadoClasificacion = SessionManagerSISPE.getEstadoActivo(request);
							clasificacionUsuarioAnteriorDTO = clasificacionUsuarioActualDTO;
							LogSISPE.getLog().info("se agreg\u00F3 el usuario, indice de inicio: "+nuevaClasificacionUsuarioDTO.getNpIndiceClasificacionUsuario());							
						}else{
							if(clasificacionUsuarioActualDTO.getEstadoClasificacionUsuario().equals(SessionManagerSISPE.getEstadoActivo(request)))
								estadoClasificacion = SessionManagerSISPE.getEstadoActivo(request);
							clasificacionUsuarioAnteriorDTO = clasificacionUsuarioActualDTO;
						}
						
			  		//se almacena la \u00FAltima clasificacion
						if(contadorUsuarios == clasificacionesUsuarios.size()){
							//se crea el nuevo objeto
							ClasificacionUsuarioDTO nuevaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO();
							nuevaClasificacionUsuarioDTO.getId().setCodigoCompania(clasificacionUsuarioActualDTO.getId().getCodigoCompania());
							nuevaClasificacionUsuarioDTO.getId().setCodigoClasificacion(clasificacionUsuarioActualDTO.getId().getCodigoClasificacion());
							nuevaClasificacionUsuarioDTO.getId().setUserId(clasificacionUsuarioActualDTO.getUser().getUserId());
							nuevaClasificacionUsuarioDTO.setClasificacion(clasificacionUsuarioActualDTO.getClasificacion());
							nuevaClasificacionUsuarioDTO.setUser(clasificacionUsuarioActualDTO.getUser());
							nuevaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(estadoClasificacion);
							nuevaClasificacionUsuarioDTO.setNpIndiceClasificacionUsuario(new Integer(indiceInicioAgrupacionUsuario));
							resumenUsuariosClasificaciones.add(nuevaClasificacionUsuarioDTO);
						}
		  			LogSISPE.getLog().info("codigo usuario actual: "+clasificacionUsuarioActualDTO.getUser().getUserId());
	  				LogSISPE.getLog().info("codigo usuario anterior: "+clasificacionUsuarioAnteriorDTO.getUser().getUserId());
					}
		  		//se almacena la colecci\u00F3n de resumen
					session.setAttribute("ec.com.smx.sic.sispe.colResumenUsuarioClasificacionDTO", resumenUsuariosClasificaciones);

		  	}
		  	//se vuelve a asignar la colecci\u00F3n de usuarios
		  	session.setAttribute("ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO", usuarios);
		  	//se crea esta variable para poder agregar clasificaciones por la b\u00FAsqueda
		  	session.setAttribute("ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda", "ok");
	  	}
  	}catch(Exception ex){
  		salida = "errorGlobal";
  		LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
  	}
  	
  	//se guardan los mensajes
  	saveInfos(request, infos);
  	saveMessages(request, success);
  	return mapping.findForward(salida);
  	
  }
  
  /**
   * Carga los usuarios que se pueden configurar
   * @param  request		- Petici\u00F3n HTTP manejada
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  @SuppressWarnings("unchecked")
  public Collection cargarUsuarios(HttpServletRequest request)throws Exception
  {
  	HttpSession session = request.getSession();
  	Collection<VistaEntidadResponsableDTO> colvistaEntidadResponsableDTO = (Collection<VistaEntidadResponsableDTO>)session.getAttribute("ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO");
  	if(colvistaEntidadResponsableDTO == null){
  		//se arma el objeto para la consulta
  		VistaEntidadResponsableDTO consultaVistaEntidadResponsableDTO = new VistaEntidadResponsableDTO();
  		consultaVistaEntidadResponsableDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
  		consultaVistaEntidadResponsableDTO.setTipoEntidadResponsable(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
		
  		colvistaEntidadResponsableDTO = SessionManagerSISPE.getServicioClienteServicio()
				.transObtenerVistaEntidadResponsable(consultaVistaEntidadResponsableDTO);
  		
  		//se llama al m\u00E9todo que genera la colecci\u00F3n de entidades responsables sin usuarios duplicados, por razones del rol
  		colvistaEntidadResponsableDTO = WebSISPEUtil.agruparEntidadesResponsablesPorUsuario(colvistaEntidadResponsableDTO);
  		
			//se guarda en sesi\u00F3n
  		session.setAttribute("ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO",
  				colvistaEntidadResponsableDTO);
  	}
		return colvistaEntidadResponsableDTO;
  }
  
  /**
   * Guarda la nueva configuraci\u00F3n de los permisos sobre una clasificaci\u00F3n
   * @param  request		- Petici\u00F3n HTTP manejada
   * @param  idUsuario	
   * @param  errors			
   * @param  success		
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  public static boolean guardarNuevaConfiguracionUsuario(HttpServletRequest request, String idUsuario,
  		ActionMessages errors, ActionMessages success)throws Exception{
  	HttpSession session = request.getSession();
  	//se obtiene de sesi\u00F3n la colecci\u00F3n de usuarios por clasificaci\u00F3n
  	Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuario = (Collection <ClasificacionUsuarioDTO>)
  		session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
  	
  	if(clasificacionesPorUsuario !=null){
	  	//se iteran los objetos para asignarles el c\u00F3digo del usuario final
	  	for (ClasificacionUsuarioDTO clasificacionUsuarioDTO : clasificacionesPorUsuario) {
	  		clasificacionUsuarioDTO.getId().setUserId(idUsuario);
	  		clasificacionUsuarioDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());/*Campos de auditoria*/
		}
  	
  		try{
  			//lamada al m\u00E9todo que registra las nuevas configuraciones
  			SessionManagerSISPE.getServicioClienteServicio().transRegistrarClasificacionesUsuario(clasificacionesPorUsuario);
  			success.add("exitoRegistro", new ActionMessage("message.exito.registro", "La configuraci\u00F3n de permisos"));
        //se guarda el mensaje de exito en la sesi\u00F3n porque la proxima redirecci\u00F3n es a una acci\u00F3n 
  	  	request.getSession().setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensajes.exito", success);
  			return true;
  		}catch(SISPEException ex){
  			errors.add("codigoClasificacion", new ActionMessage("errors.SISPEException", ex.getMessage()));
  			return false;
  		}
  	}
  	return false;
  }
  
  /**
   * Guarda la nueva configuraci\u00F3n de los permisos sobre una clasificaci\u00F3n
   * @param  request		- Petici\u00F3n HTTP manejada
   * @param  indices		
   * @param  parametro	
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  public static void eliminarClasificacionUsuario(HttpServletRequest request, 
  		String [] indices, String parametro)throws Exception{
  	//se obtiene la sesi\u00F3n
  	HttpSession session = request.getSession();
  	//se toma de sesi\u00F3n la colecci\u00F3n de usuarios por clasificaciones
  	ArrayList <ClasificacionUsuarioDTO> clasificacionesPorUsuario = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
  	//se crea esta colecci\u00F3n de respaldo para almacenar los objetos que deben eliminarse
  	Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuarioEliminadas = new ArrayList<ClasificacionUsuarioDTO>();
  	
  	if(clasificacionesPorUsuario!=null && !clasificacionesPorUsuario.isEmpty()){
	  	for(int i=0; i<indices.length; i++){
	  		int indice = Integer.parseInt(indices[i]);
	  		ClasificacionUsuarioDTO clasificacionUsuarioDTO = clasificacionesPorUsuario.get(indice);
	  		//si la eliminaci\u00F3n es la p\u00E1gina de actualizaci\u00F3n
	  		if(parametro.equals("N")){
	  			clasificacionesPorUsuarioEliminadas.add(clasificacionUsuarioDTO);
	  		}else{
	  			if(clasificacionUsuarioDTO.getNpEstadoClasificacionAgregada()!=null){
	  				clasificacionesPorUsuarioEliminadas.add(clasificacionUsuarioDTO);
	  			}else{
	  				clasificacionUsuarioDTO.setEstadoClasificacionUsuario(null);
	  			}
	  		}
	  	}
	  	clasificacionesPorUsuario.removeAll(clasificacionesPorUsuarioEliminadas);
  	}
  	//se actualiza la colecci\u00F3n en sesi\u00F3n
  	session.setAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario", clasificacionesPorUsuario);
  }
  
  /**
   * Guarda la nueva configuraci\u00F3n de los permisos sobre una clasificaci\u00F3n
   * @param  request		- Petici\u00F3n HTTP manejada
   * @param  indices
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  public static void activarDesactivarClasificacionUsuario(HttpServletRequest request, String [] indices)throws Exception{
  	//se obtiene la sesi\u00F3n
  	HttpSession session = request.getSession();
  	//se toma de sesi\u00F3n la colecci\u00F3n de usuarios por clasificaciones
  	ArrayList <ClasificacionUsuarioDTO> clasificacionesPorUsuario = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
  	if(clasificacionesPorUsuario!=null && !clasificacionesPorUsuario.isEmpty()){
	  	for(int i=0; i<indices.length; i++){
	  		int indice = Integer.parseInt(indices[i]);
	  		ClasificacionUsuarioDTO clasificacionUsuarioDTO = clasificacionesPorUsuario.get(indice);
	  		if(clasificacionUsuarioDTO.getEstadoClasificacionUsuario().equals(SessionManagerSISPE.getEstadoActivo(request))){
	  			clasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoInactivo(request));
	  		}else{
	  			clasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
	  		}
	  	}
  	}
  	//se actualiza la colecci\u00F3n en sesi\u00F3n
  	session.setAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario", clasificacionesPorUsuario);
  }
  
  /**
   * Agrega una clasificaci\u00F3n al detalle
   * @param  request							- Petici\u00F3n HTTP manejada
   * @param  codigoClasificacion
   * @param  errors
   * @param  success		
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  public static void agregarClasificacionUsuario(HttpServletRequest request, String codigoClasificacion, ActionMessages errors)throws Exception{
  	//se obtiene la sesi\u00F3n
  	HttpSession session = request.getSession();

  	//se toma de sesi\u00F3n la colecci\u00F3n de usuarios por clasificaciones
  	Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuario = 
  		(Collection <ClasificacionUsuarioDTO>)session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
  	
  	if(clasificacionesPorUsuario == null)
  		clasificacionesPorUsuario = new ArrayList<ClasificacionUsuarioDTO>();
  	
  	
  	//variable bandera para el control de usuarios repetidos
  	boolean exiteClasificacionEnColeccion = false;
  	ClasificacionUsuarioDTO clasificacionUsuarioEncontradoDTO = null;
  	//primero se verifica si el usuario ya est\u00E1 agregado
  	for (ClasificacionUsuarioDTO clasificacionUsuarioDTO : clasificacionesPorUsuario) {
			if(clasificacionUsuarioDTO.getId().getCodigoClasificacion().equals(codigoClasificacion)){
				clasificacionUsuarioEncontradoDTO = clasificacionUsuarioDTO;
				exiteClasificacionEnColeccion = true;
				break;
			}
		}
  	
  	//si el usuario no existe se crea el nuevo objeto
  	if(!exiteClasificacionEnColeccion){
  		//se llama al m\u00E9todo que obtiene la clasificaci\u00F3n en base al c\u00F3digo ingresado
  		//se construye el objeto clasificacionDTO para la consulta
    	ClasificacionDTO consultaClasificacionDTO = new ClasificacionDTO();
    	consultaClasificacionDTO.getId().setCodigoClasificacion(codigoClasificacion);
    	consultaClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
    	consultaClasificacionDTO.setEstadoClasificacion(SessionManagerSISPE.getEstadoActivo(request));
    	Collection clasificaciones = SessionManagerSISPE.getServicioClienteServicio()
    		.transObtenerClasificacion(consultaClasificacionDTO);
    	
    	//se verifica los datos obtenidos
    	if(!clasificaciones.isEmpty()){
    		//se itera la unica clasificaci\u00F3n obtenida
    		for(Iterator <ClasificacionDTO> it = clasificaciones.iterator(); it.hasNext();){
	    		ClasificacionDTO clasificacionDTO = it.next();
		    	
		  		ClasificacionUsuarioDTO nuevaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO();
		  		nuevaClasificacionUsuarioDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  		nuevaClasificacionUsuarioDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());	  		
		  		nuevaClasificacionUsuarioDTO.setClasificacion(clasificacionDTO);
		  		nuevaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
		  		nuevaClasificacionUsuarioDTO.setNpEstadoClasificacionAgregada("OK");
		  		
		  		clasificacionesPorUsuario.add(nuevaClasificacionUsuarioDTO);
    		}
	  		//se genera un mensaje de exito
	  		//success.add("clasificacionAgregada", new ActionMessage("message.exito.itemAgregado", "La Clasificaci\u00F3n"));
	  		request.setAttribute("clasificacionAgregada","ok");
    	}else{
				//se genera un mensaje de error
				errors.add("clasificacion", new ActionMessage("message.codigo.invalido","una clasificaci\u00F3n"));
    	}
  	}else{
  		if(clasificacionUsuarioEncontradoDTO!=null){
  			if(clasificacionUsuarioEncontradoDTO.getEstadoClasificacionUsuario()==null){
  				clasificacionUsuarioEncontradoDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
    			//se genera un mensaje de exito
    			//success.add("clasificacionAgregada", new ActionMessage("message.exito.itemAgregado", "La Clasificaci\u00F3n"));
  			}else{
  				//se genera un mensaje de error
  				errors.add("clasificacion", new ActionMessage("errors.itemRepetido", "La Clasificaci\u00F3n"));
  			}
  		}
  	}
  	//se actualiza la colecci\u00F3n en sesi\u00F3n
  	session.setAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario", clasificacionesPorUsuario);
  }
  
  /**
   * Agrega un conjunto de clasificaciones al detalle
   * @param  request							- Petici\u00F3n HTTP manejada
   * @param  codigoClasificacion
   * @param  errors		
   * @return colecci\u00F3n de objetos <code>VistaEntidadResponsableDTO</code> donde se encuentran los datos del usuario
   */
  public static void agregarClasificacionesUsuario(HttpServletRequest request,
  		Collection <ClasificacionDTO> clasificaciones)throws Exception{
  	
  	//se obtiene la sesi\u00F3n
  	HttpSession session = request.getSession();

  	//se toma de sesi\u00F3n la colecci\u00F3n de usuarios por clasificaciones
  	Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuario = 
  		(Collection <ClasificacionUsuarioDTO>)session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
  	
  	if(clasificacionesPorUsuario == null)
  		clasificacionesPorUsuario = new ArrayList<ClasificacionUsuarioDTO>();
  	
  	int contClasificacionesNoAgregadas = 0;
  	
  	for(Iterator <ClasificacionDTO> it = clasificaciones.iterator(); it.hasNext();)
  	{
  		ClasificacionDTO clasificacionDTO = it.next();
  		
    	//variable bandera para el control de usuarios repetidos
    	boolean exiteClasificacionEnColeccion = false;
    	ClasificacionUsuarioDTO clasificacionUsuarioEncontradoDTO = null;
    	//primero se verifica si el usuario ya est\u00E1 agregado
    	for (ClasificacionUsuarioDTO clasificacionUsuarioDTO : clasificacionesPorUsuario) {
  			if(clasificacionUsuarioDTO.getId().getCodigoClasificacion().equals(clasificacionDTO.getId().getCodigoClasificacion())){
  				clasificacionUsuarioEncontradoDTO = clasificacionUsuarioDTO;
  				exiteClasificacionEnColeccion = true;
  				contClasificacionesNoAgregadas++;
  				break;
  			}
  		}

    	//si el usuario no existe se crea el nuevo objeto
    	if(!exiteClasificacionEnColeccion){
    		//se verifica los datos obtenidos
    		ClasificacionUsuarioDTO nuevaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO();
    		nuevaClasificacionUsuarioDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
    		nuevaClasificacionUsuarioDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());	  		
    		nuevaClasificacionUsuarioDTO.setClasificacion(clasificacionDTO);
    		nuevaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
    		nuevaClasificacionUsuarioDTO.setNpEstadoClasificacionAgregada("OK");

    		clasificacionesPorUsuario.add(nuevaClasificacionUsuarioDTO);
    	}else	if(clasificacionUsuarioEncontradoDTO!=null 
    			&& clasificacionUsuarioEncontradoDTO.getEstadoClasificacionUsuario()==null){
    		clasificacionUsuarioEncontradoDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
    	}
  	}
  	
  	//variable para controlar la posici\u00F3n del scroll en la pantalla
		request.setAttribute("clasificacionAgregada","ok");
		
  	//se actualiza la colecci\u00F3n en sesi\u00F3n
  	session.setAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario", clasificacionesPorUsuario);
  }
}
