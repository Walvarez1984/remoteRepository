package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PENDIENTE;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jdom.Document;

import ec.com.kruger.utilitario.dao.commons.dto.ListSet;
import ec.com.kruger.utilitario.dao.commons.dto.SearchResultDTO;
import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.smx.corpv2.dto.AreaTrabajoDTO;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.TransformerUtil;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloProveedorDTO;
import ec.com.smx.sic.cliente.mdl.dto.VistaProveedorDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloID;
import ec.com.smx.sic.cliente.mdl.dto.id.ProveedorID;
import ec.com.smx.sic.cliente.mdl.dto.sispe.ArticuloTemporadaDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.ArticuloTemporadaDetalleDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TemporadaDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.ArticuloTemporadaKardexDTO;
import ec.com.smx.sic.sispe.dto.CalendarioMotivoMovimientoDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * @author osaransig
 *
 */
public final class PereciblesUtil {
	
	//variables de sesion
	
	/**
	 * Almacena la coleccion de articulos perecibles por proveedor 
	 */
	public static final String COLECCION_PERECIBLES = "ec.com.smx.sic.sispe.coleccion.perecibles";
	public static final String COLECCION_CONSOLIDADO_PEDIDO = "ec.com.smx.sic.sispe.coleccion.consolidado.pedido";
	public static final String ARTICULO_PERECIBLE_SELECCIONADO = "ec.com.smx.sic.sispe.articulo.perecible.seleccionado";
	public static final String DETALLES_MOVIMIENTOS = "ec.com.smx.sic.sispe.detalle.movimientos";
	public static final String LISTA_MOTIVOS_MOVIMIENTOS = "ec.com.smx.sic.sispe.lista.motivos.movimientos";
	public static final String LISTA_COMPLETA_MOTIVOS_MOVIMIENTOS = "ec.com.smx.sic.sispe.lista.completa.motivos.movimientos";
	public static final String MOSTRAR_FILTRO_LOCALES = "ec.com.smx.sic.sispe.mostrar.filtros.locales";
	public static final String MOSTRAR_OPCION_ORDEN_COMPRA = "ec.com.smx.sic.sispe.mostrar.orden.compra";
	
	public static final String COL_STOCK_PERECIBLES = "ec.com.smx.sic.sispe.stock.perecibles.col";
	
	public static final String COL_ART_TEM_BUSQUEDA = "ec.com.smx.sic.sispe.stock.perecibles.col";
	
	public static final String MONITOR_PERECIBLES_ATRAS = "ec.com.smx.sic.sispe.monitor.perecibles.atras";
	
	//filtros en session
	public static final String FILTROS_STOCK_PERECIBLES = "ec.com.smx.sic.sispe.filtros.stock.perecibles";
	public static final String FILTROS_CONSOLIDADO_PEDIDO = "ec.com.smx.sic.sispe.filtros.stock.perecibles";
	
	public static PereciblesUtil instancia = new PereciblesUtil();
	
	//Constantes tipo stock
	public final static String STOCK_NEGOCIADO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.negociado");
	public final static String STOCK_DISPONIBLE_RESERVAS = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.disponible.reservas");
	public final static String STOCK_DISPONIBLE_LOCALES = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.disponible.locales");
	public final static String STOCK_RESERVADO_RESERVAS = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.reservado.reservas");
	public final static String STOCK_RESERVADO_LOCALES = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.reservado.locales");
	public final static String STOCK_DESPACHADO_RESERVAS = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.despachado.reservas");
	public final static String STOCK_DESPACHADO_LOCALES = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.stock.despachado.locales");
	
	//Constantes codigo motivo movimiento
	public final static String CANTIDAD_NEGOCIADA_INICIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.mot.mov.cantidad.negociada.inicial");
	public final static String CANTIDAD_RESERVA_INICIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.mot.mov.cantidad.reserva.inicial");
	public final static String AJUSTE_CANTIDAD_RES_ING = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.mot.mov.ajuste.cantidad.reservada.ing");
	public final static String AJUSTE_CANTIDAD_RES_EGR = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.mot.mov.ajuste.cantidad.reservada.egr");
	
	
	private static final String PEDIDOS_COL_KEY= "pedidoLocalesCol";
	private static final String CANTIDAD_CONSOLIDADO_KEY = "cantidadConsolidado";
	private static final String ARTICULO_KEY = "articuloDTO";
	private static final String FECHA_DESPACHO_KEY = "fechaDespachoBodega";
	private static final String PEDIDO_KEY = "pedidoDTO";
	private static final String CANTIDAD_KEY = "cantidad";
	private static final String NUM_RESERVA_KEY = "numeroReserva";
	
	private static final String PROVEEDOR_KEY = "proveedor";
	private static final String ID_PROVEEDOR_KEY = "idProveedor";
	private static final String ART_PERECIBLES_KEY = "articulosPereciblesCol";
	private static final String NOMBRE_PROVEEDOR_KEY = "nombreProveedor";
	private static final String CODIGO_PROVEEDOR_KEY = "codigoProveedor";
	private static final String STOCK_REAL_BODEGA = "stockRealBodega";
	
	public static final Integer NUMERO_FILAS = Integer.valueOf(MessagesWebSISPE.getString("parametro.paginacion.rango"));
	
	/**
	 * Constructor por default
	 */
	private PereciblesUtil () {
		
	}
	
	public static PereciblesUtil getInstancia () {
		return instancia;
	}
	
	public  Collection<Map<String, Object>> consultaStockPerecibles (HttpServletRequest request, ListadoPedidosForm form, ActionMessages errors, ActionMessages info) {
		
		Collection<Map<String,Object>> colStockPredecibles = new ArrayList<Map<String, Object>>();
		
		try {
			ArticuloTemporadaDTO articuloTemporadaDTO = generarObjetoBusqueda(form);
			articuloTemporadaDTO.getId().setCodigoTemporada(SessionManagerSISPE.getCurrentTemporadaActiva().getId().getCodigoTemporada());
			articuloTemporadaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			articuloTemporadaDTO.setNpCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));
			articuloTemporadaDTO.setNpConsultarStockReal(Boolean.TRUE);
			articuloTemporadaDTO.setNpUserLogged(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			Collection<ArticuloTemporadaDTO> articuloTemporadaDTOs = new ArrayList<ArticuloTemporadaDTO>();
			
			articuloTemporadaDTOs = SISPEFactory.getServicioMonitoreoPerecibles().obtenerStockPerecibles(articuloTemporadaDTO);
			colStockPredecibles = construirColeccionPerecibles(articuloTemporadaDTOs);
			
			request.getSession().setAttribute(COL_STOCK_PERECIBLES, colStockPredecibles);
			//se guarda en session la consulta
			request.getSession().setAttribute(COL_ART_TEM_BUSQUEDA, articuloTemporadaDTOs);
			
		} catch (SISPEException e) {
			errors.add("", new ActionMessage("errors.busqueda.stock.perecibles",e.getMessage()));
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);

		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		
		
