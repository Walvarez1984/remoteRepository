package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.PereciblesUtil;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

public class EnviarOrdenCompraPereciblesAction extends BaseAction  {
	
	public static final String COLECCION_LOCALES = "sispe.vistaEstablecimientoCiudadLocalDTO";
	public static final String CARACTER_SEPARACION = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
	
	/**
	 * Accion para redireccionar a la pantalla de workListBPM
	 * 
	 * @param mapping 			mapeo del archivo struts-config.xml
	 * @param form 			    formulario utilizado en la acci&oacute;n
	 * @param request 			petici&oacute;n
	 * @param response 			respuesta
	 * @exception Exception		excepci&oacute;n
	 * @return forward 			a direccionar
	 */	

	@SuppressWarnings({ "unchecked", "unused"})
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("Entrando a EnviarOrdenCompraPereciblesAction");

		//Declaraciones e inicializaciones
		HttpSession session = request.getSession();
		ActionMessages exitos = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages info = new ActionMessages();
		ActionMessages warnings = new ActionMessages();
		ActionErrors error = new ActionErrors();
		ListadoPedidosForm formulario = (ListadoPedidosForm) form;
       	String forward = "desplegar";
     	//se verifica el valor de la petici\u00F3n
      	String peticion = request.getParameter("ayuda");
       	
      //se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		copiarFiltrosBusqueda(beanSession, formulario);
		
		try{   	        	   
			LogSISPE.getLog().info("Ingreso a cargar EnviarOrdenCompraPereciblesAction" );
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.envio.ordencompra.perecibles"));
			
			
			 if(request.getParameter("buscar")!=null) {
				LogSISPE.getLog().info( "Buscando articulos para enviar al proveedor");
				
				//tab consolidar pedido 
				if (beanSession.getPaginaTab().getTabSeleccionado() == 0) {/*Consultar consolidado pedido orden de compra*/
					
					Collection<Map<String, Object>> colec = PereciblesUtil.getInstancia().consultarConsolidadoPedido(request, formulario, errors, info);
					if (CollectionUtils.isEmpty(colec)) {
						info.add("listaVacia", new ActionMessage("message.listaVaciaBusqueda", "registros"));
						request.getSession().removeAttribute(PereciblesUtil.COLECCION_CONSOLIDADO_PEDIDO);
						
					} else {
						request.getSession().setAttribute(PereciblesUtil.COLECCION_CONSOLIDADO_PEDIDO, colec);
					}
					
				} else if (beanSession.getPaginaTab().getTabSeleccionado() == 1) {/*stock perecibles*/
					// se obtiene la lista de articulos perecibles por stock
					Collection<Map<String, Object>> colec = PereciblesUtil.getInstancia().consultaStockPerecibles(request, formulario, errors, info);
					
					if (CollectionUtils.isEmpty(colec)) {
						info.add("listaVacia", new ActionMessage("message.listaVaciaBusqueda", "registros"));
						request.getSession().removeAttribute(PereciblesUtil.COLECCION_PERECIBLES);
						
					} else {
						request.getSession().setAttribute(PereciblesUtil.COLECCION_PERECIBLES, colec);
					}
					
				}
				

			} else if (request.getParameter("obtenerDetalleArticulo") != null) {
				
				Map<String, Object> articuloSeleccionado = PereciblesUtil.getInstancia().obtenerArticuloSeleccionado(request, formulario, errors, info);
				
				request.getSession().setAttribute(PereciblesUtil.ARTICULO_PERECIBLE_SELECCIONADO, articuloSeleccionado);
				forward = "desplegarDetalleArticulo";
				
			} else if (null != peticion && peticion.equals("atrasStockPerecibles")) {
				request.getSession().removeAttribute(PereciblesUtil.ARTICULO_PERECIBLE_SELECCIONADO);
				
			} else if (request.getParameter("registrarNuevoMovimiento") != null) {/*Abrir popup nuevo movimiento*/
				session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				
				PereciblesUtil.getInstancia().crearPopUpNuevoMovimiento(formulario, request);
				forward = "desplegarDetalleArticulo";
				
			} else if (request.getParameter("nuevoMovimiento") != null) {/*Guardar nuevo movimiento*/
				
				LogSISPE.getLog().info("Codigo Motivo: {}", formulario.getComboMotivoMovimiento());
				LogSISPE.getLog().info("Cantidad: {}", formulario.getCantidadMotivoMovimiento());
				LogSISPE.getLog().info("Nuevo movimiento registrado");
				PereciblesUtil.getInstancia().guardarDetalleMovimientoKardex(request, form, errors, info, exitos, formulario);
//				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
				forward = "desplegarDetalleArticulo";
				
			} else if (request.getParameter("cancelarNuevoMovimiento") != null) {
				forward = "desplegarDetalleArticulo";
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
				
			} else if (request.getParameter("actualizarDatosArticulo") != null) {
				//actualizar
				Map<String, Object> articuloSeleccionado  = (Map<String, Object>) request.getSession().getAttribute(PereciblesUtil.ARTICULO_PERECIBLE_SELECCIONADO);
				
				ArticuloDTO articuloDTOSeleccionado = (ArticuloDTO) articuloSeleccionado.get("articulo");
				PereciblesUtil.getInstancia().actualizarDatos(request, articuloDTOSeleccionado, formulario);
				
				forward = "desplegarDetalleArticulo";
				
			}
			 
			else if (request.getParameter("incluirDetalle") != null) { /*Cuando selecciona la opcion crear xls*/
				Collection<Map<String,Object>> colStockPredecibles = (Collection<Map<String,Object>>) request.getSession().getAttribute(PereciblesUtil.COLECCION_PERECIBLES);
				
				if (colStockPredecibles == null) {
					info.add("Buscar", new ActionMessage("message.busqueda.antes.generar.archivo.stock.perecibles"));
					
				} else {
					PereciblesUtil.getInstancia().crearPopUpIncluirDetalle(request);
				}
				
				formulario.setOpConfirmarIncluirDetalle(null);
			}
			
			
			else if (request.getParameter("ordenCompra") != null) { /*Cuando selecciona la opcion orden compra*/
				PereciblesUtil.getInstancia().enviarOrdenCompra(request, formulario, errors, info, exitos);
			}
			 
			else if (request.getParameter("cancelarIncluirDetalle") != null) {
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
			}
			 
			 
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				int tabSeleccionado=beanSession.getPaginaTab().getTabSeleccionado();
				cambiarTabsMonitorPedidos(beanSession, tabSeleccionado, formulario, request, errors, info);
			} 
			
			 
			else if (peticion != null && peticion.equals("xls")) {
				LogSISPE.getLog().info("GENERAR ARCHIVO XLS");
				
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);/*remove pop up*/
				LogSISPE.getLog().info("Info check: {}", formulario.getOpConfirmarIncluirDetalle());
				
