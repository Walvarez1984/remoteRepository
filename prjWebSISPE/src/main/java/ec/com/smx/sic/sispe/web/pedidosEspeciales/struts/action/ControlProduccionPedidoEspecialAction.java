/*
 * ControlProduccionPedidoEspecialAction.java
 * Creado el 04/04/2008 15:01:49
 *   	
 */
package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jdom.Document;

import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.common.util.TransformerUtil;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.mensajeria.commons.resources.MensajeriaMessages;
import ec.com.smx.mensajeria.dto.EventoDTO;
import ec.com.smx.mensajeria.dto.id.EventoID;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.ControlProduccionPedidoEspecialForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author nperalta
 *
 */
@SuppressWarnings("unchecked")
public class ControlProduccionPedidoEspecialAction extends BaseAction{
	private final String ORDEN_ASCENDENTE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	private static final String VALOR_SI_DESPACHAR = "SI_DP";
	//habilita checkbox y despliegue
	//private static final String CERRAR_DIA_OK ="ec.com.smx.sic.sispe.cerrarDiaOk";
	// muestra mensaje de si el dia ya se cerr\u00F3 o no
	private static final String MOSTRAR_INFO="ec.com.smx.sic.sispe.info.cerrarDia";
	//indica cuando se habilita boton de despacho y cierre de dia
	private static final String HABILITAR_DESPACHO="ec.com.smx.sic.sispe.habilitaDespacho";
	//variable que guarda los pedidos que se despacharan
	private static final String PEDIDOS_DESPACHADOS = "ec.com.smx.sic.sispe.pedidos.despachados";
	private static final String ACEPTAR_REPORTE = "aceptarReporte";
	//guarda campos de b\u00FAsqueda del tab Despachos
	private static final String CONSULTA_PEDIDOS_POR_DESPACHAR = "ec.com.smx.sic.sispe.pedidos.busqueda";
	//generaci\u00F3n de reportes
	private static final String RPT_PEDIDOLOCAL = "ec.com.smx.sic.sispe.reporte.pedidoLocal";
	private static final String RPT_ARTICULO = "ec.com.smx.sic.sispe.reporte.articulo";
	//variable de confirmaci\u00F3n del popUp
	private static final String ACEPTAR_CERRAR_DIA = "aceptarCerrarDia";
	//guarda campos de b\u00FAsqueda del tab Pendientes
	private static final String CONSULTA_PEDIDOS_PENDIENTES="ec.com.smx.sic.sispe.pedidos.busquedaPendientes";
	//guarda colecci\u00F3n de pedidos del tab pendientes
	public static final String COL_PEDIDOS_PENDIENTES="ec.com.smx.sic.sispe.despachoPedEsp.pedidosPendientes"; 
	private static final String COL_PAGINA_PEDIDOS = "ec.com.smx.sic.sispe.pedidos.subPagina";
	
	//guarda el objeto de busqueda de los art\u00EDculos
	private static final String CONSULTA_ARTICULOS="ec.com.smx.sic.sispe.despachoPedEsp.consultaArticulos";
	private static final String COL_PAGINA_ARTICULOS="ec.com.smx.sic.sispe.despachoPedEsp.paginaArticulos";
	
	private static final String INDICE_TAB_0 = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("despachoPedEsp.pagTab0");
	private static final String INDICE_TAB_1 = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("despachoPedEsp.pagTab1");
	private static final String INDICE_TAB_2 = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("despachoPedEsp.pagTab2");
	
