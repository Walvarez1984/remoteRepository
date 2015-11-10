package ec.com.smx.sic.sispe.web.session;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_CON_IVA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.com.smx.corporativo.admparamgeneral.dto.CompaniaDTO;
import ec.com.smx.corporativo.admparamgeneral.servicio.AdmParametrosGeneralS;
import ec.com.smx.corporativo.admparamgeneral.servicio.AutorizacionesS;
import ec.com.smx.corporativo.commons.factory.CorporativoFactory;
import ec.com.smx.framework.resources.FrameworkMessages;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.session.SessionManagerBase;
import ec.com.smx.framework.session.SessionManagerSsoConfig;
import ec.com.smx.framework.web.util.Constantes;
import ec.com.smx.mensajeria.commons.factory.MensajeriaFactory;
import ec.com.smx.mensajeria.servicio.MensajeriaS;
import ec.com.smx.sic.cliente.common.SICConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TemporadaDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.servicio.ServicioClienteServicio;

/**
 * @author 	bmontesdeoca
 * @author 	fmunoz
 * @author 	mbraganza
 * @version 3.0
 * @since 	JSDK 5.0
 */
public class SessionManagerSISPE extends SessionManagerBase
{
	//nombres para algunas variables de sesi\u00F3n generales
	public static final String PREFIJO_VARIABLE_SESION = "ec.com.smx.sic.sispe.";
	public static final String DATOS_TABLA_ORDENAR = "ec.com.smx.sic.sispe.ordenamiento.datosTabla";
	public static final String DATOS_COLUMNA_ORDENADA = "ec.com.smx.sic.sispe.ordenamiento.datosColumna";
	public static final String COLECCION_PEDIDOS_GENERAL = "ec.com.smx.sic.sispe.pedidos";
	public static final String CANTIDAD_TOTAL_PEDIDOS = "ec.com.smx.sic.sispe.numero.pedidos";
	public static final String CONSULTA_VISTAPEDIDO = "ec.com.smx.sic.sispe.pedidos.busqueda";
	public static final String CONSULTA_VISTAARTICULO = "ec.com.smx.sic.sispe.articulos.busqueda";
	public static final String VISTA_PEDIDO = "ec.com.smx.sic.sispe.vistaPedidoDTO";
	public static final String VISTA_PEDIDO_EMPRESAS = "ec.com.smx.sic.sispe.empresas.vistaPedidoCol";
	public static final String VISTA_PEDIDO_AUX = "ec.com.smx.sic.sispe.vistaPedidoDTOAUX";
	public static final String ACCION_ACTUAL = "ec.com.smx.sic.sispe.accion";
	public static final String DETALLE_PEDIDO_ORIGINAL = "ec.com.smx.sic.sispe.detallePedidoOriginal";
	public static final String MENSAJES_SISPE	= "ec.com.smx.sic.sispe.mensajes";
	
