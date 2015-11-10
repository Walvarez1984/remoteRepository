/*
 * Clase CotizarReservarAction.java
 * Creado el 27/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.ACEPTAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_CON_IVA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CANCELAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CHECK_PAGO_EFECTIVO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_ESTABLECIMIENTO_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_PEDIDO_CONSOLIDADOS_AUX;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_TIPOS_PEDIDO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PAR_ACE_USO_AUT;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PAR_CAN_USO_AUT;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PAR_USAR_AUTORIZACION;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PERSONADTO_COL;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PREFIJO_VARIABLE_SESION;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.SUBIR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.TIPO_AUTORIZACION_COL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CODIGO_ESTADO_SINPAGO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_AUMENTAR_PRODUCTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_PROCESO_QUITAR_PRODUCTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_RESERVACION_CAMBIO_CONTACO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_RESERVACION_MODIFICACION;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_RESERVACION_MODIFICACION_CONTACTO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.TIPO_RESERVACION_NUEVO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearchParameter;
import ec.com.smx.autorizaciones.common.factory.AutorizacionesFactory;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.PersonaDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.TipoAutorizacionDTO;
import ec.com.smx.corporativo.gestionservicios.dto.EmpresaDTO;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.FuncionarioDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.web.util.CorpCommonWebConstantes;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.mensajeria.commons.util.WebMensajeriaUtil;
import ec.com.smx.sic.cliente.common.SICConstantes;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloMedidaDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.sispe.TipoDescuentoID;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.mercancias.dto.ConvenioEntregaDomicilioDTO;
import ec.com.smx.sic.mercancias.dto.ConvenioEntregaDomicilioDetalleDTO;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.BuscarArticuloUtil;
import ec.com.smx.sic.sispe.common.util.ClienteUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.CotizarPedidosAnterioresUtil;
import ec.com.smx.sic.sispe.common.util.DescuentosUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.EnvioMailUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
//import ec.com.smx.sic.sispe.commons.util.OrdenCompraUtil;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.EstructuraResponsable;
import ec.com.smx.sic.sispe.dto.AbonoPedidoDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoID;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleHoraCamionLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioHoraLocalDTO;
import ec.com.smx.sic.sispe.dto.ClientePedidoDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Permite realizar la cotizaci\u00F3n de un pedido especial.
 * Maneja un conjunto de tags que muestran diversas secciones del formulario:
 * <ul>
 * 	<li>DETALLE DEL PEDIDO: Muestra el detalle de los art\u00EDculos incluidos en el pedido y los totales.</li>
 * 	<li>BUSQUEDA DE ARTICULOS: Muestra una secci\u00F3n donde se puede realizar la b\u00FAsqueda de art\u00EDculos, 
 * 		ya sea por c\u00F3digo de clasificaci\u00F3n, por descripci\u00F3n de la clasificaci\u00F3n o por nombre del art\u00EDculo.</li>
 * 	<li>DESCUENTOS: Muestra la secci\u00F3n donde se escoge el o los tipos de descuento que se asignar\u00E1 al pedido.</li>
 *  <li>AUTORIZACION: Muestra la secci\u00F3n donde se ingresa el n\u00FAmero de autorizaci\u00F3n en caso de necesitarse.</li>
 * </ul>
 * <ul>
 * 	Las siguientes secciones solo est\u00E1n disponibles en una RESERVACI\u00F3N:
 * 	<li>FORMA DE PAGO: Muestra la secci\u00F3n donde se escoge la forma de pago que se asignar\u00E1 al pedido.</li>
 *  <li>ENTREGAS A DOMICILIO: Muestra la secci\u00F3n donde se ingresan las cantidades a entregar de cada art\u00EDculo junto con las 
 * 		direcciones domiciliarias.</li>
 *  <li>ENTREGAS A LOCALES: Muestra la secci\u00F3n donde se ingresan las cantidades a entregar de cada art\u00EDculo y el local donde 
 * 		ser\u00E1n retirados.</li>
 * </ul>
 * 
 * @author 	fmunoz
 * @author 	Wladimir L\u00F3pez
 * @version 3.0
 * @since  	JSDK 1.5.0
 */
@SuppressWarnings("unchecked")
public class CotizarReservarAction extends BaseAction
{
	//Clase utilizada para convertir una fecha de formato String a Timestamp
	private SqlTimestampConverter convertidor = null;
	//constatntes para nombrar las variables de sesi\u00F3n
	private final String ORDEN_ASC = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	public static final String EXISTEN_PAVOS = "ec.com.smx.sic.sispe.pedido.existenPavos";
	public static final String CAMBIO_PESO = "ec.com.smx.sic.sispe.pedido.cambioPesoPavos";
	public static final String EXISTEN_ESPECIALES_RESERVADOS = "ec.com.smx.sic.sispe.pedido.existenEspeciales";
	public static final String EXISTEN_ENTREGAS_SICMER = "ec.com.smx.sic.sispe.pedido.existenEntregasSICMER";
	public static final String SUB_PAGINA = "ec.com.smx.sic.sispe.pedido.subPagina";
	public static final String ACCION_ANTERIOR = "ec.com.smx.sic.sispe.cotizarRecotizar.accionAnterior";
	public static final String COL_TIPO_ESPECIALES = "ec.com.smx.sic.sispe.pedido.tipoEspeciales";
	public static final String COL_ART_ESPECIALES = "ec.com.smx.sic.sispe.pedido.listaEspeciales";
	public static final String DETALLE_PEDIDO = "ec.com.smx.sic.sispe.detallePedido";
	public static final String DETALLE_PEDIDO_VALIDAR_NUMBONOS = "ec.com.smx.sic.sispe.detallePedido.validar.numero.bonos";
	public static final String DETALLE_PEDIDO_ORIGINAL = "ec.com.smx.sic.sispe.detallePedido.original";
	public static final String DETALLE_PEDIDO_CLONE = "ec.com.smx.sic.sispe.detallePedidoClone";
	public static final String DETALLE_PEDIDO_CLONE_AUX = "ec.com.smx.sic.sispe.detallePedidoCloneAux";		
	public static final String DETALLES_PEDIDOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.detallesPedidosConsolidados";
	public static final String DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS = "ec.com.smx.sic.sispe.detallesPedidosConsolidados.no.repetidos";
	public static final String DETALLES_PEDIDOS_CONSOLIDADOS_ELIMINADOS = "ec.com.smx.sic.sispe.detallesPedidosConsolidadosEliminados";
	public static final String VISTA_PEDIDOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.vistaPedidosConsolidados";
	public static final String VISTA_PEDIDOS_DESCUENTOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.vistaPedidosDescuentosConsolidados";
	public static final String VISTA_PEDIDOS_CONSOLIDADOS_TOTAL = "ec.com.smx.sic.sispe.vistaPedidosConsolidadosTotal";
	public static final String VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS = "ec.com.smx.sic.sispe.vistaPedidosConsolidadosEliminados";
	public static final String PEDIDOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.pedidosConsolidados";
	public static final String PEDIDOS_CONSOLIDADOS_ELIMINADOS = "ec.com.smx.sic.sispe.pedidosConsolidadosEliminados";
	public static final String DETALLE_TOTAL_CONSOLIDADO = "ec.com.smx.sic.sispe.detalleTotalConsolidados";
	public static final String COL_CODIGOS_ARTICULOS = "ec.com.smx.sic.sispe.pedido.codigosArticulos";
	public static final String SUB_TOTAL_APLICA_IVA = "ec.com.smx.sic.sispe.pedido.subTotalAplicaIVA";
	public static final String SUB_TOTAL_NOAPLICA_IVA = "ec.com.smx.sic.sispe.pedido.subTotalNoAplicaIVA";
	public static final String TOTAL_PEDIDO= "ec.com.smx.sic.sispe.pedido.subTotalPedido";
	public static final String DESCUENTO_TOTAL = "ec.com.smx.sic.sispe.pedido.descuentoTotal";
	public static final String PORCENTAJE_TOT_DESCUENTO = "ec.com.smx.sic.sispe.pedido.descuento.porcentajeTotal";
	public static final String PEDIDO_EN_PROCESO = "ec.com.smx.sic.sispe.pedido.enProceso";
	public static final String LOCAL_ANTERIOR = "ec.com.smx.sic.sispe.cotizarRecotizar.localAnterior";
	public static final String COL_TIPO_DESCUENTO = "ec.com.smx.sic.sispe.pedido.tipoDescuento";
	public static final String COL_TIPO_DESCUENTO_MOSTRAR_RANGOS = "ec.com.smx.sic.sispe.pedido.tipoDescuento.mostrar.rangos";
	public static final String COL_MOTIVO_DESCUENTO = "ec.com.smx.sic.sispe.pedido.motivoDescuento";
//	public static final String COL_COMPRADORES_INTERNOS = "ec.com.smx.sic.sispe.pedido.compradoresCol";
//	public static final String COL_COMPRADORES_PEDIDO = "ec.com.smx.sic.sispe.pedido.compradoresPedidoCol";
	public static final String COL_DESCUENTOS = "ec.com.smx.sic.sispe.pedido.descuentos";
	public static final String DES_MAX_NAV_EMP = "ec.com.smx.sic.sispe.pedido.descuentoMaxNavEmp";
	public static final String COL_DESCUENTOS_PEDIDO_GENERAL = "ec.com.smx.sic.sispe.pedido.descuentosPedidoGeneral";
	public static final String COL_DESCUENTOS_PEDIDO_ACTUAL = "ec.com.smx.sic.sispe.pedido.descuentosActual";
	public static final String COL_DESCUENTOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.pedido.descuentos.consolidados";
	public static final String COL_DESC_SELECCIONADOS = "ec.com.smx.sic.sispe.pedido.descuento.seleccionados";
	public static final String COL_DESC_VARIABLES = PREFIJO_VARIABLE_SESION.concat("descuentoVariableCol");
	public static final String ES_ENTIDAD_BODEGA = "ec.com.smx.sic.sispe.entidadBodega";
	public static final String DETALLE_SIN_ACTUALIZAR = "ec.com.smx.sic.sispe.pedido.detalleSinActualizar";
	public static final String COL_INDICES_CANTIDADES_MODIFICADAS = "ec.com.smx.sic.sispe.detalle.indices.cantidadesModificadas";
	public static final String ELIMINACION_DESDE_DET_PRINCIPAL = "ec.com.smx.sic.sispe.calendarizacion.eliminacionDetallePrincipal";
	public static final String PORCENTAJE_ABONO = "ec.com.smx.sic.sispe.pedido.porcentajeAbono";	
	public static final String VALOR_ABONO = "ec.com.smx.sic.sispe.pedido.valorAbono";	
	//VARIABLES QUE ALMACENAN LAS SUBCLASIFICACIONES DE CANASTAS O DESPENSAS
	public static final String SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA = "ec.com.smx.sic.sispe.tipoArticulo.canasta";
	public static final String SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA = "ec.com.smx.sic.sispe.tipoArticulo.despensa";
	
	public static final String ERRORES_ENVIO_MAIL = "ec.com.smx.sic.sispe.enviarMail.errors";
	public static final String PEDIDO_GENERADO = "ec.com.smx.sic.sispe.pedidoDTO";
	public static final String PORCENTAJE_CALCULO_FLETE = "ec.com.smx.sic.sispe.entregas.porcentajeCalculoFlete";
	public static final String FECHA_MIN_ENTREGA_CD_RES = "ec.com.smx.sic.sispe.pedido.fechaMinimaEntrega";
	public static final String FECHA_MIN_ENTREGA_LOC_RES = "ec.com.smx.sic.sispe.pedido.fechaMinimaEntregaLocRes";
	public static final String BUSQUEDA_PRINCIPAL = "ec.com.smx.sic.sispe.pedido.busquedaPrincipal";
	public static final String FECHA_ENTREGA = "ec.com.smx.sic.sispe.pedido.fechaEntrega";
	public static final String TIPO_ENTREGA = "ec.com.smx.sic.sispe.pedido.tipoEntrega";
	public static final String COSTO_FLETE = "ec.com.smx.sic.sispe.pedido.costoFlete";
	public static final String DESCRICPION_USO_AUTORIZACION = PREFIJO_VARIABLE_SESION.concat("descripcionUsoAutorizacion");
	public static final String OCULTA_SECCION_DESCUENTOS = PREFIJO_VARIABLE_SESION.concat("ocultaSeccionDescuentos");
	
	public static final String TIPO_MENSAJE_VALIDACION = "ec.com.smx.sic.sispe.tipoMensajeValidacion";
	
	static final String DIAS_VALIDEZ = "ec.com.smx.sic.sispe.cotizacion.diasValidez";
	static final String MOSTRAR_AUTORIZACION = "ec.com.smx.sic.sispe.pedido.mostrarAutorizacion";
	static final String RESPONSABLE_LOCAL = "ec.com.smx.sic.sispe.responsable.local";

	private static final String INGRESO_PRIMERA_VEZ_BODEGA = "ec.com.smx.sic.sispe.cotizar.primeraVez.bodega";
	private static final String DIRECTORIO_SALIDA_REPORTE = "ec.com.smx.sic.sispe.reporte.directorioSalida";
	private static final String RESPONSABLE_RESERVACION = "ec.com.smx.sic.sispe.reservacion.responsable";
	public static final String TRANSACCION_REALIZADA = "ec.com.smx.sic.sispe.transaccionRealizada";
	public static final String IMPRIMIR_RESPONSABLES = "ec.com.smx.sic.sispe.imprimirResponsables";
	private static final String CODIGO_TIPO_DESCUENTO_GENERAL = "ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoGeneral";
	public static final String CODIGO_TIPO_DESCUENTO_VARIABLE = "ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable";
	public static final String CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO = "ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo";
	public static final String CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO = "ec.com.smx.sic.sispe.parametro.codigoTipoDes.navEmp.credito";
	public static final String CODIGO_TIPO_DESCUENTO_PORCAJAS = "ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja";
	public static final String CODIGO_TIPO_DESCUENTO_PORMAYORISTA = "ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista";
	public static final String CODIGO_TIPO_DESCUENTO_MARCA_PROPIA = "ec.com.smx.sic.sispe.parametro.codigo.tipo.descuento.marcaPropia";
	public static final String VALOR_PORCENTAJE_TOLERANCIA = "ec.com.smx.sic.sispe.parametro.valorPorcentajeTolerancia";
	public static final String VALOR_CALCULO_LIMITES_PESOS = "ec.com.smx.sic.sispe.parametro.valorCalculoLimitesPesos";
	public static final String CLASIFICACIONES_PERMITIDAS_CAMBIO_PESOS = "ec.com.smx.sic.sispe.parametro.clasificacionesArticulos.cambioPesos";
	public static final String CODIGO_ESTADOS_PERMITIDOS_MODIFICAR_RESERVA = "ec.com.smx.sic.sispe.parametro.codigoEstados.permitidos.modificarReserva";
	public static final String CLASIFICACIONES_AUX_CAMBIO_PESOS = "ec.com.smx.sic.sispe.parametro.clasificacionesArticulos.cambioPesosAux";
	
	public static final String TELEFONO_LOCAL = "ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal";
	public static final String ADMINISTRADOR_LOCAL = "ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"; 
	public static final String COD_FORMATOS_NEGOCIO_CAMBIO_PRECIOS = "ec.com.smx.sic.sispe.pedido.codigosFormatosDeNegocioCambioPrecio";
	public static final String COD_FORMATOS_NEGOCIO_PRECIOS_AFILIADO = "ec.com.smx.sic.sispe.pedido.codigosFormatosDeNegocioPreciosAfiliado";
	public static final String PORCENTAJE_ABONO_PESO_VARIABLE = "ec.com.smx.sic.sispe.porcentajeAbono.pedidoPesoVariable";

	public static final String AUTORIZACION_CONSOLIDAR_MAXIMO = "ec.com.smx.sic.sispe.consolidar.maximo.autorizacionDTO";
	public static final String AUTORIZACION_CAMBIO_PESOS = "ec.com.smx.sic.sispe.cambioDescuento.autorizacionDTO";
	public static final String AUTORIZACION_DESCUENTOS = "ec.com.smx.sic.sispe.descuento.autorizacionDTO";
	public static final String ACEPTAR_DESCUENTO_VARIABLE = "aceptarAutorizacion";//valor que retorna en ayuda cuando acepta la autorizacion de descuentos
	public static final String ACEPTAR_CAMBIO_PESOS = "aceptarAutorizacionPeso";
	public static final String AGREGAR_AUTORIZACION_ARTICULOS = "agregarAutorizacion";//valor que retorna en ayuda cuando acepta la autorizacion de descuentos
	public static final String VALOR_CANCELAR = "cancelarAutorizacion";//valor que retorna en ayuda cuando cancela la autorizacion de descuentos
	public static final String VALOR_CANCELAR_CONSOLIDACION = "cancelarAutorizacionConsolidacion";	
	public static final String VALOR_CANCELAR_RESERVA = "cancelarModificarReserva";
	
	public static final String ACTIVAR_INPUTS_CAMBIO_PRECIOS = "ec.com.smx.sic.sispe.actualizar.preciosUnitarios";
	public static final String ES_RESERVA_TEMPORAL = "ec.com.smx.sic.sispe.pedido.reservacionTemporal";
	
	public static final String CAMBIOS_SOLICITUD_CD_MOD_RES = "ec.com.smx.sic.sispe.modificacionReserva.cambiosSolicitudCD";
	public static final String AUTORIZACION_MODIFICA_RESERVA_SOLICITUD_CD = "ec.com.smx.sic.sispe.autorizacionDTO.modResCamSolCD";
	public static final String NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI = "ec.com.smx.sic.sispe.pedido.numeroBonosRecibir";
	public static final String MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI = "ec.com.smx.sic.sispe.pedido.monto.minimoCompra";
	public static final String VALOR_BONO_COMPROBANTE_MAXI = "ec.com.smx.sic.sispe.pedido.valor.bono.maxinavidad";
	public static final String MONTO_CALCULADO_COMPROBANTE_MAXI = "ec.com.smx.sic.sispe.pedido.valor.calculado.maxinavidad";
	
	public static final String POSICION_DIV = "ec.com.smx.sic.sispe.posicion.div" ;
	
	public static final String TIPO_ELIMINACION = "ec.com.smx.sic.sispe.tipoEliminacion";

	//wc validar si se actualiza el detalle
	public static final String DEBE_ACTUALIZAR = "ec.com.smx.sic.sispe.debeActualizar";
	
	//Popups manuales
	public static final String POPUP_DESCUENTO = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.popupDescuento";
	public static final String POPUP_RESERVAR_STOCK_ENTREGA = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.popupReservarStockEntrega";
	public static final String POPUP_DESCUENTO_VARIABLE = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.popupDescuentoVariable";
	public static final String TIPO_AUTORIZADOR = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.tipoAutorizador";
	public static final String POPUP_AUTORIZACIONES = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.popupAutorizaciones";	
	
	public static final String FORMULARIO_TEMPORAL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.formulario";
	public static final String VALOR_MAX_DES_LOCAL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "pedido.valorMaxDescuento";
	
	
	public static final String COL_DETALLE_PEDIDO_RESERVA = "ec.com.smx.sic.sispe.reservaPedido.detallePedido";
	public static final String COL_DETALLE_PEDIDO_ORIGINAL = "ec.com.smx.sic.sispe.reservaPedido.detallePedido.original";
	public static final String COL_DETALLE_PEDIDO_MODIFICADO = "ec.com.smx.sic.sispe.reservaPedido.detallePedido.modificado";
	public static final String ERROR_ENTREGAS_ACCTION = "ec.com.smx.sic.sispe.errorEntregaAction";
	public static final String COL_RESPONSABLES_ENTREGAS = "ec.com.smx.sic.sispe.responsables.entregas";
	public static final String COL_DESCUENTOS_VARIABLES = "ec.com.smx.sic.sispe.descuentos.autorizaciones.variables";
	
	
	//Variables para descuento
	public static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");//-
	private static final  String CODIGO_GERENTE_COMERCIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoGerenteComercial"); //COM
	private static final  String CODIGO_MOTIVO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento"); //CMD
	private static final  String INDICE_DESCUENTO_GENERAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.descuento.indice.descuentos.general"); //INX
	public static final String CODIGO_TIPO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento"); //CTD
	private static final String EXISTEN_DESCUENTOS_VARIABLES = "ec.com.smx.sic.sispe.existen.dctos.variables"; //SI, NULL
	public static final  String BORRAR_DESCUENTOS = "ec.com.smx.sic.sispe.borrar.descuentos"; // SI, NO
	//aplicar descuentos automaticos de pavos 
    public static final String APLICAR_DESCUENTOS_PAVOS= "ec.com.smx.sic.sispe.descuentos.pavos";
    public static final String TIPO_DESCUENTO_CLASIFICACION_PAVOS_COL= "ec.com.smx.sic.sispe.tipdescla.pavosCol";
    public static final String DETALLES_DCTO_AUTOMATICO_PAVOS_COL= "ec.com.smx.sic.sispe.detalles.conDctoAutomaticoPavos_col";
    public static final String CLASIFICACIONES_PAVOS_SELECCIONADOS_COL= "ec.com.smx.sic.sispe.clasificaciones.seleccionadas.dsctoAutomatico";
    public static final String EXISTEN_DESCUENTOS_CAJAS= "ec.com.smx.sic.sispe.descuentos.cajas"; //SI, NULL
    public static final String EXISTEN_DESCUENTOS_MAYORISTA= "ec.com.smx.sic.sispe.descuentos.mayorista"; //SI, NULL
    public static final String COL_DESCUENTOS_APLICADOS= "ec.com.smx.sic.sispe.descuentos.aplicadosCol";//SI, NULL
    //para autorizacion de entregas
    public static final String POPUPAUTORIZACIONENTREGAS ="ec.com.smx.sic.sispe.popUpAutorizacionEntregas";
    public static final String NUMEROAUTORIZACIONEXTERNAERROR ="ec.com.smx.sic.sispe.numeroautorizacionexternaerror";
    
    public static final String ES_PEDIDO_CONSOLIDADO = "ec.com.smx.sic.sispe.es.pedido.consolidado"; //TRUE, FALSE
    public static final String ELIMINAR_DESCUENTOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.eliminar.descuentos.consolidados"; //TRUE, FALSE
    public static final String SIN_DESCUENTOS_CONSOLIDADO = "ec.com.smx.sic.sispe.sin.descuentos.consolidados"; //TRUE, FALSE
    public static final String DETALLES_CONSOLIDADOS_ACTUALES = "ec.com.smx.sic.sispe.detalles.consolidados.actuales"; //detalles consolidados actuales 
    public static final String OCULTAR_BOTON_DESCUENTOS = "ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion"; //oculta el boton de descuentos consolidacion
    public static final String RESPALDO_OP_DESCUENTOS = "ec.com.smx.sic.sispe.respaldo.op.descuentos";  //respaldo del opDescuentos del formulario
    public static final String RESPALDO_DESCUENTOS_SELECCIONADOS = "ec.com.smx.sic.sispe.respaldo.descuentos.seleccionados";  // respaldos de la coleccion de llaves seleccionadas
    public static final String RESPALDO_DESCUENTOS_CONSOLIDADOS = "ec.com.smx.sic.sispe.respaldo.descuentos.consolidados";  // respaldos de la coleccion de descuentos consolidados
    public static final String TITULO_POPUP_CONFIRMACION = "ec.com.smx.sic.sispe.titulo.popup.confirmacion";
    public static final String BORRAR_ACCION_ANTERIOR = "ec.com.smx.sic.sispe.borrar.accion.anterior"; //indica cuando borrar de sesion la variable AutorizacionUtil.ACCION_ANTERIOR
		
    private static Integer indice = 0;
    private static String codigoArticuloObsoleto;
    public static final String DETALLE_PEDIDO_OBSOLETO = "ec.com.smx.sic.sispe.detallePedidoObsoleto";
    public static final String DETALLE_PEDIDO_TMP = "ec.com.smx.sic.sispe.detallePedidoTmp";
    private static final String HORACOL="ec.com.smx.sic.sispe.horaCol";
    
  //variables para clasificar los tipos descuentos
    public static final String TIPO_DES_EXCLUYENTES="ec.com.smx.sic.sispe.tipoDescuentoExcluyente";
    public static final String TIPO_DES_OTROS="ec.com.smx.sic.sispe.tipoDescuentoOtros";
    public static final String TIPO_DES_VARIABLES="ec.com.smx.sic.sispe.tipoDescuentoVariables";
    public static final String PRIMER_INGRESO_AL_PEDIDO = SessionManagerSISPE.PREFIJO_VARIABLE_SESION +  "primer.ingreso.al.pedido";
    public static final String OPCION_MODIFICAR_RESERVA = SessionManagerSISPE.PREFIJO_VARIABLE_SESION +  "opcion.modificacion.reserva";
    
    //coleccion de los departamentos 
	public static final String COL_DEPARTAMENTOS_PEDIDO = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "departamentos.pedido";
	public static final String SE_AGREGO_ARTICULO_OBSOLETO = "";
	
	//almacena los cambios que se hicieron a una reserva en MODIFICACION DE RESERVA
	public static final String CAMBIOS_MODIFICACION_RESERVA = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "cambios.modificacion.reserva";
	
	public static final String IMPRIMIR_CONVENIOS_SICMER = SessionManagerSISPE.PREFIJO_VARIABLE_SESION + "imprimir.convenios.SICMER";
	public static final String MOSTRAR_RESUMEN_PEDIDO = "ec.com.smx.sic.sispe.mostrar.resumen.pedido";

    boolean verificarstock = false;
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		LogSISPE.getLog().info("Accion Cotizar Reservar Action");
		ActionErrors errores = new ActionErrors();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		//se obtiene el formulario
		CotizarReservarForm formulario = (CotizarReservarForm)form;
		HttpSession session = request.getSession();
		
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);		

		String salida = "desplegar";
		String accionReservar= "crearCotizacion.do";
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		String peticion = request.getParameter(Globals.AYUDA); 
		
		session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
		session.removeAttribute("ec.com.smx.banderaModal");
		session.setAttribute(SE_AGREGO_ARTICULO_OBSOLETO, "");
		//Variable de sesion para mantener varibles de formulario en 2 request (ejm: componente JSF)
		if(session.getAttribute(FORMULARIO_TEMPORAL)!=null){
			formulario = (CotizarReservarForm)session.getAttribute(FORMULARIO_TEMPORAL);
			session.removeAttribute(FORMULARIO_TEMPORAL);
		}
		try {
			//si existe autorizaciones de stock se redirecciona dinamicamente dependiendo del caso
			AutorizacionesUtil.redireccionarAutorizacionesStock(request, formulario, errors, warnings, infos);
			
			if (request.getParameter("cambiarTipoDocumentoContacto") != null) {				
				LogSISPE.getLog().info("Se ha cambiado el tipo de documento del contacto a: {}",formulario.getTipoDocumento());	
				//borrado de los campos del contacto
				formulario.setNombreContacto(null);
				formulario.setNumeroDocumentoContacto(null);
				formulario.setTelefonoContacto(null);
				formulario.setEmailContacto(null);
				formulario.setTipoDocumentoContacto(null);
				
				//borrado de los campos de la empresa
				formulario.setRucEmpresa(null);
				formulario.setNombreEmpresa(null);
				formulario.setTelefonoEmpresa(null);
				
				//borrado de los campos de la persona
				formulario.setNombrePersona(null);
				formulario.setNumeroDocumentoPersona(null);
				formulario.setTelefonoPersona(null);
				formulario.setEmailPersona(null);
				formulario.setTipoDocumentoPersona(null);
				
				//borra de sesion los datos del contacto o empresa
				request.getSession().removeAttribute(ContactoUtil.PERSONA);
				request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);				
			}
			/* cuando se consultan los datos del cliente al dar ENTER sobre la caja de texto del documento
			 * del cliente
			 */
			else if (request.getParameter("consultarCliente") !=null && request.getParameter("cerrarPopUpCorporativo") == null) {
				LogSISPE.getLog().info("Se procede a consultar los datos del cliente, numDocumento: {}", formulario.getNumeroDocumento() );
				formulario.setTipoDocumento("");

					//se realiza la consulta de los datos del cliente
					if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())){
						salida = ContactoUtil.consultarCliente(formulario, request, session, errores, errors, accion, estadoActivo, estadoInactivo, response, infos, warnings, beanSession,salida);
					}else{
						ContactoUtil.buscarPersonaEmpresa(formulario, request, session, response, errors);
					}
				}
			/*
			 * Seleccionar persona-empresa del componente busqueda - cambios oscar
			 */
			else if(request.getParameter("personaEmpresaDesdeComBusqueda") != null) {
				salida = ContactoUtil.perEmpBusqueda(formulario, request, session, errors, response, infos,warnings, beanSession,salida);
			}
			
			/*
			 * Actualizar empresa o persona JSF - cambios oscar
			 */
			else if(request.getParameter("actualizarEmpresaJSF") != null) {
				LogSISPE.getLog().info("actualizarEmpresaJSF");
				salida = "desplegar";
			}
			
			
			//Procesa respuesta de las autorizaciones cuando ya han sido creadas anteriormente
			else if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"))!=null && request.getSession().getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)==null){
				
				//se restaura de sesion los mensajes 
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.warnings") != null){
					warnings = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.warnings");
				}
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.infos") != null){
					infos = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.infos");
				}
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.errors") != null){
					errors = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.errors");
				}
				
				LogSISPE.getLog().info("Tomando accion de una autorizacion {}" , request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")));
				AutorizacionesUtil.solicitarAutorizacion(null, request, formulario, exitos, warnings, infos, errors);
				session.removeAttribute(AutorizacionesUtil.CODIGO_TIPO_AUTORIZACION);
				
				//se eliminan de sesion los respaldos de los mensajes
				if(session.getAttribute("ec.com.smx.sic.sispe.borrar.respaldo.mensajes") != null){
					session.removeAttribute("ec.com.smx.sic.sispe.respaldo.warnings");
					session.removeAttribute("ec.com.smx.sic.sispe.respaldo.infos");
					session.removeAttribute("ec.com.smx.sic.sispe.respaldo.errors");
					session.removeAttribute("ec.com.smx.sic.sispe.borrar.respaldo.mensajes");
				}else{
					session.setAttribute("ec.com.smx.sic.sispe.borrar.respaldo.mensajes", "ok");
				}
			}
			/*-------------------- cuando se cierra el popup de descuentos ----------------------*/
			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("cancelarDescuento")){
				LogSISPE.getLog().info("cerrar popup descuentos");
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				session.removeAttribute(POPUP_DESCUENTO);
				session.removeAttribute(BORRAR_DESCUENTOS);	
				DescuentosUtil.restaurarDetallesAutorizaciones(request, formulario);
				if(session.getAttribute(DescuentosUtil.RESPALDO_DESC_SELECCIONADOS) != null){
					formulario.setOpDescuentos((String[]) session.getAttribute(DescuentosUtil.RESPALDO_DESC_SELECCIONADOS));
					session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, formulario.getOpDescuentos());
				}
				session.removeAttribute(DescuentosUtil.RESPALDO_DESC_SELECCIONADOS);
			}
			/*-------------------- cuando se cancela el popup de la cola de Autorizaciones ----------------------*/
			else if( request.getParameter("cancelar")!=null ){
				
				//se restaura de sesion los mensajes 
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.warnings") != null){
					warnings = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.warnings");
				}
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.infos") != null){
					infos = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.infos");
				}
				if(session.getAttribute("ec.com.smx.sic.sispe.respaldo.errors") != null){
					errors = (ActionMessages) session.getAttribute("ec.com.smx.sic.sispe.respaldo.errors");
				}
				
				//se realiza esta validacion para que solo la primera vez que ingresa muestre todos los popUps
				if(session.getAttribute(CotizarReservarAction.PRIMER_INGRESO_AL_PEDIDO) != null){
					//se muestra el popUp de autorizaciones de descuento variable
					session.setAttribute(AutorizacionesUtil.MOSTRAR_POPUP_AUTORIZACION_STOCK, Boolean.FALSE);
					
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().
							getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
				
					//si existen autorizaciones de descuento variable se muestra el popUp de resumen 
					if(CollectionUtils.isNotEmpty(colaAutorizaciones)){
						
						//se muestra el popUp de autorizaciones de stock solo cuando existen aut. pendientes
						Boolean mostrarPopAutDescVar = Boolean.FALSE;
						for(EstadoPedidoAutorizacionDTO autorizacionActual : colaAutorizaciones){
							if(!autorizacionActual.getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
								mostrarPopAutDescVar = Boolean.TRUE;
								break;
							}
						}	
						
						if(mostrarPopAutDescVar){
							request.getSession().removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
							AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, errors);
							request.getSession().setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS, "OK");
							return mapping.findForward(salida);
						}
					}
				}
				
				LogSISPE.getLog().info("cerrar popup de autorizaciones");
				session.removeAttribute(POPUP_AUTORIZACIONES);
				session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
//				session.setAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK, session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK_RESPALDO));
				Collection<EstadoPedidoAutorizacionDTO> colaAutorizacionesStock = (Collection<EstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK);
				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				AutorizacionesUtil.eliminarAutorizacionesPorCodigoTipoAutorizacion(ConstantesGenerales.TIPO_AUTORIZACION_STOCK, colDetallePedido, colaAutorizacionesStock);
//				session.removeAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK_RESPALDO);
				//si viene de la consolidacion, se setea correctamente la salida y se eliminan de sesion algunas variables
				if(request.getSession().getAttribute("autorizaGerComConsolidacion") != null){
					session.removeAttribute("autorizaGerComConsolidacion");
					salida = "desplegarConsolidarPedido";
				}
				session.removeAttribute(AutorizacionesUtil.CODIGO_TIPO_AUTORIZACION);
				
				//se eliminan de sesion los respaldos de los mensajes
				session.removeAttribute("ec.com.smx.sic.sispe.respaldo.warnings");
				session.removeAttribute("ec.com.smx.sic.sispe.respaldo.infos");
				session.removeAttribute("ec.com.smx.sic.sispe.respaldo.errors");
			}
			/*-------- cuando se pasa de una cotizaci\u00F3n o recotizaci\u00F3n a una reservacion y viseversa ---------*/
			else if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.cambiar.contexto"))!=null || ( session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)!=null && 
					((String)session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)).equals(MessagesWebSISPE.getString("accion.autorizaciones.cambiar.contexto"))) ){
				if(accion!=null){
					
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
					
					//solo cuando el estado es diferente a cotizacion (COT) se valida 
					if(accion != MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion")){
						//se valida que el pedido en session sea el actual en la BDD
						Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO);
						
						//si el pedido no es el actual, no continua el proceso de guardar
						if(!pedidoActual){
							//cuando el pedido ha sido modificado desde otra sesion
							CotizacionReservacionUtil.instanciarVentanaPedidoModificado(request, "crearCotizacion.do");
							return mapping.findForward(salida);
						}
					}
					
					//cuando se ingresa directamente a reservar un cotizaci\u00F3n creada anteriormente
					if(session.getAttribute(CrearReservacionAction.INGRESA_DIRECTAMENTE_RESERVAR)!=null){
						//si la acci\u00F3n actual es una reservaci\u00F3n y se desea pasar a cotizar
						if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))){
							LogSISPE.getLog().info("accion: {}",accion);
							String diasValidez = (String)session.getAttribute(DIAS_VALIDEZ);
							if(diasValidez==null){
								//se obtienen los d\u00EDas de validez de una cotizaci\u00F3n
								ParametroDTO parametroDTO = new ParametroDTO();
								parametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
								diasValidez = SessionManagerSISPE.getServicioClienteServicio().transObtenerDiasValidezCotizacion(parametroDTO);
								LogSISPE.getLog().info("d\u00EDas de validez: {}",diasValidez);
								session.setAttribute(DIAS_VALIDEZ, diasValidez);
							}
							session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"));
							session.setAttribute(ACCION_ANTERIOR, accion);
							session.setAttribute(SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");
							//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la recotizacion
							session.setAttribute(MOSTRAR_AUTORIZACION,
									MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearRecotizacion.estado"));
							session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de cotizaci\u00f3n");
							//se inicializa el valor del campo que realiza la validaci\u00F3n
							formulario.setOpValidarPedido(SessionManagerSISPE.getEstadoActivo(request));
						}else{
							//Se valida si tiene autorizaciones solicitadas
							if(AutorizacionesUtil.solicitarAutorizacion(MessagesWebSISPE.getString("accion.autorizaciones.cambiar.contexto"),request, formulario, exitos, warnings, infos, errors)){
								salida = "desplegar";
								//Cambiar al tab de pedidos
								ContactoUtil.cambiarTabPedidos(beanSession);
								return forward(salida, mapping, request, errors, exitos, warnings, infos, beanSession);
							}
							//se llama al m\u00E9todo que realiza el registro de la cotizaci\u00F3n
							this.guardarCotizacion(accion, request, formulario, errors, exitos, false);
							session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
							session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
							session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
							
							//si no se generaron errores
							if(errors.isEmpty()){
								//se cargan los datos del contacto
								ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
								
								//si la acci\u00F3n actual es una cotizaci\u00F3n y se desea pasar a reservar
								String accionAnterior = (String)session.getAttribute(ACCION_ANTERIOR);
								session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,accionAnterior);
								
								//se inicializa nuevamente la reservaci\u00F3n
								this.inicializarReservacion(formulario, request);
								accionAnterior = "";
//								//se aumenta esta linea para que no se pierdan los checks del popDescuento cuando se pasa de una cotizacion a reservar
								AutorizacionesUtil.verificarAutorizacionesVariables((Collection <DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO), request, formulario);
							}	
						}
					}
					//cuando se crea una cotizaci\u00F3n o recotizaci\u00F3n desde cero
					else{
						
						//Verificacion diferidos
						//if(!WebSISPEUtil.verificarProblemasStock(request, colDetallePedido)){
							if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion")) 
									|| accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))){
								//Se valida si tiene autorizaciones solicitadas
								if(AutorizacionesUtil.solicitarAutorizacion(MessagesWebSISPE.getString("accion.autorizaciones.cambiar.contexto"),request, formulario, exitos, warnings, infos, errors)){
									salida = "desplegar";
									//Cambiar al tab de pedidos
									ContactoUtil.cambiarTabPedidos(beanSession);
									return forward(salida, mapping, request, errors, exitos, warnings, infos, beanSession);
								}
								
								//se llama al m\u00E9todo que realiza el registro de la cotizaci\u00F3n
								this.guardarCotizacion(accion, request, formulario, errors, exitos, false);
								session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
								session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
								session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
								//si no se generaron errores
								if(errors.isEmpty()){
									
									//se cargan los datos del contacto
									ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
									
									//se envian las notificacion PUSH a los dispositivos de los autorizadores
									AutorizacionesUtil.enviarNotificacionAutorizadores(request);
									
									session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
											MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"));
									session.setAttribute(ACCION_ANTERIOR, accion);
									//se inicializa nuevamente la reservaci\u00F3n
									this.inicializarReservacion(formulario, request);
									
									//se aumenta esta linea para que no se pierdan los checks del popDescuento cuando se pasa de una cotizacion a reservar
									AutorizacionesUtil.verificarAutorizacionesVariables((Collection <DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO), request, formulario);
								}

							}else{
								String accionAnterior = (String)session.getAttribute(ACCION_ANTERIOR);
								if(accionAnterior.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"))){
									//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la cotizacion/reservacion
									session.setAttribute(MOSTRAR_AUTORIZACION,
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
								}else{
									//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la recotizacion
									session.setAttribute(MOSTRAR_AUTORIZACION,
											MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearRecotizacion.estado"));
								}
								session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Formulario de cotizaci\u00f3n");
								session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,accionAnterior);
								session.setAttribute(SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");
								//se inicializa el valor del campo que realiza la validaci\u00F3n
								formulario.setOpValidarPedido(SessionManagerSISPE.getEstadoActivo(request));

								accionAnterior = "";
							}
						/*}else{
							LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
							//se crea la ventana que pide la autorizacion
							UtilPopUp popUp = new UtilPopUp();
							popUp.setTituloVentana("Problemas Stock (pavos/pollos/canastas)");
							popUp.setEtiquetaBotonOK("Si");
							popUp.setEtiquetaBotonCANCEL("No");
							
							popUp.setMensajeVentana("Art\u00EDculos(pavos/pollos/canastas) sin existencia stock. Antes de continuar debe ingresar un n\u00FAmero de autorizaci\u00F3n generado por el Gerente Comercial");
							popUp.setValorOK("requestAjax('crearCotizacion.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok&&autorizaGerCom1=ok&&opcionBotton=1',evalScripts: true});ocultarModal();");
							session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
													
							popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarAutorizacion=ok'});ocultarModal();");
							popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarAutorizacion=ok'});ocultarModal();");
							popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
							//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POS
							popUp.setPreguntaVentana("Desea aplicar la autorizacion ingresada?");
							popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
							popUp.setTope(70d);
							popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);

							//se guarda la ventana
							session.setAttribute(SessionManagerSISPE.POPUP, popUp);
						}*/
					}
					//se actualizan los datos del vistaPedidoDTO para que no le tome como pedido modificado en otra sesion
					PedidoDTO pedidoDTO = (PedidoDTO)session.getAttribute(PEDIDO_GENERADO);
					if(pedidoDTO!=null){
						if(vistaPedidoDTO != null){
							vistaPedidoDTO.getId().setSecuencialEstadoPedido(pedidoDTO.getEstadoPedidoDTO().getId().getSecuencialEstadoPedido());
						}else{
								//se crea el vistaPedido
								VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
								//se construye el objeto VistaPedidoDTO para realizar la consulta
								consultaVistaPedidoDTO = new VistaPedidoDTO();
								consultaVistaPedidoDTO.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
								consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoDTO.getId().getCodigoAreaTrabajo());
								consultaVistaPedidoDTO.getId().setCodigoPedido(pedidoDTO.getId().getCodigoPedido());
								consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		
								//primero se obtienen los datos del pedido
								Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
								if(CollectionUtils.isNotEmpty(colVistaPedidoDTO)){
									vistaPedidoDTO = colVistaPedidoDTO.iterator().next();
									vistaPedidoDTO.getId().setSecuencialEstadoPedido(pedidoDTO.getEstadoPedidoDTO().getId().getSecuencialEstadoPedido());
								}
						}
					}
					session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
				}
				//para cambiar al tab de pedidos
				 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
					 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);			
				 }
			}
			/*----------------------- si se escogi\u00F3 un tipo de art\u00EDculo especial --------------------------*/
			else if(request.getParameter("opEspeciales")!=null){
				ArrayList tipoEspeciales = (ArrayList)session.getAttribute(COL_TIPO_ESPECIALES);
				LogSISPE.getLog().info("indice especiales: {}",formulario.getOpTipoEspeciales());
				String indiceEspeciales = formulario.getOpTipoEspeciales();
				EspecialDTO especialDTO = (EspecialDTO)tipoEspeciales.get(Integer.parseInt(indiceEspeciales));
				LogSISPE.getLog().info("tama\u00F1o coleccion: {}",especialDTO.getArticulos().size());
				session.setAttribute(COL_ART_ESPECIALES,especialDTO.getArticulos());
				formulario.setOpEspecialesSeleccionados(null);
			}
			/*----------------------- Muestra el pop up de Descuentos --------------------------*/
			else if(request.getParameter("popupDescuento")!=null){
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				
				//se obtiene de la sesion los datos del detalle y de la lista de articulos.
				Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				LogSISPE.getLog().info("Mostrar pop up descuentos");
				session.setAttribute(POPUP_DESCUENTO, "ok");
				session.setAttribute(CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
//				AutorizacionesUtil.verificarDepartamentosPedido(request, errors, warnings, Boolean.TRUE);
//				AutorizacionesUtil.buscarAutorizadoresDepartamentos(request, warnings);
				
				if(!CollectionUtils.isEmpty(detallePedido)){
					LogSISPE.getLog().info("Mostrar pop up descuentos");
					session.setAttribute(POPUP_DESCUENTO, "ok");
					session.setAttribute(CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
					AutorizacionesUtil.verificarDepartamentosPedido(request, errors, warnings, Boolean.TRUE);
//					AutorizacionesUtil.buscarAutorizadoresDepartamentos(request, warnings);
					
					String [] descSeleccionados = (String []) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);		
					Double[][] porcentajeVarDescuento = (Double[][]) request.getSession().getAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE);
					
					if (descSeleccionados != null){	
						session.removeAttribute("ec.com.smx.sic.sispe.sinDescuentosCajasMayorista");
						descSeleccionados = CotizacionReservacionUtil.corregirOpDescuentos(request, descSeleccionados);
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, descSeleccionados);
						formulario.setOpDescuentos(descSeleccionados);
						session.setAttribute(DescuentosUtil.RESPALDO_DESC_SELECCIONADOS, descSeleccionados);
					}
					
					if(porcentajeVarDescuento != null ){
						formulario.setPorcentajeVarDescuento(porcentajeVarDescuento);
					}
					
//					CotizacionReservacionUtil.getActivarDesactivarPorcentajeDsctoVariables(request, formulario);
					
					session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
					session.setAttribute(BORRAR_DESCUENTOS, "SI");
				}else{
					errors.add("errorSinDetalles",new ActionMessage("errors.gerneral","El pedido no tiene detalles, por lo que no se puede aplicar descuentos"));
					//se quita la variable de session para que no muestre el popup de descuentos
					session.removeAttribute(POPUP_DESCUENTO);
				}
			}
			/*----------------------- si se desea agregar un art\u00EDculo al detalle --------------------------*/
			else if(request.getParameter("agregarArticulo")!=null )
			{
				//variable para saber si proviene del boton actualizar.
				session.removeAttribute(CAMBIO_PESO);
				//se obtiene de la sesion los datos del detalle y de la lista de articulos.
				ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				//se recupera de sesion el pedido consolidado
				Collection<DetallePedidoDTO> detallePedidoConsolidado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				
				//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos
				ArrayList<String> codigosArticulos = (ArrayList<String>)session.getAttribute(COL_CODIGOS_ARTICULOS);
				//se obtiene el subTotal que aplica IVA
				Double subTotalAplicaIVA = (Double)session.getAttribute(SUB_TOTAL_APLICA_IVA);
				//se obtiene el subTotal que no aplica IVA
				Double subTotalNoAplicaIVA = (Double)session.getAttribute(SUB_TOTAL_NOAPLICA_IVA);
				//variables que almacenar\u00E1n los nuevos subtotales del pedido
				double nuevoSubTotalAplicaIVA = subTotalAplicaIVA.doubleValue();
				double nuevoSubTotalNoAplicaIVA = subTotalNoAplicaIVA.doubleValue();
				//variable bandera que indica si se agregaron art\u00EDculos
				boolean seAgregaronArticulos = false;
				String articulosDeBaja = "";
				StringBuffer articulosObsoletos = null;
				//UtilPopUp popUp = null;
										
				//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
				String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
				session.setAttribute(CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);				
				
				if(formulario.getOpEspecialesSeleccionados()==null || request.getParameter("ingresoManual")!=null){
					String codigoArticulo = formulario.getCodigoArticulo().trim();
					try{
						//se eliminan los ceros al inicio del c\u00F3digo de barras del articulo
						codigoArticulo = Long.valueOf(formulario.getCodigoArticulo().trim()).toString();
					}catch (NumberFormatException e) {LogSISPE.getLog().error("error de aplicaci\u00F3n",e);}
					
					LogSISPE.getLog().info("LOCAL RESPONSABLE: {}",SessionManagerSISPE.getCodigoLocalObjetivo(request));
					//llamada al m\u00E9todo que construye la consulta
					ArticuloDTO consultaArticuloDTO = new ArticuloDTO();					
					//consultaArticuloDTO.getId().setCodigoArticulo(codigoArticulo);
					consultaArticuloDTO.setNpCodigoBarras(codigoArticulo);
										
					WebSISPEUtil.construirConsultaArticulos(request, consultaArticuloDTO, estadoInactivo, estadoInactivo, accion);
					
					//no consultar articulos precodificados
					consultaArticuloDTO.setCodigoEstado(SICArticuloConstantes.getInstancia().ESTADOARTICULO_CODIFICADO);

					try{
						//llamada al m\u00E9todo de la capa de servicio que devuelve el art\u00EDculo solicitado
						ArticuloDTO articuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(consultaArticuloDTO);
						if(articuloDTO != null) //solo si se obtubo un art\u00EDculo
						{
							codigoArticulo = articuloDTO.getId().getCodigoArticulo();
							//verificar si el stock no es null y el articulo es de tipo pavo
							if(articuloDTO.getNpStockArticulo() == null && CotizacionReservacionUtil.esPavo(articuloDTO.getCodigoClasificacion(), request)){
								errors.add("SinTemporada",new ActionMessage("erros.codigoBarrasSinTemporada"));
							}else{
							//verificar si el articulo se debe aplicar descuento automatico o no
							formulario.setOpDescuentos(CotizacionReservacionUtil.iniciarDescuentoPavos(request, articuloDTO));
							//SE CONFIRMA PRIMERO SI EL ARTICULO YA ESTA EN EL LISTADO
							if(codigosArticulos.contains(codigoArticulo) &&
//									!articuloDTO.getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) &&
//									!articuloDTO.getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
								!articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
								!articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
								//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
								errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetido",articuloDTO.getDescripcionArticulo()));
								LogSISPE.getLog().info("error: ya esta en la lista");
							}else if(articuloDTO.getNpEstadoArticuloSIC()!=null && 
									articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
								//cuando el art\u00EDculo esta de baja en el SIC
								errors.add("articuloDeBaja",new ActionMessage("errors.agregarArticuloDeBaja", codigoArticulo));
							}else{
								if(articuloDTO.getNpStockArticulo()!=null
										&& articuloDTO.getNpStockArticulo().longValue() < Long.parseLong(formulario.getCantidadArticulo().trim())){
									//variable para controlar el error
									request.setAttribute("ec.com.smx.sic.sispe.articulo.problemasStock","ok");
								}else if(articuloDTO.getNpAlcance()!=null && articuloDTO.getNpAlcance().equals(estadoInactivo)){
									//variable para controlar el error
									request.setAttribute("ec.com.smx.sic.sispe.articulo.problemasAlcance","ok");
								}
								//se guarda el art\u00EDculo para el manejo de errores de stock o alcance
								request.setAttribute("ec.com.smx.sic.sispe.articuloDTO",articuloDTO);
								
								//llamada al m\u00E9todo que construye el detalle del pedido
								DetallePedidoDTO detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(new Long(formulario.getCantidadArticulo().trim()),
										detallePedido.size(), articuloDTO, request,errors);

								if(detallePedidoDTO != null && !CotizacionReservacionUtil.canastoConRecetaSinTemporadaActiva(detallePedidoDTO.getArticuloDTO(), infos)){
									//se a\u00F1ade el detalle a la colecci\u00F3n
									detallePedido.add(detallePedidoDTO);
									indice =  ConstantesGenerales.ESTADO_CODIGO_BARRAS;
									codigoArticuloObsoleto = detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo();
									LogSISPE.getLog().info("SE ANADIO A LA LISTA");
									
									//aniadir al pedido consolididao el detalle ingresado
									if(detallePedidoConsolidado != null){
										detallePedidoConsolidado.add(detallePedidoDTO);
									}
									//se actualiza la lista de codigos de art\u00EDculos
									codigosArticulos.add(codigoArticulo);
									
									//si el art\u00EDculo esta considerado como obsoleto en el SIC
									if(articuloDTO.getClaseArticulo() != null && articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) &&
											articuloDTO.getNpStockArticulo() != null && articuloDTO.getNpStockArticulo() < Long.parseLong(formulario.getCantidadArticulo().trim())){
										//a\u00F1ade los codigos de los art\u00EDculos encontrados 
										if(articulosObsoletos == null){
											articulosObsoletos = new StringBuffer();
										}
										articulosObsoletos.append(articuloDTO.getId().getCodigoArticulo().concat(", "));
									}
									//QUITAR ESTAS 2 LINEAS 
									//articulosObsoletos = new StringBuffer();
									//articulosObsoletos.append("CODIGOARTICULO NO.");
								
									
									//verifica si encontr\u00F3 alg\u00FAn art\u00EDculo obsoleto para mostrar el popup de advertencia
									if(articulosObsoletos != null){
										LogSISPE.getLog().info("**Encontro art\u00EDculos obsoletos.....");
										instanciarVentanaArticuloObsoleto(request,"");																				
									}
									
									//se blanquean los campos de ingreso
									formulario.setCodigoArticulo(null);
									formulario.setCantidadArticulo(null);
									//se agregaron art\u00EDculos
									seAgregaronArticulos = true;
									
									//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
									formulario.setOpDescuentos(DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE));
									
								}else if(infos.isEmpty()){
									errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo, es probable que existan problemas con la informaci\u00F3n registrada"));
								}
								
							}
							session.setAttribute(CotizacionReservacionUtil.MODIFICAR_PESO_INACTIVO, null);
						}
						}else{
							infos.add("articulosVacio",new ActionMessage("message.codigoBarras.invalido"));
						}
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
						errors.add("errorSIC", new ActionMessage("errors.agregarArticuloSIC"));
					}
				}else //cuando se desea agregar un grupo de art\u00EDculos
				{
					//se obtine la colecci\u00F3n de indices seleccionados
					String especialesSeleccionados[] = formulario.getOpEspecialesSeleccionados();										
					
					String cantidadArticuloEspecial[] = formulario.getVectorCantidadEspecial();
					
					int indiceTipoEspecial = 0; //indice para la colecci\u00F3n principal de especiales
					int indiceArticuloEspecial = 0; //indice para recorrer la colecci\u00F3n de art\u00EDculos especiales
					int indiceGlobal = 0; //indice global para los controles de la secci\u00F3n
					//ArrayList listaEspeciales = (ArrayList)session.getAttribute(COL_ESPECIALES);
					ArrayList<EspecialDTO> tipoEspeciales = (ArrayList<EspecialDTO>)session.getAttribute(COL_TIPO_ESPECIALES);

					List<ArticuloDTO> articulosSeleccionados = new ArrayList<ArticuloDTO>();
					LogSISPE.getLog().info("tamano seleccionados: {}",especialesSeleccionados.length);
					Long [] cantidades = new Long [especialesSeleccionados.length];
					
					//se itera el arreglo de indices
					for(int i=0;i<especialesSeleccionados.length;i++){
						String indices[] = especialesSeleccionados[i].split(SEPARADOR_TOKEN);
						//se obtienen los indices correctos
						indiceTipoEspecial = Integer.parseInt(indices[0]);
						indiceArticuloEspecial = Integer.parseInt(indices[1]);
						indiceGlobal = Integer.parseInt(indices[2]);

						//se obtiene el objeto EspecialDTO
						EspecialDTO especialDTO = (EspecialDTO)tipoEspeciales.get(indiceTipoEspecial);
						//se obtiene la colecci\u00F3n de art\u00EDculos dentro del tipo de especial seleccionado anteriormente
						ArrayList<ArticuloDTO> articulosEspeciales = (ArrayList<ArticuloDTO>)especialDTO.getArticulos();
						ArticuloDTO articuloEspecial = (ArticuloDTO)articulosEspeciales.get(indiceArticuloEspecial);
						WebSISPEUtil.construirConsultaArticulos(request,articuloEspecial,estadoInactivo, estadoInactivo, accion);
						articulosSeleccionados.add(articuloEspecial);
						try{
							LogSISPE.getLog().info("cantidad [{}]= {}",indiceGlobal,cantidadArticuloEspecial[indiceGlobal]);
							cantidades[i]=new Long(cantidadArticuloEspecial[indiceGlobal]);
						}catch(Exception ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							cantidades[i]=new Long(1);
						}
					}

					try{
						//llamada al m\u00E9todo que obtiene el stock, existencia y alcance de los art\u00EDculos
						SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosSeleccionados);
												
						for(int i=0;i<articulosSeleccionados.size();i++)
						{
							ArticuloDTO articuloDTO = articulosSeleccionados.get(i);
							//verificar si el articulo se debe aplicar descuento automatico o no
							formulario.setOpDescuentos(CotizacionReservacionUtil.iniciarDescuentoPavos(request,articuloDTO));
							
							DetallePedidoDTO detallePedidoDTO = null;
							
							//SE CONFIRMA PRIMERO SI EL ARTICULO YA ESTA EN EL LISTADO
							if(!codigosArticulos.contains(articuloDTO.getId().getCodigoArticulo()) ||
							//if(!codigosArticulos.contains(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()) ||
								articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
								articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
								//se verifica el estado del art\u00EDculo
								if(articuloDTO.getNpEstadoArticuloSIC()==null ||
										!articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
									
									//llamada al m\u00E9todo que construye el detalle del pedido
									
									try {
										detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(cantidades[i], detallePedido.size(), articuloDTO,request,errors);
										if(detallePedidoDTO!= null && !CotizacionReservacionUtil.canastoConRecetaSinTemporadaActiva(detallePedidoDTO.getArticuloDTO(), errors)){
											//se a\u00F1ade el detalle a la colecci\u00F3n
											detallePedido.add(detallePedidoDTO);
											
											//aniadir al pedido consolididao el detalle ingresado
											if(detallePedidoConsolidado != null){
												detallePedidoConsolidado.add(detallePedidoDTO);
											}
											
											indice =  ConstantesGenerales.ESTADO_ESPECIALES;
											//se actualiza la lista de codigos de art\u00EDculos
											codigosArticulos.add(articuloDTO.getId().getCodigoArticulo());
											//codigosArticulos.add(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras());
											LogSISPE.getLog().info("SE ANADIO A LA LISTA");
			
											//se agregaron art\u00EDculos
											seAgregaronArticulos = true;
											
											//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
											formulario.setOpDescuentos(DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE));
										}
										else if(errors.isEmpty()){
											LogSISPE.getLog().info("Error al costruir los detalles del articulo");
											errors.add("articulosErrorData", new ActionMessage("errors.articuloError", articuloDTO.getDescripcionArticulo()));
										}
									} catch (Exception ex) {
										LogSISPE.getLog().info("Error al costruir los detalles del articulo -> {}",ex);
										errors.add("articulosErrorData", new ActionMessage("errors.articuloError", articuloDTO.getDescripcionArticulo()));
										throw new SISPEException();
									}
								}else{
									articulosDeBaja = articulosDeBaja.concat(articuloDTO.getId().getCodigoArticulo()).concat(", ");
								}
								//si el art\u00EDculo esta considerado como obsoleto en el SIC
								LogSISPE.getLog().info("::::: * clase del articulo -> {}", articuloDTO.getClaseArticulo());
								if(articuloDTO.getClaseArticulo() != null && articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) &&
										articuloDTO.getNpStockArticulo() != null && articuloDTO.getNpStockArticulo() < cantidades[i]){
									//a\u00F1ade los codigos de los art\u00EDculos encontrados 
									if(articulosObsoletos == null){
										articulosObsoletos = new StringBuffer();
									}
									articulosObsoletos.append(articuloDTO.getId().getCodigoArticulo().concat(", "));
								}
							}else{
								if(codigosArticulos.contains(articuloDTO.getId().getCodigoArticulo()) &&
									!articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
									!articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
									//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
									errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetido",articuloDTO.getDescripcionArticulo()));
									LogSISPE.getLog().info("error: ya esta en la lista");
								}
							}
						}
						
						//verifica si encontr\u00F3 alg\u00FAn art\u00EDculo obsoleto para mostrar el popup de advertencia
						if(articulosObsoletos != null){
							instanciarVentanaArticuloObsoleto(request,"");
						}
						
						//se agrega un mensaje de error
						if(!articulosDeBaja.equals("")){
							errors.add("articulosDeBaja", new ActionMessage("errors.agregarGrupoArticulosDeBaja", articulosDeBaja));
						}
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						if(errors.isEmpty()){
							errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
							errors.add("errorSIC", new ActionMessage("errors.agregarGrupoArticulosSIC"));
						}
					}					
					
					session.setAttribute(CotizacionReservacionUtil.MODIFICAR_PESO_INACTIVO, null);
				}

				//SE ACTUALIZAN LOS TOTALES EN SESION
				session.setAttribute(SUB_TOTAL_APLICA_IVA, Double.valueOf(nuevoSubTotalAplicaIVA));
				session.setAttribute(SUB_TOTAL_NOAPLICA_IVA, Double.valueOf(nuevoSubTotalNoAplicaIVA));

				//solo si fueron agregados art\u00EDculos al detalle
				if(seAgregaronArticulos){
					
					//se actualiza los detalles consolidados
					ConsolidarAction.crearDetalleConsolidadosNoRepetidos(request, detallePedidoConsolidado);
					
					//se actualizan los descuentos
					formulario.actualizarDescuentos(request, warnings);
					//llamada al m\u00E9todo que actualiza los totales
					//formulario.actualizarTotales(request);

					//se comenta esta linea porque este metodo se llama cuando se actualizan los descuentos
					//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
//					ActionMessage message = CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
//					if(message != null){
//						warnings.add("errorCalculo", message);
//					}
				}

				//se actualizan las variables de sesi\u00F3n
				session.setAttribute(COL_CODIGOS_ARTICULOS, codigosArticulos);
				session.setAttribute(DETALLE_PEDIDO, detallePedido);
				session.setAttribute(PEDIDO_EN_PROCESO, "ok");

				formulario.setChecksSeleccionar(null);
				//formulario.setChecksSeleccionar(new String[]{"0","1"});
				formulario.setCheckSeleccionarTodo(null);
				formulario.setOpEspecialesSeleccionados(null);
				//formulario.setOpTipoEspeciales("0"); //se muestra la primera secci\u00F3n de especiales

				//Cambiar al tab de pedidos
				ContactoUtil.cambiarTabPedidos(beanSession);
		}
			/*------------ [NO ES DESDE UN LOCAL] cuando se desea cambiar el local destino del pedido -------------*/
			else if(request.getParameter("cambiarLocal")!=null){
				LogSISPE.getLog().info("codEstablecimiento: {}" ,request.getParameter("codEstablecimiento"));
				if(formulario.getIndiceLocalResponsable().equals("") || formulario.getIndiceLocalResponsable().equals("ciudad")){
					//condici\u00F3n para manejar el error
					errors.add("localResponsable",new ActionMessage("errors.cambio.local"));
					LogSISPE.getLog().info("local anterior: {}"+session.getAttribute(LOCAL_ANTERIOR));
					formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
				}else{
					//esta condici\u00F3n se cumple solamente cuando se ingresa por primera vez a la cotizaci\u00F3n
					if(session.getAttribute(INGRESO_PRIMERA_VEZ_BODEGA)!=null){
						//se guarda toda la descripci\u00F3n
						session.setAttribute(LOCAL_ANTERIOR, formulario.getIndiceLocalResponsable());
						//la primera vez que se ingresa al formulario
						//se obtienen los datos del local
						VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
						//se obtiene el c\u00F3digo del local de la posici\u00F3n 0
						session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaLocalDTO.getId().getCodigoLocal());
						//se obtiene el c\u00F3digo del establecimiento de la posici\u00F3n 2
						session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaLocalDTO.getCodigoEstablecimiento());

						//se llama al m\u00E9todo que verifica la posibilidad que tiene el establecimiento
						//para cambiar el precio
						CotizacionReservacionUtil.verificarFormatoNegocioParaCambioPrecios(vistaLocalDTO.getCodigoEstablecimiento(), request);

						session.removeAttribute(INGRESO_PRIMERA_VEZ_BODEGA);
						//-------- se cargan todos los especiales en base al c\u00F3digo escogido -----------

						//creaci\u00F3n del DTO para los especiales
						EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
						especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						especialDTO.setNpCodigoLocal(SessionManagerSISPE.getCodigoLocalObjetivo(request));
						especialDTO.setEstadoEspecial(estadoActivo);
						especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
						especialDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
						
						//especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
						ArrayList<EspecialDTO> especiales = (ArrayList<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloEspecial(especialDTO);
						eliminarDetallesConProblemas(especiales);
						session.setAttribute(COL_TIPO_ESPECIALES,especiales);
					}else{
						//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
						WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.cambiolocal", 
								"\u00BFDesea cambiar el local?", "siCambiarLocal", "noCambiarLocal", request);
					}
				}
			}
			/*------------ [NO ES DESDE UN LOCAL] cuando se acepta la confirmaci\u00F3n del cambio de local -------------*/
			else if(formulario.getBotonSi()!=null && formulario.getBotonSi().equals("siCambiarLocal")){
				//se respaldan las colecciones de datos que est\u00E1n en sesi\u00F3n antes de inicializar el formulario
				String diasValidez = (String)session.getAttribute(DIAS_VALIDEZ);
				Collection tiposDescuento = (Collection)session.getAttribute(COL_TIPO_DESCUENTO);
				String excluyentes = (String)session.getAttribute(TIPO_DES_EXCLUYENTES);
				String otros = (String)session.getAttribute(TIPO_DES_OTROS);
				String variables = (String)session.getAttribute(TIPO_DES_VARIABLES);
				Collection motivosDescuento = (Collection)session.getAttribute(COL_MOTIVO_DESCUENTO);
				String indiceLocalResponsable = formulario.getIndiceLocalResponsable();
				Integer localObjetivo = SessionManagerSISPE.getCodigoLocalObjetivo(request);
				String codFormatosNegocioCambioPrecios = (String)session.getAttribute(COD_FORMATOS_NEGOCIO_CAMBIO_PRECIOS);  
				
				//se inicializa el formulario
				inicializarCotizacionSinLlamarAlServicio(formulario, request);

				//-------- se cargan todos los especiales en base al c\u00F3digo escogido -----------
				//creaci\u00F3n del DTO para el art\u00EDculo
				EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
				especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				especialDTO.setNpCodigoLocal(localObjetivo);
				especialDTO.setEstadoEspecial(estadoActivo);
				especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				especialDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
				
				//especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				ArrayList<EspecialDTO> especiales = (ArrayList<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloEspecial(especialDTO);
				eliminarDetallesConProblemas(especiales);
				session.setAttribute(COL_TIPO_ESPECIALES,especiales);
				//se reasignan los valores
				session.setAttribute(DIAS_VALIDEZ,diasValidez);
				session.setAttribute(COL_TIPO_DESCUENTO,tiposDescuento);
				session.setAttribute(TIPO_DES_EXCLUYENTES, excluyentes);
				session.setAttribute(TIPO_DES_OTROS, otros);
				session.setAttribute(TIPO_DES_VARIABLES, variables);
				session.setAttribute(COL_MOTIVO_DESCUENTO,motivosDescuento);
				//esta variable indica que el proceso no se est\u00E1 realizando desde un local
				session.setAttribute(ES_ENTIDAD_BODEGA, "ok");
				formulario.setIndiceLocalResponsable(indiceLocalResponsable);
				//se guarda toda la descripci\u00F3n
				session.setAttribute(LOCAL_ANTERIOR, indiceLocalResponsable);
				//se obtienen los datos del local
				VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
				//se obtiene el c\u00F3digo del local de la posici\u00F3n 0
				session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaLocalDTO.getId().getCodigoLocal());
				//se obtiene el c\u00F3digo del establecimiento de la posici\u00F3n 2
				session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaLocalDTO.getCodigoEstablecimiento());

				//se almacena el tel\u00E9fono del local responsable y el nombre del administrador
				//estos valores se usan cuando el pedido se realiza desde la bodega
				session.setAttribute(TELEFONO_LOCAL, vistaLocalDTO.getTelefonoLocal());
				session.setAttribute(ADMINISTRADOR_LOCAL, vistaLocalDTO.getAdministradorLocal());

				//se reasigna el listado de codigos
				session.setAttribute(COD_FORMATOS_NEGOCIO_CAMBIO_PRECIOS, codFormatosNegocioCambioPrecios);
				//se llama al m\u00E9todo que verifica la posibilidad que tiene el establecimiento
				//para cambiar el precio
				CotizacionReservacionUtil.verificarFormatoNegocioParaCambioPrecios(vistaLocalDTO.getCodigoEstablecimiento(), request);

				//Se contruye los tabs de Contacto y Pedidos
				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request,formulario);
				beanSession.setPaginaTab(tabsCotizaciones);
			}
			//----------------- cuando se desea volver a la secci\u00F3n de b\u00FAsqueda
			else if(request.getParameter("volverBuscar")!=null){
				//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.regresarABusqueda", 
						"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
			}
			//--------- cuando se responde SI para volver a la pantalla de b\u00FAsqueda de cotizaciones y recotizaciones
			else if(formulario.getBotonSi()!=null && formulario.getBotonSi().equals("siVolverBusqueda") || request.getParameter("siVolverBusqueda") != null){
				session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA, "ok");
				session.removeAttribute(PEDIDO_EN_PROCESO);
				//se activa el men\u00FA principal
				MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
				
				//regresar los valores anteriores si consolido o no 
		      	//Collection<VistaPedidoDTO> visPedColAux=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarColAux");
		      	//LogSISPE.getLog().info("regresar los valores anteriores {}",visPedColAux==null?visPedColAux:visPedColAux.size());
		      	//request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol",visPedColAux);
		      	//formulario.setDatosConsolidados(visPedColAux);
		      	//eliminar variable de session de pedidos consolidados eliminados
		      	session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
		      	session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
		      	session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
		      	session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
		      	session.setAttribute("ec.com.smx.sic.sispe.accion",(String)session.getAttribute(ACCION_ANTERIOR));
		      	session.removeAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
		      	session.removeAttribute(CotizacionReservacionUtil.DESC_NAVEMP_CREDITO_SELECCIONADO);
				
		      	formulario.setVectorPesoActual(null);
		      	
				//seg\u00FAn la acci\u00F3n actual se mustra la pantalla de b\u00FAsqueda adecuada
				if(accion!=null && accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){					
					salida = "listaModificarReservacion";
				}else if(accion!=null && accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
					salida = "desplegarConsolidarPedido";
					}
				else{
					session.removeAttribute("ec.com.smx.sic.sispe.accion.consolidar");
					salida = "listaCotizadosRecotizados";
				}
				session.removeAttribute(SessionManagerSISPE.POPUP);
				ContactoUtil.eliminarDataManagersSessionCorp(request);
			}
			/*-------------- cuando se cancela una confirmaci\u00F3n ---------------*/
			else if(formulario.getBotonNo()!=null){
				if(formulario.getBotonNo().equals("noCambiarLocal"))
					//se restablece el c\u00F3digo ingresado anteriormente, cuando se cancela el cambio de local desde bodega
					formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
				salida = "desplegar";				
			}
			/*-------- cuando se presiona el boton Actualizar o cuando se acepta recalcular los valores
			 * del detalle con los precios actuales (Ventana de Confirmaci\u00F3n) -------------*/
			else if(request.getParameter("actualizarDetalle")!=null || request.getParameter("confirmarStockObsoleto")!=null || request.getParameter("cancelarStockObsoleto")!=null){
				//JM
				try{
					ActionMessages warningsTemp =  (ActionMessages) session.getAttribute(CotizarReservarForm.WARNINGS_TEMPORAL);
					if(warningsTemp != null && warningsTemp.size() > 0){
						warnings = warningsTemp;
						session.removeAttribute(CotizarReservarForm.WARNINGS_TEMPORAL);
					}
						
					//Parametro para cerrar el popup de articulos de pedidos anteriores mientras actualiza el detalle del pedido
					if(request.getParameter("pedidosAnteriores")!=null&&request.getParameter("accionesPedAnt")!=null){   
						LogSISPE.getLog().info("Busqueda de Pedidos Anteriores");
						CotizarPedidosAnterioresUtil.accionesPedAnt(request,formulario,errors,exitos, warnings,infos);	
						session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO);
					}
					
					//si selecciona la opci\u00F3n calcelar del popUp de art\u00EDculos obsoletos
						if (request.getParameter("cancelarStockObsoleto")!=null ){
							eliminarArticulosObsoletos(request);
							request.setAttribute("cancelarStockObsoleto", null);
							session.setAttribute(SessionManagerSISPE.POPUP, null);
							if(formulario.getCodigoArticuloObsoleto()!=""){
							errors.add("cancelarStockObsoleto",new ActionMessage("errors.stock.articulo.obsoleto.cancelar", formulario.getCodigoArticuloObsoleto()));
							}
							formulario.setCodigoArticuloObsoleto("");
						}
						
						//si elige la opci\u00F3n confirmar del popUp de los art\u00EDculos obsoletos
						if(request.getParameter("confirmarStockObsoleto")!=null){	
							request.setAttribute("confirmarStockObsoleto", null);
							session.setAttribute(SessionManagerSISPE.POPUP, null);
						}
						
						session.setAttribute(CAMBIO_PESO,"ok");						
						String [] descSeleccionados = (String []) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
						if (descSeleccionados != null){
							formulario.setOpDescuentos(descSeleccionados);
						}
						
//						se salvan los descuentos actuales cuando hay cambio precios
						if(request.getParameter("intercambioPrecios") != null){
							Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
							if(CollectionUtils.isNotEmpty(descuentos)){
								CotizacionReservacionUtil.generarOpDescuentos(request, descuentos);
							}						
						}
						//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
						formulario.setOpDescuentos(DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE));
						
						CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
						
						//validacion para cerrar el check actualizar precios y recetas solo cuando se seleccione esta opcion
						if ( null != request.getParameter("intercambioPrecios")) {
							session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
							
//							//se procesan los descuentos y Autorizaciones
//							List detallePedido = (List)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//							VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
//							CotizacionReservacionUtil.procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
							AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, warnings);	
						}
						
						List<DetallePedidoDTO> colDetalle = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);						
						List<DetallePedidoDTO> colDetallePedido =new  ArrayList<DetallePedidoDTO>();
						for(DetallePedidoDTO detalle : colDetalle){
							if(detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()!=null && detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){								
								detalle.getArticuloDTO().setNpStockArticulo(detalle.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
							}
							colDetallePedido.add(detalle);
						}					
						session.setAttribute(DETALLE_PEDIDO, colDetallePedido);
										
						//valida el ingreso de valor 0 o null
						Boolean validar = false;
						for(DetallePedidoDTO detalle : colDetalle){
							if(detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
								if((detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()==0 || detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()== null)
										|| (detalle.getEstadoDetallePedidoDTO().getNpCantidadEstado() > detalle.getEstadoDetallePedidoDTO().getStoLocArtObs())){
									validar = true;
									break;
								}
							}
						}	
						
						if(validar == true){
							warnings.clear();
							errors.clear();
							CotizarReservarAction.instanciarVentanaArticuloObsoleto(request,"");
						}else{
							formulario.setNuevoCodigoBarrasOk(false);
							formulario.setNuevoEspecialOk(false);	
							formulario.setRespaldo(0);
							formulario.setIndiceDetalle(ConstantesGenerales.ESTADO_SIN_SELECCION);
						}
						
						if (request.getParameter("cancelarStockObsoleto")!=null ){
							request.setAttribute("cancelarStockObsoleto", null);
							session.setAttribute(SessionManagerSISPE.POPUP, null);
							formulario.setNuevoCodigoBarrasOk(false);
							formulario.setNuevoEspecialOk(false);
							formulario.setIndiceDetalle(ConstantesGenerales.ESTADO_SIN_SELECCION);
						}
						
						//para verificar si la opci\u00F3n es modificar reserva
						Boolean despacho = validarFechaDespachoEnDetallePedido(session, colDetalle);
						//si existen despachos y si la opci\u00F3n es modificar reserva, se muestra el pupup
						if(despacho && session.getAttribute(ModificarReservacionAction.MOSTRAR_POPUP_DESPACHO)!=null
								&& session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){			
							CotizacionReservacionUtil.instanciarVentanaEntregaDespachada(request);
						}	
				}
				catch(Exception ex){
					//si cae en error por no encontrar c\u00F3dogo de barras de algun art\u00EDculo
				//	session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
					List<DetallePedidoDTO> colDetalle = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);						
					List<DetallePedidoDTO> colDetallePedido =new  ArrayList<DetallePedidoDTO>();
					for(DetallePedidoDTO detalle : colDetalle){
						if(detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()!=null && detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){								
							detalle.getArticuloDTO().setNpStockArticulo(detalle.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
						}
						colDetallePedido.add(detalle);
					}	
					
					Boolean validar = false;
					for(DetallePedidoDTO detalle : colDetalle){
						if(detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
							if((detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()== null||detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()==0)
									|| (detalle.getEstadoDetallePedidoDTO().getNpCantidadEstado() > detalle.getEstadoDetallePedidoDTO().getStoLocArtObs())){
								validar = true;
								break;
							}
						}
					}
					
					if(validar == true){
						warnings.clear();
						errors.clear();
						CotizarReservarAction.instanciarVentanaArticuloObsoleto(request,"");
					}else{
						formulario.setNuevoCodigoBarrasOk(false);
						formulario.setNuevoEspecialOk(false);
						formulario.setRespaldo(0);
						formulario.setIndiceDetalle(ConstantesGenerales.ESTADO_SIN_SELECCION);
					}
					
					if (request.getParameter("cancelarStockObsoleto")!=null ){
						request.setAttribute("cancelarStockObsoleto", null);
						session.setAttribute(SessionManagerSISPE.POPUP, null);
						formulario.setNuevoCodigoBarrasOk(false);
						formulario.setNuevoEspecialOk(false);
						formulario.setIndiceDetalle(ConstantesGenerales.ESTADO_SIN_SELECCION);
					}
					
					//para verificar si la opci\u00F3n es modificar reserva
					Boolean despacho = validarFechaDespachoEnDetallePedido(session, colDetalle);
					//si existen despachos y si la opci\u00F3n es modificar reserva, se muestra el pupup
					if(despacho && session.getAttribute(ModificarReservacionAction.MOSTRAR_POPUP_DESPACHO)!=null
							&& session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){			
						CotizacionReservacionUtil.instanciarVentanaEntregaDespachada(request);
					}
				}
				if(request.getParameter("intercambioPrecios") != null && EntregaLocalCalendarioUtil.verificarEntregasProximoDespacho(request)){
					LogSISPE.getLog().info("mostrar popUp editar entregas por entregas proximas a despachar");
					CrearReservacionAction.instanciarPopUpDespachosProximos(request);
				}
				
				//IOnofre. Cierra el PopUp de buscar articulos
				if(request.getParameter("buscador") !=null){
					LogSISPE.getLog().info("Cierra el PopUp de busqueda de articulos");
					session.removeAttribute(BuscarArticuloUtil.POPUP_BUSQUEDA_ARTICULOS);
					session.removeAttribute("ec.com.smx.sic.sispe.catalogoArticulos.articulos");
				}
				
				//IOnofre. Cierra el PopUp de buscar articulos por estructura comercial
				if(request.getParameter("buscarPorEstructura") !=null){
					LogSISPE.getLog().info("Cierra el PopUp de busqueda de articulos por estructura comercial");
					session.removeAttribute(BuscarArticuloUtil.POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL);
					session.removeAttribute("ec.com.smx.sic.sispe.catalogoArticulos.articulos");
				}
			}
			/*-------- cuando se presiona el check Reservar Stock Entrega*/
			else if(request.getParameter("reservarStockEntrega")!=null){
				if(formulario.getCheckReservarStockEntrega()!=null && formulario.getCheckReservarStockEntrega().equals(estadoActivo)){	
					formulario.setCheckReservarStockEntrega(null);
					infos.add("habilitado", new ActionMessage("message.reservarStockEntrega.activo"));					
				}else{
					formulario.setCheckReservarStockEntrega(estadoActivo);
					infos.add("deshabilitado", new ActionMessage("message.reservarStockEntrega.inactivo"));					
				}	
			}
			else if(request.getParameter("cambiarHora")!=null){
				LogSISPE.getLog().info("Ingreso a cambiarHora");
			}else if(request.getParameter("cambiarMinuto")!=null){
				LogSISPE.getLog().info("Ingreso a cambiarMinuto");
				
			}/*-------- cuando se cancela el popUp Reservar Stock Entrega*/
			else if (request.getParameter("cancelarReservarStockEntrega")!=null ){
				request.setAttribute("cancelarReservarStockEntrega", null);
				session.setAttribute(SessionManagerSISPE.POPUP, null);
			}				
			/*-------- cuando se presiona el check pago en efectivo*/
			else if(request.getParameter("pagoEfectivo")!=null){
				LogSISPE.getLog().info("presiono el check pago en efectivo-->{}",formulario.getCheckPagoEfectivo());
				
				session.setAttribute(CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
				
				//obtengo el codigoTipDesMax-navidad desde un parametro
				String codigoTipoDctoMaxiNavidad = CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
					//se verifica para habilitar un nuevo descuento similar al general.
					if(formulario.getCheckPagoEfectivo()!=null && formulario.getCheckPagoEfectivo().equals(estadoActivo)){
						session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
						infos.add("habilitado", new ActionMessage("messages.pagoEfectivo.activo"));
						
						final String existNavEmpCredito = CotizacionReservacionUtil.validarExisteDesNavEmpCredito(request);
						if (existNavEmpCredito != null) {
							request.getSession().removeAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
							
							Object descuentos = request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
							if (descuentos != null) {
								
								String [] arrayDesc = (String[]) descuentos;
								Collection<String> descSelecCol = CollectionUtils.select(Arrays.asList(arrayDesc), new Predicate() {
									
									public boolean evaluate(Object arg0) {
										String llave = (String) arg0; 
										return !llave.equals(existNavEmpCredito);
									}
								});
								
								request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, descSelecCol.toArray(new String[0]));
								
							}
						
							
							Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(COL_DESCUENTOS);
							
							if (!CollectionUtils.isEmpty(colDescuentoEstadoPedidoDTO)) {
								
								Collection<String> desEstPedCol = CollectionUtils.select(colDescuentoEstadoPedidoDTO, new Predicate() {
									
									public boolean evaluate(Object arg0) {
										DescuentoEstadoPedidoDTO desEst = (DescuentoEstadoPedidoDTO) arg0;
										return !desEst.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().
												equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
									}
								});
								
								session.setAttribute(COL_DESCUENTOS, desEstPedCol);
								
							}
							
							
						}
					}else{
						session.removeAttribute(CHECK_PAGO_EFECTIVO);
						infos.add("deshabilitado", new ActionMessage("messages.pagoEfectivo.inactivo"));
						
						Boolean existeLlaveMaxiNavidad = Boolean.FALSE;
						//se eliminan las llaves que tengan el codigoTipoDescuentoMaxiNavidad
						Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(COL_DESC_SELECCIONADOS);
						if(CollectionUtils.isNotEmpty(llaveDescuentoCol) && codigoTipoDctoMaxiNavidad != null && !codigoTipoDctoMaxiNavidad.equals("")){
							for(String llaveActual : llaveDescuentoCol){
								if(llaveActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDctoMaxiNavidad)){
									llaveDescuentoCol.remove(llaveActual);
									existeLlaveMaxiNavidad = Boolean.TRUE;
									break;
								}
							}
							session.setAttribute(COL_DESC_SELECCIONADOS, llaveDescuentoCol);
							
							//se elimina la llave de opDescuentos
							if(existeLlaveMaxiNavidad){
								String[] opDescuentosSession = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);	
								if(opDescuentosSession != null && opDescuentosSession.length > 0){
									opDescuentosSession = CotizacionReservacionUtil.eliminarLlavesRepetidas(opDescuentosSession);
									String[] opDescuentos = new String[opDescuentosSession.length-1];
									int posVectos = 0;
									for(String llaveAct: opDescuentosSession){
										if(llaveAct != null && !llaveAct.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDctoMaxiNavidad)){
											opDescuentos[posVectos] = llaveAct;
											posVectos++;
										}
									}
									request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
								}	
							}
						}						
						// se elimina el descuento maxinavidad
						if(CollectionUtils.isEmpty(llaveDescuentoCol)){
							Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(COL_DESCUENTOS);
							if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTO)){
								for(DescuentoEstadoPedidoDTO dsctoActual : colDescuentoEstadoPedidoDTO){
									if(dsctoActual.getLlaveDescuento()!=null && dsctoActual.getLlaveDescuento().contains(CODIGO_TIPO_DESCUENTO+codigoTipoDctoMaxiNavidad)){
										colDescuentoEstadoPedidoDTO.remove(dsctoActual);
										break;
									}
								}
							}
							session.setAttribute(COL_DESCUENTOS, colDescuentoEstadoPedidoDTO);
						}
						//se actualizan los descuentos									
//						formulario.actualizarDescuentos(request, warnings);
						CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
					}
					//				if ( "NO".equals(session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") )) {//cambios oscar
					//					request.setAttribute("ec.com.smx.sic.sispe.reservacion.preciosMejorados", "ok");
					//				}
			}

			/*------------------ cuando se desea actualizar el valor unitario de un art\u00EDculo ----------------------*/
			else if(request.getParameter("actualizarPreciosUnitarios")!=null){
				session.setAttribute(ACTIVAR_INPUTS_CAMBIO_PRECIOS,"ok");
			}
			
			/*-------------------- cuando se desea eliminar un registro del detalle del pedido ----------------------*/
			else if(request.getParameter("eliminarArticulos")!=null)
			{
				LogSISPE.getLog().info("por eliminar");
				//se obtienen los indices seleccionados
				String [] indicesRegistros = formulario.getChecksSeleccionar();
				//String todos = formulario.getCheckSeleccionarTodo();
				//obtengo la colecci\u00F3n de detalles de la sesion
				List<DetallePedidoDTO> detallePedido = (List<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				//obtengo los codigos de los art\u00EDculos de sesi\u00F3n
				ArrayList codigosArticulos = (ArrayList)session.getAttribute(COL_CODIGOS_ARTICULOS);

				boolean eliminarDetalles = true;
				String confirmarEliminarDetalle = request.getParameter("siEliminarDetalle");
				
				Collection<DetallePedidoDTO> detallesEliminados = new ArrayList<DetallePedidoDTO>();
				
				if(indicesRegistros!=null)
				{
					//obtengo los atributos del formulario en sesion
					Double subTotalAplicaIVA = (Double)session.getAttribute(SUB_TOTAL_APLICA_IVA);
					Double subTotalNoAplicaIVA = (Double)session.getAttribute(SUB_TOTAL_NOAPLICA_IVA);
//					Double descuentoTotal = (Double)session.getAttribute(DESCUENTO_TOTAL);
//					LogSISPE.getLog().info("descuentoTotal.doubleValue(): "+descuentoTotal.doubleValue());
					//se inicializan los nuevos totales del pedido
					double nuevoSubTotalAplicaIVA = subTotalAplicaIVA.doubleValue();
					double nuevoSubTotalNoAplicaIVA = subTotalNoAplicaIVA.doubleValue();

					int indiceRegistro=0;
					LogSISPE.getLog().info("# registros: {}",indicesRegistros.length);
					Collection<DetallePedidoDTO> detallePedidoConsolidado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
					
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesBorrar = new ArrayList<EstadoPedidoAutorizacionDTO>();
					ArrayList<DetalleEstadoPedidoAutorizacionDTO> desactivarAutorizacionesCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
					
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)
							request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
					
					ArrayList<EstadoPedidoAutorizacionDTO> actualizarAutorizacionesBorrar = new ArrayList<EstadoPedidoAutorizacionDTO>();
					ArrayList<EstadoPedidoAutorizacionDTO> actualizarAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)
							request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTUALIZAR);
					
					//se recorre el arreglo de indices
					for(int i=0;i<indicesRegistros.length;i++){
						indiceRegistro = Integer.parseInt(indicesRegistros[i]);
						//Obtengo el detalle que voy a eliminar
						DetallePedidoDTO detallePedidoDTO = detallePedido.get(indiceRegistro);
						
						if(EntregaLocalCalendarioUtil.existenEntregasDomicilioCDEntregadas(detallePedidoDTO)){
							if(confirmarEliminarDetalle == null){
								CotizacionReservacionUtil.instanciarPopUpEliminarDetalleEntregasDespachadas(request, "crearCotizacion.do");
								formulario.setChecksSeleccionar(indicesRegistros);
								eliminarDetalles = false;
								break;
							}else{
								LogSISPE.getLog().info("se confirma eliminar detalles con entregas a domicilio desde el CD despachadas");
								session.removeAttribute(SessionManagerSISPE.POPUP);
							}
						}
						
						LogSISPE.getLog().info("ARTICULO: {}",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
						EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();

						//SE ACTUALIZA EL SUBTOTAL QUE APLICA IVA SI EL ARTICULO ELIMINADO TIENE IVA
						if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
							//se actualiza el subtotal que aplica iva (TARIFA 12%)
							nuevoSubTotalAplicaIVA = nuevoSubTotalAplicaIVA - estadoDetallePedidoDTO.getValorTotalEstadoIVA().doubleValue();
						}else{
							//se actualiza el subtotal que no aplica iva (TARIFA 0%)
							nuevoSubTotalNoAplicaIVA = nuevoSubTotalNoAplicaIVA - estadoDetallePedidoDTO.getValorTotalEstado().doubleValue();
						}

						detallesEliminados.add(SerializationUtils.clone(detallePedidoDTO));
						//modifica los datos de las entregas del detalle a eliminar
						formulario.eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, new ActionErrors(), request, false, false);
						
						//eliminar detalle del pedido consolidado
						if(detallePedidoConsolidado!=null){
							for(DetallePedidoDTO detPedDTO: detallePedidoConsolidado){
								 if(detPedDTO.getId().getCodigoArticulo().equals(detallePedidoDTO.getId().getCodigoArticulo()) && 
										 detPedDTO.getId().getCodigoPedido().equals(detallePedidoDTO.getId().getCodigoPedido())){
									 detallePedidoConsolidado.remove(detPedDTO);
									 break;
								 }
							}
							//se actualizan el pedido consolidado sumarizado
							ConsolidarAction.crearDetalleConsolidadosNoRepetidos(request, detallePedidoConsolidado);
						}
						//eliminar autorizaciones de las colas
						if(estadoDetallePedidoDTO.getDetalleEstadoPedidoAutorizacionCol()!=null && 
								!estadoDetallePedidoDTO.getDetalleEstadoPedidoAutorizacionCol().isEmpty()){
							//Buscando autorizaciones descuento variables Pendientes
							if(colaAutorizaciones!=null && !colaAutorizaciones.isEmpty()){
								for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizaciones){
									for(DetalleEstadoPedidoAutorizacionDTO detalle : estadoDetallePedidoDTO.getDetalleEstadoPedidoAutorizacionCol()){
										//solo para el caso de descuentos variable
										if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
												detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
											if(detalle.getEstadoPedidoAutorizacionDTO()!=null 
													&& detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacion.getValorReferencial())){									
												//se agrega solo una vez la autorizacion
												if(CollectionUtils.isNotEmpty(colaAutorizacionesBorrar)){
													Boolean encontroValorRef = Boolean.FALSE;
													for(EstadoPedidoAutorizacionDTO autActual : colaAutorizacionesBorrar){
														if(autActual.getValorReferencial().equals(autorizacion.getValorReferencial())){
															encontroValorRef = Boolean.TRUE;
														}
													}
													if(!encontroValorRef){
														colaAutorizacionesBorrar.add(detalle.getEstadoPedidoAutorizacionDTO());
														desactivarAutorizacionesCol.add(detalle);
														break;
													}
												}
												else {
													colaAutorizacionesBorrar.add(detalle.getEstadoPedidoAutorizacionDTO());
													desactivarAutorizacionesCol.add(detalle);
													break;
												}
											}
										}
									}
								}
							}					
						}
						
						//Buscando autorizaciones descuento variables solicitadas
						if(actualizarAutorizaciones!=null && !actualizarAutorizaciones.isEmpty()){
							for(EstadoPedidoAutorizacionDTO autorizacion : actualizarAutorizaciones){
								if(CollectionUtils.isNotEmpty(estadoDetallePedidoDTO.getDetalleEstadoPedidoAutorizacionCol())){
									for(DetalleEstadoPedidoAutorizacionDTO detalle : estadoDetallePedidoDTO.getDetalleEstadoPedidoAutorizacionCol()){
										//solo para el caso de descuentos variable
										if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
												detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
											if(detalle.getEstadoPedidoAutorizacionDTO()!=null 											
													&& detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacion.getValorReferencial())){											
												actualizarAutorizacionesBorrar.add(detalle.getEstadoPedidoAutorizacionDTO());
												break;
											}
										}
									}
								}
							}
						}
					}	
					
					if(eliminarDetalles){
						//se elimina el primer registro
						indiceRegistro = Integer.parseInt(indicesRegistros[0]);
						detallePedido.remove(indiceRegistro);
						codigosArticulos.remove(indiceRegistro);
						int contElimnados=1;
						//se eliminan los siguientes registros
						for(int i=1;i<indicesRegistros.length;i++){
							indiceRegistro = Integer.parseInt(indicesRegistros[i]);
							indiceRegistro = indiceRegistro - contElimnados;
							detallePedido.remove(indiceRegistro);
							codigosArticulos.remove(indiceRegistro);
							//se eliminan los registros seleccionados
							contElimnados++;
						}
						
						EntregaLocalCalendarioUtil.eliminarEntregasPedido(request,new ArrayList<DetallePedidoDTO>(detallePedido));
						
						//si todavia existen detalles
						if(detallePedido != null && !detallePedido.isEmpty() && !colaAutorizacionesBorrar.isEmpty()){
							//se verifica si los detalles actuales pertenecen a la autorizacion que esta por ser eliminada
							
							Collection<EstadoPedidoAutorizacionDTO> autorizacionesReponer = new ArrayList<EstadoPedidoAutorizacionDTO>();
							//se recorren las autorizaciones por borrar
							for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizacionesBorrar){
								for(DetallePedidoDTO detalleActual : detallePedido){
									//si el detalle tiene autorizaciones
									if(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol() != null && !detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().isEmpty()){
										//se recorren las autorizaciones del detalle actual 
										for(DetalleEstadoPedidoAutorizacionDTO detalle : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
											//solo para el caso de descuentos variable
											if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
													detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
												if(detalle.getEstadoPedidoAutorizacionDTO()!=null 
														&& detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacion.getValorReferencial())){	
													
													if(!autorizacionesReponer.contains(autorizacion)){
														LogSISPE.getLog().info("autorizacion repuesta "+detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial());
														autorizacionesReponer.add(autorizacion);
														desactivarAutorizacionesCol.remove(detalle);
													}
													break;
												}
											}
										}
									}								
								}													
							}
							
							//se verifica en pedidos consolidados si la autorizacion depende de otro pedido
							if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
								for(EstadoPedidoAutorizacionDTO autorizacion : colaAutorizacionesBorrar){
									for(DetallePedidoDTO detalleActual : detallePedidoConsolidado){
										//si el detalle tiene autorizaciones
										if(AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detalleActual, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue())){
											//se recorren las autorizaciones del detalle actual 
											for(DetalleEstadoPedidoAutorizacionDTO detalle : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
												//solo para el caso de descuentos variable
												if(detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
														detalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
													if(detalle.getEstadoPedidoAutorizacionDTO()!=null 
															&& detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial().equals(autorizacion.getValorReferencial())){	
														
														if(!autorizacionesReponer.contains(autorizacion)){
															LogSISPE.getLog().info("autorizacion repuesta "+detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial());
															autorizacionesReponer.add(autorizacion);
															desactivarAutorizacionesCol.remove(detalle);
														}
														break;
													}
												}
											}
										}								
									}													
								}
							}
							colaAutorizacionesBorrar.removeAll(autorizacionesReponer);
						}
						
						//Eliminando autorizaciones descuento variable nuevas
						if(!colaAutorizacionesBorrar.isEmpty()){
							//se desactivan las autorizaciones que fueron eliminadas
							Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
							if(CollectionUtils.isEmpty(autorizacionesDesactivarCol)){
								autorizacionesDesactivarCol = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
							}
							autorizacionesDesactivarCol.addAll(desactivarAutorizacionesCol);
							session.setAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL, autorizacionesDesactivarCol);
							
							colaAutorizaciones.removeAll(colaAutorizacionesBorrar);						
							Double[][] porcentajeVarDescuentos = (Double[][]) session.getAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE);
							
							for(EstadoPedidoAutorizacionDTO autorizacionEliminarActual : colaAutorizacionesBorrar){
								String valorReferencialBorrar = autorizacionEliminarActual.getValorReferencial();
								
								//se transforma el valor referencial a llave descuento
								//6-CTD3-CMDCAN-COM01-INX1-AUTORIZACION DESCUENTO VARIABLE OLIVER WRIGHT-9.0 ---> CTD3-CMDCAN-COM01
								valorReferencialBorrar = valorReferencialBorrar.split(SEPARADOR_TOKEN)[1]+SEPARADOR_TOKEN
										+valorReferencialBorrar.split(SEPARADOR_TOKEN)[2]+SEPARADOR_TOKEN
										+valorReferencialBorrar.split(SEPARADOR_TOKEN)[3];
								
								// se borra de opDescuento la llave
								String[] opDescuentos = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);	
								if(opDescuentos != null && opDescuentos.length > 0)
									for(int i=0; i<opDescuentos.length; i++)
										if(opDescuentos[i] != null &&  opDescuentos[i].contains(valorReferencialBorrar))
											opDescuentos[i]= null;
								
								//se borra el porcentaje de ese comprador
								int indice = new Integer(autorizacionEliminarActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].split(INDICE_DESCUENTO_GENERAL)[1].substring(0,autorizacionEliminarActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
								int indiceMp = new Integer(autorizacionEliminarActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].split(INDICE_DESCUENTO_GENERAL)[1].substring(autorizacionEliminarActual.getValorReferencial().split(SEPARADOR_TOKEN)[4].split(INDICE_DESCUENTO_GENERAL)[1].length()-1));
								if(porcentajeVarDescuentos.length > indice){
									porcentajeVarDescuentos[indice][indiceMp] = 0D;
									session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, porcentajeVarDescuentos);
								}
								
								//se buscan los nulos
								int contadorNulls = 0;
								for(int i=0; i<opDescuentos.length; i++)
									if(opDescuentos[i] == null || opDescuentos.equals(""))
										contadorNulls++;
								
								//se eliminan los nulos del vector nuevoOpDescuento
								if(contadorNulls != 0){
									String[] opDescuentosSinNulls = new String[opDescuentos.length-contadorNulls];
									int pos = 0;
									for(int i=0; i<opDescuentos.length; i++){
										if(opDescuentos[i] != null && !opDescuentos[i].equals("")){
											opDescuentosSinNulls[pos] = opDescuentos[i];
											pos++;
										}
									}
									opDescuentos = opDescuentosSinNulls;
								}					

								request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
								
								
								//se recupera de sesion los descuentos
								Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
								//se recupera de sesion los descuentos variables
								Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
								if(colDescuentoEstadoPedidoDTO != null && !colDescuentoEstadoPedidoDTO.isEmpty() ){
									// se borra el descuento
									for(DescuentoEstadoPedidoDTO descuentoActual : colDescuentoEstadoPedidoDTO){
										if(descuentoActual.getLlaveDescuento() != null && descuentoActual.getLlaveDescuento().contains(valorReferencialBorrar)){
											colDescuentoEstadoPedidoDTO.remove(descuentoActual);
											break;
										}
									}
									//se borran los descuentos seleccionados
									session.setAttribute(CotizarReservarAction.COL_DESCUENTOS, colDescuentoEstadoPedidoDTO);
								}
								//se borran los descuentos variables
								if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
									for(DescuentoEstadoPedidoDTO descuentoActual : colDescuentoEstadoPedidoDTOVariables){
										if(descuentoActual.getLlaveDescuento() != null && descuentoActual.getLlaveDescuento().contains(valorReferencialBorrar)){
											colDescuentoEstadoPedidoDTOVariables.remove(descuentoActual);
											break;
										}
									}
									session.setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES, colDescuentoEstadoPedidoDTOVariables);
								}
								
								//se borra la llave de sesion
								Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
								if(llaveDescuentoCol != null && !llaveDescuentoCol.isEmpty()){
									for(String llaveActual : llaveDescuentoCol){
										if(llaveActual.contains(valorReferencialBorrar)){
											llaveDescuentoCol.remove(llaveActual);
											break;
										}
									}
									request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llaveDescuentoCol);		
								}																				
							}						

							if(colaAutorizaciones.isEmpty())
								request.getSession().removeAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
						}

						//Eliminando autorizacaniones decuento variable creadas
						if(!actualizarAutorizacionesBorrar.isEmpty()){
							actualizarAutorizaciones.removeAll(actualizarAutorizacionesBorrar);
							if(actualizarAutorizaciones.isEmpty())
								request.getSession().removeAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTUALIZAR);
						}

						//se actualiza el detalle del pedido y los c\u00F3digos de los art\u00EDculos
						session.setAttribute(DETALLE_PEDIDO, detallePedido);
						codigosArticulos = new ArrayList<String>();
						for(DetallePedidoDTO detalle:detallePedido){						
							codigosArticulos.add(detalle.getArticuloDTO().getId().getCodigoArticulo());
						}
						
						session.setAttribute(COL_CODIGOS_ARTICULOS,codigosArticulos);
						
						//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
						EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedido, request);
						
						//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
						formulario.setOpDescuentos(DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE));

						//se actualiza el valor de los descuentos y las variables asociadas
						formulario.actualizarDescuentos(request, warnings);

						//se actualiza las variable que contiene el subTotal que aplica el IVA
						session.setAttribute(SUB_TOTAL_APLICA_IVA, Double.valueOf(nuevoSubTotalAplicaIVA));
						//se actualiza la variable que contiene el subTotal que no aplica el IVA
						session.setAttribute(SUB_TOTAL_NOAPLICA_IVA, Double.valueOf(nuevoSubTotalNoAplicaIVA));

						//lamada al m\u00E9todo que actualiza los totales del pedido
						//formulario.actualizarTotales(request);

						//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
						ActionMessage message = CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
						if(message != null){
							warnings.add("errorCalculo", message);
						}
						//se resetean los totales de los formularios en caso de que existan cantidades negativas
						//por los decimales
						if(formulario.getSubTotalAplicaIVA().doubleValue()<=0){
							formulario.setSubTotalAplicaIVA(Double.valueOf(0));
							formulario.setIvaTotal(Double.valueOf(0));
							session.setAttribute(SUB_TOTAL_APLICA_IVA, Double.valueOf(0));
						}else if(formulario.getSubTotalNoAplicaIVA().doubleValue()<=0){
							formulario.setSubTotalNoAplicaIVA(Double.valueOf(0));
							session.setAttribute(SUB_TOTAL_NOAPLICA_IVA, Double.valueOf(0));
						}
						//condici\u00F3n adicional para el descuento
						if(formulario.getDescuentoTotal().doubleValue()<=0)
							formulario.setDescuentoTotal(Double.valueOf(0));

						//esta condici\u00F3n significa que se eliminaron todos los art\u00EDculos del detalle
						if(detallePedido.isEmpty()){
							//se blanquean los campos del formulario
							formulario.setSubTotal(Double.valueOf(0));
							formulario.setSubTotalAplicaIVA(Double.valueOf(0));
							formulario.setSubTotalNoAplicaIVA(Double.valueOf(0));
							formulario.setDescuentoTotal(Double.valueOf(0));
							formulario.setIvaTotal(Double.valueOf(0));
							formulario.setTotal(Double.valueOf(0));
							session.setAttribute(SUB_TOTAL_APLICA_IVA, Double.valueOf(0));
							session.setAttribute(SUB_TOTAL_NOAPLICA_IVA, Double.valueOf(0));

							//se eliminan las variables de sesi\u00F3n utilizadas en las entregas
//							session.removeAttribute(EntregaLocalCalendarioAction.VALORTOTALENTREGA);
							session.removeAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
							session.removeAttribute(EntregaLocalCalendarioAction.SECDIRECCIONES);
							session.removeAttribute(EntregaLocalCalendarioAction.DIRECCIONES);
						}
						session.setAttribute(CotizacionReservacionUtil.MODIFICAR_PESO_INACTIVO, null);
					}
				}else{
					infos.add("articulosEliminar",new ActionMessage("info.seleccionArticulosEliminar"));
				}
				
				if(eliminarDetalles){
					formulario.setCheckSeleccionarTodo(null);
					formulario.setChecksSeleccionar(null);
					//formulario.setCheckActualizarStockAlcance(null);
					//se eliminan las autorizaciones de stock cuando sea el caso
					AutorizacionesUtil.eliminarAutorizacionDetallesEliminados(request, detallePedido);
					CotizarReservarForm.eliminarDireccionEntregaPedido(request, detallesEliminados);
				}
				salida = "desplegar";
			}
			
			else if(request.getParameter("cancelarEliminarDetalleDespachado") != null){
				LogSISPE.getLog().info("cancela eliminar detalles por tener entregas a domicilio desde el CD despachadas");
				session.removeAttribute(SessionManagerSISPE.POPUP);
			}
			/*-------------------- cuando se desea subir un archivo de configuracion del beneficiario ----------------------*/
			else if(request.getParameter("subirArchivo")!=null) {
				//verifica si existen entregas con sicmer para enviar un mail de alerta
				if(session.getAttribute(EXISTEN_ENTREGAS_SICMER)!=null){
					CotizacionReservacionUtil.instanciarPopUpMailEntregasSICMER(request,(session.getAttribute(IMPRIMIR_CONVENIOS_SICMER)!=null?Boolean.TRUE:Boolean.FALSE)&&((VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.vistaPedido")).getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO));
					SessionManagerSISPE.getServicioClienteServicio().transEnviarNotificacionEntregasSICMER((VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.vistaPedido"));
				}else{
				LogSISPE.getLog().info("Entra a subirArchivo del Beneficiario");
				//se construye la ventana de pregunta archivo beneficiario
				instanciarPopUpPreguntaBeneficiario(request);
				}
				salida = "registro";
			}
			/*-------------------- muestra la ventana de la configuracion de entregas a domicilio y sus responsables ----------------------*/
			else if(request.getParameter("subirVentanaEntregas")!=null) {
				LogSISPE.getLog().info("<<<Entra a mostrar la configuracion de entregas a domicilio>>");
				//se construye la ventana 
				instanciarPopUpEntregaDomicilioBodega(request);
				salida = "registro";
			}
			/*-------------------- muestra la ventana de confirmacion cambio entidad responsable ----------------------*/
			else if(request.getParameter("aceptarCambioEntidad")!=null) {
				LogSISPE.getLog().info("<<<Entra a cambiar la entidad responsable>>");
				salida = "registro";
			}
			/*-------------------- muestra la ventana de confirmacion cambio entidad responsable ----------------------*/
			else if(request.getParameter("cancelarCambioEntidad")!=null) {
				LogSISPE.getLog().info("<<<Entra a cancelar la entidad responsable>>");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegar";
			}
			/*-------- cuando se acepta la subida de un archivo de configuracion del beneficiario ----------*/
			else if(request.getParameter(ACEPTAR_ARCH_BENE) != null) {
				
				EnvioMailUtil.enviarMailReservacion(request);
				PedidoDTO pedidoDTO = new PedidoDTO();
				PedidoDTO pedidoActuallizadoDTO = new PedidoDTO();
				LogSISPE.getLog().info("Entra a mostra la ventana para subir el archivo del beneficiario");
				//se construye la ventana de informacion de archivos del beneficiario y sus opciones(subir,eliminar)
				LogSISPE.getLog().info("Valor del checkSubirArchivo->{}",formulario.getCheckSubirArchivo());
				//obtiene el codigo del pedido en session
				//VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				
				//se obtiene el codigoPedido del detalle actual del pedido
				List<DetallePedidoDTO> detalleReservacion = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				DetallePedidoDTO entrega = new DetallePedidoDTO();
				entrega = detalleReservacion.get(0);
				LogSISPE.getLog().info("Codigo Pedido->{}",entrega.getId().getCodigoPedido());
				
				//seteo el codigo de pedido y codigoCompania
				pedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				pedidoDTO.getId().setCodigoPedido(entrega.getId().getCodigoPedido());
				//obtengo el pedido actual
				ArrayList<PedidoDTO> pedidoActual = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
				pedidoActuallizadoDTO = pedidoActual.get(0);
				//seteo el valor en el campo archivoBaneficiario
				pedidoActuallizadoDTO.setArchivoBeneficiario(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
				SessionManagerSISPE.getServicioClienteServicio().transActualizarPedido(pedidoActuallizadoDTO);
				if(formulario.getCheckSubirArchivo() != null){
					CotizacionReservacionUtil.instanciarVentanaSubirArchivoBeneficiario(request, accionReservar);
					CotizacionReservacionUtil.listarArchivos(request, entrega.getId().getCodigoPedido());		
				}else{
					//se borra la ventana de confirmacion del archivo Beneficiario
					CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionReservar);
				}
				salida = "registro";	
			}
			/*-------------------- cuando se desea subir un archivo de configuracion del beneficiario ----------------------*/
			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("adjuntarArchivo"))
			{				
				LogSISPE.getLog().info("Boton Aceptar");
				ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
			    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
			    // verifica si el archivo examinado se repite con otro almacenado anteriormente
			    boolean verificarArchivo = true;
			    //se obtiene el codigoPedido del detalle actual del pedido
				List<DetallePedidoDTO> detalleReservacion = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				DetallePedidoDTO entrega = new DetallePedidoDTO();
				entrega = detalleReservacion.get(0);
			    archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			    archivoPedidoDTO.setId(archivoPedidoID);
			    archivoPedidoDTO.setCodigoLocal(SessionManagerSISPE.getCodigoLocalObjetivo(request));
			    archivoPedidoDTO.setCodigoPedido(entrega.getId().getCodigoPedido());
			    archivoPedidoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
			    archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.Beneficiario"));
			    archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));
			    archivoPedidoDTO.setArchivo(formulario.getArchivo().getFileData());
			    //CAMPOS DE AUDITORIA
			    archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			    archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			    session.removeAttribute(SessionManagerSISPE.ARCHIVO_DTO);
			    //verifica si el nuevo archivo no tiene el mismo nombre.
			    verificarArchivo = CotizacionReservacionUtil.validarArchivo(request, formulario.getArchivo().getFileName());
			    if(verificarArchivo){
			    	LogSISPE.getLog().info("Archivo Diferente");
			    	//se acepta el archivo del beneficiario
					CotizacionReservacionUtil.aceptarArchivoBeneficiario(request,archivoPedidoDTO);
			    } else {
			    	LogSISPE.getLog().info("Archivo Repetido");
			    	CotizacionReservacionUtil.instanciarVentanaAuxiliarArchivo(request, accionReservar);
			    }
				salida = "registro";
			}
			//accion del boton reemplazar archivo existente.
			else if(request.getParameter("reemplazarArchivo") != null){
				LogSISPE.getLog().info("Acepto el reeplazo del archivo");
				ArchivoPedidoDTO archivoDTO =(ArchivoPedidoDTO)session.getAttribute(SessionManagerSISPE.ARCHIVO_DTO);
				LogSISPE.getLog().info("Compania->{}",archivoDTO.getId().getCodigoCompania());
				LogSISPE.getLog().info("secuencial->{}",archivoDTO.getId().getSecuencialArchivoPedido());
				LogSISPE.getLog().info("local->{}",archivoDTO.getCodigoLocal());
				LogSISPE.getLog().info("codpedido->{}",archivoDTO.getCodigoPedido());
				LogSISPE.getLog().info("tama\u00F1o->{}",archivoDTO.getTamanioArchivo());
				LogSISPE.getLog().info("tipo->{}",archivoDTO.getTipoArchivo());
				LogSISPE.getLog().info("nombre->{}",archivoDTO.getNombreArchivo());
				//LogSISPE.getLog().info("bytes->{}",archivoDTO.getArchivo());
				
				LogSISPE.getLog().info("Nombre_Nuevo->{}",formulario.getArchivo().getFileName());
				//LogSISPE.getLog().info("Bytes Nuevo->{}",formulario.getArchivo().getFileData());
				LogSISPE.getLog().info("Tama\u00F1o Nuevo->{}",formulario.getArchivo().getFileSize());
				
				//setea el tama\u00F1o, bytes, nombre.
				archivoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
				archivoDTO.setArchivo(formulario.getArchivo().getFileData());
				archivoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));	
				//CAMPOS DE AUDITORIA
				archivoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				archivoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				//creo el nuevo archivo.
				CotizacionReservacionUtil.confirmarReemplazo(request, archivoDTO);
				//finalmente oculta el popUpAuxiliar.
				CotizacionReservacionUtil.cancelarReeplazoArchivo(request, accionReservar);
				salida = "registro";	
			}
		    /*-------- cuando se cancela el reemplazo del archivo existente----------*/
			else if(request.getParameter("cancelarReemplazo") != null){
					//se cancela el archivo del beneficiario 
					CotizacionReservacionUtil.cancelarReeplazoArchivo(request, accionReservar);
					salida = "registro";
			}
			/*-------- cuando se cancela el la subida de un archivo de configuracion del beneficiario----------*/
			else if(request.getParameter(CANCELAR_ARCH_BENE) != null){
				//se cancela el archivo del beneficiario 
				LogSISPE.getLog().info("Boton Cancelar");
				//session.removeAttribute(SessionManagerSISPE.POPUP);
				
				EnvioMailUtil.enviarMailReservacion(request);
				CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionReservar);
				salida = "registro";
			}
			/*-------- cuando se acepta  la subida de un archivo de configuracion del beneficiario----------*/
			else if(request.getParameter(SUBIR_ARCH_BENE) != null){
				//se cancela el archivo del beneficiario 
				LogSISPE.getLog().info("Se confirmo la subida del Archivo");
				//remueve la variable de session
				CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionReservar);
				salida = "registro";
			}
			//accion del icono eliminar archivo del popUp mostrarArchivoBeneficiario
			else if(request.getParameter("eliminarArchivo") != null){
				//se obtine el secuencial del indice que envia desde la jsp.
				Long secuencialArchivo = Long.valueOf(request.getParameter("eliminarArchivo"));
				
				//se obtiene el codigoPedido del detalle actual del pedido
				List<DetallePedidoDTO> detalleReservacion = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				DetallePedidoDTO entrega = new DetallePedidoDTO();
				entrega = detalleReservacion.get(0);
				
				LogSISPE.getLog().info("Boton Eliminar de la lista de archivos{}",secuencialArchivo);
				//session.removeAttribute(SessionManagerSISPE.POPUP);
				CotizacionReservacionUtil.eliminarArchivoBeneficiario(request, secuencialArchivo, entrega.getId().getCodigoPedido());
				salida = "registro";	
			}
			else if(request.getParameter("verArchivo") != null){
				LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
				CotizacionReservacionUtil.verArchivo(request, response);
			}
			//aceptar autorizaci\u00F3n para modificar reserva
			else if(request.getParameter("aceptarAutorizacionModRes") != null){
				LogSISPE.getLog().info("Acepta la autorizaci\u00F3n para modificar la reserva");
				String tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.modificarReservaSolicitudBodega");
				String grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
				//obtiene el rol de administrados de bodega
				ParametroDTO parametro = new ParametroDTO();
				parametro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoRolAdministradorBodega"));
				//aumento el codigo del rolId de administrador de bodega
				String rolId = SISPEFactory.obtenerServicioSispe().transObtenerValorParametro(parametro).getValorParametro();
				Integer codigoLocalReferencia = (Integer)session.getAttribute(CODIGO_LOCAL_REFERENCIA);
				
				//condici\u00F3n para las autorizaciones de modificacion de la reserva
				LogSISPE.getLog().info("OPAutorizacion {}",formulario.getOpAutorizacion());
				LogSISPE.getLog().info("estadoInactivo {}",estadoInactivo);
//				if(formulario.getOpAutorizacion().equals(estadoInactivo)){
//					try{
//						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.validarNumeroAutorizacion(request, request.getParameter("numeroAutorizacion").trim(), 
//								request.getParameter("observacionAutorizacion"), tipoAutorizacion, grupoAutorizacion, codigoLocalReferencia, rolId);
//
//						session.setAttribute(AUTORIZACION_MODIFICA_RESERVA_SOLICITUD_CD, autorizacionDTO);
//						AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO, request);
//						session.removeAttribute(SessionManagerSISPE.POPUP);
//						session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
//						session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
//						LogSISPE.getLog().info("acepta la autorizacion de modificacion reserva");
//						exitos.add("exitoAceptarAutorizacion", new ActionMessage("errors.autorizacion.creada.exito"));
//					}catch(Exception ex){
//						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
//						UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
//						utilPopUp.setMensajeErrorVentana("La autorizaci\u00F3n ingresada no es v\u00E1lida");						
//						formulario.setOpAutorizacion(estadoInactivo);
//					}
//				}
			}
			//--------- validar la autorizacion cuando se quiere un descuento variable -----
			else if(request.getParameter(ACEPTAR_DESCUENTO_VARIABLE) != null){
				LogSISPE.getLog().info("Entra por el metodo aceptar_descuento_variable/\u00F3 problemas de stock en (pavos-pollos-canastas) boton aceptar de la ventana de autorizacion");
				String tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.descuentoVariable");
				String grupoAutorizacion;
				String rolId = null;
				ParametroDTO parametro = new ParametroDTO();
				parametro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//valida en que grupo de autorizacion debe verificar
				if(request.getParameter("autorizaGerCom") != null){
					grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.gerenciaComercial");
					//obtiene el rol de GerenciaComercial				
					parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoRolGerenteComercial"));
					//aumento el codigo del rolId de GerenciaComercial
					rolId= SISPEFactory.obtenerServicioSispe().transObtenerValorParametro(parametro).getValorParametro();
				}else if(request.getParameter("autorizaGerCom1") != null){
					tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.sinStock.pavpolcan");
					grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.gerenciaComercial");					
					//obtiene el rol de GerenciaComercial				
					parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoRolGerenteComercial"));
					//aumento el codigo del rolId de GerenciaComercial
					rolId= SISPEFactory.obtenerServicioSispe().transObtenerValorParametro(parametro).getValorParametro();					
				}else if(request.getParameter("autorizaGerComConsolidacion") != null){
					tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.consolidar.maximo");
					grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.gerenciaComercial");
					//obtiene el rol de GerenciaComercial				
					parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoRolGerenteComercial"));
					//aumento el codigo del rolId de GerenciaComercial
					rolId= SISPEFactory.obtenerServicioSispe().transObtenerValorParametro(parametro).getValorParametro();
				}
				else{
					grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
				}				
				Integer codigoLocalReferencia = (Integer)session.getAttribute(CODIGO_LOCAL_REFERENCIA);
				
				//condici\u00F3n para las autorizaciones del descuento variable
				if(formulario.getOpAutorizacion().equals(estadoInactivo)){
					LogSISPE.getLog().info("Estado opAutorizaci\u00F3n inactivo");
				}else{
					try{						
						UserDto userDTO = SessionManagerSISPE.getDefault().loginUser(request.getParameter("loginAutorizacion").trim(),
								request.getParameter("passwordAutorizacion").trim(), null, request, response).getUser();
						
						if(userDTO!=null){
							boolean pasoValidacion=true;//variable para el control de la validaci\u00F3n del usuario autorizador
							LogSISPE.getLog().info("USUARIO VALIDO");

							try{
								//se realiza la validaci\u00F3n del usuario autorizado
								AutorizacionesUtil.validarAutorizado(userDTO, request);
								
							}catch(Exception ex){
								UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
								utilPopUp.setMensajeErrorVentana("Los datos no corresponden a un usuario autorizador para el local " + codigoLocalReferencia + ", o es posible que usted no se encuentre autorizado.");
								pasoValidacion=false;
								
								formulario.setOpAutorizacion(estadoInactivo);
							}

							if(pasoValidacion){
								
								//llamada al m\u00E9todo que crea el objeto autorizaci\u00F3n
								AutorizacionDTO autorizacionDTO = null;
//								AutorizacionesUtil.construirNuevaAutorizacion(request,request.getParameter("observacionAutorizacion"),
//										grupoAutorizacion, tipoAutorizacion, SessionManagerSISPE.getCurrentEntidadResponsable(request), codigoLocalReferencia);
								
								//se elimina la ventana de sesi\u00F3n
								session.removeAttribute(SessionManagerSISPE.POPUP);
								session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);//cambios oscar
								session.setAttribute(AUTORIZACION_DESCUENTOS, autorizacionDTO);
								session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
								
								LogSISPE.getLog().info("entra a aplicar los descuentos");
								String [] descSeleccionados = (String []) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);//cambios oscar
								Double porcenDescuento = Double.valueOf(request.getSession().getAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO).toString());
								Double[][]porDes= new Double[1][2];
								porDes[0][0]=porcenDescuento;
								if (descSeleccionados != null) {
									formulario.setOpDescuentos(descSeleccionados);
									formulario.setPorcentajeVarDescuento(porDes);
									request.getSession().removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
									request.getSession().removeAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO);
								}
								
								//se llama al m\u00E9todo que aplica los descuentos
								aceptarDescuento(formulario, request, errors, warnings, exitos, infos);
							}
						}else{
							LogSISPE.getLog().info("USUARIO NO VALIDO");
							UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
							utilPopUp.setMensajeErrorVentana("Los datos del usuario son inv\u00E1lidos");
							
							formulario.setOpAutorizacion(estadoActivo);
						}
					}catch(Exception ex){
						LogSISPE.getLog().info("USUARIO NO VALIDO");
						UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
						utilPopUp.setMensajeErrorVentana("Los datos del usuario son inv\u00E1lidos");
						
						formulario.setOpAutorizacion(estadoActivo);
					}
				}
			}
			//para el caso de autorizacion de consolidacion
			else if(request.getSession().getAttribute("autorizaGerComConsolidacion") != null){
				LogSISPE.getLog().info("Entra a aceptar la autorizacion de consolidacion");
				
				session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
				session.removeAttribute("autorizaGerComConsolidacion");
				session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
				String respuestaAutorizacion  = (String) request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"));
				Boolean validarAutorizacion = AutorizacionesUtil.verificarAutorizacion(respuestaAutorizacion, request, errors, infos);
				if(validarAutorizacion){
					//hay que finalizar el workflow de autorizaciones
					String caraterSeparacionComa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"); // ,
					Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW); 
					if(CollectionUtils.isNotEmpty(autorizacionesFinalizarWorkflow)){
						for(String autorizacionFinalizar  : autorizacionesFinalizarWorkflow){
							
							try{
								
								String[] autorizacion = autorizacionFinalizar.split(caraterSeparacionComa);
								AutorizacionesFactory.getInstancia().getaIAutorizacionesServicio().transFinalizarAutorizacion(autorizacion[0], autorizacion[1],
										Integer.parseInt(autorizacion[2]), autorizacion[3], autorizacion[4]);
								LogSISPE.getLog().info("autorizacion finalizada correctamente: {}",autorizacionFinalizar);
								
							}catch (Exception e){
								
								LogSISPE.getLog().error("Error al finalizar la autorizacion");
								errors.add("casoNoFinalizado", new ActionMessage("errors.gerneral","Error al finalizar el flujo de autorizaciones "+e.getMessage()+"."));
							}
						}
						request.getSession().removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
					}
					
					//se setean las variables para que pueda consolidar el pedido
					session.setAttribute(AUTORIZACION_CONSOLIDAR_MAXIMO,new AutorizacionDTO());
					//se elimina la ventana de la sesi\u00F3n
					exitos.add("exitoAceptarAutorizacion", new ActionMessage("errors.autorizacion.gerenteComercial.creada.exito"));
				}
				else{
					errors.add("Autorizacion",new ActionMessage("errors.autorizacion.invalida"));
					formulario.setOpAutorizacion(estadoInactivo);
				}
				
				salida = "desplegarConsolidarPedido";
			}	
			//aceptar Crear Autorizaci\u00F3n (Descuento Variable)
			else if(request.getParameter(AGREGAR_AUTORIZACION_ARTICULOS)!=null){
				
				AutorizacionesUtil.agregarAutorizacionAlPedido(request, formulario, exitos, errors, warnings);				
			}
			//cancelar la autorizacion
			else if(request.getParameter(VALOR_CANCELAR)!=null){
				LogSISPE.getLog().info("cancela la autorizacion");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);//cambios oscar
				session.removeAttribute(POPUP_DESCUENTO_VARIABLE);
			}
			//cancelar la autorizacion consolidacion
			else if(request.getParameter(VALOR_CANCELAR_CONSOLIDACION)!=null){
				LogSISPE.getLog().info("cancela la autorizacion");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
				session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
				salida="desplegarConsolidarPedido";
			}
			/*---------------------------- cuando se acepta el tipo de descuento --------------------------------*/
			else if(request.getParameter("aplicarDescuento")!=null)
			{
				LogSISPE.getLog().info("entra a aplicar los descuentos");				
				if(validarAutorizadoresDescVariable(formulario, request, infos, errors, warnings, exitos)){
						aceptarDescuento(formulario, request, errors, warnings, exitos, infos);
						request.getSession().setAttribute(CotizarReservarAction.APLICAR_DESCUENTOS_PAVOS,Boolean.FALSE);
					if(errors.isEmpty() && session.getAttribute(POPUP_DESCUENTO_VARIABLE) == null){
						session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);	
						session.removeAttribute(POPUP_DESCUENTO);
					}else if(!errors.isEmpty()){
						//si existen errores se sube a sesion la variable para que no pinte los errores en la pagina de cotizaciones 
						request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
					}
					//cambios oscar
					AutorizacionDTO autorizacion =  (AutorizacionDTO)session.getAttribute(AUTORIZACION_DESCUENTOS);
					if (null == autorizacion) {
						if (session.getAttribute(SessionManagerSISPE.POPUP) != null &&
								"Descuento Variable".equals(((UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP)).getTituloVentana())){
							session.removeAttribute(POPUP_DESCUENTO);
						}
					}
					
					//Cambiar al tab de pedidos
					if (beanSession.getPaginaTab().getTabs().size() != 1 ) {
						ContactoUtil.cambiarTabPedidos(beanSession);
					}
				}
				
				salida = "desplegar";
			}
			/*------------- cuando se quiere eliminar una de las direcciones de entrega o un local ---------------*/
			else if(request.getParameter("numEntrega")!=null)
			{
				//se obtiene el detalle del pedido
				ArrayList detalle = (ArrayList)session.getAttribute(DETALLE_PEDIDO);
				try{
					int numDetalle = Integer.parseInt(request.getParameter("numDetalle"));
					int numEntrega = Integer.parseInt(request.getParameter("numEntrega"));
					//se obtiene el detalle del pedido donde esta el beneficiario que se quiere eliminar
					DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)detalle.get(numDetalle);
					//se obtiene la colecci\u00F3n de c\u00E9dulas de los beneficiarios del art\u00EDculo
					Collection cedulas = detallePedidoDTO.getNpCedulasBeneficiarios();
					//se obtiene la colecci\u00F3n que contiene las direcciones de entrega y los c\u00F3digos de locales
					ArrayList direccionesYLocalesEntrega = (ArrayList)detallePedidoDTO.getNpLocalesDireccionesEntrega();

					//se obtienen las entregas del art\u00EDculo
					ArrayList entregas = (ArrayList)detallePedidoDTO.getEntregaDetallePedidoCol();

					EntregaDetallePedidoDTO entregaDTO = (EntregaDetallePedidoDTO)entregas.get(numEntrega);
					//se obtiene la colecci\u00F3n de beneficiarios
					//TODO: REVISAR FUNCIONARIOS
//					ArrayList beneficiarios = (ArrayList)entregaDTO.getBeneficiarios();
//					if(beneficiarios!=null){
//						Collection cedulasAEliminar = new ArrayList();
//						for(int i=0;i<beneficiarios.size();i++){
//							BeneficiarioSICDTO beneficiarioDTO = (BeneficiarioSICDTO)beneficiarios.get(i);
//							cedulasAEliminar.add(beneficiarioDTO.getCedulaBeneficiario()); 
//						}
//						entregaDTO.setBeneficiarios(null);
//						cedulas.removeAll(cedulasAEliminar);
//					}

					//se actualiza el valor del contador de art\u00EDculos entregados
					long contadorEntrega=detallePedidoDTO.getNpContadorEntrega().longValue() - entregaDTO.getCantidadEntrega().longValue();
					detallePedidoDTO.setNpContadorEntrega(new Long(contadorEntrega));
					//se elimina la entrega seleccionada        
					entregas.remove(numEntrega);
					//se elimina la direcci\u00F3n de entrega o el c\u00F3digo del local
					direccionesYLocalesEntrega.remove(numEntrega);

					salida="desplegar";     
				}catch(NumberFormatException e){
					errors.add("formatoIndice",new ActionMessage("errors.indiceDetalle.formato"));
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
				}
			}
			/*-------- cuando se quiere aplicar una autorizaci\u00F3n ----------*/
			else if(request.getParameter("aplicarAutorizacion")!=null)
			{
				boolean flag = false;
//				Collection<AutorizacionDTO> autorizaciones = (Collection<AutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACION_COL);
//				if(autorizaciones != null && !autorizaciones.isEmpty()){
//					for(AutorizacionDTO autDTO : autorizaciones){
//						LogSISPE.getLog().info("--CodigoAutorizacion--{}",autDTO.getId().getCodigoAutorizacion());
//						LogSISPE.getLog().info("--TipoAutorizacion--{}",autDTO.getSecuencialGrupoTipoAutorizacion());
//						if(autDTO.getSecuencialGrupoTipoAutorizacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.abonoStock"))){
//							flag = true;
//							break;
//						}
//					}
//				}
				//por ahora se valida solo las autorizaciones de abono cero
				if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO)){
					flag = true;
					LogSISPE.getLog().info("Ya existe una autorizacion de tipo abono cero");
					infos.add("autorizacionTipoStockAbono",new ActionMessage("info.autorizacionEstadoPedidoAbonoStockYaExiste"));

				}


				if(!flag){
					LogSISPE.getLog().info("No encontro una autorizacion de tipo Stock o abono");
//					//se llama al m\u00E9todo valida si se puede aplicar una autorizaci\u00F3n
//					AutorizacionesUtil.verificarAutorizacion(request, response, exitos, errors, accion, formulario.getOpAutorizacion(), 
//							formulario.getNumeroAutorizacion(), formulario.getObservacionAutorizacion(), 
//							formulario.getLoginAutorizacion(), formulario.getPasswordAutorizacion());
					AutorizacionesUtil.validarAutorizacionPorNumeroUsuarioContrasenia(formulario, request, exitos, 
							infos, errors,ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO);
				}
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//Cambiar al tab de pedidos
				ContactoUtil.cambiarTabPedidos(beanSession);
			}
			/*-------- cuando se quiere usar una autorizaci\u00F3n externa ----------*/
			else if(request.getParameter(PAR_USAR_AUTORIZACION) != null){
				String parametro = request.getParameter(PAR_USAR_AUTORIZACION);
				if(parametro.equals("2")){
					//se realiza la consulta para llenar el combo de tipo de autorizacion
					TipoAutorizacionDTO tipoAutorizacionDTO = new TipoAutorizacionDTO();
					tipoAutorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					tipoAutorizacionDTO.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
					
					Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCOL = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionDTO);
					tipoAutorizacionDTO = null;
					session.setAttribute(TIPO_AUTORIZACION_COL, tipoAutorizacionDTOCOL);
					
					//se construye la informaci\u00F3n de la ventana
					/*UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Usar Autorizaci\u00F3n Externa");
					popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
					popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
					popUp.setValorOK("requestAjax('crearCotizacion.do', ['mensajes','pregunta'], {parameters: 'aceptarUsoAut=ok', evalScripts:true});ocultarModal();");
					popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['mensajes','pregunta'], {parameters: 'cancelarUsoAut=ok', popWait:false});ocultarModal();");
					popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
					popUp.setContenidoVentana("servicioCliente/autorizacion/usarNumeroAutorizacion.jsp");*/
					//session.setAttribute(SessionManagerSISPE.POPUP, popUp);
					session.setAttribute(POPUPAUTORIZACIONENTREGAS, "ok");
					//popUp = null;
					
					//se inicializan los campos
					formulario.setTipoAutorizacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
					formulario.setNumeroAutorizacion(null);
					formulario.setObservacionAutorizacion(null);
				}
			}
			/*-------- cuando se acepta el uso de una autorizaci\u00F3n externa ----------*/
			else if(request.getParameter(PAR_ACE_USO_AUT) != null){
				//se obtiene el tipo de autorizaci\u00F3n seleccionado				
				String grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
				if(formulario.getTipoAutorizacion()!=null && formulario.getNumeroAutorizacion()!= null){
					if(formulario.getTipoAutorizacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))){
						grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
					}
					try{
						//se realiza la validaci\u00F3n
						AutorizacionDTO autorizacionDTO = null;
//						AutorizacionesUtil.validarNumeroAutorizacion(request, formulario.getNumeroAutorizacion().trim(), formulario.getObservacionAutorizacion(), grupoAutorizacion, formulario.getTipoAutorizacion());
//						AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO, request);
						//session.removeAttribute(SessionManagerSISPE.POPUP);
						session.removeAttribute(POPUPAUTORIZACIONENTREGAS);
						session.removeAttribute(TIPO_AUTORIZACION_COL);
						exitos.add("Autorizacion",new ActionMessage("errors.autorizacion.creada.exito"));
						session.removeAttribute(NUMEROAUTORIZACIONEXTERNAERROR);
					}catch (Exception e) {
						LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
						//se obtiene la ventana popup de la sesi\u00F3n
						/*UtilPopUp popup = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
						popup.setMensajeErrorVentana(MessagesWebSISPE.getString("errors.autorizacion.externa"));*/
						//errors.add("Autorizacion",new ActionMessage("errors.autorizacion.invalida"));
						session.setAttribute(NUMEROAUTORIZACIONEXTERNAERROR, "ok");
					}
				}
			}
			/*-------- cuando se cancela el uso de una autorizaci\u00F3n externa ----------*/
			else if(request.getParameter(PAR_CAN_USO_AUT) != null){
				//session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(POPUPAUTORIZACIONENTREGAS);
				session.removeAttribute(TIPO_AUTORIZACION_COL);
				session.removeAttribute(NUMEROAUTORIZACIONEXTERNAERROR);
			}
			//--------- cuando se desea buscar una persona para agregarla al pedido---------
			else if(request.getParameter("verPopUpClientes") != null){
				LogSISPE.getLog().info("Se procede a presentar ventana emergente...");
				
				UtilPopUp popUp = ClienteUtil.crearPopUpBusquedaClientes();
				//OANDINO: Al ejecutar la acci\u00F3n ACEPTAR en la ventana emergente se actualiza igualmente la secci\u00F3n 'seccion_detalle'
				popUp.setValorOK("requestAjax('crearCotizacion.do',['pregunta','contextoPedido','datosCliente','seccion_detalle'],{parameters: 'aceptarPersona=ok',evalScripts:true});ocultarModal();");
				popUp.setValorCANCEL("requestAjax('crearCotizacion.do',['pregunta'],{parameters: 'cancelarPopUpClientes=ok'});ocultarModal();");
				popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
				
				session.setAttribute(SessionManagerSISPE.POPUP, popUp);			
			}
			//------- busqueda de personas ---------
			else if(request.getParameter("buscarPersonaEmpresa") != null){
				if (StringUtils.isNotEmpty(formulario.getNumeroDocumento()) && StringUtils.isNotEmpty(formulario.getTipoDocumento())) {
					ContactoUtil.buscarPersonaEmpresa(formulario, request, session, response, errors);
					if(session.getAttribute(ContactoUtil.PERSONA)!=null && ((ec.com.smx.corpv2.dto.PersonaDTO)session.getAttribute(ContactoUtil.PERSONA)).getNumeroRuc()==null && session.getAttribute(ContactoUtil.RUC_PERSONA)!=null){
						ContactoUtil.setRucPersona(true);
						((ec.com.smx.corpv2.dto.PersonaDTO)session.getAttribute(ContactoUtil.PERSONA)).setNumeroRuc(session.getAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS).toString());
						salida = ContactoUtil.editarPersona(form, request, request.getSession(), response, errors);
					}
				}else{
					ContactoUtil.consultarCliente(formulario, request, session, errores, errors, accion, estadoActivo, estadoInactivo, response, infos, warnings, beanSession, salida);
				}		
			}
			//------- persona encontrada ----------
			else if(request.getParameter("aceptarPersona") != null){
				LogSISPE.getLog().info("Se procede a llamar a m\u00E9todo aceptarPersona en Reservas...");
				ClienteUtil.asignarPersonaSeleccionada(request, formulario);
				//se actualiza el detalle para verificar si es una empresa que no paga IVA
				Collection detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				formulario.actualizarDetalleForm(request, errores, accion, estadoActivo, estadoInactivo,detallePedido);
				CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
			}
			//------- busqueda de personas cancelada --------
			else if(request.getParameter("cancelarPopUpClientes")!=null){
				LogSISPE.getLog().info("Se cancela el proceso en CotizarReservaAction...");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(PERSONADTO_COL);
				
				//IMPORTANTE
				// Se anulan los valores de paginaci\u00F3n cada vez que se cancela el proceso de b\u00FAsqueda
				session.removeAttribute(ClienteUtil.VAR_START_PAG);
				session.removeAttribute(ClienteUtil.VAR_RANGE_PAG);
				session.removeAttribute(ClienteUtil.VAR_SIZE_PAG);
				//----------
			}
			else	/*-------------------------- cuando se da clic en los campos de paginaci\u00F3n de los pedidos--------------------------------*/
		      if((request.getParameter("range")!=null || request.getParameter("start")!=null) && request.getParameter("pedidos")!=null)
		      {
		      	LogSISPE.getLog().info("Entro a la paginaci\u00F3n de pedidos");
		        //se obtiene el tamano de la coleccion total de articulos
						int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
						LogSISPE.getLog().info("tama\u00F1o: {}" , tamano);
						String start = String.valueOf(request.getParameter("start"));
						LogSISPE.getLog().info("Tipo {}",request.getParameter("pedidos"));
						

						if(tamano > 0){
							//Recupero la vista pedido con los parametros de la busqueda
							VistaPedidoDTO consultaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO);
							//Cambio el inicio de la busqueda
							consultaPedidoDTO.setNpFirstResult(Integer.parseInt(start));
							Collection colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
							
							Collection<VistaPedidoDTO> colRemovePedidos = (ArrayList<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS);
							if(colRemovePedidos != null && !colRemovePedidos.isEmpty()){
								List<VistaPedidoDTO> colNueva = new ArrayList<VistaPedidoDTO>();
								for(VistaPedidoDTO vistaDTO : (ArrayList<VistaPedidoDTO>)colRemovePedidos){
									for(VistaPedidoDTO vistaDTOAux : (ArrayList<VistaPedidoDTO>)colVistaPedidoDTO){
										if(vistaDTO.getId().getCodigoPedido().equals(vistaDTOAux.getId().getCodigoPedido())){
											colNueva.add(vistaDTOAux);
											break;
										}
									}
								}
								if(!colNueva.isEmpty()){
									colVistaPedidoDTO.removeAll(colNueva);
								}
							}
							
							//se asignan las variables de paginaci\u00F3n
							formulario.setStart(start);
							formulario.setRange(request.getParameter("range"));
							formulario.setSize(String.valueOf(tamano));
							formulario.setDatos(colVistaPedidoDTO);
							LogSISPE.getLog().info("tama\u00F1o nueva coleccion: {}" , colVistaPedidoDTO.size());
							
			          //se guarda la colecci\u00F3n general
			          session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
			        //se guarda en sesi\u00F3n el indice de la paginaci\u00F3n
			        session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
				}
				salida = "desplegarConsolidarPedido";
		      }
			// Paginaci\u00F3n de listado original

		      else if(request.getParameter("buscador")== null && request.getParameter("range")!= null && request.getParameter("start")!= null){
		    	  if((request.getSession().getAttribute(CotizarReservarForm.PEDIDOS_ANTERIORES)!=null) 
		    			  && (request.getSession().getAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA)==null))
		    	  {
		    	        LogSISPE.getLog().info("ENTRO A LA PAGINACION");
		    	        Collection datos= (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
		    	        if(datos!=null){
		    	          formulario.setSize(String.valueOf(datos.size()));
		    	          int size= datos.size();
		    	          int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
		    	          int start= Integer.parseInt(request.getParameter("start"));
		    	          formulario.setStart(String.valueOf(start));
		    	          formulario.setRange(String.valueOf(range));
		    	          formulario.setSize(String.valueOf(size));
		    	          
		    	          Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
		    	          ListadoPedidosAction.verificarAutorizacionesPedido(datosSub);
		    	          session.setAttribute(ListadoPedidosAction.COL_SUB_PAGINA,datosSub);
		    	        }
		    	      }
		    	  else{ 
					LogSISPE.getLog().info("Se procede a paginar el listado original en Reservas...");	
					ClienteUtil.paginarListadoPersonas(request, request.getParameter("start"));
		    	  }
				}
			/*-------- CONFIRMACION de la impresi\u00F3n del pedido como texto -------*/
			else if(request.getParameter("confirmarImpresionTexto")!=null){
				LogSISPE.getLog().info("confirmar impresion TEXTO");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//lamada al m\u00E9todo que asigna las variables para la pregunta de confirmaci\u00F3n
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.imprimirPedido",
						"Para imprimir el documento haga click en Aceptar", "siImprimirPedido", null, request);
				//pantalla de resumen
				salida="registro";
			}
			
			//Ver responsables
			else if(request.getParameter("verResponsable") != null){
				LogSISPE.getLog().info("--Entro a ver responsable");
				session.removeAttribute(EntregaLocalCalendarioAction.ESTRUCTURA_RESPONSABLE);
				//se obtine el secuencial del indice que envia desde la jsp.
				String indiceEntrega = String.valueOf(request.getParameter("verResponsable"));
				
				Collection<EstructuraResponsable> detalleEstructuraCol = (ArrayList<EstructuraResponsable>)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
				EstructuraResponsable estructuraResponsable = (EstructuraResponsable)CollectionUtils.get(detalleEstructuraCol, Integer.valueOf(indiceEntrega)-1);
				session.setAttribute(EntregaLocalCalendarioAction.ESTRUCTURA_RESPONSABLE, estructuraResponsable);
				
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Entidad responsable");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
				popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', evalScripts:true});ocultarModal();");
				popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
				popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
				popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarVentanaResponsable.jsp");
				popUp.setAncho(50D);
				popUp.setTope(40D);
				session.setAttribute(SessionManagerSISPE.POPUP, popUp);
				popUp = null;
				//pantalla de resumen
				salida="registro";
			}
			
			else if(request.getParameter("ocultarVentanaResponsable") != null){
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//pantalla de resumen
				salida="registro";
			}
			
			else if(peticion!=null && peticion.equals("siGenerarPDFRes")){
				LogSISPE.getLog().info("entra al reporte pdf");
				String tipoArchivo = "pdf";
				final String NOMBRE_REPORTE = "pedidoRes";
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));
				request.removeAttribute("ec.com.smx.sic.sispe.mostrarConfirmacion");
				salida = "reporteResEntPDF";
			}
			
			/*-------- CONFIRMACION de la generaci\u00F3n del documento como PDF -------*/
			else if(request.getParameter("confirmarPDF")!=null){
				session.removeAttribute(SessionManagerSISPE.POPUP);
				LogSISPE.getLog().info("confirmar impresion PDF");
				//lamada al m\u00E9todo que asigna las variables para la pregunta de confirmaci\u00F3n
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.generarPDF",
						"Para generar el pdf haga click en Aceptar", "siGenerarPDF", null, request);
				//pantalla de resumen      	
				salida="registro";
			}
			/*-------- CONFIRMACION de la generaci\u00F3n del documento como PDF -------*/
			else if(request.getParameter("confirmar")!=null){
				LogSISPE.getLog().info("confirmar impresion PDF");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//lamada al m\u00E9todo que asigna las variables para la pregunta de confirmaci\u00F3n
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.generarPDF",
						"Para generar el pdf haga click en Aceptar", "siGenerarPDF", null, request);
				//pantalla de resumen      	
				salida="registro";
			}
			/*-------- cuando se desea imprimir el pedido como texto ------------*/
			else if(request.getAttribute("imprimirTexto")!=null){
				//remuevo la bandera de la impresion
				request.getSession().removeAttribute(EstadoPedidoUtil.ACCION_ORIGEN);
				//Me indica que proviene de una cotizacion-recotizacion-reserva
		      	request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "COT_PED");
				//se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
				WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, true);

				VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				//se asigna el local responsable, adicionalmente se agrega 
				formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()
						+ " - "+ vistaPedidoDTO.getNombreLocal());
				//pantalla de resumen  
				salida="registro";
			}
			/*-------- cuando se desea generar el pedido en formato PDF ------------*/
			else if(request.getAttribute("formarPDF")!=null){
				//se crea el nombre del archivo
				String nombreArchivo = "";
				if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")))
					nombreArchivo = "reservacion";
				else
					nombreArchivo = "cotizacion";
				
				//remuevo la bandera de la impresion
				request.getSession().removeAttribute(EstadoPedidoUtil.ACCION_ORIGEN);
				//Me indica que proviene de una cotizacion-recotizacion-reserva
		      	request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "COT_PED");

				//se llama al m\u00E9todo que forma el nombre completo del archivo
				String nombreCompletoArchivo = WebSISPEUtil.generarNombreArchivo(nombreArchivo, "pdf");
				//se guarda el nombre del archivo
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", nombreCompletoArchivo);
				//llamada al m\u00E9todo que crea el directorio en el servidor, donde se almacena el archivo
				//que luego ser\u00E1 enviado por mail
				String directorioSalida = WebMensajeriaUtil.crearCarpeta("SISPE") + nombreCompletoArchivo;
				//se guarda el directorio de salida
				session.setAttribute(DIRECTORIO_SALIDA_REPORTE, directorioSalida);
				//session.setAttribute(ARCHIVO_ADJUNTAR,"ok");
				LogSISPE.getLog().info("ANTES DE GENERAR EL ARCHIVO PDF");

				//se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
				WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, false);

				//salida a la p\u00E1gina del PDF	
				salida="reportePDF";
			}
			/*-------- ver listado de  Diferidos -------*/
		      else if(request.getParameter("verDiferido")!=null){
		      	LogSISPE.getLog().info("presion\u00F3 el boton ver diferido");
		      	String tipoReport = "cotizarReservar";
		      	WebSISPEUtil.buscarDiferidos1(request);
		      	WebSISPEUtil.instanciarPopUpDiferidos(request,tipoReport);
		      }
			/*-------- cuando se cancela ventana diferidos----------*/
			  else if(request.getParameter("cancelarDiferido") != null){ 
				LogSISPE.getLog().info("Boton Cancelar");
				session.removeAttribute(SessionManagerSISPE.POPUP);
			  }
			/*-------- cuando se cancela ventana responsables----------*/
			  else if(request.getParameter("cancelarResponsables") != null){ 
				LogSISPE.getLog().info("Boton Cancelar");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//pantalla de resumen
				salida="registro";
			  }
			/*-------- cuando se desea enviar el email del pedido -------*/
			else if(request.getParameter("enviarEmail")!=null){
				//se muestra la ventana de configuraci\u00F3n para el env\u00EDo de email
				UtilPopUp utilPopUp = new UtilPopUp();
				utilPopUp.setTituloVentana("Env\u00EDo del email");
				utilPopUp.setMensajeVentana("Seleccione las opciones con las que desea enviar el pedido.");
				utilPopUp.setFormaBotones(UtilPopUp.OK);
				utilPopUp.setValorOK("siEnviarEmail");
				utilPopUp.setContenidoVentana("reportes/opcionesGenerarPedido.jsp");
				request.setAttribute(SessionManagerSISPE.POPUP, utilPopUp);
			}else if(request.getParameter("redactarMail")!=null){
				LogSISPE.getLog().info("popUp de envio de correo");
				session.setAttribute("ec.com.smx.sic.sispe.redactarMail", "ok");
				VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				session.setAttribute("ec.com.smx.sic.sispe.paraMail","");
				if(vistaPedido!=null){
					ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedido);
					session.setAttribute("ec.com.smx.sic.sispe.textoMail", MessagesWebSISPE.getString("messages.mail.textoMail").replace("{0}", " "+(vistaPedido.getNombreEmpresa()!=null?vistaPedido.getNombreEmpresa():vistaPedido.getNombrePersona())));
					if(vistaPedido.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")) 
							|| vistaPedido.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
							|| vistaPedido.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada"))){
						session.setAttribute("ec.com.smx.sic.sispe.asuntoMail",  "Cotizaci\u00F3n del pedido");
					}else{
						session.setAttribute("ec.com.smx.sic.sispe.asuntoMail",  "Reservaci\u00F3n del pedido");
					}
					
					if(vistaPedido.getNombreEmpresa()!=null && vistaPedido.getCedulaContacto()!=null){
						if(vistaPedido.getEmailContacto()!=null && !vistaPedido.getEmailContacto().isEmpty()){
							session.setAttribute("ec.com.smx.sic.sispe.paraMail", vistaPedido.getEmailContacto());
						}
					}else{
						if(vistaPedido.getEmailPersona()!=null && !vistaPedido.getEmailPersona().isEmpty()){
							session.setAttribute("ec.com.smx.sic.sispe.paraMail",vistaPedido.getEmailPersona());
						}
					}
				}else{
					session.setAttribute("ec.com.smx.sic.sispe.textoMail", MessagesWebSISPE.getString("messages.mail.textoMail").replace("{0}", ""));
					session.setAttribute("ec.com.smx.sic.sispe.asuntoMail",  "");
				}
				
				salida="registro";				
			}
			else if (peticion != null && peticion.equals("xls")){
				LogSISPE.getLog().info("confirmar impresion XSL");
		      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("PedidoXLS", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
		      	salida= "reporteCotizarXLS";
			}

			/**
			 * MUESTRA EL POPUP PARA EDITAR PERSONA
			 */
		    else if(request.getParameter("editarPersona")!=null && request.getParameter("editarPersona").equals("ok")){//--cambios oscar
		    	salida = ContactoUtil.editarPersona(formulario, request, session, response, errors);
			}
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR PERSONA
			 */
		     else if(request.getParameter("registrarPersona")!=null && request.getParameter("registrarPersona").equals("ok")){
				LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR PERSONA del SISTEMA CORPORATIVO");
				salida = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarPersona", formulario,errors);
		    }
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR LOCALIZACION
			 */
		    else if(request.getParameter("registrarLocalizacion")!=null && request.getParameter("registrarLocalizacion").equals("ok")){
		    	LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR LOCALIZACION del SISTEMA CORPORATIVO");
				salida = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarLocalizacion", formulario,errors);
			}
			/**
			 * MUESTRA EL POPUP PARA EDITAR EMPRESA
			 */
		    else if(request.getParameter("editarEmpresa")!=null && request.getParameter("editarEmpresa").equals("ok")){
		    	salida = ContactoUtil.editarEmpresa(response, request, session, salida, formulario,errors);
			}
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR EMPRESA
			 */
		    else if(request.getParameter("registrarEmpresa")!=null && request.getParameter("registrarEmpresa").equals("ok")){
		    	LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR EMPRESA del SISTEMA CORPORATIVO");
				salida = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarEmpresa", formulario,errors);
			}
			
			/**
			 * CIERRA EL POPUP PARA LA INTREGRACION CON JSF
			 */
		    else if(request.getParameter("cerrarPopUpCorporativo") != null){
		    	
				String valorCerrarPopUp = request.getParameter("cerrarPopUpCorporativo");
		    	LogSISPE.getLog().info("cerrarPopUpCorporativo: {}",valorCerrarPopUp);
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
				request.getSession().removeAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS);
				//mensajes al cerrar el popUp de la integracion con el corporativo
				if(!formulario.getNumeroDocumento().isEmpty()){
					if(valorCerrarPopUp.equals("guardar")){
						infos.add("", new ActionMessage("message.integracion.corp.creacion.cliente"));
						
					}else if(valorCerrarPopUp.equals("cancelarCreacion")){
						//cancelacion creacion persona 
						infos.add("", new ActionMessage("message.integracion.corp.cacelacion.creacion"));
						
					}else if(valorCerrarPopUp.equals("modificar")){
						//modificacion persona exitosa 
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.cliente"));
						if(ContactoUtil.isRucPersona()){
							ContactoUtil.setRucPersona(false);
						}
						if(request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null){
							if (request.getSession().getAttribute(ContactoUtil.RUC_PERSONA).equals("ok")){
								ContactoUtil.limpiarContacto(formulario, session);
							}else{
								ContactoUtil.setearValoresPersonaEncontradaEnFormulario(request, formulario, (ec.com.smx.corpv2.dto.PersonaDTO) request.getSession().getAttribute(ContactoUtil.PERSONA));
							}
						}
						
					}else if(valorCerrarPopUp.equals("cancelarModificacion")){
						//cancelacion modificacion persona
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.modificacion"));
						if(ContactoUtil.isRucPersona()){
							ContactoUtil.limpiarContacto(formulario, session);
							ContactoUtil.setRucPersona(false);
						}
						
					}else if(valorCerrarPopUp.equals("registrarEmpresa")){
						//Creacion empresa con exito
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Empresa",""));
						
					}else if(valorCerrarPopUp.equals("editarEmpresa")){
						//modificacion empresa exitosa 
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.empresa.localizacion","Empresa"));
						
					}else if(valorCerrarPopUp.equals("cancelarEdicionEmpresa")){
						//cancelar edicion de empresa 
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Empresa",""));
						
					}else if(valorCerrarPopUp.equals("registrarLocalizacion")){
						//la localizacion se creo exitosamente
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cancelarRegistroLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.creacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("modificarLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cancelarModificarLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.modificacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cerrarPersonaEmpresa")){
						
						//si presiona el bonon cancelar se limpian los datos de la persona/empresa
//						ContactoUtil.limpiarContacto(formulario, session);
//						ContactoUtil.limpiarVariablesFormulario(formulario);
						
						if(StringUtils.isNotEmpty(formulario.getTipoDocumento())){
							if (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
									|| (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null)) {
								if(ContactoUtil.isRucPersona()){
									ContactoUtil.limpiarContacto(formulario, session);
									ContactoUtil.setRucPersona(false);
								}
								ContactoUtil.ejecutarAccionControlador(request, response, "#{personaController.visualizarPersona()}");
							} else if (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
								ContactoUtil.ejecutarAccionControlador(request, response, "#{empresaController.visualizarEmpresa()}");
							}
						}
					}
				}
				else{
					ContactoUtil.limpiarContacto(formulario, session);
				}
//					if(valorCerrarPopUp.equals("cancelarCreacion")){
//						ContactoUtil.mostrarPopUpCorporativo(request,session,"buscarPersona",formulario,errors);
//					}
//					if(valorCerrarPopUp.equals("cerrarPersonaEmpresa")){
//						Map<String, Object> parametrosParaCorp;
//						parametrosParaCorp=ContactoUtil.asignarParametrosCorpVisualizarPersona();
//						formulario.setTipoDocumento("");
//						ContactoUtil.integrarCorporativoJSF(request, session, "visualizarPersona", formulario, null, parametrosParaCorp);
//						ContactoUtil.construirTabsContactoPedido(request, formulario);
//					}
//					formulario.setNombrePersona("");
//					formulario.setNombreEmpresa("");
//					session.removeAttribute(ContactoUtil.PERSONA);
//					session.removeAttribute(ContactoUtil.LOCALIZACION);
//				}
				//se regresa siempre al tab del pedido
				ContactoUtil.cambiarTabContactoPedidos(beanSession, (valorCerrarPopUp.equals("registrarLocalizacion") || valorCerrarPopUp.equals("modificarLocalizacion")) ? 0 : 1);
				
				//actualizar componente de empresas -- ultimos cambios
//				FacesUtil.getDefaultInstance().getFacesContext(request, response);
//				FacesUtil.getDefaultInstance().resetManagedBean("empresaController");
				
				if(session.getAttribute(MOSTRAR_RESUMEN_PEDIDO) != null){
					salida = "registro";
				}

			}
			else if (peticion!=null && peticion.equals("salir")){
				LogSISPE.getLog().info("regresando a la cotizacion {}",peticion);
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
				String valorNA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.noAplica");
				//se guarda en sesi\u00F3n el registro obtenido
				PedidoDTO pedidoDTO= (PedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.clienteOriginal");
				if(pedidoDTO!=null){
					LogSISPE.getLog().info("Obtener datos del cliente desde el pedido");
					//se llenan los campos restantes del cliente
//					formulario.setCedulaContacto(pedidoDTO.getCedulaContacto());
//					formulario.setNombreContacto(pedidoDTO.getNombreContacto());
//					formulario.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
					formulario.setNumeroDocumento(pedidoDTO.getNpTipoDocumento());
					//para el contexto del pedido EMPRESARIAL o INDIVIDUAL
					formulario.setOpTipoDocumento(pedidoDTO.getContextoPedido());
	
					//datos de la empresa
//					if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//						formulario.setRucEmpresa(pedidoDTO.getRucEmpresa());
//					if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//						formulario.setNombreEmpresa(pedidoDTO.getNombreEmpresa());
				}else{
					LogSISPE.getLog().info("Obtener datos del cliente desde la vista del pedido");
					String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
					//se guarda en sesi\u00F3n el pedido seleccionado
					VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
					if(vistaPedidoDTO!=null){
						/*------------------- se asignan los atributos necesarios al formulario --------------------*/
						LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
						//se asigna el contexto del pedido al formulario
						if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
							formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
							formulario.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
							formulario.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());						
				             formulario.setTelefonoEmpresa(vistaPedidoDTO.getTelefonoEmpresa());
				             formulario.setNumeroDocumento(vistaPedidoDTO.getRucEmpresa());
				             //se eliminan los datos del contacto
				             formulario.setNumeroDocumentoContacto(null);
				       		 formulario.setNombreContacto(null);
				        	 formulario.setTelefonoContacto(null);
				     		 formulario.setEmailContacto(null);  				     		 
						}else{
							formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
				    		formulario.setNumeroDocumento(vistaPedidoDTO.getCedulaContacto());
				            formulario.setNombrePersona(vistaPedidoDTO.getNombreContacto());
				            formulario.setTelefonoPersona(vistaPedidoDTO.getTelefonoContacto());     
				            formulario.setNumeroDocumentoPersona(vistaPedidoDTO.getCedulaContacto());
				            formulario.setTipoDocumentoPersona(vistaPedidoDTO.getTipoDocumentoCliente());
				            //se eliminan los datos de la empresa
				            formulario.setRucEmpresa(null);
				            formulario.setNombreEmpresa(null);
				            formulario.setTelefonoEmpresa(null);
						}
						formulario.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
						if(vistaPedidoDTO.getNumBonNavEmp()!=null){
							formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp().toString());
						}
	
//						//se asigna el tipo de documento del cliente al formulario
//						if(vistaPedidoDTO.getTipoDocumentoCliente().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"))){
//							formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
//						}else{
//							formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE);
//						}
	
						formulario.setOpTipoEspeciales("0");
						formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
						formulario.setOpAutorizacion(estadoInactivo);
	
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
					}
				}
				
				//session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
				//Seleccionar los pedidos ya consolidados
		      	Collection<VistaPedidoDTO> visPedColAux=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarColAux");
		      	LogSISPE.getLog().info("regresar los valores anteriores {}",visPedColAux==null?visPedColAux:visPedColAux.size());
		      	//request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol",visPedColAux);
		      	formulario.setDatosConsolidados(visPedColAux);
		      	CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
		      	//eliminar variable de session de pedidos consolidados eliminados
		      	session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
		      	session.setAttribute("ec.com.smx.sic.sispe.accion",session.getAttribute(ACCION_ANTERIOR));
		      	session.removeAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
				salida="desplegar";
			}/*--------------------------------- cuando se busca un pedido desde consolidacion--------------------------------------*/
		    else if(request.getParameter("buscar") != null){
		    	  buscarPedidosConsolidados(request, infos, errors, formulario,
						session);
		    	  salida = "desplegarConsolidarPedido";
		    }		
			/*--------------------------------- cuando se presiona en el boton de consolidar--------------------------------------*/
		    else if(request.getParameter("guardarConsolidacion") != null){
		    	 LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		    	 LogSISPE.getLog().info("MostrarPopUpConfirmacion");
		    	 String mensajeConf=MessagesWebSISPE.getString("message.confirmacion.consolidacion");
		    	 session.setAttribute( POSICION_DIV ,request.getParameter( "posicionScroll" ));
		    	 //cambios oscar
		    	 session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
		    	 if(formulario.getDatosConsolidados().size()>0){
						String []pedidosValidos=formulario.getPedidosValidados();
						int posicion=0;
						Boolean pedidoEncontrado=Boolean.FALSE;
						for(int i=0;i<pedidosValidos.length;i++){
							if(pedidosValidos[i]!=null && pedidosValidos[i].equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
								posicion=i;
								pedidoEncontrado=Boolean.TRUE;
								break;
							}
						}
						if(pedidoEncontrado){

					    	 if(ConsolidarAction.tieneAutorizacion){
					    		//validar que tenga pedidos a consolidar
						    	 if(formulario.getDatosConsolidados()==null){
								    	 mensajeConf="Est\u00E1 seguro(a) que desea eliminar todos los pedidos a consolidar?";
								    	 WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"confirmarEliminarConsolidacion",null,request);  
								 }
						    	 else{
						    	  WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"confirmarGuardarConsolidacion",null,request);  
						    	 }
						    	 exitos.add("autorizacionConsolidarPedido",new ActionMessage("info.consolidacionPedidos.autorizado"));						    	 
						    	 salida = "desplegarConsolidarPedido";
					    	 }else {
					    		 session.setAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2,"2");
								 LogSISPE.getLog().info("va a abrir la ventana de autorizacion para consolidacion");					  	
								 // se llama al popUp de autorizacion
								 instanciarPopUpAutorizacion(request,"1","");
								 salida = "desplegarConsolidarPedido";
							}
						}else{
							  infos.add("consolidarPedido",new ActionMessage("message.numeropedidos.seleccionado.consolidar"));
							  salida = "desplegarConsolidarPedido";
						 }
		    	 } else{
					  infos.add("consolidarPedido",new ActionMessage("message.numeropedidos.seleccionado.consolidar"));
					  salida = "desplegarConsolidarPedido";
				 }
		    	 
		    } 
		    else if(request.getParameter("cancelarEliminacionPedido")!=null){
		    	LogSISPE.getLog().info("cancelarEliminacionPedido");
		    	session.removeAttribute(SessionManagerSISPE.POPUP);
		    	salida = "desplegarConsolidarPedido";
		    }
			/*--------------------------------- cuando se presiona en el boton de confirmar consolidacion--------------------------------------*/
		      else if(request.getParameter("confirmarEliminarConsolidacion")!=null){
		    	  LogSISPE.getLog().info("confirmarEliminarConsolidacion");
		    	  LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		    	  //obtener de sesion en el caso de que ya se haya seleccionado pedidos a cotizar
					List<VistaPedidoDTO> listDatosConsolidados= (List<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.listDatosConsolidados");
					
					//Colecciones principales a eliminar por no tener pedidos asociados
					Collection<VistaPedidoDTO> visPedEliminarCol= (Collection<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidosEliminadosCol");
					Collection<VistaPedidoDTO> visPedConsolidarEliminarCol= (Collection<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidosEliminadosConsolidadosCol");
					
					for(VistaPedidoDTO vistaPedidoDTO:visPedConsolidarEliminarCol){
						for(VistaPedidoDTO listDatosPedidoDTO:listDatosConsolidados){
							if(vistaPedidoDTO.getId().getCodigoPedido().equals(listDatosPedidoDTO.getId().getCodigoPedido())){
								listDatosConsolidados.remove(listDatosPedidoDTO);
								break;
							}
						}
					}
					if(listDatosConsolidados.size()==0){
						formulario.setDatosConsolidados(null);
					}
					String []validarPedidos=new String[listDatosConsolidados.size()];
			        String []numeroPedidoConsolidado=new String[listDatosConsolidados.size()];
			        //eliminar de sesion el pedido seleccionado como consolidado
			        request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
					 int iPedidos=0;
			            for(VistaPedidoDTO visPedDTO:listDatosConsolidados){
			            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
			            	validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
			            	numeroPedidoConsolidado[iPedidos]=null;
			            	iPedidos++;
			            }
			            formulario.setPedidosValidados(validarPedidos);
			            formulario.setNumeroConsolidado(numeroPedidoConsolidado);
				
			
			if(visPedEliminarCol!=null && visPedEliminarCol.size()>0){
				Collection<VistaPedidoDTO> visPedEliminarColAct= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
				if(visPedEliminarColAct==null){
					visPedEliminarColAct= new ArrayList<VistaPedidoDTO>();
				}
				visPedEliminarColAct.addAll(visPedEliminarCol);
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS,visPedEliminarColAct);
			}
			//verificar si no se ha seleccionado ningun pedido a consolidar
			
//			 	if(formulario.getDatos()!=null && formulario.getDatos().size()>0) {
//					List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(formulario.getDatos());
//					listDatos.addAll(visPedEliminarCol);
//					formulario.setDatos(listDatos);
//				}else{
//					List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(visPedEliminarCol.size());
//					listDatos.addAll(visPedEliminarCol);
//					formulario.setDatos(listDatos);
//				}
//			 	//agrego para a la session para el link del pedido
//			 	session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, formulario.getDatos());
//			 	if(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null){
//		    		  int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
//		    		  tamano = tamano + visPedEliminarCol.size();
//		    		  session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, tamano);	   
//		    	}else{
//		    		session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, visPedEliminarCol.size());
//		    	}
			 	
				//eliminar empresas, en el caso de q elimine pedidos
				Collection<VistaPedidoDTO> visPedEliminarColAct= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
				session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS);
				
				//agregar los pedidos sobrantes a la coleccion de consolidados
				if(formulario.getDatosConsolidados()!=null){
					formulario.setDatosConsolidados(listDatosConsolidados);
				}
				//determinar si se aplican precios de afiliado o no 
				Boolean sinPrecioAfiliado=Boolean.TRUE;
				for(VistaPedidoDTO visPedDTO:visPedEliminarCol){
					if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
		    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
		    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
		    			sinPrecioAfiliado=Boolean.FALSE;
		    			break;
		    		}
				}
				if(sinPrecioAfiliado){
	    			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
				}
				//desconsolidar los pedidos desde base
				if(visPedEliminarCol!= null && !visPedEliminarCol.isEmpty()){
					Collection<DetallePedidoDTO> detallePedidoConsolidadoEliminarCol=ConsolidarAction.construirDetallesPedidoDesdeVistaConsolidados(formulario, request,visPedEliminarColAct,Boolean.TRUE,Boolean.TRUE, errors);
					//se dejan las autorizaciones en su estado original
					AutorizacionesUtil.asignarAutorizacionesOriginalesDesconsolidados(request, detallePedidoConsolidadoEliminarCol);
					session.removeAttribute(CotizarReservarAction.RESPALDO_OP_DESCUENTOS);//eliminar la coleccion de seleccionados para que al momento de desconsolidar no asigne los descuentos automaticos CAJ-MAY
					ConsolidarAction.eliminarDescuentosConsolidados(request,session, visPedEliminarCol,detallePedidoConsolidadoEliminarCol);
					 //trans guardar pedidos
					 try{
						 Collection<PedidoDTO> pedidoColConsolidadoEliminado=(Collection<PedidoDTO>) session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						 PedidoDTO pedidoDTO = new PedidoDTO();
						//se obtiene la vistaPedidoDTO
						VistaPedidoDTO vistaPedidoDTO = visPedEliminarCol.iterator().next();
						//cuando se guarda la modificaci\u00F3n de una cotizaci\u00F3n anterior
						pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
						pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
						
						pedidoDTO.setUserId(vistaPedidoDTO.getUserId()); //campos de auditoria
						pedidoDTO.setUsuarioRegistro(vistaPedidoDTO.getUserId());
						//verificar si los pedidos esta consolidados para eliminar, caso contrario solo se quitaran de la lista
						Boolean desconsolidarPedidos=Boolean.TRUE;
						for(PedidoDTO pedDTO:pedidoColConsolidadoEliminado){
							if(pedDTO.getCodigoConsolidado()==null){
								desconsolidarPedidos=Boolean.FALSE;
								break;
							}
						}
						if(desconsolidarPedidos){
							if(CollectionUtils.isNotEmpty(visPedEliminarCol)){
								for (VistaPedidoDTO visPedDTO: visPedEliminarCol){
									for (PedidoDTO pedDTO: pedidoColConsolidadoEliminado){
										if(visPedDTO.getId().getCodigoPedido().equals(pedDTO.getId().getCodigoPedido())){
											pedDTO.setNpNumBonNavEmp(visPedDTO.getNumBonNavEmp());
											break;
										}
									}	
								}
							}
							
							//se desactivan las autorizaciones que fueron eliminadas
							Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
							if(CollectionUtils.isNotEmpty(autorizacionesDesactivarCol)){
								pedidoDTO.setNpAutorizacionesDesactivarCol(autorizacionesDesactivarCol);
								AutorizacionesUtil.crearAutorizacionesFinalizarWorkflow(autorizacionesDesactivarCol, request);
							}
							
							//se activan las autorizaciones que fueron eliminadas
							Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesActivarCol = (HashSet<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTIVAR_COL);
							if(CollectionUtils.isNotEmpty(autorizacionesActivarCol)){
								if(CollectionUtils.isEmpty(pedidoDTO.getNpAutorizacionesDesactivarCol())){
									pedidoDTO.setNpAutorizacionesDesactivarCol(new HashSet<DetalleEstadoPedidoAutorizacionDTO>());
								}
								pedidoDTO.getNpAutorizacionesDesactivarCol().addAll(autorizacionesActivarCol);
							}
							
							SessionManagerSISPE.getServicioClienteServicio().transGuardarConsolidacion(pedidoDTO, null, null, pedidoColConsolidadoEliminado, detallePedidoConsolidadoEliminarCol);
							//---------wc actualizar pedidos que fueron desconsolidados--------//
							Collection collectionVistaPedidoDTOsAux = new ArrayList();
							if(visPedEliminarCol!=null && !visPedEliminarCol.isEmpty()){
								//Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);							
								for(VistaPedidoDTO vistaPedidoDTOit:visPedEliminarCol){								
									VistaPedidoDTO vistaPedidoDTOaux = new VistaPedidoDTO();
									vistaPedidoDTOaux.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
									vistaPedidoDTOaux.getId().setCodigoPedido(vistaPedidoDTOit.getId().getCodigoPedido());
									vistaPedidoDTOaux.setCodigoConsolidado(null);
									vistaPedidoDTOaux.setEstadoActual("SI");
									collectionVistaPedidoDTOsAux.add(SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoDTOaux).iterator().next());
								}
								
								if(formulario.getDatos()!=null && formulario.getDatos().size()>0) {
									List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(formulario.getDatos());
									//listDatos.addAll(visPedEliminarCol);
									listDatos.addAll(collectionVistaPedidoDTOsAux);
									formulario.setDatos(listDatos);
								}else{
									List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(visPedEliminarCol.size());
									//listDatos.addAll(visPedEliminarCol);
									listDatos.addAll(collectionVistaPedidoDTOsAux);
									formulario.setDatos(listDatos);
								}
								
							}//---------wc actualizar pedidos que fueron desconsolidados--------//
						}else {
							if(formulario.getDatos()!=null && formulario.getDatos().size()>0) {
								List listDatos=new ArrayList(formulario.getDatos());
								listDatos.addAll(visPedEliminarCol);
								
								formulario.setDatos(listDatos);
							}else{
								List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(visPedEliminarCol.size());
								listDatos.addAll(visPedEliminarCol);								
								formulario.setDatos(listDatos);
							}	
						}
						
						//agrego para a la session para el link del pedido
					 	session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, formulario.getDatos());
					 	if(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null){
				    		  int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
				    		  tamano = tamano + visPedEliminarCol.size();
				    		  session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, tamano);	   
				    	}else{
				    		session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, visPedEliminarCol.size());
				    	}
						
						session.removeAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						session.removeAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
						session.removeAttribute(COL_DESCUENTOS);
						session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
						session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTIVAR_COL);
						//eliminar de la coleccion de consolidados y agregar a la coleccion de pendientes por consolidar
						infos.add("consolidarPedido",new ActionMessage("message.desconsolidar.pedidos"));
					 }
					 catch (Exception ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("registrarConsolidacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la desconsolidaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en GUARDAR"));
						errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					 }
				}
				//buscarPedidosConsolidados(request, infos, errors, formulario, session);
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegarConsolidarPedido";
		    	  //salida = "desplegar";
		      }
			/*--------------------------------- cuando se presiona en el boton de confirmar consolidacion con descuentos--------------------------------------*/
		      else if(request.getParameter("aceptarDescuentos")!=null && request.getParameter("aceptarDescuentos").equals("ok")){
		    	  LogSISPE.getLog().info("confirmarConsolidacionDescuentos");
		    	  try{
		    		    session.setAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS, Boolean.TRUE);
		    		    session.setAttribute(SIN_DESCUENTOS_CONSOLIDADO, Boolean.FALSE);
				     	//recuperando las variables de sesion por defecto
						formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
						String valorNA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.noAplica");
						//se guarda en sesi\u00F3n el registro obtenido
						PedidoDTO pedidoDTO= (PedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.clienteOriginal");
						if(pedidoDTO!=null){
							//se llenan los campos restantes del cliente
//							formulario.setCedulaContacto(pedidoDTO.getCedulaContacto());
//							formulario.setNombreContacto(pedidoDTO.getNombreContacto());
//							formulario.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
							formulario.setTipoDocumento(pedidoDTO.getNpTipoDocumento());
							//para el contexto del pedido EMPRESARIAL o INDIVIDUAL
							formulario.setOpTipoDocumento(pedidoDTO.getContextoPedido());
			
							//datos de la empresa
//							if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//								formulario.setRucEmpresa(pedidoDTO.getRucEmpresa());
//							if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//								formulario.setNombreEmpresa(pedidoDTO.getNombreEmpresa());
						}
						else{
							String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
							//se guarda en sesi\u00F3n el pedido seleccionado
							VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
							if(vistaPedidoDTO!=null){
								/*------------------- se asignan los atributos necesarios al formulario --------------------*/
								formulario.setNumeroDocumento(vistaPedidoDTO.getCedulaContacto());
								formulario.setNombreContacto(vistaPedidoDTO.getNombreContacto());
								formulario.setTelefonoContacto(vistaPedidoDTO.getTelefonoContacto());
								LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
								//se asigna el contexto del pedido al formulario
								if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
									formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
									formulario.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
									formulario.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
								}else{
									formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
								}
		
								//se asigna el tipo de documento del cliente al formulario
								if(vistaPedidoDTO.getTipoDocumentoCliente().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"))){
									formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
								}else{
									formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE);
								}
		
								formulario.setOpTipoEspeciales("0");
								formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
								formulario.setOpAutorizacion(estadoInactivo);
		
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
							}
						}
						//agregar solo el pedido consolidado
						Collection<VistaPedidoDTO> visPedColConsAux = (Collection<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS);
						
//						List<VistaPedidoDTO> listDatosConsolidados=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados());
//						Collection<VistaPedidoDTO> pedidoConsolidado=new ArrayList<VistaPedidoDTO>(1);
//						String []numConsolidados=formulario.getNumeroConsolidado();
//						formulario.setNumeroPedidoConsolidado(formulario.getNumeroAConsolidar());
//						for(int iConsolidado=0;iConsolidado<numConsolidados.length;iConsolidado++){
//							LogSISPE.getLog().info("numConsolidados[{}]: {}",iConsolidado,numConsolidados[iConsolidado]);
//							if(numConsolidados[iConsolidado]!=null){
//								VistaPedidoDTO visPedDTO=listDatosConsolidados.get(iConsolidado);
//								pedidoConsolidado.add(visPedDTO);
//								formulario.setNumeroPedidoConsolidado(visPedDTO.getId().getCodigoPedido());
//								break;
//							}
//						}
					 
					  //obtener todos los detalles de los pedidos consolidados
					  //session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
					  LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
					  //if(formulario.getDatosConsolidados().size()>0){
					  if(visPedColConsAux!=null && !visPedColConsAux.isEmpty()){
						  formulario.setNumeroPedidoConsolidado(visPedColConsAux.iterator().next().getId().getCodigoPedido());
						  formulario.setDatosConsolidados(visPedColConsAux);
//						//se obtiene la colecci\u00F3n de los pedidos 
//						List pedidos =(List) formulario.getDatosConsolidados();
//						LogSISPE.getLog().info("total de pedidos consolidados Util: {}",pedidos.size());
//						//creaci\u00F3n del DTO para almacenar el pedido selecionado
//						VistaPedidoDTO vistaPedidoActDTO = (VistaPedidoDTO)pedidos.get(0);
//						//obtener todos los pedidos consolidados
//						Collection<VistaPedidoDTO> visPedColConsAux = vistaPedidoActDTO.getVistaPedidoDTOCol();
//						//determinar si se aplican precios de afiliado o no 
						Boolean sinPrecioAfiliado=Boolean.TRUE;
						for(VistaPedidoDTO visPedDTO:visPedColConsAux){
							if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
				    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
				    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
				    			sinPrecioAfiliado=Boolean.FALSE;
				    			break;
				    		}
						}
						if(sinPrecioAfiliado){
			    			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
						}
						
						Collection descuentosAux = null;
						String codigoTipoDescuentoVariable = "";
						if(formulario.getOpDescuentoSeleccionadoConsolidado()!=null && !formulario.getOpDescuentoSeleccionadoConsolidado().equals("")){
							String descuentoSeleccionado= formulario.getOpDescuentoSeleccionadoConsolidado()[0];
							if(descuentoSeleccionado.equals("")){
								//se eliminan los descuentos variables y las autorizaciones de los detalles
		    					session.setAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS, Boolean.FALSE);
								session.setAttribute(SIN_DESCUENTOS_CONSOLIDADO, Boolean.TRUE);
		    					session.removeAttribute(COL_DESCUENTOS_VARIABLES);
		    					
		    					//Si no existen descuentos se deshabilitan los descuentos automaticos por caja y mayorista
			    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
			    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA, "NO");
							}
							else{
								String[] tipoDescuento=descuentoSeleccionado.split(SEPARADOR_TOKEN);
								
								if(tipoDescuento[0].equals("PO")){
									descuentosAux = (Collection)session.getAttribute(COL_DESCUENTOS);
									
									//variable para que si existen autorizaciones pendientes no se pierdan
									session.setAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS, Boolean.FALSE);
								}
								else{
									String pedido=tipoDescuento[1];
									for(VistaPedidoDTO visPedDTO:visPedColConsAux){
										if(visPedDTO.getId().getCodigoPedido().equals(pedido)){
											 descuentosAux =visPedDTO.getDescuentosEstadosPedidos();
											 break;
										}
									}
								}
								// se busca si hay descuentos variables
								if(CollectionUtils.isNotEmpty(descuentosAux)){
									
									ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
									if(parametroDTO.getValorParametro()!=null){
										codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
									}
									//se verifica si es un descuento de tipo variable
									for(DescuentoEstadoPedidoDTO descuentoAct : (Collection<DescuentoEstadoPedidoDTO>) descuentosAux){
										if(descuentoAct.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoVariable)){
											session.removeAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS);
											session.setAttribute("existe.descuentos.variables.normales", true);
											break;
										}
									}
									ConsolidarAction.crearParametrosDescuentos(request, session, descuentosAux);
								}
							}
						}
						
						List<DetallePedidoDTO> detallePedido=ConsolidarAction.construirDetallesPedidoDesdeVistaConsolidados(formulario, request,visPedColConsAux,Boolean.FALSE,Boolean.FALSE, errors);
						AutorizacionesUtil.agregarAutorizacionSimilaresDescVar(request, (ArrayList<DetallePedidoDTO>)detallePedido);
						CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
						
						VistaPedidoDTO vistaPedidoActual= (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						if(!accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
							if(vistaPedidoActual!=null){
								visPedColConsAux.add(vistaPedidoActual);
								Collection<DetallePedidoDTO> detallePedidoActual= (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
								if(detallePedidoActual!=null){
									detallePedido.addAll(detallePedidoActual);
								}
							}
						}
						session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallePedido);
						session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,visPedColConsAux);
						
						Collection<VistaPedidoDTO> vistaPedidoTotal=new ArrayList<VistaPedidoDTO>(visPedColConsAux.size()+1);
						vistaPedidoTotal.addAll(visPedColConsAux);
						session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL,vistaPedidoTotal);
						formulario.setDatosConsolidadosTotal(vistaPedidoTotal);
						
						if(formulario.getOpDescuentoSeleccionadoConsolidado()!=null && !formulario.getOpDescuentoSeleccionadoConsolidado().equals("")){
							String descuentoSeleccionado= formulario.getOpDescuentoSeleccionadoConsolidado()[0];
							if(!descuentoSeleccionado.equals("")){
								String[] tipoDescuento=descuentoSeleccionado.split(SEPARADOR_TOKEN);
								if(tipoDescuento[0].equals("PO")){
									Collection descuentos = (Collection)session.getAttribute(COL_DESCUENTOS);
									ConsolidarAction.crearParametrosDescuentos(request, session, descuentos);
								}
								else{
									String pedido=tipoDescuento[1];
									List<VistaPedidoDTO> visPedConDesc= (List<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
									for(VistaPedidoDTO visPedDTO:visPedConDesc){
										if(visPedDTO.getId().getCodigoPedido().equals(pedido)){
											Collection descuentos =visPedDTO.getDescuentosEstadosPedidos();
											ConsolidarAction.crearParametrosDescuentos(request, session, descuentos);
											//validacion Check-Pago Efectivo
											if(descuentos != null && !descuentos.isEmpty()){
												session.setAttribute(COL_DESCUENTOS, descuentos);
												CotizacionReservacionUtil.validarCheckMaxiNavidad(request, descuentos, formulario);
											}
										}
									}
								}
							}else{
								session.removeAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
								session.removeAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
								//obtengo el codigoTipDesMax-navidad desde un parametro
								CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
								//Opcion escogio sin descuentos
								formulario.setCheckPagoEfectivo(null);
		    					session.removeAttribute(DES_MAX_NAV_EMP);
		    					session.removeAttribute(CHECK_PAGO_EFECTIVO);
		    					
		    					//se guardan los detallesEstadoPedidoAutorizacionDTO que van a ser eliminados para cambiar su estado a inactivo
		    					CotizacionReservacionUtil.obtenerAutorizacionesInactivas(request, detallePedido, Boolean.TRUE);
							}
						}
						//jmena
						CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
//						AutorizacionesUtil.eliminarAutorizacionesNoSonConsolidadas(request, detallePedido);
						ConsolidarAction.crearPedidoGeneral(request, formulario, session,detallePedido,infos,errores,errors,warnings,accion,estadoActivo,estadoInactivo,Boolean.TRUE);
						CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, (Collection<DetallePedidoDTO>) session.getAttribute(DETALLE_PEDIDO));
						Collection descuentos = (Collection)session.getAttribute(COL_DESCUENTOS);
						//validacion Check-Pago Efectivo
						if(descuentos != null && !descuentos.isEmpty()){
							CotizacionReservacionUtil.validarCheckMaxiNavidad(request, descuentos, formulario);
							CotizacionReservacionUtil.agregarDescripcionDescuentosVariables(descuentos, request);
						}
					  }
					  else{
						  session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
						  session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
						  //obtengo el codigoTipDesMax-navidad desde un parametro
						  CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
						  //desabilita el check pago en efectivo
						  formulario.setCheckPagoEfectivo(null);
						  session.removeAttribute(DES_MAX_NAV_EMP);
						  session.removeAttribute(CHECK_PAGO_EFECTIVO);
					  }
					  //session.removeAttribute(SessionManagerSISPE.POPUP);
					  //cambios oscar
					  PaginaTab  tabsConsolidacion = new PaginaTab("crearCotizacion", "deplegar", 1, 410, request);
					  Tab tabPedidos = new Tab("Detalle del Pedido","crearCotizacion","/servicioCliente/cotizarRecotizarReservar/detallePedido.jsp",true);
					  tabsConsolidacion.addTab(tabPedidos);
					  beanSession.setPaginaTab(tabsConsolidacion);
			    	salida = "desplegar";
		    	}
		  		catch (Exception e){
		  			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		  			
		  			ActionMessages errorsArticulos = (ActionMessages) request.getAttribute("ec.com.smx.sic.sispe.errores.articulos");
					if(errorsArticulos != null && !errorsArticulos.isEmpty()){
						errors = errorsArticulos;
					}
		  			errors.add("SISPEException",new ActionMessage("errors.SISPEException",e.getMessage()));
		  			formulario.setDatosConsolidados((ArrayList<VistaPedidoDTO>)session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX));
		  			session.removeAttribute(COL_DESCUENTOS);
		  			session.removeAttribute(SessionManagerSISPE.POPUP);
		  			salida = "desplegarConsolidarPedido";
		  		}
		  		if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS) == null){
		  			session.removeAttribute(SessionManagerSISPE.POPUP);
		  		}
		      }
			/*--------------------------------- cuando se presiona en el boton de confirmar consolidacion con descuentos--------------------------------------*/
		      else if(request.getParameter("cancelarDescuentos")!=null && request.getParameter("cancelarDescuentos").equals("ok")){
		    	  session.removeAttribute(SessionManagerSISPE.POPUP);
		    	  salida = "desplegarConsolidarPedido";
		      }
			/*--------------------------------- cuando se presiona en el boton de confirmar consolidacion--------------------------------------*/
		      else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("confirmarGuardarConsolidacion")){
		    	  LogSISPE.getLog().info("confirmarGuardarConsolidacion");
		    	  removerVariablesSession(session);
		    	  session.setAttribute(ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
		    	  session.setAttribute("ec.com.smx.sic.sispe.primer.ingreso.consolidacion", "ok");
		    	  session.setAttribute(OCULTAR_BOTON_DESCUENTOS, "ok");
		    	  session.setAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS, Boolean.TRUE);
				  
		    	  session.removeAttribute(COL_DESCUENTOS_VARIABLES); //
				  session.removeAttribute(COL_DESC_SELECCIONADOS); //opDescuentos
				  session.removeAttribute(COL_DESCUENTOS);
				  session.removeAttribute(RESPALDO_OP_DESCUENTOS);
				  session.removeAttribute(RESPALDO_DESCUENTOS_SELECCIONADOS);
				  session.removeAttribute(RESPALDO_DESCUENTOS_CONSOLIDADOS);
				  
		    	  session.removeAttribute(DETALLES_CONSOLIDADOS_ACTUALES);
		    	  session.removeAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		    	  session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
		    	  session.removeAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
		    	  session.removeAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE);
		    	  session.removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS); //laves de los descuentos
		    	  
				  if(formulario.getDatosConsolidados().size()>0){
					String []pedidosValidos=formulario.getPedidosValidados();
					int posicion=0;
					Boolean pedidoEncontrado=Boolean.FALSE;
					for(int i=0;i<pedidosValidos.length;i++){
						if(pedidosValidos[i]!=null && pedidosValidos[i].equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
							posicion=i;
							pedidoEncontrado=Boolean.TRUE;
							break;
						}
					}
										
					if(pedidoEncontrado){	
						
						//se obtiene la colecci\u00F3n de los pedidos 
						List pedidos =(List) formulario.getDatosConsolidados();
						LogSISPE.getLog().info("total de pedidos consolidados Util: {}",pedidos.size());
						//creaci\u00F3n del DTO para almacenar el pedido selecionado
						VistaPedidoDTO vistaPedidoActDTO = (VistaPedidoDTO)pedidos.get(posicion);
						//obtener todos los pedidos consolidados
						Collection<VistaPedidoDTO> visPedColConsAux = (Collection<VistaPedidoDTO>)SerializationUtils.clone((Serializable)vistaPedidoActDTO.getVistaPedidoDTOCol());
						  
						  if(visPedColConsAux!=null && visPedColConsAux.size()>1){
							  Collection<DescuentoEstadoPedidoDTO> desEstPedCol = (Collection)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_PEDIDO_GENERAL);
							  Collection<DescuentoEstadoPedidoDTO> desEstPedConCol = (Collection)session.getAttribute(COL_DESCUENTOS);
							  Boolean existenDescuentos = Boolean.FALSE;
							  DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = null; 
							  
							  if(desEstPedCol == null || (desEstPedCol != null && desEstPedCol.isEmpty())){
								  descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
								  for(VistaPedidoDTO vistaPedidoDTO:visPedColConsAux){
						    			//se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
						    			LogSISPE.getLog().info("codigoLocal: {}",vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						    			LogSISPE.getLog().info("codigoPedido: {}",vistaPedidoDTO.getId().getCodigoPedido());
						    			LogSISPE.getLog().info("codigoEstado: {}",vistaPedidoDTO.getId().getCodigoEstado());
						    			LogSISPE.getLog().info("secuencialEstadoPedido: {}",vistaPedidoDTO.getId().getSecuencialEstadoPedido());
						    			
						    			descuentoEstadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
						    			descuentoEstadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
						    			descuentoEstadoPedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
						    			descuentoEstadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						    			descuentoEstadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
						    			
						    			if(vistaPedidoDTO.getCodigoConsolidado() != null){
						    				descuentoEstadoPedidoDTO.getId().setEsDescuentoConsolidado(estadoInactivo);
						    			}else{
						    				descuentoEstadoPedidoDTO.getId().setEsDescuentoConsolidado(estadoActivo);
						    			}
//						    			descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoInactivo);
						    			descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
						    			descuentoEstadoPedidoDTO.getDescuentoDTO().setTipoDescuentoDTO(new TipoDescuentoDTO());
						    			desEstPedCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoEstadoTipoPedido(descuentoEstadoPedidoDTO);
						    			if(desEstPedCol != null && !desEstPedCol.isEmpty()){ 
						    				//modificar la descripcion de descuentos variables, cuando se solicita a compradores.
						    				CotizacionReservacionUtil.agregarDescripcionDescuentosVariables(desEstPedCol, request);
						    				
						    				vistaPedidoDTO.setDescuentosEstadosPedidos(desEstPedCol);
						    				existenDescuentos=Boolean.TRUE;
						    			}
						    			//Si no existen descuentos se deshabilitan los descuentos automaticos por caja y mayorista
						    			else{
						    				vistaPedidoDTO.setDescuentosEstadosPedidos(null);
						    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
						    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA, "NO");
						    			}
						    	  }
								  
								  //obtiene los descuentos del pedido consolidado
								  //if(existenDescuentos){
									  VistaPedidoDTO vistaPedidoDTO = visPedColConsAux.iterator().next();
									  if(vistaPedidoDTO.getCodigoConsolidado() != null && (desEstPedConCol == null || (desEstPedConCol != null && desEstPedConCol.isEmpty()))){
											descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
											descuentoEstadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
							    			descuentoEstadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
							    			descuentoEstadoPedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
							    			descuentoEstadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
							    			descuentoEstadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
							    			descuentoEstadoPedidoDTO.getId().setEsDescuentoConsolidado(estadoActivo);
//							    			descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoInactivo);
							    			descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
							    			descuentoEstadoPedidoDTO.getDescuentoDTO().setTipoDescuentoDTO(new TipoDescuentoDTO());
							    			desEstPedConCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoEstadoTipoPedido(descuentoEstadoPedidoDTO);
							    			if(desEstPedConCol != null && !desEstPedConCol.isEmpty()){
							    				
							    				//modificar la descripcion de descuentos variables, cuando se solicita a compradores.
						    					 CotizacionReservacionUtil.agregarDescripcionDescuentosVariables(desEstPedConCol, request);
							    					 
							    				existenDescuentos=Boolean.TRUE;
							    				session.setAttribute(COL_DESCUENTOS, desEstPedConCol);
							    			}
							    			//Si no existen descuentos se deshabilitan los descuentos automaticos por caja y mayorista
							    			else{
							    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
							    				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA, "NO");
							    			}
										}
								  //}
							  }
							  
							//session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,visPedColConsAux);
							session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_DESCUENTOS_CONSOLIDADOS,visPedColConsAux);
							session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS,visPedColConsAux);
				    	//se carga la configuraci\u00F3n de los descuentos
						Collection tipoDescuentoCol= (Collection) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
							if(tipoDescuentoCol==null){
								CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
								//Obtener el tipo de descuento por cajas y mayorista
						  	      CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request);
							}
				    	  if(existenDescuentos){
				    		  String[] tamDescuentos=new String[formulario.getOpSeleccionPedidosConsolidados()==null?0:formulario.getOpSeleccionPedidosConsolidados().length];
				    		  formulario.setOpDescuentoSeleccionadoConsolidado(tamDescuentos);
				    		  //CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
					    	  instanciarPopUpSeleccionDescuentos(request);
					    	  salida = "desplegarConsolidarPedido";
				    	  }
				    	  else{
				    		  try{
				    			  ConsolidarAction.inicializarVariablesFormulario(request, formulario,session, estadoActivo, estadoInactivo,infos,errores,errors,warnings,(String)session.getAttribute("ec.com.smx.sic.sispe.accion"));
					    		  salida = "desplegar";
				    		  }
				    		  catch (Exception ex){
				    			  LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
								  errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
								  session.removeAttribute(COL_DESCUENTOS);
								  session.removeAttribute(SessionManagerSISPE.POPUP);
								  formulario.setDatosConsolidados((ArrayList<VistaPedidoDTO>)session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX));
								  salida = "desplegarConsolidarPedido";
				    		  }
				    	  }
					  }else{
						  infos.add("consolidarPedido",new ActionMessage("message.numeropedidos.consolidar"));
						  salida = "desplegarConsolidarPedido";
					  }
				}
				else{
						  infos.add("consolidarPedido",new ActionMessage("message.numeropedidos.seleccionado.consolidar"));
						  salida = "desplegarConsolidarPedido";
					  }
				}
				else{
					session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
					ConsolidarAction.inicializarVariablesFormulario(request, formulario,session, estadoActivo, estadoInactivo,infos,errores,errors,warnings,(String)session.getAttribute("ec.com.smx.sic.sispe.accion"));
					salida = "desplegar";
				}
				  
				beanSession.setPaginaTab(ContactoUtil.construirTabsContactoPedido(request, formulario));
					
				
		      }
		      /*------------cerrar popup confirmacion pedidos seleccionados------------*/
		      else if(request.getParameter("botonNO")!=null){
					if(ConsolidarAction.tieneAutorizacion){
						exitos.add("autorizacionConsolidarPedido",new ActionMessage("info.consolidacionPedidos.autorizado"));
					}
				}
			/*--------------------------------- eliminar pedidos consolidados consolidacion--------------------------------------*/
		      else if(request.getParameter("eliminarPedidosConsolidados")!=null ){
		    	  		    	  
		    	  LogSISPE.getLog().info("eliminarPedidosConsolidados");
		    	//obtener los pedidos a consolidar y guardar en sesion
		    	//obtener los pedidos a consolidar y guardar en sesion
				//VistaPedidoDTO consolidadoDTO=(VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
				VistaPedidoDTO consolidadoDTO=null;
				
				String []pedidoValido=formulario.getPedidosValidados();	
				int contador = 0;
				Boolean pedEncontrado = Boolean.FALSE;
				//se obtiene el la posici\u00F3n del pedido a consolidar seleccionado
				for(String itVistaPedidoDTO:pedidoValido){
					if(itVistaPedidoDTO.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
						pedEncontrado=Boolean.TRUE;
						break;								
					}	
						contador++;
				}
				if(pedEncontrado){						
					consolidadoDTO = (VistaPedidoDTO)CollectionUtils.get((List<VistaPedidoDTO>)formulario.getDatosConsolidados(), contador);					
					session.setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",consolidadoDTO);
				}
				
			    boolean validacionCheck = false;
			    //Verifico si los check activados corresponden al grupo de pedidos seleccionados
				if(consolidadoDTO != null && consolidadoDTO.getVistaPedidoDTOCol() != null && !consolidadoDTO.getVistaPedidoDTOCol().isEmpty() &&
						formulario.getOpSeleccionPedidosConsolidados() != null ){
					//recorro los check seleccionados
					for(int iOp=0;iOp<formulario.getOpSeleccionPedidosConsolidados().length;iOp++){ 
						validacionCheck = false;
						String []check=formulario.getOpSeleccionPedidosConsolidados()[iOp].split("-");
						//recorro
						for(VistaPedidoDTO visPedDTO:consolidadoDTO.getVistaPedidoDTOCol()){
							if(check[1].equals(visPedDTO.getId().getCodigoPedido())){
								validacionCheck = true;
							}
						}
						if(!validacionCheck){
							LogSISPE.getLog().info("El check activados no corresponde al grupo de pedidos seleccionado");
							break;
						}
					}
				}
				
				//obtener de sesion en el caso de que ya se haya seleccionado pedidos a cotizar
			    	  int tamanioConsolidados=formulario.getOpSeleccionPedidosConsolidados()==null?0:formulario.getOpSeleccionPedidosConsolidados().length;
			    	  
			    	  if(consolidadoDTO == null){
			    		  errors.add("pedidoUnicoDesconsolidar",new ActionMessage("message.numeropedidos.seleccionado.desconsolidar"));
			    	  }else if(tamanioConsolidados == 0){			    		
			    		  errors.add("checkPedidosDesconsolidar",new ActionMessage("message.checkpedidos.seleccionado.desconsolidar")); 
			    	  }else if(!validacionCheck){
			    		  errors.add("checkPedidosDesconsolidar",new ActionMessage("message.checkpedidos.seleccionado.grupoPedidos"));
			    	  }else if(tamanioConsolidados > 0){
			    		  int tamanioConsolidar=formulario.getOpSeleccionPedidosConsolidar()==null?0:formulario.getOpSeleccionPedidosConsolidar().length;
				    	  LogSISPE.getLog().info("Tamanio getOpSeleccionPedidosConsolidar() {}", tamanioConsolidar);
				    	  LogSISPE.getLog().info("Tamanio getOpSeleccionPedidosConsolidados() {}", tamanioConsolidados);
								Collection<VistaPedidoDTO> visPedCol=new ArrayList<VistaPedidoDTO>(tamanioConsolidados+tamanioConsolidar);
								Collection<VistaPedidoDTO> visPedEliminarCol=new ArrayList<VistaPedidoDTO>(tamanioConsolidados+tamanioConsolidar);
								
								List<VistaPedidoDTO> listDatosConsolidados=new ArrayList<VistaPedidoDTO>();
							if(formulario.getDatosConsolidados()!=null && formulario.getDatosConsolidados().size()>0) {
								listDatosConsolidados=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados().size());
								//clonar la coleccion
						    	Collection<VistaPedidoDTO> visPedColeCol=formulario.getDatosConsolidados();
								for(VistaPedidoDTO vistaPedidoDTO:visPedColeCol){
									listDatosConsolidados.add((VistaPedidoDTO)SerializationUtils.clone(vistaPedidoDTO));
								}
								//Colecciones principales a eliminar por no tener pedidos asociados
								Collection<VistaPedidoDTO> visPedConsolidarEliminarCol=new ArrayList<VistaPedidoDTO>(listDatosConsolidados.size());
								for(VistaPedidoDTO vistaPedidoDTO:listDatosConsolidados){
										//VistaPedidoDTO vistaPedidoDTO=listDatosConsolidados.get(0);
									if(consolidadoDTO!=null){
											if(vistaPedidoDTO!=null && vistaPedidoDTO.getId().getCodigoPedido().equals(consolidadoDTO.getId().getCodigoPedido())){
												LogSISPE.getLog().info("Tamanio vistaPedidoDTO.getVistaPedidoDTOCol() {}",vistaPedidoDTO.getVistaPedidoDTOCol().size());
												consolidadoDTO.setVistaPedidoDTOCol(new ArrayList<VistaPedidoDTO>());
												for(VistaPedidoDTO visPedDTO:vistaPedidoDTO.getVistaPedidoDTOCol()){
													consolidadoDTO.getVistaPedidoDTOCol().add((VistaPedidoDTO)SerializationUtils.clone(visPedDTO));
												}
												request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",consolidadoDTO);
												for(VistaPedidoDTO visPedDTO:vistaPedidoDTO.getVistaPedidoDTOCol()){
													LogSISPE.getLog().info("visPedDTO.getId().getCodigoPedido() {}",visPedDTO.getId().getCodigoPedido());
													if(formulario.getOpSeleccionPedidosConsolidados()!=null){
														for(int iOp=0;iOp<formulario.getOpSeleccionPedidosConsolidados().length;iOp++){ 
															String []check=formulario.getOpSeleccionPedidosConsolidados()[iOp].split("-");
															 LogSISPE.getLog().info("indiceConsolidado {}",check[0]);
															 LogSISPE.getLog().info("pedidoConsolidado {}",check[1]);
															 if(visPedDTO.getId().getCodigoPedido().equals(check[1])){
																 LogSISPE.getLog().info("PedidoEliminado: {}",visPedDTO.getId().getCodigoPedido());
																 visPedEliminarCol.add(visPedDTO);
																 
															 }
														}
													}
												}
												if(visPedEliminarCol.size()>0){
													vistaPedidoDTO.getVistaPedidoDTOCol().removeAll(visPedEliminarCol);
													if(vistaPedidoDTO.getVistaPedidoDTOCol().size()==0){
														visPedConsolidarEliminarCol.add(vistaPedidoDTO);
													}
												}
												else{
													visPedCol.addAll(vistaPedidoDTO.getVistaPedidoDTOCol());
												}
											}
										}	
									}
								request.getSession().setAttribute("ec.com.smx.sic.sispe.pedidosEliminadosCol",visPedEliminarCol);
								request.getSession().setAttribute("ec.com.smx.sic.sispe.listDatosConsolidados",listDatosConsolidados);
								
								if(visPedConsolidarEliminarCol.size()>0){
									request.getSession().setAttribute("ec.com.smx.sic.sispe.pedidosEliminadosConsolidadosCol",visPedConsolidarEliminarCol);
									//String mensajeConf="Ud esta eliminando todos los pedidos agrupados, desea desconsolidar todos los pedidos?";
									
									if(ConsolidarAction.tieneAutorizacion){
										CotizacionReservacionUtil.instanciarVentanaEliminarPedidoTotal(request, accion);
									}else {
										
										session.setAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2,"2");
										 LogSISPE.getLog().info("va a abrir la ventana de autorizacion para eliminar pedidos");					  	
										 // se llama al popUp de autorizacion
										 //TIPO_ELIMINACION = Boolean.TRUE;
										 session.setAttribute(TIPO_ELIMINACION, MessagesWebSISPE.getString("message.tipoEliminacionTotal"));
										 instanciarPopUpAutorizacionEliminar(request,"1","");
										 salida = "desplegarConsolidarPedido";										
									}									
							    	//WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"confirmarEliminarConsolidacion",null,request);  
								}
								else{
									if(consolidadoDTO!=null){
										Collection<VistaPedidoDTO> pedidoSeleccionado= consolidadoDTO.getVistaPedidoDTOCol();
										int tamPedidoSeleccionado=pedidoSeleccionado.size();
										int tamPedidoEliminados=visPedEliminarCol.size();
										int difPedidos=tamPedidoSeleccionado-tamPedidoEliminados;
										if(difPedidos==1){
											errors.add("pedidoUnico",new ActionMessage("message.numeropedidos.desconsolidar"));
										}
										else{
											
											if(ConsolidarAction.tieneAutorizacion){											
												CotizacionReservacionUtil.instanciarVentanaEliminarPedidos(request, accion);
											}else {
												session.setAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2,"2");
												 LogSISPE.getLog().info("va a abrir la ventana de autorizacion para eliminar pedidos");					  	
												 // se llama al popUp de autorizacion
												 //TIPO_ELIMINACION = Boolean.FALSE;
												 session.setAttribute(TIPO_ELIMINACION, MessagesWebSISPE.getString("message.tipoEliminacionParcial"));
												 instanciarPopUpAutorizacionEliminar(request,"1","");
												 salida = "desplegarConsolidarPedido";	
											}
										}
									}else{
										errors.add("pedidoUnicoDesconsolidar",new ActionMessage("message.numeropedidos.seleccionado.desconsolidar"));
									}
								}
						}
			    		  
			    	  }
					salida = "desplegarConsolidarPedido";
		      }
			/*--------------------------------- cuando se presiona en el boton de confirmar eliminar pedidos consolidados--------------------------------------*/
		      else if(request.getParameter("confirmarEliminarPedidoConsolidado")!=null){
		    	Collection<VistaPedidoDTO> visPedEliminarCol=(Collection<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidosEliminadosCol");
		    	List<VistaPedidoDTO> listDatosConsolidados= (List<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.listDatosConsolidados");
		    	 //obtener los detalles de los pedidos consolidados eliminados
		    	  Collection<DetallePedidoDTO> detallePedidoConsolidadoEliminarCol=null;
		    	//determinar si se aplican precios de afiliado o no 
		  		Boolean sinPrecioAfiliado=Boolean.TRUE;
		  		for(VistaPedidoDTO visPedDTO:visPedEliminarCol){
		  			if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
		    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
		    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
		    			sinPrecioAfiliado=Boolean.FALSE;
		    			break;
		    		}
		  		}
		  		if(sinPrecioAfiliado){
		  			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
		  		}
		    	  
		  		 Collection<VistaPedidoDTO> visPedColConsEliminarProcesar =new ArrayList<VistaPedidoDTO>(visPedEliminarCol.size());
				  //buscar si los pedidos en la coleccion visPedColConsAux estan consolidados para recalcular, caso contrario se esperara a que presione el boton de consolidar
				  for (VistaPedidoDTO visPedDTO: visPedEliminarCol){
						PedidoDTO pedidoBuscar= new PedidoDTO();
						pedidoBuscar.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
						pedidoBuscar.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
						pedidoBuscar.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
						LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",visPedDTO.getId().getCodigoPedido());
						Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
						LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
						if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
							PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
							if(pedActDTO.getCodigoConsolidado()!=null){
								visPedColConsEliminarProcesar.add(visPedDTO);
							}
						}
					}
		  			if(!visPedColConsEliminarProcesar.isEmpty()){
			    	  //obtener los detalles de los pedidos consolidados eliminados
			    	  detallePedidoConsolidadoEliminarCol=ConsolidarAction.construirDetallesPedidoDesdeVistaConsolidados(formulario,request,visPedColConsEliminarProcesar,Boolean.TRUE,Boolean.TRUE, errors);
			    	  //se dejan las autorizaciones en su estado original
					  AutorizacionesUtil.asignarAutorizacionesOriginalesDesconsolidados(request, detallePedidoConsolidadoEliminarCol);
					  ConsolidarAction.eliminarDescuentosConsolidados(request,session, visPedColConsEliminarProcesar,detallePedidoConsolidadoEliminarCol);
		  			}
		  			//obtener detalles de pedidos consolidados
		    	  String []pedidosValidados=formulario.getPedidosValidados();
				  int pos=0;
				  Boolean pedidoEncontrado=Boolean.FALSE;
				  for(pos=0;pos<pedidosValidados.length;pos++){
					  if(pedidosValidados[pos]!=null && pedidosValidados[pos].equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
						  pedidoEncontrado=Boolean.TRUE;
						  break;
					  }					 
				  }
				  
				  if(listDatosConsolidados!=null && listDatosConsolidados.size()>0) {
						if(pedidoEncontrado){				
							VistaPedidoDTO visPedDTOAct=(VistaPedidoDTO)listDatosConsolidados.get(pos);
							request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTOAct);
							
						}
				  }
		    	  VistaPedidoDTO consolidadoDTO=(VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
				  Collection<VistaPedidoDTO> visPedColConsAux =consolidadoDTO.getVistaPedidoDTOCol();
				  sinPrecioAfiliado=Boolean.TRUE;
			  		for(VistaPedidoDTO visPedDTO:visPedColConsAux){
			  			if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
			    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			    			sinPrecioAfiliado=Boolean.FALSE;
			    			break;
			    		}
			  		}
			  		if(sinPrecioAfiliado){
			  			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
			  		}
				  
				  
				  //llenar el codigo que asociara al pedido consolidado
				  formulario.setNumeroPedidoConsolidado(visPedColConsAux.iterator().next().getId().getCodigoPedido());
				  Collection<VistaPedidoDTO> visPedColConsProcesar =new ArrayList<VistaPedidoDTO>(visPedColConsAux.size());
				  //buscar si los pedidos en la coleccion visPedColConsAux estan consolidados para recalcular, caso contrario se esperara a que presione el boton de consolidar
				  for (VistaPedidoDTO visPedDTO: visPedColConsAux){
						PedidoDTO pedidoBuscar= new PedidoDTO();
						pedidoBuscar.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
						pedidoBuscar.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
						pedidoBuscar.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
						LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",visPedDTO.getId().getCodigoPedido());
						Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
						LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
						if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
							PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
							if(pedActDTO.getCodigoConsolidado()!=null){
								visPedColConsProcesar.add(visPedDTO);
							}
						}
					}
				  				  
				  
				  if(!visPedColConsProcesar.isEmpty()){
					  Collection<DetallePedidoDTO> detallesPedidoConsolidado=ConsolidarAction.construirDetallesPedidoDesdeVistaConsolidados(formulario,request,visPedColConsProcesar,Boolean.TRUE,Boolean.FALSE, errors);
					  session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallesPedidoConsolidado);
					  //Sumar los valores y cantidades al pedido general y para verificar si aplica o no un descuento
					  //detallesPedidoConsolidado = CotizacionReservacionUtil.sumarValoresCantidadesPedidoGeneral(formulario, session, null);
					  session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,visPedColConsProcesar);
					  if(CollectionUtils.isNotEmpty(visPedColConsAux)){
						//for(VistaPedidoDTO visPedDTO:visPedColConsAux){
						    VistaPedidoDTO visPedDTO = visPedColConsAux.iterator().next();
							DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO=new DescuentoEstadoPedidoDTO();
							descuentoEstadoPedidoDTO.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
							descuentoEstadoPedidoDTO.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
							descuentoEstadoPedidoDTO.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
			    			descuentoEstadoPedidoDTO.getId().setCodigoEstado(visPedDTO.getId().getCodigoEstado());
			    			descuentoEstadoPedidoDTO.getId().setSecuencialEstadoPedido(visPedDTO.getId().getSecuencialEstadoPedido());
		    				descuentoEstadoPedidoDTO.getId().setEsDescuentoConsolidado(estadoActivo);
			    			descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
			    			descuentoEstadoPedidoDTO.getDescuentoDTO().setTipoDescuentoDTO(new TipoDescuentoDTO());
							Collection descuentos=SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoEstadoTipoPedido(descuentoEstadoPedidoDTO);
							ConsolidarAction.crearParametrosDescuentos(request, session, descuentos);
							//se realiza una sola busqueda porque cuando se consolida el descuento es el mismo para todos los pedidos
							//break;
						}
					  
					  
					  Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
					  Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
					  ConsolidarAction.aplicarDescuentosConsolidados(request, session,null,descuentoVariableCol, llaveDescuentoCol,errors);
				  }
	    	  	//trans guardar pedidos
					 try{
						 Collection<PedidoDTO> pedidoColActualizado= (Collection<PedidoDTO>)session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
						 Collection<DetallePedidoDTO> colDetallePedidoConsolidadoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
						 LogSISPE.getLog().info("CodigoConsolidado: {}",formulario.getNumeroPedidoConsolidado());
						 
						 Collection<PedidoDTO> pedidoColConsolidadoEliminado=(Collection<PedidoDTO>) session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						 						 
						 PedidoDTO pedidoDTO = new PedidoDTO();
						 Collection collectionVistaPedidoDTOsAux = new ArrayList();
						 						 
						//se obtiene la vistaPedidoDTO
						if((pedidoColActualizado!=null && pedidoColActualizado.size()>0) && (pedidoColConsolidadoEliminado!=null && pedidoColConsolidadoEliminado.size()>0)){ 
							VistaPedidoDTO vistaPedidoDTO = visPedColConsAux.iterator().next();
							//cuando se guarda la modificaci\u00F3n de una cotizaci\u00F3n anterior
							pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
							pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
							pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
							pedidoDTO.setCodigoConsolidado(formulario.getNumeroPedidoConsolidado());
							pedidoDTO.setPedidosConsolidar(pedidoColActualizado);
							//datos de auditoria
							pedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
							SessionManagerSISPE.getServicioClienteServicio().transGuardarConsolidacion(pedidoDTO, pedidoDTO.getPedidosConsolidar(), colDetallePedidoConsolidadoDTO, pedidoColConsolidadoEliminado, detallePedidoConsolidadoEliminarCol);
						}
						
						//---------wc actualizar pedidos que fueron desconsolidados--------//
						if(visPedEliminarCol!=null && !visPedEliminarCol.isEmpty()){
							//Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
							for(VistaPedidoDTO vistaPedidoDTOit:visPedEliminarCol){								
								VistaPedidoDTO vistaPedidoDTOaux = new VistaPedidoDTO();
								vistaPedidoDTOaux.getId().setCodigoCompania(vistaPedidoDTOit.getId().getCodigoCompania());
								vistaPedidoDTOaux.getId().setCodigoPedido(vistaPedidoDTOit.getId().getCodigoPedido());
								vistaPedidoDTOaux.setCodigoConsolidado(null);
								vistaPedidoDTOaux.setEstadoActual("SI");	
								Collection pedCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoDTOaux);
								if(pedCol!=null && !pedCol.isEmpty()){
									collectionVistaPedidoDTOsAux.add(pedCol.iterator().next());
								}
							}							
						}
						//---------wc actualizar pedidos que fueron desconsolidados--------//
						
						session.removeAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						session.removeAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
						infos.add("consolidarPedido",new ActionMessage("message.desconsolidar.pedidos"));
						 if(formulario.getDatos()!=null && formulario.getDatos().size()>0) {
							List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(formulario.getDatos());
							//listDatos.addAll(visPedEliminarCol);
							listDatos.addAll(collectionVistaPedidoDTOsAux);
							formulario.setDatos(listDatos);
						 }else{
							List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(visPedEliminarCol.size());
							//listDatos.addAll(visPedEliminarCol);
							listDatos.addAll(collectionVistaPedidoDTOsAux);
							formulario.setDatos(listDatos);
						 }
						 //agrego para a la session para el link del p\u00E9dido
						 session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, formulario.getDatos());
					 	 if(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null){
				    		  int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
				    		  tamano = tamano + visPedEliminarCol.size();
				    		  session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, tamano);	   
				    	 }else{
				    		session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, visPedEliminarCol.size());
				    	 }	
						 //agregar los pedidos sobrantes a la coleccion de consolidados
						 if(formulario.getDatosConsolidados()!=null){
					 		formulario.setDatosConsolidados(listDatosConsolidados);
						 }
				    	session.removeAttribute(SessionManagerSISPE.POPUP);
				    	//jdanny
						session.removeAttribute(COL_PEDIDO_CONSOLIDADOS_AUX);
						session.setAttribute(COL_PEDIDO_CONSOLIDADOS_AUX, listDatosConsolidados);
						
						//---------wc actualizar la coleccion principal de pedidos consolidados--------//
						Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
						if(pedidoActColDTO!=null && !pedidoActColDTO.isEmpty()){
							String codigoConsolidado =pedidoActColDTO.iterator().next().getCodigoConsolidado();
							VistaPedidoDTO vistaPedidoDTOaux = new VistaPedidoDTO();
							vistaPedidoDTOaux.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
							vistaPedidoDTOaux.setCodigoConsolidado(codigoConsolidado);
							vistaPedidoDTOaux.setEstadoActual("SI");
							Collection<VistaPedidoDTO> collectionVistaPedidoDTOs = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoDTOaux);
							VistaPedidoDTO vistaPedidoUpdate = new VistaPedidoDTO();
							String []pedidoValido=formulario.getPedidosValidados();						
							int contador = 0;
							Boolean pedEncontrado = Boolean.FALSE;
							//se obtiene el la posici\u00F3n del pedido a consolidar seleccionado
							for(String itVistaPedidoDTO:pedidoValido){
								if(itVistaPedidoDTO.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
									pedEncontrado=Boolean.TRUE;
									break;								
								}	
									contador++;
							}
							if(pedEncontrado){						
								vistaPedidoUpdate = (VistaPedidoDTO)CollectionUtils.get((List<VistaPedidoDTO>)formulario.getDatosConsolidados(), contador);
								vistaPedidoUpdate.getId().setCodigoPedido(codigoConsolidado);
								vistaPedidoUpdate.setCodigoConsolidado(codigoConsolidado);
								vistaPedidoUpdate.setVistaPedidoDTOCol(collectionVistaPedidoDTOs);
								session.setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",vistaPedidoUpdate);
							}				
							//---------wc actualizar la coleccion principal de pedidos consolidados--------//
						}
						
					 }
					 catch (Exception ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("registrarConsolidacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la desconsolidaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en GUARDAR"));
						errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					 }
		    	  salida = "desplegarConsolidarPedido";
		      }
			/*--------------------------------- agregar pedidos consolidados consolidacion--------------------------------------*/
		      else if(request.getParameter("agregarPedidosConsolidados")!=null ){
		    	  LogSISPE.getLog().info("agregarPedidosConsolidados");
			    	//obtener de sesion en el caso de que ya se haya seleccionado pedidos a cotizar
		    	  LogSISPE.getLog().error("Tamanio getOpSeleccionPedidosConsolidar() {}", formulario.getOpSeleccionPedidosConsolidar());
		    	  LogSISPE.getLog().error("Tamanio getOpSeleccionPedidosConsolidados() {}", formulario.getOpSeleccionPedidosConsolidados());
		    	  session.setAttribute( POSICION_DIV ,request.getParameter( "posicionScroll" ));
		    	  
		    	  int tamanioConsolidados=formulario.getOpSeleccionPedidosConsolidados()==null?0:formulario.getOpSeleccionPedidosConsolidados().length;
		    	  int tamanioConsolidar=formulario.getOpSeleccionPedidosConsolidar()==null?0:formulario.getOpSeleccionPedidosConsolidar().length;
		    	  int tamPedidosConsolidados=0;
		    	  
		    	  if(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null){
		    		  int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
			    	  if(tamano >= tamanioConsolidar){
			    		  tamano = tamano - tamanioConsolidar;
			    		  session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, tamano);
			    	  } 
		    	  }
		    	  
		    	  if(tamanioConsolidar>0){
		    	  Collection<VistaPedidoDTO> visPedCol=new ArrayList<VistaPedidoDTO>(tamanioConsolidados+tamanioConsolidar);
				  //obtener los pedidos a consolidar y guardar en sesion
				  Collection<VistaPedidoDTO> listDatosConsolidados=new ArrayList<VistaPedidoDTO>();;
				  VistaPedidoDTO consolidadoDTO=(VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
				  LogSISPE.getLog().info("estado consolidadoDTO {}",consolidadoDTO);
				  String []pedidosValidados=formulario.getPedidosValidados();
				  int pos=0;
				  Boolean pedidoEncontrado=Boolean.FALSE;
				  for(pos=0;pos<pedidosValidados.length;pos++){
					  if(pedidosValidados[pos]!=null && pedidosValidados[pos].equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
						  pedidoEncontrado=Boolean.TRUE;
						  break;
					  }
				  }
				  
				 
				  if(!pedidoEncontrado){
					  session.setAttribute( POSICION_DIV , 0);
				  }
				  
				  
				  if(formulario.getDatosConsolidados()!=null && formulario.getDatosConsolidados().size()>0) {
					  List pedidoConsolidado=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados());
						listDatosConsolidados=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados());
						if(pedidoEncontrado){
							VistaPedidoDTO visPedDTOAct=(VistaPedidoDTO)pedidoConsolidado.get(pos);
							if(visPedDTOAct.getVistaPedidoDTOCol()!=null && visPedDTOAct.getVistaPedidoDTOCol().size()>0){
									tamPedidosConsolidados=visPedDTOAct.getVistaPedidoDTOCol().size();
							}
						}
						LogSISPE.getLog().error("listDatosConsolidados {}",listDatosConsolidados.size());
						for(VistaPedidoDTO visPedConDTO:listDatosConsolidados){
							if(consolidadoDTO!=null){
								if(visPedConDTO!=null && visPedConDTO.getId().getCodigoPedido().equals(consolidadoDTO.getId().getCodigoPedido())){
									LogSISPE.getLog().error("Tamanio vistaPedidoDTO.getVistaPedidoDTOCol() {}",visPedConDTO.getVistaPedidoDTOCol().size());
									for(VistaPedidoDTO visPedDTO:visPedConDTO.getVistaPedidoDTOCol()){
										LogSISPE.getLog().error("visPedDTO.getId().getCodigoPedido() {}",visPedDTO.getId().getCodigoPedido());
										LogSISPE.getLog().error("PedidoAgregado: {}",visPedDTO.getId().getCodigoPedido());
										visPedCol.add(visPedDTO);
									}
								}
							}
						}
					}
				  
				  
				  ParametroDTO parametro = new ParametroDTO();
				  parametro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania()); 
				  parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.valormaximo.empresar.consolidar"));
				  Integer valorMaximoEmpresas= Integer.valueOf(SISPEFactory.obtenerServicioSispe().transObtenerValorParametro(parametro).getValorParametro());
				  
				  AutorizacionDTO autorizacionConsolidarMaximoDTO= (AutorizacionDTO)session.getAttribute(AUTORIZACION_CONSOLIDAR_MAXIMO);
				    if((tamPedidosConsolidados+tamanioConsolidar)<=valorMaximoEmpresas || autorizacionConsolidarMaximoDTO!=null){
						//verificar si no se ha seleccionado ningun pedido a consolidar
						if(formulario.getDatos()!=null && formulario.getDatos().size()>0) {
							List<VistaPedidoDTO> listDatos=new ArrayList<VistaPedidoDTO>(formulario.getDatos());
							if(formulario.getOpSeleccionPedidosConsolidar()!=null){
								for(int iOp=0;iOp<formulario.getOpSeleccionPedidosConsolidar().length;iOp++){
									String []check=formulario.getOpSeleccionPedidosConsolidar()[iOp].split("-");
									 LogSISPE.getLog().error("indice {}",check[0]);
									 LogSISPE.getLog().error("pedido {}",check[1]);
									
									VistaPedidoDTO vistaPedidoDTO=listDatos.get(Integer.valueOf(check[0]));
									LogSISPE.getLog().error("PedidoAgregado: {}",vistaPedidoDTO.getId().getCodigoPedido());
									visPedCol.add(vistaPedidoDTO);
								}
							}
							if(visPedCol.size()>0){
								for(VistaPedidoDTO vistaPedidoDTO:visPedCol){
									listDatos.remove(vistaPedidoDTO);
								}
								formulario.setDatos(listDatos);
							}
						}
						//request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol",visPedCol);
						LogSISPE.getLog().error("Total de pedidos a consolidar {}",visPedCol.size());
						//agregar nuevas empresas consolidadas a la coleccion de empresas
//						ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula=CI
//						ec.com.smx.sic.sispe.pedido.tipoDocumento.ruc=RUC
//						ec.com.smx.sic.sispe.pedido.tipoDocumento.pasaporte=PA
						
//						Collection<VistaPedidoDTO> vistaPedidoCol= (Collection<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS);
//						if(vistaPedidoCol==null){
//							vistaPedidoCol= new ArrayList<VistaPedidoDTO>();
//						}
//						for(VistaPedidoDTO vistaPedidosConsolidados: visPedCol) {
//							Boolean existeEmpresa= Boolean.FALSE;
//							for(VistaPedidoDTO vistaPedidosEmpresas: vistaPedidoCol) {
//								if(vistaPedidosConsolidados.getTipoDocumentoCliente().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.ruc"))){
//									if(vistaPedidosConsolidados.getRucEmpresa().equals(vistaPedidosEmpresas.getRucEmpresa())){
//										existeEmpresa= Boolean.TRUE;
//									}
//								}
//								else{
//									if(vistaPedidosConsolidados.getCedulaContacto().equals(vistaPedidosEmpresas.getCedulaContacto())){
//										existeEmpresa= Boolean.TRUE;
//									}
//								}
//							}
//							if(!existeEmpresa){
//								vistaPedidoCol.add(vistaPedidosConsolidados);
//							}
//						}
						session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS,visPedCol);
						
						if(consolidadoDTO!=null){
							if(listDatosConsolidados!=null){
								for(VistaPedidoDTO visPedConDTO:listDatosConsolidados){
										if(visPedConDTO!=null && visPedConDTO.getId().getCodigoPedido().equals(consolidadoDTO.getId().getCodigoPedido())){
											visPedConDTO.setVistaPedidoDTOCol(visPedCol);
											break;
										}
								}
							}
						}else{
								//guardar en una nueva coleccion para simular que esta consolidado, crear coleccion tipo arbol
								VistaPedidoDTO vistaPedidoDTOConsolidado= new VistaPedidoDTO();
								vistaPedidoDTOConsolidado.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
								vistaPedidoDTOConsolidado.getId().setCodigoEstado("CSD");
								vistaPedidoDTOConsolidado.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
								vistaPedidoDTOConsolidado.getId().setCodigoPedido(formulario.getNumeroAConsolidar());
								vistaPedidoDTOConsolidado.setVistaPedidoDTOCol(visPedCol);
								
								request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",vistaPedidoDTOConsolidado);
								
								//listDatosConsolidados.add(vistaPedidoDTOConsolidado);
								Collection<VistaPedidoDTO> vistaPedidoColAux= new ArrayList<VistaPedidoDTO>(listDatosConsolidados);
								listDatosConsolidados= new ArrayList<VistaPedidoDTO>(listDatosConsolidados.size());
								listDatosConsolidados.add(vistaPedidoDTOConsolidado);
								listDatosConsolidados.addAll(vistaPedidoColAux);
								
								String []validarPedidos=new String[listDatosConsolidados.size()+1];
						        String []numeroPedidoConsolidado=new String[listDatosConsolidados.size()+1];
						        int iPedidos=0;
					            for(VistaPedidoDTO visPedDTO:listDatosConsolidados){
					            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
					            	if(visPedDTO.getId().getCodigoPedido().equals(formulario.getNumeroAConsolidar().trim())){
					            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
					            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+formulario.getNumeroAConsolidar();
					            			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTO);
					            	}
					            	else{
					            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
					            		numeroPedidoConsolidado[iPedidos]=null;
					            	}
					            	iPedidos++;
					            }
					            formulario.setPedidosValidados(validarPedidos);
					            formulario.setNumeroConsolidado(numeroPedidoConsolidado);
						}
						//request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol",listDatosConsolidados);
						formulario.setDatosConsolidados(listDatosConsolidados);
						//jdannyy
						session.removeAttribute(COL_PEDIDO_CONSOLIDADOS_AUX);
						session.setAttribute(COL_PEDIDO_CONSOLIDADOS_AUX, listDatosConsolidados);
						
						infos.add("exitoAgregarPedido", new ActionMessage("message.seleccionar.pedidosConsolidar.exito"));
						//se elimina la bandera que permite consolidar mas de 5 pedidos
						session.removeAttribute(AUTORIZACION_CONSOLIDAR_MAXIMO);
					}
					else{
						session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
					  	LogSISPE.getLog().info("va a abrir la ventana de autorizacion de gerente Comercial para consolidacion");
						//se crea la ventana que pide la autorizacion
					  	LogSISPE.getLog().info("va a abrir la ventana de autorizacion para pedidos consolidados");
					  	AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.getInstancia().TIPO_AUTORIZACION_CONSOLIDACION, Boolean.TRUE, null);
					}
		    	  }	else {//wc mensaje de error si no se ha seleccionado pedidos para agregar
		    		  errors.add("errorAgregarPedidoConsolidar", new ActionMessage("message.seleccionar.pedidosConsolidar.error"));
				}	    	  
		    	  
		    	  salida = "desplegarConsolidarPedido";
		      } else	//mostrar los pedidos consolidados
				if(request.getParameter("indice")!=null)
				{
					LogSISPE.getLog().info("Buscar cotizaciones y recotizaciones");
					LogSISPE.getLog().info("ACCION ACTUAL {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
					
					 if(session.getAttribute(OCULTAR_BOTON_DESCUENTOS) != null ){
						 session.setAttribute(RESPALDO_OP_DESCUENTOS, request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS));
						 session.setAttribute(RESPALDO_DESCUENTOS_SELECCIONADOS, request.getSession().getAttribute(COL_DESC_SELECCIONADOS));
						 session.setAttribute(RESPALDO_DESCUENTOS_CONSOLIDADOS, session.getAttribute(COL_DESCUENTOS));
					 }
					  
					//se oculta el boton de los descuentos
					session.removeAttribute(OCULTAR_BOTON_DESCUENTOS);
					//verificar que el pedido no tuvo ningun cambio
					Boolean verificaPreciosDetallePedido=Boolean.FALSE;
					Boolean verificaDetallePedido=Boolean.FALSE;
					String accionConsolidar=(String)session.getAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
					String accionActual=(String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
					Boolean verificarSIC=Boolean.FALSE;
					Boolean verificaEntregas = Boolean.FALSE;
					Boolean verificarCambioContacto = Boolean.FALSE;
					Boolean verificarAutorizacionesDsctoVar = Boolean.FALSE;
					Boolean verificarCambioDescuento = Boolean.FALSE;
					
					if(accionConsolidar==null){
						LogSISPE.getLog().info("----------EMPIEZA EL PROCESO DE COMPARACION DEL DETALLE PEDIDO--------");
						//1ro Verifica si hubo cambio de Precios en los totales y subtotales del pedido
						verificaPreciosDetallePedido = CotizacionReservacionUtil.verificarPreciosDetallePedido(request);
						if(!verificaPreciosDetallePedido){
							//2do Verifica si hubo eliminacion o modificacion de articulos en el pedido
							verificaDetallePedido = CotizacionReservacionUtil.verificarCambiosDetallePedido(request);	
						}
						
						//---------------------->
						PedidoDTO pedidoDTO = crearPedidoDTO(accion, formulario, request);
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						//3ro Verifica si hubo cambios entregas o entidad Responsable
						verificaEntregas = CotizacionReservacionUtil.verificarCambiosEntregas(request, pedidoDTO.getEntidadResponsable());
						//5to Verifica si cambio el contacto en la reserva
						if(CotizacionReservacionUtil.isCambioContacto(vistaPedidoDTO, formulario, request)){
							verificarCambioContacto = Boolean.TRUE;
							pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(TIPO_RESERVACION_CAMBIO_CONTACO);
						}
						//6to se verifica si solicitaron autorizacion para descuento variable
						verificarAutorizacionesDsctoVar = AutorizacionesUtil.existenAutorizacionesPorAplicar((ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES));
													
						verificarCambioDescuento = CotizacionReservacionUtil.verificarExisteCambiosDescuentosReserva(request);
						
						//Impresion de cambios al modificar la Reserva
						LogSISPE.getLog().info("----------Existen cambios en los precios del detalle Pedido--------{}", verificaPreciosDetallePedido);
						LogSISPE.getLog().info("----------Existen cambios en el detalle Pedido---------------------{}", verificaDetallePedido);
						LogSISPE.getLog().info("----------Existen cambios en las entregas del detalle Pedido-------{}", verificaEntregas);
						LogSISPE.getLog().info("----------Existen Cambios en el contacto en la reserva-------------{}", verificarCambioContacto);
						LogSISPE.getLog().info("----------Existen Cambios en los descuentos variables--------------{}", verificarAutorizacionesDsctoVar);
						LogSISPE.getLog().info("----------Existen Cambios en los descuentos------------------------{}", verificarCambioDescuento);
						
						//<-------------------------
						
					}
					//Cuado es la opcion consolidar no debe de consultar el stock en el SIC
					if(accionConsolidar!= null && !accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
						verificarSIC=Boolean.TRUE;
					}
					
					if(verificaPreciosDetallePedido==Boolean.FALSE && verificaDetallePedido==Boolean.FALSE && !verificaEntregas && !verificarCambioContacto
							&& !verificarAutorizacionesDsctoVar && !verificarCambioDescuento){
						try{
							
							if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
									|| accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
								//este m\u00E9todo inicializa los par\u00E1metros 
								CrearReservacionAction.inicializarParametros(request, formulario);
							}
//							CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
							//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
//							boolean eliminoEntregas = CotizacionReservacionUtil.construirDetallesPedidoDesdeVista(formulario, request,infos, errors, warnings, false,verificarSIC);
							boolean eliminoEntregas = ConsolidarAction.construirDetallesPedidoDesdeVista(formulario, request,infos, errors, warnings, false,verificarSIC);
							
							if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
									|| accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
									|| accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))){
								
								VistaPedidoDTO vistaAux = (VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.vistaPedidoDTOActual");
								String codigoPedido=(String)request.getParameter("codigoPedido");
								//Verifica si el pedido seleccionado es el ingresado desde la busqueda
								if(vistaAux != null && codigoPedido != null){
									if(codigoPedido.equals(vistaAux.getId().getCodigoPedido())){
										//MUESTRA LA SECCION DEL ACORDEON DESCUENTOS
										session.removeAttribute(OCULTA_SECCION_DESCUENTOS);
									}else{
										//OCULTA LA SECCION DEL ACORDEON DESCUENTOS 
										session.setAttribute(OCULTA_SECCION_DESCUENTOS, "ok");
									}
								}else{
									//OCULTA LA SECCION DEL ACORDEON DESCUENTOS 
									session.setAttribute(OCULTA_SECCION_DESCUENTOS, "ok");
								}	
							}else{
								//MUESTRA LA SECCION DEL ACORDEON DESCUENTOS
								session.removeAttribute(OCULTA_SECCION_DESCUENTOS);
								session.removeAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP);
							}
							
							if(eliminoEntregas)
								warnings.add("siEliminoEntregas",new ActionMessage("warnings.entregasEliminadas"));
		
							//se obtienen los d\u00EDas de validez para una cotizaci\u00F3n
							ParametroDTO consultaParametroDTO = new ParametroDTO();
							consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							String diasValidez = SessionManagerSISPE.getServicioClienteServicio().transObtenerDiasValidezCotizacion(consultaParametroDTO);
							session.setAttribute(CotizarReservarAction.DIAS_VALIDEZ, diasValidez);
		
							//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la recotizaci\u00F3n
							//session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
									//MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearRecotizacion.estado"));
							
							session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
							MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
							
							//se obtiene la descripci\u00F3n del tipo de autorizaci\u00F3n gen\u00E9rico que permite crear una reservaci\u00F3n
//							TipoAutorizacionDTO tipoAutorizacionFiltro = new TipoAutorizacionDTO();
//							tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//							tipoAutorizacionFiltro.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"));
//							
//							Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCol = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionFiltro);
//							tipoAutorizacionFiltro = null;
//							//se obtiene el \u00FAnico registro
//							if(tipoAutorizacionDTOCol != null && !tipoAutorizacionDTOCol.isEmpty()){
//								//se guarda la descripci\u00F3n en sesi\u00F3n
//								session.setAttribute(CotizarReservarAction.DESCRICPION_USO_AUTORIZACION, tipoAutorizacionDTOCol.iterator().next().getDescripcion());
//							}
							//Se inicializa el tipo de autorizacion generico
							AutorizacionesUtil.iniciarTipoAutorizacionGenerico(request);
		
							//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
							session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de cotizaci\u00F3n");
		
							//variable que sirve para saber que inicialmente se ingres\u00F3 a una recotizaci\u00F3n,
							//esta se utiliza en la jsp para mostrar o no el combo de locales
							session.setAttribute("ec.com.smx.sic.sispe.recotizacion","ok");
		
							//se asigna el estado activo a esta opci\u00F3n para que no valide el stock cuando se guarde una cotizaci\u00F3n
							formulario.setOpValidarPedido(estadoActivo);
							
							//se valida si el pedido viene ya con una autorizacion de %peso cambio pavos
							List<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
							
							//se eliminan los descuentos variables y las autorizaciones de los detalles
							if(session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS) != null && (Boolean)session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS).equals(Boolean.TRUE)){
								session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
								//se guardan los detallesEstadoPedidoAutorizacionDTO que van a ser eliminados para cambiar su estado a inactivo
//								CotizacionReservacionUtil.obtenerAutorizacionesInactivas(request, detallePedidoDTOCol, Boolean.TRUE);
							}
							//metodo que verifica que autorizaciones tiene el pedido
							AutorizacionesUtil.verificacionAutorizaciones(detallePedidoDTOCol.get(0).getId().getCodigoPedido(), request, errors, exitos);
							
						}catch(Exception ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
							salida = "listado";
						}
					}else{
						instanciarPopUpCambiosPedido(request);
					}
					
					ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
					
					beanSession.setPaginaTab(ContactoUtil.construirTabsContactoPedido(request, formulario));//cambios oscar
					
					//ejecutar el metodo para inicializar el controlador adecuado
					ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
					
					salida = "desplegar";
				} else if (request.getParameter("consolidadoGeneral")!= null && request.getParameter("consolidadoGeneral").equals("ok")){
					LogSISPE.getLog().info("Consolidacion general");
					LogSISPE.getLog().info("ACCION ACTUAL {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
					
					if(((String[])request.getSession().getAttribute(RESPALDO_OP_DESCUENTOS))!=null && ((String[])request.getSession().getAttribute(RESPALDO_OP_DESCUENTOS)).length>0){
						session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS,  request.getSession().getAttribute(RESPALDO_OP_DESCUENTOS));
					}
					if(CollectionUtils.isNotEmpty((Collection<String>) request.getSession().getAttribute(RESPALDO_DESCUENTOS_SELECCIONADOS))){
						session.setAttribute(COL_DESC_SELECCIONADOS,  request.getSession().getAttribute(RESPALDO_DESCUENTOS_SELECCIONADOS));
					}
					  
					//se muestra el boton de descuentos
					session.setAttribute(OCULTAR_BOTON_DESCUENTOS, "ok");
					//verificar que el pedido no tuvo ningun cambio
					Boolean verificaPreciosDetallePedido=Boolean.FALSE;
					Boolean verificaDetallePedido=Boolean.FALSE;
					String accionConsolidar=(String)session.getAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
					if(accionConsolidar==null){
						LogSISPE.getLog().info("----------EMPIEZA EL PROCESO DE COMPARACION DEL DETALLE PEDIDO--------");
						//1ro Verifica si hubo cambio de Precios en los totales y subtotales del pedido
						verificaPreciosDetallePedido = CotizacionReservacionUtil.verificarPreciosDetallePedido(request);
						if(!verificaPreciosDetallePedido){
							//2do Verifica si hubo eliminacion o modificacion de articulos en el pedido
							verificaDetallePedido = CotizacionReservacionUtil.verificarCambiosDetallePedido(request);
							
						}
						//Impresion de cambios al modificar la Reserva
						LogSISPE.getLog().info("----------Existen cambios en los precios del detalle Pedido--------{}", verificaPreciosDetallePedido);
						LogSISPE.getLog().info("----------Existen cambios en el detalle Pedido--------{}", verificaDetallePedido);
					}
						
					if(verificaPreciosDetallePedido==Boolean.FALSE && verificaDetallePedido==Boolean.FALSE){
						//Oculta la session de descuentos
						if(session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
								|| session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
								|| session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))){
							//OCULTA LA SECCION DEL ACORDEON DESCUENTOS 
							session.setAttribute(OCULTA_SECCION_DESCUENTOS, "ok");
						}else{
							session.removeAttribute(OCULTA_SECCION_DESCUENTOS);
						}
						//remover el pedido que esta en sesion porque estoy en el pedido general
						session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						//calcular el total del pedido consolidado
						String pedidoGeneral= (String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral");
						if(pedidoGeneral==null){
							List<DetallePedidoDTO> detallePedidoConsolidado = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
							//Collection<DetallePedidoDTO> detallePedidoActual= (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
							//detallePedidoConsolidado.addAll(detallePedidoActual);
							//session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallePedidoConsolidado);
							Collection<VistaPedidoDTO> visPedColConsAux= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
							session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS, visPedColConsAux);
							//determinar si se aplican precios de afiliado o no 
							Boolean sinPrecioAfiliado=Boolean.TRUE;
							for(VistaPedidoDTO visPedDTO:visPedColConsAux){
								if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
					    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
					    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
					    			sinPrecioAfiliado=Boolean.FALSE;
					    			break;
					    		}
							}
							if(sinPrecioAfiliado){
				    			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
				    			formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
							}
							
							//se obtiene la configuracion de las autorizaciones del pedido consolidado
//							CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
							try{
								ConsolidarAction.crearPedidoGeneral(request, formulario, session,detallePedidoConsolidado,infos,errores,errors,warnings,accion,estadoActivo,estadoInactivo,Boolean.FALSE);
							}catch (Exception ex){
								LogSISPE.getLog().info("----------Existen cambios en los precios del detalle Pedido--------{}", verificaPreciosDetallePedido);
								errors.add("error",new ActionMessage("errors.gerneral",ex));
							}
							//Valida check pago efectivo
							Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(COL_DESCUENTOS);
							if(descuentos != null && !descuentos.isEmpty()){
								CotizacionReservacionUtil.validarCheckMaxiNavidad(request, descuentos, formulario);
							}else{
								//obtengo el codigoTipDesMax-navidad desde un parametro
								CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
								formulario.setCheckPagoEfectivo(null);
								session.removeAttribute(CHECK_PAGO_EFECTIVO);
							}
							session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,"CONSOLIDAR");
						}
					}
					else{
						 //String mensajeConf="Ud. realiz\u00F3 una modificaci\u00F3n en el pedido, Presione el boton guardar para no perder los cambios";
				    	 //WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"procederPerderCambios",null,request); ss
						instanciarPopUpCambiosPedido(request);
					}
					//cambios oscar
					PaginaTab  tabsConsolidacion = new PaginaTab("crearCotizacion", "deplegar", 1, 410, request);
					Tab tabPedidos = new Tab("Detalle del Pedido","crearCotizacion","/servicioCliente/cotizarRecotizarReservar/detallePedido.jsp",true);
					tabsConsolidacion.addTab(tabPedidos);
				    beanSession.setPaginaTab(tabsConsolidacion);
					salida = "desplegar";
					session.setAttribute(COL_DESC_SELECCIONADOS,  request.getSession().getAttribute(RESPALDO_DESCUENTOS_SELECCIONADOS));
				}
				else if(request.getParameter("ocultarPopUp") != null){
					session.removeAttribute(SessionManagerSISPE.POPUP);
				}
				else if(request.getParameter("ocultarPopUpPedidoNoReservado") != null){
					EnvioMailUtil.enviarMailAutorizacionStockPavos(request);
					session.removeAttribute(SessionManagerSISPE.POPUP);
					salida = "registro";
				}
				//------------------------cuando indicamos que se actualicen los precios de los pedidos consolidados
				else if(request.getParameter("confirmarActualizarPrecios")!= null && request.getParameter("confirmarActualizarPrecios").equals("ok")){
					LogSISPE.getLog().info("actualizar los precios consolidados");
						session.setAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios","SI");
						CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.FALSE);
						session.removeAttribute(SessionManagerSISPE.POPUP);
						session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
				}
				//------------------------cuando indicamos que no se actualicen los precios de los pedidos consolidados
				else if(request.getParameter("cancelarActualizarPrecios")!= null && request.getParameter("cancelarActualizarPrecios").equals("ok")){
					LogSISPE.getLog().info("cancelar actualizar los precios consolidados");
					session.removeAttribute(SessionManagerSISPE.POPUP);
					session.setAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios","NO");
					session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
					
//					//se procesan los descuentos y Autorizaciones
//					List detallePedido = (List)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
					
					//se recuperan los valores de caja/mayorista originales
					CotizacionReservacionUtil.asignarPreciosCajaMayoristaOriginales(request, vistaPedidoDTO);
					
					if(vistaPedidoDTO != null){
						WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
					}
					AutorizacionesUtil.verificarAutorizacionesVariables((List)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO), request, formulario);
					formulario.actualizarDescuentos(request, warnings);
					AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, warnings);
					session.setAttribute(BORRAR_ACCION_ANTERIOR, true);
					
					session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS, "OK");
					
					//para verificar si la opci\u00F3n es modificar reserva
					List<DetallePedidoDTO> colDetalle = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);	
					Boolean despacho = validarFechaDespachoEnDetallePedido(
							session, colDetalle);
					//si existen despachos y si la opci\u00F3n es modificar reserva, se muestra el pupup
					if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){			
						CotizacionReservacionUtil.instanciarVentanaEntregaDespachada(request);
					}
					
					if(EntregaLocalCalendarioUtil.verificarEntregasProximoDespacho(request)){
						LogSISPE.getLog().info("mostrar popUp editar entregas por entregas proximas a despachar");
						CrearReservacionAction.instanciarPopUpDespachosProximos(request);
					}
				}
			//---------------------------- cuando se desea registrar la cotizaci\u00F3n -----------------------
			else if(formulario.getBotonRegistrarCotizacion()!=null || ( session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)!=null && 
					((String)session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)).equals("registrarCotizacion")) ){
				
				LogSISPE.getLog().info("Se accede mediante 'botonRegistrarCotizacion'");
				session.removeAttribute(PRIMER_INGRESO_AL_PEDIDO);
				
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				
				//solo cuando el estado es diferente a cotizacion (COT) se valida 
				if(accion != MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion")){
					//se valida que el pedido en session sea el actual en la BDD
					Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO);
					
					//si el pedido no es el actual, no continua el proceso de guardar
					if(!pedidoActual){
						//cuando el pedido ha sido modificado desde otra sesion
						CotizacionReservacionUtil.instanciarVentanaPedidoModificado(request, "crearCotizacion.do");
						return mapping.findForward(salida);
					}
				}
				
				LogSISPE.getLog().info("Registrando cotizaci\u00F3n..");
				if(session.getAttribute(TRANSACCION_REALIZADA)==null){
					//Se valida si tiene autorizaciones solicitadas
					if(AutorizacionesUtil.solicitarAutorizacion("registrarCotizacion",request, formulario, exitos, warnings, infos, errors)){
						salida = "desplegar";
						//Cambiar al tab de pedidos
						ContactoUtil.cambiarTabPedidos(beanSession);
						return forward(salida, mapping, request, errors, exitos, warnings, infos, beanSession);
					}	
					//se actualiza el estado de la accion para que no muestre el numero de reservacion
					session.setAttribute("ec.com.smx.sic.sispe.accion", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"));
					session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
					formulario.setBotonRegistrarCotizacion(null);
					//se llama al m\u00E9todo que realiza el registro de la cotizaci\u00F3n
					salida = this.guardarCotizacion(accion, request, formulario, errors, exitos, true);
					
					if( salida.equalsIgnoreCase("registro")){

						//se cargan los datos del contacto
						ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
						
						if(warnings!=null && warnings.size()==1 && warnings.get().next().toString().startsWith("error.contactos.localizacion.sinContacto")){
							warnings.clear();
						}
						//Se contruyen los tabs de Resumen y Pedidos
						PaginaTab tabsResumen = ContactoUtil.construirTabsResumenPedido(request,formulario);
						beanSession.setPaginaTab(tabsResumen);

						//ejecutar el metodo para inicializar el controlador adecuado
						ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
						
						//ContactoUtil.cambiarAEstadoResumenComponenteContactos(request, response, formulario);
						
						//se muestra el popUp indicando que el pedido no fue reservado por tener autorizaciones pendientes
						if(session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK) != null 
								&& (Boolean)session.getAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK)){
							CotizacionReservacionUtil.instanciarPopUpPedidoNoReservado(request, "crearCotizacion.do");
						}
						
						//se envian las notificacion PUSH a los dispositivos de los autorizadores
						AutorizacionesUtil.enviarNotificacionAutorizadores(request);
						
					}
					//se sube una variable a sesi\u00F3n para controlar lo que se muestra en los reportes
					if(request.getAttribute(CotizarReservarForm.RESERVACION_TEMPORAL)!=null){
						session.setAttribute(ES_RESERVA_TEMPORAL, "OK");
					}
				}else{
					salida="registro";
				}
				
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				
				//Verificacion diferidos
				WebSISPEUtil.verificarDiferidosCotizarRes(request);
				formulario.setBotonRegistrarCotizacion(null);
				session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
				session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
				session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
				session.setAttribute(MOSTRAR_RESUMEN_PEDIDO, "ok");

			}
			/*----------- pasar a las entregas con el calendario----*/
			else if((formulario.getRegistrarFecha()!=null  && !formulario.getRegistrarFecha().equals("")) 
					|| (request.getParameter("registrarFecha") != null  && !request.getParameter("registrarFecha").equals(""))){
				//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
				if (formulario.getCheckReservarStockEntrega()!=null && formulario.getCheckReservarStockEntrega().equals(estadoActivo)){
					errors.add("preEntregaAutomatica",new ActionMessage("message.reservacion.seleccionPreEntregaAutomatica"));	
				}else{		
					session.setAttribute(EntregaLocalCalendarioAction.HORACOL, obteneHorasMinutos(false));
					
					//obtengo la colecci\u00F3n de detalles de la sesion
					List<DetallePedidoDTO> detallePedido = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					
					if(CollectionUtils.isNotEmpty(detallePedido)){
						//session.setAttribute(EntregaLocalCalendarioAction.MINUTOCOL, minutos());
						salida = pasarAEntregas(request, formulario);
					}else{
						errors.add("sinDetalles",new ActionMessage("errors.detalle.requerido"));
					}
				}
			}
			
			/*------------------ cuando se quiere almacenar una reservaci\u00F3n -----------------------*/
			else if(formulario.getBotonRegistrarReservacion()!=null || ( session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)!=null && 
					((String)session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)).equals(MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion")))
					 ){
				session.removeAttribute(PRIMER_INGRESO_AL_PEDIDO);
				//Se inicializa con la bandera en true para saber que si hubo cambios en el detalle Pedido
				boolean verificaPreciosDetallePedido = true;
				boolean verificaDetallePedido = true;
				boolean verificaEntregas = true;
				boolean verificaAbono = true;
				boolean verificarCambioDescuento = true;
				String valorValidacion = null;
				Boolean verificarCambioContacto = Boolean.FALSE;
				Boolean modificarReservaLQD = Boolean.FALSE;
				Boolean modificarReservaDisminucionMonto = Boolean.FALSE;
				//indica si la reserva que se va ha modifica se han quitado y agregado productos
				Boolean modificarReservaAumDis = Boolean.FALSE; 
				String tipoProceso = null;
				Boolean verificarAutorizacionesDsctoVar = Boolean.FALSE;
				String cambiosReserva="";
				LogSISPE.getLog().info("Se accede mediante 'botonRegistrarReservacion....'");
				LogSISPE.getLog().info("Registrando reservacion...");
				
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		
				Boolean mostrarDatosPersonaEmpresa = Boolean.FALSE;
				//se valida que el pedido en session sea el actual en la BDD
				Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO);
				
				//si el pedido no es el actual, no continua el proceso de guardar, validar siempre y cuando no se procese por la opci\u00F3n de crear una nueva reserva solo con los cambios, (nuevos articulos o nuevas cantidades)
				if(!pedidoActual){
					//cuando el pedido ha sido modificado desde otra sesion
					CotizacionReservacionUtil.instanciarVentanaPedidoModificado(request, "crearCotizacion.do");
					return mapping.findForward(salida);
				}

				//se verifica si el pedido no se encuentra en proceso de pago en el POS
				if(vistaPedidoDTO!=null && CotizacionReservacionUtil.reservaBloqueadaPOS(vistaPedidoDTO)){
					//cuando el pedido esta bloqueado en el punto de venta
					session.setAttribute(CotizarReservarAction.TITULO_POPUP_CONFIRMACION, "Informaci\u00F3n");
					WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.reservaBloqueadaPOS", 
							"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
					return mapping.findForward(salida);
				}
				
				//verifica si al momento de confirmar una modificacion de reserva no existe una orden de salida con guia de remision de las entregas con SICMER
				if(vistaPedidoDTO!=null && EstadoPedidoUtil.reservaBloqueadaDesdeSICMER(errors,vistaPedidoDTO.getId().getCodigoPedido(),vistaPedidoDTO.getId().getCodigoCompania())) {
					String accionPopUp="confirmar";
					String accionOk="requestAjax('crearCotizacion.do', ['pregunta','div_pagina'], {parameters: 'siVolverBusqueda=ok', evalScripts:true});ocultarModal();";
					CotizacionReservacionUtil.instanciarPopUpNotificacionBloqueoReservaSICMER(request, vistaPedidoDTO.getLlaveContratoPOS(),accionPopUp,accionOk);
					return mapping.findForward(salida);
				}
				
				if((formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)==null){
					if(formulario.getTipoDocumentoContacto()==null && formulario.getBotonRegistrarReservacion()!=null && formulario.getBotonRegistrarReservacion().equals("regReservacion")){
						ContactoUtil.ejecutarAccionControlador(request, response, "#{empresaController.editarLocalizacion()}");
						session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminLocalizacion.jsf");
						ContactoUtil.mostrarPopUpCorporativo(request, session, "editarEmpresa", formulario,errors);
						return mapping.findForward(salida);
					}
				}
				
				//verificamos que tipo de modificacion se va ha realizar
				if(vistaPedidoDTO != null && vistaPedidoDTO.getCodigoEstadoPagado()!=null //si viene desde una cotizacion o recotizacion el estado pagado es null
						&& vistaPedidoDTO.getCodigoEstadoPagado().equals(CODIGO_ESTADO_PAGADO_LIQUIDADO)){
					tipoProceso = CotizacionReservacionUtil.verificarCambioDetallePedido(request);
					LogSISPE.getLog().info("----------Tipo de proceso de modificacion de la reserva----------{}", tipoProceso);
				}
				
				//se verifica si hay errores en las entregas
				if(request.getAttribute(CotizarReservarForm.ERROR_ENTREGAS) != null){
					if(vistaPedidoDTO == null){//cuando se viene desde la pantalla de cotizacion esta vista esta en null
						infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
						//se cargan las horas y minutos para seleccionar en el combo de las entregas
						session.setAttribute(EntregaLocalCalendarioAction.HORACOL, obteneHorasMinutos(false));
						//session.setAttribute(EntregaLocalCalendarioAction.MINUTOCOL, minutos());
						//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
						salida = pasarAEntregas(request, formulario);
						saveErrors(request, errors);
						//se termina el m\u00E9todo
						return mapping.findForward(salida);
					}
					else if (vistaPedidoDTO.getCodigoEstadoPagado().equals(CODIGO_ESTADO_PAGADO_LIQUIDADO) && tipoProceso.equals(TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS)) {
						infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
						//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
						session.setAttribute(EntregaLocalCalendarioAction.HORACOL, obteneHorasMinutos(false));
						//session.setAttribute(EntregaLocalCalendarioAction.MINUTOCOL, minutos());
						salida = pasarAEntregas(request, formulario);
						saveErrors(request, errors);
						//se termina el m\u00E9todo
						return mapping.findForward(salida);
					}
					else if (!vistaPedidoDTO.getCodigoEstadoPagado().equals(CODIGO_ESTADO_PAGADO_LIQUIDADO)) {
						
						//Se verifica si el check de reservar stock entrega est\u00E1 seleccionado
						if (formulario.getCheckReservarStockEntrega()!=null && formulario.getCheckReservarStockEntrega().equals(estadoActivo)){													
							try{			
								Double valorAbonoSistema = Util.roundDoubleMath((Double)session.getAttribute(CotizarReservarAction.VALOR_ABONO), NUMERO_DECIMALES);
								Double valorAbonoManual = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()),NUMERO_DECIMALES);									
								Double total = Util.roundDoubleMath(formulario.getTotal(), NUMERO_DECIMALES);
								
								if(valorAbonoManual.doubleValue() > total.doubleValue()){
									errors.add("valorAbono",new ActionMessage("errors.pedido.abonoExcedido",valorAbonoManual,total));
								}else if(valorAbonoManual.doubleValue() < valorAbonoSistema.doubleValue()){
									errors.add("valorAbono",new ActionMessage("message.reservacion.valorMinimoAbono"));
								}else{
									session.setAttribute(HORACOL, obteneHorasMinutos(false));
									//session.setAttribute(MINUTOCOL, minutos());
									EntregaLocalCalendarioUtil.obtenerFechaMinimaEntrega(request,errores,formulario);
									//se llama a la ventana de ReservarbStock Entrega
									instanciarVentanaReservarStockEntrega(request);	
								}	
								verificarstock=false;
							}catch(NumberFormatException e){
								errors.add("Exception",new ActionMessage("errors.formato.double","Valor abono"));
								verificarstock=false;
							}
						}else{
							infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
							session.setAttribute(EntregaLocalCalendarioAction.HORACOL, obteneHorasMinutos(false));
							//session.setAttribute(EntregaLocalCalendarioAction.MINUTOCOL, minutos());

							//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
							salida = pasarAEntregas(request, formulario);
							saveErrors(request, errors);
							//se termina el m\u00E9todo
							return mapping.findForward(salida);
						}
					}
				}				
							
				//primero se verifica si la transacci\u00F3n ya fue realizada
				if(session.getAttribute(TRANSACCION_REALIZADA)==null )//&& verificarstock==true
				{
					session.setAttribute(EntregaLocalCalendarioAction.HORACOL, obteneHorasMinutos(false));
					//session.setAttribute(EntregaLocalCalendarioAction.MINUTOCOL, minutos());
					//se elimina de los detalles las autorizaciones de stock ke fueron eliminados
					AutorizacionesUtil.eliminarAutorizacionesHijasRechazadas((Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO), request);
					
					String entidadLocal = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");//LOCAL
					LogSISPE.getLog().info("---Entro en el if de transaccion realizada---");
//					//se obtiene el detalle del Pedido
//					Collection<DetallePedidoDTO> detalleReservacion = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);							
				
					PedidoDTO pedidoDTO = crearPedidoDTO(accion, formulario, request);
					
					if(formulario.getBotonRegistrarReservacion()!=null && formulario.getBotonRegistrarReservacion().equals("siCamEntResponsable")){
						pedidoDTO.setEntidadResponsable(entidadLocal);
						formulario.setResponsableLocal(true);
						session.setAttribute(SessionManagerSISPE.POPUP, null);
					}
					
					String entidadResponsable = pedidoDTO.getEntidadResponsable();//BODEGA
					
					if(entidadResponsable==null){				
						pedidoDTO = (PedidoDTO)session.getAttribute(PEDIDO_GENERADO);
						entidadResponsable = pedidoDTO.getEntidadResponsable();
						request.setAttribute("aceptarReservarStockEntrega", null);
						session.setAttribute(SessionManagerSISPE.POPUP, null);
					}
					
					String entidadResponsable1 = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");//BOD
					LogSISPE.getLog().info("entidadResponsable--{}",entidadResponsable);
					LogSISPE.getLog().info("entidadResponsable1--{}",entidadResponsable1);
					if(StringUtils.isNotEmpty(entidadResponsable) && StringUtils.isNotEmpty(entidadResponsable1) && 
							entidadResponsable.equals(entidadResponsable1)){
						valorValidacion =  CotizacionReservacionUtil.verificaDesLocEntResp(request,pedidoDTO,formulario.getTipoEntrega());
						LogSISPE.getLog().info("---Valor de la validacion--{}",valorValidacion);
					}
					
					// Modificacion de las reservas
					if(pedidoDTO.getNpModificarReserva() != null && pedidoDTO.getNpModificarReserva().equals(estadoActivo)){
						if(valorValidacion != null && (valorValidacion.equals("ok1") || valorValidacion.equals("ok2"))){
							//banderas para que no perminta la modificacionReserva
							verificaPreciosDetallePedido = false;
							verificaDetallePedido = false;
							verificaEntregas = false;
							verificaAbono = false;
							verificarCambioDescuento = false;
							
						}//else if (!formulario.getBotonRegistrarReservacion().equals("siCamEntResponsable")){
							//se debe validar siempre si hay cambios en el pedido
							LogSISPE.getLog().info("----------EMPIEZA EL PROCESO DE COMPARACION DEL DETALLE PEDIDO--------");
							
							vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
							//1ro Verifica si hubo cambio de Precios en los totales y subtotales del pedido
							verificaPreciosDetallePedido = CotizacionReservacionUtil.verificarPreciosDetallePedido(request);
							cambiosReserva=verificaPreciosDetallePedido==true?cambiosReserva.concat("precios,"):cambiosReserva;
							//2do Verifica si hubo eliminacion o modificacion de articulos en el pedido
							verificaDetallePedido = CotizacionReservacionUtil.verificarCambiosDetallePedido(request);
							cambiosReserva=verificaDetallePedido==true?cambiosReserva.concat("cantidades y/o se agreg\u00F3/quito art\u00EDculos,"):cambiosReserva;
							//3ro Verifica si hubo cambios entregas o entidad Responsable
							verificaEntregas = CotizacionReservacionUtil.verificarCambiosEntregas(request, pedidoDTO.getEntidadResponsable());
							cambiosReserva=verificaEntregas==true?cambiosReserva.concat("entregas,"):cambiosReserva;
							//4to Verifica si hubo cambios en los abonos
							verificaAbono = CotizacionReservacionUtil.verificarAbonoPedido(request, formulario);
							cambiosReserva=verificaAbono==true?cambiosReserva.concat("valor del abono,"):cambiosReserva;
							//5to Verifica si cambio el contacto en la reserva
							if(CotizacionReservacionUtil.isCambioContacto(vistaPedidoDTO, formulario,request)){
								verificarCambioContacto = Boolean.TRUE;
								pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(TIPO_RESERVACION_CAMBIO_CONTACO);
							}
							cambiosReserva=verificarCambioContacto==true?cambiosReserva.concat("contacto,"):cambiosReserva;
							//6to se verifica si solicitaron autorizacion para descuento variable
							verificarAutorizacionesDsctoVar = AutorizacionesUtil.existenAutorizacionesPorAplicar((ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES));
							cambiosReserva=verificarAutorizacionesDsctoVar==true?cambiosReserva.concat("autorizaciones de descuento variable,"):cambiosReserva;
							
							verificarCambioDescuento = CotizacionReservacionUtil.verificarExisteCambiosDescuentosReserva(request);
							cambiosReserva=verificarCambioDescuento==true?cambiosReserva.concat("descuentos"):cambiosReserva;
							
							//Impresion de cambios al modificar la Reserva
							LogSISPE.getLog().info("----------Existen cambios en los precios del detalle Pedido--------{}", verificaPreciosDetallePedido);
							LogSISPE.getLog().info("----------Existen cambios en el detalle Pedido--------{}", verificaDetallePedido);
							LogSISPE.getLog().info("----------Existen cambios en las entregas del detalle Pedido--------{}", verificaEntregas);
							LogSISPE.getLog().info("----------Existen cambios en los abonos del pedido--------{}", verificaAbono);
							LogSISPE.getLog().info("----------Existen Cambios en el contacto en la reserva----------{}", verificarCambioContacto);
							LogSISPE.getLog().info("----------Existen Cambios en los descuentos variables----------{}", verificarAutorizacionesDsctoVar);
							
							//asignamos el tipo proceso
							if(tipoProceso==null){
								tipoProceso = CotizacionReservacionUtil.verificarCambioDetallePedido(request);
								LogSISPE.getLog().info("----------Tipo de proceso de modificacion de la reserva----------{}", tipoProceso);
								
							} //si solo cambiaron los precios y no los detalles del pedido significa que se debe a cambio de descuentos en el pedido

							if (verificaPreciosDetallePedido && !verificaDetallePedido && !verificaEntregas && !verificaAbono && !verificarCambioContacto){
								//asignamos el nuevo tipo de proceso
								tipoProceso = TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
							
							}
							//jparedes
							//Estas lineas se incrementan porque cuando se modifica la reserva solo por descuentos y si no alcanza el valor del primer abono tambien cambia el verificaAbono
							if(verificaPreciosDetallePedido && verificaAbono && verificarCambioDescuento && tipoProceso.equals(ConstantesGenerales.ESTADO_INACTIVO)){
								//asignamos el nuevo tipo de proceso
								tipoProceso = TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
							}

							//se sobreescribe el tipo de proceso, para el POS se va ha utilizar el mismo
							// en el caso TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS = TIPO_PROCESO_AUMENTAR_PRODUCTOS, solo para el POS
							if(tipoProceso.equals(TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS)){
								pedidoDTO.setNpTipoProceso(TIPO_PROCESO_AUMENTAR_PRODUCTOS);
							}
							//siempre que quite o ponga descuentos debe generarse un nuevo pedido
							if(verificaPreciosDetallePedido && verificarCambioDescuento){
								//asignamos el nuevo tipo de proceso
								tipoProceso = TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO;
								pedidoDTO.setNpTipoProceso(tipoProceso);
							}
							else {
								pedidoDTO.setNpTipoProceso(tipoProceso);
							}
							
							//si hubo cambios en el precio o en detalle del pedido, obtenemos los cambio que se realizaron
							if (verificaPreciosDetallePedido || verificaDetallePedido) {
								//integracion SISPE - POS
								pedidoDTO.setNpDiferenciaDetallePedidoPOS(CotizacionReservacionUtil.compararDetallePedidoPOS(request, tipoProceso));
								pedidoDTO.setNpArticulosNuevosPedidoCol(CotizacionReservacionUtil.obtenerDetallePedidoNuevos(request));
								//si se cambio el contacto y los detalles								
								pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(verificarCambioContacto ? 
										TIPO_RESERVACION_MODIFICACION_CONTACTO : TIPO_RESERVACION_MODIFICACION);								
								LogSISPE.getLog().info("----------TIPO_RESERVACION_MODIFICACION cambio contacto y los detalles---------- {}" , pedidoDTO.getEstadoPedidoDTO().getNpEstadoModificacionReserva());
								
							}else if(verificaEntregas) {
								//si se cambio el contacto y las entregas
								pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(verificarCambioContacto ? 
										TIPO_RESERVACION_MODIFICACION_CONTACTO : TIPO_RESERVACION_MODIFICACION);								
								LogSISPE.getLog().info("----------TIPO_RESERVACION_MODIFICACION cambio contacto y los detalles---------- {}" , pedidoDTO.getEstadoPedidoDTO().getNpEstadoModificacionReserva());
							}
							
							if(pedidoDTO.getEstadoPedidoDTO().getCodigoEstadoPagado().equals(MessagesWebSISPE.getString("codigoEstadoPagadoLiquidado"))){
								if(tipoProceso.equals(TIPO_PROCESO_QUITAR_PRODUCTOS)){
									modificarReservaDisminucionMonto = Boolean.TRUE;//cuando solo se devolvieron articulos
								}else if(tipoProceso.equals(TIPO_PROCESO_AUMENTAR_PRODUCTOS)){
									modificarReservaLQD = Boolean.TRUE; //cuando solo agregaron articulos
								}else if (tipoProceso.equals(TIPO_PROCESO_QUITAR_AUMENTAR_PRODUCTOS)) {
									modificarReservaAumDis = Boolean.TRUE;//cuando se agregaron y disminuyeron articulos
									tipoProceso = TIPO_PROCESO_AUMENTAR_PRODUCTOS;
									pedidoDTO.setNpTipoProceso(tipoProceso);
								}
							}else if(session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO) != null && 
									pedidoDTO.getEstadoPedidoDTO().getCodigoEstadoPagado().equals(MessagesWebSISPE.getString("codigoEstadoPagadoTotal"))){
								modificarReservaDisminucionMonto = Boolean.TRUE;
							}
							pedidoDTO.setNpLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
						}
				   // }					
					
					//ponemos en session el pedido que vamos a modificar la reserva
					session.setAttribute("ec.com.smx.sic.sispe.pedidoReservar", pedidoDTO);
					
					//validaciones antes del proceso reserva
					if(valorValidacion != null && valorValidacion.equals("ok1") && formulario.getResponsableLocal()==false){
						//Se a\u00F1ade el mensaje que no puede la bodega hacerse responsable del pedido.
						instanciarPopUpAvisoResponsable(request, valorValidacion);
					}else if(valorValidacion != null && valorValidacion.equals("ok2") && formulario.getResponsableLocal()==false){
						//Se a\u00F1ade el mensaje que no existe solo canastas de catalago y no hay ninguna entrega a domicilio
						instanciarPopUpAvisoResponsable(request, valorValidacion);
					}else if (modificarReservaLQD) { 
						LogSISPE.getLog().info("::::::::::::::: Se muestra el popUp para validar se crea una nueva factura pedido LQD");
						instanciarPopUpModReservaPedPTO(request);
						saveErrors(request, errors);
						return mapping.findForward("desplegar");
					}else if (modificarReservaDisminucionMonto) {
						LogSISPE.getLog().info(":::: Se muestra el popUp indicando un vale de caja en estadoPedido PTO y PPA");
						instanciarPopUpDisminusionReserva(request);
					}else if (modificarReservaAumDis) {
						LogSISPE.getLog().info(":::: Aumento y modificacion de productos");						
						instanciarPopUpAumDisReserva(request);						
					}else if(!verificaPreciosDetallePedido && !verificaDetallePedido && !verificaEntregas && !verificaAbono && !verificarCambioContacto && !verificarCambioDescuento && !verificarAutorizacionesDsctoVar){
						//si no se realizaron cambios en el pedido
						errors.add("modificacionReserva",new ActionMessage("errors.modificarReserva.detallePedido"));
					
					}else if(verificarCambioContacto && !verificaPreciosDetallePedido && !verificaDetallePedido && !verificaEntregas && !verificaAbono && !verificarCambioDescuento && !verificarAutorizacionesDsctoVar){							
						LogSISPE.getLog().info("----------Solo se cambio el contacto en la reserva----------");
						
						PedidoDTO pedidoDTOAux = (PedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
						pedidoDTOAux.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());//campos de auditoria
						
						//se obtiene el detalle del Pedido
						Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
						//request.getAttribute(CotizarReservarForm.ERROR_ENTREGAS)
						if(request.getAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION) != null){
							infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
							//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
							salida = pasarAEntregas(request, formulario);
							saveErrors(request, errors);
							//se termina el m\u00E9todo
							return mapping.findForward(salida);
//						}else if(!WebSISPEUtil.verificarProblemasStock(requecost, colDetallePedido)){	
						//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//						}else if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacion(request, colDetallePedido)){
						}else if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacionAnterior(request, colDetallePedido)){
							
							salida = actualizarReservaMC(formulario, request, infos, errors, warnings, exitos, salida, beanSession,cambiosReserva);
							session.removeAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION);
							session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
							if(salida.equals("registro")){
								mostrarDatosPersonaEmpresa = Boolean.TRUE;
							}
						}else{
							LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
							//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//							AutorizacionesUtil.agregarAutorizacionPorTipo(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK);
							AutorizacionesUtil.agregarAutorizacionPorTipoAnterior(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, warnings);
							AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, Boolean.FALSE, null);
//							instanciarPopAutorizacionStockArtEsp(request);
						}
												
						pedidoDTOAux = null;
						
					}else{//si paso todas las validaciones comienza el proceso de reserva						
						
						//se obtiene el detalle del Pedido
						Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
						
						//primero se realiza la validacion de las entregas
						AutorizacionesUtil.verificarResponsableEntrega(colDetallePedido, request);
						
						if(EntregaLocalCalendarioUtil.verificarEntregasProximoDespacho(request)){
							LogSISPE.getLog().info("mostrar popUp editar entregas por entregas proximas a despachar");
							CrearReservacionAction.instanciarPopUpDespachosProximos(request);
							return mapping.findForward(salida);
						}
						
						//verificamos nuevamente las entregas
						if(request.getAttribute(CotizarReservarForm.ERROR_ENTREGAS) != null){							
							if (formulario.getCheckReservarStockEntrega()!=null && formulario.getCheckReservarStockEntrega().equals(estadoActivo)){													
								try{			
									Double valorAbonoSistema = Util.roundDoubleMath((Double)session.getAttribute(CotizarReservarAction.VALOR_ABONO), NUMERO_DECIMALES);
									Double valorAbonoManual = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()),NUMERO_DECIMALES);									
									Double total = Util.roundDoubleMath(formulario.getTotal(), NUMERO_DECIMALES);
									
									if(valorAbonoManual.doubleValue() > total.doubleValue()){
										errors.add("valorAbono",new ActionMessage("errors.pedido.abonoExcedido",valorAbonoManual,total));
									}else if(valorAbonoManual.doubleValue() < valorAbonoSistema.doubleValue()){
										errors.add("valorAbono",new ActionMessage("message.reservacion.valorMinimoAbono"));
									}else{
										session.setAttribute(HORACOL, obteneHorasMinutos(false));
										//session.setAttribute(MINUTOCOL, minutos());
										EntregaLocalCalendarioUtil.obtenerFechaMinimaEntrega(request,errores,formulario);
										instanciarVentanaReservarStockEntrega(request);	
									}									
								}catch(NumberFormatException e){
									errors.add("Exception",new ActionMessage("errors.formato.double","Valor abono"));
								}
							}else{
								infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
								//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
								salida = pasarAEntregas(request, formulario);
								saveErrors(request, errors);
								//se termina el m\u00E9todo
								return mapping.findForward(salida);
							}
						}else
							//Validacion si se configuro pedir al cd y articulos sin stock(pavos,canastas y pollos)
//							if(!WebSISPEUtil.verificarProblemasStock(request, colDetallePedido)){
							//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//							if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacion(request, colDetallePedido)){
							if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacionAnterior(request, colDetallePedido)){
								
								//nuevas y modificaciones de reservas que que no tienen estado LQD
								try{
									salida = reservarPedido(formulario, request, infos, errors, warnings, exitos, salida, beanSession,cambiosReserva);
									session.removeAttribute(CotizarReservarForm.ERROR_ENTREGAS);
									session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
	//								session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
									formulario.setBotonRegistrarReservacion(null);
									formulario.setResponsableLocal(false);
									if(salida.equals("registro")){
										mostrarDatosPersonaEmpresa = Boolean.TRUE;
									}
								}catch(Exception ex){
									LogSISPE.getLog().info("Error al registrar la reserva, restaurando a la pagina de reservacion",ex);
									errors.add("errorReservarPedido", new  ActionMessage("errors.gerneral", "Error al registrar la reserva "+ex.getMessage()));
								}

							}else{
								LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
								//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//								AutorizacionesUtil.agregarAutorizacionPorTipo(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK);
								AutorizacionesUtil.agregarAutorizacionPorTipoAnterior(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, warnings);
								if(infos.size() == 0){
									session.setAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION,  MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"));
									AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, Boolean.FALSE, null);
								}
								else if(session.getAttribute("ec.com.smx.sic.sispe.confirmar.pedido") != null 
										&& (Boolean)session.getAttribute("ec.com.smx.sic.sispe.confirmar.pedido")){
									infos = new ActionMessages();
									try{
										salida = reservarPedido(formulario, request, infos, errors, warnings, exitos, salida, beanSession, cambiosReserva);
										session.removeAttribute(CotizarReservarForm.ERROR_ENTREGAS);
										session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
		//								session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
										formulario.setBotonRegistrarReservacion(null);
										formulario.setResponsableLocal(false);
										if(salida.equals("registro")){
											mostrarDatosPersonaEmpresa = Boolean.TRUE;
										}
									}catch(Exception ex){
										LogSISPE.getLog().info("Error al registrar la reserva, restaurando a la pagina de reservacion"+ex);
										errors.add("errorReservarPedido", new  ActionMessage("errors.gerneral", "Error al registrar la reserva "+ex.getMessage()));
									}
								}
//								instanciarPopAutorizacionStockArtEsp(request);
							}
						}
				}else{
						salida="registro";
						/*if(verificarstock==false){
							salida="desplegar";
							verificarstock=true;
						}*/
				}
				//Verificacion diferidos
				WebSISPEUtil.verificarDiferidosCotizarRes(request);
				
				if(mostrarDatosPersonaEmpresa){
					//se cargan los datos del contacto
					ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
					//ejecutar el metodo para inicializar el controlador adecuado
					ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
					
					//se envian las notificacion PUSH a los dispositivos de los autorizadores
					AutorizacionesUtil.enviarNotificacionAutorizadores(request);
					session.setAttribute(MOSTRAR_RESUMEN_PEDIDO, "ok");
				}
				
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
			
			}else if (formulario.getBotonConsolidarPedidos()!=null){
				LogSISPE.getLog().info("Presiono el boton cancelar");
				instanciarPopUpPreguntaConsolidacion(request);
				salida = "desplegar";
				
			}else if (request.getParameter("cancelarModificarReserva") != null) {
				
				//se cierra el popUp
				LogSISPE.getLog().info("Se cancela la modificacion de un reserva");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegar";
				
			}else if (request.getParameter("cancelarModificarReservaLQD") != null){
				
				LogSISPE.getLog().info("Se cancela la modificacion de una reserva en estado LQD solo aumentaron productos");
				Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO_CLONE_AUX);
				
				if(detallePedido!=null && !detallePedido.isEmpty()){	
		          CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
		          //llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
		          CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
		          //lamada al m\u00E9todo que actualiza el valor del saldo del abono
		          //actualizarSaldoAbonoPedido(request, formulario);
		        }
				
				session.setAttribute(DETALLE_PEDIDO, detallePedido);
				
				// Objetos para construir los tabs
				beanSession.setPaginaTab(ContactoUtil.construirTabsContactoPedido(request, formulario));
				
				//session.removeAttribute(POPUP_DIFERENCIA_PEDIDO);
				session.removeAttribute(DETALLE_PEDIDO_CLONE);
				session.removeAttribute(DETALLE_PEDIDO_CLONE_AUX);
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				
			}else if (request.getParameter("verPopUpDetallesCambio") != null && formulario.getRadioReserva().equals("nuevaReserva")) {
				
				LogSISPE.getLog().info("PopUp diferencia del detalle del pedido para la nueva reserva");				
				session.removeAttribute(SessionManagerSISPE.POPUP);				
				
				Collection<DetallePedidoDTO> detallePedido = CotizacionReservacionUtil.calcularDiferenciaDetallePedido(request, formulario, errores);				
				Collection<DetallePedidoDTO> detallePedidoColClone = (Collection<DetallePedidoDTO>)session.getAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
				Collection<DetallePedidoDTO> detallePedidoModClone = (Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)(Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO));
				
				session.setAttribute(DETALLE_PEDIDO_CLONE_AUX, detallePedidoModClone);
				session.setAttribute(DETALLE_PEDIDO_CLONE, detallePedidoColClone);
				session.setAttribute(DETALLE_PEDIDO, detallePedido);				
				instanciarPopUpNuevaReservaLQD(request, beanSession, detallePedido.iterator().next().getId().getCodigoPedido());
			
			}else if (request.getParameter("modificarReserva") != null) {
				
				LogSISPE.getLog().info("Nueva reserva con la diferencia de los detalles del pedido");
				
				Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				VistaPedidoDTO vistaPedidoDTOClone = new VistaPedidoDTO();
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				
				vistaPedidoDTOClone = vistaPedidoDTO.clone();				
				vistaPedidoDTOClone.setAbonoPedido(0D);
				vistaPedidoDTOClone.setCodigoEstadoPagado(CODIGO_ESTADO_SINPAGO);
				vistaPedidoDTOClone.setAbonosPedidos(new ArrayList<AbonoPedidoDTO>());
				vistaPedidoDTOClone.setLlaveContratoPOS(null);
				vistaPedidoDTOClone.setNpEsNuevaReserva(Boolean.TRUE);
				vistaPedidoDTOClone.setNpCrearNuevoPedido(Boolean.TRUE);
				//Inicializar este valor para que no se aplique a las condiciones de consolidaci\u00F3n
				formulario.setNumeroPedidoConsolidado(null);
				formulario.setDatosConsolidados(null);
				session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				session.removeAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO);
				
				session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTOClone);
		        //solo si el detalle del pedido no est\u00E1 vacio
		        if(detallePedido!=null && !detallePedido.isEmpty()){
		        	for (DetallePedidoDTO detallePedidoDTO : detallePedido) {
		        		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoBodega(null);
		        		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(null);
		        		DetalleCanastaAction.calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
					}
			        
		        	formulario.actualizarDetalleForm(request, errores, accion, estadoActivo, estadoInactivo,detallePedido);
		        	
		        	CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
			        //llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
			        CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
		        }
		        
		        //se inicializan los par\u00E1metros de la reservaci\u00F3n y algunos datos en sesi\u00F3n y el formulario
		        CrearReservacionAction.inicializarParametros(request, formulario);
		        
		        //se actualiza el detalle de la cotizaci\u00F3n que est\u00E1 en sesi\u00F3n
		        session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
		        //session.removeAttribute(ModificarReservacionAction.CODIGO_PAGADO_TOTALMENTE);
		        session.removeAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR);
		       
		        session.setAttribute(ModificarReservacionAction.PEDIDO_SIN_PAGO, "ok");
		        
		        formulario.setChecksSeleccionar(null);
		        formulario.setCheckSeleccionarTodo(null);
		        formulario.setCheckActualizarStockAlcance(null);
		        formulario.setValorAbono(null);
		        // Objetos para construir los tabs, si fue modificacion de reserva
				beanSession.setPaginaTab(ContactoUtil.construirTabsContactoPedido(request, formulario));
				
		        infos.add("confirmacionNuevaReserva", new ActionMessage("info.modificacionReserva.mensaje"));
				session.removeAttribute(SessionManagerSISPE.POPUP);	
				saveInfos(request, infos);
				saveErrors(request, errors);
				return mapping.findForward("desplegar");
			
			}else if (request.getParameter("modificarReservaDismAum") != null || 
					(session.getAttribute(OPCION_MODIFICAR_RESERVA) != null && session.getAttribute(OPCION_MODIFICAR_RESERVA).equals("modificarReservaDismAum"))) {
				
				session.removeAttribute(OPCION_MODIFICAR_RESERVA);
				//se obtiene el detalle del Pedido
				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				//Validacion si se configuro pedir al cd y articulos sin stock(pavos,canastas y pollos)
				//if(!WebSISPEUtil.verificarProblemasStock(request, colDetallePedido)){
				//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//				if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacion(request, colDetallePedido)){	
				if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacionAnterior(request, colDetallePedido)){
					try{
						//se debe anular todo le pedido anterior y crear uno nuevo con la nueva reserva
	 					salida = reservarPedido(formulario, request, infos, errors, warnings, exitos, salida, beanSession,null);
						//se cierra el popUp
						session.removeAttribute(SessionManagerSISPE.POPUP);
						session.removeAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION);
						session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
					}catch(Exception ex){
						LogSISPE.getLog().info("Error al registrar la reserva, restaurando a la pagina de reservacion"+ex);
					}

					
				}else{
					session.setAttribute(OPCION_MODIFICAR_RESERVA, "modificarReservaDismAum");
					LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
					//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//					AutorizacionesUtil.agregarAutorizacionPorTipo(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK);
					AutorizacionesUtil.agregarAutorizacionPorTipoAnterior(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, warnings);
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, Boolean.FALSE, null);
//					instanciarPopAutorizacionStockArtEsp(request);
				}
				//Verificacion diferidos
				WebSISPEUtil.verificarDiferidosCotizarRes(request);
			}			
			
			else if ((request.getParameter("verPopUpDetallesCambio") != null && formulario.getRadioReserva().equals("factura")) || 
					(session.getAttribute(OPCION_MODIFICAR_RESERVA) != null && session.getAttribute(OPCION_MODIFICAR_RESERVA).equals("verPopUpDetallesCambio")) ) {
				
				session.removeAttribute(OPCION_MODIFICAR_RESERVA);
				
				LogSISPE.getLog().info("mantener reserva actual");
				//se obtiene el detalle del Pedido
				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				validarEntregas(request, errors, session, colDetallePedido, infos);
				
				if(request.getAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION) != null){
					infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
					//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
					salida = pasarAEntregas(request, formulario);
					saveErrors(request, errors);
					//se cierra el popUp
					session.removeAttribute(SessionManagerSISPE.POPUP);
					//se termina el m\u00E9todo
					return mapping.findForward(salida);
				}else 
					//Validacion si se configuro pedir al cd y articulos sin stock(pavos,canastas y pollos)
				//	if(!WebSISPEUtil.verificarProblemasStock(request, colDetallePedido)){
					//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//				if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacion(request, colDetallePedido)){	
				if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacionAnterior(request, colDetallePedido)){
					try{
						//se debe anular todo le pedido anterior y crear uno nuevo con la nueva reserva
	 					salida = reservarPedido(formulario, request, infos, errors, warnings, exitos, salida, beanSession,null);
						//se cierra el popUp
						session.removeAttribute(SessionManagerSISPE.POPUP);
						session.removeAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION);
						session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
						if(salida.equals("registro")){					
							//se cargan los datos del contacto
							ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, errores);
							//ejecutar el metodo para inicializar el controlador adecuado
							ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
						}
						
					}catch(Exception ex){
						LogSISPE.getLog().info("Error al registrar la reserva, restaurando a la pagina de reservacion"+ex);
					}

				}else{
					session.setAttribute(OPCION_MODIFICAR_RESERVA, "verPopUpDetallesCambio");
					LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
					//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//					AutorizacionesUtil.agregarAutorizacionPorTipo(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK);
					AutorizacionesUtil.agregarAutorizacionPorTipoAnterior(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, warnings);
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, Boolean.FALSE, null);
//					instanciarPopAutorizacionStockArtEsp(request);
				}
				//Verificacion diferidos
				WebSISPEUtil.verificarDiferidosCotizarRes(request);
				
			}else if (request.getParameter("modificarReservaDismi") != null || 
				(session.getAttribute(OPCION_MODIFICAR_RESERVA) != null && session.getAttribute(OPCION_MODIFICAR_RESERVA).equals("modificarReservaDismi")) ) {//reserva LQD, PTO y PPA que tiene devulucion
					
				session.removeAttribute(OPCION_MODIFICAR_RESERVA);			

				LogSISPE.getLog().info("sale modificarReservaDismi");
				//se obtiene el detalle del Pedido
				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				
				validarEntregas(request, errors, session, colDetallePedido, infos);
				
				if(request.getAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION) != null){
					infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
					//se llama a la funci\u00F3n que pasa el control a la pantalla de entregas
					salida = pasarAEntregas(request, formulario);
					saveErrors(request, errors);
					//se cierra el popUp
					session.removeAttribute(SessionManagerSISPE.POPUP);
					//se termina el m\u00E9todo
					return mapping.findForward(salida);
				}else 
					//Validacion si se configuro pedir al cd y articulos sin stock(pavos,canastas y pollos)
//					if(!WebSISPEUtil.verificarProblemasStock(request, colDetallePedido)){
					//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//					if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacion(request, colDetallePedido)){
					if(!WebSISPEUtil.tieneProblemasDeStockPorClasificacionAnterior(request, colDetallePedido)){
						try{
							salida = reservarPedido(formulario, request, infos, errors, warnings, exitos, salida, beanSession,null);
							//se cierra el popUp
							session.removeAttribute(SessionManagerSISPE.POPUP);	
							session.removeAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION);
						session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
						}catch(Exception ex){
							LogSISPE.getLog().info("Error al registrar la reserva, restaurando a la pagina de reservacion"+ex);
						}
				}else{
					session.setAttribute(OPCION_MODIFICAR_RESERVA, "modificarReservaDismi");
					LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
					//bgudino descomentar esta linea y comentar la siguiente para usar la nueva funcionalidad de las autorizaciones de stock
//					AutorizacionesUtil.agregarAutorizacionPorTipo(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK);
					AutorizacionesUtil.agregarAutorizacionPorTipoAnterior(request, colDetallePedido, infos, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, warnings);
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, true, ConstantesGenerales.TIPO_AUTORIZACION_STOCK, Boolean.FALSE, null);
//					instanciarPopAutorizacionStockArtEsp(request);
				}
				//Verificacion diferidos
				WebSISPEUtil.verificarDiferidosCotizarRes(request);
			}			
			else if(request.getParameter("aceptarSalirCon") != null){
				cargarPedidosConsolidados(request, formulario, session,estadoActivo);
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute("ec.com.smx.sic.sispe.estadosPago");
				session.removeAttribute(COL_DESCUENTOS);
				session.removeAttribute(COL_DESC_SELECCIONADOS);
				session.removeAttribute(COL_DESC_VARIABLES);
				session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS_PEDIDO_GENERAL);
				session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
				session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS);
				//wc variable para popUp autorizacion descuentos variable
				session.removeAttribute(AUTORIZACION_DESCUENTOS);
				session.removeAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios");
				
				//se limpian las variables para que cargue correctamente cuando ingresa a consolidar otros pedidos 
				session.removeAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
				session.removeAttribute(DES_MAX_NAV_EMP);
				session.removeAttribute(CHECK_PAGO_EFECTIVO);
				session.removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
				
				salida="desplegarConsolidarPedido";
			}
			else if(request.getParameter("cancelarCon") != null){
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegar";
			}
			//Presentar el detalle del pedido desde p\u00E1ginas que usan VistaPedidoDTO 
			else if (request.getParameter("linkConsolidadoGeneral") != null){				
	
				removerVariablesSession(session);
				session.removeAttribute(COL_DESC_SELECCIONADOS);
				session.removeAttribute(COL_DESC_VARIABLES);
				session.removeAttribute("ec.com.smx.sic.sispe.sinDescuentos");
				session.removeAttribute(DETALLE_PEDIDO);
				session.removeAttribute(RESPALDO_DESCUENTOS_CONSOLIDADOS);
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
				
				session.setAttribute(POSICION_DIV ,request.getParameter( "posicionScroll" )); 
				session.setAttribute(ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
				
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
				String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
				//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
 				session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto", valorTipoArticuloCanastas);
				
				//subClasificacion para articulos tipo despensas 
		  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
		  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
				//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
				session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", valorTipoArticuloDespensas);
				
				int indice= Integer.parseInt(request.getParameter("linkConsolidadoGeneral"));
				LogSISPE.getLog().info("Indice-Pedido-->{}",indice);
				if(session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX) != null){
					VistaPedidoDTO vistaPedidoAux = (VistaPedidoDTO)CollectionUtils.get(session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX),indice);
					session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,vistaPedidoAux.getVistaPedidoDTOCol());
					session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS,vistaPedidoAux.getVistaPedidoDTOCol());
					Boolean sinPrecioAfiliado=Boolean.TRUE;
					for(VistaPedidoDTO visPedDTO:vistaPedidoAux.getVistaPedidoDTOCol()){
						if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
			    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			    			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			    			sinPrecioAfiliado=Boolean.FALSE;
			    			break;
			    		}
					}
					if(sinPrecioAfiliado){
		    			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
					}	
					
					List<DetallePedidoDTO> detallePedido = ConsolidarAction.construirDetallesPedidoDesdeVistaConsolidados(formulario, request,vistaPedidoAux.getVistaPedidoDTOCol(),Boolean.FALSE,Boolean.FALSE, errors);
					session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallePedido);
					CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, detallePedido);
					session.removeAttribute(COL_DESC_SELECCIONADOS);
					
					//obtener llaves de descuentos y valores de descuentos variables 
					CotizacionReservacionUtil.obtenerLLavesDescuentos(request, session, true);
					
					if (WebSISPEUtil.existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
						session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
					}
					if (WebSISPEUtil.existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
						session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
					}
					
					//Se construye el pedido consolidado
					ConsolidarAction.crearPedidoGeneral(request, formulario, session,detallePedido,infos,errores,errors,warnings,accion,estadoActivo,estadoInactivo,Boolean.FALSE);										
				}
				request.getSession().setAttribute("ec.com.smx.sic.sispe.pedidoResumenConsolidado", "ok");
				salida = "desplegarLinkConsolidado";
			}
			/**
			 * @author Wladimir L\u00F3pez
			 * Control para los tabs de persona y pedidos
			 */
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				
				String parametro= beanSession.getPaginaTab().getParametroRequest();
				if(!parametro.equals("rTabPopUp")){
					if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
						/*--------------- si se escogi\u00F3 el tab de personas ----------------*/
						LogSISPE.getLog().info("Se elige el tab de PERSONAS");
						//ContactoUtil.cambiarTabContactoPedidos(beanSession, 0);
						session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//						ContactoUtil.ejecutarAccionControlador(request, response, "#{personaEmpresaController.cargarBuscar()}");
						
					}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
						/*------------ si se escogi\u00F3 el tab de pedidos entregados --------------*/
						LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
						//ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
						session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
					}
				}
				else{
					if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
						/*--------------- Detalles del nuevo pedido ----------------*/
						cambiarTabsDiferenciasDetallePed(beanSession,0);
					}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
						/*--------------- Cambios en el pedido ----------------*/
						LogSISPE.getLog().info("Cambios en el pedido");
						cambiarTabsDiferenciasDetallePed(beanSession,1);
					}
				}
			}
			//consulta el estado de una autorizacion solicitada
			else if(request.getParameter("consultarAutorizacion") != null){
				LogSISPE.getLog().info("Consultando autorizacion para indice {}",request.getParameter("indiceArticulo"));
				int indice = new Integer(request.getParameter("indiceArticulo")).intValue();
				ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(DETALLE_PEDIDO);
				DetallePedidoDTO detallePedidoDTO = detallePedidoCol.get(indice);
				Long codigoTipoAutorizacion =  Long.valueOf(request.getParameter("tipoAutorizacion"));
				AutorizacionesUtil.buscarAutorizacionesPorArticulo(request, detallePedidoDTO, formulario, codigoTipoAutorizacion, errors);
				session.setAttribute(BORRAR_ACCION_ANTERIOR, Boolean.TRUE);
			}
			else if (formulario.getBotonGuardarConsolidarPedidos()!=null || ( session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)!=null && 
					((String)session.getAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION)).equals("registrarConsolidacion"))){
				LogSISPE.getLog().info("Guardar consolidados");
				try{
					//se indica que todos los pedidos manejan precio de afiliado, ya que el check siempre esta activo y desactivado
					if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null){
						formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
					}
					if(formulario.getDatosConsolidados()==null || formulario.getDatosConsolidados().size()==0){
						warnings.add("noExistenPedidos",new ActionMessage("warning.noexisten.pedidos.consolidar"));
						//se retorna el valor del forward
						salida= "desplegar";
					}
					else{
						if(AutorizacionesUtil.solicitarAutorizacion("registrarConsolidacion",request, formulario, exitos, warnings, infos, errors)){
							salida = "desplegar";
							return forward(salida, mapping, request, errors, exitos, warnings, infos, beanSession);
						}
						
						//se llama al m\u00E9todo que construye el PedidoDTO
						PedidoDTO pedidoDTO = new PedidoDTO();
						//se obtiene la vistaPedidoDTO
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						Collection<PedidoDTO> pedidoColActualizado= (Collection<PedidoDTO>)session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
						if(vistaPedidoDTO == null){
							//se obtiene la vistaPedidoDTO
							PedidoDTO pedidoActualizadoDTO = pedidoColActualizado.iterator().next();
							//cuando se guarda la modificaci\u00F3n de una cotizaci\u00F3n anterior
							pedidoDTO.getId().setCodigoAreaTrabajo(pedidoActualizadoDTO.getId().getCodigoAreaTrabajo());
							pedidoDTO.getId().setCodigoCompania(pedidoActualizadoDTO.getId().getCodigoCompania());
							pedidoDTO.getId().setCodigoPedido(pedidoActualizadoDTO.getId().getCodigoPedido());
						}else{
							//cuando se guarda la modificaci\u00F3n de una cotizaci\u00F3n anterior
							pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
							pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
							pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
						}
						
						LogSISPE.getLog().info("CODIGO COMPANIA: {}",pedidoDTO.getId().getCodigoCompania());
						
						//se obtiene el detalle del Pedido
						Collection <DetallePedidoDTO> colDetallePedidoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
						//se ordena la colecci\u00F3n del detalle del pedido antes de enviarla a guardar al igual que los c\u00F3digos
						//de art\u00EDculos
						//Se verifica si tiene autorizaciones solicitadas
						ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
						if(colaAutorizaciones!=null && !colaAutorizaciones.isEmpty()){
							pedidoDTO.setNpAutorizacionesEstadoDetallePedidoCol(colaAutorizaciones);
							
							Boolean solicitoAutorizacion = Boolean.FALSE;
							//se actualizan los detalles con los datos nuevos de la cola de autorizaciones
							for(EstadoPedidoAutorizacionDTO autorizacionCola : colaAutorizaciones){
								
								//se recorren los detalles
								for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
									if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
										//se recorren las autorizaciones del detalle
										for(DetalleEstadoPedidoAutorizacionDTO autorizacionDetalle : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
											if(autorizacionCola.getValorReferencial() != null && autorizacionDetalle.getEstadoPedidoAutorizacionDTO().getValorReferencial() != null
													&& autorizacionDetalle.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() ==
													ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
												
												//se verifica el estado de la autorizacion 
												if(!solicitoAutorizacion && autorizacionCola.getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)
														&& autorizacionCola.getId().getCodigoEstadoPedidoAutorizacion() == null){
													solicitoAutorizacion = Boolean.TRUE;
													session.setAttribute(AutorizacionesUtil.SOLICITO_AUTORIZACION_DESCUENTO_VARIABLE, solicitoAutorizacion);
												}
												
												//se omite el porcentaje porque pudieron aplicar porcentaje diferente
												String valorRefColaAut = autorizacionCola.getValorReferencial();
												valorRefColaAut = valorRefColaAut.replace(SEPARADOR_TOKEN+valorRefColaAut.split(SEPARADOR_TOKEN)[6], "");
												
												String valorRefDetalle = autorizacionDetalle.getEstadoPedidoAutorizacionDTO().getValorReferencial();
												valorRefDetalle = valorRefDetalle.replace(SEPARADOR_TOKEN+valorRefDetalle.split(SEPARADOR_TOKEN)[6], "");
												
												//se setean los nuevos valores
												if(valorRefColaAut.equals(valorRefDetalle)){
													autorizacionDetalle.getEstadoPedidoAutorizacionDTO().setCodigoAutorizacion(autorizacionCola.getCodigoAutorizacion());
													autorizacionDetalle.getEstadoPedidoAutorizacionDTO().setNumeroProceso(autorizacionCola.getNumeroProceso());
													autorizacionDetalle.getEstadoPedidoAutorizacionDTO().setValorReferencial(autorizacionCola.getValorReferencial());
													autorizacionDetalle.getEstadoPedidoAutorizacionDTO().setEstado(autorizacionCola.getEstado());
												}
											}
										}
									}
								}
							}
							CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, colDetallePedidoDTO);
						}
						
						//Se verifica si hay autorizaciones para actualizar
						AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTUALIZAR), pedidoDTO);
						
						//se desactivan las autorizaciones que fueron eliminadas
						Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
						if(CollectionUtils.isNotEmpty(autorizacionesDesactivarCol)){
							pedidoDTO.setNpAutorizacionesDesactivarCol(autorizacionesDesactivarCol);
							//se comenta esta linea para que no se finalicen las autorizaciones omitidas
//							AutorizacionesUtil.crearAutorizacionesFinalizarWorkflow(autorizacionesDesactivarCol, request);
						}
						
						//se setea las autorizaciones a finalizar del workflow
						Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW); 
						if(CollectionUtils.isNotEmpty(autorizacionesFinalizarWorkflow)){
							pedidoDTO.setNpAutorizacionesWorkFlow(autorizacionesFinalizarWorkflow);
						}
						
						colDetallePedidoDTO = ColeccionesUtil.sort(colDetallePedidoDTO, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
						pedidoDTO.setNpCodigosArticulos(ColeccionesUtil.sort(pedidoDTO.getNpCodigosArticulos(), ColeccionesUtil.ORDEN_ASC));
						//se actualizan las variables de sesi\u00F3n
						session.setAttribute(DETALLE_PEDIDO, colDetallePedidoDTO);
						
//						Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
//						Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
//						CotizacionReservacionUtil.aplicarDescuentosConsolidados(request, session,	colDetallePedidoDTO,descuentoVariableCol, llaveDescuentoCol,errors);
						
						//Obtener los pedidos que fueron consolidados y que se eliminaran de la consolidacion
						Collection<PedidoDTO> pedidoConsolidadosEliminarCol = (Collection<PedidoDTO>)session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						Collection <DetallePedidoDTO> detallePedidoConsolidadoEliminarCol = (Collection <DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
						//coleccion de detalles pedidos consolidados
						Collection <DetallePedidoDTO> colDetallePedidoConsolidadoDTO=null;
						//jdannyy
						formulario.setDatosConsolidados((ArrayList<VistaPedidoDTO>)session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX));
						String []validarPedidos=new String[formulario.getDatosConsolidados().size()+1];
				        String []numeroPedidoConsolidado=new String[formulario.getDatosConsolidados().size()+1];
				        int iPedidos=0;
				        VistaPedidoDTO vistaPedidoDTOAux= (VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
				        Boolean pedidoEncontrado=Boolean.FALSE;
				         for(VistaPedidoDTO visPedDTO:(ArrayList<VistaPedidoDTO>)formulario.getDatosConsolidados()){
			            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
			            	//LogSISPE.getLog().info("codigoPed formulario {}, codigoPed seleccionado {} ", visPedDTO.getId().getCodigoPedido(), vistaPedidoDTOAux.getId().getCodigoPedido());
			            	validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
		            		numeroPedidoConsolidado[iPedidos]=null;
			            	if(vistaPedidoDTOAux!=null && !pedidoEncontrado){
			            		if(vistaPedidoDTOAux.getId().getCodigoPedido().equals("-1") && vistaPedidoDTOAux.getId().getCodigoPedido().equals(visPedDTO.getId().getCodigoPedido())){
			            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+vistaPedidoDTOAux.getId().getCodigoPedido();
			            			pedidoEncontrado=Boolean.TRUE;
			            		}else{
					            		Collection<VistaPedidoDTO> vistaPedidoCol=visPedDTO.getVistaPedidoDTOCol();
					            		Collection<VistaPedidoDTO> resVisPedCol=ColeccionesUtil.simpleSearch("id.codigoPedido",vistaPedidoDTOAux.getId().getCodigoPedido() , vistaPedidoCol);
					            		if(!resVisPedCol.isEmpty()){
					            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
					            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+vistaPedidoDTOAux.getId().getCodigoPedido();
					            			pedidoEncontrado=Boolean.TRUE;
					            		}
			            			}
		            			}
//		            			else{
//				            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
//				            		numeroPedidoConsolidado[iPedidos]=null;
//			            		}
		            		
			            	iPedidos++;
			            }				         
				        formulario.setPedidosValidados(validarPedidos);
				        formulario.setNumeroConsolidado(numeroPedidoConsolidado);				        
				        
						Collection<VistaPedidoDTO> visPedCol=formulario.getDatosConsolidados();
						//setear el campo isNpTieneConsolidacion para determinar q el pedido es consolidado
						//Collection<VistaPedidoDTO> visPedCol=formulario.getDatosConsolidados();
						if(visPedCol!=null && visPedCol.size()>0){
							colDetallePedidoConsolidadoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
							LogSISPE.getLog().info("CodigoConsolidado: {}",formulario.getNumeroPedidoConsolidado());
							if(!formulario.getNumeroPedidoConsolidado().equals("CONSOLIDADO")){
								pedidoDTO.setCodigoConsolidado(formulario.getNumeroPedidoConsolidado());
							}
							pedidoDTO.setNpTieneConsolidacion(Boolean.TRUE);
							Collection<VistaPedidoDTO> pedidosConsolidados= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
							if(pedidoColActualizado==null){
								
								Collection<PedidoDTO> pedidoColDTO= new ArrayList<PedidoDTO>();
								if(CollectionUtils.isNotEmpty(pedidosConsolidados)){
									for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
										PedidoDTO pedidoBuscar= new PedidoDTO();
										pedidoBuscar.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
										pedidoBuscar.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
										pedidoBuscar.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
										LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",visPedDTO.getId().getCodigoPedido());
										Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
										LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
										if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
											PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
											if(visPedDTO.getId().getCodigoPedido().equals(pedActDTO.getId().getCodigoPedido())){
												pedActDTO.setNpNumBonNavEmp(visPedDTO.getNumBonNavEmp());
											}
											pedidoColDTO.add(pedActDTO);
										}
										pedidoDTO.setPedidosConsolidar(pedidoColDTO);	
									}
								}
							}
							else{
								if(CollectionUtils.isNotEmpty(pedidosConsolidados)){
									for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
										for (PedidoDTO pedDTO: pedidoColActualizado){
											if(visPedDTO.getId().getCodigoPedido().equals(pedDTO.getId().getCodigoPedido())){
												pedDTO.setNpNumBonNavEmp(visPedDTO.getNumBonNavEmp());
												break;
											}
										}	
									}
								}
								
								pedidoDTO.setPedidosConsolidar(pedidoColActualizado);	
							}
						}else{
							if(pedidoConsolidadosEliminarCol!=null && !pedidoConsolidadosEliminarCol.isEmpty()){
								pedidoDTO.setCodigoConsolidado(null);
								pedidoDTO.setNpTieneConsolidacion(Boolean.TRUE);
							}
						}						
				
						pedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());//campos de auditoria
						SessionManagerSISPE.getServicioClienteServicio().transGuardarConsolidacion(pedidoDTO, pedidoDTO.getPedidosConsolidar(), colDetallePedidoConsolidadoDTO, pedidoConsolidadosEliminarCol, detallePedidoConsolidadoEliminarCol);
						
						Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
						String codigoConsolidado =pedidoActColDTO.iterator().next().getCodigoConsolidado();
						VistaPedidoDTO vistaPedidoDTOaux = new VistaPedidoDTO();
						vistaPedidoDTOaux.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
						vistaPedidoDTOaux.setCodigoConsolidado(codigoConsolidado);
						vistaPedidoDTOaux.setEstadoActual("SI");
						Collection<VistaPedidoDTO> collectionVistaPedidoDTOs = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(vistaPedidoDTOaux);
						VistaPedidoDTO vistaPedidoUpdate = new VistaPedidoDTO();
						//actualizar la coleccion principal despues de guardar
						String []pedidoValido=formulario.getPedidosValidados();
						int contador = 0;
						pedidoEncontrado = Boolean.FALSE;
						//se obtiene el la posici\u00F3n del pedido a consolidar seleccionado
						for(String itVistaPedidoDTO:pedidoValido){
							if(itVistaPedidoDTO!=null){
								if(itVistaPedidoDTO.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
									pedidoEncontrado=Boolean.TRUE;
									break;								
								}	
									contador++;
							}
						}							
						
						if(pedidoEncontrado){						
							vistaPedidoUpdate = (VistaPedidoDTO)CollectionUtils.get((List<VistaPedidoDTO>)formulario.getDatosConsolidados(), contador);
							vistaPedidoUpdate.getId().setCodigoPedido(codigoConsolidado);
							vistaPedidoUpdate.setCodigoConsolidado(codigoConsolidado);
							vistaPedidoUpdate.setVistaPedidoDTOCol(collectionVistaPedidoDTOs);
							session.setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",vistaPedidoUpdate);
						}
						
						removerVariablesSession(session);
						
						//wc variable para popUp autorizacion descuentos variable
						session.removeAttribute(AUTORIZACION_DESCUENTOS);
						session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
						//se elimina las autorizaciones finalizadas
						request.getSession().removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
						
						//se activa el men\u00FA principal
						MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
						
						//se verifica si solicito autorizacion de descuento variable
						if(session.getAttribute(AutorizacionesUtil.SOLICITO_AUTORIZACION_DESCUENTO_VARIABLE) != null 
								&& (Boolean) session.getAttribute(AutorizacionesUtil.SOLICITO_AUTORIZACION_DESCUENTO_VARIABLE)){
							//mensaje de exito con aviso de solicitud de autorizaciones
							exitos.add("exitoConsolidacion",new ActionMessage("message.consolidacion.exito.dsctos.variables"));
						}
						else{
							//mensaje de exito para el registro
							exitos.add("exitoConsolidacion",new ActionMessage("message.consolidacion.exito"));
						}

						salida="desplegarConsolidarPedido";
						
						//se eliminan las variables para que no vuelva a ingresar a guardar la consolidacion
						formulario.setBotonGuardarConsolidarPedidos(null);
						session.removeAttribute(AutorizacionesUtil.ACCION_ANTERIOR_AUTORIZACION);
						session.removeAttribute(AutorizacionesUtil.SOLICITO_AUTORIZACION_DESCUENTO_VARIABLE);
					
						//se limpian las variables para que cargue correctamente cuando ingresa a consolidar otros pedidos 
						session.removeAttribute(CotizarReservarAction.DETALLE_PEDIDO);
						session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS);
						session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
						session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
						session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
						session.removeAttribute(DES_MAX_NAV_EMP);
						session.removeAttribute(CHECK_PAGO_EFECTIVO);
						session.removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
						
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					LogSISPE.getLog().info("CODIGO ERRROR: {}",ex.getCodigoError());
					errors.add("registrarConsolidacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la consolidaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en GUARDAR"));
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					//se retorna el valor del forward
					salida= "desplegar";
				}
				
			}
			else//validar si selecciono un pedido para consolidar	
				if(request.getParameter("validarPedido")!=null ){
					String pedido=(String)request.getParameter("validarPedido");
					LogSISPE.getLog().info("Ingresando a validar Pedido {}",pedido);
					//indentificar si esta seleccionado o no el pedido
//					Collection<VistaPedidoDTO> vistaPedidoDTOCol= (Collection<VistaPedidoDTO>)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol");
					Collection<VistaPedidoDTO> vistaPedidoDTOCol= formulario.getDatosConsolidados();
		            String []validarPedidos=formulario.getPedidosValidados();
		            String []numeroPedidoConsolidado=formulario.getNumeroConsolidado();
		            if(validarPedidos==null || validarPedidos.length==0){
		            	validarPedidos= new String[vistaPedidoDTOCol.size()];
		            }
		            if(numeroPedidoConsolidado==null || numeroPedidoConsolidado.length==0){
		            	numeroPedidoConsolidado= new String[vistaPedidoDTOCol.size()];
		            }
		            int iPedidos=0;
		            for(VistaPedidoDTO visPedDTO:vistaPedidoDTOCol){
		            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
		            	if(visPedDTO.getId().getCodigoPedido().equals(pedido)){
		            		if(validarPedidos[iPedidos]!=null && validarPedidos[iPedidos].equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
		            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
		            			numeroPedidoConsolidado[iPedidos]=null;
		            			request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
		            		}
		            		else{
		            			//valida que no exista pedidos en estado de reserva
		            			boolean permiteSeleccion = true;
		            			if(visPedDTO.getVistaPedidoDTOCol() != null && !visPedDTO.getVistaPedidoDTOCol().isEmpty()){
		            				for(Iterator i = visPedDTO.getVistaPedidoDTOCol().iterator(); i.hasNext();){
		            					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)i.next();
		            					LogSISPE.getLog().info("vistaPedidoDTO.getId().getCodigoEstado(): {}", vistaPedidoDTO.getId().getCodigoEstado());
		            					if(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")) 
		            							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion")) 
		            							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")) 
		            							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado")) 
		            							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))){
		            						permiteSeleccion = false;
		            						break;
		            					}
		            				}
		            			}
		            			if(permiteSeleccion){
			            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+pedido;
			            			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTO);
		            			}else{
		            				infos.add("noSeleccionGrupo", new ActionMessage("message.seleccionar.grupoConsolidacion"));
		            			}
		            		}
		            	}
		            	else{
		            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
		            		numeroPedidoConsolidado[iPedidos]=null;
		            	}
		            	iPedidos++;
		            }
		            
		            if(ConsolidarAction.tieneAutorizacion){
		            	exitos.add("autorizacionConsolidarPedido",new ActionMessage("info.consolidacionPedidos.autorizado"));
		            }
		            
		            formulario.setPedidosValidados(validarPedidos);
		            formulario.setNumeroConsolidado(numeroPedidoConsolidado);
					salida="desplegarConsolidarPedido";
				}
			
			
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				int tabSeleccionado=beanSession.getPaginaTab().getTabSeleccionado();
				cambiarTabsDiferenciasDetallePed(beanSession, tabSeleccionado);
			}
			//--Cambios oscar
			//datos de la localizacion desde el componente de contactos
			else if(request.getParameter("localizacionCorporativo")!=null){
				try{
					LogSISPE.getLog().info("Localizacion desde el corporativo");
					
	//				if(session.getAttribute("ec.com.smx.sic.sispe.contactos.localizacion")!=null){
					if(session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP)!=null){
	//					LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO)session.getAttribute("ec.com.smx.sic.sispe.contactos.localizacion");
						LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO)session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP);
						
						if(localizacionSeleccionada.getEstadoLocalizacion().equals(CorporativoConstantes.ESTADO_ACTIVO)){
						
							LogSISPE.getLog().info(localizacionSeleccionada.toString());
							
							if(ContactoUtil.asignarDatosLocalizacion(localizacionSeleccionada, formulario)){
								infos.add("localizacion",new ActionMessage("info.contactos.localizacion.seleccionada", localizacionSeleccionada.getDescripcionLocalizacion()));
							}else{
								warnings.add("localizacion",new ActionMessage("error.contactos.localizacion.sinContacto", localizacionSeleccionada.getDescripcionLocalizacion()));
							}
						
						}else {
							errors.add("localizacion",new ActionMessage("error.contactos.localizacion.inactivo", localizacionSeleccionada.getDescripcionLocalizacion()));
						}
						
					}else{
						infos.add("localizacion",new ActionMessage("error.contactos.localizacion.noAsignada"));
					}
				} catch (Exception e) {
					//excepcion desconocida
					LogSISPE.getLog().error("Error en localizacion corporativo",e);
					errors.add("",new ActionMessage("errors.gerneral",e.getMessage()));
				}
			}
				
			//cuando se desea ver la localizacion de una empresa del corporativo desde el sispe --cambios oscar
			else if(request.getParameter("visualizarLocalizacion")!=null){
				
				LogSISPE.getLog().info("Ver localizacion");
				
				ContactoUtil.cargarComponenteLocalizaciones(request);
				ContactoUtil.mostrarPopUpCorporativo(request, session, "verLocalizacion", formulario,errors);
				//validar si se selecciona la localizacion desde la pesta\u00F1a de resumen dce la reservacion
//				if (session.getAttribute(ContactoUtil.TAB_RESUMEN_PEDIDO) != null) {
//					PaginaTab tabsResumen = ContactoUtil.construirTabsResumenPedido(request,formulario);
//					beanSession.setPaginaTab(tabsResumen);
//					salida = "registro";
//				}
			}
			
			else if(request.getParameter("pedidosAnteriores")!=null&&request.getParameter("accionesPedAnt")!=null){   
				LogSISPE.getLog().info("Busqueda de Pedidos Anteriores");
				CotizarPedidosAnterioresUtil.accionesPedAnt(request,formulario,errors, exitos,warnings,infos);				
			}else if(request.getParameter("aceptarPopUpDespacho")!=null){			
				session.setAttribute(SessionManagerSISPE.POPUP, null);	
				session.setAttribute(ModificarReservacionAction.MOSTRAR_POPUP_DESPACHO, null);
			}else if(request.getParameter("imprimirConvenios")!=null){
				WebSISPEUtil.instanciarPopUpImpresionConvenios(request,Boolean.FALSE);
			}else if(peticion!=null && peticion.equals("imprimirConvenioL")){
		      	LogSISPE.getLog().info("impresion de convenios Laser");
		      	session.setAttribute("ec.com.smx.sic.sispe.imprimirConvenios","imprimirConvenioL");
		      	request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "okLaser");
		      	session.removeAttribute(SessionManagerSISPE.POPUP);
				LogSISPE.getLog().info("Entra a subirArchivo del Beneficiario");
				//se construye la ventana de pregunta archivo beneficiario
				instanciarPopUpPreguntaBeneficiario(request);
		      	salida="registro";
		      }
			else if(peticion!=null && peticion.equals("imprimirConvenioM")){
				LogSISPE.getLog().info("impresion de convenios Matricial");
				session.setAttribute("ec.com.smx.sic.sispe.imprimirConvenios","imprimirConvenioM");
				request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "okMatriz");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				LogSISPE.getLog().info("Entra a subirArchivo del Beneficiario");
				//se construye la ventana de pregunta archivo beneficiario
				instanciarPopUpPreguntaBeneficiario(request);
				salida="registro";
		      }
			
			//IOnofre. Saca el PopUp para buscar los articulos
			else if(request.getParameter("buscador")!=null){   
				LogSISPE.getLog().info("Busqueda de Articulos");
				BuscarArticuloUtil.accionesBuscarArticulos(request, formulario, errors, exitos, warnings, infos);				
			}
			
			//IOnofre. Saca el PopUp de buscar productos por Estructura Comercial
			else if(request.getParameter("buscarPorEstructura")!=null){   
				LogSISPE.getLog().info("Busqueda de Articulos por Estructura Comercial");
				BuscarArticuloUtil.accionesBuscarArticulos(request, formulario, errors, exitos, warnings, infos);
			}
			
			else if(request.getParameter("abonoCero") != null){
				
				if(request.getParameter("abonoCero").equals("ok")){
					LogSISPE.getLog().info("Activa la opcion de abono cero");
					formulario.setCheckAbonoCero(request.getParameter("abonoCero"));
					formulario.setValorAbono("0");
				}else{
					LogSISPE.getLog().info("Desactiva la opcion de abono cero");
					formulario.setCheckAbonoCero(null);
					formulario.setValorAbono(null);
				}
			}
			
			//------- CASO POR OMISION --------
			else{
				try{
					LogSISPE.getLog().info("Accediendo por primera vez a CotizarReservarAction...");
					//se inicializan los valores de la cotizaci\u00F3n que no vienen de la aplicaci\u00F3n
					inicializarCotizacionSinLlamarAlServicio(formulario, request);
					//se inicialzan los valores de la cotizaci\u00F3n que vienen de la aplicaci\u00F3n
					inicializarCotizacionLlamandoAlServicio(request, formulario);
					request.getSession().removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);//cambios oscar
					request.getSession().removeAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO);
	//				ContactoUtil.getTokenCorp(request);//cambios oscar
					//Se contruye los tabs de Contacto y Pedidos
					PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request,formulario);
					beanSession.setPaginaTab(tabsCotizaciones);
					salida = "desplegar";
				}catch(Exception ex){
					//excepcion desconocida
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		// finaliza con		
		return forward(salida, mapping, request, errors, exitos, warnings, infos, beanSession);
	}

	public Boolean validarFechaDespachoEnDetallePedido(HttpSession session,
			List<DetallePedidoDTO> colDetalle) {
		Boolean despacho = false;
		if(session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){	
			//verifico si ya existen despachos
			for(DetallePedidoDTO detallePedido :  colDetalle){
				if(CollectionUtils.isNotEmpty(detallePedido.getEntregaDetallePedidoCol())){
					for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){					
						if(entregaDetallePedido.getFechaRegistroDespacho()!=null && entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
							despacho = true;
							break;
						}
						if((entregaDetallePedido.getFechaRegistroDespacho()==null && 
								entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega()!=null &&
								entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) &&
								entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().getTime() <= UtilesSISPE.obtenerFechaActual().getTime())){
							despacho = true;
							break;
						}
					}
				}
			}
		}
		return despacho;
	}

	/**
	 * Validar las entregas de los pedidos que tienen estado LQD
	 * @param request
	 * @param errors
	 * @param session
	 * @param colDetallePedido
	 */
	private void validarEntregas(HttpServletRequest request, ActionMessages errors, HttpSession session,
			Collection<DetallePedidoDTO> colDetallePedido, ActionMessages infos) {
		
		if(errors.isEmpty() && colDetallePedido!=null){
			LogSISPE.getLog().info("por validar las entregas");
			
			session.removeAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION);
			
			for(DetallePedidoDTO detallePedidoDTO : colDetallePedido){
				
				//---------------------------------------------------------------------
				//se valida que todos los art\u00EDculos esten entregados en su totalidad
				//---------------------------------------------------------------------
				if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
					long cantidadEstado = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					long cantidadEntregada = detallePedidoDTO.getNpContadorEntrega().longValue();
					LogSISPE.getLog().info("CANTIDAD ENTREGADA: {}",cantidadEntregada);
					if(cantidadEstado > cantidadEntregada){
						infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
						//se realizan las asignaciones correspondientes para observar la secci\u00F3n de entregas
						session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/entregaArticulos.jsp");
						//se crea una variable para indicar que se produjo un error por entregas incompletas
						request.setAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION, "ok");
						//se blanquean los errores para que vaya a la acci\u00F3n y haga el forward a la 
						//p\u00E1gina de entregas
						errors.clear();
						break; //se termina el m\u00E9todo
					}
				}else{
					infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
					//se realizan las asignaciones correspondientes para observar la secci\u00F3n de entregas
					session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/entregaArticulos.jsp");
					//se crea una variable para indicar que se produjo un error por entregas incompletas
					request.setAttribute(CotizarReservarAction.ERROR_ENTREGAS_ACCTION, "ok");
					//se blanquean los errores para que vaya a la acci\u00F3n y haga el forward a la 
					//p\u00E1gina de entregas
					errors.clear();
					break; //se termina el m\u00E9todo
				}
			}
		}
	}
	
	/**
	 * Action Forward
	 * 
	 * @param forward
	 * @param mapping
	 * @param request
	 * @param errors
	 * @param messages
	 * @param infos
	 * @return
	 */
	private final ActionForward forward(String forward, ActionMapping mapping, HttpServletRequest request, ActionMessages errors, 
			ActionMessages messages, ActionMessages warnings, ActionMessages infos, BeanSession beanSession) {
		LogSISPE.getLog().info("saliendo por:" + forward);
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);
				
		saveErrors(request, errors);
		saveMessages(request, messages);
		saveWarnings(request, warnings);
		saveInfos(request, infos);
		return mapping.findForward(forward);
	}
	/**
	 * @param request
	 * @param infos
	 * @param errors
	 * @param formulario
	 * @param session
	 * @throws Exception
	 */
	public void buscarPedidosConsolidados(HttpServletRequest request,
			ActionMessages infos, ActionMessages errors,
			CotizarReservarForm formulario, HttpSession session)
			throws Exception {
		String salida;
		LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		String buscarConsolidados=formulario.getOpBuscarConsolidados();
		  LogSISPE.getLog().info("Buscando pedidos CotizarRecotizar desde CONSOLIDACION");
			//obtener de sesion en el caso de que ya se haya seleccionado pedidos a cotizar
		  	//Collection<VistaPedidoDTO> visPedCol=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol");
			Collection<VistaPedidoDTO> visPedCol=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados()==null?0:formulario.getDatosConsolidados().size());
			Collection<VistaPedidoDTO> visPedColeCol=formulario.getDatosConsolidados();
			//clonar cada registro
			if(visPedColeCol!=null){
		    	for(VistaPedidoDTO visPedClone:visPedColeCol){
		    		visPedCol.add((VistaPedidoDTO)SerializationUtils.clone(visPedClone));
		    	}
			}
		  	String excluirPedidos="";
		  	if(visPedCol!=null){
		  		LogSISPE.getLog().info("Pedidos consolidados {}",visPedCol.size());
		  		String []pedidosConsolidados=new String[visPedCol.size()];
		  		int index=0;
		  		String separador="-";
		  		for(VistaPedidoDTO visPedDTO:visPedCol){
		  			String value=String.valueOf(index)+separador+visPedDTO.getId().getCodigoPedido();
		  			excluirPedidos=excluirPedidos+visPedDTO.getId().getCodigoPedido()+",";
		  			LogSISPE.getLog().info("value {}",value);
		  			pedidosConsolidados[index]=value;
		  				index++;
		  		}
		  		//formulario.setOpSeleccionPedidosConsolidados(pedidosConsolidados);
		  	}
		  	
		  	//colecci\u00F3n que almacena los pedidos buscados
		    Collection <VistaPedidoDTO> colVistaPedidoDTO = new ArrayList <VistaPedidoDTO>();
		    Collection <VistaPedidoDTO> colVistaPedidoConDTO = new ArrayList <VistaPedidoDTO>();

			 VistaPedidoDTO consultaPedidoDTO =null;
		        //DTO que contiene los pedidos a buscar
		        consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
		        consultaPedidoDTO.setNpConsultarTodosConsolidadosSinAnulados("SI");
		        consultaPedidoDTO.setNpConsultarSinConsolidados("SI");
		        consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		        //se asignan los campos de ordenamiento
		  			String [][] camposOrden = new String [][]{{"codigoAreaTrabajo",ORDEN_ASC},{"codigoPedido",ORDEN_ASC},{"fechaInicialEstado", ORDEN_ASC}};
		  			consultaPedidoDTO.setNpCamposOrden(camposOrden);
		  			
		        try
		        {
		        	//metodo que trae el total de registros
		        	Integer total = SessionManagerSISPE.getServicioClienteServicio().transObtenerTotalVistaPedido(consultaPedidoDTO);
		        	session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, total);
		        	LogSISPE.getLog().info("total de registros: {}",total);
		        	//se asignan los par\u00E1metros para la paginaci\u00F3n en la base de datos
		        	int start = 0;
		        	int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
		        	consultaPedidoDTO.setNpFirstResult(start);
		        	consultaPedidoDTO.setNpMaxResult(range);
		        	LogSISPE.getLog().info("excluirPedidos: {}",excluirPedidos);
		        	
		        	
//		        	DTO que contiene los pedidos consolidados
		        	if(buscarConsolidados!=null){
		        		VistaPedidoDTO consultaPedidoConsolidadosDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
			        	consultaPedidoConsolidadosDTO.setNpConsultarTodosConsolidados(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			        	consultaPedidoConsolidadosDTO.setNpConsultarTodosConsolidadosSinAnulados("SI");
			        	colVistaPedidoConDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoConsolidadosDTO);
			        	colVistaPedidoDTO= formulario.getDatos();
		        	}
		        	else{
		        		//obtiene solamente los registros en el rango establecido
		        		//se incializa la colecci\u00F3n
			  			formulario.setDatos(null);
			  			consultaPedidoDTO.setNpConsultarSinConsolidados("SI");
			        	colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
		        	}
		        	//Limpia la variable de session
		        	session.removeAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO);
		        	//se guarda los parametros de la busqueda para la vistaPedido
		            session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
		          if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
		            infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos"));
		          }else{
		            LogSISPE.getLog().info("ENTRO A LA PAGINACION");
		            //crear coleccion de todos los pedidos consolidados
		            Collection<VistaPedidoDTO> visPedColConsolidados=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTO.size());
		            for(VistaPedidoDTO visPedDTO:colVistaPedidoConDTO){
		        		if(visPedDTO.getCodigoConsolidado()!=null && !visPedDTO.getCodigoConsolidado().equals("") ){
		        			LogSISPE.getLog().info("Pedido consolidado: {}",visPedDTO.getId().getCodigoPedido().trim());
		        			Boolean agregarConsolidado=Boolean.TRUE;
		        			if(visPedColConsolidados.size()>0){
		        				//buscar si ya esta agregado el pedido consolidado
		        				for(VistaPedidoDTO visPedConDTO:visPedColConsolidados){
		        					if(visPedConDTO.getCodigoConsolidado().equals(visPedDTO.getCodigoConsolidado())){
		        						agregarConsolidado=Boolean.FALSE;
		        						break;
		        					}
		        				}
		        			}
		        			if(agregarConsolidado){
		    					visPedColConsolidados.add(visPedDTO);
		    				}
		        		}
		        	}
		            
		            //agrupar los pedidos consolidados para mostrar en forma de arbol
		            Collection<VistaPedidoDTO> visPedColConsAux;
		            Collection<VistaPedidoDTO> vistaPedidoDTOCol=new ArrayList<VistaPedidoDTO>();
		            LogSISPE.getLog().info("Total pedidos consolidados: {}",colVistaPedidoConDTO.size());
		            LogSISPE.getLog().info("Total pedidos visPedColConsolidados: {}",visPedColConsolidados.size());
		            
		            for(VistaPedidoDTO visPedDTOCon:visPedColConsolidados){
		            	String codigoConsolidado=visPedDTOCon.getCodigoConsolidado();
		            	VistaPedidoDTO vistaPedidoDTOConsolidado= new VistaPedidoDTO();
						vistaPedidoDTOConsolidado.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
						vistaPedidoDTOConsolidado.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
						vistaPedidoDTOConsolidado.setCodigoConsolidado(codigoConsolidado);
						vistaPedidoDTOConsolidado.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
						//obtener todos los pedidos consolidados
						visPedColConsAux = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosConsolidados(vistaPedidoDTOConsolidado);
						vistaPedidoDTOConsolidado.getId().setCodigoEstado("CSD");
			        	vistaPedidoDTOConsolidado.getId().setCodigoPedido(codigoConsolidado);
			        	LogSISPE.getLog().info("Total pedidos visPedColConsAux: {}",visPedColConsAux.size());
						if(visPedColConsAux.size()>0){
		            		vistaPedidoDTOConsolidado.setVistaPedidoDTOCol(visPedColConsAux);
		            		vistaPedidoDTOCol.add(vistaPedidoDTOConsolidado);
		            		//Eliminar los pedidos que ya estan en la seccion de consolidado
		            		for(VistaPedidoDTO visPedDTOEliminar:visPedColConsAux){
		            			for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
		            				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
		            					LogSISPE.getLog().info("remover de coleccion colVistaPedidoDTO");
		            					colVistaPedidoDTO.remove(visPedDTOEliCon);
		            					break;
		            				}
		            			}	
		            		}
		            	}
		            }
		          //Eliminar los pedidos que ya estan en la seccion de consolidado y vienen de sesion
		    		LogSISPE.getLog().info("Total pedidos vistaPedidoDTOCol: {}",vistaPedidoDTOCol.size());
		    		for(VistaPedidoDTO visPedDTOEliminar:visPedCol){
		    			for(VistaPedidoDTO visPedDTOEliCon:vistaPedidoDTOCol){
		    				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
		    					LogSISPE.getLog().info("remover de coleccion vistaPedidoDTOCol");
		    					vistaPedidoDTOCol.remove(visPedDTOEliCon);
		    					break;
		    				}
		    			}	
		    		}
		    		//agregar  a la coleccion seleccionada como consolidada los nuevos pedidos consolidados consultados
					visPedCol.addAll(vistaPedidoDTOCol);
					//activar el check inicial para indicar q el existe un pedido consolidado
					String []validarPedidos=new String[visPedCol.size()];
			        String []numeroPedidoConsolidado=new String[visPedCol.size()];
			        int iPedidos=0;
			        VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
		            for(VistaPedidoDTO visPedDTO:visPedCol){
		            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
		            	if(visPedDTO.getId().getCodigoPedido().equals("CONSOLIDADO")){
		            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
		            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+formulario.getNumeroAConsolidar();
		            			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTO);
		            			//Eliminar los pedidos que ya estan en la seccion de consolidado
				            		for(VistaPedidoDTO visPedDTOEliminar:visPedDTO.getVistaPedidoDTOCol()){
				            			for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
				            				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
				            					colVistaPedidoDTO.remove(visPedDTOEliCon);
				            					break;
				            				}
				            			}	
				            		}
		            	}
		            	else{
		            		if(vistaPedidoDTO!=null){
		            			if(visPedDTO.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
		            				validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+vistaPedidoDTO.getId().getCodigoPedido();
		            			}
		            			else{
				            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
				            		numeroPedidoConsolidado[iPedidos]=null;
			            		}
		            		}else{
			            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
			            		numeroPedidoConsolidado[iPedidos]=null;
		            		}
		            	}
		            	iPedidos++;
		            }
		            
		            for(VistaPedidoDTO visEliCol:visPedCol){
		            	for(VistaPedidoDTO vistaConsolidadosCol:visEliCol.getVistaPedidoDTOCol()){
		            		for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
				            	if(visPedDTOEliCon.getId().getCodigoPedido().equals(vistaConsolidadosCol.getId().getCodigoPedido())){
				            		colVistaPedidoDTO.remove(visPedDTOEliCon);
		        					break;
		        				}
		            		}
		            	}
		            }
		            
		            formulario.setPedidosValidados(validarPedidos);
		            formulario.setNumeroConsolidado(numeroPedidoConsolidado);
					//agregar a la coleccion para mostrar en la seccion de pedidos consolidados
					formulario.setDatosConsolidados(visPedCol);
					
		            formulario.setDatos(colVistaPedidoDTO);
		            formulario.setStart(String.valueOf(start));
		            formulario.setRange(String.valueOf(range));
		            formulario.setSize(String.valueOf(total));
		            session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
		            
//				            //se guarda la colecci\u00F3n general
//				            session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
//				            //se guarda los parametros de la busqueda para la vistaPedido
//				            session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
		            //se elimina la variable del indice de paginaci\u00F3n
		            session.removeAttribute(ListadoPedidosForm.INDICE_PAGINACION);
		            
		            //llamada a la funci\u00F3n que inicializa los datos de ordenamiento
		            this.inicializarDatosOrdenamiento(request);

		          }
		        }catch(SISPEException ex){
		          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		          errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos"));
		        }
//     }
		    salida = "desplegarConsolidarPedido";
	}

	/**
	 * @param request
	 * @param formulario
	 * @param session
	 * @param estadoActivo
	 * @throws Exception
	 * @throws SISPEException
	 */
	public void cargarPedidosConsolidados(HttpServletRequest request,
			CotizarReservarForm formulario, HttpSession session,
			String estadoActivo) throws Exception, SISPEException {
		LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		LogSISPE.getLog().info("---Entro en el if de consolidar Pedidos--- {}",formulario.getBotonConsolidarPedidos());
		//session.setAttribute("ec.com.smx.sic.sispe.activar.atras","ATRAS");
		//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
		session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
		
		session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
		
		session.setAttribute(ACCION_ANTERIOR, session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		session.setAttribute("ec.com.smx.sic.sispe.accion",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"));
		
     //se consultan los tipos de pedido
		TipoPedidoDTO tipoPedidoFiltro = new TipoPedidoDTO(Boolean.TRUE);
		tipoPedidoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		session.setAttribute(COL_TIPOS_PEDIDO, SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoPedido(tipoPedidoFiltro));
		
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
		
     //se verifica si la entidad responsable es un local
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			//se obtienen los locales por ciudad
			SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			
				//primero se verifica si el rol del usuario logeado est\u00E1 configurado en los par\u00E1metros
				//WebSISPEUtil.verificarUsuarioPedidoEspecial(SessionManagerSISPE.getCurrentEntidadResponsable(request), request);
		}
		 
		String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
		  //se realiza la consulta de estados
		  EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
		  consultaEstadoDTO = new EstadoSICDTO();
	        consultaEstadoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
	        
		  //consultaEstadoDTO.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.estadoNormal"));
		  //Obtener datos de la colecci\u00F3n de estados, en la base de datos
		  Collection estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
		  //guardar en sesi\u00F3n esta colecci\u00F3n
		  session.setAttribute(SessionManagerSISPE.COL_ESTADOS,estados);
		  
		//se consultan los estados de pago
		  	EstadoSICDTO estadoSICDTO = new EstadoSICDTO();
		  	estadoSICDTO.getId().setCodigoEstado(new StringBuilder()
		  		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))
		  		.append(",")
		  		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))
		  		.append(",")
		  		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente"))
		  		.append(",")
		  		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago")).toString());
		  	session.setAttribute("ec.com.smx.sic.sispe.estadosPago", SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(estadoSICDTO));
		  	
		  //se inicializan los atributos del formulario
		  	//formulario.setDatos(null);
		  	formulario.setEtiquetaFechas("Fecha de Estado");
		  	
		  	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
		  	if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadBodega)){
		  		formulario.setOpEntidadResponsable(entidadBodega);
		  	}
		  	
		  	formulario.setOpEstadoActivo(estadoActivo);
		  	
		  	//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
		  	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
		  			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
		  	
		  	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
		  			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
		  	
		  	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");
		  	//Variable de sesion para que no presente el combo de estados
		  	session.setAttribute(SessionManagerSISPE.ESTADO_CHECK,"ok");
		  	PedidoDTO pedidoDTO= (PedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.clienteOriginal");
		  	if(pedidoDTO!=null){
				//se llenan los campos restantes del cliente
		  		if(pedidoDTO.getNpNombreContacto()==null || pedidoDTO.getNpNombreContacto().equals("")){
		  			pedidoDTO.setNpNombreContacto(formulario.getNombrePersona());
		  		}
		  		if(pedidoDTO.getNpTelefonoContacto()==null || pedidoDTO.getNpTelefonoContacto().equals("")){
		  			pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoPersona());
		  		}
			}
		  	
		    //Seleccionar los pedidos ya consolidados
		  	//Collection<VistaPedidoDTO> visPedCol=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol");
		  	//jdannyy
			formulario.setDatosConsolidados((ArrayList<VistaPedidoDTO>)session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX));
			String []validarPedidos=new String[formulario.getDatosConsolidados().size()+1];
	        String []numeroPedidoConsolidado=new String[formulario.getDatosConsolidados().size()+1];
	        int iPedidos=0;
	        VistaPedidoDTO vistaPedidoDTOAux= (VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
	         for(VistaPedidoDTO visPedDTO:(ArrayList<VistaPedidoDTO>)formulario.getDatosConsolidados()){
            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
            	if(vistaPedidoDTOAux!=null){
        			if(visPedDTO.getId().getCodigoPedido().equals(vistaPedidoDTOAux.getId().getCodigoPedido())){
        				validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+vistaPedidoDTOAux.getId().getCodigoPedido();
        			}
        			else{
	            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
	            		numeroPedidoConsolidado[iPedidos]=null;
            		}
        		}else{
            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
            		numeroPedidoConsolidado[iPedidos]=null;
        		}
            	iPedidos++;
            }
	        formulario.setPedidosValidados(validarPedidos);
	        formulario.setNumeroConsolidado(numeroPedidoConsolidado);
	}




	/**
	 * Inicializaci\u00F3n de los valores de la cotizaci\u00F3n que no vienen de la aplicaci\u00F3n
	 * 
	 * @param formulario		Formulario manejado actualmente
	 * @param request			Petici\u00F3n que se est\u00E1 procesando
	 * @param accion			Acci\u00F3n que se est\u00E1 realizando
	 */
	private void inicializarCotizacionSinLlamarAlServicio(CotizarReservarForm formulario, HttpServletRequest request)throws Exception
	{
		//se obtiene la sesi\u00F3n activa
		HttpSession session = request.getSession();

		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com.smx"
		SessionManagerSISPE.removeVarSession(request);

		//se inicializan las variables correspondientes
		session.setAttribute(ACCION_ANTERIOR, "");
		session.setAttribute(LOCAL_ANTERIOR, "");

		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
				MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
		session.setAttribute(DETALLE_PEDIDO, new ArrayList());
		session.setAttribute(COL_CODIGOS_ARTICULOS, new ArrayList());
		session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de cotizaci\u00f3n");
		session.setAttribute(CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
		session.removeAttribute(CotizacionReservacionUtil.DESC_NAVEMP_CREDITO_SELECCIONADO);
		//se inicializan las variables de sesi\u00F3n para los subtotales
		//session.setAttribute("ec.com.smx.sic.sispe.pedido.subTotal",Double.valueOf(0));
		session.setAttribute(SUB_TOTAL_NOAPLICA_IVA, 0D);
		session.setAttribute(SUB_TOTAL_APLICA_IVA, 0D);
		session.setAttribute(DESCUENTO_TOTAL, 0D);
		session.setAttribute(PORCENTAJE_TOT_DESCUENTO, 0D);
		//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la reservacion
		//session.setAttribute(MOSTRAR_AUTORIZACION,
			//	MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearCotizacion.estado"));
		
		//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la recotizacion
		session.setAttribute(MOSTRAR_AUTORIZACION,
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
		
		//se obtiene la descripci\u00F3n del tipo de autorizaci\u00F3n gen\u00E9rico que permite crear una reservaci\u00F3n
		
//		TipoAutorizacionDTO tipoAutorizacionFiltro = new TipoAutorizacionDTO();
//		tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		tipoAutorizacionFiltro.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"));
		
//		Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCol = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionFiltro);
//		tipoAutorizacionFiltro = null;
//		//se obtiene el \u00FAnico registro
//		if(tipoAutorizacionDTOCol != null && !tipoAutorizacionDTOCol.isEmpty()){
//			//se guarda la descripci\u00F3n en sesi\u00F3n
//			session.setAttribute(DESCRICPION_USO_AUTORIZACION, tipoAutorizacionDTOCol.iterator().next().getDescripcion());
//		}
		//Se inicializa el tipo de autorizacion generico
		AutorizacionesUtil.iniciarTipoAutorizacionGenerico(request);


		//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos
//		session.setAttribute(TIPO_ART_RECETA_CANASTA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
//		session.setAttribute(TIPO_ART_RECETA_DESPENSA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		//subClasificacion para articulos tipo canastos 
  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
		String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
		session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA, valorTipoArticuloCanastas);
		
		//subClasificacion para articulos tipo despensas 
  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA, valorTipoArticuloDespensas);
		//indica que se puede pasar directamente a una reservaci\u00F3n
		session.setAttribute(SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");
		//indica que la b\u00FAsqueda es en la pantalla principal
		session.setAttribute(BUSQUEDA_PRINCIPAL,"ok");

		/*------ se incializan los campos del formulario ---------*/
		formulario.setLocalResponsable("");
		formulario.setIndiceLocalResponsable("");
//		formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
		formulario.setTipoDocumento("");

		//se inicializan los totales
		formulario.setSubTotalBrutoNoAplicaIva(0D);
		formulario.setTotalDescuentoIva(0D);
		formulario.setSubTotalNetoBruto(0D);
		formulario.setSubTotal(0D);
		formulario.setSubTotalNoAplicaIVA(0D);
		formulario.setSubTotalAplicaIVA(0D);
		formulario.setDescuentoTotal(0D);
		formulario.setIvaTotal(0D);
		formulario.setTotal(0D);
		//estos campos se utilizan en la reservaci\u00F3n
		formulario.setCostoFlete(0D);
		formulario.setCostoTotalPedido(0D);

		formulario.setOpAutorizacion(estadoInactivo);
		//se inicializa la variable de control de validaci\u00F3n
		formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
		formulario.setOpTipoEspeciales("0"); //primer indice
		formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));

		//se incializan los datos del cliente 
		formulario.setNumeroDocumento(null);
		formulario.setNombreContacto(null);
		formulario.setTelefonoContacto(null);
		formulario.setRucEmpresa(null);
		formulario.setNombreEmpresa(null);
		formulario.setNombrePersona(null);
		formulario.setTelefonoPersona(null);
		formulario.setDatosConsolidados(null);
		formulario.setNumeroPedidoConsolidado(null);
		
		formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
		session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
		
		session.setAttribute(CALCULO_PRECIOS_CON_IVA, estadoActivo);
		
		//se asigna el estado activo a esta opci\u00F3n para que no valide el stock cuando se guarde una cotizaci\u00F3n
		formulario.setOpValidarPedido(estadoActivo);
		
		//se desactiva el men\u00FA de opciones
		MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
	}

	/**
	 * Inicializaci\u00F3n de los datos de la cotizaci\u00F3n que vienen desde la aplicaci\u00F3n
	 * 
	 * @param request			Petici\u00F3n que se est\u00E1 procesando
	 * @param accion			Acci\u00F3n que se est\u00E1 realizando
	 */
	private void inicializarCotizacionLlamandoAlServicio(HttpServletRequest request, CotizarReservarForm formulario)throws Exception{
		//se obtiene la sesi\u00F3n activa
		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		//se obtienen los d\u00EDas de validez de una cotizaci\u00F3n
		ParametroDTO parametroDiasValidez = new ParametroDTO(Boolean.TRUE);
		parametroDiasValidez.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		SISPEFactory.obtenerServicioSispe().transObtenerDiasValidezCotizacion(parametroDiasValidez);
		String diasValidez = SessionManagerSISPE.getServicioClienteServicio().transObtenerDiasValidezCotizacion(parametroDiasValidez);
		session.setAttribute(DIAS_VALIDEZ,diasValidez);
		
		//obtengo el codigoTipDesMax-navidad desde un parametro
		CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);

		//se verifica la entidad responsable
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			//se realiza la consulta de los locales
			SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			session.setAttribute(ES_ENTIDAD_BODEGA,"ok");
			session.setAttribute(INGRESO_PRIMERA_VEZ_BODEGA,"ok");
		}else{
			String token = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken");
			formulario.setLocalResponsable(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal()
					+" "+token+" "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());

			//creaci\u00F3n del DTO para el art\u00EDculo
			EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
			especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			especialDTO.setNpCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			especialDTO.setEstadoEspecial(estadoActivo);
			especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			especialDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
			
			//especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			ArrayList<EspecialDTO> especiales = (ArrayList<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloEspecial(especialDTO);
			eliminarDetallesConProblemas(especiales);
			session.setAttribute(COL_TIPO_ESPECIALES,especiales);
			ArrayList<EspecialDTO> especial = (ArrayList<EspecialDTO>)especiales;
			//realizamos el get para cargar la informacion que mostraremos en la pantalla 
			for(EspecialDTO dto : especial){
				for(ArticuloDTO articuloDTO : dto.getArticulos()){
					LogSISPE.getLog().info("articuloDTO.getDescripcionArticulo() {}, articuloDTO.getPrecioBase() {} ", articuloDTO.getDescripcionArticulo(), articuloDTO.getPrecioBase());
					LogSISPE.getLog().info("articuloDTO.getDescripcionArticulo() {}, articuloDTO.getPrecioBaseImp() {} ", articuloDTO.getDescripcionArticulo(),articuloDTO.getPrecioBaseImp());
					LogSISPE.getLog().info("articuloDTO.getDescripcionArticulo() {}, articuloDTO.getPrecioBaseNoAfi() {} ", articuloDTO.getDescripcionArticulo(),articuloDTO.getPrecioBaseNoAfi());
					LogSISPE.getLog().info("articuloDTO.getDescripcionArticulo() {}, articuloDTO.getPVP() {} ", articuloDTO.getDescripcionArticulo(),articuloDTO.getPVP());
					LogSISPE.getLog().info("articuloDTO.getDescripcionArticulo() {}, articuloDTO.getPVPImp() {} ", articuloDTO.getDescripcionArticulo(),articuloDTO.getPVPImp());					
				}
			}
			
			//se almacena el c\u00F3digo del local para utilizarlo en futuras consultas
			session.setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento());

			//se llama al m\u00E9todo que verifica la posibilidad que tiene el establecimiento
			//para cambiar el precio
			CotizacionReservacionUtil.verificarFormatoNegocioParaCambioPrecios(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento(), request);
			
//			//verficamos si es local de tipo aki para habilitar o desabilitar el check de precios de afiliado
//			if(CotizacionReservacionUtil.verificarFormatoNegocioPreciosAfiliado(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento(), request)){
//				session.setAttribute(HABILITADO_PRECIOS_AFILIADO, "OK");
//			}else {
//				session.removeAttribute(HABILITADO_PRECIOS_AFILIADO);
//			}
		}

		//se carga la configuraci\u00F3n de los descuentos
		CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
		
		//Obtener el tipo de descuento por cajas y mayorista
	    CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 

	}

	/**
	 * Realiza el registro de la cotizaci\u00F3n
	 * 
	 * @param accion
	 * @param request
	 * @param formulario
	 * @param errors
	 * @param exitos
	 * @param esRegistroFinal
	 */
	private String guardarCotizacion(String accion, HttpServletRequest request, CotizarReservarForm formulario, 
			ActionMessages errors, ActionMessages exitos, boolean esRegistroFinal)throws Exception {
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		boolean cotizacion = true;
		if(!accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"))){
			cotizacion = false;
		}
		//coleccion de detalles pedidos consolidados
		Collection <DetallePedidoDTO> colDetallePedidoConsolidadoDTO=null;
		//coleccion de detalles pedidos consolidados
		Collection <DetallePedidoDTO> colDetallePedidoConsolidadoProcesar=null;
		
		//se obtiene el detalle del Pedido
		Collection <DetallePedidoDTO> colDetallePedidoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
		
		//se llama al m\u00E9todo que construye el PedidoDTO
		PedidoDTO pedidoDTO = crearPedidoDTO(accion, formulario, request);
		LogSISPE.getLog().info("CODIGO COMPANIA: {}",pedidoDTO.getId().getCodigoCompania());

		//Se verifica si tiene autorizaciones solicitadas
		AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES), pedidoDTO);
		
		//Se verifica si hay autorizaciones para actualizar
		AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTUALIZAR), pedidoDTO);
				
		//primero se realiza la validacion de las entregas
		AutorizacionesUtil.verificarResponsableEntrega(colDetallePedidoDTO, request);
		
		//se validan los otros tipos de autorizaciones
		AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK), pedidoDTO);
		
		//se elimina de los detalles las autorizaciones de stock ke fueron eliminados
		AutorizacionesUtil.eliminarAutorizacionesHijasRechazadas(colDetallePedidoDTO, request);
		
		//se desactivan las autorizaciones que fueron eliminadas
		Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
		if(CollectionUtils.isNotEmpty(autorizacionesDesactivarCol)){
			pedidoDTO.setNpAutorizacionesDesactivarCol(autorizacionesDesactivarCol);
			AutorizacionesUtil.crearAutorizacionesFinalizarWorkflow(autorizacionesDesactivarCol, request);
			session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);	
		}
		
		//se setea las autorizaciones a finalizar del workflow
		Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW); 
		if(CollectionUtils.isNotEmpty(autorizacionesFinalizarWorkflow)){
			pedidoDTO.setNpAutorizacionesWorkFlow(autorizacionesFinalizarWorkflow);
		}
		
//		//se obtiene de sesion la coleccion de autorizaciones genericas
//		Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesPedidoCol = 
//				(Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
//		if(CollectionUtils.isNotEmpty(autorizacionesPedidoCol)){
//			pedidoDTO.setNpAutorizacionesPedidoCol(autorizacionesPedidoCol);
//		}
		
		//se ordena la colecci\u00F3n del detalle del pedido antes de enviarla a guardar al igual que los c\u00F3digos
		//de art\u00EDculos
		
		colDetallePedidoDTO = ColeccionesUtil.sort(colDetallePedidoDTO, ColeccionesUtil.ORDEN_ASC, "articuloDTO.codigoBarrasActivo.id.codigoBarras");
		pedidoDTO.setNpCodigosArticulos(ColeccionesUtil.sort(pedidoDTO.getNpCodigosArticulos(), ColeccionesUtil.ORDEN_ASC));
		//se actualizan las variables de sesi\u00F3n
		session.setAttribute(DETALLE_PEDIDO, colDetallePedidoDTO);//colDetallePedidoDTO.iterator().next().getArticuloDTO().getPrecioBaseImp()
		session.setAttribute(COL_CODIGOS_ARTICULOS, pedidoDTO.getNpCodigosArticulos());
		
		//setear el campo isNpTieneConsolidacion para determinar q el pedido es consolidado
		Collection<VistaPedidoDTO> visPedCol = formulario.getDatosConsolidados();
		if(visPedCol!=null && visPedCol.size()>0){
			Collection<PedidoDTO> pedidoColActualizado = (Collection<PedidoDTO>)session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
			colDetallePedidoConsolidadoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			//coleccion a procesar cuando guarda los pedidos consolidados
			colDetallePedidoConsolidadoProcesar= new ArrayList<DetallePedidoDTO>(colDetallePedidoConsolidadoDTO.size());
			for(DetallePedidoDTO detPedCon:colDetallePedidoConsolidadoDTO){
				colDetallePedidoConsolidadoProcesar.add((DetallePedidoDTO)SerializationUtils.clone(detPedCon));
			}
			LogSISPE.getLog().info("CodigoConsolidado: {}",formulario.getNumeroPedidoConsolidado());
			if(!formulario.getNumeroPedidoConsolidado().equals("CONSOLIDADO")){
				pedidoDTO.setCodigoConsolidado(formulario.getNumeroPedidoConsolidado());
			}
			pedidoDTO.setNpTieneConsolidacion(Boolean.TRUE);
			if(pedidoColActualizado==null){
				Collection<VistaPedidoDTO> pedidosConsolidados= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				Collection<PedidoDTO> pedidoColDTO= new ArrayList<PedidoDTO>();
				for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
					PedidoDTO pedidoBuscar= new PedidoDTO();
					pedidoBuscar.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
					pedidoBuscar.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
					pedidoBuscar.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
					LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",visPedDTO.getId().getCodigoPedido());
					Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
					LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
					if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
						PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
						pedidoColDTO.add(pedActDTO);
					}
					pedidoDTO.setPedidosConsolidar(pedidoColDTO);	
				}
			}
			else{
				//eliminar pedido actual como consolidado, para que se registre el nuevo estado
				for(PedidoDTO pedidoConsolidado:pedidoColActualizado){
					if(pedidoConsolidado.getId().getCodigoPedido().equals(pedidoDTO.getId().getCodigoPedido())){
						pedidoColActualizado.remove(pedidoConsolidado);
						break;
					}
				}
				for(DetallePedidoDTO detPedActualDTO:colDetallePedidoDTO){
					for(DetallePedidoDTO detPedDTO:colDetallePedidoConsolidadoProcesar){
						if(detPedDTO.getId().getCodigoArticulo().equals(detPedActualDTO.getId().getCodigoArticulo())&& 
								 detPedDTO.getId().getCodigoPedido().equals(pedidoDTO.getId().getCodigoPedido())){
							colDetallePedidoConsolidadoProcesar.remove(detPedDTO);
							break;
						}
					}
				}
				
				pedidoDTO.setPedidosConsolidar(pedidoColActualizado);	
			}
		}

		try{
			PedidoDTO pedidoActualDTO = null;
			pedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			if(cotizacion){
				//se registra el pedido en la base de datos y se retorna el n\u00FAmero de la Cotizaci\u00F3n
				LogSISPE.getLog().info("Cotizacion");
				
				pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().transRegistrarCotizacion(pedidoDTO, colDetallePedidoDTO,colDetallePedidoConsolidadoProcesar);
			}else{
				LogSISPE.getLog().info("Re-Cotizacion");
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = (CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL);
				LogSISPE.getLog().info("**CALENDARIOCONFIGURACIONDIALOCAL GUARDARCOTIZACION** {}" , calendarioConfiguracionDiaLocalDTO);
				
				LogSISPE.getLog().info("Localizacion seleccionada: {}", session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP));
				//se registra la recotizacion en la base de datos
				pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().transRegistrarRecotizacion(pedidoDTO, colDetallePedidoDTO,calendarioConfiguracionDiaLocalDTO,pedidoDTO.getPedidosConsolidar(), colDetallePedidoConsolidadoDTO);
			}

			session.setAttribute(PEDIDO_GENERADO,pedidoActualDTO);
			//consulta para crear la vista y obtener las configuraciones	
			VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
			List<VistaPedidoDTO> pedidos = null;
			consultaVistaPedidoDTO.getId().setCodigoCompania(pedidoActualDTO.getId().getCodigoCompania());
			consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoActualDTO.getId().getCodigoAreaTrabajo());
			consultaVistaPedidoDTO.getId().setCodigoPedido(pedidoActualDTO.getId().getCodigoPedido());
			consultaVistaPedidoDTO.getId().setCodigoEstado(pedidoActualDTO.getEstadoPedidoDTO().getId().getCodigoEstado());
			consultaVistaPedidoDTO.getId().setSecuencialEstadoPedido(pedidoActualDTO.getEstadoPedidoDTO().getId().getSecuencialEstadoPedido());
			pedidos = (List<VistaPedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
			if(CollectionUtils.isNotEmpty(pedidos)){
				consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(0);
				//para que haga los calculos restantes, para mostrar la vista
				consultaVistaPedidoDTO.setVistaDetallesPedidos(null);
				consultaVistaPedidoDTO.setVistaDetallesPedidosReporte(null);
				EstadoPedidoUtil.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
				if(consultaVistaPedidoDTO.getNumBonNavEmp()!=null){
					formulario.setNumBonNavEmp(consultaVistaPedidoDTO.getNumBonNavEmp().toString());
				}else{
					formulario.setNumBonNavEmp(null);
				}
			}
			//esta variable verifica si es el registro final de la cotizaci\u00F3n
			if(esRegistroFinal){
				//se elimina la variable de sesi\u00F3n que indica que la reservacion est\u00E1 en proceso
				session.removeAttribute(PEDIDO_EN_PROCESO);
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Resumen de la cotizaci\u00f3n");
				
				//se verifica si existe en sesi\u00F3n los d\u00EDas de validez de una cotizaci\u00F3n
				//esta verificaci\u00F3n es necesaria cuando se guarda la reservaci\u00F3n de forma temporal 
				//(boton GUARDAR en el formulario de Reservaci\u00F3n)
				if(session.getAttribute(DIAS_VALIDEZ)==null){
					ParametroDTO parametroDiasValidez = new ParametroDTO();
					parametroDiasValidez.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					String diasValidez = SessionManagerSISPE.getServicioClienteServicio().transObtenerDiasValidezCotizacion(parametroDiasValidez);
					session.setAttribute(DIAS_VALIDEZ,diasValidez);
				}
				
				//mensaje de exito para el registro
				exitos.add("validezCotizacion",new ActionMessage("message.cotizacion.validez",session.getAttribute(DIAS_VALIDEZ)));
				session.setAttribute(TRANSACCION_REALIZADA,"ok");
//				if(pedidoDTO.getPedidosConsolidar()!= null && pedidoDTO.getPedidosConsolidar().size()>0){
//					return "desplegar";
//				}
				//se retorna el valor del forward
				return "registro";
			}
			//mensaje de exito para el registro autom\u00E1tico
			exitos.add("validezCotizacion",new ActionMessage("message.exito.registro", "La Cotizaci\u00F3n"));

			//solo si existen recetas
			if(session.getAttribute(CotizarReservarForm.EXISTEN_RECETAS)!=null){
				//se itera el detalle del pedido
				for(DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO){
					//si esta propiedad no es nula significa que este canasto se guardo anteriormente como una nueva receta
					//y se debe asignar null a su valor para un registro posterior
					if(detallePedidoDTO.getArticuloDTO().getNpArticuloAnteriorDTO()!=null){
						detallePedidoDTO.getArticuloDTO().setNpArticuloAnteriorDTO(null);
					}
				}
			}
			AutorizacionesUtil.verificacionAutorizaciones(pedidoActualDTO.getId().getCodigoPedido(), request, errors, exitos);
			return "desplegar";

		}catch(SISPEException ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			//esta variable verifica si es el registro final de la cotizaci\u00F3n
			if(esRegistroFinal){
				LogSISPE.getLog().info("CODIGO ERRROR: {}",ex.getCodigoError());			
				errors.add("registrarCotizacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la cotizaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en GUARDAR"));
				//si la ACCION_ACTUAL y la accion por donde entro al metodo no coinciden se pone en la variable de session la accion por donde se entro
				if(!session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).toString().equals(accion)){
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,accion);
				}
			}else{
				errors.add("registroAutomatico", new ActionMessage("errors.llamadaServicio.registrarDatos", "la Cotizaci\u00F3n, verifique los datos"));
			}
			errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
			//se retorna el valor del forward
			return "desplegar";
		}
	}

	/**
	 * Crea al objeto PedidoDTO
	 * 
	 * @param  accion			String que indica la acci\u00F3n que se est\u00E1 ejecutando	
	 * @param  formulario		Formulario manejado actualmente
	 * @param  request			La petici\u00F3n manejada actualmente
	 * @return pedidoDTO			El objeto que identifica un <code>PedidoDTO</code>
	 */
	private PedidoDTO crearPedidoDTO(String accion,CotizarReservarForm formulario, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		boolean tieneDescuentoTipoMaxNavEmp = false;		
		PedidoDTO pedidoDTO = new PedidoDTO();
		EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();
		//se crea el convertidor
		convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
		Timestamp fechaMinEntrega = null;
		LogSISPE.getLog().info("fecha de entrega: {}",formulario.getFechaEntrega());
		if(formulario.getFechaEntrega() != null){
			//se obtiene la fecha de entrega del formulario y se le cambia el formato
			fechaMinEntrega = (Timestamp)convertidor.convert(Timestamp.class,formulario.getFechaEntrega());
		}
		//se obtiene la accion anterior
		String accionAnterior = (String)session.getAttribute(ACCION_ANTERIOR);
		try
		{
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			//se obtiene el id del usuario logeado
			String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
			LogSISPE.getLog().info("accionAnterior: {}",accionAnterior);
			
			//se obtiene la vistaPedidoDTO
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			//esta condici\u00F3n se cumple cuando la vistaPedidoDTO es nula
			if(vistaPedidoDTO == null){
				//cuando se realiza una cotizaci\u00F3n desde cero
				pedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//se obtiene el c\u00F3digo del local
				LogSISPE.getLog().info("CODIGO LOCAL: {}",SessionManagerSISPE.getCodigoLocalObjetivo(request));
				pedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
				//se verifica si la cotizaci\u00F3n fue guardada anteriormente
				PedidoDTO pedidoDTOGenerado = (PedidoDTO)session.getAttribute(PEDIDO_GENERADO);
				if(pedidoDTOGenerado != null){
					pedidoDTO.getId().setCodigoPedido(pedidoDTOGenerado.getId().getCodigoPedido());
					pedidoDTO.setCodigoClientePedido(pedidoDTOGenerado.getCodigoClientePedido());
				}
			}else{
				//cuando se guarda la modificaci\u00F3n de una cotizaci\u00F3n anterior
				pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
				pedidoDTO.setCodigoClientePedido(vistaPedidoDTO.getId().getCodigoClientePedido());
				pedidoDTO.setDescripcionPedido(vistaPedidoDTO.getObsmigperTemp());
				pedidoDTO.setCodigoPedidoRelacionado(vistaPedidoDTO.getCodigoPedidoRelacionado());
				if(vistaPedidoDTO.getCodigoEstadoPagado()!=null)
					estadoPedidoDTO.setCodigoEstadoPagado(vistaPedidoDTO.getCodigoEstadoPagado());
			}//pedidoDTO.setCodigoEstadoPagado(vistaPedidoDTO.getCodigoEstadoPagado());

			//se inicializa el valor para la confirmaci\u00F3n de una reserva y para especiales reservados
			pedidoDTO.setConfirmarReserva(estadoInactivo);
			pedidoDTO.setEspecialesReservados(estadoInactivo);

			//se verifica si el usuario logeado es de la bodega
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
				if(session.getAttribute(CrearReservacionAction.INGRESA_DIRECTAMENTE_RESERVAR)==null
						&& session.getAttribute(CrearRecotizacionAction.INGRESA_DIRECTAMENTE_RECOTIZAR) == null){
					//si el usuario es la bodega se debe tomar de este campo, no de la entidad responsable
					//se obtienen los datos del local
					VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
					if(vistaLocalDTO != null){
						//se concatenan los valores del c\u00F3digo y nombre del local
						formulario.setLocalResponsable(vistaLocalDTO.getId().getCodigoLocal()+"-"+vistaLocalDTO.getNombreLocal());
					}
				}
				pedidoDTO.setNpDescripcionLocalOrigen(formulario.getLocalResponsable());
			}else{
				pedidoDTO.setNpDescripcionLocalOrigen(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal() 
						+"-" +SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
			}
			
			String accionReservacion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion");
			String accionModReservacion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion");
			
			//la siguiente condici\u00F3n se aplica solo para la reservaci\u00F3n
			if((accion.equals(accionReservacion) && session.getAttribute(CotizarReservarForm.RESERVACION_TEMPORAL) == null)
					|| accion.equals(accionModReservacion)){

				//se verifica el proceso actual
				if(accion.equals(accionReservacion)){//&& formulario.getCheckReservarStockEntrega()==null && !formulario.getCheckReservarStockEntrega().equals(estadoActivo))
					try{
						Double abonoManualFormateado = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()), NUMERO_DECIMALES);
						//se asignan los valores del abono inicial al estado
						estadoPedidoDTO.setPorcentajeAbonoInicial((Double)session.getAttribute(PORCENTAJE_ABONO));
						estadoPedidoDTO.setValorAbonoInicialSistema((Double)session.getAttribute(VALOR_ABONO));
						estadoPedidoDTO.setValorAbonoInicialManual(abonoManualFormateado);
						//indicamos que es una nueva reservacion
						estadoPedidoDTO.setNpEstadoModificacionReserva(TIPO_RESERVACION_NUEVO);					
					}catch(NumberFormatException e){
						ActionMessages errors = new ActionMessages();
						errors.add("Exception",new ActionMessage("errors.formato.double","Valor abono"));
					}
				}else if(accion.equals(accionModReservacion)){
					
					//indicamos que se esta modificando la reserva
					estadoPedidoDTO.setNpEstadoModificacionReserva(TIPO_RESERVACION_MODIFICACION);
					
					//esta condici\u00F3n se aplica para la modificaci\u00F3n de la reserva
					if(session.getAttribute(ModificarReservacionAction.PEDIDO_SIN_PAGO) != null){
						//pedido sin abonos
						Double abonoManualFormateado = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()),NUMERO_DECIMALES);
						//se asignan los valores del abono inicial al estado
						estadoPedidoDTO.setPorcentajeAbonoInicial((Double)session.getAttribute(PORCENTAJE_ABONO));
						estadoPedidoDTO.setValorAbonoInicialSistema((Double)session.getAttribute(VALOR_ABONO));
						estadoPedidoDTO.setValorAbonoInicialManual(abonoManualFormateado);
					}else{
						//pedido con abonos realizados
						if(session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO)!=null){
							Double totalReserva = Util.roundDoubleMath(formulario.getTotal(), NUMERO_DECIMALES);
							estadoPedidoDTO.setValorAbonoInicialSistema(totalReserva);
							estadoPedidoDTO.setValorAbonoInicialManual(totalReserva);
						}else if(vistaPedidoDTO != null){
							//se asignan los valores del abono
							estadoPedidoDTO.setValorAbonoInicialSistema(vistaPedidoDTO.getAbonoPedido());
							estadoPedidoDTO.setValorAbonoInicialManual(vistaPedidoDTO.getAbonoPedido());
						}
						
						if(vistaPedidoDTO != null) estadoPedidoDTO.setPorcentajeAbonoInicial(vistaPedidoDTO.getPorcentajeAbonoInicial());
					}
				}
				
				//se verifica la condici\u00F3n de existencia de pavos
				if(session.getAttribute(EXISTEN_PAVOS)!=null)
					pedidoDTO.setConfirmarReserva(estadoActivo);
				
				//solo si existen art\u00EDculos especiales reservados en bodega
				if(session.getAttribute(EXISTEN_ESPECIALES_RESERVADOS) != null)
					pedidoDTO.setEspecialesReservados(estadoActivo);				
				//berifica numero/bonos-maxi-navidad
				CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad((Collection)session.getAttribute(COL_DESCUENTOS), request,null);			
			}
			else if(accion.equals(accionReservacion) && StringUtils.isNotEmpty(formulario.getValorAbono())){
				//se guarda solo el valor del abono cuando esta en la opcion de reservacion y presiona el boton guardar
				Double abonoManualFormateado = Util.roundDoubleMath(Double.valueOf(formulario.getValorAbono()), NUMERO_DECIMALES);
				//se asignan los valores del abono inicial al estado
				estadoPedidoDTO.setPorcentajeAbonoInicial((Double)session.getAttribute(PORCENTAJE_ABONO));
				estadoPedidoDTO.setValorAbonoInicialSistema((Double)session.getAttribute(VALOR_ABONO));
				estadoPedidoDTO.setValorAbonoInicialManual(abonoManualFormateado);
			}

			LogSISPE.getLog().info("codigo_compania: "+pedidoDTO.getId().getCodigoCompania()
					+	", codigo_local: "+pedidoDTO.getId().getCodigoAreaTrabajo()
					+	", codigo_pedido: "+pedidoDTO.getId().getCodigoPedido()
					+	", confirmar reserva: "+pedidoDTO.getConfirmarReserva()
					+	", codigo_clientePedido: "+pedidoDTO.getConfirmarReserva());

			/**
			 * @author wlopez
			 * Permite verificar si un cliente pedido existe ya sea por persona o empresa
			 */
			boolean modificarClientePedido=false;
			if(pedidoDTO.getCodigoClientePedido()==null){
				modificarClientePedido=true;
			}
			//@autor bgudino
			//se actualizan los datos del contacto en el caso que se haya modificado					
			else{
				//existe un cambio de localizacion desde el componente de empresas
				if (vistaPedidoDTO==null || (session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP)!=null && vistaPedidoDTO.getCodigoLocalizacion()!=null && !vistaPedidoDTO.getCodigoLocalizacion().equals(
						((LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP)).getId().getCodigoLocalizacion().toString()))) {
					
					modificarClientePedido=true;
					
				}else {
					//para el caso de personas
					if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
							|| vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){												
						if(!vistaPedidoDTO.getNumeroDocumentoPersona().equals(formulario.getNumeroDocumento())){ 
								modificarClientePedido=true;
						}
					}
					//para el caso de empresas
					else if((vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL))
							&& !vistaPedidoDTO.getRucEmpresa().equals(formulario.getNumeroDocumento())){													
								modificarClientePedido=true;
							}
					}				
			}
			ClientePedidoDTO cliePed = null;

			// se verifica si el numero de documento del contacto ha cambiado
			if (modificarClientePedido) {
				cliePed = ContactoUtil.consultarClientePedido(formulario.getTipoDocumento(), request);

				if (cliePed!=null && cliePed.getId().getCodigoClientePedido() != null) {
					pedidoDTO.setCodigoClientePedido(cliePed.getId().getCodigoClientePedido());
					pedidoDTO.setClientePedidoDTO(cliePed);
				}else{
					pedidoDTO.setCodigoClientePedido(null);
					pedidoDTO.setClientePedidoDTO(cliePed);
				}
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumento());
			}
			
			if(formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) ||
					formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
					|| (formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null)){
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
				pedidoDTO.setNpCedulaContacto(formulario.getNumeroDocumentoPersona());
				pedidoDTO.setNpNombreContacto(formulario.getNombrePersona());
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumento());
				pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoPersona());
				if (request.getSession().getAttribute(ContactoUtil.RUC_PERSONA) != null){
					pedidoDTO.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.ruc.persona.natural"));
				}
			}
			else if(formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || 
					formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){																
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
				pedidoDTO.setNpCedulaContacto(formulario.getNumeroDocumentoContacto());
				pedidoDTO.setNpNombreContacto(formulario.getNombreContacto());
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumentoContacto());
				pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoContacto());
				//se setean los datos de la empresa
				pedidoDTO.setNpNombreEmpresa(formulario.getNombreEmpresa());
				pedidoDTO.setNpRucEmpresa(formulario.getRucEmpresa());
				pedidoDTO.setNpTelefonoEmpresa(formulario.getTelefonoEmpresa());
			}
						
			pedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());//campos de auditoria
			pedidoDTO.setSysId(SessionManagerSISPE.getDefault().getDefaultSystemId());

			//se asigna el tipo de pedido
			pedidoDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
			
			LogSISPE.getLog().info("tipo de pedido: {}",pedidoDTO.getContextoPedido());
			
			//asignacion de la entidad responsable
			if(session.getAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE)!=null)
				pedidoDTO.setEntidadResponsable((String)session.getAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE));
			LogSISPE.getLog().info("tipo de pedido: {}",pedidoDTO.getContextoPedido());
			
			pedidoDTO.setFechaMinimaEntrega(fechaMinEntrega);

			//pedidoDTO.setDireccionEntrega(direccionEntrega);
			pedidoDTO.setNpCodigosArticulos((Collection)session.getAttribute(COL_CODIGOS_ARTICULOS));

			//fecha minima de entrega y despacho
			pedidoDTO.setPrimeraFechaEntrega(formulario.getFechaMinimaEntrega());
			pedidoDTO.setPrimeraFechaDespacho(formulario.getFechaMinimaDespacho());
			LogSISPE.getLog().info("fechaMinimaDespacho: {}" , formulario.getFechaMinimaDespacho());
			LogSISPE.getLog().info("fechaMinimaEntrega: {}" , formulario.getFechaMinimaEntrega());
			//fecha m\u00E1xima de entrega
			pedidoDTO.setUltimaFechaEntrega(formulario.getFechaMaximaEntrega());
			LogSISPE.getLog().info("fechaM\u00E1ximaEntrega: {}" , formulario.getFechaMaximaEntrega());

			Collection descuentos = (Collection)session.getAttribute(COL_DESCUENTOS);
			pedidoDTO.setDescuentosEstadosPedidos(descuentos);
			
			//jmena se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
			String codigoTipoDescuentoPagEfe = CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
			
			//jmena validacion maxi navidad empresarial
			if(descuentos != null && !descuentos.isEmpty()){
				for(DescuentoEstadoPedidoDTO descuentoEsPedDTO : (ArrayList<DescuentoEstadoPedidoDTO>)descuentos){
					if(descuentoEsPedDTO.getDescuentoDTO() != null && descuentoEsPedDTO.getDescuentoDTO().getTipoDescuentoDTO() != null
							&& descuentoEsPedDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe)){
						tieneDescuentoTipoMaxNavEmp = true;
						break;
					}
				}
				if(tieneDescuentoTipoMaxNavEmp){
					pedidoDTO.setNpPagoEfectivo(true);
				}else{
					if((String)session.getAttribute(CHECK_PAGO_EFECTIVO)!= null && ((String)session.getAttribute(CHECK_PAGO_EFECTIVO)).equals("ok")){ 
						pedidoDTO.setNpPagoEfectivo(true);
					}
					else{
						pedidoDTO.setNpPagoEfectivo(false);
					}
				}
			}else{
				if((String)session.getAttribute(CHECK_PAGO_EFECTIVO)!= null && ((String)session.getAttribute(CHECK_PAGO_EFECTIVO)).equals("ok")){ 
					pedidoDTO.setNpPagoEfectivo(true);
				}
				else{
					pedidoDTO.setNpPagoEfectivo(false);
				}
			}

			//se formatean los totales del pedido antes de almacenarlos
			formulario.setDescuentoTotal((Double)session.getAttribute(DESCUENTO_TOTAL));
			Double descuentoFormateado = Util.roundDoubleMath(formulario.getDescuentoTotal(),NUMERO_DECIMALES);
			Double subTotalFormateado = Util.roundDoubleMath(formulario.getSubTotal(),NUMERO_DECIMALES);
			Double subTotalAplicaIVAFormateado = Util.roundDoubleMath(formulario.getSubTotalAplicaIVA(),NUMERO_DECIMALES);
			Double subTotalNoAplicaIVAFormateado = Util.roundDoubleMath(formulario.getSubTotalNoAplicaIVA(),NUMERO_DECIMALES);
			Double ivaTotalFormateado = Util.roundDoubleMath(formulario.getIvaTotal(),NUMERO_DECIMALES);
			Double totalPedidoFormateado = Util.roundDoubleMath(formulario.getTotal(),NUMERO_DECIMALES);

			//se asignan los totales por estado
			estadoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			estadoPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
			estadoPedidoDTO.setDescuentoTotalPedido(descuentoFormateado); //aqui guarda  los pedidos
			estadoPedidoDTO.setPorcentajeTotalDescuento((Double)session.getAttribute(PORCENTAJE_TOT_DESCUENTO));
			double porcentajeSubTotalDescuento= estadoPedidoDTO.getDescuentoTotalPedido().doubleValue()*100 / subTotalFormateado.doubleValue();
			estadoPedidoDTO.setPorcentajeSubTotalDescuento(Double.valueOf(porcentajeSubTotalDescuento));
			estadoPedidoDTO.setSubTotalPedido(subTotalFormateado);
			estadoPedidoDTO.setSubTotalAplicaIVA(subTotalAplicaIVAFormateado);
			estadoPedidoDTO.setSubTotalNoAplicaIVA(subTotalNoAplicaIVAFormateado);
			estadoPedidoDTO.setIvaPedido(ivaTotalFormateado);
			estadoPedidoDTO.setTotalPedido(totalPedidoFormateado);
			estadoPedidoDTO.setUserId(userId);
			estadoPedidoDTO.setEstadoValidacion(formulario.getOpValidarPedido());

			//este campo indica el recargo por las entregas realizadas a domicilio
			estadoPedidoDTO.setValorCostoEntregaPedido(formulario.getCostoFlete());
			
			//se asigna el estado de calculos con precios de afiliado
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null){
				estadoPedidoDTO.setEstadoPreciosAfiliado(estadoActivo);
			}else{
				estadoPedidoDTO.setEstadoPreciosAfiliado(estadoInactivo);
			}
			
			//se asigna el estado de calculos con iva o sin iva
			estadoPedidoDTO.setEstadoCalculosIVA((String)session.getAttribute(CALCULO_PRECIOS_CON_IVA));
			if(estadoPedidoDTO.getEstadoCalculosIVA() == null){
				estadoPedidoDTO.setEstadoCalculosIVA(estadoActivo);
			}
			
			/*guardar numero de bonos
			 *@autor osaransig*/
			
			Object numBonosObject = request.getSession().getAttribute(NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
			if (numBonosObject != null && ((Integer) numBonosObject) > 0 ) {
				estadoPedidoDTO.setNumBonNavEmp((Integer) numBonosObject);
			}
			
			pedidoDTO.setEstadoPedidoDTO(estadoPedidoDTO);

			//se obtiene de sesion la coleccion de autorizaciones genericas
			Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO> autorizacionesPedidoCol = 
					(Collection<ec.com.smx.autorizaciones.dto.AutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
			if(CollectionUtils.isNotEmpty(autorizacionesPedidoCol)){
				pedidoDTO.setNpAutorizacionesPedidoCol(autorizacionesPedidoCol);
			}

			pedidoDTO.setClienteProcesadoCorporativo(estadoInactivo);
			pedidoDTO.setArchivoBeneficiario(estadoInactivo);
			
			//se verifica si es por modificaci\u00F3n de la reserva
			if(accion.equals(accionModReservacion)){
				LogSISPE.getLog().info("---Entro por la accion ModificarReserva--{}",accion);
				pedidoDTO.setNpModificarReserva(estadoActivo);
			} else{
				LogSISPE.getLog().info("---No Entro por la accion ModificarReserva--{}",accion);
				pedidoDTO.setNpModificarReserva(estadoInactivo);
			}
			
			 if (vistaPedidoDTO!= null && vistaPedidoDTO.getNpEsNuevaReserva()) {
				 LogSISPE.getLog().info("--- Se cambia a nueva reserva ---");
				 pedidoDTO.setNpModificarReserva(estadoInactivo);
			 }
			 
			 if(vistaPedidoDTO!= null && vistaPedidoDTO.getNpCrearNuevoPedido()  !=null && vistaPedidoDTO.getNpCrearNuevoPedido()){
				 pedidoDTO.setNpCrearNuevoPedido(Boolean.TRUE);
			 }
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
			session.setAttribute("ec.com.smx.sic.sispe.pedido.fechaPedido",simpleDateFormat.format(new Date()));	
			
			//se asigna la coleccion  calendarioHoraLocalCol al pedido
			Collection<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol = (Collection<CalendarioHoraLocalDTO>) session.getAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL);
			if(calendarioHoraLocalDTOCol != null){
				pedidoDTO.setNpCalendarioHoraLocalDTOCol(calendarioHoraLocalDTOCol);
			}
			
			//se asigna la colleccion calendarioDetalleHoraCamionLocalCol al pedido
			Collection<CalendarioDetalleHoraCamionLocalDTO> calendarioDetalleHoraCamionLocalDTOCol = (Collection<CalendarioDetalleHoraCamionLocalDTO>) session.getAttribute(EntregaLocalCalendarioUtil.CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
			if(CollectionUtils.isNotEmpty(calendarioDetalleHoraCamionLocalDTOCol)){
				pedidoDTO.setNpCalendarioDetalleHoraCamionLocalCol(calendarioDetalleHoraCamionLocalDTOCol);
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			return null;
		}
		return pedidoDTO;
	}

	/**
	 * Inicializa los datos necesarios para la reservaci\u00F3n
	 * 
	 * @param  formulario		Formulario manejado actualmente
	 * @param  request 			La petici\u00F3n que se est\u00E1 procesando actualmente
	 */		
	private void inicializarReservacion(CotizarReservarForm formulario,HttpServletRequest request)throws Exception{
		
		HttpSession session = request.getSession();
		//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la reservacion
		session.setAttribute(MOSTRAR_AUTORIZACION, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
		session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Formulario de reservaci\u00f3n");
		formulario.setOpAutorizacion(SessionManagerSISPE.getEstadoInactivo(request));
		
		//se cargan los par\u00E1metros necesarios para la reservaci\u00F3n
		String codigoPorcentajeAbono = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.porcentajeAbonoInicial");
		String codigoPorcentajeCalculoFlete = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.porcentajeCalculoFlete");
		String codigoFecMinEntCD = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.fechaEntrega");
		String codigoFecMinDesLoc = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal");
	
		String [] codigosParametros = new String [] {codigoPorcentajeAbono,codigoPorcentajeCalculoFlete,codigoFecMinEntCD,codigoFecMinDesLoc};
		
		//se consultan los par\u00E1metros
		Collection<ParametroDTO> colParametroDTO = WebSISPEUtil.obtenerParametrosAplicacion(codigosParametros, request);
		
		if(colParametroDTO != null){
			for(ParametroDTO parametroDTO : colParametroDTO){
				if(parametroDTO.getId().getCodigoParametro().equals(codigoPorcentajeAbono)){
					//se almacena en sesi\u00F3n el porcentaje y el abono
					session.setAttribute(PORCENTAJE_ABONO, Double.valueOf(parametroDTO.getValorParametro()));
					//se actualiza el abono
					formulario.actualizarAbono(request, formulario.getTotal());
				}else if(parametroDTO.getId().getCodigoParametro().equals(codigoPorcentajeCalculoFlete)){
					try{
						//se guarda el sesi\u00F3n el valor del par\u00E1metro para el porcentaje del c\u00E1lculo del flete
						session.setAttribute(PORCENTAJE_CALCULO_FLETE, Double.valueOf(parametroDTO.getValorParametro()));
					}catch(NumberFormatException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						session.setAttribute(PORCENTAJE_CALCULO_FLETE, 0D);
					}
				}else if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinEntCD) || parametroDTO.getId().getCodigoParametro().equals(codigoFecMinDesLoc)){
					//se crea un formato para la fecha
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
					int diasSumar = Integer.parseInt(parametroDTO.getValorParametro());
					if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinDesLoc)){
						diasSumar ++; //se suma un d\u00EDa porque el par\u00E1metro es para calcular la fecha m\u00EDnima de despacho
					}
					
					//para obtener la fecha m\u00EDnima de entrega se suman los d\u00EDas del par\u00E1metro
					Timestamp fechaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, diasSumar);
					//se formatea la fecha de entrega
					String minimaFechaEntrega = simpleDateFormat.format(fechaEntrega);
					
					if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinEntCD)){
						session.setAttribute(FECHA_MIN_ENTREGA_CD_RES,minimaFechaEntrega);
						formulario.setFechaEntrega(minimaFechaEntrega);
					}else{
						session.setAttribute(FECHA_MIN_ENTREGA_LOC_RES,minimaFechaEntrega);
					}
				}
			}
		}
		
		//se obtiene la descripci\u00F3n del tipo de autorizaci\u00F3n gen\u00E9rico que permite crear una reservaci\u00F3n
//		TipoAutorizacionDTO tipoAutorizacionFiltro = new TipoAutorizacionDTO();
//		tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		tipoAutorizacionFiltro.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"));
//		

//		Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCol = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionFiltro);
//		tipoAutorizacionFiltro = null;
//		//se obtiene el \u00FAnico registro
//		if(tipoAutorizacionDTOCol != null && !tipoAutorizacionDTOCol.isEmpty()){
//			//se guarda la descripci\u00F3n en sesi\u00F3n
//			session.setAttribute(DESCRICPION_USO_AUTORIZACION, tipoAutorizacionDTOCol.iterator().next().getDescripcion());
//		}
		//Se inicializa el tipo de autorizacion generico
		AutorizacionesUtil.iniciarTipoAutorizacionGenerico(request);

	}

	/**
	 * Aplica descuentos al pedido
	 * @param formulario
	 * @param request
	 * @param errors
	 * @param infos
	 * @param warnings
	 * @param exitos
	 * @throws Exception
	 */
	public static void aplicarDescuento(Collection<DescuentoDTO> descuentoVariableCol, 
			Collection <String>llavDesCol, ArrayList<DetallePedidoDTO> detallePedido, 
			CotizarReservarForm formulario,final HttpServletRequest request,
			ActionMessages errors, ActionMessages warnings, ActionMessages exitos)throws Exception{
		
		HttpSession session=request.getSession();
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		Collection <String>llaveDescuentoCol = llavDesCol; 
		
		try{

			//si fue seleccionado el descuento variable
			/*if(descuentosVarSeleccionados != null){
				for(DescuentoDTO descuentoDTO: descuentosVarSeleccionados){
					//se actualiza primero en la base de datos el porcentaje ingresado para el descuento variable
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarDescuento(descuentoDTO);
				}
			}*/
			//se obtienen las claves que indican un estado activo y un estado inactivo
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);	
			
			//se saca un respaldo del vectorCantidad para que en el metodo actualizarDetalleForm realice los procesos correctamente
			CotizacionReservacionUtil.respaldoIndicesCantidadesPesos(detallePedido, formulario, request,accion, estadoActivo, estadoInactivo);
			

			//llamada al m\u00E9todo de la capa de servicio
			String []descuentosSel= (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO ;//= (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(COL_DESCUENTOS) ;	
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(COL_DESCUENTOS_VARIABLES);			
			
			
			if(CollectionUtils.isEmpty(colDescuentoEstadoPedidoDTOVariables)){
				colDescuentoEstadoPedidoDTOVariables = new ArrayList<DescuentoEstadoPedidoDTO>();
			}
			VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			//control para que no se pierdan los descuentos por caja y mayorista
			Boolean aplicaDsctoCajas=Boolean.FALSE;
			Boolean aplicaDsctoMayorista=Boolean.FALSE;
			
			if(descuentosSel != null && descuentosSel.length > 0){
				for(int i=0;i<descuentosSel.length;i++){
					if(descuentosSel[i] != null && !descuentosSel.equals("")){
						if(descuentosSel[i].equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS)))){
							request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
							request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS_POPUP,"ok");
							aplicaDsctoCajas=Boolean.TRUE;
						}else{
							if(descuentosSel[i].equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA)))){
								request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
								aplicaDsctoMayorista=Boolean.TRUE;
								request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA_POPUP,"ok");
							}
						}
						if(!aplicaDsctoCajas  && ((String)request.getSession().getAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_CAJAS))!=null){
							request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
							request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS_POPUP);
						}
						if(!aplicaDsctoMayorista  && ((String)request.getSession().getAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_MAYORISTA))!=null){
							request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
							request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA_POPUP);
						}
					}
				}
			}
			

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

			colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
			colDescuentoEstadoPedidoDTO.addAll(colDescuentoEstadoPedidoDTOVariables);						
			int contLlavesVariables = 0;
			for(String llaveActual : llaveDescuentoCol){
				if(llaveActual.split(SEPARADOR_TOKEN).length > 3){
					contLlavesVariables++;
				}
			}
			
			//es un descuento variable			
			if(contLlavesVariables == llaveDescuentoCol.size() && contLlavesVariables>0){
				Collection<DescuentoEstadoPedidoDTO> descuentosProcesados = SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido,llaveDescuentoCol,descuentoVariableCol);
				//se crea el descuento con valores cero cuando el articulo con autorizacion ya fue aplicado otro descuento (MAYORISTA)
				if(descuentosProcesados.size() == 0){
					String[] valorReferencial = llaveDescuentoCol.iterator().next().split(SEPARADOR_TOKEN);
					
					DescuentoEstadoPedidoDTO descuentoVar = new  DescuentoEstadoPedidoDTO() ;
					descuentoVar.setDescuentoDTO(descuentoVariableCol.iterator().next());
					descuentoVar.getDescuentoDTO().getTipoDescuentoDTO().getId().setCodigoTipoDescuento(valorReferencial[1].split(CODIGO_TIPO_DESCUENTO)[1]);
					descuentoVar.getDescuentoDTO().getTipoDescuentoDTO().getId().setCodigoCompania(descuentoVar.getDescuentoDTO().getId().getCodigoCompania());
					descuentoVar.getDescuentoDTO().getTipoDescuentoDTO().setDescripcionTipoDescuento("VARIABLE");
					descuentoVar.setValorPrevioDescuento(0D);
					descuentoVar.setValorMotivoDescuento(0D);
					descuentoVar.setValorDescuento(0D);
					descuentoVar.setPorcentajeDescuento(descuentoVariableCol.iterator().next().getPorcentajeDescuento());
					descuentoVar.getId().setCodigoReferenciaDescuentoVariable(valorReferencial[3]);
					descuentoVar.getId().setCodigoDescuento(descuentoVar.getDescuentoDTO().getId().getCodigoDescuento());
					descuentoVar.setRangoInicialDescuento(descuentoVar.getDescuentoDTO().getRangoInicialDescuento());
					descuentoVar.setRangoFinalDescuento(descuentoVar.getDescuentoDTO().getRangoFinalDescuento());
					
					descuentoVar.setLlaveDescuento(valorReferencial[0]+SEPARADOR_TOKEN+valorReferencial[1]+SEPARADOR_TOKEN+valorReferencial[2]+SEPARADOR_TOKEN+valorReferencial[3]+SEPARADOR_TOKEN+valorReferencial[4].substring(valorReferencial[4].length()-1));
					descuentosProcesados.add(descuentoVar);
					CotizacionReservacionUtil.agregarDescripcionDescuentosVariables(descuentosProcesados, request);
				}
				colDescuentoEstadoPedidoDTO.addAll(descuentosProcesados);
				
//				colDescuentoEstadoPedidoDTO.addAll(SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido,llaveDescuentoCol,descuentoVariableCol));
				
				request.getSession().setAttribute(COL_DESCUENTOS_VARIABLES, new ArrayList<DescuentoEstadoPedidoDTO>(colDescuentoEstadoPedidoDTO));					
				request.getSession().removeAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS);
			}
			else {												
				if(CollectionUtils.isEmpty(llaveDescuentoCol) && CollectionUtils.isEmpty(descuentoVariableCol)){
					SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido,null, null);
				}
				else{
					colDescuentoEstadoPedidoDTO.addAll(SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido,llaveDescuentoCol,descuentoVariableCol));
				}
			}
			
			//se guarda en sesi\u00F3n el pedido seleccionado
	        
	        String pedidoGeneral=(String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral");
			//eliminar detalles que no pertenecen al pedido
			if(pedidoGeneral==null){
				List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
				if(vistaPedidoDTO!=null){
					for(Iterator<DetallePedidoDTO> itDetPedCon = detallePedido.iterator(); itDetPedCon.hasNext();){
						DetallePedidoDTO detPed=itDetPedCon.next();
						if(!vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
							detallesEliminar.add(detPed);
						}
					}
					detallePedido.removeAll(detallesEliminar);
				}
			}
			double totalDescuento = 0;
			double totalPorcentaje = 0;
			if(colDescuentoEstadoPedidoDTO!=null)
				if(colDescuentoEstadoPedidoDTO.isEmpty() && (descuentosSel==null || descuentosSel.length==0)){
					//warnings.add("errorDescuentos",new ActionMessage("message.descuentos.noAplicados")); 
					session.removeAttribute(DES_MAX_NAV_EMP);
				}
				else if(!colDescuentoEstadoPedidoDTO.isEmpty()){
					if(exitos != null){
						Iterator<ActionMessage> iterator = exitos.get();				
						Boolean encontroClave = Boolean.FALSE;
						
						//se recorre para buscar el key el mensaje
						while(iterator.hasNext()){
							ActionMessage messageActual = iterator.next();
							if(messageActual.getKey().equals("message.exito")){
								encontroClave = Boolean.TRUE;					
							}
						}		
						//si no existe el mensaje se agrega
						if(!encontroClave){		
							exitos.add("exito",new ActionMessage("message.exito","El descuento",""));
						}
					}
					
					//Verifica el numero de bonos-Maxi solo si es reservacion - modificacionReserva
					if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))|| 
							accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))|| 
							accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion"))|| 
							accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion"))|| 
							accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho"))|| 
							accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega"))){
						//berifica numero/bonos-maxi-navidad
						CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad(colDescuentoEstadoPedidoDTO, request,null);
					}
					//valida si debe mostra el mensaje(TODOS LOS PEDIOS DEBEN DE SER PAGADOS EN EFECTIVO)
					if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido")) && 
							CotizacionReservacionUtil.verificadDescuentoMaxi(request, colDescuentoEstadoPedidoDTO)){
						session.setAttribute(CotizarReservarAction.DES_MAX_NAV_EMP, "ok");
					}else{
						session.removeAttribute(DES_MAX_NAV_EMP);
					}
					//se obtiene el c\u00F3digo del tipo de descuento variable
					String codigoTipoDescVariable = (String)session.getAttribute(CODIGO_TIPO_DESCUENTO_VARIABLE);
					if(codigoTipoDescVariable == null){
						//Obtengo el parametro para descuentos variables
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
						if(parametroDTO.getValorParametro() != null){
							codigoTipoDescVariable = parametroDTO.getValorParametro();
							session.setAttribute(CODIGO_TIPO_DESCUENTO_VARIABLE, codigoTipoDescVariable);
						}
					}
					if(session.getAttribute(ES_PEDIDO_CONSOLIDADO)!=null && !(Boolean)session.getAttribute(ES_PEDIDO_CONSOLIDADO)){
						llaveDescuentoCol = new ArrayList<String>();
						//se obtiene la suma de los decuentos
						for(Iterator <DescuentoEstadoPedidoDTO> iterator = colDescuentoEstadoPedidoDTO.iterator();iterator.hasNext();) {
							DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = iterator.next();
							llaveDescuentoCol.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
							LogSISPE.getLog().info("DSCTO EST PEDIDO # {}",descuentoEstadoPedidoDTO.getValorDescuento().doubleValue());
							totalDescuento = totalDescuento + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							totalPorcentaje = totalPorcentaje + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();						
						}
					}else{
						//se obtiene la suma de los decuentos
						for(Iterator <DescuentoEstadoPedidoDTO> iterator = colDescuentoEstadoPedidoDTO.iterator();iterator.hasNext();) {
							DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = iterator.next();
							LogSISPE.getLog().info("DSCTO EST PEDIDO # {}",descuentoEstadoPedidoDTO.getValorDescuento().doubleValue());
							totalDescuento = totalDescuento + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							totalPorcentaje = totalPorcentaje + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();						
						}
					}
					
				}
//				else{
//					exitos.add("exito",new ActionMessage("message.exito","El descuento",""));
//				}
			//si existen llaves en sesion se agregan a la colleccion de llaves para que no se pierdan descuentos
			Collection<String> llavesSesion = (ArrayList<String>) session.getAttribute(COL_DESC_SELECCIONADOS);
			if(CollectionUtils.isNotEmpty(llavesSesion)){
					llaveDescuentoCol.addAll(llavesSesion);
			}
			
			if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
				//se agregan las llaves variables para que se mantengan los descuentos variables
				for(DescuentoEstadoPedidoDTO descuentoVariableAct : colDescuentoEstadoPedidoDTOVariables){
					if(descuentoVariableAct.getLlaveDescuento() != null && !llaveDescuentoCol.contains(descuentoVariableAct.getLlaveDescuento())){
						llaveDescuentoCol.add(descuentoVariableAct.getLlaveDescuento());
					}
				}
			}
				
			//se guarda en sesi\u00F3n el total de descuentos que aplican y no aplican IVA
			session.setAttribute(DESCUENTO_TOTAL, Double.valueOf(totalDescuento));
			formulario.setDescuentoTotal(Double.valueOf(totalDescuento));
			//se guarda en sesi\u00F3n el porcentaje total de los descuentos
			session.setAttribute(PORCENTAJE_TOT_DESCUENTO,Double.valueOf(totalPorcentaje));

			session.setAttribute(COL_DESCUENTOS,colDescuentoEstadoPedidoDTO);
			session.setAttribute(COL_DESC_SELECCIONADOS, llaveDescuentoCol);

			//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
			//CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
			formulario.actualizarDescuentos(request, warnings);

		}
		catch(SISPEException ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
		}
	}

	/**
	 * 
	 * @param formulario
	 * @param request
	 * @param errors
	 * @param warnings
	 * @param exitos
	 * @param infos
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void aceptarDescuento(CotizarReservarForm formulario, HttpServletRequest request,
			ActionMessages errors,ActionMessages warnings,ActionMessages exitos,ActionMessages infos)throws Exception{
		
		HttpSession session=request.getSession();
		ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
		session.removeAttribute(TIPO_AUTORIZADOR);
		//Sumar los valores y cantidades al pedido general y para verificar si aplica o no un descuento
		String pedidoGeneral= (String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
		if(pedidoGeneral==null){
			ConsolidarAction.sumarValoresCantidadesPedidoGeneral(formulario, session, detallePedido);
			//Eliminar valores de descuentos consolidados anteriormente
			//CotizacionReservacionUtil.eliminarDescuentosConsolidados(detallePedidoConsolidado);
		}
		if(detallePedido!=null && !detallePedido.isEmpty())
		{
			LogSISPE.getLog().info("****va a aplicar descuento****");
			//se crea la colecci\u00F3n que almacenar\u00E1 los c\u00F3digos que ser\u00E1n enviados a la capa de servicio
			Collection<String> llaveDescuentoCol = new ArrayList<String>();
			//se crea el arreglo de string para obtener los descuentos seleccionados
			String [] opDescuentos = formulario.getOpDescuentos();
			Double [][]porcentajeVarDescuento=formulario.getPorcentajeVarDescuento();
			
			//validar descuentos excluyentes
			List<String> colDesExclSelec = new ArrayList<String>();
			boolean validarDescExcluyentes = CotizacionReservacionUtil.validarExclusionDescuentos(opDescuentos,
					CotizacionReservacionUtil.getDescuentosExcluyentes(request), colDesExclSelec);
			
			if (validarDescExcluyentes) {
				
				errors.add("", new ActionMessage("errors.descuentosExcluyentesGenerico", 
						CotizacionReservacionUtil.getObtenerTipoDescuentoPorID(colDesExclSelec.get(0), request).getDescripcionTipoDescuento(),
						CotizacionReservacionUtil.getObtenerTipoDescuentoPorID(colDesExclSelec.get(1), request).getDescripcionTipoDescuento()));
				LogSISPE.getLog().info("No se puede seleccionar el descuento  {} y el {}", CotizacionReservacionUtil.getObtenerTipoDescuentoPorID(colDesExclSelec.get(0), request).getDescripcionTipoDescuento(),
						CotizacionReservacionUtil.getObtenerTipoDescuentoPorID(colDesExclSelec.get(1), request).getDescripcionTipoDescuento());
			} else {
			
				//Eliminar todos los descuentos
				//if(formulario.getOpSinDescuentos()!=null){
				//VERIFICAR SI ESTA O NO SELECIONADO EL DESCUENTO POR CAJAS Y MAYORISTA
				Boolean aplicaDsctoCajas=Boolean.FALSE;
				Boolean aplicaDsctoMayorista=Boolean.FALSE;
				
				//se recupera el opDescuentos de sesion para que no se pierda en descuento de cajas cuando aplica un variable
				String[] opDescuentosSesion = (String[]) session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS); 
				if(opDescuentosSesion == null){
					opDescuentosSesion = opDescuentos;
				}
				if(opDescuentosSesion!=null){
					session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
					session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
					for(int i=0;i<opDescuentosSesion.length;i++){
						if(StringUtils.isNotEmpty(opDescuentosSesion[i])){
							if(opDescuentosSesion[i].equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS)))){
								request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
								aplicaDsctoCajas=Boolean.TRUE;
							}else{
								if(opDescuentosSesion[i].equals(((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA)))){
									request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
									aplicaDsctoMayorista=Boolean.TRUE;
								}
							}
							if(!aplicaDsctoCajas && ((String)request.getSession().getAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_CAJAS))!=null){
								session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
							}
							if(!aplicaDsctoMayorista && ((String)request.getSession().getAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_MAYORISTA))!=null){
								session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
							}
						}
					}
				}		
				
				//}
				//Aplicar descuentos seleccionados
				formulario.setOpDescuentos(opDescuentos);
				formulario.setPorcentajeVarDescuento(porcentajeVarDescuento);
				//else 
				if(opDescuentos!=null && opDescuentos.length > 0){
					//se obtiene la colecci\u00F3n de los tipos de descuento
					List tiposDescuento =  (ArrayList)session.getAttribute(COL_TIPO_DESCUENTO);
					boolean descGenSeleccionado = false; //bandera para indicar si fue seleccionado el descuento General
					boolean descVarSeleccionado = false; //bandera para indicar si fue seleccionado el descuento Variable
					boolean descPagEfeSeleccionado = false; //bandera para indicar si fue seleccionado el descuento Pago Efectivo
					boolean errorPorcentajeVariable = false;
					
					Double maxPorcDescVariable = null;
					Double minPorcDescVariable = null;
					Double valor = 0D;
					
					//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento general
					String codigoTipoDescuentoGeneral = (String)session.getAttribute(CODIGO_TIPO_DESCUENTO_GENERAL);
					if(codigoTipoDescuentoGeneral == null){
						//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento general
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoGeneral", request);
						if(parametroDTO.getValorParametro()!=null){
							session.setAttribute(CODIGO_TIPO_DESCUENTO_GENERAL, parametroDTO.getValorParametro());
							codigoTipoDescuentoGeneral = parametroDTO.getValorParametro();
						}
					}
					//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
					String codigoTipoDescuentoPagEfe = (String)session.getAttribute(CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO);
					if(session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
						if(codigoTipoDescuentoPagEfe == null){
							//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento pago en efectivo
							ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo", request);
							if(parametroDTO.getValorParametro()!=null){
								session.setAttribute(CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO, parametroDTO.getValorParametro());
								codigoTipoDescuentoPagEfe = parametroDTO.getValorParametro();
							}
						}
					}else{
						codigoTipoDescuentoPagEfe = null;
					}
					//variable que almacenar\u00E1 el primer codigo tipo descuento diferente encontrado
					String indiceTipoDescuento="";
					boolean hayMasDeUnMotivoSeleccionado = false;
					TipoDescuentoDTO tipoDescuentoDTO = null;
					//colecci\u00F3n para guardar los descuentos variables seleccionados
					Collection<DescuentoDTO> descuentoVariableCol = new ArrayList<DescuentoDTO>();
	
					//se iteran los descuentos seleccionados
					for(int i=0;i<opDescuentos.length;i++){
//						Duplex<Integer, Boolean> autorizaGerenteComercial = new Duplex<Integer,Boolean>(); //bandera para saber si el gerente comercial debe autorizar el descuento variable
						//se divide la expresi\u00F3n en arreglos de string, tomando como separador el caracter "-"
						String [] clave = opDescuentos[i].split(SEPARADOR_TOKEN);
						LogSISPE.getLog().info("subcadena[0]: {}",clave[0]);
						//se asigna el indice del tipo de descuento ubicado en el primer arreglo
						String indiceTipoDescuentoActual = clave[0];
						LogSISPE.getLog().info("anterior codTipoDescuento: {}",indiceTipoDescuento);
						LogSISPE.getLog().info("OPDESCUENTO[{}]: {}",i,opDescuentos[i]);
						//se verifica si fue seleccionado el tipo de descuento variable
						tipoDescuentoDTO = (TipoDescuentoDTO)tiposDescuento.get(Integer.parseInt(indiceTipoDescuentoActual));
						//Descuento Variable
						if(tipoDescuentoDTO.getNpTipoDescuentoVariable()!=null){	
							descVarSeleccionado = true;
						}
						//Mas de un motivo seleccionado para el  mismo tipo de descuento
						if(indiceTipoDescuento.equals(indiceTipoDescuentoActual) && tipoDescuentoDTO.getNpTipoDescuentoVariable()==null){						
							hayMasDeUnMotivoSeleccionado = true; //indica que se escogi\u00F3 mas de un motivo descuento para un tipo de descuento
							break;
						}
						if(!hayMasDeUnMotivoSeleccionado){						
							llaveDescuentoCol.add(opDescuentos[i]);
							indiceTipoDescuento = indiceTipoDescuentoActual;
							
							//se verifica si el tipo de descuento general fue seleccionado
							if(tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoGeneral)){
								descGenSeleccionado = true;
							}
							//jmena se verifica si el tipo de descuento pago en efectivo
							if(codigoTipoDescuentoPagEfe != null && tipoDescuentoDTO.getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe)){
								descPagEfeSeleccionado = true;
							}
							//Descuento Variable
							if(tipoDescuentoDTO.getNpTipoDescuentoVariable()!=null){
								//se obtiene el valor de la caja de texto con el porcentaje de descuento
								Integer indice = null;
								Integer indiceMp = null;
								for(int j=0;j<clave.length;j++){
									if(clave[j].contains(INDICE_DESCUENTO_GENERAL)){
										indice = new Integer(clave[j].substring(3,clave[j].length()-1));
										indiceMp=new Integer(clave[j].substring(clave[j].length()-1));
									}
								}							
								if(indice!=null){
									try{
										//se obtiene el porcentaje m\u00E1ximo de descuento variable
										valor = 0D;
										maxPorcDescVariable = CotizacionReservacionUtil.obtenerPorcentajeMaximoDescuentoPorEstablecimiento(request);
										minPorcDescVariable = CotizacionReservacionUtil.obtenerPorcentajeMinimoDescuentoPorEstablecimiento(request);
										valor = porcentajeVarDescuento[indice][indiceMp];
										
										//para el caso de gerente comercial
										if(maxPorcDescVariable != null && valor >= minPorcDescVariable && valor <= maxPorcDescVariable){
											//se obtiene los detalles del descuento
											for(DescuentoDTO descuentoDTO : tipoDescuentoDTO.getDetalleDescuentos()){
												//se determina cual fue el descuento seleccionado en base al Motivo: MONTO o CANTIDAD
												//el c\u00F3digo del motivo est\u00E1 en la tercera posici\u00F3n del arreglo clave: clave[2]
												if(clave[2].equals(CODIGO_MOTIVO_DESCUENTO.concat(descuentoDTO.getCodigoMotivoDescuento()))){
													descuentoDTO.setPorcentajeDescuento(Math.floor(valor * 100) / 100);
													descuentoVariableCol.add(descuentoDTO);
													descuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
													descuentoDTO.getTipoDescuentoDTO().setId(new TipoDescuentoID());
												}
											}
										}else{
											errorPorcentajeVariable = true;
										}
									}catch(Exception ex){
										errorPorcentajeVariable = true;
									}
								}else{
									errorPorcentajeVariable = true;
								}
							}
						}
					}
					
					//se realizan las validaciones primero
					if(hayMasDeUnMotivoSeleccionado && tipoDescuentoDTO!=null){
						errors.add("tiposDescuentos",new ActionMessage("errors.motivoDescuento.masDeUno",tipoDescuentoDTO.getDescripcionTipoDescuento()));
					}else if(descGenSeleccionado && descVarSeleccionado && descPagEfeSeleccionado){
						//este mensaje se genera cuando fue seleccionado el descuento variable y general y pago en efectivo
						errors.add("descuentoVarGenEfe", new ActionMessage("errors.descuentosExcluyentesOpcion1"));
					}
//					else if(descGenSeleccionado && descVarSeleccionado){
//						//este mensaje se genera cuando fue seleccionado el descuento variable y general
//						errors.add("descuentoVarGen", new ActionMessage("errors.descuentosExcluyentes"));
//					}
					else if(descGenSeleccionado && descPagEfeSeleccionado){
						//este mensaje se genera cuando fue seleccionado el descuento general y pago en efectivo
						errors.add("descuentoGenEfe", new ActionMessage("errors.descuentosExcluyentesOpcion2"));
					}
					//se comenta esta linea para habilitar descuento variable y maxi navidad por peticion de HELEN
//					else if(descVarSeleccionado && descPagEfeSeleccionado){
//						//este mensaje se genera cuando fue seleccionado el descuento variable y pago en efectivo
//						errors.add("descuentoVarEfe", new ActionMessage("errors.descuentosExcluyentesOpcion3"));
//					}
					else if(errorPorcentajeVariable){
						if(maxPorcDescVariable != null){
							//este mensaje se genera cuando se aprobaron un porcentaje fuera de rango
							errors.add("descuentoVariable",new ActionMessage("errors.descuentoVariable.gerente.comercial", valor,minPorcDescVariable,maxPorcDescVariable));
						}else{
							//este mensaje se genera cuando se ingres\u00F3 un valor de porcentaje variable inv\u00E1lido
							errors.add("descuentoVariable",new ActionMessage("errors.descuentoVariable.sin.porcentaje"));
						}
	
					}else{
						//eliminar descuentos para volver a aplicar solo los seleccionados
						if(request.getSession().getAttribute(BORRAR_DESCUENTOS) != null && request.getSession().getAttribute(BORRAR_DESCUENTOS).equals("SI")
								&& request.getSession().getAttribute(EXISTEN_DESCUENTOS_VARIABLES) == null ){
							eliminarDescuentos(formulario, request, errors, exitos,	session, detallePedido);
							session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
						}
						LogSISPE.getLog().info("descVarSeleccionado: {}",descVarSeleccionado);					
						if(descVarSeleccionado && request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) == null ){
							LogSISPE.getLog().info("Va a abrir la ventana de autorizacion");
							session.setAttribute(POPUP_DESCUENTO_VARIABLE, "ok");
							formulario.setOpDescuentos(opDescuentos);
							session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
						}
						else{
							//Aplicar Descuentos Variables autorizados
							if(descVarSeleccionado && request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) != null ){	
								//Ubicando el comprador del pedido
								String [] clave = opDescuentos[0].split(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken"));																			
								Boolean encontroClasificacionDepartamento = Boolean.FALSE; 
								String codigoClasificacionPadre = "";
								String codigoAutorizador = "";
								for(int j=0;j<clave.length;j++){
									if(clave[j].contains(CODIGO_GERENTE_COMERCIAL)){
										codigoClasificacionPadre = AutorizacionesUtil.obtenerCodigoClasificacionPadreDeLlaveDescuento(opDescuentos[0]);
										codigoAutorizador = AutorizacionesUtil.obtenerCodigoAutorizadorDeLlaveDescuento(opDescuentos[0]);
										encontroClasificacionDepartamento = Boolean.TRUE;
										break;
									}
								}
								if(encontroClasificacionDepartamento){
									
									//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
//							  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.autorizar.descuentoVariable.stock", request);
//							  		String[] codigoParametroAutorizarLineaComercial = parametroDTO.getValorParametro().split(AutorizacionesUtil.SEPARADOR_COMA);
//							  		Long codigoAutorizarDescVar = Long.parseLong(codigoParametroAutorizarLineaComercial[0]);
									Long codigoAutorizarDescVar = AutorizacionesUtil.obtenerCodigoProcesoAutorizarDescVar(request);
									//se obtiene de session la coleccion de departamentos
									Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol = (Collection<ClasificacionDTO>) session.getAttribute(CotizarReservarAction.COL_DEPARTAMENTOS_PEDIDO);
									ClasificacionDTO clasificacionDTO = AutorizacionesUtil.obtenerClasificacionDTOPorCodigo(clasificacionDepartamentosPedidoCol, codigoClasificacionPadre, codigoAutorizador, request);
									
									ArrayList<DetallePedidoDTO> detallePedidoClasificacion = new ArrayList<DetallePedidoDTO>(); 
									Collection<String> codigosClasificacionesPAvosCol = (Collection<String>) request.getSession().getAttribute(CotizarReservarAction.CLASIFICACIONES_PAVOS_SELECCIONADOS_COL);
																		
									for(DetallePedidoDTO detallePedidoDTO: detallePedido){
										//Busca si la clasificacion del articulo del pedido coincide con las clasificaciones que administra el comprador del descuento variable
//										if(clasificacionDTO.getId().getCodigoClasificacion().equals(detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre())){
										if(StringUtils.isEmpty(detallePedidoDTO.getNpIdAutorizador())){
											FuncionarioDTO funcionarioAutorizador = AutorizacionesUtil.obtenerFuncionarioAutorizadorPorClasificacion(AutorizacionesUtil.obtenerCodigoClasificacion(detallePedidoDTO),
													AutorizacionesUtil.obtenerTipoMarca(detallePedidoDTO),request, codigoAutorizarDescVar, warnings);
											if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
												detallePedidoDTO.setNpIdAutorizador(funcionarioAutorizador.getUsuarioFuncionario());
											}
										}
										
										if(clasificacionDTO.getId().getCodigoClasificacion().equals(AutorizacionesUtil.obtenerCodigoDepartamento(detallePedidoDTO))){
																							
											//se verifica que no tenga descuento automatico de pavos
											Boolean tieneDescAutomatico = Boolean.FALSE;
											if(codigosClasificacionesPAvosCol != null && !codigosClasificacionesPAvosCol.isEmpty()){
												
												if(codigosClasificacionesPAvosCol.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){ 
													tieneDescAutomatico = Boolean.TRUE;		
													break;
												}
											}
											if(!tieneDescAutomatico){
												detallePedidoClasificacion.add(detallePedidoDTO);
											}
										}
									}
									
									if(CollectionUtils.isNotEmpty(detallePedidoClasificacion)){
										LogSISPE.getLog().info("va a aplicar descuentos Variables ");
										aplicarDescuento(descuentoVariableCol, llaveDescuentoCol, detallePedidoClasificacion, formulario, request, errors, warnings, exitos);
										//session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
										//Aplicar descuentos a los pedidos consolidados //TODO verificar el caso de descuentos variables
//										CotizacionReservacionUtil.aplicarDescuentosConsolidados(request, session,detallePedidoClasificacion,descuentoVariableCol,llaveDescuentoCol,errors);
									}else{
										errors.add("descuentoVariable",new ActionMessage("errors.descuentoVariable.clasificacion.articulo",clasificacionDTO.getDescripcionClasificacion()));
									}
								} else {
									//TODO verificar este mensaje
									errors.add("descuentoVariable",new ActionMessage("errors.descuentoVariable.comprador"));
								}
							}
							//Aplicar descuentos normales
							else{
								LogSISPE.getLog().info("va a aplicar descuentos");
								//se obtiene la configuracion de las autorizaciones
								CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, detallePedido);
								
								aplicarDescuento(descuentoVariableCol, llaveDescuentoCol, detallePedido, formulario, request, errors, warnings, exitos);
								verificarDescuentosCajaMayorista(request,warnings, session);
								session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
								//Aplicar descuentos a los pedidos consolidados
								//CotizacionReservacionUtil.aplicarDescuentosConsolidados(request, session,detallePedido,descuentoVariableCol,llaveDescuentoCol,errors);							
							}
						}
						
					}
				}else{
					infos.add("seleccion",new ActionMessage("errors.seleccion.requerido","un tipo de Descuento"));
				}
				
			
			}
		}else{
			infos.add("detalleVacio",new ActionMessage("errors.detalle.requerido"));
		}
	}

	private static void verificarDescuentosCajaMayorista(HttpServletRequest request, ActionMessages warnings,HttpSession session) {
		if(session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS_POPUP)!=null  ){
			if(session.getAttribute(CotizarReservarAction.COL_DESCUENTOS)==null){
				CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.descuentos.noAplicados", "errorDescuentos", null, warnings);
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS_POPUP);
			}else{
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
				final String descuentoPorCajas =MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja");
				
				if(CollectionUtils.select(colDescuentoEstadoPedidoDTO, new Predicate() {
					public boolean evaluate(Object arg0) {
						DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO=(DescuentoEstadoPedidoDTO)arg0;
						return  descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(descuentoPorCajas);
					}
				}).size()==0){
					CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.descuentos.noAplicados", "errorDescuentos", null, warnings);	
					session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS_POPUP);
				}
			}
		}
		
		if(session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA_POPUP)!=null ){
			if(session.getAttribute(CotizarReservarAction.COL_DESCUENTOS)==null){
				CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.descuentos.noAplicados", "errorDescuentos", null, warnings);
				session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA_POPUP);
			}else{
				Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
				final String descuentoMayorista = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista");
				if(CollectionUtils.select(colDescuentoEstadoPedidoDTO, new Predicate() {
					public boolean evaluate(Object arg0) {
						DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO=(DescuentoEstadoPedidoDTO)arg0;
						return   descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(descuentoMayorista);
					}
				}).size()==0){
					CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.descuentos.noAplicados", "errorDescuentos", null, warnings);
					session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA_POPUP);
				}
			}
		}
	}

	/**
	 * @param formulario
	 * @param request
	 * @param errors
	 * @param exitos
	 * @param session
	 * @param detallePedido
	 * @throws SISPEException
	 * @throws Exception
	 */
	public static void eliminarDescuentos(CotizarReservarForm formulario, HttpServletRequest request, ActionMessages errors, 
			ActionMessages exitos, HttpSession session,	ArrayList<DetallePedidoDTO> detallePedido) throws SISPEException, Exception {
		
		//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
		session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
		
		session.removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
		session.removeAttribute(DES_MAX_NAV_EMP);
		session.removeAttribute(NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
		session.removeAttribute(MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI);
		session.removeAttribute(VALOR_BONO_COMPROBANTE_MAXI);
		session.removeAttribute(MONTO_CALCULADO_COMPROBANTE_MAXI);
		formulario.setCheckPagoEfectivo(null);
		session.removeAttribute(COL_DESCUENTOS);
		session.removeAttribute(COL_DESC_SELECCIONADOS);
		session.removeAttribute(COL_DESC_VARIABLES);
		//session.removeAttribute("ec.com.smx.sic.sispe.pedido.descuentoSinActualizar");
		//se inicializan las variables de los descuentos
		session.setAttribute(DESCUENTO_TOTAL,Double.valueOf(0));
		session.setAttribute(PORCENTAJE_TOT_DESCUENTO,Double.valueOf(0));
		formulario.setDescuentoTotal(Double.valueOf(0));

		//llamada al m\u00E9todo que quita los descuentos
		SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido,null, null);
//		exitos.add("sinDescuentos",new ActionMessage("message.sinDescuentos"));
		//eliminar los detalles que no corresponden al pedido
		List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
		VistaPedidoDTO vistaPedidoActual= (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		if(vistaPedidoActual!=null){
			for (Iterator<DetallePedidoDTO> it = detallePedido.iterator(); it.hasNext();){
				Object objeto = it.next();
				DetallePedidoDTO detallePedidoDTO = null;
				if(objeto instanceof DetallePedidoDTO){
					detallePedidoDTO = (DetallePedidoDTO)objeto;
				}
				if(!vistaPedidoActual.getId().getCodigoPedido().equals(detallePedidoDTO.getId().getCodigoPedido())){
					detallesEliminar.add(detallePedidoDTO);
				}
				//Eliminar descuentos por caja y mayoristas
				CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, true,false);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoCajaReceta(null);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoMayoristaReceta(null);
			}
			detallePedido.removeAll(detallesEliminar);
		}
		else{
			//Eliminar descuentos por caja y mayoristas
			session.setAttribute("ec.com.smx.sic.sispe.sinDescuentosCajasMayorista","SI");	
			for (DetallePedidoDTO detPed: detallePedido){
				//se recalcula el valor total del canasto en algunos casos
				CotizacionReservacionUtil.recalcularPrecioReceta(detPed, request);
				CotizacionReservacionUtil.calcularValoresDetalle(detPed, request, true,false);
				detPed.getEstadoDetallePedidoDTO().setNpValorDescuentoCajaReceta(null);
				detPed.getEstadoDetallePedidoDTO().setNpValorDescuentoMayoristaReceta(null);
			}
		}
		//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
		session.setAttribute("ec.com.smx.sic.sispe.sinDescuentos","SI");
		//CotizacionReservacionUtil.aplicarDescuentosConsolidados(request, session,detallePedido,null,null,errors);
		//ELiminar autorizaciones por descuento variable
		ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession()
				.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
		
//		if(colaAutorizaciones!=null && !colaAutorizaciones.isEmpty()){	
			if(formulario.getOpSinDescuentos() != null){
				//se guardan los detallesEstadoPedidoAutorizacionDTO que van a ser eliminados para cambiar su estado a inactivo
				if(session.getAttribute(ES_PEDIDO_CONSOLIDADO) != null && (Boolean)session.getAttribute(ES_PEDIDO_CONSOLIDADO).equals(Boolean.TRUE)){
					Collection<DetallePedidoDTO> detallePedidoConsolidado = (Collection<DetallePedidoDTO>) session.getAttribute(DETALLES_PEDIDOS_CONSOLIDADOS);
//					//se obtienen las autorizaciones inactivas
//					CotizacionReservacionUtil.obtenerAutorizacionesInactivas(request, detallePedidoConsolidado, Boolean.FALSE);
					//se elimina la configuracion de los detalles consolidados
					session.removeAttribute(CotizarReservarAction.DETALLES_CONSOLIDADOS_ACTUALES);
					//se asigna las autorizaciones sin autorizaciones
					ConsolidarAction.asignarAutorizacionesDetallesConsolidados(request, detallePedidoConsolidado);
					//se obtiene la contiguracion sin autorizaciones
					CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, detallePedidoConsolidado);
				}
				else{
					//se obtienen las autorizaciones inactivas
					CotizacionReservacionUtil.obtenerAutorizacionesInactivas(request, detallePedido, Boolean.FALSE);
				}
				AutorizacionesUtil.eliminarAutorizacionDescVar(colaAutorizaciones,detallePedido,request, Boolean.TRUE);
				request.getSession().removeAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
				request.getSession().removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);		
				request.getSession().removeAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE);
				CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
			}
			else{
				AutorizacionesUtil.eliminarAutorizacionDescVar(colaAutorizaciones,detallePedido,request, Boolean.FALSE);
				//se verifica si no hay descuentos variables seleccionados para borrar las autorizaciones variables
//				Boolean hayAutorizacionVariable = Boolean.FALSE;
				String[] opDescuentos = formulario.getOpDescuentos();
				if(opDescuentos != null && opDescuentos.length > 0){
//					for(int i=0; i<opDescuentos.length; i++){
//						if(opDescuentos[i] != null && (opDescuentos[i].contains(CODIGO_ADMINISTRADOR_LOCAL) || opDescuentos[i].contains(CODIGO_GERENTE_COMERCIAL))){
//							hayAutorizacionVariable = Boolean.TRUE;
//							break;
//						}
//					}


//					if(!hayAutorizacionVariable)
						AutorizacionesUtil.eliminarAutorizacionesNoSeleccionadas(detallePedido, request, opDescuentos);
				}
			}
//		}			
		formulario.setOpDescuentos(new String[0]);
		session.removeAttribute(BORRAR_DESCUENTOS);
		formulario.setPorcentajeVarDescuento(new Double[0][0]);
		
		//se elimina de sesion el parametro
		session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		
	}		
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String pasarAEntregas(HttpServletRequest request, CotizarReservarForm formulario)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		if(session.getAttribute(FECHA_ENTREGA)==null){
			session.setAttribute(FECHA_ENTREGA,formulario.getFechaEntrega());
		}
		//se verifica si el usuario logeado no es de un local
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			if(session.getAttribute(CrearReservacionAction.INGRESA_DIRECTAMENTE_RESERVAR)==null
					&& session.getAttribute(CrearRecotizacionAction.INGRESA_DIRECTAMENTE_RECOTIZAR) == null){
				//se crea una variable request para almacanar la descripci\u00F3n del local de or\u00EDgen
				//se obtienen los datos del local
				VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
				if(vistaLocalDTO!=null){
					formulario.setLocalResponsable(vistaLocalDTO.getId().getCodigoLocal() +" - "+ vistaLocalDTO.getNombreLocal());
				}
			}
		}

		return "entregaCalendario";
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpCambiosPedido(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Informaci\u00F3n");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'ocultarPopUp=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/consolidarPedido/popUpConfirmacionCambios.jsp");
		//popUp.setPreguntaVentana("Ud. realiz\u00F3 una modificaci\u00F3n en el pedido, Presione el boton guardar para no perder los cambios");
		popUp.setAncho(42D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * Construye la informaci&oacute;n de la ventana pregunta archivo beneficiario
	 * @param request
	 * @return
	 */
	private void instanciarPopUpPreguntaBeneficiario(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Beneficiario");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'aceptarArchBene=ok', popWait:true, evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarArchBene=ok', popWait:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		//popUp.setPreguntaVentana("\u00BFDesea adjuntar un archivo del beneficiario al pedido?");
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/confirmarArchivoBeneficiario.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}	
	
	private void instanciarPopUpPreguntaConsolidacion(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'aceptarSalirCon=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarCon=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setPreguntaVentana("\u00BFEsta seguro de cancelar la consolidaci\u00F3n?. S\u00ED presiona el boton (Si) se perder\u00E1n todos los cambios realizados.");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpSeleccionDescuentos(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Selecci\u00F3n de descuentos");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['div_pagina','mensajes','pregunta'], {parameters: 'aceptarDescuentos=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarDescuentos=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		//popUp.setPreguntaVentana("\u00BFDesea adjuntar un archivo del beneficiario al pedido?");
		popUp.setContenidoVentana("servicioCliente/consolidarPedido/confirmarDescuentosConsolidados.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpEntregaDomicilioBodega(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Entregas a domicilio bodega");
		//popUp.setEtiquetaBotonOK("Si");
		//popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());		
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/ventanaEntregasDomicilioBodega.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpAvisoResponsable(HttpServletRequest request, String tipoMensaje)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		session.removeAttribute(TIPO_MENSAJE_VALIDACION);
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Entidad responsable");
		popUp.setAncho(70D);
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("realizarEnvio('siCamEntResponsable')");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarCambioEntidad=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());		
		if(tipoMensaje.equals("ok1")){
			popUp.setContenidoVentana("servicioCliente/confirmarReservacion/ventanaConfirmacionEntidadRes.jsp");
		}else if(tipoMensaje.equals("ok2")){
			session.setAttribute(TIPO_MENSAJE_VALIDACION,"ok");
			popUp.setContenidoVentana("servicioCliente/confirmarReservacion/ventanaConfirmacionEntidadRes.jsp");
		}
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	  /**
	   * Inicializa los datos para el ordenamiento
	   * @param request
	   */
	  private void inicializarDatosOrdenamiento(HttpServletRequest request){
	  	
	    //se incializan los datos de la tabla
	    String[][] datosTabla = {
	      {"id.codigoPedido", "C\u00F3digo del pedido", null},
	      {"llaveContratoPOS", "Llave del contrato", null},
	      {"fechaInicialEstado", "Fecha de pedido", null},
//	      {"nombreContacto", "Nombre del Cliente", null},
	      {"contactoEmpresa", "Nombre del cliente", null},
	      {"totalPedido", "Valor total", null},
	      {"abonoPedido", "Valor abonado", null},
	      {"descripcionEstado", "Estado", null},
	      {"etapaEstadoActual", "Etapa del estado", null},
	      {"estadoActual", "Estado actual", null}
	    };
	    //se guardan los datos en sesi\u00F3n
	    request.getSession().setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
	    //se inicializa los datos de la culumna ordenada 
	    request.getSession().setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {"C\u00F3digo del Pedido", "Ascendente"});
	  }
	  
	  /**
		 * 
		 * @param request
		 * @return
		 */
		private void instanciarPopUpAutorizacion(HttpServletRequest request, String opcion, String mensaje)throws Exception{
			//se crea la ventana que pide la autorizacion
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Consolidar pedidos");
			popUp.setEtiquetaBotonOK("Si");
			popUp.setEtiquetaBotonCANCEL("No");
			
			popUp.setMensajeVentana("Para realizar una consolidaci&oacute;n se requiere la autorizaci&oacute;n del ADMINISTRADOR");
			//popUp.setValorOK("requestAjaxByNameForm('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true},'cotizarRecotizarReservarForm');ocultarModal();");
									
			popUp.setAccionEnvioCerrar("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POSsalida = "desplegarConsolidarPedido";
			popUp.setPreguntaVentana("\u00BFDesea aplicar la autorizaci&oacute;n ingresada?");
			popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);

			//se guarda la ventana
			if(opcion.equals("1")){
				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true});");
				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true});");
				request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);			
			}else{
				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true});");
				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true});");
				popUp.setMensajeErrorVentana(mensaje);
				request.getSession().setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
			}
			
			request.getSession().setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			request.getSession().setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
			popUp=null;
		}
		
		/**
		 * 
		 * @param request
		 * @return
		 */
		public static void instanciarPopUpAutorizacionEliminar(HttpServletRequest request, String opcion, String mensaje)throws Exception{
			//se crea la ventana que pide la autorizacion
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Eliminar pedidos consolidados");
			popUp.setEtiquetaBotonOK("Si");
			popUp.setEtiquetaBotonCANCEL("No");
			
			popUp.setMensajeVentana("Para eliminar uno o m&aacute;s pedidos consolidados se requiere la autorizaci&oacute;n del ADMINISTRADOR");
			//popUp.setValorOK("requestAjaxByNameForm('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true},'cotizarRecotizarReservarForm');ocultarModal();");
									
			popUp.setAccionEnvioCerrar("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POSsalida = "desplegarConsolidarPedido";
			popUp.setPreguntaVentana("\u00BFDesea aplicar la autorizaci&oacute;n ingresada?");
			popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);

			//se guarda la ventana
			if(opcion.equals("1")){
				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacionEliminar=ok',evalScripts: true});");
				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacionEliminar=ok',evalScripts: true});");
				request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);			
			}else{
				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacionEliminar=ok',evalScripts: true});");
				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacionEliminar=ok',evalScripts: true});");
				popUp.setMensajeErrorVentana(mensaje);
				request.getSession().setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
			}
			
			request.getSession().setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
			request.getSession().setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
			popUp=null;
		}

		/**
		 * Remover las variables de session
		 * @param session
		 */
		private void removerVariablesSession(HttpSession session){
			session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA, "ok");
			session.removeAttribute(SessionManagerSISPE.POPUP);
			session.removeAttribute("ec.com.smx.sic.sispe.estadosPago");
			session.removeAttribute(COL_DESCUENTOS);
			session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS_PEDIDO_GENERAL);
			session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS);
			session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS);
			session.removeAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios");
			session.removeAttribute(PEDIDO_EN_PROCESO);
			session.removeAttribute(VISTA_PEDIDOS_CONSOLIDADOS);
			session.removeAttribute(DETALLES_PEDIDOS_CONSOLIDADOS);
			session.removeAttribute(DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS);
			session.removeAttribute(PEDIDOS_CONSOLIDADOS_ELIMINADOS);
			session.removeAttribute(DETALLES_PEDIDOS_CONSOLIDADOS_ELIMINADOS);
			session.removeAttribute(PEDIDOS_CONSOLIDADOS);
		}
		
		/***
		 * Construye la informaci\u00F3n de la ventana envio mail articulos perecibles
		 * @param request
		 */
		private void instanciarPopUpMailArtPerecibles(HttpServletRequest request){			
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Confirmaci\u00F3n compras");
			popUp.setMensajeVentana("La reservaci\u00F3n tiene art\u00EDculos perecibles que fueron solicitados al CD. " +
					"Un mail ser\u00E1 enviado autom\u00E1ticamente a los usuarios responsables en el Area de Compras.");
			popUp.setFormaBotones(UtilPopUp.OK);
			//popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', evalScripts:true});ocultarModal();");
			//popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'subirArchivo=ok', evalScripts:true});ocultarModal();");
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la advertencia
			request.setAttribute(SessionManagerSISPE.POPUP, popUp);			
			popUp = null;
		}
		
		private void instanciarPopUpAutModReservaBodega(HttpServletRequest request){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Autorizaci\u00F3n - modificar reserva");
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setEtiquetaBotonCANCEL("Cancelar");
			popUp.setMensajeVentana("Existen cambios en los art\u00EDculos solicitados al CD, antes de continuar debe ingresar un n\u00FAmero de autorizaci\u00F3n generado por el administrador de Bodega");
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarAutorizacion=ok', evalScripts: true});ocultarModal();");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'aceptarAutorizacionModRes=ok', evalScripts: true});ocultarModal();");
			//popUp.setValorOK("realizarEnvio('regReservacion');");
			popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			popUp = null;
		}
		
		private void instanciarPopUpModReservaPedPTO(HttpServletRequest request){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Modificar reserva");
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setEtiquetaBotonCANCEL("Cancelar");
			popUp.setMensajeVentana("<b>Se agregaron nuevos art\u00EDculos a la reserva actual</b>");
			popUp.setContenidoVentana("servicioCliente/confirmarReservacion/confirmarAccionModReserva.jsp");
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarModificarReserva=ok', evalScripts: true});ocultarModal();");														
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['div_pagina','mensajesModReserva'], {parameters: 'verPopUpDetallesCambio=ok', evalScripts: true});ocultarModal();");
			//popUp.setValorOK("realizarEnvio('regReservacion');");
			popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);							
			popUp.setTope(90d);
			popUp.setAncho(56D);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);			
			popUp = null;
		}
		
		/**
		 * Instanciar el pop indicando si existe una disminucion en el monto en el proceso de modificar una reserva
		 * para estadoPedido PTO y LQD
		 * @param request
		 * @param valorDevolucion
		 */
		private void instanciarPopUpDisminusionReserva(HttpServletRequest request){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			StringBuilder mensaje = new StringBuilder();
			String valorDevolucion = null;
			
			PedidoDTO pedidoDTO = null;
			String codigoEstadoPagado = null;
			pedidoDTO = (PedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
			
			if(session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO)!=null)
				valorDevolucion = session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO).toString();			
			
			mensaje.append("El total del pedido disminuy&oacute;, hay una devoluci&oacute;n de <b>$".toString()
				.concat(Util.roundDoubleMath(Double.parseDouble(valorDevolucion), NUMERO_DECIMALES).toString()));
			
			if(pedidoDTO.getEstadoPedidoDTO().getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO)){
				codigoEstadoPagado =  MessagesWebSISPE.getString("label.codigoEstadoLQD");
			}else if(pedidoDTO.getEstadoPedidoDTO().getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL)){
				codigoEstadoPagado = MessagesWebSISPE.getString("label.codigoEstadoPTO");
			}
			
			popUp.setTituloVentana("Modificar reserva ".concat(codigoEstadoPagado));
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setEtiquetaBotonCANCEL("Cancelar");
			popUp.setMensajeVentana(mensaje.toString());			
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'modificarReservaDismi=ok', evalScripts: true});ocultarModal();");			
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarModificarReserva=ok', evalScripts: true});ocultarModal();");
			popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);							
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			
			pedidoDTO = null;
			popUp = null;
			mensaje = null;
			valorDevolucion = null;
			
		}
		
		/**
		 * 
		 * @param request
		 * @param beanSession
		 * @param codigoPedido
		 * @param reservaConDescuentos si la nueva reserva se va ha generar con los descuentos actuales del pedido
		 */
		private void instanciarPopUpNuevaReservaLQD(HttpServletRequest request, BeanSession beanSession, String codigoPedido){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();						
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Modificar reserva");
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setEtiquetaBotonCANCEL("Cancelar");
			popUp.setMensajeVentana("Los cambios realizados en el pedido No. <b>".concat(codigoPedido.concat("</b> son:")));
			popUp.setContenidoVentana("servicioCliente/confirmarReservacion/detalleModificacionReserva.jsp");			
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarModificarReservaLQD=ok', evalScripts: true});ocultarModal();");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'modificarReserva=ok', evalScripts: true});ocultarModal();");
			popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			popUp.setTope(10D);
			popUp.setAncho(60D);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			
			//Crear los tabs para la diferecia del detalle del pedido
			beanSession.setPaginaTab(construirTabsDiferenciasDetallePed(request));
			
			popUp = null;
		}
		
		/**
		 * PopUp cuando se aumentaron y disminuyeron productos en una reserva con estado LQD
		 * @param request
		 */
		private void instanciarPopUpAumDisReserva(HttpServletRequest request){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			String mensaje = null;
			
			mensaje = "Usted aument&oacute; y quit&oacute; art&iacute;culos del pedido, se va ha generar una nueva reserva por el nuevo detalle del pedido";
			
			popUp.setTituloVentana("Modificar reserva");
			popUp.setEtiquetaBotonOK("Aceptar");
			popUp.setEtiquetaBotonCANCEL("Cancelar");
			popUp.setMensajeVentana(mensaje);			
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'modificarReservaDismAum=ok', evalScripts: true});ocultarModal();");			
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarModificarReserva=ok', evalScripts: true});ocultarModal();");
			popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);							
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
						
			popUp = null;
			mensaje = null;
		}
		
		/**
		 * Instanciar popUp autorizaci&oacute;n Problemas Stock (pavos/pollos/canastas)
		 * @param request
		 */
		private void instanciarPopAutorizacionStockArtEsp(HttpServletRequest request){
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			//se crea la ventana que pide la autorizacion
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Problemas stock (pavos/pollos/canastas)");
			popUp.setEtiquetaBotonOK("Si");
			popUp.setEtiquetaBotonCANCEL("No");
			
			popUp.setMensajeVentana("Art\u00EDculos(pavos/pollos/canastas) sin existencia stock. Antes de continuar debe ingresar un n\u00FAmero de autorizaci\u00F3n generado por el Gerente Comercial.");
			popUp.setValorOK("requestAjax('crearCotizacion.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok&&autorizaGerCom1=ok&&opcionBotton=3',evalScripts: true});ocultarModal();");
			session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
									
			popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarAutorizacion=ok'});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarAutorizacion=ok'});ocultarModal();");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POS
			popUp.setPreguntaVentana("\u00BFDesea aplicar autorizacion ingresada?");
			popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
			popUp.setTope(70d);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);

			//se guarda la ventana
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			popUp = null;
		}
		
		
		/**
		 * Actualizar la reserva de un pedido, si solo se modifica el contacto
		 * @param formulario
		 * @param request
		 * @param infos
		 * @param errors
		 * @param warnings
		 * @param exitos
		 * @param pedidoDTO
		 * @param salidaOUT
		 * @param cambiosReserva 
		 * @return
		 * @throws SISPEException
		 * @throws Exception
		 */
		private String actualizarReservaMC(CotizarReservarForm formulario, HttpServletRequest request,
				ActionMessages infos, ActionMessages errors, ActionMessages warnings, ActionMessages exitos, 
				String salidaOUTParam, BeanSession beanSession, String cambiosReserva) throws SISPEException, Exception{
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			PedidoDTO pedidoActualDTO = null;
			Collection<DetallePedidoDTO> detalleReservacion = null;
			String salidaOUT = salidaOUTParam;
			try {				
				//se obtiene el pedido a reservar ec.com.smx.sic.sispe.pedidoReservar
				PedidoDTO pedidoDTO = (PedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
				
				detalleReservacion = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);	
				detalleReservacion = ColeccionesUtil.sort(detalleReservacion, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
				
				//se actualizan las variables de sesi\u00F3n
				session.setAttribute(DETALLE_PEDIDO, detalleReservacion);
				session.setAttribute(COL_CODIGOS_ARTICULOS, pedidoDTO.getNpCodigosArticulos());
				
				pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().transActualizarReservacion(pedidoDTO, 
						SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail());
				
				//consulta para crear la vista y obtener las configuraciones					
				VistaPedidoDTO consultaVistaPedidoDTO =  new VistaPedidoDTO();
				List<VistaPedidoDTO> pedidos = null;
				consultaVistaPedidoDTO.getId().setCodigoCompania(pedidoActualDTO.getId().getCodigoCompania());
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoActualDTO.getId().getCodigoAreaTrabajo());
				consultaVistaPedidoDTO.getId().setCodigoPedido(pedidoActualDTO.getId().getCodigoPedido());
				consultaVistaPedidoDTO.getId().setCodigoEstado(pedidoActualDTO.getEstadoPedidoDTO().getId().getCodigoEstado());
				consultaVistaPedidoDTO.getId().setSecuencialEstadoPedido(pedidoActualDTO.getEstadoPedidoDTO().getId().getSecuencialEstadoPedido());
				pedidos = (List<VistaPedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
				if(CollectionUtils.isNotEmpty(pedidos)){
					consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(0);
					EstadoPedidoUtil.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
					if(consultaVistaPedidoDTO.getNumBonNavEmp()!=null){
						formulario.setNumBonNavEmp(consultaVistaPedidoDTO.getNumBonNavEmp().toString());
					}else{
						formulario.setNumBonNavEmp(null);
					}
					consultarEntregasConSICMER(session,consultaVistaPedidoDTO);
					DetalleEstadoPedidoAction.obtenerEntregas(session,consultaVistaPedidoDTO.getVistaDetallesPedidosReporte());
				}
				if(pedidoDTO.isNpImprimirConveniosSICMER()){
					session.setAttribute(IMPRIMIR_CONVENIOS_SICMER, pedidoDTO.isNpImprimirConveniosSICMER());
				}
				//valores necesarios para enviar los correos
				session.setAttribute(CAMBIOS_MODIFICACION_RESERVA, cambiosReserva);
				
				//se asigna la acci\u00F3n de reservaci\u00F3n en caso de que se realize la modificaci\u00F3n de una reserva
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"));
				
				//se almacenan varios datos en sesi\u00F3n para el control posterior
				session.setAttribute(PEDIDO_GENERADO, pedidoActualDTO);
				session.setAttribute(RESPONSABLE_RESERVACION,	pedidoDTO.getNpResponsablePedido());
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Reservaci\u00F3n realizada");
				//se elimina la variable de sesi\u00F3n que indica que la reservacion est\u00E1 en proceso
				session.removeAttribute(PEDIDO_EN_PROCESO);
				session.removeAttribute(RESPONSABLE_LOCAL);
				
				//indica que el registro finaliz\u00F3 con exito
				session.setAttribute(TRANSACCION_REALIZADA,"ok");
				
				//se envian las notificaciones a bodega
				EnvioMailUtil.enviarMailNotificacionBodega(request);
				
				beanSession.setPaginaTab(ContactoUtil.construirTabsResumenPedido(request, formulario));
				
				salidaOUT = "registro";
				
			} catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				errors.add("registrarReservacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la reservaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en CONFIRMAR, caso contrario haga clic en GUARDAR"));
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());
				errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));
				session.removeAttribute(TRANSACCION_REALIZADA);
				salidaOUT="desplegar";
			}
			
			return salidaOUT;
		}
		
		/**
		 * 
		 * @param formulario
		 * @param request
		 * @param infos
		 * @param errors
		 * @param warnings
		 * @param exitos
		 * @param salidaOut
		 * @param beanSession
		 * @param cambiosReserva 
		 * @return
		 * @throws SISPEException
		 * @throws Exception
		 */
		private String reservarPedido(CotizarReservarForm formulario, HttpServletRequest request,ActionMessages infos, 
				ActionMessages errors, ActionMessages warnings, ActionMessages exitos, String salidaOutParam, BeanSession beanSession, String cambiosReserva) 
						throws SISPEException, Exception{
			String salidaOut = salidaOutParam;
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();	
			//se obtiene el pedido a reservar
			PedidoDTO pedidoDTO = (PedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
			Collection<DetallePedidoDTO> detalleReservacion = null;			
			
			PedidoDTO pedidoActualDTO = null;
			
			//se obtiene el detalle del Pedido
			detalleReservacion = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
			
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			
			detalleReservacion = ColeccionesUtil.sort(detalleReservacion, ColeccionesUtil.ORDEN_ASC, "id.codigoArticulo");
			pedidoDTO.setNpCodigosArticulos(ColeccionesUtil.sort(pedidoDTO.getNpCodigosArticulos(), ColeccionesUtil.ORDEN_ASC));
			
			if (session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO)!=null) {
				Double valorDev = (Double)session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO);
				pedidoDTO.setNpSaldoFavorCliente(valorDev>0 ? valorDev:0D);
			}
			
			if(session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)!=null){
				pedidoDTO.setNpEntregasPedidoCol((Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO));
			}
			
			//se actualizan las variables de sesi\u00F3n
			session.setAttribute(DETALLE_PEDIDO, detalleReservacion);
			session.setAttribute(COL_CODIGOS_ARTICULOS, pedidoDTO.getNpCodigosArticulos());
			/*proceso que obtiene las variables de los pedidos consolidados*/
			Collection <DetallePedidoDTO> colDetallePedidoConsolidadoDTO=null;
			//setear el campo isNpTieneConsolidacion para determinar q el pedido es consolidado
			Collection<VistaPedidoDTO> visPedCol=formulario.getDatosConsolidados();
			if(visPedCol!=null && visPedCol.size()>0){
				Collection<PedidoDTO> pedidoColActualizado= (Collection<PedidoDTO>)session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
				colDetallePedidoConsolidadoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				LogSISPE.getLog().info("CodigoConsolidado: {}",formulario.getNumeroPedidoConsolidado());
				if(!formulario.getNumeroPedidoConsolidado().equals("CONSOLIDADO")){
					pedidoDTO.setCodigoConsolidado(formulario.getNumeroPedidoConsolidado());
				}
				pedidoDTO.setNpTieneConsolidacion(Boolean.TRUE);
				if(pedidoColActualizado==null){
					Collection<VistaPedidoDTO> pedidosConsolidados= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
					Collection<PedidoDTO> pedidoColDTO= new ArrayList<PedidoDTO>();
					for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
						PedidoDTO pedidoBuscar= new PedidoDTO();
						pedidoBuscar.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
						pedidoBuscar.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
						pedidoBuscar.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
						LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",visPedDTO.getId().getCodigoPedido());
						Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
						LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
						if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
							PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
							pedidoColDTO.add(pedActDTO);
						}
						pedidoDTO.setPedidosConsolidar(pedidoColDTO);	
					}
				}
				else{
					//eliminar pedido actual como consolidado, para que se registre el nuevo estado
					for(PedidoDTO pedidoConsolidado:pedidoColActualizado){
						if(pedidoConsolidado.getId().getCodigoPedido().equals(pedidoDTO.getId().getCodigoPedido())){
							pedidoColActualizado.remove(pedidoConsolidado);
							break;
						}
					}
					for(DetallePedidoDTO detPedActualDTO:detalleReservacion){
						for(DetallePedidoDTO detPedDTO:colDetallePedidoConsolidadoDTO){
							if(detPedDTO.getId().getCodigoArticulo().equals(detPedActualDTO.getId().getCodigoArticulo())&& 
								 detPedDTO.getId().getCodigoPedido().equals(pedidoDTO.getId().getCodigoPedido())){
								colDetallePedidoConsolidadoDTO.remove(detPedDTO);
								break;
							}
						}
					}

					pedidoDTO.setPedidosConsolidar(pedidoColActualizado);	
				}
			}
			/*FIN proceso que obtiene las variables de los pedidos consolidados*/
				
			LogSISPE.getLog().info("::::::::::::::: va a verificar si muestra popup de modificacion de reserva");
			//valida autorizacion modificaci\u00F3n de reserva
			if(session.getAttribute(CAMBIOS_SOLICITUD_CD_MOD_RES) != null){
				session.removeAttribute(CAMBIOS_SOLICITUD_CD_MOD_RES);
				LogSISPE.getLog().info("va a abrir la ventana de autorizacion para modificar la reserva");
				//instanciarPopUpAutModReservaBodega(request);					
				//saveErrors(request, errors);
				//se termina el m\u00E9todo //TODO wc
				//return mapping.findForward("desplegar");
			}
									
			try{
				//Verificar si hay autorizaciones Pendientes				
				//Se valida si tiene autorizaciones solicitadas
				if(AutorizacionesUtil.solicitarAutorizacion(MessagesWebSISPE.getString("accion.autorizaciones.registrar.reservacion"),request, formulario, exitos, warnings, infos, errors)){
					salidaOut = "desplegar";
					session.setAttribute(FORMULARIO_TEMPORAL, formulario);
					//Cambiar al tab de pedidos
					ContactoUtil.cambiarTabPedidos(beanSession);
					//return forward(salidaOut, mapping, request, errors, exitos, warnings, infos, beanSession);
					return salidaOut;
				}
				//se valida que todas las autorizaciones esten aprobadas
				else{
					Collection<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (Collection<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
					Boolean puedeReservar = Boolean.TRUE;
					if(colaAutorizaciones!=null && !colaAutorizaciones.isEmpty()){							
						for(EstadoPedidoAutorizacionDTO estadoPedAut : colaAutorizaciones ){
							//si alguna autorizacion de descuento variable no esta aprobada, no puede seguir con la reservacion
							if (!estadoPedAut.getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA) 
									&& estadoPedAut.getNpTipoAutorizacion() != null 
									&& estadoPedAut.getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
								puedeReservar = Boolean.FALSE;	
								break;
							}
						}
					}
					if(!puedeReservar){
						infos.add("AutorizacionesPendientes",new ActionMessage("errors.reservar.autorizaciones.pendientes"));
						return salidaOut;
					}
				}
				
				//se desactivan las autorizaciones que fueron eliminadas
				Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (Collection<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
				if(CollectionUtils.isNotEmpty(autorizacionesDesactivarCol)){
					pedidoDTO.setNpAutorizacionesDesactivarCol(autorizacionesDesactivarCol);
					AutorizacionesUtil.crearAutorizacionesFinalizarWorkflow(autorizacionesDesactivarCol, request);
				}
				
				//se setea las autorizaciones a finalizar del workflow
				Collection<String> autorizacionesFinalizarWorkflow = (Collection<String>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW); 
				if(CollectionUtils.isNotEmpty(autorizacionesFinalizarWorkflow)){
					pedidoDTO.setNpAutorizacionesWorkFlow(autorizacionesFinalizarWorkflow);
				}
				
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = (CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL);
				
				LogSISPE.getLog().info("**CALENDARIOCONFIGURACIONDIALOCAL REGISTRARRESERVACION** {}" , calendarioConfiguracionDiaLocalDTO);	
				LogSISPE.getLog().info("**CALENDARIOCONFIGURACIONDIALOCALAUX ** {}" , session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX));
				LogSISPE.getLog().info("**CALENDARIOCONFIGURACIONDIALOCALAUX1** {}" , session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1));
					
				LogSISPE.getLog().info(" Inicio: transRegistrarReservacion --------------------------------------------------------------------------------------------------------------------");
				//se registra el pedido en la base de datos y se retorna el objeto reservaci\u00F3n creado
				pedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				pedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				
				//Se verifica si tiene autorizaciones solicitadas
				AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES), pedidoDTO);
				
				//Se verifica si hay autorizaciones para actualizar
				AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTUALIZAR), pedidoDTO);
				
				//se validan los otros tipos de autorizaciones
				AutorizacionesUtil.agregarAutorizacionesNoRepetidasAlPedido((ArrayList<EstadoPedidoAutorizacionDTO>)session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES_STOCK), pedidoDTO);
				
				
				if(pedidoDTO.getNpModificarReserva()!=null && pedidoDTO.getNpModificarReserva().equals(estadoInactivo)){					
					pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(TIPO_RESERVACION_NUEVO);					
					pedidoDTO.setNpEsNuevaReserva(Boolean.TRUE);
				}
				
				//se crea un nuevo pedido cuando solo aumento productos y el cliente quiere una nueva factura por la diferencia 
				if (pedidoDTO.getNpCrearNuevoPedido()) {
					pedidoDTO.getEstadoPedidoDTO().setNpEstadoModificacionReserva(TIPO_RESERVACION_NUEVO);
					pedidoDTO.setNpEsNuevaReserva(Boolean.TRUE);
					pedidoDTO.setNpModificarReserva(ConstantesGenerales.ESTADO_ACTIVO);
					session.removeAttribute(CotizarReservarForm.DEVOLUCION_ABONO);
				}
				//VERIFICAR SI EXISTEN LICORES O NO EN EL PEDIDO
				pedidoDTO.getEstadoPedidoDTO().setTieneLicores(ConstantesGenerales.PEDIDO_SIN_LICORES); /*Valor por default*/
				for(DetallePedidoDTO detPedActualDTO:detalleReservacion){
					
					String respuesta = CotizacionReservacionUtil.verificarArticuloEsLicorPorClasificacion(request, detPedActualDTO);
					if (StringUtils.equals(respuesta, ConstantesGenerales.PEDIDO_CON_LICORES)) {
						pedidoDTO.getEstadoPedidoDTO().setTieneLicores(respuesta);
						break;
					}
					
					if (StringUtils.equals(respuesta, ConstantesGenerales.PEDIDO_CON_LICORES_CANASTOS)) {
						pedidoDTO.getEstadoPedidoDTO().setTieneLicores(respuesta);
					}
					
				}
				
				//solo si el pedido tiene codigoEstadoPagado = LQD

				if(pedidoDTO.getNpModificarReserva()!=estadoInactivo && pedidoDTO.getEstadoPedidoDTO().getCodigoEstadoPagado().equals(CODIGO_ESTADO_PAGADO_LIQUIDADO)){

					if(pedidoDTO.getNpTipoProceso().equals(TIPO_PROCESO_AUMENTAR_PRODUCTOS) || pedidoDTO.getNpTipoProceso().equals(TIPO_PROCESO_DISMINUIR_AUMENTAR_MONTO_PEDIDO)){
		
						pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().
								transRegistrarReservacionLQD(pedidoDTO, detalleReservacion, calendarioConfiguracionDiaLocalDTO,
									SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail(), colDetallePedidoConsolidadoDTO);
	
					} else{
	
						pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().
						transRegistrarReservacion(pedidoDTO, detalleReservacion, calendarioConfiguracionDiaLocalDTO,
						SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail(),colDetallePedidoConsolidadoDTO, pedidoDTO.getNpEsNuevaReserva());
	
					}

				} else {//

					pedidoActualDTO = SessionManagerSISPE.getServicioClienteServicio().
					transRegistrarReservacion(pedidoDTO, detalleReservacion, calendarioConfiguracionDiaLocalDTO,
					SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail(),colDetallePedidoConsolidadoDTO, pedidoDTO.getNpEsNuevaReserva());
				}
				//consulta para crear la vista y obtener las configuraciones					
				VistaPedidoDTO consultaVistaPedidoDTO =  new VistaPedidoDTO();
				List<VistaPedidoDTO> pedidos = null;
				consultaVistaPedidoDTO.getId().setCodigoCompania(pedidoDTO.getId().getCodigoCompania());
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoDTO.getId().getCodigoAreaTrabajo());
				consultaVistaPedidoDTO.getId().setCodigoPedido(pedidoDTO.getId().getCodigoPedido());
				consultaVistaPedidoDTO.getId().setCodigoEstado(pedidoDTO.getEstadoPedidoDTO().getId().getCodigoEstado());
				consultaVistaPedidoDTO.getId().setSecuencialEstadoPedido(pedidoDTO.getEstadoPedidoDTO().getId().getSecuencialEstadoPedido());
				pedidos = (List<VistaPedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
				if(CollectionUtils.isNotEmpty(pedidos)){
					consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(0);
					//para que haga los calculos restantes, para mostrar la vista
					consultaVistaPedidoDTO.setVistaDetallesPedidos(null);
					consultaVistaPedidoDTO.setVistaDetallesPedidosReporte(null);
					EstadoPedidoUtil.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
					if(consultaVistaPedidoDTO.getNumBonNavEmp()!=null){
						formulario.setNumBonNavEmp(consultaVistaPedidoDTO.getNumBonNavEmp().toString());
					}else{
						formulario.setNumBonNavEmp(null);
					}
					DetalleEstadoPedidoAction.obtenerEntregas(session,consultaVistaPedidoDTO.getVistaDetallesPedidosReporte());
					consultarEntregasConSICMER(session,consultaVistaPedidoDTO);
				}				
				
				if(pedidoDTO.isNpImprimirConveniosSICMER()){
					session.setAttribute(IMPRIMIR_CONVENIOS_SICMER, pedidoDTO.isNpImprimirConveniosSICMER());
				}
				//valores necesarios para enviar los correos
				session.setAttribute(CAMBIOS_MODIFICACION_RESERVA, cambiosReserva);
				pedidoActualDTO.setNpAutorizacionesWorkFlow(pedidoDTO.getNpAutorizacionesWorkFlow());
				pedidoActualDTO.setEstadoPedidoDTO(pedidoDTO.getEstadoPedidoDTO());
				LogSISPE.getLog().info(" Fin: transRegistrarReservacion --------------------------------------------------------------------------------------------------------------------");
					
				//se almacenan varios datos en sesi\u00F3n para el control posterior
				session.setAttribute(PEDIDO_GENERADO, pedidoActualDTO);
				session.setAttribute(RESPONSABLE_RESERVACION,	pedidoDTO.getNpResponsablePedido());
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Reservaci\u00F3n realizada");
				//se elimina la variable de sesi\u00F3n que indica que la reservacion est\u00E1 en proceso
				session.removeAttribute(PEDIDO_EN_PROCESO);
				session.removeAttribute(RESPONSABLE_LOCAL);
				session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
				session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW);
				session.removeAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL);
					
				////////////////////Envio de Mail Otro Local Entrega**********
				DetallePedidoDTO entrega = new DetallePedidoDTO();
				List<DetallePedidoDTO> colEntrega = (ArrayList<DetallePedidoDTO>)detalleReservacion;
				entrega= colEntrega.get(0);
				String codigoPedido = entrega.getId().getCodigoPedido();
				LogSISPE.getLog().info("Cod_Pedido->{}", codigoPedido);
				
//				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//				
//				//este metodo se paso al EnvioMailUtil para enviarlo despues de registrado la reservacion
//				SessionManagerSISPE.getServicioClienteServicio().transEnvioMailEntregaOtroLocal(codigoPedido,colDetallePedido, consultaVistaPedidoDTO.getLlaveContratoPOS());
				///////////////////////********************************************
					
				//////////////////////	/Proceso que verifica el envio de mails cuando hay entregas a domicilio y el Responsable es Bodega
					
				//obtiene par\u00E1metro Activacion de verificaci\u00F3n de mails.
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.validacionEnvioMailsPosiblesFuncionariosResponsables", request);
				Integer parametroActivacion = Integer.parseInt(parametroDTO.getValorParametro());
				boolean mostrarEnvioMail = false;
				String accionMail = "reserva";
				LogSISPE.getLog().info("Valor Parametro de Activacion->{}",parametroActivacion);
				if(parametroActivacion == Integer.parseInt(estadoActivo)){
					mostrarEnvioMail =  SessionManagerSISPE.getServicioClienteServicio().transEnvioMailResponsableDomicilioBodega(codigoPedido,accionMail);
					//verifico si se envio el mail o no
					LogSISPE.getLog().info("Valor del Boolean mostrarEnvioMail->{}",mostrarEnvioMail);
					if(mostrarEnvioMail){
//						LogSISPE.getLog().info("Entro en el if mostrar popup");
//						instanciarPopUpEntregaDomicilioBodega(request);
						infos.add("EntregaDomicilioBodega",new ActionMessage("info.entrega.domicilio.bodega"));
					}
				}else{
					LogSISPE.getLog().info("Valor del Parametro EnvioMailsFuncionariosEntregaDomicilio Desactivado");
				}
					
				//se verifica si existen art\u00EDculos especiales reservados en bodega para enviar el email
				//a los usuarios de compras
				if(session.getAttribute(EXISTEN_ESPECIALES_RESERVADOS)!=null){
					////se construye la informaci\u00F3n de la ventana envio mail articulos perecibles
					instanciarPopUpMailArtPerecibles(request);
						
				}else if(!mostrarEnvioMail && session.getAttribute(EXISTEN_ESPECIALES_RESERVADOS)==null){
					//verifica si existen entregas con sicmer para enviar un mail de alerta
					if(session.getAttribute(EXISTEN_ENTREGAS_SICMER)!=null){
						CotizacionReservacionUtil.instanciarPopUpMailEntregasSICMER(request,(session.getAttribute(IMPRIMIR_CONVENIOS_SICMER)!=null?Boolean.TRUE:Boolean.FALSE) && consultaVistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO));
						SessionManagerSISPE.getServicioClienteServicio().transEnviarNotificacionEntregasSICMER((VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.vistaPedido"));
					}else{
					LogSISPE.getLog().info("Entra a subirArchivo del Beneficiario");
					//se construye la ventana de pregunta archivo beneficiario
					instanciarPopUpPreguntaBeneficiario(request);
					}
				}
				//se asigna la acci\u00F3n de reservaci\u00F3n en caso de que se realize la modificaci\u00F3n de una reserva
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"));
					
				//mensaje en caso de devoluci\u00F3n
				if(session.getAttribute(CotizarReservarForm.DEVOLUCION_ABONO) != null){
					exitos.add("registroReservacion", new ActionMessage("exitos.modificacionReserva.abonoMayorATotal"));
					if(pedidoDTO.getNpTipoProceso()!=estadoInactivo && pedidoDTO.getNpTipoProceso().equals(ConstantesGenerales.TIPO_PROCESO_QUITAR_PRODUCTOS)){
						exitos.add("existeNotaCredito", new ActionMessage("exitos.modificacionReserva.notaCredito"));
					}
				}
				
				//indica que el registro finaliz\u00F3 con exito
				session.setAttribute(TRANSACCION_REALIZADA,"ok");
					
				beanSession.setPaginaTab(ContactoUtil.construirTabsResumenPedido(request, formulario));
				
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				
				//se termina el registro
				salidaOut="registro";
					
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				errors.add("registrarReservacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la reservaci\u00F3n, int\u00E9ntelo nuevamente haciendo clic en CONFIRMAR, caso contrario haga clic en GUARDAR"));
				LogSISPE.getLog().info("codigo error: {}",ex.getCodigoError());
				errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));
				session.removeAttribute(TRANSACCION_REALIZADA);
				throw new Exception(ex);
			}
			
//			OrdenCompraUtil.crearOrdenCompra(detalleReservacion, SessionManagerSISPE.getDefault().getDefaultCompanyId(), 
//					(String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));

		return salidaOut;
	}

		public static void consultarEntregasConSICMER(HttpSession session,
				VistaPedidoDTO consultaVistaPedidoDTO) {
			if(session.getAttribute(DetalleEstadoPedidoAction.RESUMEN_ENTREGAS)!=null){
				if(consultaVistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO)
						&& !consultaVistaPedidoDTO.getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ANULADO)){
					Collection<EntregaPedidoDTO> entregasPedido=(Collection<EntregaPedidoDTO>)session.getAttribute(DetalleEstadoPedidoAction.RESUMEN_ENTREGAS);
					if(CollectionUtils.isNotEmpty(entregasPedido)){
						Collection<ConvenioEntregaDomicilioDTO> convenios=new ArrayList<ConvenioEntregaDomicilioDTO>();
						for(EntregaPedidoDTO entrega:entregasPedido){
							if(CollectionUtils.isNotEmpty(entrega.getEntregaPedidoConvenioCol())){
								if(entrega.getEntregaPedidoConvenioCol().iterator().next().getEstadoInventario().equals(ConstantesGenerales.ESTADO_ACTIVO)){
									ConvenioEntregaDomicilioDTO convenio=new ConvenioEntregaDomicilioDTO(); 
									convenio.getId().setSecuencialConvenio(entrega.getEntregaPedidoConvenioCol().iterator().next().getSecuencialConvenio());
									convenio.getId().setCodigoCompania(entrega.getId().getCodigoCompania());
									convenio.setLocalDestinoDTO(new LocalDTO());
									convenio.setVendedorDTO(new PersonaDTO());
									convenio.setPersonaDTO(new PersonaDTO());
									convenio.setEmpresaDTO(new EmpresaDTO());
									convenio=SISPEFactory.getDataService().findUnique(convenio);
									ConvenioEntregaDomicilioDetalleDTO convenioDetalle=new ConvenioEntregaDomicilioDetalleDTO(); 
									convenioDetalle.getId().setCodigoCompania(convenio.getId().getCodigoCompania());
									convenioDetalle.getId().setSecuencialConvenio(convenio.getId().getSecuencialConvenio());
									ArticuloDTO articuloDTO=new ArticuloDTO();
									articuloDTO.setArtBitCodBarCol(new ArrayList<ArticuloBitacoraCodigoBarrasDTO>());
									ArticuloBitacoraCodigoBarrasDTO artBitCodBarDTO= new ArticuloBitacoraCodigoBarrasDTO();
									artBitCodBarDTO.setEstadoArticuloBitacora(SICConstantes.getInstancia().ESTADO_ACTIVO_NUMERICO);
									articuloDTO.getArtBitCodBarCol().add(artBitCodBarDTO);
									convenioDetalle.setArticuloDTO(articuloDTO);
									Set<ConvenioEntregaDomicilioDetalleDTO> setConEntDomDetDTO =new HashSet<ConvenioEntregaDomicilioDetalleDTO>();
									convenio.setConvenioEntregaDomicilioDetalleSet(setConEntDomDetDTO);
									setConEntDomDetDTO.addAll(SISPEFactory.getDataService().findObjects(convenioDetalle));
									convenios.add(convenio);
								}
							}
						}
						session.setAttribute(DetalleEstadoPedidoAction.CONVENIOS,convenios);
					}
				}
			}
		}

	/**
	 * Este m&eacute;todo crea los tab de las diferencias del detalle del pedido	
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsDiferenciasDetallePed(HttpServletRequest request) {
		// Objetos para construir los tabs
		request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
		request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
		PaginaTab tabsDiferenciasDetallePed = new PaginaTab("crearCotizacion", "deplegar", 0, 480, request);
		try {
			Tab tabPedidoNuevo = new Tab("Detalles del nuevo pedido", "crearCotizacion", "/servicioCliente/confirmarReservacion/tabPedidoNuevo.jsp", true);
			Tab tabPedidoAnterior = new Tab("Pedido anterior modificado","crearCotizacion","/servicioCliente/confirmarReservacion/tabPedidoAnterior.jsp",false);
			
			tabsDiferenciasDetallePed.addTab(tabPedidoNuevo);
			tabsDiferenciasDetallePed.addTab(tabPedidoAnterior);
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al crea los tabs para las diferencias del detalle del pedido",e);
			return null;
		}
		return tabsDiferenciasDetallePed;
	}
	
	/**
	 * Este m&eacute;todo selecciona el tab	
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabsDiferenciasDetallePed(BeanSession beanSession, int posicionTab) {
		if(posicionTab != -1){
			ArrayList<?> tabsDiferenciasDetallePed= beanSession.getPaginaTab().getTabs(); 
			for(int i=0;i<tabsDiferenciasDetallePed.size();i++){
				if(posicionTab==i){
					beanSession.getPaginaTab().getTab(i).setSeleccionado(true);
				}
				else{
					beanSession.getPaginaTab().getTab(i).setSeleccionado(false);
				}
			}
			
		}
	}
		
	public static Boolean validarAutorizadoresDescVariable(CotizarReservarForm formulario, HttpServletRequest request,ActionMessages infos, 
			ActionMessages errors, ActionMessages warnings, ActionMessages exitos) throws Exception{
		try{
			LogSISPE.getLog().info("ingresa al metodo : validarAutorizadoresDescVariable");
			
			request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, formulario.getOpDescuentos());
			request.getSession().setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, formulario.getPorcentajeVarDescuento());
			
			request.getSession().setAttribute(CotizarReservarAction.RESPALDO_OP_DESCUENTOS, formulario.getOpDescuentos());
			
			String[] opDescuento = formulario.getOpDescuentos();				
			Double[][] porcentajeVarDescuento = formulario.getPorcentajeVarDescuento();
			
			//se obtiene de session las clasificaciones de pavos
			Collection<String> codigosClasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_PAVOS);
			
			//si la coleccion esta vacia se inicializa
			if(CollectionUtils.isEmpty(codigosClasificacionesPavos)){
				CotizacionReservacionUtil.inicializarClasificacionesPavosCanastos(request);
				codigosClasificacionesPavos = (Collection<String>) request.getSession().getAttribute(CotizacionReservacionUtil.CLASIFICACIONES_PAVOS);
			}
					
			Collection<String> llavesDesVarPavos = new ArrayList<String>();
				
			int tamVector = 0;
			if(opDescuento != null){
				tamVector = opDescuento.length;
			}else{
				formulario.setOpSinDescuentos("ok");
			}
 
			Boolean gerenteComercial = Boolean.FALSE;
			Boolean descuentoGeneralSeleccionado = Boolean.FALSE;
				
			//banderas para los errores
			Boolean errorPorcentajeGerente = Boolean.FALSE;
			
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento general
			String codigoTipoDescuentoGeneral = (String)request.getSession().getAttribute(CODIGO_TIPO_DESCUENTO_GENERAL);
			if(codigoTipoDescuentoGeneral == null){
				//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento general
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoGeneral", request);
				if(parametroDTO.getValorParametro()!=null){
					request.getSession().setAttribute(CODIGO_TIPO_DESCUENTO_GENERAL, parametroDTO.getValorParametro());
					codigoTipoDescuentoGeneral = parametroDTO.getValorParametro();
				}
			}
			
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
			String codigoTipoDescuentoMarcaPropia = DescuentosUtil.obtenerCodigoTipoDescuentoMarcaPropia(request);
			
			//el usuario desea eliminar todos los descuentos
			if(formulario.getOpSinDescuentos() != null){
				request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
				request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA, "NO");
				request.getSession().removeAttribute(CotizarReservarAction.RESPALDO_OP_DESCUENTOS);
				request.getSession().removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
				//si es pedido consolidado se obtienen las autorizaciones que seran desactivadas
				if(request.getSession().getAttribute(ES_PEDIDO_CONSOLIDADO) != null && (Boolean)request.getSession().getAttribute(ES_PEDIDO_CONSOLIDADO)){
					CotizacionReservacionUtil.obtenerAutorizacionesInactivas(request, 
							(ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS), Boolean.FALSE);
				}
				//se borran las autorizaciones de los pedidos consolidados
				request.getSession().removeAttribute(CotizarReservarAction.DETALLES_CONSOLIDADOS_ACTUALES);
				request.getSession().setAttribute(ELIMINAR_DESCUENTOS_CONSOLIDADOS, Boolean.TRUE);
				eliminarDescuentos(formulario, request, errors, exitos, request.getSession(), (ArrayList<DetallePedidoDTO>) request.getSession().getAttribute(DETALLE_PEDIDO));
				request.getSession().removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);	
				request.getSession().removeAttribute(POPUP_DESCUENTO);
				request.getSession().removeAttribute(COL_DESC_SELECCIONADOS);
				request.getSession().removeAttribute(COL_DESCUENTOS);	
				request.getSession().removeAttribute(COL_DESCUENTOS_VARIABLES);
				request.getSession().removeAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
				request.getSession().removeAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE);
				
				//se elimina la bandera de aplicar descuentos marca propia
				request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA);
				return false;
			}
			//si existen datos
			 if(tamVector > 0 ){
				 
				String codigoClasificacionPadre;
				String[] clave;
				int indice;
				int indiceMp;
					
				Double maxPorcDescVariable = CotizacionReservacionUtil.obtenerPorcentajeMaximoDescuentoPorEstablecimiento(request);
				Double minPorcDescVariable = CotizacionReservacionUtil.obtenerPorcentajeMinimoDescuentoPorEstablecimiento(request);
				Double valor = 0D;
				Boolean existenDctoVariables = Boolean.FALSE;
				Boolean descuentoMarcaPropiaSeleccionado = Boolean.FALSE;
				//se recorre los vectores para verificar los descuentos seleccionados
//				for(int i=0; i<tamVector; i++){
				int i=0;
				while(i<opDescuento.length){
					//solo para el caso de descuentos variables
					if(opDescuento[i] != null &&  opDescuento[i].contains(CODIGO_GERENTE_COMERCIAL)){
						existenDctoVariables = Boolean.TRUE;
						clave = opDescuento[i].split(SEPARADOR_TOKEN);
						indice = Integer.parseInt(clave[clave.length-1].substring(3,clave[clave.length-1].length()-1));
						indiceMp = Integer.parseInt(clave[clave.length-1].substring(clave[clave.length-1].length()-1));
						
						//se verifica si es autorizador de pavos
						if(CollectionUtils.isNotEmpty(codigosClasificacionesPavos)){
							for(String codigoClasificacionActual : codigosClasificacionesPavos){
								
								codigoClasificacionPadre = "00"+codigoClasificacionActual.substring(0, 2);
								if(opDescuento[i].contains(CODIGO_GERENTE_COMERCIAL+codigoClasificacionPadre)){
									if(!llavesDesVarPavos.contains(opDescuento[i])){
										llavesDesVarPavos.add(opDescuento[i]);
									}
								}
							}
						}
							
						valor = porcentajeVarDescuento[indice][indiceMp];
						if(valor >= minPorcDescVariable && valor <= maxPorcDescVariable){
							gerenteComercial = Boolean.TRUE;									
						}
						else{ 
							errorPorcentajeGerente = Boolean.TRUE;
							porcentajeVarDescuento[indice][indiceMp]=0D;
							opDescuento=ArrayUtils.removeElement(opDescuento, opDescuento[i]);
						}
						//se verifica si el descuento general esta seleccionado
					}else if(opDescuento[i].contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoGeneral+SEPARADOR_TOKEN)){
						
						descuentoGeneralSeleccionado = Boolean.TRUE;
					}
					//se verifica si esta seleccionado el descuento MARCA PROPIA
					else if(opDescuento[i].contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia+SEPARADOR_TOKEN)){
						descuentoMarcaPropiaSeleccionado = Boolean.TRUE;
						request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA,ConstantesGenerales.ESTADO_SI);
					}
					i++;
				}
				
				request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuento);
				
				//quiere decir que el usuario deselecciono el descuento MARCA PROPIA
				if(!descuentoMarcaPropiaSeleccionado && request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA) != null){
					request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA,ConstantesGenerales.ESTADO_NO);
				}else if(descuentoMarcaPropiaSeleccionado){
					
					ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);

					//saco respaldo del detalle pedido por si no se puedo aplicar el descuento marca propia no se pierdan los descuentos variables
					DescuentosUtil.respaldarDetallesAutorizaciones(request, formulario);
					
					AutorizacionesUtil.eliminarAutorizacionesNoSeleccionadas(detallePedidoCol, request, opDescuento);

					//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
					opDescuento = DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE);
				
					formulario.setOpDescuentos(opDescuento);
					//si en opDescuentos no esta seleccionada la llave marca propia se muestra el mensaje
					Boolean existeLlaveMarcaPropia = Boolean.FALSE;
					if(opDescuento != null && opDescuento.length > 0){
						for(String opActual : opDescuento){
							if(StringUtils.isNotEmpty(opActual) && opActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia)){
								existeLlaveMarcaPropia = Boolean.TRUE;
								break;
							}
						}
					}
					if(!existeLlaveMarcaPropia){
						
						if(request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia") != null && request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") != null){
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							errors.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia.conCantidad", 
									request.getSession().getAttribute("ec.com.smx.sic.sispe.cantidad.marca.propia"),
									request.getSession().getAttribute("ec.com.smx.sic.sispe.porcentaje.pedido.marcaPropia"),
									(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") ,(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia")));
						}else{
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							errors.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia"));	
						}
						
						//si existen errores se sube a sesion la variable para que no pinte los errores en la pagina de cotizaciones 
						request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
						request.getSession().removeAttribute(DescuentosUtil.MENSAJE_NO_APLICA_MARCA_PROPIA);

						//se restauran los detalles y autorizaciones de descuento variable
						DescuentosUtil.restaurarDetallesAutorizaciones(request, formulario);
						
						return false;
					}
				}
				
				//se sube a sesion si existe descuento variable
				if(existenDctoVariables){
					request.getSession().setAttribute(EXISTEN_DESCUENTOS_VARIABLES, "SI");
				}else{
					request.getSession().removeAttribute(EXISTEN_DESCUENTOS_VARIABLES);
				}

				ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				
				buscarDetallesConDctoAutomaticos(detallePedidoCol,  request);
				Collection<String> codigosClasificacionesPAvosCol = (Collection<String>) request.getSession().getAttribute(CLASIFICACIONES_PAVOS_SELECCIONADOS_COL);
				
				//bandera para habilitar o no los descuentos variables de pavos y normales de pavos
				Boolean habilitarDescuentoCongelados = Boolean.TRUE; 
				if(CollectionUtils.isNotEmpty(llavesDesVarPavos) && CollectionUtils.isNotEmpty(codigosClasificacionesPAvosCol)){
						
					//se recorren las clasificaciones de pavos
					for(String clasificPavoSeleccionado: codigosClasificacionesPAvosCol){
						
						//se obtiene el autorizador de la clasificacion de pavos de descuento normal
						FuncionarioDTO funAutorizador = AutorizacionesUtil.obtenerFuncionarioAutorizadorPorClasificacion(clasificPavoSeleccionado,null,
								request, AutorizacionesUtil.obtenerCodigoProcesoAutorizarDescVar(request), warnings);
						
						//si existe el funcionario autorizador
						if(funAutorizador != null && StringUtils.isNotEmpty(funAutorizador.getUsuarioFuncionario())){
							//se recorren las llaves de descuentos varibles de pavos
							for(String llaveDescVarPavo : llavesDesVarPavos){
								
								//se obtiene la clasificacion y el autorizador de cada llave
								String codigoAutorizador = AutorizacionesUtil.obtenerCodigoAutorizadorDeLlaveDescuento(llaveDescVarPavo);
								
								//se compara si el autorizador de descuento normal y variable es el mismo
								if(funAutorizador.getUsuarioFuncionario().equals(codigoAutorizador)){
									habilitarDescuentoCongelados = Boolean.FALSE;
									LogSISPE.getLog().info("estan seleccionados descuentos normales y variables de la clasificacion "+clasificPavoSeleccionado
											+" con el mismo autorizador "+funAutorizador.getUsuarioFuncionario());
									break;
								}
							}
							if(!habilitarDescuentoCongelados){
								break;
							}
						}
					}
					
					if(!habilitarDescuentoCongelados){
						CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.agregar.descuento.pavos.y.descuento.variable", "errorDescuentoAutorizacion", null, errors);
					}	
				}			
				
				if(porcentajeVarDescuento != null && porcentajeVarDescuento.length > 0){
					if(errorPorcentajeGerente){
						errors.add("descuentoVariable",new ActionMessage("errors.descuentoVariable.rango.permitido",minPorcDescVariable,maxPorcDescVariable));
					}
				}
				
				//se valida si debe aplicar descuento general y variable o no
				if(descuentoGeneralSeleccionado && existenDctoVariables && !DescuentosUtil.validarSiAplicaDescuentoGeneral(detallePedidoCol, request, opDescuento)){
					errors.add("descuentoVarGen", new ActionMessage("errors.descuentosExcluyentes"));
				}
			}				
		}			
		catch (Exception e) {				
			LogSISPE.getLog().info("Error al verificar los autorizadores para descuentos variables ",e);
			throw new Exception(e);
		}
		if(errors.isEmpty()){
			return true;
		}
		
		//si existen errores se sube a sesion la variable para que no pinte los errores en la pagina de cotizaciones 
		request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
		return false;			
	}
		
		/**
		 * Busca de acuerdo a los descuentos seleccionados las clasificaciones que son de pavos con descuento automatico (pavos, pavos marca propia)
		 * @param detallePedidoCol
		 * @param request
		 */
		public static void buscarDetallesConDctoAutomaticos (ArrayList<DetallePedidoDTO> detallePedidoCol, HttpServletRequest request) throws Exception{
			try{
				LogSISPE.getLog().info("ingresa al metodo : buscarDetallesConDctoAutomaticos");
				String[] opDescuento = (String[]) request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);		
				Set<String> clasificacionesSeleccionadasPavos = new HashSet<String>();
								
				if(opDescuento != null && opDescuento.length > 0){
					
//					Collection<TipoDescuentoClasificacionDTO> clasificacionesPavos = (Collection<TipoDescuentoClasificacionDTO>) request.getSession().getAttribute(CotizarReservarAction.TIPO_DESCUENTO_CLASIFICACION_PAVOS_COL);
					Collection<TipoDescuentoClasificacionDTO> clasificacionesPavos = new ArrayList<TipoDescuentoClasificacionDTO>();
					//si no existen datos se consultan y se sube a sesion
//					if(CollectionUtils.isEmpty(clasificacionesPavos)){
						ParametroDTO parametroDescuentoPavos = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosDescuentosPavos", request);					
						String parametrosTipoDescuentoPavos = parametroDescuentoPavos.getValorParametro();
						parametrosTipoDescuentoPavos=parametrosTipoDescuentoPavos.replace(",", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"));
						String []valorParametro= parametrosTipoDescuentoPavos.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"));
						//Obtener las clasificaciones para los tipos de descuentos obtenidos desde la tabla de parametros
						TipoDescuentoClasificacionDTO tipClasificacionDTO= new TipoDescuentoClasificacionDTO();
						tipClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						tipClasificacionDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
						tipClasificacionDTO.setCriteriaSearch(new CriteriaSearch());
						tipClasificacionDTO.getCriteriaSearch().addCriteriaSearchParameter(new CriteriaSearchParameter<String[]>("id.codigoTipoDescuento",ComparatorTypeEnum.IN_COMPARATOR,valorParametro));
						clasificacionesPavos= SISPEFactory.getDataService().findObjects(tipClasificacionDTO);
						request.getSession().setAttribute(CotizarReservarAction.TIPO_DESCUENTO_CLASIFICACION_PAVOS_COL, clasificacionesPavos);
//					}					
					
					if(CollectionUtils.isNotEmpty(clasificacionesPavos)){						
						//se recorren los detalles
						if(CollectionUtils.isNotEmpty(detallePedidoCol)){
							for(DetallePedidoDTO detallePedidoActual : detallePedidoCol){									
								//se verifica la clasificacion de pavos
								for(TipoDescuentoClasificacionDTO clasificacionPavosActual : clasificacionesPavos){
									//se verifica si el detalle es de pavos
									if(detallePedidoActual.getArticuloDTO().getCodigoClasificacion().equals(clasificacionPavosActual.getId().getCodigoClasificacion())){
										for(String opDctoActual : opDescuento){									
											if(opDctoActual != null && opDctoActual.contains("CTD"+clasificacionPavosActual.getId().getCodigoTipoDescuento()+SEPARADOR_TOKEN)){
												clasificacionesSeleccionadasPavos.add(clasificacionPavosActual.getId().getCodigoClasificacion());
											}
										}
									}
								}								
							}						
						}
					}				
				}
				request.getSession().setAttribute(CLASIFICACIONES_PAVOS_SELECCIONADOS_COL, clasificacionesSeleccionadasPavos);
			}
			catch (Exception e) {
				LogSISPE.getLog().info("Error al buscar detalles con descuento automatico de pavos {}",e);		
				throw new Exception(e);
			}		
		}	
		
		
		/**
		 * PopUp para ingresar el stock de los articulos obsoletos.
		 * 
		 * @param request HttpServletRequest
		 * @param mensaje mensaje para mostrar en pantalla.
		 * @throws Exception Excepcion
		 */
		public static void instanciarVentanaArticuloObsoleto(HttpServletRequest request, String mensaje)throws Exception{
			
			List<DetallePedidoDTO> colDetalle = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			List<DetallePedidoDTO> colDetalleObsoleto = new ArrayList<DetallePedidoDTO>();				
			
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Advertencia");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle','div_datosCliente'],{parameters: 'confirmarStockObsoleto=ok&actualizarDetalle=ok',evalScripts:true});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['mensajes','pregunta','especiales','seccion_detalle','div_datosCliente'], {parameters: 'cancelarStockObsoleto=ok',evalScripts:true});ocultarModal();");
			popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());	
			popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/popUpArticuloObsoleto.jsp");
			popUp.setAncho(60D);
			popUp.setTope(40D);
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			popUp = null;	
			
			//valida para mostrar los articulos obsoletos que cumplan la condicion cantidadEstado > stoLocArtObs
			for(DetallePedidoDTO detalle : colDetalle ){
				if(detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
					if((detalle.getEstadoDetallePedidoDTO().getCantidadEstado() > detalle.getEstadoDetallePedidoDTO().getStoLocArtObs())){
						
						colDetalleObsoleto.add(detalle);
					}
				}
			}
			
			if (indice ==  ConstantesGenerales.ESTADO_CODIGO_BARRAS){
				for(DetallePedidoDTO detalle : colDetalle){
					if(detalle.getArticuloDTO().getId().getCodigoArticulo().equals(codigoArticuloObsoleto)&&detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
						colDetalleObsoleto = new ArrayList<DetallePedidoDTO>();
						colDetalleObsoleto.add(detalle);
					}
				}
				indice = ConstantesGenerales.ESTADO_INICIAL_OBSOLETO;
			}
			
			if (indice ==  ConstantesGenerales.ESTADO_ESPECIALES){
				colDetalleObsoleto = new ArrayList<DetallePedidoDTO>();
				for(DetallePedidoDTO detalle : colDetalle){
					if(detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
						if((detalle.getEstadoDetallePedidoDTO().getCantidadEstado() > detalle.getEstadoDetallePedidoDTO().getStoLocArtObs())){
							colDetalleObsoleto.add(detalle);
						}
					}
				}
				indice = ConstantesGenerales.ESTADO_INICIAL_OBSOLETO;
			}
			
			if(colDetalleObsoleto.isEmpty()){
				session.setAttribute(SessionManagerSISPE.POPUP, null);
			}else{
				session.setAttribute(DETALLE_PEDIDO_OBSOLETO, colDetalleObsoleto);
				session.setAttribute(SE_AGREGO_ARTICULO_OBSOLETO, "ok");
			}
		}
		
		/**
		 * PopUp para configurar una reserva por defecto
		 * @param request
		 * @throws Exception
		 */
		public static void instanciarVentanaReservarStockEntrega(HttpServletRequest request)throws Exception{
			HttpSession session = request.getSession();
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Configuraci\u00F3n reservar stock entrega");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			popUp.setValorOK("requestAjax('crearCotizacion.do',['mensaje_popUp','div_pagina'],{parameters: 'aceptarReservarStockEntrega=ok',evalScripts:true});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['mensajes','pregunta'], {parameters: 'cancelarReservarStockEntrega=ok',evalScripts:true});ocultarModal();");
			popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
			popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/popUpReservarStockEntrega.jsp");			
			popUp.setAncho(45D);
			popUp.setTope(40D);
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);			
			popUp = null;
		}
		
		/**
		 * M\u00E9todo par obtener una lista de horas desde 06:00 am hasta las 20:00 pm
		 * @return lista de Horas
		 */
		public static List<String> obtenerHoras(boolean hoy ){
			List<String> horas = new ArrayList<String>();
			String hora = "";
			int i;
			if(hoy){
				i=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1;	
			}else{
				i=6;
			}
			
			for(;i<=20; i++){				
				if(i <=9){
					hora = "0"+i;
					horas.add(hora);
				}else{
					horas.add(String.valueOf(i));
				}				
			}
			return horas;
		}
		
		/**
		 * M\u00E9todo par asignar una  lista de minutos, intervalo de 15 minutos.
		 * @return lista de minutos
		 */
		public static List<String> obtenerMinutos(){
			List<String> minutos = new ArrayList<String>();			
			minutos.add("00");
			//minutos.add("15");
			minutos.add("30");
			//minutos.add("45");
			return minutos;
		}
		
		/**
		 * M\u00E9todo par obtener una lista de horas y minutos desde 06:00 am hasta las 20:00 pm
		 * @return lista de Horas
		 */
		public static List<String> obteneHorasMinutos(boolean hoy){
			List<String> horasMinutos = new ArrayList<String>();
			List<String> horas = obtenerHoras(hoy);			
			List<String> minutos = obtenerMinutos();	
			if(horas.size()>0){
				for(String hora : horas){	
					if(hora.equals("21")){
						break;
					}else{
						for(String minuto : minutos){
							if(!(hora.equals("20") &&horasMinutos.contains("20:00"))){
								horasMinutos.add(hora + ":" + minuto);
							}
						}	
					}								
				}
			}
			return horasMinutos;
		}
		
		public static void eliminarDetallesConProblemas(ArrayList<EspecialDTO> especiales){
			
			if(CollectionUtils.isNotEmpty(especiales)){
				for(EspecialDTO especialDTO : especiales){
					if(CollectionUtils.isNotEmpty(especialDTO.getArticulos())){
						
						List<ArticuloDTO> articulosEliminar = new ArrayList<ArticuloDTO>();
						//se recorren los articulos 
						for(ArticuloDTO articuloDTO : especialDTO.getArticulos()){
							if(!ArticuloMedidaDTO.isLoaded(articuloDTO.getArticuloMedidaDTO())){
								articulosEliminar.add(articuloDTO);
								LogSISPE.getLog().info("No se mostrara el articulo {}",articuloDTO.getDescripcionArticulo());
							}
						}
						especialDTO.getArticulos().removeAll(articulosEliminar);
					}
				}
			}
		}
		
		/**
		 * Elimina de la coleccion de codigos de articulos los articulos obsoletos que no fueron agregados
		 * @param request
		 */
		private static void eliminarArticulosObsoletos(HttpServletRequest request){
			
			 HttpSession session = request.getSession();
			 Collection<DetallePedidoDTO> detallesObsoletosCol =  (Collection<DetallePedidoDTO>) session.getAttribute(DETALLE_PEDIDO_OBSOLETO);
			 ArrayList<String> codigosArticulos = (ArrayList<String>)session.getAttribute(COL_CODIGOS_ARTICULOS);
			 
		
			 
			 
			 if(CollectionUtils.isNotEmpty(detallesObsoletosCol) && CollectionUtils.isNotEmpty(codigosArticulos)){
				 
				 //obtenemos el detalle del pedido qyue estan en sesion
				 Collection<DetallePedidoDTO> detallesPedidoCol =  (Collection<DetallePedidoDTO>) session.getAttribute(DETALLE_PEDIDO);
				
				 
				 //se recorren los detalles obsoletos que no fueron agregados
				 for(final DetallePedidoDTO detallePedidoDTO : detallesObsoletosCol){
					 
					 Boolean existeArticulo=Boolean.FALSE;
					 if(CollectionUtils.isNotEmpty(detallesPedidoCol)){
						 
						 existeArticulo = CollectionUtils.exists(detallesPedidoCol, new Predicate() {
							
							@Override
							public boolean evaluate(Object arg0) {
								// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
								DetallePedidoDTO detPedDTO= (DetallePedidoDTO)arg0;
								return detallePedidoDTO.getId().getCodigoArticulo().equals(detPedDTO.getId().getCodigoArticulo());
							}
						});
					 }
					 if(!existeArticulo){
						 codigosArticulos.remove(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
					 }
				 }
				 session.setAttribute(COL_CODIGOS_ARTICULOS, codigosArticulos);
			 }
		}
}