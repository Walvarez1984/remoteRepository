/*
 * EntregaLocalCalendarioAction.java
 * Creado el 14/02/2013 10:14:34
 *   	
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;


import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CHECK_PAGO_EFECTIVO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_DOMICIO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_MI_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_OTRO_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.STOCK_ENTREGA_LOCAL;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.dto.SearchDTO;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.dto.DivisionGeoPoliticaDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloBitacoraCodigoBarrasID;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloID;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.CalendarioLocalEntregaDomicilioUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.ManejarArchivo;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.EstructuraResponsable;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalID;
import ec.com.smx.sic.sispe.dto.CalendarioHoraLocalDTO;
import ec.com.smx.sic.sispe.dto.ConfiguracionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.reportes.dto.AutorizacionEntregasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.CostoEntregasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.DireccionesDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class EntregaLocalCalendarioAction extends BaseAction{
	
	/************************Variables desesion***************************************/
	public static final String CALENDARIODIALOCALCOL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol"; //calendario para un mes especifico
	public static final String CALENDARIODIALOCALCOLBODEGA= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOBodegaCol"; //calendario para un mes especifico
	public static final String MESSELECCIONADOBODEGA= "ec.com.smx.calendarizacion.mes.seleccionado.bodega"; //calendario para un mes especifico
	public static final String NUMEROSEMANASBODEGA="ec.com.smx.calendarizacion.semana.seleccionado.bodega";
	public static final String CALENDARIODIALOCAL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTO"; //CD del dia seleccionado en el calendario
	public static final String CALENDARIODIALOCALCOLAUX= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOColAux"; //dias que deben ser modificados su cantidad acumulada
	public static final String SECDIRECCIONES= "ec.com.smx.sic.sispe.pedido.secDirecciones"; //numero de direcciones
	public static final String DIRECCION= "ec.com.smx.sic.sispe.direccionDTO"; //direccion
	private static final String VISTALOCALCOL= "ec.com.smx.sic.sispe.vistaLocalDTOCol"; //locales
	public static final String VISTALOCALORIGEN= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.origen"; //local origen
	public static final String VISTALOCALDESTINO= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.destino"; //local destino
	public static final String VISTALOCALACTUAL= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.destino"; //local que esta logueado
	public static final String LOCALID= "ec.com.smx.sic.sispe.localID"; //id del local seleccionado
	public static final String DIASELECCIONADO= "ec.com.smx.calendarizacion.diaSeleccionado"; //dia de despacho seleccionado en el calendario
	public static final String DIASELECCIONADOAUX= "ec.com.smx.calendarizacion.diaSeleccionadoAux"; //dia de despacho seleccionado en el calendario
	public static final String MESSELECCIONADO= "ec.com.smx.calendarizacion.mesBusqueda"; //mes seleccionado en el calendario
	public static final String FECHAMINIMA= "ec.com.smx.sic.sispe.fechaMinima"; //fecha minima de despacho a locales
	public static final String FECHAMAXIMA= "ec.com.smx.sic.sispe.fechaMaxima"; //fecha maxima de despacho a locales
	private static final String SECTORSELECCIONADO= "ec.com.smx.sic.sispe.sectorSeleccionado"; //indice local seleccionado
	private static final String FECHABUSQUEDA= "ec.com.smx.calendarizacion.fechaBuscada"; //fecha minima de entrega
	public static final String NUMEROSEMANAS= "ec.com.smx.calendarizacion.numeroSemanas"; //numero de semanas que tiene un mes
	private static final String ORDENDIAS= "ec.com.smx.sic.sispe.calendarizacion.ordenDias"; //Orden de dias de la semana
	private static final String CONFIGURACION= "ec.com.smx.sic.sispe.entregas.configuracionDTOCol"; //Coleccion de configuraciones para las entregas

	public static final String AUTORIZACION= "ec.com.smx.sic.sispe.pedidos.autorizacionDTO"; //autorizacion viene del formulario

	public static final String DETALLELPEDIDOAUX= "ec.com.smx.sic.sispe.detallePedidoAux"; //contiene el detalle del pedido se incia con el detalle incial y se trabaja con el si se elige guardar la entrega se sobreescribe la sesion del detalle del pedido
	public static final String ESTRUCTURA_RESPONSABLE= "ec.com.smx.sic.sispe.estructuraResponsable"; //contiene por entregas del pedido los responsables
	public static final String DIRECCIONES= "ec.com.smx.sic.sispe.pedido.direcciones"; //direcciones donde se han relizado entregas
	public static final String DIRECCIONESAUX= "ec.com.smx.sic.sispe.pedido.direccionesAux"; //sesion auxiliar para almacenar las direcciones antes de guardar
	public static final String COSTOENTREGA= "ec.com.smx.sispe.pedido.costoEntregasDTOCol"; //coleccion de los costos de las entregas a domicilio
	public static final String COSTOENTREGAAUX= "ec.com.smx.sispe.pedido.costoEntregasDTOColAux"; //coleccion de los costos de las entregas auxiliar antes de grabar los cambios
	public static final String CALENDARIOCONFIGURACIONDIALOCAL= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTO"; //configuracion real de las modificaciones en el calendario
	public static final String CALENDARIOCONFIGURACIONDIALOCALAUX= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTOAux"; //configuracion auxiliar de las modificaciones en el calendario
	public static final String CALENDARIOCONFIGURACIONDIALOCALAUX1= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTOAux1"; //configuracion auxiliar de las modificaciones en el calendario
	public static final String VALORTOTALENTREGAAUX= "ec.com.smx.sispe.pedido.valorTotalEntregaAux"; //costo entrega total
	public static final String VALORPARCIALENTREGA= "ec.com.smx.sispe.pedido.valoParcialEntrega"; //costo entrega total
	public static final String SECDIRECCIONESAUX= "ec.com.smx.sic.sispe.pedido.secDireccionesAux"; //numero de direcciones
	public static final String VISTAESTABLECIMIENTOCIUDADLOCAL="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol"; //Carga las ciudades para la entrega a domicilio desde el CD
	private static final String LUGARENTREGADOMICILIO="ec.com.smx.sic.sispe.lugarEntregaDomicilio"; //etiqueta que indica desde donde es la entrega a domicilio
	
	public static final String EXISTENCAMBIOS= "ec.com.smx.sic.sispe.existenCambios"; //Variable para saber si existieron cambios en las entregas
	public static final String ENTIDADRESPONSABLELOCAL= "ec.com.smx.sic.sispe.entidadResponsable"; //Variable para saber si la entidad responsable es el local
	
	
	public static final String NOMBREENTIDADRESPONSABLE= "ec.com.smx.sic.sispe.nombreEntidadResponsable"; //Variable que indica cual es la entidad responsable
	private static final String EXISTELUGARENTREGA="ec.com.smx.sic.sispe.existeLugarEntrega";//Variable de sesion que indica si ya fue seleccionado un lugar de entrega
	private static final String EDITAFECHAMINIMA="ec.com.smx.sic.sispe.editaFechaMinima";//Variable de sesion que indica si la fecha minima va a poder ser editada o no
	
	public static final String EXISTENENTREGAS="ec.com.smx.sic.sispe.existenEntregas";//Variable de sesion que indica si existe al menos una entrega para que salga visible el check de eliminar
	
	public static final String COMBOSELECCIONCIUDAD="ec.com.smx.sic.sispe.comboSeleccionCiudad";//Variable de sesion que indica que debe ser visible el combo de ciudades
	
	//Variables para configurar los diferentes tipos de entregas
	public static final String SELECCIONARLOCAL="ec.com.smx.sic.sispe.seleccionarLocal";//Seleccionar el local 
	public static final String SELECCIONARCALENDARIO="ec.com.smx.sic.sispe.seleccionarCalendario";//Seleccionar fecha de recepcion
	public static final String HABILITARCANTIDADENTREGA="ec.com.smx.sic.sispe.habilitarCantidadEntrega";//Cantidad de entrega
	public static final String HABILITARCANTIDADRESERVA="ec.com.smx.sic.sispe.habilitarCantidadReserva";//Cantidad de recepcion
	public static final String HABILITARDIRECCION="ec.com.smx.sic.sispe.habilitarDireccion";//Direccion y distancia
	public static final String HABILITARBOTONACEPTAR="ec.com.smx.sic.sispe.habilitarBotonAceptar";//Boton aceptar
	private static final String CONFIGURACIONCARGADA="ec.com.smx.sic.sispe.configuracionCargada";//Variable de sesion que indica si configuracion de la entrega ya fue cargada se usa para indicar el paso uno de la ayuda
	
	//Variable para la navegaci\u00F3n del popUpEntregas
	public static final String TABSELECCIONADONAVEGACION="ec.com.smx.sic.sispe.tabseleccionadonavegacion";//tab seleccionado
	
	public static final String MOSTRAROPCIONCANASTOSESPECIALES = "ec.com.smx.sic.sispe.mostraropcioncanastosespeciales";//para mostrar opcion de canastos especiales
	public static final String BLOQUEAROPCIONENTREGADOMICILIO = "ec.com.smx.sic.sispe.bloquearopcionentregadomicilio";//para mostrar opcion de canastos especiales
	public static final String OPCIONCANASTOSESPECIALES="ec.com.smx.sic.opcioncanastosespeciales"; //Sesion que indica la ultima opcion de canastos especiales
	public static final String PASOSPOPUP = "ec.com.smx.sic.pasospopup";//para el control de los mensajes de los pasos
	public static final String CONFIRMACIONANTERIORCONFENTREGAS="ec.com.smx.sic.confirmacionanteriorentregas";//para confirmar cuando quiere regresar en la configuracio de las entregas
	public static final String POSICIONDIVCONFENTREGAS = "ec.com.smx.sic.sispe.posiciondivconfentregas" ;
	
	//Almacena la configuracion de las entregas
	public static final String LUGARENTREGA="ec.com.smx.sic.sispe.lugarEntrega";//Registra el lugar de entrega
	public static final String TIPOENTREGA="ec.com.smx.sic.sispe.tipoEntrega";//Registra el tipo de entrega
	public static final String STOCKENTREGA="ec.com.smx.sic.sispe.stockEntrega";//Registra de donde va ser tomado el stock
	public static final String STOCKENTREGAAUX="ec.com.smx.sic.sispe.stockEntregaAux";//Registra de donde va ser tomado el stock para manejo de cantidades en entrega parcial
	
	public static final String FECHAENTREGACLIENTE="ec.com.smx.sic.sispe.fechaEntregaCliente";//Sirve para guardar la fecha de entrega del cliente
	
	public static final String MENSAJEPASOS="ec.com.smx.sic.mensajes"; //Sesion que indica los mensajes que saldran en cada paso
	public static final String PASO="ec.com.smx.sic.paso"; //Indica el numero de paso que debe salir en el mensaje
	
	public static final String OPCIONLUGARENTREGA="ec.com.smx.sic.opcionLugarEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
	public static final String OPCIONTIPOENTREGA="ec.com.smx.sic.opcionTipoEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
	public static final String OPCIONSTOCK="ec.com.smx.sic.opcionStock"; //Sesion que indica la ultima opcion del stock seleccionado
	public static final String BANDERA_CONFIGURA_CAL_BOD = "ec.com.smx.sic.flag.calendario.bodega";
	
	private static final String DESACTIVAR_ENT_RES_LOC = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("entregas.desactivarEntResLoc");
	
	//OANDINO: Variables de session utilizadas en la validaci\u00F3n de c\u00F3digo de local de tr\u00E1nsito ---------------------
	//Variable que permite mantener el valor obtenido de VALORPARAMETRO desde SCSPETPARAMETRO
	public static final String VAR_VALOR_PARAMETRO_TOTAL = "ec.com.smx.sic.sispe.valorParametro";
	
	//Variable que almacena el valor seleccionado del combo de cuidades "seleccionCiudad" en "localesCalendario.jsp"
	public static final String VAR_VALOR_CODIGO_CIUDAD_COMBO = "ec.com.smx.sic.sispe.codigoCiudad";
	public static final String CIUDAD_SECTOR_ENTREGA = "ec.com.smx.sic.sispe.ciudad.sector.entrega";
	public static final String CLASIFICACIONES_PERECIBLES = "ec.com.smx.sic.sispe.clasificacionesPerecibles";
	public static final String TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE = "ec.com.smx.sic.sispe.totalSolicitadoCDaDomicilioNoPerecible";
	public static final String PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO = "ec.com.smx.sic.sispe.parametroLimiteInferiorObligatorioTransito";
	public static final String MOSTRAR_POPUPTAB = "ec.com.smx.sic.sispe.mostrarPopUpTab";
	
	public static final String ENTREGAS_RESPONSABLES= "ec.com.smx.sic.sispe.entregasResp"; //contiene el detalle del pedido con sus responsables
	public static final String HORA_SELECCIONADA = "ec.com.smx.sic.sispe.hora.seleccionada"; //vector horas[0], minutos[1], segundos[2]
	//public static final String PEDIDO_DISTRIBUIDO_CAMIONES = "ec.com.smx.sic.sispe.pedido.distribuido.camiones"; //bandera indica que el pedido ya fue asignado a camiones 
	public static final String CALENDARIO_HORA_LOCAL_SELECCIONADO = "ec.com.smx.sic.sispe.calendario.hora.local.seleccionado"; //guarda el obj tipo CalendarioHoraCamionLocalDTO
	public static final String MES_ACTUAL_CALENDARIO = "ec.com.smx.sic.sispe.mes.actual.calendario";
	
	//Variable para mostrar o no el calendario de bodega por horas
	public static final String MOSTRAR_CALENDARIO_BODEGA_POR_HORAS = "ec.com.smx.sic.sispe.mostrar.calendario.bodega.por.horas";	
	public static final String CALENDARIODIALOCAL_PARCIAL_POR_HORAS = "ec.com.smx.sic.sispe.calendario.parcial.por.horas"; //se mantienen los datos de la calendarizacion por horas cuando es entrega parcial
	
	//variable para los checks de transito de las entregas
	public static final String CHECKTRANSITO = "ec.com.smx.sispe.checktransito";
	
//	public static final String ELIMINO_TODAS_ENTREGAS = "ec.com.smx.sispe.eliminarTodasEntregas";
	
//	variable para saber si se elimino alguna direccion de las entrega
	public static final String ENTREGAS_ELIMINO_DIRECCION = "ec.com.smx.sic.sispe.entregas.elimino.direccion";
	
	//variable para almacenar las entregas configuradas en la session
	public static final String ENTREGAS_PEDIDO = "ec.com.smx.sic.sispe.entregas.pedido";
	
	public static final String HORACOL="ec.com.smx.sic.sispe.horaCol";
	public static final String MINUTOCOL="ec.com.smx.sic.sispe.minutoCol";
	public static final String CODIGOENTREGAPREVIO = "ec.com.smx.sic.sispe.entregas.codigoEntrega.previo";
	
	/**
	 * Almacenar los calendarios existentes y creados al momento de reallizar la configuracion e entregas
	 */
	public static final String CALENDARIOS_ENTREGAS_ELIMINAR 	= "ec.com.smx.sic.sispe.calendario.entregas.eliminar";
	
	public static final String CANTIDAD_CANASTOS_ENTREGAS_BODEGA = "ec.com.smx.sic.sispe.cantidad.canastos.entrega.bodega";
	/**
	 * Autorizaciones de entregas
	 */
	private static final String AUTORIZACIONENTREGASCOL= "ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol"; //coleccion de autorizaciones
	
	public int indiceEntrega;
	public static Boolean editarEntrega = false;
	public static final String EDITAR_ENTREGA= "ec.com.smx.sic.sispe.pedidos.editarEntrega";
	
	//Listas temporales para editar las entregas
	public Collection<DetallePedidoDTO> detallePedidoDTOColTmp ;
	public ArrayList<EntregaDetallePedidoDTO> entregasTmp;
	public Collection<DireccionesDTO> direccionesColTmp;
	public Object[] calendarioDiaLocalDTOOBJTmp;
	private CalendarioHoraLocalDTO calendarioHoraLocalSeleccionado;
	private Set<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol;
	public static final String DIRECCIONES_TMP = "ec.com.smx.sic.sispe.pedidos.direccionesTmp";
	private Boolean asignaLocal=false;
	private String ciudadGye;
	private String ciudad;
	//private int bultos;
	public static final String BOOLEAN_CIUDAD_SOLO_LECTURA= "ec.com.smx.sic.sispe.entregas.sicmer.ciudad.sololectura.boolean";
	public static final String FORMULARIO_SICMER= "ec.com.smx.sic.sispe.entregas.sicmer.formulario";
	public static final String ESTABLECIMIENTOS_SICMER= "ec.com.smx.sic.sispe.bloqueoEstablecimientosSICMER";
	
	/**
	 * <p>
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
	 * </p>
	 * 
	 * @param mapping 		El mapeo utilizado para seleccionar esta instancia
	 * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request 		La petici&oacue; que estamos procesando
	 * @param response 		La respuesta HTTP que se genera
	 * @return ActionForward	Los seguimiento de salida de las acciones
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		/************************Declaracion de variables**********************************/
		HttpSession session = request.getSession();
		ActionMessages messages=new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages info = new ActionMessages();
		ActionMessages warnings=new ActionMessages();
		ActionErrors error = new ActionErrors();
		CotizarReservarForm formulario=(CotizarReservarForm)form;
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		String forward="desplegar";
		
		String salida = "desplegar";
		String accionEstado= "entregaLocalCalendario.do";
		
		//Mensajes en los botones del popUp de configuracion de entregas
		final String anterior = MessagesWebSISPE.getString("boton.confirmacion.reservas.anterior");
		final String confirmar = MessagesWebSISPE.getString("boton.confirmacion.reservas.confirmar");
		final String siguiente = MessagesWebSISPE.getString("boton.confirmacion.reservas.siguiente");
		final String atras = MessagesWebSISPE.getString("boton.confirmacion.reservas.atras");
		
		//se obtienen las claves que indican un estado activo y un estado inactivo
		final String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		final String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);	
		session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
		//ya esta realizada la configuacion de entregas de todos los detalles
		if(request.getParameter("sinCambiosEnEntregas")!=null){
			LogSISPE.getLog().info("--- todos los detalles estan configurados ---");
			warnings.add("sinCambios",new ActionMessage("warnings.sinCambiosReserva"));
		}
		else if(request.getParameter("entregasCompletas")!=null){
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			LogSISPE.getLog().info("--- todos los detalles estan configurados ---");
			info.add("entregasCompletas",new ActionMessage("info.entregas.completas"));
		}
		
		//Configuracion de la entrega
		else if(request.getParameter("abrirConfiguracion")!=null){
			LogSISPE.getLog().info("--- abrir Popup configuracion ---");
			editarEntrega = false;
			session.setAttribute(EDITAR_ENTREGA, false);
			// Objetos para construir los tabs, si fue modificacion de reserva
			//asignacionValoresFormulario(session, formulario);			
			request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
			request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
			beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
			instanciarVentanaOpcionesConfiguracion(request);
			session.setAttribute(MOSTRAR_POPUPTAB, "ok");
			session.removeAttribute(HABILITARCANTIDADENTREGA);
			session.removeAttribute(HABILITARCANTIDADRESERVA);
			session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
			session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
			session.removeAttribute(PASOSPOPUP);
			request.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
			session.removeAttribute( POSICIONDIVCONFENTREGAS);
			session.removeAttribute(HORA_SELECCIONADA);
			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
			session.removeAttribute(ConsolidarAction.MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS);
			regresarvaloresInicialesExistenEntregas(session);
			formulario.setSeleccionCiudad(null);
			formulario.setSelecionCiudadZonaEntrega(null);
			session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
			
			//para mostrar opcion de canastos especiales
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
			////formulario.setOpElaCanEsp(null);
			//si esta condicion es falsa significa que no debe poder configurar para que el CD haga esta entrega a domicilio solo MI LOCAL Y OTRO LOCAL
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
			String codClaRecetasNuevas = parametroDTO.getValorParametro();
			
			ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
			Integer codMinCanastosEspeciales = Integer.parseInt(parametroMinCanastas.getValorParametro());
			
			if(CotizacionReservacionUtil.verificarExistenCanastasEspeciales(detallePedidoDTOCol, request,codClaRecetasNuevas,codMinCanastosEspeciales)){
				session.setAttribute(MOSTRAROPCIONCANASTOSESPECIALES, "ok");
				if(!CotizacionReservacionUtil.verificarCantidadCanastasEspeciales(detallePedidoDTOCol, request,codClaRecetasNuevas,codMinCanastosEspeciales, info)){
					session.setAttribute(BLOQUEAROPCIONENTREGADOMICILIO, "ok");
					info.add("entregasCompletas",new ActionMessage("info.bloqueo.entregas.domicilio"));
				}
			}else{
				session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
				session.removeAttribute(BLOQUEAROPCIONENTREGADOMICILIO);
			}
			
			obtenerEstablecimientosEntregasSICMER(request, session);
			
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA, null);			
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONTIPOENTREGA, null);			
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK, null);
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES, null);
		}
		
		//TODO Falta la seccion de autorizaciones para la entrega
		
		//Configuracion de la entrega
		else if(request.getParameter("entregas") != null){
			
			LogSISPE.getLog().info("Opci\u00F3n CD: {}",formulario.getOpStock());
			LogSISPE.getLog().info("entra a habilitar los parametros de ingreso segun la combinacion de opciones seleccionada");
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
								
			if(beanSession.getPaginaTabPopUp().getTabSeleccionado()!=0){
				EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			}
			//se carga la ciudad del local del usuario logueado
			if(formulario.getOpLugarEntrega() != null && formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
				
				VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
				vistaLocalDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));
				vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				vistaLocalDTO.setNombreCiudad("");
				LogSISPE.getLog().info("va a consultar el local para SICMER");
				Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
				VistaLocalDTO vistaLocalDTOE=vistaLocalDTOCol.iterator().next();
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesRecomendadasEntregasDomicilio", request);
				String[] ciudadesRecomendadas = parametroDTO.getValorParametro() != null ? parametroDTO.getValorParametro().split(",") : null;
				if(Arrays.asList(ciudadesRecomendadas).contains(vistaLocalDTOE.getCodigoCiudad())){
					formulario.setSeleccionCiudad(vistaLocalDTOE.getCodigoCiudad());
					
				}else{
					formulario.setSeleccionCiudad("OTR"+vistaLocalDTOE.getNombreCiudad()+"/"+vistaLocalDTOE.getCodigoCiudad());
					
				}
				LogSISPE.getLog().info("Ciudad para SICMER: "+formulario.getSeleccionCiudad());
				//se coloca que solo sea de lectura el combo
				session.setAttribute(BOOLEAN_CIUDAD_SOLO_LECTURA,"true");
				//se sube a sesion el formulario
				CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
				session.setAttribute(FORMULARIO_SICMER,formularioSicmer);
				//carga combo con zonas de ciudad encontrada
				cargarZonasCiudadComboSICMER(formulario, session, info);
				if((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES)!=null && formulario.getOpElaCanEsp()==null){
					formulario.setOpElaCanEsp((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES));
				}
				if(formulario.getOpElaCanEsp()!=null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
					if(StringUtils.isNotEmpty(formulario.getHoraDesde())){
						session.setAttribute(CalendarioLocalEntregaDomicilioUtil.HORAS_DIA_DESDE, CalendarioLocalEntregaDomicilioUtil.cargarHorasDia(7,21));
						session.setAttribute(CalendarioLocalEntregaDomicilioUtil.HORAS_DIA_HASTA, CalendarioLocalEntregaDomicilioUtil.cargarHorasDia(Integer.parseInt(formulario.getHoraDesde().substring(0,2))+1,22));
					//si no es hoy y no se ha elegido las horas
					}else{
						session.setAttribute(CalendarioLocalEntregaDomicilioUtil.HORAS_DIA_DESDE, CalendarioLocalEntregaDomicilioUtil.cargarHorasDia(7,21));
						session.setAttribute(CalendarioLocalEntregaDomicilioUtil.HORAS_DIA_HASTA, CalendarioLocalEntregaDomicilioUtil.cargarHorasDia(8,22));
					}
				}
			}
			
			if(session.getAttribute(FECHABUSQUEDA) != null && formulario.getBuscaFecha() == null){
				sumarDiasParaProducirCanastosEspeciales(request, formulario,info,Boolean.FALSE);
				
				long diferenciaEntregaBusca= ConverterUtil.returnDateDiff((String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA),formulario.getFechaEntrega());
				if(diferenciaEntregaBusca<0){//si la fecha de busqueda fue cambiada x disminucion de fecha minima con autorizacion, tenemos que regresarle al valor original
					session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getFechaEntrega());
				}
				session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getFechaEntrega()));
				formulario.setBuscaFecha(formulario.getFechaEntrega());
