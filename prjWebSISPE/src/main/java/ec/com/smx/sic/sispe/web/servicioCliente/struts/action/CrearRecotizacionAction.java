/*
 * Clase CrearRecotizacionAction.java
 * Creado el 10/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

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

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.DescuentosUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase prepara y carga los datos de la cotizaci\u00F3n o recotizaci\u00F3n seleccionada , para realizar su Recotizaci\u00F3n. 
 * </p>
 * @author 	fmunoz
 * @author Wladimir L\u00F3pez
 * @version 2.0
 * @since 	JSDK 1.4.2
 */
public class CrearRecotizacionAction extends BaseAction
{
	public static final String INGRESA_DIRECTAMENTE_RECOTIZAR = "ec.com.smx.sic.sispe.recotizacion";
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacute;n que estamos procesando
   * @param response		La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{

		HttpSession session= request.getSession();
		CotizarReservarForm formulario = (CotizarReservarForm)form;
		ActionMessages warnings = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		
		//se obtiene la clave que indica al estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String salida = "desplegar";
		
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		/*------ procesa la petici\u00F3n cuando se escogi\u00F3 la opci\u00F3n reservar desde el formulario de busqueda de -------
		 *------------------------------ cotizaciones y recotizaciones ---------------------------------------------
		 */
		if(request.getParameter("indice")!=null)
		{
			LogSISPE.getLog().info("Buscar cotizaciones y recotizaciones");
			String accion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion");
			//se asigna la aci\u00F3n actual
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, accion);
			List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			int indice = Integer.parseInt(request.getParameter("indice"));
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
			
			//se valida que el pedido en session sea el actual en la BDD
			Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO);
			
			//se quita de sesion la bandera que indica si debe o no mostrar el boton de mensajes autorizaciones en el estado del pedido
			session.removeAttribute("ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones");
			
			if(vistaPedidoDTO.getCodigoConsolidado() != null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
				session.setAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
			}
			if(pedidoActual){
				try{
					StringBuffer validacionFecha = CotizacionReservacionUtil.verificacionFechaInicialPedido(request,vistaPedidoDTO);
					if(validacionFecha == null){
						
						session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
						//se carga la configuraci\u00F3n de los descuentos
						CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
						//Obtener el tipo de descuento por cajas y mayorista
						CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
						//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
						boolean eliminoEntregas = CotizacionReservacionUtil.construirDetallesPedidoDesdeVista(formulario, request, infos, errors, warnings,false,Boolean.TRUE);

						if(eliminoEntregas)
							warnings.add("siEliminoEntregas",new ActionMessage("warnings.entregasEliminadas"));
						//se obtienen los d\u00EDas de validez para una cotizaci\u00F3n
						ParametroDTO consultaParametroDTO = new ParametroDTO();
						consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						String diasValidez = SessionManagerSISPE.getServicioClienteServicio().transObtenerDiasValidezCotizacion(consultaParametroDTO);
						session.setAttribute(CotizarReservarAction.DIAS_VALIDEZ, diasValidez);

							//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la recotizaci\u00F3n
							//session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
									//MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearRecotizacion.estado"));
							
							session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
									MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
							
//							//se obtiene la descripci\u00F3n del tipo de autorizaci\u00F3n gen\u00E9rico que permite crear una reservaci\u00F3n
//							TipoAutorizacionDTO tipoAutorizacionFiltro = new TipoAutorizacionDTO();
//							tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//							tipoAutorizacionFiltro.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"));
//							
//							Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCol = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionFiltro);
//							tipoAutorizacionFiltro = null;
//							//se obtiene el \u00FAnico registro
//							if(tipoAutorizacionDTOCol != null && !tipoAutorizacionDTOCol.isEmpty()){
//								//se guarda la descripci\u00F3n en sesi\u00F3n
//								session.setAttribute(CotizarReservarAction.DESCRICPION_USO_AUTORIZACION, tipoAutorizacionDTOCol.iterator().next().getDescripcion());
//							}
							//Se inicializa el tipo de autorizacion generico
							AutorizacionesUtil.iniciarTipoAutorizacionGenerico(request);
							
							//obtengo el codigoTipDesMax-navidad desde un parametro
							CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);

							//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
							session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de cotizaci\u00F3n");

							//variable que sirve para saber que inicialmente se ingres\u00F3 a una recotizaci\u00F3n,
							//esta se utiliza en la jsp para mostrar o no el combo de locales
							session.setAttribute(INGRESA_DIRECTAMENTE_RECOTIZAR,"ok");

							//se asigna el estado activo a esta opci\u00F3n para que no valide el stock cuando se guarde una cotizaci\u00F3n
							formulario.setOpValidarPedido(estadoActivo);
							
							
							//se valida si el pedido viene ya con una autorizacion de %peso cambio pavos
							List<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
							//metodo que verifica que autorizaciones tiene el pedido
							AutorizacionesUtil.verificacionAutorizaciones(detallePedidoDTOCol.get(0).getId().getCodigoPedido(), request, errors, exitos);
							if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null
									&& (Boolean) session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
								//para el caso de pedidos consolidados se arma la cola de autorizaciones de todos los detalles
								Collection<DetallePedidoDTO> detallePedidosConsolidados = (Collection<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
								AutorizacionesUtil.verificarAutorizacionesVariables(detallePedidosConsolidados, request, formulario);
							} else{
								AutorizacionesUtil.verificarAutorizacionesVariables(detallePedidoDTOCol, request, formulario);
							}
//							AutorizacionesUtil.verificarClasificacionesPedido(request, errors, warnings, Boolean.FALSE);							
							AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, errors);
							DescuentosUtil.validarSiAplicaDescuentoMarcaPropia(request, infos);
//							//verficamos si es local de tipo aki para habilitar o desabilitar el check de precios de afiliado
//							if(CotizacionReservacionUtil.verificarFormatoNegocioPreciosAfiliado(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento(), request)){
//								session.setAttribute(HABILITADO_PRECIOS_AFILIADO, "OK");
//							}else {
//								session.removeAttribute(HABILITADO_PRECIOS_AFILIADO);
//							}
					}else{
						String[] parametros = validacionFecha.toString().split(",");
						errors.add("FECHA_INICIAL_ESTADO_PEDIDO",new ActionMessage("errors.validacion.fechaInicialEstadoPedido","recotizaci\u00F3n",parametros[0],parametros[1]));
						session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
						salida = "listado";
					}
				}catch(Exception ex){
					ActionMessages errorsArticulos = (ActionMessages) request.getAttribute("ec.com.smx.sic.sispe.errores.articulos");
					if(errorsArticulos != null && !errorsArticulos.isEmpty()){
						errors = errorsArticulos;
					}
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					salida = "listado";
				}
			}else {
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
				warnings.add("pedidoModificado", new ActionMessage("warnings.pedido.modificado.anteriormente", vistaPedidoDTO.getId().getCodigoPedido()));
				salida = "listado";
			}
			//se cargan los datos del contacto
			ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());
			//Se contruyen los tabs de Contacto y Pedidos
			PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
			beanSession.setPaginaTab(tabsCotizaciones);
			
			//ejecutar el metodo para inicializar el controlador adecuado
			ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
			
		}
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);
		
		//se guardan todos los mensajes generados
		saveWarnings(request,warnings);
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);
		
		LogSISPE.getLog().info("sale por: {}",salida);
		
		//Se transfiere el control a la p\u00E1gina correspondiente	
		return mapping.findForward(salida);	
	}
  
}
