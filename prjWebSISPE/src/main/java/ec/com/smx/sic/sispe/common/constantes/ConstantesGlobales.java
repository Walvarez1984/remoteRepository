package ec.com.smx.sic.sispe.common.constantes;

import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;

public class ConstantesGlobales {
	public final static String FORWARD_REDIRECT = "redirect";
	public static final String PARAMETRO_TOKEN= MessagesWebSISPE.getString("security.tokenparameter");
	//PARAMETRO QUE TIENE LA URL PARA REDIRECCIONAR /sispeV2/index.jsf si el parametro que recibe es vacio
	public static final String URL_REDIRECCION =MessagesWebSISPE.getString("url.sispev2.calendario");
	
	public static final String PARAMETRO_URL_RETORNO = MessagesWebSISPE.getString("parametro.url.retorno");
	public static final String ID_ROL_ENTIDAD_RESPONSABLE= "idRolEntidadResponsable";
	public static final String CODIGO_LOCAL_ENTIDAD_RESPONSABLE= "codigoLocalEntidadResponsable";
	public static final String CODIGO_DIV_GEO_POL_ENTIDAD_RESPONSABLE= "codigoDivGeoPolEntidadResponsable";
	public static final String ID_USUARIO_ENTIDAD_RESPONSABLE= "idUsuarioEntidadResponsable";
	public static final String ESTADO_ACTIVO= "estadoActivo";
	public static final String AREA_TRABAJO= "areaTrabajo";
	public static final String PAGINA_REDIRECCIONAR= "pageRedirect";
	public static final String OPCION= "opcion";
	public static final String CODIGO_TIPO_DESCUENTO_PAGO_EFECTIVO= "codigoTipoDescuentoPagoEfectivo";
	//VARIABLES PARA EL SEGUNDO POPUP detalle de la receta
	public static final String SECUENCIAL_ESTADO_PEDIDO= "secEstPedido";
	public static final String ACCION_ACTULA= "acciAct";
	public static final String NP_CODIGO_LOCAL= "npCodLocal";
}
