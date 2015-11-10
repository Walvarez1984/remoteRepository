/*
 * Clase CrearReservacionAction.java
 * Creado el 07/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.DescuentosUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase prepara y carga los datos de una cotizaci\u00F3n o recotizaci\u00F3n, para su Reservaci\u00F3n. 
 * </p>
 * @author	fmunoz
 * @version 2.0
 * @since	JSDK 1.4.2
 */
public class CrearReservacionAction extends BaseAction
{
	public static final String INGRESA_DIRECTAMENTE_RESERVAR = "ec.com.smx.sic.sispe.reservacion";
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		HttpSession session= request.getSession();
		CotizarReservarForm formulario = (CotizarReservarForm)form;
		ActionMessages warnings=new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		
		//se obtiene la clave que indica al estado inactivo
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		//se obtiene la clave que indica al estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String salida = "desplegar";

		/*------ procesa la petici\u00F3n cuando se escogi\u00F3 la opci\u00F3n reservar desde el formulario de busqueda de ---------- 
		 *------------------------------ cotizaciones y recotizaciones ------------------------------------------------
		 */
		if(request.getParameter("indice")!=null)
		{
			String accion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion");
			//se asigna la aci\u00F3n actual
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, accion);
			List pedidos = (List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			int indice = Integer.parseInt(request.getParameter("indice"));
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
			
			//se valida que el pedido en session sea el actual en la BDD
			Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO);
			
			if(pedidoActual){
				try{
					StringBuffer validacionFecha = CotizacionReservacionUtil.verificacionFechaInicialPedido(request,vistaPedidoDTO);
					if(validacionFecha == null){
						
						if(vistaPedidoDTO.getCodigoConsolidado() != null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
							session.setAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
						}
						request.getSession().setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_NAVEMP_CREDITO, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito"));
						//se carga la configuraci\u00F3n de los descuentos
						CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
						//Obtener el tipo de descuento por cajas y mayorista
						CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
						//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
						boolean eliminoEntregas = CotizacionReservacionUtil.construirDetallesPedidoDesdeVista(formulario, request, infos, errors, warnings,false,Boolean.TRUE);
						if(eliminoEntregas)
							warnings.add("siEliminoEntregas",new ActionMessage("warnings.entregasEliminadas"));
			
						//se inicializa la propiedad que indica validaci\u00F3n del formulario
						formulario.setOpValidarPedido(estadoInactivo);
			
						//se inicializan los par\u00E1metros de la reservaci\u00F3n y algunos datos en sesi\u00F3n y el formulario
						inicializarParametros(request, formulario);
						
						//obtengo el codigoTipDesMax-navidad desde un parametro
						CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
						
						//se guardan en sesi\u00F3n el estado de la autorizaci\u00F3n en la reservacion
						session.setAttribute(CotizarReservarAction.MOSTRAR_AUTORIZACION,
								MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion.estado"));
			
						//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
						session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de reservaci\u00F3n");
			
						//variable que sirve para saber que inicialmente se ingres\u00F3 a una reservaci\u00F3n,
						//esta se utiliza en la jsp para mostrar o no el combo de locales
						session.setAttribute(INGRESA_DIRECTAMENTE_RESERVAR,"ok");
						
						//se valida si el pedido viene ya con una autorizacion de %peso cambio pavos
						List<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
						
						//metodo que verifica que autorizaciones tiene el pedido
						AutorizacionesUtil.verificacionAutorizaciones(detallePedidoDTOCol.get(0).getId().getCodigoPedido(), request, errors, exitos);
						AutorizacionesUtil.verificarAutorizacionesVariables(detallePedidoDTOCol, request, formulario);
//						AutorizacionesUtil.verificarClasificacionesPedido(request, errors, warnings, Boolean.FALSE);						
						AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, errors);
						DescuentosUtil.validarSiAplicaDescuentoMarcaPropia(request, infos);
						if(vistaPedidoDTO.getValorAbonoInicialManual() != null){
							formulario.setValorAbono(vistaPedidoDTO.getValorAbonoInicialManual().toString());
						}
						if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS) == null 
								&& EntregaLocalCalendarioUtil.verificarEntregasProximoDespacho(request)){
							LogSISPE.getLog().info("mostrar popUp editar entregas por entregas proximas a despachar");
							instanciarPopUpDespachosProximos(request);
						}
//						//verficamos si es local de tipo aki para habilitar o desabilitar el check de precios de afiliado
//						if(CotizacionReservacionUtil.verificarFormatoNegocioPreciosAfiliado(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento(), request)){
//							session.setAttribute(HABILITADO_PRECIOS_AFILIADO, "OK");
//						}else {
//							session.removeAttribute(HABILITADO_PRECIOS_AFILIADO);
//						}
						
						//se cargan los datos del contacto
						ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());
						