				byte [] byteBuffer = PereciblesUtil.getInstancia().crearExceReporteStockPerecibles(request, formulario );
				
				if (byteBuffer != null && byteBuffer.length > 0) {
					
					String fileName =  WebSISPEUtil.generarNombreArchivo("Stock Perecibles", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel"));
					
					response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					String contentType = "application/ms-excel";
					response.setContentType(contentType);
					ServletOutputStream outputStream = response.getOutputStream();
					outputStream.write(byteBuffer);
					outputStream.flush();
					outputStream.close();
					forward=null;
				}
			}
			 
			else if (request.getParameter("mostrarDetallePedido") != null) {
				
				LogSISPE.getLog().info("MOSTRAR DETALLE PEDIDO");
				
				if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
					session.setAttribute(DetalleEstadoPedidoAction.RESPALDO_TABS, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
				}
				
				VistaPedidoDTO vistaPedidoDTO = PereciblesUtil.getInstancia().obtenerPedidoSeleccionado(request);									
				
				EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
				DetalleEstadoPedidoAction.obtenerEntregas(session, vistaPedidoDTO.getVistaDetallesPedidosReporte());
				WebSISPEUtil.verificarDiferidosPedido(request);
				
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				
				PaginaTab tabDetallePedido= new PaginaTab("detalleEstadoPedido", "deplegar", 0,335, request);
				Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
				tabDetallePedido.addTab(tabDetallePedidoComun);
				if(CollectionUtils.isNotEmpty((Collection<EntregaPedidoDTO>)session.getAttribute(DetalleEstadoPedidoAction.RESUMEN_ENTREGAS))){
					Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
					tabDetallePedido.addTab(tabDetalleEntregas);	
				}
				beanSession.setPaginaTab(tabDetallePedido);
				
				request.getSession().setAttribute(PereciblesUtil.MONITOR_PERECIBLES_ATRAS, "ok");
				forward = "desplegarDetallePedido";
				
			}
			 
