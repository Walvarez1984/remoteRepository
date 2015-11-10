/*
 * DetalleEstadoPedidoEspecialAction.java
 * Creado el 08/04/2008 14:37:17
 *   	
 */
package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.mensajeria.commons.util.WebMensajeriaUtil;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleEstadoPedidoAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author nperalta
 *
 */
@SuppressWarnings("unchecked")
public class DetalleEstadoPedidoEspecialAction extends BaseAction {
	private static final String DIRECTORIO_SALIDA_REPORTE = "ec.com.smx.sic.sispe.reporte.directorioSalida";
	public static final String INDICE_PEDIDO_SELECCIONADO = "ec.com.smx.sic.sispe.pedidoSeleccionado.indice";
	
	private static final String VISTA_PEDIDO = "ec.com.smx.sic.sispe.vistaPedido";
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{

		ActionMessages exitos = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		ListadoPedidosForm formulario = (ListadoPedidosForm)form;

		HttpSession session = request.getSession();
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		boolean generarXSL = false;
		String ayuda= request.getParameter(Globals.AYUDA);
		if(ayuda!=null && !ayuda.equals("")){
			if(ayuda.equals("xls"))
				generarXSL = true;
		}
		LogSISPE.getLog().info("valor del ayuda: {}",ayuda);
		String forward="detallePedido";
		
		try{
			//----- cuando se desea ver el detalle (submit) ----------
			if(request.getParameter("detallePedido")!=null){
				//toma el \u00EDndice del pedido seleccionado
				int indice= Integer.parseInt(request.getParameter("detallePedido"));
				//se obtienen los datos del pedido
				this.obtenerPedido(indice, request,errors);

				forward="detallePedido";
			}
			//------ cuando se hace clic en Atras --------
			else if(request.getParameter("atras")!=null){
				String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
				if(accion!=null){
					if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaConfirmarPedidoEspecial"))){
						forward="listaPedidosEspeciales";
					}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaControlProduccionPedEsp"))){
						//se guardan los indices de las jerarqu\u00EDas, en caso de ser necesario
						this.guardarIndicesNiveles(request);
						forward="listaControlProduccionPedEsp";
					}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"))){
						forward="estadoPedido";
					}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.anulacion"))){
						forward="anulacionPedido";
					}
				}
				session.removeAttribute(VISTA_PEDIDO);
				MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
			}else if(request.getParameter("redactarMail")!=null){
				LogSISPE.getLog().info("popUp de envio de correo");
				session.setAttribute("ec.com.smx.sic.sispe.redactarMail", "ok");		
				VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				session.setAttribute("ec.com.smx.sic.sispe.paraMail","");
				if(vistaPedido!=null){
					
					//setando por defecto cero en valores nulos para que pinte bien
					vistaPedido.setValorTotalBrutoSinIva(vistaPedido.getValorTotalBrutoSinIva() != null ? vistaPedido.getValorTotalBrutoSinIva() : 0D);
					vistaPedido.setTotalDescuentoIva(vistaPedido.getTotalDescuentoIva()!= null ? vistaPedido.getTotalDescuentoIva() : 0D );
					vistaPedido.setSubTotalNetoBruto(vistaPedido.getSubTotalNetoBruto() != null ? vistaPedido.getSubTotalNetoBruto() : 0D);
					vistaPedido.setSubTotalNoAplicaIVA(vistaPedido.getSubTotalNoAplicaIVA() != null ? vistaPedido.getSubTotalNoAplicaIVA() : 0D);
					vistaPedido.setSubTotalAplicaIVA(vistaPedido.getSubTotalAplicaIVA() != null ? vistaPedido.getSubTotalAplicaIVA() : 0D);
					vistaPedido.setIvaPedido(vistaPedido.getIvaPedido() != null ? vistaPedido.getIvaPedido() : 0D);
					vistaPedido.setValorCostoEntregaPedido(vistaPedido.getValorCostoEntregaPedido() != null ? vistaPedido.getValorCostoEntregaPedido() : 0D);
					vistaPedido.setTotalPedido(vistaPedido.getTotalPedido() != null ? vistaPedido.getTotalPedido() : 0D);
					
					ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedido);
					session.setAttribute("ec.com.smx.sic.sispe.textoMail", MessagesWebSISPE.getString("messages.mail.textoMail").replace("{0}", " "+(vistaPedido.getNombreEmpresa()!=null?vistaPedido.getNombreEmpresa():vistaPedido.getNombrePersona())));
						
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
				}
				session.setAttribute("ec.com.smx.sic.sispe.asuntoMail",  "Pedido especial");
			}else if(request.getParameter("cancelarMail")!=null){
				session.removeAttribute("ec.com.smx.sic.sispe.redactarMail");	
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			}else if(ayuda!=null && ayuda.equals("siEnviarEmail")){
				VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				if(vistaPedido.getEstadoPreciosAfiliado()!=null && vistaPedido.getEstadoPreciosAfiliado().equals(estadoActivo)){
					session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
				}
				
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
//					mailMensajeEST.setMensaje(formulario.getTextoMail());
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
			}
			else if(request.getParameter("confirmarPDF")!=null){
				LogSISPE.getLog().info("va a generar PDF");
				//se crea el nombre del archivo
				String nombreArchivo = "pedidoEspecial";
				//se llama al m\u00E9todo que forma el nombre completo del archivo
				String nombreCompletoArchivo = WebSISPEUtil.generarNombreArchivo(nombreArchivo, "pdf");
				//se guarda el nombre del archivo
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", nombreCompletoArchivo);
				//llamada al m\u00E9todo que crea el directorio en el servidor, donde se almacena el archivo
				//que luego ser\u00E1 enviado por mail
				String directorioSalida = WebMensajeriaUtil.crearCarpeta("SISPE") + nombreCompletoArchivo;
				//se guarda el directorio de salida
				session.setAttribute(DIRECTORIO_SALIDA_REPORTE, directorioSalida);
				LogSISPE.getLog().info("ANTES DE GENERAR EL ARCHIVO PDF");

				//se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
				WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, false);

				//salida a la p\u00E1gina del PDF	
				forward="reportePDF";
			}

			else if(request.getParameter("confirmarImpresionTexto")!=null){
				LogSISPE.getLog().info("va a imprimir el pedido");
				//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
				request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");

				forward="resumenDetalle";
			}
			
			/*-------- CONFIRMACION de la generaci\u00F3n del documento como XSL -------*/
		    else if(generarXSL){
		    	LogSISPE.getLog().info("confirmar impresion XLS");
		      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("Reporte de Pedido Especial", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
		      	forward="rptEstadoPedidoEspecialXLS";
		    }
			
			//Presentar el detalle de un pedido desde una subcolecci\u00F3n de la colecci\u00F3n principal de vistaArticulo
			else if(session.getAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO)!= null){
				
				List<VistaArticuloDTO> listVistaArticuloDTO = null;
				
				//se obtine la colecci\u00F3n de art\u00EDculos de sesi\u00F3n y se transforma a un List
				if(session.getAttribute(SessionManagerSISPE.COL_VISTA_ARTICULO)!=null){
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
					VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
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
						this.obtenerDetallesPedido(vistaPedidoDTO, request);
						//se almacena el DTO obtenido en sesi\u00F3n.
						session.setAttribute(VISTA_PEDIDO,vistaPedidoDTO);
					}else{
						errors.add("detallePedido", new ActionMessage("errors.obtenerDatos", "Pedido"));
					}
					DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
				}
			}
			else{
				Integer indice = (Integer)session.getAttribute(INDICE_PEDIDO_SELECCIONADO);
				//se obtienen los datos del pedido
				this.obtenerPedido(indice, request, errors);
			}

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			forward = "errorGlobal";
		}
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);
		return mapping.findForward(forward);

	}

	/**
	 * 
	 * @param indice
	 * @param request
	 * @throws Exception
	 */
	private void obtenerPedido(int indice, HttpServletRequest request, ActionMessages errors)throws Exception{
		HttpSession session = request.getSession();
		List<VistaPedidoDTO>pedidos = new ArrayList<VistaPedidoDTO>();
		//se verifica la acci\u00F3n actual
		String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//si la acci\u00F3n actual es el despacho de pedidos especiales
		if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaControlProduccionPedEsp"))){
			PaginaTab paginaTab = (PaginaTab)session.getAttribute(SessionManagerSISPE.PAGINA_TAB);
			//se verifica el tab seleccionado
			if(paginaTab!= null && paginaTab.esTabSeleccionado(1)){
				//obtener la colecci\u00F3n de pedidos por despachar
				pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			}else if(paginaTab!=null && paginaTab.esTabSeleccionado(2)){
				//obtener la colecci\u00F3n de pedidos pendientes
				pedidos=(List<VistaPedidoDTO>)session.getAttribute(ControlProduccionPedidoEspecialAction.COL_PEDIDOS_PENDIENTES);
			}
		}else{
			//obtener de la sesion la colecci\u00F3n de los pedidos en general
			pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
		}
	
		if(pedidos!=null && !pedidos.isEmpty()){
			VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
			this.obtenerDetallesPedido(vistaPedidoDTO, request);

			//se almacena el DTO obtenido en sesi\u00F3n.
			session.setAttribute(VISTA_PEDIDO,vistaPedidoDTO);

			LogSISPE.getLog().info("**estado: {}", vistaPedidoDTO.getId().getCodigoEstado());
			
			ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoDTO);
		}
		
		DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
		
		MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
	}

	/**
	 * 
	 * @param vistaPedidoDTO
	 * @param request
	 * @throws Exception
	 */
	private void obtenerDetallesPedido(VistaPedidoDTO vistaPedidoDTO, HttpServletRequest request) throws Exception
	{
		HttpSession session = request.getSession();
		
		//se obtiene el nombre del local de entrega en caso de que no est\u00E9 cargado
		if(vistaPedidoDTO.getCodigoLocalEntrega()!=null && vistaPedidoDTO.getNombreLocalEntrega().equals("")){
			LocalID consultaLocalID = new LocalID();
			consultaLocalID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaLocalID.setCodigoLocal(vistaPedidoDTO.getCodigoLocalEntrega());
			LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(consultaLocalID);
			//se guarda el local
			vistaPedidoDTO.setNombreLocalEntrega(localDTO.getNombreLocal());
		}
		
		Collection detallePedido = vistaPedidoDTO.getVistaDetallesPedidosReporte();
		if(detallePedido==null){
			LogSISPE.getLog().info("trae el detalle del pedido");
			//creaci\u00F3n del objeto VistaDetallePedidoDTO para la consulta
			VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
			consultaVistaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
			//LogSISPE.getLog().info("CODIGO DEL LOCAL: "+ vistaPedidoDTO.getId().getCodigoLocal());

			consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			//consultaVistaDetallePedidoDTO.setCodigoTipoArticulo(vistaPedidoDTO.getCodigoTipoPedido());
			consultaVistaDetallePedidoDTO.getObservacionAutorizacionOrdenCompra();

			//asignaci\u00F3n de los par\u00E1metros de consulta
			consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			consultaVistaDetallePedidoDTO.getEstadoDetallePedido();
			consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			//busqueda de los detalles
			detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
			vistaPedidoDTO.setVistaDetallesPedidosReporte(detallePedido);

		}
		
		//se sube a sesi\u00F3n el c\u00F3digo del estado despachado
		session.setAttribute("ec.com.smx.sic.sispe.estado.despachadoEspecial",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachado"));
		session.setAttribute("ec.com.smx.sic.sispe.estado.despachoPrevio",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio"));
		session.setAttribute("ec.com.smx.sic.sispe.estado.solicitadoEspecial",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.solicitado"));
	}
	
	/**
	 * 
	 * @param request
	 */
	private void guardarIndicesNiveles(HttpServletRequest request)
	{
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
}
