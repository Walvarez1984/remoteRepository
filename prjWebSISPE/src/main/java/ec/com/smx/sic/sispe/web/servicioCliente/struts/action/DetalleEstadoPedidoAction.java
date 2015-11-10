/*
 * DetalleEstadoPedidoAction.java
 * Creado el 03/05/2006
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_PEDIDO_CONSOLIDADOS_AUX;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloBitacoraCodigoBarrasID;
import ec.com.smx.sic.cliente.mdl.dto.id.ArticuloID;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.EstructuraResponsable;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoID;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action.KardexAction;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.reportes.struts.action.ReporteEntregasAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * La clase <code>DetalleEstadoPedidoAction</code> consulta los datos del detalle del pedido seleccionado 
 * desde p\u00E1ginas que utilizan <code>VistaPedidoDTO</code> o p\u00E1ginas que usan <code>VistaReporteGeneralDTO</code>.
 * 
 * @author 	fmunoz
 * @author 	mbraganza
 * @version	3.0
 * @since	JSDK 1.5.0
 */
@SuppressWarnings("unchecked")
public class DetalleEstadoPedidoAction extends BaseAction
{
//	private static final String tipoArticuloCanasto = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//	private static final String tipoArticuloDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
	private static final String TIPO_PEDIDO_EMPRESARIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial");
	
	private static final String SESSION_VALOR_INDICE_ACTIVO_VRG = "ec.com.smx.sic.sispe.primerIndiceVRG";
	public static final String RESPALDO_TABS = "ec.com.smx.sic.sispe.respaldoTABS";
	private static final String RESPALDO_INDICE = "ec.com.smx.sic.sispe.indice";
	public static final String AUXILIAR_ATRAS_DETESTPED = "ec.com.smx.sic.sispe.atras.auxiliar.detalle.estado.pedido";
	public static final String RESPALDO_TABRESPALDO = "ec.com.smx.sic.controlesusuario.tabRespaldo";

	public static final String RESUMEN_ENTREGAS = "ec.com.smx.sic.sispe.resumen.entregas";
	public static final String CONVENIOS ="ec.com.smx.sic.sispe.convenioEntregaCol";
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
	 * (si la respuesta se ha completado <code>null</code>)
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&aacute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
//		ActionMessages exitos = new ActionMessages();
		HttpSession session = request.getSession();
		List<VistaPedidoDTO> pedidos = null;
		VistaPedidoDTO consultaVistaPedidoDTO = null;
		String salida = "desplegar";
		boolean generarPDF = false;
		boolean imprimirDocumento = false;
		boolean generarXSL = false;
		boolean imprimirConvenioL= false;
		boolean imprimirConvenioM = false;
		String ayuda= request.getParameter(Globals.AYUDA);
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		ListadoPedidosForm formulario = (ListadoPedidosForm)form;
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		
		//IOnofre. Variable para carga de archivo croquis
		String accionEstado= "detalleEstadoPedido.do";
		
