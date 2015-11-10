/*
 * EnvioMailUtil.java
 * Creado el 02/04/2014 09:52:19
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ec.com.smx.autorizaciones.common.factory.AutorizacionesFactory;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.autorizaciones.integracion.dto.Autorizacion;
import ec.com.smx.autorizaciones.integracion.dto.AutorizacionEstado;
import ec.com.smx.autorizaciones.integracion.dto.ValorComponente;
import ec.com.smx.frameworkv2.security.dto.UserDto;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author bgudino
 *
 */

@SuppressWarnings("unchecked")
public class EnvioMailUtil {

	/**
	 * Envia los correos despues de registrar la reservacion y de mostrar la pantalla de resumen del pedido
	 * @param request
	 * @throws Exception 
	 */
	public static void enviarMailReservacion(HttpServletRequest request) throws Exception{
		LogSISPE.getLog().info("se procede a realizar el envio mails luego de haberse registrado la reservacion");
		enviarMailAutorizacionStockPavos(request);
		enviarMailNotificacionCompras(request);
		enviarMailNotificacionBodega(request);
	}
	
	/**
	 * Envia el correo de notificacion de compras y el correo de entregas a otro local
	 * @param request
	 */
	private static void enviarMailNotificacionCompras(HttpServletRequest request){
		
		LogSISPE.getLog().info("se procede a realizar el envio mails notificando a los compradores");
		HttpSession session = request.getSession();
		
		PedidoDTO pedidoDTO  = (PedidoDTO)session.getAttribute(CotizarReservarAction.PEDIDO_GENERADO);
		Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		if(pedidoDTO != null && pedidoDTO.getEstadoPedidoDTO() != null && CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			String codigoPedido = pedidoDTO.getId().getCodigoPedido();
			String llaveContratoPos = pedidoDTO.getEstadoPedidoDTO().getLlaveContratoPOS();
			
			try {
				SessionManagerSISPE.getServicioClienteServicio().transEnviarMailNotificacionCompras(pedidoDTO.getEstadoPedidoDTO(), detallePedidoDTOCol);
				SessionManagerSISPE.getServicioClienteServicio().transEnvioMailEntregaOtroLocal(codigoPedido, detallePedidoDTOCol, llaveContratoPos);
			} catch (Exception e) {
				LogSISPE.getLog().error("error al enviar los mails despues de haber registrado la reserva "+e.getMessage());
			}
		}
	}
	
	
	/**
	 * Envia el correo al usuario de bodega de pavos indicandole que la autorizacion de stock ha sido solicitada /gestionada por parte del gerente comercial
	 * @param request
	 * @throws Exception
	 */
	public static void enviarMailAutorizacionStockPavos(HttpServletRequest request) throws Exception{
		
		try{
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)
					request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK);
			
			if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
				
				PedidoDTO pedidoDTO  = (PedidoDTO)request.getSession().getAttribute(CotizarReservarAction.PEDIDO_GENERADO);
				
				//se obtiene los userid de los usuarios que toca enviarles el correo de notificacion
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.usuarios.envio.correos.autorizacion.stock.pavos", request);
				