//				}else{
//					formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//				}
			}
			
			if(session.getAttribute(FECHABUSQUEDA)!=null && formulario.getFechaEntregaCliente() == null){
				//a la fecha establecida como de entrega se le suma un d\u00EDa para dibujar en el calendario de despacho de los locales y tener para seleccionar minimo 2 dias.
				Date fechaEntregaCliente = (Date)session.getAttribute(FECHABUSQUEDA);
				GregorianCalendar fechaCalendario = new GregorianCalendar();
				if(editarEntrega){
					//obtiene la lista de entregas temporal
					ArrayList<EntregaDetallePedidoDTO> entregas = this.entregasTmp;	
					//obtiene la entrega seleccionada
					EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);
					fechaEntregaCliente = entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente();
					fechaCalendario.setTime(fechaEntregaCliente);
				}else{					
					fechaCalendario.setTime(fechaEntregaCliente);
					fechaCalendario.add(Calendar.DAY_OF_MONTH, 1);
				}	
				formulario.setFechaEntregaCliente(DateManager.getYMDDateFormat().format(fechaCalendario.getTime()));
			}
			
			if(formulario.validarTipoEntregas(error,request)==0){
				
				if(formulario.getOpElaCanEsp()==null){
					formulario.setOpElaCanEsp(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
				}
				
				if(formulario.validarCantidadesIngresadasModificadas(error, request) == 0){
					   if(verificarArticulosObsoletos(session,info,formulario)){
						aceptaConfiguracionEntrega(request, formulario, error, errors, warnings,info);
						EntregaLocalCalendarioUtil.cargaCiudades(request, errors);
						if(session.getAttribute(OPCIONLUGARENTREGA)!=null && session.getAttribute(OPCIONLUGARENTREGA).toString().equals(CONTEXTO_ENTREGA_DOMICIO)){
							ParametroDTO parametroDTOR = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesEntregaDomicilio", request);
							if(parametroDTOR!=null && parametroDTOR.getValorParametro() != null){
								request.getSession().setAttribute("ec.com.smx.sic.sispe.entrega.ciudadesPermitidasDomicilio", parametroDTOR.getValorParametro());
							}else{
								request.getSession().setAttribute("ec.com.smx.sic.sispe.entrega.ciudadesPermitidasDomicilio", "");
							}
						}
						if(errors.size()>0){
							LogSISPE.getLog().info("no se acpeto la configuracion");
							
							if(session.getAttribute(OPCIONLUGARENTREGA)!=null){
								//Si la configuraci\u00F3n no fue aceptada regreso a la fecha y configuracion anterior
								formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
								formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
								formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
								formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
								
								if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && 
										(session.getAttribute(NOMBREENTIDADRESPONSABLE)).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
									//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
									formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
								}
							}
						}	
						
						if(error.size() == 0 && errors.size() == 0){
							
							String tabHabilitados = tabHabilitadosParciales(request);
							
							if((String)session.getAttribute(SessionManagerSISPE.MENSAJES_SISPE)==null){
								session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
							}
							
							if(tabHabilitados == null){
								if(beanSession.getPaginaTabPopUp().getCantidadTabs() == 3){
									beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
									regresarValoresIniciales(session);
									
								}
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								if(formulario.getFechaEntregaCliente()!=null && formulario.getFechaEntregaCliente().equals(sdf.format(Calendar.getInstance().getTime()))){
									session.setAttribute(EntregaLocalCalendarioAction.HORACOL, CotizarReservarAction.obteneHorasMinutos(true));
								}else{
									session.setAttribute(EntregaLocalCalendarioAction.HORACOL, CotizarReservarAction.obteneHorasMinutos(false));
								}
								navegacionPopUp(session, beanSession, anterior, confirmar,"entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajesPopUp','entregas','pregunta','opcionesBusqueda'",1,"anteriorD","aceptarD");
							}else{
								//parciales
								if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){
									navegacionPopUp(session, beanSession,anterior,confirmar,"entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajes','entregas','pregunta','opcionesBusqueda'",2,"anteriorD","aceptarD");
									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
									SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
									if(formulario.getFechaEntregaCliente()!=null && formulario.getFechaEntregaCliente().equals(sdf.format(Calendar.getInstance().getTime()))){
										session.setAttribute(EntregaLocalCalendarioAction.HORACOL, CotizarReservarAction.obteneHorasMinutos(true));
									}else{
										session.setAttribute(EntregaLocalCalendarioAction.HORACOL, CotizarReservarAction.obteneHorasMinutos(false));
									}
								}
								else{
									navegacionPopUp(session, beanSession,anterior,siguiente,"entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","entregas","","'mensajesPopUp','listado_articulos'",1,"anteriorD","siguienterD");
									session.setAttribute(TABSELECCIONADONAVEGACION, 2);
									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
									regresarValoresIniciales(session);
								}
							}
						}
					}
				}
			}
			
			//si la opci\u00F3n es editar la entrega
			if(editarEntrega && error.size() == 0 && errors.size() == 0){
				//obtiene la lista de entregas temporal
				ArrayList<EntregaDetallePedidoDTO> entregas = this.entregasTmp;
				//obtiene la entrega seleccionada
				EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);	
				//guarda el codigo de entrega previo para la nueva entrega editada, solamente si mantiene los contextos de entrega
				if(session.getAttribute(OPCIONSTOCK).toString().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().toString())
						&&session.getAttribute(OPCIONLUGARENTREGA).toString().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().toString())
						&&session.getAttribute(OPCIONTIPOENTREGA).toString().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAlcanceEntrega().toString())
						&&session.getAttribute(OPCIONCANASTOSESPECIALES).toString().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp())){
					session.setAttribute(CODIGOENTREGAPREVIO, entregaDetallePedidoDTO.getEntregaPedidoDTO().getId().getCodigoEntregaPedido());
						}
				LogSISPE.getLog().info("Editar entrega, codigo contexto entrega: "+entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega());
				//otro local	
				if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")))){
					if(formulario.getOpLugarEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().toString()) && beanSession.getPaginaTabPopUp().getTabSeleccionado() == 2
							&& !asignaLocal){
							formulario.setLocal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().substring(7,10));
							formulario.setListaLocales(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().substring(7,10));
							aceptaConfiguracionEntrega(request, formulario, error, errors, warnings,info);
							asignaLocal=true;
					}
				}
				
				//hora-minuto
				GregorianCalendar fechaCalendario = new GregorianCalendar();
				fechaCalendario.setTime(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());				
				int horaCalendario = fechaCalendario.get(Calendar.HOUR_OF_DAY);
				String hora="";
				if(horaCalendario <10){
					hora = "0"+horaCalendario;
				}else{
					hora = ""+horaCalendario;
				}
				int minutoCalendario = fechaCalendario.get(Calendar.MINUTE);
				String minuto="";
				if(minutoCalendario == 0){
					minuto="0"+ minutoCalendario;
				}else{
					minuto="" + minutoCalendario;
				}
				formulario.setHoras(hora);				
				formulario.setMinutos(minuto);
				formulario.setHorasMinutos(formulario.getHoras() + ":" + formulario.getMinutos());
					
				//entregas a domicilio
				if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
					colocarCiudadSector(request, session, formulario, entregaDetallePedidoDTO);
					if(ciudadGye.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil")) && 
							session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
						//solo si es la ciudad de guayaquil tenemos que mostrar el calendario de la bodega 97 y el check de transito debe ser obligatorio
						LocalID localID=new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito")));	
						EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());				
						if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
							obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
						}
					}//asigna valores al calendario
					else if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
						obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
					}
					
					//direcci\u00F3n
					//IOnofre. Se obtienen los valores del DTO para que se puedan editar en la JSP los campos de la direccion completa
					formulario.setReferencia(entregaDetallePedidoDTO.getEntregaPedidoDTO().getReferencia());
					//formulario.setDireccion(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
					formulario.setCallePrincipal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCallePrincipal());
					formulario.setNumeroCasa(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNumeroCasa());
					formulario.setCalleTransversal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalleTransversal());
					
					if(CollectionUtils.isNotEmpty(direccionesColTmp)){
						DireccionesDTO direccionesDTO = new DireccionesDTO();
						for(DireccionesDTO direccionDTO : direccionesColTmp){
							if(direccionDTO.getDescripcion().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega()) && direccionDTO.getFechaEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente())){
								direccionesDTO = direccionDTO;
							}
						}
						//asigna radio button KILOMETRO u HORA
						if((direccionesDTO.getHoras()!=null && direccionesDTO.getMinutos()!=null)){// &&(!direccionesDTO.getHoras().equals("00") && !direccionesDTO.getMinutos().equals("00"))
							if(!direccionesDTO.getHoras().equals("") && !direccionesDTO.getMinutos().equals("")){
								formulario.setUnidadTiempo("H");
								formulario.setDistanciaH(direccionesDTO.getHoras());
								formulario.setDistanciaM(direccionesDTO.getMinutos());
							}	
							else{
								formulario.setUnidadTiempo("K");
								formulario.setDistancia(direccionesDTO.getDistanciaDireccion());						
							}
						}else{
							formulario.setUnidadTiempo("K");
							formulario.setDistancia(direccionesDTO.getDistanciaDireccion());						
						}
					}
				}
				//si la entrega es a domicilio desde local
				if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
					LogSISPE.getLog().info("Editar Entrega con opcion de entrega domicilio desde local");
					//se cargan los datos si se edita una entrega con contexto 34 domicilio desde local
					if(SearchDTO.isLoaded(entregaDetallePedidoDTO.getEntregaPedidoDTO()) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega()!=null 
							&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().toString().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
						colocarCiudadSector(request, session, formulario, entregaDetallePedidoDTO);
					
						formulario.setDireccion(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
						//se busca y se cargan los datos del vendedor
						formulario.setCodigoVendedorSicmer(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoVendedor().toString());
						PersonaDTO vendedor=new PersonaDTO();
						vendedor.getId().setCodigoPersona(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoVendedor());
						Collection<PersonaDTO> colVendedores=SISPEFactory.getDataService().findObjects(vendedor);
						formulario.setNombreVendedorSicmer(colVendedores.iterator().next().getNombreCompleto());
						formulario.setNumeroDocumentoSicmer(colVendedores.iterator().next().getNumeroDocumento());
					
						formulario.setQuienRecibeSicmer(entregaDetallePedidoDTO.getEntregaPedidoDTO().getQuienRecibira());
						if(session.getAttribute(OPCIONSTOCK).toString().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().toString())){
							formulario.setFechaEntregaCliente(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().toString().substring(0, 10));
						}else{
							formulario.setFechaEntregaCliente(formulario.getBuscaFecha());
						}
						
						formulario.setHoraDesde(entregaDetallePedidoDTO.getEntregaPedidoDTO().getHorasEntrega().substring(0,5));
						formulario.setHoraHasta(entregaDetallePedidoDTO.getEntregaPedidoDTO().getHorasEntrega().substring(8,13));
						//se sube a sesion los datos del formulario cargado
						CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
						session.setAttribute(FORMULARIO_SICMER,formularioSicmer);
					}
				}

				//asigna valores de reserva	
				if(beanSession.getPaginaTabPopUp().getTabSeleccionado() == 1 && 
						(session.getAttribute(EntregaLocalCalendarioAction.OPCIONTIPOENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))
								|| session.getAttribute(EntregaLocalCalendarioAction.OPCIONTIPOENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))){
					Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
					String[] cantidades = new String[detallePedidoDTOCol.size()];
					int i = 0;
					for(DetallePedidoDTO detallePedido :  this.detallePedidoDTOColTmp){	
						if(detallePedido.getEntregaDetallePedidoCol().isEmpty()){
							cantidades[i] = "0";
							i++;
						}else{
							int noExiste = 0;
							for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){							
									for(DetallePedidoDTO detallePedidoDTO :  entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){
										//compara las entregas en las dos listas y asigna el valor de reserva
										if(detallePedido.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) 
												&& entregaDetallePedido.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) 
												&& entregaDetallePedido.getEntregaPedidoDTO().getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega())){
											cantidades[i] = entregaDetallePedido.getCantidadEntrega().toString();											
											i++;
										}else{										
											noExiste++;
										}
									}
									if(noExiste == entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido().size()*detallePedido.getEntregaDetallePedidoCol().size() ){
										cantidades[i] = "0";											
										i++;
									}
							}
						}
					}				
					i = 0;
					for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){	
						if(cantidades[i]!=null)
						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(cantidades[i]));
						i++;
					}
				}
			}			
		}
		
		//regresar a configuracion de entregas
		else if(request.getParameter("atrasPopUpEntregas")!=null){
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			
			String tabHabilitados = tabHabilitadosParciales(request);
			
			if(tabHabilitados == null){
				
				request.setAttribute(CONFIRMACIONANTERIORCONFENTREGAS, "ok");
				session.removeAttribute(HABILITARCANTIDADENTREGA);
				
			}else{
				
				if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){
					
					request.setAttribute(CONFIRMACIONANTERIORCONFENTREGAS, "ok");
					
				}else{
					regresarPopUp(session, beanSession,siguiente,"","entregas","atrasPopUpEntregas",0,"siguienterD","");
					session.removeAttribute(TABSELECCIONADONAVEGACION);
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
					session.removeAttribute(PASOSPOPUP);
					session.removeAttribute(HABILITARCANTIDADENTREGA);
					session.removeAttribute(CODIGOENTREGAPREVIO);
					regresarValoresIniciales(session);
					regresarvaloresInicialesExistenEntregas(session);
				}
				
			}
			session.removeAttribute( POSICIONDIVCONFENTREGAS);
		}
		
		//se selecciona el boton Ok del popUp de confirmacion de entregas
		else if(request.getParameter("seleccionaOK") != null){
			
			LogSISPE.getLog().info("va a configurar las entregas nuevamente");
			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
			session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
			session.removeAttribute(EXISTELUGARENTREGA);
			session.removeAttribute(EDITAFECHAMINIMA);
			//session.removeAttribute(CALENDARIODIALOCALCOL);
			session.removeAttribute(FECHAENTREGACLIENTE);
			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			String tabHabilitados = tabHabilitadosParciales(request);
			CalendarioLocalEntregaDomicilioUtil.eliminarVariablesCalendarioSICMER(session, formulario);
			if(tabHabilitados == null){
				anteriorConfiguracionEntregas(session, beanSession, siguiente, "", "entregas", "atrasPopUpEntregas", 0, "siguienterD", "", "ayuda.mensajePasoConfEnt1", "ayuda.paso1");
				session.removeAttribute(CODIGOENTREGAPREVIO);
			}
			else{
				if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){
					anteriorConfiguracionEntregas(session, beanSession, atras, siguiente,"atrasPopUpEntregas","entregas",1,"anteriorD","siguienterD", "ayuda.mensajePasoConfEnt2.1.1", "ayuda.paso2");
				}
			}
			session.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
			asignaLocal=false;
			
			
			if(formulario.getCalendarioDiaLocal()!=null && formulario.getCalendarioDiaLocal().length>0){
				for(Object dia:(Object[])formulario.getCalendarioDiaLocal()){
					CalendarioDiaLocalDTO diaCal=(CalendarioDiaLocalDTO) dia;
					if(diaCal.getNpEsSeleccionado()){
						diaCal.setNpEsSeleccionado(false);	
					}
				}
			}
			