		if(ayuda!=null && !ayuda.equals("")){
			if(ayuda.equals("siGenerarPDF") && !(beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)))
				generarPDF = true;
			else if(ayuda.equals("siImprimirPedido"))
				imprimirDocumento = true;
			else if (ayuda.equals("xls"))
				generarXSL = true;
			else if (ayuda.equals("imprimirConvenioL"))
				imprimirConvenioL= true;
			else if (ayuda.equals("imprimirConvenioM"))
				imprimirConvenioM= true;
		}
		
		//respaldo del tab inicial
		if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
			if(session.getAttribute(RESPALDO_TABRESPALDO)== null){
				session.setAttribute(RESPALDO_TABRESPALDO, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
			}			
		}
		
		LogSISPE.getLog().info("ayuda: {}",ayuda);
		request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedidoResumenConsolidado");
		try{
			//Presentar el detalle del pedido desde el reporte de ventas
			if (request.getParameter("codigoPedido") != null 
					&& request.getParameter("codigoLocal") != null
					&& request.getParameter("codigoEstado") != null
					&& request.getParameter("secuencialEstadoPedido") != null
					&& request.getParameter("llaveContratoPOS") != null)
			{
				//RESPALDO EL TABS
				if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
					session.setAttribute(RESPALDO_TABS, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
				}
				consultaVistaPedidoDTO = new VistaPedidoDTO();
				consultaVistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaVistaPedidoDTO.getId().setCodigoEstado(request.getParameter("codigoEstado"));
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(Integer.valueOf(request.getParameter("codigoLocal")));
				consultaVistaPedidoDTO.getId().setCodigoPedido(request.getParameter("codigoPedido"));
				consultaVistaPedidoDTO.getId().setSecuencialEstadoPedido(request.getParameter("secuencialEstadoPedido"));
				consultaVistaPedidoDTO.setLlaveContratoPOS(request.getParameter("llaveContratoPOS"));
				
				pedidos = (List<VistaPedidoDTO>) SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);

				if (pedidos != null && !pedidos.isEmpty()){
					LogSISPE.getLog().info("Tama\u00F1o de la Vista {}",pedidos.size());
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(0);
					EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
					EstadoPedidoUtil.obtenerDetallesPedidoAutorizaciones(vistaPedidoDTO, request);
					
					//Cargar en sesion los datos de la persona o de la empresa del indice seleccionado
					//ContactoUtil.cargarDatosPersonaEmpresa(request, consultaVistaPedidoDTO);
					
					WebSISPEUtil.verificarDiferidosPedido(request);
					obtenerEntregas(session,vistaPedidoDTO.getVistaDetallesPedidosReporte());
					//se crean los tab del detalle del pedido y detalle de las autorizaciones del pedido
					PaginaTab tabDetallePedidoAutorizaciones = AutorizacionesUtil.construirTabsDetalleAutorizacion(request, vistaPedidoDTO);
					beanSession.setPaginaTab(tabDetallePedidoAutorizaciones);
					
					obtenerRolesEnvioMail(request);
				}else{
					errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
				}
			}
			else if(request.getParameter("popUpObsoleto")!=null){
				instanciarVentanaObsoleto(request);
			}else if(request.getParameter("cerrarPopUpObsoleto")!=null){
				session.setAttribute(SessionManagerSISPE.POPUP, null);
			}	
			//se realiza el cambio de tabs
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				int posicionTab = beanSession.getPaginaTab().getTabSeleccionado();

				ContactoUtil.cambiarTabContactoPedidos(beanSession, posicionTab);
				LogSISPE.getLog().info("tab seleccionado {}",beanSession.getPaginaTab().getTituloTabSeleccionado());
			}
			
			//presenta el detalle del pedido desde las paginas del kardex
			else if(request.getParameter("verPedidoKardex") != null)
			{
				//RESPALDO EL TABS
				if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
					session.setAttribute(RESPALDO_TABS, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
				}
				CalendarioDiaLocalDTO calendarioDiaLocalDTO = null;
				Collection entregaDTOCol = null;
				String codigoPedido = request.getParameter("verPedidoKardex");
				EstadoPedidoDTO consultaEstadoPedidoDTO = new EstadoPedidoDTO();
				consultaEstadoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaEstadoPedidoDTO.getId().setCodigoPedido(codigoPedido);
				consultaEstadoPedidoDTO.setPedidoDTO(new PedidoDTO());
				//se realiza la consulta del estado actual
				EstadoPedidoDTO estadoPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoActualPedido(consultaEstadoPedidoDTO);
				
				//valor de area de trabajo del usuario logueado
				session.setAttribute("ec.com.smx.sic.sispe.codigo.area.trabajo", SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
				session.setAttribute("ec.com.smx.sic.sispe.nombre.area.trabajo", SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
				
				if (estadoPedidoDTO != null){
//					EntregaDTO entregaDTO = new EntregaDTO();
//					entregaDTO.getId().setCodigoCompania(estadoPedidoDTO.getId().getCodigoCompania());
//					entregaDTO.getId().setCodigoAreaTrabajo(estadoPedidoDTO.getId().getCodigoAreaTrabajo());
//					entregaDTO.getId().setCodigoPedido(estadoPedidoDTO.getId().getCodigoPedido());
//					entregaDTO.getId().setCodigoEstado(estadoPedidoDTO.getId().getCodigoEstado());
//					entregaDTO.getId().setSecuencialEstadoPedido(estadoPedidoDTO.getId().getSecuencialEstadoPedido());
//					entregaDTO.setNpFiltarPorEntregasLocalOdomicilio(new Boolean(true));
//					entregaDTO.setCodigoLocalEntrega(new Integer(request.getParameter("local")));
//					entregaDTO.setCodigoLocalSector(entregaDTO.getCodigoLocalEntrega());
//					entregaDTO.setNpCantidadDespacho(new Long(1));
//					entregaDTO.setNpOrderBy("fechaEntregaCliente");
//					calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
//					entregaDTO.setFechaDespachoBodega(new Timestamp(calendarioDiaLocalDTO.getId().getFechaCalendarioDia().getTime()));
//					entregaDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
//					
//					entregaDTO.getEstadoDetallePedidoDTO().setDetallePedidoDTO(new DetallePedidoDTO());
//					
//					//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n de entregas
//					entregaDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerEntregaQBE(entregaDTO);
//					LogSISPE.getLog().info("entregasDTOCol: {}" , entregaDTOCol.size());
//					session.setAttribute("ec.com.smx.sic.sispe.entregaDTOCol", entregaDTOCol);
//					session.setAttribute("ec.com.smx.sic.sispe.estadoPedidoDTO", estadoPedidoDTO);
//					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto",tipoArticuloCanasto);
//					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", tipoArticuloDespensa);
//					LogSISPE.getLog().info("consultaEnElKardex...1");
					
					EntregaDetallePedidoDTO entregaDetalle = new EntregaDetallePedidoDTO();
					entregaDetalle.getId().setCodigoCompania(estadoPedidoDTO.getId().getCodigoCompania());
					entregaDetalle.setCodigoAreaTrabajo(estadoPedidoDTO.getId().getCodigoAreaTrabajo());
					entregaDetalle.setCodigoPedido(estadoPedidoDTO.getId().getCodigoPedido());
					entregaDetalle.setCodigoEstado(estadoPedidoDTO.getId().getCodigoEstado());
					entregaDetalle.setSecuencialEstadoPedido(estadoPedidoDTO.getId().getSecuencialEstadoPedido());
					entregaDetalle.setEntregaPedidoDTO(new EntregaPedidoDTO());
					//entregaDetalle.setNpFiltarPorEntregasLocalOdomicilio(new Boolean(true));
					entregaDetalle.getEntregaPedidoDTO().setCodigoAreaTrabajoEntrega(new Integer(request.getParameter("local")));
					entregaDetalle.getEntregaPedidoDTO().setCodigoLocalSector(entregaDetalle.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
					entregaDetalle.setNpCantidadDespacho(new Long(1));
					entregaDetalle.setNpOrderBy("entregaPedidoDTO.fechaEntregaCliente");
					calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDiaLocalDTO");
					entregaDetalle.getEntregaPedidoDTO().setFechaDespachoBodega(new Timestamp(calendarioDiaLocalDTO.getId().getFechaCalendarioDia().getTime()));
					entregaDetalle.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
					
					entregaDetalle.getEstadoDetallePedidoDTO().setDetallePedidoDTO(new DetallePedidoDTO());
									
					
					//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n de entregas
					entregaDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerDetalleEntregaQBE(entregaDetalle);
					LogSISPE.getLog().info("entregasDTOCol: {}" , entregaDTOCol.size());
					session.setAttribute("ec.com.smx.sic.sispe.entregaDTOCol", entregaDTOCol);
					session.setAttribute("ec.com.smx.sic.sispe.estadoPedidoDTO", estadoPedidoDTO);
//					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto",tipoArticuloCanasto);
//					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", tipoArticuloDespensa);
					//subClasificacion para articulos tipo canastos 
			  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
					String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
					//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto", valorTipoArticuloCanastas);
					
					//subClasificacion para articulos tipo despensas 
			  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
			  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
					//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
					session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", valorTipoArticuloDespensas);
					LogSISPE.getLog().info("consultaEnElKardex...1");
					
					
				} else {
					session.removeAttribute("ec.com.smx.sic.sispe.entregaDTOCol");
					session.removeAttribute("ec.com.smx.sic.sispe.estadoPedidoDTO");
					errors.add("errorReporte", new ActionMessage("errors.obtenerDatos", "pedido: " + codigoPedido));
				}
				//se sube a sesi\u00F3n el tipo de pedido para la impresi\u00F3n del reporte
				session.setAttribute("ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial",TIPO_PEDIDO_EMPRESARIAL);
				
				salida= "desplegarDetalle";
				
				
				/**------------ se crea una vista pedido para cargar los datos de la cabecera del pedido--------**/						
				consultaVistaPedidoDTO = new VistaPedidoDTO();
				consultaVistaPedidoDTO.getId().setCodigoCompania(estadoPedidoDTO.getId().getCodigoCompania());
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(estadoPedidoDTO.getId().getCodigoAreaTrabajo());
				consultaVistaPedidoDTO.getId().setCodigoPedido(codigoPedido);
				consultaVistaPedidoDTO.getId().setCodigoEstado(estadoPedidoDTO.getId().getCodigoEstado());
				consultaVistaPedidoDTO.getId().setSecuencialEstadoPedido(estadoPedidoDTO.getId().getSecuencialEstadoPedido());
				pedidos = (List<VistaPedidoDTO>) SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
				
				if (pedidos != null && !pedidos.isEmpty()){
					LogSISPE.getLog().info("Tama\u00F1o de la Vista {}",pedidos.size());
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(0);
					EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
					session.setAttribute("ec.com.smx.sic.sispe.vistaPedido",vistaPedidoDTO);
				}else{
					errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
				}				
				/**-----------------------**/
				
			}
			//descargar y guardar archivo del croquis
			if(request.getParameter("verArchivo") != null && request.getParameter("indice") != null){
				LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
				CotizacionReservacionUtil.verArchivo(request, response);
			}
			
			//Presentar el detalle del pedido desde p\u00E1ginas que usan VistaPedidoDTO 
			else if (request.getParameter("indice") != null || (session.getAttribute(RESPALDO_INDICE) != null && session.getAttribute(AUXILIAR_ATRAS_DETESTPED) != null)) {
				
				Integer indice = null;
				if (request.getParameter("indice") != null) {
					indice= Integer.valueOf(request.getParameter("indice"));
				} else if (session.getAttribute(RESPALDO_INDICE) != null) {
					indice= Integer.valueOf(session.getAttribute(RESPALDO_INDICE).toString());
				}
				
				//se elimina la bandera que pinta el boton de mensajes de autorizaciones
				session.removeAttribute("ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones");
				
				//Ionofre. Se elimina la variable que permite imprimir las etiquetas de entregas
				session.removeAttribute("ec.com.smx.sic.sispe.imprimirEtiquetas");
				
				if(request.getParameter("listaConsolidados") != null){
					LogSISPE.getLog().info("Seccion lista de consolidados");
					session.setAttribute(CotizarReservarAction. POSICION_DIV ,request.getParameter( "posicionScroll" )); 
					int subIndice= Integer.parseInt(request.getParameter("listaConsolidados"));
					if(session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX) != null){						
						VistaPedidoDTO vistaPedidoAux = (VistaPedidoDTO)CollectionUtils.get(session.getAttribute(COL_PEDIDO_CONSOLIDADOS_AUX),indice);
						VistaPedidoDTO vistaPedidoActual = (VistaPedidoDTO)CollectionUtils.get(vistaPedidoAux.getVistaPedidoDTOCol(),subIndice);
						EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoActual, request);
						EstadoPedidoUtil.obtenerDetallesPedidoAutorizaciones(vistaPedidoActual, request);
						WebSISPEUtil.verificarDiferidosPedido(request);
//						obtenerEntregas(session,consultaVistaPedidoDTO.getVistaDetallesPedidosReporte());
						//se crean los tab del detalle del pedido y detalle de las autorizaciones del pedido
						PaginaTab tabDetallePedidoAutorizaciones = AutorizacionesUtil.construirTabsDetalleAutorizacion(request, vistaPedidoActual);
						beanSession.setPaginaTab(tabDetallePedidoAutorizaciones);
					}
					
				}else{
					LogSISPE.getLog().info("Link del indice codigo pedido");
					//RESPALDO EL TABS
					if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
						//PaginaTab
						session.setAttribute(RESPALDO_TABS, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
					}
					//obtener de la sesion la colecci\u00F3n de los pedidos en general
					pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					
					if(pedidos == null)
						//se obtienen los pedidos con despacho pendiente 
						pedidos = (List<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
					
					if(pedidos == null)
						//se obtienen los pedidos con entrega pendiente 
						pedidos = (List<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar");
					
					if(pedidos == null)
						//se obtienen los pedidos despachados
						pedidos = (List<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");

					if(pedidos!=null && !pedidos.isEmpty()){
						consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
						//para que haga los calculos restantes, para mostrar la vista
						consultaVistaPedidoDTO.setVistaDetallesPedidosReporte(null);
						consultaVistaPedidoDTO.setValorTotalBrutoSinIva(null);
						consultaVistaPedidoDTO.setVistaDetallesPedidos(null);
						EstadoPedidoUtil.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
						EstadoPedidoUtil.mostrarDetallesPedidoPorEstado(consultaVistaPedidoDTO, consultaVistaPedidoDTO.getVistaDetallesPedidosReporte());
						EstadoPedidoUtil.obtenerDetallesPedidoAutorizaciones(consultaVistaPedidoDTO, request);
						
						//Cargar en sesion los datos de la persona o de la empresa del indice seleccionado
						//ContactoUtil.cargarDatosPersonaEmpresa(request, consultaVistaPedidoDTO);
						
						WebSISPEUtil.verificarDiferidosPedido(request);
						obtenerEntregas(session,consultaVistaPedidoDTO.getVistaDetallesPedidosReporte());
						CotizarReservarAction.consultarEntregasConSICMER(session,consultaVistaPedidoDTO);
						//se crean los tab del detalle del pedido y detalle de las autorizaciones del pedido
						PaginaTab tabDetallePedidoAutorizaciones = AutorizacionesUtil.construirTabsDetalleAutorizacion(request, consultaVistaPedidoDTO);
						beanSession.setPaginaTab(tabDetallePedidoAutorizaciones);
						
					}else{
						errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
					}
				}
				
				obtenerRolesEnvioMail(request);
				
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.setAttribute(RESPALDO_INDICE, indice);//cambios Oscar
				session.removeAttribute(AUXILIAR_ATRAS_DETESTPED);
			}
			//---- ver el detalle de una orden de compra ----------
			else if(request.getParameter("indiceInfoOrdenCompra") != null){
				//se obtiene el indice del detalle de la cotizaci\u00F3n seleccionado
				int indice = Integer.parseInt(request.getParameter("indiceInfoOrdenCompra"));
				//se obtiene de sesi\u00F3n la colecci\u00F3n con el estado del detalle del pedido
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				ArrayList colVistaDetallePedido = (ArrayList) vistaPedidoDTO.getVistaDetallesPedidosReporte();
				//se obtiene el objeto VistaDetallePedidoDTO
				VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)colVistaDetallePedido.get(indice);
				request.setAttribute("vistaDetallePedidoDTO", vistaDetallePedidoDTO);
			}
      /*-------- CONFIRMACION de la impresi\u00F3n del pedido como texto -------*/
      else if(request.getParameter("confirmarImpresionTexto")!=null){
    	LogSISPE.getLog().info("confirmar impresion TEXTO");
    	session.removeAttribute(SessionManagerSISPE.POPUP);
    	//llama al metodo que actualiza el campo npStockArticulo del reporte de texto y PDF que se cotiene de la VISTA_PEDIDO.
    	EstadoPedidoUtil.actualizarArticuloStockIntegracionSic(request, errors);
    	if(errors.size() == 0){
    		//lamada al m\u00E9todo que asigna las variables para la pregunta de confirmaci\u00F3n
	      	WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.imprimirPedido",
	      			"Para imprimir el documento haga click en Aceptar", "siImprimirPedido", null, request);
    	}
      }	 
      /*-------- CONFIRMACION de la generaci\u00F3n del documento como PDF -------*/
      else if(request.getParameter("confirmarPDF")!=null){
      	LogSISPE.getLog().info("confirmar impresion PDF");
      	session.removeAttribute(SessionManagerSISPE.POPUP);
      	//llama al metodo que actualiza el campo npStockArticulo del reporte de texto y PDF que se cotiene de la VISTA_PEDIDO.
      	EstadoPedidoUtil.actualizarArticuloStockIntegracionSic(request, errors);
      	if(errors.size() == 0){
      		//lamada al m\u00E9todo que asigna las variables para la pregunta de confirmaci\u00F3n
          	WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.generarPDF",
          			"Para generar el pdf haga click en Aceptar", "siGenerarPDF", null, request);
    	}
      	
      }
	  /*-------- ver Vista Articulo Diferido -------*/
      else if(request.getParameter("verDiferido")!=null){
      	LogSISPE.getLog().info("presion\u00F3 el boton ver diferido");
      	String tipoReport = "estadoPedido"; 
      	WebSISPEUtil.buscarDiferidos(request);
      	WebSISPEUtil.instanciarPopUpDiferidos(request,tipoReport);
      }
		/*-------- cuando se cancela ventana diferidos----------*/
	  else if(request.getParameter("cancelarDiferido") != null){ 
		LogSISPE.getLog().info("Boton Cancelar");
		session.removeAttribute(SessionManagerSISPE.POPUP);
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
		}else if(request.getParameter("cancelarMail")!=null){
			session.removeAttribute("ec.com.smx.sic.sispe.redactarMail");	
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
		}else if(ayuda!=null && ayuda.equals("siEnviarEmail")){
			VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
			
			if(vistaPedido.getEstadoPreciosAfiliado()!=null && vistaPedido.getEstadoPreciosAfiliado().equals(estadoActivo)){
				session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			}
			request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "ESTADO_PED");
			
			EstadoPedidoUtil.actualizarArticuloStockIntegracionSic(request, errors);
			
			//envio automatico del mail
			LogSISPE.getLog().info("empieza el envio..");				
			//obtengo el mail del Local y mail del Usuario
			String mailLocal = SessionManagerSISPE.getCurrentEntidadResponsable(request).getEmailLocal();
			String mailUsuario = SessionManagerSISPE.getCurrentEntidadResponsable(request).getEmailUser();
			
			LogSISPE.getLog().info("Mail Local---{}",mailLocal);
			LogSISPE.getLog().info("Mail Usuario---{}",mailUsuario);
			
			WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, false);
			try{
				
				MailMensajeEST mailMensajeEST = WebSISPEUtil.construirEstructuraMail(request, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.envioPedido"));
				if(formulario.getCcMail()!=null && !formulario.getCcMail().isEmpty()){
					mailMensajeEST.setCc(new String [] {formulario.getCcMail()});
				}
				mailMensajeEST.setPara(new String [] {formulario.getEmailEnviarCotizacion()});
				session.setAttribute("ec.com.smx.sic.sispe.pedido.fechaPedido",vistaPedido.getFechaInicialEstado());
				mailMensajeEST.setAsunto(formulario.getAsuntoMail());
//				mailMensajeEST.setMensaje(formulario.getTextoMail());
				mailMensajeEST.setFormatoHTML(true);
				LogSISPE.getLog().info("Nombre del Mail seteado en el objeto---{}",mailMensajeEST.getDe().toString());
				LogSISPE.getLog().info("CodigoEvento:::::: {}",mailMensajeEST.getEventoDTO().getId().getCodigoEvento());
				request.setAttribute("ec.com.smx.sic.sispe.envioMail", mailMensajeEST);
				formulario.setTextoMail(formulario.getTextoMail().replace("\n", "<br>"));
				request.setAttribute("ec.com.smx.sic.sispe.textoMail", formulario.getTextoMail());
				infos.add("envioMail",new ActionMessage("message.mail.send.exito"));
				session.removeAttribute("ec.com.smx.sic.sispe.redactarMail");
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			}catch(Exception ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				errors.add("envioMail",new ActionMessage("errors.mail.send.error",ex.getMessage()));
			}
		}else if(request.getParameter("imprimirConvenios")!=null){
		      	LogSISPE.getLog().info("presion\u00F3 el boton imprimir convenios");
		      	WebSISPEUtil.instanciarPopUpImpresionConvenios(request,Boolean.TRUE);
		      }
		else if(imprimirConvenioL){
	      	LogSISPE.getLog().info("impresion de convenios Laser");
	      	session.setAttribute("ec.com.smx.sic.sispe.imprimirConvenios","imprimirConvenioL");
	      	request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "okLaser");
	      }
		else if(imprimirConvenioM){
			LogSISPE.getLog().info("impresion de convenios Matricial");
			session.setAttribute("ec.com.smx.sic.sispe.imprimirConvenios","imprimirConvenioM");
			request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "okMatriz");
	      }
			
      /*-------- cuando se desea imprimir el pedido como texto ------------*/
      else if(imprimirDocumento){
      	LogSISPE.getLog().info("imprimir documento");
      	//Me indica que proviene de la pantalla estado pedido para armar los articulos diferidos
      	request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "ESTADO_PED");
      	
      	VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
		if(vistaPedidoDTO != null && vistaPedidoDTO.getVistaDetallesPedidosReporte() != null){
			if(!ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZADO .equals(vistaPedidoDTO.getId().getCodigoEstado())
					&& !ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RECOTIZADO.equals(vistaPedidoDTO.getId().getCodigoEstado())
					&& !ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ANULADO.equals(vistaPedidoDTO.getId().getCodigoEstado())
					&& !ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZACION_CADUCADA.equals(vistaPedidoDTO.getId().getCodigoEstado())){
				if(vistaPedidoDTO.getNumBonNavEmp()!=null && vistaPedidoDTO.getNumBonNavEmp().intValue()>0){
					Double valorMinimoCompra = 0D;
					Double valorBonoMaxi = 0D;
					
					//Obtengo el valor minimo de compra para el calculo bono maxi-navidad desde la tabla parametros
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.bono.monto.minimoCompra", request);
					if(parametroDTO.getValorParametro() != null){
						valorMinimoCompra = Double.parseDouble(parametroDTO.getValorParametro());
					}
					//Obtengo el valor minimo de compra para el calculo bono maxi-navidad desde la tabla parametros
					parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.valor.bono.maxinavidad", request);
					if(parametroDTO.getValorParametro() != null){
						valorBonoMaxi = Double.parseDouble(parametroDTO.getValorParametro());
					}
					
					request.getSession().setAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI,vistaPedidoDTO.getNumBonNavEmp());
					request.getSession().setAttribute(CotizarReservarAction.MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI, valorMinimoCompra);
					request.getSession().setAttribute(CotizarReservarAction.VALOR_BONO_COMPROBANTE_MAXI, valorBonoMaxi);
				}
			}
		}
        //se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
      	WebSISPEUtil.inicializarParametrosImpresion(request, SessionManagerSISPE.getEstadoActivo(request), true);
      }
	 //exportar a pdf
	  else if (generarPDF){
			LogSISPE.getLog().info("entra al reporte pdf");
			String tipoArchivo = "pdf";
			final String NOMBRE_REPORTE = "pedido";
			request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));
			//Me indica que proviene de la pantalla estado pedido para armar los articulos diferidos
	      	request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "ESTADO_PED");
			//se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
			WebSISPEUtil.inicializarParametrosImpresion(request, SessionManagerSISPE.getEstadoActivo(request), false);
			//request.removeAttribute("ec.com.smx.sic.sispe.mostrarConfirmacion");
			salida = "reporteEstadoPedidoPDF";
	  }
	  /*-------- CONFIRMACION de la generaci\u00F3n del documento como XSL -------*/
	  else if(generarXSL){
	    	LogSISPE.getLog().info("confirmar impresion XSL");
	    	VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
			if(vistaPedidoDTO != null && vistaPedidoDTO.getVistaDetallesPedidosReporte() != null){
				//se cargan los detalles de las recetas en caso de que no se hayan consultado todav\u00EDa
//				String tipoArticuloRecetaCanasta = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//				String tipoArticuloRecetaDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
				for(Iterator<VistaDetallePedidoDTO> it = vistaPedidoDTO.getVistaDetallesPedidosReporte().iterator(); it.hasNext();){
					VistaDetallePedidoDTO vistaDetallePedidoDTO = it.next();
					//si es una receta
//	  			if(vistaDetallePedidoDTO.getCodigoTipoArticulo()!=null && 
//	  					(vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaCanasta) || vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaDespensa))){
//	  			
		  			if(vistaDetallePedidoDTO.getCodigoClasificacion()!=null && 
		  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
		  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
		  				
		  				if(vistaDetallePedidoDTO.getArticuloDTO()== null)
		  					vistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
		  				
		  				if( !vistaDetallePedidoDTO.getArticuloDTO().getTieneArticuloRelacionado()){
			  				//se carga la receta del art\u00EDculo
			  				Collection recetaArticulo = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
			  				vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticulo);
		  				}
		  			}
				}
			}
	      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("Detalle_Pedido", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
	      	salida= "reporteEstadoXLS";
	  }
      //------------------- cuando se desea regresar a la pantalla principal
      else if(request.getParameter("atras")!=null){
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
    	  session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
    	  session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
    	  session.removeAttribute(CONVENIOS);
    	  //RESPALDO EL TABS
			if(session.getAttribute(RESPALDO_TABS)!= null){
				//session.setAttribute("ec.com.smx.sic.controlesusuario.tab", session.getAttribute(RESPALDO_TABS));
				session.setAttribute("ec.com.smx.sic.controlesusuario.tab", session.getAttribute(RESPALDO_TABRESPALDO));
				//se obtiene el bean que contiene los campos genericos
				//BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
				//beanSession.setPaginaTab((PaginaTab)session.getAttribute(RESPALDO_TABS));
				beanSession.setPaginaTab((PaginaTab)session.getAttribute(RESPALDO_TABRESPALDO));
			}
    	String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
      	LogSISPE.getLog().info("Acci\u00F3n actual: {}",accion);
      	if(accion!=null){
      		if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar")))
      			salida="listaCotizadosRecotizados";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion"))){
						//se guardan los indices de las jerarqu\u00EDas, en caso de ser necesario
						this.guardarIndicesNiveles(request);
      			salida="controlProduccion";
      		}
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion")))
      			salida="actualizarProduccion";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteVentasServicio"))){
      			request.setAttribute("accionAbonos", "desplegar");
      			salida="reporteVentas";
      		}      		
      		else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteDespachos"))) {
				salida="reporteDespachos";
			}      		
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho")))
      			salida="despacho";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despachoEspecial")))
      			salida="despachoEspecial";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega")))
      			salida="entrega";      		
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarAbonoPedido")))
      			salida="registroPago";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.verAbonoPedido")))
      			salida="registroPago";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.anulacion")))
      			salida="anulacion";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion")))
      			salida="listaConfirmarReserva";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"))){
      			salida="estadoPedido";
      			//se guarda en el request porque solo es temporal
      			request.setAttribute(EstadoPedidoAction.INDICE_PEDIDO_SELECCIONADO, session.getAttribute(EstadoPedidoAction.INDICE_PEDIDO_SELECCIONADO));
      			session.removeAttribute(EstadoPedidoAction.INDICE_PEDIDO_SELECCIONADO);
      		}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")))
      			salida="modificarReserva";
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.enviarDespacho"))){
						//se guardan los indices de las jerarqu\u00EDas, en caso de ser necesario
						this.guardarIndicesNiveles(request);
      			salida="enviarDespacho";
      		}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarOrdenCompra"))){
      			salida="ordenesCompra";
      		}
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteArticuloPedido"))){
      			// Se obtiene desde session el valor del registro completo elegido
      			if(session.getAttribute(KardexAction.SESSION_VAR_INDICES_VRG) != null){
      				// Se obtiene y carga en session el registro del indice padre
	      			String parametroVRG = (String)session.getAttribute(KardexAction.SESSION_VAR_INDICES_VRG);
	      			LogSISPE.getLog().info("INDICE VRG: {}",parametroVRG);
	      			
	      			String [] indiceActivo = parametroVRG.split("_");
	      			session.setAttribute(SESSION_VALOR_INDICE_ACTIVO_VRG, indiceActivo[0]);
      			
      			}
      			// Se anula el valor de session para una nueva ocasi\u00F3n
      			session.removeAttribute(KardexAction.SESSION_VAR_INDICES_VRG);
      			
      			// Se direcciona a la pantalla del reporte de articulos/pedidos del kardex
      			salida="reporteArticuloPedidos";
      		}
      		else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteEntregas"))){
      			// Se obtiene desde session el valor del registro completo elegido
      			if(session.getAttribute(ReporteEntregasAction.SESSION_VAR_INDICES_VRG) != null){
      				// Se obtiene y carga en session el registro del indice padre
	      			String parametroVRG = (String)session.getAttribute(ReporteEntregasAction.SESSION_VAR_INDICES_VRG);
	      			LogSISPE.getLog().info("INDICE VRG: {}",parametroVRG);
	      			
	      			String [] indiceActivo = parametroVRG.split("_");
	      			session.setAttribute(SESSION_VALOR_INDICE_ACTIVO_VRG, indiceActivo[0]);
      			
      			}
      			// Se anula el valor de session para una nueva ocasi\u00F3n
      			session.removeAttribute(ReporteEntregasAction.SESSION_VAR_INDICES_VRG);
      			
      			// Se direcciona a la pantalla del reporte de entregas
      			salida="reporteEntregas";
      		}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
      			salida="consolidarPedido";
      		}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.desbloquearReservaPOS"))){
      			salida="bloquearReservaPOS";
      		}else{
      			salida = "listaPedidos";
      		}
      	}
      	
      	((ListadoPedidosForm)form).setOpEstadoActivo(((ListadoPedidosForm)form).getEstadoActual()?ConstantesGenerales.ESTADO_ACTIVO:ConstantesGenerales.ESTADO_INACTIVO);
      	
      	LogSISPE.getLog().info("salir: {}",salida);
      	session.removeAttribute("ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones");
      }
			//Presentar el detalle de un pedido desde una subcolecci\u00F3n de la colecci\u00F3n principal de vistaArticulo
			else if(session.getAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO)!= null){
				
				List<VistaArticuloDTO> listVistaArticuloDTO = null;
				
				//se obtine la colecci\u00F3n de art\u00EDculos de sesi\u00F3n y se transforma a un List
				//se verifican las posibles variables de ses
				if(session.getAttribute(ActualizacionProduccionAction.COLECCION_ARTICULOS) != null){
					listVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(ActualizacionProduccionAction.COLECCION_ARTICULOS);
				}else if(session.getAttribute(RegistrarOrdenCompraAction.COL_PAGINA_ARTICULOS) != null){
					listVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(RegistrarOrdenCompraAction.COL_PAGINA_ARTICULOS);
				}else if(session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO)!=null){
					listVistaArticuloDTO = (List<VistaArticuloDTO>)session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO);
				}

				if(listVistaArticuloDTO != null){
					
					VistaArticuloDTO vistaArticuloDTO = null;

					String parametro = (String)session.getAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO);
					//se divide el indice enviado para llegar al indice del pedido
					String [] indiceGlobal = parametro.split("-");
					//en indiceGlobal[0] se encuentra la palabla IPVA por ese motivo los indices se toman desde la posici\u00F3n 1
					//se obtiene la colecci\u00F3n del segundo nivel
					List<VistaArticuloDTO> listVistaArticuloDTO2 = (List<VistaArticuloDTO>)listVistaArticuloDTO.get(Integer.parseInt(indiceGlobal[1])).getColVistaArticuloDTO();
					//se obtiene la colecci\u00F3n del tercer nivel
					List<VistaArticuloDTO> listVistaArticuloDTO3 = (List<VistaArticuloDTO>)listVistaArticuloDTO2.get(Integer.parseInt(indiceGlobal[2])).getColVistaArticuloDTO();
					//se verifica si existe una subcolecci\u00F3n para el nivel 2
					if(listVistaArticuloDTO3 != null && !listVistaArticuloDTO3.isEmpty() && indiceGlobal.length >= 4){
						//se obtiene el objeto que contiene los datos del pedido
						vistaArticuloDTO = listVistaArticuloDTO3.get(Integer.parseInt(indiceGlobal[3]));
					}else{
						vistaArticuloDTO = listVistaArticuloDTO2.get(Integer.parseInt(indiceGlobal[2]));
					}
					
					//se construye el objeto VistaPedidoDTO para realizar la consulta
					consultaVistaPedidoDTO = new VistaPedidoDTO();
					consultaVistaPedidoDTO.getId().setCodigoCompania(vistaArticuloDTO.getId().getCodigoCompania());
					consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(vistaArticuloDTO.getId().getCodigoAreaTrabajo());
					consultaVistaPedidoDTO.getId().setCodigoPedido(vistaArticuloDTO.getId().getCodigoPedido());
					consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

					//primero se obtienen los datos del pedido
					Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
					//se obtiene el primer elemento
					if(colVistaPedidoDTO!=null && !colVistaPedidoDTO.isEmpty()){
						VistaPedidoDTO vistaPedidoDTO = colVistaPedidoDTO.iterator().next();
						//se obtienen los detalles
						EstadoPedidoUtil.obtenerDetallesPedido(vistaPedidoDTO, request);
						obtenerEntregas(session, vistaPedidoDTO.getVistaDetallesPedidosReporte());
						obtenerRolesEnvioMail(request);
						PaginaTab tabDetallePedido= new PaginaTab("detalleEstadoPedido", "deplegar", 0,335, request);
						Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
						Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
						tabDetallePedido.addTab(tabDetallePedidoComun);	
						tabDetallePedido.addTab(tabDetalleEntregas);
						beanSession.setPaginaTab(tabDetallePedido);
					}else{
						errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
					}
				}
			}
			else if(request.getParameter("mensajesAutorizaciones") != null){
				LogSISPE.getLog().info("se muestra los mensajes de las autorizaciones");
				
				ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesConsulta = (ArrayList<EstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_CONSULTA);
				if(CollectionUtils.isNotEmpty(colaAutorizacionesConsulta)){
					
					VistaPedidoDTO vistaPedido = (VistaPedidoDTO) session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
					CotizarReservarForm formularioCotizacion = new CotizarReservarForm();
					//seteando datos necensarios en el metodo mostrarPopUpAutorizaciones
					if(vistaPedido != null){
						formularioCotizacion.setTotal(vistaPedido.getTotalPedido());
						formularioCotizacion.setTipoDocumento(vistaPedido.getTipoDocumentoCliente());
						formularioCotizacion.setNombreEmpresa(vistaPedido.getNombreEmpresa());
						formularioCotizacion.setNombrePersona(vistaPedido.getNombrePersona());
						if(StringUtils.isNotEmpty(vistaPedido.getTipoDocumentoCliente()) && vistaPedido.getTipoDocumentoCliente().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
							formularioCotizacion.setNumeroDocumento(vistaPedido.getRucEmpresa());
						}else{
							formularioCotizacion.setNumeroDocumento(vistaPedido.getNumeroDocumentoPersona());
						}
					}
				
					String urlComponenteAutorizaciones =  AutorizacionesUtil.mostrarPopUpAutorizaciones(colaAutorizacionesConsulta, request, formularioCotizacion, false);
					if(StringUtils.isNotEmpty(urlComponenteAutorizaciones)){
						request.getSession().setAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES, urlComponenteAutorizaciones);
					}else{
						errors.add("errorMostrarComponenteAut", new ActionMessage("errors.gerneral","Error al mostrar el componente de autorizaciones"));
					}
				}
			}
			//cuando presiona boton cancelar o cerrar del popUp de autorizaciones
			else if(request.getParameter("cancelar") != null){
				LogSISPE.getLog().info("cerrar popup de autorizaciones");
				session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
			}
			//cuando presiona el boton aceptar del popUp de autorizaciones
			else if(request.getParameter("autorizaciones") != null){
				LogSISPE.getLog().info("cerrar popup de autorizaciones");
				session.removeAttribute(CotizarReservarAction.POPUP_AUTORIZACIONES);
				infos.add("noPuedeAplicarAutorizacion", new ActionMessage("errors.gerneral", "Para aplicar la autorizaci\u00F3n por favor ingresar por RECOTIZAR o RESERVAR"));
			}
