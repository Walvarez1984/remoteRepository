/* Creado el 08/06/2007
 * EntregaLocalCalendarioAction.java
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import ec.com.smx.framework.web.action.BaseAction;

/**
 * <p>
 * Esta clase permite realizar las entregas a domicilio y a locales, comentada se cambio a otro action (se conserva el anterior para verificar negocio)
 * </p>
 * 
 * <b>Utliza los m\u00E9todos</b>
 * <ul>
 * 	<li><b>transObtenerVistaLocal(vistaLocalDTO)</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transObtenerVistaLocalSector((vistaLocalSectorDTO))</b> del servicio ServicioClienteServicio</li>
 * 	<li><b>transObtenerParametro(consultaParametroDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerVistaEstablecimientoCiudadLocalSector(vistaEstablecimientoCiudadLocalDTO)</b> del servicio ServicioClienteServicio</li>
 *  <li><b>transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima)</b> del servicio ServicioClienteServicio</li>
 * </ul>
 * 
 * Utliza forward para determinar la siguiente acci\u00F3n o pantalla.
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class EntregaLocalCalendarioActionAnt extends BaseAction {

//	//Clase utilizada para convertir una fecha de formato String a Timestamp.
//	private static SqlTimestampConverter convertidor = null;
//	/************************Variables desesion***************************************/
//	public static String CALENDARIODIALOCALCOL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol"; //calendario para un mes especifico
//	public static String CALENDARIODIALOCALCOLBODEGA= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOBodegaCol"; //calendario para un mes especifico
//	public static String MESSELECCIONADOBODEGA= "ec.com.smx.calendarizacion.mes.seleccionado.bodega"; //calendario para un mes especifico
//	public static String NUMEROSEMANASBODEGA="ec.com.smx.calendarizacion.semana.seleccionado.bodega";
//	public static String CALENDARIODIALOCAL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTO"; //CD del dia seleccionado en el calendario
//	public static String CALENDARIODIALOCALCOLAUX= "ec.com.smx.calendarizacion.calendarioDiaLocalDTOColAux"; //dias que deben ser modificados su cantidad acumulada
//	public static String SECDIRECCIONES= "ec.com.smx.sic.sispe.pedido.secDirecciones"; //numero de direcciones
//	public static String DIRECCION= "ec.com.smx.sic.sispe.direccionDTO"; //direccion
////	public static String VALORTOTALENTREGA= "ec.com.smx.sispe.pedido.valorTotalEntrega"; //costo entrega total
//	private static String VISTALOCALCOL= "ec.com.smx.sic.sispe.vistaLocalDTOCol"; //locales
//	public static String VISTALOCALORIGEN= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.origen"; //local origen
//	public static String VISTALOCALDESTINO= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.destino"; //local destino
//	public static String VISTALOCALACTUAL= "ec.com.smx.sic.sispe.entregas.vistaLocalDTO.destino"; //local que esta logueado
//	private static String LOCALID= "ec.com.smx.sic.sispe.localID"; //id del local seleccionado
//	public static String DIASELECCIONADO= "ec.com.smx.calendarizacion.diaSeleccionado"; //dia de despacho seleccionado en el calendario
//	public static String DIASELECCIONADOAUX= "ec.com.smx.calendarizacion.diaSeleccionadoAux"; //dia de despacho seleccionado en el calendario
//	private static String MESSELECCIONADO= "ec.com.smx.calendarizacion.mesBusqueda"; //mes seleccionado en el calendario
//	private static String FECHAMINIMA= "ec.com.smx.sic.sispe.fechaMinima"; //fecha minima de despacho a locales
//	private static String FECHAMAXIMA= "ec.com.smx.sic.sispe.fechaMaxima"; //fecha maxima de despacho a locales
//	private static String SECTORSELECCIONADO= "ec.com.smx.sic.sispe.sectorSeleccionado"; //indice local seleccionado
//	private static String FECHABUSQUEDA= "ec.com.smx.calendarizacion.fechaBuscada"; //fecha minima de entrega
//	private static String NUMEROSEMANAS= "ec.com.smx.calendarizacion.numeroSemanas"; //numero de semanas que tiene un mes
//	private static String ORDENDIAS= "ec.com.smx.sic.sispe.calendarizacion.ordenDias"; //Orden de dias de la semana
//	private static String CONFIGURACION= "ec.com.smx.sic.sispe.entregas.configuracionDTOCol"; //Coleccion de configuraciones para las entregas
//
//	public static String AUTORIZACION= "ec.com.smx.sic.sispe.pedidos.autorizacionDTO"; //autorizacion viene del formulario
//	private static String AUTORIZACIONENTREGASCOL= "ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol"; //coleccion de autorizaciones
//
//	public static String DETALLELPEDIDOAUX= "ec.com.smx.sic.sispe.detallePedidoAux"; //contiene el detalle del pedido se incia con el detalle incial y se trabaja con el si se elige guardar la entrega se sobreescribe la sesion del detalle del pedido
//	public static String ESTRUCTURA_RESPONSABLE= "ec.com.smx.sic.sispe.estructuraResponsable"; //contiene por entregas del pedido los responsables
//	public static String DIRECCIONES= "ec.com.smx.sic.sispe.pedido.direcciones"; //direcciones donde se han relizado entregas
//	public static String DIRECCIONESAUX= "ec.com.smx.sic.sispe.pedido.direccionesAux"; //sesion auxiliar para almacenar las direcciones antes de guardar
//	public static String COSTOENTREGA= "ec.com.smx.sispe.pedido.costoEntregasDTOCol"; //coleccion de los costos de las entregas a domicilio
//	public static String COSTOENTREGAAUX= "ec.com.smx.sispe.pedido.costoEntregasDTOColAux"; //coleccion de los costos de las entregas auxiliar antes de grabar los cambios
//	public static String CALENDARIOCONFIGURACIONDIALOCAL= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTO"; //configuracion real de las modificaciones en el calendario
//	public static String CALENDARIOCONFIGURACIONDIALOCALAUX= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTOAux"; //configuracion auxiliar de las modificaciones en el calendario
//	public static String CALENDARIOCONFIGURACIONDIALOCALAUX1= "ec.com.smx.sic.sispe.pedido.calendarioConfiguracionDiaLocalDTOAux1"; //configuracion auxiliar de las modificaciones en el calendario
////	public static String VALORTOTALENTREGAAUX= "ec.com.smx.sispe.pedido.valorTotalEntregaAux"; //costo entrega total
//	private static String SECDIRECCIONESAUX= "ec.com.smx.sic.sispe.pedido.secDireccionesAux"; //numero de direcciones
//	private static String VISTAESTABLECIMIENTOCIUDADLOCAL="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol"; //Carga las ciudades para la entrega a domicilio desde el CD
//	private static String LUGARENTREGADOMICILIO="ec.com.smx.sic.sispe.lugarEntregaDomicilio"; //etiqueta que indica desde donde es la entrega a domicilio
//	
//	private static String EXISTENCAMBIOS= "ec.com.smx.sic.sispe.existenCambios"; //Variable para saber si existieron cambios en las entregas
//	public static String ENTIDADRESPONSABLELOCAL= "ec.com.smx.sic.sispe.entidadResponsable"; //Variable para saber si la entidad responsable es el local
//	
//	
//	public static String NOMBREENTIDADRESPONSABLE= "ec.com.smx.sic.sispe.nombreEntidadResponsable"; //Variable que indica cual es la entidad responsable
//	private static String EXISTELUGARENTREGA="ec.com.smx.sic.sispe.existeLugarEntrega";//Variable de sesion que indica si ya fue seleccionado un lugar de entrega
//	private static String EDITAFECHAMINIMA="ec.com.smx.sic.sispe.editaFechaMinima";//Variable de sesion que indica si la fecha minima va a poder ser editada o no
//	
//	private static String EXISTENENTREGAS="ec.com.smx.sic.sispe.existenEntregas";//Variable de sesion que indica si existe al menos una entrega para que salga visible el check de eliminar
//	
//	public static String COMBOSELECCIONCIUDAD="ec.com.smx.sic.sispe.comboSeleccionCiudad";//Variable de sesion que indica que debe ser visible el combo de ciudades
//	
//	//Variables para configurar los diferentes tipos de entregas
//	public static String SELECCIONARLOCAL="ec.com.smx.sic.sispe.seleccionarLocal";//Seleccionar el local 
//	public static String SELECCIONARCALENDARIO="ec.com.smx.sic.sispe.seleccionarCalendario";//Seleccionar fecha de recepcion
//	public static String HABILITARCANTIDADENTREGA="ec.com.smx.sic.sispe.habilitarCantidadEntrega";//Cantidad de entrega
//	public static String HABILITARCANTIDADRESERVA="ec.com.smx.sic.sispe.habilitarCantidadReserva";//Cantidad de recepcion
//	public static String HABILITARDIRECCION="ec.com.smx.sic.sispe.habilitarDireccion";//Direccion y distancia
//	public static String HABILITARBOTONACEPTAR="ec.com.smx.sic.sispe.habilitarBotonAceptar";//Boton aceptar
//	private static String CONFIGURACIONCARGADA="ec.com.smx.sic.sispe.configuracionCargada";//Variable de sesion que indica si configuracion de la entrega ya fue cargada se usa para indicar el paso uno de la ayuda
//	
//	//Variable para la navegaci\u00F3n del popUpEntregas
//	public static String TABSELECCIONADONAVEGACION="ec.com.smx.sic.sispe.tabseleccionadonavegacion";//tab seleccionado
//	
//	public static String MOSTRAROPCIONCANASTOSESPECIALES = "ec.com.smx.sic.sispe.mostraropcioncanastosespeciales";//para mostrar opcion de canastos especiales
//	public static String OPCIONCANASTOSESPECIALES="ec.com.smx.sic.opcioncanastosespeciales"; //Sesion que indica la ultima opcion de canastos especiales
//	public static String PASOSPOPUP = "ec.com.smx.sic.pasospopup";//para el control de los mensajes de los pasos
//	public static String CONFIRMACIONANTERIORCONFENTREGAS="ec.com.smx.sic.confirmacionanteriorentregas";//para confirmar cuando quiere regresar en la configuracio de las entregas
//	public static String POSICIONDIVCONFENTREGAS = "ec.com.smx.sic.sispe.posiciondivconfentregas" ;
//	
//	//Almacena la configuracion de las entregas
//	public static String LUGARENTREGA="ec.com.smx.sic.sispe.lugarEntrega";//Registra el lugar de entrega
//	public static String TIPOENTREGA="ec.com.smx.sic.sispe.tipoEntrega";//Registra el tipo de entrega
//	public static String STOCKENTREGA="ec.com.smx.sic.sispe.stockEntrega";//Registra de donde va ser tomado el stock
//	public static String STOCKENTREGAAUX="ec.com.smx.sic.sispe.stockEntregaAux";//Registra de donde va ser tomado el stock para manejo de cantidades en entrega parcial
//	
//	public static String FECHAENTREGACLIENTE="ec.com.smx.sic.sispe.fechaEntregaCliente";//Sirve para guardar la fecha de entrega del cliente
//	
//	public static String MENSAJEPASOS="ec.com.smx.sic.mensajes"; //Sesion que indica los mensajes que saldran en cada paso
//	public static String PASO="ec.com.smx.sic.paso"; //Indica el numero de paso que debe salir en el mensaje
//	
//	public static String OPCIONLUGARENTREGA="ec.com.smx.sic.opcionLugarEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
//	public static String OPCIONTIPOENTREGA="ec.com.smx.sic.opcionTipoEntrega"; //Sesion que indica la ultima opcion de lugar de entrega seleccionada
//	public static String OPCIONSTOCK="ec.com.smx.sic.opcionStock"; //Sesion que indica la ultima opcion del stock seleccionado
//	public static String BANDERA_CONFIGURA_CAL_BOD = "ec.com.smx.sic.flag.calendario.bodega";
//	
//	private static String DESACTIVAR_ENT_RES_LOC = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("entregas.desactivarEntResLoc");
//	
//	//OANDINO: Variables de session utilizadas en la validaci\u00F3n de c\u00F3digo de local de tr\u00E1nsito ---------------------
//	//Variable que permite mantener el valor obtenido de VALORPARAMETRO desde SCSPETPARAMETRO
//	public static String VAR_VALOR_PARAMETRO_TOTAL = "ec.com.smx.sic.sispe.valorParametro";
//	
//	//Variable que almacena el valor seleccionado del combo de cuidades "seleccionCiudad" en "localesCalendario.jsp"
//	private static String VAR_VALOR_CODIGO_CIUDAD_COMBO = "ec.com.smx.sic.sispe.codigoCiudad";
//	public static String CIUDAD_SECTOR_ENTREGA = "ec.com.smx.sic.sispe.ciudad.sector.entrega";
//	public static final String CLASIFICACIONES_PERECIBLES = "ec.com.smx.sic.sispe.clasificacionesPerecibles";
//	public static final String TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE = "ec.com.smx.sic.sispe.totalSolicitadoCDaDomicilioNoPerecible";
//	public static final String PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO = "ec.com.smx.sic.sispe.parametroLimiteInferiorObligatorioTransito";
//	public static final String MOSTRAR_POPUPTAB = "ec.com.smx.sic.sispe.mostrarPopUpTab";
//	
//	public static String ENTREGAS_RESPONSABLES= "ec.com.smx.sic.sispe.entregasResp"; //contiene el detalle del pedido con sus responsables
//	public static String HORA_SELECCIONADA = "ec.com.smx.sic.sispe.hora.seleccionada"; //vector horas[0], minutos[1], segundos[2]
//	//public static String PEDIDO_DISTRIBUIDO_CAMIONES = "ec.com.smx.sic.sispe.pedido.distribuido.camiones"; //bandera indica que el pedido ya fue asignado a camiones 
//	public static String CALENDARIO_HORA_LOCAL_SELECCIONADO = "ec.com.smx.sic.sispe.calendario.hora.local.seleccionado"; //guarda el obj tipo CalendarioHoraCamionLocalDTO
//	public static String MES_ACTUAL_CALENDARIO = "ec.com.smx.sic.sispe.mes.actual.calendario";
//	
//	//Variable para mostrar o no el calendario de bodega por horas
//	public static String MOSTRAR_CALENDARIO_BODEGA_POR_HORAS = "ec.com.smx.sic.sispe.mostrar.calendario.bodega.por.horas";	
//	public static String CALENDARIODIALOCAL_PARCIAL_POR_HORAS = "ec.com.smx.sic.sispe.calendario.parcial.por.horas"; //se mantienen los datos de la calendarizacion por horas cuando es entrega parcial
//	
//	//variable para los checks de transito de las entregas
//	public static String CHECKTRANSITO = "ec.com.smx.sispe.checktransito";
//	
////	public static String ELIMINO_TODAS_ENTREGAS = "ec.com.smx.sispe.eliminarTodasEntregas";
//	
////	variable para saber si se elimino alguna direccion de las entrega
//	public static String ENTREGAS_ELIMINO_DIRECCION = "ec.com.smx.sic.sispe.entregas.elimino.direccion";
//	
//	//--------------------------------------------------------------------------------------------------------------
//		
//	
//		
//	/**
//	 * <p>
//	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
//	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
//	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
//	 * </p>
//	 * 
//	 * @param mapping 		El mapeo utilizado para seleccionar esta instancia
//	 * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
//	 *          				campos
//	 * @param request 		La petici&oacue; que estamos procesando
//	 * @param response 		La respuesta HTTP que se genera
//	 * @return ActionForward	Los seguimiento de salida de las acciones
//	 * @throws Exception
//	 */
//	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception 
//	{
//		/************************Declaracion de variables**********************************/
//		HttpSession session = request.getSession();
//		ActionMessages messages=new ActionMessages();
//		ActionMessages errors = new ActionMessages();
//		ActionMessages info = new ActionMessages();
//		ActionMessages warnings=new ActionMessages();
//		ActionErrors error = new ActionErrors();
//		CotizarReservarForm formulario=(CotizarReservarForm)form;
//		//se obtiene el bean que contiene los campos genericos
//		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);	
//		String forward="desplegar";
//		
//		//ya esta realizada la configuacion de entregas de todos los detalles
//		if(request.getParameter("sinCambiosEnEntregas")!=null){
//			LogSISPE.getLog().info("--- todos los detalles estan configurados ---");
//			warnings.add("sinCambios",new ActionMessage("warnings.sinCambiosReserva"));
//		}
//		//Configuracion de la entrega
//		else if(request.getParameter("abrirConfiguracion")!=null){ 
//			LogSISPE.getLog().info("--- abrir Popup configuracion ---");
//			// Objetos para construir los tabs, si fue modificacion de reserva
//			//asignacionValoresFormulario(session, formulario);			
//			request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
//			request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
//			beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
//			instanciarVentanaOpcionesConfiguracion(request);
//			session.setAttribute(MOSTRAR_POPUPTAB, "ok");
//			session.removeAttribute(HABILITARCANTIDADENTREGA);
//			session.removeAttribute(HABILITARCANTIDADRESERVA);
//			session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//			session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
//			session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
//			session.removeAttribute(PASOSPOPUP);
//			request.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
//			session.removeAttribute( POSICIONDIVCONFENTREGAS);
//			session.removeAttribute(HORA_SELECCIONADA);
//			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
//			regresarvaloresInicialesExistenEntregas(session);
//			formulario.setSeleccionCiudad(null);
//			formulario.setSelecionCiudadZonaEntrega(null);
//			session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
//			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
//			
//			//para mostrar opcion de canastos especiales
//			Collection detallePedidoDTOCol=(Collection)session.getAttribute(DETALLELPEDIDOAUX);
//			////formulario.setOpElaCanEsp(null);
//			if(CotizacionReservacionUtil.verificarCanastasEspeciales(detallePedidoDTOCol, request)){
//				session.setAttribute(MOSTRAROPCIONCANASTOSESPECIALES, "ok");
//			}
//			
//		}
//		
//		//TODO pendiente de pasar
//		/*-------- cuando se quiere usar una autorizaci\u00F3n externa ----------*/
//		else if(request.getParameter(PAR_USAR_AUTORIZACION)!=null){
//			String parametro = request.getParameter(PAR_USAR_AUTORIZACION);
//			if(parametro.equals("2")){
//				
//				//se realiza la consulta para llenar el combo de tipo de autorizacion
//				TipoAutorizacionDTO tipoAutorizacionDTO = new TipoAutorizacionDTO();
//				tipoAutorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//				
//				String[] codigosInternos= new String[]{MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"),
//						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.montoMinimoEntregaDomicilioCD")};
//				tipoAutorizacionDTO.setCriteriaSearch(new CriteriaSearch());
//				tipoAutorizacionDTO.getCriteriaSearch().addCriteriaSearchParameter(new CriteriaSearchParameter<String>("codigoInterno",ComparatorTypeEnum.IN_COMPARATOR,codigosInternos));
//				
//				Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCOL = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionDTO);
//				
//				tipoAutorizacionDTO = null;
//				
//				session.setAttribute(TIPO_AUTORIZACION_COL, tipoAutorizacionDTOCOL);
//				session.setAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS, "ok");
//				
//				//se inicializan los campos
//				formulario.setTipoAutorizacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
//				formulario.setNumeroAutorizacion(null);
//				formulario.setObservacionAutorizacion(null);
//			}
//		}
//		//TODO pendiente de pasar
//		/*-------- cuando se acepta el uso de una autorizaci\u00F3n externa ----------*/
//		else if(request.getParameter(PAR_ACE_USO_AUT) != null){
//			//se obtiene el tipo de autorizaci\u00F3n seleccionado				
//			String grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
//			if(formulario.getTipoAutorizacion()!=null && formulario.getNumeroAutorizacion()!= null){
//				if(formulario.getTipoAutorizacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega")) ||
//						formulario.getTipoAutorizacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.montoMinimoEntregaDomicilioCD"))){
//					grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
//				}
//				try{
//					//se realiza la validaci\u00F3n
//					AutorizacionDTO autorizacionDTO = AutorizacionesUtil.validarNumeroAutorizacion(request, formulario.getNumeroAutorizacion().trim(), formulario.getObservacionAutorizacion(), grupoAutorizacion, formulario.getTipoAutorizacion());
//					AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO, request);
//					//session.removeAttribute(SessionManagerSISPE.POPUP);
//					session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//					session.removeAttribute(TIPO_AUTORIZACION_COL);
//					messages.add("Autorizacion",new ActionMessage("errors.autorizacion.creada.exito"));
//					session.removeAttribute(CotizarReservarAction.NUMEROAUTORIZACIONEXTERNAERROR);
//				}catch (Exception e) {
//					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);					
//					session.setAttribute(CotizarReservarAction.NUMEROAUTORIZACIONEXTERNAERROR, "ok");
//				}
//			}
//		}
//		//TODO pendiente de pasar
//		/*-------- cuando se cancela el uso de una autorizaci\u00F3n externa ----------*/
//		else if(request.getParameter(PAR_CAN_USO_AUT) != null){			
//			session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//			session.removeAttribute(TIPO_AUTORIZACION_COL);
//			session.removeAttribute(CotizarReservarAction.NUMEROAUTORIZACIONEXTERNAERROR);
//		}
//
//		//Configuracion de la entrega
//		else if(request.getParameter("entregas")!=null){
//			
//			LogSISPE.getLog().info("Opci\u00F3n CD: {}",formulario.getOpStock());
//			
//			LogSISPE.getLog().info("entra a habilitar los parametros de ingreso segun la combinacion de opciones seleccionada");
//			
//			if(session.getAttribute(FECHABUSQUEDA)!=null && formulario.getBuscaFecha() == null){
//				formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));				
//			}
//			
//			if(session.getAttribute(FECHABUSQUEDA)!=null && formulario.getFechaEntregaCliente() == null){
//				formulario.setFechaEntregaCliente((DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
//			}
//			
//			if(beanSession.getPaginaTabPopUp().getTabSeleccionado()!=0){
//				asignacionValoresFormulario(session, formulario);
//			}
//			
//			
//			if(formulario.validarTipoEntregas(error,request)==0){
//				//recupero los datos del detalle del pedido
//				////Collection detallePedidoDTOCol=(Collection)session.getAttribute(DETALLELPEDIDOAUX);
//				//Bandera que me indica quien elabora las canastas especiales
//				////formulario.setOpElaCanEsp(null);
//				//si es true existe canastas especiales mostrar popUp
//				////if(CotizacionReservacionUtil.verificarCanastasEspeciales(detallePedidoDTOCol, request) && !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) &&
//						////!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//					//instanciarVentanaOpcionesCanastas(request);
//				////}else{
//				if(formulario.getOpElaCanEsp()==null){
//					formulario.setOpElaCanEsp(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//				}	
//				if(formulario.validarCantidadesIngresadasModificadas(error, request) == 0){
//										
//						aceptaConfiguracionEntrega(request, formulario, error, errors, warnings);
////						session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
////						session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
//						cargaCiudades(request, errors);
//						if(errors.size()>0){
//							LogSISPE.getLog().info("no se acpeto la configuracion");
//							
//							if(session.getAttribute(OPCIONLUGARENTREGA)!=null){
//								//Si la configuraci\u00F3n no fue aceptada regreso a la fecha y configuracion anterior
//								formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
//								formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
//								formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
//								formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
//								
//								if(session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null && (session.getAttribute(NOMBREENTIDADRESPONSABLE)==null || 
//										(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))))){
//									//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//								}
//								else if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && (session.getAttribute(NOMBREENTIDADRESPONSABLE)).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//									//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//									formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
//									
//								}
//								
//							}
//						}	
//						if(error.size() == 0){
//							
//							String tabHabilitados=null;
//							
//							tabHabilitados = tabHabilitadosParciales(request,tabHabilitados);
//							
//							if((String)session.getAttribute(SessionManagerSISPE.MENSAJES_SISPE)==null){
//								session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
//							}
//							
//							if(tabHabilitados == null){
//								if(beanSession.getPaginaTabPopUp().getCantidadTabs() == 3){
//									beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
//									regresarValoresIniciales(session);
//								}							
//								navegacionPopUp(session, beanSession,"Anterior","Confirmar","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajes','entregas','pregunta','opcionesBusqueda'",1,"anteriorD","aceptarD");
//							}
//							else{//parciales
//								if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){
//									navegacionPopUp(session, beanSession,"Anterior","Confirmar","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajes','entregas','pregunta','opcionesBusqueda'",2,"anteriorD","aceptarD");
//								}
//								else{
//									navegacionPopUp(session, beanSession,"Anterior","Siguiente","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","entregas","","'mensajesPopUp','listado_articulos'",1,"anteriorD","siguienterD");
//									session.setAttribute(TABSELECCIONADONAVEGACION, 2);
//									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
//									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//									regresarValoresIniciales(session);
//								}
//							}
//						}
//					}
//			}
//			
//		}
//		//regresar a configuracion de entregas
//		else if(request.getParameter("atrasPopUpEntregas")!=null){
//			asignacionValoresFormulario(session, formulario);			
//			
//			String tabHabilitados=null;
//			
//			tabHabilitados = tabHabilitadosParciales(request, tabHabilitados);
//			
//			if(tabHabilitados == null){
//				request.setAttribute(CONFIRMACIONANTERIORCONFENTREGAS, "ok");
//				session.removeAttribute(HABILITARCANTIDADENTREGA);
//				
//			}
//			else{
//				
//				if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){					
//					
//					request.setAttribute(CONFIRMACIONANTERIORCONFENTREGAS, "ok");					
//					
//				}
//				else{
//					regresarPopUp(session, beanSession,"Siguiente","","entregas","atrasPopUpEntregas",0,"siguienterD","");
//					session.removeAttribute(TABSELECCIONADONAVEGACION);
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
//					session.removeAttribute(PASOSPOPUP);
//					session.removeAttribute(HABILITARCANTIDADENTREGA);
//					regresarValoresIniciales(session);
//					regresarvaloresInicialesExistenEntregas(session);
//				}
//												
//				
//			}
//			session.removeAttribute( POSICIONDIVCONFENTREGAS);			
//			
//		}
//		else if(request.getParameter("seleccionaOK") != null){
//			LogSISPE.getLog().info("va a configurar las entregas nuevamente");
//			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
//			session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
//			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
//			asignacionValoresFormulario(session, formulario);
//			String tabHabilitados=null;
//			
//			tabHabilitados = tabHabilitadosParciales(request, tabHabilitados);
//			if(tabHabilitados == null){
//				anteriorConfiguracionEntregas(session, beanSession, "Siguiente", "", "entregas", "atrasPopUpEntregas", 0, "siguienterD", "", "ayuda.mensajePasoConfEnt1", "ayuda.paso1");
//			}
//			else{
//				if(beanSession.getPaginaTabPopUp().getTabSeleccionado()==2){
//					anteriorConfiguracionEntregas(session, beanSession, "Atras","Siguiente","atrasPopUpEntregas","entregas",1,"anteriorD","siguienterD", "ayuda.mensajePasoConfEnt2.1.1", "ayuda.paso2");
//				}
//			}
//			session.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
//			
//		}
//		else if(request.getParameter("seleccionaCANCEL") != null){
//			
//			LogSISPE.getLog().info("cancela y acepta la configuracion de entregas");
//	    	session.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
//			
//		}
//		else if(request.getParameter("replicarValores") != null){
//			
//			LogSISPE.getLog().info("empieza a replicar valores en todas las cajas de texto del pedido");	    	
//	    	Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//	    	
//			for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();				
//				
//				try{
//					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(formulario.getValorReplica()));
//				}catch(NumberFormatException e){
//					LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");					
//					errors.add("valorReplica",new ActionMessage("error.validacion.replica.cantidadEstado.invalido"));
//					break;
//				}
//				
//			}
//			
//		}
//		//aceptar entrega desde el opUp seleccionar responsable de las canastas especiales
//		else if(request.getParameter("seleccionarOpcion") != null){
//			if(formulario.getOpElaCanEsp() != null){
//				LogSISPE.getLog().info("Seleccion\u00F3 para la elaboraci\u00F3n--{}",formulario.getOpElaCanEsp());
//				session.removeAttribute(SessionManagerSISPE.POPUP);
//				aceptaConfiguracionEntrega(request, formulario, error, errors, warnings);
//				formulario.setSeleccionCiudad("");
//				if(errors.size()>0){
//					LogSISPE.getLog().info("no se acpeto la configuracion");
//					if(session.getAttribute(OPCIONLUGARENTREGA)!=null){
//						//Si la configuraci\u00F3n no fue aceptada regreso a la fecha y configuracion anterior
//						formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
//						formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
//						formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
//						formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
//						
//						if(session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null && (session.getAttribute(NOMBREENTIDADRESPONSABLE)==null || 
//								(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))))){
//							//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//						}
//						else if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && (session.getAttribute(NOMBREENTIDADRESPONSABLE)).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//							//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//							formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
//							
//						}
//					}
//				}
//			}
//		}
//		
//		//Ver responsables
//		else if(request.getParameter("verResponsable") != null){
//			LogSISPE.getLog().info("--Entro a ver responsable");
//			session.removeAttribute(ESTRUCTURA_RESPONSABLE);
//			//se obtine el secuencial del indice que envia desde la jsp.
//			String indiceEntrega = String.valueOf(request.getParameter("verResponsable"));
//			
//			Collection<EstructuraResponsable> detalleEstructuraCol = (ArrayList<EstructuraResponsable>)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
//			EstructuraResponsable estructuraResponsable = (EstructuraResponsable)CollectionUtils.get(detalleEstructuraCol, Integer.valueOf(indiceEntrega)-1);
//			session.setAttribute(EntregaLocalCalendarioAction.ESTRUCTURA_RESPONSABLE, estructuraResponsable);
//			
//			UtilPopUp popUp = new UtilPopUp();
//			popUp.setTituloVentana("Entidad responsable");
//			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
//			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//			popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', evalScripts:true});ocultarModal();");
//			popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//			popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
//			popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarVentanaResponsable.jsp");
//			popUp.setAncho(50D);
//			popUp.setTope(40D);
//			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
//			popUp = null;
//		}
//		
//		else if(request.getParameter("ocultarVentanaResponsable") != null){
//			session.removeAttribute(SessionManagerSISPE.POPUP);
//			// Objetos para construir los tabs, si fue modificacion de reserva
//			beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
//			session.removeAttribute(MOSTRAR_POPUPTAB);
//			session.removeAttribute(HABILITARCANTIDADENTREGA);
//			session.removeAttribute(TABSELECCIONADONAVEGACION);			
//			regresarValoresIniciales(session);
//			regresarvaloresInicialesExistenEntregas(session);
//			session.removeAttribute(HABILITARCANTIDADRESERVA);
//			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
//			session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
//			session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//			session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
//			session.removeAttribute(PASOSPOPUP);
//			session.removeAttribute( POSICIONDIVCONFENTREGAS);
//		}
//		
//		else if(request.getParameter("ocultarOpcion") != null){
//			session.removeAttribute(SessionManagerSISPE.POPUP);
//		}
//		/***********************************************************************************
//		 ***********************ACEPTAR PARAMETROS PARA LA ENTREGA**************************
//		 ***********************************************************************************/
//		//Boton aceptar en el ingreso de datos de la entrega
//		else if(request.getParameter("botonAceptarDatos")!=null){
//			LogSISPE.getLog().info("entra a botonAceptarDatos");
//			formulario.mantenerValoresEntregas(request);
//			//Hago las validaciones de formulario de valores nulos y formatos
//			if(formulario.validarBusqueda(error, request)==0){
//				if(formulario.getBuscaFecha()==null){
//					formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
//				}
//				
//				//verifica si ingresaron una fecha menor a la fecha minima de entrega o existe una autorizacion
//				if(session.getAttribute(EDITAFECHAMINIMA)!=null && 
//						(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0.0 
//								|| AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))!=null)){
//					//Diferencia entre fecha de entrega y fecha minima de entrega
//					long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
//					if(diferenciaEntregaBusca < 0.0){
//						LogSISPE.getLog().info("error en diferencia");
//						errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",formulario.getBuscaFecha()));
//					}
//					else{
//						boolean loc=true;//variable que se usa para reconocer cuando se ha ingresado un local valido
//						VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//						LogSISPE.getLog().info("*****local destino***** {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
//						//Si esta activo el combo para seleccionar los locales 
//						if(session.getAttribute(SELECCIONARLOCAL)!=null ){
//							String local=null;
//							//si fue ingresado un local desde la caja de textos
//							if(formulario.getLocal()!=null && !formulario.getLocal().equals("")){
//								LogSISPE.getLog().info("ingreso un local");
//								local=formulario.getLocal();
//								formulario.setListaLocales(null);
//							}
//							//si fue seleccionado un local desde el combo
//							else{
//								LogSISPE.getLog().info("local seleccionado: {}" , formulario.getListaLocales());
//								local=formulario.getListaLocales();
//							}							
//							//si el local seleccionado o ingresado es distinto al local de destino
//							if(errors.size()==0 && !local.equals(vistaLocalDestinoDTO.getId().getCodigoLocal().toString())){
//								//cargo el local destino
//								loc=buscaLocalBusqueda(formulario, request,local);
//								LogSISPE.getLog().info("encontro al local: {}" , loc);
//								//AYUDA								
//								
//								if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){					
//									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
//									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//								}
//								else{					
//										session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
//										session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));								
//								}
//								if((String)session.getAttribute(PASOSPOPUP)!=null){
//									session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
//									session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//								}
//								
//							}
//						}
//						//Si no se va a seleccionar un local por defecto el local destino siempre es el local de origen
//						session.removeAttribute(DIASELECCIONADO);
//						session.removeAttribute(CALENDARIODIALOCAL);
//						session.removeAttribute(DIRECCION);
//						//Si el local fue encontrado
//						if(loc){
//							//Obtengo el local destino
//							vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//							LogSISPE.getLog().info("***LOCAL DESTINO***: {}" ,vistaLocalDestinoDTO.getId().getCodigoLocal());
//							//En los casos donde deba haber una fecha de despacho se obtiene el calendario
//							//if(session.getAttribute(SELECCIONARCALENDARIO)!=null || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")))){
//							if(session.getAttribute(SELECCIONARCALENDARIO)!=null || (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")))){
//								LogSISPE.getLog().info("va a desplegar el calendario");
//								LocalID localID=new LocalID();
//								localID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
//								localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
//								//Cargo el calendario
//								obtenerLocal(session, request, localID, errors, formulario);
//							}
//							/*else if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
//								LogSISPE.getLog().info("va a habilitar las direcciones");
//								session.setAttribute(DIASELECCIONADO, "ok");
//							}*/
//							
//							session.removeAttribute(CONFIGURACIONCARGADA);
//							
//						}
//						//Si el local ingresado no existe
//						else
//							errors.add("errorLocal",new ActionMessage("errors.local"));
//					}
//				}
//				//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
//				else{
//					//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//					errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
//					session.removeAttribute(VISTALOCALCOL);
//				}
//			}
//		}
//		
//		//Selecci\u00F3n de checkbox para local de tr\u00E1nsito
//		/*else if(request.getParameter("botonCheckTransito")!=null && !request.getParameter("botonCheckTransito").equals("")){
//			LogSISPE.getLog().info("Seleccionando checkbox para establecer valor de local de tr\u00E1nsito...");
//			
//			//Se habilita variable de session de control
//			session.setAttribute(EXISTENCAMBIOS, "ok");
//		}*/
//		
//		/******************************************************************************
//		 ****************************GUARDAR ENTREGAS***********************************
//		 ******************************************************************************/
//		
//		//Guardar cambios en las entregas
//		else if(request.getParameter("botonGuardarCambios")!=null && !request.getParameter("botonGuardarCambios").equals("")){
//			LogSISPE.getLog().info("entra a guardar entregas");
//			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
//			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
//				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
//			}
//			
//			//check nuevamente si tiene o no pago en efectivo
//			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
//					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
//				formulario.setCheckPagoEfectivo(estadoActivo);
//			}
//			
//			
//			//OANDINO: Se verifica el registro correspondiente al checkbox seleccionado del listado de entregas
//			//Se crea nueva instancia de colecci\u00F3n para almacenar colecciones del tipo DetallePedidoDTO
//			List<DetallePedidoDTO> detallePedidoDTOColEntregas = (List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
//			
//			//Se crea nueva instancia de colecci\u00F3n para almacenar colecciones del tipo EntregaDTO
//			List<EntregaDTO> entregasDetalleCol = null;
//			
//			//Se crea una instancia del tipo EntregaDTO
//			EntregaDTO entregaDetalle = null;
//			
//			for(DetallePedidoDTO detalle : detallePedidoDTOColEntregas) {
//				for(Iterator<EntregaDTO> iteradorEnt = detalle.getEntregas().iterator();iteradorEnt.hasNext();) {
//					EntregaDTO entregaDTOTrans = iteradorEnt.next();
//					entregaDTOTrans.setCodigoLocalTransito(null);
//				}
//			}
//			
//			//Se recorre el arreglo creado en base a la selecci\u00F3n m\u00FAltiple de checkboxes
//			if(formulario.getCheckTransitoArray() != null && formulario.getCheckTransitoArray().length > 0){
//				for(int i=0; i<formulario.getCheckTransitoArray().length; i++){
//						LogSISPE.getLog().info("Value CheckTransito: {}", formulario.getCheckTransitoArray()[i]);
//						//Se obtiene el registro del checkbox seleccionado
//						String[] valorCheck = formulario.getCheckTransitoArray()[i].split("-");		
//						
//						//Obtengo el registro de detalle
//						int numRegistro=(new Integer(valorCheck[1])).intValue();
//						
//						//Obtengo el registro de entrega del detalle
//						int numEntrega=(new Integer(valorCheck[2])).intValue();
//						
//						//Se obtiene el par\u00E1metro VALUE del chekcbox
//						String valorParametro = valorCheck[0];
//						
//						LogSISPE.getLog().info("Reg. Detalle[{}]: {}",i,numRegistro);
//						LogSISPE.getLog().info("Reg. Entrega[{}]: {}",numEntrega,numEntrega);
//						
//						//Se obtiene el listado original de registros de entregas presentado en pantalla desde sesi\u00F3n
//						detallePedidoDTOColEntregas=(List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
//															
//						// Se obtiene el DTO espec\u00EDfico correspondiente al registro obtenido de entre toda la colecci\u00F3n original
//						DetallePedidoDTO detallePedidoDTO = detallePedidoDTOColEntregas.get(numRegistro);
//						
//						LogSISPE.getLog().info("Art\u00EDculo[{}]: {}",i,detallePedidoDTO.getId().getCodigoArticulo());
//														
//						//Se obtiene la colecci\u00F3n de entregas por cada detalle
//						entregasDetalleCol = (List<EntregaDTO>)detallePedidoDTO.getEntregas();
//						
//						//Obtengo el registro espec\u00EDfico del total de entregas por detalle
//						entregaDetalle = entregasDetalleCol.get(numEntrega);
//						
//						//Se setean los valores para el c\u00F3digo del local de tr\u00E1nsito
//						entregaDetalle.setCodigoLocalTransito(Integer.valueOf(valorParametro));
//				}
//			}
//				//se guarda el valor de los checks de transito
//				session.setAttribute(EntregaLocalCalendarioUtil.CHECKS_ENTREGAS_TRANSITO, formulario.getCheckTransitoArray());
//				
//				//---------------------------------------------------------------------------------------------------------------------------------
//				//guardo el detalle del pedido
//				Collection detallePedidoDTOCol = (Collection)session.getAttribute(DETALLELPEDIDOAUX);
//				session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoDTOCol);
//				
//				//si existen cambios en la configuraci\u00F3n de las entregas
//				if(session.getAttribute(EXISTENCAMBIOS)!=null){
//					LogSISPE.getLog().info("Opci\u00F3n: EXISTEN CAMBIOS...");
//
//					//guardo las direcciones
//					Collection direccionesCol = (Collection)session.getAttribute(DIRECCIONESAUX);
//					session.setAttribute(DIRECCIONES, direccionesCol);
//					//guardo los costos
//					Collection costosCol = (Collection)session.getAttribute(COSTOENTREGAAUX);
//					session.setAttribute(COSTOENTREGA, costosCol);
//					//guardo los cambios en el calendario
//					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);								
//					
//					// Se mantienen en sesison los datos a ser guardados en kardex
//					session.setAttribute(CALENDARIOCONFIGURACIONDIALOCAL, calendarioConfiguracionDiaLocal);
//					
//					//guarda la fecha de entrega
//					//TODO verificar esta l\u00EDnea 
//					//session.setAttribute(CotizarReservarAction.FECHA_ENTREGA, DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//					formulario.setFechaEntrega((String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA));
//
////wcEntregas		if(session.getAttribute(VALORTOTALENTREGAAUX)!=null){
////						Double costo=(Double)session.getAttribute(VALORTOTALENTREGAAUX);
////						LogSISPE.getLog().info("costo final: {}" , costo);
////						session.setAttribute(VALORTOTALENTREGA, costo);
////					}wcEntregas
//					if(session.getAttribute(SECDIRECCIONESAUX)!=null){
//						Integer cont=(Integer)session.getAttribute(SECDIRECCIONESAUX);
//						session.setAttribute(SECDIRECCIONES, cont);
//					}
//					session.removeAttribute(DETALLELPEDIDOAUX);
//					session.removeAttribute(DIRECCIONESAUX);
//					session.removeAttribute(COSTOENTREGAAUX);
//					session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
//					session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
////wcEntregas		session.removeAttribute(VALORTOTALENTREGAAUX);
//					session.removeAttribute(STOCKENTREGAAUX);
//					session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
//					
//					if(session.getAttribute(ENTIDADRESPONSABLELOCAL)==null){
//						session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//						//session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//					}else {
//						session.setAttribute(NOMBREENTIDADRESPONSABLE, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//					}
//					
//					//se actualiza el costo total del pedido
//					formulario.actualizarCostoTotalPedido(request);
//					forward="cotizar";
//				}else{
//					
//					LogSISPE.getLog().info("Opci\u00F3n: NO EXISTEN CAMBIOS...");
//					session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
//					formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//					//se actualiza el costo total del pedido
//					
//					//se actualiza los valores de la variables auxiliares de session COSTOENTREGA y DIRECCIONES
//					//guardo las direcciones
//					if(session.getAttribute(ENTREGAS_ELIMINO_DIRECCION) != null){
//						Collection direccionesCol = (Collection)session.getAttribute(DIRECCIONESAUX);
//						session.setAttribute(DIRECCIONES, direccionesCol);
//					}
//					
//						Collection costosCol = (Collection)session.getAttribute(COSTOENTREGAAUX);
//						session.setAttribute(COSTOENTREGA, costosCol);
//					
//					
//					formulario.actualizarCostoTotalPedido(request);
//					session.removeAttribute(STOCKENTREGAAUX);
//					forward="cotizar";
//				}
//				session.removeAttribute(CLASIFICACIONES_PERECIBLES);
//				session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
//				session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
//				
//				//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
//				CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
//				
//				//si esta en el tab de contactos se cambia al tab de pedidos				
//				 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
//					 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);		
//					 session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
//					//se guarda el beanSession
//					SessionManagerSISPE.setBeanSession(beanSession, request);
//				 }
//				 entidadResponsablePedido(session);
//				//Se contruyen los tabs de Contacto y Pedidos
//				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
//				beanSession.setPaginaTab(tabsCotizaciones);
//		}
//
//		//Cancelar entrega (abre la ventana de pregunta si desea guardar los cambios)
//		else if(request.getParameter("botonCerrarEntregas")!=null && !request.getParameter("botonCerrarEntregas").equals("")){
//			LogSISPE.getLog().info("entra a cancelar la entrega");
//			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
//			if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
//				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
//			}
//			
//			//check nuevamente si tiene o no pago en efectivo
//			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
//					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
//				formulario.setCheckPagoEfectivo(estadoActivo);
//			}
//			session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
//			if(session.getAttribute(EXISTENCAMBIOS)!=null){
//				LogSISPE.getLog().info("si existieron cambios");
//				request.setAttribute("ventanaCerrar", "ok");				
//			}
//			else{
//				session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
//				formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//				//se actualiza el costo total del pedido
//				formulario.actualizarCostoTotalPedido(request);
//				//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
//				CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
//				session.removeAttribute(STOCKENTREGAAUX);
//				session.removeAttribute(CLASIFICACIONES_PERECIBLES);
//				session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
//				session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
//				session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
//				session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
//				session.removeAttribute(ENTREGAS_ELIMINO_DIRECCION);
//				//Se contruyen los tabs de Contacto y Pedidos
//				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
//				beanSession.setPaginaTab(tabsCotizaciones);
//				forward="cotizar";				
//			}
//			
//		}
//
//		//Salir sin guardar los cambios
//		else if(request.getParameter("regresar")!=null && !request.getParameter("regresar").equals("")){
//			//Regresar a la cotizacion
//			LogSISPE.getLog().info("entro a regresar");
//			session.removeAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"));
//			formulario.setFechaEntrega((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
//			//se actualiza el costo total del pedido
//			formulario.actualizarCostoTotalPedido(request);
//			session.removeAttribute(DETALLELPEDIDOAUX);
//			session.removeAttribute(DIRECCIONESAUX);
//			session.removeAttribute(COSTOENTREGAAUX);
//			session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
////			session.removeAttribute(VALORTOTALENTREGAAUX);
//			session.removeAttribute(STOCKENTREGAAUX);
//			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
//			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
//			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
//			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_PROCESADO);
//			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_SELECCIONADO);
//			session.removeAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL);
//			//check nuevamente si tiene o no pago en efectivo
//			if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
//					session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
//				formulario.setCheckPagoEfectivo(estadoActivo);
//			}
//			
//			//prueba
//			Collection<DetallePedidoDTO> detallePedidoDTOCol=(ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//			for(DetallePedidoDTO detallePedido: detallePedidoDTOCol){
//				if(detallePedido.getEntregas()!=null && detallePedido.getEntregas().size()>0)
//					LogSISPE.getLog().info("------numero de entregas----- {}" , detallePedido.getEntregas().size());
//			}
//			
//			//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
//			CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
//			//Se contruyen los tabs de Contacto y Pedidos
//			PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
//			beanSession.setPaginaTab(tabsCotizaciones);
//			forward="cotizar";
//		}
//
//		/***********************************************************************************
//		 *********************************CALENDARIO********************************
//		 ***********************************************************************************/
//
//		//Selecciona un dia del mes
//		else if(request.getParameter("seleccionCal")!=null){
//			LogSISPE.getLog().info("entra a seleccionar un dia del calendario");
//			formulario.mantenerValoresEntregas(request);
//			if(formulario.validarFechaEntrega(error, request)==0){
//				//Entro a seleccionar un calendario
//				seleccionDia(formulario,new Integer(request.getParameter("seleccionCal")).intValue(),session,true,errors,request);
//				formulario.setDirecciones(null);
//				formulario.setDireccion(null);
//				formulario.setDistancia(null);
//				formulario.setDistanciaH(null);
//				formulario.setDistanciaM(null);
//				session.removeAttribute(DIRECCION);
//				session.removeAttribute("otraCuidad");
//				session.setAttribute( POSICIONDIVCONFENTREGAS ,"ok");
//			}
//		}
//		/***************************************NAVEGACION ENTRE MESES*************************************/
//		else if(request.getParameter("mesAnterior")!=null){
//			LogSISPE.getLog().info("mes anterior");
//			formulario.mantenerValoresEntregas(request);
//			Date mes=(Date)session.getAttribute(MESSELECCIONADO);
//			//resto un mes al mes actual
//			GregorianCalendar fechaCalendario=new GregorianCalendar();
//			fechaCalendario.setTime(mes);
//			fechaCalendario.add(Calendar.MONTH,-1);
//			mes=fechaCalendario.getTime();
//			//fecha minima
//			Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//			/*****************************************************************************************/
//			//fecha maxima
//			Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//			obtenerCalendario(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}
//		else if(request.getParameter("mesSiguiente")!=null){
//			LogSISPE.getLog().info("mes siguiente");
//			formulario.mantenerValoresEntregas(request);
//			Date mes=(Date)session.getAttribute(MESSELECCIONADO);
//			//sumo un mes al mes actual
//			GregorianCalendar fechaCalendario=new GregorianCalendar();
//			fechaCalendario.setTime(mes);
//			fechaCalendario.add(Calendar.MONTH,1);
//			mes=fechaCalendario.getTime();
//			//fecha minima
//			Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//			/*****************************************************************************************/
//			//fecha maxima
//			Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//			/*****************************************************************************************/
//			obtenerCalendario(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}		
//		else if(request.getParameter("semanaAnterior")!=null){
//			LogSISPE.getLog().info("semana anterior");
//			formulario.mantenerValoresEntregas(request);
//			
//			Date fechaEntCliente = (Date)session.getAttribute(FECHABUSQUEDA); 
//			
//			Calendar calendario = Calendar.getInstance();
//			calendario.setTime(fechaEntCliente);
//			
//			calendario.add(Calendar.DAY_OF_WEEK, - calendario.get(Calendar.DAY_OF_WEEK)+1);
//			fechaEntCliente = calendario.getTime();
//			
//			//Date mes=(Date)session.getAttribute(MESSELECCIONADO);
//			Date mes = (Date)session.getAttribute(FECHAMINIMA);
//			//resto una semana al mes actual
//			GregorianCalendar fechaCalendario=new GregorianCalendar();
//			fechaCalendario.setTime(mes);
//			
//			GregorianCalendar fechaCalendarioAux=new GregorianCalendar();
//			fechaCalendarioAux.setTime(mes);
//			fechaCalendarioAux.add(Calendar.WEEK_OF_YEAR,-1);
//						
//			if(fechaEntCliente.before(fechaCalendarioAux.getTime()) || fechaEntCliente.equals(fechaCalendarioAux.getTime())){
//				fechaCalendario.add(Calendar.WEEK_OF_YEAR,-1);
//				mes=fechaCalendario.getTime();
//				//fecha minima
//				//Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//				Date fechaMinima = mes;
//
//				//fecha maxima
//				Date fechaMaxima=(Date)session.getAttribute(FECHAMINIMA);
//				obtenerCalendarioPorSemana(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
//			}
//				
//		}
//		else if(request.getParameter("semanaSiguiente")!=null){
//			LogSISPE.getLog().info("semana siguiente");
//			formulario.mantenerValoresEntregas(request);
//			//Date mes=(Date)session.getAttribute(MESSELECCIONADO);
//			Date mes = (Date)session.getAttribute(FECHAMAXIMA);
//			//sumo una semana al mes actual
//			GregorianCalendar fechaCalendario=new GregorianCalendar();
//			fechaCalendario.setTime(mes);
//			fechaCalendario.add(Calendar.WEEK_OF_YEAR,1);
//			mes=fechaCalendario.getTime();
//			//fecha minima
//			//Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//			Date fechaMinima = (Date)session.getAttribute(FECHAMAXIMA);
//
//			//fecha maxima
//			//Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//			Date fechaMaxima = mes;
//			obtenerCalendarioPorSemana(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}
//		else if(request.getParameter("verificarDisponibilidadCamionesHora") != null){
//			LogSISPE.getLog().info("verificarDisponibilidadCamionesHora");		
//			session.setAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO,(String)request.getParameter("verificarDisponibilidadCamionesHora"));
//			
//			Object[] calendarioDiaLocalDTOOBJ =	(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//			if(calendarioDiaLocalDTOOBJ != null && calendarioDiaLocalDTOOBJ.length > 0){
//				int indiceDiaLocal= new Integer(request.getParameter("verificarDisponibilidadCamionesHora").split("-")[0]);
//				int indiceHoraLocal = new Integer(request.getParameter("verificarDisponibilidadCamionesHora").split("-")[1]);
//				session.setAttribute(DIASELECCIONADOAUX, indiceDiaLocal);
//				
//				CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indiceDiaLocal];
//				Set<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol = (HashSet<CalendarioHoraLocalDTO>)calendarioDiaLocalDTO.getCalendarioHoraLocalCol();
//				ArrayList<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux =  new ArrayList<CalendarioHoraLocalDTO>(calendarioHoraLocalDTOCol);
//				CalendarioHoraLocalDTO calendarioHoraLocalSeleccionado = calendarioHoraLocalDTOColAux.get(indiceHoraLocal);					
//				
//				//se setean los datos del formulario asociados a la fecha y hora seleccionda
//				formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//				String[] horaSeleccionada = calendarioHoraLocalSeleccionado.getId().getHora().toString().split(":");				
//				formulario.setHoras(horaSeleccionada[0]);
//				formulario.setMinutos(horaSeleccionada[1]);
//				formulario.setSegundos(horaSeleccionada[2]);				
//				session.setAttribute(HORA_SELECCIONADA, horaSeleccionada);
//				
//				//realiza los procesos normales que hacia antes
//				formulario.mantenerValoresEntregas(request);
//				if(formulario.validarFechaEntrega(error, request)==0){
//					seleccionDia(formulario, indiceDiaLocal, session, true, errors, request);
//					
//					EntregaLocalCalendarioUtil.verificarDisponibilidadCamionesHora(request, calendarioHoraLocalSeleccionado, formulario, errors, warnings, info);	
//					//si hay errores o advertencias no se pinta el dia seleccionado
//					if(errors.size() > 0 || warnings.size() > 0){
//						session.removeAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO);
////						formulario.setFechaEntregaCliente(null);
//						formulario.setHoras(null);
//						formulario.setMinutos(null);
//						request.getSession().removeAttribute(EntregaLocalCalendarioUtil.DIA_HORA_SELECCIONADO);
//						request.getSession().removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX);
//						request.getSession().removeAttribute(EntregaLocalCalendarioAction.HORA_SELECCIONADA);
//						request.getSession().removeAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES);
//					}else{
//						session.setAttribute(CALENDARIODIALOCALCOL, calendarioDiaLocalDTOOBJ);
//					}
//				}
//				
//			}
//									
//		}
// 
//
//		/***********************************************************************************
//		 *********************************DIRECCIONES***************************************
//		 ***********************************************************************************/
//
//		else if(request.getParameter("seleccionaDir")!=null){
//			formulario.mantenerValoresEntregas(request);
//			//Si la entrega es a domicilio
//			if(session.getAttribute(LUGARENTREGA)!=null && session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				LogSISPE.getLog().info("entra a asignar una direccion existente: {}" , formulario.getDirecciones().length);
//				boolean seleccionado=false;//reconoce si la direccion fue o no seleccionada
//				//Recupero la lista de direcciones
//				List<DireccionesDTO> direccionesDTOCol = (List<DireccionesDTO>)session.getAttribute(DIRECCIONESAUX);
//				//funcion para solo chequear una direccion
//				String[] seleccionados = new String[direccionesDTOCol.size()];
//				LogSISPE.getLog().info("tamano dir1: {}" ,seleccionados.length);
//				for(int i = 0; i<seleccionados.length; i++){
//					if(i != Integer.parseInt(request.getParameter("seleccionaDir"))){
//						LogSISPE.getLog().info("i1:{}" ,i);
//						seleccionados[i]=null;
//					}else{
//						LogSISPE.getLog().info("i2:{}" ,i);
//						seleccionados[i]=Integer.valueOf(i).toString();
//						seleccionado = true;
//						if(formulario.getDirecciones().length==0){
//							seleccionado = false;
//							seleccionados[i] = null;
//						}
//					}
//				}
//				
//				formulario.setDirecciones(seleccionados);
//				LogSISPE.getLog().info("tamano dir2: {}",seleccionados.length);
//				if(seleccionado){
//					//obtengo el local seleccionado
//					VistaLocalDTO vistaLocalDTO = (VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//					DireccionesDTO direccionesDTO = direccionesDTOCol.get(Integer.parseInt(request.getParameter("seleccionaDir")));
//					session.setAttribute(DIRECCION, direccionesDTO);
//					LogSISPE.getLog().info("codigo local: {}", vistaLocalDTO.getId().getCodigoLocal());
//					LogSISPE.getLog().info("codigo dirSec: {}", direccionesDTO.getCodigoSector());
//					//Pregunto si la direccion pertence al sector del local seleccionado
//					if(vistaLocalDTO.getId().getCodigoLocal().toString().equals(direccionesDTO.getCodigoSector())){
//						formulario.setDireccion(direccionesDTO.getDescripcion());
//						LogSISPE.getLog().info("minutos: {}", direccionesDTO.getMinutos());
//						if((direccionesDTO.getHoras()!=null && !direccionesDTO.getHoras().equals("")) || (direccionesDTO.getMinutos()!=null && !direccionesDTO.getMinutos().equals(""))){
//							LogSISPE.getLog().info("llena distancia en horas");
//							if(direccionesDTO.getHoras()!=null && !direccionesDTO.getHoras().equals("0"))
//								formulario.setDistanciaH(direccionesDTO.getHoras());
//							formulario.setDistanciaM(direccionesDTO.getMinutos());
//							formulario.setUnidadTiempo("H");
//							formulario.setDistancia(null);
//						}else{
//							LogSISPE.getLog().info("llena distancia en kilometros");
//							formulario.setDistancia(direccionesDTO.getDistanciaDireccion());
//							formulario.setUnidadTiempo("K");
//							formulario.setDistanciaH(null);
//							formulario.setDistanciaM(null);
//						}
//					}else{
//						formulario.setDirecciones(null);
//						formulario.setUnidadTiempo(null);
//						formulario.setDistancia(null);
//						formulario.setDistanciaH(null);
//						formulario.setDistanciaM(null);
//						formulario.setDireccion(null);
//						session.removeAttribute(DIRECCION);
//						errors.add("seleccionDireccion",new ActionMessage("errors.seleccionDireccionL",vistaLocalDTO.getNombreLocal()));
//					}
//				}else{
//					formulario.setDirecciones(null);
//					formulario.setUnidadTiempo(null);
//					formulario.setDistancia(null);
//					formulario.setDistanciaH(null);
//					formulario.setDistanciaM(null);
//					formulario.setDireccion(null);
//					session.removeAttribute(DIRECCION);
//				}
//			}
//			//Si la entrega es a locales
//			else
//				formulario.setDirecciones(null);
//		}
//		/***********************************************************************************
//		 *********************************RESERVACION***************************************
//		 ***********************************************************************************/
//		//Actualizar los detalles de las entregas
//		else if(request.getParameter("botonActualizarEntrega")!=null){
//			LogSISPE.getLog().info("Entro a actualizar entregas");
//			//Hago las validaciones de formulario de valores nulos y formatos
//			if(formulario.validarCantidadesEntregas(error, request)==0){
//				int indexEntregas=0;//indice para las cantidades de las entregas
//				boolean entregasActualizadas=false;//varible que verifica si existieron modificaciones en las entregas
//				int numeroBultos=0;//variable para calcular el numero de bultos
//				//recupero los datos del detalle del pedido
//				Collection detallePedidoDTOCol=(Collection)session.getAttribute(DETALLELPEDIDOAUX);
//				//Ingreso de nuevos detalles
//				for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//					DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//					/********************************************************************************/
//					//Comprueba y modifica los detalles de las entregas
//					Collection entregasCol = detallePedidoDTO.getEntregas();
//					if(entregasCol!=null && entregasCol.size()>0){
//						for(Iterator numeroDetalleEntrega=entregasCol.iterator();numeroDetalleEntrega.hasNext();){
//							EntregaDTO entregaDTO = (EntregaDTO)numeroDetalleEntrega.next();
//							LogSISPE.getLog().info("cantidadEntrega[{}]: {}" ,indexEntregas, entregaDTO.getCantidadEntrega());
//							LogSISPE.getLog().info("NpCantidadEntrega: {}" , entregaDTO.getNpCantidadEntrega());
//							LogSISPE.getLog().info("npCantidadEstado******* {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//							LogSISPE.getLog().info("cantidad despacho: {}" , entregaDTO.getCantidadDespacho());
//							LogSISPE.getLog().info("cantidad NpCantidadDespacho: {}" , entregaDTO.getNpCantidadDespacho());
//							
//							//Existen cambios en las entregas
//							if(entregaDTO.getCantidadEntrega().longValue()!=entregaDTO.getNpCantidadEntrega().longValue()){
//								//Calcula el numero de bultos
//								LogSISPE.getLog().info("Va a actualizar la entrega");
//								//actualiza el numero de bultos entregados
//								detallePedidoDTO.setNpContadorEntrega(new Long(detallePedidoDTO.getNpContadorEntrega().longValue()-entregaDTO.getCantidadEntrega()+entregaDTO.getNpCantidadEntrega()));
//								LogSISPE.getLog().info("NpContadorEntrega: {}" , detallePedidoDTO.getNpContadorEntrega());
//								entregaDTO.setCantidadEntrega(entregaDTO.getNpCantidadEntrega());
//								//actualiza la cantidad de articulos que falta por entregar
//								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue()));
//								LogSISPE.getLog().info("NpCantidadEstado: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//								//existieron cambios en las entregas
//								session.setAttribute(EXISTENCAMBIOS,"ok");
//								entregasActualizadas=true;
//							}
//							if(entregaDTO.getCantidadDespacho().longValue()!=entregaDTO.getNpCantidadDespacho().longValue()){
//								if(entregaDTO.getNpCantidadDespacho().longValue()>0){
//									entregaDTO.setFechaRegistroDespacho(null);
//								}
//								else{
//									GregorianCalendar fechaActual=new GregorianCalendar();
//									// IMPORTANTE: Aqui se setea la fecha actual para el registro de kardex
//									fechaActual.setTime(new Date());
//									entregaDTO.setFechaRegistroDespacho(new Timestamp(fechaActual.getTime().getTime()));
//									entregaDTO.setCantidadDespacho(new Long(0));
//								}
//								LogSISPE.getLog().info("NpCantidadDespacho: {}",entregaDTO.getNpCantidadDespacho());
//								//Calcula el numero de bultos
//								numeroBultos=UtilesSISPE.calcularCantidadBultos(entregaDTO.getNpCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
//								LogSISPE.getLog().info("num bultos: {}" , numeroBultos);
//								LogSISPE.getLog().info("Va a actualizar la entrega");
//								//actualiza el numero de bultos entregados
//								detallePedidoDTO.setNpContadorDespacho(new Long(detallePedidoDTO.getNpContadorDespacho().longValue()-entregaDTO.getCantidadDespacho()+entregaDTO.getNpCantidadDespacho()));
//								LogSISPE.getLog().info("NpContadorEntrega: {}" , detallePedidoDTO.getNpContadorDespacho());
//								entregaDTO.setCantidadDespacho(entregaDTO.getNpCantidadEntrega());
//								entregaDTO.setNpCantidadBultos(new Integer(numeroBultos));
//								//actualiza la cantidad de articulos que falta por reservar
//								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorDespacho().longValue()));
//								LogSISPE.getLog().info("CantidadReservarSIC: {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
//								//existieron cambios en las entregas
//								session.setAttribute(EXISTENCAMBIOS,"ok");
//								entregasActualizadas=true;
//							}
//							indexEntregas++;
//						}
//					}
//				}
//				//Si existieron cambios en las entregas
//				if(entregasActualizadas){
//					LogSISPE.getLog().info("existio cambios en las entregas");
//					messages.add("agregaDetalles",new ActionMessage("messages.actualizarEntrega"));
//					//si la entrega no es una entrega directa de la bodega al local
//					if((session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
//							&& !session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) || (session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
//						LogSISPE.getLog().info("elimina la sesion de dia seleccionado");
//						session.removeAttribute(DIASELECCIONADO);
//					}
//					session.removeAttribute(CALENDARIODIALOCAL);
//					LogSISPE.getLog().info("Existieron Cambios En La Reserva");
//					CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal = (CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
//					LogSISPE.getLog().info("****calendarioConfiguracionDiaLocal**** {}" , calendarioConfiguracionDiaLocal);
//					session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocal);
//					session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
//					formulario.setDirecciones(null);
//					/***************Actualiza el calendario********************/
//					if(session.getAttribute(CALENDARIODIALOCALCOL)!=null){
//						//fecha minima
//						Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//						//fecha maxima
//						Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//						//obtengo el mes del dia que fue seleccionado para la entrega
//						Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//						obtenerCalendario(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario,true);
//					}
//				}
//				else
//					warnings.add("sinCambios",new ActionMessage("warnings.sinCambiosEntregas"));
//			}
//		}
//		//Agrega los detalles de las entregas
//		else if(request.getParameter("botonAceptarEntrega")!=null){
//			//setea la hora al formulario
//			String[] horaSelecionada = (String[])session.getAttribute(HORA_SELECCIONADA);
//			if(horaSelecionada != null && horaSelecionada.length > 0){
//				formulario.setHoras(horaSelecionada[0]);
//				formulario.setMinutos(horaSelecionada[1]);
//				formulario.setSegundos(horaSelecionada[2]);
//			}
//			
//			String valorCodigoCiudadSector =null;
//			if(formulario.getSelecionCiudadZonaEntrega() != null && !formulario.getSelecionCiudadZonaEntrega().equals(""))
//				valorCodigoCiudadSector = formulario.getSelecionCiudadZonaEntrega();
//				
//			asignacionValoresFormulario(session, formulario);
//			if(formulario.getOpLugarEntrega() !=null && formulario.getOpTipoEntrega() != null && formulario.getOpStock() !=null){
//				LogSISPE.getLog().info("Entro a agregar entregas");
//				
//				String validarCheckTransito = "";
//				String valorCodigoTransito = "";
//										
//				String fechaMinimaEntrega = null;
//				String fechaEntregaCliente = null;
//				
//				if(session.getAttribute(FECHABUSQUEDA)!=null && formulario.getBuscaFecha() == null){
//					formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//					formulario.setFechaEntregaCliente(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//				}
//				
//				Long sumaCantSolCD = session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE) != null ? (Long)session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE): 0L;
//				LogSISPE.getLog().info("sumaCantSolCD {}",sumaCantSolCD.longValue());
//				if(formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
//					//si no se solicit\u00F3 mercader\u00EDa al CD
//					//se toman las fechas directamente del formulario, porque no hay un calendario de por medio
//					fechaMinimaEntrega = formulario.getBuscaFecha();
//					fechaEntregaCliente = formulario.getFechaEntregaCliente();
//				}else{
//					//se recuperan las fechas de sesi\u00F3n, porque hay un calendario de por medio y si las fechas son cambiadas primero se debe hacer click en el bot\u00F3n ACEPTAR
//					//para que cambie la configuraci\u00F3n del calendario
//					fechaMinimaEntrega = ConverterUtil.parseDateToString((Date)session.getAttribute(FECHABUSQUEDA));
//					fechaEntregaCliente = (String)session.getAttribute(FECHAENTREGACLIENTE);
//
//					if((String)session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && (String)session.getAttribute(LUGARENTREGA) !=null && session.getAttribute(LUGARENTREGA).toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//						//este es un caso especial donde no hay calendario
//						fechaMinimaEntrega = formulario.getBuscaFecha();
//						fechaEntregaCliente = formulario.getFechaEntregaCliente();
//					}
//					//jmena aqui le subo a session la fecha de entrega al cliente
//					session.setAttribute(FECHAENTREGACLIENTE, fechaEntregaCliente);
//					
//					//OANDINO: Siempre y cuando se haya solicitado a CD se procede a validar el valor del par\u00E1metro de tr\u00E1nsito
//					//para mostrar o no checkbox seg\u00FAn el c\u00F3digo de local y ciudad 										
//																	
//					// Se declara variable para almacenar el codigo de la ciudad
//					String valorCodigoCiudad = "";					
//					//Se verifica si se ha seleccionado entrega a domicilio y la entidad responsable es la bodega
//					//if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
//					if(formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
//						LogSISPE.getLog().info("Se ha elegido realizar una entrega a domicilio");
//						//Se obtiene el valor del combo de selecci\u00F3n de cuidades a comparar con VALORPARAMETRO
//						valorCodigoCiudad = (String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO);
//					}else{				
//						// Se obtiene desde session el local de entrega
//						VistaLocalDTO vistaLocalEntregaDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//						LogSISPE.getLog().info("Local destino para entregas: {}", vistaLocalEntregaDTO.getId().getCodigoLocal());
//						
//						// Se obtiene el c\u00F3digo de la ciudad del local desde BD
//						LocalID localID = new LocalID();
//						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//						localID.setCodigoLocal(vistaLocalEntregaDTO.getId().getCodigoLocal());
//						LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);				
//						LogSISPE.getLog().info("Ciudad destino para entregas: {}" ,localDTO.getCodigoCiudad());
//						
//						// Se setea en la variable general el valor del c\u00F3digo de la ciudad a comparar con VALORPARAMETRO
//						valorCodigoCiudad = localDTO.getCodigoCiudad();				
//					}
//												
//					// Se obtiene desde session el contenido de VALORPARAMETRO de SCSPETPARAMETRO
//					String valorParametro = (String)session.getAttribute(VAR_VALOR_PARAMETRO_TOTAL);
//					String[] parametroDividido = valorParametro.split(",");						
//					
//					// Se comparan c\u00F3digos de ciudad con VALORPARAMETRO
//					for(int i=0; i<parametroDividido.length; i++){
//						
//						String[] codigosCiudad = parametroDividido[i].split("-");						
//						LogSISPE.getLog().info("Valor par\u00E1metro - c\u00F3digo ciudad truncado: {}",codigosCiudad[0]);		
//						
//						if(codigosCiudad[0].equals(valorCodigoCiudad)){
//							LogSISPE.getLog().info("Los c\u00F3digos son iguales");
//							
//							//Se almacena codigosCiudad[1] en una variable de session y ese valor se setea en VALUE del checkbox 
//							LogSISPE.getLog().info("Valor c\u00F3digo ciudad: {}",codigosCiudad[1]);
//																			
//							//session.setAttribute(VAR_VALOR_CODIGO_PARAMETRO, codigosCiudad[1]);
//							valorCodigoTransito = codigosCiudad[1];
//							
//							//Variable que determina si se muestra o no el checkbox en pantalla												
//							validarCheckTransito = "siAplica";	
//													
//							break;
//						}
//					}
//				}
//				
//				//valida lo de autorizaciones
//				boolean bodegaCanastas = false; //bandera que me indica si la configuracion es para la bod(99)
//				Date fechaMinimaReferenciaAut = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
//				Date fechaBusquedaAux =  DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
//				if(session.getAttribute(EXISTELUGARENTREGA)!=null && session.getAttribute(EDITAFECHAMINIMA)!=null){
//					//si la fecha m\u00EDnima es menor a hoy
//					if(fechaBusquedaAux.getTime() >= fechaMinimaReferenciaAut.getTime()){
//						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
//						//Verifica si la fecha minima de entrega fue modificada
//						if((ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0
//								|| autorizacionDTO!=null)){
//							
//							LogSISPE.getLog().info("va a verificar la fecha minima de entrega");
//							
//							//jmena en la configuracion Inicializo fechaEntregaCliente
//							if(formulario.getFechaEntregaCliente()==null || formulario.getFechaEntregaCliente().equals("")){
//								formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES)));
//							}
////							if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////									formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//							if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//								bodegaCanastas = true;
//							}
//							//Diferencia entre fecha de entrega y fecha minima de entrega
//							long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
//							LogSISPE.getLog().info("diferenciaEtregaBusca: {}" , diferenciaEntregaBusca);
//							
//							if(diferenciaEntregaBusca<0.0 && !bodegaCanastas){
//								LogSISPE.getLog().info("error en diferencia");
//								errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString()));
//							}else{
//								//si la fecha ingresada es menor a la fecha minima de entrega y existe una autorizacion o la entidad responsable es local 
//								if(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)))>0){
//									LogSISPE.getLog().info("entra a cambiar la fecha minima de entrega");
//									LogSISPE.getLog().info("la fecha minima es menor pero hay autorizacion: {}" , formulario.getBuscaFecha());
//									session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
//									session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
//									warnings.add("fechaMinima",new ActionMessage("warnings.fechaMinima"));										
//								}
//							}
//							
//						}
//						//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
//						else{
//							//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//							errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
//							session.removeAttribute(VISTALOCALCOL);
//							formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
//						}
//					}
//					//si la fecha ingresada es mayor a la de hoy 
//					else{
//						//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//						errors.add("buscaFecha",new ActionMessage("errors.fechaMinima"));
//					}
//				}
//				//fin validacion de autorizaciones
//				
//				LogSISPE.getLog().info("Fecha min: {}" , fechaMinimaEntrega);
//				//Hago las validaciones de formulario de valores nulos y formatos
//				//if(fechaEntregaCliente!=null){//comentado porque aun no se tiene fechaEntregaCliente
//					if(formulario.getBuscaFecha()==null)
//						formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
//					//verifica si ingresaron una fecha menor a la fecha minima de entrega o existe una autorizacion, si la entidad responsable es el local tambien deja modificar la fecha minima de entrega sin autorizacion
//					if(ConverterUtil.returnDateDiff(fechaMinimaEntrega,(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA))) <= 0.0 
//							|| AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))!=null 
//							|| (String)session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null){
//							if(fechaEntregaCliente!=null && !fechaEntregaCliente.isEmpty()){
//								//Diferencia entre fecha de entrega y fecha minima de entrega
//								long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(fechaMinimaEntrega, fechaEntregaCliente);
//								if(diferenciaEntregaBusca<0.0){
//									LogSISPE.getLog().info("error en diferencia");
//									errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",fechaMinimaEntrega));
//								}else{
//									LogSISPE.getLog().info("----calendarioConfiguracionDiaLocal--- {}" , session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX));
//									if(session.getAttribute(EXISTELUGARENTREGA)!=null && formulario.validarCantidades(error, request)==0 && formulario.getHoras()!=null && formulario.getMinutos()!=null){
//										Collection detallePedidoDTOCol=(Collection)session.getAttribute(DETALLELPEDIDOAUX);
//										if(validarCapacidadBodega(request,session, errors, formulario,detallePedidoDTOCol)){//validar la bodega de canastos cuando la configuracion es bodCanastos,otroLocal,pedirCD 
//											//se crea el convertidor de fechas
//											convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
//											
//											Boolean validarCantidadCanastos = Boolean.TRUE;
//											String tipoEntregaSesion= (String)session.getAttribute(CotizarReservarAction.TIPO_ENTREGA);
//											//Obtener el parametro para saber el valor del minimo de canastas que se pueden agregar en las entregas a domicilio
//											ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
//											Integer codMinCanastosDomicilio = Integer.parseInt(parametroMinCanastas.getValorParametro());
//											//Obtener el parametro para saber el monto minimo de una entrega a domicilio
//											ParametroDTO paramMontoMinEntregaDomicioCD = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
//											Double montoMinEntregaDomicioCD = Double.valueOf(paramMontoMinEntregaDomicioCD.getValorParametro());
//											
//											LogSISPE.getLog().info("tipoEntregaSesion: {}",tipoEntregaSesion);
//											//LogSISPE.getLog().info("Responsable: {}",formulario.getOpLocalResponsable());
//											String codCanastosCatalogo = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request)).getValorParametro();
//											String codCanastosEspeciales = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request)).getValorParametro();
//											//validar que sean solo 50 las entregas a domicilio
//				//							if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local")) && 
//				//									formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//											if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//												
//												long cantidadTotal = 0;
//												long cantidadTotalOtrosProductos = 0;
//												validarCantidadCanastos = Boolean.FALSE;
//												
//												Double totalEntregaTotalSeleccionada = 0D;//solo se llena cuando son entregas totales
//												
//												for(Iterator<DetallePedidoDTO> it = detallePedidoDTOCol.iterator();it.hasNext();){
//													DetallePedidoDTO detallePedidoDTO = it.next();
//													LogSISPE.getLog().info("getCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue());
//													LogSISPE.getLog().info("getNpCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
//													//validar que solo sean canastos de cat\u00E1logo o especiales
//													if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
//															|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
//														LogSISPE.getLog().info("valido cantidad {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
//														cantidadTotal = cantidadTotal + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue();
//													}
//													
//													totalEntregaTotalSeleccionada += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta();
//												}
//												LogSISPE.getLog().info("Cantidad total de canastos de cat\u00E1logo y especiales: {}", cantidadTotalOtrosProductos);
//												LogSISPE.getLog().info("Cantidad total de otros productos: {}", cantidadTotal);
//												if(cantidadTotal > 0 && cantidadTotal >= codMinCanastosDomicilio.longValue() || totalEntregaTotalSeleccionada > montoMinEntregaDomicioCD){
//													LogSISPE.getLog().info("no cumple con validaci\u00F3n de cantidad m\u00EDnima de canastas a domicilio");
//													// se verifica si tiene una autorizacion de bodega
//													if (AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, 
//															MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.montoMinimoEntregaDomicilioCD"))==null){
//														validarCantidadCanastos = Boolean.TRUE;
//													}
//												}
//												
//											}
//// wcaiza validar											
//											if(validarCantidadCanastos || (formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null 
//													&& formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
//													&&  formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) ){
////												obtengo el local destino
//												VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//												LogSISPE.getLog().info("local destino: {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
//												CalendarioDiaLocalDTO calendarioDiaLocalDTO=null;
//												if(session.getAttribute(CALENDARIODIALOCAL)!=null){
//													LogSISPE.getLog().info("si existe calendario");
//													//obtengo el dia seleccionado
//													calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
//												}
//												LogSISPE.getLog().info("****calendarioDiaLocalDTO**** {}",calendarioDiaLocalDTO);
//												//coleccion de costos de entrega de direcciones
//												Collection costoEntregasDTOCol = session.getAttribute(COSTOENTREGAAUX)!=null ? (Collection)session.getAttribute(COSTOENTREGAAUX) : new ArrayList();
//												//coleccion de direcciones de entrega
//												Collection direccionesDTOCol = session.getAttribute(DIRECCIONESAUX)!=null ? (Collection)session.getAttribute(DIRECCIONESAUX) : new ArrayList();
//												//Autorizacion
//												AutorizacionDTO autorizacionDTO = null;
//												
//												//Si existe autorizacion
//												if(session.getAttribute(AUTORIZACION)!=null)
//													autorizacionDTO=(AutorizacionDTO)session.getAttribute(AUTORIZACION);
//												
//												//session del costo total de la entrega a domicilio
////												double costoEntregaParcial = 0;
////												if(session.getAttribute(VALORTOTALENTREGAAUX)!=null){
////													costoEntregaParcial=((Double)session.getAttribute(VALORTOTALENTREGAAUX)).doubleValue();
////												}
////												LogSISPE.getLog().info("costoEntregaParcial: {}",costoEntregaParcial);
//												String tipoEntrega=null;//variable para saber si es entrega a locales o direcciones
//												int indexDetalles=0;//indice para las cantidades de los detalles
//												int numeroBultos=0;//variable para calcular el numero de bultos
//												int totalBultos=0;//total bultos de los detalles
//												double distancia=0;//distancia entre el local y la direccion
//												int detallesAgregados=0;//contador para saber cuantos detalles nuevos se agregaros
//												Double totalEntregaSeleccionada = 0d;
//												//******** transformo la hora ingresada a tipo Time **************
//												
//												LogSISPE.getLog().info("Horas: ----{}",formulario.getHoras());
//												LogSISPE.getLog().info("MInutos: --{}",formulario.getMinutos());
//												
//												GregorianCalendar fechaEntregaCompleta = new GregorianCalendar();
//												fechaEntregaCompleta.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
//												fechaEntregaCompleta.set(Calendar.HOUR_OF_DAY,Integer.parseInt(formulario.getHoras()));
//												fechaEntregaCompleta.set(Calendar.MINUTE,Integer.parseInt(formulario.getMinutos()));
//												
//												GregorianCalendar fechaSinHora = new GregorianCalendar();
//												fechaSinHora.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
//												GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
//												fechaSinHoraAux.add(Calendar.DAY_OF_MONTH, -1);
//												GregorianCalendar fechaActual = new GregorianCalendar();
//												fechaActual.setTime(new Date());
//												
//												LogSISPE.getLog().info("Fecha registro: ----{}",fechaActual.getTime());
//												
//												if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//													/***************Para entregas Locales***********************/
//													//se obtiene la clave para el tipo de entrega a un local
//													tipoEntrega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local");
//												}
//												else{
//													/***************Para entregas domicilio ***********************/
//													tipoEntrega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion");
//												}
//												LogSISPE.getLog().info("*****entro a agregar entregas desde la accion**********");
//												//Contador de direcciones
//												int secDirecciones=1;
//												if(session.getAttribute(SECDIRECCIONESAUX)!=null){
//													secDirecciones=((Integer)session.getAttribute(SECDIRECCIONESAUX)).intValue();
//												}
//												LogSISPE.getLog().info("secDirecciones:.- {}" , secDirecciones);
//				
//												//validar que no se asignen los pedidos el domingo cuando las entregas son a domicilio - quito o guayaquil
//												//Boolean entregasDomicilioQuitoGuayaquil=Boolean.FALSE;
//												Boolean entregasDomicilio=Boolean.FALSE;
//												Boolean errorDespachoDomingo=Boolean.TRUE;
//												String ciudadSeleccionada=(String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO);
//												GregorianCalendar fechaSinHoraAux1 = null;
//												if(((String)session.getAttribute(ENTIDADRESPONSABLELOCAL))==null && ((String)session.getAttribute(LUGARENTREGA)).toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//													//Inicio- Calculo nuevo para la fecha de despacho
//													//En las entregas a domicilio para guayaquil la fecha de despacho debe ser dos d\u00EDas antes de la fecha de entrega
//													entregasDomicilio=Boolean.TRUE;
//													String seleccionaCiudad=(String)session.getAttribute("otraCuidad");
//													String []ciudadesDomicilio=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.ciudadesDomicilio").split(",");
//													LogSISPE.getLog().info("Entrega a Domicilio? {}",seleccionaCiudad);
//													LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",ciudadSeleccionada);
//													LogSISPE.getLog().info("Codigos cuidades {}",ciudadesDomicilio[0]+"-"+ciudadesDomicilio[1]);
//													Integer nDiasAntes=0;
//													if(seleccionaCiudad!=null && ciudadSeleccionada!=null){
//														if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[0])){
//															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.guayaquil", request);
//															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//															LogSISPE.getLog().info("diasDespachoGuayaquil {}",nDiasAntes);
//															//entregasDomicilioQuitoGuayaquil=Boolean.TRUE;
//														}else
//														if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[1])){
//															if(verificarCanastasCatalogo(request)){
//																LogSISPE.getLog().info("Si contiene canastos de catalogo");
//																ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.quito", request);
//																nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//																LogSISPE.getLog().info("diasDespachoQuito {}",nDiasAntes);
//																//entregasDomicilioQuitoGuayaquil=Boolean.TRUE;
//															}else{
//																LogSISPE.getLog().info("No contiene canastos de catalogo se procede normalmente");
//																ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//																nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//																LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
//															}
//														}else{
//															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//															LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
//														}
//													}else{
//														LogSISPE.getLog().info("debe seleccionar una ciudad y existir los par\u00E1metros");
//														ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//														nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//													}
//													
//													GregorianCalendar fechaSinHora1 = new GregorianCalendar();
//													fechaSinHora1.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
//													fechaSinHoraAux1 = (GregorianCalendar)fechaSinHora1.clone();
//													fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -nDiasAntes);
//													//verificar que la fecha restada no sea menor a hoy
//													Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
//													GregorianCalendar fechaActua = new GregorianCalendar();
//													fechaActua.setTime(fechaMinimaReferencia);
//														
//													if(ConverterUtil.returnDateDiff(fechaActua,fechaSinHoraAux1) < 0.0){
//														errors.add("fechaMinimaDespacho",new ActionMessage("errors.fechaMinima.despacho"));
//													}else{
//														//if(entregasDomicilioQuitoGuayaquil){
//															//verificar si la fecha seleccionada es domingo
//															if(obtenerDia(fechaSinHoraAux1).equals(MessagesWebSISPE.getString("constante.dia.semana"))){
//																LogSISPE.getLog().info("La fecha de despacho de la bodega va ha ser un dia Domingo");
//																//validar si se puede o no despachar los domingos
//																String[] fechasDomingoPermitidas;
//																ParametroDTO parametroBusqueda = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.fechasDomingoDespachoCD", request);
//																fechasDomingoPermitidas = parametroBusqueda.getValorParametro().split(",");
//																LogSISPE.getLog().info("Domingos permitidos {}",parametroBusqueda.getValorParametro());
//																//verifica las excepciones de la fecha restada
//																for(String diaExc : fechasDomingoPermitidas){
//																	LogSISPE.getLog().info("::::::**** calendarioDia.mes: {} ; calendarioDia.dia: {}; ",fechaSinHoraAux1.get(Calendar.MONTH),  fechaSinHoraAux1.get(Calendar.DAY_OF_MONTH) );
//																	LogSISPE.getLog().info("::::::**** diaExc.mes: {} ; diaExc.dia: {}; ",Integer.parseInt(diaExc.split("-")[0]),Integer.parseInt(diaExc.split("-")[1]));
//																	if(fechaSinHoraAux1.get(Calendar.MONTH) == (Integer.parseInt(diaExc.split("-")[0])-1) && fechaSinHoraAux1.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(diaExc.split("-")[1])){
//																		errorDespachoDomingo=Boolean.FALSE;
//																		break;
//																	}
//																}
//																
//																//Si el domingo habilitado para despachar no est\u00E1 activo, entonces la fecha de despacho ser\u00E1 el sabado anterior
//																if(errorDespachoDomingo){
//																	//a la fecha de despacho le restamos un dia para que sea s\u00E1bado
//																	fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -1);
//																	LogSISPE.getLog().info("La fecha de despacho de la bodega va ha ser el dia: "+obtenerDia(fechaSinHoraAux1) +" fecha:"+new Timestamp(fechaSinHoraAux1.getTime().getTime()));
//																	errorDespachoDomingo=Boolean.FALSE;
//																}
//															}else{
//																errorDespachoDomingo=Boolean.FALSE;
//															}
//				//										}else{
//				//											errorDespachoDomingo=Boolean.FALSE;
//				//										}
//													}
//														
//												}else{
//													errorDespachoDomingo=Boolean.FALSE;
//												}
//												if((entregasDomicilio && ciudadSeleccionada==null) && (entregasDomicilio && ((formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&  !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))))) ){
//													//WA
//													LogSISPE.getLog().info("Ciudad seleccionada en null");
//													errors.add("despachosDomingo",new ActionMessage("error.validacion.ciudad.vacia"));
//												}else{S88
//													if(!errorDespachoDomingo){
//														//Ingreso de nuevos detalles
//														for(Iterator<DetallePedidoDTO> it = detallePedidoDTOCol.iterator();it.hasNext();){ 
//															DetallePedidoDTO detallePedidoDTO = it.next();
//															boolean condicion = false;//condicion para ingreso de nuevos costos
//															boolean entregaConDomicilioExistente = false;//condicion para ingreso de nuevas direcciones
//															Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
//															//LogSISPE.getLog().info("-------codArticulo " + detallePedidoDTO.getId().getCodigoArticulo() +" CantidadEstado-------- " + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//															LogSISPE.getLog().info(new StringBuilder().append("-------codArticulo ").append(detallePedidoDTO.getId().getCodigoArticulo())
//																	.append(" CantidadEstado-------- ").append(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()).toString());
//															
//															if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()>0){
//																LogSISPE.getLog().info("cantidadEstado[{}]: {}" ,indexDetalles, detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//																//agrego un detalle del pedido
//																Collection entregas = detallePedidoDTO.getEntregas()!=null ? detallePedidoDTO.getEntregas() : new ArrayList();
//																//Nueva entrega
//																EntregaDTO entregaDTO1 = new EntregaDTO();
//																
//																//entregaDTO1.setCodigoCiudadSectorEntrega(valorCodigoCiudadSector);ok cab
//																entregaDTO1.setNpValorCodigoTransito(valorCodigoTransito);
//																//entregaDTO1.setCantidadParcialDespacho(0L);ok det
//																//entregaDTO1.getId().setCodigoArticulo(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());//ok det
//																//entregaDTO1.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());//ok cab y det
//																//entregaDTO1.setCantidadEntrega(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());//ok det
//																entregaDTO1.setFechaEntregaCliente(new Timestamp(fechaEntregaCompleta.getTimeInMillis()));//ok
//																//entregaDTO1.setNpCantidadEntrega(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());ok det
//																//entregaDTO1.setTipoEntrega(tipoEntrega);ok cab
//																entregaDTO1.setNpContadorBeneficiario(0L);
//																//Seteo la configuracion utilizada para la entrega
//																//entregaDTO1.setCodigoAlcanceEntrega(Integer.valueOf((String)(session.getAttribute(TIPOENTREGA))));ok cab
//																//entregaDTO1.setCodigoContextoEntrega(Integer.valueOf((String)(session.getAttribute(LUGARENTREGA))));ok cab
//																//entregaDTO1.setCodigoObtenerStock(Integer.valueOf((String)(session.getAttribute(STOCKENTREGA))));ok cab
//																//entregaDTO1.setNpCantidadBultos(0);ok det
//																//Asigna la autorizacion si la tiene
//																if (autorizacionDTO!=null){
//																	entregaDTO1.setAutorizacionDTO(autorizacionDTO);
//																}
//																
//																//Cuando se realizan despachos de bodega
//																if(calendarioDiaLocalDTO!=null){
//																	LogSISPE.getLog().info("tiene pedido a bodega");
//																	LogSISPE.getLog().info("**CantidadReservarSIC:** {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
//																	entregaDTO1.setCalendarioDiaLocalDTO(calendarioDiaLocalDTO);
//																	
//						//											if(session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && session.getAttribute(LUGARENTREGA).toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//						//												//entregaDTO1.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux.getTime().getTime()));
//						//												//Inicio- Calculo nuevo para la fecha de despacho
//						//												//En las entregas a domicilio para guayaquil la fecha de despacho debe ser dos d\u00EDas antes de la fecha de entrega
//						//												String seleccionaCiudad=(String)session.getAttribute("otraCuidad");
//						//												String ciudadSeleccionada=(String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO);
//						//												String []ciudadesDomicilio=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.ciudadesDomicilio").split(",");
//						//												LogSISPE.getLog().info("Entrega a Domicilio? {}",seleccionaCiudad);
//						//												LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",ciudadSeleccionada);
//						//												LogSISPE.getLog().info("Codigos cuidades {}",ciudadesDomicilio[0]+"-"+ciudadesDomicilio[1]);
//						//												Integer nDiasAntes=0;
//						//												if(seleccionaCiudad!=null && ciudadSeleccionada!=null){
//						//													if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[0])){
//						//														ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.guayaquil", request);
//						//														nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//						//														LogSISPE.getLog().info("diasDespachoGuayaquil {}",nDiasAntes);
//						//													}else if(seleccionaCiudad.equals("ok") && ciudadSeleccionada.equals(ciudadesDomicilio[1])){
//						//														if(verificarCanastasCatalogo(request)){
//						//															LogSISPE.getLog().info("Si contiene canastos de catalogo");
//						//															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega.quito", request);
//						//															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//						//															LogSISPE.getLog().info("diasDespachoQuito {}",nDiasAntes);
//						//														}else{
//						//															LogSISPE.getLog().info("No contiene canastos de catalogo se procede normalmente");
//						//															ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//						//															nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//						//															LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
//						//														}
//						//													}else{
//						//														ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//						//														nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//						//														LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
//						//													}
//						//												}else{
//						//													LogSISPE.getLog().info("debe seleccionar una ciudad y existir los par\u00E1metros");
//						//													ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
//						//													nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
//						//												}
//						//												
//						//												GregorianCalendar fechaSinHora1 = new GregorianCalendar();
//						//												fechaSinHora1.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
//						//												
//						//												GregorianCalendar fechaSinHoraAux1 = (GregorianCalendar)fechaSinHora1.clone();
//						//												fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -nDiasAntes);
//						//												entregaDTO1.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux1.getTime().getTime()));
//						//												//Fin
//						//												entregaDTO1.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//						//												entregaDTO1.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//						//											}
//																	if(entregasDomicilio){
//																		entregaDTO1.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux1.getTime().getTime()));
//																		entregaDTO1.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//																		entregaDTO1.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//																	}else{
//																		if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//																			entregaDTO1.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux.getTime().getTime()));
//																			entregaDTO1.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//																			entregaDTO1.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
//																		}else{
//																			entregaDTO1.setFechaDespachoBodega((Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
//																			entregaDTO1.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
//																			entregaDTO1.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
//																			if(entregaDTO1.getCantidadDespacho().longValue()==0){
//																				entregaDTO1.setFechaRegistroDespacho(entregaDTO1.getFechaEntregaCliente());
//																			}
//																		}
//																	}
//																	
//																	if(detallePedidoDTO.getNpContadorDespacho()==null)
//																		detallePedidoDTO.setNpContadorDespacho(0L);
//																	detallePedidoDTO.setNpContadorDespacho(Long.valueOf(detallePedidoDTO.getNpContadorDespacho().longValue()+entregaDTO1.getNpCantidadDespacho().longValue()));
//																	//Si el despacho es parcial desde bodega 
//																	if(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))
//																		detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
//																	else
//																		detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorDespacho().longValue()));
//																	LogSISPE.getLog().info("***CantidadReservarSIC:*** {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
//																	if(entregaDTO1.getCantidadDespacho().longValue()>0){
//																		//calculo los bultos
//																		numeroBultos = UtilesSISPE.calcularCantidadBultos(entregaDTO1.getCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
//																		entregaDTO1.setNpCantidadBultos(numeroBultos);
//																	}
//																	
//																	//OANDINO: Verificar si la cantidad de despacho es mayor a cero, seg\u00FAn esto mostrar o no checkboxs de transito ---------
//																	LogSISPE.getLog().info("Cant. Despacho: {}",entregaDTO1.getCantidadDespacho());
//																											
//																	if((!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica")) && entregaDTO1.getCantidadDespacho().longValue()>0){																				
//																		entregaDTO1.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//																		//Se suman las cantidades pedidas a bodega
//																		/*if(!verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) 
//																				&& entregaDTO1.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
//																				){
//																			sumaCantSolCD = sumaCantSolCD + entregaDTO1.getCantidadDespacho();
//																			LogSISPE.getLog().info("**(1)Sumatoria de articulos solicitados al CD: {}", sumaCantSolCD);
//																		}*/
//																	}else{
//																		entregaDTO1.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
//																	}
//																	//------------------------------------------------------------------------------------------------------------
//																}
//																//si no existe despacho a bodega se setea la fechaDespachoBodega porq es obligatorio
//																else{
//																	entregaDTO1.setFechaDespachoBodega(new Timestamp(fechaSinHora.getTime().getTime()));
//																	//Si la entidad responsable es la bodega y la entrega es a domicilio 
//																	//se asigna la fechaRegistroDespacho para que la bodega no despache el registro
//																	entregaDTO1.setFechaRegistroDespacho(new Timestamp(fechaActual.getTime().getTime()));
//																	entregaDTO1.setNpCantidadDespacho(0L);
//																	entregaDTO1.setCantidadDespacho(0L);
//																}
//																	
//																//Entregas a Locales
//																if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//																	LogSISPE.getLog().info("Entrega a Locales...");
//																	//validar si las entregas es en el local  y existen canastos especiales, entonces el calendario mostrado es de la 99, pero la direccion de entrega y lugar de entrega es el local
//																	if(vistaLocalDestinoDTO.getId().getCodigoLocal().intValue()!= Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito")).intValue() && vistaLocalDestinoDTO.getId().getCodigoLocal().intValue()!=Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos")).intValue()){
//																		entregaDTO1.setCodigoLocalEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal());
//																		entregaDTO1.setDireccionEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.prefijoLocal")+": "+vistaLocalDestinoDTO.getId().getCodigoLocal()+ " - " + vistaLocalDestinoDTO.getNombreLocal());
//																	}
//																	else{
//																		entregaDTO1.setCodigoLocalEntrega(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
//																		entregaDTO1.setDireccionEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.prefijoLocal")+": "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal()+ " - " + SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
//																	}
//																}
//																//Entregas a domicilio
//																else{
//																	LogSISPE.getLog().info("Entrega a Domicilio...");
//																	
//																	totalEntregaParcialSeleccionada = valorEntregaArticuloBultos(detallePedidoDTO);	
//																	LogSISPE.getLog().info("Valor entrega parcial {} ", totalEntregaParcialSeleccionada);
//																	entregaDTO1.setCostoParcialEntrega(totalEntregaParcialSeleccionada);
//																	totalEntregaSeleccionada += totalEntregaParcialSeleccionada;
//																	
//																	//OANDINO: Se valida el valor para presentar o no el checkbox de local de tr\u00E1nsito --------------------------------------------------
//																	if((!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica")) && entregaDTO1.getCantidadDespacho().longValue()>0){																				
//																		entregaDTO1.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//																		//Se suman las cantidades pedidas a bodega
//																		if(!verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
//																			sumaCantSolCD = sumaCantSolCD + entregaDTO1.getCantidadDespacho();
//																			LogSISPE.getLog().info("**(2)Sumatoria de articulos solicitados al CD: {}", sumaCantSolCD);
//																		}
//																	}else{
//																		entregaDTO1.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
//																	}
//																	//-----------------------------------------------------------------------------------------------------------------------------------																		
//																	
//																	//Si existe un local intermedio para la entrega a domicilio
//																	if(session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null){
//																		entregaDTO1.setCodigoLocalSector(vistaLocalDestinoDTO.getId().getCodigoLocal());
//																		LogSISPE.getLog().info("codigo sector entrega: {}" , entregaDTO1.getCodigoLocalSector());
//																		if(session.getAttribute(DIRECCION)==null)
//																			entregaDTO1.setDireccionEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal()+ ": - " + formulario.getDireccion());
//																		else{
//																			DireccionesDTO direccionSeleccionada = (DireccionesDTO)session.getAttribute(DIRECCION);
//																			entregaDTO1.setDireccionEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal()+ ": - " + direccionSeleccionada.getDescripcion());
//																		}
//																	}else{
//																		entregaDTO1.setDireccionEntrega(formulario.getDireccion());
//																		entregaDTO1.setCodigoLocalSector(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
//																		entregaDTO1.setCodigoSectorEntrega((String)session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO));
//																	}
//																	//Calculo el costo
//																	//Si la distancia fue ingresada en tiempo
//																	if(formulario.getUnidadTiempo().equals("H")){
//																		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.factorConversionEntregasDomicilio", request);
//																		int prametroConversionAKilometros=(new Integer(parametroDTO.getValorParametro())).intValue();
//																		//transformo la distancia de tiempo a kilometros
//																		long hora = (Long.valueOf(formulario.getDistanciaH())).longValue()*60;
//																		long minutos = (Long.valueOf(formulario.getDistanciaM())).longValue();
//																		double totalMinutos = hora + minutos;
//																		distancia = totalMinutos * prametroConversionAKilometros;
//																		//session.getAttribute(STOCKENTREGA)
//																		entregaDTO1.setCostoEntrega(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(Double.valueOf(distancia), request, totalEntregaParcialSeleccionada, 
//																				session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
//																		LogSISPE.getLog().info("costo H****: {}" , entregaDTO1.getCostoEntrega());
//																		entregaDTO1.setDistanciaEntrega(distancia);
//																	}
//																	//Si la distancia fue ingresada en kilometros
//																	else{
//																		entregaDTO1.setCostoEntrega(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(Double.valueOf(formulario.getDistancia()),request, totalEntregaParcialSeleccionada, 
//																				session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
//																		distancia = (Double.valueOf(formulario.getDistancia())).doubleValue();
//																		entregaDTO1.setDistanciaEntrega(distancia);
//																		LogSISPE.getLog().info("costo K****: {}" , entregaDTO1.getCostoEntrega());
//																	}
//																	
//																	if(session.getAttribute(DIRECCION)!=null){
//																		//Si selecciono una de las direcciones ingresadas anteriormente
//																		DireccionesDTO direccionesDTO2 = (DireccionesDTO)session.getAttribute(DIRECCION);
//																		LogSISPE.getLog().info("codigo direccion: {}" , direccionesDTO2.getCodigoDireccion());
//																		entregaDTO1.setNpDireccionEntregaDomicilio(direccionesDTO2.getCodigoDireccion());
//																	}else{
//																		//Si es una nueva direccion le asigna el secuencial de direcciones
//																		entregaDTO1.setNpDireccionEntregaDomicilio(Integer.valueOf(secDirecciones).toString());
//																	}
//																	
//																	LogSISPE.getLog().info("dir registrada: {}", entregaDTO1.getNpDireccionEntregaDomicilio());
//																	
//																	/*******************Comprueba si la direccion ya ha sido ingresada al menos una vez*********/
//																	if(direccionesDTOCol.size()>0){q
//																		for (Iterator<DireccionesDTO> iter = direccionesDTOCol.iterator(); iter.hasNext();) {
//																			DireccionesDTO direccionesDTO = iter.next();
//																			LogSISPE.getLog().info("NpDireccionEntregaDomicilio: {}" , entregaDTO1.getNpDireccionEntregaDomicilio());
//																			LogSISPE.getLog().info("codigoDireccion: {}" , direccionesDTO.getCodigoDireccion());
//																			if(entregaDTO1.getNpDireccionEntregaDomicilio().equals(direccionesDTO.getCodigoDireccion())){
//																				LogSISPE.getLog().info("no hay nueva direccion");
//																				entregaConDomicilioExistente = true;
//																				//Si existe ya la direccion incrementa su contador
//																				direccionesDTO.setNumeroDireccion(direccionesDTO.getNumeroDireccion()+1);
//																			}
//																		}
//																	}
//																	
//																	/*******************Comprueba si el costo ya ha sido ingresado al menos una vez*********/
//																	if(costoEntregasDTOCol.size()>0){
//																		for(Iterator costo = costoEntregasDTOCol.iterator();costo.hasNext();){
//																			CostoEntregasDTO costoEntregasDTO =(CostoEntregasDTO)costo.next();
//																			LogSISPE.getLog().info("costo.sector: {}" , costoEntregasDTO.getCodigoSector());
//																			LogSISPE.getLog().info("costo.fechaEntrega{}" , costoEntregasDTO.getFechaEntrega());
//																			LogSISPE.getLog().info("entrega.fechaEntrega{}" , entregaDTO1.getFechaEntregaCliente());
//																			LogSISPE.getLog().info("entrega.direccion: {}", entregaDTO1.getDireccionEntrega());
//																			if(entregaConDomicilioExistente && entregaDTO1.getDireccionEntrega().equals(costoEntregasDTO.getCodigoSector()) && costoEntregasDTO.getFechaEntrega().equals(entregaDTO1.getFechaEntregaCliente())){
//																				condicion = true;
//																				LogSISPE.getLog().info("no hay nuevo costo");
//																				//Si existe ya el costo incremente su contador
//																				costoEntregasDTO.setNumeroEntregas(costoEntregasDTO.getNumeroEntregas()+1);
//																				
//																				Double valorEntregaTotal = Double.valueOf(
//																						WebSISPEUtil.costoEntregaDistancia(distancia, request, totalEntregaSeleccionada, 
//																								session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))));
//																				
//																				costoEntregasDTO.setValor(valorEntregaTotal);
//
//																				//agregamos la entrega relacionada al costo
//																				costoEntregasDTO.getColEntregaDTOs().add(entregaDTO1);
//																				
//																				LogSISPE.getLog().info("costoEntrega.getNumeroEntregas: {}",costoEntregasDTO.getNumeroEntregas());
//																			}
//																		}
//																	}
//																	
//																	//si es la entrega es a una direcci\u00F3n no existente en otra entrega
//																	if(!entregaConDomicilioExistente){
//																		LogSISPE.getLog().info("Es la primera direccion");
//																		DireccionesDTO direccionesDTO1 = new DireccionesDTO();
//																		direccionesDTO1.setCodigoDireccion(entregaDTO1.getNpDireccionEntregaDomicilio());
//																		LogSISPE.getLog().info("codigoDir:  {}" , direccionesDTO1.getCodigoDireccion());
//																		direccionesDTO1.setDescripcion(formulario.getDireccion());
//																		direccionesDTO1.setFechaEntrega(entregaDTO1.getFechaEntregaCliente());
//																		direccionesDTO1.setCodigoSector(vistaLocalDestinoDTO.getId().getCodigoLocal().toString());
//																		direccionesDTO1.setDistanciaDireccion(Double.valueOf(distancia).toString());
//																		if(formulario.getDistanciaH()!=null && !formulario.getDistanciaH().equals(ConstantesGenerales.ESTADO_INACTIVO))
//																			direccionesDTO1.setHoras(formulario.getDistanciaH());
//																		if(formulario.getDistanciaH()!=null)
//																			direccionesDTO1.setMinutos(formulario.getDistanciaM());
//																		direccionesDTO1.setNumeroDireccion(1);
//																		direccionesDTOCol.add(direccionesDTO1);
//																		session.setAttribute(DIRECCIONESAUX, direccionesDTOCol);
//																	}
//										
//																	//se debe crear un nuevo costo
//																	if(!condicion){
//																		LogSISPE.getLog().info("se crea un nuevo costo");
//																		CostoEntregasDTO costoEntregasDTO1 = new CostoEntregasDTO();
//																		costoEntregasDTO1.setFechaEntrega(entregaDTO1.getFechaEntregaCliente());
//																		costoEntregasDTO1.setCodigoSector(entregaDTO1.getDireccionEntrega());
//																		LogSISPE.getLog().info("entregaDTO1.getCostoEntrega() {} ", entregaDTO1.getCostoEntrega());
//																		costoEntregasDTO1.setValor(entregaDTO1.getCostoEntrega());
//																		costoEntregasDTO1.setNumeroEntregas(1);
//																		costoEntregasDTO1.setDistancia(Double.valueOf(distancia).doubleValue());
//																		costoEntregasDTO1.setDescripcion(formulario.getDireccion());
//																		//agregamos la entrega relacionada al costo
//																		costoEntregasDTO1.setColEntregaDTOs(new ArrayList<EntregaDTO>());
//																		costoEntregasDTO1.getColEntregaDTOs().add(entregaDTO1);
//																		costoEntregasDTOCol.add(costoEntregasDTO1);
////																		costoEntregaParcial = costoEntregaParcial + costoEntregasDTO1.getValor().doubleValue();
////																		LogSISPE.getLog().info("costoEntregaParcial: {}",costoEntregaParcial);
//																		session.setAttribute(COSTOENTREGAAUX, costoEntregasDTOCol);
////																		session.setAttribute(VALORTOTALENTREGAAUX, Double.valueOf(costoEntregaParcial));
//																	}
//																	/*********************************************/
//																}
//																
//																//verifico si eligio como responsable de las canastas especiales a la Bod99
//																if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//																	entregaDTO1.setElaborarCanastosEspeciales(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//																}else if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
//																	entregaDTO1.setElaborarCanastosEspeciales(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//																}
//																
//																/*ESTRUCTURA ANTIGUA*/
//																//A\u00F1ado la entrega
//																entregas.add(entregaDTO1);
//																detallePedidoDTO.setEntregas(entregas);
//																detallePedidoDTO.setNpContadorEntrega(new Long(detallePedidoDTO.getNpContadorEntrega().longValue()+entregaDTO1.getNpCantidadEntrega().longValue()));
//																//Indico cual es la cantidad que falta por entregar
//																detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue()));
//																totalBultos=totalBultos+numeroBultos;//sumo los bultos recien a\u00F1adidos
//																LogSISPE.getLog().info("agrego un detalle");
//																detallesAgregados++;
//																//existieron cambios en las entregas
//																session.setAttribute(EXISTENCAMBIOS,"ok");
//															}
//															else
//																detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue());
//															indexDetalles++;
//														}
//														
//														if(session.getAttribute(HABILITARDIRECCION) != null && session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS)!=null){
//															//distribucion de los bultos en camiones
//															try{
//																CalendarioHoraLocalDTO calendarioHoraLocalDTO = (CalendarioHoraLocalDTO)request.getSession().getAttribute(CALENDARIO_HORA_LOCAL_SELECCIONADO);
//																if(calendarioHoraLocalDTO!=null){
//																	EntregaLocalCalendarioUtil.descontarBultosPorHora(request, calendarioHoraLocalDTO, totalBultos,  (LocalID)session.getAttribute(LOCALID), formulario, errors, warnings);
//																}
//															}
//															catch (Exception e) {
//																LogSISPE.getLog().info("Problemas al asignar el pedido a los camiones");
//																errors.add("asignacionPedidoCamiones",new ActionMessage("errors.asignar.pedido.camiones"));
//															}
//														}
//													
//														//fin for
//														//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
//														try {
//															EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOCol, request);
//														} catch (Exception e) {
//															// TODO: handle exception
//															LogSISPE.getLog().info("Problemas con los responsables de las entregas");
//															errors.add("responsablesEntrega",new ActionMessage("error.validacion.asignacion.responsables"));
//														}
//														
//														
//														//CotizacionReservacionUtil.asignarResponsablesEntregas(request,detallePedidoDTOCol);
//														//Valida si es obligatorio el paso por la bodega de tr\u00E1nsito
//														if(!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica") 
//																&& sumaCantSolCD >= (Long.parseLong((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO)))){
//															//recorre los detalles
//															for(DetallePedidoDTO detallePedidoDTO : (Collection<DetallePedidoDTO>)detallePedidoDTOCol){
//																//si no es un art\u00EDculo perecible
//																if(!verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
//																	//recorre las entregas que tenga configurado hasta ese momento
//																	HashMap<String,Long> articuloEncontrado= new HashMap<String, Long>();
//																	for(EntregaDTO entregaAux : (Collection<EntregaDTO>)detallePedidoDTO.getEntregas()){
//																		//si est\u00E1 habilitado el check de transito
//																		if(entregaAux.getNpValidarCheckTransito() != null && entregaAux.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
//																				&& entregaAux.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
//																				){
//																			if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
//																					|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
//																				entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//																				
//																			}else{
//																				
//																				if(articuloEncontrado.size()==0){
//																					articuloEncontrado.put(entregaAux.getId().getCodigoArticulo(),entregaAux.getCantidadEntrega().longValue());
//																					if(entregaAux.getCantidadEntrega().longValue() > 0 && entregaAux.getCantidadEntrega().longValue() >= codMinCanastosDomicilio.longValue()){
//																						if(entregaAux.getNpPasoObligatorioBodegaTransito()==null || entregaAux.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"))){
//																							entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//																						}
//																					}
//																				}
//																				else{
//																					Long cantidad=articuloEncontrado.get(entregaAux.getId().getCodigoArticulo());
//																					entregaAux.setNpPasoObligatorioBodegaTransito(null);
//																					if(cantidad==null){
//																						articuloEncontrado.put(entregaAux.getId().getCodigoArticulo(),entregaAux.getCantidadEntrega().longValue());
//																					}else{
//																						long cantidadaActual=entregaAux.getCantidadEntrega().longValue();
//																						long cantidadParcialTotal=cantidad.longValue()+cantidadaActual;
//																						articuloEncontrado.remove(entregaAux.getId().getCodigoArticulo());
//																						articuloEncontrado.put(entregaAux.getId().getCodigoArticulo(),cantidadParcialTotal);
//																						if(cantidadParcialTotal > 0 && cantidadParcialTotal >= codMinCanastosDomicilio.longValue()){
//																							Collection<EntregaDTO> entregasEncontradas=	ColeccionesUtil.simpleSearch("id.codigoArticulo", entregaAux.getId().getCodigoArticulo(), (Collection<EntregaDTO>)detallePedidoDTO.getEntregas());
//																							
//																							//indicar que es obligatorio el paso por la bodega de transito de guayaquil
//																							for(EntregaDTO entrega : entregasEncontradas){
//																								if(entrega.getNpPasoObligatorioBodegaTransito()==null || entrega.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"))){
//																									entrega.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//																								}
//																							}
//																						}
//																						else{
//																							entregaAux.setNpPasoObligatorioBodegaTransito(null);
//																						}
//																					}
//																				}
//																				
//				//																//entregaAux.getId().getCodigoArticulo()
//				//																if(detallePedidoDTO.getNpCantidadValidaBodTransito()){eerr
//				//																	//entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//				//																}else{
//				//																	entregaAux.setNpPasoObligatorioBodegaTransito(null);
//				//																}
//																			}
//																		}
//																	}
//																}
//															}
//														}
//														session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
//														
//														//Si existieron cambios en las entregas(Se a\u00F1adieron detalles nuevos)
//														if(detallesAgregados>0){
//															messages.add("agregaDetalles",new ActionMessage("messages.nuevoDetalle"));
//															
//															if(calendarioDiaLocalDTO!=null)
//																calendarioDiaLocalDTO.setNpEsSeleccionado(new Boolean(false));
//															//session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalDTO);
//															session.setAttribute(SECDIRECCIONESAUX, new Integer(secDirecciones+1));
//															if((session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && !session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) || (session.getAttribute(ENTIDADRESPONSABLELOCAL)==null && !session.getAttribute(LUGARENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
//																LogSISPE.getLog().info("elimina la sesion de dia seleccionado");
//																session.removeAttribute(DIASELECCIONADO);
//															}
//															session.removeAttribute(CALENDARIODIALOCAL);
//															session.removeAttribute(DIRECCION);
//															formulario.setDireccion(null);
//															formulario.setDirecciones(null);
//															formulario.setUnidadTiempo(null);
//															formulario.setDistancia(null);
//															formulario.setDistanciaH(null);
//															formulario.setDistanciaM(null);
//															formulario.setSeleccionCiudad("");
//															//..session.removeAttribute(DIASELECCIONADO);
//															LogSISPE.getLog().info("Existieron Cambios En La Reserva");
//															CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
//															
//															session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocal);
//															session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
//															session.setAttribute(EXISTENENTREGAS, "ok");
//															//Me aseguro que quede la ultima configuracion valida
//															formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
//															formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
//															formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
//															formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
//															//PASO UNO DE LA AYUDA
//															if(session.getAttribute("siDireccion")!=null){
//																session.removeAttribute("siDireccion");
//																session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//																
//																session.removeAttribute(DIASELECCIONADO);
//																//jmena valida el mensaje correspondiente a la bod.canastos
//				//												if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
//				//														formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//																if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//																	session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3"));
//																}else{
//																	if(session.getAttribute(COMBOSELECCIONCIUDAD)==null){
//																		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
//																	}
//																	else{
//																		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
//																	}
//																}
//															}
//															
//															session.removeAttribute(SessionManagerSISPE.POPUP);
//															// Objetos para construir los tabs, si fue modificacion de reserva
//															beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
//															session.removeAttribute(MOSTRAR_POPUPTAB);
//															session.removeAttribute(TABSELECCIONADONAVEGACION);
//															session.removeAttribute(HABILITARCANTIDADENTREGA);
//															session.removeAttribute(HABILITARCANTIDADRESERVA);
//															session.removeAttribute(MOSTRAROPCIONCANASTOSESPECIALES);
//															session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//															session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
//															session.removeAttribute(PASOSPOPUP);
//															session.removeAttribute( POSICIONDIVCONFENTREGAS);
//														}
//														//No existieron cambios
//														else if(messages.size()==0)
//															warnings.add("sinCambios",new ActionMessage("warnings.sinCambiosReserva"));
//														/***************Actualiza el calendario********************/
//														if(session.getAttribute(CALENDARIODIALOCALCOL)!=null){
//															//fecha minima
//															Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//															//fecha maxima
//															Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//															//obtengo el mes del dia que fue seleccionado para la entrega
//															Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//															obtenerCalendario(session,request,(LocalID)session.getAttribute(LOCALID),errors,mes,fechaMinima,fechaMaxima,formulario, true);
//															
//															//jmena validacion cal.bod una vez que acepta reset la fecha entrega cliente.
//				//											if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
//				//													formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//															if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//																formulario.setFechaEntregaCliente(null);
//																session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//																session.removeAttribute(COMBOSELECCIONCIUDAD);
//															}
//														}
//														
////														//se asigna la relacion entre la direccion de entrega y el costo
////														CotizarReservarForm.asignarCostoEntregaDireccion(request, formulario.getSubTotal(), Boolean.FALSE);
//														
//													}//WA
//													else{
//														if(errors.size()==0){ //para el caso dondo la fecha de despacho es menor a la fecha actual
//															LogSISPE.getLog().info("No cumple la condicion que para las entregas se despachen el domingo");	
//															errors.add("despachosDomingo",new ActionMessage("error.validacion.despacho.domicilio"));
//														}
//													}
//												}
//											}//JM
//											else{
//												LogSISPE.getLog().info("No cumple la condicion que para las entregas a domicilio minimo de canastas sea 50");
//												errors.add("minEntrega",new ActionMessage("error.validacion.cantidadMinima.entrega.domicilio",codMinCanastosDomicilio));
//												
//											}//wcaiza validar
//										}
//										else{
//											LogSISPE.getLog().info("No Existieron Cambios En La Reserva");
//											CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
//											session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocal);
//											session.removeAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX1);
//										}
//									}
//								}
//							
//							}else{
//								warnings.add("configuracionInicial",new ActionMessage("warnings.sinSeleccioneConfiguracionEntregas"));
//							}
//					}//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
//					else{
//						//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//						errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
//						session.removeAttribute(VISTALOCALCOL);
//					}
//					if(error.size() == 0 && errors.size() == 0 && warnings.size() == 0){
//						session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
//					}
//					
//					
////				}//comentado porque aun no se tiene fechaEntregaCliente
////				//No existieron cambios
////				else
////					warnings.add("configuracionInicial",new ActionMessage("warnings.sinSeleccioneConfiguracion"));
//			}
//			
//		}
//		
//		else if(request.getParameter("botonCalcularCostoEntregaDomicilio") != null){
//			
//			asignacionValoresFormulario(session, formulario);
//			Collection<CostoEntregasDTO> colCostoEntrega = null;
//			
//			Collection detallePedidoDTOCLoneCol = (Collection)SerializationUtils.clone((Serializable)(Collection)session.getAttribute(DETALLELPEDIDOAUX));
//			if (!CollectionUtils.isEmpty(detallePedidoDTOCLoneCol)){
////				String tipoEntregaSesion= (String)session.getAttribute(CotizarReservarAction.TIPO_ENTREGA);
//				
////				if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//				
//				if (formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null 
//					&& formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
//						&&  formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
//					
//					Double totalEntregaSeleccionada = 0d;
//					Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
//					Double distancia = 0d;
//					
//					colCostoEntrega = new ArrayList<CostoEntregasDTO>();
//					CostoEntregasDTO costoEntregasDTO = new CostoEntregasDTO();
//					
//					for(Iterator<DetallePedidoDTO> it = detallePedidoDTOCLoneCol.iterator();it.hasNext();){
//						
//						DetallePedidoDTO detallePedidoDTO = it.next();
//						LogSISPE.getLog().info("getCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue());
//						LogSISPE.getLog().info("getNpCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
//						
//						totalEntregaParcialSeleccionada = valorEntregaArticuloBultos(detallePedidoDTO);	
//						LogSISPE.getLog().info("Valor entrega parcial {} ", totalEntregaParcialSeleccionada);
//						
//						totalEntregaSeleccionada += totalEntregaParcialSeleccionada;
//						
//						//Calculo el costo
//						//Si la distancia fue ingresada en tiempo
//						if(formulario.getUnidadTiempo().equals("H")){
//							ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.factorConversionEntregasDomicilio", request);
//							int prametroConversionAKilometros=(new Integer(parametroDTO.getValorParametro())).intValue();
//							//transformo la distancia de tiempo a kilometros
//							long hora = (Long.valueOf(formulario.getDistanciaH())).longValue()*60;
//							long minutos = (Long.valueOf(formulario.getDistanciaM())).longValue();
//							double totalMinutos = hora + minutos;
//							distancia = totalMinutos * prametroConversionAKilometros;
//						}
//						//Si la distancia fue ingresada en kilometros
//						else{
//							distancia = Double.valueOf(formulario.getDistancia());
//						}
//					}
//					
//					costoEntregasDTO.setValor(Double.valueOf(
//							WebSISPEUtil.costoEntregaDistancia(distancia, request, totalEntregaSeleccionada,
//									session.getAttribute(STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
//					
//					colCostoEntrega.add(costoEntregasDTO);
//					formulario.setCostoEntregaDomicilio(calcularCostoFlete(request, colCostoEntrega, formulario));
//				}
//			}
//		}
//		
//		//Elimina un detalle
//		else if(request.getParameter("botonEliminarEntrega")!=null){
//			try{
//				
//				instanciarVentanaEliminarEntregas(request);
//				
////				LogSISPE.getLog().info("va a elminiar un detalle");
////				//Obtengo la configuracion actual de reserva a bodega
////				String tomarStock=(String)session.getAttribute(STOCKENTREGA);
////				//se utiliza para contar cuantos detalles se eliminaron
////				int contadorEliminados = 0;
////				formulario.mantenerValoresEntregas(request);
////				List<DetallePedidoDTO> detallePedidoDTOCol = null;
////				//obtengo el local
////				VistaLocalDTO vistaLocalDTOAUX =(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
////				
////				if(formulario.getSeleccionEntregas().length>0){
////					Collection<EntregaDTO> entregasAux = new ArrayList<EntregaDTO>();//almaceno las entregas de un mismo detalle que van a ser eliminadas
////					DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
////					int numEntrega=0;//indice de la entrega a eliminar
////					int numDetalle=0;//indice del detalle
////					long cantidadDespacho=0;
////					String[] parametros = formulario.getSeleccionEntregas()[0].split("-");
////					//guarda el indice del detalle para saber cuando ya se ha borrado todas la entregas de un mismo detalle
////					int numDetalleAnterior = Integer.parseInt(parametros[1]);
////					//obtiene la sumatoria
////					Long sumaCantSolCD = session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE) != null ? (Long)session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE): 0L;
////					
////					//recupero los datos del detalle del pedido
////					detallePedidoDTOCol = (List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
////					detallePedidoDTO = detallePedidoDTOCol.get(numDetalleAnterior);
////					
////					for(int i=0;i<formulario.getSeleccionEntregas().length;i++){
////						LogSISPE.getLog().info("SeleccionEntregas: {}" , formulario.getSeleccionEntregas()[i]);
////						parametros=formulario.getSeleccionEntregas()[i].split("-");
////						numEntrega = Integer.parseInt(parametros[0]);
////						numDetalle = Integer.parseInt(parametros[1]);
////						LogSISPE.getLog().info("numeroDetalleAnterior: {}" , numDetalleAnterior);
////						LogSISPE.getLog().info("numeroDetalle: {}" , numDetalle);
////						LogSISPE.getLog().info("numeroEntrega: {}" , numEntrega);
////						
////						if(numDetalleAnterior != numDetalle){
////							LogSISPE.getLog().info("va a elminar los detalles");
////							for (Iterator iter = entregasAux.iterator(); iter.hasNext();) {
////								EntregaDTO entrega = (EntregaDTO) iter.next();
////								//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
////								if(entrega.getNpValidarCheckTransito() != null && entrega.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
////										&& entrega.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
////										&& entrega.getNpPasoObligatorioBodegaTransito() != null && entrega.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
////									sumaCantSolCD = sumaCantSolCD - entrega.getCantidadDespacho();
////								}
////								detallePedidoDTO.getEntregas().remove(entrega);
////							}
////							//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
////							//solo en caso que la entrega sea parcial a bodega
////							LogSISPE.getLog().info("****tomarStock**** " + tomarStock);
////							if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
////								cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
////								LogSISPE.getLog().info("va a mantener el valor{}" , cantidadDespacho);
////								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
////							}
////							cantidadDespacho = 0;
////							entregasAux = new ArrayList<EntregaDTO>();
////							numDetalleAnterior = numDetalle;
////							detallePedidoDTO = detallePedidoDTOCol.get(numDetalle);
////							//se cuentan los detalles eliminados
////							contadorEliminados++;
////						}
////						
////						LogSISPE.getLog().info("articulo detalle: {}" , detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
////						List entregas=(List)detallePedidoDTO.getEntregas();
////						EntregaDTO entregaDTO = (EntregaDTO)entregas.get(numEntrega);
////						if(entregaDTO.getCantidadDespacho().longValue()>0)
////							cantidadDespacho = cantidadDespacho + entregaDTO.getCantidadDespacho().longValue();
////						detallePedidoDTO.setNpContadorEntrega(Long.valueOf(detallePedidoDTO.getNpContadorEntrega().longValue() - entregaDTO.getCantidadEntrega().longValue()));
////						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue() + entregaDTO.getCantidadEntrega().longValue()));
////						LogSISPE.getLog().info("cantidad detalle: {}" , entregaDTO.getCantidadEntrega());
////						
////						//se elimina costo y direccion de envio en caso de entregas a domicilio
////						formulario.eliminaCostoEntrega(entregaDTO,session);
////						
////						//Si existio pedido a bodega dentro de la entrega
////						if(entregaDTO.getCalendarioDiaLocalDTO()!=null){
////							detallePedidoDTO.setNpContadorDespacho(Long.valueOf(detallePedidoDTO.getNpContadorDespacho().longValue()-entregaDTO.getCantidadDespacho().longValue()));
////							//Si el despacho es parcial desde bodega 
////							if(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA)!=null){
////								if(!session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))
////									/*detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
////								else*/
////									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()+entregaDTO.getCantidadDespacho().longValue()));
////							}else
////								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0l);
////							LogSISPE.getLog().info("dial local: {}" , entregaDTO.getCalendarioDiaLocalDTO().getId().getFechaCalendarioDia());
////							/*************************Modifico la CD del local eliminando la entrega************************/
////							// Parametros para buscar la capacidad del local en la fecha
////							CalendarioDiaLocalID calendarioDiaLocalID = new CalendarioDiaLocalID();
////							calendarioDiaLocalID.setCodigoCompania(entregaDTO.getId().getCodigoCompania());
////							if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
////								LogSISPE.getLog().info("local a eliminar1: {}" , entregaDTO.getCodigoLocalEntrega());
////								calendarioDiaLocalID.setCodigoLocal(entregaDTO.getCodigoLocalEntrega());
////							}
////							else{
////								LogSISPE.getLog().info("local a eliminar2: {}" , entregaDTO.getCodigoLocalSector());
////								calendarioDiaLocalID.setCodigoLocal(new Integer(entregaDTO.getCodigoLocalSector()));
////							}
////							Date mes=DateManager.getYMDDateFormat().parse(entregaDTO.getFechaDespachoBodega().toString());
////							calendarioDiaLocalID.setFechaCalendarioDia(mes);
////							CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = new CalendarioConfiguracionDiaLocalDTO();
////							if(session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX)!=null)
////								calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
////							LogSISPE.getLog().info("EntregaLocal - BuscarDiaPorID");
////							CalendarioDiaLocalDTO calendarioDiaLocalDTO2=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
////							/**************actualizo la capacidad disponible en el local para el despacho***********/
////							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2: {}" , calendarioDiaLocalDTO2);
////							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Compania: {}" , calendarioDiaLocalDTO2.getId().getCodigoCompania());
////							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Local: {}" , calendarioDiaLocalDTO2.getId().getCodigoLocal());
////							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Fecha: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
////							LogSISPE.getLog().info("EntregaLocal - entregaDTO.np: {}" , entregaDTO.getNpCantidadBultos());
////							LogSISPE.getLog().info("capacidad disponible2: {}" , calendarioDiaLocalDTO2.getCantidadDisponible());
////							LogSISPE.getLog().info("CantidadAcumulada: {}" , calendarioDiaLocalDTO2.getCantidadAcumulada());
////							LogSISPE.getLog().info("CapacidadAdicional: {}" , calendarioDiaLocalDTO2.getCapacidadAdicional());
////							LogSISPE.getLog().info("CapacidadNormal(): {}" , calendarioDiaLocalDTO2.getCapacidadNormal());
////							
////							//cantidades informativas
////							Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
////							//cantidad para frio
////							CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
////							cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
////							cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
////							cantidadesInformativas.add(cantidadFrio);
////							//cantidad para seco
////							CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
////							cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
////							cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
////							cantidadesInformativas.add(cantidadSeco);
////							
////							//veo si existe autorizacion para ese dia
////							AutorizacionDTO autorizacionDTO = formulario.buscaAutorizacion(session, calendarioDiaLocalID.getCodigoLocal().toString(), entregaDTO.getFechaDespachoBodega());
////							if(autorizacionDTO!=null)
////								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),true, cantidadesInformativas);
////							else
////								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),false, cantidadesInformativas);
////							/**********Va a modificar la cantidad acumulada***********************/
////							LocalID localID=new LocalID();
////							localID.setCodigoCompania(calendarioDiaLocalDTO2.getId().getCodigoCompania());
////							localID.setCodigoLocal(calendarioDiaLocalDTO2.getId().getCodigoLocal());
////							obtenerCalendario(session, request, localID, errors, calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), null, null, formulario, true);
////							Object[] calendarioDiaLocalDTOOBJ=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
////							int dia=-1;
////							//Busco el dia de despacho
////							LogSISPE.getLog().info("***********Va a buscar el dia de despacho*******************");
////							for(int indice=0;indice<calendarioDiaLocalDTOOBJ.length;indice++){
////								CalendarioDiaLocalDTO calDia=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indice];
////								if(calDia.getId().getFechaCalendarioDia().equals(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia())){
////									dia=indice;
////									break;
////								}
////							
////							}
////							LogSISPE.getLog().info("dia: {}" , dia);
////							LogSISPE.getLog().info("fecha de despacho: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
////							LogSISPE.getLog().info("fecha de entrega: {}" , entregaDTO.getFechaEntregaCliente());
////							cargaDiasModificarCA(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), entregaDTO.getFechaEntregaCliente(), formulario, request, dia);
////							//Obtengo los dias que debo modificar su capacidad acumulada
////							
////							Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIODIALOCALCOLAUX);
////							for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
////								LogSISPE.getLog().info("%%%%%%%%%%%%%%%%VA A RESTABLECER LOS VALORES EN EL CALENDARIO%%%%%%%%%%%%%");
////								LogSISPE.getLog().info("compania: {}" , calendarioDiaLocalDTOAux.getId().getCodigoCompania());
////								LogSISPE.getLog().info("local: {}" , calendarioDiaLocalDTOAux.getId().getCodigoLocal());
////								
////								//cantidades informativas
////								Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
////								//cantidad para frio
////								CantidadCalendarioDiaLocalDTO cantidadFrioAux = new CantidadCalendarioDiaLocalDTO();
////								cantidadFrioAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
////								cantidadFrioAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
////								cantidadesInformativasAux.add(cantidadFrioAux);
////								//cantidad para seco
////								CantidadCalendarioDiaLocalDTO cantidadSecoAux = new CantidadCalendarioDiaLocalDTO();
////								cantidadSecoAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
////								cantidadSecoAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
////								cantidadesInformativasAux.add(cantidadSeco);
////								
////								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), calendarioDiaLocalDTOAux.getCapacidadAdicional(), new Double(0), new Double(-entregaDTO.getNpCantidadBultos().longValue()),false,cantidadesInformativasAux);
////								LogSISPE.getLog().info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
////							}
////							session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalDTO);
////							/***************************************************************************************/
////			
////							/***********************************************************************************************/
////							
////						}
////						entregasAux.add(entregaDTO);
////						//..detallePedidoDTO.getEntregas().remove(entregaDTO);
////						//seteo la variable de sesion para saber que existieron cambios
////						session.setAttribute(EXISTENCAMBIOS,"ok");
////						//ordena la coleccion de detalles
////						//..ordenarDetalleEntregas(session);
////					}
////					
////					if(entregasAux.size()>0){
////						LogSISPE.getLog().info("elimina las entregas:");
////						
////						for (Iterator iter = entregasAux.iterator(); iter.hasNext();) {
////							EntregaDTO entrega = (EntregaDTO) iter.next();							
////							//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
////							if(entrega.getNpValidarCheckTransito() != null && entrega.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))  
////									&& entrega.getNpPasoObligatorioBodegaTransito() != null && entrega.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
////									&& entrega.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
////									){
////								sumaCantSolCD = sumaCantSolCD - entrega.getCantidadDespacho();
////							}
////							detallePedidoDTO.getEntregas().remove(entrega);
////						}
////						detallePedidoDTO= detallePedidoDTOCol.get(numDetalle);
////						session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
////						
////						//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
////						//solo en caso que la entrega sea parcial a bodega
////						LogSISPE.getLog().info("cantidadDespacho: {}" , cantidadDespacho);
////						LogSISPE.getLog().info("tomarStock: {}" , tomarStock);
////						if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
////							LogSISPE.getLog().info("va a mantener la cantidad");
////							cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
////							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
////						
////						}
////						//detallePedidoDTO.getEntregas().remove(entCol);
////						LogSISPE.getLog().info("numero de entregas del detalle: {}" , detallePedidoDTO.getEntregas().size());
////					}
////					
////					//valido obligatoriedad de tr\u00E1nsito
////					for(DetallePedidoDTO detallePed : detallePedidoDTOCol){
////						if(detallePed.getEntregas() != null && !detallePed.getEntregas().isEmpty()){
////							for(EntregaDTO entregaVal : (Collection<EntregaDTO>)detallePed.getEntregas()){
////								if(sumaCantSolCD < Long.parseLong(((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO)))){
////									if(entregaVal.getNpValidarCheckTransito() != null && entregaVal.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) && 
////											entregaVal.getNpPasoObligatorioBodegaTransito() != null && entregaVal.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
////										entregaVal.setNpPasoObligatorioBodegaTransito(null);
////									}
////								}
////							}
////						}
////					}
////					
////					//if(session.getAttribute(EXISTENCAMBIOS)!=null)
////					//	messages.add("eliminarDetalle",new ActionMessage("messages.eliminarDetalle"));
////					
////					//Itero los detalles para saber si fueron eliminadas todas las entregas
////					boolean eliminadasTodas=true;
////					LogSISPE.getLog().info("va a ver si se eliminaron todas las entregas");
////					for(DetallePedidoDTO detallePedido : detallePedidoDTOCol){
////						if(detallePedido.getEntregas().size()>0){
////							LogSISPE.getLog().info("Si tiene detalles la entrega");
////							eliminadasTodas=false;
////						}
////					}
////					if (eliminadasTodas){
////						LogSISPE.getLog().info("todas las entregas fueron eliminadas");
////						Integer local = 0;
////						if(formulario.getLocalResponsable()!=null){
////							//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
////							//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
////							local = Integer.parseInt(formulario.getLocalResponsable().split("-")[0].trim());
////						}
////						formulario.setLocal(local.toString());
////						formulario.setListaLocales(local.toString());
////						VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
////						vistaLocalDTO.getId().setCodigoLocal(local);
////						vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
////						Collection vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
////						LogSISPE.getLog().info("locales: {}" , vistaLocalDTOCol.size());
////						VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
////						LogSISPE.getLog().info("Local: ...{}" , local);
////						LocalID localID=new LocalID();
////						localID.setCodigoCompania(vistaLocal.getId().getCodigoCompania());
////						localID.setCodigoLocal(local);
////						LogSISPE.getLog().info("****local: ****{}" , localID.getCodigoLocal());
////						
////						session.setAttribute(VISTALOCALORIGEN,vistaLocal);
////						session.setAttribute(VISTALOCALDESTINO,vistaLocal);
////						session.setAttribute(LOCALID, localID);
////						//obtenerCalendario(session, request, localID, errors,ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()) , null, null, formulario);
////					}
////				}else{
////					warnings.add("eliminarEntrega",new ActionMessage("warnings.eliminarEntrega"));
////				}
////				if(session.getAttribute(CALENDARIODIALOCALCOL)!=null && session.getAttribute(LOCALID)!=null){
////					LocalID localID = (LocalID)session.getAttribute(LOCALID);
////					//se verifica si se elimin\u00F3 todo el detalle
////					LogSISPE.getLog().info("va a consultar el calendario despues de eliminar las entregas");
////					//fecha minima
////					Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
////					/*****************************************************************************************/
////					//fecha maxima
////					Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
////					//if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
////					//Diferencia entre fecha de entrega y fecha minima de entrega
////					//jmena verifica que la fechaEntregaCliente no sea null
////					if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
////						formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
////					}
////					Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
////					
////					//jmena validacion para el nuevo calendario de bodega
////					if(vistaLocalDTOAUX != null && vistaLocalDTOAUX.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
////						localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
////						buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
////						//elimino el dia seleccionado del cal.bodega
////						formulario.setFechaEntregaCliente(null);
////					}
////					obtenerCalendario(session,request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
////				}
////				
////				if(formulario.getTodo()!=null)
////					session.removeAttribute(EXISTENENTREGAS);
////				formulario.setSeleccionEntregas(null);
////				formulario.setTodo(null);
//
//			}catch(Exception e){
//				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//			}
//		}
//		else if(request.getParameter("confirmarEliminarEntregas")!=null){
//				try{
//				LogSISPE.getLog().info("va a elminiar un detalle");
//				//Obtengo la configuracion actual de reserva a bodega
//				String tomarStock=(String)session.getAttribute(STOCKENTREGA);
//				//se utiliza para contar cuantos detalles se eliminaron
//				int contadorEliminados = 0;
//				formulario.mantenerValoresEntregas(request);
//				List<DetallePedidoDTO> detallePedidoDTOCol = null;
//				//obtengo el local
//				VistaLocalDTO vistaLocalDTOAUX =(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//				
//				if(formulario.getSeleccionEntregas().length>0){
//					Collection<EntregaDTO> entregasAux = new ArrayList<EntregaDTO>();//almaceno las entregas de un mismo detalle que van a ser eliminadas
//					DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
//					int numEntrega=0;//indice de la entrega a eliminar
//					int numDetalle=0;//indice del detalle
//					long cantidadDespacho=0;
//					String[] parametros = formulario.getSeleccionEntregas()[0].split("-");
//					//guarda el indice del detalle para saber cuando ya se ha borrado todas la entregas de un mismo detalle
//					int numDetalleAnterior = Integer.parseInt(parametros[1]);
//					//obtiene la sumatoria
//					Long sumaCantSolCD = session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE) != null ? (Long)session.getAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE): 0L;
//					
//					//recupero los datos del detalle del pedido
//					detallePedidoDTOCol = (List<DetallePedidoDTO>)session.getAttribute(DETALLELPEDIDOAUX);
//					List<EntregaDTO> entregasEliminarCol = new ArrayList<EntregaDTO>();
//					detallePedidoDTO = detallePedidoDTOCol.get(numDetalleAnterior);
//					
//					for(int i=0;i<formulario.getSeleccionEntregas().length;i++){
//						LogSISPE.getLog().info("SeleccionEntregas: {}" , formulario.getSeleccionEntregas()[i]);
//						parametros=formulario.getSeleccionEntregas()[i].split("-");
//						numEntrega = Integer.parseInt(parametros[0]);
//						numDetalle = Integer.parseInt(parametros[1]);
//						LogSISPE.getLog().info("numeroDetalleAnterior: {}" , numDetalleAnterior);
//						LogSISPE.getLog().info("numeroDetalle: {}" , numDetalle);
//						LogSISPE.getLog().info("numeroEntrega: {}" , numEntrega);
//						
//						if(numDetalleAnterior != numDetalle){
//							LogSISPE.getLog().info("va a elminar los detalles");
//							for (Iterator iter = entregasAux.iterator(); iter.hasNext();) {
//								EntregaDTO entrega = (EntregaDTO) iter.next();
//								//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
//								if(entrega.getNpValidarCheckTransito() != null && entrega.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
//										&& entrega.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
//										&& entrega.getNpPasoObligatorioBodegaTransito() != null && entrega.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
//									sumaCantSolCD = sumaCantSolCD - entrega.getCantidadDespacho();
//								}
//								detallePedidoDTO.getEntregas().remove(entrega);
//							}
//							//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
//							//solo en caso que la entrega sea parcial a bodega
//							LogSISPE.getLog().info("****tomarStock**** " + tomarStock);
//							if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
//								cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
//								LogSISPE.getLog().info("va a mantener el valor{}" , cantidadDespacho);
//								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
//							}
//							cantidadDespacho = 0;
//							entregasAux = new ArrayList<EntregaDTO>();
//							numDetalleAnterior = numDetalle;
//							detallePedidoDTO = detallePedidoDTOCol.get(numDetalle);
//							//se cuentan los detalles eliminados
//							contadorEliminados++;
//						}
//						
//
//						LogSISPE.getLog().info("articulo detalle: {}" , detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
//						List entregas=(List)detallePedidoDTO.getEntregas();
//						EntregaDTO entregaDTO = (EntregaDTO)entregas.get(numEntrega);
//						//calculo los bultos
//						int numeroBultos = 0;
//						if(entregaDTO.getCantidadDespacho().longValue() > 0){
//							numeroBultos =  UtilesSISPE.calcularCantidadBultos(entregaDTO.getCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
//							entregaDTO.setNpCantidadBultos(numeroBultos);
//						}
//						entregasEliminarCol.add(entregaDTO);
//						if(entregaDTO.getCantidadDespacho().longValue()>0)
//							cantidadDespacho = cantidadDespacho + entregaDTO.getCantidadDespacho().longValue();
//						detallePedidoDTO.setNpContadorEntrega(Long.valueOf(detallePedidoDTO.getNpContadorEntrega().longValue() - entregaDTO.getCantidadEntrega().longValue()));
//						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue() + entregaDTO.getCantidadEntrega().longValue()));
//						LogSISPE.getLog().info("cantidad detalle: {}" , entregaDTO.getCantidadEntrega());
//						
//						//se elimina costo y direccion de envio en caso de entregas a domicilio
//						formulario.eliminaCostoEntrega(entregaDTO,request);
//						
//						//Si existio pedido a bodega dentro de la entrega
//						if(entregaDTO.getCalendarioDiaLocalDTO()!=null){
//							detallePedidoDTO.setNpContadorDespacho(Long.valueOf(detallePedidoDTO.getNpContadorDespacho().longValue()-entregaDTO.getCantidadDespacho().longValue()));
//							//Si el despacho es parcial desde bodega 
//							if(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA)!=null){
//								if(!session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))
//									/*detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
//								else*/
//									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()+entregaDTO.getCantidadDespacho().longValue()));
//							}else
//								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0l);
//							LogSISPE.getLog().info("dial local: {}" , entregaDTO.getCalendarioDiaLocalDTO().getId().getFechaCalendarioDia());
//							/*************************Modifico la CD del local eliminando la entrega************************/
//							// Parametros para buscar la capacidad del local en la fecha
//							CalendarioDiaLocalID calendarioDiaLocalID = new CalendarioDiaLocalID();
//							calendarioDiaLocalID.setCodigoCompania(entregaDTO.getId().getCodigoCompania());
//							if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//								LogSISPE.getLog().info("local a eliminar1: {}" , entregaDTO.getCodigoLocalEntrega());
//								calendarioDiaLocalID.setCodigoLocal(entregaDTO.getCodigoLocalEntrega());
//							}
//							else{
//								LogSISPE.getLog().info("local a eliminar2: {}" , entregaDTO.getCodigoLocalSector());
//								calendarioDiaLocalID.setCodigoLocal(new Integer(entregaDTO.getCodigoLocalSector()));
//							}
//							//verifica si el pedido es a domicilio para restar la capacidad de furgones solo del dia seleccionado
//							Date mes=null;
////							if(entregaDTO.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))  &&
////									formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//							if(entregaDTO.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//								 mes=DateManager.getYMDDateFormat().parse(entregaDTO.getFechaEntregaCliente().toString());
//							}else{
//								mes=DateManager.getYMDDateFormat().parse(entregaDTO.getFechaDespachoBodega().toString());
//							}
//							
//							
//							//Date mes=DateManager.getYMDDateFormat().parse(entregaDTO.getFechaDespachoBodega().toString());
//							calendarioDiaLocalID.setFechaCalendarioDia(mes);
//							CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = new CalendarioConfiguracionDiaLocalDTO();
//							if(session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX)!=null)
//								calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
//							LogSISPE.getLog().info("EntregaLocal - BuscarDiaPorID");
//							CalendarioDiaLocalDTO calendarioDiaLocalDTO2=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
//							/**************actualizo la capacidad disponible en el local para el despacho***********/
//							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2: {}" , calendarioDiaLocalDTO2);
//							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Compania: {}" , calendarioDiaLocalDTO2.getId().getCodigoCompania());
//							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Local: {}" , calendarioDiaLocalDTO2.getId().getCodigoLocal());
//							LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Fecha: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
//							LogSISPE.getLog().info("EntregaLocal - entregaDTO.np: {}" , entregaDTO.getNpCantidadBultos());
//							LogSISPE.getLog().info("capacidad disponible2: {}" , calendarioDiaLocalDTO2.getCantidadDisponible());
//							LogSISPE.getLog().info("CantidadAcumulada: {}" , calendarioDiaLocalDTO2.getCantidadAcumulada());
//							LogSISPE.getLog().info("CapacidadAdicional: {}" , calendarioDiaLocalDTO2.getCapacidadAdicional());
//							LogSISPE.getLog().info("CapacidadNormal(): {}" , calendarioDiaLocalDTO2.getCapacidadNormal());
//							
//							//cantidades informativas
//							Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
//							//cantidad para frio
//							CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
//							cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
//							cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
//							cantidadesInformativas.add(cantidadFrio);
//							//cantidad para seco
//							CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
//							cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
//							cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
//							cantidadesInformativas.add(cantidadSeco);
//							
//							//veo si existe autorizacion para ese dia
//							AutorizacionDTO autorizacionDTO = formulario.buscaAutorizacion(session, calendarioDiaLocalID.getCodigoLocal().toString(), entregaDTO.getFechaDespachoBodega());
//							if(autorizacionDTO!=null)
//								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),true, cantidadesInformativas);
//							else
//								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),false, cantidadesInformativas);
//							/**********Va a modificar la cantidad acumulada***********************/
//							LocalID localID=new LocalID();
//							localID.setCodigoCompania(calendarioDiaLocalDTO2.getId().getCodigoCompania());
//							localID.setCodigoLocal(calendarioDiaLocalDTO2.getId().getCodigoLocal());
//							obtenerCalendario(session, request, localID, errors, calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), null, null, formulario, true);
//							Object[] calendarioDiaLocalDTOOBJ=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//							int dia=-1;
//							//Busco el dia de despacho
//							LogSISPE.getLog().info("***********Va a buscar el dia de despacho*******************");
//							for(int indice=0;indice<calendarioDiaLocalDTOOBJ.length;indice++){
//								CalendarioDiaLocalDTO calDia=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indice];
//								if(calDia.getId().getFechaCalendarioDia().equals(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia())){
//									dia=indice;
//									break;
//								}
//							
//							}
//							LogSISPE.getLog().info("dia: {}" , dia);
//							LogSISPE.getLog().info("fecha de despacho: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
//							LogSISPE.getLog().info("fecha de entrega: {}" , entregaDTO.getFechaEntregaCliente());
//							cargaDiasModificarCA(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), entregaDTO.getFechaEntregaCliente(), formulario, request, dia);
//							//Obtengo los dias que debo modificar su capacidad acumulada
//							
//							Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(CALENDARIODIALOCALCOLAUX);
//							for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
//								LogSISPE.getLog().info("%%%%%%%%%%%%%%%%VA A RESTABLECER LOS VALORES EN EL CALENDARIO%%%%%%%%%%%%%");
//								LogSISPE.getLog().info("compania: {}" , calendarioDiaLocalDTOAux.getId().getCodigoCompania());
//								LogSISPE.getLog().info("local: {}" , calendarioDiaLocalDTOAux.getId().getCodigoLocal());
//								
//								//cantidades informativas
//								Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
//								//cantidad para frio
//								CantidadCalendarioDiaLocalDTO cantidadFrioAux = new CantidadCalendarioDiaLocalDTO();
//								cantidadFrioAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
//								cantidadFrioAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
//								cantidadesInformativasAux.add(cantidadFrioAux);
//								//cantidad para seco
//								CantidadCalendarioDiaLocalDTO cantidadSecoAux = new CantidadCalendarioDiaLocalDTO();
//								cantidadSecoAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
//								cantidadSecoAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
//								cantidadesInformativasAux.add(cantidadSecoAux);
//								
//								calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), calendarioDiaLocalDTOAux.getCapacidadAdicional(), new Double(0), new Double(-entregaDTO.getNpCantidadBultos().longValue()),false,cantidadesInformativasAux);
//								LogSISPE.getLog().info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//							}
//							session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalDTO);
//							/***************************************************************************************/
//			
//							/***********************************************************************************************/
//							
//						}
//						entregasAux.add(entregaDTO);
//						//..detallePedidoDTO.getEntregas().remove(entregaDTO);
//						//seteo la variable de sesion para saber que existieron cambios
//						session.setAttribute(EXISTENCAMBIOS,"ok");
//						//ordena la coleccion de detalles
//						//..ordenarDetalleEntregas(session);
//					}
//					
//					if(entregasAux.size()>0){//aca
//						LogSISPE.getLog().info("elimina las entregas:");
//						
//						for (Iterator iter = entregasAux.iterator(); iter.hasNext();) {
//							EntregaDTO entrega = (EntregaDTO) iter.next();							
//							//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
//							if(entrega.getNpValidarCheckTransito() != null && entrega.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))  
//									&& entrega.getNpPasoObligatorioBodegaTransito() != null && entrega.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
//									&& entrega.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
//									){
//								sumaCantSolCD = sumaCantSolCD - entrega.getCantidadDespacho();
//							}
//							detallePedidoDTO.getEntregas().remove(entrega);
//						}
//						detallePedidoDTO= detallePedidoDTOCol.get(numDetalle);
//						session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
//						
//						//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
//						//solo en caso que la entrega sea parcial a bodega
//						LogSISPE.getLog().info("cantidadDespacho: {}" , cantidadDespacho);
//						LogSISPE.getLog().info("tomarStock: {}" , tomarStock);
//						if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
//							LogSISPE.getLog().info("va a mantener la cantidad");
//							cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
//							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
//						
//						}
//						//detallePedidoDTO.getEntregas().remove(entCol);
//						LogSISPE.getLog().info("numero de entregas del detalle: {}" , detallePedidoDTO.getEntregas().size());
//					}
//					
//					//valido obligatoriedad de tr\u00E1nsito
//					for(DetallePedidoDTO detallePed : detallePedidoDTOCol){
//						if(detallePed.getEntregas() != null && !detallePed.getEntregas().isEmpty()){
//							for(EntregaDTO entregaVal : (Collection<EntregaDTO>)detallePed.getEntregas()){
//								if(sumaCantSolCD < Long.parseLong(((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO)))){
//									if(entregaVal.getNpValidarCheckTransito() != null && entregaVal.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) && 
//											entregaVal.getNpPasoObligatorioBodegaTransito() != null && entregaVal.getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
//										entregaVal.setNpPasoObligatorioBodegaTransito(null);
//									}
//								}
//							}
//						}
//					}
//					
//					//if(session.getAttribute(EXISTENCAMBIOS)!=null)
//					//	messages.add("eliminarDetalle",new ActionMessage("messages.eliminarDetalle"));
//					
//					//elimino los camiones que haran la entrega 
//					EntregaLocalCalendarioUtil.eliminarCamionesDelPedido(entregasEliminarCol, request);
//					//Itero los detalles para saber si fueron eliminadas todas las entregas
//					boolean eliminadasTodas=true;
//					LogSISPE.getLog().info("va a ver si se eliminaron todas las entregas");
//					for(DetallePedidoDTO detallePedido : detallePedidoDTOCol){
//						if(detallePedido.getEntregas().size()>0){
//							LogSISPE.getLog().info("Si tiene detalles la entrega");
//							eliminadasTodas=false;
//						}
//					}
//					if (eliminadasTodas){
//						LogSISPE.getLog().info("todas las entregas fueron eliminadas");
//						Integer local = 0;
//						if(formulario.getLocalResponsable()!=null){
//							//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
//							//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
//							local = Integer.parseInt(formulario.getLocalResponsable().split("-")[0].trim());
//						}
//						formulario.setLocal(local.toString());
//						formulario.setListaLocales(local.toString());
//						VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
//						vistaLocalDTO.getId().setCodigoLocal(local);
//						vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//						Collection vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
//						LogSISPE.getLog().info("locales: {}" , vistaLocalDTOCol.size());
//						VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
//						LogSISPE.getLog().info("Local: ...{}" , local);
//						LocalID localID=new LocalID();
//						localID.setCodigoCompania(vistaLocal.getId().getCodigoCompania());
//						localID.setCodigoLocal(local);
//						LogSISPE.getLog().info("****local: ****{}" , localID.getCodigoLocal());
//						session.setAttribute(VISTALOCALORIGEN,vistaLocal);
//						session.setAttribute(VISTALOCALDESTINO,vistaLocal);
//						session.setAttribute(LOCALID, localID);
//						//obtenerCalendario(session, request, localID, errors,ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()) , null, null, formulario);
////						session.removeAttribute(COSTOENTREGA);
////						session.removeAttribute(COSTOENTREGAAUX);
////						session.setAttribute(ELIMINO_TODAS_ENTREGAS, ConstantesGenerales.ESTADO_ACTIVO);
//						
//					}
//				}else{
//					warnings.add("eliminarEntrega",new ActionMessage("warnings.eliminarEntrega"));
//				}
//				if(session.getAttribute(CALENDARIODIALOCALCOL)!=null && session.getAttribute(LOCALID)!=null){
//					LocalID localID = (LocalID)session.getAttribute(LOCALID);
//					//se verifica si se elimin\u00F3 todo el detalle
//					LogSISPE.getLog().info("va a consultar el calendario despues de eliminar las entregas");
//					//fecha minima
//					Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//					/*****************************************************************************************/
//					//fecha maxima
//					Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//					//if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//					//Diferencia entre fecha de entrega y fecha minima de entrega
//					//jmena verifica que la fechaEntregaCliente no sea null
//					if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
//						formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//					}
//					Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//					
//					//jmena validacion para el nuevo calendario de bodega
//					if(vistaLocalDTOAUX != null && vistaLocalDTOAUX.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//						localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
//						buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
//						//elimino el dia seleccionado del cal.bodega
//						formulario.setFechaEntregaCliente(null);
//					}
//					obtenerCalendario(session,request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
//				}
//				//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
//				EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOCol, request);
//				
//				if(formulario.getTodo()!=null)
//					session.removeAttribute(EXISTENENTREGAS);
//				formulario.setSeleccionEntregas(null);
//				formulario.setTodo(null);
//				LogSISPE.getLog().info("remover PoPuP");	
//				session.removeAttribute(SessionManagerSISPE.POPUP);
//				session.removeAttribute(EXISTENCAMBIOS);
//				session.removeAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL);
//				session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_PROCESADO);
//
//			}catch(Exception e){
//				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//			}
//		} else if (request.getParameter("cancelarEliminarEntregas")!=null){
//			LogSISPE.getLog().info("cancelarEliminarEntregas");
//	    	session.removeAttribute(SessionManagerSISPE.POPUP);
//		}
//		
//		/******************************************************************************
//		 ****************************AUTORIZACIONES*************************************
//		 ******************************************************************************/
//		else if(request.getParameter("botonAutorizar")!=null){
//			LogSISPE.getLog().info("Entro al boton autorizar");
//			if(session.getAttribute(CALENDARIODIALOCAL)!=null){
//				//se crea el convertidor
//				convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
//				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
//				asignacionValoresFormulario(session, formulario);
//				AutorizacionDTO autorizacionDTO =null;
//				if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) &&  
//						formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && 
//						((String)session.getAttribute("ec.com.smx.sic.sispe.validar.bodega"))!=null ){
//				 autorizacionDTO = formulario.buscaAutorizacion(session,(SessionManagerSISPE.getCurrentLocal(request)).toString(),(Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
//				}else{
//					autorizacionDTO = formulario.buscaAutorizacion(session,calendarioDiaLocalDTO.getId().getCodigoLocal().toString(),(Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
//				}
//				
//				if(autorizacionDTO==null){
//					LogSISPE.getLog().info("va a abrir la ventana de autorizacion");
//					request.setAttribute("ec.com.smx.sic.pedido.numeroAutorizacion", "Ingrese el n\u00FAmero de autorizaci\u00F3n");
//					formulario.setAutorizacion(null);					
//				}else{
//					errors.add("autorizacion",new ActionMessage("errors.autorizacion",calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//				}
//			}else{
//				errors.add("selecciondia",new ActionMessage("errors.localDia"));
//			}
//		}
//		/*******************************************************************************************************/
//		//seleccionar una ciudad de entrega
//		else if(request.getParameter("seleccionCiudadCombo")!=null){
//			LogSISPE.getLog().info("selecciona una ciudad en el combo: {}" , formulario.getSeleccionCiudad());
//		
//			if(formulario.getSeleccionCiudad().equals("ciudad") || formulario.getSeleccionCiudad().equals("")){
//				session.removeAttribute(DIASELECCIONADO);
//				info.add(formulario.getSeleccionCiudad(), new ActionMessage("errors.seleccion.ciudad", "Ciudad de Entrega V\u00E1lida"));
//			}else {
//				//OANDINO: Se carga el valor del nombre ciudad/c\u00F3digo ciudad -----------------------------			
//				String[] valorCodigoDivGeoPol = formulario.getSeleccionCiudad().split("/");
//				
//				if(valorCodigoDivGeoPol.length > 1){
//					// Se carga en session el valor del c\u00F3digo de la ciudad que viene adjunto al nombre de la misma desde combobox de JSP
//					session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, valorCodigoDivGeoPol[1]);
//				}else{
//					session.setAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO, formulario.getSeleccionCiudad());
//					
//					//buscar zonas de esa ciudad
//					cargarZonaCiudad(formulario, session);
//					
//					//Si la ciudad no tiene zona, se carga los totales de camiones
//					Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
//					if(CollectionUtils.isEmpty(zonasCiudad)){	
//						if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null)
//							obtenerCalendarioPorSemana(session, request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
//					}
//				}
//				LogSISPE.getLog().info("C\u00F3digo DivGeoPol: {}",session.getAttribute(VAR_VALOR_CODIGO_CIUDAD_COMBO));
//				//----------------------------------------------------------------------------------------
//				
//				//Saco las tres primeras letras del valor del combo para ver si selecciono una ciudad distinta a las recomendadas
//				String subNombre = valorCodigoDivGeoPol[0].substring(0, 3);
//				
//				if(!subNombre.equals("") && subNombre.equals("OTR")){
//					LogSISPE.getLog().info("Selecciono otra ciudad");
//					//Si selecciona otra ciudad se realiza la pregunta
//					request.setAttribute("ec.com.smx.sic.sispe.mensajeSeleccionCiudad", "ok");
//					session.setAttribute("otraCuidad", "ok");
//				}else if(formulario.getSeleccionCiudad().equals(""))
//					session.removeAttribute(DIASELECCIONADO);
//				else{
//					LogSISPE.getLog().info("selecciono ciudad recomendada");
//					if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
//						int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
//						session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
//					}else{
//						session.setAttribute(DIASELECCIONADO, "ok");
//					}
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePaso2.3"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//					
//					session.setAttribute("siDireccion", "ok");
//					session.setAttribute("otraCuidad", "ok");
//				}
//				//resetea la direccion cuando no es calendario por horas
//				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) == null){
//					formulario.setDirecciones(null);
//					formulario.setDireccion(null);
//					formulario.setDistancia(null);
//					formulario.setDistanciaH(null);
//					formulario.setDistanciaM(null);
//					session.removeAttribute(DIRECCION);
//				}
//			}
//
//		}
//
//		//cuando se selecciona el combo de ZONA de CIUDAD
//		else if(request.getParameter("seleccionCiudadZonaCombo")!= null){
//			
//			String zonaSeleccionada = formulario.getSelecionCiudadZonaEntrega();
//			LogSISPE.getLog().info("zona seleccionada {}",zonaSeleccionada);
//				
//			if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
//				obtenerCalendarioPorSemana(session, request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
//			}
//			
//			
//		}
//		//seleccionar SI en la ventana del mensaje "otra ciudad"
//		else if(request.getParameter("condicionSi")!=null){
//			LogSISPE.getLog().info("ventada de ciudad opcion si");
//			
//			//se elimina el calendario de bodega por horas para que cargue el calendario normal
//			session.removeAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS);
//			session.setAttribute(SELECCIONARLOCAL, "ok");
//			
//			asignacionValoresFormulario(session, formulario);
//			formulario.setOpLugarEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal"));
//			session.removeAttribute(DIASELECCIONADO);
//			request.removeAttribute("ec.com.smx.sic.sispe.mensajeSeleccionCiudad");
//			aceptaConfiguracionEntrega(request, formulario, error, errors, warnings);
//			session.removeAttribute(POSICIONDIVCONFENTREGAS);
//			
//			//buscar zonas de esa ciudad
//			cargarZonaCiudad(formulario, session);
//			
//			//Si la ciudad no tiene zona, se carga los totales de camiones
//			Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
//			if(CollectionUtils.isEmpty(zonasCiudad)){	
//				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null)
//					obtenerCalendarioPorSemana(session, request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
//			}
//			
//		}
//		//seleccionar NO en la ventana del mensaje "otra ciudad"
//		else if(request.getParameter("condicionNo")!=null){
//			LogSISPE.getLog().info("ventada de ciudad opcion no");
//			if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
//				int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
//				session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
//			}else{
//				session.setAttribute(DIASELECCIONADO, "ok");
//			}
//			formulario.setDirecciones(null);
//			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
//			//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//			if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){
//				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso4"));
//			}
//			else{
//				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//			}
//			session.setAttribute("siDireccion", "ok");
//			
//			//buscar zonas de esa ciudad
//			cargarZonaCiudad(formulario, session);
//			
//			//Si la ciudad no tiene zona, se carga los totales de camiones
//			Collection<DivisionGeoPoliticaDTO> zonasCiudad =  (Collection<DivisionGeoPoliticaDTO>)session.getAttribute(CIUDAD_SECTOR_ENTREGA);
//			if(CollectionUtils.isEmpty(zonasCiudad)){	
//				if(session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null)
//					obtenerCalendarioPorSemana(session, request, (LocalID)session.getAttribute(LOCALID), null, (Date)session.getAttribute(MES_ACTUAL_CALENDARIO), (Date)session.getAttribute(FECHAMINIMA), (Date)session.getAttribute(FECHAMAXIMA), formulario, true);
//			}
//		}
//		
//		//aceptar la autorizacion
//		else if(request.getParameter("condicionAceptar")!=null){
//			//Si ingreso un numero de autorizacion
//			asignacionValoresFormulario(session, formulario);
//			if(formulario.getAutorizacion()!=null && !formulario.getAutorizacion().equals("")){
//				AutorizacionDTO autorizacionDTO = new AutorizacionDTO();
//				try{
//					//veirifico que no se repita el numero de autorizacion para otro local
//					boolean existeAutorizacion = formulario.buscaNumeroAutorizacion(session, formulario.getAutorizacion());
//					if(!existeAutorizacion){
//						
//						String tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.reservarBodegaLocal");
//						String grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
//						
////						if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////								formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//						//obtengo el local
//						VistaLocalDTO vistaLocalDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//						
//						Integer codigoLocal=vistaLocalDTO.getId().getCodigoLocal().intValue();
//						if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//							grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
//						}else if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) &&  
//								formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && 
//								((String)session.getAttribute("ec.com.smx.sic.sispe.validar.bodega"))!=null ){
//							grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
//							codigoLocal=Integer.valueOf((SessionManagerSISPE.getCurrentLocal(request)).toString()).intValue();
//						}
//						
//						
//						
//
//
//						//metodo para validar autorizacion
//						autorizacionDTO = AutorizacionesUtil.validarNumeroAutorizacion(request, formulario.getAutorizacion(), null, 
//								tipoAutorizacion, grupoAutorizacion, codigoLocal);
//						
//						//se crea el convertidor
//						convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
//						
//						CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
//						Collection<AutorizacionEntregasDTO> autorizacionEntregasWDTOCol=new ArrayList<AutorizacionEntregasDTO>();
//						AutorizacionEntregasDTO autorizacionEntregasWDTO=new AutorizacionEntregasDTO();
//						autorizacionEntregasWDTO.setCodigoAutorizacion(autorizacionDTO.getId().getCodigoAutorizacion());
//						//Obtener el c\u00F3digo de local dado el c\u00F3digo de \u00E1rea de trabajo de la autorizaci\u00F3n
//						//para realizar la autorizaci\u00F3n de entrega
//						AreaTrabajoID areaTrabajoId = new AreaTrabajoID();
//						areaTrabajoId.setCodigoCompania(autorizacionDTO.getId().getCodigoCompania());
//						areaTrabajoId.setCodigoAreaTrabajo(autorizacionDTO.getCodigoAreaTrabajo());
//						AreaTrabajoDTO areaTrabajoEntrega = CorporativoFactory.getParametrosService().findAreaTrabajoById(areaTrabajoId);
//						//jmena Para la autorizacion del calendarioBodega seteo el codigoLocal (99)
////						if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////								formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//						if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//							autorizacionEntregasWDTO.setCodigoLocal(vistaLocalDTO.getId().getCodigoLocal().toString());
//						}else{
//							autorizacionEntregasWDTO.setCodigoLocal(String.valueOf(areaTrabajoEntrega.getCodigoLocal()));
//						}
//						LogSISPE.getLog().info("fecha a la que se le asigna autorizacion: {}", calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
//						autorizacionEntregasWDTO.setFechaAutorizacion((Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
//						autorizacionEntregasWDTO.setNumeroAutorizacion(formulario.getAutorizacion());
//						autorizacionEntregasWDTO.setAutorizacionDTO(autorizacionDTO);
//						if(session.getAttribute(AUTORIZACIONENTREGASCOL)!=null)
//							autorizacionEntregasWDTOCol=(Collection)session.getAttribute(AUTORIZACIONENTREGASCOL);
//						autorizacionEntregasWDTOCol.add(autorizacionEntregasWDTO);
//						session.setAttribute(AUTORIZACIONENTREGASCOL,autorizacionEntregasWDTOCol);
//						info.add("autorizacionValida",new ActionMessage("info.autorizacionValida"));
//					}
//					else
//						errors.add("existeAutorziacion",new ActionMessage("errors.existeAutorizacion"));
//				}catch (Exception ex ) {
//					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
//					errors.add("autorizacion",new ActionMessage("errors.autorizacionInvalida"));
//				}
//			}
//		}
//		//cerrar la ventana de autorizacion
//		else if(request.getParameter("condicionCancelar")!=null){
//			LogSISPE.getLog().info("entra a cerrar la ventana de autorizacion");
//			session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
//			formulario.setAutorizacion(null);
//		}
//		/**
//		 * @author jmena
//		 * Control para los tabs de configuracion de entrega
//		 */
//		else if (beanSession.getPaginaTab() != null && session.getAttribute(MOSTRAR_POPUPTAB) == null && beanSession.getPaginaTab().comprobarSeleccionTab(request) ) {
//			
//			if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
//				//se reasigna el valor de los checks de transito
//				formulario.setCheckTransitoArray((String [])session.getAttribute(CHECKTRANSITO));
//				/*--------------- si se escogi\u00F3 el tab configuraciones entrega op1 ----------------*/
//				LogSISPE.getLog().info("Se elige el tab de configuracion Opcion1");
//				WebSISPEUtil.cambiarTabConfiguracionesEntregas(beanSession, 0);
//				session.removeAttribute(CHECKTRANSITO);
//			}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
//				session.setAttribute(CHECKTRANSITO, formulario.getCheckTransitoArray());
//				/*------------   si se escogi\u00F3 el tab configuraciones entrega op2 --------------*/
//				LogSISPE.getLog().info("Se elige el tabde configuracion Opcion2");
//				//prepar la nueva coleccion para mostrar los responsables
//				Collection<EntregaDTO> entregasResp=new ArrayList<EntregaDTO>();
//				Collection<DetallePedidoDTO> detPedResp=new ArrayList<DetallePedidoDTO>();
//				Collection<DetallePedidoDTO> detallePedidoDTOCol=(Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//				if(!CollectionUtils.isEmpty(detallePedidoDTOCol)){
//					for(DetallePedidoDTO detPed:detallePedidoDTOCol){
//						if(!CollectionUtils.isEmpty(detPed.getEntregas())){
//							Collection<EntregaDTO> entregas=(Collection<EntregaDTO>)detPed.getEntregas();
//							if(CollectionUtils.isEmpty(entregasResp)){
//								entregasResp=(Collection<EntregaDTO>)SerializationUtils.clone((Serializable)entregas);
//								for(EntregaDTO entPed:entregasResp){
//									detPedResp=new ArrayList<DetallePedidoDTO>();
//									entPed.setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
//									detPed.setNpReponsable(null);
//									DetallePedidoDTO dp= new DetallePedidoDTO();
//									dp.setArticuloDTO(new ArticuloDTO());
//									dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
//									dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
//									dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
//									dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
//									obtenerResponsableEntrega(session,dp,entPed);
//									detPedResp.add(dp);
//									entPed.setNpDetallePedido(detPedResp);
//								}
//							}
//							else{
//								for(EntregaDTO entPed:entregas){
//									Boolean existeEntrega=Boolean.FALSE;
//									DetallePedidoDTO dp= new DetallePedidoDTO();
//									dp.setArticuloDTO(new ArticuloDTO());
//									dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
//									dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
//									dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
//									dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
//									for(EntregaDTO entPedRes:entregasResp){
//										Long diferencia= entPed.getFechaEntregaCliente().getTime()-entPedRes.getFechaEntregaCliente().getTime();
//										if(diferencia==0L && entPed.getDireccionEntrega().toUpperCase().trim().equals(entPedRes.getDireccionEntrega().toUpperCase().trim())){
//											//estructura de responsables, asignar a los detalles
//											detPed.setNpReponsable(null);
//											obtenerResponsableEntrega(session,dp,entPed);
//											entPedRes.getNpDetallePedido().add(dp);
//											existeEntrega=Boolean.TRUE;
//											break;
//										}
//									}
//									if(!existeEntrega){
//										entPed.setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
//										detPed.setNpReponsable(null);
//										obtenerResponsableEntrega(session,dp,entPed);
//										entPed.getNpDetallePedido().add(dp);
//										entregasResp.add(entPed);
//										
//									}
//								}
//							}
//						}					
//					}
//				}
//				
//				session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_RESPONSABLES,entregasResp);
//				WebSISPEUtil.cambiarTabConfiguracionesEntregas(beanSession, 1);
//			}			
//		}
//		/**
//		 * @author jmena
//		 * Control para los tabs de configuracion de entrega
//		 */
//		else if (beanSession.getPaginaTabPopUp() != null && beanSession.getPaginaTabPopUp().comprobarSeleccionTab(request)) {
//			
////			asignacionValoresFormulario(session, formulario);
////			
////			String tabHabilitados=null;
////			
////			tabHabilitados = tabHabilitadosParciales(request, tabHabilitados);
////			
////			regresarValoresIniciales(session);
////			
////			if (beanSession.getPaginaTabPopUp().esTabSeleccionado(0)) {
////				/*--------------- si se escogi\u00F3 el tab configuraciones entrega op1 ----------------*/
////				if(tabHabilitados != null){
////					session.removeAttribute(TABSELECCIONADONAVEGACION);
////				}
////				navegacionPopUp(session, beanSession,"Siguiente","","entregaLocalCalendario.do","entregaLocalCalendario.do","entregas","ocultarVentanaResponsable","ocultarModal();","'entregas','pregunta'",0,"siguienterD","");
////				LogSISPE.getLog().info("Se elige el tab de configuracion Opcion1");
////				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
////				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
////				session.removeAttribute(PASOSPOPUP);
////				//WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, 0);
////			}else if (beanSession.getPaginaTabPopUp().esTabSeleccionado(1)) {
////				/*------------   si se escogi\u00F3 el tab configuraciones entrega op2 --------------*/
////				if(tabHabilitados == null){		
////					navegacionPopUp(session, beanSession,"Anterior","Confirmar","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajes','entregas','pregunta','opcionesBusqueda'",1,"anteriorD","aceptarD");
////					
////					//mensajes de los pasos
////					mantenerValoresMensajesPasosNavegacionPopUP(session, formulario, "ayuda.paso2");					
////					//fin mensajes de los pasos
////					
////				}
////				else{					
////					navegacionPopUp(session, beanSession,"Anterior","Siguiente","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","entregas","","'mensajesPopUp','listado_articulos'",1,"anteriorD","siguienterD");
////					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
////					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
////				}
////				
////				LogSISPE.getLog().info("Se elige el tabde configuracion Opcion2");
////				//WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, 1);
////			}else if (beanSession.getPaginaTabPopUp().esTabSeleccionado(2)) {
////				/*------------   si se escogi\u00F3 el tab configuraciones entrega op2 --------------*/				
////				navegacionPopUp(session, beanSession,"Anterior","Confirmar","entregaLocalCalendario.do","entregaLocalCalendario.do","atrasPopUpEntregas","botonAceptarEntrega","ocultarModal();","'mensajes','entregas','pregunta','opcionesBusqueda'",2,"anteriorD","aceptarD");
////				LogSISPE.getLog().info("Se elige el tabde configuracion Opcion3");
////				//WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, 2);
////
////				//mensajes de los pasos
////				mantenerValoresMensajesPasosNavegacionPopUP(session, formulario,"ayuda.paso3");
////				//fin mensajes de los pasos
////			}
//			
//		}
//		
//		//----- caso por omisi\u00F3n -----
//		else{
//			LogSISPE.getLog().info("entro al else");
//			session.removeAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE);
//			session.removeAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO);
//			session.removeAttribute(CLASIFICACIONES_PERECIBLES);
//			
//			//OANDINO: Se carga en session el contenido del campo CODIGOPARAMETRO de SCSPETPARAMETRO ------------------------------------			
//			//String codigoCiudadCheck = "";
//			
//			// Plantilla de b\u00FAsqueda
//			ParametroID parametroID = new ParametroID();
//			parametroID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());			
//			parametroID.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD"));
//			
//			// Se obtiene el valor de VALORPARAMETRO de SCSPETPARAMETRO
//			ParametroDTO parametroValorDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametroID);
//			String valorParametro = parametroValorDTO.getValorParametro();
//			LogSISPE.getLog().info("Valor par\u00E1metro completo: {}",valorParametro);
//			
//			// Se sube a session el valor anterior
//			session.setAttribute(VAR_VALOR_PARAMETRO_TOTAL, valorParametro);
//			
//			//se obtiene el valor del par\u00E1metro que contiene el limite inferior para que una entrega se obligatoriamente  con transito
//			session.setAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO, WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.totalReservaCDObligatoriedadBodegaTransito", request).getValorParametro());
//			//---------------------------------------------------------------------------------------------------------------------------
//			
//			//variable de sesion para cambiar el metas
//			session.setAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"),"ok");			
//			session.removeAttribute(EXISTENCAMBIOS);//variable de sesion para saber si existieron cambios antes de salir
//			
//			//cargo los dias de la semana
//			String[] orden1 = {"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};
//			String[] orden2 = {"Dom","Lun","Mar","Mie","Jue","Vie","Sab"};
//			String[] dias=null;
//			//si el primer dias es el 1 la semana empieza en domingo caso contrario en lunes
//			if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.primerDiaSemana").equals("1"))
//				dias=orden2;
//			else
//				dias=orden1;
//			//guardo en sesion los dias de la semana
//			session.setAttribute(ORDENDIAS,dias);
//			if(session.getAttribute(CONFIGURACION)== null){
//				//Obtiene la configuracion para las entregas
//				obtenerConfiguracionEntrega(request, errors);
//			}
//			if(session.getAttribute(OPCIONLUGARENTREGA)==null){
//				formulario.setOpLugarEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"));
//				formulario.setOpTipoEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"));
//				formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"));
//			}
//			else{
//				formulario.setOpLugarEntrega((String)(session.getAttribute(OPCIONLUGARENTREGA)));
//				formulario.setOpTipoEntrega((String)(session.getAttribute(OPCIONTIPOENTREGA)));
//				formulario.setOpStock((String)(session.getAttribute(OPCIONSTOCK)));
//			}
//			
//			if(session.getAttribute(VISTALOCALORIGEN)==null){
//				Integer local = 0;
//				if(formulario.getLocalResponsable()!=null){
//					//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
//					//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
//					local = Integer.parseInt(formulario.getLocalResponsable().split("-")[0].trim());
//				}
//				VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
//				vistaLocalDTO.getId().setCodigoLocal(local);
//				vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//				Collection vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
//				LogSISPE.getLog().info("locales: {}" , vistaLocalDTOCol.size());
//				VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();			
//				session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN, vistaLocal);
//				//por defecto el local destino es el mismo de origen
//				session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO, vistaLocal);
//			}
//			//Saco dos copia de la configuracion de los calendarios
//			if(session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL)!=null){
//				LogSISPE.getLog().info("si tiene configuracion del calendario");
//				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocal=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCAL);
//				CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalAux=calendarioConfiguracionDiaLocal.copiar();
//				session.setAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalAux);
//			}
//			
//			/*************** Uso una sesion auxiliar para el detalle pedido, calendarioConfiguracionDialLocal, costos y direcciones *******************/						
//			//Clono el detalle del pedido
//			Collection detallePedidoDTOColAux=new ArrayList<DetallePedidoDTO>();
//			List<DetallePedidoDTO> detallePedidoDTOCol=(List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//			Long sumaCantSolCD = 0L;
//			for (int i = 0; i < detallePedidoDTOCol.size(); i++){
//				//contDetalle = i;
//				//int contEntrega = 0;
//				
//				DetallePedidoDTO detallePedidoDTO = detallePedidoDTOCol.get(i);
//				DetallePedidoDTO detallePedidoDTOAux = detallePedidoDTO.clone();
//				Collection entregasCol=new ArrayList<EntregaDTO>();
//				if(detallePedidoDTO.getEntregas()!=null && detallePedidoDTO.getEntregas().size()>0){
//					for (Iterator iter = detallePedidoDTO.getEntregas().iterator(); iter.hasNext();) {
//						EntregaDTO entrega = (EntregaDTO)iter.next();
//						EntregaDTO entregaAux = entrega.clone();
//						entregasCol.add(entregaAux);
//						session.setAttribute(EXISTENENTREGAS, "ok");
//						
//						LogSISPE.getLog().info("**entregaAux.getNpValidarCheckTransito(): {}", entregaAux.getNpValidarCheckTransito());
//						LogSISPE.getLog().info("**entregaAux.getNpValorCodigoTransito(): {}", entregaAux.getNpValorCodigoTransito());
//						//si est\u00E1 habilitado el check de transito y es a domicilio
//						if(!verificarArticuloPerecible(request, detallePedidoDTOAux.getArticuloDTO().getCodigoClasificacion()) 
//								&& entregaAux.getNpValidarCheckTransito() != null && entregaAux.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
//								&& entregaAux.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
//								){
//							sumaCantSolCD = sumaCantSolCD + entregaAux.getCantidadDespacho();
//						}
//					}
//				}
//				detallePedidoDTOAux.setEntregas(entregasCol);
//				detallePedidoDTOColAux.add(detallePedidoDTOAux);
//			}
//			//sube a session la sumatoria
//			session.setAttribute(TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
//			
//			//verifica si debe aplicar obligatoriedad de transito en el caso de tener previamente configurado las entregas
//			if(detallePedidoDTOColAux != null && !detallePedidoDTOColAux.isEmpty()){
//				for (DetallePedidoDTO detallePedidoDTOAux : (Collection<DetallePedidoDTO>)detallePedidoDTOColAux){
//					if(!verificarArticuloPerecible(request, detallePedidoDTOAux.getArticuloDTO().getCodigoClasificacion()) && 
//							detallePedidoDTOAux.getEntregas() != null && !detallePedidoDTOAux.getEntregas().isEmpty()
//							){
//						for (EntregaDTO entregaAux : (Collection<EntregaDTO>)detallePedidoDTOAux.getEntregas()) {
//							//si est\u00E1 habilitado el check de transito
//							if(sumaCantSolCD >= Long.parseLong((String)session.getAttribute(PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO))
//									&& entregaAux.getNpValidarCheckTransito() != null && entregaAux.getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
//									&& entregaAux.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
//							){
//								entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//							}
//						}
//					}
//				}
//			}
//			
//			//se reasigna el valor de los checks de transito
//			formulario.setCheckTransitoArray((String [])session.getAttribute(EntregaLocalCalendarioUtil.CHECKS_ENTREGAS_TRANSITO));
//			
//			session.setAttribute(DETALLELPEDIDOAUX, detallePedidoDTOColAux);
//			//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
//			EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOColAux, request);
//			/**********************************************************************************/
//			LogSISPE.getLog().info("direcciones: {}" , session.getAttribute(DIRECCIONESAUX));
//			//Clono las direcciones
//			if(session.getAttribute(DIRECCIONES)!=null){
//				Collection direccionesDTOColAux=new ArrayList<DireccionesDTO>();
//				Collection direccionesDTOCol=(Collection)session.getAttribute(DIRECCIONES);
//				for (Iterator iter = direccionesDTOCol.iterator(); iter.hasNext();) {
//					DireccionesDTO direccionesDTO = (DireccionesDTO) iter.next();
//					DireccionesDTO direccionesDTOAux = direccionesDTO.clone();
//					direccionesDTOColAux.add(direccionesDTOAux);
//				}
//				session.setAttribute(DIRECCIONESAUX, direccionesDTOColAux);
//			}
//			LogSISPE.getLog().info("costos: {}" , session.getAttribute(COSTOENTREGAAUX));
//			//Clono los costos
//			if(session.getAttribute(COSTOENTREGA)!=null){
//				Collection costoEntregasDTOColAux = new ArrayList<CostoEntregasDTO>();
//				Collection costoEntregasDTOCol=(Collection)session.getAttribute(COSTOENTREGA);
//				for (Iterator iter = costoEntregasDTOCol.iterator(); iter.hasNext();) {
//					CostoEntregasDTO costoEntregasDTO = (CostoEntregasDTO) iter.next();
//					costoEntregasDTOColAux.add(costoEntregasDTO.clone());
//				}
//				session.setAttribute(COSTOENTREGAAUX, costoEntregasDTOColAux);
//			}
////			//Variable auxiliar para el costo de la entrega
////			if(session.getAttribute(VALORTOTALENTREGA)!=null){
////				Double costoAnt = (Double)session.getAttribute(VALORTOTALENTREGA);
////				LogSISPE.getLog().info("costoAnt: {}" , costoAnt);
////				double costoAux = costoAnt.doubleValue();
////				session.setAttribute(VALORTOTALENTREGAAUX, new Double(costoAux));
////			}
//			//Variable auxiliar para el costo de la entrega
//			if(session.getAttribute(SECDIRECCIONES)!=null){
//				Integer contDir=(Integer)session.getAttribute(SECDIRECCIONES);
//				int contAux = contDir.intValue();
//				session.setAttribute(SECDIRECCIONESAUX, new Integer(contAux));
//			}
//			
//			String entidadResponsableLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
//			String entidadResponsableBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
//			
//			//se obtiene el par\u00E1metro de la clasifiacaci\u00F3n que obliga a que la entidad responsable sea el CD
//			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionResponsabilidadCD", request);
//			String [] codigosClasificacion = null;
//			if(parametroDTO.getValorParametro()!=null){
//				codigosClasificacion = parametroDTO.getValorParametro().split(",");
//			}
//			
//			boolean entResEsBOD = false;
//			//se verifica el detalle del pedido
//			if(codigosClasificacion != null){
//				Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//				for(int i=0; i<codigosClasificacion.length;i++){
//					if(!ColeccionesUtil.simpleSearch("articuloDTO.codigoClasificacion", codigosClasificacion[i], colDetallePedidoDTO).isEmpty()){
//						entResEsBOD = true;
//						break;
//					}
//				}
//			}
//
//			if(entResEsBOD){
//				//si la entidad resulta ser BODEGA se crea una nueva variable de sesi\u00F3n para desactivar la opci\u00F3n de entidad responsable LOCAL
//				session.setAttribute(DESACTIVAR_ENT_RES_LOC, "ok");
//			}else{
//				session.removeAttribute(DESACTIVAR_ENT_RES_LOC);
//			}
//			
//			/*-------------------------------------------------------------------------------*/
//			//Por defecto la entrega es a locales
//			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
//			/*-------------------------------------------------------------------------------*/
//			
//			//selecciono las opciones por defecto
//			if(session.getAttribute(NOMBREENTIDADRESPONSABLE)==null 
//					|| (session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null && session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(entidadResponsableLocal))){
//
//				//se verifica si la entidad responsable resulta ser la Bodega
//				if(entResEsBOD){
//					//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//					formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
//					session.setAttribute(NOMBREENTIDADRESPONSABLE,entidadResponsableBodega);
//				}else{
//					//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//				}
//			}
//			else if(session.getAttribute(NOMBREENTIDADRESPONSABLE)!=null 
//					&& session.getAttribute(NOMBREENTIDADRESPONSABLE).toString().equals(entidadResponsableBodega)){
//				//formulario.setOpLocalResponsable(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//				formulario.setOpStock(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
//			}
//			
//			/**********************Por defecto la fecha minima de entrega es la fecha de entrega************/
//			LogSISPE.getLog().info("fecha inicial entrega minima: {}" , session.getAttribute(CotizarReservarAction.FECHA_ENTREGA));
//			
//			//Guardo la fecha minima de entrega
//			//session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA))));
//			formulario.setBuscaFecha(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES).toString());
//			session.setAttribute(FECHABUSQUEDA, ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
//			
//			//por defecto la fecha de entrega es igual a la fecha minima de entrega es la actual
//			session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
//			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//			/**********************Elimina variables de session***************************/
//			session.removeAttribute(LOCALID);//id del local seleccionado
//			session.removeAttribute(DIASELECCIONADO);//dia de despacho seleccionado en el calendario
//			session.removeAttribute(CALENDARIODIALOCALCOL);//calendario
//			session.removeAttribute(VISTALOCALCOL);//locales
//			session.removeAttribute(CALENDARIODIALOCAL);//CD del dia seleccionado en el calendario
//			session.removeAttribute(FECHAMINIMA);//fecha minima de despacho a locales
//			session.removeAttribute(FECHAMAXIMA);//fecha maxima de despacho a locales
//			session.removeAttribute(MES_ACTUAL_CALENDARIO);
//			session.removeAttribute(SECTORSELECCIONADO);//indice local seleccionado
//			session.removeAttribute(DIRECCION);
//			session.removeAttribute(EXISTELUGARENTREGA);
//			session.removeAttribute(SELECCIONARLOCAL);
//			session.removeAttribute(SELECCIONARCALENDARIO);
//			session.removeAttribute(HABILITARCANTIDADENTREGA);
//			session.removeAttribute(HABILITARCANTIDADRESERVA);
//			session.removeAttribute(HABILITARBOTONACEPTAR);
//			session.removeAttribute(HABILITARDIRECCION);
//			session.removeAttribute("ventanaLocalEntrega");//elimina la variable que abre la ventana de seleccion de local de entrega
//			//PASO UNO DE LA AYUDA
//			session.setAttribute(CONFIGURACIONCARGADA, "ok");
//			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
//			session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
//			
//			// Objetos para construir los tabs, si fue modificacion de reserva
//			beanSession.setPaginaTab(WebSISPEUtil.construirTabsConfiguracionEntregas(request, formulario));
//			
//			if((String)session.getAttribute(EXISTENENTREGAS)==null){
//				LogSISPE.getLog().info("--- abrir Popup configuracion si no existen aun entregas configuradas---");
//				// Objetos para construir los tabs, si fue modificacion de reserva						
//				request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
//				request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
//				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
//				instanciarVentanaOpcionesConfiguracion(request);
//				session.setAttribute(MOSTRAR_POPUPTAB, "ok");
//				session.removeAttribute(HABILITARCANTIDADENTREGA);
//				session.removeAttribute(HABILITARCANTIDADRESERVA);
//				session.removeAttribute(CotizarReservarAction.POPUPAUTORIZACIONENTREGAS);
//				session.setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
//				session.removeAttribute("ec.com.smx.sic.pedido.numeroAutorizacion");
//				session.removeAttribute(PASOSPOPUP);
//				request.removeAttribute(CONFIRMACIONANTERIORCONFENTREGAS);
//				session.removeAttribute( POSICIONDIVCONFENTREGAS);
//				session.removeAttribute(DIRECCIONESAUX);
//				regresarvaloresInicialesExistenEntregas(session);
//				
//				formulario.setOpElaCanEsp(null);
//				formulario.setOpLugarEntrega(null);
//				formulario.setOpTipoEntrega(null);
//				formulario.setOpStock(null);
//				
//				//para mostrar opcion de canastos especiales
//				Collection detallePedidoDTOColPopup=(Collection)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//				////formulario.setOpElaCanEsp(null);
//				if(CotizacionReservacionUtil.verificarCanastasEspeciales(detallePedidoDTOColPopup, request)){
//					session.setAttribute(MOSTRAROPCIONCANASTOSESPECIALES, "ok");
//				}				
//			}
//			
//		}
//		//FIN EL ELSE POR FIN
//		
//			/**************************Refresca los valores de las variables de formulario***********************/		
//			if(session.getAttribute(CALENDARIODIALOCALCOL)!=null)
//				formulario.setCalendarioDiaLocal((Object[])session.getAttribute(CALENDARIODIALOCALCOL));
//			
//			if(session.getAttribute(FECHABUSQUEDA)!=null){
//				LogSISPE.getLog().info("setea fecha");
//				if(session.getAttribute(EDITAFECHAMINIMA)!=null){
//					formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//				}else{
//					if(formulario.getOpStock()!=null && !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
//						formulario.setBuscaFecha(DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA)));
//					}else{
//						formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
//					}
//				}
//			}
//
//			if(session.getAttribute(VISTALOCALDESTINO)!=null){
//				VistaLocalDTO vistaLocalDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//				formulario.setIdLocalOSector(vistaLocalDTO.getId().getCodigoLocal().toString());
//				formulario.setLocalOSector(vistaLocalDTO.getNombreLocal());
//			}
//			if(session.getAttribute(CALENDARIODIALOCAL)!=null){
//				CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
//				LogSISPE.getLog().info("setea fechaDespacho {}"+DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//				formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//			}
//			//Instancio la primera vez la ventana de configuracion
////			if(session.getAttribute(CotizarReservarAction.MOSTRAR_CONFIGURACION) == null ){
////				instanciarConfiguracionEntregas(request);
////				session.setAttribute(CotizarReservarAction.MOSTRAR_CONFIGURACION, "ok");
////			}
//
//			//Grabo los mensajes para que se desplieguen
//			saveMessages(request,messages);
//			saveInfos(request,info);
//			LogSISPE.getLog().info("error: {}" , error.size());
//			LogSISPE.getLog().info("errors: {}" , errors.size());
//			if(error.size()>0){
//				LogSISPE.getLog().info("graba error");
//				saveErrors(request,(ActionMessages)error);
//			}
//			if(errors.size()>0){
//				LogSISPE.getLog().info("graba errors");
//				saveErrors(request,errors);
//			}
//			saveWarnings(request,warnings);
//
//			LogSISPE.getLog().info("salida EntregasCalendario4: {}" , forward);
//
//			return mapping.findForward(forward);
//		
//	}
//
//
//
//	/**
//	 * @param request
//	 * @param session
//	 * @param errors
//	 * @param formulario
//	 * @param detallePedidoDTOCol
//	 * @throws Exception
//	 * @throws SISPEException
//	 */
//	private Boolean validarCapacidadBodega(HttpServletRequest request,
//			HttpSession session, ActionMessages errors,
//			CotizarReservarForm formulario, Collection detallePedidoDTOCol)
//			throws Exception, SISPEException {
//		Boolean existeError=Boolean.TRUE;
//		if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) &&  formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//			//recupero los datos del detalle del pedido
//			LocalID localID=new LocalID();
//			localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			localID.setCodigoLocal(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos")).intValue());
//			Date mesSeleccionado= (Date)session.getAttribute(MESSELECCIONADO);
//			obtenerCalendarioOtroLocal(session, request, localID, errors, mesSeleccionado, null,null, formulario, null);
//			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLBODEGA);
//			
//			GregorianCalendar fechaSinHora = new GregorianCalendar();
//			fechaSinHora.setTime(ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()));
//			GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
//			fechaSinHoraAux.add(Calendar.DAY_OF_MONTH, -1);
//			
//			int diaSeleccionado= fechaSinHoraAux.get(Calendar.DAY_OF_MONTH);
//			CalendarioDiaLocalDTO calendarioDiaLocalBodegaDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
//			int numeroBultos=0;
//			for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()>0){
//					numeroBultos = numeroBultos+UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue(), detallePedidoDTO.getArticuloDTO());
//				}
//			}
//			//Obtengo la configuracion del dia seleccionado
//			CalendarioDiaLocalDTO calendarioDiaLocalActual=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalBodegaDTO.getId(),null);
//			if((new Long(ConverterUtil.fromDoubleToInteger(calendarioDiaLocalActual.getCantidadDisponible()))).longValue()<numeroBultos){
//				session.setAttribute("ec.com.smx.sic.sispe.validar.bodega","ok");
//				 existeError=Boolean.FALSE;
//				//Si no existe la capacidad verifica si tiene autorizacion
//				GregorianCalendar horaDespacho=new GregorianCalendar();
//				horaDespacho.setTime(calendarioDiaLocalActual.getId().getFechaCalendarioDia());
//				if(!calendarioDiaLocalActual.getId().getCodigoLocal().toString().equals((SessionManagerSISPE.getCurrentLocal(request)).toString())){
//					AutorizacionDTO autorizacionDTO = buscaAutorizacion(session,(SessionManagerSISPE.getCurrentLocal(request)).toString(),new Timestamp(horaDespacho.getTime().getTime()));
//					if(autorizacionDTO==null){
//						errors.add("cantidadMayor",new ActionMessage("errors.capacidadDisponibleBodega",numeroBultos,calendarioDiaLocalActual.getId().getCodigoLocal()));
//					}
//					else{
//						 session.removeAttribute("ec.com.smx.sic.sispe.validar.bodega");
//						 existeError=Boolean.TRUE;
//					}
//				}
//			}
//		}
//		return existeError;
//	}
//
//
//
//	@SuppressWarnings({ "unused", "rawtypes" })
//	private void entidadResponsablePedido(HttpSession session){		
//		
//		Collection responsablesPedidoCol = (Collection)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
//		if(responsablesPedidoCol !=null && !responsablesPedidoCol.isEmpty()){
//			for (Iterator<EstructuraResponsable> it = responsablesPedidoCol.iterator();it.hasNext();) {
//				EstructuraResponsable estructuraResponsable = it.next();
//				if(!estructuraResponsable.getResponsablePedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.responsable.pedido.local"))){
//					session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
//					break;
//				}
//				session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
//			}
//		}
//		
//		
//	}
//	
//	private void regresarvaloresInicialesExistenEntregas(HttpSession session) {
//		// se procesa para regresar a los valores normales las entregas parciales
//		
//			long cantidad=0L;
//			long cantidadEntregada=0L;
//			Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//			for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//				for (Iterator numeroEntregas = detallePedidoDTO.getEntregas().iterator();numeroEntregas.hasNext();) {
//					EntregaDTO entregaDTO = (EntregaDTO) numeroEntregas.next();
//					cantidadEntregada = cantidadEntregada + entregaDTO.getCantidadEntrega();
//				}
//				cantidad = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - cantidadEntregada;
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantidad);
//				cantidadEntregada = 0L;
//			}
//			
//		
//	}
//
//	
//
//	private void mantenerValoresMensajesPasosNavegacionPopUP(
//			HttpSession session, CotizarReservarForm formulario,String numPaso) {
//		//mensajes de los pasos
//		if(formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3"));
//		}else{
//			if((String)session.getAttribute(COMBOSELECCIONCIUDAD)==null){
//				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
//			}
//			else{
//				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.2"));
//			}
//		}
//		session.setAttribute(PASO, MessagesWebSISPE.getString(numPaso));
//		if((String)session.getAttribute(HABILITARDIRECCION)!=null && (String)session.getAttribute("siDireccion")!=null){
//			//AYUDA
//			session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
//			//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//			if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))))  ){
//				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso4"));
//			}
//			else{
//				session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//			}			
//		}
//		//fin mensajes de los pasos
//	}
//
//
//
//	/**
//	 * @param session
//	 */
//	private void obtenerResponsableEntrega(HttpSession session,DetallePedidoDTO detPed,EntregaDTO entrega) {
//		Collection<EstructuraResponsable> detalleEstructuraCol = (ArrayList<EstructuraResponsable>)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
//		if(!CollectionUtils.isEmpty(detalleEstructuraCol)){
//			for(EstructuraResponsable resEnt:detalleEstructuraCol){
//				ArticuloDTO artPed=resEnt.getDetallePedidoDTO().getArticuloDTO();
//				EntregaDTO entPed=resEnt.getEntregaDTO();
//				if(artPed!=null && entPed!= null){
//							Long diferencia= entPed.getFechaEntregaCliente().getTime()-entrega.getFechaEntregaCliente().getTime();
//							if(diferencia==0L && entPed.getDireccionEntrega().toUpperCase().trim().equals(entrega.getDireccionEntrega().toUpperCase().trim())){
//								if(entPed.getCodConResDes().intValue()==entrega.getCodConResDes().intValue() && entPed.getCodConResEnt().intValue()==entrega.getCodConResEnt().intValue() && entPed.getCodConResPro().intValue()==entrega.getCodConResPro().intValue()){
//									if(detPed.getArticuloDTO().getId().getCodigoArticulo().equals(artPed.getId().getCodigoArticulo()) && detPed.getNpReponsable()==null){
//										detPed.setNpReponsable(resEnt);
//										break;
//									}
//								}
//							}
//						}
//			}
//		}
//	}
//
//
//	
//	private void anteriorConfiguracionEntregas(HttpSession session, BeanSession beanSession,String etiquetaOK,String etiquetaCancel,String accionOK,String accionCancel,Integer numTab,String claseBotonOK,String claseBotonCancel,String mensajeAccion,String imgNumMensajes){
//		regresarPopUp(session, beanSession,etiquetaOK,etiquetaCancel,accionOK,accionCancel,numTab,claseBotonOK,claseBotonCancel);
//		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString(mensajeAccion));
//		session.setAttribute(PASO, MessagesWebSISPE.getString(imgNumMensajes));
//	}
//
//	private String tabHabilitadosParciales(HttpServletRequest request,
//			String tabHabilitados) {
//		if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
//			tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA);
//		}
//		else{
//			if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA)!=null){
//				tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA);
//			}
//		}
//		return tabHabilitados;		
//	}
//
//
//	/**
//	 * permite regresar a los valores iniciales cuando esta navegando en el popup de configuracion de entregas
//	 * @param HttpSession session
//	 */
//	private void regresarValoresIniciales(HttpSession session) {
//		Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//		for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//			DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//			if(detallePedidoDTO.getEntregas().size() == 0){
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
//			}					
//		}
//	}
//
//
//
//	private static void asignacionValoresFormulario(HttpSession session,CotizarReservarForm formulario) {
//		
//		if((String)session.getAttribute(OPCIONLUGARENTREGA)!=null && formulario.getOpLugarEntrega()==null){
//			formulario.setOpLugarEntrega((String) session.getAttribute(OPCIONLUGARENTREGA));
//		}
//		
//		if((String)session.getAttribute((OPCIONTIPOENTREGA))!=null && formulario.getOpTipoEntrega()==null){
//			formulario.setOpTipoEntrega((String) session.getAttribute(OPCIONTIPOENTREGA));
//		}
//		
//		if((String)session.getAttribute(OPCIONSTOCK)!=null && formulario.getOpStock()==null){
//			formulario.setOpStock((String) session.getAttribute(OPCIONSTOCK));
//		}
//		if((String)session.getAttribute(OPCIONCANASTOSESPECIALES)!=null && formulario.getOpElaCanEsp()==null){
//			formulario.setOpElaCanEsp((String)session.getAttribute(OPCIONCANASTOSESPECIALES));
//		}
//	}
//	
//
//	private void regresarPopUp(HttpSession session, BeanSession beanSession,String etiquetaOk,String etiquetaCANCEL,String accionOk,String accionCANCEL,Integer numTab,String estiloOK,String estiloCANCEL) {
//		
//		UtilPopUp popUpConfEntregas = (UtilPopUp) session.getAttribute(SessionManagerSISPE.POPUP);
//					
//		popUpConfEntregas.setTituloVentana("Configuraci\u00F3n de entregas");
//		popUpConfEntregas.setFormaBotones(UtilPopUp.OK_CANCEL);
//		popUpConfEntregas.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//		popUpConfEntregas.setEtiquetaBotonOK(etiquetaOk);
//		popUpConfEntregas.setEtiquetaBotonCANCEL(etiquetaCANCEL);
//		
//		popUpConfEntregas.setEstiloOK(estiloOK);
//		popUpConfEntregas.setEstiloCANCEL(estiloCANCEL);
//		
//		popUpConfEntregas.setValorOK("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp','pregunta2'], {parameters: '"+accionOk+"=ok',popWait:true, evalScripts:true});");//entregas
//		
//		if(numTab.equals(0)){
//			popUpConfEntregas.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//			popUpConfEntregas.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
//		}
//		else{
//			if(numTab.equals(1)){
//				popUpConfEntregas.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','mensajesPopUp'], {parameters: '"+accionCANCEL+"=ok',popWait:true, evalScripts:true});");//atrasPopUpEntregas
//			}
//			
//		}
//		
//		popUpConfEntregas.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//		//popUpConfEntregas.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
//		popUpConfEntregas.setAncho(75D);
//		popUpConfEntregas.setTope(-150D);
//		session.setAttribute(SessionManagerSISPE.POPUP, popUpConfEntregas);
//		
//		
//		//cambiar al tab2Cuando presiono en siguiente
//		LogSISPE.getLog().info("Se elige el tabde configuracion Opcion1");
//		WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, numTab);
//		
//		popUpConfEntregas = null;
//	}
//
//
//
//	private void navegacionPopUp(HttpSession session, BeanSession beanSession,String etiquetaOk,String etiquetaCancel,String nomPagOk,String nomPagCancel, String parOk,String paraCancel,String modal,String seccionMensajes,int numTab,String estiloOK,String estiloCANCEL) {
//		
//		//cambio de valores de popUp
//		UtilPopUp popUpCal = (UtilPopUp) session.getAttribute(SessionManagerSISPE.POPUP);
//		
//		popUpCal.setEtiquetaBotonOK(etiquetaOk);
//		popUpCal.setEtiquetaBotonCANCEL(etiquetaCancel);
//		
//		popUpCal.setEstiloOK(estiloOK);
//		popUpCal.setEstiloCANCEL(estiloCANCEL);
//		
//		popUpCal.setValorOK("requestAjax('"+nomPagOk+"', ['popupConfirmar','entregas','mensajesPopUp','pregunta2'], {parameters: '"+parOk+"=ok',popWait:true, evalScripts:true});");
//		popUpCal.setValorCANCEL("requestAjax('"+nomPagCancel+"', ['popupConfirmar',"+seccionMensajes+",'reserva','reservaCab','resumenEntregas'], {parameters: '"+paraCancel+"=ok',popWait:true, evalScripts:true});"+modal);
//		popUpCal.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//		
//		session.setAttribute(SessionManagerSISPE.POPUP, popUpCal);
//		
//		//cambiar al tab2Cuando presiono en siguiente
//		LogSISPE.getLog().info("Se elige el tabde configuracion Opcion2");
//		WebSISPEUtil.cambiarTabPopUpConfiguracionesEntregas(beanSession, numTab);
//		
//		popUpCal = null;
//		
//	}
//	
//	
//	
//	/**
//	 * Funcion que realiza la configuracion de la pantalla de entregas de acuerdo a las opciones seleccionadas
//	 * @param formulario
//	 * @param request
//	 */
//	public static void obtenerConfiguracionEntregas(CotizarReservarForm formulario, HttpServletRequest request, ActionMessages errors){
//		HttpSession session=request.getSession();
//		//Borro las variables de session
//		session.removeAttribute(SELECCIONARLOCAL);
//		session.removeAttribute(SELECCIONARCALENDARIO);
//		session.removeAttribute(HABILITARCANTIDADENTREGA);
//		session.removeAttribute(HABILITARCANTIDADRESERVA);
//		session.removeAttribute(HABILITARBOTONACEPTAR);
//		session.removeAttribute(HABILITARDIRECCION);
//		//session.removeAttribute(DIASELECCIONADO);//Si se quiere que la parte de direcciones no desaparezca borrar esta linea
//		session.removeAttribute(CALENDARIODIALOCALCOL);
//		//session.removeAttribute(COMBOSELECCIONCIUDAD);
//		session.removeAttribute("siDireccion");
//
//		//Variable para mostrar la seccion de configuracion de fechas, hora y local
//		session.setAttribute(EXISTELUGARENTREGA, "ok");
//		//AYUDA
//		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
//		session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//		//Registro el lugar de entrega
//		if((formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))))// ||
//				//(formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) && formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")))  ||
//				//(formulario.getOpLugarEntrega()!=null && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")) && formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))
//			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"));
//		else{
//			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
//			session.removeAttribute(DIASELECCIONADO);
//			formulario.setSeleccionCiudad("");
//		}
//		LogSISPE.getLog().info("lugar entrega: {}" , formulario.getOpLugarEntrega());
//		LogSISPE.getLog().info("toma stock: {}" , formulario.getOpStock()); 
//		//Caso en el que se debe habilitar la seleccion de locales
//		//if((!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) && formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && !formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"))) || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")))){
//		if(!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) && 
//				!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")) && 
//				!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){// &&
//				//formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
//			LogSISPE.getLog().info("va a habilitar la seleccion de lo locales");
//			//se realiza la consulta de los locales
//			if(session.getAttribute(SessionManagerSISPE.COLECCION_LOCALES)==null)
//				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
//			session.setAttribute(SELECCIONARLOCAL, "ok");
//			//borro el calendario
//			session.removeAttribute(CALENDARIODIALOCALCOL);
//		}
//		//Caso en el que se debe habilitar la seleccion del calendario
//		LogSISPE.getLog().info("OpStock: {}",formulario.getOpStock());
//		//jmena if((!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) && formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))) || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && !formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))){
//		//if((!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")) && formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))) || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")))){
//		if((!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))){
//			LogSISPE.getLog().info("asigna variable de sesion que visualiza el calendario");
//			session.setAttribute(SELECCIONARCALENDARIO, "ok");
//			//jmena validacion para el calendario de bodega
//			//if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
//				//	formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//			if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				//AYUDA				
//				if(( (formulario.getOpTipoEntrega()!=null &&  formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP) == null){
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				}
//				else{
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				}
//				if((String)session.getAttribute(PASOSPOPUP)!=null){
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//				}
//			}else{
//				//AYUDA 
//				
//				if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP) == null ){					
//					session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.1.1"));
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				}
//				else{					
//					if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//							session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEntAux2.2"));
//							session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//						}else{
//							session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
//							session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//						}
//				}
//				if((String)session.getAttribute(PASOSPOPUP)!=null){
//					if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//						session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEntAux2.2"));
//						session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//					}else{
//						session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.2"));
//						session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//					}
//				}
//				
//			}
//			session.removeAttribute(DIASELECCIONADO);
//			formulario.setSeleccionCiudad("");
//		}
//		//Caso en el que se debe habilitar las cantidades de entregas
//		if(formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))){
//			session.setAttribute(HABILITARCANTIDADENTREGA, "ok");
//			//se obtiene el bean que contiene los campos genericos
//			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
//			if(!beanSession.getPaginaTabPopUp().esTabSeleccionado(2)){
//				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
//			}
//			session.setAttribute(PASOSPOPUP, "ok");
//			
//		}
//		//Caso en el que se debe habilitar las cantidades de recepcion
//		if(formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
//			session.setAttribute(HABILITARCANTIDADRESERVA, "ok");
//			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
//			if(!beanSession.getPaginaTabPopUp().esTabSeleccionado(2)){
//				beanSession.setPaginaTabPopUp(WebSISPEUtil.construirTabsPopUpConfEnt(request, formulario));
//			}
//			session.setAttribute(PASOSPOPUP, "ok");
//		}
//		//Caso en el que se habilita la seccion de direcciones
//		LogSISPE.getLog().info("opLugarEntrega: "+formulario.getOpLugarEntrega());
//		if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//			LogSISPE.getLog().info("la entrega es a domicilio");
//			session.setAttribute(HABILITARDIRECCION, "ok");
//			//si se da el caso de entregas directas al domicilio desde la bodega o local visualiza directamente las direcciones sin seleccionar calendario
//			//if((formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))) || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")))){
//			//if((formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))))){
//			if((formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))){
//				LogSISPE.getLog().info("visualiza las direcciones");
//				if(session.getAttribute(DIASELECCIONADO)!=null && !String.valueOf(session.getAttribute(DIASELECCIONADO)).equals("ok")){
//					int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
//					session.setAttribute(DIASELECCIONADOAUX,diaSeleccionado);
//				}else{
//					session.setAttribute(DIASELECCIONADO, "ok");
//				}
//				formulario.setSeleccionCiudad("");
//				//AYUDA
//				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt2.3.1"));
//				//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock() !=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//				}
//				else{
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				}
//			}
//			//Si va a ser la entrega a domicilio desde el CD
//			//jmena if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//			//if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) &&
//			if(!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				session.setAttribute(COMBOSELECCIONCIUDAD, "ok");
//				if(session.getAttribute(VISTAESTABLECIMIENTOCIUDADLOCAL)==null)
//					cargaCiudades(request, errors);
//				//AYUDA
//				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePaso2.3.2"));
//				//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				if(( (formulario.getOpTipoEntrega() !=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))) && (String)session.getAttribute(PASOSPOPUP)==null ){
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//				}
//				else{
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso2"));
//				}
//			}
//		}
//		//Caso en el que se habilita el boton aceptar
//		LogSISPE.getLog().info("opLugarEntrega: {}",formulario.getOpLugarEntrega());
//		LogSISPE.getLog().info("opTipoEntrega: {}" , formulario.getOpTipoEntrega());
////		if (!((formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) 
////				&& formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) 
////				|| (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) 
////						&& formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))))) {
//		if (!((formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) 
//				|| (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))))) {
//			session.setAttribute(HABILITARBOTONACEPTAR, "ok");
//		}
//		
//		try{
//			boolean bodegaCan = false;
//			//Caso en que se solicita algo al CD
//			if(!formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))){
//				//jmena configura para el calendario de la bodega(99)
////				if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))
////						&& formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//					session.setAttribute(EDITAFECHAMINIMA, "OK");
//					bodegaCan = true;
//				}
//				//si el responsable es el CD 
////				if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))
////						&& !formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//				if(!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//					Date fechaMinima=(Date)session.getAttribute(FECHABUSQUEDA);
//					LogSISPE.getLog().info("fecha Minima: {}" , fechaMinima);
//					formulario.setBuscaFecha(ConverterUtil.parseDateToString(fechaMinima));
//					//session.setAttribute(EDITAFECHAMINIMA, "OK");
//				}else if(!bodegaCan){
//					//si el responsable es el local
//					formulario.setBuscaFecha(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_LOC_RES).toString());
//					session.setAttribute(FECHABUSQUEDA, ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
//					session.removeAttribute(EDITAFECHAMINIMA);
//				}
//				
//			}else{
//				formulario.setBuscaFecha(ConverterUtil.parseDateToString(new Date()));
//				session.removeAttribute(EDITAFECHAMINIMA);
//			}
//			if(formulario.getFechaEntregaCliente()!=null && !formulario.getFechaEntregaCliente().equals("") && (ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente())<=0.0)){
//				formulario.setFechaEntregaCliente(formulario.getBuscaFecha());
//			}
//		}catch(Exception e){
//			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//		}
//
//				
//		session.removeAttribute(CONFIGURACIONCARGADA);
//		
//		//Guarda la ultima configracion de la entrega seleccionada
//		session.setAttribute(OPCIONLUGARENTREGA,formulario.getOpLugarEntrega());
//		session.setAttribute(OPCIONTIPOENTREGA,formulario.getOpTipoEntrega());
//		session.setAttribute(OPCIONSTOCK,formulario.getOpStock());
//		session.setAttribute(OPCIONCANASTOSESPECIALES, formulario.getOpElaCanEsp());
//		
//		//Guardo la fecha de entrega al cliente en sesion
//		session.setAttribute(FECHAENTREGACLIENTE, formulario.getFechaEntregaCliente());
//
//	}
//
//	/**
//	 * obtiene los datos del local destino para cargar el calendario
//	 * @param session
//	 * @param request
//	 * @param localID
//	 * @param errors
//	 * @throws Exception
//	 */
//
//	public static void obtenerLocal(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,CotizarReservarForm formulario) throws Exception
//	{
//		session.setAttribute(LOCALID, localID);
//		
//		LogSISPE.getLog().info("fecha de busqueda: formulario.getFechaEntregaCliente() {}" , formulario.getFechaEntregaCliente());
//		LogSISPE.getLog().info("fecha de busqueda: (String)(session.getAttribute(FECHAENTREGACLIENTE))() {}" , (String)(session.getAttribute(FECHAENTREGACLIENTE)));
//		
//		if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
//			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//		}
//		Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		
//		LogSISPE.getLog().info("fecha de busqueda: {}" , mes);
//		//jmena bandera que muestra o no la configuracion del local bodega.
//		session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
//		String rangoDias = (String)session.getAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO);
//		if(rangoDias == null){
//			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
//			ParametroDTO consultaParametroDTO = new ParametroDTO();
//			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
//			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
//			
//			rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
//			session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
//			consultaParametroDTO = null;
//		}
//		
//		int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
//		LogSISPE.getLog().info("parametro: {}" , parametro);
//		
//		//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
//		Date fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		GregorianCalendar fechaCalendario = new GregorianCalendar();
//		fechaCalendario.setTime(fechaMinima);
//		fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
//		fechaMinima = fechaCalendario.getTime();
//		
//		//------ fmunoz -------
//		//se obtiene el par\u00E1metro desde la base
//		ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
//		int canDiaFecMinDes = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
//		Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, canDiaFecMinDes, 0, 0, 0, 0));
//		
//		//si la fecha m\u00EDnima es menor o igual a hoy
//		if(fechaCalendario.getTimeInMillis() < fechaMinimaReferencia.getTime()){
//			fechaMinima = fechaMinimaReferencia;
//		}
//		
//		//Resto al dia de entrega un d\u00EDa para ver la fecha m\u00E1xima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
//		Date fechaMaxima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		GregorianCalendar fechaCalendario1=new GregorianCalendar();
//		fechaCalendario1.setTime(fechaMaxima);
//		fechaCalendario1.add(Calendar.DAY_OF_MONTH,-1);
//		fechaMaxima=fechaCalendario1.getTime();
//		
//		session.removeAttribute(CALENDARIODIALOCAL);
//		session.removeAttribute(DIASELECCIONADO);
//		
//		//jmena verifico si es el calendario de la bod/canastos
//		if(localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//			GregorianCalendar fechaMaximaDomicilio = new GregorianCalendar();
//			fechaMaximaDomicilio.setTime(new Date());
//			fechaMaximaDomicilio.set(GregorianCalendar.MONTH, 12);
//			fechaMaximaDomicilio.set(GregorianCalendar.DAY_OF_MONTH, 31);
//			fechaMaxima = new Date(fechaMaximaDomicilio.getTimeInMillis());
//			fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
//			//Inicializo la fecha de entrega null
//			formulario.setFechaEntregaCliente(null);
//			request.getSession().setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//			request.getSession().getAttribute(BANDERA_CONFIGURA_CAL_BOD);
//			//session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//		}
//		
//		long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(fechaMaxima));
//		if(diferenciaEntregaBusca<0.0){
//			LogSISPE.getLog().info("Mensaje rango de fechas con errores");
//			errors.add("rangoFechas",new ActionMessage("errors.rangoInicialFinalFechasCalendario",parametro,canDiaFecMinDes));
//		}else{
//			obtenerCalendario(session,request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}
//		
//		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
//		LogSISPE.getLog().info("fechaMaxima: {}" , fechaMaxima);
//		session.setAttribute(FECHAMINIMA,fechaMinima);
//		session.setAttribute(FECHAMAXIMA,fechaMaxima);
//		
//
//	}
//	/**
//	 * obtiene datos del calendario
//	 * @param session
//	 * @param request
//	 * @param localID
//	 * @param errors
//	 * @param mes
//	 * @throws Exception
//	 */
//
//	public static void obtenerCalendario(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechaMinima,Date fechaMaxima,CotizarReservarForm formulario, Boolean verCalendario) throws Exception
//	{
//		try{
//			LogSISPE.getLog().info("*****entra a cargar el calendario*****");
//			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
//			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
//			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
//			LogSISPE.getLog().info("calendarioConfiguracionDiaLocalDTO: {}" , session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX));
//			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
//			List calendarioDiaLocalDTOCol;// = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
//			try{
//				calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
//				//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
//				LogSISPE.getLog().info("minima: {}" , fechaMinima);
//				LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
//				Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
//				session.setAttribute(CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
//				//subo a session el mes de busqueda
//				session.setAttribute(MESSELECCIONADO,mes);
//				//calculo cuantas semanas tiene el mes
//				int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
//				LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
//				//subo a sesion el numero de semanas
//				session.setAttribute(NUMEROSEMANAS,new Integer(maximoSemanas));
//			}catch (Exception e) {
//				if(errors != null)
//					errors.add("localSinCalendario",new ActionMessage("warnings.configuracion.plantilla.por.defecto",localID.getCodigoLocal()));			
//			} 
//			
//		}catch(Exception e){
//			LogSISPE.getLog().info("error al cargar calendario: {}" , e.getStackTrace());  
//			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
//		}
//	}
//	
//	/**
//	 * obtiene datos del calendario de la bodega 97 o 99
//	 * @param session
//	 * @param request
//	 * @param localID
//	 * @param errors
//	 * @param mes
//	 * @throws Exception
//	 */
//
//	public static void obtenerCalendarioOtroLocal(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechaMinima,Date fechaMaxima,CotizarReservarForm formulario, Boolean verCalendario) throws Exception
//	{
//		try{
//			LogSISPE.getLog().info("*****entra a cargar el calendario*****");
//			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
//			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
//			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
//			List calendarioDiaLocalDTOCol;// = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
//			try{
//				calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,null, null);
//				//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
//				LogSISPE.getLog().info("minima: {}" , fechaMinima);
//				LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
//				Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
//				session.setAttribute(CALENDARIODIALOCALCOLBODEGA,calendarioDiaLocalDTOOBJ);
//				//subo a session el mes de busqueda
//				session.setAttribute(MESSELECCIONADOBODEGA,mes);
//				//calculo cuantas semanas tiene el mes
//				int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
//				LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
//				//subo a sesion el numero de semanas
//				session.setAttribute(NUMEROSEMANASBODEGA,new Integer(maximoSemanas));
//			}catch (Exception e) {
//				if(errors != null)
//					errors.add("localSinCalendario",new ActionMessage("warnings.configuracion.plantilla.por.defecto",localID.getCodigoLocal()));			
//			} 
//			
//		}catch(Exception e){
//			LogSISPE.getLog().info("error al cargar calendario: {}" , e.getStackTrace());  
//			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
//		}
//	}
//	/**
//	 * Seleccion de dias del mes
//	 * @param formulario
//	 * @param dia               dia individual, semana, o dia de la semana seleccionado en el calendario
//	 * @param session
//	 * @param seleccionado		dice si los dias van a ser seleccionados o no		
//	 * @param indice            dice cual va a ser el incremento entre dias ej. si se seleccion una semana deben seleccionarse los dias sumandole 7
//	 * @param limite         	dice cuantos dias van a ser seleccionados   
//	 */
//	private static void seleccionDia(CotizarReservarForm formulario, int dia,HttpSession session,boolean seleccionado,ActionMessages errors, HttpServletRequest request) throws Exception{
//		LogSISPE.getLog().info("entra a seleccionar los dias: " + dia);
//		Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//		//jmena bandera para saber si configura para un(cal. bodega o un cal. local)
//		boolean flag = false;
//		boolean calBodega = false;
//		//dia seleccionado
//		int diaSeleccionado=0;
//		CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia];
//		LogSISPE.getLog().info("seleccionado: {}" , calendarioDiaLocalDTO.getNpEsSeleccionado());
//		LogSISPE.getLog().info("fechaCalendario: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
//		//fecha minima
//		Date fechaMinima=(Date)session.getAttribute(FECHAMINIMA);
//		/*****************************************************************************************/
//		//fecha maxima
//		Date fechaMaxima=(Date)session.getAttribute(FECHAMAXIMA);
//		/*****************************************************************************************/
//		//Diferencia entre fecha minima y fecha seleccionada
//		long diferenciaFechasMinima= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//		//Diferencia entre fecha de entrega y fecha seleccionada
//		long diferenciaFechasEntrega= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()),ConverterUtil.parseDateToString(fechaMaxima));
//		LogSISPE.getLog().info("diferenciaFechasMinima{}" , diferenciaFechasMinima);
//		LogSISPE.getLog().info("diferenciaFechasEntrega{}" , diferenciaFechasEntrega);
//		
//		VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//		
//		asignacionValoresFormulario(session, formulario);
//		
//		//jmena valida si es el calendario de bodega
////		if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////				formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && vistaLocalDestinoDTO != null &&
////				vistaLocalDestinoDTO.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//		/*if(((formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")))&&
//				(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && vistaLocalDestinoDTO != null &&
//				vistaLocalDestinoDTO.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))))){*/
//				
//			
//			if(vistaLocalDestinoDTO != null && vistaLocalDestinoDTO.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//				if(formulario.getOpElaCanEsp() != null){
//					if((formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
//					   (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
//					   (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||	
//					   (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")))) ){
//						calBodega = true;
//						Date fechaMinEnt = ConverterUtil.parseStringToDate(formulario.getBuscaFecha());
//						//Verifico si la fecha eccionada en el cal es mayor o igual a la de entrega.
//						if(calendarioDiaLocalDTO.getId().getFechaCalendarioDia().getTime() >= fechaMinEnt.getTime()){
//							formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//							session.setAttribute(FECHAENTREGACLIENTE,formulario.getFechaEntregaCliente());
//							//AYUDA
//							if((formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||	
//									   (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")))) ){
//								session.setAttribute(HABILITARDIRECCION, "ok");
//								session.setAttribute(COMBOSELECCIONCIUDAD, "ok");
//								session.setAttribute(FECHAENTREGACLIENTE,formulario.getFechaEntregaCliente());
//								//formulario.setSeleccionCiudad("");
//								if(session.getAttribute(VISTAESTABLECIMIENTOCIUDADLOCAL)==null)
//									cargaCiudades(request, errors);
//							}
//							
//						}else{
//							errors.add("fechaSeleccionada", new ActionMessage("errors.fechaSeleccionadaMinimaCalBod"));
//							session.removeAttribute(HABILITARDIRECCION);
//							//session.removeAttribute(PASO);
//							session.removeAttribute("siDireccion");
//							formulario.setFechaEntregaCliente(null);
//							flag = true;
//						}
//					}
//				}
//			}
//		
//		//Compara si la fecha elegida es mayor o igual a la fecha de busqueda
//		if((diferenciaFechasMinima<0.0 || diferenciaFechasEntrega<0.0) && !flag && !calBodega){
//			LogSISPE.getLog().info("error de fechas");
//			if(diferenciaFechasMinima<0.0)
//				errors.add("fechaMinima", new ActionMessage("errors.fechaSeleccionadaMinima"));
//			if(diferenciaFechasEntrega<0.0)
//				errors.add("fechaEntrega", new ActionMessage("errors.fechaSeleccionadaEntrega",ConverterUtil.parseDateToString(fechaMaxima)));
//		}else if(!flag){
//			LogSISPE.getLog().info("va a seleccionar la fecha");
//			//jmena verifico de que tipo es
//			if(session.getAttribute(DIASELECCIONADO)!=null && session.getAttribute(DIASELECCIONADO) instanceof Integer){
//				//diaSeleccionado=((Integer)session.getAttribute(DIASELECCIONADO)).intValue();Aqui
//				diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADO)));
//				//Recupero el dia seleccionado anteriormente
//				CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
//				calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
//			}else if(session.getAttribute(DIASELECCIONADO)!=null && session.getAttribute(DIASELECCIONADO) instanceof String){
//				diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(DIASELECCIONADOAUX)));
//				//Recupero el dia seleccionado anteriormente
//				CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
//				calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
//				session.removeAttribute(DIASELECCIONADO);
//				session.removeAttribute(DIASELECCIONADOAUX);
//			}
//			//Cargo los dias a los que les debo modifcar la cantidad acumulada
//			cargaDiasModificarCA(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()), formulario, request, dia);
//			session.setAttribute(DIASELECCIONADO, dia);
//			session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
//			calendarioDiaLocalDTO.setNpEsSeleccionado(new Boolean(true));
//			formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
//			LogSISPE.getLog().info("dia seleccionado: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
//			//session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
//			formulario.setCalendarioDiaLocal(calendarioDiaLocalDTOObj);
//			LogSISPE.getLog().info("habilitarDireccion: {}" , session.getAttribute(HABILITARDIRECCION));
//			if(session.getAttribute(HABILITARDIRECCION)!=null){
//				//AYUDA
//				session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
//				//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//				if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))))  ){
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso4"));
//				}
//				else{
//					session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
//				}
//				session.setAttribute("siDireccion", "ok");
//			}
//		}
//	}
//	/**
//	 * Busca el local en base al que se va a realizar la b\u00FAsqueda, sube a sesion el local destino y retorna true si encontro el local
//	 * @param formulario
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	private static boolean buscaLocalBusqueda(CotizarReservarForm formulario,HttpServletRequest request,String local) throws Exception{
//		VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
//		vistaLocalDTO.getId().setCodigoLocal(new Integer(local));
//		vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		LogSISPE.getLog().info("va a consultar el local");
//		Collection vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
//		if(vistaLocalDTOCol!=null && vistaLocalDTOCol.size()>0){
//			VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
//			request.getSession().setAttribute(VISTALOCALDESTINO, vistaLocal);
//			return(true);
//		}
//		return(false);
//	}
//	
//	/**
//	 * obtiene los parametros que se van a usar para la configuracion de la entrega
//	 * @param request
//	 * @param errors
//	 */
//	private static void obtenerConfiguracionEntrega(HttpServletRequest request,ActionMessages errors){
//		try{
//			//Construye parametros para la consulta
//			ConfiguracionDTO configuracionDTO=new ConfiguracionDTO();
//			configuracionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			configuracionDTO.setEstadoConfiguracion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//			configuracionDTO.setProcesoConfiguracion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.configuracion.procesoReserva"));
//			//Metodo que trae las configuraciones de las entregas
//			Collection<ConfiguracionDTO> configuracionDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerConfiguracion(configuracionDTO);
//			request.getSession().setAttribute(CONFIGURACION, configuracionDTOCol);
//		}catch(Exception e){
//			errors.add("configuracion",new ActionMessage("errors.cargarConfiguraciones"));
//		}
//	}
//	
//	/**
//	 * 
//	 * @param request
//	 * @param entregaDTO1
//	 */
//	public static void cargaDireccionesYCostos(HttpServletRequest request, EntregaDTO entregaDTO1, Double subTotal){
//		LogSISPE.getLog().info("la entrega es a domicilio y va a verificar direcciones y costos");
//		HttpSession session=request.getSession();
//		//coleccion de costos de entrega de direcciones
//		Collection costoEntregasDTOCol = session.getAttribute(COSTOENTREGA)!=null ? (Collection)session.getAttribute(COSTOENTREGA) : new ArrayList();
//		//coleccion de direcciones de entrega
//		Collection direccionesDTOCol = session.getAttribute(DIRECCIONES)!=null ? (Collection)session.getAttribute(DIRECCIONES) : new ArrayList();
//		boolean condicionCosto = false;//condicion para ingreso de nuevos costos
//		boolean entregaConDomicilioExistente = false;//condicion para ingreso de nuevas direcciones
//		
//		Boolean esEntregaDomStockLocal = esEntregaDomStockLocal(entregaDTO1);
//
//		//Contador de direcciones
//		int secDirecciones=1;
//		if(session.getAttribute(SECDIRECCIONES)!=null){
//			secDirecciones=((Integer)session.getAttribute(SECDIRECCIONES)).intValue();
//		}
//		
////		//session del costo total de la entrega a domicilio
////		double costoEntregaParcial = 0;
////		if(session.getAttribute(VALORTOTALENTREGA)!=null){
////			costoEntregaParcial = ((Double)session.getAttribute(VALORTOTALENTREGA)).doubleValue();
////		}
//		
//		//Saco la direccion de la entrega
//		int posicion = entregaDTO1.getDireccionEntrega().indexOf("-");
//		String direccion=entregaDTO1.getDireccionEntrega().substring(posicion+1);
//		
//		try{
//			
//			/*******************Comprueba si la direccion ya ha sido ingresada al menos una vez*********/
//			if(direccionesDTOCol!=null && direccionesDTOCol.size()>0){
//				for (Iterator iter = direccionesDTOCol.iterator(); iter.hasNext();) {
//					DireccionesDTO direccionesDTO = (DireccionesDTO) iter.next();
//					LogSISPE.getLog().info("npDireccionEntregaDomicilio: {}" , entregaDTO1.getNpDireccionEntregaDomicilio());
//					LogSISPE.getLog().info("codigoDireccion: {}" , direccionesDTO.getCodigoDireccion());
//					if(direccion.equals(direccionesDTO.getDescripcion())){
//						LogSISPE.getLog().info("no hay nueva direccion");
//						entregaConDomicilioExistente = true;
//						//Si existe ya la direccion incrementa su contador
//						direccionesDTO.setNumeroDireccion(direccionesDTO.getNumeroDireccion()+1);
//						entregaDTO1.setNpDireccionEntregaDomicilio(direccionesDTO.getCodigoDireccion());
//					}
//				}
//			}
//			/*******************Comprueba si el costo ya ha sido ingresado al menos una vez*********/
//			if(costoEntregasDTOCol!=null && costoEntregasDTOCol.size()>0){
//				for(Iterator costo=costoEntregasDTOCol.iterator();costo.hasNext();){
//					CostoEntregasDTO costoEntregasDTO =(CostoEntregasDTO)costo.next();
//					LogSISPE.getLog().info("costo.sector: {}" , costoEntregasDTO.getCodigoSector());
//					LogSISPE.getLog().info("costo.fechaEntrega{}" , costoEntregasDTO.getFechaEntrega());
//					LogSISPE.getLog().info("entrega.fechaEntrega{}" , entregaDTO1.getFechaEntregaCliente());
//					LogSISPE.getLog().info("entrega.direccion: {}",entregaDTO1.getDireccionEntrega());
//					Double costoEntregaTemp = costoEntregasDTO.getNpValorParcial();
//					if(entregaConDomicilioExistente && entregaDTO1.getDireccionEntrega().equals(costoEntregasDTO.getCodigoSector()) && costoEntregasDTO.getFechaEntrega().equals(entregaDTO1.getFechaEntregaCliente())){
//						condicionCosto = true;
//						LogSISPE.getLog().info("no hay nuevo costo");
//						//Si existe ya el costo incremente su contador
//						costoEntregaTemp = costoEntregaTemp + entregaDTO1.getCostoParcialEntrega();
//						costoEntregasDTO.setNpValorParcial(costoEntregaTemp);
//						
//						Double valorEntregaTotal = Double.valueOf(
//								WebSISPEUtil.costoEntregaDistancia(costoEntregasDTO.getDistancia(), request, 
//										costoEntregaTemp, entregaDTO1.getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))));
//						
//						costoEntregasDTO.setValor(valorEntregaTotal);
//						costoEntregasDTO.setNumeroEntregas(costoEntregasDTO.getNumeroEntregas()+1);
//						LogSISPE.getLog().info("costoEntrega: {}" , costoEntregasDTO.getNumeroEntregas());
//						
//						//agregamos la entrega relacionada al costo
//						costoEntregasDTO.getColEntregaDTOs().add(entregaDTO1);
//					}
//				}
//			}
//			
//			//cuando hay una nueva direcci\u00F3n
//			if(!entregaConDomicilioExistente){
//				LogSISPE.getLog().info("Es una nueva direcci\u00F3n");
//				DireccionesDTO direccionesDTO1 = new DireccionesDTO();
//				direccionesDTO1.setCodigoDireccion(Integer.valueOf(secDirecciones).toString());
//				LogSISPE.getLog().info("codigoDir: {}" , direccionesDTO1.getCodigoDireccion());
//				direccionesDTO1.setDescripcion(direccion);
//				direccionesDTO1.setFechaEntrega(entregaDTO1.getFechaEntregaCliente());
//				direccionesDTO1.setCodigoSector(entregaDTO1.getCodigoLocalSector().toString());
//				if (entregaDTO1.getDistanciaEntrega()!=null) {
//					direccionesDTO1.setDistanciaDireccion(entregaDTO1.getDistanciaEntrega().toString());
//				} else {
//					direccionesDTO1.setDistanciaDireccion(Double.valueOf(WebSISPEUtil.distanciaEntregaCosto(entregaDTO1.getCostoEntrega(),request, esEntregaDomStockLocal)).toString());
//				}
//				direccionesDTO1.setNumeroDireccion(1);
//				direccionesDTOCol.add(direccionesDTO1);
//				session.setAttribute(DIRECCIONES, direccionesDTOCol);
//				session.setAttribute(SECDIRECCIONES, Integer.valueOf(secDirecciones+1));
//				entregaDTO1.setNpDireccionEntregaDomicilio(direccionesDTO1.getCodigoDireccion());
//			}
//			
//			//cuando hay un nuevo costo
//			if(!condicionCosto){
//				LogSISPE.getLog().info("es un nuevo costo");
//				CostoEntregasDTO costoEntregasDTO1 = new CostoEntregasDTO();
//				costoEntregasDTO1.setFechaEntrega(entregaDTO1.getFechaEntregaCliente());
//				costoEntregasDTO1.setCodigoSector(entregaDTO1.getDireccionEntrega());
//				if (entregaDTO1.getDistanciaEntrega()!=null) {
//					costoEntregasDTO1.setDistancia(entregaDTO1.getDistanciaEntrega());
//				} else {
//					costoEntregasDTO1.setDistancia(WebSISPEUtil.distanciaEntregaCosto(entregaDTO1.getCostoEntrega(),request, esEntregaDomStockLocal));
//				}
//				
//				Double valorEntregaTotal = Double.valueOf(
//						WebSISPEUtil.costoEntregaDistancia(costoEntregasDTO1.getDistancia(), request, 
//								entregaDTO1.getCostoParcialEntrega(), entregaDTO1.getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))));
//				
//				costoEntregasDTO1.setValor(valorEntregaTotal);
//				costoEntregasDTO1.setNumeroEntregas(1);
//				costoEntregasDTO1.setNpValorParcial(entregaDTO1.getCostoParcialEntrega());
//				costoEntregasDTO1.setDescripcion(direccion);
//				
//				//agregamos la entrega relacionada al costo
//				costoEntregasDTO1.setColEntregaDTOs(new ArrayList<EntregaDTO>());
//				costoEntregasDTO1.getColEntregaDTOs().add(entregaDTO1);
//				costoEntregasDTOCol.add(costoEntregasDTO1);
////				costoEntregaParcial = costoEntregaParcial + costoEntregasDTO1.getValor().doubleValue();
////				LogSISPE.getLog().info("costoEntregaParcial: {}",costoEntregaParcial);
//				session.setAttribute(COSTOENTREGA, costoEntregasDTOCol);
////				session.setAttribute(VALORTOTALENTREGA, Double.valueOf(costoEntregaParcial));
//				
//			}
//			
////			CotizarReservarForm.asignarCostoEntregaDireccion(request, subTotal, Boolean.FALSE);
//			
////			LogSISPE.getLog().info("costo del flete: {}" , session.getAttribute(VALORTOTALENTREGA));
//		}catch(Exception e){
//			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//		}
//	}
//	
//	/**
//	 * Si le entrega es a domicilio y el stock se va ha tomar del local
//	 * @param entregaDTO
//	 * @return
//	 */
//	private static Boolean esEntregaDomStockLocal(EntregaDTO entregaDTO){
//		
//		if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion")) 
//				&& entregaDTO.getCodigoObtenerStock()==(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.local"))){
//			return Boolean.TRUE;
//		}
//		
//		return Boolean.FALSE;
//	}
//	
//	/**
//	 * 
//	 * @param fechaCalendarioDia
//	 * @param fechaEntregaCliente
//	 * @param formulario
//	 * @param request
//	 * @param dia
//	 */	
//	public static void cargaDiasModificarCA(Date fechaCalendarioDia, Date fechaEntregaCliente, CotizarReservarForm formulario, HttpServletRequest request,int dia){
//		try{
//			HttpSession session=request.getSession();
//			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(CALENDARIODIALOCALCOL);
//			//Armo una coleccion con los dias que voy a sumar capacidad acumulada
//			Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=new ArrayList<CalendarioDiaLocalDTO>();
//			CalendarioDiaLocalDTO calendarioDiaLocalDTOAux=new CalendarioDiaLocalDTO();
//			//Obtengo cuantos dias hay desde el dia seleccionado hasta el dia de entrega
//			GregorianCalendar fechaDespacho=new GregorianCalendar();
//			fechaDespacho.setTime(fechaCalendarioDia);
//			GregorianCalendar fechaEntrega=new GregorianCalendar();
//			fechaEntrega.setTime(fechaEntregaCliente);
//			int numdias=WebSISPEUtil.calcularDiasEntreFechas(fechaDespacho,fechaEntrega);
//			int diasCalendario=calendarioDiaLocalDTOObj.length;
//			LogSISPE.getLog().info("numero de dias entre la fecha de despacho y la fecha de entrega: {}" , numdias);
//			for(int i=0;i<=numdias;i++){
//				if((dia+i+1)<diasCalendario){
//					calendarioDiaLocalDTOAux=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia + i +1];
//					calendarioDiaLocalDTOCol.add(calendarioDiaLocalDTOAux);
//				}
//				
//			}
//			LogSISPE.getLog().info("dias a modificar: {}" , calendarioDiaLocalDTOCol.size());
//			session.setAttribute(CALENDARIODIALOCALCOLAUX, calendarioDiaLocalDTOCol);
//		}catch(Exception e){
//			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param request
//	 * @param formulario
//	 * @param error
//	 * @param errors
//	 * @param warnings
//	 */
//	private static void aceptaConfiguracionEntrega(HttpServletRequest request,CotizarReservarForm formulario,
//			ActionErrors error, ActionMessages errors, ActionMessages warnings){
//		
//		HttpSession session = request.getSession();
//		try{
//			boolean flag = true; //variable que indica si se va a aceptar la configuracion o no
//			boolean bodegaCanastas = false; //bandera que me indica si la configuracion es para la bod(99)
//			
//			//Hago las validaciones de formulario de valores nulos y formatos
//			if(formulario.validarTipoEntregas(error,request)==0){
//				formulario.mantenerValoresEntregas(request);
//
//				Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
//				Date fechaBusquedaAux =  DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
//				//Si existe un tipo de entrega y esta editable el campo para fecha minima
//				if(session.getAttribute(EXISTELUGARENTREGA)!=null && session.getAttribute(EDITAFECHAMINIMA)!=null){
//					//si la fecha m\u00EDnima es menor a hoy
//					if(fechaBusquedaAux.getTime() >= fechaMinimaReferencia.getTime()){
//						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"));
//						//Verifica si la fecha minima de entrega fue modificada
//						if((ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)) <= 0
//								|| autorizacionDTO!=null)){
//							
//							LogSISPE.getLog().info("va a verificar la fecha minima de entrega");
//							
//							//jmena en la configuracion Inicializo fechaEntregaCliente
//							if(formulario.getFechaEntregaCliente()==null || formulario.getFechaEntregaCliente().equals("")){
//								formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES)));
//							}
////							if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////									formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//							if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//								bodegaCanastas = true;
//							}
//							//Diferencia entre fecha de entrega y fecha minima de entrega
//							long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),formulario.getFechaEntregaCliente());
//							LogSISPE.getLog().info("diferenciaEtregaBusca: {}" , diferenciaEntregaBusca);
//							
//							if(diferenciaEntregaBusca<0.0 && !bodegaCanastas){
//								LogSISPE.getLog().info("error en diferencia");
//								errors.add("fechaEntregaCliente", new ActionMessage("errors.fechaSeleccionadaEntregaMinima",session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString()));
//							}else{
//								//si la fecha ingresada es menor a la fecha minima de entrega y existe una autorizacion o la entidad responsable es local 
//								if(ConverterUtil.returnDateDiff(formulario.getBuscaFecha(),(String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)))>0){
//									LogSISPE.getLog().info("entra a cambiar la fecha minima de entrega");
//									LogSISPE.getLog().info("la fecha minima es menor pero hay autorizacion: {}" , formulario.getBuscaFecha());
//									session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
//									session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
//									warnings.add("fechaMinima",new ActionMessage("warnings.fechaMinima"));	
//								}
//							}							
//						}
//						//si la fecha ingresada es menor a la fecha minima de entrega y no existe una autorizacion
//						else{
//							//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//							errors.add("buscaFecha",new ActionMessage("errors.fechaBuscaEntrega",DateManager.getYMDDateFormat().format((Date)session.getAttribute(FECHABUSQUEDA))));
//							session.removeAttribute(VISTALOCALCOL);
//							formulario.setFechaEntregaCliente((String)(session.getAttribute(FECHAENTREGACLIENTE)));
//						}
//					}
//					//si la fecha ingresada es mayor a la de hoy 
//					else{
//						//blanqueo el texto para poder preguntar por el validar mandatory para que se pinte de rojo la caja de texto
//						errors.add("buscaFecha",new ActionMessage("errors.fechaMinima"));
//					}
//				}
//				
//				if(errors.size()==0){
//					String tomarStock =(String)session.getAttribute(STOCKENTREGAAUX);
//					//Pregunto cual es la entidad responsable seleccionada, si es la bodega hago las validaciones para saber
//					//si se puede seleccionar
//					//if(formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//					  ////if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//					if(formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//						//Valido la entidad responsable
//						ArrayList detalleReservacion = (ArrayList)session.getAttribute(DETALLELPEDIDOAUX);
//						PedidoDTO consultaPedidoDTO = new PedidoDTO();
//						consultaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//						consultaPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
//						consultaPedidoDTO.setAutorizacionDTOCol((Collection<AutorizacionDTO>)session.getAttribute(AutorizacionesUtil.AUTORIZACION_COL));
//						String fechaMinimaEntregaCD = (String)session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES);
//						consultaPedidoDTO.setFechaMinimaEntrega(new Timestamp(WebSISPEUtil.construirFechaCompleta(fechaMinimaEntregaCD, 0, 0, 0, 0, 0, 0)));
//						
//						
//						
//						
//						LogSISPE.getLog().info("**fecha minima:** {}" , consultaPedidoDTO.getFechaMinimaEntrega());
//						//se llama al m\u00E9todo que verifica cual es la entidad responsable
//						SessionManagerSISPE.getServicioClienteServicio().transValidarEntidadResponsable(consultaPedidoDTO,detalleReservacion);
//						LogSISPE.getLog().info("LA ENTIDAD RESPONSABLE ES:{} ",consultaPedidoDTO.getEntidadResponsable());
//						
//						if(consultaPedidoDTO.getNpMsjErrorEntidadResponsable()==null){
//							session.removeAttribute(ENTIDADRESPONSABLELOCAL);
//							if(!bodegaCanastas){
//								String fechaMinEntrega = session.getAttribute(CotizarReservarAction.FECHA_ENTREGA).toString();
//								//Fecha minima desde parametro
//								Date fechaRefMinima =  DateManager.getYMDDateFormat().parse(fechaMinimaEntregaCD);
//								//Fecha minima subida a session
//								Date fechaRefEntrega =  DateManager.getYMDDateFormat().parse(fechaMinEntrega);
//								//se valida la fecha de entrega 
//								if(fechaBusquedaAux.getTime() >= fechaRefEntrega.getTime() && fechaBusquedaAux.getTime() <= fechaRefMinima.getTime() ){
//									session.setAttribute(CotizarReservarAction.FECHA_ENTREGA,formulario.getBuscaFecha());
//								}else{
//									//se inicializa nuevamnete la fecha m\u00EDnima de entrega
//									formulario.setBuscaFecha(fechaMinEntrega);
//								}
//							}
//							session.setAttribute(FECHABUSQUEDA,ConverterUtil.parseStringToDate(formulario.getBuscaFecha()));
//						}else{
//							errors.add("entidadResponsable", new ActionMessage("errors.gerneral",consultaPedidoDTO.getNpMsjErrorEntidadResponsable() + ". LA CONFIGURACION NO HA SIDO ACEPTADA"));
//							flag=false;
//						}
//						consultaPedidoDTO = null;
//					}else
//						if(formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) 
//								&& !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))){
//							session.setAttribute(ENTIDADRESPONSABLELOCAL, "ok");
//						}else{
//							session.removeAttribute(ENTIDADRESPONSABLELOCAL);
//						}
//					
//					//Si la configuracion fue aceptada
//					if(flag){
//						obtenerConfiguracionEntregas(formulario, request, errors);
//						//si no se va a seleccionar local de entrega por defecto el local destino es igual al local origen
//						if(session.getAttribute(SELECCIONARLOCAL)==null){
//							if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//								if(!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal"))){
//									buscaLocalBusqueda(formulario, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
//								}
//								else{
//									session.setAttribute(SELECCIONARLOCAL,"ok");
//								}
//							}else{
//								VistaLocalDTO vistaLocalOrigenDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALORIGEN);
//								LogSISPE.getLog().info("local origen: {}" , vistaLocalOrigenDTO.getId().getCodigoLocal());
//								session.setAttribute(VISTALOCALDESTINO, vistaLocalOrigenDTO);
//							}
//						}
//						//Si se va tomar todo de bodega setear el maximo numero de articulos que se pueden pedir a bodega
//						if(formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))){
//							LogSISPE.getLog().info("va a tomar todo de bodega");
//							
//							//se va a mostrar calendario por hora
//							if( (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))
//								&& (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
//										|| formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
//								session.setAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS, Boolean.TRUE);
//							}
//								
//							//recupero el detalle del pedido
//							Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//							for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//								DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//								if(detallePedidoDTO.getNpContadorEntrega()!=null)
//									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoDTO.getNpContadorEntrega().longValue()));
//								else
//									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()));
//							}
//						}
//						//Sino se setea 0 pero solo en caso de que no se hayan ingreado cantidades anteriormente o que la configuracion
//						//anterior no haya sido parcial a bodega
//						else{
//							LogSISPE.getLog().info("-----tomarStock---- {}" , tomarStock);
//							//recupero el detalle del pedido
//							if(tomarStock!=null && !tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
//								
//								//se va a mostrar calendario por hora
//								if( (formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")))
//									&& (formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
//											|| formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
//									session.setAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS, Boolean.TRUE);
//								}
//								Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//								for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//									DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
//								}
//							}
//							else if(tomarStock==null){
//								Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
//								for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
//									DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
//									detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
//								}
//							}
//						}
//						/*************Carga el calendario en caso de ser necesario*/
//						boolean loc=true;//variable que se usa para reconocer cuando se ha ingresado un local valido
//						VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//						LogSISPE.getLog().info("*****local destino***** {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
//						//Si esta activo el combo para seleccionar los locales 
//						if(session.getAttribute(SELECCIONARLOCAL)!=null ){
//							String local=null;
//							//si fue seleccionado un local desde el combo
//							if(formulario.getListaLocales()!=null && !formulario.getListaLocales().equals("")){
//								LogSISPE.getLog().info("local seleccionado: {}" , formulario.getListaLocales());
//								local=formulario.getListaLocales();
//								formulario.setLocal(local);
//							}	
//							//si fue ingresado un local desde la caja de textos
//							else if(formulario.getLocal()!=null && !formulario.getLocal().equals("")){
//								LogSISPE.getLog().info("ingreso un local");
//								local=formulario.getLocal();
//								formulario.setListaLocales(local);
//							}	
//							else{
//								local=((VistaLocalDTO)session.getAttribute(VISTALOCALORIGEN)).getId().getCodigoLocal().toString();
//								formulario.setLocal(local);
//								formulario.setListaLocales(local);
//							}
//							//si el local seleccionado o ingresado es distinto al local de destino
//							if(errors.size()==0 && !local.equals(vistaLocalDestinoDTO.getId().getCodigoLocal().toString()) || session.getAttribute(CALENDARIODIALOCALCOL)==null){
//								//cargo el local destino
//								loc=buscaLocalBusqueda(formulario, request,local);
//								LogSISPE.getLog().info("encontro al local: {}" , loc);
//							}
//						}
//						//Si el local fue encontrado
//						if(loc){
//							//Obtengo el local destino
//							vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//							LogSISPE.getLog().info("***LOCAL DESTINO***: {}" ,vistaLocalDestinoDTO.getId().getCodigoLocal());
//							//En los casos donde deba haber una fecha de despacho se obtiene el calendario
//							//if(session.getAttribute(SELECCIONARCALENDARIO)!=null || (formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal")))){
//							if(session.getAttribute(SELECCIONARCALENDARIO)!=null || ((formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"))) && formulario.getOpStock()!=null && !formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))){
//								LogSISPE.getLog().info("va a desplegar el calendario");
//								LocalID localID=new LocalID();
//								localID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
//								localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
////								if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) &&
////										formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
//								if(formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//									localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
//									loc=buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
//									LogSISPE.getLog().info("encontro al local: {}" , loc);
//								}else{
//									localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
//								}
//								//Cargo el calendario	
//								if (session.getAttribute(MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null)
//									obtenerLocalCalendarioPorSemana(session, request, localID, errors, formulario);
//								else
//									obtenerLocal(session, request, localID, errors, formulario);
//								
//								session.removeAttribute(DIASELECCIONADO);
//								session.removeAttribute(CALENDARIODIALOCAL);
//								session.removeAttribute(DIRECCION);
//							}
//						}
//						//Si el local ingresado no existe
//						else{
//							errors.add("errorLocal",new ActionMessage("errors.local"));
//							if (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))) {
//								session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
//							}
//						}
//						//Cargo el lugar desde donde se va a realizar la entrega al domicilio
//						VistaLocalDTO vistaLocal=(VistaLocalDTO)session.getAttribute(VISTALOCALDESTINO);
//						LogSISPE.getLog().info("va a guardar el local de entrega");
//						//if(session.getAttribute(ENTIDADRESPONSABLELOCAL)!=null){
//						if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && 
//								!formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
//							session.setAttribute(LUGARENTREGADOMICILIO,"Local " + vistaLocal.getId().getCodigoLocal());
//							session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
//						}	
//						else
//							session.setAttribute(LUGARENTREGADOMICILIO,"el CD ");
//						
//						
//						LogSISPE.getLog().info("local de entrega: {}" ,session.getAttribute(LUGARENTREGADOMICILIO).toString());
//						LogSISPE.getLog().info("lugarEnt: {}" , formulario.getOpLugarEntrega());
//						LogSISPE.getLog().info("tipoEnt: {}" , formulario.getOpTipoEntrega());
//						LogSISPE.getLog().info("stockEnt: {}" , formulario.getOpStock());
//						//Subo a session la configuracion
//						session.setAttribute(LUGARENTREGA,formulario.getOpLugarEntrega());
//						session.setAttribute(TIPOENTREGA,formulario.getOpTipoEntrega());
//						session.setAttribute(STOCKENTREGA,formulario.getOpStock());
//						session.setAttribute(STOCKENTREGAAUX,formulario.getOpStock());						
//					}
//				}
//			}
//			formulario.setDirecciones(null);
//		}catch(Exception e){
//			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
//		}
//	}
//	
//	/**
//	 * Funci\u00F3n que carga las ciudades para las entregas a domicilio desde el CD
//	 * @param request
//	 * @param errors
//	 */
//	private static void cargaCiudades(HttpServletRequest request,ActionMessages errors){
//		try{
//			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
//			//se obtienen las ciudades recomendadas desde un par\u00E1metro
//			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesRecomendadasEntregasDomicilio", request);
//			String[] ciudadesRecomendadas = parametroDTO.getValorParametro() != null ? parametroDTO.getValorParametro().split(",") : null;
//			VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = new VistaEstablecimientoCiudadLocalDTO();
//			vistaEstablecimientoCiudadLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			vistaEstablecimientoCiudadLocalDTO.setNpObtenerCiudadesSinLocales("ok");
//			Collection<VistaEstablecimientoCiudadLocalDTO> vistaEstablecimientoCiudadLocalDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaEstablecimientoCiudadLocalSector(vistaEstablecimientoCiudadLocalDTO);
//			LogSISPE.getLog().info("numero de ciudades: {}" , vistaEstablecimientoCiudadLocalDTOCol.size());
//			//Busco las ciudades recomendadas
//			for(VistaEstablecimientoCiudadLocalDTO tituloCargaCiudad: vistaEstablecimientoCiudadLocalDTOCol){
//				for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudad: (Collection<VistaEstablecimientoCiudadLocalDTO>)tituloCargaCiudad.getVistaLocales()){
//					//boolean esCiudadRecomendada=false;
//					vistaEstablecimientoCiudad.setNpCiudadRecomendada(null);
//					LogSISPE.getLog().info("codigo ciudad: {}" , vistaEstablecimientoCiudad.getId().getCodigoCiudad());
//					if(ciudadesRecomendadas != null){
//						for(int i=0;i<ciudadesRecomendadas.length;i++){
//							if(vistaEstablecimientoCiudad.getId().getCodigoCiudad().equals(ciudadesRecomendadas[i])){
//								vistaEstablecimientoCiudad.setNpCiudadRecomendada(estadoActivo);
//								LogSISPE.getLog().info("{} es ciudad recomendada", vistaEstablecimientoCiudad.getId().getCodigoCiudad());
//								break;
//							}
//						}
//					}
//				}
//			}
//			request.getSession().setAttribute(VISTAESTABLECIMIENTOCIUDADLOCAL, vistaEstablecimientoCiudadLocalDTOCol);
//			LogSISPE.getLog().info("ciudades: {}", vistaEstablecimientoCiudadLocalDTOCol.size());
//		}catch(Exception e){
//			errors.add("ciudades",new ActionMessage("errors.cargarCiudades",e.getStackTrace()));
//		}
//	}
//	
//	private Boolean verificarCanastasCatalogo(HttpServletRequest request){
//		LogSISPE.getLog().info("--Entro en la funcion verificarCanastasCatalogo");
//		Boolean existeCanastaCatalogo=Boolean.FALSE;
//		try {
//		HttpSession session = request.getSession();
//		//se obtiene los parametros desde el properties
//		ParametroDTO parametroDTO;
//		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
//		String codClaCanastos = parametroDTO.getValorParametro();
//		LogSISPE.getLog().info("Id clasificacion de canastos de catalago: {}",codClaCanastos);
//		Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
//		LogSISPE.getLog().info("Detalles del pedido: {}",detallePedidoCol.size());
//		if(detallePedidoCol != null && !detallePedidoCol.isEmpty()){
//			//Validacion de DespachosLocal solo canastasCatalago
//			for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
//				//verifico que solo sea un canasto de catalago y tipoEntregas.
//				if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)){
//					LogSISPE.getLog().info("El pedido contiene canastos de catalago");
//					existeCanastaCatalogo= Boolean.TRUE;
//					break;
//				}
//			}
//		}
//		} catch (Exception e) {
//			LogSISPE.getLog().info("Error verificarCanastasCatalogo {}",e.getStackTrace());
//		}
//		return existeCanastaCatalogo;
//	}
//	/**
//	 * 
//	 * @param request
//	 * @param codigoClasificacionArticulo
//	 * @return
//	 * @throws Exception 
//	 * @throws MissingResourceException 
//	 */
//	@SuppressWarnings("unused")
//	private boolean verificarArticuloPerecible(HttpServletRequest request, String codigoClasificacionArticulo) throws MissingResourceException, Exception{
//		//Colecci\u00F3n con los c\u00F3digos de las clasificaciones que son consideradas como perecibles
//		Collection<String> clasificacionesPerecibles = (Collection<String>)request.getSession().getAttribute(CLASIFICACIONES_PERECIBLES);
//		
//		//verifica si se consult\u00F3 las clasificaciones, caso contrario las busca.
//		if(clasificacionesPerecibles == null ){
//			//obtiene el c\u00F3digo del especial para perecibles
//			ParametroDTO parametro = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialPerecibles", request);
//			//Plantilla de b\u00FAsqueda
//			EspecialClasificacionDTO especialClasificacionDTO = new EspecialClasificacionDTO();
//			especialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			especialClasificacionDTO.getId().setCodigoEspecial(parametro.getValorParametro());
//			especialClasificacionDTO.setEstadoEspecialClasificacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
//			//obtiene las clasificaciones
//			clasificacionesPerecibles = SISPEFactory.obtenerServicioSispe().transObtenerCodigosEspecialClasificacion(especialClasificacionDTO);
//			request.getSession().setAttribute(CLASIFICACIONES_PERECIBLES, clasificacionesPerecibles);
//		}
//		//encontr\u00F3 clasificaciones
//		if(clasificacionesPerecibles != null && !clasificacionesPerecibles.isEmpty()){
//			//recorre los c\u00F3digos de las clasificaciones de perecibles
//			for(String codClaPerecible : clasificacionesPerecibles){
//				//si la clasificaci\u00F3n del art\u00EDculo est\u00E1 en dentro de las clasificaciones de perecibles
//				if(codClaPerecible.equals(codigoClasificacionArticulo)){
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}
//	
//	public static void instanciarVentanaEliminarEntregas(HttpServletRequest request)throws Exception{
//		HttpSession session = request.getSession();
//		UtilPopUp popUp = new UtilPopUp();
//		popUp.setTituloVentana("Confirmaci\u00F3n");
//		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
//		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'confirmarEliminarEntregas=ok', evalScripts:true});ocultarModal();");
//		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarEliminarEntregas=ok', popWait:false, evalScripts:true});ocultarModal();");
//		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
//		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/confirmarEliminarEntregas.jsp");
//		popUp.setAncho(40D);
//		popUp.setTope(40D);
//		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
//		popUp = null;
//	}
//	
//	//
//	public static void instanciarVentanaOpcionesConfiguracion(HttpServletRequest request)throws Exception{
//		HttpSession session = request.getSession();
//		UtilPopUp popUp = new UtilPopUp();
//		popUp.setTituloVentana("Configuraci\u00F3n de entregas");
//		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
//		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//		popUp.setEtiquetaBotonOK("Siguiente");
//		popUp.setEstiloOK("siguienterD");
//		//popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['div_pagina','pregunta','entregas','mensajes','confirmarLocalEntrega','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'seleccionarOpcion=ok', evalScripts:true});ocultarModal();");
//		popUp.setValorOK("requestAjax('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true, evalScripts:true});");
//		popUp.setValorCANCEL("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//		//popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
//		popUp.setAccionEnvioCerrar("requestAjax('entregaLocalCalendario.do', ['pregunta','entregas'], {parameters: 'ocultarVentanaResponsable=ok', popWait:false, evalScripts:true});ocultarModal();");
//		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/entregas/mostrarOpcionConfiguracion.jsp");
//		popUp.setAncho(75D);
//		popUp.setTope(-150D);
//		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
//		session.setAttribute(MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConfEnt1"));
//		session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso1"));
//		popUp = null;
//	}
//	
//	private String obtenerDia(Calendar fechaDespacho){
//		String days [] = {"Domingo","Lunes","Martes","Mi\u00E9rcoles","Jueves","Viernes","S\u00E1bado"};
//		return days[fechaDespacho.get(Calendar.DAY_OF_WEEK)-1]; 
//	}
//	
//	/**
//	 * Carga las zonas asociadas a una ciudad
//	 * @param formulario
//	 * @param session
//	 * @throws Exception 
//	 */
//	private static void cargarZonaCiudad(CotizarReservarForm formulario,  HttpSession session) throws Exception{
//		LogSISPE.getLog().info("ingresa al metodo cargarZonaCiudad ");
//		session.removeAttribute(CIUDAD_SECTOR_ENTREGA);
//		String codigoCiudad = "";
//		if(formulario.getSeleccionCiudad() != null)
//			if(formulario.getSeleccionCiudad().split("/").length > 1)
//				codigoCiudad = formulario.getSeleccionCiudad().split("/")[1];
//			else
//				codigoCiudad = formulario.getSeleccionCiudad();
//		if(!codigoCiudad.equals("")){
//			DivisionGeoPoliticaDTO ciudadConsulta = new DivisionGeoPoliticaDTO();
//			ciudadConsulta.setCodigoDivGeoPolPadre(codigoCiudad);								
//			Collection<DivisionGeoPoliticaDTO> zonasCiudad = SISPEFactory.getDataService().findObjects(ciudadConsulta);
//			session.setAttribute(CIUDAD_SECTOR_ENTREGA, zonasCiudad);			
//		}
//	}
//	
//
//	/**
//	 * obtiene los datos del local destino para cargar el calendario por semana
//	 * @param session
//	 * @param request
//	 * @param localID
//	 * @param errors
//	 * @throws Exception
//	 */
//
//	public static void obtenerLocalCalendarioPorSemana(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,CotizarReservarForm formulario) throws Exception
//	{
//		session.setAttribute(LOCALID, localID);
//		
//		LogSISPE.getLog().info("fecha de busqueda: formulario.getFechaEntregaCliente() {}" , formulario.getFechaEntregaCliente());
//		LogSISPE.getLog().info("fecha de busqueda: (String)(session.getAttribute(FECHAENTREGACLIENTE))() {}" , (String)(session.getAttribute(FECHAENTREGACLIENTE)));
//		
//		if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
//			formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//		}
//		Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		
//		LogSISPE.getLog().info("fecha de busqueda: {}" , mes);
//		//jmena bandera que muestra o no la configuracion del local bodega.
//		session.removeAttribute(BANDERA_CONFIGURA_CAL_BOD);
//		String rangoDias = (String)session.getAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO);
//		if(rangoDias == null){
//			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
//			ParametroDTO consultaParametroDTO = new ParametroDTO();
//			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasMinimosFechaDespacho"));
//			Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
//			
//			rangoDias = !parametros.isEmpty() ? parametros.iterator().next().getValorParametro() : null;
//			session.setAttribute(RANGO_DIAS_DESPACHO_BODEGA_CALENDARIO, rangoDias);
//			consultaParametroDTO = null;
//		}
//		
//		int parametro = Integer.parseInt(rangoDias); //variable para el parametro del dia minino de despacho a local
//		LogSISPE.getLog().info("parametro: {}" , parametro);
//		
//		//Resto al dia de entrega el parametro para ver la fecha minima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
//		Date fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		GregorianCalendar fechaCalendario = new GregorianCalendar();
//		fechaCalendario.setTime(fechaMinima);
//		fechaCalendario.add(Calendar.DAY_OF_MONTH, (-1)*parametro);
//		fechaMinima = fechaCalendario.getTime();
//		
//		//------ fmunoz -------
//		//se obtiene el par\u00E1metro desde la base
//		ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
//		int canDiaFecMinDes = consultaParametroDTO.getValorParametro() != null ? Integer.parseInt(consultaParametroDTO.getValorParametro()) : 0;
//		Date fechaMinimaReferencia = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), 0, canDiaFecMinDes, 0, 0, 0, 0));
//		
//		//si la fecha m\u00EDnima es menor o igual a hoy
//		if(fechaCalendario.getTimeInMillis() < fechaMinimaReferencia.getTime()){
//			fechaMinima = fechaMinimaReferencia;
//		}
//		
//		//Resto al dia de entrega un d\u00EDa para ver la fecha m\u00E1xima en que se puede recibir la mercader\u00EDa desde la bodega (fechaDespachoBodega)
//		Date fechaMaxima = DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//		GregorianCalendar fechaCalendario1=new GregorianCalendar();
//		fechaCalendario1.setTime(fechaMaxima);
//		fechaCalendario1.add(Calendar.DAY_OF_MONTH,-1);
//		fechaMaxima=fechaCalendario1.getTime();
//		
//		session.removeAttribute(CALENDARIODIALOCAL);
//		session.removeAttribute(DIASELECCIONADO);
//		
//		//jmena verifico si es el calendario de la bod/canastos
//		if(localID.getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//			GregorianCalendar fechaMaximaDomicilio = new GregorianCalendar();
//			fechaMaximaDomicilio.setTime(new Date());
////			fechaMaximaDomicilio.set(GregorianCalendar.MONTH, 12);
////			fechaMaximaDomicilio.set(GregorianCalendar.DAY_OF_MONTH, 31);
//			fechaMaximaDomicilio.add(GregorianCalendar.DAY_OF_MONTH,11);
//			fechaMaxima = new Date(fechaMaximaDomicilio.getTimeInMillis());
//			fechaMinima = DateManager.getYMDDateFormat().parse(formulario.getBuscaFecha());
//			//Inicializo la fecha de entrega null
//			formulario.setFechaEntregaCliente(null);
//			request.getSession().setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//			request.getSession().getAttribute(BANDERA_CONFIGURA_CAL_BOD);
//			//session.setAttribute(BANDERA_CONFIGURA_CAL_BOD, "ok");
//		}
//		
//		long diferenciaEntregaBusca= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(fechaMaxima));
//		if(diferenciaEntregaBusca<0.0){
//			LogSISPE.getLog().info("Mensaje rango de fechas con errores");
//			errors.add("rangoFechas",new ActionMessage("errors.rangoInicialFinalFechasCalendario",parametro,canDiaFecMinDes));
//		}else{
//			obtenerCalendarioPorSemana(session,request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}
//		
//		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
//		LogSISPE.getLog().info("fechaMaxima: {}" , fechaMaxima);
//		session.setAttribute(FECHAMINIMA,fechaMinima);
//		session.setAttribute(FECHAMAXIMA,fechaMaxima);		
//		session.setAttribute(MES_ACTUAL_CALENDARIO, mes);
//	}
//	
//	/**
//	 * obtiene datos del calendario por semana
//	 * @param session
//	 * @param request
//	 * @param localID
//	 * @param errors
//	 * @param mes
//	 * @throws Exception
//	 */
//
//	public static void obtenerCalendarioPorSemana(HttpSession session,HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechaMinima,Date fechaMaxima,CotizarReservarForm formulario, Boolean verCalendario) throws Exception
//	{
//		try{
//			LogSISPE.getLog().info("*****entra a cargar el calendario por semana*****");
//			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
//			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
//			
//			Calendar calendario = Calendar.getInstance();
//			calendario.setTime(fechaMinima);
//			
//			calendario.add(Calendar.DAY_OF_WEEK, - calendario.get(Calendar.DAY_OF_WEEK)+1);
//			fechaMinima = calendario.getTime();
//			
//			calendario.add(Calendar.DAY_OF_WEEK, 7);
//			fechaMaxima = calendario.getTime();
//			
//			GregorianCalendar nuevaFecha = new GregorianCalendar();
//			nuevaFecha.setTime(fechaMinima);
//			nuevaFecha.add(GregorianCalendar.DAY_OF_WEEK, 7);
//			fechaMaxima = nuevaFecha.getTime();
//			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(CALENDARIOCONFIGURACIONDIALOCALAUX);
//			
//			//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
//			List calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFechaSemana(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
//			if(CollectionUtils.isNotEmpty(calendarioDiaLocalDTOCol)){
//								
//				Date fechaEntCliente = (Date)session.getAttribute(FECHABUSQUEDA); 
//				
//				for(Iterator<CalendarioDiaLocalDTO> it=calendarioDiaLocalDTOCol.iterator();it.hasNext();){
//					CalendarioDiaLocalDTO calendarioActual = it.next();
//					
//					//deshabilito los dias inferiores a la fecha de entrega
//					if(fechaEntCliente.after(calendarioActual.getId().getFechaCalendarioDia())){
//						calendarioActual.setNpPuedeSeleccionar(Boolean.FALSE);
//						calendarioActual.setNpEsDistintoMes(Boolean.FALSE);
//					}
//					calendarioActual.setCalendarioHoraLocalCol(EntregaLocalCalendarioUtil.obtenerBultosDisponiblesCiudadSectorFecha(formulario, calendarioActual.getId().getFechaCalendarioDia(), localID, request));
//				}
//			}
//						
//			//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
//			LogSISPE.getLog().info("minima: {}" , fechaMinima);
//			LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
//			Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
//			session.setAttribute(CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
//			session.setAttribute(CALENDARIODIALOCAL_PARCIAL_POR_HORAS, calendarioDiaLocalDTOOBJ);
//			//subo a session el mes de busqueda
//			session.setAttribute(MESSELECCIONADO,mes);
//			//calculo cuantas semanas tiene el mes
//			int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
//			//int maximoSemanas = 1;
//			LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
//			//subo a sesion el numero de semanas
//			session.setAttribute(NUMEROSEMANAS,new Integer(maximoSemanas));
//			session.setAttribute(FECHAMINIMA, fechaMinima);
//			session.setAttribute(FECHAMAXIMA, fechaMaxima);
//		}catch(SISPEException e){
//			LogSISPE.getLog().info("error al cargar calendario: {}" , e.getStackTrace());  
//			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
//		}
//	}
//	
//	/**
//	 * Comprueba si existe una autorizacion para una fecha de despacho
//	 * @author jacalderon
//	 * @param  entregaDTO				El objeto entrega
//	 * @param  session
//	 */
//	public AutorizacionDTO buscaAutorizacion(HttpSession session,String codigoLocal, Timestamp fecha){
//		Collection autorizacionEntregasWDTOCol =(Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidos.autorizacionEntregasWDTOCol");
//		AutorizacionDTO autorizacionDTO=null;
//		if (autorizacionEntregasWDTOCol!=null){
//			LogSISPE.getLog().info("autorizaciones: {}" , autorizacionEntregasWDTOCol.size());
//			for (Iterator iter = autorizacionEntregasWDTOCol.iterator(); iter.hasNext();) {
//				AutorizacionEntregasDTO autorizacionEntregasWDTO = (AutorizacionEntregasDTO) iter.next();
//				LogSISPE.getLog().info("local: {} - {}" , autorizacionEntregasWDTO.getCodigoLocal(), codigoLocal);
//				LogSISPE.getLog().info("fecha: {} - {}" , autorizacionEntregasWDTO.getFechaAutorizacion(), fecha);
//				if(autorizacionEntregasWDTO.getCodigoLocal().equals(codigoLocal) && fecha.equals(autorizacionEntregasWDTO.getFechaAutorizacion())){
//					LogSISPE.getLog().info("existe una autorizacion");
//					autorizacionDTO=autorizacionEntregasWDTO.getAutorizacionDTO();
//				}
//			}
//		}
//		return(autorizacionDTO);
//	}
//	
//	/**
//	 * El calculo del costo de la entrega, en base a los bultos que equivale cada articulo
//	 * @param detallePedidoDTO
//	 * @return
//	 * @throws SISPEException
//	 */
//	public static Double valorEntregaArticuloBultos(DetallePedidoDTO detallePedidoDTO) throws SISPEException{ //, EntregaDTO entregaDTO
//		
//		Double costoEntrega = 0D;
//
//			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()>0 && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()>0){
//				//se obtiene los bultos a los que equivale el total de articulos que se van a reservar.
//				int bultosTotalEntrega = UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado(), detallePedidoDTO.getArticuloDTO());
//				//se obtiene los bultos a los que equivale la cantidad parcial articulos que se van a reservar.
//				int bultosParcialEntrega = UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado(), detallePedidoDTO.getArticuloDTO());
//				//Double valorUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta()/bultosTotalEntrega;
//				costoEntrega = bultosParcialEntrega * detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta()/bultosTotalEntrega;
//			}
//
//		return costoEntrega;
//	}
//	
//	/**
//	 * 
//	 * @param request
//	 * @param colCostoEntrega
//	 * @param formulario
//	 * @return
//	 * @throws Exception
//	 */
//	private Double calcularCostoFlete(HttpServletRequest request, 
//			Collection<CostoEntregasDTO> colCostoEntrega, CotizarReservarForm formulario)throws Exception
//	{
//		LogSISPE.getLog().info(" -- actualizarCostoTotalPedido -- ");
//		
//		Double porcentajeCalculoCostoFlete = (Double)request.getSession().getAttribute(CotizarReservarAction.PORCENTAJE_CALCULO_FLETE);
//		
//		Double valorFlete = 0D;
//			
//		if (!CollectionUtils.isEmpty(colCostoEntrega) && porcentajeCalculoCostoFlete!=null){
//			
//			Double porcentajeValorTotal = formulario.getSubTotal() * (porcentajeCalculoCostoFlete/100);
//			Double porcentajeValorTotalRound = Util.roundDoubleMath(porcentajeValorTotal,NUMERO_DECIMALES);
//			
//			LogSISPE.getLog().info("Porcentaje 2% del total pedido: {}", porcentajeValorTotalRound);
//			
//			for(CostoEntregasDTO costoEntregaDTO : colCostoEntrega){
//				LogSISPE.getLog().info("costo entrega: {}", costoEntregaDTO.getValor());
//				
//				if(costoEntregaDTO.getValor() > porcentajeValorTotalRound.doubleValue()){
//					double diferenciaCostoEntrega = costoEntregaDTO.getValor() - porcentajeValorTotalRound.doubleValue();
////					this.costoFlete = Double.valueOf(diferenciaCostoEntrega);
//					valorFlete += Double.valueOf(diferenciaCostoEntrega);
////					costoEntregaDTO.setValorFlete(valorFlete);
////					this.total = this.costoFlete + this.subTotal;
//				}else{
////					this.costoFlete = 0D;
//					valorFlete += 0D;
////					this.total = this.subTotal;
//				}
//			}
//		}
//		
//		return valorFlete;
//	}
}