//			//reestabelce el NpEsSeleccionado a su valor por defecto
//			Object[] calendarioDiaLocalDTOOBJ =	(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//			if(calendarioDiaLocalDTOOBJ!=null){
//				for(Object object : calendarioDiaLocalDTOOBJ){
//					CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)object;
//					if(calendarioDiaLocalDTO.getNpEsSeleccionado()){
//						calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.FALSE);
//					}
//				}
//				session.setAttribute(CALENDARIODIALOCALCOL, calendarioDiaLocalDTOOBJ);
//				formulario.setHorasMinutos(null);
//			}									
		}
		
		else if(request.getParameter("seleccionaCANCEL") != null){
			
			LogSISPE.getLog().info("cancela y acepta la configuracion de entregas");
			session.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
			//asignamos los valores por defecto cuando presionamos en cancelar para continuar con las validaciones
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			
		}
		
		else if(request.getParameter("replicarValores") != null){
			
			LogSISPE.getLog().info("empieza a replicar valores en todas las cajas de texto del pedido");
	    	Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
	    	
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
				
				try{
					if((detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega())>0){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(formulario.getValorReplica()));
					}
				}catch(NumberFormatException e){
					LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");
					errors.add("valorReplica",new ActionMessage("error.validacion.replica.cantidadEstado.invalido"));
					break;
				}
				
			}
			
		}
		
		//aceptar entrega desde el opUp seleccionar responsable de las canastas especiales
		else if(request.getParameter("seleccionarOpcion") != null){
			if(formulario.getOpElaCanEsp() != null){
				LogSISPE.getLog().info("Seleccion\u00F3 para la elaboraci\u00F3n--{}",formulario.getOpElaCanEsp());
				session.removeAttribute(SessionManagerSISPE.POPUP);
				aceptaConfiguracionEntrega(request, formulario, error, errors, warnings,info);
				formulario.setSeleccionCiudad("");
				if(errors.size()>0){
					LogSISPE.getLog().info("no se acpeto la configuracion");
					if(session.getAttribute(OPCIONLUGARENTREGA)!=null){
						//Si la configuraci\u00F3n no fue aceptada regreso a la fecha y configuracion anterior
						formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
						formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
						formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
						formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
						
						if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && (session.getAttribute(NOMBREENTIDADRESPONSABLE)).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
							//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
							formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
						}
					}
				}
			}
		}
		
		//Ver responsables
		else if(request.getParameter("verResponsable") != null){
			LogSISPE.getLog().info("--Entro a ver responsable");
			session.removeAttribute(ESTRUCTURA_RESPONSABLE);
			//se obtine el secuencial del indice que envia desde la jsp.
			String indiceEntrega = String.valueOf(request.getParameter("verResponsable"));
			
			Collection<EstructuraResponsable> detalleEstructuraCol = (ArrayList<EstructuraResponsable>)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
			EstructuraResponsable estructuraResponsable = (EstructuraResponsable)CollectionUtils.get(detalleEstructuraCol, Integer.valueOf(indiceEntrega)-1);
			session.setAttribute(EntregaLocalCalendarioAction.ESTRUCTURA_RESPONSABLE, estructuraResponsable);
			
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTituloVentana("Entidad responsable");
			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
			popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', evalScripts:true});ocultarModal();");
			popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
			popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
			popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarVentanaResponsable.jsp");
			popUp.setAncho(50D);
			popUp.setTope(40D);
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			popUp = null;
		}
		
		// ocultarVentanaResponsable
		else if(request.getParameter("ocultarVentanaResponsable") != null){
			session.removeAttribute(SessionManagerSISPE.POPUP);
			// Objetos para construir los tabs, si fue modificacion de reserva
			beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
			session.removeAttribute(MOSTRAR_POPUPTAB);
			session.removeAttribute(HABILITARCANTIDADENTREGA);
			session.removeAttribute(TABSELECCIONADONAVEGACION);
			regresarValoresIniciales(session);
			regresarvaloresInicialesExistenEntregas(session);
			session.removeAttribute(HABILITARCANTIDADRESERVA);
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
			session.removeAttribute(BLOQUEAROPCIONENTREGADOMICILIO);
			session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
			session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
			session.removeAttribute(PASOSPOPUP);
			session.removeAttribute( POSICIONDIVCONFENTREGAS);
			session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
			session.removeAttribute(EXISTELUGARENTREGA);
			session.removeAttribute(EDITAFECHAMINIMA);
			session.removeAttribute(CALENDARIODIALOCALCOL);
			session.removeAttribute(FECHAENTREGACLIENTE);
			session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
			session.removeAttribute(EntregaLocalCalendarioAction.CODIGOENTREGAPREVIO);
			CalendarioLocalEntregaDomicilioUtil.eliminarVariablesCalendarioSICMER(session, formulario);			
			if(formulario.getCalendarioDiaLocal()!=null && formulario.getCalendarioDiaLocal().length>0){
				for(Object dia:(Object[])formulario.getCalendarioDiaLocal()){
					CalendarioDiaLocalDTO diaCal=(CalendarioDiaLocalDTO) dia;
					if(diaCal.getNpEsSeleccionado()){
						diaCal.setNpEsSeleccionado(false);	
					}
				}
			}
			//Si la opci\u00F3n es editar entrega, recupera los datos de las entregas
			if(editarEntrega){				
				session.setAttribute(DETALLELPEDIDOAUX,this.detallePedidoDTOColTmp);
				session.setAttribute(ENTREGAS_RESPONSABLES,this.entregasTmp);
				session.setAttribute(DIRECCIONESAUX,this.direccionesColTmp);
								
				if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : this.entregasTmp){					
						//calendario seleccionado
						if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null && entregaDetallePedidoDTO.getNpCantidadBultos()>0){
							try{
								CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO();
								calendarioHoraLocalDTOCol = (Set<CalendarioHoraLocalDTO>)calendarioDiaLocalDTO.getCalendarioHoraLocalCol();
								ArrayList<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux =  new ArrayList<CalendarioHoraLocalDTO>(calendarioHoraLocalDTOCol);
								calendarioHoraLocalSeleccionado = calendarioHoraLocalDTOColAux.get(0);		
								formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));	
							}catch (Exception e) {
								LogSISPE.getLog().info("Error al obtener las entregas:"+e);
							}
						}
						if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
							//obtiene la lista de ciudades
							ciudad = "";	
							ciudadGye = "";
							if(session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)!=null){	
								for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO : (Collection<VistaEstablecimientoCiudadLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)){						
									for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocal : (Collection<VistaEstablecimientoCiudadLocalDTO>)vistaEstablecimientoCiudadLocalDTO.getVistaLocales()){
										String codigoVistaCiudad = "OTR" + vistaEstablecimientoCiudadLocal.getNombreCiudad() + "/" + vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();							
										if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada()!=null){
											if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada().equals("1")){
												codigoVistaCiudad = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
											}
										}
										if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol())){
											ciudad = codigoVistaCiudad;
											if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"))){
												ciudadGye = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
											}	
										}						
									}
								}
							}				
							//asigna la ciudad
							formulario.setSeleccionCiudad(ciudad);			
							session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol());				
							//asigna la zona
							formulario.setSelecionCiudadZonaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega());
						}
						
						formulario.setSeleccionCiudad(ciudad);
						request.getSession().setAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL,calendarioHoraLocalDTOCol);					
						request.getSession().setAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO,calendarioHoraLocalSeleccionado);
						
						CalendarioHoraLocalDTO calendarioHoraLocalDTO = (CalendarioHoraLocalDTO)request.getSession().getAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO);				
						
						if(calendarioHoraLocalDTO!=null ){					
							EntregaLocalCalendarioUtil.descontarEntregasPorHora(request, calendarioHoraLocalDTO, 0,  (LocalID)session.getAttribute(LOCALID), formulario, errors, warnings);													
							session.removeAttribute("ec.com.smx.sic.sipse.totalBultos");
						}
						//formulario.setCalendarioDiaLocal((Object[])session.getAttribute(CALENDARIODIALOCALCOL));//this.calendarioDiaLocalDTOOBJTmp
					}
				}				
			}
			
			//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
			if(CollectionUtils.isNotEmpty(detallePedidoDTOColTmp)){ //solo si la coleccion no es vacia se asigna los nuevos responsables
				EntregaLocalCalendarioUtil.procesarResponsablesEntrega(this.detallePedidoDTOColTmp, request);
			}
			
			/*if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
					formulario.setSeleccionCiudad(ciudad);
					request.getSession().setAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL,calendarioHoraLocalDTOCol);					
					request.getSession().setAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO,calendarioHoraLocalSeleccionado);					
					CalendarioHoraLocalDTO calendarioHoraLocalDTO = (CalendarioHoraLocalDTO)request.getSession().getAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO);				
					
					if(calendarioHoraLocalDTO!=null ){					
						EntregaLocalCalendarioUtil.descontarBultosPorHora(request, calendarioHoraLocalDTO, 0,  (LocalID)session.getAttribute(LOCALID), formulario, errors, warnings);													
						session.removeAttribute("ec.com.smx.sic.sipse.totalBultos");
					}
					//formulario.setCalendarioDiaLocal((Object[])session.getAttribute(CALENDARIODIALOCALCOL));//this.calendarioDiaLocalDTOOBJTmp
			}*/				
		
			//session.setAttribute("otraCuidad", "ok");
			editarEntrega = false;
			asignaLocal=false;
			session.setAttribute(EDITAR_ENTREGA, false);
		}
		
		else if(request.getParameter("ocultarOpcion") != null){
			session.removeAttribute(SessionManagerSISPE.POPUP);
		}
		
		//Boton aceptar en el ingreso de datos de la entrega
		else if(request.getParameter("botonAceptarDatos")!=null){
			
			/***********************************************************************************
			 ***********************ACEPTAR PARAMETROS PARA LA ENTREGA**************************
			 ***********************************************************************************/
			
			LogSISPE.getLog().info("entra a botonAceptarDatos");
			formulario.mantenerValoresEntregas(request);
			//Hago las validaciones de formulario de valores nulos y formatos
			if(formulario.validarBusqueda(error, request)==0){
				if(formulario.getBuscaFecha()==null){
					formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
				}
				
				//verifica si ingresaron una fecha menor a la fecha minima de entrega o existe una autorizacion
				if(session.getAttribute(EDITAFECHAMINIMA)!=null && 
						(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0.0 
								|| AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))!=null)){
					//Diferencia entre fecha de entrega y fecha minima de entrega
					long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
					if(diferenciaEntregaBusca < 0.0){
						LogSISPE.getLog().info("error en diferencia");
						errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",formulario.getBuscaFecha()));
					}
					else{
						boolean loc=true;//variable que se usa para reconocer cuando se ha ingresado un local valido
						VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
						LogSISPE.getLog().info("*****local destino***** {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
						//Si esta activo el combo para seleccionar los locales 
						if(session.getAttribute(SELECCIONARLOCAL)!=null ){
							String local=null;
							//si fue ingresado un local desde la caja de textos
							if(formulario.getLocal()!=null && !formulario.getLocal().equals("")){
								LogSISPE.getLog().info("ingreso un local");
								local=formulario.getLocal();
								formulario.setListaLocales(null);
							}
							//si fue seleccionado un local desde el combo
							else{
								LogSISPE.getLog().info("local seleccionado: {}" , formulario.getListaLocales());
								local=formulario.getListaLocales();
							}							
							//si el local seleccionado o ingresado es distinto al local de destino
							if(errors.size()==0 && !local.equals(vistaLocalDestinoDTO.getId().getCodigoLocal().toString())){
								//cargo el local destino
								loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,local);
								LogSISPE.getLog().info("encontro al local: {}" , loc);
								
								//AYUDA
								if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) 
										|| (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) 
										&& (String)session.getAttribute(PASOSPOPUP)==null ){
									
									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
								}
								else{
									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
								}
								if((String)session.getAttribute(PASOSPOPUP)!=null){
									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
								}
							}
						}
						//Si no se va a seleccionar un local por defecto el local destino siempre es el local de origen
						session.removeAttribute(DIASELECCIONADO);
						session.removeAttribute(CALENDARIODIALOCAL);
						session.removeAttribute(DIRECCION);
						formulario.setListaLocales(null);
						//Si el local fue encontrado
						if(loc){
							//Obtengo el local destino
							vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
							LogSISPE.getLog().info("***LOCAL DESTINO***: {}" ,vistaLocalDestinoDTO.getId().getCodigoLocal());
							//En los casos donde deba haber una fecha de despacho se obtiene el calendario
							if(session.getAttribute(SELECCIONARCALENDARIO)!=null || (formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_MI_LOCAL))){
								LogSISPE.getLog().info("va a desplegar el calendario");
								LocalID localID=new LocalID();
								localID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
								localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
								//Cargo el calendario
								obtenerLocal(session, request, localID, errors, formulario);
							}
							/*else if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
								LogSISPE.getLog().info("va a habilitar las direcciones");
								session.setAttribute(DIASELECCIONADO, "ok");
							}*/
							
							session.removeAttribute(CONFIGURACIONCARGADA);
							
						}else{ //Si el local ingresado no existe
							errors.add("errorLocal",new ActionMessage("errors.local"));
						}
					}
				}
				//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
				else{
					//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
					errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
					session.removeAttribute(VISTALOCALCOL);
				}
			}
		}
		
		//Guardar cambios en las entregas
		else if(request.getParameter("botonGuardarCambios")!=null && !request.getParameter("botonGuardarCambios").equals("")){
			
			/******************************************************************************
			 ****************************GUARDAR ENTREGAS***********************************
			 ******************************************************************************/
			
			LogSISPE.getLog().info("entra a guardar entregas");
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			}
			
			//check nuevamente si tiene o no pago en efectivo
			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
				formulario.setCheckPagoEfectivo(estadoActivo);
			}
			
			//OANDINO: Se verifica el registro correspondiente al checkbox seleccionado del listado de entregas
			//Se crea nueva instancia de colecci\u00F3n para almacenar colecciones del tipo DetallePedidoDTO
			List<DetallePedidoDTO> detallePedidoDTOColEntregas = (List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
			
			//Se crea nueva instancia de colecci\u00F3n para almacenar colecciones del tipo EntregaPedidoDTO
			List<EntregaDetallePedidoDTO> entregasDetalleCol = null;
			
			//Se crea una instancia del tipo EntregaPedidoDTO
			EntregaDetallePedidoDTO entregaDetalle = null;
			
			for(DetallePedidoDTO detalle : detallePedidoDTOColEntregas) {
				for(Iterator<EntregaDetallePedidoDTO> iteradorEnt = detalle.getEntregaDetallePedidoCol().iterator();iteradorEnt.hasNext();) {
					EntregaDetallePedidoDTO entregaDTOTrans = iteradorEnt.next();
//					entregaDTOTrans.setCodigoLocalTransito(null);
					entregaDTOTrans.getEntregaPedidoDTO().setCodigoBodega(null);
				}
			}
			
			//Se recorre el arreglo creado en base a la selecci\u00F3n m\u00FAltiple de checkboxes
			if(formulario.getCheckTransitoArray() != null && formulario.getCheckTransitoArray().length > 0){
				
				for(int i=0; i<formulario.getCheckTransitoArray().length; i++){
					LogSISPE.getLog().info("Value CheckTransito: {}", formulario.getCheckTransitoArray()[i]);
					//Se obtiene el registro del checkbox seleccionado
					String[] valorCheck = formulario.getCheckTransitoArray()[i].split("-");
					
					//Obtengo el registro de detalle
					int numRegistro=(new Integer(valorCheck[1])).intValue();
					
					//Obtengo el registro de entrega del detalle
					int numEntrega=(new Integer(valorCheck[2])).intValue();
					
					//Se obtiene el par\u00E1metro VALUE del chekcbox
					String valorParametro = valorCheck[0];
					
					LogSISPE.getLog().info("Reg. Detalle[{}]: {}",i,numRegistro);
					LogSISPE.getLog().info("Reg. Entrega[{}]: {}",numEntrega,numEntrega);
					
					//Se obtiene el listado original de registros de entregas presentado en pantalla desde sesi\u00F3n
					detallePedidoDTOColEntregas=(List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
														
					// Se obtiene el DTO espec\u00EDfico correspondiente al registro obtenido de entre toda la colecci\u00F3n original
					DetallePedidoDTO detallePedidoDTO = detallePedidoDTOColEntregas.get(numRegistro);
					
					LogSISPE.getLog().info("Art\u00EDculo[{}]: {}",i,detallePedidoDTO.getId().getCodigoArticulo());
													
					//Se obtiene la colecci\u00F3n de entregas por cada detalle
					entregasDetalleCol = (List<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol();
					
					//Obtengo el registro espec\u00EDfico del total de entregas por detalle
					entregaDetalle = entregasDetalleCol.get(numEntrega);
					
					//Se setean los valores para el c\u00F3digo del local de tr\u00E1nsito
					entregaDetalle.getEntregaPedidoDTO().setCodigoBodega(valorParametro);
				}
			}
			
			//se guarda el valor de los checks de transito
			session.setAttribute(EntregaLocalCalendarioUtil.CHECKS_ENTREGAS_TRANSITO, formulario.getCheckTransitoArray());
			
			//---------------------------------------------------------------------------------------------------------------------------------
			//guardo el detalle del pedido
			Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoDTOCol);
			
			//si existen cambios en la configuraci\u00F3n de las entregas
			if(session.getAttribute(EXISTENCAMBIOS)!=null){
				
				LogSISPE.getLog().info("Opci\u00F3n: EXISTEN CAMBIOS...");
				
				//guardo las direcciones
				Collection<DireccionesDTO> direccionesCol = (Collection<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
				session.setAttribute(DIRECCIONES, direccionesCol);
				//guardo los costos
				Collection<CostoEntregasDTO> costosCol = (Collection<CostoEntregasDTO>)session.getAttribute(COSTOENTREGAAUX);
				session.setAttribute(COSTOENTREGA, costosCol);
				//guardo los cambios en el calendario
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);								
				
				// Se mantienen en sesison los datos a ser guardados en kardex
				session.setAttribute(CALENDARIOCONFIGURACIONDIALOCAL, calendarioConfiguracionDiaLocal);
				
				//guarda la fecha de entrega
				formulario.setFechaEntrega((String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA));
				
				if(session.getAttribute(SECDIRECCIONESAUX)!=null){
					Integer cont=(Integer)session.getAttribute(SECDIRECCIONESAUX);
					session.setAttribute(SECDIRECCIONES, cont);
				}
				
				session.removeAttribute(DETALLELPEDIDOAUX);
				session.removeAttribute(DIRECCIONESAUX);
				session.removeAttribute(COSTOENTREGAAUX);
				session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
				session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
				session.removeAttribute(VALORTOTALENTREGAAUX);
				session.removeAttribute(STOCKENTREGAAUX);
				session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
				
				if(session.getAttribute(ENTIDADRESPONSABLELOCAL)==null){
					session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
					//session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
				}else {
					session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
				}
				
				//se actualiza el costo total del pedido
				formulario.actualizarCostoTotalPedido(request);
				messages.add("guaCambiosEntregas",new ActionMessage("exito.guardarCambios.entregas"));
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				forward="cotizar";
			}else{
				
				LogSISPE.getLog().info("Opci\u00F3n: NO EXISTEN CAMBIOS...");
				session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
				formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
				//se actualiza el costo total del pedido
				
				//se actualiza los valores de la variables auxiliares de session COSTOENTREGA y DIRECCIONES
				//guardo las direcciones
				if(session.getAttribute(ENTREGAS_ELIMINO_DIRECCION) != null){
					Collection<DireccionesDTO> direccionesCol = (Collection<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
					session.setAttribute(DIRECCIONES, direccionesCol);
				}
				
				Collection<CostoEntregasDTO> costosCol = (Collection<CostoEntregasDTO>)session.getAttribute(COSTOENTREGAAUX);
				session.setAttribute(COSTOENTREGA, costosCol);
				
				formulario.actualizarCostoTotalPedido(request);
				session.removeAttribute(STOCKENTREGAAUX);
				info.add("sinCambiosEntregas",new ActionMessage("info.sinCambios.entregas"));
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				forward="cotizar";
			}
			
			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
			
			//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
			CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
			
			//si esta en el tab de contactos se cambia al tab de pedidos				
			if(beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
				ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);		
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
				//se guarda el beanSession
				SessionManagerSISPE.setBeanSession(beanSession, request);
			}
			EntregaLocalCalendarioUtil.entidadResponsablePedido(session);
			//Se contruyen los tabs de Contacto y Pedidos
			PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
			beanSession.setPaginaTab(tabsCotizaciones);
			
		}
		
		//Cancelar entrega (abre la ventana de pregunta si desea guardar los cambios)
		else if(request.getParameter("botonCerrarEntregas")!=null && !request.getParameter("botonCerrarEntregas").equals("")){
			LogSISPE.getLog().info("entra a cancelar la entrega");
			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			}
			
			//check nuevamente si tiene o no pago en efectivo
			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
				formulario.setCheckPagoEfectivo(estadoActivo);
			}
			session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
			if(session.getAttribute(EXISTENCAMBIOS)!=null){
				LogSISPE.getLog().info("si existieron cambios");
				request.setAttribute("ventanaCerrar", "ok");
			}
			else{
				session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
				formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
				//se actualiza el costo total del pedido
				formulario.actualizarCostoTotalPedido(request);
				//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
				CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
				session.removeAttribute(STOCKENTREGAAUX);
				session.removeAttribute(CLASIFICACIONES_PERECIBLES);
				session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
				session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
				session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
				session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
				session.removeAttribute(ENTREGAS_ELIMINO_DIRECCION);
				//Se contruyen los tabs de Contacto y Pedidos
				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
				beanSession.setPaginaTab(tabsCotizaciones);
				info.add("sinCambiosEntregas",new ActionMessage("info.sinCambios.entregas"));
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				forward="cotizar";
			}
			session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
		}
		
		//Salir sin guardar los cambios
		else if(request.getParameter("regresar")!=null && !request.getParameter("regresar").equals("")){
			//Regresar a la cotizacion
			LogSISPE.getLog().info("entro a regresar");
			session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
			formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
			//se actualiza el costo total del pedido
			formulario.actualizarCostoTotalPedido(request);
			session.removeAttribute(DETALLELPEDIDOAUX);
			session.removeAttribute(DIRECCIONESAUX);
			session.removeAttribute(COSTOENTREGAAUX);
			session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
			session.removeAttribute(VALORTOTALENTREGAAUX);
			session.removeAttribute(STOCKENTREGAAUX);
			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_PROCESADO);
			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
			session.removeAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL);
			session.removeAttribute(ENTREGAS_PEDIDO);
			//check nuevamente si tiene o no pago en efectivo
			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
				formulario.setCheckPagoEfectivo(estadoActivo);
			}
			
			//prueba
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			for(DetallePedidoDTO detallePedido: detallePedidoDTOCol){
				
				if(!CollectionUtils.isEmpty(detallePedido.getEntregaDetallePedidoCol())){
					LogSISPE.getLog().info("------numero de entregas----- {}" , detallePedido.getEntregaDetallePedidoCol().size());
				}
				
//				if(detallePedido.getEntregas()!=null && detallePedido.getEntregas().size()>0)
//					LogSISPE.getLog().info("------numero de entregas----- {}" , detallePedido.getEntregas().size());
			}
			
			//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
			CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
			//Se contruyen los tabs de Contacto y Pedidos
			PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
			beanSession.setPaginaTab(tabsCotizaciones);
			info.add("sinCambiosEntregas",new ActionMessage("info.sinCambios.entregas"));
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			forward="cotizar";
		}
		
		/***********************************************************************************
		 *********************************CALENDARIO********************************
		 ***********************************************************************************/
		//Selecciona un dia del mes
		else if(request.getParameter("seleccionCal")!=null){
			LogSISPE.getLog().info("entra a seleccionar un dia del calendario");
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
			formulario.mantenerValoresEntregas(request);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			if(formulario.validarFechaEntrega(error, request)==0){
				//Entro a seleccionar un calendario
				EntregaLocalCalendarioUtil.seleccionDia(formulario,new Integer(request.getParameter("seleccionCal")).intValue(),session,errors,request);
				formulario.setDirecciones(null);
				//no se resetea la direccion si la entrega es a domicilio desde local
				if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)==null){
					formulario.setDireccion(null);
				}
				formulario.setDistancia(null);
				formulario.setDistanciaH(null);
				formulario.setDistanciaM(null);
				session.removeAttribute(DIRECCION);
				session.removeAttribute("otraCuidad");
				session.setAttribute( POSICIONDIVCONFENTREGAS ,"ok");
			}
		}
		
		/***************************************NAVEGACION ENTRE MESES*************************************/
		else if(request.getParameter("mesAnterior")!=null){
			LogSISPE.getLog().info("mes anterior");
			formulario.mantenerValoresEntregas(request);
			Date mes=(Date)session.getAttribute(MESSELECCIONADO);
			//resto un mes al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			fechaCalendario.add(Calendar.MONTH,-1);
			mes=fechaCalendario.getTime();
			//fecha minima
			Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
			/*****************************************************************************************/
			//fecha maxima
			Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
			EntregaLocalCalendarioUtil.obtenerCalendario(request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
		}
		
		else if(request.getParameter("mesSiguiente")!=null){
			LogSISPE.getLog().info("mes siguiente");
			formulario.mantenerValoresEntregas(request);
			Date mes=(Date)session.getAttribute(MESSELECCIONADO);
			//sumo un mes al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			fechaCalendario.add(Calendar.MONTH,1);
			mes=fechaCalendario.getTime();
			//fecha minima
			Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
			/*****************************************************************************************/
			//fecha maxima
			Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
			/*****************************************************************************************/
			EntregaLocalCalendarioUtil.obtenerCalendario(request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
		}
		//se regresa una semana en el calendario y se cargan los bultos disponibles
		else if(request.getParameter("semanaAnterior")!=null){
			LogSISPE.getLog().info("semana anterior");
			formulario.mantenerValoresEntregas(request);
			
			Date fechaEntCliente = (Date)session.getAttribute(FECHABUSQUEDA); 
			
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fechaEntCliente);
			
			calendario.add(Calendar.DAY_OF_WEEK, - calendario.get(Calendar.DAY_OF_WEEK)+1);
			fechaEntCliente = calendario.getTime();
			
			//Date mes=(Date)session.getAttribute(MESSELECCIONADO);
			Date mes = (Date)session.getAttribute(FECHAMINIMA);
			//resto una semana al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			
			GregorianCalendar fechaCalendarioAux=new GregorianCalendar();
			fechaCalendarioAux.setTime(mes);
			fechaCalendarioAux.add(Calendar.WEEK_OF_YEAR,-1);
						
			if(fechaEntCliente.before(fechaCalendarioAux.getTime()) || fechaEntCliente.equals(fechaCalendarioAux.getTime())){
				fechaCalendario.add(Calendar.WEEK_OF_YEAR,-1);
				mes=fechaCalendario.getTime();
				//fecha minima
				//Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
				Date fechaMinima = mes;

				//fecha maxima
				Date fechaMaxima=(Date)session.getAttribute(FECHAMINIMA);
				obtenerCalendarioPorSemana(request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
			}else{
				warnings.add("noPuedeRegresar", new ActionMessage("warning.no.puede.regresar.semana"));
			}
		}
		
		else if(request.getParameter("semanaSiguiente")!=null){
			LogSISPE.getLog().info("semana siguiente");
			formulario.mantenerValoresEntregas(request);
			//Date mes=(Date)session.getAttribute(MESSELECCIONADO);
			Date mes = (Date)session.getAttribute(FECHAMAXIMA);
			//sumo una semana al mes actual
			GregorianCalendar fechaCalendario=new GregorianCalendar();
			fechaCalendario.setTime(mes);
			fechaCalendario.add(Calendar.DAY_OF_WEEK,-1);
			fechaCalendario.add(Calendar.WEEK_OF_YEAR,1);
			mes=fechaCalendario.getTime();
			//fecha minima
			//Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
			Date fechaMinima = (Date)session.getAttribute(FECHAMAXIMA);

			//fecha maxima
			//Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
			Date fechaMaxima = mes;
			obtenerCalendarioPorSemana(request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
		}
		
		else if(request.getParameter("verificarDisponibilidadCamionesHora") != null){
			
			formulario.validarCamposRequeridosEntregasDomicilioDesdeCD(request, errors);
			
			if(session.getAttribute(EXISTELUGARENTREGA) != null && errors.size() == 0){
				LogSISPE.getLog().info("verificarDisponibilidadCamionesHora");
				session.setAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO,(String)request.getParameter("verificarDisponibilidadCamionesHora"));
				
				Object[] calendarioDiaLocalDTOOBJ =	(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
				if(calendarioDiaLocalDTOOBJ != null && calendarioDiaLocalDTOOBJ.length > 0){
					int indiceDiaLocal= new Integer(request.getParameter("verificarDisponibilidadCamionesHora").split("-")[0]);
					int indiceHoraLocal = new Integer(request.getParameter("verificarDisponibilidadCamionesHora").split("-")[1]);
					session.setAttribute(DIASELECCIONADOAUX, indiceDiaLocal);
					
					CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indiceDiaLocal];
					Set<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol = (HashSet<CalendarioHoraLocalDTO>)calendarioDiaLocalDTO.getCalendarioHoraLocalCol();
					ArrayList<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux =  new ArrayList<CalendarioHoraLocalDTO>(calendarioHoraLocalDTOCol);
					CalendarioHoraLocalDTO calendarioHoraLocalSeleccionado = calendarioHoraLocalDTOColAux.get(indiceHoraLocal);
					
					//se setean los datos del formulario asociados a la fecha y hora seleccionda
					formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
					String[] horaSeleccionada = calendarioHoraLocalSeleccionado.getId().getHora().toString().split(":");
					formulario.setHoras(horaSeleccionada[0]);
					formulario.setMinutos(horaSeleccionada[1]);
					formulario.setSegundos(horaSeleccionada[2]);
					formulario.setHorasMinutos(formulario.getHoras()+":"+formulario.getMinutos());
					session.setAttribute(HORA_SELECCIONADA, horaSeleccionada);
					
					//realiza los procesos normales que hacia antes
					formulario.mantenerValoresEntregas(request);
					EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
					if(formulario.validarFechaEntrega(error, request)==0){
						EntregaLocalCalendarioUtil.seleccionDia(formulario, indiceDiaLocal, session, errors, request);
						
						EntregaLocalCalendarioUtil.verificarDisponibilidadEntregasHora(request, calendarioHoraLocalSeleccionado, formulario, errors, warnings, info);
						//si hay errores o advertencias no se pinta el dia seleccionado
						if(errors.size() > 0 || warnings.size() > 0){
							session.removeAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO);
							formulario.setHoras(null);
							formulario.setMinutos(null);
							formulario.setHorasMinutos(null);
							request.getSession().removeAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO);
							request.getSession().removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX);
							request.getSession().removeAttribute(EntregaLocalCalendarioAction.HORA_SELECCIONADA);
						}else{
							session.setAttribute(CALENDARIODIALOCALCOL, calendarioDiaLocalDTOOBJ);
						}
					}
				}
			}
		}
		
		/***********************************************************************************
		 *********************************DIRECCIONES***************************************
		 ***********************************************************************************/

		else if(request.getParameter("seleccionaDir")!=null){
			formulario.mantenerValoresEntregas(request);
			//Si la entrega es a domicilio
			if(session.getAttribute(LUGARENTREGA)!=null && session.getAttribute(LUGARENTREGA).equals(CONTEXTO_ENTREGA_DOMICIO)){
				LogSISPE.getLog().info("entra a asignar una direccion existente: {}" , formulario.getDirecciones().length);
				boolean seleccionado=false;//reconoce si la direccion fue o no seleccionada
				//Recupero la lista de direcciones
				List<DireccionesDTO> direccionesDTOCol = (List<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
				//funcion para solo chequear una direccion
				String[] seleccionados = new String[direccionesDTOCol.size()];
				LogSISPE.getLog().info("tamano dir1: {}" ,seleccionados.length);
				for(int i = 0; i<seleccionados.length; i++){
					if(i != Integer.parseInt(request.getParameter("seleccionaDir"))){
						LogSISPE.getLog().info("i1:{}" ,i);
						seleccionados[i]=null;
					}else{
						LogSISPE.getLog().info("i2:{}" ,i);
						seleccionados[i]=Integer.valueOf(i).toString();
						seleccionado = true;
						if(formulario.getDirecciones().length==0){
							seleccionado = false;
							seleccionados[i] = null;
						}
					}
				}
				
				formulario.setDirecciones(seleccionados);
				LogSISPE.getLog().info("tamano dir2: {}",seleccionados.length);
				if(seleccionado){
					//obtengo el local seleccionado
					VistaLocalDTO vistaLocalDTO = (VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
					DireccionesDTO direccionesDTO = direccionesDTOCol.get(Integer.parseInt(request.getParameter("seleccionaDir")));
					session.setAttribute(DIRECCION, direccionesDTO);
					LogSISPE.getLog().info("codigo local: {}", vistaLocalDTO.getId().getCodigoLocal());
					LogSISPE.getLog().info("codigo dirSec: {}", direccionesDTO.getCodigoSector());
					//Pregunto si la direccion pertence al sector del local seleccionado
					if(vistaLocalDTO.getId().getCodigoLocal().toString().equals(direccionesDTO.getCodigoSector())){
						formulario.setDireccion(direccionesDTO.getDescripcion());
						LogSISPE.getLog().info("minutos: {}", direccionesDTO.getMinutos());
						if((direccionesDTO.getHoras()!=null && !direccionesDTO.getHoras().equals("")) || (direccionesDTO.getMinutos()!=null && !direccionesDTO.getMinutos().equals(""))){
							LogSISPE.getLog().info("llena distancia en horas");
							if(direccionesDTO.getHoras()!=null && !direccionesDTO.getHoras().equals("0"))
								formulario.setDistanciaH(direccionesDTO.getHoras());
							formulario.setDistanciaM(direccionesDTO.getMinutos());
							formulario.setUnidadTiempo("H");
							formulario.setDistancia(null);
						}else{
							LogSISPE.getLog().info("llena distancia en kilometros");
							formulario.setDistancia(direccionesDTO.getDistanciaDireccion());
							formulario.setUnidadTiempo("K");
							formulario.setDistanciaH(null);
							formulario.setDistanciaM(null);
						}
					}else{
						formulario.setDirecciones(null);
						formulario.setUnidadTiempo(null);
						formulario.setDistancia(null);
						formulario.setDistanciaH(null);
						formulario.setDistanciaM(null);
						formulario.setDireccion(null);
						session.removeAttribute(DIRECCION);
						errors.add("seleccionDireccion",new ActionMessage("errors.seleccionDireccionL",vistaLocalDTO.getNombreLocal()));
					}
				}else{
					formulario.setDirecciones(null);
					formulario.setUnidadTiempo(null);
					formulario.setDistancia(null);
					formulario.setDistanciaH(null);
					formulario.setDistanciaM(null);
					formulario.setDireccion(null);
					session.removeAttribute(DIRECCION);
				}
			}else{ //Si la entrega es a locales
				formulario.setDirecciones(null);
			}
		}
		
		/***********************************************************************************
		 *********************************RESERVACION***************************************
		 ***********************************************************************************/
		//Actualizar los detalles de las entregas
		else if(request.getParameter("botonActualizarEntrega")!=null){
			//TODO FALTA IMPLEMENTAR FUNCIONALIDAD
			LogSISPE.getLog().info("//TODO FALTA IMPLEMENTAR FUNCIONALIDAD");
		}
		
		//Agrega los detalles de las entregas
		else if(request.getParameter("botonAceptarEntrega")!=null){
			LogSISPE.getLog().info("Presiono el boton aceptar entregas");	
			//Validar si ya existe una entrega configurada en la misma fecha y hora
			Boolean existeEntrega = false;
//			Collection<DetallePedidoDTO> detallePedidoDTOCols=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//			if(!CollectionUtils.isEmpty(detallePedidoDTOCols)){
//				for(DetallePedidoDTO detPed:detallePedidoDTOCols){
//					if(!CollectionUtils.isEmpty(detPed.getEntregaDetallePedidoCol())){
//						Collection<EntregaDetallePedidoDTO> entregas=(Collection<EntregaDetallePedidoDTO>)detPed.getEntregaDetallePedidoCol();								
//						for(EntregaDetallePedidoDTO entDetPed:entregas){								
//							Date fecha= new Date(WebSISPEUtil.construirFechaCompleta(formulario.getFechaEntregaCliente(), 0, 0, Integer.parseInt(formulario.getHoras()), Integer.parseInt(formulario.getMinutos()), Integer.parseInt(formulario.getSegundos()), 0));															
//							if(entDetPed.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()==(fecha.getTime())){								
//								existeEntrega = true;
//								errors.add("errorFechas",new ActionMessage("errors.coincidenFechas",entDetPed.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega(),formulario.getFechaEntregaCliente(),detPed.getArticuloDTO().getDescripcionArticulo()));
//							}
//						}							
//					}
//							
//				}
//			}
					
			if(!existeEntrega){
				String[] horaSelecionada = (String[])session.getAttribute(HORA_SELECCIONADA);
				if(horaSelecionada != null && horaSelecionada.length > 0){
					formulario.setHoras(horaSelecionada[0]);
					formulario.setMinutos(horaSelecionada[1]);
					formulario.setSegundos(horaSelecionada[2]);
				}
				
				String valorCodigoCiudadSector =null;
				if(formulario.getSelecionCiudadZonaEntrega() != null && !formulario.getSelecionCiudadZonaEntrega().equals("")){
					valorCodigoCiudadSector = formulario.getSelecionCiudadZonaEntrega();
				}
					
				EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
				
				if(formulario.getOpLugarEntrega() !=null && formulario.getOpTipoEntrega() != null && formulario.getOpStock() !=null){
					
					LogSISPE.getLog().info("Entro a agregar entregas");
					
					String validarCheckTransito = "";
					String valorCodigoTransito = "";
											
					String fechaMinimaEntrega = null;
					String fechaEntregaCliente = null;
					
					if(session.getAttribute(FECHABUSQUEDA)!=null && formulario.getBuscaFecha() == null){
						formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
						formulario.setFechaEntregaCliente(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
					}
					
					Long sumaCantSolCD = session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE)==null ? 0L:(Long)session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
					LogSISPE.getLog().info("sumaCantSolCD: {}",sumaCantSolCD.longValue());
					
					if(formulario.getOpStock()!=null && formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL)){
						//si no se solicit\u00F3 mercader\u00EDa al CD
						//se toman las fechas directamente del formulario, porque no hay un calendario de por medio
						fechaMinimaEntrega = formulario.getBuscaFecha();
						fechaEntregaCliente = formulario.getFechaEntregaCliente();
					}else{
						//se recuperan las fechas de sesi\u00F3n, porque hay un calendario de por medio y si las fechas son cambiadas primero se debe hacer click en el bot\u00F3n ACEPTAR
						//para que cambie la configuraci\u00F3n del calendario
						fechaMinimaEntrega = ConverterUtil.parseDateToString((Date)session.getAttribute(FECHABUSQUEDA));
						fechaEntregaCliente = (String)session.getAttribute(FECHAENTREGACLIENTE);
						
						if((String)session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && (String)session.getAttribute(LUGARENTREGA) !=null && session.getAttribute(LUGARENTREGA).toString().equals(CONTEXTO_ENTREGA_DOMICIO)){
							//este es un caso especial donde no hay calendario
							fechaMinimaEntrega = formulario.getBuscaFecha();
							fechaEntregaCliente = formulario.getFechaEntregaCliente();
						}
						
						//jmena aqui le subo a session la fecha de entrega al cliente
						session.setAttribute(FECHAENTREGACLIENTE, fechaEntregaCliente);
						
						//OANDINO: Siempre y cuando se haya solicitado a CD se procede a validar el valor del par\u00E1metro de tr\u00E1nsito
						//para mostrar o no checkbox seg\u00FAn el c\u00F3digo de local y ciudad
						
						// Se declara variable para almacenar el codigo de la ciudad
						String valorCodigoCiudad = "";
						//Se verifica si se ha seleccionado entrega a domicilio y la entidad responsable es la bodega
						if(formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
							LogSISPE.getLog().info("Se ha elegido realizar una entrega a domicilio");
							//Se obtiene el valor del combo de selecci\u00F3n de cuidades a comparar con VALORPARAMETRO
							valorCodigoCiudad = (String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO);
						}else{
							// Se obtiene desde session el local de entrega
							VistaLocalDTO vistaLocalEntregaDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
							LogSISPE.getLog().info("Local destino para entregas: {}", vistaLocalEntregaDTO.getId().getCodigoLocal());
							
							// Se obtiene el c\u00F3digo de la ciudad del local desde BD
							LocalID localID = new LocalID();
							localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
							localID.setCodigoLocal(vistaLocalEntregaDTO.getId().getCodigoLocal());
							LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);
							LogSISPE.getLog().info("Ciudad destino para entregas: {}" ,localDTO.getCodigoCiudad());
							
							// Se setea en la variable general el valor del c\u00F3digo de la ciudad a comparar con VALORPARAMETRO
							//se comenta por solicitud de la bodega de GYE,reunion el 11-11-2014, no permitir que otros locales de GYE seleccionen la bodega de transito
							//valorCodigoCiudad = localDTO.getCodigoCiudad();
						}
						
						// Se obtiene desde session el contenido de VALORPARAMETRO de SCSPETPARAMETRO
						String valorParametro = (String)session.getAttribute(VAR_VALOR_PARAMETRO_TOTAL);
						String[] parametroDividido = valorParametro.split(",");
						
						// Se comparan c\u00F3digos de ciudad con VALORPARAMETRO
						for(int i=0; i<parametroDividido.length; i++){
							
							String[] codigosCiudad = parametroDividido[i].split("-");
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
					
					//valida lo de autorizaciones
					boolean bodegaCanastas = false; //bandera que me indica si la configuracion es para la bod(99)
					Date fechaMinimaReferenciaAut = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
					Date fechaBusquedaAux =  DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
					if(session.getAttribute(EXISTELUGARENTREGA) != null && session.getAttribute(EDITAFECHAMINIMA) != null){
						//si la fecha m\u00EDnima es menor a hoy
						if(fechaBusquedaAux.getTime() >= fechaMinimaReferenciaAut.getTime()){
//							AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA);
							boolean existeAutorizacion = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA);
							//Verifica si la fecha minima de entrega fue modificada
							if((ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0
									|| existeAutorizacion==Boolean.TRUE)){
								
								LogSISPE.getLog().info("va a verificar la fecha minima de entrega");
								
								//jmena en la configuracion Inicializo fechaEntregaCliente
								if(formulario.getFechaEntregaCliente()==null || formulario.getFechaEntregaCliente().equals("")){
									formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES)));
								}
								if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
									bodegaCanastas = true;
								}
								//Diferencia entre fecha de entrega y fecha minima de entrega
								long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
								LogSISPE.getLog().info("diferenciaEtregaBusca: {}" , diferenciaEntregaBusca);
								
								if(diferenciaEntregaBusca<0.0 && !bodegaCanastas){
									LogSISPE.getLog().info("error en diferencia");
									errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString()));
								}else{
									disminuirFechaMinima(formulario, warnings,session);
								}
							}else{ //si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
								
								//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
								errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
								session.removeAttribute(VISTALOCALCOL);
								formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
							}
							
						}else{ //si la fecha ingresada es mayor a la de hoy 
							//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
							errors.add("buscaFecha",new ActionMessage("errors.fechaMinima"));
						}
					}
					//fin validacion de autorizaciones
					
					LogSISPE.getLog().info("Fecha min: {}" , fechaMinimaEntrega);
					//Hago las validaciones de formulario de valores nulos y formatos
					
					if(formulario.getBuscaFecha()==null){
						formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
					}
					
					//verifica si ingresaron una fecha menor a la fecha minima de entrega o existe una autorizacion, 
					//si la entidad responsable es el local tambien deja modificar la fecha minima de entrega sin autorizacion
					if(ConverterUtil.returnDateDiff(fechaMinimaEntrega,(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA))) <= 0.0 
							|| AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))!=null 
							|| (String)session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null){
						
						if(fechaEntregaCliente!=null && !fechaEntregaCliente.isEmpty()){
							//Diferencia entre fecha de entrega y fecha minima de entrega
							long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(fechaMinimaEntrega, fechaEntregaCliente);
							if(diferenciaEntregaBusca<0.0){
								LogSISPE.getLog().info("error en diferencia");
								errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",fechaMinimaEntrega));
							}else{
								LogSISPE.getLog().info("----calendarioConfiguracionDiaLocal--- {}" , session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX));
								if(session.getAttribute(EXISTELUGARENTREGA)!=null && formulario.validarCantidades(error, request)==0 && formulario.getHoras()!=null && formulario.getMinutos()!=null && formulario.getHorasMinutos()!=null){
									Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
									//validar la bodega de canastos cuando la configuracion es bodCanastos,otroLocal,pedirCD
									if(validarCapacidadBodega(request, errors, formulario, detallePedidoDTOCol)){
										
										Boolean validarCantidadCanastos = Boolean.TRUE;
										String tipoEntregaSesion= (String)session.getAttribute(CotizarReservarAction.TIPO_ENTREGA);
										
										//Obtener el parametro para saber el valor del minimo de canastas que se pueden agregar en las entregas a domicilio
										ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
										Integer codMinCanastosDomicilio = Integer.parseInt(parametroMinCanastas.getValorParametro());
										//Obtener el parametro para saber el monto minimo de una entrega a domicilio
										ParametroDTO paramMontoMinEntregaDomicioCD = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
										Double montoMinEntregaDomicioCD = Double.valueOf(paramMontoMinEntregaDomicioCD.getValorParametro());
										
										LogSISPE.getLog().info("tipoEntregaSesion: {}",tipoEntregaSesion);
										//LogSISPE.getLog().info("Responsable: {}",formulario.getOpLocalResponsable());
										String codCanastosCatalogo = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request)).getValorParametro();
										String codCanastosEspeciales = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request)).getValorParametro();
										//validar que sean solo 50 las entregas a domicilio