		return colStockPredecibles;
	}
	
	private ArticuloTemporadaDTO generarObjetoBusqueda (ListadoPedidosForm formulario) {
		ArticuloDTO articuloDTO = new ArticuloDTO();
		articuloDTO.setId(null);
		articuloDTO.setNpCodigoBarras(formulario.getCodigoArticuloTxt().equals("") ? null: formulario.getCodigoArticuloTxt());
		articuloDTO.setCodigoClasificacion(formulario.getCodigoClasificacionTxt().equals("") ? null: formulario.getCodigoClasificacionTxt());
		articuloDTO.setDescripcionArticulo(formulario.getDescripcionArticuloTxt().equals("") ? null: formulario.getDescripcionArticuloTxt());
//		formulario.getCodigoLocal();
		
		ArticuloProveedorDTO articuloProveedorDTO = new ArticuloProveedorDTO();
		articuloProveedorDTO.setArticulo(articuloDTO);
		
		ArticuloTemporadaDTO articuloTemporadaDTO = new ArticuloTemporadaDTO();
		articuloTemporadaDTO.setArticuloProveedorDTO(articuloProveedorDTO);
		return articuloTemporadaDTO;
	}
	
	/**
	 * Armar objeto para presentarse en pantalla, con los siguientes atributos: 
	 * proveedor, idProveedor, articuloPerecibleCol
	 * @param articuloTemporadaDTOs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  Collection<Map<String, Object>> construirColeccionPerecibles (Collection<ArticuloTemporadaDTO> articuloTemporadaDTOs) {
		
		Collection<Map<String, Object>> colPerecibles = new ArrayList<Map<String, Object>>();
		
		for (ArticuloTemporadaDTO articuloTemporadaDTO2 : articuloTemporadaDTOs) {
			
			Map<String, Object> proveedorEncontrado = buscarProveedor(articuloTemporadaDTO2, colPerecibles);
			
			if (null == proveedorEncontrado) {
				
				Map<String, Object> proveedorArticuloMap = new HashMap<String, Object>();
				
				//agregar nuevo proveedor
				colPerecibles.add(construirProveedor(articuloTemporadaDTO2, proveedorArticuloMap));
				Collection<Map<String, Object>> colArticulosStockPerecibles = new ArrayList<Map<String, Object>>();
				
				//agregar primer articulo
				colArticulosStockPerecibles.add(construirArticuloStockPerecible(articuloTemporadaDTO2));
				proveedorArticuloMap.put(ART_PERECIBLES_KEY, colArticulosStockPerecibles);
				
			} else {
				//agregar articulo al proveedor encontrado
				Collection<Map<String, Object>> coleccionArticulos = (Collection<Map<String, Object>>) proveedorEncontrado.get(ART_PERECIBLES_KEY);
				coleccionArticulos.add(construirArticuloStockPerecible(articuloTemporadaDTO2));
			}
			
		}
		calcularNumeroArticulos(colPerecibles);
		return colPerecibles;
	}
	
	
	@SuppressWarnings("rawtypes")
	private void calcularNumeroArticulos(Collection<Map<String, Object>> colPerecibles) {
		for (Map<String, Object> map : colPerecibles) {
			map.put("size", ((ArrayList)map.get(ART_PERECIBLES_KEY)).size());
		}
	}
	
	private  Map<String, Object> construirProveedor (ArticuloTemporadaDTO articuloTemporadaDTO2, Map<String, Object> proveedorArticuloMap ) {
		
		proveedorArticuloMap.put(PROVEEDOR_KEY, articuloTemporadaDTO2.getArticuloProveedorDTO().getVistaProveedor());
		proveedorArticuloMap.put(ID_PROVEEDOR_KEY, articuloTemporadaDTO2.getArticuloProveedorDTO().getVistaProveedor().getId());
		
		return proveedorArticuloMap;
	}
	
	/**
	 * Armar objeto articulo a mostrarse por  proveedor con los siguientes atributos:
	 * articulo, stockNegociado, stockDisponibleReservas, stockDisponibleLocales, stockReservadoReservas, stockReservadoLocales
	 * @param articuloTemporadaDTO2
	 * @return
	 */
	private  Map<String, Object> construirArticuloStockPerecible (ArticuloTemporadaDTO articuloTemporadaDTO2) {
		
		Map<String, Object> articulosStockPerecibleMap = new HashMap<String, Object>();
		
		articulosStockPerecibleMap.put("articulo", articuloTemporadaDTO2.getArticuloProveedorDTO().getArticulo());
		
		for (ArticuloTemporadaDetalleDTO articuloTemporadaDetalleDTO2 : articuloTemporadaDTO2.getArticuloTemporadaDetalleCol()) {
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_NEGOCIADO)) {
				articulosStockPerecibleMap.put("stockNegociado", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_DISPONIBLE_RESERVAS)) {
				articulosStockPerecibleMap.put("stockDisponibleReservas", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_DISPONIBLE_LOCALES)) {
				articulosStockPerecibleMap.put("stockDisponibleLocales", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_RESERVADO_RESERVAS)) {
				articulosStockPerecibleMap.put("stockReservadoReservas", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_RESERVADO_LOCALES)) {
				articulosStockPerecibleMap.put("stockReservadoLocales", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_DESPACHADO_RESERVAS)) {
				articulosStockPerecibleMap.put("stockDespachadoReservas", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(STOCK_DESPACHADO_LOCALES)) {
				articulosStockPerecibleMap.put("stockDespachadoLocales", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
			
			if (articuloTemporadaDetalleDTO2.getId().getCodigoTipoStock().toString().equals(ConstantesGenerales.PEDIDO_ASISTIDO)) {
				articulosStockPerecibleMap.put("desPesAsi", articuloTemporadaDetalleDTO2.getCantidadTotal());
			}
		}
		
		//para mostrar en los detalles
		articulosStockPerecibleMap.put(NOMBRE_PROVEEDOR_KEY, articuloTemporadaDTO2.getArticuloProveedorDTO().getVistaProveedor().getNombreProveedor());
		articulosStockPerecibleMap.put(CODIGO_PROVEEDOR_KEY, articuloTemporadaDTO2.getArticuloProveedorDTO().getVistaProveedor().getId().getCodigoProveedor());
		//agregar el stock de la bodega real
		articulosStockPerecibleMap.put(STOCK_REAL_BODEGA,new Double(articuloTemporadaDTO2.getArticuloProveedorDTO().getArticulo().getNpStockArticulo()==null?0:articuloTemporadaDTO2.getArticuloProveedorDTO().getArticulo().getNpStockArticulo()));
		
		return articulosStockPerecibleMap;
	}
	
	@SuppressWarnings("unchecked")
	private  Map<String, Object> buscarProveedor (ArticuloTemporadaDTO articuloTemporadaDTO2, Collection<Map<String, Object>> colPerecibles ) {
		
		final ProveedorID proveedorID = articuloTemporadaDTO2.getArticuloProveedorDTO().getVistaProveedor().getId();
		Collection<Map<String, Object>> proveedorArticuloEncontrado = null;
		
		proveedorArticuloEncontrado = CollectionUtils.select(colPerecibles, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Map<String, Object> proArt = (Map<String, Object>) arg0;
				ProveedorID proveedorIDLocal = (ProveedorID) proArt.get(ID_PROVEEDOR_KEY);
				
				return proveedorIDLocal.getCodigoCompania().equals(proveedorID.getCodigoCompania()) && 
						proveedorIDLocal.getCodigoProveedor().equals(proveedorID.getCodigoProveedor()) ;
			}
		});
		
		if (proveedorArticuloEncontrado.isEmpty()) {
			return null;
		} else {
			return (Map<String, Object>) proveedorArticuloEncontrado.toArray()[0];
		}
		
		
	}
	
	/**
	 * Metodo par obtener articulo seleccionado de la coleccion de articulos almacenados en session
	 * @param request
	 * @param form
	 * @param errors
	 * @param info
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> obtenerArticuloSeleccionado (HttpServletRequest request, ListadoPedidosForm formulario, ActionMessages errors, ActionMessages info) {
		
		Integer indiceProveedor = Integer.parseInt(request.getParameter("indiceProveedor").toString());
		Integer indiceArticulo = Integer.parseInt(request.getParameter("indiceArticulo").toString());
		Collection<Map<String, Object>> colEnSesion = (Collection<Map<String, Object>>) request.getSession().getAttribute(COLECCION_PERECIBLES);
		Map<String, Object> proveedorSeleccionado = (Map<String, Object>) colEnSesion.toArray()[indiceProveedor];
		Collection<Map<String, Object>> articulosProveedor = (Collection<Map<String, Object>>) proveedorSeleccionado.get(ART_PERECIBLES_KEY);
		Map<String, Object> articuloSeleccionado = (Map<String, Object>) articulosProveedor.toArray()[indiceArticulo];
		
		LogSISPE.getLog().info("Articulo seleccionado: {}, {}", ((ArticuloDTO)articuloSeleccionado.get("articulo")).getDescripcionArticulo(), 
				((ArticuloDTO)articuloSeleccionado.get("articulo")).getNpCodigoBarras());
		
		cargarDetallesMovimientos(articuloSeleccionado, request, 0, formulario);
		
		return articuloSeleccionado;
		
	}
	
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public void cargarDetallesMovimientos (Map<String, Object>  articuloSeleccionado, HttpServletRequest request, Integer start, ListadoPedidosForm formulario) {
		ArticuloTemporadaDTO articuloTemporadaDTO = new ArticuloTemporadaDTO();
		
		articuloTemporadaDTO.getId().setCodigoArticulo( ((ArticuloDTO)articuloSeleccionado.get("articulo")).getId().getCodigoArticulo());
		articuloTemporadaDTO.getId().setCodigoCompania(((ArticuloDTO)articuloSeleccionado.get("articulo")).getId().getCodigoCompania());
		articuloTemporadaDTO.getId().setCodigoProveedor(articuloSeleccionado.get(CODIGO_PROVEEDOR_KEY).toString());
		articuloTemporadaDTO.getId().setCodigoTemporada(SessionManagerSISPE.getDefault().getCurrentTemporadaActiva().getId().getCodigoTemporada());
		
		//paginacion
		articuloTemporadaDTO.setFirstResult(start);
		articuloTemporadaDTO.setMaxResults(NUMERO_FILAS);
		Collection<ArticuloTemporadaKardexDTO> colArticuloTemporadaKardexDTOs = null;
		SearchResultDTO searchResult = null;
		try {
			searchResult = SISPEFactory.getServicioMonitoreoPerecibles().obtenerDetalleMovimientoKardex(articuloTemporadaDTO);
			colArticuloTemporadaKardexDTOs =  searchResult.getResults();
		} catch (SISPEException e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		
		request.getSession().setAttribute(DETALLES_MOVIMIENTOS, colArticuloTemporadaKardexDTOs);
		
		//se asignan las variables de paginaci\u00F3n
		formulario.setStart(String.valueOf(start));
		formulario.setRange(String.valueOf(NUMERO_FILAS));
		formulario.setSize(String.valueOf(searchResult.getCountResults()));
		
	}
	
	public void crearPopUpNuevoMovimiento(ListadoPedidosForm form, HttpServletRequest request){
		
		//inicializar variable a ser ingresadas
		form.setComboMotivoMovimiento("");
		form.setCantidadMotivoMovimiento("");
		
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("NuevoMovimiento");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setContenidoVentana("servicioCliente/monitorStockPerecibles/nuevoMovimiento.jsp");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setTope(0d);
		popUp.setAncho(65d);
		popUp.setValorOK("requestAjax('enviarOCPerecibles.do',['mensajes','pregunta','div_pagina'],{parameters: 'nuevoMovimiento=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('enviarOCPerecibles.do', ['pregunta'], {parameters: 'cancelarNuevoMovimiento=ok', popWait:false, evalScripts:true});ocultarModal();");
		request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
		cargarMotivoMovimientos(request);

	}
	
	
	public void consultarMotivosMovimientos (HttpServletRequest request) {
		Collection<CalendarioMotivoMovimientoDTO> listMotMov = null;
		try {
			listMotMov = SISPEFactory.getServicioMonitoreoPerecibles().obtenerMotivosMovimientos();
			LogSISPE.getLog().info("Size lista Mot mov: {}", listMotMov.size());
			request.getSession().setAttribute(LISTA_COMPLETA_MOTIVOS_MOVIMIENTOS, listMotMov);
		} catch (SISPEException e) {
			LogSISPE.getLog().error("Error al obtener motivos movimientos");
			LogSISPE.getLog().error("error de aplicaci\u00F3n", e);
		}
		
	}
	
	/**
	 * Obtener motivos movimientos dependiendo del contexto
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void cargarMotivoMovimientos (HttpServletRequest request) {
		
		Collection<Map<String, Object>> colectionMotiMov = new ArrayList<Map<String, Object>>();
		
		request.getSession().removeAttribute(LISTA_MOTIVOS_MOVIMIENTOS);
		
		Collection<CalendarioMotivoMovimientoDTO> listMotMov = null;
		listMotMov = (Collection<CalendarioMotivoMovimientoDTO>) request.getSession().getAttribute(LISTA_COMPLETA_MOTIVOS_MOVIMIENTOS);
		LogSISPE.getLog().info("Size lista Mot mov: {}", listMotMov.size());
		
		Collection<ArticuloTemporadaKardexDTO> colArticuloTemporadaKardexDTOs = (Collection<ArticuloTemporadaKardexDTO>) request.getSession().getAttribute(DETALLES_MOVIMIENTOS);
		
		ArticuloTemporadaKardexDTO artTemKey = new ArticuloTemporadaKardexDTO();
		artTemKey.setCodigoMotivoMovimiento(CANTIDAD_NEGOCIADA_INICIAL);
		
		List<ArticuloTemporadaKardexDTO> listArtemKar = new ArrayList<ArticuloTemporadaKardexDTO>(colArticuloTemporadaKardexDTOs);
		//buscando cantidad negociada inicial
		Collections.sort(listArtemKar, new Comparator<ArticuloTemporadaKardexDTO>() {

			public int compare(ArticuloTemporadaKardexDTO o1, ArticuloTemporadaKardexDTO o2) {
				return o1.getCodigoMotivoMovimiento().compareTo(o2.getCodigoMotivoMovimiento());
			}
			
		});
		
//		int i = Collections.binarySearch(listArtemKar, artTemKey, new Comparator<ArticuloTemporadaKardexDTO>() {
//
//			public int compare(ArticuloTemporadaKardexDTO o1, ArticuloTemporadaKardexDTO o2) {
//				return o1.getCodigoMotivoMovimiento().compareTo(o2.getCodigoMotivoMovimiento());
//			}
//			
//		});
		
		Map<String, Object> articuloSeleccionado =(Map<String, Object>)request.getSession().getAttribute(PereciblesUtil.ARTICULO_PERECIBLE_SELECCIONADO);
		
		Double valorNegocido=Double.valueOf(articuloSeleccionado.get("stockNegociado")!=null?articuloSeleccionado.get("stockNegociado").toString():"0");
		//cargar solo motivo movimiento:  CANTIDAD NEGOCIADA INICIAL
		if (colArticuloTemporadaKardexDTOs.size() == 0 || valorNegocido == 0D) {
			
			Map<String, Object> objIngresoCmm = new HashMap<String, Object>();
			objIngresoCmm.put("tipoMovimiento","INGRESO");
			objIngresoCmm.put("listMotMov", obtenerMotivoMovimientoPorCodigoMotMov(listMotMov, CANTIDAD_NEGOCIADA_INICIAL));
			colectionMotiMov.add(objIngresoCmm);
			
		} else {
			
			if (mostrarCantidadReservaInicial(colArticuloTemporadaKardexDTOs, CANTIDAD_RESERVA_INICIAL)) {
				
				listMotMov = removerMotivosMovimientoPorCodigoMotMov(listMotMov, CANTIDAD_NEGOCIADA_INICIAL, AJUSTE_CANTIDAD_RES_EGR, AJUSTE_CANTIDAD_RES_ING);
			} else {
				listMotMov = removerMotivosMovimientoPorCodigoMotMov(listMotMov, CANTIDAD_NEGOCIADA_INICIAL, CANTIDAD_RESERVA_INICIAL);
			}
			
			Map<String, Object> objIngresoCmm = new HashMap<String, Object>();
			objIngresoCmm.put("tipoMovimiento","INGRESO");
			objIngresoCmm.put("listMotMov",obtenerMotivoMovimientoPorCodigoTipMov(listMotMov, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigo.tipo.movimiento.ingreso")));
			
			Map<String, Object> objEgresoCmm = new HashMap<String, Object>();
			objEgresoCmm.put("tipoMovimiento","EGRESO");
			objEgresoCmm.put("listMotMov",obtenerMotivoMovimientoPorCodigoTipMov(listMotMov, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigo.tipo.movimiento.egreso")));
			
			colectionMotiMov.add(objIngresoCmm);
			colectionMotiMov.add(objEgresoCmm);
			
		}
		
		request.getSession().setAttribute(LISTA_MOTIVOS_MOVIMIENTOS, colectionMotiMov);
			
		
	}
	
	
	@SuppressWarnings("unchecked")
	private Boolean mostrarCantidadReservaInicial (Collection<ArticuloTemporadaKardexDTO> colArticuloTemporadaKardexDTOs, final String codigoMotivoMov) {
		Collection<ArticuloTemporadaKardexDTO> kardexDTOs = null;
		kardexDTOs = CollectionUtils.select(colArticuloTemporadaKardexDTOs, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				ArticuloTemporadaKardexDTO articuloTemporadaKardexDTO = (ArticuloTemporadaKardexDTO)arg0;
				
				return articuloTemporadaKardexDTO.getCodigoMotivoMovimiento().equals(codigoMotivoMov);
			}
		});
		return kardexDTOs.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private Collection<CalendarioMotivoMovimientoDTO> obtenerMotivoMovimientoPorCodigoTipMov (Collection<CalendarioMotivoMovimientoDTO> listMotMov, final String codigoTipoMov) {
		
		return CollectionUtils.collect(CollectionUtils.select(listMotMov, new Predicate() {
			
			public boolean evaluate(Object arg0) {	
				CalendarioMotivoMovimientoDTO cmm = (CalendarioMotivoMovimientoDTO) arg0;
				return cmm.getCodigoTipoMovimiento().equals(codigoTipoMov);
			}
		}), new Transformer() {
			
			public Object transform(Object arg0) {
				CalendarioMotivoMovimientoDTO cm = (CalendarioMotivoMovimientoDTO) arg0;
				
				StringBuilder desc = new StringBuilder();
				
				if (codigoTipoMov.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigo.tipo.movimiento.ingreso"))) {
					
					if (!cm.getDescripcionMotivoMovimiento().contains("(+)")) {
						desc.append("(+) ");
					}
					desc.append(cm.getDescripcionMotivoMovimiento());
					
				} else {
					if (!cm.getDescripcionMotivoMovimiento().contains("(-)")) {
						desc.append("(-) ");
					}	
					
					desc.append(cm.getDescripcionMotivoMovimiento());
				}
				
				
				cm.setDescripcionMotivoMovimiento(desc.toString());
				return cm;
			}
		});
		 
	}
	
	
	@SuppressWarnings("unchecked")
	private Collection<CalendarioMotivoMovimientoDTO> obtenerMotivoMovimientoPorCodigoMotMov (Collection<CalendarioMotivoMovimientoDTO> listMotMov, final String codigoMovMov) {
		return CollectionUtils.select(listMotMov, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				CalendarioMotivoMovimientoDTO cmm = (CalendarioMotivoMovimientoDTO) arg0;
				return cmm.getId().getCodigoMotivoMovimiento().equals(codigoMovMov);
			}
		});
		 
	}
	
	
	@SuppressWarnings("unchecked")
	private Collection<CalendarioMotivoMovimientoDTO> removerMotivosMovimientoPorCodigoMotMov (Collection<CalendarioMotivoMovimientoDTO> listMotMov, final String... codigoMovMov) {
		return CollectionUtils.select(listMotMov, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				CalendarioMotivoMovimientoDTO cmm = (CalendarioMotivoMovimientoDTO) arg0;
				boolean result = Boolean.TRUE;
				for (String string : codigoMovMov) {
					if (cmm.getId().getCodigoMotivoMovimiento().equals(string)) {
						result = Boolean.FALSE;
						break;
					}
				}
				return result;
			}
		});
		 
	}
	
	/**
	 * Guardar detalle movimiento kardex
	 * @param request
	 * @param form
	 * @param errors
	 * @param info
	 * @param exitos
	 * @param formulario
	 */
	@SuppressWarnings("unchecked")
	public void guardarDetalleMovimientoKardex (HttpServletRequest request, ActionForm form, ActionMessages errors, ActionMessages info, ActionMessages exitos, ListadoPedidosForm formulario) {
		
		if (validarCamposRequeridos(form, errors, info, exitos, formulario)) {
			
			ArticuloTemporadaKardexDTO temporadaKardexDTO = new ArticuloTemporadaKardexDTO();
			Map<String, Object> articuloSeleccionado  = (Map<String, Object>) request.getSession().getAttribute(ARTICULO_PERECIBLE_SELECCIONADO);
			
			ArticuloDTO articuloDTOSeleccionado = (ArticuloDTO) articuloSeleccionado.get("articulo");
			
			temporadaKardexDTO.setCodigoArticulo(articuloDTOSeleccionado.getId().getCodigoArticulo());
			temporadaKardexDTO.setCodigoProveedor(articuloSeleccionado.get(CODIGO_PROVEEDOR_KEY).toString());
			temporadaKardexDTO.setCodigoTemporada(SessionManagerSISPE.getCurrentTemporadaActiva().getId().getCodigoTemporada());
			temporadaKardexDTO.getId().setCodigoCompania(SessionManagerSISPE.getDefault().getDefaultCompanyId());
			
			temporadaKardexDTO.setCantidad(new Double (formulario.getCantidadMotivoMovimiento()));
			temporadaKardexDTO.setFechaInicio(new Date());
			temporadaKardexDTO.setCodigoMotivoMovimiento(formulario.getComboMotivoMovimiento());
			temporadaKardexDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			temporadaKardexDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			try {
				SISPEFactory.getServicioMonitoreoPerecibles().transGuardarDetalleMovimientoKardex(temporadaKardexDTO);
				exitos.add("",new ActionMessage("message.detalle.motivo.movimiento.guardado.exito"));
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
				request.getSession().removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				actualizarDatos(request, articuloDTOSeleccionado, formulario);
			} catch (SISPEException e) {
				errors.add("error", new ActionMessage("errors.guardar.detalle.movimiento.kardex"));
				errors.add("", new ActionMessage("errors.mail.send.error", e.toString().substring(e.toString().indexOf(":")+1)));
			}
			
		}
		
	}
	
	
	private Boolean validarCamposRequeridos (ActionForm form, ActionMessages errors, ActionMessages info, ActionMessages exitos, ListadoPedidosForm formulario) {
		Boolean resul = Boolean.TRUE;
		if (formulario.getComboMotivoMovimiento().equals("") || formulario.getComboMotivoMovimiento() == null 
				|| formulario.getComboMotivoMovimiento().equals("INGRESO") || formulario.getComboMotivoMovimiento().equals("EGRESO")) {
			
			errors.add("motivo movimiento", new ActionMessage("errors.requerido", "motivo movimiento"));
			resul = Boolean.FALSE;
		}
		
		if (formulario.getCantidadMotivoMovimiento().equals("") || formulario.getCantidadMotivoMovimiento() == null) {
			errors.add("cantidad", new ActionMessage("errors.requerido", "cantidad"));
			resul = Boolean.FALSE;
		} else {
			
			try {
				if (Double.valueOf(formulario.getCantidadMotivoMovimiento()) < 0) {
					errors.add("cantidad", new ActionMessage("errors.formato.double", "cantidad"));
					resul = Boolean.FALSE;
				}
			} catch (NumberFormatException e) {
				errors.add("cantidad", new ActionMessage("errors.formato.double", "cantidad"));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				resul = Boolean.FALSE;
			}
		}
		
		return resul;
	}
	
	/**
	 * Actualizar datos stock perecibles
	 * @param request
	 * @param articuloDTO
	 */
	@SuppressWarnings("unchecked")
	public void actualizarDatos (HttpServletRequest request, ArticuloDTO articuloDTO, ListadoPedidosForm formulario ) {
		
		ArticuloTemporadaDTO articuloTemporadaDTO = new ArticuloTemporadaDTO();
		ArticuloDTO artibuscar = new ArticuloDTO();
		artibuscar.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
		artibuscar.getId().setCodigoCompania(articuloDTO.getId().getCodigoCompania());
//		artibuscar.setNpCodigoBarras(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras());
		
		articuloTemporadaDTO.setArticuloProveedorDTO(new ArticuloProveedorDTO());
		articuloTemporadaDTO.getArticuloProveedorDTO().setArticulo(artibuscar);
		articuloTemporadaDTO.getId().setCodigoTemporada(SessionManagerSISPE.getCurrentTemporadaActiva().getId().getCodigoTemporada());

		
		Collection<ArticuloTemporadaDTO> articuloTemporadaActualizado = new ArrayList<ArticuloTemporadaDTO>();
		Collection<Map<String,Object>> colStockPredecibles = new ArrayList<Map<String, Object>>();
		
		try {
			articuloTemporadaActualizado = SISPEFactory.getServicioMonitoreoPerecibles().obtenerStockPerecibles(articuloTemporadaDTO);
			colStockPredecibles = construirColeccionPerecibles(articuloTemporadaActualizado);
			
		} catch (SISPEException e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		
		Collection<Map<String, Object>> colEnSesion = (Collection<Map<String, Object>>)request.getSession().getAttribute(COLECCION_PERECIBLES);
		
		buscarPerecibleActualizado(request, colEnSesion, (Map<String,Object>)colStockPredecibles.toArray()[0]);
		Collection<Map<String, Object>> listArt = (Collection<Map<String, Object>>) ((Map<String,Object>)colStockPredecibles.toArray()[0]).get(ART_PERECIBLES_KEY);
		
		cargarDetallesMovimientos((Map<String,Object>)listArt.toArray()[0], request, 0, formulario);
		request.getSession().setAttribute(ARTICULO_PERECIBLE_SELECCIONADO, (Map<String,Object>)listArt.toArray()[0]);
	}
	
	
	@SuppressWarnings("unchecked")
	private void buscarPerecibleActualizado (HttpServletRequest request, Collection<Map<String, Object>> colEnSesion, final Map<String, Object> nuevoPerecibles ) {
		
		request.getSession().removeAttribute(COLECCION_PERECIBLES);
		
		Collection<Map<String, Object>> colArtProAct = CollectionUtils.select(colEnSesion, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				 Map<String, Object> actualPerecible = ( Map<String, Object>) arg0;
				 
				 ProveedorID proveedorIDLocal = (ProveedorID) actualPerecible.get(ID_PROVEEDOR_KEY);
				 
				 ProveedorID proveedorIDNuevo = (ProveedorID) nuevoPerecibles.get(ID_PROVEEDOR_KEY);
				 
				 return proveedorIDLocal.getCodigoCompania().equals(proveedorIDNuevo.getCodigoCompania()) && 
							proveedorIDLocal.getCodigoProveedor().equals(proveedorIDNuevo.getCodigoProveedor()) ;
			}
		});
		
		Map<String, Object> articulosPerCol = (Map<String, Object>) colArtProAct.toArray()[0];
		
		Collection<Map<String, Object>> colArtPreAct = (Collection<Map<String, Object>>) articulosPerCol.get(ART_PERECIBLES_KEY);
		
		
		Map<String, Object> perecibleActualizado  = (Map<String, Object>)((Collection<Map<String, Object>>) nuevoPerecibles.get(ART_PERECIBLES_KEY)).toArray()[0];
		 
		final ArticuloDTO articuloActualizado = (ArticuloDTO) perecibleActualizado.get("articulo");
		
		Collection<Map<String, Object>> articulosActu = CollectionUtils.select(colArtPreAct, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Map<String, Object> artPerAct = (Map<String, Object>)arg0;
				ArticuloDTO artAct = (ArticuloDTO)artPerAct.get("articulo");
				articuloActualizado.setNpStockArticulo(artAct.getNpStockArticulo());
				return artAct.getId().getCodigoArticulo().equals(articuloActualizado.getId().getCodigoArticulo()) &&
						artAct.getId().getCodigoCompania().equals(articuloActualizado.getId().getCodigoCompania());
			}
		});
		
		//seteando nuevos valores
		Map<String, Object> articu = (Map<String, Object>) articulosActu.toArray()[0];
		articu.put("stockNegociado", perecibleActualizado.get("stockNegociado"));
		articu.put("stockDisponibleReservas", perecibleActualizado.get("stockDisponibleReservas"));
		articu.put("stockDisponibleLocales", perecibleActualizado.get("stockDisponibleLocales"));
		articu.put("stockRealBodega", new Double(((ArticuloDTO)articu.get("articulo")).getNpStockArticulo()));
		articu.put("desPesAsi", perecibleActualizado.get("desPesAsi"));
		articu.put("stockReservadoReservas", perecibleActualizado.get("stockReservadoReservas"));
		articu.put("stockReservadoLocales", perecibleActualizado.get("stockReservadoLocales"));
		
		request.getSession().setAttribute(COLECCION_PERECIBLES, colEnSesion);
	}
	
	
	
	/////////////////////////////////////////
	////////// Pesta\u00F1a Consolidar ///////////
	////////Pedido orden de Compra///////////
	/////////////////////////////////////////
	
	
	/**
	 * Consultar consolidado pedido
	 * @param request
	 * @param formulario
	 * @param errors
	 * @param info
	 * @return
	 */
	public Collection <Map <String, Object>> consultarConsolidadoPedido (HttpServletRequest request, ListadoPedidosForm formulario, ActionMessages errors, ActionMessages info) throws SISPEException{
		
		ArticuloDTO articuloDTO = generarObjetoBusqueda(formulario).getArticuloProveedorDTO().getArticulo();
		AreaTrabajoDTO areaTrabajoDTO = new AreaTrabajoDTO();
		
		try {
			areaTrabajoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			if (null != formulario.getCodigoLocal() && !formulario.getCodigoLocal().equals("")) {
				areaTrabajoDTO.getId().setCodigoAreaTrabajo(Integer.valueOf(formulario.getCodigoLocal()));
			}
		} catch (Exception e1) { LogSISPE.getLog().error("error de aplicaci\u00F3n",e1); }
		
		
		EntregaPedidoDTO entregaDTO = new EntregaPedidoDTO();
		obtenerFiltroFechaDespacho(formulario, entregaDTO);
		Collection<DetallePedidoDTO> colDetallePedido = null;
		
		try {
			
			TemporadaDTO temporadaDTO = SessionManagerSISPE.getCurrentTemporadaActiva();
			
			String estadoOrdenCompra = CODIGO_ESTADO_PEDIDO_PENDIENTE;
			if (formulario != null && formulario.getEstadoPedido() != null && !formulario.getEstadoPedido().equals("")) {
				estadoOrdenCompra = formulario.getEstadoPedido();
			}
			
			colDetallePedido = SISPEFactory.getServicioMonitoreoPerecibles().obtenerConsolidadoPedido(
					articuloDTO, areaTrabajoDTO, entregaDTO, temporadaDTO, estadoOrdenCompra);
			
		}catch (SISPEException e) {
			errors.add("", new ActionMessage("errors.mail.send.error", e.toString().substring(e.toString().indexOf(":")+1)));
		}
		
		if (colDetallePedido.isEmpty()) {
			return null;
			
		} else {
			return generarEstructuraConsolidadoPedido(colDetallePedido);
		}
	}
	
	
	
	/**
	 * Generar filtro por fecha de despacho
	 * @param formulario
	 * @param entregaDTO
	 */
	private void obtenerFiltroFechaDespacho (ListadoPedidosForm formulario, EntregaPedidoDTO entregaDTO) {
		
		entregaDTO.setId(null);
		
		Calendar now = Calendar.getInstance(new Locale("es", "ES"));
		
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		if (formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))) {
			
			Date fechaInicial= null, fechaFinal = null;
			
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = ConverterUtil.parseStringToDate(formulario.getFechaInicial());
					
					LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = ConverterUtil.parseStringToDate(formulario.getFechaFinal());
					LogSISPE.getLog().info("Fecha final: {}",fechaFinal);
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			entregaDTO.addCriteriaSearchParameter("fechaDespachoBodega", ComparatorTypeEnum.BETWEEN_INCLUDE_COMPARATOR, 
					new Date[]{fechaInicial, fechaFinal});
			
		} else if (formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))) {
			
			entregaDTO.setFechaDespachoBodega(new Timestamp( now.getTime().getTime()));
			
		} else if (formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))) {
			
			LogSISPE.getLog().info("fecha actual: {}",  now.getTime());
			now.add(Calendar.MONTH, - Integer.parseInt(formulario.getNumeroMeses()));
			LogSISPE.getLog().info("fecha anterior: {}", now.getTime());
			
			entregaDTO.addCriteriaSearchParameter("fechaDespachoBodega", ComparatorTypeEnum.BETWEEN_INCLUDE_COMPARATOR, 
					new Date[]{now.getTime(), new Timestamp(new Date().getTime())});
		} 
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection <Map <String, Object>> generarEstructuraConsolidadoPedido (Collection<DetallePedidoDTO> colDetallePedido) throws SISPEException {
		
		Collection<Map<String, Object>> colArticulos = new ArrayList<Map<String, Object>>();
		
		for (DetallePedidoDTO detallePedidoDTO : colDetallePedido ) {
			
			if (CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTOsCol())) {
				throw new SISPEException("Error, no se encontro estadosDetallesPedidos para el pedido " + detallePedidoDTO.getId().getCodigoPedido());
			}
			
			//devuelve siempre el estado detalle pedido asociado al estado pedido actual
			EstadoDetallePedidoDTO estadoDetallePedidoSearch = detallePedidoDTO.getEstadoDetallePedidoDTOsCol().toArray(new EstadoDetallePedidoDTO[0])[0]; 
			
			if (CollectionUtils.isEmpty(estadoDetallePedidoSearch.getEntregaDetallePedidoCol())) {
				throw new SISPEException("Error, no se encontraron entregas para el pedido " + detallePedidoDTO.getId().getCodigoPedido());
			}
			
			Collection<EntregaDetallePedidoDTO> colEntregas = estadoDetallePedidoSearch.getEntregaDetallePedidoCol();
			
			List<EntregaPedidoDTO> colEntregasProc = new ArrayList<EntregaPedidoDTO>();
			
			Arrays.sort(colEntregasProc.toArray(new EntregaPedidoDTO[0]), new Comparator<EntregaPedidoDTO>() {

				public int compare(EntregaPedidoDTO o1, EntregaPedidoDTO o2) {
					// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
					return o1.getFechaRegistro().compareTo(o2.getFechaRegistro());
				}
			} );
			
			for (EntregaDetallePedidoDTO entDetPedDTO : colEntregas) {
				
				int i = Collections.binarySearch(colEntregasProc, entDetPedDTO.getEntregaPedidoDTO(), new Comparator<EntregaPedidoDTO>() {

					public int compare(EntregaPedidoDTO o1, EntregaPedidoDTO o2) {
						return o1.getId().getCodigoEntregaPedido().compareTo(o2.getId().getCodigoEntregaPedido());
					}
				});
				
				if (i < 0) {
					
					entDetPedDTO.getEntregaPedidoDTO().setEntregaDetallePedidoCol(new ListSet());
					entDetPedDTO.getEntregaPedidoDTO().getEntregaDetallePedidoCol().add(entDetPedDTO);
					colEntregasProc.add(entDetPedDTO.getEntregaPedidoDTO());
				}
			}
			
			for (EntregaPedidoDTO entrega : colEntregasProc) {
				
				Map<String, Object> articuloEncontrado = buscarArticulo(detallePedidoDTO, colArticulos, entrega);
				for (EntregaDetallePedidoDTO entDetPedDTO : entrega.getEntregaDetallePedidoCol()) {
					if (null == articuloEncontrado ) {
						Map<String, Object> articuloMap = new HashMap<String, Object>();
						colArticulos.add(construirArticuloMap(detallePedidoDTO, articuloMap, entDetPedDTO));
						
						//agregar pedido al articulo, primera vez
						Collection<Map<String, Object>> colPedidoLocales = new ArrayList<Map<String, Object>>();
						Map<String, Object> nuevoPedidoLocalMap = new HashMap<String, Object>();
						colPedidoLocales.add(construirPedidoLocalMap(detallePedidoDTO, nuevoPedidoLocalMap, entrega.getFechaDespachoBodega(), entDetPedDTO.getCantidadEntrega(), estadoDetallePedidoSearch));
						articuloMap.put(PEDIDOS_COL_KEY, colPedidoLocales);
						
					} else {
						
						Collection<Map<String, Object>> coleccionPedidos = (Collection<Map<String, Object>>) articuloEncontrado.get(PEDIDOS_COL_KEY);
						
						Map<String, Object> nuevoPedidoLocalMap = new HashMap<String, Object>();
						coleccionPedidos.add(construirPedidoLocalMap(detallePedidoDTO, nuevoPedidoLocalMap, entrega.getFechaDespachoBodega(), entDetPedDTO.getCantidadEntrega(), estadoDetallePedidoSearch));
						Long cantidadConsolidadoActual = (Long) articuloEncontrado.get(CANTIDAD_CONSOLIDADO_KEY);
						Long nuevoCantidadPedido = (Long) nuevoPedidoLocalMap.get(CANTIDAD_KEY);
						
						articuloEncontrado.put(CANTIDAD_CONSOLIDADO_KEY, cantidadConsolidadoActual +  nuevoCantidadPedido);
					}
				}
			}
	}
		
		//ordenar por fecha de despacho ascendentemente
		List colArticulosRes = new ArrayList<Map<String, Object>>(colArticulos);
		Collections.sort(colArticulosRes, new Comparator<Map<String, Object>>() {

			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Timestamp time1 = (Timestamp) o1.get(FECHA_DESPACHO_KEY);
				Timestamp time2 = (Timestamp) o2.get(FECHA_DESPACHO_KEY);
				
				return time1.compareTo(time2);
			}
		});
		
		return colArticulosRes;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> buscarArticulo (DetallePedidoDTO detallePedidoDTO, Collection<Map<String, Object>> colArticulos, EntregaPedidoDTO entregaDTO) {
		
		final ArticuloID articuloIDABuscar = detallePedidoDTO.getArticuloDTO().getId();
//		EstadoDetallePedidoDTO estadoDetallePedido = detallePedidoDTO.getEstadoDetallePedidoDTOsCol().toArray(new EstadoDetallePedidoDTO[0])[0];
		
		Timestamp fechaDespacho =  entregaDTO.getFechaDespachoBodega();
		
		String fecha = null;
		try {
			fecha = UtilesSISPE.obtenerFechaConFormato(fechaDespacho, "yyyy-MM-dd");
		} catch (Exception e) {
			LogSISPE.getLog().error(e.getMessage());
		}
		
		final String fechaTest = fecha;
		
		
		Collection<Map<String, Object>> objetosEncontrados = null;
		objetosEncontrados = CollectionUtils.select(colArticulos, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Map<String, Object> objetoActual = (Map<String, Object>) arg0;
				ArticuloID articuloId = ((ArticuloDTO) objetoActual.get(ARTICULO_KEY)).getId();
				
				String fechaDespacho = null;
				try {
					fechaDespacho = UtilesSISPE.obtenerFechaConFormato((Timestamp)objetoActual.get(FECHA_DESPACHO_KEY), "yyyy-MM-dd");
				} catch (Exception e) {
					LogSISPE.getLog().error(e.getMessage());
				}
			
				
				return  articuloId.getCodigoArticulo().equals(articuloIDABuscar.getCodigoArticulo())
						&& articuloId.getCodigoCompania().equals(articuloIDABuscar.getCodigoCompania()) 
						&& fechaDespacho.equals(fechaTest);
			}
		});
		if (objetosEncontrados.isEmpty()) {
			return null;
			
		} else {
			return (Map<String, Object>) objetosEncontrados.toArray()[0];
		}
		
	}

	
	/**
	 * Obtener vistaPedido apartir del pedido selecciondo
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VistaPedidoDTO obtenerPedidoSeleccionado (HttpServletRequest request) throws SISPEException {
		
		Integer indiceArticulo = Integer.valueOf(request.getParameter("indiceArticulo"));
		Integer indicePedido = Integer.valueOf(request.getParameter("indicePedido"));
		
		Collection<Map<String, Object>> colArtiPedido = (Collection<Map<String, Object>>) request.getSession().getAttribute(COLECCION_CONSOLIDADO_PEDIDO);
		Map<String, Object> mapArtPedSeleccionado = (Map<String, Object>) colArtiPedido.toArray()[indiceArticulo];
		
		Collection<Map<String, Object>> colPedido = (Collection<Map<String, Object>>) mapArtPedSeleccionado.get(PEDIDOS_COL_KEY);
		Map<String, Object> mapPedSeleccionado = (Map<String, Object>) colPedido.toArray()[indicePedido];
		
		PedidoDTO pedidoDTO = (PedidoDTO) mapPedSeleccionado.get(PEDIDO_KEY);
		VistaPedidoDTO vistaPedidoDTO = new VistaPedidoDTO();
		vistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoDTO.getId().getCodigoAreaTrabajo());
		vistaPedidoDTO.getId().setCodigoPedido(pedidoDTO.getId().getCodigoPedido());
		vistaPedidoDTO.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
		
		Collection<VistaPedidoDTO> colVistaPedido = null;
		
		try {
			colVistaPedido = SISPEFactory.obtenerServicioSispe().transObtenerVistaPedido(vistaPedidoDTO);
		} catch (SISPEException e) {
			LogSISPE.getLog().error("Error al obtener vista pedido: {}", e);
			throw new SISPEException(e);
		}
		
		VistaPedidoDTO vistaPedidoResp = (VistaPedidoDTO) CollectionUtils.find(colVistaPedido, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				
				VistaPedidoDTO vistaPedidoSearch = (VistaPedidoDTO) arg0;
				
				return vistaPedidoSearch.getFechaFinalEstado() == null;
			}
		});
		
		return vistaPedidoResp;
		
	}
	
	private Map<String, Object> construirArticuloMap(DetallePedidoDTO detallePedidoDTO, Map<String, Object> articulosMap, EntregaDetallePedidoDTO entrega) {
		
		articulosMap.put(ARTICULO_KEY, detallePedidoDTO.getArticuloDTO());
		articulosMap.put(CANTIDAD_CONSOLIDADO_KEY, entrega.getCantidadEntrega());
		if (entrega != null) {
			
			articulosMap.put(FECHA_DESPACHO_KEY, entrega.getEntregaPedidoDTO().getFechaDespachoBodega());
			LogSISPE.getLog().info("fecha despacho: {}", entrega.getEntregaPedidoDTO().getFechaDespachoBodega() );
		}
		
		return articulosMap;
	}
	
	private Map<String, Object> construirPedidoLocalMap (DetallePedidoDTO detallePedidoDTO, Map<String, Object> nuevoPedidoLocalMap, 
			final Timestamp fechaDespachoBodega, Long cantidadEntrega, EstadoDetallePedidoDTO estadoDetallePedidoDTO ) throws SISPEException {
		
		if (null != estadoDetallePedidoDTO.getEstadoPedidoDTO()) {
			detallePedidoDTO.getPedidoDTO().setEstadoPedidoDTO(estadoDetallePedidoDTO.getEstadoPedidoDTO());
			nuevoPedidoLocalMap.put(NUM_RESERVA_KEY, estadoDetallePedidoDTO.getEstadoPedidoDTO().getLlaveContratoPOS());
			LogSISPE.getLog().info("Numero de la reserva: {}", estadoDetallePedidoDTO.getEstadoPedidoDTO().getLlaveContratoPOS());
		}
		nuevoPedidoLocalMap.put(PEDIDO_KEY, detallePedidoDTO.getPedidoDTO());
		nuevoPedidoLocalMap.put(CANTIDAD_KEY, cantidadEntrega);
		
		return nuevoPedidoLocalMap;
	}
	
	public void crearPopUpIncluirDetalle (HttpServletRequest request) {
		
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Opci\u00F3n excel");
		popUp.setAncho(40D);
		popUp.setContenidoVentana("servicioCliente/monitorStockPerecibles/chkIncluirDetalle.jsp");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setTope(20D);
		popUp.setAccionEnvioCerrar("requestAjax('enviarOCPerecibles.do', ['pregunta'], {parameters: 'cancelarIncluirDetalle=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setValorOK("enviarFormulario('xls', 0, false);hide(['popupConfirmar']);ocultarModal();");
		popUp.setValorCANCEL("requestAjax('enviarOCPerecibles.do', ['pregunta'], {parameters: 'cancelarIncluirDetalle=ok', popWait:false, evalScripts:true});ocultarModal();");
		request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
		
	}
	
	
	/**
	 * Generar xml datos
	 * @param request
	 * @param formulario
	 * @return
	 */
	public StringBuffer generarXmlReporteStockPerecibles ( HttpServletRequest request, ListadoPedidosForm formulario) {
		
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		xml.append("<datos>");
		
		generarXMLColecProveedores(xml, request, formulario);
		
		xml.append("<rangoFechaTemporada>");
		xml.append(generarRangoFechaTemporada());
		xml.append("</rangoFechaTemporada>");
		
		xml.append("<mostrarDetalle>");
		xml.append(formulario.getOpConfirmarIncluirDetalle() == null ? "NO" : "SI");
		xml.append("</mostrarDetalle>");
		
		xml.append("</datos>");
		
		
		return xml;
	}
	
	/**
	 * Generar contenido xml
	 * @param xml
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StringBuffer generarXMLColecProveedores (StringBuffer xml, HttpServletRequest request, ListadoPedidosForm formulario) {
		
		Collection<Map<String,Object>> colStockPredecibles = (Collection<Map<String,Object>>)request.getSession().getAttribute(COLECCION_PERECIBLES);
		List<Map<String, String>> listFechas = getArrayDiasTemporada(); /*Obtener listado fechas*/
		List listaMovimientosEr = consultarMovimientosKardex(request, true, ConstantesGenerales.RESERVA_EGRESO);
		List listaMovimientosIr = consultarMovimientosKardex(request, true, ConstantesGenerales.RESERVA_INGRESO);
		List listaMovimientosPestana2 = consultarMovimientosKardex(request, true, ConstantesGenerales.PEDIDO_ASISTIDO_INGRESO);
		
		if (null != colStockPredecibles) {
			
			//xml encabezado del archivo
			for (Map<String,Object> mapProveedor : colStockPredecibles) {
				VistaProveedorDTO proveedorDTO = (VistaProveedorDTO) mapProveedor.get("proveedor");
				xml.append("<listProveedores>");
				xml.append("<nombreProveedor>");
				xml.append(proveedorDTO.getNombreProveedor());
				xml.append("</nombreProveedor>");
				
				//lista articulos
				
				Collection<Map<String, Object>> artiPere = (Collection<Map<String, Object>>) mapProveedor.get("articulosPereciblesCol");
				for (Map<String, Object> articuloPerecibleMap : artiPere) {
					
					xml.append("<listArticulos>");
					xml.append("<nombreArticulo>");
					xml.append(((ArticuloDTO)articuloPerecibleMap.get("articulo")).getDescripcionArticulo());
					xml.append("</nombreArticulo>");
					
					xml.append("<codigoArticulo>");
					xml.append(((ArticuloDTO)articuloPerecibleMap.get("articulo")).getCodigoBarrasActivo().getId().getCodigoBarras());
					xml.append("</codigoArticulo>");
					
					String tipoStock = null;
					//generar tipos stock
					for (int i = 0; i < 5; i++) {
						
						tipoStock = new String( i == 0 ? "stockNegociado" : i == 1 ? "stockDisponibleReservas" :i==2? "stockDisponibleLocales":i==3? "stockRealBodega":"desPesAsi");
						
						xml.append("<");xml.append(tipoStock);xml.append(">");
						if (articuloPerecibleMap.get(tipoStock) == null) {
							xml.append(0);
						} else {
							xml.append(articuloPerecibleMap.get(tipoStock));
						}
						
						xml.append("</");xml.append(tipoStock);xml.append(">");
					}
					
					xml.append("</listArticulos>");
				}
				
				xml.append("<numeroArticulos>");
				xml.append(artiPere.size() - 1);
				xml.append("</numeroArticulos>");
				
				xml.append("</listProveedores>");
				
			}
			
			
			if (null != formulario.getOpConfirmarIncluirDetalle()) {
			
				//xml detalle de movimientos
				for (int i = 0; i < listFechas.size(); i++) {
					xml.append("<listKardex>");
					xml.append("<listFecha>");
					
					xml.append("<fecha>");
						xml.append(listFechas.get(i).get("dia"));
					xml.append("</fecha>");
					
					xml.append("<esDomingo>");
						if (null == listFechas.get(i).get("esDomingo")) {
							xml.append("no");
						} else {
							xml.append("si");
						}
					xml.append("</esDomingo>");
					
					
					for (Map<String,Object> mapProveedor : colStockPredecibles) {
						
						Collection<Map<String, Object>> artiPere = (Collection<Map<String, Object>>) mapProveedor.get("articulosPereciblesCol");
						
						for (Map<String, Object> articuloPerecibleMap : artiPere) {
							xml.append("<listValorCelda>");
							xml.append("<valorCelda>");
							xml.append(buscarMovimientoKardexPorDia(listaMovimientosIr, listaMovimientosEr, 
									listFechas.get(i).get("dia"), 
									(String)((ArticuloDTO) articuloPerecibleMap.get("articulo")).getId().getCodigoArticulo(), 
									((ProveedorID) mapProveedor.get(ID_PROVEEDOR_KEY)).getCodigoProveedor()));
							xml.append("</valorCelda>");
							
							//nuevos cambios
							
							xml.append("<valorCelda2>");
							xml.append(buscarMovimientoKardexPorDia(listaMovimientosPestana2, 
									listFechas.get(i).get("dia"), 
									(String)((ArticuloDTO) articuloPerecibleMap.get("articulo")).getId().getCodigoArticulo(), 
									((ProveedorID) mapProveedor.get(ID_PROVEEDOR_KEY)).getCodigoProveedor()));
							xml.append("</valorCelda2>");
							
							// fin nuevos cambios
							
							xml.append("<esDomingo>");
							if (null == listFechas.get(i).get("esDomingo")) {
								xml.append("no");
							} else {
								xml.append("si");
							}
							xml.append("</esDomingo>");
							
							xml.append("</listValorCelda>");
						}
					}
					
					xml.append("</listFecha>");
					xml.append("</listKardex>");
				}
				
			} else { /*Calcular total*/
				
				List listaMovimientosPorArticuloEr = consultarMovimientosKardex(request, false, ConstantesGenerales.RESERVA_EGRESO);
				List listaMovimientosPorArticuloIr = consultarMovimientosKardex(request, false, ConstantesGenerales.RESERVA_INGRESO);
				List listaMovimientosPorArticuloSegundaPestana = consultarMovimientosKardex(request, false, ConstantesGenerales.PEDIDO_ASISTIDO_INGRESO);
				
				for (Map<String,Object> mapProveedor : colStockPredecibles) {
					
					Collection<Map<String, Object>> artiPere = (Collection<Map<String, Object>>) mapProveedor.get("articulosPereciblesCol");
					
					for (Map<String, Object> articuloPerecibleMap : artiPere) {
						xml.append("<listValorCelda>");
						xml.append("<valorCelda>");
						xml.append(buscarMovimientoKardexPorArticulo(listaMovimientosPorArticuloEr, listaMovimientosPorArticuloIr, 
								(String)((ArticuloDTO) articuloPerecibleMap.get("articulo")).getId().getCodigoArticulo(), 
								((ProveedorID) mapProveedor.get(ID_PROVEEDOR_KEY)).getCodigoProveedor()));
						xml.append("</valorCelda>");
						xml.append("</listValorCelda>");
					}
					
					//sugunda pesta\u00F1a
					Collection<Map<String, Object>> artiDespachados = (Collection<Map<String, Object>>) mapProveedor.get("articulosPereciblesCol");
					//inicio nueva funcionalidad
					for (Map<String, Object> articuloPerecibleMap : artiDespachados) {
						xml.append("<listValorCeldaDespachados>");
						xml.append("<valorCelda>");
						xml.append(buscarMovimientoKardexPorArticulo(listaMovimientosPorArticuloSegundaPestana, 
								(String)((ArticuloDTO) articuloPerecibleMap.get("articulo")).getId().getCodigoArticulo(), 
								((ProveedorID) mapProveedor.get(ID_PROVEEDOR_KEY)).getCodigoProveedor()));
						xml.append("</valorCelda>");
						xml.append("</listValorCeldaDespachados>");
					}
					//fin nueva funcionalidad
				}
			}
		}
		
		return xml;
	}
	
	/**
	 * generar el reporte dato el template  y los datos
	 * @param request
	 * @param formulario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public byte[] crearExceReporteStockPerecibles (HttpServletRequest request, ListadoPedidosForm formulario) {
		
		Collection<Map<String,Object>> colStockPredecibles = (Collection<Map<String,Object>>)request.getSession().getAttribute(COLECCION_PERECIBLES);
		String contenidoArchivo = null;
		String contenidoXML = null;
		
		try {
			String contenidoXSL = TransformerUtil.obtenerPlantillaHTML(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.plantilla.stock.perecibles"));
			
			if (colStockPredecibles != null) {
				contenidoXML = generarXmlReporteStockPerecibles(request, formulario).toString();
				
				contenidoXML = contenidoXML.replace("&", "&amp;");

				Document result;
				
				Document docXML = TransformerUtil.stringToXML(contenidoXML);
			    Document docXSL = TransformerUtil.stringToXML(contenidoXSL);

			    result = TransformerUtil.transformar(docXML, docXSL);
			    
			    contenidoArchivo = TransformerUtil.xmlToString(result);
			    contenidoArchivo = contenidoArchivo.replaceAll("UTF-8", "iso-8859-1");
//			    transform(contenidoXML.getBytes());
			}
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error crear excel: {}", e);
		} 
		
		if (contenidoArchivo != null) {
			return contenidoArchivo.getBytes();
		}
		
		return new byte[0];
	}
	
	
	/**
	 * Metodo para prueba reportes
	 * @param bytes
	 */
	public void transform (byte [] bytes) {
		
//		javax.xml.transform.Source xmlSource =
//                new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(bytes));
		
		javax.xml.transform.Source xmlSource =
                new javax.xml.transform.stream.StreamSource("D:/templates/datos2.xml");
		
        javax.xml.transform.Source xsltSource =
                new javax.xml.transform.stream.StreamSource("D:/templates/template1.xsl");
        javax.xml.transform.Result result =
                new javax.xml.transform.stream.StreamResult("D:/templates/result.xsl");
 
        // create an instance of TransformerFactory
        javax.xml.transform.TransformerFactory transFact =
                javax.xml.transform.TransformerFactory.newInstance( );
 
        javax.xml.transform.Transformer trans;
		try {
			trans = transFact.newTransformer(xsltSource);
			
			trans.transform(xmlSource, result);
				
		} catch (TransformerException e) {
			LogSISPE.getLog().error("Error de aplicacion. "+e);
		} 
		
     
	}
	
	/**
	 * Metodo para consultar  los movimientos del kardex de acuerdo a la busqueda de stock perecibles
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List consultarMovimientosKardex (HttpServletRequest request, Boolean agruparConsultaPorDia, String codigoTipoMovimiento) {
		
		List resultado = null;
		
		try {
			
			ArticuloTemporadaKardexDTO articuloTemporadaKardexDTO = new ArticuloTemporadaKardexDTO();
			articuloTemporadaKardexDTO.setCodigoMotivoMovimiento(codigoTipoMovimiento);
			articuloTemporadaKardexDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			articuloTemporadaKardexDTO.setCodigoTemporada(SessionManagerSISPE.getCurrentTemporadaActiva().getId().getCodigoTemporada());
			
			//Coleccion de la busqueda stock perecibles guardada en session
			Collection<ArticuloTemporadaDTO> articuloTemporadaDTOs = (Collection<ArticuloTemporadaDTO>) request.getSession().getAttribute(COL_ART_TEM_BUSQUEDA);
			   
			List<String> listCodigoArticulo = new ArrayList<String>();
			
			//obtener codigoArticulo de la coleccion de la busqueda
			CollectionUtils.collect(articuloTemporadaDTOs, new Transformer() {
				
				public Object transform(Object arg0) {
					ArticuloTemporadaDTO articuloTemporadaDTO = (ArticuloTemporadaDTO)arg0;
					return articuloTemporadaDTO.getId().getCodigoArticulo();
				}
			}, listCodigoArticulo);
			
//			for (ArticuloTemporadaDTO articuloTemporadaDTO : articuloTemporadaDTOs) {
//				listCodigoArticulo.add(articuloTemporadaDTO.getId().getCodigoArticulo());
//			}
			
			resultado = SISPEFactory.getServicioMonitoreoPerecibles().
					obtenerCantidadMovimientoPorFechaYArticulo(articuloTemporadaKardexDTO, 
							listCodigoArticulo.isEmpty() ? null : listCodigoArticulo.toArray(new String [0]), agruparConsultaPorDia);
			
		} catch (SISPEException e) {
			LogSISPE.getLog().error("Error al consultar movimiento del kardex: {}", e);
		} catch (Exception e) {
			LogSISPE.getLog().error("Error aplicacion: {}", e);
		}
		
		return resultado;
	}
	
	public void generarDetalleMovimientosPorDias (StringBuffer xml, Collection<Map<String, Object>> artiPere, List<String> listaFechas) {
		
		for (int i = 0 ; i < listaFechas.size(); i++) {
			xml.append("<").append(i).append(">");
			
			xml.append("<fecha>");
				xml.append(listaFechas.get(i));
			xml.append("</fecha>");
			
			xml.append("</").append(i).append(">");
			
		}
		
	}
	
	/**
	 * Obtener lista de dias desde el inicio hasta el fin de temporada
	 * Se identifica si el dia es domingo
	 * @return 
	 */
	private List<Map<String, String>> getArrayDiasTemporada() {

		List<Map<String, String>> listDias = new ArrayList<Map<String, String>>();

		TemporadaDTO temporadaDTO = SessionManagerSISPE.getCurrentTemporadaActiva();

		Calendar calendarInicio = Calendar.getInstance(new Locale("es", "ES"));
		calendarInicio.setTime(temporadaDTO.getFechaInicialTemporada());

		Calendar calendarFin = Calendar.getInstance(new Locale("es", "ES"));
		calendarFin.setTime(temporadaDTO.getFechaFinalTemporada());

//		DateFormat sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("es", "ES"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
//		int i = 0;

		do {
//			i++;
//			System.out.print(i + ") ");
//			System.out.println(dt.format(new Date(calendarInicio .getTimeInMillis())));
			
			Map<String, String> diaMap = new HashMap<String, String>();
			diaMap.put("dia", sdf.format(new Date(calendarInicio.getTimeInMillis())));
			
			if (calendarInicio.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//				System.out.println(" Domingo");
				diaMap.put("esDomingo", "si");
			}
			
			listDias.add(diaMap);

			calendarInicio.add(Calendar.DAY_OF_MONTH, 1);
		} while (calendarInicio.getTime().before(calendarFin.getTime()));

		return listDias;
	}
	
	/**
	 * Buscar articulo en la colleccion del kardex y retorna el valor de la cantidad vendida por dia
	 * @param colABuscar
	 * @param fechaDia
	 * @param codigoArticulo
	 * @param codigoProveedor
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Double buscarMovimientoKardexPorDia (List colABuscarIr, List colABuscarEr, final String fechaDia, final String codigoArticulo, final String codigoProveedor) {
		
		Collection col = colABuscarEr;
		Collection col2 = colABuscarIr;
		//para los egresos
		Object registroEncontradoEr = (Object) CollectionUtils.find(col, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				Date time = (Date) dato[3];
				//Timestamp time = (Timestamp) dato[3];
				
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
				String fecha = sd.format(time);
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor) && fecha.equals(fechaDia);
			}
		});
		
		//Para las devoluciones
		Object registroEncontradoIr = (Object) CollectionUtils.find(col2, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				Date time = (Date) dato[3];
				//Timestamp time = (Timestamp) dato[3];
				
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
				String fecha = sd.format(time);
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor) && fecha.equals(fechaDia);
			}
		});
		
		if (null == registroEncontradoEr && null == registroEncontradoIr ) {
			return 0D;
		} else if(null == registroEncontradoIr){
			return (Double)((Object[]) registroEncontradoEr)[0];
		}else if(null == registroEncontradoEr){
			return (Double)((Object[]) registroEncontradoIr)[0];
		}else{
			return (Double)((Object[]) registroEncontradoEr)[0] - (Double)((Object[]) registroEncontradoIr)[0];
		}
		
	}
public Double buscarMovimientoKardexPorDia (List colABuscar, final String fechaDia, final String codigoArticulo, final String codigoProveedor) {
		
		Collection col = colABuscar;
		Object registroEncontrado = (Object) CollectionUtils.find(col, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				Date time = (Date) dato[3];
				//Timestamp time = (Timestamp) dato[3];
				
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
				String fecha = sd.format(time);
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor) && fecha.equals(fechaDia);
			}
		});
		
		
		if (null == registroEncontrado) {
			return 0D;
		} else{ 
			return (Double)((Object[]) registroEncontrado)[0];
		}
		
	}
	
	/**
	 * Consultar total de movimientos por articulo
	 * @param colABuscar
	 * @param codigoArticulo
	 * @param codigoProveedor
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Double buscarMovimientoKardexPorArticulo (List colABuscar, final String codigoArticulo, final String codigoProveedor) {
		
		Collection col = colABuscar;
		
		Object registroEncontrado = (Object) CollectionUtils.find(col, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor);
			}
		});
		
		if (null == registroEncontrado) {
			return 0D;
		} else {
			return (Double)((Object[]) registroEncontrado)[0];
		}
		
	}
	public Double buscarMovimientoKardexPorArticulo (List listaMovimientosPorArticuloEr, List listaMovimientosPorArticuloIr, final String codigoArticulo, final String codigoProveedor) {
		
		Collection colEr = listaMovimientosPorArticuloEr;
		Collection colIr = listaMovimientosPorArticuloIr;
		
		Object registroEncontradoEr = (Object) CollectionUtils.find(colEr, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor);
			}
		});
		
		Object registroEncontradoIr = (Object) CollectionUtils.find(colIr, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				Object [] dato = (Object []) arg0;
				String codArticulo = (String) dato[1];
				String codProvee = (String) dato[2];
				
				return codArticulo.equals(codigoArticulo) && codProvee.equals(codigoProveedor);
			}
		});
		
		if (null == registroEncontradoEr && null == registroEncontradoIr ) {
			return 0D;
		} else if(null == registroEncontradoIr){
			return (Double)((Object[]) registroEncontradoEr)[0];
		}else if(null == registroEncontradoEr){
			return (Double)((Object[]) registroEncontradoIr)[0];
		}else{
			return (Double)((Object[]) registroEncontradoEr)[0] - (Double)((Object[]) registroEncontradoIr)[0];
		}
	}
	/**
	 * Generar cadena del rando de la fecha de la temporada para el reporte
	 * @return
	 */
	public StringBuilder generarRangoFechaTemporada () {
		
		TemporadaDTO temporadaDTO = SessionManagerSISPE.getCurrentTemporadaActiva();
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es"));
		
		Calendar fechaInicio = Calendar.getInstance(new Locale("es", "ES"));
		fechaInicio.setTime(temporadaDTO.getFechaInicialTemporada());
		
		Calendar fechaFin = Calendar.getInstance(new Locale("es", "ES"));
		fechaFin.setTime(temporadaDTO.getFechaFinalTemporada());
		
		StringBuilder textoFecha = new StringBuilder();
		textoFecha.append("DEL ");
		textoFecha.append(fechaInicio.get(Calendar.DAY_OF_MONTH));
		
		textoFecha.append(" DE ");
		textoFecha.append(dfs.getMonths()[fechaInicio.get(Calendar.MONTH)].toUpperCase());
		
		textoFecha.append(" DEL ");
		textoFecha.append(fechaInicio.get(Calendar.YEAR));
		
		textoFecha.append(" AL ");
		textoFecha.append(fechaFin.get(Calendar.DAY_OF_MONTH));
		
		textoFecha.append(" DE ");
		textoFecha.append(dfs.getMonths()[fechaFin.get(Calendar.MONTH)].toUpperCase());
		
		textoFecha.append(" DEL ");
		textoFecha.append(fechaFin.get(Calendar.YEAR));
		
		return textoFecha;
	}
	
	
	/**
	 * Enviar orden de compra
	 */
	@SuppressWarnings("unchecked")
	public void enviarOrdenCompra (HttpServletRequest request, ListadoPedidosForm form, ActionMessages errors, ActionMessages info, ActionMessages exitos) {
		
		Collection<Map<String, Object>> colConPed = (Collection<Map<String, Object>>) request.getSession().getAttribute(PereciblesUtil.COLECCION_CONSOLIDADO_PEDIDO);
		
		if (CollectionUtils.isEmpty(colConPed) || 
				form.getEstadoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enviado.ordencompra"))) {
			
			info.add("listaVacia", new ActionMessage("mensaje.orden.compra.sin.registros", "registros"));
			
		} else {
		
			Collection<EstadoPedidoDTO> estPedCol = new ArrayList<EstadoPedidoDTO> ();
			
			for (Map<String, Object> map : colConPed) {
				Collection<Map<String, Object>> colPedidos = (Collection<Map<String, Object>>) map.get(PEDIDOS_COL_KEY);
				
				for (Map<String, Object> mapPedido : colPedidos) {
					PedidoDTO pedidoDTO = (PedidoDTO) mapPedido.get(PEDIDO_KEY);
					
					estPedCol.add(pedidoDTO.getEstadoPedidoDTO());
				}
			}
			
			try {
				
				SISPEFactory.getServicioMonitoreoPerecibles().transEnviarOrdenCompra(colConPed, 
						estPedCol, SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				exitos.add("",new ActionMessage("mensaje.orden.compra.exito"));
				
				request.getSession().removeAttribute(PereciblesUtil.COLECCION_CONSOLIDADO_PEDIDO);
				
			} catch (SISPEException e) {
				errors.add("", new ActionMessage("error.procesar.orden.compra"));
				errors.add("SISPEException",new ActionMessage("errors.SISPEException", e.getMessage()));
				LogSISPE.getLog().error("Error al enviar Orden compra: " + e);
				
			}  catch (Exception e) {
				errors.add("", new ActionMessage("error.procesar.orden.compra"));
				errors.add("SISPEException",new ActionMessage("errors.SISPEException", e.getMessage()));
				LogSISPE.getLog().error("Error al enviar Orden compra: " + e);
			}
			
		}
	}
}
