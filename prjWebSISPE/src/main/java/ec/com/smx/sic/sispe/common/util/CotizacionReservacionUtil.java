/*
 * CotizacionReservacionUtil.java
 * Creado el 14/07/2008 10:23:23
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_CON_IVA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CHECK_PAGO_EFECTIVO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CLASIFICACION_RECETAS_CATALOGO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_ESTABLECIMIENTO_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.ESTABLECIMIENTOS_DEPARTAMENO_CLASIFICACIONES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.HABILITADO_CAMBIO_PRECIOS;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.TOTAL_DESCUENTO_POR_ARTICULO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.VALOR_PORCENTAJE_IVA;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.DEFAULT_COMPANY;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.ESTADO_REGISTRO_ANULAR;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.ESTADO_REGISTRO_IGUAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.ESTADO_REGISTRO_NUEVO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_AUMENTAR_PRODUCTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS;

import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearchParameter;
import ec.com.smx.autorizaciones.common.factory.AutorizacionesFactory;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.autorizaciones.dto.AutorizacionValorComponenteDTO;
import ec.com.smx.autorizaciones.integracion.dto.Autorizacion;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corporativo.commons.factory.CorporativoFactory;
import ec.com.smx.corporativo.gestionservicios.dto.EmpresaDTO;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.FuncionarioDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.ClasesUtil;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.frameworkv2.util.dto.DynamicComponentDto;
import ec.com.smx.frameworkv2.util.dto.DynamicComponentValueDto;
import ec.com.smx.sic.cliente.common.SICConstantes;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloCalculo;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloLocalPrecioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloPrecioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionUsuarioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TemporadaDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.vo.ArticuloVO;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.ArchivoEntregaDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoDTO;
import ec.com.smx.sic.sispe.dto.AutorizacionEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DefArchivoEntregaDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoID;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetalleRecetaDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoBloqueoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaRecetaArticuloDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ConsolidarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleCanastaAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ListaModificarReservacionAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ModificarReservacionAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.DespachosEntregasForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author fmunoz
 * @author Wladimir L\u00F3pez
 *
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class CotizacionReservacionUtil
{
	public static final String ID_ROL_COMPRAS = "ec.com.smx.sic.sispe.rolCompras";
	//variables que almacenarn los tipos de art\u00EDculo para el calculo de los totales
	private static final String TIPO_ARTICULO_PAVO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
	private static final String TIPO_ARTICULO_OTRO_PESO_VARIABLE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.otroPesoVariable");
	
	public static final String PRECIOS_ALTERADOS = "ec.com.smx.sic.sispe.reservacion.preciosMejorados";
	public static final String PRECIOS_ALTERADOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.consolidar.preciosMejorados";
	public static final String PRECIOS_ALTERADOS_RESERVAS_PTO_LQD = "ec.com.smx.sic.sispe.reservacion.pagada.con.cambio.precios";
//	public static final String PRECIOS_ACTUALES = "SI_PA";
	
	public final static String CODIGOS_CLASIFICACIONES_ESPECIALES = "ec.com.smx.sic.sispe.codigosClasificacionesPerecibles";
	public static final String ORDEN_ASCENDENTE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	// variable para habilitar o desabilitar el campo de peso de los articulos de peso variable
	public static final String MODIFICAR_PESO_INACTIVO = "ec.com.smx.sic.sispe.modificarPeso.inactivo";
	//variable para almacenar los descuentos seleccionados cuando primero se escoge los descuentos y luego se pide la autorizacion
	public static final String DESCUENTOS_SELECCIONADOS = "ec.com.smx.sic.sispe.descuentos.seleccionados";
	public static final String PORCENTAJE_DESCUENTO = "ec.com.smx.sic.sispe.porcentaje.descuento";
	public static final String PORCENTAJE_DESCUENTO_VARIABLE = "ec.com.smx.sic.sispe.porcentaje.variables.descuento";
	
	//variable para almacenar los descuentos seleccionados cuando primero se escoge los descuentos y luego se pide la autorizacion
	
	//Variables para descuentos
	private static final String CODIGO_GERENTE_COMERCIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoGerenteComercial");
//	private static final String CODIGO_ADMINISTRADOR_LOCAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoAdministradorLocal");
	private static final String CODIGO_TIPO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento"); //CTD
	private static final String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");	
	public  static final String MOSTRAR_AUTORIZACION_ADMIN_LOCAL = "ec.com.smx.sic.sispe.parametro.autorizacion.adminLocal";
	
	//variables para controlar el descuento de cajas y mayorista
	public static final String APLICA_DESCUENTOS_PORCAJAS ="ec.com.smx.sic.sispe.aplica.descuentoPorCajas";
	public static final String APLICA_DESCUENTOS_PORMAYORISTA ="ec.com.smx.sic.sispe.aplica.descuentoPorMayorista";
	public static final String APLICA_DESCUENTOS_PORCAJAS_POPUP ="ec.com.smx.sic.sispe.aplica.descuentoPorCajasPopUp";
	public static final String APLICA_DESCUENTOS_PORMAYORISTA_POPUP ="ec.com.smx.sic.sispe.aplica.descuentoPorMayoristaPopUp";
	public static final String APLICA_DESCUENTO_MARCA_PROPIA ="ec.com.smx.sic.sispe.aplica.descuento.marcaPropia";
	
	public static final String LLAVE_DESCUENTOS_PORCAJAS ="ec.com.smx.sic.sispe.llave.descuentoPorCajas";
	public static final String LLAVE_DESCUENTOS_PORMAYORISTA ="ec.com.smx.sic.sispe.llave.descuentoPorMayorista";
	public static final String LLAVE_DESCUENTOS_MARCA_PROPIA ="ec.com.smx.sic.sispe.llave.descuento.marca.propia";
	
	//codigos de las clasificaciones de canastos y pavos
	public static final String CLASIFICACIONES_PAVOS = "ec.com.smx.sic.sispe.clasificaciones.pavos";
	public static final String CLASIFICACIONES_CANASTOS = "ec.com.smx.sic.sispe.clasificaciones.canastos";
	//variable para controlar el mensaje de los abonos en el reporte comprobanteBonoReserva.jsp
	public static final String DESC_NAVEMP_CREDITO_SELECCIONADO = "ec.com.smx.sic.sispe.navEmpCre.seleccionado";
	
	//variable para almacenar si un pedido tiene costo de entrega
	public static final String COSTO_FLETE_ENTREGA_PEDIDO_CONSOLIDADO = "ec.com.smx.sic.sispe.costoEntregaPedidoConsolidado";
	
	//variable para almacenar si un pedido tiene costo de entrega
	public static final String CANASTOS_MODIFICADOS_CONSOLIDACION = "ec.com.smx.sic.sispe.canastos.modificados.consolidacion";
	
	//varialbe para almacenar si el local esta activo para dar precio mayorista
	public static final String LOCAL_ACTIVO_PRECIO_MAYORISTA= "ec.com.smx.sic.sispe.local.activo.precioMayorista";
	
	//variables para clasificar los tipos de descuentos
	public static String excluyente=null;
	public static String otros=null;
	public static String variables=null;
	
	/**
	 * Verifica que cuando el usuario est\u00E9 cotizando, recotizando o reservando y decide escoger otra opci\u00F3n 
	 * en el men\u00FA principal se muestre una pregunta de confirmaci\u00F3n.
	 * 
	 * @param request		Petici\u00F3n que se est\u00E1 procesando
	 * @param accion		Acci\u00F3n que se desea realizar
	 * @return int 			Variable bandera que indica si se debe o no mostrar la pregunta, 
	 * 						[1: se debe mostrar la pregunta, 0: no se debe mostrar la pregunta]							
	 */
	public static int verificarPregunta(HttpServletRequest request, String accion)
	{
		HttpSession session = request.getSession();
		session.removeAttribute("ec.com.smx.sic.sispe.accionEscojida");
		if(request.getAttribute("ec.com.smx.sic.sispe.respuestaPregunta.si")==null){
			if(session.getAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO)!=null){
				session.setAttribute("ec.com.smx.sic.sispe.accionEscojida",accion);
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param request
	 * @param estadoActivo
	 * @throws Exception
	 */
	public static void cargarConfiguracionDescuentos(HttpServletRequest request, String estadoActivo)throws Exception{
		//---------------------- se cargan los descuentos -----------------------
		//se crean las colecciones que almacenar\u00E1n los tipos y motivos de descuentos 
		LogSISPE.getLog().info("cargarConfiguracionDescuentos");
		TipoDescuentoDTO tipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
		tipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		tipoDescuentoDTO.setEstadoTipoDescuento(estadoActivo);
		tipoDescuentoDTO.setEstadoPublicacion(estadoActivo);
		tipoDescuentoDTO.setDetalleDescuentos(new ArrayList<DescuentoDTO>());
		Collection<TipoDescuentoDTO> tipoDescuento = SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoDescuento(tipoDescuentoDTO);
		validarVisibilidadMotivoDescuento(request,tipoDescuento);
		clasificarTipoDescuentoEnExcluyentes(request,tipoDescuento);
		
		Collection<TipoDescuentoDTO> tipoDescuentoMostrarRangos = quitarTipoDesCajaMayorista(tipoDescuento, request);
		
		//Motivos [MON,CAN]
		MotivoDescuentoDTO motivoDescuentoDTO = new MotivoDescuentoDTO(Boolean.TRUE);
		motivoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		motivoDescuentoDTO.getId().setCodigoMotivoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.monto"));
		motivoDescuentoDTO.setEstadoMotivoDescuento(estadoActivo);
		Collection<MotivoDescuentoDTO> motivoDescuento = SessionManagerSISPE.getServicioClienteServicio().transObtenerMotivoDescuento(motivoDescuentoDTO);
		
//		CompradorDTO comprador = new CompradorDTO(Boolean.TRUE);
//		comprador.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		comprador.getId().setCodigoComprador(null);
//		comprador.setTipoComprador(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoCompradorInterno"));
//		FuncionarioDTO funcionario = new FuncionarioDTO();
//		funcionario.setEstadoFuncionario(CorporativoConstantes.ESTADO_ACTIVO);
//		FuncionarioPerfilDTO funPerDTO = new FuncionarioPerfilDTO();
//		ProfileDto perfil = new ProfileDto();
//		perfil.setProfileName(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.perfilComprador.gerenteComercial"));
//		funPerDTO.setPerfilDTO(perfil);
//		Collection<FuncionarioPerfilDTO> perfiles = new ArrayList<FuncionarioPerfilDTO>(); perfiles.add(funPerDTO);
//		funcionario.setFuncionarioPerfiles(perfiles);
		//comprador.setFuncionarioComprador(funcionario);
//		comprador.setFuncionarioDTO(funcionario);
//		Set<ClasificacionDTO> clasificacionCol = new HashSet<ClasificacionDTO>();
//		ClasificacionDTO clasificacionDTO = new ClasificacionDTO(Boolean.TRUE);
//		clasificacionDTO.getId().setCodigoClasificacion(null);
//		clasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		clasificacionCol.add(clasificacionDTO);
//		comprador.setClasificacionCol(clasificacionCol);
//		Collection<CompradorDTO> compradoresInternos = SISPEFactory.getDataService().findObjects(comprador);
		
		//Clasificar los tipos de descuentos
		
		for(TipoDescuentoDTO clasificarDescuentos : tipoDescuento){
			if(clasificarDescuentos.getNpExcluyente() == MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.excluyentes")){
				excluyente=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.excluyentes");
			}else if(clasificarDescuentos.getNpExcluyente() == MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.otros")){
				otros=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.otros");
			}else{
				variables= MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.variables");
			}
			
		}
		
		//se almacena los tipos de clasificacion
		request.getSession().setAttribute(CotizarReservarAction.TIPO_DES_EXCLUYENTES, excluyente);
		request.getSession().setAttribute(CotizarReservarAction.TIPO_DES_OTROS, otros);
		request.getSession().setAttribute(CotizarReservarAction.TIPO_DES_VARIABLES, variables);

		//se almacenan los tipos y motivos de descuentos
		request.getSession().setAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO, tipoDescuento);
		request.getSession().setAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO_MOSTRAR_RANGOS, tipoDescuentoMostrarRangos);
		request.getSession().setAttribute(CotizarReservarAction.COL_MOTIVO_DESCUENTO, motivoDescuento);
//		request.getSession().setAttribute(CotizarReservarAction.COL_COMPRADORES_INTERNOS, compradoresInternos);

		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.aplicar.autorizacion.adminLocal", request);
		request.getSession().setAttribute(MOSTRAR_AUTORIZACION_ADMIN_LOCAL, parametroDTO.getValorParametro()); 		 
		 
	}
	
	/**
	 * Determina el precio que se debe tomar en cuenta para realizar el c\u00E1lculo del total del detalle para el 
	 * art\u00EDculo que se va a agregar al detalle.
	 * 
	 * @param cantidad						La cantidad/peso del art\u00EDculo que se va a incluir en el detalle
	 * @param sizeDetalle					El tama\u00F1o del detalle del pedido
	 * @param articuloDTO					El objeto <code>ArticuloDTO</code> desde donde se obtienen los datos 
	 * 														del precio
	 * @param  request						La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return detallePedidoDTO		El detalle del pedido que fu\u00E9 creado
	 */
	public static DetallePedidoDTO construirNuevoDetallePedido(Long cantidad, int sizeDetalle, ArticuloDTO articuloDTO, HttpServletRequest request,ActionMessages errors)throws Exception{
		
		LogSISPE.getLog().info("cantidad: {}", cantidad);
		//se obtienen los estados
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		HttpSession session = request.getSession();
		
		//Obtengo la accion actual
		String accion=(String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		//se inicializan los objetos del detalle
		DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
		detallePedidoDTO.setArticuloDTO(articuloDTO);
		detallePedidoDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
		try{
			//se verifica el stock del art\u00EDculo
			if(articuloDTO.getNpStockArticulo()!=null 
					&& articuloDTO.getNpStockArticulo().longValue() < cantidad.longValue()){
				//existen problemas de stock
				articuloDTO.setNpEstadoStockArticulo(estadoInactivo);
			}

			//se asignan las propiedades al ArticuloDTO
			articuloDTO.setNpCodigoClasificacionAnterior(articuloDTO.getCodigoClasificacion()); 
			//se asigna el c\u00F3digo de clasificaci\u00F3n
			LogSISPE.getLog().info("NP CODIGO CLASIFICACION ANTERIOR: {}",articuloDTO.getCodigoClasificacion());
			//creacion del Estado del detalle
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadParcialEstado(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setReservarBodegaSIC(estadoInactivo);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEspecialReservado(estadoInactivo);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstadoReservado(0D);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(0D);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(cantidad);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantidad);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoInactivo);
			
			//se validan los datos de los articulos
			int cantidadErrores = errors.size();
			UtilesSISPE.validarArticuloDetallePedido(detallePedidoDTO, errors);
			if(cantidadErrores != errors.size()){
				return null;
			}
			
			//detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(articuloDTO.getCantidadMinimaMayoreo());			
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(articuloDTO.getHabilitadoPrecioMayoreo() ? articuloDTO.getCantidadMayoreo():0);
			
			//valores sin IVA
			//detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(articuloDTO.getPrecioArticulo());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(articuloDTO.getPrecioBase());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaEstado(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCaja(): 0);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayorista(articuloDTO.getHabilitadoPrecioMayoreo() ? articuloDTO.getPrecioMayorista(): 0);
			
			//sdetallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstadoOriginal(articuloDTO.getPrecioArticulo());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstadoOriginal(articuloDTO.getPrecioBase());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaEstadoOriginal(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCaja(): 0);
			
			//stock obsoleto
			if(articuloDTO.getNpStockArticulo()!=null && detallePedidoDTO.getArticuloDTO().getClaseArticulo()!=null){
				detallePedidoDTO.getEstadoDetallePedidoDTO().setStoLocArtObs(detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals("O") ?articuloDTO.getNpStockArticulo().intValue():null);
			}
			
			//se verifica si el art\u00EDculo aplica IVA
			if(articuloDTO.getAplicaImpuestoVenta()){
				//valores con IVA
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(articuloDTO.getPrecioArticuloIVA());
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstado(articuloDTO.getPrecioCajaIVA());
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVA(articuloDTO.getPrecioMayoristaIVA());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(articuloDTO.getPrecioBaseImp());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstado(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCajaImp(): 0);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVA(articuloDTO.getHabilitadoPrecioMayoreo() ? articuloDTO.getPrecioMayoristaImp():0);
				
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstadoOriginal(articuloDTO.getPrecioBaseImp());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstadoOriginal(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCajaImp(): 0);
			}else{
				//se asignan los valores sin IVA
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(articuloDTO.getPrecioBase());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstado(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCaja():0);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVA(articuloDTO.getHabilitadoPrecioMayoreo() ? articuloDTO.getPrecioMayorista():0);
				
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstadoOriginal(articuloDTO.getPrecioBase());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstadoOriginal(articuloDTO.getHabilitadoPrecioCaja() ? articuloDTO.getPrecioCaja(): 0);
			}
			
			//se relaliza el c\u00E1lculo de los precios no afiliados
			WebSISPEUtil.calcularPreciosNoAfiliados(request, detallePedidoDTO.getEstadoDetallePedidoDTO(), articuloDTO);
			
			//Si la accion no es de Creacion de pedidos especiales
			if(!accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.crearPedidoEspecial"))){
				
				//double pesoTotalAproximado = articuloDTO.getPesoAproximado().doubleValue() * detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
				double pesoTotalAproximado = articuloDTO.getArticuloComercialDTO().getPesoAproximadoVenta() * detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
				detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));

				//se verifica el tipo de art\u00EDculo
				if(articuloDTO.getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
					//se almacena el valor ingresado en el campo cantidad al peso
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(cantidad));
				}
				
				//llamada al m\u00E9todo que calcula los totales del detallle
				calcularValoresDetalle(detallePedidoDTO, request, true,false);
			}

			//se verifica si el art\u00EDculo tiene IVA para la asignaci\u00F3n de estados
			if(articuloDTO.getAplicaImpuestoVenta())
				detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(estadoActivo);
			else
				detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(estadoInactivo);

			String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
			//se crea el detalle del pedido
			detallePedidoDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
			detallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			detallePedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
			detallePedidoDTO.setNpContadorEntrega(0L);
			detallePedidoDTO.setNpCodigoClasificacion(articuloDTO.getCodigoClasificacion());
			detallePedidoDTO.setNpCodigoClasificacionArticulo(articuloDTO.getCodigoClasificacion() + caracterToken + articuloDTO.getId().getCodigoArticulo());
			detallePedidoDTO.setNpCodigoTipoDescuento(articuloDTO.getNpCodigoTipoDescuento());
			detallePedidoDTO.setNpCodigoTipoDescuentoArticulo(articuloDTO.getNpCodigoTipoDescuento() + caracterToken
					+ articuloDTO.getId().getCodigoArticulo() + caracterToken + sizeDetalle);
			//campos para registrar el pedido por departamento
			detallePedidoDTO.setNpDepartamento(articuloDTO.getNpDepartamento());
			detallePedidoDTO.setNpDepartamentoArticulo(articuloDTO.getNpDepartamento() + caracterToken + articuloDTO.getId().getCodigoArticulo());

			//condici\u00F3n que verifica si el art\u00EDculo es un canasto, en ese caso se crea una nueva referencia al 
			//objeto ArticuloDTO
//			if(articuloDTO.getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) || 
//					articuloDTO.getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
			if(articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
						articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
				//se guarda el art\u00EDculo en el detalle
				//detallePedidoDTO.setArticuloDTO(articuloDTO.clone());
				detallePedidoDTO.setArticuloDTO((ArticuloDTO)SerializationUtils.clone(articuloDTO));
				//se inicializan los estados del sic por los items del canasto
				detallePedidoDTO.getArticuloDTO().setNpAlcanceReceta(estadoActivo);
				detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticuloReceta(estadoActivo);
				detallePedidoDTO.getArticuloDTO().setNpEstadoArticuloSICReceta(estadoActivo);
				
				//se obtiene el c\u00F3digo del canasto vac\u00EDo
				String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
				//se verifica si el art\u00EDculo que se agrega es un canasto de cotizaciones vac\u00EDo
				if(articuloDTO.getId().getCodigoArticulo().equals(codigoCanCotVacio)){
					//cuando este canasto se agrega no se consulta su receta porque es vac\u00EDa
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
					detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(new ArrayList<ArticuloRelacionDTO>());
				}else//si escribe el codigo de un canasto de cotizaciones, indicar que es un canasto de cotizaciones y cargar su receta actual
					if(articuloDTO.getCodigoArticuloOriginal()!= null && articuloDTO.getCodigoArticuloOriginal().equals(codigoCanCotVacio)){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
						//se llama al m\u00E9todo que consulta la receta de cada art\u00EDculo que es un canasto
						cargarRecetaArticulo(request, detallePedidoDTO, null,Boolean.TRUE,null, true);
						//se recalcula el valor total del canasto en algunos casos
						recalcularPrecioReceta(detallePedidoDTO, request);
						
					}else if(articuloDTO.getCodigoArticuloOriginal()!= null && articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
						//se llama al m\u00E9todo que consulta la receta de cada art\u00EDculo que es un canasto
						cargarRecetaArticulo(request, detallePedidoDTO, null,Boolean.TRUE,null, true);
						//se recalcula el valor total del canasto en algunos casos
						recalcularPrecioReceta(detallePedidoDTO, request);
						
					}else{
						//se llama al m\u00E9todo que consulta la receta de cada art\u00EDculo que es un canasto
						cargarRecetaArticulo(request, detallePedidoDTO, null,Boolean.TRUE,null, false);
					}
			}else{
				detallePedidoDTO.setArticuloDTO(articuloDTO);
			}

			LogSISPE.getLog().info("estadoDetallePedidoDTO.getValorTotalEstadoIVA(): {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstadoIVA());
						
		}catch(NullPointerException ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n, NullPointerException",ex);
			errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral",ex.getMessage()));
			return null;
		}
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral",ex.getMessage()));
			return null;
		}
		return detallePedidoDTO;  //se retorna el total del detalle creado
	}
	
	/**
	 * Determina el precio que se debe tomar en cuenta para realizar el c\u00E1lculo del total del detalle para el 
	 * art\u00EDculo que se va a agregar al detalle.
	 * 
	 * @param  formulario				El formulario donde se mostrar\u00E1n los datos
	 * @param  request				La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return detallePedidoDTO		El detalle del pedido que fu\u00E9 creado
	 */
	/**
	 * @param formulario				El formulario donde se mostrar\u00E1n los datos
	 * @param request					La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @param infos						Mostrar mensajes de informacion en la aplicacion
	 * @param errors					Mostrar mensajes de error en la aplicacion
	 * @param warnings					Mostrar mensajes de advertencia en la aplicacion
	 * @param reconstruirDetalles		Reconstruye el detalle
	 * @param verificarSIC				Verificar el stock desde el SIC en recetas y articulos
	 * @return
	 * @throws Exception
	 */
	public static boolean construirDetallesPedidoDesdeVista(CotizarReservarForm formulario, HttpServletRequest request,ActionMessages infos, ActionMessages errors, ActionMessages warnings, boolean reconstruirDetalles,Boolean verificarSIC)throws Exception{
		
		LogSISPE.getLog().info("Construir Detalles Pedido DesdeVista");
		HttpSession session = request.getSession();

		//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
		session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
		
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = ( String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		String estadoInactivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_INACTIVO);
		
		String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		VistaPedidoDTO vistaPedidoDTO = null;
		Boolean consultarDescuentos=Boolean.TRUE;
		List<DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
		List<DetallePedidoDTO> detallePedido1 = new ArrayList <DetallePedidoDTO>();
		Collection<String> codigosArticulos = new ArrayList <String>();
		//colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
		List<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
		int indice = 0;
		List<VistaPedidoDTO> pedidos = null;
		VistaPedidoDTO consultaPedidoDTOAux = new VistaPedidoDTO();
		Collection <VistaPedidoDTO> colVistaPedidoDTOAux = null;
		
		//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
		String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
		session.setAttribute(CotizarReservarAction.CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);
		//Obtener limite de dias para anular pedidos
		SessionManagerSISPE.getObtenerLimiteTiempoValidezReserva(request);
		
		if(reconstruirDetalles){
			VistaPedidoDTO vistaPedidoDTOAux = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			//indice = Integer.parseInt(String.valueOf(session.getAttribute(WebSISPEUtil.INDICE_BUQUEDA_PEDIDO)));
			//reConstruirDetallesPedidoDesdeVista(formulario, request);
			pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			consultaPedidoDTOAux.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaPedidoDTOAux.getId().setCodigoPedido(vistaPedidoDTOAux.getId().getCodigoPedido());
			consultaPedidoDTOAux.getId().setCodigoAreaTrabajo(vistaPedidoDTOAux.getId().getCodigoAreaTrabajo());
			consultaPedidoDTOAux.setEntidadResponsable(vistaPedidoDTOAux.getEntidadResponsable()); //BOD o LOC
			//se verifica si la consulta se debe filtrar solo los pedidos en el estado actual
			consultaPedidoDTOAux.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			//obtiene solamente los registros en el rango establecido
			colVistaPedidoDTOAux = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTOAux);
			Collection<VistaPedidoDTO> vistaPedidoTotal= (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
			for(VistaPedidoDTO vistaDTO : vistaPedidoTotal){
				if(vistaDTO.getId().getCodigoPedido().equals(vistaPedidoDTOAux.getId().getCodigoPedido())){
					vistaPedidoTotal.remove(vistaDTO);
					break;
				}
			}
			vistaPedidoTotal.add((VistaPedidoDTO)CollectionUtils.get(colVistaPedidoDTOAux, 0));
			session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL, vistaPedidoTotal);
			pedidos=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTOAux);
			consultarDescuentos=Boolean.TRUE;
			session.removeAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
			//verificar si debe cargar el pedido en solo lectura
			String soloLectura=(String)request.getParameter("soloLectura");
			if(soloLectura!=null){
				 if(soloLectura.equals("SI")){
					 session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,"CONSOLIDAR"); 
				 }
				 else{
					 session.removeAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
				 }
			}
		}else{
			//se obtiene la colecci\u00F3n de los pedidos 
			pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			if(request.getParameter("indice")!=null){
				if(((String)request.getParameter("indice")).equals("ok")){
					String codigoPedido=(String)request.getParameter("codigoPedido");
					Integer codigoLocal=null;
					if(request.getParameter("codigoLocal")!=null){
						 codigoLocal=Integer.valueOf(request.getParameter("codigoLocal"));
					}
					String entidadResponsable=(String)request.getParameter("entidadResponsable");
					
					PedidoDTO pedidoDTO=(PedidoDTO)session.getAttribute(CotizarReservarAction.PEDIDO_GENERADO);
					//DTO que contiene los pedidos a buscar
			       
					consultaPedidoDTOAux.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			        if(codigoPedido==null || codigoPedido.equals("")){
			        	consultaPedidoDTOAux.getId().setCodigoPedido(pedidoDTO.getId().getCodigoPedido());
			        }
			        else{
			        	consultaPedidoDTOAux.getId().setCodigoPedido(codigoPedido);
			        }
					LogSISPE.getLog().info("N\u00FAmero pedido: {}",consultaPedidoDTOAux.getId().getCodigoPedido());
					if(codigoLocal==null){
						codigoLocal=pedidoDTO.getId().getCodigoAreaTrabajo();
			        }
					//se asigna el codigo del local 
					consultaPedidoDTOAux.getId().setCodigoAreaTrabajo(codigoLocal);
					LogSISPE.getLog().info("Codigo Local: {}",codigoLocal);
					
					if(entidadResponsable==null){
						entidadResponsable=pedidoDTO.getEntidadResponsable();
			        }
					//se asigna la entidad responsable
					consultaPedidoDTOAux.setEntidadResponsable(entidadResponsable); //BOD o LOC
					LogSISPE.getLog().info("Entidad responsable: {}",entidadResponsable);
	   		        //se verifica si la consulta se debe filtrar solo los pedidos en el estado actual
					consultaPedidoDTOAux.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
					//obtiene solamente los registros en el rango establecido
					colVistaPedidoDTOAux = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTOAux);
					 pedidos=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTOAux);
					//se verifica con que precios se guard\u00F3 el pedido
					 VistaPedidoDTO vistaPedidoActualDTO= (VistaPedidoDTO)pedidos.get(0);
					 if(formulario.getCheckCalculosPreciosAfiliado()!= null && formulario.getCheckCalculosPreciosAfiliado().equals(estadoActivo)){
						 vistaPedidoActualDTO.setEstadoPreciosAfiliado(estadoActivo);
					 }
					 else{
						 vistaPedidoActualDTO.setEstadoPreciosAfiliado(estadoInactivo);
					 }
					 consultarDescuentos=Boolean.FALSE;
					 session.removeAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
					 session.removeAttribute(CotizarReservarAction.CAMBIO_PESO);
					 //verificar si debe cargar el pedido en solo lectura
					 String soloLectura=(String)request.getParameter("soloLectura");
					 if(soloLectura!=null){
						 if(soloLectura.equals("SI")){
							 session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,"CONSOLIDAR"); 
						 }
						 else{
							 session.removeAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
						 }
					 }
				}
				else{
					indice = Integer.parseInt(request.getParameter("indice"));
				}
			}else{
				//para el caso de la modificaci\u00F3n de una reservaci\u00F3n
				indice = Integer.parseInt((String)session.getAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR));
				//se elimina la variable
				session.removeAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR);
			}
		}
		//creaci\u00F3n del DTO para almacenar el pedido selecionado
		vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
		
		
		String accionConsolidar=(String)session.getAttribute(WebSISPEUtil.ACCION_CONSOLIDAR); 
		if(accionConsolidar==null){
			//se guarda en sesi\u00F3n el pedido seleccionado
			session.setAttribute("ec.com.smx.sic.sispe.vistaPedidoDTOActual", vistaPedidoDTO);
		}
		//se guarda en sesi\u00F3n el pedido seleccionado
		session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
		session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_AUX, vistaPedidoDTO.clone());
		//se guarda el c\u00F3digo del local
		session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		//se guarda el c\u00F3digo de establecimiento
		session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaPedidoDTO.getCodigoEstablecimiento());
		//numero usado para consolidar en el caso que aun no este consolidado
		formulario.setNumeroAConsolidar(vistaPedidoDTO.getId().getCodigoPedido());
		
		//crear coleccion inicial de listado de empresas consolidadas
		if(consultarDescuentos){
			Collection<VistaPedidoDTO> vistaPedidoCol=new ArrayList<VistaPedidoDTO>();
			vistaPedidoCol.add(vistaPedidoDTO);
			session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS, vistaPedidoCol);
		}
		
		//se verifica si el usuario logeado es de la bodega
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){      
			session.setAttribute(CotizarReservarAction.ES_ENTIDAD_BODEGA,"ok");

			//se cargan los datos del local
			LocalID localID = new LocalID();
			localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			localID.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);

			if(localDTO!=null){
				//se guarda el telefono y el nombre del administrador del local
				session.setAttribute(CotizarReservarAction.TELEFONO_LOCAL, localDTO.getTelefonoLocal());
				session.setAttribute(CotizarReservarAction.ADMINISTRADOR_LOCAL, localDTO.getNombreAdministrador());
			}
		}

		session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE,vistaPedidoDTO.getEntidadResponsable());
		
		//se verifica con que precios se guard\u00F3 el pedido
		if(vistaPedidoDTO.getEstadoPreciosAfiliado()!=null && vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
		}else{
			//se verifica con que precios se guard\u00F3 el pedido
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO) != null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
				vistaPedidoDTO.setEstadoPreciosAfiliado(estadoActivo);
			}else{
				formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
				vistaPedidoDTO.setEstadoPreciosAfiliado(estadoInactivo);
				session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
			}
			
			//formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
			//session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
		}
		
		//se verifica si los calculos se deben realizar con iva o sin iva por el ruc de la empresa
		verificarEmpresaExentaIVA(request, vistaPedidoDTO.getRucEmpresa(), vistaPedidoDTO.getContextoPedido());
		
//		//se carga la configuraci\u00F3n de los descuentos se cometa porque ya se llama al inicio del metodo apenas preciona el el boton recotizar, reservar.
//		cargarConfiguracionDescuentos(request, estadoActivo);
		
		//se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
		VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
		consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
		consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
		consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
		consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
		consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
		consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
		
		detalleVistaPedido = (List<VistaDetallePedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
		EstadoPedidoUtil.mostrarDetallesPedidoPorEstado(vistaPedidoDTO, detalleVistaPedido);
		
		Boolean despacho = validarFechasDespacho(session, detalleVistaPedido);
		
		//si existen despachos y si es la opci\u00F3n modificar reserva
		if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){			
			session.setAttribute(ModificarReservacionAction.MOSTRAR_POPUP_DESPACHO,"ok");
		}
		
		LogSISPE.getLog().info("Detalles Pedido DesdeVista {}",detalleVistaPedido.size());
		String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
		//se crea la colecci\u00F3n de art\u00EDculos para realizar la consulta de los stocks y alcances
		ArrayList <ArticuloDTO> articulos = new ArrayList <ArticuloDTO>();
		//contador para los precios alterados
		int contadorPreciosAlterados = 0;
		
		//variable para sumar cuantos articulos han sido entregados
		long contadorEntrega=0;
		//variable para sumar cuantos articulos han sido despachados
		long contadorDespacho=0;
		
		//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
		WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
		
		//Llave descuento para aplicar el DSCTo de pavos automatico
//		String []llaveDsctos=null;
		//se itera la vistaDetallePedido para crear un DetallePedidoDTO
		
		//codigo de autorizacion, Autorizacioneshijas --map para consultar solo una vez las autorizaciones hijas
		Map<Long, HashSet<AutorizacionDTO>> mapAutorizacionesHijas = new HashMap<Long, HashSet<AutorizacionDTO>>();
			
		for (int i=0;i<detalleVistaPedido.size();i++){
			contadorEntrega=0;
			contadorDespacho=0;
			boolean precioAlterado = false; //variable para el control de cambios en los precios
			VistaDetallePedidoDTO vistaDetallePedidoDTO = detalleVistaPedido.get(i);
			DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
			//se crea el DetallePedidoDTO para poder almacenarlo en la reservaci\u00F3n
			detallePedidoDTO.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			detallePedidoDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			detallePedidoDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			detallePedidoDTO.getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			detallePedidoDTO.setArticuloDTO(vistaDetallePedidoDTO.getArticuloDTO());
			detallePedidoDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
			
			//creaci\u00F3n del estado del detalle del pedido
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoEstado(vistaDetallePedidoDTO.getId().getCodigoEstado());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
			//este campo se asigna para las entregas
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
			
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadAnterior(vistaDetallePedidoDTO.getCantidadEstado()); //cambios oscar--------
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadParcialEstado(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadParcialEstado(vistaDetallePedidoDTO.getCantidadParcialEstado()==null?0L:vistaDetallePedidoDTO.getCantidadParcialEstado());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setReservarBodegaSIC(estadoInactivo);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstadoReservado(0D);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEspecialReservado(estadoInactivo);
			
			//aplica precio caja/mayorista
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioCaja(vistaDetallePedidoDTO.getAplicaPrecioCaja());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioMayorista(vistaDetallePedidoDTO.getAplicaPrecioMayorista());
			
			//autorizacion relacionada de stock
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAutorizacionRelacionadaStock(vistaDetallePedidoDTO.getAutorizacionRelacionadaStock());
			
			//stock obsoleto
			if(vistaDetallePedidoDTO.getStoLocArtObs()!=null){
				detallePedidoDTO.getEstadoDetallePedidoDTO().setStoLocArtObs(vistaDetallePedidoDTO.getStoLocArtObs()!=0 ?vistaDetallePedidoDTO.getStoLocArtObs():0);
			}
			
			//canasta especial
			if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
				detallePedidoDTO.getArticuloDTO().setNpNuevoCodigoClasificacion(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion());		
			}
			
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
			//cambios para que no se pierdan los descuentos de pedidos consolidados cuando registra pesos finales
			//para cuando va a registrar pesos finales
			if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null &&
					request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
			//solo si el art\u00EDculo es de peso variable
	          if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
	          	if(vistaDetallePedidoDTO.getPesoRegistradoLocal() == null)
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoArticuloEstado());
	          	else{
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());
	          	}
	          }
			}else{
	      	//se inicializa el peso aproximado
				if(vistaDetallePedidoDTO.getPesoRegistradoLocal()!=null && vistaDetallePedidoDTO.getPesoRegistradoLocal().doubleValue()!=0){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());
				}else{
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(null);
				}
			}
			//estos campos pueden variar al final de este m\u00E9todo, en el caso que se deban aplicar descuentos 
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoDescuento(vistaDetallePedidoDTO.getValorTotalEstadoDescuento());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(vistaDetallePedidoDTO.getValorTotalEstadoNeto());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(vistaDetallePedidoDTO.getValorTotalEstadoNetoIVA());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(vistaDetallePedidoDTO.getValorPrevioVenta());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(vistaDetallePedidoDTO.getValorFinalEstadoDescuento());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(vistaDetallePedidoDTO.getValorUnitarioPOS());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(vistaDetallePedidoDTO.getEstadoCanCotVacio());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(
					vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo() ? 
							vistaDetallePedidoDTO.getArticuloDTO().getCantidadMayoreo():0);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(vistaDetallePedidoDTO.getAplicaIVA());
			
			//Se consulta si tiene autorizaciones
			
			DetalleEstadoPedidoAutorizacionDTO detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
			EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
			estadoPedidoAutorizacionDTO.setAutorizacionDTO(new ec.com.smx.autorizaciones.dto.AutorizacionDTO());
			detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
			detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
			detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo());
			detalleAutorizaciones.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			detalleAutorizaciones.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
			detalleAutorizaciones.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			detalleAutorizaciones.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesCol = SISPEFactory.getDataService().findObjects(detalleAutorizaciones);
			
			boolean agregarAutorizacionPrincipal = true;
			
			if(CollectionUtils.isEmpty(autorizacionesCol)){
				
				agregarAutorizacionPrincipal = false;
					
				//si no encuentra ninguna autorizacion se busca si tiene autorizaciones relacionadas de stock
				detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
				detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
				detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getId().getCodigoAreaTrabajo());
				detalleAutorizaciones.getId().setCodigoArticulo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoArticulo());
				detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());
				detalleAutorizaciones.getId().setCodigoEstado(null);
				detalleAutorizaciones.getId().setCodigoEstadoPedidoAutorizacion(detallePedidoDTO.getEstadoDetallePedidoDTO().getAutorizacionRelacionadaStock());
				detalleAutorizaciones.getId().setCodigoPedido(detallePedidoDTO.getId().getCodigoPedido());
				detalleAutorizaciones.getId().setSecuencialEstadoPedido(null);
				detalleAutorizaciones.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				autorizacionesCol = SISPEFactory.getDataService().findObjects(detalleAutorizaciones);
			}
			
			if(CollectionUtils.isNotEmpty(autorizacionesCol)){
				
				//coleccion con autorizaciones solo de stock
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesStockCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				
				for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : autorizacionesCol){
					if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null && detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion() != null){
						detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpTipoAutorizacion(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion());
						//para el caso de descuento variable
						if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
							
							if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.adicional"))){
								detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpNombreDepartamento(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE \\("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.adicional")+"\\)")[1]);
							}else{
								if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1].contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"))){
									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpNombreDepartamento(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1].split("\\("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia")+"\\)")[0]);
								}else{
									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpNombreDepartamento(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1]);
								}
							}
						}
						else if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
							
							HashSet<AutorizacionDTO> autorizacionesHijas = null;
							Long key = detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion();
							
							//si no contiene la autorizacion se consultan las autorizaciones hijas
							if(!mapAutorizacionesHijas.containsKey(key)){
								
								//se consuntan las autorizaciones hijas
								AutorizacionDTO autorizacionDTO = detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO();
								AutorizacionDTO autorizacionHijaConsulta = new  AutorizacionDTO();
								autorizacionHijaConsulta.getId().setCodigoCompania(autorizacionDTO.getId().getCodigoCompania());
								autorizacionHijaConsulta.getId().setCodigoSistema(autorizacionDTO.getId().getCodigoSistema());
								autorizacionHijaConsulta.setCodigoAutorizacionPadre(autorizacionDTO.getId().getCodigoAutorizacion());
								
								//se consultan los valores componente
								AutorizacionValorComponenteDTO autorizacionValorComponente = new AutorizacionValorComponenteDTO();
								DynamicComponentValueDto valorComponenteDto=new DynamicComponentValueDto();
								valorComponenteDto.setDynamicComponentDto(new DynamicComponentDto());
								autorizacionValorComponente.setDynamicComponentValueDTO(valorComponenteDto);
								autorizacionValorComponente.getId().setCodigoCompania(autorizacionDTO.getId().getCodigoCompania());
								autorizacionValorComponente.getId().setCodigoSistema(autorizacionDTO.getId().getCodigoSistema());
								autorizacionHijaConsulta.setAutorizacionValorComponenteDTO(new ArrayList<AutorizacionValorComponenteDTO>());
								autorizacionHijaConsulta.getAutorizacionValorComponenteDTO().add(autorizacionValorComponente);
								
								Collection<AutorizacionDTO> autorizacionesHijasCol = SISPEFactory.getDataService().findObjects(autorizacionHijaConsulta);
								LogSISPE.getLog().info("autorizaciones hijas encontradas "+autorizacionesHijasCol.size());
								
								if(CollectionUtils.isNotEmpty(autorizacionesHijasCol)){
									autorizacionesHijas = new HashSet<AutorizacionDTO>();
									
									for(AutorizacionDTO autorizacionHijaActual : autorizacionesHijasCol){
										
										//se consultan los valores componentes
										if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getAutorizacionValorComponenteDTO())){
											
											//Create Value transient helper collection
											List<DynamicComponentValueDto> dynamicComponentValues =  new ArrayList<DynamicComponentValueDto>();
											for (AutorizacionValorComponenteDTO autorizacionValorComponenteDTO : autorizacionHijaActual.getAutorizacionValorComponenteDTO()) {
												dynamicComponentValues.add(autorizacionValorComponenteDTO.getDynamicComponentValueDTO());			
											}
											autorizacionHijaActual.setValoresComponente(dynamicComponentValues);
										}
										autorizacionesHijas.add(autorizacionHijaActual);
									}
								}
								mapAutorizacionesHijas.put(key, autorizacionesHijas);
							}else{
								//se toman las autorizaciones del map
								autorizacionesHijas = mapAutorizacionesHijas.get(key);
							}
							detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setAutorizacionesHijas(autorizacionesHijas);
							autorizacionesStockCol.add(detalleAutorizacion);
						}
					}
				}
				
				if(agregarAutorizacionPrincipal){
					LogSISPE.getLog().info("Tiene {} autorizacion(es) el articulo {}", autorizacionesCol.size(),vistaDetallePedidoDTO.getId().getCodigoArticulo());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(autorizacionesCol);
				}
				detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionAnteriorCol(autorizacionesStockCol);
			}
			
			//se busca y sube a session autorizaciones del tipo TIPO_AUTORIZACION_ELABORAR_CANASTOS			
			if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS.longValue())){
				//se verifica si tiene autorizacion del tipo BODEGA ELABORA CANASTOS
				AutorizacionEstadoPedidoDTO autorizacionEstadoPedidoDTO = AutorizacionesUtil.obtenerEstadoPedidoAutorizacionDTO(vistaDetallePedidoDTO, ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS);
				
				//se agrega la autorizacion a la coleccion
				if(autorizacionEstadoPedidoDTO != null){
					
					Collection<AutorizacionDTO> autorizaciones = (Collection<AutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
					
					if(CollectionUtils.isEmpty(autorizaciones)){
						autorizaciones = new ArrayList<AutorizacionDTO>();
					}
					
					autorizaciones.add(autorizacionEstadoPedidoDTO.getAutorizacionDTO());							
					request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL, autorizaciones);
				}
			}
		
			//se llama a la funci\u00F3n que realiza el control y asignaci\u00F3n de precios
			precioAlterado = controlPrecios(vistaDetallePedidoDTO, detallePedidoDTO.getEstadoDetallePedidoDTO(), request);
			
//			DescuentosUtil.validarSiAplicaDescuentoCajasMayorista(detallePedidoDTO, request);
			
			//se validan los datos de los articulos
			int cantidadErrores = errors.size();
			UtilesSISPE.validarArticuloDetallePedido(detallePedidoDTO, errors);
			if(cantidadErrores != errors.size()){
				throw new SISPEException("Error al obtener el art\u00EDculo, existen problemas con la informaci\u00F3n registrada");
			}
			
			//lamada al m\u00E9todo que determina los totales por detalle      
			calcularValoresDetalle(detallePedidoDTO, request, false,false);
			
			if(session.getAttribute(PRECIOS_ALTERADOS) != null 
					&& session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") != null
					&& session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios").equals(ConstantesGenerales.ESTADO_NO)){
				asignarPreciosCajaMayoristaOriginales(request, detallePedidoDTO, vistaDetallePedidoDTO, null);
			}
			
			//itero las entregas para cargar los valores no persistentes
			for(EntregaDetallePedidoDTO entregaDetalle:vistaDetallePedidoDTO.getEntregaDetallePedidoCol()){
				LogSISPE.getLog().info("cantidad entrega: {}", entregaDetalle.getCantidadEntrega());
				LogSISPE.getLog().info("cantidad despacho: {}", entregaDetalle.getCantidadDespacho());
				contadorEntrega=contadorEntrega+entregaDetalle.getCantidadEntrega().longValue();
				LogSISPE.getLog().info("contador entrega: {}", contadorEntrega);
				entregaDetalle.setNpCantidadEntrega(entregaDetalle.getCantidadEntrega());
				contadorDespacho=contadorDespacho+entregaDetalle.getCantidadDespacho().longValue();
				entregaDetalle.setNpCantidadDespacho(entregaDetalle.getCantidadDespacho());
				LogSISPE.getLog().info("contador despacho: {}", contadorDespacho);
				
				entregaDetalle.setNpCantidadEntregaFueModificada(null);//verificar esta variable cuando se haga la parte de modificar reservaciones
				entregaDetalle.getEntregaPedidoDTO().setNpContadorBeneficiario(0L);
				entregaDetalle.getEntregaPedidoDTO().setCodigoAutorizacion(null);
				
			}
				
			
			LogSISPE.getLog().info("----contador entrega---- {}", contadorEntrega);
			LogSISPE.getLog().info("----contador despacho---- {}", contadorDespacho);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-contadorEntrega));
			detallePedidoDTO.setNpContadorDespacho(Long.valueOf(contadorDespacho));
			detallePedidoDTO.setNpContadorEntrega(Long.valueOf(contadorEntrega));
			//se asignan las entregas por cada detalle
			detallePedidoDTO.setEntregaDetallePedidoCol(vistaDetallePedidoDTO.getEntregaDetallePedidoCol());//asignamos las entregas

			detallePedidoDTO.setNpCodigoClasificacion(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion());
			detallePedidoDTO.setNpCodigoClasificacionArticulo(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion()
					+ caracterToken
					+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
			detallePedidoDTO.setNpCodigoTipoDescuento(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento());
			detallePedidoDTO.setNpCodigoTipoDescuentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento()
					+ caracterToken
					+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()
					+ caracterToken
					+ i);
			//campos para registrar el pedido por departamento
			detallePedidoDTO.setNpDepartamento(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento());
			detallePedidoDTO.setNpDepartamentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento()
					+ caracterToken
					+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());

			//se verifica si el art\u00EDculo es un canasto
//			if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) || 
//					vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
			if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
					vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
				
				//Si el canasto de cotizaciones por validaciones cambio su estado EstadoCanCotVacio 1 a 0 cuando abre el pedido se vuelve a verificar y asignar correctamente este valor.
				String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
				if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codigoCanCotVacio)){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
				}else if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
				}				
				//se llama al m\u00E9todo que carga la receta de cada art\u00EDculo que es un canasto
				cargarRecetaArticulo(request, detallePedidoDTO, vistaPedidoDTO.getId().getSecuencialEstadoPedido(),verificarSIC,null, false);
				
				if(detallePedidoDTO.getNpRecetaActualEsDiferenteAHistorial()){
					precioAlterado = true;
				}
			}
			
			//llamada al m\u00E9todo que construye la consulta de los art\u00EDculos
			WebSISPEUtil.construirConsultaArticulos(request,detallePedidoDTO.getArticuloDTO(), estadoInactivo, estadoInactivo, accionActual);
			//se almacenan los detalles y los art\u00EDculos
			detallePedidoDTO.setFechaRegistro(vistaDetallePedidoDTO.getFechaRegistro());
			detallePedidoDTO.setUsuarioRegistro(vistaDetallePedidoDTO.getUsuarioRegistro());
			detallePedido.add(detallePedidoDTO);
			detallePedido1.add((DetallePedidoDTO)SerializationUtils.clone(detallePedidoDTO));
			articulos.add(detallePedidoDTO.getArticuloDTO());
			
			//se almacenan los codigos de art\u00EDculos del detalle
			codigosArticulos.add(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			//codigosArticulos.add(vistaDetallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras());

			//si se altero el precio de un art\u00EDculo
			if(precioAlterado)
				contadorPreciosAlterados++;
			
			LogSISPE.getLog().info("estadoArticuloSIC *: {}, estadoArticuloSICReceta *: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());
			//llaveDsctos=iniciarDescuentoPavos(request, detallePedidoDTO.getArticuloDTO());
		}
		
		//se gestiona el alcance desde el MAX cuando ingresa por RESERVACION
//		gestionarAlcanceMax(articulos, request);
		
		//Consultar el stock del articulo, siempre y cuando se requiera
		if(verificarSIC){
			//llamada al m\u00E9todo que obtiene el stock y alcance de los art\u00EDculos
			SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);
		}
		//se recorre nuevamente el detalle del pedido, ahora para verificar su stock
		for(int i=0;i<detallePedido.size();i++){
			DetallePedidoDTO detallePedidoDTO = detallePedido.get(i);
			if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null 
					&& detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
				//se asigna el estado correspondiente al estado del stock
				detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);
				//stock obsoleto
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs() != null){
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs()!=0 && detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals("O")){
						detallePedidoDTO.getArticuloDTO().setNpStockArticulo(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());				
					}
				}
			}
			LogSISPE.getLog().info("estadoArticuloSIC **: {}, estadoArticuloSICReceta **: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());
		}

		//boolean reprocesarDescuentos = false;
		//se verifica tambi\u00E9n el estado del pago en caso de la modificaci\u00F3n de una reservaci\u00F3n
		//si esta pagado totalmente o liquidado no se debe realizar ning\u00FAn recalculo adicional
		if(contadorPreciosAlterados > 0){
			
			if(vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))
					|| vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))){
				session.setAttribute(PRECIOS_ALTERADOS_RESERVAS_PTO_LQD, "SI");
				
			}else {
				//reprocesarDescuentos = true;
				session.setAttribute(PRECIOS_ALTERADOS, "SI");
			}
		}	
		
		//Obtener los pedidos consolidados para valver a calcular y ver si se procesa o no los descuentos
		Collection<DetallePedidoDTO> detallePedidoConsolidado = null;
		if(vistaPedidoDTO.getCodigoConsolidado()!=null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
			Collection<VistaPedidoDTO> colVistaPedidoDTO= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
			if(consultarDescuentos==Boolean.TRUE){
				VistaPedidoDTO consultaPedidoDTO = new VistaPedidoDTO();
				consultaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaPedidoDTO.setCodigoConsolidado(vistaPedidoDTO.getCodigoConsolidado()); 
				consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				consultaPedidoDTO.setDescuentosEstadosPedidos(new ArrayList<DescuentoEstadoPedidoDTO>()); 
				colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
				LogSISPE.getLog().info("TAMANO DE PEDIDOS CONSOLIDADOS DESDE BUSQUEDAS: {}",colVistaPedidoDTO.size());
			}
			
			//obtener todos los pedidos consolidados
			session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,colVistaPedidoDTO);
			
			//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
			WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
			
			detallePedidoConsolidado = ConsolidarAction.agregarPedidosConsolidados(formulario, request, session, detallePedido, vistaPedidoDTO, colVistaPedidoDTO, errors);
			
			Collection<VistaPedidoDTO> vistaPedidoTotal=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTO.size()+1);
			vistaPedidoTotal.addAll(colVistaPedidoDTO);
			session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL,vistaPedidoTotal);
			formulario.setDatosConsolidadosTotal(vistaPedidoTotal);
		}
		else{
			//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
			WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
			
			if(formulario.getNumeroAConsolidar()!=null){
				Collection<VistaPedidoDTO> vistaPedidoTotal= (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
				if(vistaPedidoTotal!=null && vistaPedidoTotal.size()>0){
					vistaPedidoDTO.setCodigoConsolidado(formulario.getNumeroAConsolidar());
					detallePedidoConsolidado = ConsolidarAction.agregarPedidosConsolidados(formulario, request, session, detallePedido, vistaPedidoDTO, vistaPedidoTotal, errors);
				}
				else{
					formulario.setNumeroPedidoConsolidado(null);
					formulario.setDatosConsolidados(null);
				}
			}
			else{
				formulario.setNumeroPedidoConsolidado(null);
				formulario.setDatosConsolidados(null);
			}
		}
		/**--------**/
		//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
		session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
	
		//se recalcula los precios del pedido actual una vez que se carg\u00F3 los pedidos consolidados
		//lamada al m\u00E9todo que determina los totales por detalle     
		for(DetallePedidoDTO detallePedidoDTORecorrido : detallePedido){
			
			if(detallePedidoDTORecorrido.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
				calcularValoresDetalle(detallePedidoDTORecorrido, request, false,false);
			}
		}
		
		//se elimina de sesion el parametro
		session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		
		//llamada a la funcion que realiza el procesamiento de los descuentos
		DescuentoEstadoPedidoDTO descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
		//Aplicar descuentos a los pedidos consolidados
		if(detallePedidoConsolidado!=null){
			//setear los nuevos valores en el caso de que existio un cambio en el peso de los pavos
			/*for (int i=0;i<detalleVistaPedido.size();i++){
				VistaDetallePedidoDTO vistaDetallePedidoDTO = detalleVistaPedido.get(i);
				//se inicializa el peso aproximado
				for(DetallePedidoDTO detPed:detallePedido){
					//se inicializa el peso aproximado
					if(vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido()) &&
							vistaDetallePedidoDTO.getId().getCodigoArticulo().equals(detPed.getId().getCodigoArticulo())){
						if(vistaDetallePedidoDTO.getPesoRegistradoLocal()!=null && vistaDetallePedidoDTO.getPesoRegistradoLocal().doubleValue()!=0){
							detPed.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
						}else{
							detPed.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
						}
					}
				}
			}*/
//			Collection<DetallePedidoDTO> detallePedidoGeneral=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
			Collection<DetallePedidoDTO> detallePedidoGeneral = (Collection<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS);
//			Collection<DetallePedidoDTO> detallePedidoProcesar=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
//			for(DetallePedidoDTO detPedDTO : detallePedidoConsolidado){
//				
//				detallePedidoProcesar.add((DetallePedidoDTO)SerializationUtils.clone(detPedDTO));
//			}
//			
//			detallePedidoGeneral.add((DetallePedidoDTO)detallePedidoProcesar.iterator().next());
//			int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
			Boolean existeArt = Boolean.FALSE;
//			int i=0;
//			for (DetallePedidoDTO detallePedidoDTO:detallePedidoProcesar){
//				Long cantidadTotalConsolidada=0L;
//				Double valorTotalConsolidada=0D;
//				existeArt= Boolean.FALSE;
//				for (DetallePedidoDTO detallePedidoConsolidadoDTO:detallePedidoGeneral){
//					if(detallePedidoDTO!= null && detallePedidoConsolidadoDTO!=null){
//						
//						if(i>0 && detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
//							cantidadTotalConsolidada=(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
//							valorTotalConsolidada=Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
//							valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
//									detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
//							cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
//							
//							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(valorTotalConsolidada);
//							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalConsolidada);
//							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
//							existeArt= Boolean.TRUE;
//							break;
//						}
//					}
//				}
//				
//				if(i>0 && existeArt== Boolean.FALSE){
//					detallePedidoGeneral.add(detallePedidoDTO);
//				}
//				i++;
//			}
			
			for(DetallePedidoDTO detPedGen:detallePedidoGeneral){
				existeArt= Boolean.FALSE;
				for(DetallePedidoDTO detPed:detallePedido){
					if(detPedGen.getId().getCodigoArticulo().equals(detPed.getId().getCodigoArticulo())){
						existeArt= Boolean.TRUE;
					}
				}
				if(!existeArt){
					boolean esActivoPrecioMayorista = false;
					if(detPedGen.getArticuloDTO().getHabilitadoPrecioMayoreo()){
						esActivoPrecioMayorista = true;
					}
					if(!esActivoPrecioMayorista && detPedGen.getArticuloDTO().getHabilitadoPrecioCaja() && detPedGen.getArticuloDTO().getPrecioCaja() > 0){
						if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null){
							detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado());
							detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorTotalEstado());
						}else{
							detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
							detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
						}
					}else{
						detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
						detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
					}
					detPedGen.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
					detPedGen.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
					detallePedido.add(detPedGen);
				}
			}
			
			LogSISPE.getLog().info("AplicarDescuentosConsolidados");
			//se inicializan las sumatorias
			double valorDescuentoTotal = 0;
			double porcentajeTotalDescuento = 0;
			//obtener llaves de descuentos y valores de descuentos variables 
			obtenerLLavesDescuentos(request, session, false);
			
			Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
			Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
			
//			if(vistaPedidoDTO.getCodigoConsolidado()!=null && consultarDescuentos==Boolean.TRUE){
//				//llamada a la funcion que realiza el procesamiento de los descuentos
//				descuentoEstadoPedidoTotalDTO = procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
//				llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
//				descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
//			}
			
//			if(descuentoEstadoPedidoTotalDTO.getDescuentoDTO()==null){
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
				if(llaveDescuentoCol!= null && llaveDescuentoCol.isEmpty()){
					llaveDescuentoCol= null;
				}
				if(descuentoVariableCol!= null && descuentoVariableCol.isEmpty()){
					descuentoVariableCol= null;
				}
				
				if(session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") != null && ((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios")).equals("SI")){
					//Actualizar los precios de los detalles consolidados a procesar
					ConsolidarAction.actualizarDetallePedidosConsolidados(request, 
						estadoActivo, estadoInactivo,vistaPedidoDTO.getRucEmpresa(),vistaPedidoDTO.getTipoDocumentoCliente(),detallePedido);
					//lamada al m\u00E9todo que determina los totales por detalle     
					
					for(DetallePedidoDTO detallePedidoDTORecorrido : detallePedido){
						 
						if(detallePedidoDTORecorrido.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
							CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTORecorrido, request, false,false);
						}
						else{
							if(detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
								//recorre cada item de la canasta
								for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol())){
									recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0L);
									recetaArticuloDTO.setValorPrevioEstadoDescuento(0D);
								}
							}else{
								//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
								detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
								detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
							}
						}
					}
					
				}
				
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
				if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
					if(CollectionUtils.isEmpty(llaveDescuentoCol))
						llaveDescuentoCol = new ArrayList<String>();
					for(DescuentoEstadoPedidoDTO dsctoVariable : colDescuentoEstadoPedidoDTOVariables){
						if(dsctoVariable.getLlaveDescuento() != null && !dsctoVariable.getLlaveDescuento().equals("") 
								&& !llaveDescuentoCol.contains(dsctoVariable.getLlaveDescuento())){
							llaveDescuentoCol.add(dsctoVariable.getLlaveDescuento());
						}
					}
				}
				
				//cuando no ingresa por la opcion de consolidacion, debe cargarse la coleccion de detalles consolidados con autorizaciones
				if(accionActual != null && !accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
					CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, detallePedido);
				}
				//colDescuentoEstadoPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido, llaveDescuentoCol, descuentoVariableCol);
				colDescuentoEstadoPedidoDTO = procesarDescuentosPorTipo(request, detallePedido, llaveDescuentoCol);
				
				//si es pedido consolidado se aplica los valores del descuento variable a las autorizaciones
				if(ConsolidarAction.esPedidoConsolidado(request)){
					AutorizacionesUtil.aplicarEstadoDescuentoVariableAutorizacionesPedido(request, detallePedido);
					
					//si viene desde la recotizacion se sube a sesion los DETALLES_CONSOLIDADOS_ACTUALES con autorizaciones
					if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null 
							&& (request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))
							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))
							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")))){
						obtenerDetallesAutorizacionesActuales(request, null);
					}
					
//					//se asigna la configuracion de las autorizaciones a los detalles
//					asignarAutorizacionesDetallesConsolidados(request, detallePedido);
				}
				//eliminar los detalles que no corresponden al pedido
				List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
				for(DetallePedidoDTO detPed:detallePedido){
					if(!vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
						detallesEliminar.add(detPed);
					}
				}
				detallePedido.removeAll(detallesEliminar);
				if(colDescuentoEstadoPedidoDTO!=null && !colDescuentoEstadoPedidoDTO.isEmpty()){
					LogSISPE.getLog().info("descuentos: {}",colDescuentoEstadoPedidoDTO.size());
					//si se alteraron los precios se itera la colecci\u00F3n de descuentos para calcular el total de descuentos
					for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : colDescuentoEstadoPedidoDTO){
						
						valorDescuentoTotal = valorDescuentoTotal + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
						porcentajeTotalDescuento = porcentajeTotalDescuento + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
					}
					LogSISPE.getLog().info("porcentajeTotalDescuento: {}",porcentajeTotalDescuento);
				}
				//se inicializa el objeto resultado
				descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
				descuentoEstadoPedidoTotalDTO.setPorcentajeDescuento(Double.valueOf(porcentajeTotalDescuento));
				descuentoEstadoPedidoTotalDTO.setValorDescuento(Double.valueOf(valorDescuentoTotal));
				//se almacena el descuento total y el descuento para los art\u00EDculos con IVA
				session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(valorDescuentoTotal));
				//se guarda en sesi\u00F3n el porcentaje total de los descuentos
				session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(porcentajeTotalDescuento));
				session.setAttribute(CotizarReservarAction.COL_DESCUENTOS,colDescuentoEstadoPedidoDTO);
//			}
//			//borrar pedidos consolidados desde sesion
//			session.removeAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
			//aplicarDescuentosConsolidadosV2(request, session,	detallePedido,descuentoVariableCol, llaveDescuentoCol);
				
		}else{
//			//llamada a la funcion que realiza el procesamiento de los descuentos
//			if(session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") != null && ((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios")).equals("SI")){
//				//Actualizar los precios de los detalles consolidados a procesar
//				actualizarDetallePedidosConsolidados(request, 
//					estadoActivo, estadoInactivo,vistaPedidoDTO.getRucEmpresa(),vistaPedidoDTO.getTipoDocumentoCliente(),detallePedido);
//			}
			//Tiene precios alterados, primero actualiza los precios despues deben procesarse los descuentos
			if(session.getAttribute(PRECIOS_ALTERADOS) == null){	
				descuentoEstadoPedidoTotalDTO = procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
			}
			else {
//				//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
//				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request);		
				descuentoEstadoPedidoTotalDTO = procesarDescuentos(vistaPedidoDTO, detallePedido, false, request);
				//se verifica la existencia de una colecci\u00F3n de descuentos
				if(vistaPedidoDTO.getDescuentosEstadosPedidos()!=null && !vistaPedidoDTO.getDescuentosEstadosPedidos().isEmpty()){
					
					Collection<DescuentoEstadoPedidoDTO> descuentos = vistaPedidoDTO.getDescuentosEstadosPedidos();												
					Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
					
					for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentos){
						
						//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
						if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null)
							llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());						
					}
					
					if(!llavesDescuentosAplicados.isEmpty()){
						
						//se generan las llaves para setearlas en opDescuentos del formulario
						Collection<TipoDescuentoDTO> tipoDescuento = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);					
						String[] opDescuentos = new String[llavesDescuentosAplicados.size()];
						int posVector = 0;
						int posDecuento = 0;					
						for(TipoDescuentoDTO tipoDctoActual : tipoDescuento){
							for(String llaveActual : llavesDescuentosAplicados){
								String nuevaLlave = llaveActual;
								String codigoTipoDescuento = llaveActual.split(SEPARADOR_TOKEN)[1].split("CTD")[1];
								if(tipoDctoActual.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento)){
									nuevaLlave =  nuevaLlave.replace(llaveActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, ""+posDecuento+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO);
									opDescuentos[posVector] = nuevaLlave;
									posVector++;
								}
							}
							posDecuento++;
						}
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
						
						//sube a sesion variables necesarias para procesar descuentos
						CotizarReservarAction.buscarDetallesConDctoAutomaticos((ArrayList<DetallePedidoDTO>) detallePedido,  request);		
					}	
				}
				
				//----------------------------------->
			}	
		}
//		//respaldar nuevamente la coleccion detallePedido1 que es donde se almacena el pedido original ya que se aplicaron descuentos
//		detallePedido1= new ArrayList<DetallePedidoDTO>(detallePedido.size());
//		for(DetallePedidoDTO detPed:detallePedido){
//			detallePedido1.add((DetallePedidoDTO)SerializationUtils.clone(detPed));
//		}
		
		//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
		establecerDescuentosFormulario(request, formulario);
		
		LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
		
		//obtengo el codigoTipDesMax-navidad desde un parametro
		CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
		
		//validacion check pago en efectivo
		if(vistaPedidoDTO.getPagoEfectivo() != null && vistaPedidoDTO.getPagoEfectivo().equals(estadoActivo)){
			//Activa el check Pago en Efectivo
			formulario.setCheckPagoEfectivo(estadoActivo);
			//Presenta el tipo de descuento maxi-navidad de la seccion de descuentos
			session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
		}else if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null){
			formulario.setCheckPagoEfectivo(estadoActivo);
			session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
		}
		else if(vistaPedidoDTO.getPagoEfectivo() != null && vistaPedidoDTO.getPagoEfectivo().equals(estadoInactivo) && session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) == null){
			formulario.setCheckPagoEfectivo(null);
			session.removeAttribute(CHECK_PAGO_EFECTIVO);
		}else{
			formulario.setCheckPagoEfectivo(null);
			session.removeAttribute(CHECK_PAGO_EFECTIVO);
		}

		//se limpia el valor del abono
		formulario.setValorAbono(null);
		//se asigna el contexto del pedido al formulario
		if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
			formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
		}else{
			formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
		}

		formulario.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
		if(vistaPedidoDTO.getNumBonNavEmp()!=null){
			formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp().toString());
		}

		formulario.setOpTipoEspeciales("0");
		formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
		formulario.setOpAutorizacion(estadoInactivo);
		
		/*------------------- se asignan los atributos necesarios al formulario --------------------*/
		if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
				|| vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
			if(vistaPedidoDTO.getContactoEmpresa().startsWith(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
				formulario.setNumeroDocumento(vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
				session.setAttribute(ContactoUtil.RUC_PERSONA, vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
				formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
			}else{
				formulario.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
			}
		}
		else 
			if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) ||
				vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
			
			request.getSession().setAttribute(ContactoUtil.LOC_GUARDADA, vistaPedidoDTO.getCodigoLocalizacion());
			formulario.setNumeroDocumento(vistaPedidoDTO.getRucEmpresa());
		}

		//se asigna el local responsable, adicionalmente se agrega 
		formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()
				+ " " + caracterToken + " " + vistaPedidoDTO.getNombreLocal());
		LogSISPE.getLog().info("LOCAL RESPONSABLE OBTENIDO: {}",formulario.getLocalResponsable());

		//se inicializa el costo del flete
    //se asigna el costo del flete
		if(vistaPedidoDTO.getValorCostoEntregaPedido()!=null){
			formulario.setCostoFlete(vistaPedidoDTO.getValorCostoEntregaPedido());
		}else{
			formulario.setCostoFlete(0D);
		}
		formulario.setDescuentoTotal(descuentoEstadoPedidoTotalDTO.getValorDescuento());
		
		session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO_VALIDAR_NUMBONOS, detallePedido);
		session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
		
		//se recalculan los valores del pedido completamente
		calcularValoresFinalesPedido(request, detallePedido, formulario);
		
//		session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
		
//		//LLAMAR A LA FUNCION PARA APLICAR LOS DESCUENTOS DE PAVOS AUTOMATICAMENTE
//		if(llaveDsctos!= null && llaveDsctos.length>0){
//			formulario.actualizarDescuentos(request, errors);
//		}
		
		session.setAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL,detallePedido1);
		
		session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS,codigosArticulos);
		//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos 
//		session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
//		session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		//subClasificacion para articulos tipo canastos 
  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
		String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
		session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA, valorTipoArticuloCanastas);
		
		//subClasificacion para articulos tipo despensas 
  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA, valorTipoArticuloDespensas);
		//esta variable indica que la reservaci\u00F3n est\u00E1 en proceso
		session.setAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO,"ok");
		//indica que la b\u00FAsqueda es en la pantalla principal
		session.setAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL,"ok");

		//---------------- se cargan todos los especiales -------------------
		//creaci\u00F3n del DTO para el art\u00EDculo
		EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
		especialDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
		especialDTO.setNpCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		especialDTO.setEstadoEspecial(estadoActivo);
		especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		especialDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
		
		ArrayList<EspecialDTO> especiales = (ArrayList<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloEspecial(especialDTO);
		CotizarReservarAction.eliminarDetallesConProblemas(especiales);
		session.setAttribute(CotizarReservarAction.COL_TIPO_ESPECIALES,especiales);
		if(especiales!=null && !especiales.isEmpty())
			session.setAttribute(CotizarReservarAction.COL_ART_ESPECIALES,((EspecialDTO)especiales.get(0)).getArticulos());
		
		//-------------------------------------------------------------------
		//se inicializa la sub-p\u00E1gina que se mostrar\u00E1
		session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");

		//se inicializan las variables para el control de la acci\u00F3n CotizarReservarAction
		session.setAttribute(CotizarReservarAction.ACCION_ANTERIOR, "");
		session.setAttribute(CotizarReservarAction.LOCAL_ANTERIOR, "");

		//se desactiva el men\u00FA de opciones
		MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);

		//se llama al m\u00E9todo que verifica la posibilidad que tiene el establecimiento
		//para cambiar el precio
		verificarFormatoNegocioParaCambioPrecios(vistaPedidoDTO.getCodigoEstablecimiento(), request);
		
//		//ver si es un local tipo aki para habilitar el check de precios de afiliado
//		verificarFormatoNegocioPreciosAfiliado(vistaPedidoDTO.getCodigoEstablecimiento(), request);

		//se carga la configuraci\u00F3n inicial de las entregas si es necesario
		//eliminar entregas solo cuando la opcion es diferente de registro de pesos finales
		//se comenta porque no deveria eliminarse las entregas, porque puede darse el caso que solo vas a agregar un descuento y todas las entregas quedarian igual
		boolean eliminoEntregas =Boolean.FALSE;
		if(!accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
			eliminoEntregas = formulario.cargarConfiguracionInicial(new ActionErrors(), request);
		}
		
		//se procesan las entregas para verificar si se activa el transito o no
		EntregaLocalCalendarioUtil.procesarEntregasParaVerificarTransito(request, formulario);
		
		LogSISPE.getLog().info("PRECIOS ALTERADOS: {}",session.getAttribute(PRECIOS_ALTERADOS));
		//se verifica si hubo cambio en los precios
		if(session.getAttribute(PRECIOS_ALTERADOS) != null){
			if(detallePedidoConsolidado==null){
				//se crea la ventana que se indicar\u00E1 el cambio de precios
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Precios y recetas modificados");
				popUp.setMensajeVentana("Algunos precios y detalles de recetas son DIFERENTES a los guardados inicialmente, " +
						"si usted desea puede actualizar estos datos haciendo clic en SI, si desea conservar los anteriores haga clic en NO, " +
					"posteriormente usted puede cambiar estos datos activando la opci\u00F3n <b>\"" + MessagesWebSISPE.getString("label.calculosConDatosActuales") + "\"</b> y haciendo clic en el bot\u00F3n <b>\"Actualizar\"</b>");
				popUp.setPreguntaVentana("\u00BFDesea actualizar el valor de los datos?");
				popUp.setEtiquetaBotonOK("Si");
				popUp.setEtiquetaBotonCANCEL("No");
				//popUp.setValorOK(PRECIOS_ACTUALES);
				if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
					popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok&intercambioPrecios=ok&preciosActuales=ok'});");
					popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes','div_datosCliente'], {parameters: 'cancelarActualizarPrecios=ok', popWait:false, evalScripts:true});");
				}else{
					popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok&intercambioPrecios=ok&preciosActuales=ok'});ocultarModal();");
					popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes','div_datosCliente'], {parameters: 'cancelarActualizarPrecios=ok', popWait:false, evalScripts:true});ocultarModal();");
				}				
				popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
				popUp.setAncho(Double.valueOf(60));
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
				
				//se guarda la ventana
				request.setAttribute(SessionManagerSISPE.POPUP, popUp);
			}
			else{
				//verificacion si los pedidos consolidados aplican precio de afiliados o no
				session.setAttribute(PRECIOS_ALTERADOS_CONSOLIDADOS,"CAMBIO_PRECIO");
				//se verifica si hubo cambio en los precios
				String actualizarPrecios=((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios"));
				LogSISPE.getLog().info("ACTUALIZAR PRECIOS: {}",actualizarPrecios);
				if(actualizarPrecios==null){
					if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS) != null && !((String)session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS)).equals("ok")){
						session.removeAttribute(SessionManagerSISPE.POPUP);
						session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS,"ok");
						ConsolidarAction.instanciarVentanaModificacionPreciosConsolidados(request);
					}
				}
//				actualizarDetallePedidosConsolidados(request, 
//						estadoActivo, estadoInactivo,vistaPedidoDTO.getRucEmpresa(),vistaPedidoDTO.getTipoDocumentoCliente(),detallePedido);
//				session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
			}
		}else{
			//si existen despachos y si es la opci\u00F3n modificar reserva
			boolean existenEntregasDomicilioCDDespachadas = false;
			for(DetallePedidoDTO detallePedidoDTO : detallePedido){
				if(EntregaLocalCalendarioUtil.existenEntregasDomicilioCDEntregadas(detallePedidoDTO)){
					existenEntregasDomicilioCDDespachadas = true;
					break;
				}
			}
			if( (despacho || existenEntregasDomicilioCDDespachadas) 
					&& session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
				instanciarVentanaEntregaDespachada(request);
			}
		}
		
		//se elimina de sesion el parametro
		session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		
		return eliminoEntregas;
	}

	public static Boolean validarFechasDespacho(HttpSession session,
			List<VistaDetallePedidoDTO> detalleVistaPedido) {
		Boolean despacho = false;
		if(session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){	
			//verifico si ya existen despachos
			for(VistaDetallePedidoDTO vistaDetallePedido :  detalleVistaPedido){
				if(CollectionUtils.isNotEmpty(vistaDetallePedido.getEntregaDetallePedidoCol())){//validar que existan entregas
					for(EntregaDetallePedidoDTO entregaDetallePedido:  vistaDetallePedido.getEntregaDetallePedidoCol()){					
						if(entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) && 
								((entregaDetallePedido.getFechaRegistroDespacho()==null && 
								entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega()!=null &&
								entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().getTime() <= UtilesSISPE.obtenerFechaActual().getTime()) || 
								(entregaDetallePedido.getFechaRegistroDespacho()!=null))){
							despacho = true;
							break;
						}
					}
				}
			}
		}
		return despacho;
	}

	public static void obtenerLLavesDescuentos(HttpServletRequest request,
			HttpSession session, boolean esPedidoGeneral) throws Exception {
		Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
		Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
		String sinDescuentos=(String)session.getAttribute("ec.com.smx.sic.sispe.sinDescuentos");
		if(CollectionUtils.isEmpty(llaveDescuentoCol) && CollectionUtils.isEmpty(descuentoVariableCol) && sinDescuentos==null){
			String codigoTipoDescuentoVariable = "";
			//se obtiene el c\u00F3digo del tipo de descuento variable
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
			if(parametroDTO.getValorParametro()!=null){
				codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
			}
			Collection<VistaPedidoDTO> pedidosConsolidados= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
			//colecciones que se enviar\u00E1n al m\u00E9todo que calcula los descuentos
			Collection<DescuentoDTO> descuentoVariableActualCol = new ArrayList<DescuentoDTO>();
			Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
			//obtener los descuentos del pedido actual
			VistaPedidoDTO vistaPedidoActualDTO =(VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			if(!esPedidoGeneral){		
				//se obtiene la colecci\u00F3n de los estados del descuentos
				Collection<DescuentoEstadoPedidoDTO> descuentos = vistaPedidoActualDTO.getDescuentosEstadosPedidos();
				if(descuentos==null){
					//llamada a la funci\u00F3n que obtiene los descuentos
					WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoActualDTO, request,Boolean.FALSE);
					descuentos = vistaPedidoActualDTO.getDescuentosEstadosPedidos();
				}
				if(descuentos!=null){
					for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentos){
						
						LogSISPE.getLog().info("[codigoTipoDescuento]: {}",descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento());
						//se verifica el c\u00F3digo del descuento variable
						if(descuentoEstadoPedidoDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento())){
							//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
							//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
							descuentoEstadoPedidoDTO.getDescuentoDTO().setPorcentajeDescuento(descuentoEstadoPedidoDTO.getPorcentajeDescuento());
							descuentoVariableActualCol.add(descuentoEstadoPedidoDTO.getDescuentoDTO());
						}
						
						//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
						if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
							llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
						}
						LogSISPE.getLog().info("LLAVE: {}",descuentoEstadoPedidoDTO.getLlaveDescuento());
					}
				}
			} 
			//asignar  al primer pedido el descuento en nulo para que encuentre y llene las variables de descuento variable
			if(CollectionUtils.isNotEmpty(pedidosConsolidados)){
				pedidosConsolidados.iterator().next().setDescuentosEstadosPedidos(null);
			}
			for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
				//buscar descuentos en caso de no estar null visPedDTO.getDescuentosEstadosPedidos()
				if(esPedidoGeneral){
					if(visPedDTO.getDescuentosEstadosPedidos()==null){
						//llamada a la funci\u00F3n que obtiene los descuentos
						WebSISPEUtil.obtenerDescuentosEstadoPedido(visPedDTO, request,Boolean.FALSE);
					}
				}
				
				//se verifica la existencia de una colecci\u00F3n de descuentos
				if(!visPedDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente")) && visPedDTO.getDescuentosEstadosPedidos()!=null && !visPedDTO.getDescuentosEstadosPedidos().isEmpty())
				{					 
					//se obtiene la colecci\u00F3n de los estados del descuentos
					 Collection<DescuentoEstadoPedidoDTO> descuentosPedidoConsolidado = visPedDTO.getDescuentosEstadosPedidos();
					
					for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentosPedidoConsolidado){
						
						LogSISPE.getLog().info("[codigoTipoDescuento]: {}",descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento());
						//se verifica el c\u00F3digo del descuento variable
						if(descuentoEstadoPedidoDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento())){
							//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
							//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
							descuentoEstadoPedidoDTO.getDescuentoDTO().setPorcentajeDescuento(descuentoEstadoPedidoDTO.getPorcentajeDescuento());
							descuentoVariableActualCol.add(descuentoEstadoPedidoDTO.getDescuentoDTO());
						}
						
						//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
						if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
							llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
						}
						LogSISPE.getLog().info("LLAVE: {}",descuentoEstadoPedidoDTO.getLlaveDescuento());
					}
					
				}
			}
			//obtener los descuentos llaves y decuentos variables que se aplicaran
			Collection<String> llavesProcesada=new ArrayList<String>();
			if(llavesDescuentosAplicados!= null && llavesDescuentosAplicados.size()>0){
				llavesProcesada.add(llavesDescuentosAplicados.iterator().next());
				Boolean llaveEncontrada=Boolean.FALSE;
				for(String llave:llavesDescuentosAplicados){
					String []descuento=llave.split("-");
					llaveEncontrada=Boolean.FALSE;
					for(String llaveProcesa:llavesProcesada){
						if(descuento.length >= 4){
							if(llaveProcesa.contains(descuento[1]+"-"+descuento[2]+"-"+descuento[3])){
								llaveEncontrada=Boolean.TRUE;
								break;
							}
						}
						else if(llaveProcesa.contains(descuento[1]+"-"+descuento[2])){
							llaveEncontrada=Boolean.TRUE;
							break;
						}
					}
					if(llaveEncontrada==Boolean.FALSE){
						llavesProcesada.add(llave);
					}
				}
			}
			llaveDescuentoCol = llavesProcesada;
			descuentoVariableCol=descuentoVariableActualCol;
			session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llaveDescuentoCol);
			session.setAttribute(CotizarReservarAction.COL_DESC_VARIABLES,descuentoVariableCol);
		}
	}


	/**
	 * Calcula los totales para cada detalle
	 * 
	 * @param detallePedidoDTO									El detalle del pedido
	 * @param request														Petici\u00F3n http
	 * @param verificarActualizacionPrecios			Indica si se debe tomar en cuenta el recalculo por diferencia entre precios hist\u00F3ricos y actuales
	 */
	public static void calcularValoresDetalle(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request, boolean verificarIntercambioPrecios,boolean actualizarPrecios)throws Exception{

		String accion = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		boolean esActivadoPrecioMayoristaArticulo = false;
		Integer codigoLocal = (Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
		//variable para controlar el c\u00E1lculo con precios de afiliado o no
		boolean preciosAfiliado = true;
		if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
			preciosAfiliado = false;
		}
		
		//se inicializan los totales del detalle
		double totalDetalle = 0;
		double totalDetalleCaja=0;
		double totalDetalleUnidad=0;
		double totalDetalleIVA = 0;
		Collection<String> detalleCajaUnidad = new ArrayList<String>();

		//se llama al m\u00E9todo que realiza el control de asignaci\u00F3n precios, cuando 
		//pueden cambiar los valores del pedido por precios actuales diferentes
		controlarRecalculoPorDiferenciaPrecios(detallePedidoDTO, request, verificarIntercambioPrecios,actualizarPrecios);

		//se inicializan los precios
		double precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado();
		double precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado();
		double precioCaja = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado();
		double precioCajaIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaIVAEstado();
		//se determina los precios que se deben usar
		if(!preciosAfiliado){
			precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado();
			precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVANoAfiliado();
			precioCaja = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaNoAfiliado();
			precioCajaIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaIVANoAfiliado();
		}
		
		//Cantidad Mayoreo para consolidado
		Long cantidadMayoreoEstadoConsolidado = 0L;
		if(request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null 
				&& ((String)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedioGeneral") == null || (request.getParameter(Globals.AYUDA) != null && request.getParameter(Globals.AYUDA).equals("guardarConsolidacion")))){
			List<DetallePedidoDTO> detallesPedidoConsolidado = (List<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			if(!detallesPedidoConsolidado.isEmpty()){
				for(DetallePedidoDTO detallePedidoEncontrado : detallesPedidoConsolidado){
					
					if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoEncontrado.getId().getCodigoArticulo())){
						cantidadMayoreoEstadoConsolidado = cantidadMayoreoEstadoConsolidado + detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado();
						LogSISPE.getLog().info("**MAYORISTA CantidadEstadoDetGen(): {}", detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado());
						LogSISPE.getLog().info("**MAYORISTA CantidadTotalConsolidadaDetGen: {}", detallePedidoEncontrado.getNpCantidadTotalConsolidada());
					}
				}
			}
		}
		LogSISPE.getLog().info("**MAYORISTA CantidadEstado: {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
		LogSISPE.getLog().info("**MAYORISTA cantidadMayoreoEstadoConsolidado: {}", cantidadMayoreoEstadoConsolidado.longValue());
		
		//se verifica si se debe aplicar el precio de mayorista
		if((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA)==null){
			if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo() && 
					esArticuloActivoParaPrecioMayorista(codigoLocal, detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado(),		
					detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayorista(), cantidadMayoreoEstadoConsolidado.longValue() > 0 ? cantidadMayoreoEstadoConsolidado : detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado(), 
					detallePedidoDTO.getArticuloDTO(), request)){
			//if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()){
				precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayorista();
				precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayoristaIVA();
				if(!preciosAfiliado){
					precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayoristaNoAfiliado();
					precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayoristaIVANoAfiliado();
				}
				esActivadoPrecioMayoristaArticulo = true;
			}
		}
		LogSISPE.getLog().info("PRECIO UNITARIO {}",precioUnitario);
		LogSISPE.getLog().info("*detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado() {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado());
		
		//se verifica si el art\u00EDculo es de peso variable
		if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO) || detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
			//se inicializa el valor del peso ingresado
			double pesoIngresado = 0;
			if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO) 
					&& accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){

				//se calcula el total del detalle en base al peso registrado en la confirmaci\u00F3n de la reservaci\u00F3n
				totalDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitario;
				totalDetalleIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue()	* precioUnitarioIVA;
			
				//se asigna el peso ingresado
				pesoIngresado = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue();
				LogSISPE.getLog().info("se actualiz\u00F3 desde la confirmaci\u00F3n de pesos");
			}else{
				//se calcula el total del detalle
				totalDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue()	* precioUnitario;
				totalDetalleIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitarioIVA;
				//se asigna el peso ingresado
				pesoIngresado = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue();
				LogSISPE.getLog().info("se actualiz\u00F3 desde la cotizaci\u00F3n o reservaci\u00F3n");
			}
			//Si la accion no es de Creacion de pedidos especiales
			if(accion!=null && !accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.crearPedidoEspecial"))){
				//si el art\u00EDculo es de peso variable (NO PAVO)
				if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
					LogSISPE.getLog().info("es un art\u00EDculo de PesoVariable (NO PAVO)");
					LogSISPE.getLog().info("peso ingresado: {}",pesoIngresado);
					
					Double cantidad = Double.valueOf(Math.ceil(pesoIngresado));
					if(cantidad > 0)
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(new Long(cantidad.longValue()));
					else
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(new Long(1));
	
					//este atributo se utiliza en la secci\u00F3n de entregas
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
					LogSISPE.getLog().info("cantidad generada: {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
				}
			}

			//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES));

			
		}else if((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS) == null 
				&& esArticuloActivoParaPrecioCaja(codigoLocal, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado(), detallePedidoDTO.getArticuloDTO(), request)){
			//se obtiene el cociente entero de la divisi\u00F3n entre la cantidad y la unidad de manejo
			long cociente = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
			//se calcula el residuo de la divisi\u00F3n
			long residuo = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue() * cociente;
			
			long residuoPrecioNormal = 0;
			double precioUnitarioIVAResiduoNormal = 0;
			double precioUnitarioResiduoNormal = 0;
			double totalDetalleUnidadNormal = 0; 
			boolean aplicaCajaResiduo = false;
			
			//si el pedido es parte de un grupo de consolidaci\u00F3n
			if(request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null 
					&& ((String)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedioGeneral") == null || (request.getParameter(Globals.AYUDA) != null && request.getParameter(Globals.AYUDA).equals("guardarConsolidacion")))){
				Map<String, long[]> cocienteResiduoConsolidado = new HashMap<String, long[]>();
				List<DetallePedidoDTO> detallesPedidoConsolidado = (List<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				long residuoConsolidado = 0;
				//obtiene el residuo de todos los pedidos
				cocienteResiduoConsolidado.put(detallePedidoDTO.getId().getCodigoPedido(), new long[]{cociente, residuo});
				residuoConsolidado = residuo;
				if(!detallesPedidoConsolidado.isEmpty()){
					LogSISPE.getLog().info("**CAJA CocienteAct {}: {}", detallePedidoDTO.getId().getCodigoPedido(), cociente);
					LogSISPE.getLog().info("**CAJA ResiduoAct {}: {}", detallePedidoDTO.getId().getCodigoPedido(), residuo);
					for(DetallePedidoDTO detallePedidoEncontrado : detallesPedidoConsolidado){
						
						if(!detallePedidoDTO.getId().getCodigoPedido().equals(detallePedidoEncontrado.getId().getCodigoPedido()) 
								&& detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoEncontrado.getId().getCodigoArticulo())){
							long cocienteAux = detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() / detallePedidoEncontrado.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
							long residuoAux = detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoEncontrado.getArticuloDTO().getUnidadManejoPrecioCaja().longValue() * cocienteAux;
							residuoConsolidado += residuoAux;
							cocienteResiduoConsolidado.put(detallePedidoEncontrado.getId().getCodigoPedido(), new long[]{cocienteAux, residuoAux});
							LogSISPE.getLog().info("**CAJA CocienteAux {}: {}", detallePedidoEncontrado.getId().getCodigoPedido(), cocienteAux);
							LogSISPE.getLog().info("**CAJA ResiduoAux {}: {}", detallePedidoEncontrado.getId().getCodigoPedido(), residuoAux);
						}
					}
				}
				LogSISPE.getLog().info("**CAJA ResiduoCon: {}",  residuoConsolidado);
				//cocienteResiduoConsolidado.put(null, new long[]{0, residuoConsolidado});
				LogSISPE.getLog().info("**CAJA cocienteResiduoConsolidado: {}", cocienteResiduoConsolidado);
				//Verifica si el residuo del total consolidado aplica precio de caja
				double precioUnitarioIVARespaldo = precioUnitarioIVA;
				double precioUnitarioRespaldo = precioUnitario;
				if((residuoConsolidado / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue()) > 0){
					//distribuye el residuo consolidado que aplica precio de caja seg\u00FAn el n\u00FAmero de pedido
					long cocienteUnidadAux = (residuoConsolidado / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue()) * detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
					//long residuoAux = residuoConsolidado - cocienteUnidadAux;
					
					Map<String, long[]> cocienteResiduoConsolidadoOrd = new TreeMap(cocienteResiduoConsolidado);
					for(Iterator i = cocienteResiduoConsolidadoOrd.keySet().iterator(); i.hasNext();){
						String codigoPedido = (String)i.next();
						long[] cocienteResiduo = cocienteResiduoConsolidadoOrd.get(codigoPedido);
						if(cocienteResiduo[1] > 0 && cocienteUnidadAux > 0){
							if(cocienteResiduo[1] > cocienteUnidadAux){
								//para mostrar los datos correctos en pantalla
								if(detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
									//calcula el precio normal de las unidades que sobraron
									residuoPrecioNormal = cocienteResiduo[1] - cocienteUnidadAux;
									precioUnitarioIVAResiduoNormal = precioUnitarioIVARespaldo;
									precioUnitarioResiduoNormal = precioUnitarioRespaldo;
									residuo = cocienteUnidadAux;
								}
								cocienteUnidadAux = 0;
							}else{
								cocienteUnidadAux -= cocienteResiduo[1];
							}
							//calcula el precio caja de las unidades que completan una caja en el consolidado
							if(detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
								precioUnitarioIVA = Util.roundDoubleMath(precioCajaIVA / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue(), NUMERO_DECIMALES);
								precioUnitario = Util.roundDoubleMath(precioCaja / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue(), NUMERO_DECIMALES);
								//validaci\u00F3n para imprimir los detalles del precio de caja
								aplicaCajaResiduo = true;
							}
						}
					}
				}
			}
			
			if(residuo>0){
				totalDetalle = cociente * precioCaja 	+ residuo * precioUnitario;
				totalDetalleIVA = cociente * precioCajaIVA 	+ residuo * precioUnitarioIVA;

				//------- segmento aumentado para mostrar en pantalla el detalle de los art\u00EDculos con precio de caja 
				totalDetalleCaja = cociente * precioCajaIVA;
				totalDetalleUnidad = residuo * precioUnitarioIVA;
				//Desglose del precio por cajas y unidades
				if(cociente > 0){
					if(cociente == 1){
						detalleCajaUnidad.add(cociente + " caja a " +  precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleCaja,NUMERO_DECIMALES));
					}else{
						detalleCajaUnidad.add(cociente + " cajas a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleCaja,NUMERO_DECIMALES));
					}
				}
				
				if(residuo == 1){
					detalleCajaUnidad.add(residuo + (aplicaCajaResiduo ? " unidad de caja a " : " unidad a ") + precioUnitarioIVA + " = " + Util.roundDoubleMath(totalDetalleUnidad,NUMERO_DECIMALES));
				}else{
					detalleCajaUnidad.add(residuo + (aplicaCajaResiduo ? " unidades de caja a " : " unidades a ") + precioUnitarioIVA + " = " + Util.roundDoubleMath(totalDetalleUnidad,NUMERO_DECIMALES));
				}
				
				if(residuoPrecioNormal > 0){
					totalDetalleUnidadNormal = residuoPrecioNormal * precioUnitarioIVAResiduoNormal;
					totalDetalle += residuoPrecioNormal * precioUnitarioResiduoNormal;
					totalDetalleIVA += totalDetalleUnidadNormal;
					if(residuoPrecioNormal == 1){
						detalleCajaUnidad.add(residuoPrecioNormal + " unidad a " + precioUnitarioIVAResiduoNormal + " = " + Util.roundDoubleMath(totalDetalleUnidadNormal,NUMERO_DECIMALES));
					}else{
						detalleCajaUnidad.add(residuoPrecioNormal + " unidades a " + precioUnitarioIVAResiduoNormal + " = " + Util.roundDoubleMath(totalDetalleUnidadNormal,NUMERO_DECIMALES));
					}
				}
				
				//--------------------------------------------------------------------------------
			}else{
				totalDetalle = cociente * precioCaja;
				totalDetalleIVA = cociente * precioCajaIVA;
				if(cociente==1)
					detalleCajaUnidad.add(cociente + " caja a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleIVA,NUMERO_DECIMALES));
				else
					detalleCajaUnidad.add(cociente + " cajas a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleIVA,NUMERO_DECIMALES));
				
			}
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpDetalleUnidades(detalleCajaUnidad);
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
				//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
				//se asigna la cantidad
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(Long.valueOf(aplicaCajaResiduo ? (residuoPrecioNormal > 0 ? residuoPrecioNormal : 0) : residuo));
				//se asigna el precio del detalle
				//se debe tomar el valor del residuo y multiplicarlo por el valor unitario sin IVA
				double totalDetalleUnitario = (aplicaCajaResiduo ? (residuoPrecioNormal > 0 ? residuoPrecioNormal : 0) : residuo) * (residuoPrecioNormal > 0 ? precioUnitarioResiduoNormal : precioUnitario);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalleUnitario), NUMERO_DECIMALES));
				
				//se calcula el valor total aplicar descuento MARCA PROPIA
				Double npValorPrevioEstadoDcto = (aplicaCajaResiduo ? (residuoPrecioNormal > 0 ? residuoPrecioNormal : 0) : residuo) * (residuoPrecioNormal > 0 ? precioUnitarioResiduoNormal : precioUnitario);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDcto,NUMERO_DECIMALES));
			}
			else{
				//se calcula el valor total aplicar descuento MARCA PROPIA
				Double npValorPrevioEstadoDcto = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().doubleValue() * detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado();
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDcto,NUMERO_DECIMALES));
				
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento()==null){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
				}
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento()==null){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
				}
			}
			
			
		}else{
			totalDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitario;
			totalDetalleIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
			//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
			//se asigna la cantidad y el precio total del detalle sin IVA
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES));
			
			//se calcula el valor total aplicar descuento MARCA PROPIA
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES));
		}

		//si el precio de mayorista se activa no se deben dar descuentos
		if(esActivadoPrecioMayoristaArticulo){
			if((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA)==null){
				//se calcula el valor total aplicar descuento MARCA PROPIA
				Double npValorPrevioEstadoDcto = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitario;
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDcto,NUMERO_DECIMALES));
				
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0l);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0d);
			}else{
				//se aplica el descuento al total de los detalles porque no aplica MAYORISTA	
				Double npValorPrevioEstadoDcto = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().doubleValue() * detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado();
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDcto,NUMERO_DECIMALES));
			}
		}
		
		//se llama a la funci\u00F3n que calcula cuantos bultos que representa cada art\u00EDculo
		int bultos = UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue(), detallePedidoDTO.getArticuloDTO());
		detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadBultos(Integer.valueOf(bultos));
		
		totalDetalle = Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES);
		totalDetalleIVA = Util.roundDoubleMath(Double.valueOf(totalDetalleIVA),NUMERO_DECIMALES);
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstado(totalDetalle);
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoIVA(totalDetalleIVA);
		
		LogSISPE.getLog().info("total detalle: {}",totalDetalle);
		LogSISPE.getLog().info("total detalle iva: {}",totalDetalleIVA);

		LogSISPE.getLog().info("npHabilitarPrecioCaja: {}",detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja());//getNpHabilitarPrecioCaja()
		//si no se deben aplicar descuentos se actualizan los totales netos
		if(request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS) == null){
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(totalDetalle);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(totalDetalleIVA);
		}
		
		//bgudino, si a un articulo tipo caja no se aplica descuento caja, se pintan los detalles de los precios caja
		if(request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS) != null && request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS).equals("NO")
				&&	!esLocalActivoParaPrecioMayorista(codigoLocal, detallePedidoDTO.getArticuloDTO(), request) 
				&& detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja() && detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado().doubleValue() > 0){
		
			long cociente = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
			//se calcula el residuo de la divisi\u00F3n
			long residuo = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue() * cociente;
			
			long residuoPrecioNormal = 0;
			double precioUnitarioIVAResiduoNormal = 0;
			double precioUnitarioResiduoNormal = 0;
			double totalDetalleUnidadNormal = 0; 
			boolean aplicaCajaResiduo = false;
			
			//si el pedido es parte de un grupo de consolidaci\u00F3n
			if(request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null 
					&& ((String)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedioGeneral") == null || (request.getParameter(Globals.AYUDA) != null && request.getParameter(Globals.AYUDA).equals("guardarConsolidacion")))){
				Map<String, long[]> cocienteResiduoConsolidado = new HashMap<String, long[]>();
				List<DetallePedidoDTO> detallesPedidoConsolidado = (List<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				long residuoConsolidado = 0;
				//obtiene el residuo de todos los pedidos
				cocienteResiduoConsolidado.put(detallePedidoDTO.getId().getCodigoPedido(), new long[]{cociente, residuo});
				residuoConsolidado = residuo;
				if(!detallesPedidoConsolidado.isEmpty()){
					LogSISPE.getLog().info("**CAJA CocienteAct {}: {}", detallePedidoDTO.getId().getCodigoPedido(), cociente);
					LogSISPE.getLog().info("**CAJA ResiduoAct {}: {}", detallePedidoDTO.getId().getCodigoPedido(), residuo);
					for(DetallePedidoDTO detallePedidoEncontrado : detallesPedidoConsolidado){
						
						if(!detallePedidoDTO.getId().getCodigoPedido().equals(detallePedidoEncontrado.getId().getCodigoPedido()) 
								&& detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoEncontrado.getId().getCodigoArticulo())){
							long cocienteAux = detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() / detallePedidoEncontrado.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
							long residuoAux = detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoEncontrado.getArticuloDTO().getUnidadManejoPrecioCaja().longValue() * cocienteAux;
							residuoConsolidado += residuoAux;
							cocienteResiduoConsolidado.put(detallePedidoEncontrado.getId().getCodigoPedido(), new long[]{cocienteAux, residuoAux});
							LogSISPE.getLog().info("**CAJA CocienteAux {}: {}", detallePedidoEncontrado.getId().getCodigoPedido(), cocienteAux);
							LogSISPE.getLog().info("**CAJA ResiduoAux {}: {}", detallePedidoEncontrado.getId().getCodigoPedido(), residuoAux);
						}
					}
				}
				LogSISPE.getLog().info("**CAJA ResiduoCon: {}",  residuoConsolidado);
				//cocienteResiduoConsolidado.put(null, new long[]{0, residuoConsolidado});
				LogSISPE.getLog().info("**CAJA cocienteResiduoConsolidado: {}", cocienteResiduoConsolidado);
				//Verifica si el residuo del total consolidado aplica precio de caja
				double precioUnitarioIVARespaldo = precioUnitarioIVA;
				double precioUnitarioRespaldo = precioUnitario;
				if((residuoConsolidado / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue()) > 0){
					//distribuye el residuo consolidado que aplica precio de caja seg\u00FAn el n\u00FAmero de pedido
					long cocienteUnidadAux = (residuoConsolidado / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue()) * detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue();
					//long residuoAux = residuoConsolidado - cocienteUnidadAux;
					
					Map<String, long[]> cocienteResiduoConsolidadoOrd = new TreeMap(cocienteResiduoConsolidado);
					for(Iterator i = cocienteResiduoConsolidadoOrd.keySet().iterator(); i.hasNext();){
						String codigoPedido = (String)i.next();
						long[] cocienteResiduo = cocienteResiduoConsolidadoOrd.get(codigoPedido);
						if(cocienteResiduo[1] > 0 && cocienteUnidadAux > 0){
							if(cocienteResiduo[1] > cocienteUnidadAux){
								//para mostrar los datos correctos en pantalla
								if(detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
									//calcula el precio normal de las unidades que sobraron
									residuoPrecioNormal = cocienteResiduo[1] - cocienteUnidadAux;
									precioUnitarioIVAResiduoNormal = precioUnitarioIVARespaldo;
									precioUnitarioResiduoNormal = precioUnitarioRespaldo;
									residuo = cocienteUnidadAux;
								}
								cocienteUnidadAux = 0;
							}else{
								cocienteUnidadAux -= cocienteResiduo[1];
							}
							//calcula el precio caja de las unidades que completan una caja en el consolidado
							if(detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
								precioUnitarioIVA = Util.roundDoubleMath(precioCajaIVA / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue(), NUMERO_DECIMALES);
								precioUnitario = Util.roundDoubleMath(precioCaja / detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja().longValue(), NUMERO_DECIMALES);
								//validaci\u00F3n para imprimir los detalles del precio de caja
								aplicaCajaResiduo = true;
							}
						}
					}
				}
			}
			
			if(residuo>0){
				totalDetalle = cociente * precioCaja 	+ residuo * precioUnitario;
				totalDetalleIVA = cociente * precioCajaIVA 	+ residuo * precioUnitarioIVA;

				//------- segmento aumentado para mostrar en pantalla el detalle de los art\u00EDculos con precio de caja 
				totalDetalleCaja = cociente * precioCajaIVA;
				totalDetalleUnidad = residuo * precioUnitarioIVA;
				//Desglose del precio por cajas y unidades
				if(cociente > 0){
					if(cociente == 1){
						detalleCajaUnidad.add(cociente + " caja a " +  precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleCaja,NUMERO_DECIMALES));
					}else{
						detalleCajaUnidad.add(cociente + " cajas a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleCaja,NUMERO_DECIMALES));
					}
				}
				
				if(residuo == 1){
					detalleCajaUnidad.add(residuo + (aplicaCajaResiduo ? " unidad de caja a " : " unidad a ") + precioUnitarioIVA + " = " + Util.roundDoubleMath(totalDetalleUnidad,NUMERO_DECIMALES));
				}else{
					detalleCajaUnidad.add(residuo + (aplicaCajaResiduo ? " unidades de caja a " : " unidades a ") + precioUnitarioIVA + " = " + Util.roundDoubleMath(totalDetalleUnidad,NUMERO_DECIMALES));
				}
				
				if(residuoPrecioNormal > 0){
					totalDetalleUnidadNormal = residuoPrecioNormal * precioUnitarioIVAResiduoNormal;
					totalDetalle += residuoPrecioNormal * precioUnitarioResiduoNormal;
					totalDetalleIVA += totalDetalleUnidadNormal;
					if(residuoPrecioNormal == 1){
						detalleCajaUnidad.add(residuoPrecioNormal + " unidad a " + precioUnitarioIVAResiduoNormal + " = " + Util.roundDoubleMath(totalDetalleUnidadNormal,NUMERO_DECIMALES));
					}else{
						detalleCajaUnidad.add(residuoPrecioNormal + " unidades a " + precioUnitarioIVAResiduoNormal + " = " + Util.roundDoubleMath(totalDetalleUnidadNormal,NUMERO_DECIMALES));
					}
				}
			}else{
				totalDetalle = cociente * precioCaja;
				totalDetalleIVA = cociente * precioCajaIVA;
				if(cociente==1){
					detalleCajaUnidad.add(cociente + " caja a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleIVA,NUMERO_DECIMALES));
				}else{
					detalleCajaUnidad.add(cociente + " cajas a " + precioCajaIVA + " = " + Util.roundDoubleMath(totalDetalleIVA,NUMERO_DECIMALES));
				}
			}
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpDetalleUnidades(detalleCajaUnidad);
		}
	}
	
	/**
	 * Determina si los precios de cada art\u00EDculo deben ser actualizados por los valores actuales o los valores del
	 * estado anterior
	 * @param estadoDetallePedidoDTO
	 * @param accion
	 * @param request
	 */
	private static void controlarRecalculoPorDiferenciaPrecios(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request, boolean verificarIntercambioPrecios,boolean actualizarPrecios)throws Exception{

		//si se detect\u00F3 cambios en los precios actuales en relaci\u00F3n a los precios del estado anterior
		//y si no se activ\u00F3 la opci\u00F3n para cambiar precios manualmente
		if(verificarIntercambioPrecios && request.getSession().getAttribute(PRECIOS_ALTERADOS)!=null 
				&& request.getSession().getAttribute(PRECIOS_ALTERADOS).equals("SI")
				&& request.getSession().getAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS) == null){

			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			boolean cargarPreciosActuales = false;
			//hay dos formas de trabajar con los precios actuales
			//1. Activando el check desde la pantalla (parametro: "checkCalculosPreciosMejorados")
			//2. Respondiendo SI a la pregunta de confirmaci\u00F3n cuando se carga la pantalla (parametro: "preciosActuales")
			if((request.getParameter("checkCalculosPreciosMejorados")!=null && request.getParameter("checkCalculosPreciosMejorados").equals(estadoActivo))
					|| request.getParameter("preciosActuales")!=null){
				cargarPreciosActuales = true;
			}
			else if(actualizarPrecios){
				cargarPreciosActuales = actualizarPrecios;
			}

			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioActual()!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioEstadoAnterior()!=null){
				LogSISPE.getLog().info("entra a modificar los precios");
				//si la selecci\u00F3n esta activa
				if(cargarPreciosActuales){
					LogSISPE.getLog().info("se cargan los precios actuales");
					//se asignan los precios actuales de afiliado (posici\u00F3n 0)
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioActual()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioIVAActual()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaActual()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaIVAActual()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayorista(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaActual()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVA(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaIVAActual()[0]);
					//se asignan los precios actuales de no afiliado (posici\u00F3n 1)
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioActual()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioIVAActual()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaActual()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaIVAActual()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaActual()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaIVAActual()[1]);
				}else{
					LogSISPE.getLog().info("se cargan los precios del estado anterior");
					//se asignan los precios del estado anterior de afiliado (posici\u00F3n 0)
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioEstadoAnterior()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioIVAEstadoAnterior()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaEstadoAnterior()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVAEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaIVAEstadoAnterior()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayorista(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaEstadoAnterior()[0]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVA(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaIVAEstadoAnterior()[0]);
					//se asignan los precios del estado anterior de no afiliado (posici\u00F3n 1)
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioEstadoAnterior()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorUnitarioIVAEstadoAnterior()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaEstadoAnterior()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorCajaIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorCajaIVAEstadoAnterior()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaNoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaEstadoAnterior()[1]);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayoristaIVANoAfiliado(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorMayoristaIVAEstadoAnterior()[1]);
				}
			}

			//se verifica si existe una diferencia en la receta
			if(detallePedidoDTO.getNpRecetaActualEsDiferenteAHistorial()){
				//se intercambia el contenido de la receta 
//				Collection<ArticuloRelacionDTO> recetaArticulo = Collections.unmodifiableCollection(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol());
//				Collection<ArticuloRelacionDTO> recetaArticuloOriginal = Collections.unmodifiableCollection(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal());
				ArrayList<String> itemsAgregados = detallePedidoDTO.getItemsAgregadosReceta();
				ArrayList<String> itemsEliminados = detallePedidoDTO.getItemsAgregadosReceta();
				Integer canMaxModIteRec = detallePedidoDTO.getNpCanMaxModIteRec();
				
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getRecetaArticulosRespaldo())){
					detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(detallePedidoDTO.getArticuloDTO().getRecetaArticulosRespaldo());
					detallePedidoDTO.getArticuloDTO().setRecetaArticulosRespaldo(null);
				}
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginalRespaldo())){
					detallePedidoDTO.getArticuloDTO().setRecetaArticulosOriginal(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginalRespaldo());
					detallePedidoDTO.getArticuloDTO().setRecetaArticulosOriginalRespaldo(null);
				}
				detallePedidoDTO.setItemsAgregadosReceta(detallePedidoDTO.getItemsAgregadosRecetaRespaldo());
				detallePedidoDTO.setItemsEliminadosReceta(detallePedidoDTO.getItemsEliminadosRecetaRespaldo());
				detallePedidoDTO.setNpCanMaxModIteRec(detallePedidoDTO.getNpCanMaxModIteRecRespaldo());

//				detallePedidoDTO.getArticuloDTO().setRecetaArticulosRespaldo(recetaArticulo);
//				detallePedidoDTO.getArticuloDTO().setRecetaArticulosOriginalRespaldo(recetaArticuloOriginal);
				detallePedidoDTO.setItemsAgregadosRecetaRespaldo(itemsAgregados);
				detallePedidoDTO.setItemsEliminadosRecetaRespaldo(itemsEliminados);
				detallePedidoDTO.setNpCanMaxModIteRecRespaldo(canMaxModIteRec);
				
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
					recalcularPrecioReceta(detallePedidoDTO, request);
				}
				verificarDatosSICReceta(request, detallePedidoDTO, false);
				verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request);
			}
		}
	}
	
	/**
	 * Construye una nueva receta para los canastos
	 * 
	 * @param  articuloDTO					El art\u00EDculoDTO que se desea agregar
	 * @param  cantidadArticulo			La cantidad del art\u00EDculo que se desea agregar
	 * @param  request							La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return recetaArticuloDTO		El objeto receta que fu\u00E9 creado
	 */	
	public static ArticuloRelacionDTO construirNuevaReceta(ArticuloDTO articuloDTO, Long cantidadArticulo, HttpServletRequest request)throws Exception{
		//se obtiene el estado activo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		//se inicializan los objetos de la receta
		ArticuloRelacionDTO recetaArticuloDTO = null;
//		ArticuloPrecioDTO articuloPrecioDTO = null;
//		Collection<ArticuloPrecioDTO> articuloPrecioCol = null;
		Integer codigoLocal = (Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
		
		//se obtiene la cantidad solicitada del canasto
		//la comparaci\u00F3n se hace como si se multiplicara [1 x cantidadCanasto]
		DetallePedidoDTO detallePedidoDTO = ((DetallePedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.receta.detallePedidoDTO"));
		Long cantidadTotal = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * cantidadArticulo.longValue();
		//si no es implemento se verifican las otras condiciones
		if(articuloDTO.getNpImplemento().equals(estadoInactivo)
				&& articuloDTO.getNpStockArticulo()!=null 
				&& (articuloDTO.getNpStockArticulo().longValue() < cantidadTotal.longValue())){
			//se asigna el valor correspondiente a este atributo
			articuloDTO.setNpEstadoStockArticulo(SessionManagerSISPE.getEstadoInactivo(request));
		}

		try{
			
//			articuloPrecioCol = new ArrayList<ArticuloPrecioDTO>();
			//se arma la nueva receta
			recetaArticuloDTO = new ArticuloRelacionDTO();
			recetaArticuloDTO.getId().setCodigoArticulo(detallePedidoDTO.getId().getCodigoArticulo());
			recetaArticuloDTO.getId().setCodigoArticuloRelacionado(articuloDTO.getId().getCodigoArticulo());
			recetaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			recetaArticuloDTO.setNpNuevo(estadoActivo);			
			recetaArticuloDTO.setEstado(estadoActivo);
			recetaArticuloDTO.setCantidad(cantidadArticulo.intValue());			
//			recetaArticuloDTO.setPrecioUnitario(articuloDTO.getPrecioBase());
//			recetaArticuloDTO.setPrecioCaja(articuloDTO.getPrecioCaja());			
//			recetaArticuloDTO.setPrecioMayorista(articuloDTO.getPrecioMayorista());
//			articuloPrecioDTO = new ArticuloPrecioDTO();
//			articuloPrecioDTO.getId().setCodigoCompania(articuloDTO.getId().getCodigoCompania());
//			articuloPrecioDTO.getId().setCodigoTipoPrecio(SICArticuloConstantes.getInstancia().TIPO_PRECIO_BASE);
//			articuloPrecioDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
//			articuloPrecioDTO.setValorActual(articuloDTO.getPrecioBase());									
//			articuloPrecioCol.add(articuloPrecioDTO);
//			
//			articuloPrecioDTO = new ArticuloPrecioDTO();
//			articuloPrecioDTO.getId().setCodigoCompania(articuloDTO.getId().getCodigoCompania());
//			articuloPrecioDTO.getId().setCodigoTipoPrecio(SICArticuloConstantes.getInstancia().TIPO_PRECIO_CAJA);
//			articuloPrecioDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
//			articuloPrecioDTO.setValorActual(articuloDTO.getPrecioCaja());										
//			articuloPrecioCol.add(articuloPrecioDTO);
//			
//			articuloPrecioDTO = new ArticuloPrecioDTO();
//			articuloPrecioDTO.getId().setCodigoCompania(articuloDTO.getId().getCodigoCompania());
//			articuloPrecioDTO.getId().setCodigoTipoPrecio(SICArticuloConstantes.getInstancia().TIPO_PRECIO_MAYORISTA);
//			articuloPrecioDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
//			articuloPrecioDTO.setValorActual(articuloDTO.getPrecioMayorista());										
//			articuloPrecioCol.add(articuloPrecioDTO);
			
			
			recetaArticuloDTO.setCantidadMinimaMayoreo(articuloDTO.getCantidadMayoreo());
			recetaArticuloDTO.setNpCodigoTipoDescuento(articuloDTO.getNpCodigoTipoDescuento());
			
			recetaArticuloDTO.setArticuloRelacion(articuloDTO);
			
			//si el art\u00EDculo aplica IVA
			//los valores con impuestos se obtienen desde el sic
//			if(articuloDTO.getAplicaImpuestoVenta().equals(estadoActivo)){
//				recetaArticuloDTO.setPrecioUnitarioIVA(articuloDTO.getPrecioBaseImp());
//				recetaArticuloDTO.setPrecioCajaIVA(articuloDTO.getPrecioCajaImp());
//				recetaArticuloDTO.setPrecioMayoristaIVA(articuloDTO.getPrecioMayoristaImp());
//			}else{
//				recetaArticuloDTO.setPrecioUnitarioIVA(articuloDTO.getPrecioBase());
//				recetaArticuloDTO.setPrecioCajaIVA(articuloDTO.getPrecioCaja());
//				recetaArticuloDTO.setPrecioMayoristaIVA(articuloDTO.getPrecioMayorista());
//			}

			//se verifican si los items aplican para precio de caja o mayorista
			//if(esLocalActivoParaPrecioMayorista(codigoLocal, recetaArticuloDTO.getArticulo(), request)){
			if(esLocalActivoParaPrecioMayorista(codigoLocal, recetaArticuloDTO.getArticuloRelacion(), request)){
				recetaArticuloDTO.getArticuloRelacion().setNpHabilitarPrecioCaja(estadoInactivo);
				if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo() && recetaArticuloDTO.getArticuloRelacion().getPrecioMayorista() > 0 && recetaArticuloDTO.getArticuloRelacion().getCantidadMayoreo() > 0 ){
					recetaArticuloDTO.getArticuloRelacion().setNpHabilitarPrecioMayorista(estadoActivo);
				}
			}
			
			//antes de realizar la llamada a los calculos del detalle de la receta comprbar que se pase el articulo relacion
			if(recetaArticuloDTO.getArticuloRelacion() == null){
				recetaArticuloDTO.setArticuloRelacion(recetaArticuloDTO.getArticulo());
			}
			//fin llamada a los calculos del detalle de la receta comprbar que se pase el articulo relacion
			
			//lamada al m\u00E9todo que calcula los totales del detalle
			calcularTotalesDetalleReceta(recetaArticuloDTO);
			
			//si no es una canasta vacia
			if(!detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
				recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0l);
				recetaArticuloDTO.setValorPrevioEstadoDescuento(0d);
				recetaArticuloDTO.setValorTotalEstado(recetaArticuloDTO.getPrecioTotal());
				//recetaArticuloDTO.setValorTotalEstadoIVA(recetaArticuloDTO.getPrecioTotalIVA());
				recetaArticuloDTO.setValorTotalEstadoIVA(recetaArticuloDTO.getPrecioTotalIMP());
			}
			
//			articuloDTO.setArticuloPrecioCol(articuloPrecioCol);
			recetaArticuloDTO.setArticulo(articuloDTO);

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return recetaArticuloDTO;
	}
	
	/**
	 * Determina los valores de los totales del detalle de un canasto.
	 * 
	 * @param recetaArticuloDTO			- El objeto que representa un item del canasto
	 * @param estadoActivo					- El valor que indica un estado activo
	 */
	public static void calcularTotalesDetalleReceta(ArticuloRelacionDTO recetaArticuloDTO)
	{
		//se incializan los totales
		double valorTotal = 0;
		double valorTotalIVA = 0;
		ArticuloPrecioDTO articuloPrecioDTO = new ArticuloPrecioDTO();
		//ArticuloDTO articuloDTO = new ArticuloDTO();
		
		Collection<ArticuloPrecioDTO> articuloPrecioCol = new ArrayList<ArticuloPrecioDTO>();
		
		//se verifica si el art\u00EDculo es de peso variable
		if(recetaArticuloDTO.getArticuloRelacion().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
			//se calcula el peso total
			//double pesoTotal = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getArticulo().getPesoAproximado().doubleValue();
			double pesoTotal = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getArticuloRelacion().getArticuloComercialDTO().getPesoAproximadoVenta();			
			//se calcula el precio total de acuerdo al peso
//			valorTotal = pesoTotal * recetaArticuloDTO.getPrecioUnitario().doubleValue();
//			valorTotalIVA = pesoTotal * recetaArticuloDTO.getPrecioUnitarioIVA().doubleValue();			
			valorTotal = pesoTotal * recetaArticuloDTO.getArticuloRelacion().getPrecioBase().doubleValue();
//			valorTotalIVA = pesoTotal * recetaArticuloDTO.getPrecioUnitarioIVA().doubleValue();
			valorTotalIVA = pesoTotal * recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp().doubleValue();
			LogSISPE.getLog().info("valorTotalIVA: {}",valorTotalIVA);
		}else{
			//se calcula el precio total de forma normal
//			valorTotal = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getPrecioUnitario().doubleValue();
//			valorTotalIVA = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getPrecioUnitarioIVA().doubleValue();
			valorTotal = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getArticuloRelacion().getPrecioBase().doubleValue();
			valorTotalIVA = recetaArticuloDTO.getCantidad().longValue() * recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp().doubleValue();
		}
		//se asignan los totales
		valorTotal = Util.roundDoubleMath(Double.valueOf(valorTotal),NUMERO_DECIMALES);
		valorTotalIVA = Util.roundDoubleMath(Double.valueOf(valorTotalIVA),NUMERO_DECIMALES);
		
//		recetaArticuloDTO.setPrecioTotal(valorTotal);
//		recetaArticuloDTO.setPrecioTotalIVA(valorTotalIVA);
		articuloPrecioDTO = new ArticuloPrecioDTO();
		articuloPrecioDTO.getId().setCodigoCompania(recetaArticuloDTO.getId().getCodigoCompania());
		articuloPrecioDTO.getId().setCodigoTipoPrecio(SICArticuloConstantes.TIPO_PRECIO_CAJA);
		articuloPrecioDTO.getId().setCodigoArticulo(recetaArticuloDTO.getId().getCodigoArticulo());
		articuloPrecioDTO.setValorActual(valorTotal);										
		articuloPrecioCol.add(articuloPrecioDTO);
		
		//recetaArticuloDTO.setArticulo(articulo1)
		
	}
	
	/**
	 * Asigna el listado de items de un canasto al art\u00EDculo del detalle del pedido
	 * @param request 						La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @param detallePedidoDTO 				El objeto que contiene el detalle del pedido
	 * @param secuencialEstadoPedido 		Indica el secuencial del hist\u00F3rico del pedido para obtener el detalle de la receta
	 * @param verificarSIC					Indica si se debe o no consultar el stock desde el SIC para cada receta
	 * @throws Exception
	 */
	public static void cargarRecetaArticulo(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, String secuencialEstadoPedido,Boolean verificarSIC,
			Collection<ArticuloRelacionDTO> recetaArticuloConsolidado, boolean joinArticuloTemporada)throws Exception
	{
		//se obtiene el estado activo e inactivo [1 y 0 respectivamente]
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//se crea la consulta para traer los items del canasto
		ArticuloRelacionDTO consultaRecetaArticuloDTO = new ArticuloRelacionDTO(Boolean.TRUE);
		consultaRecetaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaRecetaArticuloDTO.getId().setCodigoArticulo(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
		//consultaRecetaArticuloDTO.getId().setCodigoArticuloRelacionado(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
		consultaRecetaArticuloDTO.setEstado(estadoActivo);
		//se crea el objeto articuloDTO para obtener la relaci\u00F3n
		ArticuloDTO articuloDTO = new ArticuloDTO();
		//ArticuloDTO articuloDTO = new ArticuloDTO(null,null);
		//se construye la consulta para traer el status del art\u00EDculo desde el SIC
		WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, estadoActivo, detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo(), accionActual);
		consultaRecetaArticuloDTO.setArticulo(articuloDTO);
		//se asigna un indicador para saber si es un canasto de cotizaciones (1: si, 0: no)
		consultaRecetaArticuloDTO.setNpEstadoRecetaEspecial(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio());
		
		//campo no persistente que indica si se consulta o no con integracion SIC
		consultaRecetaArticuloDTO.setNpVerificarSIC(verificarSIC);
		
		//Obtener datos de la colecci\u00F3n de recetas
		Collection<ArticuloRelacionDTO> recetaArticuloActual = SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticulo(consultaRecetaArticuloDTO, joinArticuloTemporada);
		LogSISPE.getLog().info("recetaArticuloActual.size() {}: ", recetaArticuloActual.size());
		//se asigna la nueva receta
		detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloActual);
		
		ActionMessages errors = new ActionMessages();
		//se validan los datos de los articulos
		UtilesSISPE.validarArticuloDetallePedido(detallePedidoDTO, errors);
		if(!errors.isEmpty()){
			request.setAttribute("ec.com.smx.sic.sispe.errores.articulos", errors);
			throw new SISPEException("Error al obtener el art\u00EDculo, existen problemas con la informaci\u00F3n registrada");
		}
		
		if(secuencialEstadoPedido!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
			
			EstadoDetalleRecetaDTO estadoDetalleRecetaDTO = new EstadoDetalleRecetaDTO();
			estadoDetalleRecetaDTO.getId().setCodigoCompania(consultaRecetaArticuloDTO.getId().getCodigoCompania());
			estadoDetalleRecetaDTO.getId().setCodigoArticulo(consultaRecetaArticuloDTO.getId().getCodigoArticulo());
			estadoDetalleRecetaDTO.getId().setSecuencialEstadoPedido(secuencialEstadoPedido);
			estadoDetalleRecetaDTO.setCodigoAreaTrabajo(consultaRecetaArticuloDTO.getArticulo().getNpCodigoLocal());
			
			//se modifica la consulta anterior para obtener el hist\u00F3rico
			consultaRecetaArticuloDTO.setNpSecuencialEstadoPedido(secuencialEstadoPedido);
			Collection<ArticuloRelacionDTO> recetaArticuloEstado =null;
			//Solo si existe un pedido consolidado
			if(CollectionUtils.isEmpty(recetaArticuloConsolidado)){
			//se realiza la consulta
			recetaArticuloEstado =
					SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticuloDesdeHistoricoReceta(estadoDetalleRecetaDTO, consultaRecetaArticuloDTO);
			}else{
				recetaArticuloEstado=recetaArticuloConsolidado;
			}
			LogSISPE.getLog().info("recetaArticuloEstado.size() {}: ", recetaArticuloEstado.size());
			if(CollectionUtils.isNotEmpty(recetaArticuloEstado) && CollectionUtils.isNotEmpty(recetaArticuloActual)){
				//se verifica la receta actual
				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloActual);
				//se recalcula el valor de la receta actual
				double valorRecetaActual = DetalleCanastaAction.calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.TRUE);
				
				//se verifica la receta anterior
				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloEstado);
				//se recalcula el valor de la receta anterior
				double valorRecetaEstado = DetalleCanastaAction.calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
				
				if(valorRecetaActual != valorRecetaEstado || verificarCambioPreciosCajaMayoristaEnReceta(recetaArticuloActual, recetaArticuloEstado)){
					//existe modificaci\u00F3n de precios
					detallePedidoDTO.setNpRecetaActualEsDiferenteAHistorial(Boolean.TRUE);
				}
				
				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloEstado);
				detallePedidoDTO.getArticuloDTO().setRecetaArticulosRespaldo(recetaArticuloActual);
				
			}else if(CollectionUtils.isNotEmpty(recetaArticuloActual)){
				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloActual);
			}else if(CollectionUtils.isNotEmpty(recetaArticuloEstado)){
				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloEstado);
			}
			
			//se recalculan los valores
			recalcularPrecioReceta(detallePedidoDTO, request);
			calcularValoresDetalle(detallePedidoDTO, request, false,false);
		}
		
		//se verifica si exiten \u00EDtems en la receta
		if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol() == null){
			detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(new ArrayList<ArticuloRelacionDTO>());
		}else if(!detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty()){
			//el siguiente m\u00E9todo realiza validaciones sobre stock y alcance de los items adem\u00E1s de algunas inicializaciones necesarias
			//se env\u00EDa el \u00FAltimo par\u00E1metro en "false" para que no obtenga los stocks y alcances desde el SIC ya que el m\u00E9todo anterior "transObtenerRecetaArticulo" 
			//ya realiza esta tarea
			verificarDatosSICReceta(request, detallePedidoDTO, false);
		}
		
		articuloDTO = null;
		consultaRecetaArticuloDTO = null;
	}
	
	/**
	 * Obtiene el stock y alcance de los \u00EDtems de un canasto.
	 * 
	 * @param 	request									La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @param 	detallePedidoDTO				El detalle principal del pedido
	 * @param 	obtenerDatosSIC					Indicador para consultar al SIC el stock, alcance entre otras cosas
	 * 																	[true: SI consultar, false: NO consultar]	
	 * @throws 	Exception						
	 */
	public static void verificarDatosSICReceta(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, boolean obtenerDatosSIC)throws Exception{
		
		if(obtenerDatosSIC){
			String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			Collection<ArticuloDTO> articulosCanasto = new ArrayList<ArticuloDTO>();
			//se recorre la la colecci\u00F3n de la receta
			for(Iterator <ArticuloRelacionDTO> it = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator(); it.hasNext(); ){
				ArticuloRelacionDTO recetaArticuloDTO = it.next();
				//se construye la consulta para traer el stock y alcance de los art\u00EDculos del canasto
				WebSISPEUtil.construirConsultaArticulos(request,recetaArticuloDTO.getArticuloRelacion(),estadoActivo, 
						detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo(), accionActual);
				
				recetaArticuloDTO.getArticuloRelacion().setNpSecuencialEstadoPedido(null);
				articulosCanasto.add(recetaArticuloDTO.getArticuloRelacion());
			}
			
			//se gestiona el alcance desde el MAX
			gestionarAlcanceMax(articulosCanasto, request);

			//llamada al m\u00E9todo que obtiene el stock y alcance de los art\u00EDculos
			SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosCanasto);
		}

		//se analizan los posibles problemas en items de la receta por stock, alcance, etc.
		verificarProblemasEnRecetaPorDatosSIC(detallePedidoDTO, request);
		
		//si la propiedad de la receta original est\u00E1 vacia
		if(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal()==null){
			LogSISPE.getLog().info("LA RECETA ORIGINAL ESTA VACIA");
			Collection<ArticuloRelacionDTO> recetaOriginal = new ArrayList<ArticuloRelacionDTO>();
			//se realiza la clonaci\u00F3n de cada item
			for(Iterator <ArticuloRelacionDTO> it = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator(); it.hasNext(); ){
				ArticuloRelacionDTO recetaArticuloDTO = it.next();
				//recetaOriginal.add(recetaArticuloDTO.clone());
				recetaOriginal.add((ArticuloRelacionDTO)SerializationUtils.clone(recetaArticuloDTO));
			}
			//se guarda la receta original
			detallePedidoDTO.getArticuloDTO().setRecetaArticulosOriginal(recetaOriginal);
		}

		//se eliminan problemas de stock, alcance, etc. de las canastas de cat\u00E1logo
		eliminarProblemasRecetaCatalogoPorProblemasEnItems(detallePedidoDTO, request);
	}
	
	/**
	 * 
	 * @param detallePedidoDTO
	 * @param request
	 */
	public static void verificarProblemasEnRecetaPorDatosSIC(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request){
		
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		String estadoDeBaja = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja");
		
		boolean problemasStock = false;
		boolean problemasAlcance = false;
		boolean problemasDeBaja = false;
		boolean problemasInactivoSIC = false;
		
		//se itera la receta final para establecer los valores de stock, alcance y precios
		for(Iterator <ArticuloRelacionDTO> it = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator(); it.hasNext(); ){
			ArticuloRelacionDTO recetaArticuloDTO = it.next();

			//si el art\u00EDculo no es implemento
			//jmena if(recetaArticuloDTO.getArticulo().getNpImplemento().equals(estadoInactivo)){
				//STOCK se compara con la cantidad inicial de los canastos
				if(recetaArticuloDTO.getArticuloRelacion().getNpStockArticulo()!=null 
						&& (recetaArticuloDTO.getArticuloRelacion().getNpStockArticulo().longValue() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()* recetaArticuloDTO.getCantidad())){
					recetaArticuloDTO.getArticuloRelacion().setNpEstadoStockArticulo(estadoInactivo);
					problemasStock = true;
				}
				//ALCANCE
				if(recetaArticuloDTO.getArticuloRelacion().getNpAlcance()!=null
						&& recetaArticuloDTO.getArticuloRelacion().getNpAlcance().equals(estadoInactivo)){
					problemasAlcance = true;
				}
				
				//ESTADO (de baja/inactivo)
				if(recetaArticuloDTO.getArticuloRelacion().getNpEstadoArticuloSIC()!=null){
					if(recetaArticuloDTO.getArticuloRelacion().getNpEstadoArticuloSIC().equals(estadoDeBaja))
						problemasDeBaja = true;
					else if(recetaArticuloDTO.getArticuloRelacion().getNpEstadoArticuloSIC().equals(estadoInactivo))
						problemasInactivoSIC = true;
				}
			//}
		}

		if(problemasStock)
			//se cambia el estado del stock en el art\u00EDculo principal por problemas en los items de la receta
			detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticuloReceta(estadoInactivo);
		else
			detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticuloReceta(estadoActivo);

		if(problemasAlcance)
			//se cambia el estado del alcance en el art\u00EDculo principal por problemas en los items de la receta
			detallePedidoDTO.getArticuloDTO().setNpAlcanceReceta(estadoInactivo);
		else
			detallePedidoDTO.getArticuloDTO().setNpAlcanceReceta(estadoActivo);

		if(problemasDeBaja){
			detallePedidoDTO.getArticuloDTO().setNpEstadoArticuloSICReceta(estadoDeBaja);
		}else if(problemasInactivoSIC)
			detallePedidoDTO.getArticuloDTO().setNpEstadoArticuloSICReceta(estadoInactivo);
		else{
			detallePedidoDTO.getArticuloDTO().setNpEstadoArticuloSICReceta(estadoActivo);
		}
	}
	
	/**
	 * 
	 * @param detallePedidoDTO
	 * @param request
	 * @throws Exception
	 */
	public static void eliminarProblemasRecetaCatalogoPorProblemasEnItems(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request)throws Exception{
		
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//se obtiene la clasificaci\u00F3n para las recetas de cat\u00E1logo
		String clasificacionRecetaCatalogo = obtenerClasificacionRecetaCatalogo(request);
		
		//este control adicional sobre el estado en el SIC solo se debe realizar para los canastos que son de cat\u00E1logo que no hayan sufrido modificaci\u00F3n alguna
		//o que sea el canasto de cotizaciones, en resumen los canastos de cat\u00E1logo nunca deben tener problemas si los items tienen problemas
		if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(clasificacionRecetaCatalogo)
				&& detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() == null
				&& detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoInactivo)){
			detallePedidoDTO.getArticuloDTO().setNpAlcanceReceta(estadoActivo);
			detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticuloReceta(estadoActivo);
			detallePedidoDTO.getArticuloDTO().setNpEstadoArticuloSICReceta(estadoActivo);
		}
	}
	
	/**
	 * Verifica si un establecimiento est\u00E1 habilitado para realizar el cambio de precios.
	 * 
	 * @param  codigoFormatoNegocio	- C\u00F3digo del formato de negocio actual
	 * @param  request								- Peticion HTTP
	 * @throws Exception
	 */
	public static void verificarFormatoNegocioParaCambioPrecios(Integer codigoFormatoNegocio, HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		String codigosFormatosNegocio = (String)session.getAttribute(CotizarReservarAction.COD_FORMATOS_NEGOCIO_CAMBIO_PRECIOS);
		//se obtiene el par\u00E1metro que indica los formatos de negocio habilitados para cambiar el precio
		if(codigosFormatosNegocio == null){
			ParametroDTO parametroFormatosNegocioCP = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosFormatosNegocioHabilitadosCambiarPrecio", request);
			if(parametroFormatosNegocioCP.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.COD_FORMATOS_NEGOCIO_CAMBIO_PRECIOS, parametroFormatosNegocioCP.getValorParametro());
				codigosFormatosNegocio = parametroFormatosNegocioCP.getValorParametro();
			}
		}

		//se verifica si existen los c\u00F3digos
		if(codigosFormatosNegocio != null){
			codigosFormatosNegocio = "," + codigosFormatosNegocio + ",";
			//se verifica si el establecimiento actual est\u00E1 en la lista
			if(codigosFormatosNegocio.indexOf("," + codigoFormatoNegocio + ",") >= 0){
				session.setAttribute(HABILITADO_CAMBIO_PRECIOS, "OK");
			}else{
				session.removeAttribute(HABILITADO_CAMBIO_PRECIOS);
			}
			LogSISPE.getLog().info("codigo actual formato de negocio: {}",codigoFormatoNegocio);
			LogSISPE.getLog().info("codigos formatos de negocio: {}",codigosFormatosNegocio);
		}
	}
	
  /**
   * Determina el valor total de los descuentos por caja 
   * 
   * @param request												- La petici\u00F3n HTTP
   * @param colDetallePedidoDTO						- Colecci\u00F3n del detalle del pedido
   */
  public static ActionMessage calcularValoresFinalesPedido(HttpServletRequest request, Collection <DetallePedidoDTO> colDetallePedidoDTO, CotizarReservarForm cotizarReservarForm) throws Exception
  {
	int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
	AutorizacionesUtil.agregarAutorizacionSimilaresDescVar(request, (ArrayList<DetallePedidoDTO>) colDetallePedidoDTO);  
  	HttpSession session = request.getSession();
  	String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
  	String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
  	VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
  	boolean errorDescuentoCaja = false;
  	//se inicializan los subtotales
  	double subTotalPedidoAplicaIVA = 0;
  	double subTotalPedidoNoAplicaIVA= 0;
  	
  	double descuentoTotalCajas = 0;
  	double descuentoTotalMayorista = 0;
  	double valorTotalDescuentoCajas = 0;
  	double valorTotalDescuentoMayorista = 0;
  	
	//se inicializan los subtotales
  	double subTotalDescuentosIVA = 0;
  	double subTotalDescuentosNoIVA = 0;
	
  	ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
	String codCanCat = parametroDTO.getValorParametro();
	
	parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
	String codClaRecetasNuevas = parametroDTO.getValorParametro();
	
	//parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoCanastoCotizacionesVacio", request);
	String codCanCotVacios = SessionManagerSISPE.getCodigoCanastoVacio(request);
	
	//parametro precios Temp
	String calcularCanastosPreciosTemp=null;
	if(vistaPedidoDTO!=null){
		calcularCanastosPreciosTemp=vistaPedidoDTO.getObsmigperTemp();
	}
	
	//se desactivan los descuentos por caja y mayorista de acuerdo al pedido
//	session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
//	session.setAttribute( CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA , "NO");
	obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
	String llaveDescuentoPorCajas = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
	String llaveDescuentoMayorista = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
//	Collection<String> llaveDescuentoCol = (Collection<String>) session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS); 
	String[] llaveDescuentoCol = (String[]) session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
	if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean) session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
		String[] llavesDescuentosSeleccionados = (String[]) session.getAttribute(CotizarReservarAction.RESPALDO_OP_DESCUENTOS);
		if(llavesDescuentosSeleccionados != null && llavesDescuentosSeleccionados.length > 0){
			llaveDescuentoCol = llavesDescuentosSeleccionados;
		}
	}
	
	if(llaveDescuentoCol != null && llaveDescuentoCol.length > 0  && llaveDescuentoPorCajas != null && llaveDescuentoMayorista != null){
		for(String llaveActual : llaveDescuentoCol){
			if(llaveActual != null && llaveActual.contains(llaveDescuentoPorCajas)){
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
				LogSISPE.getLog().info("descuento por cajas habilitado ");
			} else if(llaveActual != null && llaveActual.contains(llaveDescuentoMayorista)){
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
				LogSISPE.getLog().info("descuento mayorista habilitado ");
			}
		}
	}
	
  	//se recorre la colecci\u00F3n del detalle
  	int indice = 0;
  	cotizarReservarForm.setSubTotalBrutoNoAplicaIva(0D);
  	for(DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO){
  		//se asignan los nuevos valores para el POS
 		ajustarValoresPOS(detallePedidoDTO, accionActual, request,cotizarReservarForm,indice,codClaRecetasNuevas,codCanCotVacios,codCanCat,calcularCanastosPreciosTemp);
 		if(esLocalActivoParaPrecioMayorista(SessionManagerSISPE.getCurrentLocal(request), detallePedidoDTO.getArticuloDTO(), request)){
 			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
 		  		//--- se verifica si existe un art\u00EDculo con descuento de mayorista ----
 		  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista()!= null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista().doubleValue() > 0){
 		  			descuentoTotalMayorista = descuentoTotalMayorista + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista().doubleValue();
 		  			valorTotalDescuentoMayorista = valorTotalDescuentoMayorista + detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
 		  		}
 	 		}
 	  		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
 				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta()!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta() > 0){
 					descuentoTotalMayorista += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta().doubleValue();
 					valorTotalDescuentoMayorista += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
 				}
 	  		}
 		}else{
 			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
 		  		//--- se verifica si existe un art\u00EDculo con descuento por precio de caja ----
 		  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja()!= null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja().doubleValue() > 0){
 		  			descuentoTotalCajas = descuentoTotalCajas + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja().doubleValue();
 		  			valorTotalDescuentoCajas = valorTotalDescuentoCajas + detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
 		  		}
 	 		}
 	 		
 	  		
 	  		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
 		  		//se verifican los posibles descuentos por caja o mayorista en una receta
 				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta()!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta() > 0){
 					descuentoTotalCajas += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta().doubleValue();
 					valorTotalDescuentoCajas += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
 				}
 	  		}
 		}
 		
  		
		if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null 
				&& !detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty() 
				&& (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanCat))
				&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null){
			//calcular el valor con iva y sin iva de los canastos para sacar el valor total
			for(ArticuloRelacionDTO recetaArticuloCanCotVac : (Collection<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
				//subTotalPedidoNoAplicaIVA =subTotalPedidoNoAplicaIVA+recetaArticuloCanCotVac.getValorTotalEstado();
				//valorTotalNeto=valorTotalNeto+(recetaArticuloCanCotVac.getValorTotalEstado()-recetaArticuloCanCotVac.getValorTotalEstadoDescuento());
				if(recetaArticuloCanCotVac.getArticuloRelacion().getAplicaImpuestoVenta()){
					subTotalPedidoAplicaIVA=subTotalPedidoAplicaIVA+SICArticuloCalculo.getInstancia().calcularValorConImpuestos((recetaArticuloCanCotVac.getValorTotalEstado()-(recetaArticuloCanCotVac.getValorTotalEstadoDescuento()==null?0D:recetaArticuloCanCotVac.getValorTotalEstadoDescuento())), crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
					//subTotalPedidoAplicaIVA =subTotalPedidoAplicaIVA+SICArticuloCalculo.getInstancia().calcularValorConImpuestos(recetaArticuloCanCotVac.getValorTotalEstado(), crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
				}else{
					subTotalPedidoNoAplicaIVA=subTotalPedidoNoAplicaIVA+(recetaArticuloCanCotVac.getValorTotalEstado()-(recetaArticuloCanCotVac.getValorTotalEstadoDescuento()==null?0D:recetaArticuloCanCotVac.getValorTotalEstadoDescuento()));
//					valorTotalImpuestoEstado =valorTotalImpuestoEstado+recetaArticuloCanCotVac.getValorTotalEstado();
//					subTotalPedidoNoAplicaIVA =subTotalPedidoNoAplicaIVA+recetaArticuloCanCotVac.getValorTotalEstadoNeto();
				}
				
			}
			
		}else{
			//se verifica si el art\u00EDculo tiene IVA
	  		if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
	  			subTotalDescuentosIVA +=detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
	  			subTotalPedidoAplicaIVA += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
	  		}else{
	  			subTotalDescuentosNoIVA +=detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
	  			subTotalPedidoNoAplicaIVA += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
	  		}
		}
  		
  		//si este valor es menor a cero significa que hay errores en los descuentos por caja porque el precio o la unidad de manejo es erronea
  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento().doubleValue() < 0){
  			errorDescuentoCaja = true;
  		}
  		if(esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
  			indice++;
  		}
  	}
  	
  	cotizarReservarForm.setTotalDescuentoIva(Util.roundDoubleMath(subTotalDescuentosIVA+subTotalDescuentosNoIVA,numeroDecimales));
  	
		//se obtiene la colecci\u00F3n de descuentos de la sesi\u00F3n
		Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
		if (colDescuentoEstadoPedidoDTO != null){
			Collection<DescuentoEstadoPedidoDTO> colEliminar = new ArrayList<DescuentoEstadoPedidoDTO>();
			for(DescuentoEstadoPedidoDTO descuentoEstadoPedido : colDescuentoEstadoPedidoDTO ){
				LogSISPE.getLog().info("***Llave descuento(calcularValoresFinalesPedido): {} - {}", descuentoEstadoPedido.getLlaveDescuento(), descuentoEstadoPedido.getId().getCodigoPedido());
				//se verifica si ya existe un descuento incluido por art\u00EDculo
				if(descuentoEstadoPedido.getEsAplicadoAutomaticamente()!=null && descuentoEstadoPedido.getEsAplicadoAutomaticamente().equals(estadoActivo)){
					colEliminar.add(descuentoEstadoPedido);
					LogSISPE.getLog().info("***Llave descuento(calcularValoresFinalesPedido, colEliminar): {}", descuentoEstadoPedido.getLlaveDescuento());
				}
			}
			colDescuentoEstadoPedidoDTO.removeAll(colEliminar);
			colEliminar = null;
		}
		else 
			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
		
		
		//se llama al m\u00E9todo que crea el nuevo tipo de descuento
		colDescuentoEstadoPedidoDTO = CotizacionReservacionUtil.crearDescuentoIncluidoPorArticulo(descuentoTotalCajas, descuentoTotalMayorista, valorTotalDescuentoCajas, valorTotalDescuentoMayorista,
				colDescuentoEstadoPedidoDTO, request);
  	
		//se valida si es pedido consolidado y si existen descuento por cajas habilitado
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean) session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
			
			//agregar el descuento con valor cero para el caso de pedidos consolidados
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
				//se crea el objeto para consultar el descuento que se va a agregar
		  		DescuentoDTO descuentoFiltro = new DescuentoDTO();
		  		descuentoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  		descuentoFiltro.setTipoDescuentoDTO(new TipoDescuentoDTO());
			//se crea el objeto final
				if(descuentoTotalCajas == 0){
					descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"));
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoCajas);
					descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoCajas);
					descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalCajas, NUMERO_DECIMALES));
					descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
			  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
			  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
			  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
			  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
			  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
			  		
			  		if(colDescuentoEstadoPedidoDTO == null){
			  			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
			  		}
			  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
			  		colDescuentoEstadoPedidoDTO.add(descuentoEstadoPedidoDTO);
				}
			}
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
				//se crea el objeto para consultar el descuento que se va a agregar
		  		DescuentoDTO descuentoFiltro = new DescuentoDTO();
		  		descuentoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  		descuentoFiltro.setTipoDescuentoDTO(new TipoDescuentoDTO());
				if(descuentoTotalMayorista == 0){
					descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"));
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoMayorista);
					descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoMayorista);
					descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalMayorista, NUMERO_DECIMALES));
					descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
			  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
			  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
			  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
			  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
			  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
			  		
			  		if(colDescuentoEstadoPedidoDTO == null){
			  			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
			  		}
			  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
			  		colDescuentoEstadoPedidoDTO.add(descuentoEstadoPedidoDTO);
				}
			}
		}
		
		request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS, colDescuentoEstadoPedidoDTO == null ? null : colDescuentoEstadoPedidoDTO.size() > 0 ? colDescuentoEstadoPedidoDTO : null);
	
		Double valorDesc=0D;
		for(DescuentoEstadoPedidoDTO descuentoEstadoPedido : colDescuentoEstadoPedidoDTO ){
			 valorDesc+= descuentoEstadoPedido.getValorDescuento();
		}
		
		cotizarReservarForm.setTotalDescuentoIva(valorDesc);
	 	//se actualizan los totales del pedido
	  	calcularTotalesPedido(request, cotizarReservarForm, subTotalPedidoAplicaIVA, subTotalPedidoNoAplicaIVA);
		
		/*Cambios Oscar*/
		verificaCantidadBonosMaxiNavidad(colDescuentoEstadoPedidoDTO, request,null);
		
  	//se elimina la variable que determinaba si se agregaron art\u00EDculos al pedido por la b\u00FAsqueda
  	session.removeAttribute(BuscarArticuloAction.ART_AGREGADOS_BUSQUEDA_PED);
  	
  	if(errorDescuentoCaja)
  		return new ActionMessage("errors.descuentoCajas");
  	
  	return null;
  }
  

  /**
   * 
   * @param request
   * @param session
   * @throws Exception
   */
public static void verificarSiAplicaPreciosCajaMayorista(HttpServletRequest request, HttpSession session) throws Exception {
	obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
	String llaveDescuentoPorCajas = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
	String llaveDescuentoMayorista = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
	//Collection<String> llaveDescuentoCol = (Collection<String>) session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS); 
	String[] llaveDescuentoCol = (String[]) session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS); 
	if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean) session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
		String[] llavesDescuentosSeleccionados = (String[]) session.getAttribute(CotizarReservarAction.RESPALDO_OP_DESCUENTOS);
		if(llavesDescuentosSeleccionados != null && llavesDescuentosSeleccionados.length > 0){
			llaveDescuentoCol = llavesDescuentosSeleccionados;
		}
	}
	Boolean aplicaDesCajas=Boolean.FALSE;
	Boolean aplicaDesMayorista=Boolean.FALSE;
	if(llaveDescuentoCol != null && llaveDescuentoCol.length > 0 && llaveDescuentoPorCajas != null && llaveDescuentoMayorista != null){
		for(String llaveActual : llaveDescuentoCol){
			if(llaveActual != null && llaveActual.contains(llaveDescuentoPorCajas)){
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
				aplicaDesCajas=Boolean.TRUE;
				LogSISPE.getLog().info("descuento por cajas habilitado ");
			}else if(!aplicaDesCajas){
				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
			}
				
			if(llaveActual != null && llaveActual.contains(llaveDescuentoMayorista)){
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
				aplicaDesMayorista=Boolean.TRUE;
				LogSISPE.getLog().info("descuento mayorista habilitado ");
			}else if(!aplicaDesMayorista){
				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
			}
		}
	}
}

  
  /**
   * 
   * @param descuentoTotalCajas
   * @param descuentoTotalMayorista
   * @param valorTotalDescuentoCajas
   * @param valorTotalDescuentoMayorista
   * @param colDescuentoEstadoPedidoDTO
   * @param request
   */
  public static Collection<DescuentoEstadoPedidoDTO> crearDescuentoIncluidoPorArticulo(double descuentoTotalCajas, double descuentoTotalMayorista, double valorTotalDescuentoCajas,
  		double valorTotalDescuentoMayorista, Collection<DescuentoEstadoPedidoDTO> colDesEstPedDTO, HttpServletRequest request)throws Exception{
  	
	  Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = colDesEstPedDTO;
	  //si existe un descuento, solamente se crea un tipo de descuento adicional (por caja o mayorista, no ambos)
  	if(descuentoTotalCajas > 0 || descuentoTotalMayorista > 0){
  		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
  		Double totalDescuentoPorArticulo = 0D;
  		//se crea el objeto para consultar el descuento que se va a agregar
  		DescuentoDTO descuentoFiltro = new DescuentoDTO();
  		descuentoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
  		descuentoFiltro.setTipoDescuentoDTO(new TipoDescuentoDTO());
  		
  		//se crea el objeto final
  		if(descuentoTotalCajas > 0){
  			descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"));
  			DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
  			descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoCajas);
  			descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoCajas);
  			descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalCajas, NUMERO_DECIMALES));
  			descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
  	  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
  	  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
  	  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
  	  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
  	  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
  	  		
  	  		if(colDescuentoEstadoPedidoDTO == null){
  	  			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
  	  		}
  	  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
  	  		colDescuentoEstadoPedidoDTO.add(descuentoEstadoPedidoDTO);
  	  		totalDescuentoPorArticulo += descuentoEstadoPedidoDTO.getValorDescuento();
  		}
  		if(descuentoTotalMayorista > 0){
  			descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"));
  			DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
  			descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoMayorista);
  			descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoMayorista);
  			descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalMayorista, NUMERO_DECIMALES));
  			descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
  	  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
  	  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
  	  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
  	  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
  	  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
  	  		
  	  		if(colDescuentoEstadoPedidoDTO == null){
  	  			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
  	  		}
  	  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
  	  		colDescuentoEstadoPedidoDTO.add(descuentoEstadoPedidoDTO);
  	  		totalDescuentoPorArticulo += descuentoEstadoPedidoDTO.getValorDescuento();
  		}
  		
  		request.getSession().setAttribute(TOTAL_DESCUENTO_POR_ARTICULO, totalDescuentoPorArticulo);
  	}else{
  		request.getSession().removeAttribute(TOTAL_DESCUENTO_POR_ARTICULO);
  	}
  	
  	return colDescuentoEstadoPedidoDTO;
  }
  
  /**
   * Se calculan los valores del detalle que se mostrar\u00E1n en pantalla para los ajustes con el POS
   * 
   * @param detallePedidoDTO
   * @param accionActual
   * @param request
   */
  public static void ajustarValoresPOS(DetallePedidoDTO detallePedidoDTO, String accionActual, HttpServletRequest request, CotizarReservarForm cotizarReservarForm, Integer indice, String codClaRecetasNuevas,String codCanCotVacios,String codCanCat,String calcularCanastosPreciosTemp)throws Exception{
  	
  	double precioUnitarioIVA = 0;
   	double precioUnitario = 0;
   	
   	Boolean aplicaPrecioMayorista = Boolean.FALSE; 
   	
  	String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
  	//se inicializan los precios
  	if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){

  			precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().doubleValue();		
			precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().doubleValue();	
			
		   	if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"))){		   		
				precioUnitarioIVA = 0;
			   	precioUnitario = 0;
				for(ArticuloRelacionDTO articulo : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){					
					precioUnitario = precioUnitario + (articulo.getArticuloRelacion().getPrecioBase()*articulo.getCantidad().longValue());
					precioUnitarioIVA = precioUnitarioIVA + (articulo.getArticuloRelacion().getPrecioBaseImp()*articulo.getCantidad().longValue());
				}
			}
			
			if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
				precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVANoAfiliado().doubleValue();
				precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().doubleValue();
				if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null){		   		
					precioUnitarioIVA = 0;
				   	precioUnitario = 0;
					for(ArticuloRelacionDTO articulo : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){						
						precioUnitario = precioUnitario + (articulo.getArticuloRelacion().getPrecioBaseNoAfi()*articulo.getCantidad().longValue());
						precioUnitarioIVA = precioUnitarioIVA + (articulo.getArticuloRelacion().getPrecioBaseNoAfiImp()*articulo.getCantidad().longValue());
					}
				}
			}
  	}else{
  		precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().doubleValue();
  		
  		if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null){		   		
			precioUnitarioIVA = 0;
			for(ArticuloRelacionDTO articulo : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){	
				precioUnitarioIVA = precioUnitarioIVA + (articulo.getArticuloRelacion().getPrecioBaseImp()*articulo.getCantidad().longValue());
			}
		}
  		
		if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
			precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().doubleValue();
			if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null){		   		
				precioUnitarioIVA = 0;
				for(ArticuloRelacionDTO articulo : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){	
					precioUnitarioIVA = precioUnitarioIVA + (articulo.getArticuloRelacion().getPrecioBaseNoAfiImp()*articulo.getCantidad().longValue());
				}
			}
		}		
		precioUnitario = precioUnitarioIVA;
  	}
  	//redondear precios calculados
  	precioUnitarioIVA = Util.roundDoubleMath(Double.valueOf(precioUnitarioIVA), NUMERO_DECIMALES);
   	precioUnitario = Util.roundDoubleMath(Double.valueOf(precioUnitario), NUMERO_DECIMALES);
  	
	//--------- se ajusta cada uno de los valores del detalle por motivos de venta en el POS --------
  	double valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitario;
  	double valorPrevioVentaIVA =0D;
  	valorPrevioVentaIVA=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
  	
  	Boolean aplicaIVA = true;
  	if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null){	
	  	for(ArticuloRelacionDTO articulo : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){	
	  		if(!articulo.getArticuloRelacion().getAplicaImpuestoVenta() || articulo.getArticuloRelacion().getPrecioBase()==0){
	  			aplicaIVA=false;
	  			detallePedidoDTO.setNpAplicaImpuestoCanastoEspecial(false);
	  		}
	  		else if(articulo.getArticuloRelacion().getPrecioBase() > 0 && articulo.getArticuloRelacion().getAplicaImpuestoVenta()){
	  			aplicaIVA=true;
	  			detallePedidoDTO.setNpAplicaImpuestoCanastoEspecial(true);
	  			break;
	  		}
		}
  	}
  	  	
  	if(calcularCanastosPreciosTemp==null){
  		if(!aplicaIVA){
  	  		valorPrevioVentaIVA = 0D;
  	  		valorPrevioVentaIVA = valorPrevioVenta;
  	  	}
  		else if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
  			detallePedidoDTO.setNpAplicaImpuestoCanastoEspecial(true);
//	  		valorPrevioVentaIVA=SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);//detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
	  		valorPrevioVentaIVA = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
	  	}
  	}

	Double valorTotalBrutoSinIva=0D;
	Double valorBrutoSinIva=0D;
	Double valorTotalPrevio=0D;
	
		//se verifica si el art\u00EDculo es de peso variable
		if(esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
			//se verifica la acci\u00F3n actual
			if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)
					&& accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
				//si es desde la pantalla de confirmaci\u00F3n de pesos
				valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitario;
				valorPrevioVentaIVA =  detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitarioIVA;
				if(calcularCanastosPreciosTemp==null){
					if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//						valorPrevioVentaIVA =  SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);//detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitarioIVA;
						valorPrevioVentaIVA = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
					}
				}
			}else{
				//si es desde otra pantalla
				valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitario;
				valorPrevioVentaIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitarioIVA;
				if(calcularCanastosPreciosTemp==null){
					if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//						valorPrevioVentaIVA = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, (VALOR_PORCENTAJE_IVA*100));// Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);//detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitarioIVA;
						valorPrevioVentaIVA = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
					}
				}
			}
		}
		
		//se asigna el valor previo venta
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(Util.roundDoubleMath(Double.valueOf(valorPrevioVentaIVA), NUMERO_DECIMALES));
		valorPrevioVenta = Util.roundDoubleMath(valorPrevioVenta, NUMERO_DECIMALES);
		if(cotizarReservarForm!=null){
				valorTotalPrevio=cotizarReservarForm.getSubTotalBrutoNoAplicaIva()==null?0D:cotizarReservarForm.getSubTotalBrutoNoAplicaIva();
				if(calcularCanastosPreciosTemp!=null 
						&& detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta() 
						&& detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) 
						&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null
						&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codCanCotVacios)){
					valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVentaIVA / (1 + VALOR_PORCENTAJE_IVA),NUMERO_DECIMALES);
					valorTotalBrutoSinIva=valorTotalPrevio+valorBrutoSinIva;
				}
				else{
					valorTotalBrutoSinIva=valorTotalPrevio+valorPrevioVenta;
					valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVenta,NUMERO_DECIMALES);
				}
				cotizarReservarForm.setSubTotalBrutoNoAplicaIva(Util.roundDoubleMath(valorTotalBrutoSinIva, NUMERO_DECIMALES));
		}else{
			LogSISPE.getLog().info("CALCULO DE VALOR BRUTO SIN IVA PARA PEDIDOS CONSOLIDADO");
			if(calcularCanastosPreciosTemp!=null 
					&& detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta() 
					&& detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) 
					&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null
					&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codCanCotVacios)){
				valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVentaIVA / (1 + VALOR_PORCENTAJE_IVA),NUMERO_DECIMALES);
			}
			else{
				valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVenta,NUMERO_DECIMALES);
			}
		}
			
		double descuentoPrincipal = 0;
		double descuentoReceta = 0;
		detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoCaja(null);
		detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoMayorista(null);
		
		//si existe un pedido consolidado
		Long cantidadEstadoConsolidadoCaja = 0L;
		if(request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null 
				&& ((String)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedioGeneral") == null || (request.getParameter(Globals.AYUDA) != null && request.getParameter(Globals.AYUDA).equals("guardarConsolidacion")))){
			List<DetallePedidoDTO> detallesPedidoConsolidado = (List<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			if(!detallesPedidoConsolidado.isEmpty()){
				for(DetallePedidoDTO detallePedidoEncontrado : detallesPedidoConsolidado){
					
					if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoEncontrado.getId().getCodigoArticulo())){
						cantidadEstadoConsolidadoCaja += detallePedidoEncontrado.getEstadoDetallePedidoDTO().getCantidadEstado();
					}
				}
			}
		}
		LogSISPE.getLog().info("**CAJA ajustarValoresPOS cantidadEstadoConsolidadoCaja(): {}", cantidadEstadoConsolidadoCaja);
		
		//se valida si el detalle aplica precio caja/mayorista
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioCaja() == null){
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioCaja(ConstantesGenerales.ESTADO_INACTIVO);
		}
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioMayorista() == null){
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioMayorista(ConstantesGenerales.ESTADO_INACTIVO);
		}
		
		if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()){
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioCaja(ConstantesGenerales.ESTADO_ACTIVO);
		}
		
		if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()){
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioMayorista(ConstantesGenerales.ESTADO_ACTIVO);
			aplicaPrecioMayorista = Boolean.TRUE;
		}
		
		//para realizar el calculo de los descuentos incluidos por art\u00EDculo
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
			if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()
					&& detallePedidoDTO.getArticuloDTO().getUnidadManejoPrecioCaja() <= (cantidadEstadoConsolidadoCaja.longValue() > 0 ? cantidadEstadoConsolidadoCaja : detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado())){
				//si es un art\u00EDculo con precio de caja, se guarda el valor del descuento por caja del detalle
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoCaja(obtenerDescuentoIncluidoPorArticulo(valorPrevioVenta, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado()));
				descuentoPrincipal = detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja();
			}
		}
		
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
			if(aplicaPrecioMayorista){
				//si el mayoreo est\u00E1 activo para el art\u00EDculo
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoMayorista(obtenerDescuentoIncluidoPorArticulo(valorPrevioVenta, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado()));
				descuentoPrincipal = detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista();
			}
		}
		
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
			//se verifica el descuento en receta
			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta() != null){
				descuentoReceta += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta().doubleValue();
			}
		}
		
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta() != null){
				descuentoReceta += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta().doubleValue();
			}
		}
		
		//se calcula el descuento principal
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstadoDescuento() != null){
			descuentoPrincipal = descuentoPrincipal + detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstadoDescuento().doubleValue();
		}
		//se asigna el descuento final
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(
				Util.roundDoubleMath(descuentoPrincipal + descuentoReceta, NUMERO_DECIMALES));
		
		LogSISPE.getLog().info("::::descuentoPrincipal: {}, descuentoReceta: {}", descuentoPrincipal, descuentoReceta);
		
		double valorUnitarioVenta = 0;
		double valorNetoPrevio =0;
		//if(descuentoPrincipal > 0 || esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento() > 0 || esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
			if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null 
					&& !detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty() 
					&& (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanCat))
					&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null){
					//&& detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas)
				 	//ESTAS LINEAS SE COMENTAN PORQUE NO DEBE VALIDAR SOLO CANASTOS DE COTIZACIONES SINO TODOS LOS ESPECIALES INCLUSO LOS QUE SE CONVIERTEN COMO LOS DE CATALOGO
					//&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codCanCotVacios)){
				Double valorTotalEstado =0D;
				Long cantidadDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();

				
				
				if(calcularCanastosPreciosTemp==null){
					LogSISPE.getLog().info("CALCULO DE COTIZACIONES CON METODO NUEVO");
//					for(ArticuloRelacionDTO recetaArticuloCanCotVac : (Collection<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
//						if(recetaArticuloCanCotVac.getArticuloRelacion().getHabilitadoPrecioCaja() && 
//								esArticuloActivoParaPrecioCaja(codigoLocal, recetaArticuloCanCotVac.getArticuloRelacion().getPrecioCaja(), recetaArticuloCanCotVac.getArticuloRelacion(), request)){
//							double precioUnitarioCajas = recetaArticuloCanCotVac.getArticuloRelacion().getPrecioBase();
//							valorTotalEstado =recetaArticuloCanCotVac.getCantidad()*precioUnitarioCajas;
//							valorTotalEstado = Util.roundDoubleMath(Double.valueOf(valorTotalEstado), NUMERO_DECIMALES);
//						}
//						else
//							if(recetaArticuloCanCotVac.getArticuloRelacion().getHabilitadoPrecioMayoreo()
//									&& cantidadDetalle >= recetaArticuloCanCotVac.getArticuloRelacion().getCantidadMayoreo()){
//								double precioUnitarioMayorista =  recetaArticuloCanCotVac.getArticuloRelacion().getPrecioBase();
//								valorTotalEstado =recetaArticuloCanCotVac.getCantidad()*precioUnitarioMayorista;
//								valorTotalEstado = Util.roundDoubleMath(Double.valueOf(valorTotalEstado), NUMERO_DECIMALES);
//							}
//							else{
//								double precioBaseReceta =  recetaArticuloCanCotVac.getArticuloRelacion().getPrecioBase();
//								valorTotalEstado =recetaArticuloCanCotVac.getCantidad()*precioBaseReceta;
//								valorTotalEstado = Util.roundDoubleMath(Double.valueOf(valorTotalEstado), NUMERO_DECIMALES);
//							}
//						nuevoPrecioUnitario+=valorTotalEstado;
//					}
					
//					Double valorFinalDescuento=detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
//					Double valorNetoCanCot=nuevoPrecioUnitario*cantidadDetalle;
//					valorNetoCanCot=Util.roundDoubleMath(Double.valueOf(valorNetoCanCot), NUMERO_DECIMALES);
////					Double valorIVANetoCanCot=Util.roundDoubleMath(Double.valueOf(valorNetoCanCot * (1 + VALOR_PORCENTAJE_IVA)), NUMERO_DECIMALES);
//					Double valorNetoCanCotSinDesc=valorNetoCanCot-valorFinalDescuento;	
//					//se calcula el valor unitario Neto
//					valorUnitarioVenta =Util.roundDoubleMath(Double.valueOf(nuevoPrecioUnitario), NUMERO_DECIMALES);
//					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(valorUnitarioVenta);
//					if(!aplicaIVA){
//			  	  		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(valorUnitarioVenta);
//			  	  		valorNetoPrevio = valorNetoCanCotSinDesc;
//			  	  	}else{
//			  	  		precioUnitarioIVA = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(nuevoPrecioUnitario, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
//			  	  		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(precioUnitarioIVA);
//			  	  		valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoCanCotSinDesc, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
//			  	  	}
//					//se calcula el valor unitario Neto
//					valorUnitarioVenta = Util.roundDoubleMath(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado(), NUMERO_DECIMALES);
					
					Double valorTotalImpuestoEstado =0D;
					Double valorTotalNeto =0D;
					Double valorTotalImpuestoNeto =0D;
					Double valorTotalEstadoDescuento =0D;
					
					for(ArticuloRelacionDTO recetaArticuloCanCotVac : (Collection<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
						valorTotalEstado =valorTotalEstado+recetaArticuloCanCotVac.getValorTotalEstado();
						valorTotalEstadoDescuento=recetaArticuloCanCotVac.getValorTotalEstadoDescuento()==null?0D:recetaArticuloCanCotVac.getValorTotalEstadoDescuento();
						valorTotalNeto=valorTotalNeto+(recetaArticuloCanCotVac.getValorTotalEstado()-valorTotalEstadoDescuento);
						if(recetaArticuloCanCotVac.getArticuloRelacion().getAplicaImpuestoVenta()){
							valorTotalImpuestoNeto=valorTotalImpuestoNeto+SICArticuloCalculo.getInstancia().calcularValorConImpuestos((recetaArticuloCanCotVac.getValorTotalEstado()-valorTotalEstadoDescuento), crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
							valorTotalImpuestoEstado =valorTotalImpuestoEstado+SICArticuloCalculo.getInstancia().calcularValorConImpuestos(recetaArticuloCanCotVac.getValorTotalEstado(), crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
						}else{
							valorTotalImpuestoNeto=valorTotalImpuestoNeto+(recetaArticuloCanCotVac.getValorTotalEstado()-valorTotalEstadoDescuento);
							valorTotalImpuestoEstado =valorTotalImpuestoEstado+recetaArticuloCanCotVac.getValorTotalEstado();
						}
						
					}
					
					//detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(Util.roundDoubleMath(Double.valueOf(valorTotalImpuestoEstado), NUMERO_DECIMALES));
					
					if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
						valorUnitarioVenta = precioUnitarioIVA;
						valorNetoPrevio = valorTotalImpuestoNeto;
					}else{
						valorUnitarioVenta = precioUnitario;
						valorNetoPrevio = valorTotalNeto;
					}
					//se suman los valores de descuento de cajas y mayoristas ya que al momento de relizar la sumatorias en el proceso anterior estos ya se encuentran restados
					Double valorFinalDescuentoCajaMayorista=detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta()==null?0:detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta();
					valorFinalDescuentoCajaMayorista+=detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta()==null?0:detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta();
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(Util.roundDoubleMath(Double.valueOf(valorTotalImpuestoEstado+valorFinalDescuentoCajaMayorista), NUMERO_DECIMALES));
					
					LogSISPE.getLog().info("::::valorUnitarioVenta: {}",valorUnitarioVenta);
				}else{
					LogSISPE.getLog().info("CALCULO DE COTIZACIONES CON METODO ANTERIOR");
			          precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().doubleValue();
			          Double valorFinalDescuento = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
			          Double valorIVANetoCanCot = Double.valueOf(precioUnitarioIVA * cantidadDetalle.longValue());
			          valorIVANetoCanCot = Util.roundDoubleMath(Double.valueOf(valorIVANetoCanCot.doubleValue()), NUMERO_DECIMALES);
			          Double valorNetoCanCot = Util.roundDoubleMath(Double.valueOf(valorIVANetoCanCot.doubleValue() / (1 + VALOR_PORCENTAJE_IVA)), NUMERO_DECIMALES);
			          Double valorNetoCanCotSinDesc = Double.valueOf(valorNetoCanCot.doubleValue() - valorFinalDescuento.doubleValue());

			          valorUnitarioVenta = Util.roundDoubleMath(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado(), NUMERO_DECIMALES).doubleValue();
//			          valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoCanCotSinDesc, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(Double.valueOf(valorNetoCanCotSinDesc.doubleValue() * (1 + VALOR_PORCENTAJE_IVA)), NUMERO_DECIMALES).doubleValue();
			          valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoCanCotSinDesc, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
			          LogSISPE.getLog().info("::::valorNetoPrevio: {}", Double.valueOf(valorNetoPrevio));
				}
			}
			else{
				
					LogSISPE.getLog().info("AJUSTE POS 1");
					LogSISPE.getLog().info("::::valorPrevioVenta: {}, ValorFinalEstadoDescuento: {}", valorPrevioVenta, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento());
					//se calcula el valor neto intermedio
					//double valorNetoPrevio = valorPrevioVenta - descuentoPrincipal;
					//valorNetoPrevio = valorPrevioVenta - detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
					valorNetoPrevio = valorBrutoSinIva - detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento();
					//si el art\u00EDculo tiene IVA y los calculos se realizan con IVA
					if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
						if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()){
							valorUnitarioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayoristaIVA();
//							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorNetoPrevio * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);
							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
							if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
								valorUnitarioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayoristaIVANoAfiliado();
							}
						}
						else{
//							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorNetoPrevio * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);
							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
							//se calcula el valor unitario Neto
							if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
								valorUnitarioVenta = precioUnitarioIVA;
							}else{
								valorUnitarioVenta = precioUnitario;
							}
						}
					}
					else{
						//se calcula el valor unitario Neto
						if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorNetoPrevio * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);
							valorNetoPrevio = SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorNetoPrevio, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
							valorUnitarioVenta = precioUnitarioIVA;
						}else{
							valorUnitarioVenta = precioUnitario;
							valorNetoPrevio = Util.roundDoubleMath(valorNetoPrevio, NUMERO_DECIMALES);
						}
					}
					LogSISPE.getLog().info("::::valorUnitarioVenta: {}",valorUnitarioVenta);
			}
		}else{
			LogSISPE.getLog().info("AJUSTE POS 2");
			if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null 
					&& !detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty() 
					&& (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanCat))
					&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null){
				LogSISPE.getLog().info("CALCULO DE COTIZACIONES");
				Double valorTotalEstado =0D;
				Double valorTotalImpuestoEstado =0D;
				Double valorItem =0D;
				Double valorImpuestoItem =0D;
				Long cantidadDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();

				//Integer codigoLocal = (Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
				for(ArticuloRelacionDTO recetaArticuloCanCotVac : (Collection<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
					double precioBaseReceta =  recetaArticuloCanCotVac.getArticuloRelacion().getPrecioBase();
					valorItem =recetaArticuloCanCotVac.getCantidad()*precioBaseReceta*cantidadDetalle.longValue();
					valorTotalEstado =valorTotalEstado+ Util.roundDoubleMath(Double.valueOf(valorItem), NUMERO_DECIMALES);
					
					if(recetaArticuloCanCotVac.getArticuloRelacion().getAplicaImpuestoVenta()){
						valorImpuestoItem =recetaArticuloCanCotVac.getCantidad()*precioBaseReceta*cantidadDetalle.longValue();
						valorImpuestoItem =SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorImpuestoItem, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
						valorTotalImpuestoEstado =valorTotalImpuestoEstado+ Util.roundDoubleMath(Double.valueOf(valorImpuestoItem), NUMERO_DECIMALES);
					}else{
						valorImpuestoItem =recetaArticuloCanCotVac.getCantidad()*precioBaseReceta*cantidadDetalle.longValue();
						valorTotalImpuestoEstado =valorTotalImpuestoEstado+ Util.roundDoubleMath(Double.valueOf(valorImpuestoItem), NUMERO_DECIMALES);
					}
				}
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(Util.roundDoubleMath(Double.valueOf(valorTotalImpuestoEstado), NUMERO_DECIMALES));
				
				if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
					valorUnitarioVenta = precioUnitarioIVA;
					valorNetoPrevio = valorTotalImpuestoEstado;
				}else{
					valorUnitarioVenta = precioUnitario;
					valorNetoPrevio = valorTotalEstado;
				}
				
			}else{
				if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo) && detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
					valorUnitarioVenta = precioUnitarioIVA;
					valorNetoPrevio = valorPrevioVentaIVA;
				}else{
					valorUnitarioVenta = precioUnitario;
					valorNetoPrevio = valorPrevioVenta;
				}
			}
		}
		
		if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas")) ){					
			if(!aplicaIVA){
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(precioUnitario);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(precioUnitario);
	  	  	}else{
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(precioUnitario);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(precioUnitarioIVA);
	  	  	}
		}		
		
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(Util.roundDoubleMath(Double.valueOf(valorUnitarioVenta), NUMERO_DECIMALES));
		//se asigna el valor de venta final
		double valorVentaFinal = valorNetoPrevio;//detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioPOS().doubleValue() * detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
		LogSISPE.getLog().info("::::valorVenta Final: {}",valorVentaFinal);
		detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(Util.roundDoubleMath(Double.valueOf(valorVentaFinal), NUMERO_DECIMALES));

  }
  
  /**
   * 
   * @param detallePedidoDTO
   * @return
   */
  private static double obtenerDescuentoIncluidoPorArticulo(Double valorTotalNormal, Double valorTotalReal){
  	//se resta el valor total normal (cantidad * valorUnitario) del valor total que se obtubo anteriormente calculado en base a reglas del negocio (tomando en cuenta precios de caja o mayorista)
		double descuentoItem = valorTotalNormal.doubleValue() - valorTotalReal.doubleValue();
		return Util.roundDoubleMath(Double.valueOf(descuentoItem), NUMERO_DECIMALES);
  }
  
  /**
   * 
   * @param request
   * @param cotizarReservarForm
   * @param subTotalAplicaIVA
   * @param subTotalNoAplicaIVA
   * @throws Exception
   */
  public static void calcularTotalesPedido(HttpServletRequest request, CotizarReservarForm cotizarReservarForm, double subTotalAplicaIVA, 
  		double subTotalNoAplicaIVA)throws Exception{
  	
  	HttpSession session = request.getSession();
  	String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
  	
  	//se realizan algunos c\u00E1lculos y se asigna el resulatado a las variables del formulario
	cotizarReservarForm.setSubTotal(Double.valueOf(subTotalAplicaIVA + subTotalNoAplicaIVA));
  	cotizarReservarForm.setSubTotalNetoBruto(Double.valueOf(cotizarReservarForm.getSubTotalBrutoNoAplicaIva().doubleValue()-cotizarReservarForm.getTotalDescuentoIva().doubleValue()));// subTotalAplicaIVA + subTotalNoAplicaIVA));
  	cotizarReservarForm.setSubTotalNoAplicaIVA(subTotalNoAplicaIVA);
  	if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
  		//CALCULOS NORMALES
	  	//se calcula el valor sin IVA del subTotalAplicaIVA
	  	double subTotalAplicaIVASinIVA = subTotalAplicaIVA / (1 + Double.valueOf(SessionManagerSISPE.getValorIVA(request)).doubleValue());
	  	subTotalAplicaIVASinIVA = Util.roundDoubleMath(Double.valueOf(subTotalAplicaIVASinIVA), NUMERO_DECIMALES);
	  	cotizarReservarForm.setSubTotalAplicaIVA(Double.valueOf(subTotalAplicaIVASinIVA));
	  	double valorIVA = subTotalAplicaIVA - subTotalAplicaIVASinIVA;
	  	cotizarReservarForm.setIvaTotal(valorIVA);
	  	cotizarReservarForm.setTotal(cotizarReservarForm.getSubTotal());
  	}else{
  		//CALCULOS SIN IVA
  		cotizarReservarForm.setSubTotalNoAplicaIVA(subTotalNoAplicaIVA + subTotalAplicaIVA); //se guarda en este campo porque los c\u00E1lculos se hicieron sin iva
  		cotizarReservarForm.setSubTotalAplicaIVA(0D);
	  	cotizarReservarForm.setIvaTotal(0D);
	  	cotizarReservarForm.setTotal(cotizarReservarForm.getSubTotal());
  	}
  	
//		//solo cuando es una reservaci\u00F3n se va a cumplir la condici\u00F3n
//		Collection costoEntregasDTOCol = (Collection)session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
//		//esta variable la obtengo de la clase EntregaLocalCalendarioAction, indica si la forma de realizar las entregas
//		//a domicilio es por sector o no
//		String entregaDomicilioPorSector = (String)session.getAttribute("ec.com.smx.sic.sispe.entregaDirLocal");
//		if(costoEntregasDTOCol!=null && entregaDomicilioPorSector!=null){//no ingresa cuado se modifica reserva l aprimera vez
//			double valorTotalEntregas = 0;
//			for(Iterator it = costoEntregasDTOCol.iterator(); it.hasNext(); ){
//				CostoEntregasDTO costoEntregasDTO = (CostoEntregasDTO)it.next();
//				costoEntregasDTO.setValor(WebSISPEUtil.costoEntrega(cotizarReservarForm.getTotal(), costoEntregasDTO.getPorcentajeCostoFlete()));
//				valorTotalEntregas = valorTotalEntregas + costoEntregasDTO.getValor();
//			}
//			session.setAttribute(EntregaLocalCalendarioAction.VALORTOTALENTREGA, Double.valueOf(valorTotalEntregas));
//		}
		
		//se actualiza el valor total del pedido
		cotizarReservarForm.actualizarCostoTotalPedido(request);

  	//se guardan en sesi\u00F3n los totales del pedido
  	session.setAttribute(CotizarReservarAction.SUB_TOTAL_APLICA_IVA, cotizarReservarForm.getSubTotalAplicaIVA());
  	session.setAttribute(CotizarReservarAction.SUB_TOTAL_NOAPLICA_IVA, cotizarReservarForm.getSubTotalNoAplicaIVA());
  	session.setAttribute(CotizarReservarAction.TOTAL_PEDIDO, cotizarReservarForm.getTotal());
  }
  
	
	/**
	 * Realiza el control de precios cuando se va a recotizar, reservar o confirmar los pesos finales
	 * 
	 * @param vistaDetallePedidoDTO		-	Objeto que representa un <code>VistaDetallePedidoDTO</code>
	 * @param estadoDetallePedidoDTO	- Objeto que representa un <code>EstadoDetallePedidoDTO</code>
	 * @param request									- La petici\u00F3n HTTP
	 * @return												[true: si el precio almacenado fue alterado, false: si el precio almacenado no fue alterado]
	 */
	public static boolean controlPrecios(VistaDetallePedidoDTO vistaDetallePedidoDTO, EstadoDetallePedidoDTO estadoDetallePedidoDTO, HttpServletRequest request)throws Exception{
		boolean existioCambioPrecio = false;

		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		//precios actuales
		Double precioUnitarioActual = Util.roundDoubleMath(vistaDetallePedidoDTO.getArticuloDTO().getPrecioBase(), 2);
		Double precioCajaActual = Util.roundDoubleMath(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()?vistaDetallePedidoDTO.getArticuloDTO().getPrecioCaja():0, 2);
		Double precioMayorista = Util.roundDoubleMath(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()?vistaDetallePedidoDTO.getArticuloDTO().getPrecioMayorista():0, 2);
		
		//precios del estado
		Double precioUnitarioEstado = Util.roundDoubleMath(vistaDetallePedidoDTO.getValorUnitarioEstado(), 2);
		Double precioCajaEstado = Util.roundDoubleMath(vistaDetallePedidoDTO.getValorCajaEstado(), 2);
		Double precioMayoristaEstado = Util.roundDoubleMath(vistaDetallePedidoDTO.getValorMayoristaEstado(), 2);
		
		//si no es un canasto de cotizaciones
		if(!estadoDetallePedidoDTO.getEstadoCanCotVacio().equals(estadoActivo)){
			//se deben comparar los precios unitarios para determinar si existen cambios
			if(precioUnitarioActual.doubleValue() != precioUnitarioEstado.doubleValue()
					|| precioCajaActual.doubleValue() != precioCajaEstado.doubleValue()
					|| precioMayorista.doubleValue() != precioMayoristaEstado.doubleValue()
					|| (!vistaDetallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta() && vistaDetallePedidoDTO.getValorUnitarioEstado().doubleValue()
							!= vistaDetallePedidoDTO.getValorUnitarioIVAEstado().doubleValue())){
				
				//asignacionPreciosActuales(vistaDetallePedidoDTO, estadoDetallePedidoDTO, SessionManagerSISPE.getEstadoActivo(request));
				existioCambioPrecio = true;
			}
		}
		
		//se asignan siempre los precios del estado anterior
		asignacionPreciosEstadoActual(vistaDetallePedidoDTO, estadoDetallePedidoDTO, existioCambioPrecio, request);

		return existioCambioPrecio;
	}
	
	/**
	 * Realiza la asignaci\u00F3n de los precios del estado anterior
	 * @param vistaDetallePedidoDTO		-	Objeto que representa un <code>VistaDetallePedidoDTO</code>	
	 * @param estadoDetallePedidoDTO	- Objeto que representa un <code>EstadoDetallePedidoDTO</code>
	 * @param precioActualDiferente   - Indica si el precio actual fue modificado o no
	 * @param estadoActivo     				- Indica un estado activo [1: activo]
	 */
	private static void asignacionPreciosEstadoActual(VistaDetallePedidoDTO vistaDetallePedidoDTO, 
			EstadoDetallePedidoDTO estadoDetallePedidoDTO, 
			boolean precioActualDiferente, HttpServletRequest request)throws Exception{
		
		Double porcentajePreciosAfiliado = WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request);
		
		LogSISPE.getLog().info("**se asignan los precios del estado anterior**");
		//PRECIOS DE AFILIADO
		//valores unitarios
		estadoDetallePedidoDTO.setValorUnitarioEstado(vistaDetallePedidoDTO.getValorUnitarioEstado());
		estadoDetallePedidoDTO.setValorUnitarioIVAEstado(vistaDetallePedidoDTO.getValorUnitarioIVAEstado());
		//valores de caja
		estadoDetallePedidoDTO.setValorCajaEstado(vistaDetallePedidoDTO.getValorCajaEstado());
		estadoDetallePedidoDTO.setValorCajaIVAEstado(vistaDetallePedidoDTO.getValorCajaIVAEstado());
		//valores de mayorista
		estadoDetallePedidoDTO.setValorMayorista(vistaDetallePedidoDTO.getValorMayoristaEstado());
		estadoDetallePedidoDTO.setValorMayoristaIVA(vistaDetallePedidoDTO.getValorMayoristaIVAEstado());
		
		//PRECIOS NO AFILIADO
		//se calculan los precios de no afiliado si el valor es cero
		if(vistaDetallePedidoDTO.getValorUnitarioNoAfiliado().doubleValue() == 0){
			vistaDetallePedidoDTO.setValorUnitarioNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorUnitarioEstado(), porcentajePreciosAfiliado));
			vistaDetallePedidoDTO.setValorUnitarioIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorUnitarioIVAEstado(), porcentajePreciosAfiliado));
			vistaDetallePedidoDTO.setValorCajaNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorCajaEstado(), porcentajePreciosAfiliado));
			vistaDetallePedidoDTO.setValorCajaIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorCajaIVAEstado(), porcentajePreciosAfiliado));
			vistaDetallePedidoDTO.setValorMayoristaNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorMayoristaEstado(), porcentajePreciosAfiliado));
			vistaDetallePedidoDTO.setValorMayoristaIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(vistaDetallePedidoDTO.getValorMayoristaIVAEstado(), porcentajePreciosAfiliado));
		}
		//valores unitarios
		estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(vistaDetallePedidoDTO.getValorUnitarioNoAfiliado());
		estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(vistaDetallePedidoDTO.getValorUnitarioIVANoAfiliado());
		//valores de caja 
		estadoDetallePedidoDTO.setValorCajaNoAfiliado(vistaDetallePedidoDTO.getValorCajaNoAfiliado());
		estadoDetallePedidoDTO.setValorCajaIVANoAfiliado(vistaDetallePedidoDTO.getValorCajaIVANoAfiliado());
		//valores de mayorista
		estadoDetallePedidoDTO.setValorMayoristaNoAfiliado(vistaDetallePedidoDTO.getValorMayoristaNoAfiliado());
		estadoDetallePedidoDTO.setValorMayoristaIVANoAfiliado(vistaDetallePedidoDTO.getValorMayoristaIVANoAfiliado());
		
		//se inicializan los valores originales
		estadoDetallePedidoDTO.setValorUnitarioEstadoOriginal(vistaDetallePedidoDTO.getValorUnitarioEstado());
		estadoDetallePedidoDTO.setValorUnitarioIVAEstadoOriginal(vistaDetallePedidoDTO.getValorUnitarioIVAEstado());
		estadoDetallePedidoDTO.setValorCajaEstadoOriginal(vistaDetallePedidoDTO.getValorCajaEstado());
		estadoDetallePedidoDTO.setValorCajaIVAEstadoOriginal(vistaDetallePedidoDTO.getValorCajaIVAEstado());
		
		//se verifica si hubo un cambio en el precio actual en relaci\u00F3n al estado anterior
		if(precioActualDiferente){
			//ESTE SEGMENTO REALIZA UN RESPALDO DE LOS PRECIOS ACTUALES, PARA UTILIZARLOS EN LOS CALCULOS DE LA RESERVA
			//PARA EL CAMBIO DE PRECIOS POR DECISION DEL USUARIO
			
			//se construye el objeto para realiza el c\u00E1lculo
			EstadoDetallePedidoDTO edpCalculoPrecios = new EstadoDetallePedidoDTO();
			edpCalculoPrecios.setValorUnitarioEstado(vistaDetallePedidoDTO.getArticuloDTO().getPrecioBase());
			edpCalculoPrecios.setValorUnitarioIVAEstado(vistaDetallePedidoDTO.getArticuloDTO().getPrecioBaseImp());
			edpCalculoPrecios.setValorCajaEstado(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioCaja():0);
			edpCalculoPrecios.setValorCajaIVAEstado(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioCajaImp():0);
			edpCalculoPrecios.setValorMayorista(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioMayorista():0);
			edpCalculoPrecios.setValorMayoristaIVA(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioMayoristaImp():0);
			
			//se llama a la funci\u00F3n que calcula los precios de No Afiliado
			//WebSISPEUtil.calcularPreciosNoAfiliados(request, edpCalculoPrecios);
			
			estadoDetallePedidoDTO.setNpValorUnitarioActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getPrecioBase(), edpCalculoPrecios.getValorUnitarioNoAfiliado()});
			estadoDetallePedidoDTO.setNpValorCajaActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioCaja():0, edpCalculoPrecios.getValorCajaNoAfiliado()});
			estadoDetallePedidoDTO.setNpValorMayoristaActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioMayorista():0, edpCalculoPrecios.getValorMayoristaNoAfiliado()});
			
			//cuando se asigna el precio actual se realiza la siguiente verificaci\u00F3n
			if(vistaDetallePedidoDTO.getAplicaIVA().equals(SessionManagerSISPE.getEstadoActivo(request))){
				estadoDetallePedidoDTO.setNpValorUnitarioIVAActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getPrecioBaseImp(), edpCalculoPrecios.getValorUnitarioIVANoAfiliado()});
				estadoDetallePedidoDTO.setNpValorCajaIVAActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioCajaImp():0, edpCalculoPrecios.getValorCajaIVANoAfiliado()});
				estadoDetallePedidoDTO.setNpValorMayoristaIVAActual(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getArticuloDTO().getPrecioMayoristaImp():0, edpCalculoPrecios.getValorMayoristaIVANoAfiliado()});
			}else{
				//se asignan los precios sin iva
				estadoDetallePedidoDTO.setNpValorUnitarioIVAActual(estadoDetallePedidoDTO.getNpValorUnitarioActual());
				estadoDetallePedidoDTO.setNpValorCajaIVAActual(estadoDetallePedidoDTO.getNpValorCajaActual());
				estadoDetallePedidoDTO.setNpValorMayoristaIVAActual(estadoDetallePedidoDTO.getNpValorMayoristaActual());
			}

			//se respaldan los precios del estado anterior
			estadoDetallePedidoDTO.setNpValorUnitarioEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getValorUnitarioEstado(), vistaDetallePedidoDTO.getValorUnitarioNoAfiliado()});
			estadoDetallePedidoDTO.setNpValorUnitarioIVAEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getValorUnitarioIVAEstado(), vistaDetallePedidoDTO.getValorUnitarioIVANoAfiliado()});
			estadoDetallePedidoDTO.setNpValorCajaEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getValorCajaEstado():0, vistaDetallePedidoDTO.getValorCajaNoAfiliado()});
			estadoDetallePedidoDTO.setNpValorCajaIVAEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja()? vistaDetallePedidoDTO.getValorCajaIVAEstado():0, vistaDetallePedidoDTO.getValorCajaIVANoAfiliado()});
			estadoDetallePedidoDTO.setNpValorMayoristaEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getValorMayoristaEstado():0, vistaDetallePedidoDTO.getValorMayoristaNoAfiliado()});
			estadoDetallePedidoDTO.setNpValorMayoristaIVAEstadoAnterior(new Double [] {vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()? vistaDetallePedidoDTO.getValorMayoristaIVAEstado():0, vistaDetallePedidoDTO.getValorMayoristaIVANoAfiliado()});

			edpCalculoPrecios = null;
		}
	}
	
	/**
	 * Procesa los descuentos para el pedido y en caso de ser necesario vuelve a calcular sus valores
	 * 
	 * @param vistaPedidoDTO
	 * @param detallePedido
	 * @param actualizarDescuento
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static DescuentoEstadoPedidoDTO procesarDescuentos(VistaPedidoDTO vistaPedidoDTO, Collection<DetallePedidoDTO> colDetallePedidoDTO,
			boolean actualizarDescuento,final HttpServletRequest request) throws Exception{

		HttpSession session = request.getSession();
		//se inicializa el objeto resultado
		DescuentoEstadoPedidoDTO descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
		descuentoEstadoPedidoTotalDTO.setPorcentajeDescuento(0d);
		descuentoEstadoPedidoTotalDTO.setValorDescuento(0d);
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//se inicializan las sumatorias
		double valorDescuentoTotal = 0;
		double porcentajeTotalDescuento = 0;

		//se eliminan los descuentos
		SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(colDetallePedidoDTO,null, null);
		
		//se obtienen los descuentos en caso de ser necesario
		//esta condici\u00F3n significa que en el estado anterior hubieron descuentos
		if(vistaPedidoDTO!=null && vistaPedidoDTO.getDescuentoTotalPedido().doubleValue() > 0){

//			if(vistaPedidoDTO.getDescuentosEstadosPedidos()==null || vistaPedidoDTO.getDescuentosEstadosPedidos().isEmpty()){
				//llamada a la funci\u00F3n que obtiene los descuentos
				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
//			}

			//se verifica la existencia de una colecci\u00F3n de descuentos
			if(vistaPedidoDTO.getDescuentosEstadosPedidos()!=null && !vistaPedidoDTO.getDescuentosEstadosPedidos().isEmpty())
			{
				String codigoTipoDescuentoVariable = "";
				//se obtiene el c\u00F3digo del tipo de descuento variable
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
				if(parametroDTO.getValorParametro()!=null){
					codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
				}
//				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request);
				//se obtiene la colecci\u00F3n de los estados del descuentos
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = vistaPedidoDTO.getDescuentosEstadosPedidos();
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
				//colecciones que se enviar\u00E1n al m\u00E9todo que calcula los descuentos
				Collection<DescuentoDTO> descuentoVariableCol = new ArrayList<DescuentoDTO>();
				Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
				//llave descuentos actuales
				Collection<String> llavesActualesAplicados = new ArrayList<String>();
				
				for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : colDescuentoEstadoPedidoDTOVariables){
					
					LogSISPE.getLog().info("[codigoTipoDescuento]: {}",descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento());
					//se verifica el c\u00F3digo del descuento variable
					if(descuentoEstadoPedidoDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento())){
						//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
						//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
						descuentoEstadoPedidoDTO.getDescuentoDTO().setPorcentajeDescuento(descuentoEstadoPedidoDTO.getPorcentajeDescuento());
						descuentoVariableCol.add(descuentoEstadoPedidoDTO.getDescuentoDTO());
						LogSISPE.getLog().info("llave descuento variable agregada: {}", descuentoEstadoPedidoDTO.getLlaveDescuento());
					}
					
					//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
					if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
						llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
					}
					LogSISPE.getLog().info("LLAVE: {}",descuentoEstadoPedidoDTO.getLlaveDescuento());
				}
				
				if(!llavesDescuentosAplicados.isEmpty()){
					
					//ELIMINAR DESCUENTOS DE CAJAS Y MAYORISTAS PARA QUE NO APLIQUE A LA FUNCION DE DSTOS
					if(llavesDescuentosAplicados!=null){
						Collection<String> llavesSinCajas=CollectionUtils.select(llavesDescuentosAplicados, new Predicate() {
							public boolean evaluate(Object arg0) {
								String llaveEncontar=(String)arg0;
								return  !llaveEncontar.equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS)));
							}
						});
						
						Collection<String> llavesActuales=CollectionUtils.select(llavesSinCajas, new Predicate() {
							public boolean evaluate(Object arg0) {
								String llaveEncontar=(String)arg0;
								return  !llaveEncontar.equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA)));
							}
						});
						llavesDescuentosAplicados=new ArrayList<String>(); 
						llavesDescuentosAplicados=(Collection<String>)SerializationUtils.clone((Serializable)llavesActuales);
					}
					
					
					//se generan las llaves para setearlas en opDescuentos del formulario
					Collection<TipoDescuentoDTO> tipoDescuento = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);					
					String[] opDescuentosAux = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					String[] opDescuentos = opDescuentosAux;
					
					if(request.getSession().getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS) == null){
					//if(opDescuentos == null || opDescuentos.length == 0){
						//se arman las llaves opDescuentos
//						String[] opDescuentos;
						int posVector = 0;
						int posDecuento = 0;
						//se copias las llaves que ya estan seteadas
						if(opDescuentosAux != null && opDescuentosAux.length > 0){
							 opDescuentos = new String[llavesDescuentosAplicados.size()+opDescuentosAux.length];
							 for(int i=0; i<opDescuentosAux.length; i++){
								 if(opDescuentosAux[i] != null){
									 opDescuentos[posVector] = opDescuentosAux[i];
									 posVector++;
								 }
							 }
						} 
						else
							opDescuentos = new String[llavesDescuentosAplicados.size()];
						
						if(CollectionUtils.isNotEmpty(tipoDescuento)){
							for(TipoDescuentoDTO tipoDctoActual : tipoDescuento){
								for(String llaveActual : llavesDescuentosAplicados){
									String nuevaLlave = llaveActual;
									String codigoTipoDescuento = llaveActual.split(SEPARADOR_TOKEN)[1].split("CTD")[1];
									if(tipoDctoActual.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento)){
										nuevaLlave =  nuevaLlave.replace(llaveActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, ""+posDecuento+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO);
										opDescuentos[posVector] = nuevaLlave;
										llavesActualesAplicados.add(nuevaLlave);
										posVector++;
									}
								}
								posDecuento++;
							}
						}
					 }
					
					//}
					request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
					//porcentajeTotalDescuento=0;
					LogSISPE.getLog().info("precio alterado: {}",actualizarDescuento);
					
					//se procesan los descuentos variables
					colDescuentoEstadoPedidoDTO = DescuentosUtil.procesarDescuentos(request, colDetallePedidoDTO, llavesDescuentosAplicados, colDescuentoEstadoPedidoDTOVariables); 
					
					colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>)colDescuentoEstadoPedidoDTO;
					
					if(colDescuentoEstadoPedidoDTOVariables!=null && !colDescuentoEstadoPedidoDTOVariables.isEmpty()){
						LogSISPE.getLog().info("descuentos: {}",colDescuentoEstadoPedidoDTOVariables.size());
						//si se alteraron los precios se itera la colecci\u00F3n de descuentos para calcular el total de descuentos
						for(Iterator <DescuentoEstadoPedidoDTO> it = colDescuentoEstadoPedidoDTOVariables.iterator();it.hasNext();){
							DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = it.next();
							valorDescuentoTotal = valorDescuentoTotal + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							porcentajeTotalDescuento = porcentajeTotalDescuento + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
						}
					}
				}else{
					valorDescuentoTotal = vistaPedidoDTO.getDescuentoTotalPedido().doubleValue();
					porcentajeTotalDescuento = vistaPedidoDTO.getPorcentajeTotalDescuento().doubleValue();
				}
				LogSISPE.getLog().info("porcentajeTotalDescuento: {}",porcentajeTotalDescuento);
				session.setAttribute(CotizarReservarAction.COL_DESCUENTOS, colDescuentoEstadoPedidoDTOVariables);
				
				//Verifica el numero de bonos-Maxi solo si es reservacion - modificacionReserva
				if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))||
						accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))|| 
						accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion"))|| 
						accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion"))|| 
						accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho"))|| 
						accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega"))){
					//berifica numero/bonos-maxi-navidad
					CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad(colDescuentoEstadoPedidoDTOVariables, request,null);
				}
				
				//se guarda en sesi\u00F3n las llaves de los descuentos, es decir los desuentos seleccionados
				session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llavesActualesAplicados);
				session.setAttribute(CotizarReservarAction.COL_DESC_VARIABLES,descuentoVariableCol);
			}
		}
		//se almacena el descuento total y el descuento para los art\u00EDculos con IVA
		session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(valorDescuentoTotal));
		//se guarda en sesi\u00F3n el porcentaje total de los descuentos
		session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(porcentajeTotalDescuento));
		//se actualiza el objeto
		descuentoEstadoPedidoTotalDTO.setPorcentajeDescuento(Double.valueOf(porcentajeTotalDescuento));
		descuentoEstadoPedidoTotalDTO.setValorDescuento(Double.valueOf(valorDescuentoTotal));	

		return descuentoEstadoPedidoTotalDTO;
	}
	
	/**
	 * Compara el detalle de la receta original con el actual para verificar cambios
	 * @param recetaOriginal
	 * @param recetaActual
	 * @param articuloDTO
	 */
	public static boolean verificarDiferenciasConRecetaOriginal(DetallePedidoDTO detallePedidoDTO,	HttpServletRequest request)throws Exception
	{
		//las colecciones a comparar no deben ser nulas
		if(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal()!=null && detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null){

			LogSISPE.getLog().info("tamano original: {}",detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal().size());
			LogSISPE.getLog().info("tamano actual: {}",detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().size());
			//variable para determinar si las colecciones son diferentes
			boolean hayDiferenciasConRecetaOriginal = false;
			
			if(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal().size() == detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().size()){
				//primero se ordenan las colecciones para realizar la comparaci\u00F3n
				Collection<ArticuloRelacionDTO> recetaOrdenadaOriginal = ColeccionesUtil.sort(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal(), ColeccionesUtil.ORDEN_ASC, "id.codigoArticuloRelacionado");
				Collection<ArticuloRelacionDTO> recetaOrdenadaActual = ColeccionesUtil.sort(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol(), ColeccionesUtil.ORDEN_ASC, "id.codigoArticuloRelacionado");
				//se iteran las colecciones para realizar la comparaci\u00F3n
				Iterator <ArticuloRelacionDTO> it1 = recetaOrdenadaOriginal.iterator();
				for(Iterator <ArticuloRelacionDTO> it2 = recetaOrdenadaActual.iterator();it2.hasNext();){
					ArticuloRelacionDTO receta1 = it1.next();
					ArticuloRelacionDTO receta2 = it2.next();
					if(!receta1.getId().getCodigoArticuloRelacionado().equals(receta2.getId().getCodigoArticuloRelacionado())){//getCodigoArticulo()
						hayDiferenciasConRecetaOriginal = true;
						break;
					}else if(!receta1.getCantidad().equals(receta2.getCantidad())){
						hayDiferenciasConRecetaOriginal = true;
						break;
					}
				}
			}else
				hayDiferenciasConRecetaOriginal = true;

			LogSISPE.getLog().info("diferencias con receta original: {}",hayDiferenciasConRecetaOriginal);

			if(!hayDiferenciasConRecetaOriginal){
				//se borra el nuevo c\u00F3digo de clasificaci\u00F3n temporal
				detallePedidoDTO.getArticuloDTO().setNpNuevoCodigoClasificacion(null);
				eliminarProblemasRecetaCatalogoPorProblemasEnItems(detallePedidoDTO, request);
			}else{
				//si las colecciones son diferentes significa que si se deben tomar en cuenta los alcances reales
				//adem\u00E1s la receta pertenecer\u00E1 a una nueva clasificaci\u00F3n
				
				//seteo el codigo original de la canasta en el nuevo campo .
				if(detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal() == null){
					LogSISPE.getLog().info("::::::::Codigo Articulo Orignal null:::::: ->{}",detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
					detallePedidoDTO.getArticuloDTO().setCodigoArticuloOriginal(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
				} else{
					LogSISPE.getLog().info("::::::::Codigo Articulo Orignal not null:::::: ->{}",detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
					detallePedidoDTO.getArticuloDTO().setCodigoArticuloOriginal(detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal());
				}
				//se obtiene la clasificaci\u00F3n para las nuevas recetas
				String clasificacionRecetasNuevas = (String)request.getSession().getAttribute("ec.com.smx.sic.sispe.clasificacion.nuevaReceta");
				if(clasificacionRecetasNuevas==null){
					//se obtiene el par\u00E1metro para calcular el m\u00E1ximo de modificaciones
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
					clasificacionRecetasNuevas = parametroDTO.getValorParametro();
					request.getSession().setAttribute("ec.com.smx.sic.sispe.clasificacion.nuevaReceta", clasificacionRecetasNuevas);
				}
				//se asigna la nueva clasificaci\u00F3n temporal
				detallePedidoDTO.getArticuloDTO().setNpNuevoCodigoClasificacion(clasificacionRecetasNuevas);
				detallePedidoDTO.setNpExisteCambioCanastoEspecial(Boolean.TRUE);
			}
			
			return hayDiferenciasConRecetaOriginal;
		}
		
		return false;
	}
	
	/**
	 * Verifica si un canasto de cotizacion ingresado es igual a un canasto de cat\u00E1logo
	 * @param detallePedidoDTO
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean verificarCanastoExistente(DetallePedidoDTO detallePedidoDTO,	HttpServletRequest request,String codigoClasificacionTipoCanasto)throws Exception{
		Boolean existeCanasto=false;
		VistaRecetaArticuloDTO vistaRecetaArticuloConsultaDTO =  new VistaRecetaArticuloDTO();
		Collection<VistaRecetaArticuloDTO> vistasRecetaArticulo = new ArrayList<VistaRecetaArticuloDTO>();
		
		//crea la clave para el nuevo art\u00EDculo
		final String claveRecetaArticulo = UtilesSISPE.obtenerClaveReceta(detallePedidoDTO.getArticuloDTO(),vistaRecetaArticuloConsultaDTO);
		
		vistaRecetaArticuloConsultaDTO.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());
		vistaRecetaArticuloConsultaDTO.setClaveRecetaArticulo(claveRecetaArticulo);
		vistasRecetaArticulo = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaRecetaArticulo((vistaRecetaArticuloConsultaDTO));
		
		VistaRecetaArticuloDTO vistaRecetaArticulo= (VistaRecetaArticuloDTO)CollectionUtils.find(vistasRecetaArticulo, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				// TODO Auto-generated method stub
				VistaRecetaArticuloDTO obj= (VistaRecetaArticuloDTO)object;
				return obj.getClaveRecetaArticulo().equals(claveRecetaArticulo);
			}
		});
		
			//valida si la clave generada coincide con el canasto de catalogo y si es un canasto de catalogo, si es nulo significa que no econtro
			if(vistaRecetaArticulo!=null && vistaRecetaArticulo.getClaveRecetaArticulo().equals(claveRecetaArticulo) && 
					vistaRecetaArticulo.getCodigoClasificacion().equals(codigoClasificacionTipoCanasto)){
				existeCanasto = true;
				//asigna la descripcion del art\u00EDculo a la varaiable de sesion
				request.getSession().setAttribute(DetalleCanastaAction.CANASTA_EXISTENTE,vistaRecetaArticulo.getDescripcionArticulo());
				request.getSession().setAttribute(DetalleCanastaAction.VISTADETALLE_CANASTAESPECIAL,vistaRecetaArticulo);
			}
		
		return existeCanasto;
	}
	
	
	/**
	 * Verifica mediante una consulta al esquema corporativo si una empresa est\u00E1 exenta de pagar IVA mediante el RUC
	 * @param request
	 * @param rucEmpresa
	 * @throws Exception
	 */
	public static void verificarEmpresaExentaIVA(HttpServletRequest request, String rucEmpresa, String contextoPedido)throws Exception{
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		//si es un pedido de tipo empresarial
		if(contextoPedido!= null && contextoPedido.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"))
				&& rucEmpresa!=null && !rucEmpresa.trim().equals("")){
			LogSISPE.getLog().info("Se procede a validar rucEmpresa...");
			EmpresaDTO empresaBusqueda= new EmpresaDTO();
			empresaBusqueda.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			empresaBusqueda.setNumeroRuc(rucEmpresa.trim());
			empresaBusqueda.setSujetoPasivoIVA(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo.corporativo"));				
							
			EmpresaDTO empresaResultado = CorporativoFactory.getServiciosService().findEmpresa(empresaBusqueda);
			if(empresaResultado!=null){
				LogSISPE.getLog().info("Estado inactivo");
				request.getSession().setAttribute(CALCULO_PRECIOS_CON_IVA, estadoInactivo);
			}else{
				LogSISPE.getLog().info("Estado activo");
				request.getSession().setAttribute(CALCULO_PRECIOS_CON_IVA, estadoActivo);
			}
		}else{
			request.getSession().setAttribute(CALCULO_PRECIOS_CON_IVA, estadoActivo);
		}
	}
	
	/**
	 * 
	 * @param Collection detallePedidoDTOCol
	 * @throws Exception
	 */
	public static boolean verificarCantidadCanastasEspeciales(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request,String codClaRecetasNuevas, 
			Integer codMinCanastosEspeciales, ActionMessages info) throws Exception{
		
		boolean flag = false;
		boolean cumpleCondicion = false;
		long cantidadTotal = 0;
		
		if(detallePedidoDTOCol != null && !detallePedidoDTOCol.isEmpty()){
			
			String codigoPedido = detallePedidoDTOCol.iterator().next().getId().getCodigoPedido();
			
			//Ingreso de nuevos detalles
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){ 
				//verifico canastas de catalogo
				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || 
						detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
					cantidadTotal = cantidadTotal + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
					flag = true;
				}else{
					if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))){
						cantidadTotal = cantidadTotal +detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
					}else if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue() > 0){
						cantidadTotal = cantidadTotal +UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue(), detallePedidoDTO.getArticuloDTO());
					}
				}
			}
			
			if(cantidadTotal > 0 && cantidadTotal >= codMinCanastosEspeciales.longValue() && flag){
				cumpleCondicion = true;
				
				//si se trata de pedidos consolidados, no es necesario autorizacion porque el pedido cumple con todas las condiciones
				if(ConsolidarAction.esPedidoConsolidado(request)){
					request.getSession().removeAttribute(ConsolidarAction.MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS);
				}
			}else if(ConsolidarAction.esPedidoConsolidado(request)){
				
				//se validan las condiciones de los pedidos consolidados
				cantidadTotal += ConsolidarAction.obtenerEquivalenteEnCanastosConsolidados(request, codigoPedido);
				
				if(cantidadTotal > 0 && cantidadTotal >= codMinCanastosEspeciales.longValue() && flag){
					cumpleCondicion = true;
					info.add("canastos.cosolidados",new ActionMessage("info.canastos.consolidados.elaboran.cd"));
					request.getSession().setAttribute(ConsolidarAction.MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS, Boolean.TRUE);
				}
			}
		}
		return cumpleCondicion;
	}
	
	/**
	 * 
	 * @param Collection detallePedidoDTOCol
	 * @throws Exception
	 */
	public static boolean verificarExistenCanastasEspeciales(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request, String codClaRecetasNuevas, Integer codMinCanastosEspeciales) throws Exception{
		long cantidadTotal = 0;
		Boolean existenCanastosEspeciales=Boolean.FALSE;
		if(detallePedidoDTOCol != null && !detallePedidoDTOCol.isEmpty()){
			//Ingreso de nuevos detalles
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){ 
				//verifico canastas de catalogo
//				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || 
//						detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
//					cantidadTotal = cantidadTotal + detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
//					existenCanastosEspeciales=Boolean.TRUE;
//				}
//				else{
//					cantidadTotal = cantidadTotal +UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue(), detallePedidoDTO.getArticuloDTO());
//				}
				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || 
						detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
					cantidadTotal=detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
					if(cantidadTotal>0){
						existenCanastosEspeciales=Boolean.TRUE;
					}
				}
				
			}
			LogSISPE.getLog().info("Cantidad total de bultos:"+cantidadTotal);
			if(existenCanastosEspeciales){// && cantidadTotal >= codMinCanastosEspeciales){//solo si la cantidad total de canastos por configurar cumple con la condicion m\u00EDnima se muestra la opcion de quien elabora los canastos
				
				//si es un pedido consolidado se muestra la autorizacion
				//para que la bodega elabore los canastos
				if(ConsolidarAction.esPedidoConsolidado(request)){
					request.getSession().setAttribute(ConsolidarAction.MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS, Boolean.TRUE);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param pesoTotal								- Peso total ingresado
	 * @param pesoAproximadoUnidad		- Peso aproximado por art\u00EDculo
	 * @return
	 * @throws Exception
	 */
	public static Long recalcularCantidadPorModificacionPesos(Double pesoTotal, Double pesoAproximadoUnidad){
		if(pesoAproximadoUnidad == null || pesoTotal == null){
			return 0L;
		}
		double cantidadDouble = pesoTotal / pesoAproximadoUnidad;
		return Long.valueOf((long)Math.ceil(cantidadDouble));
	}
	
	/**
	 * 
	 * @param request
	 */
	public static Double obtenerPorcentajeMaximoDescuentoPorEstablecimiento(HttpServletRequest request)throws Exception{
		try{
			//se obtiene el par\u00E1metro desde la base de datos
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentajeDescuentoVariable", request);
			VistaEntidadResponsableDTO entidadResponsable = SessionManagerSISPE.getCurrentEntidadResponsable(request);
			Integer establecimiento = entidadResponsable.getCodigoEstablecimiento();
			if(!entidadResponsable.getTipoEntidadResponsable().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				//se obtiene el establecimiento en base al local seleccionado en la pantalla
				establecimiento = (Integer)request.getSession().getAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA);
			}
			
			//se divide el par\u00E1metro obtenido
			String [] parametro = parametroDTO.getValorParametro().split(",");
			for(int i=0; i<parametro.length; i++){
				//se verifica el c\u00F3digo del establecimiento
				if(Integer.parseInt(parametro[i].split("-")[0]) == establecimiento.intValue()){
					return Double.valueOf(parametro[i].split("-")[1]);
				}
			}
			
			throw new Exception("No se pudo obtener un porcentaje m\u00E1ximo v\u00E1lido de descuento para el formato de negocio.");
		}catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}
	}
	

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Double obtenerPorcentajeMinimoDescuentoPorEstablecimiento(HttpServletRequest request)throws Exception{
		try{
			//se obtiene el par\u00E1metro desde la base de datos
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.minimo.porcentaje.variable", request);
			VistaEntidadResponsableDTO entidadResponsable = SessionManagerSISPE.getCurrentEntidadResponsable(request);
			Integer establecimiento = entidadResponsable.getCodigoEstablecimiento();
			if(!entidadResponsable.getTipoEntidadResponsable().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				//se obtiene el establecimiento en base al local seleccionado en la pantalla
				establecimiento = (Integer)request.getSession().getAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA);
			}
			
			//se divide el par\u00E1metro obtenido
			String [] parametro = parametroDTO.getValorParametro().split(",");
			for(int i=0; i<parametro.length; i++){
				//se verifica el c\u00F3digo del establecimiento
				if(Integer.parseInt(parametro[i].split(SEPARADOR_TOKEN)[0]) == establecimiento.intValue()){
					return Double.valueOf(parametro[i].split(SEPARADOR_TOKEN)[1]);
				}
			}
			
			throw new Exception("No se pudo obtener un porcentaje m\u00EDnimo v\u00E1lido de descuento para el formato de negocio.");
		}catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @param articuloDTO
	 * @return
	 */
	public static boolean esArticuloPesoVariable(ArticuloDTO articuloDTO){
		return (articuloDTO.getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO) || articuloDTO.getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE));
	}
	
	/**
	 * 
	 * @param codigoLocal
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static boolean esLocalActivoParaPrecioMayorista(Integer codLoc, ArticuloDTO articuloDTO, HttpServletRequest request)throws Exception{
		
		try{
			ParametroDTO parametroDTO = null;

			Integer codigoLocal = codLoc;
			//Collection<Integer> localesActivosMayorista = (Collection<Integer>)request.getSession().getAttribute(LOCALES_ACTIVOS_PRECIO_MAYORISTA);
			String codigoPrecioMayorista=articuloDTO.getEnumTipoPrecio().getCodigoTipoPrecio();
			//obtiene los locales habilitados para precio de mayorista
//			if(localesActivosMayorista == null || (localesActivosMayorista != null && localesActivosMayorista.isEmpty())){
//				parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.localesPrecioMayorista", request);
//				valorParametro = parametroDTO.getValorParametro();
//				String parametro[] = valorParametro.split(",");
//				localesActivosMayorista = new ArrayList<Integer>();
//				for(int i = 0; i < parametro.length; i++){
//					localesActivosMayorista.add(Integer.parseInt(parametro[i]));
//				}
//				request.getSession().setAttribute(LOCALES_ACTIVOS_PRECIO_MAYORISTA, localesActivosMayorista);
//			}
			
			//si esta logeado con el adminsispe y esta cotizando en un local mayorista se asigna el codigo del local seleccionado para que aplique el descuento
			if(codigoLocal != null && codigoLocal.intValue() == 0 && request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA) != null){
				Integer codigoLocalSeleccionado = (Integer) request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
				if(codigoLocalSeleccionado != null && codigoLocalSeleccionado > 0){
					codigoLocal = codigoLocalSeleccionado;
				}
			}
			
			//verifica si el local est\u00E1 dentro de los locales habilitados
			if(codigoPrecioMayorista != null && codigoPrecioMayorista.equals(SICArticuloConstantes.TIPO_PRECIO_MAYORISTA)){//if(localesActivosMayorista.contains(codigoLocal)){
				if(request.getSession().getAttribute(LOCAL_ACTIVO_PRECIO_MAYORISTA)==null){
					request.getSession().setAttribute(LOCAL_ACTIVO_PRECIO_MAYORISTA, "ok");
				}
				if((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA)==null){
					//INICIALIZAR DESCUENTO DE MAYORISTA
//					if(iniciarDescuentoPorMayorista(request, articuloDTO)==null){
//						return  false;
//					}
					//se valida si existe llave mayorista
					String[] llaveDescto = iniciarDescuentoPorMayorista(request);
					if(llaveDescto != null && llaveDescto.length > 0){
						obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
						String llaveDescuentoMayorista = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
						for(String llaveActual : llaveDescto){
							if(StringUtils.isNotEmpty(llaveActual) && StringUtils.isNotEmpty(llaveDescuentoMayorista) && llaveActual.equals(llaveDescuentoMayorista)){
								return Boolean.TRUE;
							}
						}
					}
				}
				return Boolean.FALSE;
				
			}else{
				String validaMayoristaFormatoClasificacion = (String)request.getSession().getAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
				//verifica si est\u00E1 habilitada la validaci\u00F3n de precio mayorista por establecimiento y clasificaci\u00F3n 
				if(validaMayoristaFormatoClasificacion == null){
					parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
					validaMayoristaFormatoClasificacion = parametroDTO.getValorParametro();
				}
				
				if(validaMayoristaFormatoClasificacion.equals(SessionManagerSISPE.getEstadoActivo(request))){
					MultiKeyMap establecimientosClasificaciones = (MultiKeyMap)request.getSession().getAttribute(ESTABLECIMIENTOS_DEPARTAMENO_CLASIFICACIONES);
					
					if(establecimientosClasificaciones == null){
						establecimientosClasificaciones = SessionManagerSISPE.getServicioClienteServicio().transGenerarMapaEstablecimientoClasificaciones(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					}
					
					//verifica si el local pertenece a alguno de los formatos configurados y 
					//si la clasificaci\u00F3n del art\u00EDculo est\u00E1 dentro de los departamentos de ese formato de negocio
					Integer codigoEstablecimiento = (Integer)request.getSession().getAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA);
					Integer codigoDepartamento = Integer.parseInt(articuloDTO.getCodigoClasificacion().substring(0, 2));
					LogSISPE.getLog().info("**** articuloDTO.getCodigoClasificacion(): {}",articuloDTO.getCodigoClasificacion());
					if(establecimientosClasificaciones.containsKey(codigoEstablecimiento, codigoDepartamento)){
						//obtiene las clasificaci\u00F3n excluyentes de ese departamento
						Collection<String> clasificaciones = (Collection<String>)establecimientosClasificaciones.get(codigoEstablecimiento, codigoDepartamento);
						//si la clasificaci\u00F3n no es excluyente, aplica precio mayorista
						if(clasificaciones == null || (clasificaciones != null && !clasificaciones.contains(articuloDTO.getCodigoClasificacion()))){
							//INICIALIZAR DESCUENTO DE MAYORISTA
							iniciarDescuentoPorMayorista(request);
							return true;
						}
					}
				}
			}

		}catch(Exception e){
			LogSISPE.getLog().error("Error al verificar si el local es activo para precio mayorista. "+e);
		}
			
		LogSISPE.getLog().info("local no activo para mayoreo");
		return false;
	}
	
	/**
	 * 
	 * @param codigoLocal
	 * @param estadoDetallePedidoDTO
	 * @param articuloDTO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static boolean esArticuloActivoParaPrecioMayorista(Integer codigoLocal,Integer cantidadMinimaMayoreoEstado, Double valorMayorista,
			Long cantidadEstado, ArticuloDTO articuloDTO, HttpServletRequest request)throws Exception{
		//verifica si se aplica precio de mayorista
		if(esLocalActivoParaPrecioMayorista(codigoLocal, articuloDTO, request)
				&& cantidadMinimaMayoreoEstado > 0 
				&& valorMayorista > 0 
				&& cantidadEstado.intValue() >= cantidadMinimaMayoreoEstado.intValue()){
			articuloDTO.setNpHabilitarPrecioMayorista(SessionManagerSISPE.getEstadoActivo(request));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param codigoLocal
	 * @param articuloDTO
	 * @return
	 * @throws Exception
	 */
	public static boolean esArticuloActivoParaPrecioCaja(Integer codigoLocal, Double precioCaja, ArticuloDTO articuloDTO, HttpServletRequest request)throws Exception{
		if(!esLocalActivoParaPrecioMayorista(codigoLocal, articuloDTO, request) 
				&& articuloDTO.getHabilitadoPrecioCaja() && precioCaja.doubleValue() > 0){
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
				//SI NO EXISTE DESCUENTO ES LA PRIMERA VEZ, SI EXISTE EL DESCUENTO CAJ INICIARLO
				Collection<DescuentoEstadoPedidoDTO>colDsctos=(Collection<DescuentoEstadoPedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS_APLICADOS);
				if(!CollectionUtils.isEmpty(colDsctos)){
					Collection<DescuentoEstadoPedidoDTO> colResp = CollectionUtils.select(colDsctos, new Predicate() {
						
						public boolean evaluate(Object arg0) {
							DescuentoEstadoPedidoDTO desEstPed = (DescuentoEstadoPedidoDTO) arg0;
							DescuentoEstadoPedidoID idDesEstPed = desEstPed.getId();
							
							return idDesEstPed.getCodigoReferenciaDescuentoVariable().equals("CAJ");
						}
					});
					if(!CollectionUtils.isEmpty(colResp)){
						iniciarDescuentoPorCajas(request,null);
						return true;
					}
					else{
						if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
							iniciarDescuentoPorCajas(request,null);
							return true;
						}
						else{
							request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
							return false;
						}
					}
				}
				
				iniciarDescuentoPorCajas(request,null);
			}
			else if(request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS) != null && ((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS)).equals("NO")){
				return false;
			}
			
			return true;
		}
		articuloDTO.setNpHabilitarPrecioCaja(SessionManagerSISPE.getEstadoInactivo(request));
		return false;
	}
	
	/**
	 * Recalcular el valor total (valor unitario) de una receta cuando tiene una caracteristica especial
	 * 
	 * @param detallePedidoDTO
	 * @param formulario
	 * @param request
	 * @throws Exception
	 */
	public static void recalcularPrecioReceta(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request)throws Exception{
		//se verifica si es un canasto de cotizaciones
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)
				&& detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null
				&& !detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty()){
			
			LogSISPE.getLog().info("va a recalcular el valor del canasto");
			//el precio devuelto es siempre de afiliado y calculado con IVA
			//Obtener vistaPedido para saber si se calcula el pedido con en nuevo metodo o el anterior
//			VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
//			String calcularCanastosPreciosTemp=null;
//			if(vistaPedidoDTO!=null){
//				calcularCanastosPreciosTemp=vistaPedidoDTO.getObsmigperTemp();
//			}
			double valorUnitarioArticulo = DetalleCanastaAction.calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
			double valorUnitarioArticuloNA = UtilesSISPE.aumentarPorcentajeAPrecio(Util.roundDoubleMath((Double)request.getSession().getAttribute(DetalleCanastaAction.TOTAL_RECETA), NUMERO_DECIMALES), WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
//			if(calcularCanastosPreciosTemp==null){
				//se debe eliminar el iva para asignar el precio sin iva
				if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
					//se asigna el precio de afiliado sin iva
					double valorUnitarioSinIVA = Util.roundDoubleMath((Double)request.getSession().getAttribute(DetalleCanastaAction.TOTAL_RECETA_SIN_IVA), NUMERO_DECIMALES);//valorUnitarioArticulo / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(Util.roundDoubleMath(valorUnitarioSinIVA, NUMERO_DECIMALES));
					
					//se asigna el precio de no afiliado sin iva
					double valorUnitarioNoAfiSinIVA = UtilesSISPE.aumentarPorcentajeAPrecio(Util.roundDoubleMath((Double)request.getSession().getAttribute(DetalleCanastaAction.TOTAL_RECETA_SIN_IVA), NUMERO_DECIMALES), WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));//valorUnitarioArticuloNA / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(Util.roundDoubleMath(valorUnitarioNoAfiSinIVA, NUMERO_DECIMALES));
				}else{
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(valorUnitarioArticulo);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(valorUnitarioArticuloNA);
				}
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(valorUnitarioArticulo);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(valorUnitarioArticuloNA);
			}
//		}
	}
	
	/**
	 * Funcion que valida y realiza el cambio de pesos ingresado desde formulario
	 * @param detallePedidoDTO
	 * @param pesoTotalAproximado,pesoTotalIngesado 
	 * @param request
	 * @throws Exception
	 */
	public static StringBuffer validacionCambioPesosFormulario(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request, Double pesoTotalAproximado, Double pesoTotalIngesado)throws Exception{
		//Mensaje para la validacion
		StringBuffer pocentajeTolerancia = null;
		
		Double valorCalculo = obtenerValorCalculoLimitesPesos(request);
		Double tolerancia = obtenerValorPorcentajeTolerancia(request);
		
		//Validacion para el cambio de pesos en pavos
		if(pesoTotalIngesado.doubleValue() != pesoTotalAproximado.doubleValue()){
			//Del Peso aproximado del articulo le sumo o le resto segun un parametro para el calculo
			//double pesoMedioAproximadoInicial = detallePedidoDTO.getArticuloDTO().getPesoAproximado().doubleValue() - valorCalculo;
			double pesoMedioAproximadoInicial = detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta() - valorCalculo;
			//double pesoMedioAproximadoFinal = detallePedidoDTO.getArticuloDTO().getPesoAproximado().doubleValue() + valorCalculo;
			double pesoMedioAproximadoFinal = detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta() + valorCalculo;
			
			if(!detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getValorTipoControlCosto().equals(SICArticuloConstantes.TIPCONCOS_PIEPESUM)){
				
				//Encuento el valor maximo y minimo del pesoMedioAproximado del (Pavo)
				double valorMinimo = Util.roundDoubleMath(Double.valueOf(((pesoMedioAproximadoInicial*(tolerancia/100))+pesoMedioAproximadoInicial)*detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()), NUMERO_DECIMALES);
				double valorMaximo = Util.roundDoubleMath(Double.valueOf(((pesoMedioAproximadoFinal*(tolerancia/100))+pesoMedioAproximadoFinal)*detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()), NUMERO_DECIMALES);
				
				LogSISPE.getLog().info("----Valor Minimo---{}",valorMinimo);
				LogSISPE.getLog().info("----Valor Maximo---{}",valorMaximo);
				if(pesoTotalIngesado.doubleValue() <=  valorMaximo && pesoTotalIngesado.doubleValue() >= valorMinimo){
					LogSISPE.getLog().info("----Peso entre el rango de tolerancia---");
					//Cambio al peso ingresado
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(pesoTotalIngesado.doubleValue());
				}else{
					pocentajeTolerancia = new StringBuffer();
					if(valorMaximo!=0 && valorMinimo!=0){
						pocentajeTolerancia.append(tolerancia).append(",").append(valorMaximo).append(",").append(valorMinimo);
					}
				}
			}else{
				Double pesoAproximado=detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta();
				Double cantidad= pesoTotalIngesado.doubleValue() / pesoAproximado.doubleValue();
				cantidad=Util.roundDoubleMath(cantidad,0);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(pesoTotalIngesado.doubleValue());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(cantidad==0D?1:cantidad.longValue());
			}
		}else{
			LogSISPE.getLog().info("----No Hubo cambio de Peso---");
		}
		return pocentajeTolerancia;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static String obtenerClasificacionRecetaCatalogo(HttpServletRequest request)throws Exception{
		//se obtiene la clasificaci\u00F3n para las recetas de cat\u00E1logo
		String clasificacionRecetaCatalogo = (String)request.getSession().getAttribute(CLASIFICACION_RECETAS_CATALOGO);
		if(clasificacionRecetaCatalogo == null){
			//se obtiene el par\u00E1metro para calcular el m\u00E1ximo de modificaciones
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
			clasificacionRecetaCatalogo = parametroDTO.getValorParametro();
			request.getSession().setAttribute(CLASIFICACION_RECETAS_CATALOGO, clasificacionRecetaCatalogo);
		}
		return clasificacionRecetaCatalogo;
	}
	
	/**
	 * 
	 * @param request
	 */
	public static void obtenerClasificacionesEspecialesCompras(HttpServletRequest request)throws Exception{
		//se obtiene el rol de compras de sesi\u00F3n
		String rolCompras = (String)request.getSession().getAttribute(ID_ROL_COMPRAS);
		if(rolCompras == null){
			//se obtiene el rol de los usuarios de compras
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.rolUsuariosCompras", request);
			rolCompras = parametroDTO.getValorParametro();
			request.getSession().setAttribute(ID_ROL_COMPRAS, rolCompras);
			parametroDTO = null;
		}
		
		//se obtiene la colecci\u00F3n de c\u00F3digos de clasificaciones que manejan los usuarios de compras
		Collection<String> codigosClasificaciones = (Collection<String>)request.getSession().getAttribute(CODIGOS_CLASIFICACIONES_ESPECIALES);
		
		//se verifica el valor del par\u00E1metro
		if(rolCompras!=null && codigosClasificaciones == null){
			Collection<UserDto> colUserDto = FrameworkFactory.getSecurityService().getUsersByRole(rolCompras);
			if(colUserDto!=null && !colUserDto.isEmpty()){
				String idsUsuarios = "";
				//se itera la colecci\u00F3n de usuarios para concatenar los ids de usuarios
				for(UserDto userDto : colUserDto){
					idsUsuarios = idsUsuarios.concat(userDto.getUserId()).concat(",");
				}
				//se elimina el \u00FAltimo separador
				idsUsuarios = idsUsuarios.substring(0, idsUsuarios.length() - 1);
				
				//se realiza la consulta para obtener las clasificaciones a las que tienen acceso los usuarios
				ClasificacionUsuarioDTO consultaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO(Boolean.TRUE);
				consultaClasificacionUsuarioDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaClasificacionUsuarioDTO.getId().setUserId(idsUsuarios);
				consultaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
				
				Collection<ClasificacionUsuarioDTO> colClasificacionUsuarioDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacionesUsuario(consultaClasificacionUsuarioDTO);

				if(colClasificacionUsuarioDTO!=null && !colClasificacionUsuarioDTO.isEmpty()){
					//se crea la colecci\u00F3n que solo contiene los c\u00F3digos de clasificaciones
					codigosClasificaciones = new ArrayList<String>();
					for(ClasificacionUsuarioDTO clasificacionUsuarioDTO: colClasificacionUsuarioDTO){
						codigosClasificaciones.add(clasificacionUsuarioDTO.getId().getCodigoClasificacion());
					}
					//se guardan la colecci\u00F3n
					request.getSession().setAttribute(CODIGOS_CLASIFICACIONES_ESPECIALES, codigosClasificaciones);
				}
			}
			colUserDto = null;
		}
	}
	public static void instanciarVentanaSubirArchivoBeneficiario(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro en CotizarReservarUtil->instanciarVentanaSubirArchivoBeneficiario");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo beneficiario");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'adjuntarArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarArchBene=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaArchivoBene.jsp");
		popUp.setAncho(50D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	public static void instanciarVentanaSubirArchivoFoto(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo foto");
		//popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'aceptaArchivoFoto=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelaArchivoFoto=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaArchivoFotos.jsp");
		popUp.setAncho(60D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	public static void instanciarVentanaConfirmarModificacionPedido(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo foto");
		//popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'aceptaArchivoFoto=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelaArchivoFoto=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaArchivoFotos.jsp");
		popUp.setAncho(60D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	public static void instanciarVentanaEliminarPedidoTotal(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','resultadosPedidosConsolidados','div_pagina','mensajes'], {parameters: 'confirmarEliminarConsolidacion=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarEliminacionPedido=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/consolidarPedido/confirmarEliminarConsolidados.jsp");
		popUp.setAncho(45D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	
	public static void instanciarVentanaEliminarPedidos(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','resultadosPedidosConsolidados','div_pagina','mensajes'], {parameters: 'confirmarEliminarPedidoConsolidado=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarEliminacionPedido=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/consolidarPedido/confirmarEliminarPedidoConsolidado.jsp");
		popUp.setAncho(45D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	public static void instanciarVentanaAdvertenciaEliminacion(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Eliminaci\u00F3n de archivo");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'aceptaCancelacionFotos=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelaArchivoFoto=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaAdvertenciaEliminacion.jsp");
		popUp.setAncho(60D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	public static void instanciarVentanaAuxiliarArchivo(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar la ventana de confirmacion sobre escribir archivo");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Beneficiario");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'reemplazarArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarReemplazo=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setPreguntaVentana("\u00BFDesea sobre escribir el archivo existente del pedido?");
		//popUp.setContenidoVentana("servicioCliente/confirmarReservacion/confirmarArchivoBeneficiario.jsp");
		session.setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
		popUp = null;
	}
	public static void instanciarVentanaAuxiliarArchivoFoto(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar la ventana de confirmacion sobre escribir archivo");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo foto");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'reemplazarArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarReemplazo=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setPreguntaVentana("\u00BFDesea sobre escribir el archivo existente del pedido?");
		//popUp.setContenidoVentana("servicioCliente/confirmarReservacion/confirmarArchivoBeneficiario.jsp");
		session.setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
		popUp = null;
	}
	
	/**
	 * popUp para regresar a la pantalla de busqueda cuando un pedido ha sido modificado desde otra sesion 
	 * @param request
	 * @param accion
	 * @throws Exception
	 */
	public static void instanciarVentanaPedidoModificado(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar el popUp para regresar a la pantalla de busqueda cuando un pedido ha sido modificado desde otra sesion ");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Advertencia");
		popUp.setEtiquetaBotonOK("ACEPTAR");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina'], {parameters: 'siVolverBusqueda=ok', evalScripts:true});ocultarModal();");
		popUp.setContenidoVentana("/servicioCliente/cotizarRecotizarReservar/regresarPantallaBusqueda.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * popUp para indicar que el canasto que se esta creando es un especial y se va a verificar si el canasto existe en el SIC
	 * @param request
	 * @param accion
	 * @throws Exception
	 */
	public static void instanciarVentanaVerificarCanastoEspecial(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar el popUp para verificar el cansto especial ");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Informaci\u00F3n");
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'siVerificarStock=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarVerificarCanastosEspeciales=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/cotizarRecotizarReservar/popUpVerificarCanastoEspecial.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	/**
	 * popUp para regresar a la pantalla de busqueda cuando un pedido ha sido modificado desde otra sesion 
	 * @param request
	 * @param accion
	 * @throws Exception
	 */
	public static void instanciarVentanaConfirmacionGuardarPedido(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar el popUp para confirmar si los pesos finales ingresados son los correctos");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Advertencia");
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setEtiquetaBotonCANCEL("Cancelar");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'registrarPesosFinales=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarPesosFinales=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/confirmarReservacion/ventanaConfirmacionPesosFinales.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * popUp para regresar a la pantalla de busqueda cuando un pedido ha sido modificado desde otra sesion 
	 * @param request
	 * @param accion
	 * @throws Exception
	 */
	/*public static void instanciarVentanaConfirmacionGuardarPedido(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar el popUp para confirmar si los pesos finales ingresados son los correctos");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Advertencia");
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setEtiquetaBotonCANCEL("Cancelar");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'registrarPesosFinales=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarPesosFinales=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/confirmarReservacion/ventanaConfirmacionPesosFinales.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}*/
	
	/**
	 * instancia el contenido del popUp pedido no reservado 
	 * @param request
	 * @param accion
	 * @throws Exception
	 */
	public static void instanciarPopUpPedidoNoReservado(HttpServletRequest request,String accion)throws Exception{		
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro al instanciar el popUp generico ");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Advertencia");
		popUp.setEtiquetaBotonOK("ACEPTAR");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina'], {parameters: 'ocultarPopUpPedidoNoReservado=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta','div_pagina'], {parameters: 'ocultarPopUpPedidoNoReservado=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/cotizarRecotizarReservar/popUpPedidoNoReservado.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	public static void aceptarArchivoBeneficiario(HttpServletRequest request,ArchivoPedidoDTO archivoArmado)throws Exception{
		LogSISPE.getLog().info("Entro a aceptarArchivoBeneficiario");
		HttpSession session = request.getSession();
		//ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
	    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		//seteo el codigo del pedido
		archivoPedidoDTO.setCodigoPedido(archivoArmado.getCodigoPedido());
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		try{			    
		    //entra al servicio de guardar en la base el archivo beneiciario
		    SessionManagerSISPE.getServicioClienteServicio().transCrearArchivoBenficiario(archivoArmado);
		    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
		    Collection<ArchivoPedidoDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerArchivoBeneficiario(archivoPedidoDTO);
			//se sube a sesion los archivo filtrados por codigoPedido
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
			LogSISPE.getLog().info("size files ->"+colFiles.size());
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}	
	}
	
	public static void agregarSessionArchivoFoto(HttpServletRequest request,DefArchivoEntregaDTO defArchivoDTO)throws Exception{		
		HttpSession session = request.getSession();
		DefArchivoEntregaDTO defArchivo = new DefArchivoEntregaDTO();
		defArchivo.getId().setCodigoCompania(defArchivoDTO.getId().getCodigoCompania());
		defArchivo.getId().setCodigoArticulo(defArchivoDTO.getId().getCodigoArticulo());
		defArchivo.getId().setCodigoPedido(defArchivoDTO.getId().getCodigoPedido());
		defArchivo.getId().setCodigoAreaTrabajo(defArchivoDTO.getId().getCodigoAreaTrabajo());
		defArchivo.setSecuencialEntrega(defArchivoDTO.getSecuencialEntrega());
		defArchivo.setSecuencialEstadoPedido(defArchivoDTO.getSecuencialEstadoPedido());

		//entra al servicio de guardar en la base el archivo beneiciario
	    SessionManagerSISPE.getServicioClienteServicio().transCrearDefinicionArchivo(defArchivoDTO);
	    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
	    //defArchivoDTO.setEntregaDTO(null);
	    Collection<DefArchivoEntregaDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerDefinicionArchivo(defArchivo);
		//se sube a sesion los archivo filtrados por codigoPedido
		session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO, colFiles);
		
		LogSISPE.getLog().info("size files ->"+colFiles.size());
	}
	
	public static boolean verificarArchivoFotosEliminar(Collection<DefArchivoEntregaDTO> colArchivos)throws Exception{
		boolean flag = false;
		//Proceso de verificacion y eliminacion.
		flag = SessionManagerSISPE.getServicioClienteServicio().transProcesoDeEliminacionDefinicionArchivo(colArchivos);
		return flag;
	}
	
	public static void confirmarReemplazo(HttpServletRequest request,ArchivoPedidoDTO archivoArmado)throws Exception{
		LogSISPE.getLog().info("Confirmar Reeeplazo");
		HttpSession session = request.getSession();
	    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		//seteo el codigo del pedido
		archivoPedidoDTO.setCodigoPedido(archivoArmado.getCodigoPedido());
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		try{			    
		    //entra al servicio de guardar en la base el archivo beneiciario
		    SessionManagerSISPE.getServicioClienteServicio().transActualizarArchivoBeneficiario(archivoArmado);
		    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
		    Collection<ArchivoPedidoDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerArchivoBeneficiario(archivoPedidoDTO);
			//se sube a sesion los archivo filtrados por codigoPedido
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
			LogSISPE.getLog().info("size files ->"+colFiles.size());
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}	
	}
	public static void listarArchivos(HttpServletRequest request,String pedido)throws Exception{
		LogSISPE.getLog().info("Entro listar los archivos CotizarReservarUtil");
		HttpSession session = request.getSession();		
		ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		archivoPedidoDTO.setCodigoPedido(pedido);
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
	    Collection<ArchivoPedidoDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerArchivoBeneficiario(archivoPedidoDTO);
	    
	    //se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
		session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
	}
	
	public static void busquedaArchivoFoto(HttpServletRequest request,DefArchivoEntregaDTO defArchivo)throws Exception{
		LogSISPE.getLog().info("Entro listar los archivos CotizarReservarUtil");
		HttpSession session = request.getSession();
	    session.removeAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
	    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
	    Collection<DefArchivoEntregaDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerDefinicionArchivo(defArchivo);
	    if(colFiles != null && !colFiles.isEmpty()){
	    	 //se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO, colFiles);
	    }
	}
	
	public static void cancelarArchivoBeneficiario(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro cancelarArchivo CotizarReservarUtil");
		 session.removeAttribute(SessionManagerSISPE.POPUP);
	}
	
	public static void cancelarReeplazoArchivo(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a ocultar la ventana confirmacion");
		 session.removeAttribute(SessionManagerSISPE.POPUPAUX);
	}
	
	public static void eliminarArchivoBeneficiario(HttpServletRequest request,Long secuencial, String codigoPedido)throws Exception{
		HttpSession session = request.getSession();
		
		ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		archivoPedidoDTO.getId().setSecuencialArchivoPedido(secuencial);
		LogSISPE.getLog().info("Entro elimnarArchivoBeneficiario CotizarReservarUtil");
		LogSISPE.getLog().info("Valor del Secuencial:{}", secuencial);
		//Entra al servicio para elimnar los archivos del pedidobeneficiario.
		SessionManagerSISPE.getServicioClienteServicio().transEliminarArchivoBeneficiario(archivoPedidoDTO);
		//seteo el codigo del pedido
		archivoPedidoDTO = new ArchivoPedidoDTO();
		archivoPedidoDTO.setCodigoPedido(codigoPedido); 
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//Entra al servicio para obtener lo archivos del pedidobeneficiario.
	    Collection<ArchivoPedidoDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerArchivoBeneficiario(archivoPedidoDTO);
	    session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
	}
	
	public static void eliminarArchivoFoto(HttpServletRequest request,Integer indiceArchivo)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Indice Archivo:{}", indiceArchivo);
		boolean flag = false;
		List<DefArchivoEntregaDTO> colArchivos = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
		session.removeAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX);
		//Verifico que la colleccion no sea null
		if(colArchivos != null && !colArchivos.isEmpty()){
			DefArchivoEntregaDTO defArchivo = colArchivos.get(indiceArchivo);
			//bandera para limpiar la varable se session
			if(colArchivos.size() == 1){
				flag= true;
				session.setAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX, colArchivos.get(indiceArchivo));
			}
			//Metodo que elimina los archivos agregados
			SessionManagerSISPE.getServicioClienteServicio().transEliminarDefinicionArchivo(defArchivo);
			
			if(flag){
				session.removeAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
			}
			//Entra al servicio para obtener lo archivos del pedidobeneficiario.
		    Collection<DefArchivoEntregaDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerDefinicionArchivo(defArchivo);
	    	//se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO, colFiles); 
		}
	}
	
	public static void reemplazarArchivoFoto(HttpServletRequest request, DespachosEntregasForm formulario)throws Exception{
		HttpSession session = request.getSession();
		DefArchivoEntregaDTO defArchivoDTO =(DefArchivoEntregaDTO)session.getAttribute(SessionManagerSISPE.ARCHIVO_DTO);
		defArchivoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
		defArchivoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.FotoDespacho"));
		defArchivoDTO.setTamanioArchivo(BigDecimal.valueOf(formulario.getArchivo().getFileSize()));
		//Creo el archivo
		ArchivoEntregaDTO archiv = new ArchivoEntregaDTO();
		archiv.setDatosArchivo(formulario.getArchivo().getFileData());
		defArchivoDTO.setArchivoEntregaDTO(archiv);
		
		//entra al servicio de guardar en la base el archivo beneiciario
	    SessionManagerSISPE.getServicioClienteServicio().transActualizarDefinicionArchivo(defArchivoDTO);
	    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
	    Collection<DefArchivoEntregaDTO> colFiles = SessionManagerSISPE.getServicioClienteServicio().transObtenerDefinicionArchivo(defArchivoDTO);
    	//Limpia la variable de session
	    session.removeAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
	    //se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
		session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO, colFiles);
	}
	
	//funcion para ver descargar o guarda el archivo del beneficiario
	public static void verArchivo(HttpServletRequest request,HttpServletResponse response)throws Exception {
		LogSISPE.getLog().info("Entro a descargar el Archivo del Utils");
		ArrayList<ArchivoPedidoDTO> colArchivos = (ArrayList<ArchivoPedidoDTO>)request.getSession().getAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE); 
		String tipo = "txt";
		try {
			if (colArchivos!= null){
			int indiceArchivo= Integer.parseInt(request.getParameter("indice"));
			ArchivoPedidoDTO archivoDTO = (ArchivoPedidoDTO)colArchivos.get(indiceArchivo);
				descargarArchivo(response, archivoDTO.getArchivo(), archivoDTO.getNombreArchivo(), tipo);
			}
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}
	}
	
	//funcion para ver descargar o guarda el archivo del beneficiario
	public static void verArchivoFoto(HttpServletRequest request,HttpServletResponse response)throws Exception {
		LogSISPE.getLog().info("Entro a descargar el Archivo del Utils");
		ArrayList<DefArchivoEntregaDTO> colArchivos = (ArrayList<DefArchivoEntregaDTO>)request.getSession().getAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO); 
		String tipo = "jpg";
		try {
			if (colArchivos!= null){
				int indiceArchivo= Integer.parseInt(request.getParameter("indice"));
				DefArchivoEntregaDTO archivoDTO = colArchivos.get(indiceArchivo);
				descargarArchivo(response, archivoDTO.getArchivoEntregaDTO().getDatosArchivo(), archivoDTO.getNombreArchivo(), tipo);
			}
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}
	}
	
	//funcion que presenta la ventana de descarga
	private static void descargarArchivo(HttpServletResponse response, byte [] archivoPedido, String nombreArchivo, String contenType) throws Exception {
		OutputStream archivo;
		archivo = response.getOutputStream();
		response.setContentType(contenType);
		response.setHeader("Content-Disposition","attachment; filename="+ nombreArchivo);
		for(int i=0; i<archivoPedido.length; i++){
			archivo.write((char)archivoPedido[i]);
		}
		archivo.flush();
		archivo.close();
	}
	/**
	 * Clase que valida tama\u00F1o del archivo y archivos repetidos
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static boolean validarArchivo(HttpServletRequest request, String nombreArchivo) throws Exception {
		LogSISPE.getLog().info("***Entro validar archivos repetidos***");
		boolean flag = true;
		HttpSession session = request.getSession();		
		ArrayList<ArchivoPedidoDTO> colArchivos = (ArrayList<ArchivoPedidoDTO>)session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE);
	    session.removeAttribute(SessionManagerSISPE.ARCHIVO_DTO);
		if(colArchivos != null && !colArchivos.isEmpty()){
	    	LogSISPE.getLog().info("El pedido tiene archivos a comparar");
	    	for(ArchivoPedidoDTO file : colArchivos){
		    	if(file.getNombreArchivo().equals(nombreArchivo)){
		    		LogSISPE.getLog().info("Archivo con el mismo nombre");
		    		session.setAttribute(SessionManagerSISPE.ARCHIVO_DTO, file);
		    		flag = false;
		    		break;
		    	}
		    }	
	    } else
	    	LogSISPE.getLog().info("El pedido no tiene archivos a validar");
	    return flag;
	}
	/**
	 * Funcion que verifica si el check pago efectivo tiene que ser habilitado o no
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static boolean validarCheckMaxiNavidad(HttpServletRequest request, Collection<DescuentoEstadoPedidoDTO> colDescuentos, CotizarReservarForm formulario) throws Exception {
		LogSISPE.getLog().info("***Entro a validar el check maxi-navidad***");
		HttpSession session = request.getSession();
		boolean desMaxNav = false;
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		
		String codigoTipoDescuentoPagEfe = obtenerCodigoTipoDescuentoMaxiNavidad(request);
		//Encuento el tipo de pedido Maxi-navidad
		for(DescuentoEstadoPedidoDTO descuentoEstDTO : (ArrayList<DescuentoEstadoPedidoDTO>)colDescuentos){
			if(descuentoEstDTO.getDescuentoDTO().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe)){
				desMaxNav = Boolean.TRUE;
			}
		}
		
//		if(codigoTipoDescuentoPagEfe!=null && codigoTipoDescuentoPagEfe.length()>0)
//			desMaxNav = Boolean.TRUE;
		
		//Si existe el descuento 
		if(desMaxNav){
			formulario.setCheckPagoEfectivo(estadoActivo);
			session.setAttribute(CotizarReservarAction.DES_MAX_NAV_EMP, "ok");
			session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
			//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
			establecerDescuentosFormulario(request, formulario);
		}else{
			formulario.setCheckPagoEfectivo(null);
			session.removeAttribute(CotizarReservarAction.DES_MAX_NAV_EMP);
			session.removeAttribute(CHECK_PAGO_EFECTIVO);
		}
		
	    return desMaxNav;
	}
	
	/**
	 * Funcion que verifica si entre los descuentos existe el maxi-navidad
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static boolean verificadDescuentoMaxi(HttpServletRequest request, Collection<DescuentoEstadoPedidoDTO> colDescuentos) throws Exception {
		LogSISPE.getLog().info("*Entra a la funcion verificadDescuentoMaxi*");
		boolean desMaxNav = false;
		String codigoTipoDescuentoPagEfe = obtenerCodigoTipoDescuentoMaxiNavidad(request);
		//Encuento el tipo de pedido Maxi-navidad
		for(DescuentoEstadoPedidoDTO descuentoEstDTO : (ArrayList<DescuentoEstadoPedidoDTO>)colDescuentos){
			if(descuentoEstDTO.getDescuentoDTO().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe)){
				desMaxNav = true;
				break;
			}
		}
	    return desMaxNav;
	}
	
	/**
	 * Funcion que verifica si el check pago efectivo tiene que ser habilitado o no
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static String obtenerCodigoTipoDescuentoMaxiNavidad(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento maxi-navidad
		String codigoTipoDescuentoPagEfe = (String)session.getAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO);
		if(codigoTipoDescuentoPagEfe == null){
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo", request);
			if(parametroDTO.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO, parametroDTO.getValorParametro());
				codigoTipoDescuentoPagEfe = parametroDTO.getValorParametro();
			}
		}
		return codigoTipoDescuentoPagEfe;
	}
	
	/**
	 * Funcion que verifica si el check pago efectivo tiene que ser habilitado o no
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static void obtenerCodigoTipoDescuentoPorCajasMayorista(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento por cajas
		String llaveCajas=(String)session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
		String llaveMayorista=(String)session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
		
		Collection<TipoDescuentoDTO> tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
		
		//se inicializa los tipos de descuentos
		if(CollectionUtils.isEmpty(tiposDescuento)){
			cargarConfiguracionDescuentos(request, (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO));
			tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
		}
		
		if(llaveCajas == null){
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento por cajas
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja", request);
			if(parametroDTO.getValorParametro()!=null){
				String parametrosTipoDescuentoPorCajas=parametroDTO.getValorParametro();
				session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PORCAJAS, parametrosTipoDescuentoPorCajas);
				//Crear la llave de descuento de cajas
				//Agregar las nuevas llaves de descuentos automaticos.
				if(!CollectionUtils.isEmpty(tiposDescuento)){
					//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
					int indiceFila=0;
							for (TipoDescuentoDTO tipDesDTO : tiposDescuento) {
								if(parametrosTipoDescuentoPorCajas.trim().equals(tipDesDTO.getId().getCodigoTipoDescuento())){
									String llaveDes=indiceFila+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento")+parametrosTipoDescuentoPorCajas
											+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento")+
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.cantidad");
											session.setAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS,llaveDes);
											break;	
								}
								indiceFila++;
							}
					}
				
			}
		}
		if(llaveMayorista == null){
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento por mayorista
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista", request);
			if(parametroDTO.getValorParametro()!=null){
				String parametrosTipoDescuentoPorMayorista=parametroDTO.getValorParametro();
				session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PORMAYORISTA, parametrosTipoDescuentoPorMayorista);
				
				
				if(!CollectionUtils.isEmpty(tiposDescuento)){
					//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
					int indiceFila=0;
							for (TipoDescuentoDTO tipDesDTO : tiposDescuento) {
								if(parametrosTipoDescuentoPorMayorista.trim().equals(tipDesDTO.getId().getCodigoTipoDescuento())){
									String llaveDes=indiceFila+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento")+parametrosTipoDescuentoPorMayorista
											+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento")+
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.cantidad");
											session.setAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA,llaveDes);
											break;	
								}
								indiceFila++;
							}
					}
				
			}
		}
	}
	
	/**
	 * Funcion que verifica si la fechaInicial Pedido esta entre el numero de meses atras parametrizado
	 * @param request
	 * @return
	 */
	public static StringBuffer verificacionFechaInicialPedido(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO) throws Exception {
		
		Timestamp fechaPedido = vistaPedidoDTO.getFechaInicialEstado();
		String estadoPedidoActual = vistaPedidoDTO.getId().getCodigoEstado();
		LogSISPE.getLog().info("el pedido se encuentra en el estado: {} con fechaInicialEstado: {}",estadoPedidoActual, fechaPedido.toString());
		 
		StringBuffer permitirModificacion = new StringBuffer();
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroMeses.permitidosRcoResMod", request);
		//bgudino comentar esta linea
		//parametroDTO.setValorParametro("CCA-6,OTRO-12");
		SimpleDateFormat dateFormat =  new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
		if(parametroDTO != null){
			//se separan los datos del parametro por estado del pedido
			String[] datosParametros = parametroDTO.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
			
			if(datosParametros != null && datosParametros.length > 0){
				Calendar calendarioFechaActual =  new GregorianCalendar();
				GregorianCalendar calendarioFechaInicio =  new GregorianCalendar();
				Boolean validarMesesAtras = Boolean.FALSE;
				for(String datoActual : datosParametros){
							
					if(StringUtils.isNotEmpty(datoActual) && datoActual.contains(SEPARADOR_TOKEN)){
						String estadoPedido =  datoActual.split(SEPARADOR_TOKEN)[0].trim();
						Integer nMesesRestar = Integer.parseInt( datoActual.split(SEPARADOR_TOKEN)[1].trim());
						
						//se validan los pedidos de acuerdo al estado
						if(estadoPedido.equals(estadoPedidoActual)){				
							validarMesesAtras = Boolean.TRUE;
						}else if(estadoPedido.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.otro"))){
							//valor por defecto para estados que no esten parametrizados
							validarMesesAtras = Boolean.TRUE;
						}
						if(validarMesesAtras){
							calendarioFechaInicio.add(Calendar.MONTH, (nMesesRestar*-1));
							LogSISPE.getLog().info("FECHA-INICIO->{}",dateFormat.format(calendarioFechaInicio.getTime()));
							LogSISPE.getLog().info("FECHA-FINAL->{}", dateFormat.format(calendarioFechaActual.getTime()));
							LogSISPE.getLog().info("FECHA-PEDIDO->{}",dateFormat.format(fechaPedido.getTime()));
							if(fechaPedido.after(calendarioFechaInicio.getTime())){
								return null;
							}
							if(fechaPedido.getTime() >= calendarioFechaInicio.getTimeInMillis() && fechaPedido.getTime() <= calendarioFechaActual.getTimeInMillis()){
								permitirModificacion = null;
							}else{
								permitirModificacion.append(DateManager.getYMDDateFormat().format(fechaPedido.getTime())).append(",").append(DateManager.getYMDDateFormat().format(calendarioFechaInicio.getTimeInMillis()));
							}
							break;
						}
					}
				}
			}
		}
		return permitirModificacion;
	}
	
	/**
	 * Funcion que verifica y retorna si ya esta en session el porcentaje de tolerancia desde obtenido desde la tabla parametro
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static Double obtenerValorPorcentajeTolerancia(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Double porcentajeTolerancia = 0D;
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento maxi-navidad
		String porcentajeToleranciaAux = (String)session.getAttribute(CotizarReservarAction.VALOR_PORCENTAJE_TOLERANCIA);
		
		
		if(porcentajeToleranciaAux == null){
			//se obtiene el par\u00E1metro que indica el % tolerancia
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.articulos.pesoVariable.porcentajeTolerancia", request);
			if(parametroDTO.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.VALOR_PORCENTAJE_TOLERANCIA, parametroDTO.getValorParametro());
				porcentajeTolerancia = Double.parseDouble(parametroDTO.getValorParametro());
			}
		}else{
			porcentajeTolerancia = Double.parseDouble(porcentajeToleranciaAux);
		}
		return porcentajeTolerancia;
	}
	
	/**
	 * Funcion que verifica y retorna si ya esta en session el valorCalculoLimitePesos desde la tabla parametro
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static Double obtenerValorCalculoLimitesPesos(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Double valorCalculoLimite = 0D;
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento maxi-navidad
		String valorCalculoAux = (String)session.getAttribute(CotizarReservarAction.VALOR_CALCULO_LIMITES_PESOS);
		if(valorCalculoAux == null){
			//se obtiene el par\u00E1metro que indica el % tolerancia
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.valorCalculoLimiteModificacionArticuloPavo", request);
			if(parametroDTO.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.VALOR_CALCULO_LIMITES_PESOS, parametroDTO.getValorParametro());
				valorCalculoLimite = Double.parseDouble(parametroDTO.getValorParametro());
			}
		}else{
			valorCalculoLimite = Double.parseDouble(valorCalculoAux);
		}
		return valorCalculoLimite;
	}
	
	/**
	 * Funcion que verifica y retorna si ya esta en session las clasificaciones permitidas para el cambio de pesos desde la tabla parametro
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static String obtenerClasificacionesParaCambioPesos(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento maxi-navidad
		String clasificacionesArticulos = (String)session.getAttribute(CotizarReservarAction.CLASIFICACIONES_PERMITIDAS_CAMBIO_PESOS);
		if(clasificacionesArticulos == null){
			//se obtiene el par\u00E1metro que indica el % tolerancia
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificaciones.articulosPesoVariable", request);
			if(parametroDTO.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.CLASIFICACIONES_PERMITIDAS_CAMBIO_PESOS, parametroDTO.getValorParametro());
				clasificacionesArticulos = parametroDTO.getValorParametro();
			}
		}
		return clasificacionesArticulos;
	}
	
	/**
	 * Funcion que verifica y retorna los codigos de los estados de un pedido permitidos para modificar una reservacion
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static String obtenerCodigosEstadoPedidosModificacionReserva(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento maxi-navidad
		String codigoEstados = (String)session.getAttribute(CotizarReservarAction.CODIGO_ESTADOS_PERMITIDOS_MODIFICAR_RESERVA);
		if(codigoEstados == null){
			//se obtiene el par\u00E1metro que indica el % tolerancia
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosEstados.permitidos.modificacionReservacion", request);
			if(parametroDTO.getValorParametro()!=null){
				session.setAttribute(CotizarReservarAction.CODIGO_ESTADOS_PERMITIDOS_MODIFICAR_RESERVA, parametroDTO.getValorParametro());
				codigoEstados = parametroDTO.getValorParametro();
			}
		}
		return codigoEstados;
	}
	
	/**
	 * valida archivos repetidos
	 * @param request
	 * @param formulario
	 * @param errors
	 * @return
	 */
	public static boolean validarArchivoFoto(HttpServletRequest request, String nombreArchivo) throws Exception {
		LogSISPE.getLog().info("***Entro validar archivos repetidos***");
		boolean flag = false;
		HttpSession session = request.getSession();
		session.removeAttribute(SessionManagerSISPE.ARCHIVO_DTO);
		ArrayList<DefArchivoEntregaDTO> colArchivos = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
		if(colArchivos != null && !colArchivos.isEmpty()){
	    	LogSISPE.getLog().info("El pedido tiene archivos a comparar");
	    	for(DefArchivoEntregaDTO file : colArchivos){
		    	if(file.getNombreArchivo().equals(nombreArchivo)){
		    		LogSISPE.getLog().info("Archivo con el mismo nombre");
		    		session.setAttribute(SessionManagerSISPE.ARCHIVO_DTO, file);
		    		flag = true;
		    		break;
		    	}
		    }	
	    } else
	    	LogSISPE.getLog().info("El pedido no tiene archivos a validar");
	    return flag;
	}
	
	public static void establecerDescuentosFormulario(HttpServletRequest request, CotizarReservarForm formulario) throws Exception{
		//establece los valores de los descuentos en el formulario
		if(request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO) != null 
				&& request.getSession().getAttribute(CotizarReservarAction.COL_MOTIVO_DESCUENTO) != null 
				&& request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS) != null
				){
			LogSISPE.getLog().info("******************* Establecer Valores de Descuento en el Formulario ***********************");
			//obtiene los tipos de descuento
			ArrayList<TipoDescuentoDTO> tipoDescuentoDTOCol = (ArrayList<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			//obtiene los motivos de descueto
			ArrayList<MotivoDescuentoDTO> motivoDescuentoDTOCol = (ArrayList<MotivoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_MOTIVO_DESCUENTO);
			//obtiene los descuentos seleccionados
			ArrayList<DescuentoEstadoPedidoDTO> descuentosSeleccionados = (ArrayList<DescuentoEstadoPedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
			//obtiene los compradores disponibles
//			ArrayList<CompradorDTO> compradorDTOCol = (ArrayList<CompradorDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_COMPRADORES_INTERNOS);
			//establece el size de la propiedad
			String[] opDesSeleccionados = new String[tipoDescuentoDTOCol.size() * motivoDescuentoDTOCol.size()];
			String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
			//variable para la concatenaci\u00F3n del tipo y el motivo de descuento
			StringBuffer codTipMotDes = null;
			String codTipMotDesS = null;
			//variable para la concatenaci\u00F3n del tipo y el motivo de descuento seleccionado
			StringBuffer codDesSel = null;
			//contador para la posici\u00F3n del arreglo de opciones
			int contadorOp = 0;
			//contador para el tipo de descuentos
			int contadorTipDes = 0;
			//parametro para descuentos variables
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
			String codigoTipDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento");
			String codigoMotDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento");
			//recorre los tipos
			for(TipoDescuentoDTO tipoDescuento : tipoDescuentoDTOCol){
				//recorre los motivos
				for(MotivoDescuentoDTO motivoDescuento : motivoDescuentoDTOCol){
					//recorre los descuentos seleccionados
					for(DescuentoEstadoPedidoDTO descuentoSeleccionado : descuentosSeleccionados){
						codTipMotDes = null;
						codDesSel = null;
						codTipMotDesS = null;
						codTipMotDes = new StringBuffer();
						codDesSel = new StringBuffer();
						//concatena los codigos de los seleccionados y los mostrados para la comparaci\u00F3n
						codTipMotDes.append(codigoTipDes).append(tipoDescuento.getId().getCodigoTipoDescuento()).append(caracterToken).append(codigoMotDes).append(motivoDescuento.getId().getCodigoMotivoDescuento());
						codDesSel.append(codigoTipDes).append(descuentoSeleccionado.getDescuentoDTO().getCodigoTipoDescuento()).append(caracterToken).append(codigoMotDes).append(descuentoSeleccionado.getDescuentoDTO().getCodigoMotivoDescuento());
						codTipMotDesS = codTipMotDes.toString();
						//verifica si las opciones mostradas coinciden con las opciones seleccionadas
						if(codTipMotDesS.equals(codDesSel.toString())){
							opDesSeleccionados[contadorOp] = Integer.toString(contadorTipDes).concat(caracterToken).concat(codTipMotDesS);
							//verifica si es un descuento variable
							if(descuentoSeleccionado.getDescuentoDTO().getCodigoTipoDescuento().equals(parametroDTO.getValorParametro())){
								//TODO Revisar cambio a arreglo -- formulario.setPorcentajeVarDescuento(descuentoSeleccionado.getDescuentoDTO().getPorcentajeDescuento())[0];
//								if(compradorDTOCol!=null && !compradorDTOCol.isEmpty())
//									formulario.setPorcentajeVarDescuento(new Double[compradorDTOCol.size()]);
//							}else {
								formulario.setPorcentajeVarDescuento(new Double[0][0]);
							}
							contadorOp++;
						}
					}
					
				}
				contadorTipDes++;
			}
			//asigna el arreglo de opciones en el formulario
			formulario.setOpDescuentos(opDesSeleccionados);
			caracterToken = null;
			codTipMotDes = null;
			codDesSel = null;
			codTipMotDesS = null;
			parametroDTO = null;
			codigoTipDes = null;
			codigoMotDes = null;
			
		} else {
			//TODO Revisar cambio a arreglo -- formulario.setPorcentajeVarDescuento(new Double(0));
			formulario.setPorcentajeVarDescuento(new Double[0][0]);
		}
	}
	/**
	 * funcion que valida si el detalle del pedido fue alterado en la accion modificacion de la Reserva
	 * @param request
	 * @return flag - Si existen cambios(true) caso contrario no hay cambios(false)
	 */
	public static boolean verificarPreciosDetallePedido(HttpServletRequest request) throws Exception {
		boolean flag = false;
		LogSISPE.getLog().info("--Entro en el Metodo comparar totales");
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
		detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
		List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
		
		session.setAttribute(CotizarReservarAction.COL_DETALLE_PEDIDO_ORIGINAL, detalleOriginal);
		session.setAttribute(CotizarReservarAction.COL_DETALLE_PEDIDO_MODIFICADO, detalleModificado);
				
		Double valorTotalVentaOriginal = 0D;
		Double valorTotalVentaModificado = 0D;

		//Comparacion de precios del detalle original del pedido
		for(int i=0;i<detalleOriginal.size();i++){			
			LogSISPE.getLog().info("detalleO valor totalVenta {}", detalleOriginal.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta());
			//LogSISPE.getLog().info("detalleM valor totalVenta {}", detalleModificado.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta());
			valorTotalVentaOriginal = valorTotalVentaOriginal + detalleOriginal.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
			//valorTotalVentaModificado = valorTotalVentaModificado + detalleModificado.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();					
		}
		
		//Comparacion de precios del detalle modificado del pedido
		for(int i=0;i<detalleModificado.size();i++){			
			//LogSISPE.getLog().info("detalleO valor totalVenta {}", detalleOriginal.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta());
			LogSISPE.getLog().info("detalleM valor totalVenta {}", detalleModificado.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta());
			//valorTotalVentaOriginal = valorTotalVentaOriginal + detalleOriginal.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
			valorTotalVentaModificado = valorTotalVentaModificado + detalleModificado.get(i).getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();					
		}
		LogSISPE.getLog().info("Total1 {}", valorTotalVentaOriginal);
		LogSISPE.getLog().info("Total2 {}", valorTotalVentaModificado);
		
		if(valorTotalVentaOriginal.doubleValue() != valorTotalVentaModificado.doubleValue()){
			// wc el total del pedido aumento
//			if(valorTotalVentaOriginal.doubleValue() < valorTotalVentaModificado.doubleValue())
//				session.setAttribute(RESERVA_AUMENTO_TOTAL, ConstantesGenerales.ESTADO_ACTIVO);
//			else //si el pedido disminuye de precio //0 = disminuye el monto del pedido
//				session.setAttribute(RESERVA_AUMENTO_TOTAL, ConstantesGenerales.ESTADO_INACTIVO);
			flag = true;
		}

		return flag;
	}
	
	/**
	 * Metodo para verificar si el detalle de un pedido se le agregaron o quitaron art&iacute;culos.
	 * @param request
	 * @return 	Tipo de proceso = 1, solo se elimina o se reduce la cantidad de los productos.<br>
	 * 			Tipo de proceso = 2, solo se agrega o se aumenta la cantidad de los productos.<br>
	 * 			Tipo de proceso = 3, se aumenta o se reduce los productos.<br>
	 * @throws Exception 
	 */
	public static String verificarCambioDetallePedido(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		
		String tipoProceso = ConstantesGenerales.ESTADO_INACTIVO;
		int contadorNuevos = 0;
		int contadorEliminados = 0;
		Long cantMod = 0L;
		Long cantOrg = 0L;
		
		Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
		detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
		List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
		
		//si se existen cambios en los canastos especiales
		for (DetallePedidoDTO detallePedidoDTOMod : detallePedidoModificado){
			//solo si ya se creo el canasto especial, caso contrario se procesa como que solo se agreg\u00F3 un nuevo articulo
			if (detallePedidoDTOMod.getArticuloDTO().getNpNuevoCodigoClasificacion()!=null && detallePedidoDTOMod.getArticuloDTO().getNpNuevoCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasElaboradas"))
					&& detallePedidoDTOMod.getNpExisteCambioCanastoEspecial()){
				return ConstantesGenerales.TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
			}
		}
				//si se elimino algun articulo del pedido
		for (DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
			if (ColeccionesUtil.simpleSearch("articuloDTO.id.codigoArticulo", 
					detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo(), detalleModificado).isEmpty()){
				contadorEliminados ++;
			}
		}
		//se verifica si el articulo esta en las dos colecciones
		for (DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
			//se busca articulos nuevos en el pedido
			if (ColeccionesUtil.simpleSearch("articuloDTO.id.codigoArticulo", 
					detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo(), detalleOriginal).isEmpty()){
				contadorNuevos ++;
			} else {
				//si el articulo esta en las dos colecciones se verifica si cambiaron las cantidades
				for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
					
					//si los articulos en los pedidos son iguales
					if(detallePedidoDTOMod.getId().getCodigoArticulo().equals(detallePedidoDTOOrg.getId().getCodigoArticulo())){
						
						cantMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getCantidadEstado();
						cantOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getCantidadEstado();
						
						//se verifica si las cantidades de los articulos han cambiado
						if(!cantMod.equals(cantOrg)){
							
							if(cantMod>cantOrg){
								contadorNuevos++;
							}
							else if (cantMod<cantOrg){ 
								contadorEliminados++;
							}
						}
						cantMod = 0L;
						cantOrg = 0L;
					}
				}
			}
		}

		if(contadorEliminados>0 && contadorNuevos==0){
			tipoProceso = ConstantesGenerales.TIPO_PROCESO_QUITAR_PRODUCTOS;
		}else if (contadorNuevos>0 && contadorEliminados==0) {
			tipoProceso = ConstantesGenerales.TIPO_PROCESO_AUMENTAR_PRODUCTOS;
		}else if (contadorNuevos>0 && contadorEliminados>0) {
			tipoProceso = ConstantesGenerales.TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS;
		}
		
		return tipoProceso;
	}
	
	/**
	 * funcion que valida si el detalle del pedido fue alterado en la accion modificacion de la Reserva
	 * @param request
	 * @return Si existen cambios(true) caso contrario no hay cambios(false)
	 */
	public static boolean verificarCambiosDetallePedido(HttpServletRequest request) throws Exception {
		boolean flag = false;
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
		detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
		List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
		
		if(detalleOriginal.size() != detalleModificado.size()){
			LogSISPE.getLog().info("--Opcion1-- por comparacion tama\u00F1o Detalle");
			flag = true;
		}else {
			//Comparacion de cantidades
			for(int i=0;i<detalleOriginal.size();i++){
				LogSISPE.getLog().info("detalleO codigo {}", detalleOriginal.get(i).getArticuloDTO().getId().getCodigoArticulo());
				LogSISPE.getLog().info("detalleM codigo {}", detalleModificado.get(i).getArticuloDTO().getId().getCodigoArticulo());
				LogSISPE.getLog().info("detalleO cantidad {}", detalleOriginal.get(i).getEstadoDetallePedidoDTO().getCantidadEstado());
				LogSISPE.getLog().info("detalleM cantidad {}", detalleModificado.get(i).getEstadoDetallePedidoDTO().getCantidadEstado());
				if(detalleOriginal.get(i).getArticuloDTO().getId().getCodigoArticulo().equals(detalleModificado.get(i).getArticuloDTO().getId().getCodigoArticulo())){
					Long cant1 = detalleOriginal.get(i).getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					Long cant2 = detalleModificado.get(i).getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					if(cant1.longValue() != cant2.longValue()){
						LogSISPE.getLog().info("--Entro opcion2 por comparacion cantidades detalle--");
						flag= true;
						break;
					}else{
						//Si es un canasto modificado
						if(detalleOriginal.get(i).getArticuloDTO().getCodigoArticuloOriginal() != null){
							if(detalleOriginal.get(i).getArticuloDTO().getArticuloRelacionCol().size() != detalleModificado.get(i).getArticuloDTO().getArticuloRelacionCol().size()){
								LogSISPE.getLog().info("--Entro opcion4 por comparacion tama\u00F1o Items Canasto--");
								flag = true;
								break;
							}else {
								LogSISPE.getLog().info("Entro a comparar los codigos y cantidades del Items de la receta");
								List<ArticuloRelacionDTO> recetaO = (ArrayList<ArticuloRelacionDTO>)detalleOriginal.get(i).getArticuloDTO().getArticuloRelacionCol();
								List<ArticuloRelacionDTO> recetaM = (ArrayList<ArticuloRelacionDTO>)detalleModificado.get(i).getArticuloDTO().getArticuloRelacionCol();
								for(int j=0; j<recetaO.size(); j++){
									if(recetaO.get(j).getArticuloRelacion().getId().getCodigoArticulo().equals(recetaM.get(j).getArticuloRelacion().getId().getCodigoArticulo())){
										Long cantR1 = recetaO.get(j).getCantidad().longValue();
										Long cantR2 = recetaM.get(j).getCantidad().longValue();
										if(cantR1.longValue() != cantR2.longValue()){
											LogSISPE.getLog().info("--Entro opcion6--");
											flag = true;
											break;
										}	
									}else{
										LogSISPE.getLog().info("--Entro opcion7--");
										flag = true;
										break;
									}
								}
							}
						}else if(detalleModificado.get(i).getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
							LogSISPE.getLog().info("--Entro opcion5 canasta modificada--");
							flag = true;
							break;
						}
					}	
				}else{
					LogSISPE.getLog().info("--Entro opcion3 por comparacion de codigos Articulo detalle--");
					flag = true;
					break;
				}		
			}
		}
		return flag;
	}
	/**
	 * funcion que valida si el detalle del pedido fue alterado en la accion modificacion de la Reserva
	 * @param request
	 * @return Si existen cambios(true) caso contrario no hay cambios(false)
	 */
	public static boolean verificarCambiosEntregas(HttpServletRequest request, String entidadResponsable) throws Exception {
		boolean flag = false;
		LogSISPE.getLog().info("--Entro en el Metodo comparar cambios en las configuraciones de las entregas");
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> detallePedidoOriginalEntregas = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
		detallePedidoOriginalEntregas = ColeccionesUtil.sort(detallePedidoOriginalEntregas, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		Collection<DetallePedidoDTO> detallePedidoModificadoEntregas = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		detallePedidoModificadoEntregas = ColeccionesUtil.sort(detallePedidoModificadoEntregas, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
		
		List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginalEntregas;
		List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificadoEntregas;
		VistaPedidoDTO vistaPedidoDTOAntes = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO_AUX);
		if(!vistaPedidoDTOAntes.getEntidadResponsable().equals(entidadResponsable)){
			LogSISPE.getLog().info("--Opcion-- por cambio entidadResponsable");
			flag = true;
		}else{
			if(detalleOriginal.size() != detalleModificado.size()){
				LogSISPE.getLog().info("--Opcion0-- por comparacion tama\u00F1o del detalle pedido");
				flag = true;
			} else{
				for (int i = 0; i < detalleOriginal.size(); i++){
					DetallePedidoDTO detallePedidoDTO1 = detalleOriginal.get(i);
					DetallePedidoDTO detallePedidoDTO2 = detalleModificado.get(i);
					if(detallePedidoDTO1.getEntregaDetallePedidoCol() != null && detallePedidoDTO2.getEntregaDetallePedidoCol() != null && detallePedidoDTO1.getEntregaDetallePedidoCol().size() != detallePedidoDTO2.getEntregaDetallePedidoCol().size()){
						LogSISPE.getLog().info("--Opcion1-- por comparacion tama\u00F1o entregas");
						flag = true;
						break;
					}else if (detallePedidoDTO1.getEntregaDetallePedidoCol() != null && detallePedidoDTO2.getEntregaDetallePedidoCol() != null && detallePedidoDTO1.getEntregaDetallePedidoCol().size()>0){
						Collection<EntregaDetallePedidoDTO> entregas1 = (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO1.getEntregaDetallePedidoCol();
						entregas1 = ColeccionesUtil.sort(entregas1, ColeccionesUtil.ORDEN_ASC, "codigoArticulo");
						Collection<EntregaDetallePedidoDTO> entregas2 = (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO2.getEntregaDetallePedidoCol();
						entregas2 = ColeccionesUtil.sort(entregas2, ColeccionesUtil.ORDEN_ASC, "codigoArticulo");
						List<EntregaDetallePedidoDTO> entregaOriginal = (ArrayList<EntregaDetallePedidoDTO>)entregas1;
						List<EntregaDetallePedidoDTO> entregaModificado = (ArrayList<EntregaDetallePedidoDTO>)entregas2;
						for (int j = 0; j < entregaOriginal.size(); j++) {
							//fecha entregaCliente0
							Calendar fechaEntregaClienteO = new GregorianCalendar();
							fechaEntregaClienteO.setTimeInMillis(entregaOriginal.get(j).getEntregaPedidoDTO().getFechaEntregaCliente().getTime());
							//fecha entregaClienteM
							Calendar fechaEntregaClienteM = new GregorianCalendar();
							fechaEntregaClienteM.setTimeInMillis(entregaModificado.get(j).getEntregaPedidoDTO().getFechaEntregaCliente().getTime());
							//fecha despachoBODEGA
							Calendar fechaDespachoO = new GregorianCalendar();
							fechaDespachoO.setTimeInMillis(entregaOriginal.get(j).getEntregaPedidoDTO().getFechaDespachoBodega().getTime());
							//fecha entregaClienteM
							Calendar fechaDespachoM = new GregorianCalendar();
							fechaDespachoM.setTimeInMillis(entregaModificado.get(j).getEntregaPedidoDTO().getFechaDespachoBodega().getTime());
							
							LogSISPE.getLog().info("--codigoArticuloO--{}",entregaOriginal.get(j).getCodigoArticulo());
							LogSISPE.getLog().info("--codigoArticuloM--{}",entregaModificado.get(j).getCodigoArticulo());
							LogSISPE.getLog().info("--tipoEntregaO--{}",entregaOriginal.get(j).getEntregaPedidoDTO().getTipoEntrega());
							LogSISPE.getLog().info("--tipoEntregaM--{}",entregaModificado.get(j).getEntregaPedidoDTO().getTipoEntrega());
							if(!entregaOriginal.get(j).getEntregaPedidoDTO().getTipoEntrega().equals(entregaModificado.get(j).getEntregaPedidoDTO().getTipoEntrega())){
								LogSISPE.getLog().info("--Opcion2-- por comparacion tipoEntrega");
								flag = true;
								//break;
							}else if (ManejoFechas.restarFechas(entregaOriginal.get(j).getEntregaPedidoDTO().getFechaEntregaCliente(), entregaModificado.get(j).getEntregaPedidoDTO().getFechaEntregaCliente())!=0D){//!ManejoFechas.compararCalendariosPorFecha(fechaEntregaClienteO,fechaEntregaClienteM)){
								LogSISPE.getLog().info("--Opcion3-- por comparacion fechaEntregaCliente");
								flag = true;
								break;
							}else if (!ManejoFechas.compararCalendariosPorFecha(fechaDespachoO,fechaDespachoM)){
								LogSISPE.getLog().info("--Opcion4-- por comparacion fechaDespachoBodega");
								flag = true;
								break;
							}else if (!entregaOriginal.get(j).getEntregaPedidoDTO().getDireccionEntrega().equals(entregaModificado.get(j).getEntregaPedidoDTO().getDireccionEntrega())){
								LogSISPE.getLog().info("--Opcion8-- por comparacion direcciones");
								flag = true;
								break;
							}else if (entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() !=  entregaModificado.get(j).getEntregaPedidoDTO().getCodigoContextoEntrega().intValue()){
								LogSISPE.getLog().info("--Opcion9-- por comparacion codigoContextoEntrega");
								flag = true;
								break;
							}else if (entregaOriginal.get(j).getCantidadEntrega().longValue() !=  entregaModificado.get(j).getCantidadEntrega().longValue()){
								LogSISPE.getLog().info("--Opcion10-- por comparacion cantidadEntrega");
								flag = true;
								break;
							}else if (entregaOriginal.get(j).getCantidadDespacho().longValue() !=  entregaModificado.get(j).getCantidadDespacho().longValue()){
								LogSISPE.getLog().info("--Opcion11-- por comparacion cantidaddepacho");
								flag = true;
								break;
							}
							//Comparacion por separado por la 1ra condicion que podria ser diferente de null y separa los demas else if.
							if (entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega() != null && entregaModificado.get(j).getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega() != null){
								if(entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().intValue() !=  entregaModificado.get(j).getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().intValue()){
									LogSISPE.getLog().info("--Opcion5-- por comparacion codigoLocalEntrega");
									flag = true;
									break;
								}	
							} 
							if(entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoLocalSector() != null && entregaModificado.get(j).getEntregaPedidoDTO().getCodigoLocalSector() != null){						
								if(entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoLocalSector().intValue() !=entregaModificado.get(j).getEntregaPedidoDTO().getCodigoLocalSector()){
									LogSISPE.getLog().info("--Opcion6-- por comparacion codigoLocalSector");
									flag = true;
									break;
								}
							}
							if(entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoDivGeoPol() != null && entregaModificado.get(j).getEntregaPedidoDTO().getCodigoDivGeoPol() != null){
								if(!entregaOriginal.get(j).getEntregaPedidoDTO().getCodigoDivGeoPol().equals(entregaModificado.get(j).getEntregaPedidoDTO().getCodigoDivGeoPol())){
									LogSISPE.getLog().info("--Opcion7-- por comparacion codigoSectorEntrega");
									flag = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}
	/**
	 * funcion que valida los abonos del pedido  en la accion modificacion de la Reserva
	 * @param request
	 * @return Si existen cambios(true) caso contrario no hay cambios(false)
	 */
	public static boolean verificarAbonoPedido(HttpServletRequest request, CotizarReservarForm formulario) throws Exception {
		boolean flag = false;
		LogSISPE.getLog().info("--Entro en el Metodo comparar los abonos del pedido");
		HttpSession session = request.getSession();
		//se obtiene la vistaPedidoDTO
		VistaPedidoDTO vistaPedidoDTO1 = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		if(session.getAttribute("ec.com.smx.sic.sispe.modificarReserva.sinPago") != null){
			Double abonoManualFormateado = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()), NUMERO_DECIMALES);
			LogSISPE.getLog().info("Valor abonoInicialManual{}", vistaPedidoDTO1.getValorAbonoInicialManual());
			LogSISPE.getLog().info("Valor abonoManulInicial Ingresado{}", abonoManualFormateado.doubleValue());
			if(vistaPedidoDTO1.getValorAbonoInicialManual() != null && vistaPedidoDTO1.getValorAbonoInicialManual().doubleValue() != abonoManualFormateado.doubleValue()){
				LogSISPE.getLog().info("Opcion1 abonosManualInicial distintos");
				flag = true;
			}
		}
		return flag;
	}	
	
	/** 
	 * @param request
	 * @throws Exception 
	 * @throws SISPEException 
	 */
	public static String obtenerAutorizacionAbonoStock(HttpServletRequest request, String pedido, String secuencialEstPed, Integer codigoLocal) throws SISPEException, Exception{
//		boolean flag = false;
		StringBuffer cadena = new StringBuffer();
		LogSISPE.getLog().info("M\u00E9todo que obtiene la autorizaci\u00F3n registrada de tipo (Stock y Abono)");
		LogSISPE.getLog().info("CodigoPedido->{}",pedido);
		LogSISPE.getLog().info("Secuencial Estado Pedido->{}",secuencialEstPed);
		LogSISPE.getLog().info("Codigo Local->{}",codigoLocal);
		
		try{
			
			AutorizacionEstadoPedidoDTO filtroDTO = new AutorizacionEstadoPedidoDTO();
			filtroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			filtroDTO.setCodigoLocal(codigoLocal);
			filtroDTO.setSecuencialEstadoPedido(secuencialEstPed);
			filtroDTO.setCodigoPedido(pedido);
			filtroDTO.setEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo.corporativo"));
			List<AutorizacionEstadoPedidoDTO> autorizacionEstadoPedidoDTOCol = (ArrayList<AutorizacionEstadoPedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerAutorizacionEstadoPedidoDTO(filtroDTO);
			
			//se consultas las nuevas autorizaciones
			if(CollectionUtils.isNotEmpty(autorizacionEstadoPedidoDTOCol)){
				
				Long codigoTipAutAbonoCero = ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO.longValue();
				ec.com.smx.autorizaciones.dto.AutorizacionDTO plantillaAut = null;
				
				Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesPedidoCol = 
						(Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL); 
				
				//se inicializa la lista de autorizaciones
				if(CollectionUtils.isEmpty(autorizacionesPedidoCol)){
					autorizacionesPedidoCol = new ArrayList<ec.com.smx.autorizaciones.dto.AutorizacionDTO>();
				}
				
				//se recorre los objetos de la lista
				for(AutorizacionEstadoPedidoDTO autorizacionEstPedDTO : autorizacionEstadoPedidoDTOCol){
					plantillaAut = new ec.com.smx.autorizaciones.dto.AutorizacionDTO();
					plantillaAut.getId().setCodigoAutorizacion(autorizacionEstPedDTO.getCodigoSistemaAutorizacion());
					plantillaAut.getId().setCodigoCompania(autorizacionEstPedDTO.getId().getCodigoCompania());
					plantillaAut.getId().setCodigoSistema(autorizacionEstPedDTO.getCodigoSistema());
					
					ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = SISPEFactory.getDataService().findUnique(plantillaAut);
					
					if(autorizacionDTO != null &&  autorizacionDTO.getCodigoTipoAutorizacion() != null){
						
						if(autorizacionDTO.getCodigoTipoAutorizacion().longValue() == codigoTipAutAbonoCero.longValue()
								&& !AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, codigoTipAutAbonoCero)){
							
							LogSISPE.getLog().info("---Si tiene una autorizacion de tipo stock o abono {}->",autorizacionDTO.getId().getCodigoAutorizacion());
							autorizacionesPedidoCol.add(autorizacionDTO);
							cadena.append("stock").append(SEPARADOR_TOKEN);
						}
						else if(autorizacionDTO.getCodigoTipoAutorizacion().longValue() == 
								ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue()
								&& !AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)){
							
							LogSISPE.getLog().info("tiene una autorizacion para disminuir fecha minima de entrega {}->",autorizacionDTO.getId().getCodigoAutorizacion());
							autorizacionesPedidoCol.add(autorizacionDTO);
						}
						else if(autorizacionDTO.getCodigoTipoAutorizacion().longValue() == 
								ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL.longValue()
								&& !AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL)){
							
							LogSISPE.getLog().info("tiene una autorizacion para reservar bodega de local {}->",autorizacionDTO.getId().getCodigoAutorizacion());
							autorizacionesPedidoCol.add(autorizacionDTO);
						}
					}
				}
				//se sube a sesion la coleccion de autorizaciones genericas
				request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL, autorizacionesPedidoCol);
			}
			
//			if(autorizacionEstadoPedidoDTOCol != null && !autorizacionEstadoPedidoDTOCol.isEmpty()){
//				AutorizacionDTO plantillaAut = null;
//				for(AutorizacionEstadoPedidoDTO autorizacionEstPedDTO : autorizacionEstadoPedidoDTOCol){
//					plantillaAut = new AutorizacionDTO();
//					plantillaAut.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//					plantillaAut.getId().setCodigoAutorizacion(autorizacionEstPedDTO.getCodigoSistemaAutorizacion().toString());
//					plantillaAut.setGrupoTipoAutorizacionDTO(new GrupoTipoAutorizacionDTO());
//					plantillaAut.getGrupoTipoAutorizacionDTO().setTipoAutorizacionDTO(new TipoAutorizacionDTO());				
//					AutorizacionDTO autorizacionDTO = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerAutorizacion(plantillaAut);
//					LogSISPE.getLog().info("----Codigo AutorizacionDTO----{}",autorizacionDTO.getId().getCodigoAutorizacion());
//					LogSISPE.getLog().info("----tipo AutorizacionDTO----{}",autorizacionDTO.getSecuencialGrupoTipoAutorizacion());
//					if(autorizacionDTO != null && autorizacionDTO.getSecuencialGrupoTipoAutorizacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.abonoStock"))){
//						LogSISPE.getLog().info("---Si tiene una autorizacion de tipo stock o abono {}->",autorizacionDTO.getId().getCodigoAutorizacion());
//						AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO,request);
//						if(!flag){
//							cadena.append("stock").append("-");
//						}else{
//							cadena.append("stock");
//						}
//						flag = true;
//					}
//					if(autorizacionDTO != null && autorizacionDTO.getSecuencialGrupoTipoAutorizacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.stockPavPolCan"))){
//						LogSISPE.getLog().info("---Si tiene una autorizacion de tipo stock (pavos,pollos,canastas) {}->",autorizacionDTO.getId().getCodigoAutorizacion());
//						AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO,request);
//						request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACION_STOCK_PAVPOLCAN, autorizacionDTO);
//						//Nota si tiene mas autorizaciones de tipo (Stock o abono solo le asigna la 1ra el nuevo pedido)
//						if(!flag){
//							cadena.append("stockPavPolCan").append("-");
//						}else{
//							cadena.append("stockPavPolCan");
//						}
//						flag = true;
//					}
//				}	
//			}
			
			//bgudino
			//se comenta estas lineas para que siempre vuelva a pedir autorizacion de stock
			//Se consulta si tiene autorizaciones
			DetalleEstadoPedidoAutorizacionDTO  estadoPedidoAut = new DetalleEstadoPedidoAutorizacionDTO();
			estadoPedidoAut.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			estadoPedidoAut.getId().setCodigoPedido(pedido);
			estadoPedidoAut.getId().setSecuencialEstadoPedido(secuencialEstPed);
//			estadoPedidoAut.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo()); 
			estadoPedidoAut.getId().setCodigoAreaTrabajo(codigoLocal);
			estadoPedidoAut.setEstadoPedidoAutorizacionDTO(new EstadoPedidoAutorizacionDTO());
			//relacion con autorizaciones y autorizaciones hijas
			AutorizacionDTO autorizacionDTO = new AutorizacionDTO();
			autorizacionDTO.setAutorizacionesHijas(new HashSet<AutorizacionDTO>());
			AutorizacionDTO autorizacionHijaDTO = new AutorizacionDTO();
			autorizacionHijaDTO.setUsuarioAutorizadorDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
			autorizacionHijaDTO.setUsuarioAutorizadoDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
			autorizacionHijaDTO.setAutorizacionesHijas(new HashSet<AutorizacionDTO>());
			autorizacionHijaDTO.getAutorizacionesHijas().add(new AutorizacionDTO());
			autorizacionDTO.getAutorizacionesHijas().add(autorizacionHijaDTO);
			estadoPedidoAut.getEstadoPedidoAutorizacionDTO().setAutorizacionDTO(autorizacionDTO);
			estadoPedidoAut.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			Boolean encontroAutorizacion = Boolean.FALSE;
			Collection<DetalleEstadoPedidoAutorizacionDTO> detallePedidoAutorizacionCol = SISPEFactory.getDataService().findObjects(estadoPedidoAut);
			if(CollectionUtils.isNotEmpty(detallePedidoAutorizacionCol)){
				Collection<EstadoPedidoAutorizacionDTO> colaOtrasAutorizaciones = new ArrayList<EstadoPedidoAutorizacionDTO>();
				
				Long secuencial = 1L;
				for(DetalleEstadoPedidoAutorizacionDTO resultado : detallePedidoAutorizacionCol){
					resultado.getEstadoPedidoAutorizacionDTO().setNpTipoAutorizacion(resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion());
					if(resultado.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
						
						//se controla que no se agregen autorizaciones repetidas
						if(!colaOtrasAutorizaciones.contains(resultado.getEstadoPedidoAutorizacionDTO())){
							
							//Objeto que se debe enviar al componente de autorizaciones
							Autorizacion autorizacion = new Autorizacion();
							autorizacion.setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
							autorizacion.setAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
							autorizacion.setEstadoInicial(null);
							autorizacion.setSecuencial(secuencial);
							autorizacion.setCodigoTipoAutorizacion(resultado.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion());
							autorizacion.setCodigoAutorizacion(resultado.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());
							
							String nombreAutorizador = ""; 
							if(resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null 
									&& resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getUsuarioAutorizador() != null){
							  	//se consultan el nombre del funcionario
						    	FuncionarioDTO funcionarioConsultaDTO = new FuncionarioDTO();
						    	funcionarioConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						    	funcionarioConsultaDTO.setUsuarioFuncionario(resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getUsuarioAutorizador());
						    	funcionarioConsultaDTO.setPersonaDTO(new PersonaDTO());
						    	
						     	FuncionarioDTO funcionarioDTO = SISPEFactory.getDataService().findUnique(funcionarioConsultaDTO);
						     	
						     	if(funcionarioDTO != null && funcionarioDTO.getPersonaDTO() != null){
						     		nombreAutorizador = " - "+(StringUtils.isNotEmpty(funcionarioDTO.getPersonaDTO().getPrimerNombre()) ? funcionarioDTO.getPersonaDTO().getPrimerNombre() : "NA")
						     				+" "+(StringUtils.isNotEmpty(funcionarioDTO.getPersonaDTO().getPrimerApellido()) ? funcionarioDTO.getPersonaDTO().getPrimerApellido() : "NA");
						     	}else{
						     		nombreAutorizador = "SIN ASIGNAR";
						     	}
						    	LogSISPE.getLog().info("autorizador: {}", nombreAutorizador);
							}
					  
							autorizacion.setTituloAutorizacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.titulo.autorizacion.stock")+nombreAutorizador);
							//se setean las autorizaciones hijas
							if(CollectionUtils.isNotEmpty(resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getAutorizacionesHijas())){
								Set<AutorizacionDTO> autorizacionesHijasDTO = resultado.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getAutorizacionesHijas();
								List<AutorizacionDTO>  temp =   new ArrayList<AutorizacionDTO>(autorizacionesHijasDTO); 
								Collection<Autorizacion> autorizacionesHijas = AutorizacionesFactory.getInstancia().getaIAutorizacionesServicio().transTransformAutorizacionDTOToAutorizacion(temp);
								LogSISPE.getLog().info("la autorizacion "+ resultado.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion()+" tienene: "+autorizacionesHijas.size()+" aut. hijas" );
								autorizacion.setAutorizacionesHijas(new HashSet<Autorizacion>(autorizacionesHijas));	
							}
							resultado.getEstadoPedidoAutorizacionDTO().setNpAutorizacion(autorizacion);
							colaOtrasAutorizaciones.add(resultado.getEstadoPedidoAutorizacionDTO());
							secuencial++;
						}
						encontroAutorizacion = Boolean.TRUE;
						LogSISPE.getLog().info("---Si tiene una autorizacion de tipo stock o abono {}->", resultado.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion());
					}
				}
				if(encontroAutorizacion){
					cadena.append("stockPavPolCan");
					request.getSession().setAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK, colaOtrasAutorizaciones);
//					request.getSession().setAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK_RESPALDO, colaOtrasAutorizaciones);
				}
			} else{		
				LogSISPE.getLog().info("---No tiene una autorizacion de tipo stock o abono");
			}
			
		}catch(Exception e){
			LogSISPE.getLog().error("Error al obtenerAutorizacionAbonoStock, "+e);
		}
		
		return cadena.toString();
	}
	
	/**
	 * funcion que valida si existe en el pedido las cantidades minimas de articulos(1602 canastas de catalago) y cumplan para que la bod sea el responsable.
	 * @param request
	 * @return opciones de mensajes validadores en la reserva y modificacionReserva
	 */
	public static String verificaDesLocEntResp(HttpServletRequest request, PedidoDTO pedidoDTO, String tipoEntrega) throws Exception {
		boolean flag = false;
		String valorValidacion;
		LogSISPE.getLog().info("--Entro en la funcion verificaCanastasSoloCatalago y Cantidades");
		HttpSession session = request.getSession();
		EstadoDetallePedidoDTO estadoDetallePedidoDTO = null;
		Long cantidadTotalCan = 0L;
		
		long cantidadTotalArticuloEspecial = 0;
		Double totalEntregaTotalSeleccionada = 0D;//solo se llena cuando son entregas totales
		Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
		
		boolean siCanastaMayor50 = false;
		boolean esDespachoLocal = true;
		//se obtiene los parametros desde el properties
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
		String codClaCanastos = parametroDTO.getValorParametro();//1602
		
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
		String codClaRecetasNuevas = parametroDTO.getValorParametro();//1606
		
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
		String cantidadCanasta = parametroDTO.getValorParametro();//50
		
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadTotalArticulosValidaResponsablePedido", request);
		String cantidadTotalCanastas = parametroDTO.getValorParametro();//80
		
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
		Double montoMinimoEntregaDomicilioCD = Double.valueOf(parametroDTO.getValorParametro());//1000
		
		Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);

		if(detallePedidoCol != null && !detallePedidoCol.isEmpty()){
			
			String codigoPedido = detallePedidoCol.iterator().next().getId().getCodigoPedido();
			
			//si es pedido consolidado, se agrega al pedido actual los otros pedidos consolidados
			//para validar la entidad responsable con el grupo de consolidados
			if(ConsolidarAction.esPedidoConsolidado(request)){
				
				ArrayList<DetallePedidoDTO> detalleReservacionConsolidado = new ArrayList<DetallePedidoDTO>(detallePedidoCol); 
				detalleReservacionConsolidado.addAll(ConsolidarAction.obtenerDetallePedidoElaboraCD(request, detallePedidoCol.iterator().next().getId().getCodigoPedido()));
				detallePedidoCol = new ArrayList<DetallePedidoDTO>(detalleReservacionConsolidado);
			}
			
			//Validacion de DespachosLocal solo canastasCatalago
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
				estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
				Collection<EntregaDetallePedidoDTO> entDetPedCol = (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol();
				if(entDetPedCol==null){
					entDetPedCol = new ArrayList<EntregaDetallePedidoDTO>();
				}
				//verifico que solo sea un canasto de catalago y tipoEntregas.
				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos) && detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() == null){
					LogSISPE.getLog().info("Entra a opcion solo canastos catalago");
					for(EntregaDetallePedidoDTO entDetPedDTO : entDetPedCol){
						if(entDetPedDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion1"))&& 
								(entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
							esDespachoLocal = false;
							break;
						}
					}
					if(!esDespachoLocal){
						//Sale del 1er for
						break;
					}
				}else if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
					//Sale por canastas especiales
					esDespachoLocal = false;
					break;
				}else{
					//se verifica si al menos un articulo tiene entrega a domicilio sin importar que sea canastos yque cumpla con las condiciones de cantidades
					for(EntregaDetallePedidoDTO entDetPedDTO : entDetPedCol){
						if(entDetPedDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion1"))&& 
								(entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
							if(estadoDetallePedidoDTO.getCantidadEstado().longValue() >= Integer.parseInt(cantidadCanasta)){
								siCanastaMayor50 = true;
							}
						}
					}
					//Sale por otros articulos
					esDespachoLocal = false;
					break;
				}
			}
			//Validacion de cantidades para que la bodega sea el responsable
			for(DetallePedidoDTO detallePedidoDTO1 : detallePedidoCol){
				
				estadoDetallePedidoDTO = detallePedidoDTO1.getEstadoDetallePedidoDTO();
				totalEntregaParcialSeleccionada = UtilesSISPE.valorEntregaCDBultos(detallePedidoDTO1);
				
				//para el caso que el pedido consolidado no tenga configuradas entregas
				if(totalEntregaParcialSeleccionada.longValue() == 0 && !detallePedidoDTO1.getId().getCodigoPedido().equals(codigoPedido)){
					totalEntregaParcialSeleccionada = UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO1);
				}
				totalEntregaTotalSeleccionada += totalEntregaParcialSeleccionada;
				
				
				if(detallePedidoDTO1.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos) || detallePedidoDTO1.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas)){
					cantidadTotalCan = cantidadTotalCan + estadoDetallePedidoDTO.getCantidadEstado().longValue();
					if(estadoDetallePedidoDTO.getCantidadEstado().longValue() >= Integer.parseInt(cantidadCanasta)){
						siCanastaMayor50 = true;
					}
					
					if(detallePedidoDTO1.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasElaboradas"))){
						//cantidadTotalArticuloEspecial= cantidadTotalArticuloEspecial + estadoDetallePedidoDTO.getCantidadEstado().longValue();
						
						if(tipoEntrega!=null && tipoEntrega.equals(ConstantesGenerales.TIPO_ENTREGA_PARCIAL) && detallePedidoDTO1.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue() >= 0){
							if(detallePedidoDTO1.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()!=0){
								cantidadTotalArticuloEspecial = cantidadTotalArticuloEspecial + UtilesSISPE.calcularCantidadBultos(detallePedidoDTO1.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue(), detallePedidoDTO1.getArticuloDTO());
							}
						}else{
							cantidadTotalArticuloEspecial= cantidadTotalArticuloEspecial + detallePedidoDTO1.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
						}
						
					}
				}	
			}
			//	RETORNO true si NO CUMPLE LAS CONDICIONES MINIMAS PARA QUE LA BODEGA SEA EL RESPONSABLE.
			if(!siCanastaMayor50 && cantidadTotalCan < Integer.parseInt(cantidadTotalCanastas)){
				flag = true;
			}
			
			//nueva validacion, solo si la sumatoria total de cantidad especiales es mas de 50 se identifica que el responsable es bodega o el valor total de la entrega sea mayor a 1000
			if ((cantidadTotalArticuloEspecial >= Integer.parseInt(cantidadCanasta)) || totalEntregaTotalSeleccionada.longValue()>0 && totalEntregaTotalSeleccionada.longValue() >= montoMinimoEntregaDomicilioCD.longValue()){
				flag = false;
			}
		}
		if((esDespachoLocal && !flag)){
			//popUp Despachos locales y canastas solo de catalago cumplen
			valorValidacion = "ok2";
		}else if((esDespachoLocal && flag)){//|| (canastoCatalogo && !esDespachoLocal)){
			//popUp Despachos locales y canastas solo de catalago no cumplen las cantidades
			valorValidacion = "ok1";
		}else if(!esDespachoLocal && flag){
			//popUp Cantidades minimas no cumplen
			valorValidacion = "ok1";
		}else{
			//popUp cumplen ambas validaciones
			valorValidacion = "ok3";
		}
		return valorValidacion;
	}
	
	/**
	 * 
	 * @param cantidadCanastaDetalle
	 * @param recetaArticuloDTO
	 * @param request
	 * @param verificarIntercambioPrecios
	 * @throws Exception
	 */
	public static void calcularValoresDetalleCanastaEspecial(EstadoDetallePedidoDTO estadoDetallePedidoDTO, ArticuloRelacionDTO recetaArticuloDTO, HttpServletRequest request, boolean verificarIntercambioPrecios)throws Exception{
		
		boolean esActivadoPrecioMayoristaArticulo = false;
		Integer codigoLocal = (Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
		//variable para controlar el c\u00E1lculo con precios de afiliado o no
		boolean preciosAfiliado = true;
		if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
			preciosAfiliado = false;
		}
		
		//se inicializan los totales del detalle
		double totalDetalle = 0;
		double totalDetalleIVA = 0;
		
		//calcula el valor total de items de ese producto por canasta
		Long cantidadTotalItem = estadoDetallePedidoDTO.getCantidadEstado().longValue() * recetaArticuloDTO.getCantidad().longValue();

		//se llama al m\u00E9todo que realiza el control de asignaci\u00F3n precios, cuando 
		//pueden cambiar los valores del pedido por precios actuales diferentes
		//controlarRecalculoPorDiferenciaPrecios(detallePedidoDTO, request, verificarIntercambioPrecios);

		//se inicializan los precios
		//double precioUnitario = recetaArticuloDTO.getPrecioUnitario();
		double precioUnitario = recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
		//double precioUnitarioIVA = recetaArticuloDTO.getPrecioUnitarioIVA();
		double precioUnitarioIVA = recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp();
		double precioCaja = recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja()?recetaArticuloDTO.getArticuloRelacion().getPrecioCaja():0;
		double precioCajaIVA = recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja()?recetaArticuloDTO.getArticuloRelacion().getPrecioCajaImp():0;
		//se determina los precios que se deben usar
		if(!preciosAfiliado){
			precioUnitario = UtilesSISPE.aumentarPorcentajeAPrecio(precioUnitario, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
			precioUnitarioIVA = UtilesSISPE.aumentarPorcentajeAPrecio(precioUnitarioIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
			precioCaja = UtilesSISPE.aumentarPorcentajeAPrecio(precioCaja, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
			precioCajaIVA = UtilesSISPE.aumentarPorcentajeAPrecio(precioCajaIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
		}
		
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
			//se verifica si se debe aplicar el precio de mayorista
			if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo() 
					&& esArticuloActivoParaPrecioMayorista(codigoLocal, recetaArticuloDTO.getArticuloRelacion().getCantidadMayoreo(), recetaArticuloDTO.getArticuloRelacion().getPrecioMayorista(), 
					cantidadTotalItem ,recetaArticuloDTO.getArticuloRelacion(), request)){
				precioUnitario = recetaArticuloDTO.getArticuloRelacion().getPrecioMayorista();
				precioUnitarioIVA = recetaArticuloDTO.getArticuloRelacion().getPrecioMayoristaImp();
				if(!preciosAfiliado){
					precioUnitario = UtilesSISPE.aumentarPorcentajeAPrecio(precioUnitario, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
					precioUnitarioIVA = UtilesSISPE.aumentarPorcentajeAPrecio(precioUnitarioIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request));
				}
				esActivadoPrecioMayoristaArticulo = true;
			}
		}
		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && 
				esArticuloActivoParaPrecioCaja(codigoLocal, recetaArticuloDTO.getArticuloRelacion().getPrecioCaja(), recetaArticuloDTO.getArticuloRelacion(), request)){
			//se obtiene el cociente entero de la divisi\u00F3n entre la cantidad y la unidad de manejo
			long cociente = cantidadTotalItem.longValue() / recetaArticuloDTO.getArticuloRelacion().getUnidadManejoPrecioCaja().longValue();
			//se calcula el residuo de la divisi\u00F3n
			long residuo = cantidadTotalItem.longValue() - recetaArticuloDTO.getArticuloRelacion().getUnidadManejoPrecioCaja().longValue() * cociente;
			if(residuo>0){
				totalDetalle = cociente * precioCaja 	+ residuo * precioUnitario;
				//totalDetalleIVA = cociente * precioCajaIVA 	+ residuo * precioUnitarioIVA;
				if(recetaArticuloDTO.getArticuloRelacion().getAplicaImpuestoVenta()){
					totalDetalleIVA= SICArticuloCalculo.getInstancia().calcularValorConImpuestos(totalDetalle, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
				}else{
					totalDetalleIVA = cociente * precioCajaIVA 	+ residuo * precioUnitarioIVA;
				}
			}else{
				totalDetalle = cociente * precioCaja;
				//totalDetalleIVA = cociente * precioCajaIVA;
				if(recetaArticuloDTO.getArticuloRelacion().getAplicaImpuestoVenta()){
					totalDetalleIVA= SICArticuloCalculo.getInstancia().calcularValorConImpuestos(totalDetalle, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
				}else{
					totalDetalleIVA = cociente * precioCajaIVA;
				}
				
			}
			//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
			//se asigna la cantidad
			recetaArticuloDTO.setCantidadPrevioEstadoDescuento(Long.valueOf(residuo));
			//se asigna el precio del detalle
			//se debe tomar el valor del residuo y multiplicarlo por el valor unitario sin IVA
			double totalDetalleUnitario = residuo * precioUnitario;
			recetaArticuloDTO.setValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalleUnitario), NUMERO_DECIMALES));
			
			//se calcula el valor total aplicar descuento MARCA PROPIA
			Double npValorPrevioEstadoDcto = (estadoDetallePedidoDTO.getCantidadEstado().longValue() * recetaArticuloDTO.getCantidad().longValue()) 
					* recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
			recetaArticuloDTO.setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDcto,NUMERO_DECIMALES));
		}else{
			totalDetalle = cantidadTotalItem.longValue() * precioUnitario;
			//SE ACTUALIZA ESTE CALCULO PORQUE SE DEBE DE CUADRAR EL CALCULO DEL PRECIO DEL CANASTO CON EL PRECIO DE ITEMS INDIVIDUALES 
			//totalDetalleIVA = cantidadTotalItem.longValue() * precioUnitarioIVA;
			if(recetaArticuloDTO.getArticuloRelacion().getAplicaImpuestoVenta()){
				totalDetalleIVA= SICArticuloCalculo.getInstancia().calcularValorConImpuestos(totalDetalle, crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
			}else{
				totalDetalleIVA = cantidadTotalItem.longValue() * precioUnitarioIVA;
			}
			
			//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
			//se asigna la cantidad y el precio total del detalle sin IVA
			recetaArticuloDTO.setCantidadPrevioEstadoDescuento(cantidadTotalItem);
			recetaArticuloDTO.setValorPrevioEstadoDescuento(Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES));
			recetaArticuloDTO.setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(totalDetalle,NUMERO_DECIMALES));
		}

		//si el precio de mayorista se activa no se deben dar descuentos
		if(esActivadoPrecioMayoristaArticulo){
			recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0l);
			recetaArticuloDTO.setValorPrevioEstadoDescuento(0d);

			//se calcula el valor total aplicar descuento MARCA PROPIA
			Double npValorPrevioEstadoDescuento = cantidadTotalItem.longValue() * precioUnitario;
			recetaArticuloDTO.setNpValorPrevioEstadoDescuento(Util.roundDoubleMath(npValorPrevioEstadoDescuento,NUMERO_DECIMALES));
		}
		
		totalDetalle = Util.roundDoubleMath(Double.valueOf(totalDetalle),NUMERO_DECIMALES);
		totalDetalleIVA = Util.roundDoubleMath(Double.valueOf(totalDetalleIVA),NUMERO_DECIMALES);
		recetaArticuloDTO.setValorTotalEstado(totalDetalle);
		recetaArticuloDTO.setValorTotalEstadoIVA(totalDetalleIVA);
		
		LogSISPE.getLog().info("total detalle canasta: {}",totalDetalle);
		LogSISPE.getLog().info("total detalle canasta iva: {}",totalDetalleIVA);

		LogSISPE.getLog().info("npHabilitarPrecioCaja: {}",recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja());
		
		//si no se deben aplicar descuentos se actualizan los totales netos
		if(request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS) == null){
			recetaArticuloDTO.setValorTotalEstadoNeto(totalDetalle);
			recetaArticuloDTO.setValorTotalEstadoNetoIVA(totalDetalleIVA);
		}
	}		

		
		/**
		 * 
		 * @param request
		 * @param infos
		 * @param errors
		 * @param warnings
		 * @param formulario
		 * @param estadoActivo
		 * @param estadoInactivo
		 * @throws Exception
		 */
		public static void actualizarDetalleAction(HttpServletRequest request, ActionMessages infos, ActionMessages errors, ActionMessages warnings, CotizarReservarForm formulario, String estadoActivo, String estadoInactivo,Boolean actualizarSIC) throws Exception {
			LogSISPE.getLog().info("Se accede a la opci\u00F3n de actualizarDetalle en el action");
			
			HttpSession session = request.getSession();
			String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			StringBuffer articulosObsoletos = null;
			
//			UtilPopUp popUp = null;
			//primero se verifica si la acci\u00F3n es una reservaci\u00F3n y el tab activo son las entregas 
			if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
				//se realizan las asignaciones correspondientes para observar la secci\u00F3n del detalle del pedido
				session.setAttribute(CotizarReservarAction.SUB_PAGINA, "cotizarRecotizarReservar/detallePedido.jsp");
			}
			//se obtiene de la sesion los datos del detalle y de la lista de articulos.
			List<DetallePedidoDTO> detallePedido = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			//solo si el detalle del pedido no est\u00E1 vacio
//			Boolean stockMayorAlCD=Boolean.FALSE;
			if(detallePedido!=null && !detallePedido.isEmpty()){
				try{
					Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
					Collection<DescuentoEstadoPedidoDTO> descuentosVariablesCol = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
					//comento estas lineas porque cuando ingreso por consolidacion y selecciono un descuento variable y un normal, se aplica solo el variable
//					if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null 
//							&& (request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))
//							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
//							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))
//							|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))) ){
//						generarOpDescuentos(request, descuentos);
//					}
					
					if(CollectionUtils.isEmpty(descuentosVariablesCol) && session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS) == null){
						descuentosVariablesCol = new ArrayList<DescuentoEstadoPedidoDTO>();
						//se buscan los descuentos variables
						if(CollectionUtils.isNotEmpty(descuentos)){
							String codigoTipoDescuentoVariable = "";
							ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
							if(parametroDTO.getValorParametro()!=null){
								codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
							}
							//se verifica si es un descuento de tipo variable
							for(DescuentoEstadoPedidoDTO descuentoAct : (Collection<DescuentoEstadoPedidoDTO>) descuentos){
								if(descuentoAct.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoVariable)){
									descuentosVariablesCol.add(descuentoAct);
								}
							}
						}

						//se valida que suba a sesion solo los descuentos variables seleccionados
						Collection<DescuentoEstadoPedidoDTO> descuentosActuales = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
						Collection<DescuentoEstadoPedidoDTO> descuentosVariablesActuales = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
						if(CollectionUtils.isEmpty(descuentosVariablesActuales)){
							descuentosVariablesActuales = new ArrayList<DescuentoEstadoPedidoDTO>();
						}
						
						if(CollectionUtils.isNotEmpty(descuentosActuales) && CollectionUtils.isNotEmpty(descuentosVariablesCol)){
							//se verifica que el descuento variable este aplicado actualmente // 273-CTD2-CMDCAN,  274-CTD17-CMDMON   //212-CTD3-CMDMON-COM01
							for(DescuentoEstadoPedidoDTO dsctoActual : descuentosActuales){
								for(DescuentoEstadoPedidoDTO dsctoVariable : descuentosVariablesCol){
									String llaveDsctoActual = dsctoActual.getLlaveDescuento();
									if(llaveDsctoActual != null && !llaveDsctoActual.equals("")){
										String llaveDsctoVariable = dsctoVariable.getLlaveDescuento();
										if(llaveDsctoVariable != null && !llaveDsctoVariable.equals("")){
											llaveDsctoActual =  llaveDsctoActual.replace(llaveDsctoActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
											llaveDsctoVariable =  llaveDsctoVariable.replace(llaveDsctoVariable.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
											if(llaveDsctoVariable.contains(llaveDsctoActual) || llaveDsctoActual.contains(llaveDsctoVariable)){
												descuentosVariablesActuales.add(dsctoVariable);
												break;
											}
											
										}
									}
								}
							}
							//sube a sesion los descuentos variables
							request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES,descuentosVariablesActuales);
						}
						else if(CollectionUtils.isNotEmpty(descuentosVariablesCol)){
						//sube a sesion los descuentos variables
							request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES,descuentosVariablesCol);
						}
						
					}
					ConsolidarAction.eliminarDescuentosConsolidados((ArrayList<DetallePedidoDTO>) detallePedido);
					
					//para el caso de pedidos consolidados con descuentos variables
					//si existen descuentos variables, hay que validar que tambien exista cola de autorizaciones
					if(request.getSession().getAttribute("existe.descuentos.variables.normales") != null && request.getSession().getAttribute("existe.descuentos.variables.normales").equals(Boolean.TRUE)){
						if(CollectionUtils.isEmpty((ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES))){
							AutorizacionesUtil.verificarAutorizacionesVariables(detallePedido, request, formulario);
						}
					}
					
					formulario.actualizarDescuentos(request, warnings);					

				}catch(Exception ex){
					String pedidoGeneral= (String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral");
					if(pedidoGeneral==null){
						errors.add("errorDescuentos",new ActionMessage("errors.llamadaServicio.general"));
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					}
					else{
						throw new Exception("Error al actualizar los descuentos: "+ex.getMessage());
					}
				}

				//verificaci\u00F3n para la actualizaci\u00F3n del stock y alcance
				if(actualizarSIC){
					Collection<ArticuloDTO> articulos = new ArrayList<ArticuloDTO>();
					if(formulario.getCheckActualizarStockAlcance()!=null && formulario.getCheckActualizarStockAlcance().equals(estadoActivo)){
						try{
							articulosObsoletos = obtenerStockSICArticulosRecetas(request, estadoActivo, estadoInactivo, accion, articulosObsoletos, detallePedido, articulos);

							//quitar luego estas 2 lineas de codigo
							//articulosObsoletos = new StringBuffer();
							//articulosObsoletos.append("CODIGOARTICULO NO.");
							
							//verifica si encontr\u00F3 alg\u00FAn art\u00EDculo obsoleto para mostrar el popup de advertencia
							if(articulosObsoletos != null){	
								
								//session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS) != null && !((String)session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS)).equals("ok")
								if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS)!=null){
									warnings.add("preciosAlterados", new ActionMessage("message.error.articulosObsoletos", articulosObsoletos.toString()));
								}else {							
									/*popUp = new UtilPopUp();
									popUp.setTituloVentana("Advertencia");
									popUp.setFormaBotones(UtilPopUp.OK);
									popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
									popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
									popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
									popUp.setMensajeVentana("Los art&iacute;culos con c&oacute;digo barras ".concat(articulosObsoletos.toString()).concat(" est&aacute;n considerados como OBSOLETOS en el SIC y pueden tener problemas de stock."));
									request.setAttribute(SessionManagerSISPE.POPUP, popUp);
									popUp = null;*/
									
									if(request.getParameter("confirmarStockObsoleto")==null && request.getParameter("cancelarStockObsoleto")==null
											&&	!accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
										 CotizarReservarAction.instanciarVentanaArticuloObsoleto(request,"");
									}
								}
							}
						}catch(SISPEException ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));
						}
					}
				}
				//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
				ActionMessage message = CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
				if(message != null){
					warnings.add("errorCalculo", message);
				}
				session.setAttribute(MODIFICAR_PESO_INACTIVO, null);
				
			}

			
			//se actualiza el detalle de la cotizaci\u00F3n que est\u00E1 en sesi\u00F3n
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedido);
			session.removeAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR);
			session.removeAttribute(CotizarReservarAction.COL_INDICES_CANTIDADES_MODIFICADAS);
			
			if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
				//se guarda un info en caso de que se haya eliminado una entrega
				//porque la cantidad ingresada en el detalle fue menor
				if(request.getAttribute(CotizarReservarForm.INFO_ENTREGA_ELIMINADA) != null && vistaPedidoDTO!=null){
						//&& !vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL)){
					infos.add("cantidadModificada",new ActionMessage("message.detallePedido.cantidadModificada"));
				}
			}
			//if(stockMayorAlCD){
			
			if(vistaPedidoDTO!= null && (String)session.getAttribute(CotizarReservarForm.INFO_CANTIDAD_MAYOR) != null && vistaPedidoDTO.getCodigoEstadoPagado()!=null){
					//&& !vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL)){
				warnings.add("Cantidad Mayor al CD", new ActionMessage("message.detallePedido.cantidadMayorModificada"));
			}
			//se actualizan los datos del formulario
			formulario.setChecksSeleccionar(null);
			formulario.setCheckSeleccionarTodo(null);
			//formulario.setCheckActualizarStockAlcance(null);

			//las siguientes variables de sesi\u00F3n son utilizadas cuando se utiliza la b\u00FAsqueda de art\u00EDculos
			if(session.getAttribute(CotizarReservarForm.PEDIDOS_ANTERIORES) == null){
				session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
			}
			session.removeAttribute(CatalogoArticulosAction.DIVISION_CATALOGO);
			session.removeAttribute(CatalogoArticulosAction.CATALOGO_ACTUAL);
			session.removeAttribute(CatalogoArticulosAction.CATALOGO_ANTERIOR);
			//se elimina la variable que indica el cambio manual de los precios
			session.removeAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS);
			
			session.removeAttribute(CotizarReservarAction.DEBE_ACTUALIZAR);
//			String preciosAc = request.getParameter("preciosActuales");			
		
		}

		/**
		 * @param request
		 * @param estadoActivo
		 * @param estadoInactivo
		 * @param accion
		 * @param articulosObsoletos
		 * @param detallePedido
		 * @param articulos
		 * @return
		 * @throws SISPEException
		 * @throws Exception
		 */
		public static StringBuffer obtenerStockSICArticulosRecetas( HttpServletRequest request, String estadoActivo, String estadoInactivo, String accion, 
				StringBuffer artObs, List detallePedido, Collection<ArticuloDTO> articulos) throws SISPEException, Exception {
			
			Boolean stockMayorAlCD;
			StringBuffer articulosObsoletos = artObs;
			for(Iterator<DetallePedidoDTO> it = detallePedido.iterator(); it.hasNext();){
				DetallePedidoDTO detallePedidoDTO = it.next();
				ArticuloDTO articuloDTO = detallePedidoDTO.getArticuloDTO();
				WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, estadoInactivo, estadoInactivo, accion);
				articulos.add(articuloDTO);
			}
			
			LogSISPE.getLog().info("actualizando datos desde el SIC");
			
			//se gestiona el alcance desde el MAX
			gestionarAlcanceMax(articulos, request);
			
			//llamada al m\u00E9todo que obtiene el stock y alcance de los art\u00EDculos
			SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);

			//se itera la colecci\u00F3n del detalle principal
			for(int i=0;i<articulos.size();i++){
				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)detallePedido.get(i);
				if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
					LogSISPE.getLog().info("stock articulo CD{}",detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue());
					LogSISPE.getLog().info("stock articulo {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue());
					detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);					
					
					//stock obsoleto
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs() != null){
						if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs()!=0 && detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
							detallePedidoDTO.getArticuloDTO().setNpStockArticulo(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());				
						}
					}
					
					//se obtienen las entregas del art\u00EDculo
					ArrayList entregas = (ArrayList)detallePedidoDTO.getEntregas();
					LogSISPE.getLog().info("existeEntregas {}",entregas);
					if(entregas!=null && entregas.size()>0){
						stockMayorAlCD=Boolean.TRUE;
					}
				}else{
					detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoActivo);
				}
				//verifica si el art\u00EDculo es Obsoleto y si la cantidad solicitada es mayor al stock en el CD
				if(detallePedidoDTO.getArticuloDTO().getClaseArticulo() != null && detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) &&
						detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()){
					//a\u00F1ade los codigos de los art\u00EDculos encontrados 
					if(articulosObsoletos == null){
						articulosObsoletos = new StringBuffer();
					}
					articulosObsoletos.append(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()).append(", ");
					
				}
				
//				if(detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) 
//						|| detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
						detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
					//se verifica el stock y alcance de cada \u00EDtem del canasto
					CotizacionReservacionUtil.verificarDatosSICReceta(request, detallePedidoDTO, true);
				}
			}
			return articulosObsoletos;
		}
		
		
		/**
		 * Calcular la cantidad de bonos navidad empresarial
		 * @param colDescuentoEstadoPedidoDTO		coleccion de los descuento por estado pedido
		 * @param request
		 * @throws Exception
		 * @author osaransig
		 */
		public static void verificaCantidadBonosMaxiNavidad(Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO, HttpServletRequest request,Collection<DetallePedidoDTO> detallePedidoConsolidado)throws Exception{
			
			//inicializo las variables a utilizar en la impresion del resumen del pedido (BONO-MAXI-NAVIDAD)
			request.getSession().removeAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
			request.getSession().removeAttribute(CotizarReservarAction.MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI);
			request.getSession().removeAttribute(CotizarReservarAction.VALOR_BONO_COMPROBANTE_MAXI);
			request.getSession().removeAttribute(CotizarReservarAction.MONTO_CALCULADO_COMPROBANTE_MAXI);
			
			Collection<DetallePedidoDTO> detallePedidoCol =null;
			if(CollectionUtils.isEmpty(detallePedidoConsolidado)){
				//obtiene detalle del pedido para calcular el monto sobre el cual se va a calcular el num de abonos
				detallePedidoCol = (List<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);	
			}else{
				detallePedidoCol =detallePedidoConsolidado;
			}
			
			Double valorTotalPedido = 0D;
			ParametroDTO parametroDTO = null;
//			Collection<DescuentoEstadoPedidoDTO> colDesEstadoPedido = (Collection<DescuentoEstadoPedidoDTO>) SerializationUtils.clone((Serializable) colDescuentoEstadoPedidoDTO);
//			Collection<DescuentoEstadoPedidoDTO> colDesEstadoPedido = colDescuentoEstadoPedidoDTO;
			Collection<DescuentoEstadoPedidoDTO> colDesEstadoPedido = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
			
			if (detallePedidoCol == null) {
				detallePedidoCol = (List<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO_VALIDAR_NUMBONOS);
			}
			
//			ParametroDTO parametroArtNoMaxiNavidad = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.articulos.excluidos.navidad.empresarial", request);
			
//			String[] codigosClaArtNoNavidadEmpresarial = parametroArtNoMaxiNavidad == null ? null : parametroArtNoMaxiNavidad
//							.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
			
			
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
			String codigoTipoDescuentoPagEfe = (String) request.getSession().getAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO);
			
			if(codigoTipoDescuentoPagEfe == null){
				codigoTipoDescuentoPagEfe = CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
			}
			
			
			String existNavEmpCredito = validarExisteDesNavEmpCredito(request);
			
			if (existNavEmpCredito != null) {
				
				DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = obtenerDescuentoNavidadEmpresarial(request, existNavEmpCredito);
		
				if (CollectionUtils.isEmpty(colDesEstadoPedido)){
					colDesEstadoPedido = new ArrayList<DescuentoEstadoPedidoDTO>();
					colDesEstadoPedido.add(descuentoEstadoPedidoDTO);
				} else {
					//validar si ya se encuentra agregado el descuento navidad empresarial credito
					DescuentoEstadoPedidoDTO descuentoEstPedFind = (DescuentoEstadoPedidoDTO) CollectionUtils.find(colDesEstadoPedido, new Predicate() {
						
						public boolean evaluate(Object arg0) {
							
							DescuentoEstadoPedidoDTO desEstPed = (DescuentoEstadoPedidoDTO) arg0;
							
							return desEstPed.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().
									equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
						}
					});
					
					if (descuentoEstPedFind == null) {
						colDesEstadoPedido.add(descuentoEstadoPedidoDTO);
					}
				}
				
				
				valorTotalPedido = calcularMontoBonos(detallePedidoCol, request);
				
				
			} else { 
				
				if (colDesEstadoPedido != null && !colDesEstadoPedido.isEmpty()) {
					final String codigoTipDesPagEfeAux = codigoTipoDescuentoPagEfe;
					boolean existTipDesPagEfc = CollectionUtils.exists(colDesEstadoPedido, new Predicate() {
						
						public boolean evaluate(Object arg0) {
							
							DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = (DescuentoEstadoPedidoDTO) arg0;
							
							return descuentoEstadoPedidoDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipDesPagEfeAux);
						}
					});
					
					//cuando se selecciona descuento pago en efectivo
					if (existTipDesPagEfc) {
						
						valorTotalPedido = calcularMontoBonos(detallePedidoCol, request);
					
					}
				}
			}
			
			
			if(colDesEstadoPedido != null && !colDesEstadoPedido.isEmpty()){
				
				request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS, colDesEstadoPedido);
				//se obtiene la suma de los decuentos
				for(Iterator <DescuentoEstadoPedidoDTO> iterator = colDesEstadoPedido.iterator();iterator.hasNext();){
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = iterator.next();
					//Si el descuento es maxi-navidad
					if(descuentoEstadoPedidoDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe) ||
							descuentoEstadoPedidoDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"))) {
						
						if(descuentoEstadoPedidoDTO.getValorPrevioDescuento() != null &&
								descuentoEstadoPedidoDTO.getValorPrevioDescuento().doubleValue() > 0){
							
							LogSISPE.getLog().info("--Valor Total Pedido-->{}",request.getSession().getAttribute(CotizarReservarAction.TOTAL_PEDIDO));
							Double valorMinimoCompra = 0D;
							Double valorBonoMaxi = 0D;
							Integer numeroBonos = 0;
//							Double valorTotalPedido=(Double)request.getSession().getAttribute(CotizarReservarAction.TOTAL_PEDIDO);
//							if(valorTotalPedido==null){
//								VistaPedidoDTO vistaPedidoDTO1 = (VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
//								if(vistaPedidoDTO1!=null){
//									valorTotalPedido=vistaPedidoDTO1.getTotalPedido();
//								}
//							}
							
							
							//Obtengo el valor minimo de compra para el calculo bono maxi-navidad desde la tabla parametros
							parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.bono.monto.minimoCompra", request);
							if(parametroDTO.getValorParametro() != null){
								valorMinimoCompra = Double.parseDouble(parametroDTO.getValorParametro());
							}
							//Obtengo el valor minimo de compra para el calculo bono maxi-navidad desde la tabla parametros
							parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.valor.bono.maxinavidad", request);
							if(parametroDTO.getValorParametro() != null){
								valorBonoMaxi = Double.parseDouble(parametroDTO.getValorParametro());
							}
							//Si el valorMinimoCompra que se obtubo del parametro es mayor a cero y solo si selecciono el check de pago en efectivo
							if( valorMinimoCompra > 0 && (valorTotalPedido!=null && valorTotalPedido.intValue()>0)){
								numeroBonos = (int)(valorTotalPedido.doubleValue()/valorMinimoCompra);
								if(numeroBonos > 0){
									request.getSession().setAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI, numeroBonos);
									request.getSession().setAttribute(CotizarReservarAction.MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI, valorMinimoCompra);
									request.getSession().setAttribute(CotizarReservarAction.VALOR_BONO_COMPROBANTE_MAXI, valorBonoMaxi);
									request.getSession().setAttribute(CotizarReservarAction.MONTO_CALCULADO_COMPROBANTE_MAXI, Util.roundDoubleMath(valorTotalPedido,NUMERO_DECIMALES));
								}
							}
						}
					}
				}
			}
			
		}
		
		
		
		/**
		 * Validar si se selecciono el descuento navidad empresarial credito
		 * @return
		 */
		public static String validarExisteDesNavEmpCredito(HttpServletRequest request) {
			
			final String credito = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento") +
					 MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito");
			request.getSession().removeAttribute(DESC_NAVEMP_CREDITO_SELECCIONADO);
			
			Object descuentos = request.getSession().getAttribute(DESCUENTOS_SELECCIONADOS);
			String [] descuentosSel = null;
			
			if (descuentos != null) {

				descuentosSel = ((String[]) descuentos).clone();
				
				for (String string : descuentosSel) {
					if (!StringUtils.isEmpty(string)) { 
						String [] array = string.split("-");
						if (array[1].equals(credito)) {
							request.getSession().setAttribute(DESC_NAVEMP_CREDITO_SELECCIONADO, "OK");
							return string;
						}
					}
				}
				

			}
			return null;
		}
		
		
		private static Date validarFechaFiltro(String fechaFiltro){
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fecha = null;
			try {
				if(fechaFiltro!=null && !fechaFiltro.equals("")){
				fecha = formatoDelTexto.parse(fechaFiltro);
				}
			} catch (ParseException e) {
				LogSISPE.getLog().info("Error en el formato de la fecha yyyy-MM-dd fecha:{} exception {}",fecha,e);

			}
			return fecha;
		}
		
		/**
		 * Calcular el total de monto del pedido del cual se obtendra el numero de bonos
		 * @param detallePedidoCol
		 * @param request
		 * @return
		 * @throws Exception
		 * @author osaransig
		 */
		public static Double calcularMontoBonos (Collection<DetallePedidoDTO> detallePedidoCol, HttpServletRequest request) throws Exception {
			
			Double valorTotalPedido = 0D;
			final ParametroDTO parametroCodigoDesCaja = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja", request);
			
			final ParametroDTO parametroLicores = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentos.prohibidoEntregarClienteDomingos", request);
			String fechaAProcesar="2014-11-17 23:59:59";
			VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			Calendar fI = Calendar.getInstance();
			fI.setTime(new Date(validarFechaFiltro(fechaAProcesar).getTime()));
			//calculo del total para obtener el numero de abonos
			if (!CollectionUtils.isEmpty(detallePedidoCol)) {
				for (DetallePedidoDTO detallePedidoDTO : detallePedidoCol) {
	
					EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
					
					String codigoClasificacion = detallePedidoDTO.getArticuloDTO().getCodigoClasificacion();
					
					if (esCanasto(codigoClasificacion, request)) {
						
						//canasto de cotizaciones
						if (estadoDetallePedidoDTO.getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)) {
							
							//si valor total estadodecuento es diferente a valorFinalEstadoDescuento se supone q tiene 2 descuentos
							//de cajas - mayorista, de cajas - general
							
							Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
							
							boolean existdesCajas = CollectionUtils.exists(descuentos, new Predicate() {
								
								public boolean evaluate(Object arg0) {
									DescuentoEstadoPedidoDTO descuento = (DescuentoEstadoPedidoDTO) arg0;
									
									return descuento.getDescuentoDTO().getCodigoTipoDescuento().equals(parametroCodigoDesCaja.getValorParametro());
								}
							});
							
							final ParametroDTO parametroCodigoMayorista = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista", request);
							boolean existDsctoMayorista = CollectionUtils.exists(descuentos, new Predicate() {
								
								public boolean evaluate(Object arg0) {
									DescuentoEstadoPedidoDTO descuento = (DescuentoEstadoPedidoDTO) arg0;
									
									return descuento.getDescuentoDTO().getCodigoTipoDescuento().equals(parametroCodigoMayorista.getValorParametro());
								}
							});
							
							if (existdesCajas || existDsctoMayorista) {
								Collection<ArticuloRelacionDTO> articuloRelacionCol = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
								
								for (ArticuloRelacionDTO articuloRelacionDTO : articuloRelacionCol) {
									//se excluye licores xq ya tienen descuento de caja
									String codClasArtRel = articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion().substring(0, 2);
									if (!parametroLicores.getValorParametro().contains(codClasArtRel)) {
										//se excluyen articulos con descuento de cajas
										//obtiene la fecha actual
										
										Timestamp fechaActual =  new Timestamp(fI.getTime().getTime());
										//LogSISPE.getLog().info("factual: {} , fie: {}", fechaActual, vistaPedidoDTO.getFechaPedido());
										
										if (vistaPedidoDTO!=null && vistaPedidoDTO.getFechaPedido().getTime() <= fechaActual.getTime()) {
											valorTotalPedido += articuloRelacionDTO.getValorTotalEstadoNeto();
										}else{
											
											//se excluyen todos los detalles con descuento de cajas o mayorista
											if ((articuloRelacionDTO.getNpAplicaPrecioCaja() !=null && articuloRelacionDTO.getNpAplicaPrecioCaja().equals(ConstantesGenerales.ESTADO_INACTIVO)) &&
													(articuloRelacionDTO.getNpAplicaPrecioMayorista() !=null && articuloRelacionDTO.getNpAplicaPrecioMayorista().equals(ConstantesGenerales.ESTADO_INACTIVO))) {
												valorTotalPedido += articuloRelacionDTO.getValorTotalEstadoNetoIVA();
											}else{
												if(articuloRelacionDTO.getArticuloRelacion().getHabilitadoPrecioCaja() != null && articuloRelacionDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo() != null
														&& !articuloRelacionDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && !articuloRelacionDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
													valorTotalPedido += articuloRelacionDTO.getValorTotalEstadoNetoIVA();
												}
											}
										}
									} else {
										
										Long cantidad = articuloRelacionDTO.getCantidadPrevioEstadoDescuento();
										LogSISPE.getLog().info("Unidad de manejo: {}", articuloRelacionDTO.getArticuloRelacion().getUnidadManejoVenta());
										LogSISPE.getLog().info("Precio Base: {}", articuloRelacionDTO.getArticuloRelacion().getPrecioBase());
										valorTotalPedido += cantidad * articuloRelacionDTO.getArticuloRelacion().getPrecioBaseImp();
										
									}
								}
								
							} else { //cuando no existe descuento de cajas se toma todo el valor del canasto de cotizaciones para calcular el numero de bonos
								
								int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
								double valorTotalDescuento=Util.roundDoubleMath(estadoDetallePedidoDTO.getValorTotalEstadoDescuento(),numeroDecimales).doubleValue();
								double valorFinalEstadoDescuento=Util.roundDoubleMath(estadoDetallePedidoDTO.getValorFinalEstadoDescuento(),numeroDecimales).doubleValue();
								
								if (estadoDetallePedidoDTO.getValorTotalEstadoDescuento() == null || valorTotalDescuento==valorFinalEstadoDescuento) {
									valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
								}
							}
						} else { //canasto de catalogo
							
							valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
						}
						
						
					} else if (esPavo(codigoClasificacion, request)) {
						if (!validarExisteDescPavos(request,codigoClasificacion)) {
							valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
						}
						
					} else if (parametroLicores.getValorParametro().contains(codigoClasificacion.substring(0, 2))) {
						
						//si el valorTotalEstadoDescuento es null o cero indica que este articulo no tiene descuento 
						//y por lo tanto se va a generar bonos
						if (estadoDetallePedidoDTO.getValorFinalEstadoDescuento() == null
								|| estadoDetallePedidoDTO.getValorTotalEstadoDescuento().equals(estadoDetallePedidoDTO.getValorFinalEstadoDescuento())) {
		
							valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
						} else {
							LogSISPE.getLog().info("Unidad de manejo: {}", detallePedidoDTO.getArticuloDTO().getUnidadManejoVenta());
							LogSISPE.getLog().info("Precio Base: {}", detallePedidoDTO.getArticuloDTO().getPrecioBase());
							valorTotalPedido += (estadoDetallePedidoDTO.getCantidadEstado() % detallePedidoDTO.getArticuloDTO().getUnidadManejoVenta()) * detallePedidoDTO.getArticuloDTO().getPrecioBase();
						}
						
					} else {
					
						//si el valorTotalEstadoDescuento es null o cero indica que este articulo no tiene descuento 
						//y por lo tanto se va a generar bonos
						
						Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
						
						boolean existDesCajas = CollectionUtils.exists(descuentos, new Predicate() {
							
							public boolean evaluate(Object arg0) {
								DescuentoEstadoPedidoDTO descuento = (DescuentoEstadoPedidoDTO) arg0;
								
								return descuento.getDescuentoDTO().getCodigoTipoDescuento().equals(parametroCodigoDesCaja.getValorParametro());
							}
						});
						
						if (existDesCajas) {
							if (detallePedidoDTO.getArticuloDTO().getTieneUnidadManejoCol()) {
								valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
							}
							
						} else {
							
							if (estadoDetallePedidoDTO.getValorFinalEstadoDescuento() == null
									|| estadoDetallePedidoDTO.getValorTotalEstadoDescuento().doubleValue() == estadoDetallePedidoDTO.getValorFinalEstadoDescuento().doubleValue()) {
								valorTotalPedido += estadoDetallePedidoDTO.getValorTotalVenta();
							}
						}
					}
				}
			}
			
			return valorTotalPedido;
		}
		
		
		
		/**
		 * Validar se encuentra seleccionado el descuento de pavos
		 * @param request
		 * @return
		 * @throws Exception
		 * @author osaransig
		 */
		public static boolean validarExisteDescPavos (HttpServletRequest request, String codigoClasificacion) throws Exception {
			
			ParametroDTO parametroDesPavos = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosDescuentosPavos", request);
			
			Object descuentos = request.getSession().getAttribute(DESCUENTOS_SELECCIONADOS);
			String [] descuentosSel = null;
			
			int i = 0;
			
			String [] tipDesPavos = parametroDesPavos.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
			
			//OBTENER DESCUENTO CLASIFICACIONES ASIGNADAS AL TIPO DE DESCUENTO
			TipoDescuentoClasificacionDTO tipClasificacionDTO= new TipoDescuentoClasificacionDTO();
			tipClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			tipClasificacionDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
			tipClasificacionDTO.setCriteriaSearch(new CriteriaSearch());
			tipClasificacionDTO.getId().setCodigoClasificacion(codigoClasificacion);
			tipClasificacionDTO.getCriteriaSearch().addCriteriaSearchParameter(new CriteriaSearchParameter<String[]>("id.codigoTipoDescuento",ComparatorTypeEnum.IN_COMPARATOR,tipDesPavos));
			Collection<TipoDescuentoClasificacionDTO>tipDesClaCol= SISPEFactory.getDataService().findObjects(tipClasificacionDTO);
			
			if (descuentos != null && CollectionUtils.isNotEmpty(tipDesClaCol)) {
				LogSISPE.getLog().info("Nuevo de tipos de descuentos encontrados: {}",tipDesClaCol.size());
				descuentosSel = ((String[]) descuentos).clone();
				descuentosSel = (String[]) CollectionUtils.collect(Arrays.asList(descuentosSel), new Transformer() {
					
					public Object transform(Object arg0) {
						String string = (String) arg0;
						return string.split("-")[1];
					}
				}).toArray(new String [0]);
				
				Arrays.sort(descuentosSel);
				
				for (TipoDescuentoClasificacionDTO tipDesPavDTO : tipDesClaCol) {
					
					String claveDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento") + tipDesPavDTO.getId().getCodigoTipoDescuento();
					
					i = Arrays.binarySearch(descuentosSel, claveDes, new Comparator<String>() {

						public int compare(String o1, String o2) {
//							String codigoTipoDes = o1.split("-")[1];
							LogSISPE.getLog().info("codigo tip Desc: {}",o1);
							return o1.compareTo(o2);
						}

					});
					
					if (i >= 0) { // descuento de pavos seleccionado
						return true;
					}
				}
				
			}
			
			return false;
		}
		
		public static DescuentoEstadoPedidoDTO obtenerDescuentoNavidadEmpresarial (HttpServletRequest request, String llaveDescuento) {
			TipoDescuentoDTO tipoDescuentoDTO = new TipoDescuentoDTO();
			tipoDescuentoDTO.getId().setCodigoTipoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
			
			DescuentoDTO descuentoDTO = new DescuentoDTO();
			descuentoDTO.setTipoDescuentoDTO(tipoDescuentoDTO);
			
			descuentoDTO = SISPEFactory.getDataService().findUnique(descuentoDTO);
			
			DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
			descuentoEstadoPedidoDTO.setDescuentoDTO(descuentoDTO);
			descuentoEstadoPedidoDTO.setValorPrevioDescuento(1D);
			descuentoEstadoPedidoDTO.setValorDescuento(0D);
			descuentoEstadoPedidoDTO.setValorMotivoDescuento(0D);
			descuentoEstadoPedidoDTO.setPorcentajeDescuento(0D);
			descuentoEstadoPedidoDTO.setRangoFinalDescuento(0D);
			descuentoEstadoPedidoDTO.setRangoInicialDescuento(0D);
			descuentoEstadoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoDTO.getId().getCodigoDescuento());
			descuentoEstadoPedidoDTO.setLlaveDescuento(llaveDescuento);
			
			return descuentoEstadoPedidoDTO;
		}
		
		/**
		 * 
		 * @param detallePedidoDTOs
		 * @return
		 */
		public static Collection<String> obtenerReservarBodegaSIC(List<DetallePedidoDTO> detallePedidoDTOs){
			List<EstadoDetallePedidoDTO> detallePedidoDTOsAux = new ArrayList<EstadoDetallePedidoDTO>();
			
			for(DetallePedidoDTO dto:  detallePedidoDTOs ){
				detallePedidoDTOsAux.add(dto.getEstadoDetallePedidoDTO());
			}
						
			Collection<String> inputString = CollectionUtils.collect(detallePedidoDTOsAux, new Transformer() {
			      public Object transform(Object o) {			    	
			          return (ClasesUtil.invocarMetodoGet(o, "reservarBodegaSIC")).toString();
			      }
			  });			
			
			return inputString;
		}
		
		/**
		 * Inicia el parametro para aplicar los descuentos automaticos de pavos
		 * @param request
		 * @param formulario
		 */
		public static String[] iniciarDescuentoPavos(HttpServletRequest request, ArticuloDTO articuloDTO) throws Exception{
			LogSISPE.getLog().info("entra a validar si debe o no aplicar descuentos de pavos automaticos");
			//Agregar las nuevas llaves de descuentos automaticos.
			Collection<TipoDescuentoDTO> tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			Collection<String> llavesProcesada=(Collection<String>)request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
			String[] llaveDescto= (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
			//VALIDAR SI EL ARTICULO INGRESADO SON PAVOS O PAVOS MARCA PROPIA, SOLO EN ESTE CASO APLICAR AUTOMATICAMENTE
			ParametroDTO parametroDescuentoPavos = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosDescuentosPavos", request);
			
			Boolean aplicaDescuentoAutomatico=Boolean.FALSE;
			Collection<TipoDescuentoClasificacionDTO> tipDesClaCol=null;
			if(parametroDescuentoPavos.getValorParametro()!=null){
				String parametrosTipoDescuentoPavos = parametroDescuentoPavos.getValorParametro();
				parametrosTipoDescuentoPavos=parametrosTipoDescuentoPavos.replace(",", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"));
				String []valorParametro= parametrosTipoDescuentoPavos.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"));
				
				//Obtener las clasificaciones para los tipos de descuentos obtenidos desde la tabla de parametros
				TipoDescuentoClasificacionDTO tipClasificacionDTO= new TipoDescuentoClasificacionDTO();
				tipClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				tipClasificacionDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
				tipClasificacionDTO.setCriteriaSearch(new CriteriaSearch());
				tipClasificacionDTO.getCriteriaSearch().addCriteriaSearchParameter(new CriteriaSearchParameter<String[]>("id.codigoTipoDescuento",ComparatorTypeEnum.IN_COMPARATOR,valorParametro));
				tipDesClaCol= SISPEFactory.getDataService().findObjects(tipClasificacionDTO);
				//cambio bgudino
				eliminarClasificacionDctosConAutorizacion(request, tipDesClaCol);
				if(!CollectionUtils.isEmpty(tipDesClaCol)){
					//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
					if(llavesProcesada==null){
						llavesProcesada= new ArrayList<String>();
					}
					int indiceFila=0;
					for (TipoDescuentoClasificacionDTO tipDesClaDTO : tipDesClaCol) {
						if(tipDesClaDTO.getId().getCodigoClasificacion().equals(articuloDTO.getCodigoClasificacion())){
							for (TipoDescuentoDTO tipDesDTO : tiposDescuento) {
								if(tipDesClaDTO.getId().getCodigoTipoDescuento().equals(tipDesDTO.getId().getCodigoTipoDescuento())){
									break;
								}
								indiceFila++;
							}
							aplicaDescuentoAutomatico=Boolean.TRUE;
							final String llaveDes=indiceFila+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+
									MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento")+tipDesClaDTO.getId().getCodigoTipoDescuento().toString()
									+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento")+
									MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.cantidad");
							Collection<String> llavesEncontardas=CollectionUtils.select(llavesProcesada, new Predicate() {
								public boolean evaluate(Object arg0) {
									String llaveEncontar=(String)arg0;
									
									return llaveEncontar.equals(llaveDes);
								}
							});
							if(CollectionUtils.isEmpty(llavesEncontardas)){
								llavesProcesada.add(llaveDes);
							}
						}
					}
				}
				
			}
			
			//cargar las llaves de los descuentos de pavos y pavos marcaPropia
				if(aplicaDescuentoAutomatico){
					int tamLlave=0;
					if(llaveDescto!= null){
						tamLlave=llaveDescto.length;
					}
					String []nuevallaveDescto= new String[llavesProcesada.size()+tamLlave];
					//Validar que las llaves aplicadas no se vuelvan a repetir
					int pos=0;
					for (String llave : llavesProcesada) {	
						Boolean llaveEncontrada=Boolean.FALSE;
						if(tamLlave>0){
							for (String llavDes : llaveDescto) {
								if(llave.equals(llavDes)){
									llaveEncontrada=Boolean.TRUE;
								}
							}
						}
						if(!llaveEncontrada){
							nuevallaveDescto[pos]=llave;
							pos++;
						}
					}
					//Agregar solo llaves validas y no repetidas
					if(tamLlave>0){
						for (String llavDes : llaveDescto) {
							nuevallaveDescto[pos]=llavDes;
							pos++;
						}
					}
					nuevallaveDescto = eliminarLlavesRepetidas(nuevallaveDescto);
					request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llavesProcesada);
					request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, nuevallaveDescto);
					request.getSession().setAttribute(CotizarReservarAction.TIPO_DESCUENTO_CLASIFICACION_PAVOS_COL, tipDesClaCol);
				}
				return llaveDescto;
			}
		
		/**
		 * Inicia el parametro para aplicar los descuentos por cajas
		 * @param request
		 * @param formulario
		 */
		public static String[] iniciarDescuentoPorCajas(HttpServletRequest request, ArticuloDTO articuloDTO) throws Exception{
			LogSISPE.getLog().info("entra a validar si debe o no aplicar descuentos automatico de caja");
			//Agregar las nuevas llaves de descuentos automaticos.
			Collection<TipoDescuentoDTO> tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			Collection<String> llavesProcesada=(Collection<String>)request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
			String[] llaveDescto= (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
			
			//se saca un respaldo de las llaves de descuentos variables para que no se pierdan 
			Collection<String> llavesDctoVariables  = new ArrayList<String>();
			if(llaveDescto != null && llaveDescto.length > 0){
				for(String llaveAct : llaveDescto){
					if(llaveAct != null && llaveAct.contains(CODIGO_GERENTE_COMERCIAL)  && llaveAct.contains("INX")){
						llavesDctoVariables.add(llaveAct);
					}
				}
			}
			String sinDescuentos= (String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
			//Obtener las clasificaciones para los tipos de descuentos obtenidos desde la tabla de parametros
			if(!CollectionUtils.isEmpty(tiposDescuento)){
					//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
					if(llavesProcesada==null){
						llavesProcesada= new ArrayList<String>();
					}
					final String llaveDes = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
					Collection<String> llavesEncontardas=CollectionUtils.select(llavesProcesada, new Predicate() {
					public boolean evaluate(Object arg0) {
							String llaveEncontar=(String)arg0;
								return llaveEncontar.equals(llaveDes);
							}
					});
					if(CollectionUtils.isEmpty(llavesEncontardas)){
							llavesProcesada.add(llaveDes);
					}
			}
			
			//cargar las llaves de los descuentos de pavos y pavos marcaPropia
			if(sinDescuentos==null){	
				int tamLlave=0;
				if(llaveDescto!= null){
					tamLlave=llaveDescto.length;
				}
				
				if(!CollectionUtils.isEmpty(llavesProcesada)){
					String []nuevallaveDescto= new String[llavesProcesada.size()+tamLlave+llavesDctoVariables.size()];
					//Validar que las llaves aplicadas no se vuelvan a repetir
					int pos=0;
						for (String llave : llavesProcesada) {	
							Boolean llaveEncontrada=Boolean.FALSE;
							if(tamLlave>0){
								for (String llavDes : llaveDescto) {
									if(llave.equals(llavDes)){
										llaveEncontrada=Boolean.TRUE;
									}
								}
							}
							if(!llaveEncontrada){
								nuevallaveDescto[pos]=llave;
								pos++;
							}
						}
						//Agregar solo llaves validas y no repetidas
						if(tamLlave>0){
							for (String llavDes : llaveDescto) {
								nuevallaveDescto[pos]=llavDes;
								pos++;
							}
						}		
	
						
						for(String llaveAct: llavesDctoVariables){
							nuevallaveDescto[pos] = llaveAct;
							pos++;
						}
						nuevallaveDescto = eliminarLlavesRepetidas(nuevallaveDescto);	
						request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llavesProcesada);
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, nuevallaveDescto);
						request.getSession().setAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_CAJAS,"SI");
					//return llaveDescto;
				}
			}
			return llaveDescto;
		}
		
		/**
		 * Inicia el parametro para aplicar los descuentos por cajas
		 * @param request
		 * @param formulario
		 */
		public static String[] iniciarDescuentoPorMayorista(HttpServletRequest request) throws Exception{
			LogSISPE.getLog().info("entra a validar si debe o no aplicar descuentos de pavos automaticos");
			//Agregar las nuevas llaves de descuentos automaticos.
			Collection<TipoDescuentoDTO> tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			Collection<String> llavesProcesada=(Collection<String>)request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
			String[] llaveDescto= (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
			String[] nuevallaveDescto = null;
			
			//se saca un respaldo de las llaves de descuentos variables para que no se pierdan 
			Collection<String> llavesDctoVariables  = new ArrayList<String>();
			if(llaveDescto != null && llaveDescto.length > 0){
				for(String llaveAct : llaveDescto){
					if(llaveAct != null && (llaveAct.contains(CODIGO_GERENTE_COMERCIAL))){
						llavesDctoVariables.add(llaveAct);
					}
				}
			}
			
			String sinDescuentos= (String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
			
			//Obtener las clasificaciones para los tipos de descuentos obtenidos desde la tabla de parametros
			if(!CollectionUtils.isEmpty(tiposDescuento)){
					//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
					if(llavesProcesada==null){
						llavesProcesada= new ArrayList<String>();
					}
					else{
						llavesProcesada=CollectionUtils.select(llavesProcesada, new Predicate() {
							public boolean evaluate(Object arg0) {
									String llaveEncontar=(String)arg0;
										return llaveEncontar!=null;
									}
							});
					}
					final String llaveDes = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
					Collection<String> llavesEncontardas=CollectionUtils.select(llavesProcesada, new Predicate() {
					public boolean evaluate(Object arg0) {
							String llaveEncontar=(String)arg0;
								return llaveEncontar!=null?llaveEncontar.equals(llaveDes):false;
							}
					});
					if(CollectionUtils.isEmpty(llavesEncontardas)){
							llavesProcesada.add(llaveDes);
					}
			}
			
			
			if(sinDescuentos==null){	
					int tamLlave=0;
					if(llaveDescto!= null){
						tamLlave=llaveDescto.length;
					}
					if(!CollectionUtils.isEmpty(llavesProcesada)){
						nuevallaveDescto = new String[llavesProcesada.size()+tamLlave];
						//Validar que las llaves aplicadas no se vuelvan a repetir
						int pos=0;
							for (String llave : llavesProcesada) {	
								Boolean llaveEncontrada=Boolean.FALSE;
								if(tamLlave>0){
									for (String llavDes : llaveDescto) {
										if(llave.equals(llavDes)){
											llaveEncontrada=Boolean.TRUE;
										}
									}
								}
								if(!llaveEncontrada){
									nuevallaveDescto[pos]=llave;
									pos++;
								}
							}
							//Agregar solo llaves validas y no repetidas
							if(tamLlave>0){
								for (String llavDes : llaveDescto) {
									if(llavDes!=null){
										nuevallaveDescto[pos]=llavDes;
										pos++;
									}
								}
							}		
	
							
							for(String llaveAct: llavesDctoVariables){
								Boolean llaveEncontrada=Boolean.FALSE;
								for(String llaveNuevas: nuevallaveDescto){
									if(llaveAct!=null && llaveNuevas!=null && llaveNuevas.equals(llaveAct) ){
										llaveEncontrada=Boolean.TRUE;
									}
								}
								if(!llaveEncontrada){
									nuevallaveDescto[pos] = llaveAct;
									pos++;
								}
							}
							nuevallaveDescto = eliminarLlavesRepetidas(nuevallaveDescto);	
							request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llavesProcesada);
							request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, nuevallaveDescto);
							request.getSession().setAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_MAYORISTA,"SI");
					}
					//return llaveDescto;
				}
				return llaveDescto == null ? nuevallaveDescto : llaveDescto ;
			}
		
		public static void eliminarClasificacionDctosConAutorizacion(HttpServletRequest request, Collection<TipoDescuentoClasificacionDTO> clasificacionesPavos) throws Exception{
			try{
				Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);						
				
				if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){
			
					//se recorren los detalles y se buscan las clasificaciones
					for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
						if(detalleActual.getEstadoDetallePedidoDTO() != null && CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							
							if(CollectionUtils.isNotEmpty(clasificacionesPavos)){
								//se recorren los tipos de descuentos para comparar las clasificaciones
								for(TipoDescuentoClasificacionDTO clasificacionAutomatica : clasificacionesPavos){
									
									//si el detalle ya tiene autorizacion se elimina la clasificacion para los descuentos automaticos
									if(detalleActual.getArticuloDTO().getCodigoClasificacion().equals(clasificacionAutomatica.getId().getCodigoClasificacion())){
										clasificacionesPavos.remove(clasificacionAutomatica);
										break;
									}
								}	
							}
						}
					}					
				}				
			}
			catch (Exception e) {
				LogSISPE.getLog().info("Error de aplicacion ",e);
			}			
		}
		
		/**
		 * Elimina llaves repetidas
		 * @param llaveDescuentoCol
		 * @return
		 * @throws Exception
		 */
		public static  Collection<String>  eliminarLlavesRepetidas (Collection<String> llaveDescuentoCol) throws Exception{
			LogSISPE.getLog().info("ingreso al metodo de eliminar llaves repetidas");
			Collection<String> llavesNoRepetidas = new ArrayList<String>();
			if(CollectionUtils.isNotEmpty(llaveDescuentoCol)){
				for(String llaveActual : llaveDescuentoCol){
					if(llaveActual != null && !llaveActual.equals("")){
						Boolean encontroLlave = Boolean.FALSE;
						String llaveOrg = llaveActual;
						//se quita el primer elemento de la llave
						llaveOrg = llaveOrg.replace(llaveOrg.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
						if(llaveOrg.split(SEPARADOR_TOKEN).length == 4){
//							llaveOrg = llaveOrg.replace(SEPARADOR_TOKEN+llaveOrg.split(SEPARADOR_TOKEN)[3], "");
							llaveOrg = llaveOrg.replace(llaveOrg.split(SEPARADOR_TOKEN)[3], llaveOrg.split(SEPARADOR_TOKEN)[3].substring(llaveOrg.split(SEPARADOR_TOKEN)[3].length()-1));
						}
						if(CollectionUtils.isNotEmpty(llavesNoRepetidas)){
							for(String llaveNoRepetida : llavesNoRepetidas){
//								if(llaveNoRepetida.contains(llaveOrg) || llaveActual.contains(llaveNoRepetida)){
//									encontroLlave = Boolean.TRUE;
//								}
								if(llaveOrg.split(SEPARADOR_TOKEN).length == 4 && llaveNoRepetida.split(SEPARADOR_TOKEN).length == 5){
									if(llaveNoRepetida.contains(llaveOrg.substring(0,llaveOrg.length()-2)) && llaveNoRepetida.split(SEPARADOR_TOKEN)[4].substring(llaveNoRepetida.split(SEPARADOR_TOKEN)[4].length()-1).equals(llaveOrg.split(SEPARADOR_TOKEN)[3])){
										encontroLlave = Boolean.TRUE;
									}	
								}else{
									if(llaveNoRepetida.contains(llaveOrg)){
										encontroLlave = Boolean.TRUE;
									}
								}
								
							}
							if(!encontroLlave){
								llavesNoRepetidas.add(llaveActual);
							}
						}else{ 
							llavesNoRepetidas.add(llaveActual);
						}
					}
				}				
			}
			return llavesNoRepetidas;
		}
		
		/**
		 * Elimina llaves repetidas
		 * @param llaveDescuentoCol
		 * @return
		 * @throws Exception
		 */
		public static  String[]  eliminarLlavesRepetidas (String[] llaveDescuentoCol) throws Exception{
			LogSISPE.getLog().info("ingreso al metodo de eliminar llaves repetidas del opDescuentos");
			Collection<String> llavesDescuentos = new ArrayList<String>();
			if(llaveDescuentoCol != null && llaveDescuentoCol.length> 0){
				for(String llaveActual : llaveDescuentoCol){
					llavesDescuentos.add(llaveActual);
				}
				llavesDescuentos = eliminarLlavesRepetidas(llavesDescuentos);
//				ColeccionesUtil.sort(llavesDescuentos, ColeccionesUtil.ORDEN_ASC);
				String[] opDescuentos = new String[llavesDescuentos.size()];
				if(CollectionUtils.isNotEmpty(llavesDescuentos)){
					int posVectos = 0;
					for(String llaveAct : llavesDescuentos){
						opDescuentos[posVectos] = llaveAct;
						posVectos++;
					}
				}
				return opDescuentos;
			}
			else return null;
		}
		
		
		private  static Collection<TipoDescuentoDTO> quitarTipoDesCajaMayorista(Collection<TipoDescuentoDTO> tipoDescuento, HttpServletRequest request) throws SISPEException {
			Collection<TipoDescuentoDTO> colDescuento = null;
			try {
				final ParametroDTO parametroCodigoCaja = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja", request);
				final ParametroDTO parametroCodigoMayorista = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista", request);
				colDescuento = CollectionUtils.select(tipoDescuento, new Predicate() {
					
					public boolean evaluate(Object arg0) {
						TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO) arg0;
						
						return !tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(parametroCodigoCaja.getValorParametro()) &&
								!tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(parametroCodigoMayorista.getValorParametro());
					}
				});
				
				
			} catch (Exception e) {
				throw new SISPEException(e.getMessage());
			}
			
			return colDescuento;
		}
		
		
		private static void validarVisibilidadMotivoDescuento(HttpServletRequest request,Collection<TipoDescuentoDTO> tipoDescuento)  throws SISPEException {
			try{
					
				//CAMBIOS OSCAR
//				ParametroDTO parametroPagoEfectivo = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo", request);
					
//				String codMaxiNavidadEmpresarial = parametroPagoEfectivo.getValorParametro();//SETEAR ESTE VALOR CUANDO SE CARGA
//				String codigoTipoDescuentoVariable=null;
//				String codigoTipoDescuentoGeneral= null;
//				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoGeneral", request);
//				if(parametroDTO.getValorParametro()!=null){
//					codigoTipoDescuentoGeneral = parametroDTO.getValorParametro();
//				}
//												
//				parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
//				if(parametroDTO.getValorParametro()!=null){
//					codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
//				}
				
				List<String> codigosDctoPorMonto = null;
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipos.descuentos.por.monto", request);
				if(parametroDTO.getValorParametro()!=null){
					codigosDctoPorMonto =  Arrays.asList(parametroDTO.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion")));
				}
				
				for (TipoDescuentoDTO tipoDescuentoDTO : tipoDescuento) {					
	//						if (tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(codMaxiNavidadEmpresarial) || 
	//								tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoVariable) || 
	//								tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoGeneral)) {
					if(codigosDctoPorMonto.contains(tipoDescuentoDTO.getId().getCodigoTipoDescuento())){
							tipoDescuentoDTO.setNpVisible(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.monto"));
					} else {
						tipoDescuentoDTO.setNpVisible(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.cantidad"));
					}
				}
			} catch (Exception e) {
				throw new SISPEException(e.getMessage());
			}
			
		}
		
		
		/**
		 * 
		 * @param request
		 * @param formulario
		 * @param errors		
		 * @return 
		 * @throws Exception
		 */
		public static Collection<DetallePedidoDTO> calcularDiferenciaDetallePedido(HttpServletRequest request, ActionForm formulario, ActionErrors errors) throws Exception {
			
			LogSISPE.getLog().info("--Entro en el Metodo obtenerDiferenciaDetallePedido");
			
			Collection<DetallePedidoDTO> detallePedidoDTOs = null;
			DetallePedidoDTO detallePedidoDTOClone = null;
						
			ParametroDTO parametroFiltro = null;
			String clasificacionRecetaNueva = null;
			
//			String tipoCanasto = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//			String tipoDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
			
			Long cantMod = 0L;
			Long cantOrg = 0L;
			
			Boolean existenCanastos = Boolean.FALSE;			
			
			try {
				
				HttpSession session = request.getSession();
				
				Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
				detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
				List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
				detallePedidoDTOs = new ArrayList<DetallePedidoDTO>();	
				
				if(clasificacionRecetaNueva == null){
					parametroFiltro = new ParametroDTO();
					parametroFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					parametroFiltro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas"));
					clasificacionRecetaNueva = SessionManagerSISPE.getServicioClienteServicio().transObtenerValorParametro(parametroFiltro).getValorParametro();
				}
				
				if(!CollectionUtils.isEmpty(detalleModificado) && !CollectionUtils.isEmpty(detalleOriginal)){
					
					//se recorre la coleccion detallePedidoDTOMod para ver q cambio se realizaron en el pedido
					for(DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
						
						//primero se verifica si el articulo esta en las dos colecciones
						//se busca articulos nuevos en el pedido
						if(ColeccionesUtil.simpleSearch("id.codigoArticulo", 
								detallePedidoDTOMod.getId().getCodigoArticulo(), detalleOriginal).isEmpty()){						
							
							detallePedidoDTOs.add(detallePedidoDTOMod);					
							LogSISPE.getLog().info("El articulo {} - {} es nuevo en el pedido", detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo(), 
									detallePedidoDTOMod.getArticuloDTO().getDescripcionArticulo());						
							
						} else {//si el articulo esta en las dos colecciones se verifica si cambiaron las cantidades
							
							for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
								existenCanastos = Boolean.FALSE;
								//si los articulos en los pedidos son iguales
								if(detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo().
										equals(detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo())){
									
									LogSISPE.getLog().info("El articulo {} es comun en los dos pedidos ", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
									
									cantMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getCantidadEstado();
									cantOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getCantidadEstado();
									
									//control para los articulos que son receta
//									if(detallePedidoDTOOrg.getArticuloDTO().getCodigoTipoArticulo().equals(tipoCanasto) || 
//											detallePedidoDTOOrg.getArticuloDTO().getCodigoTipoArticulo().equals(tipoDespensa)){
//										existenCanastos = Boolean.TRUE;
//										LogSISPE.getLog().info("Existen canastos {} ", existenCanastos);									
//										//se verifica si es canasto modificado
//										if(detallePedidoDTOOrg.getArticuloDTO().getCodigoClasificacion().equals(clasificacionRecetaNueva)){
//											LogSISPE.getLog().info("-------- ES CANASTO MODIFICADO --------");
//										}
//									}
									
									detallePedidoDTOOrg.setNpCantidadAnterior(cantOrg);
									
									//se verifica si las cantidades de los articulos han cambiado
									if(!cantMod.equals(cantOrg)){		
										
										//insertamos el articulo con la cantidad cambiada en la coleccion
										detallePedidoDTOClone = new DetallePedidoDTO();
										detallePedidoDTOClone = detallePedidoDTOOrg.clone();
										detallePedidoDTOClone.getEstadoDetallePedidoDTO().setCantidadEstado(cantMod-cantOrg);
										detallePedidoDTOClone.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantMod-cantOrg);
										detallePedidoDTOClone.getEstadoDetallePedidoDTO().setReservarBodegaSIC(null);
										
										if(!CollectionUtils.isEmpty(detallePedidoDTOClone.getEntregaDetallePedidoCol())){
											
											for(Iterator<EntregaDetallePedidoDTO> ite = detallePedidoDTOClone.getEntregaDetallePedidoCol().iterator(); ite.hasNext();){
												EntregaDetallePedidoDTO entDetPedDto = ite.next();
												//TODO Validar beneficiarios
//												dto.setBeneficiarios(null);
//												dto.setNpContadorBeneficiario(0l);
												
												//variable para indicar como se debe realizar la eliminaci\u00F3n del costo
												request.getSession().setAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL, "ok");
												
												//Si la entrega tiene como lugar de entrega el domicilio voy a cargar direccion y costo
												if(entDetPedDto.getEntregaPedidoDTO().getCodigoContextoEntrega().toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
													EntregaLocalCalendarioUtil.cargaDireccionesYCostos(request, entDetPedDto, ((CotizarReservarForm)formulario).getSubTotal());
													LogSISPE.getLog().info("codigo de la direccion: {}", entDetPedDto.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
												}
												//se eliminan los costos de las entregas
												((CotizarReservarForm) formulario).eliminaCostoEntrega(entDetPedDto, request);
												((CotizarReservarForm) formulario).eliminaEntregasDetallePedido(errors, request, entDetPedDto, detallePedidoDTOClone.getArticuloDTO().getNpTipoBodega());
											}
											
											detallePedidoDTOClone.setEntregaDetallePedidoCol(null);
											detallePedidoDTOClone.setNpContadorEntrega(0L);
											detallePedidoDTOClone.setNpContadorDespacho(0L);
											detallePedidoDTOClone.setNpCedulasBeneficiarios(null);
											detallePedidoDTOClone.setNpLocalesDireccionesEntrega(null);
										}
										
										
//										if(detallePedidoDTOClone.getEntregas()!=null && !detallePedidoDTOClone.getEntregas().isEmpty()){
//											for(Iterator<EntregaDTO> ite = detallePedidoDTOClone.getEntregas().iterator();ite.hasNext();){
//												EntregaDTO entregaDTO = ite.next();
//												entregaDTO.setBeneficiarios(null);
//												entregaDTO.setNpContadorBeneficiario(0l);
//												//variable para indicar como se debe realizar la eliminaci\u00F3n del costo
//												request.getSession().setAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL, "ok");
//												
//												//Si la entrega tiene como lugar de entrega el domicilio voy a cargar direccion y costo
//												if(entregaDTO.getCodigoContextoEntrega().toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//													EntregaLocalCalendarioUtil.cargaDireccionesYCostos(request, entregaDTO, ((CotizarReservarForm)formulario).getSubTotal());
//													LogSISPE.getLog().info("codigo de la direccion: {}", entregaDTO.getNpDireccionEntregaDomicilio());
//												}												
//												//se eliminan los costos de las entregas											
//												((CotizarReservarForm) formulario).eliminaCostoEntrega(entregaDTO, request);
//												((CotizarReservarForm) formulario).eliminaEntregasDetallePedido(errors, request, entregaDTO, detallePedidoDTOClone.getArticuloDTO().getNpTipoBodega());												
//											}
//											
//											detallePedidoDTOClone.setEntregas(null);
//											detallePedidoDTOClone.setNpContadorEntrega(0L);
//											detallePedidoDTOClone.setNpContadorDespacho(0L);
//											detallePedidoDTOClone.setNpCedulasBeneficiarios(null);
//											detallePedidoDTOClone.setNpLocalesDireccionesEntrega(null);
//										}
										
										
										detallePedidoDTOs.add(detallePedidoDTOClone);
										
										LogSISPE.getLog().info("----------- {} cambio de cantidad ", detallePedidoDTOClone.getArticuloDTO().getDescripcionArticulo());
										LogSISPE.getLog().info("----------- {} cantidad inicial ", cantOrg);
										LogSISPE.getLog().info("----------- {} cantidad nueva ", cantMod);					
										
									}
									
									cantMod = 0L;
									cantOrg = 0L;
									
									if(!existenCanastos)
										break; //si hay articulos que no son de tipo canasto no es necesario seguir recorriendo la coleccion
								}
							}
						}
					}
				}
				//objetos elegibles para el garbage collector
				cantMod = null;
				cantOrg = null;			
				detallePedidoDTOClone = null;	
				
			} catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);				
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());				
			}
			//se retorna la coleccion DetallePedidoDTO con los cambios que hay en el pedido
			return detallePedidoDTOs;
		}
		
		/**
		 * Se obtiene solo los articulos nuevos que se agregaron aun pedido
		 * @param request
		 * @return
		 * @throws Exception
		 */
		public static Collection<DetallePedidoDTO> obtenerDetallePedidoNuevos(HttpServletRequest request) throws Exception {
			
			Collection<DetallePedidoDTO> detallePedidoDTOs = null;
			
			try {
				
				HttpSession session = request.getSession();
				
				Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
				detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
				List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
				detallePedidoDTOs = new ArrayList<DetallePedidoDTO>();
				
				for(DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
				
					if(ColeccionesUtil.simpleSearch("id.codigoArticulo", 
							detallePedidoDTOMod.getId().getCodigoArticulo(), detalleOriginal).isEmpty()){
						//si no se encuentra se agrega a la coleccion resultado
						detallePedidoDTOMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_NUEVO);
						detallePedidoDTOs.add(detallePedidoDTOMod);					
						LogSISPE.getLog().info("El articulo {} es nuevo en el pedido", detallePedidoDTOMod.getId().getCodigoArticulo());
						
					}
				}
				
			}catch(SISPEException ex){
				
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());
			}
			
			return detallePedidoDTOs;
		}
		
		/**
		 * Compara dos colecciones de tipo DetallePedidoDTO		 * 
		 * @param tipoProceso, El proceso que se va ha realizar en el POS
		 * @return coleccion con los cambios de articulos y cantidades
		 * @throws Exception 
		 */
		public static Collection<DetallePedidoDTO> compararDetallePedidoPOS(HttpServletRequest request, String tipoProceso) throws Exception {
			
			LogSISPE.getLog().info("--Entro en el Metodo comparar detalles del pedido");
			
			Collection<DetallePedidoDTO> detallePedidoDTOs = null;
			DetallePedidoDTO detallePedidoDTOClone = null;
			DetallePedidoDTO detallePedidoDTOCloneAnu = null;	
			DetallePedidoDTO detallePedidoDTOCloneOrgMod = null;
			//String clasificacionRecetaNueva = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas");
			
			//Tipo de proceso = 1, solo se elimina o se reduce la cantidad de los productos
//			String tipoProcesoQuitarProductos = TIPO_PROCESO_QUITAR_PRODUCTOS;			
			//Tipo de proceso = 2, solo se agrega o se aumenta la cantidad de los productos			 
			String tipoProcesoAumentarProductos = TIPO_PROCESO_AUMENTAR_PRODUCTOS; 
			//Tipo de proceso = 3, se aumenta o se reduce los productos
			String tipoProcesoQuitarAumentarProductos = TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS;
			//Tipo de proceso = 4, se aumenta o disminuyo el monto del pedido
			String tipoProcesoDisminuirAumentarMontoPedido = TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
			
			Long cantMod = 0L;
			Long cantOrg = 0L;
			
			Double totalVentaMod = 0D;
			Double totalVentaOrg = 0D;
			
			Double totalVentaRecMod = 0D;
			Double totalVentaRecOrg = 0D;
			
			Double pesoVentaMod = 0D;
			Double pesoVentaOrg = 0D;
			
			try {
				
				HttpSession session = request.getSession();
				
				Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
				detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
				List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
				detallePedidoDTOs = new ArrayList<DetallePedidoDTO>();				
				//se anula todos los detalles ya que se va ha crear un nuevo pedido
				if(tipoProcesoAumentarProductos.equals(tipoProceso) || tipoProcesoQuitarAumentarProductos.equals(tipoProceso)) {
					for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
						
						for(DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
							
							if(detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo().
									equals(detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo())){
								
								cantMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getCantidadEstado();
								cantOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getCantidadEstado();
								
								detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_ANULAR);
								detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantMod);
								
								detallePedidoDTOs.add(detallePedidoDTOOrg);
								
								LogSISPE.getLog().info(new StringBuilder().append("El articulo ").append(detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo())
										.append(" - ").append(detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo()).append(" cantidad - ")
										.append(detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getCantidadEstado()).append(" se va anular del pedido").toString());
							
							}
						}
					}
					
					if(tipoProcesoQuitarAumentarProductos.equals(tipoProceso)){
						//si se elimino algun articulo del pedido
						for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
							if(ColeccionesUtil.simpleSearch("articuloDTO.id.codigoArticulo", 
									detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo(), detalleModificado).isEmpty()){
								
								detallePedidoDTOOrg.setNpExistenEliminados(Boolean.TRUE);
								detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_ANULAR);
								detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoEliminado(ConstantesGenerales.ESTADO_ACTIVO);
								detallePedidoDTOs.add(detallePedidoDTOOrg);
								
								LogSISPE.getLog().info("El articulo {} - {} se elimino del pedido", detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo(), 
										detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
								
							}
						}
					}
				}else{
					//si se elimino algun articulo del pedido
					for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
						if(ColeccionesUtil.simpleSearch("articuloDTO.id.codigoArticulo", 
								detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo(), detalleModificado).isEmpty()){
							
							detallePedidoDTOOrg.setNpExistenEliminados(Boolean.TRUE);
							detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_ANULAR);
							detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoEliminado(ConstantesGenerales.ESTADO_ACTIVO);
							detallePedidoDTOs.add(detallePedidoDTOOrg);
							
							LogSISPE.getLog().info("El articulo {} - {} se elimino del pedido", detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo(), 
									detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
							
						}
					}
					//se recorre la coleccion detallePedidoDTOMod para ver q cambio se realizaron en el pedido
					for(DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
						//primero se verifica si el articulo esta en las dos colecciones
						//se busca articulos nuevos en el pedido
						if(ColeccionesUtil.simpleSearch("articuloDTO.id.codigoArticulo", 
								detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo(), detalleOriginal).isEmpty()){
							//si no se encuentra se agrega a la coleccion resultado
							detallePedidoDTOMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_NUEVO);
							detallePedidoDTOs.add(detallePedidoDTOMod);					
							LogSISPE.getLog().info("El articulo {} - {} es nuevo en el pedido", detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo(), 
									detallePedidoDTOMod.getArticuloDTO().getDescripcionArticulo());
													
						} else {//si el articulo esta en las dos colecciones se verifica si cambiaron las cantidades
							
							for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
								
								//si los articulos en los pedidos son iguales
								if(detallePedidoDTOMod.getArticuloDTO().getId().getCodigoArticulo().
										equals(detallePedidoDTOOrg.getArticuloDTO().getId().getCodigoArticulo())){
									
									LogSISPE.getLog().info("El articulo {} es comun en los dos pedidos ", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
									
									cantMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getCantidadEstado();
									cantOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getCantidadEstado();
									
									totalVentaMod = Util.roundDoubleMath(detallePedidoDTOMod.getEstadoDetallePedidoDTO().getValorTotalVenta(),NUMERO_DECIMALES);
									totalVentaOrg = Util.roundDoubleMath(detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getValorTotalVenta(),NUMERO_DECIMALES);
									
									pesoVentaMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getPesoArticuloEstado()!=null ? detallePedidoDTOMod.getEstadoDetallePedidoDTO().getPesoArticuloEstado(): 0D;
									pesoVentaOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getPesoArticuloEstado()!=null ? detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getPesoArticuloEstado(): 0D;
									
									//se verifica si las cantidades de los articulos han cambiado
									if(!cantMod.equals(cantOrg) && !tipoProcesoDisminuirAumentarMontoPedido.equals(tipoProceso)){
										detallePedidoDTOCloneAnu = new DetallePedidoDTO();
										detallePedidoDTOCloneAnu = SerializationUtils.clone(detallePedidoDTOOrg);
										if(detallePedidoDTOMod.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasElaboradas"))){
											Collection<ArticuloRelacionDTO> recetaArticuloMod=detallePedidoDTOMod.getArticuloDTO().getArticuloRelacionCol();
											Collection<ArticuloRelacionDTO> recetaArticuloCloneOrg=detallePedidoDTOCloneAnu.getArticuloDTO().getArticuloRelacionCol();
											
											
											for(ArticuloRelacionDTO artRelDTOMod:recetaArticuloMod){
												ArticuloRelacionDTO artRelDTOCloneOrg=(ArticuloRelacionDTO)CollectionUtils.find(recetaArticuloCloneOrg,new BeanPropertyValueEqualsPredicate("articuloRelacion.id.codigoArticulo",artRelDTOMod.getArticuloRelacion().getId().getCodigoArticulo()));
												
												totalVentaRecMod = Util.roundDoubleMath(artRelDTOMod.getValorTotalEstadoNeto(),NUMERO_DECIMALES);
												totalVentaRecOrg = Util.roundDoubleMath(artRelDTOCloneOrg.getValorTotalEstadoNeto(),NUMERO_DECIMALES);
												if(cantOrg > cantMod){
													artRelDTOCloneOrg.setCantidadPrevioEstadoDescuento(cantOrg-cantMod);
													artRelDTOCloneOrg.setValorTotalEstadoNeto(totalVentaRecOrg-totalVentaRecMod);
												}else{
													artRelDTOCloneOrg.setCantidadPrevioEstadoDescuento(cantMod - cantOrg);
													artRelDTOCloneOrg.setValorTotalEstadoNeto(totalVentaRecMod - totalVentaRecOrg);
												}
											}
										}
										
											//agregamos el articulo original que se anula
											if(cantOrg > cantMod){
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setCantidadEstado(cantOrg-cantMod);
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setValorTotalVenta(totalVentaOrg-totalVentaMod);
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setPesoArticuloEstado(pesoVentaOrg-pesoVentaMod);
											}else{
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setCantidadEstado(cantMod - cantOrg);
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setValorTotalVenta(totalVentaMod - totalVentaOrg);
												detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setPesoArticuloEstado(pesoVentaMod - pesoVentaOrg);
											}
											detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantMod);
											detallePedidoDTOCloneAnu.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_ANULAR);
											detallePedidoDTOs.add(detallePedidoDTOCloneAnu);
											
											//agregamos el articulo nuevo 
											detallePedidoDTOClone = new DetallePedidoDTO();								
											detallePedidoDTOClone = SerializationUtils.clone(detallePedidoDTOMod);
											detallePedidoDTOClone.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_NUEVO);
											detallePedidoDTOs.add(detallePedidoDTOClone);
										
										LogSISPE.getLog().info("----------- {} cambio de cantidad ", detallePedidoDTOClone.getArticuloDTO().getDescripcionArticulo());
										LogSISPE.getLog().info("----------- {} cantidad inicial ", cantOrg);
										LogSISPE.getLog().info("----------- {} cantidad nueva ", cantMod);			
										
									}else{//caso contrario el articulo no ha tenido cambios
//										detallePedidoDTOMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_IGUAL);
										//jparedes
										//Se agrego este control porque no solo puede ser el tipo proceso quitar articulos si no tambien por disminucion e incremento del monto del pedido
										if(tipoProcesoDisminuirAumentarMontoPedido.equals(tipoProceso) && !totalVentaMod.equals(totalVentaOrg)){
											detallePedidoDTOCloneOrgMod = new DetallePedidoDTO();
											detallePedidoDTOCloneOrgMod = SerializationUtils.clone(detallePedidoDTOOrg);
											detallePedidoDTOCloneOrgMod.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantOrg);
											detallePedidoDTOCloneOrgMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_IGUAL);
											LogSISPE.getLog().info("El articulo {} no cambia de cantidad pero si el valor del pedido ", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
											detallePedidoDTOs.add(detallePedidoDTOCloneOrgMod);
										}else{
										detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_IGUAL);
										LogSISPE.getLog().info("El articulo {} no cambia de cantidad ", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
										detallePedidoDTOs.add(detallePedidoDTOOrg);
										}
									}
									
									cantMod = 0L;
									cantOrg = 0L;
									
	//								if(!existenCanastos)
	//									break; //si hay articulos que no son de tipo canasto no es necesario seguir recorriendo la coleccion
								}
								
							}
						}
					}
					
					//objetos elegibles para el garbage collector
					cantMod = null;
					cantOrg = null;			
					detallePedidoDTOClone = null;
					detallePedidoDTOCloneAnu = null;
				}
				
			} catch(SISPEException ex){
				
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);				
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());
				
			}
			return detallePedidoDTOs;
		}
		
		/**
		 * Se compara los cambios que ha tenido un pedido, tomando como referencia el valorTotalVenta() de cada detalle del pedido
		 * @param request
		 * @return
		 */
		public static Collection<DetallePedidoDTO> compararDetallePedido(HttpServletRequest request) {
			LogSISPE.getLog().info("----------- Entro al metodo compararDetallePedido para calcular la devolucion -----------");
			Collection<DetallePedidoDTO> detallePedidoDTOs = null;
			
			Double montoMod = 0D;
			Double montoOrg = 0D;
			
			try{
				
				HttpSession session = request.getSession();
				
				Collection<DetallePedidoDTO> detallePedidoOriginal = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
				detallePedidoOriginal = ColeccionesUtil.sort(detallePedidoOriginal, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				Collection<DetallePedidoDTO> detallePedidoModificado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				detallePedidoModificado = ColeccionesUtil.sort(detallePedidoModificado, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				List<DetallePedidoDTO> detalleOriginal = (ArrayList<DetallePedidoDTO>)detallePedidoOriginal;
				List<DetallePedidoDTO> detalleModificado = (ArrayList<DetallePedidoDTO>)detallePedidoModificado;
				detallePedidoDTOs = new ArrayList<DetallePedidoDTO>();
				
				if(!CollectionUtils.isEmpty(detalleModificado) && !CollectionUtils.isEmpty(detalleOriginal)){
				
					//si se elimino algun articulo del pedido
					for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
						if(ColeccionesUtil.simpleSearch("id.codigoArticulo", 
								detallePedidoDTOOrg.getId().getCodigoArticulo(), detalleModificado).isEmpty()){
							
							detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoEliminado(ConstantesGenerales.ESTADO_ACTIVO);
							detallePedidoDTOs.add(detallePedidoDTOOrg);
							LogSISPE.getLog().info("El articulo {} se elimino del pedido", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
						}
					}
					
					//se recorre la coleccion detallePedidoDTOMod para ver q cambio se realizaron en el pedido
					for(DetallePedidoDTO detallePedidoDTOMod : detalleModificado){
						LogSISPE.getLog().info("El articulo detallePedidoDTOMod {} a buscar en la coleccion original detallePedidoDTOOrg", detallePedidoDTOMod.getArticuloDTO().getDescripcionArticulo());
						//si el articulo esta en las dos colecciones se verifica si cambiaron las cantidades
						for(DetallePedidoDTO detallePedidoDTOOrg : detalleOriginal){
							
							//si los articulos en los pedidos son iguales
							if(detallePedidoDTOMod.getId().getCodigoArticulo().equals(detallePedidoDTOOrg.getId().getCodigoArticulo())){
								
								LogSISPE.getLog().info("El articulo {} es comun en los dos pedidos ", detallePedidoDTOOrg.getArticuloDTO().getDescripcionArticulo());
								
								montoMod = detallePedidoDTOMod.getEstadoDetallePedidoDTO().getValorTotalVenta();
								montoOrg = detallePedidoDTOOrg.getEstadoDetallePedidoDTO().getValorTotalVenta();
								
								//el articulo no ha tenido cambios
								if (montoMod.doubleValue() == montoOrg.doubleValue()){
									detallePedidoDTOMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_IGUAL);
								} else {
									//agregamos el estadoRegistro del articulo anterior
									detallePedidoDTOOrg.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_ANULAR);
									detallePedidoDTOs.add(detallePedidoDTOOrg);
									
									//agregamos el estadoRegistro del articulo nuevo
									detallePedidoDTOMod.getEstadoDetallePedidoDTO().setNpEstadoRegistro(ESTADO_REGISTRO_NUEVO);
									detallePedidoDTOs.add(detallePedidoDTOMod);
									
									LogSISPE.getLog().info("----------- {} cantidad inicial ", montoOrg);
									LogSISPE.getLog().info("----------- {} cantidad nueva ", montoMod);
								}
								
								montoOrg = 0D;
								montoMod = 0D;
								
							}
						}
					}
				}
				
			} catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n no se pudo compararDetallePedido",ex);
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());
			} catch(Exception ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n no se pudo compararDetallePedido",ex);
				LogSISPE.getLog().info("codigo error: {}",ex);
			} finally{
				//objetos elegibles para el garbage collector
				montoOrg = null;
				montoMod = null;
			}
			return detallePedidoDTOs;
		}
		
		/**
		 * M&eacute;todo para buscar elementos en una coleccion de DetallePedidoDTO
		 * @param detallePedidoDTOs colecci&oacute;n donde se va ha realizar la busqueda
		 * @param nombreAtributo atributo con el que se va ha comparar
		 * @param estados Atributos que se van a buscar
		 * @return
		 */
		private static Collection<DetallePedidoDTO> buscarDetallesPorEstado(Collection<DetallePedidoDTO> detallePedidoDTOs, 
				final String nombreAtributo, final String ...estados) {
			
			return CollectionUtils.select(detallePedidoDTOs, new Predicate() {
				
				public boolean evaluate(Object arg0) {
					DetallePedidoDTO dto = (DetallePedidoDTO) arg0;
						
					Object obj = ClasesUtil.invocarMetodoGet(dto.getEstadoDetallePedidoDTO(), nombreAtributo);
					if (obj == null) {
						return false;
					}				

					return obj.toString().equals(estados[0]);
//					for (String estado : estados) {
//						return obj.toString().equals(estado);
//					}
//					return false;
				}
			});
		}
		
		/**
		 * Calcula el valor de la devoluci&oacute;n de un pedido en base a los cambios en el detalle del pedido
		 * @param detallePedidoDTOs colecci&oacute;n con la informaci&oacute;n de los cambios que se hicieron en el detalle del pedido
		 * @return
		 */
		@Deprecated
		public static Double calcularValorDevolucion(Collection<DetallePedidoDTO> detallePedidoDTOs){
			
			Double valorDevolucion = 0d;
			
			Collection<DetallePedidoDTO> itemsEliminados = buscarDetallesPorEstado(detallePedidoDTOs, "npEstadoEliminado", ConstantesGenerales.ESTADO_ACTIVO);
			Collection<DetallePedidoDTO> itemsAnular = buscarDetallesPorEstado(detallePedidoDTOs, "npEstadoRegistro", ESTADO_REGISTRO_ANULAR);
			Collection<DetallePedidoDTO> itemsNuevo = buscarDetallesPorEstado(detallePedidoDTOs, "npEstadoRegistro", ESTADO_REGISTRO_NUEVO);
			
			if(!CollectionUtils.isEmpty(itemsEliminados)){
				for(DetallePedidoDTO dto:itemsEliminados){
						valorDevolucion += dto.getEstadoDetallePedidoDTO().getValorTotalVenta();
				}
			}
			
			
			//si existen solo modificaciones de cantidades en los items hay que sumar solo lo restante al valor total de la devoluvion
			if(!CollectionUtils.isEmpty(itemsAnular) && !CollectionUtils.isEmpty(itemsNuevo)){
				for(final DetallePedidoDTO dtoAnular : itemsAnular){
					LogSISPE.getLog().info("El articulo dtoAnular {} a buscar en la coleccion itemsNuevo", dtoAnular.getArticuloDTO().getDescripcionArticulo());
					Collection<DetallePedidoDTO> itemsNuevoEncontrados= CollectionUtils.select(itemsNuevo, new Predicate() {
						public boolean evaluate(Object arg0) {
							DetallePedidoDTO detPedDTO=(DetallePedidoDTO)arg0;
							return detPedDTO.getId().getCodigoArticulo().equals(dtoAnular.getId().getCodigoArticulo());
						}
					});	
					
					if(!CollectionUtils.isEmpty(itemsNuevoEncontrados)){
							valorDevolucion+=dtoAnular.getEstadoDetallePedidoDTO().getValorTotalVenta();
					}
				}
			}
			
			//si existen items nuevos la sumatoria de los totales de debe de restar del valor total de la devolucion
			for(final DetallePedidoDTO dtoNuevo : itemsNuevo){
				
				Collection<DetallePedidoDTO> itemsAnularEncontrados= CollectionUtils.select(itemsAnular, new Predicate() {
					public boolean evaluate(Object arg0) {
						DetallePedidoDTO detPedDTO=(DetallePedidoDTO)arg0;
						return detPedDTO.getId().getCodigoArticulo().equals(dtoNuevo.getId().getCodigoArticulo());
					}
				});
				
				if(!CollectionUtils.isEmpty(itemsAnularEncontrados)){

					//verificamos si el item se esta eliminando y agregando solo se debe restar una sola vez el valor de la devolucion
					Collection<DetallePedidoDTO> itemsEliminadosEncontrados= CollectionUtils.select(itemsEliminados, new Predicate() {
						public boolean evaluate(Object arg0) {
							DetallePedidoDTO detPedDTO=(DetallePedidoDTO)arg0;
							return detPedDTO.getId().getCodigoArticulo().equals(dtoNuevo.getId().getCodigoArticulo());
						}
					});
					if(!CollectionUtils.isEmpty(itemsEliminadosEncontrados)){
						for(DetallePedidoDTO dto:itemsEliminadosEncontrados){
							valorDevolucion -= dto.getEstadoDetallePedidoDTO().getValorTotalVenta();
						}
					}

				}
				valorDevolucion-=dtoNuevo.getEstadoDetallePedidoDTO().getValorTotalVenta();
			}

			return valorDevolucion;
		}
		
		
		/**
		 * llena una coleccion con las clasificaciones de canastos y otra con las clasificaciones de pavos
		 * @param request
		 * @throws Exception
		 */
		public static void inicializarClasificacionesPavosCanastos(HttpServletRequest request) throws Exception{
			try{
				LogSISPE.getLog().info("ingresa al metodo inicializarClasificacionesPavosCanastos");
				Collection<String> clasificacionesCanastos = new ArrayList<String>();
				Collection<String> clasificacionesPavos = new ArrayList<String>();
				
				ParametroDTO parametroDTO =  WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
				clasificacionesCanastos.add(parametroDTO.getValorParametro());
				parametroDTO =  WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
				clasificacionesCanastos.add(parametroDTO.getValorParametro());
				
				parametroDTO =  WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.clasificacionesAutorizacionGerenteComercial.pavosPollosCanastos", request);
				clasificacionesPavos.addAll(Arrays.asList(parametroDTO.getValorParametro().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"))));
				clasificacionesPavos.removeAll(clasificacionesCanastos);
				
				request.getSession().setAttribute(CLASIFICACIONES_CANASTOS, clasificacionesCanastos);
				request.getSession().setAttribute(CLASIFICACIONES_PAVOS, clasificacionesPavos);
			}
			catch (Exception e) {
				LogSISPE.getLog().error("error al inicializar las clasificaciones de canastos y pavos", e);
			}
		}
		
		/**
		 * Verifica si la clasificacion de un articulo pertenece a canastos
		 * @param codigoClasificacion
		 * @param request
		 * @return true si es canasto, false caso contrario
		 * @throws Exception
		 */
		public static Boolean esCanasto(String codigoClasificacion, HttpServletRequest request) throws Exception{
			
			Collection<String> clasificacionesCanastos = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_CANASTOS);
			if(CollectionUtils.isEmpty(clasificacionesCanastos)){
				inicializarClasificacionesPavosCanastos(request);
				clasificacionesCanastos = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_CANASTOS);
			}
			
			if(CollectionUtils.isNotEmpty(clasificacionesCanastos) && codigoClasificacion != null && !codigoClasificacion.isEmpty())
				if(clasificacionesCanastos.contains(codigoClasificacion))
					return Boolean.TRUE;

			return Boolean.FALSE;
		}
		
		
		/**
		 * Verifica si la clasificacion de un articulo es pertenece a los pavos
		 * @param codigoClasificacion
		 * @param request
		 * @return true si es pavo, false caso contrario
		 * @throws Exception
		 */
		public static Boolean esPavo(String codigoClasificacion, HttpServletRequest request) throws Exception{
			
			Collection<String> clasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_PAVOS);
			if(CollectionUtils.isEmpty(clasificacionesPavos)){
				inicializarClasificacionesPavosCanastos(request);
				clasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_PAVOS);
			}
			
			if(CollectionUtils.isNotEmpty(clasificacionesPavos) && codigoClasificacion != null && !codigoClasificacion.isEmpty())
				if(clasificacionesPavos.contains(codigoClasificacion))
					return Boolean.TRUE;

			return Boolean.FALSE;
		}
		
		
		/**
		 * agrega a los descuentos el tipo de descuento variable ejemplo : VARIABLE ABASTOS
		 * @param colDescuentoEstadoPedido
		 * @param request
		 * @throws Exception
		 */
		public static void agregarDescripcionDescuentosVariables(Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedido, HttpServletRequest request) throws Exception{
			
			if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedido)){
				
				//se obtiene de session la coleccion de departamentos
				Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol = AutorizacionesUtil.iniciarClasificacionesDepartamentos(request);
				 for(DescuentoEstadoPedidoDTO descuentoActual :(Collection<DescuentoEstadoPedidoDTO>) colDescuentoEstadoPedido){
					 if(descuentoActual.getId().getCodigoReferenciaDescuentoVariable().contains(CODIGO_GERENTE_COMERCIAL)){

							DescuentoDTO descuento =   SerializationUtils.clone(descuentoActual.getDescuentoDTO());
							descuentoActual.setDescuentoDTO(null);
							String[] llaveDescuento = descuentoActual.getLlaveDescuento().split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"));
							if(llaveDescuento.length >= 4){
								
								String nombreDepartamento = "";
								String codigoClasificacionPadre = AutorizacionesUtil.obtenerCodigoClasificacionPadreDeLlaveDescuento(descuentoActual.getLlaveDescuento());
								
								 if(!codigoClasificacionPadre.equals("") && nombreDepartamento.equals("")){
									 
									 ClasificacionDTO departamento = null;
										if(CollectionUtils.isNotEmpty(clasificacionDepartamentosPedidoCol) && StringUtils.isNotEmpty(codigoClasificacionPadre)){
											for(ClasificacionDTO clasificacionActual : clasificacionDepartamentosPedidoCol){
												if(clasificacionActual.getId().getCodigoClasificacion().equals(codigoClasificacionPadre)){
													departamento = clasificacionActual;
													break;
												}
											}
										}
										if(departamento != null){
											nombreDepartamento = departamento.getDescripcionClasificacion().split(SEPARADOR_TOKEN)[0];
										}else{
											LogSISPE.getLog().info("No existe un departamento con la clasificacion: "+codigoClasificacionPadre);
										}
								 }	
								 if(descuento.getTipoDescuentoDTO().getDescripcionTipoDescuento().equals("VARIABLE")){
									 descuento.getTipoDescuentoDTO().setDescripcionTipoDescuento(descuento.getTipoDescuentoDTO().getDescripcionTipoDescuento()+" "+nombreDepartamento);
								 }
								 descuentoActual.setDescuentoDTO(descuento);
							 	descuentoActual.setNpNombreDescuentoVariable(descuento.getTipoDescuentoDTO().getDescripcionTipoDescuento());
							}	
					 }else{
						 descuentoActual.setNpNombreDescuentoVariable(descuentoActual.getDescuentoDTO().getTipoDescuentoDTO().getDescripcionTipoDescuento());
						 LogSISPE.getLog().info("descripcion del descuento: {}",descuentoActual.getNpNombreDescuentoVariable());
					 }
				 }
			}
		}
		
		/**
		 * genera los valores de los descuentos (CotizarReservarform.opDescuentos[]) para setearlas en el formulario
		 * @param request
		 * @param descuentos
		 * @throws Exception 
		 */
		public static void generarOpDescuentos(HttpServletRequest request, Collection<DescuentoEstadoPedidoDTO> descuentos) throws Exception{

			String desNaviEmpre = validarExisteDesNavEmpCredito(request);
			
			obtenerCodigoTipoDescuentoPorCajasMayorista(request);
			//se verifica la existencia de una colecci\u00F3n de descuentos
			if(CollectionUtils.isNotEmpty(descuentos)){
				
				Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
				Collection<String> llavesDescuentosCajaMayorista = new ArrayList<String>();
				//se sacan las llaves de los descuentos
				for(Iterator<DescuentoEstadoPedidoDTO> it = descuentos.iterator();it.hasNext();){
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = it.next();
					
					//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
					if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
						llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
					}
					else{
						if(descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
							llavesDescuentosCajaMayorista.add((String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS));
						}
						else if(descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
							llavesDescuentosCajaMayorista.add((String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA));
						}
					}
				}
				//se agregan los descuentos de caja y mayorista
				llavesDescuentosAplicados.addAll(llavesDescuentosCajaMayorista);
				
				if(CollectionUtils.isNotEmpty(llavesDescuentosAplicados)){
					
					//se generan las llaves para setearlas en opDescuentos del formulario
					Collection<TipoDescuentoDTO> tipoDescuento = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
					int llavesAdicionales = StringUtils.isNotEmpty(desNaviEmpre) ? 1 : 0;
					String[] opDescuentosSession = (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					llavesAdicionales += (opDescuentosSession != null && opDescuentosSession.length > 0) ? opDescuentosSession.length : 0;  
					String[] opDescuentos = new String[llavesDescuentosAplicados.size()+llavesAdicionales];
					int posVector = 0;
					int posDecuento = 0;
					//se recorren los tipos de descuentos
					for(TipoDescuentoDTO tipoDctoActual : tipoDescuento){
						//se recorren las llaves
						for(String llaveActual : llavesDescuentosAplicados){
							String nuevaLlave = llaveActual;
							String codigoTipoDescuento = llaveActual.split(SEPARADOR_TOKEN)[1].split("CTD")[1];
							if(tipoDctoActual.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento)){
								nuevaLlave =  nuevaLlave.replace(llaveActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, ""+posDecuento+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO);
								opDescuentos[posVector] = nuevaLlave;
								posVector++;
							}
						}
						posDecuento++;
					}
					//se agregan las llaves de navidad emresarial
					if(StringUtils.isNotEmpty(desNaviEmpre)){
						opDescuentos[posVector] = desNaviEmpre;
						posVector++;
					}
					//se agregan las llaves de session
					if(opDescuentosSession != null && opDescuentosSession.length > 0 ){
						for(String descAct : opDescuentosSession){
							opDescuentos[posVector] = descAct;
							posVector++;
						}
					}
					
					eliminarLlavesRepetidas(opDescuentos);
//					if (desNaviEmpre != null && opDescuentos != null) {
//						String[] opDescuentosAux = new String[opDescuentos.length + 1];
//						
//						for (int i = 0; i < opDescuentos.length; i++) {
//							opDescuentosAux[i] = opDescuentos[i];
//						}
//						opDescuentosAux[opDescuentos.length] = desNaviEmpre;
//
//						opDescuentos = opDescuentosAux;
//						
//					}
					request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
					request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llavesDescuentosAplicados);
				}	
			}
		}
		
		
		/**
		 * Agrega un mensaje del tipo(error, warning, exitos) solo una vez
		 * @param key
		 * @param mensajeMostrar
		 * @param identificadorMensaje
		 * @param actionMessage
		 */
		public static void agregarActionMessageNoRepetido(String key, String identificadorMensaje,  String mensajeMostrar, ActionMessages actionMessage){
			LogSISPE.getLog().info("ingresa al metodo agregarActionMessageNoRepetido");
			if(actionMessage != null){
				Iterator<ActionMessage> iterator = actionMessage.get();				
				Boolean encontroClave = Boolean.FALSE;
				
				//se recorre para buscar el key el mensaje
				while(iterator.hasNext()){
					ActionMessage messageActual = iterator.next();
					if(messageActual.getKey().equals(key))
						encontroClave = Boolean.TRUE;									
				}		
				//si no existe el mensaje se agrega
				if(!encontroClave){		
					actionMessage.add(identificadorMensaje, new ActionMessage(key, mensajeMostrar));
				}
			}
		}
		
		/**
		 * Procesa los descuentos separando los descuentos variables por comprador, y los normales se procesan comun y corriente
		 * @param request
		 * @param colDetallePedidoDTO
		 * @param llaveDescuentoCol
		 * @param descuentoVariableCol
		 * @return
		 */
		public static Collection<DescuentoEstadoPedidoDTO>  procesarDescuentosPorTipo( final HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedidoDTO, Collection<String> llavDesCol){
			try{
				Collection<String> llaveDescuentoCol = llavDesCol;
				//se eliminan los descuentos
				SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(colDetallePedidoDTO,null, null);
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();	
				ConsolidarAction.asignarAutorizacionesDetallesConsolidados(request, colDetallePedidoDTO);
				
				//declaraci\u00F3n de las variables
				HttpSession session = request.getSession();
				llaveDescuentoCol = CotizacionReservacionUtil.eliminarLlavesRepetidas(llaveDescuentoCol);
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);		
				
				if(CollectionUtils.isEmpty(colDescuentoEstadoPedidoDTOVariables))
					colDescuentoEstadoPedidoDTOVariables = new ArrayList<DescuentoEstadoPedidoDTO>();
				
				//ELIMINAR DESCUENTOS DE CAJAS Y MAYORISTAS PARA QUE NO APLIQUE A LA FUNCION DE DSTOS
				if(llaveDescuentoCol!=null){
					
					Collection<String> llavesSinCajas=CollectionUtils.select(llaveDescuentoCol, new Predicate() {
						public boolean evaluate(Object arg0) {
							String llaveEncontar=(String)arg0;
							return  !llaveEncontar.equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS)));
						}
					});
					
					Collection<String> llavesActuales=CollectionUtils.select(llavesSinCajas, new Predicate() {
						public boolean evaluate(Object arg0) {
							String llaveEncontar=(String)arg0;
							return  !llaveEncontar.equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA)));
						}
					});
					llaveDescuentoCol=new ArrayList<String>(); 
					llaveDescuentoCol=(Collection<String>)SerializationUtils.clone((Serializable)llavesActuales);
				}
				try{
					if(CollectionUtils.isNotEmpty(llaveDescuentoCol)){ 
						//se procesan los descuentos
						colDescuentoEstadoPedidoDTO = DescuentosUtil.procesarDescuentos(request, colDetallePedidoDTO, llaveDescuentoCol, colDescuentoEstadoPedidoDTOVariables);
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
				return colDescuentoEstadoPedidoDTO;
				
			}
			catch (Exception e) {
				LogSISPE.getLog().error("Error la procesar los descuentos por tipo", e);
				return null;
			}
		}
		
		
		/**
		 * obtiene las autorizaciones que has sido eliminandas para cambiarles de estado y guardarlas como inactivas
		 * @param request
		 * @param detallePedidoCol
		 * @param eliminarDetalles
		 */
		public static void obtenerAutorizacionesInactivas(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol, Boolean eliminarDetalles){
			try{
				//solo para pedidos consolidados
//				if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean)request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
					LogSISPE.getLog().info("ingresa a obtener las autorizaciones inactivas");
					
					Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
					if(CollectionUtils.isEmpty(autorizacionesDesactivarCol))
						autorizacionesDesactivarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
					
					if(CollectionUtils.isNotEmpty(detallePedidoCol)){
						//se recorren los detalles
						for(DetallePedidoDTO detallePedidoDTO: detallePedidoCol){
							if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								Collection<DetalleEstadoPedidoAutorizacionDTO> detalleEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
								//Iterando detalle autorizaciones del articulo
								for(DetalleEstadoPedidoAutorizacionDTO detalleAct : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
									DetalleEstadoPedidoAutorizacionDTO detalle = SerializationUtils.clone(detalleAct);
									//solo para el caso de descuentos variable
									if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
											detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()
									&& (!detalle.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA.toString()) || eliminarDetalles.equals(Boolean.FALSE))){
										
										//cuando es pedido consolidado se actualizan el ID
										if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null){
											detalle.getId().setCodigoPedido(detallePedidoDTO.getId().getCodigoPedido());
											detalle.getId().setSecuencialEstadoPedido(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido());
											detalle.getId().setCodigoEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado());
										}
										detalleEliminarCol.add(detalle);
										detalle.setEstado(CorporativoConstantes.ESTADO_INACTIVO.toString());
										autorizacionesDesactivarCol.add(detalle);
										LogSISPE.getLog().info("autorizacion a desactivar: {} del pedido: {}",detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial(), detalle.getId().getCodigoPedido());
									}
								}
								//se eliminan las autorizaciones
								if(CollectionUtils.isNotEmpty(detalleEliminarCol) && eliminarDetalles){
									detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(detalleEliminarCol);
									if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()))
										detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
								}
							}
						}
					}
					request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL, autorizacionesDesactivarCol);
//				}
			}
			catch (Exception e) {
				LogSISPE.getLog().error("error al obtener las autorizaciones inactivas {}",e);
			}
		}
		
		
		/**
		 * obtiene las autorizaciones de los detalles del pedido consolido para aplicar esa configuracion a los detalles de cada pedido cuando se cargue el pedido
		 * @param request
		 */
		public static void obtenerDetallesAutorizacionesActuales(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol){
			try{
				//solo para pedidos consolidados
				if(ConsolidarAction.esPedidoConsolidado(request)){
					LogSISPE.getLog().info("ingresa a obtener los detalles consolidados con autorizaciones");
					Collection<DetallePedidoDTO> detallesPedidoConsolidado = null;
					if(CollectionUtils.isNotEmpty(detallePedidoCol)){
						detallesPedidoConsolidado = detallePedidoCol;
					}else{
						 detallesPedidoConsolidado = (Collection<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
					}
					Collection<DetallePedidoDTO> detalleActualConAutorizacionesCol = new ArrayList<DetallePedidoDTO>();
					// se recorren los detalles
					if(CollectionUtils.isNotEmpty(detallesPedidoConsolidado)){
						//se verifica si tiene autorizaciones
						for(DetallePedidoDTO detalleActual : detallesPedidoConsolidado){
							if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								//se guarda el estadoDetallePedidoDTO
								detalleActualConAutorizacionesCol.add(detalleActual); 
								LogSISPE.getLog().info("articulo con autorizacion: "+detalleActual.getArticuloDTO().getDescripcionArticulo());
							}
						}
						request.getSession().setAttribute(CotizarReservarAction.DETALLES_CONSOLIDADOS_ACTUALES, detalleActualConAutorizacionesCol);
					}
					if(CollectionUtils.isNotEmpty(detallePedidoCol)){
						//se asigna la configuracion de las autorizaciones a los detalles
						Collection<DetallePedidoDTO> detallesConsolidados = (Collection<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
						
						ConsolidarAction.asignarAutorizacionesDetallesConsolidados(request, detallesConsolidados);
						request.getSession().setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS, detallesConsolidados);
					}
					else{
						request.getSession().setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS, detallesPedidoConsolidado);
					}
				}
			}
			catch (Exception e) {
				LogSISPE.getLog().error("Error al obtener los detalles consolidados con autorizaciones");
			}
		}
		
		
		/**
		 * Corrige las llaves de los descuentos de acuerdo al formato del opDescuentos
		 * @param request
		 * @param llavesDescuentosAplicados
		 * @return
		 * @throws Exception
		 */
		public static  String[] corregirOpDescuentos(HttpServletRequest request, String[] llavesDescuentosAplicados) throws Exception {

			LogSISPE.getLog().info("Ingresa a corregir las llaves del opDescuentos");
			obtenerCodigoTipoDescuentoPorCajasMayorista(request);
			//se verifica la existencia de una colecci\u00F3n de descuentos
			if(llavesDescuentosAplicados != null && llavesDescuentosAplicados.length > 0){

				//se generan las llaves para setearlas en opDescuentos del formulario
				Collection<TipoDescuentoDTO> tipoDescuento = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);					
				String[] opDescuentos = new String[llavesDescuentosAplicados.length];
				int posVector = 0;
				int posDecuento = 0;
				//se recorren los tipos de descuentos
				for(TipoDescuentoDTO tipoDctoActual : tipoDescuento){
					//se recorren las llaves
					for(String llaveActual : llavesDescuentosAplicados){
						String nuevaLlave = llaveActual;
						//se valida que no lleguen llaves nulas
						if(llaveActual != null && !llaveActual.equals("")){
							String codigoTipoDescuento = llaveActual.split(SEPARADOR_TOKEN)[1].split(CODIGO_TIPO_DESCUENTO)[1];
							if(tipoDctoActual.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento)){
								nuevaLlave =  nuevaLlave.replace(llaveActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, ""+posDecuento+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO);
								opDescuentos[posVector] = nuevaLlave;
								posVector++;
							}
						}
					}
					posDecuento++;
				}
				return opDescuentos;
			}
			return llavesDescuentosAplicados;
		}
		
		
		/**
		 * Validar descuento excluyentes
		 * @param descuentosSeleccionados		  descuentos seleccionados
		 * @param descuentosExcluyentes			  descuentos excluyentes
		 * @param descuentosExcluyentesNombres    lista donde se guarda los descuentos excluyentes seleccionados para mostrar mensaje de error
		 * @return
		 * @throws SISPEException
		 * @author osaransig
		 */
		public static boolean validarExclusionDescuentos (String [] descuentosSeleccionados, String [] descuentosExcluyentes, List<String> descuentosExcluyentesNombres) throws SISPEException {
			
			//codigo tipo descuento
			final String ctd = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento");
			
			int y = 0;
			for (String descuentoExcluyente : descuentosExcluyentes) {
				if (descuentosSeleccionados != null) {
					for (String descuentoSeleccionado : descuentosSeleccionados) {
						
						String codigoTipoDes = descuentoSeleccionado.split("-")[1];
						if (codigoTipoDes.equals(ctd + descuentoExcluyente) && !descuentosExcluyentesNombres.contains(descuentoExcluyente)) {
							descuentosExcluyentesNombres.add(descuentoExcluyente);
							y++;
						}
					}
				}
			}
			
			if (y > 1) {
				return true;
			}
			return false;
		}
		
		
		public static TipoDescuentoDTO getObtenerTipoDescuentoPorID(final String codigoTipoDescuento, HttpServletRequest request) {
			
			Collection<TipoDescuentoDTO> tipoDescuentoCol = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			
			TipoDescuentoDTO tipoDesc = (TipoDescuentoDTO) CollectionUtils.find(tipoDescuentoCol, new Predicate() {
				
				public boolean evaluate(Object arg0) {
					TipoDescuentoDTO tipDesFind = (TipoDescuentoDTO) arg0;
					return tipDesFind.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento);
				}
			});
			
			
			return tipoDesc;
		}
		
		public static String [] getDescuentosExcluyentes(HttpServletRequest request) throws Exception {
			
			ParametroID id = new ParametroID();
			id.setCodigoCompania(ConstantesGenerales.DEFAULT_COMPANY);
			id.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoGeneral"));
			ParametroDTO paramDesGeneral = SISPEFactory.obtenerServicioSispe().transObtenerParametroPorID(id);
			
			id.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo"));
			ParametroDTO paramDesNaviEmpre = SISPEFactory.obtenerServicioSispe().transObtenerParametroPorID(id);
			String navEmpCredito = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito");
//			
//			ParametroDTO parametroDesVar = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
			
			return new String [] {paramDesGeneral.getValorParametro(), paramDesNaviEmpre.getValorParametro(), navEmpCredito};//, parametroDesVar.getValorParametro()};
			
			//se obtienen los codigos de descuentos excluyentes del parametro
//			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipos.descuentos.excluyentes", request);
//			String codigoDescuentoJuguetes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoJuguetes");
//			return parametroDTO.getValorParametro().concat(codigoDescuentoJuguetes).split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
			
		}
		
		
//		/**
//		 * Desactiva las casillas de  porcentajes de las autorizaciones ya aprobadas o solicitadas
//		 * @param request
//		 * @return
//		 */
//		public static void getActivarDesactivarPorcentajeDsctoVariables(HttpServletRequest request, CotizarReservarForm formulario){
//			try{
//				LogSISPE.getLog().info("desactivando los porcentajes con descuento variables solicitados o aprobados");
//				String[] porcentajesActivos = null;
//				Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
//				String[] opDescuentos = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
//				
//				if(CollectionUtils.isNotEmpty(colaAutorizaciones)  && opDescuentos != null && opDescuentos.length > 0 
//						&& formulario.getPorcentajeVarDescuento() != null && formulario.getPorcentajeVarDescuento().length > 0){
//					porcentajesActivos = new String[formulario.getPorcentajeVarDescuento().length];
//					
//					int posVector = 0;
//					//se activan todos los porcentajes
//					for(int i=0; i<opDescuentos.length; i++){
//						//se valida que no lleguen llaves nulas
//						if(opDescuentos[i] != null && !opDescuentos[i].equals("")){
//							if(opDescuentos[i].contains(CODIGO_GERENTE_COMERCIAL)){
//								porcentajesActivos[posVector] = opDescuentos[i]+SEPARADOR_TOKEN+false;
//								posVector ++;
//							}
//						}
//					}
//						
//					//se recorren los descuentos seleccionador para validar los varibles
//					int posVectorOpDscto = 0;
//					posVector = 0;
//					for(String descuentoSeleccionado : opDescuentos){
//						
//						if(descuentoSeleccionado != null && !descuentoSeleccionado.equals("")){
//							//se valida si es un descuento variable
//							if(descuentoSeleccionado.contains(CODIGO_GERENTE_COMERCIAL) && descuentoSeleccionado.split(SEPARADOR_TOKEN).length >= 4){
//								//7-CTD3-CMDMON-COM01-INX0
//								//se busca el descuento asociado a esa llave
//								String llaveActual = descuentoSeleccionado.replace(descuentoSeleccionado.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
//								
//								//se recorren las autorizaciones
//								for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
//									//se verifica la llave y el estado de la autorizacion
//									if(autorizacionActual.getValorReferencial() != null && autorizacionActual.getValorReferencial().contains(llaveActual) 
//											&& !autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
//										//se deshabilita el porcentaje
//										porcentajesActivos[posVector] = opDescuentos[posVectorOpDscto]+SEPARADOR_TOKEN+true;
//										break;
//									}
//								}
//								posVector++;
//							}
//						}
//						posVectorOpDscto ++;
//					}
//				}
//				formulario.setDesactivarCasillasPorcentaje(porcentajesActivos);
//			}catch (Exception e) {
//				LogSISPE.getLog().error("error al desactivar los porcentajes con descuento variables solicitados o aprobados" , e);
//			}
//
//		}
		
		
		/**
		 * Valida si el pedido que esta en sesion es el actual en la BDD.
		 * @param vistaPedidoDTO
		 * @return true si es el pedido actual, false si fue modificado en otra sesion
		 * @throws SISPEException
		 * @throws Exception
		 */
		public static Boolean verificarPedidoActual(VistaPedidoDTO vistaPedidoDTO) throws SISPEException, Exception{
			LogSISPE.getLog().info("se valida que el pedido que se encuentra en sesion sea el pedido actual en la BDD");
			
			Boolean esPedidoActual = Boolean.FALSE;

			if(vistaPedidoDTO != null &&  
			(vistaPedidoDTO.getNpEsNuevaReserva()==null || !vistaPedidoDTO.getNpEsNuevaReserva() &&
					(vistaPedidoDTO.getNpCrearNuevoPedido()==null || !vistaPedidoDTO.getNpCrearNuevoPedido()))){
				
				//se arma la consulta del estado pedido
				EstadoPedidoDTO consultaEstadoPedidoDTO = new EstadoPedidoDTO();
				consultaEstadoPedidoDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
				consultaEstadoPedidoDTO.setFechaFinalEstado(null);
				consultaEstadoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				if(vistaPedidoDTO.getId() != null){
					consultaEstadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
					consultaEstadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
					consultaEstadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
					consultaEstadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
					LogSISPE.getLog().info("consultando el pedido: "+vistaPedidoDTO.getId().getCodigoPedido()+" secuencialEstadoPedido: "+vistaPedidoDTO.getId().getSecuencialEstadoPedido()+
							" llaveContratoPos: "+vistaPedidoDTO.getLlaveContratoPOS());
				}
				//se valida que no haya cambiado el codigoEstadoPagado desde el punto de venta
				if(StringUtils.isNotEmpty(vistaPedidoDTO.getCodigoEstadoPagado())){
					consultaEstadoPedidoDTO.setCodigoEstadoPagado(vistaPedidoDTO.getCodigoEstadoPagado());
				}
				
				//se consulta el estado pedido
				Collection<EstadoPedidoDTO> estadoPedidoDTOCol = SISPEFactory.obtenerServicioSispe().transObtenerEstadoPedido(consultaEstadoPedidoDTO);
				
				//si no existen datos no es el pedido actual
				if(CollectionUtils.isEmpty(estadoPedidoDTOCol)){
					esPedidoActual = Boolean.FALSE;
					LogSISPE.getLog().info("El pedido no es el actual");
				}else if(estadoPedidoDTOCol.size() == 1) {
					esPedidoActual = Boolean.TRUE;
					LogSISPE.getLog().info("pedido actual");
				}
			}else{
				//para cuando viene directamente de la cotizacion, la vista es nula
				esPedidoActual = Boolean.TRUE;
			}
			
			return esPedidoActual;
		}
	
		
	/**
	 * Metodo para verificar si existe un cambio en la reserva por descuento maxinavidad empresarial, devuelve true en caso de que
	 * exista una modificicacion por este descuento caso contrario devuelve false
	 * @param request
	 * @return
	 * @throws SISPEException
	 * @author osaransig
	 */
	public static boolean verificarExisteCambiosDescuentosReserva(HttpServletRequest request) throws SISPEException {
		
		Boolean respuesta = Boolean.FALSE;

		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		Collection<DescuentoEstadoPedidoDTO> descuentoEstadoPedidoCol = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
		
		//descuentos originales del pedido
		Collection<String> colIdDesIniciales = getIdentificadorDescuento(vistaPedidoDTO.getDescuentosEstadosPedidos());
		//descuentos finales del pedido
		Collection<String> colIdDesFinales = getIdentificadorDescuento(descuentoEstadoPedidoCol);
		
		if (colIdDesIniciales.size() != colIdDesFinales.size()) {
			respuesta = Boolean.TRUE;
		} else if (CollectionUtils.subtract(colIdDesIniciales, colIdDesFinales).size() > 0) {
			respuesta = Boolean.TRUE;
		}
		
		return respuesta;
	}
	
	
	private static Collection<String> getIdentificadorDescuento(Collection<DescuentoEstadoPedidoDTO> descuentoEstadoPedidoCol) {
		Collection<String> respuestaCol = new ArrayList<String>();
		final String codTipDes = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento");
		
		if(CollectionUtils.isNotEmpty(descuentoEstadoPedidoCol)){
			for (DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentoEstadoPedidoCol) {
				
				if (descuentoEstadoPedidoDTO != null) {
					if (StringUtils.isEmpty(descuentoEstadoPedidoDTO.getLlaveDescuento())) {
						respuestaCol.add(descuentoEstadoPedidoDTO.getId().getCodigoDescuento());
					} else {

						String regex = codTipDes + "[0-9]+";
						Pattern p = Pattern.compile(regex);
						Matcher m = p.matcher(descuentoEstadoPedidoDTO.getLlaveDescuento());

						if (m.find()) {
							respuestaCol.add(m.group().replace(codTipDes, "").trim());
						}
					}
				}
			}
		}

		return respuestaCol;
	}
	
	/**
	 * Verificar si el articulo es licor. Si el articulo no es canasto y tiene licores se devolvera el valor de 2, en el caso 
	 * de que el articulo sea un licor pero este dentro de una canasta se devolvera 1.
	 * @param request
	 * @param detallePedidoDTO
	 * @return
	 * @throws SISPEException
	 * @throws Exception
	 * @author osaransig
	 */
	public static String verificarArticuloEsLicorPorClasificacion (HttpServletRequest request, DetallePedidoDTO detallePedidoDTO) throws SISPEException, Exception {
		String codigoClasificionPadre = "";
		String codigoClasificion = "";
		String codigoClasificacionLicores = "";
		String respuesta = ConstantesGenerales.PEDIDO_SIN_LICORES;
		
		// Se obtiene el par\u00E1metro de los departamentos que prohibe la ley entregar los dias domingos
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentos.prohibidoEntregarClienteDomingos", request);
		
		if (parametroDTO.getValorParametro() == null) {
			throw new SISPEException(
					"No se pudo obtener parametro: prohibicion de venta de licores los domingos");
		} else {
			codigoClasificacionLicores = parametroDTO.getValorParametro();
		}
		
		codigoClasificionPadre = detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().substring(0, 2);
		codigoClasificion= detallePedidoDTO.getArticuloDTO().getCodigoClasificacion();
		
	
		// Si no es canasto el valor del parametro tiene licores en 2
		if (!esCanasto(codigoClasificion, request)) {
			if(codigoClasificacionLicores.contains(codigoClasificionPadre)){
				respuesta = ConstantesGenerales.PEDIDO_CON_LICORES;
			}
			
		} else if (esCanasto(codigoClasificion, request)) { // Si es canasto y tiene licores el valor de respuesta es 1
			
			for(ArticuloRelacionDTO recetaDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
				
				codigoClasificionPadre = recetaDTO.getArticuloRelacion().getCodigoClasificacion().substring(0, 2);
				//valida si existe un articulo cuya clasificacion sea(13-licores)
				if(codigoClasificacionLicores.contains(codigoClasificionPadre)){
					respuesta = ConstantesGenerales.PEDIDO_CON_LICORES_CANASTOS;
					break;
				}
			}
		}
		
		return respuesta;
	}
	
	
	public static List<Integer> getDiasNoVenderLicores(String diasProhibicion) throws SISPEException {
		
		List<Integer> listRes = new ArrayList<Integer>();
		String [] diasNoLicores = diasProhibicion.split(ConstantesGenerales.CARACTER_SEPARACION);
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));
		
		String [] days = dfs.getShortWeekdays();
		
		String [] daysBus = (String[]) CollectionUtils.collect(Arrays.asList(days), new Transformer() {
			
			public Object transform(Object arg0) {
				String string = (String) arg0;
				return string.replaceAll("\u00E1", "a").replaceAll("\u00E9", "e"); /*quitar tildes*/
			}
		}).toArray(new String[0]);
		
		
		for (String dias : diasNoLicores) {
			for (int i = 0; i < daysBus.length; i++) {
				if (dias.equals(daysBus[i])) {
					listRes.add(i);
				}
			}
		}
		
		return listRes;
	}
	
	public static String getNombreDiasNoVenderLicores(List<Integer> listRes) throws SISPEException {
		
		String nombresDiasNoVenderLicores = "";
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));
		
		String [] days = dfs.getWeekdays();
		
			for (int i = 0; i < listRes.size(); i++) {
				if (StringUtils.isEmpty(nombresDiasNoVenderLicores)) {
					nombresDiasNoVenderLicores=days[listRes.get(i)];
				}else{
					nombresDiasNoVenderLicores=nombresDiasNoVenderLicores+ConstantesGenerales.CARACTER_SEPARACION+days[listRes.get(i)];
				}
			}
		
		return nombresDiasNoVenderLicores;
	}
	
	public static String getValueParameter (String codigoParametro,HttpServletRequest request) throws SISPEException {
		
		ParametroID parametroId = new ParametroID();
		parametroId.setCodigoCompania(DEFAULT_COMPANY);
		parametroId.setCodigoParametro(codigoParametro);
		
		ParametroDTO parametroDTO  = null;
		try {
			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion(codigoParametro,request);
		} catch (Exception e) {
			LogSISPE.getLog().error("error al obtener parametro con el codigo: {}", codigoParametro);
			throw new SISPEException(e);
		}
		
		return parametroDTO.getValorParametro();
		
	}
	
	
	/**
	 * Se verifica si cambio el contacto en una modificacion de reserva.
	 * @param vistaPedidoDTO
	 * @param formulario
	 * @return
	 */
	public static Boolean isCambioContacto(VistaPedidoDTO vistaPedidoDTO,CotizarReservarForm formulario,HttpServletRequest request){
		
		Boolean verificarCambioContacto = Boolean.FALSE;
		
		if(vistaPedidoDTO==null){
			verificarCambioContacto=Boolean.TRUE;
		}
		else {
			//para el caso de personas
			if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
					|| vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
				if((formulario.getRucEmpresa()!=null && !formulario.getRucEmpresa().isEmpty()) || !vistaPedidoDTO.getNumeroDocumentoPersona().equals(formulario.getNumeroDocumento())){ 
					verificarCambioContacto=Boolean.TRUE;
				}
			}
			//para el caso de empresas
			else if((vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || 
					vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL))
					&& !vistaPedidoDTO.getRucEmpresa().equals(formulario.getNumeroDocumento())){
				verificarCambioContacto=Boolean.TRUE;
			}
			//para el caso en que cambiaron la localizacion
			else if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || 
					vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
				String codigoLocalizacionGuardada= vistaPedidoDTO.getCodigoLocalizacion();
				Long codigoLocalizacionSeleccionada= ((LocalizacionDTO)request.getSession().getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP))==null?0L:((LocalizacionDTO)request.getSession().getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP)).getId().getCodigoLocalizacion();
				if(!codigoLocalizacionGuardada.equals(codigoLocalizacionSeleccionada.toString()) && codigoLocalizacionSeleccionada.longValue()!=0){
					verificarCambioContacto=Boolean.TRUE;
				}
			}
		}			
		return verificarCambioContacto;	
	}
	
	
	private static void clasificarTipoDescuentoEnExcluyentes(HttpServletRequest request, Collection<TipoDescuentoDTO> tipoDescuento)  throws SISPEException {
		try{
				
			List<String> codigosDctoExcluyentes = new ArrayList<String>();
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipos.descuentos.excluyentes", request);
			if(parametroDTO.getValorParametro()!=null){
				String codigoDescuentoJuguetes =MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoJuguetes");
				codigosDctoExcluyentes =  Arrays.asList(parametroDTO.getValorParametro().concat(codigoDescuentoJuguetes).split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion")));
			}
			
			Collection<TipoDescuentoDTO> tipoDescuentoDTOEXC=new ArrayList<TipoDescuentoDTO>();
			Collection<TipoDescuentoDTO> tipoDescuentoDTOOTR=new ArrayList<TipoDescuentoDTO>();
			Collection<TipoDescuentoDTO> tipoDescuentoDTOVAR=new ArrayList<TipoDescuentoDTO>();
			
			for (TipoDescuentoDTO tipoDescuentoDTO : tipoDescuento) {					
				if(codigosDctoExcluyentes.contains(tipoDescuentoDTO.getId().getCodigoTipoDescuento())){
					tipoDescuentoDTO.setNpExcluyente(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.excluyentes"));
					tipoDescuentoDTOEXC.add(tipoDescuentoDTO);
				} else if(tipoDescuentoDTO.getNpTipoDescuentoVariable() == null && !codigosDctoExcluyentes.contains(tipoDescuentoDTO.getId().getCodigoTipoDescuento())) {
					tipoDescuentoDTO.setNpExcluyente(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.otros"));
					tipoDescuentoDTOOTR.add(tipoDescuentoDTO);
				} else{
					tipoDescuentoDTO.setNpExcluyente(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.variables"));
					tipoDescuentoDTOVAR.add(tipoDescuentoDTO);
				}
			}
			//ordeno los descuentos segun se muestran
			tipoDescuento.clear();
			tipoDescuento.addAll(tipoDescuentoDTOEXC);
			tipoDescuento.addAll(tipoDescuentoDTOOTR);
			tipoDescuento.addAll(tipoDescuentoDTOVAR);
			
		} catch (Exception e) {
			throw new SISPEException(e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param valor
	 * @return
	 */
	public static Map<String, Double>  crearMapParaRedondearValores(Double valor){
		 Map<String, Double> map = new HashMap<String, Double>();
		 map.put("PI", valor);
		 map.put("VI", 0D);
		 return map;
	}

	/**
	 * M&eacute;todo para validar si la reserva se encuentra bloqueada en el POS
	 * @param vistaPedidoDTO
	 * @return <code>TRUE</code> si el pedido esta bloqueado en el POS, <code>FALSE</code> si se puede reservar
	 * @throws SISPEException
	 * @throws Exception
	 */
	public static Boolean reservaBloqueadaPOS(VistaPedidoDTO vistaPedidoDTO) throws SISPEException, Exception{
		
		Boolean bandera = Boolean.FALSE;
		
		if(vistaPedidoDTO!=null){
		
			//obtemos el ultimo estadoPedido de la BD
			EstadoPedidoBloqueoDTO estadoPedidoBloqueoDTO = new EstadoPedidoBloqueoDTO();
			estadoPedidoBloqueoDTO.setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			estadoPedidoBloqueoDTO.setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
			estadoPedidoBloqueoDTO.setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			estadoPedidoBloqueoDTO.setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			estadoPedidoBloqueoDTO.setHabilitarCambiosPos(ConstantesGenerales.ESTADO_ACTIVO);
			estadoPedidoBloqueoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			
			EstadoPedidoBloqueoDTO estadoPedidoBloqueoActual = SISPEFactory.getDataService().findUnique(estadoPedidoBloqueoDTO);
			
			if(estadoPedidoBloqueoActual !=null){
				bandera = Boolean.TRUE;
			}
		}
		
		return bandera;
	}
	
	/**
	 * Pasa los valores de un ActionMessages a un String
	 * @param message
	 * @return
	 */
	public static String obtenerValorDeAcctionMessages(ActionMessages message){
		String contenidoActMesg = "";
		
		if(message != null){
			Iterator<ActionMessage> iterator = message.get();				
			
			//se recorre para buscar el key el mensaje
			while(iterator.hasNext()){
				ActionMessage messageActual = iterator.next();
				Object[] datos = messageActual.getValues();
				for(Object datoActual : datos){
					contenidoActMesg +=  (String) datoActual;
				}
			}		
		}
		return contenidoActMesg;
	}
	
	/**
	 * Sacar un respaldo del vectorCantidad, vectorPeso, vectorPesoActual para que en el metodo actualizarDetalleForm realice los procesos correctamente
	 * @param detallePedido
	 * @param formulario
	 * @param request
	 * @param accion
	 * @param estadoActivo
	 * @param estadoInactivo
	 * @throws Exception
	 */
	public static void respaldoIndicesCantidadesPesos(
			ArrayList<DetallePedidoDTO> detallePedido,
			CotizarReservarForm formulario, final HttpServletRequest request,
			String accion, String estadoActivo, String estadoInactivo)
			throws Exception {
		
		String[] vectorCantidadAux = formulario.getVectorCantidad();
		String[] vectorCantidad = new String[ detallePedido.size()];
		String[] vectorPesoActualAux = formulario.getVectorPesoActual();
		String[] vectorPesoActual = new String[ detallePedido.size()];
		String[] vectorPesoAux = formulario.getVectorPeso();
		String[] vectorPeso = new String[ detallePedido.size()];
		int pos = 0;
		int posVectoPeso = 0;
		//Obtengo las clasificaciones permitidas desde la tabla parametro
		String clasificacionesArticulosCambioPesos= CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request);
		for(DetallePedidoDTO detPedDTO : detallePedido){
			vectorCantidad[pos] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
			vectorPeso[pos] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado());
			
			//esta condici\u00F3n es para el caso de art\u00EDculos de peso variable que son pavos y no a cambiado la cantidad en el formulario
			if(detPedDTO.getArticuloDTO().getCodigoClasificacion()!=null && clasificacionesArticulosCambioPesos != null &&
					clasificacionesArticulosCambioPesos.contains(detPedDTO.getArticuloDTO().getCodigoClasificacion()) && 
					detPedDTO.getArticuloDTO().getTipoCalculoPrecio().equals(CotizarReservarForm.TIPO_ARTICULO_PAVO)){
				//se verifica si existe peso registrado por el local
				if(detPedDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal() != null && detPedDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() != 0){
					detPedDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal()));
					vectorPesoActual[posVectoPeso] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal());
				}else{
					vectorPesoActual[posVectoPeso] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado());
				}
				posVectoPeso++;	
			}
			
			pos++;
		}
		formulario.setVectorCantidad(vectorCantidad);
		formulario.setVectorPeso(vectorPeso);
		formulario.setVectorPesoActual(vectorPesoActual);
		formulario.actualizarDetalleForm(request, new ActionErrors(), accion, estadoActivo, estadoInactivo,detallePedido);	
		formulario.setVectorCantidad(vectorCantidadAux);
		formulario.setVectorPeso(vectorPesoAux);
		formulario.setVectorPesoActual(vectorPesoActualAux);
	}
	
	/**
	 * 
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaEntregaDespachada(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Pedido con art\u00EDculos despachados");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);		
		popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'aceptarPopUpDespacho=ok'});ocultarModal();");		
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		popUp.setContenidoVentana("servicioCliente/modificarReserva/popUpEntregaDespachada.jsp");			
		popUp.setAncho(45D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);			
		popUp = null;
	}
	
	/**
	 * Metodo para gestionar el alcance temporal desde el MAX
	 * @param articulosCol	Coleccion de articulos
	 * @param request		request
	 * @throws Exception
	 */
	private static void gestionarAlcanceMax(Collection<ArticuloDTO> articulosCol, HttpServletRequest request) throws Exception{
		try{
			
			LogSISPE.getLog().info("validando si se debe gestionar alcances desde el MAX");
			ArticuloVO articuloVO = new ArticuloVO();
//			articuloVO.setCodigoLocalAlcance(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
			articuloVO.setCodigoLocalAlcance(Integer.parseInt(AutorizacionesUtil.asignarAreaTrabajoAutorizacion(request)));
			String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			
			if(StringUtils.isNotEmpty(accionActual)){
				
				if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))){
					articuloVO.setAccessItemId(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigo.accessItemId.reservacion"));				
				}else if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
					articuloVO.setAccessItemId(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigo.accessItemId.modificarReservacion"));
				}
			}
			
			articuloVO.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
			articuloVO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			articuloVO.setUsuarioAlcance(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			articuloVO.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			
			Collection<String> codigosArticulos = new ArrayList<String>();
			
			if(CollectionUtils.isNotEmpty(articulosCol)){
				
				//se recorren los articulos
				for(ArticuloDTO articuloDTO : articulosCol){
					
					//si es una canasta
					if(CollectionUtils.isNotEmpty(articuloDTO.getArticuloRelacionCol()) 
							&& articuloDTO.getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
						
						//se recorre la receta del canasto para verificar los detalles sin alcance
						for(ArticuloRelacionDTO articuloRelacionDTO : articuloDTO.getArticuloRelacionCol()){
							
							//se verifica si esta en 2 = solicitar alacance temporal
							if(articuloRelacionDTO.getArticuloRelacion().getNpAlcance() != null 
									&& articuloRelacionDTO.getArticuloRelacion().getNpAlcance().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claveAlcanceTemporal"))){
								
								if(articuloVO.getFechaFinAlcance() == null){
									articuloVO.setFechaFinAlcance(obtenerFechaFinAlcanceTemporal(request));
								}
								
								codigosArticulos.add(articuloRelacionDTO.getArticuloRelacion().getId().getCodigoArticulo());
								LogSISPE.getLog().info("articulo de canasto "+articuloRelacionDTO.getArticuloRelacion().getId().getCodigoArticulo()+" agregado para resolver alcance.");
							}
						}
					}
					
					//se agrega el articulo para se gestionado
					if(articuloDTO.getNpAlcance() != null 
							&& articuloDTO.getNpAlcance().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claveAlcanceTemporal"))){
						
						if(articuloVO.getFechaFinAlcance() == null){
							articuloVO.setFechaFinAlcance(obtenerFechaFinAlcanceTemporal(request));
						}
						
						codigosArticulos.add(articuloDTO.getId().getCodigoArticulo());
						LogSISPE.getLog().info("articulo: "+articuloDTO.getId().getCodigoArticulo()+" agregado para resolver alcance.");
					}
				}
			}
			articuloVO.setCodigosArticulos(codigosArticulos);
			
			//si existen detalles sin alcance, se gestiona el alcance
			if(CollectionUtils.isNotEmpty(articuloVO.getCodigosArticulos())){
				SessionManagerSISPE.getServicioClienteServicio().transGestionarAlcanceDesdeMax(articuloVO);
				
				////consultar el parametro que indica de donde se consulta el alcance de articulos
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.estado.consultar.alcance", request);
				//0-SIC, 1-MAX
				int estadoConsultarAlcance = Integer.parseInt(parametroDTO.getValorParametro());
				
				//control para enviar la trama de alcances temporales al SIC solo cuando el SIC sea quien da el alcance temporal, 
				//de lo contrario el MAX enviara la trama al SIC en el metodo de gestionar los alcances
				if (estadoConsultarAlcance == Integer.parseInt(ConstantesGenerales.ESTADO_ACTIVO)){
					
					//se asigna a todos los articulos sin alcance 1
					if(CollectionUtils.isNotEmpty(articulosCol)){
						
						//se recorren los articulos
						for(ArticuloDTO articuloDTO : articulosCol){
							
							//si es una canasta
							if(CollectionUtils.isNotEmpty(articuloDTO.getArticuloRelacionCol()) 
									&& articuloDTO.getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
								
								//se recorre la receta del canasto para verificar los detalles sin alcance
								for(ArticuloRelacionDTO articuloRelacionDTO : articuloDTO.getArticuloRelacionCol()){
									
									//se verifica si esta en 2 = solicitar alacance temporal
									if(articuloRelacionDTO.getArticuloRelacion().getNpAlcance() != null 
											&& articuloRelacionDTO.getArticuloRelacion().getNpAlcance().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claveAlcanceTemporal"))){
										
										articuloRelacionDTO.getArticuloRelacion().setNpAlcance(ConstantesGenerales.ESTADO_ACTIVO);
									}
								}
							}
							
							if(articuloDTO.getNpAlcance() != null 
									&& articuloDTO.getNpAlcance().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claveAlcanceTemporal"))){
								
								articuloDTO.setNpAlcance(ConstantesGenerales.ESTADO_ACTIVO);
							}
						}
					}
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().error("Error al setear los valores en ArticuloVO para gestionar el alcance desde el MAX. "+e.getMessage());
		}
	}
	
	
	/**
	 * Cuando existe cambio de precios y los detalles fueron guardados con precios de CAJA/MAYORISTA, se asignan a los articulos dichos precios
	 * para que conserven/apliquen los descuentos.
	 * @param request
	 * @param vistaPedidoDTO
	 * @throws Exception
	 * @author bgudino
	 */
	public static void asignarPreciosCajaMayoristaOriginales(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO) throws Exception{

		//se obtiene la sesion
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		//se sube a sesion el parametro para que durante todo el for se consulte solo una vez, al terminar el for se quita de sesion
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
		session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
		
		//para el caso que ingresa desde la pantalla de consolidacion
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (List<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			if(CollectionUtils.isNotEmpty(colDetallePedido) &&  CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				//se recorre los detalles del vistaPedidoDTO
				for(DetallePedidoDTO detPedConsolidado : detallePedidoConsolidado){
					
					//se compara el detalle de la vistaPedidoDTO con los detalles de pedido de sesion 
					for(DetallePedidoDTO detalleActual : colDetallePedido){
						
						//se compara el detalle de la vistaPedidoDTO con los detalles de pedido de sesion 
						if(detPedConsolidado.getId().getCodigoAreaTrabajo().intValue() == detalleActual.getId().getCodigoAreaTrabajo().intValue()
								&& detPedConsolidado.getId().getCodigoArticulo().equals(detalleActual.getId().getCodigoArticulo())
								&& detPedConsolidado.getId().getCodigoCompania().intValue() == detalleActual.getId().getCodigoCompania().intValue()
								&& detPedConsolidado.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(detalleActual.getEstadoDetallePedidoDTO().getId().getCodigoEstado())
								&& detPedConsolidado.getId().getCodigoPedido().equals(detalleActual.getId().getCodigoPedido())
								&& detPedConsolidado.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido().equals(detalleActual.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido())){
							
							asignarPreciosCajaMayoristaOriginales(request, detalleActual, null, detPedConsolidado);
							break;
						}
					}
				}
			}
	
		}else{
			if(CollectionUtils.isNotEmpty(colDetallePedido) &&  CollectionUtils.isNotEmpty(vistaPedidoDTO.getVistaDetallesPedidosReporte())){
				
				//se recorre los detalles del vistaPedidoDTO
				for(VistaDetallePedidoDTO vistaDetallePedidoDTO : (ArrayList<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
					
					//se compara el detalle de la vistaPedidoDTO con los detalles de pedido de sesion 
					for(DetallePedidoDTO detalleActual : colDetallePedido){
						
						//se compara el detalle de la vistaPedidoDTO con los detalles de pedido de sesion 
						if(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo().intValue() == detalleActual.getId().getCodigoAreaTrabajo().intValue()
								&& vistaDetallePedidoDTO.getId().getCodigoArticulo().equals(detalleActual.getId().getCodigoArticulo())
								&& vistaDetallePedidoDTO.getId().getCodigoCompania().intValue() == detalleActual.getId().getCodigoCompania().intValue()
								&& vistaDetallePedidoDTO.getId().getCodigoEstado().equals(detalleActual.getEstadoDetallePedidoDTO().getId().getCodigoEstado())
								&& vistaDetallePedidoDTO.getId().getCodigoPedido().equals(detalleActual.getId().getCodigoPedido())
								&& vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido().equals(detalleActual.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido())){
							
							asignarPreciosCajaMayoristaOriginales(request, detalleActual, vistaDetallePedidoDTO, null);
							break;
						}
					}
				}
			}

			//para el caso de pedidos consolidados
			if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null ){
				Collection<DetallePedidoDTO> colDetallePedidoConsolidado = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				
				if(CollectionUtils.isNotEmpty(colDetallePedidoConsolidado) && CollectionUtils.isNotEmpty(vistaPedidoDTO.getVistaDetallesPedidosReporte())){
					
					//se recorre los detalles del vistaPedidoDTO
					for(VistaDetallePedidoDTO vistaDetallePedidoDTO : (ArrayList<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
						
						//se compara el detalle de la vistaPedidoDTO con los detalles de pedido de sesion 
						for(DetallePedidoDTO detalleActual : colDetallePedidoConsolidado){
							
							//se busca el detalle pedido
							if(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo().intValue() == detalleActual.getId().getCodigoAreaTrabajo().intValue()
									&& vistaDetallePedidoDTO.getId().getCodigoArticulo().equals(detalleActual.getId().getCodigoArticulo())
									&& vistaDetallePedidoDTO.getId().getCodigoCompania().intValue() == detalleActual.getId().getCodigoCompania().intValue()){
								
								asignarPreciosCajaMayoristaOriginales(request, detalleActual, vistaDetallePedidoDTO, null);
							}
						}
					}
				}
			}
		}

		//se elimina de sesion el parametro
		session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
	}

	/**
	 * asignan a los articulos los propiedades para habiliar su precio caja/mayorista.
	 * @param request
	 * @param vistaDetallePedidoDTO
	 * @throws Exception
	 * @author bgudino
	 */
	public static void asignarPreciosCajaMayoristaOriginales(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO,
			VistaDetallePedidoDTO vistaDetallePedidoDTO, DetallePedidoDTO detallePedidoConsolidadoOriginal) throws Exception{
		
		Boolean actualizarCalculoPrecio = Boolean.FALSE;
			
		if(detallePedidoDTO != null){
			LogSISPE.getLog().info("aplicando los precios originales de caja y mayorista");
			
			String estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			
			Collection<ArticuloPrecioDTO> articuloPrecioCol = detallePedidoDTO.getArticuloDTO().getArticuloPrecioCol();
				
			//se recorre los articulosPrecio del detalle encontrado
			for(ArticuloPrecioDTO articuloPrecioDTO : articuloPrecioCol){
				
				LogSISPE.getLog().info("Tipo Precio: "+articuloPrecioDTO.getId().getCodigoTipoPrecio());
				//se verifica si el detalle fue guardado con precio mayorista activo
				if (articuloPrecioDTO.getId().getCodigoTipoPrecio().equals(SICArticuloConstantes.TIPO_PRECIO_MAYORISTA)
						&& (detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioMayorista() != null 
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioMayorista().equals(ConstantesGenerales.ESTADO_ACTIVO))){
					
					//se asignas los datos necesarios para que el detalle conserve el precio MAYORISTA
					actualizarCalculoPrecio = Boolean.TRUE;
					//se activa el estado del precio
					articuloPrecioDTO.setEstadoPrecio(estadoActivo);
					
					articuloPrecioDTO.setValorActual(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayorista());
					if(vistaDetallePedidoDTO != null){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getCantidadMinimaMayoreo());
					}else if(detallePedidoConsolidadoOriginal != null){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(detallePedidoConsolidadoOriginal.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado());
					}
					
					if(articuloPrecioDTO.getNpArticuloLocalPrecio() == null){
						
						articuloPrecioDTO.setNpArticuloLocalPrecio(new ArticuloLocalPrecioDTO());
						articuloPrecioDTO.getNpArticuloLocalPrecio().setEstadoPrecio(SICConstantes.ESTADO_ACTIVO_NUMERICO);
						articuloPrecioDTO.getNpArticuloLocalPrecio().setArticuloPrecio(articuloPrecioDTO);
						articuloPrecioDTO.getNpArticuloLocalPrecio().getId().setCodigoTipoPrecio(SICArticuloConstantes.TIPO_PRECIO_MAYORISTA);
					}
					
				}
				//se verifica si el detalle fue guardado con precio CAJA activo
				else if(articuloPrecioDTO.getId().getCodigoTipoPrecio().equals(SICArticuloConstantes.TIPO_PRECIO_CAJA)
						&& (detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioCaja() != null 
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaPrecioCaja().equals(ConstantesGenerales.ESTADO_ACTIVO))){
					
					//se asignas los datos necesarios para que el detalle conserve el precio CAJA
					actualizarCalculoPrecio = Boolean.TRUE;
					//se activa el estado del precio
					articuloPrecioDTO.setEstadoPrecio(estadoActivo);
					
					articuloPrecioDTO.setValorActual(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado());
					
					if(articuloPrecioDTO.getNpArticuloLocalPrecio() == null){
						
						articuloPrecioDTO.setNpArticuloLocalPrecio(new ArticuloLocalPrecioDTO());
						articuloPrecioDTO.getNpArticuloLocalPrecio().setEstadoPrecio(SICConstantes.ESTADO_ACTIVO_NUMERICO);
						articuloPrecioDTO.getNpArticuloLocalPrecio().setArticuloPrecio(articuloPrecioDTO);
						articuloPrecioDTO.getNpArticuloLocalPrecio().getId().setCodigoTipoPrecio(SICArticuloConstantes.TIPO_PRECIO_CAJA);
					}
				}
			}
		}
		if(actualizarCalculoPrecio){
			//se vuelve a calcular los valores del detalle con los cambios
			calcularValoresDetalle(detallePedidoDTO, request, false, false);
		}
	}
	
	
	/**
	 * Se comparan las dos recetas de canastos y se verifica si existe cambio en el estado de los precios CAJA/MAYORISTA
	 * para mostrar la alerta de cambio de precios
	 * @param recetaArticuloActual
	 * @param recetaArticuloEstado
	 * @return true si existe cambio en el estado de los precios CAJA/MAYORISTA entre los detalles de la receta, false caso contrario
	 * @author bgudino
	 */
	private static Boolean verificarCambioPreciosCajaMayoristaEnReceta(Collection<ArticuloRelacionDTO> recetaArticuloActual, 
			Collection<ArticuloRelacionDTO> recetaArticuloEstado){
		
		Boolean existeCambioPrecioEnReceta = Boolean.FALSE;
	
		LogSISPE.getLog().info("verificando el precio caja o mayorista de los detalles de la canasta ha cambiado");
		if(CollectionUtils.isNotEmpty(recetaArticuloActual) && CollectionUtils.isNotEmpty(recetaArticuloEstado)){
			
			//se recorre la receta actual
			for(ArticuloRelacionDTO articuloActual : recetaArticuloActual){
				
				LogSISPE.getLog().info("detalle actual: "+articuloActual.getArticuloRelacion().getDescripcionArticulo());
				
				//se recorre la receta guardada en el estadoPedido para comparar
				//for(ArticuloRelacionDTO articuloEstado : recetaArticuloEstado){
					
				if(CollectionUtils.isNotEmpty(recetaArticuloEstado)){
						ArticuloRelacionDTO articuloEstado=recetaArticuloEstado.iterator().next();
					
					//se verifica si es el mismo articulo
					if(articuloActual.getId().getCodigoArticulo().equals(articuloEstado.getId().getCodigoArticulo())
							&& articuloActual.getId().getCodigoArticuloRelacionado().equals(articuloEstado.getId().getCodigoArticuloRelacionado())
							&& articuloActual.getId().getCodigoCompania().equals(articuloEstado.getId().getCodigoCompania())){
						
						LogSISPE.getLog().info("detalle estado: "+articuloEstado.getArticuloRelacion().getDescripcionArticulo());
						
						if(articuloActual.getArticuloRelacion().getHabilitadoPrecioCaja() != articuloEstado.getArticuloRelacion().getHabilitadoPrecioCaja()){
							return Boolean.TRUE;
						}
						if(articuloActual.getArticuloRelacion().getHabilitadoPrecioMayoreo() != articuloEstado.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
							return Boolean.TRUE;
						}	
					}
				}
			}
		}
		return existeCambioPrecioEnReceta;
	}
	
	public static void instanciarPopUpMailEntregasSICMER(HttpServletRequest request,Boolean modificacionReserva){			
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Alerta entregas con SICMER");
		popUp.setMensajeVentana("La reservaci\u00F3n tiene entregas a domicilio desde el local, se han creado los convenios " +
				"de entrega en <strong>SICMER</strong>. " +
				"Un mail ser\u00E1 enviado al usuario responsable para que genere la <strong>orden de salida</strong>.");
		popUp.setFormaBotones(UtilPopUp.OK);
		if(modificacionReserva){
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'imprimirConvenios=ok', evalScripts:true});ocultarModal();");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'imprimirConvenios=ok', evalScripts:true});ocultarModal();");
		}else{
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', evalScripts:true});ocultarModal();");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', evalScripts:true});ocultarModal();");
		}
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//se guarda la advertencia
		request.setAttribute(SessionManagerSISPE.POPUP, popUp);		
		session.removeAttribute(CotizarReservarAction.EXISTEN_ENTREGAS_SICMER);
		popUp = null;
	}
	
	public static void instanciarPopUpNotificacionBloqueoReservaSICMER(HttpServletRequest request, String numeroReserva,String accion,String accionOK)throws Exception{
		
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		
		popUp.setTituloVentana("Reserva con entrega pendiente");
		if(numeroReserva!=null){
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append("<strong>").append(numeroReserva).append("</strong>")
				.append(" no se puede "+accion+",").toString());	
				
		}else{
			popUp.setPreguntaVentana(new StringBuilder().append("El pedido").append(" no se puede "+accion+",").toString());	
				
		}
		popUp.setPreguntaVentana(popUp.getPreguntaVentana().concat(" debido a que se ha generado una orden de salida en SICMER y la mercader\u00EDa se encuentra en proceso de entrega."));
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK(accionOK);
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
		
	}
	
		
	
	/**
	 * POPUP QUE ALERTA CUANDO SE VA A BORRAR UN ARTICULO QUE TIENE ENTREGAS A DOMICILIO DESDE EL CD DESPACHADAS
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author bgudino
	 */
	public static void instanciarPopUpEliminarDetalleEntregasDespachadas(HttpServletRequest request, String action)throws Exception{
		
		HttpSession session = request.getSession();
		String parametersOk = "eliminarArticulos=ok&siEliminarDetalle=ok";
		if(action.equals("detalleCanasta.do")){
			parametersOk = "siEliminarDetalle=ok";
		}
		LogSISPE.getLog().info("Entro a instanciar el popUp instanciarPopUpEliminarDetalleEntregasDespachadas");
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Informaci\u00F3n");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+action+"',['pregunta','div_pagina','mensajes','seccion_detalle'], {parameters: '"+parametersOk+"', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+action+"', ['pregunta','div_pagina','mensajes','seccion_detalle'], {parameters: 'cancelarEliminarDetalleDespachado=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/modificarReserva/popUpEliminarDetalleEntregasDespachadas.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	
	/**
	 * si el articulo es un canasto especial, se valida que toda la receta tenga una temporada activa
	 * @param articuloDTO
	 * @return
	 * @throws Exception 
	 */
	public static boolean canastoConRecetaSinTemporadaActiva(ArticuloDTO articuloDTO, ActionMessages infos) throws Exception{
		
		boolean canastoSintemporada = false;
		
		if(articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
				//|| articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))){
			
			if(CollectionUtils.isNotEmpty(articuloDTO.getArticuloRelacionCol())){
				LogSISPE.getLog().info("el articulo: "+articuloDTO.getDescripcionArticulo()+" es un canasto");
				
				ArticuloRelacionDTO articuloRelacionConsultaDTO = new ArticuloRelacionDTO();
				articuloRelacionConsultaDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
				articuloRelacionConsultaDTO.getId().setCodigoArticuloRelacionado(null);
				articuloRelacionConsultaDTO.getId().setCodigoCompania(articuloDTO.getId().getCodigoCompania());
				articuloRelacionConsultaDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				
				int cantidadItems = SISPEFactory.getDataService().findCount(articuloRelacionConsultaDTO).intValue();
				LogSISPE.getLog().info("cantidad de items del articulo actual: "+articuloDTO.getArticuloRelacionCol().size());
				LogSISPE.getLog().info("cantidad de items de la receta del canasto: "+cantidadItems);
				
				 if(cantidadItems > articuloDTO.getArticuloRelacionCol().size()){
					 LogSISPE.getLog().info("El canasto tiene "+(cantidadItems - articuloDTO.getArticuloRelacionCol().size())+" items con la temporada inactiva");
					 canastoSintemporada = true;
				  }
			}else{
				LogSISPE.getLog().info("El canasto no tiene una receta con temporada activa");

				canastoSintemporada = true;
				
				//se consulta el codigo de barras del CANASTO DE COTIZACIONES
				ParametroDTO parametroConsultaDTO = new ParametroDTO();
				parametroConsultaDTO.getId().setCodigoCompania(DEFAULT_COMPANY);
				parametroConsultaDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCanastoCotizacionesVacio"));
				parametroConsultaDTO.setGrupoSistema(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoSistema"));
				
				ParametroDTO parametroDTO = SISPEFactory.getDataService().findUnique(parametroConsultaDTO);
				
				if(parametroDTO != null && StringUtils.isNotEmpty(parametroDTO.getValorParametro())
						&& articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras().equals(parametroDTO.getValorParametro())){
					canastoSintemporada = false;
					LogSISPE.getLog().info("El articulo es del tipo CANASTO DE COTIZACIONES, no se valida la receta");
				}
			}
		}
		if(canastoSintemporada){
			infos.add("errorContruirArticulo", new ActionMessage("error.canasto.sin.temporada",articuloDTO.getDescripcionArticulo()));
		}
		return canastoSintemporada;
	}
	
	
	/**
	 * Obtiene la fecha final del alcance temporal de la siguiente forma:
	 * Consulta la temporada activa del SISPE, si la fecha actual esta dentro de la temporada se toma la fecha final de la temporada
	 * como la fecha final del alcance temporal.
	 * Si la fecha actual esta fuera de una temporada activa, se aumenta a la fecha actual el numero de meses del parametro 159
	 * y esa es la fecha final
	 * @param request
	 * @return fechaFinal del alcance temporal, ya sea la fecha final de la temporada activa o la fecha actual mas los meses del parametro 159
	 * @throws Exception
	 */
	private static final Date obtenerFechaFinAlcanceTemporal(HttpServletRequest request) throws Exception{
		
		LogSISPE.getLog().info("obteniendo la fecha final del alcance temporal");
		Date fechaActual = new Date();
		Date fechaFinal = null;
		
		TemporadaDTO temporadaDTO = new TemporadaDTO();
		temporadaDTO.getId().setCodigoCompania(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCompania"));
		temporadaDTO.setEstadoTemporada(ConstantesGenerales.ESTADO_ACTIVO);
		temporadaDTO = SISPEFactory.getDataService().findUnique(temporadaDTO);
		
		if(temporadaDTO != null && temporadaDTO.getFechaInicialTemporada() != null && temporadaDTO.getFechaFinalTemporada() != null){
			
			LogSISPE.getLog().info("fecha inicial: "+temporadaDTO.getFechaInicialTemporadaS()+" fecha final: "+temporadaDTO.getFechaFinalTemporadaS());
			//si la fecha actual es mayor o igual al inicio de temporada
			if((fechaActual.equals(temporadaDTO.getFechaInicialTemporada()) ||  fechaActual.after(temporadaDTO.getFechaInicialTemporada()) ) 
					&& (fechaActual.equals(temporadaDTO.getFechaFinalTemporada()) || fechaActual.before(temporadaDTO.getFechaFinalTemporada()))){
				
				LogSISPE.getLog().info("la fecha actual esta en la temporada del SISPE");
				fechaFinal = temporadaDTO.getFechaFinalTemporada();
				
			}else{
				
				//esta fuera de temporada
				LogSISPE.getLog().info("la fecha actual esta en fuera de la temporada del SISPE");
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.meses.alcance.temporal", request);
				
				if(StringUtils.isNotEmpty(parametroDTO.getValorParametro())){
					
					Calendar calendarFechaFin = Calendar.getInstance();
					calendarFechaFin.add(Calendar.MONTH, Integer.parseInt(parametroDTO.getValorParametro()));
					fechaFinal = calendarFechaFin.getTime();
				}
			}
		}else{
			LogSISPE.getLog().error("No se pudo encontrar una temporada activa");
		}
		
		LogSISPE.getLog().info("la fecha final del alcance temporal es :"+fechaFinal);
		return fechaFinal;
	}
				
	
}