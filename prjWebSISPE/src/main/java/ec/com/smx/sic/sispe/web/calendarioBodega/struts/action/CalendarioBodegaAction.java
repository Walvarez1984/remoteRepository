/*
 * CalendarioBodegaAction.java
 * Creado el 21/07/2010 12:25:19
 *   	
 */
package ec.com.smx.sic.sispe.web.calendarioBodega.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.dto.Duplex;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.common.factory.SICFactory;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloImpuestoDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloMedidaDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloProveedorDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.ConfiguracionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetalleRecetaDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaCalendarioArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.calendarioBodega.struts.form.CalendarioBodegaForm;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.reportes.dto.DireccionesDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleEstadoPedidoAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author jvillacis
 *
 */
public class CalendarioBodegaAction extends BaseAction {
	//se obtienen los estados de b\u00FAsqueda
	private String estadoReservado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
	private String estadoEnProduccion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
	//private String estadoProducido = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
	//private String estadoDespachado =  MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
	//private String estadoEntregado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado");
	private String estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
	private String estadoInactivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
	
	public static final String CLASIFICACIONES_PERECIBLES = "ec.com.smx.sic.sispe.clasificacionesPerecibles";
	
	//Nombre de la variable que almacenar\u00E1 la colecci\u00F3n de art\u00EDculo
	private final String COLECCION_ARTICULOS_PRODUCCION = "ec.com.smx.sic.sispe.articulosProduccion";
	
	//Variable de session para el calendario bodega
	private final String COLECCION_ARTICULOS_DESPACHO_LOCAL = "ec.com.smx.sic.sispe.articulosDespachoLocal";
	private final String COLECCION_ARTICULOS_DESPACHO_DOMICILIO = "ec.com.smx.sic.sispe.articulosDespachoDomicilio";
	//Variable de Impresion en el reporte
	private final String COLECCION_ARTICULOS_DESPACHO_LOCAL_IMPRESION = "ec.com.smx.sic.sispe.articulosDespachoLocalImpresion";
	private final String COLECCION_ARTICULOS_DESPACHO_DOMICILIO_IMPRESION = "ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion";
	private final String COLECCION_ARTICULOS_DESPACHO_DOMICILIO_GUAYAQUIL_IMPRESION = "ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.guayaquil";
	
