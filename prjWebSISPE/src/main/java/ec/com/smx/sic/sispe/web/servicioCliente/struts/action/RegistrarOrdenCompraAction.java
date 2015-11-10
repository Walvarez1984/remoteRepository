package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;
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
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.RegistrarOrdenCompraForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * 
 * @author mbraganza
 * @author jcalderon
 * @author fmunoz
 */
@SuppressWarnings("unchecked")
public class RegistrarOrdenCompraAction extends BaseAction 
{
	public static final String COL_PAGINA_ARTICULOS = "ec.com.smx.sic.sispe.ordenesCompra.paginaArticulos";
	private static final String RESPALDO_PAGINA_PEDIDO = "ec.com.smx.sic.sispe.ordenesCompra.indicePagPedido";
	private static final String RESPALDO_PAGINA_ARTICULO = "ec.com.smx.sic.sispe.ordenesCompra.indicePagArticulo";
	private static final String CONSULTAR_NUEVAMENTE_ARTICULOS = "ec.com.smx.sic.sispe.ordenesCompra.consultarArticulosNuevamente"; 
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		String salida = "ordenCompra";
		RegistrarOrdenCompraForm formulario = (RegistrarOrdenCompraForm) form;
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		HttpSession session = request.getSession();
		//se obtiene el objeto que contiene los tabs
		PaginaTab paginaTab = (PaginaTab)session.getAttribute(SessionManagerSISPE.PAGINA_TAB);

		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		//se obtiene el valor de la petici\u00F3n
		String peticion = request.getParameter(Globals.AYUDA);

		//buscar los pedidos
		if (request.getParameter("buscar") != null){
			
			LogSISPE.getLog().info("entra a la busqueda");
			int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
			
			//---------- si se escogi\u00F3 el tab de los pedidos sin ordenes de compra ----------------
			if(paginaTab.esTabSeleccionado(0))
			{
				LogSISPE.getLog().info("busqueda de pedidos");
				Collection pedidos = null;
				
				VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);

				//estados del pedido
				consultaPedidoDTO.getId().setCodigoEstado(
						new StringBuilder()
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
						.toString()
				);

				//Buscar por estado actual [SI]
				consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

				//Filtrar los pedidos por el usuario logueado actualmente
				consultaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				//solo si el pedido fue reservado en bodega por el SIC
				consultaPedidoDTO.setReservarBodegaSIC(SessionManagerSISPE.getEstadoActivo(request));
				//Ordenar por codigo de local
				consultaPedidoDTO.setNpCamposOrden(new String[][]{{"codigoAreaTrabajo", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente")}});
				//este campo en estado inactivo indica que solo se van a traer pedidos que no tengan orden de compra alguna ingresada
				consultaPedidoDTO.setNpOrdenCompra(estadoInactivo);

				//metodo que trae el total de registros
				Integer totalPedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerTotalVistaPedido(consultaPedidoDTO);

				consultaPedidoDTO.setNpFirstResult(0);
				consultaPedidoDTO.setNpMaxResult(range);

				//obtiene solamente los registros en el rango establecido
				pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);

				LogSISPE.getLog().info("se actualizan los atributos de paginaci\u00F3n");
				formulario.setStart(String.valueOf(0));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(totalPedidos));

				//se guarda la colecci\u00F3n general
				session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,pedidos);
				session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, totalPedidos);
				//se guarda los parametros de la busqueda para la vistaPedido
				session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
				session.setAttribute(RESPALDO_PAGINA_PEDIDO, "0");
				
