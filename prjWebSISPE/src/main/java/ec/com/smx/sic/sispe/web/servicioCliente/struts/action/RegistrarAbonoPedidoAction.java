package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.AbonoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.FormaPagoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.TipoAbonoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.integracion.sic.SISPEIntegracion;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.RegistrarAbonoPedidoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author mbraganza
 *
 */
public class RegistrarAbonoPedidoAction extends BaseAction {
	
	private static final String PEDIDO_INFORMACION_ABONO = "ec.com.smx.sic.sispe.registro.abono.informacion";

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String salida = "listarPedidosAbonos";
		RegistrarAbonoPedidoForm formulario = (RegistrarAbonoPedidoForm) form;
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Valor Request indice: {}" , request.getParameter("indice"));
		LogSISPE.getLog().info("Valor Indice para reenviar: {}" , request.getParameter("indiceReenvio"));
		LogSISPE.getLog().info("Reenviar pedido: {}" , request.getParameter("reenviarPedido"));
		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarAbonoPedido"));
		session.setAttribute("ec.com.smx.sic.sispe.codigoEstadoPagadoTotalmente", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
		session.setAttribute("ec.com.smx.sic.sispe.codigoEstadoPagadoLiquidado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"));
		session.setAttribute("ec.com.smx.sic.sispe.codigoEstadoPagadoSinPago", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago"));
		session.setAttribute("ec.com.smx.sic.sispe.codigoEstadoPagadoParcialmente", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente"));
		
		//Buscar los pedidos
		if (request.getParameter("buscar") != null){
	        Collection pedidos = null;
	        
//	      BORRAR ------------------------
			LogSISPE.getLog().info("Llama a m\u00E9todo x9");
			// -------------------------------
			
	        VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
	        StringBuilder codigoEstado = new StringBuilder();
	        
	        //estados del pedido
	        codigoEstado.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
    			.append(",")
    			.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
    			.append(",")
    			.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
    			.append(",")
    			.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
    			.append(",")
    			.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))
    			.append(",")
    			.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservaConfirmada"));
	        
	        //desde la opcion de ver abonos se debe incluir los pedidos anulados
	        if(session.getAttribute("ec.com.smx.sic.sispe.reenviarModificarAbonos")!=null){
	        	codigoEstado.append(",").append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"));
    		}
	        
	        consultaPedidoDTO.getId().setCodigoEstado(codigoEstado.toString());
	        
	        LogSISPE.getLog().info("RegistrarAbonoPedido - estados: {}" , consultaPedidoDTO.getId().getCodigoEstado());
	        
	        //Buscar por estado actual [SI]
	        consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

	        try {
	          //se obtiene la colecci\u00F3n de Pedidos buscados y se la almacena en sesi\u00F3n
	          pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
	          if (pedidos == null || pedidos.isEmpty()){
	            infos.add("listaVacia", new ActionMessage("message.listaVaciaBusqueda","Pedidos"));
	            //saveInfos(request, infos);
	            session.removeAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
	            session.removeAttribute("ec.com.smx.sic.sispe.pedidos");
	          } else {
	            Collection datosSub = formulario.paginarDatos(pedidos, 0,pedidos.size(),false);
	            session.setAttribute("ec.com.smx.sic.sispe.vistasPedidosAbonos", pedidos);
            	session.setAttribute("ec.com.smx.sic.sispe.pedidos.subPagina", datosSub);
            	session.setAttribute("ec.com.smx.sic.sispe.pedidos", pedidos);
            	LogSISPE.getLog().info("Coleccion de pedidos para abonos: {}" , session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina"));
	            
	            /*if (datosSub.size() == 1){
	            	obtenerDatosParaRegistroAbonos((ArrayList) datosSub, 0, request, errors);
	            	salida = "registrarAbonos";
	            } else {
	            	session.setAttribute("ec.com.smx.sic.sispe.vistasPedidosAbonos", pedidos);
	            	session.setAttribute("ec.com.smx.sic.sispe.pedidos.subPagina", datosSub);
	            	
	            	LogSISPE.getLog().info("Coleccion de pedidos para abonos: " + session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina"));
	            }*/ 
            		            
	          }
	        } catch(SISPEException ex){
	          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
	          errors.add("errorBuscar", new ActionMessage("errors.busqueda", ""));
	        }
	       
		}
		
		//Paginaci\u00F3n
		else if (request.getParameter("start") != null){
			try{
				Collection vistasPedidos = (Collection) session.getAttribute("ec.com.smx.sic.sispe.vistasPedidosAbonos");
				Collection datosPaginados = null;
				
				if (vistasPedidos != null){
					datosPaginados = formulario.paginarDatos(vistasPedidos, Integer.parseInt(request.getParameter("start")),vistasPedidos.size(),false);
					session.setAttribute("ec.com.smx.sic.sispe.pedidos.subPagina", datosPaginados);
					session.setAttribute("ec.com.smx.sic.sispe.inicioPaginacionVistasPedidosAbonos", request.getParameter("start"));
				}
			} catch (Exception e){
				LogSISPE.getLog().info("Error al paginar al mostrar la lista de pedidos a abonar");
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
		}
		
		//Se selecciona un pedido para registrar los abonos
		else if (request.getParameter("indice") != null){
			try{
				ArrayList vistasPedidos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
				int nVistaPedido = Integer.parseInt(request.getParameter("indice"));
				
				VistaPedidoDTO vistaPedidoActual = obtenerDatosParaRegistroAbonos(vistasPedidos, nVistaPedido, request, errors); 
				if (vistaPedidoActual  != null){
					
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.meses.atras.prohibido.pagar.reservas", request);
					
					if(parametroDTO.getValorParametro() != null){
						Integer numeroMeses = Integer.valueOf(parametroDTO.getValorParametro());
						
						if(validarPedidoActivoParaAbonar(request, vistaPedidoActual, numeroMeses, warnings)){
							LogSISPE.getLog().info("El pedido es activo para realizar el abono");
							session.setAttribute("ec.com.smx.sic.sispe.indicePedidoParaAbonar", nVistaPedido);
							LogSISPE.getLog().info("seleccion vistaPedidoAbonos: {}" , session.getAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos"));
							MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
							salida = "registrarAbonos";
						}else{
							LogSISPE.getLog().info("El pedido no esta activo para realizar un abono porque fue reservado hace varios meses");
							salida = "listarPedidosAbonos";
						}
					}
				} else {
					errors.add("registroAbono", new ActionMessage("errors.abonoPedidoAnulado"));
				}
				
			}catch(Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}
		
		//Mostrar la pregunta para regresar al listado de pedidos
		else if(request.getParameter("volverBuscar") != null){
			LogSISPE.getLog().info("regresar a busqueda");
			
			if ((formulario.getValorAbono() != null && formulario.getValorAbono().equals("") == false) 
					|| (formulario.getComboFormasPago() != null && formulario.getComboFormasPago().equals("") == false)
					|| (formulario.getCodigoCaja() != null && formulario.getCodigoCaja().equals("") == false)
					|| (formulario.getCodigoCajero() != null && formulario.getCodigoCajero().equals("") == false)
					|| (formulario.getNumeroTransaccionAbono() != null && formulario.getNumeroTransaccionAbono().equals("") == false)
					|| (formulario.getObservacionAbono() != null && formulario.getObservacionAbono().equals("") == false)){
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.regresarABusqueda", 
						"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
				
			} else {
				request.setAttribute("ec.com.sxm.sic.sispe.regresarSinConfirmacion", "");
				MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
			}
			salida = "registrarAbonos";
		}
		
		//Mostrar la pregunta para reenviar el pedido al punto de venta
		else if(request.getParameter("reenviarPedido") != null){
			LogSISPE.getLog().info("reenviar reserva a punto de venta");
			LogSISPE.getLog().info("Indice para reenviar: {}" , request.getParameter("indiceReenvio"));
			session.setAttribute("ec.com.smx.sic.sispe.indicePedidoReenviar", request.getParameter("indiceReenvio"));
			LogSISPE.getLog().info("Indice en sesion para reenviar al ver pregunta: {}", session.getAttribute("ec.com.smx.sic.sispe.indicePedidoReenviar"));
			WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.reenviarPedidoPOS", 
					"\u00BFDesea reenviar los datos de la reserva al punto de venta?", "siReenviarPedido", null, request);
		}
		
		//Se presiona el bot\u00F3n si para regresar al listado de pedidos
		else if (formulario.getBotonSi() != null && formulario.getBotonSi().equals("siVolverBusqueda")){
			LogSISPE.getLog().info("regresar desde el registro del abono");
			Collection vistasPedidos = (Collection) session.getAttribute("ec.com.smx.sic.sispe.vistasPedidosAbonos");
			Collection datosPaginados = null;
			int inicioPaginacion = 0;
			
			if (vistasPedidos != null){
				inicioPaginacion = (session.getAttribute("ec.com.smx.sic.sispe.inicioPaginacionVistasPedidosAbonos") == null) ? 0 : 
					Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.inicioPaginacionVistasPedidosAbonos").toString());
				datosPaginados = formulario.paginarDatos(vistasPedidos, inicioPaginacion,vistasPedidos.size(),false);
				session.setAttribute("ec.com.smx.sic.sispe.pedidos.subPagina", datosPaginados);
			}
			MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
			//SessionManagerSISPE.removeVarSession(request);
		}
		
		//Se presiona el bot\u00F3n si para reenviar los datos del pedido al punto de venta
		else if (formulario.getBotonSi() != null && formulario.getBotonSi().equals("siReenviarPedido")){
			try{
				LogSISPE.getLog().info("si reenviar los datos del pedido al punto de venta");
				
				PedidoDTO pedidoDTO = null;
				EstadoPedidoDTO estadoPedidoDTO = null;
				ArrayList vistasPedidos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
				LogSISPE.getLog().info("indice en sesion para reenvio: {}" , session.getAttribute("ec.com.smx.sic.sispe.indicePedidoReenviar"));
				int nVistaPedido = Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.indicePedidoReenviar").toString());
				VistaPedidoDTO vistaPedido = (VistaPedidoDTO) vistasPedidos.get(nVistaPedido);
				
				pedidoDTO = new PedidoDTO();
				pedidoDTO.getId().setCodigoCompania(vistaPedido.getId().getCodigoCompania());
				pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedido.getId().getCodigoAreaTrabajo());
				pedidoDTO.getId().setCodigoPedido(vistaPedido.getId().getCodigoPedido());
				pedidoDTO.setEntidadResponsable(vistaPedido.getEntidadResponsable());
				LogSISPE.getLog().info("entidadResponsableAbono: {}" , pedidoDTO.getEntidadResponsable());
				pedidoDTO.setNpCedulaContacto(vistaPedido.getCedulaContacto());
				pedidoDTO.setNpLlaveContratoPOS(vistaPedido.getLlaveContratoPOS());
				pedidoDTO.setNpRucEmpresa(vistaPedido.getRucEmpresa());
				pedidoDTO.setContextoPedido(vistaPedido.getContextoPedido());
				
				estadoPedidoDTO = new EstadoPedidoDTO();
				estadoPedidoDTO.getId().setCodigoEstado(vistaPedido.getId().getCodigoEstado());
				estadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedido.getId().getSecuencialEstadoPedido());
				estadoPedidoDTO.setTotalPedido(vistaPedido.getTotalPedido());
				estadoPedidoDTO.setValorAbonoInicialManual(vistaPedido.getValorAbonoInicialManual());
				estadoPedidoDTO.setValorAbonoInicialSistema(vistaPedido.getValorAbonoInicialSistema());
				estadoPedidoDTO.setValorCostoEntregaPedido(vistaPedido.getValorCostoEntregaPedido());
				estadoPedidoDTO.setEstadoCalculosIVA(vistaPedido.getEstadoCalculosIVA());
				estadoPedidoDTO.setLlaveContratoPOS(vistaPedido.getLlaveContratoPOS());
				
				//se verifica si el valor total del pedido es igual a abono inicial
				if(estadoPedidoDTO.getValorAbonoInicialManual()!= null && estadoPedidoDTO.getValorAbonoInicialManual().doubleValue() == estadoPedidoDTO.getTotalPedido().doubleValue()){
					//se resta un par\u00E1metro prestablecido al valor de abono manual
					ParametroID parametroFiltro = new ParametroID();
					parametroFiltro.setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
					parametroFiltro.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.valorDolaresRestarAbonoTotalReserva"));
					ParametroDTO parametroDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametroFiltro);
					if(parametroDTO != null){
						estadoPedidoDTO.setValorAbonoInicialManual(estadoPedidoDTO.getValorAbonoInicialManual().doubleValue() - Double.parseDouble(parametroDTO.getValorParametro()));
					}
				}
				
				pedidoDTO.setEstadoPedidoDTO(estadoPedidoDTO);
				
				vistaPedido.setLlaveContratoPOS(SISPEIntegracion.generarConfirmacionReservacionPedidoPOS(pedidoDTO));
				messages.add("abonoPedido", new ActionMessage("message.exito.registrarAbonoPedido", vistaPedido.getId().getCodigoPedido(), vistaPedido.getLlaveContratoPOS()));
			} catch(Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				LogSISPE.getLog().info("Error al reenviar los datos del pedido al POS");
				errors.add("abonoPedido", new ActionMessage("errors.reenvioDatosPedido"));
			}
		}
		