	public static final String BOTON_BENEFICIARIO ="ec.com.sic.sispe.boton.benenficiario";
	// OANDINO: Variable que controla el tipo de accion a utilizar conjuntamente con seccionBusqueda.JSP
	public static final String ACCION_SECCION_BUSQUEDA = "ec.com.smx.sic.sispe.accion.seccion.busqueda.reportes";
	// ---------------------------------------------------------------------------------------------
	public static final String LISTADO_DATOS_ADMINISTRACION = "ec.com.smx.sic.sispe.tabla";
	public static final String TIPO_FECHA_BUSQUEDA = "ec.com.smx.sic.sispe.tipoFechaBusqueda";
	public static final String MOSTRAR_METODO_AUTORIZACION_2 = "ec.com.smx.sic.sispe.autorizacion.metodo2";
	private static final String COD_EXCEPCION_REGISTRAR_NUEVAMENTE = "ec.com.smx.sic.sispe.codigoExcepcion.registrarNuevamente";
	private static final String BEANSESSION = "ec.com.smx.sic.sispe.beanSession";
	public static final String POPUP = "ec.com.smx.sic.sispe.popUpConfirmacion";
	public static final String POPUPAUX = "ec.com.smx.sic.sispe.popUpConfirmacionAux";
	public static final String COL_ESTADOS = "ec.com.smx.sic.sispe.estados";
	public static final String PAGINA_TAB = "ec.com.smx.sic.sispe.paginaTab";
	public static final String COL_VISTA_ARTICULO = "ec.com.smx.sic.sispe.colVistaArticuloDTO";
	public static final String INDICE_PEDIDO_VISTA_ARTICULO = "ec.com.smx.sic.sispe.vistaArticulo.indiceGlobalPedido";
	public static final String SECUENCIA_PREFIJOS_MAIL_ESTABLECIMIENTO = "ec.com.smx.sic.sispe.parametro.prefijoMailTipoEstablecimiento";
	public static final String COL_ESTRUCTURA_EMAIL = PREFIJO_VARIABLE_SESION.concat("colMailMensajeEST");
	public static final String COL_ARCHIVO_BENE = "ec.com.smx.sic.sispe.archivo.beneficiario";
	public static final String COL_ARCHIVO_FOTO = "ec.com.smx.sic.sispe.archivoEntrega.foto";
	public static final String DEF_ARCHVO_AUX = "ec.com.smx.sic.sispe.archivoEntrega.fotoAux";
	public static final String TOTAL_COL_ARCHIVO_FOTO = "ec.com.smx.sic.sispe.totalArchivoEntrega.foto";
	public static final String PEDIDO_TOTAL_COL_ARCHIVO_FOTO = "ec.com.smx.sic.sispe.pedido.totalArchivoEntrega.foto";
	public static final String HA_SUBIDO_FOTOS = "ec.com.smx.sic.sispe.bandera.fotos";
	public static final String ARCHIVO_DTO = "ec.com.smx.sic.sispe.archivo.beneficiarioDTO";
	//variables que establecen la ruta de las plantillas para el men\u00FA
	private static final String PLANTILLA = "/ec/com/smx/sic/sispe/web/resources/menuH.xsl";
	private static final String PLANTILLA_PANEL = "/ec/com/smx/sic/sispe/web/resources/menuPanel.xsl";
	private static final String ID_PANEL = FrameworkMessages.getString("framework.menuID.panel");
	
	//los siguientes nombres de variables no deben llevar el prefijo "ec.com.smx" porque deben permanecer activas mientras se este usando la aplicaci\u00F3n
	private static final String CORP_ADM_PARAM_GENERAL_NAME_SESSION_ATTRIBUTE = "corporativo.admParamatrosGeneralesService";
	private static final String CURRENT_ENTIDAD_RESPONSABLE = "sispe.vistaEntidadResponsableDTO";
	private static final String CURRENT_TEMPORADA_ACTIVA="sispe.temporadaActiva";
	public static final String COLECCION_LOCALES = "sispe.vistaEstablecimientoCiudadLocalDTO";
	private static final String VALOR_IVA = "sispe.valor.iva";
	public static final String ESTADO_ACTIVO = "sispe.estado.activo";
	public static final String ESTADO_INACTIVO = "sispe.estado.inactivo";
	private static final String CODIGO_CANASTO_VACIO = "sispe.codigo.canastoVacio";
	private static final String LIMITE_TIEMPO_VALIDEZ_RESERVA = "sispe.codigo.limite.validez.reserva";
	public static final String TITULO_VENTANA = "WebSISPE.tituloVentana";
	public static final String PREFIJO_IPVA = "IPVA";
	//variable para controlar la accion del estado check
	public static final String ESTADO_CHECK = "ec.com.smx.sic.sispe.accion.check";
	private static SessionManagerSISPE defInstance =new SessionManagerSISPE();
	//variable para controlar la plantilla seteada de busqueda.
	public static final String PLANTILLA_BUSQUEDA_GENERAL1 = "plantilla.busquedaGeneralReporte";
	//variable para controlar la plantilla seteada de busqueda linea Produccion.
	public static final String PLANTILLA_BUSQUEDA = "plantilla.busquedaGeneralLinPro";
	public static final String BODEGA_DTO = "ec.com.smx.sic.sispe.administracion.BodegaDTO";
	public static final String ESTADO_ACTUAL = "sispe.estado.actual";

	
	public static SessionManagerSISPE getDefault(){
		return defInstance;
	}
	/***********************************************/
	public String getDefaultSystemId() {
		return MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID");
	}
	