	private final String CALENDARIO_BODEGA = "ec.com.smx.sic.sispe.calendarioBodega";
	private final String DETALLE_CANASTA = "ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta";
	private final String DETALLE_CANASTALP = "ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanastaLP";
	private final String NOMBRE_COL_DIA_ACTUAL = ".diaActual";
	private final String NOMBRE_COL_DIA_SELEC = ".diaSeleccionado";
	private final String CANASTA = ".canasta";
	private final String DESPENSA = ".despensa";
	private final String CATALOGO = ".catalogo";
	private final String ESPECIAL = ".especial";
	private final String LINEA = ".linea";
	private final String CAL_SEL = ".sel";
	private final String CAL_POS = ".pos";
	private final String MES_SELECCIONADO = "ec.com.smx.sic.sispe.mesSeleccionado";
	private final String MES_SELECCIONADO_NOMBRE = "ec.com.smx.sic.sispe.mesSeleccionadoNombre";
	private final String FECHA_SELECCIONADA = "ec.com.smx.sic.sispe.fechaSeleccionada";
	private final String FECHA_ACTUAL_NOMBRE = "ec.com.smx.sic.sispe.fechaActualNombre";
	private final String FECHA_SELECCIONADA_NOMBRE = "ec.com.smx.sic.sispe.fechaSeleccionadaNombre";
	private final String ANIO_CALENDARIO = "ec.com.smx.sic.sispe.anioCalendario";
	private final String INDICE_DIA_SELECCIONADO = "ec.com.smx.sic.sispe.indiceDiaSeleccionado";
	private static final String[] MESES_ANIO = {"ENERO","FEBRERO", "MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
	private final String BANDERA_BODEGA_TRANSITO = "ec.com.smx.sic.sispe.banderaBodegaTransito";
	private final String BANDERA_BODEGA_POR_CLASIFICACION = "ec.com.smx.sic.sispe.banderaBodegaPorClasificacion";
	private final String ACCESO_USUARIO_CALENDARIO = "ec.com.smx.sic.sispe.usuarioAccesoCalendario";
	private final String IMPRESION_DIA = "ec.com.smx.sic.sispe.reporte.diaCalendario";
	//NUEVAS variables de sesion para cargar el calendario de entregas
	public static final String TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE = "ec.com.smx.sic.sispe.totalSolicitadoCDaDomicilioNoPerecible";
	public static final String PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO = "ec.com.smx.sic.sispe.parametroLimiteInferiorObligatorioTransito";
	//Variable que permite mantener el valor obtenido de VALORPARAMETRO desde SCSPETPARAMETRO
	public static final String VAR_VALOR_PARAMETRO_TOTAL = "ec.com.smx.sic.sispe.valorParametro";
	private static final String EXISTENCAMBIOS= "ec.com.smx.sic.sispe.existenCambios"; //Variable para saber si existieron cambios en las entregas
	private static final String ORDENDIAS= "ec.com.smx.sic.sispe.calendarizacion.ordenDias"; //Orden de dias de la semana
	private static final String CONFIGURACION= "ec.com.smx.sic.sispe.entregas.configuracionDTOCol"; //Coleccion de configuraciones para las entregas
	public static final String OPCIONLUGARENTREGA="ec.com.smx.sic.opcionLugarEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
	public static final String OPCIONTIPOENTREGA="ec.com.smx.sic.opcionTipoEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
	public static final String OPCIONSTOCK="ec.com.smx.sic.opcionStock"; //Sesion que indica la ultima opcion del stock seleccionado
	public static final String VISTALOCALORIGEN= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.origen"; //local origen
	public static final String CALENDARIOCONFIGURACIONDIALOCAL= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTO"; //configuracion real de las modificaciones en el calendario
	public static final String CALENDARIOCONFIGURACIONDIALOCALAUX= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTOAux"; //configuracion auxiliar de las modificaciones en el calendario
	public static final String DETALLELPEDIDOAUX= "ec.com.smx.sic.sispe.detallePedidoAux"; //contiene el detalle del pedido se incia con el detalle incial y se trabaja con el si se elige guardar la entrega se sobreescribe la sesion del detalle del pedido
	public static final String DIRECCIONES= "ec.com.smx.sic.sispe.pedido.direcciones"; //direcciones donde se han relizado entregas
	public static final String DIRECCIONESAUX= "ec.com.smx.sic.sispe.pedido.direccionesAux"; //sesion auxiliar para almacenar las direcciones antes de guardar
	public static final String COSTOENTREGA= "ec.com.smx.sispe.pedido.costoEntregasDTOCol"; //coleccion de los costos de las entregas a domicilio
	public static final String COSTOENTREGAAUX= "ec.com.smx.sispe.pedido.costoEntregasDTOColAux"; //coleccion de los costos de las entregas auxiliar antes de grabar los cambios
	public static final String VALORTOTALENTREGA= "ec.com.smx.sispe.pedido.valorTotalEntrega"; //costo entrega total
	public static final String VALORTOTALENTREGAAUX= "ec.com.smx.sispe.pedido.valorTotalEntregaAux"; //costo entrega total
	public static final String SECDIRECCIONES= "ec.com.smx.sic.sispe.pedido.secDirecciones"; //numero de direcciones
	public static final String NOMBREENTIDADRESPONSABLE= "ec.com.smx.sic.sispe.nombreEntidadResponsable"; //Variable que indica cual es la entidad responsable
	private static final String FECHABUSQUEDA= "ec.com.smx.calendarizacion.fechaBuscada"; //fecha minima de entrega
	private static final String LOCALID= "ec.com.smx.sic.sispe.localID"; //id del local seleccionado
	public static final String DIASELECCIONADO= "ec.com.smx.calendarizacion.diaSeleccionado"; //dia de despacho seleccionado en el calendario
	public static final String CALENDARIODIALOCALCOL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol"; //calendario para un mes especifico
	public static final String CALENDARIODIALOCALCOLAUX= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOColAux"; //dias que deben ser modificados su cantidad acumulada
	private static final String VISTALOCALCOL= "ec.com.smx.sic.sispe.vistaLocalDTOCol"; //locales
	public static final String CALENDARIODIALOCAL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTO"; //CD del dia seleccionado en el calendario
	private static final String FECHAMINIMA= "ec.com.smx.sic.sispe.fechaMinima"; //fecha minima de despacho a locales
	private static final String FECHAMAXIMA= "ec.com.smx.sic.sispe.fechaMaxima"; //fecha maxima de despacho a locales
	private static final String SECTORSELECCIONADO= "ec.com.smx.sic.sispe.sectorSeleccionado"; //indice local seleccionado
	public static final String DIRECCION= "ec.com.smx.sic.sispe.direccionDTO"; //direccion
	private static final String EXISTELUGARENTREGA="ec.com.smx.sic.sispe.existeLugarEntrega";//Variable de sesion que indica si ya fue seleccionado un lugar de entrega
	public static final String SELECCIONARLOCAL="ec.com.smx.sic.sispe.seleccionarLocal";//Seleccionar el local
	public static final String SELECCIONARCALENDARIO="ec.com.smx.sic.sispe.seleccionarCalendario";//Seleccionar fecha de recepcion
	public static final String HABILITARCANTIDADENTREGA="ec.com.smx.sic.sispe.habilitarCantidadEntrega";//Cantidad de entrega
	public static final String HABILITARCANTIDADRESERVA="ec.com.smx.sic.sispe.habilitarCantidadReserva";//Cantidad de recepcion
	public static final String HABILITARDIRECCION="ec.com.smx.sic.sispe.habilitarDireccion";//Direccion y distancia
	public static final String HABILITARBOTONACEPTAR="ec.com.smx.sic.sispe.habilitarBotonAceptar";//Boton aceptar
	public static final String MENSAJEPASOS="ec.com.smx.sic.mensajes"; //Sesion que indica los mensajes que saldran en cada paso
	public static final String PASO="ec.com.smx.sic.paso"; //Indica el numero de paso que debe salir en el mensaje
	public static final String DETALLE_PEDIDO = "ec.com.smx.sic.sispe.detallePedido";
	public static final String VISTA_PEDIDO = "ec.com.smx.sic.sispe.vistaPedido";
	private static final String MESSELECCIONADO= "ec.com.smx.calendarizacion.mesBusqueda"; //mes seleccionado en el calendario
	private static final String NUMEROSEMANAS= "ec.com.smx.calendarizacion.numeroSemanas"; //numero de semanas que tiene un mes
	private static final String FECHADESPACHO= "ec.com.smx.calendarizacion.fechadespacho"; //numero de semanas que tiene un mes
	//mostrarPanel de confirmacion
	private final String CONFIRMACION  = "ec.com.smx.sic.sispe.popUpConfirmacion";
	
	//variable para recesta estado
	public static final String RECETA_ESTADO = "ec.com.smx.sic.sispe.recetaEstado";
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		/************************Declaracion de variables**********************************/
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		HttpSession session = request.getSession();
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		CalendarioBodegaForm formulario=(CalendarioBodegaForm)form;
		String forward = "desplegar";
		
		//elimina la variable del detalle pedido
		if(request.getParameter("indiceDetallePedido") == null && request.getParameter("actualizar") == null && 
				(beanSession.getPaginaTab() == null || ( beanSession.getPaginaTab() != null && !beanSession.getPaginaTab().comprobarSeleccionTab(request)))){
			LogSISPE.getLog().info("Borrando sesion VISTA_PEDIDO");
			session.removeAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
			session.removeAttribute(DETALLE_CANASTA);
			//remover mensaje de confirmacion
			session.removeAttribute("ec.com.smx.sic.sispe.configGuardar");
		}
		session.removeAttribute(IMPRESION_DIA);
		
		/*-------------------------- cuando se selecciona el cambio de mes --------------------------------*/
		if(request.getParameter("mesAdelante") != null || request.getParameter("mesAtras") != null){
			LogSISPE.getLog().info("Entro a cambio de mes");
			try{
				Integer mesSeleccionado;
				Integer anioCalendario;
				Integer mesSeleccionadoTemp;
				Integer anioCalendarioTemp; 
				Calendar fechaBase = new GregorianCalendar();
				fechaBase.setTime(new Date());
				
				//si va un mes hacia adelante
				if(request.getParameter("mesAdelante") != null){
					mesSeleccionadoTemp = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_POS)));
					anioCalendarioTemp = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_POS)));
					mesSeleccionado = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_POS)));
					anioCalendario = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_POS)));
					
					mesSeleccionado++;
					//el anio
					if(mesSeleccionado > 11){
						mesSeleccionado = 0;
						anioCalendario++;
					}
					//para el mes seleccionado
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_SEL), anioCalendarioTemp);
					session.setAttribute(MES_SELECCIONADO.concat(CAL_SEL), mesSeleccionadoTemp);
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_SEL), MESES_ANIO[mesSeleccionadoTemp]);
					//para el mes siguiente
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_POS), anioCalendario);
					session.setAttribute(MES_SELECCIONADO.concat(CAL_POS), mesSeleccionado);
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_POS), MESES_ANIO[mesSeleccionado]);
					
					//pone el calendario siguiente en el seleccionado
					session.setAttribute(CALENDARIO_BODEGA.concat(CAL_SEL), (Collection<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIO_BODEGA.concat(CAL_POS)));
					//obtiene el calendario del mes selecionado
					fechaBase.set(Calendar.MONTH, mesSeleccionado);
					fechaBase.set(Calendar.YEAR, anioCalendario);
					fechaBase.set(Calendar.DAY_OF_MONTH, 1);
					//Presento en el calendario segun el usuario logeado	
					obtenerCalendario(request, fechaBase, (GregorianCalendar)session.getAttribute(FECHA_SELECCIONADA), CAL_POS);
				}
				//si va un mes hacia atras
				else{
					mesSeleccionadoTemp = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_SEL)));
					anioCalendarioTemp = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_SEL)));
					mesSeleccionado = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_SEL)));
					anioCalendario = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_SEL)));
					
					mesSeleccionado--;
					//el anio
					if(mesSeleccionado < 0){
						mesSeleccionado = 11;
						anioCalendario--;
					}
					//para el mes seleccionado
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_SEL), anioCalendario);
					session.setAttribute(MES_SELECCIONADO.concat(CAL_SEL), mesSeleccionado);
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_SEL), MESES_ANIO[mesSeleccionado]);
					//para el mes siguiente
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_POS), anioCalendarioTemp);
					session.setAttribute(MES_SELECCIONADO.concat(CAL_POS), mesSeleccionadoTemp);
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_POS), MESES_ANIO[mesSeleccionadoTemp]);
					
					//pone el calendario seleccionado en el siguiente
					session.setAttribute(CALENDARIO_BODEGA.concat(CAL_POS), (Collection<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIO_BODEGA.concat(CAL_SEL)));
					//obtiene el calendario del mes selecionado
					fechaBase.set(Calendar.MONTH, mesSeleccionado);
					fechaBase.set(Calendar.YEAR, anioCalendario);
					fechaBase.set(Calendar.DAY_OF_MONTH, 1);
					//Presento en el calendario segun el usuario logeado
					obtenerCalendario(request, fechaBase, (GregorianCalendar)session.getAttribute(FECHA_SELECCIONADA), CAL_SEL);
				}
				
				
				/*//recorre un mes hacia adelante
				if(request.getParameter("mesAdelante") != null){
					mesSeleccionado++;
				}
				//recorre un mes hacia atr\u00E1s
				else{
					mesSeleccionado--;
				}
				//el anio
				if(mesSeleccionado < 0){
					mesSeleccionado = 11;
					anioCalendario--;
				}else if(mesSeleccionado > 11){
					mesSeleccionado = 0;
					anioCalendario++;
				}
				
				session.setAttribute(ANIO_CALENDARIO.concat(CAL_SEL), anioCalendario);
				session.setAttribute(MES_SELECCIONADO.concat(CAL_SEL), mesSeleccionado);
				session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_SEL), MESES_ANIO[mesSeleccionado]);
				
				//obtiene el calendario del mes selecionado
				fechaBase.set(Calendar.MONTH, mesSeleccionado);
				fechaBase.set(Calendar.YEAR, anioCalendario);
				fechaBase.set(Calendar.DAY_OF_MONTH, 1);
				obtenerCalendario(request, fechaBase, (GregorianCalendar)session.getAttribute(FECHA_SELECCIONADA), CAL_SEL);*/
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				//errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
			}
		}
		/*----------------------cuando se selecciona un d\u00EDa diferente al actual-------------------*/
		else if(request.getParameter("diaSeleccionado") != null){
			LogSISPE.getLog().info("Entro a seleccion de dia");
			try{
				CalendarioDiaLocalDTO anteriorDiaCalendarioSeleccionado = null;
				CalendarioDiaLocalDTO nuevoDiaCalendarioSeleccionado = null;
				//obtiene el calendario de session
				ArrayList<CalendarioDiaLocalDTO> calendarioBodegaSel = (ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIO_BODEGA.concat(CAL_SEL));
				//obtiene el calendario de session
				ArrayList<CalendarioDiaLocalDTO> calendarioBodegaPos = (ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIO_BODEGA.concat(CAL_POS));
				
				Calendar fechaSeleccionada = new GregorianCalendar();
				Integer diaSeleccionado = null;
				Integer anioSelecccionado = null;
				Integer mesSeleccionado = null;
				
				//separa el indice y la colecci\u00F3n a la que pertenece del d\u00EDa nuevo seleccionado
				String[] indicesNuevo = request.getParameter("diaSeleccionado").split(",");
				String coleccionNuevo = indicesNuevo[0];
				Integer indiceNuevo = Integer.parseInt(indicesNuevo[1]);
				//separa el indice y la colecci\u00F3n a la que pertenece del d\u00EDa nuevo seleccionado
				String[] indicesAnterior = ((String)session.getAttribute(INDICE_DIA_SELECCIONADO)).split(",");
				String coleccionAnterior = indicesAnterior[0];
				Integer indiceAnterior = Integer.parseInt(indicesAnterior[1]);
				
				//obtiene calendario con el d\u00EDa nuevo seleccionado
				if(coleccionNuevo.equals(CAL_SEL.substring(1))){
					//obtiene el nuevo d\u00EDa seleccionado 
					nuevoDiaCalendarioSeleccionado = calendarioBodegaSel.get(indiceNuevo);
					
					//valida si el dia seleccionado tambi\u00E9n est\u00E1 en el mes POS
					/*if(nuevoDiaCalendarioSeleccionado.getNpEsDistintoMes() && nuevoDiaCalendarioSeleccionado.getNpDiaMes() < 10){
						calendarioBodegaPos.get(7 - (calendarioBodegaSel.size() - indiceNuevo)).setNpEsSeleccionado(true);
					}*/
					for(CalendarioDiaLocalDTO calendarioDiaLocal : calendarioBodegaPos){
						LogSISPE.getLog().info(":::SEL: calendarioDiaLocal {} ; nuevoDiaCalendarioSeleccionado {}"
								,calendarioDiaLocal.getId().getFechaCalendarioDia(),nuevoDiaCalendarioSeleccionado.getId().getFechaCalendarioDia());
						if(calendarioDiaLocal.getId().getFechaCalendarioDia().compareTo(nuevoDiaCalendarioSeleccionado.getId().getFechaCalendarioDia()) == 0){
							calendarioDiaLocal.setNpEsSeleccionado(true);
							LogSISPE.getLog().info("::: fechas iguales");
							break;
						}
					}
					
					diaSeleccionado = new Integer(nuevoDiaCalendarioSeleccionado.getNpDiaMes());
					anioSelecccionado = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_SEL)));
					mesSeleccionado = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_SEL)));
				}else{
					//obtiene el nuevo d\u00EDa seleccionado 
					nuevoDiaCalendarioSeleccionado = calendarioBodegaPos.get(indiceNuevo);
					
					//valida si el dia seleccionado tambi\u00E9n est\u00E1 en el mes SEL
					/*if(nuevoDiaCalendarioSeleccionado.getNpEsDistintoMes() && nuevoDiaCalendarioSeleccionado.getNpDiaMes() > 10){
						calendarioBodegaSel.get(calendarioBodegaSel.size() - (7 - indiceNuevo)).setNpEsSeleccionado(true);
					}*/
					for(CalendarioDiaLocalDTO calendarioDiaLocal : calendarioBodegaSel){
						LogSISPE.getLog().info(":::POS: calendarioDiaLocal {} ; nuevoDiaCalendarioSeleccionado {}"
								,calendarioDiaLocal.getId().getFechaCalendarioDia(),nuevoDiaCalendarioSeleccionado.getId().getFechaCalendarioDia());
						if(calendarioDiaLocal.getId().getFechaCalendarioDia().compareTo(nuevoDiaCalendarioSeleccionado.getId().getFechaCalendarioDia()) == 0){
							calendarioDiaLocal.setNpEsSeleccionado(true);
							LogSISPE.getLog().info("::: fechas iguales");
							break;
						}
					}
					
					diaSeleccionado = new Integer(nuevoDiaCalendarioSeleccionado.getNpDiaMes());
					anioSelecccionado = new Integer((Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_POS)));
					mesSeleccionado = new Integer((Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_POS)));
				}
				
				//obtiene el calendario con el d\u00EDa anterior seleccionado
				if(coleccionAnterior.equals(CAL_SEL.substring(1))){
					//obtiene el d\u00EDa seleccionado anteriormente
					anteriorDiaCalendarioSeleccionado = calendarioBodegaSel.get(indiceAnterior);
					
					//verifica si el dia seleccionado anteriormente tambi\u00E9n pertenece a un dia del calendario POS
					/*if(anteriorDiaCalendarioSeleccionado.getNpEsDistintoMes() && anteriorDiaCalendarioSeleccionado.getNpDiaMes() < 10){
						//obtengo el d\u00EDa del calendario POS
						calendarioBodegaPos.get(7 - (calendarioBodegaSel.size() - indiceNuevo)).setNpEsSeleccionado(false);
					}*/
					for(CalendarioDiaLocalDTO calendarioDiaLocal : calendarioBodegaPos){
						LogSISPE.getLog().info(":::Sel: calendarioDiaLocal {} ; anteriorDiaCalendarioSeleccionado {}"
								,calendarioDiaLocal.getId().getFechaCalendarioDia(),anteriorDiaCalendarioSeleccionado.getId().getFechaCalendarioDia());
						if(calendarioDiaLocal.getId().getFechaCalendarioDia().compareTo(anteriorDiaCalendarioSeleccionado.getId().getFechaCalendarioDia()) == 0){
							calendarioDiaLocal.setNpEsSeleccionado(false);
							LogSISPE.getLog().info("::: fechas iguales");
							break;
						}
					}
				}else{
					//obtiene el d\u00EDa seleccionado anteriormente
					anteriorDiaCalendarioSeleccionado = calendarioBodegaPos.get(indiceAnterior);
					
					//verifica si el dia seleccionado anteriormente tambi\u00E9n pertenece a un dia del calendario SEL
					/*if(anteriorDiaCalendarioSeleccionado.getNpEsDistintoMes() && anteriorDiaCalendarioSeleccionado.getNpDiaMes() > 10){
						//obtengo el d\u00EDa del calendario SEL
						calendarioBodegaSel.get(calendarioBodegaSel.size() - (7 - indiceAnterior)).setNpEsSeleccionado(false);
					}*/
					for(CalendarioDiaLocalDTO calendarioDiaLocal : calendarioBodegaSel){
						LogSISPE.getLog().info(":::POS: calendarioDiaLocal {} ; anteriorDiaCalendarioSeleccionado {}"
								,calendarioDiaLocal.getId().getFechaCalendarioDia(),anteriorDiaCalendarioSeleccionado.getId().getFechaCalendarioDia());
						if(calendarioDiaLocal.getId().getFechaCalendarioDia().compareTo(anteriorDiaCalendarioSeleccionado.getId().getFechaCalendarioDia()) == 0){
							calendarioDiaLocal.setNpEsSeleccionado(false);
							LogSISPE.getLog().info("::: fechas iguales");
							break;
						}
					}
				}
				
				//actualiza los campos no persistentes para cambiar los estilos en el calendario
				nuevoDiaCalendarioSeleccionado.setNpEsSeleccionado(true);
				anteriorDiaCalendarioSeleccionado.setNpEsSeleccionado(false);
				
				//verifica si el d\u00EDa seleccionado pertenece al mes distinto al seleccionado
				if(nuevoDiaCalendarioSeleccionado.getNpEsDistintoMes()){
					//si en anterior
					if(diaSeleccionado > 10){
						mesSeleccionado--;
						if(mesSeleccionado < 0){
							mesSeleccionado = 11;
							anioSelecccionado--;
						}
					}
					//si posterior
					else if(diaSeleccionado < 10){
						mesSeleccionado++;
						if(mesSeleccionado > 11){
							mesSeleccionado = 0;
							anioSelecccionado++;
						}
					}
				}
				
				//establece la fecha seleccionada
				fechaSeleccionada.setTime(new Date());
				fechaSeleccionada.set(Calendar.YEAR, anioSelecccionado);
				fechaSeleccionada.set(Calendar.MONTH, mesSeleccionado);
				fechaSeleccionada.set(Calendar.DAY_OF_MONTH, diaSeleccionado);
				
				/**PRODUCCION**/
				//producci\u00F3n para el d\u00EDa seleccionado
				obtenerProduccionDia(request, fechaSeleccionada, NOMBRE_COL_DIA_SELEC, null);

				/**DESPACHO LOCALES**/
				//despacho a locales para el d\u00EDa seleccionado
				obtenerDespachoLocalDia(request, fechaSeleccionada, NOMBRE_COL_DIA_SELEC);
				
				/**DESPACHO DOMICILIO**/
				//despacho a locales para el d\u00EDa seleccionado
				obtenerDespachoDomicilioDia(request, fechaSeleccionada, NOMBRE_COL_DIA_SELEC);
				
				session.setAttribute(INDICE_DIA_SELECCIONADO, request.getParameter("diaSeleccionado"));
				//session.setAttribute(CALENDARIO_BODEGA.concat(CAL_SEL), calendarioBodega);
				session.setAttribute(FECHA_SELECCIONADA, fechaSeleccionada);
				session.setAttribute(FECHA_SELECCIONADA_NOMBRE, new Timestamp(fechaSeleccionada.getTimeInMillis()));
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				//errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
			}
		}
		/*----------------------Si seleccion\u00F3 ver detalle del Pedido-----------------------------*/
		else if(request.getParameter("codigoPedido") != null){
			LogSISPE.getLog().info("Entra a desplegar detalle del pedido");
			
			//se construye el objeto VistaPedidoDTO para realizar la consulta
			VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
			consultaVistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaVistaPedidoDTO.getId().setCodigoPedido(request.getParameter("codigoPedido"));
			consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

			//primero se obtienen los datos del pedido
			Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
			LogSISPE.getLog().info("coleccion colVistaPedidoDTO obtenida");
			//se obtiene el primer elemento
			if(colVistaPedidoDTO != null && !colVistaPedidoDTO.isEmpty()){
				VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.iterator().next();
				//se obtienen los detalles
				EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
				DetalleEstadoPedidoAction.obtenerEntregas(session,vistaPedidoDTO.getVistaDetallesPedidosReporte());
				DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				
				PaginaTab tabDetallePedido= new PaginaTab("detalleEstadoPedido", "deplegar", 0,335, request);
				Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
				Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
				tabDetallePedido.addTab(tabDetallePedidoComun);	
				tabDetallePedido.addTab(tabDetalleEntregas);
				beanSession.setPaginaTab(tabDetallePedido);
				LogSISPE.getLog().info("ec.com.smx.sic.sispe.tipoArticulo.despensa: {}", session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa"));
			}
		}
		else if(request.getParameter("codigoPedidoEntr") != null){
			LogSISPE.getLog().info("Mostrar pantalla de configuracion de entregas");
			String codigoPedido=request.getParameter("codigoPedidoEntr").trim();			
			String codigoArticulo=request.getParameter("codigoArticulo").trim();	
			LogSISPE.getLog().info("codigoPedido {}",codigoPedido);
			LogSISPE.getLog().info("codigoArticulo {}",codigoArticulo);
			//se construye el objeto VistaPedidoDTO para realizar la consulta
			VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
			consultaVistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaVistaPedidoDTO.getId().setCodigoPedido(codigoPedido);
			consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

			//primero se obtienen los datos del pedido
			Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
			LogSISPE.getLog().info("coleccion colVistaPedidoDTO obtenida");
			//se obtiene el primer elemento
			if(colVistaPedidoDTO != null && !colVistaPedidoDTO.isEmpty()){
				VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.iterator().next();
				//se obtienen los detalles
				EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
				LogSISPE.getLog().info("ec.com.smx.sic.sispe.tipoArticulo.despensa: {}", session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa"));
				for(Iterator <VistaDetallePedidoDTO> it = vistaPedidoDTO.getVistaDetallesPedidosReporte().iterator(); it.hasNext();){
					VistaDetallePedidoDTO visDetPedDTO = it.next();
					int i=0;
					if(visDetPedDTO.getId().getCodigoArticulo().equals(codigoArticulo)){
						construirDetallesPedidoDesdeVista(formulario,request,visDetPedDTO,vistaPedidoDTO,i);
					}
				}
			}
			cargarCalendarioEntregas(session,request,errors,formulario);
			//Obtener locales que no tienen productos perecibles
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO(Boolean.TRUE);
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.establecimientos.sinproductos.perecibles"));
			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
			String establecimientos= !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
			LogSISPE.getLog().info("Estableciminetos sin productos perecibles {}",establecimientos);
			String []establecimientosCol= establecimientos.split(",");
			
			session.setAttribute("ec.com.smx.sic.sispe.establecimientosCol", establecimientosCol);
			LogSISPE.getLog().info("tam establecimientosCol {}", establecimientosCol.length);
			for(int k=0;k<establecimientosCol.length;k++){
				LogSISPE.getLog().info("pos {}: elemento {}",k,establecimientosCol[k]);
			}
			
			session.setAttribute("ec.com.smx.sic.sispe.entregaArticuloLocal", "pedido");
			request.setAttribute("ec.com.smx.sic.sispe.codigoArticulo",codigoArticulo);
			session.removeAttribute(CONFIRMACION);
			
		}else if(request.getParameter("entregas") != null){
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
			session.removeAttribute(FECHADESPACHO);
			formulario.setFechaDespacho(null);
			LogSISPE.getLog().info("Mostrar calendario de entregas");
			LogSISPE.getLog().info("compania {}",request.getParameter("compania"));
			LogSISPE.getLog().info("codigoLocal {}",request.getParameter("codigoLocal"));
			String codigoLocal=request.getParameter("codigoLocal").trim();
			String textoCombo=request.getParameter("textoCombo").trim();
			LocalID localId= new LocalID();
			localId.setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
			LogSISPE.getLog().info("getFechaMinimaEntrega {}",vistaPedidoDTO.getFechaMinimaEntrega());
			localId.setCodigoLocal(Integer.parseInt(codigoLocal));
			session.setAttribute(SELECCIONARCALENDARIO, "ok");
			session.setAttribute(LOCALID, localId);
			session.setAttribute("ec.com.smx.sic.sispe.localSeleccionado", textoCombo);
			//fecha maxima
			Date fechaMaxima= new Date(vistaPedidoDTO.getFechaMinimaEntrega().getTime());
			
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO(Boolean.TRUE);
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
			
			String rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
			session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
			int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
			LogSISPE.getLog().info("parametroDiasDespacho: {}" , parametro);
			
			//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
			GregorianCalendar fechaCalendario = new GregorianCalendar();
			fechaCalendario.setTime(fechaMaxima);
			fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
			Date fechaMinima = fechaCalendario.getTime();
			LogSISPE.getLog().info("fechaMinima {}",fechaMinima);
			LogSISPE.getLog().info("fechaMaxima {}",fechaMaxima);
			
			/***************Actualiza el calendario********************/
				//obtengo el mes del dia que fue seleccionado para la entrega
				Date mes=new Date(System.currentTimeMillis());
				LogSISPE.getLog().info("mes {}",mes);
				session.setAttribute(FECHAMINIMA,fechaMinima);
				session.setAttribute(FECHAMAXIMA,fechaMaxima);
				obtenerCalendarioEntregas(session,request,localId,errors,mes,fechaMinima,fechaMaxima,formulario,true);
				LogSISPE.getLog().info("vistaPerdida {}",vistaPedidoDTO);
		}/***************************************NAVEGACION ENTRE MESES*************************************/
		else if(request.getParameter("mesAnterior")!=null){
			LogSISPE.getLog().info("mes anterior");
			//formulario.mantenerValoresEntregas(request);
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
			obtenerCalendarioEntregas(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
		}
		else if(request.getParameter("mesSiguiente")!=null){
			LogSISPE.getLog().info("mes siguiente");
			//formulario.mantenerValoresEntregas(request);
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
			obtenerCalendarioEntregas(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
		}
		//Guardar cambios en las entregas
		else if(request.getParameter("botonGuardarCambios")!=null && !request.getParameter("botonGuardarCambios").equals("")){
			LogSISPE.getLog().info("Entro a guardar los nuevos cambios de direccion a local desde bodega de guayaquil");
			try{
				String fechaDespacho=(String)session.getAttribute(FECHADESPACHO);
				LogSISPE.getLog().info("fechaDespacho {}",fechaDespacho);
				if(fechaDespacho!=null){
					session.removeAttribute(CONFIRMACION);
					String textoLocal= (String)session.getAttribute("ec.com.smx.sic.sispe.localSeleccionado");
					LogSISPE.getLog().info("Local {}",textoLocal);
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
					PedidoDTO pedidoDTO= new PedidoDTO();
					pedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
					pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
					pedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
					pedidoDTO.setNpDescripcionLocalOrigen(textoLocal);
					pedidoDTO.setNpNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
					pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombreContacto());
					pedidoDTO.setEntidadResponsable(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
					
					Collection<EntregaPedidoDTO> entregasCol = (Collection<EntregaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.entregasPerecibles");
					LocalID localSeleccionado=(LocalID)session.getAttribute(LOCALID);
					CalendarioDiaLocalDTO calendarioDiaLocalDTO=null;
					if(session.getAttribute(CALENDARIODIALOCAL)!=null){
						LogSISPE.getLog().info("si existe calendario");
						//obtengo el dia seleccionado
						calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
					}
					LogSISPE.getLog().info("****calendarioDiaLocalDTO**** {}",calendarioDiaLocalDTO);
					LogSISPE.getLog().info("getFechaDespacho{}",fechaDespacho);
					String nombreLocal=(String)session.getAttribute("ec.com.smx.sic.sispe.localSeleccionado");
					for(EntregaPedidoDTO entregaPedidoDTO : entregasCol){
						LogSISPE.getLog().info("Fecha Entrega cliente--{}",entregaPedidoDTO.getFechaEntregaCliente());
							LogSISPE.getLog().info("Fecha Despacho Bodega--{}",entregaPedidoDTO.getFechaDespachoBodega());
							LogSISPE.getLog().info("Direccion--{}",entregaPedidoDTO.getDireccionEntrega());
							LogSISPE.getLog().info("Nombre local--{}",nombreLocal);
							entregaPedidoDTO.setCodigoAreaTrabajoEntrega(localSeleccionado.getCodigoLocal());
							entregaPedidoDTO.setCodigoDivGeoPol(null);
							entregaPedidoDTO.setCodigoLocalSector(null);
							entregaPedidoDTO.setDireccionEntrega("LOCAL: "+localSeleccionado.getCodigoLocal()+" - "+nombreLocal.trim()+" - "+entregaPedidoDTO.getDireccionEntrega());
							entregaPedidoDTO.setCostoEntrega(null);
							entregaPedidoDTO.setTipoEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.local1"));
							entregaPedidoDTO.setCodigoContextoEntrega(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.otroLocal")));
							entregaPedidoDTO.setCodigoBodega(null);
							entregaPedidoDTO.setCalendarioDiaLocalDTO(calendarioDiaLocalDTO);
							SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
							Date fecha = formatoDelTexto.parse(fechaDespacho);
							entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fecha.getTime()));
					}
					if(formulario.validarCantidades(errors,request)==0){
						LogSISPE.getLog().info("Validacion correcta");
						CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1);
						LogSISPE.getLog().info("Calendario-->{}",calendarioConfiguracionDiaLocalDTO);
						SessionManagerSISPE.getServicioClienteServicio().transModificarDireccionEntregaGuayaquil(pedidoDTO,entregasCol,calendarioConfiguracionDiaLocalDTO);
						session.removeAttribute("ec.com.smx.sic.sispe.entregaArticuloLocal");
						infos.add("InfoEntrega",new ActionMessage("infos.asignacionNuevoLocal"));
					}
				}
				else{
					session.setAttribute(CONFIRMACION, "OK");
					errors.add("errorBusqueda",new ActionMessage("errors.SISPEException","Ud no ha seleccionado un local de entrega o una fecha de despacho"));
				}
			}catch (Exception e) {
				LogSISPE.getLog().info("Error: "+e);
				errors.add("dinamico", new ActionMessage("errors.SISPEException", e.getMessage()));
			}
			
		}
		else if(request.getParameter("botonMostrarConfirmacion")!=null && !request.getParameter("botonMostrarConfirmacion").equals("")){
			LogSISPE.getLog().info("Mostrar panel de confirmacion");
		}
		//Selecciona un dia del mes
		else if(request.getParameter("seleccionCal")!=null){
			LogSISPE.getLog().info("entra a seleccionar un dia del calendario");
			formulario.mantenerValoresEntregas(request);
			//if(formulario.validarFechaEntrega(errors, request)==0){
				//Entro a seleccionar un calendario
				seleccionDia(formulario,new Integer(request.getParameter("seleccionCal")).intValue(),session,true,errors,request);
				session.removeAttribute(DIRECCION);
			//}
				session.setAttribute("ec.com.smx.sic.sispe.configGuardar", "pedido");
		}
		/*----------------------Si seleccion\u00F3 ver detalle del Canasto----------------------------*/
		else if(request.getParameter("indiceDetallePedido") != null){
			LogSISPE.getLog().info("Entra a desplegar detalle de la canasta");
			LogSISPE.getLog().info("indiceDetallePedido: {}" , request.getParameter("indiceDetallePedido"));
			
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_ELIMINADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_AGREGADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_MODIFICADOS);
			session.removeAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
			
			Collection detalleCanasto = null;
			//se obtiene el indice del detalle de la cotizaci\u00F3n seleccionada
			int indice = Integer.parseInt(request.getParameter("indiceDetallePedido"));
			//se obtiene de sesi\u00F3n la colecci\u00F3n con el estado del detalle del pedido
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
			List colVistaDetallePedido = (List) vistaPedidoDTO.getVistaDetallesPedidosReporte();
			//se obtiene el objeto VistaDetallePedidoDTO
			VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)colVistaDetallePedido.get(indice);
			request.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO",vistaDetallePedidoDTO);
			
			//Obtener los art\u00EDculos de la receta
			if (vistaDetallePedidoDTO.getArticuloDTO() != null){
				if(vistaDetallePedidoDTO.getArticuloDTO().getTieneArticuloRelacionado()){
					//detalleCanasto = vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
					detalleCanasto =obtenerPreciosActualReceta(vistaDetallePedidoDTO);					
				}else{
					
					detalleCanasto = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
					
					if(vistaDetallePedidoDTO.getArticuloDTO() == null){
						vistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
					}
					vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(detalleCanasto);
				}
			}
			
			if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal() != null){
				Collection<Long> canastoOriginal = null;
				Collection<Long> canastoModificado = null;
				LogSISPE.getLog().info("Empieza el Proceso de Comparacion de Canasta Modificadas");
				//Tabs Canastas
				EstadoPedidoUtil.instanciarTabsCanasta(request,"calendarioBodega", forward);
				LogSISPE.getLog().info(":::::--Canasto Original--:::::");
				Collection <ArticuloRelacionDTO> colRecetaO = EstadoPedidoUtil.obtenerReceta(request, vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal());
				Collection colRecetaM = vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
				//Traigo los codigos de los items del canasto original y modificado
				canastoOriginal = EstadoPedidoUtil.itemsCanasta(request, colRecetaO);
				LogSISPE.getLog().info(":::::--Canasto Modificada--:::::{}",vistaDetallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().size());
				canastoModificado = EstadoPedidoUtil.itemsCanasta1(request, colRecetaM);
				EstadoPedidoUtil.comparacionItemsCanasta(request, canastoOriginal, canastoModificado, colRecetaO, colRecetaM);
				//Comparo las cantidades del CanastoO con el CanastoM
				EstadoPedidoUtil.comparacionCantidadesItemsCanasta(request, colRecetaO, colRecetaM);
				//se construye el objeto ARTICULO para la consulta del encabezado original
				ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
				consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    	    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
	    	    consultaArticuloDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal());
	    	    List <ArticuloDTO> articuloDTO = (ArrayList<ArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
	    	    if(articuloDTO != null && !articuloDTO.isEmpty()){
	    	    	ArticuloDTO art = (ArticuloDTO)articuloDTO.get(0);
	    	    	//Se guarda encabezado receta Original
	    	    	request.getSession().setAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL, art);
	    	    }
	    	    request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL, colRecetaO);
	    	    
			}
			
			//SE GUARDA ITEMS DE LA RECETA DEL ESTADODETALLERECETA
			request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_ACTUAL, detalleCanasto);
			request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",detalleCanasto);
			
			//se elimina esta variable para que no se imprima nuevamente el estado del detalle del pedido
			session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte");
			request.setAttribute("ORIGEN_DETALLE_CANASTA", "pedido");
			
			/*//SE GUARDA LA RECETA
			request.setAttribute(DETALLE_CANASTA,detalleCanasto);
			
			//se elimina esta variable para que no se imprima nuevamente el estado del detalle del pedido
			session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte");*/
		}
		/*-- Control de Tabs--*/
		else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
			LogSISPE.getLog().info("Control Tabs Calendario");
			session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta");
			/*---------- si se escogi\u00F3 el tag de Canasta Estado Actual --------------*/
			if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
				/*--------------- si se escogi\u00F3 el tab de pedidos por entregar ----------------*/
				LogSISPE.getLog().info("Entro por el tab Canasto Estado Actual");
				Collection<EstadoDetalleRecetaDTO> colRecetaAc = (ArrayList<EstadoDetalleRecetaDTO>)session.getAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
				session.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",colRecetaAc);
			}
			/*------------ si se escogi\u00F3 el tab de Canasta Estado Detalle Modificado --------------*/    
			else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
				LogSISPE.getLog().info("Entro por el tab Canasto Estado Detalle Modificado");
				Collection <ArticuloRelacionDTO> colRecetaO = (ArrayList<ArticuloRelacionDTO>)session.getAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
				session.setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",colRecetaO);	
			}
			forward = "estadoDetalleCanasto";
		}
		/*----------------------Cerrar PopUp DetallePedido---------------------------------------*/
		else if(request.getParameter("cerrarPopUpDetalle") != null){
			LogSISPE.getLog().info("Cerrar PopUp Detalle");
		}
		/*----------------------Mostrar Solo Detalle de Canasta---------------------------------------*/
		else if(request.getParameter("codigoArticulo") != null){
			LogSISPE.getLog().info("Mostrar solo detalle de canasta");
			
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_ELIMINADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_AGREGADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_MODIFICADOS);
			session.removeAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
			
			Collection detalleReceta = null;
			EstadoDetalleRecetaDTO estadoDetalleRecetaFiltro = new EstadoDetalleRecetaDTO();
			estadoDetalleRecetaFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			//estadoDetalleRecetaFiltro.getId().setCodigoRecetaArticulo(request.getParameter("codigoArticulo"));
			estadoDetalleRecetaFiltro.getId().setCodigoArticulo(request.getParameter("codigoArticulo"));
			estadoDetalleRecetaFiltro.getId().setSecuencialEstadoPedido(request.getParameter("secuencialEstadoPedido"));
			//estadoDetalleRecetaFiltro.setArticuloDTO(new ArticuloDTO());
			//estadoDetalleRecetaFiltro.setArticuloDTO(new ArticuloDTO());			
			
			estadoDetalleRecetaFiltro.setRecetaArticuloDTO(new ArticuloRelacionDTO());
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().setArticuloRelacion(new ArticuloDTO());		
			
			//para impuestos
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().getArticuloRelacion().setArticuloImpuestoCol(new ArrayList<ArticuloImpuestoDTO>());
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().getArticuloRelacion().getArticuloImpuestoCol().add(new ArticuloImpuestoDTO());
			
			//para medidas
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().getArticuloRelacion().setArticuloMedidaDTO(new ArticuloMedidaDTO());
			
			//codigo de barras
			ArticuloBitacoraCodigoBarrasDTO artCodBarDTO= new ArticuloBitacoraCodigoBarrasDTO();
			artCodBarDTO.setEstadoArticuloBitacora(estadoActivo);
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().getArticuloRelacion().setArtBitCodBarCol(new ArrayList<ArticuloBitacoraCodigoBarrasDTO>());
			estadoDetalleRecetaFiltro.getRecetaArticuloDTO().getArticuloRelacion().getArtBitCodBarCol().add(artCodBarDTO);
			
			//SE GUARDA LA RECETA
			detalleReceta = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoDetalleReceta(estadoDetalleRecetaFiltro);
			request.setAttribute(DETALLE_CANASTA, detalleReceta);
			
			if(StringUtils.isNotEmpty(request.getParameter("codigoArtOri")) && request.getParameter("codigoArtOri") != null){
				Collection<Long> canastoOriginal = null;
				Collection<Long> canastoModificado = null;
				LogSISPE.getLog().info("Empieza el Proceso de Comparacion de Canasta Modificadas");
				//Tabs Canastas
				EstadoPedidoUtil.instanciarTabsCanasta(request, "calendarioBodega", forward);
				LogSISPE.getLog().info(":::::--Canasto Original--:::::");
				Collection <ArticuloRelacionDTO> colRecetaO = EstadoPedidoUtil.obtenerReceta(request, request.getParameter("codigoArtOri"));
				Collection colRecetaM = detalleReceta;
				//Traigo los codigos de los items del canasto original y modificado
				canastoOriginal = EstadoPedidoUtil.itemsCanasta(request, colRecetaO);
				LogSISPE.getLog().info(":::::--Canasto Modificada--:::::{}", detalleReceta.size());
				canastoModificado = EstadoPedidoUtil.itemsCanasta1(request, colRecetaM);
				EstadoPedidoUtil.comparacionItemsCanasta(request, canastoOriginal, canastoModificado, colRecetaO, colRecetaM);
				//Comparo las cantidades del CanastoO con el CanastoM
				EstadoPedidoUtil.comparacionCantidadesItemsCanasta(request, colRecetaO, colRecetaM);
				//se construye el objeto ARTICULO para la consulta del encabezado original
				ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
				consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    	    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
	    	    consultaArticuloDTO.getId().setCodigoArticulo(request.getParameter("codigoArtOri"));
	    	    List <ArticuloDTO> articuloDTO = (ArrayList<ArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
	    	    if(articuloDTO != null && !articuloDTO.isEmpty()){
	    	    	ArticuloDTO art = (ArticuloDTO)articuloDTO.get(0);
	    	    	//Se guarda encabezado receta Original
	    	    	request.getSession().setAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL, art);
	    	    }
	    	    request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL, colRecetaO);
	    	    
			}
			
			//SE GUARDA ITEMS DE LA RECETA DEL ESTADODETALLERECETA
			request.getSession().setAttribute(EstadoPedidoUtil.CANASTO_ACTUAL, detalleReceta);
			request.getSession().setAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta",detalleReceta);
			
			//se elimina esta variable para que no se imprima nuevamente el estado del detalle del pedido
			session.removeAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.reporte");
			request.setAttribute("ORIGEN_DETALLE_CANASTA", "pedido");
		}
		/*----------------------Mostrar Solo Detalle de Canasta en Producci\u00F3n en L\u00EDnea*/
		else if(request.getParameter("codigoArticuloLP") != null){
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_ELIMINADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_AGREGADOS);
			session.removeAttribute(EstadoPedidoUtil.COL_ITEMS_MODIFICADOS);
			session.removeAttribute(EstadoPedidoUtil.ENCABEZADO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_0RIGINAL);
			session.removeAttribute(EstadoPedidoUtil.CANASTO_ACTUAL);
			LogSISPE.getLog().info("Entro a Mostrar solo detalle de canasta");
			request.removeAttribute(DETALLE_CANASTALP);// setAttribute(DETALLE_CANASTALP
			try{
				
				//Obtengo la receta original validado desde el sic
				request.setAttribute(DETALLE_CANASTALP, EstadoPedidoUtil.obtenerReceta(request, request.getParameter("codigoArticuloLP")));
			}catch(SISPEException e){
				//error.add("No existe ese Art\u00EDculo en el sic",new ActionMessage("errors.articulos.ArticuloNoExistente"));
			}
		}
		/*----------------------Actualizar Pantalla---------------------------------------*/
		else if(request.getParameter("actualizar") != null){
			LogSISPE.getLog().info("Entro a actualizar");
			Calendar fechaActual = null;
			Calendar fechaPosteriorActual = null;
			Calendar fechaBaseCalendario = null;
			Calendar fechaBaseCalendarioSiguiente = null;
			
		  	try{
				//fecha actual
				fechaActual = new GregorianCalendar();
				fechaActual.setTime(new Date());
				//fecha posterior a la actual
				fechaPosteriorActual = (GregorianCalendar)session.getAttribute(FECHA_SELECCIONADA);
				//fechaBase para el calendario
				fechaBaseCalendario = (GregorianCalendar)fechaActual.clone();
				fechaBaseCalendario.set(Calendar.MONTH, (Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_SEL)));
				fechaBaseCalendario.set(Calendar.YEAR, (Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_SEL)));
				fechaBaseCalendario.set(Calendar.DAY_OF_MONTH, 1);
				//fechaBase para el calendario siguiente
				fechaBaseCalendarioSiguiente = (GregorianCalendar)fechaActual.clone();
				fechaBaseCalendarioSiguiente.set(Calendar.MONTH, (Integer)session.getAttribute(MES_SELECCIONADO.concat(CAL_POS)));
				fechaBaseCalendarioSiguiente.set(Calendar.YEAR, (Integer)session.getAttribute(ANIO_CALENDARIO.concat(CAL_POS)));
				fechaBaseCalendarioSiguiente.set(Calendar.DAY_OF_MONTH, 1);
		  		
				/**PRODUCCION**/
				//producci\u00F3n para el d\u00EDa actual
				obtenerProduccionDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL, true);
				//producci\u00F3n para el d\u00EDa seleccionado
				obtenerProduccionDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC, null);

				/**DESPACHO LOCALES**/
				//despacho a locales para el d\u00EDa actual
				obtenerDespachoLocalDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
				//despacho a locales para el d\u00EDa seleccionado
				obtenerDespachoLocalDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
				
				/**ENTREGA DOMICILIO**/
				//despacho a locales para el d\u00EDa actual
				obtenerDespachoDomicilioDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
				//despacho a locales para el d\u00EDa seleccionado
				obtenerDespachoDomicilioDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
				
				/**CALENDARIO**/
				obtenerCalendario(request, fechaBaseCalendario, fechaPosteriorActual, CAL_SEL);
				obtenerCalendario(request, fechaBaseCalendarioSiguiente, fechaPosteriorActual, CAL_POS);
	    	  	
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}
		}
		/*----------------------imprimir secci\u00F3n d\u00EDa hoy-----------------------------*/
		else if(request.getParameter("ayuda") != null && request.getParameter("ayuda").equals("imprimirDiaHoy")){
			LogSISPE.getLog().info("Imprimir Secci\u00F3n D\u00EDa Hoy");
			//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
			request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
			//variable para idicar el d\u00EDa a imprimir
			session.setAttribute(IMPRESION_DIA, NOMBRE_COL_DIA_ACTUAL.substring(1));
			//Inicio de Impresion  
			Calendar fechaActual = new GregorianCalendar();
			fechaActual.setTime(new Date());
			/**DESPACHO LOCALES**/
			//despacho a locales para el d\u00EDa actual
			obtenerDespachoLocalDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
			/**ENTREGA DOMICILIO**/
			//despacho a locales para el d\u00EDa actual
			obtenerDespachoDomicilioDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
			/**ENTREGA DOMICILIO GUAYAQUIL**/
			request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir.domicilio.guayaquil", "ok");
			obtenerDespachoDomicilioDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
			
		}
		/*----------------------imprimir secci\u00F3n d\u00EDa seleccionado-----------------------------*/
		else if(request.getParameter("ayuda") != null && request.getParameter("ayuda").equals("imprimirDiaSel")){
			LogSISPE.getLog().info("Imprimir Secci\u00F3n D\u00EDa Seleccionado");
			//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
			request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
			//variable para idicar el d\u00EDa a imprimir
			session.setAttribute(IMPRESION_DIA, NOMBRE_COL_DIA_SELEC.substring(1));
			//Inicio de Impresion
			Date fechaCambiada = new Date();
			fechaCambiada = ConverterUtil.parseStringToDate(String.valueOf(session.getAttribute(FECHA_SELECCIONADA_NOMBRE)));
			Calendar fechaPosteriorActual = new GregorianCalendar();
			fechaPosteriorActual.setTime(fechaCambiada);
			
			/**DESPACHO LOCALES**/
			//despacho a locales para el d\u00EDa seleccionado
			obtenerDespachoLocalDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
			/**ENTREGA DOMICILIO**/
			//entrega a domicilio para el d\u00EDa seleccionado
			obtenerDespachoDomicilioDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
			/**ENTREGA DOMICILIO GUAYAQUIL**/
			request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir.domicilio.guayaquil", "ok");
			obtenerDespachoDomicilioDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
		}
		/*----------------------Si entra a la p\u00E1gina por primera vez-----------------------------*/
		else{
			LogSISPE.getLog().info("Entra por primera vez");
			Calendar fechaActual = null;
			Calendar fechaPosteriorActual = null;
			Calendar fechaCalendarioSiguiente = null;
			
		  	try{
		  		//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
				SessionManagerSISPE.removeVarSession(request);
				//se obtiene el estado para los pedidos reservado, en producci\u00F3n y anulado
				session.setAttribute("sispe.estado.activo", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoReservado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"));
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoEnProduccion", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"));
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"));
				session.removeAttribute(ACCESO_USUARIO_CALENDARIO);
				
				//Verifico que el usuario logeado tenga clasificaciones asignadas
				if(verificarUsuarioLogeadoBodega(request)){
					//LogSISPE.getLog().info("Usuario Logeado tiene Permisos de las clasificaciones 1606/1602");
					session.setAttribute(ACCESO_USUARIO_CALENDARIO, "ok");
					//fecha actual
					fechaActual = new GregorianCalendar();
					fechaActual.setTime(new Date());
					session.setAttribute(FECHA_ACTUAL_NOMBRE, new Timestamp(System.currentTimeMillis()));
					//fecha posterior a la actual
					fechaPosteriorActual = new GregorianCalendar();
					fechaPosteriorActual.setTime(new Date());
					fechaPosteriorActual.add(Calendar.DAY_OF_MONTH, 1);
					session.setAttribute(FECHA_SELECCIONADA, fechaPosteriorActual);
					session.setAttribute(FECHA_SELECCIONADA_NOMBRE, new Timestamp(fechaPosteriorActual.getTimeInMillis()));
					//sube a session el mes de la fecha actual para el calendario seleccionado
					session.setAttribute(MES_SELECCIONADO.concat(CAL_SEL), fechaActual.get(Calendar.MONTH));
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_SEL), MESES_ANIO[fechaActual.get(Calendar.MONTH)]);
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_SEL), fechaActual.get(Calendar.YEAR));
					//sube a session el mes para el calendario siguiente
					fechaCalendarioSiguiente = (GregorianCalendar)fechaActual.clone();
					fechaCalendarioSiguiente.set(Calendar.DAY_OF_MONTH, 1);
					fechaCalendarioSiguiente.add(Calendar.MONTH, 1);
					session.setAttribute(MES_SELECCIONADO.concat(CAL_POS), fechaCalendarioSiguiente.get(Calendar.MONTH));
					session.setAttribute(MES_SELECCIONADO_NOMBRE.concat(CAL_POS), MESES_ANIO[fechaCalendarioSiguiente.get(Calendar.MONTH)]);
					session.setAttribute(ANIO_CALENDARIO.concat(CAL_POS), fechaCalendarioSiguiente.get(Calendar.YEAR));
			  		
					//USUARIOS BODEGA
					/**PRODUCCION**/
					//producci\u00F3n para el d\u00EDa actual
					obtenerProduccionDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL, true);
					//producci\u00F3n para el d\u00EDa seleccionado
					obtenerProduccionDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC, null);	
					
					/**DESPACHO LOCALES**/
					//despacho a locales para el d\u00EDa actual
					obtenerDespachoLocalDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
					//despacho a locales para el d\u00EDa seleccionado
					obtenerDespachoLocalDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
					
					/**ENTREGA DOMICILIO**/
					//despacho a locales para el d\u00EDa actual
					obtenerDespachoDomicilioDia(request, fechaActual, NOMBRE_COL_DIA_ACTUAL);
					//despacho a locales para el d\u00EDa seleccionado
					obtenerDespachoDomicilioDia(request, fechaPosteriorActual, NOMBRE_COL_DIA_SELEC);
					
					/**CALENDARIO**/
					obtenerCalendario(request, fechaActual, fechaPosteriorActual, CAL_SEL);
					obtenerCalendario(request, fechaCalendarioSiguiente, fechaPosteriorActual, CAL_POS);
				}else{
					LogSISPE.getLog().info("El usuario logeado no posee clasificaciones");
					errors.add("usuarioSinClasificaciones", new ActionMessage("errors.calendarioBodega.usuarioSinClasificaciones"));
				}
			}catch(Exception ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				//errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
			}
		}
				
		saveErrors(request,errors);
		//saveMessages(request,messages);
		saveInfos(request, infos);
		return mapping.findForward(forward);
	}

	private Collection<ArticuloRelacionDTO> obtenerPreciosActualReceta(VistaDetallePedidoDTO vistaDetallePedidoDTO) {
		ArticuloRelacionDTO artRelDTO= new ArticuloRelacionDTO();
		artRelDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
		artRelDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
		
		artRelDTO.setArticuloRelacion(new ArticuloDTO());
		artRelDTO.getArticuloRelacion().setNpCodigoLocal(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
		
		artRelDTO.getArticuloRelacion().setArtBitCodBarCol(new ArrayList<ArticuloBitacoraCodigoBarrasDTO>());
		artRelDTO.getArticuloRelacion().getArtBitCodBarCol().add(new ArticuloBitacoraCodigoBarrasDTO());
		artRelDTO.getArticuloRelacion().setArticuloProveedorCol(new ArrayList<ArticuloProveedorDTO>());
		artRelDTO.getArticuloRelacion().getArticuloProveedorCol().add(new ArticuloProveedorDTO());
		artRelDTO.getArticuloRelacion().setClasificacionDTO(new ClasificacionDTO());
		
		Collection<ArticuloRelacionDTO> artRelCol= SICFactory.getInstancia().articulo.getArticuloService().obtenerArticuloVenta(artRelDTO, ArticuloRelacionDTO.class);
		return artRelCol;
	}

	/**
	 * 
	 * @param request
	 * @param fechaConsulta
	 * @param diaCol
	 * @throws Exception
	 */
	private void obtenerProduccionDia(HttpServletRequest request, Calendar fechaConsulta, String diaCol, Boolean produccionPendiente) throws Exception{
		//valida si obtiene la producci\u00F3n
		if(((String)request.getSession().getAttribute(BANDERA_BODEGA_TRANSITO)).equals(estadoInactivo) 
				&& ((String)request.getSession().getAttribute(BANDERA_BODEGA_POR_CLASIFICACION)).equals(estadoInactivo) ){
			LogSISPE.getLog().info("obtenerProduccionDia {}", fechaConsulta);
			HttpSession session = request.getSession();
			Collection colCanastaArticuloDTO = null;
			Collection colCanastaEspArticuloDTO = null;
			Collection colDespensaArticuloDTO = null;
			Collection colDespensaEspArticuloDTO = null;
			Collection colCanastaLinArticuloDTO = null;
			Collection colDespensaLinArticuloDTO = null;
			
			VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
			VistaCalendarioArticuloDTO consultaVistaCalendarioArticuloDTO = new VistaCalendarioArticuloDTO();
			Calendar fechaAcualSumada = (GregorianCalendar)fechaConsulta.clone();
			Date fechaConsultaAMD = new Date(fechaConsulta.getTimeInMillis());
			//fechas de inicio y fin de d\u00EDa
			Date fechaInicio = new Date();
		  	Date fechaFin = new Date();
		  	
			//obtiene par\u00E1metro con el rango de d\u00EDas para obtener la producci\u00F3n de pedidos con respecto a la fecha de despacho
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.rangoDiasProduccionPedidos", request);
			//suma el par\u00E1metro a la fecha actual
			fechaAcualSumada.add(Calendar.DAY_OF_MONTH, Integer.parseInt(parametroDTO.getValorParametro())); 
			
	  		//Obtengo la fecha Fin del d\u00EDa
			fechaAcualSumada.set(Calendar.HOUR_OF_DAY, 23);
			fechaAcualSumada.set(Calendar.MINUTE, 59);
			fechaAcualSumada.set(Calendar.SECOND, 0);
			fechaAcualSumada.set(Calendar.MILLISECOND, 0);
	  		fechaFin = fechaAcualSumada.getTime();
	  		
	  		//Obtengo la fecha de Inicio del d\u00EDa
	  		fechaAcualSumada.add(Calendar.DAY_OF_MONTH, -1);
	  		fechaInicio = fechaAcualSumada.getTime();
	  		
	  		//clasificacion para nuevas recetas
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
			String clasificacionNuevaReceta=parametroDTO.getValorParametro();
	  		//establece los campos para la b\u00FAsqueda
			consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaVistaArticuloDTO.getId().setCodigoEstado(estadoReservado.concat(",").concat(estadoEnProduccion));
			//valida la fecha que env\u00EDa para obtener producci\u00F3n pendiente
			if(produccionPendiente != null && produccionPendiente){
				ParametroDTO parametroDTOF = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroMesesProduccionPendiente", request);
				fechaAcualSumada.add(Calendar.MONTH, -(Integer.parseInt(parametroDTOF.getValorParametro())));
				fechaInicio = fechaAcualSumada.getTime();
			}
			consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(new Timestamp(fechaInicio.getTime()));
			consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(new Timestamp(fechaFin.getTime()));
			consultaVistaArticuloDTO.setArticuloCompletado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion"));
			consultaVistaArticuloDTO.setNpCodigoClasificacion(parametroDTO.getValorParametro());
			consultaVistaArticuloDTO.setNpPendientes(produccionPendiente);
			consultaVistaArticuloDTO.setNpEstadoPedidoReserva(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoReservado.confirmado"));
			
			/**Descomentar esta l\u00EDnea si se desea realizar la b\u00FAsqueda por clasificaciones del usuario**/
			//consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			
			//Obtengo valor por defecto entidad Responsable (BOD).
			String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
			consultaVistaArticuloDTO.setEntidadResponsable(entidadBodega);
			//bandera para la bodega de transito
			consultaVistaArticuloDTO.setNpFiltroBodegaTransito(null);
	
			//llamada al m\u00E9todo de la capa de servicio
			
			//subClasificacion para articulos tipo canastos 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
			
			//subClasificacion para articulos tipo despensas 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
	  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
			//para canasta de cat\u00E1logo
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloLocal"));
			//consultaVistaArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
			consultaVistaArticuloDTO.setCodigoSubClasificacion(valorTipoArticuloCanastas);
			consultaVistaArticuloDTO.setNpDesplegarItemsReceta(estadoInactivo);
			consultaVistaArticuloDTO.setNpCodigoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"));
			colCanastaArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(CANASTA).concat(CATALOGO), colCanastaArticuloDTO);
			//para canasta especial
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.pedidoArticuloEsp"));
			consultaVistaArticuloDTO.setNpDesplegarItemsReceta(estadoActivo);
			consultaVistaArticuloDTO.setNpCodigoClasificacion(clasificacionNuevaReceta);
			colCanastaEspArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(CANASTA).concat(ESPECIAL), colCanastaEspArticuloDTO);
			//para despensa de cat\u00E1logo
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloLocal"));
			//consultaVistaArticuloDTO.setCodigoTipoArticulo(parametroDTO.getValorParametro());
			consultaVistaArticuloDTO.setCodigoSubClasificacion(valorTipoArticuloDespensas);
			consultaVistaArticuloDTO.setNpDesplegarItemsReceta(estadoInactivo);
			consultaVistaArticuloDTO.setNpCodigoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"));
			colDespensaArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(DESPENSA).concat(CATALOGO), colDespensaArticuloDTO);
			//para despensa especial
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.pedidoArticuloEsp"));
			consultaVistaArticuloDTO.setNpDesplegarItemsReceta(estadoActivo);
			consultaVistaArticuloDTO.setNpCodigoClasificacion(clasificacionNuevaReceta);
			colDespensaEspArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(DESPENSA).concat(ESPECIAL), colDespensaEspArticuloDTO);
			
			consultaVistaCalendarioArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			if(produccionPendiente != null && produccionPendiente){
				consultaVistaCalendarioArticuloDTO.setEsPendiente(estadoActivo);
				consultaVistaCalendarioArticuloDTO.setNpFechaFin(fechaConsultaAMD);
				consultaVistaCalendarioArticuloDTO.setNpFechaInicio(fechaInicio);
			}else{
				consultaVistaCalendarioArticuloDTO.setFechaCalendario(fechaConsultaAMD);
			}
			
			//l\u00EDnea de producci\u00F3n canasta
			//consultaVistaCalendarioArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
			consultaVistaCalendarioArticuloDTO.setCodigoSubClasificacion(valorTipoArticuloCanastas);
			colCanastaLinArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(consultaVistaCalendarioArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(CANASTA).concat(LINEA), colCanastaLinArticuloDTO);
			
			//l\u00EDnea de producci\u00F3n despensa
			//subClasificacion para articulos tipo despensas 
			//consultaVistaCalendarioArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
			consultaVistaCalendarioArticuloDTO.setCodigoSubClasificacion(valorTipoArticuloDespensas);
			colDespensaLinArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(consultaVistaCalendarioArticuloDTO);
			session.setAttribute(COLECCION_ARTICULOS_PRODUCCION.concat(diaCol).concat(DESPENSA).concat(LINEA), colDespensaLinArticuloDTO);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param fechaConsulta
	 * @param diaCol
	 * @throws SISPEException
	 * @throws Exception
	 */
	private void obtenerDespachoLocalDia(HttpServletRequest request, Calendar fechaConsulta, String diaCol) throws SISPEException, Exception{
		LogSISPE.getLog().info("obtenerDespachoLocalDia {}", fechaConsulta);
		HttpSession session = request.getSession();
		Collection colCanastaArticuloDTO = null;
		//Collection colDespensaArticuloDTO = null;
		VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
		Calendar fechaActual = (GregorianCalendar)fechaConsulta.clone();
		//fechas de inicio y fin de d\u00EDa
		Date fechaInicio = new Date();
	  	Date fechaFin = new Date();
	  	
  		//Obtengo la fecha Fin del d\u00EDa
  		fechaActual.set(Calendar.HOUR_OF_DAY, 23);
  		fechaActual.set(Calendar.MINUTE, 59);
  		fechaActual.set(Calendar.SECOND, 0);
  		fechaActual.set(Calendar.MILLISECOND, 0);
  		fechaFin = fechaActual.getTime();
  		
  		//Obtengo la fecha de Inicio del d\u00EDa
	  	fechaActual.add(Calendar.DAY_OF_MONTH, -1);
	  	fechaInicio = fechaActual.getTime();
		
  		//establece los campos para la b\u00FAsqueda
		consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//consultaVistaArticuloDTO.getId().setCodigoEstado(estadoProducido.concat(",").concat(estadoDespachado));
		//consultaVistaArticuloDTO.setArticuloCompletado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion"));
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(new Timestamp(fechaInicio.getTime()));
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(new Timestamp(fechaFin.getTime()));
		consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.localPedidoArticulo"));
		consultaVistaArticuloDTO.setNpEsDespachoLocal(true);
		consultaVistaArticuloDTO.setNpEstadoPedidoReserva(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoReservado.confirmado"));
		consultaVistaArticuloDTO.setNumeroTotalEntregas(0);
		consultaVistaArticuloDTO.setNumeroEntregados(0);
		
		if(((String)session.getAttribute(BANDERA_BODEGA_TRANSITO)).equals(estadoActivo)){
			//consultaVistaArticuloDTO.getId().setCodigoEstado(estadoDespachado.concat(",").concat(estadoEntregado));
			consultaVistaArticuloDTO.setNpFiltroBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"));
		}else{
			if(((String)session.getAttribute(BANDERA_BODEGA_POR_CLASIFICACION)).equals(estadoActivo)){
				consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			}else{
				//Obtengo valor por defecto entidad Responsable (BOD).
				consultaVistaArticuloDTO.setEntidadResponsable(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
				//consultaVistaArticuloDTO.getId().setCodigoEstado(estadoProducido.concat(",").concat(estadoDespachado));
				if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir") == null){
					LogSISPE.getLog().info("--No entro por la Opcion Impresion--");
					//subClasificacion para articulos tipo canastos 
			  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			  		String codigosSubClasificaciones=parametroDTO.getValorParametro();
			  		//subClasificacion para articulos tipo despensas 
			  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
					//para canasta y despensa
					//consultaVistaArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta").concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa")));
					consultaVistaArticuloDTO.setCodigoSubClasificacion(codigosSubClasificaciones.concat(",").concat(parametroDTO.getValorParametro()));
				}/*else{
					//obtiene los despachos a Guayaquil con 2 d\u00EDas de anticipaci\u00F3n
					consultaVistaArticuloDTO.setCodigoCiudadEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"));
				}*/
			}
		}
		
		///llamada al m\u00E9todo de la capa de servicio
		colCanastaArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
		if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir") != null){
			LogSISPE.getLog().info("--Variable de session de Impresion--");
			session.setAttribute(COLECCION_ARTICULOS_DESPACHO_LOCAL_IMPRESION.concat(diaCol).concat(CANASTA), colCanastaArticuloDTO);
		}else{
			session.setAttribute(COLECCION_ARTICULOS_DESPACHO_LOCAL.concat(diaCol).concat(CANASTA), colCanastaArticuloDTO);
		}
		
	}
	
	/**
	 * 
	 * @param request
	 * @param fechaConsulta
	 * @param diaCol
	 * @throws SISPEException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void obtenerDespachoDomicilioDia(HttpServletRequest request, Calendar fechaConsulta, String diaCol) throws SISPEException, Exception{
		LogSISPE.getLog().info("obtenerDespachoDomicilioDia {}", fechaConsulta);
		HttpSession session = request.getSession();
		VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
		Calendar fechaActual = (GregorianCalendar)fechaConsulta.clone();
		Collection despachosCanastaDomicilio = null;
		//Collection despachosDespensaDomicilio = null;
		//fechas de inicio y fin de d\u00EDa
		Calendar fechaInicio = null;
		Calendar fechaFin = null;
	  	
  		//Obtengo la fecha Fin del d\u00EDa
  		fechaActual.set(Calendar.HOUR_OF_DAY, 23);
  		fechaActual.set(Calendar.MINUTE, 59);
  		fechaActual.set(Calendar.SECOND, 0);
  		fechaActual.set(Calendar.MILLISECOND, 0);
  		fechaFin = (GregorianCalendar)fechaActual.clone();
  		
  		//Obtengo la fecha de Inicio del d\u00EDa
	  	fechaActual.add(Calendar.DAY_OF_MONTH, -1);
	  	fechaInicio = (GregorianCalendar)fechaActual.clone();
		
	  	//establece los campos para la b\u00FAsqueda
		consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//consultaVistaArticuloDTO.getId().setCodigoEstado(estadoDespachado.concat(",").concat(estadoEntregado));
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(new Timestamp(fechaInicio.getTimeInMillis()));
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(new Timestamp(fechaFin.getTimeInMillis()));
		consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.fechaEntregaPedidoArticulo"));
		consultaVistaArticuloDTO.setNpEsDespachoLocal(false);
		consultaVistaArticuloDTO.setNpEstadoPedidoReserva(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoReservado.confirmado"));
		consultaVistaArticuloDTO.setNumeroTotalEntregas(0);
		consultaVistaArticuloDTO.setNumeroEntregados(0);
		
		//Filtro para los usuarios que pertencen a la bodega Transito
		String codigoBodegaTransito = null;
		if(((String)session.getAttribute(BANDERA_BODEGA_TRANSITO)).equals(estadoActivo)){
			codigoBodegaTransito = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito");
		}else{
			if(((String)request.getSession().getAttribute(BANDERA_BODEGA_POR_CLASIFICACION)).equals(estadoActivo)){
				consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			}else{
				//Obtengo valor por defecto entidad Responsable (BOD) solo este filtro es para la bodega de canastos.
				consultaVistaArticuloDTO.setEntidadResponsable(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
				if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir") == null){
					LogSISPE.getLog().info("--No entro por la Opcion Impresion--");
					//subClasificacion para articulos tipo canastos 
			  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			  		String codigosSubClasificaciones=parametroDTO.getValorParametro();
			  		//subClasificacion para articulos tipo despensas 
			  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
					//para canasta y despensa
					//consultaVistaArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta").concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa")));
					consultaVistaArticuloDTO.setCodigoSubClasificacion(codigosSubClasificaciones.concat(",").concat(parametroDTO.getValorParametro()));
				}else{
					//obtiene las entregas a domicilio anticipadas de la ciudad especificada
					consultaVistaArticuloDTO.setCodigoCiudadEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"));
					if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir.domicilio.guayaquil") != null){
						consultaVistaArticuloDTO.setNpDomicilioSoloGuayaqui(true);
					}
				}
			}
		}	
		consultaVistaArticuloDTO.setNpFiltroBodegaTransito(codigoBodegaTransito);
		//bandera para obtener el numero total de camiones
		consultaVistaArticuloDTO.setNpObtenerCamiones(estadoInactivo);
		//llamada al m\u00E9todo de la capa de servicio
		despachosCanastaDomicilio = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
		//buscar articulos perecibles para setear en variable no persistente
		Boolean tienePerecibles=Boolean.FALSE;
		for(int i=0;i<despachosCanastaDomicilio.size();i++){
			VistaArticuloDTO visArtDTO=(VistaArticuloDTO)despachosCanastaDomicilio.iterator().next();
			Collection<VistaArticuloDTO> visArtCol2= visArtDTO.getColVistaArticuloDTO();
			for(VistaArticuloDTO visArtDTO2:visArtCol2){
				for(VistaArticuloDTO visArtDTO3:visArtDTO2.getColVistaArticuloDTO()){
					if(verificarArticuloPerecible(request,visArtDTO3.getCodigoClasificacion())){
						LogSISPE.getLog().info("Es perecible");
						visArtDTO3.setNpEsPerecible(Boolean.TRUE);
						tienePerecibles=Boolean.TRUE;
					}
					else{
						LogSISPE.getLog().info("No es perecible");
						visArtDTO3.setNpEsPerecible(Boolean.FALSE);
					}
				}
			}
		}
		session.setAttribute("ec.com.smx.sic.sispe.tieneperecibles", tienePerecibles);
		
		//Verificio si entro por el metodo de impresion
		if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir") != null){
			LogSISPE.getLog().info("--Variable de session de Impresion--");
			if(request.getAttribute("ec.com.smx.sic.sispe.funcionImprimir.domicilio.guayaquil") != null){
				session.setAttribute(COLECCION_ARTICULOS_DESPACHO_DOMICILIO_GUAYAQUIL_IMPRESION.concat(diaCol).concat(CANASTA), despachosCanastaDomicilio);
			}else{
				session.setAttribute(COLECCION_ARTICULOS_DESPACHO_DOMICILIO_IMPRESION.concat(diaCol).concat(CANASTA), despachosCanastaDomicilio);
			}
		}else{
			session.setAttribute(COLECCION_ARTICULOS_DESPACHO_DOMICILIO.concat(diaCol).concat(CANASTA), despachosCanastaDomicilio);
		}
	}
	/**
	 * 
	 * @param request
	 * @param codigoClasificacionArticulo
	 * @return
	 * @throws Exception 
	 * @throws MissingResourceException 
	 */
	@SuppressWarnings("unused")
	private boolean verificarArticuloPerecible(HttpServletRequest request, String codigoClasificacionArticulo) throws MissingResourceException, Exception{
		//Colecci\u00F3n con los c\u00F3digos de las clasificaciones que son consideradas como perecibles
		Collection<String> clasificacionesPerecibles = (Collection<String>)request.getSession().getAttribute(CLASIFICACIONES_PERECIBLES);
		
		//verifica si se consult\u00F3 las clasificaciones, caso contrario las busca.
		if(clasificacionesPerecibles == null ){
			//obtiene el c\u00F3digo del especial para perecibles
			ParametroDTO parametro = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialPerecibles", request);
			//Plantilla de b\u00FAsqueda
			EspecialClasificacionDTO especialClasificacionDTO = new EspecialClasificacionDTO();
			especialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			especialClasificacionDTO.getId().setCodigoEspecial(parametro.getValorParametro());
			//obtiene las clasificaciones
			clasificacionesPerecibles = SISPEFactory.obtenerServicioSispe().transObtenerCodigosEspecialClasificacion(especialClasificacionDTO);
			request.getSession().setAttribute(CLASIFICACIONES_PERECIBLES, clasificacionesPerecibles);
		}
		//encontr\u00F3 clasificaciones
		if(clasificacionesPerecibles != null && !clasificacionesPerecibles.isEmpty()){
			//recorre los c\u00F3digos de las clasificaciones de perecibles
			for(String codClaPerecible : clasificacionesPerecibles){
				//si la clasificaci\u00F3n del art\u00EDculo est\u00E1 en dentro de las clasificaciones de perecibles
				if(codClaPerecible.equals(codigoClasificacionArticulo)){
					return true;
				}
			}
		}
		
		return false;
	}
	/**
	 * obtiene datos del calendario
	 * @param session
	 * @param request
	 * @param localID
	 * @param errors
	 * @param mes
	 * @throws Exception
	 */

	public static void obtenerCalendarioEntregas(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechaMinima,Date fechaMaxima,CalendarioBodegaForm formulario, Boolean verCalendario) throws Exception
	{
		try{
			LogSISPE.getLog().info("*****entra a cargar el calendario*****");
			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1);
			LogSISPE.getLog().info("calendarioConfiguracionDiaLocalDTO: {}" , session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1));
			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
			List calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
			LogSISPE.getLog().info("lista de calendario: " + calendarioConfiguracionDiaLocalDTO);
			LogSISPE.getLog().info("minima: {}" , fechaMinima);
			LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
			Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
			session.setAttribute(CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
			//subo a session el mes de busqueda
			session.setAttribute(MESSELECCIONADO,mes);
			LogSISPE.getLog().info("mes: {}",mes);
			//calculo cuantas semanas tiene el mes
			int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
			LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
			//subo a sesion el numero de semanas
			session.setAttribute(NUMEROSEMANAS,new Integer(maximoSemanas));
			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null){
				LogSISPE.getLog().info("inicial calendario local");	
			 	formulario.setCalendarioDiaLocal(((Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)));
			}
			
		}catch(SISPEException e){
			LogSISPE.getLog().info("error al cargar calendario: {}" , e.getStackTrace());  
			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
		}
	}
	/**
	 * 
	 * @param request
	 * @param diaBase
	 * @param diaSeleccionado
	 * @throws Exception 
	 * @throws SISPEException 
	 */
	private void obtenerCalendario(HttpServletRequest request, Calendar diaBase, Calendar diaSeleccionado, String nombreColeccion) throws SISPEException, Exception{
		HttpSession session = request.getSession();
		Duplex<Collection<CalendarioDiaLocalDTO>, Integer> calendarioBodega = null;
		String idUsuarioClasificacion = null;
		
		//Filtro para los usuarios que pertencen a la bodega Transito
		String codigoBodegaTransito = null;
		if(((String)session.getAttribute(BANDERA_BODEGA_TRANSITO)).equals(estadoActivo)){
			codigoBodegaTransito = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito");
		}
		//fin
		
		if(((String)session.getAttribute(BANDERA_BODEGA_POR_CLASIFICACION)).equals(estadoActivo)){
			idUsuarioClasificacion = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
		}
		
		calendarioBodega = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioBodega(diaBase, diaSeleccionado, 
				SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania(), idUsuarioClasificacion, codigoBodegaTransito
				,session.getAttribute("ec.com.smx.sic.sispe.rangoDiasProduccionPedidos")!=null?Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.rangoDiasProduccionPedidos").toString()):null
						,session.getAttribute("ec.com.smx.sic.sispe.numeroMesesProduccionPendiente")!=null?Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.numeroMesesProduccionPendiente").toString()):null
				,session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasta")!=null?session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasta").toString():null,session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa")!=null?session.getAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa").toString():null);
		
		if(calendarioBodega.getSecondObject() != null){
			session.setAttribute(INDICE_DIA_SELECCIONADO, nombreColeccion.substring(1).concat(",").concat(Integer.toString(calendarioBodega.getSecondObject())));
		}
		session.setAttribute(CALENDARIO_BODEGA.concat(nombreColeccion), calendarioBodega.getFirstObject());
	}
	/**
	 * @param request
	 * @throws Exception
	 */
	private boolean verificarUsuarioLogeadoBodega(HttpServletRequest request) throws SISPEException,Exception{
		String activarFiltroBodegaTransito = estadoInactivo;
		String activarFiltroBodegaClasificacion = estadoInactivo;
		HttpSession sesion = request.getSession();
		ParametroDTO parametroDTO = null;
		String rolBodegaCanastos = null;
		
		//obtien el rol del administrados de bodega
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoRolAdministradorBodega", request);
		rolBodegaCanastos = parametroDTO.getValorParametro();
		
		//valida si es bodega de canastos
		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol().equals(rolBodegaCanastos)){//VistaEntidadResponsableDTO
			LogSISPE.getLog().info("El usuario Logeado pertenece a la bodega de canastos");
		}else{
			//obtiene par\u00E1metro de la bodega de Transito y su codigo(97,Guayaquil)
			parametroDTO = null;
			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD", request);
			String [] cadenaValor = parametroDTO.getValorParametro().split("-");
			
			//obtengo el usuario logeado y verifico si pertenece a la bodega de Transito
			if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal() == Integer.parseInt(cadenaValor[1]) 
					&& SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoDivGeoPol().equals(cadenaValor[0])){
				LogSISPE.getLog().info("El usuario Logeado pertenece a la bodega de transito");
				activarFiltroBodegaTransito = estadoActivo;
			} else{
				LogSISPE.getLog().info("El usuario Logeado pertenece a un bodega por clasificacion");
				activarFiltroBodegaClasificacion = estadoActivo;
			}
		}
		
		sesion.setAttribute(BANDERA_BODEGA_TRANSITO , activarFiltroBodegaTransito);
		sesion.setAttribute(BANDERA_BODEGA_POR_CLASIFICACION , activarFiltroBodegaClasificacion);
		
		if(activarFiltroBodegaTransito.equals(estadoActivo)){
			return true;
		}else if(activarFiltroBodegaClasificacion.equals(estadoActivo)){
			if(WebSISPEUtil.verificarUsuarioClasificacion(request, false)){
				return true;
			}
		}else if(activarFiltroBodegaTransito.equals(estadoInactivo) && activarFiltroBodegaClasificacion.equals(estadoInactivo)){
			if(WebSISPEUtil.verificarUsuarioClasificacion(request, true)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * obtiene los parametros que se van a usar para la configuracion de la entrega
	 * @param request
	 * @param errors
	 */
	private static void obtenerConfiguracionEntrega(HttpServletRequest request,ActionMessages errors){
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
	 * Cargar el calendario para configurar las entregas desde la bodega de transito de guayaquil
	 * @param session
	 * @param request
	 * @param errors
	 * @param formulario
	 */
	private void cargarCalendarioEntregas(HttpSession session,HttpServletRequest request,ActionMessages errors,CalendarioBodegaForm formulario){
		try {
			LogSISPE.getLog().info("cargarCalendarioEntregas");
			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
			
			//OANDINO: Se carga en session el contenido del campo CODIGOPARAMETRO de SCSPETPARAMETRO ------------------------------------			
			//String codigoCiudadCheck = "";
			
			// Plantilla de b\u00FAsqueda
			ParametroID parametroID = new ParametroID();
			parametroID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());			
			parametroID.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD"));
			
			// Se obtiene el valor de VALORPARAMETRO de SCSPETPARAMETRO
			ParametroDTO parametroValorDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametroID);
			String valorParametro = parametroValorDTO.getValorParametro();
			LogSISPE.getLog().info("Valor par\u00E1metro completo: {}",valorParametro);
			
			// Se sube a session el valor anterior
			session.setAttribute(VAR_VALOR_PARAMETRO_TOTAL, valorParametro);
			
			//se obtiene el valor del par\u00E1metro que contiene el limite inferior para que una entrega se obligatoriamente  con transito
			session.setAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO, WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.totalReservaCDObligatoriedadBodegaTransito", request).getValorParametro());
			//---------------------------------------------------------------------------------------------------------------------------
			
			//variable de sesion para cambiar el metas
			session.setAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"),"ok");			
			session.removeAttribute(EXISTENCAMBIOS);//variable de sesion para saber si existieron cambios antes de salir
			LogSISPE.getLog().info("cargando dias de la semana");
			//cargo los dias de la semana
			String[] orden1 = {"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};
			String[] orden2 = {"Dom","Lun","Mar","Mie","Jue","Vie","Sab"};
			String[] dias=null;
			//si el primer dias es el 1 la semana empieza en domingo caso contrario en lunes
			if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.primerDiaSemana").equals("1"))
				dias=orden2;
			else
				dias=orden1;
			//guardo en sesion los dias de la semana
			session.setAttribute(ORDENDIAS,dias);
			if(session.getAttribute(CONFIGURACION)== null){
				//Obtiene la configuracion para las entregas
				obtenerConfiguracionEntrega(request, errors);
			}
			if(session.getAttribute(OPCIONLUGARENTREGA)==null){
				formulario.setOpLugarEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"));
				formulario.setOpTipoEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"));
			}
			else{
				formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
				formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
				formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
			}
			
			//Saco dos copia de la configuracion de los calendarios
			if(session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL)!=null){
				LogSISPE.getLog().info("si tiene configuracion del calendario");
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL);
				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalAux=calendarioConfiguracionDiaLocal.copiar();
				session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalAux);
			}
			
			/*************** Uso una sesion auxiliar para el detalle pedido, calendarioConfiguracionDialLocal, costos y direcciones *******************/						
			LogSISPE.getLog().info("Clono el detalle del pedido");
			//Clono el detalle del pedido
			VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO)session.getAttribute(VISTA_PEDIDO);
			LogSISPE.getLog().info("listaDetallesArticulos {}",vistaPedidoDTO);
			if(vistaPedidoDTO!=null){
				Collection<DetallePedidoDTO> detallePedidoDTOColAux=new ArrayList<DetallePedidoDTO>();
				//se verifica si el detalle est\u00E1 nulo
				Collection detallePedidoDTOCol = vistaPedidoDTO.getVistaDetallesPedidosReporte();
				/**********************************************************************************/
				LogSISPE.getLog().info("direcciones: {}" , session.getAttribute(DIRECCIONESAUX));
				//Clono las direcciones
				LogSISPE.getLog().info("Clono las direcciones");
				if(session.getAttribute(DIRECCIONES)!=null){
					Collection direccionesDTOColAux=new ArrayList<DireccionesDTO>();
					Collection direccionesDTOCol=(Collection)session.getAttribute(DIRECCIONES);
					for (Iterator iter = direccionesDTOCol.iterator(); iter.hasNext();) {
						DireccionesDTO direccionesDTO = (DireccionesDTO) iter.next();
						DireccionesDTO direccionesDTOAux = direccionesDTO.clone();
						direccionesDTOColAux.add(direccionesDTOAux);
					}
					session.setAttribute(DIRECCIONESAUX, direccionesDTOColAux);
				}
				//se realiza la consulta de los locales
				if(session.getAttribute(SessionManagerSISPE.COLECCION_LOCALES)==null){
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
				}
				session.setAttribute(SELECCIONARLOCAL, "ok");
				/*-------------------------------------------------------------------------------*/
				/**********************Por defecto la fecha minima de entrega es la fecha de entrega************/
				LogSISPE.getLog().info("fecha inicial entrega minima: {}" , session.getAttribute(CotizarReservarAction.FECHA_ENTREGA));
				//Guardo la fecha minima de entrega
				session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA))));
				//por defecto la fecha de entrega es igual a la fecha minima de entrega es la actual
				formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
				/**********************Elimina variables de session***************************/
				session.removeAttribute(LOCALID);//id del local seleccionado
				session.removeAttribute(DIASELECCIONADO);//dia de despacho seleccionado en el calendario
				session.removeAttribute(CALENDARIODIALOCALCOL);//calendario
				session.removeAttribute(VISTALOCALCOL);//locales
				session.removeAttribute(CALENDARIODIALOCAL);//CD del dia seleccionado en el calendario
				session.removeAttribute(FECHAMINIMA);//fecha minima de despacho a locales
				session.removeAttribute(FECHAMAXIMA);//fecha maxima de despacho a locales
				session.removeAttribute(SECTORSELECCIONADO);//indice local seleccionado
				session.removeAttribute(DIRECCION);
				session.removeAttribute(EXISTELUGARENTREGA);
				session.removeAttribute(SELECCIONARCALENDARIO);
				session.removeAttribute("ventanaLocalEntrega");//elimina la variable que abre la ventana de seleccion de local de entrega
				
			}
			else{
				LogSISPE.getLog().error("vista de articulos vacia");
				errors.add("Error",new ActionMessage("Vista de articulos vacia"));
			}
		}catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			errors.add("Error",new ActionMessage("Error "+e));
		}
	}/**
	 * Seleccion de dias del mes
	 * @param formulario
	 * @param dia               dia individual, semana, o dia de la semana seleccionado en el calendario
	 * @param session
	 * @param seleccionado		dice si los dias van a ser seleccionados o no		
	 * @param indice            dice cual va a ser el incremento entre dias ej. si se seleccion una semana deben seleccionarse los dias sumandole 7
	 * @param limite         	dice cuantos dias van a ser seleccionados   
	 */
	private void seleccionDia(CalendarioBodegaForm formulario, int dia,HttpSession session,boolean seleccionado,ActionMessages errors, HttpServletRequest request) throws Exception{
		LogSISPE.getLog().info("entra a seleccionar los dias: " + dia);
		Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
		//dia seleccionado
		int diaSeleccionado=0;
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia];
		LogSISPE.getLog().info("seleccionado: {}" , calendarioDiaLocalDTO.getNpEsSeleccionado());
		LogSISPE.getLog().info("fechaCalendario: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
		//fecha minima
		Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
		/*****************************************************************************************/
		//fecha maxima
		Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
		LogSISPE.getLog().info("fecha maxima: {}",fechaMaxima);
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaMaxima);
		fechaCalendario.add(Calendar.DAY_OF_MONTH, -1);
		fechaMaxima = fechaCalendario.getTime();
		/*****************************************************************************************/
		//Diferencia entre fecha minima y fecha seleccionada
		long diferenciaFechasMinima= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
		//Diferencia entre fecha de entrega y fecha seleccionada
		long diferenciaFechasEntrega= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()),ConverterUtil.parseDateToString(fechaMaxima));
		LogSISPE.getLog().info("diferenciaFechasMinima{}" , diferenciaFechasMinima);
		LogSISPE.getLog().info("diferenciaFechasEntrega{}" , diferenciaFechasEntrega);
		//Compara si la fecha elegida es mayor o igual a la fecha de busqueda
		if(diferenciaFechasMinima<0.0 || diferenciaFechasEntrega<0.0){
			LogSISPE.getLog().info("error de fechas");
			if(diferenciaFechasMinima<0.0)
				errors.add("fechaMinima", new ActionMessage("errors.fechaSeleccionadaMinima"));
			if(diferenciaFechasEntrega<0.0)
				errors.add("fechaEntrega", new ActionMessage("errors.fechaSeleccionadaEntrega",ConverterUtil.parseDateToString(fechaMaxima)));
		}
		else{
			LogSISPE.getLog().info("va a seleccionar la fecha");
			if(session.getAttribute(DIASELECCIONADO)!=null){
				diaSeleccionado=((Integer)session.getAttribute(DIASELECCIONADO)).intValue();
				//Recupero el dia seleccionado anteriormente
				CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
				calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
			}
			//Cargo los dias a los que les debo modifcar la cantidad acumulada
			cargaDiasModificarCA(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), (Date)session.getAttribute(FECHAMAXIMA), formulario, request, dia);
			session.setAttribute(DIASELECCIONADO, dia);
			session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
			//LogSISPE.getLog().info("entro al no de dia seleccionado");
			calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.TRUE);
			formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
			session.setAttribute(FECHADESPACHO,DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
			LogSISPE.getLog().info("dia seleccionado: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
			//session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
			formulario.setCalendarioDiaLocal(calendarioDiaLocalDTOObj);
			LogSISPE.getLog().info("habilitarDireccion: {}" , session.getAttribute(HABILITARDIRECCION));
		}
	}
	/**
	 * 
	 * @param fechaCalendarioDia
	 * @param fechaEntregaCliente
	 * @param formulario
	 * @param request
	 * @param dia
	 */	
	public static void cargaDiasModificarCA(Date fechaCalendarioDia, Date fechaEntregaCliente,CalendarioBodegaForm formulario, HttpServletRequest request,int dia){
		try{
			HttpSession session=request.getSession();
			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
			//Armo una coleccion con los dias que voy a sumar capacidad acumulada
			Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=new ArrayList<CalendarioDiaLocalDTO>();
			CalendarioDiaLocalDTO calendarioDiaLocalDTOAux=new CalendarioDiaLocalDTO();
			//Obtengo cuantos dias hay desde el dia seleccionado hasta el dia de entrega
			GregorianCalendar fechaDespacho=new GregorianCalendar();
			fechaDespacho.setTime(fechaCalendarioDia);
			GregorianCalendar fechaEntrega=new GregorianCalendar();
			fechaEntrega.setTime(fechaEntregaCliente);
			LogSISPE.getLog().info("FECHA DESPACHO: {}",fechaDespacho.getTime());
			LogSISPE.getLog().info("FECHA ENTREGA: {}",fechaEntrega.getTime());
			int numdias=WebSISPEUtil.calcularDiasEntreFechas(fechaDespacho,fechaEntrega);
			int diasCalendario=calendarioDiaLocalDTOObj.length;
			LogSISPE.getLog().info("numero de dias entre la fecha de despacho y la fecha de entrega: {}" , numdias);
			LogSISPE.getLog().info("calendarioDiaLocalDTOObj: {}, dia: {}",diasCalendario,dia);
			for(int i=0;i<=numdias;i++){
				if((dia+i+1)<diasCalendario){
					calendarioDiaLocalDTOAux=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia + i +1];
					calendarioDiaLocalDTOCol.add(calendarioDiaLocalDTOAux);
				}
			}
			LogSISPE.getLog().info("dias a modificar: {}" , calendarioDiaLocalDTOCol.size());
			session.setAttribute(CALENDARIODIALOCALCOLAUX, calendarioDiaLocalDTOCol);
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}
	/**
	 * Determina el precio que se debe tomar en cuenta para realizar el c\u00E1lculo del total del detalle para el 
	 * art\u00EDculo que se va a agregar al detalle.
	 * 
	 * @param  formulario				El formulario donde se mostrar\u00E1n los datos
	 * @param  request				La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return detallePedidoDTO		El detalle del pedido que fu\u00E9 creado
	 */
	private static boolean construirDetallesPedidoDesdeVista(CalendarioBodegaForm formulario,HttpServletRequest request, VistaDetallePedidoDTO vistaDetallePedidoDTO,VistaPedidoDTO vistaPedidoDTO, int i)throws Exception
	{
		HttpSession session = request.getSession();
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		String estadoInactivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_INACTIVO);
		String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
		//colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
		List<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
		List<DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
		List<DetallePedidoDTO> detallePedido1 = new ArrayList <DetallePedidoDTO>();
		Collection<String> codigosArticulos = new ArrayList <String>();
		try{
//		//se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
//		VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
//		consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
//		consultaVistaDetallePedidoDTO.getId().setCodigoLocal(vistaPedidoDTO.getId().getCodigoLocal());
//		consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
//		consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
//		consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
//		consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
//		consultaVistaDetallePedidoDTO.setEntregas(new ArrayList<EntregaDTO>());
//		detalleVistaPedido = (List<VistaDetallePedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);

		//se crea la colecci\u00F3n de art\u00EDculos para realizar la consulta de los stocks y alcances
		ArrayList <ArticuloDTO> articulos = new ArrayList <ArticuloDTO>();
		//contador para los precios alterados
		int contadorPreciosAlterados = 0;
		
		//variable para sumar cuantos articulos han sido entregados
		long contadorEntrega=0;
		//variable para sumar cuantos articulos han sido despachados
		long contadorDespacho=0; 
		
//		//se itera la vistaDetallePedido para crear un DetallePedidoDTO
//		for (int i=0;i<detalleVistaPedido.size();i++){
			
			contadorEntrega=0;
			contadorDespacho=0;
			boolean precioAlterado = false; //variable para el control de cambios en los precios
//			VistaDetallePedidoDTO vistaDetallePedidoDTO = detalleVistaPedido.get(i);
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
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
			//este campo se asigna para las entregas
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadParcialEstado(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setReservarBodegaSIC(estadoInactivo);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstadoReservado(0D);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEspecialReservado(estadoInactivo);

			//se inicializa el peso aproximado
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
			//estos campos pueden variar al final de este m\u00E9todo, en el caso que se deban aplicar descuentos 
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoDescuento(vistaDetallePedidoDTO.getValorTotalEstadoDescuento());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(vistaDetallePedidoDTO.getValorTotalEstadoNeto());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(vistaDetallePedidoDTO.getValorTotalEstadoNetoIVA());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(vistaDetallePedidoDTO.getValorPrevioVenta());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(vistaDetallePedidoDTO.getValorFinalEstadoDescuento());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(vistaDetallePedidoDTO.getValorUnitarioPOS());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(vistaDetallePedidoDTO.getEstadoCanCotVacio());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getArticuloDTO().getCantidadMayoreo());
			detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(vistaDetallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()? estadoActivo:estadoInactivo);
			//Obtener entrega del articulo seleccionado
			Collection<EntregaDetallePedidoDTO> entregasCol=new ArrayList<EntregaDetallePedidoDTO>(vistaPedidoDTO.getVistaDetallesPedidosReporte().size());
			//itero las entregas para cargar los valores no persistentes
			int numeroBultos=0;
			for (EntregaDetallePedidoDTO entregaDetallePedidoDTO: vistaDetallePedidoDTO.getEntregaDetallePedidoCol()) {
					LogSISPE.getLog().info("cantidad entrega: {}", entregaDetallePedidoDTO.getCantidadEntrega());
					LogSISPE.getLog().info("cantidad despacho: {}", entregaDetallePedidoDTO.getCantidadDespacho());
					contadorEntrega=contadorEntrega+entregaDetallePedidoDTO.getCantidadEntrega().longValue();
					LogSISPE.getLog().info("contador entrega: {}", contadorEntrega);
					entregaDetallePedidoDTO.setNpCantidadEntrega(entregaDetallePedidoDTO.getCantidadEntrega());
					contadorDespacho=contadorDespacho+entregaDetallePedidoDTO.getCantidadDespacho().longValue();
					entregaDetallePedidoDTO.setNpCantidadDespacho(entregaDetallePedidoDTO.getCantidadDespacho());
					LogSISPE.getLog().info("contador despacho: {}", contadorDespacho);
					entregaDetallePedidoDTO.setNpCantidadEntregaFueModificada(null);//verificar esta variable cuando se haga la parte de modificar reservaciones
					entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpContadorBeneficiario(0L);
					entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodigoAutorizacion(null);
					numeroBultos=UtilesSISPE.calcularCantidadBultos(entregaDetallePedidoDTO.getNpCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
					LogSISPE.getLog().info("num bultos: {}" , numeroBultos);
					entregaDetallePedidoDTO.setNpCantidadBultos(new Integer(numeroBultos));
					entregasCol.add(entregaDetallePedidoDTO);
			}
			LogSISPE.getLog().info("Agregando a sesion las entregas");
			session.setAttribute("ec.com.smx.sic.sispe.entregasPerecibles", entregasCol);
			
			LogSISPE.getLog().info("----contador entrega---- {}", contadorEntrega);
			LogSISPE.getLog().info("----contador despacho---- {}", contadorDespacho);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-contadorEntrega));
			detallePedidoDTO.setNpContadorDespacho(Long.valueOf(contadorDespacho));
			detallePedidoDTO.setNpContadorEntrega(Long.valueOf(contadorEntrega));
			//se asignan las entregas por cada detalle
			detallePedidoDTO.setEntregaDetallePedidoCol(vistaDetallePedidoDTO.getEntregaDetallePedidoCol());

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

