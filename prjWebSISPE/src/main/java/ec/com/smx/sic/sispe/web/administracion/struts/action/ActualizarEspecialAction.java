/*
 * ActualizarEspecialAction.java
 * Creado el 19/03/2007
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

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.EspecialForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de actualizaci\u00F3n de Especiales
 *  y generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	jacalderon
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class ActualizarEspecialAction extends BaseAction
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
	 * @param form			    El formulario asociado a esta acci\u00F3nde donde se toman y establecen valores de
	 *          				campos
	 * @param request 		    El request que estamos procesando
	 * @param response		    La respuesta HTTP que se crea
	 * @return ActionForward	Describe donde y como se redirige el control
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		//objetos para los mensajes
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		
		HttpSession session = request.getSession();
		EspecialForm formulario = (EspecialForm)form;
		String salida = "desplegar";
		//se obtienen los valores de los estados activo e inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		try
		{
			//verifica si se escogi\u00F3 un registro-------------------------------------------------------------------
			if(request.getParameter("indice")!=null)
			{
				//colecci\u00F3n que almacena los registros de especiales
				ArrayList especialDTOCol = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
				int indice = Integer.parseInt(request.getParameter("indice"));

				//se guarda en sesi\u00F3n el indice de la colecci\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.indice",Integer.toString(indice));
				//recupera el registro a modificar de la colecci\u00F3n
				EspecialDTO especialDTO = (EspecialDTO)especialDTOCol.get(indice);

				//se crea una nueva colecci\u00F3n para que el objeto especialDTO no se vea afectado por las 
				//modificaciones temporales
				Collection especialClasificacionDTOCol = especialDTO.getEspecialesClasificaciones();
				session.setAttribute("ec.com.smx.sic.sispe.especialesClasificaciones", especialClasificacionDTOCol);

				LogSISPE.getLog().info("especialesClasificaciones: " + especialClasificacionDTOCol);

				//se actualizan los datos del formulario
				formulario.setEstadoEspecial(especialDTO.getEstadoEspecial());
				formulario.setDescripcionEspecial(especialDTO.getDescripcionEspecial());
				if(especialDTO.getPublicado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion")))
					formulario.setOpPublicar(estadoActivo);
				formulario.setTipoPedido(especialDTO.getCodigoTipoPedido());
				
				//se guardan los datos en sesi\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.especial.estadoEspecial", formulario.getEstadoEspecial());
				session.setAttribute("ec.com.smx.sic.sispe.especial.descripcionEspecial", formulario.getDescripcionEspecial());

				if(especialClasificacionDTOCol!=null){
					Collection<String> codigosClasificaciones = new ArrayList<String>();
					for(Iterator it=especialClasificacionDTOCol.iterator();it.hasNext();){
						EspecialClasificacionDTO especialClasificacionDTO = (EspecialClasificacionDTO)it.next();
						codigosClasificaciones.add(especialClasificacionDTO.getId().getCodigoClasificacion());
					}
					session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",codigosClasificaciones);
					LogSISPE.getLog().info("LONGITUD CODIGOS: "+codigosClasificaciones.size());
				}

				//se cargan los tipos de pedido
				if(session.getAttribute(EspecialAction.COL_TIPOS_PEDIDO) == null){
					//se carga el listado de pedidos
					TipoPedidoDTO tipoPedidoDTO = new TipoPedidoDTO(Boolean.TRUE);
					tipoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					//lamada al m\u00E9todo del servicio
					Collection <TipoPedidoDTO> colTipoPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoPedido(tipoPedidoDTO);
					session.setAttribute(EspecialAction.COL_TIPOS_PEDIDO, colTipoPedidoDTO);
				}
			}
			/*-------------------------- cuando se desea agregar una clasificaci\u00F3n ----------------------------*/
			else if(formulario.getBotonAgregarClasificacion()!=null){
				//llamada al m\u00E9todo que agrega una clasificaci\u00F3n
				EspecialAction.agregarClasificacion(request, formulario.getCodigoClasificacionEspecial(), 
						"A", infos, errors);
			}
			/*------------ cuando se desea agregar una colecci\u00F3n de clasificaciones -------------*/
			else if(request.getParameter("agregarClasificaciones")!=null){
				//lamada a la funci\u00F3n que realiza la adici\u00F3n
				EspecialAction.agregarClasificaciones(request, "A");
			}
			/*------------------------- si se desactiva o activa una clasificaci\u00F3n --------------------------------*/
			else if(request.getParameter("indiceDesactivacionActivacion")!=null)
			{
				List<EspecialClasificacionDTO> especialClasificaciones = (List<EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
				try{
					//se obtiene el EspecialClasificacionDTO
					EspecialClasificacionDTO especialClasificacionDTO = especialClasificaciones.get(Integer.parseInt(request.getParameter("indiceDesactivacionActivacion")));
					if(especialClasificacionDTO.getEstadoEspecialClasificacion().equals(estadoActivo))
						especialClasificacionDTO.setEstadoEspecialClasificacion(estadoInactivo);
					else{
						especialClasificacionDTO.setEstadoEspecialClasificacion(estadoActivo);
					}
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
				//se actualiza la colecci\u00F3n
				session.setAttribute("ec.com.smx.sic.sispe.especialesClasificaciones",especialClasificaciones);
			}
			/*------------------------- si se elimina una clasificaci\u00F3n --------------------------------*/
			else if(request.getParameter("indiceEliminacion")!=null)
			{
				//se obtienen las clasificaciones
				List<EspecialClasificacionDTO> especialClasificaciones = (List<EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
				try{
					int indice = Integer.parseInt(request.getParameter("indiceEliminacion"));
					//se obtiene el EspecialClasificacionDTO
					EspecialClasificacionDTO especialClasificacionDTO = especialClasificaciones.get(indice);
					especialClasificacionDTO.setEstadoEspecialClasificacion(null);
				}catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
			}
			//cuando se presiona sobre el bot\u00F3n actualizar----------------------------------------------------------------
			else if(formulario.getBotonActualizar()!=null)
			{
				//se obtiene la colecci\u00F3n especiales
				List<EspecialDTO> especialDTOCol = (List<EspecialDTO>)session.getAttribute("ec.com.smx.sic.sispe.tabla");
				try{
					//se obtiene el indice de la colecci\u00F3n  
					int indice = Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.indice").toString());

					EspecialDTO especialDTO = especialDTOCol.get(indice);
					//se construye el nuevo especial
					EspecialAction.construirEspecial(especialDTO, formulario, request);
					
					especialDTO.setUsuarioModificacion(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					
					//llamada al m\u00E9todo del servicio para almacenar el nuevo registro
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarEspecial(especialDTO);
					
					formulario.setBotonActualizar(null);
					//se guarda emensaje de exito 
					ControlMensajes controlMensajes = new ControlMensajes();
					controlMensajes.setMessages(session,"message.exito.actualizacion","El Especial");
					session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");

					//se cargan los datos de los especiales
					EspecialAction.cargarEspeciales(request);
					salida="listado";

				}catch(NumberFormatException ex)
				{
					//formato de n\u00FAmero no v\u00E1lido
					errors.add("errorIndice", new ActionMessage("errors.indiceDetalle.formato"));
				}catch(SISPEException ex)
				{
					//si falla el m\u00E9todo de obtenci\u00F3n de datos
					errors.add("especiales", new ActionMessage("errors.llamadaServicio.registrarDatos", "Especiales"));
					errors.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
			}
			//cancelaci\u00F3n de la acci\u00F3n------------------------------------------------------------------------------------
			else if(formulario.getBotonCancelar()!=null || request.getParameter("volver")!=null)
			{
				//se cargan los datos de los especiales
				//EspecialAction.cargarEspeciales(request);
				salida="listado";
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}

		//se guardan los mensajes
		saveInfos(request, infos);
		saveErrors(request, errors);
		
		return mapping.findForward(salida);
	}
}