	private static final String COL_LOCALES_MAIL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("despachoPedEsp.colLocalesMail");
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)	throws Exception{

		ActionMessages exitos = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		//ActionMessages warnings = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		
		HttpSession session= request.getSession();
		ControlProduccionPedidoEspecialForm formulario = (ControlProduccionPedidoEspecialForm)form;
		
		String peticion = request.getParameter(Globals.AYUDA);
		PaginaTab paginaTab = (PaginaTab)session.getAttribute(SessionManagerSISPE.PAGINA_TAB);
		String forward="desplegar";
		LogSISPE.getLog().info("peticion: {}",peticion);

		try{

			//------------------ b\u00FAsqueda -------------------------
			if(request.getParameter("buscar")!=null){
				LogSISPE.getLog().info("ENTRA A BOTON BUSCAR");
				if(paginaTab!=null && (paginaTab.esTabSeleccionado(1) || paginaTab.esTabSeleccionado(2))){
					//se realiza la consulta de los pedidos
					this.consultarPedidos(request, formulario, infos, errors);
				}else{
					//se realiza la consulta por art\u00EDculo
					this.consultarArticulosPorDespachar(request, formulario, infos, errors);
				}
			}

			//--------------- desplegar el detalle del pedido ------------------
			else if(request.getParameter("detalle")!=null){
				LogSISPE.getLog().info("**Entra a detalle Pedido**");
				ArrayList pedidos = null;
				VistaPedidoDTO consultaVistaPedidoDTO = null;
				int indice= Integer.parseInt(request.getParameter("detalle"));
				//obtener de la sesion la colecci\u00F3n de los pedidos en general
				pedidos = (ArrayList)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//se toma los detalles del registro seleccioando
				if(pedidos!=null && !pedidos.isEmpty()){
					consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);

					this.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
					LogSISPE.getLog().info("indice registro :" + indice);
				}
			}
			//--------------- despachar pedidos ------------------
			else if(request.getParameter("botonDespacharPed")!=null){
				LogSISPE.getLog().info("**ENTRA A BOTON DESPACHAR PEDIDOS");
				//se llama al m\u00E9todo que realiza las validaciones antes del despacho
				forward = this.verificarDetallesPedidos(request, formulario, errors, exitos, true);
			}
			//------------- despachar art\u00EDculos ---------------
			else if(request.getParameter("botonDespacharArticulos")!=null){
				LogSISPE.getLog().info("**ENTRA A BOTON DESPACHAR ARTICULOS");
				//se llama al m\u00E9todo que realiza las validaciones antes del despacho
				this.verificarValoresArticulos(request, session);
				Collection<VistaArticuloDTO> colVistaArticuloDTO = (Collection<VistaArticuloDTO>)session.getAttribute(COL_PAGINA_ARTICULOS);
				try{
					//se llama al m\u00E9todo que realiza el despacho
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarDespachoEspecialPorArticulo(colVistaArticuloDTO);
					//se realiza nuevamente la consulta de art\u00EDculos
					this.consultarArticulosPorDespachar(request, formulario, infos, errors);
					
					//se eliminan las variables relacionadas con los pedidos
					session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					session.removeAttribute(COL_PAGINA_PEDIDOS);
					session.removeAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR);
					session.removeAttribute(INDICE_TAB_1);
					
					exitos.add("exitoDespacho", new ActionMessage("message.exito.registro", "El despacho de Art\u00EDculos"));
				}catch(SISPEException ex){
					errors.add("errorDespacho", new ActionMessage("errors.llamadaServicio.registrarDatos", "el despacho por art\u00EDculo"));
					errors.add("errorSISPE", new ActionMessage("errors.SISPEException", ex.getMessage()));
				}
			}
			//---------- cuando se accede al detalle de un pedido por despachar desde el tab de pedidos ---------------
			else if(peticion != null && peticion.startsWith("dp")){
				LogSISPE.getLog().info("**ENTRA A REVISAR EL DETALLE ");
				//se llama al m\u00E9todo que realiza las validaciones antes del despacho
				this.verificarDetallesPedidos(request, formulario, errors, exitos, false);
				int indice = Integer.parseInt(peticion.substring(2));
				//si el pedido est\u00E1 en el tab de despacho
				if(paginaTab.esTabSeleccionado(1)){
					LogSISPE.getLog().info("indice seleccionado tab despacho: {}",indice);
					session.setAttribute(DetalleEstadoPedidoEspecialAction.INDICE_PEDIDO_SELECCIONADO, Integer.valueOf(indice));
				}
				forward = "detallePedido";
			}
			//-- se accede al detalle de un pedido desde el tab de art\u00EDculos --
			else if(peticion!=null && peticion.startsWith(SessionManagerSISPE.PREFIJO_IPVA)){
				//m\u00E9todo que reasigna las cantidades ingresadas
				this.verificarValoresArticulos(request, session);
				session.setAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO, peticion);

				forward = "detallePedido";
			}
			//-------------- cuando se acepta el despacho de un pedido --------------
			else if(peticion != null && peticion.equals(VALOR_SI_DESPACHAR)){
				LogSISPE.getLog().info("entra a si_despachar");
				//verifico que hay pedidos en la sesion
				if(session.getAttribute(PEDIDOS_DESPACHADOS)!=null){
					LogSISPE.getLog().info("tomo de sesi\u00F3n pedidos despachados");
					//tomo de sesion los pedidos seleccionados, los asigno a la colecci\u00F3n
					Collection<VistaPedidoDTO> colVistaPedidoDTO = (ArrayList<VistaPedidoDTO>)session.getAttribute(PEDIDOS_DESPACHADOS);
					try{
						//se realiza el despacho
						this.realizarDespacho(request, exitos, errors, colVistaPedidoDTO, formulario);
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					}
				}
			}
			//------------------ se realiza el cierre de d\u00EDa ------------------
			else if(request.getParameter("cerrarDia")!=null){
				LogSISPE.getLog().info("entra a cerrarDia");

				//se crea la ventana popUp
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Confirmaci\u00F3n de cerrar d\u00EDa");
				popUp.setMensajeVentana("Si usted cierra el d\u00EDa, los siguientes pedidos que se crear\u00E1n tendr\u00E1n " +
						"la fecha m\u00EDnima de despacho aumentada un d\u00EDa. " +
				"Si usted cierra el d\u00EDa, ya no lo podr\u00E1 abrir ");
				popUp.setPreguntaVentana("\u00BFDesea cerrar el d\u00EDa?");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setValorOK(ACEPTAR_CERRAR_DIA);
				popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
				popUp.setEtiquetaBotonOK("Aceptar");
				popUp.setEtiquetaBotonCANCEL("Cancelar");
				popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
				//se guarda en sesion
				request.setAttribute(SessionManagerSISPE.POPUP, popUp);
			}
			//------------------ aceptar cierre de d\u00EDa ------------------
			else if(peticion!=null && peticion.equals(ACEPTAR_CERRAR_DIA)){
				//parametro que se envia en el m\u00E9todo para registrar el cierre
				VistaPedidoDTO vistaPedidoDTO = new VistaPedidoDTO();
				vistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
				vistaPedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				//se envia el c\u00F3digo del tipo de pedido
				vistaPedidoDTO.setCodigoTipoPedido((String)session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER));				
				LogSISPE.getLog().info("codigo tipo pedido {}", vistaPedidoDTO.getCodigoTipoPedido());
				try{
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarCierreDiaPedidoEspecial(vistaPedidoDTO);

					//muestra mensaje de que puede despachar
					session.removeAttribute(MOSTRAR_INFO);
					//habilitar boton despacho
					session.setAttribute(HABILITAR_DESPACHO, "ok");

					//mensaje de \u00E9xito: puede despachar pedidos
					exitos.add("cerrarDia",new ActionMessage("messages.especial.cerrarDia.exito"));

					if(paginaTab!=null && (paginaTab.esTabSeleccionado(1) || paginaTab.esTabSeleccionado(2))){
						//se realiza la consulta de los pedidos
						this.consultarPedidos(request, formulario, infos, errors);
						//se eliminan las variables relacionadas con los art\u00EDculos
						session.removeAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
						session.removeAttribute(COL_PAGINA_ARTICULOS);
						session.removeAttribute(CONSULTA_ARTICULOS);
						session.removeAttribute(INDICE_TAB_0);
					}else{
						//se realiza la consulta por art\u00EDculo
						this.consultarArticulosPorDespachar(request, formulario, infos, errors);
						//se eliminan las variables relacionadas con los pedidos
						session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
						session.removeAttribute(COL_PAGINA_PEDIDOS);
						session.removeAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR);
						session.removeAttribute(INDICE_TAB_1);
					}

				}catch(SISPEException ex){
					errors.add("errorCierre", new ActionMessage("errors.llamadaServicio.registrarDatos", "El Cierre de D\u00EDa"));
				}
			}
			
			//------------------- muestra una ventana donde se muestran los tipos de reportes a mostrar ---------------------
			else if(request.getParameter("crearReporte")!=null){
				LogSISPE.getLog().info("entra a crearReporte");
				//dependiendo del tab en el que se encuentre revisa si hay colecci\u00F3n para poder llamar al popup del reporte.
				if(paginaTab.esTabSeleccionado(1) && session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL)==null){
					errors.add("errorReporte",new ActionMessage("errors.numeroPedido.requerido"));
				}else if(paginaTab.esTabSeleccionado(2) && session.getAttribute(COL_PEDIDOS_PENDIENTES)==null){
					errors.add("errorReporte",new ActionMessage("errors.numeroPedido.requerido"));
				}else{
					//se crea la ventana popUp
					UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Opciones de agrupaci\u00F3n");
					popUp.setMensajeVentana("Escoja una de las siguientes opciones para crear el reporte: ");
					popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
					popUp.setValorOK(ACEPTAR_REPORTE);
					popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
					popUp.setEtiquetaBotonOK("Aceptar");
					popUp.setEtiquetaBotonCANCEL("Cancelar");
					popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
					popUp.setContenidoVentana("/pedidosEspeciales/modificacionYDespacho/opcionesDespacho.jsp");
					formulario.setOpTipoAgrupacion("lp");

					//se guarda
					request.setAttribute(SessionManagerSISPE.POPUP, popUp);
				}
			}
			//------------------- se genera la consulta para mostrar los reportes --------------------
			else if(peticion != null && peticion.equals(ACEPTAR_REPORTE)){
				LogSISPE.getLog().info("entrar a aceptarReporte");
				VistaReporteGeneralDTO vistaReporteGeneralDTO = new VistaReporteGeneralDTO();

				//si hay resultados en la b\u00FAsqueda de art\u00EDculos por despachar
				if(session.getAttribute(CONSULTA_ARTICULOS)!= null && paginaTab.esTabSeleccionado(0)){
					VistaArticuloDTO vistaArticuloDTO = (VistaArticuloDTO)session.getAttribute(CONSULTA_ARTICULOS);
					//codigo de la compania
					vistaReporteGeneralDTO.getId().setCodigoCompania(vistaArticuloDTO.getId().getCodigoCompania());
					//codigo del local actual
					vistaReporteGeneralDTO.getId().setCodigoAreaTrabajo(vistaArticuloDTO.getId().getCodigoAreaTrabajo());
					//codigo estado
					vistaReporteGeneralDTO.getId().setCodigoEstado(vistaArticuloDTO.getId().getCodigoEstado());
					//codigo pedido
					vistaReporteGeneralDTO.getId().setCodigoPedido(vistaArticuloDTO.getId().getCodigoPedido());
					//codigo art\u00EDculo
					vistaReporteGeneralDTO.getId().setCodigoArticulo(vistaArticuloDTO.getId().getCodigoArticulo());
					//descripcion art\u00EDculo
					vistaReporteGeneralDTO.setDescripcionArticulo(vistaArticuloDTO.getDescripcionArticulo());
					//c\u00F3digo de clasificaci\u00F3n
					vistaReporteGeneralDTO.setCodigoClasificacion(vistaArticuloDTO.getCodigoClasificacion());

					vistaReporteGeneralDTO.setCedulaContacto(vistaArticuloDTO.getCedulaContacto());
					vistaReporteGeneralDTO.setNombreContacto(vistaArticuloDTO.getNombreContacto());
					vistaReporteGeneralDTO.setRucEmpresa(vistaArticuloDTO.getRucEmpresa());
					vistaReporteGeneralDTO.setNombreEmpresa(vistaArticuloDTO.getNombreEmpresa());
					vistaReporteGeneralDTO.setPrimeraFechaDespachoDesde(vistaArticuloDTO.getNpPrimeraFechaDespachoInicialTimestamp());
					vistaReporteGeneralDTO.setPrimeraFechaDespachoHasta(vistaArticuloDTO.getNpPrimeraFechaDespachoFinalTimestamp());
					vistaReporteGeneralDTO.setCodigoTipoPedido(vistaArticuloDTO.getCodigoTipoPedido());
					vistaReporteGeneralDTO.setNpEstadoEnProceso(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado"));
				}else{
					VistaPedidoDTO vistaPedidoDTO = null;
					
					//si hay resultados en la b\u00FAsqueda de pedidos por despachar y est\u00E1 en el tab de despacho
					if(session.getAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR)!=null && paginaTab.esTabSeleccionado(1)){
						vistaPedidoDTO =(VistaPedidoDTO)session.getAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR);
					}
					//si hay resultados en la b\u00FAsqueda de pedidos pendientes y est\u00E1 en el tab de pendientes
					else if(session.getAttribute(CONSULTA_PEDIDOS_PENDIENTES)!=null && paginaTab.esTabSeleccionado(2)){
						vistaPedidoDTO =(VistaPedidoDTO)session.getAttribute(CONSULTA_PEDIDOS_PENDIENTES);
					}
					
					if(vistaPedidoDTO != null){
						//codigo de la compania
						vistaReporteGeneralDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
						//codigo del local actual
						vistaReporteGeneralDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						//codigo estado
						vistaReporteGeneralDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
						//codigo pedido
						vistaReporteGeneralDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
						//filtros de art\u00EDculos
						if(vistaPedidoDTO.getArticuloDTO() != null){
							LogSISPE.getLog().info("codigo articulo: "+vistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
							vistaReporteGeneralDTO.getId().setCodigoArticulo(vistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
							vistaReporteGeneralDTO.setCodigoClasificacion(vistaPedidoDTO.getArticuloDTO().getCodigoClasificacion());
							vistaReporteGeneralDTO.setDescripcionArticulo(vistaPedidoDTO.getArticuloDTO().getDescripcionArticulo());
						}
						vistaReporteGeneralDTO.setCedulaContacto(vistaPedidoDTO.getCedulaContacto());
						vistaReporteGeneralDTO.setNombreContacto(vistaPedidoDTO.getNombreContacto());
						vistaReporteGeneralDTO.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
						vistaReporteGeneralDTO.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
						vistaReporteGeneralDTO.setPrimeraFechaDespachoDesde(vistaPedidoDTO.getNpPrimeraFechaDespachoInicial());
						vistaReporteGeneralDTO.setPrimeraFechaDespachoHasta(vistaPedidoDTO.getNpPrimeraFechaDespachoFinal());
						vistaReporteGeneralDTO.setCodigoTipoPedido(vistaPedidoDTO.getCodigoTipoPedido());
	
						//si es pendiente asigno otro criterio de b\u00FAsqueda
						if(paginaTab.esTabSeleccionado(1)){
							vistaReporteGeneralDTO.setNpEstadoEnProceso(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado"));
						}else{
							LogSISPE.getLog().info("asigno nuevo criterio de b\u00FAsqueda a reporte");
							vistaReporteGeneralDTO.setNpFechaCierrePedidoEspecialNula(session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO).toString());
						}
					}
				}
				
				vistaReporteGeneralDTO.setEstadoActual(true);
				//no obtener entregas
				vistaReporteGeneralDTO.setNpNoObtenerEntregas("ok");
				
				//se verifica que tipo de resporte se escogi\u00F3
				if(formulario.getOpTipoAgrupacion().equals("lp")){
					LogSISPE.getLog().info("escogi\u00F3 localPedido");
					vistaReporteGeneralDTO.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.pedidosPorLocal"));
					ArrayList<VistaReporteGeneralDTO> vistaReporteGeneralDTOcol = (ArrayList<VistaReporteGeneralDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralDTO);

					session.removeAttribute(RPT_ARTICULO);
					session.setAttribute(RPT_PEDIDOLOCAL, vistaReporteGeneralDTOcol);
					LogSISPE.getLog().info("va a imprimir el pedido");
					//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
					request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
				}else{
					vistaReporteGeneralDTO.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.articulos"));
					ArrayList<VistaReporteGeneralDTO> vistaReporteGeneralDTOcol = (ArrayList<VistaReporteGeneralDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralDTO);
					LogSISPE.getLog().info("escogi\u00F3 art\u00EDculo ");
					session.removeAttribute(RPT_PEDIDOLOCAL);
					session.setAttribute(RPT_ARTICULO, vistaReporteGeneralDTOcol);
					LogSISPE.getLog().info("va a imprimir el pedido");
					//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
					request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
				}
				
				forward="reporte";
			}

			//--------------- Control de Tabs --------------------
			else if(paginaTab!= null && paginaTab.comprobarSeleccionTab(request)){
				
				int start = 0;
				int size = 0;
				//--- si se escogi\u00F3 el tab de los articulos por despachar ---
				if(paginaTab.esTabSeleccionado(0)){
					LogSISPE.getLog().info("tab despachar articulos");
					if(session.getAttribute(INDICE_TAB_0) != null && session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO) != null){
						start = Integer.valueOf(session.getAttribute(INDICE_TAB_0).toString());
						size = ((Collection)session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO)).size();
						
						//valores de la paginaci\u00F3n
						formulario.setStart(String.valueOf(start));
						formulario.setSize(String.valueOf(size));
					}else{
						//se consultan los posibles art\u00EDculos
						this.consultarArticulosPorDespachar(request, formulario, infos, errors);
					}
					//se respaldan los valores ingresados en los pedidos
					this.verificarDetallesPedidos(request, formulario, errors, exitos, false);
				}
				//si esogi\u00F3 el tab de pedidos a despachar
				else if (paginaTab.esTabSeleccionado(1)) {
					LogSISPE.getLog().info("tab despachar pedidos");

					if(session.getAttribute(INDICE_TAB_1) != null && session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL) != null){
						start = Integer.valueOf(session.getAttribute(INDICE_TAB_1).toString());
						size = ((Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL)).size();
						
						//valores de la paginaci\u00F3n
						formulario.setStart(String.valueOf(start));
						formulario.setSize(String.valueOf(size));
						
					}else{
						//se consultan los posibles pedidos
						this.consultarPedidos(request, formulario, infos, errors);
					}

					//se respaldan los valores ingresados en la secci\u00F3n de art\u00EDculos
					this.verificarValoresArticulos(request, session);
				}
				// si escogi\u00F3 el tab de pedidos pendientes(con cierre de d\u00EDa)
				else if(paginaTab.esTabSeleccionado(2)){
					//si no se cerr\u00F3 el d\u00EDa
					LogSISPE.getLog().info("tab pendientes");
					if(session.getAttribute(MOSTRAR_INFO)!=null){
						infos.add("cerrarDia",new ActionMessage("info.especial.cerrarDia.Pendiente"));
					}
					
					//se respaldan los valores ingresados en la secci\u00F3n de art\u00EDculos
					this.verificarValoresArticulos(request, session);
				}
				
				formulario.setCheckSeleccionarTodo(null);
				formulario.setChecksSeleccionar(null);
				

			}
			//------------------ habilitar el despacho de los pedidos pendientes -----------------------
			else if(request.getParameter("cerrarPendiente")!=null){
				LogSISPE.getLog().info("entra a cerrar pendiente");
				//coleccion que guarda los pedidos que se mandar\u00E1n a despachar
				Collection<VistaPedidoDTO> colVistaPedidoDTO = new ArrayList<VistaPedidoDTO>();
				String [] checks = formulario.getChecksSeleccionar();
				if(checks == null){
					errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un pedido"));
				}else{
					LogSISPE.getLog().info("checks seleccionados: "+checks.length);
					//obtengo la colecci\u00F3n de pedidos buscados
					ArrayList<VistaPedidoDTO> colPedidos = (ArrayList<VistaPedidoDTO>)session.getAttribute(COL_PEDIDOS_PENDIENTES);
					for(int indice=0;indice<checks.length;indice++){
						VistaPedidoDTO vistaPedidoDTO=colPedidos.get((new Integer(checks[indice])).intValue());
						colVistaPedidoDTO.add(vistaPedidoDTO);
					}
					
					try{
						//llamo a la funci\u00F3n que despacha.
						SessionManagerSISPE.getServicioClienteServicio().transHabilitarDespachoPedidosEspeciales(colVistaPedidoDTO);
						exitos.add("despachoRealizado", new ActionMessage("message.exito.registro","El cierre de los pedidos pendientes"));
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("despachoNoRealizado", new ActionMessage("errors.llamadaServicio.registrarDatos", "El Despacho"));
					}
					
					formulario.setCheckSeleccionarTodo(null);
					formulario.setChecksSeleccionar(null);
					this.consultarPedidos(request, formulario, null, null);
				}
			}
			//--------------------- paginacion ------------------------
			else if(request.getParameter("range")!=null || request.getParameter("start")!=null)
			{
				LogSISPE.getLog().info("ENTRO A LA PAGINACION");
				Collection datos = null;
				String nombrePagina = "";
				if(paginaTab!=null && paginaTab.esTabSeleccionado(0)){
					datos = (Collection)session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
					session.setAttribute(INDICE_TAB_0, request.getParameter("start"));
					nombrePagina = COL_PAGINA_ARTICULOS;
				}else if(paginaTab!=null && paginaTab.esTabSeleccionado(1)){
					datos = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					session.setAttribute(INDICE_TAB_1, request.getParameter("start"));
					nombrePagina = COL_PAGINA_PEDIDOS;
				}

				if(datos!=null){
					formulario.setSize(String.valueOf(datos.size()));
					int size= datos.size();
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango30"));
					int start= Integer.parseInt(request.getParameter("start"));
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
					session.setAttribute(nombrePagina, datosSub);
					formulario.setChecksSeleccionar(null);
				}
				
			}
			//------------ se realiza el despacho previo ---------------
			else if(request.getParameter("despachoPrevio") != null){
				//se obtienen los seleccionados
				String [] checks = formulario.getChecksSeleccionar();
				if(checks != null){
					//se obtiene el usuario logeado
					String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();					
					Collection<VistaPedidoDTO> colPedidosSeleccionados = new ArrayList<VistaPedidoDTO>();
					//se obtiene la colecci\u00F3n de pedidos
					List<VistaPedidoDTO> listVistaPedidoDTO = (List<VistaPedidoDTO>)session.getAttribute(COL_PAGINA_PEDIDOS);
					for(int i=0;i<checks.length;i++){
						int indicePedido = Integer.parseInt(checks[i]);
						VistaPedidoDTO vistaPedidoDTO = listVistaPedidoDTO.get(indicePedido);
						if(vistaPedidoDTO.getVistaDetallesPedidos()!=null){
							int indiceDetalle = 0;
							//se iteran los detalles del pedido seleccionado
							for(Iterator<VistaDetallePedidoDTO> it = vistaPedidoDTO.getVistaDetallesPedidos().iterator();it.hasNext();){
								VistaDetallePedidoDTO vistaDetallePedidoDTO = it.next();
								//se verifica el valor del peso ingresado
								if(request.getParameter(indicePedido + "-" + indiceDetalle) != null){
									try{
										vistaDetallePedidoDTO.setPesoRegistradoBodega(Double.parseDouble(request.getParameter(indicePedido + "-" + indiceDetalle)));
									}catch(NumberFormatException ex){
										vistaDetallePedidoDTO.setPesoRegistradoBodega(0D);
									}
								}else{
									vistaDetallePedidoDTO.setPesoRegistradoBodega(0D);
								}
								indiceDetalle ++;
							}
						}
						
						vistaPedidoDTO.setUserId(userId);
						//el indice del pedido est\u00E1 en el valor que contiene cada check
						colPedidosSeleccionados.add(vistaPedidoDTO);
					}

					try{
						//se registra los datos en la base mediante el m\u00E9todo del servicio
						SessionManagerSISPE.getServicioClienteServicio().transRegistrarDespachoPrevioPedidoEspecial(colPedidosSeleccionados);
						
						//Parametros necesarios para buscar la plantilla, deben ser ingresados por cada sistema
						EventoID eventoID = new EventoID();
						eventoID.setCodigoEvento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.despachoPrevio"));
						eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
						eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						EventoDTO eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);

						String descripcionEvento = "";
						String nombreUsuario = SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreCompletoUsuario();
						
						LocalID localID = new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						//se procede al env\u00EDo del mail al local que realiz\u00F3 el pedido
						for(VistaPedidoDTO vistaPedidoDTO : colPedidosSeleccionados){
							localID.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
							LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);
							if(localDTO != null && localDTO.getEmailLocal()!=null){
								String [] mail = new String [] {localDTO.getEmailLocal()};
								//se realiza el proceso de env\u00EDo
								MailMensajeEST mailMensajeEST = new MailMensajeEST();
								mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuenta.sispe"));
								mailMensajeEST.setPara(mail);
								descripcionEvento = eventoDTO.getDescripcionEvento().replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.numeroPedido"),vistaPedidoDTO.getId().getCodigoPedido());
								descripcionEvento = descripcionEvento.replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.nombreUsuario"), nombreUsuario);
								mailMensajeEST.setAsunto(eventoDTO.getAsuntoEvento());
								mailMensajeEST.setMensaje(descripcionEvento);
								mailMensajeEST.setEventoDTO(eventoDTO);
								//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
								mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
								mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
								//metodo para enviar el mail
								SessionManagerSISPE.getMensajeria().transEnvioMail(mailMensajeEST, false);
								LogSISPE.getLog().info("***MAIL ENVIADO***");
							}
						}
						//mensaje de exito
						exitos.add("despachoPrevio", new ActionMessage("message.exito.registro", "El Despacho Previo"));
					}catch(Exception ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("errorDespachoPrevio", new ActionMessage("errors.llamadaServicio.registrarDatos", "el despacho previo"));
						errors.add("errorSISPE", new ActionMessage("errors.SISPEException", ex.getMessage()));
					}
				}else{
					errors.add("seleccion", new ActionMessage("errors.seleccion.requerido", "Pedido"));
				}
			}
			//---------------- despacho previo por art\u00EDculo ----------------
			else if(request.getParameter("desPreArt") != null){
				LogSISPE.getLog().info("**DESPACHO PREVIO POR ARTICULO");
				//se llama al m\u00E9todo que realiza las validaciones antes del despacho
				List<VistaArticuloDTO> lisVistaArticuloDTO = (List<VistaArticuloDTO>)this.verificarValoresArticulos(request, session);
				
				Collection<VistaArticuloDTO> colVistaArticuloFinal = new ArrayList<VistaArticuloDTO>();
				//se obtiene el primer elemento de la colecci\u00F3n para inciar la comparaci\u00F3n, debe ser clonado para que no afecte a la colecci\u00F3n original
				VistaArticuloDTO vistaArticuloDTOReferencia = lisVistaArticuloDTO.get(0).clone();
				Collection<VistaArticuloDTO> detalleArticulos = new ArrayList<VistaArticuloDTO>();
				int indice = -1;
				
				//se realiza la iteraci\u00F3n de los art\u00EDculos
				for(VistaArticuloDTO vistaArticuloDTO : lisVistaArticuloDTO){
					indice ++;
					//mientras los c\u00F3digos son iguales se agregan todos los detalles del pedido en otra colecci\u00F3n
					if(vistaArticuloDTO.getId().getCodigoAreaTrabajo().equals(vistaArticuloDTOReferencia.getId().getCodigoAreaTrabajo())){
						detalleArticulos.add(vistaArticuloDTO);
					}else{
						vistaArticuloDTOReferencia.setColVistaArticuloDTO(detalleArticulos);
						colVistaArticuloFinal.add(vistaArticuloDTOReferencia);

						//se inicializa nuevamente el proceso con otra referencia, debe ser clonado para que no afecte a la colecci\u00F3n original
						vistaArticuloDTOReferencia = lisVistaArticuloDTO.get(indice).clone();
						detalleArticulos = new ArrayList<VistaArticuloDTO>();
						detalleArticulos.add(vistaArticuloDTO);
					}
				}
				//se agrega el \u00FAltimo elemento a la colecci\u00F3n final
				vistaArticuloDTOReferencia.setColVistaArticuloDTO(detalleArticulos);
				colVistaArticuloFinal.add(vistaArticuloDTOReferencia);
				
				//se obtiene el evento asociado al email
				EventoID eventoID = new EventoID();
				eventoID.setCodigoEvento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.despachoPrevio"));
				eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
				eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				EventoDTO eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);
				
				Collection<MailMensajeEST> colMailMensajeEST = new ArrayList<MailMensajeEST>();
				LocalID localID = new LocalID();
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//se realiza la iteraci\u00F3n para realizar el env\u00EDo de los emails a cada local
				for(VistaArticuloDTO vistaArticuloDTO : colVistaArticuloFinal){
					//se busca el email por cada local
					localID.setCodigoLocal(vistaArticuloDTO.getId().getCodigoAreaTrabajo());
					LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);
					
					if(localDTO!=null && localDTO.getEmailLocal()!=null){
						//se construye la estructura para el enviar el email
						MailMensajeEST mailMensajeEST = new MailMensajeEST();
						mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuenta.sispe"));
						mailMensajeEST.setPara(new String [] {localDTO.getEmailLocal()});						
						mailMensajeEST.setAsunto(eventoDTO.getAsuntoEvento());
						//MailMensajeEST.setMensaje(descripcionEvento);
						mailMensajeEST.setEventoDTO(eventoDTO);						
						mailMensajeEST.setMensaje("");
						//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
						mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
						mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
						mailMensajeEST.setFormatoHTML(true);
						//se agrega a la colecci\u00F3n de estructuras del email
						colMailMensajeEST.add(mailMensajeEST);
					}
				}
				//se guarda la colecci\u00F3n de estructuras
				session.setAttribute(SessionManagerSISPE.COL_ESTRUCTURA_EMAIL, colMailMensajeEST);
				session.setAttribute(COL_LOCALES_MAIL, colVistaArticuloFinal);
				
				//mensaje de exito
				exitos.add("despachoPrevio", new ActionMessage("message.exito.registro", "El Despacho Previo"));
			}else if(request.getParameter("reporteDespachos") != null){
				LogSISPE.getLog().info("reporte Despachos");

				//se crea la ventana popUp
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Reporte de despachos");
				popUp.setMensajeVentana("Seleccione el rango de fechas de despacho para generar el reporte");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
				popUp.setContenidoVentana("pedidosEspeciales/modificacionYDespacho/popUpReporteDespachos.jsp");
				popUp.setValorOK("enviarFormulario('xls', 0, false);hide(['popupConfirmar']);ocultarModal();");
				popUp.setEtiquetaBotonOK("Aceptar");
				popUp.setEtiquetaBotonCANCEL("Cancelar");
				popUp.setValorCANCEL("requestAjax('controlProduccionPedEsp.do', ['pregunta'], {parameters: 'cancelarReporteDespachos=ok',popWait:false});ocultarModal();");
				popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
				//se guarda en sesion
				request.setAttribute(SessionManagerSISPE.POPUP, popUp);
				formulario.setFechaFinalRepDespachos(formulario.getFechaFinal());
				formulario.setFechaInicialRepDespachos(formulario.getFechaInicial());

			}else if(request.getParameter("cancelarReporteDespachos") != null){
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);/*remove pop up*/
			}else if (peticion != null && peticion.equals("xls")) {
				LogSISPE.getLog().info("GENERAR ARCHIVO XLS");
				if(formulario.getFechaInicialRepDespachos()!=null && formulario.getFechaFinalRepDespachos()!=null){
					request.getSession().removeAttribute(SessionManagerSISPE.POPUP);/*remove pop up*/
					byte [] byteBuffer =null;
					try {
						byteBuffer = crearExcelReporteDespachos(request, formulario );	
					} catch (Exception e) {
						LogSISPE.getLog().error("Error al generar reporte Despachos XLS",e);
						errors.add("Reporte despachos", new ActionMessage("","Error al generar el reporte de despachos"));
					}
					
					
					if (byteBuffer != null && byteBuffer.length > 0) {
						
						String fileName =  WebSISPEUtil.generarNombreArchivo("Reporte Despachos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel"));
						
						response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
						String contentType = "application/ms-excel";
						response.setContentType(contentType);
						ServletOutputStream outputStream = response.getOutputStream();
						outputStream.write(byteBuffer);
						outputStream.flush();
						outputStream.close();
						forward=null;
					}
				}else{
					LogSISPE.getLog().info("No ha escogido fechas para realizar el reporte");
				}
			}
			//----------------------- por omisi\u00F3n ----------------------	
			else{
				//b\u00FAsqueda por omisi\u00F3n
				//se eliminan las variables de sesi\u00F3n que comiencen con "ec.com"
				SessionManagerSISPE.removeVarSession(request);

				//primero se verifica si el rol del usuario logeado est\u00E1 configurado en los par\u00E1metros
				String codigoTipoPedidoUsuario = WebSISPEUtil.verificarUsuarioPedidoEspecial(SessionManagerSISPE.getCurrentEntidadResponsable(request), request);

				if(codigoTipoPedidoUsuario != null){
					//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
					session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "B\u00FAsqueda de pedidos especiales");
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaControlProduccionPedEsp"));

					//se inicializan los radios de b\u00FAsqueda
					
					// IMPORTANTE: Se comenta campo campoBusqueda para habilitar consulta m\u00FAltiple
					// ------------------------------------------------------------------------------------------------------------
					//formulario.setOpcionCampoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
					// ------------------------------------------------------------------------------------------------------------
					
					formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"));
					//se formatean las fechas
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
					Timestamp fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 1, 0, 0, 0, 0));
					Timestamp fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 1, 23, 59, 59, 999));
					
					//inicializa campo de b\u00FAsqueda por fechas con la del d\u00EDa de ma\u00F1ana
					formulario.setFechaInicial(simpleDateFormat.format(fechaInicial));
					formulario.setFechaFinal(simpleDateFormat.format(fechaFinal));
					formulario.setEtiquetaFechas("Fecha de Despacho");
					
					// IMPORTANTE: Se comenta campo campoBusqueda para habilitar consulta m\u00FAltiple
					// ---------------------------------------------------------
					//formulario.setCampoBusqueda("");
					// ---------------------------------------------------------

					session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de despacho");

					//se verifica si la entidad responsable es un local
					String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
					if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
						formulario.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal().toString());
					else{
						//se obtienen los locales por ciudad
						SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
					}
					//Iniciar Tabs
					PaginaTab tabsDespachos = new PaginaTab("controlProduccionPedEsp", "desplegar", 0, 440, request);
					Tab tabArticulosPorDespachar = new Tab("Articulos", "controlProduccionPedEsp", "/pedidosEspeciales/modificacionYDespacho/tabDespachoArticulos.jsp", true);
					Tab tabPedidosPorDespachar = new Tab("Pedidos", "controlProduccionPedEsp", "/pedidosEspeciales/modificacionYDespacho/tabDespachoPedidos.jsp", false);
					Tab tabPedidosDespachados = new Tab("Pendientes", "controlProduccionPedEsp", "/pedidosEspeciales/modificacionYDespacho/tabPendientes.jsp", false);
					tabsDespachos.addTab(tabArticulosPorDespachar);
					tabsDespachos.addTab(tabPedidosPorDespachar);
					tabsDespachos.addTab(tabPedidosDespachados);

					session.setAttribute(SessionManagerSISPE.PAGINA_TAB, tabsDespachos);

					//verifica si el dia se ha cerrado
					boolean diaCerrado = this.verificarCierreDiaPedidosEspeciales(codigoTipoPedidoUsuario);
					if(diaCerrado){
						session.removeAttribute(MOSTRAR_INFO);
						LogSISPE.getLog().info("ya puede despachar");
						//habilita boton despacho y deshabilita boton cerrar dia
						session.setAttribute(HABILITAR_DESPACHO,"ok");
					}else{
						//muestra mensaje de informaci\u00F3n
						session.setAttribute(MOSTRAR_INFO, "ok");
						LogSISPE.getLog().info("debe cerrar dia");	
						infos.add("cerrarDia",new ActionMessage("info.especial.cerrarDia.cerrar"));
					}

					//se realiza la consulta por art\u00EDculo
					this.consultarArticulosPorDespachar(request, formulario, infos, errors);
					
				}else{
					errors.add("errorRol", new ActionMessage("errors.configuracionRolPedidosEspeciales"));
				}
			}

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			forward = "errorGlobal";
		}

		//se guardan los mensajes
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);

		return mapping.findForward(forward);
	}

	/**
	 * Obtiene los detalles de los pedidos.
	 * @param request
	 * @param formulario
	 * @param errors
	 * @param realizarDespacho
	 * @return
	 * @throws Exception
	 */
	private String verificarDetallesPedidos(HttpServletRequest request, ControlProduccionPedidoEspecialForm formulario, ActionMessages errors, 
			ActionMessages exitos, boolean realizarDespacho) throws Exception{

		HttpSession session = request.getSession();
		
		//tomo la coleccion pedidos seleccionados
		LogSISPE.getLog().info("detalle tab 1");
		List<VistaPedidoDTO> colPedidos = (List<VistaPedidoDTO>)session.getAttribute(COL_PAGINA_PEDIDOS);

		if(colPedidos != null){
			
			String estadoError = null;
			//se obtiene el usuario
			String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
			String filasNoDespachadas = "";
			
			if(realizarDespacho){
				estadoError = "1";
			}
			//colecci\u00F3n que guarda los pedidos que se mandar\u00E1n a despachar
			Collection<VistaPedidoDTO> colVistaPedidoDTO = new ArrayList<VistaPedidoDTO>();
			//se obtienen los seleccionados
			String [] checks = formulario.getChecksSeleccionar();
			//contador de pesos en cero
			int contPesosCero = 0;

			if(checks == null && realizarDespacho){
				errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un pedido"));
			}
			
			//se recorre el arreglo de indices de los pedidos
			for(int indicePedido = 0; indicePedido<colPedidos.size();indicePedido++){
				VistaPedidoDTO vistaPedidoDTO = colPedidos.get(indicePedido);
				vistaPedidoDTO.setNpEstadoError(null);
				LogSISPE.getLog().info("indice: "+indicePedido);
	
				//toma los detalles del registro seleccionado
				List detallePedido=(List)vistaPedidoDTO.getVistaDetallesPedidos();
				if(detallePedido!=null){
	
					boolean indiceEncontrado = false;
					//si hay checks seleccionados se verifica si el indice del pedido actual est\u00E1 en el arreglo
					if(checks!=null){
						//se busca el indice del pedido en la colecci\u00F3n de los checks seleccionados, 
						//en el momento que lo encuentra valida sus valores
						for(int i=0;i<checks.length;i++){
							if(indicePedido == Integer.valueOf(checks[i])){
								LogSISPE.getLog().info("indice seleccionado : {}",indicePedido);
								indiceEncontrado = true;
								break;
							}
						}
					}
	
					LogSISPE.getLog().info("*** detalleSize: {}",detallePedido.size());
					//se recorre los detalles del pedido
					for(int indiceDetalle=0;indiceDetalle<detallePedido.size();indiceDetalle++){
						//si existe un valor ingresado
						if(request.getParameter(indicePedido+"-"+indiceDetalle)!=null){
							//obtiene el valor del peso que corresponde al indice del detalle
							String datoPeso = request.getParameter(indicePedido+"-"+indiceDetalle);
							//objeto que guardar\u00E1 el peso ingresado
							VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)detallePedido.get(indiceDetalle);
							vistaDetallePedidoDTO.setNpEstadoError(null);
							double peso = 0;
							LogSISPE.getLog().info("validar peso");
	
							try{
								peso = Double.parseDouble(datoPeso);
								if(peso == 0){
									formulario.setPeso("0.0");
								}else if(peso < 0){
									peso = (-1)*peso;
								}
							}catch(NumberFormatException ex){
								formulario.setPeso("0.0");
								peso = 0;
							}
	
							//si es un pedido seleccionado, si el peso es cero y se est\u00E1 realizando el despacho
							if(indiceEncontrado && peso == 0 && realizarDespacho){
								//pinta la fila con peso=0 de color rojo
								vistaDetallePedidoDTO.setNpEstadoError(estadoError);
								vistaPedidoDTO.setNpEstadoError(estadoError);
									
								contPesosCero ++;
							}
	
							//session.setAttribute(CERRAR_DIA_OK,"ok");
							vistaDetallePedidoDTO.setPesoRegistradoBodega(peso);
						}
					}
	
					//se agrega el pedido a la colecci\u00F3n final
					if(indiceEncontrado){
						//se asigna el usuario logeado en el sistema, para que el nuevo estado se guarde con el usuario correcto
						vistaPedidoDTO.setUserId(userId);
						colVistaPedidoDTO.add(vistaPedidoDTO);
						
						if(vistaPedidoDTO.getFechaCierrePedidoEspecial() == null){
							//se guardan las filas que no cumplen
							filasNoDespachadas = filasNoDespachadas.concat(String.valueOf(indicePedido + 1)).concat(",");
						}
					}
				}

			}
	
			LogSISPE.getLog().info("**tama\u00F1o coleccion 1: {}",colVistaPedidoDTO.size());
	
			//solo si se va a realizar el despacho
			if(realizarDespacho){
				if(!filasNoDespachadas.equals("")){
					//se genera un error indicando los pedidos que no se despachar\u00E1n
					errors.add("noDespachados", new ActionMessage("errors.filasNoProcesadas", filasNoDespachadas, "est\u00E1n deshabilitados para ser despachados."));
				}else	if(!colVistaPedidoDTO.isEmpty()){
					//subo a sesi\u00F3n los pedidos seleccionados
					session.setAttribute(PEDIDOS_DESPACHADOS,colVistaPedidoDTO); 
		
					if(contPesosCero > 0){
						//se crea la ventana popUp
						LogSISPE.getLog().info("entra a pesosencero");
						UtilPopUp popUp = new UtilPopUp();
						popUp.setTituloVentana("Confirmaci\u00F3n de despacho");
						popUp.setPreguntaVentana("Existen pedidos cuyos pesos no han sido ingresados, est\u00E1 seguro de que desea continuar? ");
						popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
						popUp.setValorOK(VALOR_SI_DESPACHAR);
						popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
						popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
						popUp.setEtiquetaBotonOK("Aceptar");
						popUp.setEtiquetaBotonCANCEL("Cancelar");
						//se guarda
						request.setAttribute(SessionManagerSISPE.POPUP, popUp);
					}else{
						LogSISPE.getLog().info("entra a despachar");
						try{
								//se realiza el despacho del pedido
								this.realizarDespacho(request, exitos, errors, colVistaPedidoDTO, formulario);
						}catch(SISPEException ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						}
					}
				}
			}
		}
		return "desplegar";
	}

	/** Se realiza la consulta de pedidos
	 * 
	 * @param request
	 * @param formulario
	 * @param infos
	 * @param errors
	 * @throws Exception
	 */
	private void consultarPedidos(HttpServletRequest request, ControlProduccionPedidoEspecialForm formulario, 
			ActionMessages infos, ActionMessages errors) throws Exception{

		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		//si no existe un c\u00F3digo para el tipo de pedido no se puede realizar la b\u00FAsqueda
		if(session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER) != null){
			//colecci\u00F3n que almacena los pedidos buscados
			Collection<VistaPedidoDTO> colVistaPedidoDTO = new ArrayList<VistaPedidoDTO>();

			PaginaTab paginaTab = (PaginaTab)session.getAttribute(SessionManagerSISPE.PAGINA_TAB);
			//DTO que contiene los campos utilizados para la busqueda
			
			// BORRAR ------------------------
			LogSISPE.getLog().info("Llama a m\u00E9todo x1");
			// -------------------------------
			
			VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
			//se asigna el c\u00F3digo del tipo de pedido para la b\u00FAsqueda
			consultaPedidoDTO.setCodigoTipoPedido((String)session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER));

			LogSISPE.getLog().info("**CODIGO TIPO PEDIDO: {}", consultaPedidoDTO.getCodigoTipoPedido());
			//se busca por estado actual [SI]
			consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			//se asignan los campos de ordenamiento
			String [][] camposOrden = new String [][]{{"CODIGOAREATRABAJO",ORDEN_ASCENDENTE},{"PRIMERAFECHADESPACHO",ORDEN_ASCENDENTE}};
			consultaPedidoDTO.setNpCamposOrden(camposOrden);
			//se asigna este campo para indicar que la fecha de despacho no debe ser buscada en las entregas
			consultaPedidoDTO.setNpBuscarPorPrimerasFechas("ok");
			//asigno valor al campo npObtenerLocalEntrega para hacer visible el local de entrega
			consultaPedidoDTO.setNpObtenerLocalEntrega(estadoActivo);
			//si est\u00E1 en el tab de pendientes aumento un criterio de b\u00FAsqueda
			if(paginaTab.esTabSeleccionado(2)){
				LogSISPE.getLog().info("por fecha de cierre nula");
				consultaPedidoDTO.setNpFechaCierrePedidoEspecialNula(estadoActivo);
			}

			try{
				//se obtiene la colecci\u00F3n de Pedidos buscados y se la almacena en sesi\u00F3n
				if(paginaTab.esTabSeleccionado(1)){
					consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.confirmado")
							.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio"))
							.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado")));
					
					colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosEspecialesPorDespachar(consultaPedidoDTO);
				}else if(paginaTab.esTabSeleccionado(2)){
					consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.confirmado")
							.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio")));
					colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
				}
				
				if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
					if(paginaTab.esTabSeleccionado(1)){
						session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
						session.removeAttribute(COL_PAGINA_PEDIDOS);
						session.removeAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR);
						session.removeAttribute(INDICE_TAB_1);
					}else if(paginaTab.esTabSeleccionado(2)){
						session.removeAttribute(COL_PEDIDOS_PENDIENTES);
						session.removeAttribute(CONSULTA_PEDIDOS_PENDIENTES);
					}
					
					if(infos!=null){
						//se muestra un mensaje indicando que la lista est\u00E1 vacia
						infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos Especiales"));
						LogSISPE.getLog().info("lista vacia despacho");
					}
				}else{
					//solo si hay pedidos
					if(paginaTab.esTabSeleccionado(1)){
						LogSISPE.getLog().info("guarda en despacho");
						LogSISPE.getLog().info("ENTRO A LA PAGINACION");
						int size= colVistaPedidoDTO.size();
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango30"));
						int start= 0;
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));
	
						Collection datosSub = Util.obtenerSubCollection(colVistaPedidoDTO,start, start + range > size ? size : start+range);
						//env\u00EDa la colecci\u00F3n solo para los pedidos despachados
						session.setAttribute(COL_PAGINA_PEDIDOS,datosSub);
						//se guarda colecci\u00F3n de los pedidos
						session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
						//guardo los campos de b\u00FAsqueda
						session.setAttribute(CONSULTA_PEDIDOS_POR_DESPACHAR,consultaPedidoDTO);
						session.setAttribute(INDICE_TAB_1, "0");
						
						//se blanquean los checks
						formulario.setChecksSeleccionar(null);
						formulario.setCheckSeleccionarTodo(null);

					}else if(paginaTab.esTabSeleccionado(2)){
						//si el dia se ha cerrado
						if(session.getAttribute(MOSTRAR_INFO)==null){
							LogSISPE.getLog().info("guarda en pendientes");		
							//guardo los campos de b\u00FAsqueda
							session.setAttribute(CONSULTA_PEDIDOS_PENDIENTES, consultaPedidoDTO);
							//guardo la colecci\u00F3n
							session.setAttribute(COL_PEDIDOS_PENDIENTES, colVistaPedidoDTO);
							session.setAttribute(INDICE_TAB_2, "0");
						}
					}
				}
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				if(errors != null){
					//si ocurre un error al llamar al m\u00E9todo del servicio
					errors.add("Pedidos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos Especiales"));
				}
			}
		}
	}

	/**
	 * 
	 * @param vistaPedidoDTO
	 * @param request
	 * @throws Exception
	 */
	private void obtenerDetallesPedido(VistaPedidoDTO vistaPedidoDTO, HttpServletRequest request) throws Exception
	{
		Collection detallePedido = vistaPedidoDTO.getVistaDetallesPedidos();
		if(detallePedido==null){
			LogSISPE.getLog().info("trae el detalle del pedido");
			//creaci\u00F3n del objeto VistaDetallePedidoDTO para la consulta
			VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
			consultaVistaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
			LogSISPE.getLog().info("*CODIGO DEL LOCAL: {}", vistaPedidoDTO.getId().getCodigoAreaTrabajo());

			//asignaci\u00F3n de los par\u00E1metros de consulta
			consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			//consultaVistaDetallePedidoDTO.setCodigoTipoArticulo(vistaPedidoDTO.getCodigoTipoPedido());
			consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			//solo si el estado es DESPACHADO
			if(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado"))){
				consultaVistaDetallePedidoDTO.setNpFechaFinalEstadoDetalleNula(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			}
			
			//busqueda de los detalles
			detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
			vistaPedidoDTO.setVistaDetallesPedidos(detallePedido);
		}
	}	
	/**
	 * Realiza el despacho de los pedidos TAB PEDIDOS
	 * @param request
	 * @param exitos
	 * @param errors
	 * @param colVistaPedidoDTO
	 * @param formulario
	 */
	private void realizarDespacho(HttpServletRequest request, ActionMessages exitos, ActionMessages errors, 
			Collection<VistaPedidoDTO> colVistaPedidoDTO, ControlProduccionPedidoEspecialForm formulario)throws SISPEException{

		HttpSession session = request.getSession();
		try{
			//llamo a la funci\u00F3n que despacha.
			SessionManagerSISPE.getServicioClienteServicio().transRegistrarDespachoPedidoEspecial(colVistaPedidoDTO);
			exitos.add("despachoRealizado", new ActionMessage("message.exito.registro","El despacho de los pedidos"));

			formulario.setChecksSeleccionar(null);
			formulario.setCheckSeleccionarTodo(null);
			//elimino la colecci\u00F3n
			session.removeAttribute(PEDIDOS_DESPACHADOS);
			//se realiza nuevamente la b\u00FAsqueda
			this.consultarPedidos(request, formulario, null, null);

			//se eliminan las variables relacionadas con los art\u00EDculos
			session.removeAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
			session.removeAttribute(COL_PAGINA_ARTICULOS);
			session.removeAttribute(CONSULTA_ARTICULOS);
			session.removeAttribute(INDICE_TAB_0);
			
		}catch(SISPEException ex){
			errors.add("despachoNoRealizado", new ActionMessage("errors.llamadaServicio.registrarDatos", "El Despacho"));
			errors.add("SISPEException", new ActionMessage("errors.SISPEException", ex.getMessage()));
			throw ex;
		}catch(Exception ex){
			throw new SISPEException(ex);
		}
	}

	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param infos
	 * @param errors
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void consultarArticulosPorDespachar(HttpServletRequest request, ControlProduccionPedidoEspecialForm formulario, 
			ActionMessages infos, ActionMessages errors) throws Exception{
		
		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//si no existe un c\u00F3digo para el tipo de pedido no se puede realizar la b\u00FAsqueda
		if(session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER) != null){
			
			//se llama a la funci\u00F3n que construye la consulta
			VistaArticuloDTO consultaVistaArticuloDTO = WebSISPEUtil.construirConsultaVistaArticulos(request, formulario);
			
			//se asigna el c\u00F3digo del tipo de pedido para la b\u00FAsqueda
			consultaVistaArticuloDTO.setCodigoTipoPedido((String)session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER));

			LogSISPE.getLog().info("**CODIGO TIPO PEDIDO: {}", consultaVistaArticuloDTO.getCodigoTipoPedido());
			consultaVistaArticuloDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.confirmado")
					.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio"))
					.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado")));		

			//asigno valor al campo npObtenerLocalEntrega para hacer visible el local de entrega
			consultaVistaArticuloDTO.setNpNoObtenerEntregas(estadoActivo);
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.LocalYArticuloPedEsp"));
			if(session.getAttribute(HABILITAR_DESPACHO)!=null){
				//si ya se cerr\u00F3 el d\u00EDa
				consultaVistaArticuloDTO.setNpEstadoCierreDiaPedidosEspeciales(estadoActivo);
			}else{
				consultaVistaArticuloDTO.setNpEstadoCierreDiaPedidosEspeciales(estadoInactivo);
			}
			
			try{
				//se obtiene la colecci\u00F3n de Pedidos buscados y se la almacena en sesi\u00F3n
				Collection<VistaArticuloDTO> colVistaArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(consultaVistaArticuloDTO);
				
				if(colVistaArticuloDTO==null || colVistaArticuloDTO.isEmpty()){
					if(infos!=null){
						//se muestra un mensaje indicando que la lista est\u00E1 vacia
						infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos para despachar"));
						LogSISPE.getLog().info("lista vacia despacho");
					}
					session.removeAttribute(COL_PAGINA_ARTICULOS);
					session.removeAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
					session.removeAttribute(CONSULTA_ARTICULOS);
					session.removeAttribute(INDICE_TAB_0);
				}else{
					LogSISPE.getLog().info("ENTRO A LA PAGINACION");
					int size= colVistaArticuloDTO.size();
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango30"));
					int start= 0;
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection subColeccion = Util.obtenerSubCollection(colVistaArticuloDTO,start, start + range > size ? size : start+range);
					//se guarda la sub colecci\u00F3n de art\u00EDculos
					session.setAttribute(COL_PAGINA_ARTICULOS, subColeccion);
					//se guarda colecci\u00F3n de todos los art\u00EDculos
					session.setAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO, colVistaArticuloDTO);
					//guardo los campos de b\u00FAsqueda
					session.setAttribute(CONSULTA_ARTICULOS, consultaVistaArticuloDTO);
					session.setAttribute(INDICE_TAB_0, "0");
				}
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				if(errors != null){
					//si ocurre un error al llamar al m\u00E9todo del servicio
					errors.add("Pedidos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos Especiales"));
				}
			}
		}
		
	}
	
	/**
	 * Verifica los valores ingresados para los art\u00EDculos y en base a eso llena los valores de cada objeto
	 * 
	 * @param request
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	private Collection<VistaArticuloDTO> verificarValoresArticulos(HttpServletRequest request, HttpSession session){
		//se obtiene la colecci\u00F3n de art\u00EDculos de sesi\u00F3n
		List<VistaArticuloDTO> lisVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(COL_PAGINA_ARTICULOS);
		Collection<VistaArticuloDTO> colFinalVistaArticuloDTO = null;
		
		if(lisVistaArticuloDTO != null){
			colFinalVistaArticuloDTO = new ArrayList<VistaArticuloDTO>();
			String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
			//se itera cada detalle para asignar el peso ingresado en el campo del objeto
			for(int i=0; i<lisVistaArticuloDTO.size();i++){
				VistaArticuloDTO vistaArticuloDTO = lisVistaArticuloDTO.get(i);
				vistaArticuloDTO.setUserId(userId);
				if(request.getParameter("peso".concat(String.valueOf(i))) != null){
					try{
						//se obtiene el valor del peso ingresado
						Double peso = Double.parseDouble(request.getParameter("peso".concat(String.valueOf(i))));
						if(peso >= 0){
							vistaArticuloDTO.setNpPesoIngresado(peso);
						}else{
							vistaArticuloDTO.setNpPesoIngresado(null);
						}
					}catch(Exception ex){
						vistaArticuloDTO.setNpPesoIngresado(null);
					}
					
					if(vistaArticuloDTO.getNpPesoIngresado()!=null && !vistaArticuloDTO.getNpPesoIngresado().equals("")){
						colFinalVistaArticuloDTO.add(vistaArticuloDTO);
					}
				}
			}
		}
		return colFinalVistaArticuloDTO;
	}
	
	/**
	 * Verifica si el d\u00EDa fue cerrado para cada uno de los tipos de pedidos, si por lo menos uno no se cerr\u00F3 retorna <code>false</code>
	 * @param  request
	 * @param  codigoTipoPedido
	 * @return
	 * @throws Exception
	 */
	private boolean verificarCierreDiaPedidosEspeciales(String codigoTipoPedido)throws Exception{
		
		//se obtienen los posibles tipos de pedidos
		String [] codigosTipoPedido = codigoTipoPedido.split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
		for(int i=0;i<codigosTipoPedido.length;i++){
			//se verifica si el d\u00EDa ya fue cerrado para cada tipo de pedido
			if(SessionManagerSISPE.getServicioClienteServicio().transObtenerActualCierreDiaPedidoEspecial(codigosTipoPedido[i]) == null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * generar el reporte dato el template  y los datos
	 * @param request
	 * @param formulario
	 * @return
	 * @throws Exception 
	 * @throws SISPEException 
	 */
	public byte[] crearExcelReporteDespachos(HttpServletRequest request, ListadoPedidosForm formulario) throws SISPEException, Exception {
		
		VistaReporteGeneralDTO vistaReporteGeneralDTO = new VistaReporteGeneralDTO();
		vistaReporteGeneralDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		vistaReporteGeneralDTO.setPrimeraFechaDespachoDesde(new Timestamp(sdf.parse(formulario.getFechaInicialRepDespachos()).getTime()));
		vistaReporteGeneralDTO.setPrimeraFechaDespachoHasta(new Timestamp(sdf.parse(formulario.getFechaFinalRepDespachos()).getTime()));
//		vistaReporteGeneralDTO.setNpEstadoEnProceso(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado"));
		vistaReporteGeneralDTO.setEstadoActual(true);
		vistaReporteGeneralDTO.setCodigoTipoPedido((String)request.getSession().getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER));
		vistaReporteGeneralDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.confirmado")
				.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio"))
				.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado")));
		//no obtener entregas
		vistaReporteGeneralDTO.setNpNoObtenerEntregas("ok");
		
		vistaReporteGeneralDTO.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.pedidosPorLocal"));
		ArrayList<VistaReporteGeneralDTO> vistaReporteGeneralDTOcol = (ArrayList<VistaReporteGeneralDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralDTO);

		String contenidoArchivo = null;
		String contenidoXML = null;
		
		try {
			String contenidoXSL = TransformerUtil.obtenerPlantillaHTML(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.plantilla.reporte.despachos"));
			
			if (CollectionUtils.isNotEmpty(vistaReporteGeneralDTOcol)) {
				contenidoXML = generarXmlReporteDespachos(vistaReporteGeneralDTOcol,formulario.getFechaInicialRepDespachos(),formulario.getFechaFinalRepDespachos()).toString();
				
				contenidoXML = contenidoXML.replace("&", "&amp;");

				Document result;
				
				LogSISPE.getLog().info(contenidoXML.toString());
				
				Document docXML = TransformerUtil.stringToXML(contenidoXML);
			    Document docXSL = TransformerUtil.stringToXML(contenidoXSL);

			    result = TransformerUtil.transformar(docXML, docXSL);
			    
			    contenidoArchivo = TransformerUtil.xmlToString(result);
			    contenidoArchivo = contenidoArchivo.replaceAll("UTF-8", "iso-8859-1");
//			    transform(contenidoXML.getBytes());
			}else{
				LogSISPE.getLog().info("No existen datos para realizar el reporte de despachos");
			}	
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error crear excel: {}", e);
		} 
		
		if (contenidoArchivo != null) {
			return contenidoArchivo.getBytes();
		}
		
		return new byte[0];
	}
	
	/**
	 * Generar xml datos
	 * @param request
	 * @param formulario
	 * @return
	 */
	public StringBuffer generarXmlReporteDespachos (ArrayList<VistaReporteGeneralDTO> vistaReporteGeneralDTOcol,String fechaDesde,String fechaHasta) {
		
		StringBuffer xml = new StringBuffer();
		StringBuffer xmlCant = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		xml.append("<datos>");
		xml.append("<fechaDesde>");xml.append(fechaDesde);xml.append("</fechaDesde>");
		xml.append("<fechaHasta>");xml.append(fechaHasta);xml.append("</fechaHasta>");
		int i = 0;
		int j = 0;
		if(CollectionUtils.isNotEmpty(vistaReporteGeneralDTOcol)){
			for(VistaReporteGeneralDTO vistaRepGenDTO:vistaReporteGeneralDTOcol) {
				
				String nombreLocal=vistaRepGenDTO.getId().getCodigoAreaTrabajo()+" - "+vistaRepGenDTO.getNombreLocal();
//				if(vistaRepGenDTO.getPesoArticuloEstado()!=null && vistaRepGenDTO.getPesoArticuloEstado()>0){

					Collection<VistaReporteGeneralDTO> colvistaReporteGeneralDTOPeso=new ArrayList<VistaReporteGeneralDTO>();
					Collection<VistaReporteGeneralDTO> colvistaReporteGeneralDTOCant=new ArrayList<VistaReporteGeneralDTO>();
					for(VistaReporteGeneralDTO vistaRepGenDTO1:vistaRepGenDTO.getDetallesReporte()) {
						
						for(final VistaReporteGeneralDTO vistaRepGenDTO2:vistaRepGenDTO1.getDetallesReporte()) {
							if(vistaRepGenDTO2.getPesoArticuloEstado()!=null && vistaRepGenDTO2.getPesoArticuloEstado()>0){
								if(!CollectionUtils.exists(colvistaReporteGeneralDTOPeso, new Predicate() {
									
									@Override
									public boolean evaluate(Object object) {
										VistaReporteGeneralDTO vistaRepGenDTO=(VistaReporteGeneralDTO) object;
										if(vistaRepGenDTO.getCodigoBarras().equals(vistaRepGenDTO2.getCodigoBarras())){
											vistaRepGenDTO.setPesoArticuloEstado((vistaRepGenDTO.getPesoArticuloEstado()!=null?vistaRepGenDTO.getPesoArticuloEstado():0)+(vistaRepGenDTO2.getPesoArticuloEstado()!=null?vistaRepGenDTO2.getPesoArticuloEstado():0));
											vistaRepGenDTO.setPesoRegistradoBodega((vistaRepGenDTO.getPesoRegistradoBodega()!=null?vistaRepGenDTO.getPesoRegistradoBodega():0)+(vistaRepGenDTO2.getPesoRegistradoBodega()!=null?vistaRepGenDTO2.getPesoRegistradoBodega():0));
											return true;
										}else{
											return false;
										}
									}
								})){
									colvistaReporteGeneralDTOPeso.add(vistaRepGenDTO2);	
								}
							}
							if(vistaRepGenDTO2.getCantidadEstado()!=null && vistaRepGenDTO2.getCantidadEstado()>0){
								if(!CollectionUtils.exists(colvistaReporteGeneralDTOCant, new Predicate() {
									
									@Override
									public boolean evaluate(Object object) {
										VistaReporteGeneralDTO vistaRepGenDTO=(VistaReporteGeneralDTO) object;
										if(vistaRepGenDTO.getCodigoBarras().equals(vistaRepGenDTO2.getCodigoBarras())){
											vistaRepGenDTO.setCantidadEstado((vistaRepGenDTO.getCantidadEstado()!=null?vistaRepGenDTO.getCantidadEstado():0)+(vistaRepGenDTO2.getCantidadEstado()!=null?vistaRepGenDTO2.getCantidadEstado():0));
											vistaRepGenDTO.setPesoRegistradoBodega((vistaRepGenDTO.getPesoRegistradoBodega()!=null?vistaRepGenDTO.getPesoRegistradoBodega():0)+(vistaRepGenDTO2.getPesoRegistradoBodega()!=null?vistaRepGenDTO2.getPesoRegistradoBodega():0));
											return true;
										}else{
											return false;
										}
									}
								})){
									colvistaReporteGeneralDTOCant.add(vistaRepGenDTO2);	
								}
							}

						}
					}
					if(CollectionUtils.isNotEmpty(colvistaReporteGeneralDTOPeso)) {
						i++;							
						if(i==1){
							xml.append("<artPeso>");
						}
						xml.append("<local>");
						xml.append("<nombreLocal>");xml.append(nombreLocal);xml.append("</nombreLocal>");
						for(VistaReporteGeneralDTO vistaRepGenDTOArt:colvistaReporteGeneralDTOPeso){
							xml.append("<articulo>");
							xml.append("<codBarras>");xml.append(vistaRepGenDTOArt.getCodigoBarras());xml.append("</codBarras>");
							xml.append("<descripcion>");xml.append(vistaRepGenDTOArt.getDescripcionArticulo());xml.append("</descripcion>");
							xml.append("<pesoSol>");xml.append(vistaRepGenDTOArt.getPesoArticuloEstado());xml.append("</pesoSol>");
							xml.append("<pesoDes>");xml.append(vistaRepGenDTOArt.getPesoRegistradoBodega()!=null?vistaRepGenDTOArt.getPesoRegistradoBodega():"0");xml.append("</pesoDes>");
							xml.append("</articulo>");
						}
						xml.append("</local>");
					}
//				}
//				if(vistaRepGenDTO.getCantidadEstado()!=null && vistaRepGenDTO.getCantidadEstado()>0){
					if(CollectionUtils.isNotEmpty(colvistaReporteGeneralDTOCant)) {
						j++;
						if(j==1){
							xmlCant.append("<artCantidad>");
						}
						xmlCant.append("<local>");
						xmlCant.append("<nombreLocal>");xmlCant.append(nombreLocal);xmlCant.append("</nombreLocal>");
//					for(VistaReporteGeneralDTO vistaRepGenDTO1:vistaRepGenDTO.getDetallesReporte()) {
//						for(VistaReporteGeneralDTO vistaRepGenDTO2:vistaRepGenDTO1.getDetallesReporte()) {
//							xmlCant.append("<articulo>");
//							xmlCant.append("<codBarras>");xmlCant.append(vistaRepGenDTO2.getCodigoBarras());xmlCant.append("</codBarras>");
//							xmlCant.append("<descripcion>");xmlCant.append(vistaRepGenDTO2.getDescripcionArticulo());xmlCant.append("</descripcion>");
//							xmlCant.append("<cantSol>");xmlCant.append(vistaRepGenDTO2.getCantidadEstado());xmlCant.append("</cantSol>");
//							xmlCant.append("<cantDes>");xmlCant.append(vistaRepGenDTO2.getPesoRegistradoBodega()!=null?vistaRepGenDTO2.getPesoRegistradoBodega():"0");xmlCant.append("</cantDes>");
//							xmlCant.append("</articulo>");
//						}
//					}
//					Collection<VistaReporteGeneralDTO> colvistaReporteGeneralDTO=new ArrayList<VistaReporteGeneralDTO>();
//					for(VistaReporteGeneralDTO vistaRepGenDTO1:vistaRepGenDTO.getDetallesReporte()) {
//						
//						for(final VistaReporteGeneralDTO vistaRepGenDTO2:vistaRepGenDTO1.getDetallesReporte()) {
//							if(!CollectionUtils.exists(colvistaReporteGeneralDTO, new Predicate() {
//								
//								@Override
//								public boolean evaluate(Object object) {
//									VistaReporteGeneralDTO vistaRepGenDTO=(VistaReporteGeneralDTO) object;
//									if(vistaRepGenDTO.getCodigoBarras().equals(vistaRepGenDTO2.getCodigoBarras())){
//										vistaRepGenDTO.setCantidadEstado(vistaRepGenDTO.getCantidadEstado()+vistaRepGenDTO2.getCantidadEstado());
//										vistaRepGenDTO.setPesoRegistradoBodega(vistaRepGenDTO.getPesoRegistradoBodega()+vistaRepGenDTO2.getPesoRegistradoBodega());
//										return true;
//									}else{
//										return false;
//									}
//								}
//							})){
//								colvistaReporteGeneralDTO.add(vistaRepGenDTO2);	
//							}
//							
//
//						}
//					}
						for(VistaReporteGeneralDTO vistaRepGenDTOArt:colvistaReporteGeneralDTOCant){
							xmlCant.append("<articulo>");
							xmlCant.append("<codBarras>");xmlCant.append(vistaRepGenDTOArt.getCodigoBarras());xmlCant.append("</codBarras>");
							xmlCant.append("<descripcion>");xmlCant.append(vistaRepGenDTOArt.getDescripcionArticulo());xmlCant.append("</descripcion>");
							xmlCant.append("<cantSol>");xmlCant.append(vistaRepGenDTOArt.getCantidadEstado());xmlCant.append("</cantSol>");
							xmlCant.append("<cantDes>");xmlCant.append(vistaRepGenDTOArt.getPesoRegistradoBodega()!=null?vistaRepGenDTOArt.getPesoRegistradoBodega():"0");xmlCant.append("</cantDes>");
							xmlCant.append("</articulo>");
						}
						xmlCant.append("</local>");
					}
//				}
				
			}
			if(i>0){
				xml.append("</artPeso>");
			}
			if(j>0){
				xmlCant.append("</artCantidad>");
			}
		}
		xml.append(xmlCant);
		xml.append("</datos>");
		
		return xml;
	}
}
