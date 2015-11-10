/*
 * AutorizacionesUtil.java
 * Creado el 01/07/2008 18:04:26
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_MI_LOCAL;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jdom.Document;

import com.google.gson.Gson;

import ec.com.kruger.common.xml.fo.ConfiguracionPaginaFo;
import ec.com.kruger.common.xml.fo.ImpresoraFo;
import ec.com.kruger.common.xml.fo.impl.ImpresoraFoImpl;
import ec.com.kruger.utilitario.dao.commons.annotations.RelationField.JoinType;
import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.kruger.workflow.commons.factory.WorkflowFactory;
import ec.com.kruger.workflow.dto.DataKeyValueDTO;
import ec.com.kruger.workflow.dto.WorkItemDTO;
import ec.com.smx.autorizaciones.common.factory.AutorizacionesFactory;
import ec.com.smx.autorizaciones.dto.TipoAutorizadorUsuarioDTO;
import ec.com.smx.autorizaciones.integracion.dto.Autorizacion;
import ec.com.smx.autorizaciones.integracion.dto.AutorizacionDataKey;
import ec.com.smx.autorizaciones.integracion.dto.AutorizacionEstado;
import ec.com.smx.autorizaciones.integracion.dto.ValorComponente;
import ec.com.smx.corporativo.admparamgeneral.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.AutorizadoDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.AutorizadorDTO;
import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.FuncionarioDTO;
import ec.com.smx.corpv2.dto.FuncionarioPerfilDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.corpv2.dto.id.AreaTrabajoID;
import ec.com.smx.framework.common.enumeration.TipoEmpresaEnum;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.TransformerUtil;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.common.validator.Validator;
import ec.com.smx.framework.common.validator.ValidatorImpl;
import ec.com.smx.framework.exception.FrameworkException;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.jsf.common.parser.SystemConnectionSerializer;
import ec.com.smx.framework.jsf.common.parser.URLSystemConnection;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.frameworkv2.multicompany.dto.UserCompanySystemDto;
import ec.com.smx.frameworkv2.security.dto.ParameterComponentDto;
import ec.com.smx.frameworkv2.security.dto.ProfileDto;
import ec.com.smx.frameworkv2.util.dto.DataFileDto;
import ec.com.smx.frameworkv2.util.dto.DynamicComponentValueDto;
import ec.com.smx.frameworkv2.util.dto.FileInformationDto;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.CompradorDTO;
import ec.com.smx.sic.cliente.mdl.dto.FuncionarioTipoMarcaDTO;
import ec.com.smx.sic.cliente.mdl.dto.LineaComercialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.LineaComercialDTO;
import ec.com.smx.sic.cliente.mdl.dto.LineaComercialFuncionarioDTO;
import ec.com.smx.sic.cliente.mdl.dto.LineaComercialFuncionarioProcesoDTO;
import ec.com.smx.sic.cliente.mdl.dto.LineaComercialFuncionarioTipoMarcaDTO;
import ec.com.smx.sic.cliente.mdl.dto.MarcaArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.AutorizacionEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionStockDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.AnulacionesAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ConsolidarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.AnulacionesForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author fmunoz, bgudino
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class AutorizacionesUtil{
	
	private static final AutorizacionesUtil INSTANCIA = new AutorizacionesUtil();
	
	public static AutorizacionesUtil getInstancia(){
		return INSTANCIA;
	}
	
	public static final String AUTORIZACION_COL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "autorizacionCol";
	public static final String AUTORIZACION_STOCK_PAVPOLCAN = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "autorizacionPavPolCan";
	public static final String COLA_AUTORIZACIONES = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "estadoPedidoAutorizacionDTOCol";
	public static final String COLA_AUTORIZACIONES_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "cola.autorizaciones.stock";
//	public static final String COLA_AUTORIZACIONES_STOCK_RESPALDO = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "cola.autorizaciones.stock.respaldo";
	public static final String AUTORIZACIONES_CONSULTA = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "consultaPedidoAutorizacionDTOCol";
	public static final String AUTORIZACIONES_ACTUALIZAR = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "actualizarPedidoAutorizacionDTOCol";
	public static final String ACCION_ANTERIOR_AUTORIZACION = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "requestAccionAutorizacion";	
//	public static final String AUTORIZACION_GERENTE_COMERCIAL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "soloGerenteComercial";
	public static final String AUTORIZACIONES_DESACTIVAR_COL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "desactivarAutorizacionPedidoCol"; 
	public static final String AUTORIZACIONES_ACTIVAR_COL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "activarAutorizacionPedidoCol"; 
	public static final String AUTORIZACIONES_PEDIDO_COL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "sistema.autorizaciones.Col"; //guarda las autorizaciones a nivel del estado-pedido
	
	//Variables para descuento
	public static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
	public static final  String SEPARADOR_COMA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
	public static final  String SEPARADOR_DOS_PUNTOS = ":";
	private static final  String INDICE_DESCUENTO_GENERAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.descuento.indice.descuentos.general"); //INX
	public static final  String CODIGO_GERENTE_COMERCIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoGerenteComercial"); //COM
//	private static final  String CODIGO_ADMINISTRADOR_LOCAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoAdministradorLocal"); //ADM
	public  static final  String RUTA_ARCHIVO_AUTORIZACION = "ec.com.smx.sic.sispe.archivo.parametros.autorizaciones"; 
	public  static final  String ALTO_POPUP_AUTORIZACIONES = "ec.com.smx.sic.sispe.alto.popup.autorizacion"; 
	public  static final  String TIENE_AUTORIZACION_STOCK_CANASTOS = "ec.com.smx.sic.sispe.pedido.con.autorizacion.stock.canastos"; //true, false, NULL
	public  static final  String TIENE_AUTORIZACION_STOCK_PAVOS = "ec.com.smx.sic.sispe.pedido.con.autorizacion.stock.pavos";// true, false, NULL
	public  static final  String TIENE_AUTORIZACIONES_PENDIENTES_STOCK = "ec.com.smx.sic.sispe.pedido.con.autorizacion.pendiente.stock";// true si existen autorizaciones por aprobar, false caso contrario
	public  static final  String ID_USUARIO_GERENTE_COMERCIAL_CANASTOS = "ec.com.smx.sic.sispe.id.usuario.gerente.comercial.canastos";// KFLOW100
	public  static final  String ID_USUARIO_GERENTE_COMERCIAL_PAVOS = "ec.com.smx.sic.sispe.id.usuario.gerente.comercial.pavos";// FRM1630
	
	//Autorizaciones  para finalizar el workflow desde el gestor
	public  static final  String AUTORIZACIONES_FINALIZAR_WORKFLOW = "ec.com.smx.sic.sispe.autorizaciones.finalizar.workflow";//Collection<String>
	
	//variable para saber si solicito autorizaciones de descuento variable
	public  static final  String SOLICITO_AUTORIZACION_DESCUENTO_VARIABLE = "ec.com.smx.sic.sispe.solicito.autorizacion.dscto.variable";//true, null
	//coleccion de String con los codigos de las autoizaciones hijas por finalizar
	public static final String AUTORIZACIONES_HIJAS_POR_FINALIZAR  = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "autorizaciones.hijas.por.finalizar";
	//es una bandera que indica si se debe o no mostrar el popUp de autorizaciones de stock
	public static final String MOSTRAR_POPUP_AUTORIZACION_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "mostrar.popup.autStock";
	public static final String CODIGO_TIPO_AUTORIZACION = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "codigo.tipo.autorizacion";
	private static final String CODIGO_TIPO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento"); //CTD
	
	//coleccion con las clasificaciones pertenecientes a DEPARTAMENTOS
	public static final String COL_CLASIFICACIONES_DEPARTAMENTO = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "clasificaciones.departamentos";
	
	/**
	 *codigos de las clasificaciones que necesitan autorizacion de stock de un gerente comercial
	 */
	public static final String CLASIFICACIONES_NECESITAN_AUTORIZACION_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "clasificaciones.necesitan.autorizacion.stock";
//	public static final String COL_CLASIFICACIONES_DEPARTAMENTO_AUTORIZADOR = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "clasificaciones.departamento.comprador";
	public static final String CODIGO_PROCESO_AUTORIZAR_DESC_VARIABLE = SessionManagerSISPE.PREFIJO_VARIABLE_SESION+"codigo.proceso.autorizar.desc.var";
	public static final String CODIGO_PROCESO_AUTORIZAR_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION+"codigo.proceso.autorizar.stock";
	//vector String[] con los autorizadores y las lineas comerciales que tienen que enviarse copia de la autorizacion de descuento variable
	public static final String CODIGO_PROCESO_COPIA_AUTORIZAR_DESC_VARIABLE = SessionManagerSISPE.PREFIJO_VARIABLE_SESION+".proceso.envio.copia.descuento.variable";
	public static final String CODIGO_PROCESO_COPIA_AUTORIZAR_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION+".proceso.envio.copia.autorizar.stock";
	
	public static final Integer TAMANIO_RESPUESTA_DESC_VARIABLE = MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.tamanio.cadena.autorizacion.descVariable");
	public static final Integer TAMANIO_RESPUESTA_STOCK = MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.tamanio.cadena.autorizacion.stock");
	
	public static final String FECHAS_NO_VALIDAR_STOCK = SessionManagerSISPE.PREFIJO_VARIABLE_SESION+".fechas.no.validar.stock";
	
		

	
	/**
	 * 
	 * @param usuarioAutorizador
	 * @param request
	 * @throws Exception
	 */
	public static void validarAutorizado(UserDto usuarioAutorizador, HttpServletRequest request)throws Exception{

		//se obtiene el id del usuario logeado
		String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();

		LogSISPE.getLog().info("USUARIO AUTORIZADO: {} \n {} USUARIO AUTORIZADOR: {}",userId,usuarioAutorizador.getUserId());

		//creaci\u00F3n del objeto Autorizador
		AutorizadorDTO autorizadorDTO = new AutorizadorDTO();
		autorizadorDTO.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		autorizadorDTO.setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
		autorizadorDTO.setIdUsuarioAutorizador(usuarioAutorizador.getUserId());
		autorizadorDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		//este c\u00F3digo de local es del usuario que est\u00E1 logeado en el sistema, el mismo que debe estar autorizado
		autorizadorDTO.setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());

		//creaci\u00F3n del objeto AutorizadoDTO
		AutorizadoDTO autorizadoDTO = new AutorizadoDTO();
		autorizadoDTO.setAutorizadorDTO(autorizadorDTO);
		autorizadoDTO.getId().setIdUsuarioAutorizado(userId);
		autorizadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);

		//llamada al m\u00E9todo para validar el usuario autorizado
		SessionManagerSISPE.getCorpAutorizacionesServicio().validarAutorizado(autorizadoDTO);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////    METODOS PARA MANEJO DE NUEVO SISTEMA DE AUTORIZACIONES   //////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	


	/*
	 * Obtiene un objeto tipo AutorizacionDTO de coleccion de autorizaciones de acuerdo al tipo indicado*
	 * @param request
	 * @param tipoAutorizacion
	 * @retur AutorizacionDTO encontradan
	 */
	public static AutorizacionDTO obtenerAutorizacionPorTipoDesdeSesion(HttpServletRequest request, String tipoAutorizacion){
		Collection<AutorizacionDTO> autorizaciones = (Collection<AutorizacionDTO>)request.getSession().getAttribute(AUTORIZACION_COL);
		if(autorizaciones!=null){
			for(AutorizacionDTO autorizacionDTO : autorizaciones){
				if(autorizacionDTO.getGrupoTipoAutorizacionDTO().getTipoAutorizacionDTO().getCodigoInterno().equals(tipoAutorizacion)){
					return autorizacionDTO;
				}
			}
		}

		return null;
	}
	
	/**
	 * Valida si en la coleccion de las autorizaciones del estado-pedido existe una autorizacion del tipo indicado 
	 * @param request
	 * @param codigoTipoAutorizacion
	 * @return true si existe la autorizacion,false caso contrario
	 */
	public static Boolean obtenerAutorizacionPorTipoDesdeSesion(HttpServletRequest request, Long codigoTipoAutorizacion){
		Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizaciones = (Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_PEDIDO_COL);
		if(CollectionUtils.isNotEmpty(autorizaciones)){
			for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO : autorizaciones){
				if(autorizacionDTO.getCodigoTipoAutorizacion().equals(codigoTipoAutorizacion)){
					LogSISPE.getLog().info("Existe una autorizacion en session del tipo: {}",codigoTipoAutorizacion);
					return Boolean.TRUE;
				}
			}
		} 
		return Boolean.FALSE;
	}
	
	/**
	 * Valida si en la coleccion de las autorizaciones del estado-pedido existe una autorizacion del tipo indicado 
	 * @param request
	 * @param codigoTipoAutorizacion
	 * @return true si existe la autorizacion,false caso contrario
	 */
	public static ec.com.smx.autorizaciones.dto.AutorizacionDTO obtenerObjAutorizacionPorTipoDesdeSesion(HttpServletRequest request, Long codigoTipoAutorizacion,Long codigoAutorizacion){
		Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizaciones = (Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_PEDIDO_COL);
		if(CollectionUtils.isNotEmpty(autorizaciones)){
			for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO : autorizaciones){
				if(codigoAutorizacion==null){
					if(autorizacionDTO.getCodigoTipoAutorizacion().equals(codigoTipoAutorizacion)){
						return autorizacionDTO;
					}
				}else if(autorizacionDTO.getCodigoTipoAutorizacion().equals(codigoTipoAutorizacion) && autorizacionDTO.getId().getCodigoAutorizacion().longValue()==codigoAutorizacion.longValue()){
					return autorizacionDTO;
				}
			}
		} 
		return null;
	}



	/**
	 * 
	 * @param autorizacionDTO
	 * @param request
	 * @throws Exception 
	 * @throws FrameworkException 
	 */
	@SuppressWarnings("rawtypes")
	public static void verificacionAutorizaciones(String codigoPedido, HttpServletRequest request, ActionMessages errors, ActionMessages exitos) throws Exception{
		request.getSession().removeAttribute(TIENE_AUTORIZACION_STOCK_CANASTOS);
		request.getSession().removeAttribute(TIENE_AUTORIZACION_STOCK_PAVOS);
		request.getSession().removeAttribute(TIENE_AUTORIZACIONES_PENDIENTES_STOCK);
		String cadenaAutorizaciones = "";
		EstadoPedidoDTO consultaEstadoPedidoDTO = new EstadoPedidoDTO();
		consultaEstadoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaEstadoPedidoDTO.getId().setCodigoPedido(codigoPedido);
		//consultaEstadoPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado"));
		consultaEstadoPedidoDTO.setNpEsUltimoEstado(true);
		//se realiza la consulta del estado actual
		Collection estadoPedidoDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoPedido(consultaEstadoPedidoDTO);
		EstadoPedidoDTO estadoPedidoDTO = null;
		if (estadoPedidoDTOCol!=null && !estadoPedidoDTOCol.isEmpty() && estadoPedidoDTOCol.size()==1){
			Iterator<EstadoPedidoDTO> it = estadoPedidoDTOCol.iterator();
			estadoPedidoDTO = it.next();
			String secuencialEstadoPedido = estadoPedidoDTO.getId().getSecuencialEstadoPedido();
			Integer codigoLocal = estadoPedidoDTO.getId().getCodigoAreaTrabajo();
			cadenaAutorizaciones = CotizacionReservacionUtil.obtenerAutorizacionAbonoStock(request,codigoPedido, secuencialEstadoPedido,codigoLocal);
		}
		if(cadenaAutorizaciones != null && !cadenaAutorizaciones.equals("")){
			String parametro[] = cadenaAutorizaciones.split(SEPARADOR_TOKEN);
			//Mensajes Informativos
			for(int i=0 ; i<parametro.length; i++){
				if(parametro[i].equals("stock")){
					exitos.add("autorizacionTipoStockAbono",new ActionMessage("message.autorizacionEstadoPedidoAbonoStock"));
				}
				if(parametro[i].equals("stockPavPolCan")){
					exitos.add("autorizacionTipoGerenciaComercial",new ActionMessage("message.autorizacionGerenciaComercialStock"));
				}
			}
		}
	}


	
	/**
	 * Metodo que almacena al detalle si hay articulo que necesitan autorizacion
	 * @param request
	 * @param detallePedidoCol	articulos agregados al pedido
	 * @param tipoAutorizacion true[Gerente comercial] , false[Administrador local]
	 * @param errors
	 * @param warnings
	 * @param exitos
	 * @throws Exception
	 */
	public static void agregarAutorizacionDescVar(HttpServletRequest request, ArrayList<DetallePedidoDTO> detallePedidoCol, String valorReferencial, StringBuffer tituloAut, 
			ClasificacionDTO clasificacionDTO, Double porcentaje, ActionMessages errors, ActionMessages warnings, ActionMessages exitos) throws Exception{
		try{
			//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
	  		Long codigoAutorizarDescVar = obtenerCodigoProcesoAutorizarDescVar(request);
	  		Long codigoTipoAutorizacion = ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue();
	  		
			LogSISPE.getLog().info("Guardando info para autorizaci\u00F3n");
			
			HttpSession session = request.getSession();
			Collection<String> codigosClasificacionesPAvosCol = (Collection<String>) session.getAttribute(CotizarReservarAction.CLASIFICACIONES_PAVOS_SELECCIONADOS_COL);
			
			String nombreDepartamento ="";
			if(valorReferencial.contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.adicional"))){
				nombreDepartamento =valorReferencial.split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE \\("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.adicional")+"\\)")[1];
			}else{
				if(valorReferencial.split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1].contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"))){
					nombreDepartamento =valorReferencial.split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1].split("\\("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia")+"\\)")[0];
				}else{
					nombreDepartamento =valorReferencial.split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1];
				}
			}
			
			//se obtiene de sesion la cola de autorizaciones
			ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES);
			String usuAut=valorReferencial.split(SEPARADOR_TOKEN)[3].split(SEPARADOR_DOS_PUNTOS)[1];
				//Objeto no serializable que se debe enviar al componente
				Autorizacion nuevaAutorizacion = new Autorizacion();
//				nuevaAutorizacion.setUsuarioAutorizado(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				nuevaAutorizacion.setUsuarioAutorizado(asignarUsuarioAutorizado(request));
				
				if(StringUtils.isNotEmpty(usuAut)){
					String[] codigoUsuariofuncionario = new String[]{usuAut};
					//Agregando usuario del comprador
					nuevaAutorizacion.setFiltroUsuariosAutorizadores(codigoUsuariofuncionario); 
					nuevaAutorizacion.setUsuarioAutorizador(usuAut);
				}else{
					warnings.add("sinFuncionario", new ActionMessage("warning.clasificacion.autorizador", clasificacionDTO.getCodigoClasificacionPadre()+" "+nombreDepartamento));
				}
				
				DetalleEstadoPedidoAutorizacionDTO detEstPedAutDTO = null;
				
				//Nueva Autorizacion Descuento Variable
				EstadoPedidoAutorizacionDTO autorizacionPendienteDTO = new EstadoPedidoAutorizacionDTO();
				autorizacionPendienteDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				autorizacionPendienteDTO.setEstado(ConstantesGenerales.ESTADO_AUT_PENDIENTE);
				autorizacionPendienteDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				autorizacionPendienteDTO.setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
				
				autorizacionPendienteDTO.setNpTipoAutorizacion(codigoTipoAutorizacion);
//				if(valorReferencial.split(SEPARADOR_TOKEN)[3].split(":")[1].split(",").length>1){
//					autorizacionPendienteDTO.setValorReferencial(valorReferencial.replace(valorReferencial.split(SEPARADOR_TOKEN)[3].split(":")[1], usuAut));	
//				}else{
					autorizacionPendienteDTO.setValorReferencial(valorReferencial);
//				}
				autorizacionPendienteDTO.setNpNombreDepartamento(nombreDepartamento);
				
				nuevaAutorizacion.setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
//				nuevaAutorizacion.setAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString());
				nuevaAutorizacion.setAreaTrabajo(asignarAreaTrabajoAutorizacion(request));
				nuevaAutorizacion.setTituloAutorizacion(tituloAut.toString());
				
				if(colaAutorizaciones==null){
					colaAutorizaciones = new ArrayList<EstadoPedidoAutorizacionDTO>();
				}
				
				//Secuencial temporal necesario para ubicar en la vuelta las autorizaciones creadas
				Long secuencial = new Long(colaAutorizaciones.size()) + 1;			
				nuevaAutorizacion.setSecuencial(secuencial);		
				autorizacionPendienteDTO.setNpAutorizacion(nuevaAutorizacion);	
				
				//A\u00F1adiendo autorizaciones a articulos		
				int bandera = 0;
				
				//bandera que indica si la autorizacion debe o no agregarse a la cola de autorizaciones 
				Boolean agregarAutorizacion = Boolean.FALSE;
				//se recorren los detalles del pedido
				for(DetallePedidoDTO detallePedidoDTO: detallePedidoCol){
					if(StringUtils.isEmpty(detallePedidoDTO.getNpIdAutorizador())){
						//Busca si la clasificacion del articulo del pedido coincide con las clasificaciones que administra el comprador del descuento variable
			//			if(clasificacionDTO.getId().getCodigoClasificacion().equals(detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre())){
						FuncionarioDTO funcionarioAutorizador = obtenerFuncionarioAutorizadorPorClasificacion(obtenerCodigoClasificacion(detallePedidoDTO),obtenerTipoMarca(detallePedidoDTO),
								request, codigoAutorizarDescVar, warnings);
						if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
							detallePedidoDTO.setNpIdAutorizador(funcionarioAutorizador.getUsuarioFuncionario());
						}
					}
					
					if(detallePedidoDTO.getNpIdAutorizador() != null && detallePedidoDTO.getNpIdAutorizador().equals(usuAut)
							&& clasificacionDTO.getId().getCodigoClasificacion().equals(obtenerCodigoDepartamento(detallePedidoDTO))
							&& obtenerTipoMarca(detallePedidoDTO).equals(valorReferencial.split(SEPARADOR_TOKEN)[4].substring(valorReferencial.split(SEPARADOR_TOKEN)[4].length()-1).equals("0")?"PRO":"PRV")){

		//			if(clasificacionDTO.getId().getCodigoClasificacion().equals(obtenerCodigoDepartamento(detallePedidoDTO))){
						Boolean tieneDescAutomatico = Boolean.FALSE;
						//se verifica que no tenga descuento automatico de pavos
						if(CollectionUtils.isNotEmpty(codigosClasificacionesPAvosCol)){
							for(String codigoclasificacionPAvosActual : codigosClasificacionesPAvosCol){
								if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codigoclasificacionPAvosActual)){ 
									tieneDescAutomatico = Boolean.TRUE;		
									break;
								}
							}
						}
						if(!tieneDescAutomatico){
							//solo se consulta una vez
							if(!agregarAutorizacion){
								Collection<String> idAutorizadorCopia = obtenerIdFuncionarioRecibiraCopiaAutorizacion(request, detallePedidoDTO, codigoTipoAutorizacion);
								if(CollectionUtils.isNotEmpty(idAutorizadorCopia)){
									nuevaAutorizacion.getUsuariosAutorizadores().addAll(idAutorizadorCopia);
									LogSISPE.getLog().info("se enviaran copia de autorizaciones a: "+idAutorizadorCopia);
								}
							}
							
							agregarAutorizacion = Boolean.TRUE;
							//cuando no tiene autorizaciones de descuento variable
							if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())  
									|| !verificarArticuloPorTipoAutorizacion(detallePedidoDTO, codigoTipoAutorizacion)){	
								
								if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
									detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>());
								}
								detEstPedAutDTO = new DetalleEstadoPedidoAutorizacionDTO();
								detEstPedAutDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
								detEstPedAutDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
								detEstPedAutDTO.setEstadoPedidoAutorizacionDTO(autorizacionPendienteDTO);
								detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().add(detEstPedAutDTO);
								bandera = 1;
							}
//							cuando ya tiene autorizaciones
//							else{			
//								for(DetalleEstadoPedidoAutorizacionDTO detalle : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
//									//solo para el caso de descuentos variable
//									if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
//											 detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
////										Se verifica si los detalles ya tienen autorizaciones anteriores
//										if(detalle.getEstadoPedidoAutorizacionDTO().getNumeroProceso() != null ){
//											if(!detalle.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
//												if(bandera==0) bandera = 3;
//												else if(bandera==1) bandera = 2;
//											}
//										}
//										else if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion()!=null &&
//												detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
//											
//											if(bandera==0) bandera = 3; 
//											else if(bandera==1) bandera = 2;
//										}else{
//											detEstPedAutDTO = new DetalleEstadoPedidoAutorizacionDTO();
//											detEstPedAutDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//											detEstPedAutDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
//											detEstPedAutDTO.setEstadoPedidoAutorizacionDTO(autorizacionPendienteDTO);
//											detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().add(detEstPedAutDTO);
//											if(bandera==3) bandera = 2;
//											else bandera = 1;
//										}
//									}
//								}
//							}	
						}
					} 
				}
				
				if(bandera == 1){	
					CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.agregar.autorizacion.descuento.variable", "exito", null, exitos);
				}
//				else if(bandera == 2 && session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) == null){
//					warnings.add("warning",new ActionMessage("warning.agregar.autorizacion.descuento.variable"));
//				}
//				else if(bandera == 3){
//					CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.agregar.autorizacion.descuento.variable", "alerta", clasificacionDTO.getId().getCodigoClasificacion(), warnings);
//				}
				//Si no existen Errores se agrega a la cola de nuevas autorizaciones
				if(bandera != 3){
					StringBuffer descripcion = new StringBuffer();
					descripcion.append("Solicito autorizaci\u00F3n de descuento variable del ");
					descripcion.append(porcentaje);
					descripcion.append("% para el departamento: ");
					descripcion.append(clasificacionDTO.getDescripcionClasificacion().split(SEPARADOR_TOKEN)[0]);
				
					nuevaAutorizacion.setDescripcion(descripcion.toString());
					//se valida que no se ingresen datos repetidos
					if(!agregarAutorizacion){
						warnings.add("autorizacionNoAgregada", new ActionMessage("errors.gerneral","No se agreg\u00F3 la: "+nuevaAutorizacion.getTituloAutorizacion()+" por no tener art\u00EDculos por aplicar"));
					}else if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
						Boolean autorizacionrepetida = Boolean.FALSE;
						for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
							//7-CTD3-CMDMON-COM01-INX1-AUTORIZACION DESCUENTO VARIABLE ABASTOS-9.0
							//se borra el indice de los compradores
							String[] vect = autorizacionActual.getValorReferencial().split(SEPARADOR_TOKEN);
							String valorRef1 = SEPARADOR_TOKEN+vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3]+SEPARADOR_TOKEN+vect[4].substring(vect[4].length()-1);
							
							vect = autorizacionPendienteDTO.getValorReferencial().split(SEPARADOR_TOKEN);
							String valorRef2 = SEPARADOR_TOKEN+vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3]+SEPARADOR_TOKEN+vect[4].substring(vect[4].length()-1);
							LogSISPE.getLog().info("valorReferencial de la cola de autorizaciones: "+valorRef1+" valorRef2: "+valorRef2);
							if(valorRef1.equals(valorRef2)	&& autorizacionActual.getNpTipoAutorizacion().equals(autorizacionPendienteDTO.getNpTipoAutorizacion())){
								autorizacionrepetida = Boolean.TRUE;
							}
								
						}
						if(!autorizacionrepetida && agregarAutorizacion){
							colaAutorizaciones.add(autorizacionPendienteDTO);
						}
					} else { 
						colaAutorizaciones.add(autorizacionPendienteDTO);
					}
					session.setAttribute(COLA_AUTORIZACIONES,colaAutorizaciones);
					//agregar detalles para pedidos consolidados
					List<DetallePedidoDTO> detallePedidoConsolidado = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
					
					if( !CollectionUtils.isEmpty(detallePedidoConsolidado) && !CollectionUtils.isEmpty(detallePedidoCol)){
						
						for(DetallePedidoDTO detPedDTO : detallePedidoCol){
							
							if(verificarArticuloPorTipoAutorizacion(detPedDTO, codigoTipoAutorizacion)){
							
								for(DetallePedidoDTO detPedCon : detallePedidoConsolidado){
									
									if(detPedDTO.getId().getCodigoArticulo().equals(detPedCon.getId().getCodigoArticulo())){
										
										for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detPedDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
											
											if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
												
												detPedCon.getEstadoDetallePedidoDTO().agregarDetalleEstadoPedidoAutorizacionDTO(autorizacionActual);
											}
										}
									}
								}
							}
						}
					}
				}
			
			
		
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al crear las autorizaciones de descuento variable. {}",e);
		}
	}


	
	/**
	 * Metodo que busca y elimina autorizaciones pendientes por descuentos
	 * @param colaAutorizaciones 
	 * @param request
	 * @param detallePedidoCol	articulos agregados al pedido
	 * @throws Exception
	 */
	public static void eliminarAutorizacionDescVar(Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesParam, 
			ArrayList<DetallePedidoDTO> detallePedidoCol, HttpServletRequest request, Boolean eliminarTodos) throws Exception{
		
		LogSISPE.getLog().info("Eliminando autorizaciones Descuentos");
		Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones= colaAutorizacionesParam;

		//se obtiene de la sesion los datos del detalle y de la lista de articulos.		
		for(DetallePedidoDTO detallePedidoDTO: detallePedidoCol){
			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()!=null && 
					!detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().isEmpty()){
				
				Collection<DetalleEstadoPedidoAutorizacionDTO> detalleEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				//Iterando detalle autorizaciones del articulo
				for(DetalleEstadoPedidoAutorizacionDTO detalle : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
					//solo para el caso de descuentos variable
					if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion()!=null &&
							 detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
						if(eliminarTodos){
							detalleEliminarCol.add(detalle);
							colaAutorizaciones = new ArrayList<EstadoPedidoAutorizacionDTO>();
						}
						else if(!detalle.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA.toString()) 
						&& !detalle.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA.toString())){
							detalleEliminarCol.add(detalle);
							//Eliminando de la cola de autorizaciones
							if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
								for(EstadoPedidoAutorizacionDTO autorizacionCola : colaAutorizaciones){
									//se compara por los IDs cuando los tienen
									if(detalle.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion() != null
											&& autorizacionCola.getId().getCodigoEstadoPedidoAutorizacion() != null){
										if(detalle.getEstadoPedidoAutorizacionDTO().getId().getCodigoCompania().equals(autorizacionCola.getId().getCodigoCompania()) &&
												detalle.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion().equals(autorizacionCola.getId().getCodigoEstadoPedidoAutorizacion())){
											colaAutorizaciones.remove(autorizacionCola);
											break;
										}
									}
									//se comparan los valores referenciales
									else{
										if(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial() != null 
												&& autorizacionCola.getValorReferencial() != null){
											if(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacionCola.getValorReferencial()) ){
												colaAutorizaciones.remove(autorizacionCola);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				if(CollectionUtils.isNotEmpty(detalleEliminarCol)){
					detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(detalleEliminarCol);
					if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()))
						detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
				}
			}
		}
		if(CollectionUtils.isEmpty(colaAutorizaciones)){
			request.getSession().removeAttribute(COLA_AUTORIZACIONES);
		}else{ 
			request.getSession().setAttribute(COLA_AUTORIZACIONES,colaAutorizaciones);
		}
	}

	/**
	 * Metodo que envia la informaci\u00F3n al sistema de autorizaciones y arma la url para iframe del popup resultante
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String mostrarPopUpAutorizaciones(Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones,
			HttpServletRequest request, CotizarReservarForm formulario, Boolean mostrarBotonCancelar) throws Exception{
		
		LogSISPE.getLog().info("Mostrando popup autorizaciones");		
		
		try{
			
			URLSystemConnection urlSystemConnection = new URLSystemConnection();
			//evitar que se bloquee la pantalla despues de mostrar el componente de autorizaciones
			urlSystemConnection.setScriptParameters(new HashMap<String, Object>());
			urlSystemConnection.getScriptParameters().put("evalScripts", Boolean.TRUE);
			urlSystemConnection.setFormActionURL("crearCotizacion.do");
			urlSystemConnection.setFormName("cotizarRecotizarReservarForm");
			urlSystemConnection.setRenderSection(new ArrayList<String>());
			urlSystemConnection.getRenderSection().add("\'div_pagina\'");
			urlSystemConnection.getRenderSection().add("\'mensajes\'");
			urlSystemConnection.getRenderSection().add("\'pregunta\'");
			urlSystemConnection.setViewParameters(new HashMap<String, Object>());		
			urlSystemConnection.getViewParameters().put("backgroundColor", "#FFFFD2");
			urlSystemConnection.getViewParameters().put("cerrarPopUp", "parent.ocultarModal();");
			
			//por defecto es true 
			if(!mostrarBotonCancelar){
				urlSystemConnection.getViewParameters().put("showCancelButton", "false;");
			}
			//se busca el codigo del pedido para pasarlo como parametro
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			// se obtiene el valor de filtro tipo autorizacion
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.filtro.autorizacion", request);
			Long valorDescuentoAprobado = new Long(parametroDTO.getValorParametro().split(SEPARADOR_TOKEN)[0]); //23 descuento aprobado
			Long valorDescuentoSolicitado = new Long(parametroDTO.getValorParametro().split(SEPARADOR_TOKEN)[1]);//61 descuento solicitado
	
			ValorComponente[] vectorComponentes = new ValorComponente[2];
			
			HashMap<Long, ValorComponente[]> filtroTipoAutorizacion;
			
			AutorizacionDataKey dataKeyTipoAutorizacion = null;
			AutorizacionDataKey dataKeyAreaTrabajo=new AutorizacionDataKey("AREA TRABAJO", String.valueOf(asignarAreaTrabajoAutorizacion(request)), false, true);
			AutorizacionDataKey dataKeyTotalPedido=new AutorizacionDataKey("TOTAL PEDIDO",  Util.roundDoubleMath(new Double(formulario.getTotal().toString()), 2).toString(), false, true);
			AutorizacionDataKey dataKeyTotalAplicarDcto= null;
			AutorizacionDataKey dataKeyNombreContacto= null;
			AutorizacionDataKey dataKeyNumeroDocumento=new AutorizacionDataKey("N\u00DAMERO DOCUMENTO", "", false, true);
			AutorizacionDataKey dataKeyDetallePedido=null;
			
			if(formulario.getTipoDocumento() != null){
				 dataKeyNumeroDocumento=new AutorizacionDataKey("N\u00DAMERO DOCUMENTO", formulario.getNumeroDocumento() == null ? "" : formulario.getNumeroDocumento(), false, true);
				 Validator validador=new ValidatorImpl();
				 if(validador.validateRUC(formulario.getNumeroDocumento())){
					if(!validador.validateTipoRUC(formulario.getNumeroDocumento()).equals(TipoEmpresaEnum.NATURAL)){
						dataKeyNombreContacto = new AutorizacionDataKey("NOMBRE EMPRESA", formulario.getNombreEmpresa() == null ? "" : formulario.getNombreEmpresa().replace(";", " "), false, true);
					}else{ 
						dataKeyNombreContacto = new AutorizacionDataKey("NOMBRE COMERCIAL", formulario.getNombrePersona() == null ? "" : formulario.getNombrePersona().replace(";", " "), false, true);
					}
				 }else{
					 dataKeyNombreContacto = new AutorizacionDataKey("NOMBRE CLIENTE", formulario.getNombrePersona() == null ? "" : formulario.getNombrePersona().replace(";", " "), false, true);
				 }
			}
			
			AutorizacionDataKey dataKeyNumeroPedido = new AutorizacionDataKey("N\u00DAMERO PEDIDO", "PEDIDO NUEVO", false, true);
			if(vistaPedidoDTO!= null){
				dataKeyNumeroPedido=new AutorizacionDataKey("N\u00DAMERO PEDIDO", vistaPedidoDTO.getId().getCodigoPedido(), false, true);
			}
			
			if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null ){
				dataKeyNumeroPedido=new AutorizacionDataKey("N\u00DAMERO PEDIDO", "PEDIDO CONSOLIDADO", false, true);
				dataKeyNombreContacto = new AutorizacionDataKey("NOMBRE CLIENTES", "CLIENTES VARIOS", false, true);
				dataKeyNumeroDocumento = new AutorizacionDataKey("N\u00DAMERO DOCUMENTO", "CLIENTES VARIOS", false, true);
			}
			Boolean soloAutorizacion = Boolean.FALSE;
			Boolean mostrarBotonSolicitarTodos = Boolean.TRUE;
			ArrayList<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();

			if(colaAutorizaciones != null && !colaAutorizaciones.isEmpty()){
				
				for(EstadoPedidoAutorizacionDTO autorizacionPendienteDTO : colaAutorizaciones){
					
					//cuando se muestra solo para aplicar autorizacion se oculta el boton solicitarTodos
					if(autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial() != null 
							&& autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial().equals(AutorizacionEstado.AUTORIZADA.getEstado())){
						mostrarBotonSolicitarTodos = Boolean.FALSE;
					}
					
					filtroTipoAutorizacion = new HashMap<Long, ValorComponente[]>();
					if(autorizacionPendienteDTO.getValorReferencial() != null){
						String[] vectorValorReferencial=autorizacionPendienteDTO.getValorReferencial().split(SEPARADOR_TOKEN);
						if(vectorValorReferencial.length > 0){	
							vectorComponentes[1]=new ValorComponente(valorDescuentoSolicitado, vectorValorReferencial[vectorValorReferencial.length-1].toString(), true, false); //61 Descuento solicitado
						}
					}
					String departamentoAutorizacion = "DESCUENTO VARIABLE ";
					if(autorizacionPendienteDTO.getNpAutorizacion() != null && StringUtils.isNotEmpty(autorizacionPendienteDTO.getNpAutorizacion().getDescripcion())){
						departamentoAutorizacion += " - "+autorizacionPendienteDTO.getNpAutorizacion().getDescripcion().split(SEPARADOR_DOS_PUNTOS)[1].split(SEPARADOR_TOKEN)[0];
						autorizacionPendienteDTO.getNpAutorizacion().setDescripcion("");
					}
					dataKeyTipoAutorizacion = new AutorizacionDataKey("TIPO AUTORIZACION", departamentoAutorizacion, false, true);
					vectorComponentes[0]=new ValorComponente(valorDescuentoAprobado, "0.00", false, true); //23 Descuento aprobado
					String sumaDetallesComprador = sumarDetallesDelMismoDepartamento(request,autorizacionPendienteDTO.getValorReferencial()).toString();
					ValorComponente[] nuevoVector = SerializationUtils.clone(vectorComponentes);
					filtroTipoAutorizacion.put(ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE, nuevoVector);				
					autorizacionPendienteDTO.getNpAutorizacion().setFiltroTipoAutorizacion(filtroTipoAutorizacion);
					
					dataKeyTotalAplicarDcto = new AutorizacionDataKey("VALOR APLICAR DESCUENTO", sumaDetallesComprador, false, true);	
					AutorizacionDataKey vectorDataKey[]=null;
					
					if(autorizacionPendienteDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE) 
							&& autorizacionPendienteDTO.getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
						dataKeyDetallePedido=new AutorizacionDataKey(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.pdf.detalle.pedido"),MessagesWebSISPE.getString("ec.com.smx.sic.sispe.pdf.detalle.pedido"), false, false);
					}
					
					if(dataKeyDetallePedido!=null){
						vectorDataKey=new AutorizacionDataKey[]{dataKeyTipoAutorizacion, dataKeyAreaTrabajo, dataKeyNumeroPedido, dataKeyTotalPedido, dataKeyTotalAplicarDcto, dataKeyNumeroDocumento, dataKeyNombreContacto,dataKeyDetallePedido};
						dataKeyDetallePedido=null;
					}else{
						vectorDataKey=new AutorizacionDataKey[]{dataKeyTipoAutorizacion, dataKeyAreaTrabajo, dataKeyNumeroPedido, dataKeyTotalPedido, dataKeyTotalAplicarDcto, dataKeyNumeroDocumento, dataKeyNombreContacto};
					}
					
					autorizacionPendienteDTO.getNpAutorizacion().setDataKeys(vectorDataKey);	
					
					if(!autorizacionCol.contains(autorizacionPendienteDTO.getNpAutorizacion()))
						autorizacionCol.add(autorizacionPendienteDTO.getNpAutorizacion());
					
					//cuando se va solo a aplicar autorizaciones
					if(autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial() != null && autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial().equals(AutorizacionEstado.AUTORIZADA.getEstado())){
						soloAutorizacion = Boolean.TRUE;
					}
				}
			}	
			
			//por defecto es true
			if(!mostrarBotonSolicitarTodos){
				urlSystemConnection.getViewParameters().put("showSolicitarTodos", "false;");
			}
			
			request.getSession().setAttribute(CODIGO_TIPO_AUTORIZACION, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.toString());
			//cuando es solo para una autorizacion se reduce el tama\u00F1o, por defecto es 465px
			if(autorizacionCol.size() == 1){
				//se muestra el popUp mas peque\u00F1o
				if(soloAutorizacion){
					urlSystemConnection.getViewParameters().put("componentHeight", "290px;");
					request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "390px");
				}
				else{
					urlSystemConnection.getViewParameters().put("componentHeight", "370px;");
					request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "470px");
				}			
			}
			else{
				//se muestra el popUp mas peque\u00F1o
				if(soloAutorizacion){
					urlSystemConnection.getViewParameters().put("componentHeight", "350px;");
					request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "450px");
				}
				else 
					request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "565px");
			}
			
			if(request.getSession().getAttribute("ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones") != null){
				urlSystemConnection.setFormActionURL("detalleEstadoPedido.do");
				urlSystemConnection.setFormName("listadoPedidosForm");
			}
			//se guarda el json y se retorna el url generado
			return(guardarJson(urlSystemConnection, request, autorizacionCol));
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al mostrar el componente de autorizaciones de descuento variable. {}",e);
			return null;
		}
	}
	
	private static StringBuilder generarXmlDetallePedidoAutorizaciones(HttpServletRequest request){
		StringBuilder datos= new StringBuilder();
		String estadoActivo=SessionManagerSISPE.getEstadoActivo(request);
		VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO) request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		datos.append("<datos>");
		datos.append("<numPedido>");datos.append(vistaPedidoDTO.getId().getCodigoPedido());datos.append("</numPedido>");
		if(vistaPedidoDTO.getNombreEmpresa()!=null){
			datos.append("<documentoCliente>");datos.append(vistaPedidoDTO.getRucEmpresa());datos.append("</documentoCliente>");
			datos.append("<nombreCliente>");datos.append(vistaPedidoDTO.getNombreEmpresa());datos.append("</nombreCliente>");
			datos.append("<telefonoCliente>");datos.append(vistaPedidoDTO.getTelefonoEmpresa());datos.append("</telefonoCliente>");
			
		}else{
			datos.append("<documentoCliente>");datos.append(vistaPedidoDTO.getNumeroDocumentoPersona());datos.append("</documentoCliente>");
			datos.append("<nombreCliente>");datos.append(vistaPedidoDTO.getNombrePersona());datos.append("</nombreCliente>");
			datos.append("<telefonoCliente>");datos.append(vistaPedidoDTO.getTelefonoPersona());datos.append("</telefonoCliente>");
			datos.append("<emailCliente>");datos.append(vistaPedidoDTO.getEmailPersona());datos.append("</emailCliente>");
		}
		datos.append("<numBonos>");datos.append(vistaPedidoDTO.getNumBonNavEmp()!=null?vistaPedidoDTO.getNumBonNavEmp():"0");datos.append("</numBonos>");
		
		
		for(VistaDetallePedidoDTO vistaDetalle:(Collection<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
			datos.append("<detalles>");
			datos.append("<codigoBarras>");datos.append(vistaDetalle.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras());datos.append("</codigoBarras>");	
			datos.append("<descripcionArticulo>");datos.append(vistaDetalle.getArticuloDTO().getDescripcionArticulo());datos.append("</descripcionArticulo>");
			datos.append("<cantidad>");datos.append(vistaDetalle.getCantidadEstado());datos.append("</cantidad>");
			datos.append("<peso>");datos.append(vistaDetalle.getPesoRegistradoLocal()!=null?vistaDetalle.getPesoRegistradoLocal():vistaDetalle.getPesoArticuloEstado());datos.append("</peso>");
			
			datos.append("<vUnitario>");datos.append(vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoActivo)?vistaDetalle.getValorUnitarioEstado():vistaDetalle.getValorUnitarioNoAfiliado());datos.append("</vUnitario>");
			datos.append("<vUnitarioIVA>");datos.append(vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoActivo)?vistaDetalle.getValorUnitarioIVAEstado():vistaDetalle.getValorUnitarioIVANoAfiliado());datos.append("</vUnitarioIVA>");
			datos.append("<totalIVA>");datos.append(vistaDetalle.getValorPrevioVenta());datos.append("</totalIVA>");
			datos.append("<IVA>");datos.append(vistaDetalle.getAplicaIVA().equals(estadoActivo)?"I":"");datos.append("</IVA>");
			datos.append("<descuento>");datos.append(vistaDetalle.getValorFinalEstadoDescuento()>0?"D":"");datos.append("</descuento>");
			datos.append("<totalNeto>");datos.append(vistaDetalle.getValorTotalVenta());datos.append("</totalNeto>");
			datos.append("</detalles>");
		}
		
		if(CollectionUtils.isNotEmpty(vistaPedidoDTO.getDescuentosEstadosPedidos())){
			Double totalDescuentos=0D;
			for(DescuentoEstadoPedidoDTO descuento:(Collection<DescuentoEstadoPedidoDTO>)vistaPedidoDTO.getDescuentosEstadosPedidos()){
				datos.append("<descuentosPedido>");
				datos.append("<descripcion>");datos.append(descuento.getDescuentoDTO().getTipoDescuentoDTO().getDescripcionTipoDescuento());datos.append("</descripcion>");
				datos.append("<porcentaje>");datos.append(descuento.getPorcentajeDescuento());datos.append("</porcentaje>");
				datos.append("<descuento>");datos.append(descuento.getValorDescuento());datos.append("</descuento>");
				totalDescuentos+=descuento.getValorDescuento();
				datos.append("</descuentosPedido>");
			}
			datos.append("<totalDescuentos>");datos.append(totalDescuentos);datos.append("</totalDescuentos>");
		}
		
		datos.append("<subTotalBruto>");datos.append(vistaPedidoDTO.getValorTotalBrutoSinIva());datos.append("</subTotalBruto>");
		datos.append("<descuentos>");datos.append(vistaPedidoDTO.getTotalDescuentoIva());datos.append("</descuentos>");
		datos.append("<subTotalNeto>");datos.append(vistaPedidoDTO.getSubTotalNetoBruto());datos.append("</subTotalNeto>");
		datos.append("<tarifa0>");datos.append(vistaPedidoDTO.getSubTotalNoAplicaIVA());datos.append("</tarifa0>");
		datos.append("<tarifa12>");datos.append(vistaPedidoDTO.getSubTotalAplicaIVA());datos.append("</tarifa12>");
		datos.append("<iva12>");datos.append(vistaPedidoDTO.getIvaPedido());datos.append("</iva12>");
		datos.append("<costoFlete>");datos.append(vistaPedidoDTO.getValorCostoEntregaPedido());datos.append("</costoFlete>");
		datos.append("<total>");datos.append(vistaPedidoDTO.getTotalPedido());datos.append("</total>");
		datos.append("</datos>");
		return datos;
		
	}
	
	/**
	 * Metodo que recibe la informacion del sistema de autorizaciones y la rearma en el esquema de autorizaciones del SISPE,
	 * por referencia se actualizan tambien los datos en el detallePedido que va hacia el metodo de guardar la cotizacion
	 * @param detallePedidoCol
	 * @param request
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public static Boolean procesarRespuestaAutorizaciones(HttpServletRequest request, CotizarReservarForm formulario, 
			ActionMessages exitos, ActionMessages warnings, ActionMessages infos, ActionMessages errors) throws Exception{
		
		Boolean guardar = Boolean.FALSE;
		try{
			LogSISPE.getLog().info("popUp autorizacion "+request.getParameter("ec.com.smx.sic.sispe.pedido.popupAutorizaciones"));
			//Respuesta del sistema de autorizaciones: 1:19805:F17-1609:7:AUTORIZADA:{Descuento Aprobado:8.0,Descuento Solicitado:12.0}:enviarNotificacion-1
			LogSISPE.getLog().info("Respuesta del sistema de autorizaciones: {}",request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")));
			if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) != null){
	
				Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
				
				//Separando el grupo de autorizaciones
				String [] autorizaciones = request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")).split(SEPARADOR_COMA);
				Collection<String> autorizacionesSolicitadas = new ArrayList<String>();
				Collection<String> autorizacionesAprobadas = new ArrayList<String>();
				Collection<String> autorizacionesNuevasAprobadas = new ArrayList<String>();
				Collection<String> autorizacionesRechazadas = new ArrayList<String>();
				Collection<String> autorizacionesUtilizadas = new ArrayList<String>();
				Collection<String> autorizacionesCaducadas = new ArrayList<String>();
				Collection<String> autorizacionesAprobadasNoUsadas = new ArrayList<String>();
				
				//primero se valida que el sistema de autorizaciones haya retornado correctamente los datos
				if(autorizaciones != null && autorizaciones.length > 0){
					for(int i=0; i<autorizaciones.length; i++){
						String autorizacion = autorizaciones[i];
						if(autorizacion.contains("null")){
							LogSISPE.getLog().error("el sistema de autorizaciones no ha podido crear la autorizacion");
							CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.respuesta.sistema.autorizacion", "error", null, errors);
							return Boolean.FALSE;
						}
						String [] detalleAut = autorizacion.split(SEPARADOR_DOS_PUNTOS);
						
						//se inicia la coleccion
						if(CollectionUtils.isEmpty(autorizacionesFinalizarWorkflow)){
							autorizacionesFinalizarWorkflow = new ArrayList<String>();
						}else{
							//se verifica que la colleccion no tenga ese processCode, porque cuando se reutiliza una autorizacion puede generarse dos detalles
							Collection<String> datosEliminar = new ArrayList<String>();
							for(String act : autorizacionesFinalizarWorkflow){
								if(act.contains(detalleAut[2])){
									datosEliminar.add(act);
								}
							}
							autorizacionesFinalizarWorkflow.removeAll(datosEliminar);
						}
						
						//se verifica que no este en estado solicitado
						if(!autorizacion.contains(AutorizacionEstado.SOLICITADA.getEstado())){
							
							//se arma las autorizaciones del workflow   --> processCode,estadoAutorizacion,companyID,sistemID,userID
							autorizacionesFinalizarWorkflow.add(obtenerAutorizacionPorFinalizar(request, detalleAut, null));
						}
						
						for(int j=0; j<detalleAut.length; j++){
							if(detalleAut[j].equals(AutorizacionEstado.SOLICITADA.getEstado())){
								//en caso de que una autorizacion ya solicitada no cambie su estado el primer valor viene la palabra null en ese
								//caso solo consulta y no hace nada
								if(!detalleAut[0].equals("null")){
									autorizacionesSolicitadas.add(autorizacion);
									break;
								}
							}else if(detalleAut[j].equals(AutorizacionEstado.AUTORIZADA.getEstado())){
								if(!detalleAut[0].equals("null"))
									autorizacionesNuevasAprobadas.add(autorizacion);
								else
									autorizacionesAprobadas.add(autorizacion);
								break;
							}else if(detalleAut[j].equals(AutorizacionEstado.RECHAZADA.getEstado())){
								autorizacionesRechazadas.add(autorizacion);
								break;
							}else if(detalleAut[j].equals(AutorizacionEstado.UTILIZADA.getEstado())){
								autorizacionesUtilizadas.add(autorizacion);
								break;
							}else if(detalleAut[j].equals(AutorizacionEstado.CADUCADA.getEstado())){
								autorizacionesCaducadas.add(autorizacion);
								break;
							}
							else if(detalleAut[j].equals(AutorizacionEstado.NO_USADA.getEstado())){
								autorizacionesAprobadasNoUsadas.add(autorizacion);
								break;
							}
						}
					}
				} else{
					LogSISPE.getLog().error("el sistema de autorizaciones no ha podido crear la autorizacion");
					CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.respuesta.sistema.autorizacion", "error", null, errors);
					return Boolean.FALSE;
				}
				//se sube a sesion las autorizaciones workflow
				request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
				
				//Ubicando autorizaciones solicitadas con las del pedido
				if(autorizacionesSolicitadas!=null && !autorizacionesSolicitadas.isEmpty()){	
					LogSISPE.getLog().info("Tiene autorizaciones por crear: {}", autorizacionesSolicitadas.size());
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);			
					Boolean autorizacionEncontrada = Boolean.FALSE;
					EstadoPedidoAutorizacionDTO autorizacionNoEncontrada = new EstadoPedidoAutorizacionDTO();
					for(EstadoPedidoAutorizacionDTO autorizacionSolicitadaDTO: colaAutorizaciones){				
						autorizacionNoEncontrada = autorizacionSolicitadaDTO;
						for(String autorizacion : autorizacionesSolicitadas){						
							if(autorizacion.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){						
								String secuencial = autorizacion.split(SEPARADOR_DOS_PUNTOS)[0];
								//1:27661:F17-5520:7:SOLICITADA:true
								//Si los secuenciales coinciden se setean los datos
								if(autorizacionSolicitadaDTO.getNpAutorizacion().getSecuencial() != null 
										&& autorizacionSolicitadaDTO.getNpAutorizacion().getSecuencial().longValue() == new Long(secuencial).longValue()){							
									String codigoAutorizacion = autorizacion.split(SEPARADOR_DOS_PUNTOS)[1];
									String processCode = autorizacion.split(SEPARADOR_DOS_PUNTOS)[2];
									autorizacionSolicitadaDTO.setCodigoAutorizacion(new Long(codigoAutorizacion));
									autorizacionSolicitadaDTO.setNumeroProceso(processCode);
									autorizacionSolicitadaDTO.setEstado(ConstantesGenerales.ESTADO_AUT_SOLICITADA);
									autorizacionEncontrada = Boolean.TRUE;
									autorizacionNoEncontrada = null;
									guardar = Boolean.TRUE;
									autorizacionSolicitadaDTO.getNpAutorizacion().setEnviarNotificacion(autorizacion.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue() 
											? Boolean.valueOf(autorizacion.split(SEPARADOR_DOS_PUNTOS)[5]): Boolean.FALSE);
									autorizacionSolicitadaDTO.getNpAutorizacion().setProcessCode(processCode);
									break;
								}
								
								else if(autorizacionSolicitadaDTO.getCodigoAutorizacion() != null && autorizacion.split(SEPARADOR_DOS_PUNTOS)[1].equals(autorizacionSolicitadaDTO.getCodigoAutorizacion().toString())){
									autorizacionSolicitadaDTO.getNpAutorizacion().setSecuencial(new Long(secuencial));
									autorizacionEncontrada = Boolean.TRUE;
									autorizacionNoEncontrada = null;
									guardar = Boolean.TRUE;
									autorizacionSolicitadaDTO.getNpAutorizacion().setEnviarNotificacion(autorizacion.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue() 
											? Boolean.valueOf(autorizacion.split(SEPARADOR_DOS_PUNTOS)[5]): Boolean.FALSE);
									break;
								}
							
							}else{
								LogSISPE.getLog().info("Autorizacion length no es > 4: {}", autorizacion.split(SEPARADOR_DOS_PUNTOS).length);
								errors.add("error",new ActionMessage("errors.respuesta.sistema.autorizacion",SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreUsuario()));
								return Boolean.FALSE;
							}
						}				
					}
					//En caso de no encontrar una autorizacion solicitada
					if(!autorizacionEncontrada){
						errors.add("error",new ActionMessage("errors.respuesta.sistema.autorizacion.sinRespuesta", autorizacionNoEncontrada.getNpTipoAutorizacion()));
						return Boolean.FALSE;
					}
				}
				//Ubicando autorizaciones solicitadas con ingreso de numero de autorizacion
				if(autorizacionesNuevasAprobadas!=null && !autorizacionesNuevasAprobadas.isEmpty()){	
					
					LogSISPE.getLog().info("Tiene autorizaciones Solicitadas por aplicar: {}", autorizacionesNuevasAprobadas.size());
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
					//bandera para borrar las autorizaciones para el admin de local cuando los datos son erroneos
					Boolean autorizacionEncontrada = Boolean.FALSE;
					EstadoPedidoAutorizacionDTO autorizacionNoEncontrada = new EstadoPedidoAutorizacionDTO();
					
					for(String autorizacion : autorizacionesNuevasAprobadas){	
						for(EstadoPedidoAutorizacionDTO autorizacionSolicitadaDTO: colaAutorizaciones){	
							autorizacionNoEncontrada = autorizacionSolicitadaDTO;
							if(autorizacion.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
								if(!verificarSiCodigoAutorizacionEstaRepetido(new Long(autorizacion.split(SEPARADOR_DOS_PUNTOS)[1]), colaAutorizaciones)){
									String secuencial = autorizacion.split(SEPARADOR_DOS_PUNTOS)[0];
									String codigoAutorizacion = autorizacion.split(SEPARADOR_DOS_PUNTOS)[1];
									
									//Si los secuenciales coinciden se setean los datos
									Boolean procesarAutorizacion=false;
								
									if(autorizacionSolicitadaDTO.getCodigoAutorizacion() != null && autorizacionSolicitadaDTO.getCodigoAutorizacion().toString().equals(codigoAutorizacion)){
										procesarAutorizacion = Boolean.TRUE;
									}
									else if(autorizacionSolicitadaDTO.getCodigoAutorizacion() == null && autorizacionSolicitadaDTO.getNpAutorizacion().getSecuencial() != null 
											&& autorizacionSolicitadaDTO.getNpAutorizacion().getSecuencial().longValue() == new Long(secuencial).longValue()){
										procesarAutorizacion = Boolean.TRUE;								
									}
									
									if(procesarAutorizacion){							
										String processCode = autorizacion.split(SEPARADOR_DOS_PUNTOS)[2];
										String valorDcto = "";
										if(autorizacion.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
											valorDcto=autorizacion.split(SEPARADOR_DOS_PUNTOS)[6].split(SEPARADOR_TOKEN)[0];									
										}
										autorizacionSolicitadaDTO.setCodigoAutorizacion(new Long(codigoAutorizacion));
										autorizacionSolicitadaDTO.setNumeroProceso(processCode);
										
										autorizacionSolicitadaDTO.setEstado(ConstantesGenerales.ESTADO_AUT_APROBADA);
										autorizacionSolicitadaDTO.getNpAutorizacion().setSecuencial(Long.parseLong(secuencial));
										
										autorizacionEncontrada = Boolean.TRUE;
										autorizacionNoEncontrada = null;
										//Aplicando el descuento
										String [] opDescuentos = {autorizacionSolicitadaDTO.getValorReferencial()};					
										formulario.setOpDescuentos(opDescuentos);
										String [] clave = opDescuentos[0].split(SEPARADOR_TOKEN);
										int indice = 0;
										int indiceMp = 0;
										//buscando el indice
										for(int i=0; i<clave.length; i++){
											if(clave[i].contains(INDICE_DESCUENTO_GENERAL)){
												indice = Integer.parseInt(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].substring(0,clave[i].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
												indiceMp = Integer.parseInt(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].substring(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
												break;
											}									
										}
										if(clave !=null && clave.length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
											if(formulario.getPorcentajeVarDescuento() == null || formulario.getPorcentajeVarDescuento().length == 0 || formulario.getPorcentajeVarDescuento().length <= indice){
												Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol =  (Collection<ClasificacionDTO>) 
														request.getSession().getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
												formulario.setPorcentajeVarDescuento(new Double[clasificacionDepartamentosPedidoCol.size()][2]);
											}
											if(!valorDcto.equals("")){										
												formulario.getPorcentajeVarDescuento()[indice][indiceMp] = new Double(valorDcto);
												clave[clave.length-1]=valorDcto.toString();
											}
											else {
												clave[clave.length-1]=valorDcto.toString();
												formulario.getPorcentajeVarDescuento()[indice][indiceMp] = new Double(clave[6]);
											}
											//Rearmando la llave del descuento con los tres primero items del arreglo
											opDescuentos[0] = clave[0] + SEPARADOR_TOKEN + clave[1] 
															  + SEPARADOR_TOKEN + clave[2]
															  + SEPARADOR_TOKEN + clave[3]
															  + SEPARADOR_TOKEN + clave[4];
											String valorReferencial=clave[0];
											for(int i=1; i<clave.length; i++){
												valorReferencial+=SEPARADOR_TOKEN+clave[i]; 
											}
											autorizacionSolicitadaDTO.setValorReferencial(valorReferencial);
											
											CotizarReservarAction.aceptarDescuento(formulario, request, errors, warnings, exitos, infos);
													
											if(errors.size()==0){																				
												autorizacionSolicitadaDTO.setEstado(ConstantesGenerales.ESTADO_AUT_APROBADA);
												guardar = Boolean.TRUE;
												//para actualizar el estado de todos los detalles de la misma autorizacion
												ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
												agregarAutorizacionSimilaresDescVar(request, detallePedidoCol);
											}
										}								
										break;
									}
								}else{
									LogSISPE.getLog().info(" la autorizacion que intenta aplicar ya fue usada en este mismo pedido anteriormente");
									CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.autorizacion.utilizada","autorizacionUsada", "", errors);
									autorizacionEncontrada = Boolean.TRUE;
								}
							}else{
								LogSISPE.getLog().info("Autorizacion length no es > 6: {}", autorizacion.split(SEPARADOR_DOS_PUNTOS).length);
								errors.add("error",new ActionMessage("errors.respuesta.sistema.autorizacion",SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreUsuario()));
								return Boolean.FALSE;
							}
						}
					}
					
					request.getSession().setAttribute(COLA_AUTORIZACIONES, colaAutorizaciones);
					//En caso de no encontrar una autorizacion solicitada
					if(!autorizacionEncontrada){
						errors.add("error",new ActionMessage("errors.respuesta.sistema.autorizacion.sinRespuesta", autorizacionNoEncontrada.getNpTipoAutorizacion()));
						return Boolean.FALSE;
					}
				}
				//Buscando si hay autorizaciones para aplicar
	//			else{
				if(autorizacionesAprobadas!=null && !autorizacionesAprobadas.isEmpty()){			
					LogSISPE.getLog().info("Tiene autorizaciones por aplicar: {}", autorizacionesAprobadas.size());
					ArrayList<EstadoPedidoAutorizacionDTO> autorizacionesConsulta = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_CONSULTA);
					//Seg\u00FAn la navegacion de la pantalla siempre es una coleccion de una autorizacion que puede aplicar varios articulos del pedido
					if(autorizacionesConsulta != null && !autorizacionesConsulta.isEmpty()){	
						for(EstadoPedidoAutorizacionDTO detalle : autorizacionesConsulta){
							String [] opDescuentos = {detalle.getValorReferencial()};					
							formulario.setOpDescuentos(opDescuentos);
							String [] clave = opDescuentos[0].split(SEPARADOR_TOKEN);
							if(clave !=null && clave.length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
								formulario.setPorcentajeVarDescuento( new Double [][]{{ new Double(clave[6].substring(0,clave[6].length()-1)) },{ new Double(clave[6].substring(clave[6].length()-1)) }});	
								//Rearmando la llave del descuento con los tres primero items del arreglo
								opDescuentos[0] = clave[0] + SEPARADOR_TOKEN + clave[1]
												  + SEPARADOR_TOKEN + clave[2]
												  + SEPARADOR_TOKEN + clave[3]
												  + SEPARADOR_TOKEN + clave[4];							
								CotizarReservarAction.aceptarDescuento(formulario, request, errors, warnings, exitos, infos);
								if(errors.size()==0){
									detalle.setEstado(ConstantesGenerales.ESTADO_AUT_APROBADA);
									actualizarAutorizacionPedido(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request);
								}
							}
						}
					}					
				}
					//Se valida si la autorizacion esta en estado solicitada y ya fue utilizada por otro pedido o sistema y advierte al usuario
				if(autorizacionesUtilizadas!=null && !autorizacionesUtilizadas.isEmpty()){					
					//1:14942:F17-1228:UTILIZAR
					for(String  autorizacionUtilizada : autorizacionesUtilizadas){
						Long codigoAutorizacion = new Long(autorizacionUtilizada.split(SEPARADOR_DOS_PUNTOS)[1]);
						String processCode = autorizacionUtilizada.split(SEPARADOR_DOS_PUNTOS)[2];
						LogSISPE.getLog().info("Tiene autorizaciones ya utilizadas: {}", autorizacionesUtilizadas.size());
						ArrayList<EstadoPedidoAutorizacionDTO> autorizacionesConsulta = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_CONSULTA);
						//Seg\u00FAn la navegacion de la pantalla siempre es una coleccion de una autorizacion que puede aplicar varios articulos del pedido
						if(autorizacionesConsulta != null && !autorizacionesConsulta.isEmpty()){
							for(EstadoPedidoAutorizacionDTO detalle : autorizacionesConsulta){
								if(detalle.getCodigoAutorizacion().longValue() == codigoAutorizacion && detalle.getNumeroProceso().equals(processCode)){
									detalle.setEstado(ConstantesGenerales.ESTADO_AUT_UTILIZADA);
									actualizarAutorizacionPedido(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request);
									CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.autorizacion.utilizada", "warning", "", warnings);
								}
							}
						}
						else {
							guardar = Boolean.TRUE;
						}
					}					
				}
					
					//Buscando si hay autorizaciones rechazadas
				if(autorizacionesRechazadas!=null && !autorizacionesRechazadas.isEmpty()){
					//1:14921:F17-1225:RECHAZAR
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
					String[] opDescuentos = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesEliminar = new ArrayList<EstadoPedidoAutorizacionDTO>(); 
					String nombreDepartamentos = "";
					for(String  autorizacionRechazada : autorizacionesRechazadas){
						Long codigoAutorizacion = new Long(autorizacionRechazada.split(SEPARADOR_DOS_PUNTOS)[1]);
						String processCode = autorizacionRechazada.split(SEPARADOR_DOS_PUNTOS)[2];
						LogSISPE.getLog().info("Tiene autorizaciones por rechazar: {}", autorizacionesRechazadas.size());
	//					ArrayList<EstadoPedidoAutorizacionDTO> autorizacionesConsulta = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_CONSULTA);
						//Seg\u00FAn la navegacion de la pantalla siempre es una coleccion de una autorizacion que puede aplicar varios articulos del pedido
						if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
							
							for(EstadoPedidoAutorizacionDTO detalle : colaAutorizaciones){
								if(detalle.getCodigoAutorizacion().longValue() == codigoAutorizacion && detalle.getNumeroProceso().equals(processCode)){
									detalle.setEstado(ConstantesGenerales.ESTADO_AUT_RECHAZADA);
									actualizarAutorizacionPedido(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request);
									
									//se quita del opDescuentos la llave
									eliminarLlaveDeAutorizacionesNoValidas(colaAutorizaciones, opDescuentos, codigoAutorizacion, processCode); 
									colaAutorizacionesEliminar.add(detalle);
									String nombreDepartamento = detalle.getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE ")[1];
									nombreDepartamentos += nombreDepartamento.endsWith(" ") ? nombreDepartamento.substring(0, nombreDepartamento.length()-1)+", " : nombreDepartamento+", ";
									//se quita el espacio al inicio y final de la linea
									eliminarAutorizacionDetalle(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request,ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE);
								}
							}
						}
					}
					warnings.add("descuentoEliminado", new ActionMessage("warning.descuento.variable.eliminado",nombreDepartamentos));
					//se elimina la autorizacion y se elimina de sesion
					colaAutorizaciones.removeAll(colaAutorizacionesEliminar);
					//se sube a sesion el opDescuentos sin las llaves de las autorizaciones rechazadas
					request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
				}
					//Si la autorizacion se aprobo y no se utilizo esta puede caducar
				if(autorizacionesCaducadas!=null && !autorizacionesCaducadas.isEmpty()){
					
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
					String[] opDescuentos = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					
					//1:14921:F17-1225:CADUCAR
					for(String  autorizacionCaducada : autorizacionesCaducadas){
						Long codigoAutorizacion = new Long(autorizacionCaducada.split(SEPARADOR_DOS_PUNTOS)[1]);
						String processCode = autorizacionCaducada.split(SEPARADOR_DOS_PUNTOS)[2];
						LogSISPE.getLog().info("Tiene autorizaciones ya utilizadas: {}", autorizacionesUtilizadas.size());
						ArrayList<EstadoPedidoAutorizacionDTO> autorizacionesConsulta = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_CONSULTA);
						//Seg\u00FAn la navegacion de la pantalla siempre es una coleccion de una autorizacion que puede aplicar varios articulos del pedido
						if(autorizacionesConsulta != null && !autorizacionesConsulta.isEmpty()){
							for(EstadoPedidoAutorizacionDTO detalle : autorizacionesConsulta){
								if(detalle.getCodigoAutorizacion().longValue() == codigoAutorizacion && detalle.getNumeroProceso().equals(processCode)){
									detalle.setEstado(ConstantesGenerales.ESTADO_AUT_CADUCADA);
									actualizarAutorizacionPedido(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request);
									CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.autorizacion.caducada", "warning", "warning.autorizacion.caducada", warnings);
									
									//se quita del opDescuentos la llave
									eliminarLlaveDeAutorizacionesNoValidas(colaAutorizaciones, opDescuentos, codigoAutorizacion, processCode);
								}
							}
						}
					}
					//se sube a sesion el opDescuentos sin las llaves de las autorizaciones rechazadas
					request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
				}
					
					//Si la autorizacion se aprobo y el usuario rechazo la autorizacion, se cancelan los descuentos
				if(CollectionUtils.isNotEmpty(autorizacionesAprobadasNoUsadas)){
					String[] opDescuentos = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					//2:18883:F17-1456:NO USADA
					for(String  autorizacionAprobadaNoUsada : autorizacionesAprobadasNoUsadas){
						String secuencial = autorizacionAprobadaNoUsada.split(SEPARADOR_DOS_PUNTOS)[0];
						Long codigoAutorizacion = new Long(autorizacionAprobadaNoUsada.split(SEPARADOR_DOS_PUNTOS)[1]);
						String processCode = autorizacionAprobadaNoUsada.split(SEPARADOR_DOS_PUNTOS)[2];
						LogSISPE.getLog().info("Tiene autorizaciones aprobadas y rechazadas por el usuario: {}", autorizacionesAprobadasNoUsadas.size());
						ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
				
						if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
							ArrayList<EstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<EstadoPedidoAutorizacionDTO>();
							for(EstadoPedidoAutorizacionDTO detalle : colaAutorizaciones){
								
								//Si los secuenciales coinciden se setean los datos
								Boolean procesarAutorizacion=false;
							
								
								if( detalle.getNpAutorizacion().getSecuencial() != null 
										&& detalle.getNpAutorizacion().getSecuencial().longValue() == new Long(secuencial).longValue()){
									procesarAutorizacion = Boolean.TRUE;								
								}							
								else if(detalle.getCodigoAutorizacion() != null && detalle.getCodigoAutorizacion().longValue() == codigoAutorizacion
										&& detalle.getNumeroProceso().equals(processCode))
									procesarAutorizacion = Boolean.TRUE;
								
								if(procesarAutorizacion){									
									autorizacionesEliminar.add(detalle);
									detalle.setCodigoAutorizacion(codigoAutorizacion);
									detalle.setNumeroProceso(processCode);
									
									detalle.setEstado(ConstantesGenerales.ESTADO_AUT_NO_USADA);
									eliminarAutorizacionDetalle(detalle.getId().getCodigoEstadoPedidoAutorizacion(),detalle,request, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE);
									//se quita del opDescuentos la llave
									eliminarLlaveDeAutorizacionesNoValidas(colaAutorizaciones, opDescuentos, codigoAutorizacion, processCode);
									CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.autorizacion.no.usada", "warning", "warning.autorizacion.no.usada", warnings);
								}								
							}
							//Se quita de la cola de autorizaciones 
							colaAutorizaciones.removeAll(autorizacionesEliminar);
							request.getSession().setAttribute(COLA_AUTORIZACIONES,colaAutorizaciones);
							//se sube a sesion el opDescuentos sin las llaves de las autorizaciones rechazadas
							request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
						}
					}					
				}
			}else{
				errors.add("error",new ActionMessage("errors.respuesta.sistema.autorizacion",SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreUsuario()));
			}		
				
	}catch (Exception e) {
		LogSISPE.getLog().error("Error al procesar la respuesta del sistema de autorizaciones: {}",e);
		errors.add("procesarRespuestaAutorizaciones", new ActionMessage("errors.gerneral","Error al procesar la respuesta del sistema de autorizaciones "+e.getMessage()));
	}
	return guardar;
	}
	
	/**
	 * Metodo que ubica una autorizacion en articulos del pedido para su modificacion
	 * @param codigo
	 * @param estadoAutAprobada
	 * @param request
	 */
	private static void actualizarAutorizacionPedido(Long codigo, EstadoPedidoAutorizacionDTO detalle, HttpServletRequest request) {
		LogSISPE.getLog().info("Actualizando la autorizacion {}", codigo);
		
		ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		if(detallePedidoCol!=null && !detallePedidoCol.isEmpty()){
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoCol){
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()!=null && 
						!detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().isEmpty()){																						
					for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacionesDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						//solo para el caso de descuentos variable
						if(detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
								 detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
							if(detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion()!=null &&
									detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion().longValue() == codigo){
								detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().setEstado(detalle.getEstado());
								break;
							}
						}
					}
				}
			}
			//Se agrega a la cola de autorizaciones por actualizar el estado
			ArrayList<EstadoPedidoAutorizacionDTO> actualizarAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_ACTUALIZAR);
			if(actualizarAutorizaciones==null)
				actualizarAutorizaciones = new ArrayList<EstadoPedidoAutorizacionDTO>();
			actualizarAutorizaciones.add(detalle);
			request.getSession().setAttribute(AUTORIZACIONES_ACTUALIZAR,actualizarAutorizaciones);
		}
	}

	/**
	 * Metodo que almacena al detalle si hay articulo que necesitan autorizacion
	 * @param request
	 * @param detallePedidoCol	articulos agregados al pedido
	 * @param tipoAutorizacion true[Gerente comercial] , false[Administrador local]
	 * @param errors
	 * @param warnings
	 * @param exitos
	 * @throws Exception
	 */
	public static void buscarAutorizacionesPorArticulo(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, CotizarReservarForm formulario, 
			Long codigoTipoAutorizacion, ActionMessages errors) throws Exception{
		LogSISPE.getLog().info("Filtrando Autorizaciones ");
		
		//Se agrega a la cola de autorizaciones
		ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesConsulta = new ArrayList<EstadoPedidoAutorizacionDTO>();		
				
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			
			for(DetalleEstadoPedidoAutorizacionDTO detalle : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				//solo para el caso de descuentos variable
				if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null && 
						detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()
						&& codigoTipoAutorizacion == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
					Autorizacion autorizacion = new Autorizacion();
					
					if(ec.com.smx.autorizaciones.dto.AutorizacionDTO.isLoaded(detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO())
							&& detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null){
						autorizacion.setAreaTrabajo(detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getAreaTrabajo());
					}
					
					String [] titulo = detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN);
					if(titulo!= null && titulo[5]!=null && titulo[titulo.length-1]!=null)
						autorizacion.setTituloAutorizacion(titulo[5] + " " + titulo[titulo.length-1] +  "%");
					else
						autorizacion.setTituloAutorizacion("AUTORIZACION DESCUENTO VARIABLE" );// + detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial() + "%");
					autorizacion.setCodigoAutorizacion( detalle.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());
					
					//se agregan los usuarios que recibiran copia para que los mensajes les lleguen a todos
					List<String> idAutorizadorCopia = (List<String>) obtenerIdFuncionarioRecibiraCopiaAutorizacion(request, detallePedidoDTO,
							ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue());
					if(CollectionUtils.isNotEmpty(idAutorizadorCopia)){
						autorizacion.getUsuariosAutorizadores().addAll(idAutorizadorCopia);
						LogSISPE.getLog().info("se enviaran copia de autorizaciones a: "+idAutorizadorCopia);
					}
					
					detalle.getEstadoPedidoAutorizacionDTO().setNpAutorizacion(autorizacion);
					colaAutorizacionesConsulta.add(detalle.getEstadoPedidoAutorizacionDTO());
				}
				//para el caso de autorizaciones de stock 
				else if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null && 
						detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue())
						&& codigoTipoAutorizacion == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
					//para las autorizaciones de stock 
					mostrarPopUpAutorizacionesPorTipo(request, Boolean.TRUE, ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue(), Boolean.TRUE, null);
					return;
				}
			}
			request.getSession().setAttribute(AUTORIZACIONES_CONSULTA,colaAutorizacionesConsulta);
//			request.getSession().setAttribute(COLA_AUTORIZACIONES,colaAutorizacionesConsulta);
			
			String urlComponenteAutorizaciones =  mostrarPopUpAutorizaciones(colaAutorizacionesConsulta,request, formulario, true);
			if(StringUtils.isNotEmpty(urlComponenteAutorizaciones)){
				request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, urlComponenteAutorizaciones);
			}else{
				errors.add("errorMostrarComponenteAut", new ActionMessage("errors.gerneral","Error al mostrar el componente de autorizaciones"));
			}
		}
	}



	/**
	 * Solicita o procesa las autorizaciones
	 * @param accionAutorizacion
	 * @param request
	 * @param formulario
	 * @param exitos
	 * @param warnings
	 * @param infos
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public static Boolean solicitarAutorizacion(String accionAutorizacion, HttpServletRequest request, CotizarReservarForm formulario, 
			ActionMessages exitos, ActionMessages warnings, ActionMessages infos, ActionMessages errors) throws Exception {
		
		LogSISPE.getLog().info("Verificando si hay solicitudes nuevas de autorizaciones");
		if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"))==null){
			//Verifica si tiene autorizaciones en cola
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (Collection<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
			if(colaAutorizaciones!=null && !colaAutorizaciones.isEmpty()){	
				Collection<EstadoPedidoAutorizacionDTO > colaAutorizacionesAux = new ArrayList<EstadoPedidoAutorizacionDTO>();
				
				for(EstadoPedidoAutorizacionDTO estadoPedAut : colaAutorizaciones ){
					if(estadoPedAut.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
						//Verifica si esta en Reservacion
						if(accionAutorizacion.equals(MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"))){
							estadoPedAut.getNpAutorizacion().setEstadoInicial(AutorizacionEstado.AUTORIZADA.getEstado());
						}else{ 
							estadoPedAut.getNpAutorizacion().setEstadoInicial(null);
						}
						colaAutorizacionesAux.add(estadoPedAut);
					}
				}
				
				//llamando a metodo que devuelve url del sistema de autorizaciones
				if(colaAutorizacionesAux.size() > 0){
					
					String urlComponenteAutorizaciones =  mostrarPopUpAutorizaciones(colaAutorizacionesAux,request, formulario, true);
					if(StringUtils.isNotEmpty(urlComponenteAutorizaciones)){
						request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, urlComponenteAutorizaciones);
						request.getSession().setAttribute(ACCION_ANTERIOR_AUTORIZACION, accionAutorizacion);
						return Boolean.TRUE;
					}else{
						errors.add("errorMostrarComponenteAut", new ActionMessage("errors.gerneral","Error al mostrar el componente de autorizaciones"));
					}
				}else{
					return Boolean.FALSE;
				}
			}
		}
		//---------------------------- registrar la cotizaci\u00F3n al ejecutar autorizaciones-----------------------
		else{
			//se procesan por separado las autorizaciones de stock y las de descuento variable
			String respuestaSistemaAutorizaciones = request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"));
			String[] vectorAut = respuestaSistemaAutorizaciones.split(SEPARADOR_COMA);
			Long codigoTipoAutorizacion = obtenerCodigoTipoAutorizacion(vectorAut[0]);
			if(codigoTipoAutorizacion != null && codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
				//es una autorizacion de stock
				
				//Verifica si tiene autorizaciones en cola
				Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (Collection<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
				if(CollectionUtils.isNotEmpty(colaAutorizaciones)){	
					Collection<EstadoPedidoAutorizacionDTO > colaAutorizacionesAux = new ArrayList<EstadoPedidoAutorizacionDTO>();
					
					for(EstadoPedidoAutorizacionDTO estadoPedAut : colaAutorizaciones ){
						if(estadoPedAut.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
							//Verifica si esta en Reservacion
							if(accionAutorizacion.equals(MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"))){
								estadoPedAut.getNpAutorizacion().setEstadoInicial(AutorizacionEstado.AUTORIZADA.getEstado());
							}else{ 
								estadoPedAut.getNpAutorizacion().setEstadoInicial(null);
							}
							colaAutorizacionesAux.add(estadoPedAut);
						}
					}
					
					//llamando a metodo que devuelve url del sistema de autorizaciones
					if(colaAutorizacionesAux.size() > 0){
						
						String urlComponenteAutorizaciones =  mostrarPopUpAutorizaciones(colaAutorizacionesAux,request, formulario, true);
						if(StringUtils.isNotEmpty(urlComponenteAutorizaciones)){
							request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, urlComponenteAutorizaciones);
							request.getSession().setAttribute(ACCION_ANTERIOR_AUTORIZACION, accionAutorizacion);
							return Boolean.TRUE;
						}else{
							errors.add("errorMostrarComponenteAut", new ActionMessage("errors.gerneral","Error al mostrar el componente de autorizaciones"));
						}
					}else{
						return Boolean.FALSE;
					}
				}
				return Boolean.FALSE;
			}
			//Procesando la respuesta del sistema de autorizaciones
			LogSISPE.getLog().info("Se solicitaron autorizaciones");
			Boolean guardar = procesarRespuestaAutorizaciones(request, formulario, exitos, warnings, infos, errors);
			request.getSession().removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
			if(!guardar){
				request.getSession().removeAttribute(ACCION_ANTERIOR_AUTORIZACION);
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}


	
	/**
	 * verifica si el pedido ya tiene algun descuento variable 
	 * para vincular la autorizacion a los nuevos articulos del mismo comprador 
	 * @param request
	 * @throws Exception
	 */
	public static void agregarAutorizacionSimilaresDescVar(HttpServletRequest request, ArrayList<DetallePedidoDTO> detallePedidoCol) throws Exception{

		//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
  		Long codigoAutorizarDescVar = obtenerCodigoProcesoAutorizarDescVar(request);
  		
		LogSISPE.getLog().info("ingresa al metodo : agregarAutorizacionSimilaresDescVar");		
		HttpSession session = request.getSession();

		//se obtiene de sesion la clasificacion de pavos
		Collection<String> codigosClasificacionesPAvosCol = (Collection<String>) request.getSession().getAttribute(CotizarReservarAction.CLASIFICACIONES_PAVOS_SELECCIONADOS_COL);
		//se obtiene de sesion las clasificaciones de los departamentos
		Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol =  (Collection<ClasificacionDTO>) session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
		
		//si la coleccion esta nula se apunta a todas las clasificaciones
		if(CollectionUtils.isEmpty(clasificacionDepartamentosPedidoCol)){
			
			verificarDepartamentosPedido(request, new ActionMessages(), new ActionMessages(), Boolean.FALSE);
			
			if(session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO) != null){
				clasificacionDepartamentosPedidoCol = (Collection<ClasificacionDTO>) session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
			}
		}
		
		//idAutorizador, autorizacion
		Map<ClasificacionDTO, DetalleEstadoPedidoAutorizacionDTO> mapCompradoresAut = new HashMap<ClasificacionDTO, DetalleEstadoPedidoAutorizacionDTO>();
		
		try{
			if(CollectionUtils.isNotEmpty(detallePedidoCol) && CollectionUtils.isNotEmpty(clasificacionDepartamentosPedidoCol)){
				
				//se recorre las clasificaciones de departamentos del pedido
				for(ClasificacionDTO clasificacionDTO : clasificacionDepartamentosPedidoCol){
					//se recorre los detalles
					for(DetallePedidoDTO detallePedidoActual : detallePedidoCol){
						
						//solo para el caso de descuentos variables
						if(verificarArticuloPorTipoAutorizacion(detallePedidoActual, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE)){
							
							if(detallePedidoActual.getNpIdAutorizador() != null && compararAutorizador(clasificacionDTO.getNpIdAutorizador(), detallePedidoActual.getNpIdAutorizador())
									&& clasificacionDTO.getId().getCodigoClasificacion().equals(obtenerCodigoDepartamento(detallePedidoActual))){
								
								String codigoClasificacionPadre = obtenerCodigoDepartamento(detallePedidoActual);
								DetalleEstadoPedidoAutorizacionDTO autorizacion = obtenerAutorizacionVariable(detallePedidoActual);
								
								if(mapCompradoresAut.containsKey(clasificacionDTO)){
									if(autorizacion != null && !mapCompradoresAut.get(clasificacionDTO).getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial())){
										mapCompradoresAut.put(SerializationUtils.clone(clasificacionDTO), autorizacion);
									}else if(autorizacion != null && autorizacion.getEstadoPedidoAutorizacionDTO().getEstado().
											equals(ConstantesGenerales.ESTADO_AUT_APROBADA)	&& autorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial()
											.contains(codigoClasificacionPadre) && compararAutorizador(clasificacionDTO.getNpIdAutorizador(), autorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getUsuarioAutorizador())){
										mapCompradoresAut.put(clasificacionDTO, autorizacion);
									}
								}else{
									mapCompradoresAut.put(clasificacionDTO, autorizacion);
								}
							}
						}
					}					
				}											
				
				if(!mapCompradoresAut.isEmpty()){
					
					Set<Map.Entry<ClasificacionDTO, DetalleEstadoPedidoAutorizacionDTO>> valoresMap = mapCompradoresAut.entrySet();
					
					//codigoClasificacion,idAutorizador
//					Map<String, String> mapClasificacionAutorizador = new HashMap<String, String>();
//					String key;
					
					//se recorre los detalles
					for(DetallePedidoDTO detallePedidoActual : detallePedidoCol){
						String idAutorizador = "";
						
						//se recorren los valores del map
						for(Map.Entry<ClasificacionDTO, DetalleEstadoPedidoAutorizacionDTO> objActual : valoresMap){
							//se compara la clasificacion del comprador con la del articulo del detalle
							if(StringUtils.isEmpty(detallePedidoActual.getNpIdAutorizador())){
								
//								key = AutorizacionesUtil.obtenerCodigoClasificacion(detallePedidoActual);
								
//								if(!mapClasificacionAutorizador.containsKey(key)){
									
									FuncionarioDTO funcionarioAutorizador = obtenerFuncionarioAutorizadorPorClasificacion(obtenerCodigoClasificacion(detallePedidoActual),obtenerTipoMarca(detallePedidoActual),
											request, codigoAutorizarDescVar, new ActionMessages());
									
									if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
										idAutorizador = funcionarioAutorizador.getUsuarioFuncionario();
//										mapClasificacionAutorizador.put(key, idAutorizador);
									}
									
//								}else{
//									idAutorizador = mapClasificacionAutorizador.get(key);
//								}
								
								if(StringUtils.isNotEmpty(idAutorizador)){
									detallePedidoActual.setNpIdAutorizador(idAutorizador);
								}
							}
							if(detallePedidoActual.getNpIdAutorizador() != null && (objActual.getValue().getEstadoPedidoAutorizacionDTO().getValorReferencial().contains("MARCA PROPIA")?"PRO":"PRV").equals(obtenerTipoMarca(detallePedidoActual))
									&& objActual.getKey().getId().getCodigoClasificacion().equals(obtenerCodigoDepartamento(detallePedidoActual))){
								
								Boolean tieneDescAutomatico = Boolean.FALSE;
								//se verifica que no tenga descuento automatico de pavos
								
								if(CollectionUtils.isNotEmpty(codigosClasificacionesPAvosCol)){
									
									for(String codigoclasificacionPAvosActual : codigosClasificacionesPAvosCol){
										if(detallePedidoActual.getArticuloDTO().getCodigoClasificacion().equals(codigoclasificacionPAvosActual)){
											tieneDescAutomatico = Boolean.TRUE;
											//se elimina la autorizacion de descuento variable
											reemplazarAutorizacionDescVariable(detallePedidoActual, null);
											break;
										}
									}
								}
								if(!tieneDescAutomatico){
									DetalleEstadoPedidoAutorizacionDTO autorizacionDescVar = objActual.getValue();
									//se corrige los IDs
									autorizacionDescVar.getId().setCodigoAreaTrabajo(detallePedidoActual.getId().getCodigoAreaTrabajo());
									autorizacionDescVar.getId().setCodigoArticulo(detallePedidoActual.getId().getCodigoArticulo());
									autorizacionDescVar.getId().setCodigoCompania(detallePedidoActual.getId().getCodigoCompania());
									autorizacionDescVar.getId().setCodigoPedido(detallePedidoActual.getId().getCodigoPedido());
									autorizacionDescVar.getId().setSecuencialEstadoPedido(detallePedidoActual.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido());
									reemplazarAutorizacionDescVariable(detallePedidoActual, autorizacionDescVar);
									LogSISPE.getLog().info("al detalle "+detallePedidoActual.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()+":" +detallePedidoActual.getArticuloDTO().getDescripcionArticulo()
											+" se le asigno la autorizacion: "+autorizacionDescVar.getEstadoPedidoAutorizacionDTO().getValorReferencial());
									break;
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al actualizar autorizaciones del mismo comprador {}",e);
		}
	}
	
	public static boolean compararAutorizador(String clasificacionAutorizadores,String autorizador){
		if(clasificacionAutorizadores.split(",").length>1){
			if(clasificacionAutorizadores.split(",")[0].equals(autorizador) 
					||clasificacionAutorizadores.split(",")[1].equals(autorizador)){
				return true;
			}
		}else if(clasificacionAutorizadores.equals(autorizador)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param detallePedidoDTOCol
	 * @param request
	 * @param formulario
	 */
	public static void verificarAutorizacionesVariables(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request, CotizarReservarForm formulario) throws Exception{
		try{
			LogSISPE.getLog().info("ingresa al metodo : verificarAutorizacionesVariables");
			ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = new ArrayList<EstadoPedidoAutorizacionDTO>();
			ArrayList<String> listaOpDescuentos = new ArrayList<String>();
				
			if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
				// se recorre los detalles del pedido
				for(DetallePedidoDTO detalleActual : detallePedidoDTOCol){			
					if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						
						//se recorre las autorizaciones 
						for(DetalleEstadoPedidoAutorizacionDTO detalle : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							//solo para el caso de descuentos variable
							if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null && 
									detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
								Autorizacion autorizacion = new Autorizacion();
								
								if(ec.com.smx.autorizaciones.dto.AutorizacionDTO.isLoaded(detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO())
										&& detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null){
									autorizacion.setAreaTrabajo(detalle.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getAreaTrabajo());
								}
								
								String [] titulo = detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN);
								if(titulo != null && titulo.length >= 7 && titulo[5] !=null && titulo[titulo.length-1] !=null){
									autorizacion.setTituloAutorizacion(titulo[5]);
									
									if(obtenerTipoMarca(detalleActual).equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
										if(!titulo[5].contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"))){
											autorizacion.setTituloAutorizacion(autorizacion.getTituloAutorizacion().toString().concat(" ("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia")+")"));
										}
									}
//									autorizacion.setTituloAutorizacion(titulo[5] + " " + titulo[titulo.length-1] +  "%");
								}else{
									autorizacion.setTituloAutorizacion("AUTORIZACION DESCUENTO VARIABLE");// + detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial() + "%)");
									
									if(obtenerTipoMarca(detalleActual).equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
										if(!autorizacion.getTituloAutorizacion().contains(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"))){
											autorizacion.setTituloAutorizacion(autorizacion.getTituloAutorizacion().toString().concat(" ("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia")+")"));
										}
									}
								}
								autorizacion.setTituloAutorizacion(autorizacion.getTituloAutorizacion().toString().concat(" "+ titulo[titulo.length-1] +  "%"));
								autorizacion.setCodigoAutorizacion( detalle.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());

								//se agregan los usuarios que recibiran copia para que los mensajes les lleguen a todos
								List<String> idAutorizadorCopia = (List<String>) obtenerIdFuncionarioRecibiraCopiaAutorizacion(request, detalleActual,
											ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue());
								if(CollectionUtils.isNotEmpty(idAutorizadorCopia)){
									autorizacion.getUsuariosAutorizadores().addAll(idAutorizadorCopia);
									LogSISPE.getLog().info("se enviaran copia de autorizaciones a: "+idAutorizadorCopia);
								}
								
								detalle.getEstadoPedidoAutorizacionDTO().setNpAutorizacion(autorizacion);
									
								LogSISPE.getLog().info("valor referencial {}",detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial());
								//se agregan el valor referencial a la coleccion
								if(!listaOpDescuentos.contains(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial())){
									listaOpDescuentos.add(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial());
									colaAutorizaciones.add(detalle.getEstadoPedidoAutorizacionDTO());
								}
//								if(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[3].split(":")[1].split(",").length>1){
//									colaAutorizaciones.add(detalle.getEstadoPedidoAutorizacionDTO());
//								}else{
//									if(!colaAutorizaciones.contains(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial())){
//										colaAutorizaciones.add(detalle.getEstadoPedidoAutorizacionDTO());
//									}
//								}
//								if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
//									Boolean addAut=Boolean.FALSE;
//									for (EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO:colaAutorizaciones) {
//										if(!estadoPedidoAutorizacionDTO.getValorReferencial().equals(detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial())){
//											addAut=Boolean.TRUE;
//										}
//									}
//
//									if(addAut){
//										colaAutorizaciones.add(detalle.getEstadoPedidoAutorizacionDTO());
//									}
//								}else{
//									colaAutorizaciones.add(detalle.getEstadoPedidoAutorizacionDTO());
//								}
							}
						}					
					}				
				}
			}
			request.getSession().setAttribute(COLA_AUTORIZACIONES, colaAutorizaciones);
			request.getSession().setAttribute(AUTORIZACIONES_CONSULTA, colaAutorizaciones);
				
			try{
				//de acuerdo a los valores referenciales se arma los tipos y porcentajes de descuentos del pedido
				if(listaOpDescuentos != null && !listaOpDescuentos.isEmpty()){
					int tamVector = -1;
					for(String opDescuentoActual : listaOpDescuentos){
						String[] clave = opDescuentoActual.split(SEPARADOR_TOKEN);							
						if(clave.length > 0){
							int indice=0;
							for(int i = 1; i < clave.length; i++){								
								if(clave[i].contains(INDICE_DESCUENTO_GENERAL)){
									indice=Integer.parseInt(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].substring(0,clave[i].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
									if(indice > tamVector)
										tamVector = indice;
									break;
								}									
							}
						}
					}
					tamVector++;
					 
					String[] opDescuento=new String[listaOpDescuentos.size()];
					Double[][] porcentajeVarDescuento = new Double[tamVector][2];
					
					int posVector = 0;					
					for(String opDescuentoActual : listaOpDescuentos){
						String[] clave = opDescuentoActual.split(SEPARADOR_TOKEN);							
						if(clave.length > 0){
							String opDcto = clave[0];
							int indice=0;
							int indiceMp=0;
							for(int i = 1; i <= 4; i++){								
								opDcto+= SEPARADOR_TOKEN+clave[i];
								if(clave[i].contains(INDICE_DESCUENTO_GENERAL)){
									indice=Integer.parseInt(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].substring(0,clave[i].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));		
									indiceMp=Integer.parseInt(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].substring(clave[i].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
								}
							}
							opDescuento[posVector]=opDcto;								
							porcentajeVarDescuento[indice][indiceMp]=new Double(clave[clave.length-1]);
							posVector++;
						}
					}
					
					//se crea el opDescuentos uniendo los descuentos normales con los especiales
					String[] opDescuentosNormales = (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
					if(opDescuentosNormales != null && opDescuentosNormales.length > 0){
						String[] nuevoOpDescuento = new String[opDescuento.length + opDescuentosNormales.length];
						for(int i=0; i<opDescuentosNormales.length; i++)
							if(opDescuentosNormales[i] != null && !opDescuentosNormales[i].contains(CODIGO_GERENTE_COMERCIAL))
							nuevoOpDescuento[i]=opDescuentosNormales[i];
						
						int posicionVector = opDescuentosNormales.length;
												
						for(int i=0; i<opDescuento.length; i++){
							Boolean encontroLlave = Boolean.FALSE;
							for(int j=0; j<nuevoOpDescuento.length; j++){
								if(nuevoOpDescuento[j] != null && opDescuento[i].contains(nuevoOpDescuento[j])){
									nuevoOpDescuento[j] = opDescuento[i];
									encontroLlave = Boolean.TRUE;
									break;
								}
							}
							if(!encontroLlave){
								nuevoOpDescuento[posicionVector]=opDescuento[i];
								posicionVector++;
							}
						}
						//se buscan los nulos
						int contadorNulls = 0;
						for(int i=0; i<nuevoOpDescuento.length; i++)
							if(nuevoOpDescuento[i] == null || nuevoOpDescuento.equals(""))
								contadorNulls++;
						
						//se eliminan los nulos del vector nuevoOpDescuento
						if(contadorNulls != 0){
							String[] opDescuentosSinNulls = new String[nuevoOpDescuento.length-contadorNulls];
							int pos = 0;
							for(int i=0; i<nuevoOpDescuento.length; i++){
								if(nuevoOpDescuento[i] != null && !nuevoOpDescuento[i].equals("")){
									opDescuentosSinNulls[pos] = nuevoOpDescuento[i];
									pos++;
								}
							}
							nuevoOpDescuento = opDescuentosSinNulls;							
						}		
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, nuevoOpDescuento);
						formulario.setOpDescuentos(nuevoOpDescuento);												
					}
					else{
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuento);
						formulario.setOpDescuentos(opDescuento);						
					}					
					formulario.setPorcentajeVarDescuento(porcentajeVarDescuento);	
					request.getSession().setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuento);
				}
			}
			catch (Exception e) {
				LogSISPE.getLog().error("error al asignar los campos de descuentos al formulario {}",e);
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al verificar las autorizaciones variables {}",e);
			throw new Exception(e);
		}		
	}
	
	
	/**
	 * Suma los detalles que pertenecen al mismo comprador
	 * @param request
	 * @param llaveDescuento
	 * @return
	 * @throws Exception
	 */
	public static Double sumarDetallesDelMismoDepartamento(HttpServletRequest request, String llaveDescuento) throws Exception{
		Double sumaPedidosDepartamento = 0D;
		HttpSession session = request.getSession();
		try{
			LogSISPE.getLog().info("sumando detalles del mismo departamento para la llave "+llaveDescuento);
			
			//se obtiene de sesion el detallePedido
			ArrayList<DetallePedidoDTO> detallePedidoCol=(ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isEmpty(detallePedidoCol)){
				//si ingresa es porque no tiene pedidos consolidados
				detallePedidoCol =	(ArrayList<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			}
			
			//se obtiene de sesion la coleccion de clasificaciones de pavos
			Collection<String> codigosClasificacionesPAvosCol = (Collection<String>) session.getAttribute(CotizarReservarAction.CLASIFICACIONES_PAVOS_SELECCIONADOS_COL);
			
			if(detallePedidoCol != null && !detallePedidoCol.isEmpty()){
				
				String codigoClasificacionPadre =  obtenerCodigoClasificacionPadreDeLlaveDescuento(llaveDescuento);
				String codigoAutorizador = obtenerCodigoAutorizadorDeLlaveDescuento(llaveDescuento);

				//se recorren los detalles del pedido
				for(DetallePedidoDTO detalleActual : detallePedidoCol){
					if(StringUtils.isEmpty(detalleActual.getNpIdAutorizador())){
						//se consulta el funcionario autorizador
						FuncionarioDTO funcionarioAutorizador = obtenerFuncionarioAutorizadorPorClasificacion(obtenerCodigoClasificacion
								(detalleActual),obtenerTipoMarca(detalleActual), request, obtenerCodigoProcesoAutorizarDescVar(request), new ActionMessages());
						
						if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
							detalleActual.setNpIdAutorizador(funcionarioAutorizador.getUsuarioFuncionario());
						}
					}
					
					//se comparan las clasificaciones y los autorizadores de cada clasificacion
					if(StringUtils.isNotEmpty(detalleActual.getNpIdAutorizador()) && codigoClasificacionPadre.equals(obtenerCodigoDepartamento(detalleActual))
							&& detalleActual.getNpIdAutorizador().equals(codigoAutorizador)){
						
						//se verifica que no tenga descuento automatico de pavos
						Boolean tieneDescAutomatico = Boolean.FALSE;
						if(codigosClasificacionesPAvosCol != null && !codigosClasificacionesPAvosCol.isEmpty()){
							for(String codigoclasificacionPAvosActual : codigosClasificacionesPAvosCol){
								if(detalleActual.getArticuloDTO().getCodigoClasificacion().equals(codigoclasificacionPAvosActual)){
									tieneDescAutomatico = Boolean.TRUE;		
									break;
								}
							}
						}
						if(!tieneDescAutomatico){
							sumaPedidosDepartamento += detalleActual.getEstadoDetallePedidoDTO().getValorTotalEstadoNeto();								
						}
					}
				}					
			}
			return  Util.roundDoubleMath(sumaPedidosDepartamento, 2);
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al sumar los detalles del mismo comprador {}",e);	
			return sumaPedidosDepartamento;
		}
		
	}
	
	/**
	 * Este metodo se usa cuando recotiza o reserva un pedido saber en que estado se encuentran las autorizaciones 
	 * @param formulario
	 * @param request
	 * @param errors
	 * throws Exception
	 */
	public static void verificarEstadoAutorizaciones( CotizarReservarForm formulario, HttpServletRequest request, ActionMessages errors ) throws Exception{		
		try{
			LogSISPE.getLog().info("ingresa al metodo : verificarEstadoAutorizaciones");
			//Tiene precios alterados, primero actualiza los precios despues deben procesarse los descuentos y autorizaciones
			if(request.getSession().getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS) == null){
				request.getSession().setAttribute(CotizarReservarAction.PRIMER_INGRESO_AL_PEDIDO, "ok");
				request.getSession().removeAttribute(ACCION_ANTERIOR_AUTORIZACION);

				Boolean mostrarPopAutorizacionesStock = (Boolean) ((request.getSession().getAttribute(MOSTRAR_POPUP_AUTORIZACION_STOCK) != null) ? request.getSession().getAttribute(MOSTRAR_POPUP_AUTORIZACION_STOCK) : Boolean.TRUE);
				//primero se muestra la autorizacion de stock si existen autorizaciones pendientes
				if(mostrarPopAutorizacionesStock && verificarExistenAutorizacionesStockPendientes(request, null)){
					
					Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK);
					if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
						mostrarPopUpAutorizacionesPorTipo(request, Boolean.TRUE, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, true, null);
						return;
					}
				}
				
				request.getSession().removeAttribute(MOSTRAR_POPUP_AUTORIZACION_STOCK);
				request.getSession().removeAttribute(CotizarReservarAction.PRIMER_INGRESO_AL_PEDIDO);
				ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
				ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesAux = new ArrayList<EstadoPedidoAutorizacionDTO>();
				if(colaAutorizaciones != null && !colaAutorizaciones.isEmpty()){
					for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
						if(!autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
							colaAutorizacionesAux.add(autorizacionActual);
						}
					}	
					if(!colaAutorizacionesAux.isEmpty()){
						String urlComponenteAutorizaciones =  mostrarPopUpAutorizaciones(colaAutorizacionesAux,request, formulario, true);
						if(StringUtils.isNotEmpty(urlComponenteAutorizaciones)){
							request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, urlComponenteAutorizaciones);
						}else{
							errors.add("errorMostrarComponenteAut", new ActionMessage("errors.gerneral","Error al mostrar el componente de autorizaciones"));
						}
					}
				}				
			}
		}
		catch (Exception ex) {
			LogSISPE.getLog().error("Error al verificar el estado de las autorizaciones {}",ex);
			errors.add("verificarAutorizaciones", new ActionMessage("errors.verificar.estado.autorizaciones"));
		}		
	}
	
	
	/**
	 * cuando existen datos en el vector opDescuentos del formulario, ordena los compradores de acuerdo al indice de la llave para que 
	 * siempre salgan seleccionados los checks del popUp descuentos
	 * @param listaDepartamentos
	 * @param request
	 * @throws Exception
	 */
	public static void  ordenarCompradores(Collection<ClasificacionDTO> listaDepartamentos, HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		Double[][] porcentajeVarDescuentos = new Double[listaDepartamentos.size()][2];
		
		try{
			
			LogSISPE.getLog().info("ordenando los departamentos");
			String[] opDescuentos = (String[]) session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);	
			opDescuentos = CotizacionReservacionUtil.eliminarLlavesRepetidas(opDescuentos);
			
			inicializarPorcentajesDescVar(porcentajeVarDescuentos);
			
			if(opDescuentos != null && opDescuentos.length > 0){
				
				ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES);
				
				//se valida que las llaves de descuentos variables tengan indices
				int posDctoVariable = 0;
				for(int i=0; i<opDescuentos.length; i++){
					String opDcto = opDescuentos[i];
					if(opDcto != null && opDcto.contains(CODIGO_GERENTE_COMERCIAL)){						
						if(!opDcto.contains(INDICE_DESCUENTO_GENERAL)){
							//se busca el indice que viene de la cola de autorizaciones
							if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
								//se recorren las autorizaciones
								for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizaciones){
									String[] vect = autorizacion.getValorReferencial().split(SEPARADOR_TOKEN);
									String valorRef1 = vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3]+SEPARADOR_TOKEN+vect[4].substring(vect[4].length()-1);
									
									if(valorRef1.equals(opDcto.replace(opDcto.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO))){
										//7-CTD3-CMDMON-COM04-INX0-AUTORIZACION DESCUENTO VARIABLE PERECIBLES-5.0
//										opDcto+= SEPARADOR_TOKEN+autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[4];
										opDcto= opDcto.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[1]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[2]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[3]+SEPARADOR_TOKEN+autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[4];
										opDescuentos[i] = opDcto;
									}
								}
							} else{
								opDcto= opDcto.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[1]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[2]+SEPARADOR_TOKEN+opDcto.split(SEPARADOR_TOKEN)[3]+SEPARADOR_TOKEN+INDICE_DESCUENTO_GENERAL+posDctoVariable+opDcto.split(SEPARADOR_TOKEN)[4];
//								opDcto+= SEPARADOR_TOKEN+INDICE_DESCUENTO_GENERAL+posDctoVariable;
								opDescuentos[i] = opDcto;
							}
						}						
						posDctoVariable++;
					}
				}
				
				//si existen descuentos variables se ordena los departamentos
				if(posDctoVariable != 0){
					
//					inicializarPorcentajesDescVar(porcentajeVarDescuentos);
					
					//se obtiene de sesion el detalle pedido
					ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					
					ArrayList<ClasificacionDTO> departamentos  = new ArrayList<ClasificacionDTO>(listaDepartamentos);
					
					//se recorren las llaves de los descuentos
									
					for(int i=0;i<departamentos.size();i++){
						for(int j=0;j<opDescuentos.length;j++){
							
							if(opDescuentos[j] != null && !opDescuentos[j].equals("")){
								String[] vectorOpDescuento = opDescuentos[j].split(SEPARADOR_TOKEN);
								//solo para el caso de descuentos variables
								if(vectorOpDescuento.length > 4 && opDescuentos[j].contains(CODIGO_GERENTE_COMERCIAL)){
									
									//7-CTD3-CMDMON-COM01-INX1
									String codigoClasificacionPadre = obtenerCodigoClasificacionPadreDeLlaveDescuento(opDescuentos[j]);
									String codigoAutorizador = obtenerCodigoAutorizadorDeLlaveDescuento(opDescuentos[j]);
									
									int indice = new Integer(vectorOpDescuento[4].split(INDICE_DESCUENTO_GENERAL)[1].substring(0,vectorOpDescuento[4].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
									int indiceMp = new Integer(vectorOpDescuento[4].split(INDICE_DESCUENTO_GENERAL)[1].substring(vectorOpDescuento[4].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
									
									//se busca el departamento
									if(!codigoClasificacionPadre.equals("")){
														
										if(departamentos.get(i).getId().getCodigoClasificacion().equals(codigoClasificacionPadre)){
											if(i!=indice){
												String llaveCambiada  =  opDescuentos[j];
												String indiceAnterior = llaveCambiada.split(SEPARADOR_TOKEN)[4].substring(0,llaveCambiada.split(SEPARADOR_TOKEN)[4].length()-1);
												String indiceNuevo = INDICE_DESCUENTO_GENERAL+i;
												llaveCambiada = llaveCambiada.replace(indiceAnterior, indiceNuevo);
												opDescuentos[j] = llaveCambiada;											
												
												//Se cambia el indice de los detalles que tienen esa llave											
												if(CollectionUtils.isNotEmpty(detallePedido)){
													for(DetallePedidoDTO detalleActual : detallePedido){
														if(detalleActual != null && CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
															DetalleEstadoPedidoAutorizacionDTO autorizacion = detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().iterator().next();
//															if(codigoClasificacionPadre.equals(detalleActual.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre())){
															if(StringUtils.isNotEmpty(detalleActual.getNpIdAutorizador()) && codigoAutorizador.equals(detalleActual.getNpIdAutorizador())
																	&& codigoClasificacionPadre.equals(obtenerCodigoClasificacion(detalleActual))){
																autorizacion.getEstadoPedidoAutorizacionDTO().setValorReferencial(autorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().replace(indiceAnterior, indiceNuevo));
															}
														}
													}
												}
												
												//se cambia el indice de la cola de autorizaciones											
												if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
													for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizaciones){
														if(autorizacion.getValorReferencial().contains(CODIGO_GERENTE_COMERCIAL+codigoClasificacionPadre+SEPARADOR_DOS_PUNTOS+codigoAutorizador)){
															autorizacion.setValorReferencial(autorizacion.getValorReferencial().replace(indiceAnterior, indiceNuevo));
															porcentajeVarDescuentos[i] [indiceMp]= new Double(autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[autorizacion.getValorReferencial().split(SEPARADOR_TOKEN).length-1]);
														}													
													}
												}
											}else{
												if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
													for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizaciones){
														if(autorizacion.getValorReferencial().contains(CODIGO_GERENTE_COMERCIAL+codigoClasificacionPadre+SEPARADOR_DOS_PUNTOS+codigoAutorizador)
																&& autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[4].substring(autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[4].length()-1).equals(Integer.toString(indiceMp))){
															porcentajeVarDescuentos[i][indiceMp] = new Double(autorizacion.getValorReferencial().split(SEPARADOR_TOKEN)[autorizacion.getValorReferencial().split(SEPARADOR_TOKEN).length-1]);
														}													
													}
												}
											}
										}	
									}
								}
							}
						}
					}				
					session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedido);
					session.setAttribute(COLA_AUTORIZACIONES, colaAutorizaciones);
					session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuentos);
					session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
					session.setAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO, departamentos);

				}
				else{
					session.setAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO, listaDepartamentos);
					//si no existen descuento variables se eliminan los porcentajes
					if(porcentajeVarDescuentos != null && porcentajeVarDescuentos.length > 0){
						//se inicializa el vector de porcentajes
//						inicializarPorcentajesDescVar(porcentajeVarDescuentos);
						session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuentos);
					}
				}
			}
			else{
				session.setAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO, listaDepartamentos);
				
				//si no existen descuento variables se eliminan los porcentajes
				if(porcentajeVarDescuentos != null && porcentajeVarDescuentos.length > 0){
//					inicializarPorcentajesDescVar(porcentajeVarDescuentos);
					session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuentos);
				}
			}
				
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al ordenar los compradores del pedido {}",e);	
			inicializarPorcentajesDescVar(porcentajeVarDescuentos);
			session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuentos);
			session.setAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO, listaDepartamentos);
		}
	}

	public static void inicializarPorcentajesDescVar(
			Double[][] porcentajeVarDescuentos) {
		for(int i=0; i<porcentajeVarDescuentos.length; i++){
			porcentajeVarDescuentos[i][0]= 0D;
			porcentajeVarDescuentos[i][1]= 0D;
		}
	}
	
	
	/**
	 * Elimina de los detalles, la colaAutorizaciones y los descuentos, las autorizaciones que fueron DESELECCIONADAS del opDescuentos
	 * @param detallePedidoCol
	 * @param request
	 * @param opDescuentos
	 * @throws Exception
	 */
	public static void eliminarAutorizacionesNoSeleccionadas(ArrayList<DetallePedidoDTO> detallePedidoCol, 
			HttpServletRequest request, String[] opDescuentos ) throws Exception{		
		try{
		
			LogSISPE.getLog().info("Eliminando autorizaciones no seleccionadas en opDescuentos");	
			
			//se desactivan las autorizaciones que fueron eliminadas
			Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
			if(CollectionUtils.isEmpty(autorizacionesDesactivarCol)){
				autorizacionesDesactivarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
			}
			
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
			ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
			if(CollectionUtils.isNotEmpty(detallePedidoCol) && CollectionUtils.isNotEmpty(colaAutorizaciones)){
				
				if(opDescuentos != null && opDescuentos.length > 0){
					ArrayList<DetallePedidoDTO> colDetallePedidoDTO =  new ArrayList<DetallePedidoDTO>();
					ArrayList<DetallePedidoDTO> colDetallePedidoDTOAux = new ArrayList<DetallePedidoDTO>();
					
					//se separan los detalles con autorizaciones
					for(DetallePedidoDTO detalleActual : detallePedidoCol){
						if(detalleActual.getEstadoDetallePedidoDTO() != null && CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){				
							colDetallePedidoDTO.add(detalleActual);	
							colDetallePedidoDTOAux.add(detalleActual);
						}
					}
										
	
					//se separan los detalles que si estan seleccionados
					for(DetallePedidoDTO detalleActual : colDetallePedidoDTOAux){
						Boolean encontroLlave = Boolean.FALSE;
						for(int i=0; i<opDescuentos.length; i++){
							// se busca si la llave del detalle actual esta seleccionada
							if(opDescuentos[i] != null && opDescuentos[i].split(SEPARADOR_TOKEN).length >3){
								for(DetalleEstadoPedidoAutorizacionDTO  autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
									//solo para el caso de descuentos variable
									if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
										 autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
										
										String[] vect = opDescuentos[i].split(SEPARADOR_TOKEN);
										String valorRef1 = SEPARADOR_TOKEN+vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3]+SEPARADOR_TOKEN+vect[4].substring(vect[4].length()-1);
										
										vect = autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN);
										String valorRef2 = SEPARADOR_TOKEN+vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3]+SEPARADOR_TOKEN+vect[4].substring(vect[4].length()-1);
										
//										LogSISPE.getLog().info("valor referencial  {}, llave opDescuento {}",autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial(), valorRef);
										if(valorRef1.equals(valorRef2)){
											encontroLlave = Boolean.TRUE;
											break;			
										}
									}
								}																					
							}
						}
						if(encontroLlave)
							colDetallePedidoDTO.remove(detalleActual);
						
					}
					
					//para el caso que haya detalles con autorizaciones que no estan seleccionados
					if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){
						Set<String> llavesEliminar = new HashSet<String>();
						for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
							detallePedidoCol.remove(detalleActual);
							Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminarCol =  new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
							for(DetalleEstadoPedidoAutorizacionDTO  autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
								//solo para el caso de descuentos variable
								if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
									 autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
									colaAutorizaciones.remove(autorizacionActual.getEstadoPedidoAutorizacionDTO());
									autorizacionesDesactivarCol.add(autorizacionActual);
									llavesEliminar.add(autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial());
									autorizacionesEliminarCol.add(autorizacionActual);
								}
							}
							LogSISPE.getLog().info("Eliminando {} autorizacion(es) de descuento variable del articulo: {}",autorizacionesEliminarCol.size() ,detalleActual.getArticuloDTO().getDescripcionArticulo());
							detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminarCol);
							if(CollectionUtils.isEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
							}
							detallePedidoCol.add(detalleActual);

						}
						//se buscan los descuentos que tienen la llave por eliminar 					
						if(CollectionUtils.isNotEmpty(llavesEliminar) && CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
							for(String llaveActual : llavesEliminar){
								//[6-CTD3-CMDCAN-COM04-INX1-AUTORIZACION DESCUENTO VARIABLE JORGE HERNANDEZ-9.0]
								String[] vect = llaveActual.split(SEPARADOR_TOKEN);
								String nuevaLlave = vect[1]+SEPARADOR_TOKEN+vect[2]+SEPARADOR_TOKEN+vect[3];	
								for(DescuentoEstadoPedidoDTO desctoActual : colDescuentoEstadoPedidoDTOVariables){
									if(desctoActual.getLlaveDescuento() != null && desctoActual.getLlaveDescuento().contains(nuevaLlave)){
										colDescuentoEstadoPedidoDTOVariables.remove(desctoActual);
										break;
									}
								}
							}
							//se sube a sesion los descuentos modificados y la colaAutorizaciones
							request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES, colDescuentoEstadoPedidoDTOVariables);
							request.getSession().setAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES, colaAutorizaciones);
						}
					}
				}			
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al eliminar los las autorizaciones deseleccionadas {}",e);
		}
	}
	
	/**
	 * Crea los tab de detalle comun del pedido y el tab de detalle de las autorizaciones del pedido
	 * @param request
	 * @return Tabs con los datos del contacto(persona o Empresa) y el tab con el detalle de los pesos
	 */
	public static PaginaTab construirTabsDetalleAutorizacion(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO) {
		PaginaTab tabDetallePedidoAutorizaciones = null;		
		try {					
			tabDetallePedidoAutorizaciones = new PaginaTab("detalleEstadoPedido", "deplegar", 0,340, request);
			Boolean autStock = false;	
			Boolean autDctoVar= false;
			Boolean entregas= false;
			Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
			Tab tabDetalleAutorizaciones = null;
			Tab tabDetalleAutorizacionesStock = null;
			Tab tabDescuentosPorArticulo = null;
			
			//para verificar si tiene autorizaciones de stock
			if(StringUtils.isNotEmpty(vistaPedidoDTO.getTieneAutorizacionStock()) && vistaPedidoDTO.getTieneAutorizacionStock().equals(ConstantesGenerales.ESTADO_SI)){
				tabDetalleAutorizacionesStock = new Tab("Detalle aut. stock", "detalleEstadoPedido", "/servicioCliente/autorizacion/detalleAutorizacionStockPedido.jsp", false);						
				autStock = true;
				vistaPedidoDTO.setTieneAutorizacion(ConstantesGenerales.ESTADO_SI);
			}
			
			//para obtener el estado de la autorizaci\u00F3n de stock
			for(VistaDetallePedidoDTO vistaDetallePedidoDTO : (Collection<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
				if(vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()!=null){					
					for(DetalleEstadoPedidoAutorizacionDTO detalleEstadoPedidoAutorizacionDTO : vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){						
						
						if(detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
							//se consulta la autorizacion padre				
							ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = AutorizacionesFactory.getInstancia().
									getaIAutorizacionesServicio().transBuscarAutorizacionPorId(detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());
							
							//si la autorizacion tiene autorizaciones hijas
							if(autorizacionDTO != null && CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionesHijas())){
								
								//codigos de los componentes de autorizaciones usados para detalles de cada articulo hijo
								Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
								Long codigoComponenteRadiodArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.radio.articulo"));//101
								
								LogSISPE.getLog().info("la autorizacion "+autorizacionDTO.getId().getCodigoAutorizacion()+" tiene "+autorizacionDTO.getAutorizacionesHijas().size()+
										" autorizacion(es) hija(s).");
								//se recorren las autorizaciones hijas
								for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionHijaActual : autorizacionDTO.getAutorizacionesHijas()){									
									if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getValoresComponente())){
										String codigoArticulo = obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
										//el estado de la autorizacion  puede tener los siguientes estados 0=RECHAZADA, 1=APROBADA, P=PENDIENTE
										String valorComponente = obtenerValorComponentePorID(codigoComponenteRadiodArt, autorizacionHijaActual);
										char estadoAutorizacion = ' ';
										if(StringUtils.isNotEmpty(valorComponente)){
											estadoAutorizacion= valorComponente.charAt(0);
										}										
										//se recorren los detalles para buscar el detalle autorizado
										if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(codigoArticulo)){
											//estado 0 = rechazada, 1 = autorizada, P = pendiente, ' ' = pendiente
											switch (estadoAutorizacion) {
											//rechazado
											case '0':
												LogSISPE.getLog().info("la autorizacion del articulo "+vistaDetallePedidoDTO.getId().getCodigoArticulo()+" - "+vistaDetallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+
														" fue RECHAZADA");
													detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_RECHAZADA);
												break;
											//autorizado
											case '1':
												LogSISPE.getLog().info("la autorizacion del articulo "+vistaDetallePedidoDTO.getId().getCodigoArticulo()+" - "+vistaDetallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+
														" fue APROBADA");
												detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_APROBADA);
												break;
											//pendiente	
											case 'P':
												LogSISPE.getLog().info("la autorizacion del articulo "+vistaDetallePedidoDTO.getId().getCodigoArticulo()+" - "+vistaDetallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+
														" esta pendiene por gestionar");
												detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_SOLICITADA);
												break;
											//sin estado	
											case ' ':
												LogSISPE.getLog().info("la autorizacion del articulo "+vistaDetallePedidoDTO.getId().getCodigoArticulo()+" - "+vistaDetallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+
														" esta pendiene por gestionar");
												detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_SOLICITADA);
												break;
											}
										}	
									}
								}								
							}else{
								LogSISPE.getLog().info("la autorizacion se gestion\u00F3 con un n\u00FAmero de autorizaci\u00F3n");
								detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_APROBADA);
								
							}
						}
					}
				}
				if(CollectionUtils.isNotEmpty(vistaDetallePedidoDTO.getEntregaDetallePedidoCol())){
					entregas=true;
				}
			}
			
			//para verificar si tiene autorizaciones de descuento variable
			if(vistaPedidoDTO.getTieneAutorizacionDctVar() != null &&  vistaPedidoDTO.getTieneAutorizacionDctVar().equals("SI")){
				tabDetalleAutorizaciones = new Tab("Detalle aut. dscto. variable", "detalleEstadoPedido", "/servicioCliente/autorizacion/detalleAutorizacionEstadoPedido.jsp", false);
				tabDescuentosPorArticulo = new Tab("Descuentos por art\u00EDculo", "detalleEstadoPedido", "/servicioCliente/autorizacion/descuentosPorArticuloEstadoPedido.jsp", false);
				vistaPedidoDTO.setTieneAutorizacion("SI");
				autDctoVar = true;
			}
			
			tabDetallePedidoAutorizaciones.addTab(tabDetallePedidoComun);
			if(entregas){
				Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
				tabDetallePedidoAutorizaciones.addTab(tabDetalleEntregas);
			}	
			if(autDctoVar){
				tabDetallePedidoAutorizaciones.addTab(tabDetalleAutorizaciones);
			}
			if(autStock){
				tabDetallePedidoAutorizaciones.addTab(tabDetalleAutorizacionesStock);
				vistaPedidoDTO.setTieneAutorizacionStock(ConstantesGenerales.ESTADO_SI);
			}
			if(autDctoVar){
				tabDetallePedidoAutorizaciones.addTab(tabDescuentosPorArticulo);
			}	
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs {}", e);
		}

		return tabDetallePedidoAutorizaciones;
	}
	
	
	
	/**
	 * Metodo que elimina una autorizacion en articulos del pedido
	 * @param codigo
	 * @param estadoAutAprobada
	 * @param request
	 */
	private static void eliminarAutorizacionDetalle(Long codigo, EstadoPedidoAutorizacionDTO detalle, HttpServletRequest request, Long codigoTipoAutorizacion) {
		LogSISPE.getLog().info("eliminando la autorizacion {}", codigo);
		
		ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		if(CollectionUtils.isNotEmpty(detallePedidoCol)){
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoCol){
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){	
					
					//las autorizacion que seran eliminadas
					Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
					for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacionesDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						//solo para el caso de descuentos variable
						if(detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
								detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
							
							//se comparan los datos
							if( (detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion()!=null &&
									detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getId().getCodigoEstadoPedidoAutorizacion().longValue() == codigo) 
									|| (detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNumeroProceso().equals(detalle.getNumeroProceso())
									&& detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().longValue() == detalle.getCodigoAutorizacion())){
								autorizacionesEliminar.add(detalleAutorizacionesDTO);
							}
						}
					}
					//se eliminan las autorizaciones
					detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
					if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
					}
				}
			}
		}
	}
	
	
	/**
	 * Metodo que envia la informaci\u00F3n al sistema de autorizaciones y arma la url para iframe del popup resultante
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static void mostrarPopUpAutorizacionesPorTipo(HttpServletRequest request, Boolean mostrarBotonCancelar, Long codigoTipoAutorizacion, 
			Boolean mostrarAutorizacionesPendientes, ActionMessages warnings) throws Exception{
		LogSISPE.getLog().info("Mostrando popup mostrarPopUpAutorizacionesPorTipo");		
		
		URLSystemConnection urlSystemConnection = new URLSystemConnection();
		//evitar que se bloquee la pantalla despues de mostrar el componente de autorizaciones
		urlSystemConnection.setScriptParameters(new HashMap<String, Object>());
		urlSystemConnection.getScriptParameters().put("evalScripts", Boolean.TRUE);
		urlSystemConnection.setFormActionURL("crearCotizacion.do");
		urlSystemConnection.setFormName("cotizarRecotizarReservarForm");
		urlSystemConnection.setRenderSection(new ArrayList<String>());
		urlSystemConnection.getRenderSection().add("\'div_pagina\'");
		urlSystemConnection.getRenderSection().add("\'pregunta\'");
		urlSystemConnection.getRenderSection().add("\'seccion_detalle\'");
		urlSystemConnection.setViewParameters(new HashMap<String, Object>());		
		urlSystemConnection.getViewParameters().put("backgroundColor", "#F4F5EB");
		
		//cerrar popUp sobre Popup
		if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)
				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL)
				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS)){
			
			urlSystemConnection.getViewParameters().put("cerrarPopUp", "parent.ocultarModalID('frameModal2');");
			urlSystemConnection.getRenderSection().add("\'mensajesPopUp\'");
		}else{
			urlSystemConnection.getViewParameters().put("cerrarPopUp", "parent.ocultarModal();");
			urlSystemConnection.getRenderSection().add("\'mensajes\'");
		}
		
		//cuando es solo para una autorizacion se reduce el tama\u00F1o, por defecto es 465px
		urlSystemConnection.getViewParameters().put("componentHeight", "270px;");
		request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "370px");
		request.getSession().setAttribute(CODIGO_TIPO_AUTORIZACION, codigoTipoAutorizacion.toString());
				
		//por defecto es true 
		if(!mostrarBotonCancelar){
			urlSystemConnection.getViewParameters().put("showCancelButton", "false;");	
		}
		
		//para el caso de autorizaciones de stock
		if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
			
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK);
			
			//se procesa solo si existen datos
			if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
				
				Boolean mostrarBotonSolicitarTodos = Boolean.TRUE;
				
				Collection<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();
				for(EstadoPedidoAutorizacionDTO autorizacionPendienteDTO : colaAutorizacionesStock){
					
					//cuando se muestra solo para aplicar autorizacion se oculta el boton solicitarTodos
					if(autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial() != null 
							&& autorizacionPendienteDTO.getNpAutorizacion().getEstadoInicial().equals(AutorizacionEstado.AUTORIZADA.getEstado())){
						mostrarBotonSolicitarTodos = Boolean.FALSE;
					}
					
					//si no existe filtroTipoAutorizacion se inicializa
					if(autorizacionPendienteDTO.getNpAutorizacion().getFiltroTipoAutorizacion() == null){
						HashMap<Long, ValorComponente[]> filtroTipoAutorizacion = new  HashMap<Long, ValorComponente[]>();
						filtroTipoAutorizacion.put(codigoTipoAutorizacion, new ValorComponente[0]);
						autorizacionPendienteDTO.getNpAutorizacion().setFiltroTipoAutorizacion(filtroTipoAutorizacion);
					}
					
					//se van a mostrar todas las autorizaciones menos las ya aprobadas y dependiendo del caso las pendientes para la pantalla de resumen
					if(!colaAutorizacionesStock.contains(autorizacionPendienteDTO.getNpAutorizacion()) 
							&&  !autorizacionPendienteDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) && (!autorizacionPendienteDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA)
									|| verificarExisteAutorizacionStockPendientePorDetalle(autorizacionPendienteDTO, request))){
						if(mostrarAutorizacionesPendientes){
							autorizacionCol.add(autorizacionPendienteDTO.getNpAutorizacion());
						}else if(autorizacionPendienteDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
							autorizacionCol.add(autorizacionPendienteDTO.getNpAutorizacion());
						}
					}
				}
				
				//por defecto es true
				if(!mostrarBotonSolicitarTodos){
					urlSystemConnection.getViewParameters().put("showSolicitarTodos", "false");
				}
				
				//se valida que exista autorizaciones para mostrar 
				if(CollectionUtils.isNotEmpty(autorizacionCol)){
					//cuando es solo para una autorizacion se reduce el tama\u00F1o, por defecto es 465px
					if(autorizacionCol.size() == 1){
						urlSystemConnection.getViewParameters().put("componentHeight", "300px;");
						request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "400px");
					}else{
						urlSystemConnection.getViewParameters().put("componentHeight", "400px;");
						request.getSession().setAttribute(ALTO_POPUP_AUTORIZACIONES, "500px");
					}
					
					//se setea el color distintivo de autorizacion de stock
					urlSystemConnection.getViewParameters().put("backgroundColor", "#ffedd2");
					//se guarda el JSON
					String url = guardarJson(urlSystemConnection, request, autorizacionCol);
					
					request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, url);
					request.getSession().setAttribute(ACCION_ANTERIOR_AUTORIZACION, "aceptarAutorizacion");
					request.getSession().setAttribute(CotizarReservarAction.ACEPTAR_DESCUENTO_VARIABLE, "ok");
				}else{
					LogSISPE.getLog().info("No se muestra el componente de autorizaciones porque no existen datos para mostrar");
				}
			}
		} 
		//para el caso de autorizacion para consolidar mas de 5 pedidos
		else if(codigoTipoAutorizacion.equals(ConstantesGenerales.getInstancia().TIPO_AUTORIZACION_CONSOLIDACION)){

			String usuarioAutorizadorConsolidacion="";
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.id.usuario.funcionario.autoriza.consolidacion", request);
			usuarioAutorizadorConsolidacion = parametroDTO.getValorParametro();	
			EstadoPedidoAutorizacionDTO autorizacionPendiente = construirEstadoPedidoAutorizacion(null, request, 1L,codigoTipoAutorizacion,
					"AUTORIZACI\u00F3N PARA CONSOLIDAR PEDIDOS"+consultarNombreUsuario(usuarioAutorizadorConsolidacion), new String[] {usuarioAutorizadorConsolidacion}); 
			Collection<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();
			autorizacionCol.add(autorizacionPendiente.getNpAutorizacion());
			
			//se guarda el JSON
			String url = guardarJson(urlSystemConnection, request, autorizacionCol);
			//variables necesarias para levantar el popUp
			request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, url);
			request.getSession().setAttribute("autorizaGerComConsolidacion", "ok");
			request.getSession().setAttribute(ACCION_ANTERIOR_AUTORIZACION, "autorizaGerComConsolidacion");
		} 
		//para el caso de autorizacion para disminuir la fecha minima de entrega
		else if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)){

			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.disminuir.fecha.min.entrega", request);
			String usuarioAutorizadorDisminuirFechaMinima = parametroDTO.getValorParametro();
			
			EstadoPedidoAutorizacionDTO autorizacionPendiente = construirEstadoPedidoAutorizacion(null, request, 1L,codigoTipoAutorizacion,
					"AUTORIZACI\u00D3N PARA DISMINUIR LA FECHA M\u00CDNIMA DE ENTREGA"+consultarNombreUsuario(usuarioAutorizadorDisminuirFechaMinima), new String[] {usuarioAutorizadorDisminuirFechaMinima}); 
			Collection<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();
			autorizacionCol.add(autorizacionPendiente.getNpAutorizacion());

			urlSystemConnection.setFormActionURL("entregaLocalCalendario.do");
			
			//se guarda el JSON
			String url = guardarJson(urlSystemConnection, request, autorizacionCol);
			//variables necesarias para levantar el popUp
			request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, url);
		}
		//para el caso de autorizacion para reservar bodega de local
		else if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL)){

			CotizarReservarForm formulario = (CotizarReservarForm)request.getSession().getAttribute(CotizarReservarAction.FORMULARIO_TEMPORAL);
			String codigolocal = formulario.getLocal() != null ? formulario.getLocal() : SessionManagerSISPE.getCodigoLocalObjetivo(request).toString(); 
			String[] usuariosAutorizadores = obtenerAutorizadorDeLocal(codigolocal, codigoTipoAutorizacion, request);
			request.getSession().removeAttribute(CotizarReservarAction.FORMULARIO_TEMPORAL);
			if(usuariosAutorizadores != null && usuariosAutorizadores.length>0){
				EstadoPedidoAutorizacionDTO autorizacionPendiente = construirEstadoPedidoAutorizacion(null, request, 1L,codigoTipoAutorizacion,
						"AUTORIZACI\u00D3N PARA RESERVAR BODEGA LOCAL", usuariosAutorizadores);
				
				Collection<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();
				autorizacionCol.add(autorizacionPendiente.getNpAutorizacion());
				urlSystemConnection.setFormActionURL("entregaLocalCalendario.do");
				
				//se guarda el JSON
				String url = guardarJson(urlSystemConnection, request, autorizacionCol);
				//variables necesarias para levantar el popUp
				request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, url);
			}else{
				LogSISPE.getLog().info("El local seleccionado no tiene un usuario autorizador");
				if(warnings != null){
					warnings.add("noExisteLocal", new ActionMessage("warning.local.sin.usuario.autorizador"));
				}
			}
		}
		//autorizacion para que la bodega sea quien elabora los canastos especiales en pedidos consolidados
		else if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS.longValue()){
			
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.disminuir.fecha.min.entrega", request);
			String usuarioAutorizadorDisminuirFechaMinima = parametroDTO.getValorParametro();
			
			EstadoPedidoAutorizacionDTO autorizacionPendiente = construirEstadoPedidoAutorizacion(null, request, 1L, codigoTipoAutorizacion,
					"AUTORIZACI\u00D3N PARA QUE EL CD ELABORE LOS CANASTOS"+consultarNombreUsuario(usuarioAutorizadorDisminuirFechaMinima), new String[] {usuarioAutorizadorDisminuirFechaMinima}); 
			Collection<Autorizacion> autorizacionCol = new ArrayList<Autorizacion>();
			autorizacionCol.add(autorizacionPendiente.getNpAutorizacion());

			urlSystemConnection.setFormActionURL("entregaLocalCalendario.do");
			
			//se guarda el JSON
			String url = guardarJson(urlSystemConnection, request, autorizacionCol);
			//variables necesarias para levantar el popUp
			request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, url);
		}
	}
	
	/**
	 * En base al userId busca el usuario y retorna el nombre completo
	 * @param idUsuario
	 * @return
	 */
	private static String consultarNombreUsuario(String idUsuario){
		//se consultan el nombre del funcionario
    	UserDto usuarioConsulta = new UserDto();
    	usuarioConsulta.setUserId(idUsuario);
    	
     	UserDto userDto = SISPEFactory.getDataService().findUnique(usuarioConsulta);
     	String nombreUsuario = (userDto != null && StringUtils.isNotEmpty(usuarioConsulta.getUserCompleteName())) ? " - "+usuarioConsulta.getUserCompleteName() : "" ;
  
		return nombreUsuario;
	}
	
	
	/**
	 * Inserta en la tabla KSSEGTPARCOM el json con los datos de la autorizacion que se pintara en el componente de autorizaciones
	 * @param urlSystemConnection
	 * @param request
	 * @param autorizacionCol
	 * @return
	 * @throws Exception
	 */
	private static String guardarJson(URLSystemConnection urlSystemConnection, HttpServletRequest request, Collection<Autorizacion> autorizacionCol) throws Exception{
		//------------------GUARDAR LOS PARAMETROS EN LA BASE DE DATOS---------------------
		
		String componentId="";
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.parameterComponent.id", request);
		componentId = parametroDTO.getValorParametro();	
									
		ParameterComponentDto parameterComponentDTO = new ParameterComponentDto();
		parameterComponentDTO.setComponentId(componentId);
		parameterComponentDTO.setValueParameter(new Gson().toJson(autorizacionCol));
		parameterComponentDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		parameterComponentDTO.setStatus(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
		
		SISPEFactory.getDataService().transCreate(parameterComponentDTO);	
		
		urlSystemConnection.getViewParameters().put("idAutorizaciones", parameterComponentDTO.getId().getParameterComponentId());	
		LogSISPE.getLog().info("id del parcom pasado al componente de autorizaciones "+parameterComponentDTO.getId().getParameterComponentId());
		String urlOpcionSystema = "/pages/administracion/autorizaciones/solicitarAutorizacion.jsf"; 
		
		UserCompanySystemDto userInfo = new UserCompanySystemDto();
		userInfo.getId().setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		userInfo.getId().setSystemId(MessagesWebSISPE.getString("security.SYSTEM_ID_AUTORIZACIONES"));
//		userInfo.getId().setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		userInfo.getId().setUserId(asignarUsuarioAutorizado(request));
		
		LogSISPE.getLog().info("parametros seteados en el componente de autorizaciones -- companyId: "+userInfo.getId().getCompanyId() 
				+" systemId: "+userInfo.getId().getSystemId()+" userId: "+userInfo.getId().getUserId());

		String url = SystemConnectionSerializer.getInstancia().generateURLSystemConnection(request, userInfo, urlOpcionSystema, urlSystemConnection);
		LogSISPE.getLog().info("URL AUTORIZACIONES: {}", url);
		return url;
	}
	
	
	/**
	 * crea las autorizaciones  
	 * @param request
	 * @param detallePedidoCol
	 * @param infos
	 * @param codigoTipoAutorizacion
	 * @throws Exception
	 */
	public static void agregarAutorizacionPorTipo(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol, 
			ActionMessages infos, Long codigoTipoAutorizacion, ActionMessages warnings) throws Exception{
		
		try{

			//Para las autorizaciones de Stock
			if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
				if(CollectionUtils.isNotEmpty(detallePedidoCol)){
					
					//se obtiene las clasificaciones que necesitan autorizacion de stock
					Collection<String> clasificacionesAutorizacionGerenteComercial = obtenerClasificacionesAutorizacionGerenteComercial(request);
					Collection<DetallePedidoDTO> detallesCanastosEspecialesSinStock = new ArrayList<DetallePedidoDTO>();
					
					//se elimina esta variable para que se guarde el pedido
					request.getSession().removeAttribute(CotizarReservarAction.BORRAR_ACCION_ANTERIOR);
					
					//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
					Long codigoAutorizarStock = obtenerCodigoProcesoAutorizarStock(request);
					
					LogSISPE.getLog().info("Guardando info para autorizaci\u00F3n de Stock ");

					//coleccion que guardara las autorizaciones
					Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (Collection<EstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK); 
					if(CollectionUtils.isEmpty(colaAutorizacionesStock)){
						colaAutorizacionesStock = new ArrayList<EstadoPedidoAutorizacionDTO>(); 
					}
					EstadoPedidoAutorizacionDTO nuevaAutorizacion = null;
					
					//se recupera de session el pedido
					PedidoDTO pedidoDTO = (PedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
					
					//variable que almacenara el codigo del Usuario funcionario del autorizador y la lista de detalles que pertenecen a ese autorizador
					Map<String, Collection<DetallePedidoDTO>> mapCompradoresStockCero = new HashMap<String, Collection<DetallePedidoDTO>>();
					//coleccion de detalles que NO poseen autorizador
					Collection<DetallePedidoDTO> detallesSinAutorizador = new ArrayList<DetallePedidoDTO>();
					
					Boolean mostrarPopUpAutorizaciones = Boolean.FALSE;
					Boolean agregarInfo = Boolean.FALSE;
					
					//codigoClasificacion, codigoAutorizador
					Map<String, String> mapClasificacionAutorizador = new HashMap<String, String>();
					//se obtienen los detalles que pertenecen a cada autorizador
					for(DetallePedidoDTO detalleActual : detallePedidoCol){
						
						//si el pedido no tiene una autorizacion de stock, se crea la autorizacion
						if(!verificarArticuloPorTipoAutorizacion(detalleActual, codigoTipoAutorizacion)){
								
							//si la cantidad del detalle no ha sido modificado, no se genera la autorizacion
							Boolean verificarStock = Boolean.TRUE;
							//valida si aumentaron los pavos sin stock o si se agregaron pavos sin stock
							if(CollectionUtils.isNotEmpty(pedidoDTO.getNpDiferenciaDetallePedidoPOS())){
								//se recorren los detalles modificados
								for(DetallePedidoDTO detalleModificado : pedidoDTO.getNpDiferenciaDetallePedidoPOS()){
									//se comparan los articulos
									if(detalleActual.getId().getCodigoArticulo().equals(detalleModificado.getId().getCodigoArticulo())){
										//se comparan las cantidades
										if(detalleActual.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() <= detalleModificado.getEstadoDetallePedidoDTO().getCantidadEstado()){
											//verificamos si el pedido ya tiene una autorizacion
											if(verificarArticuloPorTipoAutorizacion(detalleModificado, ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
												//si el detalle es el mismo no se valida el stock
												verificarStock = Boolean.FALSE;
											}
											break;
										}
									}
								}
							}
							
							//ingresa al proceso de creacion de la autorizacion de stock
							obtenerMapDetallesSinStock(request, warnings, clasificacionesAutorizacionGerenteComercial, detallesCanastosEspecialesSinStock, codigoAutorizarStock, mapCompradoresStockCero,
									detallesSinAutorizador, mapClasificacionAutorizador, detalleActual, verificarStock);
						}else{
							//se verifica el estado de la autorizacion existente
							boolean existeAutorizacionPendiente = Boolean.TRUE;
							boolean autorizacionCanastoEspecialPendiente = Boolean.FALSE;
							for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
								
								//si la autorizacion es de stock
								if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
									
									 if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
											|| autorizacionActual.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA)){
										 
										existeAutorizacionPendiente =  Boolean.FALSE;
									}
									//si el detalles es un canasto especial y la autorizacion esta pendiente no permitir verificar los detalles pendientes
									else if(obtenerCodigoClasificacion(detalleActual).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
												|| detalleActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
										 
										autorizacionCanastoEspecialPendiente = Boolean.TRUE;
									 }
								}
							}
							if(existeAutorizacionPendiente || autorizacionCanastoEspecialPendiente){
								agregarInfo = Boolean.TRUE;
							}
							
							if(!autorizacionCanastoEspecialPendiente && (obtenerCodigoClasificacion(detalleActual).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
									|| detalleActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO))){
								//se verifica si debe agregarse algun detalle del canasto al map para solicitarStock
								obtenerMapDetallesSinStock(request, warnings, clasificacionesAutorizacionGerenteComercial, detallesCanastosEspecialesSinStock, codigoAutorizarStock, mapCompradoresStockCero,
										detallesSinAutorizador, mapClasificacionAutorizador, detalleActual, true);
							}
						}
					}
					
					//Se recorren los valores del map
					Set<Map.Entry<String, Collection<DetallePedidoDTO>>> valoresMap = mapCompradoresStockCero.entrySet();
					if(CollectionUtils.isNotEmpty(valoresMap)){
						
						mostrarPopUpAutorizaciones = Boolean.TRUE;
						
						for(Map.Entry<String, Collection<DetallePedidoDTO>> valorActual : valoresMap){
							//clasificaciones de los articulos con problemas de stock 
							String clasificaciones = "";
							String valorReferencial = "";
							Collection<DetallePedidoDTO> detallesComprador = valorActual.getValue();
							String codigoFuncionario = valorActual.getKey();
							
					    	//se consultan el nombre del funcionario
					    	FuncionarioDTO funcionarioConsultaDTO = new FuncionarioDTO();
					    	funcionarioConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					    	funcionarioConsultaDTO.setUsuarioFuncionario(codigoFuncionario);
					    	funcionarioConsultaDTO.setPersonaDTO(new PersonaDTO());
					    	
					     	FuncionarioDTO funcionarioDTO = SISPEFactory.getDataService().findUnique(funcionarioConsultaDTO);
					     	String nombreAutorizador = "";
					     	if(funcionarioDTO != null && funcionarioDTO.getPersonaDTO() != null){
					     		nombreAutorizador = " - "+funcionarioDTO.getPersonaDTO().getPrimerNombre()+" "+funcionarioDTO.getPersonaDTO().getPrimerApellido();
					     	}else{
					     		ec.com.smx.frameworkv2.security.dto.UserDto usuario = new ec.com.smx.frameworkv2.security.dto.UserDto();
					     		usuario.setUserId(codigoFuncionario);
					     		ec.com.smx.frameworkv2.security.dto.UserDto usuarioEncontrado = SISPEFactory.getDataService().findUnique(usuario);
					     		if(usuarioEncontrado != null){
					     			nombreAutorizador = " - "+usuarioEncontrado.getUserCompleteName();
					     		}
					     	}
					    	LogSISPE.getLog().info("idFuncionario: "+codigoFuncionario+" autorizador: {}", nombreAutorizador);
					    	
							if(CollectionUtils.isNotEmpty(detallesComprador)){
								
								String[] autorizadores = new String[]{codigoFuncionario}; 
								
								//bandera para validar si es autorizador de canastos para agregar a las bodegas como autorizadoras 
								boolean esCanasto = Boolean.FALSE;
								for(DetallePedidoDTO detalleAct : detallesComprador){
									String codigoClasificacion = obtenerCodigoClasificacion(detalleAct);
//									esCanasto = CotizacionReservacionUtil.esCanasto(codigoClasificacion, request);
									
									if(codigoClasificacion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))){
										esCanasto = Boolean.TRUE;
										break;
									}
								}
								
								if(esCanasto){
									autorizadores = codigoFuncionario.split(SEPARADOR_COMA);
									nombreAutorizador = " - BODEGA DE CANASTOS";
								}
								
								//si la autorizacion es de canastos se agregan la bodega de canastos como autorizadores
//								if(esCanasto){
//									//se aumenta al id del comprador de canastos,  los usuarios autorizadores del parametro
//									ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.id.usuarios.autorizadores.stock.canastos", request);
//									String idUsuarioAutorizadoresStock =  parametroDTO.getValorParametro()+"," + codigoFuncionario;
//									autorizadores = idUsuarioAutorizadoresStock.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));	
//								}
								
								nuevaAutorizacion = construirEstadoPedidoAutorizacion(detallesComprador, request, Long.valueOf(colaAutorizacionesStock.size() + 1), 
										ConstantesGenerales.TIPO_AUTORIZACION_STOCK, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.titulo.autorizacion.stock")
										+nombreAutorizador, autorizadores);
								
//								Collection<String> idAutorizadorCopia = obtenerIdFuncionarioRecibiraCopiaAutorizacion(request, detallesComprador.iterator().next(),
//										ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue());
//								if(CollectionUtils.isNotEmpty(idAutorizadorCopia)){
//									nuevaAutorizacion.getNpAutorizacion().getUsuariosAutorizadores().addAll(idAutorizadorCopia);
//									LogSISPE.getLog().info("se enviaran copia de autorizaciones a: "+idAutorizadorCopia);
//								}
								if(nuevaAutorizacion != null){
									nuevaAutorizacion.getNpAutorizacion().setUsuarioAutorizador(codigoFuncionario);
									if(esCanasto){
										nuevaAutorizacion.getNpAutorizacion().setEstadoInicial(AutorizacionEstado.AUTORIZADA.getEstado());
										nuevaAutorizacion.getNpAutorizacion().setUserAutorizadorSelected(codigoFuncionario);
									}
									
									if(!colaAutorizacionesStock.contains(nuevaAutorizacion)){
										colaAutorizacionesStock.add(nuevaAutorizacion);
									}
									
									if(CollectionUtils.isNotEmpty(detallesCanastosEspecialesSinStock)){
										for(DetallePedidoDTO detallePedidoDTO : detallesComprador){
											if(detallePedidoDTO.getEstadoDetallePedidoDTO().getId() == null){
												detallesComprador.addAll(detallesCanastosEspecialesSinStock);
												break;
											}
										}
									}
									
									//se vinculan las autorizaciones al DetallePedido
									for(DetallePedidoDTO detalleActual : detallesComprador){
										if(detalleActual.getEstadoDetallePedidoDTO().getId() != null){
											construirDetalleEstadoPedidoAutorizacionDTO(detalleActual, nuevaAutorizacion, request, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, codigoFuncionario);
											if(!clasificaciones.contains(obtenerCodigoClasificacion(detalleActual))){
												clasificaciones += obtenerCodigoClasificacion(detalleActual)+"-"+detalleActual.getArticuloDTO().getClasificacionDTO().getDescripcionClasificacion()+", ";
												valorReferencial += obtenerCodigoClasificacion(detalleActual)+", ";
											}
										}
									}
									//se elimina la ultima coma y se remplaza por punto
									clasificaciones = clasificaciones.substring(0, clasificaciones.length()-2)+".";
									valorReferencial = valorReferencial.substring(0, valorReferencial.length()-2);
									//se setea en el valor referencial los codigos de las clasificaciones de los articulos
									nuevaAutorizacion.setValorReferencial(valorReferencial);
									nuevaAutorizacion.getNpAutorizacion().setDescripcion("Solicito autorizaci\u00F3n para art\u00EDculos sin existencia de stock pertenecientes a las clasificaciones: "+clasificaciones);
								}else{
									
									DetalleEstadoPedidoAutorizacionDTO detEstPedAut = descontarAutorizacionStockAFavor(request);
									
									if(detEstPedAut != null){
										
										nuevaAutorizacion = detEstPedAut.getEstadoPedidoAutorizacionDTO();
										//se vinculan las autorizaciones al DetallePedido
										for(DetallePedidoDTO detalleActual : detallesComprador){
											
											if(detalleActual.getEstadoDetallePedidoDTO().getId() != null){
												construirDetalleEstadoPedidoAutorizacionDTO(detalleActual, nuevaAutorizacion, request, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, codigoFuncionario);
												if(!clasificaciones.contains(obtenerCodigoClasificacion(detalleActual))){
													clasificaciones += obtenerCodigoClasificacion(detalleActual)+"-"+detalleActual.getArticuloDTO().getClasificacionDTO().getDescripcionClasificacion()+", ";
													valorReferencial += obtenerCodigoClasificacion(detalleActual)+", ";
												}
											}
										}
									}
									
									infos.add("autorizacion.anterior.restablecida", new ActionMessage("info.autorizacion.stock.restablecida"));
									request.getSession().setAttribute("ec.com.smx.sic.sispe.confirmar.pedido", Boolean.TRUE);
								}
							}
						}
					}
					
					request.getSession().setAttribute(COLA_AUTORIZACIONES_STOCK, colaAutorizacionesStock);
					
					//se valida si debe mostrar o no los infos para que se muestre el popUp de autorizaciones
					if(!mostrarPopUpAutorizaciones && agregarInfo){
						//se agrega el mensaje de informacion de autorizacion pendiente por aprobar
						CotizacionReservacionUtil.agregarActionMessageNoRepetido("info.autorizacion.stock.pendiente.aprobar", "autorizacionPendiente", null, infos);
					}
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().error("Error al agregar crear la autorizacion del tipo "+codigoTipoAutorizacion+" "+e);
		}
	}

	/**
	 * @param request
	 * @param warnings
	 * @param clasificacionesAutorizacionGerenteComercial
	 * @param detallesCanastosEspecialesSinStock
	 * @param codigoAutorizarStock
	 * @param mapCompradoresStockCero
	 * @param detallesSinAutorizador
	 * @param mapClasificacionAutorizador
	 * @param detalleActual
	 * @param verificarStock
	 * @throws Exception
	 */
	private static void obtenerMapDetallesSinStock(HttpServletRequest request, ActionMessages warnings, Collection<String> clasificacionesAutorizacionGerenteComercial,
			Collection<DetallePedidoDTO> detallesCanastosEspecialesSinStock, Long codigoAutorizarStock, Map<String, Collection<DetallePedidoDTO>> mapCompradoresStockCero,
			Collection<DetallePedidoDTO> detallesSinAutorizador, Map<String, String> mapClasificacionAutorizador, DetallePedidoDTO detalleActual, Boolean verificarStock)throws Exception {
		
		try{
			if(verificarStock && detalleActual.isNpSinStockPavPolCan() 
					&& detalleActual.getArticuloDTO().getClasificacionDTO() != null
					&& solicitarAutorizacionStockPorFechas(request, obtenerCodigoClasificacion(detalleActual))){
				
				//para el caso de articulos que tienen asignados compradores
				if(detalleActual.getArticuloDTO().getCodigoClasificacion() != null){
					
					//coleccion de detallePedido 
					Collection<DetallePedidoDTO> detallesComprador;
					
					String autorizador = null;
					String codigoClasificacion = obtenerCodigoClasificacion(detalleActual);
					
					//cuando se trata de un detalle normal o de un canasto de catalogo
					if(!CotizacionReservacionUtil.esCanasto(codigoClasificacion, request) 
							|| codigoClasificacion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))){
						
						//si ya se ha buscado el autorizador de esa clasificacion
						if(mapClasificacionAutorizador.containsKey(codigoClasificacion)){
							//se obtiene el valor de esa llave
							autorizador = mapClasificacionAutorizador.get(codigoClasificacion);
						}else{
							
							//se busca el autorizador de esa clasificacion
							//----------------->
//							FuncionarioDTO funcionarioDTO = obtenerFuncionarioAutorizadorPorClasificacion(codigoClasificacion, request, codigoAutorizarStock, warnings);
//							if(funcionarioDTO != null && StringUtils.isNotEmpty(funcionarioDTO.getUsuarioFuncionario())){
//								autorizador = funcionarioDTO.getUsuarioFuncionario();
//								mapClasificacionAutorizador.put(codigoClasificacion, autorizador);
//							}
							//<---------------

							//se comenta el if de arriba para que los canastos de catalogo las autorice la bodega y los especiales los funcionario asignados
							
							if(!CotizacionReservacionUtil.esCanasto(codigoClasificacion, request)){
								FuncionarioDTO funcionarioDTO = obtenerFuncionarioAutorizadorPorClasificacion(codigoClasificacion,obtenerTipoMarca(detalleActual), request, codigoAutorizarStock, warnings);
								if(funcionarioDTO != null && StringUtils.isNotEmpty(funcionarioDTO.getUsuarioFuncionario())){
									autorizador = funcionarioDTO.getUsuarioFuncionario();
									mapClasificacionAutorizador.put(codigoClasificacion, autorizador);
								}
							}else{
								ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.id.usuarios.autorizadores.stock.canastos", request);
								if(parametroDTO != null && StringUtils.isNotEmpty(parametroDTO.getValorParametro())){
									autorizador = parametroDTO.getValorParametro();
									mapClasificacionAutorizador.put(codigoClasificacion, autorizador);
								}
								else{
									LogSISPE.getLog().error("No existe un funcionario autorizador de la clasificacion "+codigoClasificacion);
									
									//se muestra el mensaje y se cancela la creacion de autorizaciones de stock
									warnings.add("sinFuncionario", new ActionMessage("warning.clasificacion.autorizador", codigoClasificacion+" - "+
													detalleActual.getArticuloDTO().getClasificacionDTO().getDescripcionClasificacion()));
									request.getSession().removeAttribute(COLA_AUTORIZACIONES_STOCK);
									return;
								}
							}
						}
					//solo para el caso de canastos especiales
					}else if(codigoClasificacion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
							|| detalleActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
						
						//se agrega el detalle a la coleccion de canastos especiales con problemas de stock para asociar la autorizacion al detalle padre
						if(!detallesCanastosEspecialesSinStock.contains(detalleActual)){
							detallesCanastosEspecialesSinStock.add(detalleActual);
						}
						
						//se recorren los detalles del canasto y verificar los detalles que no tiene stock
						if(CollectionUtils.isNotEmpty(detalleActual.getArticuloDTO().getArticuloRelacionCol())
								&& detalleActual.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
								
							//se inicializa la coleccion de detalles relacionados
							if(CollectionUtils.isEmpty(detalleActual.getNpDetallesRelacionadosSinStockCol())){
								detalleActual.setNpDetallesRelacionadosSinStockCol(new ArrayList<DetallePedidoDTO>());
							}
							
							//se recorren los detalles del canasto especial
							for(ArticuloRelacionDTO articuloRelacionDTO : detalleActual.getArticuloDTO().getArticuloRelacionCol()){
								
								if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
										&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
										&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
									//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
									
									if(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() != null && detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() != null){
										
										Long cantidadReservarSic = detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() - articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo();
										
										if(cantidadReservarSic > 0 
										&& detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo()
										//para los detalles nuevos o los detalles con autorizaciones rechazadas
//										&& (articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() == null 
//												|| articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA))
												){
											
											DetalleEstadoPedidoAutorizacionStockDTO autStockDTO = obtenerDetalleEstadoPedidoAutorizacionStockDTO(detalleActual, false, articuloRelacionDTO.getId().getCodigoArticuloRelacionado());
											
											boolean agregarDetalle = true; 
											if(autStockDTO != null && autStockDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
												
												if(autStockDTO.getCantidadUtilizada() != null
														&& cantidadReservarSic <= autStockDTO.getCantidadUtilizada()){
													
													agregarDetalle = false;
												}
											}else if(autStockDTO != null && autStockDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA)){
												//se elimina la autorizacion rechazada
												eliminarAutorizacionStock(detalleActual, autStockDTO);
											}
											
											if(agregarDetalle){
												DetallePedidoDTO nuevoDetallePedidoDTO = new DetallePedidoDTO();
												nuevoDetallePedidoDTO.setId(detalleActual.getId());
												nuevoDetallePedidoDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setId(null);
//												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC((detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() 
//														* articuloRelacionDTO.getCantidad()) - articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo());
												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() 
														* articuloRelacionDTO.getCantidad());
												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadParcialReservarSic(detalleActual.getEstadoDetallePedidoDTO().getNpCantidadParcialReservarSic());
												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setDetallePedidoDTO(detalleActual);
												nuevoDetallePedidoDTO.setArticuloDTO(articuloRelacionDTO.getArticuloRelacion());
												nuevoDetallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionAnteriorCol(
														detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol());
												
//												//Se agrega al canasto los detalles de la receta sin stock
//												detalleActual.getNpDetallesRelacionadosSinStockCol().add(nuevoDetallePedidoDTO);
												
												//si ya se ha buscado el autorizador de esa clasificacion
												if(mapClasificacionAutorizador.containsKey(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())){
													autorizador = mapClasificacionAutorizador.get(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion());
												}else{
													//se busca el autorizador de esa clasificacion
													FuncionarioDTO funcionarioDTO = obtenerFuncionarioAutorizadorPorClasificacion(articuloRelacionDTO.getArticuloRelacion().
															getCodigoClasificacion(),articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca(), request, codigoAutorizarStock, warnings);
													if(funcionarioDTO != null && StringUtils.isNotEmpty(funcionarioDTO.getUsuarioFuncionario())){
														autorizador = funcionarioDTO.getUsuarioFuncionario();
														mapClasificacionAutorizador.put(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion(), autorizador);
													}else{
														LogSISPE.getLog().error("No existe un funcionario autorizador de la clasificacion "+articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion());
														
														//se muestra el mensaje y se cancela la creacion de autorizaciones de stock
														warnings.add("sinFuncionario", new ActionMessage("warning.clasificacion.autorizador", articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion()+" - "+
																articuloRelacionDTO.getArticuloRelacion().getClasificacionDTO().getDescripcionClasificacion()));
														request.getSession().removeAttribute(COLA_AUTORIZACIONES_STOCK);
														return;
													}
												}
												
												if(StringUtils.isNotEmpty(autorizador)){
													
													nuevoDetallePedidoDTO.setNpIdAutorizador(autorizador);
													//Se agrega al canasto los detalles de la receta sin stock
													if(!detalleActual.getNpDetallesRelacionadosSinStockCol().contains(detalleActual)){
														detalleActual.getNpDetallesRelacionadosSinStockCol().add(nuevoDetallePedidoDTO);
													}
													
													LogSISPE.getLog().info("funcionario autorizador: {}",autorizador);
													detallesComprador = new ArrayList<DetallePedidoDTO>();
													//se agrega un detalle a la lista del comprador 
													if(mapCompradoresStockCero.containsKey(autorizador)){
														detallesComprador = mapCompradoresStockCero.get(autorizador);
													}
													detallesComprador.add(nuevoDetallePedidoDTO);
													mapCompradoresStockCero.put(autorizador, detallesComprador);
												}
												autorizador = null;
											}
										}
									}
								}
							}
						}
					}
					
					if(StringUtils.isNotEmpty(autorizador)){
						
//						String key = detalleActual.getArticuloDTO().getClasificacionDTO().getCompradorDTO().getFuncionarioDTO().getUsuarioFuncionario();
						LogSISPE.getLog().info("funcionario autorizador: {}",autorizador);
						detallesComprador = new ArrayList<DetallePedidoDTO>();
						//se agrega un detalle a la lista del comprador 
						if(mapCompradoresStockCero.containsKey(autorizador)){
							detallesComprador = mapCompradoresStockCero.get(autorizador);
						}
						detallesComprador.add(detalleActual);
						mapCompradoresStockCero.put(autorizador, detallesComprador);
					}
				}else{
					//para el caso de articulos que no tienen asignado un comprador
					detallesSinAutorizador.add(detalleActual);
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().error("Error al obtener el map con los detalles sin stock. "+e);
			throw new SISPEException(e);
		}
	}

	
	public static void agregarAutorizacionPorTipoAnterior(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol, 
			ActionMessages infos, Long codigoTipoAutorizacion, ActionMessages warnings) throws Exception{
		
		Boolean cargarStockNuevaVersion = Boolean.TRUE;
		if(cargarStockNuevaVersion){
			agregarAutorizacionPorTipo(request, detallePedidoCol, infos, codigoTipoAutorizacion, warnings);
			return;
		}
		//Para las autorizaciones de Stock
		if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
			if(CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				LogSISPE.getLog().info("Guardando info para autorizaci\u00F3n de Stock ");

				//coleccion que guardara las autorizaciones
				Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (Collection<EstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK); 
				if(CollectionUtils.isEmpty(colaAutorizacionesStock)){
					colaAutorizacionesStock = new ArrayList<EstadoPedidoAutorizacionDTO>(); 
				}
				Boolean ingresoAutCanasto = Boolean.FALSE;
				Boolean ingresoAutPavos = Boolean.FALSE;
				
				EstadoPedidoAutorizacionDTO autorizacionPendienteCanastos = null;
				EstadoPedidoAutorizacionDTO autorizacionPendientePavos = null;
				
				//se recupera de session el pedido
				PedidoDTO pedidoDTO = (PedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
				
				//se crean las autorizaciones por tipo de clasificacion
				for(DetallePedidoDTO detalleActual : detallePedidoCol){
					
					LogSISPE.getLog().info("articulo {} - {}", detalleActual.getId().getCodigoArticulo(), detalleActual.getArticuloDTO().getDescripcionArticulo());
					Boolean verificarStock = Boolean.TRUE;
					//valida si aumentaron los pavos sin stock o si se agregaron pavos sin stock
					if(CollectionUtils.isNotEmpty(pedidoDTO.getNpDiferenciaDetallePedidoPOS())){
						//se recorren los detalles modificados
						for(DetallePedidoDTO detalleModificado : pedidoDTO.getNpDiferenciaDetallePedidoPOS()){
							//se comparan los articulos
							if(detalleActual.getId().getCodigoArticulo().equals(detalleModificado.getId().getCodigoArticulo())){
								//se comparan las cantidades
								if(detalleActual.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() <= detalleModificado.getEstadoDetallePedidoDTO().getCantidadEstado()){
									//si el detalle es el mismo no se valida el stock
									verificarStock = Boolean.FALSE;
									break;
								}
							}
						}
					}
					
					if(verificarStock && detalleActual.isNpSinStockPavPolCan()){
						//es clasificacion de canastos
						if(CotizacionReservacionUtil.esCanasto(obtenerCodigoClasificacion(detalleActual), request)){
							
							if(!ingresoAutCanasto){

								//se aumenta al id del comprador de canastos,  los usuarios autorizadores del parametro
								ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.id.usuarios.autorizadores.stock.canastos", request);
								String idUsuarioAutorizadoresStock =  parametroDTO.getValorParametro()+"," + getIdUsuarioGerenteComercialCanastos(request);
								String[] filtroUsuariosAutorizadores = idUsuarioAutorizadoresStock.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
								
								autorizacionPendienteCanastos = construirEstadoPedidoAutorizacion(detallePedidoCol, request, Long.valueOf(colaAutorizacionesStock.size() + 1), 
										ConstantesGenerales.TIPO_AUTORIZACION_STOCK, "CANASTOS SIN EXISTENCIA DE STOCK", filtroUsuariosAutorizadores);
								
								//se solicita siempre y cuando no exista una en sesion
								if(request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_CANASTOS) == null  || 
										(request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_CANASTOS) != null && request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_CANASTOS).equals(Boolean.FALSE))){
									colaAutorizacionesStock.add(autorizacionPendienteCanastos);
									request.getSession().setAttribute(TIENE_AUTORIZACION_STOCK_CANASTOS, Boolean.FALSE);
									LogSISPE.getLog().info("canasto con problemas de stock");
								}
								
								ingresoAutCanasto = Boolean.TRUE;
							}
							
							//se vinculan las autorizaciones al pedido
							construirDetalleEstadoPedidoAutorizacionDTO(detalleActual, autorizacionPendienteCanastos, request, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, getIdUsuarioGerenteComercialCanastos(request));
						}
						//es clasificacion de pavos
						else if(CotizacionReservacionUtil.esPavo(detalleActual.getArticuloDTO().getCodigoClasificacion(), request)){
							
							if(!ingresoAutPavos){
								autorizacionPendientePavos = construirEstadoPedidoAutorizacion(detallePedidoCol, request, Long.valueOf(colaAutorizacionesStock.size() + 1), 
										ConstantesGenerales.TIPO_AUTORIZACION_STOCK, "PAVOS/POLLOS SIN EXISTENCIA DE STOCK", new String[] {getIdUsuarioGerenteComercialPavos(request)});
								
								//se solicita siempre y cuando no exista una en sesion
								if(request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_PAVOS) == null || 
										(request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_PAVOS) != null && request.getSession().getAttribute(TIENE_AUTORIZACION_STOCK_PAVOS).equals(Boolean.FALSE))){
									colaAutorizacionesStock.add(autorizacionPendientePavos);
									request.getSession().setAttribute(TIENE_AUTORIZACION_STOCK_PAVOS, Boolean.FALSE);
									LogSISPE.getLog().info("pavo con problemas de stock");
								}
								
								ingresoAutPavos = Boolean.TRUE;
							}
							
							//se vinculan las autorizaciones al pedido
							construirDetalleEstadoPedidoAutorizacionDTO(detalleActual,autorizacionPendientePavos,request, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, getIdUsuarioGerenteComercialPavos(request));
						}
					}
				}
				//se respalda la colecci\u00F3n de autorizaciones antes de ser reemplazada
//				request.getSession().setAttribute(COLA_AUTORIZACIONES_STOCK_RESPALDO, request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK));
				request.getSession().setAttribute(COLA_AUTORIZACIONES_STOCK, colaAutorizacionesStock);
			}
		}
	}
	
	
	/**
	 * Se crea un EstadoPedidoAutorizacionDTO de acuerdo a los datos pasados por parametro
	 * @param detalleActual
	 * @param request
	 * @param secuencialAutorizacion 
	 * @param codigoTipoAutorizacion
	 * @return
	 * @throws Exception
	 */
	private static EstadoPedidoAutorizacionDTO construirEstadoPedidoAutorizacion(Collection<DetallePedidoDTO> detalleActualCol, HttpServletRequest request, 
			Long secuencialAutorizacion, Long codigoTipoAutorizacion, String tituloAutorizacion, String[] filtroUsuarioAutorizador)throws Exception{
		
		LogSISPE.getLog().info("creando el EstadoPedidoAutorizacionDTO");
		//se crea el nuevo EstadoPedidoAutorizacion
		EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
		estadoPedidoAutorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		estadoPedidoAutorizacionDTO.setEstado(ConstantesGenerales.ESTADO_AUT_PENDIENTE);
		estadoPedidoAutorizacionDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		estadoPedidoAutorizacionDTO.setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
		estadoPedidoAutorizacionDTO.setNpTipoAutorizacion(codigoTipoAutorizacion);	
		
		//Objeto que se debe enviar al componente de autorizaciones
		Autorizacion autorizacion = new Autorizacion();
		autorizacion.setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
//		autorizacion.setAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString());
		autorizacion.setAreaTrabajo(asignarAreaTrabajoAutorizacion(request));
		autorizacion.setEstadoInicial(null);
		if(codigoTipoAutorizacion.equals(ConstantesGenerales.getInstancia().TIPO_AUTORIZACION_CONSOLIDACION)
//				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)
				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)
				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL)
				|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS)){
			autorizacion.setEstadoInicial(AutorizacionEstado.AUTORIZADA.getEstado());
		}
		autorizacion.setSecuencial(secuencialAutorizacion);
		HashMap<Long, ValorComponente[]> filtroTipoAutorizacion = new  HashMap<Long, ValorComponente[]>();
		filtroTipoAutorizacion.put(codigoTipoAutorizacion, new ValorComponente[0]);
		autorizacion.setFiltroTipoAutorizacion(filtroTipoAutorizacion);
		autorizacion.setTituloAutorizacion(tituloAutorizacion);
		autorizacion.setFiltroUsuariosAutorizadores(filtroUsuarioAutorizador);
//		autorizacion.setUsuarioAutorizado(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		autorizacion.setUsuarioAutorizado(asignarUsuarioAutorizado(request));
		
		//para el caso de autorizaciones de STOCK
		if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){

			//codigos de los componentes de autorizaciones usados para detalles de cada articulo hijo
			Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
			Long codigoComponenteDescripcionArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.descripcion.articulo")); //82 
			Long codigoComponenteCantidadArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.cantidad.articulo"));//83
			Long codigoComponenteRadiodArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.radio.articulo"));//101
			
			ValorComponente[] vectorComponentes = new ValorComponente[4];
			filtroTipoAutorizacion = new HashMap<Long, ValorComponente[]>();
			vectorComponentes[0] = new ValorComponente(codigoComponenteCodigoArt , "", true, false);
			vectorComponentes[1] = new ValorComponente(codigoComponenteDescripcionArt, "", true, false);
			vectorComponentes[2] = new ValorComponente(codigoComponenteCantidadArt, "", true, false);
			vectorComponentes[3] = new ValorComponente(codigoComponenteRadiodArt, "", true, false);
			filtroTipoAutorizacion.put(codigoTipoAutorizacion, vectorComponentes);
			String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			
			//cuando se esta modificando una reserva se muestra solo para aplicar autorizacion 
			if(StringUtils.isNotEmpty(accionActual) && accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
				LogSISPE.getLog().info("accion actual: "+accionActual);
				autorizacion.setEstadoInicial(AutorizacionEstado.AUTORIZADA.getEstado());
			}
			
			autorizacion.setFiltroTipoAutorizacion(filtroTipoAutorizacion);
			
			//si existen autorizaciones hijas
			if(CollectionUtils.isNotEmpty(detalleActualCol)){
				
				//se setean los datakeys del area trabajo y codigo del pedido
				String areaTrabajo = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString();
//						+"-"+SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo();
				AutorizacionDataKey dataKeyAreaTrabajo=new AutorizacionDataKey("AREA TRABAJO", areaTrabajo, false, true);
				AutorizacionDataKey dataKeyNumeroPedido = new AutorizacionDataKey("N\u00DAMERO PEDIDO", detalleActualCol.iterator().next().getId().getCodigoPedido(), false, true);
//				AutorizacionDataKey dataKeyPrimeraFechaDespacho=new AutorizacionDataKey("PRIMERA FECHA DESPACHO", "2015-08-05 15:00", false, true);
				AutorizacionDataKey dataKeyIdAsistenteComercial=new AutorizacionDataKey("ASISTENTE COMERCIAL", "FRM168", false, false);
//				AutorizacionDataKey vectorDataKey[]=new AutorizacionDataKey[]{dataKeyAreaTrabajo, dataKeyNumeroPedido,dataKeyPrimeraFechaDespacho,dataKeyIdAsistenteComercial};
				AutorizacionDataKey vectorDataKey[]=new AutorizacionDataKey[]{dataKeyAreaTrabajo, dataKeyNumeroPedido, dataKeyIdAsistenteComercial};
				autorizacion.setDataKeys(vectorDataKey);
				
				//coleccion de los codigos de barras que ya se han procesado
				Collection<String> codigosProcesados = new ArrayList<String>();
				
				for(DetallePedidoDTO detalleHijo : detalleActualCol){
					
					String codigoBarrasActual = detalleHijo.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras();
					if(!codigosProcesados.contains(codigoBarrasActual)){
						
						Long cantidadDetalles = sumarCantidadDetallesRepetidos(request, detalleActualCol, codigoBarrasActual);
						if(cantidadDetalles > 0){
							
							codigosProcesados.add(codigoBarrasActual);
							detalleHijo.getEstadoDetallePedidoDTO().setNpCantidadTotalReservarSic(cantidadDetalles);
							vectorComponentes = new ValorComponente[4];
							filtroTipoAutorizacion = new HashMap<Long, ValorComponente[]>();
							Autorizacion autorizacionHija = new Autorizacion();
							autorizacionHija.setCodigoSistema(autorizacion.getCodigoSistema());
							//se concatena a la descripcion del articulo la medida
							String descripcionArticulo = detalleHijo.getEstadoDetallePedidoDTO().getId() == null ? "RECETA: " : ""; 
							descripcionArticulo += detalleHijo.getArticuloDTO().getDescripcionArticulo();
							descripcionArticulo += StringUtils.isNotEmpty(detalleHijo.getArticuloDTO().getArticuloMedidaDTO().getReferenciaMedida()) ? 
									" - "+detalleHijo.getArticuloDTO().getArticuloMedidaDTO().getReferenciaMedida() : "";
							vectorComponentes[0] = new ValorComponente(codigoComponenteCodigoArt ,codigoBarrasActual, true, false);
							vectorComponentes[1] = new ValorComponente(codigoComponenteDescripcionArt, descripcionArticulo, true, false);
							vectorComponentes[2] = new ValorComponente(codigoComponenteCantidadArt, cantidadDetalles.toString(), true, false);
							vectorComponentes[3] = new ValorComponente(codigoComponenteRadiodArt, "", true, false);
							filtroTipoAutorizacion.put(codigoTipoAutorizacion, vectorComponentes);
							autorizacionHija.setFiltroTipoAutorizacion(filtroTipoAutorizacion);
							
							//se agrega a la coleccion de autorizaciones hijas
							autorizacion.addAutorizacionesHija(autorizacionHija);
						}
					}
				}
			}
			if(CollectionUtils.isEmpty(autorizacion.getAutorizacionesHijas())){
				return null;
			}
		}
		estadoPedidoAutorizacionDTO.setNpAutorizacion(autorizacion);
		return estadoPedidoAutorizacionDTO;
	}
	
	/**
	 * se agrega al DetallePedidoDTO el EstadoPedidoAutorizacionDTO del tipo de autorizacion indicado
	 * @param detalleActual
	 * @param estadoPedidoAutorizacionDTO
	 * @param request
	 * @param codigoTipoAutorizacion
	 */
	private static void construirDetalleEstadoPedidoAutorizacionDTO(DetallePedidoDTO detalleActual, EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO, 
			HttpServletRequest request, Long codigoTipoAutorizacion, String idAutorizador){
		
		try{
			LogSISPE.getLog().info("creando el DetalleEstadoPedidoAutorizacion ");
			Boolean agregarDetalle = Boolean.TRUE;
			
			if(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol() != null){
				
				for(DetalleEstadoPedidoAutorizacionDTO dto : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
					
					//si el art\u00EDculo procesado ya existe en el detalle de las autorizaciones no se toma en cuenta otra vez
					if(dto.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(codigoTipoAutorizacion)
							&& dto.getId().getCodigoArticulo().equals(detalleActual.getId().getCodigoArticulo()) 
							&& dto.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() == null
							&& CollectionUtils.isEmpty(detalleActual.getNpDetallesRelacionadosSinStockCol())){
						
						agregarDetalle = Boolean.FALSE;
						break;
					}
				}
			}
			if(agregarDetalle){
				DetalleEstadoPedidoAutorizacionDTO detalleEstPedAutorizacion = new DetalleEstadoPedidoAutorizacionDTO();
				detalleEstPedAutorizacion.getId().setCodigoAreaTrabajo(detalleActual.getId().getCodigoAreaTrabajo());
				detalleEstPedAutorizacion.getId().setCodigoCompania(detalleActual.getId().getCodigoCompania());
				detalleEstPedAutorizacion.getId().setCodigoArticulo(detalleActual.getId().getCodigoArticulo());
				detalleEstPedAutorizacion.getId().setCodigoPedido(detalleActual.getId().getCodigoPedido());
				detalleEstPedAutorizacion.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
				detalleEstPedAutorizacion.setEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo.corporativo"));
				detalleEstPedAutorizacion.setEstadoAutorizacion(estadoPedidoAutorizacionDTO.getEstado());
				
				//para el caso de autorizacion de stock se agregan los siguientes campos
				if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
					
					//se inicializa la coleccion
					detalleEstPedAutorizacion.setDetalleEstadoPedidoAutorizacionStockDTOCol(new ArrayList<DetalleEstadoPedidoAutorizacionStockDTO>());					
					
					//para el caso de detalles normales
					if(CollectionUtils.isEmpty(detalleActual.getNpDetallesRelacionadosSinStockCol())){
						DetalleEstadoPedidoAutorizacionStockDTO detAutorizacionStockDTO = obtenerDetalleEstadoPedidoAutorizacionStockDTO(detalleActual, true, null);
						detAutorizacionStockDTO.setCodigoCompania(detalleActual.getId().getCodigoCompania());
						detAutorizacionStockDTO.setCodigoAreaTrabajo(detalleActual.getId().getCodigoAreaTrabajo());
						detAutorizacionStockDTO.setCodigoPedido(detalleActual.getId().getCodigoPedido());
						detAutorizacionStockDTO.setCodigoEstado(detalleActual.getEstadoDetallePedidoDTO().getId().getCodigoEstado());
						detAutorizacionStockDTO.setCodigoArticulo(detalleActual.getId().getCodigoArticulo());
						detAutorizacionStockDTO.setSecuencialEstadoPedido(detalleActual.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido());
						detAutorizacionStockDTO.setCodigoEstadoPedidoAutorizacion(estadoPedidoAutorizacionDTO.getId().getCodigoEstadoPedidoAutorizacion());
						detAutorizacionStockDTO.setCantidadParcialSolicitarStock(detalleActual.getEstadoDetallePedidoDTO().getNpCantidadParcialReservarSic().intValue());
						detAutorizacionStockDTO.setCantidadTotalSolicitarStock(detAutorizacionStockDTO.getCantidadTotalSolicitarStock() + detalleActual.getEstadoDetallePedidoDTO().getNpCantidadTotalReservarSic().intValue());
						detAutorizacionStockDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
						detAutorizacionStockDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						detAutorizacionStockDTO.setDetalleEstadoPedidoAutorizacionDTO(detalleEstPedAutorizacion);
						detAutorizacionStockDTO.setEstadoOrdenCompra(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.order.compra.pendiente"));
						detalleEstPedAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol().add(detAutorizacionStockDTO);
						
					}else{
						//para el caso de canastos
						for(DetallePedidoDTO detalleSinStock : detalleActual.getNpDetallesRelacionadosSinStockCol()){
							
							if(idAutorizador.equals(detalleSinStock.getNpIdAutorizador())){
								
								DetalleEstadoPedidoAutorizacionStockDTO detAutorizacionStockDTO = obtenerDetalleEstadoPedidoAutorizacionStockDTO(detalleSinStock, true, detalleSinStock.getArticuloDTO().getId().getCodigoArticulo());
								detAutorizacionStockDTO.setCodigoCompania(detalleActual.getId().getCodigoCompania());
								detAutorizacionStockDTO.setCodigoAreaTrabajo(detalleActual.getId().getCodigoAreaTrabajo());
								detAutorizacionStockDTO.setCodigoPedido(detalleActual.getId().getCodigoPedido());
								detAutorizacionStockDTO.setCodigoEstado(detalleActual.getEstadoDetallePedidoDTO().getId().getCodigoEstado());
								detAutorizacionStockDTO.setCodigoArticulo(detalleActual.getId().getCodigoArticulo());
								detAutorizacionStockDTO.setSecuencialEstadoPedido(detalleActual.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido());
								detAutorizacionStockDTO.setCodigoEstadoPedidoAutorizacion(estadoPedidoAutorizacionDTO.getId().getCodigoEstadoPedidoAutorizacion());
								detAutorizacionStockDTO.setCodigoArticuloRelacionado(detalleSinStock.getArticuloDTO().getId().getCodigoArticulo());
								detAutorizacionStockDTO.setCantidadParcialSolicitarStock(detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadParcialReservarSic() != null
										? detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadParcialReservarSic().intValue() : detalleSinStock.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue());
								
								if(existeAutorizacionAprobadaParaMismoDetalle(detalleActual, detalleSinStock)){
									//se creara una nueva autorizacion solo por la diferencia
									detAutorizacionStockDTO.setCantidadTotalSolicitarStock(detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadTotalReservarSic() != null ?
											detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadTotalReservarSic().intValue() : detalleSinStock.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue());
									detAutorizacionStockDTO.setCantidadTotalAutorizadaStock(0);
									detAutorizacionStockDTO.setCantidadUtilizada(0);
								}else{
									//se actualiza la autorizacion actual con el nuevo parcial a solicitar
									detAutorizacionStockDTO.setCantidadTotalSolicitarStock(detAutorizacionStockDTO.getCantidadTotalSolicitarStock() + (detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadTotalReservarSic() != null ?
											detalleSinStock.getEstadoDetallePedidoDTO().getNpCantidadTotalReservarSic().intValue() : detalleSinStock.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue()));
								}
								
								detAutorizacionStockDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
								detAutorizacionStockDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								detAutorizacionStockDTO.setDetalleEstadoPedidoAutorizacionDTO(detalleEstPedAutorizacion);
								detAutorizacionStockDTO.setEstadoOrdenCompra(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.order.compra.pendiente"));
								detalleEstPedAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol().add(detAutorizacionStockDTO);
							}
						}
					}
				}
				//campos de auditoria
				detalleEstPedAutorizacion.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				
				if(CollectionUtils.isEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
					detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>());
				}
				detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().add(detalleEstPedAutorizacion);
			}
			
		}catch(Exception e){
			LogSISPE.getLog().error("Error al construirDetalleEstadoPedidoAutorizacionDTO.. {}",e);
		}
	}
	
	
	/**
	 * Verifica si el canasto especial tiene autorizaciones de stock aprobada para un articulo de su receta
	 * @param detallePedidoDTO
	 * @param detallePedidoReceta
	 * @return
	 */
	private static boolean existeAutorizacionAprobadaParaMismoDetalle(DetallePedidoDTO detallePedidoDTO, DetallePedidoDTO detallePedidoReceta){
		
		LogSISPE.getLog().info("verificando si el detalle tipo canasto ya posee una autorizacion de stock aprobada para la receta"+detallePedidoReceta.getArticuloDTO().getDescripcionArticulo());
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			
			for(DetalleEstadoPedidoAutorizacionDTO detEstPedAutDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				
				//se verifica que sea autorizacion de stock y que este aprobada
				if(detEstPedAutDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()
						&& (detEstPedAutDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
								|| detEstPedAutDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))){
					
					//se verifican las autorizaciones de stock
					if(CollectionUtils.isNotEmpty(detEstPedAutDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
						
						for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
							
							//se compara los id
							if(detallePedidoReceta.getId().getCodigoCompania() == stockDTO.getCodigoCompania()
									&& detallePedidoReceta.getId().getCodigoAreaTrabajo().equals(stockDTO.getCodigoAreaTrabajo()) 
									&& detallePedidoReceta.getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
									&& detallePedidoReceta.getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())
									&& detallePedidoReceta.getArticuloDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticuloRelacionado())){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param respuestaSistemaAutorizaciones
	 * @param request
	 * @param codigoTipoAutorizacion
	 * @return true si todas las autorizaciones han sido aprobadas correctamente
	 * @throws Exception
	 */
	public static Boolean verificarAutorizacion(String respuestaSistemaAutorizaciones, HttpServletRequest request, ActionMessages errors, ActionMessages infos) throws Exception{
		
		Boolean autorizacionesValidas = Boolean.FALSE;
		try{
			String[] autorizaciones = respuestaSistemaAutorizaciones.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
		
			if(autorizaciones != null && autorizaciones.length > 0){
				Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
				
				//contador de autorizaciones por estado
				Map<String, Integer> mapContAutorizaciones = new HashMap<String, Integer>();				
				for(int i=0; i<autorizaciones.length; i++){
					String[] detalleAut = autorizaciones[i].split(SEPARADOR_DOS_PUNTOS);
					//1:19987:F17-1774:9:UTILIZADA:0  1:22636:F17-3343:11:SOLICITADA:1   [1, 30110, F17-6390, 11, SOLICITADA, 1, true]
					Long secuencial = Long.valueOf(detalleAut[0]);
					String codigoAutorizacion = detalleAut[1];
					String processCode = detalleAut[2];
					Long codigoTipoAutorizacion = Long.valueOf(detalleAut[3]);
					String estadoAutorizacion = detalleAut[4];
					String estadoAutSispe = obtenerEstadoAutorizacionComponenteASispe(estadoAutorizacion);
					Boolean enviarNotificacion = detalleAut.length >= TAMANIO_RESPUESTA_STOCK.intValue() ? Boolean.valueOf(detalleAut[6]): Boolean.FALSE;
					Integer cantidadAutorizacioneshijas = 0;
					if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
						cantidadAutorizacioneshijas = Integer.parseInt(detalleAut[5]);
					}
					LogSISPE.getLog().info("Validando la autorizacion: {}", autorizaciones[i]);
					//se inicia la coleccion
					if(CollectionUtils.isEmpty(autorizacionesFinalizarWorkflow)){
						autorizacionesFinalizarWorkflow = new ArrayList<String>();
					}else{
						//se verifica que la colleccion no tenga ese processCode, porque cuando se reutiliza una autorizacion puede generarse dos detalles
						Collection<String> datosEliminar = new ArrayList<String>();
						for(String act : autorizacionesFinalizarWorkflow){
							if(act.contains(detalleAut[2])){
								datosEliminar.add(act);
							}
						}
						autorizacionesFinalizarWorkflow.removeAll(datosEliminar);
					}
					
					//se agrega una autorizacion para que sea finalizada
					if(estadoAutorizacion.equals(AutorizacionEstado.AUTORIZADA.getEstado()) ||  estadoAutorizacion.equals(AutorizacionEstado.GESTIONADA.getEstado())){
						autorizacionesFinalizarWorkflow.add(obtenerAutorizacionPorFinalizar(request, detalleAut, null));
					}
					
					if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
						LogSISPE.getLog().info("es una autorizacion de STOCK");
						Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK);
						
						LogSISPE.getLog().info("obteniendo el estado de la autorizacion");
						// se procesan las autorizaciones de acuerdo a su estado
						
						if(estadoAutSispe.equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
							estadoAutSispe = ConstantesGenerales.ESTADO_AUT_SOLICITADA;
						}
						// se procesan las autorizaciones
						
						//contador de autorizaciones por estado
//						Map<String, Integer> mapContAutorizaciones = new HashMap<String, Integer>();
						//se cambia el estado de la autorizacion
						LogSISPE.getLog().info("cambiando el estado de la cola de autorizaciones de stock que se encuentra en sesion");
						
						cambiarEstadoAutorizacion(colaAutorizacionesStock, new Long(codigoAutorizacion), secuencial, processCode, estadoAutSispe, cantidadAutorizacioneshijas,
								request, codigoTipoAutorizacion, mapContAutorizaciones, errors, enviarNotificacion);
						
						//si aplica un numero de autorizacion se aplica el estado a todos los detalles
						//se obtiene de sesion el detalle del pedido
						ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
						if(CollectionUtils.isNotEmpty(detallePedidoCol) && cantidadAutorizacioneshijas == 0 ){
							
							//se obtiene las clasificaciones que necesitan autorizacion de stock
							Collection<String> clasificacionesAutorizacionGerenteComercial = obtenerClasificacionesAutorizacionGerenteComercial(request);
							LogSISPE.getLog().info("cambiando el estado de los detalles del pedido");
							
							for(DetallePedidoDTO detallePedido : detallePedidoCol){
								
								if(CollectionUtils.isNotEmpty(detallePedido.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
									
									for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedido.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
										
										if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(codigoTipoAutorizacion) 
												&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpAutorizacion() != null
												&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpAutorizacion().getSecuencial() != null
												&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpAutorizacion().getSecuencial().equals(secuencial)){
											
											//se cambia el estado de la autorizacion
											autorizacionActual.getEstadoPedidoAutorizacionDTO().setEstado(estadoAutSispe);
											autorizacionActual.setEstadoAutorizacion(estadoAutSispe);
											autorizacionActual.getEstadoPedidoAutorizacionDTO().setNumeroProceso(processCode);
											autorizacionActual.getEstadoPedidoAutorizacionDTO().setCodigoAutorizacion(Long.parseLong(codigoAutorizacion));
											
											//si el detalle tiene autorizacion de stock y es canasto especial se verifican los detalles para cambiar el estado de la receta
											if(obtenerCodigoClasificacion(detallePedido).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
														|| detallePedido.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
												
												//toca recorres los detalles del canasto y verificar los detalles que no tiene stock
												if(CollectionUtils.isNotEmpty(detallePedido.getArticuloDTO().getArticuloRelacionCol())
														&& detallePedido.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
													
													//se recorren los detalles del canasto especial
													for(ArticuloRelacionDTO articuloRelacionDTO : detallePedido.getArticuloDTO().getArticuloRelacionCol()){
														
														if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
																&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
																&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
															
															//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
															if(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() !=null && detallePedido.getEstadoDetallePedidoDTO().getCantidadReservarSIC() !=null){
																
																if(detallePedido.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0 
																&& detallePedido.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo()
																&& (articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() == null || articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() > 0)){
																	
																	String codigoBarrasDetalle = articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().getCodigoBarras();
																	String descripcionArticuloDetalle = articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo();
																	
																	LogSISPE.getLog().info("la autorizacion de la receta "+codigoBarrasDetalle+" - "+descripcionArticuloDetalle+" fue APROBADA");
//																	cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_APROBADA, codigoTipoAutStock, estadoAutorizacionPadre,  mapContAutorizaciones, codigoAutorizacion);
																	articuloRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_APROBADA);
																	articuloRelacionDTO.getArticuloRelacion().setNpStockArticuloAutorizado(detallePedido.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
																}
															}
														}
													}
												}
											}
											
											//se cambia el estado de las autorizaciones de stock
											if(CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
												
												for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
													
													autorizacionStockDTO.setEstadoAutorizacion(estadoAutSispe);
													LogSISPE.getLog().info("estado de la autorizacion de stock {}",autorizacionStockDTO.getEstadoAutorizacion());
													
													//se lleva la cuenta de la cantidad total autorizada
													autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
															? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
															: autorizacionStockDTO.getCantidadParcialSolicitarStock());
													autorizacionStockDTO.setCantidadUtilizada(autorizacionStockDTO.getCantidadTotalAutorizadaStock());
													LogSISPE.getLog().info("solicitado "+autorizacionStockDTO.getCantidadParcialSolicitarStock()+" autorizado "+autorizacionStockDTO.getCantidadTotalAutorizadaStock());
												}
											}
										}
									}
									
									//-------------------->
									
									//si el pedido tiene autorizacionesAnteriores se recorren las lista de autorizacionesAnteriores
									if(CollectionUtils.isNotEmpty(detallePedido.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol())){
										
										//se recorren las autorizaciones para que conserve el estado aplicado
										for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedido.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()){
										
											//se compara el tipo de autorizacion
											if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion
													&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null 
													&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().equals(codigoAutorizacion)){
												
												//si el estado es diferente es la primera vez que se aplica el estado
												if(!autorizacionActual.getEstadoAutorizacion().equals(estadoAutSispe)){
													autorizacionActual.setEstadoAutorizacion(estadoAutorizacion);
													
													//si la autorizacion fue aprobada se sumariza las autorizaciones aprobadas de ese detalle
													if(autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) 
															&& CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
														
														for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
															
															//se lleva la cuenta de la cantidad total autorizada
															autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
																	? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
																	: autorizacionStockDTO.getCantidadParcialSolicitarStock());
														}
													}
												}
											}
										}
									}
									LogSISPE.getLog().info("al "+detallePedido.getArticuloDTO().getDescripcionArticulo()+ " se  le asigna el estado "+estadoAutorizacion);
									//-------------------->
								}
							}
						}
						
//						mostrarMensajesAutorizacionesStock(infos, mapContAutorizaciones);
						
						if(estadoAutorizacion.equals(AutorizacionEstado.AUTORIZADA.getEstado())){
							request.getSession().setAttribute(COLA_AUTORIZACIONES_STOCK, colaAutorizacionesStock);
						}
//					
					} else if(codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)
							|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL)
							|| codigoTipoAutorizacion.equals(ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS)){
						
						if(estadoAutSispe.equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
							
							ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = new ec.com.smx.autorizaciones.dto.AutorizacionDTO();
							autorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							autorizacionDTO.getId().setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
							autorizacionDTO.setCodigoTipoAutorizacion(codigoTipoAutorizacion);
							autorizacionDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
							autorizacionDTO.setEstado(SessionManagerSISPE.getEstadoActivo(request));
//							autorizacionDTO.setUsuarioAutorizado(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
							autorizacionDTO.setUsuarioAutorizado(asignarUsuarioAutorizado(request));
							autorizacionDTO.getId().setCodigoAutorizacion(Long.parseLong(codigoAutorizacion));
							autorizacionDTO.setProcessCode(processCode);
							autorizacionDTO.setEstadoAutorizacion(estadoAutorizacion);
							
							Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesPedidoCol = 
									(Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL); 
							
							//se inicializa la lista de autorizaciones
							if(CollectionUtils.isEmpty(autorizacionesPedidoCol)){
								autorizacionesPedidoCol = new ArrayList<ec.com.smx.autorizaciones.dto.AutorizacionDTO>();
							}

							autorizacionesPedidoCol.add(autorizacionDTO);
							//se sube a sesion la coleccion de autorizaciones genericas
							request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL, autorizacionesPedidoCol);
							request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
							
							return Boolean.TRUE;
						} else{
							return Boolean.FALSE;
						}
					}
				}
				//se setea en el infos el mensaje de resumen
				mostrarMensajesAutorizacionesStock(infos, mapContAutorizaciones);
				
				//se busca si existen autorizaciones que no han sido gestionadas
				Boolean existeAutorizacionStockPendiente = Boolean.FALSE; //indica si existen autorizaciones de stock pendientes por aprobar
				Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK);
				if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
					for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizacionesStock){
						if(autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA) || autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
							existeAutorizacionStockPendiente = Boolean.TRUE; 
							break;
						}
					}
				}
				
				//se sube a sesion la bandera de autorizacionesPendientes y las autorizaciones workflow
				request.getSession().setAttribute(TIENE_AUTORIZACIONES_PENDIENTES_STOCK, existeAutorizacionStockPendiente);
				request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
				
				autorizacionesValidas = Boolean.TRUE;
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al validar las autorizaciones "+respuestaSistemaAutorizaciones+" "+e);
			errors.add("autorizacion", new ActionMessage("errors.gerneral","Error al validar la autorizaci\u00F3n"));
		}
		
		return autorizacionesValidas;
	}

	/**
	 * De acuerdo al estado de los articulos con aut de stock se arma el mensaje de resumen.
	 * @param infos
	 * @param mapContAutorizaciones
	 */
	private static void mostrarMensajesAutorizacionesStock(ActionMessages infos, Map<String, Integer> mapContAutorizaciones) {
		//Se recorren los valores del map
		Set<Map.Entry<String, Integer>> valoresMap = mapContAutorizaciones.entrySet();
		if(CollectionUtils.isNotEmpty(valoresMap)){
			String estadoRechazado = AutorizacionEstado.RECHAZADA.getEstado().substring(0,AutorizacionEstado.RECHAZADA.getEstado().length()-1)+"O";
			String resumenAut = "Existen Autorizaciones de STOCK con: ";
			for(Map.Entry<String, Integer> valorActual : valoresMap){
				if(valorActual.getValue() == 1){
					resumenAut += " "+valorActual.getValue()+" art\u00EDculo "+valorActual.getKey() +"," ;
				} else{
					resumenAut += " "+valorActual.getValue()+" art\u00EDculos "+valorActual.getKey() +"S," ;
				}
			} 
			resumenAut = resumenAut.substring(0, resumenAut.length()-1);
			if(resumenAut.contains(estadoRechazado)){
				String autRechazadas = "";
				if(mapContAutorizaciones.get(estadoRechazado) == 1){
					autRechazadas = "un art\u00EDculo, por favor elim\u00EDnelo";
				}else{
					autRechazadas =mapContAutorizaciones.get(estadoRechazado)+ " art\u00EDculos, por favor elim\u00EDnelos";
				}
				resumenAut = "Se rechaz\u00F3 la autorizaci\u00F3n de stock para "+autRechazadas+" o vuelva a solicitar autorizaci\u00F3n, o cambie el tipo de entrega como RESPONSABLE el LOCAL";
			}
//			infos.add("conteoAutorizaciones", new ActionMessage("errors.gerneral", resumenAut));
			CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.gerneral", "conteoAutorizaciones", resumenAut, infos);
		}
	}

	
	/**
	 * obtiene los id de usuario de los gerentes comerciales para las clasificaciones de canastos y pavos y sube a sesion los IDs
	 * @param request
	 * @throws Exception
	 */
	public static void iniciarIdGerentesComercialesCanastosPavos(HttpServletRequest request) throws Exception{
		try{
			String idUsuarioAutorizadorCanastos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_CANASTOS); 
			String idUsuarioAutorizadorPavos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_PAVOS); 
			
			//si los ids son nulos se realiza la busqueda 
			if(idUsuarioAutorizadorCanastos == null || idUsuarioAutorizadorPavos == null){
				// se obtiene de sesion las clasificaciones de pavos y canastos
				Collection<String> clasificacionesCanastos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_CANASTOS);
				Collection<String> clasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_PAVOS);

				//si la coleccion esta vacia se inicializa
				if(CollectionUtils.isEmpty(clasificacionesCanastos)){
					CotizacionReservacionUtil.inicializarClasificacionesPavosCanastos(request);
					clasificacionesCanastos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_CANASTOS);
				}
				//si la coleccion esta vacia se inicializa
				if(CollectionUtils.isEmpty(clasificacionesPavos)){
					CotizacionReservacionUtil.inicializarClasificacionesPavosCanastos(request);
					clasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_PAVOS);
				}
				
				
				if(CollectionUtils.isNotEmpty(clasificacionesCanastos) && CollectionUtils.isNotEmpty(clasificacionesPavos)){
					
					//se busca el gerente comercial de canastos
					CompradorDTO comprador = new CompradorDTO(Boolean.TRUE);
					comprador.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					comprador.getId().setCodigoComprador(null);
					comprador.setTipoComprador(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoCompradorInterno"));
					FuncionarioDTO funcionario = new FuncionarioDTO();
					funcionario.setEstadoFuncionario(CorporativoConstantes.ESTADO_ACTIVO);
					FuncionarioPerfilDTO funPerDTO = new FuncionarioPerfilDTO();
					ProfileDto perfil = new ProfileDto();
					perfil.setProfileName(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.perfilComprador.gerenteComercial"));
					funPerDTO.setPerfilDTO(perfil);
					Collection<FuncionarioPerfilDTO> perfiles = new ArrayList<FuncionarioPerfilDTO>();
					perfiles.add(funPerDTO);
					funcionario.setFuncionarioPerfiles(perfiles);
					comprador.setFuncionarioDTO(funcionario);
					Collection<ClasificacionDTO> clasificacionCol = new ArrayList<ClasificacionDTO>();
					for(String codigoClasificacion : clasificacionesCanastos){
						ClasificacionDTO clasificacionDTO = new ClasificacionDTO(Boolean.TRUE);
						clasificacionDTO.getId().setCodigoClasificacion(codigoClasificacion);
						clasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						clasificacionCol.add(clasificacionDTO);
					}
					comprador.setClasificacionCol(clasificacionCol);
					CompradorDTO gerenteAutorizadorCanastos = SISPEFactory.getDataService().findUnique(comprador);
					if(gerenteAutorizadorCanastos != null)
						request.getSession().setAttribute(ID_USUARIO_GERENTE_COMERCIAL_CANASTOS, gerenteAutorizadorCanastos.getFuncionarioDTO().getUsuarioFuncionario());
						
					//se busca el gerente comercial de pavos
					comprador.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					comprador.getId().setCodigoComprador(null);
					comprador.setTipoComprador(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoCompradorInterno"));
					funcionario = new FuncionarioDTO();
					funcionario.setEstadoFuncionario(CorporativoConstantes.ESTADO_ACTIVO);
					funPerDTO = new FuncionarioPerfilDTO();
					perfil = new ProfileDto();
					perfil.setProfileName(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.perfilComprador.gerenteComercial"));
					funPerDTO.setPerfilDTO(perfil);
					perfiles = new ArrayList<FuncionarioPerfilDTO>(); 
					perfiles.add(funPerDTO);
					funcionario.setFuncionarioPerfiles(perfiles);
					comprador.setFuncionarioDTO(funcionario);
					clasificacionCol = new ArrayList<ClasificacionDTO>();
					for(String codigoClasificacion : clasificacionesPavos){
						ClasificacionDTO clasificacionDTO = new ClasificacionDTO(Boolean.TRUE);
						clasificacionDTO.getId().setCodigoClasificacion(codigoClasificacion);
						clasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						clasificacionCol.add(clasificacionDTO);
					}
					comprador.setClasificacionCol(clasificacionCol);
					CompradorDTO gerenteAutorizadorPavos = SISPEFactory.getDataService().findUnique(comprador);
					if(gerenteAutorizadorPavos != null)
						request.getSession().setAttribute(ID_USUARIO_GERENTE_COMERCIAL_PAVOS, gerenteAutorizadorPavos.getFuncionarioDTO().getUsuarioFuncionario());
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener los id de los gerentes comerciales de canastos y pavos {}",e);
		}
	}
	
	/**
	 * retorna el ID del gerente comercial con las clasificaciones de canastos
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getIdUsuarioGerenteComercialCanastos(HttpServletRequest request) throws Exception{
		
		String idUsuarioAutorizadorCanastos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_CANASTOS); 
		if(idUsuarioAutorizadorCanastos == null){
			iniciarIdGerentesComercialesCanastosPavos(request);
			idUsuarioAutorizadorCanastos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_CANASTOS); 
		}
			
		 return idUsuarioAutorizadorCanastos;
	}
	
	/**
	 * retorna el ID del gerente comercial con las clasificaciones de pavos
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getIdUsuarioGerenteComercialPavos(HttpServletRequest request) throws Exception{
		
		String idUsuarioAutorizadorPavos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_PAVOS);
		if(idUsuarioAutorizadorPavos == null){
			iniciarIdGerentesComercialesCanastosPavos(request);
			idUsuarioAutorizadorPavos = (String)request.getSession().getAttribute(ID_USUARIO_GERENTE_COMERCIAL_PAVOS); 
		}
		return idUsuarioAutorizadorPavos;
	}
	
	/**
	 * Verifica si un articulo tiene autorizaciones del codigoTipoAutorizacion
	 * @param detallePedidoDTO
	 * @param codigoTipoAutorizacion la autorizacion que se desea ver si tiene el detallePedido
	 * @return
	 */
	public static Boolean verificarArticuloPorTipoAutorizacion(DetallePedidoDTO detallePedidoDTO, Long codigoTipoAutorizacion){
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion)
					return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	/**
	 * Verifica si un articulo tiene autorizaciones del codigoTipoAutorizacion
	 * @param detallePedidoDTO
	 * @param codigoTipoAutorizacion la autorizacion que se desea ver si tiene el detallePedido
	 * @return
	 */
	public static Boolean verificarArticuloPorTipoAutorizacionYCodigo(DetallePedidoDTO detallePedidoDTO, Long codigoTipoAutorizacion, long codigoAutorizacion){
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
					
					if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null 
							&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getId().getCodigoAutorizacion().longValue() == codigoAutorizacion){
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

	
	/**
	 * elimina del pedido las autorizaciones del tipo recibido como parametro y que no su CodigoAutorizacion sea null
	 * @param codigoTipoAutorizacion
	 * @param colDetallePedido
	 */
	public static void eliminarAutorizacionesPorCodigoTipoAutorizacion(Long codigoTipoAutorizacion, Collection<DetallePedidoDTO> colDetallePedido, Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones){
		try{
			LogSISPE.getLog().info("ingresa al metodo  eliminarAutorizacionesPorCodigoTipoAutorizacion");
			if(CollectionUtils.isNotEmpty(colDetallePedido) && codigoTipoAutorizacion != null){
				//se recorre los detalles del pedido
				for(DetallePedidoDTO detalleActual : colDetallePedido){
					if(detalleActual.getEstadoDetallePedidoDTO() != null && CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						
						//se recorren las autorizaciones
						Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion &&
									autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() == null){
								autorizacionesEliminar.add(autorizacionActual);
								//se eliminan los detalles que se solicitaran stock
								detalleActual.setNpDetallesRelacionadosSinStockCol(null);
								if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
									
									Collection<EstadoPedidoAutorizacionDTO> autEliminarSession = new ArrayList<EstadoPedidoAutorizacionDTO>();
									for(EstadoPedidoAutorizacionDTO autSession : colaAutorizaciones){
										
										if(autSession.getNpTipoAutorizacion().equals(codigoTipoAutorizacion) && autSession.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
											
											autEliminarSession.add(autSession);
//											colaAutorizaciones.remove(autSession);
										}
									}
									colaAutorizaciones.removeAll(autEliminarSession);
								}
							}
						}
						detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
						if(CollectionUtils.isEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
						}
					}
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al eliminar las autorizaciones por tipo de autrizacion {}",e);
		}
	}
	
	/**
	 * Actualiza el estado de los descuentos variables a las autorizaciones relacionadas
	 * @param request
	 * @param detallePedidoCol
	 */
	public static void aplicarEstadoDescuentoVariableAutorizacionesPedido(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol ){
		try{
			LogSISPE.getLog().info("aplicando el estado de los descuentos variables a las autorizaciones");
			
			//obteniendo de sesion la cola de autorizaciones
			ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
			if(CollectionUtils.isNotEmpty(colaAutorizaciones) && CollectionUtils.isNotEmpty(detallePedidoCol)){
				// se recorre la cola de autorizaciones
				for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
					//se recorre los detalles del pedido
					for(DetallePedidoDTO detalleActual : detallePedidoCol){
						if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							//se recorre los detalles de autorizaciones
							for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
								//se comparan los Ids
								if(autorizacionActual.getId().getCodigoEstadoPedidoAutorizacion() != null && detalleAutorizacion.getId().getCodigoEstadoPedidoAutorizacion() != null &&
										autorizacionActual.getId().getCodigoEstadoPedidoAutorizacion().equals(detalleAutorizacion.getId().getCodigoEstadoPedidoAutorizacion())
										&& autorizacionActual.getId().getCodigoCompania().equals(detalleAutorizacion.getId().getCodigoCompania())){
									//se actualizan los campos del objeto de sesion
									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setCodigoAutorizacion(autorizacionActual.getCodigoAutorizacion());
									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNumeroProceso(autorizacionActual.getNumeroProceso());
									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setEstado(autorizacionActual.getEstado());
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al aplicar el estado de los descuentos variables a las autorizaciones {}",e);
		}
	}
	
	/**
	 * no muestra las autorizaciones que han sido solicitadas fuera de un pedido consolidado
	 * @param request
	 * @param detallePedidoDTO
	 */
	public static void eliminarAutorizacionesNoSonConsolidadas(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO){
		try{
			LogSISPE.getLog().info("Ingresa al metodo eliminarAutorizacionesNoSonConsolidadas");
			if(detallePedidoDTO != null && detallePedidoDTO.getEstadoDetallePedidoDTO() != null 
					&& CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
				
				//se obtiene la coleccion con las autorizaciones a inactivar
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
				if(CollectionUtils.isEmpty(autorizacionesDesactivarCol)){
					autorizacionesDesactivarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				}
					
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
					
					//solo para el caso de descuentos variable
					if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
							autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
						String processCode = autorizacionActual.getEstadoPedidoAutorizacionDTO().getNumeroProceso();
						if(processCode != null && !processCode.equals("")){
							Collection<WorkItemDTO> itemEncontrados = new ArrayList<WorkItemDTO>();
							WorkItemDTO workItemConsulta = new WorkItemDTO();
							workItemConsulta.setProcessCode(processCode);
							workItemConsulta.addCriteriaSearchParameter("dataKeyMessage", ComparatorTypeEnum.LIKE_ANYWHERE_COMPARATOR, "PEDIDO CONSOLIDADO");
							itemEncontrados = WorkflowFactory.getWorklistService().findWorkItem(workItemConsulta);
							LogSISPE.getLog().info("processCode: "+processCode+ " del pedido: "+detallePedidoDTO.getId().getCodigoPedido()+ " workItems encontrados: "+itemEncontrados.size());
							//no existe items consolidados, 
							if(CollectionUtils.isEmpty(itemEncontrados)){
								autorizacionesEliminar.add(autorizacionActual);
								autorizacionActual.setEstado(CorporativoConstantes.ESTADO_INACTIVO);
								autorizacionesDesactivarCol.add(autorizacionActual);
							}
						}
					}
				}
				LogSISPE.getLog().info("autorizaciones eliminadas por no pertenecer a pedidos consolidados: {}",autorizacionesEliminar.size());
				detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
				if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()))
					detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);

				//se sube a sesion la coleccion con las autorizaciones inactivas
				request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL, autorizacionesDesactivarCol );
			}
		}catch (Exception e) {
		 LogSISPE.getLog().error("Error al eliminar las autorizaciones que no son generadas desde la consolidacion {}",e);
		}
	}

	
	/**
	 * deja las autorizaciones originales cuando se va a desconsolidar los pedidos
	 * @param request
	 * @param detallePedidoDTOCol
	 */
	public static void asignarAutorizacionesOriginalesDesconsolidados( HttpServletRequest request,  Collection<DetallePedidoDTO>  detallePedidoDTOCol){
		try{
			LogSISPE.getLog().info("Ingresa al metodo asignarAutorizacionesOriginalesDesconsolidados");
			if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
				
				//guarda las autorizaciones que van a ser desactivadas
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesActivarCol = (HashSet<DetalleEstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTIVAR_COL);
				if(CollectionUtils.isEmpty(autorizacionesActivarCol))
					autorizacionesActivarCol = new HashSet<DetalleEstadoPedidoAutorizacionDTO>();
				
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesctivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
				if(CollectionUtils.isEmpty(autorizacionesDesctivarCol))
					autorizacionesDesctivarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					
					//Se consulta si tiene autorizaciones
					
					DetalleEstadoPedidoAutorizacionDTO detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
					EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
					estadoPedidoAutorizacionDTO.setAutorizacionDTO(new ec.com.smx.autorizaciones.dto.AutorizacionDTO());
					estadoPedidoAutorizacionDTO.getAutorizacionDTO().setCodigoTipoAutorizacion(ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE);
					//se omite el estado de la autorizacion
//					estadoPedidoAutorizacionDTO.setEstado(ConstantesGenerales.ESTADO_AUT_APROBADA);
					detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
					detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
					detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo());
					detalleAutorizaciones.getId().setCodigoPedido(detallePedidoDTO.getId().getCodigoPedido());
					detalleAutorizaciones.getId().setSecuencialEstadoPedido(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido());
					detalleAutorizaciones.getId().setCodigoArticulo(detallePedidoDTO.getId().getCodigoArticulo());
					Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesCol = SISPEFactory.getDataService().findObjects(detalleAutorizaciones);
					if(CollectionUtils.isNotEmpty(autorizacionesCol)){
						//coleccion para las autorizaciones que van a ser eliminadas
						Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
						for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : autorizacionesCol){
							String processCode = detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getNumeroProceso();
							
							//se asigna el tipo de autorizacion
							if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null && detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion() != null)
								detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpTipoAutorizacion(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion());
								
							if(processCode != null && !processCode.equals("")){
								//se quitan las autorizaciones que fueron creadas desde CONSOLIDACION
								Collection<WorkItemDTO> itemEncontrados = new ArrayList<WorkItemDTO>();
								WorkItemDTO workItemConsulta = new WorkItemDTO();
								workItemConsulta.setProcessCode(processCode);
								workItemConsulta.addCriteriaSearchParameter("dataKeyMessage", ComparatorTypeEnum.LIKE_ANYWHERE_COMPARATOR, "PEDIDO CONSOLIDADO");
								itemEncontrados = WorkflowFactory.getWorklistService().findWorkItem(workItemConsulta);
								LogSISPE.getLog().info("processCode: "+processCode+ " del pedido: "+detallePedidoDTO.getId().getCodigoPedido()+ " workItems encontrados: "+itemEncontrados.size());
								if(CollectionUtils.isNotEmpty(itemEncontrados)){
									autorizacionesEliminar.add(detalleAutorizacion);
									detalleAutorizacion.setEstado(CorporativoConstantes.ESTADO_INACTIVO);
									autorizacionesDesctivarCol.add(detalleAutorizacion);
								}
							}
						}
						//se eliminan las autorizaciones consolidadas
						autorizacionesCol.removeAll(autorizacionesEliminar);
						if(CollectionUtils.isEmpty(autorizacionesCol)){
							autorizacionesEliminar = null;
						}
						//si existen mas de una autorizacion se elige la de menor secuenciaEstadoPedido
						else if(autorizacionesCol.size() > 1){
							//se ordena la colleccion de autorizacion por el secuencial estadoPedio
							ColeccionesUtil.sort(autorizacionesCol, ColeccionesUtil.ORDEN_ASC, "id.secuencialEstadoPedido");
							//se toma las autorizaciones que tengan el primer secuencial 
							DetalleEstadoPedidoAutorizacionDTO primerAutorizacion = autorizacionesCol.iterator().next();
							
							primerAutorizacion.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
							
							//si la coleccion esta vacia se inicializa
							if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>());
							}
							//se agrega al detalle la autorizacion
							detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().add(primerAutorizacion);
							
							autorizacionesActivarCol.add(primerAutorizacion);
						}
						else{
							autorizacionesCol.iterator().next().setEstado(CorporativoConstantes.ESTADO_ACTIVO);
							//si la coleccion esta vacia se inicializa
							if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>());
							}
							//se agrega al detalle la autorizacion
							detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().addAll(autorizacionesCol);
							
							autorizacionesActivarCol.addAll(autorizacionesCol);
							LogSISPE.getLog().info("Tiene "+autorizacionesCol.size()+" autorizacion(es) el articulo "+detallePedidoDTO.getId().getCodigoArticulo()+" del pedido "+detallePedidoDTO.getId().getCodigoPedido());
						}
					}
				}
				 request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTIVAR_COL, autorizacionesActivarCol);
				 request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL, autorizacionesDesctivarCol);
			}
		}catch (Exception e) {
		 LogSISPE.getLog().error("Error al eliminar las autorizaciones que no son generadas desde la consolidacion {}",e);
		}
	}
	
	
	/**
	 * elimina la llave de autorizaciones no validas
	 * @param colaAutorizaciones
	 * @param opDescuentos
	 * @param codigoAutorizacion
	 * @param processCode
	 */
	public static void eliminarLlaveDeAutorizacionesNoValidas( ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones, String[] opDescuentosParam, Long codigoAutorizacion, String processCode){
		LogSISPE.getLog().info("ingresa al metodo eliminarLlaveDeAutorizacionesNoValidas");
		
		if(CollectionUtils.isNotEmpty(colaAutorizaciones) && opDescuentosParam != null && opDescuentosParam.length > 0){
			
			String[] opDescuentos = opDescuentosParam;
			
			int contNulos = 0;
			//se recorre la cola de autorizaciones
			for(EstadoPedidoAutorizacionDTO autorizacionAct : colaAutorizaciones){
				
				//se busca la autorizacion rechazada en la cola de autorizaciones
				if(autorizacionAct.getCodigoAutorizacion().equals(codigoAutorizacion) && autorizacionAct.getNumeroProceso().equals(processCode)){
					//se recorre el opDescuentos
					for(int i=0; i<opDescuentos.length; i++){
						String llaveOpDscto = opDescuentos[i];
						if(StringUtils.isNotEmpty(llaveOpDscto)  &&  llaveOpDscto.contains(CODIGO_GERENTE_COMERCIAL)){
							//CTD3-CMDMON-COM01-INX1
							llaveOpDscto = llaveOpDscto.replace(llaveOpDscto.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
							if(llaveOpDscto.split(SEPARADOR_TOKEN).length == 4){
								//CTD3-CMDMON-COM01
								llaveOpDscto = llaveOpDscto.replace(SEPARADOR_TOKEN+llaveOpDscto.split(SEPARADOR_TOKEN)[3], "");
							}
							//se compara la autorizacion con el opDescuento
							if(autorizacionAct.getValorReferencial().contains(llaveOpDscto)){
								LogSISPE.getLog().info("Llave eliminada de opDescuentos {}", opDescuentos[i]);
								opDescuentos[i] = null;
								contNulos++;
							}
						}
					}
				}
			}
			//se eliminan los nulos
			if(contNulos > 0){
				String[] nuevoOpDescuentos = new String[opDescuentos.length - contNulos];
				int posNuevo = 0;
				for(int i=0; i<opDescuentos.length; i++){
					if(opDescuentos[i] != null && !opDescuentos.equals("")){
						nuevoOpDescuentos[posNuevo] = opDescuentos[i];
						posNuevo++;
					}
				}
				opDescuentos = nuevoOpDescuentos;
			}
		}
	}
	
	
	/**
	 * Llena la coleccion con los datos de las autorizaciones que han sido solo solicitadas y se borran las autorizaciones de dscto variable
	 * @param colaAutorizaciones
	 * @param request
	 * @throws Exception
	 * @author bgudino
	 */
	public static void crearAutorizacionesFinalizarWorkflow(Collection<DetalleEstadoPedidoAutorizacionDTO> colaAutorizaciones, HttpServletRequest request) throws Exception{
		
		LogSISPE.getLog().info("se llenan las autorizaciones que seran desactivadas porque no fueron aprobadas y el usuario elimino las autorizaciones");

		Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
		
		if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
			
			//se inicia la coleccion
			if(CollectionUtils.isEmpty(autorizacionesFinalizarWorkflow)){
				autorizacionesFinalizarWorkflow = new ArrayList<String>();
			}
			String processCode;
			String detalle;
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
				processCode = autorizacionActual.getEstadoPedidoAutorizacionDTO().getNumeroProceso();
				
				//se arma las autorizaciones del workflow   --> processCode,estadoAutorizacion,companyID,sistemID,userID
				if(processCode != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){
					//F17-6937,SOLICITADA,1,SISAUT,SISPE84
					detalle = processCode+SEPARADOR_COMA+AutorizacionEstado.SOLICITADA.getEstado()+
							SEPARADOR_COMA+SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania()+
							SEPARADOR_COMA+MessagesWebSISPE.getString("security.SYSTEM_ID_AUTORIZACIONES")+
							SEPARADOR_COMA+asignarUsuarioAutorizado(request);
				    //se valida que no ingrese autorizaciones repetidas
					if(!autorizacionesFinalizarWorkflow.contains(detalle)){
				    	autorizacionesFinalizarWorkflow.add(detalle);
				    }
					LogSISPE.getLog().info("autorizacion que sera cancelada: {}",detalle);
				}
			}
			
			//se sube a sesion 
			request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
		}
	}
	
	
	/**
	 * Valida si existen autorizaciones de descuento variable en estado pendiente
	 * @param colaAutorizaciones
	 * @return true si existen autorizaciones por aplicar, false caso contrario
	 */
	public static Boolean existenAutorizacionesPorAplicar(Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones){
		Boolean existenAutorizacionesPendiente = Boolean.FALSE;
		
		if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
			//se recorre la cola de autorizaciones
			for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
				//se valida si existen autorizaciones pendientes
				if(autorizacionActual.getNpTipoAutorizacion().equals(ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()) 
						&& autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
					existenAutorizacionesPendiente = Boolean.TRUE;
					break;
				}
			}
		}
		return existenAutorizacionesPendiente;
	}
	
	
	/**
	 * Valida las autorizaciones en el nuevo componente de autorizaciones ya sea por numero de autorizacion o por usuario y contrase\u00F1a,
	 * en el caso de usuario-contrase\u00F1a, se validan los datos y retorna una nueva autorizacion
	 * @param formulario
	 * @param request
	 * @param exitos
	 * @param infos
	 * @param errors
	 * @return true si la validacion es exitosa, false caso contrario
	 * @throws Exception
	 */
	public static Boolean validarAutorizacionPorNumeroUsuarioContrasenia(ActionForm formularioGenerico, HttpServletRequest  request, 
			ActionMessages exitos,  ActionMessages infos, ActionMessages errors, Long codigoTipoAutorizacion) throws Exception{
		
		CotizarReservarForm formulario = asignarValoresAutorizacionesEnFormulario(formularioGenerico);
		
		String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		Boolean validarAutorizacion = Boolean.FALSE;
		String descripcionAutorizacion = "";
		
		//se valida el tipo de autorizacion
		if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_ANULAR_PEDIDO.longValue()){
			validarAutorizacion = Boolean.TRUE;
			descripcionAutorizacion = "ANULAR PEDIDO";
			
		} else if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_CONSOLIDAR_MINIMO_PEDIDOS.longValue()){
			validarAutorizacion = Boolean.TRUE;
			descripcionAutorizacion = "CONSOLIDAR MINIMO DE PEDIDOS";
			
		}else if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO.longValue()){
			descripcionAutorizacion = "DISMINUIR EL VALOR RECOMENDADO DEL ABONO";
		}
		
		//se valida la autorizaci\u00F3n solo cuando se reserva o modifica una reserva
		if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
				|| accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
				|| validarAutorizacion ){
			String tipoValidacion = "";
			//se obtiene de sesion la coleccion de autorizaciones
			Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesPedidoCol = 
					(Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>)request.getSession().getAttribute(AUTORIZACIONES_PEDIDO_COL);
			
			//se inicializa la coleccion de autorizaciones
			if(CollectionUtils.isEmpty(autorizacionesPedidoCol)){
				autorizacionesPedidoCol = new ArrayList<ec.com.smx.autorizaciones.dto.AutorizacionDTO>();
			}
			
			Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
			//se inicia la coleccion
			if(CollectionUtils.isEmpty(autorizacionesFinalizarWorkflow)){
				autorizacionesFinalizarWorkflow = new ArrayList<String>();
			}
			
			try{
//				Long codigoTipAutAbonoCero = ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO.longValue();
				Boolean autorizacionValida = Boolean.FALSE; //el resultado de validar el numero ingresado o el usuario y contrase\u00F1a
				
				String validarPorNumeroAutorizacion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.validarpor.numero.autorizacion");
				String validarPorUsuarioContrasenia = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.validarpor.usuario.contrasenia");
				//validacion por numero de autorizacion
				if(StringUtils.isNotEmpty(formulario.getNumeroAutorizacion())
						&& StringUtils.isNotEmpty(formulario.getOpAutorizacion()) && formulario.getOpAutorizacion().equals(validarPorNumeroAutorizacion)){
					
					LogSISPE.getLog().info("validando autorizacion por numero de autorizacion");
					tipoValidacion = "n\u00FAmero de autorizaci\u00F3n";
					//se consulta la autorizacion del numero ingresado
					ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = new ec.com.smx.autorizaciones.dto.AutorizacionDTO();
					autorizacionDTO.setNumeroAutorizacion(Long.parseLong(formulario.getNumeroAutorizacion().trim()));
					autorizacionDTO.setCodigoTipoAutorizacion(codigoTipoAutorizacion);
//					autorizacionDTO.setUsuarioAutorizado(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					autorizacionDTO.setUsuarioAutorizado(asignarUsuarioAutorizado(request));
					
					String respuestaSistemaAutorizaciones = AutorizacionesFactory.getInstancia().getaIAutorizacionesServicio().transValidarAutorizacion(autorizacionDTO);
					LogSISPE.getLog().info("resultado de la validacion de la autorizacion: {}" ,respuestaSistemaAutorizaciones);
					
					//IDENTIFICADOR : CODIGOAUTORIZACION : PROCESSCODE : ESTADOAUTORIZACION
					//0:21094:F17-2818:6:AUTORIZADA
					String[] respuesta = respuestaSistemaAutorizaciones.split(SEPARADOR_DOS_PUNTOS); 
					if(StringUtils.isNotEmpty(respuesta[0])){
						//para el caso 0 = validacion correcta
						if(respuesta[0].equals("0") && respuesta.length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
							autorizacionDTO.getId().setCodigoAutorizacion(Long.parseLong(respuesta[1]));
							autorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							autorizacionDTO.getId().setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
							autorizacionDTO.setProcessCode(respuesta[2]);
							autorizacionDTO.setEstadoAutorizacion(respuesta[4]);
							autorizacionDTO.setObservacion(autorizacionDTO.getObservacion()+" - "+formulario.getObservacionAutorizacion());
						}else{
							autorizacionDTO = null;
							//los casos de error
							switch (Integer.parseInt(respuesta[0])) {
							case 1:
								errors.add("autorizacion", new ActionMessage("errors.gerneral","Error, la autorizaci\u00F3n esta destinada a otro usuario"));
								break;
							case 2:
								errors.add("autorizacion", new ActionMessage("errors.gerneral","Error, no existe una autorizaci\u00F3n con el n\u00FAmero ingresado"));
								break;
							case 3:
								errors.add("autorizacion", new ActionMessage("errors.gerneral","Error, el tipo de autorizaci\u00F3n no corresponde con la solicitada"));
								break;
							case 4:
								errors.add("autorizacion", new ActionMessage("errors.gerneral","Error, la autorizaci\u00F3n ha sido usada en otro momento"));
								break;
							case 5:
								errors.add("autorizacion", new ActionMessage("errors.gerneral","Error, la autorizaci\u00F3n esta caducada"));
								break;
							}
						}
					}
				
					//si la autorizacion existe
					if(autorizacionDTO != null){
						autorizacionesPedidoCol.add(autorizacionDTO);
						
						//se arma las autorizaciones del workflow   --> processCode,estadoAutorizacion,companyID,sistemID,userID
						autorizacionesFinalizarWorkflow.add(obtenerAutorizacionPorFinalizar(request, null, autorizacionDTO));
						
						//mensaje de exito
						exitos.add("exitoAutorizacion",new ActionMessage("message.exito","La Autorizaci\u00F3n",""));
						//se sube a sesion la coleccion de autorizaciones estado pedido
						request.getSession().setAttribute(AUTORIZACIONES_PEDIDO_COL, autorizacionesPedidoCol);
						request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
						autorizacionValida = Boolean.TRUE;
					}
				} else if(StringUtils.isNotEmpty(formulario.getLoginAutorizacion()) && StringUtils.isNotEmpty(formulario.getPasswordAutorizacion())
						&& StringUtils.isNotEmpty(formulario.getOpAutorizacion()) && formulario.getOpAutorizacion().equals(validarPorUsuarioContrasenia)){
					//validacion por usuario contrase\u00F1a
					LogSISPE.getLog().info("validando autorizacion por usuario-contrase\u00F1a");
					tipoValidacion = "usuario-contrase\u00F1a";
					
					//se crea la nueva autorizacion
					ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = new ec.com.smx.autorizaciones.dto.AutorizacionDTO();
					autorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					autorizacionDTO.getId().setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
					autorizacionDTO.setCodigoTipoAutorizacion(codigoTipoAutorizacion);
					autorizacionDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					autorizacionDTO.setEstado(SessionManagerSISPE.getEstadoActivo(request));
					autorizacionDTO.setDescripcion("Autorizacion creada a partir de validacion del usuario y contrase\u00F1a para "+descripcionAutorizacion);
//					autorizacionDTO.setUsuarioAutorizado(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					autorizacionDTO.setUsuarioAutorizado(asignarUsuarioAutorizado(request));
					autorizacionDTO.setObservacion(formulario.getObservacionAutorizacion());
//					autorizacionDTO.setAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString());
					autorizacionDTO.setAreaTrabajo(asignarAreaTrabajoAutorizacion(request));
//					autorizacionDTO.setValoresComponente(new ArrayList<DynamicComponentValueDto>());
					
					//Se crea el AreaTrabajoID
					AreaTrabajoID areaTrabajoID = new AreaTrabajoID();
					areaTrabajoID.setCodigoAreaTrabajo(Integer.parseInt(asignarAreaTrabajoAutorizacion(request)));
					areaTrabajoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					
					try{
						AutorizacionesFactory.getInstancia().getaIAutorizacionesServicio().
						transSolicitarAndAprobarAutorizacion(autorizacionDTO, formulario.getLoginAutorizacion(), formulario.getPasswordAutorizacion(), areaTrabajoID);
						
						autorizacionesPedidoCol.add(autorizacionDTO);
						
						//se arma las autorizaciones del workflow   --> processCode,estadoAutorizacion,companyID,sistemID,userID
						autorizacionesFinalizarWorkflow.add(obtenerAutorizacionPorFinalizar(request, null, autorizacionDTO));
						
						//mensaje de exito
						exitos.add("exitoAutorizacion",new ActionMessage("message.exito","La Autorizaci\u00F3n",""));
						//se sube a sesion la coleccion de autorizaciones estado pedido
						request.getSession().setAttribute(AUTORIZACIONES_PEDIDO_COL, autorizacionesPedidoCol);
						request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesFinalizarWorkflow);
						autorizacionValida = Boolean.TRUE;
					}catch (Exception e) {
						LogSISPE.getLog().error("error al validar usuario-contrase\u00F1a desde el componente de autorizaciones {}",e);
						errors.add("errorValidacion", new ActionMessage("errors.gerneral","Error, los datos del usuario son inv&aacute;lidos"));
						autorizacionValida = Boolean.FALSE;
					}
				}
				return autorizacionValida;
			}catch (Exception e) {
				LogSISPE.getLog().error("Error al validar la autorizacion por "+tipoValidacion+" "+e);
				//agregar mensaje de error al validar autorizacion
				errors.add("errorValidacion", new ActionMessage("errors.gerneral","Error al validar la autorizaci\u00F3n por "+tipoValidacion));
				return Boolean.FALSE;
			}
		}else{
			infos.add("accionIncorrecta",new ActionMessage("info.crear.autorizacion.accion.actual.incorrecta"));
			return false;
		}
	}
	
	/**
	 * Asigna los valores del formulario necesarios para validar una autorizacion por numero o usuario/contrasena
	 * @param formularioGenerico
	 * @return
	 */
	public static CotizarReservarForm asignarValoresAutorizacionesEnFormulario(ActionForm formularioGenerico){
		
		CotizarReservarForm formulario = new CotizarReservarForm();
		if(formularioGenerico instanceof AnulacionesForm){
			LogSISPE.getLog().info("seteando los datos del formulario tipo AnulacionesForm");
			AnulacionesForm form = (AnulacionesForm) formularioGenerico;
			formulario.setNumeroAutorizacion(form.getNumeroAutorizacion());
			formulario.setOpAutorizacion(form.getOpAutorizacion());
			formulario.setLoginAutorizacion(form.getLoginAutorizacion());
			formulario.setPasswordAutorizacion(form.getPasswordAutorizacion());
			formulario.setObservacionAutorizacion(form.getObservacionAutorizacion());
		}else if(formularioGenerico instanceof CotizarReservarForm){
			formulario = (CotizarReservarForm) formularioGenerico;
		}
		return formulario;
		
	}
	
	/**
	 * Al momento de confirmar la reserva, se valida si cambio la configuraci\u00F3n de las entregas para conservar o eliminar las autorizaciones de stock
	 * @param detallePedidoDTOCol
	 * @param request
	 */
	public static void verificarResponsableEntrega(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request){
		LogSISPE.getLog().info("Se verifica si la mercaderia se va a pedir al Centro de Distribucion(CD) para validar o no el stock");
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			//se obtiene de sesion las autorizaciones de stock
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES_STOCK);
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStockEliminar = new ArrayList<EstadoPedidoAutorizacionDTO>();
			Long codigoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue(); 
			
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){ 
				//se recorren las entregas
				if(detallePedidoDTO != null && CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
					Boolean existeConfDomicilio = Boolean.FALSE;
					//se verifica en todas las entregas si hay entregas que toman del CD
					Boolean eliminarAutorizacionStock = Boolean.FALSE;
					for(EntregaDetallePedidoDTO entregaDTO : (ArrayList<EntregaDetallePedidoDTO>) detallePedidoDTO.getEntregaDetallePedidoCol()){
						//se valida si NO se va a tomar el stock del CD 
						if(entregaDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega"))
								|| entregaDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.parcialBodega"))){
							existeConfDomicilio = Boolean.TRUE;
							break;
						}
					}
					
					
					if(!existeConfDomicilio){
						detallePedidoDTO.setNpSinStockPavPolCan(false);
						if(verificarArticuloPorTipoAutorizacion(detallePedidoDTO, codigoAutorizacionStock)){
							eliminarAutorizacionStock = Boolean.TRUE;
						}
					}else{
						LogSISPE.getLog().info("validar las cantidades solicitadas");
					}
					//se eliminan las autorizaciones de stock porque el stock no sera tomado del CD
					if(eliminarAutorizacionStock && CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						
						//coleccion de las autorizaciones a ser eliminadas
						Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
						//se recorren las autorizaciones
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoAutorizacionStock){
								autorizacionesEliminarCol.add(autorizacionActual);
								LogSISPE.getLog().info("Autorizacion de stock eliminada del articulo "+detallePedidoDTO.getId().getCodigoArticulo()+"-"+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
								//se elimina la autorizacion de sesion
								if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
									for(EstadoPedidoAutorizacionDTO estadoPedidoAut : colaAutorizacionesStock){
										if( estadoPedidoAut.getNpTipoAutorizacion().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion())
												&& estadoPedidoAut.getId().getCodigoCompania().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getId().getCodigoCompania())
												&& estadoPedidoAut.getCodigoAutorizacion().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion())
												&& estadoPedidoAut.getNumeroProceso().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNumeroProceso())){
//											colaAutorizacionesStock.remove(estadoPedidoAut);
											colaAutorizacionesStockEliminar.add(estadoPedidoAut);
											break;
										}
									}
								}
							}
						}
						
						//se quitan las autorizaciones
						detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminarCol);
						if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
						}
					}
				}
			}
			
			//para borrar la autorizacion de sesion se valida que los demas articulos no dependan de esas autorizaciones
			if(CollectionUtils.isNotEmpty(colaAutorizacionesStockEliminar)){
				//se recorren los detalles del pedido
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						//se recorren las autorizaciones
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoAutorizacionStock){
								//se elimina la autorizacion de sesion
								if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
									for(EstadoPedidoAutorizacionDTO estadoPedidoAut : colaAutorizacionesStock){
										if( estadoPedidoAut.getNpTipoAutorizacion().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion())
												&& estadoPedidoAut.getId().getCodigoCompania().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getId().getCodigoCompania())
												&& estadoPedidoAut.getCodigoAutorizacion().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion())
												&& estadoPedidoAut.getNumeroProceso().equals(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNumeroProceso())){
											colaAutorizacionesStockEliminar.remove(estadoPedidoAut);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
			//se quitan las autorizaciones
			if(CollectionUtils.isNotEmpty(colaAutorizacionesStockEliminar) && CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
				colaAutorizacionesStock.removeAll(colaAutorizacionesStockEliminar);
			}
			
			//se quitan las autorizaciones eliminadas de sesion
			request.getSession().setAttribute(COLA_AUTORIZACIONES_STOCK, colaAutorizacionesStock);
			if(CollectionUtils.isEmpty(colaAutorizacionesStock)){
				request.getSession().removeAttribute(TIENE_AUTORIZACIONES_PENDIENTES_STOCK);
			} else {
				//se sube a sesion la bandera de estado de autorizaciones de stock
				request.getSession().setAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK, verificarExistenAutorizacionesStockPendientes(request, detallePedidoDTOCol));
			}
		}
	}
	
	
	/**
	 * Para el caso de las autorizaciones de stock, se procesan el estado de cada autorizacion hija dependiendo del codigo de la autorizacion padre
	 * @param estadoPedidoAutorizacionDTO
	 * @param request
	 * @param codigoTipoAutorizacion
	 * @throws Exception 
	 */
	private static void procesarAutorizacionesHijas(EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO, HttpServletRequest request, Long codigoTipoAutorizacion, String estadoAutorizacionPadre,
			Map<String, Integer> mapContAutorizaciones, Long codigoAutorizacion, ActionMessages errors) throws Exception{
		
		LogSISPE.getLog().info("se procesan las autorizaciones hijas");
		
		Long codigoTipoAutStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
		
		if(codigoTipoAutorizacion == codigoTipoAutStock){
			
			//se obtiene las clasificaciones que necesitan autorizacion de stock
			Collection<String> clasificacionesAutorizacionGerenteComercial = obtenerClasificacionesAutorizacionGerenteComercial(request);
			
			//se consulta la autorizacion padre
			ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = AutorizacionesFactory.getInstancia().
					getaIAutorizacionesServicio().transBuscarAutorizacionPorId(estadoPedidoAutorizacionDTO.getNpAutorizacion().getCodigoAutorizacion());
			
			//se obtiene de sesion el detalle del pedido
			ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			
			//si la autorizacion tiene autorizaciones hijas y existen detalles del pedido
			if(autorizacionDTO != null && CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionesHijas()) && CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				//codigos de los componentes de autorizaciones usados para detalles de cada articulo hijo
				Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
				Long codigoComponenteRadiodArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.radio.articulo"));//101
				
				LogSISPE.getLog().info("la autorizacion "+autorizacionDTO.getId().getCodigoAutorizacion()+" tiene "+autorizacionDTO.getAutorizacionesHijas().size()+
						" autorizaciones hijas.");
				
				//se recorren las autorizaciones hijas
				for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionHijaActual : autorizacionDTO.getAutorizacionesHijas()){
					
					if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getValoresComponente())){
						String codigoBarras = obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
						//el estado de la autorizacion  puede tener los siguientes estados 0=RECHAZADA, 1=APROBADA, P=PENDIENTE
						String valorComponente = obtenerValorComponentePorID(codigoComponenteRadiodArt, autorizacionHijaActual);
						char estadoAutorizacion = ' ';
						if(StringUtils.isNotEmpty(valorComponente)){
							estadoAutorizacion= valorComponente.charAt(0);
						}else {
							LogSISPE.getLog().error("el estado de la autorizacion hija "+ autorizacionHijaActual.getId().getCodigoAutorizacion() +" esta vacia o nula");
							if(!estadoAutorizacionPadre.equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){
								errors.add("autorizacion.sin.estado", new ActionMessage("errors.verificar.estado.autorizaciones"));
							}
							break;
						}
						
						//se recorren los detalles para buscar el detalle autorizado
						for(DetallePedidoDTO detalleActual : detallePedidoCol){
							//cuando se trata de un detalle normal o de un canasto de catalogo
							if(!CotizacionReservacionUtil.esCanasto(obtenerCodigoClasificacion(detalleActual), request) || obtenerCodigoClasificacion(detalleActual).
									equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))){
								if(verificarArticuloPorTipoAutorizacion(detalleActual, codigoTipoAutStock) &&
										detalleActual.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(codigoBarras)){
									//estado 0 = rechazada, 1 = autorizada, 2 = pendiente
									switch (estadoAutorizacion) {
									//rechazado
									case '0':
										LogSISPE.getLog().info("la autorizacion del articulo "+detalleActual.getId().getCodigoArticulo()+" - "+detalleActual.getArticuloDTO().getDescripcionArticulo()+
												" fue RECHAZADA");
										cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_RECHAZADA, codigoTipoAutStock, estadoAutorizacionPadre, mapContAutorizaciones, codigoAutorizacion);
										detalleActual.setNpSinStockPavPolCan(true);
										break;
									//autorizado
									case '1':
										LogSISPE.getLog().info("la autorizacion del articulo "+detalleActual.getId().getCodigoArticulo()+" - "+detalleActual.getArticuloDTO().getDescripcionArticulo()+
												" fue APROBADA");
										cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_APROBADA, codigoTipoAutStock, estadoAutorizacionPadre,  mapContAutorizaciones, codigoAutorizacion);
										break;
									//postergado	
									case 'P':
										LogSISPE.getLog().info("la autorizacion del articulo "+detalleActual.getId().getCodigoArticulo()+" - "+detalleActual.getArticuloDTO().getDescripcionArticulo()+
												" esta pendiene por gestionar");
//										cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_SOLICITADA, codigoTipoAutStock, estadoAutorizacionPadre);
										break;
									}
								}
							}
							//si el detalle tiene autorizacion de stock y es canasto especial se verifican los detalles
							else if(verificarArticuloPorTipoAutorizacionYCodigo(detalleActual, codigoTipoAutStock, codigoAutorizacion) 
									&& (obtenerCodigoClasificacion(detalleActual).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
										|| detalleActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO))){
								
								//toca recorres los detalles del canasto y verificar los detalles que no tiene stock
								if(CollectionUtils.isNotEmpty(detalleActual.getArticuloDTO().getArticuloRelacionCol())
										&& detalleActual.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
									
									//se recorren los detalles del canasto especial
									for(ArticuloRelacionDTO articuloRelacionDTO : detalleActual.getArticuloDTO().getArticuloRelacionCol()){
										
										if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
												&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
												&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
											
											//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
											if(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() !=null && detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() !=null){
												
												if(detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0 
												&& detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo()
												&& (articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() == null || articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() > 0)){
													
													String codigoBarrasDetalle = articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().getCodigoBarras();
													String descripcionArticuloDetalle = articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo();
													
													if(codigoBarrasDetalle.equals(codigoBarras)){
														//estado 0 = rechazada, 1 = autorizada, 2 = pendiente
														switch (estadoAutorizacion) {
														//rechazado
														case '0':
															LogSISPE.getLog().info("la autorizacion del articulo "+codigoBarrasDetalle+" - "+descripcionArticuloDetalle+" fue RECHAZADA");
															articuloRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_RECHAZADA);
//																cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_RECHAZADA, codigoTipoAutStock, estadoAutorizacionPadre, mapContAutorizaciones, codigoAutorizacion);
//																detalleActual.setNpSinStockPavPolCan(false);
															break;
														//autorizado
														case '1':
															LogSISPE.getLog().info("la autorizacion del articulo "+codigoBarrasDetalle+" - "+descripcionArticuloDetalle+" fue APROBADA");
//															cambiarEstadoAutorizacionDelDetalle(detalleActual, ConstantesGenerales.ESTADO_AUT_APROBADA, codigoTipoAutStock, estadoAutorizacionPadre,  mapContAutorizaciones, codigoAutorizacion);
															articuloRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_APROBADA);
															articuloRelacionDTO.getArticuloRelacion().setNpStockArticuloAutorizado(detalleActual.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
															break;
														//postergado	
														case 'P':
															LogSISPE.getLog().info("la autorizacion del articulo "+codigoBarrasDetalle+" - "+descripcionArticuloDetalle+" esta pendiene por gestionar");
															articuloRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_SOLICITADA);
															break;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				aplicarEstadoAutorizacionStockPadre(request, detallePedidoCol, clasificacionesAutorizacionGerenteComercial, estadoAutorizacionPadre, codigoAutorizacion, mapContAutorizaciones);
				request.getSession().setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoCol);
			} 
			else if(CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				//se aplica el estado del padre a todas las autorizaciones hijas para el caso de solicitada
				for(DetallePedidoDTO detalleActual : detallePedidoCol){
					cambiarEstadoAutorizacionDelDetalle(detalleActual, estadoAutorizacionPadre, codigoTipoAutStock, estadoAutorizacionPadre, mapContAutorizaciones, codigoAutorizacion);
				}
				request.getSession().setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoCol);
			}
		}
	}
	
	/**
	 * busca el DynamicComponentValueDto asociado al idComponente
	 * @param idComponente 			codigo del componente
	 * @param autorizacionDTO		Autorizacion hija
	 * @return	DynamicComponentValueDto 
	 */
	public static String obtenerValorComponentePorID(Long idComponente, ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO){
		String dynComponenteValue = null;
		if(CollectionUtils.isNotEmpty(autorizacionDTO.getValoresComponente())){
			for(DynamicComponentValueDto componentActual : autorizacionDTO.getValoresComponente()){
				if(componentActual.getComponentId().equals(idComponente)){
					LogSISPE.getLog().info("codigoComponente: "+componentActual.getComponentId()+" valorComponente: "+componentActual.getComponentValue());
					return componentActual.getComponentValue();
				}
			}
		}
		return dynComponenteValue;
	}
	
	
	
	/**
	 * cambia el estado del objeto DetalleEstadoPedidoAutorizacionDTO al estado indicado
	 * @param detallePedidoDTO
	 * @param estadoAutorizacion
	 * @param codigoTipoAutorizacion
	 */
	private static void cambiarEstadoAutorizacionDelDetalle(DetallePedidoDTO detallePedidoDTO, String estadoAutorizacionHija, Long codigoTipoAutorizacion, String estadoAutorizacionPadre,
		Map<String, Integer> mapContAutorizaciones, Long codigoAutorizacion){
		
		//si el pedido tiene autorizaciones se recorren las lista de autorizaciones
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			
			//se recorren las autorizaciones para que conserve el estado aplicado
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
			
				//se compara el tipo de autorizacion
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion
						&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null 
						&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().equals(codigoAutorizacion)){
					
					//si el estado es diferente es la primera vez que se aplica el estado
					if(!autorizacionActual.getEstadoAutorizacion().equals(estadoAutorizacionHija)){
						
						//se cambia el estado de la autorizacion
						autorizacionActual.getEstadoPedidoAutorizacionDTO().setEstado(estadoAutorizacionPadre);
						autorizacionActual.setEstadoAutorizacion(estadoAutorizacionHija);
						
						if(CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
							
							for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
								
								//se asigna el estado de la autorizacion hija
								autorizacionStockDTO.setEstadoAutorizacion(autorizacionActual.getEstadoAutorizacion());
//								if(estadoAutorizacionHija.equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){
//									autorizacionStockDTO.setCantidadUtilizada(0);
//									autorizacionStockDTO.setCantidadTotalAutorizadaStock(0);
//								}
								
								//si la autorizacion fue aprobada se sumariza las autorizaciones aprobadas de ese detalle
								if(estadoAutorizacionHija.equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
									//se lleva la cuenta de la cantidad total autorizada
									autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
											? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
											: autorizacionStockDTO.getCantidadParcialSolicitarStock());
									autorizacionStockDTO.setCantidadUtilizada(autorizacionStockDTO.getCantidadTotalAutorizadaStock());
								}
							}
						}
						
						//se cuentan las autorizaciones
						String key = obtenerEstadoAutorizacionSispeAComponente(estadoAutorizacionHija);
						if(key.endsWith("ADA")){
							key = key.replace("ADA", "ADO");
						}
						int contador = 0;
						if(mapContAutorizaciones.containsKey(key)){
							contador = mapContAutorizaciones.get(key);
						}
						contador++;
						mapContAutorizaciones.put(key, contador);
					}
				}
			}
		}
		
		//si el pedido tiene autorizacionesAnteriores se recorren las lista de autorizacionesAnteriores
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol())){
			
			//se recorren las autorizaciones para que conserve el estado aplicado
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()){
			
				//se compara el tipo de autorizacion
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion
						&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null 
						&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().equals(codigoAutorizacion)){
					
					//si el estado es diferente es la primera vez que se aplica el estado
					if(!autorizacionActual.getEstadoAutorizacion().equals(estadoAutorizacionHija)){
						autorizacionActual.setEstadoAutorizacion(estadoAutorizacionHija);
						
						//si la autorizacion fue aprobada se sumariza las autorizaciones aprobadas de ese detalle
						if(estadoAutorizacionHija.equals(ConstantesGenerales.ESTADO_AUT_APROBADA) 
								&& CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
							
							for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
								
								//se lleva la cuenta de la cantidad total autorizada
								autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
										? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
										: autorizacionStockDTO.getCantidadParcialSolicitarStock());
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * procesa cada autorizacion de acuerdo a su estado
	 * @param colaAutorizacionesStock
	 * @param codigoAutorizacion
	 * @param secuencial
	 * @param processCode
	 * @throws Exception 
	 */
	private static void cambiarEstadoAutorizacion(Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock, Long codigoAutorizacion,
			Long secuencial, String processCode, String estadoAutorizacion, Integer cantidadAutorizacioneshijas, HttpServletRequest request,
			Long codigoTipoAutorizacion, Map<String, Integer> mapContAutorizaciones, ActionMessages errors, Boolean enviarNotificacion) throws Exception{
		if(CollectionUtils.isNotEmpty(colaAutorizacionesStock)){
			
			for(EstadoPedidoAutorizacionDTO autorizacionAct : colaAutorizacionesStock){
				if((autorizacionAct.getCodigoAutorizacion() != null && autorizacionAct.getCodigoAutorizacion().longValue() == codigoAutorizacion)
						|| autorizacionAct.getNpAutorizacion().getSecuencial().equals(secuencial)){
					autorizacionAct.setCodigoAutorizacion(new Long(codigoAutorizacion));
					autorizacionAct.setNumeroProceso(processCode);
					autorizacionAct.setEstado(estadoAutorizacion);
					autorizacionAct.getNpAutorizacion().setEnviarNotificacion(enviarNotificacion);
					//se procesan las autorizaciones hijas
					if(cantidadAutorizacioneshijas > 0 ){
						procesarAutorizacionesHijas(autorizacionAct, request, codigoTipoAutorizacion, estadoAutorizacion, mapContAutorizaciones, codigoAutorizacion, errors);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * De acuerdo al estado que retorna el componente de autorizaciones obtiene el estado de la autorizacion en SISPE
	 * Ejemp: AUTORIZADA - APR, CADUCADA - CAD, etc.. 
	 * @param estado
	 * @return el estado de la autorizacion de los objetos de SISPE
	 */
	private static String obtenerEstadoAutorizacionComponenteASispe(String estado){
		String estadoAutorizacion = null;
		if(StringUtils.isNotEmpty(estado)){
			if(estado.equals(AutorizacionEstado.AUTORIZADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_APROBADA;
			}else if(estado.equals(AutorizacionEstado.CADUCADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_CADUCADA;
			}else if(estado.equals(AutorizacionEstado.NO_USADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_NO_USADA;
			}else if(estado.equals(AutorizacionEstado.RECHAZADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_RECHAZADA;
			}else if(estado.equals(AutorizacionEstado.SOLICITADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_SOLICITADA;
			}else if(estado.equals(AutorizacionEstado.UTILIZADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_UTILIZADA;
			}else if(estado.equals(AutorizacionEstado.GESTIONADA.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_GESTIONADA;
			}else if(estado.equals(AutorizacionEstado.PENDIENTE.getEstado())){
				estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_PENDIENTE;
			}
			LogSISPE.getLog().info("Estado de la autorizacion: "+estadoAutorizacion+" - "+estado );
		}
		return estadoAutorizacion;
	}
	
	/**
	 * De acuerdo al estado que tiene la autorizacion en SISPE  retorna el estado del componente de autorizaciones
	 * Ejemp: APR - AUTORIZADA, CAD - CADUCADA, etc.. 
	 * @param estado
	 * @return el estado de la autorizacion de los objetos de SISPE
	 */
	private static String obtenerEstadoAutorizacionSispeAComponente(String estado){
		String estadoAutorizacion = null;
		if(StringUtils.isNotEmpty(estado)){
			if(estado.equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
				estadoAutorizacion = AutorizacionEstado.AUTORIZADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_CADUCADA)){
				estadoAutorizacion = AutorizacionEstado.CADUCADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_NO_USADA)){
				estadoAutorizacion = AutorizacionEstado.NO_USADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA)){
				estadoAutorizacion = AutorizacionEstado.RECHAZADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){
				estadoAutorizacion = AutorizacionEstado.SOLICITADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_UTILIZADA)){
				estadoAutorizacion = AutorizacionEstado.UTILIZADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA)){
				estadoAutorizacion = AutorizacionEstado.GESTIONADA.getEstado();
			}else if(estado.equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
				estadoAutorizacion = AutorizacionEstado.PENDIENTE.getEstado();
			}
			LogSISPE.getLog().info("Estado de la autorizacion: "+estadoAutorizacion+" - "+estado );
		}
		return estadoAutorizacion;
	}
	
	/**
	 * se valida si las autorizaciones deben finalizarse el flujo, esto es para los casos que el usuario elimino los articulos que 
	 * estaban con problema de stock antes de que el autorizador haya gestionado la solicitud de la autorizacion
	 * @param request
	 */
	@Deprecated
	public static void validarSiFinalizaFlujoAutorizaciones(HttpServletRequest request, DetallePedidoDTO  detallePedidoDTO){
		LogSISPE.getLog().info("validando las autorizaciones hijas que deben ser eliminadas para que no sigan pendientes ");
		
		Long codigoTipoAutorizacion = ConstantesGenerales.TIPO_AUTORIZACION_STOCK;
		//coleccion con los codigos de autorizaciones de los detalles que debe forzarce su finalizacion
		Collection<String> autorizacionesHijasPorFinalizar = (Collection<String>) request.getSession().getAttribute(AUTORIZACIONES_HIJAS_POR_FINALIZAR);
		if(CollectionUtils.isEmpty(autorizacionesHijasPorFinalizar)){
			autorizacionesHijasPorFinalizar = new ArrayList<String>();
		}
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				//se verifica que sea autorizacion de stock
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
					//se verifica que la autorizacion este pendiente o rechazada o caducada
					if(!autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
						//se agrega a la coleccion de autorizaciones por eliminar
						if(!autorizacionesHijasPorFinalizar.contains(autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion())){
							autorizacionesHijasPorFinalizar.add(autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().toString());
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Se eliminan las autorizaciones hijas que fueron rechazadas por el autorizador
	 * @param colDetallePedidoDTO
	 * @param request
	 */
	public static void eliminarAutorizacionesHijasRechazadas(Collection<DetallePedidoDTO> colDetallePedidoDTO, HttpServletRequest request){
		
		Boolean existenAutPendientes = Boolean.FALSE;
		//se elimina de los detalles las autorizaciones de stock ke fueron eliminados
		LogSISPE.getLog().info("eliminando las autorizaciones hijas rechazadas");
		//eliminando autorizaciones rechazadas
		if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){
			//se recorre los detalles del pedido
			for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
				if(detalleActual.getEstadoDetallePedidoDTO() != null && CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
					//se recorren las autorizaciones
					Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
					for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue() &&
								(autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA)
										|| autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_UTILIZADA)
										|| autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_CADUCADA))){
							autorizacionesEliminar.add(autorizacionActual);
							existenAutPendientes = Boolean.TRUE;
							LogSISPE.getLog().info("La autorizacion del detalle  "+detalleActual.getId().getCodigoArticulo()+" - " +detalleActual.getArticuloDTO().getDescripcionArticulo()+" sera eliminada");
						}
					}
					detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
					if(CollectionUtils.isEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
					}
				}
			}
			if(existenAutPendientes){
				request.getSession().setAttribute(TIENE_AUTORIZACIONES_PENDIENTES_STOCK, existenAutPendientes);
			}
		}
	}

	
	/**
	 * Verifica si el detalle tiene autorizacion de stock en estado APR o GES
	 * @param detallePedidoDTO
	 * @return true si tiene autorizacion de stock en estado APR o GES, else caso contrario
	 * @throws Exception 
	 * @throws MissingResourceException 
	 */
	public static Boolean verificarDetalleAutorizadoStock(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request) throws MissingResourceException, Exception{
		Long codigoTipoAutorizacion = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
					if(autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
							|| autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA)){
						
						if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
								|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
							
							//para el caso de canasto especial, se valida el stock de la receta
							if(CotizarReservarForm.verificarStockDeDetallesCanastosEspeciales(request, detallePedidoDTO, obtenerClasificacionesAutorizacionGerenteComercial(request))){
								return Boolean.FALSE;
							}
						}
						
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * @param codigoLocal				codigo del local 
	 * @param codigoTipoAutorizacion	tipo de autorizacion
	 * @param request
	 * @return	retorna el userId del usuario autorizador del local indicado
	 * @throws Exception
	 */
	private static String[] obtenerAutorizadorDeLocal(String codLoc, Long codigoTipoAutorizacion, HttpServletRequest request) throws Exception{

		HttpSession session = request.getSession();
		String stockBodega = (String) session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA);
		String codigoLocal=codLoc;
		
		if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA) != null 
				&& (session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(CONTEXTO_ENTREGA_MI_LOCAL)
						||session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL))
				&& (StringUtils.isNotEmpty(stockBodega) && (stockBodega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")) 
			    	||  stockBodega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))))
			    || (request.getSession().getAttribute("ec.com.smx.sic.sispe.validar.bodega")!=null)){
			codigoLocal = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos");
		}
		
		if(StringUtils.isNotEmpty(codigoLocal)){
			TipoAutorizadorUsuarioDTO tipAutUsu = new TipoAutorizadorUsuarioDTO();
			tipAutUsu.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			tipAutUsu.getId().setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
			tipAutUsu.getId().setCodigoTipoAutorizacion(codigoTipoAutorizacion);
			tipAutUsu.setEstado(SessionManagerSISPE.getEstadoActivo(request));
			tipAutUsu.setCriteriaSearch(new CriteriaSearch());
			tipAutUsu.setUsuarioAutorizadorDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
			tipAutUsu.getCriteriaSearch().addDistinctSearchParameter("usuarioAutorizadorDTO.userId");
			Collection<String> usuariosAutorizadoresCol = SISPEFactory.getDataService().findFieldValue(tipAutUsu,  "usuarioAutorizadorDTO.userId", true);
			
			FuncionarioDTO funcionarioConsulta = new FuncionarioDTO();
			funcionarioConsulta.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			funcionarioConsulta.setCodigoAreaTrabajo(Integer.parseInt(codigoLocal));
			funcionarioConsulta.addCriteriaSearchParameter("usuarioFuncionario", ComparatorTypeEnum.IN_COMPARATOR, usuariosAutorizadoresCol);
			Collection<FuncionarioDTO> funcionariosCol = SISPEFactory.getDataService().findObjects(funcionarioConsulta);
			
			if(CollectionUtils.isNotEmpty(funcionariosCol)){
				LogSISPE.getLog().info("registros encontrados: "+funcionariosCol.size());
				String[] funcionariosAutorizadores = new String[funcionariosCol.size()];
				int posVector = 0;
				for(FuncionarioDTO funcionarioActual : funcionariosCol){
					LogSISPE.getLog().info("El autorizador del local "+codigoLocal+" es: " +funcionarioActual.getUsuarioFuncionario());
					funcionariosAutorizadores[posVector] = funcionarioActual.getUsuarioFuncionario();
					posVector ++;
				}
				
				return funcionariosAutorizadores;
			}
		}
		return new String[0];
	}
	
	
	
	public static Long obtenerCodigoTipoAutorizacion(String respuestaSitemaAutorizaciones){
		Long codigoTipoAutorizacion = null;
		
		if(StringUtils.isNotEmpty(respuestaSitemaAutorizaciones) && respuestaSitemaAutorizaciones.split(SEPARADOR_DOS_PUNTOS).length >= TAMANIO_RESPUESTA_DESC_VARIABLE.intValue()){
			try {
				codigoTipoAutorizacion = Long.parseLong(respuestaSitemaAutorizaciones.split(SEPARADOR_DOS_PUNTOS)[3]);
			} catch (Exception e) {
				LogSISPE.getLog().error("Error al obtener el tipoDeAutorizacion del String que retorna el componente de autorizaciones {}",e);
			}
		}
		return codigoTipoAutorizacion;
	}
	
	/**
	 * Para el caso de las autorizaciones de stock y si presiona el boton confirmar Reserva y existen autorizaciones pendientes, toca que se 
	 * baya por la opcion de guardar
	 * @param request
	 * @param formulario
	 * @param errors
	 * @param warnings
	 * @param infos
	 * @throws Exception
	 */
	public static void redireccionarAutorizacionesStock(HttpServletRequest request, CotizarReservarForm formulario, ActionMessages errors, 
			ActionMessages warnings, ActionMessages infos) throws Exception{
		HttpSession session = request.getSession();
		//se redireccionan de acuerdo a la autorizacion
		if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) != null){
			String respuestaSistAut = request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"));
			
			if(session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION) != null){

				String[] vectorAut = respuestaSistAut.split(",");
				Long codigoTipoAutorizacion = AutorizacionesUtil.obtenerCodigoTipoAutorizacion(vectorAut[0]);
				if(codigoTipoAutorizacion != null && codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
					//es una autorizacion de stock
					Boolean  autorizacionValida = AutorizacionesUtil.verificarAutorizacion(respuestaSistAut, request, errors, infos);
					//nueva funcionalidad de las autorizaciones
					if(autorizacionValida){
						Boolean existeAutorizacionStockPendiente = (Boolean) session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK);
						//si no existen autorizaciones pendientes, puede confirmar el pedido
						if(!existeAutorizacionStockPendiente){
//							//pasa a confirmar la reserva -- guardarReservacion
							session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION, MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"));
							formulario.setBotonRegistrarReservacion("regReservacion");
						}else{
							//tiene que guardarse el pedido porque existen autorizaciones pendientes -- registrarCotizacion
							session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION, MessagesWebSISPE.getString("accion.autorizaciones.registrar.cotizacion"));
						}
						if (session.getAttribute(CotizarReservarAction.OPCION_MODIFICAR_RESERVA) != null){
							session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"));							
							formulario.setBotonRegistrarReservacion(null);
						}
					}
//					//funcionamiento antiguo de autorizaciones de stock (el if de autorizacionValida)
//					if(autorizacionValida){
//						if(session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_CANASTOS) != null && session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_CANASTOS).equals(Boolean.FALSE)){
//							session.setAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_CANASTOS, Boolean.TRUE);
//						}
//						if(session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_PAVOS) != null && session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_PAVOS).equals(Boolean.FALSE)){
//							session.setAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_PAVOS, Boolean.TRUE);
//						}
//						session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION, MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"));
//						formulario.setBotonRegistrarReservacion("regReservacion");
//						
//						if (session.getAttribute(CotizarReservarAction.OPCION_MODIFICAR_RESERVA) != null){
//							session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"));							
//							formulario.setBotonRegistrarReservacion(null);
//						}
//					}
					else if(errors.size() == 0){
						//se eliminan las autorizaciones de stock y las banderas 
						warnings.add("autorizacionStockCaducada", new ActionMessage("warning.autorizacion.stock.utilizada"));
					}
					
					//se elimina la ventana de la sesi\u00F3n
					session.removeAttribute(SessionManagerSISPE.POPUP);
					session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);//cambios oscar
					session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
					
					//cuando se muestra el componente de autorizaciones presionando el icono, no debe guardar o confirmar la reservacion
					if(session.getAttribute(CotizarReservarAction.BORRAR_ACCION_ANTERIOR) != null && (Boolean) session.getAttribute(CotizarReservarAction.BORRAR_ACCION_ANTERIOR)){
						session.removeAttribute(CotizarReservarAction.BORRAR_ACCION_ANTERIOR);
						session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
					}
					
					//se realiza esta validacion para que solo la primera vez que ingresa muestre todos los popUps
					if(session.getAttribute(CotizarReservarAction.PRIMER_INGRESO_AL_PEDIDO) != null){
						//se muestra el popUp de autorizaciones de descuento variable
						session.setAttribute(MOSTRAR_POPUP_AUTORIZACION_STOCK, Boolean.FALSE);
						AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, errors);
						
						session.setAttribute("ec.com.smx.sic.sispe.respaldo.warnings", warnings);
						session.setAttribute("ec.com.smx.sic.sispe.respaldo.infos", infos);
						session.setAttribute("ec.com.smx.sic.sispe.respaldo.errors", errors);
					}
				}
			}
		}
	}
	
	/**
	 * Obtiene la autorizacion de descuento variable del detallePedido
	 * @param detallePedidoActual
	 * @return
	 */
	private static DetalleEstadoPedidoAutorizacionDTO obtenerAutorizacionVariable(DetallePedidoDTO detallePedidoActual){
		Long codigoTipoAutorizacion = ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE;
		if(CollectionUtils.isNotEmpty(detallePedidoActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoActual.
					getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
					return autorizacionActual;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Remplaza las autorizaciones variable del detallePedido por el pasado como parametro
	 * @param detallePedidoActual
	 * @param autorizacion
	 */
	private static void reemplazarAutorizacionDescVariable(DetallePedidoDTO detallePedidoActual, DetalleEstadoPedidoAutorizacionDTO autorizacion){
		Long codigoTipoAutorizacion = ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE;
		Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
		Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesAgregar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
		if(CollectionUtils.isNotEmpty(detallePedidoActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoActual.
					getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacion){
					autorizacionesEliminar.add(autorizacionActual);
					if(autorizacion != null){
						autorizacionesAgregar.add(autorizacion);
					}
				}
			}
			detallePedidoActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
			detallePedidoActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().addAll(autorizacionesAgregar);
		}else if(autorizacion != null){
			detallePedidoActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>());
			detallePedidoActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().add(autorizacion);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws Exception
	 */
	public static void iniciarTipoAutorizacionGenerico(HttpServletRequest request) throws Exception{
		try{
			LogSISPE.getLog().info("Inicializando los tipos de autorizaciones genericos que servir\u00E1 para: disminuir el valor recomendado del abono , etc");
			ec.com.smx.autorizaciones.dto.TipoAutorizacionDTO tipoAutorizacionFiltro = new ec.com.smx.autorizaciones.dto.TipoAutorizacionDTO();
			tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			tipoAutorizacionFiltro.getId().setCodigoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
			tipoAutorizacionFiltro.getId().setCodigoTipoAutorizacion(Long.parseLong(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.abono.cero")));
			
			ec.com.smx.autorizaciones.dto.TipoAutorizacionDTO tipoAutorizacionDTO = (ec.com.smx.autorizaciones.dto.TipoAutorizacionDTO) SISPEFactory.getDataService().findUnique(tipoAutorizacionFiltro);
			tipoAutorizacionFiltro = null;
			if(tipoAutorizacionDTO != null){
				//se guarda la descripci\u00F3n en sesi\u00F3n
				request.getSession().setAttribute(CotizarReservarAction.DESCRICPION_USO_AUTORIZACION, "SERVIR\u00C1 PARA: "+tipoAutorizacionDTO.getNombreTipoAutorizacion());
				LogSISPE.getLog().info(" SERVIR\u00C1 PARA: "+tipoAutorizacionDTO.getNombreTipoAutorizacion());
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al consultar el tipo de autorizacion generico {}" ,e);
		}
	}
	
	
	/**
	 * Consulta y sube a sesion las colecciones de los departamentos
	 * @param request
	 */
	public static Collection<ClasificacionDTO> iniciarClasificacionesDepartamentos(HttpServletRequest request){
		
		Collection<ClasificacionDTO> clasificacionesDepartamentosCol = (Collection<ClasificacionDTO>) request.getSession().getAttribute(COL_CLASIFICACIONES_DEPARTAMENTO);
		if(CollectionUtils.isEmpty(clasificacionesDepartamentosCol)){
			//se realiza la consulta de todas la clasificaciones pertenecientes a departamento
		    ClasificacionDTO clasificacionDTOConsulta = new ClasificacionDTO();
		    clasificacionDTOConsulta.setCodigoTipoClasificacion("02"); //nivel del departamento
		    clasificacionDTOConsulta.addCriteriaSearchParameter("id.codigoClasificacion", ComparatorTypeEnum.LIKE_START_COMPARATOR,"00"); //se omiten datos de prueba
		   
		    clasificacionesDepartamentosCol = SISPEFactory.getDataService().findObjects(clasificacionDTOConsulta);
		   
		    ColeccionesUtil.sort(clasificacionesDepartamentosCol, ColeccionesUtil.ORDEN_ASC, "id.codigoClasificacion"); //se ordenan las clasificaciones
	
		    // se imprimen las clasificaciones
		    if(CollectionUtils.isNotEmpty(clasificacionesDepartamentosCol)){
//			    int contador = 1;
//			    for(ClasificacionDTO clasif : clasificacionesDepartamentosCol){
//				    LogSISPE.getLog().info(contador <10  ? "0"+contador+").- "+clasif.getId().getCodigoClasificacion()+" - "+clasif.getDescripcionClasificacion()
//				 		   : contador+").- "+clasif.getId().getCodigoClasificacion()+" - "+clasif.getDescripcionClasificacion());
//				    contador ++;
//			    }
			    //se sube a sesion la coleccion de departamentos
			    request.getSession().setAttribute(COL_CLASIFICACIONES_DEPARTAMENTO, clasificacionesDepartamentosCol);
		    }
		}
		return clasificacionesDepartamentosCol;
	}
	
	
	/**
	 * Carga las clasificaciones de los departamentos de los articulos del pedido.
	 * @param request
	 * @param errors
	 * @param warnings
	 * @param ordenarDepartamentos
	 * @throws Exception
	 */
	public static void verificarDepartamentosPedido(HttpServletRequest request,ActionMessages errors, ActionMessages warnings, Boolean ordenarDepartamentos) throws Exception {
		
		
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("verificando los departamentos de los articulos del pedido");
		
		Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		//si es pedido consolidado se verifican los departamentos de todos los pedidos
		if(ConsolidarAction.esPedidoConsolidado(request)
				&& session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null){
			//se obtiene de sesion el pedido consolidado
			detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
		}
		
		//si existen detalles del pedido
		if(CollectionUtils.isNotEmpty(detallePedidoCol)){
			
			try{
				//Consultar porcentaje maximo de descuento por establecimientos
				if(request.getSession().getAttribute(CotizarReservarAction.VALOR_MAX_DES_LOCAL)==null){
					request.getSession().setAttribute(CotizarReservarAction.VALOR_MAX_DES_LOCAL,CotizacionReservacionUtil.obtenerPorcentajeMaximoDescuentoPorEstablecimiento(request));
				}
				
//				Collection<ClasificacionDTO> clasificacionesDepartamentosCol = new ArrayList<ClasificacionDTO>(iniciarClasificacionesDepartamentos(request));
				Collection<ClasificacionDTO> clasificacionesDepartamentosCol = (Collection<ClasificacionDTO>) SerializationUtils.clone((Serializable)iniciarClasificacionesDepartamentos(request));
			
				if(CollectionUtils.isNotEmpty(clasificacionesDepartamentosCol)){
					//se recorren los detalles del pedido
					
					String departamentoArticulo = "";
					for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
						
						if(StringUtils.isEmpty(detallePedidoDTO.getNpIdAutorizador())){
							FuncionarioDTO funcionarioAutorizador = obtenerFuncionarioAutorizadorPorClasificacion(obtenerCodigoClasificacion(detallePedidoDTO),obtenerTipoMarca(detallePedidoDTO),
									request, obtenerCodigoProcesoAutorizarDescVar(request), new ActionMessages());
							detallePedidoDTO.setNpIdAutorizador(funcionarioAutorizador!=null?funcionarioAutorizador.getUsuarioFuncionario():null);
						}
						
						//se obtiene el departamento del detallePedidoDTO
						departamentoArticulo = obtenerCodigoDepartamento(detallePedidoDTO);
						
						//se recorren las clasificaciones
						for(ClasificacionDTO clasificacionDTO : clasificacionesDepartamentosCol){
							
							if(departamentoArticulo.equals(clasificacionDTO.getId().getCodigoClasificacion())){
								LogSISPE.getLog().info("el articulo "+detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()+"-"+
										detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" pertenece al departamento "
										+clasificacionDTO.getId().getCodigoClasificacion()+" - "+clasificacionDTO.getDescripcionClasificacion());
								
								if(StringUtils.isNotEmpty(detallePedidoDTO.getNpIdAutorizador())){
								
									Collection<FuncionarioDTO> autorizadoresDepartamentosCol = obtenerFuncionarioAutorizadorPorClasificacion(clasificacionDTO.getId().getCodigoClasificacion(),obtenerTipoMarca(detallePedidoDTO),
										obtenerCodigoProcesoAutorizarDescVar(request), request, Boolean.TRUE); 
									if(CollectionUtils.isNotEmpty(autorizadoresDepartamentosCol)){
//										String codigosFuncionarios="";
										
										for(FuncionarioDTO funcionarioAut : autorizadoresDepartamentosCol){
											if(obtenerTipoMarca(detallePedidoDTO).equals(funcionarioAut.getTipoResponsable())
											&& detallePedidoDTO.getNpIdAutorizador().equals(funcionarioAut.getUsuarioFuncionario())){
												boolean contieneAutorizador=false;
												if(clasificacionDTO.getNpIdAutorizador()!=null){
													for(String autorizador:clasificacionDTO.getNpIdAutorizador().split(",")){
														if(funcionarioAut.getUsuarioFuncionario().equals(autorizador)){
															contieneAutorizador=true;
															break;
														}
													}	
												}
												if(!contieneAutorizador){
													if(funcionarioAut.getTipoResponsable()!=null && funcionarioAut.getTipoResponsable().equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
														clasificacionDTO.setNpIdAutorizador(funcionarioAut.getUsuarioFuncionario()+","+(clasificacionDTO.getNpIdAutorizador()!=null?clasificacionDTO.getNpIdAutorizador():""));
													}else{
														clasificacionDTO.setNpIdAutorizador((clasificacionDTO.getNpIdAutorizador()!=null?clasificacionDTO.getNpIdAutorizador():"")+funcionarioAut.getUsuarioFuncionario()+",");
													}
												}
											}
										}
								
										if(StringUtils.isEmpty(clasificacionDTO.getNpIdAutorizador())){
											activarDesactivarClasificacionesPorTipoMarca(obtenerTipoMarca(detallePedidoDTO),clasificacionDTO,false);
										}else{
											activarDesactivarClasificacionesPorTipoMarca(obtenerTipoMarca(detallePedidoDTO),clasificacionDTO,true);
										}
									}else{
										CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.clasificacion.autorizador","sinFuncionario", detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion()+" - "+(obtenerTipoMarca(detallePedidoDTO).equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())?MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"):MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.proveedor")), warnings);	
										activarDesactivarClasificacionesPorTipoMarca(obtenerTipoMarca(detallePedidoDTO),clasificacionDTO,false);
									}
								}else{
									CotizacionReservacionUtil.agregarActionMessageNoRepetido("warning.clasificacion.autorizador","sinFuncionario", detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion()+" - "+(obtenerTipoMarca(detallePedidoDTO).equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())?MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"):MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.proveedor")), warnings);
									activarDesactivarClasificacionesPorTipoMarca(obtenerTipoMarca(detallePedidoDTO),clasificacionDTO,false);
								}
								Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(COLA_AUTORIZACIONES);
								
								if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
									for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
										//se verifica la llave y el estado de la autorizacion
										if(autorizacionActual.getValorReferencial() != null && obtenerCodigoClasificacionPadreDeLlaveDescuento(autorizacionActual.getValorReferencial()).equals(clasificacionDTO.getId().getCodigoClasificacion())
												&& !autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
											if(autorizacionActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].substring(autorizacionActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].length()-1).equals(ConstantesGenerales.ESTADO_INACTIVO)){
												clasificacionDTO.setNpActivarValorDescMp(false);
											}else{
												clasificacionDTO.setNpActivarValorDescProv(false);
											}
										}
									}
								}

								break;
							}
						}
					}
					
					CollectionUtils.filter(clasificacionesDepartamentosCol, new Predicate() {
						
						@Override
						public boolean evaluate(Object object) {
							ClasificacionDTO clasificacion=	(ClasificacionDTO)object;
						if(clasificacion.getNpIdAutorizador()!=null){
							clasificacion.setNpIdAutorizador(clasificacion.getNpIdAutorizador().substring(0,clasificacion.getNpIdAutorizador().length()-1));
							return true;	
						}
							return false;
						}
					});
					
					clasificacionesDepartamentosCol = ColeccionesUtil.sort(clasificacionesDepartamentosCol, ColeccionesUtil.ORDEN_ASC, "id.codigoClasificacion");
					
					//se sube a sesion la coleccion de departamentos existentes en el pedido
					if(ordenarDepartamentos){
						ordenarCompradores(clasificacionesDepartamentosCol, request);
					}else{
						 session.setAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO,clasificacionesDepartamentosCol);
					}
					
					LogSISPE.getLog().info("existen {} departamentos en el pedido",clasificacionesDepartamentosCol.size());
				}
			}catch (Exception e) {
				LogSISPE.getLog().error("Error al verificar los departamentos del pedido {}",e);
				errors.add("errorVerificarDepartamentos ",new ActionMessage("errors.gerneral", "Error al verificar los departamentos de los art\u00EDculos"));
			}
		}
	}
	
	
	public static String obtenerTipoMarca(DetallePedidoDTO detallePedidoDTO) {
		if(detallePedidoDTO!=null && detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO() != null 
				&& MarcaArticuloDTO.isLoaded(detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getMarcaComercialArticulo())
				&& detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getMarcaComercialArticulo() != null){
			 return detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca();
		}
		return SICArticuloConstantes.getInstancia().MARCAPROVEEDOR.toString();
	}
	
	private static void activarDesactivarClasificacionesPorTipoMarca(
			String tipoMarca, ClasificacionDTO clasificacionDTO,boolean bandera) {
		if(tipoMarca!=null){
			if( tipoMarca.equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
				clasificacionDTO.setNpActivarDescMp(bandera);
				clasificacionDTO.setNpActivarValorDescMp(bandera);
			}else{
				clasificacionDTO.setNpActivarDescProv(bandera);
				clasificacionDTO.setNpActivarValorDescProv(bandera);
			}
		}
	}
	
	/**
	 * Busca en una coleccion de clasificacion un objeto con el id indicado
	 * @param clasificacionesCol
	 * @param codigoClasificacion
	 * @return ClasificacionDTO encontrado
	 * @throws Exception 
	 */
	public static ClasificacionDTO obtenerClasificacionDTOPorCodigo(Collection<ClasificacionDTO> claCol, String codigoClasificacion,
			String codigoAutorizador, HttpServletRequest request) throws Exception{
		
		LogSISPE.getLog().info("obteniendo la clasificacion del codigo "+codigoClasificacion +" y el autorizador "+codigoAutorizador);
		Collection<ClasificacionDTO> clasificacionesCol = claCol;
		
		//si la coleccion esta nula se apunta a todas las clasificaciones
		if(CollectionUtils.isEmpty(clasificacionesCol)){
			
			HttpSession session = request.getSession();
			verificarDepartamentosPedido(request, new ActionMessages(), new ActionMessages(), Boolean.FALSE);
			
			if(session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO) != null){
				clasificacionesCol = (Collection<ClasificacionDTO>) session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
			}
		}
		
		if(CollectionUtils.isNotEmpty(clasificacionesCol) && StringUtils.isNotEmpty(codigoClasificacion)){
			for(ClasificacionDTO clasificacionActual : clasificacionesCol){
				if(clasificacionActual.getNpIdAutorizador().split(",").length>1){
					if(clasificacionActual.getId().getCodigoClasificacion().equals(codigoClasificacion)
							&& (codigoAutorizador.equals(clasificacionActual.getNpIdAutorizador().split(",")[0])
									|| codigoAutorizador.equals(clasificacionActual.getNpIdAutorizador().split(",")[1]))){
						return clasificacionActual;
					}
				}else{
					if(clasificacionActual.getId().getCodigoClasificacion().equals(codigoClasificacion)
							&& codigoAutorizador.equals(clasificacionActual.getNpIdAutorizador())){
						return clasificacionActual;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param exitos
	 * @param errors
	 * @param warnings
	 * @throws Exception 
	 */
	public static void agregarAutorizacionAlPedido(HttpServletRequest request, CotizarReservarForm formulario, ActionMessages exitos,
			ActionMessages errors, ActionMessages warnings) throws Exception{

		HttpSession session = request.getSession();
		
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);		
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
				
		LogSISPE.getLog().info("acepta solicitar la autorizacion");
		ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		//elimina las autorizaciones de los compradores que no estan seleccionados				
		AutorizacionesUtil.eliminarAutorizacionesNoSeleccionadas(detallePedidoCol, request, formulario.getOpDescuentos());
		
		//se salvan las variables necesarias para procesar los descuentos
		String [] descSeleccionados = formulario.getOpDescuentos();
		Double [][] porcentajeVarDescuento = formulario.getPorcentajeVarDescuento();
		Collection<String> llaveDescuentoColSesion = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);	
		Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS); 
		
		//se eliminan todos los descuentos 
		CotizarReservarAction.eliminarDescuentos(formulario, request, errors, exitos, session, detallePedidoCol);									
		
		request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, descSeleccionados);
		request.getSession().setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuento);
		
		formulario.setPorcentajeVarDescuento(porcentajeVarDescuento);
		//Se arma la cadena que consta de la clave del descuento seleccionado, el titulo y el porcentaje de descuento solicitado
		Boolean encontroclave = Boolean.FALSE;	
		Collection<String> llaveDescuentoCol = new ArrayList<String>(); //llave de descuentos que no sean variables

		if (descSeleccionados != null){
			formulario.setOpDescuentos(descSeleccionados);
		}
		
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
		
		//se obtiene de session la coleccion de departamentos
		Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol = (Collection<ClasificacionDTO>) session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
		
		for(int i=0; i<formulario.getOpDescuentos().length; i++){
			
			if(StringUtils.isNotEmpty(formulario.getOpDescuentos()[i]) && formulario.getOpDescuentos()[i].contains(CODIGO_GERENTE_COMERCIAL)){
				StringBuffer tituloAut = new StringBuffer(); tituloAut.append("AUTORIZACION DESCUENTO VARIABLE ");
															
				//se busca la clasificacion del departamento
				String codigoClasificacionPadre = obtenerCodigoClasificacionPadreDeLlaveDescuento(formulario.getOpDescuentos()[i]);
				String codigoAutorizador = obtenerCodigoAutorizadorDeLlaveDescuento(formulario.getOpDescuentos()[i]);
				if(StringUtils.isNotEmpty(codigoAutorizador)){
					ClasificacionDTO clasificacionActualDTO = obtenerClasificacionDTOPorCodigo(clasificacionDepartamentosPedidoCol, codigoClasificacionPadre, codigoAutorizador, request);
					
					if(clasificacionActualDTO != null){
						tituloAut.append(clasificacionActualDTO.getDescripcionClasificacion().split(SEPARADOR_TOKEN)[0]);
					}
					
					String [] clave = formulario.getOpDescuentos()[i].split(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken"));			
					//[7, CTD3, CMDMON, COM0022, INX0]
					//Solo para el caso de los descuentos variables
					if(clave[1].length()>=4 && clave[1].substring(0,3).equals( MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento")) 							
							&&  clave[1].substring(3).equals(parametroDTO.getValorParametro()) ){
						
						Integer indice = new Integer(clave[4].substring(3,clave[4].length()-1));
						Integer indiceMp = new Integer(clave[4].substring(clave[4].length()-1));
						encontroclave = Boolean.TRUE;
						tituloAut.append(indiceMp==0?" ("+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia")+")":"");
						String valorReferencial = formulario.getOpDescuentos()[i].toString() + MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken") +
								tituloAut.toString() + MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken") +
								formulario.getPorcentajeVarDescuento()[indice.intValue()][indiceMp];
						tituloAut.append(" (" + formulario.getPorcentajeVarDescuento()[indice.intValue()][indiceMp]+"%)");
						
						//se crea la autorizacion
						AutorizacionesUtil.agregarAutorizacionDescVar(request,detallePedidoCol,valorReferencial, tituloAut, clasificacionActualDTO,
								formulario.getPorcentajeVarDescuento()[indice.intValue()][indiceMp], errors,warnings,exitos);
						
						//solo para cuando es un pedido consolidado
						if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
								&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
							
							//se obtiene la configuracion de las autorizaciones del pedido consolidado
							CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
						}
					}
					 
				}
				
			}else{
				llaveDescuentoCol.add(formulario.getOpDescuentos()[i]);						
			}
		}
		
		if(!encontroclave){
			errors.add("error",new ActionMessage("errors.agregar.autorizacion.tipo.descuento.variable"));
		}
		
		if(CollectionUtils.isNotEmpty(llaveDescuentoCol)){
			
			LogSISPE.getLog().info("va a aplicar el descuentos excepto variables");
			ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			CotizarReservarAction.aplicarDescuento(null, llaveDescuentoCol, detallePedido, formulario, request, errors, warnings, exitos);
		}
		//TODO verificar este bloque de codigo
		else{
			//6-CTD3-CMDMON-COM04-INX0  
			//se eliminan las llaves que no estan seleccionadas
			if(CollectionUtils.isNotEmpty(llaveDescuentoColSesion) && (descSeleccionados != null && descSeleccionados.length > 0)){
				//se recorren los descuentos seleccionados en el formulario
				Collection<String> nuevasllavesDscts = new ArrayList<String>();
				Collection<DescuentoEstadoPedidoDTO> nuevosDsctos = new ArrayList<DescuentoEstadoPedidoDTO>();
				for(int i=0; i<descSeleccionados.length; i++){
					
					String llave1 = descSeleccionados[i];
					if(llave1 != null && !llave1.equals("")){
						for(String llaveSesion : llaveDescuentoColSesion){
							//se elimina el secuencial y la posicion del vector de la llave
							String llaveSesionAux = llaveSesion.replace(llaveSesion.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+llaveSesion.split(SEPARADOR_TOKEN)[1], llaveSesion.split(SEPARADOR_TOKEN)[1]);
							if(llaveSesionAux.split(SEPARADOR_TOKEN).length >= 4){
								llaveSesionAux = llaveSesionAux.replace(SEPARADOR_TOKEN+llaveSesionAux.split(SEPARADOR_TOKEN)[3], "");
							}
							//se agregan las llaves y descuentos vinculados a esa llave
							if(llave1.contains(llaveSesionAux)){
								LogSISPE.getLog().info("llave agregada {}",llaveSesion);
								nuevasllavesDscts.add(llaveSesion);
								if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTO)){
									for(DescuentoEstadoPedidoDTO descuentoAct : colDescuentoEstadoPedidoDTO){
										if(descuentoAct.getLlaveDescuento() != null && !descuentoAct.getLlaveDescuento().equals("")){
											String llaveDscto = new String(descuentoAct.getLlaveDescuento());
											llaveDscto = llaveDscto.replace(llaveDscto.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+llaveDscto.split(SEPARADOR_TOKEN)[1], llaveDscto.split(SEPARADOR_TOKEN)[1]);
											if(llaveSesion.contains(llaveDscto)){
												nuevosDsctos.add(descuentoAct);
												LogSISPE.getLog().info("descuento agregado {}",descuentoAct.getLlaveDescuento());
											}
										}
									}
								}
							}
						}
					}
				}
				session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,nuevasllavesDscts); 
				session.setAttribute(CotizarReservarAction.COL_DESCUENTOS,nuevosDsctos);
				
				//se saca un respaldo del vectorCantidad para que en el metodo actualizarDetalleForm realice los procesos correctamente
				CotizacionReservacionUtil.respaldoIndicesCantidadesPesos(detallePedidoCol, formulario, request,accion, estadoActivo, estadoInactivo);
				
				formulario.actualizarDescuentos(request,warnings);
			}
		}
		
		session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, formulario.getOpDescuentos());
		session.removeAttribute(CotizarReservarAction.POPUP_DESCUENTO_VARIABLE);
		session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);	
		session.removeAttribute(CotizarReservarAction.POPUP_DESCUENTO);
	}
	

	/**
	 * 
	 * @param codigoClasificacion
	 * @param request
	 * @return coleccion de funcionariosDTO autorizadores de la clasificacion indicada
	 * @throws Exception 
	 */
	private static Collection<FuncionarioDTO>  obtenerFuncionarioAutorizadorPorClasificacion(String codigoClasificacion,String tipoMarca, Long codigoProceso, HttpServletRequest request, 
			Boolean esDepartamento) throws Exception{
		
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		//linea comercial clasificacion
	   LineaComercialClasificacionDTO linComcla = new LineaComercialClasificacionDTO();
	   linComcla.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	   if(esDepartamento){
		   linComcla.addCriteriaSearchParameter("codigoClasificacion", ComparatorTypeEnum.LIKE_START_COMPARATOR, codigoClasificacion.substring(2));
	   }else{
		   linComcla.setCodigoClasificacion(codigoClasificacion);
	   }
	   linComcla.setEstado(estadoActivo);
	   linComcla.setClasificacion(new ClasificacionDTO());
	   linComcla.addJoin("clasificacion", JoinType.INNER);
	   
	   //join con linea comercial
	   LineaComercialDTO lineaComercial = new LineaComercialDTO();
	   lineaComercial.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	   lineaComercial.setLineaComercialClasificaciones(new ArrayList<LineaComercialClasificacionDTO>());
	   lineaComercial.getLineaComercialClasificaciones().add(linComcla);
	   lineaComercial.setEstado(estadoActivo);
	   lineaComercial.setValorTipoLineaComercial(ec.com.smx.corpv2.common.util.CorporativoConstantes.TIPO_LINEA_COMERCIAL_COMERCIAL);
	   
	   //join con linea comercial funcionario
	   LineaComercialFuncionarioDTO linComfun = new LineaComercialFuncionarioDTO();
	   linComfun.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	   
	   //join con Funcionario
	   linComfun.setFuncionario(new FuncionarioDTO());
	   linComfun.addJoin("funcionario", JoinType.INNER);
	   linComfun.setLineaComercial(lineaComercial);
	   linComfun.setEstado(estadoActivo);
	   linComfun.getFuncionario().setPersonaDTO(new PersonaDTO());
	   linComfun.addJoin("funcionario.personaDTO", JoinType.INNER);
	   
	   linComfun.setLineaComercialFuncionarioProcesoCol(new ArrayList<LineaComercialFuncionarioProcesoDTO>());
	   
	   //linea comercial funcionario proceso
	   LineaComercialFuncionarioProcesoDTO linComFunPro = new LineaComercialFuncionarioProcesoDTO();
	   linComFunPro.setCodigoProceso(codigoProceso);
	   linComFunPro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	   linComFunPro.setLineaComercialFuncionarioDTO(linComfun);
	   linComFunPro.setEstado(estadoActivo);
	   	   
	   linComfun.getLineaComercialFuncionarioProcesoCol().add(linComFunPro);
	   
	   if(StringUtils.isNotEmpty(tipoMarca)){
//	   
		   linComfun.setLineaComercialFuncionarioTipoMarcaCol(new ArrayList<LineaComercialFuncionarioTipoMarcaDTO>());
		   LineaComercialFuncionarioTipoMarcaDTO linComFunTipMar = new LineaComercialFuncionarioTipoMarcaDTO();
		   linComFunTipMar.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		   FuncionarioTipoMarcaDTO funTipMar=new FuncionarioTipoMarcaDTO();
		   funTipMar.setValorTipoMarca(tipoMarca);
		   funTipMar.setEstado(estadoActivo);
		   linComFunTipMar.setFuncionarioTipoMarca(funTipMar);
		   linComFunTipMar.setEstado(estadoActivo);
		   
		   linComfun.getLineaComercialFuncionarioTipoMarcaCol().add(linComFunTipMar);
	   }
//	   
	   //se realiza la consulta
	   Collection<LineaComercialFuncionarioDTO> lineaComercialFuncionarioCol =  SISPEFactory.getDataService().findObjects(linComfun);
	   
	   //se obtienen los funcionarios autorizadores encotrados
	   Collection<FuncionarioDTO> funcionariosAutorizadores = new ArrayList<FuncionarioDTO>();
	   
	   if(CollectionUtils.isNotEmpty(lineaComercialFuncionarioCol)){
		   //se recorre los funcionarios-linea comercial
		   for(LineaComercialFuncionarioDTO linComFunActual : lineaComercialFuncionarioCol){
			   if(!funcionariosAutorizadores.contains(linComFunActual.getFuncionario())){
				   linComFunActual.getFuncionario().setTipoResponsable(linComFunActual.getLineaComercialFuncionarioTipoMarcaCol().iterator().next().getFuncionarioTipoMarca().getValorTipoMarca());
				   funcionariosAutorizadores.add(linComFunActual.getFuncionario());
			   }
		   }
	   }
	   return funcionariosAutorizadores;
	}
	
	
	
//	/**
//	 * Busca los autorizadores correspondientes a cada clasificacion(Departamento) del pedido para mostrarlo en el popUpDescuentos
//	 * @param request
//	 * @param warnings
//	 * @throws Exception 
//	 */
//	public static void buscarAutorizadoresDepartamentos(HttpServletRequest request, Collection<ClasificacionDTO> clasificacionesDepartamentosCol, ActionMessages warnings) throws Exception{
//		
//		StringBuffer clasificacionesSinAutorizador = new StringBuffer();
//		if(CollectionUtils.isNotEmpty(clasificacionesDepartamentosCol)){
//			
//			Collection<ClasificacionDTO> clasificacionesEliminar = new ArrayList<ClasificacionDTO>();
//		
//			String codigosFuncionarios = "";
//			Boolean mostrarMensaje = Boolean.FALSE;
//			
//			Collection<ClasificacionDTO> clasificacionesAgregar = new ArrayList<ClasificacionDTO>();
//			
//			//se recorren las clasificaciones actuales
//			for(ClasificacionDTO clasifDepartamento : clasificacionesDepartamentosCol){
//				
//				Collection<FuncionarioDTO> autorizadoresDepartamentosCol = obtenerFuncionarioAutorizadorPorClasificacion(clasifDepartamento.getId().getCodigoClasificacion(),
//						obtenerCodigoProcesoAutorizarDescVar(request), request, Boolean.TRUE); 
//				
//				//si encontro datos se asignan el idAutorizador y nombreAutorizador a la clasificacion
//				if(CollectionUtils.isNotEmpty(autorizadoresDepartamentosCol)){
//					
//					//contador de las posiciones de la coleccion
////					Integer cont = 0;
//					for(FuncionarioDTO funcionarioAut : autorizadoresDepartamentosCol){
//						
////						if(cont > 0){
////							// a partir del segundo registro por ser clasificacion clonada se asigna diferente
////							ClasificacionDTO clasificacionNueva = SerializationUtils.clone(clasifDepartamento);
////							clasificacionNueva.setDescripcionClasificacion(clasificacionNueva.getDescripcionClasificacion().replace(nombreFuncionario, ""));
////							nombreFuncionario = funcionarioAut.getPersonaDTO().getPrimerNombre()+" "+funcionarioAut.getPersonaDTO().getPrimerApellido(); 
////							
////							if(!clasificacionNueva.getDescripcionClasificacion().contains(nombreFuncionario)){
////								clasificacionNueva.setDescripcionClasificacion(clasificacionNueva.getDescripcionClasificacion()+nombreFuncionario);
////							}
////							clasificacionNueva.setNpIdAutorizador(funcionarioAut.getUsuarioFuncionario());
////							//validar que existan detalles para esa clasificacion para agregar o no 
////							if(verificarExistenArticulosParaClasificacion(request, clasificacionNueva)){
////								clasificacionesAgregar.add(clasificacionNueva);
////							}
////						}else{
////							//cuando es el primer registro
////							nombreFuncionario = funcionarioAut.getPersonaDTO().getPrimerNombre()+" "+funcionarioAut.getPersonaDTO().getPrimerApellido(); 
////							if(!clasifDepartamento.getDescripcionClasificacion().contains(nombreFuncionario)){
////								clasifDepartamento.setDescripcionClasificacion(clasifDepartamento.getDescripcionClasificacion()+" - "+nombreFuncionario);
////							}
//							clasifDepartamento.setNpIdAutorizador(funcionarioAut.getUsuarioFuncionario());
////							
////							if(!verificarExistenArticulosParaClasificacion(request, clasifDepartamento)){
////								clasificacionesEliminar.add(clasifDepartamento);
////							}
////						}
////						cont ++;
//						if(verificarExistenArticulosParaClasificacion(request, clasifDepartamento)){
////							clasificacionesAgregar.add(clasificacionNueva);
//							codigosFuncionarios +=funcionarioAut.getUsuarioFuncionario()+",";
//						}
//						
//					}
//					
//					if(StringUtils.isNotBlank(codigosFuncionarios)){
//						clasifDepartamento.setNpIdAutorizador(codigosFuncionarios.substring(0,codigosFuncionarios.length()-1));
//					}
//				}else{
//					//se genera el mensaje para mostrar y se elimina la clasificacion para que no se pinte en el popupDescuento
//					mostrarMensaje = Boolean.TRUE;
//					clasificacionesSinAutorizador.append(clasifDepartamento.getId().getCodigoClasificacion());
//					clasificacionesSinAutorizador.append(" - ");
//					clasificacionesSinAutorizador.append(clasifDepartamento.getDescripcionClasificacion());
//					clasificacionesSinAutorizador.append(", ");
//					clasificacionesEliminar.add(clasifDepartamento);
//				}
//			}
//			clasificacionesDepartamentosCol.addAll(clasificacionesAgregar);
//			
//			//para cuando no existen autorizadores se agrega el mensaje de alerta y se elimina la clasificacion
//			if(CollectionUtils.isNotEmpty(clasificacionesEliminar)){
//				clasificacionesDepartamentosCol.removeAll(clasificacionesEliminar);
//				if(mostrarMensaje){
//					warnings.add("sinFuncionario", new ActionMessage("warning.clasificacion.autorizador",clasificacionesSinAutorizador));
//				}
//			}
//		}
//	}
	
	
	/**
	 * obtiene de la linea comercial el funcionario autorizador de la clasificacion y tipo proceso indicado
	 * @param codigoClasificacionDetalle
	 * @param request
	 * @param codigoProceso
	 * @return 
	 * @throws Exception
	 */
	public static FuncionarioDTO obtenerFuncionarioAutorizadorPorClasificacion(String codigoClasificacionDetalle, String tipoMarca,
			HttpServletRequest request, Long codigoProceso, ActionMessages warnings) throws Exception{
		
		if(StringUtils.isNotEmpty(codigoClasificacionDetalle)){
			
			LogSISPE.getLog().info("obteniendo el funcionario autorizador para la clasificacion, {}",codigoClasificacionDetalle);
			
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			//linea comercial clasificacion
		   LineaComercialClasificacionDTO linComcla = new LineaComercialClasificacionDTO();
		   linComcla.setCodigoClasificacion(codigoClasificacionDetalle);
		   linComcla.setEstado(estadoActivo);
		
		   //join con linea comercial
		   LineaComercialDTO lineaComercial = new LineaComercialDTO();
		   lineaComercial.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		   lineaComercial.setLineaComercialClasificaciones(new ArrayList<LineaComercialClasificacionDTO>());
		   lineaComercial.getLineaComercialClasificaciones().add(linComcla);
		   lineaComercial.setEstado(estadoActivo);
		   lineaComercial.setValorTipoLineaComercial(ec.com.smx.corpv2.common.util.CorporativoConstantes.TIPO_LINEA_COMERCIAL_COMERCIAL);
		   
		   //join con linea comercial funcionario
		   LineaComercialFuncionarioDTO linComfun = new LineaComercialFuncionarioDTO();
		   linComfun.setFuncionario(new FuncionarioDTO());
//		   linComfun.getFuncionario().setPersonaDTO(new  PersonaDTO());
		   linComfun.setLineaComercial(lineaComercial);
		   linComfun.setEstado(estadoActivo);
		   
		   if(StringUtils.isNotEmpty(tipoMarca)){
			   linComfun.setLineaComercialFuncionarioTipoMarcaCol(new ArrayList<LineaComercialFuncionarioTipoMarcaDTO>());
			   
			   LineaComercialFuncionarioTipoMarcaDTO linComFunTipMar = new LineaComercialFuncionarioTipoMarcaDTO();
			   linComFunTipMar.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			   FuncionarioTipoMarcaDTO funTipMar=new FuncionarioTipoMarcaDTO();
			   funTipMar.setValorTipoMarca(tipoMarca);
			   funTipMar.setEstado(estadoActivo);
			   linComFunTipMar.setFuncionarioTipoMarca(funTipMar);
			   linComFunTipMar.setEstado(estadoActivo);
			   
			   linComfun.getLineaComercialFuncionarioTipoMarcaCol().add(linComFunTipMar);
		   }
		   
		   //linea comercial funcionario proceso
		   LineaComercialFuncionarioProcesoDTO linComFunPro = new LineaComercialFuncionarioProcesoDTO();
		   linComFunPro.setCodigoProceso(codigoProceso);
		   linComFunPro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		   linComFunPro.setLineaComercialFuncionarioDTO(linComfun);
		   linComFunPro.setEstado(estadoActivo);
		   
		   //se realiza la consulta
		   Collection<LineaComercialFuncionarioProcesoDTO> linComFunProCol = SISPEFactory.getDataService().findObjects(linComFunPro);
		   
		   if(CollectionUtils.isNotEmpty(linComFunProCol) ){
			   if(linComFunProCol.size() == 1){
				   return linComFunProCol.iterator().next().getLineaComercialFuncionarioDTO().getFuncionario();
			   } else{
				   //si hay mas de una linea comercial se verifica que exista en solo una linea comercial hija
				   Collection<LineaComercialFuncionarioProcesoDTO> linComFunHijaCol = new ArrayList<LineaComercialFuncionarioProcesoDTO>();
				   
				   //se recorren los funcionarios-proceso
				   for(LineaComercialFuncionarioProcesoDTO funcionarioProc : linComFunProCol ){
					   if(funcionarioProc.getLineaComercialFuncionarioDTO().getLineaComercial().getCodigoLineaComercialPadre() != null){
						   linComFunHijaCol.add(funcionarioProc);
					   }
				   }
				   //Si existe solo una hija, se  asigna al autorizador de la linea comecial hija
				   if(linComFunHijaCol.size() == 1){
					   return linComFunHijaCol.iterator().next().getLineaComercialFuncionarioDTO().getFuncionario();
				   }else{
					   LogSISPE.getLog().error(" Existen "+linComFunProCol.size() +" autorizadores para una misma clasificacion");
					   warnings.add("errorDatosLineaComercial", new ActionMessage("errors.gerneral", "Existen "+linComFunHijaCol.size()
							   +" autorizadores para la clasificaci\u00F3n "+codigoClasificacionDetalle));
				   }
			   }
		   }
		}
		return null;
	}
	
	
	/**
	 * cuando se eliminan detalles del pedido se verifica si toca eliminar las autorizaciones de stock asociadas a los detalles eliminados
	 * @param request
	 * @param detallePedidoDTOCol
	 */
	public static void eliminarAutorizacionDetallesEliminados(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoDTOCol){
		
		HttpSession session = request.getSession();
		
		Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES_STOCK);
		if(CollectionUtils.isNotEmpty(colaAutorizacionesStock) && CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			LogSISPE.getLog().info("verificando si toca eliminar autorizaciones de stock");
			
			Long codigoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK;
			Collection<EstadoPedidoAutorizacionDTO> autorizacionesEliminarCol = new ArrayList<EstadoPedidoAutorizacionDTO>();
			//se recorren los detalles del pedido
			for(EstadoPedidoAutorizacionDTO estadoPedAut : colaAutorizacionesStock){

				Boolean borrarAutorizacion = Boolean.TRUE;
				//se recorren los detalles para comparar con las autorizaciones existentes
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					
					//se verifica si el detalle tiene una autorizacion de stock
					if(verificarArticuloPorTipoAutorizacion(detallePedidoDTO, codigoAutorizacionStock)){
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							
							if(estadoPedAut.getCodigoAutorizacion() != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null
									&& estadoPedAut.getCodigoAutorizacion().longValue() ==
									autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().longValue()){
								borrarAutorizacion = Boolean.FALSE;
								break;
							}
						}
						if(!borrarAutorizacion){
							break;
						}
					}
				}
				if(borrarAutorizacion){
					LogSISPE.getLog().info("se eliminara la autorizacion de stock "+estadoPedAut.getCodigoAutorizacion());
					autorizacionesEliminarCol.add(estadoPedAut);
				}
			}
			if(autorizacionesEliminarCol.size() > 0){
				//se eliminan autorizaciones por no haber detalles pertenecientes a esas autorizaciones
				
				colaAutorizacionesStock.removeAll(autorizacionesEliminarCol);
				session.setAttribute(COLA_AUTORIZACIONES_STOCK, colaAutorizacionesStock);
			}
			//se sube a sesion la bandera de estado de autorizaciones de stock
			session.setAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK, verificarExistenAutorizacionesStockPendientes(request, detallePedidoDTOCol));
		}
	}
	
	
	/**
	 * verifica si existen autorizaciones de stock que aun no has sido gestionadas
	 * @param request
	 * @param detallePedidoDTOCol
	 * @return true si existen autorizaciones de stock pendientes por gestionar
	 */
	public static Boolean verificarExistenAutorizacionesStockPendientes(HttpServletRequest request, Collection<DetallePedidoDTO> detPedDTOCol){
		
		Boolean existeAutorizacionStockPendiente = Boolean.FALSE;
		int contAutorizacionesPendientes = 0;
		Collection<DetallePedidoDTO> detallePedidoDTOCol=detPedDTOCol;
		
		if(detallePedidoDTOCol == null){
			detallePedidoDTOCol = (Collection<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		}
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			try {
				aplicarEstadoAutorizacionADetalleReceta(request, detallePedidoDTOCol, obtenerClasificacionesAutorizacionGerenteComercial(request));
			} catch (Exception e) {
				LogSISPE.getLog().error("Error al aplicar el estado de la autorizacion del canasto a los detalles del canasto {}",e);
			}
			
			//coleccion con los codigos de autorizaciones de los detalles que debe forzarce su finalizacion
			Collection<String> autorizacionesHijasPorFinalizar = (Collection<String>) request.getSession().getAttribute(AUTORIZACIONES_FINALIZAR_WORKFLOW);
			if(CollectionUtils.isEmpty(autorizacionesHijasPorFinalizar)){
				autorizacionesHijasPorFinalizar = new ArrayList<String>();
			}
			
			Long codigoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK;
			Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
			Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
			Long codigoComponenteCantidadArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.cantidad.articulo"));//83
			
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
				
				Boolean eliminarAutorizacionStock = Boolean.FALSE;
				
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){

					Collection<DetalleEstadoPedidoAutorizacionDTO> autEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
					Long cantidadSolicitada = 0L;
					String codigoArticulo = null;
					//se recorren las autorizaciones para verificar el estado
					for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						
						//se valida el estado de la autorizacion
						if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoAutorizacionStock
								&& autorizacionActual.getEstadoAutorizacion() != null && (autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)
								|| autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE))){
							
							LogSISPE.getLog().info("existe autorizacion de stock en estado {}",autorizacionActual.getEstadoAutorizacion()+" del "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
							existeAutorizacionStockPendiente = Boolean.TRUE;
							contAutorizacionesPendientes ++;
							
							ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = autorizacionActual.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO();
							if(autorizacionDTO != null && ec.com.smx.autorizaciones.dto.AutorizacionDTO.isLoaded(autorizacionDTO.getAutorizacionesHijas()) &&
									CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionesHijas())){
								
								for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionHijaActual : autorizacionDTO.getAutorizacionesHijas()){
									
									if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getValoresComponente())){
										
										codigoArticulo = obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
										//se verifica el codigo de barras
										if(StringUtils.isNotEmpty(codigoArticulo) && codigoArticulo.equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())){
											
											cantidadSolicitada = Long.parseLong(obtenerValorComponentePorID(codigoComponenteCantidadArt, autorizacionHijaActual));
											
											if(cantidadSolicitada != 0 && detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() >= cantidadSolicitada){
												
												autEliminar.add(autorizacionActual);
												autorizacionesEliminarCol.add(autorizacionActual);
												eliminarAutorizacionStock = Boolean.TRUE;
												existeAutorizacionStockPendiente = Boolean.FALSE; 
												contAutorizacionesPendientes --;
												
												//se agrega el codigo de la autorizacion hija para eliminarla
												if(!autorizacionesHijasPorFinalizar.contains(autorizacionHijaActual.getId().getCodigoAutorizacion().toString())){
													autorizacionesHijasPorFinalizar.add(autorizacionHijaActual.getId().getCodigoAutorizacion().toString());
												}
											}
											break;
										}
									}
								}
							}
						}else if(!existeAutorizacionStockPendiente){
							existeAutorizacionStockPendiente = verificarSolicitarAutorizacionStockCanastoEspecial(request, detallePedidoDTO);
							
							if(existeAutorizacionStockPendiente){
								contAutorizacionesPendientes ++;
							}
						}
					}
					if(eliminarAutorizacionStock){
						
						eliminarAutorizacionStockColaAutorizaciones(request.getSession(), autorizacionesHijasPorFinalizar);
						//se desactiva para que no valide la autorizacion de stock
						detallePedidoDTO.setNpSinStockPavPolCan(false);
						//se quitan las autorizaciones
						detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autEliminar);
						if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
						}
					}
				}
			}
			//se sube a sesion las autorizaciones por finalizar
			if(CollectionUtils.isNotEmpty(autorizacionesHijasPorFinalizar)){
				request.getSession().setAttribute(AUTORIZACIONES_FINALIZAR_WORKFLOW, autorizacionesHijasPorFinalizar);
			}
		}
		if(contAutorizacionesPendientes > 0){
			LogSISPE.getLog().info("existen "+contAutorizacionesPendientes+ " autorizaciones de stock pendientes.");
			existeAutorizacionStockPendiente = true;
		}
		return existeAutorizacionStockPendiente;
	}
	
	
	/**
	 * Si la autorizacion a eliminar no tiene autorizaciones hijas se elimina de sesion de la cola de autorizaciones
	 * @param session
	 * @param autorizacionesEliminarCol
	 */
	public static void eliminarAutorizacionStockColaAutorizaciones(HttpSession session, Collection<String> autorizacionesHijasPorFinalizar){
		
		if(CollectionUtils.isNotEmpty(autorizacionesHijasPorFinalizar)){
			
			Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (Collection<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES_STOCK);
			Collection<EstadoPedidoAutorizacionDTO> autEliminarSesion = new ArrayList<EstadoPedidoAutorizacionDTO>();
			
			for(EstadoPedidoAutorizacionDTO autorizacionDTO : colaAutorizacionesStock){
				
				//se recorren las autorizaciones que seran eliminadas
				for(String autorizacionHijaEliminar : autorizacionesHijasPorFinalizar){
					
					if(autorizacionDTO.getAutorizacionDTO() != null && CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionDTO().getAutorizacionesHijas())){
						
						Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesEliminar = new ArrayList<ec.com.smx.autorizaciones.dto.AutorizacionDTO>();
						//se recorren las autorizaciones hijas
						for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autHija : autorizacionDTO.getAutorizacionDTO().getAutorizacionesHijas()){
							
							if(autHija.getId().getCodigoAutorizacion() != null && autHija.getId().getCodigoAutorizacion().longValue() == Long.parseLong(autorizacionHijaEliminar)){
								
								LogSISPE.getLog().info("eliminando la autorizacion hija "+autorizacionHijaEliminar);
								autorizacionesEliminar.add(autHija);
							}
						}
						autorizacionDTO.getAutorizacionDTO().getAutorizacionesHijas().removeAll(autorizacionesEliminar);
						if(CollectionUtils.isEmpty(autorizacionDTO.getAutorizacionDTO().getAutorizacionesHijas())){
							autEliminarSesion.add(autorizacionDTO);
						}
					}
				}
			}
			
			colaAutorizacionesStock.removeAll(autEliminarSesion);
		}
	}
	
	
	public static boolean verificarExisteAutorizacionStockPendientePorDetalle(EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO, HttpServletRequest request){
		
		try{
			boolean existeAutorizacionStockPendiente = false;
			
			HttpSession session = request.getSession();
			//se obtiene de sesion el detalle pedido
			Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			
			if(CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				Long codigoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				Long codigoComponenteCodigoArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.codigo.articulo")); // 81;
				Long codigoComponenteCantidadArt = Long.parseLong(MessagesAplicacionSISPE.getString("autorizacion.hija.codigo.componente.cantidad.articulo"));//83
				
				
				//se recorren los detalles del pedido
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
					if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						
						//se recorren las autorizaciones para verificar el estado
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							
							//se valida el estado de la autorizacion
							if( autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoAutorizacionStock
									&& autorizacionActual.getEstadoAutorizacion() != null 
									&& (autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)
											|| autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE))
									&& estadoPedidoAutorizacionDTO.getId().getCodigoEstadoPedidoAutorizacion() != null
											&& estadoPedidoAutorizacionDTO.getId().getCodigoEstadoPedidoAutorizacion().
									equals(autorizacionActual.getId().getCodigoEstadoPedidoAutorizacion())){
								
								LogSISPE.getLog().info("existe autorizacion de stock en estado {}",autorizacionActual.getEstadoAutorizacion());
								existeAutorizacionStockPendiente = Boolean.TRUE; 
								
								ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = autorizacionActual.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO();
								if(autorizacionDTO != null && ec.com.smx.autorizaciones.dto.AutorizacionDTO.isLoaded(autorizacionDTO.getAutorizacionesHijas()) &&
										CollectionUtils.isNotEmpty(autorizacionDTO.getAutorizacionesHijas())){
									
									Long cantidadSolicitada = 0L;
									String codigoArticulo = null;
									
									for(ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionHijaActual : autorizacionDTO.getAutorizacionesHijas()){
										if(CollectionUtils.isNotEmpty(autorizacionHijaActual.getValoresComponente())){
											codigoArticulo = obtenerValorComponentePorID(codigoComponenteCodigoArt, autorizacionHijaActual);
											//se verifica el codigo de barras
											if(StringUtils.isNotEmpty(codigoArticulo) && codigoArticulo.equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())){
												cantidadSolicitada = Long.parseLong(obtenerValorComponentePorID(codigoComponenteCantidadArt, autorizacionHijaActual));
												if(cantidadSolicitada != 0 && detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() >= cantidadSolicitada){
													autorizacionesEliminarCol.add(autorizacionActual);
													existeAutorizacionStockPendiente = Boolean.FALSE; 
												}
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		
			return existeAutorizacionStockPendiente;
			
		}catch(Exception ex){
			
			LogSISPE.getLog().error("error al verificarExisteAutorizacionStockPendientePorDetalle "+ex);
			return true;
		}
	}
	
	
	/**
	 * Obtiene el departamento al que pertenece el detallePedidoDTO
	 * @param detallePedidoDTO
	 * @return
	 */
	public static String obtenerCodigoDepartamento(DetallePedidoDTO detallePedidoDTO){
		
		String departamentoArticulo = "";
			
		if(detallePedidoDTO.getArticuloDTO().getClasificacionDTO() == null){
			departamentoArticulo = "00"+detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion().substring(0,2);
		}
		else{
			//se comparan las clasificacion del detalle con los departamentos
			departamentoArticulo =  detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre() != null ?
					detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre()
					: "00"+detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion().substring(0,2);
		}
		return departamentoArticulo;
	}
	
	
	/**
	 * Obtiene el codigoClasificacion del detallePedidoDTO
	 * @param detallePedidoDTO
	 * @return
	 */
	public static String obtenerCodigoClasificacion(DetallePedidoDTO detallePedidoDTO){
		
		String departamentoArticulo = "";
			
		if(detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null){
			departamentoArticulo = detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion();
		}
		else{
			//se comparan las clasificacion del detalle con los departamentos
			departamentoArticulo =  detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion();
		}
		return departamentoArticulo;
	}
	
	
	/**
	 * De la llave del descuento variable obtiene el codigoClasificacion
	 * @param llaveDescuento
	 * @return
	 */
	public static String obtenerCodigoClasificacionPadreDeLlaveDescuento(String llaveDescuento){
//		7-CTD3-CMDMON-COM0022:FRM1458-INX1
		return llaveDescuento.split(SEPARADOR_TOKEN)[3].split(CODIGO_GERENTE_COMERCIAL)[1].split(SEPARADOR_DOS_PUNTOS)[0];
	}
	
	
	/**
	 * De la llave del descuento variable obtiene el codigoAutorizador
	 * @param llaveDescuento
	 * @return
	 */
	public static String obtenerCodigoAutorizadorDeLlaveDescuento(String llaveDescuento){
//		7-CTD3-CMDMON-COM0022:FRM1458-INX1
		return llaveDescuento.split(SEPARADOR_TOKEN)[3].split(CODIGO_GERENTE_COMERCIAL)[1].split(SEPARADOR_DOS_PUNTOS)[1];
	}
	
	
	/**
	 * Obtiene de sesion o de la base de datos el codigo para autorizar descuento variable desde linea comercial
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Long obtenerCodigoProcesoAutorizarDescVar(HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		Long codigoAutorizarDescVar = (Long) session.getAttribute(CODIGO_PROCESO_AUTORIZAR_DESC_VARIABLE);
		
		// si la variable no esta en sesion se consulta y guarda en sesion el dato
		if(codigoAutorizarDescVar == null){
			
			//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
	  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.autorizar.descuentoVariable.stock", request);
	  		String[] codigoParametroAutorizarLineaComercial = parametroDTO.getValorParametro().split(SEPARADOR_COMA);
	  		codigoAutorizarDescVar = Long.parseLong(codigoParametroAutorizarLineaComercial[0]);
	  		session.setAttribute(CODIGO_PROCESO_AUTORIZAR_DESC_VARIABLE, codigoAutorizarDescVar);
	  		
	  		//se setea de una vez el proceso paraautorizar stock
	  		Long codigoAutorizarStock = Long.parseLong(codigoParametroAutorizarLineaComercial[1]);
	  		session.setAttribute(CODIGO_PROCESO_AUTORIZAR_STOCK, codigoAutorizarStock);
		}
		return codigoAutorizarDescVar;
	}
	
	/**
	 * Obtiene de sesion o de la base de datos el codigo para autorizar stock desde linea comercial
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Long obtenerCodigoProcesoAutorizarStock(HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		Long codigoAutorizarStock = (Long) session.getAttribute(CODIGO_PROCESO_AUTORIZAR_STOCK);
		
		// si la variable no esta en sesion se consulta y guarda en sesion el dato
		if(codigoAutorizarStock == null){
			
			//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
	  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.autorizar.descuentoVariable.stock", request);
	  		String[] codigoParametroAutorizarLineaComercial = parametroDTO.getValorParametro().split(SEPARADOR_COMA);
	  		codigoAutorizarStock = Long.parseLong(codigoParametroAutorizarLineaComercial[1]);
	  		session.setAttribute(CODIGO_PROCESO_AUTORIZAR_STOCK, codigoAutorizarStock);
	  		
	  		//se setea de una vez el proceso paraautorizar stock
	  		Long codigoAutorizarDescVar = Long.parseLong(codigoParametroAutorizarLineaComercial[0]);
	  		session.setAttribute(CODIGO_PROCESO_AUTORIZAR_DESC_VARIABLE, codigoAutorizarDescVar);
		}
		return codigoAutorizarStock;
	}
	

//	/**
//	 * 
//	 * @param request
//	 * @param clasificacionDTO
//	 * @return
//	 * @throws Exception
//	 */
//	private static Boolean verificarExistenArticulosParaClasificacion(HttpServletRequest request, ClasificacionDTO clasificacionDTO) throws Exception{
//		
//		HttpSession session = request.getSession();
//		//se obtiene de sesion el detalle pedido
//		Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//		
//		//si es pedido consolidado se suma el pedido consolidado para valide correctamente las cantidades
//		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
//				&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)
//				&& session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null){
//			//se obtiene de sesion el pedido consolidado
//			detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
//		}
//		
//		if(CollectionUtils.isNotEmpty(detallePedidoCol) && clasificacionDTO != null){
//			
//			//codigoClasificacion,idAutorizador
//			Map<String, String> mapClasificacionAutorizador = new HashMap<String, String>();
//			String key;
//			String idAutorizador = "";
//			
//			//se recorren los detalles
//			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
//				
//				if(StringUtils.isEmpty(detallePedidoDTO.getNpIdAutorizador())){
//					
//					key = obtenerCodigoClasificacion(detallePedidoDTO);
//					
//					if(!mapClasificacionAutorizador.containsKey(key)){
//						
//						//se consulta el funcionario autorizador de ese articulo
//						FuncionarioDTO funcionarioAutorizador = obtenerFuncionarioAutorizadorPorClasificacion(obtenerCodigoClasificacion(detallePedidoDTO),detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca(),
//								request, obtenerCodigoProcesoAutorizarDescVar(request), new ActionMessages());
//						
//						if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
//							idAutorizador = funcionarioAutorizador.getUsuarioFuncionario();
//							mapClasificacionAutorizador.put(key, idAutorizador);
//						}
//						
//					}else{
//						idAutorizador = mapClasificacionAutorizador.get(key);
//					}
//					
//					if(StringUtils.isNotEmpty(idAutorizador)){
//						detallePedidoDTO.setNpIdAutorizador(idAutorizador);
//					}
//				}
//				
//				if(StringUtils.isNotEmpty(detallePedidoDTO.getNpIdAutorizador()) && detallePedidoDTO.getNpIdAutorizador().equals(clasificacionDTO.getNpIdAutorizador())
//						&& clasificacionDTO.getId().getCodigoClasificacion().equals(obtenerCodigoDepartamento(detallePedidoDTO))){
//					return Boolean.TRUE; 
//				}
//			}
//		}
//		return Boolean.FALSE;
//	}
	
	
	/**
	 * agrega las autorizaciones sin repetir al pedido
	 * @param colaAutorizaciones
	 * @param pedidoDTO
	 */
	public static void agregarAutorizacionesNoRepetidasAlPedido(Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones, PedidoDTO pedidoDTO){
		
		try{
			if(pedidoDTO != null && CollectionUtils.isNotEmpty(colaAutorizaciones)){
				
				//si el pedido no tiene datos se asigna directamente
				if(CollectionUtils.isEmpty(pedidoDTO.getNpAutorizacionesEstadoDetallePedidoCol())){
					pedidoDTO.setNpAutorizacionesEstadoDetallePedidoCol(colaAutorizaciones);
				}else{
					
					//si existen datos se validan que no se agreguen autorizaciones repetidas
					for(EstadoPedidoAutorizacionDTO autorizacionAct : colaAutorizaciones){
						
						Boolean agregarAutorizacion = Boolean.TRUE;
						
						//se recorren las autorizaciones del pedido y se compara con la cola de autorizaciones
						for(EstadoPedidoAutorizacionDTO autorizacionPedido : pedidoDTO.getNpAutorizacionesEstadoDetallePedidoCol()){
							
							//se compara el codigo de la autorizacion
							if(autorizacionPedido.getNpAutorizacion().getCodigoAutorizacion() != null 
									&& autorizacionAct.getNpAutorizacion().getCodigoAutorizacion() != null
								&& autorizacionPedido.getNpAutorizacion().getCodigoAutorizacion().longValue() == 
								autorizacionAct.getNpAutorizacion().getCodigoAutorizacion().longValue()){
									agregarAutorizacion = Boolean.FALSE;
									break;
							}
						}
						if(agregarAutorizacion){
							pedidoDTO.getNpAutorizacionesEstadoDetallePedidoCol().add(autorizacionAct);
							LogSISPE.getLog().info("agregado al pedido la autorizacion con codigo: " +autorizacionAct.getNpAutorizacion().getCodigoAutorizacion());
						}
					}
				}
			}	
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al agregar autorizaciones no repetidas al pedido {}",e);
		}
	}
	
	/**
	 * Consulta el parametro 142 y obtiene los el codigo del proceso para enviar copia de autorizacion de acuerdo al tipo indicado 
	 * @param request
	 * @param codigoTipoAutorizacion
	 * @return el codigoprocesoEnvioCopiaAutorizacion
	 * @throws Exception
	 */
	public static Long obtenerCodigoProcesoEnviarCopiaAutorizacion(HttpServletRequest request, Long codigoTipoAutorizacion) throws Exception{
		
		HttpSession session = request.getSession();
		Long codigoProcesoEnvioCopiaAutorizacion = null;
		try{
			//para el caso de descuento variable
			if(codigoTipoAutorizacion == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
				codigoProcesoEnvioCopiaAutorizacion = (Long) session.getAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_DESC_VARIABLE);
				// si la variable no esta en sesion se consulta y guarda en sesion el dato
				if(codigoProcesoEnvioCopiaAutorizacion == null){
					
					//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
			  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.autorizador.recibira.copia.descuento.variable", request);
			  		String[] codigoParametroAutorizarLineaComercial = parametroDTO.getValorParametro().split(SEPARADOR_COMA);
			  		codigoProcesoEnvioCopiaAutorizacion = Long.parseLong(codigoParametroAutorizarLineaComercial[0].trim());
			  		session.setAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_DESC_VARIABLE, codigoProcesoEnvioCopiaAutorizacion);
			  		LogSISPE.getLog().info("codigo proceso de envio copia de autorizacion de descuento variable: "+codigoProcesoEnvioCopiaAutorizacion);
			  		
			  		//se setea de una vez el proceso para autorizar stock
			  		Long codigoAutorizarStock = Long.parseLong(codigoParametroAutorizarLineaComercial[1].trim());
			  		session.setAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_STOCK, codigoAutorizarStock);
				}
			} 
			//para el caso de autorizacion de stock
			else if(codigoTipoAutorizacion == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
				
				codigoProcesoEnvioCopiaAutorizacion = (Long) session.getAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_STOCK);
				// si la variable no esta en sesion se consulta y guarda en sesion el dato
				if(codigoProcesoEnvioCopiaAutorizacion == null){
					
					//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
			  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.autorizador.recibira.copia.descuento.variable", request);
			  		String[] codigoParametroAutorizarLineaComercial = parametroDTO.getValorParametro().split(SEPARADOR_COMA);
			  		codigoProcesoEnvioCopiaAutorizacion = Long.parseLong(codigoParametroAutorizarLineaComercial[1].trim());
			  		session.setAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_STOCK, codigoProcesoEnvioCopiaAutorizacion);
			  		LogSISPE.getLog().info("codigo proceso de envio copia de autorizacion de stock: "+codigoProcesoEnvioCopiaAutorizacion);
			  		
			  		//se setea de una vez el proceso para autorizar descuento var
			  		Long codigoAutorizarDescVar = Long.parseLong(codigoParametroAutorizarLineaComercial[0].trim());
			  		session.setAttribute(CODIGO_PROCESO_COPIA_AUTORIZAR_DESC_VARIABLE, codigoAutorizarDescVar);
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener  el codigo del proceso: enviar copia autorizador {}",e);
		}
		return codigoProcesoEnvioCopiaAutorizacion;
	}
	
	/**
	 * 
	 * @param request
	 * @param detallePedidoDTO
	 * @return
	 * @throws Exception
	 */
	public static Collection<String> obtenerIdFuncionarioRecibiraCopiaAutorizacion(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO,
			Long codigoTipoAutorizacion) throws Exception{

		//Coleccion de funcionarios autorizadores
		Collection<String> funcionariosAutorizadoresCopia = new ArrayList<String>();
		
		try{
			LogSISPE.getLog().info("buscando los funcionarios que recibiran copia de la clasificacion: "+detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion());
			//se obtiene el proceso de envio copia en autorizacion de descuento variable
			Long codigoProcesoEnvioCopia = obtenerCodigoProcesoEnviarCopiaAutorizacion(request, codigoTipoAutorizacion);
			
			if(codigoProcesoEnvioCopia != null){
			
				//se obtiene los funcionarios autorizadores de la clasificacion
				Collection<FuncionarioDTO> autorizadoresDepartamentosCol = obtenerFuncionarioAutorizadorPorClasificacion(detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getId().getCodigoClasificacion(),
						obtenerTipoMarca(detallePedidoDTO),codigoProcesoEnvioCopia, request, Boolean.FALSE);
				
				//se agregan los autorizadores
				if(CollectionUtils.isNotEmpty(autorizadoresDepartamentosCol)){
					for(FuncionarioDTO funcionarioActual : autorizadoresDepartamentosCol){
						funcionariosAutorizadoresCopia.add(funcionarioActual.getUsuarioFuncionario());
					}
				}
			}
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener los funcionarios que recibian copia de autorizacion de descuento variable {}",e);
		}
		return funcionariosAutorizadoresCopia;
	}
	
	/**
	 * Envia notificaciones PUSH a los dispositivos de los autorizadores
	 * @param request
	 */
	public static void enviarNotificacionAutorizadores(HttpServletRequest request){
		try{
			
			HttpSession session = request.getSession();
			//se obtiene de sesion las autorizaciones de descuento variable (Si existen autorizaciones de descuentos y stock, en esta coleccion vienen todas las autorizaciones)
			ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES);
			
			PedidoDTO pedidoDTO = (PedidoDTO) request.getSession().getAttribute(CotizarReservarAction.PEDIDO_GENERADO);
			String codigoPedido = pedidoDTO.getId().getCodigoPedido();
			FileInformationDto archivoPdf=null;
			//Cuando solo existen autorizaciones de stock
			if(CollectionUtils.isEmpty(colaAutorizaciones)){
				colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(COLA_AUTORIZACIONES_STOCK);
			}
			if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
				LogSISPE.getLog().info("ingresa a enviar notificaciones a los autorizadores");
				String textoNotificacion = null;
				for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
					if(autorizacionActual.getNpAutorizacion().getEnviarNotificacion() != null && autorizacionActual.getNpAutorizacion().getEnviarNotificacion()){
						
						if(autorizacionActual.getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
							textoNotificacion = "Solicitud de autorizaci&#243;n de: DESCUENTO VARIABLE. Caso: "+autorizacionActual.getNumeroProceso();
							
							StringBuilder datos = new StringBuilder();
							datos=generarXmlDetallePedidoAutorizaciones(request);
							
							if(StringUtils.isNotEmpty(datos)){
								String plantillaXSL = TransformerUtil.obtenerPlantillaHTML(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.autorizaciones.generarPdf"));
								Document docXML = TransformerUtil.stringToXML(datos.toString().replace("&", "&amp;"));
								Document docXSL = TransformerUtil.stringToXML(plantillaXSL);
								ImpresoraFo impresoraFo = new ImpresoraFoImpl();
								byte[] contenido= impresoraFo.getBytesDocumentoPdf(docXML, docXSL,new ConfiguracionPaginaFo(), new HashMap<String, Object>());
								
								archivoPdf=new FileInformationDto();
								archivoPdf.setName("DetallesPedido-"+codigoPedido+"-"+System.currentTimeMillis()+".pdf");
								archivoPdf.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
								archivoPdf.setDescription("DetallesPedido-"+codigoPedido+"-"+System.currentTimeMillis()+".pdf");
								archivoPdf.setContentType("application/pdf");
								archivoPdf.setFileSize(Long.valueOf(contenido.length));
								archivoPdf.getId().setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania()) ;
								archivoPdf.setStatus(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo.corporativo"));
								archivoPdf.setRegisterUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
								archivoPdf.setRegisterDate(new Timestamp(System.currentTimeMillis()));
								
								DataFileDto datosPdf=new DataFileDto();
								datosPdf.getId().setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
								datosPdf.setFileContent(contenido);
								archivoPdf.setDataFileCol(new ArrayList<DataFileDto>());
								archivoPdf.getDataFileCol().add(datosPdf);
								
							}
							
						}else if(autorizacionActual.getNpTipoAutorizacion().longValue() == ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
							textoNotificacion = "Solicitud de autorizaci&#243;n de: STOCK. Caso: "+autorizacionActual.getNumeroProceso();
						}
						try{
							ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO = ec.com.smx.autorizaciones.util.AutorizacionesUtil.transformarAutorizacion(autorizacionActual.getNpAutorizacion());
							autorizacionActual.setAutorizacionDTO(autorizacionDTO);
							actualizarDataKeyEnPedidoNuevo(request, autorizacionActual,codigoPedido,archivoPdf);
							AutorizacionesFactory.getInstancia().getaIAutorizacionesServicio().transNotificarDispositivosAutorizadorPush(textoNotificacion, autorizacionDTO);							
							archivoPdf=null;
							LogSISPE.getLog().info("notificacion enviada correctamente por la autorizacion "+autorizacionActual.getCodigoAutorizacion()+":"+autorizacionActual.getNumeroProceso());
//							autorizacionActual.getNpAutorizacion().setEnviarNotificacion(null);
						}catch (Exception e) {
							LogSISPE.getLog().error(" error al enviar la notificacion a los dispositivos de los autorizadores. {}",e);
						}
					}
				}
			}
		}catch (Exception e) {
			LogSISPE.getLog().error("error al enviar notificacion a los dispositivos de los autorizadores. {}",e);
		}
	}

	
	/**
	 * Si se esta guardando un pedido nuevo con autorizaciones, se cambia el valor de los datakeys guardados con el numero del pedido
	 * @param request
	 * @param estadoPedidoAutorizacionDTO
	 */
	private static void actualizarDataKeyEnPedidoNuevo(HttpServletRequest request, EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO,String codigoPedido,FileInformationDto archivoPdf){
		try{
			//se verifica si es un pedidoNuevo
			if(CollectionUtils.isNotEmpty(estadoPedidoAutorizacionDTO.getAutorizacionDTO().getDataKeys())){
				
				if(StringUtils.isNotEmpty(codigoPedido)){
					
					//se consultan los IDs de los workitems relacionados al pedido nuevo
					WorkItemDTO workItemConsulta = new WorkItemDTO();
					workItemConsulta.getId().setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					workItemConsulta.setProcessCode(estadoPedidoAutorizacionDTO.getNumeroProceso());
					workItemConsulta.setCriteriaSearch(new CriteriaSearch());
					workItemConsulta.getCriteriaSearch().addDistinctSearchParameter("id.workItemId");
					Collection<String> codigosWorkItems  = SISPEFactory.getDataService().findFieldValue(workItemConsulta,  "id.workItemId", true);
					
					if(CollectionUtils.isNotEmpty(codigosWorkItems)){
						//se consultan los datakeys guardados
						DataKeyValueDTO dataKeyConsulta = new DataKeyValueDTO();
						dataKeyConsulta.getId().setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						dataKeyConsulta.addCriteriaSearchParameter("id.workItemId", ComparatorTypeEnum.IN_COMPARATOR, codigosWorkItems);
						
						Collection<DataKeyValueDTO> dataKeysActualizarDTOCol = SISPEFactory.getDataService().findObjects(dataKeyConsulta);
						Collection<DataKeyValueDTO> dataKeysArchivoCol = new ArrayList<DataKeyValueDTO>();
						
						if(CollectionUtils.isNotEmpty(dataKeysActualizarDTOCol)){
							for(DataKeyValueDTO dataKeyActualDTO : dataKeysActualizarDTOCol){
								if(dataKeyActualDTO.getValue().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.pedido.nuevo"))){
									dataKeyActualDTO.setValue(codigoPedido);
									try{
										SISPEFactory.getDataService().transUpdate(dataKeyActualDTO);
										LogSISPE.getLog().info("actualizado el datakey con workiteId "+dataKeyActualDTO.getId().getWorkItemId()+" correctamente.");
									}catch (Exception e) {
										LogSISPE.getLog().error("Error al actualizar el datakey con workiteId "+dataKeyActualDTO.getId().getWorkItemId()+". "+e);
									}
								}else
								
								if(estadoPedidoAutorizacionDTO.getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()
								&& dataKeyActualDTO.getValue().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.pdf.detalle.pedido"))){
									dataKeyActualDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
									dataKeysArchivoCol.add(dataKeyActualDTO);
								}
							}
							if(CollectionUtils.isNotEmpty(dataKeysArchivoCol)){
								WorkflowFactory.getWorkflowAdministratorService().transCreateDataKeyValueFileInformation(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania(),dataKeysArchivoCol, archivoPdf);
							}
							
						}
					}
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al actualizar el dataKey con el numero del pedido: {}",e);
		}
	}
	
	
	/**
	 * Obtiene las clasificaciones que requieren autorizacion de stock
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static Collection<String> obtenerClasificacionesAutorizacionGerenteComercial(HttpServletRequest request) throws Exception{
		
		//se obtiene de sesion la coleccion de clasificaciones
		Collection<String> clasificacionesAutorizacionGerenteComercial = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_NECESITAN_AUTORIZACION_STOCK);
		
		if(CollectionUtils.isEmpty(clasificacionesAutorizacionGerenteComercial)){
			
			//se consultan las clasificaciones
			clasificacionesAutorizacionGerenteComercial = new ArrayList<String>();
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialExistenciaObligatoria", request);
			
			if(parametroDTO.getValorParametro()!=null){
				//se obtiene los c\u00F3digos de clasificaciones
				EspecialClasificacionDTO consultaEspecialClasificacionDTO = new EspecialClasificacionDTO();
				consultaEspecialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaEspecialClasificacionDTO.getId().setCodigoEspecial(parametroDTO.getValorParametro());
				consultaEspecialClasificacionDTO.setEstadoEspecialClasificacion(SessionManagerSISPE.getEstadoActivo(request));
				consultaEspecialClasificacionDTO.setCriteriaSearch(new CriteriaSearch());
				consultaEspecialClasificacionDTO.getCriteriaSearch().addDistinctSearchParameter("id.codigoClasificacion");
				clasificacionesAutorizacionGerenteComercial = SISPEFactory.getDataService().findFieldValue(consultaEspecialClasificacionDTO,  "id.codigoClasificacion", true);
				
				//se sube a sesion la coleccion de clasificaciones
				request.getSession().setAttribute(CLASIFICACIONES_NECESITAN_AUTORIZACION_STOCK, clasificacionesAutorizacionGerenteComercial);
			}
		}
		return clasificacionesAutorizacionGerenteComercial;
	}
	
	
	/**
	 * Se setea el estadoAutorizacion al canastoEspecial dependiendo del estado de las autorizaciones de stock de la receta del canasto 
	 * @param request
	 * @param detallePedidoCol
	 * @param clasificacionesAutorizacionGerenteComercial
	 * @param estadoAutorizacionPadre
	 * @param codigoAutorizacion
	 * @param mapContAutorizaciones
	 */
	private static void aplicarEstadoAutorizacionStockPadre(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol, Collection<String> clasificacionesAutorizacionGerenteComercial, 
			String estadoAutorizacionPadre, Long codigoAutorizacion, Map<String, Integer> mapContAutorizaciones){
		try{
			
			if(CollectionUtils.isNotEmpty(detallePedidoCol)){

				long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
				for(DetallePedidoDTO detalleActual : detallePedidoCol){
					
					//si el detalle tiene autorizaciones de stock
					if(verificarArticuloPorTipoAutorizacion(detalleActual, codigoTipoAutorizacionStock)){
						
						//se verifica si el detalle es un canasto especial
						if((obtenerCodigoClasificacion(detalleActual).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
								|| detalleActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO))){
						
							//se recorren los detalles del canasto para verificar los detalles que no tiene stock
							if(CollectionUtils.isNotEmpty(detalleActual.getArticuloDTO().getArticuloRelacionCol())
									&& detalleActual.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
							
								Boolean existeAutorizacionPendiente = Boolean.FALSE;
								Boolean existeAutorizacionAprobada = Boolean.FALSE;
								Boolean existeAutorizacionRechazada = Boolean.FALSE;
								
								LogSISPE.getLog().info("el "+detalleActual.getArticuloDTO().getDescripcionArticulo()+" tiene detalles con autorizacion de stock ");
								//se recorren los detalles del canasto especial
								for(ArticuloRelacionDTO articuloRelacionDTO : detalleActual.getArticuloDTO().getArticuloRelacionCol()){
						
									if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
											&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
											&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null
											&& articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() != null){
										
										//se verifica el estado de la autorizacion hija
										if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA)){
											existeAutorizacionRechazada = Boolean.TRUE;
											LogSISPE.getLog().info(articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" en estado "+articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion());
											
										}else if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
											existeAutorizacionAprobada = Boolean.TRUE;
											LogSISPE.getLog().info(articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" en estado "+articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion());
											
										}else if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){
											existeAutorizacionPendiente = Boolean.TRUE;
											LogSISPE.getLog().info(articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" en estado "+articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion());
										}
									}
								}
								String estadoAutorizacion = "";
								int contadorEstados = 0;
								if(existeAutorizacionRechazada){
									contadorEstados ++;
									estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_RECHAZADA;
								} 
								if(existeAutorizacionPendiente){
									contadorEstados ++;
									estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_SOLICITADA;
								}
								if(existeAutorizacionAprobada){
									contadorEstados ++;
									estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_APROBADA;
								}
								
								if(contadorEstados > 1){
									if(existeAutorizacionPendiente){
										estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_SOLICITADA;
									}else{
										estadoAutorizacion = ConstantesGenerales.ESTADO_AUT_GESTIONADA; 
										detalleActual.setNpSinStockPavPolCan(false);
									}
								}
								if(contadorEstados >= 1){
									
									for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
										
										//se compara el tipo de autorizacion
										if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock
												&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null
												&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().equals(codigoAutorizacion)){
											
											//si el estado es diferente es la primera vez que se aplica el estado
											if(!autorizacionActual.getEstadoAutorizacion().equals(estadoAutorizacionPadre)){
												
												//se cambia el estado de la autorizacion
												autorizacionActual.getEstadoPedidoAutorizacionDTO().setEstado(estadoAutorizacionPadre);
												autorizacionActual.setEstadoAutorizacion(estadoAutorizacion);
												
												if(CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
													
													for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
														
														//se recorren los detalles del canasto especial
														for(ArticuloRelacionDTO articuloRelacionDTO : detalleActual.getArticuloDTO().getArticuloRelacionCol()){
															
															//se compara el detalle del canasto con la autorizacion de stock
															if(autorizacionStockDTO.getCodigoArticuloRelacionado() != null && autorizacionStockDTO.getCodigoArticuloRelacionado().equals(articuloRelacionDTO.getId().getCodigoArticuloRelacionado())
																	&& articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() != null){
																
																autorizacionStockDTO.setEstadoAutorizacion(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion());
																LogSISPE.getLog().info("estado de la autorizacion de stock {}",autorizacionStockDTO.getEstadoAutorizacion());
																
																if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
																	
																	//se lleva la cuenta de la cantidad total autorizada
																	autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
																			? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
																			: autorizacionStockDTO.getCantidadParcialSolicitarStock());
																	autorizacionStockDTO.setCantidadUtilizada(autorizacionStockDTO.getCantidadTotalAutorizadaStock());
																	LogSISPE.getLog().info("solicitado "+autorizacionStockDTO.getCantidadParcialSolicitarStock()+" autorizado "+autorizacionStockDTO.getCantidadTotalAutorizadaStock());
																	break;
																}
															}
														}
													}
												}
												
												//se cuentan las autorizaciones
												String key = obtenerEstadoAutorizacionSispeAComponente(estadoAutorizacion);
												if(key.endsWith("ADA")){
													key = key.replace("ADA", "ADO");
												}
												int contador = 0;
												if(mapContAutorizaciones.containsKey(key)){
													contador = mapContAutorizaciones.get(key);
												}
												contador++;
												mapContAutorizaciones.put(key, contador);
											}
										}
									}
									
									//si el pedido tiene autorizacionesAnteriores se recorren las lista de autorizacionesAnteriores
									if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol())){
										
										//se recorren las autorizaciones para que conserve el estado aplicado
										for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()){
										
											//se compara el tipo de autorizacion
											if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock
													&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion() != null 
													&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion().equals(codigoAutorizacion)){
												
												//si el estado es diferente es la primera vez que se aplica el estado
												if(!autorizacionActual.getEstadoAutorizacion().equals(estadoAutorizacionPadre)){
													autorizacionActual.setEstadoAutorizacion(estadoAutorizacionPadre);
													
													//si la autorizacion fue aprobada se sumariza las autorizaciones aprobadas de ese detalle
													if(autorizacionActual.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) 
															&& CollectionUtils.isNotEmpty(autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
														
														for(DetalleEstadoPedidoAutorizacionStockDTO autorizacionStockDTO : autorizacionActual.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
															
															//se lleva la cuenta de la cantidad total autorizada
															autorizacionStockDTO.setCantidadTotalAutorizadaStock(autorizacionStockDTO.getCantidadTotalAutorizadaStock() != null 
																	? autorizacionStockDTO.getCantidadTotalAutorizadaStock() + autorizacionStockDTO.getCantidadParcialSolicitarStock() 
																	: autorizacionStockDTO.getCantidadParcialSolicitarStock());
														}
													}
												}
											}
										}
									}
									LogSISPE.getLog().info("al "+detalleActual.getArticuloDTO().getDescripcionArticulo()+ " se  le asigna el estado "+estadoAutorizacion);
								}
							}
						}
					}
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al aplicar el estado de las autorizacion stock al detalle padre despues de procesar desde el componente de autorizaciones {}",e);
		}
	}
	
	
	/**
	 * Si existen varios articulos repetidos se calcula el total sumarizado
	 * @param detallesPedidoDTOCol
	 * @param codigoBarrasArticuloActual
	 * @return cantidad de todos los detalles del mismo tipo
	 */
	private static Long sumarCantidadDetallesRepetidos(HttpServletRequest request, Collection<DetallePedidoDTO> detallesPedidoDTOCol, String codigoBarrasArticuloActual){
		Long cantidadSumarizada = 0L;
		Long cantidadParcial = 0L;
		Long cantidadFavor = 0L;
		
		if(CollectionUtils.isNotEmpty(detallesPedidoDTOCol)){
			//codigoBarras, cantidadStockFAvor
			Map<String, Integer> mapArticuloStock = obtenerAutorizacionStockAFavor(request);
			
			//se recorren los detalles del pedido
			for(DetallePedidoDTO detallePedidoDTO : detallesPedidoDTOCol){
				
				//se comparan si es el articulo actual
				if(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(codigoBarrasArticuloActual)){
//					cantidadSumarizada += detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
					cantidadParcial = obtenerCantidadSolicitarStock(request, detallePedidoDTO).longValue();
					cantidadFavor = cantidadParcial < 0 ? cantidadParcial : 0L;
					
					if(mapArticuloStock.containsKey(codigoBarrasArticuloActual)){
						int cantidadParcialFavor = mapArticuloStock.get(codigoBarrasArticuloActual);
						if(cantidadParcialFavor >= cantidadParcial){
							cantidadFavor -= cantidadParcial;
							mapArticuloStock.put(codigoBarrasArticuloActual, (cantidadParcialFavor - cantidadParcial.intValue()));
						}else{
							cantidadFavor -= cantidadParcialFavor;
							mapArticuloStock.put(codigoBarrasArticuloActual, 0);
						}
					}
					cantidadParcial = cantidadParcial > 0 ? cantidadParcial : 0L; 
					cantidadSumarizada += cantidadParcial + cantidadFavor;
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadTotalReservarSic(0L);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadParcialReservarSic(cantidadParcial);
				}
			}
			request.getSession().setAttribute("ec.com.smx.sic.sispe.stock.favor", mapArticuloStock);
		}
		
		return cantidadSumarizada > 0 ? cantidadSumarizada : 0L;
	}
	
	
	/**
	 * Obtiene los detalles del pedido, y compara con los detalles que solicitaran autorizacion de stock, se crea una lista con los
	 * detalles que tengan autorizaciones y que no sean los que solicitaran autorizacion
	 * @param request
	 * @param detallesPedidoDTOCol
	 * @return
	 */
	public static Map<String, Integer> obtenerAutorizacionStockAFavor(HttpServletRequest request){
		
		//codigoBarras, cantidadStockFAvor
		Map<String, Integer> mapArticuloStock = new HashMap<String, Integer>();
		
		//se obtiene de sesion el detalle del pedido
		Collection<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		if(CollectionUtils.isNotEmpty(detallePedidoCol)){
			
			long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			String key = "";
			int cantidadFavor;
			
			//se recorren los detalles de todo el pedido
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
				
				//se verifica que tenga autorizaciones aprobadas
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
					LogSISPE.getLog().info("detalle actual: "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
					
					int cantidadSolicitarStock = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue()
							- (detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null ? detallePedidoDTO.getArticuloDTO().getNpStockArticulo().intValue() : 0);
					
					for(DetalleEstadoPedidoAutorizacionDTO detEstPedAutorizacionDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						
						if(detEstPedAutorizacionDTO != null 
								&& detEstPedAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock
								&& (detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
									|| detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))){
							
							//para el caso de detalles canastos especiales
							if(obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
								
								//se recorren los detalles del canasto
								for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){

									//el detalle tiene las autorizaciones de stock de todo el canasto, por eso toca recorrer la coleccion y buscar el que corresponda
									for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutorizacionDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
										
										key =  articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().getCodigoBarras();
										//se compara los datos
										if(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania() == stockDTO.getCodigoCompania()
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo() == stockDTO.getCodigoAreaTrabajo() 
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido().equals(stockDTO.getSecuencialEstadoPedido())
												&& articuloRelacionDTO.getArticuloRelacion().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticuloRelacionado())){
											
											if(stockDTO.getCantidadTotalAutorizadaStock() > 0 && stockDTO.getCantidadTotalAutorizadaStock() > cantidadSolicitarStock){
												LogSISPE.getLog().info("al articulo "+key+" le autorizaron "+stockDTO.getCantidadTotalAutorizadaStock()+" y necesita "+cantidadSolicitarStock);
												cantidadFavor = stockDTO.getCantidadTotalAutorizadaStock() - cantidadSolicitarStock;
												if(mapArticuloStock.containsKey(key)){
													cantidadFavor += mapArticuloStock.get(key);
												}
												mapArticuloStock.put(key, cantidadFavor);
											}
										}
									}
								}
							}else{
								
								//para detalles normales
								for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutorizacionDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
									
									key =  detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras();
									//se compara los datos
									if(detEstPedAutorizacionDTO.getId().getCodigoCompania() == stockDTO.getCodigoCompania()
											&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo() == stockDTO.getCodigoAreaTrabajo() 
											&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
											&& detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())){
										
										if(stockDTO.getCantidadTotalAutorizadaStock() > 0 && stockDTO.getCantidadTotalAutorizadaStock() > cantidadSolicitarStock){
											LogSISPE.getLog().info("al articulo "+key+" le autorizaron "+stockDTO.getCantidadTotalAutorizadaStock()+" y necesita "+cantidadSolicitarStock);
											cantidadFavor = stockDTO.getCantidadTotalAutorizadaStock() - cantidadSolicitarStock;
											if(mapArticuloStock.containsKey(key)){
												cantidadFavor += mapArticuloStock.get(key);
											}
											mapArticuloStock.put(key, cantidadFavor);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return mapArticuloStock;
	}
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static DetalleEstadoPedidoAutorizacionDTO descontarAutorizacionStockAFavor(HttpServletRequest request){
		
		//codigoBarras, cantidadStockFAvor
		Map<String, Integer> mapArticuloStock = (Map<String, Integer>) request.getSession().getAttribute("ec.com.smx.sic.sispe.stock.favor");
		
		//se obtiene de sesion el detalle del pedido
		Collection<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		if(CollectionUtils.isNotEmpty(detallePedidoCol) && !mapArticuloStock.isEmpty()){
			
			long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			String key = "";
			int cantidadFavor;
			
			//se recorren los detalles de todo el pedido
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
				
				//se verifica que tenga autorizaciones aprobadas
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
					
					LogSISPE.getLog().info("detalle actual: "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
					
					int cantidadSolicitarStock = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue()
							- (detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null ? detallePedidoDTO.getArticuloDTO().getNpStockArticulo().intValue() : 0);
					
					for(DetalleEstadoPedidoAutorizacionDTO detEstPedAutorizacionDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
						
						if(detEstPedAutorizacionDTO != null 
								&& detEstPedAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock
								&& (detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
									|| detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))){
							
							//para el caso de detalles canastos especiales
							if(obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
								
								//se recorren los detalles del canasto
								for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){

									//el detalle tiene las autorizaciones de stock de todo el canasto, por eso toca recorrer la coleccion y buscar el que corresponda
									for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutorizacionDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
										
										key =  articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().getCodigoBarras();
										//se compara los datos
										if(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania() == stockDTO.getCodigoCompania()
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo() == stockDTO.getCodigoAreaTrabajo() 
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())
												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getSecuencialEstadoPedido().equals(stockDTO.getSecuencialEstadoPedido())
												&& articuloRelacionDTO.getArticuloRelacion().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticuloRelacionado())){
											
											if(stockDTO.getCantidadTotalAutorizadaStock() > 0 && stockDTO.getCantidadTotalAutorizadaStock() > cantidadSolicitarStock){
												
												//se verifica si el stock a favor de este articulo fue utilizado por otro detalle
												cantidadFavor = stockDTO.getCantidadTotalAutorizadaStock() - cantidadSolicitarStock;
												if(mapArticuloStock.containsKey(key) && cantidadFavor > mapArticuloStock.get(key)){
													int cantidadUsada = cantidadFavor - mapArticuloStock.get(key);
													LogSISPE.getLog().info("se usaron el stock a favor por: "+cantidadUsada+ " del articulo "+key);
//													stockDTO.setCantidadTotalAutorizadaStock(stockDTO.getCantidadTotalAutorizadaStock() - cantidadUsada);
													stockDTO.setCantidadUtilizada(stockDTO.getCantidadUtilizada() - cantidadUsada);
													return detEstPedAutorizacionDTO;
												}
											}
										}
									}
								}
							}else{
								
								//para detalles normales
								for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutorizacionDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
									
									key =  detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras();
									
									//se compara los datos
									if(detEstPedAutorizacionDTO.getId().getCodigoCompania() == stockDTO.getCodigoCompania()
											&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo() == stockDTO.getCodigoAreaTrabajo() 
											&& detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
											&& detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())){
										
										if(stockDTO.getCantidadTotalAutorizadaStock() > 0 && stockDTO.getCantidadTotalAutorizadaStock() > cantidadSolicitarStock){
											//se verifica si el stock a favor de este articulo fue utilizado por otro detalle
											cantidadFavor = stockDTO.getCantidadTotalAutorizadaStock() - cantidadSolicitarStock;
											if(mapArticuloStock.containsKey(key) && cantidadFavor > mapArticuloStock.get(key)){
												int cantidadUsada = cantidadFavor - mapArticuloStock.get(key);
												LogSISPE.getLog().info("se usaron el stock a favor por: "+cantidadUsada+ " del articulo "+key);
//												stockDTO.setCantidadTotalAutorizadaStock(stockDTO.getCantidadTotalAutorizadaStock() - cantidadUsada);
												stockDTO.setCantidadUtilizada(stockDTO.getCantidadUtilizada() - cantidadUsada);
												return detEstPedAutorizacionDTO;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	
	
	/**
	 * Aplica el estado de la autorizacion que tiene el CANASTO  a los detalles de la receta
	 * @param request
	 * @param detallePedidoCol
	 * @param clasificacionesAutorizacionGerenteComercial
	 */
	private static void aplicarEstadoAutorizacionADetalleReceta(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol, Collection<String> clasificacionesAutorizacionGerenteComercial){
		
		if(CollectionUtils.isNotEmpty(detallePedidoCol)){

			long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
				
				//si el detalle tiene autorizaciones de stock
				if(verificarArticuloPorTipoAutorizacion(detallePedidoDTO, codigoTipoAutorizacionStock)){
					
					//se verifica si el detalle es un canasto especial
					if((obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
							|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO))){
						
						
						aplicarEstadoAutorizacionStockCanastoEspecial(detallePedidoDTO);
						
//						String estadoAutorizacionPadre = "";
//						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
//							if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock){
//								estadoAutorizacionPadre = autorizacionActual.getEstadoAutorizacion();
//								break;
//							}
//						}
//					
//						//se recorren los detalles del canasto para verificar los detalles que no tiene stock
//						if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())
//								&& detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
//						
//							LogSISPE.getLog().info("el "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" tiene detalles con autorizacion de stock ");
//							//se recorren los detalles del canasto especial
//							for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
//					
//								if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
//										&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
//										&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
//									
//									if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0 
//											&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo()
//											&& (articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() == null || articuloRelacionDTO.getArticuloRelacion().getNpStockArticuloAutorizado() > 0)){
//										//se verifica el estado de la autorizacion hija
//										if(StringUtils.isEmpty(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion())){
//											articuloRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(estadoAutorizacionPadre);
//											LogSISPE.getLog().info("al articulo "+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" se le asigno el estado "+estadoAutorizacionPadre);
//										}
//									}
//								}
//							}
//						}
					}
				}
			}
		}
	}

	
	
	/**
	 * Si todos los detalles de la receta han sido aprobados, canbia el estado de la autorizacion del canasto para que pueda confirmar el PEDIDO
	 * @param request
	 * @throws Exception
	 */
	public static void aplicarEstadoAutorizacionStockRecetaADetalle(HttpServletRequest request) throws Exception{
		
		Collection<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		Collection<String> clasificacionesAutorizacionGerenteComercial = obtenerClasificacionesAutorizacionGerenteComercial(request);
		
		if(CollectionUtils.isNotEmpty(detallePedidoCol)){

			long codigoTipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
				
				//si el detalle tiene autorizaciones de stock
				if(verificarArticuloPorTipoAutorizacion(detallePedidoDTO, codigoTipoAutorizacionStock)){
					
					//se verifica si el detalle es un canasto especial
					if((obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
							|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO))){
					
						//se recorren los detalles del canasto para verificar los detalles que no tiene stock
						if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
							if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
						
								Boolean autorizacionesAprobadas = Boolean.TRUE;
								
								LogSISPE.getLog().info("el "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" tiene detalles con autorizacion de stock ");
								//se recorren los detalles del canasto especial
								for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
						
									if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
											&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
											&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
										
										if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() == null){
											autorizacionesAprobadas = Boolean.FALSE;
											
										}else if(!articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
											autorizacionesAprobadas = Boolean.FALSE;
											LogSISPE.getLog().info(articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" en estado "
											+articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion());
										}
									}
								}
								if(autorizacionesAprobadas){
									//si todas las autorizaciones han sido aprobadas se cambia el estado del canasto
									for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
										if(autorizacionActual != null && autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == codigoTipoAutorizacionStock){
											autorizacionActual.setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_APROBADA);
											autorizacionActual.getEstadoPedidoAutorizacionDTO().setEstado(ConstantesGenerales.ESTADO_AUT_GESTIONADA);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Verifica si el codigo de autorizacion se encuentra en la actual cola de autorizaciones
	 * @param codigoAutorizacion
	 * @param colaAutorizaciones
	 * @return true si el codigo esta en la cola de autorizaciones, false caso contrario
	 */
	private static Boolean verificarSiCodigoAutorizacionEstaRepetido(Long codigoAutorizacion, Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones){
		LogSISPE.getLog().info("verificando si el codigo de autorizacion {} fue usado anteriormente ",codigoAutorizacion);
		
		if(CollectionUtils.isNotEmpty(colaAutorizaciones) && codigoAutorizacion != null){
			for(EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO : colaAutorizaciones){
				if(estadoPedidoAutorizacionDTO.getCodigoAutorizacion() != null && estadoPedidoAutorizacionDTO.getCodigoAutorizacion().longValue() == codigoAutorizacion 
						&& estadoPedidoAutorizacionDTO.getEstado().equals(ConstantesGenerales.ESTADO_AUT_PENDIENTE)){
					LogSISPE.getLog().info(" el codigo de autorizacion {} ya fue usado anteriormente ",codigoAutorizacion);
					return Boolean.TRUE;
				}
				
			}
		}
		return Boolean.FALSE;
	}
	
	
	/**
	 * Obtiene la cantidad de detalles que se solicitaran stock,
	 * si el detalle tiene autorizado anteriormente alguna cantidad se solicita solo por la diferencia
	 * @param detallePedidoDTO
	 * @return
	 */
	private static Integer obtenerCantidadSolicitarStock(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO){
		
		Integer cantidadSolicitarStock = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().intValue()
				- detallePedidoDTO.getArticuloDTO().getNpCantidadCanastosReservados().intValue();
//				- ((detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo() > 0) 
//						? detallePedidoDTO.getArticuloDTO().getNpStockArticulo().intValue() : 0);
		
		//si el detalle tiene autorizaciones anteriores
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol())){
			
			//si el detalle tiene autorizaciones anteriores
			for(DetalleEstadoPedidoAutorizacionDTO detEstPedAutorizacionDTO : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()){
				
				if(detEstPedAutorizacionDTO != null && detEstPedAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue()
					== ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue()){
					
					//si la autorizacion fue aprobada
					if(detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) || detEstPedAutorizacionDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA)){
						
						for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detEstPedAutorizacionDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
							
							//se compara los datos
							if(detEstPedAutorizacionDTO.getId().getCodigoCompania() == stockDTO.getCodigoCompania()
									&& detEstPedAutorizacionDTO.getId().getCodigoAreaTrabajo().equals(stockDTO.getCodigoAreaTrabajo()) 
									&& detEstPedAutorizacionDTO.getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
									&& detEstPedAutorizacionDTO.getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())
									&& stockDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
								
								Boolean validarCantidades = false;
								
								if(StringUtils.isNotEmpty(stockDTO.getCodigoArticuloRelacionado())){
									if(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(stockDTO.getCodigoArticuloRelacionado())){
										validarCantidades = true;
									}
								}else{
									validarCantidades = true;
								}
								
								if(validarCantidades){
									LogSISPE.getLog().info("se resta la cantidad aprobada anteriormente: "+stockDTO.getCantidadParcialSolicitarStock());
									
									Integer cantidadFavor =  stockDTO.getCantidadTotalAutorizadaStock();
//									if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo() > 0
//											&& stockDTO.getCantidadUtilizada() != null && stockDTO.getCantidadUtilizada() > 0){
//										cantidadFavor -= stockDTO.getCantidadUtilizada(); 
//									}
									//si la cantidad actual es menor a la autorizada, no se solicita ninguna autorizacion
									if(cantidadSolicitarStock > cantidadFavor){
										cantidadSolicitarStock -= cantidadFavor;
										stockDTO.setCantidadUtilizada(stockDTO.getCantidadTotalAutorizadaStock());
									}else{	
										//stockDTO.setCantidadUtilizada(cantidadSolicitarStock);
										stockDTO.setCantidadUtilizada(stockDTO.getCantidadUtilizada() + cantidadSolicitarStock);
										cantidadSolicitarStock = 0;
										detallePedidoDTO.setNpSinStockPavPolCan(false);
										detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(new ArrayList<DetalleEstadoPedidoAutorizacionDTO>
										(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()));
									}
									stockDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
									break;
								}
							}
						}
					}
				}
			}
		}
		LogSISPE.getLog().info("articulo: "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+ " cantidad de stock a solicitar: "+cantidadSolicitarStock);
		return cantidadSolicitarStock;
	}
	
	
	/**
	 * si el detalle tiene relacionada una autorizacion, retorna dicha autorizacion caso contrario un nuevo objeto.
	 * @param detallePedidoDTO
	 * @return
	 */
	public static DetalleEstadoPedidoAutorizacionStockDTO obtenerDetalleEstadoPedidoAutorizacionStockDTO(DetallePedidoDTO detallePedidoDTO, Boolean verificarAutorizacionRelacionada, String codigoArticuloRelacion){
		
		//se crea una nueva instancia de DetalleEstadoPedidoAutorizacionStockDTO
		DetalleEstadoPedidoAutorizacionStockDTO deStockDTO = new DetalleEstadoPedidoAutorizacionStockDTO();
		deStockDTO.setEstadoAutorizacion(ConstantesGenerales.ESTADO_AUT_SOLICITADA);
		
		Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionStockCol ;
		if(verificarAutorizacionRelacionada){
			autorizacionStockCol = detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol();
		}else{
			autorizacionStockCol = detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol();
		}
		
		if(CollectionUtils.isNotEmpty(autorizacionStockCol)){

			//se recorren las autorizaciones
			for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : autorizacionStockCol){
				if(CollectionUtils.isNotEmpty(detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
					
					for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
						//se compara los id
						if(detallePedidoDTO.getId().getCodigoCompania() == stockDTO.getCodigoCompania()
								&& detallePedidoDTO.getId().getCodigoAreaTrabajo().equals(stockDTO.getCodigoAreaTrabajo()) 
								&& detallePedidoDTO.getId().getCodigoPedido().equals(stockDTO.getCodigoPedido())
								&& detallePedidoDTO.getId().getCodigoArticulo().equals(stockDTO.getCodigoArticulo())){
							
							if(StringUtils.isNotEmpty(stockDTO.getCodigoArticuloRelacionado()) && StringUtils.isNotEmpty(codigoArticuloRelacion)){
								
								if(codigoArticuloRelacion.equals(stockDTO.getCodigoArticuloRelacionado())){
									deStockDTO = SerializationUtils.clone(stockDTO);
									break;
								}
							}else{
								deStockDTO = SerializationUtils.clone(stockDTO);
								break;
							}
						}
					}
				}
			}
		}else if(!verificarAutorizacionRelacionada){
			return null;
		}
		deStockDTO.setCantidadParcialSolicitarStock(deStockDTO.getCantidadParcialSolicitarStock() != null ? deStockDTO.getCantidadParcialSolicitarStock() : 0);
		deStockDTO.setCantidadTotalSolicitarStock(deStockDTO.getCantidadTotalSolicitarStock() != null ? deStockDTO.getCantidadTotalSolicitarStock() : 0);
		return deStockDTO;
	}
	
	
	/**
	 * Elimina de un detallePedidoDTO una autorizacion de stock
	 * @param detallePedidoDTO
	 * @param auStockDTO
	 */
	private static void eliminarAutorizacionStock(DetallePedidoDTO detallePedidoDTO, DetalleEstadoPedidoAutorizacionStockDTO auStockDTO){
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){

			//se recorren las autorizaciones
			for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				if(CollectionUtils.isNotEmpty(detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
					
					for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
						
						//se compara los id
						if(stockDTO.getId().getCodigoDetEstPedAutStock().longValue() == auStockDTO.getId().getCodigoDetEstPedAutStock().longValue()){
							
							LogSISPE.getLog().info("Eliminando la autorizacion de stock con codigo {}",stockDTO.getId().getCodigoDetEstPedAutStock());
							detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol().remove(stockDTO);
							break;
						}
					}
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol())){

			//se recorren las autorizaciones
			for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionAnteriorCol()){
				if(CollectionUtils.isNotEmpty(detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
					
					for(DetalleEstadoPedidoAutorizacionStockDTO stockDTO : detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
						
						//se compara los id
						if(stockDTO.getId().getCodigoDetEstPedAutStock().longValue() == auStockDTO.getId().getCodigoDetEstPedAutStock().longValue()){
							
							LogSISPE.getLog().info("Eliminando la autorizacion de stock con codigo {}",stockDTO.getId().getCodigoDetEstPedAutStock());
							detalleAutorizacion.getDetalleEstadoPedidoAutorizacionStockDTOCol().remove(stockDTO);
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Valida si debe solicitar autorizacion de stock en base a un parametro con fechas y clasificaciones que no debe pedir stock
	 * @param request
	 * @param codigoClasificacion
	 * @return true si hay que pedir autorizacion de stock, false caso contrario
	 */
	public static boolean solicitarAutorizacionStockPorFechas(HttpServletRequest request, String codigoClasificacion){
		try{
			String fechasNoValidarStock = (String)request.getSession().getAttribute(FECHAS_NO_VALIDAR_STOCK);
			
			if(fechasNoValidarStock == null){
				//se obtiene el parametro que indica las fechas que no hay que validar stock
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.fechas.no.solicitar.stock", request);
				fechasNoValidarStock = parametroDTO.getValorParametro();
				
				//no se sube a sesion para que siempre se consulte el valor del parametro
//				request.getSession().setAttribute(FECHAS_NO_VALIDAR_STOCK, fechasNoValidarStock);
			}
			
			LogSISPE.getLog().info("datos del parametro: {}",fechasNoValidarStock);
			
			if(StringUtils.isNotEmpty(fechasNoValidarStock)){
				
				//se obtienen los rangos de fechas 
				String[] fechasNoValidar = fechasNoValidarStock.split(SEPARADOR_COMA);
				
				//se recorren los rangos de fechas
				if(fechasNoValidar != null && fechasNoValidar.length > 0){
					
					for(String fechaNoValidarActual : fechasNoValidar){
						
						//se obtienen las fechas y clasificaciones de cada rango de fechas
						String[] fechasClasificaciones = fechaNoValidarActual.split(SEPARADOR_DOS_PUNTOS);
						
						//se obtienen los datos 
						if(fechasClasificaciones != null && fechasClasificaciones.length == 3){
							
							Calendar calendario = Calendar.getInstance();
							
							SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd");
							Date fechaInicio = formatFecha.parse(calendario.get(Calendar.YEAR)+"-"+fechasClasificaciones[0]);
							List<String> clasificaciones = Arrays.asList(fechasClasificaciones[1].split(SEPARADOR_TOKEN));
							Date fechaFin = formatFecha.parse(calendario.get(Calendar.YEAR)+"-"+fechasClasificaciones[2]);
							
							//se obtiene la fecha actual
							Date fechaActual = new Date();
							LogSISPE.getLog().info("fechaInicio: "+formatFecha.format(fechaInicio)+" fechaActual: "
							+formatFecha.format(fechaActual)+" fechaFin "+formatFecha.format(fechaFin));
							
							//la fecha actual tiene que ser mayor o igual a la fecha de inicio y menor o igual a la fecha fin
							if(fechaActual.compareTo(fechaInicio) >= 0 && fechaActual.compareTo(fechaFin) <= 0 && clasificaciones.contains(codigoClasificacion)){
								
								LogSISPE.getLog().info("no hay que solicitar autorizacion de stock para la clasificacion {}",codigoClasificacion);
								return false;
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			LogSISPE.getLog().error("Error al validarFechaActivaSolicitarAutorizacionPorClasificacion. {}",e);
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param detallePedidoDTO
	 */
	public static void aplicarEstadoAutorizacionStockCanastoEspecial(DetallePedidoDTO detallePedidoDTO){
		
		long tipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
		
		//si el detalles es de tipo canasto y tiene autorizaciones de stock
		if((obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
				|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)) 
				&& CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())
				&& verificarArticuloPorTipoAutorizacion(detallePedidoDTO,tipoAutorizacionStock)){
			
			//se recorren las autorizaciones
			for(DetalleEstadoPedidoAutorizacionDTO autorizacionDetalle : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
				
				//si la autorizaciones tiene autorizaciones de stock
				if(autorizacionDetalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(tipoAutorizacionStock)
						&& CollectionUtils.isNotEmpty(autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
					
					//se recorren las autorizaciones de stock
					for(DetalleEstadoPedidoAutorizacionStockDTO autStock : autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
						
						if(autStock.getEstado().equals(ConstantesGenerales.ESTADO_ACTIVO) && StringUtils.isNotEmpty(autStock.getEstadoAutorizacion())){
							
							//se asigna ese estado de autorizacion al detalle de la receta
							ArticuloRelacionDTO artRelacionDTO = obtenerArticuloRelacion(detallePedidoDTO, autStock.getCodigoArticulo(), autStock.getCodigoArticuloRelacionado());
							if(artRelacionDTO != null){
								artRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(autStock.getEstadoAutorizacion());
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Obtiene un articuloRelacion de un canasto especial en base a los codigos
	 * @param detallePedidoDTO
	 * @param codigoArticuloCanasto
	 * @param codigoArticuloReceta
	 * @return articuloRelacion si existe en el canasto, null caso contrario
	 */
	public static ArticuloRelacionDTO obtenerArticuloRelacion(DetallePedidoDTO detallePedidoDTO, String codigoArticuloCanasto, String codigoArticuloReceta){
		
		//se recorre la receta del canasto especial
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
			if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
				
				for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
					
					//se comparan los codigos
					if(articuloRelacionDTO.getId().getCodigoArticulo().equals(codigoArticuloCanasto)
							&& articuloRelacionDTO.getId().getCodigoArticuloRelacionado().equals(codigoArticuloReceta)){
						return articuloRelacionDTO;
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Cuando se eliminan detalles de una canasta especial que tienen autorizacion de stock, se inactiva la referencia a ese detalle en DetalleEstadoPedidoAutorizacionStockDTO
	 * @param detallePedidoDTO
	 */
	public static void inactivarAutorizacionesStockItemsEliminados(DetallePedidoDTO detallePedidoDTO){
	
		LogSISPE.getLog().info(" validando si los items eliminados tienen autorizaciones de stock");
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getItemsEliminadosReceta())){
			
			long tipoAutorizacionStock = ConstantesGenerales.TIPO_AUTORIZACION_STOCK.longValue();
			
			//si el detalles es de tipo canasto y tiene autorizaciones de stock
			if((obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
					|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)) 
					&& CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())
					&& verificarArticuloPorTipoAutorizacion(detallePedidoDTO,tipoAutorizacionStock)){
				
				Collection<DetalleEstadoPedidoAutorizacionDTO> autEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
				//se recorren las autorizaciones
				for(DetalleEstadoPedidoAutorizacionDTO autorizacionDetalle : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
					
					Collection<DetalleEstadoPedidoAutorizacionStockDTO> autStockEliminarCol = new ArrayList<DetalleEstadoPedidoAutorizacionStockDTO>();
					
					//si las autorizaciones tiene del tipo stock
					if(autorizacionDetalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(tipoAutorizacionStock)
							&& CollectionUtils.isNotEmpty(autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol())
							&& CollectionUtils.isNotEmpty(detallePedidoDTO.getItemsEliminadosReceta())){
						
						//se recorren las autorizaciones de stock
						for(DetalleEstadoPedidoAutorizacionStockDTO autStock : autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
							
							//se recorren los detallesEliminados
							for(String clasificacionEliminada : detallePedidoDTO.getItemsEliminadosReceta()){
								
								if(autStock.getCodigoArticuloRelacionado() != null && autStock.getCodigoArticuloRelacionado().equals(clasificacionEliminada)){
									
									autStock.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
									int cantidadUtilizada = (autStock.getCantidadUtilizada() != null && autStock.getCantidadParcialSolicitarStock() != null) ? 
											(autStock.getCantidadUtilizada() - autStock.getCantidadParcialSolicitarStock()) : 0;
									autStock.setCantidadUtilizada(cantidadUtilizada > 0 ? cantidadUtilizada : 0);
									LogSISPE.getLog().info("autorizacion de stock eliminada para el articulo relacionado: {}",clasificacionEliminada);
									autStockEliminarCol.add(autStock);
									break;
								}
							}
						}
						if(autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol().size() == autStockEliminarCol.size()){
							autEliminarCol.add(autorizacionDetalle);
						}else{
							autorizacionDetalle.getDetalleEstadoPedidoAutorizacionStockDTOCol().removeAll(autStockEliminarCol);
						}
					}
				}
				detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autEliminarCol);
			}
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @param detallePedidoDTO
	 */
	public static boolean verificarSolicitarAutorizacionStockCanastoEspecial(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO){
		
		//se verifica si es canasto especial
		if(obtenerCodigoClasificacion(detallePedidoDTO).equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
				|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
			
			Collection<String> clasificacionesAutorizacionGerenteComercial = new ArrayList<String>();
			try {
				clasificacionesAutorizacionGerenteComercial = obtenerClasificacionesAutorizacionGerenteComercial(request);
			} catch (Exception e) {
				LogSISPE.getLog().error("error al ");
			}
			LogSISPE.getLog().info("validar el stock de los detalles del canasto especial");
			
			if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
				
				//se recorren los detalles del canasto especial
				for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
					
					if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
							&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
							&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
						
						if(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() !=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() !=null){
							
							if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 
							articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() && detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0){
								
								 if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() != null
										&& (articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
												|| articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))){
									
									//para el caso de autorizaciones aprobadas o gestionadas se verifica las cantidades de los items del canasto
									DetalleEstadoPedidoAutorizacionStockDTO autStockDTO = AutorizacionesUtil.obtenerDetalleEstadoPedidoAutorizacionStockDTO(detallePedidoDTO, false, articuloRelacionDTO.getId().getCodigoArticuloRelacionado());
									if(autStockDTO != null){
										
										Long cantidadReservarSic = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() - 
												(( articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() != null ? articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() : 0)
														+ articuloRelacionDTO.getArticuloRelacion().getNpCantidadCanastosReservados());
//											
										if(autStockDTO.getCantidadUtilizada() != null && cantidadReservarSic > autStockDTO.getCantidadUtilizada().longValue()){
											
											LogSISPE.getLog().info("la receta "+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" necesita autorizacion de stock por: "+(cantidadReservarSic - autStockDTO.getCantidadUtilizada().longValue()));
											return true;
										}
									}
								}else if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() != null
										&& articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_RECHAZADA)){
									
									LogSISPE.getLog().info("la autorizacion de la receta "+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" ha sido rechazada, necesita solicitar autorizacion por ese detalle");
									detallePedidoDTO.setNpSinStockPavPolCan(true);
									return true;
									
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * En los pedidos reservados con canastos especiales, asigna en el campo snpCantidadCanastosReservados la cantidad que fue reservada
	 * @param request
	 */
	public static void asignarNpCantidadCanastoEspecialReservado(HttpServletRequest request){
		
		try{
			HttpSession session = request.getSession();
			Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			String accioActual = (String) session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			
			if(accioActual != null && accioActual.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
					&& CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				String codigoClasificacion;
				//se recorren los detalles del pedido
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
					
					//se verifica si es canasto especial
					codigoClasificacion = obtenerCodigoClasificacion(detallePedidoDTO);
					if(codigoClasificacion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						
						//se recorre la receta
						if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
							
							for(ArticuloRelacionDTO arRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
//								arRelacionDTO.getArticuloRelacion().setNpCantidadCanastosReservados(detallePedidoDTO.getArticuloDTO().getNpStockArticulo());
								arRelacionDTO.getArticuloRelacion().setNpCantidadCanastosReservados(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado() * arRelacionDTO.getCantidad());
								LogSISPE.getLog().info("articulo receta: "+arRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" - reservado "+arRelacionDTO.getArticuloRelacion().getNpCantidadCanastosReservados());
							}
						}
					}else{
						detallePedidoDTO.getArticuloDTO().setNpCantidadCanastosReservados(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
						LogSISPE.getLog().info("articulo: "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" - reservado "+detallePedidoDTO.getArticuloDTO().getNpCantidadCanastosReservados());
					}
				}
			}
		}catch (Exception e){
			LogSISPE.getLog().error("Error al asignarNpCantidadCanastoEspecialReservado. "+e);
		}
	}
	

	/**
	 * retorna el codigoAreaTrabajo por defecto, pero si esta logueado el ADMINSISPE se toma el area de trabajo del local seleccionado
	 * @param request
	 * @return
	 */
	public static String asignarAreaTrabajoAutorizacion(HttpServletRequest request){
		
		String codigoAreaTrabajo = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString();
		
		try{
			
			HttpSession session = request.getSession();		
			
			//si el adminSispe esta realizando una cotizacion con autorizaciones, se setea el area de trabajo del local seleccionado
			if(StringUtils.isNotEmpty(codigoAreaTrabajo) && Integer.parseInt(codigoAreaTrabajo) == 0){
				
				Integer codigoLocalSeleccionado = null;
				
					if(session.getAttribute(CODIGO_LOCAL_REFERENCIA) != null){
						
						codigoLocalSeleccionado = (Integer) session.getAttribute(CODIGO_LOCAL_REFERENCIA);
						
					}else if(session.getAttribute(AnulacionesAction.INDICE_PEDIDO_SELECCIONADO) != null){
						
						//cuando el adminsispe va a anular un pedido se toma el codigo del local del vistaPedidoDTO
						int indice = Integer.parseInt((String) session.getAttribute(AnulacionesAction.INDICE_PEDIDO_SELECCIONADO));
						List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
						
						if(CollectionUtils.isNotEmpty(pedidos)){
							//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO 
							VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
							if(vistaPedidoDTO != null && vistaPedidoDTO.getId().getCodigoAreaTrabajo() != null){
								codigoLocalSeleccionado = vistaPedidoDTO.getId().getCodigoAreaTrabajo();
							}
						}
					}
				
				
				if(codigoLocalSeleccionado != null && codigoLocalSeleccionado > 0){
					codigoAreaTrabajo = codigoLocalSeleccionado.toString();
				}
			}
		}catch (Exception e){
			LogSISPE.getLog().error("Error al obtener el codigo de area de trabajo del pedido "+e);
		}
		
		LogSISPE.getLog().info("codigoAreaTrabajo:  {}",codigoAreaTrabajo);
		return codigoAreaTrabajo;
	}
	
	
	/**
	 * Si esta logueado el usuario adminsispe y desea cotizar pedidos con autorizaciones,
	 * se consulta el usuario administrador(admin,despa,cajas del parametro 162) del local seleccionado
	 * y se setea ese usuario como usuario autorizado. 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String asignarUsuarioAutorizado(HttpServletRequest request) throws Exception{
						
		String userId = SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId();
		
		try{
			
			String codigoAreaTrabajo = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString();
			if(StringUtils.isNotEmpty(codigoAreaTrabajo) && Integer.parseInt(codigoAreaTrabajo) == 0){
				
				Integer codigoLocalSeleccionado = Integer.valueOf(asignarAreaTrabajoAutorizacion(request));
				
				if(codigoLocalSeleccionado != null && codigoLocalSeleccionado > 0){
					
					ParametroDTO parametroDto = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.usuarios.administradores.local", request);
					
					if(parametroDto != null && StringUtils.isNotEmpty(parametroDto.getValorParametro()) && parametroDto.getValorParametro().contains(SEPARADOR_COMA)){
						
						LogSISPE.getLog().info("consultando los usuarios {} del local:  {}", parametroDto.getValorParametro(), codigoLocalSeleccionado);
						String[] usuarios = parametroDto.getValorParametro().split(SEPARADOR_COMA);
						
						if(usuarios != null && usuarios.length > 0){
							
							Collection<UserDto>  usersDto ;
							
							for(String usuarioActual : usuarios){
								
								usersDto =  FrameworkFactory.getSecurityService().findUsersByUserName(usuarioActual.trim()+codigoLocalSeleccionado, false, false, false);
								
								if(usersDto != null && usersDto.size() == 1){
									
									userId = usersDto.iterator().next().getUserId();
									break;
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			LogSISPE.getLog().error("Error al consultar el userId actual "+e);
		}
		
		LogSISPE.getLog().info("UserId actual: "+userId);
		return userId;
	}
	
	
	/**
	 * Genera el Strig con los datos para finalizar el flujo de la autorizacion
	 * @param request
	 * @param detalleAut
	 * @param autorizacionDTO
	 * @return
	 * @throws MissingResourceException
	 * @throws Exception
	 */
	public static String obtenerAutorizacionPorFinalizar(HttpServletRequest request, String[] detalleAut, 
			ec.com.smx.autorizaciones.dto.AutorizacionDTO autorizacionDTO) throws MissingResourceException, Exception{
		
		String autorizacionPorFinalizar = "";
//		processCode,estadoAutorizacion,companyID,sistemID,userID
		
		if(detalleAut != null && detalleAut.length >= 4){
			
			autorizacionPorFinalizar = detalleAut[2]+
					SEPARADOR_COMA+detalleAut[4]+
					SEPARADOR_COMA+SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania()+
					SEPARADOR_COMA+MessagesWebSISPE.getString("security.SYSTEM_ID_AUTORIZACIONES")+
					SEPARADOR_COMA+asignarUsuarioAutorizado(request);
			
		} else if(autorizacionDTO != null){
			
			autorizacionPorFinalizar = autorizacionDTO.getProcessCode()+
					SEPARADOR_COMA+autorizacionDTO.getEstadoActualAutorizacion()+
					SEPARADOR_COMA+autorizacionDTO.getId().getCodigoCompania()+
					SEPARADOR_COMA+autorizacionDTO.getId().getCodigoSistema()+
					SEPARADOR_COMA+asignarUsuarioAutorizado(request);
		}
		
		LogSISPE.getLog().info("Autorizacion por finalizar: "+autorizacionPorFinalizar);
		return autorizacionPorFinalizar;
	}
	
	
	/**
	 * Obtiene la AutorizacionEstadoPedidoDTO asociada a un DetallePedidoDTO
	 * @param vistaDetallePedidoDTO
	 * @param codigoTipoAutorizacion
	 * @return
	 */
	public static AutorizacionEstadoPedidoDTO obtenerEstadoPedidoAutorizacionDTO(VistaDetallePedidoDTO vistaDetallePedidoDTO, long codigoTipoAutorizacion){
		
		AutorizacionEstadoPedidoDTO autorizacionEstadoPedidoDTO = null;
		
		try{
			AutorizacionEstadoPedidoDTO autConsultaDTO = new AutorizacionEstadoPedidoDTO();
			autConsultaDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			autConsultaDTO.setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
			autConsultaDTO.setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			autConsultaDTO.setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			autConsultaDTO.setCodigoEstado(vistaDetallePedidoDTO.getId().getCodigoEstado());
			autConsultaDTO.setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
			
			autConsultaDTO.setAutorizacionDTO(new ec.com.smx.autorizaciones.dto.AutorizacionDTO());
			autConsultaDTO.getAutorizacionDTO().setAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo().toString());
			autConsultaDTO.getAutorizacionDTO().setCodigoTipoAutorizacion(codigoTipoAutorizacion);
			autConsultaDTO.getAutorizacionDTO().getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			autConsultaDTO.getAutorizacionDTO().getId().setCodigoSistema(CorporativoConstantes.SYSTEMID_SISPE);
			
			autorizacionEstadoPedidoDTO = SISPEFactory.getDataService().findUnique(autConsultaDTO);
		
		}catch(Exception e){
			LogSISPE.getLog().error("Error al obtenerEstadoPedidoAutorizacionDTO "+e);
		}
		return autorizacionEstadoPedidoDTO;
		
	}
	
}

