/*
 * ActualizarTipoDescuentoAction.java 
 * Creado el 22/06/2006
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.TipoDescuentoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de actualizaci\u00F3n de Tipo 
 * de Descuetos y generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class ActualizarTipoDescuentoAction extends BaseAction
{
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear). Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control.
	 * Este m\u00E9todo permite:
	 * <ul>
	 * <li>Actualizar un Tipo de Descuento</li>
	 * </ul>
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form			El formulario (si lo hay) asociado a esta acci\u00F3nde donde se toman y establecen valores de
	 *          				campos
	 * @param request 		El request que estamos procesando
	 * @param response		La respuesta HTTP que se crea
	 * @return ActionForward	Describe donde y como se redirige el control
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		ActionMessages messages=new ActionMessages();

		HttpSession session = request.getSession();
		TipoDescuentoForm formulario = (TipoDescuentoForm)form;
		//se obtienen los valores de los estados activo e inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		String salida = "desplegar";
		try
		{
			//verifica si se escogi\u00F3 un registro-------------------------------------------------------------------
			if(request.getParameter("indice")!=null)
			{
				//colecci\u00F3n que almacena los registros de tipos de descuento
				ArrayList tipoDescuentos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
				int indice = Integer.parseInt(request.getParameter("indice"));

				//se guarda en sesi\u00F3n el indice de la colecci\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.indice",Integer.toString(indice));
				//recupera el registro a modificar de la colecci\u00F3n
				TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)tipoDescuentos.get(indice);

				//se crea una nueva colecci\u00F3n para que el objeto tipoDescuentoDTO no se vea afectado por las 
				//modificaciones temporales
				Collection tiposDescuentosClasificaciones = tipoDescuentoDTO.getTiposDescuentosClasificaciones();
				session.setAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones", tiposDescuentosClasificaciones);

				LogSISPE.getLog().info("tiposDescuentosClasificaciones: " + tiposDescuentosClasificaciones);

				//se actualizan los datos del formulario
				formulario.setEstadoTipoDescuento(tipoDescuentoDTO.getEstadoTipoDescuento());
				formulario.setDescripcionTipoDescuento(tipoDescuentoDTO.getDescripcionTipoDescuento());
				//se guardan los datos en sesi\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.estadoTipoDescuento", tipoDescuentoDTO.getEstadoTipoDescuento());
				session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcionTipoDescuento", tipoDescuentoDTO.getDescripcionTipoDescuento());

				if(tiposDescuentosClasificaciones==null || tiposDescuentosClasificaciones.isEmpty()){
					//formato den\u00FAmero no v\u00E1lido
					tiposDescuentosClasificaciones = new ArrayList();
					messages.add("clasificaciones", new ActionMessage("message.clasificaciones.listaVacia"));
					saveInfos(request,messages);
					session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",new ArrayList());


				}else{
					Collection codigosClasificaciones = new ArrayList();
					for(Iterator it=tiposDescuentosClasificaciones.iterator();it.hasNext();){
						TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO)it.next();
						codigosClasificaciones.add(tipoDescuentoClasificacionDTO.getId().getCodigoClasificacion());
					}
					session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",codigosClasificaciones);
					LogSISPE.getLog().info("LONGITUD CODIGOS: "+codigosClasificaciones.size());
				}

			}
			/*-------------------------- cuando se desea agragar una clasificaci\u00F3n ----------------------------*/
			else if(formulario.getBotonAgregarClasificacion()!=null)
			{
				//se obtienen los codigos de clasificaciones
				ArrayList codigosClasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
				//se obtienen los tipos de descuentos
				ArrayList tiposDescuentosClasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones");
				if(!codigosClasificaciones.isEmpty() && codigosClasificaciones.contains(formulario.getCodigoClasificacionDescuento()))
				{
					int indiceCodigo = codigosClasificaciones.indexOf(formulario.getCodigoClasificacionDescuento());
					TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO)tiposDescuentosClasificaciones.get(indiceCodigo);
					if(tipoDescuentoClasificacionDTO.getEstadoTipoDescuentoClasificacion()==null)
						tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoActivo);
					else{
						//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
						messages.add("clasificacion",new ActionMessage("errors.clasificacionRepetida",formulario.getCodigoClasificacionDescuento()));
						saveErrors(request,messages);
					}
				}else
				{
					//se construye el objeto clasificacionDTO
					ClasificacionDTO consultaClasificacionDTO = new ClasificacionDTO(Boolean.TRUE);
					consultaClasificacionDTO.getId().setCodigoClasificacion(formulario.getCodigoClasificacionDescuento());
					consultaClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					consultaClasificacionDTO.setEstadoClasificacion(estadoActivo);
					ArrayList clasificaciones = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(consultaClasificacionDTO);
					if(clasificaciones!=null && !clasificaciones.isEmpty()){
						LogSISPE.getLog().info("tama\u00F1o de las clasificaciones: "+clasificaciones.size());
						ClasificacionDTO clasificacionDTO = (ClasificacionDTO)clasificaciones.get(0);
						//se construye el objeto de las tipoDescuentoClasificacionDTO
						TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = new TipoDescuentoClasificacionDTO();
						tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoActivo);
						tipoDescuentoClasificacionDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
						tipoDescuentoClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						tipoDescuentoClasificacionDTO.setClasificacionDTO(clasificacionDTO);
						//primero se verifica si la clasificaci\u00F3n que desea agregar se encuentra en otro especial
						TipoDescuentoClasificacionDTO tipoDescuentoClasificacionConsultado = SessionManagerSISPE.getServicioClienteServicio().transValidarTipoDescuentoClasificacion(tipoDescuentoClasificacionDTO);
						if(tipoDescuentoClasificacionConsultado != null){
							//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
							messages.add("clasificacionRepetida",new ActionMessage("errors.anadir.clasificacion.tipoDescuento",
									formulario.getCodigoClasificacionDescuento(),clasificacionDTO.getDescripcionClasificacion(),
									tipoDescuentoClasificacionConsultado.getId().getCodigoTipoDescuento(), tipoDescuentoClasificacionConsultado.getClasificacionDTO().getDescripcionClasificacion()));
							saveErrors(request,messages);
						}
						else{
							//campos de auditoria
							tipoDescuentoClasificacionDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			            	tipoDescuentoClasificacionDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			            	
							tiposDescuentosClasificaciones.add(tipoDescuentoClasificacionDTO);
							codigosClasificaciones.add(formulario.getCodigoClasificacionDescuento());
						}

					}else{
						//si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
						messages.add("clasificacionNoEncontrada",new ActionMessage("message.codigo.invalido","una clasificaci\u00F3n"));
						saveInfos(request,messages);
					}
				}
			}
			/*------------------------- si se desactiva o activa una clasificaci\u00F3n --------------------------------*/
			else if(request.getParameter("indiceDesactivacionActivacion")!=null)
			{
				ArrayList clasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones");
				try{
					//se obtiene el TipoDescuentoClasificacionDTO
					TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO)
					clasificaciones.get(Integer.parseInt(request.getParameter("indiceDesactivacionActivacion")));
					if(tipoDescuentoClasificacionDTO.getEstadoTipoDescuentoClasificacion().equals(estadoActivo))
						tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoInactivo);  
					else
						tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoActivo);  
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					saveErrors(request,messages);
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
				//se recuperan los datos
				formulario.setEstadoTipoDescuento((String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.estadoTipoDescuento"));
				formulario.setDescripcionTipoDescuento((String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcionTipoDescuento"));
			}
			/*------------------------- si se elimina una clasificaci\u00F3n --------------------------------*/
			else if(request.getParameter("indiceEliminacion")!=null)
			{
				ArrayList clasificaciones = new ArrayList((ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones"));
				ArrayList codigosClasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
				try{
					int indice = Integer.parseInt(request.getParameter("indiceEliminacion"));
					//se obtiene el TipoDescuentoClasificacionDTO
					TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO) clasificaciones.get(indice);
					if(tipoDescuentoClasificacionDTO.getId().getCodigoTipoDescuento()!=null){
						LogSISPE.getLog().info("*1");
						tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(null);
					}
					else{
						LogSISPE.getLog().info("*2");
						clasificaciones.remove(indice);
						codigosClasificaciones.remove(indice);
					}
					session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",codigosClasificaciones);
					session.setAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones",clasificaciones);
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					saveErrors(request,messages);
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
				//se recuperan los datos
				formulario.setEstadoTipoDescuento((String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.estadoTipoDescuento"));
				formulario.setDescripcionTipoDescuento((String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcionTipoDescuento"));
			}
			//cuando se presiona sobre el bot\u00F3n actualizar----------------------------------------------------------------
			else if(formulario.getBotonActualizar()!=null)
			{
				//se obtiene la colecci\u00F3n tipo de descuentos
				ArrayList tiposDescuento = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
				try{
					//se obtiene el indice de la colecci\u00F3n  
					int indice = Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.indice").toString());

					TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)tiposDescuento.get(indice);
					tipoDescuentoDTO.setEstadoTipoDescuento(formulario.getEstadoTipoDescuento());  
					tipoDescuentoDTO.setDescripcionTipoDescuento(formulario.getDescripcionTipoDescuento());
					tipoDescuentoDTO.setTiposDescuentosClasificaciones((Collection)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones"));
					tipoDescuentoDTO.setUsuarioModificacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					//llamada al m\u00E9todo del servicio para almacenar el nuevo registro
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarTipoDescuento(tipoDescuentoDTO);
					formulario.setBotonActualizar(null);
					//se guarda emensaje de exito 
					ControlMensajes controlMensajes = new ControlMensajes();
					controlMensajes.setMessages(session,"message.exito.actualizacion","El Tipo de Descuento");
					session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");

					//se actualiza la lista de tipos de descuentos desde la base de datos
					LogSISPE.getLog().info("Actualizando los tipos de descuentos");
					TipoDescuentoDTO consultaTipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
					consultaTipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					consultaTipoDescuentoDTO.setTiposDescuentosClasificaciones(new ArrayList());
					//llamada a la capa de servicio
					tiposDescuento = (ArrayList)SessionManagerSISPE.getServicioClienteServicio()
					.transObtenerTipoDescuento(consultaTipoDescuentoDTO);
					session.setAttribute("ec.com.smx.sic.sispe.tabla", tiposDescuento);

					salida="listado";

				}catch(NumberFormatException ex)
				{
					//formato den\u00FAmero no v\u00E1lido
					messages.add("errorIndice", new ActionMessage("errors.indiceDetalle.formato"));
					saveErrors(request, messages);
				}catch(SISPEException ex)
				{
					//si falla el m\u00E9todo de obtenci\u00F3n de datos
					messages.add("tipoDescuentos", new ActionMessage("errors.llamadaServicio.registrarDatos", "Tipo Descuentos"));
					messages.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
					saveErrors(request, messages);
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
			}
			//cancelaci\u00F3n de la acci\u00F3n------------------------------------------------------------------------------------
			else if(formulario.getBotonCancelar()!=null || request.getParameter("volver")!=null)
			{
				LogSISPE.getLog().info("SE CANCELO");
				//se actualiza la lista de tipos de descuentos desde la base de datos
				LogSISPE.getLog().info("Actualizando los tipos de descuentos");
				TipoDescuentoDTO consultaTipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
				consultaTipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaTipoDescuentoDTO.setTiposDescuentosClasificaciones(new ArrayList());
				//llamada a la capa de servicio
				Collection tiposDescuento = SessionManagerSISPE.getServicioClienteServicio()
					.transObtenerTipoDescuento(consultaTipoDescuentoDTO);
				session.setAttribute("ec.com.smx.sic.sispe.tabla", tiposDescuento);
				salida="listado";
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}

		return mapping.findForward(salida);
	}
}
