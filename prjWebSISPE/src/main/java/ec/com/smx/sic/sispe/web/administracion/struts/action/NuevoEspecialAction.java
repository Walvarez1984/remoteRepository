/*
 * NuevoEspecialAction.java
 * Creado el 19/03/2007
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.EspecialForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de un Nuevo Especial y
 * generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	jacalderon
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class NuevoEspecialAction extends BaseAction{
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control.
	 * Este m\u00E9todo permite:
	 * <ul>
	 * <li>Registrar un Nuevo Especial</li>
	 * </ul>
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request			El request que estamos procesando
	 * @param response		La respuesta HTTP que se crea
	 * @return ActionForward	Describe donde y como se redirige el control
	 * @throws Exception
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception 
	{

		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		
		HttpSession session = request.getSession();
		EspecialForm formulario = (EspecialForm) form;
		//se obtienen los valores de los estados activo e inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		String salida = "desplegar";

		try
		{
			//cuando se presiona sobre el bot\u00F3n nuevo-------------------------------------------------------------------
			if (formulario.getBotonRegistrarNuevo() != null) 
			{
				try {
					//creaci\u00F3n del DTO para crear el nuevo registro
					EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
					especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					
					//se construye el nuevo especial
					EspecialAction.construirEspecial(especialDTO, formulario, request);
				
					//llamada al m\u00E9todo del servicio para almacenar el nuevo registro
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarEspecial(especialDTO);

					//se cargan los datos de los especiales
					EspecialAction.cargarEspeciales(request);

					//se guarda el mensaje de exito en la sesi\u00F3n
					ControlMensajes controlMensajes = new ControlMensajes();
					controlMensajes.setMessages(session,"message.exito.registro","El Especial");
					session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
					salida="listado";
				}
				catch (SISPEException ex) {
					//cuando existe un error en el registro
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("especial", new ActionMessage("errors.llamadaServicio.registrarDatos", "el Especial"));
					errors.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
				}
			}
			/*-------------------------- cuando se desea agragar una clasificaci\u00F3n ----------------------------*/
			else if(formulario.getBotonAgregarClasificacion()!=null)
			{
				//llamada al m\u00E9todo que agrega una clasificaci\u00F3n
				EspecialAction.agregarClasificacion(request, formulario.getCodigoClasificacionEspecial(), 
						"N", infos, errors);
			}
			//------------ cuando se desea agregar una colecci\u00F3n de clasificaci\u00F3n -------------
			else if(request.getParameter("agregarClasificaciones")!=null){
				//lamada a la funci\u00F3n que realiza la adici\u00F3n
				EspecialAction.agregarClasificaciones(request, "N");
			}
			//------------------------- si se desactiva o activa una clasificaci\u00F3n ------------------------------
			else if(request.getParameter("indiceDesactivacionActivacion")!=null)
			{
				//se obtienen las clasificaciones
				ArrayList <EspecialClasificacionDTO> especialClasificaciones = (ArrayList <EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
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
				
				session.setAttribute("ec.com.smx.sic.sispe.especialesClasificaciones",especialClasificaciones);
			}
			/*------------------------- si se elimina una clasificaci\u00F3n --------------------------------*/
			else if(request.getParameter("indiceEliminacion")!=null)
			{
				//se obtienen los codigos de clasificaciones
				ArrayList <String> codigosClasificaciones = (ArrayList <String>)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
				//se obtienen las clasificaciones
				ArrayList <EspecialClasificacionDTO> especialClasificaciones = (ArrayList <EspecialClasificacionDTO>)session.getAttribute("ec.com.smx.sic.sispe.especialesClasificaciones");
				try{
					int indice = Integer.parseInt(request.getParameter("indiceEliminacion"));
					//se elimina la clasificaci\u00F3n
					especialClasificaciones.remove(indice);
					codigosClasificaciones.remove(indice);
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
				}
			}
			//cuando se presiona sobre el bot\u00F3n cancelar----------------------------------------------------------------
			else if (formulario.getBotonCancelar()!=null || request.getParameter("volver")!=null){
				//se cargan los datos de los especiales
				//EspecialAction.cargarEspeciales(request);
				salida = "listado";
			}
			else{
				//crea la colecci\u00F3n que contendr\u00E1 las clasificaciones
				session.setAttribute("ec.com.smx.sic.sispe.especialesClasificaciones",new ArrayList());
				session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",new ArrayList());
				
				if(session.getAttribute(EspecialAction.COL_TIPOS_PEDIDO) == null){
					//se carga el listado de pedidos
					TipoPedidoDTO tipoPedidoDTO = new TipoPedidoDTO(Boolean.TRUE);
					tipoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					//lamada al m\u00E9todo del servicio
					Collection <TipoPedidoDTO> colTipoPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoPedido(tipoPedidoDTO);
					session.setAttribute(EspecialAction.COL_TIPOS_PEDIDO, colTipoPedidoDTO);
				}
			}
		}
		catch(Exception ex){
			//esxepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		
		//se guardan los mensajes generados
		saveInfos(request, infos);
		saveErrors(request, errors);
		
		return mapping.findForward(salida);
	}
}