//										if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local")) && 
//												formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
										
										long cantidadTotal = 0;
										long cantidadTotalOtrosProductos = 0;
										Double totalEntregaTotalSeleccionada = 0D;//solo se llena cuando son entregas totales
										if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
											
											validarCantidadCanastos = Boolean.FALSE;
											
											for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
												
												LogSISPE.getLog().info("getCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue());
												LogSISPE.getLog().info("getNpCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
												//validar que solo sean canastos de cat\u00E1logo o especiales
												if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
														|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
													LogSISPE.getLog().info("valido cantidad {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
													cantidadTotal += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
												}else{
													cantidadTotal+=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
												}
												//totalEntregaTotalSeleccionada+=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()*detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado();
												totalEntregaTotalSeleccionada+= UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO);
											}
											totalEntregaTotalSeleccionada=Util.roundDoubleMath(totalEntregaTotalSeleccionada, ConstantesGenerales.NUMERO_DECIMALES);
											
											//se obtiene los totales consolidados
											String codigoPedido = detallePedidoDTOCol.iterator().next().getId().getCodigoPedido();
											totalEntregaTotalSeleccionada += ConsolidarAction.obtenerTotalVentaConsolidados(request, codigoPedido);
											cantidadTotal += ConsolidarAction.obtenerCantidadCanastosConsolidados(request, codigoPedido);
											
											LogSISPE.getLog().info("Cantidad total de canastos de cat\u00E1logo y especiales: {}", cantidadTotalOtrosProductos);
											LogSISPE.getLog().info("Cantidad total de otros productos: {}", cantidadTotal);
											LogSISPE.getLog().info("valorTotal total entregas: {}", totalEntregaTotalSeleccionada);
											
											if(cantidadTotal > 0 && cantidadTotal >= codMinCanastosDomicilio.longValue() || totalEntregaTotalSeleccionada > montoMinEntregaDomicioCD){
												LogSISPE.getLog().info("no cumple con validaci\u00F3n de cantidad m\u00EDnima de canastas a domicilio");
												// se verifica si tiene una autorizacion de bodega
												if (AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request,
														MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.montoMinimoEntregaDomicilioCD"))==null){
													validarCantidadCanastos = Boolean.TRUE;
												}
											}
										}
										
										if(validarCantidadCanastos || (formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null 
												&& formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) && formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL))){
											
											//obtengo el local destino
											VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
											LogSISPE.getLog().info("local destino: {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
											CalendarioDiaLocalDTO calendarioDiaLocalDTO=null;
											if(session.getAttribute(CALENDARIODIALOCAL)!=null){
												LogSISPE.getLog().info("si existe calendario");
												//obtengo el dia seleccionado
												calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
											}
											
											LogSISPE.getLog().info("****calendarioDiaLocalDTO**** {}",calendarioDiaLocalDTO);
											//coleccion de costos de entrega de direcciones
											Collection<CostoEntregasDTO> costoEntregasDTOCol = session.getAttribute(COSTOENTREGAAUX)==null ? new ArrayList<CostoEntregasDTO>():(Collection<CostoEntregasDTO>)session.getAttribute(COSTOENTREGAAUX);
											//coleccion de direcciones de entrega
											Collection<DireccionesDTO> direccionesDTOCol = session.getAttribute(DIRECCIONESAUX)==null ? new ArrayList<DireccionesDTO>():(Collection<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
											
											//Autorizacion
											//AutorizacionDTO autorizacionDTO = null;
											//Si existe autorizacion
//											if(session.getAttribute(AUTORIZACION)!=null){
//												autorizacionDTO=(AutorizacionDTO)session.getAttribute(AUTORIZACION);
//											}
											
											//session del costo total de la entrega a domicilio
											double costoEntregaParcial = 0;
											if(session.getAttribute(VALORTOTALENTREGAAUX)!=null){
												costoEntregaParcial=((Double)session.getAttribute(VALORTOTALENTREGAAUX)).doubleValue();
											}
											LogSISPE.getLog().info("costoEntregaParcial: {}",costoEntregaParcial);
											
											String tipoEntrega=null;//variable para saber si es entrega a locales o direcciones
											int totalBultos=0;//total bultos de los detalles
											//double distancia=0;//distancia entre el local y la direccion
											int detallesAgregados=0;//contador para saber cuantos detalles nuevos se agregaros
											//Double totalEntregaSeleccionada = 0d;
											
											//******** transformo la hora ingresada a tipo Time **************
											LogSISPE.getLog().info("Horas: ----{}",formulario.getHoras());
											LogSISPE.getLog().info("MInutos: --{}",formulario.getMinutos());
											LogSISPE.getLog().info("HorasMinutos: --{}",formulario.getHorasMinutos());
											
											GregorianCalendar fechaEntregaCompleta = new GregorianCalendar();
											fechaEntregaCompleta.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
											fechaEntregaCompleta.set(Calendar.HOUR_OF_DAY,Integer.parseInt(formulario.getHoras()));
											fechaEntregaCompleta.set(Calendar.MINUTE,Integer.parseInt(formulario.getMinutos()));
											
											GregorianCalendar fechaSinHora = new GregorianCalendar();
											fechaSinHora.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
											GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
											fechaSinHoraAux.add(Calendar.DAY_OF_MONTH, -1);
											GregorianCalendar fechaActual = new GregorianCalendar();
											fechaActual.setTime(new Date());
											
											LogSISPE.getLog().info("Fecha registro: ----{}",fechaActual.getTime());
											
											if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
												/***************Para entregas Locales***********************/
												//se obtiene la clave para el tipo de entrega a un local
												tipoEntrega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local");
											}else{
												/***************Para entregas domicilio ***********************/
												tipoEntrega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion");
											}
											LogSISPE.getLog().info("*****entro a agregar entregas desde la accion**********");
											//Contador de direcciones
											int secDirecciones=1;
											if(session.getAttribute(SECDIRECCIONESAUX)!=null){
												secDirecciones=((Integer)session.getAttribute(SECDIRECCIONESAUX)).intValue();
											}
											LogSISPE.getLog().info("secDirecciones:.- {}" , secDirecciones);
											
											//validar que no se asignen los pedidos el domingo cuando las entregas son a domicilio - quito o guayaquil
											//Boolean entregasDomicilioQuitoGuayaquil=Boolean.FALSE;
											Boolean entregasDomicilio=Boolean.FALSE;
											Boolean errorDespachoDomingo=Boolean.TRUE;
											String ciudadSeleccionada=(String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO);
											GregorianCalendar fechaSinHoraAux1 = null;
											
											if(((String)session.getAttribute(ENTIDADRESPONSABLELOCAL))==null && 
													((String)session.getAttribute(LUGARENTREGA)).toString().equals(CONTEXTO_ENTREGA_DOMICIO)){
												
												//Inicio- Calculo nuevo para la fecha de despacho
												//En las entregas a domicilio para guayaquil la fecha de despacho debe ser dos d\u00EDas antes de la fecha de entrega
												entregasDomicilio=Boolean.TRUE;
												String seleccionaCiudad=(String)session.getAttribute("otraCuidad");
												String []ciudadesDomicilio=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.ciudadesDomicilio").split(",");
												LogSISPE.getLog().info("Entrega a Domicilio? {}",seleccionaCiudad);
												LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",ciudadSeleccionada);
												LogSISPE.getLog().info("Codigos cuidades {}",ciudadesDomicilio[0]+"-"+ciudadesDomicilio[1]);
												Integer nDiasAntes=0;
												
												if(seleccionaCiudad!=null && ciudadSeleccionada!=null){
													if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[0])){
														ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.guayaquil", request);
														nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
														LogSISPE.getLog().info("diasDespachoGuayaquil {}",nDiasAntes);
														//entregasDomicilioQuitoGuayaquil=Boolean.TRUE;
													}else if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[1])){
														if(EntregaLocalCalendarioUtil.verificarCanastasCatalogo(request)){
															LogSISPE.getLog().info("Si contiene canastos de catalogo");
															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.quito", request);
															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
															LogSISPE.getLog().info("diasDespachoQuito {}",nDiasAntes);
															//entregasDomicilioQuitoGuayaquil=Boolean.TRUE;
														}else{
															LogSISPE.getLog().info("No contiene canastos de catalogo se procede normalmente");
															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
															LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
														}
													}else{
														ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
														nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
														LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
													}
												}else{
													LogSISPE.getLog().info("debe seleccionar una ciudad y existir los par\u00E1metros");
													ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
													nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
												}
												
												GregorianCalendar fechaSinHora1 = new GregorianCalendar();
												fechaSinHora1.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
												fechaSinHoraAux1 = (GregorianCalendar)fechaSinHora1.clone();
												fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -nDiasAntes);
												//verificar que la fecha restada no sea menor a hoy
												Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
												GregorianCalendar fechaActua = new GregorianCalendar();
												fechaActua.setTime(fechaMinimaReferencia);
												if(ConverterUtil.returnDateDiff(fechaActua,fechaSinHoraAux1) < 0.0){
													errors.add("fechaMinimaDespacho",new ActionMessage("errors.fechaMinima.despacho"));
												}else{
													if(obtenerDia(fechaSinHoraAux1).equals(MessagesWebSISPE.getString("constante.dia.semana"))){
														
														LogSISPE.getLog().info("La fecha de despacho de la bodega va ha ser un dia Domingo");
														//validar si se puede o no despachar los domingos
														String[] fechasDomingoPermitidas;
														ParametroDTO parametroBusqueda = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.fechasDomingoDespachoCD", request);
														fechasDomingoPermitidas = parametroBusqueda.getValorParametro().split(",");
														LogSISPE.getLog().info("Domingos permitidos {}",parametroBusqueda.getValorParametro());
														
														//verifica las excepciones de la fecha restada
														for(String diaExc : fechasDomingoPermitidas){
															LogSISPE.getLog().info("::::::**** calendarioDia.mes: {} ; calendarioDia.dia: {}; ",fechaSinHoraAux1.get(Calendar.MONTH),  fechaSinHoraAux1.get(Calendar.DAY_OF_MONTH) );
															LogSISPE.getLog().info("::::::**** diaExc.mes: {} ; diaExc.dia: {}; ",Integer.parseInt(diaExc.split("-")[0]),Integer.parseInt(diaExc.split("-")[1]));
															if(fechaSinHoraAux1.get(Calendar.MONTH) == (Integer.parseInt(diaExc.split("-")[0])-1) && fechaSinHoraAux1.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(diaExc.split("-")[1])){
																errorDespachoDomingo=Boolean.FALSE;
																break;
															}
														}
														
														//Si el domingo habilitado para despachar no est\u00E1 activo, entonces la fecha de despacho ser\u00E1 el sabado anterior
														if(errorDespachoDomingo){
															//a la fecha de despacho le restamos un dia para que sea s\u00E1bado
															fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -1);
															LogSISPE.getLog().info("La fecha de despacho de la bodega va ha ser el dia: "+obtenerDia(fechaSinHoraAux1) +" fecha:"+new Timestamp(fechaSinHoraAux1.getTime().getTime()));
															errorDespachoDomingo=Boolean.FALSE;
														}
													}else{
														errorDespachoDomingo=Boolean.FALSE;
													}
												}
												
											}else{
												errorDespachoDomingo=Boolean.FALSE;
											}
											if((entregasDomicilio && ciudadSeleccionada==null) && (entregasDomicilio
													&& ((formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null 
													&& formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) &&  !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))))) ){
												//WA
												LogSISPE.getLog().info("Ciudad seleccionada en null");
												errors.add("despachosDomingo",new ActionMessage("error.validacion.ciudad.vacia"));
											}else{
												if(!errorDespachoDomingo){
													
													//si la opci\u00F3n es editar
													if((Boolean)session.getAttribute(EntregaLocalCalendarioAction.EDITAR_ENTREGA)){
														//remueve entregas configuradas		
														Collection<EntregaPedidoDTO> entregasPedido = (Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)==null ? new ArrayList<EntregaPedidoDTO>():(Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO);
														ArrayList<EntregaPedidoDTO> entregasPedidoDTOTmp = (ArrayList<EntregaPedidoDTO>)SerializationUtils.clone((Serializable)entregasPedido);
														//obtiene la entrega seleccionada
														EntregaDetallePedidoDTO entregaDetallePedidoDTO = this.entregasTmp.get(indiceEntrega);		
														
														int i = 0;
														for(EntregaPedidoDTO entregaDTO : entregasPedido){
															if(entregaDTO.getCodigoDivGeoPol()!=null){
																if(entregaDTO.getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega()) &&
																		entregaDTO.getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) &&
																		entregaDTO.getCodigoDivGeoPol().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol())&&
																		entregaDTO.getCodigoCiudadSectorEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega())){
																	entregasPedidoDTOTmp.remove(i);
																}
															}
															i++;														
														}
														session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO,entregasPedidoDTOTmp);
													}
													
													detallesAgregados=EntregaLocalCalendarioUtil.crearEntregasDetallePedido(request,formulario,valorCodigoCiudadSector,validarCheckTransito,detallePedidoDTOCol,vistaLocalDestinoDTO,
															costoEntregasDTOCol,direccionesDTOCol, tipoEntrega,totalBultos,fechaEntregaCompleta,fechaSinHoraAux,fechaActual,
															entregasDomicilio,fechaSinHoraAux1,valorCodigoTransito,editarEntrega);
													
													EntregaLocalCalendarioUtil.descontarBultosDiaSeleccionado(request);

													//obtener el valor total de la cantidad solicitada al CD
													sumaCantSolCD= (Long)session.getAttribute("ec.com.smx.sic.sipse.sumaCantSolCD")==null?0L:(Long)session.getAttribute("ec.com.smx.sic.sipse.sumaCantSolCD");
													if(session.getAttribute(HABILITARDIRECCION) != null && session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS)!=null){
														//distribucion de los bultos en camiones
														try{
															CalendarioHoraLocalDTO calendarioHoraLocalDTO = (CalendarioHoraLocalDTO)request.getSession().getAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO);
															if(calendarioHoraLocalDTO!=null){
																totalBultos = (Integer) session.getAttribute("ec.com.smx.sic.sipse.totalBultos");
																int totalEntregas = 1; //Siempre sera una entrega por nuevo requerimiento de Santiago Sanchez
																EntregaLocalCalendarioUtil.descontarEntregasPorHora(request, calendarioHoraLocalDTO, totalEntregas,  (LocalID)session.getAttribute(LOCALID), formulario, errors, warnings);
																session.removeAttribute("ec.com.smx.sic.sipse.totalBultos");
															}
														}
														catch (Exception e) {
															LogSISPE.getLog().info("Problemas al asignar el pedido a los camiones {}",e);
															errors.add("asignacionPedidoCamiones",new ActionMessage("errors.asignar.pedido.camiones"));
														}
													}												
													//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
													try {
														EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOCol, request);
													} catch (Exception e) {
														LogSISPE.getLog().info("Problemas con los responsables de las entregas {}",e);
														errors.add("responsablesEntrega",new ActionMessage("error.validacion.asignacion.responsables"));
													}
													
													//Valida si es obligatorio el paso por la bodega de tr\u00E1nsito
													if(!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica") 
															&& sumaCantSolCD >= (Long.parseLong((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO)))){
														session.removeAttribute("ec.com.smx.sic.sipse.sumaCantSolCD");
														//recorre los detalles
														for(DetallePedidoDTO detallePedidoDTO : (Collection<DetallePedidoDTO>)detallePedidoDTOCol){
															//si no es un art\u00EDculo perecible
															if(!EntregaLocalCalendarioUtil.verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
																//recorre las entregas que tenga configurado hasta ese momento
																HashMap<String,Long> articuloEncontrado= new HashMap<String, Long>();
																for(EntregaDetallePedidoDTO entregaAux : (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol()){
																	//si est\u00E1 habilitado el check de transito
																	if(entregaAux.getEntregaPedidoDTO().getNpValidarCheckTransito() != null 
																		&& entregaAux.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(estadoActivo)
																			&& entregaAux.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(CONTEXTO_ENTREGA_DOMICIO)
																			){
																		
																		if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
																				|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
																			entregaAux.setNpPasoObligatorioBodegaTransito(estadoActivo);
																			
																		}else{
																			
																			//for(EntregaDetallePedidoDTO entregaDetalle:entregaAux.getEntregaDetallePedidoCol()){
																				
																				if(articuloEncontrado.size()==0){
																					articuloEncontrado.put(entregaAux.getCodigoArticulo(), entregaAux.getCantidadEntrega().longValue());
																					if(entregaAux.getCantidadEntrega().longValue() > 0 && entregaAux.getCantidadEntrega().longValue() >= codMinCanastosDomicilio.longValue()){
																						if(entregaAux.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito()==null || entregaAux.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(estadoInactivo)){
																							entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
																						}
																					}
																				}else{
																					Long cantidad=articuloEncontrado.get(entregaAux.getCodigoArticulo());
																					entregaAux.setNpPasoObligatorioBodegaTransito(null);
																					
																					if(cantidad==null){
																						articuloEncontrado.put(entregaAux.getCodigoArticulo(), entregaAux.getCantidadEntrega().longValue());
																					}else{ 
																						long cantidadaActual=entregaAux.getCantidadEntrega().longValue();
																						long cantidadParcialTotal=cantidad.longValue()+cantidadaActual;
																						articuloEncontrado.remove(entregaAux.getCodigoArticulo());
																						articuloEncontrado.put(entregaAux.getCodigoArticulo(),cantidadParcialTotal);
																						if(cantidadParcialTotal > 0 && cantidadParcialTotal >= codMinCanastosDomicilio.longValue()){
																							Collection<EntregaDetallePedidoDTO> entregasEncontradas=	ColeccionesUtil.simpleSearch("codigoArticulo", entregaAux.getCodigoArticulo(), (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol());
																							
																							//indicar que es obligatorio el paso por la bodega de transito de guayaquil
																							for(EntregaDetallePedidoDTO entDetPedDTO : entregasEncontradas){
																								if(entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito()==null || entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"))){
																									entDetPedDTO.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
																								}
																							}
																						}
																						else{
																							entregaAux.setNpPasoObligatorioBodegaTransito(null);
																						}
																					}
																				}
																			//}
																		}
																	}
																}
															}
														}
													}
													
													session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
													
													//Si existieron cambios en las entregas(Se a\u00F1adieron detalles nuevos)
													if(detallesAgregados>0){													
														messages.add("agregaDetalles",new ActionMessage("messages.nuevoDetalle"));
														
														if(calendarioDiaLocalDTO!=null){
															calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.FALSE);
														}
														session.setAttribute(SECDIRECCIONESAUX, new Integer(secDirecciones+1));
														if((session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && !session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) || (session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
															LogSISPE.getLog().info("elimina la sesion de dia seleccionado");
															session.removeAttribute(DIASELECCIONADO);
														}
														session.removeAttribute(CALENDARIODIALOCAL);
														session.removeAttribute(DIRECCION);
														formulario.setDireccion(null);
														formulario.setDirecciones(null);
														formulario.setUnidadTiempo(null);
														formulario.setDistancia(null);
														formulario.setDistanciaH(null);
														formulario.setDistanciaM(null);
														formulario.setSeleccionCiudad("");
														//..session.removeAttribute(DIASELECCIONADO);
														LogSISPE.getLog().info("Existieron Cambios En La Reserva");
														CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
														
														session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocal);
														session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
														session.setAttribute(EXISTENENTREGAS, "ok");
														//Me aseguro que quede la ultima configuracion valida
														formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
														formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
														formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
														formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
														//PASO UNO DE LA AYUDA
														if(session.getAttribute("siDireccion")!=null){
															session.removeAttribute("siDireccion");
															session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
															
															session.removeAttribute(DIASELECCIONADO);
															//jmena valida el mensaje correspondiente a la bod.canastos
			//												if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
			//														formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
															if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
																session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3"));
															}else{
																if(session.getAttribute(COMBOSELECCIONCIUDAD)==null){
																	session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
																}
																else{
																	session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
																}
															}
														}
														
														session.removeAttribute(SessionManagerSISPE.POPUP);
														// Objetos para construir los tabs, si fue modificacion de reserva
														beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
														session.removeAttribute(MOSTRAR_POPUPTAB);
														session.removeAttribute(TABSELECCIONADONAVEGACION);
														session.removeAttribute(HABILITARCANTIDADENTREGA);
														session.removeAttribute(HABILITARCANTIDADRESERVA);
														session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
														session.removeAttribute(BLOQUEAROPCIONENTREGADOMICILIO);
														session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
														session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
														session.removeAttribute(PASOSPOPUP);
														session.removeAttribute( POSICIONDIVCONFENTREGAS);
														session.removeAttribute(EXISTELUGARENTREGA);
														session.removeAttribute(EDITAFECHAMINIMA);
														session.removeAttribute(CALENDARIODIALOCALCOL);
														session.removeAttribute(FECHAENTREGACLIENTE);
														session.removeAttribute(EntregaLocalCalendarioAction.CODIGOENTREGAPREVIO);
														CalendarioLocalEntregaDomicilioUtil.eliminarVariablesCalendarioSICMER(session, formulario);
													}
													
													//No existieron cambios
													else if(messages.size()==0){
														warnings.add("sinCambios",new ActionMessage("warnings.sinCambiosReserva"));
													}
													/***************Actualiza el calendario********************/
													if(session.getAttribute(CALENDARIODIALOCALCOL)!=null){
														//fecha minima
														Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
														//fecha maxima
														Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
														//obtengo el mes del dia que fue seleccionado para la entrega
														Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
														EntregaLocalCalendarioUtil.obtenerCalendario(request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
														
														//jmena validacion cal.bod una vez que acepta reset la fecha entrega cliente.
			//											if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
			//													formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
														if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
															formulario.setFechaEntregaCliente(null);
															session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
															session.removeAttribute(COMBOSELECCIONCIUDAD);
														}
													}
												}else{
													if(errors.size()==0){ //para el caso dondo la fecha de despacho es menor a la fecha actual
														LogSISPE.getLog().info("No cumple la condicion que para las entregas se despachen el domingo");	
														errors.add("despachosDomingo",new ActionMessage("error.validacion.despacho.domicilio"));
													}
												}
											}
										}else{
												LogSISPE.getLog().info("No cumple la condicion que para las entregas a domicilio minimo de canastas sea 50");
												errors.add("minEntrega",new ActionMessage("error.validacion.cantidadMinima.entrega.domicilio",codMinCanastosDomicilio,montoMinEntregaDomicioCD));	
										}
									}else{
										LogSISPE.getLog().info("No Existieron Cambios En La Reserva");
										CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
										session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocal);
										session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
									}
								}
							}
						}else{
							//validacion diferente en caso de entrega a domicilio desde local
							if(!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
								if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
									errors.add("horaSeleccionada", new ActionMessage("errors.fecha.entrega.domicilio.requerida"));
									formulario.setFechaEntregaCliente(null);
								}else{
									warnings.add("configuracionInicial",new ActionMessage("warnings.sinSeleccioneConfiguracionEntregas"));
								}
							}
							else{
								errors.add("fechaEntregaCliente",new ActionMessage("errors.seleccionDia.requerido"));							
							}

							if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) &&
									session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")) &&
									session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
								formulario.setFechaEntregaCliente(null);
							}
						}
					}else{//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
						//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
						errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
						session.removeAttribute(VISTALOCALCOL);
					}
					
					if(error.size() == 0 && errors.size() == 0 && warnings.size() == 0){
						session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
					}
					
					if(error.size() > 0){
						if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) &&
								session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")) &&(
								session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")) 
								|| session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio.local")))){
							formulario.setFechaEntregaCliente(null);
						}
					}								
				}
				obtenerEntregas(session);
				formulario.setHoras(null);
				formulario.setMinutos(null);
			}
		}
		
		//calcular el costo del flete de la entrega a domicilio
		else if(request.getParameter("botonCalcularCostoEntregaDomicilio") != null){
			CostoEntregasDTO costoEntregasDTO = new CostoEntregasDTO();
			EntregaLocalCalendarioUtil.costoEntregaDomicilio(request, session, formulario, beanSession,costoEntregasDTO);
		}
		
		//instanciar el popUp para confirmar eliminar entregas seleccionadas
		else if(request.getParameter("botonEliminarEntrega")!=null){
			//verificamos si ha seleccionado una entrega para eliminar
			if(formulario.getSeleccionEntregas().length>0){
				instanciarVentanaEliminarEntregas(request);
				session.removeAttribute(CALENDARIOS_ENTREGAS_ELIMINAR);
			}else{
				//warnings.add("eliminarEntrega",new ActionMessage("warnings.eliminarEntrega"));
				instanciarVentanaAdvertenciaEliminarEntregas(request);
			}
		}		
		//confirmar eliminar las entregas seleccionadas
		else if(request.getParameter("confirmarEliminarEntregas")!=null){
			EntregaLocalCalendarioUtil.eliminarEntregasPedido(request, session, errors, warnings,formulario);
			obtenerEntregas(session);
		}		
		//cancelar la eliminacion de las entregas seleccionadas
		else if (request.getParameter("cancelarEliminarEntregas")!=null){
			LogSISPE.getLog().info("cancelarEliminarEntregas");
			session.removeAttribute(SessionManagerSISPE.POPUP);
		}		
		/******************************************************************************
		 ****************************AUTORIZACIONES*************************************
		 ******************************************************************************/
		//Autorizaciones para reservar bodega de local
		else if(request.getParameter("botonAutorizar")!=null){
			LogSISPE.getLog().info("Entro al boton autorizar");
			Long codigoTipoAutorizacion = ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL;
			
			if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
				CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
				session.setAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER,formularioSicmer);
			}
			
			if(session.getAttribute(CALENDARIODIALOCAL)!=null){
				//se crea el convertidor
				SqlTimestampConverter convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
				EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
				AutorizacionDTO autorizacionDTO =null;
				if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) &&  
						formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && 
						((String)session.getAttribute("ec.com.smx.sic.sispe.validar.bodega"))!=null ){
				 autorizacionDTO = formulario.buscaAutorizacion(session,(SessionManagerSISPE.getCurrentLocal(request)).toString(),(Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
				}else{
					autorizacionDTO = formulario.buscaAutorizacion(session,calendarioDiaLocalDTO.getId().getCodigoLocal().toString(),(Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
				}
				
				if(autorizacionDTO==null){
					//si no existe una autorizacion de ese tipo se muestra el componente de autorizaciones
					LogSISPE.getLog().info("Se muestra el componente de autorizaciones para RESERVAR BODEGA DE LOCAL");
					request.getSession().setAttribute(CotizarReservarAction.FORMULARIO_TEMPORAL, formulario);
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, Boolean.TRUE, codigoTipoAutorizacion, null, warnings);
					formulario.setAutorizacion(null);					
				}else{
					errors.add("autorizacion",new ActionMessage("errors.autorizacion",calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
				}
			}else{
				errors.add("selecciondia",new ActionMessage("errors.localDia"));
				EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
				if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
					formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
					formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
					formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
					formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
					formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
					formulario.setSelecionCiudadZonaEntrega(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSelecionCiudadZonaEntrega());
					formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
					formulario.setHoraDesde(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraDesde());
					formulario.setHoraHasta(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraHasta());
				}
			}
		}
		//si selecciona la opci\u00F3n eliminar desde la lista de entregas
		else if(request.getParameter("eliminarEntrega")!=null){	
			Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);	
			//obtiene la lista de entregas
			ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);
			//obtiene la entrega seleccionada
			indiceEntrega = Integer.valueOf(request.getParameter("indiceEntrega"));
			EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);	
			Boolean despacho = false;
			//obtiene los detalles de la entrega
			for(DetallePedidoDTO detallePedido :  detallePedidoDTOCol){
				for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){							
					for(DetallePedidoDTO detallePedidoDTO :  entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){							
						//compara las entregas en las dos listas para pasar a eliminar
						if(detallePedido.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega())){								
							if(entregaDetallePedido.getFechaRegistroDespacho()!=null && entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))//11
									|| (entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) && entregaDetallePedido.getEntregaPedidoDTO().getCodigoContextoEntrega().toString().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL) 
											&& entregaDetallePedido.getFechaRegistroEntregaCliente()!=null)){//9 y 34 y entregado - domicilio desde local
								despacho = true;
								break;
							}									
						}	
					}						
				}			
			}
				
			if(despacho){
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				errors.add("entregaEliminar",new ActionMessage("errors.entregaCDEliminar"));
			}else{	
				instanciarVentanaEliminarEntrega(request);
			}
		}//si confirma eliminar la entrega desde la lista de entregas
		else if(request.getParameter("confirmarEliminarEntrega")!=null){			
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);	
			//obtiene la lista de entregas
			ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);	
			EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);
			
			//llama al m\u00E9todo que elimina la entrega seleccionada
			listaEliminarEntregas(detallePedidoDTOCol, request, session, formulario, errors, warnings);
			//remueve la entrega seleccionada
			entregas.remove(indiceEntrega);
			session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_RESPONSABLES,entregas);
			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, null);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fechahora"));
			info.add("eliminarEntrega",  new ActionMessage("info.entrega.eliminar",simpleDateFormat.format(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente())));
			obtenerEntregas(session);
			session.setAttribute(EntregaLocalCalendarioAction.EXISTENCAMBIOS,"OK");
		}		
		//si cancela la eliminacion de la entrega desde la lista de entregas
		else if (request.getParameter("cancelarEliminarEntrega")!=null){
			session.removeAttribute(SessionManagerSISPE.POPUP);
		}//si selecciona la opci\u00F3n editar desde la lista de entregas
		else if(request.getParameter("editarEntrega")!=null){			
			Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);	
			//obtiene la lista de entregas
			ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);
			//obtiene la entrega seleccionada
			indiceEntrega = Integer.valueOf(request.getParameter("indiceEntrega"));
			EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);	
			Boolean despacho = false;
			//obtiene los detalles de la entrega
			for(DetallePedidoDTO detallePedido :  detallePedidoDTOCol){
				for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){							
					for(DetallePedidoDTO detallePedidoDTO :  entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){							
						//compara las entregas en las dos listas para pasar a editar
						if(detallePedido.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega())){								
							if(entregaDetallePedido.getFechaRegistroDespacho()!=null && entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))//11
									|| (entregaDetallePedido.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) && entregaDetallePedido.getEntregaPedidoDTO().getCodigoContextoEntrega().toString().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL) 
											&& entregaDetallePedido.getFechaRegistroEntregaCliente()!=null)){//9 y 34 y entregado - domicilio desde local
								despacho = true;
								break;
							}									
						}	
					}						
				}			
			}
				
			if(despacho){
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				errors.add("entregaEditar",new ActionMessage("errors.entregaCDEditar"));
			}else{	
				instanciarVentanaEditarEntrega(request);
			}								
		}//si confirma editar la entrega desde la lista de entregas
		else if(request.getParameter("confirmarEditarEntrega")!=null){
			Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
			//obtiene la lista de entregas
			ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);	
			//Obtiene las direcciones
			Collection<DireccionesDTO> direccionesDTOCol = (Collection<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
			//Almacena las listas temporales
			detallePedidoDTOColTmp = (Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedidoDTOCol);
			entregasTmp =  (ArrayList<EntregaDetallePedidoDTO>)SerializationUtils.clone((Serializable)entregas);
			direccionesColTmp = (Collection<DireccionesDTO>)SerializationUtils.clone((Serializable)direccionesDTOCol);
			session.setAttribute(DIRECCIONES_TMP,direccionesColTmp);	

			//obtiene la entrega seleccionada
			EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregasTmp.get(indiceEntrega);				
							
			editarEntrega = true;			
			session.setAttribute(EDITAR_ENTREGA, editarEntrega);
			asignaLocal=false;
			//calendario temporal
			Object[] calendarioDiaLocalDTOOBJ = (Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);			
			calendarioDiaLocalDTOOBJTmp = (Object[])SerializationUtils.clone((Serializable)calendarioDiaLocalDTOOBJ);
			
			//elimina las entregas
			listaEliminarEntregas(detallePedidoDTOCol,request, session, formulario, errors, warnings);
			//remueve la entrega seleccionada
			entregas.remove(indiceEntrega);
			session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_RESPONSABLES,entregas);	
				
			LogSISPE.getLog().info("--- Abrir Popup configuracion ---");			
			beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
			instanciarVentanaOpcionesConfiguracion(request);			
			regresarvaloresInicialesExistenEntregas(session);			
			
			//opciones de entrega
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES,entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp());					
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONTIPOENTREGA,entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAlcanceEntrega().toString());		
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK,entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().toString());
			session.setAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA,entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().toString());				
						
			//bultos			
			//bultos = entregaDetallePedidoDTO.getNpCantidadBultos();	
			