//			//se verifica si el art\u00EDculo es un canasto
//			if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) || 
//					vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
//				//se llama al m\u00E9todo que carga la receta de cada art\u00EDculo que es un canasto
//				cargarRecetaArticulo(request, detallePedidoDTO, vistaPedidoDTO.getId().getSecuencialEstadoPedido());
//				if(detallePedidoDTO.getNpRecetaActualEsDiferenteAHistorial()){
//					precioAlterado = true;
//				}
//			}
			
			//llamada al m\u00E9todo que construye la consulta de los art\u00EDculos
			WebSISPEUtil.construirConsultaArticulos(request,detallePedidoDTO.getArticuloDTO(), estadoInactivo, estadoInactivo, accionActual);
			//se almacenan los detalles y los art\u00EDculos
			detallePedido.add(detallePedidoDTO);
			detallePedido1.add((DetallePedidoDTO)SerializationUtils.clone(detallePedidoDTO));
			articulos.add(detallePedidoDTO.getArticuloDTO());
			//se almacenan los codigos de art\u00EDculos del detalle
			codigosArticulos.add(vistaDetallePedidoDTO.getId().getCodigoArticulo());

			//si se altero el precio de un art\u00EDculo
			if(precioAlterado)
				contadorPreciosAlterados++;
			
			LogSISPE.getLog().info("estadoArticuloSIC *: {}, estadoArticuloSICReceta *: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());

		//llamada al m\u00E9todo que obtiene el stock y alcance de los art\u00EDculos
		SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);
		//se recorre nuevamente el detalle del pedido, ahora para verificar su stock
//		for(int j=0;j<detallePedido.size();j++){
//			DetallePedidoDTO detPedDTO = detallePedido.get(j);
			if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null 
					&& detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
				//se asigna el estado correspondiente al estado del stock
				detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);
			}
			LogSISPE.getLog().info("estadoArticuloSIC **: {}, estadoArticuloSICReceta **: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());
//		}
		session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
		
		session.setAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL,detallePedido1);
		
		session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS,codigosArticulos);
		}
		catch (Exception e){
			return false;
		}
		return true;
	}
}