						//Se contruyen los tabs de Contacto y Pedidos
						PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request, formulario);
						beanSession.setPaginaTab(tabsCotizaciones);
						
						//ejecutar el metodo para inicializar el controlador adecuado
						ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
					}else{
						String[] parametros = validacionFecha.toString().split(",");
						errors.add("FECHA_INICIAL_ESTADO_PEDIDO",new ActionMessage("errors.validacion.fechaInicialEstadoPedido","reservaci\u00F3n",parametros[0],parametros[1]));
						session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
						salida = "listado";
					}
				}catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					salida = "listado";
				}
			}else{
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
				warnings.add("pedidoModificado", new ActionMessage("warnings.pedido.modificado.anteriormente", vistaPedidoDTO.getId().getCodigoPedido()));
				salida = "listado";
			}
		}
		else if(request.getParameter("modificarEntregas") != null){
			
			LogSISPE.getLog().info("Cerrando el popUp de alerta de entregas proximas su fecha de despacho");
			request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
			salida = CotizarReservarAction.pasarAEntregas(request, formulario);
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
	
	/**
	 * Inicializa los par\u00E1metros de la reservaci\u00F3n y algunos datos en sesi\u00F3n y el formulario
	 * @param request
	 * @param formulario
	 * @throws Exception
	 */
	protected static void inicializarParametros(HttpServletRequest request, CotizarReservarForm formulario)throws Exception{
		
		HttpSession session = request.getSession();
		
		//se cargan los par\u00E1metros necesarios para la reservaci\u00F3n
		String codigoPorcentajeAbono = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.porcentajeAbonoInicial");
		String codigoPorcentajeCalculoFlete = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.porcentajeCalculoFlete");
		String codigoFecMinEntCD = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.fechaEntrega");
		String codigoFecMinDesLoc = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal");
	
		String [] codigosParametros = new String [] {codigoPorcentajeAbono,codigoPorcentajeCalculoFlete,codigoFecMinEntCD,codigoFecMinDesLoc};
		
		//se consultan los par\u00E1metros
		Collection<ParametroDTO> colParametroDTO = WebSISPEUtil.obtenerParametrosAplicacion(codigosParametros, request);
		
		if(colParametroDTO != null){
			for(ParametroDTO parametroDTO : colParametroDTO){
				if(parametroDTO.getId().getCodigoParametro().equals(codigoPorcentajeAbono)){
					//calculo del valor del abono m\u00EDnimo
					double porcentajeAbono = Double.parseDouble(parametroDTO.getValorParametro());
					Double totalFormateado = Util.roundDoubleMath(formulario.getTotal(),NUMERO_DECIMALES);
					double valorAbono = totalFormateado.doubleValue() * porcentajeAbono/100;
					//se formatea el valor del abono con dos decimales antes de almacenarlo
					Double valorAbonoFormateado = Util.roundDoubleMath(valorAbono,NUMERO_DECIMALES);
					//se almacena en sesi\u00F3n el porcentaje y el abono
					session.setAttribute(CotizarReservarAction.PORCENTAJE_ABONO,Double.valueOf(porcentajeAbono));
					session.setAttribute(CotizarReservarAction.VALOR_ABONO,valorAbonoFormateado);
					
				}else if(parametroDTO.getId().getCodigoParametro().equals(codigoPorcentajeCalculoFlete)){
					try{
						//se guarda el sesi\u00F3n el valor del par\u00E1metro para el porcentaje del c\u00E1lculo del flete
						session.setAttribute(CotizarReservarAction.PORCENTAJE_CALCULO_FLETE, Double.valueOf(parametroDTO.getValorParametro()));
					}catch(NumberFormatException ex){
						session.setAttribute(CotizarReservarAction.PORCENTAJE_CALCULO_FLETE, 0D);
						LogSISPE.getLog().info("error en el valor del par\u00E1metro: ",ex);
					}
				}else if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinEntCD) || parametroDTO.getId().getCodigoParametro().equals(codigoFecMinDesLoc)){
					
					//se crea un formato para la fecha
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
					Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
					int diasSumar = Integer.parseInt(parametroDTO.getValorParametro());
					if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinDesLoc)){
						diasSumar ++; //se suma un d\u00EDa porque el par\u00E1metro es para calcular la fecha m\u00EDnima de despacho
					}
					
					//para obtener la fecha m\u00EDnima de entrega se suman los d\u00EDas del par\u00E1metro
					Timestamp fechaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, diasSumar);
					//se formatea la fecha de entrega
					String minimaFechaEntrega = simpleDateFormat.format(fechaEntrega);
					
					if(parametroDTO.getId().getCodigoParametro().equals(codigoFecMinEntCD)){
						session.setAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES,minimaFechaEntrega);
						formulario.setFechaEntrega(minimaFechaEntrega);
					}else{
						session.setAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_LOC_RES,minimaFechaEntrega);
					}
				}
			}
		}
		
		//se obtiene la descripci\u00F3n del tipo de autorizaci\u00F3n gen\u00E9rico que permite crear una reservaci\u00F3n
