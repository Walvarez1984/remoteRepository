/* Creado el 15/04/2008
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author jacalderon
 * @author fmunoz
 * @author Wladimir L\u00F3pez
 * @version 3.0
 */
public class ModificarReservacionAction  extends BaseAction
{
	public static final String INGRESA_DIRECTAMENTE_RESERVAR = "ec.com.smx.sic.sispe.reservacion";
	public static final String CODIGO_PAGADO_TOTALMENTE = "ec.com.smx.sic.sispe.estadoPagadoTotalmente";
	public static final String CODIGO_PAGADO_LIQUIDADO = "ec.com.smx.sic.sispe.estadoLiquidado";
	
	public static final String PEDIDO_PAGADO_TOTALMENTE = "ec.com.smx.sic.sispe.modificarReserva.pagadoTotalmente";
	public static final String PEDIDO_PAGADO_LIQUIDADO = "ec.com.smx.sic.sispe.modificarReserva.pagadoLiquidado";
	
	public static final String PEDIDO_SIN_PAGO = "ec.com.smx.sic.sispe.modificarReserva.sinPago";	 

	public static final String ELIMINO_ENTREGAS = "ec.com.smx.sic.sispe.modificarReserva.eliminoEntregas";	
	
	public static final String MOSTRAR_POPUP_DESPACHO = "ec.com.smx.sic.sispe.mostrar.popup.despacho";	
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          				campos
	 * @param request 		La petici&oacute;n que estamos procesando
	 * @param response		La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{

		HttpSession session= request.getSession();
		CotizarReservarForm formulario = (CotizarReservarForm)form;
		ActionMessages warnings = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		
		//se obtiene la clave que indica al estado inactivo
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		//se obtiene la clave que indica al estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String salida = "modificarReserva";
		session.removeAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);

		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		//----- procesa la petici\u00F3n cuando se escogi\u00F3 la opci\u00F3n reservar desde el formulario de busqueda de
		//----- cotizaciones y recotizaciones
		if(session.getAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR)!=null)
		{
			LogSISPE.getLog().info(">>>>>>Entro por la accion ModificarReserva<<<<<<<");
			//remueve la autorizaci\u00F3n de modificaci\u00F3n de reserva
			session.removeAttribute(CotizarReservarAction.AUTORIZACION_MODIFICA_RESERVA_SOLICITUD_CD);
			String accion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion");
			//se asigna la aci\u00F3n actual
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, accion);
			List pedidos = (List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			Integer indice = Integer.valueOf(session.getAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR).toString());
			VistaPedidoDTO vistaPedidoDTOAux = (VistaPedidoDTO)pedidos.get(indice);
			
			//se valida que el pedido en session sea el actual en la BDD
			Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTOAux);
			if(pedidoActual){
				try{
					StringBuffer validacionFecha = CotizacionReservacionUtil.verificacionFechaInicialPedido(request,vistaPedidoDTOAux);
					if(validacionFecha == null){
						
						if(vistaPedidoDTOAux.getCodigoConsolidado() != null && !vistaPedidoDTOAux.getCodigoConsolidado().equals("")){
							session.setAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
						}
						
						//se carga la configuraci\u00F3n de los descuentos
						CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
						//Obtener el tipo de descuento por cajas y mayorista
						CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
						
						//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
						boolean eliminoEntregas = CotizacionReservacionUtil.construirDetallesPedidoDesdeVista(formulario, request, infos, errors, warnings,false,Boolean.TRUE);
						//Clono las entregas del pedido
						List<DetallePedidoDTO> detallePedidoDTOColAux= new ArrayList<DetallePedidoDTO>(); 
						List<DetallePedidoDTO> detallePedidoDTOCol=(List<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
						LogSISPE.getLog().info("--Busca la configuracion de entregas antes de la modificacionReserva--");
						for (int i = 0; i < detallePedidoDTOCol.size(); i++){
							DetallePedidoDTO detallePedidoDTO = detallePedidoDTOCol.get(i);
							DetallePedidoDTO detallePedidoDTOAux = detallePedidoDTO.clone();
							
							Collection<EntregaDetallePedidoDTO> entregasCol=new ArrayList<EntregaDetallePedidoDTO>();
							
							if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
								
								entregasCol = (Collection<EntregaDetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedidoDTO.getEntregaDetallePedidoCol());
								EntregaLocalCalendarioUtil.obtenerEntregasPedido(session,entregasCol);
							}
							
//							if(!CollectionUtils.isEmpty(detallePedidoDTO.getColEntregaDetallePedido())){
//								
//								entregasDetalleCol = (Collection)SerializationUtils.clone((Serializable)detallePedidoDTO.getColEntregaDetallePedido());
//							}
							
//							if(detallePedidoDTO.getEntregas()!=null && detallePedidoDTO.getEntregas().size()>0){
//								for (Iterator iter = detallePedidoDTO.getEntregas().iterator(); iter.hasNext();) {
//									EntregaDTO entrega = (EntregaDTO)iter.next();
//									EntregaDTO entregaAux = entrega.clone();
//									entregasCol.add(entregaAux);	
//								}
//							}
							detallePedidoDTOAux.setEntregaDetallePedidoCol(entregasCol);
//							detallePedidoDTOAux.setColEntregaDetallePedido(entregasDetalleCol);
//							
							detallePedidoDTOColAux.add(detallePedidoDTOAux);
						}
						session.setAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL, detallePedidoDTOColAux);
						//metodo que verifica que autorizaciones tiene el pedido
						AutorizacionesUtil.verificacionAutorizaciones(detallePedidoDTOCol.get(0).getId().getCodigoPedido(), request, errors, exitos);
						AutorizacionesUtil.verificarAutorizacionesVariables(detallePedidoDTOCol, request, formulario);
//						AutorizacionesUtil.verificarClasificacionesPedido(request, errors, warnings, Boolean.FALSE);							
						AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, errors);
						AutorizacionesUtil.asignarNpCantidadCanastoEspecialReservado(request);
						
						if(eliminoEntregas){
							warnings.add("siEliminoEntregas",new ActionMessage("warnings.entregasEliminadas"));
							session.setAttribute(ELIMINO_ENTREGAS, "ok");
						}
						//se inicializa la propiedad que indica validaci\u00F3n del formulario
						formulario.setOpValidarPedido(estadoInactivo);
			
						//este m\u00E9todo inicializa los par\u00E1metros 
						CrearReservacionAction.inicializarParametros(request, formulario);
						
						//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la reservacion
						session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
								MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
			
						//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
						session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de reservaci\u00F3n");
						//session.removeAttribute("ec.com.smx.sic.sispe.pedido.cambioContexto");
			
						//variable que sirve para saber que inicialmente se ingres\u00F3 a una reservaci\u00F3n,
						//esta se utiliza en la jsp para mostrar o no el combo de locales
						session.setAttribute(INGRESA_DIRECTAMENTE_RESERVAR,"ok");
						
						//variable que sirve para saber si se debe modificar el peso de un articulo con peso variable,
						session.setAttribute(CotizacionReservacionUtil.MODIFICAR_PESO_INACTIVO, "ok");
						
						//se sube a sesi\u00F3n el valor que indica un pago total y estado liquidado de la reserva
						session.setAttribute(CODIGO_PAGADO_TOTALMENTE,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
						
						session.setAttribute(CODIGO_PAGADO_LIQUIDADO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"));
			
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
						//si en el pedido no se han realizado abonos o est\u00E1 en estado anulado
						if(vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago")) ||
								vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"))){
							//si el pedido no ha recibido abonos
							session.setAttribute(PEDIDO_SIN_PAGO, "ok");
						}else{
							//se eliminan estas variables porque ya fueron cargadas anteriormente en el m\u00E9todo "inicializarParametros(request, formulario)"
							session.removeAttribute(CotizarReservarAction.PORCENTAJE_ABONO);
							session.removeAttribute(CotizarReservarAction.VALOR_ABONO);
						}
					}else{
						String[] parametros = validacionFecha.toString().split(",");
						errors.add("FECHA_INICIAL_ESTADO_PEDIDO",new ActionMessage("errors.validacion.fechaInicialEstadoPedido","modificaci\u00F3n",parametros[0],parametros[1]));
						salida = "listado";
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					salida = "listado";
				}
				
//				//verficamos si es local de tipo aki para habilitar o desabilitar el check de precios de afiliado
//				if(CotizacionReservacionUtil.verificarFormatoNegocioPreciosAfiliado(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento(), request)){
//					session.setAttribute(HABILITADO_PRECIOS_AFILIADO, "OK");
//				}else {
//					session.removeAttribute(HABILITADO_PRECIOS_AFILIADO);
//				}
				
			}else{
				warnings.add("pedidoModificado", new ActionMessage("warnings.pedido.modificado.anteriormente", vistaPedidoDTOAux.getId().getCodigoPedido()));
				salida = "listado";
			}
			
			//cuando existen cambios de precios y la reserva se encuentra pagada totalmente o liquidada se asignan los precios originales
			if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_RESERVAS_PTO_LQD) != null){
				
				LogSISPE.getLog().info("la reserva liquidada tiene cambio de precios, se cargan los precios originales");
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				
				//se recuperan los valores de caja/mayorista originales
				CotizacionReservacionUtil.asignarPreciosCajaMayoristaOriginales(request, vistaPedidoDTO);
				
				if(vistaPedidoDTO != null){
					WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
				}
				AutorizacionesUtil.verificarAutorizacionesVariables((List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO), request, formulario);
				formulario.actualizarDescuentos(request, warnings);
				AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, warnings);	
				session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_RESERVAS_PTO_LQD);
			}

			//se cargan los datos del contacto
			ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());
			
			//Se contruyen los tabs de Contacto y Pedidos
			PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
			beanSession.setPaginaTab(tabsCotizaciones);
			
			//ejecutar el metodo para inicializar el controlador adecuado
			ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
		}
		
		//se guarda el beanSession		
		SessionManagerSISPE.setBeanSession(beanSession, request);
		
		//se guardan todos los mensajes generados
		saveWarnings(request,warnings);
		saveErrors(request,errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);

		LogSISPE.getLog().info("sale por: {}",salida);
		//Se transfiere el control a la p\u00E1gina correspondiente	
		return mapping.findForward(salida);	
	}
}
