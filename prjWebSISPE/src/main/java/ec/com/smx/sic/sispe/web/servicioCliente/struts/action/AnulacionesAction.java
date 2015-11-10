/*
 * Clase AnulacionesAction.java 
 * Creado 13/06/06
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.mensajeria.commons.resources.MensajeriaMessages;
import ec.com.smx.mensajeria.dto.EventoDTO;
import ec.com.smx.mensajeria.dto.id.EventoID;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDocumentoTransaccionDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.AnulacionesForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Clase que permite la manipulaci\u00F3n de datos de Anulaciones y generar 
 * la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * 
 * @author 	dlopez, fmunoz
 * @version 2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class AnulacionesAction extends BaseAction 
{
	//se obtienen los estados de b\u00FAsqueda
	private String estadoCotizado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado");
	private String estadoRecotizado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado");
	private String estadoCotizacionCaducada = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada");
	private String estadoReservado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
	private String estadoEnProduccion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
	private String estadoProducido = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
	private String estadoSolicitadoEspecial = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.solicitado");
	private String estadoConfirmadoEspecial = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.confirmado");
	private String estadoDespachoPrevioEspecial = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.despachoPrevio");
	private String estadoDespachado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
	
	private final String ordenAscendente = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	private static final String ANULAR_PEDIDO_ESPECIAL = "SI_AP";
	private static final String ANULAR_PEDIDO_CON_CIERRE_DIA="SI_AP_CIERRE";

	public static final String INDICE_PEDIDO_SELECCIONADO = "ec.com.smx.sic.sispe.anulacion.numPedido";
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
	 * Este m\u00E9todo permite:
	 * <ul>
	 * <li>Mostrar el listado de Pedidos que se pueden anular en base a una busqueda por:</li>
	 * 	<ul>
	 * 	<li>N\u00FAmero de Pedido</li>
	 * 	<li>C\u00E9dula</li>
	 *  <li>Nombre del contacto</li>
	 *  <li>Fecha del Estado</li>.
	 * 	</ul>
	 * <li>Mostrar el detalle del pedido</li>
	 * </ul>
	 * 
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          				campos
	 * @param request 		La petici&oacute;n que estamos procesando
	 * @param response		La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		String peticion = request.getParameter(Globals.AYUDA);

		HttpSession session = request.getSession();
		AnulacionesForm formulario = (AnulacionesForm) form;
		//SqlTimestampConverter convertidor; 	//convertidor de una fecha de formato String a Timestamp
		String salida = "desplegar"; 		//retorno para forward por defecto

		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
//		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		try
		{
			/*----------- cuando se da clic en los campos de paginaci\u00F3n ---------------*/
			if(request.getParameter("range")!=null || request.getParameter("start")!=null)
			{
				LogSISPE.getLog().info("ENTRO A LA PAGINACION");
				Collection<VistaPedidoDTO> datos= (Collection<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				if(datos!=null){
					int size= datos.size();
					int range= Integer.parseInt(request.getParameter("range"));
					int start= Integer.parseInt(request.getParameter("start"));
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
					formulario.setDatos(datosSub);
					session.setAttribute("ec.com.smx.sic.sispe.paginacion.start",request.getParameter("start"));
					
					//se elimina la ventana de sesi\u00F3n
					session.removeAttribute(SessionManagerSISPE.POPUP);
				}
			}
			//cuando se busca un pedido--------------------------------------------
			else if (request.getParameter("buscar") != null) 
			{
				//colecci\u00F3n que almacena los pedidos buscados
				Collection<VistaPedidoDTO> pedidos = new ArrayList<VistaPedidoDTO>();
				//DTO que contiene los pedidos a buscar
				
				//BORRAR ------------------------
				LogSISPE.getLog().info("Llama a m\u00E9todo x3");
				// -------------------------------
				
				VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
				//filtro por el estado del pedido
				if(formulario.getEstadoPedido() == null || formulario.getEstadoPedido().equals("")){
					consultaPedidoDTO.getId().setCodigoEstado(estadoCotizado.concat(",").concat(estadoRecotizado).concat(",").concat(estadoReservado).concat(",")
							.concat(estadoCotizacionCaducada).concat(",").concat(estadoEnProduccion).concat(",").concat(estadoProducido).concat(",").concat(estadoSolicitadoEspecial)
							.concat(",").concat(estadoConfirmadoEspecial).concat(",").concat(estadoDespachoPrevioEspecial).concat(",").concat(estadoDespachado));
				}
				//consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				//se asignan los campos de ordenamiento
				String [][] camposOrden = new String [][]{{"fechaInicialEstado",ordenAscendente}};
				consultaPedidoDTO.setNpCamposOrden(camposOrden);
				
				//Borrado de la lista de paginaci\u00F3n
				formulario.setDatos(null);
				//se almacena en sesi\u00F3n el objeto VistaPedidoDTO, para actualizar 
				session.setAttribute("ec.com.smx.sic.sispe.busqueda.consultaPedidoDTO",consultaPedidoDTO);

				try 
				{
					//se consultan los pedidos de acuerdo al par\u00E1metro de busqueda
					pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
					
					for(VistaPedidoDTO vistaPedidoDTO:pedidos){
						Collection<VistaDetallePedidoDTO> detallePedido = vistaPedidoDTO.getVistaDetallesPedidosReporte();
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
							consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
							//busqueda de los detalles
							detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
							vistaPedidoDTO.setVistaDetallesPedidosReporte(detallePedido);

						}
						ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoDTO);
					}

					//verifica si la consulta no fue vac\u00EDa
					if(pedidos == null || pedidos.isEmpty()) {
						infos.add("listaVacia", new ActionMessage("message.listaVacia", "pedidos para Anular"));
						formulario.setDatos(null);
					}else{
						int size= pedidos.size();
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
						int start= 0;
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));

						Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);
						formulario.setDatos(datosSub);
						//se establece la consulta de registros en sesi\u00F3n
						session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, pedidos);

					}

					//variables para presentaci\u00F3n
					session.setAttribute("ec.com.smx.sic.sispe.estado.anulado",
							MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"));

					//se eliminan las variables de sessi\u00F3n que controlan la aparici\u00F3n del estilo
					session.removeAttribute(INDICE_PEDIDO_SELECCIONADO);
					//se elimina la variable de inicio de paginaci\u00F3n
					session.removeAttribute("ec.com.smx.sic.sispe.paginacion.start");

				} catch (SISPEException e) {
					errors.add("Anulaciones", new ActionMessage("errors.llamadaServicio.obtenerDatos", "Anulaciones"));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}
			}
			//cuando presiona el link anular, pedir autorizaci\u00F3n----------------------------
			else if (request.getParameter("indice") != null)
			{
				int indice = Integer.parseInt(request.getParameter("indice"));
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO 
				VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
				LogSISPE.getLog().info("estado pedido: {}", vistaPedidoDTO.getId().getCodigoEstado());												
				//se verifica si la reserva esta bloqueada porque se ya se realizo una orden de salida y esta en proceso de entrega
				if(EstadoPedidoUtil.reservaBloqueadaDesdeSICMER(errors, vistaPedidoDTO.getId().getCodigoPedido(),vistaPedidoDTO.getId().getCodigoCompania())) {
					String accionPopUp="anular";
					String accionOK="hide(['popupConfirmar']);ocultarModal();";
					CotizacionReservacionUtil.instanciarPopUpNotificacionBloqueoReservaSICMER(request, vistaPedidoDTO.getLlaveContratoPOS(),accionPopUp,accionOK);
					salida = "desplegar";
				}else{
				
					session.setAttribute(INDICE_PEDIDO_SELECCIONADO, String.valueOf(indice));
					
					//si el tipo de pedido es normal
					if(vistaPedidoDTO.getCodigoTipoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"))){
						
						EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();
						estadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						estadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
						estadoPedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
						estadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
						estadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
						estadoPedidoDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
						
						//Consultamos si el pedido tiene notas de credito activas
						Collection<EstadoPedidoDocumentoTransaccionDTO> colNotasCredidoActiva = 
								SISPEFactory.getServicioIntegracionPOS().obtenerEstadoPedidoDocumentoTransaccion(estadoPedidoDTO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.documento.nota.credito"));
						
						//Solo se puede reservar si el pedido no tiene notas de credito activas
						if (CollectionUtils.isEmpty(colNotasCredidoActiva)) {
							Double valorPedido=vistaPedidoDTO.getTotalPedido()==null?0:vistaPedidoDTO.getTotalPedido();
							Double valorAbonado=vistaPedidoDTO.getAbonoPedido()==null?0:vistaPedidoDTO.getAbonoPedido();
							Collection<VistaPedidoDTO> visPedCol=null;
							if(valorAbonado.longValue()>valorPedido.longValue()){
								visPedCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerHistoricoModificacionesReserva(vistaPedidoDTO,estadoPedidoDTO);
							}
							
							//Si tiene vales de caja de igual manera se muestar el mensaje para no dejar modificar la reserva
							if (CollectionUtils.isEmpty(visPedCol)) {
										
						
								//se crea la ventana para el mensaje
								UtilPopUp popUp = new UtilPopUp();
								popUp.setTituloVentana("Anulaci\u00F3n del pedido");
								
								//se verifica si el pedido tiene abonos realizados
//								if(!vistaPedidoDTO.getCodigoEstadoPagado().equals(estadoSinPago)){
//									StringBuffer mensajeError = new StringBuffer();
//									popUp.setEtiquetaBotonOK("Aceptar");
//									popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
//									popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
//									popUp.setFormaBotones(UtilPopUp.OK);
//									popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//									//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POS
//									mensajeError.append("La reservaci\u00F3n No <b>");
//									if(vistaPedidoDTO.getLlaveContratoPOS() != null){
//										mensajeError.append(vistaPedidoDTO.getLlaveContratoPOS());
//									}
//									popUp.setMensajeVentana(mensajeError.append("</b> no puede ser anulada desde esta pantalla, porque ya tiene abonos realizados, la anulaci\u00F3n se debe realizar desde el <b>Punto de Venta</b>.").toString());
//									
//									//se guarda la ventana
//									request.setAttribute(SessionManagerSISPE.POPUP, popUp);
//			
//								}else{
									popUp.setEtiquetaBotonOK("Si");
									popUp.setEtiquetaBotonCANCEL("No");
									popUp.setMensajeVentana("Antes de continuar debe ingresar un n\u00FAmero de autorizaci\u00F3n, o estar autorizado para generar una autorizaci\u00F3n.");
									
									String codigoAreaTrabajo = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo().toString();
									if(StringUtils.isNotEmpty(codigoAreaTrabajo) && Integer.parseInt(codigoAreaTrabajo) == 0){
										//esta logueado el adminsispe
										String areaTrabajoPedido = AutorizacionesUtil.asignarAreaTrabajoAutorizacion(request);
										popUp.setMensajeVentana(popUp.getMensajeVentana().concat(" Debe ingresar el usuario y contrase\u00F1a del administrador del local "+areaTrabajoPedido));
									}
									
									popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
									popUp.setValorOK("aplicarAutorizacion");
									popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
									popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
									//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POS
									popUp.setPreguntaVentana("\u00BFDesea anular el pedido No <b>".concat(vistaPedidoDTO.getId().getCodigoPedido()).concat("</b>?"));
									popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
									popUp.setTope(75d);
									
									session.setAttribute(SessionManagerSISPE.POPUP, popUp);
									session.setAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2, "ok");
			//					}
								
								//se guardan algunas variables que se utilizar\u00E1n en la p\u00E1gina
								request.setAttribute("ec.com.smx.sic.sispe.anular","ok");
							}else {
									String tipoDocumento="un vale de caja";
									instanciarPopUpReservaNotaCredito(request, vistaPedidoDTO.getLlaveContratoPOS(),tipoDocumento);
									salida = "desplegar";
								}
								
							}else {
								String tipoDocumento="una nota de cr\u00E9dito";
								instanciarPopUpReservaNotaCredito(request, vistaPedidoDTO.getLlaveContratoPOS(), tipoDocumento);
								salida = "desplegar";
							}
					}else{
						//para anulaci\u00F3n pedidos especiales
						UtilPopUp popUp = new UtilPopUp();
						popUp.setTituloVentana("Anulaci\u00F3n de pedidos especiales");
						popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
						popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
						popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
						popUp.setEtiquetaBotonOK("Aceptar");
						popUp.setEtiquetaBotonCANCEL("Cancelar");
						popUp.setPreguntaVentana("\u00BFEsta seguro que desea anular el pedido No "+ vistaPedidoDTO.getId().getCodigoPedido() + "?");
						
						if(vistaPedidoDTO.getId().getCodigoEstado().equals(estadoSolicitadoEspecial)){
							LogSISPE.getLog().info("entra a solicitado carnes");
							popUp.setValorOK(ANULAR_PEDIDO_ESPECIAL);
						}
						else if(vistaPedidoDTO.getId().getCodigoEstado().equals(estadoConfirmadoEspecial)&& 
								vistaPedidoDTO.getFechaCierrePedidoEspecial()==null){
							LogSISPE.getLog().info("no se cerr\u00F3 el d\u00EDa");
							popUp.setMensajeVentana("El pedido ya ha sido confirmado");
							popUp.setValorOK(ANULAR_PEDIDO_ESPECIAL);
						}
						else if(vistaPedidoDTO.getId().getCodigoEstado().equals(estadoConfirmadoEspecial)&& 
								vistaPedidoDTO.getFechaCierrePedidoEspecial()!=null){
							LogSISPE.getLog().info("se cerr\u00F3 el d\u00EDa");
							popUp.setMensajeVentana("El d\u00EDa fue cerrado para este pedido, se enviar\u00E1 un mail al encargado de bodega");
							popUp.setValorOK(ANULAR_PEDIDO_CON_CIERRE_DIA);
						}
						//se guarda
						request.setAttribute(SessionManagerSISPE.POPUP, popUp);
					}	
				}
			}
			else if(peticion != null && peticion.equals(ANULAR_PEDIDO_ESPECIAL)){
				LogSISPE.getLog().info("entra a valor-si-anular");
				
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtiene el indice de la sesi\u00F3n del registro a anular
				int indice = Integer.parseInt(session.getAttribute(INDICE_PEDIDO_SELECCIONADO).toString());
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) pedidos.get(indice);
				SessionManagerSISPE.getServicioClienteServicio().transRegistrarAnulacion(vistaPedidoDTO, SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail());
				//se refresca el listado de pedidos
				consultarPedidos(request,formulario);
				//mensaje de exito para la anulaci\u00F3n
				success.add("exitoAnulacion",new ActionMessage("message.exito.registro","La Anulaci\u00F3n"));
			}
			//cuando se anula un pedido que ya a sido cerrado
			else if(peticion != null && peticion.equals(ANULAR_PEDIDO_CON_CIERRE_DIA)){
				LogSISPE.getLog().info("entra a anular con cierre d\u00EDa");
				
				/*-----------anulaci\u00F3n del pedido------------*/
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtiene el indice de la sesi\u00F3n del registro a anular
				int indice = Integer.parseInt(session.getAttribute(INDICE_PEDIDO_SELECCIONADO).toString());
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) pedidos.get(indice);
				
				ArrayList<UserDto> userDTOCol= (ArrayList<UserDto>)WebSISPEUtil.obtenerUsuariosTipoPedido( vistaPedidoDTO.getCodigoTipoPedido(), request);
				if(userDTOCol!=null){
					String[] userMail = new String[userDTOCol.size()];
					LogSISPE.getLog().info("tama\u00F1o: {}" ,userDTOCol.size());
					for(int i=0;i<userDTOCol.size();i++)
					{
						UserDto userDto = userDTOCol.get(i);
						userMail[i]=userDto.getUserEmail();
						LogSISPE.getLog().info("mail: {}",userMail[i]);
					}
					/*-----------env\u00EDo mail---------------------*/
					//Parametros necesarios para buscar la plantilla, deben ser ingresados por cada sistema
					EventoID eventoID=new EventoID();
					eventoID.setCodigoEvento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.anulacionPedidoEspecial"));
					eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
					eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					EventoDTO eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);
					MailMensajeEST mailMensajeEST=new MailMensajeEST();
					mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuenta.sispe"));
					mailMensajeEST.setPara(userMail);
					String descripcionEvento = eventoDTO.getDescripcionEvento().replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.numeroPedido"),vistaPedidoDTO.getId().getCodigoPedido());
					descripcionEvento = descripcionEvento.replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.nombreUsuario"), SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreCompletoUsuario());				
					mailMensajeEST.setAsunto(eventoDTO.getAsuntoEvento());
					mailMensajeEST.setMensaje(descripcionEvento);
					mailMensajeEST.setEventoDTO(eventoDTO);
					//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
					mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
					mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
					mailMensajeEST.setFormatoHTML(true);
					//metodo para enviar el mail
					success.add("envioMail",new ActionMessage("messages.mail.send.exitoEnvio"));
					SessionManagerSISPE.getMensajeria().transEnvioMail(mailMensajeEST, false);
					LogSISPE.getLog().info("***MAIL ENVIADO***");
				}
				
				//registra la anulaci\u00F3n
				SessionManagerSISPE.getServicioClienteServicio().transRegistrarAnulacion(vistaPedidoDTO, SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail());
				//se refresca el listado de pedidos
				consultarPedidos(request, formulario);
				//mensaje de exito para la anulaci\u00F3n
				success.add("exitoAnulacion",new ActionMessage("message.exito.registro","La Anulaci\u00F3n"));
			}else if (request.getParameter("cerrarPopUpModReserva") != null) {
				session.removeAttribute(SessionManagerSISPE.POPUP);
				salida = "desplegar";
			}
			//------------- Cuando se anula un pedido --------------------
			else if (request.getParameter(Globals.AYUDA)!=null && request.getParameter(Globals.AYUDA).equals("aplicarAutorizacion"))
			{
//				//se obtiene el tipo de autorizaci\u00F3n.
//				String tipoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.anularPedido");
//				String grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
				
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtiene el indice de la sesi\u00F3n del registro a anular
				int indice = Integer.parseInt(session.getAttribute(INDICE_PEDIDO_SELECCIONADO).toString());
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO
				VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
				
				boolean pasoValidacion= AutorizacionesUtil.validarAutorizacionPorNumeroUsuarioContrasenia(formulario, request, success, infos, errors, 
						ConstantesGenerales.TIPO_AUTORIZACION_ANULAR_PEDIDO.longValue());
				try{
					if(pasoValidacion){
						
						//llamada al m\u00E9todo que crea el objeto autorizaci\u00F3n
						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerObjAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_ANULAR_PEDIDO ,null);
//								AutorizacionesUtil.construirNuevaAutorizacion(request,request.getParameter("observacionAutorizacion"),
//								grupoAutorizacion, tipoAutorizacion, SessionManagerSISPE.getCurrentEntidadResponsable(request), vistaPedidoDTO.getId().getCodigoAreaTrabajo());
						
						//se asigna la autorizaci\u00F3n previamente creada
						vistaPedidoDTO.setAutorizacionDTO(autorizacionDTO);
						vistaPedidoDTO.setNpAutorizacionesWorkFlow((Collection<String>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW));
						//se llama a la funci\u00F3n que carga los descuentos del pedido
						//esto es necesario para el correcto registro de los descuentos en caso de que existan
						WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO,request,Boolean.FALSE);
						
						if(vistaPedidoDTO.getCodigoEstadoPagado()!=null && !vistaPedidoDTO.getCodigoEstadoPagado().isEmpty()){
							//llamada al m\u00E9todo de anulaci\u00F3n de pedido
							SessionManagerSISPE.getServicioClienteServicio().transRegistrarAnulacion(vistaPedidoDTO, SessionManagerSISPE.getDefault().getLoggedUser(request).getUserEmail());
							request.getSession().removeAttribute(AutorizacionesUtil.AUTORIZACIONES_FINALIZAR_WORKFLOW); //eliminar la autorizacion
						}else {
							throw new SISPEException("El pedido no puede se anulado getCodigoEstadoPagado() NULL" + vistaPedidoDTO.getCodigoEstadoPagado());
						}
						
						//se refresca el listado de pedidos
						consultarPedidos(request,formulario);

						//se elimina las variables de sessi\u00F3n que controlan la aparici\u00F3n del estilo
						session.removeAttribute(INDICE_PEDIDO_SELECCIONADO);
						
						//mensaje de exito
						success.add("pedidoAnulado", new ActionMessage("message.exito.anulacionPedido"));
						
						//se elimina la ventana de sesi\u00F3n
						session.removeAttribute(SessionManagerSISPE.POPUP);
					}		
					else{
						//se saca el contenido de los errores en una cadena para imprimirlo en el popUp
						String errores = CotizacionReservacionUtil.obtenerValorDeAcctionMessages(errors);
						errors = new ActionMessages();
						
						LogSISPE.getLog().info("USUARIO NO VALIDO");
						UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
						if(StringUtils.isNotEmpty(errores)){
							utilPopUp.setMensajeErrorVentana(errores.toString());
						}else{
							utilPopUp.setMensajeErrorVentana("Los datos del usuario son inv\u00E1lidos");	
						}
						
						request.setAttribute("ec.com.smx.sic.sispe.anular","ok");
					}
				}catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					UtilPopUp utilPopUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
					utilPopUp.setMensajeErrorVentana("La autorizaci\u00F3n ingresada no es v\u00E1lida");
					
					formulario.setNumeroAutorizacion(request.getParameter("numeroAutorizacion"));
					request.setAttribute("ec.com.smx.sic.sispe.anular","ok");
				}
			}
			//caso por defecto - se inicializa la acci\u00F3n-------------------------
			else {
				//se inicializan las propiedades de la paginaci\u00F3n
				formulario.setDatos(null);
				formulario.setSize(null);
				formulario.setStart(null);
				formulario.setRange(null);
				formulario.setEtiquetaFechas("Fecha de Estado");
				formulario.setOpEstadoActivo(estadoActivo);

				//se eliminan las variables de sesi\u00F3n que comienzen con ec.com
				SessionManagerSISPE.removeVarSession(request);

				//se verifica si la entidad responsable es un local
				String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
					//se obtienen los locales por ciudad
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

				//se guarda la accion
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.anulacion"));
				//t\u00EDtulo de la ventana
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de anulaciones");
				//se inicia la consulata de estados
				EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
				consultaEstadoDTO.getId().setCodigoEstado(estadoCotizado.concat(",").concat(estadoRecotizado).concat(",").concat(estadoCotizacionCaducada).concat(",")
						.concat(estadoReservado).concat(",").concat(estadoEnProduccion).concat(",").concat(estadoProducido).concat(",").concat(estadoSolicitadoEspecial)
						.concat(",").concat(estadoConfirmadoEspecial).concat(",").concat(estadoDespachoPrevioEspecial));
				consultaEstadoDTO.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.estadoNormal"));

				//Obtener datos de la colecci\u00F3n de estados, en la base de datos
				Collection<EstadoSICDTO> estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
				//guardar en sesi\u00F3n esta colecci\u00F3n
				session.setAttribute(SessionManagerSISPE.COL_ESTADOS, estados);

				//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
      	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
      			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
      	
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		
		//se guardan los mensajes generados
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);

		return mapping.findForward(salida);
	}
	
	/**
	 * @param request
	 * @param formulario
 	 * @param pedidos
	 **/
	private Collection<VistaPedidoDTO> consultarPedidos(HttpServletRequest request,	AnulacionesForm formulario)throws Exception{
		
		HttpSession session = request.getSession();
		Collection<VistaPedidoDTO> pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido((VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.busqueda.consultaPedidoDTO"));

		session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, pedidos);
		formulario.setDatos(null); //se blanquea la colecci\u00F3n del formulario

		//verifica si la consulta no fue vac\u00EDa
		if (pedidos != null && !pedidos.isEmpty()){
			int size= pedidos.size();
			int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
			int start= 0;
			if(session.getAttribute("ec.com.smx.sic.sispe.paginacion.start")!=null){
				try{
					start = Integer.parseInt((String)session.getAttribute("ec.com.smx.sic.sispe.paginacion.start"));
				}catch(NumberFormatException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}
			}
			formulario.setStart(String.valueOf(start));
			formulario.setRange(String.valueOf(range));
			formulario.setSize(String.valueOf(size));

			Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);
			formulario.setDatos(datosSub);
		}
		
		return pedidos;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpReservaNotaCredito(HttpServletRequest request, String numeroReserva,String tipoDocumento)throws Exception{
		
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Reserva con nota de cr\u00E9dito pendiente");
		popUp.setEtiquetaBotonOK("Aceptar");
//		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('anulacionPedido.do',['mensajes','seccion_autorizacion','lista_pedidos','divPopUp'], {parameters: 'cerrarPopUpModReserva=ok', evalScripts:true});ocultarModal();");
//		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarCon=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		
//		popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
//		popUp.setValorOK("hide(['popupConfirmar']);ocultarModal();");
		
		popUp.setPreguntaVentana("La reserva No. ".concat(numeroReserva).concat(" tiene "+tipoDocumento+" pendiente. Ac\u00E9rquese al Punto de Venta"));
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
		
	}
	
	

	
}