				if(pedidos==null || pedidos.isEmpty()){
					infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos"));
				}
			}
			//------------------- si se escogi\u00F3 el tab de los articulos sin ordenes de compra -----------------
			else if (paginaTab.esTabSeleccionado(1)) {
				LogSISPE.getLog().info("tab articulos");
				VistaArticuloDTO vistaArticuloDTO = WebSISPEUtil.construirConsultaVistaArticulos(request, formulario);

				//estados del articulo
				vistaArticuloDTO.getId().setCodigoEstado(
						new StringBuilder()
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
						.append(",")
						.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
						.toString()
				);

				//Filtrar los pedidos por el usuario logueado actualmente
				vistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				vistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloPedido"));
				vistaArticuloDTO.setNpNoObtenerEntregas(estadoActivo);
				vistaArticuloDTO.setNpNumeroOrdenCompra(estadoInactivo);
				
				//se llama al m\u00E9todo que realiza la b\u00FAsqueda
				this.realizarBusquedaArticulos(infos, request, formulario, vistaArticuloDTO);
			}
		}

		//Paginaci\u00F3n
		else if (request.getParameter("start") != null && request.getParameter("range")!=null)
		{
			String start = request.getParameter("start");
			LogSISPE.getLog().info("start: {}", request.getParameter("start"));
			
			if(paginaTab.esTabSeleccionado(0))
			{
				LogSISPE.getLog().info("va a paginar los pedidos");
				//se obtiene el tamano de la coleccion total de articulos
				int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
				LogSISPE.getLog().info("tama\u00F1o: {}" , tamano);
				
				if(tamano > 0){
					//Recupero la vista pedido con los parametros de la busqueda
					VistaPedidoDTO consultaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO);
					//Cambio el inicio de la busqueda
					consultaPedidoDTO.setNpFirstResult(Integer.valueOf(start));
					Collection colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
					LogSISPE.getLog().info("coleccion que devuelve: {}" , colVistaPedidoDTO.size());
					//se asignan las variables de paginaci\u00F3n
					formulario.setStart(start);
					formulario.setRange(request.getParameter("range"));
					formulario.setSize(String.valueOf(tamano));

					//se guarda la colecci\u00F3n general
					session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
					//se guarda en sesi\u00F3n el indice de la paginaci\u00F3n de los pedidos
					session.setAttribute(RESPALDO_PAGINA_PEDIDO, start);
				}
			}else if (paginaTab.esTabSeleccionado(1)) {
				LogSISPE.getLog().info("tab articulos");
				//se obtiene la colecci\u00F3n completa
				List<VistaArticuloDTO> colVistaArticuloDTO = (List<VistaArticuloDTO>) session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
				if (colVistaArticuloDTO != null){
					//se llama al m\u00E9todo que realiza el respaldo de las ordenes de compra y las observaciones ingresadas
					this.respaldarOrdenesCompra(formulario, session);
					//se guarda la nueva p\u00E1gina
					session.setAttribute(COL_PAGINA_ARTICULOS, formulario.paginarDatos(colVistaArticuloDTO, Integer.parseInt(request.getParameter("start")),colVistaArticuloDTO.size(),false));
					//se guarda en sesi\u00F3n el indice de la paginaci\u00F3n de los art\u00EDculos
					session.setAttribute(RESPALDO_PAGINA_ARTICULO, start);
				}
			}
		}

		//Seleccionar un pedido para presentar sus detalles
		else if (request.getParameter("indice") != null){
			try{
				List vistasPedidos = (List) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				LogSISPE.getLog().info("numero de pedidos: {}" , vistasPedidos.size());
				Collection<EstadoDetallePedidoDTO> estadosDetallesPedido = null;
				int nVistaPedido = Integer.parseInt(request.getParameter("indice"));

				estadosDetallesPedido = this.obtenerDetallesPedido(vistasPedidos, nVistaPedido, request);
				if (estadosDetallesPedido != null){
					session.setAttribute("ec.com.smx.sic.sispe.indicePedidoRegistroOrdenCompra", nVistaPedido);
					MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
					salida = "registrarOrdenCompra";
				}else{
					errors.add("registroOrdenCompra", new ActionMessage("errors.ordenCompraPedidoAnulado"));
				}
			}catch(Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}

		//Mostrar la pregunta para regresar al listado de pedidos
		else if(request.getParameter("volverBuscar") != null){
			LogSISPE.getLog().info("regresar a busqueda");
			boolean mostrarPreguntaConfirmacion = false;

			if (formulario.getNumerosAutorizaciones() != null && formulario.getObservacionesNumerosAutorizaciones() != null){
				for (int i = 0; i < formulario.getNumerosAutorizaciones().length; i++){
					if (formulario.getNumerosAutorizaciones()[i].equals("") == false 
							|| formulario.getObservacionesNumerosAutorizaciones()[i].equals("") == false){
						mostrarPreguntaConfirmacion = true;
						break;
					}
				}
			}

			if (mostrarPreguntaConfirmacion){
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.regresarABusqueda", 
						"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
			}else {
				request.setAttribute("ec.com.sxm.sic.sispe.regresarSinConfirmacion", "");
				MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
			}
			salida = "registrarOrdenCompra";
		}

		//Se presiona el bot\u00F3n si para regresar al listado de pedidos
		else if (formulario.getBotonSi() != null && formulario.getBotonSi().equals("siVolverBusqueda")){
			LogSISPE.getLog().info("regresar desde el registro de las ordenes de compra");
			MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);
		}

		//Copiar ordenes de compra y observacion de los pedidos
		else if (request.getParameter("copiarOrdenPedido") != null){
			LogSISPE.getLog().info("entro a copiar orden de compra de pedidos");
			String seleccionados[] = formulario.getCheckSeleccion();
			if(seleccionados != null){
				Collection<EstadoDetallePedidoDTO> detalle= (ArrayList<EstadoDetallePedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido");
				int indice=0;
				int indiceRegistro=0;
				for(EstadoDetallePedidoDTO estadoDetallePedidoDTO:detalle){
					LogSISPE.getLog().info("indice: {}", indice);
					LogSISPE.getLog().info("indiceRegistro: {}" , indiceRegistro);
					if(indice<seleccionados.length && (new Integer(seleccionados[indice])).intValue()==indiceRegistro){
						LogSISPE.getLog().info("va a copiar");
						LogSISPE.getLog().info("num: {}" , formulario.getNumeroOrdenPedido());
						LogSISPE.getLog().info("obs: {}" , formulario.getObservacionPedido());
						estadoDetallePedidoDTO.setNumeroAutorizacionOrdenCompra(formulario.getNumeroOrdenPedido());
						estadoDetallePedidoDTO.setObservacionAutorizacionOrdenCompra(formulario.getObservacionPedido());
						indice++;
					}
					indiceRegistro++;
				}
				formulario.setTodoPedidos(null);
				formulario.setCheckSeleccion(null);
			}else{
				errors.add("seleccionados", new ActionMessage("errors.seleccion.requerido", "Art\u00EDculo"));
			}
			salida = "registrarOrdenCompra";
		}

		//----- Guardar los n\u00FAmeros de \u00F3rdenes de compra para los art\u00EDculos de un pedido ------------
		else if (formulario.getBotonGuardarOrdenesCompra() != null){
			VistaPedidoDTO vistaPedidoActual = null;
			Collection estadosPedido = null;
			List estadosDetallesPedido = null;
			boolean registrarOrdenesCompra = false;
			String userId = SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId();
			
			if(formulario.validarNumOrden(errors,request)==0){
				try{
					LogSISPE.getLog().info("va a guardar");

					if (session.getAttribute("ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra") != null){
						vistaPedidoActual = (VistaPedidoDTO) session.getAttribute("ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra");

						if (formulario.getNumerosAutorizaciones() != null && formulario.getObservacionesNumerosAutorizaciones() != null){
							for (int i = 0; i < formulario.getNumerosAutorizaciones().length; i++){
								if (!formulario.getNumerosAutorizaciones()[i].equals("") || !formulario.getObservacionesNumerosAutorizaciones()[i].equals("")){
									registrarOrdenesCompra = true;
									break;
								}
							}
						}

						if(registrarOrdenesCompra){
							if (session.getAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido") != null){
								estadosDetallesPedido = (List)session.getAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido");
								for (int i = 0; i < formulario.getNumerosAutorizaciones().length; i++){
									if (!formulario.getNumerosAutorizaciones()[i].equals("")){
										EstadoDetallePedidoDTO estadoDetallePedidoActual = (EstadoDetallePedidoDTO) estadosDetallesPedido.get(i);
										estadoDetallePedidoActual.setNumeroAutorizacionOrdenCompra(formulario.getNumerosAutorizaciones()[i]);
										estadoDetallePedidoActual.setObservacionAutorizacionOrdenCompra(formulario.getObservacionesNumerosAutorizaciones()[i]);
									}
								}
							}
							vistaPedidoActual.setUserId(userId);
							SessionManagerSISPE.getServicioClienteServicio().transRegistrarOrdenesCompraPorPedido(vistaPedidoActual, estadosDetallesPedido);
							messages.add("ordenCompra", new ActionMessage("message.exito.registrarOrdenCompra", vistaPedidoActual.getId().getCodigoPedido(), vistaPedidoActual.getLlaveContratoPOS()));
							session.setAttribute(CONSULTAR_NUEVAMENTE_ARTICULOS, "ok");
						}else{
							warnings.add("ordenCompra", new ActionMessage("warning.ordenesCompraNoIngresadas", vistaPedidoActual.getId().getCodigoPedido(), vistaPedidoActual.getLlaveContratoPOS()));
						}
					}
					MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"), session);

				}catch(Exception e){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
					errors.add("ordenCompra", new ActionMessage("errors.registroNumeroAutorizacionOrdenCompra", e.getMessage()));
					salida = "registrarOrdenCompra";
				}
			}else{
				LogSISPE.getLog().info("errores: {}" , errors.size());
				salida = "registrarOrdenCompra";
			}
		}

		//------------------------ Control de Tabs -------------------------------
		else if (paginaTab!= null && paginaTab.comprobarSeleccionTab(request)){
			String size = "0";
			String start = "0";
			//---------- si se escogi\u00F3 el tab de los pedidos -------------
			if(paginaTab.esTabSeleccionado(0))
			{
				//se obtiene la cantidad total de pedidos
				if(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)!=null){
					size = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).toString();
				}
				//se obtiene el indice de inicio de paginaci\u00F3n
				if(session.getAttribute(RESPALDO_PAGINA_PEDIDO) != null){
					start = (String)session.getAttribute(RESPALDO_PAGINA_PEDIDO);
				}
				LogSISPE.getLog().info("tab pedidos");
			}
			//------------------- si se escogi\u00F3 el tab de los articulos -----------------
			else if (paginaTab.esTabSeleccionado(1)) {
				LogSISPE.getLog().info("tab articulos");
				
				//verifica si se debe realizar una nueva consulta de art\u00EDculos porque ya se actualiz\u00F3 uno o m\u00E1s pedidos
				if(session.getAttribute(CONSULTAR_NUEVAMENTE_ARTICULOS)!=null){
					//se llama al m\u00E9todo que realiza la b\u00FAsqueda
					Collection<VistaArticuloDTO> articulos = this.realizarBusquedaArticulos(infos, request, formulario, (VistaArticuloDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAARTICULO));
					if(articulos != null){
						size = String.valueOf(articulos.size());
					}
					session.removeAttribute(CONSULTAR_NUEVAMENTE_ARTICULOS);
				}else{
					Collection colVistaArticuloDTO = (Collection)session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
					if(colVistaArticuloDTO!=null){
						size = String.valueOf(colVistaArticuloDTO.size());
					}
					if(session.getAttribute(RESPALDO_PAGINA_ARTICULO) != null){
						start = (String)session.getAttribute(RESPALDO_PAGINA_ARTICULO);
					}
				}
			}
			
			//se actualizan los datos de la paginaci\u00F3n
			formulario.setStart(start);
			formulario.setSize(size);
		}
		//-------------- copia las ordenes ingresadas a los art\u00EDculos seleccionados -------------
		else if(request.getParameter("copiarOrdenArticulos") != null){
			//se verifica si se seleccionaron art\u00EDculos
			String [] articulosSeleccionados = formulario.getCheckSeleccion();
			if(articulosSeleccionados != null){
				List<VistaArticuloDTO> listVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(COL_PAGINA_ARTICULOS);
				for(int i=0;i<articulosSeleccionados.length;i++){
					int indice = Integer.parseInt(articulosSeleccionados[i]);
					VistaArticuloDTO vistaArticuloDTO = listVistaArticuloDTO.get(indice);
					vistaArticuloDTO.setNpNumeroOrdenCompra(formulario.getNumeroOrdenArticulo());
					vistaArticuloDTO.setNpObservacionOrdenCompra(formulario.getObservacionOrdenArticulo());
				}
				formulario.setCheckSeleccion(null);
			}else{
				errors.add("seleccionados", new ActionMessage("errors.seleccion.requerido", "Art\u00EDculo"));
			}
		}
		//----------- se guardan las ordenes de compra por art\u00EDculo ----------------
		else if(request.getParameter("guardarOrdenCompraArt") != null){
			try{
				//se llama al m\u00E9todo que realiza el respaldo de las ordenes de compra y las observaciones ingresadas
				this.respaldarOrdenesCompra(formulario, session);
				
				//se obtiene la colecci\u00F3n de art\u00EDculos
				Collection<VistaArticuloDTO> colVistaArticuloDTO = (Collection<VistaArticuloDTO>)session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
				//se llama al m\u00E9todo que registra las ordenes de compra
				SessionManagerSISPE.getServicioClienteServicio().transRegistrarOrdenesCompraPorArticulo(colVistaArticuloDTO, SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				
				//se realiza nuevamente la consulta para actualizar la colecci\u00F3n en memoria
				this.realizarBusquedaArticulos(infos, request, formulario, (VistaArticuloDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAARTICULO));
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				
			}
		}
		//cuando se accede al detalle de un pedido desde el tab de art\u00EDculos
		else if(peticion!=null && peticion.startsWith("IPVA")){
			//se llama al m\u00E9todo que realiza el respaldo de las ordenes de compra y las observaciones ingresadas
			this.respaldarOrdenesCompra(formulario, session);
			session.setAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO, peticion);
			salida = "detallePedido";
		}
		//primer ingreso a la pagina
		else{
			SessionManagerSISPE.removeVarSession(request);
			session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Ordenes de compra");
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarOrdenCompra"));

			//Verificar si el usuario logeado no es de un local para cargar los locales
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable()
					.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			}

			//Datos del formulario
			session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");

			//Iniciar Tabs
			PaginaTab nuevaPaginaTab = new PaginaTab("registrarOrdenCompra", "ordenCompra", 1, 460, request);
			Tab tabPedidos = new Tab("Pedidos", "registrarOrdenCompra", "/servicioCliente/registrarOrdenCompra/listaPedidosOrdenCompra.jsp", false);
			Tab tabArticulos = new Tab("Art\u00EDculos", "registrarOrdenCompra", "/servicioCliente/registrarOrdenCompra/listaArticulosOrdenCompra.jsp", true);
			nuevaPaginaTab.addTab(tabPedidos);
			nuevaPaginaTab.addTab(tabArticulos);

			//se guarda el objeto que contiene los tabs
			session.setAttribute(SessionManagerSISPE.PAGINA_TAB, nuevaPaginaTab);
		}

		LogSISPE.getLog().info("salida registroordencompra: {}" , salida);

		saveErrors(request,errors);
		saveInfos(request, infos);
		saveMessages(request, messages);
		saveWarnings(request, warnings);

		return mapping.findForward(salida);
	}

	/**
	 * 
	 * @param vistasPedidos
	 * @param nVistaPedido
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Collection<EstadoDetallePedidoDTO> obtenerDetallesPedido(List<VistaPedidoDTO> vistasPedidos, int nVistaPedido, HttpServletRequest request) throws Exception{

		VistaPedidoDTO vistaPedidoActual = null;
		VistaPedidoDTO vistaPedidoBusqueda = null;
		EstadoDetallePedidoDTO estadoDetallePedidoBusqueda = null;
		Collection vistasPedidoConsulta = null;
		Collection<EstadoDetallePedidoDTO> estadosDetallesPedidos = null;
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		if (nVistaPedido > -1 && nVistaPedido < vistasPedidos.size()){
			LogSISPE.getLog().info("indicePedidoOrdenCompra: {}" , nVistaPedido);
			vistaPedidoActual = vistasPedidos.get(nVistaPedido);
			//Consultar nuevamente la vistaPedidoActual
			vistaPedidoBusqueda = new VistaPedidoDTO();
			vistaPedidoBusqueda.getId().setCodigoCompania(vistaPedidoActual.getId().getCodigoCompania());
			vistaPedidoBusqueda.getId().setCodigoAreaTrabajo(vistaPedidoActual.getId().getCodigoAreaTrabajo());
			vistaPedidoBusqueda.getId().setCodigoPedido(vistaPedidoActual.getId().getCodigoPedido());
			vistaPedidoBusqueda.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			vistasPedidoConsulta = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoBusqueda);
			if (vistasPedidoConsulta.isEmpty() == false){
				vistaPedidoActual = (VistaPedidoDTO) vistasPedidoConsulta.iterator().next();
				if (vistaPedidoActual.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"))){
					return null;
				}
				estadoDetallePedidoBusqueda = new EstadoDetallePedidoDTO();
				estadoDetallePedidoBusqueda.getId().setCodigoCompania(vistaPedidoActual.getId().getCodigoCompania());
				estadoDetallePedidoBusqueda.getId().setCodigoAreaTrabajo(vistaPedidoActual.getId().getCodigoAreaTrabajo());
				estadoDetallePedidoBusqueda.getId().setCodigoPedido(vistaPedidoActual.getId().getCodigoPedido());
				estadoDetallePedidoBusqueda.getId().setCodigoEstado(vistaPedidoActual.getId().getCodigoEstado());
				estadoDetallePedidoBusqueda.getId().setSecuencialEstadoPedido(vistaPedidoActual.getId().getSecuencialEstadoPedido());
				estadoDetallePedidoBusqueda.setDetallePedidoDTO(new DetallePedidoDTO());
				estadoDetallePedidoBusqueda.getDetallePedidoDTO().setArticuloDTO(new ArticuloDTO());
				estadoDetallePedidoBusqueda.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				estadoDetallePedidoBusqueda.setReservarBodegaSIC(estadoActivo);
				//estadoDetallePedidoBusqueda.setEspecialReservado(estadoActivo);

				estadosDetallesPedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoDetallePedido(estadoDetallePedidoBusqueda);
				for (EstadoDetallePedidoDTO estadoActual : estadosDetallesPedidos) {
					estadoActual.setNpNumeroAutorizacionOrdenCompraInCorrecto(Boolean.FALSE);
				}
				LogSISPE.getLog().info("estadosDetallePedido: {}" , estadosDetallesPedidos.size());
				ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoActual);
				request.getSession().setAttribute("ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra", vistaPedidoActual);
				request.getSession().setAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido", estadosDetallesPedidos);
			}
		}

		return estadosDetallesPedidos;
	}
	
	/**
	 * 
	 * @param listVistaArticuloDTO
	 * @param formulario
	 */
	private void respaldarOrdenesCompra(RegistrarOrdenCompraForm formulario, HttpSession session)
	{
		//se obtiene las numeros y observaciones
		String [] numerosOrden = formulario.getNumerosOrdenesArticulo();
		String [] observacionesOrden = formulario.getObservacionOrdenesArticulo();
		List<VistaArticuloDTO> listVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(COL_PAGINA_ARTICULOS);
		
		if(numerosOrden!=null && observacionesOrden != null){
			for(int i=0;i<numerosOrden.length;i++){
				VistaArticuloDTO vistaArticuloDTO = listVistaArticuloDTO.get(i);
				vistaArticuloDTO.setNpNumeroOrdenCompra(numerosOrden[i]);
				vistaArticuloDTO.setNpObservacionOrdenCompra(observacionesOrden[i]);
			}
		}
	}
	
	/**
	 * 
	 * @param infos
	 * @param request
	 * @param formulario
	 * @param vistaArticuloDTO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Collection<VistaArticuloDTO> realizarBusquedaArticulos(ActionMessages infos, HttpServletRequest request, RegistrarOrdenCompraForm formulario,
			VistaArticuloDTO vistaArticuloDTO) throws Exception{

		if(vistaArticuloDTO != null){
			HttpSession session = request.getSession();
			//obtiene solamente los registros en el rango establecido
			Collection<VistaArticuloDTO> articulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(vistaArticuloDTO);

			if(articulos==null || articulos.isEmpty()){
				infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos"));
				session.removeAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
				session.removeAttribute(COL_PAGINA_ARTICULOS);
			}else{
				LogSISPE.getLog().info("ENTRO A LA PAGINACION");
				Collection datosSub = formulario.paginarDatos(articulos, 0, articulos.size(),false);
				//Coleccion total de articulos
				session.setAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO, articulos);
				//Coleccion de articulos paginada
				session.setAttribute(COL_PAGINA_ARTICULOS, datosSub);
			}
			//se guarda los parametros de la busqueda para la vistaPedido
			session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAARTICULO, vistaArticuloDTO);
			//se respalda el indice de pagianci\u00F3n
			session.setAttribute(RESPALDO_PAGINA_ARTICULO, "0");
			return articulos;
		}
		return null;
	}
}
