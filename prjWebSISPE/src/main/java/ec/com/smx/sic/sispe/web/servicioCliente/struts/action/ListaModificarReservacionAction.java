/* Creado el 15/04/2008
 * TODO
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDocumentoTransaccionDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetalleArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.integracion.sic.SISPEIntegracion;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author jacalderon
 * @version 3.0
 */
@SuppressWarnings("unchecked")
public class ListaModificarReservacionAction extends ListadoPedidosAction
{
	public static final String INDICE_RESERVA_MODIFICAR = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("indiceReservaModificar");
	
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		//se obtiene la sesi\u00F3n
		HttpSession session= request.getSession();
		ActionMessages errors = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		
		String salida = "desplegar";
		
		//String peticion = request.getParameter(Globals.AYUDA);
		
		//se guarda la acci\u00F3n actual
		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"));
		
		try{
			//----- cuando se selecciona una reservaci\u00F3n para su modificaci\u00F3n -----------
			if(request.getParameter("indiceModificar")!=null){
				
				Integer indice = Integer.valueOf(request.getParameter("indiceModificar"));
				//se obtiene la colecci\u00F3n de los pedidos 
				List<VistaPedidoDTO> colVistaPedidoDTO = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.get(indice.intValue());
				
				//se verifica si la reserva esta bloqueada porque se esta realizando un pago en el POS
				if(EstadoPedidoUtil.reservaBloqueadaDesdePOS(vistaPedidoDTO.getId().getCodigoAreaTrabajo(), 
						vistaPedidoDTO.getId().getCodigoCompania(), 
						vistaPedidoDTO.getId().getCodigoEstado(), vistaPedidoDTO.getId().getCodigoPedido(), 
						vistaPedidoDTO.getId().getSecuencialEstadoPedido())) {
					instanciarPopUpNotificacionBloqueoReserva(request, vistaPedidoDTO.getLlaveContratoPOS(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
					salida = "desplegar";
					return mapping.findForward(salida);
				}
				
				//se verifica si la reserva esta bloqueada porque se ya se realizo una orden de salida con guia de remision y esta en proceso de entrega
				if(EstadoPedidoUtil.reservaBloqueadaDesdeSICMER(errors,vistaPedidoDTO.getId().getCodigoPedido(),vistaPedidoDTO.getId().getCodigoCompania())) {
					String accionPopUp="modificar";
					String accionOk="requestAjax('listaModificarReserva.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cerrarPopUpBloqueoReserva=ok', evalScripts:true});ocultarModal();";
					CotizacionReservacionUtil.instanciarPopUpNotificacionBloqueoReservaSICMER(request, vistaPedidoDTO.getLlaveContratoPOS(),accionPopUp,accionOk);
					salida = "desplegar";
					return mapping.findForward(salida);
				}
				

				//se verifica si el pedido tiene notas de credito activas				
				EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();
				estadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				estadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				estadoPedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
				estadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
				estadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
				estadoPedidoDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
				
				//Consultamos si el pedido tiene notas de credito activas
				Collection<EstadoPedidoDocumentoTransaccionDTO> colNotasCredidoActiva = 
						SISPEFactory.getServicioIntegracionPOS().obtenerEstadoPedidoDocumentoTransaccion(estadoPedidoDTO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.documento.nota.credito"));
				
				//Solo se puede reservar si el pedido no tiene notas de credito activas
				if (CollectionUtils.isEmpty(colNotasCredidoActiva)) {
					Double valorPedido=vistaPedidoDTO.getTotalPedido()==null?0:vistaPedidoDTO.getTotalPedido();
					Double valorAbonado=vistaPedidoDTO.getAbonoPedido()==null?0:vistaPedidoDTO.getAbonoPedido();
					Collection<VistaPedidoDTO> visPedCol=null;
					if(valorAbonado.longValue()>valorPedido.longValue()){
						visPedCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerHistoricoModificacionesReserva(vistaPedidoDTO,null);
					}
					
					//Si tiene vales de caja de igual manera se muestar el mensaje para no dejar modificar la reserva
					if (CollectionUtils.isEmpty(visPedCol)) {
						
						//jparedes
						//Verificar si la reserva fue entregada parcialmente, si es asi ya no se puede modificar
						VistaDetalleArticuloDTO vistaDetalleArticuloBusqueda = new VistaDetalleArticuloDTO();
						vistaDetalleArticuloBusqueda.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
						vistaDetalleArticuloBusqueda.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						vistaDetalleArticuloBusqueda.setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
//						vistaDetalleArticuloBusqueda.setReservarBodegaSIC(ConstantesGenerales.ESTADO_ACTIVO);
//						vistaDetalleArticuloBusqueda.setCantidadArticuloDespachoEstadoPedido(0);
						vistaDetalleArticuloBusqueda.setNpEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
						vistaDetalleArticuloBusqueda.setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
						//obtener reserva estado actual
						Collection<VistaDetalleArticuloDTO> detallesArticulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetalleArticulo(vistaDetalleArticuloBusqueda);
						
						int contadorFechaRegistroDes = 0;
						
						//se debe dejar modificar todos las reservas, excepto las que todas sus entregas son a domicilio CD y esten todas despachadas
						if(CollectionUtils.isNotEmpty(detallesArticulos)){
							for(VistaDetalleArticuloDTO vistaDetArt: detallesArticulos){
								
								LogSISPE.getLog().info("cantidad "+vistaDetArt.getCantidadArticuloEstadoPedido()+" - "+vistaDetArt.getCodigoArticuloEstadoPedido()+" id: "+vistaDetArt.getId().getCodigoArticulo()+"-"+vistaDetArt.getId().getSecuencialEntrega());
								
								if(vistaDetArt.getCantidadArticuloDespachoEstadoPedido() != null && vistaDetArt.getCantidadArticuloDespachoEstadoPedido() > 0 
										&& vistaDetArt.getFechaRegistroDespacho() != null && vistaDetArt.getFechaRegistroEntregaCliente()!= null 
										&& vistaDetArt.getCodigoContextoEntrega().toString().equals(ConstantesGenerales.CONTEXTO_ENTREGA_DOMICIO)
										&& vistaDetArt.getCodigoObtenerStock().intValue() == MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega").intValue()){
									contadorFechaRegistroDes ++;
								}
							}
						}
						if(contadorFechaRegistroDes > 0 && contadorFechaRegistroDes == detallesArticulos.size()){
							instanciarPopUpNotificacionBloqueoReserva(request, vistaPedidoDTO.getLlaveContratoPOS(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,null);
							salida = "desplegar";
						}else{
							//se sube a sesi\u00F3n el pedido
							session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
							//se guarda el indice del pedido seleccionado
							session.setAttribute(INDICE_RESERVA_MODIFICAR, request.getParameter("indiceModificar"));
							//obtengo el codigoTipDesMax-navidad desde un parametro
							CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
							salida ="modificarReservacion";
						}
					}else {
						String tipoDocumento="un vale de caja";
						instanciarPopUpNotificacionBloqueoReserva(request, vistaPedidoDTO.getLlaveContratoPOS(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, tipoDocumento);
						salida = "desplegar";
					}
					
				}else {
					
					//instanciarPopUpReservaNotaCredito(request, vistaPedidoDTO.getLlaveContratoPOS());
					String tipoDocumento="una nota de cr\u00E9dito";
					instanciarPopUpNotificacionBloqueoReserva(request, vistaPedidoDTO.getLlaveContratoPOS(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, tipoDocumento);
					salida = "desplegar";
				}
				
				/*
				String estadoSinPago = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago");

				String estadoAnulado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado");
				//se verifica si el estado del pedido es anulado
				if(vistaPedidoDTO.getId().getCodigoEstado().equals(estadoAnulado)){
					//se puede modificar la reservaci\u00F3n
					salida ="modificarReservacion";
					session.setAttribute("ec.com.smx.sic.sispe.estadoPedido.anulado", estadoAnulado);
				}else{
					
					UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Modificaci\u00F3n de Reserva");
					popUp.setEtiquetaBotonOK("Aceptar");
					popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
					popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
					popUp.setFormaBotones(UtilPopUp.OK);
					popUp.setFormaEnvioDatos(UtilPopUp.OTRO);
					
					//se verifica el estado del pedido y si est\u00E1 reservado en bodega
					if(vistaPedidoDTO.getReservarBodegaSIC().equals(SessionManagerSISPE.getEstadoActivo(request))){
							if(!vistaPedidoDTO.getCodigoEstadoPagado().equals(estadoSinPago)){
								//se asigna el mensaje
								popUp.setMensajeVentana("El pedido No <b>".concat(vistaPedidoDTO.getId().getCodigoPedido()).concat("</b> fue enviado a bodega para su procesamiento, " +
									"y adem\u00E1s tiene abonos realizados, para realizar los cambios primero debe <b>anularse desde el Punto de Venta</b> y luego utilizar nuevamente esta opci\u00F3n para buscar el pedido anulado."));
							}else{
								//se asigna el mensaje
								popUp.setMensajeVentana("El pedido No <b>".concat(vistaPedidoDTO.getId().getCodigoPedido()).concat("</b> fue enviado a bodega para su procesamiento, " +
									"para realizar los cambios primero debe <b>anularse desde la pantalla de Anulaci\u00F3n de Pedidos</b> y luego utilizar nuevamente esta opci\u00F3n para buscar el pedido anulado."));
							}
						//se guarda la ventana
						request.setAttribute(SessionManagerSISPE.POPUP, popUp);
					}else{
						if(!vistaPedidoDTO.getCodigoEstadoPagado().equals(estadoSinPago)){
							//se asigna el mensaje
							popUp.setMensajeVentana("El pedido No <b>".concat(vistaPedidoDTO.getId().getCodigoPedido()).concat("</b> " +
								"ya tiene abonos, para realizar los cambios primero debe anularse desde el Punto de Venta y luego utilizar la opci\u00F3n <b>Modificar Reserva desde Anulaci\u00F3n</b>"));
							//se guarda la ventana
							request.setAttribute(SessionManagerSISPE.POPUP, popUp);
						}else{
							//se puede modificar la reservaci\u00F3n
							popUp = null;
							salida ="modificarReservacion";
						}
					}
				}
				*/
			}
			//cuando se selecciona una reservaci\u00F3n para su reeenv\u00EDo al SIC
			else if(request.getParameter("indiceReenviar") != null){
			
				Integer indice = Integer.valueOf(request.getParameter("indiceReenviar"));
				//se obtiene la colecci\u00F3n de los pedidos 
				List<VistaPedidoDTO> colVistaPedidoDTO = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.get(indice.intValue());
				
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Reenv\u00EDo al SIC");
				popUp.setEtiquetaBotonOK("Si");
				popUp.setEtiquetaBotonCANCEL("No");
				popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
				popUp.setValorOK("requestAjax('listaModificarReserva.do',['mensajes'],{parameters: 'reeenviarSIC=ok'});hide(['popupConfirmar']);ocultarModal();");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
				popUp.setMensajeVentana("El pedido <b>".concat(vistaPedidoDTO.getId().getCodigoPedido()).concat("</b> ser\u00E1 reenviado al SIC, si es el caso, para la reserva en el CD. " +
						"Tome en cuenta que si este pedido ya est\u00E1 en el SIC se duplicar\u00E1n las cantidades reservadas."));
				popUp.setPreguntaVentana("\u00BFDesea realizar el reenvio?");
				
				//se guarda la ventana
				request.setAttribute(SessionManagerSISPE.POPUP, popUp);
				//se sube a sesi\u00F3n el pedido
				session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);

			}
			//cuando se acepta el reenv\u00EDo al SIC
			else if(request.getParameter("reeenviarSIC")!=null){
				
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				PedidoDTO pedidoDTO = new PedidoDTO();
				pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
				pedidoDTO.setNpEstadoPedido(vistaPedidoDTO.getId().getCodigoEstado());
				pedidoDTO.setEntidadResponsable(vistaPedidoDTO.getEntidadResponsable());
				//pedidoDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
				pedidoDTO.setNpLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
				pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombreContacto());
				pedidoDTO.setCodigoPedidoRelacionado(vistaPedidoDTO.getCodigoPedidoRelacionado());
				
				
				if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) ||
						vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
					pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
//					pedidoDTO.setNpCedulaContacto(vistaPedidoDTO.getNumeroDocumentoPersona());
					pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombrePersona());
//					pedidoDTO.setNpTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
//					pedidoDTO.setNpTelefonoContacto(vistaPedidoDTO.getTelefonoPersona());
				}
				else if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || 
						vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){																
					pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
//					pedidoDTO.setNpCedulaContacto(vistaPedidoDTO.getNumeroDocumentoPersona());
					//pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombreEmpresa());
//					pedidoDTO.setNpTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
//					pedidoDTO.setNpTelefonoContacto(vistaPedidoDTO.getTelefonoContacto());
//					//se setean los datos de la empresa
					pedidoDTO.setNpNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
					pedidoDTO.setNpRucEmpresa(vistaPedidoDTO.getRucEmpresa());
//					pedidoDTO.setNpTelefonoEmpresa(vistaPedidoDTO.getTelefonoEmpresa());
				}
				
				
				String estadoActivo = session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO).toString();
				try{
					String reservarBodegaSIC = SISPEIntegracion.generarReservacionBodega(pedidoDTO, ConstantesGenerales.TIPO_RESERVACION_NUEVO, null,null,ConstantesGenerales.ESTADO_SI,ConstantesGenerales.ESTADO_SI);
					if(reservarBodegaSIC.equals(estadoActivo)){
						exitos.add("exitoReenvioSIC", new ActionMessage("exito.reenviarReservaSIC"));
					}else{
						infos.add("noEnviadoSIC", new ActionMessage("infos.reenviarReservaSIC"));
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("errorSISPE", new ActionMessage("errors.SISPEException", ex.getMessage()));
				}
			//} else if (request.getParameter("cerrarPopUpModReserva") != null) {
			} else if (request.getParameter("cerrarPopUpBloqueoReserva") != null) {
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegar";
			}
			
			else{
				//se ejecuta la acci\u00F3n padre con los casos generales
				return super.execute(mapping, form, request, response);
			}
			
		}catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);
		
		return mapping.findForward(salida);
	}
	
	/**
	 * PopUp para notificar que no se puede modificar una reserva
	 * @param request
	 * @param numeroReserva EL numeroReserva que esta bloqueado para modificaciones 
	 * @param bloqueoNotaCredito <code>TRUE</code> si el bloqueo es por nota de credito activa
	 * @param bloqueoPOS <code>TRUE</code> si el bloqueo es porque la reserva se realizando el pago en el pos
	 * @throws Exception
	 */
	private void instanciarPopUpNotificacionBloqueoReserva(HttpServletRequest request, String numeroReserva, Boolean bloqueoNotaCredito, Boolean bloqueoPOS, Boolean bloqueoPedidoEntregado, String tipoDocumento)throws Exception{
		
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		
		if(bloqueoNotaCredito){
			popUp.setTituloVentana("Reserva con " +tipoDocumento+" pendiente");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append(numeroReserva)
					.append(" tiene "+tipoDocumento+" pendiente. Ac\u00E9rquese al Punto de Venta").toString());
		}else if(bloqueoPOS){
			popUp.setTituloVentana("Reserva bloqueada en el punto de venta");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append("<strong>").append(numeroReserva).append("</strong>")
					.append(" actualmente se est\u00E1 pagando en el punto de venta, no se puede modificar").toString());
		}else if(bloqueoPedidoEntregado){
			popUp.setTituloVentana("Reserva entregada parcialmente");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append(numeroReserva)
					.append(" se encuentra ENTREGADA PARCIALMENTE. No puede ser modificada.").toString());
		}
		
//		popUp.setTituloVentana("Reserva con nota de cr\u00E9dito pendiente");
//		popUp.setEtiquetaBotonOK("Aceptar");
//		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('listaModificarReserva.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cerrarPopUpBloqueoReserva=ok', evalScripts:true});ocultarModal();");
//		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarCon=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		
//		popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
//		popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
		
//		popUp.setPreguntaVentana("La reserva No. ".concat(numeroReserva).concat(" tiene una nota de cr\u00E9dito pendiente. Ac\u00E9rquese al Punto de Venta"));
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
		
	}
}
