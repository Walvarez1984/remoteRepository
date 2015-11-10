/*
 * Clase ListaBloquearReservaPOSAction.java 
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.BloquearReservaPOSForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author 	
 * @version
 * @since
 */
@SuppressWarnings("unchecked")
public class ListaBloquearReservaPOSAction extends BaseAction{
	
	//se obtienen los estados de b\u00FAsqueda
	private String estadoReservado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
	private String estadoEnProduccion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
	private String estadoProducido = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
	public static final String SECUENCIAL_TRANSACCION = "ec.com.smx.sic.sispe.secuencial.transaccion";
	
	private final String ordenAscendente = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");

	private static final String INDICE_PEDIDO_SELECCIONADO = "ec.com.smx.sic.sispe.anulacion.numPedido";
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
	 * Este m\u00E9todo permite:
	 * <ul>
	 * <li>Mostrar el listado de Pedidos que se encuentran bloqueados en el POS por el proceso de pago.:</li>
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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
//		String peticion = request.getParameter(Globals.AYUDA);

		HttpSession session = request.getSession();
		BloquearReservaPOSForm formulario = (BloquearReservaPOSForm) form;
		//SqlTimestampConverter convertidor; 	//convertidor de una fecha de formato String a Timestamp
		String salida = "desplegar"; 		//retorno para forward por defecto

		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		try{
			/*----------- cuando se da clic en los campos de paginaci\u00F3n ---------------*/
			if(request.getParameter("range")!=null || request.getParameter("start")!=null){
				LogSISPE.getLog().info("ENTRO A LA PAGINACION");
				Collection datos= (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				if(datos!=null){
					int size= datos.size();
					int range= Integer.parseInt(request.getParameter("range"));
					int start= Integer.parseInt(request.getParameter("start"));
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
					formulario.setDatos(datosSub);
					session.setAttribute("ec.com.smx.sic.sispe.paginacion.start",request.getParameter("start"));
					
					//se elimina la ventana de sesi\u00F3n
					session.removeAttribute(SessionManagerSISPE.POPUP);
				}
			}
			//cuando se busca un pedido--------------------------------------------
			else if (request.getParameter("buscar") != null){
				
				//colecci\u00F3n que almacena los pedidos buscados
				Collection<VistaPedidoDTO> pedidos = new ArrayList<VistaPedidoDTO>();
				
				VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
				//filtro por el estado del pedido
				if(formulario.getEstadoPedido() == null || formulario.getEstadoPedido().equals("")){
					consultaPedidoDTO.getId().setCodigoEstado(estadoReservado.concat(ConstantesGenerales.CARACTER_SEPARACION).concat(estadoEnProduccion).concat(ConstantesGenerales.CARACTER_SEPARACION).concat(estadoProducido).concat(ConstantesGenerales.CARACTER_SEPARACION).concat(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO).concat(ConstantesGenerales.CARACTER_SEPARACION));
				}
				//consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				//se asignan los campos de ordenamiento
				String [][] camposOrden = new String [][]{{"fechaInicialEstado",ordenAscendente}};
				consultaPedidoDTO.setNpCamposOrden(camposOrden);
				
				consultaPedidoDTO.setHabilitarCambiosPos(estadoActivo);
				
				//Borrado de la lista de paginaci\u00F3n
				formulario.setDatos(null);
				//se almacena en sesi\u00F3n el objeto de consulta VistaPedidoDTO, para refrescar la pantalla 
				session.setAttribute("ec.com.smx.sic.sispe.busqueda.consultaPedidoDTO",consultaPedidoDTO);
				
				try {
					//se consultan los pedidos de acuerdo al par\u00E1metro de busqueda
					pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
					
					//verifica si la consulta no fue vac\u00EDa
					if(pedidos == null || pedidos.isEmpty()) {
						infos.add("listaVacia", new ActionMessage("message.listaVacia", "pedidos para desbloquear"));
						formulario.setDatos(null);
					}else{
						int size= pedidos.size();
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
						int start= 0;
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));
						
						Collection datosSub = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);
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
					errors.add("PedidosBloqueados", new ActionMessage("errors.llamadaServicio.obtenerDatos", "Pedidos Bloqueados en el POS"));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}
			}
			//cuando presiona el link de desbloquear la reserva
			else if (request.getParameter("indice") != null){
				int indice = Integer.parseInt(request.getParameter("indice"));
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO 
				VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
				LogSISPE.getLog().info("getCodigoPedido: {}", vistaPedidoDTO.getId().getCodigoPedido());
				
				session.setAttribute(INDICE_PEDIDO_SELECCIONADO, String.valueOf(indice));
				session.setAttribute(SECUENCIAL_TRANSACCION, vistaPedidoDTO.getSecuencialTransaccion());
				
				instanciarPopUpDesbloquearReserva(request);
				
			}
			