//			//calendario seleccionado
//			if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null && entregaDetallePedidoDTO.getNpCantidadBultos()>0){
//				try{
//					CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)entregaDetallePedidoDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO();
//					calendarioHoraLocalDTOCol = (HashSet)calendarioDiaLocalDTO.getCalendarioHoraLocalCol();
//					//request.getSession().setAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL,calendarioHoraLocalDTOCol);
//					ArrayList<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux =  new ArrayList<CalendarioHoraLocalDTO>(calendarioHoraLocalDTOCol);
//					calendarioHoraLocalSeleccionado = calendarioHoraLocalDTOColAux.get(0);		
//					//request.getSession().setAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO,calendarioHoraLocalSeleccionado);
//					formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));	
//				}catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//			if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				//obtiene la lista de ciudades
//				ciudad = "";	
//				ciudadGye = "";
//				if(session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)!=null){	
//					for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO : (Collection<VistaEstablecimientoCiudadLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)){						
//						for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocal : (Collection<VistaEstablecimientoCiudadLocalDTO>)vistaEstablecimientoCiudadLocalDTO.getVistaLocales()){
//							String codigoVistaCiudad = "OTR" + vistaEstablecimientoCiudadLocal.getNombreCiudad() + "/" + vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();							
//							if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada()!=null){
//								if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada().equals("1")){
//									codigoVistaCiudad = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
//								}
//							}
//							if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol())){
//								ciudad = codigoVistaCiudad;
//								if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"))){
//									ciudadGye = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
//								}	
//							}						
//						}
//					}
//				}				
//				//asigna la ciudad
//				formulario.setSeleccionCiudad(ciudad);			
//				session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol());				
//				//asigna la zona
//				formulario.setSelecionCiudadZonaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega());
//			}
			
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
			String codClaRecetasNuevas = parametroDTO.getValorParametro();
			
			ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
			Integer codMinCanastosEspeciales = Integer.parseInt(parametroMinCanastas.getValorParametro());
			//para mostrar opcion de canastos especiales				
			if(CotizacionReservacionUtil.verificarExistenCanastasEspeciales(detallePedidoDTOCol, request,codClaRecetasNuevas,codMinCanastosEspeciales)){
				session.setAttribute(MOSTRAROPCIONCANASTOSESPECIALES, "ok");
				if(!CotizacionReservacionUtil.verificarCantidadCanastasEspeciales(detallePedidoDTOCol, request,codClaRecetasNuevas,codMinCanastosEspeciales, info)){
					session.setAttribute(BLOQUEAROPCIONENTREGADOMICILIO, "ok");
					info.add("entregasCompletas",new ActionMessage("info.bloqueo.entregas.domicilio"));
				}
			}else{
				session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
				session.removeAttribute(BLOQUEAROPCIONENTREGADOMICILIO);
			}
					
			obtenerEstablecimientosEntregasSICMER(request, session);
			
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			
		}//si cancela editar entrega desde la lista de entregas
		else if (request.getParameter("cancelarEditarEntrega")!=null){
			session.removeAttribute(SessionManagerSISPE.POPUP);
			editarEntrega = false;
			session.setAttribute(EDITAR_ENTREGA, false);
		}
		else if(request.getParameter("seleccionMouse")!=null){			
			Collection<DetallePedidoDTO> detallePedidoDTOCol = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);	
			//obtiene la lista de entregas
			ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);
			//obtiene la entrega seleccionada
			indiceEntrega = Integer.valueOf(request.getParameter("indiceEntrega"));
			EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);	
			
			//obtiene los detalles de la entrega
			for(DetallePedidoDTO detallePedido :  detallePedidoDTOCol){
				for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){	
						entregaDetallePedido.getEntregaPedidoDTO().setSeleccionMouse(0);
						for(DetallePedidoDTO detallePedidoDTO :  entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){							
							//compara las entregas en las dos listas y asigna el valor para pintar la fila
							if(detallePedido.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) 
									&& entregaDetallePedido.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) 
									&& entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()) 
									&& entregaDetallePedido.getEntregaPedidoDTO().getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega())){
									
									entregaDetallePedido.getEntregaPedidoDTO().setSeleccionMouse(1);
							}
//							else{
//								entregaDetallePedido.getEntregaPedidoDTO().setSeleccionMouse(0);
//							}
						}						
				}			
			}	
									
			session.setAttribute(DETALLELPEDIDOAUX,detallePedidoDTOCol);
			for(EntregaDetallePedidoDTO entreDetallePedidoDTO: entregas){
				entreDetallePedidoDTO.getEntregaPedidoDTO().setSeleccionMouse(0);
			}			
			entregas.get(indiceEntrega).getEntregaPedidoDTO().setSeleccionMouse(1);	
			session.setAttribute(ENTREGAS_RESPONSABLES,entregas);
		}
		//Autorizaciones para disminuir fecha minima de entrega
		else if(request.getParameter(GlobalsStatics.PAR_USAR_AUTORIZACION)!=null){
			
			String parametro = request.getParameter(GlobalsStatics.PAR_USAR_AUTORIZACION);
			if(parametro.equals("2") ){
				
				Long codigoTipoAutorizacion = ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue();
				
				if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, codigoTipoAutorizacion)){
						
					if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
						CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
						session.setAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER,formularioSicmer);
					}
					
					LogSISPE.getLog().info("Se muestra el componente de autorizaciones para disminuir la fecha minima de entrega");
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, Boolean.TRUE, codigoTipoAutorizacion, null, warnings);
				}else{
					
					info.add("autorizacionExistente", new ActionMessage("info.autorizacion.disminuir.fecha.entrega.ya.existe","DISMINUIR FECHA M\u00CDNIMA DE ENTREGA"));
					EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
					
					if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
						formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
						formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
						formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
						formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
						formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
						formulario.setSelecionCiudadZonaEntrega(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSelecionCiudadZonaEntrega());
						formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
						formulario.setHoraDesde(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraDesde());
						formulario.setHoraHasta(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraHasta());
					}
				}
			}else if(parametro.equals("3")){
				
				Long codigoTipoAutorizacion = ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS.longValue();
				
				if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, codigoTipoAutorizacion)){
						
					LogSISPE.getLog().info("Se muestra el componente de autorizaciones para que la bodega elabore los canstos");
					AutorizacionesUtil.mostrarPopUpAutorizacionesPorTipo(request, Boolean.TRUE, codigoTipoAutorizacion, null, warnings);
					
				}else{
					info.add("autorizacionExistente", new ActionMessage("info.autorizacion.disminuir.fecha.entrega.ya.existe",": EL CD ELABORE LOS CANASTOS"));
				}
			}
		}
		//se procesa la respuesta del sistema de autorizaciones
		else if(request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador")) != null){
			
			LogSISPE.getLog().info("verificando una autorizacon de entregas");
			session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
			String respuestaAutorizaciones = (String) request.getParameter(MessagesWebSISPE.getString("accion.autorizaciones.respuesta.autorizador"));
			
			Boolean autorizacionValida =  AutorizacionesUtil.verificarAutorizacion(respuestaAutorizaciones, request, errors, info);
			if(autorizacionValida){
				LogSISPE.getLog().info("se disminuye la fecha minima de entrega y reservar espacio en locales");
				//1:19987:F17-1774:9:UTILIZADA:0
				Long codigoTipoAutorizacion= Long.valueOf(respuestaAutorizaciones.split(AutorizacionesUtil.SEPARADOR_DOS_PUNTOS)[3]);
				Long numeroAutorizacion= Long.valueOf(respuestaAutorizaciones.split(AutorizacionesUtil.SEPARADOR_DOS_PUNTOS)[1]);
				if(codigoTipoAutorizacion.longValue()==ConstantesGenerales.TIPO_AUTORIZACION_RESERVAR_BODEGA_LOCAL.longValue()){
					//Si ingreso un numero de autorizacion
//					EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
						AutorizacionDTO autorizacionDTO = new AutorizacionDTO();
						try{
							//veirifico que no se repita el numero de autorizacion para otro local
							boolean existeAutorizacion = formulario.buscaNumeroAutorizacion(session,String.valueOf(numeroAutorizacion.longValue()));
							if(!existeAutorizacion){
								//obtengo el local
								VistaLocalDTO vistaLocalDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
								
//								Integer codigoLocal=vistaLocalDTO.getId().getCodigoLocal().intValue();
//								if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) &&  
//										formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && 
//										((String)session.getAttribute("ec.com.smx.sic.sispe.validar.bodega"))!=null ){
//									//grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
//									codigoLocal=Integer.valueOf((SessionManagerSISPE.getCurrentLocal(request)).toString()).intValue();
//								}
								
								
								
		
		
								//metodo para validar autorizacion
								autorizacionDTO = AutorizacionesUtil.obtenerObjAutorizacionPorTipoDesdeSesion(request, codigoTipoAutorizacion,numeroAutorizacion);
								
								//se crea el convertidor
								SqlTimestampConverter convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
								
								CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
								Collection<AutorizacionEntregasDTO> autorizacionEntregasWDTOCol=new ArrayList<AutorizacionEntregasDTO>();
								AutorizacionEntregasDTO autorizacionEntregasWDTO=new AutorizacionEntregasDTO();
								autorizacionEntregasWDTO.setCodigoAutorizacion(autorizacionDTO.getId().getCodigoAutorizacion());
							
								//jmena Para la autorizacion del calendarioBodega seteo el codigoLocal (99)
								if(session.getAttribute(OPCIONLUGARENTREGA).toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
									autorizacionEntregasWDTO.setCodigoLocal(vistaLocalDTO.getId().getCodigoLocal().toString());
								}else if(request.getSession().getAttribute("ec.com.smx.sic.sispe.validar.bodega")!=null){ // ingresa solo cuando hay que validar el valor de disponibilidad de otro local y disponibilidad de la bodega 99
									autorizacionEntregasWDTO.setCodigoLocal(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
								}else{
									autorizacionEntregasWDTO.setCodigoLocal(String.valueOf(calendarioDiaLocalDTO.getId().getCodigoLocal()));
								}
								LogSISPE.getLog().info("fecha a la que se le asigna autorizacion: {}", calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
								autorizacionEntregasWDTO.setFechaAutorizacion((Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
								autorizacionEntregasWDTO.setNumeroAutorizacion(numeroAutorizacion.toString());
								autorizacionEntregasWDTO.setAutorizacionDTO(autorizacionDTO);
								if(session.getAttribute(AUTORIZACIONENTREGASCOL)!=null)
									autorizacionEntregasWDTOCol=(Collection<AutorizacionEntregasDTO>)session.getAttribute(AUTORIZACIONENTREGASCOL);
								autorizacionEntregasWDTOCol.add(autorizacionEntregasWDTO);
								session.setAttribute(AUTORIZACIONENTREGASCOL,autorizacionEntregasWDTOCol);
								info.add("autorizacionValida",new ActionMessage("info.autorizacionValida"));
							}
							else
								errors.add("existeAutorziacion",new ActionMessage("errors.existeAutorizacion"));
						}catch (Exception ex ) {
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("autorizacion",new ActionMessage("errors.autorizacionInvalida"));
						}
				}else if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue()){
					info.add("autorizacionValida",new ActionMessage("info.autorizacionValida"));
					
				}else if(codigoTipoAutorizacion.longValue() == ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS.longValue()){
					info.add("autorizacionValida",new ActionMessage("info.autorizacionValida"));
					
				}
			}else{
				LogSISPE.getLog().info("La autorizacion ingresada no es valida");
				errors.add("autorizacion",new ActionMessage("errors.autorizacionInvalida"));
			}
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
				formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
				formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
				formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
				formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
				formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
				formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
				formulario.setSelecionCiudadZonaEntrega(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSelecionCiudadZonaEntrega());
				formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
				formulario.setHoraDesde(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraDesde());
				formulario.setHoraHasta(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraHasta());
			}
		}
		//se cierra el popUp del componente de autorizaciones
		else if(request.getParameter("cancelar") != null){
			LogSISPE.getLog().info("se cierra el popUp de autorizaciones");
			session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
				formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
				formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
				formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
				formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
				formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
				formulario.setSelecionCiudadZonaEntrega(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSelecionCiudadZonaEntrega());
				formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
				formulario.setHoraDesde(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraDesde());
				formulario.setHoraHasta(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getHoraHasta());
			}
		}
		//aceptar la autorizacion
		else if(request.getParameter("condicionAceptar")!=null){
			//TODO Falta la seccion de autorizaciones para la entrega
			LogSISPE.getLog().info("//TODO Falta la seccion de autorizaciones para la entrega");
		}
		
		//cerrar la ventana de autorizacion
		else if(request.getParameter("condicionCancelar")!=null){
			//TODO Falta la seccion de autorizaciones para la entrega
			LogSISPE.getLog().info("//TODO Falta la seccion de autorizaciones para la entrega");
		}
		
		//seleccionar una ciudad de entrega
		else if(request.getParameter("seleccionCiudadCombo")!=null){
			LogSISPE.getLog().info("selecciona una ciudad en el combo: {}" , formulario.getSeleccionCiudad());
		
			if(formulario.getSeleccionCiudad().equals("ciudad") || formulario.getSeleccionCiudad().equals("")){
				session.removeAttribute(DIASELECCIONADO);
				warnings.add(formulario.getSeleccionCiudad(), new ActionMessage("errors.seleccion.ciudad", "Ciudad de Entrega V\u00E1lida"));
				//se quita de sesion los sectores porque la ciudad no es valida
				session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
			}else {
				//OANDINO: Se carga el valor del nombre ciudad/c\u00F3digo ciudad -----------------------------			
				String[] valorCodigoDivGeoPol = formulario.getSeleccionCiudad().split("/");
				
				if(valorCodigoDivGeoPol.length > 1){
					// Se carga en session el valor del c\u00F3digo de la ciudad que viene adjunto al nombre de la misma desde combobox de JSP
					session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, valorCodigoDivGeoPol[1]);
				}else{
					session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, formulario.getSeleccionCiudad());
					
					//buscar zonas de esa ciudad
					cargarZonaCiudad(formulario, session);
					
					//Si la ciudad no tiene zona, se carga los totales de camiones
					//Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
					//if(CollectionUtils.isEmpty(zonasCiudad)){	
					LocalID localID=new LocalID();
					localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")));
					Boolean loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
					LogSISPE.getLog().info("encontro al local: {}" , loc);
						if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
							obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
							//obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
						}
					//}
				}
				LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO));
				//----------------------------------------------------------------------------------------
				
				//Saco las tres primeras letras del valor del combo para ver si selecciono una ciudad distinta a las recomendadas
				String subNombre = valorCodigoDivGeoPol[0].substring(0, 3);
				
				if(!subNombre.equals("") && subNombre.equals("OTR")){
					LogSISPE.getLog().info("Selecciono otra ciudad");
//					//Si selecciona otra ciudad se realiza la pregunta
//					request.setAttribute("ec.com.smx.sic.sispe.mensajeSeleccionCiudad", "ok");
					
					//buscar zonas de esa ciudad
					cargarZonaCiudad(formulario, session);
					
					if(((String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO)).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"))){
						//solo si es la ciudad de guayaquil tenemos que mostrar el calendario de la bodega 97 y el check de transito debe ser obligatorio
						LocalID localID=new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito")));
						Boolean loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
						LogSISPE.getLog().info("encontro al local: {}" , loc);
						if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
							obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
							//obtenerCalendarioPorSemana(request, localID, null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
						}
					}else{
						LocalID localID=new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")));
						Boolean loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
						LogSISPE.getLog().info("encontro al local: {}" , loc);
							if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
								obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
								//obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
							}
//						
//						//Si la ciudad no tiene zona, se carga los totales de camiones
//						Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
//						if(CollectionUtils.isEmpty(zonasCiudad)){	
//							if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
//								obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
//							}
//						}
					}
					
					session.setAttribute("otraCuidad", "ok");
				}else if(formulario.getSeleccionCiudad().equals(""))
					session.removeAttribute(DIASELECCIONADO);
				else{
					LogSISPE.getLog().info("selecciono ciudad recomendada");
					if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
						int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
						session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
					}else{
						session.setAttribute(DIASELECCIONADO, "ok");
					}
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePaso2.3"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
					
					session.setAttribute("siDireccion", "ok");
					session.setAttribute("otraCuidad", "ok");
				}
				//resetea la direccion cuando no es calendario por horas
				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) == null){
					formulario.setDirecciones(null);
					formulario.setDireccion(null);
					formulario.setDistancia(null);
					formulario.setDistanciaH(null);
					formulario.setDistanciaM(null);
					session.removeAttribute(DIRECCION);
				}
			}
		}
		//Validacion Ciudad Calendario SICMER
		else if(request.getParameter("seleccionCiudadComboSICMER")!=null){
			LogSISPE.getLog().info("selecciona una ciudad en el combo: {}" , formulario.getSeleccionCiudad());
			formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
			cargarZonasCiudadComboSICMER(formulario, session, info);
		}
		//cuando se selecciona el combo de ZONA de CIUDAD
		else if(request.getParameter("seleccionCiudadZonaCombo")!= null){
			
			String zonaSeleccionada = formulario.getSelecionCiudadZonaEntrega();
			LogSISPE.getLog().info("zona seleccionada {}",zonaSeleccionada);
				
			if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
				obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
			}
		}
		//Validacion Sector Ciudad Calendario SICMER
		else if(request.getParameter("seleccionCiudadZonaComboSICMER")!= null){
			formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
			String zonaSeleccionada = formulario.getSelecionCiudadZonaEntrega();
			LogSISPE.getLog().info("zona seleccionada {}",zonaSeleccionada);
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
		}

		//seleccionar SI en la ventana del mensaje "otra ciudad"
		else if(request.getParameter("condicionSi")!=null){
			LogSISPE.getLog().info("ventada de ciudad opcion si");
			
			//se elimina el calendario de bodega por horas para que cargue el calendario normal
			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
			session.setAttribute(SELECCIONARLOCAL, "ok");
			
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			formulario.setOpLugarEntrega(CONTEXTO_ENTREGA_OTRO_LOCAL);
			session.removeAttribute(DIASELECCIONADO);
			request.removeAttribute("ec.com.smx.sic.sispe.mensajeSeleccionCiudad");
			aceptaConfiguracionEntrega(request, formulario, error, errors, warnings,info);
			session.removeAttribute(POSICIONDIVCONFENTREGAS);
			
			//buscar zonas de esa ciudad
			cargarZonaCiudad(formulario, session);
			
			//Si la ciudad no tiene zona, se carga los totales de camiones
			Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
			if(CollectionUtils.isEmpty(zonasCiudad)){	
				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
					obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
				}
			}
		}
		
		//seleccionar NO en la ventana del mensaje "otra ciudad"
		else if(request.getParameter("condicionNo")!=null){
			LogSISPE.getLog().info("ventada de ciudad opcion no");
			if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
				int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
				session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
			}else{
				session.setAttribute(DIASELECCIONADO, "ok");
			}
			formulario.setDirecciones(null);
			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
			//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
			if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){
				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso4"));
			}
			else{
				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
			}
			session.setAttribute("siDireccion", "ok");
			
			//buscar zonas de esa ciudad
			cargarZonaCiudad(formulario, session);
			
			//Si la ciudad no tiene zona, se carga los totales de camiones
			Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
			if(CollectionUtils.isEmpty(zonasCiudad)){	
				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
					obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
				}
			}
		}
		
		////Cambio de tab entre la configuracion de entregas y el responsable de las entregas
		/**
		 * @author jmena
		 * Control para los tabs de configuracion de entrega
		 */
		else if (beanSession.getPaginaTab() != null && session.getAttribute(MOSTRAR_POPUPTAB) == null && beanSession.getPaginaTab().comprobarSeleccionTab(request) ){
			if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
				//se reasigna el valor de los checks de transito
				formulario.setCheckTransitoArray((String [])session.getAttribute(CHECKTRANSITO));
				/*--------------- si se escogi\u00F3 el tab configuraciones entrega op1 ----------------*/
				LogSISPE.getLog().info("Se elige el tab de configuracion Opcion1");
				WebSISPEUtil.cambiarTabConfiguracionesEntregas(beanSession, 0);
				session.removeAttribute(CHECKTRANSITO);
			}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
				session.setAttribute(CHECKTRANSITO, formulario.getCheckTransitoArray());
				/*------------   si se escogi\u00F3 el tab configuraciones entrega op2 --------------*/
				LogSISPE.getLog().info("Se elige el tabde configuracion Opcion2");
				obtenerEntregas(session);
				WebSISPEUtil.cambiarTabConfiguracionesEntregas(beanSession, 1);
			}
		}
		/**
		 * Parametro para manejar el calendario SICMER
		 */
		else if(request.getParameter("calendariosicmer")!=null&&request.getParameter("accioncalendario")!=null){
			LogSISPE.getLog().info("Calendario Sicmer");
			EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
			CalendarioLocalEntregaDomicilioUtil.accionesCalendario(request,errors,info, formulario);
			formulario.setOpLugarEntrega(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL);
			
		}
		else if(request.getParameter("actualizarCalendario") != null){
			
			LogSISPE.getLog().info("Actualizando el calendario");
			
			if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
				
				LogSISPE.getLog().info("Actualizando el calendario de entregas a domicilio desde el CD");
				formulario.mantenerValoresEntregas(request);
				session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, formulario.getSeleccionCiudad());
				