			else if (request.getParameter("atrasMonitorPerecibles") != null) {
				if(session.getAttribute(DetalleEstadoPedidoAction.RESPALDO_TABS)!= null){
					session.setAttribute("ec.com.smx.sic.controlesusuario.tab", session.getAttribute(DetalleEstadoPedidoAction.RESPALDO_TABS));
					beanSession.setPaginaTab((PaginaTab)session.getAttribute(DetalleEstadoPedidoAction.RESPALDO_TABS));
				}
				request.getSession().removeAttribute(PereciblesUtil.MONITOR_PERECIBLES_ATRAS);
				forward = "desplegar";
			}
			
			 
			else if ((request.getParameter("range")!=null || request.getParameter("start")!=null)) {
				LogSISPE.getLog().info("****Entro a la paginaci\u00F3n movimiento kardex****");
			        
				String start = String.valueOf(request.getParameter("start"));
				Map<String, Object> articuloSeleccionado = (Map<String, Object>) request.getSession().getAttribute(PereciblesUtil.ARTICULO_PERECIBLE_SELECCIONADO);
				
				PereciblesUtil.getInstancia().cargarDetallesMovimientos (articuloSeleccionado, request, Integer.valueOf(start), formulario);
					
		        session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
		        forward = "desplegarDetalleArticulo";
			}
			 