			//cuando presiona el boton de confirmar desbloquear
//			else if (request.getParameter(Globals.AYUDA)!=null && request.getParameter(Globals.AYUDA).equals("desbloquearReserva")){
			else if(request.getParameter("desbloquearReserva")!=null){
				LogSISPE.getLog().info("Se procede a desbloquear la reserva");
				
				int indice = Integer.valueOf((session.getAttribute(INDICE_PEDIDO_SELECCIONADO).toString())).intValue();
				//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos
				List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>) session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//obtenci\u00F3n del registro de la colecci\u00F3n como objeto vistaPedidoDTO 
				VistaPedidoDTO vistaPedidoDTO = pedidos.get(indice);
				
				if(vistaPedidoDTO!=null){
					LogSISPE.getLog().info("getCodigoPedido: {}", vistaPedidoDTO.getId().getCodigoPedido());
					try {
						
						String usuarioLogueado = SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreUsuario();
						vistaPedidoDTO.setUserId(usuarioLogueado);
						SessionManagerSISPE.getServicioClienteServicio().transActualizarDesbloquearReservaPOS(vistaPedidoDTO, formulario.getNumeroConfirmacion().trim());
						infos.add("BloquearReservaPOSExito", new ActionMessage("info.reserva.desbloqueado", "BloquearReservaPOSExito"));
						session.removeAttribute(SessionManagerSISPE.POPUP);
						//se refresca la consulta de pedidos
						consultarPedidos(request, formulario);
					} catch (SISPEException ex) {
						errors.add("BloquearReservaPOS", new ActionMessage("errors.reserva.desbloqueado", "BloquearReservaPOS"));
						errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));
						//throw new SISPEException(e);
					}
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
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.desbloquearReservaPOS"));
				//t\u00EDtulo de la ventana
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario desbloquear reserva");
				//se inicia la consulata de estados
				EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
				consultaEstadoDTO.getId().setCodigoEstado(estadoReservado.concat(",").concat(estadoEnProduccion).concat(",").concat(estadoProducido));
				consultaEstadoDTO.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.estadoNormal"));

				//Obtener datos de la colecci\u00F3n de estados, en la base de datos
				Collection estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
				//guardar en sesi\u00F3n esta colecci\u00F3n
				session.setAttribute(SessionManagerSISPE.COL_ESTADOS, estados);

				//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
				session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
				
				//siempre buscar por el estado actual
				session.setAttribute("ec.com.smx.sic.sispe.estado.actual",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
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
	 * Instanciar el popUp para ingresar el n&uacute;mero de confirmaci&oacute;n generado en el POS
	 * @param request
	 */
	private void instanciarPopUpDesbloquearReserva(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Desbloquear reserva");
		popUp.setEtiquetaBotonOK("Desbloquear");
		popUp.setEtiquetaBotonCANCEL("Cancelar");
		popUp.setContenidoVentana("servicioCliente/bloquearReservaPOS/mensajeConfirmacion.jsp");
		popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
		popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
		popUp.setValorOK("requestAjax('bloquearReservaPOS.do', ['pregunta','div_pagina','mensajesBloquearPOS','divPopUp','mensajes'], {parameters: 'desbloquearReserva=ok', evalScripts:true});ocultarModal();");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setAncho(42D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * Se consulta los pedidos que se encuentran bloqueados desde el POS
	 * @param request
	 * @param formulario
	 * @return
	 * @throws Exception
	 */
	private Collection consultarPedidos(HttpServletRequest request,	BloquearReservaPOSForm formulario)throws Exception{
		
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

			Collection datosSub = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);
			formulario.setDatos(datosSub);
		}
		
		return pedidos;
	}
	
}