//				buscar zonas de esa ciudad
				cargarZonaCiudad(formulario, session);
				
				LocalID localID = new LocalID();
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")));
				obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
			}
		}
		
		//IOnofre. Accion del boton que muestra el PopUp de adjuntar archivo croquis
		else if(request.getParameter("mostrarPopUpArchCroquis") != null){
			
			String indicePedido = request.getParameter("indiceEntregaCroquis");
			ManejarArchivo manejarArchivo = new ManejarArchivo();
			manejarArchivo.mostrarPopupArchivoCroquis(request, session, indicePedido, accionEstado);
			forward = "croquis";
	     }
		//IOnofre. Accion del boton que cancela el PopUp de adjuntar archivo croquis
		else if(request.getParameter("cancelarArchCroquis") != null){
			LogSISPE.getLog().info("se cierra el popUp del archivo croquis");
			session.removeAttribute(SessionManagerSISPE.POPUP);
		}
		//IOnofre. Accion del boton que adjunta el archivo croquis
		 else if(request.getParameter("adjuntarArchivo")!=null ){
			 LogSISPE.getLog().info("se carga el archivo seleccionado");
			 ManejarArchivo manejarArchivo = new ManejarArchivo();
			 manejarArchivo.adjuntarArchivoCroquis(request, session, formulario, accionEstado);
			 salida = "desplegar";
		}
		//IOnofre. Accion del icono eliminar archivo del popUp mostrarArchivoCroquis
		else if(request.getParameter("eliminarArchivo") != null){
			LogSISPE.getLog().info("se elimina el archivo seleccionado");
			ManejarArchivo manejarArchivo = new ManejarArchivo();
			manejarArchivo.eliminarArchivoCroquis(request, session);
			salida = "desplegar";
		}
		//IOnofre. Cierra el popUp de remplazo de archivo croquis
		else if(request.getParameter("cancelarReemplazo") != null){
			LogSISPE.getLog().info("se cierra el popUp de remplazo de archivo croquis");
			session.removeAttribute(SessionManagerSISPE.POPUPAUX);
		}
		//IOnofre. Remplaza de archivo croquis anteriormente ingresado
		else if(request.getParameter("reemplazarArchivo") != null){
			LogSISPE.getLog().info("se reemplaza el archivo croquis anteriormente ingresado");
			ManejarArchivo manejarArchivo = new ManejarArchivo();
			manejarArchivo.remplazarArchivoCroquisAntIngresado(request, session, formulario);
			salida = "desplegar";
		}

		
		//----- caso por omisi\u00F3n -----
		else{
			editarEntrega = false;
			session.setAttribute(EDITAR_ENTREGA, false);
			LogSISPE.getLog().info("entro al else");
			
			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
			
			// Plantilla de b\u00FAsqueda
			ParametroID parametroID = new ParametroID();
			parametroID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			parametroID.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD"));
			
			// Se obtiene el valor de VALORPARAMETRO de SCSPETPARAMETRO
			ParametroDTO parametroValorDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametroID);
			String valorParametro = parametroValorDTO.getValorParametro();
			LogSISPE.getLog().info("Valor par\u00E1metro completo: {}",valorParametro);
			
			//obtener la cantidad minima para realizar la configuracion de canastos especiales y de catalogo
			ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
			Integer codMinCanastosDomicilio = Integer.parseInt(parametroMinCanastas.getValorParametro());
			
			session.setAttribute(CANTIDAD_CANASTOS_ENTREGAS_BODEGA,codMinCanastosDomicilio);
			
			// Se sube a session el valor anterior
			session.setAttribute(VAR_VALOR_PARAMETRO_TOTAL, valorParametro);
			
			//se obtiene el valor del par\u00E1metro que contiene el limite inferior para que una entrega se obligatoriamente  con transito
			session.setAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO, WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.totalReservaCDObligatoriedadBodegaTransito", request).getValorParametro());
			//---------------------------------------------------------------------------------------------------------------------------
			
			//variable de sesion para cambiar el metas
			session.setAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"),"ok");
			
			session.removeAttribute(EXISTENCAMBIOS);//variable de sesion para saber si existieron cambios antes de salir
			
			final String lunes = MessagesWebSISPE.getString("mensaje.dia.semana.lunes");
			final String martes = MessagesWebSISPE.getString("mensaje.dia.semana.martes");
			final String miercoles = MessagesWebSISPE.getString("mensaje.dia.semana.miercoles");
			final String jueves = MessagesWebSISPE.getString("mensaje.dia.semana.jueves");
			final String viernes = MessagesWebSISPE.getString("mensaje.dia.semana.viernes");
			final String sabado = MessagesWebSISPE.getString("mensaje.dia.semana.sabado");
			final String domingo = MessagesWebSISPE.getString("mensaje.dia.semana.domingo");
			
			//cargo los dias de la semana
			String[] orden1 = {lunes, martes, miercoles, jueves, viernes, sabado, domingo};
			String[] orden2 = {domingo, lunes, martes, miercoles, jueves, viernes, sabado};
			String[] dias=null;
			//si el primer dias es el 1 la semana empieza en domingo caso contrario en lunes
			if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.primerDiaSemana").equals(estadoActivo)){
				dias=orden2;
			}else{
				dias=orden1;
			}
			//guardo en sesion los dias de la semana
			session.setAttribute(ORDENDIAS,dias);
			if(session.getAttribute(CONFIGURACION)== null){
				//Obtiene la configuracion para las entregas
				obtenerConfiguracionEntrega(request, errors);
			}
			
			if(session.getAttribute(OPCIONLUGARENTREGA)==null){
				formulario.setOpLugarEntrega(CONTEXTO_ENTREGA_MI_LOCAL);
				formulario.setOpTipoEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"));
				formulario.setOpStock(STOCK_ENTREGA_LOCAL);
			}else{
				formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
				formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
				formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
			}
			
			if(session.getAttribute(VISTALOCALORIGEN)==null){
				Integer local = 0;
				if(formulario.getLocalResponsable()!=null){
					//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
					//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
					local = Integer.parseInt(formulario.getLocalResponsable().split("-")[0].trim());
				}
				VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
				vistaLocalDTO.getId().setCodigoLocal(local);
				vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
				LogSISPE.getLog().info("locales: {}" , vistaLocalDTOCol.size());
				VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
				session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN, vistaLocal);
				//por defecto el local destino es el mismo de origen
				session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO, vistaLocal);
			}
			//Saco dos copia de la configuracion de los calendarios
			if(session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL)!=null){
				LogSISPE.getLog().info("si tiene configuracion del calendario");
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL);
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalAux=calendarioConfiguracionDiaLocal.copiar();
				session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalAux);
			}
			
			/*************** Uso una sesion auxiliar para el detalle pedido, calendarioConfiguracionDialLocal, costos y direcciones *******************/
			//Clono el detalle del pedido
			Collection<DetallePedidoDTO> detallePedidoDTOColAux=new ArrayList<DetallePedidoDTO>();
			
			/*List<DetallePedidoDTO> detallePedidoDTOCols=(List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);		
			//asigna fecha de despacho bodega
			for(DetallePedidoDTO detallePedido :  detallePedidoDTOCols){
				for(EntregaDetallePedidoDTO entregaDetallePedidoDTO:  detallePedido.getEntregaDetallePedidoCol()){
					if(entregaDetallePedidoDTO.getFechaRegistroDespacho()==null && entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))
							&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()!=null
							&& session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
							&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega().getTime() <= UtilesSISPE.obtenerFechaActual().getTime()){
						entregaDetallePedidoDTO.setFechaRegistroDespacho(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());	
					}		
				}			
			}
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedidoDTOCols);	*/
			
			List<DetallePedidoDTO> detallePedidoDTOCol=(List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);	
			
			Long sumaCantSolCD = 0L;
			
			for (int i = 0; i < detallePedidoDTOCol.size(); i++){
				//contDetalle = i;
				//int contEntrega = 0;
				
				DetallePedidoDTO detallePedidoDTO = detallePedidoDTOCol.get(i);
				DetallePedidoDTO detallePedidoDTOAux = detallePedidoDTO.clone();
				
				Collection<EntregaDetallePedidoDTO> entregaPedidoCol = new ArrayList<EntregaDetallePedidoDTO>();
				
				if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
					
					for(EntregaDetallePedidoDTO entregaDetallePedido:detallePedidoDTO.getEntregaDetallePedidoCol()){
						EntregaDetallePedidoDTO entregaDetallePedidoAux = SerializationUtils.clone(entregaDetallePedido); 
						entregaPedidoCol.add(entregaDetallePedidoAux);
						
						//asigna fecha de despacho bodega
						if((entregaDetallePedidoAux.getFechaRegistroDespacho()==null && entregaDetallePedidoAux.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))
								&& entregaDetallePedidoAux.getEntregaPedidoDTO().getFechaDespachoBodega()!=null
								&& session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
								&& entregaDetallePedidoAux.getEntregaPedidoDTO().getFechaDespachoBodega().getTime() <= UtilesSISPE.obtenerFechaActual().getTime())){
							entregaDetallePedidoAux.setFechaRegistroDespacho(entregaDetallePedidoAux.getEntregaPedidoDTO().getFechaDespachoBodega());	
						}
						
						session.setAttribute(EXISTENENTREGAS, "ok");
												
						LogSISPE.getLog().info("**entregaAux.getNpValidarCheckTransito(): {}", entregaDetallePedidoAux.getEntregaPedidoDTO().getNpValidarCheckTransito());
						LogSISPE.getLog().info("**entregaAux.getNpValorCodigoTransito(): {}", entregaDetallePedidoAux.getEntregaPedidoDTO().getNpValorCodigoTransito());
						
						//si est\u00E1 habilitado el check de transito y es a domicilio
						if(!EntregaLocalCalendarioUtil.verificarArticuloPerecible(request, detallePedidoDTOAux.getArticuloDTO().getCodigoClasificacion())
								&& entregaDetallePedidoAux.getEntregaPedidoDTO().getNpValidarCheckTransito() != null && entregaDetallePedidoAux.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
								&& entregaDetallePedidoAux.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(CONTEXTO_ENTREGA_DOMICIO)
								){
							
							for(EntregaDetallePedidoDTO entregaDetalle:entregaDetallePedidoAux.getEntregaPedidoDTO().getEntregaDetallePedidoCol()){
								sumaCantSolCD = sumaCantSolCD + entregaDetalle.getCantidadDespacho();
							}
						}
					}
				}
				
				detallePedidoDTOAux.setEntregaDetallePedidoCol(entregaPedidoCol);
				detallePedidoDTOColAux.add(detallePedidoDTOAux);
			}
			//sube a session la sumatoria
			session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
			
			//verifica si debe aplicar obligatoriedad de transito en el caso de tener previamente configurado las entregas
			if(!CollectionUtils.isEmpty(detallePedidoDTOColAux)){
				//verificar si el detalle del pedido tiene articulos perecibles y ya tiene configurada una entrega
				for (DetallePedidoDTO detallePedidoDTOAux : (Collection<DetallePedidoDTO>)detallePedidoDTOColAux){
					if(!EntregaLocalCalendarioUtil.verificarArticuloPerecible(request, detallePedidoDTOAux.getArticuloDTO().getCodigoClasificacion())
							&& !CollectionUtils.isEmpty(detallePedidoDTOAux.getEntregaDetallePedidoCol())){
						for(EntregaDetallePedidoDTO dtoEntDetPed: (Collection<EntregaDetallePedidoDTO>)detallePedidoDTOAux.getEntregaDetallePedidoCol()){
									//si est\u00E1 habilitado el check de transito
									if(sumaCantSolCD >= Long.parseLong((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO))
											&& dtoEntDetPed.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(CONTEXTO_ENTREGA_DOMICIO)
											&& dtoEntDetPed.getEntregaPedidoDTO().getNpValidarCheckTransito() != null && dtoEntDetPed.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(estadoActivo)){
										
										dtoEntDetPed.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
										
									}
								}
					}
				}
			}
			
			//se reasigna el valor de los checks de transito
			formulario.setCheckTransitoArray((String [])session.getAttribute(EntregaLocalCalendarioUtil.CHECKS_ENTREGAS_TRANSITO));
			
			session.setAttribute(DETALLELPEDIDOAUX, detallePedidoDTOColAux);
			//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
			EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOColAux, request);
			/**********************************************************************************/
			LogSISPE.getLog().info("direcciones: {}" , session.getAttribute(DIRECCIONESAUX));
			
			//Clono las direcciones
			if(session.getAttribute(DIRECCIONES)!=null){
				Collection<DireccionesDTO> direccionesDTOColAux=new ArrayList<DireccionesDTO>();
				Collection<DireccionesDTO> direccionesDTOCol=(Collection<DireccionesDTO>)session.getAttribute(DIRECCIONES);
				for (DireccionesDTO direccionesDTO:direccionesDTOCol) {
					DireccionesDTO direccionesDTOAux = direccionesDTO.clone();
					direccionesDTOColAux.add(direccionesDTOAux);
				}
				session.setAttribute(DIRECCIONESAUX, direccionesDTOColAux);
			}
			
			LogSISPE.getLog().info("costos: {}" , session.getAttribute(COSTOENTREGAAUX));
			//Clono los costos
			if(session.getAttribute(COSTOENTREGA)!=null){
				Collection<CostoEntregasDTO> costoEntregasDTOColAux = new ArrayList<CostoEntregasDTO>();
				Collection<CostoEntregasDTO> costoEntregasDTOCol=(Collection<CostoEntregasDTO>)session.getAttribute(COSTOENTREGA);
				for (CostoEntregasDTO costoEntregasDTO: costoEntregasDTOCol) {
					costoEntregasDTOColAux.add(costoEntregasDTO.clone());
				}
				session.setAttribute(COSTOENTREGAAUX, costoEntregasDTOColAux);
			}
			
			//TODO validar para que sirve este codigo y la variable de sesion
			if(session.getAttribute(SECDIRECCIONES)!=null){
				Integer contDir=(Integer)session.getAttribute(SECDIRECCIONES);
				int contAux = contDir.intValue();
				session.setAttribute(SECDIRECCIONESAUX, new Integer(contAux));
			}
			
			String entidadResponsableLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			String entidadResponsableBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
			
			//se obtiene el par\u00E1metro de la clasificaci\u00F3n que obliga a que la entidad responsable sea el CD
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionResponsabilidadCD", request);
			String [] codigosClasificacion = null;
			if(parametroDTO.getValorParametro()!=null){
				codigosClasificacion = parametroDTO.getValorParametro().split(",");
			}
			
			boolean entResEsBOD = false;
			//se verifica el detalle del pedido
			if(codigosClasificacion != null){
				Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				for(int i=0; i<codigosClasificacion.length;i++){
					if(!ColeccionesUtil.simpleSearch("articuloDTO.codigoClasificacion", codigosClasificacion[i], colDetallePedidoDTO).isEmpty()){
						entResEsBOD = true;
						break;
					}
				}
			}

			if(entResEsBOD){
				//si la entidad resulta ser BODEGA se crea una nueva variable de sesi\u00F3n para desactivar la opci\u00F3n de entidad responsable LOCAL
				session.setAttribute(DESACTIVAR_ENT_RES_LOC, "ok");
			}else{
				session.removeAttribute(DESACTIVAR_ENT_RES_LOC);
			}
			
			/*-------------------------------------------------------------------------------*/
			//Por defecto la entrega es a locales
			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
			/*-------------------------------------------------------------------------------*/
			
			//selecciono las opciones por defecto
			if(session.getAttribute(NOMBREENTIDADRESPONSABLE)==null 
					|| (session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(entidadResponsableLocal))){

				//se verifica si la entidad responsable resulta ser la Bodega
				if(entResEsBOD){
					//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
					formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
					session.setAttribute(NOMBREENTIDADRESPONSABLE,entidadResponsableBodega);
				}
			}else if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null 
					&& session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(entidadResponsableBodega)){
				//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
				formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
			}
			
			/**********************Por defecto la fecha minima de entrega es la fecha de entrega************/
			LogSISPE.getLog().info("fecha inicial entrega minima: {}" , session.getAttribute(CotizarReservarAction.FECHA_ENTREGA));
			
			//Guardo la fecha minima de entrega
			formulario.setBuscaFecha(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES).toString());
			session.setAttribute(FECHABUSQUEDA, ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
			
			//por defecto la fecha de entrega es igual a la fecha minima de entrega es la actual
			session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
			
			/**********************Elimina variables de session***************************/
			session.removeAttribute(LOCALID);//id del local seleccionado
			session.removeAttribute(DIASELECCIONADO);//dia de despacho seleccionado en el calendario
			session.removeAttribute(CALENDARIODIALOCALCOL);//calendario
			session.removeAttribute(VISTALOCALCOL);//locales
			session.removeAttribute(CALENDARIODIALOCAL);//CD del dia seleccionado en el calendario
			session.removeAttribute(FECHAMINIMA);//fecha minima de despacho a locales
			session.removeAttribute(FECHAMAXIMA);//fecha maxima de despacho a locales
			session.removeAttribute(MES_ACTUAL_CALENDARIO);
			session.removeAttribute(SECTORSELECCIONADO);//indice local seleccionado
			session.removeAttribute(DIRECCION);
			session.removeAttribute(EXISTELUGARENTREGA);
			session.removeAttribute(SELECCIONARLOCAL);
			session.removeAttribute(SELECCIONARCALENDARIO);
			session.removeAttribute(HABILITARCANTIDADENTREGA);
			session.removeAttribute(HABILITARCANTIDADRESERVA);
			session.removeAttribute(HABILITARBOTONACEPTAR);
			session.removeAttribute(HABILITARDIRECCION);
			session.removeAttribute("ventanaLocalEntrega");//elimina la variable que abre la ventana de seleccion de local de entrega
			
			//PASO UNO DE LA AYUDA
			session.setAttribute(CONFIGURACIONCARGADA, "ok");
			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
			session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
			
			// Objetos para construir los tabs, si fue modificacion de reserva
			beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
			
			if((String)session.getAttribute(EXISTENENTREGAS)==null){
				LogSISPE.getLog().info("--- abrir Popup configuracion si no existen aun entregas configuradas---");
				// Objetos para construir los tabs, si fue modificacion de reserva						
				request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
				request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
				instanciarVentanaOpcionesConfiguracion(request);
				session.setAttribute(MOSTRAR_POPUPTAB, "ok");
				session.removeAttribute(HABILITARCANTIDADENTREGA);
				session.removeAttribute(HABILITARCANTIDADRESERVA);
				session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
				session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
				session.removeAttribute(PASOSPOPUP);
				request.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
				session.removeAttribute( POSICIONDIVCONFENTREGAS);
				session.removeAttribute(DIRECCIONESAUX);
				regresarvaloresInicialesExistenEntregas(session);
				
				formulario.setOpElaCanEsp(null);
				formulario.setOpLugarEntrega(null);
				formulario.setOpTipoEntrega(null);
				formulario.setOpStock(null);
				
				//para mostrar opcion de canastos especiales
				Collection<DetallePedidoDTO> detallePedidoDTOColPopup=(Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				////formulario.setOpElaCanEsp(null);
				
				parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
				String codClaRecetasNuevas = parametroDTO.getValorParametro();
				
				parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
				Integer codMinCanastosEspeciales = Integer.parseInt(parametroMinCanastas.getValorParametro());
				
				if(CotizacionReservacionUtil.verificarExistenCanastasEspeciales(detallePedidoDTOColPopup, request,codClaRecetasNuevas,codMinCanastosEspeciales)){
					session.setAttribute(MOSTRAROPCIONCANASTOSESPECIALES, "ok");
					if(!CotizacionReservacionUtil.verificarCantidadCanastasEspeciales(detallePedidoDTOColPopup, request,codClaRecetasNuevas,codMinCanastosEspeciales, info)){
						session.setAttribute(BLOQUEAROPCIONENTREGADOMICILIO, "ok");
						info.add("entregasCompletas",new ActionMessage("info.bloqueo.entregas.domicilio"));
					}
				}else{
					session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
					session.removeAttribute(BLOQUEAROPCIONENTREGADOMICILIO);
				}
			}
			obtenerEstablecimientosEntregasSICMER(request, session);
			
			obtenerEntregas(session);
			
			//verfifico si todas las entregas estan configuradas
			Boolean entregasCompletas = true;
			if(detallePedidoDTOColAux!=null){
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOColAux){
					Long cantidaEstado = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
					Long cantidadTotal = 0L;
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
						cantidadTotal = cantidadTotal + entregaDetallePedidoDTO.getCantidadEntrega();
					}
					if(!cantidaEstado.equals(cantidadTotal)){
						entregasCompletas = false;
						break;
					}
				}
			}
			
			if(!entregasCompletas){
				info.add("camposEntrega",new ActionMessage("errors.detalle.camposEntrega"));
				saveErrors(request, errors);
			}
		}
		
		/**************************Refresca los valores de las variables de formulario***********************/		
		if(session.getAttribute(CALENDARIODIALOCALCOL)!=null){
			formulario.setCalendarioDiaLocal((Object[])session.getAttribute(CALENDARIODIALOCALCOL));
		}
		if(session.getAttribute(FECHABUSQUEDA)!=null){
			LogSISPE.getLog().info("setea fecha");
			if(session.getAttribute(EDITAFECHAMINIMA)!=null){
				formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
			}else{
				if(formulario.getOpStock()!=null && !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
					formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
				}else{
					formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
				}
			}
		}

		if(session.getAttribute(VISTALOCALDESTINO)!=null){
			VistaLocalDTO vistaLocalDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
			formulario.setIdLocalOSector(vistaLocalDTO.getId().getCodigoLocal().toString());
			formulario.setLocalOSector(vistaLocalDTO.getNombreLocal());
		}
		if(session.getAttribute(CALENDARIODIALOCAL)!=null){
			CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
			LogSISPE.getLog().info("setea fechaDespacho {}"+DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
			formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
		}
		
		//Grabo los mensajes para que se desplieguen
		saveMessages(request,messages);
		saveInfos(request,info);
		LogSISPE.getLog().info("error: {}" , error.size());
		LogSISPE.getLog().info("errors: {}" , errors.size());
		if(error.size()>0){
			LogSISPE.getLog().info("graba error");
			saveErrors(request,(ActionMessages)error);
		}
		if(errors.size()>0){
			LogSISPE.getLog().info("graba errors");
			saveErrors(request,errors);
		}
		saveWarnings(request,warnings);

		LogSISPE.getLog().info("salida EntregasCalendario4: {}" , forward);

		return mapping.findForward(forward);
		
	}

	public void obtenerEstablecimientosEntregasSICMER(
			HttpServletRequest request, HttpSession session) throws Exception {
		ParametroDTO parametroDTO;
		String establecimientos[]=null;
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.bloqueoEstablecimientosSICMER", request);
		if(parametroDTO.getValorParametro()!=null && !parametroDTO.getValorParametro().equals("")){
		establecimientos=parametroDTO.getValorParametro().split(",");
		}
		if(establecimientos!=null){
			Boolean bloquearSICMER=true;
			for(String establecimiento:establecimientos){
				if(establecimiento.equals(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento().toString())){
					bloquearSICMER=false;
					break;
				}
			}
			if(bloquearSICMER){
				session.setAttribute(ESTABLECIMIENTOS_SICMER, "ok");
			}else{
				session.removeAttribute(ESTABLECIMIENTOS_SICMER);	
			}
		}
	}

	/**
	 * @param session
	 * @return
	 */
	private Boolean verificarArticulosObsoletos(HttpSession session,ActionMessages info,CotizarReservarForm formulario) {
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		Boolean entregaPendienteObsoletos=Boolean.FALSE;
		Boolean entregaPendienteOtrosArticulos=Boolean.FALSE;
		for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
			if(detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))
					&& formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) {
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()!=detallePedidoDTO.getNpContadorEntrega().longValue()){
					detallePedidoDTO.setNpHabilitarEntregaObsoletos(Boolean.FALSE);
					entregaPendienteObsoletos=Boolean.TRUE;
				}
			}else {
				detallePedidoDTO.setNpHabilitarEntregaObsoletos(Boolean.TRUE);
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()!=detallePedidoDTO.getNpContadorEntrega().longValue()){
					entregaPendienteOtrosArticulos=Boolean.TRUE;
				}
			}
		}
		if(entregaPendienteObsoletos && entregaPendienteOtrosArticulos){
			info.add("articuloObsoleto",new ActionMessage("error.validacion.articulo.obsoleto"));
			return Boolean.TRUE;
		}
		if(entregaPendienteObsoletos && !entregaPendienteOtrosArticulos){
			info.add("articuloObsoleto",new ActionMessage("error.validacion.articulo.obsoletoPendientes"));
			return Boolean.FALSE;
		}
		if(!entregaPendienteObsoletos && entregaPendienteOtrosArticulos){
			return Boolean.TRUE;
		}else{
			return Boolean.TRUE;
		}
		
	}
	
	/**
	 * Abrir el popUp de configuraci&oacute;n de entregas
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaOpcionesConfiguracion(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Configuraci\u00F3n de entregas");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setEtiquetaBotonOK("Siguiente");
		popUp.setEstiloOK("siguienterD");
		//popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['div_pagina','pregunta','entregas','mensajes','confirmarLocalEntrega','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'seleccionarOpcion=ok', evalScripts:true});ocultarModal();");
		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true, evalScripts:true});mostrarModal();");
		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
		//popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
		popUp.setAncho(75D);
		popUp.setTope(-150D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
		session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
		popUp = null;
	}
	
	/**
	 * M&eacute;todo que procesa para regresar a los valores normales las entregas parciales
	 * @param session
	 */
	private void regresarvaloresInicialesExistenEntregas(HttpSession session) {
		// 
		long cantidad=0L;
		long cantidadEntregada=0L;
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
			for (EntregaDetallePedidoDTO entregaDetallePedidoDTO: detallePedidoDTO.getEntregaDetallePedidoCol()) {
				cantidadEntregada = cantidadEntregada + entregaDetallePedidoDTO.getCantidadEntrega();
			}
			cantidad = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - cantidadEntregada;
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantidad);
			cantidadEntregada = 0L;
		}
		
		//se enceran los nps de los detalles consolidados
		//se valida si se trata de un pedido consolidado
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
					
					for (EntregaDetallePedidoDTO entregaDetallePedidoDTO: detallePedidoDTO.getEntregaDetallePedidoCol()) {
						cantidadEntregada = cantidadEntregada + entregaDetallePedidoDTO.getCantidadEntrega();
					}
					cantidad = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - cantidadEntregada;
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantidad);
					cantidadEntregada = 0L;
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param error
	 * @param errors
	 * @param warnings
	 */
	private static void aceptaConfiguracionEntrega(HttpServletRequest request,CotizarReservarForm formulario,
			ActionErrors error, ActionMessages errors, ActionMessages warnings,ActionMessages info){
		
		HttpSession session = request.getSession();
		try{
			boolean flag = true; //variable que indica si se va a aceptar la configuracion o no
			//boolean bodegaCanastas = false; //bandera que me indica si la configuracion es para la bod(99)
			
			//Hago las validaciones de formulario de valores nulos y formatos
			if(formulario.validarTipoEntregas(error,request) == 0){
				
				formulario.mantenerValoresEntregas(request);
				Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
				Date fechaBusquedaAux =  DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
				//Si existe un tipo de entrega y esta editable el campo para fecha minima
				if(session.getAttribute(EXISTELUGARENTREGA)!=null && session.getAttribute(EDITAFECHAMINIMA)!=null){
					//si la fecha m\u00EDnima es menor a hoy
					if(fechaBusquedaAux.getTime() >= fechaMinimaReferencia.getTime()){
//						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
						Boolean existeAutorizacion = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue());
						//Verifica si la fecha minima de entrega fue modificada
						if((ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0
//								|| autorizacionDTO!=null)){
								|| existeAutorizacion)){
							
							LogSISPE.getLog().info("va a verificar la fecha minima de entrega");
							
							//jmena en la configuracion Inicializo fechaEntregaCliente
							if(formulario.getFechaEntregaCliente()==null || formulario.getFechaEntregaCliente().equals("")){
								formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES)));
							}
							
//							if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
//								bodegaCanastas = true;
//							}
							//Diferencia entre fecha de entrega y fecha minima de entrega
							long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
							LogSISPE.getLog().info("diferenciaEntregaBusca: {}" , diferenciaEntregaBusca);
							
							if((Boolean)request.getSession().getAttribute(EntregaLocalCalendarioAction.EDITAR_ENTREGA)){
								if(diferenciaEntregaBusca<0.0 && !formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
									formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
								}else{
									disminuirFechaMinima(formulario, warnings,session);
								}
							}else{
								if(diferenciaEntregaBusca<=0.0 && !formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
									LogSISPE.getLog().info("error en diferencia");
									errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString()));
								}else{
									disminuirFechaMinima(formulario, warnings,session);
								}
							}
						}
						//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
						else{
							errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
							//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
							session.removeAttribute(VISTALOCALCOL);
							formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
						}
					}
					//si la fecha ingresada es mayor a la de hoy 
					else{
						//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
						errors.add("buscaFecha",new ActionMessage("errors.fechaMinima"));
					}
				}
				
				//se valida la autorizacion de la bodega para elaborar los canastos en pedidos consolidados
				if(ConsolidarAction.esPedidoConsolidado(request)){
					
					if(request.getSession().getAttribute(ConsolidarAction.MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS) != null){
						//se valida si ya existe la autorizacion
						if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_CD_ELABORA_CANASTOS.longValue())){
							LogSISPE.getLog().info("pedido consolidado sin autorizacion");
							errors.add("autorizacionNecesaria",new ActionMessage("errors.autorizacion.bodega.elabora.canastos.requerida"));
						}
					}
				}
				
				if(errors.size()==0){
					
					//verificar que el calendario no este en nulo para deseleccionar los d\u00EDas anterirmente seleccionados
					if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null && !editarEntrega){
						Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
						//jmena verifico de que tipo es
						if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)!=null && session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO) instanceof Integer){
							//diaSeleccionado=((Integer)session.getAttribute(DIASELECCIONADO)).intValue();Aqui
							int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)));
							//Recupero el dia seleccionado anteriormente
							CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
							calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
						}else if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX)!=null && session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX) instanceof String){
							int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX)));
							//Recupero el dia seleccionado anteriormente
							CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
							calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
							session.removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO);
							session.removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX);
						}
					}
					
					
					String tomarStock =(String)session.getAttribute(STOCKENTREGAAUX);
					
					//Pregunto cual es la entidad responsable seleccionada, si es la bodega hago las validaciones para saber si se puede seleccionar
					if(formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
						//Valido la entidad responsable
						ArrayList<DetallePedidoDTO> detalleReservacion = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
						PedidoDTO consultaPedidoDTO = new PedidoDTO();
						consultaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						consultaPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
						consultaPedidoDTO.setNpAutorizacionesPedidoCol((Collection<AutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_PEDIDO_COL));
						String fechaMinimaEntregaCD = (String)session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES);
						consultaPedidoDTO.setFechaMinimaEntrega(new Timestamp(WebSISPEUtil.construirFechaCompleta(fechaMinimaEntregaCD, 0, 0, 0, 0, 0, 0)));
						
						//si es pedido consolidado, se agrega al pedido actual los otros pedidos consolidados
						//para validar la entidad responsable con el grupo de consolidados
						if(ConsolidarAction.esPedidoConsolidado(request)){
							
							ArrayList<DetallePedidoDTO> detalleReservacionConsolidado = new ArrayList<DetallePedidoDTO>(detalleReservacion); 
							detalleReservacionConsolidado.addAll(ConsolidarAction.obtenerDetallePedidoElaboraCD(request, detalleReservacion.iterator().next().getId().getCodigoPedido()));
							detalleReservacion = new ArrayList<DetallePedidoDTO>(detalleReservacionConsolidado);
						}
						
						LogSISPE.getLog().info("**fecha minima:** {}" , consultaPedidoDTO.getFechaMinimaEntrega());
						//se llama al m\u00E9todo que verifica cual es la entidad responsable
						SessionManagerSISPE.getServicioClienteServicio().transValidarEntidadResponsable(consultaPedidoDTO,detalleReservacion,ConstantesGenerales.ENTIDAD_RESPONSABLE_BODEGA,ConstantesGenerales.TIPO_ENTREGA_PARCIAL);
						
						detalleReservacion = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
						LogSISPE.getLog().info("LA ENTIDAD RESPONSABLE ES:{} ", consultaPedidoDTO.getEntidadResponsable());
						
						if(consultaPedidoDTO.getNpMsjErrorEntidadResponsable()==null){
							session.removeAttribute(ENTIDADRESPONSABLELOCAL);
							if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
								String fechaMinEntrega = session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString();
								//Fecha minima desde parametro
								Date fechaRefMinima =  DateManager.getYMDDateFormat().parse(fechaMinimaEntregaCD);
								//Fecha minima subida a session
								Date fechaRefEntrega =  DateManager.getYMDDateFormat().parse(fechaMinEntrega);
								//se valida la fecha de entrega 
								if(fechaBusquedaAux.getTime() >= fechaRefEntrega.getTime() && fechaBusquedaAux.getTime() <= fechaRefMinima.getTime() ){
									session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
								}else{
									//se inicializa nuevamnete la fecha m\u00EDnima de entrega
									formulario.setBuscaFecha(fechaMinEntrega);
								}
								sumarDiasParaProducirCanastosEspeciales(request,formulario,info,Boolean.TRUE);
							}
							session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
						}else{
							errors.add("entidadResponsable", new ActionMessage("errors.gerneral",consultaPedidoDTO.getNpMsjErrorEntidadResponsable() + ". LA CONFIGURACION NO HA SIDO ACEPTADA"));
							flag=false;
							//Subo a session la configuracion
							session.setAttribute(LUGARENTREGA,formulario.getOpLugarEntrega());
							session.setAttribute(TIPOENTREGA,formulario.getOpTipoEntrega());
							session.setAttribute(STOCKENTREGA,formulario.getOpStock());
							session.setAttribute(STOCKENTREGAAUX,formulario.getOpStock());
						}
						consultaPedidoDTO = null;
					}else{
						if(formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) 
								&& !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))){
							session.setAttribute(ENTIDADRESPONSABLELOCAL, "ok");
						}else{
							session.removeAttribute(ENTIDADRESPONSABLELOCAL);
						}
					}
					//Si la configuracion fue aceptada
					if(flag){
						obtenerConfiguracionEntregas(formulario, request, errors,warnings);
						//si no se va a seleccionar local de entrega por defecto el local destino es igual al local origen
						if(session.getAttribute(SELECCIONARLOCAL)==null){
							if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
								if(!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_OTRO_LOCAL)){
									EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
								}else{
									session.setAttribute(SELECCIONARLOCAL,"ok");
								}
							}else{
								VistaLocalDTO vistaLocalOrigenDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALORIGEN);
								LogSISPE.getLog().info("local origen: {}" , vistaLocalOrigenDTO.getId().getCodigoLocal());
								session.setAttribute(VISTALOCALDESTINO, vistaLocalOrigenDTO);
							}
						}
						//Si se va tomar todo de bodega setear el maximo numero de articulos que se pueden pedir a bodega
						if(formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))){
							LogSISPE.getLog().info("va a tomar todo de bodega");
							
							//se va a mostrar calendario por hora
							if( (formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO))
								&& (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
										|| formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
								session.setAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS, Boolean.TRUE);
							}else{
								session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
							}
								
							//recupero el detalle del pedido
							Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
							for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
								if(detallePedidoDTO.getNpContadorEntrega()!=null){
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoDTO.getNpContadorEntrega().longValue()));
								}else{
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()));
								}
							}
						}
						//Sino se setea 0 pero solo en caso de que no se hayan ingreado cantidades anteriormente o que la configuracion
						//anterior no haya sido parcial a bodega
						else{
							LogSISPE.getLog().info("-----tomarStock---- {}" , tomarStock);
							//recupero el detalle del pedido
							if(tomarStock!=null && !tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
								
								//se va a mostrar calendario por hora
								if( (formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO))
									&& (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
											|| formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
									session.setAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS, Boolean.TRUE);
								}else{
									session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
								}
								
								Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
								for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
								}
							}
							else if(tomarStock==null){
								Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
								for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
								}
							}
						}
						/*************Carga el calendario en caso de ser necesario*/
						boolean loc=true;//variable que se usa para reconocer cuando se ha ingresado un local valido
						VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
						LogSISPE.getLog().info("*****local destino***** {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
						//Si esta activo el combo para seleccionar los locales 
						if(session.getAttribute(SELECCIONARLOCAL)!=null ){
							String local=null;
							//si fue seleccionado un local desde el combo
							if(formulario.getListaLocales()!=null && !formulario.getListaLocales().equals("")){
								LogSISPE.getLog().info("local seleccionado: {}" , formulario.getListaLocales());
								local=formulario.getListaLocales();
								formulario.setLocal(local);
							}	
							//si fue ingresado un local desde la caja de textos
							else if(formulario.getLocal()!=null && !formulario.getLocal().equals("")){
								LogSISPE.getLog().info("ingreso un local");
								local=formulario.getLocal();
								formulario.setListaLocales(local);
							}	
							else{
								local=((VistaLocalDTO)session.getAttribute(VISTALOCALORIGEN)).getId().getCodigoLocal().toString();
								formulario.setLocal(local);
								formulario.setListaLocales(local);
							}
							//si el local seleccionado o ingresado es distinto al local de destino
							if(errors.size()==0 && !local.equals(vistaLocalDestinoDTO.getId().getCodigoLocal().toString()) || session.getAttribute(CALENDARIODIALOCALCOL)==null){
								//cargo el local destino
								loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,local);
								LogSISPE.getLog().info("encontro al local: {}" , loc);
							}
						}
						//Si el local fue encontrado
						if(loc){
							//Obtengo el local destino
							vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//							formulario.setLocalResponsable(vistaLocalDestinoDTO.getDireccionLocal());
//							formulario.setLocalResponsable(vistaLocalDestinoDTO.getId().getCodigoLocal()+ " " + MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken") + " " + vistaLocalDestinoDTO.getNombreLocal());
							LogSISPE.getLog().info("***LOCAL DESTINO***: {}" ,vistaLocalDestinoDTO.getId().getCodigoLocal());
							//En los casos donde deba haber una fecha de despacho se obtiene el calendario
							if(session.getAttribute(SELECCIONARCALENDARIO)!=null || ((formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_MI_LOCAL)) && formulario.getOpStock()!=null && !formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL))){
								LogSISPE.getLog().info("va a desplegar el calendario");
								LocalID localID=new LocalID();
								localID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
								localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
//								if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
//										formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
								if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
									localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
									loc=EntregaLocalCalendarioUtil.buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
									LogSISPE.getLog().info("encontro al local: {}" , loc);
								}else{
									localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
								}
								//Cargo el calendario	
								if (session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
									obtenerLocalCalendarioPorSemana(request, localID, errors, formulario);
								}
								else{
									obtenerLocal(request, localID, errors, formulario);
								}
								
								session.removeAttribute(DIASELECCIONADO);
								session.removeAttribute(CALENDARIODIALOCAL);
								session.removeAttribute(DIRECCION);
							}
						}
						//Si el local ingresado no existe
						else{
							errors.add("errorLocal",new ActionMessage("errors.local"));
							if (formulario.getOpStock()!=null && formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL)) {
								session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
							}
						}
						//Cargo el lugar desde donde se va a realizar la entrega al domicilio
						VistaLocalDTO vistaLocal=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
						LogSISPE.getLog().info("va a guardar el local de entrega");
						//if(session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null){
						if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && 
								!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
							session.setAttribute(LUGARENTREGADOMICILIO,"Local " + vistaLocal.getId().getCodigoLocal());
							session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
						}	
						else{
							session.setAttribute(LUGARENTREGADOMICILIO,"el CD ");
						}
						
						LogSISPE.getLog().info("local de entrega: {}" ,session.getAttribute(LUGARENTREGADOMICILIO).toString());
						LogSISPE.getLog().info("lugarEnt: {}" , formulario.getOpLugarEntrega());
						LogSISPE.getLog().info("tipoEnt: {}" , formulario.getOpTipoEntrega());
						LogSISPE.getLog().info("stockEnt: {}" , formulario.getOpStock());
						//Subo a session la configuracion
						session.setAttribute(LUGARENTREGA,formulario.getOpLugarEntrega());
						session.setAttribute(TIPOENTREGA,formulario.getOpTipoEntrega());
						session.setAttribute(STOCKENTREGA,formulario.getOpStock());
						session.setAttribute(STOCKENTREGAAUX,formulario.getOpStock());
					}
				}
			}
			formulario.setDirecciones(null);
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}

	public static void disminuirFechaMinima(CotizarReservarForm formulario,
			ActionMessages warnings, HttpSession session) throws Exception {
		//si la fecha ingresada es menor a la fecha minima de entrega y existe una autorizacion o la entidad responsable es local 
		if(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)))>0){
			LogSISPE.getLog().info("entra a cambiar la fecha minima de entrega");
			LogSISPE.getLog().info("la fecha minima es menor pero hay autorizacion: {}" , formulario.getBuscaFecha());
			session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
			session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
			warnings.add("fechaMinima",new ActionMessage("warnings.fechaMinima"));	
		}
	}

	/**
	 * @param request
	 * @param formulario
	 * @param info
	 * @param session
	 * @param fechaMinEntrega
	 * @throws Exception
	 * @throws SISPEException
	 */
	public static void sumarDiasParaProducirCanastosEspeciales(
			HttpServletRequest request, CotizarReservarForm formulario,
			ActionMessages info, Boolean consultarFecha)
			throws Exception, SISPEException {
		HttpSession session= request.getSession();
		String fechaMinEntrega="";
		//verifico si existen canastos especiales para sumar los dias extras que necesita la bodega de canstos para la produccion
		if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
			if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA.longValue()) || !consultarFecha){
				//sacamos del parametro el valor en dias a sumar de la fecha de entrega.
				ParametroDTO consultaParametroDTO = new ParametroDTO();
				consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diazARestarAFechaDespachoAvisoProduccion"));
				Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
				
				String rangoDiasProducirCanastosEspeciales = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
				if(rangoDiasProducirCanastosEspeciales!=null){
					int diasProducir = Integer.parseInt(rangoDiasProducirCanastosEspeciales);
					LogSISPE.getLog().info("diasParaProducir: {}" , diasProducir);
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
					//para obtener la fecha m\u00EDnima de entrega se suman los d\u00EDas del par\u00E1metro
					Timestamp fechaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, diasProducir);
					//se formatea la fecha de entrega
					fechaMinEntrega = simpleDateFormat.format(fechaEntrega);
					formulario.setFechaEntrega(fechaMinEntrega);
					BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
					if(formulario.getOpTipoEntrega()!=null &&  formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))){
							if(beanSession.getPaginaTabPopUp().getCantidadTabs()==3 && (String)session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null && !consultarFecha){
								info.add("fechaMinimaCanastosEspeciales", new ActionMessage("info.fecha.minima.entrega.paraProducir.canastosEspeciales",diasProducir ));
							}
					}else{
						if(formulario.getOpTipoEntrega()!=null && !consultarFecha){
							info.add("fechaMinimaCanastosEspeciales", new ActionMessage("info.fecha.minima.entrega.paraProducir.canastosEspeciales",diasProducir ));
						}
					}
				}
				session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getFechaEntrega());
				//ponemos en null la fecha de entrega al cliente para que tome la fecha de entrega seteada anteriormente
				formulario.setBuscaFecha(fechaMinEntrega);
			}
		}
	}
	
	/**
	 * Funci&oacute;n que realiza la configuraci&oacute;n de la pantalla de entregas de acuerdo a las opciones seleccionadas
	 * @param formulario
	 * @param request
	 * @param errors
	 * @param warnings 
	 */
	public static void obtenerConfiguracionEntregas(CotizarReservarForm formulario, HttpServletRequest request, ActionMessages errors, ActionMessages warnings){
		HttpSession session=request.getSession();
		//Borro las variables de session
		session.removeAttribute(SELECCIONARLOCAL);
		session.removeAttribute(SELECCIONARCALENDARIO);
		session.removeAttribute(HABILITARCANTIDADENTREGA);
		session.removeAttribute(HABILITARCANTIDADRESERVA);
		session.removeAttribute(HABILITARBOTONACEPTAR);
		session.removeAttribute(HABILITARDIRECCION);
		//session.removeAttribute(DIASELECCIONADO);//Si se quiere que la parte de direcciones no desaparezca borrar esta linea
		session.removeAttribute(CALENDARIODIALOCALCOL);
		//session.removeAttribute(COMBOSELECCIONCIUDAD);
		session.removeAttribute("siDireccion");

		//Variable para mostrar la seccion de configuracion de fechas, hora y local
		session.setAttribute(EXISTELUGARENTREGA, "ok");
		//AYUDA
		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1"));
		session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
		//Registro el lugar de entrega
		if((formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO))){
			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"));
		}
		//caso de entrega a domicilio desde local
		else if((formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL))){
			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
		} else {
			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
			session.removeAttribute(DIASELECCIONADO);
			formulario.setSeleccionCiudad("");
		}
		
		LogSISPE.getLog().info("lugar entrega: {}" , formulario.getOpLugarEntrega());
		LogSISPE.getLog().info("toma stock: {}" , formulario.getOpStock()); 
		//Caso en el que se debe habilitar la seleccion de locales
		if(!formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL) && 
				!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_MI_LOCAL) && !formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) && !formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL) ){
			LogSISPE.getLog().info("va a habilitar la seleccion de lo locales");
			//se realiza la consulta de los locales
			if(session.getAttribute(SessionManagerSISPE.COLECCION_LOCALES)==null)
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			session.setAttribute(SELECCIONARLOCAL, "ok");
			//borro el calendario
			session.removeAttribute(CALENDARIODIALOCALCOL);
		}
		//Caso en el que se debe habilitar la seleccion del calendario
		LogSISPE.getLog().info("OpStock: {}",formulario.getOpStock());
		
		if((!formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL))){
			LogSISPE.getLog().info("asigna variable de sesion que visualiza el calendario");
			session.setAttribute(SELECCIONARCALENDARIO, "ok");
			//jmena validacion para el calendario de bodega
			if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
				//AYUDA				
				if(( (formulario.getOpTipoEntrega()!=null &&  formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP) == null){
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				}
				else{
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				}
				if((String)session.getAttribute(PASOSPOPUP)!=null){
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				}
			}else{
				//AYUDA 
				if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP) == null ){					
					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				}
				else{					
					if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
							session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEntAux2.2"));
							session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
						}else{
							session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
							session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
						}
				}
				if((String)session.getAttribute(PASOSPOPUP)!=null){
					if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
						session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEntAux2.2"));
						session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
					}else{
						session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
						session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
					}
				}
			}
			session.removeAttribute(DIASELECCIONADO);
