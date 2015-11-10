/*
 * GlobalsStatics.java
 * Creado el 11/06/2009 14:14:17
 *   	
 */
package ec.com.smx.sic.sispe.common.constantes;

import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;

/**
 * @author fmunoz
 * @author jmena
 *
 */
public interface GlobalsStatics
{
	//CONSTANTES
	public String PAR_USAR_AUTORIZACION = "usarAutorizacion";
	public String PAR_ACE_USO_AUT = "aceptarUsoAut";
	public String PAR_CAN_USO_AUT = "cancelarUsoAut";
	public String ACEPTAR_ARCH_BENE = "aceptarArchBene";
	public String CANCELAR_ARCH_BENE = "cancelarArchBene";
	public String SUBIR_ARCH_BENE = "adjuntarArchivo";
	public String SUBIR_ARCHIVO_FOTO = "subirArchivoFoto";
	
	//public String EXAMINAR_ARCHIVO = "examinarArchivo";
	
	public int NUMERO_DECIMALES = MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.numeroDecimales").intValue();
	public double VALOR_PORCENTAJE_IVA = Double.parseDouble(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.porcentajeIVA"));
	public String SEPARADOR_PARAMETROS = "_";
	public String DESCRIPCION_DESCUENTO_CAJA = "POR CAJAS (Incluido por Art\u00EDculo)";
	public String DESCRIPCION_DESCUENTO_MAYORISTA = "MAYORISTA (Incluido por Art\u00EDculo)";
	
	//NOMBRES DE VARIABLES DE SESION
	public String PREFIJO_VARIABLE_SESION = "ec.com.smx.sic.sispe.";	
	public String TIPO_AUTORIZACION_COL = PREFIJO_VARIABLE_SESION.concat("tiposAutorizaciones");
	public String PERSONADTO_COL = PREFIJO_VARIABLE_SESION.concat("personaDTOCol");
	public String CALCULO_PRECIOS_AFILIADO = PREFIJO_VARIABLE_SESION.concat("pedido.preciosAfiliado");
	public String CALCULO_PRECIOS_CON_IVA = PREFIJO_VARIABLE_SESION.concat("precios.iva");
	public String RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO = PREFIJO_VARIABLE_SESION.concat("entregas.rangoDiasDespachoBodega");
	public String CANT_DIAS_OBTENER_FECHAMINIMADESPACHO = PREFIJO_VARIABLE_SESION.concat("entregas.cantidadDiasFechaMinimaDespacho");
	public String COL_ESTADO_DETALLE_PEDIDO = PREFIJO_VARIABLE_SESION.concat("estadoDetallePedidoCol");
	public String VISTA_PEDIDO = PREFIJO_VARIABLE_SESION.concat("vistaPedidoDTO");
	public String CODIGO_CANASTOS_CATALOGO = PREFIJO_VARIABLE_SESION.concat("clasificacion.canastosCatalogo");	
	//almacenan el local y tipo de establecimiento cuando se realiza un pedido
	public String CODIGO_LOCAL_REFERENCIA = PREFIJO_VARIABLE_SESION.concat("codigoLocal");
	public String CODIGO_ESTABLECIMIENTO_REFERENCIA = PREFIJO_VARIABLE_SESION.concat("codigoTipoEstablecimientoObjetivo");
	public String TOTAL_DESCUENTO_POR_ARTICULO = PREFIJO_VARIABLE_SESION.concat("descuentoTotalPorArticulo");
	//public String LOCALES_ACTIVOS_PRECIO_MAYORISTA = PREFIJO_VARIABLE_SESION.concat("localesActivosPrecioMayorista");
	public String BANDERA_CONFIGURA_CAL_BOD = PREFIJO_VARIABLE_SESION.concat("flag.calendario.bodega");
	//public String LOCAL_CON_PRECIO_MAYORISTA = PREFIJO_VARIABLE_SESION.concat("localActualPrecioMayorista");
	public String COL_TIPOS_PEDIDO = PREFIJO_VARIABLE_SESION.concat("tiposPedido");
	public String CLASIFICACION_RECETAS_CATALOGO = "ec.com.smx.sic.sispe.clasificacion.recetaCatalogo";
	public String HABILITADO_CAMBIO_PRECIOS = "ec.com.smx.sic.sispe.establecimientoHabilitadoCambioPrecios";
	public String PORCENTAJE_CALCULO_PRECIOS_AFILIADO = PREFIJO_VARIABLE_SESION.concat("porcentajeCalculoPreciosAfiliado");
	public String PORCENTAJE_DIFERENCIA_PRECIOS_NORMAL_MAYORISTA = PREFIJO_VARIABLE_SESION.concat("porcentajeDiferenciaPreciosNormalMayorista");
	public String VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO = PREFIJO_VARIABLE_SESION.concat("validaMayoristaEstablecimientoClasificacion");
	public String ESTABLECIMIENTOS_DEPARTAMENO_CLASIFICACIONES = PREFIJO_VARIABLE_SESION.concat("establecimientosDepartamentosClasificacionesExc");
	public String CHECK_PAGO_EFECTIVO = PREFIJO_VARIABLE_SESION.concat("check.pagoEfectivo");
	public String COL_PEDIDO_CONSOLIDADOS_AUX = PREFIJO_VARIABLE_SESION.concat("pedidosConsolidadosAux");
	public String HABILITADO_PRECIOS_AFILIADO = "ec.com.smx.sic.sispe.establecimientoHabilitadoPreciosAfiliado";
	
	//variables del calendario del SICMER(entrega a domicilio desde local) 
	public final static String CALENDARIO_MES_EST  = PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".calendarioMesEST");
	public final static String RESUMEN_DIA_EST  = PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".resumenDiaEST");
	
	public final static String POPUP_CALENDARIO  = PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".popupCalendario");
	public final static String FECHA_ACTUAL  = PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".fechaActual");
	
	
	
}