		//Registrar el abono del pedido
		else if (request.getParameter("registrarAbono")!= null){
			VistaPedidoDTO vistaPedidoActual = null;
			AbonoPedidoDTO abonoPedidoDTO = null;
			ArrayList vistasPedidos = null;
			int indicePedido = 0;
			
			try{
				LogSISPE.getLog().info("Registrar Abono");
				vistaPedidoActual = (VistaPedidoDTO) session.getAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos");
				
				if (vistaPedidoActual != null){
					abonoPedidoDTO = new AbonoPedidoDTO();
					abonoPedidoDTO.getId().setCodigoCompania(vistaPedidoActual.getId().getCodigoCompania());
					abonoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoActual.getId().getCodigoAreaTrabajo());
					abonoPedidoDTO.getId().setCodigoPedido(vistaPedidoActual.getId().getCodigoPedido());
					//abonoPedidoDTO
					abonoPedidoDTO.setSecuencialAbonoPOS(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.noAplica"));
					abonoPedidoDTO.setCodigoCajaAbono(formulario.getCodigoCaja());
					abonoPedidoDTO.setCodigoCajeroAbono(formulario.getCodigoCajero());
					abonoPedidoDTO.setCodigoFormaPago(formulario.getComboFormasPago());
					abonoPedidoDTO.setCodigoLocalAbono(SessionManagerSISPE.getCurrentLocal(request));
					//abonoPedidoDTO.setLlaveContratoPOS(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.noAplica"));
					abonoPedidoDTO.setLlaveContratoPOS(vistaPedidoActual.getLlaveContratoPOS());
					abonoPedidoDTO.setNumeroTransaccionAbono(formulario.getNumeroTransaccionAbono());
					abonoPedidoDTO.setUsuarioRegistroAbono(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					//coloca el numero de factura(2 campos) y la observacion por consiguiente separado por un pipe |
					abonoPedidoDTO.setObservacionAbono(formulario.getPuntoEmisionFactura()+formulario.getNumeroFactura()+"|"+formulario.getObservacionAbono());
					abonoPedidoDTO.setValorAbono(new Double(formulario.getValorAbono()));
					abonoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
					//correcci\u00F3n de los decimales
					DecimalFormat twoDigit = new DecimalFormat("0.00");
					vistaPedidoActual.setSaldoAbonoPedido(Double.valueOf(twoDigit.format(vistaPedidoActual.getSaldoAbonoPedido()).replace(",", ".")));
					
					//Tipo de abono: inicial, abono, final
					if (vistaPedidoActual.getSaldoAbonoPedido().doubleValue() == vistaPedidoActual.getTotalPedido().doubleValue()){
						if (abonoPedidoDTO.getValorAbono().doubleValue() == vistaPedidoActual.getSaldoAbonoPedido().doubleValue()){
							abonoPedidoDTO.setCodigoTipoAbono(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAbono.cuotaFinal"));
						} else {
							abonoPedidoDTO.setCodigoTipoAbono(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAbono.cuotaInicial"));							
						}
					} else {
						if (abonoPedidoDTO.getValorAbono().doubleValue() == vistaPedidoActual.getSaldoAbonoPedido().doubleValue()){
							abonoPedidoDTO.setCodigoTipoAbono(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAbono.cuotaFinal"));
						} else {
							abonoPedidoDTO.setCodigoTipoAbono(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAbono.abonoAreserva"));
						}
					}
					
					//abonoPedidoDTO.setCodigoTipoAbono(formulario.getComboTiposAbonos());
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarPagoPedido(abonoPedidoDTO, vistaPedidoActual);
					messages.add("abonoPedido", new ActionMessage("message.exito.registrarAbonoPedido", vistaPedidoActual.getId().getCodigoPedido(), vistaPedidoActual.getLlaveContratoPOS()));
					MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
					try{
						vistasPedidos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
						indicePedido = (Integer) session.getAttribute("ec.com.smx.sic.sispe.indicePedidoParaAbonar");
						vistaPedidoActual = this.obtenerVistaPedidoActualConAbonos(vistasPedidos, indicePedido);
						vistasPedidos.set(indicePedido, vistaPedidoActual);
					}catch(Exception e){
						LogSISPE.getLog().info("Error al obtener el pedido actual al registrar el abono");
						LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
						session.removeAttribute("ec.com.smx.sic.sispe.inicioPaginacionVistasPedidosAbonos");
						session.removeAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
						//SessionManagerSISPE.removeVarSession(request);
					}
				}
			} catch (Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				errors.add("abonoPedido", new ActionMessage("errors.registroAbonoPedido", e.getMessage()));
				salida = "registrarAbonos";
			}
		}else if (request.getParameter("actualizarAbonoPedido") != null){
			
			LogSISPE.getLog().error("Actualizar la pagina de abonos");			
			try{
				
				ArrayList vistasPedidos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.pedidos.subPagina");
				int nVistaPedido = (Integer)session.getAttribute("ec.com.smx.sic.sispe.indicePedidoParaAbonar");
				
				if (obtenerDatosParaRegistroAbonos(vistasPedidos, nVistaPedido, request, errors) != null){
					session.setAttribute("ec.com.smx.sic.sispe.indicePedidoParaAbonar", nVistaPedido);
					LogSISPE.getLog().info("seleccion vistaPedidoAbonos: {}" , session.getAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos"));
					MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
					salida = "registrarAbonos";
				} else {
					errors.add("registroAbono", new ActionMessage("errors.abonoPedidoAnulado"));
				}
				
			}catch(Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
		}
				
		//Primer ingreso a la p\u00E1gina
		else {
			EstadoSICDTO estadoBusqueda = null;
			Collection estadosPago = null;
			SessionManagerSISPE.removeVarSession(request);
			session.setAttribute("WebSISPE.tituloVentana","Pedidos");
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarAbonoPedido"));
			
			//Estados de pago del pedido
			estadoBusqueda = new EstadoSICDTO();
			estadoBusqueda.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.caracteristicaEstado"));
			estadosPago = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(estadoBusqueda);
			session.setAttribute("ec.com.smx.sic.sispe.estadosPago", estadosPago);
			
			//Datos del formulario
			formulario.setEtiquetaFechas("Fecha de Estado");
			session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");
						
		}
		
		LogSISPE.getLog().info("salida registroabonos: {}" , salida);
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveWarnings(request, warnings);
		saveMessages(request, messages);
		return mapping.findForward(salida);
	}
	
	
	/**
	 * 
	 * @param vistaPedidoDTO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Collection obtenerAbonosPedido(VistaPedidoDTO vistaPedidoDTO) throws Exception{
		Collection abonosPedido = null;
		AbonoPedidoDTO abonoBusqueda = new AbonoPedidoDTO();
		
		abonoBusqueda.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
		abonoBusqueda.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		abonoBusqueda.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
		abonoBusqueda.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
		//numero de reservacion
		abonoBusqueda.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
		abonoBusqueda.setFormaPagoDTO(new FormaPagoDTO());
		abonoBusqueda.setTipoAbonoDTO(new TipoAbonoDTO());
		abonosPedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerAbonoPedido(abonoBusqueda);
		//request.getSession().setAttribute("ec.com.smx.sic.sispe.abonosPedido", abonosPedido);
		return abonosPedido;
	}
	
	/**
	 * 
	 * @param vistasPedidos
	 * @param nVistaPedido
	 * @return
	 * @throws Exception
	 */
	private VistaPedidoDTO obtenerVistaPedidoActualConAbonos(ArrayList vistasPedidos, int nVistaPedido) throws Exception{
		VistaPedidoDTO vistaPedidoActual = null;
		VistaPedidoDTO vistaPedidoBusqueda = null;
		Collection vistasPedidoConsulta = null;

		if (nVistaPedido > -1 && nVistaPedido < vistasPedidos.size()){
			LogSISPE.getLog().info("indiceAbonos: {}" , nVistaPedido);
			vistaPedidoActual = (VistaPedidoDTO) vistasPedidos.get(nVistaPedido);
			//Consultar nuevamente la vistaPedidoActual
			vistaPedidoBusqueda = new VistaPedidoDTO();
			vistaPedidoBusqueda.getId().setCodigoCompania(vistaPedidoActual.getId().getCodigoCompania());
			vistaPedidoBusqueda.getId().setCodigoAreaTrabajo(vistaPedidoActual.getId().getCodigoAreaTrabajo());
			vistaPedidoBusqueda.getId().setCodigoPedido(vistaPedidoActual.getId().getCodigoPedido());
			vistaPedidoBusqueda.setLlaveContratoPOS(vistaPedidoActual.getLlaveContratoPOS());
			vistaPedidoBusqueda.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			vistasPedidoConsulta = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoBusqueda);
			if (vistasPedidoConsulta.isEmpty() == false){
				vistaPedidoActual = (VistaPedidoDTO) vistasPedidoConsulta.iterator().next();
			}
			vistaPedidoActual.setAbonosPedidos(obtenerAbonosPedido(vistaPedidoActual));
		}
		return vistaPedidoActual;
	}
	
	/**
	 * 
	 * @param vistasPedidos
	 * @param nVistaPedido
	 * @param request
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	private VistaPedidoDTO obtenerDatosParaRegistroAbonos(ArrayList vistasPedidos, int nVistaPedido, HttpServletRequest request, ActionMessages errors) throws Exception{
		FormaPagoDTO formaPagoBusqueda = null;
		//TipoAbonoDTO tipoAbonoBusqueda = null;
		VistaPedidoDTO vistaPedidoActual = null;
		Collection formasPagoAbonos = null;
		//Collection<TipoAbonoDTO> tiposAbonos = null;
		
		vistaPedidoActual = this.obtenerVistaPedidoActualConAbonos(vistasPedidos, nVistaPedido);
		
		if (vistaPedidoActual.getEstadoActual().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"))){
			return null;
		}
		
		if (vistaPedidoActual.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente")) == false 
				&& request.getSession().getAttribute("ec.com.smx.sic.sispe.formasPagoAbonos") == null){
			formaPagoBusqueda = new FormaPagoDTO();
			formaPagoBusqueda.getId().setCodigoCompania(vistaPedidoActual.getId().getCodigoCompania());
			formaPagoBusqueda.setEstadoFormaPago(SessionManagerSISPE.getEstadoActivo(request));
			formasPagoAbonos = SessionManagerSISPE.getServicioClienteServicio().transObtenerFormaPago(formaPagoBusqueda);
			if (formasPagoAbonos.isEmpty() == false){
				request.getSession().setAttribute("ec.com.smx.sic.sispe.formasPagoAbonos", formasPagoAbonos);
			} else {
				errors.add("errorBuscar", new ActionMessage("errors.obtenerDatosNecesarios", "formas de pago"));
			}
		}
		ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoActual);
		request.getSession().setAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos", vistaPedidoActual);
		request.getSession().setAttribute("ec.com.smx.sic.sispe.abonos.saldoPedidoActual", (vistaPedidoActual.getSaldoAbonoPedido() != null) ? vistaPedidoActual.getSaldoAbonoPedido() : new Double(0D));
		Double valorAbonoInicial = 0d;
		if(vistaPedidoActual.getValorAbonoInicialManual() != null){
			valorAbonoInicial = vistaPedidoActual.getValorAbonoInicialManual();
			/* TODO: incialmente se solicit\u00F3 asi
			if(vistaPedidoActual.getValorAbonoInicialManual().doubleValue() <= vistaPedidoActual.getValorAbonoInicialSistema().doubleValue())
				valorAbonoInicial = vistaPedidoActual.getValorAbonoInicialManual();
			else
				valorAbonoInicial = vistaPedidoActual.getValorAbonoInicialSistema();*/
		}
		request.getSession().setAttribute("ec.com.smx.sic.sispe.abonos.valorAbonoInicial", valorAbonoInicial);
		request.getSession().setAttribute("ec.com.smx.sic.sispe.abonos.estadoPagoPedido", vistaPedidoActual.getCodigoEstadoPagado());
		
		//wc validar si abono es correcto con el POS
		validarPedido(vistaPedidoActual, request);
		
		
		return vistaPedidoActual;
	}	
	
	//wc validar si abono es correcto con el POS
	public static void validarPedido(VistaPedidoDTO vistaPedidoDTO, HttpServletRequest request){
		request.getSession().removeAttribute(PEDIDO_INFORMACION_ABONO);
		if(vistaPedidoDTO.getAbonoPedido()!=null){
			if((vistaPedidoDTO.getTotalPedido()>vistaPedidoDTO.getAbonoPedido()) && vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))){
				request.getSession().setAttribute(PEDIDO_INFORMACION_ABONO, "advertencia");				
			}else if ((vistaPedidoDTO.getTotalPedido()<vistaPedidoDTO.getAbonoPedido()) && vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))) {
				request.getSession().setAttribute(PEDIDO_INFORMACION_ABONO, "credito");				
			}else if (vistaPedidoDTO.getTotalPedido()==vistaPedidoDTO.getAbonoPedido()) {
				request.getSession().setAttribute(PEDIDO_INFORMACION_ABONO, "exito");				
			}else {
				request.getSession().setAttribute(PEDIDO_INFORMACION_ABONO, "noPagadoTotalmente");				
			}			
		}		
	}
	
	
	/**
	 * Valida si el pedido fue reservado maximo hace numeroMeses meses atras, si el pedido esta fuera del plazo pero tiene algun abono realizado
	 * se permite abonar el pedido
	 * @param request
	 * @param vistaPedidoDTO
	 * @param numeroMeses
	 * @return true si se puede abonar la reserva, false caso contrario
	 * @throws Exception
	 * @author bgudino
	 */
	private static Boolean validarPedidoActivoParaAbonar(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO, Integer numeroMeses, ActionMessages warnings) throws Exception{
		
		LogSISPE.getLog().info("validando si el pedido esta dentro del plazo activo para poder realizar el abono");
		
		Date fechaPedido = new Date();
		
		if(vistaPedidoDTO != null){
			
			
			//se verifica si el pedido no tiene abonos registrados
			if(CollectionUtils.isEmpty(vistaPedidoDTO.getAbonosPedidos())){
				
				//se consulta el estadoPedidoDTO
				EstadoPedidoDTO estadoPedidoConsulta = new EstadoPedidoDTO();
				estadoPedidoConsulta.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				estadoPedidoConsulta.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				estadoPedidoConsulta.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
				estadoPedidoConsulta.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
				estadoPedidoConsulta.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
				
				EstadoPedidoDTO estadoPedidoDTO = SISPEFactory.getDataService().findUnique(estadoPedidoConsulta);
				
				if(estadoPedidoDTO != null){
					
					Calendar fechaMaximaPermitida = Calendar.getInstance();
					fechaMaximaPermitida.add(Calendar.MONTH, (numeroMeses*-1));
					fechaPedido = estadoPedidoDTO.getFechaInicialEstado();
					LogSISPE.getLog().info("fecha del pedido: "+fechaPedido);
					LogSISPE.getLog().info("fecha maxima permitida para realizar abonos: "+fechaMaximaPermitida.getTime());
					
					//se comparan las fechas
					if(estadoPedidoDTO.getFechaInicialEstado().compareTo(fechaMaximaPermitida.getTime()) >= 0){
						return Boolean.TRUE;
					}
				}
			}else{
				//si el pedido ya tiene registrado algun abono se deja pagar la reserva
				return Boolean.TRUE;
			}
		}
		warnings.add("fechaMaximaPermitida", new ActionMessage("warnings.fecha.maxima.permitida",numeroMeses, DateManager.getYMDDateFormat().format(fechaPedido)));
		return Boolean.FALSE;
	}
	
}