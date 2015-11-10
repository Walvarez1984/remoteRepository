/*
 * EspecialAction.java
 * Creado el 19/03/2007
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.EspecialForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos del Especial y generar la
 * correcta navegaci\u00F3n sobre la aplicaci\u00F3n.
 * </p>
 * @author 	jacalderon
 * @version 1.1
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class EspecialAction extends BaseAction{
	public final static String COL_TIPOS_PEDIDO = "ec.com.smx.sic.sispe.tiposPedido"; 
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control.
	 * Este m\u00E9todo permite:
	 * <ul>
	 * <li>Mostrar los registros de Especiales </li>
	 * <li>Acceso a la creaci\u00F3n de Nuevos Especiales</li>
	 * <li>Acceso a la actualizaci\u00F3n de los Especiales</li>
	 * </ul>
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form			    El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request			El request que estamos procesando
	 * @param response		    La respuesta HTTP que se crea
	 * @return ActionForward	Describe donde y como se redirige el control
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		HttpSession session = request.getSession();
		String salida = "listado";

		try
		{
			//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
			SessionManagerSISPE.removeVarSession(request);
			
			//llamada al m\u00E9todo del servicio para obtener la lista de registros
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
					MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.especial"));

			//se cargan los datos de los especiales
			Collection<TipoPedidoDTO> especialesDTOCol = cargarEspeciales(request);
			
			session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Especiales");
			session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "especiales48.gif");
			session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "especiales.jsp");

			//se crea esta variable para poder agregar clasificaciones por la b\u00FAsqueda
			session.setAttribute("ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda", "ok");

			if(especialesDTOCol== null || especialesDTOCol.isEmpty()) {
				//se guarda la lista de mensajes en sesi\u00F3n
				ControlMensajes controlMensajes = new ControlMensajes();
				controlMensajes.setMessages(session,"message.listaVacia","Especiales");
				session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
			}

		}
		catch (SISPEException e) {
			//excepci\u00F3n de llamada a m\u00E9todo
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			ControlMensajes controlMensajes = new ControlMensajes();
			controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","el Especial");
			session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","error");
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}

		return mapping.findForward(salida);
	}
	
	/**
	 * Agrega una clasificaci\u00F3n ingresada
	 * 
	 * @param request
	 * @param codigoClasificacion
	 * @param parametroAdicion
	 * @param infos
	 * @param errors
	 */
	public static void agregarClasificacion(HttpServletRequest request, String codigoClasificacion, 
			String parametroAdicion, ActionMessages infos, ActionMessages errors)throws Exception
	{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		
		//se obtienen los codigos de clasificaciones
		ArrayList <String> codigosClasificaciones = (ArrayList <String>)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
		//se obtienen los especiales
		ArrayList <EspecialClasificacionDTO> especialClasificacionDTOCol = (ArrayList <EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
		
		//se verifica las colecciones
		if(codigosClasificaciones == null)
			codigosClasificaciones = new ArrayList<String>();
		if(especialClasificacionDTOCol == null)
			especialClasificacionDTOCol = new ArrayList<EspecialClasificacionDTO>();
		
		if(!codigosClasificaciones.contains(codigoClasificacion)){
			//se construye el objeto clasificacionDTO
			ClasificacionDTO consultaClasificacionDTO = new ClasificacionDTO();
			consultaClasificacionDTO.getId().setCodigoClasificacion(codigoClasificacion);
			consultaClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaClasificacionDTO.setEstadoClasificacion(SessionManagerSISPE.getEstadoActivo(request));
			ArrayList clasificaciones = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(consultaClasificacionDTO);
			if(clasificaciones!=null && !clasificaciones.isEmpty()){
				LogSISPE.getLog().info("tama\u00F1o de las clasificaciones: "+clasificaciones.size());
				ClasificacionDTO clasificacionDTO = (ClasificacionDTO)clasificaciones.get(0);
				//se construye el objeto de las especialClasificacionDTO
				EspecialClasificacionDTO especialClasificacionDTO = new EspecialClasificacionDTO();
				especialClasificacionDTO.setEstadoEspecialClasificacion(SessionManagerSISPE.getEstadoActivo(request));
				especialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				especialClasificacionDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
				especialClasificacionDTO.setClasificacionDTO(clasificacionDTO);
				
				//se agregan los objetos a las colecciones
				especialClasificacionDTOCol.add(especialClasificacionDTO);
				codigosClasificaciones.add(codigoClasificacion);
				
				//se crea una variable request para el control del scroll
				request.setAttribute("clasificacionAgregada", "ok");
			}else{
				//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
				infos.add("clasificacionNoEncontrada",new ActionMessage("message.codigo.invalido","una clasificaci\u00F3n"));
			}
		}else if(parametroAdicion.equals("A")){
			int indiceCodigo = codigosClasificaciones.indexOf(codigoClasificacion);
			EspecialClasificacionDTO especialClasificacionDTO = especialClasificacionDTOCol.get(indiceCodigo);
			if(especialClasificacionDTO.getEstadoEspecialClasificacion()==null)
				especialClasificacionDTO.setEstadoEspecialClasificacion(SessionManagerSISPE.getEstadoActivo(request));
			else{
				//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
				errors.add("clasificacion",new ActionMessage("errors.clasificacionRepetida",codigoClasificacion));
			}
		}else{
			//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
			errors.add("clasificacion",new ActionMessage("errors.clasificacionRepetida",codigoClasificacion));
		}
	}
	
	
	/**
	 * Agrega un conjunto de clasificaciones
	 * 
	 * @param  request
	 * @param  parametroAdicion
	 * @throws Exception
	 */
	public static void agregarClasificaciones(HttpServletRequest request, String parametroAdicion)throws Exception
	{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		
		//se obtienen las clasificaciones agregadas
		Collection <ClasificacionDTO> clasificaciones = (Collection <ClasificacionDTO>)session.getAttribute(CatalogoArticulosAction.CLASIFICACIONES_AGREGADAS);
		//se obtienen los c\u00F3digos de las clasificaciones
		ArrayList <String> codigosClasificaciones = (ArrayList <String>)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
		//se obtienen los especiales
		ArrayList <EspecialClasificacionDTO> especialClasificacionDTOCol = (ArrayList <EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
		
		if(clasificaciones!=null){
			//se verifica las colecciones
			if(codigosClasificaciones == null)
				codigosClasificaciones = new ArrayList<String>();
			if(especialClasificacionDTOCol == null)
				especialClasificacionDTOCol = new ArrayList<EspecialClasificacionDTO>();
			
			//se iteran las clasificaciones
			for(Iterator <ClasificacionDTO> it = clasificaciones.iterator(); it.hasNext();){
				ClasificacionDTO clasificacionDTO = it.next();
				//se verifica si la clasificaci\u00F3n existe en el detalle
				if(!codigosClasificaciones.contains(clasificacionDTO.getId().getCodigoClasificacion())){
					//se construye el objeto especialClasificacionDTO
					EspecialClasificacionDTO especialClasificacionDTO = new EspecialClasificacionDTO();
					especialClasificacionDTO.setEstadoEspecialClasificacion(SessionManagerSISPE.getEstadoActivo(request));
					especialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					especialClasificacionDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
					especialClasificacionDTO.setClasificacionDTO(clasificacionDTO);
					
					//se agregan los objetos a las clasificaciones
					especialClasificacionDTOCol.add(especialClasificacionDTO);
					codigosClasificaciones.add(clasificacionDTO.getId().getCodigoClasificacion());
					
				}else if(parametroAdicion.equals("A")){
					int indiceCodigo = codigosClasificaciones.indexOf(clasificacionDTO.getId().getCodigoClasificacion());
					EspecialClasificacionDTO especialClasificacionDTO = especialClasificacionDTOCol.get(indiceCodigo);
					if(especialClasificacionDTO.getEstadoEspecialClasificacion()==null)
						especialClasificacionDTO.setEstadoEspecialClasificacion(SessionManagerSISPE.getEstadoActivo(request));					
				}
			}
			session.removeAttribute(CatalogoArticulosAction.CLASIFICACIONES_AGREGADAS);
			//se crea una variable request para el control del scroll
			request.setAttribute("clasificacionAgregada", "ok");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws Exception
	 */
	public static Collection<TipoPedidoDTO> cargarEspeciales(HttpServletRequest request)throws Exception{
		//se actualiza la lista de especiales desde la base de datos
		LogSISPE.getLog().info("Actualizando tabla de especiales");
		EspecialDTO consultaEspecialDTO = new EspecialDTO(Boolean.TRUE);
		consultaEspecialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaEspecialDTO.setEspecialesClasificaciones(new ArrayList());
		consultaEspecialDTO.setTipoPedidoDTO(new TipoPedidoDTO());

		//llamada a la capa de servicio
		Collection especialDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerEspecial(consultaEspecialDTO);
		request.getSession().setAttribute(SessionManagerSISPE.LISTADO_DATOS_ADMINISTRACION, especialDTOCol);

		return especialDTOCol;
	}
	
	/**
	 * 
	 * @param especialDTO
	 * @param formulario
	 * @param request
	 * @return
	 */
	public static void construirEspecial(EspecialDTO especialDTO, EspecialForm formulario, HttpServletRequest request){
		especialDTO.setEstadoEspecial(formulario.getEstadoEspecial());
		if(formulario.getOpPublicar()!=null)
			especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		else
			especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion"));
		especialDTO.setDescripcionEspecial(formulario.getDescripcionEspecial());
		especialDTO.setEspecialesClasificaciones((Collection)request.getSession().getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones"));
		especialDTO.setCodigoTipoPedido(formulario.getTipoPedido());
		especialDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		especialDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
	}
}	