	public Integer getDefaultCompanyId() {
		return Integer.valueOf(MessagesWebSISPE. getString("security.CURRENT_COMPANY_ID"));
	}

	public Locale getDefaultLocale(){
		return new Locale("es","EC");
	}
	
	public String getTokenParameterName() {
		return MessagesWebSISPE.getString("security.tokenparameter");
	}

	/*********************************************/
	

	/**
	 * Obtiene una instancia de la clase <code>ServicioClienteServicio</code> para acceder a los m\u00E9todos de la capa
	 * de servicio ubicados en el proyecto de la aplicaci\u00F3n.
	 * 
	 * @param request										La petici\u00F3n que se est\u00E1 procesando
	 * @return servicioClienteServicio	Una instancia de la clase que proporciona los m\u00E9todos en la capa de servicio
	 * 																	de la aplicaci\u00F3n
	 * @throws Exception	
	 */
	public final static ServicioClienteServicio getServicioClienteServicio() throws Exception{
		return (ServicioClienteServicio)getSISPEBean("servicioClienteTrans");
	}

	/**
	 * Permite obtener la Entidad Responsable que se encuentra conectada, y la guarda en la variable de sesi\u00F3n CURRENT_ENTIDAD_RESPONSABLE.
	 * 
	 * @param request 						Petici\u00F3n que se est\u00E1 procesando
	 * @return vistaEntidadResponsableDTO		Objeto que contiene los datos de la entidad responsable que est\u00E1 logeada 
	 * 										en el sistema
	 * @throws Exception
	 */
	public static VistaEntidadResponsableDTO getCurrentEntidadResponsable(HttpServletRequest request){
		VistaEntidadResponsableDTO vistaEntidadResponsableDTO =(VistaEntidadResponsableDTO)request.getSession().getAttribute(CURRENT_ENTIDAD_RESPONSABLE);
		return vistaEntidadResponsableDTO;
	}
	
	/**
	 * Obtiene el c\u00F3digo del local de la entidad responsable actual.
	 * 
	 * @param request	La petici\u00F3n que se est\u00E1 procesando
	 * @return el c\u00F3digo del local de la entidad responsable actual
	 */
	public static Integer getCurrentLocal(HttpServletRequest request) throws Exception{
		VistaEntidadResponsableDTO entidadResponsableActual = getCurrentEntidadResponsable(request);
		
		return entidadResponsableActual.getCodigoLocal();
	}

	/**
	 * Guarda en la sesi\u00F3n los datos de la Entidad Responsable. Guarda en la variable de sesi\u00F3n CURRENT_ENTIDAD_RESPONSABLE.
	 * 
	 * @param request 						La petici\u00F3n que se est\u00E1 procesando
	 * @param vistaEntidadResponsableDTO		Objeto que contiene los datos de la entidad responsable que est\u00E1 logeada 
	 * 										en el sistema
	 * @throws Exception
	 */
	public static void setCurrentEntidadResponsable(HttpServletRequest request,VistaEntidadResponsableDTO vistaEntidadResponsableDTO){
		request.getSession().setAttribute(CURRENT_ENTIDAD_RESPONSABLE,vistaEntidadResponsableDTO);
	}