//		TipoAutorizacionDTO tipoAutorizacionFiltro = new TipoAutorizacionDTO();
//		tipoAutorizacionFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		tipoAutorizacionFiltro.setCodigoInterno(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.crearReservacion"));
//		
//		Collection<TipoAutorizacionDTO> tipoAutorizacionDTOCol = SessionManagerSISPE.getCorpAutorizacionesServicio().obtenerTiposAutorizaciones(tipoAutorizacionFiltro);
//		tipoAutorizacionFiltro = null;
//		//se obtiene el \u00FAnico registro
//		if(tipoAutorizacionDTOCol != null && !tipoAutorizacionDTOCol.isEmpty()){
//			//se guarda la descripci\u00F3n en sesi\u00F3n
//			session.setAttribute(CotizarReservarAction.DESCRICPION_USO_AUTORIZACION, tipoAutorizacionDTOCol.iterator().next().getDescripcion());
//		}
		//Se inicializa el tipo de autorizacion generico
		AutorizacionesUtil.iniciarTipoAutorizacionGenerico(request);
	}
	
	/**
	 * 	
	 * @param request
	 */
	public static void instanciarPopUpDespachosProximos(HttpServletRequest request){
		
		String fechaMinimaDespacho = (String) request.getSession().getAttribute(EntregaLocalCalendarioUtil.FECHA_MINIMA_DESPACHO);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Alerta, entregas con fecha de despacho vencidas.");
		popUp.setPreguntaVentana("La fecha m\u00EDnima permitida para despachar desde el CD es: <b>"+fechaMinimaDespacho+
				"</b> y el pedido tiene entregas configuradas que no cumplen esta condici\u00F3n. Por favor configure nuevamente las entregas marcadas de color amarillo.");
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setValorOK("requestAjax('reservar.do',['mensajes','div_pagina'],{parameters: 'modificarEntregas=ok',evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		popUp.setAncho(Double.valueOf(50));
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		
		//se guarda la ventana
		request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
		request.getSession().removeAttribute(EntregaLocalCalendarioUtil.FECHA_MINIMA_DESPACHO);
	}
}