//			formulario.setSeleccionCiudad("");
		}
		//Caso en el que se debe habilitar las cantidades de entregas
		if(formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))){
			session.setAttribute(HABILITARCANTIDADENTREGA, "ok");
			//se obtiene el bean que contiene los campos genericos
			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			if(!beanSession.getPaginaTabPopUp().esTabSeleccionado(2)){
				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
			}
			session.setAttribute(PASOSPOPUP, "ok");
			
		}
		//Caso en el que se debe habilitar las cantidades de recepcion
		if(formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
			session.setAttribute(HABILITARCANTIDADRESERVA, "ok");
			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			if(!beanSession.getPaginaTabPopUp().esTabSeleccionado(2)){
				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
			}
			session.setAttribute(PASOSPOPUP, "ok");
		}
		
		//Caso en el que se habilita la seccion de direcciones
		LogSISPE.getLog().info("opLugarEntrega: "+formulario.getOpLugarEntrega());
		
		if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
			LogSISPE.getLog().info("la entrega es a domicilio");
			session.setAttribute(HABILITARDIRECCION, "ok");
			
			if((formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL))){
				LogSISPE.getLog().info("visualiza las direcciones");
				if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
					int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
					session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
				}else{
					session.setAttribute(DIASELECCIONADO, "ok");
				}
				formulario.setSeleccionCiudad("");
				//AYUDA
				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.1"));
				if(((formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) 
						|| (formulario.getOpStock() !=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) 
						&& (String)session.getAttribute(PASOSPOPUP)==null ){
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				}
				else{
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				}
			}
			
			//Si va a ser la entrega a domicilio desde el CD
			if(!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
				session.setAttribute(COMBOSELECCIONCIUDAD, "ok");
				if(session.getAttribute(VISTAESTABLECIMIENTOCIUDADLOCAL)==null){
					EntregaLocalCalendarioUtil.cargaCiudades(request, errors);
				}
				//AYUDA
				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePaso2.3.2"));
				//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				if(( (formulario.getOpTipoEntrega() !=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				}
				else{
					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
				}
			}
		}
		//Caso en el que se habilita el boton aceptar
		LogSISPE.getLog().info("opLugarEntrega: {}",formulario.getOpLugarEntrega());
		LogSISPE.getLog().info("opTipoEntrega: {}" , formulario.getOpTipoEntrega());
		
		if (!((formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL)) 
				|| (formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)))) {
			session.setAttribute(HABILITARBOTONACEPTAR, "ok");
		}
		
		try{
			
			long diferenciaEntregaBusca= ConverterUtil.returnDateDiff((String)session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_LOC_RES),formulario.getBuscaFecha());
			//obtenemos el parametro para sumar los dias y tener la fecha de entrega
			ParametroDTO parametroDTO= WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
			int numeroDias=1;
			if(parametroDTO!=null){
				numeroDias=Integer.valueOf(parametroDTO.getValorParametro()).intValue();
			}
			//Caso en que se solicita algo al CD
			if(!formulario.getOpStock().equals(STOCK_ENTREGA_LOCAL)){
				//jmena configura para el calendario de la bodega(99)
				if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
					session.setAttribute(EDITAFECHAMINIMA, "OK");
					if(formulario.getOpElaCanEsp() != null && !formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
						if(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)))!=0){
							formulario.setFechaEntregaCliente((String)session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES));
							//si el entrega a domicilio
							formulario.setBuscaFecha(formulario.getFechaEntregaCliente());
							session.setAttribute(FECHABUSQUEDA, ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
							formulario.setFechaEntrega(formulario.getFechaEntregaCliente());
							session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getFechaEntrega());
						}
					}
				}else{
					session.removeAttribute(EDITAFECHAMINIMA);
					if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
						if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_OTRO_LOCAL)){
							//si el responsable es el CD 
							session.setAttribute(EDITAFECHAMINIMA, "OK");
							Date fechaMinima=(Date)session.getAttribute(FECHABUSQUEDA);
							LogSISPE.getLog().info("fecha Minima: {}" , fechaMinima);
							formulario.setBuscaFecha(ConverterUtil.parseDateToString(fechaMinima));
						}else{
							//si el responsable es el CD 
							session.setAttribute(EDITAFECHAMINIMA, "OK");
							Date fechaMinima=(Date)session.getAttribute(FECHABUSQUEDA);
							LogSISPE.getLog().info("fecha Minima: {}" , fechaMinima);
							formulario.setBuscaFecha(ConverterUtil.parseDateToString(fechaMinima));
							formulario.setFechaEntregaCliente(formulario.getBuscaFecha());
						}
					}else{
						//siempre sera un dia mas a las fechas establecidas para poder pintar el calendario.
						if(diferenciaEntregaBusca>numeroDias){
							regresarFechaMinimaEntregaLocal(formulario,session, numeroDias);
						}else{
							Date fechaMinima=(Date)session.getAttribute(FECHABUSQUEDA);
							LogSISPE.getLog().info("fecha Minima: {}" , fechaMinima);
							formulario.setBuscaFecha(ConverterUtil.parseDateToString(fechaMinima));
						}
					}
				}
			}else{ //siempre sera un dia mas a las fechas establecidas para poder pintar el calendario.
				if(diferenciaEntregaBusca>numeroDias){
					regresarFechaMinimaEntregaLocal(formulario, session,numeroDias);
				}else{
					formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
					session.removeAttribute(EDITAFECHAMINIMA);
				}
			}
			if(GenericTypeValidator.formatDate(formulario.getFechaEntregaCliente(),MessagesWebSISPE.getString("formatos.fecha") , false)!=null){
				if(formulario.getFechaEntregaCliente()!=null && !formulario.getFechaEntregaCliente().equals("") 
						&& (ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente())<0.0)){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Calendar fechaEntrega = Calendar.getInstance();
					fechaEntrega.setTime(sdf.parse(formulario.getBuscaFecha()));
					fechaEntrega.add(Calendar.DAY_OF_MONTH,1);
					formulario.setFechaEntregaCliente(sdf.format(fechaEntrega.getTime()));
					if(!formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO)){
						warnings.add("fechaEntregaCliente",new ActionMessage("errors.rango.fechas","La fecha entrega cliente","la fecha m\u00EDnima de entrega"));
					}
				}
			}else{
				formulario.setFechaEntregaCliente(null);
				errors.add("fechaEntregaCliente",new ActionMessage("errors.formato.fecha","fecha entrega cliente"));
			}
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		
		session.removeAttribute(CONFIGURACIONCARGADA);
		
		//Guarda la ultima configuracion de la entrega seleccionada
		session.setAttribute(OPCIONLUGARENTREGA,formulario.getOpLugarEntrega());
		session.setAttribute(OPCIONTIPOENTREGA,formulario.getOpTipoEntrega());
		session.setAttribute(OPCIONSTOCK,formulario.getOpStock());
		session.setAttribute(OPCIONCANASTOSESPECIALES, formulario.getOpElaCanEsp());
		
		//Guardo la fecha de entrega al cliente en sesion
		session.setAttribute(FECHAENTREGACLIENTE, formulario.getFechaEntregaCliente());
	}

	/**
	 * @param formulario
	 * @param session
	 * @param numeroDias
	 * @throws ParseException
	 */
	public static void regresarFechaMinimaEntregaLocal(
			CotizarReservarForm formulario, HttpSession session, int numeroDias)
			throws ParseException {
		//verificamos que la fecha de busqueda esta sumada los dias para producir en bodega, con esto reestablecemos los valores cuando es la entrega normal sin canastos especiales
		Date fechaMinimaEntregaLocal = DateManager.getYMDDateFormat().parse((String)session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_LOC_RES));
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaMinimaEntregaLocal);
		LogSISPE.getLog().info("Suma los dias del parametro #41: {}",numeroDias);
		fechaCalendario.add(Calendar.DAY_OF_MONTH, numeroDias);
		formulario.setFechaEntregaCliente(DateManager.getYMDDateFormat().format(fechaCalendario.getTime()));
		
		//si el responsable es el local
		formulario.setBuscaFecha(formulario.getFechaEntregaCliente());
		session.setAttribute(FECHABUSQUEDA, ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
		session.removeAttribute(EDITAFECHAMINIMA);
		formulario.setFechaEntrega(formulario.getFechaEntregaCliente());
		session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getFechaEntrega());
	}
	
	
	/**
	 * obtiene los datos del local destino para cargar el calendario por semana
	 * @param request
	 * @param localID
	 * @param errors
	 * @param formulario
	 * @throws Exception
	 */
	public static void obtenerLocalCalendarioPorSemana(HttpServletRequest request,LocalID localID,
			ActionMessages errors,CotizarReservarForm formulario) throws Exception{
		
		request.getSession().setAttribute(LOCALID, localID);
		
		LogSISPE.getLog().info("fecha de busqueda: formulario.getFechaEntregaCliente() {}" , formulario.getFechaEntregaCliente());
		LogSISPE.getLog().info("fecha de busqueda: (String)(session.getAttribute(FECHAENTREGACLIENTE))() {}" , (String)(request.getSession().getAttribute(FECHAENTREGACLIENTE)));
		
		if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
			formulario.setFechaEntregaCliente((String)(request.getSession().getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
		}
		Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		
		LogSISPE.getLog().info("fecha de busqueda: {}" , mes);
		//jmena bandera que muestra o no la configuracion del local bodega.
		request.getSession().removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
		String rangoDias = (String)request.getSession().getAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO);
		if(rangoDias == null){
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO();
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
			
			rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
			request.getSession().setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
			consultaParametroDTO = null;
		}
		
		int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
		LogSISPE.getLog().info("parametro: {}" , parametro);
		
		//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaMinima);
		fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
		fechaMinima = fechaCalendario.getTime();
		
		//------ fmunoz -------
		//se obtiene el par\u00E1metro desde la base
		ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
		int canDiaFecMinDes = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
		Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, canDiaFecMinDes, 0, 0, 0, 0));
		
		//si la fecha m\u00EDnima es menor o igual a hoy
		if(fechaCalendario.getTimeInMillis() < fechaMinimaReferencia.getTime()){
			fechaMinima = fechaMinimaReferencia;
		}
		
		//Resto al dia de entrega un d\u00EDa para ver la fecha m\u00E1xima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMaxima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario1=new GregorianCalendar();
		fechaCalendario1.setTime(fechaMaxima);
		fechaCalendario1.add(Calendar.DAY_OF_MONTH,-1);
		fechaMaxima=fechaCalendario1.getTime();
		
		request.getSession().removeAttribute(CALENDARIODIALOCAL);
		request.getSession().removeAttribute(DIASELECCIONADO);
		
		//jmena verifico si es el calendario de la bod/canastos
		if(localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")) || localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))){
			
			//obtener el parametro para iniciar la produccion
			//se obtiene el par\u00E1metro desde la base
			consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diazARestarAFechaDespachoAvisoProduccion", request);
			int rangoDiasProducirCanastosEspeciales = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
			
			GregorianCalendar fechaMaximaDomicilio = new GregorianCalendar();
			fechaMaximaDomicilio.setTime(new Date());
			fechaMaximaDomicilio.add(GregorianCalendar.DAY_OF_MONTH,rangoDiasProducirCanastosEspeciales);
			fechaMaxima = new Date(fechaMaximaDomicilio.getTimeInMillis());
			fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
			//Inicializo la fecha de entrega null
			formulario.setFechaEntregaCliente(null);
			request.getSession().setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
		}
		
		long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(fechaMaxima));
		if(diferenciaEntregaBusca<0.0){
			LogSISPE.getLog().info("Mensaje rango de fechas con errores");
			errors.add("rangoFechas",new ActionMessage("errors.rangoInicialFinalFechasCalendario",parametro,canDiaFecMinDes));
		}else{
			obtenerCalendarioPorSemana(request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
		}
		
		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
		LogSISPE.getLog().info("fechaMaxima: {}" , fechaMaxima);
		request.getSession().setAttribute(FECHAMINIMA,fechaMinima);
		request.getSession().setAttribute(FECHAMAXIMA,fechaMaxima);		
		request.getSession().setAttribute(MES_ACTUAL_CALENDARIO, mes);
	}
	
	/**
	 * obtiene datos del calendario por semana
	 * @param request
	 * @param localID
	 * @param errors
	 * @param mes
	 * @param fechaMinima
	 * @param fechaMaxima
	 * @param formulario
	 * @param verCalendario
	 * @throws Exception
	 */
	public static void obtenerCalendarioPorSemana(HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechMin,
			Date fechMax,CotizarReservarForm formulario, Boolean verCalendario) throws Exception{
		
		try{
			Date fechaMinima = fechMin;
			Date fechaMaxima = fechMax; 
			
			LogSISPE.getLog().info("*****entra a cargar el calendario por semana*****");
			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
			
			HttpSession session=request.getSession();
			
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fechaMinima);
			
			calendario.add(Calendar.DAY_OF_WEEK, - calendario.get(Calendar.DAY_OF_WEEK)+1);
			fechaMinima = calendario.getTime();
			
			calendario.add(Calendar.DAY_OF_WEEK, 7);
			fechaMaxima = calendario.getTime();
			
			GregorianCalendar nuevaFecha = new GregorianCalendar();
			nuevaFecha.setTime(fechaMinima);
			nuevaFecha.add(GregorianCalendar.DAY_OF_WEEK, 7);
			fechaMaxima = nuevaFecha.getTime();
			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
			
			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
			List<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFechaSemana(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
			if(CollectionUtils.isNotEmpty(calendarioDiaLocalDTOCol)){
								
				Date fechaEntCliente = (Date)session.getAttribute(FECHABUSQUEDA); 
				
				for(Iterator<CalendarioDiaLocalDTO> it=calendarioDiaLocalDTOCol.iterator();it.hasNext();){
					CalendarioDiaLocalDTO calendarioActual = it.next();
					
					//deshabilito los dias inferiores a la fecha de entrega
					if(fechaEntCliente.after(calendarioActual.getId().getFechaCalendarioDia())){
						calendarioActual.setNpPuedeSeleccionar(Boolean.FALSE);
						calendarioActual.setNpEsDistintoMes(Boolean.FALSE);
					}
					LogSISPE.getLog().info("fecha "+calendarioActual.getId().getFechaCalendarioDia().toString());
					calendarioActual.setCalendarioHoraLocalCol(EntregaLocalCalendarioUtil.obtenerEntregasDisponiblesFecha(formulario, calendarioActual.getId().getFechaCalendarioDia(), localID, request));
				}
			}
						
			//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
			LogSISPE.getLog().info("minima: {}" , fechaMinima);
			LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
			Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
			session.setAttribute(CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
			session.setAttribute(CALENDARIODIALOCAL_PARCIAL_POR_HORAS, calendarioDiaLocalDTOOBJ);
			//subo a session el mes de busqueda
			session.setAttribute(MESSELECCIONADO,mes);
			//calculo cuantas semanas tiene el mes
			int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
			//int maximoSemanas = 1;
			LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
			//subo a sesion el numero de semanas
			session.setAttribute(NUMEROSEMANAS,new Integer(maximoSemanas));
			session.setAttribute(FECHAMINIMA, fechaMinima);
			session.setAttribute(FECHAMAXIMA, fechaMaxima);
			
		}catch(SISPEException e){
			LogSISPE.getLog().info("error al cargar calendario: {}" , e);  
			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
		}
	}
	
	/**
	 * obtiene los datos del local destino para cargar el calendario
	 * @param request
	 * @param localID
	 * @param errors
	 * @param formulario
	 * @throws Exception
	 */
	public static void obtenerLocal(HttpServletRequest request,LocalID localID,ActionMessages errors,CotizarReservarForm formulario) throws Exception{
		
		HttpSession session=request.getSession();
		session.setAttribute(LOCALID, localID);
		
		LogSISPE.getLog().info("fecha de busqueda: formulario.getFechaEntregaCliente() {}" , formulario.getFechaEntregaCliente());
		LogSISPE.getLog().info("fecha de busqueda: (String)(session.getAttribute(FECHAENTREGACLIENTE))() {}" , (String)(session.getAttribute(FECHAENTREGACLIENTE)));
		
		if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
		}
		Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		
		LogSISPE.getLog().info("fecha de busqueda: {}" , mes);
		//jmena bandera que muestra o no la configuracion del local bodega.
		session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
		String rangoDias = (String)session.getAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO);
		if(rangoDias == null){
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO();
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
			
			rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
			session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
			consultaParametroDTO = null;
		}
		
		int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
		LogSISPE.getLog().info("parametro: {}" , parametro);
		
		//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaMinima);
		fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
		fechaMinima = fechaCalendario.getTime();
		
		//------ fmunoz -------
		//se obtiene el par\u00E1metro desde la base
		ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
		int canDiaFecMinDes = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
		Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, canDiaFecMinDes, 0, 0, 0, 0));
		
		//si la fecha m\u00EDnima es menor o igual a hoy
		if(fechaCalendario.getTimeInMillis() < fechaMinimaReferencia.getTime()){
			fechaMinima = fechaMinimaReferencia;
		}
		
		//Resto al dia de entrega un d\u00EDa para ver la fecha m\u00E1xima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMaxima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario1=new GregorianCalendar();
		fechaCalendario1.setTime(fechaMaxima);
		fechaCalendario1.add(Calendar.DAY_OF_MONTH,-1);
		fechaMaxima=fechaCalendario1.getTime();
		
		session.removeAttribute(CALENDARIODIALOCAL);
		session.removeAttribute(DIASELECCIONADO);
		
		//jmena verifico si es el calendario de la bod/canastos
		if(localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
			GregorianCalendar fechaMaximaDomicilio = new GregorianCalendar();
			fechaMaximaDomicilio.setTime(new Date());
			fechaMaximaDomicilio.set(GregorianCalendar.MONTH, 12);
			fechaMaximaDomicilio.set(GregorianCalendar.DAY_OF_MONTH, 31);
			fechaMaxima = new Date(fechaMaximaDomicilio.getTimeInMillis());
			fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
			if(formulario.getOpLugarEntrega()!=null && formulario.getOpElaCanEsp()!=null && !(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)
					&& formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")))){
				//Inicializo la fecha de entrega null
				formulario.setFechaEntregaCliente(null);
			}
			request.getSession().setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//			request.getSession().getAttribute(BANDERA_CONFIGURA_CAL_BOD);
			//session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
		}
		
		long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(fechaMaxima));
		if(diferenciaEntregaBusca<0.0){
			LogSISPE.getLog().info("Mensaje rango de fechas con errores");
			errors.add("rangoFechas",new ActionMessage("errors.rangoInicialFinalFechasCalendario",parametro,canDiaFecMinDes));
		}else{
			EntregaLocalCalendarioUtil.obtenerCalendario(request, localID, errors, mes, fechaMinima, fechaMaxima, formulario, true);
//			//reestabelce el NpEsSeleccionado a su valor por defecto
//			Object[] calendarioDiaLocalDTOOBJ =	(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//			if(calendarioDiaLocalDTOOBJ!=null){
//				for(Object object : calendarioDiaLocalDTOOBJ){
//					CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)object;
//					if(calendarioDiaLocalDTO.getNpEsSeleccionado()){
//						calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.FALSE);
//					}
//				}
//				session.setAttribute(CALENDARIODIALOCALCOL, calendarioDiaLocalDTOOBJ);
//				formulario.setHorasMinutos(null);
//			}	
		}
		
		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
		LogSISPE.getLog().info("fechaMaxima: {}" , fechaMaxima);
		session.setAttribute(FECHAMINIMA,fechaMinima);
		session.setAttribute(FECHAMAXIMA,fechaMaxima);
		
	}
	
	
	/**
	 * Si se muestra el tab de configuraci&oacute;n parcial de entregas
	 * @param request
	 * @param tabHabilitados
	 * @return
	 */
	private String tabHabilitadosParciales(HttpServletRequest request){
		
		 String tabHabilitados = null;
		
		if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
			tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA);
		}
		else{
			if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA)!=null){
				tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA);
			}
		}
		return tabHabilitados;
	}
	
	/**
	 * Permite regresar a los valores iniciales cuando esta navegando en el popup de configuracion de entregas
	 * @param session
	 */
	private void regresarValoresIniciales(HttpSession session) {
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
			if(detallePedidoDTO.getEntregaDetallePedidoCol().size() == 0){
				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
			}
		}
	}
	
	/**
	 * Se crea el popUp y los tab's que se deben mostrar en la configuraci&oacute;n de entregas, con las acciones correspondientes en los botones
	 * @param session
	 * @param beanSession
	 * @param etiquetaOk
	 * @param etiquetaCancel
	 * @param nomPagOk
	 * @param nomPagCancel
	 * @param parOk
	 * @param paraCancel
	 * @param modal
	 * @param seccionMensajes
	 * @param numTab
	 * @param estiloOK
	 * @param estiloCANCEL
	 */
	private void navegacionPopUp(HttpSession session, BeanSession beanSession,String etiquetaOk,String etiquetaCancel,String nomPagOk,
			String nomPagCancel, String parOk,String paraCancel,String modal,String seccionMensajes,int numTab,String estiloOK,String estiloCANCEL) {
		
		//cambio de valores de popUp
		UtilPopUp popUpCal = (UtilPopUp) session.getAttribute(SessionManagerSISPE.POPUP);
		
		popUpCal.setEtiquetaBotonOK(etiquetaOk);
		popUpCal.setEtiquetaBotonCANCEL(etiquetaCancel);
		
		popUpCal.setEstiloOK(estiloOK);
		popUpCal.setEstiloCANCEL(estiloCANCEL);
		
		popUpCal.setValorOK("requestAjax('"+nomPagOk+"', ['popupConfirmar','entregas','mensajesPopUp','pregunta2'], {parameters: '"+parOk+"=ok',popWait:true, evalScripts:true});");
		popUpCal.setValorCANCEL("requestAjax('"+nomPagCancel+"', ['popupConfirmar',"+seccionMensajes+",'reserva','reservaCab','resumenEntregas'], {parameters: '"+paraCancel+"=ok',popWait:true, evalScripts:true});"+modal);
		popUpCal.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
		
		session.setAttribute(SessionManagerSISPE.POPUP, popUpCal);
		
		//cambiar al tab2Cuando presiono en siguiente
		LogSISPE.getLog().info("Se elige el tabde configuracion Opcion2");
		WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, numTab);
		
		session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
		
		popUpCal = null;
		
	}
	
	/**
	 * 
	 * @param session
	 * @param beanSession
	 * @param etiquetaOk
	 * @param etiquetaCANCEL
	 * @param accionOk
	 * @param accionCANCEL
	 * @param numTab
	 * @param estiloOK
	 * @param estiloCANCEL
	 */
	private void regresarPopUp(HttpSession session, BeanSession beanSession,String etiquetaOk,String etiquetaCANCEL,
			String accionOk,String accionCANCEL,Integer numTab,String estiloOK,String estiloCANCEL){
		
		UtilPopUp popUpConfEntregas = (UtilPopUp) session.getAttribute(SessionManagerSISPE.POPUP);
					
		popUpConfEntregas.setTituloVentana("Configuraci\u00F3n de entregas");
		popUpConfEntregas.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUpConfEntregas.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUpConfEntregas.setEtiquetaBotonOK(etiquetaOk);
		popUpConfEntregas.setEtiquetaBotonCANCEL(etiquetaCANCEL);
		
		popUpConfEntregas.setEstiloOK(estiloOK);
		popUpConfEntregas.setEstiloCANCEL(estiloCANCEL);
		
		popUpConfEntregas.setValorOK("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp','pregunta2'], {parameters: '"+accionOk+"=ok',popWait:true, evalScripts:true});");//entregas
		
		if(numTab.equals(0)){
			popUpConfEntregas.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
			popUpConfEntregas.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
		}
		else{
			if(numTab.equals(1)){
				popUpConfEntregas.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','mensajesPopUp'], {parameters: '"+accionCANCEL+"=ok',popWait:true, evalScripts:true});");//atrasPopUpEntregas
			}
			
		}
		
		popUpConfEntregas.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
		//popUpConfEntregas.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
		popUpConfEntregas.setAncho(75D);
		popUpConfEntregas.setTope(-150D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUpConfEntregas);
		
		//cambiar al tab2Cuando presiono en siguiente
		LogSISPE.getLog().info("Se elige el tabde configuracion Opcion1");
		WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, numTab);
		
		popUpConfEntregas = null;
	}
	
	/**
	 * 
	 * @param session
	 * @param beanSession
	 * @param etiquetaOK
	 * @param etiquetaCancel
	 * @param accionOK
	 * @param accionCancel
	 * @param numTab
	 * @param claseBotonOK
	 * @param claseBotonCancel
	 * @param mensajeAccion
	 * @param imgNumMensajes
	 */
	private void anteriorConfiguracionEntregas(HttpSession session, BeanSession beanSession,String etiquetaOK,String etiquetaCancel,
			String accionOK,String accionCancel,Integer numTab,String claseBotonOK,String claseBotonCancel,String mensajeAccion,String imgNumMensajes){
		
		regresarPopUp(session, beanSession,etiquetaOK,etiquetaCancel,accionOK,accionCancel,numTab,claseBotonOK,claseBotonCancel);
		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString(mensajeAccion));
		session.setAttribute(PASO, MessagesWebSISPE.getString(imgNumMensajes));
		
	}
	
	/**
	 * 
	 * @param session
	 * @param request
	 * @param localID
	 * @param errors
	 * @param formulario
	 * @throws Exception
	 */
	public static void obtenerLocal(HttpSession session,HttpServletRequest request,LocalID localID,
			ActionMessages errors,CotizarReservarForm formulario) throws Exception{
		
		session.setAttribute(LOCALID, localID);
		
		LogSISPE.getLog().info("fecha de busqueda: formulario.getFechaEntregaCliente() {}" , formulario.getFechaEntregaCliente());
		LogSISPE.getLog().info("fecha de busqueda: (String)(session.getAttribute(FECHAENTREGACLIENTE))() {}" , (String)(session.getAttribute(FECHAENTREGACLIENTE)));
		
		if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
		}
		Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		
		LogSISPE.getLog().info("fecha de busqueda: {}" , mes);
		//jmena bandera que muestra o no la configuracion del local bodega.
		session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
		String rangoDias = (String)session.getAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO);
		if(rangoDias == null){
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO();
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
			
			rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
			session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
			consultaParametroDTO = null;
		}
		
		int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
		LogSISPE.getLog().info("parametro: {}" , parametro);
		
		//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaMinima);
		fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
		fechaMinima = fechaCalendario.getTime();
		
		//------ fmunoz -------
		//se obtiene el par\u00E1metro desde la base
		ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
		int canDiaFecMinDes = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
		Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, canDiaFecMinDes, 0, 0, 0, 0));
		
		//si la fecha m\u00EDnima es menor o igual a hoy
		if(fechaCalendario.getTimeInMillis() < fechaMinimaReferencia.getTime()){
			fechaMinima = fechaMinimaReferencia;
		}
		
		//Resto al dia de entrega un d\u00EDa para ver la fecha m\u00E1xima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
		Date fechaMaxima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
		GregorianCalendar fechaCalendario1=new GregorianCalendar();
		fechaCalendario1.setTime(fechaMaxima);
		fechaCalendario1.add(Calendar.DAY_OF_MONTH,-1);
		fechaMaxima=fechaCalendario1.getTime();
		
		session.removeAttribute(CALENDARIODIALOCAL);
		session.removeAttribute(DIASELECCIONADO);
		
		//jmena verifico si es el calendario de la bod/canastos
		if(localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
			GregorianCalendar fechaMaximaDomicilio = new GregorianCalendar();
			fechaMaximaDomicilio.setTime(new Date());
			fechaMaximaDomicilio.set(GregorianCalendar.MONTH, 12);
			fechaMaximaDomicilio.set(GregorianCalendar.DAY_OF_MONTH, 31);
			fechaMaxima = new Date(fechaMaximaDomicilio.getTimeInMillis());
			fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
			//Inicializo la fecha de entrega null
			formulario.setFechaEntregaCliente(null);
			request.getSession().setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
			request.getSession().getAttribute(BANDERA_CONFIGURA_CAL_BOD);
			//session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
		}
		
		long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(fechaMaxima));
		if(diferenciaEntregaBusca<0.0){
			LogSISPE.getLog().info("Mensaje rango de fechas con errores");
			errors.add("rangoFechas",new ActionMessage("errors.rangoInicialFinalFechasCalendario",parametro,canDiaFecMinDes));
		}else{
			EntregaLocalCalendarioUtil.obtenerCalendario(request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
		}
		
		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
		LogSISPE.getLog().info("fechaMaxima: {}" , fechaMaxima);
		session.setAttribute(FECHAMINIMA,fechaMinima);
		session.setAttribute(FECHAMAXIMA,fechaMaxima);
		
	}
	

	
	/**
	 * PopUp para confirmar si desea eliminar las entregas seleccionadas.
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaEliminarEntregas(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'confirmarEliminarEntregas=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarEliminarEntregas=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/confirmarEliminarEntregas.jsp");
		popUp.setAncho(40D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * PopUp para confirmar eliminar la entrega.
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaEliminarEntrega(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'confirmarEliminarEntrega=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarEliminarEntrega=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/confirmarEliminarEntregas.jsp");
		popUp.setAncho(40D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * PopUp para confirmar editar la entrega.
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaEditarEntrega(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'confirmarEditarEntrega=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarEditarEntrega=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/confirmarEditarEntrega.jsp");
		popUp.setAncho(40D);
		popUp.setTope(120D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * PopUp para mostrar cuando quiere eliminar las entregas sin seleccionar alguna.
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaAdvertenciaEliminarEntregas(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Advertencia");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarEliminarEntregas=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/advertenciaEliminarEntregas.jsp");
		popUp.setAncho(40D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * Carga las zonas asociadas a una ciudad
	 * @param formulario
	 * @param session
	 * @throws Exception
	 */
	private static void cargarZonaCiudad(CotizarReservarForm formulario,  HttpSession session) throws Exception{
		LogSISPE.getLog().info("ingresa al metodo cargarZonaCiudad ");
		session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
		String codigoCiudad = "";
		if(formulario.getSeleccionCiudad() != null)
			if(formulario.getSeleccionCiudad().split("/").length > 1){
				codigoCiudad = formulario.getSeleccionCiudad().split("/")[1];
			}
			else{
				codigoCiudad = formulario.getSeleccionCiudad();
			}
		if(!codigoCiudad.equals("")){
			DivisionGeoPoliticaDTO ciudadConsulta = new DivisionGeoPoliticaDTO();
			ciudadConsulta.setCodigoDivGeoPolPadre(codigoCiudad);
			Collection<DivisionGeoPoliticaDTO> zonasCiudad = SISPEFactory.getDataService().findObjects(ciudadConsulta);
			session.setAttribute(CIUDAD_SECTOR_ENTREGA, zonasCiudad);
		}
	}
	
	/**
	 * obtiene los parametros que se van a usar para la configuracion de la entrega
	 * @param request
	 * @param errors
	 */
	private static void obtenerConfiguracionEntrega(HttpServletRequest request, ActionMessages errors){
		try{
			//Construye parametros para la consulta
			ConfiguracionDTO configuracionDTO=new ConfiguracionDTO();
			configuracionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			configuracionDTO.setEstadoConfiguracion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
			configuracionDTO.setProcesoConfiguracion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.configuracion.procesoReserva"));
			//Metodo que trae las configuraciones de las entregas
			Collection<ConfiguracionDTO> configuracionDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerConfiguracion(configuracionDTO);
			request.getSession().setAttribute(CONFIGURACION, configuracionDTOCol);
		}catch(Exception e){
			errors.add("configuracion",new ActionMessage("errors.cargarConfiguraciones"));
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param errors
	 * @param formulario
	 * @param detallePedidoDTOCol
	 * @return
	 * @throws Exception
	 * @throws SISPEException
	 */
	private Boolean validarCapacidadBodega(HttpServletRequest request, ActionMessages errors, CotizarReservarForm formulario, 
			Collection<DetallePedidoDTO> detallePedidoDTOCol) throws Exception, SISPEException{
		
		Boolean existeError=Boolean.TRUE;
		if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_OTRO_LOCAL)
				&& formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
			
			HttpSession session=request.getSession();
			
//			//recupero los datos del detalle del pedido
//			LocalID localID=new LocalID();
//			localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")).intValue());
//			Date mesSeleccionado= (Date)session.getAttribute(MESSELECCIONADO);
//			obtenerCalendarioOtroLocal(request, localID, errors, mesSeleccionado, null,null, formulario, null);
//			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLBODEGA);
			
			GregorianCalendar fechaSinHora = new GregorianCalendar();
			fechaSinHora.setTime(ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()));
			GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
			fechaSinHoraAux.add(Calendar.DAY_OF_MONTH, -1);
			
			//int diaSeleccionado= fechaSinHoraAux.get(Calendar.DAY_OF_MONTH);
			CalendarioDiaLocalID calendarioDiaLocalBodegaID=new CalendarioDiaLocalID();// (CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
			calendarioDiaLocalBodegaID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			calendarioDiaLocalBodegaID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")));
			calendarioDiaLocalBodegaID.setFechaCalendarioDia(new Date(fechaSinHoraAux.getTime().getTime()));
			int numeroBultos=0;
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()>0){
					numeroBultos = numeroBultos+UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue(), detallePedidoDTO.getArticuloDTO());
				}
			}
			//Obtengo la configuracion del dia seleccionado
			CalendarioDiaLocalDTO calendarioDiaLocalActual=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalBodegaID,null);
			if((new Long(ConverterUtil.fromDoubleToInteger(calendarioDiaLocalActual.getCantidadDisponible()))).longValue()<numeroBultos){
				session.setAttribute("ec.com.smx.sic.sispe.validar.bodega","ok");
				 existeError=Boolean.FALSE;
				//Si no existe la capacidad verifica si tiene autorizacion
				if(!calendarioDiaLocalActual.getId().getCodigoLocal().toString().equals((SessionManagerSISPE.getCurrentLocal(request)).toString())){
					AutorizacionDTO autorizacionDTO = buscaAutorizacion(session,calendarioDiaLocalBodegaID.getCodigoLocal().toString() ,new Timestamp(fechaSinHoraAux.getTime().getTime()));
					if(autorizacionDTO==null){
						errors.add("cantidadMayor",new ActionMessage("errors.capacidadDisponibleBodega",numeroBultos,calendarioDiaLocalActual.getId().getCodigoLocal()));
					}
					else{
						 session.removeAttribute("ec.com.smx.sic.sispe.validar.bodega");
						 existeError=Boolean.TRUE;
					}
				}
			}
		}
		return existeError;
	}
	
	/**
	 * 
	 * @param request
	 * @param localID
	 * @param errors
	 * @param mes
	 * @param fechaMinima
	 * @param fechaMaxima
	 * @param formulario
	 * @param verCalendario
	 * @throws Exception
	 */
	public static void obtenerCalendarioOtroLocal(HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,
			Date fechaMinima,Date fechaMaxima,CotizarReservarForm formulario, Boolean verCalendario) throws Exception{
		try{
			
			HttpSession session=request.getSession();
			
			LogSISPE.getLog().info("*****entra a cargar el calendario*****");
			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
			List<?> calendarioDiaLocalDTOCol;// = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
			try{
				calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,null, null);
				//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
				LogSISPE.getLog().info("minima: {}" , fechaMinima);
				LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
				Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
				session.setAttribute(CALENDARIODIALOCALCOLBODEGA,calendarioDiaLocalDTOOBJ);
				//subo a session el mes de busqueda
				session.setAttribute(MESSELECCIONADOBODEGA,mes);
				//calculo cuantas semanas tiene el mes
				int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
				LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
				//subo a sesion el numero de semanas
				session.setAttribute(NUMEROSEMANASBODEGA,new Integer(maximoSemanas));
			}catch (Exception e) {
				if(errors != null){
					errors.add("localSinCalendario",new ActionMessage("warnings.configuracion.plantilla.por.defecto",localID.getCodigoLocal()));
				}
			} 
			
		}catch(Exception e){
			LogSISPE.getLog().info("error al cargar calendario: {}" , e);
			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
		}
	}
	
	/**
	 * Comprueba si existe una autorizacion para una fecha de despacho
	 * @param session
	 * @param codigoLocal
	 * @param fecha
	 * @return
	 */
	public AutorizacionDTO buscaAutorizacion(HttpSession session,String codigoLocal, Timestamp fecha){
		Collection<AutorizacionEntregasDTO> autorizacionEntregasWDTOCol =(Collection<AutorizacionEntregasDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol");
		AutorizacionDTO autorizacionDTO=null;
		if (autorizacionEntregasWDTOCol!=null){
			LogSISPE.getLog().info("autorizaciones: {}" , autorizacionEntregasWDTOCol.size());
			for (AutorizacionEntregasDTO autorizacionEntregasWDTO:autorizacionEntregasWDTOCol) {
				LogSISPE.getLog().info("local: {} - {}" , autorizacionEntregasWDTO.getCodigoLocal(), codigoLocal);
				LogSISPE.getLog().info("fecha: {} - {}" , autorizacionEntregasWDTO.getFechaAutorizacion(), fecha);
				if(autorizacionEntregasWDTO.getCodigoLocal().equals(codigoLocal)){// && fecha.equals(autorizacionEntregasWDTO.getFechaAutorizacion())){
					LogSISPE.getLog().info("existe una autorizacion");
					autorizacionDTO=autorizacionEntregasWDTO.getAutorizacionDTO();
				}
			}
		}
		return(autorizacionDTO);
	}
	
	/**
	 * 
	 * @param fechaDespacho
	 * @return
	 */
	public static String obtenerDia(Calendar fechaDespacho){
		
		final String domingo = MessagesWebSISPE.getString("dia.semana.domingo");
		final String lunes = MessagesWebSISPE.getString("dia.semana.lunes");
		final String martes = MessagesWebSISPE.getString("dia.semana.martes");
		final String miercoles = MessagesWebSISPE.getString("dia.semana.miercoles");
		final String jueves = MessagesWebSISPE.getString("dia.semana.jueves");
		final String viernes = MessagesWebSISPE.getString("dia.semana.viernes");
		final String sabado = MessagesWebSISPE.getString("dia.semana.sabado");
		
		String days [] = {domingo, lunes, martes, miercoles, jueves, viernes, sabado};
		return days[fechaDespacho.get(Calendar.DAY_OF_WEEK)-1]; 
	}
	
	/**
	 * Obtiene la configuraci\u00F3n de entregas del pedido
	 * @param session
	 * @param beanSession
	 * @param formulario
	 */
	public void obtenerEntregas( HttpSession session){
		//prepar la nueva coleccion para mostrar los responsables
		Collection<EntregaDetallePedidoDTO> entregasResp=new ArrayList<EntregaDetallePedidoDTO>();
		Collection<DetallePedidoDTO> detPedResp=new ArrayList<DetallePedidoDTO>();
		Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		if(!CollectionUtils.isEmpty(detallePedidoDTOCol)){
			for(DetallePedidoDTO detPed:detallePedidoDTOCol){
				if(!CollectionUtils.isEmpty(detPed.getEntregaDetallePedidoCol())){
					Collection<EntregaDetallePedidoDTO> entregas=(Collection<EntregaDetallePedidoDTO>)detPed.getEntregaDetallePedidoCol();
					if(CollectionUtils.isEmpty(entregasResp)){
						entregasResp=(Collection<EntregaDetallePedidoDTO>)SerializationUtils.clone((Serializable)entregas);
						for(EntregaDetallePedidoDTO entPed:entregasResp){
							detPedResp=new ArrayList<DetallePedidoDTO>();
							entPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
							detPed.setNpReponsable(null);
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
							EntregaLocalCalendarioUtil.obtenerResponsableEntrega(session, dp, entPed);
							detPedResp.add(dp);
							entPed.getEntregaPedidoDTO().setNpDetallePedido(detPedResp);
						}
					}else{
						for(EntregaDetallePedidoDTO entDetPed:entregas){
							Boolean existeEntrega=Boolean.FALSE;
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
							for(EntregaDetallePedidoDTO entPedRes:entregasResp){
								Long diferencia= entDetPed.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()-entPedRes.getEntregaPedidoDTO().getFechaEntregaCliente().getTime();
								if(diferencia==0L && entDetPed.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim().equals(entPedRes.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim())
									&&	entPedRes.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()==entDetPed.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()){
									//estructura de responsables, asignar a los detalles
									detPed.setNpReponsable(null);
									EntregaLocalCalendarioUtil.obtenerResponsableEntrega(session,dp,entDetPed);
									entPedRes.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
									existeEntrega=Boolean.TRUE;
									break;
								}
							}
							if(!existeEntrega){
								entDetPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
								detPed.setNpReponsable(null);
								EntregaLocalCalendarioUtil.obtenerResponsableEntrega(session,dp,entDetPed);
								entDetPed.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
								entregasResp.add(entDetPed);
							}
						}
					}
				}
			}
		}
		session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_RESPONSABLES,entregasResp);
	}
	
	/**
	 * Carga la lista de configuraci\u00F3n de entregas a eliminar, segun la entrega seleccionada
	 * @param detallePedidoDTOCol
	 * @param request
	 * @param session
	 * @param formulario
	 * @param errors
	 * @param warnings
	 */
	public void listaEliminarEntregas(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request, HttpSession session, CotizarReservarForm formulario, ActionMessages errors, ActionMessages warnings){		
		//obtiene la lista de entregas
		ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)session.getAttribute(ENTREGAS_RESPONSABLES);	
		//obtiene la entrega seleccionada
		EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntrega);
		//crea  el vector para almacenar las entregas del pedido a eliminar
		String[] seleccionEntregas = new String[entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido().size()];	
				
		int detalle= 0;
		int indice = 0;
		for(DetallePedidoDTO detallePedido :  detallePedidoDTOCol){		
			int entrega = 0;
			for(EntregaDetallePedidoDTO entregaDetallePedido:  detallePedido.getEntregaDetallePedidoCol()){
					for(DetallePedidoDTO detallePedidoDTO :  entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){
						//compara las entregas en las dos listas y almacena en la lista de entregas a eliminar						
						if(detallePedido.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getFechaDespachoBodega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()) 
								&& entregaDetallePedido.getEntregaPedidoDTO().getDireccionEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega())){
								seleccionEntregas[indice]= entrega + "-" + detalle;
								indice++;
						}
					}
				entrega++;
			}
			detalle++;
		}						
		formulario.setSeleccionEntregas(seleccionEntregas);
		EntregaLocalCalendarioUtil.eliminarEntregasPedido(request, session, errors, warnings,formulario);
	}
	
	
	public void colocarCiudadSector(HttpServletRequest request,HttpSession session, CotizarReservarForm formulario,EntregaDetallePedidoDTO entregaDetallePedidoDTO) throws Exception{
		//obtiene la lista de ciudades
		ciudad = "";	
		ciudadGye = "";
		if(session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)!=null){	
			for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO : (Collection<VistaEstablecimientoCiudadLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)){						
				for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocal : (Collection<VistaEstablecimientoCiudadLocalDTO>)vistaEstablecimientoCiudadLocalDTO.getVistaLocales()){
					String codigoVistaCiudad = "OTR" + vistaEstablecimientoCiudadLocal.getNombreCiudad() + "/" + vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();							
					if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada()!=null){
						if(vistaEstablecimientoCiudadLocal.getNpCiudadRecomendada().equals("1")){
							codigoVistaCiudad = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
						}
					}
					if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol())){
						ciudad = codigoVistaCiudad;
						if(vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"))){
							ciudadGye = vistaEstablecimientoCiudadLocal.getId().getCodigoCiudad();
						}	
					}						
				}
			}
		}				
		//asigna la ciudad
		formulario.setSeleccionCiudad(ciudad);			
		session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol());
		//session.setAttribute("otraCuidad", "ok");
		
		//busca zonas de esa ciudad
		cargarZonaCiudad(formulario, session);		
		//Si la ciudad no tiene zona, se carga los totales de camiones
		Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
		if(CollectionUtils.isEmpty(zonasCiudad)){	
			if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null)
				obtenerCalendarioPorSemana(request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
		}				
		//asigna la zona
		formulario.setSelecionCiudadZonaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega());				
			
		LogSISPE.getLog().info("Ciudad: ",formulario.getSeleccionCiudad());
		LogSISPE.getLog().info("Zona: ",formulario.getSelecionCiudadZonaEntrega());	
	}
	
	public void cargarZonasCiudadComboSICMER(CotizarReservarForm formulario, HttpSession session, ActionMessages info) throws Exception{
		if(formulario.getSeleccionCiudad().equals("ciudad") || formulario.getSeleccionCiudad().equals("")){
			session.removeAttribute(DIASELECCIONADO);
			info.add(formulario.getSeleccionCiudad(), new ActionMessage("errors.seleccion.ciudad", "Ciudad de Entrega V\u00E1lida"));
		}else {
			//OANDINO: Se carga el valor del nombre ciudad/c\u00F3digo ciudad -----------------------------			
			String[] valorCodigoDivGeoPol = formulario.getSeleccionCiudad().split("/");
			
			if(valorCodigoDivGeoPol.length > 1){
				// Se carga en session el valor del c\u00F3digo de la ciudad que viene adjunto al nombre de la misma desde combobox de JSP
				session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, valorCodigoDivGeoPol[1]);
			}else{
				session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, formulario.getSeleccionCiudad());
				
				//buscar zonas de esa ciudad
				cargarZonaCiudad(formulario, session);
				
			}
			LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO));
			//----------------------------------------------------------------------------------------
			
			//Saco las tres primeras letras del valor del combo para ver si selecciono una ciudad distinta a las recomendadas
			String subNombre = valorCodigoDivGeoPol[0].substring(0, 3);
			
			if(!subNombre.equals("") && subNombre.equals("OTR")){
				LogSISPE.getLog().info("Selecciono otra ciudad");
//				//Si selecciona otra ciudad se realiza la pregunta
//				request.setAttribute("ec.com.smx.sic.sispe.mensajeSeleccionCiudad", "ok");
				
				//buscar zonas de esa ciudad
				cargarZonaCiudad(formulario, session);
				
				session.setAttribute("otraCuidad", "ok");
			}else{
				LogSISPE.getLog().info("selecciono ciudad recomendada");
				
				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePaso2.3"));
				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				
				session.setAttribute("siDireccion", "ok");
				session.setAttribute("otraCuidad", "ok");
			}
		}
	}
	


	public Boolean getEditarEntrega() {
		return editarEntrega;
	}

	public void setEditarEntrega(Boolean editarEntrega) {
		this.editarEntrega = editarEntrega;
	}

	public Collection<DetallePedidoDTO> getDetallePedidoDTOColTmp() {
		return detallePedidoDTOColTmp;
	}

	public void setDetallePedidoDTOColTmp(
			Collection<DetallePedidoDTO> detallePedidoDTOColTmp) {
		this.detallePedidoDTOColTmp = detallePedidoDTOColTmp;
	}

	public ArrayList<EntregaDetallePedidoDTO> getEntregasTmp() {
		return entregasTmp;
	}

	public void setEntregasTmp(ArrayList<EntregaDetallePedidoDTO> entregasTmp) {
		this.entregasTmp = entregasTmp;
	}

	public Collection<DireccionesDTO> getDireccionesColTmp() {
		return direccionesColTmp;
	}

	public void setDireccionesColTmp(Collection<DireccionesDTO> direccionesColTmp) {
		this.direccionesColTmp = direccionesColTmp;
	}
	
}