//			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
//				int posicionTab = beanSession.getPaginaTab().getTabSeleccionado();
//
//				ContactoUtil.cambiarTabContactoPedidos(beanSession, posicionTab);
//				LogSISPE.getLog().info("tab seleccionado {}",beanSession.getPaginaTab().getTituloTabSeleccionado());
//			}
			
			//IOnofre. Accion del boton adjuntar archivo de croquis
			else if(request.getParameter("mostrarPopUpArchCroquis") != null){
				
				LogSISPE.getLog().info("se va a mostrar el popUp adjuntar archivocroquis de la entrega: "+request.getParameter("indiceEntregaCroquis"));
				
				if(request.getParameter("indiceEntregaCroquis") != null && session.getAttribute(RESUMEN_ENTREGAS) != null){
					List<EntregaPedidoDTO> entregas = (List<EntregaPedidoDTO>) session.getAttribute(RESUMEN_ENTREGAS);
					int indice = Integer.valueOf(request.getParameter("indiceEntregaCroquis"));
					EntregaPedidoDTO entregaSeleccionadaDTO = entregas.get(indice);
		    	  	LogSISPE.getLog().info("Entra a mostra la ventana para subir el archivo del croquis");
					instanciarVentanaSubirArchivoCroquis(request, accionEstado);
					listarArchivosCroquis(request, entregaSeleccionadaDTO);
					session.setAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis", entregaSeleccionadaDTO);
				}
		     }
			else if(request.getParameter("cancelarArchCroquis") != null){
				LogSISPE.getLog().info("se cierra el popUp del archivo croquis");
				session.removeAttribute(SessionManagerSISPE.POPUP);
			}
			 else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("adjuntarArchivo")){
				 LogSISPE.getLog().info("se carga el archivo seleccionado");
				 LogSISPE.getLog().info("Boton Aceptar---------");
				 
				 //Obtenemos de sesion la entrega seleccionada
				 EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
				 String codigoLocal = entregaPedidoDTO.getCodigoAreaTrabajoEstadoPedido().toString();
				 String pedido = entregaPedidoDTO.getCodigoPedido();
				
				 boolean verificarArchivo = true;
				 LogSISPE.getLog().info("CodPedido->{}",pedido);
				 LogSISPE.getLog().info("CodLocal->{}",codigoLocal);
				 //se arma el archivo pedido
				 ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
				 ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
				 archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				 archivoPedidoDTO.setId(archivoPedidoID);
				 archivoPedidoDTO.setCodigoLocal(Integer.parseInt(codigoLocal));
				 archivoPedidoDTO.setCodigoPedido(pedido);
				 archivoPedidoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
				 archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.CroquisEntrega"));
				 archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));
				 archivoPedidoDTO.setArchivo(formulario.getArchivo().getFileData());
				 archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				 archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				 //verifica si el nuevo archivo no tiene el mismo nombre.
				 verificarArchivo = CotizacionReservacionUtil.validarArchivo(request, formulario.getArchivo().getFileName());
				 
				 //se validan archivos repetidos o mas de un archivo
				 if(verificarArchivo && entregaPedidoDTO.getArchivoEntregaPedidoDTO() == null){
				 	LogSISPE.getLog().info("Archivo Diferente");
				  	//se guarda el archivo pedido de la entrega seleccionada
				 	aceptarArchivoCroquis(request,archivoPedidoDTO);
				 	entregaPedidoDTO.setArchivoEntregaPedidoDTO(archivoPedidoDTO);
				 	entregaPedidoDTO.setSecuencialArchivoPedido(archivoPedidoDTO.getId().getSecuencialArchivoPedido());
				 	SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entregaPedidoDTO);
				 } else {
				   	if(verificarArchivo == false){
				   		LogSISPE.getLog().info("Archivo repetido");
					   	String mensaje = "El archivo ya existe, \u00BFdesea reemplazarlo?";
					   	instanciarVentanaAuxiliarArchivoCroquis(request, accionEstado, mensaje);
				   	}else{
				   		LogSISPE.getLog().info("Intenta ingresar mas de un archivo");
				   		String mensaje = "Solo se puede adjuntar un archivo por cada entrega, \u00BFdesea reemplazar el archivo?";
					   	instanciarVentanaAuxiliarArchivoCroquis(request, accionEstado, mensaje);
				   	}
					
				 }
				 salida = "desplegar";
			}
			//accion del icono eliminar archivo del popUp mostrarArchivoCroquis
			else if(request.getParameter("eliminarArchivo") != null){
				//Obtenemos de sesion la entrega seleccionada
				EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
				if(entregaPedidoDTO != null && entregaPedidoDTO.getSecuencialArchivoPedido() != null){
					
					eliminarReferenciaArchivoEntregaPedido(request, entregaPedidoDTO);
					 
					Long secuencialArchivo = Long.valueOf(entregaPedidoDTO.getSecuencialArchivoPedido());
					String pedido = entregaPedidoDTO.getCodigoPedido();
					//se cancela el archivo del beneficiario 
					LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",secuencialArchivo);
					LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",pedido);
					CotizacionReservacionUtil.eliminarArchivoBeneficiario(request, secuencialArchivo, pedido);
					Collection<ArchivoPedidoDTO> colFiles = (Collection<ArchivoPedidoDTO>) session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE);
					colFiles.removeAll(colFiles);
					salida = "desplegar";
					
					entregaPedidoDTO.setSecuencialArchivoPedido(null);
					entregaPedidoDTO.setArchivoEntregaPedidoDTO(null);
					
				}
			}
			//cierra el popUp de remplazo de archivo croquis
			else if(request.getParameter("cancelarReemplazo") != null){
				LogSISPE.getLog().info("se cierra el popUp de remplazo de archivo croquis");
				session.removeAttribute(SessionManagerSISPE.POPUPAUX);
			}
			//remplaza de archivo croquis anteriormente ingresado
			else if(request.getParameter("reemplazarArchivo") != null){
				LogSISPE.getLog().info("elimina el archivo existente, carga el nuevo y se cierra el popUp de remplazo de archivo croquis");
				//elimina el archivo existente
				EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
				if(entregaPedidoDTO != null && entregaPedidoDTO.getSecuencialArchivoPedido() != null){
					
					eliminarReferenciaArchivoEntregaPedido(request, entregaPedidoDTO);
					 
					Long secuencialArchivo = Long.valueOf(entregaPedidoDTO.getSecuencialArchivoPedido());
					String pedido = entregaPedidoDTO.getCodigoPedido();
					//se cancela el archivo del beneficiario 
					LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",secuencialArchivo);
					LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",pedido);
					CotizacionReservacionUtil.eliminarArchivoBeneficiario(request, secuencialArchivo, pedido);
					Collection<ArchivoPedidoDTO> colFiles = (Collection<ArchivoPedidoDTO>) session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE);
					colFiles.removeAll(colFiles);
					salida = "desplegar";
					
					entregaPedidoDTO.setSecuencialArchivoPedido(null);
					entregaPedidoDTO.setArchivoEntregaPedidoDTO(null);
					
				}
				//carga el nuevo archivo
				LogSISPE.getLog().info("se carga el archivo seleccionado");
				LogSISPE.getLog().info("Boton Aceptar---------");
				 
				//Obtenemos de sesion la entrega seleccionada
				//EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
				String codigoLocal = entregaPedidoDTO.getCodigoAreaTrabajoEstadoPedido().toString();
				String pedido = entregaPedidoDTO.getCodigoPedido();
				
				LogSISPE.getLog().info("CodPedido->{}",pedido);
				LogSISPE.getLog().info("CodLocal->{}",codigoLocal);
				//se arma el archivo pedido
				ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
				ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
				archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				archivoPedidoDTO.setId(archivoPedidoID);
				archivoPedidoDTO.setCodigoLocal(Integer.parseInt(codigoLocal));
				archivoPedidoDTO.setCodigoPedido(pedido);
				archivoPedidoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
				archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.CroquisEntrega"));
				archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));
				archivoPedidoDTO.setArchivo(formulario.getArchivo().getFileData());
				archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				 
				//se guarda el archivo pedido de la entrega seleccionada
				aceptarArchivoCroquis(request,archivoPedidoDTO);
				entregaPedidoDTO.setArchivoEntregaPedidoDTO(archivoPedidoDTO);
				entregaPedidoDTO.setSecuencialArchivoPedido(archivoPedidoDTO.getId().getSecuencialArchivoPedido());
				SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entregaPedidoDTO);
				salida = "desplegar";
				
				session.removeAttribute(SessionManagerSISPE.POPUPAUX);
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida = "errorGlobal";
		}
		saveErrors(request, errors);
		saveInfos(request, infos);
		// se envia el control a la p\u00E1gina correspondiente
		return mapping.findForward(salida);
	}
	

	public static void obtenerRolesEnvioMail(HttpServletRequest request) throws Exception {
		ParametroDTO parametroDTOR = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.rolesMail", request);
		if(parametroDTOR!=null && parametroDTOR.getValorParametro() != null){
			request.getSession().setAttribute("ec.com.smx.sic.sispe.rolesMail", parametroDTOR.getValorParametro());
		}else{
			request.getSession().setAttribute("ec.com.smx.sic.sispe.rolesMail", "");
		}
	}

	/**
	 * 
	 * @param request
	 */
	private void guardarIndicesNiveles(HttpServletRequest request){
		HttpSession session = request.getSession();
		//se obtiene el indice global del pedidio que fue seleccionado para ver su detalle
		String parametroIndice = (String)session.getAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO);
		if(parametroIndice!=null){
			String [] indiceGlobal = parametroIndice.split("-");
			//se guarda cada indice para utilizarlo en la p\u00E1gina de tal manera que la jerarqu\u00EDa donde estaba el
			//pedido aparesca abierta, no se toma en cuenta la posici\u00F3n cero
			request.setAttribute("indiceNivel1", indiceGlobal[1]);
			if(indiceGlobal.length > 2){
				request.setAttribute("indiceNivel2", indiceGlobal[2]);
			}
			//se elimina el par\u00E1metro
			session.removeAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO);
		}
	}
	
	/**
	 * PopUp informacion articulo obsoleto
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaObsoleto(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Art\u00EDculo obsoleto");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('detalleEstadoPedido.do', ['pregunta'], {parameters: 'cerrarPopUpObsoleto=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());			
		popUp.setContenidoVentana("servicioCliente/estadoPedido/popUpObsoleto.jsp");
		popUp.setAncho(40D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;		
	}
	
	/**
	 * Obtiene la configuraci\u00F3n de entregas del pedido
	 * @param session
	 * @param detallePedidoDTOCol 
	 * @param beanSession
	 * @param formulario
	 */
	public static void obtenerEntregas(HttpSession session, Collection<VistaDetallePedidoDTO> detallePedidoDTOCol){
		Collection<EntregaDetallePedidoDTO> entregasRes=new ArrayList<EntregaDetallePedidoDTO>();
		Collection<EntregaPedidoDTO> entregas=new ArrayList<EntregaPedidoDTO>();
		Collection<DetallePedidoDTO> detPedResp=new ArrayList<DetallePedidoDTO>();
		if(!CollectionUtils.isEmpty(detallePedidoDTOCol)){
			for(VistaDetallePedidoDTO detPed:detallePedidoDTOCol){
				if(!CollectionUtils.isEmpty(detPed.getEntregaDetallePedidoCol())){
					Collection<EntregaDetallePedidoDTO> detalleEntregas=(Collection<EntregaDetallePedidoDTO>)detPed.getEntregaDetallePedidoCol();
					if(CollectionUtils.isEmpty(entregasRes)){
						entregasRes=(Collection<EntregaDetallePedidoDTO>)SerializationUtils.clone((Serializable)detalleEntregas);
						for(EntregaDetallePedidoDTO entPed:entregasRes){
							detPedResp=new ArrayList<DetallePedidoDTO>();
							entPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
							dp.setNpContadorEntrega(entPed.getCantidadEntrega());
							dp.setNpContadorDespacho(entPed.getCantidadDespacho());
							dp.setNpCantidadReservaInicial(entPed.getCantidadParcialDespacho().longValue());
							EstructuraResponsable estRes=new EstructuraResponsable();
							estRes.setResponsablePedido(entPed.getEntregaPedidoDTO().getConfiguracionResPedDTO().getNombreConfiguracion());
							estRes.setResponsableProduccion(entPed.getEntregaPedidoDTO().getNpResProduccion());
							estRes.setResponsableDespacho(entPed.getEntregaPedidoDTO().getNpResDespacho());
							estRes.setResponsableEntrega(entPed.getEntregaPedidoDTO().getConfiguracionResEntDTO().getNombreConfiguracion());
							dp.setNpReponsable(estRes);
							detPedResp.add(dp);
							entPed.getEntregaPedidoDTO().setNpDetallePedido(detPedResp);
							entregas.add(entPed.getEntregaPedidoDTO());
						}
					}else{
						for(EntregaDetallePedidoDTO entDetPed:detalleEntregas){
							Boolean existeEntrega=Boolean.FALSE;
							DetallePedidoDTO dp= new DetallePedidoDTO();
							dp.setArticuloDTO(new ArticuloDTO());
							dp.getArticuloDTO().setId((ArticuloID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getId()));
							dp.getArticuloDTO().setDescripcionArticulo(detPed.getArticuloDTO().getDescripcionArticulo());
							dp.getArticuloDTO().setCodigoBarrasActivo(new ArticuloBitacoraCodigoBarrasDTO());
							dp.getArticuloDTO().getCodigoBarrasActivo().setId((ArticuloBitacoraCodigoBarrasID)SerializationUtils.clone((Serializable)detPed.getArticuloDTO().getCodigoBarrasActivo().getId()));
							dp.setNpContadorEntrega(entDetPed.getCantidadEntrega());
							dp.setNpContadorDespacho(entDetPed.getCantidadDespacho());
							dp.setNpCantidadReservaInicial(entDetPed.getCantidadParcialDespacho().longValue());
							EstructuraResponsable estRes=new EstructuraResponsable();
							estRes.setResponsablePedido(entDetPed.getEntregaPedidoDTO().getConfiguracionResPedDTO().getNombreConfiguracion());
							estRes.setResponsableProduccion(entDetPed.getEntregaPedidoDTO().getNpResProduccion());
							estRes.setResponsableDespacho(entDetPed.getEntregaPedidoDTO().getNpResDespacho());
							estRes.setResponsableEntrega(entDetPed.getEntregaPedidoDTO().getConfiguracionResEntDTO().getNombreConfiguracion());
							dp.setNpReponsable(estRes);
							for(EntregaDetallePedidoDTO entPedRes:entregasRes){
								Long diferencia= entDetPed.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()-entPedRes.getEntregaPedidoDTO().getFechaEntregaCliente().getTime();
								if(diferencia==0L && entDetPed.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim().equals(entPedRes.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim())
										&&	entPedRes.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()==entDetPed.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()){
									entPedRes.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
									existeEntrega=Boolean.TRUE;
									break;
								}
							}
							if(!existeEntrega){
								entDetPed.getEntregaPedidoDTO().setNpDetallePedido(new ArrayList<DetallePedidoDTO>());
								entDetPed.getEntregaPedidoDTO().getNpDetallePedido().add(dp);
								entregasRes.add(entDetPed);
								entregas.add(entDetPed.getEntregaPedidoDTO());
							}
						}
					}
				}
			}
		}
		entregas=ColeccionesUtil.sort(entregas,ColeccionesUtil.ORDEN_ASC,"fechaDespachoBodega");
		session.setAttribute(RESUMEN_ENTREGAS,entregas);
	}
	
	/**
	 * Configura el popUp de carga de archivo croquis
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author IOnofre
	 */
	private void instanciarVentanaSubirArchivoCroquis(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro en instanciarVentanaSubirArchivoCroquis");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Cargar archivo Croquis");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'cancelarArchCroquis=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarArchCroquis=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaArchivoBene.jsp");
		popUp.setAccionEnvio(accion);
		popUp.setAncho(50D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * Lista los archivos cargados de croquis
	 * @param request
	 * @param entregaPedidoDTO
	 * @throws Exception
	 * @author IOnofre
	 */
	private void listarArchivosCroquis(HttpServletRequest request, EntregaPedidoDTO entregaPedidoDTO)throws Exception{
		LogSISPE.getLog().info("Entro listar los archivos de croquis");
		HttpSession session = request.getSession();		
		Collection<ArchivoPedidoDTO> colFiles = new ArrayList<ArchivoPedidoDTO>();
		if(entregaPedidoDTO.getArchivoEntregaPedidoDTO() != null){
			colFiles.add(entregaPedidoDTO.getArchivoEntregaPedidoDTO());
		}
	    
	    //se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
		session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
	}
	
	/**
	 * Acepta el archivo de croquis
	 * @param request
	 * @param archivoArmado
	 * @throws Exception
	 * @author Ionofre
	 */
	private void aceptarArchivoCroquis(HttpServletRequest request,ArchivoPedidoDTO archivoArmado)throws Exception{
		LogSISPE.getLog().info("Entro a aceptarArchivoCroquis");
		HttpSession session = request.getSession();
		//ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
	    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		//seteo el codigo del pedido
		archivoPedidoDTO.setCodigoPedido(archivoArmado.getCodigoPedido());
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		try{			    
		    //entra al servicio de guardar en la base el archivo beneiciario
		    SessionManagerSISPE.getServicioClienteServicio().transCrearArchivoBenficiario(archivoArmado);
		    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
		    Collection<ArchivoPedidoDTO> colFiles = new ArrayList<ArchivoPedidoDTO>();
		    colFiles.add(archivoArmado);
			//se sube a sesion los archivo filtrados por codigoPedido
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
			LogSISPE.getLog().info("size files ->"+colFiles.size());
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}	
	}
	
	/**
	 * PopUp de pregunta, si deseea sobreescribir el archivo existente
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author IOnofre
	 */
	private void instanciarVentanaAuxiliarArchivoCroquis(HttpServletRequest request,String accion, String mensaje)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar la ventana de confirmacion sobre escribir archivo croquis");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo Croquis");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'reemplazarArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarReemplazo=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setPreguntaVentana(mensaje);
		session.setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
		popUp = null;
	}
	
	/**
	 * Obtiene datos de entregaPedidoDTO y actualiza de la base el secuencialArchivoEntregaPedidoDTO.
	 * @param request
	 * @param entregaPedidoDTO
	 * @throws SISPEException
	 * @throws Exception
	 * @author IOnofre
	 */
	private void eliminarReferenciaArchivoEntregaPedido(HttpServletRequest request, EntregaPedidoDTO entregaPedidoDTO) throws SISPEException, Exception{
		EntregaPedidoDTO entregaPedidoConsultaDTO = new EntregaPedidoDTO();
		entregaPedidoConsultaDTO.setSecuencialArchivoPedido(entregaPedidoDTO.getSecuencialArchivoPedido());
		entregaPedidoConsultaDTO.getId().setCodigoCompania(entregaPedidoDTO.getId().getCodigoCompania());
		Collection<EntregaPedidoDTO> entregasPedidoCol = SessionManagerSISPE.getServicioClienteServicio().findObtenerEntregaPedidoDTO(entregaPedidoConsultaDTO);
		 
		if(CollectionUtils.isNotEmpty(entregasPedidoCol)){
			LogSISPE.getLog().info("se eliminara la referencia de {} entregasPedidoDTO",entregasPedidoCol.size());
			for(EntregaPedidoDTO entPedDTO : entregasPedidoCol){
				entPedDTO.setSecuencialArchivoPedido(null);
				entPedDTO.setArchivoEntregaPedidoDTO(null);
				SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entPedDTO); 
			}
		}
	}
	
}
