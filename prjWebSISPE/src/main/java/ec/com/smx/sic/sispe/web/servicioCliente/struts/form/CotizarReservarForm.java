/*
 *  Clase CotizarReservarForm.java
 *  Creado el 27/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PORCENTAJE_CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.DivisionGeoPoliticaDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloPrecioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.CalendarioLocalEntregaDomicilioUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.DescuentosUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.CantidadCalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalID;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionStockDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.AutorizacionEntregasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.CostoEntregasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.DireccionesDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ConsolidarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CrearRecotizacionAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CrearReservacionAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleCanastaAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ModificarReservacionAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Contiene los campos y los demas controles del formulario de la Cotizacion,Recotizaci\u00F3n y Reservaci\u00F3n aqu\u00ED 
 * se realizan las validaciones de los datos ingresados por el usuario. Tambi\u00E9n se resetea el formulario cada vez que se realiza 
 * una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @author Wladimir L\u00F3pez
 * @version 3.0
 * @since 	JSDK 1.5
 */

@SuppressWarnings({"serial", "unchecked"})
public class CotizarReservarForm extends BuscarArticuloForm
{
	public static final String EXISTEN_RECETAS = "ec.com.smx.sic.sispe.pedido.existenCanastos"; 
	public static final String ERROR_ENTREGAS = "ec.com.smx.sic.sispe.errorEntrega";
	private final static String SI_REGISTRAR_RESERVACION = "siRegReservacion";
	private final static String SI_CAMBIO_ENTIDAD_RESPONSABLE = "siCamEntResponsable";
	public final static String RESERVACION_TEMPORAL = "reservacionTemporal";
	
	public static final String TIPO_ARTICULO_PAVO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
	private static final String TIPO_ARTICULO_OTRO_PESO_VARIABLE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.otroPesoVariable");
	//public static final String SI_RECALCULAR_DETALLE = "siRecalcularDetalle";
	public static final String INFO_ENTREGA_ELIMINADA = "ec.com.smx.sic.sispe.info.entregaEliminada";
	public static final String INFO_CANTIDAD_MAYOR = "ec.com.smx.sic.sispe.info.cantidadMayorEstado";
	public static final String DEVOLUCION_ABONO = "ec.com.smx.sic.sispe.devolucionAbono";
	public static final String WARNINGS_TEMPORAL = "ec.com.smx.sic.sispe.mensajes.temporales.validacion.form";
	
	//Variables para descuento
	private static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
	
	//Variables Cotizaciones Anteriores
	public static final  String PEDIDOS_ANTERIORES= "ec.com.smx.sic.sispe.accion.pedidos.anteriores";
  /*
	 * Campos principales donde se almacenan los datos del contacto y de la empresa:
	 * 
	 * String numeroCedula: Campo donde se ingresa el n\u00FAmero de c\u00E9dula, este debe ser v\u00E1lido
	 * String nombreContacto: Campo donde se ingresa el nombre del contacto
	 * String telefono: Campo donde se ingresa el tel\u00E9fono del contacto
	 * String rucEmpresa: Campo donde se ingresa el ruc de la empresa a la que pertenece el contacto
	 * String nombreEmpresa: Campo donde se ingresa el nombre de la empresa a la que pertenece el contacto
	 * String emailContacto: Campo donde se ingresa el correo electr\u00F3nico del contacto.
	 * String indiceLocalResponsable: Campo que contiene el indice del local seleccionado.
	 * String localResponsable: Campo que contiene lam descripci\u00F3n del local seleccionado.
	 * String tipoDocumentoContacto: Campo que indica el tipo de documento del contacto (cedula o pasaporte).
	 */
	//generico, almacena el RUC, CED, PASS, es la caja de texto
	private String numeroDocumento;
	
	//es el dato del contacto de la empresa
	private String numeroDocumentoContacto;
	private String nombreContacto;
	private String telefonoContacto;
	private String tipoDocumentoContacto;
	
	//para los datos de la persona
	private String numeroDocumentoPersona;
	private String nombrePersona;
	private String telefonoPersona;
	private String tipoDocumentoPersona;
	private String emailPersona;
	
	private String rucEmpresa;
	private String nombreEmpresa;
	private String emailContacto;
	private String telefonoEmpresa;
	
	private String tipoDocumento;
	//campo para la bodega
	private String indiceLocalResponsable;
	private String localResponsable;
	
	//String opTipoDocumento: campo para escoger el tipo de pedido que se desea: Personal, Empresarial
	private String opTipoDocumento;

	/* Campos para la secci\u00F3n de art\u00EDculos especiales:
	 * 
	 * String opTipoEspeciales: radioButton que permite escoger el tipo de art\u00EDculo especial
	 * String [] opEspecialesSeleccionados: Arreglo de string que almacena los indices de los especiales seleccionados
	 */
	private String opTipoEspeciales;
	private String [] opEspecialesSeleccionados;
	private String [] vectorCantidadEspecial = null;

	/* Campos para la secci\u00F3n de b\u00FAsqueda:
	 * 
	 * String opTipoBusqueda: radioButton que permite escoger el tipo de b\u00FAsqueda a realizar
	 * String textoBusqueda: texto a buscar
	 * String buscarArticulo: muestra los art\u00EDculos que fueron resultado de la b\u00FAsqueda
	 */
	private String opTipoBusqueda;
	private String textoBusqueda;

	/*
	 * Controles para crear un nuevo registro en el detalle del pedido, se muestran cuando se da clic en el bot\u00F3n Nuevo:
	 * 
	 * String codigoArticulo: campo donde se ingresa el c\u00F3digo del art\u00EDculo que se desea incluiren el detalle
	 * String cantidadArticulo: campo donde se ingresa la cantidad del art\u00EDculo
	 */
	private String codigoArticulo;
	private String cantidadArticulo;

	/*
	 * String checkActualizarStockAlcance: permite activar/desactivar la actualizaci\u00F3n del estado de los art\u00EDculos desde el SIC.
	 * String checkCalculosPreciosMejorados: permite activar/desactivar el calculo con el mejor precio del art\u00EDculo en un pedido donde los precios actuales de los art\u00EDculos cambiaron
	 * String checkCalculosPreciosAfiliado: permite activar/desactivar el calculo con precios de AFILIADO o NO AFILIADO
	 * String checkSeleccionarTodo: permite seleccionar todos los registros del detalle del pedido.
	 * String [] checksSeleccionar: permite seleccionar los registros sobre los que se va a realizar una determinada
	 * tarea.
	 * String [] vectorCantidad: Vector de cantidades en el detalle del pedidos
	 * String [] vectorCantidad: Vector de cantidades ajustadas manualmente por modificaci\u00F3n del peso total de un art\u00EDculo en el detalle del pedidos
	 * String [] vectorPrecio: Vector de precios unitarios en el detalle del pedidos
	 * String [] vectorPrecioNoAfi: Vector de precios unitarios en el detalle del pedidos para los precios de NO AFILIADO
	 * String [] vectorPeso: Vector de pesos promedio de los art\u00EDculos que manejan peso
	 */
	private String checkActualizarStockAlcance;
	private String checkCalculosPreciosMejorados;
	private String checkCalculosPreciosAfiliado;
	private String checkSeleccionarTodo;
	private String checkPagoEfectivo;
	//Aumento
	private String checkSubirArchivo;
	private FormFile archivo;
	private Collection<FormFile> files;
	private String opEstadoActivo;
	private String opBuscarConsolidados;
	//***
	private String [] checksSeleccionar;
	private String [] vectorCantidad;
	private String [] vectorCanAjuModPesos;
	private String [] vectorPrecio;
	private String [] vectorPrecioNoAfi;
	private String [] vectorPeso;
	private String [] vectorPesoActual;

	/*
	 * String opValidarPedido: Check que da la opci\u00F3n de ejecutar validaciones antes de registrar un pedido.
	 * String [] checksReservarBodega: Lista de checkboxs que permite seleccionar los art\u00EDculos que ser\u00E1n reservados 
	 * en bodega.
	 * String [] vectorCantidadReservarBodega: Vector de cajas de texto que permite ingresar la cantidad a reservar
	 * en bodega
	 */
	private String opValidarPedido;
	private String checkReservarTodoBodega;
	private String [] checksReservarBodega;
	private String [] vectorCantidadReservarBodega;

	/*
	 * Campos de calculos totales del detalle del pedido:
	 * 
	 * Double subTotal: Muestra el subtotal del pedido
	 * Double descuentoTotal: Muestra el descuento total del pedido
	 * Double ivaTotal: Muestra el iva total del pedido
	 * Double total: Muestra el monto total del pedido
	 * String valorAbono: Campo donde se ingresa la cantidad que se abonar\u00E1 para reservar el pedido, este campo 
	 * 	 solo es mostrado en la reservaci\u00F3n
	 */
	private Double subTotal;
	private Double subTotalNoAplicaIVA;
	private Double subTotalAplicaIVA;
	private Double descuentoTotal;
	private Double ivaTotal;
	private Double total;
	private String valorAbono;
	private String valorAbonoParcial;
	private Double costoFlete;
	private Double costoTotalPedido;
	private Double totalDescuentoIva;
	private Double subTotalBrutoNoAplicaIva;
	private Double subTotalNetoBruto;
	/*
	 * Campos de busqueda del Articulo, se muestran cuando se da clic en botonDescuento:
	 * 
	 * String opDescuentos: Arreglo de checkBox que identifica los descuentos escogidos
	 * String opSinDescuentos: CheckBox que permite quitar los descuentos al pedido
	 * Double porcentajeVarDescuento: Input Tex con el valor del porcentaje del descuento variable
	 */
	private String opDescuentos[];
	private String opSinDescuentos;
	private Double porcentajeVarDescuento[][];
//	private String desactivarCasillasPorcentaje[][];

	/*
	 * String fechaEntrega: Campo que muestra la fecha minima de entrega del pedido
	 * String fechaMinimaEntrega: Campo que contiene la primera fecha de entrega del pedido
	 * String fechaMaximaEntrega: Campo que contiene la \u00FAltima fecha de entrega del pedido
	 * String fechaMinimaDespacho: Campo que contiene la \u00FAltima fecha de despacho del pedido
	 */
	public String fechaEntrega;
	private Timestamp fechaMinimaEntrega;
	private Timestamp fechaMaximaEntrega;
	private Timestamp fechaMinimaDespacho;


	/*
	 * Controles que se muestran en la pantalla del detalle de la canasta, cuando se escoge el link junto 
	 * a la canasta en el detalle del pedido:
	 * 
	 * String [] vectorcantidadCanasta: Vector de cantidades en el detalle de la canasta
	 * String botonBuscarDetCanasta: Permite mostrar la seci\u00F3n de Busqueda de art\u00EDculos y tambi\u00E9n el cat\u00E1logo de art\u00EDculos para la canasta	
	 * String botonActualizarCanasta: Permite actualizar los datos de la lista del detalle de la canasta
	 * Double totalCanasta: Muestra el valor total de la canasta
	 * String botonRegistrarCanasta: Permite guardar los datos de la canasta
	 * String botonSalirCanasta: Permite volver al formulario del pedido
	 */
	private String [] vectorcantidadCanasta;
	private String [] vectorPrecioItem;
	private Double totalCanasta;
	private String botonRegistrarCanasta;
	private String botonSalirCanasta;

	/*
	 * Controles que se muestran en la secci\u00F3n de Autorizaci\u00F3n:
	 * 
	 * String opAutorizacion: Opci\u00F3n que permite escoger el tipo de autorizaci\u00F3n que se va aplicar
	 * String numeroAutorizacion: Campo donde se ingresa el n\u00FAmero de Autorizaci\u00F3n
	 * String loginAutorizacion: Campo donde se ingresa el login del usuario
	 * String passwordAutorizacion: Campo donde se ingresa el password del usuario
	 * String observacionAutorizacion: Campo donde se ingresa la observaci\u00F3n por el uso de la autorizaci\u00F3n
	 */
	private String opAutorizacion;
	private String numeroAutorizacion;
	private String loginAutorizacion;
	private String passwordAutorizacion;
	private String observacionAutorizacion;
	private String tipoAutorizacion;
	
	/* Controles que guardan la informaci\u00F3n de todo el formulario
	 * 
	 * String botonRegistrarCotizacion: Bot\u00F3n que permite almacenar la cotizaci\u00F3n
	 * String botonRegistrarReservacion: Bot\u00F3n que permite almacenar la reservaci\u00F3n
	 * String botonConsolidarPedidos: Bot\u00F3n consolidar pedidos
	 */
	private String botonRegistrarCotizacion;
	private String botonRegistrarReservacion;
	private String botonConsolidarPedidos;
	private String botonGuardarConsolidarPedidos;

	/* 
	 * String botonSi: Bot\u00F3n que permite confirmar una pregunta
	 * String botonNo: Bot\u00F3n que permite cancelar una pregunta
	 */
	private String botonSi;
	private String botonNo;

	/* Campos para la secci\u00F3n de b\u00FAsqueda y entregas:
	 * 
	 * String buscaFecha: radioButton que permite escoger el tipo de b\u00FAsqueda a realizar
	 * String listaLocales: seleccion de un local
	 * String buscarArticulo: muestra los art\u00EDculos que fueron resultado de la b\u00FAsqueda
	 * String calendarioDiaLocal: Calendario
	 * String dias: Dias de la senana
	 * String cantidadEstados: Articulos Seleccionados
	 * String cantidadPedidaBodega: cantidad de articulos que se pide a bodega
	 * String fechaEntregaCliente: Fecha de entrega al cliente
	 * String opTipoEntrega: opcion de tipo entrega a local o domicilio
	 * String numeroBultosF: total de bultos entregados a un local en una misma fecha
	 * String direcciones: opcion para escoger la direccion
	 * String autorizacion: texto para ingresar el numero de autorizacion en las entregas
	 * String fechaDespacho: campo para indicar la fecha de despacho
	 * String localOSector: campo para indicar que sector o local fue seleccionado
	 * String idLocalOSector: campo para indicar el id del local seleccionado
	 * String distancia: campo para indicar que distancia existe del local de entrega a la casa
	 * String unidadTiempo: campo para indicar la unidad de tiempo
	 * String distanciaH: campo para indicar la unidad de tiempo horas
	 * String distanciaM: campo para indicar la unidad de tiempo minutos
	 * String horas: campo para indicar la hora de entrega
	 * String minutos: campo para indicar el minuto de entrega
	 * String segundos: campo para indicar el segundo de entrega
	 * String opLocalEntrega: opcion para seleccional el local de entrega
	 * String local: opcion para ingresar directamente un local sin necesidad de seleccionarlo de la lista
	 * String responsableEntrega: condicion que habilita o deshabilita las opciones para el responsable de la entrega
	 * String lugarEntrega: condicion que habilita o deshabilita las opciones para el lugar de la entrega
	 * String tipoEntrega: condicion que habilita o deshabilita las opciones para el tipo de la entrega
	 * String stockEntrega: condicion que habilita o deshabilita las opciones para el stock de la entrega
	 * String seleccionCiudad: lista de ciudades para entrega a domicilio desde la bodega
	 */
	private String buscaFecha;
	private String listaLocales;
	private Object[] calendarioDiaLocal;
	private Object[] dias;
	private String[] cantidadEstados;
	private String[] cantidadEntregas;
	private String[] cantidadPedidaBodega;
	private String[] cantidadDespachos;
	private String[] seleccionEntregas;
	private String todo;
	private String registrarFecha;
	private String fechaEntregaCliente;
	private String fechaDespacho;
	private String sectores;
	private String direccion;
	//private String opLocalResponsable;
	private String opLugarEntrega;
	private String opTipoEntrega;
	private String opStock;
	private String numeroBultosF;
	private String [] direcciones;
	private String autorizacion;
	private String localOSector;
	private String distancia;
	private String unidadTiempo;
	private String distanciaH;
	private String distanciaM;
	private String horasMinutos;
	private String horas;
	private String minutos;
	private String segundos;
	private String idLocalOSector;
	private String opLocalEntrega;
	private String local;
	
	//variables para recolectar direccion de entrega completa
	private String callePrincipal;
	private String numeroCasa;
	private String calleTransversal;
	private String referencia;
	
	private String responsableEntrega;
	private String lugarEntrega;
	private String tipoEntrega;
	private String stockEntrega;
	
	private String seleccionCiudad;
	private String[] checkTransitoArray;
	private String selecionCiudadZonaEntrega; //para la zona de la ciudad
	
	/**
	 * Par\u00E1metros de paginaci\u00F3n
	 * <ul>
	 * <li>Collection datos: Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
	 * <li>String start: Indice para el inicio del paginador</li>
	 * <li>String range: Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada paginaci\u00F3n</li>
	 * <li>String size: Valor que indica el n\u00FAmero total de registros.</li>
	 * </ul>
	 */
	private Collection datosConsolidados;
	private Collection datosConsolidadosTotal;
	private Collection datos;
	private String start;
	private String range;
	private String size;
	
	/**
	 * Controles donde se ingresan los par\u00E1metros de busqueda:
	 * <ul>
	 * <li>String codigoLocal: El c\u00F3digo del local donde se desea realizar la b\u00FAsqueda, esta opci\u00F3n solo 
	 * 		est\u00E1 habilitada para la bodega</li>
	 * <li>String campoBusqueda: Campo donde se ingresa el valor del campo a buscar</li>
	 * <li>String fechaInicial: Campo donde se ingresa la fecha inicial, desde donde comienza la consulta</li>
	 * <li>String fechaFinal: Campo donde se ingresa la fecha final, hsta donde llega la consulta</li>
	 * <li>String estadoPedido: Combo donde se escoge el estado del pedido</li>
	 * <li>String opcionCampoBusqueda: RadioButton que permite escoger solo uno de los par\u00E1metros de busqueda</li>
	 * <li>String opcionFecha: RadioButton que permite escoger si se busca por rango de fechas o por toda la temporada</li>
	 * <li>String etiquetaFechas: Permite mostrar la descripci\u00F3n del tipo de fechas utilizadas para la b\u00FAsqueda</li>
	 * <ul>
	 */
	private String codigoLocal;
	private String fechaInicial;
	private String fechaFinal;
	private String estadoPedido;
	private String estadoPagoPedido;
	private String alerta;	
	private String opcionFecha;
	private String etiquetaFechas;
	private String opEntidadResponsable;
	private String opDespachoPendiente;
	private String opStockArticuloReservado;
	private String numeroMeses;
	private String opPedidoOrdenCompra;
	
	private String numeroPedidoTxt;
	private String numeroReservaTxt;
	private String numeroConsolidadoTxt;
	private String codigoClasificacionTxt;
	private String codigoArticuloTxt;
	private String descripcionArticuloTxt;
	private String documentoPersonalTxt;
	private String nombreContactoTxt;
	private String rucEmpresaTxt;
	private String nombreEmpresaTxt;
	
	private String comboTipoPedido;
	private String comboEstadoPagoPedido;
	private String comboEnviadoCD;
	private String comboTipoFecha;
	//private String comboTipoDocumento;
	private String opTipoAgrupacion;
	
	//Seleccionar uno o varios pedidos para consolidar
	private String [] opSeleccionPedidosConsolidar;
	private String opSeleccionTodos;
	
	//Seleccionar uno o varios pedidos consolidados
	private String [] opSeleccionPedidosConsolidados;
	private String opSeleccionTodosConsolidados;
	private String [] pedidosValidados;
	private String [] numeroConsolidado;
	private String numeroPedidoConsolidado;
	private String numeroAConsolidar="-1";
	private String [] opDescuentoSeleccionadoConsolidado;
	private String [] opPedidoGeneralConsolidado;
	
	//radiobutton reserva
	private String radioReserva;
	//radiobutton elaboracion de canastas especiales
	private String opElaCanEsp;
	//variable para colocar el valor q va a replicar a todas las cantidades de las entregas
	private String valorReplica;
	
	private Double costoEntregaDomicilio;
	
	//variables que controlan las acciones del popUp de art\u00EDculos obsoletos
	private int indiceDetalle=ConstantesGenerales.ESTADO_SIN_SELECCION;
	private String [] vectorObsoleto;
	private Collection<DetallePedidoDTO> detallePedidoRespaldo = new ArrayList<DetallePedidoDTO>();
	private Boolean nuevoCodigoBarrasOk = false;
	private Boolean nuevoEspecialOk = false;
	private String codigoArticuloObsoleto = "";
	private Integer respaldo = 0;
	private String numBonNavEmp;
	
	//variables para la entrega en domicilio de mi local calendario SICMER
	private String codigoVendedorSicmer;
	private String nombreVendedorSicmer;
	private String numeroDocumentoSicmer;
	private String quienRecibeSicmer;
	private String horaDesde;
	private String horaHasta;
	private String npHoraDesde;
	private String npHoraHasta;
	
	private String checkReservarStockEntrega;
	private Boolean responsableLocal= false;
	
	// variables para busqueda de  pedidos anteriores
	private String opEscogerTodos;
	private String [] opEscogerProdBuscados;
	
	
	//IOnofre. Variables para la busqueda de articulos
	/**
	   * Campos para realizar la busqueda:
	   * <ul>
	   * 	<li><code>String codigoClasificacion: </code>Campo donde se ingresa el n\u00FAmero de clasificaci\u00F3n</li>
	   * 	<li><code>String nombreClasificacion: </code>Campo donde se ingresa el nombre de la clasificaci\u00F3n</li>
	   * 	<li><code>String nombreArticulo: </code>Campo donde se ingresa el nombre del art\u00EDculo</li>
	   * 	<li><code>String opcionBusqueda: </code>Permite escoger solo una de las opciones de busqueda</li>
	   * 	<li><code>String botonBuscarProd: </code>Bot\u00F3n que permite realizar la b\u00FAsqueda</li>
	   * </ul>
	   */
	  private String [] checksClasificaciones;
	  private String codigoClasificacion;
	  private String nombreClasificacion;
	  private String nombreArticulo;
	  private String opcionBusqueda;
	  private String botonBuscarProd;
	  
	  
	  /**
	   * Campos mostrados en el formulario de la lista de art\u00EDculos buscados:
	   * <ul>
	   * 	<li><code>String opEscogerTodos: </code>Permite escoger todos los art\u00EDculos que se van a a\u00F1adir al detalle 
	   * 		del pedido</li>
	   * 	<li><code>String [] opEscogerProdBuscados: </code>Arreglo de Checkboxs que permite escoger un subconjunto de art\u00EDculos que
	   * 		se van a a\u00F1adir al detalle del pedido</li>
	   * 	<li><code>String botonAnadirArt: </code>Bot\u00F3n que permite a\u00F1adir los art\u00EDculos seleccionados</li>
	   * 	<li><code>String botonCancelarArt: </code>Bot\u00F3n que cancela la acci\u00F3n</li>
	   * 	<li><code>String [] precioEspecial: </code>Arreglo de campos para actualizar el precio especial</li>
	   * 	<li><code>String botonActualizarArt: </code>Bot\u00F3n que permite actualizar el precio especial de un art\u00EDculo, este 
	   * 		bot\u00F3n es mostrado solo cuando se hace el mantenimento de los art\u00EDculos</li>
	   * </ul>
	   */

	  private String botonAnadirArt;
	  private String botonCancelarArt;
	  private String [] precioEspecial;
	  private String botonActualizarArt;
	  
	  //IOnofre. Variables para validar el check de abono cero
	  private String checkAbonoCero;


	
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>cotizarReservar.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request){
		
		ActionErrors errors = new ActionErrors();
		
		//ActionMessages messages = new ActionMessages();
		PropertyValidator validator = new PropertyValidatorImpl();
		HttpSession session = request.getSession();		

		session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
		//se saca un respaldo de los detalles
		if (request.getParameter("cancelarStockObsoleto")==null && request.getParameter("actualizarDetalle")!=null && respaldo == 0){	
			Collection<DetallePedidoDTO> detallePedidoSesion = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			Collection<DetallePedidoDTO> colDetalle = (Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedidoSesion);
			detallePedidoRespaldo = colDetalle;
			respaldo = 1;
		}
		
		//compruebo si se ejecuto la acci\u00F3n desde la lista de detalles
		if(indiceDetalle!=ConstantesGenerales.ESTADO_SIN_SELECCION){
			nuevoCodigoBarrasOk = false;
			nuevoEspecialOk = false;
		}
		
		//si selecciono la opci\u00F3n cancelar desde el popUp de los art\u00EDculos obsoletos
		if (request.getParameter("cancelarStockObsoleto")!=null){	
			codigoArticuloObsoleto="";
			//si se agrego un articulo desde la opci\u00F3n C\u00F3digo barras
			if(nuevoCodigoBarrasOk == true && indiceDetalle == ConstantesGenerales.ESTADO_SIN_SELECCION){
				ArrayList<DetallePedidoDTO> colDetallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)SerializationUtils.clone((Serializable)colDetallePedido);
				if(nuevoEspecialOk==false){
					DetallePedidoDTO detalle = detallePedido.get(detallePedido.size()-1);
					detallePedido.remove(detalle);
					session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedido);	
					ArrayList<String> colArticulos = (ArrayList<String>)session.getAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS);
					ArrayList<String> articulos = (ArrayList<String>)SerializationUtils.clone((Serializable)colArticulos);
					
					//elimino el codigo del articulo almacenado				
					int indiceDetalle = 0;
					for (Iterator<String> it = colArticulos.iterator(); it.hasNext();)
					{						
						String articulo = it.next();
						if(articulo.equals(detalle.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())){//if(articulo.equals(detalle.getArticuloDTO().getId().getCodigoArticulo())){
							articulos.remove(indiceDetalle);
							codigoArticuloObsoleto = "El art\u00EDculo OBSOLETO con C\u00F3digo barras: " + 
							detalle.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras() +" - " + detalle.getArticuloDTO().getDescripcionArticulo() + ", no ha sido agregado.";
						}
						indiceDetalle++;
					}
					
					session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS,articulos);
					nuevoCodigoBarrasOk=false;
					indiceDetalle = -1;
				}
			}
			//si se agregaron articulos desde la secci\u00F3n de especiales
			else if(nuevoEspecialOk==true && indiceDetalle == ConstantesGenerales.ESTADO_SIN_SELECCION){
				Collection<DetallePedidoDTO> colDetallePedidoObsoleto = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO_OBSOLETO);				
				Collection<DetallePedidoDTO> colDetallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				//Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)colDetallePedido);
				Collection<DetallePedidoDTO> detalleObsoletoEliminarPedido = new ArrayList<DetallePedidoDTO>();
				ArrayList<String> colArticulos = (ArrayList<String>)session.getAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS);
				Boolean existeArticuloObsoleto=Boolean.FALSE;
				for (DetallePedidoDTO detallePedidoDTO:colDetallePedido){						
					for(DetallePedidoDTO detalleObsoleto : colDetallePedidoObsoleto){
						if(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detalleObsoleto.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())//if(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(detalleObsoleto.getArticuloDTO().getId().getCodigoArticulo())
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().equals(detalleObsoleto.getEstadoDetallePedidoDTO().getCantidadEstado())
								&& detalleObsoleto.getArticuloDTO().getClaseArticulo().equals("O")
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().equals(detalleObsoleto.getEstadoDetallePedidoDTO().getStoLocArtObs())){
							detalleObsoletoEliminarPedido.add(detallePedidoDTO);
							for (String articulo:colArticulos){//se eliminan los codigos de los articulos obsoletos no agregados
								if(articulo.equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())){
									colArticulos.remove(articulo);
									break;
								}
							}
							if(existeArticuloObsoleto){
								codigoArticuloObsoleto = codigoArticuloObsoleto + "</br>";
							}
							
							codigoArticuloObsoleto = codigoArticuloObsoleto + "El art\u00EDculo OBSOLETO con C\u00F3digo barras: " +
									detalleObsoleto.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras() +" - " + detalleObsoleto.getArticuloDTO().getDescripcionArticulo()+ ", no ha sido agregado.";
							existeArticuloObsoleto=Boolean.TRUE;
							break;
						}
					}
				}
				//eliminar articulos obsoletos no eliminados
				colDetallePedido.removeAll(detalleObsoletoEliminarPedido);
				session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, colDetallePedido);
				session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS,colArticulos);
				nuevoEspecialOk=false;
				this.indiceDetalle = ConstantesGenerales.ESTADO_SIN_SELECCION;
			}else{
				//cuando ingresa por pedidos anteriores la coleccion de respaldo esta vacia
				if(CollectionUtils.isEmpty(detallePedidoRespaldo) || detallePedidoRespaldo.size()!=((ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO)).size()){
					Collection<DetallePedidoDTO> detallePedidoSesion = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					detallePedidoRespaldo = (Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedidoSesion);
					//quitar de detallePedidoRespaldo los articulos obsoletos que tengan stock 0
					Collection<DetallePedidoDTO> detallePedidoSinObsoletos = new ArrayList<DetallePedidoDTO>();
					
					//Coleccion de codigos de barras de articulos del pedido
					List codigosArticulosPedido = (List)session.getAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS);
					for(DetallePedidoDTO detallePedidoDTO : detallePedidoRespaldo){
						if((detallePedidoDTO.getArticuloDTO().getClaseArticulo()!=null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null)&&!(detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))&& detallePedidoDTO.getArticuloDTO().getNpStockArticulo()==0))
						{
							detallePedidoSinObsoletos.add(detallePedidoDTO);
						}else{
							codigosArticulosPedido.remove(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras());
						}
					}
					
					detallePedidoRespaldo=detallePedidoSinObsoletos;
				}
				//actualizo la listade detalles, con la lista de respaldo
				session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoRespaldo);	
				indiceDetalle = ConstantesGenerales.ESTADO_SIN_SELECCION;
			}
		}
		
		//Si se agrega un art\u00EDculo ingresando el C\u00F3digo barras
		if (request.getParameter("articuloNuevo")!=null){			
			if(indiceDetalle!=ConstantesGenerales.ESTADO_SIN_SELECCION){
				nuevoCodigoBarrasOk = false;
				nuevoEspecialOk = false;
			}else{
				nuevoCodigoBarrasOk = true;
				nuevoEspecialOk = false;
				indiceDetalle = ConstantesGenerales.ESTADO_SIN_SELECCION;
			}
		}
		
		//Si se agrega articulos desde la secci\u00F3n de especiales
		if (request.getParameter("articuloNuevoEspecial")!=null){
			nuevoEspecialOk = true;
			nuevoCodigoBarrasOk = false;
			indiceDetalle = ConstantesGenerales.ESTADO_SIN_SELECCION;
		}
		
		//Si se agrega articulos desde la secci\u00F3n de especiales y se presiono el boton agregar
		if (request.getParameter("agregarArticulo")!=null && this.opEspecialesSeleccionados!=null){
			nuevoEspecialOk = true;
			nuevoCodigoBarrasOk = false;
			indiceDetalle = ConstantesGenerales.ESTADO_SIN_SELECCION;
		}
				
		//Si se ejecuta una acci\u00F3n en la lista de los detalles, desde el campo texto cantidad
		if(request.getParameter("indiceDetalle")!=null){
			indiceDetalle = Integer.valueOf(request.getParameter("indiceDetalle"));	
		}
		
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		LogSISPE.getLog().info("por validar - cotizacion/reservacion");
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//si selecciono la opci\u00F3n confirmar desde el popUp de los art\u00EDculos obsoletos
		if (request.getParameter("confirmarStockObsoleto")!=null){
			List<DetallePedidoDTO> detallePedidoObsoletos = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO_OBSOLETO);	
			//se actualizan los datos ingresados de los stock obsoletos
			actualizarDetalleObsoletos(detallePedidoObsoletos, request, errors);
		}	
				
		if(request.getParameter("aceptarReservarStockEntrega")!=null){
			if(getFechaEntregaCliente()==""){
				errors.add("Exception",new ActionMessage("errors.requerido","Fecha M\u00E1x. Entrega"));					
			}if(getHoras().equals("")){
				errors.add("Exception",new ActionMessage("errors.requerido","Hora"));					
			}if(getMinutos().equals("")){
				errors.add("Exception",new ActionMessage("errors.requerido","Minutos"));					
			}
		}
		
		if(this.horasMinutos!=null){
			if(!this.horasMinutos.equals("")){
				String hora = this.horasMinutos + ":00";
				String[] horaSeleccionada =  hora.split(":");
				this.horas = horaSeleccionada[0];
				this.minutos = horaSeleccionada[1];
				this.segundos = horaSeleccionada[2];
			}else{
				this.horas = null;
				this.minutos = null;
				this.segundos = null;
			}
		}
		
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("Valor de la peticion de Cotizar-->"+peticion);
		if(peticion!=null && !peticion.equals("")){
			if(peticion.equals("regCotizacion") || peticion.equals("regReservaTemp"))
				this.botonRegistrarCotizacion = peticion;
			else if(peticion.equals("regReservacion") || peticion.equals(SI_REGISTRAR_RESERVACION) || peticion.equals(SI_CAMBIO_ENTIDAD_RESPONSABLE)){
				if(peticion.equals("regReservacion") && session.getAttribute(RESERVACION_TEMPORAL) != null){
					session.setAttribute(RESERVACION_TEMPORAL, null);
				}
				this.botonRegistrarReservacion = peticion;
			}	
			else if(peticion.equals("acepCanasto")){
				this.botonRegistrarCanasta = peticion;
				session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
				if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
					this.setCheckCalculosPreciosAfiliado(estadoActivo);
				}
//				//cambios autorizacion stock dentro canastos especiales
//				try {
//					AutorizacionesUtil.aplicarEstadoAutorizacionStockRecetaADetalle(request);
//				} catch (Exception e) {
//					LogSISPE.getLog().info(" Error al validar si todos los detalles del canasto han sido aprobados "+e.getMessage());
//				}
			}
			else if(peticion.equals("cancCanasto")){
				this.botonSalirCanasta = peticion;
				session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
				if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
					this.setCheckCalculosPreciosAfiliado(estadoActivo);
				}
			}
			else if(peticion.equals("siGenerarPDF"))
				request.setAttribute("formarPDF","ok");
			else if(peticion.equals("siImprimirPedido"))
				request.setAttribute("imprimirTexto","ok");
			//else if(peticion.equals("enviarMail"))
				//request.setAttribute("enviarMail","ok");
			else if(peticion.equals("siRegCanasto") || peticion.equals("siRegCanastoIgual"))
				this.botonRegistrarCanasta = peticion;
			else if(peticion.equals("noRegCanasto"))
				this.botonSalirCanasta = peticion;
			/*else if(peticion.equals(CotizacionReservacionUtil.PRECIOS_ACTUALES)){
				this.checkCalculosPreciosMejorados = estadoActivo;
				request.setAttribute(SI_RECALCULAR_DETALLE, "ok");
			}*/
			//esta condici\u00F3n es exclusiva para el registro de la confirmaci\u00F3n de la reservaci\u00F3n
			else if(peticion.equals("regConfirmacionReservacion"))
				request.setAttribute("regConfirmacionReservacion","ok");
			else if(peticion.equals("regConsolidacion")){
				this.botonConsolidarPedidos=peticion;
			}else if(peticion.equals("guardarConsolidacion")){
				this.botonGuardarConsolidarPedidos=peticion;
			}

			//esta condici\u00F3n siempre debe ser consultada
			if(peticion.equals("siCambiarLocal") || peticion.equals(SI_REGISTRAR_RESERVACION) || peticion.equals("siVolverBusqueda"))
				this.botonSi = peticion;
		}
		if(request.getParameter("cambiarCanastoConsolidacion") != null || request.getParameter("conservarCanastoConsolidacion") != null){
			setBotonRegistrarCanasta(request.getParameter("cambiarCanastoConsolidacion") != null ? "cambiarCanastoConsolidacion" : "conservarCanastoConsolidacion");
			session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
				this.setCheckCalculosPreciosAfiliado(estadoActivo);
			}
		}
		
		//cuando esta cambiando el canasto de catalago a especial y existen entregas a domicilio despachadas
		if(request.getParameter("siEliminarDetalle") != null || request.getParameter("cancelarEliminarDetalleDespachado") != null){
			setBotonRegistrarCanasta(request.getParameter("siEliminarDetalle") != null ? "siEliminarDetalle" : "cancelarEliminarDetalleDespachado");
			session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
				this.setCheckCalculosPreciosAfiliado(estadoActivo);
			}
		}
		
		PropertyValidator validacion = new PropertyValidatorImpl();
		if(request.getParameter("pedidosAnteriores")!=null&&request.getParameter("accionesPedAnt")!=null&&request.getParameter("accionesPedAnt").startsWith("buscarPedAnt")){
			//se verifica si el usuario logeado no es de un local
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable()
					.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
				
				if(null != this.codigoLocal && this.codigoLocal.equals("ciudad"))
					errors.add("codigoLocal",new ActionMessage("errors.requerido","Local Responsable"));
			}
			/*------------ cuando se escoge Buscar por fechas ---------------*/
			if(this.opcionFecha!=null && this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))
					&& errors.isEmpty()){
				LogSISPE.getLog().info("Se validan los rangos de fechas...");															
									
				//si no hubieron errores
				validacion.validateFecha(errors,"fechaInicial",this.getFechaInicial(),true,
						MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha inicial");
				validacion.validateFecha(errors,"fechaFinal",this.getFechaFinal(),true,
						MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha final");									
			}
			
			/*----------- si se busca por la opci\u00F3n todos ----------------*/
			if(this.opcionFecha!=null && this.opcionFecha.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))
					&& errors.isEmpty()){
				//si no hubieron errores
				validacion.validateLong(errors, "numeroMeses", this.numeroMeses, true, 0, 999, "errors.formato.long", "N\u00FAmero de Meses");
			}
			setNuevoEspecialOk(Boolean.FALSE);
		}
		
		try
		{
      /*-------- cuando se pasa de una cotizaci\u00F3n o recotizaci\u00F3n a una reservacion y viseversa ---------*/
      if(request.getParameter("cambiarContexto")!=null){
    	  LogSISPE.getLog().info("request cambiarContexto");
//        if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion")) 
//            || accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))){
        	
        	//se asigna la propiedad en null para que no valide los campos de la reservaci\u00F3n
    	this.botonRegistrarReservacion = null;
    	this.validarCotizacionReservacion(request, errors, validator, accion);
//        }
      }
			/*------------------- cuando se presiona el boton agregar -------------------*/
			if(request.getParameter("agregarArticulo")!=null){
				
				LogSISPE.getLog().info("request agregarArticulo");
				if(this.opEspecialesSeleccionados==null || request.getParameter("ingresoManual")!=null){
					validator.validateMandatory(errors,"codigoArticulo",this.codigoArticulo,"errors.requerido","C&oacute;digo barras");
					validator.validateLong(errors,"cantidadArticulo",this.cantidadArticulo,true,1,99999,"errors.formato.long","cantidad");
				}
				//esta condici\u00F3n significa que est\u00E1 agregando desde la receta
				if(session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA)!=null && errors.isEmpty()){
					//se obtiene el detalle principal seleccionado
					//DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA);
					//se obtiene el c\u00F3digo de la receta vacia
					//String codigoRecetaVacia = SessionManagerSISPE.getCodigoCanastoVacio(request);
					//se realiza la validaci\u00F3n de la cantidada m\u00E1xima a ingresar solo si no es el canasto vacio
					//if(!detallePedidoDTO.getId().getCodigoArticulo().equals(codigoRecetaVacia)){
						String parametroCantidadMaxima = (String)session.getAttribute("ec.com.smx.sic.sispe.parametro.cantidadMaximaPorItemReceta");
						if(parametroCantidadMaxima!= null && Long.parseLong(this.cantidadArticulo.trim()) > Long.parseLong(parametroCantidadMaxima)){
							errors.add("cantidadArticulo", new ActionMessage("errors.cantidad.maxPorItemRecetaOriginal", parametroCantidadMaxima));
						}
					//}
				}
				this.cambiarAlTabPedido(session, request);
			}
			/*------------------- cuando se presiona el boton subirArchivo -------------------*/
			if(request.getParameter("adjuntarArchivo")!=null) {
				LogSISPE.getLog().info("Presiono el Boton abrir de la ventana del Examinar");
				if(this.archivo != null ){
					LogSISPE.getLog().info("Tiene un valor el archivo");
				}
					
			}
			/*----Verifica si la fecha de entrega ya fue ingresada-------*/
			else if((this.registrarFecha !=null && !this.registrarFecha.equals(""))
					|| (request.getParameter("registrarFecha") != null  && !request.getParameter("registrarFecha").equals(""))){
				
				this.registrarFecha="OK";
				LogSISPE.getLog().info("this.registrarFecha != null");
				validator.validateMandatory(errors,"fechaEntrega",this.fechaEntrega,"errors.fechaEntrega.requerida");
				if(errors.size()==0){
					EntregaLocalCalendarioUtil.obtenerFechaMinimaEntrega(request, errors, this);
				}
				if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
					this.setCheckCalculosPreciosAfiliado(estadoActivo);
				}
				//check nuevamente si tiene o no pago en efectivo
				if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok")){
					this.setCheckPagoEfectivo(estadoActivo);
				}
			}
			
			/*-------- cuando se presiona el boton Actualizar o cuando se acepta recalcular los valores
			 * del detalle con los precios actuales (Ventana de Confirmaci\u00F3n) -------------*/
			else if(request.getParameter("actualizarDetalle")!=null){
				
				//se quita de sesion los mensajes temporales
				session.removeAttribute(CotizarReservarForm.WARNINGS_TEMPORAL);
				
				Collection<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				//validar si se ingreso como valor el 0 o null en el popUp de art\u00EDculos obsoletos
				Boolean validar = false;
				for(DetallePedidoDTO detalle : detallePedido){
					if(detalle.getArticuloDTO().getClaseArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
						if(detalle.getEstadoDetallePedidoDTO().getStoLocArtObs() == null || detalle.getEstadoDetallePedidoDTO().getStoLocArtObs()==0 ){
							validar = true;
							break;
						}
					}
				}	
				
				if(validar == true && request.getParameter("confirmarStockObsoleto")!=null){
					session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
					errors.add("Exception", new ActionMessage("errors.stock.articulo.obsoleto.cero"));
				}else{
					LogSISPE.getLog().info("request actualizarDetalle");
					session.setAttribute(CotizacionReservacionUtil.MODIFICAR_PESO_INACTIVO, null);
					//Collection<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					actualizarDetalleForm(request, errors, accion, estadoActivo, estadoInactivo,detallePedido);
				}				
			}
			/*-------- cuando se presiona el boton Actualizar o cuando se acepta recalcular los valores
			 * del detalle con los precios actuales (Ventana de Confirmaci\u00F3n) -------------*/
			else if(request.getParameter("confirmarActualizarPrecios")!=null){
				LogSISPE.getLog().info("request confirmarActualizarPrecios");
				Collection<VistaPedidoDTO> pedidosConsolidados=(Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				//determinar si se aplican precios de afiliado o no 
				Boolean sinPrecioAfiliado=Boolean.TRUE;
				if(pedidosConsolidados!=null){
					for(VistaPedidoDTO visPedDTO:pedidosConsolidados){
						if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
			    			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			    			this.setCheckCalculosPreciosAfiliado(estadoActivo);
			    			sinPrecioAfiliado=Boolean.FALSE;
			    			break;
			    		}
					}
					if(sinPrecioAfiliado){
		    			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
					}
				}
				Collection<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				actualizarDetalleForm(request, errors, accion, estadoActivo, estadoInactivo,detallePedido);
			}
			/*------------- cuando se da clic en el bot\u00F3n Actualizar en el formulario de la canasta -------------------*/
			else if(request.getParameter("actualizarCanasto")!=null){
				
				LogSISPE.getLog().info("request actualizarCanasto");
				//cuando se a\u00F1adieron art\u00EDculos por la b\u00FAsqueda no se itera el detalle del canasto para actualizarlo
				if(session.getAttribute(DetalleCanastaAction.TOTAL_RECETA_A_SUMAR) == null)
				{
					LogSISPE.getLog().info("por actualizar canasta");

			    //se obtiene la cantidad m\u00E1xima a modificar por item de una receta de cat\u00E1logo
			    String cantMaxItemRecetaCatalogo = (String)session.getAttribute(DetalleCanastaAction.CANT_MAX_ITEM_RECETA); 
			    //se obtiene la articulo receta que se est\u00E1 validando
			    DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA);

					//se obtiene el detalle de la canasta
			    //List detalleCanasta = (List)session.getAttribute(DetalleCanastaAction.COL_DETALLE_RECETA);
					List<ArticuloRelacionDTO> detalleCanasta = (List<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
					//se obtiene la colecci\u00F3n de las cantidades originales del canasto
					List cantidadesOriginales = (List)session.getAttribute(DetalleCanastaAction.COL_CANT_ORIGINALES_RECETA);
					
					if(detalleCanasta!=null && !detalleCanasta.isEmpty())
					{
						//se obtiene el total del canasto
						double totalCanasto=((Double)session.getAttribute(DetalleCanastaAction.TOTAL_RECETA)).doubleValue();
						//se obtiene el c\u00F3digo de la receta vacia
						//String codigoRecetaVacia = SessionManagerSISPE.getCodigoCanastoVacio(request);
						int contadorCantidadesExedidas = 0;
						
						//se itera el detalle del canasto
						for (int i=0;i<detalleCanasta.size();i++)
						{
							ArticuloRelacionDTO recetaArticuloDTO = (ArticuloRelacionDTO)detalleCanasta.get(i);
							//se obtiene la clave que indica una modificaci\u00F3n de precios en el canasto
							String estadoModificacionPrecios = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.receta.actualizar.precio");
							boolean cantidadMaximaCorrecta = true;
							
							ArticuloDTO articuloDTO = null;
							ArticuloPrecioDTO articuloPrecioDTO = null;
							Collection<ArticuloPrecioDTO> articuloPrecioCol = null;

							try
							{	
								LogSISPE.getLog().info("CODIGO CANASTO: {}",recetaArticuloDTO.getId().getCodigoArticuloRelacionado());								
								articuloPrecioDTO = new ArticuloPrecioDTO();
								articuloDTO = new ArticuloDTO();
								articuloPrecioCol = new ArrayList<ArticuloPrecioDTO>();
								
								//se realiza la comparaci\u00F3n de cantidades
								if(Long.parseLong(vectorcantidadCanasta[i]) > Long.parseLong(cantMaxItemRecetaCatalogo)){
									cantidadMaximaCorrecta = false;
									recetaArticuloDTO.setCantidad(new Integer(vectorcantidadCanasta[i]));
								}
								
								//solo si la cantidad m\u00E1xima es correcta
								if(cantidadMaximaCorrecta){
									//si el usuario ingresa un n\u00FAmero menor que cero se hace positivo
									if(Long.parseLong(vectorcantidadCanasta[i])<0){
										long numPositivo = Long.parseLong(vectorcantidadCanasta[i]);
										numPositivo = (-1)*numPositivo;
										vectorcantidadCanasta[i] = Long.toString(numPositivo);
									}else if(Long.parseLong(vectorcantidadCanasta[i])==0)
										vectorcantidadCanasta[i] = recetaArticuloDTO.getCantidad().toString();

									//solo si la clave que indica la modificaci\u00F3n de precios unitarios est\u00E1 activa o no
									if(estadoModificacionPrecios.equals(estadoActivo)){
										//si el usuario ingresa un precio menor que cero se hace positivo
										if(Double.parseDouble(vectorPrecioItem[i])<0){
											double numPositivo = Double.parseDouble(vectorPrecioItem[i]);
											numPositivo = (-1)*numPositivo;
											vectorPrecioItem[i] = Double.toString(numPositivo);
										}else if(Double.parseDouble(vectorPrecioItem[i])==0 || Double.parseDouble(vectorPrecioItem[i])>99999.99){
											//this.vectorPrecioItem[i] = recetaArticuloDTO.getPrecioUnitarioIVA().toString();
											this.vectorPrecioItem[i] = recetaArticuloDTO.getArticuloRelacion().getPrecioBase().toString();
										}
									}

									//se almacena en la colecci\u00F3n de las recetas las nuevas cantidades ingresadas
									recetaArticuloDTO.setCantidad(new Integer(this.vectorcantidadCanasta[i]));
									//se formatea el valor del precio unitario
									Double precioUnitarioFormateado = Util.roundDoubleMath(Double.valueOf(vectorPrecioItem[i]), NUMERO_DECIMALES);
									//solo si existe la posibilidad de cambio en los precios en los detalles de la receta
									if(estadoModificacionPrecios.equals(estadoActivo)){
										//recetaArticuloDTO.setPrecioUnitarioIVA(precioUnitarioFormateado);
										articuloPrecioDTO.setValorActual(precioUnitarioFormateado);
										articuloPrecioCol.add(articuloPrecioDTO);
										articuloDTO.setArticuloPrecioCol(articuloPrecioCol);
										recetaArticuloDTO.setArticulo(articuloDTO);
										//SE VERIFICA SI EL ART\u00EDCULO APLICA IVA
										//se hace con calculos del SIC
//										if(recetaArticuloDTO.getArticulo().getAplicaImpuesto().equals(estadoActivo)){
//											//se elimina el iva del precio unitario
//											double precioUnitarioSinIVA = precioUnitarioFormateado.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//											recetaArticuloDTO.setPrecioUnitario(Util.roundDoubleMath(Double.valueOf(precioUnitarioSinIVA), NUMERO_DECIMALES));
//										}else
//											recetaArticuloDTO.setPrecioUnitario(precioUnitarioFormateado);
									}
									//se llama a la funci\u00F3n que calcula los totales del detalle
									CotizacionReservacionUtil.calcularTotalesDetalleReceta(recetaArticuloDTO);

									//se obtiene la cantidad almacenada en sesi\u00F3n para este detalle
									long cantidadOriginal = ((Long)cantidadesOriginales.get(i)).longValue();
									//solo si las cantidades son diferentes
									if(recetaArticuloDTO.getCantidad().longValue()!= cantidadOriginal){
										long diferencia = recetaArticuloDTO.getCantidad().longValue() - cantidadOriginal;
										double totalDiferenciaIVA = 0;
										//se verifica si el art\u00EDculo es de peso variable
										if(recetaArticuloDTO.getArticuloRelacion().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
											double pesoTotal = diferencia * recetaArticuloDTO.getArticuloRelacion().getArticuloComercialDTO().getPesoAproximadoVenta();
											//totalDiferenciaIVA = pesoTotal * recetaArticuloDTO.getPrecioUnitarioIVA().doubleValue();
											totalDiferenciaIVA = pesoTotal * recetaArticuloDTO.getArticulo().getPrecioBase().doubleValue();
										}else
											//totalDiferenciaIVA = diferencia * recetaArticuloDTO.getPrecioUnitarioIVA().doubleValue();
											totalDiferenciaIVA = diferencia * recetaArticuloDTO.getArticuloRelacion().getPrecioBase().doubleValue();
										
										LogSISPE.getLog().info("totalDiferenciaIVA: {}",totalDiferenciaIVA);
										//se formatea el total diferenciado
										Double totalDiferenciaIVAFormateado = Util.roundDoubleMath(Double.valueOf(totalDiferenciaIVA), NUMERO_DECIMALES);
										totalCanasto = totalCanasto + totalDiferenciaIVAFormateado.doubleValue();
										cantidadesOriginales.set(i,Long.parseLong(recetaArticuloDTO.getCantidad().toString()));
									}

									//control de stock para el detalle
									if(recetaArticuloDTO.getArticuloRelacion().getNpStockArticulo()!=null){
										long cantidadCanasto = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
										long cantidadItem = recetaArticuloDTO.getCantidad().longValue();
										long stockArticulo = recetaArticuloDTO.getArticuloRelacion().getNpStockArticulo().longValue();
										long totalCantidad = cantidadItem * cantidadCanasto;
										if(totalCantidad <= stockArticulo)
											recetaArticuloDTO.getArticuloRelacion().setNpEstadoStockArticulo(estadoActivo);
										else
											recetaArticuloDTO.getArticuloRelacion().setNpEstadoStockArticulo(estadoInactivo);
									}
								}else{
									contadorCantidadesExedidas++;
								}
								
							}catch(NumberFormatException e){
								LogSISPE.getLog().info("NumberFormatException");
							}catch(ArrayIndexOutOfBoundsException e){
								//solo se captura la excepci\u00F3n
								LogSISPE.getLog().info("ArrayIndexOutOfBoundsException");
							}
						}
						//guardo en sesion los subtotales que me van a servir para las actualizaciones posteriores.
						session.setAttribute(DetalleCanastaAction.TOTAL_RECETA, Double.valueOf(totalCanasto));
						
						//condici\u00F3n para mostrar el warning el la acci\u00F3n posterior
						if(contadorCantidadesExedidas > 0){
							//si algunas cantidades fueron exedidas se agrega un mensaje warning
							request.setAttribute("cantidadExedida","ok");
							session.setAttribute(DetalleCanastaAction.DETALLE_SIN_ACTUALIZAR, "ok");
						}else
							//si las cantidades son correctas se elimina esta variable
							session.removeAttribute(DetalleCanastaAction.DETALLE_SIN_ACTUALIZAR);
					}
				}
			}
			/*-------------------------- cuando se desea registrar una Canasta ---------------------------*/
			else if(this.botonRegistrarCanasta!=null){
				LogSISPE.getLog().info("botonRegistrarCanasta != null");
				//List detalleCanasta = (List)session.getAttribute(DetalleCanastaAction.COL_DETALLE_RECETA);
				List<ArticuloRelacionDTO> detalleCanasta = (List<ArticuloRelacionDTO>)((DetallePedidoDTO)session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA)).getArticuloDTO().getArticuloRelacionCol();
				boolean hayUnImplemento = this.verificarCantidadesDetalleCanasta(request,detalleCanasta,errors);
				if(!hayUnImplemento){
					//se obtiene la variable que indica las clasificaciones de los implementos
					String clasificacionesImplementos = (String)session.getAttribute(DetalleCanastaAction.CODIGOS_CLASIFICACIONES_IMPLEMENTOS);
					errors.add("sinItems",new ActionMessage("errors.requerido.implemento", clasificacionesImplementos));
				}
			}
			/*------------------------ cuando se da clic en el bot\u00F3n de Aplicar Autorizaci\u00F3n ------------------*/
			else if(request.getParameter("aplicarAutorizacion")!=null){
				
				LogSISPE.getLog().info("opAutorizacion: {}",this.opAutorizacion);
				if(this.opAutorizacion.equals(estadoInactivo))
					validator.validateMandatory(errors,"numeroAutorizacion",this.numeroAutorizacion,"errors.requerido","N\u00FAmero de autorizaci\u00F3n");
				else{
					validator.validateMandatory(errors,"loginAutorizacion",this.loginAutorizacion,"errors.requerido","Usuario");
					validator.validateMandatory(errors,"passwordAutorizacion",this.passwordAutorizacion,"errors.requerido","Contrase\u00F1a");
				}
			}
			/*----------------- cuando se registra una cotizaci\u00F3n o recotizacion ----------------------*/
			else if(this.botonRegistrarCotizacion!=null || this.botonRegistrarReservacion!=null)//regReservacion desde confReservar
			{
				LogSISPE.getLog().info("botonRegistrarCotizacion != null --> se va registar la cotizacion");
				
				//solo si no se ha registrado ya el pedido
				 if(session.getAttribute(CotizarReservarAction.TRANSACCION_REALIZADA) == null){
					//se verifica si es un registro temporal de la reservaci\u00F3n en este caso solo
					//se debe recotizar el pedido
					if(this.botonRegistrarCotizacion!=null && this.botonRegistrarCotizacion.equals("regReservaTemp")){
						//se modifica esta propiedad para que no valide posibles problemas de stock
						this.opValidarPedido = estadoActivo;
						//request.setAttribute(RESERVACION_TEMPORAL, "ok");
						session.setAttribute(RESERVACION_TEMPORAL, "ok");
					}
					
					//se llama al m\u00E9todo que realiza la validaci\u00F3n del formulario
					this.validarCotizacionReservacion(request, errors, validator, accion);
					
					if(StringUtils.isEmpty(tipoDocumento) || StringUtils.isEmpty(nombrePersona) && StringUtils.isEmpty(nombreEmpresa) ){
						errors.add("numeroDocumento",new ActionMessage("errors.cotizarreservar.buscarcliente"));
					}
					//check nuevamente el incicando si tiene o no precios de afiliado
					if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
						this.setCheckCalculosPreciosAfiliado(estadoActivo);
					}
					//check nuevamente si tiene o no pago en efectivo
					if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok")){
						this.setCheckPagoEfectivo(estadoActivo);
					}
				}
			}else if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) != null){
				
				String respuestaAutorizaciones = request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"));
				Long tipoAutorizacion = AutorizacionesUtil.obtenerCodigoTipoAutorizacion(respuestaAutorizaciones);
				
				if(tipoAutorizacion.longValue() != ConstantesGenerales.getInstancia().TIPO_AUTORIZACION_CONSOLIDACION.longValue()){
					//se llama al m\u00E9todo que realiza la validaci\u00F3n del formulario
					this.validarCotizacionReservacion(request, errors, validator, accion);
				}
				
				//si existen errores se cierra el popUp de autorizaciones
				if(errors != null && errors.size() == 1 && ((Iterator<ActionMessage>)errors.get()).next().getKey().equals("errors.alcanceConAutorizacion.reservacion")){
					errors.clear();
					session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
				}
			}
			/*----------------- cuando se quiere realizar una consolidacion y no ha ingresado ningun valor requerido ----------------------*/
			else if(this.botonConsolidarPedidos!=null && this.botonConsolidarPedidos.equals("regConsolidacion")){
				LogSISPE.getLog().info("botonConsolidarPedidos != null");
//				//solo si no se ha registrado ya el pedido
//				if(session.getAttribute(CotizarReservarAction.TRANSACCION_REALIZADA) == null){
					//se verifica si es un registro temporal de la reservaci\u00F3n en este caso solo
					//se debe recotizar el pedido
//					if(this.botonConsolidarPedidos!=null ){
//						//se modifica esta propiedad para que no valide posibles problemas de stock
//						this.opValidarPedido = estadoActivo;
//						request.setAttribute(RESERVACION_TEMPORAL, "ok");
//					}
//					//se llama al m\u00E9todo que realiza la validaci\u00F3n del formulario
					//this.validarCotizacionReservacion(request, errors, validator, accion);
//				}
			}
			//calcular el costo flete de la entrega
			else if(request.getParameter("botonCalcularCostoEntregaDomicilio") != null){
				
				PropertyValidator validar= new PropertyValidatorImpl();
				if(this.unidadTiempo!= null && !this.unidadTiempo.equals("K")){
					validarDistanciaTiempo(errors,validar);
				}else{
										
					//IO, validacion de nuevos campos para obtener direccion completa.
					if(this.callePrincipal== null || this.callePrincipal.isEmpty()){
						errors.add("callePrincipal",new ActionMessage("errors.requerido","Calle principal"));
					}
					if(this.numeroCasa== null || this.numeroCasa.isEmpty()){
						errors.add("numeroCasa",new ActionMessage("errors.requerido","# de casa"));
					}
					if(this.calleTransversal== null || this.calleTransversal.isEmpty()){
						errors.add("calleTransversal",new ActionMessage("errors.requerido","Calle transversal"));
					}
					if(this.referencia== null || this.referencia.isEmpty()){
						errors.add("referencia",new ActionMessage("errors.requerido","Referencia/Contacto/ Tel\u00E9fono completo:"));
					}
					
					validar.validateMandatory(errors,"unidadTiempo",this.unidadTiempo,"errors.unidadTiempo.requerido");
				}
				
			}
			/*----------------- VALIDA EL RADIOBUTTON PARA LA RESERVA ----------------------*/
			else if (getRadioReserva()==null && request.getParameter("verPopUpDetallesCambio") != null) {				
				errors.add("common.radio.err", new ActionMessage("error.common.html.radio.required"));
			}
			//Valida que los numeros de documentos sean correctos
		    else if(request.getParameter("editarPersona")!=null && request.getParameter("editarPersona").equals("ok")){		    	
		    	LogSISPE.getLog().info("editarPersona");
			}
		    else if(request.getParameter("registrarPersona")!=null && request.getParameter("registrarPersona").equals("ok")){
		    	 validarTipoDocumento(validator, errors, request);
		    }
		    else if(request.getParameter("registrarLocalizacion")!=null && request.getParameter("registrarLocalizacion").equals("ok")){
		    	validarTipoDocumento(validator, errors, request);
			}
		    else if(request.getParameter("editarEmpresa")!=null && request.getParameter("editarEmpresa").equals("ok")){
		    	LogSISPE.getLog().info("editarEmpresa");
			}
		    else if(request.getParameter("registrarEmpresa")!=null && request.getParameter("registrarEmpresa").equals("ok")){
		    	validarTipoDocumento(validator, errors, request);								
			
		    }
			/*-------- cuando se acepta el popUp Reservar Stock Entrega*/
			else if(request.getParameter("aceptarReservarStockEntrega")!=null){					
				DateFormat formatter = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha")); 
				if(getFechaEntregaCliente()!=""){
					Date date1 =(Date)formatter.parse(getFechaEntregaCliente()); 
					Date date2 =(Date)formatter.parse(getFechaEntrega());						
					if(date1.compareTo(date2)<=0){
						errors.add("Exception",new ActionMessage("errors.fechaMenor","Fecha M\u00E1x. Entrega","Fecha M\u00EDn. Entrega"));
					}
					if(getFechaEntregaCliente()!=""&&!getHoras().equals("")&&!getMinutos().equals("") && date1.compareTo(date2)>0){
						List<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);										
						EntregaLocalCalendarioUtil.reservarStockEntregaDetallePedido(request,this,detallePedidoDTOCol,getFechaEntregaCliente(), getHoras(),getMinutos(),errors);
						setBotonRegistrarReservacion("regReservacion");
					}
				}
			}
			//validacion de calendario sicmer cuando no hay rango de horas disponibles
			else if(request.getParameter("calendariosicmer")!=null&&request.getParameter("accioncalendario")!=null && request.getParameter("accioncalendario").equals("colocarfecha")){
				LogSISPE.getLog().info("Validacion Calendario Sicmer");
				if(StringUtils.isEmpty(this.getNpHoraDesde()) || StringUtils.isEmpty(this.getNpHoraHasta())){
					errors.add("RangoHoras",new ActionMessage("error.rango.horas"));
				}
			}else if(request.getParameter("botonAceptarEntrega")!=null){
				
				if(request.getSession().getAttribute(EntregaLocalCalendarioAction.MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
					validarCamposRequeridosEntregasDomicilioDesdeCD(request, errors);
				}else{
					validator.validateFecha(errors,"fechaEntregaCliente",this.getFechaEntregaCliente(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","fecha entrega cliente");
					validator.validateFecha(errors,"buscaFecha",this.getBuscaFecha(),true,
							MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","fecha m\u00EDnima de entrega");
					EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, this);
				}
				session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE,"ok");
			}
			
		    //IOnofre. Cuando se da clic en el boton A\u00F1adir art\u00EDculos
			else if(request.getParameter("agregarArticulos")!=null && request.getParameter("buscador")!=null){
		      if(this.opEscogerProdBuscados==null && this.opEscogerTodos==null){
		        errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un Art\u00EDculo"));
		      }
		    }

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}	
		return errors;
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>cotizarReservar.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		LogSISPE.getLog().info("por resetear");
		//Variable de sesion para mantener varibles de formulario en 2 request (ejm: componente JSF)
		if(request.getSession().getAttribute(CotizarReservarAction.FORMULARIO_TEMPORAL)!=null){
			return;
		}

		//solo para la reservaci\u00F3n
		this.fechaEntrega=null;
		this.opEspecialesSeleccionados = null;
		this.vectorCantidadEspecial = null;

		this.opTipoBusqueda=null;
		this.textoBusqueda=null;

		this.codigoArticulo=null;
		this.cantidadArticulo= null;

		this.checkActualizarStockAlcance=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
		this.checkCalculosPreciosMejorados = null;
		this.checkCalculosPreciosAfiliado = null;
		this.checkSeleccionarTodo=null;
		this.checksSeleccionar=null;
		this.checkPagoEfectivo=null;
		this.checkReservarStockEntrega=null;

		this.fechaMinimaDespacho=null;
		this.fechaMinimaEntrega=null;
		this.fechaMaximaEntrega=null;
		//this.opDescuentos=null;
		this.opSinDescuentos=null;

		this.vectorcantidadCanasta=null;
		this.botonRegistrarCanasta=null;
		this.botonSalirCanasta=null;

		this.numeroAutorizacion=null;
		this.loginAutorizacion=null;
		this.passwordAutorizacion=null;
		this.observacionAutorizacion=null;
		this.tipoAutorizacion = null;
		
		this.botonRegistrarCotizacion=null;
		this.botonRegistrarReservacion=null;
		this.checksReservarBodega=null;
		this.vectorCantidadReservarBodega=null;
		//this.porcentajeVarDescuento = null;

		this.botonNo=null;
		this.botonSi=null;

		this.buscaFecha=null;
		this.listaLocales=null;
		//this.calendarioDiaLocal=null;
		this.dias=null;
		this.cantidadEntregas = new String[0];
		this.cantidadPedidaBodega=new String[0]; 
		this.cantidadEstados = new String[0];
		this.cantidadDespachos=new String[0];
		this.seleccionEntregas=new String[0];
		this.todo=null;
		this.registrarFecha=null;
		this.fechaEntregaCliente=null;
		this.sectores=null;
		this.direccion=null;
		this.opTipoEntrega=null;
		//this.opLocalResponsable=null;
		this.opLugarEntrega=null;
		this.opStock=null;
		this.opElaCanEsp=null;
		//this.opTipoEntrega=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local");
		this.numeroBultosF=null;
		this.direcciones=null;
		this.autorizacion=null;
		this.fechaDespacho=null;
		this.localOSector=null;
		this.unidadTiempo=null;
		this.distancia=null;
		this.distanciaH=null;
		this.distanciaM=null;
		this.horasMinutos=null;
//		this.horas=null;
//		this.minutos=null;
		this.segundos=null;
		this.idLocalOSector=null;
		this.opLocalEntrega=null;
		this.local=null;
		this.responsableEntrega=null;
		this.lugarEntrega=null;
		this.tipoEntrega=null;
		this.stockEntrega=null;
		this.seleccionCiudad=null;
		this.selecionCiudadZonaEntrega=null;
		this.opValidarPedido=null;
		this.checkTransitoArray=null;
		this.checkSubirArchivo=null;
		//this.archivo= null;
		this.files=null;
		this.opEstadoActivo= null;
		this.opBuscarConsolidados= null;
		
		//Inicializa variables de direccion completa de entrega completa
		this.callePrincipal=null;
		this.numeroCasa=null;
		this.calleTransversal=null;
		this.referencia=null;

		this.botonConsolidarPedidos=null;
		this.botonGuardarConsolidarPedidos=null;
		
		this.opSeleccionPedidosConsolidar=null;
		this.opSeleccionTodos=null;
		
		this.opSeleccionPedidosConsolidados=null;
		this.opSeleccionTodosConsolidados=null;
		
		this.numeroMeses = "4";
		
		this.numeroPedidoTxt = null;
		this.numeroReservaTxt = null;
		this.numeroConsolidadoTxt=null;
		this.codigoClasificacionTxt = null;
		this.codigoArticuloTxt = null;
		this.descripcionArticuloTxt = null;
		this.documentoPersonalTxt = null;
		this.nombreContactoTxt = null;
		this.rucEmpresaTxt = null;
		this.nombreEmpresaTxt = null;
		this.opDescuentoSeleccionadoConsolidado=null;
		this.opPedidoGeneralConsolidado =null;
		
		this.radioReserva = null;
		this.opDescuentos=null;
		this.costoEntregaDomicilio = null;
		
		//variables entrega a domicilio desde local Calendario SICMER
		this.numeroDocumentoSicmer=null;
		this.nombreVendedorSicmer=null;
		this.quienRecibeSicmer=null;
		this.horaDesde=null;
		this.horaHasta=null;
		this.npHoraDesde=null;
		this.npHoraHasta=null;
		this.numeroDocumentoSicmer=null;
		this.nombreVendedorSicmer=null;

		//variables busqueda pedidos anteriores
		this.opcionFecha=null;
		this.fechaInicial=null;
		this.fechaFinal=null;
		this.opEscogerProdBuscados=null;
		this.opEscogerTodos=null;
		
	}


	/**
	 * 
	 * @param request
	 * @param errors
	 * @param session
	 * @param accion
	 * @param estadoActivo
	 * @param estadoInactivo
	 * @throws Exception
	 */
	public void actualizarDetalleForm(HttpServletRequest request, ActionErrors errors, String accion, String estadoActivo, String estadoInactivo,Collection<DetallePedidoDTO> detallePedido) throws Exception{
		LogSISPE.getLog().info("Actualizar detalle en el FORM");
		
		HttpSession session = request.getSession();
		
		//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
		session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
		
		boolean calculosPreciosAfiliado = true;
		boolean intercambiarPrecios = false;
		StringBuffer errorDeTolerancia = null;
		session.removeAttribute(INFO_CANTIDAD_MAYOR);
		LogSISPE.getLog().info("CheckCalculosPreciosAfiliado: {}",this.getCheckCalculosPreciosAfiliado());
		//se verifica el valor del control para precios de afiliado
		if(this.getCheckCalculosPreciosAfiliado()!=null && this.getCheckCalculosPreciosAfiliado().equals(estadoActivo)){
			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			//verifica si es un pedido consolidado y actualiza el calculo precio afiliado en todos los pedidos del grupo
			if(request.getSession().getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS) != null){
				Collection<VistaPedidoDTO> vistaPedidosConsolidados = (Collection<VistaPedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				for(VistaPedidoDTO visPedDTO : vistaPedidosConsolidados){
					visPedDTO.setEstadoPreciosAfiliado(estadoActivo);
				}
			}
		}else{
			session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
			calculosPreciosAfiliado = false;
			//verifica si es un pedido consolidado y actualiza el calculo precio afiliado en todos los pedidos del grupo
			if(request.getSession().getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS) != null){
				Collection<VistaPedidoDTO> vistaPedidosConsolidados = (Collection<VistaPedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				for(VistaPedidoDTO visPedDTO : vistaPedidosConsolidados){
					visPedDTO.setEstadoPreciosAfiliado(estadoInactivo);
				}
			}
		}

		if(request.getParameter("intercambioPrecios")!= null){
			intercambiarPrecios = true;
			request.getSession().setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS,"SI");
			//si este par\u00E1metro no es nulo significa que se respondi\u00F3 SI a la pregunta de confirmaci\u00F3n al cargarse la pantalla
			if(request.getParameter("preciosActuales") != null){
				this.checkCalculosPreciosMejorados = estadoActivo;
			}
		}
		
		//se verifica la empresa para calculos con iva o sin iva
		CotizacionReservacionUtil.verificarEmpresaExentaIVA(request, this.rucEmpresa, this.opTipoDocumento);
		
		LogSISPE.getLog().info("se actualiza el detalle");
		//cuando se a\u00F1adieron art\u00EDculos por la b\u00FAsqueda no se itera el detalle para actualizarlo
		if(session.getAttribute(BuscarArticuloAction.ART_AGREGADOS_BUSQUEDA_PED) == null)
		{
			//se obtiene el detalle del pedido de sesi\u00F3n
			//Collection detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);

			Double porcentajePrecioAfiliado = (Double)session.getAttribute(PORCENTAJE_CALCULO_PRECIOS_AFILIADO);
			//si no ocurrio alg\u00FAn error y el detalle no est\u00E1 vacio
			if(detallePedido!=null && !detallePedido.isEmpty() 
					&& this.vectorCantidad!=null && (this.vectorPrecio!=null || this.vectorPrecioNoAfi!=null))
			{
				LogSISPE.getLog().info("TAMANO DETALLE: {}",detallePedido.size());
				int indiceDetalle = 0;
				int indicePavos = 0;
				boolean cambioCantidad  = false;
				boolean existenCambios = false;
				boolean validaPesos = false;
				//StringBuffer articulosObsoletos = null;
				//UtilPopUp popUp = null;
				//esta colecci\u00F3n contiene los \u00EDndices de las cantidades modificadas con un valor menor al original
				ArrayList indicesModificados = (ArrayList)session.getAttribute(CotizarReservarAction.COL_INDICES_CANTIDADES_MODIFICADAS);
				int contadorIndicesModificados = 0;
				//se obiene la constante que representa la acci\u00F3n confirmar reservaci\u00F3n
				String accionConfirmarReservacion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion");
				//se obtiene la clave que indica una modificaci\u00F3n de precios en el detalle principal
				String inputsActivosModificacionPrecios = (String)session.getAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS);
				
				ActionMessages warnings = new ActionMessages();
				
				//se itera la colecci\u00F3n de los detalles del pedido
				for (DetallePedidoDTO detallePedidoDTO : detallePedido)
				{					
					
					cambioCantidad = false;
					validaPesos = false;
					try
					{
						//se verifica la acci\u00F3n sobre la cual se est\u00E1 actualizando el detalle del pedido
						if(accion!=null && accion.equals(accionConfirmarReservacion)){
							//solo para los art\u00EDculos de peso variable							
							if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
								//si el usuario ingresa un peso menor que cero se hace positivo
								if(Double.parseDouble(this.vectorPeso[indiceDetalle])<0){
									double peso = Double.parseDouble(this.vectorPeso[indiceDetalle]);
									peso = (-1)*peso;
									this.vectorPeso[indiceDetalle] = Double.toString(peso);
								}else if(Double.parseDouble(this.vectorPeso[indiceDetalle])==0 || Double.parseDouble(this.vectorPeso[indiceDetalle])>999999.99){
									this.vectorPeso[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().toString();
								}
								
								//se actualiza el peso en el objeto del estado
								detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(Double.valueOf(this.vectorPeso[indiceDetalle]));
								//se recalcula el valor de la cantidad
								//detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(CotizacionReservacionUtil.recalcularCantidadPorModificacionPesos(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal(), detallePedidoDTO.getArticuloDTO().getPesoAproximado()));
								//detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadAjustadaModificacionPeso(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
								//lamada a la funci\u00F3n que calcula el total del detalle
								CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, true,false);
							}
						}else{
							//CONTROL DE LA CANTIDAD INGRESADA
							LogSISPE.getLog().info("vectorCantidad: {}",this.vectorCantidad);
							//se crea esto en caso de que se ingresen decimales
							Double cantidadD = Double.valueOf(this.vectorCantidad[indiceDetalle]);
							long cantidad = cantidadD.longValue();
							this.vectorCantidad[indiceDetalle] = Long.toString(cantidad);

							//si el usuario ingresa un n\u00FAmero menor que cero se hace positivo
							if(cantidad < 0){
								long numPositivo = cantidad;
								numPositivo = (-1)*numPositivo;
								this.vectorCantidad[indiceDetalle] = Long.toString(numPositivo);
							}else if(cantidad == 0)
								this.vectorCantidad[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().toString();

							//CONTROL DEL PESO INGRESADO PARA LOS ARTICULOS QUE NO SON PAVOS
							if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
								if(Double.parseDouble(this.vectorPeso[indiceDetalle])<0){
									double peso = Double.parseDouble(this.vectorPeso[indiceDetalle]);
									peso = (-1)*peso;
									this.vectorPeso[indiceDetalle] = Double.toString(peso);
								}else if(Double.parseDouble(this.vectorPeso[indiceDetalle])==0 || Double.parseDouble(this.vectorPeso[indiceDetalle])>999999.99){
									this.vectorPeso[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().toString();
								}else{
									//se calcula la cantidad en base al peso
									Double cantidadGenerada = Math.ceil(Double.parseDouble(this.vectorPeso[indiceDetalle]));
									this.vectorCantidad[indiceDetalle] = String.valueOf(cantidadGenerada.longValue());
								}
							}
							
							//CONTROL DEL PRECIO INGRESADO
							//solo si existe la posibilidad de cambio de precios
							if(inputsActivosModificacionPrecios != null){
								if(calculosPreciosAfiliado){
									//si el usuario ingresa un precio menor que cero se hace positivo
									if(Double.parseDouble(this.vectorPrecio[indiceDetalle])<0){
										double numPositivo = Double.parseDouble(this.vectorPrecio[indiceDetalle]);
										numPositivo = (-1)*numPositivo;
										this.vectorPrecio[indiceDetalle] = Double.toString(numPositivo);
									}else if(Double.parseDouble(this.vectorPrecio[indiceDetalle])==0 || Double.parseDouble(this.vectorPrecio[indiceDetalle])>99999.99){
										if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
											//se verifica si el art\u00EDculo aplica IVA
											this.vectorPrecio[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().toString();
										else
											this.vectorPrecio[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().toString();
									}
								}else{
									//si el usuario ingresa un precio menor que cero se hace positivo
									if(Double.parseDouble(this.vectorPrecioNoAfi[indiceDetalle])<0){
										double numPositivo = Double.parseDouble(this.vectorPrecioNoAfi[indiceDetalle]);
										numPositivo = (-1)*numPositivo;
										this.vectorPrecioNoAfi[indiceDetalle] = Double.toString(numPositivo);
									}else if(Double.parseDouble(this.vectorPrecioNoAfi[indiceDetalle])==0 || Double.parseDouble(this.vectorPrecioNoAfi[indiceDetalle])>99999.99){
										if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
											//se verifica si el art\u00EDculo aplica IVA
											this.vectorPrecioNoAfi[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVANoAfiliado().toString();
										else
											this.vectorPrecioNoAfi[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().toString();
									}
								}
							}
							//Se verifica se el usuario modifico la cantidad
							if(!Long.valueOf(this.vectorCantidad[indiceDetalle]).equals(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado())){
								cambioCantidad = true;
								existenCambios = true;
								//verificar si el articulo se debe aplicar descuento automatico o no
								CotizacionReservacionUtil.iniciarDescuentoPavos(request, detallePedidoDTO.getArticuloDTO());
								
								//si la cantidad actual es mayor a la original se elimina la autorizacion de stock
								if(Long.valueOf(this.vectorCantidad[indiceDetalle]) > detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
									if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){	
										//las autorizacion que seran eliminadas
										Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
										for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacionesDTO : detallePedidoDTO.getEstadoDetallePedidoDTO()
												.getDetalleEstadoPedidoAutorizacionCol()){
												
											//se comparan los datos
											if(detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion()
													.equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
												LogSISPE.getLog().info("eliminando autorizacion de stock del articulo "+detallePedidoDTO.getId().getCodigoArticulo()+"-"+
													detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
												autorizacionesEliminar.add(detalleAutorizacionesDTO);
												detallePedidoDTO.setNpSinStockPavPolCan(true);
												
												//se inactivan las autorizaciones de stock de los detalle de la canasta especial
												if(CollectionUtils.isNotEmpty(detalleAutorizacionesDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
													//se recorren las autorizaciones de stock
													for(DetalleEstadoPedidoAutorizacionStockDTO autStock : detalleAutorizacionesDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
														
														if(autStock.getEstado().equals(ConstantesGenerales.ESTADO_ACTIVO)){
															
															//se inactiva la autorizacion de stock
															autStock.setEstado(estadoInactivo);
															
															//se asigna ese estado de autorizacion al detalle de la receta
															ArticuloRelacionDTO artRelacionDTO = AutorizacionesUtil.obtenerArticuloRelacion(detallePedidoDTO, autStock.getCodigoArticulo(), autStock.getCodigoArticuloRelacionado());
															if(artRelacionDTO != null){
																artRelacionDTO.getArticuloRelacion().setNpEstadoAutorizacion(null);
															}
														}
													}
												}
											}
										}
										//se eliminan las autorizaciones
										detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
										if(CollectionUtils.isEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
											detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
										}
									}
								}else{
									//la cantidad actual es menor a la anterior, si tiene autorizaciones de stock aprobadas se actualiza la cantidad utilizada

									if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){	
										
										for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacionesDTO : detallePedidoDTO.getEstadoDetallePedidoDTO()
												.getDetalleEstadoPedidoAutorizacionCol()){
												
											//se comparan los datos
											if(detalleAutorizacionesDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
												
												if(CollectionUtils.isNotEmpty(detalleAutorizacionesDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol())){
													
													//se recorren las autorizaciones de stock
													for(DetalleEstadoPedidoAutorizacionStockDTO autStock : detalleAutorizacionesDTO.getDetalleEstadoPedidoAutorizacionStockDTOCol()){
														
														if(autStock.getEstado().equals(ConstantesGenerales.ESTADO_ACTIVO) && autStock.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
															autStock.setCantidadUtilizada(Integer.valueOf(this.vectorCantidad[indiceDetalle]));
															LogSISPE.getLog().info("cantidad de stock utilizado: "+autStock.getCantidadUtilizada()); 
														}
													}
												}
											}
										}
									}
								}
							}
							
							boolean cambiarDetalle = true;

							//se obtiene la colecci\u00F3n de entregas
							LogSISPE.getLog().info("INDICES CANTIDADES MODIFICADAS: {}",indicesModificados);

							//se actualiz\u00F3 el detalle luego dar clic en el bot\u00F3n que registra el pedido
							if(indicesModificados!=null){
								//si el contador de indices es menor al tama\u00F1o de la colecci\u00F3n de indices modificados
								if(contadorIndicesModificados < indicesModificados.size()){
									String indice = indicesModificados.get(contadorIndicesModificados).toString();
									if(indiceDetalle == Integer.parseInt(indice)){
										LogSISPE.getLog().info("eliminar entregas por modificacion cuando indicesModificados is null");
										eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, errors, request, true, false);
										contadorIndicesModificados++;
									}
								}
							}else{
								//se actualiz\u00F3 el detalle inmediatamente luego de modificar las cantidades
								if(Long.parseLong(vectorCantidad[indiceDetalle]) < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
									LogSISPE.getLog().info("eliminar entregas por modificacion cuando indicesModificados <> null");
									
									int cantidadModificada = Integer.parseInt(vectorCantidad[indiceDetalle]);
									int cantidadDespachada = EntregaLocalCalendarioUtil.obtenerCantidadDespachada(detallePedidoDTO);
									
									//validar si se puede disminuir la cantidad
									if(EntregaLocalCalendarioUtil.existenEntregasDomicilioCDEntregadas(detallePedidoDTO)){
										if(cantidadModificada >= cantidadDespachada){
											
											LogSISPE.getLog().info("borrar solo las entregas que son configuradas al local u otro local");
											eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, errors, request, true, true);
											
										}else{
											LogSISPE.getLog().info("no puede disminuir el detalle por tener entregas a domicilio desde el CD en estado entregado");
											warnings.add("noEliminarEntregas", new ActionMessage("errors.disminuir.cantidad.por.detalles.entregados",
													detallePedidoDTO.getArticuloDTO().getDescripcionArticulo(),(cantidadDespachada)));
											cambiarDetalle = false;
										}
									}else{
										eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, errors, request, true, false);
									}
										
								}
								if(cambiarDetalle){
									//se actualiz\u00F3 el detalle inmediatamente luego de modificar las cantidades
									if(Long.parseLong(vectorCantidad[indiceDetalle]) > detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
										LogSISPE.getLog().info("cantidad ingresada mayor a la anterior");
										verificaCantidadMayorAlaEstado(detallePedidoDTO, errors, request);
									}
								}
							}
							
							if(cambiarDetalle){
								//Control de asignaciones en los precios modificados
								this.controlCambiosPrecio(detallePedidoDTO, indiceDetalle, inputsActivosModificacionPrecios, 
										calculosPreciosAfiliado, porcentajePrecioAfiliado, request);
								
								//se almacena en la colecci\u00F3n de detalles las nuevas cantidades ingresadas
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(Long.valueOf(this.vectorCantidad[indiceDetalle]));	
								
								LogSISPE.getLog().info("es canasto para nuevas cotizaciones: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio());
								
								//este atributo se utiliza en la secci\u00F3n de entregas
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(this.vectorCantidad[indiceDetalle]).longValue() - detallePedidoDTO.getNpContadorEntrega().longValue());
								
								//Obtengo las clasificaciones permitidas desde la tabla parametro
								String clasificacionesArticulosCambioPesos= CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request);
								
								//se inicializa el peso total aproximado
								//double pesoTotalAproximado = detallePedidoDTO.getArticuloDTO().getPesoAproximado().doubleValue() * detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
								double pesoTotalAproximado = detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta() * detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
								pesoTotalAproximado=Util.roundDoubleMath(pesoTotalAproximado,NUMERO_DECIMALES);
								//Se inicializa el peso del formulario solo cuado pedido actualizacion de precios
								if(intercambiarPrecios && detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()!=null && clasificacionesArticulosCambioPesos != null &&
										clasificacionesArticulosCambioPesos.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) && 
										detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
									this.vectorPesoActual[indicePavos] = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().toString();
								}
								
								//esta condici\u00F3n es para el caso de art\u00EDculos de peso variable que son pavos y no a cambiado la cantidad en el formulario
								if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()!=null && clasificacionesArticulosCambioPesos != null &&
										clasificacionesArticulosCambioPesos.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) && 
										detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
									//se verifica si existe peso registrado por el local
									if(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal() != null && detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() != 0){
										if(cambioCantidad){
											detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));
											detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(null);
										}else{
											detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal()));
										}	
									}else{
										detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));									
										
										if(!cambioCantidad){										
											if(session.getAttribute(CotizarReservarAction.DEBE_ACTUALIZAR)==null){										
												//funcion que valida y realiza el cambio de pesos ingresado desde formulario
												if(this.vectorPesoActual!=null){
													errorDeTolerancia = CotizacionReservacionUtil.validacionCambioPesosFormulario(detallePedidoDTO, request, pesoTotalAproximado, Double.valueOf(this.vectorPesoActual[indicePavos]));									
													
													if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS)==null || !session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS).equals("SI") ){
														if(errorDeTolerancia != null ){
															String[] rangos = errorDeTolerancia.toString().split(",");
															String tolerancia = rangos[0];
															String valorMaximo = rangos[1];
															String valorMinimo = rangos[2];												
															errors.add( "errorTolerancia",
																	new ActionMessage("errors.cotizarReservar.peso.pedido",
																			tolerancia.toString(), valorMaximo.toString(), valorMinimo.toString(),
																			detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo() != null ? detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras() : null));
															
														}
													}
												}
											}										 							
										}
									}
									validaPesos = true;
								}else{
									detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));
									validaPesos = false;
								}
								
								//incrementa en uno si es un articulo permitido para el cambio de pesos
								if(validaPesos){
									indicePavos++;
								}
								
								//esta condici\u00F3n es para el caso de pesos variables que no son pavos
								if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
									//se almacena el peso ingresado
									detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(this.vectorPeso[indiceDetalle]));
								}

								//se recalcula el valor total del canasto en algunos casos
								CotizacionReservacionUtil.recalcularPrecioReceta(detallePedidoDTO, request);
								//llamada a la funci\u00F3n que calcula el total del detalle
								CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, intercambiarPrecios,false);

								//control del stock para el detalle
								if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null){
									long stockArticulo = detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue();
									if(Long.parseLong(vectorCantidad[indiceDetalle])<=stockArticulo)
										detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoActivo);
									else
										detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);
								}
							}
						}
						/*
						//Para validar un objeto obsoleto
						//verifica si el art\u00EDculo es Obsoleto y si la cantidad solicitada es mayor al stock en el CD
						if(detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) &&
								detallePedidoDTO.getArticuloDTO().getNpStockArticulo() != null && detallePedidoDTO.getArticuloDTO().getNpStockArticulo() < Long.parseLong(vectorCantidad[indiceDetalle])){
							//a\u00F1ade los codigos de los art\u00EDculos encontrados 
							if(articulosObsoletos == null){
								articulosObsoletos = new StringBuffer();
							}
							articulosObsoletos.append(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().concat(", "));
						}
						//verifica si encontr\u00F3 alg\u00FAn art\u00EDculo obsoleto para mostrar el popup de advertencia
						if(articulosObsoletos != null){
							popUp = new UtilPopUp();
							popUp.setTituloVentana("Advertencia");
							popUp.setFormaBotones(UtilPopUp.OK);
							popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
							popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
							popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
							popUp.setMensajeVentana("Los art&iacute;los ".concat(articulosObsoletos.toString()).concat(" est&aacute;n considerados como OBSOLETOS en el SIC"));
							request.setAttribute(SessionManagerSISPE.POPUP, popUp);
							popUp = null;
						}*/
						
					}catch(Exception e){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
					}

					indiceDetalle++;
				}
				EntregaLocalCalendarioUtil.eliminarEntregasPedido(request,new ArrayList<DetallePedidoDTO>(detallePedido));
				if(warnings.size() > 0){
					session.setAttribute(WARNINGS_TEMPORAL, warnings);
				}
				
				if(existenCambios){
					
					//se actualiza los detalles consolidados
					ConsolidarAction.actualizarDetallesModificadosEnConsolidados(request, detallePedido);
				}
			}
		}
		
		//se elimina de sesion el parametro
		session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
	}
	
	/**
	 * 
	 * @param detallePedidoDTO
	 * @param errors
	 * @param request
	 * @param crearInfo
	 * @throws Exception 
	 */
public void eliminarEntregasPorModificacionDetallePrincipal(final DetallePedidoDTO detallePedidoDTO, ActionErrors errors, HttpServletRequest request, 
		boolean crearInfo, boolean eliminarSoloEntregasLocal) throws Exception{
		
		//coleccion de entregas a domicilio con camiones
		List<EntregaDetallePedidoDTO> entregasEliminarCol = new ArrayList<EntregaDetallePedidoDTO>();
		Integer contextoEntrega = Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"));
		Integer responsableTotalStock = Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
		Integer responsableParcialStock = Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"));
		int numeroBultos = 0;
		
		List<EntregaDetallePedidoDTO> entregasEliminadasCol = new ArrayList<EntregaDetallePedidoDTO>();
		
		if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			
			for(EntregaDetallePedidoDTO entDetPedDTO: detallePedidoDTO.getEntregaDetallePedidoCol()){
				
				if(!eliminarSoloEntregasLocal){
					//calculo los bultos 
					//valida las entregas que utilizan camiones,  a domicilio y pedir al centro de distribucion
					if(entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(contextoEntrega) 
							&& (entDetPedDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(responsableTotalStock) 
									|| entDetPedDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(responsableParcialStock))
							&& entDetPedDTO.getCantidadDespacho().longValue() > 0){
						
						numeroBultos =  UtilesSISPE.calcularCantidadBultos(entDetPedDTO.getCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
						entDetPedDTO.setNpCantidadBultos(numeroBultos);
						entregasEliminarCol.add(SerializationUtils.clone(entDetPedDTO));
						//se inicializan las variables
						EntregaLocalCalendarioUtil.inicializarVariablesSession(request, entDetPedDTO,	Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos")));
					}
				}
			
				//TODO Se comenta lo de beneficiario, porque no va a existir ese proceso
				//entregaDTO.setBeneficiarios(null);
				//entregaDTO.setNpContadorBeneficiario(0l);
				//variable para indicar como se debe realizar la eliminaci\u00F3n del costo
				request.getSession().setAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL, "ok");
				
				//se eliminan solo las entregas que fueron configuradas al local u otro local
				if(eliminarSoloEntregasLocal){
					if(entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() == MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.contextoEntrega.miLocal")
							|| entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() == MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")){
						
						//se eliminan los costos de las entregas
						this.eliminaCostoEntrega(entDetPedDTO, request);
						this.eliminaEntregasDetallePedido(errors, request, entDetPedDTO, detallePedidoDTO.getArticuloDTO().getNpTipoBodega());
						entregasEliminadasCol.add(entDetPedDTO);
					}
				}else{
					//se eliminan los costos de las entregas
					this.eliminaCostoEntrega(entDetPedDTO, request);
					this.eliminaEntregasDetallePedido(errors, request, entDetPedDTO, detallePedidoDTO.getArticuloDTO().getNpTipoBodega());
					entregasEliminadasCol.add(entDetPedDTO);
				}
			}
			//eliminar entregas del detalle
			Collection<EntregaPedidoDTO> entregasPedido = (Collection<EntregaPedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)==null ? new ArrayList<EntregaPedidoDTO>():(Collection<EntregaPedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO);
			for(EntregaPedidoDTO entPedDTO: entregasPedido){
				
				Collection<EntregaDetallePedidoDTO> entDetPedElim= new ArrayList<EntregaDetallePedidoDTO>();
				if(CollectionUtils.isNotEmpty(entPedDTO.getEntregaDetallePedidoCol())){
					
					entDetPedElim=CollectionUtils.select(entPedDTO.getEntregaDetallePedidoCol(), new Predicate() {
						public boolean evaluate(Object arg0) {
							
							EntregaDetallePedidoDTO entDetPed=(EntregaDetallePedidoDTO)arg0;
							return entDetPed.getCodigoArticulo().equals(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
						}
					});
					entPedDTO.getEntregaDetallePedidoCol().removeAll(entDetPedElim);
				}
			}
			
			//si en las entregas existen camiones, se habilitan los bultos de los camiones eliminados
			if(CollectionUtils.isNotEmpty(entregasEliminarCol)){
				EntregaLocalCalendarioUtil.eliminarCamionesDelPedido(entregasEliminarCol, request);
			}
			
			//eliminar entregas de la coleccion principal
			request.getSession().setAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO, entregasPedido);
			
			detallePedidoDTO.getEntregaDetallePedidoCol().removeAll(entregasEliminadasCol);
			
			long cantidadEstado = 0L; 
			long contadorEntregas = 0; 
			if(CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
				detallePedidoDTO.setEntregaDetallePedidoCol(null);
			}else{
				for(EntregaDetallePedidoDTO entDetPedDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
					cantidadEstado += entDetPedDTO.getCantidadEntrega(); 
					contadorEntregas ++;
				}
			}
			detallePedidoDTO.setNpContadorEntrega(cantidadEstado);
			detallePedidoDTO.setNpContadorDespacho(contadorEntregas);
			detallePedidoDTO.setNpCedulasBeneficiarios(null);
			detallePedidoDTO.setNpLocalesDireccionesEntrega(null);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado() - cantidadEstado);
			if(crearInfo){
				request.setAttribute(INFO_ENTREGA_ELIMINADA, detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
			}
		}
	}
	
	/**
	 * 
	 * @param detallePedidoDTO
	 * @param errors
	 * @param request
	 */
	public void verificaCantidadMayorAlaEstado(DetallePedidoDTO detallePedidoDTO, ActionErrors errors, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			session.setAttribute(INFO_CANTIDAD_MAYOR, detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
		}
	}
	
	/**
	 * @autor jacalderon
	 */
	public int validarTipoEntregas(ActionErrors errors,HttpServletRequest request) throws Exception
	{		
		String[] letrasConfigurarEntrega = {"A","B","C","D"};
		int posVector = 0;
		HttpSession session = request.getSession();
		PropertyValidator validador= new PropertyValidatorImpl();
		if((String)session.getAttribute(EntregaLocalCalendarioAction.MOSTRAROPCIONCANASTOSESPECIALES)!=null){
			validador.validateMandatory(errors, "opElaCanEsp", this.opElaCanEsp, "errors.seleccione.elaboracion.canastos",letrasConfigurarEntrega[posVector]);
			posVector++;
		}
		
		validador.validateMandatory(errors,"opTipoEntrega",this.opTipoEntrega,"errors.seleccione.tipoEntrega",letrasConfigurarEntrega[posVector]);
		posVector++;
		validador.validateMandatory(errors,"opStock",this.opStock,"errors.seleccione.stock",letrasConfigurarEntrega[posVector]);posVector++;		
		validador.validateMandatory(errors,"opLugarEntrega",this.opLugarEntrega,"errors.seleccione.lugarEntrega",letrasConfigurarEntrega[posVector]);
		
		return errors.size();
	}
	
	/**
	 * 
	 * @param request
	 * @param errors
	 * @param validador
	 * @throws Exception
	 */
	public void validarCotizacionReservacion(HttpServletRequest request, ActionErrors errors, 
			PropertyValidator validador, String accionActual)throws Exception{
		ActionMessages infos = new ActionMessages();
//		ActionMessages warnings = new ActionMessages();
		//se asigna el par\u00E1metro para el mensaje
		String accion = "";
		if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")) 
				|| accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))){
			accion = "RES";
		}else if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){ //PARA INDICAR QUE SI ES EL PEDIDO CONSOLIDADO NO VERIFICAR EL ALCANCE DE ARTICULOS CUANDO GRABA LA CONSOLIDACION
			accion = "COPE";
		}else{
			accion = "COT";
		}
		
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		
		//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
		Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		//verificarValoresDetallePedido(Collection detallePedido,ActionErrors errors, HttpServletRequest request, String accion);
		this.verificarValoresDetallePedido(colDetallePedidoDTO, errors, request, accion);
		
		
		if(session.getAttribute(CotizarReservarAction.ES_ENTIDAD_BODEGA)!=null){
			//si se ingres\u00F3 a recotizar o reservar desde la pantalla de b\u00FAsqueda NO se debe validar este campo
			//porque el local no se selecciona
			if(session.getAttribute(CrearReservacionAction.INGRESA_DIRECTAMENTE_RESERVAR)==null
					&& session.getAttribute(CrearRecotizacionAction.INGRESA_DIRECTAMENTE_RECOTIZAR) == null){
				//valida que el campor localResponsable sea requerido
				validador.validateMandatory(errors,"indiceLocalResponsable",this.indiceLocalResponsable,"errors.requerido","Local");
			}
		}
		
		boolean validarAbono = true;
		//si es una modificaci\u00F3n de reserva y el pedido ya tiene abonos, no se debe validar los campos del abono
		if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")) &&
				session.getAttribute(ModificarReservacionAction.PEDIDO_SIN_PAGO) == null){
			validarAbono = false;
		}
		
		if(errors.isEmpty()){
			//----------------------- validaci\u00F3n de los datos de la cabezera---------------------
			//se valida que el tipo de documento sea correcto y sea obligatorio siempre y cuando la accion sea diferente de consolidado
			if(!accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
				validarTipoDocumento(validador, errors, request);
				String  valorComboTipoDoc = getTipoDocumento();
				if((valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE))
						&& this.nombrePersona==null){
					request.getSession().removeAttribute(ContactoUtil.PERSONA);
					request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
					errors.add("nombreContacto",new ActionMessage("message.integracion.corp.buscar.cliente"));
				}
//				else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
				else if((valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)==null){
					if(this.nombreEmpresa==null){
						request.getSession().removeAttribute(ContactoUtil.PERSONA);
						request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
						errors.add("nombreEmpresa",new ActionMessage("message.integracion.corp.buscar.cliente"));
					}
					else{
						this.tipoDocumentoContacto=null;
						if(session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP)==null){
							errors.add("localizacionNoAsignada", new ActionMessage("error.contactos.localizacion.noAsignada"));
						}else{
							ContactoUtil.asignarDatosLocalizacion((LocalizacionDTO)session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP), this);
						}
					}
				}
			}
			//se valida que existan art\u00EDculos en el detalle
			if(colDetallePedidoDTO==null || colDetallePedidoDTO.isEmpty()){
				//si el detalle se encuentra vacio.
				errors.add("detalle",new ActionMessage("errors.detalle.requerido"));
				this.cambiarAlTabPedido(session, request);
			}
			getNombreContacto();
			getNombreContactoTxt();
		}
		
		//SE VALIDA LA LOGICA DE LA RESERVACION
		if(this.botonRegistrarReservacion!=null && errors.isEmpty()){
			boolean existenArtPesoVariable = false;
			//se realiza la validaci\u00F3n de las entregas
			if(errors.isEmpty() && colDetallePedidoDTO!=null){
				LogSISPE.getLog().info("por validar las entregas");
				
				for(DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO){
					//control para los art\u00EDculos que son pavos
					if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)
							|| detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
						existenArtPesoVariable = true;
					}
					
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
							request.setAttribute(ERROR_ENTREGAS, "ok");
							//se blanquean los errores para que vaya a la acci\u00F3n y haga el forward a la 
							//p\u00E1gina de entregas
							errors.clear();
							return; //se termina el m\u00E9todo
						}
					}else{
						infos.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
						//se realizan las asignaciones correspondientes para observar la secci\u00F3n de entregas
						session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/entregaArticulos.jsp");
						//se crea una variable para indicar que se produjo un error por entregas incompletas
						request.setAttribute(ERROR_ENTREGAS, "ok");
						//se blanquean los errores para que vaya a la acci\u00F3n y haga el forward a la 
						//p\u00E1gina de entregas
						errors.clear();
						return; //se termina el m\u00E9todo
					}
				}
			}

			//--------------------------------------------------------------------------------------------------------
			//se realiza la validaci\u00F3n del abono, para el caso de la modificaci\u00F3n de la reserva se continua 
			//solamente si no se han realizado pagos
			//--------------------------------------------------------------------------------------------------------
			if(errors.isEmpty() && validarAbono){
				
				LogSISPE.getLog().info("por validar formato del abono");
				//se valida la fecha de entrega y el valos del Abono
				validador.validateDouble(errors,"valorAbono",this.valorAbono,true,0,9999999999.99,"errors.formato.double","Abono");

				if(errors.isEmpty()){
					//se formatea el valor del abono con dos decimales antes de almacenarlo
					Double valorAbonoSistema = Util.roundDoubleMath((Double)session.getAttribute(CotizarReservarAction.VALOR_ABONO), NUMERO_DECIMALES);
					Double valorAbonoManual = Util.roundDoubleMath(Double.valueOf(this.valorAbono),NUMERO_DECIMALES);
					Double total = Util.roundDoubleMath(this.total, NUMERO_DECIMALES);
					
					if(valorAbonoManual.doubleValue() > total.doubleValue()){
						LogSISPE.getLog().info(" *2 ");
						//si el valor que se ingres\u00F3 en el abono es mayor que el total del pedido
						errors.add("valorAbono",new ActionMessage("errors.pedido.abonoExcedido",valorAbonoManual,total));
//					}else if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion")) == null){
					}else if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO)){
						//se varifica el abono manual
						LogSISPE.getLog().info("valorAbonoManual: {}",valorAbonoManual);
						LogSISPE.getLog().info("valorAbonoSistema: {}",valorAbonoSistema);
						if(valorAbonoManual.doubleValue() < valorAbonoSistema.doubleValue()){
							LogSISPE.getLog().info(" *3 ");
							errors.add("valorAbono",new ActionMessage("message.reservacion.valorMinimoAbono"));
						}
					}
					
					//si existen art\u00EDculos de peso variable en el detalle del pedido se realiza otro tipo de validaci\u00F3n
					if(errors.isEmpty() && existenArtPesoVariable){
						//solo si es una reservaci\u00F3n
						ParametroDTO parametroAbonoPesoVariable = null;
						String porcentajeMaxAbonoPV = (String)session.getAttribute(CotizarReservarAction.PORCENTAJE_ABONO_PESO_VARIABLE);
						if(porcentajeMaxAbonoPV == null){
							parametroAbonoPesoVariable = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentajeAbonoPesoVariable", request);
							if(parametroAbonoPesoVariable.getValorParametro()!=null){
								session.setAttribute(CotizarReservarAction.PORCENTAJE_ABONO_PESO_VARIABLE, parametroAbonoPesoVariable.getValorParametro());
								porcentajeMaxAbonoPV = parametroAbonoPesoVariable.getValorParametro();
							}
						}

						//se realiza la verificaci\u00F3n con el total a ser abonado
						if(porcentajeMaxAbonoPV != null){
							Double valorMaximoAbonar = Util.roundDoubleMath(total.doubleValue() * Double.parseDouble(porcentajeMaxAbonoPV)/100, 2);
							if(valorAbonoManual.doubleValue() > valorMaximoAbonar.doubleValue()){
								errors.add("valorAbono", new ActionMessage("errors.valorDeAbonoPesosVariables", porcentajeMaxAbonoPV+"%", valorMaximoAbonar));
							}
						}
					}
				}
			}
			
			//SE OBTIENEN LAS CLASIFICACIONES A LAS QUE TIENEN ACCESO LOS USUARIOS DE COMPRAS
			CotizacionReservacionUtil.obtenerClasificacionesEspecialesCompras(request);
		}
		
		//se asigna el par\u00E1metro para el mensaje
		/*String accion = "";
		if(this.botonRegistrarReservacion!=null)
			accion = "RES";
		else
			accion = "COT";*/

		//\u00FAltimo paso de la validaci\u00F3n
		if(this.total.doubleValue() > 0){
			//se verifican las cantidades de los detalles del pedido
			this.verificarValoresDetallePedido(colDetallePedidoDTO, errors, request, accion);
		}else{
			//se muestra un mensaje de error
			errors.add("totalPedido",new ActionMessage("errors.pedido.total"));
		}
	}
	
	/**
	 * Carga la configuracion inicial de las entregas cuando se va a modificar la reserva
	 * @author jacalderon
	 */
	public boolean cargarConfiguracionInicial(ActionErrors errors, HttpServletRequest request){
		
		HttpSession session=request.getSession();
		boolean eliminarEntregas=false; //indica si hubieron entregas con fechas de despacho menor o igual al dia actual
		//se obtiene el c\u00F3digo del local almacenado el el campo "localResponsable"
		Integer local = 0;
		VistaPedidoDTO vistaPedidoDTO = null;
		if(this.localResponsable!=null){
			//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
			//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
			local = Integer.parseInt(this.getLocalResponsable().split(SEPARADOR_TOKEN)[0].trim());
		}
		
		try{
			//Obtengo el local de origen
			VistaLocalDTO vistaLocalDTO = new VistaLocalDTO();
			vistaLocalDTO.getId().setCodigoLocal(local);
			vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
			VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
			session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN, vistaLocal);
			//por defecto el local destino es el mismo de origen
			session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO, vistaLocal);
			LogSISPE.getLog().info("LOCAL DESTINO: {}" , vistaLocal.getId().getCodigoLocal());
			/***********************************************************************************************/
			//si la entrega se accede desde un pedido recotizado o caducado se debe configurar la CD del calendario
			vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			Collection<DetallePedidoDTO> detallePedidoCol=(ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			
			//Collection<EntregaDTO> colEntregasCalendario = new ArrayList<EntregaDTO>();
			Collection<EntregaDetallePedidoDTO> colEntregasDetalleCalendario = new ArrayList<EntregaDetallePedidoDTO>();
			
			//colecci\u00F3n de c\u00F3digos de bodegas activadas para entregas a domicilio
			Collection<Integer> codigosResponsabilidadCD = UtilesSISPE.obtenerCodigosConsultaCD();
			
			//Cuando se viene a recotizar, reservar, cambiar una reserva
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL)==null 
					&& vistaPedidoDTO!=null && !vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado"))){
				LogSISPE.getLog().info("va a cargar las varibles np de las entregas");
				//Subo a sesion la entidad responsable
				session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE,vistaPedidoDTO.getEntidadResponsable());
				//cargo el calendarioDiaLocal de las entregas que tienen despacho
				Collection<CalendarioDiaLocalDTO> calendarioDialLocalDTOCol=new ArrayList<CalendarioDiaLocalDTO>();//coleccion para almacenar las configuraciones del calendario
				boolean existeConfiguracion=false;//indica si la configuracion del un dia en especial ya fue cargada
				//Collection<EntregaDTO> entregasEliminar=null;
				Collection<EntregaDetallePedidoDTO> entregasDetalleEliminar=null;
				
				boolean esResponsableBodega = false;
				//en base a la entidad responsable es bodega se carga el el par\u00E1metro para obtener la fecha m\u00EDnima de entrega a ser comparado
				//en cada entrega
				if(vistaPedidoDTO.getEntidadResponsable().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
					esResponsableBodega = true;
				}
				
				String codigoFecMinEntCD = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.fechaEntrega");
				String codigoFecMinDesLoc = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal");
				String codigoRangoDiasDespacho = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho");
				
				//se obtienen los par\u00E1metros necesarios
				ParametroDTO consultaParametroDTO = new ParametroDTO();
				consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaParametroDTO.getId().setCodigoParametro(codigoFecMinEntCD.concat(",").concat(codigoFecMinDesLoc).concat(",").concat(codigoRangoDiasDespacho));
				Collection<ParametroDTO> colParametroDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
				
				int rangoDiasParaDespachar = 0;
				int cantidadDiasFechaMinimaDespacho = 0;
				int cantidadDiasFechaMinimaEntrega = 0;
				//se itera la colecci\u00F3n de par\u00E1metros consultados
				for(ParametroDTO parametroDTO: colParametroDTO){
					if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinEntCD) && esResponsableBodega){
						cantidadDiasFechaMinimaEntrega = Integer.parseInt(parametroDTO.getValorParametro());
					}else if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinDesLoc)){
						cantidadDiasFechaMinimaDespacho = Integer.parseInt(parametroDTO.getValorParametro());
						if(!esResponsableBodega){
							//solo si la entidad responsable es el local
							cantidadDiasFechaMinimaEntrega = cantidadDiasFechaMinimaDespacho + 1;
						}
					}else if(parametroDTO.getId().getCodigoParametro().equals(codigoRangoDiasDespacho)){
						//variable para el parametro del dia minino de despacho a local
						rangoDiasParaDespachar = Integer.parseInt(parametroDTO.getValorParametro());
						session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, parametroDTO.getValorParametro());
					}
				}
				
				Timestamp fechaActual = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
				Timestamp fechaMinimaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, cantidadDiasFechaMinimaEntrega);
				Timestamp fechaMinimaDespacho = ManejoFechas.sumarDiasTimestamp(fechaActual, cantidadDiasFechaMinimaDespacho);
				Timestamp fechaReferenciaEntrega = null;
				Timestamp fechaReferenciaDespacho = null;
				
				//cargo el calendarioDiaLocal para los dias que tienen despachos
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoCol){
					
					if(detallePedidoDTO.getEntregaDetallePedidoCol()!=null){
						long cantidadEntregada=0;
						long cantidadDespachada=0;
						//Coleccion de entregas a eliminar
						//entregasEliminar = new ArrayList<EntregaDTO>();
						entregasDetalleEliminar = new ArrayList<EntregaDetallePedidoDTO>();
							for(EntregaDetallePedidoDTO entregaDetalle:detallePedidoDTO.getEntregaDetallePedidoCol()){
								
								entregaDetalle.setNpTipoBodega(detallePedidoDTO.getArticuloDTO().getNpTipoBodega());
								
								LogSISPE.getLog().info("**local de entrega: **{}" , entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
								LogSISPE.getLog().info("**codigo local sector: **{}" , entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector());
								LogSISPE.getLog().info("entidad responsable: {}" , vistaPedidoDTO.getEntidadResponsable());
								LogSISPE.getLog().info("lugar de entrega: {}" , entregaDetalle.getEntregaPedidoDTO().getCodigoContextoEntrega());
								
								LogSISPE.getLog().info("lugar entre. property: {}" , MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"));
								
								//Si la entrega tiene una fecha de despacho mayor a la fecha actual la cargo si no la elimino
								//Si la entrega tiene despacho a bodega verificar con la fecha minima de entrega
								if(entregaDetalle.getCantidadDespacho().longValue() > 0 
										&& (entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null 
												|| (entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector()!=null 
												&& !codigosResponsabilidadCD.contains(entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector())))){
									
									//no me interesa la fecha de entrega, solo la fecha m\u00EDnima de despacho
									fechaReferenciaEntrega = entregaDetalle.getEntregaPedidoDTO().getFechaEntregaCliente();
									//se crea la fecha m\u00EDnima de despacho restando el rango de d\u00EDas para despacho
									fechaReferenciaDespacho = ManejoFechas.sumarDiasTimestamp(fechaMinimaEntrega, (-1)*rangoDiasParaDespachar);
									
									//si la fecha de referencia para el despacho es menor que la fecha m\u00EDnima para un despacho
									if(fechaReferenciaDespacho.getTime() < fechaMinimaDespacho.getTime()){
										fechaReferenciaDespacho = fechaMinimaDespacho;
									}
									
								}else{
									//en este caso solo me interesa la fecha de entrega
									fechaReferenciaEntrega = fechaActual;
									//no interesa la fecha de despacho porque no se solicit\u00F3 nada al CD
									fechaReferenciaDespacho = entregaDetalle.getEntregaPedidoDTO().getFechaDespachoBodega();
								}
								
								LogSISPE.getLog().info("Fecha de entrega: {}" , entregaDetalle.getEntregaPedidoDTO().getFechaEntregaCliente());
								LogSISPE.getLog().info("Fecha referencia entrega: {}" , fechaReferenciaEntrega);
								LogSISPE.getLog().info("Fecha de despacho: {}" , entregaDetalle.getEntregaPedidoDTO().getFechaDespachoBodega());
								LogSISPE.getLog().info("Fecha referencia despacho: {}" , fechaReferenciaDespacho);
								
								//la fecha de entrega debe ser mayor o igual a la fecha m\u00EDnima de entrega y 
								//la fecha de despacho debe ser mayor o igual a la fecha m\u00EDnima de despacho para la entrega
								//No eliminar las entregas peticion de Oliver y Helen
//								if(entregaDetalle.getEntregaPedidoDTO().getFechaEntregaCliente().compareTo(fechaReferenciaEntrega)>=0
//										&& entregaDetalle.getEntregaPedidoDTO().getFechaDespachoBodega().compareTo(fechaReferenciaDespacho)>=0){
									
									LogSISPE.getLog().info("La entrega si es agregada");
									
									//Si la entrega tiene como lugar de entrega el domicilio voy a cargar direccion y costo
									if(entregaDetalle.getEntregaPedidoDTO().getCodigoContextoEntrega().toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
										EntregaLocalCalendarioUtil.cargaDireccionesYCostos(request, entregaDetalle, this.subTotal);
										LogSISPE.getLog().info("codigo de la direccion: {}", entregaDetalle.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
									}
									cantidadEntregada=cantidadEntregada+entregaDetalle.getCantidadEntrega().longValue();
									
									//si la entrega tiene despachos
									LogSISPE.getLog().info("Cantidad de despacho: {}" , entregaDetalle.getCantidadDespacho());
									LogSISPE.getLog().info("Codigo Local de entrega: {}" , entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
									LogSISPE.getLog().info("Codigo Local sector: {}", entregaDetalle.getEntregaPedidoDTO().getCodigoDivGeoPol());
									
									int numeroBultos=0;
									if(entregaDetalle.getEntregaPedidoDTO().getCodigoObtenerStock()!=null && entregaDetalle.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()== Integer.valueOf(ConstantesGenerales.STOCK_ENTREGA_DOMICILIO)){
										//si se configura una entrega pidiendo al CD, calculamos el numero de bultos
										numeroBultos=UtilesSISPE.calcularCantidadBultos(entregaDetalle.getNpCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
										LogSISPE.getLog().info("numero de bultos existente en la entrega: {}" , numeroBultos);
										entregaDetalle.setNpCantidadBultos(Integer.valueOf(numeroBultos));
									}
									
									if(entregaDetalle.getCantidadDespacho().longValue()>0 && (entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null
											|| (entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector()!=null 
											&& !codigosResponsabilidadCD.contains(entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector())))){
										
										//int numeroBultos=UtilesSISPE.calcularCantidadBultos(entregaDetalle.getNpCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
										//LogSISPE.getLog().info("numero de bultos existente en la entrega: {}" , numeroBultos);
										//entregaDetalle.setNpCantidadBultos(Integer.valueOf(numeroBultos));
										//busco si ya fue cargada la configuracion del calendario
										existeConfiguracion = false;
										for(CalendarioDiaLocalDTO calendarioDiaLocalDTO:calendarioDialLocalDTOCol){
											Calendar fecha=new GregorianCalendar();
											fecha.setTime(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
											//si las fechas de despacho coinciden
											if(entregaDetalle.getEntregaPedidoDTO().getFechaDespachoBodega().equals(new Timestamp(fecha.getTime().getTime())) 
													&& ((entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null 
													&& entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().toString().equals(calendarioDiaLocalDTO.getId().getCodigoLocal().toString())) 
													|| (entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector()!=null 
													&& entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector().toString().equals(calendarioDiaLocalDTO.getId().getCodigoLocal())))){
												
												entregaDetalle.getEntregaPedidoDTO().setCalendarioDiaLocalDTO(calendarioDiaLocalDTO);
												existeConfiguracion=true;
												
											}
										}
										
										//si no encontro la configuracion la carga
										if(!existeConfiguracion){
											//Obtengo el calendario del dia de la entrega
											CalendarioDiaLocalID calendarioDiaLocalID=new CalendarioDiaLocalID();
											calendarioDiaLocalID.setCodigoCompania(entregaDetalle.getId().getCodigoCompania());
											if(entregaDetalle.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
												
												//para el caso de canastos especiales que elabora el CD
												if(entregaDetalle.getEntregaPedidoDTO().getElaCanEsp() != null 
														&& entregaDetalle.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
													calendarioDiaLocalID.setCodigoLocal(Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos")));
												}else{
													calendarioDiaLocalID.setCodigoLocal(entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
												}
											}else{
												calendarioDiaLocalID.setCodigoLocal(Integer.valueOf(entregaDetalle.getEntregaPedidoDTO().getCodigoLocalSector()));
											}
//											Date mes=DateManager.getYMDDateFormat().parse(entregaDetalle.getEntregaPedidoDTO().getFechaDespachoBodega().toString());
											Date mes=DateManager.getYMDDateFormat().parse(entregaDetalle.getEntregaPedidoDTO().getFechaEntregaCliente().toString());
											calendarioDiaLocalID.setFechaCalendarioDia(mes);
											CalendarioDiaLocalDTO calendarioDiaLocalDTO=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,null);
											entregaDetalle.getEntregaPedidoDTO().setCalendarioDiaLocalDTO(calendarioDiaLocalDTO);
											LogSISPE.getLog().info("****CD***** {}" , calendarioDiaLocalDTO.getCantidadDisponible());
											calendarioDialLocalDTOCol.add(calendarioDiaLocalDTO);
										}
										cantidadDespachada=cantidadDespachada+entregaDetalle.getCantidadDespacho();
										
									}else{
										//se asigna 0 al numero de bultos
										entregaDetalle.setNpCantidadBultos(numeroBultos);
									}
									
//									}else{
//									LogSISPE.getLog().info("La entrega no es agregada");
									//No eliminar las entregas peticion de Oliver y Helen
//									if(entregaDetalle.getFechaRegistroDespacho()==null){											
//										entregasDetalleEliminar.add(entregaDetalle);
//									}
//								}
								
								if(entregaDetalle.getEntregaPedidoDTO().getCalendarioDiaLocalDTO() != null){
									colEntregasDetalleCalendario.add(entregaDetalle);
								}
							}
						
						//Asigno los valores np del detalle del pedido
						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantidadEntregada));
						detallePedidoDTO.setNpContadorEntrega(Long.valueOf(cantidadEntregada));
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantidadDespachada));
						detallePedidoDTO.setNpContadorDespacho(new Long(cantidadDespachada));
						//Si existieron entregas que deben ser eliminadas
						if(entregasDetalleEliminar.size()>0){
							LogSISPE.getLog().info("numero de entregas cabecera antes de eliminar: {}", detallePedidoDTO.getEntregaDetallePedidoCol().size());
							detallePedidoDTO.getEntregaDetallePedidoCol().removeAll(entregasDetalleEliminar);
							LogSISPE.getLog().info("numero de entregas cabecera despues de eliminar: {}" , detallePedidoDTO.getEntregaDetallePedidoCol().size());
							if(!vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))){
								eliminarEntregas=true;
							}
						}
					}
					
					session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedidoCol);
					
				}
			}
			
			//Cuando se viene a recotizar un pedido activo o caducado
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL)==null && vistaPedidoDTO!=null
				&& (vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado")) 
						|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")))){
				LogSISPE.getLog().info("**va a ver la configuracion del calendario**");
				//asigno una coleccion auxiliar para reunir todas las entregas
				//la idea es sumar todas las entregas hechas en una misma fecha de despacho y solo llamar una vez a la funcion que carga el calendario
				long numeroBultos=0;//total de numero de bultos en una misma fecha de despacho
				boolean auxiliar=true;//indica hasta donde se encuentran entregas con fecha de despacho y local iguales
				CalendarioDiaLocalDTO calendarioDiaLocalDTOAnt=null;
				
				Collection<EntregaDetallePedidoDTO> nuevasEntregasDetalle=null;
				if(colEntregasDetalleCalendario.size()>0){
					LogSISPE.getLog().info("tamano de las entrega: {}" , colEntregasDetalleCalendario.size());
					//ordeno la coleccion de entregas por fechas
					nuevasEntregasDetalle = ColeccionesUtil.sort(colEntregasDetalleCalendario, 
						ColeccionesUtil.ORDEN_ASC,"entregaPedidoDTO.fechaDespachoBodega","entregaPedidoDTO.fechaEntregaCliente","entregaPedidoDTO.codigoAreaTrabajoEntrega","entregaPedidoDTO.codigoDivGeoPol");
				}
				if(nuevasEntregasDetalle!=null && nuevasEntregasDetalle.size()>0){
					//Objeto que me sirve para hacer la comparacion
					EntregaDetallePedidoDTO entregaDetalleDTO = nuevasEntregasDetalle.iterator().next();
					//debo borrar la primera entrega para que no la vuelva a comparar
					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=new CalendarioConfiguracionDiaLocalDTO();
					//Itero la coleccion de las entregas ordenadas para sumar todos los bultos en una misma fecha de despacho
					for (EntregaDetallePedidoDTO entregaDetalleDTOAux : nuevasEntregasDetalle) {
						
						//Si encuentro una entrega con la misma fecha de despacho en un mismo local
						LogSISPE.getLog().info("*********************************");
						LogSISPE.getLog().info("cantidad entrega1: {}" , entregaDetalleDTO.getCantidadEntrega().longValue());
						LogSISPE.getLog().info("cantidad entrega2: {}" , entregaDetalleDTOAux.getCantidadEntrega().longValue());
						LogSISPE.getLog().info("cantidad bultos1: {}" , entregaDetalleDTO.getNpCantidadBultos());
						LogSISPE.getLog().info("cantidad bultos2: {}" , entregaDetalleDTOAux.getNpCantidadBultos());
						LogSISPE.getLog().info("codigo local entrega1: {}" , entregaDetalleDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
						LogSISPE.getLog().info("codigo local entrega2: {}" , entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
						LogSISPE.getLog().info("codigo local sector1: {}" , entregaDetalleDTO.getEntregaPedidoDTO().getCodigoLocalSector());
						LogSISPE.getLog().info("codigo local sector2: {}" , entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoLocalSector());
						LogSISPE.getLog().info("fechaDespacho1: {}" , entregaDetalleDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
						LogSISPE.getLog().info("fechaDespacho2: {}" , entregaDetalleDTOAux.getEntregaPedidoDTO().getFechaDespachoBodega());
						if(entregaDetalleDTO.getEntregaPedidoDTO().getFechaDespachoBodega().equals(entregaDetalleDTOAux.getEntregaPedidoDTO().getFechaDespachoBodega()) &&
							entregaDetalleDTO.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetalleDTOAux.getEntregaPedidoDTO().getFechaEntregaCliente()) && 
							(((entregaDetalleDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null && entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null 
								&& entregaDetalleDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().intValue()==entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().intValue()))
								||(entregaDetalleDTO.getEntregaPedidoDTO().getCodigoLocalSector()!=null && !codigosResponsabilidadCD.contains(entregaDetalleDTO.getEntregaPedidoDTO().getCodigoDivGeoPol()) 
									&& entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoLocalSector()!=null 
									&& entregaDetalleDTO.getEntregaPedidoDTO().getCodigoLocalSector().intValue()
									== entregaDetalleDTOAux.getEntregaPedidoDTO().getCodigoLocalSector().intValue()))){
							numeroBultos=numeroBultos+entregaDetalleDTOAux.getNpCantidadBultos().intValue();
							LogSISPE.getLog().info("numero de bultos: {}" , numeroBultos);
						}else{
							auxiliar=false;
						}
						
						//if(!iter.hasNext() || !auxiliar){
						if(!auxiliar){
							LogSISPE.getLog().info("va a ver la configuracion del calendario");
							//se llama a la funci\u00F3n que actualiza la configuraci\u00F3n del calendario
							this.actualizarConfiguracionCalendario(numeroBultos, entregaDetalleDTO, request, errors, calendarioDiaLocalDTOAnt, calendarioConfiguracionDiaLocalDTO);
							//Asigno la nueva entrega a comparar
							entregaDetalleDTO=entregaDetalleDTOAux;
							numeroBultos = entregaDetalleDTO.getNpCantidadBultos().longValue();
							auxiliar=true;
						}
					}
					//se llama a la funci\u00F3n que actualiza la configuraci\u00F3n del calendario para que procese el \u00FAltimo elemento
					this.actualizarConfiguracionCalendario(numeroBultos, entregaDetalleDTO, request, errors, calendarioDiaLocalDTOAnt, calendarioConfiguracionDiaLocalDTO);
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL,calendarioConfiguracionDiaLocalDTO);
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		return eliminarEntregas;
	}
	
	/**
	 * @author jacalderon
	 * @param numeroBultos
	 * @param entregaDetalleDTO
	 * @param request
	 * @param errors
	 * @param calendarioDiaLocalDTOAnt
	 */
	private void actualizarConfiguracionCalendario(long numeroBultos, EntregaDetallePedidoDTO entregaDetalleDTO, HttpServletRequest request, ActionErrors errors,
			CalendarioDiaLocalDTO calDiaLocDTOAnt, CalendarioConfiguracionDiaLocalDTO calConDiaLocDTO){
		
		HttpSession session = request.getSession();
		CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = calConDiaLocDTO;
		CalendarioDiaLocalDTO calendarioDiaLocalDTOAnt = calDiaLocDTOAnt;
		
		if(numeroBultos>0 && entregaDetalleDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null){
			//llamo a la funcion que me carga los datos del calendario
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX)!=null){
				LogSISPE.getLog().info("si tiene valor el calendarioConfiguracionDialLocal");
				calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX);
			}
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=entregaDetalleDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO();
			try{
				LogSISPE.getLog().info("calendarioConfiguracionDiaLocalDTO: {}" ,calendarioConfiguracionDiaLocalDTO);
				LogSISPE.getLog().info("CodigoCompania: {}" , calendarioDiaLocalDTO.getId().getCodigoCompania());
				LogSISPE.getLog().info("CodigoLocal: {}" , calendarioDiaLocalDTO.getId().getCodigoLocal());
				LogSISPE.getLog().info("FechaCalendarioDia: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
				LogSISPE.getLog().info("CapacidadNormal: {}" , calendarioDiaLocalDTO.getCapacidadNormal());
				LogSISPE.getLog().info("CapacidadAdicional: {}" , calendarioDiaLocalDTO.getCapacidadAdicional());
				LogSISPE.getLog().info("numeroBultos: {}" , numeroBultos);
				LogSISPE.getLog().info("CantidadAcumulada: {}" , calendarioDiaLocalDTO.getCantidadAcumulada());
				LogSISPE.getLog().info("fecha de entrega: {}" , entregaDetalleDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
				
				//cantidades informativas
				Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
				//cantidad para frio
				CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
				cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
				cantidadFrio.setCantidad((entregaDetalleDTO.getNpTipoBodega() != null && entregaDetalleDTO.getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? numeroBultos : 0L );
				cantidadesInformativas.add(cantidadFrio);
				//cantidad para seco
				CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
				cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
				cantidadSeco.setCantidad((entregaDetalleDTO.getNpTipoBodega() != null && entregaDetalleDTO.getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? numeroBultos : 0L );
				cantidadesInformativas.add(cantidadSeco);
				
				calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),false,cantidadesInformativas);
				/***Cargo los dias a los que les debo modifcar la cantidad acumulada***/
				//Si coincide el dia del mes y el local con el calendario seleccionado ya no lo vuelvo a cargar
				if(calendarioDiaLocalDTOAnt!=null){
					GregorianCalendar mesCalAnt=new GregorianCalendar();
					GregorianCalendar mesCalAct=new GregorianCalendar();
					mesCalAnt.setTime(calendarioDiaLocalDTOAnt.getId().getFechaCalendarioDia());
					mesCalAct.setTime(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
					LogSISPE.getLog().info("localAnt: {}" , calendarioDiaLocalDTOAnt.getId().getCodigoLocal());
					LogSISPE.getLog().info("localAct: {}" , calendarioDiaLocalDTO.getId().getCodigoLocal());
					LogSISPE.getLog().info("mesAnt: {}" , mesCalAnt.get(Calendar.MONTH));
					LogSISPE.getLog().info("mesAct: {}" , mesCalAct.get(Calendar.MONTH));
					LogSISPE.getLog().info("a\u00F1onAnt: {}" , mesCalAnt.get(Calendar.YEAR));
					LogSISPE.getLog().info("a\u00F1onAct: {}" , mesCalAct.get(Calendar.YEAR));
					if (!(calendarioDiaLocalDTOAnt.getId().getCodigoLocal().intValue()==calendarioDiaLocalDTO.getId().getCodigoLocal().intValue()
									&& mesCalAnt.get(Calendar.MONTH) == mesCalAct.get(Calendar.MONTH)
									&& mesCalAnt.get(Calendar.YEAR) == mesCalAct.get(Calendar.YEAR))) {
						LogSISPE.getLog().info("va a cargar calendario desde el else");
						LocalID localID=new LocalID();
						localID.setCodigoCompania(entregaDetalleDTO.getId().getCodigoCompania());
						localID.setCodigoLocal(entregaDetalleDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO().getId().getCodigoLocal());
						EntregaLocalCalendarioUtil.obtenerCalendario(request, localID, errors, calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), null, null, this, null);
						calendarioDiaLocalDTOAnt=calendarioDiaLocalDTO;
					}
				}else{
					LocalID localID = new LocalID();
					localID.setCodigoCompania(entregaDetalleDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO().getId().getCodigoCompania());
					localID.setCodigoLocal(entregaDetalleDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO().getId().getCodigoLocal());
					EntregaLocalCalendarioUtil.obtenerCalendario(request, localID, errors, calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), null, null, this, null);
					calendarioDiaLocalDTOAnt=calendarioDiaLocalDTO;
				}
				Object[] calendarioDiaLocalDTOOBJ=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
				int dia=-1;
				//Busco el dia de despacho
				LogSISPE.getLog().info("***********Va a buscar el dia de despacho*******************");
				for(int indice=0;indice<calendarioDiaLocalDTOOBJ.length;indice++){
					CalendarioDiaLocalDTO calDia=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indice];
					if(calDia.getId().getFechaCalendarioDia().equals(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())){
						dia=indice;
						break;
					}
				}
				LogSISPE.getLog().info("dia: {}" , dia);
				LogSISPE.getLog().info("fecha de despacho: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
				LogSISPE.getLog().info("fecha de entrega: {}" , entregaDetalleDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
				LogSISPE.getLog().info("local: {}", calendarioDiaLocalDTO.getId().getCodigoLocal());
				EntregaLocalCalendarioUtil.cargaDiasModificarCA(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), entregaDetalleDTO.getEntregaPedidoDTO().getFechaEntregaCliente(), this, request, dia);
				//Obtengo los dias que debo modificar su capacidad acumulada
				Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
				for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
					//cantidades informativas
					Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
					//cantidad para frio
					CantidadCalendarioDiaLocalDTO cantidadFrioAux = new CantidadCalendarioDiaLocalDTO();
					cantidadFrioAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
					cantidadFrioAux.setCantidad((entregaDetalleDTO.getNpTipoBodega() != null && entregaDetalleDTO.getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? numeroBultos : 0L );
					cantidadesInformativasAux.add(cantidadFrioAux);
					//cantidad para seco
					CantidadCalendarioDiaLocalDTO cantidadSecoAux= new CantidadCalendarioDiaLocalDTO();
					cantidadSecoAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
					cantidadSecoAux.setCantidad((entregaDetalleDTO.getNpTipoBodega() != null && entregaDetalleDTO.getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? numeroBultos : 0L );
					cantidadesInformativasAux.add(cantidadSecoAux);
					
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),false,cantidadesInformativasAux);
				}
				session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX,calendarioConfiguracionDiaLocalDTO);
			}catch(Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
		}
	}
	
	/**
	 * Permite validar los campos para busqueda
	 * @author jacalderon
	 * @param errors
	 * @param request
	 * 
	 * @return Devuelve un int.
	 */
	public int validarBusqueda(ActionErrors errors,HttpServletRequest request){
		LogSISPE.getLog().info("entra a validar la busqueda");
		PropertyValidator validar= new PropertyValidatorImpl();
		HttpSession session=request.getSession();
		if(request.getParameter("botonAceptarDatos")!=null){
			if(session.getAttribute(EntregaLocalCalendarioAction.ENTIDADRESPONSABLELOCAL)==null)
				validar.validateFecha(errors,"buscaFecha",this.buscaFecha,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute("ec.com.smx.calendarizacion.fechaBuscada")));
			if(errors.size()>0)
				this.buscaFecha=DateManager.getYMDDateFormat().format((Date)session.getAttribute("ec.com.smx.calendarizacion.fechaBuscada"));
			//en el caso de no ingresar un local para la busqueda directa
			if(this.getOpLugarEntrega()!=null && this.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal"))){
				if((this.local==null || this.local.equals("")) && (this.listaLocales==null || this.listaLocales.equals("") || this.listaLocales.equals("ciudad"))){
					LogSISPE.getLog().info("va a validar si existe ingresado un local");
					errors.add("locales",new ActionMessage("errors.local.requerido"));
				}
			}
			validar.validateFecha(errors,"fechaEntregaCliente",this.fechaEntregaCliente,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha de Entrega");
		}
		/*if(errors.size()==0){
			validar.validateLong(errors,"horas",this.horas,true,0,0,"errors.horas.requerido");
        	if(errors==null || errors.size()==0){
            	if(new Integer(this.horas).intValue()>=24){
            		errors.add("horasError",new ActionMessage("errors.horas.requerido"));
            	}
        	}
        	
        	validar.validateLong(errors,"minutos",this.minutos,true,0,0,"errors.minutos.requerido");
        	if(errors==null || errors.size()==0){
            	if(new Integer(this.minutos).intValue()>=60)
            		errors.add("minutoError",new ActionMessage("errors.minutos.requerido"));
        	}

		}*/
		LogSISPE.getLog().info("errores.: {}" , errors.size());
		return errors.size();
	}

	/**
	 * Permite validar la fecha de entrega
	 * @author jacalderon
	 * @param errors
	 * @param request
	 * 
	 * @return Devuelve un int.
	 * @throws Exception 
	 */
	public int validarFechaEntrega(ActionErrors errors,HttpServletRequest request) throws Exception{
		PropertyValidator validar= new PropertyValidatorImpl();
		if(request.getParameter("calendarioLocalLink")!=null)
			validar.validateFecha(errors,"fechaEntregaCliente",this.fechaEntregaCliente,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha de Entrega");
		
		//mensaje diferente cuando es entrega a domicilio desde local
		if(request.getSession().getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(ConstantesGenerales.CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
			this.validarCantidades(errors, request);
			if(errors.size()>0 && errors.get().next().toString().equals("errors.horas.requerido.sicmer"+"[]")){
				errors.clear();
				errors.add("horaDesdeHasta",new ActionMessage("errors.horas.requerido.sicmer"));
				Calendar fecha=Calendar.getInstance();
				fecha.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.getBuscaFecha()));
				CalendarioLocalEntregaDomicilioUtil.mostrarCalendario(request.getSession(), this, fecha, request, errors);
				request.getSession().setAttribute(CalendarioLocalEntregaDomicilioUtil.MENSAJE_HORAS_INVALIDAS,"ok");
			}
		}else{
			validar.validateLong(errors,"horas",this.horas,true,0,0,"errors.horas.requerido");
		}
		if(errors.size()==0){
			if(new Integer(this.horas).intValue()>=24){
				errors.add("horas",new ActionMessage("errors.horas.requerido"));
			}
		}
		//validar.validateLong(errors,"minutos",this.minutos,true,0,0,"errors.minutos.requerido");
		/*if(errors.size()==0){
			if(new Integer(this.minutos).intValue()>=60)
				errors.add("minutos",new ActionMessage("errors.minutos.requerido"));
		}*/
		LogSISPE.getLog().info("errores: {}", errors.size());
		return errors.size();
	}
	
	/**
	 * @author jacalderon
	 * @param request
	 */
	public void mantenerValoresEntregas(HttpServletRequest request){
		HttpSession session=request.getSession();
		int indexDetalles=0;//indice para las cantidades de los detalles
		//Obtengo el detalle del pedido
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		if(detallePedidoDTOCol != null){
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
				LogSISPE.getLog().info("indice de cantidades: {}" , indexDetalles);
				long cantEntregada = detallePedidoDTO.getNpContadorEntrega();//Numero de articulos entregados
				LogSISPE.getLog().info("*********npCantidadEstado******* {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
				/*******************Verifico cantidades en los detalles del pedido*********************/
				//Si se va a entregar parcialmente
				if(session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
					LogSISPE.getLog().info("****cantidad ingresada******{}" , this.cantidadEstados[indexDetalles]);
					if(this.cantidadEstados[indexDetalles]!=null && !this.cantidadEstados[indexDetalles].equals("")){
						LogSISPE.getLog().info("***********el articulo {} fue seleccionado*****************",indexDetalles);
						LogSISPE.getLog().info("cantidadEstado[{}]: {}",indexDetalles,this.cantidadEstados[indexDetalles]);
						long cantidadMaximaDetalle=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada;
						LogSISPE.getLog().info("cantidadMaximaDetalle:{}" , cantidadMaximaDetalle);
						//Valida el formato de la cantidad ingresada
						try{
							detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(this.cantidadEstados[indexDetalles]));
						}catch(NumberFormatException e){
							LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");
						}
					}
				}
				//Si se va a pedir a bodega
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 
						&& this.opStock != null 
						&& (this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
								|| this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 && this.cantidadPedidaBodega[indexDetalles]!=null && !this.cantidadPedidaBodega[indexDetalles].equals("")){
						/******************Valido las cantidades ingresadas en el pedido a bodega**************/
						//Valida el formato de la cantidad ingresada
						try{
							//Si la entrega es parcial y se va a entregar todo desde el CD
							if(session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA)!=null && session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial")) && session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
							else
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(this.cantidadPedidaBodega[indexDetalles]));
						}catch(NumberFormatException e){
							LogSISPE.getLog().info("cantidad errada para la cantidad de reserva");
						}
					}
				}
				indexDetalles++;
			}
		}
	}
	
	public int validarCantidadesIngresadasModificadas(ActionErrors errors,HttpServletRequest request){
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		int indexDetalles=0;
		Double totalEntregaTotalSeleccionada = 0D;//solo se llena cuando son entregas totales
		Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
		LogSISPE.getLog().info("Ingresa a validar cantidades y a ver si la configuracion es correcta");
		try{
			if((this.opLugarEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))||this.opLugarEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"))) 
					|| (String)session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
				String codCanastosEspeciales = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request)).getValorParametro();
				String codCanastosCatalogo = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request)).getValorParametro();
				long cantidadTotal = 0; //cantidad de canastos
				
				Boolean validarCantidadCanastos = Boolean.FALSE;
				ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
				Integer codMinCanastosDomicilio = Integer.parseInt(parametroMinCanastas.getValorParametro());
				
				ParametroDTO paramMontoMinEntregaDomicioCD = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
				Double montoMinEntregaDomicioCD = Double.valueOf(paramMontoMinEntregaDomicioCD.getValorParametro());
				
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
					long cantEntregada=detallePedidoDTO.getNpContadorEntrega();//Numero de articulos entregados
					
					totalEntregaTotalSeleccionada += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta();
					if((String)session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA) != null){
						
						if(this.cantidadEstados[indexDetalles]!=null){
							if(this.cantidadEstados[indexDetalles].equals("")){
								this.cantidadEstados[indexDetalles]="0";
							}
							LogSISPE.getLog().info("***********el articulo {}" , indexDetalles + " fue seleccionado*****************");
							LogSISPE.getLog().info("cantidadEstado[{}]: {}" ,indexDetalles, this.cantidadEstados[indexDetalles]);
							long cantidadMaximaDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada;
							LogSISPE.getLog().info("cantidadMaximaDetalle:{}" , cantidadMaximaDetalle);
							
							//Valida el formato de la cantidad ingresada
							try{
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(this.cantidadEstados[indexDetalles]));
							}catch(NumberFormatException e){
								LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
								errors.add("cantidadEstado",new ActionMessage("error.validacion.cantidadEstado.invalido",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
							}
							
							LogSISPE.getLog().info("NpCantidadEstado: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
							LogSISPE.getLog().info("-cantEntregada: {}" , cantEntregada);
							
							//Verifica si la cantidad por entregar es mayor a la maxima por entregar, si es asi pone la cantidad maxima a entregar por defecto
							if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 
									&& detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada)){
//									&&(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue())
//									&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()!=0						
								LogSISPE.getLog().info("nueva NpCantidadEstado: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
								errors.add("cantidadEstado",new ActionMessage("error.validacion.parcial.cantidadEstado.invalido",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()-cantEntregada,detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
							}
							totalEntregaParcialSeleccionada += UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO);
						}
					}
					//validacion de numero de canastos para entrega a domicilio
					if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
							|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
						LogSISPE.getLog().info("valido cantidad {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
						cantidadTotal = cantidadTotal + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
					}
					indexDetalles++;
				}
				
				//se obtiene los totales consolidados
				String codigoPedido = detallePedidoDTOCol.iterator().next().getId().getCodigoPedido();
				double cantidadConsolidada = ConsolidarAction.obtenerTotalVentaConsolidados(request, codigoPedido);
				totalEntregaTotalSeleccionada += cantidadConsolidada;
				totalEntregaParcialSeleccionada += cantidadConsolidada;
				cantidadTotal += ConsolidarAction.obtenerCantidadCanastosConsolidados(request, codigoPedido);
				
				//Poner en Sesion el valor parcial de la entrega
				session.setAttribute(EntregaLocalCalendarioAction.VALORPARCIALENTREGA,totalEntregaParcialSeleccionada);
				if(cantidadTotal > 0 && cantidadTotal >= codMinCanastosDomicilio.longValue()){
					validarCantidadCanastos = Boolean.TRUE;
				}
				
				if(this.opLugarEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
						&& !this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) && (this.total < montoMinEntregaDomicioCD || !validarCantidadCanastos)){
				
//					LogSISPE.getLog().info("No cumple la condicion que para las entregas a domicilio minimo de canastas sea 50");
//					errors.add("minEntrega",new ActionMessage("error.validacion.configuracion.entregas.cantidadMinima.domicilio",codMinCanastosDomicilio));
					
					//if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, 
							///MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.montoMinimoEntregaDomicilioCD"))==null){
					if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue())){
						//si no tiene autorizacion se valida las entregas a domicilio
						validarEntregaTotalParcial(errors, codMinCanastosDomicilio, montoMinEntregaDomicioCD, totalEntregaTotalSeleccionada, totalEntregaParcialSeleccionada, validarCantidadCanastos,request);
					}
				}
				else if(this.opLugarEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"))
						&& (this.total < montoMinEntregaDomicioCD || !validarCantidadCanastos)){
					ParametroDTO parametroMontoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioLocal", request);
					BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
					
					if(this.opTipoEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total")) && totalEntregaTotalSeleccionada < Double.parseDouble(parametroMontoDTO.getValorParametro())){
						errors.add("minEntrega",new ActionMessage("error.validacion.cantidadMinima.entrega.domicilio.local", parametroMontoDTO.getValorParametro(),Util.roundDoubleMath(totalEntregaTotalSeleccionada, NUMERO_DECIMALES).toString()));
					}else if (beanSession.getPaginaTabPopUp().getTabSeleccionado()!=0 && this.opTipoEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial")) && totalEntregaParcialSeleccionada < Double.parseDouble(parametroMontoDTO.getValorParametro())){
						errors.add("minEntrega",new ActionMessage("error.validacion.cantidadMinima.entrega.domicilio.local", parametroMontoDTO.getValorParametro(),Util.roundDoubleMath(totalEntregaParcialSeleccionada, NUMERO_DECIMALES).toString()));
					}
					
				}else if(totalEntregaParcialSeleccionada==0 && (String)session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){ //para el caso en que no es a domicilio y esta configurando parcialmente
					errors.add("sinCambios",new ActionMessage("warnings.sinCambiosReserva"));
				}
			}
			else{
				//validacion de seleccion de configuracion de entregas
				if(this.opElaCanEsp!=null && this.opElaCanEsp.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && this.opLugarEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")) && this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
					LogSISPE.getLog().info("No puede seleccionar la configuracion seleccionada");
					errors.add("minEntrega",new ActionMessage("error.validacion.seleccion.configuracion.entrega"));
				}
			}
		} catch (Exception e) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			errors.add("errorEntregas",new ActionMessage("errors.SISPEException",e));
		}
		return errors.size();
	}
	
	
	
	/**
	 * Permite validar los campos requeridos para las cantidades.
	 * @author jacalderon
	 * @param errors
	 * @param request
	 * 
	 * @return Devuelve un int.
	 */
	
	public int validarCantidades(ActionErrors errors,HttpServletRequest request){
		PropertyValidator validar= new PropertyValidatorImpl();
		HttpSession session = request.getSession();
		DireccionesDTO direccionesDTO = null;
		mantenerValoresEntregas(request);
		
		//si existen direccion de entrega esto es para comprobar si la entrega puede realizarse en esta direccion
		if(session.getAttribute(EntregaLocalCalendarioAction.DIRECCION)!=null)
			direccionesDTO = (DireccionesDTO)session.getAttribute(EntregaLocalCalendarioAction.DIRECCION);
		else{
			direccionesDTO = new DireccionesDTO();
		}
		
		//si la configuracion es a domicilio se valida ciudad y sector de entrega
		if((this.getOpLugarEntrega().equals(ConstantesGenerales.CONTEXTO_ENTREGA_DOMICIO) && (this.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))){
					//se valida la ciudad de entrega
					if(StringUtils.isEmpty(this.getSeleccionCiudad())){
						validar.validateMandatory(errors, "campoCiudadRequerido", this.seleccionCiudad, "warnings.campo.ciudad.requerido");
						validar.validateMandatory(errors,"sector",this.selecionCiudadZonaEntrega,"warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora");
					}
					//se valida el sector de entrega
					Collection<DivisionGeoPoliticaDTO> zonasCiudad = (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(EntregaLocalCalendarioAction.CIUDAD_SECTOR_ENTREGA);
					if(CollectionUtils.isNotEmpty(zonasCiudad)){
						if(StringUtils.isEmpty(this.getSelecionCiudadZonaEntrega())){
							validar.validateMandatory(errors,"sector",this.selecionCiudadZonaEntrega,"warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora");
						}
					}
		}
		//Validaciones de campos calendario SICMER
		if(this.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"))){
			
			if (StringUtils.isEmpty(this.getHoraDesde()) || StringUtils.isEmpty(this.getHoraHasta())){
				errors.add("horaDesdeHasta",new ActionMessage("errors.horas.requerido.sicmer"));
			}
			if (StringUtils.isEmpty(this.getDireccion())){
				errors.add("direccion",new ActionMessage("errors.direccionEntrega"));
			}
			if(StringUtils.isEmpty(this.getCodigoVendedorSicmer())){
				errors.add("codigoVendedorSicmer",new ActionMessage("error.codigoVendedorSicmer"));
			}
			if(StringUtils.isEmpty(this.getQuienRecibeSicmer())){
				errors.add("quienRecibeSicmer",new ActionMessage("error.quienRecibiraSicmer"));
			}
			
			//se valida la ciudad de entrega
			if(StringUtils.isEmpty(this.getSeleccionCiudad())){
				validar.validateMandatory(errors, "campoCiudadRequerido", this.seleccionCiudad, "warnings.campo.ciudad.requerido");
				validar.validateMandatory(errors,"sector",this.selecionCiudadZonaEntrega,"warnings.campo.zona.ciudad.requerido","");
			}
			//se valida el sector de entrega
			Collection<DivisionGeoPoliticaDTO> zonasCiudad = (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(EntregaLocalCalendarioAction.CIUDAD_SECTOR_ENTREGA);
			if(CollectionUtils.isNotEmpty(zonasCiudad)){
				if(StringUtils.isEmpty(this.getSelecionCiudadZonaEntrega())){
					validar.validateMandatory(errors,"sector",this.selecionCiudadZonaEntrega,"warnings.campo.zona.ciudad.requerido","");
				}
			}
		}
		
		//si existen nuevas entregas
		//se valida la hora
		
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		if(beanSession.getPaginaTabPopUp().getCantidadTabs() == 3 ){
			if(beanSession.getPaginaTabPopUp().getTabSeleccionado() == 2 ){
				validacionHorasMinutos(errors);
			}
		}
		else{

			if(beanSession.getPaginaTabPopUp().getCantidadTabs() == 2){
				if(beanSession.getPaginaTabPopUp().getTabSeleccionado() == 1){
					validacionHorasMinutos(errors);
				}
			}
		}
		
		//Si se presiona el boton de agregar entregas
		if(request.getParameter("botonAceptarEntrega")!=null && this.horas != null && this.fechaEntregaCliente !=null && !this.fechaEntregaCliente.isEmpty() && errors.size()==0){
											
			//******** transformo la hora ingresada a tipo Time **************
			GregorianCalendar fechaEntregaCompleta = new GregorianCalendar();
			fechaEntregaCompleta.setTime(ConverterUtil.parseStringToDate(this.fechaEntregaCliente));
			fechaEntregaCompleta.set(Calendar.HOUR_OF_DAY,Integer.parseInt(this.horas));
			fechaEntregaCompleta.set(Calendar.MINUTE,Integer.parseInt(this.minutos));
			//*****************************************************************
			LogSISPE.getLog().info("lugar de entrega: {}" , session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString());
			/***************************Si las entregas son a domicilio******************/
			//Valido que se haya ingresado la direccion, y la distancia en horas o kilometors
			if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))/* && this.direcciones==null*/){
				validar.validateFecha(errors,"fechaEntregaCliente",this.fechaEntregaCliente,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha de Entrega");
				if(errors.size()==0){
					if((String)session.getAttribute(EntregaLocalCalendarioAction.COMBOSELECCIONCIUDAD)!=null){
						validar.validateMandatory(errors,"seleccionCiudad",this.seleccionCiudad,"error.validacion.ciudad.vacia");
						Collection<DivisionGeoPoliticaDTO> zonasCiudad = (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(EntregaLocalCalendarioAction.CIUDAD_SECTOR_ENTREGA);
						if(CollectionUtils.isNotEmpty(zonasCiudad)){
							if(this.selecionCiudadZonaEntrega==null || this.getSelecionCiudadZonaEntrega().isEmpty()){
								validar.validateMandatory(errors,"sector",this.selecionCiudadZonaEntrega,"warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora");
							}
						}
					}
					//IO, validacion de nuevos campos para obtener direccion completa.
					validar.validateMandatory(errors, "callePrincipal", this.callePrincipal,"errors.requerido","Calle principal");
					validar.validateMandatory(errors, "numeroCasa", this.numeroCasa,"errors.requerido","# de casa");
					validar.validateMandatory(errors, "calleTransversal", this.calleTransversal,"errors.requerido","Calle transversal");
					validar.validateMandatory(errors, "referencia", this.referencia,"errors.requerido","Referencia/Contacto/ Tel\u00E9fono completo:");
					
					//IO, concatenacion de campos para armar la direccion completa.
					
					String ciudadSeleccionada = "";
					if(this.seleccionCiudad != null){
						ciudadSeleccionada = this.seleccionCiudad.contains("/") ? seleccionCiudad.split("/")[1] : this.seleccionCiudad;
						if(StringUtils.isNotEmpty(ciudadSeleccionada) && session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)!=null){
							for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO : (Collection<VistaEstablecimientoCiudadLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)){						
								for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocal : (Collection<VistaEstablecimientoCiudadLocalDTO>)vistaEstablecimientoCiudadLocalDTO.getVistaLocales()){
									if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(ciudadSeleccionada)){
										ciudadSeleccionada = vistaEstablecimientoCiudadLocal.getNombreCiudad()+" - ";
									}						
								}
							}
						}
					}
					
					this.direccion = ciudadSeleccionada + this.callePrincipal + " " + this.numeroCasa + " y " + this.calleTransversal + ". " + this.referencia;
					//validar.validateMandatory(errors,"direccion",this.direccion,"errors.direccion.requerido");
					validar.validateMandatory(errors,"unidadTiempo",this.unidadTiempo,"errors.unidadTiempo.requerido");					
				}
				if(errors.size()>0)
					this.direcciones=null;
				LogSISPE.getLog().info("******error unidad de tiempo****** {}" , errors.size());
				if(this.unidadTiempo!=null && this.unidadTiempo.equals("H")){
					validarDistanciaTiempo(errors,validar);
				}else if(this.unidadTiempo!=null && this.unidadTiempo.equals("K")){
					validar.validateDouble(errors, "distancia", this.distancia, true, 0, 1000, "error.validacion.distancia.invalido");
				}
				
				LogSISPE.getLog().info("fechaEntregaDireccion: {}" , direccionesDTO.getFechaEntrega());
				LogSISPE.getLog().info("fecha entregaCliente: {}" , new Timestamp(fechaEntregaCompleta.getTimeInMillis()));
				//No se pueden realizar entregas a un mismo domicilio en diferentes fechas
				if(!esFechaCorrectaParaDomicilio(request, direccionesDTO, fechaEntregaCompleta, errors)){
					LogSISPE.getLog().info("direccion en diferentes fechas");
					this.direcciones=null;
					this.unidadTiempo=null;
					this.distancia=null;
					this.direccion=null;
					session.removeAttribute(EntregaLocalCalendarioAction.DIRECCION);
//					LogSISPE.getLog().info("direcciones: {}" , this.direcciones.toString());
//					LogSISPE.getLog().info("direccion: {}", this.direccion);
					LogSISPE.getLog().info("sesion direccion: {}" , session.getAttribute(EntregaLocalCalendarioAction.DIRECCION));
				}
			}
			/*****************************************************************************************************/
			//Diferencia entre fecha de entrega y fecha minima de entrega
			long diferenciaEntregaBusca;
			try {
				diferenciaEntregaBusca = ConverterUtil.returnDateDiff(this.buscaFecha, this.fechaEntregaCliente);
				//Si la fecha de entrega es menor a la fecha minima de entrega
				if(diferenciaEntregaBusca<0.0){
					errors.add("fechaEntregaMinima", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",this.buscaFecha));
					//session.removeAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
				}
			}catch (Exception e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			/******************************Valida las cantidades ingresadas en los detalles y en las entregas*****************/
			LogSISPE.getLog().info("entro a validar editarCantidades");
			//Obtengo el detalle del pedido
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
			int indexDetalles=0;//indice para las cantidades de los detalles
			int numeroBultos=0;//variable para convertir la cantidad ingresada a bultos
			Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = null;
			boolean seleccionarDia=false;//variable que indica que no se ha seleccionado un dia en el calendario
			boolean reservoCantidadSIC = false;//variable que indica si se pidi\u00F3 almenos un valor mayor a 0 en el caso de solicitar parcialmente a bodega
			GregorianCalendar despachoT=new GregorianCalendar();//fecha de despacho
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
			VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
			if(session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO)!=null){
				//obtengo el local de entrega
				vistaLocalDTO=(VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO);
			}
			
			try{
				if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX)!=null){
					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal1=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX);
					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalAux1=calendarioConfiguracionDiaLocal1.copiar();
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalAux1);
				}
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					
					LogSISPE.getLog().info("indice de cantidades: {}" , indexDetalles);
					long cantEntregada=detallePedidoDTO.getNpContadorEntrega();//Numero de articulos entregados
					long cantReservada=detallePedidoDTO.getNpContadorDespacho();//Numero de articulos despachados por articulo
					Collection<EntregaDetallePedidoDTO> entregasCol = detallePedidoDTO.getEntregaDetallePedidoCol();
					LogSISPE.getLog().info("*********npCantidadEstado******* {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
					
					//Valida que si una articulo cuya clasificacion padre(13-licores) y la fechaEntregaCliente es en un dia (domingo) no le permita configurar
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0){
						LogSISPE.getLog().info("Verificar la fecha de entrega al cliente y determinar si el pedido tiene restricciones para licores");
						/*
						 * respuestaTieneLicores
						 *Si el pedido no tiene licores =0
						 *Si el pedido tiene licores dentro y fuera de canastos=1
						 *Si el pedido solo tiene licores fuera del canasto=2
						 */
						String respuestaTieneLicores = CotizacionReservacionUtil.verificarArticuloEsLicorPorClasificacion(request, detallePedidoDTO);
						
						String flagLicores = CotizacionReservacionUtil.getValueParameter("ec.com.smx.sic.sispe.parametro.validacion.licores",request);
						List<Integer> diasProhibicion = CotizacionReservacionUtil.getDiasNoVenderLicores(CotizacionReservacionUtil.getValueParameter("ec.com.smx.sic.sispe.parametro.dias.prohibicion.venta.licores",request));
						
						Calendar calendar = Calendar.getInstance(new Locale("es", "ES"));
						calendar.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
						
						/* No validar restriccion de licores*/
							if (!StringUtils.equals(flagLicores, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.licores.noValidar"))){
								if(diasProhibicion.contains(calendar.get(Calendar.DAY_OF_WEEK))){
									String nombreDiasProhibicion=CotizacionReservacionUtil.getNombreDiasNoVenderLicores(diasProhibicion);
									if (StringUtils.equals(flagLicores, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.licores.validarCanastosYNoCanastos"))) {
										/*No permitir la venta de licores dentro y fuera de canastos*/
										if (StringUtils.equals(respuestaTieneLicores, ConstantesGenerales.PEDIDO_CON_LICORES)) {
											errors.add("validacionfechaEntregaCliente",new ActionMessage("error.validacion.fechaEntregaCliente.articulos.licores",nombreDiasProhibicion,nombreDiasProhibicion.toUpperCase()));
											break;
										}else if (StringUtils.equals(respuestaTieneLicores, ConstantesGenerales.PEDIDO_CON_LICORES_CANASTOS)) {
											errors.add("validacionfechaEntregaCliente",new ActionMessage("error.validacion.fechaEntregaCliente.articulos.licores",nombreDiasProhibicion,nombreDiasProhibicion.toUpperCase()));
											break;
										}
									}else if (StringUtils.equals(flagLicores, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.licores.validarArticulosNoCanastos"))) {
										/* No permitir la venta de licores fuera de canastos*/
										if (StringUtils.equals(respuestaTieneLicores, ConstantesGenerales.PEDIDO_CON_LICORES)) {
											errors.add("validacionfechaEntregaCliente",new ActionMessage("error.validacion.fechaEntregaCliente.articulos.licoresPedido",nombreDiasProhibicion,nombreDiasProhibicion.toUpperCase()));
											break;
										}
									}
								}
							}
					}
					/*******************Verifico cantidades en los detalles del pedido*********************/
					//Si se va a entregar parcialmente
					if(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA!=null){
						LogSISPE.getLog().info("****cantidad ingresada****** {}" , this.cantidadEstados[indexDetalles]);
						if(this.cantidadEstados[indexDetalles]!=null && !this.cantidadEstados[indexDetalles].equals("")){
							LogSISPE.getLog().info("***********el articulo {}" , indexDetalles + " fue seleccionado*****************");
							LogSISPE.getLog().info("cantidadEstado[{}]: {}" ,indexDetalles, this.cantidadEstados[indexDetalles]);
							long cantidadMaximaDetalle=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada;
							LogSISPE.getLog().info("cantidadMaximaDetalle:{}" , cantidadMaximaDetalle);
							//Valida el formato de la cantidad ingresada
							try{
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(this.cantidadEstados[indexDetalles]));
							}catch(NumberFormatException e){
								LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
								errors.add("cantidadEstado",new ActionMessage("error.validacion.cantidadEstado.invalido",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
							}
							LogSISPE.getLog().info("NpCantidadEstado: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
							LogSISPE.getLog().info("-cantEntregada: {}" , cantEntregada);
							//Verifica si la cantidad por entregar es mayor a la maxima por entregar, si es asi pone la cantidad maxima a entregar por defecto
							if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada)){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(cantidadMaximaDetalle));
								LogSISPE.getLog().info("nueva NpCantidadEstado: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
							}
							/******verifico cantidades en las entregas******************/
							if(entregasCol!=null && entregasCol.size()>0 && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0){
								for(Iterator<EntregaDetallePedidoDTO> numeroDetalleEntrega=entregasCol.iterator();numeroDetalleEntrega.hasNext();){
									EntregaDetallePedidoDTO entregaDetallePedidoDTO = (EntregaDetallePedidoDTO)numeroDetalleEntrega.next();
									try{
										if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL)!=null){
											//obtengo el dia seleccionado
											calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
											//transformo la fecha de despacho
											despachoT.setTime(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
										}else{
											despachoT.setTime(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
										}
										LogSISPE.getLog().info("fecha: {}" , new Timestamp(despachoT.getTime().getTime()));
										LogSISPE.getLog().info("FechaDespachoBodega: {}", entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
										LogSISPE.getLog().info("CodigoLocalEntrega: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
										LogSISPE.getLog().info("FechaEntregaCliente: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
										LogSISPE.getLog().info("horaT: " + new Timestamp(fechaEntregaCompleta.getTime().getTime()));
										LogSISPE.getLog().info("*************Verifico si existe una entrega repetida******************");
										//Si la entrega es a locales
										if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
											LogSISPE.getLog().info("la entrega es a locales");
											
											//Se cambia el codigo del local porque cuando existen canastos especiales se valida con la bodega 99 y no cumple las condiciones de codigo local de entrega
											String codigoLocal=vistaLocalDTO.getId().getCodigoLocal().toString();
											Integer codigoLocalImpresion=entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega();
											if((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES)!= null &&
													((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES)).equals(ConstantesGenerales.ENTIDAD_RESPONSABLE_BODEGA) &&
													(String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA)!=null &&
													((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA)).equals(ConstantesGenerales.CONTEXTO_ENTREGA_MI_LOCAL) &&
													(String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK)!=null && 
													((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK)).equals(ConstantesGenerales.STOCK_ENTREGA_DOMICILIO)){
												codigoLocal=entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().toString();
												despachoT.setTime(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
												codigoLocalImpresion=vistaLocalDTO.getId().getCodigoLocal();
											}
											
											//Verifico si existe una entrega para un local en una fecha de despacho y en una fecha de entrega igual a la ya seleccionada***/
											if(vistaLocalDTO!=null && entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null && 
													entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega().toString().equals(codigoLocal) && 
													entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().equals(new Timestamp(fechaEntregaCompleta.getTime().getTime())) && 
													entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega().equals(new Timestamp(despachoT.getTime().getTime()))){
												LogSISPE.getLog().info("ya existe una entrega en el local: {}", vistaLocalDTO.getId().getCodigoLocal());
												errors.add("errorFechas",new ActionMessage("errors.coincidenFechas",codigoLocalImpresion,this.fechaEntregaCliente,detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
											}
										}
										//Si la entrega es a domicilio
										else{
											LogSISPE.getLog().info("entregaDTO.getNpDireccionEntregaDomicilio: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
											LogSISPE.getLog().info("direccionesDTO.getCodigoDireccion: {}" , direccionesDTO.getCodigoDireccion());
											LogSISPE.getLog().info("entregaDTO.getFechaDespachoBodega: {}", entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
											LogSISPE.getLog().info("despachoT{}" ,despachoT.getTime().getTime());

											//Verifico si existe una direccion en una fecha de entrega
											if(session.getAttribute(EntregaLocalCalendarioAction.DIRECCION)!=null && entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio()!=null && entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio().equals(direccionesDTO.getCodigoDireccion()) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().equals(new Timestamp(fechaEntregaCompleta.getTime().getTime()))){
												LogSISPE.getLog().info("ya existe una entrega en la direccion");
												DireccionesDTO direccionDTO=(DireccionesDTO)session.getAttribute(EntregaLocalCalendarioAction.DIRECCION);
												errors.add("errorDireccionFechas",new ActionMessage("errors.coincidenDirecciones",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo(),direccionDTO.getDescripcion(),ConverterUtil.parseDateToString(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente())));
												LogSISPE.getLog().info("direccion repetida en la misma fecha");
												this.direcciones=null;
												this.unidadTiempo=null;
												this.distancia=null;
												this.direccion=null;
												session.removeAttribute(EntregaLocalCalendarioAction.DIRECCION);
//												LogSISPE.getLog().info("direcciones: {}" , this.direcciones);
//												LogSISPE.getLog().info("direccion:  {}", this.direccion);
											}
										}
									}catch(Exception e){
										LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
										LogSISPE.getLog().info("error en form: {}" , e);
									}
								}
							}
						}
						else
							detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(0));
					}
					//Si se va a pedir a bodega
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 && (this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")) || this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
						if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 && this.cantidadPedidaBodega[indexDetalles]!=null && !this.cantidadPedidaBodega[indexDetalles].equals("")){
							long cantidadMaximaReservaDet=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantReservada;
							LogSISPE.getLog().info("cantidadMaximaReservaDet: {}" , cantidadMaximaReservaDet);
							/******************Valido las cantidades ingresadas en el pedido a bodega**************/
							//Valida el formato de la cantidad ingresada
							try{
								//Si la entrega es parcial y se va a entregar todo desde el CD
								if(session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial")) && session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
								else{
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(this.cantidadPedidaBodega[indexDetalles]));
									LogSISPE.getLog().info("CantidadReservaSIC: {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
									//Si pide parcialmente a bodega, valida que se ingrese almenos un valor mayor a 0
									if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0){
										reservoCantidadSIC = true;
									}
								}
								//si el art\u00EDculo no es perecible
								/*if(!verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
									//Se suman las cantidades pedidas a bodega en caso de ser una entrega a domicilio en la ciudad de guayaquil
									if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
										//Se obtiene desde session el contenido de VALORPARAMETRO para las ciudades que est\u00E1n habilitadas las bodegas de tr\u00E1nsito
										String[] parametroDividido = ((String)session.getAttribute(EntregaLocalCalendarioAction.VAR_VALOR_PARAMETRO_TOTAL)).split(",");
										String[] codigosCiudad = null;
										//Se comparan c\u00F3digos de ciudad con VALORPARAMETRO
										for(int i=0; i<parametroDividido.length; i++){
											codigosCiudad = parametroDividido[i].split("-");						
											LogSISPE.getLog().info("Valor par\u00E1metro - c\u00F3digo ciudad truncado: {}",codigosCiudad[0]);		
											if(codigosCiudad[0].equals(valorCodigoCiudad)){
												LogSISPE.getLog().info("Los c\u00F3digos son iguales");
												
												//Se almacena codigosCiudad[1] en una variable de session y ese valor se setea en VALUE del checkbox 
												LogSISPE.getLog().info("Valor c\u00F3digo ciudad: {}",codigosCiudad[1]);
																								
												//session.setAttribute(VAR_VALOR_CODIGO_PARAMETRO, codigosCiudad[1]);
												valorCodigoTransito = codigosCiudad[1];
												
												//Variable que determina si se muestra o no el checkbox en pantalla												
												validarCheckTransito = "siAplica";	
																		
												break;
											}
										}
										
									}
								}*/
								
							}catch(NumberFormatException e){
								LogSISPE.getLog().info("cantidad errada para la cantidad de reserva");
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
								errors.add("cantidadReservaSIC",new ActionMessage("error.validacion.cantidadReservaSic.invalido",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
							}
							LogSISPE.getLog().info("cantidadReservaSIC: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
							LogSISPE.getLog().info("-cantReservada: {}" , cantReservada);
							//Verifica si la cantidad por reservar es mayor a la maxima por entregar, si es asi pone la cantidad maxima a entregar por defecto
							if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()>0 && detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()>(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantReservada)){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(cantidadMaximaReservaDet));
								LogSISPE.getLog().info("nueva cantidadReservaSIC: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
							}
						}
						
						//Si fue seleccionado un dia del caldenario y existe calendario
						if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)!=null){
							LogSISPE.getLog().info("fue seleccionado un dia en el calendario");
							if((new Long(this.cantidadPedidaBodega[indexDetalles])).longValue()>0){
								if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()>0){
									//calculo el numero de bultos
									try {
										numeroBultos = UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue(), detallePedidoDTO.getArticuloDTO());
										
										if(session.getAttribute(EntregaLocalCalendarioAction.MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
											//quiere decir que es entregas a domicilio CD, no se calculan los bultos, esta configurando UNA ENTREGA
											numeroBultos = 1;
										}
										
										//cantidades informativas
										cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
										//cantidad para frio
										CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
										cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
										cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
										cantidadesInformativas.add(cantidadFrio);
										//cantidad para seco
										CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
										cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
										cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
										cantidadesInformativas.add(cantidadSeco);
										
									} catch (SISPEException e) {
										LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
										LogSISPE.getLog().info("error al calcular el numero de bultos detalles");
									}
								}
								if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null){
									/*********************Valido la CD************************/
									//Obtenga la configuracion del calendario
									CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=new CalendarioConfiguracionDiaLocalDTO();
									if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1)!=null){
										//obtengo la configuracion del calendario
										calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1);
									}
									LogSISPE.getLog().info("local del calendario: {}" , calendarioDiaLocalDTO.getId().getCodigoLocal());
									//Obtengo la configuracion del dia seleccionado
									CalendarioDiaLocalDTO calendarioDiaLocalActual=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalDTO.getId(),calendarioConfiguracionDiaLocalDTO);
									LogSISPE.getLog().info("numero de bultos:{}" , numeroBultos);
									LogSISPE.getLog().info("CD: {}" , calendarioDiaLocalActual.getCantidadDisponible());
									if((new Long(ConverterUtil.fromDoubleToInteger(calendarioDiaLocalActual.getCantidadDisponible()))).longValue()<1){//TODO verificar en configuraciones que no son al CD
										//Si no existe la capacidad verifica si tiene autorizacion
										GregorianCalendar horaDespacho=new GregorianCalendar();
										horaDespacho.setTime(calendarioDiaLocalActual.getId().getFechaCalendarioDia());
										if(!calendarioDiaLocalActual.getId().getCodigoLocal().toString().equals((SessionManagerSISPE.getCurrentLocal(request)).toString())){
											AutorizacionDTO autorizacionDTO = buscaAutorizacion(session,calendarioDiaLocalActual.getId().getCodigoLocal().toString(),new Timestamp(horaDespacho.getTime().getTime()));
											if(autorizacionDTO==null){
												errors.add("cantidadMayor",new ActionMessage("errors.capacidadDisponible",numeroBultos,detallePedidoDTO.getArticuloDTO().getDescripcionArticulo(),calendarioDiaLocalDTO.getId().getCodigoLocal()));
											}
											else{
												/*//adiciona las cantidades existentes
												if(calendarioDiaLocalActual.getDetalleCantidadCalendario() != null){
													for(CantidadCalendarioDiaLocalDTO cantidadCalendarioConf : calendarioDiaLocalActual.getDetalleCantidadCalendario()){
														for(CantidadCalendarioDiaLocalDTO cantidadCalendario : cantidadesInformativas){
															if(cantidadCalendarioConf.getTipoCantidad().equals(cantidadCalendario.getTipoCantidad())){
																LogSISPE.getLog().info("*** CI {} ant *** {}" , cantidadCalendarioConf.getTipoCantidad(),cantidadCalendarioConf.getCantidad());
																LogSISPE.getLog().info("*** CI {} act *** {}" ,cantidadCalendario.getTipoCantidad() ,cantidadCalendario.getCantidad());
																cantidadCalendario.setCantidad(cantidadCalendario.getCantidad() + cantidadCalendarioConf.getCantidad());
																break;
															}
														}
													}
												}*/
												calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
														calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),true, cantidadesInformativas, calendarioDiaLocalActual);
												//Obtengo los dias que debo modificar su capacidad acumulada
												Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
												for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
													//cantidades informativas
													Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
													//cantidad para frio
													CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
													cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
													cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
													cantidadesInformativasAux.add(cantidadFrio);
													//cantidad para seco
													CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
													cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
													cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
													cantidadesInformativasAux.add(cantidadSeco);
													
													/*//adiciona las cantidades existentes
													if(calendarioDiaLocalDTOAux.getDetalleCantidadCalendario() != null){
														for(CantidadCalendarioDiaLocalDTO cantidadCalendarioConf : calendarioDiaLocalDTOAux.getDetalleCantidadCalendario()){
															for(CantidadCalendarioDiaLocalDTO cantidadCalendario : cantidadesInformativasAux){
																if(cantidadCalendarioConf.getTipoCantidad().equals(cantidadCalendario.getTipoCantidad())){
																	LogSISPE.getLog().info("*** CI {} ant *** {}" , cantidadCalendarioConf.getTipoCantidad(),cantidadCalendarioConf.getCantidad());
																	LogSISPE.getLog().info("*** CI {} act *** {}" ,cantidadCalendario.getTipoCantidad() ,cantidadCalendario.getCantidad());
																	cantidadCalendario.setCantidad(cantidadCalendario.getCantidad() + cantidadCalendarioConf.getCantidad());
																	break;
																}
															}
														}
													}*/
													
													calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
															calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),true, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
												}
												session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
												session.setAttribute(EntregaLocalCalendarioAction.AUTORIZACION,autorizacionDTO);
											}
										}
										else{
											calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
													calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),false, cantidadesInformativas, calendarioDiaLocalActual);
											//Obtengo los dias que debo modificar su capacidad acumulada
											Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
											for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
												//cantidades informativas
												Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
												//cantidad para frio
												CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
												cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
												cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
												cantidadesInformativasAux.add(cantidadFrio);
												//cantidad para seco
												CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
												cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
												cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
												cantidadesInformativasAux.add(cantidadSeco);
												
												calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
														calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),false, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
											}
											session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
										}
									}
									else{
										calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
												calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),false, cantidadesInformativas, calendarioDiaLocalActual);
										//Obtengo los dias que debo modificar su capacidad acumulada
										Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
										for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
											//cantidades informativas
											Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
											//cantidad para frio
											CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
											cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
											cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
											cantidadesInformativasAux.add(cantidadFrio);
											//cantidad para seco
											CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
											cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
											cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
											cantidadesInformativasAux.add(cantidadSeco);
											
											calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
													calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),false, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
										}
										//session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);ss
									}
								}
							}
						}
						//Si no fue seleccionado un dia en el calendario
						else
							seleccionarDia=true;
					}
					indexDetalles++;
				}
				//Si no fue seleccionado un dia en el calendario
				if(seleccionarDia){
					session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
					errors.clear();
					errors.add("seleccionDia",new ActionMessage("errors.seleccionDia.requerido"));
				}
				//Si seleccion\u00F3 la opci\u00F3n de solicitar parcialmente a bodega, valida que se pida almenos un valor mayor a 0 en todas las entregas
				if(this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")) && !reservoCantidadSIC){
					errors.clear();
					errors.add("sinCantidadReservaBodega", new ActionMessage("error.validacion.cantidadReservaSic.sinCantidadParcial"));
				}
			}catch(Exception e){
				LogSISPE.getLog().info("error en form: {}" , e);
			}
		}else{
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
			//Cambio para que no de error de calendario en entrega a domicilio desde local
			if(!this.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"))
					&& !this.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"))
					&& EntregaLocalCalendarioAction.MOSTRAR_CALENDARIO_BODEGA_POR_HORAS == null){
				errors.add("seleccionDia",new ActionMessage("errors.seleccionDia.requerido"));
			}
		}
		
		LogSISPE.getLog().info("errores: {}" , errors.size());
		return errors.size();
	}

	/**
	 * @param errors
	 * @param validar 
	 */
	public void validarDistanciaTiempo(ActionErrors errors, PropertyValidator validar) {
		if(errors.size()==0){
			validar.validateLong(errors, "distanciaH", this.distanciaH, true, 0, 23, "error.validacion.distanciaH.invalido");			
		}
		validar.validateLong(errors, "distanciaM", this.distanciaM, true, 0, 59, "error.validacion.distanciaM.invalido");
		
		//si no se ingres\u00F3 tiempo
		if(this.distanciaH.equals("0") && this.distanciaM.equals("0")){
			errors.add("distanciaH",new ActionMessage("errors.unidadTiempo.requerido"));
		}
	}

	private void validacionHorasMinutos(ActionErrors errors) {
		if(errors.size()==0){
			try{
				int hora = Integer.valueOf(this.horas);
				if(hora < 6 || hora > 20){
					errors.add("horas",new ActionMessage("errors.rango", "Hora", "6:00", "20:00"));
				}
			}catch (Exception ex) {
				errors.add("horas",new ActionMessage("errors.rango", "Hora", "6:00", "20:00"));
			}
		}
		
		//se valida los minutos
		if(errors.size()==0){
			try{
				int minutos = Integer.valueOf(this.minutos);
				if(minutos < 0 || minutos >= 60){
					errors.add("minutos",new ActionMessage("errors.rango", "minutos", "0", "59"));
				}
			}catch (Exception ex) {
				errors.add("minutos",new ActionMessage("errors.rango", "minutos", "0", "59"));
			}
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param direccionSeleccionada
	 * @param fechaEntregaCompleta
	 * @param errors
	 * @return
	 */
	private boolean esFechaCorrectaParaDomicilio(HttpServletRequest request, DireccionesDTO direccionSeleccionada, GregorianCalendar fechaEntregaCompleta, ActionErrors errors){
		//Si no se seleccion\u00F3 una direcci\u00F3n espec\u00EDfica
		if(direccionSeleccionada.getFechaEntrega()==null){
			//coleccion de direcciones de entrega
			Collection<DireccionesDTO> direccionesDTOCol = request.getSession().getAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX)!=null ? (Collection<DireccionesDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX) : new ArrayList<DireccionesDTO>();
			if(direccionesDTOCol != null){
				for(DireccionesDTO direccionesDTO : direccionesDTOCol){
					if(direccionesDTO.getDescripcion().equals(this.direccion) && direccionesDTO.getFechaEntrega().getTime() == fechaEntregaCompleta.getTimeInMillis()){
						errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaEnttregaDirecciones", direccionesDTO.getDescripcion(), direccionesDTO.getFechaEntrega().toString()));
						return false;
					}
				}
			}
		}else if(direccionSeleccionada.getFechaEntrega().getTime() != fechaEntregaCompleta.getTimeInMillis()){
			//significa que hay una direcci\u00F3n seleccionada
			errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaEnttregaDirecciones2"));
			return false;
		}
		return true;
	}
	
	
	/**
	 * Modifica la capacidad disponible de las entregas que se van a eliminar
	 * @author jacalderon
	 */
	public void eliminaEntregasDetallePedido(ActionErrors errors,HttpServletRequest request, EntregaDetallePedidoDTO entregaDetallePedidoDTO, String tipoBodega){
		try{
			HttpSession session=request.getSession();
			
				//Obtengo el calendario de las entregas a eliminar
				//Si existio pedido a bodega dentro de la entrega
				if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null){
					// Parametros para buscar la capacidad del local en la fecha
					CalendarioDiaLocalID calendarioDiaLocalID=new CalendarioDiaLocalID();
					calendarioDiaLocalID.setCodigoCompania(entregaDetallePedidoDTO.getEntregaPedidoDTO().getId().getCodigoCompania());
					if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
						LogSISPE.getLog().info("local a eliminar1: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
						calendarioDiaLocalID.setCodigoLocal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
					}
					else{
						LogSISPE.getLog().info("local a eliminar2: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoLocalSector());
						calendarioDiaLocalID.setCodigoLocal(new Integer(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoLocalSector()));
					}
					Date mes=DateManager.getYMDDateFormat().parse(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega().toString());
					calendarioDiaLocalID.setFechaCalendarioDia(mes);
					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=new CalendarioConfiguracionDiaLocalDTO();
					if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL)!=null)
						calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL);
					LogSISPE.getLog().info("EntregaLocal - BuscarDiaPorID");
					CalendarioDiaLocalDTO calendarioDiaLocalDTO2=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
					/**************actualizo la capacidad disponible en el local para el despacho***********/
					LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2: {}" , calendarioDiaLocalDTO2);
					LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Compania: {}", calendarioDiaLocalDTO2.getId().getCodigoCompania());
					LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Local: {}" , calendarioDiaLocalDTO2.getId().getCodigoLocal());
					LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Fecha: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
					LogSISPE.getLog().info("EntregaLocal - entregaDTO.np: {}" ,entregaDetallePedidoDTO.getNpCantidadBultos());
					LogSISPE.getLog().info("capacidad disponible2: {}" , calendarioDiaLocalDTO2.getCantidadDisponible());
					LogSISPE.getLog().info("CantidadAcumulada: {}" , calendarioDiaLocalDTO2.getCantidadAcumulada());
					LogSISPE.getLog().info("CapacidadAdicional: {}" , calendarioDiaLocalDTO2.getCapacidadAdicional());
					LogSISPE.getLog().info("CapacidadNormal(): {}" , calendarioDiaLocalDTO2.getCapacidadNormal());
					
					//cantidades informativas
					Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
					//cantidad para frio
					CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
					cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
					cantidadFrio.setCantidad((tipoBodega != null && tipoBodega.equals(cantidadFrio.getTipoCantidad())) ? -entregaDetallePedidoDTO.getNpCantidadBultos().longValue() : 0L );
					cantidadesInformativas.add(cantidadFrio);
					//cantidad para seco
					CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
					cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
					cantidadSeco.setCantidad((tipoBodega != null && tipoBodega.equals(cantidadSeco.getTipoCantidad())) ? -entregaDetallePedidoDTO.getNpCantidadBultos().longValue() : 0L );
					cantidadesInformativas.add(cantidadSeco);
					
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),Double.valueOf(-entregaDetallePedidoDTO.getNpCantidadBultos().longValue()), Double.valueOf(0),false,cantidadesInformativas);
					//session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL, calendarioDiaLocalDTO2);
					/**********Va a modificar la cantidad acumulada***********************/
					LocalID localID=new LocalID();
					localID.setCodigoCompania(calendarioDiaLocalDTO2.getId().getCodigoCompania());
					localID.setCodigoLocal(calendarioDiaLocalDTO2.getId().getCodigoLocal());
					EntregaLocalCalendarioUtil.obtenerCalendario(request, localID, errors, calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), null, null, this, null);
					Object[] calendarioDiaLocalDTOOBJ=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
					int dia=-1;
					//Busco el dia de despacho
					LogSISPE.getLog().info("***********Va a buscar el dia de despacho*******************");
					for(int indice=0;indice<calendarioDiaLocalDTOOBJ.length;indice++){
						CalendarioDiaLocalDTO calDia=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indice];
						if(calDia.getId().getFechaCalendarioDia().equals(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia())){
							dia=indice;
							break;
						}
		
					}
					LogSISPE.getLog().info("dia: {}" , dia);
					LogSISPE.getLog().info("fecha de despacho: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
					LogSISPE.getLog().info("fecha de entrega:{} " , entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					EntregaLocalCalendarioUtil.cargaDiasModificarCA(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente(), this, request, dia);
					//Obtengo los dias que debo modificar su capacidad acumulada
					Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
					for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
						LogSISPE.getLog().info("%%%%%%%%%%%%%%%%VA A RESTABLECER LOS VALORES EN EL CALENDARIO%%%%%%%%%%%%%");
						LogSISPE.getLog().info("compania: {}" , calendarioDiaLocalDTOAux.getId().getCodigoCompania());
						LogSISPE.getLog().info("local: {}" , calendarioDiaLocalDTOAux.getId().getCodigoLocal());
						
						//cantidades informativas
						Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
						//cantidad para frio
						CantidadCalendarioDiaLocalDTO cantidadFrioAux = new CantidadCalendarioDiaLocalDTO();
						cantidadFrioAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
						cantidadFrioAux.setCantidad((tipoBodega != null && tipoBodega.equals(cantidadFrio.getTipoCantidad())) ? -entregaDetallePedidoDTO.getNpCantidadBultos().longValue() : 0L );
						cantidadesInformativasAux.add(cantidadFrioAux);
						//cantidad para seco
						CantidadCalendarioDiaLocalDTO cantidadSecoAux = new CantidadCalendarioDiaLocalDTO();
						cantidadSecoAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
						cantidadSecoAux.setCantidad((tipoBodega != null && tipoBodega.equals(cantidadSeco.getTipoCantidad())) ? -entregaDetallePedidoDTO.getNpCantidadBultos().longValue() : 0L );
						cantidadesInformativasAux.add(cantidadSecoAux);
						
						calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(-entregaDetallePedidoDTO.getNpCantidadBultos().longValue()),false,cantidadesInformativasAux);
						LogSISPE.getLog().info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
					}
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCAL, calendarioConfiguracionDiaLocalDTO);
				}
				
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}
	
	/**
	 * Valida las cantidades de las entregas
	 * @author jacalderon
	 * @param errors
	 * @param request
	 * @return
	 */
	public int validarCantidadesEntregas(ActionErrors errors,HttpServletRequest request){
		HttpSession session=request.getSession();
		int indexEntregas=0;//indice para las cantidades de las entregas
		long cantidadMaximaEnrega=0;//cantidad maxima para entregar(faltante por entregar)
		long cantidadMaximaReserva=0;//cantidad maxima para reservar
		long sumaDespachos=0;//numero de bultos por articulo sirve para calcular el valor maximo que se puede reservar
		long sumaEntregas=0;//auxiliar para saber el numero maximo de articulos que se puede entregar
		try{
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX)!=null){
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal1=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX);
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalAux1=calendarioConfiguracionDiaLocal1.copiar();
				session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalAux1);
			}
			LogSISPE.getLog().info("** tama\u00F1o catnidadEntregas ** {}" , cantidadEntregas.length);
			LogSISPE.getLog().info("** tama\u00F1o cantidadDespachos ** {}" , cantidadDespachos.length);
			//Obtengo el detalle del pedido
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
				
				Collection<EntregaDetallePedidoDTO> entregasCol = detallePedidoDTO.getEntregaDetallePedidoCol();
				cantidadMaximaEnrega=0;//Inicio la cantidad maxima de entrega por articulo
				cantidadMaximaReserva=0;//Cantidad maxima de despacho por articulo
				sumaDespachos=0;//inicio la suma de entregas del articulo
				sumaEntregas=0;
				//Si no existe entregas repetidas
				if(entregasCol!=null && entregasCol.size()>0){
					LogSISPE.getLog().info("***********Valida las entregas***********************");
						for(EntregaDetallePedidoDTO entdetPedDTO:entregasCol){
							LogSISPE.getLog().info("entrega: {}" , entdetPedDTO.getCodigoArticulo());
							LogSISPE.getLog().info("indice de entrega: {}" , indexEntregas);
							LogSISPE.getLog().info("entrega original: {}" , entdetPedDTO.getCantidadEntrega());
							LogSISPE.getLog().info("nueva entrega: {}" , this.cantidadEntregas[indexEntregas]);
							//Cantidad maxima de entrega
							cantidadMaximaEnrega=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue()+sumaEntregas+entdetPedDTO.getCantidadEntrega();
							LogSISPE.getLog().info("cantidadMaximaEntrega: {}" , cantidadMaximaEnrega);
							//Si existen cambios en la entrega seteo el nuevo valor
							if(indexEntregas<this.cantidadEntregas.length && !entdetPedDTO.getCantidadEntrega().toString().equals(this.cantidadEntregas[indexEntregas])){
								try{
									entdetPedDTO.setNpCantidadEntrega(new Long(this.cantidadEntregas[indexEntregas]));
								}catch(NumberFormatException e){
									entdetPedDTO.setNpCantidadEntrega(entdetPedDTO.getCantidadEntrega());
									LogSISPE.getLog().info("existio error en el formato de la entrega");
									errors.add("cantidadEntrega", new ActionMessage("error.validacion.cantidadEntrega.invalido",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
								}
								//Si la cantidad ingresada en la entrega es > a la maxima a entregar por defecto se pone la cantidad maxima a entregar
								if(errors.size()==0 && entdetPedDTO.getNpCantidadEntrega().longValue()>cantidadMaximaEnrega){
									entdetPedDTO.setNpCantidadEntrega(cantidadMaximaEnrega);
									LogSISPE.getLog().info("nueva npCantidadEntrega: {}" , entdetPedDTO.getNpCantidadEntrega());
								}
							}
							sumaEntregas=sumaEntregas+entdetPedDTO.getCantidadEntrega().longValue()-entdetPedDTO.getNpCantidadEntrega().longValue();
							LogSISPE.getLog().info("despacho original: {}" , entdetPedDTO.getCantidadDespacho());
							LogSISPE.getLog().info("nuevo despacho: {}" , this.cantidadDespachos[indexEntregas]);
							//Cantidad maxima de reserva
							cantidadMaximaReserva=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorDespacho().longValue()+sumaDespachos+entdetPedDTO.getCantidadDespacho();
							LogSISPE.getLog().info("cantidadMaximaReserva: {}" , cantidadMaximaReserva);
							if(indexEntregas<this.cantidadDespachos.length && !entdetPedDTO.getCantidadDespacho().toString().equals(this.cantidadDespachos[indexEntregas])){
								LogSISPE.getLog().info("**********existe cambios en la reserva********");
								LogSISPE.getLog().info("cantidad maxima a reserva: {}" , cantidadMaximaReserva);
								LogSISPE.getLog().info("entro a validar entrega [{}]: {}" ,indexEntregas, this.cantidadDespachos[indexEntregas]);
								//Valido la cantidad ingresada
								try{
									entdetPedDTO.setNpCantidadDespacho(new Long(this.cantidadDespachos[indexEntregas]));
								}catch(NumberFormatException e){
									entdetPedDTO.setNpCantidadDespacho(entdetPedDTO.getCantidadDespacho());
									LogSISPE.getLog().info("existio error en el formato de la reserva");
									errors.add("cantidadDespacho", new ActionMessage("error.validacion.cantidadDespacho.invalido",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()));
								}
								//Si la cantidad ingresada en la entrega es > a la maxima a entregar por defecto se pone la cantidad maxima a entregar
								if(errors.size()==0 && entdetPedDTO.getNpCantidadDespacho().longValue()>cantidadMaximaReserva){
									entdetPedDTO.setNpCantidadDespacho(cantidadMaximaReserva);
									LogSISPE.getLog().info("nueva npCantidadDespacho: {}" , entdetPedDTO.getNpCantidadDespacho());
								}
								if(entdetPedDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null){
									/*******************************Verificar capacidad disponible***************/
									LogSISPE.getLog().info("va a verificar CD de la entrega modificada");
									//Calculo cuantos bultos voy a sumar o restar del detalle a modificar
									long bultosModificado=UtilesSISPE.calcularCantidadBultos(entdetPedDTO.getNpCantidadDespacho(), detallePedidoDTO.getArticuloDTO());
									LogSISPE.getLog().info("numero de bultos a modificar: {}" , bultosModificado);
									LogSISPE.getLog().info("numero de bultos anteriores: {}" ,entdetPedDTO.getNpCantidadBultos());
									long totalBultosModificado=bultosModificado-entdetPedDTO.getNpCantidadBultos();
									LogSISPE.getLog().info("numero de bultos modificado: {}" , totalBultosModificado);
									this.verificaCD(detallePedidoDTO,entdetPedDTO,errors,request,totalBultosModificado);
									sumaDespachos=sumaDespachos+entdetPedDTO.getCantidadDespacho().longValue()-entdetPedDTO.getNpCantidadDespacho().longValue();
								}
							}
							indexEntregas++;
						}
						/**********************************************************/
				}
			}
		}catch(Exception e){
			LogSISPE.getLog().info("error en form: {}" , e);
		}
		return errors.size();
	}
	
	
	/**
	 * Verifica la capacidad disponible.
	 * @author jacalderon
	 * @param detallePedidoDTO
	 * @param entregaDTO
	 * @param errors
	 * @param request
	 * @param sumaBultosEntregas
	 */
	public void verificaCD(DetallePedidoDTO detallePedidoDTO, EntregaDetallePedidoDTO entregaDTO, ActionErrors errors, HttpServletRequest request, long sumBulEnt)
	{
		LogSISPE.getLog().info("Entra a verificar la capacidad disponible");
		long sumaBultosEntregas = sumBulEnt;
		
		try {
			HttpSession session = request.getSession();
			Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
			//cantidad para frio
			CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
			cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
			//cantidad para seco
			CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
			cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
			
			//Obtengo el calendario del dia de la entrega
			CalendarioDiaLocalID calendarioDiaLocalID=new CalendarioDiaLocalID();
			calendarioDiaLocalID.setCodigoCompania(entregaDTO.getId().getCodigoCompania());
			if(entregaDTO.getEntregaPedidoDTO().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local")))
				calendarioDiaLocalID.setCodigoLocal(entregaDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
			else
				calendarioDiaLocalID.setCodigoLocal(entregaDTO.getEntregaPedidoDTO().getCodigoLocalSector().intValue());
			Date mes=DateManager.getYMDDateFormat().parse(entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega().toString());
			calendarioDiaLocalID.setFechaCalendarioDia(mes);
			//objeto para manejar la capacidad disponible en linea
			
			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = null;
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1)!=null){
				LogSISPE.getLog().info("si tiene configuracion");
				calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1);
			}

			CalendarioDiaLocalDTO calendarioDiaLocalDTO=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
			LogSISPE.getLog().info("totalBultos Entrega: {}" , sumaBultosEntregas);
			LogSISPE.getLog().info("capacidad disponible: {}" ,calendarioDiaLocalDTO.getCantidadDisponible());
			
			//Pregunto si la cantidad modificada es mayor o menor a la anterior
			//Si es mayor verifico la CD
			if(sumaBultosEntregas>0){
				//cantidad para frio
				cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? sumaBultosEntregas : 0L );
				cantidadesInformativas.add(cantidadFrio);
				//cantidad para seco
				cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? sumaBultosEntregas : 0L );
				cantidadesInformativas.add(cantidadSeco);
				
				//Verifico si existe CD y si existe o tiene autorizacion cambio la configuracion del calendario
				if(Long.valueOf(ConverterUtil.fromDoubleToInteger(calendarioDiaLocalDTO.getCantidadDisponible())).longValue() < sumaBultosEntregas){
					LogSISPE.getLog().info("no hay capacidad: {}" , entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
					if(!calendarioDiaLocalDTO.getId().getCodigoLocal().toString().equals((SessionManagerSISPE.getCurrentLocal(request)).toString())){
						AutorizacionDTO autorizacionDTO = buscaAutorizacion(session,calendarioDiaLocalID.getCodigoLocal().toString(),entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
						if(autorizacionDTO == null){
								entregaDTO.setNpCantidadDespacho(entregaDTO.getCantidadDespacho());
							errors.add("cantidadEntrega",new ActionMessage("errors.capacidadDisponibleEntrega",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo(),detallePedidoDTO.getId().getCodigoAreaTrabajo()));
						}else if(calendarioConfiguracionDiaLocalDTO != null){
							entregaDTO.getEntregaPedidoDTO().setAutorizacionDTO(autorizacionDTO);
							calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(sumaBultosEntregas), calendarioDiaLocalDTO.getCantidadAcumulada(),true,cantidadesInformativas);
							session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
						}
					}else if(calendarioConfiguracionDiaLocalDTO != null){
						calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(sumaBultosEntregas), calendarioDiaLocalDTO.getCantidadAcumulada(),false,cantidadesInformativas);
						session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
					}
				}else if(calendarioConfiguracionDiaLocalDTO != null){
					LogSISPE.getLog().info("si hay capacidad");
					/**************actualizo la capacidad disponible en el local para el despacho***********/
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(sumaBultosEntregas), calendarioDiaLocalDTO.getCantidadAcumulada(),false,cantidadesInformativas);
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
				}
			}
			//Si es menor modifico el calendario
			else if(sumaBultosEntregas<0 && calendarioConfiguracionDiaLocalDTO != null){
				sumaBultosEntregas=-sumaBultosEntregas;
				
				//cantidad para frio
				cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? sumaBultosEntregas : 0L );
				cantidadesInformativas.add(cantidadFrio);
				//cantidad para seco
				cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? sumaBultosEntregas : 0L );
				cantidadesInformativas.add(cantidadSeco);
				
				if(entregaDTO.getEntregaPedidoDTO().getAutorizacionDTO()==null)
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(sumaBultosEntregas), calendarioDiaLocalDTO.getCantidadAcumulada(),false,cantidadesInformativas);
				else
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO.getId().getCodigoCompania(), calendarioDiaLocalDTO.getId().getCodigoLocal(), calendarioDiaLocalDTO.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO.getCapacidadNormal(), calendarioDiaLocalDTO.getCapacidadAdicional(), Double.valueOf(sumaBultosEntregas), calendarioDiaLocalDTO.getCantidadAcumulada(),true,cantidadesInformativas);
				session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
			}
			LogSISPE.getLog().info("error: {}" , errors.size());
		}catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}	
	
	/**
	 * Elimina el costo y direcciones de entrega
	 * @author jacalderon
	 * @param  entregaDTO				El objeto entrega
	 * @param  session
	 */
	public void eliminaCostoEntrega(EntregaDetallePedidoDTO entregaDetallePedidoDTO, HttpServletRequest request) throws Exception
	{
		HttpSession session = request.getSession();
		
		//String valorTotalEntregaS = EntregaLocalCalendarioAction.VALORTOTALENTREGAAUX;
		String costoEntregaS = EntregaLocalCalendarioAction.COSTOENTREGAAUX;
		String direccionesS = EntregaLocalCalendarioAction.DIRECCIONESAUX;
		if(session.getAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL)!=null){ //cuando se elimina desde la pantalla del detallePedido
			//valorTotalEntregaS = EntregaLocalCalendarioAction.VALORTOTALENTREGA;
			costoEntregaS = EntregaLocalCalendarioAction.COSTOENTREGA;
			direccionesS = EntregaLocalCalendarioAction.DIRECCIONES;
		}
		
		if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
			//Double costoEntrega = (Double)session.getAttribute(valorTotalEntregaS);
			Collection<CostoEntregasDTO> costoEntregasDTOCol = (Collection<CostoEntregasDTO>)session.getAttribute(costoEntregaS);
			Collection<CostoEntregasDTO> costoEntregasEliminarDTOCol = new ArrayList<CostoEntregasDTO>(costoEntregasDTOCol.size());
			//Busco las entregas de un mismo sector en una misma fecha
			if(costoEntregasDTOCol!=null && costoEntregasDTOCol.size()>0){
				LogSISPE.getLog().info("existen costos");
				for(CostoEntregasDTO costoEntregasDTO : costoEntregasDTOCol){
					LogSISPE.getLog().info("entrega.fechaEntrega:{}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					LogSISPE.getLog().info("costo.fechaEntrega: {}" , costoEntregasDTO.getFechaEntrega());
					LogSISPE.getLog().info("entrega.sector: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
					LogSISPE.getLog().info("costo.sector: {}" , costoEntregasDTO.getCodigoSector());
					
					//recorrer las entregas para validar sus costos
//					for(EntregaDetallePedidoDTO entPedDTO : costoEntregasDTO.getEntregaDetallePedidoCol()){
							if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().equals(costoEntregasDTO.getFechaEntrega()) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().equals(costoEntregasDTO.getCodigoSector())){
								LogSISPE.getLog().info("numero de costo1: {}" , costoEntregasDTO.getNumeroEntregas());
								costoEntregasDTO.setNumeroEntregas(costoEntregasDTO.getNumeroEntregas() - 1);
								LogSISPE.getLog().info("numero de costo2: {}" , costoEntregasDTO.getNumeroEntregas());
								
								final String codigoArticulo = entregaDetallePedidoDTO.getCodigoArticulo();
								LogSISPE.getLog().info("Articulo relacionado a la entrega eliminada: {}" , codigoArticulo);
								Collection<EntregaDetallePedidoDTO> entDetBusCol = CollectionUtils.select(costoEntregasDTO.getEntregaDetallePedidoCol(), new Predicate() {
									public boolean evaluate(Object arg0) {
										EntregaDetallePedidoDTO dto = (EntregaDetallePedidoDTO) arg0;
										return StringUtils.equals(dto.getCodigoArticulo(), codigoArticulo);
									}
								});
								
								costoEntregasDTO.getEntregaDetallePedidoCol().removeAll(entDetBusCol);
								
								Double valorParcialEntrega = 0D;
								if(!CollectionUtils.isEmpty(costoEntregasDTO.getEntregaDetallePedidoCol())){
									for (EntregaDetallePedidoDTO entDetPedDTO : costoEntregasDTO.getEntregaDetallePedidoCol()) {
										valorParcialEntrega += entDetPedDTO.getEntregaPedidoDTO().getCostoParcialEntrega()==null?0D:entDetPedDTO.getEntregaPedidoDTO().getCostoParcialEntrega();
									}
								}

								
								Double valorEntregaTotal = Double.valueOf(
										WebSISPEUtil.costoEntregaDistancia(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega(), request, 
												valorParcialEntrega, entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion1"))));
								
								costoEntregasDTO.setValor(valorEntregaTotal);
								
								if(costoEntregasDTO.getNumeroEntregas() == 0){
									LogSISPE.getLog().info("elimina el costo: {}" , costoEntregasDTO.getNombreSector());
									//costoEntrega = Double.valueOf(costoEntrega.doubleValue() - costoEntregasDTO.getValor());
									costoEntregasEliminarDTOCol.add(costoEntregasDTO);
//									costoEntregasDTOCol.remove(costoEntregasDTO);
									
		//							session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_ELIMINO_COSTO, "ok"); 
									
		//							session.setAttribute(EntregaLocalCalendarioAction.COSTOENTREGA, costoEntregasDTOCol);
									//session.setAttribute(valorTotalEntregaS, costoEntrega);
									//LogSISPE.getLog().info("Costo del flete: {}" , costoEntrega);
									//seteando el valor del costo cuando se elimina
									//session.setAttribute(EntregaLocalCalendarioAction.VALORTOTALENTREGA,costoEntrega);
									break;
								}
							}
					}
//				}
				//ELIMINAR COSTO DE LAS ENTREGAS
				costoEntregasDTOCol.removeAll(costoEntregasEliminarDTOCol);
				
				//actualizamos las variables de session relacionadas con los costos
//				session.setAttribute(EntregaLocalCalendarioAction.COSTOENTREGAAUX, costoEntregasDTOCol);
//				session.setAttribute(EntregaLocalCalendarioAction.COSTOENTREGA, costoEntregasDTOCol);
			}
			/*****************************Elimina Direcciones*******************************/
			Collection<DireccionesDTO> direccionesDTOCol = (Collection<DireccionesDTO>)session.getAttribute(direccionesS);
			Collection<DireccionesDTO> direccionesEliminarCol = new ArrayList<DireccionesDTO>(direccionesDTOCol.size());
			
			//Busco las entregas de una misma direccion
			if(direccionesDTOCol!=null && direccionesDTOCol.size()>0){
				LogSISPE.getLog().info("existen direcciones");
				for(DireccionesDTO direccionesDTO:direccionesDTOCol){
					LogSISPE.getLog().info("esta iterando");
					if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio().equals(direccionesDTO.getCodigoDireccion())){
						LogSISPE.getLog().info("numero de direccion1: {}" , direccionesDTO.getNumeroDireccion());
						direccionesDTO.setNumeroDireccion(direccionesDTO.getNumeroDireccion()-1);
						LogSISPE.getLog().info("numero de direccion2: {}" , direccionesDTO.getNumeroDireccion());
						if(direccionesDTO.getNumeroDireccion()==0){
							LogSISPE.getLog().info("elimina la direccion: {}" , direccionesDTO.getDescripcion());
							direccionesEliminarCol.add(direccionesDTO);
//							direccionesDTOCol.remove(direccionesDTO);
							session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_ELIMINO_DIRECCION, "ok");
							break;
						}
					}
				}
				//ELIMINAR DIRECCIONES
				direccionesDTOCol.removeAll(direccionesEliminarCol);
			}
		}
		
//		// actualizar el valor del costo entrega direccion que se muestra en el resumen de las entregas del pedido
//		CotizarReservarForm.asignarCostoEntregaDireccion(request, subTotal, Boolean.TRUE);
		session.removeAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL);

	}

	/**
	 * Comprueba si existe una autorizacion para una fecha de despacho
	 * @author jacalderon
	 * @param  entregaDTO				El objeto entrega
	 * @param  session
	 */
	public AutorizacionDTO buscaAutorizacion(HttpSession session,String codigoLocal, Timestamp fecha){
		Collection<AutorizacionEntregasDTO> autorizacionEntregasWDTOCol =(Collection<AutorizacionEntregasDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol");
		AutorizacionDTO autorizacionDTO=null;
		if (autorizacionEntregasWDTOCol!=null){
			LogSISPE.getLog().info("autorizaciones: {}" , autorizacionEntregasWDTOCol.size());
			for (AutorizacionEntregasDTO autorizacionEntregasWDTO : autorizacionEntregasWDTOCol) {
				
				LogSISPE.getLog().info("local: {} - {}" , autorizacionEntregasWDTO.getCodigoLocal(), codigoLocal);
				LogSISPE.getLog().info("fecha: {} - {}" , autorizacionEntregasWDTO.getFechaAutorizacion(), fecha);
				if(autorizacionEntregasWDTO.getCodigoLocal().equals(codigoLocal) && fecha.equals(autorizacionEntregasWDTO.getFechaAutorizacion())){
					LogSISPE.getLog().info("existe una autorizacion");
					autorizacionDTO=autorizacionEntregasWDTO.getAutorizacionDTO();
				}
			}
		}
		return(autorizacionDTO);
	}
	
	/**
	 * Comprueba si el numero de autorizacion ingresado ya ha sido utilizado en otro dia
	 * @author jacalderon
	 * @param session
	 * @param numeroAutorizacion
	 * @return
	 */
	public boolean buscaNumeroAutorizacion(HttpSession session,String numeroAutorizacion){
		boolean existeAutorizacion = false;
		Collection<AutorizacionEntregasDTO> autorizacionEntregasWDTOCol =(Collection<AutorizacionEntregasDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol");
		if (autorizacionEntregasWDTOCol!=null){
			LogSISPE.getLog().info("autorizaciones: {}" , autorizacionEntregasWDTOCol.size());
			for (AutorizacionEntregasDTO autorizacionEntregasWDTO : autorizacionEntregasWDTOCol) {
				
				if(autorizacionEntregasWDTO.getNumeroAutorizacion().equals(numeroAutorizacion)){
					LogSISPE.getLog().info("existe un dia con esa autorizacion");
					existeAutorizacion=true;
				}
			}
		}
		LogSISPE.getLog().info("existeAutorizacion: {}" , existeAutorizacion);
		return existeAutorizacion;
	}
	
	/**
	 * Calcula el costo total de una reservaci\u00F3n
	 * @param  request			- Peici\u00F3n HTTP actual
	 * @throws Exception
	 */
	public void actualizarCostoTotalPedido(HttpServletRequest request)throws Exception
	{
		LogSISPE.getLog().info(" -- actualizarCostoTotalPedido -- ");
		
		String accionActual=(String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		Double porcentajeCalculoCostoFlete = (Double)request.getSession().getAttribute(CotizarReservarAction.PORCENTAJE_CALCULO_FLETE);
		ArrayList<CostoEntregasDTO> colCostoEntrega = (ArrayList<CostoEntregasDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
//		Double costoTotalEntregaP = (Double)request.getSession().getAttribute(EntregaLocalCalendarioAction.VALORTOTALENTREGA); //valor cuando se modifica entregas
		Double valorFlete = 0D;
		
		//si la coleccion de costo fue inicializada y no tiene elementos se eliminaron todas las entregas desde el detalle del pedido
		if (colCostoEntrega != null && colCostoEntrega.size()==0){
			this.costoFlete = valorFlete;
			this.total = this.subTotal + this.costoFlete;
		}
			
		if (!CollectionUtils.isEmpty(colCostoEntrega) && porcentajeCalculoCostoFlete!=null){
			
			Double porcentajeValorTotal = this.subTotal * (porcentajeCalculoCostoFlete/100);
			Double porcentajeValorTotalRound = Util.roundDoubleMath(porcentajeValorTotal,NUMERO_DECIMALES);
			
			LogSISPE.getLog().info("Porcentaje 2% del total pedido: {}", porcentajeValorTotalRound);
			
			for(CostoEntregasDTO costoEntregaDTO : colCostoEntrega){
				LogSISPE.getLog().info("costo entrega: {}", costoEntregaDTO.getValor());
				
				if(costoEntregaDTO.getValor() > porcentajeValorTotalRound.doubleValue()){
					double diferenciaCostoEntrega = costoEntregaDTO.getValor() - porcentajeValorTotalRound.doubleValue();
//					this.costoFlete = Double.valueOf(diferenciaCostoEntrega);
					valorFlete += Double.valueOf(diferenciaCostoEntrega);
//					costoEntregaDTO.setValorFlete(valorFlete);
//					this.total = this.costoFlete + this.subTotal;
				}else{
//					this.costoFlete = 0D;
					valorFlete += 0D;
//					this.total = this.subTotal;
				}
			}
			this.costoFlete = valorFlete;
			this.total = this.costoFlete + this.subTotal;
			
		}
		else if (this.costoFlete!= null && this.costoFlete > 0){
			this.total = this.subTotal + this.costoFlete;
		
		}
		
		//si la accion es pedido consolidado
		if (accionActual != null && accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))
				&& request.getSession().getAttribute(CotizacionReservacionUtil.COSTO_FLETE_ENTREGA_PEDIDO_CONSOLIDADO) != null){
			this.costoFlete = (Double) request.getSession().getAttribute(CotizacionReservacionUtil.COSTO_FLETE_ENTREGA_PEDIDO_CONSOLIDADO);
			this.total = this.subTotal + this.costoFlete;
		}
		
		//se sube a sesi\u00F3n el costo del flete
		request.getSession().setAttribute(CotizarReservarAction.COSTO_FLETE, this.costoFlete);
		
		//se llama al m\u00E9todo que actualiza el abono
		this.actualizarAbono(request,this.total); //se actualiza el valor del abono
	}
	
//	/**
//	 * Asignar el valor del costo del flete de la entrega a la direccion correspondiente
//	 * @param colDireccionesDTOs
//	 * @param colCostoEntregasDTOs
//	 * @throws Exception 
//	 */
//	public static void asignarCostoEntregaDireccion(HttpServletRequest request, Double subTotal, Boolean eliminarEntregas) throws Exception{
//		
//		HttpSession session = request.getSession();
//		
//		Collection<CostoEntregasDTO> colCostoEntregasDTOs = null;
//		Collection<DireccionesDTO> colDireccionesDTOs = null;
//		
//		ParametroDTO parametroPorcentajeCalculoFlete = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentajeCalculoFlete", request);
//		Double porcentajeCalculoCostoFlete = Double.valueOf(parametroPorcentajeCalculoFlete.getValorParametro());
//		
//		//cuando se debe actualizar los costos despues de que se elimino una entrega
//		if(eliminarEntregas){
//			
//			colCostoEntregasDTOs = (Collection<CostoEntregasDTO>) session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGAAUX);
//			colDireccionesDTOs = (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX);
//			if(session.getAttribute(CotizarReservarAction.ELIMINACION_DESDE_DET_PRINCIPAL)!=null){ //cuando se elimina desde la pantalla del detallePedido
//				colCostoEntregasDTOs = (Collection<CostoEntregasDTO>) session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
//				colDireccionesDTOs = (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONES);
//			}
//			
//		} else { //cuando recien se calcula el valor de la entrega, o se carga la pantalla por primera ves
//			
//			if (session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA) != null && session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONES) != null){
//				colCostoEntregasDTOs = (Collection<CostoEntregasDTO>) session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
//				colDireccionesDTOs = (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONES);
//				
//			} else {
//				colCostoEntregasDTOs = (Collection<CostoEntregasDTO>) session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGAAUX);
//				colDireccionesDTOs = (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX);
//			}
//		}
//		
//		if(!CollectionUtils.isEmpty(colCostoEntregasDTOs) && !CollectionUtils.isEmpty(colDireccionesDTOs) 
//				&& porcentajeCalculoCostoFlete != null){
//			
//			Double porcentajeValorTotal = subTotal * (porcentajeCalculoCostoFlete/100);
//			Double porcentajeValorTotalRound = Util.roundDoubleMath(porcentajeValorTotal,NUMERO_DECIMALES);
//			
//			for(DireccionesDTO dtoDireccion : colDireccionesDTOs){
//				
//				for(CostoEntregasDTO dtoCosto : colCostoEntregasDTOs){
//					
//					if(dtoDireccion.getDescripcion().trim().equals(dtoCosto.getDescripcion().trim())
//							&& dtoDireccion.getDistanciaDireccion().trim().equals(String.valueOf(dtoCosto.getDistancia()).trim())){
//						
//						//inicializamos el valor, si es necesario
//						if(dtoDireccion.getValorFlete()==null){
//							dtoDireccion.setValorFlete(0D);
//						}
//						
//						if(dtoCosto.getValor() > porcentajeValorTotalRound.doubleValue()){
//							double diferenciaCostoEntrega = dtoCosto.getValor() - porcentajeValorTotalRound.doubleValue();
//							dtoDireccion.setValorFlete(diferenciaCostoEntrega);
//						}else{
//							dtoDireccion.setValorFlete(0D + dtoDireccion.getValorFlete());
//						}
//						
//					}
//					
//				}
//			}
//			//actualizamos la coleccion que muestra en pantalla el detalle de las direcciones
//			session.setAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX, colDireccionesDTOs);
//		}
//	}
	
	/**
	 * Actualiza el valor m\u00EDnimo del abono.
	 * @param session			La sesi\u00F3n actual
	 * @param accion			Accci\u00F3n actual que se est\u00E1 ejecutando
	 * @param totalPedido		El valor total del pedido UU
	 * @throws Exception.
	 */
	public void actualizarAbono(HttpServletRequest request, Double totalPedido) throws Exception
	{
		//se otiene la sesi\u00F3n
		HttpSession session = request.getSession(true);		
		
		String accion = session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).toString();
		
		if(session.getAttribute(CotizarReservarAction.PORCENTAJE_ABONO)!=null){
			//recalculo del valor del abono m\u00EDnimo
			double porcentajeAbono = ((Double)session.getAttribute(CotizarReservarAction.PORCENTAJE_ABONO)).doubleValue();
			Double totalFormateado = Util.roundDoubleMath(totalPedido,NUMERO_DECIMALES);
			double valorAbono = totalFormateado.doubleValue() * porcentajeAbono/100;
			Double valorAbonoFormateado = Util.roundDoubleMath(Double.valueOf(valorAbono),NUMERO_DECIMALES);
			LogSISPE.getLog().info("valorAbonoFormateado: {}",valorAbonoFormateado);
			//se almacena en sesi\u00F3n el porcentaje y el abono
			session.setAttribute(CotizarReservarAction.VALOR_ABONO,valorAbonoFormateado);			
			LogSISPE.getLog().info("SE ACTUALIZO EL VALOR ABONO MANUAL");
			//se asigna el valor calculado por el sistema al campo del formulario
			//this.valorAbono = valorAbonoFormateado.toString();
			//condici\u00F3n adicional por los decimales
			if(valorAbonoFormateado.doubleValue()<=0){
				session.setAttribute(CotizarReservarAction.VALOR_ABONO, Double.valueOf(0));
			}
		}
		if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
			//se calcula el posible valor de la devoluci\u00F3n al cliente
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			if(vistaPedidoDTO != null){
				
				Double totalReservacion = Util.roundDoubleMath(this.total,NUMERO_DECIMALES);
				//Double devolucionAbono = 0D;
				//Si va existir una devolucion en el pedido
				Double devolucionTotal = vistaPedidoDTO.getAbonoPedido() - totalReservacion;
				
				if(devolucionTotal>0 && 
						((vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO))
							||vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL))){
					CotizacionReservacionUtil.compararDetallePedido(request);
//					if(!CollectionUtils.isEmpty(detallePedido)){
						//se calcula la devolucion solo de los items que se restaron del pedido
//						devolucionAbono = devolucionTotal;//CotizacionReservacionUtil.calcularValorDevolucion(detallePedido);
//					}
				}
//				else {
//					devolucionAbono = devolucionTotal;
//				}
				
				if(devolucionTotal > 0){
					
					//cuando se registra la primera devolucion
					session.setAttribute(DEVOLUCION_ABONO, Util.roundDoubleMath(devolucionTotal, NUMERO_DECIMALES));
				}else{
					session.removeAttribute(DEVOLUCION_ABONO);
					this.setValorAbono(String.valueOf(vistaPedidoDTO.getValorAbonoInicialManual()));//aca ingresa cuando el pedido no tiene una dev previa					
				}
			}
		}
	}
		

	/**
	 * Actualiza el valor de los descuentos.
	 * @param session			La sesi\u00F3n actual
	 * @param accion			Accci\u00F3n actual que se est\u00E1 ejecutando
	 * @param totalPedido		El valor total del pedido 
	 * @throws Exception.
	 */
	public void actualizarDescuentos(final HttpServletRequest request,ActionMessages messages) throws Exception
	{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession(true);
		//se obtiene de la sesion los datos del detalle y de la lista de articulos.
		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		ConsolidarAction.asignarAutorizacionesDetallesConsolidados(request, colDetallePedidoDTO);
		
		//Collection<DetallePedidoDTO> colDetallePedidoConsolidados = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
		//Sumar los valores y cantidades al pedido general y para verificar si aplica o no un descuento
		String pedidoGeneral= (String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
		ArrayList<DetallePedidoDTO> detallePedidoConsolidado =new ArrayList<DetallePedidoDTO>();
		if(pedidoGeneral==null){
			if(vistaPedidoDTO!=null){
				for(DetallePedidoDTO detallePedidoDTO:colDetallePedidoDTO ){
					if(detallePedidoDTO.getId().getCodigoPedido().equals("-1")){
						 detallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
					}
				}
			}
			detallePedidoConsolidado = ConsolidarAction.sumarValoresCantidadesPedidoGeneral(this, session, colDetallePedidoDTO);
		}
		if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){
			
			//si no existe una cola de autorizaciones, tampoco debe existir descuentos variables	
			if(CollectionUtils.isEmpty((ArrayList<EstadoPedidoAutorizacionDTO>)request.getSession().getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES))){
				session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
			}
			
			//declaraci\u00F3n de las variables
			double totalDescuento = 0;
			double totalPorcentaje = 0;
			Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);	
			llaveDescuentoCol = CotizacionReservacionUtil.eliminarLlavesRepetidas(llaveDescuentoCol);
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO;// = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS); 
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);		
			Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
			
			if(CollectionUtils.isEmpty(colDescuentoEstadoPedidoDTOVariables)){
				colDescuentoEstadoPedidoDTOVariables = new ArrayList<DescuentoEstadoPedidoDTO>();
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
//				Collection<String> llavesActuales=new ArrayList<String>(); 
//				String llave=((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS));
//				for(String llaveColSel: llaveDescuentoCol){
//					if(llaveColSel.equals(llave)){
//						llave=((String)request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA));
//					}else{
//						llavesActuales.add(llaveColSel);
//					}
//				}
				llaveDescuentoCol=new ArrayList<String>(); 
				llaveDescuentoCol=(Collection<String>)SerializationUtils.clone((Serializable)llavesActuales);
			}
			try{
				Collection<VistaPedidoDTO> pedidosConsolidados= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				if(CollectionUtils.isNotEmpty(llaveDescuentoCol)){ 
					
					//se procesan los descuentos variables y normales
					colDescuentoEstadoPedidoDTO = DescuentosUtil.procesarDescuentos(request, colDetallePedidoDTO, llaveDescuentoCol, colDescuentoEstadoPedidoDTOVariables);
					
					if(pedidoGeneral==null){
						List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
						if(vistaPedidoDTO!=null){
							for(DetallePedidoDTO detPed:colDetallePedidoDTO){
								if(!vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
									detallesEliminar.add(detPed);
								}
							}
							colDetallePedidoDTO.removeAll(detallesEliminar);
						}
					}
					if(colDescuentoEstadoPedidoDTO == null || colDescuentoEstadoPedidoDTO.isEmpty()){
						LogSISPE.getLog().info("ELIMINAR DESCUENTOS");
						//Eliminamos los descuentos de los pedidos consolidados
						//CotizacionReservacionUtil.eliminarDescuentosConsolidados(detallePedidoConsolidado);
						String existNavEmpCredito = CotizacionReservacionUtil.validarExisteDesNavEmpCredito(request);			
						if (existNavEmpCredito == null) {							
							if(session.getAttribute(CotizarReservarAction.SE_AGREGO_ARTICULO_OBSOLETO)!=null && !session.getAttribute(CotizarReservarAction.SE_AGREGO_ARTICULO_OBSOLETO).equals("ok")){
								CotizacionReservacionUtil.agregarActionMessageNoRepetido("message.descuentos.noAplicados", "errorDescuentos", null, messages);
							}
						}
						
						
						session.removeAttribute(CotizarReservarAction.DES_MAX_NAV_EMP);
						//se crea un WARNING
						if(session.getAttribute(CotizarReservarAction.SE_AGREGO_ARTICULO_OBSOLETO)!=null &&  !session.getAttribute(CotizarReservarAction.SE_AGREGO_ARTICULO_OBSOLETO).equals("ok")){
							request.setAttribute(Globals.WARNINGS_KEY,messages);
						}
						session.setAttribute(CotizarReservarAction.SE_AGREGO_ARTICULO_OBSOLETO, "");
					}else{
						//Obtengo el parametro para descuentos variables
						ParametroDTO parametroDTO=WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
						//Obtengo la autorizacion
						AutorizacionDTO autorizacionDTO=(AutorizacionDTO)session.getAttribute(CotizarReservarAction.AUTORIZACION_DESCUENTOS);
						//se realiza la suma de los descuentos
						for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO: colDescuentoEstadoPedidoDTO){
							totalDescuento = totalDescuento + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							totalPorcentaje = totalPorcentaje + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
							//Si el descuento es de tipo varible asigno la autorizacion
							if(autorizacionDTO!=null && descuentoEstadoPedidoDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(parametroDTO.getValorParametro())){
								LogSISPE.getLog().info("existe autorizacion para descuento variable");
								descuentoEstadoPedidoDTO.setAutorizacionDTO(autorizacionDTO);
							}
						}
					}
					LogSISPE.getLog().info("TOTAL DESCUENTOS: {}",totalDescuento);
					this.setDescuentoTotal(Double.valueOf(totalDescuento));
					
					session.setAttribute(CotizarReservarAction.COL_DESCUENTOS, colDescuentoEstadoPedidoDTO);
					session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llaveDescuentoCol);
					//se guarda en sesi\u00F3n el total de descuentos
					session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(totalDescuento));
					//se guarda en sesi\u00F3n el porcentaje total de los descuentos
					session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(totalPorcentaje));
					//se elimina la variable que indica que se debe actualizar los descuentos
					//session.removeAttribute("ec.com.smx.sic.sispe.pedido.descuentoSinActualizar");
					//llamamos a la funcion que permite aplicar los descuentos solo a los pedidos que estan consolidados
					ConsolidarAction.aplicarDescuentosConsolidados(request, session,	colDetallePedidoDTO,descuentoVariableCol,llaveDescuentoCol,messages);
					
					//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
					if(session.getAttribute(DescuentosUtil.MENSAJE_NO_APLICA_MARCA_PROPIA) != null){
						request.getSession().removeAttribute(DescuentosUtil.MENSAJE_NO_APLICA_MARCA_PROPIA);
						
						if(request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia") != null && request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") != null){
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							messages.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia.conCantidad",
									request.getSession().getAttribute("ec.com.smx.sic.sispe.cantidad.marca.propia"),
									request.getSession().getAttribute("ec.com.smx.sic.sispe.porcentaje.pedido.marcaPropia"),
									(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") ,(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia")));
						}else{
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							messages.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia"));	
						}
					}
				}
				else if(pedidosConsolidados!=null){
					//Eliminar los descuentos del pedido actual y de todos los pedidos consolidados
					session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS);
					session.removeAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
					session.removeAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
					//session.removeAttribute("ec.com.smx.sic.sispe.pedido.descuentoSinActualizar");
					//se inicializan las variables de los descuentos
					session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL,Double.valueOf(0));
					session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO,Double.valueOf(0));
					this.setDescuentoTotal(Double.valueOf(0));

					//llamada al m\u00E9todo que quita los descuentos
					SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(colDetallePedidoDTO,null, null);
					if(pedidoGeneral==null){
						List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
						for(DetallePedidoDTO detPed:colDetallePedidoDTO){
							if(!vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
								detallesEliminar.add(detPed);
							}
						}
						colDetallePedidoDTO.removeAll(detallesEliminar);
					}
					//exitos.add("sinDescuentos",new ActionMessage("message.sinDescuentos"));
					
					//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
					CotizacionReservacionUtil.calcularValoresFinalesPedido(request, colDetallePedidoDTO, this);
					session.setAttribute("ec.com.smx.sic.sispe.sinDescuentos","SI");
					ConsolidarAction.aplicarDescuentosConsolidados(request, session,	colDetallePedidoDTO,null,null,messages);
					if(session.getAttribute(CotizarReservarAction.OCULTAR_BOTON_DESCUENTOS) != null)
						session.setAttribute(CotizarReservarAction.RESPALDO_DESCUENTOS_CONSOLIDADOS, session.getAttribute(CotizarReservarAction.COL_DESCUENTOS));
				}
				else{
					//Eliminar los descuentos del pedido actual y de todos los pedidos consolidados
					session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS);
					session.removeAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
					session.removeAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
					//session.removeAttribute("ec.com.smx.sic.sispe.pedido.descuentoSinActualizar");
					//se inicializan las variables de los descuentos
					session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL,Double.valueOf(0));
					session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO,Double.valueOf(0));
					this.setDescuentoTotal(Double.valueOf(0));

					//llamada al m\u00E9todo que quita los descuentos
					SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(colDetallePedidoDTO,null, null);
					
					//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
					if(session.getAttribute(DescuentosUtil.MENSAJE_NO_APLICA_MARCA_PROPIA) != null){
						request.getSession().removeAttribute(DescuentosUtil.MENSAJE_NO_APLICA_MARCA_PROPIA);
						
						if(request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia") != null && request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") != null){
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							messages.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia.conCantidad",
									request.getSession().getAttribute("ec.com.smx.sic.sispe.cantidad.marca.propia"),
									request.getSession().getAttribute("ec.com.smx.sic.sispe.porcentaje.pedido.marcaPropia"),
									(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido") ,(Double)request.getSession().getAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia")));
						}else{
							//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
							messages.add("noAplicaMarcaPropia", new ActionMessage("errors.descuentos.no.aplica.descuento.marcaPropia"));	
						}
					}
				}
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				throw new Exception();
			}
			
		}else{
			if(detallePedidoConsolidado==null || detallePedidoConsolidado.isEmpty()){
				this.setDescuentoTotal(0d);
				session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS);
				session.removeAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
				session.removeAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
	
				//se guarda en sesi\u00F3n el total de descuentos
				session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(0));
				//se guarda en sesi\u00F3n el porcentaje total de los descuentos
				session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(0));
			}
			else{
				this.setDescuentoTotal(0d);
				session.removeAttribute(CotizarReservarAction.COL_DESCUENTOS);
				//se guarda en sesi\u00F3n el total de descuentos
				session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(0));
				//se guarda en sesi\u00F3n el porcentaje total de los descuentos
				session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(0));
			}
		}	
		CotizacionReservacionUtil.calcularValoresFinalesPedido(request, colDetallePedidoDTO, this);
	}

	/**
	 * Verifica que las cantidades y los precios del detalle no se hayan modificado antes de 
	 * registrar el pedido.
	 * @param 	detallePedido			Colecci\u00F3n del detalle del pedido.
	 * @param 	errors						Collecci\u00F3n de errores manejados en el formulario.
	 * @param 	session						La sesi\u00F3n actual.
	 * @param 	accion 						El par\u00E1metro para el <code>ActionMessage</code> en caso de error.
	 * @throws 	Exception.
	 */
	public void verificarValoresDetallePedido(Collection<DetallePedidoDTO> detallePedido,ActionErrors errors, HttpServletRequest request, String accion) throws Exception{
		
		HttpSession session = request.getSession();
		String parametroMensaje = "";
		LogSISPE.getLog().info(":::ACCION: {}", accion);
		if(accion.equals("COT")){
			parametroMensaje = "la Cotizaci\u00F3n";
		}else if(accion.equals("COPE")){//verificar accion de consolidaccion para no permitir verificar el alcance de los articulos
			parametroMensaje = "la Consolidaci\u00F3n";
		}else{
			if(request.getParameter("cambiarContexto")!=null){
				parametroMensaje = "la Cotizaci\u00F3n";
			}else{
				parametroMensaje = "la Reservaci\u00F3n";
			}
		}
		
		if(session.getAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR)==null){
			int indiceDetalle =0;
			boolean detalleModificado = false;
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			
			Double porcentajePrecioAfiliado = (Double)session.getAttribute(PORCENTAJE_CALCULO_PRECIOS_AFILIADO);
			
			//se obtiene la colecci\u00F3n de c\u00F3digos de clasificaciones perecibles
			Collection<String> codigosClasificacionesEspeciales = (Collection <String>)session.getAttribute(CotizacionReservacionUtil.CODIGOS_CLASIFICACIONES_ESPECIALES);
			//colecci\u00F3n que almacenar\u00E1 los indices de los detalles cuyas cantidades fueron aminoradas
			Collection<String> indiceCantidadesModificadas = new ArrayList<String>();
			
			//se obtiene la clave que indica una modificaci\u00F3n de precios en el detalle principal
			String inputsActivadosCambioPrecio = (String)session.getAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS);
			String estadoDeBajaSIC = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja");
			
			//se obtiene el c\u00F3digo de los canastos 
			boolean calculosPreciosAfiliado = true;
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO) == null){
				calculosPreciosAfiliado = false;
			}
			
			boolean existenProblemasAlcance = false;
			boolean existenProblemasStock = false;
			boolean existenProblemasStockPavos = false;
			boolean existenProblemasArticulosObsoletos = false;
			boolean existenArticulosDadosDeBaja = false;
			boolean existenArticulosDadosDeBajaReceta = false;
			boolean existenCanastosVacios = false;
			boolean existenReservadosEnBodega = false;
			String filasArticulosDeBaja = "";
			String filasCanastosVacios = "";
			//variable para almacenar los c\u00F3digos de los art\u00EDculos obsoletos
			StringBuffer articulosObsoletosSS = null;
			
			boolean existenCanastos = false;
			boolean existenPavos = false;
			
			String tipoArticuloPavo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
			
			int contDetallesTotalCero = 0;
			int contadorArticulosEspeciales = 0;
//			String tipoCanasto = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//			String tipoDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");

			if(detallePedido!=null && !detallePedido.isEmpty()){
				
				//se crean las variables para realizar el c\u00E1lculo de las fechas m\u00E1ximas y m\u00EDnimas en las entregas
				Timestamp primeraFechaEntrega= null;
				Timestamp ultimaFechaEntrega= null;
				Timestamp primeraFechaDespacho=null;
				long diferenciaFechasEntregas=0;
				long diferenciaFechasDespacho=0;
				//OBTENER EL PARAMETRO PARA MOSTRAR LA AUTORIZACION DE GERENTE COMERCIAL EN PAVOS, POLLOS Y CANASTOS
				
				//ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.clasificacionesAutorizacionGerenteComercial.pavosPollosCanastos", request);
				
				//se obtiene el c\u00F3digo que identifica el grupo especial EXISTENCIA OBLIGATORIA
				Collection<String> clasificacionesAutorizacionGerenteComercial = new ArrayList<String>();
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialExistenciaObligatoria", request);
				if(parametroDTO.getValorParametro()!=null){
					//se obtiene los c\u00F3digos de clasificaciones
					EspecialClasificacionDTO consultaEspecialClasificacionDTO = new EspecialClasificacionDTO();
					consultaEspecialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					consultaEspecialClasificacionDTO.getId().setCodigoEspecial(parametroDTO.getValorParametro());
					consultaEspecialClasificacionDTO.setEstadoEspecialClasificacion(estadoActivo);
					Collection<EspecialClasificacionDTO> colEspecialClasificacionDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerEspecialClasificacion(consultaEspecialClasificacionDTO);
					consultaEspecialClasificacionDTO = null;
					
					//se agregan las clasificaciones a la coleccion
					if(CollectionUtils.isNotEmpty(colEspecialClasificacionDTO)){
						for(EspecialClasificacionDTO clasificacionEspActual : colEspecialClasificacionDTO){
							clasificacionesAutorizacionGerenteComercial.add(clasificacionEspActual.getId().getCodigoClasificacion());
						}
					}
				}				
				
				for(Iterator<DetallePedidoDTO> it = detallePedido.iterator();it.hasNext();){
					
					DetallePedidoDTO detallePedidoDTO = it.next();
					EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
					
					//control para los ar\u00EDculos que son receta
//					if(detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(tipoCanasto) || detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(tipoDespensa)){
					if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
							detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						existenCanastos = true;
					}
					
					//control para los art\u00EDculos que son pavos
					if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
						existenPavos = true;
					}
					
					//control para los detalles con valorTotal CERO
					if(estadoDetallePedidoDTO.getValorTotalEstado().doubleValue() == 0
							|| estadoDetallePedidoDTO.getValorTotalEstadoIVA().doubleValue() == 0){
						contDetallesTotalCero ++;
					}
					
					//se inicializa el estado de espcial
					estadoDetallePedidoDTO.setEspecialReservado(estadoInactivo);
					//control de art\u00EDculos especiales
					if(codigosClasificacionesEspeciales != null){
						//control para determinar si existen art\u00EDculos especiales en el detalle del pedido
						if(codigosClasificacionesEspeciales.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
							//especial = perecible
							detallePedidoDTO.getArticuloDTO().setNpEstadoEspecial(estadoActivo);
							estadoDetallePedidoDTO.setEspecialReservado(estadoActivo);
						}
					}

					try{
						//control de cambios en el campo cantidad
						long cantidad = Long.parseLong(this.vectorCantidad[indiceDetalle]);
						LogSISPE.getLog().info("cantidad ingresada: {}",cantidad);
						LogSISPE.getLog().info("cantidad estado: {}",estadoDetallePedidoDTO.getCantidadEstado());
						if(cantidad > 0 && estadoDetallePedidoDTO.getCantidadEstado().longValue() != cantidad){
							if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol()) && cantidad < estadoDetallePedidoDTO.getCantidadEstado().longValue()){
								LogSISPE.getLog().info("SE A\u00F1ADIO EL INDICE: {}",indiceDetalle+" POR CANTIDAD MENOR");
								indiceCantidadesModificadas.add(String.valueOf(indiceDetalle));
							}
							if(estadoDetallePedidoDTO.getCantidadEstado() != null){
								//Campo utilizado para saber si hubo un cambio de cantidades.
								estadoDetallePedidoDTO.setNpCantidadAnterior(estadoDetallePedidoDTO.getCantidadEstado());
							}
							estadoDetallePedidoDTO.setCantidadEstado(new Long(this.vectorCantidad[indiceDetalle]));
							
							detalleModificado=true;
							LogSISPE.getLog().info("CANTIDAD MODIFICADA EN LA FILA: {}",indiceDetalle);
						}

						//control de cambios en el campo peso
						Double pesoActual = Util.roundDoubleMath(Double.valueOf(this.vectorPeso[indiceDetalle]),NUMERO_DECIMALES);
						Double pesoEstado = Util.roundDoubleMath(estadoDetallePedidoDTO.getPesoArticuloEstado(),NUMERO_DECIMALES);
						LogSISPE.getLog().info("peso actual: {}",pesoActual);
						LogSISPE.getLog().info("peso estado: {}",pesoEstado);
						if(pesoActual.doubleValue() > 0 && (pesoEstado.doubleValue() != pesoActual.doubleValue())){
							estadoDetallePedidoDTO.setPesoArticuloEstado(pesoActual);
				    	//si el art\u00EDculo es de peso variable (NO PAVO)
							//esta condici\u00F3n es para el control de las entregas, ya que al modificarse el peso
							//se modifica autom\u00E1ticamente la cantidad
							if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
								if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
									LogSISPE.getLog().info("es un art\u00EDculo de PesoVariable (NO PAVO)");
									LogSISPE.getLog().info("peso ingresado: {}",pesoActual);
									Double cantidadGenerada = Double.valueOf(Math.ceil(pesoActual));
									if(cantidadGenerada.longValue() < estadoDetallePedidoDTO.getCantidadEstado().longValue()){
										LogSISPE.getLog().info("SE A\u00F1ADIO EL INDICE: {}",indiceDetalle+" POR CANTIDAD MENOR");
										indiceCantidadesModificadas.add(String.valueOf(indiceDetalle));
									}
								}
							}
							detalleModificado=true;
							LogSISPE.getLog().info("PESO MODIFICADO EN LA FILA: {}",indiceDetalle);
						}

						//Control de asignaciones de los cambios en los precios
						//se verifica si se hicieron cambios sin antes actualizar el detalle del pedido
						if(this.controlCambiosPrecio(detallePedidoDTO, indiceDetalle, inputsActivadosCambioPrecio, 
								calculosPreciosAfiliado, porcentajePrecioAfiliado, request) == true){
							detalleModificado=true;
						}
						
						//se verifica si existen canastos de cotizaciones sin items
//						if((detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(tipoCanasto) || detallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(tipoDespensa)) &&
						if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
								detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))) &&
								detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!= null && 
								detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty()){
							existenCanastosVacios = true;
							filasCanastosVacios = filasCanastosVacios.concat(String.valueOf(indiceDetalle + 1)).concat(", ");
						}
						
						//bandera para verificar si un art\u00EDculo fu\u00E9 reservado en bodega
						boolean reservadoEnBodega = false;
						long cantidadReservarBodega = 0;
						
						if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
							//se iteran las entregas para realizar algunos c\u00E1lculos y verificaciones en las cantidades
							
							for(Iterator<EntregaDetallePedidoDTO> entregasPedido = detallePedidoDTO.getEntregaDetallePedidoCol().iterator(); entregasPedido.hasNext();){
								EntregaDetallePedidoDTO entregaPedido = entregasPedido.next();
								
								//se itera el detalleEntrega para obtener la cantidad despacho
									if(entregaPedido.getCantidadDespacho()>0){
										reservadoEnBodega = true;
										cantidadReservarBodega += entregaPedido.getCantidadDespacho();
									}
								
								//proceso para calcular la primera fecha de entrega
								if(primeraFechaEntrega==null){
									primeraFechaEntrega = entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente();
								}else{
									//se calcula la diferencia entre la fecha de entrega actual y el valor de primeraFechaEntrega
									diferenciaFechasEntregas = entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente().getTime() - primeraFechaEntrega.getTime();
									if(diferenciaFechasEntregas < 0)
										primeraFechaEntrega = entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente();
								}
								//proceso para calcular la \u00FAltima fecha de entrega
								if(ultimaFechaEntrega == null){
									ultimaFechaEntrega = entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente();
								}else{
									//se calcula la diferencia entre la ultimaFechaEntrega y el valor de la fecha de entrega actual
									diferenciaFechasEntregas = ultimaFechaEntrega.getTime() - entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente().getTime();
									if(diferenciaFechasEntregas < 0)
										ultimaFechaEntrega = entregaPedido.getEntregaPedidoDTO().getFechaEntregaCliente();
								}
								
								//proceso para calcular la primera fecha de despacho
								if(primeraFechaDespacho==null){
									primeraFechaDespacho = entregaPedido.getEntregaPedidoDTO().getFechaDespachoBodega();
								}else{
									//se calcula la diferencia entre la fecha de despacho actual y el valor de primeraFechaDespacho
									diferenciaFechasDespacho = (entregaPedido.getEntregaPedidoDTO().getFechaDespachoBodega().getTime() - primeraFechaDespacho.getTime());
									if(diferenciaFechasDespacho < 0)
										primeraFechaDespacho = entregaPedido.getEntregaPedidoDTO().getFechaDespachoBodega();
								}
								
								if(entregaPedido.getEntregaPedidoDTO().getCodigoContextoEntrega()==Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"))){
									session.setAttribute(CotizarReservarAction.EXISTEN_ENTREGAS_SICMER, "ok");
								}
							}
							
							
//							for(Iterator<EntregaDTO> entregas = detallePedidoDTO.getEntregas().iterator(); entregas.hasNext();){
//								EntregaDTO entregaDTO = entregas.next();
								
//								if(entregaDTO.getCantidadDespacho() > 0){
//									reservadoEnBodega = true;
//									cantidadReservarBodega += entregaDTO.getCantidadDespacho();
//								}
//								//proceso para calcular la primera fecha de entrega
//								if(primeraFechaEntrega==null){
//									primeraFechaEntrega = entregaDTO.getFechaEntregaCliente();
//								}else{
//									//se calcula la diferencia entre la fecha de entrega actual y el valor de primeraFechaEntrega
//									diferenciaFechasEntregas = (entregaDTO.getFechaEntregaCliente().getTime() - primeraFechaEntrega.getTime());
//									if(diferenciaFechasEntregas < 0)
//										primeraFechaEntrega = entregaDTO.getFechaEntregaCliente();
//								}
								//proceso para calcular la \u00FAltima fecha de entrega
//								if(ultimaFechaEntrega == null){
//									ultimaFechaEntrega = entregaDTO.getFechaEntregaCliente();
//								}else{
//									//se calcula la diferencia entre la ultimaFechaEntrega y el valor de la fecha de entrega actual
//									diferenciaFechasEntregas = (ultimaFechaEntrega.getTime() - entregaDTO.getFechaEntregaCliente().getTime());
//									if(diferenciaFechasEntregas < 0)
//										ultimaFechaEntrega = entregaDTO.getFechaEntregaCliente();
//								}
	
								//proceso para calcular la primera fecha de despacho
//								if(primeraFechaDespacho==null){
//									primeraFechaDespacho = entregaDTO.getFechaDespachoBodega();
//								}else{
//									//se calcula la diferencia entre la fecha de despacho actual y el valor de primeraFechaDespacho
//									diferenciaFechasDespacho = (entregaDTO.getFechaDespachoBodega().getTime() - primeraFechaDespacho.getTime());
//									if(diferenciaFechasDespacho < 0)
//										primeraFechaDespacho = entregaDTO.getFechaDespachoBodega();
//								}
//							}
						}
						
						//se verifica si el art\u00EDculo se reserv\u00F3 en bodega
						if(reservadoEnBodega){
							existenReservadosEnBodega = true;
							LogSISPE.getLog().info("se reserva en bodega el art\u00EDculo {} con {}" , detallePedidoDTO.getId().getCodigoArticulo(),cantidadReservarBodega);
							estadoDetallePedidoDTO.setReservarBodegaSIC(estadoActivo);
							estadoDetallePedidoDTO.setCantidadReservarSIC(cantidadReservarBodega);
							//double pesoReservado = cantidadReservarBodega * detallePedidoDTO.getArticuloDTO().getPesoAproximado();
							double pesoReservado = cantidadReservarBodega * detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta();
							estadoDetallePedidoDTO.setPesoArticuloEstadoReservado(Util.roundDoubleMath(pesoReservado, NUMERO_DECIMALES));
							
							//se verifica si es un art\u00EDculo perecible
							if(estadoDetallePedidoDTO.getEspecialReservado().equals(estadoActivo)){
								contadorArticulosEspeciales ++;
							}
						}else{
							estadoDetallePedidoDTO.setReservarBodegaSIC(estadoInactivo);
							estadoDetallePedidoDTO.setCantidadReservarSIC(0L);
							estadoDetallePedidoDTO.setPesoArticuloEstadoReservado(0D);
						}
						
						//JM Se verifica el stock de pavos y pollos/canastos
//						if((detallePedidoDTO.getArticuloDTO().getTipoArticuloCalculoPrecio().equals(tipoArticuloPavo) ||
//								detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"))) && detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() == null){
						if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()!=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
								&& clasificacionesAutorizacionGerenteComercial.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())
								&& detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() == null){
							//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
							LogSISPE.getLog().info("cantidad estado: {}",estadoDetallePedidoDTO.getCantidadEstado());
							LogSISPE.getLog().info("cantidad reserva sic: {}",estadoDetallePedidoDTO.getCantidadReservarSIC());
							LogSISPE.getLog().info("NpStockArticulo{}",detallePedidoDTO.getArticuloDTO().getNpStockArticulo());
							LogSISPE.getLog().info("Cantidad solicitada al CD {}",estadoDetallePedidoDTO.getCantidadReservarSIC());
							if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null && estadoDetallePedidoDTO.getCantidadReservarSIC()!=null){
								if(estadoDetallePedidoDTO.getCantidadReservarSIC() > detallePedidoDTO.getArticuloDTO().getNpStockArticulo() && estadoDetallePedidoDTO.getCantidadReservarSIC() > 0){
//									existenProblemasStockPavos = true;
									detallePedidoDTO.setNpSinStockPavPolCan(true);
								}else{
									detallePedidoDTO.setNpSinStockPavPolCan(false);
								}
							}
						}
						//cambios autorizacion stock dentro canastos especiales
//						//si es canasto especial se valida el stock de sus detalles
						verificarStockDeDetallesCanastosEspeciales(request, detallePedidoDTO, clasificacionesAutorizacionGerenteComercial);
						
						//control del status del art\u00EDculo respecto al SIC
						
						//npEstadoArticuloSIC: ESTADO DEL ARTICULO EN EL SIC
						//npEstadoArticuloSICReceta: ESTADO DEL ARTICULO EN EL SIC COMO CONSECUENCIA DE LOS ITEMS DE LA RECETA
						if((detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC()!=null && 
								detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC().equals(estadoDeBajaSIC)) ||
								(detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta()!=null && 
									detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta().equals(estadoDeBajaSIC))){
							
							if(detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC()!=null && 
									detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC().equals(estadoDeBajaSIC)){
								existenArticulosDadosDeBaja = true;
							}else if(detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta()!=null && 
									detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta().equals(estadoDeBajaSIC)){
								existenArticulosDadosDeBajaReceta = true;
								filasArticulosDeBaja = filasArticulosDeBaja.concat(String.valueOf(indiceDetalle + 1)).concat(", ");
							}
						}
						//claseArticulo: ARTICULO PRINCIPAL
						else if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!= null && detallePedidoDTO.getArticuloDTO().getClaseArticulo() != null && detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) &&
								detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado() > detallePedidoDTO.getArticuloDTO().getNpStockArticulo()){
							//valida si es que pide art\u00EDculos obsoletos al CD y el stock no es suficiente
							if(articulosObsoletosSS == null){
								articulosObsoletosSS = new StringBuffer();
							}
							//a\u00F1ade el codigo del art\u00EDculo para el mensaje de error
							articulosObsoletosSS.append(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()).append(", ");
							existenProblemasArticulosObsoletos = true;
							
							LogSISPE.getLog().info("existen problemas de OBSOLETOS");
						}
						//npAlcance: ARTICULO PRINCIPAL
						//npAlcanceReceta: ARTICULO DE LA RECETA
						else if(detallePedidoDTO.getArticuloDTO().getNpAlcance()!=null && 
								detallePedidoDTO.getArticuloDTO().getNpAlcance().equals(estadoInactivo) ||
								(detallePedidoDTO.getArticuloDTO().getNpAlcanceReceta()!=null && 
										detallePedidoDTO.getArticuloDTO().getNpAlcanceReceta().equals(estadoInactivo))){
							if(!accion.equals("COPE")){ //no indicar que existe problemas de alcance cuando estamos en la pantalla de pedidos consolidados
								existenProblemasAlcance = true;
							}
							
							//No validamos alcances cuando es una autorizacion de tipo bodega o disminucion de fecha minima
							String respuestaSitemaAutorizaciones=((String)request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")));
							Long tipoAutorizacion=AutorizacionesUtil.obtenerCodigoTipoAutorizacion(respuestaSitemaAutorizaciones);
							
							if(tipoAutorizacion!=null && (tipoAutorizacion.intValue()==ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.intValue() || 
									tipoAutorizacion.intValue()==ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL.intValue())){
								existenProblemasAlcance = false;
							}
						}
						//npEstadoStockArticulo: ARTICULO PRINCIPAL
						//npEstadoStockArticuloReceta: ARTICULO DE LA RECETA
						else if (detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!= null && 
								((detallePedidoDTO.getArticuloDTO().getNpEstadoStockArticulo() != null && 
								detallePedidoDTO.getArticuloDTO().getNpEstadoStockArticulo().equals(estadoInactivo)) ||
								(detallePedidoDTO.getArticuloDTO().getNpEstadoStockArticuloReceta() != null && 
										detallePedidoDTO.getArticuloDTO().getNpEstadoStockArticuloReceta().equals(estadoInactivo)))){
							//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
							if(estadoDetallePedidoDTO.getCantidadReservarSIC() > 0
									&& estadoDetallePedidoDTO.getCantidadReservarSIC() > detallePedidoDTO.getArticuloDTO().getNpStockArticulo()
									&& (!detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(tipoArticuloPavo) &&
									(!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"))&& 
											!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas")))
									&& !clasificacionesAutorizacionGerenteComercial.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()))){
								existenProblemasStock = true;
								
								//si es una reserva para bodega se valida el stock 0
//								if(reservadoEnBodega){
//									detallePedidoDTO.setNpSinStockPavPolCan(Boolean.TRUE);
//								}

							}
						}
						detallePedidoDTO.setEstadoDetallePedidoDTO(estadoDetallePedidoDTO);
						
					}catch(NumberFormatException ex){
						//solo se captura la excepci\u00F3n
						LogSISPE.getLog().info("NumberFormatException");
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					}catch(ArrayIndexOutOfBoundsException ex){
						//solo se captura la excepcion
						LogSISPE.getLog().info("ArrayIndexOutOfBoundsException");
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					}
					indiceDetalle++;
				}

				//se guardan los valores en el formulario
				this.fechaMinimaEntrega = primeraFechaEntrega;
				this.fechaMaximaEntrega = ultimaFechaEntrega;
				this.fechaMinimaDespacho = primeraFechaDespacho;
				
				LogSISPE.getLog().info("primeraFechaEntrega: {}",this.fechaMinimaEntrega);
				LogSISPE.getLog().info("primeraFechaDespacho: {}",this.fechaMinimaDespacho);
				
				//si la colecci\u00F3n de indices de cantidades modificadas tiene elementos
				if(!indiceCantidadesModificadas.isEmpty())
					//variable de sesi\u00F3n que contiene los indices del detalle cuya cantidad fu\u00E9 modificada
					//con un valor menor al anterior
					session.setAttribute(CotizarReservarAction.COL_INDICES_CANTIDADES_MODIFICADAS, indiceCantidadesModificadas);

				//se verifica si hay canastos en el detalle del pedido
				if(existenCanastos){
					session.setAttribute(EXISTEN_RECETAS, "ok");
					//solo si todav\u00EDa no se ha consultado el par\u00E1metro
					if(session.getAttribute("ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo")==null){
						//se realiza la consulta que obtiene el par\u00E1metro que indica 
						//cual es el codigo de la clasificaci\u00F3n para canastos de cat\u00E1logo
						//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
						ParametroDTO consultaParametroDTO = new ParametroDTO();
						consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo"));
						Collection parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
						consultaParametroDTO = (ParametroDTO)(parametros.toArray())[0];
						session.setAttribute("ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo", consultaParametroDTO.getValorParametro());
					}
				}

				//se verifica si existen pavos en el detalle
				if(existenPavos){
					session.setAttribute(CotizarReservarAction.EXISTEN_PAVOS, "ok");
				}else{
					session.removeAttribute(CotizarReservarAction.EXISTEN_PAVOS);
				}
				
				LogSISPE.getLog().info("articulos especiales: {}",contadorArticulosEspeciales);
				
				//si existen art\u00EDculos perecibles se crea una variable para indicarlo
				if(contadorArticulosEspeciales > 0)
					session.setAttribute(CotizarReservarAction.EXISTEN_ESPECIALES_RESERVADOS, "ok");
				else
					session.removeAttribute(CotizarReservarAction.EXISTEN_ESPECIALES_RESERVADOS);
				
				LogSISPE.getLog().info("existen problemas de alcance: {}",existenProblemasAlcance);
				LogSISPE.getLog().info("existen problemas de stock: {}",existenProblemasStock);
				LogSISPE.getLog().info("existen problemas de obsoletos: {}",existenProblemasArticulosObsoletos);
				LogSISPE.getLog().info("existen problemas de stock pavos : {}",existenProblemasStockPavos);
		
				//solo si el detalle fue modificado
				if(detalleModificado){
					//primero se limpian los posibles errores generados anteriormente (este es un caso especial)
					errors.clear();
					session.setAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR,"ok");
					errors.add("detalleModificado",new ActionMessage("errors.detalle.sinActualizar",parametroMensaje));
					
					//wc
					session.setAttribute(CotizarReservarAction.DEBE_ACTUALIZAR, "ok");
					
					//se realizan las asignaciones correspondientes para observar la secci\u00F3n del detalle principal
					session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");
				}else if(contDetallesTotalCero > 0){
					errors.add("detallesEnCero",new ActionMessage("errors.detallePedido.valorTotalCero"));
				}else if(existenCanastosVacios){
					Iterator<ActionMessage> iterator = errors.get();				
					Boolean encontroClave = Boolean.FALSE;
					while(iterator.hasNext()){
						ActionMessage messageActual = iterator.next();
						if(messageActual.getKey().equals("errors.canastosVacios"))
							encontroClave = Boolean.TRUE;									
					}										
					if(!encontroClave)					
						errors.add("canastosVacios",new ActionMessage("errors.canastosVacios", filasCanastosVacios));
				}else if(existenProblemasStockPavos || existenProblemasStock || existenProblemasAlcance || existenArticulosDadosDeBaja || existenArticulosDadosDeBajaReceta || existenProblemasArticulosObsoletos){
					LogSISPE.getLog().info("validarPedido: {}",this.opValidarPedido);
					//JM Validacion Problemas de stock pavos y pollos
					if(existenProblemasStockPavos){
						if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.sinStock.pavpolcan"))!=null){
							WebSISPEUtil.desactivarProblemasStock(request, detallePedido);
						}
						CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.stockAlcance.cotizacion.pavos", "problemasStockAlcancePavos", null, errors);
//						errors.add("problemasStockAlcancePavos",new ActionMessage("errors.stockAlcance.cotizacion.pavos"));
					}
					//quiere decir que fue autorizado correctamente
					if(session.getAttribute(AutorizacionesUtil.AUTORIZACION_STOCK_PAVPOLCAN) != null ){
						WebSISPEUtil.desactivarProblemasStock(request, detallePedido);
					}
					//si es una cotizaci\u00F3n
					if(accion.equals("COT") && (this.opValidarPedido==null || !this.opValidarPedido.equals(estadoActivo))){
						//verificaci\u00F3n del estado
						if(existenArticulosDadosDeBaja || existenArticulosDadosDeBajaReceta){
							if(existenArticulosDadosDeBaja){
								errors.add("articulosDeBaja",new ActionMessage("errors.articulosDeBaja"));
							}
							if(existenArticulosDadosDeBajaReceta){
								errors.add("articulosDeBajaReceta",new ActionMessage("errors.articulosDeBajaReceta", filasArticulosDeBaja));
							}
						}//valida objetos obsoletos
						else if(existenProblemasArticulosObsoletos && MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.validarObsoletos").equals(estadoActivo) && 
								articulosObsoletosSS != null){
							errors.add("articulosObsoletos",new ActionMessage("errors.stock.articulo.obsoleto.reserva", articulosObsoletosSS.toString()));
							LogSISPE.getLog().info("error:articulosObsoletos: COT");
						}else if(existenProblemasAlcance || existenProblemasStock){
							errors.add("problemasStockAlcance",new ActionMessage("errors.stockAlcance.cotizacion"));
						}
					}else if(accion.equals("COT") && MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.validarObsoletos").equals(estadoActivo) && 
							articulosObsoletosSS != null){
						CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.stock.articulo.obsoleto.reserva","articulosObsoletos", articulosObsoletosSS.toString(), errors);
						LogSISPE.getLog().info("error:articulosObsoletos: COT2");
					}
					else if(accion.equals("RES") && request.getAttribute(RESERVACION_TEMPORAL) == null){
						//verificaci\u00F3n del estado
						if(existenArticulosDadosDeBaja || existenArticulosDadosDeBajaReceta){
							if(existenArticulosDadosDeBaja){
								errors.add("articulosDeBaja",new ActionMessage("errors.articulosDeBaja"));
							}
							if(existenArticulosDadosDeBajaReceta){
								errors.add("articulosDeBajaReceta",new ActionMessage("errors.articulosDeBajaReceta", filasArticulosDeBaja));
							}
						}
						//valida objetos obsoletos
						else if(existenProblemasArticulosObsoletos && MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.validarObsoletos").equals(estadoActivo) && 
								articulosObsoletosSS != null){
							errors.add("articulosObsoletos",new ActionMessage("errors.stock.articulo.obsoleto.reserva", articulosObsoletosSS.toString()));
							LogSISPE.getLog().info("error:articulosObsoletos: RES");
						}
						//esta condici\u00F3n realiza la validaci\u00F3n del alcance
						else if(existenProblemasAlcance && MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.validarAlcance").equals(estadoActivo)){
							CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.alcanceConAutorizacion.reservacion", "problemasAlcance", null, errors);
						}
						//validaci\u00F3n del stock
						else if(existenProblemasStock && MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.validarStock").equals(estadoActivo)){
							//se reliza la consulta de las clasificaciones que no permiten una reservaci\u00F3n sin existencia
//							boolean hayProblemasDeExistenciaObligatoria = false;
//							String codigoClasificacion = "";
							//se obtiene el c\u00F3digo que identifica el grupo especial EXISTENCIA OBLIGATORIA
//							parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialExistenciaObligatoria", request);
//							if(parametroDTO.getValorParametro()!=null){
//								//se obtiene los c\u00F3digos de clasificaciones
//								EspecialClasificacionDTO consultaEspecialClasificacionDTO = new EspecialClasificacionDTO();
//								consultaEspecialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//								consultaEspecialClasificacionDTO.getId().setCodigoEspecial(parametroDTO.getValorParametro());
//								consultaEspecialClasificacionDTO.setEstadoEspecialClasificacion(estadoActivo);
//								Collection<EspecialClasificacionDTO> colEspecialClasificacionDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerEspecialClasificacion(consultaEspecialClasificacionDTO);
//								consultaEspecialClasificacionDTO = null;
//								
//								if(colEspecialClasificacionDTO != null){
//									for(DetallePedidoDTO detallePedidoDTO : (Collection<DetallePedidoDTO>) detallePedido){
//										if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0
//												&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > detallePedidoDTO.getArticuloDTO().getNpStockArticulo()
//												&& (!detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(tipoArticuloPavo) &&
//												(!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"))&& 
//														!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"))))){
//										//se verifica si alguna clasificaci\u00F3n del detalle est\u00E1 en el listado de clasificaciones especiales y el art\u00EDculo no tiene stock
//											if(!ColeccionesUtil.simpleSearch("id.codigoClasificacion", detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), colEspecialClasificacionDTO).isEmpty()
//													&& detallePedidoDTO.getArticuloDTO().getNpEstadoStockArticulo().equals(estadoInactivo)){
//												hayProblemasDeExistenciaObligatoria = true;
//												codigoClasificacion = detallePedidoDTO.getArticuloDTO().getCodigoClasificacion();
//												break;
//											}
//										}
//									}
//								}
//							}

//							if(hayProblemasDeExistenciaObligatoria){
//								CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.articulosExistenciaObligatoria", "problemasStockAlcance", codigoClasificacion, errors);
////								errors.add("problemasStockAlcance",new ActionMessage("errors.articulosExistenciaObligatoria", codigoClasificacion));
//							}
							//bgudino
							//se comentan estas lineas para que se procecen los articulos por el nuevo sistema de autorizaciones
//							else if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"))==null){
//							
//							else
								if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_ABONO_CERO)){
								//no se ha generado una autorizaci\u00F3n
								CotizacionReservacionUtil.agregarActionMessageNoRepetido("errors.stockAlcance.reservacion", "problemasStockAlcance", "errors.stockAlcance.reservacion", errors);
							}
						}
					}
					//JM Validacion Problemas de stock pavos y pollos
					/*else if(existenProblemasStockPavos){
						if(AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion1(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.sinStock.pavpolcan"))!=null){
							WebSISPEUtil.desactivarProblemasStock(request, detallePedido);
						}
						//errors.add("problemasStockAlcancePavos",new ActionMessage("errors.stockAlcance.cotizacion.pavos"));
					}*/
				}
				//valida autorizaci\u00F3n de modificaci\u00F3n de reserva
				LogSISPE.getLog().info(":::ExistenReservadosBodega: {}", existenReservadosEnBodega);
				LogSISPE.getLog().info(":::ACCION_ACTUAL: {}", (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
				if(errors.isEmpty() && existenReservadosEnBodega && ((String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL)).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
					LogSISPE.getLog().info("ModificacionReserva: obtiene visa pedido");
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
					if(vistaPedidoDTO != null && 
							(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
							|| vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))) &&
							session.getAttribute(CotizarReservarAction.AUTORIZACION_MODIFICA_RESERVA_SOLICITUD_CD) == null){
						LogSISPE.getLog().info("ModificacionReserva: CAMBIOS_SOLICITUD_CD_MOD_RES");
						session.setAttribute(CotizarReservarAction.CAMBIOS_SOLICITUD_CD_MOD_RES, true);
					}
				}
			}
		}else{
			//se limpian los errores detectados anteriormente (este es un caso especial)
			errors.clear();
			errors.add("detalleModificado",new ActionMessage("errors.detalle.sinActualizar", parametroMensaje));
			//se realizan las asignaciones correspondientes para observar la secci\u00F3n del detalle principal
			session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");
		}
	}

	/**
	 * 
	 * @param detallePedidoDTO
	 * @param precioUnitarioEstado
	 * @param precioUnitarioIVAEstado
	 * @param indiceVector
	 * @param estadoModificacionPrecios
	 * @param calculosPreciosAfiliado
	 * @param porcentajePrecioAfiliado
	 * @param request
	 * @return
	 */
	private boolean controlCambiosPrecio(DetallePedidoDTO detallePedidoDTO, int indiceVector,	String estadoModificacionPrecios, boolean calculosPreciosAfiliado,
			Double porcentajePrecioAfiliado, HttpServletRequest request) throws Exception{
		
		//se obtiene el detalle del pedido
		EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
		
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		
		boolean detalleModificado = false;
		//solo si existe la posibilidad de cambio de precios
		if(estadoModificacionPrecios != null){
			Double precioVector = null;
			Double precioUnitarioEstadoRound = null;
			//control de cambios en el campo del valor unitario
			if(calculosPreciosAfiliado){
				//precios de afiliado
				precioVector = Util.roundDoubleMath(Double.valueOf(this.vectorPrecio[indiceVector]),NUMERO_DECIMALES);
				precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioIVAEstado(), NUMERO_DECIMALES);
				if(!SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
					precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioEstado(), NUMERO_DECIMALES);
			}else{
				//precios de no afiliado
				precioVector = Util.roundDoubleMath(Double.valueOf(this.vectorPrecioNoAfi[indiceVector]),NUMERO_DECIMALES);
				precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioIVANoAfiliado(), NUMERO_DECIMALES);
				if(!SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
					precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioNoAfiliado(), NUMERO_DECIMALES);
			}

			Double precioVectorAfiliado = null;
			Double precioVectorNoAfiliado = null;
			
			LogSISPE.getLog().info("precio vector: {}, precio actual: {}" , precioVector,precioUnitarioEstadoRound);
			//se comparan los precios
			if(precioVector.doubleValue() > 0 && precioUnitarioEstadoRound.doubleValue()!= precioVector.doubleValue()){
				
				//se verifica si los c\u00E1lculos son con precio de afiliado
				if(calculosPreciosAfiliado){
					precioVectorAfiliado = precioVector;
					precioVectorNoAfiliado = UtilesSISPE.aumentarPorcentajeAPrecio(precioVector, porcentajePrecioAfiliado);
					this.vectorPrecioNoAfi[indiceVector] = precioVectorNoAfiliado.toString();
				}else{
					precioVectorNoAfiliado = precioVector;
					precioVectorAfiliado = UtilesSISPE.disminuirPorcentajeAPrecio(precioVector, porcentajePrecioAfiliado);
					this.vectorPrecio[indiceVector] = precioVectorAfiliado.toString();
				}
				
				if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
					LogSISPE.getLog().info("valorUnitario: {}, valorUnitarioNoAf: {}",precioVectorAfiliado ,precioVectorNoAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioIVAEstado(precioVectorAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(precioVectorNoAfiliado);
					//se verifica si el art\u00EDculo aplica IVA
					if(estadoDetallePedidoDTO.getAplicaIVA().equals(estadoActivo)){
						//se recalcula el precio sin IVA (AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioSinIVA = precioVectorAfiliado.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioEstado(Util.roundDoubleMath(valorUnitarioSinIVA,NUMERO_DECIMALES));
						
						//se recalcula el precio sin IVA (NO AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioNoAfiSinIVA = precioVectorNoAfiliado.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(Util.roundDoubleMath(Double.valueOf(valorUnitarioNoAfiSinIVA),NUMERO_DECIMALES));
					}else{
						estadoDetallePedidoDTO.setValorUnitarioEstado(precioVectorAfiliado);
						estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(precioVectorNoAfiliado);
					}
				}else{
					estadoDetallePedidoDTO.setValorUnitarioEstado(precioVectorAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(precioVectorNoAfiliado);
					
					//se verifica si el art\u00EDculo aplica IVA
					if(estadoDetallePedidoDTO.getAplicaIVA().equals(estadoActivo)){
						//se recalcula el precio con IVA (AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioConIVA = precioVectorAfiliado.doubleValue() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioIVAEstado(Util.roundDoubleMath(valorUnitarioConIVA,NUMERO_DECIMALES));
						
						//se recalcula el precio con IVA (NO AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioNoAfiConIVA = precioVectorNoAfiliado.doubleValue() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(Util.roundDoubleMath(Double.valueOf(valorUnitarioNoAfiConIVA),NUMERO_DECIMALES));
					}else{
						estadoDetallePedidoDTO.setValorUnitarioIVAEstado(precioVectorAfiliado);
						estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(precioVectorNoAfiliado);
					}
				}
				
				detalleModificado=true;
				//solo si los precios unitarios fueron modificados se recalcula el precio de caja y el rrecio de mayorista
				UtilesSISPE.recalcularPrecioCaja(detallePedidoDTO);
				UtilesSISPE.recalcularPrecioMayorista(detallePedidoDTO, WebSISPEUtil.obtenerPorcentajeDiferenciaPrecioNormalYMayorista(request));
				LogSISPE.getLog().info("VALOR UNITARIO MODIFICADO EN LA FILA: {}",indiceVector);
			}
		}
		
		return detalleModificado;
	}
	
	/**
	 * obtiene el nombre del dia dado una fecha
	 * @param fechaEntrega
	 */
	
//	private String obtenerDia(String fechaEntrega){
//		String days [] = {"Domingo","Lunes","Martes","Mi\u00E9rcoles","Jueves","Viernes","S\u00E1bado"};
//		Date fechaEnviada =  ConverterUtil.parseStringToDate(fechaEntrega); 
//		Calendar cal=Calendar.getInstance();
//		cal.setTime(fechaEnviada);
//		return days[cal.get(Calendar.DAY_OF_WEEK)-1]; 
//	}
	
	/**
	 * Valida si en el detallePedido existe un articulo cuya clasificacion sea(13-licores)
	 * @param formulario
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean existeArticulosClasificacionSeaLicores(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, String codCanastosCatalogo, String codCanastosEspeciales) throws Exception{
		String codigoClasificionPadre;
		String codigoClasificacionLicores="";
		boolean existeDetalle = false;
		
		//se obtiene el par\u00E1metro de los departamentos que prohibe la ley entregar los dias domingos
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentos.prohibidoEntregarClienteDomingos", request);
		if(parametroDTO.getValorParametro()!=null){
			codigoClasificacionLicores = parametroDTO.getValorParametro();
		}
		codigoClasificionPadre = detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().substring(0, 2);
		//valida si existe un articulo cuya clasificacion sea(13-licores)
		if(!codigoClasificacionLicores.equals("") && codigoClasificacionLicores.contains(codigoClasificionPadre)){
			existeDetalle = true;
		}
//		//valida que en los canastos de cat\u00E1logo o especiales sus items sean de la clasificacion (13-licores)
//		else if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
//				|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
//			if(itemsCanastaClasificacion(request, detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol(), codigoClasificacionLicores)){
//				existeDetalle = true;
//			}
//		}
		return(existeDetalle);
	}
	
//	//Funcion que verifica si los items de la receta pertenecen a la clasificacionPadre(13 licores)
//	private boolean itemsCanastaClasificacion(HttpServletRequest request,Collection<ArticuloRelacionDTO> colItemsCanasta, String codigoClasificacionLicores)throws Exception {
//		LogSISPE.getLog().info("Metodo que verifica items de un canasto");
//		String codigoClasificionPadre;
//		
//		boolean existeItemsDetalle = false;
//		
//		for(ArticuloRelacionDTO recetaDTO : colItemsCanasta){
//			//codigoClasificionPadre = recetaDTO.getArticulo().getCodigoClasificacion().substring(0, 2);
//			codigoClasificionPadre = recetaDTO.getArticuloRelacion().getCodigoClasificacion().substring(0, 2);
//			//valida si existe un articulo cuya clasificacion sea(13-licores)
//			if(!codigoClasificacionLicores.equals("") && codigoClasificacionLicores.contains(codigoClasificionPadre)){
//				existeItemsDetalle = true;
//				break;
//			}
//		}
//		return existeItemsDetalle;
//	}
	
	/**
	 * Verifica si se modificaron los valores del detalle antes de guardar la canasta.
	 * @param session						- La sesi\u00F3n actual
	 * @param detalleCanasta		- La colecci\u00F3n del detalle de la canasta.
	 * @param errors						- La colecci\u00F3n de errores manejados en el formulario. 
	 * @throws Exception.
	 */
	private boolean verificarCantidadesDetalleCanasta(HttpServletRequest request, List<ArticuloRelacionDTO> detalleCanasta, ActionErrors errors) throws Exception{
		
		HttpSession session = request.getSession();
		//se obtiene el estado activo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		//se obtiene la clave que indica una modificaci\u00F3n de precios en el detalle principal
		String estadoModificacionPrecios = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.receta.actualizar.precio");
		//Variable que indica si encontro al menos un implemento en el canasto
		boolean hayUnImplemento = false;
		//si se cambio el detalle sin actualizar antes
		if(session.getAttribute(DetalleCanastaAction.DETALLE_SIN_ACTUALIZAR)==null)
		{
			boolean detalleModificado = false;
	    if(detalleCanasta!=null && !detalleCanasta.isEmpty()){
	    	//se itera el detalle del canasto
				for(int i=0;i<detalleCanasta.size();i++){
					ArticuloRelacionDTO recetaArticuloDTO = (ArticuloRelacionDTO)detalleCanasta.get(i);
					try
					{
						//Pregunta si el articulo es un implemento
						if(recetaArticuloDTO.getArticuloRelacion().getNpImplemento().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.canasto.comImplenento")))
							hayUnImplemento = true;
						//se obtiene la cantidad ingresada para el item del canasto
						long cantidad = Long.parseLong(this.vectorcantidadCanasta[i]);
						if(cantidad > 0 && recetaArticuloDTO.getCantidad().longValue() != cantidad){
							recetaArticuloDTO.setCantidad(new Integer(this.vectorcantidadCanasta[i]));
							detalleModificado=true;
						}

						//solo si existe la posibilidad de cambio de precios en la receta
//						if(estadoModificacionPrecios.equals(estadoActivo)){
//							//se obtiene el precio modificado del item del canasto
//							Double precioItemVector = Util.roundDoubleMath(Double.valueOf(this.vectorPrecioItem[i]),NUMERO_DECIMALES);
//							//Double precioItemReceta = recetaArticuloDTO.getPrecioUnitario();
//							Double precioItemReceta = recetaArticuloDTO.getArticulo().getPrecioBase();
//							if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
//								//precioItemReceta = recetaArticuloDTO.getPrecioUnitarioIVA();
//								precioItemReceta = recetaArticuloDTO.getArticulo().getPrecioBase();
							//se comparan los precios
//							if(precioItemVector.doubleValue() > 0 
//									&& (precioItemReceta.doubleValue() != precioItemVector.doubleValue())){
//								if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
//									recetaArticuloDTO.setPrecioUnitarioIVA(precioItemVector);
//									//se verifica si el art\u00EDculo aplica IVA
//									if(recetaArticuloDTO.getArticulo().getAplicaImpuesto().equals(estadoActivo)){
//										//se recalcula el precio sin IVA y se lo asigna al estado del detalle
//										double precioUnitarioSinIVA = precioItemVector.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//										recetaArticuloDTO.setPrecioUnitario(Util.roundDoubleMath(Double.valueOf(precioUnitarioSinIVA),NUMERO_DECIMALES));
//									}else
//										recetaArticuloDTO.setPrecioUnitario(precioItemVector);
//								}else{
//									recetaArticuloDTO.setPrecioUnitario(precioItemVector);
//									//se verifica si el art\u00EDculo aplica IVA
//									if(recetaArticuloDTO.getArticulo().getAplicaImpuesto().equals(estadoActivo)){
//										//se recalcula el precio con IVA y se lo asigna al estado del detalle
//										double precioUnitarioConIVA = precioItemVector.doubleValue() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//										recetaArticuloDTO.setPrecioUnitarioIVA(Util.roundDoubleMath(Double.valueOf(precioUnitarioConIVA),NUMERO_DECIMALES));										
//									}else
//										recetaArticuloDTO.setPrecioUnitarioIVA(precioItemVector);
//								}
//								detalleModificado=true;
//							}
//						}
					}catch(NumberFormatException ex){
						//solo se captura la excepci\u00F3n
						LogSISPE.getLog().info("NumberFormatException");
					}catch(ArrayIndexOutOfBoundsException ex){
						//solo se captura la excepcion
						LogSISPE.getLog().info("ArrayIndexOutOfBoundsException");
					}
				}
				//solo si el detalle fue modificado
				if(detalleModificado){
					session.setAttribute(DetalleCanastaAction.DETALLE_SIN_ACTUALIZAR,"ok");
					errors.add("detalleModificado",new ActionMessage("errors.detalle.sinActualizar","la Canasta"));
				}
			}
		}else{

			errors.add("detalleModificado",new ActionMessage("errors.detalle.sinActualizar","la Canasta"));
		}
		return hayUnImplemento;
	}
	
	/**
	 * M\u00E9todo para actualizar la lista de detalles de acuerdo a la colecci\u00F3n de art\u00EDculos obsoletos
	 * @param detallePedidoObsoletos Colecci\u00F3n con la lista de detalles de articulos obsoletos
	 * @param request
	 * @param errors
	 */
	public void actualizarDetalleObsoletos(Collection<DetallePedidoDTO> detallePedidoObsoletos, HttpServletRequest request, ActionErrors errors){			
		HttpSession session = request.getSession();
		List<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		List<DetallePedidoDTO> detallesNuevos =  new ArrayList<DetallePedidoDTO>();
		ArrayList<DetallePedidoDTO> detallePedidoTmp = (ArrayList<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedido);
		try{
			if(CollectionUtils.isNotEmpty(detallePedidoObsoletos)  && this.vectorObsoleto!=null)
			{
				LogSISPE.getLog().info("TAMANO DETALLE OBSOLETOS: {}",detallePedidoObsoletos.size());
				int indiceDetalle = 0;
				//se itera la colecci\u00F3n de los detalles obsoletos
				for (Iterator<DetallePedidoDTO> it = detallePedidoObsoletos.iterator(); it.hasNext();)
				{						
					DetallePedidoDTO detallePedidoDTO = it.next();
					detallePedidoDTO.getEstadoDetallePedidoDTO().setStoLocArtObs(Integer.parseInt(this.vectorObsoleto[indiceDetalle]));
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado() > detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs()){
						if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs()>0){
							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
							detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
							detallePedidoDTO.getArticuloDTO().setNpStockArticulo(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());
						}						
					}				
					
					detallesNuevos.add(detallePedidoDTO);	
					indiceDetalle++;
				}
				
				//se ordena la coleccion 
				indiceDetalle = 0;
				for (Iterator<DetallePedidoDTO> it = detallePedido.iterator(); it.hasNext();)
				{		
					DetallePedidoDTO detallePedidoDTO = it.next();
					for (DetallePedidoDTO detalleNuevo : detallesNuevos)
					{		
						if(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detalleNuevo.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras())//if(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(detalleNuevo.getArticuloDTO().getId().getCodigoArticulo()) &&
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().equals(detalleNuevo.getEstadoDetallePedidoDTO().getCantidadEstado())
								&& detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().equals(detalleNuevo.getEstadoDetallePedidoDTO().getStoLocArtObs()) ){							
							this.vectorCantidad[indiceDetalle]=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().toString();
							detallePedidoTmp.remove(indiceDetalle);
							detallePedidoTmp.add(indiceDetalle, detalleNuevo);
						}	
					}	
					indiceDetalle++;
				}				
			}
			//se actualiza la lista de detalles
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoTmp);	
			
		}catch(NumberFormatException e){
			LogSISPE.getLog().info("NumberFormatException");
		}		
	}
	
	/**
	 * 
	 * @param errors
	 * @param codMontoMinEntregaDomicioCD
	 * @param totalEntregaSeleccionada
	 */
	private void validarEntregaTotalParcial(ActionErrors errors, Integer codMinCanastosDomicilio, Double montoMinimoEntrega, Double totEntTotSel, 
			Double totEntParSel, Boolean validarCantidadCanastos,HttpServletRequest request){
		
		Double totalEntregaTotalSeleccionada = Util.roundDoubleMath(totEntTotSel, NUMERO_DECIMALES);
		Double totalEntregaParcialSeleccionada = Util.roundDoubleMath(totEntParSel, NUMERO_DECIMALES);
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		
		//entregas totales
		if(this.opTipoEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"))
				&& totalEntregaTotalSeleccionada >= 0 && totalEntregaTotalSeleccionada < montoMinimoEntrega && !validarCantidadCanastos){
			
			LogSISPE.getLog().info("No cumple la condicion que para las entregas total que se pide al CD monto > {} ", montoMinimoEntrega);
			errors.add("minEntrega",new ActionMessage("error.validacion.configuracion.entregas.cantidadMinima.domicilio", codMinCanastosDomicilio.toString(), montoMinimoEntrega.toString(),totalEntregaTotalSeleccionada.toString()));
			
		}
		//entregas parciales
		else if(beanSession.getPaginaTabPopUp().getTabSeleccionado() != 0){
				if (this.opTipoEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))
					&& totalEntregaParcialSeleccionada >0 && totalEntregaParcialSeleccionada < montoMinimoEntrega && !validarCantidadCanastos) {
				
				LogSISPE.getLog().info("No cumple la condicion que para las entregas parcial que se pide al CD monto > {} ", montoMinimoEntrega);
				errors.add("minEntrega",new ActionMessage("error.validacion.configuracion.entregas.cantidadMinima.domicilio",  codMinCanastosDomicilio.toString(), montoMinimoEntrega.toString(),totalEntregaParcialSeleccionada.toString()));
				
			}else if (this.opTipoEntrega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))
					&& totalEntregaParcialSeleccionada==0 && totalEntregaParcialSeleccionada < montoMinimoEntrega && !validarCantidadCanastos) {
				
				LogSISPE.getLog().info("No cumple la condicion que para las entregas parcial que se pide al CD monto > {} ", montoMinimoEntrega);
				errors.add("minEntrega",new ActionMessage("error.validacion.configuracion.entregas.cantidadMinima.domicilio",  codMinCanastosDomicilio.toString(), montoMinimoEntrega.toString(),totalEntregaParcialSeleccionada.toString()));
				
			}
		}
	}
	
	/**
	 * 
	 * @param datos
	 * @param formulario
	 * @param inicio
	 * @return
	 */
	public Collection paginarDatos(Collection datos, int inicio, int size,boolean paginacionBase) throws Exception{
		LogSISPE.getLog().info("PAGINACION");
        int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        int start = inicio;
        
        this.start = String.valueOf(start);
        this.range = String.valueOf(range);
        this.size = String.valueOf(size);
        
        LogSISPE.getLog().info("start: {}" , this.start);
        LogSISPE.getLog().info("range: {}" , this.range);
        LogSISPE.getLog().info("size: {}" , this.size);
        if(paginacionBase)
        	return datos;
        
        return Util.obtenerSubCollection(datos, start, start + range > size ? size : start + range);
	}
	
	/**
	 * @return Devuelve opTipoDocumento.
	 */
	public String getOpTipoDocumento()
	{
		return opTipoDocumento;
	}
	/**
	 * @param opTipoDocumento El opTipoDocumento a establecer.
	 */
	public void setOpTipoDocumento(String opTipoDocumento)
	{
		this.opTipoDocumento = opTipoDocumento;
	}

	/**
	 * @return Devuelve opTipoEspeciales.
	 */
	public String getOpTipoEspeciales()
	{
		return opTipoEspeciales;
	}
	/**
	 * @param opTipoEspeciales El opTipoEspeciales a establecer.
	 */
	public void setOpTipoEspeciales(String opTipoEspeciales)
	{
		this.opTipoEspeciales = opTipoEspeciales;
	}

	/**
	 * @return Devuelve opEspecialesSeleccionados.
	 */
	public String[] getOpEspecialesSeleccionados()
	{
		return opEspecialesSeleccionados;
	}
	/**
	 * @param opEspecialesSeleccionados El opEspecialesSeleccionados a establecer.
	 */
	public void setOpEspecialesSeleccionados(String[] opEspecialesSeleccionados)
	{
		this.opEspecialesSeleccionados = opEspecialesSeleccionados;
	}

	/**
	 * @return Devuelve vectorCantidadEspecial.
	 */
	public String[] getVectorCantidadEspecial()
	{
		return vectorCantidadEspecial;
	}
	/**
	 * @param vectorCantidadEspecial El vectorCantidadEspecial a establecer.
	 */
	public void setVectorCantidadEspecial(String[] vectorCantidadEspecial)
	{
		this.vectorCantidadEspecial = vectorCantidadEspecial;
	}
	/**
	 * @return Devuelve opTipoBusqueda.
	 */
	public String getOpTipoBusqueda()
	{
		return opTipoBusqueda;
	}
	/**
	 * @param opTipoBusqueda El opTipoBusqueda a establecer.
	 */
	public void setOpTipoBusqueda(String opTipoBusqueda)
	{
		this.opTipoBusqueda = opTipoBusqueda;
	}
	/**
	 * @return Devuelve textoBusqueda.
	 */
	public String getTextoBusqueda()
	{
		return textoBusqueda;
	}
	/**
	 * @param textoBusqueda El textoBusqueda a establecer.
	 */
	public void setTextoBusqueda(String textoBusqueda)
	{
		this.textoBusqueda = textoBusqueda;
	}

	/**
	 * @return Devuelve nombreContacto.
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}
	/**
	 * @param nombreContacto El nombreContacto a establecer.
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	/**
	 * @return Devuelve telefonoContacto.
	 */
	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	/**
	 * @param telefonoContacto El telefonoContacto a establecer.
	 */
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}
	/**
	 * @return Devuelve rucEmpresa.
	 */
	public String getRucEmpresa()
	{
		return rucEmpresa;
	}
	/**
	 * @param rucEmpresa El rucEmpresa a establecer.
	 */
	public void setRucEmpresa(String rucEmpresa)
	{
		this.rucEmpresa = rucEmpresa;
	}
	/**
	 * @return Devuelve nombreEmpresa.
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	/**
	 * @param nombreEmpresa El nombreEmpresa a establecer.
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	/**
	 * @return Devuelve emailContacto.
	 */
	public String getEmailContacto()
	{
		return emailContacto;
	}
	/**
	 * @param emailContacto El emailContacto a establecer.
	 */
	public void setEmailContacto(String emailContacto)
	{
		this.emailContacto = emailContacto;
	}

	/**
	 * @return Devuelve localResponsable.
	 */
	public String getLocalResponsable()
	{
		return localResponsable;
	}
	/**
	 * @param localResponsable El localResponsable a establecer.
	 */
	public void setLocalResponsable(String localResponsable)
	{
		this.localResponsable = localResponsable;
	}

	/**
	 * @return Devuelve opValidarPedido.
	 */
	public String getOpValidarPedido()
	{
		return opValidarPedido;
	}
	/**
	 * @param opValidarPedido El opValidarPedido a establecer.
	 */
	public void setOpValidarPedido(String opValidarPedido)
	{
		this.opValidarPedido = opValidarPedido;
	}

	/**
	 * @return el checkReservarTodoBodega
	 */
	public String getCheckReservarTodoBodega() {
		return checkReservarTodoBodega;
	}

	/**
	 * @param checkReservarTodoBodega el checkReservarTodoBodega a establecer
	 */
	public void setCheckReservarTodoBodega(String checkReservarTodoBodega) {
		this.checkReservarTodoBodega = checkReservarTodoBodega;
	}

	/**
	 * @return el checksReservarBodega
	 */
	public String[] getChecksReservarBodega() {
		return checksReservarBodega;
	}

	/**
	 * @param checksReservarBodega el checksReservarBodega a establecer
	 */
	public void setChecksReservarBodega(String[] checksReservarBodega) {
		this.checksReservarBodega = checksReservarBodega;
	}

	/**
	 * @return el vectorCantidadReservarBodega
	 */
	public String[] getVectorCantidadReservarBodega() {
		return vectorCantidadReservarBodega;
	}

	/**
	 * @param vectorCantidadReservarBodega el vectorCantidadReservarBodega a establecer
	 */
	public void setVectorCantidadReservarBodega(
			String[] vectorCantidadReservarBodega) {
		this.vectorCantidadReservarBodega = vectorCantidadReservarBodega;
	}

	/**
	 * @return Devuelve cantidadArticulo.
	 */
	public String getCantidadArticulo()
	{
		return cantidadArticulo;
	}
	/**
	 * @param cantidadArticulo El cantidadArticulo a establecer.
	 */
	public void setCantidadArticulo(String cantidadArticulo)
	{
		this.cantidadArticulo = cantidadArticulo;
	}
	/**
	 * @return Devuelve codigoArticulo.
	 */
	public String getCodigoArticulo()
	{
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo El codigoArticulo a establecer.
	 */
	public void setCodigoArticulo(String codigoArticulo)
	{
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Devuelve vectorCantidad.
	 */
	public String[] getVectorCantidad()
	{
		return vectorCantidad;
	}
	/**
	 * @param vectorCantidad El vectorCantidad a establecer.
	 */
	public void setVectorCantidad(String[] vectorCantidad)
	{
		this.vectorCantidad = vectorCantidad;
	}
	/**
	 * @return Devuelve vectorPrecio.
	 */
	public String[] getVectorPrecio()
	{
		return vectorPrecio;
	}
	/**
	 * @param vectorPrecio El vectorPrecio a establecer.
	 */
	public void setVectorPrecio(String[] vectorPrecio)
	{
		this.vectorPrecio = vectorPrecio;
	}

	/**
	 * @return el vectorPeso
	 */
	public String[] getVectorPeso() {
		return vectorPeso;
	}
	
	
	/**
	 * @param vectorPeso el vectorPeso a establecer
	 */
	public void setVectorPeso(String[] vectorPeso) {
		this.vectorPeso = vectorPeso;
	}
	
	/**
	 * @return el vectorPesoActual
	 */
	public String[] getVectorPesoActual() {
		return vectorPesoActual;
	}


	/**
	 * @param vectorPesoActual el vectorPesoActual a establecer
	 */
	public void setVectorPesoActual(String[] vectorPesoActual) {
		this.vectorPesoActual = vectorPesoActual;
	}


	/**
	 * @return Devuelve checkActualizarStockAlcance.
	 */
	public String getCheckActualizarStockAlcance()
	{
		return checkActualizarStockAlcance;
	}
	/**
	 * @param checkActualizarStockAlcance El checkActualizarStockAlcance a establecer.
	 */
	public void setCheckActualizarStockAlcance(String checkActualizarStockAlcance)
	{
		this.checkActualizarStockAlcance = checkActualizarStockAlcance;
	}
	/**
	 * @return Devuelve checkSeleccionarTodo.
	 */
	public String getCheckSeleccionarTodo()
	{
		return checkSeleccionarTodo;
	}
	/**
	 * @param checkSeleccionarTodo El checkSeleccionarTodo a establecer.
	 */
	public void setCheckSeleccionarTodo(String checkSeleccionarTodo)
	{
		this.checkSeleccionarTodo = checkSeleccionarTodo;
	}
	
	/**
	 * @return el checkSubirArchivo
	 */
	public String getCheckSubirArchivo() {
		return checkSubirArchivo;
	}
	/**
	 * @param checkSubirArchivo el checkSubirArchivo a establecer
	 */
	public void setCheckSubirArchivo(String checkSubirArchivo) {
		this.checkSubirArchivo = checkSubirArchivo;
	}
	
	/**
	 * @return el archivo
	 */
	public FormFile getArchivo() {
		
		return archivo;
	}


	/**
	 * @param archivo el archivo a establecer
	 */
	public void setArchivo(FormFile archivo) {
		
		this.archivo = archivo;
	}


	/**
	 * @return el files
	 */
	public Collection<FormFile> getFiles() {
		return files;
	}


	/**
	 * @param files el files a establecer
	 */
	public void setFiles(Collection<FormFile> files) {
		this.files = files;
	}


	/**
	 * @return Devuelve checksSeleccionar.
	 */
	public String[] getChecksSeleccionar()
	{
		return checksSeleccionar;
	}
	/**
	 * @param checksSeleccionar El checksSeleccionar a establecer.
	 */
	public void setChecksSeleccionar(String[] checksSeleccionar)
	{
		this.checksSeleccionar = checksSeleccionar;
	}
	
	
	/**
	 * @return Devuelve subTotal.
	 */
	public Double getSubTotal() {
		return subTotal;
	}
	/**
	 * @param subTotal El subTotal a establecer.
	 */
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	/**
	 * @return Devuelve subTotalNoAplicaIVA.
	 */
	public Double getSubTotalNoAplicaIVA()
	{
		return subTotalNoAplicaIVA;
	}
	/**
	 * @param subTotalNoAplicaIVA El subTotalNoAplicaIVA a establecer.
	 */
	public void setSubTotalNoAplicaIVA(Double subTotalNoAplicaIVA)
	{
		this.subTotalNoAplicaIVA = subTotalNoAplicaIVA;
	}
	/**
	 * @return Devuelve subTotalAplicaIVA.
	 */
	public Double getSubTotalAplicaIVA()
	{
		return subTotalAplicaIVA;
	}
	/**
	 * @param subTotalAplicaIVA El subTotalAplicaIVA a establecer.
	 */
	public void setSubTotalAplicaIVA(Double subTotalAplicaIVA)
	{
		this.subTotalAplicaIVA = subTotalAplicaIVA;
	}
	/**
	 * @return Devuelve descuentoTotal.
	 */
	public Double getDescuentoTotal()
	{
		return descuentoTotal;
	}
	/**
	 * @param descuentoTotal El descuentoTotal a establecer.
	 */
	public void setDescuentoTotal(Double descuentoTotal)
	{
		this.descuentoTotal = descuentoTotal;
	}
	/**
	 * @return Devuelve ivaTotal.
	 */
	public Double getIvaTotal() {
		return ivaTotal;
	}
	/**
	 * @param ivaTotal El ivaTotal a establecer.
	 */
	public void setIvaTotal(Double ivaTotal) {
		this.ivaTotal = ivaTotal;
	}
	/**
	 * @return Devuelve total.
	 */
	public Double getTotal() {
		return total;
	}
	/**
	 * @param total El total a establecer.
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

	/**
	 * @return el costoFlete
	 */
	public Double getCostoFlete() {
		return costoFlete;
	}
	/**
	 * @param costoFlete el costoFlete a establecer
	 */
	public void setCostoFlete(Double costoFlete) {
		this.costoFlete = costoFlete;
	}

	/**
	 * @return el costoTotalPedido
	 */
	public Double getCostoTotalPedido() {
		return costoTotalPedido;
	}
	/**
	 * @param costoTotalPedido el costoTotalPedido a establecer
	 */
	public void setCostoTotalPedido(Double costoTotalPedido) {
		this.costoTotalPedido = costoTotalPedido;
	}
	/**
	 * @return Devuelve valorAbono.
	 */
	public String getValorAbono()
	{
		return valorAbono;
	}
	/**
	 * @param valorAbono El valorAbono a establecer.
	 */
	public void setValorAbono(String valorAbono)
	{
		this.valorAbono = valorAbono;
	}
	/**
	 * @return Devuelve opDescuentos.
	 */
	public String[] getOpDescuentos()
	{
		return opDescuentos;
	}
	/**
	 * @param opDescuentos El opDescuentos a establecer.
	 */
	public void setOpDescuentos(String[] opDescuentos)
	{
		this.opDescuentos = opDescuentos;
	}

	/**
	 * @return Devuelve opSinDescuentos.
	 */
	public String getOpSinDescuentos()
	{
		return opSinDescuentos;
	}
	/**
	 * @param opSinDescuentos El opSinDescuentos a establecer.
	 */
	public void setOpSinDescuentos(String opSinDescuentos)
	{
		this.opSinDescuentos = opSinDescuentos;
	}
	/**
	 * @return Devuelve fechaEnt.
	 */
	public String getFechaEntrega()
	{
		return fechaEntrega;
	}
	/**
	 * @param fechaEnt El fechaEnt a establecer.
	 */
	public void setFechaEntrega(String fechaEntrega)
	{
		this.fechaEntrega = fechaEntrega;
	}

	/**
	 * @return Devuelve vectorcantidadCanasta.
	 */
	public String[] getVectorcantidadCanasta()
	{
		return vectorcantidadCanasta;
	}
	/**
	 * @param vectorcantidadCanasta El vectorcantidadCanasta a establecer.
	 */
	public void setVectorcantidadCanasta(String[] vectorcantidadCanasta)
	{
		this.vectorcantidadCanasta = vectorcantidadCanasta;
	}

	/**
	 * @return Devuelve vectorPrecioItem.
	 */
	public String[] getVectorPrecioItem()
	{
		return vectorPrecioItem;
	}
	/**
	 * @param vectorPrecioItem El vectorPrecioItem a establecer.
	 */
	public void setVectorPrecioItem(String[] vectorPrecioItem)
	{
		this.vectorPrecioItem = vectorPrecioItem;
	}
	/**
	 * @return Devuelve totalCanasta.
	 */
	public Double getTotalCanasta()
	{
		return totalCanasta;
	}
	/**
	 * @param totalCanasta El totalCanasta a establecer.
	 */
	public void setTotalCanasta(Double totalCanasta)
	{
		this.totalCanasta = totalCanasta;
	}
	/**
	 * @return Devuelve botonRegistrarCanasta.
	 */
	public String getBotonRegistrarCanasta()
	{
		return botonRegistrarCanasta;
	}
	/**
	 * @param botonRegistrarCanasta El botonRegistrarCanasta a establecer.
	 */
	public void setBotonRegistrarCanasta(String botonRegistrarCanasta)
	{
		this.botonRegistrarCanasta = botonRegistrarCanasta;
	}
	/**
	 * @return Devuelve botonSalirCanasta.
	 */
	public String getBotonSalirCanasta()
	{
		return botonSalirCanasta;
	}
	/**
	 * @param botonSalirCanasta El botonSalirCanasta a establecer.
	 */
	public void setBotonSalirCanasta(String botonSalirCanasta)
	{
		this.botonSalirCanasta = botonSalirCanasta;
	}

	/**
	 * @return Devuelve opAutorizacion.
	 */
	public String getOpAutorizacion()
	{
		return opAutorizacion;
	}
	/**
	 * @param opAutorizacion El opAutorizacion a establecer.
	 */
	public void setOpAutorizacion(String opAutorizacion)
	{
		this.opAutorizacion = opAutorizacion;
	}

	/**
	 * @return Devuelve numeroAutorizacion.
	 */
	public String getNumeroAutorizacion()
	{
		return numeroAutorizacion;
	}
	/**
	 * @param numeroAutorizacion El numeroAutorizacion a establecer.
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion)
	{
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return Devuelve loginAutorizacion.
	 */
	public String getLoginAutorizacion()
	{
		return loginAutorizacion;
	}
	/**
	 * @param loginAutorizacion El loginAutorizacion a establecer.
	 */
	public void setLoginAutorizacion(String loginAutorizacion)
	{
		this.loginAutorizacion = loginAutorizacion;
	}
	/**
	 * @return Devuelve passwordAutorizacion.
	 */
	public String getPasswordAutorizacion()
	{
		return passwordAutorizacion;
	}
	/**
	 * @param passwordAutorizacion El passwordAutorizacion a establecer.
	 */
	public void setPasswordAutorizacion(String passwordAutorizacion)
	{
		this.passwordAutorizacion = passwordAutorizacion;
	}
	/**
	 * @return Devuelve observacionAutorizacion.
	 */
	public String getObservacionAutorizacion()
	{
		return observacionAutorizacion;
	}
	/**
	 * @param observacionAutorizacion El observacionAutorizacion a establecer.
	 */
	public void setObservacionAutorizacion(String observacionAutorizacion)
	{
		this.observacionAutorizacion = observacionAutorizacion;
	}
	/**
	 * @return Devuelve botonRegistrarCotizacion.
	 */
	public String getBotonRegistrarCotizacion()
	{
		return botonRegistrarCotizacion;
	}
	/**
	 * @param botonRegistrarCotizacion El botonRegistrarCotizacion a establecer.
	 */
	public void setBotonRegistrarCotizacion(String botonRegistrarCotizacion)
	{
		this.botonRegistrarCotizacion = botonRegistrarCotizacion;
	}
	/**
	 * @return Devuelve botonRegistrarReservacion.
	 */
	public String getBotonRegistrarReservacion()
	{
		return botonRegistrarReservacion;
	}
	/**
	 * @param botonRegistrarReservacion El botonRegistrarReservacion a establecer.
	 */
	public void setBotonRegistrarReservacion(String botonRegistrarReservacion)
	{
		this.botonRegistrarReservacion = botonRegistrarReservacion;
	}

	/**
	 * @return Devuelve botonNo.
	 */
	public String getBotonNo()
	{
		return botonNo;
	}
	/**
	 * @param botonNo El botonNo a establecer.
	 */
	public void setBotonNo(String botonNo)
	{
		this.botonNo = botonNo;
	}
	/**
	 * @return Devuelve botonSi.
	 */
	public String getBotonSi()
	{
		return botonSi;
	}
	/**
	 * @param botonSi El botonSi a establecer.
	 */
	public void setBotonSi(String botonSi)
	{
		this.botonSi = botonSi;
	}

	/**
	 * @return el buscaFecha
	 */
	public String getBuscaFecha() {
		return this.buscaFecha;
	}

	/**
	 * @param buscaFecha el buscaFecha a establecer
	 */
	public void setBuscaFecha(String buscaFecha) {
		this.buscaFecha = buscaFecha;
	}

	/**
	 * @return el calendarioDiaLocal
	 */
	public Object[] getCalendarioDiaLocal() {
		return this.calendarioDiaLocal;
	}

	/**
	 * @param calendarioDiaLocal el calendarioDiaLocal a establecer
	 */
	public void setCalendarioDiaLocal(Object[] calendarioDiaLocal) {
		this.calendarioDiaLocal = calendarioDiaLocal;
	}

	/**
	 * @return el dias
	 */
	public Object[] getDias() {
		return this.dias;
	}

	/**
	 * @param dias el dias a establecer
	 */
	public void setDias(Object[] dias) {
		this.dias = dias;
	}

	/**
	 * @return el cantidadEntregas
	 */
	public String[] getCantidadEntregas() {
		return this.cantidadEntregas;
	}

	/**
	 * @param cantidadEntregas el cantidadEntregas a establecer
	 */
	public void setCantidadEntregas(String[] cantidadEntregas) {
		this.cantidadEntregas = cantidadEntregas;
	}

	/**
	 * @return el cantidadEstados
	 */
	public String[] getCantidadEstados() {
		return this.cantidadEstados;
	}

	/**
	 * @param cantidadEstados el cantidadEstados a establecer
	 */
	public void setCantidadEstados(String[] cantidadEstados) {
		this.cantidadEstados = cantidadEstados;
	}
	
	/**
	 * @return el registrarFecha
	 */
	public String getRegistrarFecha() {
		return this.registrarFecha;
	}
	/**
	 * @param registrarFecha el registrarFecha a establecer
	 */
	public void setRegistrarFecha(String registrarFecha) {
		this.registrarFecha = registrarFecha;
	}
	/**
	 * @return el fechaEntregaCliente
	 */
	public String getFechaEntregaCliente() {
		return this.fechaEntregaCliente;
	}
	/**
	 * @param fechaEntregaCliente el fechaEntregaCliente a establecer
	 */
	public void setFechaEntregaCliente(String fechaEntregaCliente) {
		this.fechaEntregaCliente = fechaEntregaCliente;
	}
	
	/**
	 * @return el direccion
	 */
	public String getDireccion() {
		return this.direccion;
	}
	/**
	 * @param direccion el direccion a establecer
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return el sectores
	 */
	public String getSectores() {
		return this.sectores;
	}
	/**
	 * @param sectores el sectores a establecer
	 */
	public void setSectores(String sectores) {
		this.sectores = sectores;
	}
	/**
	 * @return el opTipoEntrega
	 */
	public String getOpTipoEntrega() {
		return this.opTipoEntrega;
	}
	/**
	 * @param opTipoEntrega el opTipoEntrega a establecer
	 */
	public void setOpTipoEntrega(String opTipoEntrega) {
		this.opTipoEntrega = opTipoEntrega;
	}
	/**
	 * @return el fechaMinimaDespacho
	 */
	public Timestamp getFechaMinimaDespacho() {
		return this.fechaMinimaDespacho;
	}
	/**
	 * @param fechaMinimaDespacho el fechaMinimaDespacho a establecer
	 */
	public void setFechaMinimaDespacho(Timestamp fechaMinimaDespacho) {
		this.fechaMinimaDespacho = fechaMinimaDespacho;
	}
	/**
	 * @return el fechaMinimaEntrega
	 */
	public Timestamp getFechaMinimaEntrega() {
		return this.fechaMinimaEntrega;
	}
	/**
	 * @param fechaMinimaEntrega el fechaMinimaEntrega a establecer
	 */
	public void setFechaMinimaEntrega(Timestamp fechaMinimaEntrega) {
		this.fechaMinimaEntrega = fechaMinimaEntrega;
	}

	/**
	 * @return el fechaMaximaEntrega
	 */
	public Timestamp getFechaMaximaEntrega() {
		return fechaMaximaEntrega;
	}

	/**
	 * @param fechaMaximaEntrega el fechaMaximaEntrega a establecer
	 */
	public void setFechaMaximaEntrega(Timestamp fechaMaximaEntrega) {
		this.fechaMaximaEntrega = fechaMaximaEntrega;
	}

	/**
	 * @return el numeroBultosF
	 */
	public String getNumeroBultosF() {
		return this.numeroBultosF;
	}

	/**
	 * @param numeroBultosF el numeroBultosF a establecer
	 */
	public void setNumeroBultosF(String numeroBultosF) {
		this.numeroBultosF = numeroBultosF;
	}


	/**
	 * @return el direcciones
	 */
	public String[] getDirecciones() {
		return this.direcciones;
	}

	/**
	 * @param direcciones el direcciones a establecer
	 */
	public void setDirecciones(String[] direcciones) {
		this.direcciones = direcciones;
	}

	/**
	 * @return el autorizacion
	 */
	public String getAutorizacion() {
		return this.autorizacion;
	}

	/**
	 * @param autorizacion el autorizacion a establecer
	 */
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	/**
	 * @return el fechaDespacho
	 */
	public String getFechaDespacho() {
		return this.fechaDespacho;
	}

	/**
	 * @param fechaDespacho el fechaDespacho a establecer
	 */
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	/**
	 * @return el localOSector
	 */
	public String getLocalOSector() {
		return this.localOSector;
	}

	/**
	 * @param localOSector el localOSector a establecer
	 */
	public void setLocalOSector(String localOSector) {
		this.localOSector = localOSector;
	}

	/**
	 * @return el distancia
	 */
	public String getDistancia() {
		return this.distancia;
	}

	/**
	 * @param distancia el distancia a establecer
	 */
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

	/**
	 * @return el unidadTiempo
	 */
	public String getUnidadTiempo() {
		return this.unidadTiempo;
	}

	/**
	 * @param unidadTiempo el unidadTiempo a establecer
	 */
	public void setUnidadTiempo(String unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
	}

	/**
	 * @return el distanciaH
	 */
	public String getDistanciaH() {
		return this.distanciaH;
	}

	/**
	 * @param distanciaH el distanciaH a establecer
	 */
	public void setDistanciaH(String distanciaH) {
		this.distanciaH = distanciaH;
	}

	/**
	 * @return el distanciaM
	 */
	public String getDistanciaM() {
		return this.distanciaM;
	}

	/**
	 * @param distanciaM el distanciaM a establecer
	 */
	public void setDistanciaM(String distanciaM) {
		this.distanciaM = distanciaM;
	}	
	
	public String getHorasMinutos() {
		return horasMinutos;
	}

	public void setHorasMinutos(String horasMinutos) {
		this.horasMinutos = horasMinutos;
	}

	/**
	 * @return el horas
	 */
	public String getHoras() {
		return this.horas;
	}

	/**
	 * @param horas el horas a establecer
	 */
	public void setHoras(String horas) {
		this.horas = horas;
	}

	/**
	 * @return el minutos
	 */
	public String getMinutos() {
		return this.minutos;
	}

	/**
	 * @param minutos el minutos a establecer
	 */
	public void setMinutos(String minutos) {
		this.minutos = minutos;
	}

	/**
	 * @return el segundos
	 */
	public String getSegundos() {
		return this.segundos;
	}

	/**
	 * @param segundos el segundos a establecer
	 */
	public void setSegundos(String segundos) {
		this.segundos = segundos;
	}

	/**
	 * @return el idLocalOSector
	 */
	public String getIdLocalOSector() {
		return this.idLocalOSector;
	}

	/**
	 * @param idLocalOSector el idLocalOSector a establecer
	 */
	public void setIdLocalOSector(String idLocalOSector) {
		this.idLocalOSector = idLocalOSector;
	}

	/**
	 * @return el opLocalEntrega
	 */
	public String getOpLocalEntrega() {
		return this.opLocalEntrega;
	}

	/**
	 * @param opLocalEntrega el opLocalEntrega a establecer
	 */
	public void setOpLocalEntrega(String opLocalEntrega) {
		this.opLocalEntrega = opLocalEntrega;
	}

	/**
	 * @return el checkCalculosPreciosMejorados
	 */
	public String getCheckCalculosPreciosMejorados() {
		return checkCalculosPreciosMejorados;
	}

	/**
	 * @param checkCalculosPreciosMejorados el checkCalculosPreciosMejorados a establecer
	 */
	public void setCheckCalculosPreciosMejorados(
			String checkCalculosPreciosMejorados) {
		this.checkCalculosPreciosMejorados = checkCalculosPreciosMejorados;
	}

	/**
	 * @return el local
	 */
	public String getLocal() {
		return this.local;
	}

	/**
	 * @param local el local a establecer
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @return el indiceLocalResponsable
	 */
	public String getIndiceLocalResponsable() {
		return indiceLocalResponsable;
	}

	/**
	 * @param indiceLocalResponsable el indiceLocalResponsable a establecer
	 */
	public void setIndiceLocalResponsable(String indiceLocalResponsable) {
		this.indiceLocalResponsable = indiceLocalResponsable;
	}

	public String[] getCantidadPedidaBodega() {
		return cantidadPedidaBodega;
	}

	public void setCantidadPedidaBodega(String[] cantidadPedidaBodega) {
		this.cantidadPedidaBodega = cantidadPedidaBodega;
	}

//	public String getOpLocalResponsable() {
//		return opLocalResponsable;
//	}
//
//	public void setOpLocalResponsable(String opLocalResponsable) {
//		this.opLocalResponsable = opLocalResponsable;
//	}

	public String getOpLugarEntrega() {
		return opLugarEntrega;
	}

	public void setOpLugarEntrega(String opLugarEntrega) {
		this.opLugarEntrega = opLugarEntrega;
	}

	public String getOpStock() {
		return opStock;
	}

	public void setOpStock(String opStock) {
		this.opStock = opStock;
	}

	public String getLugarEntrega() {
		return lugarEntrega;
	}

	public void setLugarEntrega(String lugarEntrega) {
		this.lugarEntrega = lugarEntrega;
	}

	public String getResponsableEntrega() {
		return responsableEntrega;
	}

	public void setResponsableEntrega(String responsableEntrega) {
		this.responsableEntrega = responsableEntrega;
	}

	public String getStockEntrega() {
		return stockEntrega;
	}

	public void setStockEntrega(String stockEntrega) {
		this.stockEntrega = stockEntrega;
	}

	public String getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(String tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	public String getListaLocales() {
		return listaLocales;
	}

	public void setListaLocales(String listaLocales) {
		this.listaLocales = listaLocales;
	}

	public String[] getCantidadDespachos() {
		return cantidadDespachos;
	}

	public void setCantidadDespachos(String[] cantidadDespachos) {
		this.cantidadDespachos = cantidadDespachos;
	}

	public String[] getSeleccionEntregas() {
		return seleccionEntregas;
	}

	public void setSeleccionEntregas(String[] seleccionEntregas) {
		this.seleccionEntregas = seleccionEntregas;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	/**
	 * @return el checkCalculosPreciosAfiliado
	 */
	public String getCheckCalculosPreciosAfiliado() {
		return checkCalculosPreciosAfiliado;
	}

	/**
	 * @param checkCalculosPreciosAfiliado el checkCalculosPreciosAfiliado a establecer
	 */
	public void setCheckCalculosPreciosAfiliado(String checkCalculosPreciosAfiliado) {
		this.checkCalculosPreciosAfiliado = checkCalculosPreciosAfiliado;
	}

	/**
	 * @return el vectorPrecioNoAfi
	 */
	public String[] getVectorPrecioNoAfi() {
		return vectorPrecioNoAfi;
	}

	/**
	 * @param vectorPrecioNoAfi el vectorPrecioNoAfi a establecer
	 */
	public void setVectorPrecioNoAfi(String[] vectorPrecioNoAfi) {
		this.vectorPrecioNoAfi = vectorPrecioNoAfi;
	}

	public String getSeleccionCiudad() {
		return seleccionCiudad;
	}

	public void setSeleccionCiudad(String seleccionCiudad) {
		this.seleccionCiudad = seleccionCiudad;
	}

	/**
	 * @return el tipoAutorizacion
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	/**
	 * @param tipoAutorizacion el tipoAutorizacion a establecer
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}

	//OANDINO: M\u00E9todos GET y SET para verificaci\u00F3n de tr\u00E1nsito	
	/**
	 * @return par\u00E1metro de checkTransitoArray()
	 */
	public String[] getCheckTransitoArray() {
		return checkTransitoArray;
	}

	/**
	 * @param par\u00E1metro para checkTransitoArray()
	 */
	public void setCheckTransitoArray(String[] checkTransitoArray) {
		this.checkTransitoArray = checkTransitoArray;
	}


	/**
	 * @return el vectorCanAjuModPesos
	 */
	public String[] getVectorCanAjuModPesos() {
		return vectorCanAjuModPesos;
	}


	/**
	 * @param vectorCanAjuModPesos el vectorCanAjuModPesos a establecer
	 */
	public void setVectorCanAjuModPesos(String[] vectorCanAjuModPesos) {
		this.vectorCanAjuModPesos = vectorCanAjuModPesos;
	}

	/**
	 * @return el opEstadoActivo
	 */
	public String getOpEstadoActivo() {
		return opEstadoActivo;
	}

	/**
	 * @param opEstadoActivo el opEstadoActivo a establecer
	 */
	public void setOpEstadoActivo(String opEstadoActivo) {
		this.opEstadoActivo = opEstadoActivo;
	}


	public Double[][] getPorcentajeVarDescuento() {
		return porcentajeVarDescuento;
	}

	public void setPorcentajeVarDescuento(Double[][] porcentajeVarDescuento) {
		this.porcentajeVarDescuento = porcentajeVarDescuento;
	}

	public String getBotonConsolidarPedidos() {
		return botonConsolidarPedidos;
	}


	public void setBotonConsolidarPedidos(String botonConsolidarPedidos) {
		this.botonConsolidarPedidos = botonConsolidarPedidos;
	}


	public Collection getDatos() {
		return datos;
	}


	public void setDatos(Collection datos) {
		this.datos = datos;
	}


	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public String getRange() {
		return range;
	}


	public void setRange(String range) {
		this.range = range;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getCodigoLocal() {
		return codigoLocal;
	}


	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}


	public String getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public String getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public String getEstadoPedido() {
		return estadoPedido;
	}


	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}


	public String getEstadoPagoPedido() {
		return estadoPagoPedido;
	}


	public void setEstadoPagoPedido(String estadoPagoPedido) {
		this.estadoPagoPedido = estadoPagoPedido;
	}


	public String getAlerta() {
		return alerta;
	}


	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}


	public String getOpcionFecha() {
		return opcionFecha;
	}


	public void setOpcionFecha(String opcionFecha) {
		this.opcionFecha = opcionFecha;
	}


	public String getEtiquetaFechas() {
		return etiquetaFechas;
	}


	public void setEtiquetaFechas(String etiquetaFechas) {
		this.etiquetaFechas = etiquetaFechas;
	}


	public String getOpEntidadResponsable() {
		return opEntidadResponsable;
	}


	public void setOpEntidadResponsable(String opEntidadResponsable) {
		this.opEntidadResponsable = opEntidadResponsable;
	}


	public String getOpDespachoPendiente() {
		return opDespachoPendiente;
	}


	public void setOpDespachoPendiente(String opDespachoPendiente) {
		this.opDespachoPendiente = opDespachoPendiente;
	}


	public String getOpStockArticuloReservado() {
		return opStockArticuloReservado;
	}


	public void setOpStockArticuloReservado(String opStockArticuloReservado) {
		this.opStockArticuloReservado = opStockArticuloReservado;
	}


	public String getNumeroMeses() {
		return numeroMeses;
	}


	public void setNumeroMeses(String numeroMeses) {
		this.numeroMeses = numeroMeses;
	}


	public String getOpPedidoOrdenCompra() {
		return opPedidoOrdenCompra;
	}


	public void setOpPedidoOrdenCompra(String opPedidoOrdenCompra) {
		this.opPedidoOrdenCompra = opPedidoOrdenCompra;
	}


	public String getNumeroPedidoTxt() {
		return numeroPedidoTxt;
	}


	public void setNumeroPedidoTxt(String numeroPedidoTxt) {
		this.numeroPedidoTxt = numeroPedidoTxt;
	}


	public String getNumeroReservaTxt() {
		return numeroReservaTxt;
	}


	public void setNumeroReservaTxt(String numeroReservaTxt) {
		this.numeroReservaTxt = numeroReservaTxt;
	}


	public String getCodigoClasificacionTxt() {
		return codigoClasificacionTxt;
	}


	public void setCodigoClasificacionTxt(String codigoClasificacionTxt) {
		this.codigoClasificacionTxt = codigoClasificacionTxt;
	}


	public String getCodigoArticuloTxt() {
		return codigoArticuloTxt;
	}


	public void setCodigoArticuloTxt(String codigoArticuloTxt) {
		this.codigoArticuloTxt = codigoArticuloTxt;
	}


	public String getDescripcionArticuloTxt() {
		return descripcionArticuloTxt;
	}


	public void setDescripcionArticuloTxt(String descripcionArticuloTxt) {
		this.descripcionArticuloTxt = descripcionArticuloTxt;
	}


	public String getDocumentoPersonalTxt() {
		return documentoPersonalTxt;
	}


	public void setDocumentoPersonalTxt(String documentoPersonalTxt) {
		this.documentoPersonalTxt = documentoPersonalTxt;
	}


	public String getNombreContactoTxt() {
		return nombreContactoTxt;
	}


	public void setNombreContactoTxt(String nombreContactoTxt) {
		this.nombreContactoTxt = nombreContactoTxt;
	}


	public String getRucEmpresaTxt() {
		return rucEmpresaTxt;
	}


	public void setRucEmpresaTxt(String rucEmpresaTxt) {
		this.rucEmpresaTxt = rucEmpresaTxt;
	}


	public String getNombreEmpresaTxt() {
		return nombreEmpresaTxt;
	}


	public void setNombreEmpresaTxt(String nombreEmpresaTxt) {
		this.nombreEmpresaTxt = nombreEmpresaTxt;
	}


	public String getComboTipoPedido() {
		return comboTipoPedido;
	}


	public void setComboTipoPedido(String comboTipoPedido) {
		this.comboTipoPedido = comboTipoPedido;
	}


	public String getComboEstadoPagoPedido() {
		return comboEstadoPagoPedido;
	}


	public void setComboEstadoPagoPedido(String comboEstadoPagoPedido) {
		this.comboEstadoPagoPedido = comboEstadoPagoPedido;
	}


	public String getComboEnviadoCD() {
		return comboEnviadoCD;
	}


	public void setComboEnviadoCD(String comboEnviadoCD) {
		this.comboEnviadoCD = comboEnviadoCD;
	}


	public String getComboTipoFecha() {
		return comboTipoFecha;
	}


	public void setComboTipoFecha(String comboTipoFecha) {
		this.comboTipoFecha = comboTipoFecha;
	}


	public String getOpTipoAgrupacion() {
		return opTipoAgrupacion;
	}


	public void setOpTipoAgrupacion(String opTipoAgrupacion) {
		this.opTipoAgrupacion = opTipoAgrupacion;
	}


	public String[] getOpSeleccionPedidosConsolidar() {
		return opSeleccionPedidosConsolidar;
	}


	public void setOpSeleccionPedidosConsolidar(
			String[] opSeleccionPedidosConsolidar) {
		this.opSeleccionPedidosConsolidar = opSeleccionPedidosConsolidar;
	}


	public String getOpSeleccionTodos() {
		return opSeleccionTodos;
	}


	public void setOpSeleccionTodos(String opSeleccionTodos) {
		this.opSeleccionTodos = opSeleccionTodos;
	}


	public Collection getDatosConsolidados() {
		return datosConsolidados;
	}


	public void setDatosConsolidados(Collection datosConsolidados) {
		this.datosConsolidados = datosConsolidados;
	}


	public String[] getOpSeleccionPedidosConsolidados() {
		return opSeleccionPedidosConsolidados;
	}


	public void setOpSeleccionPedidosConsolidados(
			String[] opSeleccionPedidosConsolidados) {
		this.opSeleccionPedidosConsolidados = opSeleccionPedidosConsolidados;
	}


	public String getOpSeleccionTodosConsolidados() {
		return opSeleccionTodosConsolidados;
	}


	public void setOpSeleccionTodosConsolidados(String opSeleccionTodosConsolidados) {
		this.opSeleccionTodosConsolidados = opSeleccionTodosConsolidados;
	}


	public String getNumeroConsolidadoTxt() {
		return numeroConsolidadoTxt;
	}


	public void setNumeroConsolidadoTxt(String numeroConsolidadoTxt) {
		this.numeroConsolidadoTxt = numeroConsolidadoTxt;
	}


	public String[] getPedidosValidados() {
		return pedidosValidados;
	}


	public void setPedidosValidados(String[] pedidosValidados) {
		this.pedidosValidados = pedidosValidados;
	}

	public void setNumeroConsolidado(String[] numeroConsolidado) {
		this.numeroConsolidado = numeroConsolidado;
	}


	public String[] getNumeroConsolidado() {
		return numeroConsolidado;
	}


	public String getNumeroPedidoConsolidado() {
		return numeroPedidoConsolidado;
	}


	public void setNumeroPedidoConsolidado(String numeroPedidoConsolidado) {
		this.numeroPedidoConsolidado = numeroPedidoConsolidado;
	}


	public String getNumeroAConsolidar() {
		return numeroAConsolidar;
	}


	public void setNumeroAConsolidar(String numeroAConsolidar) {
		this.numeroAConsolidar = numeroAConsolidar;
	}


	public String[] getOpDescuentoSeleccionadoConsolidado() {
		return opDescuentoSeleccionadoConsolidado;
	}


	public void setOpDescuentoSeleccionadoConsolidado(
			String[] opDescuentoSeleccionadoConsolidado) {
		this.opDescuentoSeleccionadoConsolidado = opDescuentoSeleccionadoConsolidado;
	}
	
	


	public String getBotonGuardarConsolidarPedidos() {
		return botonGuardarConsolidarPedidos;
	}


	public void setBotonGuardarConsolidarPedidos(
			String botonGuardarConsolidarPedidos) {
		this.botonGuardarConsolidarPedidos = botonGuardarConsolidarPedidos;
	}


	public Collection getDatosConsolidadosTotal() {
		return datosConsolidadosTotal;
	}


	public void setDatosConsolidadosTotal(Collection datosConsolidadosTotal) {
		this.datosConsolidadosTotal = datosConsolidadosTotal;
	}


	public String[] getOpPedidoGeneralConsolidado() {
		return opPedidoGeneralConsolidado;
	}


	public void setOpPedidoGeneralConsolidado(String[] opPedidoGeneralConsolidado) {
		this.opPedidoGeneralConsolidado = opPedidoGeneralConsolidado;
	}


	public String getOpBuscarConsolidados() {
		return opBuscarConsolidados;
	}


	public void setOpBuscarConsolidados(String opBuscarConsolidados) {
		this.opBuscarConsolidados = opBuscarConsolidados;
	}


	/**
	 * @return el checkPagoEfectivo
	 */
	public String getCheckPagoEfectivo() {
		return checkPagoEfectivo;
	}


	/**
	 * @param checkPagoEfectivo el checkPagoEfectivo a establecer
	 */
	public void setCheckPagoEfectivo(String checkPagoEfectivo) {
		this.checkPagoEfectivo = checkPagoEfectivo;
	}
	/**
	 * Este m\u00E9todo permite validar el tipo de documento elegido
	 * @author Wladimir Lopez 
	 * @param validator
	 * @param errors
	 */
	private void validarTipoDocumento(PropertyValidator validator, ActionErrors errors, HttpServletRequest request) {
		LogSISPE.getLog().info("INGRESA A LA VALIDACION DEL TIPO DE DOCUMENTO");
		String  valorComboTipoDoc = getTipoDocumento(); //getComboTipoDocumento();
		validator.validateMandatory(errors,"numeroDocumento",this.numeroDocumento.trim(),"errors.requerido","N\u00FAmero Documento");
		if(StringUtils.isEmpty(tipoDocumento) || StringUtils.isEmpty(nombrePersona) && StringUtils.isEmpty(nombreEmpresa) ){
			errors.add("numeroDocumento",new ActionMessage("errors.cotizarreservar.buscarcliente"));
		}
		if (errors.isEmpty()){
			//se obtiene el bean que contiene los campos genericos
//			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			
//			if(getNumeroDocumento().matches("\\d+")==true){
////				if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
//				if(getNumeroDocumento().trim().length()==10){
//					if(!validator.validateCedula(this.numeroDocumento.trim())){
//						errors.add("cedulaContacto",new ActionMessage("errors.cedula.incorrecto"));
//						request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//						request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//						ContactoUtil.reiniciarBotonesSession(4, request);
//						ContactoUtil.limpiarVariablesFormulario(this);
//						//ContactoUtil.cambiarTabPersonas(beanSession,  MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//						beanSession.getPaginaTab().getTab(0).setTitulo(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//					}					
//				}
////				else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
//				else if(getNumeroDocumento().trim().length()==13){
//					if(!validator.validateRUC(this.numeroDocumento.trim())){
//						errors.add("cedulaContacto",new ActionMessage("errors.ruc.incorrecto"));
//						request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//						request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//						ContactoUtil.reiniciarBotonesSession(4, request);
//						ContactoUtil.limpiarVariablesFormulario(this);
//						//ContactoUtil.cambiarTabPersonas(beanSession,  MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.empresa"));
//						beanSession.getPaginaTab().getTab(0).setTitulo(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.empresa"));
//					}
//				}
//			}
			
			LogSISPE.getLog().info("VALIDACION DE BUSQUEDA DE PERSONA");
			//String  valorComboTipoDoc = getTipoDocumento();
			if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
				if(!validator.validateCedula(this.numeroDocumento.trim())){
					errors.add("cedulaContacto",new ActionMessage("errors.cedula.incorrecto"));
					request.getSession().removeAttribute(ContactoUtil.PERSONA);
					request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
					ContactoUtil.limpiarVariablesFormulario(this);
					//ContactoUtil.reiniciarBotonesSession(4, request);
				}					
			}
//			else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
//				if(!validator.validateCedula(this.numeroDocumento.trim())){
//					errors.add("cedulaContacto",new ActionMessage("errors.pasaporte.incorrecto"));
//					request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//					request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//					ContactoUtil.reiniciarBotonesSession(4, request);
//				}
//			}
			else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
				if(!validator.validateRUC(this.numeroDocumento.trim())){
					errors.add("cedulaContacto",new ActionMessage("errors.ruc.incorrecto"));
					request.getSession().removeAttribute(ContactoUtil.PERSONA);
					request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
					ContactoUtil.limpiarVariablesFormulario(this);
					//ContactoUtil.reiniciarBotonesSession(4, request);
				}
			}	
		}
		else{
			request.getSession().removeAttribute(ContactoUtil.PERSONA);
			request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
			//ContactoUtil.reiniciarBotonesSession(4, request);
			ContactoUtil.limpiarVariablesFormulario(this);
		}
		
	}

	/**
	 * @return el radioReserva
	 */
	public String getRadioReserva() {
		return radioReserva;
	}

	/**
	 * @param radioReserva el radioReserva a establecer
	 */
	public void setRadioReserva(String radioReserva) {
		this.radioReserva = radioReserva;
	}

	

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getNumeroDocumentoContacto() {
		return numeroDocumentoContacto;
	}

	public void setNumeroDocumentoContacto(String numeroDocumentoContacto) {
		this.numeroDocumentoContacto = numeroDocumentoContacto;
	}

	public String getTipoDocumentoContacto() {
		return tipoDocumentoContacto;
	}

	public void setTipoDocumentoContacto(String tipoDocumentoContacto) {
		this.tipoDocumentoContacto = tipoDocumentoContacto;
	}

	/**
	 * @return el telefonoEmpresa
	 */
	public String getTelefonoEmpresa() {
		return telefonoEmpresa;
	}

	/**
	 * @param telefonoEmpresa el telefonoEmpresa a establecer
	 */
	public void setTelefonoEmpresa(String telefonoEmpresa) {
		this.telefonoEmpresa = telefonoEmpresa;
	}
	
	/**
	 * Este metodo cambia el foco del tab de personas al tab de pedidos
	 * @param session
	 * @param request
	 */
	public void cambiarAlTabPedido(HttpSession session, HttpServletRequest request){		
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
			 LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
			 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
			 SessionManagerSISPE.setBeanSession(beanSession, request);
		 }
	}

	/**
	 * @return el numeroDocumentoPersona
	 */
	public String getNumeroDocumentoPersona() {
		return numeroDocumentoPersona;
	}

	/**
	 * @param numeroDocumentoPersona el numeroDocumentoPersona a establecer
	 */
	public void setNumeroDocumentoPersona(String numeroDocumentoPersona) {
		this.numeroDocumentoPersona = numeroDocumentoPersona;
	}

	/**
	 * @return el nombrePersona
	 */
	public String getNombrePersona() {
		return nombrePersona;
	}

	/**
	 * @param nombrePersona el nombrePersona a establecer
	 */
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	/**
	 * @return el telefonoPersona
	 */
	public String getTelefonoPersona() {
		return telefonoPersona;
	}

	/**
	 * @param telefonoPersona el telefonoPersona a establecer
	 */
	public void setTelefonoPersona(String telefonoPersona) {
		this.telefonoPersona = telefonoPersona;
	}

	/**
	 * @return el tipoDocumentoPersona
	 */
	public String getTipoDocumentoPersona() {
		return tipoDocumentoPersona;
	}

	/**
	 * @param tipoDocumentoPersona el tipoDocumentoPersona a establecer
	 */
	public void setTipoDocumentoPersona(String tipoDocumentoPersona) {
		this.tipoDocumentoPersona = tipoDocumentoPersona;
	}

	/**
	 * @return el emailPersona
	 */
	public String getEmailPersona() {
		return emailPersona;
	}

	/**
	 * @param emailPersona el emailPersona a establecer
	 */
	public void setEmailPersona(String emailPersona) {
		this.emailPersona = emailPersona;
	}

	/**
	 * @return el opElaCanEsp
	 */
	public String getOpElaCanEsp() {
		return opElaCanEsp;
	}

	/**
	 * @param opElaCanEsp el opElaCanEsp a establecer
	 */
	public void setOpElaCanEsp(String opElaCanEsp) {
		this.opElaCanEsp = opElaCanEsp;
	}

	public String getValorReplica() {
		return valorReplica;
	}

	public void setValorReplica(String valorReplica) {
		this.valorReplica = valorReplica;
	}
	public String getValorAbonoParcial() {
		return valorAbonoParcial;
	}

	public void setValorAbonoParcial(String valorAbonoParcial) {
		this.valorAbonoParcial = valorAbonoParcial;
	}

	/**
	 * @return el selecionCiudadZonaEntrega
	 */
	public String getSelecionCiudadZonaEntrega() {
		return selecionCiudadZonaEntrega;
	}

	/**
	 * @param selecionCiudadZonaEntrega el selecionCiudadZonaEntrega a establecer
	 */
	public void setSelecionCiudadZonaEntrega(String selecionCiudadZonaEntrega) {
		this.selecionCiudadZonaEntrega = selecionCiudadZonaEntrega;
	}

	public Double getTotalDescuentoIva() {
		return totalDescuentoIva;
	}

	public void setTotalDescuentoIva(Double totalDescuentoIva) {
		this.totalDescuentoIva = totalDescuentoIva;
	}

	public Double getSubTotalNetoBruto() {
		return subTotalNetoBruto;
	}

	public void setSubTotalNetoBruto(Double subTotalNetoBruto) {
		this.subTotalNetoBruto = subTotalNetoBruto;
	}

	public Double getSubTotalBrutoNoAplicaIva() {
		return subTotalBrutoNoAplicaIva;
	}

	public void setSubTotalBrutoNoAplicaIva(Double subTotalBrutoNoAplicaIva) {
		this.subTotalBrutoNoAplicaIva = subTotalBrutoNoAplicaIva;
	}

//	public String[][] getDesactivarCasillasPorcentaje() {
//		return desactivarCasillasPorcentaje;
//	}
//
//	public void setDesactivarCasillasPorcentaje(
//			String[][] desactivarCasillasPorcentaje) {
//		this.desactivarCasillasPorcentaje = desactivarCasillasPorcentaje;
//	}

	public Double getCostoEntregaDomicilio() {
		return costoEntregaDomicilio;
	}

	public void setCostoEntregaDomicilio(Double costoEntregaDomicilio) {
		this.costoEntregaDomicilio = costoEntregaDomicilio;
	}

	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

	public String[] getVectorObsoleto() {
		return vectorObsoleto;
	}

	public void setVectorObsoleto(String[] vectorObsoleto) {
		this.vectorObsoleto = vectorObsoleto;
	}

	public String getCodigoArticuloObsoleto() {
		return codigoArticuloObsoleto;
	}

	public void setCodigoArticuloObsoleto(String codigoArticuloObsoleto) {
		this.codigoArticuloObsoleto = codigoArticuloObsoleto;
	}

	public Boolean getNuevoCodigoBarrasOk() {
		return nuevoCodigoBarrasOk;
	}

	public void setNuevoCodigoBarrasOk(Boolean nuevoCodigoBarrasOk) {
		this.nuevoCodigoBarrasOk = nuevoCodigoBarrasOk;
	}

	public Boolean getNuevoEspecialOk() {
		return nuevoEspecialOk;
	}

	public void setNuevoEspecialOk(Boolean nuevoEspecialOk) {
		this.nuevoEspecialOk = nuevoEspecialOk;
	}

	public Integer getRespaldo() {
		return respaldo;
	}

	public void setRespaldo(Integer respaldo) {
		this.respaldo = respaldo;
	}

	public String getNumBonNavEmp() {
		return numBonNavEmp;
	}

	public void setNumBonNavEmp(String numBonNavEmp) {
		this.numBonNavEmp = numBonNavEmp;
	}
	
	/**
	 * 
	 * @return Campo para entrega a domicilio mi local Sicmer
	 */
	public String getCodigoVendedorSicmer() {
		return codigoVendedorSicmer;
	}
/**
 * 
 * @param codigoVendedorSicmer Campo para entrega a domicilio mi local Sicmer
 */
	public void setCodigoVendedorSicmer(String codigoVendedorSicmer) {
		this.codigoVendedorSicmer = codigoVendedorSicmer;
	}

	/**
	 * 
	 * @return Campo para entrega a domicilio mi local Sicmer
	 */
	public String getQuienRecibeSicmer() {
		return quienRecibeSicmer;
	}

	/**
	 * 
	 * @param codigoVendedorSicmer Campo para entrega a domicilio mi local Sicmer
	 */
	public void setQuienRecibeSicmer(String quienRecibeSicmer) {
		this.quienRecibeSicmer = quienRecibeSicmer;
	}

	/**
	 * @return el horaDesde
	 */
	public String getHoraDesde() {
		return horaDesde;
	}

	/**
	 * @param horaDesde el horaDesde a establecer
	 */
	public void setHoraDesde(String horaDesde) {
		this.horaDesde = horaDesde;
		//al cargar hora desde en calendario sicmer se coloca este mismo dato en otras variables
		if(StringUtils.isNotEmpty(horaDesde)){
			this.horas=horaDesde.substring(0, 2);	
			this.minutos=horaDesde.substring(3, 5);
			this.horasMinutos=horaDesde;
		}
	}

	/**
	 * @return el horaHasta
	 */
	public String getHoraHasta() {
		return horaHasta;
	}

	/**
	 * @param horaHasta el horaHasta a establecer
	 */
	public void setHoraHasta(String horaHasta) {
		this.horaHasta = horaHasta;
	}
	

	public String getCheckReservarStockEntrega() {
		return checkReservarStockEntrega;
	}

	public void setCheckReservarStockEntrega(String checkReservarStockEntrega) {
		this.checkReservarStockEntrega = checkReservarStockEntrega;
	}

	public Boolean getResponsableLocal() {
		return responsableLocal;
	}

	public void setResponsableLocal(Boolean responsableLocal) {
		this.responsableLocal = responsableLocal;
	}	
	/**
	 * @return el opEscogerTodos
	 */
	public String getOpEscogerTodos() {
		return opEscogerTodos;
	}

	/**
	 * @param opEscogerTodos el opEscogerTodos a establecer
	 */
	public void setOpEscogerTodos(String opEscogerTodos) {
		this.opEscogerTodos = opEscogerTodos;
	}

	/**
	 * @return el opEscogerProdBuscados
	 */
	public String[] getOpEscogerProdBuscados() {
		return opEscogerProdBuscados;
	}

	/**
	 * @param opEscogerProdBuscados el opEscogerProdBuscados a establecer
	 */
	public void setOpEscogerProdBuscados(String[] opEscogerProdBuscados) {
		this.opEscogerProdBuscados = opEscogerProdBuscados;
	}

	public String getNombreVendedorSicmer() {
		return nombreVendedorSicmer;
	}

	public void setNombreVendedorSicmer(String nombreVendedorSicmer) {
		this.nombreVendedorSicmer = nombreVendedorSicmer;
	}

	public String getNumeroDocumentoSicmer() {
		return numeroDocumentoSicmer;
	}

	public void setNumeroDocumentoSicmer(String numeroDocumentoSicmer) {
		this.numeroDocumentoSicmer = numeroDocumentoSicmer;
	}

	public String getNpHoraDesde() {
		return npHoraDesde;
	}

	public void setNpHoraDesde(String npHoraDesde) {
		this.npHoraDesde = npHoraDesde;
	}

	public String getNpHoraHasta() {
		return npHoraHasta;
	}

	public void setNpHoraHasta(String npHoraHasta) {
		this.npHoraHasta = npHoraHasta;
	}
		
	/**
	 * @return el callePrincipal
	 */
	public String getCallePrincipal() {
		return callePrincipal;
	}

	
	/**
	 * @param callePrincipal el callePrincipal a establecer
	 */
	public void setCallePrincipal(String callePrincipal) {
		this.callePrincipal = callePrincipal;
	}

	/**
	 * @return el numeroCasa
	 */
	public String getNumeroCasa() {
		return numeroCasa;
	}

	/**
	 * @param numeroCasa el numeroCasa a establecer
	 */
	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}

	/**
	 * @return el calleTransversal
	 */
	public String getCalleTransversal() {
		return calleTransversal;
	}

	/**
	 * @param calleTransversal el calleTransversal a establecer
	 */
	public void setCalleTransversal(String calleTransversal) {
		this.calleTransversal = calleTransversal;
	}

	/**
	 * @return el referencia
	 */
	public String getReferencia() {
		return referencia;
	}

	/**
	 * @param referencia el referencia a establecer
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	/**
	 * Si el detalle es un canasto especial, se valida el stock de los detalles para solicitar autorizacion cuando cumpla las condiciones
	 * @param request
	 * @param detallePedidoDTO
	 * @param clasificacionesAutorizacionGerenteComercial
	 * @throws MissingResourceException
	 * @throws Exception
	 */
	public static boolean verificarStockDeDetallesCanastosEspeciales(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, Collection<String> clasificacionesAutorizacionGerenteComercial) throws MissingResourceException, Exception{
		
		//cuando es canasto especial y no tiene la marca de pedir autorizacion
		if(CotizacionReservacionUtil.esCanasto(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), request)){
//				&& !detallePedidoDTO.isNpSinStockPavPolCan()){
		
			//se verifica si es canasto especial
			if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
					|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
				LogSISPE.getLog().info("validar el stock de los detalles del canasto especial");
				
				detallePedidoDTO.setNpSinStockPavPolCan(false);
				
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
					if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
						
						//se recorren los detalles del canasto especial
						for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
							
							if(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion() !=null &&  CollectionUtils.isNotEmpty(clasificacionesAutorizacionGerenteComercial) 
									&& clasificacionesAutorizacionGerenteComercial.contains(articuloRelacionDTO.getArticuloRelacion().getCodigoClasificacion())
									&& articuloRelacionDTO.getArticuloRelacion().getNpNuevoCodigoClasificacion() == null){
								//se verifica la cantidad reservada en el CD en relaci\u00F3n al stock actual
								LogSISPE.getLog().info("cantidad estado: {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
								LogSISPE.getLog().info("cantidad reserva sic: {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
								LogSISPE.getLog().info("NpStockArticulo: {} ",articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo());
								LogSISPE.getLog().info("Cantidad solicitada al CD: {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
								
								if(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() !=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() !=null){
									
									if((detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() * articuloRelacionDTO.getCantidad()) > 
									(articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() + articuloRelacionDTO.getArticuloRelacion().getNpCantidadCanastosReservados())
									&& detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() > 0){
										
										//solo para el caso de detalles sin autorizaciones o que no esten aprobados
										if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() == null
												|| !articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
											
											LogSISPE.getLog().info("el articulo receta: "+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" necesita autorizacion de stock");
											detallePedidoDTO.setNpSinStockPavPolCan(true);
											break;
										}else if(articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion() != null
												&& (articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)
														|| articuloRelacionDTO.getArticuloRelacion().getNpEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_GESTIONADA))){
											
											//para el caso de autorizaciones aprobadas o gestionadas se verifica las cantidades de los items del canasto
											DetalleEstadoPedidoAutorizacionStockDTO autStockDTO = AutorizacionesUtil.obtenerDetalleEstadoPedidoAutorizacionStockDTO(detallePedidoDTO, false, articuloRelacionDTO.getId().getCodigoArticuloRelacionado());
											if(autStockDTO != null && autStockDTO.getEstadoAutorizacion().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
												
												Long cantidadReservarSic = (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC() * articuloRelacionDTO.getCantidad().longValue()) - (articuloRelacionDTO.getArticuloRelacion().getNpStockArticulo() + articuloRelacionDTO.getArticuloRelacion().getNpCantidadCanastosReservados());
												if(autStockDTO.getCantidadUtilizada() != null && cantidadReservarSic > 0
														&& cantidadReservarSic > autStockDTO.getCantidadUtilizada()){
													
													detallePedidoDTO.setNpSinStockPavPolCan(true);
													LogSISPE.getLog().info("la receta "+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+" necesita autorizacion de stock por: "+(cantidadReservarSic - autStockDTO.getCantidadUtilizada().longValue()));
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
		return detallePedidoDTO.isNpSinStockPavPolCan();
	}
	
	
	/**
	 * Si la entrega que esta siendo eliminada tiene direccion, se elimina la direccion de session
	 * @param request
	 * @param entregaPedidoDTO
	 */
	public static void eliminarDireccionEntregaPedido(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoDTOCol){
		
		HttpSession session = request.getSession();
		
		//obtengo la colecci\u00F3n de detalles de la sesion
		List<DetallePedidoDTO> detallePedido = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			if(CollectionUtils.isNotEmpty(detallePedido)){
				
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					
					if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
						
						for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
							
							//se verifica si existen direcciones
							Collection<DireccionesDTO> direccionesDTOCol = (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX);
							
							if(CollectionUtils.isNotEmpty(direccionesDTOCol) 
									&& StringUtils.isNotEmpty(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio())
									&& !existeEntregaCDDomicilioEnPedidosActuales(entregaDetallePedidoDTO.getEntregaPedidoDTO(), detallePedido)){
								
								//se recorren las direcciones
								for(DireccionesDTO direccionDTO : direccionesDTOCol){
									
									if(direccionDTO.getCodigoDireccion().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio())
											&& direccionDTO.getFechaEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente())
											&& direccionDTO.getCodigoSector().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoLocalSector().toString())){
										
										LogSISPE.getLog().info("se eliminara la direccion {}", direccionDTO.getDescripcion());
										direccionesDTOCol.remove(direccionDTO);
										break;
									}
								}
							}
						}
					}
				}
			}else{
				LogSISPE.getLog().info("ya no existen detalles, se eliminan todas las direcciones");
//				session.removeAttribute(EntregaLocalCalendarioAction.DIRECCIONES);
				session.removeAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX);
			}
		}
	}
	
	
	/**
	 * Verifica si los detalles del pedido tienen la direccion de la entrega eliminada
	 * @param entregaPedidoDTO
	 * @param detallePedidoDTOCol
	 * @return true si existe la entrega, false caso contrario
	 */
	private static boolean existeEntregaCDDomicilioEnPedidosActuales(EntregaPedidoDTO entregaPedidoDTO, List<DetallePedidoDTO> detallePedidoDTOCol){
		
		if(StringUtils.isNotEmpty(entregaPedidoDTO.getNpDireccionEntregaDomicilio()) && CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			//se recorren los detalles
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
				
				//se verifica si el detalle tiene entregas
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
					
					//se recorren las entregas
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){

						if(StringUtils.isNotEmpty(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio())
								&& entregaPedidoDTO.getNpDireccionEntregaDomicilio().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio())){
							
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * Valida los campos requeridos en la configuracion de entregas cuando es a 
	 * domicilio desde el CD
	 * @param request
	 * @param warnings
	 * @param errors
	 * @param error
	 */
	public void validarCamposRequeridosEntregasDomicilioDesdeCD(HttpServletRequest request, ActionMessages errors){
		
		//validacion de la ciudad de entrega
		if(this.getSeleccionCiudad() == null || this.getSeleccionCiudad().isEmpty()){
			errors.add("campoCiudadRequerido",new ActionMessage("warnings.campo.ciudad.requerido"));
			
		}else if(this.getSeleccionCiudad().equals("ciudad")){
			request.getSession().removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO);
			errors.add("ciudad", new ActionMessage("errors.seleccion.ciudad", "Ciudad de Entrega V\u00E1lida"));
			//se quita de sesion los sectores porque la ciudad no es valida
			request.getSession().removeAttribute(EntregaLocalCalendarioAction.CIUDAD_SECTOR_ENTREGA);
		}
		
		//validacion de el sector de la ciudad 
		if(this.getSelecionCiudadZonaEntrega() == null || this.getSelecionCiudadZonaEntrega().isEmpty()){
			errors.add("campoCiudadRequerido",new ActionMessage("warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora"));
		}
		
		//IO, validacion de nuevos campos para obtener direccion completa.
		if(this.callePrincipal== null || this.callePrincipal.isEmpty()){
			errors.add("callePrincipal",new ActionMessage("errors.requerido","Calle principal"));
		}
		if(this.numeroCasa== null || this.numeroCasa.isEmpty()){
			errors.add("numeroCasa",new ActionMessage("errors.requerido","# de casa"));
		}
		if(this.calleTransversal== null || this.calleTransversal.isEmpty()){
			errors.add("calleTransversal",new ActionMessage("errors.requerido","Calle transversal"));
		}
		if(this.referencia== null || this.referencia.isEmpty()){
			errors.add("referencia",new ActionMessage("errors.requerido","Referencia/Contacto/ Tel\u00E9fono completo:"));
		}
		
		//IO, concatenacion de campos para armar la direccion completa.
		this.direccion = this.callePrincipal + " " + this.numeroCasa + " y " + this.calleTransversal + ". " + this.referencia;
		
		//validacion de la unidad de tiempo
		if(this.getUnidadTiempo() == null || this.getUnidadTiempo().isEmpty()){
			errors.add("unidadTiempo", new ActionMessage("errors.unidadTiempo.requerido"));
		}
	}

	/**
	 * @return el checksClasificaciones
	 */
	public String[] getChecksClasificaciones() {
		return checksClasificaciones;
	}

	/**
	 * @param checksClasificaciones el checksClasificaciones a establecer
	 */
	public void setChecksClasificaciones(String[] checksClasificaciones) {
		this.checksClasificaciones = checksClasificaciones;
	}

	/**
	 * @return el codigoClasificacion
	 */
	public String getCodigoClasificacion() {
		return codigoClasificacion;
	}

	/**
	 * @param codigoClasificacion el codigoClasificacion a establecer
	 */
	public void setCodigoClasificacion(String codigoClasificacion) {
		this.codigoClasificacion = codigoClasificacion;
	}

	/**
	 * @return el nombreClasificacion
	 */
	public String getNombreClasificacion() {
		return nombreClasificacion;
	}

	/**
	 * @param nombreClasificacion el nombreClasificacion a establecer
	 */
	public void setNombreClasificacion(String nombreClasificacion) {
		this.nombreClasificacion = nombreClasificacion;
	}

	/**
	 * @return el nombreArticulo
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	/**
	 * @param nombreArticulo el nombreArticulo a establecer
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	/**
	 * @return el opcionBusqueda
	 */
	public String getOpcionBusqueda() {
		return opcionBusqueda;
	}

	/**
	 * @param opcionBusqueda el opcionBusqueda a establecer
	 */
	public void setOpcionBusqueda(String opcionBusqueda) {
		this.opcionBusqueda = opcionBusqueda;
	}

	/**
	 * @return el botonBuscarProd
	 */
	public String getBotonBuscarProd() {
		return botonBuscarProd;
	}

	/**
	 * @param botonBuscarProd el botonBuscarProd a establecer
	 */
	public void setBotonBuscarProd(String botonBuscarProd) {
		this.botonBuscarProd = botonBuscarProd;
	}

	/**
	 * @return el botonAnadirArt
	 */
	public String getBotonAnadirArt() {
		return botonAnadirArt;
	}

	/**
	 * @param botonAnadirArt el botonAnadirArt a establecer
	 */
	public void setBotonAnadirArt(String botonAnadirArt) {
		this.botonAnadirArt = botonAnadirArt;
	}

	/**
	 * @return el botonCancelarArt
	 */
	public String getBotonCancelarArt() {
		return botonCancelarArt;
	}

	/**
	 * @param botonCancelarArt el botonCancelarArt a establecer
	 */
	public void setBotonCancelarArt(String botonCancelarArt) {
		this.botonCancelarArt = botonCancelarArt;
	}

	/**
	 * @return el precioEspecial
	 */
	public String[] getPrecioEspecial() {
		return precioEspecial;
	}

	/**
	 * @param precioEspecial el precioEspecial a establecer
	 */
	public void setPrecioEspecial(String[] precioEspecial) {
		this.precioEspecial = precioEspecial;
	}

	/**
	 * @return el botonActualizarArt
	 */
	public String getBotonActualizarArt() {
		return botonActualizarArt;
	}

	/**
	 * @param botonActualizarArt el botonActualizarArt a establecer
	 */
	public void setBotonActualizarArt(String botonActualizarArt) {
		this.botonActualizarArt = botonActualizarArt;
	}

	public String getCheckAbonoCero() {
		return checkAbonoCero;
	}

	public void setCheckAbonoCero(String checkAbonoCero) {
		this.checkAbonoCero = checkAbonoCero;
	}

	
}