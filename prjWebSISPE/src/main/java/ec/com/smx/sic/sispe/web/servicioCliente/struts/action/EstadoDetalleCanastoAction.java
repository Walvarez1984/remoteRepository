/*
 * Clase EstadoDetalleCanastoAction.java
 * Creado el 23/11/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.common.factory.SICFactory;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloProveedorDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionStockDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetalleRecetaDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase nos permite ver los datos del detalle del canasto cuando el pedido 
 * tomo un determinado estado estado.
 * 
 * @author 	fmunoz
 * @version 2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class EstadoDetalleCanastoAction extends BaseAction
{
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
	 * respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo 
	 * se redirige el control (si la respuesta se ha completado <code>null</code>)
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form				El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request 		La petici&oacute;n que estamos procesando
	 * @param response		La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&aacute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response)throws Exception{
		HttpSession session = request.getSession();
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		ActionMessages errors = new ActionMessages();
		String salida = "estadoDetalleCanasto";
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		Collection detalleCanasto = null;
		int indice=0;
		final String ORIGEN_DETALLE_KARDEX = "kardex";
		final String ORIGEN_DETALLE_PEDIDO = "pedido";
		try{
			
			if(request.getParameter("indiceDetallePedido") != null){
				session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_ELIMINADOS);
				session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_AGREGADOS);
				session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_MODIFICADOS);
				session.removeAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL);
				session.removeAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
				session.removeAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
				LogSISPE.getLog().info("indiceDetallePedido: {}" , request.getParameter("indiceDetallePedido"));
				//se obtiene el indice del detalle de la cotizaci\u00F3n seleccionado
				indice = Integer.parseInt(request.getParameter("indiceDetallePedido"));
				//se obtiene de sesi\u00F3n la colecci\u00F3n con el estado del detalle del pedido
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				List colVistaDetallePedido = (List) vistaPedidoDTO.getVistaDetallesPedidosReporte();
				//se obtiene el objeto VistaDetallePedidoDTO
				VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)colVistaDetallePedido.get(indice);
				request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO",vistaDetallePedidoDTO);

				//Obtener los art\u00EDculos de la receta
				if (vistaDetallePedidoDTO.getArticuloDTO() != null){
					if(vistaDetallePedidoDTO.getArticuloDTO().getTieneArticuloRelacionado()){// vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol() != null){						
						detalleCanasto = vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();						
					}else{
						detalleCanasto = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
						if(vistaDetallePedidoDTO.getArticuloDTO() == null){
							vistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
						}						
						vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(detalleCanasto);
					}
				}
				try{
					if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal() != null){
						Collection<Long> canastoOriginal = null;
						Collection<Long> canastoModificado = null;
						LogSISPE.getLog().info("Empieza el Proceso de Comparacion de Canasta Modificadas");
						//Tabs Canastas
						EstadoPedidoUtil.instanciarTabsCanasta(request, "estadoDetalleCanasto", "estadoDetalleCanasto");
						LogSISPE.getLog().info(":::::--Canasto Original--:::::");
						Collection <ArticuloRelacionDTO> colRecetaO = EstadoPedidoUtil.obtenerReceta(request, vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal());
						Collection colRecetaM = vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();					
						//Traigo los codigos de los items del canasto original y modificado
						canastoOriginal = EstadoPedidoUtil.itemsCanasta(request, colRecetaO);
						LogSISPE.getLog().info(":::::--Canasto Modificada--:::::{}",vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().size());
						canastoModificado = EstadoPedidoUtil.itemsCanasta1(request, colRecetaM);
						EstadoPedidoUtil.comparacionItemsCanasta(request, canastoOriginal, canastoModificado, colRecetaO, colRecetaM);
						//Comparo las cantidades del CanastoO con el CanastoM
						EstadoPedidoUtil.comparacionCantidadesItemsCanasta(request, colRecetaO, colRecetaM);
						//se construye el objeto ARTICULO para la consulta del encabezado original
						ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
						consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			    	    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
			    	    consultaArticuloDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal());
			    	    
			    	    //para obtener el codigo de barras
			    	    consultaArticuloDTO.setArtBitCodBarCol(new ArrayList<ArticuloBitacoraCodigoBarrasDTO>());
			    	    consultaArticuloDTO.getArtBitCodBarCol().add(new ArticuloBitacoraCodigoBarrasDTO());
			    	    //fin obtencion de codigo de barras
			    	    
			    	    consultaArticuloDTO.setNpCodigoLocal(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			    	    
			    	    //unidad de manejo
//			    	    consultaArticuloDTO.setArticuloUnidadManejoCol(new ArrayList<ArticuloUnidadManejoDTO>());
//						ArticuloUnidadManejoDTO articuloUnidadManejoDTO = new ArticuloUnidadManejoDTO();
//						articuloUnidadManejoDTO.setArticuloUnidadManejoUsoCol(new ArrayList<ArticuloUnidadManejoUsoDTO>());
//						articuloUnidadManejoDTO.getArticuloUnidadManejoUsoCol().add(new ArticuloUnidadManejoUsoDTO());
//						consultaArticuloDTO.getArticuloUnidadManejoCol().add(articuloUnidadManejoDTO);
			    	  
			    	    //List <ArticuloDTO> articuloDTO = (ArrayList<ArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
			    	    
			    	    //articulo proveedor
			    	    consultaArticuloDTO.setArticuloProveedorCol(new ArrayList<ArticuloProveedorDTO>());
			    	    consultaArticuloDTO.getArticuloProveedorCol().add(new ArticuloProveedorDTO());
			    	    
			    	    //join clasificacion
			    	    consultaArticuloDTO.setClasificacionDTO(new ClasificacionDTO());
			    	    
			    	    Collection<ArticuloDTO> articuloDTO = SICFactory.getInstancia().articulo.getArticuloService().obtenerArticuloVenta(consultaArticuloDTO);
			    	    if(articuloDTO != null && !articuloDTO.isEmpty()){
			    	    	//ArticuloDTO art = (ArticuloDTO)articuloDTO.get(0);		    	    	
			    	    	ArticuloDTO art = (ArticuloDTO)articuloDTO.iterator().next();
			    	    	//Se guarda encabezado receta Original
			    	    	request.getSession().setAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL, art);
			    	    }
			    	    request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL, colRecetaO);
			    	    
					}
					
					//se llenan los np asociados a las autorizaciones de stock
					asignarCamposStockReceta(vistaDetallePedidoDTO, detalleCanasto);
					
					//SE GUARDA ITEMS DE LA RECETA DEL ESTADODETALLERECETA				
					request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_ACTUAL, detalleCanasto);
					request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta", detalleCanasto);
					request.getSession().setAttribute(DetalleEstadoPedidoAction.AUXILIAR_ATRAS_DETESTPED, "ok");
					
					//se elimina esta variable para que no se imprima nuevamente el estado del detalle del pedido
					session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte");
					request.setAttribute("ORIGEN_DETALLE_CANASTA", ORIGEN_DETALLE_PEDIDO);
					salida = "estadoDetalleCanasto";
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					salida="estadoDetallePedido";
				}
			}
			
			//Se muestra el detalle de la receta desde el kardex 
			else if (request.getParameter("indiceEntregaKardex") != null){
				VistaDetallePedidoDTO vistaDetallePedidoActual = null;
				Collection<EstadoDetalleRecetaDTO> detalleReceta = null;
				EntregaDetallePedidoDTO entregaActual = null;
				Collection<EntregaDetallePedidoDTO> entregas = (Collection<EntregaDetallePedidoDTO>) session.getAttribute("ec.com.smx.sic.sispe.entregaDTOCol");
				int indiceEntregaKardex = Integer.parseInt(request.getParameter("indiceEntregaKardex"));
				
				if (entregas != null && entregas.size() - 1 >= indiceEntregaKardex){
					entregaActual = (EntregaDetallePedidoDTO) entregas.toArray()[indiceEntregaKardex];
					vistaDetallePedidoActual = new VistaDetallePedidoDTO();
					vistaDetallePedidoActual.getId().setCodigoCompania(entregaActual.getId().getCodigoCompania());
					vistaDetallePedidoActual.getId().setCodigoArticulo(entregaActual.getCodigoArticulo());
					vistaDetallePedidoActual.getId().setSecuencialEstadoPedido(entregaActual.getSecuencialEstadoPedido());
					vistaDetallePedidoActual.setDescripcionArticulo(entregaActual.getEstadoDetallePedidoDTO().getDetallePedidoDTO().getArticuloDTO().getDescripcionArticulo());
					vistaDetallePedidoActual.setArticuloDTO(entregaActual.getEstadoDetallePedidoDTO().getDetallePedidoDTO().getArticuloDTO());
					
					request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO", vistaDetallePedidoActual);
					detalleReceta = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoActual);
					if (detalleReceta != null && detalleReceta.isEmpty() == false){
						request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta", detalleReceta);
					} else {
						errors.add("errorReporte", new ActionMessage("errors.obtenerDatos", "detalle del canasto: " + request.getParameter("codigoRecetaKardex")));
					}
					request.getSession().setAttribute("ORIGEN_DETALLE_CANASTA", ORIGEN_DETALLE_KARDEX);
					session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte");
				}else{
					errors.add("errorReporte", new ActionMessage("errors.obtenerDatos", "entregas"));
				}
			}
			
			/*----------------- cuando se clic en el bot\u00F3n atras -------------------------------*/
			else if(request.getParameter("atras") != null){
				//se restablece esta variable para que se pueda imprimir nuevamente el estado del detalle del pedido
				session.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte","ok");
				session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO");
				if (request.getParameter("atras").equals(ORIGEN_DETALLE_KARDEX)){
					salida = "desplegarDetalleEntregasKardex";
				}else{
					salida = "estadoDetallePedido";
				}
			}
			/*-- Control de Tabs--*/
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				LogSISPE.getLog().info("Control Tabs");
				session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta");
				/*---------- si se escogi\u00F3 el tag de Canasta Estado Actual --------------*/
				if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
					/*--------------- si se escogi\u00F3 el tab de pedidos por entregar ----------------*/
					LogSISPE.getLog().info("Entro por el tab Canasto Estado Actual");
					Collection<EstadoDetalleRecetaDTO> colRecetaAc = (ArrayList<EstadoDetalleRecetaDTO>)session.getAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
					session.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",colRecetaAc);
				}
				/*------------ si se escogi\u00F3 el tab de Canasta Estado Detalle Modificado --------------*/    
				else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
					LogSISPE.getLog().info("Entro por el tab Canasto Estado Detalle Modificado");
					Collection <ArticuloRelacionDTO> colRecetaO = (ArrayList<ArticuloRelacionDTO>)session.getAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
					session.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",colRecetaO);						
				}
				salida = "estadoDetalleCanasto";
			}
			else if(request.getParameter("ayuda") != null && request.getParameter("ayuda").equals("xls")){
				LogSISPE.getLog().info("confirmar impresion XSL");
		    	VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				if(vistaPedidoDTO != null && vistaPedidoDTO.getVistaDetallesPedidosReporte() != null){
					//se cargan los detalles de las recetas en caso de que no se hayan consultado todav\u00EDa
//					String tipoArticuloRecetaCanasta = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//					String tipoArticuloRecetaDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
					for(Iterator<VistaDetallePedidoDTO> it = vistaPedidoDTO.getVistaDetallesPedidosReporte().iterator(); it.hasNext();){
						VistaDetallePedidoDTO vistaDetallePedidoDTO = it.next();
						//si es una receta
//		  			if(vistaDetallePedidoDTO.getCodigoTipoArticulo()!=null && 
//		  					(vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaCanasta) || vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaDespensa))){
//		  			
			  			if(vistaDetallePedidoDTO.getCodigoClasificacion()!=null && 
			  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
			  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
			  				
			  				if(vistaDetallePedidoDTO.getArticuloDTO()== null)
			  					vistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			  				
			  				if( !vistaDetallePedidoDTO.getArticuloDTO().getTieneArticuloRelacionado()){
				  				//se carga la receta del art\u00EDculo
				  				Collection recetaArticulo = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
				  				vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticulo);
			  				}
			  			}
					}
				}
		      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("Detalle_Canasto", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
				salida = "reporteEstadoXLS";
			}
		}catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		//se guardan los mensajes 
		saveErrors(request, errors);
		return mapping.findForward(salida);
	}
	
	
	/**
	 * Asigna la cantidad a reservar en el sic y el estado de la autorizacion a los detalles del canasto
	 * @param vistaDetallePedidoDTO
	 * @param detalleCanasto
	 * @author bgudino
	 */
	public static void asignarCamposStockReceta(VistaDetallePedidoDTO vistaDetallePedidoDTO, Collection<EstadoDetalleRecetaDTO> detalleCanasto){
		try{
			if(vistaDetallePedidoDTO != null && vistaDetallePedidoDTO.getEstadoDetallePedidoDTO() != null
				&& CollectionUtils.isNotEmpty(vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())
					&& CollectionUtils.isNotEmpty(detalleCanasto)){
				
				LogSISPE.getLog().info("asignando los campos de las autorizaciones de stock a los detalles de la receta.");
				
				//se verifica si el detalle tiene autorizacione de stock
				for(DetalleEstadoPedidoAutorizacionDTO detEstPedAutDTO : vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
					
					if(detEstPedAutDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()
							&& CollectionUtils.isNotEmpty(detEstPedAutDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
						
						//se verifica si los detalles de autorizacion de stock pertenecen a un canasto
						for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
							if(StringUtils.isNotEmpty(stockDTO.getCodigoArticuloRelacionado())){
								
								//se recorre el canasto para comparar con las autorizaciones
								for(EstadoDetalleRecetaDTO arRelacionDTO : detalleCanasto){
	
									//se asigna el stock que se registro en la tabla de autorizaciones de stock
									if(arRelacionDTO.getRecetaArticuloDTO().getArticuloRelacion().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticuloRelacionado())
											&& arRelacionDTO.getRecetaArticuloDTO().getArticuloRelacion().getId().getCodigoCompania() == stockDTO.getCodigoCompania()){
										
										//se asigna el stock que se solicito y el estado de la autorizacion
										arRelacionDTO.getRecetaArticuloDTO().getArticuloRelacion().setNpStockArticulo(stockDTO.getCantidadParcialSolicitarStock().longValue());
										arRelacionDTO.getRecetaArticuloDTO().getArticuloRelacion().setNpEstadoAutorizacion(stockDTO.getEstadoAutorizacion());
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("Error en el metodo asignarCamposStockReceta: "+ex.getMessage());
		}
		
	}
}