				if(parametroDTO != null){
					String[] userIds = parametroDTO.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
					
					UserDto usuarioConsulta = new UserDto(), usuarioPavos = new UserDto();
					
					for(String userIdActual : userIds){
						usuarioConsulta.setUserId(userIdActual);
						 usuarioPavos = SISPEFactory.getDataService().findUnique(usuarioConsulta);
						 
						 if(usuarioPavos != null){
							 
							 //se obtiene el correo del usuario
							 String[] mails = new String[]{usuarioPavos.getUserEmail()};
							 LogSISPE.getLog().info("se enviara el correo a: "+usuarioPavos.getUserEmail());
							 
							 //cadena para armar el contenido del mail
							 StringBuilder datosCorreo = new StringBuilder();
							 datosCorreo.append("<datosCorreo>");
				    		
							 //se recorren las autorizaciones
							 for(EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO : colaAutorizacionesStock){
									
								//se verifica el estado de la autorizacion
								if( ((estadoPedidoAutorizacionDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)
										&& estadoPedidoAutorizacionDTO.getNpAutorizacion().getEnviarNotificacion() != null 
										&& estadoPedidoAutorizacionDTO.getNpAutorizacion().getEnviarNotificacion())
										|| verificarEnvioCorreoAutorizacionStockGestionada(request, estadoPedidoAutorizacionDTO, pedidoDTO.getNpAutorizacionesWorkFlow())) 
										&& StringUtils.isNotEmpty(estadoPedidoAutorizacionDTO.getValorReferencial())){
									
									boolean existeClasificacionPavos = false;
									
									String[] codigosClasificaciones = estadoPedidoAutorizacionDTO.getValorReferencial().split(
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
									
									if(codigosClasificaciones != null && codigosClasificaciones.length > 0){
										
										for(String clasificacionActual : codigosClasificaciones){
											if(CotizacionReservacionUtil.esPavo(clasificacionActual.trim(), request)){
												existeClasificacionPavos = true;
												break;
											}
										}
									}
									if(existeClasificacionPavos){
										
										AutorizacionDTO autorizacionDTO =  AutorizacionesFactory.getInstancia().
												getaIAutorizacionesServicio().transBuscarAutorizacionPorId(estadoPedidoAutorizacionDTO.getCodigoAutorizacion());
							    		
										if(autorizacionDTO != null){
											
											boolean enviarCorreo = false;
											
											String estadoAutorizacion = autorizacionDTO.getEstadoActualAutorizacion();
											String solicitadoAprobado = estadoAutorizacion.equals(AutorizacionEstado.SOLICITADA.getEstado()) ? " solicitada al " : " gestionada por el "; 
								    		
								    		datosCorreo.append("<cabecera>");
								    		datosCorreo.append("<nombreUsuario>");datosCorreo.append(usuarioPavos.getUserCompleteName());
								    		datosCorreo.append("</nombreUsuario>");
								    		datosCorreo.append("<solicitadoAprobado>");datosCorreo.append(solicitadoAprobado);
								    		datosCorreo.append("</solicitadoAprobado>");
								    		datosCorreo.append("<estadoAutorizacion>");datosCorreo.append(estadoAutorizacion);
								    		datosCorreo.append("</estadoAutorizacion>");
								    		datosCorreo.append("<nombreGerenteComercial>");datosCorreo.append(estadoPedidoAutorizacionDTO.getNpAutorizacion().getTituloAutorizacion().split("-")[1]);
								    		datosCorreo.append("</nombreGerenteComercial>");
								    		datosCorreo.append("<codigoPedido>");datosCorreo.append(pedidoDTO.getId().getCodigoPedido());
								    		datosCorreo.append("</codigoPedido>");
								    		datosCorreo.append("</cabecera>");
								    		
								    		
							    			//codigos de los componentes de autorizaciones usados para detalles de cada articulo hijo
							    			Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
							    			Long codigoComponenteDescripcionArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.descripcion.articulo")); //82 
							    			Long codigoComponenteCantidadArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.cantidad.articulo"));//83
							    			Long codigoComponenteRadiodArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.radio.articulo"));//101
											
							    			String codigoBarras = "", descripcionArticulo = "", cantidad= "", valorComponente = ""; 
								    		//se recorren las autorizaciones hijas
								    		if(CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionesHijas())){
								    			
												datosCorreo.append("<listaDetalles>");
												
												Integer i = 1;
												for(AutorizacionDTO autorizacionHijaActual : autorizacionDTO.getAutorizacionesHijas()){
													
													datosCorreo.append("<detalle>");
													
													if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getValoresComponente())){
														codigoBarras = AutorizacionesUtil.obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
														descripcionArticulo = AutorizacionesUtil.obtenerValorComponentePorID(codigoComponenteDescripcionArt, autorizacionHijaActual);
														cantidad = AutorizacionesUtil.obtenerValorComponentePorID(codigoComponenteCantidadArt, autorizacionHijaActual);
														valorComponente = AutorizacionesUtil.obtenerValorComponentePorID(codigoComponenteRadiodArt, autorizacionHijaActual);
														if(valorComponente != null && valorComponente.equals("0")){
															estadoAutorizacion = AutorizacionEstado.RECHAZADA.getEstado();
														}else if(valorComponente != null && valorComponente.equals("1")){
															estadoAutorizacion = AutorizacionEstado.AUTORIZADA.getEstado();
														}
														
											    		datosCorreo.append("<numero>");datosCorreo.append(i.toString());
											    		datosCorreo.append("</numero>");
											    		datosCorreo.append("<codigoBarras>");datosCorreo.append(codigoBarras);
											    		datosCorreo.append("</codigoBarras>");
											    		datosCorreo.append("<descripcionArticulo>");datosCorreo.append(descripcionArticulo);
											    		datosCorreo.append("</descripcionArticulo>");
											    		datosCorreo.append("<cantidad>");datosCorreo.append(cantidad);
											    		datosCorreo.append("</cantidad>");
											    		datosCorreo.append("<estado>");datosCorreo.append(estadoAutorizacion);
											    		datosCorreo.append("</estado>");
														
													}
													datosCorreo.append("</detalle>");
													i++;
												}
												datosCorreo.append("</listaDetalles>");
												enviarCorreo = true;
												
								    		}else if(CollectionUtils.isNotEmpty(estadoPedidoAutorizacionDTO.getNpAutorizacion().getAutorizacionesHijas())){
								    			//quiere decir que ingresaron un numero de autorizacion
								    			
												datosCorreo.append("<listaDetalles>");
												
												Integer i = 1;
								    			for(Autorizacion autorizacionHijaActual : estadoPedidoAutorizacionDTO.getNpAutorizacion().getAutorizacionesHijas()){
								    				
								    				datosCorreo.append("<detalle>");
													
													if(autorizacionHijaActual.getFiltroTipoAutorizacion() != null){
														codigoBarras = obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
														descripcionArticulo = obtenerValorComponentePorID(codigoComponenteDescripcionArt, autorizacionHijaActual);
														cantidad = obtenerValorComponentePorID(codigoComponenteCantidadArt, autorizacionHijaActual);
														estadoAutorizacion = AutorizacionEstado.AUTORIZADA.getEstado();
														
											    		datosCorreo.append("<numero>");datosCorreo.append(i.toString());
											    		datosCorreo.append("</numero>");
											    		datosCorreo.append("<codigoBarras>");datosCorreo.append(codigoBarras);
											    		datosCorreo.append("</codigoBarras>");
											    		datosCorreo.append("<descripcionArticulo>");datosCorreo.append(descripcionArticulo);
											    		datosCorreo.append("</descripcionArticulo>");
											    		datosCorreo.append("<cantidad>");datosCorreo.append(cantidad);
											    		datosCorreo.append("</cantidad>");
											    		datosCorreo.append("<estado>");datosCorreo.append(estadoAutorizacion);
											    		datosCorreo.append("</estado>");
														
													}
													datosCorreo.append("</detalle>");
													i++;
								    			}
								    			datosCorreo.append("</listaDetalles>");
												enviarCorreo = true;
								    		}
								    		if(enviarCorreo){
									    		datosCorreo.append("</datosCorreo>");
									    		LogSISPE.getLog().info("correo "+datosCorreo);
									    		SessionManagerSISPE.getServicioClienteServicio().transEnviarMailAutorizacionStockPavos(datosCorreo, mails);
								    		}
										}
									}
								}
							}
				    	}
					}
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().error("Error al enviar el correo de notificacion de autorizaciones de stock "+e.getMessage());
		}
	}
	
	
	/**
	 * Si existe una autorizacion en estado aprobada y esta por finalizarse, quiere decir que
	 * van a usar la autorizacion aprobada 
	 * @param request
	 * @param colaAutorizacionesStock
	 * @return
	 */
	private static boolean verificarEnvioCorreoAutorizacionStockGestionada(HttpServletRequest request, EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO,
			Collection<String> autorizacionesFinalizarWorkflow){
		
		if(CollectionUtils.isNotEmpty(autorizacionesFinalizarWorkflow)){
			//[F17-6400,GESTIONADA,1,SISAUT,SISPE4]
			//se recorre las autorizaciones que seran finalizadas formato de cdena: processCode,estadoAutorizacion,companyID,sistemID,userID
			for(String autorizacionFinalizarActual : autorizacionesFinalizarWorkflow){
				
				LogSISPE.getLog().info("autorizacion finalizar: "+autorizacionFinalizarActual);
				String processCode = autorizacionFinalizarActual.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"))[0].trim();
				
				//se verifica el tipo de autorizacion
				if((estadoPedidoAutorizacionDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) || estadoPedidoAutorizacionDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))
						&& estadoPedidoAutorizacionDTO.getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()
						&& estadoPedidoAutorizacionDTO.getNumeroProceso().toString().equals(processCode)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * busca el DynamicComponentValueDto asociado al idComponente
	 * @param idComponente 			codigo del componente
	 * @param autorizacionDTO		Autorizacion hija
	 * @return	DynamicComponentValueDto 
	 */
	public static String obtenerValorComponentePorID(Long idComponente, Autorizacion autorizacion){
		String dynComponenteValue = null;
		
		if(autorizacion.getFiltroTipoAutorizacion() != null){
			
			Long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			ValorComponente[]  valoresComp = autorizacion.getFiltroTipoAutorizacion().get(codigoTipoAutorizacionStock);
			
			if(valoresComp != null && valoresComp.length > 0){

				//se recorren los valores
				for(ValorComponente valorComActual : valoresComp){
					if(valorComActual.getCodigoComponente().longValue() == idComponente){
						return valorComActual.getValorComponente();
					}
				}
			}
		}
		return dynComponenteValue;
	}
	
	
	/**
	 * realizar el envio de mail notificando a las bodegas de los cambios realizados en las reservas
	 * @param request
	 */
	public static void enviarMailNotificacionBodega(HttpServletRequest request){
		
		LogSISPE.getLog().info("se procede a realizar el envio  de notificacion a las bodegas con los cambios realizados a las reservas");
		HttpSession session = request.getSession();
		
		PedidoDTO pedidoDTO  = (PedidoDTO)session.getAttribute(CotizarReservarAction.PEDIDO_GENERADO);
		String cambiosReserva = (String) session.getAttribute(CotizarReservarAction.CAMBIOS_MODIFICACION_RESERVA);
		
		if(pedidoDTO != null && pedidoDTO.getEstadoPedidoDTO() != null && StringUtils.isNotEmpty(cambiosReserva)){
			
			try {
				SessionManagerSISPE.getServicioClienteServicio().transEnviarNotificacionBodega(pedidoDTO.getEstadoPedidoDTO(), cambiosReserva);
				session.removeAttribute(CotizarReservarAction.CAMBIOS_MODIFICACION_RESERVA);
			} catch (Exception e) {
				LogSISPE.getLog().error("error al enviar los mails con las notificaciones a las bodegas "+e.getMessage());
			}
		}
	}
}