			else {
				// se realiza la consulta de los locales
				if (session.getAttribute(SessionManagerSISPE.COLECCION_LOCALES) == null) {
					SessionManagerSISPE .getColeccionVistaEstablecimientoCiudadLocalDTO(request);
				}

				// se realiza la consulta de estados
				EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
				
				consultaEstadoDTO .getId().setCodigoEstado( 
						MessagesAplicacionSISPE.getString( "ec.com.smx.sic.sispe.estado.pendiente").concat(CARACTER_SEPARACION) 
						.concat(MessagesAplicacionSISPE .getString("ec.com.smx.sic.sispe.estado.enviado.ordencompra")));
				
				// Obtener datos de la colecci\u00F3n de estados, en la base de datos
				Collection<?> estados = SessionManagerSISPE .getServicioClienteServicio().transObtenerEstado( consultaEstadoDTO);
				// guardar en sesi\u00F3n esta colecci\u00F3n
				session.setAttribute(SessionManagerSISPE.COL_ESTADOS, estados);
				// No mostrar el check de estado actual
				session.setAttribute( "ec.com.smx.sic.sispe.estado.actual", MessagesAplicacionSISPE .getString("ec.com.smx.sic.sispe.estado.afirmacion"));

				// cargar las fechas de despacho por defecto
				Date fechaActual = DateManager.getCurrentDate();
				GregorianCalendar fechaCalendario = new GregorianCalendar();
				fechaCalendario.setTime(fechaActual);
				fechaCalendario.add(Calendar.DAY_OF_MONTH, 2);

				formulario.setFechaInicial(DateManager.getYMDDateFormat() .format(fechaActual));
				formulario.setFechaFinal(DateManager.getYMDDateFormat().format( fechaCalendario.getTime()));
				formulario.setOpcionFecha(MessagesWebSISPE .getString("ec.com.smx.sic.sispe.opcion.fechas"));

				PereciblesUtil.getInstancia().consultarMotivosMovimientos( request);

				// Crear los tabs para el monitor de stock
				beanSession.setPaginaTab(construirTabsMonitorStock(request));
			}
		}catch( Exception e ){	
			// en caso de error, se carga el error general y se envia a la pagina de error estabecida por la accion general:
			LogSISPE.getLog().error("Error metodo execute{}",e);
			forward = "error";
		}
		
		saveMessages(request, exitos);
		saveInfos(request, info);
		saveWarnings(request, warnings);
		saveErrors(request, errors);
		SessionManagerSISPE.setBeanSession(beanSession, request);
		
		return  toForward( forward, mapping, request);
	}
	
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos
	 * @author walvarez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsMonitorStock(HttpServletRequest request) {
		// Objetos para construir los tabs
		PaginaTab tabsMonitorStock =new PaginaTab("enviarOCPerecibles", "deplegar",	1, 480, request);
		try {
			Tab envioOrdenCompra = new Tab("Consolidar pedido Orden/Compra", "enviarOCPerecibles", "/servicioCliente/monitorStockPerecibles/detalleConsolidadoOrdenCompra.jsp", false);
			Tab tabStockPerecibles = new Tab("Stock perecibles","enviarOCPerecibles","/servicioCliente/monitorStockPerecibles/detalleStockPerecibles.jsp",true);
//			Tab tabAutorizacion = new Tab("Autorizaci\u00F3n cambio fecha","enviarOCPerecibles","/servicioCliente/monitorStockPerecibles/detalleAutorizacionCambioFecha.jsp",false);
			tabsMonitorStock.addTab(envioOrdenCompra);
			tabsMonitorStock.addTab(tabStockPerecibles);
//			tabsMonitorStock.addTab(tabAutorizacion);
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al crea los tabs para el monitor de perecibles",e);
			return null;
		}
		
		request.getSession().setAttribute(PereciblesUtil.MOSTRAR_FILTRO_LOCALES, "ok");
		return tabsMonitorStock;
	}
	
	/**
	 * Este m\u00E9todo selecciona el tab
	 * @author walvarez
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabsMonitorPedidos(BeanSession beanSession, int posicionTab, ListadoPedidosForm formulario, HttpServletRequest request, ActionMessages errors,  ActionMessages info) {
		
		if(posicionTab != -1){
			
			if (posicionTab == 0) {
				try {
					formulario.setFechaInicial(ConverterUtil.parseDateToString(new Date()));
					
					Calendar now = Calendar.getInstance();
					LogSISPE.getLog().info("fecha actual: " + now.getTime());
					
					now.add(Calendar.DAY_OF_MONTH, + 2);
					formulario.setFechaFinal(ConverterUtil.parseDateToString(now.getTime()));
					formulario.setCodigoArticuloTxt("");
					formulario.setCodigoClasificacionTxt("");
					formulario.setDescripcionArticuloTxt("");
					
					formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"));
					
					Collection<Map<String, Object>> colec = PereciblesUtil.getInstancia().consultarConsolidadoPedido(request, formulario, null, null);
					request.getSession().setAttribute(PereciblesUtil.COLECCION_CONSOLIDADO_PEDIDO, colec);
					request.getSession().setAttribute(PereciblesUtil.MOSTRAR_OPCION_ORDEN_COMPRA, "ok");
					
				} catch (Exception e) { LogSISPE.getLog().error("error de aplicaci\u00F3n",e); }
				
			} else {
				request.getSession().removeAttribute(PereciblesUtil.MOSTRAR_OPCION_ORDEN_COMPRA);
			}
			
			ArrayList<?> tabsMonitorPedidos= beanSession.getPaginaTab().getTabs(); 
			for (int i = 0; i < tabsMonitorPedidos.size(); i++) {
				
				if (posicionTab == 1) {
					request.getSession().setAttribute(PereciblesUtil.MOSTRAR_FILTRO_LOCALES, "ok");
					
				} else {
					request.getSession().removeAttribute(PereciblesUtil.MOSTRAR_FILTRO_LOCALES);
				}
				
				if(posicionTab==i){
					beanSession.getPaginaTab().getTab(i).setSeleccionado(true);
				}
				else{
					beanSession.getPaginaTab().getTab(i).setSeleccionado(false);
				}
				formulario.setCodigoArticuloTxt("");
				formulario.setCodigoClasificacionTxt("");
				formulario.setDescripcionArticuloTxt("");
			}
			
		}
	}
	

	/**
	 * Permite redireccionar la accion al forward establecido
	 * 
	 * @param forward  .- forward al cual se direcciona la acci\u00F3n
	 * @param mapping  .- mapping de la accion
	 * @param request  .- request de la llamada de la accion
	 * @return
	 */
	private final ActionForward toForward( String forward, ActionMapping mapping, HttpServletRequest request){
		LogSISPE.getLog().info("Enviar Orden compra, saliendo por : {}",forward);
		return mapping.findForward(forward);
	}
	
	private void copiarFiltrosBusqueda(BeanSession beanSession, ListadoPedidosForm formulario) {
		
		if (formulario.getEstadoPedido() != null) {
			beanSession.setEstadoPedido(formulario.getEstadoPedido());
		} else if (beanSession.getEstadoPedido() != null) {
			formulario.setEstadoPedido(beanSession.getEstadoPedido());
		}	
		
	}
}
