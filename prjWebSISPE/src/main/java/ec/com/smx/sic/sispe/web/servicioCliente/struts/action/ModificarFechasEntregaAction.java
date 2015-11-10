/*
 * ModificarFechasEntregaAction.java
 * Creado el 05/08/2009 12:20:38
 *   	
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_ESTADO_DETALLE_PEDIDO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.VISTA_PEDIDO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloUnidadManejoDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloBitacoraCodigoBarrasID;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloID;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings("unchecked")
public class ModificarFechasEntregaAction extends Action
{
	public static final String ENTREGAS_RESPONSABLES= "ec.com.smx.sic.sispe.entregasResp";
	public static final String DETALLELPEDIDOAUX= "ec.com.smx.sic.sispe.detallePedidoAux";
	
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request 				La petici&oacute;n que estamos procesando
   * @param response				La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
  	//posibles mensajes a usar
		ActionMessages errors = new ActionMessages();
		
		String forward = "principal";
		HttpSession session = request.getSession();
		//ModificarFechasEntregaForm formulario = (ModificarFechasEntregaForm)form;
		
		String peticion = request.getParameter(Globals.AYUDA);
		
		if(request.getParameter("indice") != null){
			//se forma la consulta para obtener las entregas
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)((List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL)).get(Integer.parseInt(request.getParameter("indice")));
			EstadoDetallePedidoDTO estadoDetallePedidoFiltro = new EstadoDetallePedidoDTO();
			estadoDetallePedidoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			estadoDetallePedidoFiltro.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			estadoDetallePedidoFiltro.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			estadoDetallePedidoFiltro.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			ArticuloDTO articuloDTO = new ArticuloDTO();
//			articuloDTO.setArticuloComercialDTO(new ArticuloComercialDTO());
			articuloDTO.setArticuloUnidadManejoCol(new ArrayList<ArticuloUnidadManejoDTO>());
			articuloDTO.getArticuloUnidadManejoCol().add(new ArticuloUnidadManejoDTO());
			DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
			detallePedidoDTO.setArticuloDTO(articuloDTO);
			
			estadoDetallePedidoFiltro.setDetallePedidoDTO(detallePedidoDTO); //obtiene los datos del art\u00EDculo
			
			EntregaDetallePedidoDTO entregaDetallePedidoDTO= new EntregaDetallePedidoDTO();
			entregaDetallePedidoDTO.setEntregaPedidoDTO(new EntregaPedidoDTO());
			
			estadoDetallePedidoFiltro.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>()); //obtiene las entregas de cada detalle
			estadoDetallePedidoFiltro.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
			
			ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoDTO);
			session.setAttribute(COL_ESTADO_DETALLE_PEDIDO, SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoDetallePedido(estadoDetallePedidoFiltro));
			session.setAttribute(VISTA_PEDIDO, vistaPedidoDTO);
			
			//----------PRUEBA
			Collection<EstadoDetallePedidoDTO> detalles = (Collection<EstadoDetallePedidoDTO>)session.getAttribute(COL_ESTADO_DETALLE_PEDIDO);
			//c\u00E1lculo de los bultos
			for(EstadoDetallePedidoDTO estadoDetallePedidoDTO : detalles){
				for(EntregaDetallePedidoDTO entDetPedDTO : estadoDetallePedidoDTO.getEntregaDetallePedidoCol()){
					if(entDetPedDTO.getCantidadDespacho()>0){
						entDetPedDTO.setNpCantidadBultos(UtilesSISPE.calcularCantidadBultos(entDetPedDTO.getCantidadDespacho(), estadoDetallePedidoDTO.getDetallePedidoDTO().getArticuloDTO()));
					}
				}
			}
			//---------------
			
			//para obtener la entregas del pedido - cjui\u00F1a
			List<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
			VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
			consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
			consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());			
			detalleVistaPedido = (List<VistaDetallePedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);			
			session.setAttribute(DETALLELPEDIDOAUX,detalleVistaPedido);			
			obtenerEntregas(session);
			
			LogSISPE.getLog().info("se consult\u00F3 las entregas");
		}else if(peticion != null && peticion.equals("guardar")){
			LogSISPE.getLog().info("ingresa a guardar");
			try{
				List<EstadoDetallePedidoDTO> detalle = (List<EstadoDetallePedidoDTO>)request.getSession().getAttribute(COL_ESTADO_DETALLE_PEDIDO);
				for(EstadoDetallePedidoDTO detalleDTO : detalle){
					detalleDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					DetallePedidoDTO detallePedidoDTO = detalleDTO.getDetallePedidoDTO();
					LogSISPE.getLog().info("Articulo y sus entregas--{}",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
					for(EntregaDetallePedidoDTO entDetPedDTO : detalleDTO.getEntregaDetallePedidoCol()){
						LogSISPE.getLog().info("Fecha Entrega cliente--{}",entDetPedDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
						LogSISPE.getLog().info("Fecha Despacho Bodega--{}",entDetPedDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
						LogSISPE.getLog().info("Direccion--{}",entDetPedDTO.getEntregaPedidoDTO().getDireccionEntrega());
						if(entDetPedDTO.getEntregaPedidoDTO().getNpFechaEntregaInicial() != null && entDetPedDTO.getEntregaPedidoDTO().getNpFechaDespachoInicial() != null){
							LogSISPE.getLog().info("Fecha Entrega clienteO--{}",entDetPedDTO.getEntregaPedidoDTO().getNpFechaEntregaInicial());
							LogSISPE.getLog().info("Fecha Despacho BodegaO--{}",entDetPedDTO.getEntregaPedidoDTO().getNpFechaDespachoInicial());
						}
					}	
				}
				SessionManagerSISPE.getServicioClienteServicio().transModificarFechasDespachoYEntrega((Collection<EstadoDetallePedidoDTO>)session.getAttribute(COL_ESTADO_DETALLE_PEDIDO), 
						SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				//mensaje de exito para el registro
				ControlMensajes controlMensajes = new ControlMensajes();
				controlMensajes.setMessages(session,"message.exito.fechasEntregaModificadas");
				session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA,"ok");
				
				session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				forward = "busqueda";
			}catch (SISPEException e) {
				errors.add("registro", new ActionMessage("errors.llamadaServicio.registrarDatos", "la modificaci\u00F3n de fechas"));
				errors.add("dinamico", new ActionMessage("errors.SISPEException", e.getMessage()));
			}
		}else if(peticion != null && peticion.equals("cancelar")){
			LogSISPE.getLog().info("ingresa a cancelar");
			session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA,"ok");
			forward = "busqueda";
		}
		
		saveErrors(request, errors);
		return mapping.findForward(forward);
	}
    
  	/**
	 * Obtiene la configuraci\u00F3n de entregas del pedido
	 * @param session
	 */
	public void obtenerEntregas(HttpSession session){
		//cjui\u00F1a
		Collection<EntregaDetallePedidoDTO> entregasResp=new ArrayList<EntregaDetallePedidoDTO>();
		Collection<DetallePedidoDTO> detPedResp=new ArrayList<DetallePedidoDTO>();
		Collection<VistaDetallePedidoDTO> detallePedidoDTOCol=(Collection<VistaDetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
		if(!CollectionUtils.isEmpty(detallePedidoDTOCol)){
			for(VistaDetallePedidoDTO detPed:detallePedidoDTOCol){
				if(!CollectionUtils.isEmpty(detPed.getEntregaDetallePedidoCol())){
					Collection<EntregaDetallePedidoDTO> entregas=(Collection<EntregaDetallePedidoDTO>)detPed.getEntregaDetallePedidoCol();
					if(CollectionUtils.isEmpty(entregasResp)){
						entregasResp=(Collection<EntregaDetallePedidoDTO>)SerializationUtils.clone((Serializable)entregas);						
						for(EntregaDetallePedidoDTO entPed:entregasResp){
							detPedResp=new ArrayList<DetallePedidoDTO>();
							entPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
							dp.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
							dp.getEstadoDetallePedidoDTO().setCantidadEstado(entPed.getCantidadEntrega());
							dp.setNpContadorDespacho(entPed.getCantidadDespacho());
							dp.setNpContadorEntrega(entPed.getCantidadEntrega());
							detPedResp.add(dp);
							entPed.getEntregaPedidoDTO().setNpDetallePedido(detPedResp);
						}
					}else{
						for(EntregaDetallePedidoDTO entDetPed:entregas){
							Boolean existeEntrega=Boolean.FALSE;
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));							
							dp.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
							dp.getEstadoDetallePedidoDTO().setCantidadEstado(entDetPed.getCantidadEntrega());
							dp.setNpContadorDespacho(entDetPed.getCantidadDespacho());
							dp.setNpContadorEntrega(entDetPed.getCantidadEntrega());
							
							for(EntregaDetallePedidoDTO entPedRes:entregasResp){
								Long diferencia= entDetPed.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()-entPedRes.getEntregaPedidoDTO().getFechaEntregaCliente().getTime();
								if(diferencia==0L && entDetPed.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim().equals(entPedRes.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim())){
									entPedRes.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
									existeEntrega=Boolean.TRUE;
									break;
								}
							}
							if(!existeEntrega){
								entDetPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
								entDetPed.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
								entregasResp.add(entDetPed);								
							}
						}
					}
				}
			}
		}
		
		for(EntregaDetallePedidoDTO entPed:entregasResp){
			Long cantidadDespacho = 0L;
			Long cantidadEntrega = 0L;
			for(DetallePedidoDTO detallePedido : entPed.getEntregaPedidoDTO().getNpDetallePedido()){				
				cantidadDespacho = cantidadDespacho + detallePedido.getNpContadorDespacho();
				cantidadEntrega = cantidadEntrega + detallePedido.getNpContadorEntrega();
			}
			entPed.setCantidadDespacho(cantidadDespacho);
			entPed.setCantidadEntrega(cantidadEntrega);
		}		
		session.setAttribute(ENTREGAS_RESPONSABLES,entregasResp);
	}
}