	/**
	 * Permite obtener la Temporada activa, y la guarda en la variable de sesi\u00F3n CURRENT_TEMPORADA_ACTIVA.
	 * 
	 * @param request 		Petici\u00F3n que se est\u00E1 procesando
	 * @return temporadaDTO	El objeto que contiene los datos de la temporada actual	
	 * @throws Exception
	 */
	public static TemporadaDTO getCurrentTemporadaActiva(){
		TemporadaDTO temporadaDTO = new TemporadaDTO();
		temporadaDTO.setEstadoTemporada(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
		try {
			temporadaDTO = SISPEFactory.getDataService().findUnique(temporadaDTO);
			if(temporadaDTO == null){
				LogSISPE.getLog().error("no se pudo obtener la temporada actual");
			}
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		return temporadaDTO;
	}

	/**
	 * Guarda en la sesi\u00F3n los datos de la Temporada activa. Guarda en la variable de sesi\u00F3n CURRENT_TEMPORADA_ACTIVA.
	 * 
	 * @param request 		Petici\u00F3n que se est\u00E1 procesando
	 * @param temporadaDTO	El objeto que contiene los datos de la temporada actual
	 * @throws Exception
	 */
	public static void setCurrentTemporadaActiva(HttpServletRequest request,TemporadaDTO temporadaDTO){
		request.getSession().setAttribute(CURRENT_TEMPORADA_ACTIVA,temporadaDTO);
	}

	/**
	 * Permite obtener el valor del iva de la sesi\u00F3n, si este es <code>null</code> 
	 * se toma directamente de la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static String getValorIVA(HttpServletRequest request){
		String valorIVA =(String)request.getSession().getAttribute(VALOR_IVA);
		if(valorIVA==null){
			valorIVA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.porcentajeIVA");
			request.getSession().setAttribute(VALOR_IVA,valorIVA);
		}
		return valorIVA;
	}

	/**
	 * Permite asignar el valor del estado activo de la sesi\u00F3n desde la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static void setValorIVA(HttpServletRequest request){
		request.getSession().setAttribute(VALOR_IVA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.porcentajeIVA"));
	}

	/**
	 * Permite obtener el valor del estado activo de la sesi\u00F3n, si este es <code>null</code> 
	 * se toma directamente de la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static String getEstadoActivo(HttpServletRequest request){
		String estadoActivo =(String)request.getSession().getAttribute(ESTADO_ACTIVO);
		if(estadoActivo==null){
			estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			request.getSession().setAttribute(ESTADO_ACTIVO,estadoActivo);
		}
		return estadoActivo;
	}

	/**
	 * Permite asignar el valor del estado activo de la sesi\u00F3n desde la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static void setEstadoActivo(HttpServletRequest request) throws Exception{
		request.getSession().setAttribute(ESTADO_ACTIVO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
	}

	/**
	 * Permite obtener el valor del estado inactivo de la sesi\u00F3n, si este es <code>null</code> 
	 * se toma directamente de la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static String getEstadoInactivo(HttpServletRequest request){
		String estadoInactivo =(String)request.getSession().getAttribute(ESTADO_INACTIVO);
		if(estadoInactivo==null){
			estadoInactivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
			request.getSession().setAttribute(ESTADO_INACTIVO,estadoInactivo);
		}
		return estadoInactivo;
	}

	/**
	 * Permite asignar el valor del estado inactivo de la sesi\u00F3n desde la clase <code>MessagesAplicacionSISPE</code>
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static void setEstadoInactivo(HttpServletRequest request) throws Exception{
		HttpSession session=request.getSession();
		session.setAttribute(ESTADO_INACTIVO,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
	}

	/**
	 * Permite obtener el valor del estado que indica si los valores mostrados a nivel Web est\u00E1n con IVA o sin IVA
	 * 
	 * @param request 	Petici\u00F3n que se est\u00E1 procesando
	 * @throws Exception
	 */
	public static String getEstadoPreciosIVA(HttpServletRequest request){
		HttpSession session=request.getSession();
		String estadoPreciosIVA =(String)session.getAttribute(CALCULO_PRECIOS_CON_IVA);
		if(estadoPreciosIVA==null){
			estadoPreciosIVA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			session.setAttribute(CALCULO_PRECIOS_CON_IVA, estadoPreciosIVA);
		}
		return estadoPreciosIVA;
	}
	
	/**
	 * Guarda en la sesi\u00F3n una variable que guarda un c\u00F3digo de excepci\u00F3n espec\u00EDfico, 
	 * para indicar que una transacci\u00F3n debe ser registrada nuevamente por problemas iniciales.
	 * 
	 * @param request 														La petici\u00F3n que se est\u00E1 procesando
	 * @param codigoExcepcionRegistrarNuevamente	String que contiene el c\u00F3digo de la excepci\u00F3n que indica que 
	 * 																						una transacci\u00F3n debe ser registrada nuevamente
	 * @throws Exception
	 */
	public static void setCodExceptionRegistrarNuevamente(HttpServletRequest request,String codExcepcionRegistrarNuevamente){
		HttpSession session=request.getSession();
		session.setAttribute(COD_EXCEPCION_REGISTRAR_NUEVAMENTE,codExcepcionRegistrarNuevamente);
	}

	/**
	 * Obtiene c\u00F3digo de error que produce la base de datos, y la guarda en la variable de 
	 * sesi\u00F3n COD_EXCEPCION_REGISTRAR_NUEVAMENTE.
	 * 
	 * @param request 													Petici\u00F3n que se est\u00E1 procesando
	 * @return codExcepcionRegistrarNuevamente	String que contiene el c\u00F3digo de la excepci\u00F3n que indica que 
	 * 																					una transacci\u00F3n debe ser registrada nuevamente
	 */
	public static String getCodExceptionRegistrarNuevamente(HttpServletRequest request) {
		HttpSession session=request.getSession();

		String codExcepcionRegistrarNuevamente =(String)session.getAttribute(COD_EXCEPCION_REGISTRAR_NUEVAMENTE);
		return codExcepcionRegistrarNuevamente;
	}

	/**
	 * Crea el objeto </code>BeanSession<code> y lo almacena en sesi\u00F3n y que ser\u00E1 utilizado en las acciones donde se necesite
	 * @param beanSession		- Objeto <code>BeanSession</code>
	 * @param request				- Petici\u00F3n HTTP actual
	 */
	public static void setBeanSession(BeanSession beanSession, HttpServletRequest request){
		request.getSession().setAttribute(BEANSESSION, beanSession);
	}
	
	/**
	 * Obtiene el objeto </code>BeanSession<code> de la sesi\u00F3n el mismo que ser\u00E1 utilizado en las acciones donde se necesite
	 * @param request		- Petici\u00F3n HTTP actual
	 * @return 					- objeto </code>BeanSession<code>
	 */
	public static BeanSession getBeanSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		BeanSession beanSession = (BeanSession)session.getAttribute(BEANSESSION);
		if(beanSession == null){
			beanSession = new BeanSession();
			session.setAttribute(BEANSESSION, beanSession);
		}
		return beanSession;
	}
	
	/**
	 * Obtiene el c\u00F3digo del canasto vacio
	 * @param request		- Petici\u00F3n HTTP actual
	 * @return 					- <code>String</code> que contiene el c\u00F3digo
	 */
	public static String getCodigoCanastoVacio(HttpServletRequest request)throws Exception{
		String codigoCanastoVacio = (String)request.getSession().getAttribute(CODIGO_CANASTO_VACIO);
		if(codigoCanastoVacio == null){
			//se obtiene el par\u00E1metro
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoCanastoCotizacionesVacio", request);
			String codigoBarrasCanastoVacio = parametroDTO.getValorParametro();
			
			//OBTENER EL CODIGO DEL ARTICULO CANASTO COTIZACIONES VACIO
			ArticuloBitacoraCodigoBarrasDTO artBitCodBarDTO= new ArticuloBitacoraCodigoBarrasDTO();
			artBitCodBarDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			artBitCodBarDTO.getId().setCodigoBarras(codigoBarrasCanastoVacio);
			artBitCodBarDTO.setEstadoArticuloBitacora(SICConstantes.getInstancia().ESTADO_ACTIVO_NUMERICO);
			artBitCodBarDTO= SISPEFactory.getDataService().findUnique(artBitCodBarDTO);
			
			LogSISPE.getLog().info("artBitCodBarDTO.getId().getCodigoArticulo() {}", artBitCodBarDTO.getId().getCodigoArticulo());
			
			request.getSession().setAttribute(CODIGO_CANASTO_VACIO, artBitCodBarDTO.getId().getCodigoArticulo());
			codigoCanastoVacio=artBitCodBarDTO.getId().getCodigoArticulo();
		}
		//condici\u00F3n para la funci\u00F3n no retorne un valor nulo
		if(codigoCanastoVacio  == null){
			return "";
		}
		return codigoCanastoVacio;
	}
	
	/**
	 * Obtiene el tiempo de validez de una reserva
	 * @param request		- Petici\u00F3n HTTP actual
	 * @return 					- <code>String</code> que contiene el c\u00F3digo
	 */
	public static String getObtenerLimiteTiempoValidezReserva(HttpServletRequest request)throws Exception{
		String codigoLimiteValidez = (String)request.getSession().getAttribute(LIMITE_TIEMPO_VALIDEZ_RESERVA);
		if(codigoLimiteValidez == null){
			//se obtiene el par\u00E1metro
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.limiteTiempoConfirmarReserva", request);
			codigoLimiteValidez = parametroDTO.getValorParametro();
			request.getSession().setAttribute(LIMITE_TIEMPO_VALIDEZ_RESERVA, codigoLimiteValidez);
		}
		//condici\u00F3n para la funci\u00F3n no retorne un valor nulo
		if(codigoLimiteValidez  == null){
			return "";
		}
		return codigoLimiteValidez;
	}
	
	/**
	 * Guarda en la sesi\u00F3n el c\u00F3digo del local, esto es necesario para realizar las consultas desde la bodega
	 * 
	 * @param request 			- La petici\u00F3n que se est\u00E1 procesando
	 * @param codigoLocal		- String que contiene el c\u00F3digo del local que seleccion\u00F3 el usuario de bodega
	 */
	public static void setCodigoLocalObjetivo(HttpServletRequest request,Integer codigoLocal){
		request.getSession().setAttribute(CODIGO_LOCAL_REFERENCIA,codigoLocal);
	}

	/**
	 * Obtiene de la sesi\u00F3n el c\u00F3digo del local, esto es necesario para realizar las consultas desde la bodega
	 * 
	 * @param request 			-	La petici\u00F3n que se est\u00E1 procesando
	 * @return codigoLocal	- String que contiene el c\u00F3digo del local que se obtubo de sesi\u00F3n
	 */
	public static Integer getCodigoLocalObjetivo(HttpServletRequest request)
	{
		Integer codigoLocal =(Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA);
		if(codigoLocal==null){
			LogSISPE.getLog().info("NO SE OBTUBO UN CODIGO DE LOCAL");
		}
		return codigoLocal;
	}
	
	/**
	 * Guarda en la sesi\u00F3n la colecci\u00F3n de locales y ciudades, esto es necesario para realizar las consultas 
	 * desde la bodega
	 * 
	 * @param request 			La petici\u00F3n que se est\u00E1 procesando
	 */
	public static void setColeccionVistaEstablecimientoCiudadLocalDTO(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = new VistaEstablecimientoCiudadLocalDTO();
		try{
			vistaEstablecimientoCiudadLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			vistaEstablecimientoCiudadLocalDTO.setNpObtenerDesdeVistaLocal(SessionManagerSISPE.getEstadoActivo(request));
			vistaEstablecimientoCiudadLocalDTO.setVistaLocales(new ArrayList());
			LogSISPE.getLog().info("se realiza la consulta de locales");
			//colecci\u00F3n que almacenar\u00E1 los locales existentes
			Collection colVistaEstablecimientoCiudadLocalDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaEstablecimientoCiudadLocalSector(vistaEstablecimientoCiudadLocalDTO);
			session.setAttribute(COLECCION_LOCALES,colVistaEstablecimientoCiudadLocalDTO);
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
	}

	/**
	 * Obtiene de la sesi\u00F3n la colecci\u00F3n de locales y ciudades, esto es necesario para realizar las consultas 
	 * desde la bodega
	 * 
	 * @param 	request 			La petici\u00F3n que se est\u00E1 procesando
	 * @return 	vistaLocales	La colecci\u00F3n que contiene los locales y ciudades
	 * @throws 	Exception
	 */
	public static Collection getColeccionVistaEstablecimientoCiudadLocalDTO(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		Collection vistaLocales =(Collection)session.getAttribute(COLECCION_LOCALES);
		if(vistaLocales==null){
			LogSISPE.getLog().info("no se obtubo una colecci\u00F3n de locales");
			setColeccionVistaEstablecimientoCiudadLocalDTO(request);
			vistaLocales =(Collection)session.getAttribute(COLECCION_LOCALES);
		}
		return vistaLocales;
	}
	
	/**
	 * Permite obtener una instancia para el bean de Mensajer\u00EDa
	 * @param 	request
	 * @return	mensajeriaS
	 * @throws 	Exception
	 */
	public static MensajeriaS getMensajeria() throws Exception {
		return (MensajeriaS) MensajeriaFactory.getBean("mensajeriaTrans");
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param company
	 * @throws Exception
	 */
	public static void setCurrentCompanys(HttpServletRequest request,CompaniaDTO company)
	throws Exception {
		HttpSession vosession = request.getSession(true);
		vosession.setAttribute(Constantes.CURRENT_COMPANY, company);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static CompaniaDTO getCurrentCompanys(HttpServletRequest request)throws Exception{
		try{
			HttpSession vosession = request.getSession(false);
			CompaniaDTO companiaDTO = (CompaniaDTO) vosession.getAttribute(Constantes.CURRENT_COMPANY);
			if(companiaDTO == null) {
				String companyId = MessagesWebSISPE.getString("security.CURRENT_COMPANY_ID");
				companiaDTO=getCorpAdmParametrosGeneralesService(request).findCompaniaById(new Integer(companyId));
				vosession.setAttribute(Constantes.CURRENT_COMPANY, companiaDTO);
			}
			return companiaDTO;

		}catch(Exception ex) {
			throw ex;
		}
	}

	/**
	 * Permite obtener una instancia de un bean especificado
	 * @param bean			Descripci\u00F3n del objeto del cual se desea obtener una instancia	
	 * @return objetoBean		Objeto instanciado		
	 */
	private static Object getSISPEBean(String bean){
		Object objetoBean = null;
		try{
			objetoBean=SISPEFactory.getBean(bean);
		}
		catch(Exception ex){	
			LogSISPE.getLog().info("No se ha podido intanciar {}", objetoBean);
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return objetoBean;
	}

	/**
	 * Elimina todas las variables de sesi\u00F3n que comiencen con el prefijo [ec.com.smx]
	 * espec\u00EDficado en el archivo de recursos.
	 * 
	 * @param request		La petici\u00F3n que estamos procesando
	 */
	public static void removeVarSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("se eliminan las variables de sesi\u00F3n");
		String prefix = MessagesWebSISPE.getString("prefijo.variables.session");
		//se itera la enumeraci\u00F3n con las variables de sesi\u00F3n
		for (Enumeration enume = session.getAttributeNames();enume.hasMoreElements();){
			String varSession = enume.nextElement().toString();
			//si la variable de sesi\u00F3n comienza con el prefijo establecido se elimina
			if(varSession.startsWith(prefix))
				session.removeAttribute(varSession);
		}
	}

	/**
	 * Elimina todas los formularios de sesi\u00F3n que terminen con el sufijo [Form]
	 * espec\u00EDficado en el archivo de recursos.
	 * 
	 * @param request		La petici\u00F3n que estamos procesando
	 */
	public static void removeFormSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		String sufix = MessagesWebSISPE.getString("sufijo.formularios.session");
		//se itera la enumeraci\u00F3n con las variables de sesi\u00F3n
		for (Enumeration enume = session.getAttributeNames();enume.hasMoreElements();){
			String varSession = enume.nextElement().toString();
			//si la variable de sesi\u00F3n comienza con el prefijo establecido se elimina
			if(varSession.endsWith(sufix))
				session.removeAttribute(varSession);
		}
	}

	/**
	 * Funci\u00F3n que borra todas las variables de sessi\u00F3n
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void removeAllVarSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		for (Enumeration<String> enume = session.getAttributeNames();enume.hasMoreElements();){
			session.removeAttribute(enume.nextElement());
		}
	}
	
	/**
	 * M&eacute;todo para el encapsulaci\u00F3n de los m&eacute;todos de la capa de servicios de Corporativo. Variable de sesi\u00F3n CORP_ADM_PARAM_GENERAL_NAME_SESSION_ATTRIBUTE.
	 * 
	 * @param request Petici\u00F3n.
	 * @return admparametrosgeneralService con el acceso a los m&eacute;todos de la capa de servicios.
	 * @throws Exception
	 */
	public final static AdmParametrosGeneralS getCorpAdmParametrosGeneralesService(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		AdmParametrosGeneralS admParametrosGeneralS = (AdmParametrosGeneralS) session.getAttribute(CORP_ADM_PARAM_GENERAL_NAME_SESSION_ATTRIBUTE);
		if(admParametrosGeneralS  == null) {
			//se obtiene el bean para la administraci\u00F3n de par\u00E1metros en el sistema corporativo
			admParametrosGeneralS = (AdmParametrosGeneralS)CorporativoFactory.getBean("corporativoAdmparametrosgeneralTrans");
			session.setAttribute(CORP_ADM_PARAM_GENERAL_NAME_SESSION_ATTRIBUTE,admParametrosGeneralS );
			LogSISPE.getLog().info("Administracion de parametros generales recuperado {}",admParametrosGeneralS .toString());
		} 
		return admParametrosGeneralS;
	}

	/**
	 * M&eacute;todo para el encapsulaci\u00F3n de los m&eacute;todos de la capa de servicios de Corporativo. Variable de sesi\u00F3n CORP_ADM_AUTORIZACIONES_NAME_SESSION_ATTRIBUTE.
	 * 
	 * @param request Petici\u00F3n.
	 * @return admparametrosgeneralService con el acceso a los m&eacute;todos de la capa de servicios.
	 * @throws Exception
	 */
	public final static AutorizacionesS getCorpAutorizacionesServicio() throws Exception {
		//se obtiene el bean para la administraci\u00F3n de par\u00E1metros en el sistema corporativo
		return (AutorizacionesS)CorporativoFactory.getBean("corporativoAutorizacionServiceTrans");
	}
	
	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#afterLogin(javax.servlet.http.HttpServletRequest, ec.com.smx.framework.security.dto.UserDto)
	 */
	@Override
	protected void afterLogin(HttpServletRequest arg0, UserDto arg1) {
		// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getAdminRoleIds()
	 */
	@Override
	protected String getAdminRoleIds() {
		// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
		return null;
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getClasspathReferenceClass()
	 */
	@Override
	protected Class getClasspathReferenceClass() {
		return this.getClass();
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getDefaultContext()
	 */
	@Override
	public String getDefaultContext() {
		return "struts";
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getMulticompanyEnabled()
	 */
	@Override
	public boolean getMulticompanyEnabled() {
		return false;
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getNombreVariableSesion(java.lang.String, java.lang.String)
	 */
	@Override
	protected String getNombreVariableSesion(String accessItemId, String template) {
		if(template.equals(PLANTILLA)){
			LogSISPE.getLog().info("CON PLANTILLA:"+accessItemId);
			return accessItemId;
		}
		LogSISPE.getLog().info("IDPANEL:"+ID_PANEL);
		LogSISPE.getLog().info("ID_PANEL_FRAMEWORK:"+FrameworkMessages.getString("framework.menuID.panel"));
		LogSISPE.getLog().info("ID_PANEL_SISPE:"+MessagesWebSISPE.getString("security.SISPE_MENU_ID_PANEL"));
		LogSISPE.getLog().info("SIN PLANTILLA:"+accessItemId+ ID_PANEL);
		MessagesWebSISPE.getString("security.SISPE_MENU_ID_PANEL");
		return accessItemId+ MessagesWebSISPE.getString("security.SISPE_MENU_ID_PANEL");
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getRequiredAccessItemSetIds()
	 */
	@Override
	public String[] getRequiredAccessItemSetIds() {
		String [] result={"SISPE","SISPE_H"};
		return result;
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#getTemplatesForAccessItemSetId(java.lang.String)
	 */
	@Override
	protected String[] getTemplatesForAccessItemSetId(String arg0) {
		if(arg0.equals("SISPE")){
			String [] result={PLANTILLA,PLANTILLA_PANEL};
			return result;
		}
		return new String[0];
	}

	/* (sin Javadoc)
	 * @see ec.com.smx.framework.session.SessionManagerBase#setUpSsoConfig()
	 */
	@Override
	protected SessionManagerSsoConfig setUpSsoConfig() {
		return null;
	}

}
